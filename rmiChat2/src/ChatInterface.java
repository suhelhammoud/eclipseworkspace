

import java.rmi.*;

public interface ChatInterface extends Remote {
	public Integer sendMsg(String msg)throws RemoteException;
	public String getNewMessages(Integer currentIndex) throws RemoteException;	
}
