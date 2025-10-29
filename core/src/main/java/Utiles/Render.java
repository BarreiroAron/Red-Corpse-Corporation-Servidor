package Utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont; //CUIADO
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.RedCorpseCorporation.juego.RedCorpseCorporationMain;

public class Render {
	//para evitar pasar todo el timepo la imagen de un lugar a otra
	
	public static SpriteBatch batch;
	public static RedCorpseCorporationMain inicio;
	public static BitmapFont font = new BitmapFont();
	
	public static void limpiarPantalla() {
		//Limpia la pantalla a blanco
		Gdx.gl.glClearColor(0,0,0,0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	
}
