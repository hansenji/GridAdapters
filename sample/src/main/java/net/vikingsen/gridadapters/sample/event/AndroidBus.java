package net.vikingsen.gridadapters.sample.event;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by Jordan Hansen
 */
public class AndroidBus extends Bus {

    private static AndroidBus bus;

    private final Handler mainThread = new Handler(Looper.getMainLooper());

    public static Bus getBus() {
        if (bus == null) {
            bus = new AndroidBus();
        }
        return bus;
    }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    post(event);
                }
            });
        }
    }
}
