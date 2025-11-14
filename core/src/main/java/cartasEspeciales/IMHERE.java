package cartasEspeciales;

import Utiles.Imagen;
import Utiles.Recursos;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;

public class IMHERE extends Carta {
	
	public IMHERE() {
		super("IMHERE",0, 0, Habilidad.APARICION_ALEATORIA, new Imagen(Recursos.IM_SCARED), false, 12, 20, "Esta carta no puede ser robada y no puede ser obtenida de forma común.\n"
				+ " Esta carta tiene probabilidades aleatorias de aparecer cada vez que tires una carta." ,TipoDeCarta.ESPECIAL);
	}
}