package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class NotToday extends Carta {

	private int puntosDisminuidos;
	
	//Carta de NotToday del Minecraft
	
	/*Bloquea el efecto de la carta del rival jugada en este momento.*/

	public NotToday() {
		super("NOT_TODAY",0, 0, Habilidad.BLOQUEAR_EFECTO, new Imagen(Recursos.NOT_TODAY), false, 0, 0, 
				"Bloquea el efecto de la carta del rival jugada en este momento.",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
}
