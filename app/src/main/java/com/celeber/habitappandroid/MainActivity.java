package com.celeber.habitappandroid;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private List<Ban> bansList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BanAdapter mAdapter;

    private DatabaseHelper dbHandler;
    private View actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        dbHandler = new DatabaseHelper(MainActivity.this);
        actionBar = getSupportActionBar().getCustomView();

        mAdapter = new BanAdapter(bansList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        Button helpButton = (Button) findViewById(R.id.helpButton);
        Button addButton = (Button) findViewById(R.id.addButton);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ban ban = new Ban();
                ban.setTitle("");
                ban.setSubmit("0");
                ban.setResist("0");

                dbHandler.save(ban);

                getBans();
            }
        });

        getBans();

    }

    private void getBans() {
        bansList.removeAll(bansList);
        bansList.addAll(dbHandler.getAllBans());

        mAdapter.notifyDataSetChanged();
    }


    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof BanAdapter.ItemViewHolder) {
            Ban removedBan = bansList.get(position);
            dbHandler.deleteBan(String.valueOf(removedBan.getID()));

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
        }
    }



}

