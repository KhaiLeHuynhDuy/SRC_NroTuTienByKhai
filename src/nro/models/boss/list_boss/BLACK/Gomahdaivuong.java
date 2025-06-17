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

public class Gomahdaivuong extends Boss {

    public Gomahdaivuong() throws Exception {
        super(BossType.GOMAHDAIVUONG, BossesData.GOMAHDAIVUONG);
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
        //Item roi     
        if (Util.isTrue(80, 100)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1716, Util.nextInt(1, 3), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 10), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }

        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage);
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
            Logger.logException(Gomahdaivuong.class, ex);
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
