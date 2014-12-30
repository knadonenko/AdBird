package test.com.adbirdads;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	public static final String SCRATCH = "scratch";
	// table names
	public static final String TABLE_USER = "User";
	public static final String TABLE_THEMES = "Themes";
	public static final String TABLE_ADS = "Ads";
	public static final String TABLE_HISTORY = "History";

	// scratch tables
	public static final String TABLE_SCRATCH_USER = SCRATCH + TABLE_USER;
	public static final String TABLE_SCRATCH_THEMES = SCRATCH + TABLE_THEMES;
	public static final String TABLE_SCRATCH_ADS = SCRATCH + TABLE_ADS;

	// key names
	// USER
	public static final String KEY_COLUMNID = "_id";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_ID = "themeId";
	public static final String KEY_LASTUPDATED = "lastUpdated";
	public static final String KEY_NAME = "name";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_AMOUNT = "amount";
	public static final String KEY_RATING = "rating";
	public static final String KEY_RATING_CHANGE = "ratingChange";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_CITY_UPDATE_AVAILABLE = "cityUpdateAvailable";
	public static final String KEY_THEME_UPDATE_AVAILABLE = "themesUpdateAvailable";
	
	// THEMES
	public static final String KEY_THEMES_NAME = "theme_name";
	public static final String KEY_THEMES_NAME_RUS = "theme_name_rus";
	public static final String KEY_SELECTED = "selected";
	
	//ADS
	//
	public static final String KEY_AD_ID = "adID";
	public static final String KEY_PICTURE = "adPicture";
	public static final String KEY_AD_TEXT = "adText";
	public static final String KEY_AD_TEXT_HASHTAGS = "adTextHashtags";
	public static final String KEY_AD_TEXT_REFERENCES = "adTextReferences";
	public static final String KEY_DEADLINE = "adDeadline";
	public static final String KEY_COMPANY_NAME = "companyName";
	public static final String KEY_COMPANY_LOGO = "companyLogo";
	public static final String KEY_AD_STATUS = "adStatus";
	public static final String KEY_COMPANY_USERNAME = "companyUsername";
	public static final String KEY_AD_TIMESTAMP = "adTimestamp";
	
	//ads | history
	public static final String KEY_HISTORY_ID = "historyID";
	public static final String KEY_POSTING_DATE = "postingDate";
	public static final String KEY_PAYMENT = "payment";
	public static final String KEY_CUSTORMER_ID = "customerID";
	public static final String KEY_HAS_HISTORY = "hasHistory";

	

	// The database instance - keep it static
	private static DatabaseHandler instance = null;

	// Database version
	private static final int DATABASE_VERSION = 1;

	// Database name
	private static final String DATABASE_NAME = "adBirdDatabase";

	private DatabaseHandler(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHandler getInstance(Context ctx) {
		if (instance == null)
			instance = new DatabaseHandler(ctx.getApplicationContext());

		return instance;
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + TABLE_USER +"("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_NAME + " TEXT, "
				+ KEY_USERNAME + " TEXT PRIMARY KEY, "
				+ KEY_LOCATION + " TEXT, "
				+ KEY_AMOUNT + " TEXT, "
				+ KEY_EMAIL + " TEXT, "
				+ KEY_LEVEL + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT, "
				+ KEY_CITY_UPDATE_AVAILABLE + " TEXT, "
				+ KEY_THEME_UPDATE_AVAILABLE + " TEXT, "
				+ KEY_RATING_CHANGE +" TEXT, "
				+ KEY_RATING + " TEXT)");

		db.execSQL("CREATE TABLE " + TABLE_SCRATCH_USER +"("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_NAME + " TEXT, "
				+ KEY_USERNAME + " TEXT PRIMARY KEY, "
				+ KEY_LOCATION + " TEXT, "
				+ KEY_EMAIL + " TEXT, "
				+ KEY_AMOUNT + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_RATING + " TEXT)");
		db.execSQL("CREATE TABLE " + TABLE_ADS +"("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_AD_ID + " INTEGER  PRIMARY KEY, "
				+ KEY_PICTURE + " TEXT, "
				+ KEY_COMPANY_USERNAME + " TEXT, "
				+ KEY_DEADLINE + " TEXT, "
				+ KEY_COMPANY_NAME + " TEXT, "
				+ KEY_COMPANY_LOGO + " TEXT, "
				+ KEY_AD_STATUS + " TEXT, "
				//history items
				+ KEY_HISTORY_ID + " TEXT UNIQUE, "
				+ KEY_CUSTORMER_ID + " TEXT, "
				+ KEY_HAS_HISTORY + " TEXT DEFAULT 0, "
				+ KEY_POSTING_DATE + " TEXT, "
				+ KEY_PAYMENT + " TEXT, "
				
				//finish history items
				+ KEY_AD_TEXT + " TEXT, "
				+ KEY_AD_TEXT_HASHTAGS + " TEXT, "
				+ KEY_AD_TEXT_REFERENCES + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_AD_TIMESTAMP + " TEXT)");
		
		db.execSQL("CREATE TABLE " + TABLE_HISTORY +"("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_AD_ID + " INTEGER UNIQUE, "
				+ KEY_PICTURE + " TEXT, "
				+ KEY_COMPANY_USERNAME + " TEXT, "
				+ KEY_DEADLINE + " TEXT, "
				+ KEY_COMPANY_NAME + " TEXT, "
				+ KEY_COMPANY_LOGO + " TEXT, "
				+ KEY_AD_STATUS + " TEXT, "
				//history items
				+ KEY_HISTORY_ID + " TEXT PRIMARY KEY, "
				+ KEY_CUSTORMER_ID + " TEXT, "
				+ KEY_POSTING_DATE + " TEXT, "
				+ KEY_PAYMENT + " TEXT, "
				
				//finish history items
				+ KEY_AD_TEXT + " TEXT, "
				+ KEY_LASTUPDATED + " TEXT DEFAULT CURRENT_TIMESTAMP, "
				+ KEY_AD_TIMESTAMP + " TEXT)");
		
		db.execSQL("CREATE TABLE " + TABLE_THEMES +"("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_ID + " TEXT UNIQUE PRIMARY KEY, "
				+ KEY_THEMES_NAME + " TEXT, "
				+ KEY_THEMES_NAME_RUS + " TEXT, "
				+ KEY_SELECTED + " TEXT DEFAULT 0)");

		db.execSQL("CREATE TABLE " + TABLE_SCRATCH_THEMES +"("
				+ KEY_COLUMNID + " INTEGER, "
				+ KEY_THEMES_NAME + " TEXT PRIMARY KEY, "
				+ KEY_SELECTED + " TEXT)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCRATCH_USER);
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEMES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCRATCH_THEMES);
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADS);

		
		onCreate(db);

	}
	
	public void emptyAllTables(){
		SQLiteDatabase dataBase = this.getWritableDatabase();
		dataBase.execSQL("DELETE FROM " + TABLE_USER);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_USER);

		dataBase.execSQL("DELETE FROM " + TABLE_THEMES);
		dataBase.execSQL("DELETE FROM " + TABLE_SCRATCH_THEMES);
		
		dataBase.execSQL("DELETE FROM " + TABLE_ADS);
	}

}
