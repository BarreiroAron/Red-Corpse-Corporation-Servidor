package Entidades;

import java.util.ArrayList;

import Utiles.Imagen;
import cartas.Carta;

public class Jugador extends Entidad {
	
	private String nombre;
	private int puntos;
	
	public Jugador(String nombre, int puntos, CuerpoAnimado cuerpo) {
		super(nombre, puntos, cuerpo);
	}
	
	public ArrayList<Carta> getMano() {
		return mano;
	}

	public void setMano(ArrayList<Carta> mano) {
		this.mano = mano;
	}
	
	public void agregarCarta(Carta nuevaCarta) {
		this.mano.add(nuevaCarta);
	}
	
	public void agregarPuntos(int puntos) {
		this.puntos = puntos;
	}
	
	public void robarCarta(boolean robar) {
		
	}
	
	public void bloquearRobo() {
		
	}
}
