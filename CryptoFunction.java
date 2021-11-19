package crypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.Certificate;

import java.io.FileInputStream;

import java.security.KeyStore;

import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import support.UtilityFunction;

public class CryptoFunction {
	
	
	public static PrivateKey getPrivateKey(String utente) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		char[] password = "changeit".toCharArray();
		FileInputStream fis = new FileInputStream(
				"/Users/emme_quadro/Desktop/cartellaUtenti/certificati/" + utente + "_keystore.p12");
		keyStore.load(fis, password);

		PrivateKey privateKey = (PrivateKey) keyStore.getKey(utente + "_kp", password);
		return privateKey;
	}
	
	public static PublicKey getPublicKey(String mand,String ricev) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		char[] password = "changeit".toCharArray();
		FileInputStream fis = new FileInputStream(
				"/Users/emme_quadro/Desktop/cartellaUtenti/certificati/" + mand + "_keystore.p12");
		keyStore.load(fis, password);
		Certificate certificate = keyStore.getCertificate(ricev + "_to_" + mand + "_keypair");
		PublicKey publicKey = certificate.getPublicKey();
		return publicKey;
		
	}
	
	public static boolean encryptInvio(PublicKey key_ricev, PrivateKey key_mand, String file, String mandante,
			String ricevente) throws Exception, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		String encrypted;
		byte[] cript_medio;
		// criptiamo per ben due volte
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key_mand);

		cript_medio = cipher.doFinal(file.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, key_ricev);

		encrypted = UtilityFunction.base64Encode(cipher.doFinal(cript_medio));
		
		System.out.println("Ora il tuo messaggio è al sicuro");

		// creato file e inviato
		boolean op_ok;
		if (op_ok = UtilityFunction.createFileInvio(mandante, ricevente, encrypted) == true)
			System.out.println("File inviato!");

		return op_ok;
	}

	public static boolean decryptLettura(PublicKey key_mand, PrivateKey key_ricev, String file, String ricevente,
			String mandante) throws Exception, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {

		byte[] cript_medio, file_byte;
		file_byte = UtilityFunction.base64Decode(file);

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

		cipher.init(Cipher.DECRYPT_MODE, key_ricev);
		cript_medio = cipher.doFinal(file_byte);

		cipher.init(Cipher.DECRYPT_MODE, key_mand);
		String decrypted = new String(cipher.doFinal(cript_medio));
		System.out.println("**************************");
		System.out.println("Messaggio decripatato: ");
		System.out.println(decrypted);
		System.out.println("**************************");
		System.out.println("Dopo aver letto sulla console, vedrai sul tuo desktop un file col testo decriptato");

		boolean op_ok;
		
		if (op_ok = UtilityFunction.createFileLetto(mandante, ricevente, decrypted) == true)
			System.out.println("File creato!");


		return op_ok;
	}
}
