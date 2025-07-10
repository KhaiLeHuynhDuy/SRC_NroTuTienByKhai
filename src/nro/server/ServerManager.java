package nro.server;

import EmtiManager.EmtiManager;
import com.girlkun.database.GirlkunDB;
import com.girlkun.result.GirlkunResultSet;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import nro.interfaces.ISession;
import nro.interfaces.ISessionAcceptHandler;
import nro.jdbc.daos.HistoryTransactionDAO;
import nro.kygui.ShopKyGuiManager;
import nro.map.daihoi.DaiHoiService;
import nro.models.boss.BossManager;
import nro.models.map.challenge.MartialCongressManager;
import nro.models.map.sieuhang.SieuHangManager;
import nro.models.matches.pvp.DaiHoiVoThuat;
import nro.models.player.Player;
import nro.networknew.Network;
import nro.networknew.io.MessageSendCollect;
import nro.networknew.io.MyKeyHandler;
import nro.networknew.session.MySession;
import nro.networknew.session.SessionManager;
import nro.services.ClanService;
import nro.services.NgocRongNamecService;
import nro.services.func.TopService;
import nro.sukienbanhchung.NauBanhServices;
import nro.utils.Logger;
import nro.utils.TimeUtil;

public class ServerManager {

    public static String timeStart;
    public Player pl;
    public MySession ss;
    public Socket soc;
    public static final Map CLIENTS = new HashMap();

    public static String NAME = "Girlkun75";
    public static int PORT = 8878;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    public void init() {
        Manager.gI();
        try {
            if (Manager.LOCAL) {
                return;
            }
            GirlkunDB.executeUpdate("update account set last_time_login = '2000-01-01', "
                    + "last_time_logout = '2001-01-01'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        HistoryTransactionDAO.deleteHistory();
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public long getNumPlayer() {
        long num = 0;
        try {
            GirlkunResultSet rs = GirlkunDB.executeQuery("SELECT COUNT(*) FROM `player`");
            rs.first();
            num = rs.getLong(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
        //new nro.manager.ServerManagerUI().setVisible(true);
    }

    public void run() {
        long delay = 500;
        isRunning = true;
        activeCommandLine();
        activeServerSocket();

        Manager.MAPS.forEach(nro.models.map.Map::initBoss);
        Thread.ofVirtual().start(DaiHoiVoThuat.getInstance());
        NgocRongNamecService.gI().initNgocRongNamec((byte) 0);
        Thread.ofVirtual().start(NgocRongNamecService.gI());
        Thread.ofVirtual().start(TopService.gI());
        Thread.ofVirtual().start(() -> EmtiManager.getInstance().startAutoSave());
        Thread.ofVirtual().start(() -> {
            while (isRunning) {
                try {

                    long start = System.currentTimeMillis();
                    MartialCongressManager.gI().update();
                    SieuHangManager.gI().update();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Thread.ofVirtual().start(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    ShopKyGuiManager.update();
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        NauBanhServices nauBanhServices = new NauBanhServices();
        Thread.ofVirtual().start(nauBanhServices);
        try {
            Thread.sleep(1000);
            Thread.ofVirtual().start(() -> BossManager.gI().loadBoss());
//            Manager.MAPS.forEach(nro.models.map.Map::initBoss);
            DaiHoiService.gI().initDaiHoiVoThuat();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            java.util.logging.Logger.getLogger(BossManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void activeServerSocket() {
        try {
            Network.gI().init().setAcceptHandler(new ISessionAcceptHandler() {
                @Override
                public void sessionInit(ISession is) {
                    if (!canConnectWithIp(is.getIP())) {
                        is.disconnect();
                        return;
                    }
                    is.setMessageHandler(Controller.getInstance())
                            .setSendCollect(new MessageSendCollect())
                            .setKeyHandler(new MyKeyHandler())
                            .startCollect().startQueueHandler();
                }

                @Override
                public void sessionDisconnect(ISession session) {
                    Client.gI().kickSession((MySession) session);
                }
            }).setTypeSessionClone(MySession.class)
                    .setDoSomeThingWhenClose(() -> {
                        Logger.error("SERVER CLOSE\n");
                        System.exit(0);
                    })
                    .start(PORT);
        } catch (Exception e) {
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(MySession session) {
        Object o = CLIENTS.get(session.getIP());
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.getIP(), n);
        }
    }

    private void activeCommandLine() {
        Thread.ofVirtual().start(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    Maintenance.gI().start(5);
                    System.err.println("✅ Tiến Hành Bảo Trì Server");
                } else if (line.equals("baotrinhanh")) {
                    close();
                    System.err.println("✅ Tiến Hành Đá ALL Player");
                } else if (line.equals("admin")) {
                    System.out.printf("[ADMIN CHECK] Player Online [%d], Sessions [%d], Threads JAVA [%d], , Threads SRC [%d]\n",
                            Client.gI().getPlayers().size(), SessionManager.gI().getNumSession(), ManagementFactory.getThreadMXBean().getThreadCount(), Thread.activeCount());
                } else if (line.equals("loadshop")) {
                    Manager.gI().updateShop();
                    System.err.println("✅ LOAD SHOP OK NHE DUY LOZ");
                }
            }
        });
    }

    public void close() {
        isRunning = false;
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Thông báo: lỗi lưu dữ liệu bang hội.\n");
        }
        Client.gI().close();
        ShopKyGuiManager.gI().save();
        Logger.log(Logger.RED, "Bảo trì đóng server thành công.\n");
//        try {
//            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "run.bat");
//            pb.directory(new File("C:\\Users\\Administrator\\Desktop\\Sever1"));
//            pb.inheritIO();
//            pb.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.exit(0);
    }
}
