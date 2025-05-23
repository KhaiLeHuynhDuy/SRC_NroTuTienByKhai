package nro.models.map.BDKB;

import nro.models.boss.BossType;
import nro.models.boss.BossManager;
import nro.models.boss.list_boss.phoban.TrungUyXanhLoBdkb;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.services.InventoryServiceNew;
import nro.services.ItemTimeService;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Khánh Đẹp Zoai
 */
public class BanDoKhoBauService {

    private static BanDoKhoBauService I;

    public static BanDoKhoBauService gI() {
        if (BanDoKhoBauService.I == null) {
            BanDoKhoBauService.I = new BanDoKhoBauService();
        }
        return BanDoKhoBauService.I;
    }

    public List<BanDoKhoBau> BDKBs;

    private BanDoKhoBauService() {
        this.BDKBs = new ArrayList<>();
        for (int i = 0; i < BanDoKhoBau.AVAILABLE; i++) {
            this.BDKBs.add(new BanDoKhoBau(i));
        }
    }

    public void addMapBDKB(int id, Zone zone) {
        this.BDKBs.get(id).getZones().add(zone);
    }

    public void openBDKB(Player pl, int level) {
        if (pl.clan == null) {
            Service.gI().sendThongBao(pl, "Mi làm gì có bang hội");
            return;
        }
        if (pl.bdkb_countPerDay >= 3) {
            Service.gI().sendThongBao(pl, "Bạn đã đạt giới hạn lượt đi trong ngày");
            return;
        }
        if (pl.clan.banDoKhoBau == null && InventoryServiceNew.gI().findItemBag(pl, 611) == null) {
            Service.gI().sendThongBao(pl, "Không tìm thấy bản đồ kho báu");
            return;
        }
        
        BanDoKhoBau bd = null;
        for (BanDoKhoBau dt : this.BDKBs) {
            if (dt.getClan() == null) {
                bd = dt;
                break;
            }
        }
        if (bd == null) {
            Service.gI().sendThongBao(pl, "Bản đồ đã đầy, hãy quay lại vào lúc khác!");
            return;
        }
        InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, 611), 1);
        InventoryServiceNew.gI().sendItemBags(pl);
        bd.openBDKB(pl, level);
        ItemTimeService.gI().sendTextBanDoKhoBau(pl);
    }

    public void joinBDKB(Player pl) {
        if (pl.clan == null) {
            Service.gI().sendThongBao(pl, "Mi làm gì có bang hội");
            return;
        }
        if (pl.bdkb_countPerDay >= 3) {
            Service.gI().sendThongBao(pl, "Bạn đã đạt giới hạn lượt đi trong ngày");
            return;
        }
        if (pl.clan.banDoKhoBau != null) {
            if (!pl.bdkb_isJoinBdkb) {
                pl.bdkb_countPerDay++;
                pl.bdkb_isJoinBdkb = true;
            }
            pl.bdkb_lastTimeJoin = System.currentTimeMillis();
            ChangeMapService.gI().goToDBKB(pl);
            ItemTimeService.gI().sendTextBanDoKhoBau(pl);
        }
    }

   public void updatePlayer(Player player) {
        if (player.zone == null || !MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
            return;
        }
        try {
            if (player.clan != null && player.clan.banDoKhoBau != null) {
                long now = System.currentTimeMillis();
                if (!(now >= player.clan.banDoKhoBau_lastTimeOpen && now <= (player.clan.banDoKhoBau_lastTimeOpen + BanDoKhoBau.TIME_BAN_DO_KHO_BAU))) {
                    ketthucbdkb(player);
                } else if (player.clan.banDoKhoBau.timePickReward && (System.currentTimeMillis() - player.clan.banDoKhoBau.getLastTimeOpen() > 2000)) {
                    ketthucbdkb(player);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void ketthucbdkb(Player player) {
        List<Player> playersMap = player.zone.getPlayers();
        for (int i = playersMap.size() - 1; i >= 0; i--) {
            Player pl = playersMap.get(i);
            /**
             * Đưa mọi người trong map doanh trại về nhà
             */
            if (MapService.gI().isMapBanDoKhoBau(pl.zone.map.mapId)) {
                pl.bdkb_isJoinBdkb = false;
                Service.gI().sendThongBao(pl, "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
//                ItemTimeService.gI().sendTextTime(pl, (byte) 0, "Bạn đã được đưa về tới nhà", 0);
                ItemTimeService.gI().removeTextBDKB(pl);
                ChangeMapService.gI().changeMapBySpaceShip(pl, pl.gender + 21, -1, 250);
                if (pl.clan.banDoKhoBau != null) {
                    pl.clan.banDoKhoBau.dispose();
                    pl.clan.banDoKhoBau = null;
                }
            }
        }
    }

}
