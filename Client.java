// File Name GreetingClient.java
import java.net.*;
import java.io.*;
import java.util.Arrays;
public class Client {

   public static void main(String [] args) {
      String serverName = "127.0.0.1";
      int port = Integer.parseInt("9999");
      try {
         System.out.println("Connecting to " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         // Buffers
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         
         System.out.println("Just connected to " + client.getRemoteSocketAddress());
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         
		 byte b = 10;
		 byte[] bytes = {b};
         out.write(bytes);
         
         // ---
         byte[] data = new byte[512];
         //Cuantos datos envio el server
         int count = in.read(data);
         System.out.println("Count es "+count);
         System.out.println(Arrays.toString(data));
         System.out.println("Server says " + in.read());
         // ----
         b = 30;
         out.write(b);
         
         client.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
