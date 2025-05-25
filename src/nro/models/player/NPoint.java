package nro.models.player;

//import com.arriety.card.Card;
import nro.models.card.OptionCard;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.consts.cn;
import nro.models.card.Card;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.Item;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.EffectSkillService;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Logger;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.ArrayList;

import java.util.List;

public class NPoint {//Zalo: 0358124452//Name: EMTI 

    public static final byte MAX_LIMIT = 12;
    private Player player;
    private PointFusion ps;

    public NPoint(Player player) {//Zalo: 0358124452//Name: EMTI 

        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
    }

    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;

    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;

    public long hp, hpMax, hpg;
    public long mp, mpMax, mpg;
    public long dame, dameg;
    public int def, defg;
    public int crit, critg, overflowcrit;
    public byte speed = 5;

    public boolean teleport;
    public boolean isSoHai;
    public boolean isDraburaFrost; //Cải trang Dracula Frost
    public boolean isDrabura; //Cải trang Dracula Frost
    public boolean isThoDaiCa; //Cải trang Dracula Frost
    public boolean khangTDHS;

    /**
     * Chỉ số cộng thêm
     */
    public int hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public long hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob, tlHutMpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;

    public int voHieuChuong;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;

    public short tlHpGiamODo;
    public short test;
    public short tlHpFideBlack;

    public short multicationChuong;
    public long lastTimeMultiChuong;

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {//Zalo: 0358124452//Name: EMTI 
        if (this.player.pet != null) {//Zalo: 0358124452//Name: EMTI 
            this.player.pet.nPoint.setPointWhenWearClothes();
        }
        this.setPointWhenWearClothes();
    }

    public static long calPercent(long param, int percent) {
        return param * percent / 150;
    }

    private void setPointWhenWearClothes() {//Zalo: 0358124452//Name: EMTI 
        resetPoint();
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            dame += RewardBlackBall.R2S_1;

        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            hp += RewardBlackBall.R2S_1;
            mp += RewardBlackBall.R2S_1;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            tlDameAttMob.add(RewardBlackBall.R3S_2);
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            tlPST += RewardBlackBall.R4S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 

            tlNeDon += RewardBlackBall.R5S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            tlHpHoi += RewardBlackBall.R6S_2;
            tlHutHp += RewardBlackBall.R6S_2;
        }
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            tlMpHoi += RewardBlackBall.R7S_2;
            tlHutMp += RewardBlackBall.R7S_2;
        }
        for (Card card : player.Cards) {
            if (card != null && card.Used == 1) {
                for (OptionCard io : card.Options) {
                    if (io.active == card.Level || (card.Level == -1 && io.active == 0)) {
                        switch (io.id) {
                            case 0: // Tấn công +#
                                this.dameAdd += io.param;
                                break;
                            case 2: // HP, KI+#000
                                this.hpAdd += io.param * 1000d;
                                this.mpAdd += io.param * 1000d;
                                break;
                            case 3:// fake
                                this.voHieuChuong += io.param;
                                break;
                            case 5: // +#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param);
                                break;
                            case 6: // HP+#
                                this.hpAdd += io.param;
                                break;
                            case 7: // KI+#
                                this.mpAdd += io.param;
                                break;
                            case 8: // Hút #% HP, KI xung quanh mỗi 5 giây
                                this.tlHutHpMpXQ += io.param;
                                break;
                            case 14: // Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 19: // Tấn công+#% khi đánh quái
                                this.tlDameAttMob.add(io.param);
                                break;
                            case 22: // HP+#K
                                this.hpAdd += io.param * 1000d;
                                break;
                            case 23: // MP+#K
                                this.mpAdd += io.param * 1000d;
                                break;
                            case 27: // +# HP/30s
                                this.hpHoiAdd += io.param;
                                break;
                            case 28: // +# KI/30s
                                this.mpHoiAdd += io.param;
                                break;
                            case 47: // Giáp+#
                                this.defAdd += io.param;
                                break;
                            case 48: // HP/KI+#
                                this.hpAdd += io.param;
                                this.mpAdd += io.param;
                                break;
                            case 49: // Tấn công+#%
                            case 50: // Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: // HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: // HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: // MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 88: // Cộng #% exp khi đánh quái
                                this.tlTNSM.add(io.param);
                                break;
                            case 94: // Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 95: // Biến #% tấn công thành HP
                                this.tlHutHp += io.param;
                                break;
                            case 96: // Biến #% tấn công thành MP
                                this.tlHutMp += io.param;
                                break;
                            case 97: // Phản #% sát thương
                                this.tlPST += io.param;
                                break;
                            case 100: // +#% vàng từ quái
                                this.tlGold += io.param;
                                break;
                            case 101: // +#% TN,SM
                                this.tlTNSM.add(io.param);
                                break;
                            case 103: // KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 104: // Biến #% tấn công quái thành HP
                                this.tlHutHpMob += io.param;
                                break;
                            case 147: // +#% sức đánh
                                this.tlDame.add(io.param);
                                break;
                        }
                    }
                }
            }
        }

        if (this.player != null && this.player.setClothes != null) {//Zalo: 0358124452//Name: EMTI 
            this.player.setClothes.worldcup = 0;
        }
//        0358124452
        for (Item item : this.player.inventory.itemsBody) {//Zalo: 0358124452//Name: EMTI 
            if (item != null && item.template != null) {
                switch (item.template.id) {//Zalo: 0358124452//Name: EMTI 
                    case 966:
                    case 982:
                    case 983:
                    case 883:
                    case 904:
                        player.setClothes.worldcup++;
                }

                if (item.template.id >= 592 && item.template.id <= 594) {//Zalo: 0358124452//Name: EMTI 
                    teleport = true;
                }

                for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                    switch (io.optionTemplate.id) {//Zalo: 0358124452//Name: EMTI 
                        case 232:
                        case 226:
                        case 0: //Tấn công +#
                            this.dameAdd += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 2: //HP, KI+#000
                            this.hpAdd += io.param * 1000;
                            this.mpAdd += io.param * 1000;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 3:// fake
                            this.voHieuChuong += io.param;
                            break;                            //Name: EMTI 
                        case 224:
                        case 246:
                        case 5: //+#% sức đánh chí mạng
                            this.tlDameCrit.add(io.param);
                            break;
                        case 233://Zalo: 0358124452                                //Name: EMTI 
                        case 6: //HP+#
                        case 227:
                            this.hpAdd += io.param;
                            break;
                        case 234://Zalo: 0358124452                                //Name: EMTI 
                        case 7: //KI+#
                        case 228:
                            this.mpAdd += io.param;
                            break;
                        case 230://Zalo: 0358124452                                //Name: EMTI 
                        case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                            this.tlHutHpMpXQ += io.param;
                            break;
                        case 247://Name: EMTI 
                        case 14: //Chí mạng+#%
                            this.critAdd += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 19: //Tấn công+#% khi đánh quái
                            this.tlDameAttMob.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 22: //HP+#K
                            this.hpAdd += io.param * 1000;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 23: //MP+#K
                            this.mpAdd += io.param * 1000;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 27: //+# HP/30s
                            this.hpHoiAdd += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 28: //+# KI/30s
                            this.mpHoiAdd += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 33: //dịch chuyển tức thời
                            this.teleport = true;
                            break;
                        case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                        case 47: //Giáp+#
                        case 229:
                            this.defAdd += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 48: //HP/KI+#
                            this.hpAdd += io.param;
                            this.mpAdd += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 221:

                        case 49: //Tấn công+#%
                        case 50: //Sức đánh+#%
                        case 242:
                            this.tlDame.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 222:

                        case 77: //HP+#%
                        case 244:
                            this.tlHp.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 80: //HP+#%/30s
                            this.tlHpHoi += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 81: //MP+#%/30s
                            this.tlMpHoi += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 88: //Cộng #% exp khi đánh quái
                            this.tlTNSM.add(io.param);
                            break;
                        case 241://Zalo: 0358124452     
                        case 94: //Giáp #%
                            this.tlDef.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 95: //Biến #% tấn công thành HP
                            this.tlHutHp += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 96: //Biến #% tấn công thành MP
                            this.tlHutMp += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 97: //Phản #% sát thương
                            this.tlPST += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 100: //+#% vàng từ quái
                            this.tlGold += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 101: //+#% TN,SM
                            this.tlTNSM.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 223:
                        case 103: //KI +#%
                        case 245:
                            this.tlMp.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 104: //Biến #% tấn công quái thành HP
                            this.tlHutHpMob += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 105: //Vô hình khi không đánh quái và boss
                            this.wearingVoHinh = true;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 106: //Không ảnh hưởng bởi cái lạnh
                            this.isKhongLanh = true;
                            break;
                        case 243://Zalo: 0358124452                                //Name: EMTI 
                        case 108: //#% Né đòn
                            this.tlNeDon += io.param;// đối nghịch
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 109: //Hôi, giảm #% HP
                            this.tlHpGiamODo += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 116: //Kháng thái dương hạ san
                            this.khangTDHS = true;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                            this.tlSDDep.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 147: //+#% sức đánh
                            this.tlDame.add(io.param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 75: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                            this.tlSubSD += 50;

                            this.tlTNSM.add(io.param);
                            this.tlGold += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 162: //Cute hồi #% KI/s bản thân và xung quanh
                            this.mpHoiCute += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 173: //Phục hồi #% HP và KI cho đồng đội
                            this.tlHpHoiBanThanVaDongDoi += io.param;
                            this.tlMpHoiBanThanVaDongDoi += io.param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 115: //Thỏ Đại Ca
                            this.isThoDaiCa = true;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 26: //Dracula hóa đá
                            this.isDrabura = true;
                            break;
                        case 231://Zalo: 0358124452                                //Name: EMTI 
                        case 218: // Dracula Frost
                            this.isDraburaFrost = true;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 219: // Blackfide
                            this.isSoHai = true;
                            break;
                        //khaile add
                        case 196:
                            this.hpAdd += io.param * 100_000; // 00k
                            break;
                        case 197:
                            this.mpAdd += io.param * 100_000;//00k
                            break;
                        case 198:
                            this.dameAdd += io.param * 100_000;//00k
                            break;
                        case 199:
                            this.defAdd += io.param * 100_000;//00k
                            break;
                        //end khaile add
                    }
                }
            }
        }
        for (Item item : this.player.inventory.itemsBag) {//Zalo: 0358124452//Name: EMTI 

            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {//Zalo: 0358124452//Name: EMTI 
//            for (Item item : this.player.inventory.itemsBag) {//Zalo: 0358124452//Name: EMTI 
                if (item.isNotNullItem() && item.template.id == 921) {//Zalo: 0358124452//Name: EMTI 
                    for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                        switch (io.optionTemplate.id) {//Zalo: 0358124452//Name: EMTI 
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233://Zalo: 0358124452                                //Name: EMTI 
                                this.hpAdd += io.param;
                                break;
                            case 234://Zalo: 0358124452                                //Name: EMTI 
                                this.mpAdd += io.param;
                                break;
                            case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                                this.defAdd += io.param;
                                break;
                            case 5: //+#% sức đánh chí mạng
                                this.tlDameCrit.add(io.param / 2);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            //Zalo: 0358124452                                //Name: EMTI 
                        }
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                }
            }
//        }

            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {//Zalo: 0358124452//Name: EMTI 
//            for (Item item : this.player.inventory.itemsBag) {//Zalo: 0358124452//Name: EMTI 
                if (item.isNotNullItem() && item.template.id == 2064) {//Zalo: 0358124452//Name: EMTI 
                    for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                        switch (io.optionTemplate.id) {//Zalo: 0358124452//Name: EMTI 
                            case 5:
                                this.tlDameCrit.add(io.param / 2);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233://Zalo: 0358124452                                //Name: EMTI 
                                this.hpAdd += io.param;
                                break;
                            case 234://Zalo: 0358124452                                //Name: EMTI 
                                this.mpAdd += io.param;
                                break;
                            case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                                this.defAdd += io.param;
                                break;//Zalo: 0358124452                                //Name: EMTI 
                        }
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                }
            }
//        }
            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {//Zalo: 0358124452//Name: EMTI 
//            for (Item item : this.player.inventory.itemsBag) {//Zalo: 0358124452//Name: EMTI 
                if (item.isNotNullItem() && item.template.id == 2113) {//Zalo: 0358124452//Name: EMTI 
                    for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                        switch (io.optionTemplate.id) {//Zalo: 0358124452//Name: EMTI 
                            case 5:
                                this.tlDameCrit.add(io.param / 2);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233://Zalo: 0358124452                                //Name: EMTI 
                                this.hpAdd += io.param;
                                break;
                            case 234://Zalo: 0358124452                                //Name: EMTI 
                                this.mpAdd += io.param;
                                break;
                            case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                                this.defAdd += io.param;
                                break;//Zalo: 0358124452                                //Name: EMTI 
                        }
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                }
            }
//        }
            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {//Zalo: 0358124452//Name: EMTI 
//            for (Item item : this.player.inventory.itemsBag) {//Zalo: 0358124452//Name: EMTI 
                if (item.isNotNullItem() && item.template.id == 1324) {//Zalo: 0358124452//Name: EMTI 
                    for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                        switch (io.optionTemplate.id) {//Zalo: 0358124452//Name: EMTI 
                            case 5:
                                this.tlDameCrit.add(io.param / 2);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233://Zalo: 0358124452                                //Name: EMTI 
                                this.hpAdd += io.param;
                                break;
                            case 234://Zalo: 0358124452                                //Name: EMTI 
                                this.mpAdd += io.param;
                                break;
                            case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                                this.defAdd += io.param;
                                break;//Zalo: 0358124452                                //Name: EMTI 
                        }
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                }
            }
            if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {//Zalo: 0358124452//Name: EMTI 
//            for (Item item : this.player.inventory.itemsBag) {//Zalo: 0358124452//Name: EMTI 
                if (item.isNotNullItem() && item.template.id == 1472) {//Zalo: 0358124452//Name: EMTI 
                    for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                        switch (io.optionTemplate.id) {//Zalo: 0358124452//Name: EMTI 
                            case 5:
                                this.tlDameCrit.add(io.param / 2);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;                                //Zalo: 0358124452                                //Name: EMTI 
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 232:
                                this.dameAdd += io.param;
                                break;
                            case 233://Zalo: 0358124452                                //Name: EMTI 
                                this.hpAdd += io.param;
                                break;
                            case 234://Zalo: 0358124452                                //Name: EMTI 
                                this.mpAdd += io.param;
                                break;
                            case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                                this.defAdd += io.param;
                                break;//Zalo: 0358124452                                //Name: EMTI 
                        }
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                }
            }
        }
        if (player.pet != null && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {

            if (player.getHead() == 1408) {
                tlPST += 50;
                tlHutHpMpXQ += 5;
                critAdd += 20;
                tlDame.add(20);
                tlHp.add(20);
                tlMp.add(20);
            }
        }
        if (player.pet != null && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {

            if (player.getHead() == 1414) {
                tlPST += 80;
                tlHutHpMpXQ += 10;
                critAdd += 30;
                tlDame.add(30);
                tlHp.add(30);
                tlMp.add(30);
            }
        }
        if (player.pet != null && player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {

            if (player.getHead() == 1503 || player.getHead() == 1562) {
                tlPST += 100;
                tlHutHpMpXQ += 15;
                critAdd += 45;
                switch (player.gender) {
                    case 0:
                        tlDame.add(45);
                        dameAdd += calPercent(this.dame, 1 / 10);
                        tlHp.add(45);
                        tlMp.add(45);
                        break;
                    case 1:
                        tlDame.add(45);
                        tlHp.add(45);
                        tlMp.add(45);
                        mpAdd += calPercent(this.mp, 1);
                        break;
                    case 2:
                        tlDame.add(45);
                        tlHp.add(45);
                        hpAdd += calPercent(this.hp, 1);
                        tlMp.add(45);
                        break;
                    default:
                        tlDame.add(1);
                        tlHp.add(1);
                        tlMp.add(1);
                }
                isDraburaFrost = true;
            }

        }

        setDameTrainArmor();
        setBasePoint();
    }

    private void setDameTrainArmor() {//Zalo: 0358124452//Name: EMTI 
        if (!this.player.isPet && !this.player.isBoss) {//Zalo: 0358124452//Name: EMTI 
            if (this.player.inventory.itemsBody.size() < 7) {//Zalo: 0358124452//Name: EMTI 
                return;
            }
            try {//Zalo: 0358124452//Name: EMTI 
                Item gtl = this.player.inventory.itemsBody.get(6);
                if (gtl.isNotNullItem() && gtl.template.type != 35) {//Zalo: 0358124452//Name: EMTI 
                    this.wearingTrainArmor = true;
                    this.wornTrainArmor = true;
                    this.player.inventory.trainArmor = gtl;

                    // Nếu có option 50, công dồn vào tlDame
                    for (Item.ItemOption io : gtl.itemOptions) {
                        if (io.optionTemplate.id == 50) {
                            this.tlSubSD += io.param;
                        }
                    }
                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
                } else {//Zalo: 0358124452//Name: EMTI 
                    if (this.wornTrainArmor) {//Zalo: 0358124452//Name: EMTI 
                        this.wearingTrainArmor = false;

                        if (this.player.inventory.trainArmor.itemOptions != null) {
                            for (Item.ItemOption io : this.player.inventory.trainArmor.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                                if (io.optionTemplate.id == 9 && io.param > 0) {//Zalo: 0358124452//Name: EMTI 
//                                    for (Item.ItemOption io2 : this.player.inventory.trainArmor.itemOptions) {
//                                        if (io.optionTemplate.id == 50) {
//                                            this.tlDame.add(io2.param);
//                                        }
//                                    }
                                    this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
                                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                                }

                            }
                            // Tăng thêm nếu giáp có option id 50

                        }
                    }
                }
            } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 
                e.printStackTrace();
                Logger.error("Lỗi get giáp tập luyện " + this.player.name + "\n");
            }
        }
    }

    public void setBasePoint() {//Zalo: 0358124452//Name: EMTI 
        setHpMax();
        setHp();
        setMpMax();
        setMp();
        setDame();
        setDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
        setPotara2();
        setPotara3();
        setPotara4();
        setPotara5();
        setPotara6();
    }

    private void setNeDon() {//Zalo: 0358124452//Name: EMTI 
        if (this.tlNeDon > 90) {//Zalo: 0358124452//Name: EMTI 
            this.tlNeDon = 90;
        }
//set vo cuc tu tai
//khaile add
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
            this.tlNeDon += 20;
        }
//end khaile add
    }

    public void setPotara2() {//Zalo: 0358124452//Name: EMTI 
        if (!this.player.isPet && !this.player.isBoss) {//Zalo: 0358124452//Name: EMTI 
            Item btc2 = InventoryServiceNew.gI().findItemBag(this.player, 921);
            if (btc2 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {//Zalo: 0358124452//Name: EMTI 
                if (!btc2.itemOptions.isEmpty()) {//Zalo: 0358124452//Name: EMTI 
                    int param = btc2.itemOptions.get(0).param;
                    int option_id = btc2.itemOptions.get(0).optionTemplate.id;
                    switch (option_id) {//Zalo: 0358124452//Name: EMTI 
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;                             //Zalo: 0358124452                                //Name: EMTI 
                        case 14:
                            this.crit += param;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233://Zalo: 0358124452                                //Name: EMTI 
                            this.hpAdd += param;
                            break;
                        case 234://Zalo: 0358124452                                //Name: EMTI 
                            this.mpAdd += param;
                            break;
                        case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                            this.defAdd += param;
                            break;//Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 

                    }
                }
            }
        }

    }

    public void setPotara3() {//Zalo: 0358124452//Name: EMTI 
        if (!this.player.isPet && !this.player.isBoss) {//Zalo: 0358124452//Name: EMTI 
            Item btc3 = InventoryServiceNew.gI().findItemBag(this.player, 2064);

            if (btc3 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {//Zalo: 0358124452//Name: EMTI 
                if (!btc3.itemOptions.isEmpty()) {//Zalo: 0358124452//Name: EMTI 
                    int param = btc3.itemOptions.get(0).param;
                    int option_id = btc3.itemOptions.get(0).optionTemplate.id;
                    // System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {//Zalo: 0358124452//Name: EMTI 
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);

                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233://Zalo: 0358124452                                //Name: EMTI 
                            this.hpAdd += param;
                            break;
                        case 234://Zalo: 0358124452                                //Name: EMTI 
                            this.mpAdd += param;
                            break;
                        case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                            this.defAdd += param;
                            break;//Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI                                //Zalo: 0358124452                                //Name: EMTI 
                        default:
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                    }
                }
            }
        }

    }

    public void setPotara4() {//Zalo: 0358124452//Name: EMTI 
        if (!this.player.isPet && !this.player.isBoss) {//Zalo: 0358124452//Name: EMTI 
            Item btc4 = InventoryServiceNew.gI().findItemBag(this.player, 2113);

            if (btc4 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {//Zalo: 0358124452//Name: EMTI 
                if (!btc4.itemOptions.isEmpty()) {//Zalo: 0358124452//Name: EMTI 
                    int param = btc4.itemOptions.get(0).param;
                    int option_id = btc4.itemOptions.get(0).optionTemplate.id;
                    //    System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {//Zalo: 0358124452//Name: EMTI 
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;                                        //Zalo: 0358124452                                //Name: EMTI 
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233://Zalo: 0358124452                                //Name: EMTI 
                            this.hpAdd += param;
                            break;
                        case 234://Zalo: 0358124452                                //Name: EMTI 
                            this.mpAdd += param;
                            break;
                        case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                            this.defAdd += param;
                            break;//Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI                             //Zalo: 0358124452                                //Name: EMTI 
                        default:
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                    }
                }
            }
        }

    }

    public void setPotara5() {//Zalo: 0358124452//Name: EMTI 
        if (!this.player.isPet && !this.player.isBoss) {//Zalo: 0358124452//Name: EMTI 
            Item btc4 = InventoryServiceNew.gI().findItemBag(this.player, 1324);

            if (btc4 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {//Zalo: 0358124452//Name: EMTI 
                if (!btc4.itemOptions.isEmpty()) {//Zalo: 0358124452//Name: EMTI 
                    int param = btc4.itemOptions.get(0).param;
                    int option_id = btc4.itemOptions.get(0).optionTemplate.id;
                    //    System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {//Zalo: 0358124452//Name: EMTI 
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233://Zalo: 0358124452                                //Name: EMTI 
                            this.hpAdd += param;
                            break;
                        case 234://Zalo: 0358124452                                //Name: EMTI 
                            this.mpAdd += param;
                            break;
                        case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                            this.defAdd += param;
                            break;//Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI                             //Zalo: 0358124452                                //Name: EMTI 
                        default:
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                    }
                }
            }
        }

    }

    public void setPotara6() {//Zalo: 0358124452//Name: EMTI 
        if (!this.player.isPet && !this.player.isBoss) {//Zalo: 0358124452//Name: EMTI 
            Item btc4 = InventoryServiceNew.gI().findItemBag(this.player, 1472);

            if (btc4 != null && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6) {//Zalo: 0358124452//Name: EMTI 
                if (!btc4.itemOptions.isEmpty()) {//Zalo: 0358124452//Name: EMTI 
                    int param = btc4.itemOptions.get(0).param;
                    int option_id = btc4.itemOptions.get(0).optionTemplate.id;
                    //    System.out.println("Name " + player.name + "option id" + option_id + "Param " + param);
                    switch (option_id) {//Zalo: 0358124452//Name: EMTI 
                        case 50:

                            this.dame += calPercent(this.dame, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 77:
                            this.hp += calPercent(this.hpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 103:
                            this.mp += calPercent(this.mpMax, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 94:
                            this.def += (int) calPercent(this.def, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 108:
                            this.tlNeDon += (byte) calPercent(this.tlNeDon, param);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 14:

                            this.crit += param;
                            break;
                        case 5:
                            this.tlDameCrit.add(param / 2);
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 80: //HP+#%/30s
                            this.tlHpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        case 81: //MP+#%/30s
                            this.tlMpHoi += param;
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI 
                        case 232:
                            this.dameAdd += param;
                            break;
                        case 233://Zalo: 0358124452                                //Name: EMTI 
                            this.hpAdd += param;
                            break;
                        case 234://Zalo: 0358124452                                //Name: EMTI 
                            this.mpAdd += param;
                            break;
                        case 235://Zalo: 0358124452         //Zalo: 0358124452                                //Name: EMTI 
                            this.defAdd += param;
                            break;//Zalo: 0358124452                                //Name: EMTI 
                        //Zalo: 0358124452                                //Name: EMTI                               //Zalo: 0358124452                                //Name: EMTI 
                        default:
                            break;                                //Zalo: 0358124452                                //Name: EMTI 
                    }
                }
            }
        }

    }

    private void setHpHoi() {//Zalo: 0358124452//Name: EMTI 
        this.hpHoi = Util.maxIntValue(this.hpMax / 100);
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += ((long) this.hpMax * this.tlHpHoi / 100);
        this.hpHoi += ((long) this.hpMax * this.tlHpHoiBanThanVaDongDoi / 100);
    }

    private void setMpHoi() {//Zalo: 0358124452//Name: EMTI 
        this.mpHoi = Util.maxIntValue(this.mpMax / 100);
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += ((long) this.mpMax * this.tlMpHoi / 100);
        this.mpHoi += ((long) this.mpMax * this.tlMpHoiBanThanVaDongDoi / 100);
    }

    // Assuming this.tlHp is an instance of ArrayList<Integer>
    private void setHpMax() {
        this.hpMax = this.hpg + this.hpAdd;

        for (Integer tl : this.tlHp) {
            if (tl != null) {
                this.hpMax += calPercent(this.hpMax, tl);
            }
        }
//set vo cuc tu tai
//khaile add
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 750);
        }
//end khaile add
        // Set nappa
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nappa
                == 5) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 80);
        }

        // Set worldcup
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.worldcup
                == 2) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 10);
        }

        // Check if the "ngọc rồng đen" reward with 2 stars is still valid
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, RewardBlackBall.R2S_1);
        }

        // Check if the "Monkey" skill effect is active and apply the bonus to hpMax
        if (this.player.effectSkill.isMonkey) {//Zalo: 0358124452//Name: EMTI 
            // Ensure that the player is not a pet or is a pet but not in fusion status
            if (!this.player.isPet || (this.player.isPet && ((Pet) this.player).status != Pet.FUSION)) {//Zalo: 0358124452//Name: EMTI 
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.hpMax += calPercent(this.hpMax, percent);
            }
        }

        if (this.player.isPet// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6)) {
            if (((Pet) this.player).typePet == 1) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet1);
            }
            if (((Pet) this.player).typePet == 2) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet2);
            }
            if (((Pet) this.player).typePet == 3) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet3);
            }
            if ((((Pet) this.player).typePet == 4)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet4);
            }
            if ((((Pet) this.player).typePet == 5)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet5);
            }
            if ((((Pet) this.player).typePet == 6)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet6);
            }
            if ((((Pet) this.player).typePet == 7)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet7);
            }
            if ((((Pet) this.player).typePet == 8)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet8);
            }
            if ((((Pet) this.player).typePet == 9)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet9);
            }
            if (((Pet) this.player).typePet == 10) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet10);
            }
            if ((((Pet) this.player).typePet == 11)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet11);
            }
            if ((((Pet) this.player).typePet == 12)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet12);
            }
            if ((((Pet) this.player).typePet == 13)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet13);
            }
            if ((((Pet) this.player).typePet == 14)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet14);
            }
            if ((((Pet) this.player).typePet == 15)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet15);
            }
            if ((((Pet) this.player).typePet == 16)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet16);
            }
            if ((((Pet) this.player).typePet == 17)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet17);
            }
            if ((((Pet) this.player).typePet == 18)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet18);
            }
            if ((((Pet) this.player).typePet == 19)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet19);
            }
            if ((((Pet) this.player).typePet == 20)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet20);
            }
            if ((((Pet) this.player).typePet == 21)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet21);
            }
            if ((((Pet) this.player).typePet == 22)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet22);
            }
            if ((((Pet) this.player).typePet == 23)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet23);
            }
            if ((((Pet) this.player).typePet == 24)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet24);
            }
            if ((((Pet) this.player).typePet == 25)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet25);
            }
            if ((((Pet) this.player).typePet == 26)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet26);
            }
            if ((((Pet) this.player).typePet == 27)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet27);
            }
            if ((((Pet) this.player).typePet == 28)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet28);
            }
            if ((((Pet) this.player).typePet == 29)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet29);
            }
            if ((((Pet) this.player).typePet == 30)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet30);
            }
            if ((((Pet) this.player).typePet == 31)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet31);
            }
            if ((((Pet) this.player).typePet == 32)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet32);
            }
            if ((((Pet) this.player).typePet == 33)) {
                this.hpMax += calPercent(this.hpMax, cn.hpPet33);
            }
        }

        //phù
        if (this.player.zone
                != null && MapService.gI()
                        .isMapBlackBallWar(this.player.zone.map.mapId)) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax *= this.player.effectSkin.xHPKI;
        }
        //+hp đệ
        if (this.player.pet != null && this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {//Zalo: 0358124452//Name: EMTI 
            if ((this.player.pet.typePet >= 34)) {
                this.hpMax += calPercent(this.player.pet.nPoint.hpMax, this.player.getPointfusion().getHpFusion());
            }
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        //huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {//Zalo: 0358124452//Name: EMTI 
            if (this.player.effectSkill != null && this.player.effectSkill.tiLeHPHuytSao != 0) {//Zalo: 0358124452//Name: EMTI 
                this.hpMax += calPercent(this.hpMax, player.effectSkill.tiLeHPHuytSao);

            }
        }

        //bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseSupBi) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyetSC) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 15);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 15);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhNgot) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoTrongGoi) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseGaQuay) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.hpMax += calPercent(this.hpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax -= calPercent(this.hpMax, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax -= calPercent(this.hpMax, 55);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax -= calPercent(this.hpMax, 60);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax -= calPercent(this.hpMax, 65);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax -= calPercent(this.hpMax, 70);
        }
        if (this.player.zone
                != null && MapService.gI()
                        .isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax -= calPercent(this.hpMax, 50);
        }
        //set nhật ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nhatan == 5) {
            this.hpMax += calPercent(this.hpMax, 30);
        }
        //mèo mun
        if (player.capCS
                > 0) {//Zalo: 0358124452//Name: EMTI 
            if (player.capCS <= 10) {//Zalo: 0358124452//Name: EMTI 
                hpMax += (10000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {//Zalo: 0358124452//Name: EMTI 
                hpMax += (15000) * (player.capCS);
            }
            if (player.capCS > 20) {//Zalo: 0358124452//Name: EMTI 
                hpMax += (17000) * (player.capCS);
            }
        }
        if (player.capTT
                > 0) {//Zalo: 0358124452//Name: EMTI 
            if (player.capTT <= 10) {//Zalo: 0358124452//Name: EMTI 
                hpMax += (17000) * player.capTT;
            }
            if (player.capTT <= 20 && player.capTT > 10) {//Zalo: 0358124452//Name: EMTI 
                hpMax += (25000) * (player.capTT);
            }
            if (player.capTT > 20) {//Zalo: 0358124452//Name: EMTI 
                hpMax += (32000) * (player.capTT);
            }
        }

        //mèo mun
        if (this.player.effectFlagBag.useMeoMun) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 15);
        }
        if (this.player.lastTimeTitle1 > 0 && player.isTitleUse) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.lastTimeTitle2 > 0 && player.isTitleUse2) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 10);
        }
        if (this.player.lastTimeTitle3 > 0 && player.isTitleUse3) {//Zalo: 0358124452//Name: EMTI 
            this.hpMax += calPercent(this.hpMax, 20);
        }

    }
    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)

    private void setHp() {//Zalo: 0358124452//Name: EMTI 
        if (this.hp > this.hpMax) {//Zalo: 0358124452//Name: EMTI 
            this.hp = this.hpMax;
        }
    }

    private void setMpMax() {//Zalo: 0358124452//Name: EMTI 

        this.mpMax = this.mpg + this.mpAdd;

        for (Integer tl : this.tlMp) {
            if (tl != null) {
                this.mpMax += calPercent(this.mpMax, tl);
            }
        }
//set vo cuc tu tai
//khaile add
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 750);
        }
//end khaile add
        // Set picolo
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.picolo == 5) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 100);
        }
        //set nguyệt ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.nguyetan == 5) {
            this.mpMax += calPercent(this.mpMax, 30);
        }
        // Check if the "ngọc rồng đen" reward with 2 stars is still valid
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 

            this.mpMax += calPercent(this.mpMax, RewardBlackBall.R2S_1);
        }
//        if (this.player.zone
//                != null && MapService.gI()
//                        .isMapCold(this.player.zone.map)
//                && !this.isKhongLanh) {//Zalo: 0358124452//Name: EMTI 
//            this.mpMax -= calPercent(this.mpMax, 50);
//        }
        // Set worldcup
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.worldcup == 2) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 10);
        }
        if (this.player.isPet// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6)) {
            if (((Pet) this.player).typePet == 1) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet1);
            }
            if (((Pet) this.player).typePet == 2) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet2);
            }
            if (((Pet) this.player).typePet == 3) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet3);
            }
            if ((((Pet) this.player).typePet == 4)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet4);
            }
            if ((((Pet) this.player).typePet == 5)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet5);
            }
            if ((((Pet) this.player).typePet == 6)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet6);
            }
            if ((((Pet) this.player).typePet == 7)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet7);
            }
            if ((((Pet) this.player).typePet == 8)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet8);
            }
            if ((((Pet) this.player).typePet == 9)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet9);
            }
            if (((Pet) this.player).typePet == 10) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet10);
            }
            if ((((Pet) this.player).typePet == 11)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet11);
            }
            if ((((Pet) this.player).typePet == 12)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet12);
            }
            if ((((Pet) this.player).typePet == 13)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet13);
            }
            if ((((Pet) this.player).typePet == 14)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet14);
            }
            if ((((Pet) this.player).typePet == 15)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet15);
            }
            if ((((Pet) this.player).typePet == 16)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet16);
            }
            if ((((Pet) this.player).typePet == 17)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet17);
            }
            if ((((Pet) this.player).typePet == 18)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet18);
            }
            if ((((Pet) this.player).typePet == 19)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet19);
            }
            if ((((Pet) this.player).typePet == 20)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet20);
            }
            if ((((Pet) this.player).typePet == 21)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet21);
            }
            if ((((Pet) this.player).typePet == 22)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet22);
            }
            if ((((Pet) this.player).typePet == 23)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet23);
            }
            if ((((Pet) this.player).typePet == 24)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet24);
            }
            if ((((Pet) this.player).typePet == 25)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet25);
            }
            if ((((Pet) this.player).typePet == 26)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet26);
            }
            if ((((Pet) this.player).typePet == 27)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet27);
            }
            if ((((Pet) this.player).typePet == 28)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet28);
            }
            if ((((Pet) this.player).typePet == 29)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet29);
            }
            if ((((Pet) this.player).typePet == 30)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet30);
            }
            if ((((Pet) this.player).typePet == 31)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet31);
            }
            if ((((Pet) this.player).typePet == 32)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet32);
            }
            if ((((Pet) this.player).typePet == 33)) {
                this.mpMax += calPercent(this.mpMax, cn.mpPet33);
            }
        }
        //hợp thể

        if (this.player.pet != null && this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {//Zalo: 0358124452//Name: EMTI 
            if ((this.player.pet.typePet >= 34)) {
                this.mpMax += calPercent(this.player.pet.nPoint.mpMax, this.player.getPointfusion().getMpFusion());
            }
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        //bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhiSC) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoMotMat) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 30);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoDeo) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoTrongGoi) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseGaQuay) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 80);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {
            this.mpMax += calPercent(this.mpMax, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax -= calPercent(this.mpMax, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax -= calPercent(this.mpMax, 55);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax -= calPercent(this.mpMax, 60);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax -= calPercent(this.mpMax, 65);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax -= calPercent(this.mpMax, 70);
        }

        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax *= this.player.effectSkin.xHPKI;
        }
        //xiên cá
        if (player.capCS > 0) {//Zalo: 0358124452//Name: EMTI 
            if (player.capCS <= 10) {//Zalo: 0358124452//Name: EMTI 
                mpMax += (13000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {//Zalo: 0358124452//Name: EMTI 
                mpMax += (15000) * (player.capCS);
            }
            if (player.capCS > 20) {//Zalo: 0358124452//Name: EMTI 
                mpMax += (17500) * (player.capCS);
            }
        }

        //xiên cá
        if (this.player.effectFlagBag.useXienCa) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 15);
        }
        if (this.player.lastTimeTitle1 > 0 && player.isTitleUse) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 10);
        }
        if (this.player.lastTimeTitle2 > 0 && player.isTitleUse2) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 10);
        }
        if (this.player.lastTimeTitle3 > 0 && player.isTitleUse3) {//Zalo: 0358124452//Name: EMTI 
            this.mpMax += calPercent(this.mpMax, 20);
        }
    }

    private void setMp() {//Zalo: 0358124452//Name: EMTI 
        if (this.mp > this.mpMax) {//Zalo: 0358124452//Name: EMTI 
            this.mp = this.mpMax;
        }
    }

//    private final Object lockObject = new Object();
    private void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;

        for (Integer tl : this.tlDame) {
            if (tl != null) {
                this.dame += calPercent(this.dame, tl);
            }
        }

        for (Integer tl : this.tlSDDep) {
            if (tl != null) {
                this.dame += calPercent(this.dame, tl);
            }
        }
//set vo cuc tu tai
//khaile add
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 750);
        }
//end khaile add
        if (this.player.isPet// chi so lam sao bac tu cho dj
                && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA6)) {
            if (((Pet) this.player).typePet == 1) {
                this.dame += calPercent(this.dame, cn.damePet1);
            }
            if (((Pet) this.player).typePet == 2) {
                this.dame += calPercent(this.dame, cn.damePet2);
            }
            if (((Pet) this.player).typePet == 3) {
                this.dame += calPercent(this.dame, cn.damePet3);
            }
            if ((((Pet) this.player).typePet == 4)) {
                this.dame += calPercent(this.dame, cn.damePet4);
            }
            if ((((Pet) this.player).typePet == 5)) {
                this.dame += calPercent(this.dame, cn.damePet5);
            }
            if ((((Pet) this.player).typePet == 6)) {
                this.dame += calPercent(this.dame, cn.damePet6);
            }
            if ((((Pet) this.player).typePet == 7)) {
                this.dame += calPercent(this.dame, cn.damePet7);
            }
            if ((((Pet) this.player).typePet == 8)) {
                this.dame += calPercent(this.dame, cn.damePet8);
            }
            if ((((Pet) this.player).typePet == 9)) {
                this.dame += calPercent(this.dame, cn.damePet9);
            }
            if (((Pet) this.player).typePet == 10) {
                this.dame += calPercent(this.dame, cn.damePet10);
            }
            if ((((Pet) this.player).typePet == 11)) {
                this.dame += calPercent(this.dame, cn.damePet11);
            }
            if ((((Pet) this.player).typePet == 12)) {
                this.dame += calPercent(this.dame, cn.damePet12);
            }
            if ((((Pet) this.player).typePet == 13)) {
                this.dame += calPercent(this.dame, cn.damePet13);
            }
            if ((((Pet) this.player).typePet == 14)) {
                this.dame += calPercent(this.dame, cn.damePet14);
            }
            if ((((Pet) this.player).typePet == 15)) {
                this.dame += calPercent(this.dame, cn.damePet15);
            }
            if ((((Pet) this.player).typePet == 16)) {
                this.dame += calPercent(this.dame, cn.damePet16);
            }
            if ((((Pet) this.player).typePet == 17)) {
                this.dame += calPercent(this.dame, cn.damePet17);
            }
            if ((((Pet) this.player).typePet == 18)) {
                this.dame += calPercent(this.dame, cn.damePet18);
            }
            if ((((Pet) this.player).typePet == 19)) {
                this.dame += calPercent(this.dame, cn.damePet19);
            }
            if ((((Pet) this.player).typePet == 20)) {
                this.dame += calPercent(this.dame, cn.damePet20);
            }
            if ((((Pet) this.player).typePet == 21)) {
                this.dame += calPercent(this.dame, cn.damePet21);
            }
            if ((((Pet) this.player).typePet == 22)) {
                this.dame += calPercent(this.dame, cn.damePet22);
            }
            if ((((Pet) this.player).typePet == 23)) {
                this.dame += calPercent(this.dame, cn.damePet23);
            }
            if ((((Pet) this.player).typePet == 24)) {
                this.dame += calPercent(this.dame, cn.damePet24);
            }
            if ((((Pet) this.player).typePet == 25)) {
                this.dame += calPercent(this.dame, cn.damePet25);
            }
            if ((((Pet) this.player).typePet == 26)) {
                this.dame += calPercent(this.dame, cn.damePet26);
            }
            if ((((Pet) this.player).typePet == 27)) {
                this.dame += calPercent(this.dame, cn.damePet27);
            }
            if ((((Pet) this.player).typePet == 28)) {
                this.dame += calPercent(this.dame, cn.damePet28);
            }
            if ((((Pet) this.player).typePet == 29)) {
                this.dame += calPercent(this.dame, cn.damePet29);
            }
            if ((((Pet) this.player).typePet == 30)) {
                this.dame += calPercent(this.dame, cn.damePet30);
            }
            if ((((Pet) this.player).typePet == 31)) {
                this.dame += calPercent(this.dame, cn.damePet31);
            }
            if ((((Pet) this.player).typePet == 32)) {
                this.dame += calPercent(this.dame, cn.damePet32);
            }
            if ((((Pet) this.player).typePet == 33)) {
                this.dame += calPercent(this.dame, cn.damePet33);
            }
        }

        if (this.player.pet != null && this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {//Zalo: 0358124452//Name: EMTI 
            if ((this.player.pet.typePet >= 34)) {
                this.dame += calPercent(this.player.pet.nPoint.dame, this.player.getPointfusion().getDameFusion());
            }
            this.dame += this.player.pet.nPoint.dame;
        }
        //thức ăn
        if (!this.player.isPet && this.player.itemTime != null && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 10);
        }
        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 100);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNoSC) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 120);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhSau) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhTet) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseBanhChung) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKemOcQue) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 12);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseKeoTrongGoi) {//Zalo: 0358124452//Name: EMTI 
            this.dame -= calPercent(this.dame, 90);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseSaoBien) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isUseThapCap) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 20);
        }
        if (this.player.itemTime != null && this.player.itemTime.isBiNgo) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {//Zalo: 0358124452//Name: EMTI 
            this.dame -= calPercent(this.dame, 50);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {//Zalo: 0358124452//Name: EMTI 
            this.dame -= calPercent(this.dame, 55);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {//Zalo: 0358124452//Name: EMTI 
            this.dame -= calPercent(this.dame, 60);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {//Zalo: 0358124452//Name: EMTI 
            this.dame -= calPercent(this.dame, 65);
        }
        if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {//Zalo: 0358124452//Name: EMTI 
            this.dame -= calPercent(this.dame, 70);
        }

        //giảm dame
        this.dame -= calPercent(this.dame, tlSubSD);
        //map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {//Zalo: 0358124452//Name: EMTI 
            this.dame -= calPercent(this.dame, 60);
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 

            this.dame += calPercent(this.dame, RewardBlackBall.R2S_1);
        }

        //set worldcup
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.worldcup == 2) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 10);
            this.tlDameCrit.add(20);
        }

        //set tinh ấn
        if (this.player != null && this.player.setClothes != null && this.player.setClothes.tinhan == 5) {
            this.dame += calPercent(this.dame, 20);
        }
        //phóng heo
        if (this.player.effectFlagBag.usePhongHeo) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 15);
        }
        if (player.capCS > 0) {//Zalo: 0358124452//Name: EMTI 
            if (player.capCS <= 10) {//Zalo: 0358124452//Name: EMTI 
                dame += (5000) * player.capCS;
            }
            if (player.capCS <= 20 && player.capCS > 10) {//Zalo: 0358124452//Name: EMTI 
                dame += (7000) * (player.capCS);
            }
            if (player.capCS > 20) {//Zalo: 0358124452//Name: EMTI 
                dame += (9000) * (player.capCS);
            }
        }
        if (player.capTT > 0) {//Zalo: 0358124452//Name: EMTI 
            if (player.capTT <= 10) {//Zalo: 0358124452//Name: EMTI 
                dame += (9000) * player.capTT;
            }
            if (player.capTT <= 20 && player.capTT > 10) {//Zalo: 0358124452//Name: EMTI 
                dame += (11000) * (player.capTT);
            }
            if (player.capTT > 20) {//Zalo: 0358124452//Name: EMTI 
                dame += (15000) * (player.capTT);
            }
        }
        if (this.player.lastTimeTitle1 > 0 && player.isTitleUse) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.lastTimeTitle2 > 0 && player.isTitleUse2) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 10);
        }
        if (this.player.lastTimeTitle3 > 0 && player.isTitleUse3) {//Zalo: 0358124452//Name: EMTI 
            this.dame += calPercent(this.dame, 20);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {//Zalo: 0358124452//Name: EMTI 
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {//Zalo: 0358124452//Name: EMTI 
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                this.dame += calPercent(this.dame, percent);
            }
        }

    }

    private void setDef() {//Zalo: 0358124452//Name: EMTI 
        this.def = this.defg * 4;
        this.def += this.defAdd;
        //đồ
        for (Integer tl : this.tlDef) {//Zalo: 0358124452//Name: EMTI 
            this.def += calPercent(this.def, tl);
        }
//        if (this.player.isPet && (((Pet) this.player).typePet == 33)// chi so lam sao bac tu cho dj
//                ) {//Zalo: 0358124452//Name: EMTI 
//            this.def += (this.def * cn.damePet33 / 100);//chi so hp
//        }
        //ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 

            this.def += calPercent(this.def, RewardBlackBall.R2S_2);
        }
    }
//khaile modify

    private void setCrit() {
        this.crit = this.critg + this.critAdd;
        this.overflowcrit = crit;
        // Ngọc Rồng Đen 3 Sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.crit += RewardBlackBall.R3S_2;
        }

        // Biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 100;
        }

        // Xử lý phần chí mạng dư
        if (this.crit > 100) {
            int critOverflow = this.crit - 100;  // Phần dư của chí mạng
            tlDameCrit.add(critOverflow / 5);   // Chuyển phần dư thành tlDameCrit
            this.crit = 100;                    // Giới hạn chí mạng về 100
        }
    }
//end khaile modify

    private void resetPoint() {//Zalo: 0358124452//Name: EMTI 
        this.voHieuChuong = 0;
        this.hpAdd = 0;
        this.isThoDaiCa = false; //Cải trang Thỏ Đại Ca
        this.isDrabura = false; //Cải trang Dracula
        this.isDraburaFrost = false; //Cải trang Dracula Frost
        this.isSoHai = false; //Cải trang Dracula Frost
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlDame.clear();
        this.tlDameCrit.clear();
        this.tlDameAttMob.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.test = 0;
        this.teleport = false;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.khangTDHS = false;
    }

    public void addHp(long hp) {//Zalo: 0358124452//Name: EMTI 
        this.hp += hp;
        if (this.hp > this.hpMax) {//Zalo: 0358124452//Name: EMTI 
            this.hp = this.hpMax;
        }
    }

    public void addMp(long mp) {//Zalo: 0358124452//Name: EMTI 
        this.mp += mp;
        if (this.mp > this.mpMax) {//Zalo: 0358124452//Name: EMTI 
            this.mp = this.mpMax;
        }
    }

    public void setHp(long hp) {//Zalo: 0358124452//Name: EMTI 
        if (hp > this.hpMax) {//Zalo: 0358124452//Name: EMTI 
            this.hp = this.hpMax;
        } else {//Zalo: 0358124452//Name: EMTI 
            this.hp = hp;
        }
    }

    public void setMp(long mp) {//Zalo: 0358124452//Name: EMTI 
        if (mp > this.mpMax) {//Zalo: 0358124452//Name: EMTI 
            this.mp = this.mpMax;
        } else {//Zalo: 0358124452//Name: EMTI 
            this.mp = mp;
        }
    }

    private void setIsCrit() {//Zalo: 0358124452//Name: EMTI 
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {//Zalo: 0358124452//Name: EMTI 
            isCrit = true;
        } else if (isCrit100) {//Zalo: 0358124452//Name: EMTI 
            isCrit100 = false;
            isCrit = true;
        } else {//Zalo: 0358124452//Name: EMTI 
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }
//khaile add set vo cuc tu tai

    public double getDameAttack(boolean isAttackMob) {//Zalo: 0358124452//Name: EMTI 
        setIsCrit();
        double dameAttack = this.dame;
        double dameMP = this.mpMax;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        long percentDameSkill = 0;
        long percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {//Zalo: 0358124452//Name: EMTI 
            case Skill.DRAGON:
                if (intrinsic.id == 1) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                if (this.player.isPet && (((Pet) this.player).typePet == 33)) {
                    percentXDame = 100;
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.GALICK:
                if (intrinsic.id == 16) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.isPet && (((Pet) this.player).typePet == 33)) {
                    percentXDame = 100;
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.DEMON:
                if (intrinsic.id == 8) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.MASENKO:
                if (intrinsic.id == 9) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.masenko == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                if (this.player.isPet && (((Pet) this.player).typePet == 33)) {
                    percentXDame = 100;
                }

                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.KAIOKEN:
                if (intrinsic.id == 26) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kaioken == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 25;
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {//Zalo: 0358124452//Name: EMTI 
                    percentDameIntrinsic = intrinsic.param1;
                }
                if (this.player.setClothes.setVoCucTuTai == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 100;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {//Zalo: 0358124452//Name: EMTI 
                    percentXDame = 80;
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                dameAttack = Util.nextInt((int) (dameAttack - (dameAttack * 5 / 100)),
                        (int) (dameAttack + (dameAttack * 5 / 100)));
                return (int) dameAttack;
            case Skill.MAKANKOSAPPO:

                percentDameSkill = skillSelect.damage;
                long dameSkill = (int) (dameMP * percentDameSkill / 100);
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long damekk = calPercent(this.dame, 2000);
                if (this.player.setClothes.kirin == 5) {//Zalo: 0358124452//Name: EMTI 
                    damekk *= 2;
                }
//                dame = dame + (Util.nextInt(-5, 5) * dame / 100);
                damekk = damekk + (5 * damekk / 100);
                return damekk;
        }
        if (intrinsic.id == 18
                && this.player.effectSkill.isMonkey //                && this.player.effectSkill.isBienHinh
                ) {//Zalo: 0358124452//Name: EMTI 
            percentDameIntrinsic = intrinsic.param1;
        }

        if (percentDameSkill != 0) {//Zalo: 0358124452//Name: EMTI 
            dameAttack = dameAttack * percentDameSkill / 100;
        }
        dameAttack += (dameAttack * percentDameIntrinsic / 100);
        dameAttack += (dameAttack * dameAfter / 100);

        if (isAttackMob) {//Zalo: 0358124452//Name: EMTI 
            for (Integer tl : this.tlDameAttMob) {//Zalo: 0358124452//Name: EMTI 
                dameAttack += (dameAttack * tl / 100);
            }
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
            dameAttack *= 2;
        }
        if (isCrit) {//Zalo: 0358124452//Name: EMTI 
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {//Zalo: 0358124452//Name: EMTI 
                dameAttack += (dameAttack * tl / 100);
            }
        }
        dameAttack += ((long) dameAttack * percentXDame / 100);
        dameAttack = Util.Tamkjllnext((dameAttack - (dameAttack * 5 / 100)), (dameAttack + (dameAttack * 5 / 100)));
        if (player.isPl()) {//Zalo: 0358124452//Name: EMTI 
            if (player.inventory.haveOption(player.inventory.itemsBody, 5, 159)) {//Zalo: 0358124452//Name: EMTI 
                if (Util.canDoWithTime(player.lastTimeUseOption, 60000) && (player.playerSkill.skillSelect.skillId == Skill.KAMEJOKO || player.playerSkill.skillSelect.skillId == Skill.ANTOMIC || player.playerSkill.skillSelect.skillId == Skill.MASENKO)) {//Zalo: 0358124452//Name: EMTI 
                    dameAttack *= player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    player.lastTimeUseOption = System.currentTimeMillis();
                }
            }
        }

        //check activation set
        return dameAttack;
    }

    public int getCurrPercentHP() {//Zalo: 0358124452//Name: EMTI 
        if (this.hpMax == 0) {//Zalo: 0358124452//Name: EMTI 
            return 100;
        }
        return (int) ((long) this.hp * 100 / this.hpMax);
    }

    public int getCurrPercentMP() {//Zalo: 0358124452//Name: EMTI 
        return (int) ((long) this.mp * 100 / this.mpMax);
    }

    public void setFullHpMp() {//Zalo: 0358124452//Name: EMTI 
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void setFullHp() {//Zalo: 0358124452//Name: EMTI 
        this.hp = this.hpMax;
    }

    public void setFullMp() {//Zalo: 0358124452//Name: EMTI 
        this.mp = this.mpMax;
    }

    public void setFullStamina() {//Zalo: 0358124452//Name: EMTI 
        this.stamina = this.maxStamina;
    }

    public void setF20Stamina() {//Zalo: 0358124452//Name: EMTI 
        this.stamina += (short) (this.maxStamina * 20 / 100);
    }

    public void subHP(double sub) {//Zalo: 0358124452//Name: EMTI 
        this.hp -= sub;
        if (this.hp < 0) {//Zalo: 0358124452//Name: EMTI 
            this.hp = 0;
        }
    }

    public void subMP(double sub) {//Zalo: 0358124452//Name: EMTI 
        this.mp -= sub;
        if (this.mp < 0) {//Zalo: 0358124452//Name: EMTI 
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {//Zalo: 0358124452//Name: EMTI 
        if (power < getPowerLimit()) {//Zalo: 0358124452//Name: EMTI 
            for (Integer tl : this.tlTNSM) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tiemNang * tl / 100);
            }
            if (this.player.cFlag != 0) {//Zalo: 0358124452//Name: EMTI 
                if (this.player.cFlag == 8) {//Zalo: 0358124452//Name: EMTI 
                    tiemNang += ((long) tiemNang * 10 / 100);
                } else {//Zalo: 0358124452//Name: EMTI 
                    tiemNang += ((long) tiemNang * 5 / 100);
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += tn * 3;
            }
            if (this.player.itemTime != null && this.player.itemTime.isUseVooc) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tn * 20 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx2) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tn * 100 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx5) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tn * 250 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx7) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tn * 350 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx10) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tn * 500 / 100);
            }
            if (this.player.itemTime != null && this.player.itemTime.isLoNuocThanhx15) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tn * 750 / 100);
            }

            if (this.intrinsic != null && this.intrinsic.id == 24) {//Zalo: 0358124452//Name: EMTI 
                tiemNang += ((long) tn * this.intrinsic.param1 / 100);
            }
////khaile comment
//            if (this.power >= 900000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 999999 / 1000000);
//            } else if (this.power >= 800000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 99999 / 100000);
//            } else if (this.power >= 700000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 99995 / 100000);
//            } else if (this.power >= 600000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 9999 / 10000);
//            } else if (this.power >= 250000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 9990 / 10000);
//            } else if (this.power >= 200000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 9960 / 10000);
//            } else if (this.power >= 150000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 995 / 1000);
//            } else if (this.power >= 60000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 955 / 1000);
//            } else if (this.power >= 40000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 900 / 1000);
//            } else if (this.power >= 20000000000L) {//Zalo: 0358124452//Name: EMTI 
//                tiemNang -= ((long) tn * 800 / 1000);
//            }
//end khaile comment
            if (this.player != null && this.player.zone != null) {
                if (MapService.gI().isMapDoanhTrai(player.zone.map.mapId)) {
                    tiemNang = tn * 5;
                }
                if (MapService.gI().isMapBanDoKhoBau(player.zone.map.mapId)) {
                    tiemNang = tn * 4;
                }
                if (MapService.gI().isMapConDuongRanDoc(player.zone.map.mapId)) {
                    tiemNang = tn * 4;
                }
                if (MapService.gI().isNguHanhSon(player.zone.map.mapId)) {
                    tiemNang = tn * 3;
                }
            } else {

                System.err.print("Eror x tnsm");
            }
            if (this.player.isPet) {//Zalo: 0358124452//Name: EMTI 
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {//Zalo: 0358124452//Name: EMTI 
                    tiemNang += tn * 2;
                }

            }
            tiemNang *= Manager.RATE_EXP_SERVER;

            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {//Zalo: 0358124452//Name: EMTI 
                tiemNang = 1;
            }

        } else {//Zalo: 0358124452//Name: EMTI 
            tiemNang = 10;
        }
        return tiemNang;
    }

    public long calSubTNSM(long tiemNang) {//Zalo: 0358124452//Name: EMTI 
        //khaile comment
//        if (power >= 701000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 999 / 1000);
//        } else if (power >= 501000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 980 / 1000);
//        } else if (power >= 301000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 98 / 100);
//        } else if (power >= 201000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 97 / 100);
//        } else if (power >= 150000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 95 / 100);
//        } else if (power >= 109000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 91 / 100);
//        } else if (power >= 180000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 90 / 100);
//        } else if (power >= 110000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 85 / 100);
//        } else if (power >= 100000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 80 / 100);
//        } else if (power >= 90000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 75 / 100);
//        } else if (power >= 80000000000L) {//Zalo: 0358124452//Name: EMTI 
//            tiemNang -= ((long) tiemNang * 70 / 100);
//        }
//end khaile comment
        return tiemNang;
    }

    public short getTileHutHp(boolean isMob) {//Zalo: 0358124452//Name: EMTI 
        if (isMob) {//Zalo: 0358124452//Name: EMTI 
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {//Zalo: 0358124452//Name: EMTI 
            return this.tlHutHp;
        }
    }

    public short getTiLeHutMp() {//Zalo: 0358124452//Name: EMTI 

        return this.tlHutMp;

    }

    public double subDameInjureWithDeff(double dame) {//Zalo: 0358124452//Name: EMTI 
        long def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {//Zalo: 0358124452//Name: EMTI 
            dame /= 2;
        }
        if (dame < 0) {//Zalo: 0358124452//Name: EMTI 
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {//Zalo: 0358124452//Name: EMTI 
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {//Zalo: 0358124452//Name: EMTI 
        switch (limitPower) {//Zalo: 0358124452//Name: EMTI 
            case 0:
                return 17999999999L;
            case 1:
                return 18999999999L;
            case 2:
                return 20999999999L;
            case 3:
                return 24999999999L;
            case 4:
                return 30999999999L;
            case 5:
                return 40999999999L;
            case 6:
                return 60999999999L;
            case 7:
                return 80999999999L;
            case 8:
                return 90999999999L;
            case 9:
                return 100999999999L;
            case 10:
                return 110999999999L;
            case 11:
                return 120099999999L;
            case 12:
                return 300000000000L;
            case 13:
                return 350000000000L;
            case 14:
                return 400000000000L;
            case 15:
                return 500000000000L;
            case 16:
                return 600000000000L;
            case 17:
                return 700000000000L;
            case 18:
                return 800000000000L;
            case 19:
                return 900000000000L;
            case 20:
                return 1000000000000L;

            default:
                return 0;
        }
    }

    public long getPowerNextLimit() {//Zalo: 0358124452//Name: EMTI 
        switch (limitPower + 1) {//Zalo: 0358124452//Name: EMTI 
            case 0:
                return 17999999999L;
            case 1:
                return 18999999999L;
            case 2:
                return 20999999999L;
            case 3:
                return 24999999999L;
            case 4:
                return 30999999999L;
            case 5:
                return 40999999999L;
            case 6:
                return 60999999999L;
            case 7:
                return 80999999999L;
            case 8:
                return 90999999999L;
            case 9:
                return 100999999999L;
            case 10:
                return 110999999999L;
            case 11:
                return 120099999999L;
            case 12:
                return 300000000000L;
            case 13:
                return 350000000000L;
            case 14:
                return 400000000000L;
            case 15:
                return 500000000000L;
            case 16:
                return 600000000000L;
            case 17:
                return 700000000000L;
            case 18:
                return 800000000000L;
            case 19:
                return 900000000000L;
            case 20:
                return 1000000000000L;

            default:
                return 0;
        }
    }

    public int getHpMpLimit() {//Zalo: 0358124452//Name: EMTI 
        if (limitPower == 0) {//Zalo: 0358124452//Name: EMTI 
            return 220000;
        }
        if (limitPower == 1) {//Zalo: 0358124452//Name: EMTI 
            return 240000;
        }
        if (limitPower == 2) {//Zalo: 0358124452//Name: EMTI 
            return 260000;
        }
        if (limitPower == 3) {//Zalo: 0358124452//Name: EMTI 
            return 280000;
        }
        if (limitPower == 4) {//Zalo: 0358124452//Name: EMTI 
            return 300000;
        }
        if (limitPower == 5) {//Zalo: 0358124452//Name: EMTI 
            return 350000;
        }
        if (limitPower == 6) {//Zalo: 0358124452//Name: EMTI 
            return 370000;
        }
        if (limitPower == 7) {//Zalo: 0358124452//Name: EMTI 
            return 400000;
        }
        if (limitPower == 8) {//Zalo: 0358124452//Name: EMTI 
            return 450000;
        }
        if (limitPower == 9) {//Zalo: 0358124452//Name: EMTI 
            return 500000;
        }
        if (limitPower == 10) {//Zalo: 0358124452//Name: EMTI 
            return 550000;
        }
        if (limitPower == 11) {//Zalo: 0358124452//Name: EMTI 
            return 620000;
        }
        if (limitPower == 12) {//Zalo: 0358124452//Name: EMTI 
            return 700000;
        }
        if (limitPower == 13) {//Zalo: 0358124452//Name: EMTI 
            return 740000;
        }
        if (limitPower == 14) {//Zalo: 0358124452//Name: EMTI 
            return 800000;
        }
        if (limitPower == 15) {//Zalo: 0358124452//Name: EMTI 
            return 850000;
        }
        if (limitPower == 16) {//Zalo: 0358124452//Name: EMTI 
            return 900000;
        }
        if (limitPower == 17) {//Zalo: 0358124452//Name: EMTI 
            return 1000000;
        }
        if (limitPower == 18) {//Zalo: 0358124452//Name: EMTI 
            return 1100000;
        }
        if (limitPower == 19) {//Zalo: 0358124452//Name: EMTI 
            return 1200000;
        }
        if (limitPower == 20) {//Zalo: 0358124452//Name: EMTI 
            return 1300000;
        }
        return 0;
    }

    public int getDameLimit() {//Zalo: 0358124452//Name: EMTI 
        if (limitPower == 0) {//Zalo: 0358124452//Name: EMTI 
            return 11000;
        }
        if (limitPower == 1) {//Zalo: 0358124452//Name: EMTI 
            return 12000;
        }
        if (limitPower == 2) {//Zalo: 0358124452//Name: EMTI 
            return 13000;
        }
        if (limitPower == 3) {//Zalo: 0358124452//Name: EMTI 
            return 14000;
        }
        if (limitPower == 4) {//Zalo: 0358124452//Name: EMTI 
            return 14000;
        }
        if (limitPower == 5) {//Zalo: 0358124452//Name: EMTI 
            return 16000;
        }
        if (limitPower == 6) {//Zalo: 0358124452//Name: EMTI 
            return 17000;
        }
        if (limitPower == 7) {//Zalo: 0358124452//Name: EMTI 
            return 22000;
        }
        if (limitPower == 8) {//Zalo: 0358124452//Name: EMTI 
            return 25000;
        }
        if (limitPower == 9) {//Zalo: 0358124452//Name: EMTI 
            return 32000;
        }
        if (limitPower == 10) {//Zalo: 0358124452//Name: EMTI 
            return 33000;
        }
        if (limitPower == 11) {//Zalo: 0358124452//Name: EMTI 
            return 34000;
        }
        if (limitPower == 12) {//Zalo: 0358124452//Name: EMTI 
            return 37000;

        }
        if (limitPower == 13) {//Zalo: 0358124452//Name: EMTI 
            return 38000;
        }
        if (limitPower == 14) {//Zalo: 0358124452//Name: EMTI 
            return 39000;
        }
        if (limitPower == 15) {//Zalo: 0358124452//Name: EMTI 
            return 40000;
        }
        if (limitPower == 16) {//Zalo: 0358124452//Name: EMTI 
            return 41000;
        }
        if (limitPower == 17) {//Zalo: 0358124452//Name: EMTI 
            return 42000;
        }
        if (limitPower == 18) {//Zalo: 0358124452//Name: EMTI 
            return 43000;
        }
        if (limitPower == 19) {//Zalo: 0358124452//Name: EMTI 
            return 44000;
        }
        if (limitPower == 20) {//Zalo: 0358124452//Name: EMTI 
            return 45000;
        }

        return 0;
    }

    public int getDefLimit() {//Zalo: 0358124452//Name: EMTI 
        if (limitPower == 0) {//Zalo: 0358124452//Name: EMTI 
            return 5500;
        }
        if (limitPower == 1) {//Zalo: 0358124452//Name: EMTI 
            return 6000;
        }
        if (limitPower == 2) {//Zalo: 0358124452//Name: EMTI 
            return 7000;
        }
        if (limitPower == 3) {//Zalo: 0358124452//Name: EMTI 
            return 8000;
        }
        if (limitPower == 4) {//Zalo: 0358124452//Name: EMTI 
            return 10000;
        }
        if (limitPower == 5) {//Zalo: 0358124452//Name: EMTI 
            return 12000;
        }
        if (limitPower == 6) {//Zalo: 0358124452//Name: EMTI 
            return 14000;
        }
        if (limitPower == 7) {//Zalo: 0358124452//Name: EMTI 
            return 16000;
        }
        if (limitPower == 8) {//Zalo: 0358124452//Name: EMTI 
            return 17000;
        }
        if (limitPower == 9) {//Zalo: 0358124452//Name: EMTI 
            return 18000;
        }
        if (limitPower == 10) {//Zalo: 0358124452//Name: EMTI 
            return 20000;
        }
        if (limitPower == 11) {//Zalo: 0358124452//Name: EMTI 
            return 25000;
        }
        if (limitPower == 12) {//Zalo: 0358124452//Name: EMTI 
            return 30000;

        }
        if (limitPower == 13) {//Zalo: 0358124452//Name: EMTI 
            return 40000;
        }
        if (limitPower == 14) {//Zalo: 0358124452//Name: EMTI 
            return 50000;
        }
        if (limitPower == 15) {//Zalo: 0358124452//Name: EMTI 
            return 60000;
        }
        if (limitPower == 16) {//Zalo: 0358124452//Name: EMTI 
            return 70000;
        }
        if (limitPower == 17) {//Zalo: 0358124452//Name: EMTI 
            return 80000;
        }
        if (limitPower == 18) {//Zalo: 0358124452//Name: EMTI 
            return 90000;
        }
        if (limitPower == 19) {//Zalo: 0358124452//Name: EMTI 
            return 100000;
        }
        if (limitPower == 20) {//Zalo: 0358124452//Name: EMTI 
            return 200000;
        }

        return 0;
    }

    public byte getCritLimit() {//Zalo: 0358124452//Name: EMTI 
        if (limitPower == 0) {//Zalo: 0358124452//Name: EMTI 
            return 5;
        }
        if (limitPower == 1) {//Zalo: 0358124452//Name: EMTI 
            return 6;
        }
        if (limitPower == 2) {//Zalo: 0358124452//Name: EMTI 
            return 7;
        }
        if (limitPower == 3) {//Zalo: 0358124452//Name: EMTI 
            return 8;
        }
        if (limitPower == 4) {//Zalo: 0358124452//Name: EMTI 
            return 9;
        }
        if (limitPower == 5) {//Zalo: 0358124452//Name: EMTI 
            return 10;
        }
        if (limitPower == 6) {//Zalo: 0358124452//Name: EMTI 
            return 10;
        }
        if (limitPower == 7) {//Zalo: 0358124452//Name: EMTI 
            return 10;
        }
        if (limitPower == 8) {//Zalo: 0358124452//Name: EMTI 
            return 10;
        }
        if (limitPower == 9) {//Zalo: 0358124452//Name: EMTI 
            return 10;
        }
        if (limitPower == 10) {//Zalo: 0358124452//Name: EMTI 
            return 10;
        }
        if (limitPower == 11) {//Zalo: 0358124452//Name: EMTI 
            return 10;
        }
        if (limitPower == 12) {//Zalo: 0358124452//Name: EMTI 
            return 10;

        }
        if (limitPower == 13) {//Zalo: 0358124452//Name: EMTI 
            return 11;
        }
        if (limitPower == 14) {//Zalo: 0358124452//Name: EMTI 
            return 11;
        }
        if (limitPower == 15) {//Zalo: 0358124452//Name: EMTI 
            return 11;
        }
        if (limitPower == 16) {//Zalo: 0358124452//Name: EMTI 
            return 11;
        }
        if (limitPower == 17) {//Zalo: 0358124452//Name: EMTI 
            return 11;
        }
        if (limitPower == 18) {//Zalo: 0358124452//Name: EMTI 
            return 12;
        }
        if (limitPower == 19) {//Zalo: 0358124452//Name: EMTI 
            return 12;

        }
        if (limitPower == 20) {//Zalo: 0358124452//Name: EMTI 
            return 12;

        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(long power) {//Zalo: 0358124452//Name: EMTI 
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {//Zalo: 0358124452//Name: EMTI 
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point, boolean manualForPet) {
        if (point <= 0 || point > 100) {
            return;
        }
        long tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2L * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2L * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= getHpMpLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2L * this.dameg + point - 1) / 2 * 100;
            if ((this.dameg + point) <= getDameLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 3) {
            int defOld = this.defg;
            tiemNangUse = 2L * (defOld + 5) * point / 2 * 100000;
            if ((defOld + point) <= getDefLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 4) {
            int critOld = this.critg;
            tiemNangUse = 50000000L;
            for (int i = 0; i < critOld; i++) {
                tiemNangUse *= 5L;
            }
            if ((critOld + point) <= getCritLimit()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                }
            } else {
                Service.gI().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        Service.gI().point(player);
        if (manualForPet) {
            Service.gI().sendChiSoPetGoc(((Pet) player).master);
            Service.gI().showInfoPet(((Pet) player).master);
            Service.gI().point(((Pet) player).master);
        }
    }

    private boolean doUseTiemNang(long tiemNang) {//Zalo: 0358124452//Name: EMTI 
        if (this.tiemNang < tiemNang) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang && this.tiemNang - tiemNang >= 0) {//Zalo: 0358124452//Name: EMTI 
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;

    public void update() {//Zalo: 0358124452//Name: EMTI 
        if (player != null && player.effectSkill != null) {//Zalo: 0358124452//Name: EMTI 
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {//Zalo: 0358124452//Name: EMTI 
                int tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {//Zalo: 0358124452//Name: EMTI 
                    PlayerService.gI().hoiPhuc(player, hpMax / 100 * tiLeHoiPhuc,
                            mpMax / 100 * tiLeHoiPhuc);
                    if (player.effectSkill.countCharging % 3 == 0) {//Zalo: 0358124452//Name: EMTI 
                        Service.gI().chat(player, "Phục hồi năng lượng " + getCurrPercentHP() + "%");
                    }
                } else {//Zalo: 0358124452//Name: EMTI 
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {//Zalo: 0358124452//Name: EMTI 
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {//Zalo: 0358124452//Name: EMTI 
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {//Zalo: 0358124452//Name: EMTI 
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {//Zalo: 0358124452//Name: EMTI 
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    public void dispose() {//Zalo: 0358124452//Name: EMTI 
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
    }
}
