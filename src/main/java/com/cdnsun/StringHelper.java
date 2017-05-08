package com.cdnsun;

public class StringHelper
{

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes)
    {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] hexToBytes(String string) {
        int len = string.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i+1), 16));
        }

        return bytes;
    }

    public static String join(String delimiter, String[] strings) {
        String result = "";
        for (String string: strings) {
            if (result.length() > 0) {
                result = result.concat(delimiter);
            }

            result = result.concat(string);
        }

        return result;
    }

}

