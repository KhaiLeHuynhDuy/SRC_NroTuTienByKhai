package nro.models.boss.list_boss.Doraemon;

import nro.consts.cn;
import nro.models.boss.Boss;
import nro.models.boss.BossType;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;
import java.util.Random;
import nro.consts.ConstPlayer;
import nro.models.boss.BossStatus;
import nro.models.boss.list_boss.BLACK.Black;
import nro.services.EffectSkillService;
import nro.utils.Logger;

public class Nobita extends Boss {

    public Nobita() throws Exception {
        super(BossType.NOBITA, BossesData.NOBITA);
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length - 1);
        byte randomDo2 = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);

        //Item roi
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1699, Util.nextInt(1, 3), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        if (Util.isTrue(9, 10)) {
            Service.gI().dropItemMap(this.zone, Util.ratiItem(zone, Manager.itemIds_TL[randomDo], 1, this.location.x + 5, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(9, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, Manager.itemDC12[randomDo2], 1, this.location.x + 5, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(1, 10)) {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 457, Util.nextInt(1, 20), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 861, Util.nextInt(100, 300), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        if (Util.isTrue(1, 10)) {
            generalRewards(pl);
        }

    }

    @Override
    public void wakeupAnotherBossWhenDisappear() {
        if (this.getParentBoss() == null) {
            return;
        }
        for (Boss boss : this.getParentBoss().getBossAppearTogether()[this.getParentBoss().getCurrentLevel()]) {
            System.out.println("name: " + boss.name);
            if (boss.id == BossType.XEKO || boss.id == BossType.CHAIEN) {
//                boss.changeToTypePK();
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
            Logger.logException(Nobita.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

}
