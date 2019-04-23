import java.io.*;
import java.net.*;

class Transferecc extends Thread{
	UDPClient cliente;
	boolean upload;
	boolean download;
	String filename;
	File f;
	String IPdestino;
	TransfereccDownload tfd;

	public Transferecc(File fich) throws SocketException,Exception{
		cliente = new UDPClient(this);
		this.upload=true;
		this.download=false;
		this.f = fich;
		this.filename=fich.getName();
		IPdestino="";
	}

	public Transferecc(String ficheiro, String address) throws SocketException,Exception{
		cliente = new UDPClient(this);
		this.upload=false;
		this.download=true;
		this.filename=ficheiro;
		IPdestino=address;
	}

	public Object toTProto(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}

	public void receiveDatagram(DatagramPacket p){
		
		byte[] dados = p.getDados();

		InetAddress ip = p.getAddress();

		try{

			TProto tp =	(TProto) toTProto(dados);

			if(this.upload == true){

				TransfereccUpload tup;
				tup = new TransfereccUpload(cliente,this,ip,this.f);

				new Thread(tup).start();

				tup.recebe(tp);
			}

			else{
				tfd.recebe(tp);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	public void run(){
		try{
			Thread client = new Thread(cliente);
			client.start();

			if(this.download == true){
				tfd = new TransfereccDownload(cliente,IPdestino,filename);
				new Thread(tfd).run();
			}
		} catch(UnknownHostException e){
			e.printStackTrace();
		}
	}

}
