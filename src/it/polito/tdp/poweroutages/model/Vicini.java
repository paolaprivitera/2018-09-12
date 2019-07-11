package it.polito.tdp.poweroutages.model;

public class Vicini implements Comparable<Vicini>{

	private Nerc nerc;
	private int peso;
	public Vicini(Nerc nerc, int peso) {
		this.nerc = nerc;
		this.peso = peso;
	}
	public Nerc getNerc() {
		return nerc;
	}
	public void setNerc(Nerc nerc) {
		this.nerc = nerc;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Vicini altro) {
		
		return altro.peso-this.peso;
	}
	
	
	
}
