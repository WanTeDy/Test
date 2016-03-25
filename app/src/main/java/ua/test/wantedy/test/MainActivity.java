package ua.test.wantedy.test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class MainActivity extends Activity {

    ListView mListView;
    FilmList mFilmList;
    EditText mEditText;
    ArrayList<HashMap<String, Object>> mFindList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listView);
        mEditText = (EditText) findViewById(R.id.editText);

        if (savedInstanceState == null) {
            mFilmList = FilmList.getInstance();
            if (mFilmList.getList().size() == 0) {
                String jsonUrl = "http://others.php-cd.attractgroup.com/test.json";
                DownloadJsonTask downloadJsonTask = new DownloadJsonTask();
                downloadJsonTask.execute(jsonUrl);
            } else {
                prepareSetAdapter(mFilmList.getList());
            }
        } else {
            mFilmList = (FilmList) getLastNonConfigurationInstance();
            prepareSetAdapter(mFilmList.getList());
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                if (mFindList != null) {
                    position = (Integer) mFindList.get(position).get("position");
                }
                intent.putExtra("FilmList", mFilmList);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String stringText = mEditText.getText().toString().toLowerCase();
                mFindList = mFilmList.getList();
                if (stringText != "") {
                    mFindList = new ArrayList<>();
                    for (HashMap<String, Object> map : mFilmList.getList()) {
                        String find = (String) map.get(FilmModel.sNAME);
                        if (find.toLowerCase().contains(stringText)) {
                            mFindList.add(map);
                        }
                    }
                }
                prepareSetAdapter(mFindList);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void prepareSetAdapter (ArrayList<HashMap<String, Object>> list) {
        String[] from = {FilmModel.sNAME, FilmModel.sTIME, FilmModel.sTEMP};
        int[] to = {R.id.text1, R.id.text2, R.id.image};
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), list, R.layout.list_view, from, to);
        mListView.setAdapter(adapter);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mFilmList;
    }

    private String downloadJsonUrl(String jsonUrl) {

        String data = "";
        InputStream iStream = null;

        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                iStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private class DownloadJsonTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... url) {
            if (url[0] != null) {
                String data = null;
                try {
                    data = downloadJsonUrl(url[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return data;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
                listViewLoaderTask.execute(result);
            }
        }
    }

    private class ListViewLoaderTask extends AsyncTask<String, Void, SimpleAdapter> {

        @Override
        protected SimpleAdapter doInBackground(String... strJson) {

            String string = strJson[0];
            try {
                JSONArray jsonArray = new JSONArray(string);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonfilm = jsonArray.getJSONObject(i);

                    String sItemId = jsonfilm.getString("itemId");
                    int itemId = 0;
                    itemId = Integer.parseInt(sItemId);
                    String name = jsonfilm.getString("name");
                    String imgUrl = jsonfilm.getString("image");
                    String description = jsonfilm.getString("description");
                    long time = jsonfilm.getLong("time");
                    Date date = new Date();
                    date.setTime(time);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                    String sTime = format.format(date);
                    mFilmList.add(new FilmModel(name, sTime, imgUrl, description, itemId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] from = {FilmModel.sNAME, FilmModel.sTIME, FilmModel.sTEMP};
            int[] to = {R.id.text1, R.id.text2, R.id.image};
            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), mFilmList.getList(), R.layout.list_view, from, to);

            return adapter;
        }

        @Override
        protected void onPostExecute(SimpleAdapter adapter) {

            ImageLoader imageLoader = new ImageLoader(adapter, mListView, MainActivity.this);
            imageLoader.imageStartLoad();
        }
    }
}