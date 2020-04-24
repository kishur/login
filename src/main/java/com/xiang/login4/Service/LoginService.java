package com.xiang.login4.Service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
	
	private String publicKeyString, privateKeyString;
	final Base64.Decoder decoder = Base64.getDecoder();
	final Base64.Encoder encoder = Base64.getEncoder();

	public LoginService() throws NoSuchAlgorithmException {
		this.genKeyPair();
	}

	private void genKeyPair() throws NoSuchAlgorithmException {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024, new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
		this.publicKeyString = new String(encoder.encodeToString(publicKey.getEncoded()));
		// 得到私钥字符串
		this.privateKeyString = new String(encoder.encodeToString(privateKey.getEncoded()));
	}

	// 获得一个公钥
	public String getPublicKey() {
		return this.publicKeyString;
	}

	// 私钥解密
	public String decodePassWd(String rsaPw) throws Exception {
		// 64位解码加密后的字符串
		byte[] inputByte = decoder.decode(rsaPw.getBytes("UTF-8"));
		// base64编码的私钥
		byte[] decoded = decoder.decode(this.privateKeyString);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
				.generatePrivate(new PKCS8EncodedKeySpec(decoded));
		// RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
}