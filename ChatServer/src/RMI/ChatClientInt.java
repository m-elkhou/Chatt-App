package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatClientInt extends Remote {
	
	public String getName() throws RemoteException;

	public void sendMessage(ChatClientInt c, String msg) throws RemoteException;

	public void receiveMessage(String ClientName, String msg) throws RemoteException;

	public boolean receiveData(String filename, byte[] data, int len) throws RemoteException;

	public boolean receiveData(String ClientName, String filename, byte[] data, int len) throws RemoteException;

	public void UpdatedListClient() throws RemoteException;
}