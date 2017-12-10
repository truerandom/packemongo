// File Name GreetingServer.java
import java.net.*;
import java.io.*;
import java.util.Arrays;

public class Server extends Thread {
   private ServerSocket serverSocket;
   int intentos;
   int estado;
   public Server(int port) throws IOException {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(18000);
      //intentos de captura
      intentos = 5;
      //estado
      estado = 0;
   }

   public void run() {
      while(true) {
         try {
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            
            byte[] recibidos = new byte[3];
            int numbytes = in.read(recibidos);
            System.out.println(in.available());
            System.out.println("Numero de bytes recibidos "+numbytes);
            System.out.println("Recibido "+Arrays.toString(recibidos));
            try{
				int opcion = recibidos[0];
				switch(opcion){
					// Se recibio un intento de conexion
					case 10:
						System.out.println("Recibi inicio de conexion");
						this.estado = 1;
						byte res = 20;
						byte pokid = (byte)((Math.random()* 10));
						byte[] arr = {res,pokid};
						System.out.println("Pok id "+pokid);
						out.write(arr);
						break;
					// decidio capturar el pokemon
					case 30:
						
				}
			}catch(Exception e){	
				
			}
            //System.out.println(in.readUTF());
           
            //DataOutputStream out = new DataOutputStream(server.getOutputStream());
            //out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
            //server.close();
            
         } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            break;
         } catch (IOException e) {
            e.printStackTrace();
            break;
         }
      }
   }
   
   public static void main(String [] args) {
      int port = Integer.parseInt("9999");
      try {
         Thread t = new Server(port);
         t.start();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
