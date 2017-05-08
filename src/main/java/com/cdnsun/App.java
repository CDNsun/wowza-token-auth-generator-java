package com.cdnsun;

public class App
{

    private static void printUsage()
    {
        System.out.println("Usage: encrypt|decrypt key parameters|token");
    }

    public static void main( String[] args )
    {
        try {
            if (args.length < 3) {
                throw new Exception("Not enough arguments.");
            }

            String key = args[1];

            AuthTokenGenerator gen = new AuthTokenGenerator();

            switch (args[0]) {
                case "encrypt":
                    String encToken = gen.encrypt(key, AuthTokenParameters.parse(args[2]));

                    System.out.println("token=" + encToken);

                    break;

                case "decrypt":
                    AuthTokenParameters parameters = gen.decrypt(key, args[2]);

                    System.out.println("Expire: " + parameters.getExpire());
                    System.out.println("Refs Allow: " + StringHelper.join(", ", parameters.getRefsAllow()));
                    System.out.println("Refs Deny: " + StringHelper.join(", ", parameters.getRefsDeny()));

                    break;

                default:
                    printUsage();

                    System.out.println("ERROR: Invalid command.");
            }
        } catch (Exception e) {
            printUsage();

            System.out.println("ERROR: " + e.getMessage());
        }
    }

}

