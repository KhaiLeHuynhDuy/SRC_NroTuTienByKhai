package nro.models.mob;

import nro.utils.Util;

public class MobPoint {

    public final Mob mob;
    public long hp;
    public long maxHp;
    public long dame;

    public MobPoint(Mob mob) {
        this.mob = mob;
    }

    public long getHpFull() {
        return maxHp;
    }
//khaile add

    public long get70HpFull() {
        return (long) (maxHp * 0.7);
    }

    public long get30HpFull() {
        return (long) (maxHp * 0.3);
    }

    public long get20HpFull() {
        return (long) (maxHp * 0.2);
    }
//end khaile add

    public void setHpFull(long hp) {
        maxHp = hp;
    }

    public long gethp() {
        return hp;
    }

    public void sethp(long hp) {
        if (this.hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }

    public long getDameAttack() {
        return this.dame != 0 ? this.dame + Util.nextLong(-(this.dame / 100), (this.dame / 100))
                : this.get20HpFull() * Util.nextLong(mob.pDame - 1, mob.pDame + 1) / 100
                + Util.nextLong(-(mob.level * 5), mob.level * 5);
    }
}
