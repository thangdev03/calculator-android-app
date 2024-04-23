package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.mariuszgromada.math.mxparser.Expression;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView editTextText;
    private AppDatabase db;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextText = findViewById(R.id.textViewResult);
        setButtonClickListeners();


        // Dành cho Room Persistent Library
//        db = AppDatabase.getDatabase(getApplicationContext());
//
//        new Thread(() -> {
//            Answer answer = db.answerDAO().getById(1);
//            showLastAnswer(answer);
//        }).start();

        // Dành cho bộ nhớ riêng của ứng dụng (Ghi file)
//        try {
//            FileInputStream fis = openFileInput("data.txt");
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader bufferedReader = new BufferedReader(isr);
//            StringBuilder sb = new StringBuilder();
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                sb.append(line);
//                }
//            fis.close();
//            editTextText.setText(sb);
//            } catch (IOException e) {
//            e.printStackTrace();
//            }

        // Dành cho bộ nhớ Shared Preferences
        pref = getPreferences(MODE_PRIVATE);
        String lastResult = pref.getString("ketqua", String.valueOf(0));
        editTextText.setText(lastResult);

    }

    private void showLastAnswer(Answer answer) {
        Type type = new TypeToken<String>() {
        }.getType();
        String answerSaved = new Gson().fromJson(String.valueOf(answer.answerCalculated), type);
        editTextText.setVisibility(View.VISIBLE);

        editTextText.setText(answerSaved);
    }

    private void setButtonClickListeners() {
        int[] buttonIds = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.buttonDot, R.id.buttonAC, R.id.buttonAdd, R.id.buttonSubtract, R.id.buttonMultiply, R.id.buttonDivide, R.id.buttonPercent, R.id.buttonParentheses, R.id.buttonEqual, R.id.buttonBack};
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            button.setOnClickListener(view -> onButtonClick(view));
        }
    }

    private void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        switch (buttonText) {
            case "=":
                calculateResult();
                break;
            case "()":
                handleParentheses();
                break;
            case "⌫":
                removeLastInput();
                break;
            case "AC":
                clearInput();
                break;
            default:
                appendInput(buttonText);
                break;
        }
    }

    private void appendInput(String input) {
        editTextText.setText(editTextText.getText().toString() + input);
    }

    private void removeLastInput() {
        String s = editTextText.getText().toString();
        if (s.length() > 0) {
            editTextText.setText(s.substring(0, s.length() - 1));
        }
    }

    private void clearInput() {
        editTextText.setText("");
    }

    private boolean isOpenParentheses = false;
    private void handleParentheses() {
        if (isOpenParentheses) {
            appendInput(")");
            isOpenParentheses = false;
        } else {
            appendInput("(");
            isOpenParentheses = true;
        }
    }

    private void calculateResult() {
        try {
            String expression = editTextText.getText().toString();
            Expression expressionEval = new Expression(expression);
            double result = expressionEval.calculate();
            editTextText.setText(String.valueOf(result));

            // Lưu vào Room Persistent Library
//            new Thread(() -> {
//                Answer answer = db.answerDAO().getById(1);
//                if (answer != null) {
//                    answer.answerCalculated = result;
//                    db.answerDAO().update(answer);
//                } else {
//                    Answer newAnswer = new Answer();
//                    newAnswer.answerCalculated = result;
//                    db.answerDAO().insert(newAnswer);
//                }
//            }).start();

            // Ghi vào file dành riêng cho ứng dụng
//            String fileContents = String.valueOf(result);
//            try {
//                FileOutputStream fos = openFileOutput("data.txt", MODE_PRIVATE);
//                fos.write(fileContents.getBytes());
//                } catch (IOException e) {
//                throw new RuntimeException(e);
//                }

            // Ghi dữ liệu vào SharedPreferences
            pref.edit().putString("ketqua", String.valueOf(result)).apply();

        } catch (Exception e) {
            editTextText.setText("Error");
        }
    }
}