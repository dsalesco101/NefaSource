package ethos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ethos.model.players.PlayerSave;
import ethos.util.Misc;
import ethos.util.Stopwatch;

public class CharacterBackup {

	private static final String SAVE_DIRECTORY = "./Data/CharacterSaves/";
	private static final int TIME = 7200000; //2 hours
	public static Stopwatch timer = new Stopwatch().reset();

	public static String getBackupFileName() {
		return SAVE_DIRECTORY + "Backup";
	}

	public CharacterBackup() {
		Misc.createDirectory(SAVE_DIRECTORY, PlayerSave.SAVE_DIRECTORY);
		File f1 = new File(PlayerSave.SAVE_DIRECTORY);
		File f2 = new File(getBackupFileName() + getDate() + ".zip");
		if (!f2.exists()) {
			try {
				System.out.println("[Auto-Backup] The mainsave has already been backed up today. Backup aborted.");
				zipDirectory(f1, f2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {System.out.println("[Auto-Backup] The mainsave has already been backed up today. Backup aborted.");
		}
	}

	public static void sequence() {
		Misc.createDirectory(SAVE_DIRECTORY, PlayerSave.SAVE_DIRECTORY);
		File f1 = new File(PlayerSave.SAVE_DIRECTORY);
		File f2 = new File(getBackupFileName() + getDate() + ".zip");
		if (!f2.exists()) {
			try {
				System.out.println("[BACKUP] Characters successfully backed up.");
				zipDirectory(f1, f2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("[BACKUP] Characters already backed up, backup canceled..");
		}
	}
	public static final void zipDirectory(File f, File zf) throws IOException {
		ZipOutputStream z = new ZipOutputStream(new FileOutputStream(zf));
		zip(f, f, z);
		z.close();
	}
	private static final void zip(File directory, File base, ZipOutputStream zos)
			throws IOException {
		File[] files = directory.listFiles();
		byte[] buffer = new byte[8192];
		int read = 0;
		for (int i = 0, n = files.length; i < n; i++) {
			if (files[i].isDirectory()) {
				zip(files[i], base, zos);
			} else {
				FileInputStream in = new FileInputStream(files[i]);
				ZipEntry entry = new ZipEntry(files[i].getPath().substring(
						base.getPath().length() + 1));
				zos.putNextEntry(entry);
				while (-1 != (read = in.read(buffer))) {
					zos.write(buffer, 0, read);
				}
				in.close();
			}
		}
	}

	public static String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
		now = null;
		dtf = null;
		return currentDate;

	}
}