package Entidades;

import Utiles.Imagen;

public class Rival extends Entidad {

	public Rival(String nombre, int puntos,CuerpoAnimado cuerpo) {
		super(nombre, puntos,cuerpo);
	}
	
	public void agregarPuntos(int puntos) {
		this.puntos = puntos;
	}
	
}
