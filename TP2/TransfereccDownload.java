import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

class TransfereccDownload extends Thread{
	UDPClient cliente;
	InetAddress ipd;
	String ficheirodestino;
	LinkedList<TProto> downloadData = new LinkedList<>(); 
	int n_segmento;
    final Lock l = new ReentrantLock();
    final Condition empty  = l.newCondition();


	public TransfereccDownload (UDPClient cliente1, String ipdestino, String ficheiro) throws UnknownHostException {
		cliente=cliente1;
		ipd= InetAddress.getByName(ipdestino);
		ficheirodestino=ficheiro;
	}

	public void recebe (TProto p) {
        l.lock();
        try{
            downloadData.add(p);
            empty.signal();
        } finally{
        l.unlock();
        }
	}

    public TProto nextTProto(){
	   l.lock();
       TProto tp;
       try{
            while(downloadData.size()==0){ empty.await(); }
		
            tp = downloadData.removeFirst();

	        return tp;
        } catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            l.unlock();
        }
        return tp;
	}

	public void conectar() throws Exception{   // falta organizar esta funcao acho
        // envia SYN          
        TProto syn = new TProto(0, 1, 1024,false, true, false, false,false,false,new byte[0]);
        cliente.send(syn,ipd,7777);

       while(true){
        	TProto synack = nextTProto();
        	if(synack.getSyn()==true && synack.getAck() == true){
                n_segmento = synack.getSegmentos();
        		break;
        	}
        }

        // envia ACK
        TProto ack = new TProto(2,1,1024,true,false,false,false,false,false,new byte[0]);
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

            int segmento=0;
            while(segmento < 1){
            	TProto tp = nextTProto();
            	byte c = tp.calculaChecksum(tp.getDados());
            	if (c == tp.getChecksum()) {
            		String dados = new String(tp.getDados());
            		System.out.println(dados);
            		writer.write(dados);
            	}
       			else {
       				System.out.println("Erro no checksum");
       				break;
       			}
            	segmento++;

            }
            writer.close();

            System.out.println("Download 100% com sucesso");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
