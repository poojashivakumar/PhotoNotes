package com.example.user.photonotes2;

import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddPhoto extends AppCompatActivity implements View.OnClickListener {

    private EditText captionText;
    private Button saveButton;
    private Button takePhoto;
    private ArrayList photoNotesPojoObjArrayList;
    private String name;
    String providedCaption;

    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    Bitmap takenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        captionText = (EditText) findViewById(R.id.editText);
        saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener((View.OnClickListener) this);

        takePhoto = (Button) findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    onLaunchCamera(v);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        photoNotesPojoObjArrayList = new ArrayList();
    }

    private String createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }

    public void onLaunchCamera(View view) throws IOException
    {
       name = createImageFile();
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       if (intent.resolveActivity(getPackageManager()) != null)
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(name));
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Uri takenPhotoUri = getPhotoFileUri(name);
                takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
            }
            else
            {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Uri getPhotoFileUri(String fileName)
    {

        if (isExternalStorageAvailable())
        {
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs())
            {
                Log.d(APP_TAG, "failed to create directory");
            }
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    private boolean isExternalStorageAvailable()
    {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent removeApp = new Intent(Intent.ACTION_DELETE);
        removeApp.setData(Uri.parse("package:com.example.user.photonotes2"));
        startActivity(removeApp);
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {


        providedCaption = captionText.getText().toString();
        int length = providedCaption.length();
        Uri takenPhotoUri = getPhotoFileUri(name);
        String imageCaptured = takenPhotoUri.toString();
        if(length == 0 || name == null)
        {
            Toast.makeText(AddPhoto.this , "please enter both caption and image", Toast.LENGTH_SHORT).show();
            return;
        }

        PhotoNotesPojo pojoObj = new PhotoNotesPojo();
        pojoObj.setCaption(providedCaption);
        pojoObj.setImagePath(imageCaptured);

        photoNotesPojoObjArrayList.add(pojoObj);
        insert(pojoObj);
        onBackPressed();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AddPhoto.this, MainActivity.class));
        AddPhoto.this.finish();
        super.onStop();
    }

    public void insert(PhotoNotesPojo paraPojoObj)
    {
        PhotoDbHelper androidOpenDbHelperObj = new PhotoDbHelper(this);
        SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PhotoDbHelper.CAPTION, paraPojoObj.getCaption());
        contentValues.put(PhotoDbHelper.COLUMN, paraPojoObj.getImagePath());
        sqliteDatabase.insert(PhotoDbHelper.DATABASE_TABLE, null, contentValues);

        sqliteDatabase.close();

    }




}

