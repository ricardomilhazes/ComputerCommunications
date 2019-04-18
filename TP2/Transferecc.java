import java.io.*;
import java.net.*;

class Transferecc{
	UDPClient cliente;
	//File ficheiro;
	//String filename;

	public Transferecc(){
		cliente = new UDPClient(this);
	}

	public void receiveDatagram(DatagramPacket p){
		
		byte[] dados = p.getData();

		InnetAddress ip = d.getAdress();

	}

}