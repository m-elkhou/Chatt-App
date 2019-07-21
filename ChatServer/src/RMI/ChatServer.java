package RMI;

import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ChatServer extends UnicastRemoteObject implements ChatServerInt {
	private static final long serialVersionUID = 1L;
	
	private Vector<ChatClientInt> listClient = new Vector<>();
	public Map<String, Integer> indexClient = new HashMap<>();
	private String file = "";

	public ChatServer() throws RemoteException {
		super();
	}

	@Override
	public boolean login(ChatClientInt a) throws RemoteException {
		System.out.println("* [ " + a.getName() + " ] ==> got connected....");
//		a.tell("You have Connected successfully.");
		publish("* [ " + a.getName() + " ] ==> has just connected.");

		indexClient.put(a.getName(), listClient.size());
		listClient.add(a);
		ClientNotify();
		return true;
	}

	@Override
	public boolean Exit(ChatClientInt a) throws RemoteException {
		System.out.println("--- [ " + a.getName() + " ] ==> got DesConnected....");
		listClient.remove(a);
		ReIndex();

		ClientNotify();
		return true;
	}
	
	private void ClientNotify() throws RemoteException {
		if (listClient != null && listClient.size() != 0)
			for (ChatClientInt c : listClient)
				c.UpdatedListClient();
	}
	
	private void ReIndex() {
		indexClient = new HashMap<>();
		for (int i = 0; i < listClient.size(); i++) {
			try {
				ChatClientInt c = listClient.get(i);
				if (c == null) {
					listClient.remove(i);
					ReIndex();
					return;
				}
				indexClient.put(c.getName(), i);
			} catch (Exception e) {
				listClient.remove(i);
				ReIndex();
				return;
			}
		}

	}

	@Override
	public void publish(String s) throws RemoteException {
		System.out.println(s);
		for (int i = 0; i < listClient.size(); i++) {
			try {
				ChatClientInt tmp = (ChatClientInt) listClient.get(i);
//				tmp.sendMessage(s);
			} catch (Exception e) {
				// problem with the client not connected.
				// Better to remove it
			}
		}
	}

	@Override
	public Vector<ChatClientInt> getConnected() throws RemoteException {
		return listClient;
	}

	@SuppressWarnings("resource")
	public void sendFile(ChatClientInt c) {
		// Sending The File...
		try {
			File f1 = new File(file);
			FileInputStream in = new FileInputStream(f1);
			byte[] mydata = new byte[1024 * 1024];
			int mylen = in.read(mydata);
			while (mylen > 0) {
				c.receiveData(f1.getName(), mydata, mylen);
				mylen = in.read(mydata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
