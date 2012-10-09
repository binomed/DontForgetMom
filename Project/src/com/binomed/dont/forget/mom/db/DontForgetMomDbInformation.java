package com.binomed.dont.forget.mom.db;

import android.provider.BaseColumns;

public class DontForgetMomDbInformation {

	public DontForgetMomDbInformation() {
	}

	public static final class Trip implements BaseColumns {

		private Trip() {
		}

		public static final String TRIP_ID = "TRIP";
		public static final String TRIP_NAME = "TRIP_NAME";
		public static final String TRIP_PLACE = "TRIP_PLACE";
		public static final String TRIP_DAY = "TRIP_DAY";
		public static final String TRIP_HOUR = "TRIP_HOUR";
		public static final String TRIP_PRECISION = "TRIP_PRECISION";
		public static final String TRIP_TYPE_ALERT = "TRIP_TYPE_ALERT";
		public static final String TRIP_RECIPIENT = "TRIP_RECIPIENT";
		public static final String TRIP_MESSAGE = "TRIP_MESSAGE";
		public static final String TRIP_LAST_LAUNCH = "TRIP_LAST_LAUNCH";

	}

}