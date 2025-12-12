package redServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import com.badlogic.gdx.Game;

import Entidades.CuerpoAnimado;
import Entidades.Entidad;
import Entidades.Jugador;
import Pantallas.JuegoPantalla;
import Utiles.Headless;
import cartas.Carta;
import juegos.Juego;

public class HiloServidor extends Thread implements ServidorAPI{
	
	private DatagramSocket conexion;
	private boolean fin = false;
	
	private DireccionRed[] clientes = new DireccionRed[4];
	private int cantClientes = 0 ;
	
	private int turnoActual = 0;
	
	private boolean[] clientesListos = new boolean[4];
	private int cantListos = 0;
	
	private Game game;
	
    private Juego juegoServidor;
	
	private boolean partidaIniciada = false;
	
	private int puntosDeVidaJugadores=100;

	private Thread hiloPartida;
	private volatile boolean partidaCorriendo = false;

	
	public HiloServidor() {
		System.out.println("Creando HiloServidor...");
		try {
			conexion = new DatagramSocket(8999);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	} 
	
	public void enviarMensaje(String msg, InetAddress ip , int puerto) {
		byte[] data=  msg.getBytes();
		DatagramPacket dp = new DatagramPacket(data, data.length,ip,puerto);
		try {
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override 
	public void run(){
		do {
			System.out.println("esperando mensaje");
			byte [] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data,data.length);
			try {
				conexion.receive(dp);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			procesarMensaje(dp);
		}while(!fin);
	}

	private void procesarMensaje(DatagramPacket dp) {
	    String msg = new String(dp.getData(), 0, dp.getLength()).trim();

	    if (msg.equals("Conexion")) {
	        System.out.println("Se conecto uno al server");

	            clientes[cantClientes] = new DireccionRed(dp.getAddress(), dp.getPort());

	            enviarMensaje("OK",
	                    clientes[cantClientes].getIp(),
	                    clientes[cantClientes].getPuerto());

	            cantClientes++;
	            System.out.println("Total de clientes conectados: " + cantClientes);

	    } else if (msg.equals("Listo")) {
	        // 🔹 Alguien apretó LISTO en la SalaDeEspera / Juego

	        int indice = buscarIndiceCliente(dp.getAddress(), dp.getPort());
	        if (indice != -1 && !clientesListos[indice]) {
	            clientesListos[indice] = true;
	            cantListos++;
	            System.out.println("Cliente " + indice + " está LISTO. Total listos: " + cantListos);
	        }

	        // Cuando los 2 están conectados y listos → creamos la partida
	        if (cantClientes >= 2 && cantListos >= cantClientes) {
	            System.out.println("Ambos clientes conectados y listos. Iniciando partida...");
	            iniciarPartidaEnServidor();

	            // Avisar a los clientes que la partida arranca (para que cambien de pantalla)
	            for(int i=0 ; i<cantListos ;i++) {
	            	enviarMensaje("Empieza", clientes[i].getIp(), clientes[i].getPuerto());
	    	    }
	        }
	    }else if (msg.startsWith("PLAY;")) {
	        int idx = buscarIndiceCliente(dp.getAddress(), dp.getPort());
	        if (idx == -1) return;

	        if (!partidaIniciada) {
	            System.out.println("[SERVIDOR] PLAY recibido pero la partida no está iniciada.");
	            return;
	        }

	        // Solo el jugador de turno puede jugar
	        if (!esTurnoDe(idx)) {
	            System.out.println("[SERVIDOR] PLAY ignorado, no es turno del jugador " + idx);
	            return;
	        }

	        String[] partes = msg.split(";");
	        if (partes.length < 2) return;

	        int indiceCarta;
	        try {
	            indiceCarta = Integer.parseInt(partes[1]);
	        } catch (NumberFormatException e) {
	            return;
	        }

	        // Obtenemos el jugador y su mano en el juego del servidor
	        Entidad jugador = juegoServidor.getJugadores().get(idx);

	        if (indiceCarta < 0 || indiceCarta >= jugador.getMano().size()) {
	            System.out.println("[SERVIDOR] PLAY índice de carta inválido: " + indiceCarta);
	            return;
	        }

	        Carta carta = jugador.getMano().get(indiceCarta);

	        System.out.println("[SERVIDOR] Jugador " + idx + " jugó carta índice " + indiceCarta
	                + " (id=" + carta.getId() + ")");

	        juegoServidor.jugarCartaConDelay(jugador.getMano().get(indiceCarta),jugador);

	    }

	}

	
	private int buscarIndiceCliente(InetAddress ip, int puerto) {
	    for (int i = 0; i < cantClientes; i++) {
	        if (clientes[i].getIp().equals(ip) && clientes[i].getPuerto() == puerto) {
	            return i;
	        }
	    }
	    return -1;
	}
	
	private void iniciarPartidaEnServidor() {
		
		Headless.activar(); 

	    ArrayList<Entidad> jugadores = new ArrayList<>();
	    
	    turnoActual = 0;
	    
	    String[] personajesIds = Utiles.Util.crearListaIdPerRan();
	    
	    System.out.println("Creando identidades");
	    
	    for(int i=0 ; i<cantClientes ;i++) {
	    	Entidad entidad = new Jugador("Entidad "+ i, 100, null);
	    	entidad.setIdPersonaje(personajesIds[i]);
	    	jugadores.add(entidad);
	    }

	    juegoServidor = new Juego(jugadores);
	    juegoServidor.setServidorAPI(this);
	    
	   /* Gdx.app.postRunnable(new Runnable() {
	        @Override
	        public void run() {
	            game.setScreen(new JuegoPantalla(game, juegoServidor));
	        }
	    });*/

	    System.out.println("[SERVIDOR] Juego creado. Mazo iniciado y cartas repartidas.");
	   
	    partidaCorriendo = true;
	    hiloPartida = new Thread(() -> loopPartida());
	    hiloPartida.start();
	    
	    // Mandar a cada cliente su entidad y su mano inicial
	    for (int i = 0; i < cantClientes; i++) {
	    	enviarDatosJugadoresAlCliente(i);
	        enviarDatosInicialesAlCliente(i);
	    }
	    enviarTurnoAClientes();
	    partidaIniciada = true;

	}
	
	public void avanzarTurno() {
	    turnoActual = (turnoActual + 1) % cantClientes; // alterna 0 ↔ 1
	    enviarTurnoAClientes();
	}
	
	public void enviarTurnoAClientes() {
	    String msg = "TURN;" + turnoActual;
	    for (int i = 0; i < cantClientes; i++) {
	        enviarMensaje(msg, clientes[i].getIp(), clientes[i].getPuerto());
	    }
	    System.out.println("[SERVIDOR] Turno enviado a clientes: " + turnoActual);
	}
	
	private void enviarDatosInicialesAlCliente(int indiceCliente) {
	    Entidad jugador = juegoServidor.getJugador(indiceCliente);

	    String nombreEntidad = jugador.getNombre();
	    String manoSerializada = serializarMano(jugador);
	    
	    
	    // Formato:
	    // INIT;playerIndex;nombreEntidad;HAB1,HAB2,HAB3
	    String mensaje = "INIT;" + indiceCliente + ";" + nombreEntidad + ";" + manoSerializada;

	    System.out.println("[SERVIDOR] Enviando INIT a cliente " + indiceCliente + ": " + mensaje);

	    enviarMensaje(mensaje,
	            clientes[indiceCliente].getIp(),
	            clientes[indiceCliente].getPuerto());
	}
	
	private void enviarDatosJugadoresAlCliente(int indiceCliente) {
		ArrayList<Entidad> jugadores = juegoServidor.getJugadores();
		String mensaje = "JUGADORES;" + jugadores.size();
		
		for(int i=0; i<jugadores.size(); i++ ) {
			
			String nombreEntidad = jugadores.get(i).getNombre();
			int puntos = this.puntosDeVidaJugadores;
			String personaje = jugadores.get(i).getIdPersonaje();
			
			mensaje = mensaje +";"  + nombreEntidad + ";" + puntos + ";" + personaje;
			
		}
		
		System.out.println("[SERVIDOR] Enviando jugadores a cliente " + indiceCliente + ": " + mensaje);
		
		enviarMensaje(mensaje,
	            clientes[indiceCliente].getIp(),
	            clientes[indiceCliente].getPuerto());
	}

	private String serializarMano(Entidad jugador) {
	    StringBuilder sb = new StringBuilder();
	    for (Carta c : jugador.getMano()) {
	        if (sb.length() > 0) sb.append(",");

	        sb.append(c.getId());
	    }
	    return sb.toString();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public boolean getPartidaIniciada() {
		return this.partidaIniciada;
	}
	
	public void cerrarConexion() {
	    if (conexion != null && !conexion.isClosed()) {
	        conexion.close();
	        System.out.println("Socket cerrado correctamente");
	    }
	}
	
	private boolean esTurnoDe(int idx) {
	    return idx == turnoActual;
	}
	
	public int getCantClientes() {
		return this.cantClientes;
	}

	@Override
	public void enviarMesaActualAJugadores(int idxJugador, Carta cartaEnMesa) {
	    if (cartaEnMesa == null) return;

	    String cartaId = cartaEnMesa.getId();
	    String msgMesa = "MESA;" + idxJugador + ";" + cartaId;

	    for (int i = 0; i < cantClientes; i++) {
	        enviarMensaje(msgMesa, clientes[i].getIp(), clientes[i].getPuerto());
	    }

	    System.out.println("[SERVIDOR] Enviado a clientes: " + msgMesa);
	}

	@Override
	public void enviarCartaATodos(Carta carta) {
	    if (carta == null) return;

	    String cartaId = carta.getId();
	    String msg = "CARTA_GLOBAL;" + cartaId;

	    for (int i = 0; i < cantClientes; i++) {
	        enviarMensaje(msg, clientes[i].getIp(), clientes[i].getPuerto());
	    }

	    System.out.println("[SERVIDOR] CARTA_GLOBAL enviada a todos: " + cartaId);
	}


	private void loopPartida() {
	    long ultimoTiempo = System.nanoTime();

	    while (partidaCorriendo) {
	        long ahora = System.nanoTime();
	        float delta = (ahora - ultimoTiempo) / 1_000_000_000f;
	        ultimoTiempo = ahora;

	        //  Avanzar logica del juego del SERVIDOR
	        if (juegoServidor != null) {
	            juegoServidor.actualizar();
	        }

	        try {
	            Thread.sleep(16); // un "descanso"
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            break;
	        }
	    }

	    System.out.println("[SERVIDOR] Loop de partida detenido.");
	}

	public void notificarFinPartida() {
	    partidaCorriendo = false;
	    /* en Juego cuando termine:
	    if (servidorAPI != null) {
	        servidorAPI.notificarFinPartida();
	    }*/
	}

	@Override
	public void enviarModificacionDePuntos(Entidad objetivo, int puntos, boolean esPorcentual) {
	    try {
	        int indiceJugador = juegoServidor.getJugadores().indexOf(objetivo); // ← ESTE es el índice real en la partida

	        if (indiceJugador == -1) {
	            System.out.println("[SERVIDOR] ERROR: no se encontró el índice del jugador.");
	            return;
	        }

	        // Formato:
	        // PUNTOS;indice;puntos;esPorcentual
	        String msg = "PUNTOS;" + indiceJugador + ";" + puntos + ";" + (esPorcentual ? 1 : 0);

	        // Enviar a todos
	        for (int i = 0; i < cantClientes; i++) {
	            enviarMensaje(msg, clientes[i].getIp(), clientes[i].getPuerto());
	        }

	        System.out.println("[SERVIDOR] Enviando modificación de puntos: " + msg);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}

