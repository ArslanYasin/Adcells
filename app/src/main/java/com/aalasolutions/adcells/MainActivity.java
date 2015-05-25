package com.aalasolutions.adcells;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends Activity {

    private String url1 = "http://adcells.com/public/uploads/ads/2.jpg";
    URL url;
    Uri uri;

    private EditText location,country,temperature,humidity,pressure;
    ImageView image;
    private HandleJSON obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.img);
        try {
            url = new URL(url1);
            uri = Uri.parse( url.toURI().toString() );
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        image.setImageURI(uri);
        image.setImageBitmap(getBitmapFromURL(url1));

    }
    public static Bitmap getBitmapFromURL(String src) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("src", src);
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    Log.e("Bitmap", "returned");
                    return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Exception", e.getMessage());
                    return null;
                }
            }
        });
        thread.start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items
        //to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void open(View view){
        String url = location.getText().toString();
        String finalUrl = url1 + url;
        country.setText(finalUrl);
        obj = new com.aalasolutions.adcells.HandleJSON(finalUrl);
        obj.fetchJSON();

        while(obj.parsingComplete);
        country.setText(obj.getCountry());
        temperature.setText(obj.getTemperature());
        humidity.setText(obj.getHumidity());
        pressure.setText(obj.getPressure());

    }
}