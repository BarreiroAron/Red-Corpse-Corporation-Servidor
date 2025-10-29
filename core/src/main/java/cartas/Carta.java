	package cartas;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

import Entidades.Entidad;
import Utiles.Imagen;
import Utiles.Recursos;
import juegos.ControladorDeJuego;
import juegos.Juego;

public abstract class Carta {
		
		private int puntosAumentadosRival;
		private int puntosDisminuidos;
		private boolean porcentual;
		private TipoDeCarta tipo;
		private Habilidad habilidad;
		private Imagen imagenCarta;
		private Imagen imagenEspalda= new Imagen(Recursos.CARTA_ESPALDA);
		private int x;
		private int y;
		private String descripcion;
		private EnemigoDeterminado enemigoDeterminado= EnemigoDeterminado.IZQUIERDA; ///provisoprio para ver si funciona
		
		
		
		public Carta(int puntosAumentadosRival, int puntosDisminuidos, Habilidad habilidad,Imagen texturaCarta, boolean porcentual, int x, int y, String descripcion,TipoDeCarta tipo) {
			this.puntosAumentadosRival = puntosAumentadosRival;
			this.puntosDisminuidos = puntosDisminuidos;
			this.habilidad = habilidad;
			this.imagenCarta = texturaCarta;
			this.porcentual = porcentual;
			this.x = x;
			this.y = y;
			this.descripcion = descripcion;
			this.tipo = tipo;
		}
		
		@Override
		public String toString() {
			return "Carta [habilidad=" + habilidad + ", puntosAumentadosRival=" + puntosAumentadosRival
					+ ", puntosDisminuidos=" + puntosDisminuidos + ", texturaCarta="
					+ imagenCarta + ", texturaCartaEspalda=" + imagenEspalda + "]";
		}
		
		public EnemigoDeterminado getEnemigoDeterminadoEnum() {
	        return enemigoDeterminado;
	    }

	    public void setEnemigoDeterminado(EnemigoDeterminado ed) {
	        this.enemigoDeterminado = ed;
	    }
		
		public int getPuntosAumentadosRival() {
			return puntosAumentadosRival;
		}

		public void setPuntosAumentadosRival(int puntosAumentadosRival) {
			this.puntosAumentadosRival = puntosAumentadosRival;
		}

		public int getPuntosDisminuidos() {
			return puntosDisminuidos;
		}

		public void setPuntosDisminuidos(int puntosDisminuidos) {
			this.puntosDisminuidos = puntosDisminuidos;
		}

		public Habilidad getHabilidad() {
			return habilidad;
		}

		public void setHabilidad(Habilidad habilidad) {
			this.habilidad = habilidad;
		}

		public Imagen getTexturaCarta() {
			return imagenCarta;
		}
		
		public void setTexturaCarta(Imagen texturaCarta) {
			this.imagenCarta = texturaCarta;
		}

		public Imagen getTexturaCartaEspalda() {
			return imagenEspalda;
		}

		public boolean isPorcentual() {
			return porcentual;
		}

		public void setPorcentual(boolean porcentual) {
			this.porcentual = porcentual;
		}
		
		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public Imagen getImagenCarta() {
			return imagenCarta;
		}
		
		public String getDescripcion() {
			return descripcion;
		}

		public Entidad getEnemigoDeterminado(ArrayList<Entidad> jugadores, Entidad entidadJugada) {
			return enemigoDeterminado.devolverEnemigo(jugadores,entidadJugada);
		}

		public boolean getPorcentual() {
			return porcentual;
		}

		public TipoDeCarta getTipo() {
			return this.tipo;
		}
		
	}
