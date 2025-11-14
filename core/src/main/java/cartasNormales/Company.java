package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class Company extends Carta {

	private int puntosDisminuidos;
	
	//Carta de la computadora del Lethal Company
	
	/*Reinicia por completo la partida a excepción de los puntos de cada jugador y 
	 * el tiempo de la partida. Aumenta 20 puntos al rival
	 */

	public Company() {
		super("COMPANY",10, 0, Habilidad.REINICIAR_PARTIDA, new Imagen(Recursos.COMPANY), false, 0, 0, 
				"Reinicia por completo la partida a excepción de los puntos de cada jugador y el tiempo de la partida. \n"
				+ "Aumenta 10 puntos al rival",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
}