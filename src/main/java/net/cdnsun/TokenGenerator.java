package net.cdnsun;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TokenGenerator {

    public static void main(String[] args) {
        try {
            String token = run(args);
            System.out.println(token);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String run(String[] args) throws ParseException, NoSuchAlgorithmException, IOException {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, checkArguments(args));

        String scheme = checkOptionValue(cmd.getOptionValue("s", "http"));

        String resource = checkOptionValue(cmd.getOptionValue("r"));
        if (resource == null) {
            throw new IllegalArgumentException("Resource hostname is not specified!");
        }

        String path = checkOptionValue(cmd.getOptionValue("p"));
        if (path == null) {
            throw new IllegalArgumentException("Path is not specified!");
        }

        String key = checkOptionValue(cmd.getOptionValue("k"));
        if (key == null) {
            throw new IllegalArgumentException("Key is not specified!");
        }

        String expires = checkOptionValue(cmd.getOptionValue("e", ""));
        String ip = checkOptionValue(cmd.getOptionValue("i", ""));

        return generateToken(resource, path, key, scheme, expires, ip);
    }

    private static String[] checkArguments(String[] args) {
        if (args != null && args.length != 0) {
            String lastArgument = args[args.length - 1];
            if (lastArgument != null && lastArgument.charAt(lastArgument.length() - 1) == ';') {
                lastArgument = lastArgument.substring(0, lastArgument.length() - 1);
            }
            args[args.length - 1] = lastArgument;
        }
        return args;
    }

    private static Options createOptions() {
        Options options = new Options();
        options.addOption(new Option("r", "resource", true, "CDN service domain. e.g. example: '12345.r.cdnsun.net'"));
        options.addOption(new Option("p", "path", true, "File path of the CDN resource as part of token key. e.g. /, /files, /files/file.html"));
        options.addOption(new Option("k", "key", true, "The URL signing key"));
        options.addOption(new Option("s", "scheme", true, "The scheme for CDN Resource URL. e.g. http or https"));
        options.addOption(new Option("e", "expires", true, "The expiration of the URL. This is in Unix timestamp format. This is optional."));
        options.addOption(new Option("i", "ip", true, "The IPs that allow to access. This is optional."));
        return options;
    }

    private static String generateToken(String resource, String path, String key, String scheme, String expires, String ip) throws NoSuchAlgorithmException {
        //1. Setup token key
        //1.1 append leading slash if missing
        if (path.charAt(0) != '/') {
            path = "/" + path;
        }

        //1.2 Extract uri, ignore query string arguments
        String url = resource.split("\\?")[0];

        //1.3 Formulate the token key
        String token = expires + path + key + ip;

        // 2. Setup URL
        // 2.1 Append argument - secure (compulsory)
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String url_secures = "?secure=" + Base64.getUrlEncoder().encodeToString(md5.digest(token.getBytes())).replace("+", "-").replace("/", "_").replace("=", "").split("\n")[0];

        //2.2 Append argument - expires

        if (!expires.isEmpty()) {
            expires = "&expires=" + expires;
        }

        // 2.3 Append argument - ip
        if (!ip.isEmpty()) {
            ip = "&ip=" + ip;
        }

        return scheme + "://" + url + path + url_secures + expires + ip;
    }

    private static String checkOptionValue(String option) {
        if (option != null && !option.isEmpty()) {
            if (option.charAt(0) == '\'') {
                option = option.substring(1, option.length());
            }
            if (option.charAt(option.length() - 1) == '\'') {
                option = option.substring(0, option.length() - 1);
            }
        }
        return option;
    }
}
