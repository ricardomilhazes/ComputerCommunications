import java.io.*;
import java.net.*;

class Transferecc{
	UDPClient cliente;
	boolean upload;
	boolean download;
	String filename;
	File f;
	String IPdestino;
	TransfereccUpload tfu;
	TransfereccDownload tfd;

	public Transferecc(File fich){
		cliente = new UDPClient(this);
		this.upload=true;
		this.download=false;
		this.f = fich;
		this.filename=fich.getName();
		this.IPdestino="";
	}

	public Transferecc(String ficheiro, String address){
		cliente = new UDPClient(this);
		this.upload=false;
		this.download=true;
		this.filename=ficheiro;
		this.IPdestino=address;
	}

	public Object toTProto(byte[] data){
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}

	public void receiveDatagram(DatagramPacket p){
		
		byte[] dados = p.getData();

		InnetAddress ip = p.getAdress();

	}

}