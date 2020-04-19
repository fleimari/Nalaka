package com.example.nalaka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;


public class Arvostelusivu extends AppCompatActivity {

    VideoView videoPlayer;
    ImageView imageViewer;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arvostelusivu);

        videoPlayer = (VideoView) findViewById(R.id.videoView);
        imageViewer = (ImageView) findViewById(R.id.imageView);


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

    public void getImage(View v){

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







}


