package ua.test.wantedy.test;


/**
 * Created by user on 16.03.2016.
 */
public class FilmModel {
    public static final String sNAME = "name";
    public static final String sURL = "imgUrl";
    public static final String sTIME = "time";
    public static final String sDESC = "desc";
    public static final String sItemId = "itemId";
    public static final String sTEMP = "img";

    private String mName;
    private String mTime;
    private String mImgUrl;
    private String mDescription;
    private int mItemId;

    public FilmModel (String name, String time, String imgUrl, String description, int itemId) {
        if(name == null || name == "") {
            name = "No name";
        }
        mName = name;
        mTime = time;
        if(imgUrl == null || imgUrl == "") {
            imgUrl = "http://s1.iconbird.com/ico/0612/GooglePlusInterfaceIcons/w128h1281338911623help2.png";
        }
        mImgUrl = imgUrl;
        if(description == null || description == "") {
            description = "No description";
        }
        mDescription = description;
        mItemId = itemId;
    }

    public String getmName() {
        return mName;
    }

    public int getmItemId() {
        return mItemId;
    }

    public String getmImgUrl() {
        return mImgUrl;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmTime() {
        return mTime;
    }
}


