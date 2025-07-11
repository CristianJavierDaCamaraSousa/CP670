package com.example.androidassignments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_forecast);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProgressBar progBar = findViewById(R.id.progressBar);

        progBar.setVisibility(View.VISIBLE);

        new ForecastQuery().execute();

    }

    public class ForecastQuery extends AsyncTask<String, Integer, String> {
        String min, max, current, city;
        Bitmap bitmap;

        @Override
        protected String doInBackground(String... args) {
            InputStream in = null;
            String iconName = null;
            String api = "bb69bcf7888587621c140f23b1beac4d";
            city = getIntent().getStringExtra("CITY");
            try {
                String urlStr = "https://api.openweathermap.org/data/2.5/weather?q="+city +
                        ",ca&APPID=" + api + "&mode=xml&units=metric";
                Log.i("WeatherLog", "Query used: " + urlStr);
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                in = conn.getInputStream();

                // Parser
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String tagName = parser.getName();

                        if (tagName.equals("temperature")) {

                            current = parser.getAttributeValue(null, "value");
                            min = parser.getAttributeValue(null, "min");

                            max = parser.getAttributeValue(null, "max");
                            publishProgress(25);
                        }

                        if (tagName.equals("weather")) {
                            iconName = parser.getAttributeValue(null, "icon");
                            publishProgress(50);

                            String urlStr2 = "http://openweathermap.org/img/w/" + iconName + ".png";
                            URL urlImage = new URL(urlStr2);


                        }
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error during parsing", e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            String imageFile = iconName + ".png";
            Log.i("WeatherLog", "Searching  for the image: " + imageFile);

            if (fileExist(imageFile)) {
                Log.i("WeatherLog", "Image is stored locally, Load from storage");

                FileInputStream fis = null;
                try {
                    fis = openFileInput(imageFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeStream(fis);
            } else {
                Log.i("WeatherLog", "Image not stored locally, proceding to download from web");
                String imageURL = "https://openweathermap.org/img/w/" + imageFile;
                Bitmap image = getImage(imageURL);

                try {
                    FileOutputStream outputStream = openFileOutput(imageFile, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bitmap = image;
            }

            publishProgress(100);

            return null;
        }

        public boolean fileExist(String fileName){
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }
        private Bitmap getImage(String urlStr) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ProgressBar progBar = WeatherForecast.this.findViewById(R.id.progressBar);
            progBar.setVisibility(View.VISIBLE);
            progBar.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String result){

            TextView currentText = findViewById(R.id.textCurrentTemperature);
            TextView minText = findViewById(R.id.textMinTemperature);
            TextView maxText = findViewById(R.id.textMaxTemperature);
            ProgressBar bar = findViewById(R.id.progressBar);
            ImageView imageV = findViewById(R.id.imageWeather);
            TextView cityText = findViewById(R.id.textNameOfTheCity);

            currentText.setText("Current Temperature:" + this.current);
            minText.setText("MMin Temperature:" +this.min);
            maxText.setText("Max Temperature:" +this.max);
            cityText.setText("City: "+city);

            imageV.setImageBitmap(bitmap);
            bar.setVisibility(ProgressBar.INVISIBLE);
        }

    }

}