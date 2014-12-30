package test.com.adbirdads;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class AdBirdContentProvider extends ContentProvider{
	/*
	 * references: Content Provider Tutorials
	 * http://mobile.tutsplus.com/tutorials/android/android-sdk_content-providers/
	 * http://www.vogella.com/articles/AndroidSQLite/article.html
	 */
	private DatabaseHandler dh;

	static HashMap<String, String> sScratchTablesProjectionMap;
	//static HashMap<String,String> sScratchChecklistProjectionMap;

	private static final String AUTHORITY = ".AdBirdContentProvider";

	//Tables' + Scratch tables base path
	private static final String USER_BASE_PATH = DatabaseHandler.TABLE_USER;
	private static final String SCRATCH_USER_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_USER;

	private static final String THEMES_BASE_PATH = DatabaseHandler.TABLE_THEMES;
	private static final String SCRATCH_THEMES_BASE_PATH = DatabaseHandler.TABLE_SCRATCH_THEMES;
	
	private static final String ADS_BASE_PATH = DatabaseHandler.TABLE_ADS;
	private static final String HISTORY_BASE_PATH = DatabaseHandler.TABLE_HISTORY;

	//Content URIs
	//USER and Scratch USER
	public static final Uri CONTENT_URI_USER = Uri.parse("content://" + AUTHORITY + "/" + USER_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_USER = Uri.parse("content://" + AUTHORITY + "/" +
			SCRATCH_USER_BASE_PATH);
	//THEMES and Scratch THEMES
	public static final Uri CONTENT_URI_THEMES = Uri.parse("content://" + AUTHORITY + "/" + 
			THEMES_BASE_PATH);
	public static final Uri CONTENT_URI_SCRATCH_THEMES = Uri.parse("content://" + AUTHORITY + "/" + 
			SCRATCH_THEMES_BASE_PATH);
	
	//ADS URI
	public static final Uri CONTENT_URI_ADS = Uri.parse("content://" + AUTHORITY + "/" + 
			ADS_BASE_PATH);
	//HISTORY URI
	public static final Uri CONTENT_URI_HISTORY = Uri.parse("content://" + AUTHORITY + "/" + 
			HISTORY_BASE_PATH);

	//don't really know what these are for, but let's leave them anyway :)
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/user";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/user";

	//Used for the Uri matcher

	//task and scratch task
	private static final int USER_COLLECTION = 100;
	private static final int USER_ID = 110;

	private static final int SCRATCH_USER_COLLECTION = 120;
	private static final int SCRATCH_USER_ID = 130;

	//checklist and scratch checklist
	private static final int THEMES_COLLECTION = 200;
	private static final int THEMES_ID = 210;

	private static final int SCRATCH_THEMES_COLLECTION = 220;
	private static final int SCRATCH_THEMES_ID = 230;

	private static final int ADS_COLLECTION = 300;
	private static final int ADS_ID = 310;
	
	private static final int HISTORY_COLLECTION = 400;
	private static final int HISTORY_ID = 410;



	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		//task and scratch task
		sURIMatcher.addURI(AUTHORITY, USER_BASE_PATH, USER_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, USER_BASE_PATH + "/#", USER_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_USER_BASE_PATH, SCRATCH_USER_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_USER_BASE_PATH + "/#", SCRATCH_USER_ID);

		//checklist and scratch checklist
		sURIMatcher.addURI(AUTHORITY, THEMES_BASE_PATH, THEMES_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, THEMES_BASE_PATH + "/#", THEMES_ID);

		sURIMatcher.addURI(AUTHORITY, SCRATCH_THEMES_BASE_PATH, SCRATCH_THEMES_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, SCRATCH_THEMES_BASE_PATH + "/#", SCRATCH_THEMES_ID);
		
		//ADS
		sURIMatcher.addURI(AUTHORITY, ADS_BASE_PATH, ADS_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, ADS_BASE_PATH + "/#", ADS_ID);
		
		//HISTORY
		sURIMatcher.addURI(AUTHORITY, HISTORY_BASE_PATH, HISTORY_COLLECTION);
		sURIMatcher.addURI(AUTHORITY, HISTORY_BASE_PATH + "/#", HISTORY_ID);
	}



	@Override
	public boolean onCreate() {
		dh = DatabaseHandler.getInstance(getContext());
		return false;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}


	public String getTableName(Uri uri) {
		int uriType = sURIMatcher.match(uri);

		switch (uriType) {
		case USER_COLLECTION:
			return DatabaseHandler.TABLE_USER;
		case USER_ID:
			return DatabaseHandler.TABLE_USER;
		case SCRATCH_USER_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_USER;
		case SCRATCH_USER_ID:
			return DatabaseHandler.TABLE_SCRATCH_USER;
		case THEMES_COLLECTION:
			return DatabaseHandler.TABLE_THEMES;
		case THEMES_ID:
			return DatabaseHandler.TABLE_THEMES;
		case SCRATCH_THEMES_COLLECTION:
			return DatabaseHandler.TABLE_SCRATCH_THEMES;
		case SCRATCH_THEMES_ID:
			return DatabaseHandler.TABLE_SCRATCH_THEMES;
		case ADS_COLLECTION:
			return DatabaseHandler.TABLE_ADS;
		case ADS_ID:
			return DatabaseHandler.TABLE_ADS;
		case HISTORY_COLLECTION:
			return DatabaseHandler.TABLE_HISTORY;
		case HISTORY_ID:
			return DatabaseHandler.TABLE_HISTORY;
		
		default:
			return null;
		}

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		String tableName = getTableName(uri);
		queryBuilder.setTables(tableName);




		int uriType = sURIMatcher.match(uri);

		switch(uriType) {
		case USER_COLLECTION:
			break;
		case USER_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case SCRATCH_USER_COLLECTION:
			break;
		case SCRATCH_USER_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case THEMES_COLLECTION:
			break;
		case THEMES_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		case SCRATCH_THEMES_COLLECTION:
			break;
		case SCRATCH_THEMES_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
			
		case ADS_COLLECTION:
			break;
		case ADS_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
			
		case HISTORY_COLLECTION:
			break;
		case HISTORY_ID:
			queryBuilder.appendWhere(DatabaseHandler.KEY_COLUMNID + "=" + 
					uri.getLastPathSegment());
			break;
		
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		Cursor result = queryBuilder.query(dh.getReadableDatabase(), 
				projection, selection, selectionArgs, null, null, sortOrder);
		result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dh.getWritableDatabase();
		String tableName = getTableName(uri);
		int uriType = sURIMatcher.match(uri);
		long id = 0;
		switch(uriType) {
		case USER_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_USER_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case THEMES_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case SCRATCH_THEMES_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case ADS_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		case HISTORY_COLLECTION:
			id = db.insert(tableName, null, values);
			break;
		
		}

		getContext().getContentResolver().notifyChange(uri,null);
		return ContentUris.withAppendedId(uri,id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int uriType = sURIMatcher.match(uri);
		int rowsUpdated = 0;
		String tableName = getTableName(uri);
		String id = uri.getLastPathSegment();

		switch(uriType) {
		case USER_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case USER_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + " = " + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_USER_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_USER_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case THEMES_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case THEMES_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_THEMES_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case SCRATCH_THEMES_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
			
		case ADS_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case ADS_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + " = " + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
			
		case HISTORY_COLLECTION:
			rowsUpdated = db.update(tableName, values, selection, selectionArgs);
			break;
		case HISTORY_ID:
			if(TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(tableName, values, DatabaseHandler.KEY_COLUMNID + " = " + id, null);
			} else {
				rowsUpdated = db.update(tableName, values, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsDeleted = 0;
		SQLiteDatabase db = dh.getWritableDatabase();
		int uriType = sURIMatcher.match(uri);
		String tableName = getTableName(uri);
		String id = uri.getLastPathSegment();

		switch(uriType) {
		case USER_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case USER_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_USER_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_USER_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case THEMES_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case THEMES_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
		case SCRATCH_THEMES_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case SCRATCH_THEMES_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
			
		case ADS_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case ADS_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
			
		case HISTORY_COLLECTION:
			rowsDeleted = db.delete(tableName, selection, selectionArgs);
			break;
		case HISTORY_ID:
			id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(tableName, DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(tableName, selection + "and" 
						+ DatabaseHandler.KEY_COLUMNID + "=" + id, null);
			}
			break;
	
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}


}
