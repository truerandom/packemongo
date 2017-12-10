// File Name GreetingClient.java
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
public class Client {
	public static void parseResponse(DataInputStream in){
		try{
			System.out.println("Entre a parseResponse");
			byte[] data = new byte[512];
			//Cuantos datos envio el server
			int count = in.read(data);
			System.out.println("Count es "+count);
			if(count >0){
				switch(data[0]){
					case 20:
						int idpok = data[1];
						System.out.println("Capturar pokemon? id "+idpok);
						break;
				}
			}
			System.out.println(Arrays.toString(data));
		}catch(Exception ex){
			System.out.println("ClientResponse: "+ex);
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
			parseResponse(in);
			String linea = sc.nextLine();
			System.out.println("linea "+linea);
			if(linea.equals("y")){
				System.out.println("SI");
				enviaSi(out);
			}else{
				System.out.println("NO");
				enviaNo(out);
			}
			parseResponse(in);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
