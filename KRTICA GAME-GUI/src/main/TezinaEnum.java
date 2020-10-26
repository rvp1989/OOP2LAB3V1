package main;

public enum TezinaEnum {
	LAKO(1000, 10),
	SREDNJE(750, 8),
	TESKO(500, 6);
	
	private int intervalCekanja;
	private int brojKoraka;
	
	private TezinaEnum(int intervalCekanja, int brojKoraka) {
		this.intervalCekanja = intervalCekanja;
		this.brojKoraka = brojKoraka;
	}
	
	public int getIntervalCekanja() {
		return intervalCekanja;
	}
	
	public int getBrojKoraka() {
		return brojKoraka;
	}
}
