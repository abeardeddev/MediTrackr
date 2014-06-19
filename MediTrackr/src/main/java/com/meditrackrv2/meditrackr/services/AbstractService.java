package com.meditrackrv2.meditrackr.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Andrew on 17/06/2014.
 */
public abstract class AbstractService implements Serializable, Runnable {

    private ArrayList<ServiceListener> listeners;
    private boolean error;

    public AbstractService()
    {
        listeners = new ArrayList<ServiceListener>();
    }

    public void addListeners(ServiceListener listener)
    {
        listeners.add(listener);
    }

    public void removeListeners(ServiceListener listener)
    {
        listeners.remove(listener);
    }

    public boolean hasError()
    {
        return error;
    }

    public void serviceComplete(boolean error)
    {
        this.error = error;

        Message m = _handler.obtainMessage();
        Bundle b = new Bundle();
        b.putSerializable("service", this);
        m.setData(b);
        _handler.sendMessage(m);

    }

    final Handler _handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            AbstractService service = (AbstractService)msg.getData().getSerializable("service");

            for(ServiceListener listener : service.listeners)
            {
                listener.ServiceComplete(service);
            }

        }

    };

}
