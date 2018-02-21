package project.istic.com.fetedelascience.task;

import android.content.Context;
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
                    Event event = gson.fromJson(item, Event.class);
                    Log.d("efdsf", event.toString());
                    this.manager.createEvent(event);
                    // update progress
                    Integer current = (100 * i) / json.length();
                    publishProgress(Double.valueOf(current));
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