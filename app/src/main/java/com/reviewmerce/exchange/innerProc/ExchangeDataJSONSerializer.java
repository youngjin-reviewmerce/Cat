package com.reviewmerce.exchange.innerProc;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by onebuy on 2015-07-23.
 */
public class ExchangeDataJSONSerializer {
    private Context mContext;
    private String mFilename;

    public ExchangeDataJSONSerializer(Context c, String sFile) {
        mContext = c;
        mFilename = sFile;
    }

    public void saveData(ArrayList<ExchangeData> Datas)
            throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (ExchangeData e : Datas) {
            array.put(e.toJSON());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<ExchangeData> loadData() throws IOException, JSONException {
        ArrayList<ExchangeData> crimes = new ArrayList<ExchangeData>();
        BufferedReader reader = null;
        try {
            // open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // build the array of crimes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new ExchangeData(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.e("JSON", e.toString());
            // we will ignore this one, since it happens when we start fresh
        } finally {
            if (reader != null)
                reader.close();
        }
        return crimes;
    }

}
