package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Rupa extends Canvas implements Runnable {

	private static final long serialVersionUID = 6944243095506461954L;
	private static final double KOEFICIENT_SMNJIVANJA_INTERVALA_KORAKA = 0.01;
	private static final long MILISEK_OD_POSLEDNJEG_KORAKA = 2000;
	private Object monitor = new Object();

	private int sirina;
	private int visina;
	private int brojKoraka;
	private int intervalCekanja;
	private volatile boolean slobodna = true;
	private boolean udarenaZivotinja = false;
	private static volatile long intervalRastaZivotinje;
	private Zivotinja zivotinja;
	private Thread threadRupe;

	public int getSirina() {
		return sirina;
	}

	public void setSirina(int sirina) {
		this.sirina = sirina;
	}

	public int getVisina() {
		return visina;
	}

	public void setVisina(int visina) {
		this.visina = visina;
	}

	public int getBrojKoraka() {
		return brojKoraka;
	}

	public void setBrojKoraka(int brojKoraka) {
		this.brojKoraka = brojKoraka;
	}

	public int getIntervalCekanja() {
		return intervalCekanja;
	}

	public void setIntervalCekanja(int intervalKoraka) {
		this.intervalCekanja = intervalKoraka;
	}
	
	public static long getIntervalRastaZivotinje() {
		return intervalRastaZivotinje;
	}

	public static void setIntervalRastaZivotinje(long intervalRastaZivotinje) {
		Rupa.intervalRastaZivotinje = intervalRastaZivotinje;
	}

	public Zivotinja getZivotinja() {
		return zivotinja;
	}

	public void setZivotinja(Zivotinja zivotinja) {
		this.zivotinja = zivotinja;
	}

	public boolean isSlobodna() {
		return slobodna;
	}

	public void setSlobodna(boolean slobodna) {
		this.slobodna = slobodna;
	}

	public boolean isZivotinjaUdarena() {
		return udarenaZivotinja;
	}

	public void setUdarenaZivotinja(boolean udarenaZivotinja) {
		this.udarenaZivotinja = udarenaZivotinja;
	}

	public Rupa(Zivotinja zivotinja) {
		super();
		this.zivotinja = zivotinja;
		setBackground(new Color(139, 69, 19));
		setUdaracEvent();
	}

	public synchronized Thread crtajKrticu() {
		threadRupe = new Thread(this);
		threadRupe.start();

		return threadRupe;
	}

	public synchronized void zaustaviKrticu() {
		slobodna = true;
		if(Igra.running && !udarenaZivotinja) {
			Basta.smanjiPovrce(1);
			zivotinja.zvukPobegulje();
		}
		zivotinja = null;
		udarenaZivotinja = false;
		threadRupe.interrupt();
	}

	@Override
	public void paint(Graphics g) {
		//This runs once, only when Rupa is instantiated
	}

	public void mojPaint(Graphics g, int sirinaZivotinje, int sirina) {
		if (zivotinja != null) {
			zivotinja.crtaj(g, sirinaZivotinje, sirina);
		}
	}

	@Override
	public void run() {
		runRupa();
	}

	private synchronized void setUdaracEvent() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent evt) {
				if (evt.getSource() instanceof Canvas) {
					Rupa rupa = (Rupa) evt.getComponent();
					if (evt.getID() == MouseEvent.MOUSE_PRESSED && rupa.getZivotinja() != null) {
						synchronized(monitor) {
							rupa.setUdarenaZivotinja(true);
							monitor.notifyAll();
						}
					}
				}
			}
		});
	}

	private synchronized void runRupa() {
		long intervalRasta = getUmanjeniIntervalCekanja();
		for (int i = 1;Igra.running && i <= brojKoraka; i++) {
			try {
				Thread.sleep(intervalRasta != 0 ? intervalRasta : 1);
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
			mojPaint(getGraphics(), (int)(sirina * ((double)i/brojKoraka)), sirina);
			if(udarenaZivotinja) {
				break;
			}
		}
		if(Igra.running) {
			spavajICekajUdarac();
			if(Igra.running) {
				getGraphics().clearRect(0, 0, sirina, visina);
			}
		}
		zaustaviKrticu();
	}

	private void spavajICekajUdarac() {
		if (!udarenaZivotinja) {
			synchronized (monitor) {
				try {
					monitor.wait(MILISEK_OD_POSLEDNJEG_KORAKA);
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}

	private static long getUmanjeniIntervalCekanja() {
		intervalRastaZivotinje = (long)(intervalRastaZivotinje - (intervalRastaZivotinje * KOEFICIENT_SMNJIVANJA_INTERVALA_KORAKA));
		return intervalRastaZivotinje;
	}
}
