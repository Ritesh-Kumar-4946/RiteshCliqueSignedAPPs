package com.ritesh.clique;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ritesh on 27/9/16.
 */
public class PasswordValidator {

    private Pattern pattern;
    private Matcher matcher;

  //  private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})";
    private static final String PASSWORD_PATTERN = "((^.*(?=.{10,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$))";

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    public boolean validate(final String password) {

        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}
