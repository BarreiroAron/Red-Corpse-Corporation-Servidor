package Utiles;

import com.badlogic.gdx.Gdx;

import Entidades.CuerpoAnimado;
import cartas.Carta;
import cartasEspeciales.IMHERE;
import cartasEspeciales.Inanicion;
import cartasMalas.PecadoDeLaCodicia;
import cartasMalas.Sonambulo;
import cartasNormales.CambioDeRonda;
import cartasNormales.Chester;
import cartasNormales.Colera;
import cartasNormales.Company;
import cartasNormales.Estrenimiento;
import cartasNormales.HambreContenida;
import cartasNormales.KingDice;
import cartasNormales.Mimico;
import cartasNormales.NotToday;
import cartasNormales.OjoQueTodoLoVe;
import cartasNormales.Redento;
import cartasNormales.Saltamontes;
import cartasNormales.Snake;
import cartasNormales.ThanksForPlaying;

import java.util.Random;

public class Util {
	
	public static int getAnchoPantalla() {
        return Gdx.graphics.getWidth();
    }

    public static int getAltoPantalla() {
        return Gdx.graphics.getHeight();
    }
    
    public static int sacarNumeroRandom(int porbabilidad) {
    	int randomNumero = (int) (Math.random() * porbabilidad) + 1;
    	return randomNumero;
    }

    protected static <T> void intercambiarDatos(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    public static <T> void mezclarArray(T[] array) {
    	Random random = new Random();
        int i = 0;
        for (int j : random.ints(array.length, 0, array.length).toArray()) {
        	intercambiarDatos(array, i++, j);
        }
    }
    
	public static CuerpoAnimado[] crearListaImagPerRan() {
		CuerpoAnimado[] personajes = {
				new CuerpoAnimado(
						new Imagen(Recursos.PERSONAJE_VIOLETA),
						424,              // ancho de cada frame	
						613,              // alto  de cada frame
						0.08f,
						"PERSONAJE_VIOLETA"),
				new CuerpoAnimado(
				        new Imagen(Recursos.PERSONAJE_CELESTE),
				        873,              // ancho de cada frame
				        1080,              // alto  de cada frame
				        0.08f,
				        "PERSONAJE_CELESTE"),
				new CuerpoAnimado(
				        new Imagen(Recursos.PERSONAJE_AMARILLO),
				        820,              // ancho de cada frame
				        1180,              // alto  de cada frame
				        0.08f,
				        "PERSONAJE_AMARILLO"),
				new CuerpoAnimado(
				        new Imagen(Recursos.PERSONAJE_ROJO),
				        820,              // ancho de cada frame
				        1180,              // alto  de cada frame
				        0.08f,
				        "PERSONAJE_ROJO"),
				};
		mezclarArray(personajes); 
		return personajes;
	}
	
	public static String[] crearListaIdPerRan() {
	    String[] ids = {
	        "PERSONAJE_VIOLETA",
	        "PERSONAJE_CELESTE",
	        "PERSONAJE_AMARILLO",
	        "PERSONAJE_ROJO"
	    };

	    // ya tenés mezclarArray genérico, así que esto funciona
	    mezclarArray(ids);
	    return ids;
	}
	
	public static Carta crearCartaDesdeId(String id) {

        switch (id) {

            // ---------------- CARTAS NORMALES ---------------- //
            case "CAMBIO_DE_RONDA":
                return new CambioDeRonda();

            case "CHESTER":
                return new Chester();

            case "COLERA":
                return new Colera();

            case "COMPANY":
                return new Company();

            case "ESTENIMIENTO":
                return new Estrenimiento();

            case "HAMBRE_CONTENIDA":
                return new HambreContenida();

            case "KING_DICE":
                return new KingDice();

            case "MIMICO":
                return new Mimico();

            case "NOT_TODAY":
                return new NotToday();

            case "OJO_QUE_TODO_LO_VE":
                return new OjoQueTodoLoVe();

            case "REDENTO":
                return new Redento();

            case "SALTAMONTES":
                return new Saltamontes();

            case "SNAKE":
                return new Snake();

            case "THANKS_FOR_PLAYING":
                return new ThanksForPlaying();


            // ---------------- CARTAS MALAS ---------------- //
            case "SONAMBULO":
                return new Sonambulo();

            case "PECADO_DE_LA_CODICIA":
                return new PecadoDeLaCodicia();


            // ---------------- CARTAS ESPECIALES ---------------- //
            case "IMHERE":
                return new IMHERE();

            case "INANICION":
                return new Inanicion();


            default:
                System.out.println("[ERROR] Carta no reconocida: " + id);
                return null;
        }
    }
}
