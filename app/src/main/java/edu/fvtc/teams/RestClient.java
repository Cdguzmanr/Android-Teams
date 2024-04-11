package edu.fvtc.teams;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class RestClient {
    public static final String TAG = "RestClient";
    public static void execGetOneRequest(String url,
                                         Context context,
                                         VolleyCallback volleyCallback)
    {
        Log.d(TAG, "execGetOneRequest: Start");
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ArrayList<Team> teams = new ArrayList<Team>();
        Log.d(TAG, "execGetOneRequest: " + url);

        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "onResponse: " + response);

                            try {
                                JSONObject object = new JSONObject(response);
                                Team team = new Team();
                                team.setId(object.getInt("id"));
                                team.setName(object.getString("name"));
                                team.setCity(object.getString("city"));
                                team.setRating((float)object.getDouble("rating"));
                                team.setCellPhone(object.getString("cellNumber"));
                                team.setIsFavorite(object.getBoolean("isFavorite"));

                                //team.setLatitude(object.getDouble("latitude"));
                                //team.setLongitude(object.getDouble("longitude"));

                                //String jsonPhoto = object.getString("photo");

                                //if(jsonPhoto != null)
                                //{
                                //    byte[] bytePhoto = null;
                                //    bytePhoto = Base64.decode(jsonPhoto, Base64.DEFAULT);
                                //    Bitmap bmp = BitmapFactory.decodeByteArray(bytePhoto, 0, bytePhoto.length);
                                //    team.setPhoto(bmp);
                                //}

                                teams.add(team);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            volleyCallback.onSuccess(teams);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "onErrorResponse: " + error.getMessage());
                        }
                    });

            // Important!!!
            requestQueue.add(stringRequest);

        } catch (Exception e) {
            Log.d(TAG, "execGetOneRequest: Error" + e.getMessage());
        }
    }
}
