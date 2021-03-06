import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

/**
 * 
 * @author Betalord
 */
public class Misc {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("<HH:mm:ss>");
	

	public static void log(String s) {
		System.out.println("<" + dateFormat.format(new Date()) + "> " + s);
	}

	/**
	 * Return time in milliseconds from the start of the system. Can have a negative value. 
	 */
	public static long getTime() {
		return System.nanoTime() / 1000000;
	}
	
	static String getStackTrace() {
		StringBuilder r = new StringBuilder();

		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
			r.append(ste).append("\n");
		}

		return r.toString();
	}

	static List<String> readTextFile2(String file) {
		List<String> lines = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			try {
				String line = br.readLine();
				while (line != null) {
					lines.add(line);
					line = br.readLine();
				}
				return lines;
			} finally {
				br.close();
			}			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Returns true on success. */
	static boolean saveTextFile(String file, String contents) {
		return saveTextFile(file, contents, false);
	}
	
	/** Returns true on success. */
	private static boolean saveTextFile(String file, String contents, boolean ignoreErrors) {
		BufferedWriter bw;
		
		try {
			File f = new File(file);
			// create parent folder(s) if needed:
			File parent = f.getParentFile();
			if (parent != null && !parent.exists())
				parent.mkdirs(); 
			
			bw = new BufferedWriter(new FileWriter(f));
			try {
				bw.write(contents);
			} finally {
				bw.close();
			}			
		} catch (IOException e) {
			if (!ignoreErrors)
				e.printStackTrace();
			return false;
		}
		return true;
	}
	
	static String millisToHumanForm(int millis) {
		int s = millis / 1000;
		int m = s / 60;
		s = s % 60;
		int h = m / 60;
		m = m % 60;
		
		if (s==0 && m==0 && h==0)
			return "0s";
		
		return (h > 0 ? (h + "h") : "") + (m > 0 ? (m + "m") : "") + (s > 0 ? (s + "s") : "");
	}
	
	static int max(int... values) {
		int max = Integer.MIN_VALUE;
		for (int value : values)
			if (value > max)
				max = value;
		return max;
	}
	
	static int min(int... values) {
		int min = Integer.MAX_VALUE;
		for (int value : values)
			if (value < min)
				min = value;
		return min;
	}
	
	/**
	 * Returns index of closest match from the 'values' array.
	 */
	static int findClosestMatch(int[] values, int value) {
		int best = Integer.MAX_VALUE;
		int bestIndex = -1;
		for (int i = 0; i < values.length; i++) {
			if (Math.abs(values[i] - value) < best) {
				best = Math.abs(values[i] - value);
				bestIndex = i;
			}
		}
		return bestIndex;
	}
	
	static String listToString(EnumSet<?> list) {
		StringBuilder r = new StringBuilder();
		for (Object e : list)
			r.append(e).append(", ");
		r = new StringBuilder(r.substring(0, r.length() - 2));
		return r.toString();
	}

	static String encodeFileToBase64Binary(File toEncode)  {

		byte[] encoded = new byte[0];
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(toEncode));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded, StandardCharsets.US_ASCII);
	}
	
}
