package com.todo.ctof;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int status = 0;
    Button btnChange;
    TextView textC;
    TextView textF;
    TextView t;
    EditText input;
    TextView out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = 0;
        btnChange = (Button) findViewById(R.id.button);
        textC = (TextView) findViewById(R.id.textViewC);
        textF = (TextView) findViewById(R.id.textViewF);
        t = (TextView) findViewById(R.id.textView2);
        input = (EditText) findViewById(R.id.editText);
        out = (TextView) findViewById(R.id.textView6);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!CheckNum(s)){
                    if(s.length()>1) {
                        input.setText(s.subSequence(0, s.length() - 1));
                    }
                    if(s.length()==1){
                        input.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                float input1 = 0;
                if(s.length()<=0) {
                    out.setText("");
                    return;
                }
                try{
                    input1 = Float.parseFloat(String.valueOf(input.getText()));
                }catch(NumberFormatException ex){
                    input.setText("0");
                }
                float output;
                if(status==0){
                    output = CtoF(input1);
                }else{
                    output = FtoC(input1);
                }
                out.setText(String.format("%.2f",output).replace(',','.'));
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable inputText = input.getText();
                String c1 = "F";
                String c2 = "C";
                if(status==1){
                    status=0;
                    c1="C";
                    c2="F";
                }else{
                    status=1;
                    c1="F";
                    c2="C";
                }
                textC.setText(c1);
                textF.setText(c2);
                Editable s1 = input.getText();
                CharSequence s2 = out.getText();
                input.setText(s2);
                out.setText(String.valueOf(s1));
            }
        });
    }
    public static boolean CheckNum(CharSequence text){
        try{
            Float.parseFloat(String.valueOf(text));
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public float CtoF(float a){
        return (float) (a*1.8+32);
    }
    public float FtoC(float a){
        return (a-32)/(float) 1.8;
    }
}