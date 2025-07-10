package nro.interfaces;

import nro.networknew.io.Message;

public interface IMessageHandler {

    void onMessage(final ISession p0, final Message p1) throws Exception;
}
