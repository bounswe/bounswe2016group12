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

import bounswe16group12.com.meanco.objects.Comment;
import bounswe16group12.com.meanco.objects.Relation;
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

    //TAG
    private static final String KEY_TAG_TABLE = "tags";

    private static final String KEY_TAG_ID = "id";
    private static final String KEY_TAG_NAME = "tagName";
    private static final String KEY_TAG_DESCRIPTION = "description";

    //COMMENT
    private static final String KEY_COMMENT_TABLE = "comments";

    private static final String KEY_COMMENT_ID = "id";
    private static final String KEY_COMMENT_TOPIC_ID = "topicId";
    private static final String KEY_COMMENT_CONTENT = "content";

    //RELATION
    private static final String KEY_RELATION_TABLE = "relations";

    private static final String KEY_RELATION_ID = "id";
    private static final String KEY_RELATION_NAME = "relationName";
    private static final String KEY_RELATION_FIRST_TOPIC_ID = "firstTopicId";
    private static final String KEY_RELATION_SECOND_TOPIC_ID = "secondTopicId";
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

        String CREATE_TOPIC_TABLE = "CREATE TABLE " + KEY_TOPIC_TABLE +
                "(" +
                KEY_TOPIC_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TOPIC_NAME + " TEXT," +
                KEY_TAG_LIST + " TEXT" +
                ")";

        db.execSQL(CREATE_TOPIC_TABLE);

        String CREATE_TAG_TABLE = "CREATE TABLE " + KEY_TAG_TABLE +
                "(" +
                KEY_TAG_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TAG_NAME + " TEXT," +
                KEY_TAG_DESCRIPTION + " TEXT" +
                ")";

        db.execSQL(CREATE_TAG_TABLE);

        String CREATE_COMMENT_TABLE = "CREATE TABLE " + KEY_COMMENT_TABLE +
                "(" +
                KEY_COMMENT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_COMMENT_TOPIC_ID + " INTEGER," +
                KEY_COMMENT_CONTENT + " TEXT" +
                ")";

        db.execSQL(CREATE_COMMENT_TABLE);

        String CREATE_RELATION_TABLE = "CREATE TABLE " + KEY_RELATION_TABLE +
                "(" +
                KEY_RELATION_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_RELATION_NAME + " TEXT," +
                KEY_RELATION_FIRST_TOPIC_ID + " INTEGER," +
                KEY_RELATION_SECOND_TOPIC_ID + " INTEGER," +
                KEY_RELATION_IS_BIDIRECTIONAL + " INTEGER" + // 0 or 1 (boolean)

                ")";

        db.execSQL(CREATE_RELATION_TABLE);

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
            db.execSQL("DROP TABLE IF EXISTS " + KEY_COMMENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + KEY_RELATION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + KEY_TAG_TABLE);
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

            values.put(KEY_USER_ID, user.userId);
            values.put(KEY_USER_NAME, user.username);
            values.put(KEY_USER_PASSWORD, user.password);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(KEY_USER_TABLE, values, KEY_USER_ID + "="+user.userId,null);

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_USER_ID, KEY_USER_TABLE, KEY_USER_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.userId)});
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

    public void addTopic(Topic topic){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TOPIC_ID, topic.topicId );
            values.put(KEY_TOPIC_NAME, topic.topicName);
            values.put(KEY_TAG_LIST, Arrays.toString(topic.tags.toArray()));

            db.insert(KEY_TOPIC_TABLE,null,values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public void updateTopic(Topic topic) {
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
       // return topicId;
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

    //HELPER FOR ARRAYLIST TO STRING
    private ArrayList<String> stringToList(String s){
        Log.i("TOPIC HELPER",s);
        s = s.replace("[","");
        s = s.replace("]","");
        ArrayList<String> output = new ArrayList<>(Arrays.asList(s.split(",")));
        Log.i("STRING OF FIRST",output.get(0));
        return output;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //COMMENT
    /////////////////////////////////////////////////////////////////////////////////

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateComment(Comment comment) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long commentId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_COMMENT_TOPIC_NAME, comment.topicName);
            values.put(KEY_COMMENT_CONTENT, comment.content);

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(KEY_COMMENT_TABLE, values, KEY_COMMENT_CONTENT + "= ?", new String[]{comment.content});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_COMMENT_ID, KEY_COMMENT_TOPIC_NAME, KEY_COMMENT_CONTENT);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(comment.topicName)});
                try {
                    if (cursor.moveToFirst()) {
                        commentId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                commentId = db.insertOrThrow(KEY_COMMENT_TABLE, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return commentId;
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();

        // SELECT * FROM POSTS
        String COMMENTS_SELECT_QUERY = "SELECT * FROM comments";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(COMMENTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.topicName = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_TOPIC_NAME));
                    comment.content = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_CONTENT));

                    comments.add(comment);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return comments;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //RELATION
    /////////////////////////////////////////////////////////////////////////////////

    // Insert or update a user in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateRelation(Relation relation) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long relationId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_RELATION_NAME, relation.relationName);
            values.put(KEY_RELATION_FIRST_TOPIC_NAME, relation.topicFrom);
            values.put(KEY_RELATION_SECOND_TOPIC_NAME, relation.topicTo);
            values.put(KEY_RELATION_IS_BIDIRECTIONAL, (relation.isBidirectional ? 1 : 0));

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(KEY_RELATION_TABLE, values, KEY_RELATION_NAME + "= ?", new String[]{relation.relationName});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_RELATION_ID, KEY_RELATION_FIRST_TOPIC_NAME, KEY_RELATION_SECOND_TOPIC_NAME,KEY_RELATION_IS_BIDIRECTIONAL);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(relation.relationName)});
                try {
                    if (cursor.moveToFirst()) {
                        relationId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // user with this userName did not already exist, so insert new user
                relationId = db.insertOrThrow(KEY_RELATION_TABLE, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return relationId;
    }

    public List<Relation> getAllRelations() {
        List<Relation> relations = new ArrayList<>();

        // SELECT * FROM POSTS
        String RELATIONS_SELECT_QUERY = "SELECT * FROM relations";

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(RELATIONS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                   Relation relation = new Relation();
                    relation.relationName = cursor.getString(cursor.getColumnIndex(KEY_RELATION_NAME));
                    relation.topicFrom = cursor.getString(cursor.getColumnIndex(KEY_RELATION_FIRST_TOPIC_NAME));
                    relation.topicTo = cursor.getString(cursor.getColumnIndex(KEY_RELATION_SECOND_TOPIC_NAME));
                    relation.isBidirectional = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_IS_BIDIRECTIONAL)) == 1 ? true:false;

                    relations.add(relation);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return relations;
    }

}
