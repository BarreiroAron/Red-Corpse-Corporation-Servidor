package cartas;

import java.util.ArrayList;

import Entidades.Entidad;
import juegos.Juego;

public enum EnemigoDeterminado {
	IZQUIERDA(){
		@Override
		public Entidad devolverEnemigo(ArrayList<Entidad> jugadores,Entidad entidadJugada) {
			int siguienteIndice = (jugadores.indexOf(entidadJugada) + 1) % jugadores.size();
			System.out.println("Se va a atacar el jugador"+jugadores.get(siguienteIndice).getNombre() );
			return jugadores.get(siguienteIndice);
			}
		},DERECHA() {
		@Override
		public Entidad devolverEnemigo(ArrayList<Entidad> jugadores,Entidad entidadJugada) {
			int siguienteIndice = (jugadores.indexOf(entidadJugada) - 1) % jugadores.size();
			siguienteIndice = (siguienteIndice + jugadores.size()) % jugadores.size();
			System.out.println("Se va a atacar el jugador"+jugadores.get(siguienteIndice).	getNombre() );
			return jugadores.get(siguienteIndice);
			}
		},SELECCIONAR_ENEMIGO(){
			
		@Override
		public Entidad devolverEnemigo(ArrayList<Entidad> jugadores,Entidad entidadJugada) {
			// TODO Auto-generated method stub
			return null;
			}
		},GLOBAL() {
	        @Override
	        public Entidad devolverEnemigo(ArrayList<Entidad> jugadores, Entidad entidadJugada) {
	            // Global no elige un enemigo puntual; se maneja aparte
	            return null;
	        }
	    },;	
	public abstract Entidad devolverEnemigo(ArrayList<Entidad> jugadores, Entidad entidadJugada);
}
