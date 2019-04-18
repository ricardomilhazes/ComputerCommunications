import java.net.*;
import java.io.*;

class TransfereccDownload {
	UDPClient cliente;
	InetAddress ipdestino;
	String ficheirodestino;
	byte[] downloadData = new byte[1024]; 
	int n_segmento;



	public TransfereccDownload (UDPClient cliente1, String ipdestino, String ficheiro) throws UnknownHost Exception {
		cliente=cliente1;
		ipdestino= InetAddress.getByName(ipdestino);
		ficheirodestino=ficheiro;
		n_segmento=0;
	}

	public void recebe (TProto p) {
		System.out.println("Datagram Packet from " + this.ipdestino );
		byte[n_segmento]=p;
	}

	public void beginConnection(){   // falta organizar esta funcao acho
        // envia SYN          
        TProto syn = new TProto(0, 1, 1024, new String(), true, false, false, false, new byte[0]);
        cliente.send(syn,addressDest,7777);

        // recebe SYNACK
        while(true){
            TProto synack = nextPDU();
            if(synack.getSYN() == true && synack.getACK() == true){
                segment_num = Integer.valueOf(synack.getOptions());
                break;
            }
        }

        // envia ACK
        TProto ack = new TProto(2, 1, 1024, new String(), false, false, true, false, new byte[0]);
        cliente.send(ack,addressDest,7777);
    }
	
}