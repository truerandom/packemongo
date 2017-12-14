import java.io.File;
public class locateme{
public static void main(String [] args){
	try{
		String current = new java.io.File(".").getCanonicalPath();
		System.out.println("current dir "+current);
	}catch(Exception e){
	}
}
}
