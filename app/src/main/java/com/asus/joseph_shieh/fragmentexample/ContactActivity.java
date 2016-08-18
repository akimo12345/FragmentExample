package com.asus.joseph_shieh.fragmentexample;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.content.Intent;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Random;
import java.util.concurrent.Executor;

public class ContactActivity extends AppCompatActivity {
    private final String TAG = "Joseph";
    private ArrayAdapter<String> adapter;
    private ListView list;
    private TextView counter;
    private TextView detail_title;

    int count = 0;
    int drawCount = 0;
    Random random = new Random();
    private LinearLayout drawingContaner;
    private Spinner luckys;
    private ArrayAdapter<String> luckyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        findViews();
        luckyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        luckys.setAdapter(luckyAdapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        detail_title = (TextView) findViewById(R.id.detail_info);
        detail_title.setText(message);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "抽獎中", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startDrawing();
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Firebase.setAndroidContext(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        Firebase poolRef = new Firebase("https://josephluckydraw.firebaseio.com/pool");
        poolRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add((String) dataSnapshot.child("name").getValue());
                Log.d(TAG,"name is " + dataSnapshot.child("name").getValue());
                count++;
                updateCounter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.remove((String) dataSnapshot.child("name").getValue());
                count--;
                updateCounter();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "onCancelled");
            }
        });

    }

    private void startDrawing() {
//        drawingContaner.setVisibility(View.VISIBLE);
        int randomSecond = random.nextInt(60)+30;
        new DrawingTask().execute(randomSecond);
        /*DrawingThread drawing = new DrawingThread(randomSecond);
        drawing.start();
        int lucky = random.nextInt(count);
        luckyPosition.setText(String.valueOf(lucky));
        String name = adapter.getItem(lucky);
        luckyName.setText(name);*/
    }

    class DrawingTask extends AsyncTask<Integer, Integer, Integer>{
        private int position;
        private AlertDialog dialog;
        private TextView luckyPosition;
        private TextView luckyName;
        @Override
        protected void onPreExecute() {
            dialog = new AlertDialog.Builder(ContactActivity.this)
                    .setView(R.layout.drawing)
                    .setTitle("抽獎中ing~~~")
                    .setPositiveButton("DONE", null).show();
            luckyPosition = (TextView) dialog.findViewById(R.id.drawing_position);
            luckyName = (TextView) dialog.findViewById(R.id.drawing_name);
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            for (int i=0; i<params[0]; i++){
                position = random.nextInt(count);
                publishProgress(position);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return position;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int pos  = values[0];
            luckyPosition.setText(String.valueOf(pos));
            luckyName.setText(adapter.getItem(pos));
        }

        @Override
        protected void onPostExecute(Integer integer) {
            int lucky = integer;
            luckyName.setTextColor(Color.MAGENTA);
            luckyPosition.setTextColor(Color.MAGENTA);
            //
            String name = luckyName.getText().toString();
            adapter.remove(name);
            luckyAdapter.add(name);
            luckyAdapter.notifyDataSetChanged();
            count = adapter.getCount();
            updateCounter();
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    private void findViews() {
        list = (ListView) findViewById(R.id.list);
        counter = (TextView) findViewById(R.id.counter);
//        drawingContaner = (LinearLayout) findViewById(R.id.drawing_container);
        luckys = (Spinner) findViewById(R.id.luckys);

    }

    private void updateCounter() {
        counter.setText(String.valueOf(count));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*for fragment1 test*/
    public void Go1(View V){
        new Job1Task().execute();
    }

    class Job1Task extends AsyncTask<Void, Void ,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            TextView info = (TextView) findViewById(R.id.info);
            info.setText("Job1Task DONE");
        }
    }

    public void Go2(View v){
        new Job2Task().execute(3);
    }
    class Job2Task extends AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                Thread.sleep(params[0]*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView info = (TextView) findViewById(R.id.info);
            info.setText("Job2Task DONE");
        }
    }

    public void Go3(View v){
        new Job3Task().execute(6);
    }
    class Job3Task extends AsyncTask<Integer, Integer, Void>{
        @Override
        protected Void doInBackground(Integer... params) {
            for (int i=params[0]; i>0; i--){
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            TextView info = (TextView) findViewById(R.id.info);
            info.setText(String.valueOf(values[0]) + " sec");
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TextView info = (TextView) findViewById(R.id.info);
            info.setText("Job3Task DONE");
        }
    }

}
