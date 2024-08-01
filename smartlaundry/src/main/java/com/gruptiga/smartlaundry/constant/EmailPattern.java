package com.gruptiga.smartlaundry.constant;

import java.util.regex.Pattern;

public class EmailPattern {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public static final Pattern PHONE_PATTERN = Pattern.compile("^08\\d{9,11}$");
}
