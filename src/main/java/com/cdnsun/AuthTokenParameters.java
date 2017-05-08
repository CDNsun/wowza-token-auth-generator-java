package com.cdnsun;

public class AuthTokenParameters
{

    public static AuthTokenParameters parse(String token)
    {
        String[] tokenParts = token.split("&");

        String expire = "";
        String[] refsAllow = {};
        String[] refsDeny = {};

        for (String tokenPart: tokenParts) {
            String[] paramParts = tokenPart.split("=");

            if (paramParts.length == 2) {
                switch (paramParts[0]) {
                    case "expire":
                        expire = paramParts[1];
                        break;

                    case "ref_allow":
                        refsAllow = paramParts[1].split(",");
                        break;

                    case "ref_deny":
                        refsDeny = paramParts[1].split(",");
                        break;

                    default:
                        // Skip (or throw exception?)
                }
            }
        }

        return new AuthTokenParameters(expire, refsAllow, refsDeny);
    }

    protected String expire;
    protected String[] refsAllow;
    protected String[] refsDeny;

    public AuthTokenParameters(String expire, String[] refsAllow, String[] refsDeny)
    {
        this.expire = (null == expire)? "": expire;
        this.refsAllow = (null == refsAllow)? new String[0]: refsAllow;
        this.refsDeny = (null == refsDeny)? new String[0]: refsDeny;
    }

    public String getExpire()
    {
        return this.expire;
    }

    public String[] getRefsAllow()
    {
        return this.refsAllow;
    }

    public String[] getRefsDeny()
    {
        return this.refsDeny;
    }

}

