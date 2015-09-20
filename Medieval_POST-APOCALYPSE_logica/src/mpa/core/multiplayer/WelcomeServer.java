package mpa.core.multiplayer;

import java.net.InetAddress;
import java.util.Map;

public class WelcomeServer extends Thread
{
	private int portNumber = 5000;
	private Map<InetAddress, String> humanPlayers;

}
