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
import java.nio.charset.StandardCharsets;
public class client1 {

    // ipAddy must be changed to the ip address of the server
    static String ipAddy = "127.0.0.1";
    static int portNo = 11337;
	
    

    public static void main(String[] args) throws Exception {
	
	System.out.println("opening socket");
	Socket socket = new Socket(ipAddy,portNo);
	
	//Wrapping a PrintWriter around the output streams lets us to write strings
	OutputStream outStream = socket.getOutputStream();
    InputStream inStream = socket.getInputStream();


	outStream.write("Connect Protocol 1".getBytes());


	    byte[] message1 = new byte[32];
	    inStream.read(message1);
	System.out.println(byteArrayToHexString(message1));
	System.out.println(new String(message1, StandardCharsets.US_ASCII));

	//write back to server (Ns)
	outStream.write(message1);

	// read ns ns
	byte [] message2 = new byte[48];
	inStream.read(message2);
	//System.out.println(byteArrayToHexString(message2));
	//System.out.println(new String(message2, StandardCharsets.US_ASCII));
	
	//write back again (Ns,Ns)
	outStream.write(message2);

	byte[] message3 = new byte[208];
	byte [] message4 = new byte[208];
	


	
	
	System.out.println(inStream.read(message3) + "read ");
	
	
	System.out.println(byteArrayToHexString(message3));
	Integer s = new Integer(0);
	byte [] zerobyte = new byte[16];
	for (int i = 0; i < 16; i++){
		zerobyte[i] = (byte) (0);	
	}
	 Key secretKeySpec = new SecretKeySpec(zerobyte, "AES");
	Cipher decAEScipher;
	 
	 
	decAEScipher = Cipher.getInstance("AES/ECB/NoPadding");
	decAEScipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
	
	byte[] secret = decAEScipher.update(message3);
	System.out.println();
	String tok = new String(secret);
	System.out.println(tok);
	

	
 	
	
	System.out.println("done");
        socket.close();
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
	 private static String byteArrayToHexString(byte[] data) { 
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) { 
		    int halfbyte = (data[i] >>> 4) & 0x0F;
		    int two_halfs = 0;
		    do { 
			if ((0 <= halfbyte) && (halfbyte <= 9)) 
			    buf.append((char) ('0' + halfbyte));
			else 
			    buf.append((char) ('a' + (halfbyte - 10)));
			halfbyte = data[i] & 0x0F;
		    } while(two_halfs++ < 1);
		} 
		return buf.toString();
	    } 
}












