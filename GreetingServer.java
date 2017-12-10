import java.awt.image.BufferedImage;
import java.net.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.util.Arrays;
import java.sql.*;
/*
https://stackoverflow.com/questions/29728722/read-byte-arrays-from-datainputstream
https://stackoverflow.com/questions/11208479/how-do-i-initialize-a-byte-array-in-java

Como se la tabla de estados puedo definir el tamanio del arreglo de bytes de manera dinamica
inicializandolo entre cada cambio de estado por la longitud esperada, metiendo todo en un try
catch y lanzando una excepcion y terminando la conexion si ocurre algo raro.
*/
public class GreetingServer extends Thread{
	private ServerSocket serverSocket;
	Socket server;
	int attempts;
	public GreetingServer(int port) throws IOException, SQLException, ClassNotFoundException, Exception{
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(180000);
		this.attempts = 0;
	}
	
	public void parseOption(int option){
		switch(option){
			case 10:
				System.out.println("Peticion de inicio de sesion");
				this.sendPokemon();
				break;
			case 30:
				System.out.println("Si recibido");
				break;
			case 31:
				System.out.println("No recibido");
				break;
		}
	}
		
	//envia un pokemon al cliente
	public void sendPokemon(){
		System.out.println("Aqui deberia mandar el pokemon");
	}
	
	public void run(){
		while(true){ 
			try{
				server = serverSocket.accept();
				byte[] buffer=new byte[4];
				int read = 0;
				int opcion = -1;
				DataInputStream dIn = new DataInputStream(server.getInputStream());
				while ((read = dIn.read(buffer, 0, buffer.length)) != -1) {
					System.out.println("Read es "+read);
					dIn.read(buffer);
					try{
						opcion = buffer[0];
						System.out.println("Opcion es "+opcion);
						this.parseOption(opcion);
					}catch(Exception e){
						System.out.println("Error al leer la respuesta del cliente");
					}
					System.out.println("says " + Arrays.toString(buffer));
				}
			}catch(Exception ex){
                  System.out.println(ex);
            }
		}
	}

	public static void main(String [] args) throws IOException, SQLException, ClassNotFoundException, Exception{
		Thread t = new GreetingServer(6066);
		t.start();
	}
}
/*
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.*;
import java.net.*;
import java.util.Arrays;
*/
