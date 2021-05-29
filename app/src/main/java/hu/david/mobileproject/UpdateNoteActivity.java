package hu.david.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class UpdateNoteActivity extends AppCompatActivity {

    private static final String LOG_TAG = UpdateNoteActivity.class.getName();

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private Note note;

    TextView title;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        title = findViewById(R.id.updateTitle);
        desc = findViewById(R.id.updateDesc);

        //Bundle bundle = getIntent().getExtras();
        //noteId = bundle.getString("note_item");

        Intent i = getIntent();
        note = (Note)i.getSerializableExtra("note_item");

        //Firestore peldanyositasa
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("notes");

        //Log.d(LOG_TAG, noteId);
        //queryData();

        title.setText(note.getTitle());
        desc.setText(note.getDesc());
    }

    public void deleteItem(Note item) {
        DocumentReference ref = mItems.document(item._getId());
        ref.delete()
                .addOnSuccessListener(success -> {
                    Log.d(LOG_TAG, "Item is successfully deleted: " + item._getId());
                })
                .addOnFailureListener(fail -> {
                    Toast.makeText(this, "Item " + item._getId() + " cannot be deleted.", Toast.LENGTH_LONG).show();
                });
    }

    public void updateNote(View view){
        if(title.equals("") || desc.equals("")){
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_LONG).show();
        }
        deleteItem(note);

        note.setTitle(title.getText().toString());
        note.setDesc(desc.getText().toString());

        mItems.add(note);

        finish();
    }
}