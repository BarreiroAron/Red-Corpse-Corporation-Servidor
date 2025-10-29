package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class Estrenimiento extends Carta {

	private int puntosDisminuidos;
	
	//Estreñimiento (carta sin referencia)
	
	/*Cuando es tirada esta carta, el rival (en su turno) es obligado 
a tirar una carta que si o si modifique de alguna manera tus puntos 
o los de el (Shaco no cuenta), de no poder tirar nada o no poder 
modificar los puntos. Roba dos cartas del mazo, esas cartas robadas si o si son malas.*/
	
	public Estrenimiento() {
		super(0, 0, Habilidad.ESTRENIMIENTO, new Imagen(Recursos.ESTRENIMIENTO), false, 0, 0,
				"Cuando es tirada esta carta, el rival (en su turno) es obligado a tirar una carta que si o si\n"
				+ "modifique de alguna manera tus puntos o los de el (Shaco no cuenta),\n"
				+ " de no poder tirar nada o no poder modificar los puntos.\n"
				+ " Roba dos cartas del mazo, esas cartas robadas si o si son malas.\n",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
}
