package sonidos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import Utiles.Util;

public class SonidoAmbiental {
    private long idSonidoVentilador;
    private Sound sonidoVentilador;
    private Sound sonidoDisparo;
    private boolean audioCorre;

    private final int probabilidadDisparo = 10000000;
    private float tiempoAcumulado = 0f;
    private final float intervalo = 10f;

    public SonidoAmbiental() {
        sonidoVentilador = Gdx.audio.newSound(Gdx.files.internal("VentiladorPared.ogg"));
        sonidoDisparo    = Gdx.audio.newSound(Gdx.files.internal("SonidoDisparo.mp3"));
        audioCorre = true;

        idSonidoVentilador = SonidoManager.i().playAmbientLoop(sonidoVentilador);
    }

    public void update(float delta) {
        if (!audioCorre) 
        	return;

        tiempoAcumulado += delta;
        if (tiempoAcumulado >= intervalo) {

            if (Util.sacarNumeroRandom(probabilidadDisparo) == 777) {
                SonidoManager.i().playSfx(sonidoDisparo);
            }
        }
    }

    public void stop() {
        audioCorre = false;
        SonidoManager.i().stopAmbientLoop(idSonidoVentilador);
        sonidoVentilador.dispose();
        sonidoDisparo.dispose();
    }
}
