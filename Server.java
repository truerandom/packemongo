// File Name GreetingServer.java
//https://stackoverflow.com/questions/33087890/multithreading-with-client-server-program
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.nio.file.Files;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Desktop;
// xdg-open abre la imagen con el manejador default
public class Server extends Thread {
   private ServerSocket serverSocket;
   Socket server;
   byte intentos;
   int estado;
   public Server(Socket server,int port) throws IOException {
      //serverSocket = new ServerSocket(port);
      //serverSocket.setSoTimeout(18000);
      System.out.println("Entre al constructor con servers");
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
            
            System.out.println("Just connected to " + this.server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            int c = 0;
            byte res=0;
			byte pokid=-1;
			byte[] arr;
			boolean continuar=true;
            while(!this.isInterrupted() && c < 50) {
				System.out.println("Processing with "+server);
				byte[] recibidos = new byte[3];
				int numbytes = in.read(recibidos);
				//System.out.println(in.available());
				System.out.println("Numero de bytes recibidos "+numbytes);
				c++;
				if(numbytes!=-1){
					System.out.println("Debo procesar");
					System.out.println("Recibido "+Arrays.toString(recibidos));
					int opcion = recibidos[0];
					switch(opcion){
						// Se recibio un intento de conexion
						case 10:
							System.out.println("Recibi inicio de conexion");
							this.estado = 1;
							res = 20;
							pokid = (byte)((Math.random()* 10));
							arr = new byte[]{res,pokid};
							System.out.println("Pok id "+pokid);
							out.write(arr);
							break;
						// decidio capturar el pokemon
						case 30:
							System.out.println("Intentos: "+this.intentos);
							if(this.intentos > 0){
								if(this.estado == 1){
									System.out.println("Puedes capturar");
									byte resx = (byte)(((Math.random()* 10)) %3);
									System.out.println("El resultado fue"+resx);
									//if(resx == 1){ //Chanfle
									if(resx == 1){
										System.out.println("Packemon capturado");
										arr = new byte[]{22,pokid,100};
										out.write(arr);
										System.out.println("Enviando img");
										// Leo la img
										// Funcion
										//Chanfleloquitearr = new byte[]{8,8,8,8,8,8,8,8};
										//out.write(arr);
										out = new DataOutputStream(server.getOutputStream());
										System.out.println("pase out");
										//Robot bot;
										//bot = new Robot();
										// exp
										File fnew=new File("1.jpg");
										BufferedImage oImage=ImageIO.read(fnew);
										System.out.println("pase oimage");
										ImageIO.write(oImage,"JPG",out);	
										System.out.println("pase write");
										//exit
										this.server.close();
										// finexp
										/*
										BufferedImage bimg = bot.createScreenCapture(new Rectangle(0, 0, 200, 100));
										ImageIO.write(bimg,"JPG",out);
										System.exit(1);
										*/
									}
									else{
										arr = new byte[]{21,pokid,this.intentos};
										intentos--;
										out.write(arr);
									}
								}else{
									System.out.println("Intento de captura en un edo no valido");
								}
							}else{
								System.out.println("Intentos agotados");
								// Debo mandar el paquete 23
								arr = new byte[]{23};
								out.write(arr);
							}
							break;
						case 31:
							System.out.println("Terminando la sesion del cliente");
							arr = new byte[]{32};
							out.write(arr);
						case 32:
							System.out.println("Terminando la sesion del cliente");
							arr = new byte[]{32};
							out.write(arr);
					}
				}else{
					System.exit(1);
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
				System.out.println(server);
				Thread t = new Server(server,port);
				t.start();			  
		  }
      }catch (IOException ex) {
         ex.printStackTrace();
	  }
   }
}
