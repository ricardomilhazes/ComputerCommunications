import java.io.*;
import java.net.*;

class UDPClient extends Thread{
	Transferecc cc;
	DatagramSocket clientSocket;
	byte[] receiveData = new byte[1024];

    public UDPClient(Transferecc tcc) throws Exception{
    	cc = tcc;
    	clientSocket = new DatagramSocket(7777);
    }

    public void send(TProto fragmento, InetAddress IPAddress, int port) throws Exception{
	    byte[] sendData = fragmento.fromTProto();
	    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
	    clientSocket.send(sendPacket);
    }

    void closeClient(){
    	clientSocket.close();
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