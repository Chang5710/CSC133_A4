package com.mycompany.a4.game.sound;

import java.io.InputStream;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

public class BGSound implements Runnable {

	private Media m;
	
	public BGSound(String fileName) {
		try {
			InputStream in = Display.getInstance().getResourceAsStream(getClass(), "/" + fileName);
			m = MediaManager.createMedia(in, "audio/wav" , this);
		}catch (Exception e) {
			System.out.println("Error playing sound");
			e.printStackTrace();
		}
	}
	
	public void play() {
		m.setVolume(50);
		m.play();	
	}
	
	public void pause() {
		m.pause();
	}
		
	@Override
	public void run() {
		// TODO Auto-generated method stub
		m.setTime(0);
		m.play();
	}

}
