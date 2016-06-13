package ipleiria.pt.projeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("appMusic", 0);
        Set<String> musicSet = sp.getStringSet("musicKey", new HashSet<String>());
        Toast.makeText(MainActivity.this, ".", Toast.LENGTH_SHORT).show();

        music = new ArrayList<String> (musicSet);

 //     music.add("AC DC | High Voltage \n1975 | 4 ");
 //     music.add("AC DC | Rock or Bust \n2014 | 3 ");
 //     music.add("Knife Party | Abandon Ship \n2015 | 4 ");
 //     music.add("Toni Carreira | Vagabundo Por Amor \n2004 | 5 ");


        //codigo para a listview

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, music);
        ListView listView = (ListView) findViewById(R.id.listView_music);
        listView.setAdapter(adapter);




        //codigo para o spinner
        Spinner s = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter_s = ArrayAdapter.createFromResource(this,
                R.array.myspinner, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        s.setAdapter(adapter_s);



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, R.string.current_album, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        MainActivity.this);

// Setting Dialog Title
                alertDialog2.setTitle("Confirm Delete...");

// Setting Dialog Message
                alertDialog2.setMessage("Are you sure you want delete this file?");

// Setting Icon to Dialog


// Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                Toast.makeText(getApplicationContext(),
                                        "You clicked on YES", Toast.LENGTH_SHORT)
                                        .show();

                            }
                        });

                music.remove(position);

// Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                Toast.makeText(getApplicationContext(),
                                        "You clicked on NO", Toast.LENGTH_SHORT)
                                        .show();
                                dialog.cancel();
                            }
                        });

// Showing Alert Dialog
                alertDialog2.show();



                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, music);

                ListView listView = (ListView) findViewById(R.id.listView_music);
                listView.setAdapter(adapter);


                return true;


            }
        });


    }


    public void onClick_add(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_add, null));
// Add the buttons
        builder.setPositiveButton(R.string.str, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                // obter referencias para as editText
                //faz o cast de um diálogo "genérico"
                //para um allertdialog

                AlertDialog al = (AlertDialog) dialog;

                EditText etArtist = (EditText) al.findViewById(R.id.editText_artist);

                EditText etAlbum = (EditText) al.findViewById(R.id.editText_album);

                EditText etEditora = (EditText) al.findViewById(R.id.editText_editora);

                EditText etYear = (EditText) al.findViewById(R.id.editText_year);

                RatingBar etRating = (RatingBar) al.findViewById(R.id.ratingBar);

                //obter o Artista, Album e ano de lancamento das editTexts

                String artist = etArtist.getText().toString();
                String album = etAlbum.getText().toString();
                String year = etYear.getText().toString();
                String editora= etEditora.getText().toString();
                String rating = "" + etRating.getNumStars();

                //criar novo album
                String newAlbum = artist + " | " + album + " \n" + editora + " | "  + year + " | " + rating;

                // adicionar o album a lista de albuns
                music.add(newAlbum);

                // dizer à listview para se atualizar
                ListView lv = (ListView) findViewById(R.id.listView_music);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, music);
                lv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, R.string.sss, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.st, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(MainActivity.this, R.string.cancel_mess, Toast.LENGTH_SHORT).show();
            }
        });
// Set other dialog properties
        // ...

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();



    }
    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(MainActivity.this, "A guardar albuns", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("appMusic", 0);

        SharedPreferences.Editor edit = sp.edit();

        HashSet musicSet = new HashSet(music);

        edit.putStringSet("musicKey", musicSet);

        edit.commit();
    }

    private SimpleAdapter createSimpleAdapter(ArrayList<String> music) {
        List<HashMap <String, String>> simpleAdapterData = new ArrayList<HashMap<String, String>>();

        for (String m : music) {
            HashMap<String, String> hashMap = new HashMap<>();

            String[] split = m.split(" \\| ");

            hashMap.put("name", split[0]);
            hashMap.put("phone", split[1]);

            simpleAdapterData.add(hashMap);
        }

        String[] from = {"name", "phone"};
        int[] to = {R.id.textView_artist, R.id.textView_album};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), simpleAdapterData, R.layout.listview, from, to);
        return simpleAdapter;
    }





    public void onClick_search(View view) {
        EditText txt = (EditText) findViewById(R.id.editText_search);
        Spinner spn = (Spinner) findViewById(R.id.spinner);
        ListView lv = (ListView) findViewById(R.id.listView_music);

        ArrayList<String> searchedAlbum = new ArrayList<>();

        String termo = txt.getText().toString();

        String selectedItem = (String) spn.getSelectedItem();

        if(termo.equals("")){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, music);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, R.string.Showing_all, Toast.LENGTH_SHORT).show();
        }

        else if (selectedItem.equals("All")) {
            for (String m : music) {

                if (m.contains(termo)) {
                    searchedAlbum.add(m);
                }
            }
        }

        else if (selectedItem.equals("Artist")) {
            for (String m : music) {
                String[] split = m.split("\\|");
                String artist = split[0];

                if (artist.contains(termo)) {
                    searchedAlbum.add(m);
                }
            }
        }

        else if (selectedItem.equals("ALbum")){
            for (String m : music) {
                String[] split = m.split("\\|");
                String album = split[1];

                if (album.contains(termo)) {
                    searchedAlbum.add(m);
                }
            }
        }
        else if (selectedItem.equals("Year")){
            for (String m : music) {
                String[] split = m.split("\\|");
                String year = split[2];

                if (year.contains(termo)) {
                    searchedAlbum.add(m);
                }
            }
        }
        else if (selectedItem.equals("Rating")){
            for (String m : music) {
                String[] split = m.split("\\|");
                String rating = split[3];

                if (rating.contains(termo)) {
                    searchedAlbum.add(m);
                }
            }
        }

        boolean vazia = searchedAlbum.isEmpty();

        if (!vazia) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, searchedAlbum);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, R.string.Searched_songs, Toast.LENGTH_SHORT).show();
        }
        else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, music);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, R.string.No_songs_found, Toast.LENGTH_SHORT).show();
        }
    }

}
