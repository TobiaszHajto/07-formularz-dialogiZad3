
package com.example.formularz_dialogi;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private Button btnDate, btnSubmit, btnClear;
    private Spinner spDestination;
    private SeekBar sbPeople;
    private TextView tvPeople, tvSummary;
    private Switch swInsurance;
    private CheckBox cbTerms;

    private String departureDate = "";
    private FormState previousState;

    private static class FormState {
        String name;
        CharSequence nameError;
        int destination;
        int people;
        boolean insurance;
        boolean terms;
        String date;
        String summary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        btnDate = findViewById(R.id.btnDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnClear = findViewById(R.id.btnClear);
        spDestination = findViewById(R.id.spDestination);
        sbPeople = findViewById(R.id.sbPeople);
        tvPeople = findViewById(R.id.tvPeople);
        tvSummary = findViewById(R.id.tvSummary);
        swInsurance = findViewById(R.id.swInsurance);
        cbTerms = findViewById(R.id.cbTerms);

        setUpSpinner();
        setUpSeekBar();
        setUpListeners();
    }

    private void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.destinations, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spDestination.setAdapter(adapter);
    }

    private void setUpSeekBar() {
        tvPeople.setText("1");

        sbPeople.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tvPeople.setText(String.valueOf(progress + 1));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
    }

    private void setUpListeners() {
        cbTerms.setOnCheckedChangeListener((buttonView, isChecked) ->
                btnSubmit.setEnabled(isChecked));

        btnDate.setOnClickListener(v -> showDatePicker());

        btnSubmit.setOnClickListener(v -> {
            if (validateForm()) {
                askForConfirmation();
            }
        });

        btnClear.setOnClickListener(v -> askForClearConfirmation());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    departureDate = String.format(Locale.US, "%04d-%02d-%02d",
                            year, month + 1, dayOfMonth);

                    btnDate.setText(departureDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private boolean validateForm() {
        String name = etName.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Podaj imię i nazwisko");
            etName.requestFocus();
            return false;
        }

        if (spDestination.getSelectedItemPosition() == 0) {
            tvPeople.requestFocus();
            return false;
        }

        if (departureDate.isEmpty()) {
            btnDate.setError("Wybierz datę wyjazdu");
            return false;
        }

        return true;
    }

    private void askForConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Potwierdzenie")
                .setMessage("Czy na pewno chcesz zapisać się na wycieczkę?")
                .setPositiveButton("Tak", (dialog, which) -> submitForm())
                .setNegativeButton("Nie", null)
                .show();
    }

    private void submitForm() {
        tvSummary.setText(buildSummary());

        Snackbar.make(findViewById(R.id.main),
                        "Zapisano na wycieczkę",
                        Snackbar.LENGTH_LONG)
                .show();
    }

    private String buildSummary() {
        String insurance = swInsurance.isChecked() ? "Tak" : "Nie";

        return "PODSUMOWANIE ZAPISU\n\n"
                + "Imię i nazwisko: "
                + etName.getText().toString().trim()
                + "\nKierunek: "
                + spDestination.getSelectedItem().toString()
                + "\nLiczba osób: "
                + (sbPeople.getProgress() + 1)
                + "\nData wyjazdu: "
                + departureDate
                + "\nUbezpieczenie: "
                + insurance;
    }

    private void askForClearConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Wyczyść formularz")
                .setMessage("Czy na pewno chcesz wyczyścić wszystkie pola?")
                .setPositiveButton("Tak", (dialog, which) -> clearForm())
                .setNegativeButton("Nie", null)
                .show();
    }

    private void saveFormState() {
        previousState = new FormState();

        previousState.name = etName.getText().toString();
        previousState.nameError = etName.getError();
        previousState.destination = spDestination.getSelectedItemPosition();
        previousState.people = sbPeople.getProgress();
        previousState.insurance = swInsurance.isChecked();
        previousState.terms = cbTerms.isChecked();
        previousState.date = departureDate;
        previousState.summary = tvSummary.getText().toString();
    }

    private void clearForm() {
        saveFormState();

        etName.setText("");
        etName.setError(null);

        spDestination.setSelection(0);

        sbPeople.setProgress(0);

        swInsurance.setChecked(false);
        cbTerms.setChecked(false);

        departureDate = "";
        btnDate.setText("Wybierz datę");
        btnDate.setError(null);

        tvSummary.setText("");

        Snackbar.make(findViewById(R.id.main),
                        "Formularz został wyczyszczony",
                        Snackbar.LENGTH_LONG)
                .setAction("Cofnij", v -> restoreForm())
                .show();
    }

    private void restoreForm() {
        if (previousState == null) {
            return;
        }

        etName.setText(previousState.name);
        etName.setError(previousState.nameError);

        spDestination.setSelection(previousState.destination);

        sbPeople.setProgress(previousState.people);

        swInsurance.setChecked(previousState.insurance);
        cbTerms.setChecked(previousState.terms);

        departureDate = previousState.date;

        if (departureDate.isEmpty()) {
            btnDate.setText("Wybierz datę");
        } else {
            btnDate.setText(departureDate);
        }

        tvSummary.setText(previousState.summary);
    }
}
