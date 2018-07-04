package com.khasna.cooker.Common;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Sourabh on 02/07/2018.
 */

public abstract class LocationResultReciever extends ResultReceiver {

    public LocationResultReciever(Handler handler) {
        super(handler);
    }
}
