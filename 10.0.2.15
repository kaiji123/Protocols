
//This is a simple client that connects to on ip "ipAddy" on  port "portNo" 
// and sends two strings.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class client {

    // ipAddy must be changed to the ip address of the server
    static String ipAddy = "127.0.0.1";
    static int portNo = 11337;


    public static void main(String[] args) throws Exception {

	System.out.println("opening socket");
	Socket socket = new Socket(ipAddy,portNo);
	
	//Wrapping a PrintWriter around the output streams lets us to write strings
	OutputStream outStream = socket.getOutputStream();
    InputStream inStream = socket.getInputStream();
	
	System.out.println("writing data");

	outStream.write("Connect Protocol 1".getBytes());
	outStream.flush();

    byte[] message1 = new byte[32];
    inStream.read(message1);
	
	System.out.println("closing socket");		
	socket.close();
	
	System.out.println("done");
    }

    private static byte[] hexStringToByteArray(String s) {
	int len = s.length();
	byte[] data = new byte[len / 2];
	for (int i = 0; i < len; i += 2) {
	    data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
				  + Character.digit(s.charAt(i+1), 16));
	}
	return data;
    }   
}












