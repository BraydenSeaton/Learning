package common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public final class Program {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
			IOException, InvalidAlgorithmParameterException {
		String number_value = "1";
		Integer number = 1;

		String value_to_encrypt = "This is a test";

		// Generate key
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		SecretKey aesKey = kgen.generateKey();

		String encrypted_value = encrypt(value_to_encrypt, aesKey);

		String decrypted_value = decrypt(encrypted_value, aesKey);

		String we_expect_it_to_equal = "This is a test";

		System.out.println(value_to_encrypt.equals(we_expect_it_to_equal));
	}

	private static String decrypt(String encrypted_value, SecretKey aesKey) throws IOException, InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		// Decrypt cipher
		Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
		decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);

		// Decrypt
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayInputStream inStream = new ByteArrayInputStream(encrypted_value.getBytes());
		CipherInputStream cipherInputStream = new CipherInputStream(inStream, decryptCipher);
		byte[] buf = new byte[1024];
		int bytesRead;
		while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
			outputStream.write(buf, 0, bytesRead);
		}
		return new String(outputStream.toByteArray());
	}

	private static String encrypt(String value_to_encrypt, SecretKey aesKey2)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
		// Generate key
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		SecretKey aesKey = kgen.generateKey();

		// Encrypt cipher
		Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);

		// Encrypt
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
		cipherOutputStream.write(value_to_encrypt.getBytes());
		cipherOutputStream.flush();
		cipherOutputStream.close();
		byte[] encryptedBytes = outputStream.toByteArray();
		return new String(encryptedBytes);
	}

}
