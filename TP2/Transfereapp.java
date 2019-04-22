import java.io.*;
import java.net.*;

class Transfereapp{

	public static void get(String ficheiro, String address) throws Exception{
		try{

			new Thread(new Transferecc(ficheiro,address)).run();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void put(String ficheiro) throws Exception{
		try{
			File f = new File(ficheiro);
			if(f.exists())
				new Thread(new Transferecc(f)).run();
			else
				System.out.println("Ficheiro inexistente");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception{
		try{
			if(args[0].equals("get")){
				get(args[1],args[2]);
			}
			if(args[0].equals("put")){
				put(args[1]);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}