package main;

import java.awt.Graphics;

public abstract class Zivotinja {

	private boolean pobegla;

	public boolean isPobegla() {
		return pobegla;
	}

	public void setPobegla(boolean pobegla) {
		this.pobegla = pobegla;
	}

	abstract void crtaj(Graphics g, int sirinaZivotinje, int sirinaRupe);

	abstract void zvukPobegulje();
}
