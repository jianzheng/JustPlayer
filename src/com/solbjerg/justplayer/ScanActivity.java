package com.solbjerg.justplayer;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ScanActivity extends Activity {
	
	private ImageView up;
	private ListView dirList;
	private TextView txtCurrentDir;
	private File currentDir;
	private JustPlayerApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		init();
		initListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}
	
	private void init() {
		up = (ImageView) this.findViewById(R.id.top);
		dirList = (ListView) this.findViewById(R.id.dirList);
		txtCurrentDir = (TextView) this.findViewById(R.id.currentDir);
		app = (JustPlayerApplication) this.getApplication();
		File song = new File(app.getCurrentSong());
		if(song.exists()) {
			currentDir = song.getParentFile();
		} else {
			currentDir = Environment.getExternalStorageDirectory();
		}
		setCurrentDir(currentDir);
	}
	
	private void initListeners()
	{
		dirList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent,
											View view,
											int position,
											long id)
			{
				File selectedFile = (File) parent.getItemAtPosition(position);
				if (selectedFile.isFile() && selectedFile.canRead() && selectedFile.getName().endsWith(".mp3")) {
					app.setCurrentSong(selectedFile.getAbsolutePath());
					Intent intent = getIntent();
					intent.putExtra(app.CURRENT_SONG, selectedFile.getAbsolutePath());
					setResult(RESULT_OK, intent);
					finish();
				} else if(selectedFile.isDirectory()) {
					setCurrentDir(selectedFile);
				}
			}
		});

		up.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				setCurrentDir(currentDir.getParentFile());
			}
		});
	}
	
	private void setCurrentDir(File dir) {
		currentDir = dir;
		txtCurrentDir.setText(dir.getAbsolutePath());
		setAdapterForDir(dir);
		toggleUpButton(dir);
	}
	
	private void toggleUpButton(File file) {
		up.setEnabled(file.getParentFile() != null);
	}
	
	private void setAdapterForDir(File file) {
		File[] files = file.listFiles();
		if (files == null)
		{
			files = new File[0];
		}
		ArrayAdapter<File> adapter = new FileAdapter(this, files);
		dirList.setAdapter(adapter);
	}

}
