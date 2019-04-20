import java.net.*;
import java.io.*;

class TransfereccDownload {
	UDPClient cliente;
	InetAddress ipdestino;
	String ficheirodestino;
	byte[] downloadData = new byte[1024]; 
	int n_segmento;



	public TransfereccDownload (UDPClient cliente1, String ipdestino, String ficheiro) throws UnknownHostException {
		cliente=cliente1;
		ipdestino= InetAddress.getByName(ipdestino);
		ficheirodestino=ficheiro;
		n_segmento=0;
	}

	public void recebe (TProto p) {
		System.out.println("Datagram Packet from " + this.ipdestino );
		downloadData[n_segmento]=p;
	}

	public void conectar(){   // falta organizar esta funcao acho
        // envia SYN          
        TProto syn = new TProto(0, 1, false, true, false, false,false,false);
        cliente.send(syn,addressDest,7777);


        // envia ACK
        TProto ack = new TProto(2,1,true,false,false,false,false,false);
        cliente.send(ack,addressDest,7777);
    }
	
}

public void run(){
        try{
          
            conectar();

            File file = new File(filename);

            if (file.createNewFile()){
                System.out.println("Ficheiro criado.");
            } else {
                System.out.println("O ficheiro já existe, a informação vai ser juntada.");
            }

            FileWriter writer = new FileWriter(file);


            // NÃO TENHO A CERTEZA DESTA PARTE!!!
            String dados = new String(downloadData);
            writer.write(dados);

            writer.close();

            System.out.println("Download 100% com sucesso");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
