import java.io.*;
import java.net.*;

class UDPClient{
	Transferecc cc;
	DatagramSocket clientSocket;
	byte[] receiveData = new byte[1024];

    public UDPClient() throws Exception{
    	cc = new Transferecc();
    	clientSocket = new DatagramSocket();
    }

    public void send(TProto fragmento, InetAddress IPAddress, int port) throws Exception{
	    byte[] sendData = fragmento.size();
	    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    clientSocket.send(sendPacket);
    }

   public void run(){
   		try{
   		while(true){
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			cc.receiveDatagram(receivePacket);
		}
   		} catch(Exception e) {
   		e.printStackTrace();
   		} finally {
   		clientSocket.close();
   		}
   	}
}