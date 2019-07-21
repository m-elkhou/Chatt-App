package RMI;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("deprecation")
public class Main {
	
	public static void main(String[] args) {
		try {
			String ip = InetAddress.getLocalHost().getHostAddress();
			int port = 1099;

			System.out.println("Saisier l'ip de Host /ou/ Cleck Entre <--|:");
			Scanner s = new Scanner(System.in);
			ip = s.nextLine();
			if (ip == null || ip.equals("") )
				ip = InetAddress.getLocalHost().getHostAddress();

			System.out.println("My ip is : " + ip);
			System.out.println("Port :  : " + port);

//			System.setSecurityManager(new RMISecurityManager());
			java.rmi.registry.LocateRegistry.createRegistry(port);

			ChatServer b = new ChatServer();
			Naming.rebind("rmi://" + ip + "/chat", b);
			System.out.println("[System] Chat Server is ready.");
			System.err.println("Server ready");
			
		} catch (Exception e) {
			System.out.println("Chat Server failed: " + e);
		}
	}
}