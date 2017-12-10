// File Name GreetingClient.java
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
public class Client {
	// Parse response debe regresar un entero indicando si a continuacion debe haber entrada del usuario
	// 1 entrada del usuario 0 eoc
	// El caso default es cuando se recibe la imagen
	public static int parseResponse(DataInputStream in,DataOutputStream out){
		try{
			System.out.println("Entre a parseResponse");
			byte[] data = new byte[512];
			//Cuantos datos envio el server
			int count = in.read(data);
			int idpok=-1;
			int intentos=-1;
			int imgsize = -1;
			System.out.println("Count es "+count);
			for(int i=0;i<count;i++)	
				System.out.print(data[i]+" ");
			if(count !=-1){
				switch(data[0]){
					case 20:
						idpok= data[1];
						System.out.println("Capturar pokemon? id "+idpok);
						return 1;
					case 21:
						idpok = data[1];
						intentos = data[2];
						System.out.println("Reintentar id "+idpok+" intentos "+intentos);
						return 1;
					case 22:
						idpok = data[1];
						imgsize = data[2];
						System.out.println("Pokemon capturado "+idpok);
						System.out.println("Img size: "+imgsize);
						//Aqui recibo los siguientes paquetes
						return 0;
					case 23:
						System.out.println("Intentos agotados");
						terminaSesion(out);
						return 0;
					case 32:
						System.out.println("Terminando sesion");
						System.exit(1);
						return 0;
				}
			}else{
				System.exit(1);
			}
			return 0;
			//System.out.println(Arrays.toString(data));
		}catch(Exception ex){
			System.out.println("ClientResponse: "+ex);
			return 0;
		}
	}
	
	public static void iniciaConexion(DataOutputStream out){
		try{
			byte b = 10;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
		}
	}
	
	public static void enviaSi(DataOutputStream out){
		try{
			byte b = 30;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
		}
	}
	
	public static void enviaNo(DataOutputStream out){
		try{
			byte b = 31;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
		}
	}
	
	public static void terminaSesion(DataOutputStream out){
		try{
			System.out.println("Entre a termina sesion");
			byte b = 32;
			byte[] bytes = {b};
			out.write(bytes);
		}catch(Exception ex){
			System.out.println("Ocurrio un error al enviar los datos");
		}	
	}
	
	public static void sendResponse(DataOutputStream out,Scanner sc){
		String linea = sc.nextLine();
		System.out.println("linea "+linea);
		if(linea.equals("y")){
			System.out.println("SI");
			enviaSi(out);
		}else{
			System.out.println("NO");
			enviaNo(out);
		}
		//parseResponse(in);
	}
	
	public static void main(String [] args) {
		String serverName = "127.0.0.1";
		int port = Integer.parseInt("9999");
		try {
			// Inicio de conexion y edatos
			System.out.println("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);
			Scanner sc = new Scanner(System.in);
			// Buffers
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);
			// Inicio de conexion y edatos
			iniciaConexion(out);
			// Parse response
			while(true){
				if(parseResponse(in,out) == 1)
					sendResponse(out,sc);
			}
			//client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
