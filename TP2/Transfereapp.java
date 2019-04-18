import java.io.*;
import java.net.*;

class Transfereapp{

	public static void get(String ficheiro, String address) throws Exception{
		File f = new File(ficheiro);
		TransfereccDownload.run();
	}

	public static void put(String ficheiro) throws Exception{
		File f = new File(ficheiro);
		Transferecc

	}

	public static void main(String[] args) {
		if(args[2].equals("get")){
			get(args[3],args[4]);
		}
		if(args[2].equals("put")){
			put(args[3]);
		}
	}
}