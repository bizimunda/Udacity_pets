package com.example.android.udacity_pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.udacity_pets.data.PetContract;
import com.example.android.udacity_pets.data.PetDbHelper;
import com.shamanland.fab.FloatingActionButton;


public class CatalogActivity extends AppCompatActivity {

    private PetDbHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        mDbHelper = new PetDbHelper(this);


    }

    private void displayDatabaseInfo() {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_GENDER,
                PetContract.PetEntry.COLUMN_PET_WEIGHT,
        };
        Cursor cursor = db.query(
                PetContract.PetEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        try {

            displayView.setText("Number of row in pets database table: " + cursor.getCount() + " pets. \n\n");
            displayView.append(PetContract.PetEntry._ID + " - " +
                    PetContract.PetEntry.COLUMN_PET_NAME + " - " +
                    PetContract.PetEntry.COLUMN_PET_BREED + " - " +
                    PetContract.PetEntry.COLUMN_PET_GENDER + " - " +
                    PetContract.PetEntry.COLUMN_PET_WEIGHT + " \n ");

            int idColumnIndex=cursor.getColumnIndex(PetContract.PetEntry._ID);
            int nameColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex=cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);

            while (cursor.moveToNext()){
                int currentID=cursor.getInt(idColumnIndex);
                String currentName=cursor.getString(nameColumnIndex);
                String currentBreed=cursor.getString(breedColumnIndex);
                int currentGender=cursor.getInt(genderColumnIndex);
                int currentWeight=cursor.getInt(weightColumnIndex);

                displayView.append(("\n" + currentID + " -    " +
                       currentName + " - " +
                       currentBreed + " - " +
                       currentGender + " - " +
                       currentWeight));
            }



        } finally {
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetContract.PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetContract.PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, 7);

        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, values);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }
}
