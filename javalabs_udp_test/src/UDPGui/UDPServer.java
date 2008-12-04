package UDPGui;

//UDP Server
//Usage: java UDPServer [server port]
import java.net.*;

public class UDPServer {
	
	public void run( int serverPort, int packetSize)throws Exception{
		
		DatagramSocket serverSocket = new DatagramSocket (serverPort);
		byte[] receiveData = new byte[packetSize];
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			//process data
			String sentence = new String(receivePacket.getData());
			
			System.out.println(sentence);
		
			
		}
	}
	public static void main(String[] args) throws Exception{
	 new UDPServer().run(7777,1024);
		 // end of while
	} // end of main
} // end of UDPServer
