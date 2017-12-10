import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import javax.imageio.ImageIO;
import java.io.*;
import java.net.*;
import java.util.Arrays;

public class GreetingClient{
	
    Image newimg;
    static BufferedImage bimg;
    byte[] bytes;
    public static void main(String [] args){
        String serverName = "localhost";
        int port = 9999;
        try{
            Socket client = new Socket(serverName, port);
            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());
            DataInputStream dIn = new DataInputStream(client.getInputStream());
            byte[] buffer=new byte[1024];
            /* Envio inicial de inicio de sesion */
            int code = 10;
            byte[] barray={(byte)code};
            System.out.println("Len "+barray.length);
            outToServer.write(barray);
            
            System.out.println("Cliente ahora voy a esperar la respuesta");
            int read = 0;
            while ((read = dIn.read(buffer, 0, buffer.length)) != -1) {
					System.out.println("En el cliente read es "+read);
					dIn.read(buffer);
					System.out.println("says " + Arrays.toString(buffer));
			}
            //client.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
