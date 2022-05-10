import java.io.*;
import java.util.*;

public class VigenereCipher {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner kb = new Scanner(System.in);
		ArrayList<Character> alpha = new ArrayList<Character>();
		char A = 'A';
		char a = 'a';
		char zero = '0';
		for (int i = 0; i < 26; i++) {
			alpha.add(A);
			A++;
		}
		for (int j = 26; j < 52; j++) {
			alpha.add(a);
			a++;
		}
		for (int i = 52; i < 62; i++) {
			alpha.add(zero);
			zero++;
		}
		alpha.add(',');
		alpha.add('.');
		alpha.add('?');
		alpha.add('!');
		alpha.add(' ');
		menu(alpha);
	}

	public static int getN(String s, char c) {
		int n = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c)
				n++;
		}
		return n;
	}

	public static double getIndex(double[] array) {
		if (array == null || array.length == 0)
			return -1; // null or empty

		int largest = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[largest])
				largest = i;
		}
		return largest; // position of the first largest found
	}

	static char getMax(String str) {
		// Create array to keep the count of individual
		// characters and initialize the array as 0
		int count[] = new int[256];

		// Construct character count array from the input
		// string.
		int len = str.length();
		for (int i = 0; i < len; i++)
			count[str.charAt(i)]++;

		int max = -1; // Initialize max count
		char result = ' '; // Initialize result

		// Traversing through the string and maintaining
		// the count of each character
		for (int i = 0; i < len; i++) {
			if (max < count[str.charAt(i)]) {
				max = count[str.charAt(i)];
				result = str.charAt(i);
			}
		}

		return result;
	}

	public static void menu(ArrayList<Character> alpha) throws FileNotFoundException {
		Scanner kb = new Scanner(System.in);
		int choice = 0;
		Scanner scan = null;
		PrintWriter pwriter = null;
		String newline = System.getProperty("line.separator");
		do {
			System.out.println("VigenereCipher menu: ");
			System.out.println("1- Encryption");
			System.out.println("2- Decryption");
			System.out.print("Please enter your choice: ");
			choice = kb.nextInt();
			if (choice == 1) {
				// Encryption txt file will be the input
				// Decryption dec file will be the output
				FileInputStream input = new FileInputStream("Encryption.txt");
				FileOutputStream output = new FileOutputStream("Decrypted.dec");
				scan = new Scanner(input);
				pwriter = new PrintWriter(output);
				String key = scan.nextLine();
				String str = "";
				while (scan.hasNext()) {
					str = scan.nextLine();
					if (str != null && str.length() > 0) {
						System.out.println(Encryption(input, str, output, alpha, key));
						pwriter.println(Encryption(input, str, output, alpha, key));
					} else {
						System.out.println("");
						pwriter.println("");
						continue;
					}
				}
				pwriter.close();
			} else if (choice == 2) {
				// Dectypted.dec file will be the input (we will decrypt it)
				// Encrypted.txt file will be the output (the decrypted file)
				FileInputStream input = new FileInputStream("Decrypted.dec");
				FileOutputStream output = new FileOutputStream("Encrypted.txt");
				// FileInputStream input = new FileInputStream("encrypted2.dec");
				// FileOutputStream output = new FileOutputStream("encrypted2.txt");
				scan = new Scanner(input);
				pwriter = new PrintWriter(output);
				System.out.print("Enter the encryption Key: ");
				kb.nextLine();
				String key = kb.nextLine();
				pwriter.println(KeyEncryption(key, alpha));
				System.out.println("Decryption key is: " + KeyEncryption(key, alpha));
				String str = "";
				System.out.println("Decrypted message using decrypted key is: ");
				while (scan.hasNextLine()) {
					str = scan.nextLine();
					if (str != null && str.length() > 0) {
						System.out.println(Decryption(input, str, output, alpha, key));
						pwriter.println(Decryption(input, str, output, alpha, key));
					} else {
						System.out.println("");
						pwriter.println("");
						continue;
					}
				}
				scan.close();
				pwriter.close();
			} else
				System.out.println("Wrong choice!");
			System.out.println("---------------------------");
		} while (choice == 1 || choice == 2);
		scan.close();
		pwriter.close();
	}

	public static String Encryption(FileInputStream input, String str, FileOutputStream output,
			ArrayList<Character> alpha, String key) throws FileNotFoundException {
		int keyLength = key.length();
		int[] keyIndex = new int[keyLength];
		for (int i = 0; i < keyLength; i++) {
			keyIndex[i] = alpha.indexOf(key.charAt(i));
		}
		String FinalEncrypt = "";
		int numberOfWords = (int) Math.ceil(str.length() / (keyLength + 0.0));
		int remainder = str.length() % keyLength;
		boolean s = true;
		while (s) {
			for (int i = 0; i < numberOfWords; i++) {
				if (i == (numberOfWords - 1) && (keyLength != str.length())) {
					s = false;
					for (int j = 0; j < remainder; j++) {
						int index = alpha.indexOf(str.charAt(j));
						int newIndex = (index + keyIndex[j]) % 67;
						char newLetter = alpha.get(newIndex);
						FinalEncrypt = FinalEncrypt + newLetter;
					}
					continue;
				} else if (i == (numberOfWords - 1))
					s = false;
				for (int j = 0; j < keyLength; j++) {
					int index = alpha.indexOf(str.charAt(j));
					int newIndex = (index + keyIndex[j]) % 67;
					char newLetter = alpha.get(newIndex);
					FinalEncrypt = FinalEncrypt + newLetter;
				}
				str = str.substring(keyLength);

			}

		}
		return FinalEncrypt;
	}

	public static String Decryption(FileInputStream input, String str, FileOutputStream output,
			ArrayList<Character> alpha, String key) throws FileNotFoundException {
		int keyLength = key.length();
		int[] keyIndex = new int[keyLength];
		for (int i = 0; i < keyLength; i++) {
			keyIndex[i] = alpha.indexOf(key.charAt(i));
		}
		String FinalEncrypt = "";
		int numberOfWords = (int) Math.ceil(str.length() / (keyLength + 0.0));
		int remainder = str.length() % keyLength;
		boolean s = true;
		while (s) {
			for (int i = 0; i < numberOfWords; i++) {
				if (i == (numberOfWords - 1) && (keyLength != str.length())) {
					s = false;
					for (int j = 0; j < remainder; j++) {
						int index = alpha.indexOf(str.charAt(j));
						int newIndex = (index - keyIndex[j]) % 67;
						if (newIndex < 0)
							newIndex += 67;
						char newLetter = alpha.get(newIndex);
						FinalEncrypt = FinalEncrypt + newLetter;
					}
					continue;
				} else if (i == (numberOfWords - 1))
					s = false;
				for (int j = 0; j < keyLength; j++) {
					int index = alpha.indexOf(str.charAt(j));
					int newIndex = (index - keyIndex[j]) % 67;
					if (newIndex < 0)
						newIndex += 67;
					char newLetter = alpha.get(newIndex);
					FinalEncrypt = FinalEncrypt + newLetter;
				}
				str = str.substring(keyLength);

			}

		}
		return FinalEncrypt;
	}

	public static String oneEncryption(String str, ArrayList<Character> alpha, int[] keyIndex, String key, int i) {
		String FinalEncrypt = "";
		int index = alpha.indexOf(str.charAt(i));
		int newIndex = (index - keyIndex[i]) % 67;
		if (newIndex < 0)
			newIndex += 67;
		char newLetter = alpha.get(newIndex);
		FinalEncrypt = FinalEncrypt + newLetter;
		return FinalEncrypt;
	}

	public static String enLetter(ArrayList<Character> alpha, char en, char de) {

		return "";
	}

	public static String KeyEncryption(String key, ArrayList<Character> alpha) {
		int keyLength = key.length();
		int[] keyIndex = new int[keyLength];
		for (int i = 0; i < keyLength; i++) {
			keyIndex[i] = alpha.indexOf(key.charAt(i));
		}
		String tempEncrypt = "";
		String FinalEncrypt = "";
		for (int j = 0; j < keyLength; j++) {
			int index = alpha.indexOf(key.charAt(j));
			int newIndex = (67 - index) % 67;
			char newLetter = alpha.get(newIndex);
			FinalEncrypt = FinalEncrypt + newLetter;
		}
		FinalEncrypt = FinalEncrypt + tempEncrypt;
		key = key.substring(keyLength);
		return FinalEncrypt;
	}
}
