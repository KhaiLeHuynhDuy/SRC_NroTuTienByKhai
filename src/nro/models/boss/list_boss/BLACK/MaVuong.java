package nro.models.boss.list_boss.BLACK;

import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.utils.Util;

import java.util.Random;
import nro.consts.ConstPlayer;
import nro.consts.cn;
import nro.models.boss.list_boss.cell.SieuBoHung;
import nro.models.item.Item.ItemOption;
import nro.services.RewardService;
import nro.services.TaskService;
import nro.utils.Logger;

public class MaVuong extends Boss {

    public MaVuong() throws Exception {
        super(BossType.MAVUONG, BossesData.MAVUONG);
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
        int trai = 0;
        int phai = 1;
        int next = 0;
        for (int i = 0; i < 20; i++) {
            int X = next == 0 ? -5 * trai : 5 * phai;
            if (next == 0) {
                trai++;
            } else {
                phai++;
            }
            next = next == 0 ? 1 : 0;
            if (trai > 15) {
                trai = 0;
            }
            if (phai > 15) {
                phai = 1;
            }
            //Item roi
            if (Util.isTrue(1, 20)) {
                ItemMap itemMap = new ItemMap(zone, Util.nextInt(1099, 1102), 1, location.x + X, location.y, -1);
                Service.gI().dropItemMap(zone, itemMap);
            } else {
                ItemMap hongngoc = new ItemMap(zone, 1525, 1, location.x + X, location.y, -1);
                Service.gI().dropItemMap(zone, hongngoc);
            }
            if (Util.isTrue(1, 20)) {
                ItemMap itemMap = new ItemMap(zone, 457, 1, location.x + X, location.y, -1);
                Service.gI().dropItemMap(zone, itemMap);
            }

            if (Util.isTrue(1, 20)) {
                ItemMap itemMap = new ItemMap(zone, Util.nextInt(17, 20), 1, location.x + X, location.y, -1);
                Service.gI().dropItemMap(zone, itemMap);
            }
            if (Util.isTrue(1, 20)) {
                ItemMap itemMap = new ItemMap(zone, 861, Util.nextInt(10, 20), location.x + X, location.y, -1);
                Service.gI().dropItemMap(zone, itemMap);
            }
        }

    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = Util.nextInt(100, 10000);
            if (plAtt != null && !piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage * 0.5;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }
    private long st;

    @Override
    public void active() {
        if (this.typePk == ConstPlayer.NON_PK) {
            this.changeToTypePK();
        }
        try {
        } catch (Exception ex) {
            Logger.logException(MaVuong.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    protected void notifyJoinMap() {
        if (this.getCurrentLevel() == 1) {
            return;
        }
        super.notifyJoinMap();
    }
}
