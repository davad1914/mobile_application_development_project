package hu.david.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {
    private static final String LOG_TAG = NoteListActivity.class.getName();

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    private String userId;
    EditText title;
    EditText desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        checkUser();

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY");
        //int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        title = findViewById(R.id.addTitle);
        desc = findViewById(R.id.addDesc);

        //Firestore peldanyositasa
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("notes");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
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
                userId,
                tit,
                des,
                date));
        finish();
    }

    private void checkUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }
    }
}