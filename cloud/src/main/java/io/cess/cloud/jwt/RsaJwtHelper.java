/*
 * Copyright 2006-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.cess.cloud.jwt;

import static org.springframework.security.jwt.EccJwtAlgorithms.sigAlg;
import static org.springframework.security.jwt.codec.Codecs.b64UrlDecode;
import static org.springframework.security.jwt.codec.Codecs.b64UrlEncode;
import static org.springframework.security.jwt.codec.Codecs.concat;
import static org.springframework.security.jwt.codec.Codecs.utf8Decode;
import static org.springframework.security.jwt.codec.Codecs.utf8Encode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.security.*;
import java.security.interfaces.*;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.cess.util.ByteUtil;
import io.cess.util.Ecc;
import io.cess.util.JsonUtil;
import io.cess.util.Rsa;
import org.springframework.security.jwt.BinaryFormat;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Luke Taylor
 * @author Dave Syer
 */
public class RsaJwtHelper {
	static byte[] PERIOD = utf8Encode(".");

	/**
	 * Creates a token from an encoded token string.
	 *
	 * @param token the (non-null) encoded token (three Base-64 encoded strings separated
	 * by "." characters)
	 */
	public static Jwt decode(String token, RSAPublicKey publicKey,RSAPrivateKey privateKey) throws BadPaddingException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchProviderException, InvalidKeyException, IOException {

		byte[] bytes = Rsa.decrypt(Base64.getDecoder().decode(token),publicKey);

//		int headerCount = bytes[1] + 256 * bytes[0];
//
//		int claimeCount = bytes[3] + 256 * bytes[2];

		int headerCount = ByteUtil.readShort(bytes);
		int claimeCount = ByteUtil.readShort(bytes,2);


		RsaJwtHeader header = RsaJwtHeaderHelper.create(new String(bytes,4,headerCount,"utf-8"));

		byte[] claims = new byte[claimeCount];

		ByteUtil.writeBytes(claims,bytes,claimeCount,0,4+headerCount);

//		claims = Base64.getDecoder().decode(claims);

		byte[] crypto = new byte[bytes.length - claimeCount - headerCount - 4];
		ByteUtil.writeBytes(crypto,bytes,crypto.length,0,4+headerCount+claimeCount);

//		crypto = Base64.getDecoder().decode(crypto);

		return new RsaJwtImpl(privateKey,header, claims, crypto);

//		int firstPeriod = token.indexOf('.');
//		int lastPeriod = token.lastIndexOf('.');
//
//		if (firstPeriod <= 0 || lastPeriod <= firstPeriod) {
//			throw new IllegalArgumentException("JWT must have 3 tokens");
//		}
//		CharBuffer buffer = CharBuffer.wrap(token, 0, firstPeriod);
//		// TODO: Use a Reader which supports CharBuffer
//		EccJwtHeader header = EccJwtHeaderHelper.create(buffer.toString());
//
//		buffer.limit(lastPeriod).position(firstPeriod + 1);
//		byte[] claims = b64UrlDecode(buffer);
//		boolean emptyCrypto = lastPeriod == token.length() - 1;
//
//		byte[] crypto;
//
//		if (emptyCrypto) {
//			if (!"none".equals(header.parameters.alg)) {
//				throw new IllegalArgumentException(
//						"Signed or encrypted token must have non-empty crypto segment");
//			}
//			crypto = new byte[0];
//		}
//		else {
//			buffer.limit(token.length()).position(lastPeriod + 1);
//			crypto = b64UrlDecode(buffer);
//		}
//		return new EccJwtImpl(header, claims, crypto);
	}

//	public static Jwt decodeAndVerify(String token, SignatureVerifier verifier) {
//		Jwt jwt = decode(token);
//		jwt.verifySignature(verifier);
//
//		return jwt;
//	}
//	public static Jwt decode(String token, ECPublicKey publicKey) {
//		Jwt jwt = decode(token);
//		jwt.verifySignature(verifier);
//
//		return jwt;
//	}
	
//	public static Map<String, String> headers(String token) {
//		EccJwtImpl jwt = (EccJwtImpl) decode(token);
//		Map<String, String> map = new LinkedHashMap<String, String>(jwt.header.parameters.map);
//		map.put("alg", jwt.header.parameters.alg);
//		if (jwt.header.parameters.typ!=null) {
//			map.put("typ", jwt.header.parameters.typ);
//		}
//		return map;
//	}

	public static Jwt encode(CharSequence content, RSAPrivateKey privateKey) throws NoSuchAlgorithmException {
		RsaJwtHeader header = RsaJwtHeaderHelper.create(Collections.<String, String>emptyMap());
		byte[] claims = utf8Encode(content);

		MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

		byte[] headers = header.bytes();

		byte[] bs = new byte[claims.length + headers.length];

		ByteUtil.writeBytes(bs,headers,headers.length);
		ByteUtil.writeBytes(bs,claims,claims.length,headers.length,0);

		messageDigest.update(bs);
		byte[] crypto = messageDigest.digest();

		return new RsaJwtImpl(privateKey,header,claims,crypto);
	}

//	public static Jwt encode(CharSequence content, Signer signer) {
//		return encode(content, signer, Collections.<String, String>emptyMap());
//	}
//
//	public static Jwt encode(CharSequence content, Signer signer,
//			Map<String, String> headers) {
//		EccJwtHeader header = EccJwtHeaderHelper.create(signer, headers);
//		byte[] claims = utf8Encode(content);
//		byte[] crypto = signer
//				.sign(concat(b64UrlEncode(header.bytes()), PERIOD, b64UrlEncode(claims)));
//		return new EccJwtImpl(header, claims, crypto);
//	}
}

/**
 * Helper object for JwtHeader.
 *
 * Handles the JSON parsing and serialization.
 */
class RsaJwtHeaderHelper {

	static RsaJwtHeader create(String header) {
//		byte[] bytes = b64UrlDecode(header);
		byte[] bytes = header.getBytes();
		return new RsaJwtHeader(bytes, parseParams(bytes));
	}

	static RsaJwtHeader create(Map<String, String> params) {
		Map<String, String> map = new LinkedHashMap<String, String>(params);
		map.put("alg", "ECC");
		RsaHeaderParameters p = new RsaHeaderParameters(map);
		return new RsaJwtHeader(serializeParams(p), p);
	}

	static RsaHeaderParameters parseParams(byte[] header) {
		Map<String, String> map = parseMap(utf8Decode(header));
		return new RsaHeaderParameters(map);
	}

	private static Map<String, String> parseMap(String json) {
		if (json != null) {
			json = json.trim();
			if (json.startsWith("{")) {
				return parseMapInternal(json);
			}
			else if (json.equals("")) {
				return new LinkedHashMap<String, String>();
			}
		}
		throw new IllegalArgumentException("Invalid JSON (null)");
	}

	private static Map<String, String> parseMapInternal(String json) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		json = trimLeadingCharacter(trimTrailingCharacter(json, '}'), '{');
		for (String pair : json.split(",")) {
			String[] values = pair.split(":");
			String key = strip(values[0], '"');
			String value = null;
			if (values.length > 0) {
				value = strip(values[1], '"');
			}
			if (map.containsKey(key)) {
				throw new IllegalArgumentException("Duplicate '" + key + "' field");
			}
			map.put(key, value);
		}
		return map;
	}

	private static String strip(String string, char c) {
		return trimLeadingCharacter(trimTrailingCharacter(string.trim(), c), c);
	}

	private static String trimTrailingCharacter(String string, char c) {
		if (string.length() >= 0 && string.charAt(string.length() - 1) == c) {
			return string.substring(0, string.length() - 1);
		}
		return string;
	}

	private static String trimLeadingCharacter(String string, char c) {
		if (string.length() >= 0 && string.charAt(0) == c) {
			return string.substring(1);
		}
		return string;
	}

	private static byte[] serializeParams(RsaHeaderParameters params) {
		StringBuilder builder = new StringBuilder("{");

		appendField(builder, "alg", params.alg);
		if (params.typ != null) {
			appendField(builder, "typ", params.typ);
		}
		for (Entry<String, String> entry : params.map.entrySet()) {
			appendField(builder, entry.getKey(), entry.getValue());
		}
		builder.append("}");
		return utf8Encode(builder.toString());

	}

	private static void appendField(StringBuilder builder, String name, String value) {
		if (builder.length() > 1) {
			builder.append(",");
		}
		builder.append("\"").append(name).append("\":\"").append(value).append("\"");
	}
}

/**
 * Header part of JWT
 *
 */
class RsaJwtHeader implements BinaryFormat {
	private final byte[] bytes;

	final RsaHeaderParameters parameters;

	/**
	 * @param bytes the decoded header
	 * @param parameters the parameter values contained in the header
	 */
	RsaJwtHeader(byte[] bytes, RsaHeaderParameters parameters) {
		this.bytes = bytes;
		this.parameters = parameters;
	}

	@Override
	public byte[] bytes() {
		return bytes;
	}

	@Override
	public String toString() {
		return utf8Decode(bytes);
	}
}

class RsaHeaderParameters {
	final String alg;

	final Map<String, String> map;

	final String typ = "JWT";

	RsaHeaderParameters(String alg) {
		this(new LinkedHashMap<String, String>(Collections.singletonMap("alg", alg)));
	}

	RsaHeaderParameters(Map<String, String> map) {
		String alg = map.get("alg"), typ = map.get("typ");
		if (typ != null && !"JWT".equalsIgnoreCase(typ)) {
			throw new IllegalArgumentException("typ is not \"JWT\"");
		}
		map.remove("alg");
		map.remove("typ");
		this.map = map;
		if (alg == null) {
			throw new IllegalArgumentException("alg is required");
		}
		this.alg = alg;
	}

}

class RsaJwtImpl implements Jwt {
	final RsaJwtHeader header;

	private final byte[] content;

	private final byte[] crypto;

	private String claims;

	private RSAPrivateKey privateKey;

	/**
	 * @param header the header, containing the JWS/JWE algorithm information.
	 * @param content the base64-decoded "claims" segment (may be encrypted, depending on
	 * header information).
	 * @param crypto the base64-decoded "crypto" segment.
	 */
	RsaJwtImpl(RSAPrivateKey privateCrtKey, RsaJwtHeader header, byte[] content, byte[] crypto) {
		this.header = header;
		this.content = content;
		this.crypto = crypto;
		claims = utf8Decode(content);
		this.privateKey = privateCrtKey;
	}

	/**
	 * Validates a signature contained in the 'crypto' segment.
	 *
	 * @param verifier the signature verifier
	 */
	@Override
	public void verifySignature(SignatureVerifier verifier) {
		verifier.verify(signingInput(), crypto);
	}

	private byte[] signingInput() {
		return concat(b64UrlEncode(header.bytes()), EccJwtHelper.PERIOD,
				b64UrlEncode(content));
	}

	/**
	 * Allows retrieval of the full token.
	 *
	 * @return the encoded header, claims and crypto segments concatenated with "."
	 * characters
	 */
	@Override
	public byte[] bytes() {
		byte[] headers = header.bytes();
		byte[] bs = new byte[headers.length+content.length+crypto.length+4];
		bs[0] = (byte) (headers.length / 256);
		bs[1] = (byte) (headers.length % 256);
		bs[2] = (byte) (content.length / 256);
		bs[3] = (byte) (content.length % 256);

		ByteUtil.writeBytes(bs,headers,headers.length,4,0);
		ByteUtil.writeBytes(bs,content,content.length,4+headers.length,0);
		ByteUtil.writeBytes(bs,crypto,crypto.length,4+headers.length+content.length,0);

		try {
			return Rsa.encrypt(bs,this.privateKey);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

//		return concat(b64UrlEncode(header.bytes()), EccJwtHelper.PERIOD,
//				b64UrlEncode(content), EccJwtHelper.PERIOD, b64UrlEncode(crypto));
	}

	@Override
	public String getClaims() {
		return utf8Decode(content);
	}

	@Override
	public String getEncoded() {
		return Base64.getEncoder().encodeToString(bytes());
	}
	
	public RsaJwtHeader header() {
		return this.header;
	}

	@Override
	public String toString() {
		return header + " " + claims + " [" + crypto.length + " crypto bytes]";
	}
}
