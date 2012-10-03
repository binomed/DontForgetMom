package com.binomed.dont.forget.mom.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;

public class DontForgetMomDbHelper extends SQLiteOpenHelper {

	// URI de notre content provider, elle sera utilis� pour acc�der au ContentProvider
	public static final Uri CONTENT_URI = Uri.parse("content://com.bibomed.dont.forget.mom.db.dontforgetmomprovider");

	// Nom de notre base de donn�es
	public static final String CONTENT_PROVIDER_DB_NAME = "dontforgetmom.db";
	// Version de notre base de donn�es
	public static final int CONTENT_PROVIDER_DB_VERSION = 1;
	// Nom de la table de notre base
	public static final String CONTENT_PROVIDER_TABLE_NAME = "trip";
	// Le Mime de notre content provider, la premi�re partie est toujours identique
	public static final String CONTENT_PROVIDER_MIME = "vnd.bibomed.dont.forget.mom.db.trip";

	public DontForgetMomDbHelper(Context context) {
		super(context, DontForgetMomDbHelper.CONTENT_PROVIDER_DB_NAME, null, DontForgetMomDbHelper.CONTENT_PROVIDER_DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME + " ("//
				+ Trip.TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //
				+ Trip.TRIP_NAME + " VARCHAR(255)," //
				+ Trip.TRIP_PLACE + " VARCHAR(255)," //
				+ Trip.TRIP_DAY + " LONG," //
				+ Trip.TRIP_HOUR + " LONG," //
				+ Trip.TRIP_PRECISION + " INTEGER," //
				+ Trip.TRIP_TYPE_ALERT + " INTEGER," //
				+ Trip.TRIP_RECIPIENT + " VARCHAR(255)," //
				+ Trip.TRIP_MESSAGE + " TEXT," //
				+ Trip.TRIP_LAST_LAUNCH + " LONG" //
				+ ");");
	}

	// Cette m�thode sert � g�rer la mont�e de version de notre base
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME);
		onCreate(db);

	}

}
