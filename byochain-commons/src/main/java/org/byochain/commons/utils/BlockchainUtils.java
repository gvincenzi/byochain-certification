package org.byochain.commons.utils;

import java.security.MessageDigest;

/**
 * Utility class
 * @author Giuseppe Vincenzi
 *
 */
public class BlockchainUtils {
	private static final char ZERO = '0';
	private static final String UTF_8 = "UTF-8";
	private static final String ALGORITHM = "SHA-256";

	/**
	 * Method to apply the SHA-256 algorithm to a string
	 * @param input String ti encode
	 * @return encoded String
	 */
	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
			byte[] hash = digest.digest(input.getBytes(UTF_8));
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append(ZERO);
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
