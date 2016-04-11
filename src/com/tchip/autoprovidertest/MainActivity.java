package com.tchip.autoprovidertest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText etGetName, etSetName, etSetValue;
	private Button btnGet, btnInsert, btnUpdate;
	private TextView textGet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initialLayout();

		getContentResolver()
				.registerContentObserver(
						Uri.parse("content://com.tchip.provider.AutoProvider/state/name/"),
						true, new AutoContentObserver(new Handler()));
	}

	private void initialLayout() {
		etGetName = (EditText) findViewById(R.id.etGetName);
		etSetName = (EditText) findViewById(R.id.etSetName);
		etSetValue = (EditText) findViewById(R.id.etSetValue);
		btnGet = (Button) findViewById(R.id.btnGet);
		btnInsert = (Button) findViewById(R.id.btnInsert);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		textGet = (TextView) findViewById(R.id.textGet);

		MyOnClickListener myOnClickListener = new MyOnClickListener();
		btnGet.setOnClickListener(myOnClickListener);
		btnInsert.setOnClickListener(myOnClickListener);
		btnUpdate.setOnClickListener(myOnClickListener);
	}

	class MyOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnGet: {
				String dbValue = "";
				Uri uri = Uri
						.parse("content://com.tchip.provider.AutoProvider/state/name/"
								+ etGetName.getText().toString());
				ContentResolver contentResolver = getContentResolver();
				Cursor cursor = contentResolver.query(uri, null, null, null,
						null);
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					dbValue = cursor.getString(cursor.getColumnIndex("value"));
					cursor.close();
				} else {
				}
				textGet.setText("dbValue:" + dbValue);
				Toast.makeText(MainActivity.this, "dbValue:" + dbValue,
						Toast.LENGTH_SHORT).show();
			}
				break;

			case R.id.btnInsert: {
				Uri uri = Uri
						.parse("content://com.tchip.provider.AutoProvider/state/");
				ContentResolver contentResolver = getContentResolver();
				ContentValues values = new ContentValues();
				values.put("name", etSetName.getText().toString());
				values.put("value", etSetValue.getText().toString());
				contentResolver.insert(uri, values);
			}
				break;

			case R.id.btnUpdate: {
				Uri uri = Uri
						.parse("content://com.tchip.provider.AutoProvider/state/name/"
								+ etSetName.getText().toString());
				ContentResolver contentResolver = getContentResolver();
				ContentValues values = new ContentValues();
				values.put("value", etSetValue.getText().toString());
				int count = contentResolver.update(uri, values, null, null);
				Log.v("AutoProvider", "Update count:" + count);
			}
				break;

			default:
				break;
			}
		}
	}

	public class AutoContentObserver extends ContentObserver {

		public AutoContentObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange, Uri uri) {
			String name = uri.getLastPathSegment();// getPathSegments().get(2);
			if (name.equals("state")) { // insert

			} else { // update
				Toast.makeText(MainActivity.this,
						"onChange,selfChange:" + selfChange + ",Name:" + name,
						Toast.LENGTH_SHORT).show();
			}
			super.onChange(selfChange, uri);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
		}

	}
}
