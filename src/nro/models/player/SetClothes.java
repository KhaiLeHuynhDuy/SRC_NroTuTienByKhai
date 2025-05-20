package nro.models.player;

import nro.models.item.Item;

public class SetClothes {//Zalo: 0358124452//Name: EMTI 

    private Player player;

    public SetClothes(Player player) {//Zalo: 0358124452//Name: EMTI 
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;
    public byte kaioken;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;
    public byte masenko;

    public byte kakarot;
    public byte cadic;
    public byte nappa;
    public byte xayda;

    public byte worldcup;
    public byte setDHD;

    public byte setDTS;
    public byte setDTL;
    public byte tinhan;
    public byte nguyetan;
    public byte nhatan;

//    public boolean chanmenh;

    public boolean huydietClothers;
    public boolean godClothes;
    public int ctHaiTac = -1;

    public void setup() {//Zalo: 0358124452//Name: EMTI 
        setDefault();
        setupSKT();
        setupAN();
        setupDTS();
        setupDHD();
        setupDTL();
//        setChanmenh();
        this.godClothes = true;
        for (int i = 0; i < 6; i++) {//Zalo: 0358124452//Name: EMTI 
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                if (item.template.id > 567 || item.template.id < 555) {//Zalo: 0358124452//Name: EMTI 
                    this.godClothes = false;
                    break;
                }
            } else {//Zalo: 0358124452//Name: EMTI 
                this.godClothes = false;
                break;
            }
        }

        this.huydietClothers = true;
        for (int i = 0; i < 6; i++) {//Zalo: 0358124452//Name: EMTI 
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                if (item.template.id >= 649 || item.template.id <= 663) {//Zalo: 0358124452//Name: EMTI 
                    this.huydietClothers = true;
                    break;
                }
            } else {//Zalo: 0358124452//Name: EMTI 
                this.huydietClothers = false;
                break;
            }
        }

        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
            switch (ct.template.id) {//Zalo: 0358124452//Name: EMTI 
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
//                case 1209:
//                case 1210:
//                case 1153:
//                case 1154:
//                case 1155:
                    this.ctHaiTac = ct.template.id;
                    break;

            }
        }
    }

    public boolean setGod14() {//Zalo: 0358124452//Name: EMTI 
        for (int i = 0; i < 6; i++) {//Zalo: 0358124452//Name: EMTI 
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                if (item.template.id >= 650 && item.template.id <= 663) {//Zalo: 0358124452//Name: EMTI 
                    i++;
                } else if (i == 5) {//Zalo: 0358124452//Name: EMTI 
                    this.huydietClothers = true;
                    break;
                }
            } else {//Zalo: 0358124452//Name: EMTI 
                this.huydietClothers = false;
                break;
            }
        }
        return this.huydietClothers ? true : false;
    }

    private void setupDTS() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 120) {
                                setDTS++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupDHD() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupDTL() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 15) {
                                setDTL++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {

                break;
            }
        }
    }

//    private boolean setChanmenh() {
//        for (int i = 0; i < 2; i++) {//Zalo: 0358124452//Name: EMTI 
//            Item item = this.player.inventory.itemsBody.get(i);
//            if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
//                if (item.template.id >= 1185 && item.template.id <= 1193) {//Zalo: 0358124452//Name: EMTI 
//                    i++;
//                } else if (i == 1) {//Zalo: 0358124452//Name: EMTI 
//                    this.chanmenh = true;
//                    break;
//                }
//            } else {//Zalo: 0358124452//Name: EMTI 
//                this.chanmenh = false;
//                break;
//            }
//        }
//        return this.chanmenh ? true : false;
//    }

    private void setupAN() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSett = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 34:
                            isActSett = true;
                            tinhan++;
                            break;
                        case 35:
                            isActSett = true;
                            nguyetan++;
                            break;
                        case 36:
                            isActSett = true;
                            nhatan++;
                            break;
                    }
                    if (isActSett) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupSKT() {//Zalo: 0358124452//Name: EMTI 
        for (int i = 0; i < 6; i++) {//Zalo: 0358124452//Name: EMTI 
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                    switch (io.optionTemplate.id) {//Zalo: 0358124452//Name: EMTI 
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                        case 212:
                        case 213:
                            isActSet = true;
                            kaioken++;
                            break;
                        case 214:
                        case 215:
                            isActSet = true;
                            masenko++;
                            break;
                        case 216:
                        case 217:
                            isActSet = true;
                            xayda++;
                            break;

                        case 210:
                            isActSet = true;
                            setDHD++;
                            break;
                        case 21:
                            if (io.param == 80) {//Zalo: 0358124452//Name: EMTI 
                                setDHD++;
                            }
                            break;
                    }

                    if (isActSet) {//Zalo: 0358124452//Name: EMTI 
                        break;
                    }
                }
            } else {//Zalo: 0358124452//Name: EMTI 
                break;
            }
        }
    }

    private void setDefault() {//Zalo: 0358124452//Name: EMTI 
        this.songoku = 0;
        this.kaioken = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.masenko = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.xayda = 0;
        this.setDHD = 0;
        this.worldcup = 0;
        this.tinhan = 0;
        this.nhatan = 0;
        this.nguyetan = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
    }

    public void dispose() {//Zalo: 0358124452//Name: EMTI 
        this.player = null;
    }
}
