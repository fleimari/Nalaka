package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class luoArvostelu extends AppCompatActivity {

    Button btnKamera;
    ImageView ivAnnos;
    Spinner spinnerKaupunki, spinnerRavintola, spinnerTags;
    ArrayAdapter<CharSequence> adapterKaupungit, adapterRavintolat, adapterTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luo_arvostelu);

         btnKamera = (Button) findViewById(R.id.buttonLisaaKuva);
         ivAnnos = (ImageView) findViewById(R.id.annosKuva);
         spinnerKaupunki = (Spinner) findViewById(R.id.spinnerKaupunki);
         spinnerRavintola = (Spinner) findViewById(R.id.spinnerRavintola);
         spinnerTags = (Spinner) findViewById(R.id.spinnerTags);

        adapterKaupungit = ArrayAdapter.createFromResource(this, R.array.kaupungit, android.R.layout.simple_spinner_item);
        adapterKaupungit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKaupunki.setAdapter(adapterKaupungit);

        adapterRavintolat = ArrayAdapter.createFromResource(this,  R.array.ravintolat, android.R.layout.simple_spinner_item);
        adapterRavintolat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRavintola.setAdapter(adapterRavintolat);

        adapterTags = ArrayAdapter.createFromResource(this,  R.array.tags, android.R.layout.simple_spinner_item);
        adapterTags.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTags.setAdapter(adapterTags);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ivAnnos.setImageBitmap(bitmap);
    }

    public void otaKuva (View view){
        Intent intentKuva = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentKuva, 0);
    }

    public void otaVideo (View view){
        Intent intentVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intentVideo, 0);
    }

    public void valitseGalleria (View view){
        Intent intentGalleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intentGalleria.setType("image/* video/*");
        startActivityForResult(intentGalleria, 0);
    }
}
