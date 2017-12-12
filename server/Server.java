import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.nio.file.Files;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Desktop;

public class Server extends Thread {
   private ServerSocket serverSocket;
   Socket server;	//socket de conexion con el cliente
   byte intentos;	//intentos del usuario para la captura
   int estado;		//estado interno del hilo del servidor
   byte usuario;	//id de usuario del hilo actual
   static ArrayList bdserver;
   
   public Server(Socket server,int port) throws IOException {
      this.server = server;
      System.out.println(server);
      intentos = 3;	//intentos de captura
      estado = 0;
      usuario = -1;	//usuario por default
   }
   
   static void insertaPokemon(byte usuario,byte pokid){
		System.out.println("\n++++++++++++INSERTANDO BD++++++++++++++++");
		System.out.println("Usuario: "+usuario);
		System.out.println("Pokemon: "+pokid);
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		test.addPok(bdserver,usuario,pokid);
   }
   
   /*
   Params:
   res: 		numero aleatorio que decide si el pokemon es capturado o no
   arr:			arreglo de bytes para enviar al cliente
   recibidos: 	arreglo de bytes recibidos por el cliente
   numbytes:	numero de bytes enviados por el cliente
   opcion:		primer byte del mensaje enviado por el cliente
   pokid:		id del pokemon ofrecido para captura
   */
   public void run() {
         try {
            System.out.println("Conectado a " + this.server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            int c = 0;
            byte res=0;		
			byte pokid=-1;
			byte[] arr;
            while(!this.isInterrupted() && c < 50) {
				System.out.println("\nProcesando "+server);
				byte[] recibidos = new byte[3];
				int numbytes = in.read(recibidos);
				if(numbytes!=-1){
					System.out.println("Numero de bytes recibidos "+numbytes+"\nData:\t");
					for(int i=0;i<numbytes;i++)	
						System.out.print(recibidos[i]+" ");
					c++;
					int opcion = recibidos[0];
					switch(opcion){
						// Se recibio un intento de conexion
						case 10:
							System.out.println("\nRecibi inicio de conexion");
							byte usr = recibidos[1];
							this.usuario = usr;
							this.estado = 1;
							res = 20;
							pokid = (byte)((Math.random()* 10));
							arr = new byte[]{res,pokid};
							System.out.println("Pok id "+pokid);
							System.out.println("Usuario "+usuario);
							out.write(arr);
							break;
						// decidio capturar el pokemon
						case 30:
							System.out.println("\nIntentos: "+this.intentos);
							if(this.intentos > 0){
								if(this.estado == 1){
									System.out.println("El usuario "+this.usuario+" puede capturar");
									byte resx = (byte)(((Math.random()* 10)) %3);
									if(resx == 1){
										System.out.println("Packemon capturado");
										System.out.println("Enviando pokemon");
										//Inserto el usuario en la base
										insertaPokemon(this.usuario,pokid);
										// Leo la img
										out = new DataOutputStream(server.getOutputStream());
										File fnew=new File("imgs/"+pokid+".jpg");
										byte tam = (byte)(fnew.length()/1000);
										System.out.println("Tam imagen: "+tam);
										// Envio la informacion de la imagen
										arr = new byte[]{22,pokid,tam};
										out.write(arr);
										// Leo la imagen
										BufferedImage oImage=ImageIO.read(fnew);
										// Envio la imagen
										ImageIO.write(oImage,"JPG",out);
										// Escribo en la base
										test.writeDatabase(bdserver);
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
									// nuevo error de captura
									arr = new byte[]{40};
									out.write(arr);
									this.server.close();
								}
							}else{
								System.out.println("Intentos agotados para usuario "+usuario);
								// Debo mandar el paquete 23
								arr = new byte[]{23};
								out.write(arr);
							}
							break;
						//Recibido el no
						case 31:
							System.out.println("Terminando la sesion del cliente "+this.usuario);
							arr = new byte[]{32};
							out.write(arr);
							test.writeDatabase(bdserver);
							this.server.close();
						//Terminando sesion
						case 32:
							System.out.println("Terminando la sesion del cliente "+this.usuario);
							arr = new byte[]{32};
							out.write(arr);
							test.writeDatabase(bdserver);
							this.server.close();
						//Consulta de base de datos por el usuario
						case 50:
							System.out.println("Consulta de capturas");
							// Aqui debo hacer la consulta a la base con el usuario
							ArrayList<Byte> idspoks = test.getPoks(bdserver,this.usuario);
							System.out.println(idspoks);
							arr = new byte[idspoks.size()+1];
							arr[0] = 50;
							for(int i=0;i<idspoks.size();i++)
								arr[i+1] = idspoks.get(i);
							out.write(arr);
							this.server.close();
					}
				}else{
					this.server.close();
				}
			  }
			  return;
	   }catch(Exception ex){System.out.println("Desconexion de cliente "+ex);}
	}
   
   public static void main(String [] args) {
      try{
		  bdserver = test.initDB();
		  int port = 9999;
		  ServerSocket serverSocket = new ServerSocket(port);
		  System.out.println("Listening on port "+port);
		  while(true){
				Socket server = serverSocket.accept();
				server.setSoTimeout(10000);
				Thread t = new Server(server,port);
				t.start();			  
		  }
      }catch (IOException ex) {
         ex.printStackTrace();
	  }
   }
}

class test{
	//Estructura para la base de datos: lista de listas donde:
	//el primer indice es el id de usuario y el contenido de esta lista
	//son los ids de los pokemones agregados
	static ArrayList bdatosx;
	static String fname = "database";
	
	// Recibe una linea de numeros (ids pok) separados por coma y regresa
	// una lista de bytes con esos ids
	public static ArrayList<Byte> agregaDataUsuario(String data){
		// Llenado de la base para un usuario
		String[] pokarray = data.split(",");
		ArrayList<Byte> userpok = new ArrayList<Byte>();
		for(int i=0;i<pokarray.length;i++)
			userpok.add((byte)(Integer.parseInt(pokarray[i])));
		return userpok;
	}
	
	// Regresa una base de datos a partir del archivo de texto fname
	public static ArrayList initDB(){
		ArrayList<String> lineas = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(fname))){
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
                lineas.add(sCurrentLine);
        } catch (IOException e) { e.printStackTrace(); }
		ArrayList bdatos = new ArrayList();
		//Lleno el renglon del usuario i
		for(int i=0;i<lineas.size();i++)
			bdatos.add(agregaDataUsuario(lineas.get(i)));
		return bdatos;
	}
	
	//agrego pokemon
	public static void addPok(ArrayList bdatos,byte usuario,byte pok){
		((ArrayList)bdatos.get(usuario-1)).add(pok);
	}
	
	//obtiene los ids de pokemon de la base de datos
	public static ArrayList<Byte> getPoks(ArrayList bdatos,byte usuario){
		return (ArrayList<Byte>)bdatos.get(usuario-1); 
	}
	
	// Escribe los cambios a la base de datos
	public static void writeDatabase(ArrayList bdatos){
		try{
			PrintWriter pr = new PrintWriter(fname);
			pr.close();
			BufferedWriter writer = new BufferedWriter(new FileWriter(fname, true));
			for(int i=1;i<=bdatos.size();i++){
				String actual = getPoks(bdatos,(byte)i).toString().replace("[","").replace("]","").replace(" ","");
				writer.append(actual+"\n");
			}
			writer.close();
		}catch(Exception e){ System.out.println(e); }
	}
}
