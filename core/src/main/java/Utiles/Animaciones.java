package Utiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.math.Interpolation;

import java.util.HashMap;
import java.util.Map;

public class Animaciones {

    /** Dibuja la imagen y la agranda si el mouse está encima. */
	
	/*@param delta   tiempo del frame (lo recibes en render) */
	private static final Map<Imagen, Float> escalaActual = new HashMap<>();
	public static boolean animarHover(
	        SpriteBatch batch,
	        Imagen imagen,
	        float x, float y,
	        float ancho, float alto,
	        float mouseX, float mouseY,
	        float escalaObjetivo,
	        float rapidez,        
	        float delta) {

	    boolean hovered =  mouseX >= x && mouseX <= x + ancho &&
	                       mouseY >= y && mouseY <= y + alto;

	    float target = hovered ? escalaObjetivo : 1f;

	    // escala previa (por defecto 1)
	    float escala = escalaActual.getOrDefault(imagen, 1f);

	    // interpolación suave según delta
	    escala = MathUtils.lerp(escala, target, rapidez * delta);

	    // guarda para el próximo frame
	    escalaActual.put(imagen, escala);

	    // Dibujar centrado
	    float dibW = ancho * escala, dibH = alto * escala;
	    float dibX = x - (dibW - ancho) / 2f;
	    float dibY = y - (dibH - alto ) / 2f;

	    imagen.dibujar(batch, dibX, dibY, dibW, dibH);
	    return hovered;
	}
    
    /* ----------  ESTRUCTURA INTERNA PARA EL MOVIMIENTO ---------- */
    private static class Movimiento {
        Imagen imagen;
        float  xOrigen,  yOrigen;
        float  xDestino, yDestino;
        float  ancho, alto;
        float  tiempo;          // acumulado
        float  duracion;        // total en segundos
        Runnable alFinal;       // callback opcional
    }
    
    private static final List<Movimiento> enCurso = new LinkedList<>();
    
    /** Inicia un movimiento de la imagen desde (x0,y0) hasta (x1,y1). */
    public static void iniciarMovimiento(
            Imagen img,
            float x0, float y0,
            float x1, float y1,
            float ancho, float alto,
            float segundos,
            Runnable alFinal) {

        Movimiento m = new Movimiento();
        m.imagen   = img;
        m.xOrigen  = x0;  m.yOrigen  = y0;
        m.xDestino = x1;  m.yDestino = y1;
        m.ancho = ancho;  m.alto = alto;
        m.tiempo   = 0f;
        m.duracion = segundos;
        m.alFinal  = alFinal;

        enCurso.add(m);
    }

    /** Avanza y dibuja todos los movimientos activos. */
    public static void actualizarYDibujarMovimientos(SpriteBatch batch, float delta) {
        Iterator<Movimiento> it = enCurso.iterator();
        while (it.hasNext()) {
            Movimiento m = it.next();
            m.tiempo += delta;

            float p = Math.min(m.tiempo / m.duracion, 1f);
            p = Interpolation.smooth.apply(0f, 1f, p);        // curva suave

            float x = MathUtils.lerp(m.xOrigen,  m.xDestino, p);
            float y = MathUtils.lerp(m.yOrigen,  m.yDestino, p);

            m.imagen.dibujar(batch, x, y, m.ancho, m.alto);

            if (p >= 1f) {
                if (m.alFinal != null) m.alFinal.run();
                it.remove();
            }
        }
    }
    
}
