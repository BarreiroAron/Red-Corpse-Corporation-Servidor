package cartasNormales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class Mimico extends Carta {
	
	private int puntosDisminuidos;
	
	//Mimico de referencia al Dark Souls
	
	/*Al ser jugada esta carta, en tu siguiente turno tenes que 
	 * robar una carta del mazo si o si. Si esta carta resulta ser 
	 * otra mímica. Entonces tus puntos se reducen en 30 y los puntos del rival 
	 * aumentan en 50. Si no es un mímico, los puntos del rival son reducidos en 30 
	 * y tus puntos son aumentados en 70
	 */

	public Mimico() {
		super("MIMICO",50, -30, Habilidad.MIMICO, new Imagen(Recursos.MIMICO), false, 0, 0, 
				"Al ser jugada esta carta, en tu siguiente turno tenes que robar una carta del mazo si o si.\n "
				+ "Si esta carta resulta ser otra mímica. Entonces tus puntos se reducen en 30 y los puntos del rival aumentan en 50.\n"
				+ " Si no es un mímico, los puntos del rival son reducidos en 30 y tus puntos son aumentados en 70\r\n",TipoDeCarta.NORMAL);
	}
	
	@Override
	public void setPuntosDisminuidos(int puntosDisminuidos) {
		this.puntosDisminuidos = puntosDisminuidos;
	}
}
