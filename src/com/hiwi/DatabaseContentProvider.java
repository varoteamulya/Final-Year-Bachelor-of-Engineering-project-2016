package com.hiwi;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.lifesense.ble.bean.LsDeviceInfo;

//Content Provider�??供了一�?基于content://模�?的简�?�URI寻�?�模型，
//�?��?�布和使用数�?�库的接�?�
public class DatabaseContentProvider extends ContentProvider {

	// 为CustomContentProvider�?�布一个�?�访问的URI，
	// 供其他应用程�?组件通过ContentResolver，�?�访问CustomContentProvider
	public static final Uri CONTENT_URI = Uri
			.parse("content://lifesense.ble.a3.dataBase/deviceitems");

	// 添加MySQliteOpenHelper的引用�?��?
	private MySQLiteOpenHelper myOpenHelper;

	// 创建数�?�库列�??的公有�?��?�?��?
	public static final String KEY_ID = "_id";
	public static final String KEY_DEVICE_NAME = "device_name";
	public static final String KEY_DEVICE_ADDRESS = "device_address";
	public static final String KEY_DEVICE_TYPE = "device_type";
	public static final String KEY_DEVICE_ID = "device_id";
	public static final String KEY_DEVICE_SN = "device_sn";
	public static final String KEY_DEVICE_PAIRFLAGS = "device_pairflags";
	public static final String KEY_DEVICE_MODELNUMBER = "device_modelnumber";
	public static final String KEY_DEVICE_PASSWORD = "device_password";
	public static final String KEY_DEVICE_BROCASTID = "device_brocastID";
	public static final String KEY_DEVICE_SOFTWARE_VERSION = "software_version";
	public static final String KEY_DEVICE_HARDWARE_VERSION = "hardware_version";
	public static final String KEY_DEVICE_FIRMWARE_VERSION = "firmware_version";
	public static final String KEY_DEVICE_MANUFACTURENAME = "manufacture_name";
	public static final String KEY_DEVICE_SYSTEMID = "device_systemID";
	public static final String KEY_DEVICE_MODEL = "device_model";
	public static final String KEY_DEVICE_USER_NUMBER = "user_number";
	public static final String KEY_DEVICE_USER_NAME = "user_name";
	public static final String KEY_DEVICE_SERVICE_UUID = "service_uuid";
	public static final String KEY_DEVICE_STATUS = "device_status";
	public static final String KEY_MAX_USER_QUANTITY = "max_user_number";
	public static final String KEY_PROTOCOL_TYPE = "protocol_type";

	// 创建一个新的UriMatcher,定义全表与特定行查询
	private static final int ALLROWS = 1;
	private static final int SINGLE_ROW = 2;
	private static final UriMatcher uriMatcher;

	// 填充UriMatcher对象，并设置�?�?�的查询请求Uri
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("lifesense.ble.a3.dataBase", "deviceitems", ALLROWS);
		uriMatcher.addURI("lifesense.ble.a3.dataBase", "deviceitems/#",
				SINGLE_ROW);
	}

	// 构造底层数�?�库，延迟打开数�?�库，直到需�?执行一个查询或事务时�?打开
	@Override
	public boolean onCreate() {
		myOpenHelper = new MySQLiteOpenHelper(getContext(),
				MySQLiteOpenHelper.DATABASE_NAME, null,
				MySQLiteOpenHelper.DATABASE_VERSION);
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// 为一个Content Provider URI返回一个字符串，它标识了MIME类型
		switch (uriMatcher.match(uri)) {
		case ALLROWS:
			return "vnd.android.cursor.dir/vnd.paad.users";
		case SINGLE_ROW:
			return "vnd.android.cursor.item/vnd.paad.users";
		default:
			throw new IllegalArgumentException("Unsupported URI:" + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Open a read / write database to support the transaction.
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();

		// If this is a row URI, limit the deletion to the specified row.
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = KEY_ID
					+ "="
					+ rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		default:
			break;
		}

		// To return the number of deleted items you must specify a where
		// clause. To delete all rows and return a value pass in "1".
		if (selection == null)
			selection = "1";

		// Perform the deletion.
		int deleteCount = db.delete(MySQLiteOpenHelper.DATABASE_TABLE,
				selection, selectionArgs);
		Log.v("是�?�已执行删除", selection);

		// Notify any observers of the change in the data set.
		getContext().getContentResolver().notifyChange(uri, null);

		// Return the number of deleted items.
		return deleteCount;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Open a read / write database to support the transaction.
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();

		// To add empty rows to your database by passing in an empty
		// Content Values object you must use the null column hack
		// parameter to specify the name of the column that can be
		// set to null.
		String nullColumnHack = null;

		// Insert the values into the table
		long id = db.insert(MySQLiteOpenHelper.DATABASE_TABLE, nullColumnHack,
				values);

		// Construct and return the URI of the newly inserted row.
		if (id > -1) {
			// Construct and return the URI of the newly inserted row.
			Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);

			// Notify any observers of the change in the data set.
			getContext().getContentResolver().notifyChange(insertedId, null);

			return insertedId;
		} else
			return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		// Open a read / write database to support the transaction.
		SQLiteDatabase db = myOpenHelper.getWritableDatabase();

		// If this is a row URI, limit the deletion to the specified row.
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			selection = KEY_ID
					+ "="
					+ rowID
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : "");
		default:
			break;
		}

		// Perform the update.
		int updateCount = db.update(MySQLiteOpenHelper.DATABASE_TABLE, values,
				selection, selectionArgs);

		// Notify any observers of the change in the data set.
		getContext().getContentResolver().notifyChange(uri, null);

		return updateCount;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Open the database.
		SQLiteDatabase db;
		try {
			db = myOpenHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = myOpenHelper.getReadableDatabase();
		}

		// Replace these with valid SQL statements if necessary.
		String groupBy = null;
		String having = null;

		// Use an SQLite Query Builder to simplify constructing the
		// database query.
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// If this is a row query, limit the result set to the passed in row.
		switch (uriMatcher.match(uri)) {
		case SINGLE_ROW:
			String rowID = uri.getPathSegments().get(1);
			queryBuilder.appendWhere(KEY_ID + "=" + rowID);
		default:
			break;
		}

		// Specify the table on which to perform the query. This can
		// be a specific table or a join as required.
		queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);

		// Execute the query.
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, having, sortOrder);

		// Return the result Cursor.
		return cursor;
	}

	// 获�?�数�?�库所有的内容，并以对象数组的形�?返回
	// Getting All Contacts
	public List<LsDeviceInfo> getAllContacts() {
		List<LsDeviceInfo> userList = new ArrayList<LsDeviceInfo>();

		// Select All Query
		String selectQuery = "SELECT  * FROM "
				+ MySQLiteOpenHelper.DATABASE_TABLE;

		SQLiteDatabase db = myOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				LsDeviceInfo sensor = new LsDeviceInfo();
				sensor.setDeviceName(cursor.getString(0));
				// sensor.setDeviceAddress(cursor.getString(1));
				// sensor.setDeviceType(cursor.getInt(2));
				sensor.setDeviceId(cursor.getString(3));
				sensor.setDeviceSn(cursor.getString(4));
				// sensor.setPairFlags(cursor.getString(5));
				sensor.setModelNumber(cursor.getString(6));
				sensor.setPassword(cursor.getString(7));
				sensor.setBroadcastID(cursor.getString(8));
				sensor.setSoftwareVersion(cursor.getString(9));
				sensor.setHardwareVersion(cursor.getString(10));
				sensor.setFirmwareVersion(cursor.getString(11));
				sensor.setManufactureName(cursor.getString(12));
				sensor.setSystemId(cursor.getString(13));

				// Adding contact to list
				userList.add(sensor);
			} while (cursor.moveToNext());
		}

		// return contact list
		return userList;
	}

	// 自定义一个�?有的SQLiteOpenHelper，SQLiteOpenHelper是一个抽象类，用�?�创建�?打开和�?�级数�?�库的最佳实践模�?
	private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
		// 定义数�?�库的�??称�?数�?�库版本�?数�?�表�??称
		private static final String DATABASE_NAME = "BleDevice.db";
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_TABLE = "deviceTable";

		private static final String DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + " (" + KEY_ID
				+ " integer primary key autoincrement, " + KEY_DEVICE_NAME
				+ " text , " + KEY_DEVICE_ADDRESS + " text, " + KEY_DEVICE_TYPE
				+ " text, " + KEY_DEVICE_ID + " text , " + KEY_DEVICE_SN
				+ " text , " + KEY_DEVICE_PAIRFLAGS + " text , "
				+ KEY_DEVICE_MODELNUMBER + " text , " + KEY_DEVICE_PASSWORD
				+ " text , " + KEY_DEVICE_BROCASTID + " text , "
				+ KEY_DEVICE_SOFTWARE_VERSION + " text , "
				+ KEY_DEVICE_HARDWARE_VERSION + " text , "
				+ KEY_DEVICE_FIRMWARE_VERSION + " text , "
				+ KEY_DEVICE_MANUFACTURENAME + " text , " + KEY_DEVICE_SYSTEMID
				+ " text , " +

				KEY_DEVICE_MODEL + " text , " + KEY_DEVICE_USER_NUMBER
				+ " text , " + KEY_DEVICE_USER_NAME + " text , "
				+ KEY_DEVICE_SERVICE_UUID + " text , " + KEY_PROTOCOL_TYPE
				+ " text , " + KEY_MAX_USER_QUANTITY + " text , "
				+ KEY_DEVICE_STATUS + " text);";

		public MySQLiteOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);

		}

		// 当在�?盘中没有数�?�库的时候，调用该方法，创建一个新的数�?�库
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);

		}

		// 当数�?�库版本�?匹�?时，调用该方法，自动更新数�?�库版本
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS" + DATABASE_CREATE);// 删除原版本的表
			onCreate(db);// 创建新版本的表

		}
	}

}
