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
	
	public synchronized void send(){
		try {
			//MyFrame fr = new MyFrame();
			//fr.setVisible(true);
			DatagramSocket serverSocket = new DatagramSocket(8080);
			byte[] receiverBuf = new byte[1024];
			DatagramPacket receiverPacket = new DatagramPacket(receiverBuf, receiverBuf.length);
			boolean con = true;
			int state = 0;
			int port = 8181;
			InetAddress ip = InetAddress.getByName("224.0.0.3");
			
			File file = new File("..\\server\\avecesaria.mp4");
			String path = file.getAbsolutePath();
			int fileSize = (int)file.getTotalSpace();
			System.out.println(path+file.exists());
			VideoCapture capture = new VideoCapture(path);
			System.out.println("Starting streaming to IPAddress: " + ip.toString());
			while(con){
				/*if(state==-1){
					serverSocket.receive(receiverPacket);
					String sentence = new String(receiverPacket.getData());
					if(sentence.contains("START_STREAMING")){
						state = 0;
						port = receiverPacket.getPort();
						ip = receiverPacket.getAddress();
						
					}
				}else */if(state == 0){
					
					if(!capture.isOpened()){
						capture = new VideoCapture(path);
						
						System.out.println("Error initializing OpenCV Video Capture! Please wait until it's initialized");
					}else{
						System.out.println("LO LOGRO");
						Mat frame = new Mat();
						int i = 0;
						boolean ya = false;
						while(capture.read(frame)){
							//System.out.println(frame);
							//fr.render(frame);
							/*try {
								wait(30);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
							
							
							System.out.println(frame.elemSize());
							
							byte[] senderBuff = new byte[(int) ((frame.total() * frame.channels()))];
							frame.get(0, 0, senderBuff);
							System.out.println(senderBuff);
							System.out.println(senderBuff.length);
							int length = senderBuff.length;
							if(!ya){
								byte[] y = ("LENGTH;"+length+";"+Math.ceil(fileSize/length)+";Hola").getBytes();
								DatagramPacket senderPacket = new DatagramPacket(y, y.length, ip, port);
								serverSocket.send(senderPacket);
							}
							System.out.println("frame "+i+ " length: "+length+ " channels: "+ frame.channels() + " type: "+frame.type());
							i++;
							int total = (int)Math.floor(length/32000);
							int j=0;
							int inicio =0;
							int fin =32000;
							while(j<=total){
								byte[] buf = Arrays.copyOfRange(senderBuff, inicio, fin);
								
								System.out.println(i + " " + j+ " " + inicio + " " + fin+ " " + buf.length);
								j++;
								if(j==total){
									inicio = fin+1;
									fin = fin + (length%32000);
									
								}else{
									inicio = fin+1;
									fin = fin+32000;
								}
								DatagramPacket senderPacket = new DatagramPacket(buf, buf.length, ip, port);
								serverSocket.send(senderPacket);
								try {
									wait(3);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
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

