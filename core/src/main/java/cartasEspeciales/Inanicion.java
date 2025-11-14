package cartasEspeciales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class Inanicion extends Carta {
	//Carta de Inscrypthion inanicion
	/*Esta carta no puede ser robada y no puede ser obtenida de forma común. 
	 * Si tu personaje se queda sin cartas en la mano, aparece esta carta llamada 
	 * “inaniciòn”. Al tener esta carta en la mano, según vayan pasando los turnos,
	 *  tus puntos van a ir aumentando (esto va a ser progresivamente, el primer turno x1, 
	 *  el segundo x2, el tercero x3 y así susecivamente).
	 * Referencia a Inscryption (Carta de Inanición) */

	public Inanicion() {
		super("INANICION",0, 300, Habilidad.SIN_CARTAS_EN_LA_MANO, new Imagen(Recursos.INANICION_CARTA), false, 0, 0, "Esta carta no puede ser robada y no puede ser obtenida de forma común.\n "
				+ "Si tu personaje se queda sin cartas en la mano, aparece esta carta llamada “inaniciòn”.\n "
				+ "Al tener esta carta en la mano, según vayan pasando los turnos,\n"
				+ " tus puntos van a ir aumentando en 300 \n",TipoDeCarta.ESPECIAL);
	}
}
