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


public class GreetingClient{
    Image newimg;
    static BufferedImage bimg;
    byte[] bytes;

    public static void main(String [] args){
        String serverName = "localhost";
        int port = 6066;
        try{
            Socket client = new Socket(serverName, port);
            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());
            int code = 10;
            byte[] barray={(byte)code};
            System.out.println("Len "+barray.length);
            outToServer.write(barray);
            client.close();
            /*
				Socket clientSocket = new Socket("localhost", 6789);
				DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToServer.write(msg);
             */
            /*
            Robot bot;
            bot = new Robot();
            bimg = bot.createScreenCapture(new Rectangle(0, 0, 200, 100));
            ImageIO.write(bimg,"JPG",client.getOutputStream());
            client.close();
            */
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
