package menues;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Utiles.Recursos;
import Utiles.Render;
import sonidos.SonidoManager;

public class MenuOpciones implements Screen {

    private final Game   game;
    private final Screen pantallaAnterior;


    private Stage  stage;
    private Slider ambientSlider, sfxSlider;
    private ImageButton btnVolver;

    private Texture texBar, texKnob;
    private Texture texBackUp, texBackDn;
    
    private OrthographicCamera camera;
    private Viewport viewport;
    
    private static final float VIRTUAL_WIDTH  = 1920f;
    private static final float VIRTUAL_HEIGHT = 1080f;

    public MenuOpciones(Game game, Screen pantallaAnterior) {
        this.game             = game;
        this.pantallaAnterior = pantallaAnterior;
    }

    @Override
    public void show() {

    	texBar     = new Texture(Gdx.files.internal("BarraReguladora.png"));
        texKnob    = new Texture(Gdx.files.internal("BotonRegulador.png"));
        texBackUp  = new Texture(Gdx.files.internal(Recursos.BOTON_VOLVER_MENU));
        texBackDn  = new Texture(Gdx.files.internal(Recursos.BOTON_VOLVER_MENU));

        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2f, VIRTUAL_HEIGHT / 2f, 0);
        
        stage = new Stage(viewport, Render.batch);
        Gdx.input.setInputProcessor(stage);
        
        SliderStyle sliderStyle = new SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(texBar);
        sliderStyle.knob       = new TextureRegionDrawable(texKnob);
        sliderStyle.knobBefore = sliderStyle.background;

        ImageButtonStyle backStyle = new ImageButtonStyle();
        backStyle.up   = new TextureRegionDrawable(texBackUp);
        backStyle.down = new TextureRegionDrawable(texBackDn);

       
        ambientSlider = new Slider(0, 1, 0.01f, false, sliderStyle);
        ambientSlider.setValue(SonidoManager.i().getAmbientVolume());
        ambientSlider.addListener(new ChangeListener() {
        	
            @Override public void changed(ChangeEvent event, Actor actor) {
                SonidoManager.i().setAmbientVolume(ambientSlider.getValue());
            }
            
        });

        sfxSlider = new Slider(0, 1, 0.01f, false, sliderStyle);
        sfxSlider.setValue(SonidoManager.i().getSfxVolume());
        sfxSlider.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                SonidoManager.i().setSfxVolume(sfxSlider.getValue());
            }
        });

       
        btnVolver = new ImageButton(backStyle);
        btnVolver.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(pantallaAnterior); 
                dispose();                       
            }
        });

    
        Table root = new Table();               // sin skin, usamos imagenes
        root.setFillParent(true);
        root.defaults().pad(15);

        root.add(ambientSlider).width(450).row();
        root.add(sfxSlider).width(450).row();
        root.add(btnVolver).padTop(40).size(220, 80);

        stage.addActor(root);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause()   {}
    @Override public void resume()  {}
    @Override public void hide()    {}

    @Override
    public void dispose() {
     
        stage.dispose();
        texBar.dispose();
        texKnob.dispose();
        texBackUp.dispose();
        texBackDn.dispose();
    }
}
