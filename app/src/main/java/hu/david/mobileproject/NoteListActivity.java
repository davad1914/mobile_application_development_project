package hu.david.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class NoteListActivity extends AppCompatActivity {

    private static final String LOG_TAG = NoteListActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
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

    private void queryData() {
        mItemList.clear();

        mItems.orderBy("title").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                Note item = document.toObject(Note.class);
                item.setNoteId(document.getId());
                mItemList.add(item);
            }

            if(mItemList.size() == 0){
                initializeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
}