package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Basta extends Panel implements Runnable {

	private static final long serialVersionUID = 5227585483873354012L;
	public static final int GAP = 10;
	private static int kolicinaPovrca = 100;
	private int brojKoraka = 10;
	private int intervalCekanja = 1000;
	private int ukupnanBrojKolonaMatrice;
	private Random random = new Random();
	private Thread mainThread;

	public static int getKolicinaPovrca() {
		return kolicinaPovrca;
	}

	public static void setKolicinaPovrca(int kolicinaPovrca) {
		Basta.kolicinaPovrca = kolicinaPovrca;
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

	public void setIntervalCekanja(int intervalCekanja) {
		this.intervalCekanja = intervalCekanja;
	}

	public Basta(int brojKolonaMatrice, int velicinaBaste) {
		this.setBackground(Color.GREEN);
		this.setPreferredSize(new Dimension(velicinaBaste, velicinaBaste));
		setLayout(getGridLayout(brojKolonaMatrice));
		this.ukupnanBrojKolonaMatrice = brojKolonaMatrice * brojKolonaMatrice;

		for (int i = 0; i < ukupnanBrojKolonaMatrice; i++) {
			Rupa rupa = new Rupa(null);
			rupa.setVisina((velicinaBaste - (brojKolonaMatrice - 1) * GAP) / brojKolonaMatrice);
			rupa.setSirina(rupa.getVisina());
			this.add(rupa);
		}
	}

	@Override
	public void run() {
		refreshBastu();
		try {
			// POCETNA PREDNOST
			Thread.sleep(200);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
		matricaRupa();
	}

	public Thread startujBastu() {
		mainThread = new Thread(this);
		mainThread.start();
		return mainThread;
	}

	public void stopirajBastu() {
		mainThread.interrupt();
		Igra.getStartButton().setLabel("Kreni");
		Igra.getStartButton().setEnabled(false);
	}

	private synchronized void matricaRupa() {
		Component[] comps = this.getComponents();
		List<Rupa> rupe = new LinkedList<>();
		Arrays.asList(comps).forEach(cmp -> {
			Rupa trenutnaRupa = (Rupa) cmp;
			trenutnaRupa.setIntervalCekanja(intervalCekanja);
			trenutnaRupa.setBrojKoraka(brojKoraka);
			Rupa.setIntervalRastaZivotinje(intervalCekanja / brojKoraka);
			rupe.add(trenutnaRupa);
		});

		while (kolicinaPovrca > 0 && Igra.running) {
			int index = random.nextInt(rupe.size());

			Rupa rupa = rupe.get(index);

			if (rupa.isSlobodna()) {
				rupa.setSlobodna(false);
				rupa.setZivotinja(new Krtica());
				Thread rupinThread = rupa.crtajKrticu();

				try {
					rupinThread.join();
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		stopirajBastu();
	}

	private static GridLayout getGridLayout(int brojKolonaMatrice) {
		GridLayout gridLayout = new GridLayout(brojKolonaMatrice, brojKolonaMatrice);
		gridLayout.setHgap(GAP);
		gridLayout.setVgap(GAP);
		return gridLayout;
	}

	public static void smanjiPovrce(int broj) {
		if (kolicinaPovrca > 0) {
			kolicinaPovrca -= broj;
		}
		Igra.getLabelKolicinaPovrca().setText("Povrce: " + kolicinaPovrca);
	}

	private void refreshBastu() {
		revalidate();
		repaint();
	}
}
