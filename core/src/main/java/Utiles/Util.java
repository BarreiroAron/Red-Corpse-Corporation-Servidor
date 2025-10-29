package Utiles;

import com.badlogic.gdx.Gdx;

import Entidades.CuerpoAnimado;

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
						0.08f),
				new CuerpoAnimado(
				        new Imagen(Recursos.PERSONAJE_CELESTE),
				        873,              // ancho de cada frame
				        1080,              // alto  de cada frame
				        0.08f),
				new CuerpoAnimado(
				        new Imagen(Recursos.PERSONAJE_AMARILLO),
				        820,              // ancho de cada frame
				        1180,              // alto  de cada frame
				        0.08f),
				new CuerpoAnimado(
				        new Imagen(Recursos.PERSONAJE_ROJO),
				        820,              // ancho de cada frame
				        1180,              // alto  de cada frame
				        0.08f),
				};
		mezclarArray(personajes); 
		return personajes;
	}
}
