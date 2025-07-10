package nro.networknew.session;

import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import nro.data.DataGame;
import nro.jdbc.daos.GodGK;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.networknew.io.Message;
import nro.networknew.session.Session;
import nro.server.Client;
import nro.server.Controller;
import nro.server.Maintenance;
import nro.server.Manager;
import nro.server.model.AntiLogin;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.Util;

public class MySession extends Session {

    private static final Map<String, AntiLogin> ANTILOGIN = new HashMap<>();
    private static final ConcurrentHashMap<String, Object> soulmateLocks = new ConcurrentHashMap<>();
    public Player player;

    public byte timeWait = 100;

    public boolean connected;
    public boolean sentKey;

    public static final byte[] KEYS = {0};
    public byte curR, curW;

    public String ipAddress;
    public boolean isAdmin;
    public boolean isAdmin1;
    public int userId;
    public String uu;
    public String pp;

    public int typeClient;
    public byte zoomLevel;

    public long lastTimeLogout;
    public boolean joinedGame;

    public long lastTimeReadMessage;

    public boolean actived;

    public boolean nhanvang;
    public boolean nhanngocxanh;
    public boolean nhanngochong;
    public int vnd;
    public int totalvnd;
    public int totalvnd2;
    public int totalvnd3;
    public int sk20_10;
    public int NHS;
    public int SKHE;

    public boolean vippro;

    public int goldBar;

    public int coinBar;
    public List<Item> itemsReward;
    public String dataReward;
    public boolean is_gift_box;
    public double bdPlayer;

    public int version;

    public int Bar;
//    public boolean isRIcon;

    public MySession(Socket socket) {
        super(socket);
        try {
            socket.setTcpNoDelay(true);
        } catch (SocketException ex) {
            java.util.logging.Logger.getLogger(MySession.class.getName()).log(Level.SEVERE, "Error setting socket options", ex);
        }
        ipAddress = socket.getInetAddress().getHostAddress();
        Logger.success("Login success on port " + socket.getPort() + " with IP address: " + ipAddress + "\n");
    }

    @Override
    public void sendKey() throws Exception {
        super.sendKey();
        this.startSend();
    }

    public void sendSessionKey() {
        Message msg = new Message(-27);
        try {
            msg.writer().writeByte(KEYS.length);
            msg.writer().writeByte(KEYS[0]);
            for (int i = 1; i < KEYS.length; i++) {
                msg.writer().writeByte(KEYS[i] ^ KEYS[i - 1]);
            }
            this.sendMessage(msg);
            msg.cleanup();
            sentKey = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {
//        this.sessionActive = true;
        AntiLogin al = ANTILOGIN.get(this.ipAddress);
        if (al == null) {
            al = new AntiLogin();
            ANTILOGIN.put(this.ipAddress, al);
        }
        if (!al.canLogin()) {
            Service.gI().sendThongBaoOK(this, al.getNotifyCannotLogin());
            return;
        }
        if (Manager.LOCAL) {
            Service.gI().sendThongBaoOK(this, "Server này chỉ để lưu dữ liệu\nVui lòng qua server khác");
            return;
        }

        if (Maintenance.isRuning) {
            Service.gI().sendThongBaoOK(this, "Server đang trong thời gian bảo trì, vui lòng quay lại sau");
            return;
        }
        if (!this.isAdmin && Client.gI().getPlayers().size() >= Manager.MAX_PLAYER) {
            Service.gI().sendThongBaoOK(this, "Máy chủ hiện đang quá tải, "
                    + "cư dân vui lòng di chuyển sang máy chủ khác.");
            return;
        }
        Object lock = soulmateLocks.computeIfAbsent(username, k -> new Object());
        synchronized (lock) {
            if (this.player != null) {
                return;
            } else {
                Player player = null;
                try {
                    long st = System.currentTimeMillis();
                    this.uu = username;
                    this.pp = password;

                    player = GodGK.login(this, al);
                    if (player != null) {

                        // -77 max small
                        DataGame.sendSmallVersion(this);

                        this.timeWait = 0;
                        this.joinedGame = true;
                        player.nPoint.calPoint();
                        player.nPoint.setHp(Util.maxIntValue(player.nPoint.hp));
                        player.nPoint.setMp(Util.maxIntValue(player.nPoint.mp));
                        player.zone.addPlayer(player);
                        if (player.pet != null) {
                            player.pet.nPoint.calPoint();
                            player.pet.nPoint.setHp(Util.maxIntValue(player.pet.nPoint.hp));
                            player.pet.nPoint.setMp(Util.maxIntValue(player.pet.nPoint.mp));
                        }

                        player.setSession(this);
                        Client.gI().put(player);
                        this.player = player;
                        //-28 -4 version data game
                        DataGame.sendVersionGame(this);
                        //-31 data item background
                        DataGame.sendDataItemBG(this);
                        Controller.getInstance().sendInfo(this);
                        Logger.warning("Login thành công player " + this.player.name + ": " + (System.currentTimeMillis() - st) + " ms\n");
//                    Service.gI().sendThongBaoOK(this, "Ngọc rồng sao đen sẽ mở lúc 21h hôm nay");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (player != null) {
                        player.dispose();
                    }
                }
            }
        }
    }
}
