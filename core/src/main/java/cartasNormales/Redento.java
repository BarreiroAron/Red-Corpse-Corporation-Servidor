package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class Redento extends Carta {

	//Redento del Blasphemus
	/*Cuando esta carta es jugada, tu rival pierde la posibilidad de robar 
	 * del mazo durante cuatro turnos (este efecto no aplica si es que tiene una carta en la
	 *  mano y la juega y que
	 *   diga algo como “Robar del mazo”). Tu contador pierde 8 puntos.
	 */

	public Redento() {
		super(0, 20, Habilidad.TIRAR_CARTA_ALEATOREA, new Imagen(Recursos.REDENTO), false, 0, 0, 
				"Cuando esta carta es jugada, tu rival pierde la posibilidad de robar del mazo durante cuatro turnos \n"
				+ "(este efecto no aplica si es que tiene una carta en la mano y la juega y que diga algo como “Robar del mazo”).\n"
				+ " Tu contador pierde 20 de puntos.\n",TipoDeCarta.NORMAL);
	}
}