package com.misortel.tincan.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.misortel.tincan.CallLogObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCon {
	//android application development tutorial
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CONTACT = "call_contact";
	public static final String KEY_TIME = "call_time";
	public static final String KEY_DATE = "call_date";
	private static final String DATABASE_NAME = "dbCall";
	private static final String DATABASE_TABLE = "tblCallLog";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper thisHelper;
	private final Context thisContext;
	private SQLiteDatabase db;
	private CallLogObject objCall;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " +  DATABASE_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_CONTACT + " TEXT NOT NULL, " +
					KEY_TIME + " TEXT NOT NULL, " +
					KEY_DATE + " TEXT NOT NULL);"					
			);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	public DBCon(Context c){
		thisContext = c;
	}
	
	public DBCon open() throws SQLException{
		thisHelper = new DbHelper(thisContext);
		db = thisHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		thisHelper.close();
	}

	public long createEntry(String name, String time) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MMM/dd");
		String today = df.format(c.getTime());
		ContentValues cv = new ContentValues();
		cv.put(KEY_CONTACT, name);
		cv.put(KEY_TIME, time);
		cv.put(KEY_DATE, today);
		return db.insert(DATABASE_TABLE, null, cv);
	}
	
	public void truncate(){
		//db.execSQL("DELETE from " + DATABASE_TABLE);
		
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		db.execSQL("CREATE TABLE " +  DATABASE_TABLE + " (" +
				KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				KEY_CONTACT + " TEXT NOT NULL, " +
				KEY_TIME + " TEXT NOT NULL, " +
				KEY_DATE + " TEXT NOT NULL);"					
		);
	}
	
	public String getData() {
		String result = "";
		
		// TODO Auto-generated method stub
		String[] columns = new String[]{KEY_ROWID, KEY_CONTACT, KEY_TIME};
		/*Cursor c = db.query(DATABASE_TABLE, columns, null, null, null, null, null);
		int iRow = c.getColumnIndex(KEY_ROWID);
		int iContact = c.getColumnIndex(KEY_CONTACT);
		int iTime = c.getColumnIndex(KEY_TIME);*/
		///////////////
		
		
		Cursor c = db.rawQuery(
				"SELECT * from " + DATABASE_TABLE + " ORDER BY " + KEY_ROWID + " DESC;", null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + "\n" + c.getString(c.getColumnIndex(KEY_CONTACT)) + "\t" + c.getString(c.getColumnIndex(KEY_TIME)) + "\t" + c.getString(c.getColumnIndex(KEY_DATE));
		}
		
		return result;
	}
	
	/*public ArrayList getData(){
		ArrayList data = new ArrayList();
		Cursor c = db.rawQuery(
				"SELECT * from " + DATABASE_TABLE + ";", null);
		data.clear();
		//objCall = new CallLogObject();
		
		//SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, , c, from, to)

		while(c.moveToNext()){
			objCall.setContact(c.getString(c.getColumnIndex(KEY_CONTACT)));
			//objCall.setDuration(c.getString(c.getColumnIndex(KEY_TIME)));
			data.add(objCall);
		}
		//for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

		//}

		return data;
	}*/
	
	/*public List getData(){
		List list = new ArrayList<CallLogObject>();
		Cursor c = db.rawQuery(
				"SELECT * from " + DATABASE_TABLE + ";", null);
		list.clear();
		while(c.moveToNext()){
			objCall = new CallLogObject();
			objCall.setContact(c.getString(c.getColumnIndex(KEY_CONTACT)));
			objCall.setDuration(c.getString(c.getColumnIndex(KEY_TIME)));
			list.add(objCall);
		}
		return list;		
	}*/
	
	/*public String[] getData(){
		
	}*/
	
}
