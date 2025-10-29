package sonidos;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.LongMap;
import com.badlogic.gdx.math.MathUtils;

public class SonidoManager {

	private static final SonidoManager INSTANCE = new SonidoManager();
    public static SonidoManager i() { return INSTANCE; }
    private SonidoManager() {}
    
    private volatile float ambientVol = 1f;  //esto para sonido del hambiente
    private volatile float sfxVol     = 1f;  //esto para efectos como tirar carta
    
    private final LongMap<SoundEntry> ambientLoops = new LongMap<>();
    

    public long playAmbientLoop(Sound s) {               // se usa en tus hilos
        long id = s.loop(ambientVol);
        ambientLoops.put(id, new SoundEntry(s, id));
        return id;
    }
    public void stopAmbientLoop(long id) {
        SoundEntry e = ambientLoops.remove(id);
        if (e != null) e.sound.stop(id);
    }
    public void playSfx(Sound s) { s.play(sfxVol); }      // se usa en JuegoPantalla

    public void setAmbientVolume(float v){
        ambientVol = clamp(v);
        for (SoundEntry e : ambientLoops.values())
            e.sound.setVolume(e.id, ambientVol);
    }
    public void setSfxVolume(float v){ sfxVol = clamp(v); }

    public float getAmbientVolume(){ return ambientVol; }
    public float getSfxVolume(){ return sfxVol; }
    
    //record lo que hace es como declarar una clase de dato con cosas predefinidas (constructos,get/setters, etc)
    private record SoundEntry(Sound sound, long id) {}
    private float clamp(float v){ return MathUtils.clamp(v,0f,1f); }
}