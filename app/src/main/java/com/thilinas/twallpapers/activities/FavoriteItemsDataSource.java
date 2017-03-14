package com.thilinas.twallpapers.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.thilinas.twallpapers.SQLData.MySQLiteHelper3;
import com.thilinas.twallpapers.models.Photo;

import java.util.ArrayList;


/**
 * Created by Thilina on 22-Feb-17.
 */

public class FavoriteItemsDataSource {

    public static final String FAVPICS_TABLE_NAME = "favourites";
    public static final String FAVPICS_COLUMN_ID = "id";
    public static final String FAVPICS_COLUMN_PID = "pid";
    public static final String FAVPICS_COLUMN_NAME = "name";
    public static final String FAVPICS_COLUMN_URL = "url";
    public static final String FAVPICS_COLUMN_LIKES = "likes";
    public static final String FAVPICS_COLUMN_DLIKES = "dlikes";
    public static final String FAVPICS_COLUMN_ADATE = "adate";
    public static final String FAVPICS_COLUMN_ISFAV = "isfav";
    private SQLiteDatabase database;
    private MySQLiteHelper3 dbHelper;


    public FavoriteItemsDataSource(Context context) {
        dbHelper = new MySQLiteHelper3(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public boolean insertItem (Photo photo) {
        Log.i("XXXXX"," insertItem");
        Log.i("XXXXX"," item"+photo.getName());
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVPICS_COLUMN_PID, photo.getpId());
        contentValues.put(FAVPICS_COLUMN_NAME, photo.getName());
        contentValues.put(FAVPICS_COLUMN_URL, photo.getUrl());
        contentValues.put(FAVPICS_COLUMN_URL, photo.getUrl());
        contentValues.put(FAVPICS_COLUMN_ISFAV, photo.getFav());
        contentValues.put(FAVPICS_COLUMN_LIKES, photo.getLikes());
        contentValues.put(FAVPICS_COLUMN_DLIKES, photo.getDisLikes());
        contentValues.put(FAVPICS_COLUMN_ADATE, photo.getsDate());

        long insertId = database.insert(FAVPICS_TABLE_NAME, null, contentValues);
       /* Cursor cursor = database.query(FAVPICS_TABLE_NAME, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();*/

       // database.insert(FAVPICS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Photo getItem(int id) {
        Log.i("XXXXX"," getItem");
        Cursor cursor = database.query(FAVPICS_TABLE_NAME, new String[] {
                                        FAVPICS_COLUMN_ID,
                                        FAVPICS_COLUMN_PID,
                                        FAVPICS_COLUMN_NAME,
                                        FAVPICS_COLUMN_URL,
                                        FAVPICS_COLUMN_LIKES,
                                        FAVPICS_COLUMN_DLIKES,
                                        FAVPICS_COLUMN_ADATE }, FAVPICS_COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Photo favItem = new Photo(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),cursor.getString(3),Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),cursor.getString(6));
        return favItem;
    }

    public ArrayList<Photo> getMyFavItems() {
        Log.i("XXXXX"," getmyFavItems");
        ArrayList<Photo> array_list = new ArrayList<Photo>();
        Cursor res =  database.rawQuery( "select * from "+FAVPICS_TABLE_NAME+ " WHERE "+FAVPICS_COLUMN_ISFAV + " = '1'", null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Photo photo = new Photo();
            photo.setId(res.getInt(res.getColumnIndex(FAVPICS_COLUMN_ID)));
            photo.setpId(res.getString(res.getColumnIndex(FAVPICS_COLUMN_PID)));
            photo.setName(res.getString(res.getColumnIndex(FAVPICS_COLUMN_NAME)));
            photo.setUrl(res.getString(res.getColumnIndex(FAVPICS_COLUMN_URL)));
            photo.setLikes(res.getInt(res.getColumnIndex(FAVPICS_COLUMN_LIKES)));
            photo.setDisLikes(res.getInt(res.getColumnIndex(FAVPICS_COLUMN_DLIKES)));
            array_list.add(photo);
            res.moveToNext();
        }
        return array_list;
    }

    public Photo getItembyURL(String url) {
        Log.i("XXXXX"," getItem");
        Cursor cursor = database.query(FAVPICS_TABLE_NAME, new String[] {
                        FAVPICS_COLUMN_ID,
                        FAVPICS_COLUMN_PID,
                        FAVPICS_COLUMN_NAME,
                        FAVPICS_COLUMN_URL,
                        FAVPICS_COLUMN_LIKES,
                        FAVPICS_COLUMN_DLIKES,
                        FAVPICS_COLUMN_ADATE }, FAVPICS_COLUMN_URL + "=?",
                new String[] { url }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Photo favItem = new Photo(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),cursor.getString(3),Integer.parseInt(cursor.getString(4)),Integer.parseInt(cursor.getString(5)),cursor.getString(6));
        return favItem;
    }

    public ArrayList<Photo> getAllFavItems() {
        Log.i("XXXXX"," getAllFavItems");
        ArrayList<Photo> array_list = new ArrayList<Photo>();
        Cursor res =  database.rawQuery( "select * from "+FAVPICS_TABLE_NAME, null );
        res.moveToFirst();
        while(res.isAfterLast() == false){
            Photo photo = new Photo();
            photo.setId(res.getInt(res.getColumnIndex(FAVPICS_COLUMN_ID)));
            photo.setpId(res.getString(res.getColumnIndex(FAVPICS_COLUMN_PID)));
            photo.setName(res.getString(res.getColumnIndex(FAVPICS_COLUMN_NAME)));
            photo.setUrl(res.getString(res.getColumnIndex(FAVPICS_COLUMN_URL)));
            photo.setLikes(res.getInt(res.getColumnIndex(FAVPICS_COLUMN_LIKES)));
            photo.setDisLikes(res.getInt(res.getColumnIndex(FAVPICS_COLUMN_DLIKES)));
            array_list.add(photo);
            res.moveToNext();
        }
        return array_list;
    }

    public boolean updateFavItem (Photo favItem) {
        Log.i("XXXXX"," updateFavItem");
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAVPICS_COLUMN_PID, favItem.getpId());
        contentValues.put(FAVPICS_COLUMN_NAME, favItem.getName());
        contentValues.put(FAVPICS_COLUMN_URL, favItem.getUrl());
        contentValues.put(FAVPICS_COLUMN_LIKES, favItem.getLikes());
        contentValues.put(FAVPICS_COLUMN_DLIKES, favItem.getDisLikes());
        contentValues.put(FAVPICS_COLUMN_ADATE, favItem.getsDate());
        database.update(FAVPICS_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(favItem.getId()) } );
        return true;
    }

    public Integer deleteItem (Integer id) {
        Log.i("XXXXX"," deleteItem");
        return database.delete(FAVPICS_TABLE_NAME, "id = ? ",  new String[] { Integer.toString(id) });
    }

    public int numberOfRows(){
        Log.i("XXXXX"," numberOfRows");
        int numRows = (int) DatabaseUtils.queryNumEntries(database, FAVPICS_TABLE_NAME);
        return numRows;
    }

    public void close() {
        dbHelper.close();
    }






 /*

    public Comment createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<Comment>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setComment(cursor.getString(1));
        return comment;
    }*/
}
