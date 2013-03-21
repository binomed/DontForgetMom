package com.binomed.dont.forget.mom.db;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DontForgetMomContentProvider extends ContentProvider {

	private DontForgetMomDbHelper dbHelper;

	// Used for the UriMacher
	private static final int TRIPS = 10;
	private static final int TRIP_ID = 20;

	private static final String AUTHORITY = "com.bibomed.dont.forget.mom.db";
	private static final String CONTENT_PROVIDER_MIME = "vnd.com.bibomed.dont.forget.mom.db";
	private static final String BASE_PATH = "dontforgetmomprovider";

	// URI de notre content provider, elle sera utilis� pour acc�der au ContentProvider
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/trips";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/trip";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, TRIPS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TRIP_ID);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DontForgetMomDbHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		return CONTENT_PROVIDER_MIME;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		long id = 0l;
		switch (uriType) {
		case TRIPS:
			id = sqlDB.insert(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case TRIPS:
			rowsUpdated = sqlDB.update(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME, values, selection, selectionArgs);
			break;
		case TRIP_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME, //
						values,//
						DontForgetMomDbInformation.Trip._ID + "=" + id,//
						null);
			} else {
				rowsUpdated = sqlDB.update(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME, //
						values, //
						DontForgetMomDbInformation.Trip._ID + "=" + id //
								+ " and " //
								+ selection, //
						selectionArgs);
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
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case TRIPS:
			rowsDeleted = sqlDB.delete(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME, //
					selection, //
					selectionArgs //
					);
			break;
		case TRIP_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME, //
						DontForgetMomDbInformation.Trip._ID + "=" + id, //
						null);
			} else {
				rowsDeleted = sqlDB.delete(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME, //
						DontForgetMomDbInformation.Trip._ID + "=" + id + " and " + selection, //
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(DontForgetMomDbInformation.Trip.CONTENT_PROVIDER_TABLE_NAME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TRIPS:
			break;
		case TRIP_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(DontForgetMomDbInformation.Trip._ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	private void checkColumns(String[] projection) {
		String[] available = { DontForgetMomDbInformation.Trip._ID,//
				DontForgetMomDbInformation.Trip.TRIP_DAY,//
				DontForgetMomDbInformation.Trip.TRIP_LAST_LAUNCH,//
				DontForgetMomDbInformation.Trip.TRIP_MESSAGE,//
				DontForgetMomDbInformation.Trip.TRIP_NAME,//
				DontForgetMomDbInformation.Trip.TRIP_PLACE,//
				DontForgetMomDbInformation.Trip.TRIP_PRECISION,//
				DontForgetMomDbInformation.Trip.TRIP_RECIPIENT,//
				DontForgetMomDbInformation.Trip.TRIP_TYPE_ALERT,//
				DontForgetMomDbInformation.Trip.TRIP_IN_PROGRESS //
		};
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

}
