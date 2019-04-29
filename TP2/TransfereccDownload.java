import java.net.*;
import java.io.*;
import java.util.*;

class TransfereccDownload extends Thread{
	UDPClient cliente;
	InetAddress ipd;
	String ficheirodestino;
	LinkedList<TProto> downloadData = new LinkedList<>(); 
	int n_segmento;



	public TransfereccDownload (UDPClient cliente1, String ipdestino, String ficheiro) throws UnknownHostException {
		cliente=cliente1;
		ipd= InetAddress.getByName(ipdestino);
		ficheirodestino=ficheiro;
		n_segmento=0;
	}

	public void recebe (TProto p) {
		System.out.println("Datagram Packet from " + this.ipd );
		downloadData.add(p);
	}

    public TProto nextTProto(){
		TProto tp;

    while(downloadData.size()==0){ }
		
    tp = downloadData.removeFirst();

		return tp;
			
	}

	public void conectar() throws Exception{   // falta organizar esta funcao acho
        // envia SYN          
        TProto syn = new TProto(0, 1, 1024,false, true, false, false,false,false,new byte[0]);
        cliente.send(syn,ipd,7777);

       while(true){
        	TProto synack = nextTProto();
        	if(synack.getSyn()==true && synack.getAck() == true){
                n_segmento = 5;
        		break;
        	}
        }

        // envia ACK
        TProto ack = new TProto(2,1,1024,false,true,false,false,false,false,new byte[0]);
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

            System.out.println("yo1");

            int segmento=0;
            while(segmento < n_segmento){
              System.out.println("yo2");
            	TProto tp = nextTProto();
            	String dados = new String(tp.getDados());
            	System.out.println(dados);
            	writer.write(dados);
            	segmento++;

            }
            writer.close();

            System.out.println("Download 100% com sucesso");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
