package com.test.bemillionaire;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.GregorianCalendar;

public class DB {
	private static final String LOG_TAG = "mill/DB";

	private static final String DB_NAME = "db_quiz";
	private static final int DB_VERSION = 1;

	private static final String TABLE_ANSWERS = "answers";
	public static final String ANSWER_ID = "_id";
	public static final String ANSWER_TITLE = "answer_title";
	public static final String ANSWER_ISTRUE = "is_true";
	public static final String ANSWER_CASH_POSITION = "cash_position";
	public static final String ANSWER_QUESTION_ID = "question_id";

	private static final String TABLE_ANSWER_CREATE =
	    "create table " + TABLE_ANSWERS + "(" +
				ANSWER_ID + " integer primary key autoincrement, " +
				ANSWER_QUESTION_ID + " integer,"+
				ANSWER_ISTRUE + " integer,"+
				ANSWER_CASH_POSITION + " integer,"+
				ANSWER_TITLE + " text"+
	      ");";



	private static final String TABLE_QUESTION = "questions";
	public static final String QUESTION_ID = "_id";
	public static final String QUESTION_TITLE = "question_title";
	private static final String TABLE_QUESTION_CREATE =
			"create table " + TABLE_QUESTION + "(" +
					QUESTION_ID + " integer primary key autoincrement, " +
					QUESTION_TITLE + " text"+
					");";


	private final Context context;
	private DBHelper mDBHelper;
	private SQLiteDatabase mDB;
	  
	public DB(Context context) {
		this.context = context;
	}

	public void open() {
		mDBHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
	    mDB = mDBHelper.getWritableDatabase();

	}
	public void close() {
	    if (mDBHelper!=null) mDBHelper.close();
	}

	public boolean checkForTables(){
		mDB = mDBHelper.getWritableDatabase();
		Cursor cursor = mDB.rawQuery("SELECT * FROM " + TABLE_ANSWERS, null);
		if(cursor != null && cursor.getCount() > 0){
			cursor.close();
			return true;
		}
		return false;
	}

	public void clearTables() {
		mDB.delete(TABLE_ANSWERS, null, null);
		mDB.delete(TABLE_QUESTION, null, null);
	}
	public void addAnswer(String answer, boolean istrue, String question) {

		int flag_istrue = (istrue)? 1 : 0;
		Cursor c=mDB.query(TABLE_QUESTION, null, QUESTION_TITLE + "=" + "'"+question+"'", null, null, null, null);
		c.moveToFirst();
		int indexCatId=c.getColumnIndex(QUESTION_ID);
		String catId=c.getString(indexCatId);
		ContentValues cv = new ContentValues();
		cv.put(ANSWER_TITLE, answer);
		cv.put(ANSWER_ISTRUE, flag_istrue);
		cv.put(ANSWER_QUESTION_ID,catId);
		mDB.insert(TABLE_ANSWERS, null, cv);
	}

	public void addQuestion(String question) {
		ContentValues cv = new ContentValues();
		cv.put(QUESTION_TITLE, question);
		mDB.insert(TABLE_QUESTION, null, cv);
	}


	public  Cursor getAnswersSortedbyQuestion(String question){
		Cursor c =mDB.rawQuery("SELECT * FROM " + TABLE_QUESTION + " WHERE " + QUESTION_TITLE + "=" +"'"+ question+"'", null);
		c.moveToFirst();
		int indexCatId=c.getColumnIndex(QUESTION_ID);
		int catId=c.getInt(indexCatId);
		c.close();
		return mDB.rawQuery("SELECT * FROM " + TABLE_ANSWERS + " WHERE " + ANSWER_QUESTION_ID + "=" + catId, null);
	}

	public Cursor getQuestions() {
		return mDB.query(TABLE_QUESTION, null, null, null, null, null, null);
	}




	private class DBHelper extends SQLiteOpenHelper {

	    public DBHelper(Context context, String name, CursorFactory factory, int version) {
	      super(context, name, factory, version);
	    }
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	      	db.execSQL(TABLE_ANSWER_CREATE);
			db.execSQL(TABLE_QUESTION_CREATE);

	    }

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    	
	    	db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ANSWERS + "'");
			db.execSQL("DROP TABLE IF EXISTS '" + TABLE_QUESTION + "'");

	    	onCreate(db);
	    }
	    
	  }


}


