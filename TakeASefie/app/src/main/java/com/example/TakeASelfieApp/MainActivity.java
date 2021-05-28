package com.example.TakeASelfieApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<File> files;
    protected Switch swt;
    public static final String CHANNEL_ID = "10001";
    private  static  final int REQUEST_IMAGE_CAPTURE=123;
    private EditText timeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        swt = (Switch) findViewById(R.id.swt);
        timeText = (EditText) findViewById(R.id.editTextNumber);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Intent i = new Intent(listView.getContext().getApplicationContext(), ViewImage.class);
                i.putExtra("FilePath", files.get(position).getAbsolutePath());
                listView.getContext().startActivity(i);

            }
        });
        swt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean check) {
                if (check) {
                    String stringTime = String.valueOf(timeText.getText());
                    int time = 5;
                    try {
                        time = (int) Integer.parseInt(stringTime);
                    }catch (NumberFormatException e){
                        time = 5;
                    }
                    Toast.makeText(MainActivity.this, "Confirm remind after "+ time + " seconds", Toast.LENGTH_LONG).show();
                    timeText.setEnabled(false);
                    NotificationReceiver.scheduleNotification(getApplicationContext(),5000);
                } else {
                    timeText.setEnabled(true);
                    NotificationReceiver.cancelNotification(getApplicationContext());
                    Toast.makeText(MainActivity.this, "NO", Toast.LENGTH_LONG).show();
                }
            }
        });
        files = new ArrayList<>();
        new LoadAsync().execute();
    }

    private void sendNotification() {
        //scheduleNotification(getApplicationContext(),1,"Bạn ơi","Bạn ơi");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camera_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.camera:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //https://stackoverflow.com/questions/37628580/camera-intent-returns-null-onactivityresult
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "MI_" + timeStamp + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
                photoFile = image;
            } catch (IOException ex) {
                Log.e("Image generation","IOEexx");

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    public class LoadAsync extends AsyncTask<File[],Void,Void> {

        public LoadAsync(){

        }

        @Override
        protected Void doInBackground(File[]... files) {
            ArrayList<File> files2 = new ArrayList<>();
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            files2 = getFile(storageDir);
            ImageAdapter adapter = new ImageAdapter(MainActivity.this,R.layout.single_item,files2);
            listView.setAdapter(adapter);
            return null;
        }
        private void onPostExecute(){
            display();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        display();
        super.onActivityResult(requestCode, resultCode, data);
    }
    private ArrayList<File> getFile(File file){
        ArrayList<File> listFile= new ArrayList<>();
        File[] images= file.listFiles();
        for(File filess:images){
            if (filess.isDirectory() && filess.isHidden()){
                listFile.addAll(getFile(filess));
            }else{
                if(filess.getName().endsWith(".jpg")||
                        filess.getName().endsWith(".png")){
                        listFile.add(filess);
                }
            }
        }
        return  listFile;
    }
    public void display(){
        files.clear();
        files = getFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        ImageAdapter adapter = new ImageAdapter(MainActivity.this,R.layout.single_item,files);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
    public class ImageAdapter extends BaseAdapter {
        public Context context;
        public int resources;
        public ArrayList<File> files;
        public LayoutInflater inflater;

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public ImageAdapter(Context context, int resources, ArrayList<File> files) {

            this.context = context;
            this.resources = resources;
            this.files = files;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public int getCount() {
            return files.size();
        }


        @Override
        public long getItemId(int position) {

            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View dataView = convertView;
            if (dataView == null) {
                dataView = inflater.inflate(R.layout.single_item, parent, false);
            }
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            //bmOptions.inJustDecodeBounds = true;
            //bmOptions.inSampleSize = 8;
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(files.get(position).getAbsolutePath(), bmOptions);
           ((ImageView) dataView.findViewById(R.id.image1)).setImageBitmap(bitmap);
            ((TextView) dataView.findViewById(R.id.txt1)).setText(files.get(position).getName());
            return dataView;
        }
    }


}