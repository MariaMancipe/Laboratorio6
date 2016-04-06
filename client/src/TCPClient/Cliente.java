package TCPClient;

import java.io.*;
import java.net.*;


public class Cliente extends Thread{
	
	static BufferedReader inFromUser;
	static Socket	clientSocket;
	
	
	 public Cliente() throws Exception {
	        
//	   	    clientSocket = new Socket("13.90.209.108", 6789);		 
	   	 clientSocket = new Socket("127.0.0.1", 6789);
	   	 
	   	inFromUser = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

	   	    
		 
	    }

	@Override
	public void run() {
		
		String sentence;
	    String modifiedSentence;
	    
	    System.out.println("Inicia Cliente");
	    	
		    try {

				
		   		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		   	    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		   	    sentence = "hola";
				outToServer.writeBytes(sentence + "\n");
			    modifiedSentence = inFromServer.readLine();
			    System.out.println("Servidor: " + modifiedSentence);
			    
               if(modifiedSentence.equalsIgnoreCase("hola"))
               {
            	   
            	sentence = "carga";
   				outToServer.writeBytes(sentence + "\n");
   			    modifiedSentence = inFromServer.readLine();
   			    System.out.println("Servidor: " + modifiedSentence);
   			    
   			    if(modifiedSentence.equalsIgnoreCase("carga"))
   			    {
   			    	sentence = "cerrar";
   	   				outToServer.writeBytes(sentence + "\n");
   	   			    modifiedSentence = inFromServer.readLine();
   	   			    System.out.println("Servidor: " + modifiedSentence);
   	   			    
   	   			if(modifiedSentence.equalsIgnoreCase("cerrar"))
   			    {
   			    	System.out.println("Servidor: Termina conexión"  );
   	   			
   	   			    clientSocket.getOutputStream().close();
   	   			    
   	   			    clientSocket.close();
			   }
			   }
               }
			    

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	} 

}