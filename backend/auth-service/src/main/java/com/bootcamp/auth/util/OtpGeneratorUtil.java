package com.bootcamp.auth.util;

import java.util.Random;

public class OtpGeneratorUtil {

    public static String generateOtp() {

        Random random = new Random();

        int otp =
                100000 + random.nextInt(900000);

        return String.valueOf(otp);

    }

}