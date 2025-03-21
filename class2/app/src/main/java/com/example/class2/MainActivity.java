package com.example.class2;

import static com.example.class2.R.layout.activity_main;

import android.os.Bundle;
import android.os.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
   TextView cityname;
    Button search;
    TextView show;
     String url;

     class getWeather extends AsyncTask<String, Void, String>{
         @Override
         protected String doInBackground(String... urls){
             StringBuilder result = new StringBuilder();
             try {
                 URL url = new URL(urls[0]);
                 HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                 urlConnection.connect();

                 InputStream inputStream = urlConnection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


                 String line = "";
                 while((line = reader.readLine()) != null) {
                     result.append(line).append("\n");
                 }
                 return result.toString();
             }catch (Exception e){
                 e.printStackTrace();
                 return null;
                 }
             }
             @Override
         protected void onPostExecute(String results){
             super.onPostExecute(results);
             try {
                     JSONObject jsonObject = new JSONObject(results);
                     String weatherInfo = jsonObject.getString("main");
                    weatherInfo = weatherInfo.replace("temp" , "Temparature");
                 weatherInfo = weatherInfo.replace("feels_like" , "feels like");
                 weatherInfo = weatherInfo.replace("temp_max" , "Temparature max");
                 weatherInfo = weatherInfo.replace("temp_min" , "Temparature min");
                 weatherInfo = weatherInfo.replace("pressure" , "Pressure");
                 weatherInfo = weatherInfo.replace("humidity" , "Humidity");
                 weatherInfo = weatherInfo.replace("{" , "");
                 weatherInfo = weatherInfo.replace("}" , "");
                 weatherInfo = weatherInfo.replace("," , "\n");
                 weatherInfo = weatherInfo.replace(":" , ":");
                 show.setText(weatherInfo);
                 }catch(Exception e){
                 e.printStackTrace();

                 }
         }
     }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(activity_main);
        cityname = findViewById(R.id.Cityname);
        search = findViewById(R.id.search);
        show  = findViewById(R.id.Weather);


        final String[] temp={""};


        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Button clicked!", Toast.LENGTH_SHORT).show();
                String city = cityname.getText().toString();
                try {
                    if(city!=null){
                        url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=a28d0901b5a80f35c96a29dfa2ba689f";

                    }else {
                        Toast.makeText(MainActivity.this,"Enter city", Toast.LENGTH_SHORT).show();
                    }
                    getWeather task= new getWeather();
                    temp[0] = task.execute(url).get();

                }catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (temp[0] == null){
                    show.setText("cannot able to find weather");
                }
            }



        });
    }
}