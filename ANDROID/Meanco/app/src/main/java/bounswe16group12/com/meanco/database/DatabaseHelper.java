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
import bounswe16group12.com.meanco.objects.Tag;
import bounswe16group12.com.meanco.objects.Topic;
import bounswe16group12.com.meanco.objects.User;

/**
 * Created by feper on 11/17/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "meancoDB";
    private static final int DATABASE_VERSION = 3;

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
    private static final String KEY_TAG_URL = "url";

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
                KEY_TAG_DESCRIPTION + " TEXT," +
                KEY_TAG_URL + " TEXT" +
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
    //TOPIC
    /////////////////////////////////////////////////////////////////////////////////

    //Adds topic
    public void addTopic(Topic topic){
        if(getTopic(topic.topicId) != null){
            updateTopic(topic);
        }
        else {
            SQLiteDatabase db = getWritableDatabase();

            List<Tag> tags = topic.tags;
            ArrayList<String> tagIds = new ArrayList<String>();

            for (Tag tag : tags) {
                tagIds.add("" + tag.tagId);
            }

            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_TOPIC_ID, topic.topicId);
                values.put(KEY_TOPIC_NAME, topic.topicName);
                values.put(KEY_TAG_LIST, Arrays.toString(tagIds.toArray()));

                db.insert(KEY_TOPIC_TABLE, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d("USER DB HELPER", "Error while trying to add or update user");
            } finally {
                db.endTransaction();
            }
        }
    }

    //If returns null, no such topic.
    public Topic getTopic(int topicId){
        SQLiteDatabase db = getReadableDatabase();

        String TOPIC_SELECT_WITH_ID_QUERY = "SELECT * FROM topics WHERE id = '" + topicId + "'";

        Cursor cursor = db.rawQuery(TOPIC_SELECT_WITH_ID_QUERY,null);
        Topic topic = null;
        try {
            if (cursor.moveToFirst()) {
                topic = new Topic();
                topic.topicId = cursor.getInt(cursor.getColumnIndex(KEY_TOPIC_ID));
                topic.topicName = cursor.getString(cursor.getColumnIndex(KEY_TOPIC_NAME));
                String tags = cursor.getString(cursor.getColumnIndex(KEY_TAG_LIST));

                ArrayList<String> tagIdList = stringToList(tags);
                ArrayList<Tag> tagList = new ArrayList<Tag>();
                if(tagIdList!=null) {
                    for (String s : tagIdList) {
                        int tagId = Integer.parseInt(s);
                        Tag t = getTag(tagId);
                        tagList.add(t);
                    }
                }

                topic.tags = tagList;
            }
        } finally {
            cursor.close();
        }
        return topic;
    }

    //Update topic
    public void updateTopic(Topic topic) {

        SQLiteDatabase db = getWritableDatabase();

        List<Tag> tags = topic.tags;
        ArrayList<String> tagIds = new ArrayList<String>();

        for(Tag tag : tags){
            tagIds.add(""+tag.tagId);
        }

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TOPIC_ID, topic.topicId );
            values.put(KEY_TOPIC_NAME, topic.topicName);
            values.put(KEY_TAG_LIST, Arrays.toString(tagIds.toArray()));

            db.update(KEY_TOPIC_TABLE, values, KEY_TOPIC_ID + "= ?", new String[]{""+topic.topicId});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to update topic");
        } finally {
            db.endTransaction();
        }
    }

    //Returns all topics.
    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<>();

        String TOPICS_SELECT_QUERY = "SELECT * FROM topics";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TOPICS_SELECT_QUERY, null);
        try {

            if (cursor.moveToFirst()) {
                do {

                    Topic topic = new Topic();
                    topic.topicId = cursor.getInt(cursor.getColumnIndex(KEY_TOPIC_ID));
                    topic.topicName = cursor.getString(cursor.getColumnIndex(KEY_TOPIC_NAME));
                    String tags = cursor.getString(cursor.getColumnIndex(KEY_TAG_LIST));

                    ArrayList<String> tagIdList = stringToList(tags);

                    if(tagIdList!=null) {
                        ArrayList<Tag> tagList = new ArrayList<Tag>();
                        for (String s : tagIdList) {
                            int tagId = Integer.parseInt(s);
                            Tag t = getTag(tagId);
                            tagList.add(t);
                        }

                        topic.tags = tagList;
                    }else {
                        topic.tags = null;
                    }
                    topics.add(topic);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            //Log.d("USER DB HELPER", "Error while trying to get posts from database");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return topics;
    }

    //HELPER FOR ARRAYLIST TO STRING
    private ArrayList<String> stringToList(String s){
        ArrayList<String> output = null;
        s = s.replaceAll(" ", "");
        s = s.replace("[","");
        s = s.replace("]","");
        if(!s.equals("")){
            output = new ArrayList<>(Arrays.asList(s.split(",")));
        }
        return output;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //TAG
    /////////////////////////////////////////////////////////////////////////////////

    public void addTag(Tag tag){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TAG_ID, tag.tagId );
            values.put(KEY_TAG_NAME, tag.tagName );
            values.put(KEY_TAG_DESCRIPTION,tag.context);
            values.put(KEY_TAG_URL,tag.URL);

            db.insert(KEY_TAG_TABLE,null,values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    public Tag getTag(int tagId){
        SQLiteDatabase db = getReadableDatabase();
        String TAG_SELECT_WITH_ID_QUERY = "SELECT * FROM tags WHERE id = '" + tagId + "'";

        Cursor cursor = db.rawQuery(TAG_SELECT_WITH_ID_QUERY,null);
        Tag tag = null;
        try {
            if (cursor.moveToFirst()) {
                tag = new Tag();
                tag.tagId = cursor.getInt(cursor.getColumnIndex(KEY_TAG_ID));
                tag.tagName = cursor.getString(cursor.getColumnIndex(KEY_TAG_NAME));
                tag.context = cursor.getString(cursor.getColumnIndex(KEY_TAG_DESCRIPTION));
                tag.URL = cursor.getString(cursor.getColumnIndex(KEY_TAG_URL));
            }
        } finally {
            cursor.close();
        }
        return tag;
    }

    public List<Tag> getAllTags(int topicId){
        Topic topic = getTopic(topicId);

        ArrayList<Integer> tagIds = new ArrayList<>();

        for(Tag tag : topic.tags){
            tagIds.add(tag.tagId);
        }

        List<Tag> tags = new ArrayList<>();

        String TAGS_SELECT_QUERY = "SELECT * FROM tags";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TAGS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Tag tag = new Tag();
                    tag.tagId = cursor.getInt(cursor.getColumnIndex(KEY_TAG_ID));

                    if(tagIds.contains(tag.tagId)) {
                        tag.tagName = cursor.getString(cursor.getColumnIndex(KEY_TAG_NAME));
                        tag.context = cursor.getString(cursor.getColumnIndex(KEY_TAG_DESCRIPTION));
                        tag.URL = cursor.getString(cursor.getColumnIndex(KEY_TAG_URL));

                        tags.add(tag);
                    }
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return tags;
    }

    //////////////////////////////////////////////////////////////////////////////////
    //COMMENT
    /////////////////////////////////////////////////////////////////////////////////

   public void addComment(Comment comment){
       if(getComment(comment.commentId) != null){
           updateComment(comment);
       }
       else {

           SQLiteDatabase db = getWritableDatabase();
           db.beginTransaction();
           try {
               ContentValues values = new ContentValues();
               values.put(KEY_COMMENT_ID, comment.commentId);
               values.put(KEY_COMMENT_TOPIC_ID, comment.topicId);
               values.put(KEY_COMMENT_CONTENT, comment.content);

               db.insert(KEY_COMMENT_TABLE, null, values);
               db.setTransactionSuccessful();
           } catch (Exception e) {
               Log.d("USER DB HELPER", "Error while trying to add or update user");
           } finally {
               db.endTransaction();
           }
       }
   }

    public void updateComment(Comment comment){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_COMMENT_ID, comment.commentId );
            values.put(KEY_COMMENT_TOPIC_ID, comment.topicId );
            values.put(KEY_COMMENT_CONTENT,comment.content);

            db.update(KEY_COMMENT_TABLE, values, KEY_COMMENT_ID + "= ?", new String[]{""+comment.commentId});
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    public Comment getComment(int commentId){
        SQLiteDatabase db = getReadableDatabase();
        String TAG_SELECT_WITH_ID_QUERY = "SELECT * FROM comments WHERE id = '" + commentId + "'";

        Cursor cursor = db.rawQuery(TAG_SELECT_WITH_ID_QUERY,null);
       Comment comment = null;
        try {
            if (cursor.moveToFirst()) {
                comment = new Comment();
                comment.commentId = cursor.getInt(cursor.getColumnIndex(KEY_COMMENT_ID));
                comment.topicId = cursor.getInt(cursor.getColumnIndex(KEY_COMMENT_TOPIC_ID));
                comment.content = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_CONTENT));
            }
        } finally {
            cursor.close();
        }
        return comment;
    }

    public List<Comment> getAllComments(int topicId){
        String COMMENTS_SELECT_QUERY = "SELECT * FROM comments WHERE " + KEY_COMMENT_TOPIC_ID + " = '" + topicId + "'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(COMMENTS_SELECT_QUERY, null);
        List<Comment> comments = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.commentId = cursor.getInt(cursor.getColumnIndex(KEY_COMMENT_ID));
                    comment.topicId = cursor.getInt(cursor.getColumnIndex(KEY_COMMENT_TOPIC_ID));
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

    public void addRelation(Relation relation){
        if(getRelation(relation.relationId) != null){
            updateRelation(relation);
        }
        else {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_RELATION_ID, relation.relationId);
                values.put(KEY_RELATION_NAME, relation.relationName);
                values.put(KEY_RELATION_FIRST_TOPIC_ID, relation.topicFrom);
                values.put(KEY_RELATION_SECOND_TOPIC_ID, relation.topicTo);
                values.put(KEY_RELATION_IS_BIDIRECTIONAL, relation.isBidirectional ? 1 : 0);

                db.insert(KEY_RELATION_TABLE, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d("USER DB HELPER", "Error while trying to add or update user");
            } finally {
                db.endTransaction();
            }
        }
    }

    public void updateRelation(Relation relation){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_RELATION_ID, relation.relationId );
            values.put(KEY_RELATION_NAME, relation.relationName );
            values.put(KEY_RELATION_FIRST_TOPIC_ID,relation.topicFrom);
            values.put(KEY_RELATION_SECOND_TOPIC_ID,relation.topicTo);
            values.put(KEY_RELATION_IS_BIDIRECTIONAL,relation.isBidirectional ? 1:0);

            db.update(KEY_RELATION_TABLE, values, KEY_RELATION_ID + "= ?", new String[]{""+relation.relationId});
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    public Relation getRelation(int relationId){
        SQLiteDatabase db = getReadableDatabase();
        String TAG_SELECT_WITH_ID_QUERY = "SELECT * FROM relations WHERE id = '" + relationId + "'";

        Cursor cursor = db.rawQuery(TAG_SELECT_WITH_ID_QUERY,null);
        Relation relation = null;
        try {
            if (cursor.moveToFirst()) {
                relation = new Relation();
                relation.relationId = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_ID));
                relation.relationName = cursor.getString(cursor.getColumnIndex(KEY_RELATION_NAME));
                relation.topicFrom = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_FIRST_TOPIC_ID));
                relation.topicTo = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_SECOND_TOPIC_ID));
                relation.isBidirectional = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_IS_BIDIRECTIONAL)) == 1; //if 1 -> true , else false

            }
        } finally {
            cursor.close();
        }
        return relation;
    }

    public List<Relation> getAllRelations(int topicId){
        String RELATIONS_SELECT_QUERY = "SELECT * FROM relations WHERE " + KEY_RELATION_FIRST_TOPIC_ID + " = '" + topicId + "'";
        //TODO: Add second Relation ID equality with OR

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(RELATIONS_SELECT_QUERY, null);
        List<Relation> relations = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Relation relation = new Relation();
                    relation.relationId = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_ID));
                    relation.relationName = cursor.getString(cursor.getColumnIndex(KEY_RELATION_NAME));
                    relation.topicFrom = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_FIRST_TOPIC_ID));
                    relation.topicTo = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_SECOND_TOPIC_ID));
                    relation.isBidirectional = cursor.getInt(cursor.getColumnIndex(KEY_RELATION_IS_BIDIRECTIONAL)) == 1; //if 1 -> true , else false

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



    //////////////////////////////////////////////////////////////////////////////////
    //USER
    /////////////////////////////////////////////////////////////////////////////////

    //TODO: Add login methods and alter user part
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

}
