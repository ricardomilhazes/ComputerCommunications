import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

class TransfereccUpload extends Thread{

	UDPClient cliente;
	Transferecc cc;
	InetAddress enddestino;
	File ficheiro;
	FileInputStream fis;
	int mss;
    final Lock l = new ReentrantLock();
    final Condition empty  = l.newCondition();
	LinkedList<TProto> uploadData = new LinkedList<>();
    Map<Integer,String> segmented_file = new HashMap<>();  // map com os varios fragmentos do ficheiro que dividimos
    int[] n_segmentos;
    boolean tranferencia=false;

	public TransfereccUpload(UDPClient client, Transferecc transferecc,InetAddress ipdest, File f) throws UnknownHostException, IOException{
        cliente = client;
        cc = transferecc;
        enddestino = ipdest;
        ficheiro = f;
        // Creates a FileInputStream by opening a connection to an actual file, the file named by the File object file in the file system.
        fis = new FileInputStream(f);
        mss = 1024;
    }

    public void recebe (TProto p) throws Exception{
        l.lock();
        try{
            int index = p.getSequencia()/1024;
            if(tranferencia == true){
                if(p.getAck()==true){
                        if(n_segmentos[index]==1){
                            System.out.println("Duplicated");
                            TProto retry = new TProto(index*1024,0,1024,false,false,false,false,false,false,(segmented_file.get(index*1024).getBytes()));
                            cliente.send(retry,enddestino,7777);
                        }
                        else{
                            n_segmentos[index] = 1;
                        }
                    }
                }
                else{
                    uploadData.add(p);
                    empty.signal();
                }
        } finally{
        l.unlock();
        }
    }

    public TProto nextTProto(){
       l.lock();
       TProto tp;
       try{
            while(uploadData.size()==0){ empty.await(); }
        
            tp = uploadData.removeFirst();

            return tp;
        } catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            l.unlock();
        }
        return null;
    }

    public void enviarFicheiro() throws Exception{
	    int n_segmento = segmented_file.size();

            for (int i=0, seq = 0; i< n_segmento; i++, seq+=mss) {
		        Thread.sleep(2);
                String data = segmented_file.get(seq);
                TProto p = new TProto (seq,0,1024,false,false,false,false,false,false,data.getBytes());
                byte c = p.calculaChecksum(data.getBytes());
                p.setChecksum(c);
                cliente.send(p,enddestino,7777);
            }
            
    }

     public void dividirFicheiro (){
        try{
           
            InputStreamReader isr = new InputStreamReader(fis);
           
            long file_length = ficheiro.length();
            char[] file_char = new char[(int)file_length];
         
            isr.read(file_char, 0, (int)file_length);

            char[] lidos;
            if(file_length < mss)
                lidos = new char[(int) file_length];
            else lidos = new char[mss];

            lidos[0] = file_char[0];
            int seq = 0;
            for(int i = 1; i < file_length ; i++){
                if(i%mss != 0){
                    lidos[i%mss] = file_char[i];
                }else{
                    String data = new String(lidos);
                    segmented_file.put(seq,data);
                    seq+=mss;

                    if(file_length-i < mss)
                        lidos = new char[(int) file_length-i];
                    else lidos = new char[mss];

                    lidos[0] = file_char[i];
                }
            }
            String data = new String(lidos);
            segmented_file.put(seq,data);
            n_segmentos = new int[segmented_file.size()];
            Arrays.fill(n_segmentos,0);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
        Método que define o início de uma conexão
    */
    public void conectar() throws Exception{

        // Recebe SYN
        
          while(true){
            TProto syn = nextTProto();
            if(syn.getSyn() == true){
                mss = syn.getMSS();
                break;
            }
        }

        // divide ficheiro consoante o MSS
        dividirFicheiro();

        // envia SYNACK
        TProto synack = new TProto(1, 0,1024, true, true, false, false,false,false,new byte[0]);
        synack.setSegmentos(segmented_file.size());
        System.out.println(segmented_file.size());
        cliente.send(synack,enddestino,7777);

          // recebe ACK
          while(true){
            TProto ack = nextTProto();
            if(ack.getAck() == true){
                break;
		    }
        }
    }

    /*
        Este método vai ter de ser otimizado
    */
    public void run(){
	
    try{
        
        conectar();
        this.tranferencia=true;
        enviarFicheiro();
	
    }
	
    catch(Exception e){
	   e.printStackTrace();
	}
        cc.desconectar(enddestino);
    }

}
