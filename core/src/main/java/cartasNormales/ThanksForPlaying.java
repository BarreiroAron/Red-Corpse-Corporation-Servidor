package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;
public class ThanksForPlaying extends Carta {

	//Carta de fresa ThanksForPlaying del Celeste
	/*Tus puntos se reducen en 15.*/
	
	private int puntosDisminuidos;

	public ThanksForPlaying() {
		super("THANKS_FOR_PLAYING",0, -70, Habilidad.MODIFICAR_PUNTOS, new Imagen(Recursos.THX_FOR_PLAYING_CARTA), false , 0,0, "Reduce 70 de tus puntos",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
}