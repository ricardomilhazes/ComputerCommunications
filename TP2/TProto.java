import java.io.*;
import java.net.*;

class TProto{
	
	int sequencia, sequencia_confirmacao;
	boolean ack, syn, fin, psh, rst, urg;


	public TProto(int seq, int seq_conf, boolean ack, boolean syn, boolean fin, boolean psh, boolean rst, boolean urg){
		sequencia = seq;
		seq_conf = seq_conf
		ack = ack;
		syn = syn;
		fin = fin;
		psh = psh;
		rst = rst;
		urg = urg;
	}

	public TProto trata(byte[] dados){
		
	}

	public byte[] size() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		return baos.toByteArray();
	}
}