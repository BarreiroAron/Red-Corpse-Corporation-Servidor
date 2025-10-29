package io.RedCorpseCorporation.juego;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Pantallas.PantallaCarga;
import Utiles.Render;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class RedCorpseCorporationMain extends Game {
    private SpriteBatch batch;
    private Texture MesaPrincipal;
    private Texture CartelPuntaje;

    private OrthographicCamera camera;
    private Viewport viewport;

    private static final float VIRTUAL_WIDTH = 1920;
    private static final float VIRTUAL_HEIGHT = 1080;
    
    @Override
    public void create() {
        Render.inicio = this;
        Render.batch = new SpriteBatch();
        setScreen(new menues.MenuPrincipal(this));

       // this.setScreen(new PantallaCarga(this)); // ✅ Pasás this como Game

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        viewport.apply();

        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        Render.batch.setProjectionMatrix(camera.combined); //Con esto se dimensiona bien si no se usa un monitor de pc
        super.render(); //con esto se ejecuta la pantalla
       // batch.setProjectionMatrix(camera.combined);
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
 
    }
    
    public OrthographicCamera getCamera() {
        return camera;
    }
}
