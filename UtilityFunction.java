package support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class UtilityFunction {

	public static void mainMenu() {
		System.out.println("---------------------");
		System.out.println("   MENU PRINCIPALE");
		System.out.println("1: Login");
		System.out.println("2: Exit");
		System.out.println("---------------------");
	}

	public static void subMenu() {
		System.out.println("-----------------------------");
		System.out.println("   MENU PER UTENTI LOGGATI");
		System.out.println("1: Invio file");
		System.out.println("2: Invio messaggio");
		System.out.println("3: Lettura file");
		System.out.println("4: Lettura certificato");
		System.out.println("5: Exit");
		System.out.println("-----------------------------");

	}

	public static String troncaNome(String file) {
		String[] parts = file.split("_");
		return parts[2];
	}

	public static String base64Encode(byte[] array) {
		return Base64.getEncoder().encodeToString(array);
	}

	public static byte[] base64Decode(String stringa) throws Exception {

		byte[] decodedBytes = Base64.getMimeDecoder().decode(stringa);

		return decodedBytes;
	}
	
	public static boolean createFileInvio(String mand, String ricev, String messaggio) {
		boolean ok = false;
		try {

			File newFile = new File("/Users/emme_quadro/Desktop/cartellaUtenti/" + ricev + "/ricevuto/",
					"mex_from_" + mand + "_to_" + ricev +UtilityFunction.timeStamp()+ ".txt");
			if (newFile.createNewFile()) {

				FileWriter myWriter = new FileWriter(newFile);
				myWriter.write(messaggio);
				myWriter.close();
				ok = true;

			} else {
				System.out.println("File già esiste.");

			}
		} catch (IOException e) {
			System.out.println("ERRORE!!");
			e.printStackTrace();
		}
		return ok;
	}
	
	public static boolean createFileLetto(String mand, String ricev, String messaggio) {
		boolean ok = false;
		try {

			File newFile = new File("/Users/emme_quadro/Desktop/cartellaUtenti/" + ricev + "/desktop/",
					"mex_from_" + mand + "_to_" + ricev +UtilityFunction.timeStamp()+" (decriptato).txt");
			if (newFile.createNewFile()) {

				FileWriter myWriter = new FileWriter(newFile);
				myWriter.write(messaggio);
				myWriter.close();
				ok = true;

			} else {
				System.out.println("File già esiste.");

			}
		} catch (IOException e) {
			System.out.println("ERRORE!!");
			e.printStackTrace();
		}
		return ok;
	}

	public static String leggiFile(String path) throws IOException {
		String mex = null;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			mex = everything;
		}
		return mex;
	}

	@SuppressWarnings("deprecation")
	public static boolean controllUtente(String user) {
		String path = "/Users/emme_quadro/Desktop/cartellaUtenti/ut.xlsx";
		String inwork = " ", cercare = " ";
		boolean find = false;

		try {

			FileInputStream file = new FileInputStream(new File(path));
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_STRING:
						inwork = cell.getStringCellValue();

						if (inwork.equals(user)) {

							cercare = inwork;
						}

						break;

					}

				}

			}
			user = cercare;
			workbook.close();
			file.close();
			if (user == " ") {
				find = false;
				System.out.print("utente non trovato!\n");
			} else
				find = true;

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return find;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean login(String user, String pw) {
		String path = "/Users/emme_quadro/Desktop/cartellaUtenti/utenza.xlsx";
		String inwork = " ", cercare = " ", cerc_pw = null;
		boolean find = false;

		try {

			FileInputStream file = new FileInputStream(new File(path));
			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// For each row, iterate through all the columns
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					// Check the cell type and format accordingly
					switch (cell.getCellType()) {

					case Cell.CELL_TYPE_STRING:
						inwork = cell.getStringCellValue();

						if (cercare.equals(user)) {
							if (inwork.equals(pw)) {
								cerc_pw = inwork;
								cercare = " ";
							}
						}

						if (inwork.equals(user)) {

							cercare = inwork;
						}

						break;

					}

				}

			}
			pw = cerc_pw;
			workbook.close();
			file.close();
			if (pw == null) {
				find = false;
				System.out.print("password o utente errato!\n");
			} else
				find = true;

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return find;
	}
	
	public static String timeStamp() {
		 ZoneId zid = ZoneId.systemDefault();
	        ZonedDateTime datetime = ZonedDateTime.now(zid);
	        // if want to format into a specific format
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "__HH_mm_ss" );
	        String timeStamp = datetime.format(formatter);
	        return timeStamp;
	        
	}
	public static void sleeping(int num) throws InterruptedException {
		
		for (int i=0;i<num;i++) {
			System.out.print(".");
			TimeUnit.SECONDS.sleep(1);
		}
		System.out.println();
	}

}
