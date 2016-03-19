package ua.test.wantedy.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    private FilmList mFilmList = FilmList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        mFilmList.showList(this, listView);


        //listView.setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          /*  HashMap<String, Object> itemHashMap = (HashMap<String, Object>) parent.getItemAtPosition(position);
            String titleItem = itemHashMap.get(TITLE).toString();
            String descriptionItem = itemHashMap.get(DESCRIPTION).toString();
            int imageItem = (int)itemHashMap.get(ICON);
            Toast.makeText(getApplicationContext(),
                    "Вы выбрали " + titleItem + ". Он " + descriptionItem, Toast.LENGTH_SHORT).show();
        */}
    };
}
