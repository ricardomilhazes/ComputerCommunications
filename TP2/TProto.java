import java.io.*;
import java.net.*;

class TProto implements Serializable{
	
	int sequencia, sequencia_confirmacao,mss;
	boolean ack, syn, fin, psh, rst, urg;
	byte[] dados;
	byte checksum;
	int segmentos;


	public TProto(int seq, int seq_conf,int mss1, boolean ack, boolean syn, boolean fin, boolean psh, boolean rst, boolean urg, byte[] d){
		this.sequencia = seq;
		this.sequencia_confirmacao = seq_conf;
		this.mss=mss1;
		this.ack = ack;
		this.syn = syn;
		this.fin = fin;
		this.psh = psh;
		this.rst = rst;
		this.urg = urg;
		this.dados = d;
	}

	public int getSequencia(){ 
		return this.sequencia;
	}

	public int getConfirmacao(){
		return this.sequencia_confirmacao;
	}

	public int getMSS() {
		return this.mss;
	}

	public Boolean getAck(){
		return this.ack;
	}

	public Boolean getSyn(){
		return this.syn;
	}

	public Boolean getFin(){
		return this.fin;
	}

	public Boolean getPsh(){
		return this.psh;
	}

	public Boolean getRst(){
		return this.rst;
	}

	public Boolean getUrg(){
		return this.urg;
	}

	public byte[] getDados(){
		return this.dados;
	}

	public byte getChecksum() {
		return this.checksum;
	}

	public int getSegmentos(){
		return this.segmentos;
	}

	public static final byte calculaChecksum (byte[] data) {
  		byte sum = 0;
		for (byte b : data) {
    		  sum ^= b;
  		}
 	  return sum;
	}

	public void setChecksum (byte checksum1) {
		this.checksum= checksum1;

	}

	public void setSegmentos(int segmentos1){
		this.segmentos=segmentos1;
	}

	public byte[] fromTProto() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(this);
		return baos.toByteArray();
	}
}
