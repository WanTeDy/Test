package ua.test.wantedy.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by user on 20.03.2016.
 */
public class ViewFragment extends Fragment {

    static final String POSITION = "position";
    static final String LIST = "list";

    int mPosition;
    FilmList mFilmList;
    ImageView mImageView;

    static ViewFragment newInstance(int position, Parcelable obj) {
        ViewFragment viewFragment = new ViewFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(POSITION, position);
        arguments.putParcelable(LIST, obj);
        viewFragment.setArguments(arguments);
        return viewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(POSITION);
        mFilmList = (FilmList) getArguments().getParcelable(LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_fragment, null);
        HashMap<String, Object> map = mFilmList.getList().get(mPosition);

        TextView textView1 = (TextView) view.findViewById(R.id.ftext1);
        textView1.setText((String) map.get(FilmModel.sNAME));

        TextView textView2 = (TextView) view.findViewById(R.id.ftext2);
        textView2.setText((String) map.get(FilmModel.sTIME));

        String desc = "Description:\n" + map.get(FilmModel.sDESC);
        TextView textView3 = (TextView) view.findViewById(R.id.ftext3);
        textView3.setText(desc);

        mImageView = (ImageView) view.findViewById(R.id.fimage);
        if (!map.containsKey(FilmModel.sTEMP)) {
            OneImageLoaderTask oneImageLoaderTask = new OneImageLoaderTask();
            oneImageLoaderTask.execute(map);
        } else {
            mImageView.setImageURI(Uri.parse((String) map.get(FilmModel.sTEMP)));
        }
        return view;
    }

    private class OneImageLoaderTask extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

        @Override
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... hm) {

            InputStream iStream = null;
            String imgUrl = (String) hm[0].get(FilmModel.sURL);
            int position = (Integer) hm[0].get("position");

            URL url;
            try {
                url = new URL(imgUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                File cacheDirectory = getActivity().getApplicationContext().getCacheDir();
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

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            if (result != null) {
                String path = (String) result.get("img");
                int position = (Integer) result.get("position");

                HashMap<String, Object> hm = mFilmList.getList().get(position);
                hm.put("img", path);
                mImageView.setImageURI(Uri.parse((String) result.get(FilmModel.sTEMP)));
            }
        }
    }
}