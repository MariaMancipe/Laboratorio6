package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class UDPConnection extends Thread {

	//private ArrayList<Mat> frames;

	private MyFrame f;
	
	public UDPConnection(JFrame jframe){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		f = new MyFrame(jframe);
		f.setVisible(true);
		//frames = new ArrayList<Mat>();	
	}
	public synchronized void consume(){
		
		MulticastSocket clientSocket;
		try {
			clientSocket = new MulticastSocket(8181);
			
			InetAddress ip = InetAddress.getByName("224.0.0.3");
			clientSocket.joinGroup(ip);
			//Sending Command
			/*senderBuf = "START_STREAMING".getBytes();
			DatagramPacket senderPacket = new DatagramPacket(senderBuf, senderBuf.length, ip, port);
			clientSocket.send(senderPacket);*/
			System.out.println("Connecting to ip: " + ip);
			
			//Receiving the frames length
			boolean ya = true;
			int size = -1;
			while(ya){
				byte[] receiverBuf = new byte[32000];
				DatagramPacket receiverPacket = new DatagramPacket(receiverBuf, receiverBuf.length);
				clientSocket.receive(receiverPacket);
				String sentence = new String(receiverPacket.getData());
				if(sentence.contains("LENGTH")){
					String[] x = sentence.split(";");
					size = (int)Long.parseLong(x[1]);
					//fr = Integer.parseInt(x[2]);
					System.out.println("Frame length has been received!");
					break;
				}
			}
			
			//Receiving frames
			int i=0;
			while(ya){
				int subsize = 0;
				byte[] framesB = new byte[32000];
				int j = 0;
				int k=0;
				while(subsize<=size){
					byte[] receiverBuf = null;
					if(size - subsize >=32000){
						receiverBuf = new byte[32000];
					}else{
						receiverBuf = new byte[size-subsize];
					}
					DatagramPacket receiverPacket = new DatagramPacket(receiverBuf, receiverBuf.length);
					clientSocket.receive(receiverPacket);
					System.out.println(k);
					k++;
					if(j==0){
						framesB = receiverBuf;
					}else if(j>0){
						int t = receiverBuf.length + framesB.length;
						
						byte[] y = new byte[t];
						System.arraycopy(framesB, 0, y, 0, framesB.length);
						System.arraycopy(receiverBuf, 0, y, framesB.length, receiverBuf.length);
						framesB = y;
					}
					j++;
					//System.out.println("Hola"+ framesB.length);
					//System.out.println("Hola"+ receiverBuf.length);
					subsize += 32000;
				}
				
				//Mat frame = Imgcodecs.imdecode(new MatOfByte(framesB), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
				Mat frame = new Mat(480,854,CvType.CV_8UC3);
				frame.put(0, 0, framesB);
				System.out.println(frame);
				f.render(frame);
				/*try {
					wait(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				System.out.println("frame " + i+ " created!");
				i++;
				//frames.add(frame);
				System.out.println(framesB);
			}
			clientSocket.disconnect();
			clientSocket.leaveGroup(ip);
			clientSocket.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		consume();
	}
	
	public static void main(String[] args){
		UDPConnection udp = new UDPConnection(new JFrame());
		udp.consume();
	}
}
