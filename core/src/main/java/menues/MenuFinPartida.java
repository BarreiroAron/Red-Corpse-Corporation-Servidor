package menues;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import Pantallas.PantallaCarga;
import Utiles.Recursos;

public class MenuFinPartida implements Screen{

	private final Game game;
	
	private Stage  stage;
	
	private ImageButton btnMenu ,btnJugarOtraVez;
	
	private Texture texMenuUp, texMenuDn;
	private Texture texJugarOtraVezUp, texJugarOtraVezDn;;
	
	public MenuFinPartida(Game game) {
		this.game =game;
	}

	@Override
	public void show() {
		
		 stage = new Stage(new ScreenViewport());
	     Gdx.input.setInputProcessor(stage);
	     
	     texMenuUp = new Texture(Gdx.files.internal(Recursos.BOTON_VOLVER_MENU));
	     texMenuDn = new Texture(Gdx.files.internal(Recursos.BOTON_VOLVER_MENU));
	     
	     texJugarOtraVezUp = new Texture(Gdx.files.internal(Recursos.BOTON_REINICIAR));
	     texJugarOtraVezDn = new Texture(Gdx.files.internal(Recursos.BOTON_REINICIAR));
	     
	     ImageButtonStyle backStyle = new ImageButtonStyle();
	        backStyle.up   = new TextureRegionDrawable(texMenuUp);
	        backStyle.down = new TextureRegionDrawable(texMenuDn);
	        
	        btnMenu = new ImageButton(backStyle);
	        btnMenu.addListener(new ChangeListener() {
	            @Override public void changed(ChangeEvent event, Actor actor) {
	                game.setScreen(new MenuPrincipal(game)); // vuelve al menu principal
	                dispose();                        // liberamos recursos
	            }
	     });
	        
	        ImageButtonStyle backStyle2 = new ImageButtonStyle();
	        backStyle2.up   = new TextureRegionDrawable(texJugarOtraVezUp);
	        backStyle2.down = new TextureRegionDrawable(texJugarOtraVezDn);
	        
	        btnJugarOtraVez = new ImageButton(backStyle2);
	        btnJugarOtraVez.addListener(new ChangeListener() {
	            @Override public void changed(ChangeEvent event, Actor actor) {
	                game.setScreen(new PantallaCarga(game)); // vuelve al menu principal
	                dispose();                        // liberamos recursos
	            }
	     });
	        
	     Table root = new Table();
	     root.setFillParent(true);
	     root.defaults().pad(15);
	        
	     root.add(btnMenu).padTop(80).size(220, 80);
	     
	     root.add(btnJugarOtraVez).padTop(80).size(220, 80);
	     
	     stage.addActor(root);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		stage.act(delta);
        stage.draw();
	}

	@Override
	public void resize(int w, int h) { stage.getViewport().update(w, h, true);}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		 stage.dispose();
		 texMenuUp.dispose();
		 texMenuDn.dispose();
		 texJugarOtraVezUp.dispose();
		 texJugarOtraVezDn.dispose();
	}

}

