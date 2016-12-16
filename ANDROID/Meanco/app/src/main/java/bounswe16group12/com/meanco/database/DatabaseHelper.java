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
import bounswe16group12.com.meanco.objects.Vote;

/**
 * Created by feper on 11/17/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "meancoDB";
    private static final int DATABASE_VERSION = 4;

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
    private static final String KEY_COMMENT_USERNAME = "username";

    //RELATION
    private static final String KEY_RELATION_TABLE = "relations";

    private static final String KEY_RELATION_ID = "id";
    private static final String KEY_RELATION_NAME = "relationName";
    private static final String KEY_RELATION_FIRST_TOPIC_ID = "firstTopicId";
    private static final String KEY_RELATION_SECOND_TOPIC_ID = "secondTopicId";
    private static final String KEY_RELATION_IS_BIDIRECTIONAL = "isbidirectional";

    //VOTE
    private static final String KEY_VOTE_TABLE = "vote";

    private static final String KEY_VOTE_COMMENT_ID = "commentId";
    private static final String KEY_VOTE_IS_UPVOTED = "isUpvoted";
    private static final String KEY_VOTE_IS_DOWNVOTED = "isDownvoted";

    /**
     * Use the application context, which will ensure that you
     * don't accidentally leak an Activity's context.
      */
    public static synchronized DatabaseHelper getInstance(Context context) {

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

    /**
     * Called when the database connection is being configured.
     * Configure database settings for things like foreign key support, write-ahead logging, etc.
     * @param db
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    /**
     * Called when the database is created for the FIRST time.
     * If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
      */

    @Override
    public void onCreate(SQLiteDatabase db) {
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
                KEY_COMMENT_CONTENT + " TEXT," +
                KEY_COMMENT_USERNAME + " TEXT" +
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

        String CREATE_VOTE_TABLE ="CREATE TABLE " + KEY_VOTE_TABLE +
                "(" +
                KEY_VOTE_COMMENT_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_VOTE_IS_UPVOTED + " INTEGER," +
                KEY_VOTE_IS_DOWNVOTED + " INTEGER" +
                ")";
        db.execSQL(CREATE_VOTE_TABLE);
    }

    /**
     * Called when the database needs to be upgraded.
     * This method will only be called if a database already exists on disk with the same DATABASE_NAME,
     * but the DATABASE_VERSION is different than the version of the database that exists on disk.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + KEY_TOPIC_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + KEY_COMMENT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + KEY_RELATION_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + KEY_TAG_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + KEY_VOTE_TABLE);

            onCreate(db);
        }
    }


    /**
     * 0 : TOPIC
     * 1 : TAG
     * 2 : COMMENT
     * 3 : RELATION
     * 4 : VOTE

     * @param tableId Clear table with given id.
     */
    public void clearTable(int tableId){
        String tableName = "";
        switch(tableId) {
            case 0 :
                tableName = KEY_TOPIC_TABLE;
                break;
            case 1 :
                tableName = KEY_TAG_TABLE;
                break;
            case 2 :
                tableName = KEY_COMMENT_TABLE;
                break;
            case 3 :
                tableName = KEY_RELATION_TABLE;
                break; // optional
            case 4 :
                tableName = KEY_VOTE_TABLE;
                break; // optional
            default :
        }

        getWritableDatabase().execSQL("DELETE FROM " + tableName);
    }

    /**
     * Clear all tables.
     */
    public void clearAll(){
        for(int i=0;i<5;i++)
            clearTable(i);
    }

    /**
     * Add topic to local db.
     * @param topic Topic to be added.
     */
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

    /**
     * Get topic from local db with given topic id.
     * Useful for getting topic of a comment, relation etc.
     * If returns null, to such topic.
     * @param topicId
     * @return topic
     */
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

    /**
     * Update a topic.
     * @param topic Topic to be updated.
     */
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

    /**
     * Useful for list views that require seeing all topics at once (homepage, search topic etc.)
     * @return all topics
     */
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

    //TODO: What is this?
    public List<Topic> getTopicsContainsText(String text) {
        List<Topic> topics = new ArrayList<>();

        String TOPICS_SELECT_QUERY = "SELECT * FROM topics WHERE " + KEY_TOPIC_NAME + " LIKE '%" + text + "%'";

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

    /**
     * Helper method: arraylist to string
     * @param s
     * @return output string
     */
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

    /**
     * Add tag to local db.
     * @param tag
     */

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

    /**
     * Get tag from db with given tag id.
     * Useful for getting tag from topic.
     * @param tagId
     * @return tag
     */
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

    /**
     * Get all tags used by topics (not whole wikidata ofc).
     * @param topicId
     * @return
     */
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

    /**
     * Add comment to local db.
     * @param comment
     */

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
               values.put(KEY_COMMENT_USERNAME,comment.username);

               db.insert(KEY_COMMENT_TABLE, null, values);
               db.setTransactionSuccessful();
           } catch (Exception e) {
               Log.d("USER DB HELPER", "Error while trying to add or update user");
           } finally {
               db.endTransaction();
           }
       }
   }

    /**
     * Method for editing a comment in topic detail page.
     * @param comment
     */

    public void updateComment(Comment comment){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_COMMENT_ID, comment.commentId );
            values.put(KEY_COMMENT_TOPIC_ID, comment.topicId );
            values.put(KEY_COMMENT_CONTENT,comment.content);
            values.put(KEY_COMMENT_USERNAME,comment.username);

            db.update(KEY_COMMENT_TABLE, values, KEY_COMMENT_ID + "= ?", new String[]{""+comment.commentId});
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Get comment from local db with given comment id.
     * @param commentId
     * @return
     */
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
                comment.username = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_USERNAME));
            }
        } finally {
            cursor.close();
        }
        return comment;
    }

    /**
     * Get all comments of a topic.
     * Useful for list view of comments in topic detail.
     * @param topicId
     * @return
     */
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
                    comment.username = cursor.getString(cursor.getColumnIndex(KEY_COMMENT_USERNAME));

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

    /**
     * Add relation to local db.
     * @param relation
     */

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

    //TODO: Why?
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

    /**
     * Get relation with given relation id.
     * Useful for getting relation from given topic.
     * @param relationId
     * @return
     */
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

    /**
     * Get all relations of a topic.
     * Useful for listing all relations on relations dialog created on long press on an item in homepage.
     * @param topicId
     * @return relations of topic
     */
    public List<Relation> getAllRelations(int topicId){
        String RELATIONS_SELECT_QUERY = "SELECT * FROM relations WHERE " + KEY_RELATION_FIRST_TOPIC_ID + " = '" + topicId +
                                        "' OR " + KEY_RELATION_SECOND_TOPIC_ID + " = '" + topicId + "'";

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

                    boolean isDuplicate = false;
                    if(relation.isBidirectional) {
                        for (Relation r : relations) {
                            if (relation.topicFrom == r.topicTo && relation.topicTo == r.topicFrom)
                                isDuplicate = true;
                        }
                    }
                    if(!isDuplicate)
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
    //VOTE
    /////////////////////////////////////////////////////////////////////////////////

    public void addVote(Vote vote){
        if(getVote(vote.commentId) != null){
            updateVote(vote);
        }
        else {

            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_VOTE_COMMENT_ID, vote.commentId);
                values.put(KEY_VOTE_IS_UPVOTED, vote.isUpvoted ? 1:0);
                values.put(KEY_VOTE_IS_DOWNVOTED, vote.isDownvoted ? 1:0);

                db.insert(KEY_VOTE_TABLE, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d("USER DB HELPER", "Error while trying to add or update user");
            } finally {
                db.endTransaction();
            }
        }
    }

    public void updateVote (Vote vote){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_VOTE_COMMENT_ID, vote.commentId);
            values.put(KEY_VOTE_IS_UPVOTED, vote.isUpvoted ? 1:0);
            values.put(KEY_VOTE_IS_DOWNVOTED, vote.isDownvoted ? 1:0);

            db.update(KEY_VOTE_TABLE, values, KEY_VOTE_COMMENT_ID + "= ?", new String[]{""+vote.commentId});
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
    }

    public Vote getVote(int commentId){
        SQLiteDatabase db = getReadableDatabase();
        String VOTE_SELECT_WITH_ID_QUERY = "SELECT * FROM "+ KEY_VOTE_TABLE +" WHERE "+ KEY_VOTE_COMMENT_ID +" = '" + commentId + "'";

        Cursor cursor = db.rawQuery(VOTE_SELECT_WITH_ID_QUERY,null);
        Vote vote = null;
        try {
            if (cursor.moveToFirst()) {
                vote = new Vote();
                vote.commentId = cursor.getInt(cursor.getColumnIndex(KEY_VOTE_COMMENT_ID));
                vote.isDownvoted = cursor.getInt(cursor.getColumnIndex(KEY_VOTE_IS_DOWNVOTED)) == 1;
                vote.isUpvoted = cursor.getInt(cursor.getColumnIndex(KEY_VOTE_IS_UPVOTED)) == 1;
            }
        } finally {
            cursor.close();
        }
        return vote;
    }

    public List<Vote> getAllVotes(int commentId){
        String VOTES_SELECT_QUERY = "SELECT * FROM "+ KEY_VOTE_TABLE +" WHERE " + KEY_VOTE_COMMENT_ID + " = '" + commentId + "'";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(VOTES_SELECT_QUERY, null);
        List<Vote> votes = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Vote vote = new Vote();
                    vote.commentId = cursor.getInt(cursor.getColumnIndex(KEY_VOTE_COMMENT_ID));
                    vote.isDownvoted = cursor.getInt(cursor.getColumnIndex(KEY_VOTE_IS_DOWNVOTED)) == 1;
                    vote.isUpvoted = cursor.getInt(cursor.getColumnIndex(KEY_VOTE_IS_UPVOTED)) == 1;

                    votes.add(vote);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("USER DB HELPER", "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return votes;
    }
}
