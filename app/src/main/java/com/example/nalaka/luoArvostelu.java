package com.example.nalaka;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class luoArvostelu extends AppCompatActivity implements View.OnClickListener {


    //Korjauskommentti

    Button btnKuva, btnVideo;
    ImageView picAnnos;
    VideoView vidAnnos;
    Spinner spinnerKaupunki, spinnerRavintola, spinnerTags;
    EditText otsikko,arvostelu;
    RatingBar tahdet;
    private static final int GALLERY_PICTURE_REQUEST = 1;
    private static final int GALLERY_VIDEO_REQUEST = 2;
    private Uri kuvaUri, videoUri, tiedostoUri;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage; // kuvan liittämiseen jo valmiiksi reference
    StorageReference storageRef;
    ArrayList<String> kaupunkiList;
    ArrayList<String> ravintolaList;
    ArrayList<String> tagiList;
    ArrayAdapter<String> kaupunkiAdapter,ravintolaAdapter,tagiAdapter;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luo_arvostelu);

        findViewById(R.id.search_img_btn).setOnClickListener(this);
        findViewById(R.id.logo_btn).setOnClickListener(this);
        findViewById(R.id.menu_img_btn).setOnClickListener(this);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // Realtime tietokanta reference
        kaupunkiList = new ArrayList<String>();
        ravintolaList = new ArrayList<String>();
        tagiList = new ArrayList<String>();
        ravintolaList.add("Valitse");

        kaupunkiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,kaupunkiList);
        kaupunkiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ravintolaAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ravintolaList);
        ravintolaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tagiAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tagiList);
        tagiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        queue = Volley.newRequestQueue(this);

        otsikko = findViewById(R.id.annoksenNimi);
        arvostelu = findViewById(R.id.etArvosteluTxt);
        tahdet = findViewById(R.id.ratingBar);
        //Storage tietokanta reference
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

         btnKuva = (Button) findViewById(R.id.buttonKuva);
         btnVideo = (Button) findViewById(R.id.buttonVideo);
         picAnnos = (ImageView) findViewById(R.id.annosKuva);
         vidAnnos = (VideoView) findViewById(R.id.annosVideo);

         spinnerKaupunki = (Spinner) findViewById(R.id.spinnerKaupunki);
         spinnerRavintola = (Spinner) findViewById(R.id.spinnerRavintola);
         spinnerTags = (Spinner) findViewById(R.id.spinnerTags);


         spinnerKaupunki.setAdapter(kaupunkiAdapter);
         spinnerRavintola.setAdapter(ravintolaAdapter);
         spinnerTags.setAdapter(tagiAdapter);

        findViewById(R.id.buttonJulkaiseArvostelu).setOnClickListener(this);
        haeKaupungit();
        haeTagit();

        spinnerKaupunki.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.toString() == "Lisää kaupunki"){
                        popUpKaupunki("Lisää kaupunki");
                        haeRavintolat(item.toString());
                }else {
                    haeRavintolat(item.toString());
                    spinnerRavintola.setSelection(0);
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerRavintola.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.toString() == "Lisää ravintola"){
                    if(spinnerKaupunki.getSelectedItem().toString() != "Valitse" && spinnerKaupunki.getSelectedItem().toString()!="Lisää kaupunki"){
                        popUpRavintola("Lisää ravintola");
                    }else{
                        toast("Valitse kaupunki");
                    }

                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinnerTags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                if(item.toString() == "Lisää tagi"){
                    popUpTagi("Lisää tagi");
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonJulkaiseArvostelu){
            if(otsikko.length() != 0){
                if(spinnerKaupunki.getSelectedItem().toString() != "Valitse"){
                    if(spinnerRavintola.getSelectedItem().toString() != "Valitse"){
                        if(spinnerTags.getSelectedItem().toString() != "Valitse"){
                            if(arvostelu.length() != 0){
                                String kaupunki = spinnerKaupunki.getSelectedItem().toString();
                                String ravintola = spinnerRavintola.getSelectedItem().toString();
                                String tag = spinnerTags.getSelectedItem().toString();
                                String nimi = otsikko.getText().toString();
                                String pisteet = Float.toString(tahdet.getRating());
                                String pohdinta = arvostelu.getText().toString();
                                lisaaArvostelu(kaupunki,nimi,pisteet,ravintola,pohdinta,tag);
                                Toast.makeText(this, "Arvostelu lisätty", Toast.LENGTH_SHORT).show();
                                otsikko.setText("");
                                arvostelu.setText("");
                                tahdet.setRating(0);
                                spinnerTags.setSelection(0);
                                spinnerRavintola.setSelection(0);
                                spinnerKaupunki.setSelection(0);
                            }else{
                                Toast.makeText(this, "Arvostelun teksti puuttuu", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(this, "Tagia ei valittu", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Ravintolaa ei valittu", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Kaupunkia ei valittu", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Annoksen nimi puuttuu", Toast.LENGTH_SHORT).show();
            }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri kuvaData = data.getData();
            picAnnos.setImageURI(kuvaData);

            picAnnos.setVisibility(View.VISIBLE);
            vidAnnos.setVisibility(View.INVISIBLE);

            videoUri = null;
            kuvaUri = kuvaData;

        }
        if (requestCode == GALLERY_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri videoData = data.getData();
            vidAnnos.setVideoURI(videoData);

            vidAnnos.setVisibility(View.VISIBLE);
            picAnnos.setVisibility(View.INVISIBLE);

            MediaController mediaController = new MediaController(this);
            vidAnnos.setMediaController(mediaController);
            mediaController.setAnchorView(vidAnnos);

            kuvaUri = null;
            videoUri = videoData;
        }

    }

    public void valitseKuva(View view){
        Intent intentKuva = new Intent(Intent.ACTION_GET_CONTENT);
        intentKuva.setType("image/*");
        startActivityForResult(intentKuva.createChooser(intentKuva, "Valitse kuva"), GALLERY_PICTURE_REQUEST);
    }

    public void valitseVideo(View view){
        Intent intentVideo = new Intent(Intent.ACTION_GET_CONTENT);
        intentVideo.setType("video/*");
        startActivityForResult(intentVideo.createChooser(intentVideo, "Valitse video"), GALLERY_VIDEO_REQUEST);
    }

    public void lisaaArvostelu(String kaupunki,String otsikko, String pisteet, String ravintola, String teksti, String tag){

        //Kuvan tallennus
        Fileuploader();

        String var = mDatabase.push().getKey();
        String kuvaUrl = tiedostoUri.toString();
        String peukut = "0";
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
        mDatabase.child("Arvostelut").child(var).child("Tags").push().setValue(tag);
    }


    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    private void Fileuploader()
    {
        if (kuvaUri != null && videoUri == null){
            tiedostoUri = kuvaUri;
        }
        else if (kuvaUri == null && videoUri != null){
            tiedostoUri = videoUri;
        }
        else {
            Toast.makeText(luoArvostelu.this, "Tiedoston lisäyksessä virhe", Toast.LENGTH_LONG).show();
        }

        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getExtension(tiedostoUri));
        fileReference.putFile(tiedostoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(luoArvostelu.this, "Median lisäys ONNISTUI!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                       Toast.makeText(luoArvostelu.this, "VIRHE MEDIANLATAUKSESSA!", Toast.LENGTH_LONG).show();
                    }
                });

        kuvaUri = null;
        videoUri = null;
        vidAnnos.setVisibility(View.INVISIBLE);
        picAnnos.setVisibility(View.INVISIBLE);
    }




    public void lisaaTagi(String tagi){
        mDatabase.child("Tags").push().setValue(tagi);
        toast("Tagi lisätty");
        haeTagit();
    }

    public void lisaaKaupunki(String kaupunki){
        mDatabase.child("Kaupungit").child(kaupunki).push().setValue("Hesburger");
        toast("Kaupunki lisätty");
        haeKaupungit();
    }
    public void lisaaRavintola(String kaupunki, String ravintola){
        mDatabase.child("Kaupungit").child(kaupunki).push().setValue(ravintola);
        toast("Ravintola lisätty");
        haeRavintolat(spinnerKaupunki.getSelectedItem().toString());
    }

    public void haeKaupungit(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                kaupunkiList.clear();
                kaupunkiList.add("Valitse");
                String url = "https://eighth-anvil-272013.firebaseio.com/Kaupungit.json?print=pretty";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray list = response.names();
                                    for(int i = 0; i<response.length();i++){
                                        kaupunkiList.add(list.get(i).toString());
                                    }
                                    kaupunkiList.add("Lisää kaupunki");
                                    kaupunkiAdapter.notifyDataSetChanged();
                                }catch (Exception e){}
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                queue.add(jsonObjectRequest);
            }
        });

    }

    public void haeRavintolat(final String kaupunki){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ravintolaList.clear();
                ravintolaList.add("Valitse");
                String url = "https://eighth-anvil-272013.firebaseio.com/Kaupungit.json?print=pretty";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    JSONObject arr = response.getJSONObject(kaupunki);
                                    JSONArray testlist = arr.names();
                                    for(int i =0;i<testlist.length();i++){
                                        String ravintola = arr.getString(testlist.getString(i).toString());
                                        ravintolaList.add(ravintola);
                                    }
                                    ravintolaList.add("Lisää ravintola");
                                    kaupunkiAdapter.notifyDataSetChanged();

                                }catch (Exception e){}
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });

                queue.add(jsonObjectRequest);
            }
        });
    }

    public void haeTagit(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tagiList.add("Valitse");
                String url = "https://eighth-anvil-272013.firebaseio.com/Tags.json?print=pretty";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    //JSONObject arr = response.getJSONObject("Tags");
                                    JSONArray testlist = response.names();
                                    for(int i =0;i<testlist.length();i++){
                                        String testi = response.getString(testlist.getString(i).toString());
                                        tagiList.add(testi);
                                    }
                                    tagiList.add("Lisää tagi");
                                    tagiAdapter.notifyDataSetChanged();
                                }catch (Exception e){}
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                queue.add(jsonObjectRequest);
        }
        });
    }

    public void popUpKaupunki(String otsikko){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(otsikko);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newKaupunki = input.getText().toString();
                lisaaKaupunki(newKaupunki);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    public void popUpRavintola(String otsikko){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(otsikko);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newRavintola = input.getText().toString();
                lisaaRavintola(spinnerKaupunki.getSelectedItem().toString(),newRavintola);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public void popUpTagi(String otsikko){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(otsikko);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newTag = input.getText().toString();
                lisaaTagi(newTag);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
}
