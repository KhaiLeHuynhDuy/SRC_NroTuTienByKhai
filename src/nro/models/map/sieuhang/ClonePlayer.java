package nro.models.map.sieuhang;

import nro.models.boss.BossData;
import nro.models.boss.dhvt.BossDHVT;
import nro.models.player.Player;
import nro.utils.Util;

/**
 *
 * @author Béo Mập :3
 */
public class ClonePlayer extends BossDHVT{
    
    public  ClonePlayer(Player player, BossData data, int id) throws Exception {
        super(Util.randomBossId(), data,5000);
        this.playerAtt = player;
        this.idPlayer = id;
    }
}
