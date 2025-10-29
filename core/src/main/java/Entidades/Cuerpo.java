package Entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Cuerpo {
    void draw(SpriteBatch batch,
              float x, float y,
              float width, float height,
              float delta);
}
