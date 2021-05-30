package hu.david.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

import hu.david.mobileproject.models.UserRole;

public class RoleListActivity extends AppCompatActivity {

    private static final String LOG_TAG = RoleListActivity.class.getName();
    private static final String PREF_KEY = RoleListActivity.class.getPackage().toString();
    private FirebaseUser user;

    private RecyclerView mRecyclerView;
    private ArrayList<UserRole> mItemList;
    private RoleAdapter mAdapter;

    private int gridNumber = 1;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_list);

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY");
        //int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        checkUser();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new RoleAdapter(this, mItemList);

        mRecyclerView.setAdapter(mAdapter);

        //Firestore peldanyositasa
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("roles");
        queryData();
    }

    private void queryData() {
        mItemList.clear();

        mItems.orderBy("involvementRole").limit(15).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                UserRole item = document.toObject(UserRole.class);
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
                .getStringArray(R.array.role_id);
        String[] itemsInfo = getResources()
                .getStringArray(R.array.role_href);
        String[] itemsInv = getResources()
                .getStringArray(R.array.role_involvementRole);

        // Create the ArrayList of Sports objects with the titles and
        // information about each sport.
        for (int i = 0; i < itemsList.length; i++) {
            mItems.add(new UserRole(
                    itemsList[i],
                    itemsInfo[i],
                    itemsInv[i]));
        }
    }

    private void changeSpanCount(int spanCount) {
        GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        layoutManager.setSpanCount(spanCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.role_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.appBarSwitch);
        SwitchCompat switchButton = (SwitchCompat) menuItem.getActionView();

        switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (switchButton.isChecked()){
                changeSpanCount(2);
            }else{
                changeSpanCount(1);
            }
        });
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

    public void checkUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }
    }
}