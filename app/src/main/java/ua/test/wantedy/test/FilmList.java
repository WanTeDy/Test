package ua.test.wantedy.test;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 16.03.2016.
 */
public class FilmList implements Parcelable {
    private static FilmList sInstance;
    private ArrayList<HashMap<String, Object>> mFilmList = new ArrayList<>();

    private FilmList() {
    }

    private FilmList(Parcel in) {
        sInstance = this;
        in.readList(mFilmList, null);
    }

    public void add(FilmModel filmModel) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(FilmModel.sNAME, filmModel.getmName());
        map.put(FilmModel.sURL, filmModel.getmImgUrl());
        map.put(FilmModel.sTIME, filmModel.getmTime());
        map.put(FilmModel.sDESC, filmModel.getmDescription());
        map.put(FilmModel.sItemId, filmModel.getmItemId());
        mFilmList.add(map);
    }

    public ArrayList<HashMap<String, Object>> getList() {
        return mFilmList;
    }

    public static FilmList getInstance() {
        if (sInstance == null)
            sInstance = new FilmList();
        return sInstance;
    }

    public static final Parcelable.Creator<FilmList> CREATOR
            = new Parcelable.Creator<FilmList>() {
        public FilmList createFromParcel(Parcel in) {
            return new FilmList(in);
        }

        public FilmList[] newArray(int size) {
            return new FilmList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mFilmList);
    }
}