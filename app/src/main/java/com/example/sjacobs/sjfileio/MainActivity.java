package com.example.sjacobs.sjfileio;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.database.Cursor;

public class MainActivity extends ActionBarActivity {

    private EditText firstNameField;
    private EditText lastNameField;
    private ListView rosterList;
    private MainActivity thisActivity;
    private RosterTableHelper rosterDBHelper;
    private SQLiteDatabase rosterDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstNameField = (EditText) findViewById(R.id.firstName);
        lastNameField = (EditText) findViewById(R.id.lastName);
        rosterList = (ListView) findViewById(R.id.rosterList);
        thisActivity = this;

        System.out.println("SMJ - Making DB start");
        rosterDBHelper = new RosterTableHelper(this);
        rosterDB = rosterDBHelper.getWritableDatabase();
        System.out.println("SMJ - Making DB end");

        final Button button = (Button) findViewById(R.id.clear_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                firstNameField.setText("");
                lastNameField.setText("");
            }
        });

        final Button button2 = (Button) findViewById(R.id.save_button);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                thisActivity.saveFieldName(firstNameField.getText().toString(),
                        lastNameField.getText().toString());
            }
        });

        final Button button3 = (Button) findViewById(R.id.load_button);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                thisActivity.loadFieldName();
            }
        });
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

    private void saveFieldName(String first, String last) {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor myEditor = sharedPref.edit();
        myEditor.putString("firstName", first);
        myEditor.putString("lastName", last);
        myEditor.commit();

        // DB testing
        rosterDB.execSQL("INSERT INTO " + RosterTableHelper.DICTIONARY_TABLE_NAME + " (FirstName, LastName) VALUES ('" + first + "', '" + last + "');");
    }

    private void loadFieldName() {

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String first = sharedPref.getString("firstName", "missing");
        String last = sharedPref.getString("lastName", "missing");
        firstNameField.setText(first);
        lastNameField.setText(last);
        System.out.println("First:" + first + " Last:" + last);

        // DB testing
        Cursor myCursor = rosterDB.query(RosterTableHelper.DICTIONARY_TABLE_NAME, null, null, null, null, null, null);
        System.out.println("Row Count is " + myCursor.getCount());
        System.out.println("Row Position is " + myCursor.getPosition());
        myCursor.moveToFirst();

        for(int i=1; i <= myCursor.getCount(); i++) {
            System.out.println("Row[" + i + "]: " + myCursor.getString(1) + ", " + myCursor.getString(2));
            myCursor.moveToNext();
        }

        // Reset the cursor
        myCursor.moveToFirst();

        System.out.println("Start table fill");
        // Initialize my list based on my Cursor
        String[] fromColumns = new String[] { "FirstName" , "LastName" };
        int[] toColumns = new int[] { R.id.row_first_name, R.id.row_last_name };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.roster_item, myCursor, fromColumns, toColumns, 0);
        System.out.println("After adapter construction");

        rosterList.setAdapter(adapter);
        System.out.println("End table fill");
    }

}
