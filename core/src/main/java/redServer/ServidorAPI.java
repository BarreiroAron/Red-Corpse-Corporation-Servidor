// por ejemplo en redServer/ServidorAPI.java
package redServer;

import cartas.Carta;

public interface ServidorAPI {
    void enviarMesaActualAJugadores(int idxJugador, Carta cartaEnMesa);
    void enviarCartaATodos(Carta carta);
    void notificarFinPartida();
    void enviarTurnoAClientes();
    // acá vas sumando TODO lo que Juego deba pedirle a la red
	void avanzarTurno();
}

