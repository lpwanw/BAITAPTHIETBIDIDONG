package com.example.countryinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    ListView lvCountry;
    ArrayList<String> arrayCountry = new ArrayList<>();
    AutoCompleteTextView dropText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dropText =  (AutoCompleteTextView) findViewById(R.id.auto);
        new GetJSON().execute("http://api.geonames.org/countryInfoJSON?username=wanw");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, arrayCountry);
        dropText.setAdapter(adapter);
        dropText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nationName = String.valueOf(dropText.getText());
                int pos = 0;
                for(int i = 0;i<arrayCountry.size();i++){
                    if(nationName.equals(arrayCountry.get(i))){
                        pos = i;
                        break;
                    }
                }
                Intent intent = new Intent(MainActivity.this, InfoNational.class);
                intent.putExtra("national_name", arrayCountry.get(pos));
                startActivity(intent);
            }
        });
    }


    private class GetJSON extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Đang lấy dữ liệu...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String...strings) {
            StringBuilder content = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";

                while ( (line = bufferedReader.readLine()) != null ){
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
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonGeonames = jsonObject.getJSONArray("geonames");
                for (int i = 0; i < jsonGeonames.length(); i++){
                    JSONObject object = jsonGeonames.getJSONObject(i);
                    String countryName = object.getString("countryName");
                    arrayCountry.add(countryName);
                }

                lvCountry = (ListView) findViewById(R.id.national_list);
                ArrayAdapter adapterArray = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrayCountry);
                lvCountry.setAdapter(adapterArray);

                lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, InfoNational.class);
                        intent.putExtra("national_name", arrayCountry.get(position));
                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}