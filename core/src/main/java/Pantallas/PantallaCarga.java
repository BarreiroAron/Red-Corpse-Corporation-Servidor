package Pantallas;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Entidades.Entidad;
import Entidades.Jugador;
import Entidades.Rival;
import Utiles.Imagen;
import Utiles.Recursos;
import Utiles.Render;
import juegos.Juego;

import Entidades.CuerpoAnimado;

public class PantallaCarga implements Screen {
	
	private final Game game;
	
	private Imagen fondo;
	private final float sumaDeTransparencia = 0.1f;
	private SpriteBatch i;
	private float f = 0;
	private float contadorTiempo = 0, tiempoEsperado = 1.5f;
	private boolean procesoFadeTerminado = false;
	
	private ArrayList<Entidad> jugadores = new ArrayList<>();
	private CuerpoAnimado[] personajesAnimados = Utiles.Util.crearListaImagPerRan();
	private Entidad entidad1 = new Jugador("Entidad 1", 100, personajesAnimados[1]);
	private Entidad entidad2 = new Rival("Entidad 2", 100, personajesAnimados[2]);
	private Entidad entidad3 = new Rival("Entidad 3", 100, personajesAnimados[3]);

	private Juego juego;
	
	// cámara y viewport
	private OrthographicCamera camera;
	private Viewport viewport;
	
	private static final float VIRTUAL_WIDTH = 1920;
	private static final float VIRTUAL_HEIGHT = 1080;

	public PantallaCarga(Game game) {
        this.game = game;
    }
	
	@Override
	public void show() { 
		fondo = new Imagen(Recursos.FONDO);
		i = Render.batch;
		fondo.setTransparencia(f);
		
		jugadores.add(entidad1);
		jugadores.add(entidad2);
		jugadores.add(entidad3);

		// inicializar cámara y viewport
		camera = new OrthographicCamera();
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
		camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
	}
	
	@Override
	public void render(float delta) {
		Render.limpiarPantalla();

		// actualizar cámara
		camera.update();
		i.setProjectionMatrix(camera.combined);

		i.begin();
			// dibujar fondo en coords virtuales
			fondo.dibujar(i, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
		i.end();
		
		procesarFade();
	}

	private void procesarFade() {
		if (!procesoFadeTerminado) {
			f += sumaDeTransparencia;
			if (f > 1) {
				f = 1;
				procesoFadeTerminado = true;
			}
		} else {
			contadorTiempo += sumaDeTransparencia;
			if (contadorTiempo > tiempoEsperado) {
				f -= 0.01f;
				if (f < 0) {
					f = 0;
					game.setScreen(new JuegoPantalla(game, new Juego(jugadores)));
				}
			}
		}
		fondo.setTransparencia(f);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}
	@Override public void dispose() {}
}
