package sonidos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SonidoMenuPrincipal {
	
    private long idSonidoSoundtrack;
    private Sound sonidoSoundtrack;
    private boolean audioCorre;

    public SonidoMenuPrincipal() {
        sonidoSoundtrack = Gdx.audio.newSound(Gdx.files.internal("BalatroOrquestaSoundractProvisorio.mp3"));
        audioCorre = false;
    }
    
    public void start() {
        if (!audioCorre) {
            idSonidoSoundtrack = SonidoManager.i().playAmbientLoop(sonidoSoundtrack);
            audioCorre = true;
        }
    }

    public void stop() {
        if (audioCorre) {
            SonidoManager.i().stopAmbientLoop(idSonidoSoundtrack);
            audioCorre = false;
        }
    }

    public void dispose() {
        sonidoSoundtrack.dispose();
    }
}
