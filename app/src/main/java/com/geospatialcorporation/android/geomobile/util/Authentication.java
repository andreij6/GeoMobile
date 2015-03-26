package com.geospatialcorporation.android.geomobile.util;

public class Authentication {
    private final String fixedKey = "oAbNi0ZTjacrnbY4kASQ2u3ZdSuWxBvebzelCvLo221Bc";
    private final String mobileKey1 = "+DVavtfkGddjrKXPrEP2YXQjjp4R52hcPyoyAnF1EK9LxBSvulrnHF5KVDE0PNZsmDnB0NHSSuDbmKyWYhlHg62rH00d5aaY/0FJjTIXoB/3z308OjNlut2ToJ7tJq60iQZKRlZ+dkZRirREhXQiz2yeG7WtXlbAFgdh11QomUnVdkCD7QBU0sRSsAJrlfhQh0oHICKhA3T42hMw89OulDN/WjngLPr17LzSklJ6Sql9LCM7PinQYCz6Vcwk2B55kzKBaTcImUPpmR09cjcJoVWeExwRn/gvir+Hhxzj788moF+65zFtos1TRkMlm70XLflE6zubZoGrVw5RJ1RL7mYArg3ybtV8CSSWaS9LR5SpCO68O5pJZrSKYBocEwbh6/YHLflGZ0Y/alDXzlsXeo0D0+hORJrPwvCBpeowsyZkhN+y5087m8YXQmFS0J81RxTdo1WiWzo8B60QqtpxJC37ZIwZumra/H11JVwbkJp5FAjxtgMpcsYezMp4PX2kjuyVYl1dys06BkH5v5DDSs5MsjipXSuACFh8hXb2ArXwPvDN2u3pz3wSgEFEj7hTeL+YdtJbwFzcAzJ4ioLV81AEfjAiQQfr3RqiuN8rKwsDskyFVYQvP7PlxwUzVVNsk+FSgmzwZ2cENmlbVUPMOnQXApwGxSC8YAU8zJHeIY3yyo5rFmoHS/c/x2HqbCNC+ZUv9hJMijI+y8MztVeWUSHoYwlMh9Rqa7ZwsrWvYGiknQHaqqC/pl/bYUHtim6GroTptEGLoXJ7XllATImxTZqypP6asbr9+2D/NB9CV0sx8GlzDX0r0o/ClWt0cjhpluBjIg3fXwOEjuFC9UMnRBgMLvwRfIOI/iQskmdAd/gTi03P+YZnQqSlg+4BQCjf9HjUvDbjGc31rTh73Y684AWReteC2Wbcd8eSKKgit+9wLFrkKxCqsBC6wDhzOLMvr2obWK80t9WTagLPDHCrK9MtCPOKgCpUvSOWjEKwzwcMndSD4mDUSIVBSy7BjMA1pBJpp/gzQVbK74ZzF7t5ErVneskaMOPy8pX8Va1cRlOBuaa+fdkIY8nxwWfakfJGv2vpdWDGOf7mcf3zZyIKl1JXiLbyCoDfqwTXnMzR3OfszW2ClH1IrZ+B61SWpll0qCld+/2CsiWExWqp9Gab+EgTHqXO6H30disO7sKyoxr+/9OmSvs+w9mKmmGDEWc0E1FbUCjNtTbS3QxuQ00kQw==";
    private final String mobileKey2 = "FJvcDaSRQ5sOnd3bbCA9pYhTe4hEj5WuCxYsKgAUDfY=";
    private final String dbAESIV = "JRzuilRwoQyfczQ/8A5Sqw==";
    private final String dbAESKey = "KdeexCPPO3N8T0OckBT2ykJH0+fkDmgpdvu5QPwhHSo=";

    public String getLoginAttemptId() {
        // postLoginStart("/API/Auth/Mobile/Start", fixedKey, loginAttemptIdComplete, true);

        return "";
    }

    public String afterGetLoginAttemptId() {
        /*
        aData = data.split(".");
        loginAttemptId = aData[0];
        saltBytes = Converter.base64ToBytes(aData[1]);

        var data = {
                LoginAttemptId: loginAttemptId,
                Username: username,
                Password: password
        };

        body = JSON.stringify(data);

        var encryptedBody = RSA.hex2Base64(RSA.encrypt(body, mobileKey1("Modulus"), mobileKey1("Exponent")));

        postLogin("/API/Auth/Mobile/Login", encryptedBody, completeLogin, loginFail);
        */

        return "";
    }

    public void Login(String Username, String Password) {
        //
    }

    public void GoogleLogin() {
        // postLogin("/API/Auth/Google", authToken, completeLogin, loginFail);
    }
}
