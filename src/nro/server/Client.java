package nro.server;

import com.girlkun.database.GirlkunDB;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import nro.interfaces.ISession;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.map.ItemMap;
import nro.models.matches.pvp.DaiHoiVoThuat;
import nro.models.matches.pvp.DaiHoiVoThuatService;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.player.Player;
import nro.networknew.session.MySession;
import nro.networknew.session.Session;
import nro.networknew.session.SessionManager;
import nro.services.InventoryServiceNew;
import nro.services.ItemTimeService;
import nro.services.NgocRongNamecService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.services.func.GoiRongXuong;
import nro.services.func.SummonDragon;
import nro.services.func.TransactionService;
import nro.utils.Functions;
import nro.utils.Logger;

public class Client implements Runnable {

    private static Client i;
    private int id = 1_000_000_000;
    private final List<MySession> sessions = new ArrayList<>();
    private final Map<Integer, Session> sessions_id = new HashMap<>();
    private final Map<Long, Player> players_id = new HashMap<>();
    private final Map<Integer, Player> players_userId = new HashMap<>();
    private final Map<String, Player> players_name = new HashMap<>();
    private final List<Player> players = new ArrayList<>();
    private final List<Player> bots = new ArrayList<>();
    private boolean running = true;

    private Client() {
        Thread.ofVirtual().start(this::run);
    }

    private static final Random rand = new Random();
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static String generateRandomName(int minLength, int maxLength) {
        int length = minLength + rand.nextInt(maxLength - minLength + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(rand.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public List<Player> getPlayers() {
        return this.players;
    }

//    public List<Player> getPlayers() {
//        synchronized (players) {
//            return this.players.stream().collect(Collectors.toList());
//        }
//    }
    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }

    }

    private void remove(MySession session) {
        if (session.player != null) {
            this.remove(session.player);
            session.player.dispose();
        }

        if (session.joinedGame) {
            session.joinedGame = false;
            try {
                Thread.ofVirtual().start(() -> {
                    try {
                        // Cập nhật cơ sở dữ liệu
                        GirlkunDB.executeUpdate("update account set last_time_logout = ? where id = ?", new Timestamp(System.currentTimeMillis()), session.userId);
                    } catch (SQLException e) {
                        e.printStackTrace();
//                    logger.error("Lỗi khi cập nhật cơ sở dữ liệu bất đồng bộ", e);
                        Logger.logException(Client.class, e, "Lỗi khi cập nhật cơ sở dữ liệu bất đồng bộ");
                        // Xử lý hoặc ghi log lỗi theo cần thiết
                    } catch (Exception e) {
                        e.printStackTrace();
//                    logger.error("Lỗi khác khi cập nhật cơ sở dữ liệu bất đồng bộ", e);
                        Logger.logException(Client.class, e, "Lỗi khi cập nhật cơ sở dữ liệu bất đồng bộ");
                        // Xử lý hoặc ghi log lỗi theo cần thiết
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
//            logger.error("Lỗi khi lên lịch cập nhật cơ sở dữ liệu bất đồng bộ", e);
                Logger.logException(Client.class, e, "Lỗi khi lên lịch cơ sở dữ liệu bất đồng bộ");
                // Xử lý hoặc ghi log lỗi theo cần thiết
            }
        }

        ServerManager.gI().disconnect(session);
    }

    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        if (!player.beforeDispose) {
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).removePlayerWait(player);
            DaiHoiVoThuatService.gI(DaiHoiVoThuat.getInstance().getDaiHoiNow()).removePlayer(player);
            TranhNgoc.gI().removePlayersCadic(player);
            TranhNgoc.gI().removePlayersFide(player);
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;
            if (player.idNRNM != -1) {
                ItemMap itemMap = new ItemMap(player.zone, player.idNRNM, 1, player.location.x, player.location.y, -1);
                Service.gI().dropItemMap(player.zone, itemMap);
                NgocRongNamecService.gI().pNrNamec[player.idNRNM - 353] = "";
                NgocRongNamecService.gI().idpNrNamec[player.idNRNM - 353] = -1;
                player.idNRNM = -1;
            }
            ChangeMapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
            if (player.itemTime != null && player.itemTime.isUseTDLT) {
                Item tdlt = null;
                try {
                    tdlt = InventoryServiceNew.gI().findItemBag(player, 521);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (tdlt != null) {
                    ItemTimeService.gI().turnOffTDLT(player, tdlt);
                }
            }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (GoiRongXuong.gI().playerRongXuong != null
                    && GoiRongXuong.gI().playerRongXuong.id == player.id) {
                GoiRongXuong.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                if (player.newpet != null) {
                    ChangeMapService.gI().exitMap(player.newpet);
                }
                if (player.minipet != null) {
                    ChangeMapService.gI().exitMap(player.minipet);
                }
                ChangeMapService.gI().exitMap(player.pet);
            }
        }
        PlayerDAO.updatePlayer(player);
    }

    public void kickSession(MySession session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }

    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    public Player getPlayerByUserName(String username) {
        for (Player player : players) {
            if (player.getSession() != null && player.getSession().uu.equals(username)) {
                return player;
            }
        }
        return null;
    }

    public Player getPlayerByID(int playerId) {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player != null && player.id == playerId) {
                return player;
            }
        }
        return null;
    }

    public void clear() {
        if (!bots.isEmpty()) {
            players.remove(bots.get(0));
            remove(bots.get(0));
            bots.remove(0);
        }
    }

    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    public Session getSession(int sessionId) {
        return this.sessions_id.get(sessionId);
    }

//    public void close() {
//        Logger.error("BEGIN KICK OUT SESSION.............................." + players.size() + "\n");
//        while (!GirlkunSessionManager.gI().getSessions().isEmpty()) {
//            Logger.error("LEFT PLAYER: " + this.players.size() + ".........................\n");
//            this.kickSession((MySession) GirlkunSessionManager.gI().getSessions().remove(0));
//        }
//        while (!players.isEmpty()) {
//            this.kickSession((MySession) players.remove(0).getSession());
//        }
//        Logger.error("...........................................SUCCESSFUL\n");
//    }
    public void close() {
        Logger.log(Logger.YELLOW, "BEGIN KICK OUT SESSION " + players.size() + "\n");
        while (!players.isEmpty()) {
            Player pl = players.remove(0);
            if (pl != null && pl.getSession() != null) {
                this.kickSession(pl.getSession());
            }
        }
        Logger.success("SUCCESSFUL\n");
    }
   public void cloneMySessionNotConnect() {
        Logger.error("---------START KICK SESSION DDOS---------");
        Logger.error("COUNT: " + SessionManager.gI().getSessions().size());

        if (!SessionManager.gI().getSessions().isEmpty()) {
            var iterator = SessionManager.gI().getSessions().iterator();
            while (iterator.hasNext()) {
                MySession m = (MySession) iterator.next();
                if (m != null && m.player == null) {
                    iterator.remove(); // Loại bỏ session từ danh sách
                    this.kickSession(m); // Gọi phương thức kickSession
                }
            }
        }

        Logger.error("---------SUCCESSFUL---------\n");
    } 

    private void update() {
        for (int i = SessionManager.gI().getSessions().size() - 1; i >= 0; i--) {
            ISession s = SessionManager.gI().getSessions().get(i);
            MySession session = (MySession) s;
            if (session == null) {
                SessionManager.gI().getSessions().remove(i);
                continue;
            }
            if (session.timeWait > 0) {
                session.timeWait--;
                if (session.timeWait == 0) {
                    kickSession(session);
                }
            }
        }
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            long st = System.currentTimeMillis();
            try {
                update();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Functions.sleep(Math.max(1000 - (System.currentTimeMillis() - st), 10));
        }
    }
}
