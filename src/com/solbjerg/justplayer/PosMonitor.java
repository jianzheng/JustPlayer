package com.solbjerg.justplayer;

import android.media.MediaPlayer;
import android.widget.SeekBar;

public class PosMonitor implements Runnable {
	
	private boolean complete = false;
	private MediaPlayer player;
	private SeekBar bar;

	
	public PosMonitor(MediaPlayer p, SeekBar b) {
		player = p;
		bar = b;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!complete) {
			bar.setProgress(player.getCurrentPosition());
			try {
				Thread.sleep(500);
			} catch(InterruptedException e) {
				
			}
		}
	}
	
	public void kill() {
		complete = true;
	}

}
