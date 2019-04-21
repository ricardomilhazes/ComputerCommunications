import java.net.*;
import java.io.*;

class TransfereccUpload {

	UDPClient cliente;
	Transferecc cc;
	InetAddress enddestino;
	File ficheiro;
	FileInputStream fis;
	int mss;
	int n_segmento;
	byte[] uploadData = new byte[1024];
    Map<Integer,String> segmented_file = new HashMap<>();  // map com os varios fragmentos do ficheiro que dividimos


	public TransfereCCUpload(UDPClient client, Transferecc transferecc,InetAddress ipdest, File f) throws UnknownHostException, IOException{
        cliente = client;
        cc = transferecc;
        enddestino = ipdest;
        ficheiro = f;
        // Creates a FileInputStream by opening a connection to an actual file, the file named by the File object file in the file system.
        fis = new FileInputStream(f);
        mss = 1024;
        n=0;
    }

    public void recebe (TProto p) {
    	uploadData[n]=p;
    }


    public void enviarFicheiro(){
            TProto p = new PDU(0, 0,false, false, false, true,false,false);
            // AgenteUDP sends PDU
            cliente.send(p,addressDest,7777);
        }



        // esta funcao é totalmente do Joao e do Braga, opa eu tive a ver e esta fixe e percebe-se nao sei se será melhor fazer umas mudanças à nossa maneira so p nao dar estrondo

     public void dividirFicheiro (){
        try{
           
            InputStreamReader isr = new InputStreamReader(fis);
           
            long file_length = file.length();
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
            System.out.println(segmented_file.size());

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
        Método que define o início de uma conexão
    */
    public void conectar(){

      «
        // Recebe SYN
        
        TProto syn = syn.trata(cc.receiveDatagram(cliente.receive()));

        // divide ficheiro consoante o MSS
        divideFile();

        // envia SYNACK
        TProto synack = new PDU(1, 0, true, true, false, false,false,false);
        cliente.send(synack,addressDest,7777);

          // recebe ACK
         TProto ack = ack.trata(cc.receiveDatagram(cliente.receive()));
    }

    /*
        Este método vai ter de ser otimizado
    */
    public void run(){

        conectar();

        sendFile();

        tfcc.desconectar(addressDest); // FALTA FAZER A FUNCAO DESCONECTAR

    }
