package com.example.sjacobs.sjfileio;

/**
 * Created by sjacobs on 12/13/15.
 */

import android.database.sqlite.*;
import android.content.Context;

public class RosterTableHelper extends SQLiteOpenHelper {

    // DB learning exercise
    public static final String DATABASE_NAME = "CoachCornerDB2";
    private static final int DATABASE_VERSION = 3;
    public static final String DICTIONARY_TABLE_NAME = "roster";

    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, FirstName TEXT, LastName TEXT);";

    RosterTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        System.out.println("DB Help onCreate - START");

        db.execSQL(DICTIONARY_TABLE_CREATE);

        System.out.println("DB Help onCreate - END");
        System.out.println(DICTIONARY_TABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        // Required for concrete class
    }


}
