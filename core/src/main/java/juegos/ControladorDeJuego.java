package juegos;

import java.util.ArrayList;

import Entidades.Entidad;
import cartas.Carta;
import cartas.TipoDeCarta;

public interface ControladorDeJuego {
		void siguienteJugador();
		void modificarPuntos(Entidad objetivo, int puntos, boolean esPorcentual);
		void modificarPuntosGlobal(Entidad jugadorEjecutador, int puntos, boolean esPorcentual);
		void marcarReinicio();
	    void cambiarDireccion();
	    void robarCarta(Entidad jugador);
	    void mostrarCartasSiguientes(int cantidad);
	    void mezclarMazo();
	    void intercambiarPuntos(Entidad jugador, Entidad rival);
		ArrayList<Carta> getListaPorTipoCartas(TipoDeCarta mala, ArrayList<Carta> ListaCartas1);
		ArrayList<Carta> getMazo();
		void pasarCartas(ArrayList<Carta> ListaCartas1, ArrayList<Carta> ListaCartas2);
		ArrayList<Carta> getMesa();
		void eliminarPorListaCartas(ArrayList<Carta> ListaCartas1, ArrayList<Carta> ListaCartas2);
		void activarRobarMazoAEleccion(Entidad jugador);
		void activarColera(Entidad jugador);
}
