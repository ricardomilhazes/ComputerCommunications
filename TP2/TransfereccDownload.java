import java.net.*;
import java.io.*;

class TransfereccDownload extends Thread{
	UDPClient cliente;
	InetAddress ipd;
	String ficheirodestino;
	TProto downloadData = new TProto(); 
	int n_segmento;



	public TransfereccDownload (UDPClient cliente1, String ipdestino, String ficheiro) throws UnknownHostException {
		cliente=cliente1;
		ipd= InetAddress.getByName(ipdestino);
		ficheirodestino=ficheiro;
		n_segmento=0;
	}

	public void recebe (TProto p) {
		System.out.println("Datagram Packet from " + this.ipd );
		downloadData=p;
	}

	public void conectar() throws Exception{   // falta organizar esta funcao acho
        // envia SYN          
        TProto syn = new TProto(0, 1, false, true, false, false,false,false,new byte[0]);
        cliente.send(syn,ipd,7777);


        // envia ACK
        TProto ack = new TProto(2,1,true,false,false,false,false,false,new byte[0]);
        cliente.send(ack,ipd,7777);
    }

public void run(){
        try{
          
            conectar();

            File file = new File(ficheirodestino);

            if (file.createNewFile()){
                System.out.println("Ficheiro criado.");
            } else {
                System.out.println("O ficheiro já existe, a informação vai ser juntada.");
            }

            FileWriter writer = new FileWriter(file);


            // NÃO TENHO A CERTEZA DESTA PARTE!!!
            String dados = new String(downloadData.getDados());
            writer.write(dados);

            writer.close();

            System.out.println("Download 100% com sucesso");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
