
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

	public static String encrypt(String key, String initVector, String value) {
		try {

			if(!isAESParametersValid(key,initVector,"Encrypt")){
				return  value;
			}
				// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");// "算法/模式/补码方式"0102030405060708
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
				byte[] encrypted = cipher.doFinal(value.getBytes());
				return new String(Base64.encodeBase64(encrypted));
			} catch (Exception ex) {
				LOGGER.error("Encrypt　data failed!" + ex.getMessage());
			}
			return value;
		}

	public static String decrypt(String key, String initVector, String encrypted) {
		try {
			
			if(!isAESParametersValid(key,initVector,"Decrypt")){
				return  encrypted;
			}
			
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted.getBytes("utf-8")));
			return new String(original);
		} catch (Exception ex) {
			LOGGER.error("Encrypt　data failed!" + ex.getMessage());
		}
		return encrypted;
	}

	private static boolean isAESParametersValid(String key, String initVector, String action) {
		// 判断Key是否正确
		if (key == null) {
			LOGGER.error(action + " data failed,for key is null");
			return false;
		}
		// 判断Key是否为16位
		if (key.length() != 16) {
			LOGGER.error(action + " data failed! Wrong AES Key length: must be 16 bytes long");
			return false;
		}
		// 判断initVector是否正确
		if (initVector == null) {
			LOGGER.error(action + " data failed,for initVector is null");
			return false;
		}
		// 判断initVector是否为16位
		if (initVector.length() != 16) {
			LOGGER.error(action + " data failed!Wrong IV length: must be 16 bytes long");
			return false;
		}
		return true;
	}

	public static void main(String... args) throws IOException {

		String dgl = "dongguoliang";
		String encryptData = encrypt(AgentConfig.AES_KEY, AgentConfig.IV,  dgl);
		System.out.println("加密后：" + encryptData);
		System.out.println("解密后：" + decrypt(AgentConfig.AES_KEY, AgentConfig.IV,  encryptData));
	}