package main;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;

public class Igra {
	private static final int STRANICA_KOCKE = 600;
	private static final int BROJ_REDOVA = 4;
	private Frame mainFrame;
	private Basta basta;
	private Panel controlPanel;
	public static boolean running = false;
	private static Button startButton = new Button("Kreni");
	private Checkbox lakoRB;
	private Checkbox srednjeRB;
	private Checkbox teskoRB;
	private Label labelTezina = new Label();
	private static Label labelKolicinaPovrca = new Label();

	public Igra() {
		this.basta = new Basta(BROJ_REDOVA, STRANICA_KOCKE);
		prepareGUI();
	}
	
	public static Label getLabelKolicinaPovrca() {
		return labelKolicinaPovrca;
	}

	public static void setLabelKolicinaPovrca(Label labelKolicinaPovrca) {
		Igra.labelKolicinaPovrca = labelKolicinaPovrca;
	}

	public static void main(String[] args) {
		new Igra();
	}

	void krajIgre() {
		running = false;
		startButton.setLabel("Kreni");
		basta.stopirajBastu();
		startButton.setEnabled(false);
	}
	
	public static Button getStartButton() {
		return startButton;
	}

	public static void setStartButton(Button startButton) {
		Igra.startButton = startButton;
	}
	
	private void kreni() {
		running = !running;
		if (running) {
			startButton.setLabel("Stani");
			basta.startujBastu();
		} else {
			krajIgre();
		}
	}

	private void prepareGUI() {
		prepareMainFrame();
		controlPanel = getControlPanel();

		labelTezina.setText("Tezina:");
		controlPanel.add(labelTezina);

		createTezinaRadioButtons();

		startButton.addActionListener(dugme -> {
			lakoRB.setEnabled(false);
			srednjeRB.setEnabled(false);
			teskoRB.setEnabled(false);
			kreni();
		});

		labelKolicinaPovrca.setText("Povrce: " + Basta.getKolicinaPovrca());

		controlPanel.add(startButton);
		controlPanel.add(labelKolicinaPovrca);

		mainFrame.add(basta);
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
	}

	private Panel getControlPanel() {
		controlPanel = new Panel();
		controlPanel.setBackground(Color.GRAY);
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setPreferredSize(new Dimension(200, 600));

		return controlPanel;
	}

	private void prepareMainFrame() {
		mainFrame = new Frame("Java AWT Examples");
		mainFrame.setSize(800, STRANICA_KOCKE + Basta.GAP * (BROJ_REDOVA - 1));

		mainFrame.setLayout(new BoxLayout(mainFrame, BoxLayout.X_AXIS));
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}

	private void createTezinaRadioButtons() {
		CheckboxGroup radioBtnGroup = new CheckboxGroup();
		lakoRB = new Checkbox("Lako", radioBtnGroup, true);
		srednjeRB = new Checkbox("Srednje", radioBtnGroup, false);
		teskoRB = new Checkbox("Tesko", radioBtnGroup, false);

		lakoRB.addItemListener(e -> setujIntervalCekanjaIKorake(TezinaEnum.LAKO));
		srednjeRB.addItemListener(e -> setujIntervalCekanjaIKorake(TezinaEnum.SREDNJE));
		teskoRB.addItemListener(e -> setujIntervalCekanjaIKorake(TezinaEnum.TESKO));

		controlPanel.add(lakoRB);
		controlPanel.add(srednjeRB);
		controlPanel.add(teskoRB);
	}

	private void setujIntervalCekanjaIKorake(TezinaEnum tezinaEnum) {
		basta.setIntervalCekanja(tezinaEnum.getIntervalCekanja());
		basta.setBrojKoraka(tezinaEnum.getBrojKoraka());
	}
}
