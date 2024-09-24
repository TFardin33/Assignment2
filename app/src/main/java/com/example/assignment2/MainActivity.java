package com.example.assignment2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.R;

import java.util.ArrayList;

public class StatusBar extends AppCompatActivity {

    private CheckBox checkFruits, checkVegetables, checkDairy;
    private RadioGroup radioGroupCategory;
    private RadioButton radioOrganic, radioNonOrganic;
    private RatingBar ratingBarFreshness;
    private SeekBar seekBarQuantity;
    private TextView quantityValue;
    private Switch switchDiscount;
    private Button buttonSubmit;

    private ArrayList<String> selectedItems = new ArrayList<>();
    private int selectedQuantity = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_bar);

        // Initialize UI elements
        checkFruits = findViewById(R.id.check_fruits);
        checkVegetables = findViewById(R.id.check_vegetables);
        checkDairy = findViewById(R.id.check_dairy);
        radioGroupCategory = findViewById(R.id.radio_group_category);
        ratingBarFreshness = findViewById(R.id.rating_bar_freshness);
        seekBarQuantity = findViewById(R.id.seek_bar_quantity);
        quantityValue = findViewById(R.id.quantity_value);
        switchDiscount = findViewById(R.id.switch_discount);
        buttonSubmit = findViewById(R.id.button_submit);

        // Update quantity when SeekBar is changed
        seekBarQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedQuantity = progress;
                quantityValue.setText("Quantity: " + selectedQuantity);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Button to submit the grocery list
        buttonSubmit.setOnClickListener(v -> showOrderSummary());
    }

    private void showOrderSummary() {
        // Clear previously selected items
        selectedItems.clear();

        // Collect selected items
        if (checkFruits.isChecked()) selectedItems.add(checkFruits.getText().toString());
        if (checkVegetables.isChecked()) selectedItems.add(checkVegetables.getText().toString());
        if (checkDairy.isChecked()) selectedItems.add(checkDairy.getText().toString());

        // Get selected category
        int selectedCategoryId = radioGroupCategory.getCheckedRadioButtonId();
        String selectedCategory = null;
        if (selectedCategoryId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedCategoryId);
            selectedCategory = selectedRadioButton.getText().toString();
        } else {
            Toast.makeText(this, "Please select a category!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Inflate the custom summary dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_order_summary, null);
        TextView summaryTitle = dialogView.findViewById(R.id.summary_title);
        TextView itemsText = dialogView.findViewById(R.id.items_text);
        TextView categoryText = dialogView.findViewById(R.id.category_text);
        TextView freshnessText = dialogView.findViewById(R.id.freshness_text);
        TextView quantityText = dialogView.findViewById(R.id.quantity_text);
        TextView discountText = dialogView.findViewById(R.id.discount_text);
        Button okButton = dialogView.findViewById(R.id.ok_button);

        // Settin the summary detailsv
        summaryTitle.setText("Grocery Order Summary");
        itemsText.setText("Selected Items: " + selectedItems.toString());
        categoryText.setText("Category: " + selectedCategory);
        freshnessText.setText("Freshness Rating: " + ratingBarFreshness.getRating());
        quantityText.setText("Quantity: " + selectedQuantity);
        discountText.setText("Include Discounts: " + (switchDiscount.isChecked() ? "Yes" : "No"));

        // Create and show the summary dialog
        AlertDialog orderSummaryDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        okButton.setOnClickListener(v -> {
            orderSummaryDialog.dismiss();
            resetOrder();
        });

        orderSummaryDialog.show();
    }

    private void resetOrder() {
        checkFruits.setChecked(false);
        checkVegetables.setChecked(false);
        checkDairy.setChecked(false);
        radioGroupCategory.clearCheck();
        ratingBarFreshness.setRating(0);
        seekBarQuantity.setProgress(0);
        quantityValue.setText("Quantity: 0");
        switchDiscount.setChecked(false);
        Toast.makeText(getApplicationContext(), "Order reset!", Toast.LENGTH_SHORT).show();
    }
}