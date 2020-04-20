package com.example.nalaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class luoArvostelu extends AppCompatActivity implements View.OnClickListener {

    Button btnKamera;
    ImageView ivAnnos;
    Spinner spinnerKaupunki, spinnerRavintola, spinnerTags;
    ArrayAdapter<CharSequence> adapterKaupungit, adapterRavintolat, adapterTags;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage; // kuvan liittämiseen jo valmiiksi reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luo_arvostelu);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Realtime tietokanta reference

        // Storage tietokanta reference
        //storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReference();

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

        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.search_img_btn).setOnClickListener(this);
        findViewById(R.id.logo_btn).setOnClickListener(this);
        findViewById(R.id.menu_img_btn).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button2){
            lisaaArvostelu();
        }
        if (v.getId() == R.id.search_img_btn) {
            Intent intentHakuActivity = new Intent(this, HakuActivity.class);
            startActivity(intentHakuActivity);
        }
        if (v.getId() == R.id.logo_btn){
            Intent intentPaasivu = new Intent (this, Paasivu.class);
            startActivity(intentPaasivu);
        }
        if (v.getId() == R.id.menu_img_btn) {
            PopupMenu popup = new PopupMenu(luoArvostelu.this, findViewById(R.id.menu_img_btn));
            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()) {
                        case R.id.three:
                            Toast.makeText(luoArvostelu.this, "Farewell", Toast.LENGTH_SHORT).show();
                            finish();
                            return true;
                        case R.id.two:
                            Toast.makeText(luoArvostelu.this, "Nothing here sorry", Toast.LENGTH_LONG).show();
                            return true;
                        case R.id.one:
                            Intent intentPaasivu = new Intent (luoArvostelu.this, Paasivu.class);
                            startActivity(intentPaasivu);
                            Toast.makeText(luoArvostelu.this, "Pääsivu", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
            });
            popup.show();
        }

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


    public void lisaaArvostelu(){

        // Siinä on nyt asetettu arvot mutta eihän se ole kuin lukea nuista kentistä ja korvata arvot niillä
        String var = mDatabase.push().getKey();
        String kaupunki = "Tampere";
        String kuvaUrl = "https://img.devrant.com/devrant/rant/r_1973724_9QTSY.jpg";
        String otsikko = "Da best";
        String peukut = "19";
        String pisteet = "5";
        String ravintola = "Tampereen kepappila";
        String teksti = "Hjuva paikka tulla sina kaumaan";
        String user = "Pekka";
        String videoUrl = "https://www.youtube.com/watch?v=iL7nX9W3aOU";

        mDatabase.child("Arvostelut").child(var).child("Kaupunki").setValue(kaupunki);
        mDatabase.child("Arvostelut").child(var).child("KuvaUrl").setValue(kuvaUrl);
        mDatabase.child("Arvostelut").child(var).child("Otsikko").setValue(otsikko);
        mDatabase.child("Arvostelut").child(var).child("Peukut").setValue(peukut);
        mDatabase.child("Arvostelut").child(var).child("Pisteet").setValue(pisteet);
        mDatabase.child("Arvostelut").child(var).child("Ravintola").setValue(ravintola);
        mDatabase.child("Arvostelut").child(var).child("Teksti").setValue(teksti);
        mDatabase.child("Arvostelut").child(var).child("User").setValue(user);
        mDatabase.child("Arvostelut").child(var).child("VideoUrl").setValue(videoUrl);

        // Tähän pitää varmaan rakentaa joku for-looppi että osaa tehdä sen verran komentoja kuin halutaan lisätä tageja.
        String tag1 = "Kiinalainen";
        String tag2 = "Japanilainen";
        mDatabase.child("Arvostelut").child(var).child("Tags").push().setValue(tag1);
        mDatabase.child("Arvostelut").child(var).child("Tags").push().setValue(tag2);
    }

    public void lisaaTagi(){

        // Yksi rivi lisää aina yhden tagin Tags-tauluun
        mDatabase.child("Tags").push().setValue("Piksa");
        //mDatabase.child("Tags").push().setValue("Kepapo");
        //mDatabase.child("Tags").push().setValue("Italialainen");
        //mDatabase.child("Tags").push().setValue("Grillimättö");
        //mDatabase.child("Tags").push().setValue("Kiinalainen");
    }

    public void lisaaKaupunki(){

        String kaupunki = "Muhos";
        String ravintola = "Muhoksen piksaKepapo";

        mDatabase.child("Kaupungit").child(kaupunki).push().setValue(ravintola);

    }


}
