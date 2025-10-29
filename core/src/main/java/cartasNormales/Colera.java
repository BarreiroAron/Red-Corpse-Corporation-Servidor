package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class Colera extends Carta {

	private int puntosDisminuidos;
	
	//Colera (esta es una carta sin referencia
	
	/*
Al jugar esta carta, te ves obligado
 en los siguientes 3 turnos a solo robar del mazo. 
 Si en las 3 robadas no sale ninguna carta mala: Tus puntos se reducen en 20.*/

	public Colera() {
		super(0, 30, Habilidad.COLERA , new Imagen(Recursos.COLERA), true , 0,0, 
				"Al jugar esta carta, te ves obligado en los siguientes 3 turnos a solo robar del mazo.\n"
				+ " Si en las 3 robadas no sale ninguna carta mala: Tus puntos se reducen en 30.",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
	
}
