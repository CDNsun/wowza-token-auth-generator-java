package com.cdnsun;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AuthTokenGenerator
{

    final public static String ENC_METHOD = "Blowfish";
    final public static String ENC_METHOD_DETAILED = "Blowfish/ECB/PKCS5Padding";

    private Cipher cipher;

    public AuthTokenGenerator()
        throws AuthTokenGeneratorException
    {
        try {
            this.cipher = Cipher.getInstance(ENC_METHOD_DETAILED);
        } catch (Exception e) {
            throw new AuthTokenGeneratorException("Encryption method is not supported.");
        }
    }

    public String encrypt(String key, AuthTokenParameters params)
        throws AuthTokenGeneratorException
    {
        String token = "";

        if (0 < params.getExpire().length()) {
            token = "expire=".concat(params.getExpire());
        }

        if (0 < params.getRefsAllow().length) {
            token = token.concat(
                ((0 < token.length())? "&": "")
                + "ref_allow=".concat(StringHelper.join(",", params.getRefsAllow())));
        }

        if (0 < params.getRefsDeny().length) {
            token = token.concat(
                ((0 < token.length())? "&": "")
                + "ref_deny=".concat(StringHelper.join(",", params.getRefsDeny())));
        }

        byte[] tokenBytes = token.getBytes();

        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), ENC_METHOD));

            byte[] encTokenBytes = this.cipher.doFinal(token.getBytes());

            return StringHelper.bytesToHex(encTokenBytes).toLowerCase();
        } catch (Exception e) {
            throw new AuthTokenGeneratorException(e.getMessage());
        }
    }

    public AuthTokenParameters decrypt(String key, String encToken)
        throws AuthTokenGeneratorException
    {
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), ENC_METHOD));

            byte[] tokenBytes = this.cipher.doFinal(StringHelper.hexToBytes(encToken));

            return AuthTokenParameters.parse(new String(tokenBytes));
        } catch (Exception e) {
            throw new AuthTokenGeneratorException(e.getMessage());
        }
    }

}

