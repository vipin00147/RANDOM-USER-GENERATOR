package com.example.gettingimageusingrandomapi;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    String url ;
    TextView Name,Email,Phone;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        btn = findViewById(R.id.next);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://randomuser.me/api/", null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray array = response.getJSONArray("results");
                                    JSONObject object = array.getJSONObject(0);
                                    JSONObject object1 = object.getJSONObject("picture");
                                    url = object1.getString("large").toString();
                                    Log.d("output",object1.getString("large").toString());

                                    ShowImage obj = new ShowImage(imageView);
                                    obj.execute(url);
                                    JSONObject name = object.getJSONObject("name");
                                    Name.setText("Name : "+name.getString("first").toString());
                                    Email.setText("Email : "+object.getString("email").toString());
                                    Phone.setText("Phone : "+object.getString("phone").toString());
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("output",error.toString());
                            }
                        });
                requestQueue.add(jsonObjectRequest);
            }
        });
    }

    private class ShowImage extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        ShowImage(ImageView imageView1) {
            this.imageView = imageView1;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                URL url1 = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}