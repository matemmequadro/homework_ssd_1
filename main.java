package messaggistica;

import support.UtilityFunction;
import crypto.CryptoFunction;

import java.util.Scanner;


import java.security.PrivateKey;
import java.security.PublicKey;


public class main {

	public static void main(String[] args) throws Exception {
		System.out.println("Salve cosa vuoi fare ?");
		int scelta_up, scelta_sec;
		String nome = null, pw = null;
		do {

			Scanner scanner = new Scanner(System.in);

			UtilityFunction.mainMenu();

			scelta_up = scanner.nextInt();

			switch (scelta_up) {
			case 1:
				System.out.println("MESSAGE: Ti prego inserisci user e pw");
				boolean login = false;
				while (login == false) {
					System.out.println("User:");
					nome = scanner.next();
					System.out.println("PW:");
					pw = scanner.next();
					login = UtilityFunction.login(nome, pw);
				}

				System.out.println("Ciao utente " + nome + ", cosa vuoi fare?");
				do {
					UtilityFunction.subMenu();

					scelta_sec = scanner.nextInt();
					switch (scelta_sec) {
					case 1://file da inviare nella cartella ricevuto del ricevente, criptazione, controllo della firma digitale
						String ricev = null;

						boolean controll_file = false;
						while (controll_file == false) {
							System.out.println("A chi vuoi mandare?");

							ricev = scanner.next();

							if (!ricev.equals(nome)) {
								controll_file = UtilityFunction.controllUtente(ricev);
							}
						}

						String path_da_inviare, mex, file_mex;
						System.out.println("Qual è il file che vuoi mandare?");
						file_mex = scanner.next();
						path_da_inviare = "/Users/emme_quadro/Desktop/cartellaUtenti/" + nome + "/da mandare/"
								+ file_mex + ".txt";
						mex = UtilityFunction.leggiFile(path_da_inviare);


						PrivateKey privateKeyMandFile=CryptoFunction.getPrivateKey(nome);
						PublicKey  publicKeyRicevFile=CryptoFunction.getPublicKey(nome, ricev);

				
						if (CryptoFunction.encryptInvio(publicKeyRicevFile, privateKeyMandFile, mex, nome,
								ricev) == true) {
							System.out.println("L'operazione è andata a buon fine!!");

						}
						

						System.out.println("Ora tornerai al menu per utenti loggati");
						System.out.println("");

						break;
					case 2://messaggio da inviare nella cartella ricevuto del ricevente, criptazione, controllo della firma digitale
						String ricev_mes = null;

						boolean controll_mes = false;
						while (controll_mes == false) {
							System.out.println("A chi vuoi mandare?");

							ricev_mes = scanner.next();

							if (!ricev_mes.equals(nome)) {
								controll_mes = UtilityFunction.controllUtente(ricev_mes);
							}
						}

						String messaggio;
						System.out.println("Qual è il messaggio che vuoi mandare?");
						messaggio = scanner.next();


						PrivateKey privateKeyMandMes=CryptoFunction.getPrivateKey(nome);
						PublicKey  publicKeyRicevMes=CryptoFunction.getPublicKey(nome, ricev_mes);
						

						if (CryptoFunction.encryptInvio(publicKeyRicevMes, privateKeyMandMes, messaggio, nome,
								ricev_mes) == true) {
							System.out.println("L'operazione è andata a buon fine!!");

						}
						;

						System.out.println("Ora tornerai al menu per utenti loggati");
						System.out.println("");

						break;
					case 3://lettura del file inviato, nella cartella arrivi, roba di decriptazione, controllo della firma digitale
						
						String mess_ricev, file_ricev, mandante;
						System.out.println("Qual è il file che vuoi leggere?");
						file_ricev = scanner.next();
						String path_ricevere = "/Users/emme_quadro/Desktop/cartellaUtenti/" + nome + "/ricevuto/"
								+ file_ricev + ".txt";
						mandante = UtilityFunction.troncaNome(file_ricev);
						mess_ricev = UtilityFunction.leggiFile(path_ricevere);
						PrivateKey privateKeyRicevente=CryptoFunction.getPrivateKey(nome);
						PublicKey  publicKeyMandante=CryptoFunction.getPublicKey(nome, mandante);

						if (CryptoFunction.decryptLettura(publicKeyMandante, privateKeyRicevente, mess_ricev, nome,
								mandante) == true) {
							System.out.println("L'operazione è andata a buon fine!!");

						}

						System.out.println("Ora tornerai al menu per utenti loggati");
						System.out.println("");
						break;
					case 4:
						System.out.println("MESSAGE: Addio");
						scanner.close();
						System.exit(0);

					default:
						System.out.println("ERROR: Non valido!!");
						System.out.println("");
					}
				} while (scelta_sec != 4);

				break;

			case 2:
				System.out.println("MESSAGE: Addio");
				
				scanner.close();
				System.exit(0);

			default:
				System.out.println("ERROR: Non valido!!");
				System.out.println("");

			}
		} while (scelta_up != 2);

	}

}
