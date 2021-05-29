package hu.david.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class NoteListActivity extends AppCompatActivity {

    private static final String LOG_TAG = NoteListActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<Note> mItemList;
    private NoteAdapter mAdapter;

    private int gridNumber = 1;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }

        //firestore kodok



        //firestore kodok vege

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new NoteAdapter(this, mItemList);

        mRecyclerView.setAdapter(mAdapter);

        //Firestore peldanyositasa
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("notes");
        queryData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryData();
    }

    private void queryData() {
        mItemList.clear();

        mItems.orderBy("title").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                Note item = document.toObject(Note.class);
                item.setId(document.getId());
                mItemList.add(item);
            }

            if(mItemList.size() == 0){
                initializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });
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

        queryData();
    }

    public void editItem(Note item){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("note_item", item._getId());
        startActivity(intent);
    }

    private void initializeData() {
        // Get the resources from the XML file.
        String[] itemsList = getResources()
                .getStringArray(R.array.note_desc);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.note_title);

        Date date = new Date(System.currentTimeMillis());
        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemsList.length; i++) {
            mItems.add(new Note(
                    itemsList[i],
                    itemsInfo[i],
                    date));
        }
    }

    public void goToAdd(){
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.note_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.logoutBar);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutBar:
                Log.d(LOG_TAG, "Logout clicked!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.addMenu:
                Log.d(LOG_TAG, "Add clicked!");
                goToAdd();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}