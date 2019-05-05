import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.*;

class Transferecc extends Thread{
	UDPClient cliente;
	boolean upload;
	boolean download;
	String filename;
	File f;
	String IPdestino;
	Map<InetAddress,TransfereccUpload> threads_upload = new HashMap<>();
	final Lock l = new ReentrantLock();
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
		this.cliente = new UDPClient(this);
		this.upload=false;
		this.download=true;
		this.f=null;
		this.filename=ficheiro;
		IPdestino=address;
	}

	public Object toTProto(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}

	public void receiveDatagram(DatagramPacket p){
		
		byte[] dados = p.getData();

		InetAddress ip = p.getAddress();

		try{

    TProto tp =	(TProto) toTProto(dados);

		if(this.upload == true){

			TransfereccUpload tup = threads_upload.get(ip);

			if (tup == null){
                
    		TransfereccUpload ntup = new TransfereccUpload(cliente,this,ip,this.f);
				new Thread(ntup).start();

				l.lock();
    		    threads_upload.put(ip,ntup);
   			    l.unlock();

				ntup.recebe(tp);
			} else{
				tup.recebe(tp);
			}
		} else{
			tfd.recebe(tp);
		 }

		} catch(Exception e){
			e.printStackTrace();
		}

	}

	public void desconectar(InetAddress endereco) {
		try {
			l.lock();
			threads_upload.remove(endereco);
			l.unlock();
			this.download=false;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run(){
		while(this.download == true){
			try{
				Thread client = new Thread(cliente);
				client.start();

			
				tfd = new TransfereccDownload(cliente,IPdestino,filename);
				new Thread(tfd).run();
			}
		} catch(UnknownHostException e){
			e.printStackTrace();
		}
	}

}
