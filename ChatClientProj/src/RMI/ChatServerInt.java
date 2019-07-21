package RMI;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface ChatServerInt extends Remote{	
	public boolean login (ChatClientInt a)throws RemoteException ;
	public boolean Exit (ChatClientInt a)throws RemoteException ;
	public void publish (String s)throws RemoteException ;
	public Vector getConnected() throws RemoteException ;
}