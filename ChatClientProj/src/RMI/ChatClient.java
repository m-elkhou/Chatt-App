package RMI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import View.ChatPan;
import View.DiscussionPan;
import View.Manager;

public class ChatClient extends UnicastRemoteObject implements ChatClientInt {
	private static final long serialVersionUID = 1L;
	private String name;
	private DiscussionPan discussionPan;

	public ChatClient(String n) throws RemoteException {
		super();
		name = n;
	}

	@Override
	public void sendMessage(ChatClientInt c,String msg) throws RemoteException {
		c.receiveMessage(name, msg);
	}

	@Override
	public void receiveMessage(String ClientName, String msg) throws RemoteException {
		int index = discussionPan.indexClient.get(ClientName);
		discussionPan.tabbeClient.setSelectedIndex(index);
		ChatPan chatPan = (ChatPan) discussionPan.tabbeClient.getComponent(index);
		chatPan.sendMessage(msg,false);
		Manager.writeMsg(2,"Chat/"+name+"/"+ClientName,msg);
	}
	
	@Override
	public String getName() throws RemoteException {
		return name;
	}

	public void setDiscussionPan(DiscussionPan d) {
		discussionPan = d;
	}
	
	@Override
	public void UpdatedListClient() throws RemoteException {
		discussionPan.updateUsers();
	}
	
	@Override
	public boolean receiveData(String filePath, byte[] data, int len) throws RemoteException {
		try {
			File f = new File(filePath);
			f.createNewFile();
			FileOutputStream out = new FileOutputStream(f, true);
			out.write(data, 0, len);
			out.flush();
			out.close();
			System.out.println("Done writing data...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean receiveData(String ClientName, String filename, byte[] data, int len) throws RemoteException {
		String path = Manager.path+"\\Chat\\"+name+"\\"+filename;
		
		if (receiveData(path, data, len)) {
			int index = discussionPan.indexClient.get(ClientName);
			discussionPan.tabbeClient.setSelectedIndex(index);
			ChatPan chatPan = (ChatPan) discussionPan.tabbeClient.getComponent(index);
			chatPan.sendFile(new File(path),false);
			Manager.writeMsg(4,"Chat/"+name+"/"+ClientName,path);
			return true;
		}
		return false;
	}

	public void sendFile(ChatClientInt c, File f) {
		// Sending The File...
		try {
			FileInputStream in = new FileInputStream(f);
			byte[] mydata = new byte[1024 * 1024];
			int mylen = in.read(mydata);
			while (mylen > 0) {
				c.receiveData(name, f.getName(), mydata, mylen);
				mylen = in.read(mydata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(ArrayList<ChatClientInt> list, File file) {
		for (int i = 0; i < list.size(); i++) {
			try {
				ChatClientInt c = (ChatClientInt) list.get(i);
				sendFile(c, file);
			} catch (Exception e) {
				// problem with the client not connected.
				// Better to remove it
			}
		}
	}

	public void sendData(ArrayList<String> list, Vector<ChatClientInt> v, File file) {
		for (int i = 0; i < v.size(); i++) {
			try {
				for (String ls : list) {
					if (ls.equals(v.get(i).getName()))
						sendFile(v.get(i), file);
				}
			} catch (Exception e) {
				// problem with the client not connected.
				// Better to remove it
			}
		}
	}

	public void sendData(String clientName, File file) {
		Vector<ChatClientInt> v = new Vector<>();
		
		for (int i = 0; i < v.size(); i++) {
			try {
				if (v.get(i).getName().equals(clientName))
					sendFile(v.get(i), file);
			} catch (Exception e) {
				// problem with the client not connected.
			}
		}
	}
	
}
