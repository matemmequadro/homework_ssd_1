package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import java.util.Scanner;
import java.security.cert.Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import support.UtilityFunction;

public class CryptoFunction {

	public static PrivateKey getPrivateKey(String utente) throws UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, InterruptedException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		char[] password = "changeit".toCharArray();
		FileInputStream fis = new FileInputStream(
				"/Users/emme_quadro/Desktop/cartellaUtenti/certificati/" + utente + "_keystore.p12");
		keyStore.load(fis, password);

		PrivateKey privateKey = (PrivateKey) keyStore.getKey(utente + "_kp", password);

		return privateKey;
	}

	public static PublicKey getPublicKey(String mand, String ricev) throws UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException, InterruptedException {
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

		String time = UtilityFunction.timeStamp();
		/*if (creazioneFirma(key_mand, file, mandante, ricevente, time) == true) {
			System.out.println("Creazione firma e invio in corso");
			UtilityFunction.sleeping(2);
		}*/
		String encrypted;
		byte[] cript_medio;
		// criptiamo per ben due volte
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key_mand);

		cript_medio = cipher.doFinal(file.getBytes());

		cipher.init(Cipher.ENCRYPT_MODE, key_ricev);

		encrypted = UtilityFunction.base64Encode(cipher.doFinal(cript_medio));

		System.out.println("Criptazione in corso");
		UtilityFunction.sleeping(2);

		System.out.println("Ora il tuo messaggio è al sicuro");

		System.out.println("Invio del file in corso");
		UtilityFunction.sleeping(2);

		// creato file e inviato
		boolean op_ok;
		if (op_ok = UtilityFunction.createFileInvio(mandante, ricevente, encrypted, time) == true)
			System.out.println("File inviato!");

		UtilityFunction.sleeping(2);

		return op_ok;
	}

	public static boolean decryptLettura(PublicKey key_mand, PrivateKey key_ricev, String file, String ricevente,
			String mandante, String path) throws Exception, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		boolean op_ok = false;
		byte[] cript_medio, file_byte;
		file_byte = UtilityFunction.base64Decode(file);

		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

		cipher.init(Cipher.DECRYPT_MODE, key_ricev);
		cript_medio = cipher.doFinal(file_byte);

		cipher.init(Cipher.DECRYPT_MODE, key_mand);
		String decrypted = new String(cipher.doFinal(cript_medio));

		
		
		/*  System.out.println("Verifica firma in corso");
			UtilityFunction.sleeping(2);
		  if (verificaFirma(key_mand, path, decrypted, ricevente) == true) {
			System.out.println("ok");
		} else
			System.out.println("Nooooooooooo");
		*/
		System.out.println("Decriptazione in corso");
		UtilityFunction.sleeping(2);
		System.out.println("**************************");
		System.out.println("Messaggio decripatato: ");
		System.out.println(decrypted);
		System.out.println("**************************");
		System.out.println("Dopo aver letto sulla console, ");
		System.out.println("vedrai sul tuo desktop un file col testo decriptato");
		System.out.println("Creazione e scrittura sul file in corso");

		UtilityFunction.sleeping(2);

		if (op_ok = UtilityFunction.createFileLetto(mandante, ricevente, decrypted) == true)
			System.out.println("File creato!");

		return op_ok;
	}

	public static void letturaCertificato(String nome)
			throws CertificateException, FileNotFoundException, InterruptedException {

		System.out.println("Di chi vuoi leggere il certificato? (scrivi mio se vuoi il tuo)");
		Scanner scann = new Scanner(System.in);
		String utente;
		utente = scann.next();
		if (utente.equals("mio")) {
			utente = nome;
		}

		CertificateFactory fac = CertificateFactory.getInstance("X509");
		FileInputStream is = new FileInputStream(
				"/Users/emme_quadro/Desktop/cartellaUtenti/certificati/" + utente + "_certificate.cer");
		X509Certificate cert = (X509Certificate) fac.generateCertificate(is);

		System.out.println("Lettura certificato in corso");
		UtilityFunction.sleeping(2);

		System.out.println(cert);

	}

	public static boolean creazioneFirma(PrivateKey key, String mex, String ut, String ricevente, String time)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		boolean ok = false;

		Signature signature = Signature.getInstance("SHA256withRSA");

		signature.initSign(key);
		String file = "/Users/emme_quadro/Desktop/cartellaUtenti/" + ricevente + "/ricevuto/" + ut + time ;


		byte[] messageBytes = mex.getBytes();

		signature.update(messageBytes);
		byte[] digitalSignature = signature.sign();

		Files.write(Paths.get(file),digitalSignature);
		
		
		ok=true;
		return ok;

	}

	public static boolean verificaFirma(PublicKey key, String path, String mes, String utente) throws Exception {

		String[] parts = path.split("_");

		
		String path_firma = "/Users/emme_quadro/Desktop/cartellaUtenti/" + utente + "/ricevuto/" + parts[3] + "__"
				+ parts[7] + "_" + parts[8] + "_" + parts[9];
		
		path_firma=path_firma.replaceAll(".txt", "");
		
		

		
		Signature signature = Signature.getInstance("SHA256withRSA");

		signature.initVerify(key);
		
		byte[] messageBytes = Files.readAllBytes(Paths.get(path_firma));
		
		signature.update(mes.getBytes());
		

		
		boolean ok;
		ok=signature.verify(messageBytes);

		return ok;
	}
}
