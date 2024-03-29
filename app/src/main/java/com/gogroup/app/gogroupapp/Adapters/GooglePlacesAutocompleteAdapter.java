package com.gogroup.app.gogroupapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.gogroup.app.gogroupapp.HelperClasses.RestClient;
import com.gogroup.app.gogroupapp.Responses.GooglePlaceApiResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;
    ArrayList resultApiList = null;
Context context;
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
//    private static final String API_KEY = "AIzaSyAWJ2Yn3q7Hy0QsC76Wo_EQUSD6xJocrYg";
//    private static final String API_KEY = "AIzaSyCy_Hbb2TX9SuPOAaXtUYq91s9wetY6FfE";
    private static final String API_KEY = "AIzaSyATd4VCHNZWR3IzJARzbs35Dsz36cDThfc";
        public GooglePlacesAutocompleteAdapter(Context context, int resource) {
            super(context, resource);
            this.context=context;
        }

        @NonNull
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Nullable
        @Override
        public Object getItem(int position) {
            return resultList.get(position);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }


    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
//            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            Log.d("placeApiUri",""+sb);
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return resultList;
        } catch (IOException e) {


            return resultList;

        } finally {

            if (conn != null) {

                conn.disconnect();

            }
        }
        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("ResponseAuto", jsonObj.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


            // Extract the Place descriptions from the results

            resultList = new ArrayList(predsJsonArray.length());

            for (int i = 0; i < predsJsonArray.length(); i++) {

                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));

                System.out.println("============================================================");

                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

            }

        } catch (JSONException e) {
        }
        return resultList;

    }

    public ArrayList checkGoogleAddress(String input) {


        RestClient.get().checkGooglePlaceApi(API_KEY,input).enqueue(new Callback<GooglePlaceApiResponse>() {
            @Override
            public void onResponse(Call<GooglePlaceApiResponse> call, Response<GooglePlaceApiResponse> response) {
                if (response.body() != null) {


                    Toast.makeText(context, ""+response.body().getPredictions().get(0).getDescription(), Toast.LENGTH_LONG).show();
                    resultApiList = new ArrayList(response.body().getPredictions().size());

                    for (int i = 0; i< resultApiList.size(); i++)
                    {
                        resultApiList.add(response.body().getPredictions().get(i).getPredictions());
                    }
                }
            }

            @Override
            public void onFailure(Call<GooglePlaceApiResponse> call, Throwable t) {
            }
        });
        return resultApiList;
    }


}