package Utiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Imagen {
	//clase para cargar las imagenes y dibujarlas
	
		private Texture t;
		private Sprite s;
		
		public Imagen(String ruta) {
			t = new Texture(ruta);
			s = new Sprite(t);
		}
		
		public void dibujar() {
			s.draw(Render.batch);
		}
		
		public void dibujar(SpriteBatch batch, float x, float y, float width, float height) {
		    s.setPosition(x, y);
		    s.setSize(width, height);
		    s.draw(batch);
		}

		public void setTransparencia(float f) {
			s.setAlpha(f);
		}

		public Texture getTexture() {
			// TODO Auto-generated method stub
			return t;
		}
}
