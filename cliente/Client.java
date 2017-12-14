import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.*;
import java.awt.Desktop;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class Client {
	static List myBytes = new ArrayList<Byte>();
	static byte usuario = -1;
	
	/**
	Funcion encargada de recibir e interpretar los datos del servidor 
	@param InputStream y Output stream de la conexion con el server
	@return 1 si debido a estos datos es necesaria la interaccion del usuario
	0 en otro caso.
	*/
	public static int parseResponse(DataInputStream in,DataOutputStream out){
		try{
			byte[] data = new byte[1024];
			//Cuantos datos envio el server
			int count = in.read(data);
			int idpok=-1;
			int intentos=-1;
			int imgsize = -1;
			//Si el servidor envio datos
			if(count !=-1){
				switch(data[0]){
					case 20:
						idpok= data[1];
						System.out.print("\nCapturar pokemon id "+idpok+"? y/n\t");
						return 1;
					case 21:
						idpok = data[1];
						intentos = data[2];
						System.out.print("Reintentar id "+idpok+" intentos "+intentos+"? y/n\t");
						return 1;
					case 22:
						idpok = data[1];
						imgsize = data[2];
						System.out.println("Pokemon capturado "+idpok);
						getImage(in);
						return 0;
					case 23:
						System.out.println("Intentos agotados\n");
						System.out.println("------------------------");
						System.out.println("Consultando base");
						System.out.println("------------------------");
						consultaBase(out);
						return 0;
					case 32:
						System.out.println("Terminando sesion");
						System.exit(1);
					case 40:
						System.out.println("Error en la comunicacion: captura");
						System.exit(1);
					// Consulta de los pokemones de este usuario
					case 50:
						System.out.println("Pokemones de este usuario en la bd\nids:");
						for(int i=1;i<count;i++)
							System.out.println("\t"+data[i]);
						System.exit(1);
					default:
						System.out.println("Entre a def no deberia");
						System.exit(1);
				}
			}else{
				System.exit(1);
			}
			return 0;
		}catch(Exception ex){
			System.out.println("ClientResponse: "+ex);
			return 0;
		}
	}
	
	/**
	Funcion encargada de recibir los datos de la imagen esta sera mostrada
	y se escribira en el disco
	@param InputStream de la conexion con el server
	@return void
	*/
	public static void getImage(DataInputStream in){
		String fname="pokemon";
		byte[] data = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//Cuantos datos envio el server
		int read;
		try{
			while((read = in.read()) != -1) {
				baos.write(read);
			}
		}catch(Exception e){ System.out.println(e);}
		try (FileOutputStream fos = new FileOutputStream(fname)) {
		   fos.write(baos.toByteArray());
		   fos.close();
		}catch(Exception e){
			System.out.println("Cant write img "+e);
		}try{
			BufferedImage image = ImageIO.read(new File(fname));
			JLabel label = new JLabel(new ImageIcon(image));
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.getContentPane().add(label);
			f.pack();
			f.setLocation(200,200);
			f.setVisible(true);
			Scanner sc = new Scanner(System.in);
			sc.nextLine();
		}catch(Exception ex){
			System.out.println("Error at getImage "+ex);
			System.exit(1);
		}
	}
	
	/**
	* Funcion que envia la solicitud de inicio 
	* @param OutputStream de la conexion con el server
	* @return void
	*/
	public static void iniciaConexion(DataOutputStream out){
		try{
			byte b = 10;
			// le paso el id ahora es arbitrario
			byte[] bytes = {b,usuario};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
			System.exit(1);
		}
	}
	
	/**
	 * Funcion para enviar un paquete de confirmacion si
	 * @param OutputStream de la conexion con el server
	 * @return void
	 */
	public static void enviaSi(DataOutputStream out){
		try{
			byte b = 30;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
			System.exit(1);
		}
	}
	
	/**
	 * Funcion para enviar un paquete de confirmacion no
	 * @param OutputStream de la conexion con el server
	 * @return void
	 */
	public static void enviaNo(DataOutputStream out){
		try{
			byte b = 31;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
		}
	}
	
	/**
	 * Funcion para consultar la base de datos del servidor
	 * @param OutputStream de la conexion con el server
	 * @return void
	 */
	public static void consultaBase(DataOutputStream out){
		try{
			byte b = 50;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
		}
	}
	
	/**
	 * Funcion para enviar un paquete de termino de sesion
	 * @param OutputStream de la conexion con el server
	 * @return void
	 */
	public static void terminaSesion(DataOutputStream out){
		try{
			byte b = 32;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
		}	
	}
	
	/**
	 * Funcion para enviar las respuestas al servidor
	 * @param OutputStream de la conexion con el server
	 * @return void
	 */
	public static void sendResponse(DataOutputStream out,Scanner sc){
		String linea = sc.nextLine();
		try{
			if(linea.equals("y")){
				enviaSi(out);
			}else{
				enviaNo(out);
			}
		}catch(Exception e){
			System.out.println("Error al enviar datos "+e);
			System.exit(1);
		}
	}
	
	/**
	 * Punto de entrada del ejecutable recibe host y puerto como args
	 * @param host
	 * @param puerto
	 */
	public static void main(String [] args) {
		if(args.length!=2){
			System.out.println("\nUsage\n java Client <ip> <port>");
			System.exit(1);
		}
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		usuario = (byte)(int)((Math.random()*200)%20);
		System.out.println("Current user "+usuario);
		try {
			Socket client = new Socket(serverName, port);
			try{
				client.setSoTimeout(18000);
			}catch(Exception ex){
				System.out.println("Error de conexion "+ex);
				System.exit(1);
			}
			Scanner sc = new Scanner(System.in);
			// Buffers
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Conectado a " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			// Inicio de conexion y edatos
			iniciaConexion(out);
			// Parse response
			while(true){
				if(parseResponse(in,out) == 1)
					sendResponse(out,sc);
			}
		} catch (IOException e) {
			System.out.println("Error de conexion");
			e.printStackTrace();
			System.out.println("Verifica los parametros utilizados");
		}
	}
}
