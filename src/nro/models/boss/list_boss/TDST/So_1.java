package nro.models.boss.list_boss.TDST;

import java.util.Random;
import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossStatus;
import nro.models.boss.BossType;
import nro.models.boss.BossesData;
import nro.models.boss.list_boss.android.Pic;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Logger;
import nro.utils.Util;

public class So_1 extends Boss {

    public So_1() throws Exception {
        super(BossType.SO_1, BossesData.SO_1);
    }

    @Override
    public void reward(Player pl) {
        pl.event.addEventPointBoss(1);
        Service.gI().sendThongBao(pl, "Bạn nhận được 1 điểm săn boss");
        byte randomDo = (byte) new Random().nextInt(Manager.itemDC12.length - 1);
        byte randomNR = (byte) new Random().nextInt(Manager.itemIds_NR_SB.length - 1);

        //Item roi
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1699, Util.nextInt(1, 3), this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, Util.nextInt(1688, 1692), 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        Service.gI().dropItemMap(this.zone, new ItemMap(zone, Util.nextInt(1688, 1692), 1, this.location.x + 6, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

        if (Util.isTrue(99, 100)) {
            Service.gI().dropItemMap(this.zone, Util.RaitiDoc12(zone, Manager.itemDC12[randomDo], 1, this.location.x + 5, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, Manager.itemIds_NR_SB[randomNR], 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
        }
        if (Util.isTrue(1, 100)) {
//            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 1466, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));
//        } else {
            Service.gI().dropItemMap(this.zone, new ItemMap(zone, 462, 1, this.location.x, zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id));

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
            Logger.logException(Pic.class, ex);
        }
        this.attack();
        if (Util.canDoWithTime(st, 900000)) {
            this.changeStatus(BossStatus.LEAVE_MAP);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(40, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
            if (plAtt != null) {
                switch (plAtt.playerSkill.skillSelect.template.id) {
                    case Skill.KAMEJOKO:
                        damage = damage / 2;
                    case Skill.LIEN_HOAN:
                        damage = damage * 75 / 100;
                    case Skill.MASENKO:
                        damage = damage * 130 / 100;
                    case Skill.GALICK:
                        damage = damage * 70 / 100;
                }
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
}
