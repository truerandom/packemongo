import java.awt.image.BufferedImage;
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

       public GreetingServer(int port) throws IOException, SQLException, ClassNotFoundException, Exception{
          serverSocket = new ServerSocket(port);
          serverSocket.setSoTimeout(180000);
       }

       public void run(){
           while(true){ 
               try{
                  server = serverSocket.accept();
                  byte[] buffer=new byte[4];
                  int read = 0;
                  DataInputStream dIn = new DataInputStream(server.getInputStream());
                  while ((read = dIn.read(buffer, 0, buffer.length)) != -1) {
					dIn.read(buffer);
					System.out.println("says " + Arrays.toString(buffer));
				  }
                  /*
                  BufferedImage img=ImageIO.read(ImageIO.createImageInputStream(server.getInputStream()));
                  JFrame frame = new JFrame();
                  frame.getContentPane().add(new JLabel(new ImageIcon(img)));
                  frame.pack();
                  frame.setVisible(true);                  
              }catch(SocketTimeoutException st){
                   System.out.println("Socket timed out!");
                  break;
             }catch(IOException e){
                  e.printStackTrace();
                  break;
             */
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
