package com.example.nalaka;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;


public class Arvostelusivu extends AppCompatActivity implements View.OnClickListener {

    VideoView videoPlayer;
    ImageView imageViewer;
    int i = 0;
    ArvosteluClass arvostelutiedot;
    String kuvaURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arvostelusivu);


        videoPlayer = (VideoView) findViewById(R.id.videoView);
        imageViewer = (ImageView) findViewById(R.id.imageView);
        
        findViewById(R.id.search_img_btn).setOnClickListener(this);
        findViewById(R.id.logo_btn).setOnClickListener(this);
        findViewById(R.id.menu_img_btn).setOnClickListener(this);

        


        //arvostelutiedot = (ArvosteluClass) getIntent().getSerializableExtra("Arvostelu");
        //kuvaURL = arvostelutiedot.getKuvaUrl();


        String videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4";
        //String videoUrl = "https://firebasestorage.googleapis.com/v0/b/eighth-anvil-272013.appspot.com/o/testi.mp4?alt=media&token=c8e085b3-30ff-4c75-8dd2-cb33e2ea5eb1";

        Uri viUri = Uri.parse(videoUrl);
        videoPlayer.setVideoURI(viUri);

        MediaController mediaController = new MediaController(this);
        videoPlayer.setMediaController(mediaController);
        mediaController.setAnchorView(videoPlayer);

        new DownloadImageTask(imageViewer)
                .execute("https://www.worldatlas.com/r/w728-h425-c728x425/upload/06/06/04/shutterstock-591122330.jpg");
                //.execute("https://firebasestorage.googleapis.com/v0/b/eighth-anvil-272013.appspot.com/o/It%27s%20me.png?alt=media&token=bb1db93d-2007-4bcc-8fe0-66b8f4d271c0");

    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlImage = urls[0];
            Bitmap picImage = null;
            try {
                InputStream in = new java.net.URL(urlImage).openStream();
                picImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error to load image", e.getMessage());
                e.printStackTrace();
            }
            return picImage;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void changeView(View v) {
        i++;
        if (i == 1) {
            videoPlayer.setVisibility(View.VISIBLE);
            imageViewer.setVisibility(View.INVISIBLE);
        }

        if (i == 2) {
            i = 0;
            videoPlayer.setVisibility(View.INVISIBLE);
            imageViewer.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_img_btn) {
            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
        if (v.getId() == R.id.logo_btn){
            Intent intentPaasivu = new Intent (this, Paasivu.class);
            startActivity(intentPaasivu);
        }
        if (v.getId() == R.id.menu_img_btn) {
            PopupMenu popup = new PopupMenu(Arvostelusivu.this, findViewById(R.id.menu_img_btn));
            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.three:
                            Toast.makeText(Arvostelusivu.this, "Farewell", Toast.LENGTH_SHORT).show();
                            finish();
                            return true;
                        case R.id.two:
                            Toast.makeText(Arvostelusivu.this, "Nothing here sorry", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.one:
                            Intent intentPaasivu = new Intent (Arvostelusivu.this, Paasivu.class);
                            startActivity(intentPaasivu);
                            Toast.makeText(Arvostelusivu.this, "Pääsivu", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
        }
    }

}


