package com.solbjerg.justplayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayActivity extends Activity {
	
	private String currentSong;
	private ArrayList<File> songList;
	private ImageView play;
	private ImageView next;
	private ImageView last;
	private TextView  songName;
	private MediaPlayer mediaPlayer;
	private static final String TAG = "PlayActivity";
	private JustPlayerApplication app;
	private ImageView posMinus;
	private ImageView posPlus;
	private ImageView speedMinus;
	private ImageView speedPlus;
	private SeekBar posBar;
	private SeekBar speedBar;
	private int currentSpeed;
	private int currentPos;
	private PosMonitor posMonitor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		init();
		initListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
	
	private void init() {
		play = (ImageView) this.findViewById(R.id.play);
		next = (ImageView) this.findViewById(R.id.next);
		last = (ImageView) this.findViewById(R.id.last);
		songName = (TextView) this.findViewById(R.id.currentSongName);
		app = (JustPlayerApplication) this.getApplication();
		mediaPlayer = new MediaPlayer();
		speedPlus = (ImageView) this.findViewById(R.id.speedPlus);
		speedMinus = (ImageView) this.findViewById(R.id.speedMinus);
		posPlus = (ImageView) this.findViewById(R.id.posPlus);
		posMinus = (ImageView) this.findViewById(R.id.posMinus);
		posBar = (SeekBar) this.findViewById(R.id.posBar);
		currentSong = app.getCurrentSong();
		songList = new ArrayList<File>();
		initialBySong(currentSong, true);
	}
	
	/**
	 * Initial the related information and the media player by the song played last time.
	 * @param path path of the song player last time
	 * @param initialFileList update the array of the same directory's songs
	 */
	private void initialBySong(String path, boolean initialFileList) {
		File file = new File(path);
		if(file.exists() && file.canRead() && file.isFile()) {
			if(initialFileList) {
				songList.clear();
				File dir = file.getParentFile();
				File[] files = dir.listFiles();
				Arrays.sort(files);
				for(File tmp:files) {
					if(tmp.getName().endsWith(".mp3")) {
						songList.add(tmp);
					}
				}
			}
			songName.setText(file.getName());
			if(mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
			    mediaPlayer.setDataSource("file://" + path);
			    mediaPlayer.prepare();
			} catch(IOException e) {
				Log.e(TAG, "mediaplayer error", e);
			}
			currentSong = path;
			app.setCurrentSong(path);
			if(posMonitor != null) {
				posMonitor.kill();
			}
			posBar.setProgress(0);
		}
		toggleButton();
	}
	
	/**
	 * Start the media player and update related UI
	 */
	private void play() {
		mediaPlayer.start();
		play.setImageResource(R.drawable.ic_btn_pause);
		resetProgressBar();
	}
	
	private void pause() {
		mediaPlayer.pause();
		play.setImageResource(R.drawable.ic_btn_play);
	}
	
	private void initListener() {
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mediaPlayer.isPlaying()) {
					pause();
				} else {
					play();
				}
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer m) {
				play.setImageResource(R.drawable.ic_btn_play);
				mediaPlayer.seekTo(0);
			}
		});
		next.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				boolean playing = mediaPlayer.isPlaying();
				if(playing) {
					next();
					play();
				} else {
					next();
				}
			}
		});
		last.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				boolean playing = mediaPlayer.isPlaying();
				if(playing) {
					last();
					play();
				} else {
					last();
				}
			}
		});
		speedPlus.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				speedUp();
			}
		});
		speedMinus.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				speedDown();
			}
		});
		posPlus.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				posUp();
			}
		});
		posMinus.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				posDown();
			}
		});
		posBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar s, int i, boolean b) {
				if(mediaPlayer != null && b) {
					mediaPlayer.seekTo(i);
				}
			}
			public void onStartTrackingTouch(SeekBar s) {
				
			}
			public void onStopTrackingTouch(SeekBar s) {
				
			}
		});
	}
	
	private void speedUp() {
		
	}
	
	private void speedDown() {
		
	}
	
	private void posUp() {
		int posNow = mediaPlayer.getCurrentPosition();
		int posUnit = PreferenceManager.getDefaultSharedPreferences(this).getInt("posUnit", 5000);
		int posEfter = posNow + posUnit;
		if(posEfter > mediaPlayer.getDuration()) {
			posEfter = mediaPlayer.getDuration();
		}
		posBar.setProgress(posEfter);
		mediaPlayer.seekTo(posEfter);
	}
	
	private void posDown() {
		int posNow = mediaPlayer.getCurrentPosition();
		int posUnit = PreferenceManager.getDefaultSharedPreferences(this).getInt("posUnit", 5000);
		int posEfter = posNow - posUnit;
		if(posEfter < 0) {
			posEfter = 0;
		}
		posBar.setProgress(posEfter);
		mediaPlayer.seekTo(posEfter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(mediaPlayer != null) {
			this.currentPos = mediaPlayer.getCurrentPosition();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(posMonitor != null) {
			posMonitor.kill();
		}
		mediaPlayer.stop();
		mediaPlayer.release();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_settings:
				this.startActivity(new Intent(this, PrefsActivity.class));
				break;
			case R.id.action_scan:
				this.startActivityForResult(new Intent(this, ScanActivity.class), 1);
				break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if(resultCode == RESULT_OK) {      
				   String path = data.getExtras().getString(JustPlayerApplication.CURRENT_SONG);
				   if(!currentSong.equalsIgnoreCase(path)) {
					   initialBySong(path, true);
					   play();
				   }
			} if (resultCode == RESULT_CANCELED) {    
			}
		}
	}
	
	private void next() {
		for(int i = 0;i < songList.size();i++) {
			File tmp = (File) songList.get(i);
			if(currentSong.equalsIgnoreCase(tmp.getAbsolutePath()) && i < songList.size() - 1) {
				File tmp2 = (File) songList.get(i + 1);
				initialBySong(tmp2.getAbsolutePath(), false);
				return;
			}
		}
	}
	
	private void last() {
		for(int i = 0;i < songList.size();i++) {
			File tmp = (File) songList.get(i);
			if(currentSong.equalsIgnoreCase(tmp.getAbsolutePath()) && i > 0) {
				File tmp2 = (File) songList.get(i - 1);
				initialBySong(tmp2.getAbsolutePath(), false);
				return;
			}
		}
	}
	
	private void toggleButton() {
		File file = new File(currentSong);
		if(!file.exists() || !file.canRead() || file.isDirectory()) {
			play.setEnabled(false);
			last.setEnabled(false);
			next.setEnabled(false);
		} else {
			play.setEnabled(true);
			File firstFile = (File) songList.get(0);
			File lastFile = (File) songList.get(songList.size() - 1);
			if(currentSong.equalsIgnoreCase(firstFile.getAbsolutePath())) {
				last.setEnabled(false);
			} else {
				last.setEnabled(true);
			}
			if(currentSong.equalsIgnoreCase(lastFile.getAbsolutePath())) {
				next.setEnabled(false);
			} else {
				next.setEnabled(true);
			}
		}
	}
	
	private void resetProgressBar() {
		posBar.setMax(mediaPlayer.getDuration());
		posBar.setProgress(0);
		if(posMonitor != null) {
			posMonitor.kill();
		}
		posMonitor = new PosMonitor(mediaPlayer, posBar);
		new Thread(posMonitor).start();
	}
		
}
