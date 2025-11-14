package Utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Imagen {
	//clase para cargar las imagenes y dibujarlas
	
		private Texture t;
		private Sprite s;
		
		
		public Imagen(String ruta) {
	        // 🚫 Si el server está headless, NO crees texturas
	        if (Utiles.Headless.activo() || Gdx.app == null || Gdx.graphics == null) {
	            this.t = null; // marcamos como “sin textura”
	            return;
	        }
	        this.t = new Texture(ruta);
	    }

	    public void dibujar(SpriteBatch batch, float x, float y, float w, float h) {
	        if (t == null) return; // en server no dibuja nada
	        batch.draw(t, x, y, w, h);
	    }
		
		public void dibujar() {
			s.draw(Render.batch);
		}
		

		public void setTransparencia(float f) {
			s.setAlpha(f);
		}

		public Texture getTexture() {
			// TODO Auto-generated method stub
			return t;
		}
}
