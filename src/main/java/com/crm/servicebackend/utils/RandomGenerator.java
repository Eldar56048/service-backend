package com.crm.servicebackend.utils;

import org.apache.commons.text.RandomStringGenerator;

public class RandomGenerator {
    public static String generate(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
                .build();
        return pwdGenerator.generate(length);
    }
}
