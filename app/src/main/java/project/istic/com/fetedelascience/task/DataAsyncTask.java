package project.istic.com.fetedelascience.task;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import project.istic.com.fetedelascience.R;
import project.istic.com.fetedelascience.helper.DBManager;
import project.istic.com.fetedelascience.model.Event;

public class DataAsyncTask extends AsyncTask<Void, Double, Void> {

    private Context context;
    private DBManager manager;
    private OnDataLoaded listener;

    public DataAsyncTask(Context c, DBManager m, OnDataLoaded l) {
        this.context = c;
        this.manager = m;
        this.listener = l;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Gson gson = new GsonBuilder().create();
        JsonParser jsonParser = new JsonParser();

        InputStream inputStream = this.context.getResources().openRawResource(R.raw.data_2017);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONArray json = new JSONArray(line);

                // For each item event, get fields item and create Event model
                // from it to create new entry in database
                for (int i = 0; i < json.length(); i++) {
                    JSONObject one = json.getJSONObject(i);
                    JSONObject fields = one.getJSONObject("fields");
                    JsonElement item = jsonParser.parse(fields.toString());
                    String id = null;
                    try{
                        // on verifie que l'evenement comporte un id
                        id = (String) fields.get("identifiant");
                    }catch(JSONException e){

                    }
                    if(id != null && !id.equals("")){
                        try{
                            Integer.parseInt(id);
                            Event event = gson.fromJson(item, Event.class);
                            JSONArray geolocalisation=null;
                            try{
                                // on verifie que l'evenement comporte une geolocalisation
                                geolocalisation = fields.getJSONArray("geolocalisation");
                                event.setLatitude((Double) geolocalisation.get(0));
                                event.setLongitude((Double) geolocalisation.get(1));
                            }catch(JSONException e){

                            }


                            this.manager.createEvent(event);
                        } catch(NumberFormatException e) {

                        }
                    }

                    // update progress

                    Double current = (double) Math.round(((double)(100 * i+1) / json.length()) * 10 ) / 10;

                    publishProgress(current);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values[0]);
        listener.onDataProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        listener.onDataLoaded();
    }

}
