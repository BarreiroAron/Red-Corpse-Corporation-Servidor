// por ejemplo en redServer/ServidorAPI.java
package redServer;

import Entidades.Entidad;
import cartas.Carta;

public interface ServidorAPI {
    void enviarMesaActualAJugadores(int idxJugador, Carta cartaEnMesa);
    void enviarCartaATodos(Carta carta);
    void notificarFinPartida();
    void enviarTurnoAClientes();
    // acá vas sumando TODO lo que Juego deba pedirle a la red
	void avanzarTurno();
	void enviarModificacionDePuntos(Entidad objetivo, int puntos, boolean esPorcentual);
	void enviarCartaJugada(Carta cartaPendiente, Entidad jugadorQueLaJugoPendiente);
	void enviarDatosInicialesAlClienteTodos();
}

