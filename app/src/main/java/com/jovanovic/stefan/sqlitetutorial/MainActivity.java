package com.jovanovic.stefan.sqlitetutorial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button create_new, show_all;
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    ImageView empty_imageview;
    TextView no_data;

    MyDatabaseHelper myDB;
    ArrayList<String> book_id, Title_note, book_author, book_pages;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        create_new = findViewById(R.id.create_new);
        show_all = findViewById(R.id.show_all);

        create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(MainActivity.this, Home_page.class);
                startActivity(intent);*/
                setContentView(R.layout.activity_main);
                recyclerView = findViewById(R.id.recyclerView);
                add_button = findViewById(R.id.add_button);
                empty_imageview = findViewById(R.id.empty_imageview);
                no_data = findViewById(R.id.no_data);
                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        startActivity(intent);
                    }
                });

                myDB = new MyDatabaseHelper(MainActivity.this);
                book_id = new ArrayList<>();
                Title_note = new ArrayList<>();
                book_author = new ArrayList<>();
                book_pages = new ArrayList<>();

                storeDataInArrays();


                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        customAdapter = new CustomAdapter(MainActivity.this, this, book_id, Title_note, book_author,
                book_pages);
        if (cursor.getCount() == 0) {
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);

        } else {
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                Title_note.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(MainActivity.this, Home_page.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }


    public static class Home_page extends AppCompatActivity {

        RecyclerView recyclerView;
        FloatingActionButton add_button;
        ImageView empty_imageview;
        TextView no_data;

        MyDatabaseHelper myDB;
        ArrayList<String> book_id, Title_note, book_author, book_pages;
        CustomAdapter customAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            recyclerView = findViewById(R.id.recyclerView);
            add_button = findViewById(R.id.add_button);
            empty_imageview = findViewById(R.id.empty_imageview);
            no_data = findViewById(R.id.no_data);
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Home_page.this, AddActivity.class);
                    startActivity(intent);
                }
            });

            myDB = new MyDatabaseHelper(Home_page.this);
            book_id = new ArrayList<>();
            Title_note = new ArrayList<>();
            book_author = new ArrayList<>();
            book_pages = new ArrayList<>();

            storeDataInArrays();


            recyclerView.setAdapter(customAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Home_page.this));

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1){
                recreate();

            }
        }

        void storeDataInArrays(){
            Cursor cursor = myDB.readAllData();
            if(cursor.getCount() == 0){
                empty_imageview.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.VISIBLE);

            }else{
                while (cursor.moveToNext()){
                    book_id.add(cursor.getString(0));
                    Title_note.add(cursor.getString(1));
                    book_author.add(cursor.getString(2));
                    book_pages.add(cursor.getString(3));
                }
                empty_imageview.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.my_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.delete_all){
                confirmDialog();
            }
            return super.onOptionsItemSelected(item);
        }

        void confirmDialog(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete All?");
            builder.setMessage("Are you sure you want to delete all Data?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(Home_page.this);
                    myDB.deleteAllData();
                    //Refresh Activity
                    Intent intent = new Intent(Home_page.this, Home_page.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
    }

    static class MyDatabaseHelper extends SQLiteOpenHelper {

        private Context context;
        private static final String DB_Name = "BookLibrary.db";
        private static final int DATABASE_VERSION = 1;

        private static final String TABLE_NAME = "my_library";
        private static final String COLUMN_ID = "_id";
        private static final String Note_title = "book_title";
        private static final String COLUMN_AUTHOR = "book_author";
        private static final String COLUMN_PAGES = "book_pages";

        MyDatabaseHelper(@Nullable Context context) {
            super(context, DB_Name, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME +
                            " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Note_title + " TEXT, " +
                            COLUMN_AUTHOR + " TEXT, " +
                            COLUMN_PAGES + " INTEGER);";
            db.execSQL(query);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        void addBook(String title, String author, int pages){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(Note_title, title);
            cv.put(COLUMN_AUTHOR, author);
            cv.put(COLUMN_PAGES, pages);
            long result = db.insert(TABLE_NAME,null, cv);
            if(result == -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        }

        Cursor readAllData(){
            String query = "SELECT * FROM " + TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = null;
            if(db != null){
                cursor = db.rawQuery(query, null);
            }
            return cursor;
        }

        void updateData(String row_id, String title, String author, String pages){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Note_title, title);
            cv.put(COLUMN_AUTHOR, author);
            cv.put(COLUMN_PAGES, pages);

            long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
            if(result == -1){
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
            }

        }

        void deleteOneRow(String row_id){
            SQLiteDatabase db = this.getWritableDatabase();
            long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
            if(result == -1){
                Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
            }
        }

        void deleteAllData(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TABLE_NAME);
        }

    }

    public static class UpdateActivity extends AppCompatActivity {

        EditText title_input, author_input, pages_input;
        Button update_button, delete_button;

        String id, title, author, pages;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update);

            title_input = findViewById(R.id.title_input2);
            author_input = findViewById(R.id.author_input2);
            pages_input = findViewById(R.id.pages_input2);
            update_button = findViewById(R.id.update_button);
            delete_button = findViewById(R.id.delete_button);

            //First we call this
            getAndSetIntentData();

            //Set actionbar title after getAndSetIntentData method
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setTitle(title);
            }

            update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //And only then we call this
                    MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                    title = title_input.getText().toString().trim();
                    author = author_input.getText().toString().trim();
                    pages = pages_input.getText().toString().trim();
                    myDB.updateData(id, title, author, pages);
                }
            });
            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog();
                }
            });

        }

        void getAndSetIntentData() {
            if (getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                    getIntent().hasExtra("author") && getIntent().hasExtra("pages")) {
                //Getting Data from Intent
                id = getIntent().getStringExtra("id");
                title = getIntent().getStringExtra("title");
                author = getIntent().getStringExtra("author");
                pages = getIntent().getStringExtra("pages");

                //Setting Intent Data
                title_input.setText(title);
                author_input.setText(author);
                pages_input.setText(pages);
                Log.d("stev", title + " " + author + " " + pages);
            } else {
                Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
            }
        }

        void confirmDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete " + title + " ?");
            builder.setMessage("Are you sure you want to delete " + title + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                    myDB.deleteOneRow(id);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
    }

    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private Context context;
        private Activity activity;
        private ArrayList book_id, book_title, book_author, book_pages;

        CustomAdapter(Activity activity, Context context, ArrayList book_id, ArrayList book_title, ArrayList book_author,
                      ArrayList book_pages) {
            this.activity = activity;
            this.context = context;
            this.book_id = book_id;
            this.book_title = book_title;
            this.book_author = book_author;
            this.book_pages = book_pages;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.my_row, parent, false);
            return new MyViewHolder(view);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
            holder.book_id_txt.setText(String.valueOf(book_id.get(position)));
            holder.book_title_txt.setText(String.valueOf(book_title.get(position)));
            holder.book_author_txt.setText(String.valueOf(book_author.get(position)));
            holder.book_pages_txt.setText(String.valueOf(book_pages.get(position)));
            //Recyclerview onClickListener
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("id", String.valueOf(book_id.get(position)));
                    intent.putExtra("title", String.valueOf(book_title.get(position)));
                    intent.putExtra("author", String.valueOf(book_author.get(position)));
                    intent.putExtra("pages", String.valueOf(book_pages.get(position)));
                    activity.startActivityForResult(intent, 1);
                }
            });


        }

        @Override
        public int getItemCount() {
            return book_id.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView book_id_txt, book_title_txt, book_author_txt, book_pages_txt;
            LinearLayout mainLayout;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                book_id_txt = itemView.findViewById(R.id.book_id_txt);
                book_title_txt = itemView.findViewById(R.id.book_title_txt);
                book_author_txt = itemView.findViewById(R.id.book_author_txt);
                book_pages_txt = itemView.findViewById(R.id.book_pages_txt);
                mainLayout = itemView.findViewById(R.id.mainLayout);
                //Animate Recyclerview
                Animation translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
                mainLayout.setAnimation(translate_anim);
            }

        }

    }

    public static class AddActivity extends AppCompatActivity {

        EditText title, author_input, pages_input;
        Button add_button;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add);

            title = findViewById(R.id.title_input);
            author_input = findViewById(R.id.author_input);
            pages_input = findViewById(R.id.pages_input);
            add_button = findViewById(R.id.add_button);
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                    myDB.addBook(title.getText().toString().trim(),
                            author_input.getText().toString().trim(),
                            Integer.valueOf(pages_input.getText().toString().trim()));
                }
            });
        }
    }
}
