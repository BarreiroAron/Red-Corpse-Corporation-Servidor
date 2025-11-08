package redServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HiloServidor extends Thread{
	
	private DatagramSocket conexion;
	private boolean fin = false;
	
	private DireccionRed[] clientes = new DireccionRed[2];
	private int cantClientes = 0 ;
	
	private boolean[] clientesListos = new boolean[2];
	private int cantListos = 0;

	
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
	        if (cantClientes < 2) {

	            clientes[cantClientes] = new DireccionRed(dp.getAddress(), dp.getPort());

	            enviarMensaje("OK", clientes[cantClientes].getIp(), clientes[cantClientes].getPuerto());

	            cantClientes++;

	            // Avisar a todos los que están conectados cuántos jugadores hay
	            for (int i = 0; i < cantClientes; i++) {
	                enviarMensaje("Jugadores:" + cantClientes,
	                        clientes[i].getIp(),
	                        clientes[i].getPuerto());
	            }
	        }

	    } else if (msg.equals("Listo")) {

	        // Ver qué cliente mandó "Listo"
	        for (int i = 0; i < cantClientes; i++) {
	            if (clientes[i].getIp().equals(dp.getAddress())
	                    && clientes[i].getPuerto() == dp.getPort()) {

	                if (!clientesListos[i]) {
	                    clientesListos[i] = true;
	                    cantListos++;
	                    System.out.println("Cliente " + i + " puso LISTO. Total listos: " + cantListos);
	                }
	                break;
	            }
	        }

	        // Si hay 2 conectados y los 2 pusieron listo -> Empieza
	        if (cantClientes == 2 && cantListos == 2) {
	            enviarMensaje("Empieza", clientes[0].getIp(), clientes[0].getPuerto());
	            enviarMensaje("Empieza", clientes[1].getIp(), clientes[1].getPuerto());
	            System.out.println("Todos listos, empieza la partida.");
	        }
	    }
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

