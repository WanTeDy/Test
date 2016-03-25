package ua.test.wantedy.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by user on 22.03.2016.
 */
public class ImageLoader {

    private ListView mListView;
    private Activity mActivity;
    private SimpleAdapter mSimpleAdapter;

    public ImageLoader(SimpleAdapter adapter, ListView view, Activity activity) {
        mListView = view;
        mActivity = activity;
        mSimpleAdapter = adapter;
    }

    public void imageStartLoad() {

        mListView.setAdapter(mSimpleAdapter);
        for (int i = 0; i < mSimpleAdapter.getCount(); i++) {
            HashMap<String, Object> hm = (HashMap<String, Object>) mSimpleAdapter.getItem(i);
            if (!hm.containsKey(FilmModel.sTEMP)) {
                hm.put("position", i);
            }
            ImageLoaderTask imageLoaderTask = new ImageLoaderTask();
            imageLoaderTask.execute(hm);
        }
    }

    private class ImageLoaderTask extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {

            if (!hm[0].containsKey(FilmModel.sTEMP)) {
                InputStream iStream = null;
                String imgUrl = (String) hm[0].get(FilmModel.sURL);
                int position = (Integer) hm[0].get("position");

                URL url;
                try {
                    url = new URL(imgUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    iStream = urlConnection.getInputStream();
                    File cacheDirectory = mActivity.getApplicationContext().getCacheDir();
                    File tmpFile = new File(cacheDirectory.getPath() + "/test_" + position + ".jpg");
                    FileOutputStream fOutStream = new FileOutputStream(tmpFile);
                    Bitmap b = BitmapFactory.decodeStream(iStream);
                    b.compress(Bitmap.CompressFormat.JPEG, 50, fOutStream);
                    fOutStream.flush();
                    fOutStream.close();
                    HashMap<String, Object> hmBitmap = new HashMap<>();
                    hmBitmap.put("img", tmpFile.getPath());
                    hmBitmap.put("position", position);
                    return hmBitmap;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            return hm[0];
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            if (result != null) {
                String path = (String) result.get("img");
                int position = (Integer) result.get("position");
                SimpleAdapter adapter = (SimpleAdapter) mListView.getAdapter();
                HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);
                hm.put("img", path);
                adapter.notifyDataSetChanged();
            }
        }
    }
}