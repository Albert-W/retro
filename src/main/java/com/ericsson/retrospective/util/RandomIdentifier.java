package com.ericsson.retrospective.util;

import java.util.HashSet;
import java.util.Set;

public class RandomIdentifier {
    private RandomIdentifier(){}

    
    static final String LEXICON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    static final java.util.Random rand = new java.security.SecureRandom();

    
    static final Set<String> identifiers = new HashSet<>();

    public static String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(3)+3;
            for(int i = 0; i < length; i++) {
                builder.append(LEXICON.charAt(rand.nextInt(LEXICON.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
}
