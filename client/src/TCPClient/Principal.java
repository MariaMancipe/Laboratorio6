package TCPClient;



import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import src.UDPConnection;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPasswordField;

public class Principal implements ActionListener {

	private JFrame frame;
	private JTextField textField;
	private JButton btnNewButton;
	private JLabel lblContrasea;
	private Cliente TCPClient;
	private JPanel panel;
	private static final String BOTON = "BT";
	private JPasswordField passwordField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal() {
		try {
			initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public String darDatos()
	{
		char[] valor=	passwordField.getPassword();
		String pass = String.valueOf(valor);
		
		System.out.println("Usuario " + textField.getText() + " y Contraseña: " + pass);
	
		return "Nombre " + textField.getText() + " y Contraseña: " + pass;
	}
	/**
	 * Initialize the contents of the frame.
	 * @throws Exception 
	 */
	private void initialize() throws Exception {
		TCPClient = new Cliente();
		TCPClient.start();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 419, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		JLabel lblAutenticacinDeUsuario = new JLabel("Autenticaci\u00F3n de usuario");
		
		JLabel lblNombre = new JLabel("Usuario:");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		lblContrasea = new JLabel("Contrase\u00F1a:");
		
		btnNewButton = new JButton("Ingresar");
		btnNewButton.setActionCommand( BOTON );
		btnNewButton.addActionListener( this );
		
		passwordField = new JPasswordField();
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(152)
					.addComponent(lblAutenticacinDeUsuario))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(36)
					.addComponent(lblNombre))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(26)
					.addComponent(lblContrasea))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(175)
					.addComponent(btnNewButton))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(113)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(passwordField, Alignment.LEADING)
						.addComponent(textField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
					.addGap(93))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(30)
					.addComponent(lblAutenticacinDeUsuario)
					.addGap(35)
					.addComponent(lblNombre)
					.addGap(5)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(lblContrasea)
					.addGap(9)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addComponent(btnNewButton))
		);
		panel.setLayout(gl_panel);
		
		
	}

	 public void actionPerformed( ActionEvent pEvento )
	    {
		  String comando = pEvento.getActionCommand( );
	        if( BOTON.equals( comando ) )
	        {
	        	String nombre =textField.getText();   
	    		            
	        	try {
	                
	                File statText = new File("./data/Usuario_"+nombre+".txt");
	                FileOutputStream is = new FileOutputStream(statText);
	                OutputStreamWriter osw = new OutputStreamWriter(is);    
	                Writer w = new BufferedWriter(osw);
	                System.out.println(darDatos());
	                w.write(darDatos());
	                w.close();
	                
	                
	            } catch (IOException e) {
	                System.err.println("Problem writing to the file usuario_"+nombre+".txt");
	            }
	        	panel.setVisible(false);
	        	UDPConnection udp = new UDPConnection(frame);
	        	udp.start();
	        	 }
	    }
}
