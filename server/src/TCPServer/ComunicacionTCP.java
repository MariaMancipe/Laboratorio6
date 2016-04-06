package TCPServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import src.UDPConnection;

public class ComunicacionTCP implements Runnable {

	BufferedReader inFromClient;
	DataOutputStream outToClient;

	String clientSentence;
    String capitalizedSentence;
    Socket cliente;
    int nTCP;
	
	public  ComunicacionTCP (Socket p, int idTCP){
		
           		cliente = p;
           		nTCP=idTCP;
	
	try {
		inFromClient = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
	     outToClient = new DataOutputStream(cliente.getOutputStream());
         
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
	}

	@Override
	public void run() {
		
		try {
			clientSentence = inFromClient.readLine();

	        System.out.println("Recibido: " + clientSentence);
	        capitalizedSentence = clientSentence.toUpperCase() + '\n';
	        outToClient.writeBytes(capitalizedSentence);
	        
	        clientSentence = inFromClient.readLine();
	        System.out.println("Error: "+clientSentence);
	        if( clientSentence.equalsIgnoreCase("carga"))
	        {
	        	System.out.println("Pasa por aca");
	        	System.out.println("Recibido: " + clientSentence);
		        capitalizedSentence = clientSentence.toUpperCase() + '\n';
		        outToClient.writeBytes(capitalizedSentence);
		        
		        clientSentence = inFromClient.readLine();
		        if( clientSentence.equalsIgnoreCase("cerrar"))
		        {
		        	System.out.println("Recibido: " + clientSentence);
			        capitalizedSentence = clientSentence.toUpperCase() + '\n';
			        outToClient.writeBytes(capitalizedSentence);
			        
		        }   
	        }
	                
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	finally{
		try{
			System.out.println("Cerrando conexión del cliente "+ nTCP);
			cliente.getOutputStream().close();
			cliente.close();
			UDPConnection udp = new UDPConnection();
			udp.send();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
		
	}
	
	
}
