package com.khasna.cooker.Common;

import android.util.Log;

/**
 * Created by nachiket on 4/5/2017.
 */

public class DebugClass {

    private static final boolean debug = false;
    private static final boolean delete_user = false;

    public static void DebugPrint( String tag, String message)
    {
        if (debug)
        {
            Log.d(tag, message);
        }
    }

    public static boolean IsDelete()
    {
        return delete_user;
    }
}
