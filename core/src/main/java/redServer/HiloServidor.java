package redServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import Entidades.CuerpoAnimado;
import Entidades.Entidad;
import Entidades.Jugador;
import Utiles.Headless;
import cartas.Carta;
import juegos.Juego;

public class HiloServidor extends Thread{
	
	private DatagramSocket conexion;
	private boolean fin = false;
	
	private DireccionRed[] clientes = new DireccionRed[4];
	private int cantClientes = 0 ;
	
	private boolean[] clientesListos = new boolean[4];
	private int cantListos = 0;
	
    private Juego juegoServidor;
	
	private boolean partidaIniciada = false;
	
	private int puntosDeVidaJugadores=100;

	
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
	    
	    String[] personajesIds = Utiles.Util.crearListaIdPerRan();
	    
	    System.out.println("Creando identidades");
	    
	    for(int i=0 ; i<cantClientes ;i++) {
	    	Entidad entidad = new Jugador("Entidad "+ i, 100, null);
	    	entidad.setIdPersonaje(personajesIds[i]);
	    	jugadores.add(entidad);
	    }

	    juegoServidor = new Juego(jugadores);

	    System.out.println("[SERVIDOR] Juego creado. Mazo iniciado y cartas repartidas.");

	    // Mandar a cada cliente su entidad y su mano inicial
	    for (int i = 0; i < cantClientes; i++) {
	    	enviarDatosJugadoresAlCliente(i);
	        enviarDatosInicialesAlCliente(i);
	    }
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



	public boolean getPartidaIniciada() {
		return this.partidaIniciada;
	}
	
	public void cerrarConexion() {
	    if (conexion != null && !conexion.isClosed()) {
	        conexion.close();
	        System.out.println("Socket cerrado correctamente");
	    }
	}
	
	public int getCantClientes() {
		return this.cantClientes;
	}
}

