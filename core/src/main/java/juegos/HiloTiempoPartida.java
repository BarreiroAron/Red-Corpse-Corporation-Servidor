package juegos;

public class HiloTiempoPartida extends Thread {

    private float tiempoInicial;    // milisegundos
    private float tiempoRestante;   // milisegundos

    private volatile float progreso = 1.0f; // entre 1.0 y 0.0
    private boolean enEjecucion = false;

    private final TiempoListener listener;
    
    private boolean terminado = false;

    public HiloTiempoPartida(TiempoListener listener) {
        this.listener = listener;
    }

    /**
     * Reinicia el hilo con una nueva duracion en minutos.
     */
    public void setMinutos(float tiempo) {
        this.tiempoInicial = tiempo * 60 * 1000;
        this.tiempoRestante = tiempoInicial;
        this.progreso = 1.0f;
        this.enEjecucion = true;
    }

    /**
     * Detiene el hilo manualmente.
     */
    public void detener() {
        this.enEjecucion = false;
    }
    
    public void terminar() {
        terminado = true;
    }
    /**
     * Devuelve el progreso actual (entre 1.0 y 0.0).
     */
    public float getProgreso() {
        return progreso;
    }

    @Override
    public void run() {
        while (!terminado) {
            if (!enEjecucion || tiempoRestante <= 0) {
                if (tiempoRestante <= 0 && listener != null) {
                    progreso = 0.0f;
                    listener.onProgresoActualizado(progreso);
                    listener.onTiempoFinalizado();
                }

                try {
                    Thread.sleep(100); // pausa cuando no esta corriendo
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            tiempoRestante -= 100;
            if (tiempoRestante < 0) tiempoRestante = 0;

            progreso = (float) tiempoRestante / tiempoInicial;

            if (listener != null) {
                listener.onProgresoActualizado(progreso);
            }
        }
    }
}
