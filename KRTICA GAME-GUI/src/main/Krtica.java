package main;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Krtica extends Zivotinja {
	
	@Override
	void crtaj(Graphics g, int sirinaZivotinje, int sirinaRupe) {
		g.setColor(Color.DARK_GRAY);
		g.fillOval((sirinaRupe - sirinaZivotinje) / 2, (sirinaRupe - sirinaZivotinje) / 2, sirinaZivotinje,
				sirinaZivotinje);
	}
	
	@Override
	void zvukPobegulje() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("utekla.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			System.err.println("Sound file not found");
			e.printStackTrace();
		}
	}
}
