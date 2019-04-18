import java.io.*;
import java.net.*;

class UDPClient{
	Transferecc cc;
	DatagramSocket clientSocket;
    byte[] sendData = new byte[1024];
    byte[] receiveData = new byte[1024];

    public UDPClient() throws Exception{
    	cc = new Transferecc();
    	clientSocket = new DatagramSocket();
    }

    public void send(String sentence, InetAddress IPAddress) throws Exception{
  
	    sendData = sentence.getBytes();
	    IPAddress = InetAddress.getByName("localhost");
	    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
	    clientSocket.send(sendPacket);
    }

   public void run(){
   		try{
   			while(true){
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			    clientSocket.receive(receivePacket);
			    String modifiedSentence = new String(receivePacket.getData());
			    System.out.println("FROM SERVER:" + modifiedSentence);
			}
   		} catch(Exception e) {
   		e.printStackTrace();
   		} finally {
   		clientSocket.close();
   		}
   	}
}