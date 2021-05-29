package hu.david.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    EditText title;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.addTitle);
        desc = findViewById(R.id.addDesc);

        //Firestore peldanyositasa
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("notes");
    }

    public void addNote(View view){
        String tit = title.getText().toString();
        String des = desc.getText().toString();
        if(tit.equals("") || des.equals("")){
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_LONG).show();
            return;
        }
        Date date = new Date(System.currentTimeMillis());
        mItems.add(new Note(
                tit,
                des,
                date));
        finish();
    }
}