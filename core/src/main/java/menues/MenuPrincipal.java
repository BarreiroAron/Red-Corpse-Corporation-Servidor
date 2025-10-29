package menues;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Pantallas.PantallaCarga;
import Utiles.Animaciones;
import Utiles.Imagen;
import Utiles.Render;
import sonidos.SonidoMenuPrincipal;

public class MenuPrincipal implements Screen {

    private final Game game;
    private SpriteBatch batch;

    private Imagen fondo;
    private Imagen botonJugar;
    private Imagen botonOpciones;
    private Imagen botonSalir;

    private float xBoton, yBotonJugar, yBotonOpciones, yBotonSalir;
    private float anchoBoton = 309, altoBoton = 115;

    SonidoMenuPrincipal sonidoMenuPrincipalHilo;
    
    private Screen pantallaAnterior;
    private boolean pausa = false;

    private Sound redCorpseCorporationAudio;
    private long idRedCorpseCorporationAudio;

    // cámara y viewport para manejar escalado
    private OrthographicCamera camera;
    private Viewport viewport;

    // mouse en coordenadas virtuales
    private float mouseX, mouseY;

    private static final float VIRTUAL_WIDTH = 1920;
    private static final float VIRTUAL_HEIGHT = 1080;

    public MenuPrincipal(Game game) {
        this.game = game;
    }
    
    public MenuPrincipal(Game game, Screen pantallaAnterior) {
        this.game = game;
        this.pantallaAnterior = pantallaAnterior;
        this.pausa = true;
    }

    @Override
    public void show() {
        batch = Render.batch;

        fondo = new Imagen("FondoCarga.jpg");
        botonJugar = new Imagen("jugarBoton.png");
        botonOpciones = new Imagen("opcionesBoton.png");
        botonSalir = new Imagen("salirBoton.png");

        startAudio();

        this.sonidoMenuPrincipalHilo = new SonidoMenuPrincipal();
        this.sonidoMenuPrincipalHilo.start();

        // inicializar cámara
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);

        // posiciones en coords virtuales
        xBoton = (VIRTUAL_WIDTH - anchoBoton) / 2f;
        yBotonJugar = 600f;
        yBotonOpciones = 400f;
        yBotonSalir = 200f;
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla();

        // actualizar cámara
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // actualizar mouse a coords virtuales
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);
        mouseX = mouse.x;
        mouseY = mouse.y;

        batch.begin();
        fondo.dibujar(batch, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        boolean hoverJugar = Animaciones.animarHover(batch, botonJugar, xBoton, yBotonJugar, anchoBoton, altoBoton, mouseX, mouseY, 1.1f, 10f, delta);
        boolean hoverOpciones = Animaciones.animarHover(batch, botonOpciones, xBoton, yBotonOpciones, anchoBoton, altoBoton, mouseX, mouseY, 1.1f, 10f, delta);
        boolean hoverSalir = Animaciones.animarHover(batch, botonSalir, xBoton, yBotonSalir, anchoBoton, altoBoton, mouseX, mouseY, 1.1f, 10f, delta);

        batch.end();

        if (Gdx.input.justTouched() && Gdx.input.isButtonPressed(Buttons.LEFT)) {
            if (hoverJugar) {
                if (pausa && pantallaAnterior != null) {
                    game.setScreen(pantallaAnterior);
                } else {
                    game.setScreen(new PantallaCarga(game));
                }
            } else if (hoverOpciones) {
                game.setScreen(new MenuOpciones(game, this));
            } else if (hoverSalir) {
                Gdx.app.exit();
            }
        }
    }

    public void startAudio() {
        if (redCorpseCorporationAudio == null) {
            redCorpseCorporationAudio = Gdx.audio.newSound(Gdx.files.internal("RedCorpseCorporationAudio.wav"));
            idRedCorpseCorporationAudio = redCorpseCorporationAudio.play();
            redCorpseCorporationAudio.setLooping(idRedCorpseCorporationAudio, false);
        }
    }

    public void stopAudio() {
        if (redCorpseCorporationAudio != null) {
            redCorpseCorporationAudio.stop(idRedCorpseCorporationAudio);
            redCorpseCorporationAudio.dispose();
            redCorpseCorporationAudio = null;
        }
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() { sonidoMenuPrincipalHilo.stop();}
    @Override public void dispose() { sonidoMenuPrincipalHilo.stop(); }
}
