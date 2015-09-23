package mpa.gui.multiplayer.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import mpa.gui.multiplayer.ServerSelectionPanel;

public class BroadcastConnectionListener extends Thread {
	private DatagramSocket socket;
	private ServerSelectionPanel serverSelectionPanel;
	private boolean listen = true;
	private boolean alive = true;

	public BroadcastConnectionListener(ServerSelectionPanel serverSelectionPanel) {
		this.serverSelectionPanel = serverSelectionPanel;

	}

	@Override
	public void run() {
		try {
			// Keep a socket open to listen to all the UDP trafic that is
			// destined for this port
			socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
			socket.setBroadcast(true);

			while (alive) {

				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// Receive a packet
				byte[] recvBuf = new byte[15000];
				DatagramPacket packet = new DatagramPacket(recvBuf,
						recvBuf.length);
				socket.receive(packet);

				// Packet received
				System.out.println(getClass().getName()
						+ ">>>Discovery packet received from: "
						+ packet.getAddress().getHostAddress());
				System.out.println(getClass().getName()
						+ ">>>Packet received; data: "
						+ new String(packet.getData()));

				// See if the packet holds the right command (message)
				String message = new String(packet.getData()).trim();
				String[] split = message.split(":");
				
				if (split.length == 3) {
					String ip = split[1];
					String port = split[2];
					serverSelectionPanel.addServer(ip+":"+port);
					System.out.println(ip+ ":"+port);
					
					
				}

			}
			socket.close();	
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void setListenMode(boolean listen) {
		this.listen = listen;
	}

	public void stopListener() {
		alive = false;
	}
}
