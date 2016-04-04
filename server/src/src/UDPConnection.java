package src;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class UDPConnection {
	
	public UDPConnection(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public void send(){
		try {
			DatagramSocket serverSocket = new DatagramSocket(8080);
			byte[] receiverBuf = new byte[1024];
			DatagramPacket receiverPacket = new DatagramPacket(receiverBuf, receiverBuf.length);
			boolean con = true;
			int state = -1;
			int port = 0;
			InetAddress ip = null;
			File file = new File("C:\\Software\\Workspace\\server\\media\\avecesaria.mp4");
			int fileSize = (int)file.getTotalSpace();
			System.out.println(file.exists());
			VideoCapture capture = new VideoCapture("C:/Software/Workspace/server/media/avecesaria.mp4");
			
			while(con){
				if(state==-1){
					serverSocket.receive(receiverPacket);
					String sentence = new String(receiverPacket.getData());
					if(sentence.contains("START_STREAMING")){
						state = 0;
						port = receiverPacket.getPort();
						ip = receiverPacket.getAddress();
						System.out.println("Starting streaming to IPAddress: " + ip.toString()+ " on port: "+ port);
					}
				}else if(state == 0){
					
					if(!capture.isOpened()){
						capture = new VideoCapture("C:\\Software\\Workspace\\server\\media\\avecesaria.mp4");
						
						System.out.println("Error initializing OpenCV Video Capture! Please wait until it's initialized");
					}else{
						System.out.println("LO LOGRO");
						Mat frame = new Mat();
						int i = 0;
						boolean ya = false;
						while(capture.read(frame)){
							System.out.println(frame.elemSize());
							
							byte[] senderBuff = new byte[(int) (frame.total() * frame.channels())];
							frame.get(0, 0, senderBuff);
							int length = senderBuff.length;
							if(!ya){
								byte[] y = ("LENGTH;"+length+";"+Math.ceil(fileSize/length)+";Hola").getBytes();
								DatagramPacket senderPacket = new DatagramPacket(y, y.length, ip, port);
								serverSocket.send(senderPacket);
							}
							System.out.println("frame "+i+ " length: "+length);
							i++;
							int total = (int)Math.floor(length/32000);
							int j=0;
							int inicio =0;
							int fin =31999;
							while(j<=total){
								byte[] buf = Arrays.copyOfRange(senderBuff, inicio, fin);
								System.out.println(i + " " + j+ " " + inicio + " " + fin);
								j++;
								if(j==total-1){
									inicio = fin+1;
									fin = fin + (length%32000);
									
								}else{
									inicio = fin+1;
									fin = fin+32000;
								}
								DatagramPacket senderPacket = new DatagramPacket(buf, buf.length, ip, port);
								serverSocket.send(senderPacket);
							}
						}
						capture.release();
						System.out.println("The video has been completely sent!");
						state = 1;
					}
				}else{
					con = false;
					break;
				}
			}
			serverSocket.disconnect();
			serverSocket.close();
			System.out.println("The connection was closed");
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.out.println("Error creating the Datagram Socket on the port 8080");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error receiving commands from the client");
			e.printStackTrace();
		}
		
	}


	public static void main(String[] args){
		UDPConnection udp = new UDPConnection();
		udp.send();
	}
}

