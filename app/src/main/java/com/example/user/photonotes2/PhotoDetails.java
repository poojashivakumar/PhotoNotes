package com.example.user.photonotes2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class PhotoDetails extends AppCompatActivity {
    String value, value1;
    Bitmap takenImage;

   // public final String APP_TAG = "MyCustomApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            value = extras.getString("clicked");
            TextView captionDisplay = (TextView) findViewById(R.id.captionView);
            captionDisplay.setText(value);

            value1 = extras.getString("Photo");
            Uri takenPhotoUri = Uri.parse(value1);

            takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
            int h = 550;
            int w = 600;
            Bitmap scaled = Bitmap.createScaledBitmap(takenImage, h, w, true);

            ImageView ivPreview = (ImageView) findViewById(R.id.imageView);
            ivPreview.setImageBitmap(scaled);
           // ivPreview.setRotation(270);
        }
    }

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
}




