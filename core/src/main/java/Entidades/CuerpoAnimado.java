package Entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import Utiles.Imagen;

public class CuerpoAnimado implements Cuerpo {

    private final Animation<TextureRegion> anim; //clase que cuenta 
    private float stateTime = 0f;	//contador de frames
    private String id;
    public CuerpoAnimado(Texture sheet, //consturtor con texture de una
                         int frameWidth,
                         int frameHeight,
                         float frameDurationSecs,
                         String id) {

        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight); // se determina cada region del frame
        Array<TextureRegion> frames = new Array<>();

        this.id=id;
        
        for (TextureRegion[] row : tmp)
            for (TextureRegion reg : row)
                frames.add(reg);
        		//crea el array list de cada frame un por uno
        anim = new Animation<>(frameDurationSecs, frames, Animation.PlayMode.LOOP); // aca se crea la animacion con el array de cada frame
    }
    
    public CuerpoAnimado(Imagen sheetImagen,   //constructor adaptado de imagen 
            int frameWidth,
            int frameHeight,
            float frameDurationSecs, String id) {
    	
    this(sheetImagen.getTexture(),      //lo pasa a texture             
            frameWidth,
            frameHeight,
            frameDurationSecs, id);
    }

    @Override
    public void draw(SpriteBatch batch,
                     float x, float y,
                     float width, float height,
                     float delta) {

        stateTime += delta;
        TextureRegion frame = anim.getKeyFrame(stateTime, true);


        float scale = Math.min(width  / frame.getRegionWidth(),
                               height / frame.getRegionHeight());

        float dw = frame.getRegionWidth()  * scale;
        float dh = frame.getRegionHeight() * scale;


        float drawX = x + (width  - dw) / 2f;
        float drawY = y + (height - dh) / 2f;

        batch.draw(frame, drawX, drawY, dw, dh);
    }

	public String getId() {
		return this.id;
	}
}
