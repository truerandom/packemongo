// File Name GreetingServer.java
//https://stackoverflow.com/questions/33087890/multithreading-with-client-server-program
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.nio.file.Files;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Desktop;
public class Server extends Thread {
   private ServerSocket serverSocket;
   Socket server;
   byte intentos;
   int estado;
   public Server(Socket server,int port) throws IOException {
      this.server = server;
      System.out.println(server);
      //intentos de captura
      intentos = 3;
      //estado
      estado = 0;
   }
	
   public void run() {
         try {
            //System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            //Socket server = serverSocket.accept();    
            System.out.println("Conectado a " + this.server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            int c = 0;
            byte res=0;
			byte pokid=-1;
			byte[] arr;
			boolean continuar=true;
            while(!this.isInterrupted() && c < 50) {
				System.out.println("\nProcesando "+server);
				byte[] recibidos = new byte[3];
				int numbytes = in.read(recibidos);
				if(numbytes!=-1){
					System.out.println("Numero de bytes recibidos "+numbytes+"\nData:\t");
					for(int i=0;i<numbytes;i++)	
						System.out.print(recibidos[i]+" ");
					c++;
					//System.out.println("Recibido "+Arrays.toString(recibidos));
					int opcion = recibidos[0];
					switch(opcion){
						// Se recibio un intento de conexion
						case 10:
							System.out.println("\nRecibi inicio de conexion");
							this.estado = 1;
							res = 20;
							pokid = (byte)((Math.random()* 10));
							arr = new byte[]{res,pokid};
							System.out.println("Pok id "+pokid);
							out.write(arr);
							break;
						// decidio capturar el pokemon
						case 30:
							System.out.println("\nIntentos: "+this.intentos);
							if(this.intentos > 0){
								if(this.estado == 1){
									System.out.println("Puede capturar");
									byte resx = (byte)(((Math.random()* 10)) %3);
									//if(resx == 1){ //Chanfle
									if(resx == 1){
										System.out.println("Packemon capturado");
										arr = new byte[]{22,pokid,100};
										out.write(arr);
										System.out.println("Enviando pockemon");
										// Leo la img
										out = new DataOutputStream(server.getOutputStream());
										// exp
										File fnew=new File("1.jpg");
										BufferedImage oImage=ImageIO.read(fnew);
										ImageIO.write(oImage,"JPG",out);	
										this.server.close();
									}
									else{
										System.out.println("Pokemon no capturado");
										arr = new byte[]{21,pokid,this.intentos};
										intentos--;
										out.write(arr);
									}
								}else{
									System.out.println("Intento de captura en un edo no valido");
									this.server.close();
								}
							}else{
								System.out.println("Intentos agotados");
								// Debo mandar el paquete 23
								arr = new byte[]{23};
								out.write(arr);
								this.server.close();
							}
							break;
						case 31:
							System.out.println("Terminando la sesion del cliente");
							arr = new byte[]{32};
							out.write(arr);
							this.server.close();
						case 32:
							System.out.println("Terminando la sesion del cliente");
							arr = new byte[]{32};
							out.write(arr);
							this.server.close();
					}
				}else{
					//System.exit(1);
					this.server.close();
				}
		  }
		  return;
   }catch(Exception ex){System.out.println("Error"+ex);}
}
   
   public static void main(String [] args) {
      try{
		  int port = Integer.parseInt("9999");
		  ServerSocket serverSocket = new ServerSocket(port);
		  //Socket server = null;
		  while(true){
				Socket server = serverSocket.accept();
				//server.setSoTimeout(10000);
				//System.out.println(server);
				Thread t = new Server(server,port);
				t.start();			  
		  }
      }catch (IOException ex) {
         ex.printStackTrace();
	  }
   }
}
