package com.example.coffeeordering;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void incrementQuantity(View view) {
        if (quantity == 100) {
            Toast.makeText(this, getString(R.string.more_than_100_error), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity += 1;
        displayQuantity();
    }

    public void decrementQuantity(View view) {
        if (quantity == 1) {
            Toast.makeText(this, getString(R.string.less_than_1_error), Toast.LENGTH_SHORT).show();
            return;
        }
        quantity -= 1;
        displayQuantity();
    }

    public void onSubmit(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text_view);
        String name = nameEditText.getText().toString();
        if (name.equals("")) {
            Toast.makeText(this, getString(R.string.enter_name_error), Toast.LENGTH_SHORT).show();
            return;
        }
        CheckBox hasWhippedCreamCheckBox = (CheckBox) findViewById(R.id.has_whipped_cream);
        boolean hasWhippedCream = hasWhippedCreamCheckBox.isChecked();
        CheckBox hasChocolateCheckBox = (CheckBox) findViewById(R.id.has_chocolate);
        boolean hasChocolate = hasChocolateCheckBox.isChecked();
        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String message = generateOrderSummary(name, hasWhippedCream, hasChocolate, price);
        sendEmail(message, name);
    }

    private void displayQuantity() {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int price = quantity * 5;
        if (hasWhippedCream) { price += quantity * 1; }
        if (hasChocolate) { price += quantity * 2; }
        return price;
    }

    private String generateOrderSummary(String name, boolean hasWhippedCream, boolean hasChocolate, int price) {
        String msg = getString(R.string.name_value, name);
        msg += "\n" + getString(R.string.whipped_cream_value, hasWhippedCream);
        msg += "\n" + getString(R.string.chocolate_value, hasChocolate);
        msg += "\n" + getString(R.string.quantity_value, quantity);
        msg += "\n" + getString(R.string.price_value, price);
        msg += "\n" + getString(R.string.thank_you);
        return msg;
    }

    private void sendEmail(String message, String name) {
        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ getString(R.string.email_id) });
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_for_email, name));
        email.putExtra(Intent.EXTRA_TEXT, message);
        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email.createChooser(email, getString(R.string.choose_email_client)));
        }
    }
}
