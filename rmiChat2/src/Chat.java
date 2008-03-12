
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;

import javax.swing.JTextArea;


public class Chat extends UnicastRemoteObject implements ChatInterface {

	JTextArea txtRes;
	private ArrayList<String> messages_queu=new ArrayList<String>();

	public Chat()throws RemoteException {
		super();
	}
	public Integer sendMsg(String msg) throws RemoteException {
		messages_queu.add(msg);
		System.out.println(msg);
		return messages_queu.size() ;

	}

	public String getNewMessages(Integer index_last_message) throws RemoteException {
		int i=index_last_message.intValue();
		if(i<messages_queu.size()){
			return messages_queu.get(i);
		}else
			return "no_new_messages";
	}

}
