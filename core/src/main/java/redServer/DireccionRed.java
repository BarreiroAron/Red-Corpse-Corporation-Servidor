package redServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DireccionRed {

	private InetAddress ip;
	private int puerto;
	
	public DireccionRed(InetAddress ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}
	
}
