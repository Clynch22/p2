package com.example.solution_color;

import java.util.Random;

/**
 * Created by CodyADMIN on 2/21/2016.
 */
public class Constants{
   public static Random rand = new Random();
    public static final int TAKE_PICTURE = 1;
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCELED = 1;
    private static final String DEF_PREF_FILE_NAME = "PrefFile";
    public static final String DEF_SUBJECT_TEXT = "Subject text";
    public static final String DEF_MESSAGE_TEXT = "Message text";
    public static final String PREF_NAME = "MyPrefsFile";
    public static final String makeFloat = rand.nextInt(255 - 0) + 1 + ".0";
    public static final int randColor = rand.nextInt(99);
    public static final float floatOf = Float.parseFloat(makeFloat);
    private Constants() {};
}
