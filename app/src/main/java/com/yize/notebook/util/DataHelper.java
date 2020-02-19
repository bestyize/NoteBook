package com.yize.notebook.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yize.notebook.Note;

import java.util.ArrayList;
import java.util.List;

import static com.yize.notebook.util.NoteDatabase.CONTENT;
import static com.yize.notebook.util.NoteDatabase.ID;
import static com.yize.notebook.util.NoteDatabase.MODE;
import static com.yize.notebook.util.NoteDatabase.TABLE_NAME;
import static com.yize.notebook.util.NoteDatabase.TIME;

public class DataHelper {
    private SQLiteOpenHelper helper;
    private SQLiteDatabase database;
    private static final String[] columns={
            NoteDatabase.ID,
            CONTENT,
            TIME,
            NoteDatabase.MODE
    };

    public DataHelper(Context context) {
        helper=new NoteDatabase(context);
    }

    /**
     * 增加记录
     * @param note
     * @return
     */
    public Note addNote(Note note){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CONTENT,note.getContent());
        contentValues.put(TIME,note.getTime());
        contentValues.put(NoteDatabase.MODE,note.getTag());
        long id=database.insert(NoteDatabase.TABLE_NAME,null,contentValues);
        note.setId(id);
        return note;

    }

    public Note getNote(long id){
        Cursor cursor=database.query(NoteDatabase.TABLE_NAME,columns,NoteDatabase.ID+"=?",
                new String[]{String.valueOf(NoteDatabase.ID)},null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        return new Note(cursor.getString(1),cursor.getString(2),cursor.getInt(3));
    }

    public List<Note> getNoteList(){
        Cursor cursor=database.query(NoteDatabase.TABLE_NAME,columns,null,null,null,null,null);
        Log.i("Error",""+cursor.getCount());
        List<Note> list=new ArrayList<>();
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                list.add(new Note(cursor.getLong(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)));
            }
        }
        return list;
    }

    public int updateNote(Note note){
        ContentValues contentValues=new ContentValues();
        contentValues.put(CONTENT,note.getContent());
        contentValues.put(TIME,note.getTime());
        contentValues.put(MODE,note.getTag());
        return database.update(TABLE_NAME,contentValues,NoteDatabase.ID+"=?",new String[]{String.valueOf(note.getId())});
    }

    public void removeNote(Note note){
        database.delete(TABLE_NAME,ID+"=?",new String[]{String.valueOf(note.getId())});
    }

    public void open(){
        database=helper.getWritableDatabase();
    }
    public void close(){
        helper.close();
    }
}
