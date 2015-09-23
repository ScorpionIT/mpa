package mpa.gui.multiplayer.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPSpammer extends Thread {

	private DatagramSocket socket;
	private boolean alive = true;
	private int port;
	private String ip;

	public UDPSpammer(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public UDPSpammer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {

		// Find the server using UDP broadcast
		try {
			// Open a random port to send the package
			socket = new DatagramSocket();
			socket.setBroadcast(true);

			byte[] sendData = new String ("PLAYON:"+ip+":"+port).getBytes();

			// Try the 255.255.255.255 first
			try {
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length,
						InetAddress.getByName("255.255.255.255"), 8888);
				socket.send(sendPacket);
			} catch (Exception e) {
			}

			// Broadcast the message over all the network interfaces
			Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = (NetworkInterface) interfaces
						.nextElement();

				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue; // Don't want to broadcast to the loopback
								// interface
				}

				for (InterfaceAddress interfaceAddress : networkInterface
						.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}

					// Send the broadcast package!
					try {
						while (alive) {
							DatagramPacket sendPacket = new DatagramPacket(
									sendData, sendData.length, broadcast, 8888);
							socket.send(sendPacket);
						}
					} catch (Exception e) {
					}
				}
			}

	
//			// Close the port!
			socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	public void stopSpammer() {
		alive = false;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
}
