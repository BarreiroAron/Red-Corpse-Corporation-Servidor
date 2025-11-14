package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class HambreContenida extends Carta {
	
	private int puntosDisminuidos;
	
	//Carta de Briar LOL
	
	/*Tus puntos aumentan en 30 y los del rival se reducen en 30, a cambio,
	 *  puedes escoger hasta 2 cartas del mazo que quieras a tu elección. 
	 * Si hay más de un jugador, los puntos se intercambian con el jugador siguiente en la ronda.
	 */

	public HambreContenida() {
		super("HAMBRE_CONTENIDA",30, 30, Habilidad.HAMBRE, new Imagen(Recursos.HAMBRE_CONTENIDA), false, 0, 0, 
				"Tus puntos aumentan en 3% y los del rival se reducen en 30, a cambio, \n"
				+ "puedes escoger hasta 2 cartas del mazo que quieras a tu elección. \n"
				+ "Si hay más de un jugador, los puntos se intercambian con el jugador siguiente en la ronda.\n",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
}
