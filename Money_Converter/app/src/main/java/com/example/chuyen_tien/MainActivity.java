package com.example.chuyen_tien;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> arrayList;
    static String url = "";
    AutoCompleteTextView dropdown1, dropdown2;
    ArrayAdapter<String> adapter;
    TextView textView;
    EditText InputMoney;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InputMoney = findViewById(R.id.input);
        arrayList = new ArrayList<>();
        dropdown1 = (AutoCompleteTextView) findViewById(R.id.auto1);
        dropdown2 = (AutoCompleteTextView) findViewById(R.id.auto2);
        textView = findViewById(R.id.output);
        new ReadHTML().execute("https://www.fxexchangerate.com/currency-converter-rss-feed.html");
        // Link trang web cần lấy dữ liệu
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrayList);
        dropdown1.setAdapter(adapter);
        dropdown2.setAdapter(adapter);
        dropdown1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dropdown1.showDropDown();
                }
            }
        });
        dropdown2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdown2.showDropDown();
                return false;
            }
        });
        dropdown2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dropdown2.showDropDown();
                }
            }
        });
        dropdown1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dropdown1.showDropDown();
                return false;
            }
        });
        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vt1 = dropdown1.getText()+"";
                String vt2 = dropdown2.getText()+"";

                creatDropDownForAutoCompleteText(vt2, vt1);


            }
        });
        findViewById(R.id.button_convert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = String.valueOf(dropdown1.getText()).split(" - ")[0];
                String s2 = String.valueOf(dropdown2.getText()).split(" - ")[0];
                url = "https://" + s1.toLowerCase() + ".fxexchangerate.com/" + s2.toLowerCase() + ".xml";
                if(!s1.equals(s2))
                new ReadRSS().execute(url);
            }
        });


    }
    public void creatDropDownForAutoCompleteText(String vt1, String vt2) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        dropdown1.setAdapter(adapter);
        dropdown2.setAdapter(adapter);
        dropdown1.setText(vt1);
        dropdown2.setText(vt2);

    }

    // Hàm chuyển đổi bình thường
    public float ConverterM(float money, float rate) {

        return money * rate;

    }

    // get convert
    private class ReadRSS extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Đang chuyển đổi...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();

            XMLDOMParser parser = new XMLDOMParser();
            Document document = null;
            try {
                document = parser.getDocument(s);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            NodeList nodeList = document.getElementsByTagName("description");
            String content = "";

            Element element = (Element) nodeList.item(1);

            content += parser.getCharacterDataFromElement(element);

            String[] output = content.split("<br/>");
            String s1 = output[0];

            output = s1.trim().split(" ");
            String convertedValue = output[3];

            Log.e("Hello",s1);
            textView.setText(s1.trim().replace('.',','));
            String input = InputMoney.getText().toString();

            if (input.isEmpty() || input.length() == 0 || input.equals("") || input == null) {
                Toast.makeText(MainActivity.this, "Bạn chưa nhập số", Toast.LENGTH_SHORT).show();


            } else {
                float i1 = Float.parseFloat(String.valueOf(InputMoney.getText()));
                float i2 = Float.valueOf(convertedValue);
                float conv = ConverterM(i1, i2);
                String in = String.valueOf(dropdown1.getText()).split(" - ")[0];
                String out = String.valueOf(dropdown2.getText()).split(" - ")[0];
                textView.append("\n" + formater(i1) + " " + in + " = " + formater(conv) + " " + out);

                Toast.makeText(MainActivity.this, String.valueOf(conv), Toast.LENGTH_SHORT).show();

            }


        }


    }
    public static String formater(int value) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(value);
    }public static String formater(float value) {
        DecimalFormat df = new DecimalFormat("###,###,###.000");
        String rs = df.format(value);
        String[] temp = rs.split(",");
        if(temp[0].equals("")){
            return "0"+rs;
        }
        return rs;
    }
    // hàm đọc html
    private class ReadHTML extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Đang lấy dữ liệu...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();

            org.jsoup.nodes.Document document = Jsoup.parse(s);
            org.jsoup.nodes.Element fxSidebarFrom = document.getElementById("fxSidebarFrom");
            // Lấy tag có id=fxSidebarFrom
            Elements inputElements = fxSidebarFrom.getElementsByTag("option");
            for (org.jsoup.nodes.Element inputElement : inputElements) {
                String moneyCode = inputElement.attr("value");
                // Lấy tất cả value của select option
                String Text = inputElement.text();
                arrayList.add(Text);

            }
            creatDropDownForAutoCompleteText(arrayList.get(0), arrayList.get(1));

        }
    }
}