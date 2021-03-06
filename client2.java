//This is a simple client that connects to on ip "ipAddy" on  port "portNo" 
// and sends two strings.

import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class client2 {

    // ipAddy must be changed to the ip address of the server
    static String ipAddy = "127.0.0.1";
    static int portNo = 11338;
	
    static BigInteger g = new BigInteger("129115595377796797872260754286990587373919932143310995152019820961988539107450691898237693336192317366206087177510922095217647062219921553183876476232430921888985287191036474977937325461650715797148343570627272553218190796724095304058885497484176448065844273193302032730583977829212948191249234100369155852168");
	static BigInteger p = new BigInteger("165599299559711461271372014575825561168377583182463070194199862059444967049140626852928438236366187571526887969259319366449971919367665844413099962594758448603310339244779450534926105586093307455534702963575018551055314397497631095446414992955062052587163874172731570053362641344616087601787442281135614434639");
	static Cipher decAESsessionCipher;
	static Cipher encAESsessionCipher;
	static Cipher nopad;
	static boolean debug = true;
	static String s;
    public static void main(String[] args) throws Exception {
	
	try{
	System.out.println("opening socket");
	Socket socket = new Socket(ipAddy,portNo);
	
	//Wrapping a PrintWriter around the output streams lets us to write strings
	OutputStream out = socket.getOutputStream();
    InputStream in = socket.getInputStream();
    DataOutputStream outStream =new DataOutputStream(out);
    DataInputStream inStream=new DataInputStream(in);

	System.out.println("outstream created");
    DHParameterSpec dhSpec = new DHParameterSpec(p,g);
    KeyPairGenerator diffieHellmanGen = KeyPairGenerator.getInstance("DiffieHellman");
    diffieHellmanGen.initialize(dhSpec);
    KeyPair serverPair = diffieHellmanGen.generateKeyPair();
    PrivateKey x = serverPair.getPrivate();
    PublicKey gToTheX = serverPair.getPublic();
	System.out.println(byteArrayToHexString(gToTheX.getEncoded()));
    


	System.out.println("writing");
    outStream.writeInt(gToTheX.getEncoded().length);
    outStream.write(gToTheX.getEncoded());


    int publicKeyLen = inStream.readInt();
	byte[] message1 = new byte[publicKeyLen];

    inStream.read(message1);
	System.out.println(new String(message1));
	
    KeyFactory keyfactoryDH = KeyFactory.getInstance("DH");
    X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(message1);
    PublicKey gToTheY = keyfactoryDH.generatePublic(x509Spec);
	//System.out.println(byteArrayToHexString(message1));
	System.out.println(byteArrayToHexString(gToTheY.getEncoded()));
	;


    calculateSessionKey(x, gToTheY);

	Scanner sc= new Scanner(System.in);  
	System.out.println("input your c Nonce");
	Integer cNonce = sc.nextInt();
	byte [] cbytes = BigInteger.valueOf(cNonce).toByteArray(); //cbytes is less than 16 bytes	
	


	


	byte [] msg1 = encAESsessionCipher.doFinal(cbytes);

	outStream.write(msg1);
	System.out.println("message 1 written :" +msg1.length );

	byte[] inms1 = new byte [32];
	

	inStream.read(inms1);
	byte [] dec = decAESsessionCipher.doFinal(inms1);

	System.out.println("{nc+1, ns} length "+ dec.length);
	System.out.println(new BigInteger(inms1));
	System.out.println(new String(inms1));


	// this has problems
	byte[] cinc = new byte[16]; //encrypted cinc cant decrypt it

	byte [] sn = new byte[4];

	System.out.println("bytarray " + dec);

	
	System.out.println("arraycopy phase \n");

	System.arraycopy(dec, 0, cinc, 0, 16);
	System.arraycopy(dec, 16, sn, 0, 4);
	int snonce = new BigInteger(sn).intValue();
	System.out.println("snonce : " + snonce);

	Files.write(Paths.get("cinc.txt"), cinc);


	
	System.out.println("byte cinc length :"  +cinc.length);
	System.out.println("byte in cinc enc length" + encAESsessionCipher.doFinal(cinc).length);
	

	/*
	int sonce= new BigInteger(sn).intValue();
	System.out.println("sonce value int "+ sonce);



	int cencinc = new BigInteger(cinc).intValue();
	//System.out.println("cinc to stirng\n" + cinc.toString());
	System.out.println("cinc enc to int"+cencinc);
	*/
	//System.out.println("enc ns length " + encAESsessionCipher.doFinal(sn).length);

	//
	//System.out.println("cinc to string :" + cinc.toString());
	//System.out.println("sn to string"+ sn.toString());

	//



	/*
	String sincin = sc.next();
	byte[] scout = sincin.getBytes();
	System.out.println("scout bytelengt" + scout.length);
	byte[] last  = encAESsessionCipher.doFinal(scout);
	System.out.println("last enc length" + last.length);
	outStream.write(last);
	*/
	



	System.out.println("input");

	s = sc.next();
	byte [] snincenc = Files.readAllBytes(Paths.get("cinc.txt"));
	System.out.println("read cinc");
	byte [] last = encAESsessionCipher.doFinal(snincenc);
	outStream.write(last);
	System.out.println(last.length);


	



	




	//sc.nextByte();  // we will use this is next does not work
	/*
	System.out.println("{ns+1}kcs: ");
	int sincinput = sc.nextBytes();
	byte[] sincout = BigInteger.valueOf(sincinput).toByteArray();
	byte [] outputmessage = encAESsessionCipher.doFinal(sincout);
	System.out.println("byte length of {ns+1}kcs :" + outputmessage.length);
	outStream.write(outputmessage);
	*/



	System.out.println();

	byte [] secretenc= new byte[320];
	System.out.println("reading secret enc");
	inStream.read(secretenc);
	System.out.println(new String(secretenc));
	byte[] secretdec =decAESsessionCipher.update(secretenc);
	System.out.println("secret : " + new String(secretdec));


	


	
	



	

	
 	
	
	System.out.println("done");
    socket.close();
	}
	catch(BadPaddingException e){
		System.out.println(e);
	}
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
    	// This method sets decAESsessioncipher & encAESsessioncipher 
	private static void calculateSessionKey(PrivateKey y, PublicKey gToTheX)  {
	    try {
		// Find g^xy
		KeyAgreement serverKeyAgree = KeyAgreement.getInstance("DiffieHellman");
		serverKeyAgree.init(y);
		serverKeyAgree.doPhase(gToTheX, true);
		byte[] secretDH = serverKeyAgree.generateSecret();
		if (debug) System.out.println("g^xy: "+byteArrayToHexString(secretDH));
		//Use first 16 bytes of g^xy to make an AES key
		byte[] aesSecret = new byte[16];
		System.arraycopy(secretDH,0,aesSecret,0,16);
		Key aesSessionKey = new SecretKeySpec(aesSecret, "AES");
	
		// Set up Cipher Objects
		
		decAESsessionCipher = Cipher.getInstance("AES"); //maybe make nopadding 
		decAESsessionCipher.init(Cipher.DECRYPT_MODE, aesSessionKey);
				
		nopad= Cipher.getInstance("AES/ECB/NoPadding"); //maybe make nopadding 
		nopad.init(Cipher.DECRYPT_MODE, aesSessionKey);
		encAESsessionCipher = Cipher.getInstance("AES");
		encAESsessionCipher.init(Cipher.ENCRYPT_MODE, aesSessionKey);
	    } catch (NoSuchAlgorithmException e ) {
		System.out.println(e);
	    } catch (InvalidKeyException e) {
		System.out.println(e);
	    } catch (NoSuchPaddingException e) {
		e.printStackTrace();
	    }
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












