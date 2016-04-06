package TCPServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerThread{
	
	private static final int N_THREADS = 4;
	private static final int TIME_OUT = 20000;
	private static ServerSocket welcomeSocket;
	public int idTCP;
	

	public void iniciarConexion() {
		
		idTCP=0;
		
		final ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);
		Runnable serverRunTCP = new Runnable(){
		
	
    public void run() {
    	welcomeSocket =null;
    	   	
        try {
        	   	      
                            
              welcomeSocket = new ServerSocket(6789);
              while (true) {
            	  
            	  Socket p = welcomeSocket.accept();
                  
                  System.out.println("Inicia Servidor");
              
                 p.setSoTimeout(TIME_OUT);
                 
                 idTCP++;
                 System.out.println("# Conexiones"+ idTCP);
                 
                 pool.execute(new ComunicacionTCP(p,idTCP));
                 
          }
        }
              catch (Exception e) {
            System.err.println(e.getMessage());
        }
        finally{
        try {
        	welcomeSocket.close();
        	System.out.println("Cerró conexión");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
    }
        };
        Thread serverTCP = new Thread (serverRunTCP);
        serverTCP.start();
	}
    
    public static void main(String[] args) 
	{
		
		ServerThread main = new ServerThread();
		main.iniciarConexion();
		
	}

   
}