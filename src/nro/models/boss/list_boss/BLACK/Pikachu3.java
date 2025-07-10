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
import nro.models.item.Item;
import nro.models.item.Item.ItemOption;
import nro.services.RewardService;
import nro.services.TaskService;
import nro.utils.Logger;

public class Pikachu3 extends Boss {

    public Pikachu3() throws Exception {
        super(BossType.Pikachu3, BossesData.PIKACHU3);
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
        int trai = 0;
        int phai = 1;
        int next = 0;
        for (int i = 0; i < 200; i++) {
            int X = next == 0 ? -5 * trai : 5 * phai;
            if (next == 0) {
                trai++;
            } else {
                phai++;
            }
            next = next == 0 ? 1 : 0;
            if (trai > 30) {
                trai = 0;
            }
            if (phai > 30) {
                phai = 1;
            }
            //Item roi
            if (Util.isTrue(1, 1)) {
                ItemMap itemMap = new ItemMap(zone, 1707, 10, location.x + X, zone.map.yPhysicInTop(location.x, location.y - 24), -1);
//                itemMap.options.add(new Item.ItemOption(30, 1)); // ko thể gd
                Service.gI().dropItemMap(zone, itemMap);
            }
        }
        if (Util.isTrue(1, 1)) {
            ItemMap itemMap = new ItemMap(zone, Util.nextInt(1678, 1680), 1, location.x, zone.map.yPhysicInTop(location.x, location.y - 24), -1);
            itemMap.options.add(new Item.ItemOption(30, 1)); // ko thể gd
        }
    }

    @Override
    public long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = Util.nextInt(50000, 100000);
            if (plAtt != null && !piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = Util.nextInt(1000, 10000);
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
            Logger.logException(Pikachu3.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 9000000)) {
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
