package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class Snake extends Carta {

	private int puntosDisminuidos;
	
	//Snake del metal gear solid
	
	/* Permite ver los puntos del rival durante un turno. Baja 8 puntos de tu contador*/

	public Snake() {
		super("SNAKE",0, 8, Habilidad.VER_PUNTOS_RIVAL, new Imagen(Recursos.SNAKE), false, 0, 0, 
				"Permite ver los puntos del rival durante un turno. \nBaja 8 puntos de tu contador",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
}
