import java.net.*;
import java.io.*;

class TransfereccUpload {

	UDPClient cliente;
	Transferecc cc;
	InetAddress enddestino;
	File ficheiro;
	FileInputStream fis;
	int max;
	int n_segmento;
	byte[] uploadData = new byte[1024];


	public TransfereCCUpload(UDPClient client, Transferecc transferecc,InetAddress ipdest, File f) throws UnknownHostException, IOException{
        cliente = client;
        cc = transferecc;
        enddestino = ipdest;
        ficheiro = f;
        // Creates a FileInputStream by opening a connection to an actual file, the file named by the File object file in the file system.
        fis = new FileInputStream(f);
        max = 1024;
        n=0;
    }

    public void recebe (TProto p) {
    	uploadData[n]=p;
    }
	
}