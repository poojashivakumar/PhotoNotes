package com.example.user.photonotes2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.Surface;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private ListView captionListView;
    Cursor cursor;

    private ListAdapter captionListAdapter;
    private ArrayList<PhotoNotesPojo> pojoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  int prevOrientation = getRequestedOrientation();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }*/
        //setRequestedOrientation(prevOrientation);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AddPhoto.class);
                startActivity(intent);


            }
        });



        captionListView = (ListView) findViewById(R.id.listView);
        captionListView.setOnItemClickListener(this);
        pojoArrayList = new ArrayList<PhotoNotesPojo>();
        captionListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, populateList());
        captionListView.setAdapter(captionListAdapter);
    }


    public List<String> populateList()
    {
        List<String> captionList = new ArrayList<String>();
        PhotoDbHelper openHelperClass = new PhotoDbHelper(this);
        SQLiteDatabase sqliteDatabase = openHelperClass.getReadableDatabase();

        String[] resultColumns = {PhotoDbHelper.CAPTION};
        cursor = sqliteDatabase.query(PhotoDbHelper.DATABASE_TABLE, resultColumns, null, null, null, null, null);
        while (cursor.moveToNext())
        {
            String caption = cursor.getString(cursor.getColumnIndex(PhotoDbHelper.CAPTION));
            PhotoNotesPojo pojoClass = new PhotoNotesPojo();
            pojoClass.setCaption(caption);
            pojoArrayList.add(pojoClass);
            captionList.add(caption);
        }
        sqliteDatabase.close();
        return captionList;
    }

    @Override
    public void onRestart()
    {
        super.onRestart();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        captionListAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, populateList());
        captionListView.setAdapter(captionListAdapter);

    }

    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        Intent addPhotoIntent = new Intent(this, PhotoDetails.class);
        PhotoDbHelper openHelperClass = new PhotoDbHelper(this);
        SQLiteDatabase sqliteDatabase = openHelperClass.getReadableDatabase();

        String[] resultColumns = {PhotoDbHelper.CAPTION};
        cursor = sqliteDatabase.query(PhotoDbHelper.DATABASE_TABLE, resultColumns, null, null, null, null, null);
        cursor.moveToPosition(arg2);
        String caption = cursor.getString(cursor.getColumnIndex(PhotoDbHelper.CAPTION));

        String[] imagePath = {PhotoDbHelper.COLUMN};
        cursor = sqliteDatabase.query(PhotoDbHelper.DATABASE_TABLE, imagePath, null, null, null, null, null);
        cursor.moveToPosition(arg2);
        String imageColumn = cursor.getString(cursor.getColumnIndex(PhotoDbHelper.COLUMN));

        Bundle dataBundle = new Bundle();
        dataBundle.putString("clicked", caption);
        dataBundle.putString("Photo", imageColumn);

        addPhotoIntent.putExtras(dataBundle);
        startActivity(addPhotoIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent removeApp = new Intent(Intent.ACTION_DELETE);
        removeApp.setData(Uri.parse("package:com.example.user.photonotes2"));
        startActivity(removeApp);
        return super.onOptionsItemSelected(item);
    }
}
