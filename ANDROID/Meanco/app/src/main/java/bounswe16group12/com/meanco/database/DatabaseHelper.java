package bounswe16group12.com.meanco.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.objects.User;

/**
 * Created by feper on 11/17/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "meancoDB";
    private static final int DATABASE_VERSION = 1;

    //USER
    private static final String KEY_USER_TABLE = "users";

    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_PASSWORD = "password";

    //TOPIC
    private static final String KEY_TOPIC_TABLE = "topics";

    private static final String KEY_TOPIC_ID = "id";
    private static final String KEY_TOPIC_NAME = "topicName";
    private static final String KEY_TAG_LIST = "tags";

    //COMMENT
    private static final String KEY_COMMENT_TABLE = "comments";

    private static final String KEY_COMMENT_ID = "id";
    private static final String KEY_COMMENT_TOPIC_NAME = "topicName";
    private static final String KEY_COMMENT_CONTENT = "content";

    //RELATION
    private static final String KEY_RELATION_TABLE = "relations";

    private static final String KEY_RELATION_ID = "id";
    private static final String KEY_RELATION_NAME = "relationName";
    private static final String KEY_RELATION_FIRST_TOPIC_NAME = "firstTopicName";
    private static final String KEY_RELATION_SECOND_TOPIC_NAME = "secondTopicName";
    private static final String KEY_RELATION_IS_BIDIRECTIONAL = "isbidirectional";


    //////////////////////////////////////////////////////////////////////////////////
    //CONSTRUCTOR
    /////////////////////////////////////////////////////////////////////////////////

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //////////////////////////////////////////////////////////////////////////////////
    //LIFECYCLE
    /////////////////////////////////////////////////////////////////////////////////

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + KEY_USER_TABLE +
                "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_USER_NAME + " TEXT," + // Define a foreign key
                KEY_USER_PASSWORD + " TEXT" +
                ")";

        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_COMMENT_TABLE = "CREATE TABLE " + KEY_TOPIC_TABLE +
                "(" +
                KEY_TOPIC_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TOPIC_NAME + " TEXT," +
                KEY_TAG_LIST + " TEXT" +
                ")";

        db.execSQL(CREATE_COMMENT_TABLE);

        String CREATE_RELATION_TABLE = "CREATE TABLE " + KEY_COMMENT_TABLE +
                "(" +
                KEY_COMMENT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_COMMENT_TOPIC_NAME + " TEXT," +
                KEY_COMMENT_CONTENT + " TEXT" +
                ")";

        db.execSQL(CREATE_RELATION_TABLE);

        String CREATE_TOPICS_TABLE = "CREATE TABLE " + KEY_RELATION_TABLE +
                "(" +
                KEY_RELATION_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_RELATION_NAME + " TEXT," +
                KEY_RELATION_FIRST_TOPIC_NAME + " TEXT," +
                KEY_RELATION_SECOND_TOPIC_NAME + " TEXT," +
                KEY_RELATION_IS_BIDIRECTIONAL + " INTEGER" + // 0 or 1 (boolean)

                ")";

        db.execSQL(CREATE_TOPICS_TABLE);

    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + KEY_USER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + KEY_TOPIC_TABLE);
            onCreate(db);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    //USER
    /////////////////////////////////////////////////////////////////////////////////

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateUser(User user) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_NAME, user.username);
            values.put(KEY_USER_PASSWORD, user.password);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(KEY_USER_TABLE, values, KEY_USER_NAME + "= ?", new String[]{user.username});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, KEY_USER_TABLE, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.username)});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                userId = db.insertOrThrow(KEY_USER_TABLE, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        // SELECT * FROM POSTS
        String USERS_SELECT_QUERY = "SELECT * FROM users";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USERS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.username = cursor.getString(cursor.getColumnIndex(KEY_USER_NAME));
                    newUser.password = cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD));


                    users.add(newUser);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return users;
    }


    //////////////////////////////////////////////////////////////////////////////////
    //TOPIC
    /////////////////////////////////////////////////////////////////////////////////

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateTopic(Topic topic) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long topicId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TOPIC_NAME, topic.topicName);
            values.put(KEY_TAG_LIST, Arrays.toString(topic.tags.toArray()));

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(KEY_TOPIC_TABLE, values, KEY_TOPIC_NAME + "= ?", new String[]{topic.topicName});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_TOPIC_ID, KEY_TOPIC_NAME, KEY_TAG_LIST);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(topic.topicName)});
                try {
                    if (cursor.moveToFirst()) {
                        topicId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                topicId = db.insertOrThrow(KEY_TOPIC_TABLE, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return topicId;
    }

    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();

        // SELECT * FROM POSTS
        String TOPICS_SELECT_QUERY = "SELECT * FROM topics";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TOPICS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Topic topic = new Topic();
                    topic.topicName = cursor.getString(cursor.getColumnIndex(KEY_TOPIC_NAME));
                    String tags = cursor.getString(cursor.getColumnIndex(KEY_TAG_LIST));

                    topic.tags = stringToList(tags);

                    topics.add(topic);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return topics;
    }

    private ArrayList<String> stringToList(String s){
        Log.i("TOPIC HELPER",s);
        s = s.replace("[","");
        s = s.replace("]","");
        ArrayList<String> output = new ArrayList<>(Arrays.asList(s.split(",")));
        Log.i("STRING OF FIRST",output.get(0));
        return output;
    }

}
