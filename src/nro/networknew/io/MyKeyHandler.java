package nro.networknew.io;

import nro.data.DataGame;
import nro.interfaces.ISession;
import nro.networknew.session.MySession;


public class MyKeyHandler extends KeyHandler {

    @Override
    public void sendKey(ISession session) {
        super.sendKey(session);
        DataGame.sendDataImageVersion((MySession) session);
        DataGame.sendVersionRes((MySession) session);
    }

}
