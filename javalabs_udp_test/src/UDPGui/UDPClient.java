package UDPGui;

//Usage: java UDPClient [server addr] [server port]
import java.io.*;
import java.net.*;
public class UDPClient {

	public DatagramSocket sendFile(String fileName, String serverName, int serverPort, int packetSize)
	throws Exception{

		InetAddress serverIPAddress = InetAddress.getByName(serverName);
		// get server port

		// create socket
		DatagramSocket clientSocket = new DatagramSocket ();
		// get input from keyboard
		byte[] sendData = new byte[packetSize];

		FileInputStream fin= new FileInputStream(fileName);


		char[] arrChar=new char[1024];
		while( fin.read(sendData) >0){
		
			System.out.println(new String(sendData));
			
			DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length, serverIPAddress, serverPort);
			clientSocket.send(sendPacket);
		}
		//close client socket
		clientSocket.close();
		return clientSocket;
	}

	public static void main(String[] args)throws Exception {
		new UDPClient().sendFile("d://test.txt", "localhost", 7777, 1024);
	} // end of main
} // end of UDPClient

