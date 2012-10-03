package com.binomed.dont.forget.mom.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.binomed.dont.forget.mom.db.DontForgetMomDbInformation.Trip;

public class DontForgetMomContentProvider extends ContentProvider {

	private DontForgetMomDbHelper dbHelper;

	@Override
	public boolean onCreate() {
		dbHelper = new DontForgetMomDbHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		return DontForgetMomDbHelper.CONTENT_PROVIDER_MIME;
	}

	private long getId(Uri uri) {
		String lastPathSegment = uri.getLastPathSegment();
		if (lastPathSegment != null) {
			try {
				return Long.parseLong(lastPathSegment);
			} catch (NumberFormatException e) {
				Log.e("TutosAndroidProvider", "Number Format Exception : " + e);
			}
		}
		return -1;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			long id = db.insertOrThrow(DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME, null, values);

			if (id == -1) {
				throw new RuntimeException(String.format("%s : Failed to insert [%s] for unknown reasons.", "DontForgetMomProvider", values, uri));
			} else {
				return ContentUris.withAppendedId(uri, id);
			}

		} finally {
			db.close();
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		long id = getId(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		try {
			if (id < 0)
				return db.update(DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME, values, selection, selectionArgs);
			else
				return db.update(DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME, values, Trip.TRIP_ID + "=" + id, null);
		} finally {
			db.close();
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		long id = getId(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			if (id < 0)
				return db.delete(DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME, selection, selectionArgs);
			else
				return db.delete(DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME, Trip.TRIP_ID + "=" + id, selectionArgs);
		} finally {
			db.close();
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		long id = getId(uri);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (id < 0) {
			return db.query(DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
		} else {
			return db.query(DontForgetMomDbHelper.CONTENT_PROVIDER_TABLE_NAME, projection, Trip.TRIP_ID + "=" + id, null, null, null, null);
		}
	}

}
