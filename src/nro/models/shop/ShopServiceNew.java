package nro.models.shop;

import nro.models.item.Item;
import nro.models.player.Inventory;
import nro.models.player.Player;
import nro.network.io.Message;
import nro.server.Manager;
import nro.services.InventoryServiceNew;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Logger;
import nro.utils.Util;

import java.util.List;
import nro.consts.ConstNpc;
import nro.models.item.Item.ItemOption;

public class ShopServiceNew {//Zalo: 0358124452//Name: EMTI 

    private static final byte COST_GOLD = 0;
    private static final byte COST_GEM = 1;
    private static final byte COST_ITEM_SPEC = 2;
    private static final byte COST_RUBY = 3;
    private static final byte COST_COUPON = 4;
    private static final byte COST_EVENT = 5;
    private static final byte COST_DIEM = 6;
    private static final byte NORMAL_SHOP = 0;
    private static final byte SPEC_SHOP = 3;
    private static final byte BOX = 4;

    private static ShopServiceNew I;

    public static ShopServiceNew gI() {//Zalo: 0358124452//Name: EMTI 
        if (ShopServiceNew.I == null) {//Zalo: 0358124452//Name: EMTI 
            ShopServiceNew.I = new ShopServiceNew();
        }
        return ShopServiceNew.I;
    }

    public void opendShop(Player player, String tagName, boolean allGender) {//Zalo: 0358124452//Name: EMTI 
        if (tagName.equals("ITEMS_MAIL_BOX")) {
            openShopType4(player, tagName, player.inventory.itemsMailBox);
            return;
        }
        if (tagName.equals("RUONG_PHU")) {
            openShopType4(player, tagName, player.inventory.itemsRuongPhu);
            return;
        }
        if (tagName.equals("ITEMS_LUCKY_ROUND")) {//Zalo: 0358124452//Name: EMTI 
            openShopType4(player, tagName, player.inventory.itemsBoxCrackBall);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {//Zalo: 0358124452//Name: EMTI 
            player.getSession().initItemsReward();
            return;
        }
        try {//Zalo: 0358124452//Name: EMTI 
            Shop shop = this.getShop(tagName);
            shop = this.resolveShop(player, shop, allGender);
            switch (shop.typeShop) {//Zalo: 0358124452//Name: EMTI 
                case NORMAL_SHOP:
                    openShopType0(player, shop);
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case SPEC_SHOP:
                    openShopType3(player, shop);
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                }
        } catch (Exception ex) {//Zalo: 0358124452//Name: EMTI 
            ex.printStackTrace();
            Service.gI().sendThongBao(player, ex.getMessage());
        }
    }

    private Shop getShop(String tagName) throws Exception {//Zalo: 0358124452//Name: EMTI 
        for (Shop s : Manager.SHOPS) {//Zalo: 0358124452//Name: EMTI 
            if (s.tagName != null && s.tagName.equals(tagName)) {//Zalo: 0358124452//Name: EMTI 
                return s;
            }
        }
        throw new Exception("Shop " + tagName + " không tồn tại!");
    }

//    private void _________________Xử_lý_cửa_hàng_trước_khi_gửi_______________() {//Zalo: 0358124452//Name: EMTI 
//        //**********************************************************************
//    }
    private Shop resolveShop(Player player, Shop shop, boolean allGender) {//Zalo: 0358124452//Name: EMTI 
        if (shop.tagName != null && (shop.tagName.equals("BUA_1H")
                || shop.tagName.equals("BUA_8H") || shop.tagName.equals("BUA_1M"))) {//Zalo: 0358124452//Name: EMTI 
            return this.resolveShopBua(player, new Shop(shop));
        }
        return allGender ? new Shop(shop) : new Shop(shop, player.gender);
    }

    private Shop resolveShopBua(Player player, Shop s) {//Zalo: 0358124452//Name: EMTI 
        for (TabShop tabShop : s.tabShops) {//Zalo: 0358124452//Name: EMTI 
            for (ItemShop item : tabShop.itemShops) {//Zalo: 0358124452//Name: EMTI 
                long min = 0;
                switch (item.temp.id) {//Zalo: 0358124452//Name: EMTI 
                    case 213:
                        long timeTriTue = player.charms.tdTriTue;
                        long current = System.currentTimeMillis();
                        min = (timeTriTue - current) / 60000;

                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 214:
                        min = (player.charms.tdManhMe - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 215:
                        min = (player.charms.tdDaTrau - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 216:
                        min = (player.charms.tdOaiHung - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 217:
                        min = (player.charms.tdBatTu - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 218:
                        min = (player.charms.tdDeoDai - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 219:
                        min = (player.charms.tdThuHut - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 522:
                        min = (player.charms.tdDeTu - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 671:
                        min = (player.charms.tdTriTue3 - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                    case 672:
                        min = (player.charms.tdTriTue4 - System.currentTimeMillis()) / 60000;
                        break;                                //Zalo: 0358124452                                //Name: EMTI 
                }
                if (min > 0) {//Zalo: 0358124452//Name: EMTI 
                    item.options.clear();
                    if (min >= 1440) {//Zalo: 0358124452//Name: EMTI 
                        item.options.add(new Item.ItemOption(63, (int) min / 1440));
                    } else if (min >= 60) {//Zalo: 0358124452//Name: EMTI 
                        item.options.add(new Item.ItemOption(64, (int) min / 60));
                    } else {//Zalo: 0358124452//Name: EMTI 
                        item.options.add(new Item.ItemOption(65, (int) min));
                    }
                }
            }
        }
        return s;
    }

//    private void _________________Gửi_cửa_hàng_cho_người_chơi________________() {//Zalo: 0358124452//Name: EMTI 
//        //**********************************************************************
//    }
    private void openShopType0(Player player, Shop shop) {//Zalo: 0358124452//Name: EMTI 
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {//Zalo: 0358124452//Name: EMTI 
            Message msg;
            try {//Zalo: 0358124452//Name: EMTI 
                msg = new Message(-44);
                msg.writer().writeByte(NORMAL_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {//Zalo: 0358124452//Name: EMTI 
                        msg.writer().writeShort(itemShop.temp.id);
                        if (itemShop.typeSell == COST_GOLD) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(itemShop.cost);
                            msg.writer().writeInt(0);
                        } else if (itemShop.typeSell == COST_GEM) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_RUBY) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        } else if (itemShop.typeSell == COST_COUPON) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeInt(0);
                            msg.writer().writeInt(itemShop.cost);
                        }
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

    private void openShopType3(Player player, Shop shop) {//Zalo: 0358124452//Name: EMTI 
        player.iDMark.setShopOpen(shop);
        player.iDMark.setTagNameShop(shop.tagName);
        if (shop != null) {//Zalo: 0358124452//Name: EMTI 
            Message msg;
            try {//Zalo: 0358124452//Name: EMTI 
                msg = new Message(-44);
                msg.writer().writeByte(SPEC_SHOP);
                msg.writer().writeByte(shop.tabShops.size());
                for (TabShop tab : shop.tabShops) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeUTF(tab.name);
                    msg.writer().writeByte(tab.itemShops.size());
                    for (ItemShop itemShop : tab.itemShops) {//Zalo: 0358124452//Name: EMTI 
                        msg.writer().writeShort(itemShop.temp.id);
                        msg.writer().writeShort(itemShop.iconSpec);
                        msg.writer().writeInt(itemShop.cost);
                        msg.writer().writeByte(itemShop.options.size());
                        for (Item.ItemOption option : itemShop.options) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(option.optionTemplate.id);
                            msg.writer().writeShort(option.param);
                        }
                        msg.writer().writeByte(itemShop.isNew ? 1 : 0);
                        if (itemShop.temp.type == 5) {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(1);
                            msg.writer().writeShort(itemShop.temp.head);
                            msg.writer().writeShort(itemShop.temp.body);
                            msg.writer().writeShort(itemShop.temp.leg);
                            msg.writer().writeShort(-1);
                        } else {//Zalo: 0358124452//Name: EMTI 
                            msg.writer().writeByte(0);
                        }
                    }
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 
                e.printStackTrace();
                Logger.logException(ShopServiceNew.class, e);
            }
        }
    }

    private void openShopType4(Player player, String tagName, List<Item> items) {//Zalo: 0358124452//Name: EMTI 
        if (items == null) {//Zalo: 0358124452//Name: EMTI 
            return;
        }
        player.iDMark.setTagNameShop(tagName);
        Message msg;
        try {//Zalo: 0358124452//Name: EMTI 
            msg = new Message(-44);
            msg.writer().writeByte(4);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("Phần\nthưởng");
            msg.writer().writeByte(items.size());
            for (Item item : items) {//Zalo: 0358124452//Name: EMTI 
                msg.writer().writeShort(item.template.id);
                msg.writer().writeUTF("\n|7|NR BLUE");
                msg.writer().writeByte(item.itemOptions.size() + 1);
                for (Item.ItemOption io : item.itemOptions) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeByte(io.optionTemplate.id);
                    msg.writer().writeShort(io.param);
                }
                //số lượng
                msg.writer().writeByte(31);
                msg.writer().writeShort(item.quantity);
                //
                msg.writer().writeByte(1);
                if (item.template.type == 5) {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeByte(1);
                    msg.writer().writeShort(item.template.head);
                    msg.writer().writeShort(item.template.body);
                    msg.writer().writeShort(item.template.leg);
                    msg.writer().writeShort(-1);
                } else {//Zalo: 0358124452//Name: EMTI 
                    msg.writer().writeByte(0);
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 

            e.printStackTrace();
        }
    }

//    private void _________________Mua_vật_phẩm______________________________() {//Zalo: 0358124452//Name: EMTI 
//        //**********************************************************************
//    }
    public void takeItem(Player player, byte type, int tempId, int quantity) {//Zalo: 0358124452//Name: EMTI 
        String tagName = player.iDMark.getTagNameShop();
        if (tagName == null || tagName.length() <= 0) {//Zalo: 0358124452//Name: EMTI 
            return;
        }

        if (tagName.equals("ITEMS_MAIL_BOX")) {//Zalo: 0358124452//Name: EMTI 
            getItemSideMailBox(player, player.inventory.itemsMailBox, type, tempId);
            return;
        }
        if (tagName.equals("RUONG_PHU")) {//Zalo: 0358124452//Name: EMTI 
            getItemSideRuongPhu(player, player.inventory.itemsRuongPhu, type, tempId);
            return;
        }

        if (tagName.equals("ITEMS_LUCKY_ROUND")) {//Zalo: 0358124452//Name: EMTI 
            getItemSideBoxLuckyRound(player, player.inventory.itemsBoxCrackBall, type, tempId);
            return;
        } else if (tagName.equals("ITEMS_REWARD")) {//Zalo: 0358124452//Name: EMTI 
            return;
        }

        if (player.iDMark.getShopOpen() == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Có lỗi xảy ra vui lòng báo admin");
            return;
        }
        switch (tagName) {
            case "BUA_1H":
            case "BUA_8H":
            case "BUA_1M":
                //Zalo: 0358124452//Name: EMTI
                buyItemBua(player, tempId);
                break;
            case "SHOP_DIEM_1":
            case "SHOP_DIEM_2":
            case "SHOP_DIEM_3":
            case "SHOP_DIEM_4":
                //Zalo: 0358124452//Name: EMTI
                buyItemDiem(player, tempId);
                break;
            case "SHOP_CSMM":
                buyItemCSMM(player, tempId);
                break;
            case "HE2024":
                buyItemGauPo(player, tempId);
                break;
            case "SHOP_BA":
                //Zalo: 0358124452//Name: EMTI
                buyItemBaHatMit(player, tempId);
                break;
            default:
                //Zalo: 0358124452//Name: EMTI
                buyItem(player, tempId);
                break;
        }
        Service.gI().sendMoney(player);
        InventoryServiceNew.gI().sendItemBags(player);
    }

    private boolean subMoneyByItemShop(Player player, ItemShop is) {//Zalo: 0358124452//Name: EMTI 
        int gold = 0;
        int gem = 0;
        int ruby = 0;
        int coupon = 0;
        int event = 0;

        switch (is.typeSell) {//Zalo: 0358124452//Name: EMTI 
            case COST_GOLD:
                gold = is.cost;
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case COST_GEM:
                gem = is.cost;
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case COST_RUBY:
                ruby = is.cost;
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case COST_COUPON:
                coupon = is.cost;
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case COST_EVENT:
                event = is.cost;
                break;                              //Name: EMTI 

        }
        if (player.inventory.gold < gold) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Bạn không có đủ vàng");
            return false;
        } else if (player.inventory.gem < gem) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Bạn không có đủ ngọc");
            return false;
        } else if (player.inventory.ruby < ruby) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Bạn không có đủ hồng ngọc");
            return false;
        } else if (player.inventory.coupon < coupon) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Bạn không có đủ điểm coupon");
            return false;
        } else if (player.inventory.event < event) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Bạn không có đủ điểm event");
            return false;
        }

        player.inventory.gold -= is.temp.gold;
        player.inventory.gem -= is.temp.gem;
        player.inventory.ruby -= ruby;
        player.inventory.coupon -= coupon;
        player.inventory.event -= event;

        return true;
    }

    /**
     * Mua bùa
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    private void buyItemBua(Player player, int itemTempId) {//Zalo: 0358124452//Name: EMTI 
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        if (is == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Bùa bị lỗi vui lòng báo admin");
            return;
        }
        if (!subMoneyByItemShop(player, is)) {//Zalo: 0358124452//Name: EMTI 
            return;
        }
        InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
        InventoryServiceNew.gI().sendItemBags(player);
        opendShop(player, shop.tagName, true);
    }

    /**
     * Mua đồ
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    private void buyItemCSMM(Player player, int itemTempId) {//Zalo: 0358124452//Name: EMTI 
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        int pointExchange = 0;
        int evPoint = 0;
        if (is == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }

        for (ItemOption io : is.options) {
            if (io.optionTemplate.id == 237) {
                pointExchange = io.param;
            }
        }
        if (pointExchange > 0) {
            if (evPoint >= pointExchange) {

                player.event.subEventPoint(pointExchange);

                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);

                opendShop(player, shop.tagName, true);
            } else {
//                Service.gI().sendThongBao(player, "Bạn không có đủ điểm quy lão");
                Service.gI().sendThongBao(player, "Quay con số may mắn để nhận quà hấp dẫn tại đây");
            }
        }
    }

    private void buyItemGauPo(Player player, int itemTempId) {//Zalo: 0358124452//Name: EMTI 
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        int pointExchange = 0;
        int evPoint = 0;
        if (is == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }

        for (ItemOption io : is.options) {
            if (io.optionTemplate.id == 240) {
                pointExchange = io.param;
            }
        }
        if (pointExchange > 0) {
            if (evPoint >= pointExchange) {

                player.event.subEventPoint(pointExchange);

                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);

                opendShop(player, shop.tagName, true);
            } else {
//                Service.gI().sendThongBao(player, "Bạn không có đủ điểm quy lão");
                Service.gI().sendThongBao(player, "Tham gia đổi quà tại Gấu Po để có cơ hội nhận quà");
            }
        }
    }

    private void buyItemDiem(Player player, int itemTempId) {//Zalo: 0358124452//Name: EMTI 
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        int pointExchange = 0;
        int evPoint = player.event.getEventPoint();
        if (is == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }

        for (ItemOption io : is.options) {
            if (io.optionTemplate.id == 200) {
                pointExchange = io.param;
            }
        }
        if (pointExchange > 0) {
            if (evPoint >= pointExchange) {

                player.event.subEventPoint(pointExchange);

                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);

                opendShop(player, shop.tagName, true);
            } else {
                Service.gI().sendThongBao(player, "Bạn không có đủ điểm quy lão");
            }
        }
    }

    private void buyItemBaHatMit(Player player, int itemTempId) {//Zalo: 0358124452//Name: EMTI 
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);
        int pointExchange = 0;
        int evPoint = player.event.getEventPointBHM();
        if (is == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }

        for (ItemOption io : is.options) {
            if (io.optionTemplate.id == 201) {
                pointExchange = io.param;
            }
        }
        if (pointExchange > 0) {
            if (evPoint >= pointExchange) {

                player.event.subEventPointBHM(pointExchange);

                InventoryServiceNew.gI().addItemBag(player, ItemService.gI().createItemFromItemShop(is));
                InventoryServiceNew.gI().sendItemBags(player);
                Service.gI().sendThongBao(player, "Bạn đã đổi thành công " + ItemService.gI().createItemFromItemShop(is).template.name);

                opendShop(player, shop.tagName, true);
            } else {
                Service.gI().sendThongBao(player, "Bạn không có đủ điểm săn boss");
            }
        }
    }

    /**
     * Mua vật phẩm trong cửa hàng
     *
     * @param player người chơi
     * @param itemTempId id template vật phẩm
     */
    public void buyItem(Player player, int itemTempId) {//Zalo: 0358124452//Name: EMTI 
        Shop shop = player.iDMark.getShopOpen();
        ItemShop is = shop.getItemShop(itemTempId);

        if (is == null || is.temp == null) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
            return;
        }
        if (InventoryServiceNew.gI().getCountEmptyBag(player) == 0) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Hành trang đã đầy");
            return;
        }

        // Kiểm tra nếu là vật phẩm hủy diệt và không đủ thức ăn
        if (isHuyDietItem(is) && !hasEnoughThucAn(player)) {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(player, "Không đủ thức ăn để mua đồ!");
            return;
        }

        if (shop.typeShop == ShopServiceNew.NORMAL_SHOP) {//Zalo: 0358124452//Name: EMTI 
            if (!subMoneyByItemShop(player, is)) {//Zalo: 0358124452//Name: EMTI 
                return;
            }
        } else if (shop.tagName.equals("SHOP_DIEM_1")
                || shop.tagName.equals("SHOP_DIEM_2")
                || shop.tagName.equals("SHOP_DIEM_3")
                || shop.tagName.equals("SHOP_DIEM_4")) {//Zalo: 0358124452//Name: EMTI 
            if (!subMoneyByItemShop(player, is)) {//Zalo: 0358124452//Name: EMTI 
                return;
            }

        } else if (shop.tagName.equals("SHOP_BA")) {//Zalo: 0358124452//Name: EMTI 
            if (!subMoneyByItemShop(player, is)) {//Zalo: 0358124452//Name: EMTI 
                return;
            }

        } else if (shop.tagName.equals("HE2024")) {//Zalo: 0358124452//Name: EMTI 
            if (!subMoneyByItemShop(player, is)) {//Zalo: 0358124452//Name: EMTI 
                return;
            }

        } else if (shop.tagName.equals("SHOP_CSMM")) {//Zalo: 0358124452//Name: EMTI 
            if (!subMoneyByItemShop(player, is)) {//Zalo: 0358124452//Name: EMTI 
                return;
            }

        } else if (shop.typeShop == ShopServiceNew.SPEC_SHOP) {//Zalo: 0358124452//Name: EMTI 
            if (!this.subIemByItemShop(player, is)) {//Zalo: 0358124452//Name: EMTI 
                return;
            }
        }

        Item item = ItemService.gI().createItemFromItemShop(is);
        InventoryServiceNew.gI().addItemBag(player, item);
        InventoryServiceNew.gI().sendItemBags(player);
        Service.gI().sendThongBao(player, "Mua thành công " + is.temp.name);
    }

// Kiểm tra nếu là vật phẩm hủy diệt
    private boolean isHuyDietItem(ItemShop itemShop) {//Zalo: 0358124452//Name: EMTI 
        int itemId = itemShop.temp.id;
        return itemId >= 650 && itemId <= 662;
    }

// Kiểm tra nếu có đủ thức ăn
    private boolean hasEnoughThucAn(Player player) {//Zalo: 0358124452//Name: EMTI 
        return player.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.isThucAn() && item.quantity >= 99)
                .findFirst().isPresent();
    }

//    private void _________________Bán_vật_phẩm______________________________() {//Zalo: 0358124452//Name: EMTI 
//        //**********************************************************************
//    }
    private boolean subIemByItemShop(Player pl, ItemShop itemShop) {//Zalo: 0358124452//Name: EMTI 
        boolean isBuy = false;
        short itSpec = ItemService.gI().getItemIdByIcon((short) itemShop.iconSpec);
        int buySpec = itemShop.cost;
        Item itS = ItemService.gI().createNewItem(itSpec);
        switch (itS.template.id) {//Zalo: 0358124452//Name: EMTI 
            case 76:
            case 188:
            case 189:
            case 190:
                if (pl.inventory.gold >= buySpec) {//Zalo: 0358124452//Name: EMTI 
                    pl.inventory.gold -= buySpec;
                    isBuy = true;
                } else {//Zalo: 0358124452//Name: EMTI 
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Vàng Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
            case 861:
                if (pl.inventory.ruby >= buySpec) {//Zalo: 0358124452//Name: EMTI 
                    pl.inventory.ruby -= buySpec;
                    isBuy = true;
                } else {//Zalo: 0358124452//Name: EMTI 
                    Service.gI().sendThongBao(pl, "Bạn Không Đủ Hồng Ngọc Để Mua Vật Phẩm");
                    isBuy = false;
                }
                break;

            //Zalo: 0358124452                                //Name: EMTI 
            default:
                if (InventoryServiceNew.gI().findItemBag(pl, itSpec) == null || !InventoryServiceNew.gI().findItemBag(pl, itSpec).isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                    Service.gI().sendThongBao(pl, "Không tìm thấy " + itS.template.name);
                    isBuy = false;
                } else if (InventoryServiceNew.gI().findItemBag(pl, itSpec).quantity < buySpec) {//Zalo: 0358124452//Name: EMTI 
                    Service.gI().sendThongBao(pl, "Bạn không có đủ " + buySpec + " " + itS.template.name);
                    isBuy = false;
                } else {//Zalo: 0358124452//Name: EMTI 
                    InventoryServiceNew.gI().subQuantityItemsBag(pl, InventoryServiceNew.gI().findItemBag(pl, itSpec), buySpec);
                    isBuy = true;
                }
                break;                                //Zalo: 0358124452                                //Name: EMTI 
        }
        return isBuy;
    }

    public void showConfirmSellItem(Player pl, int where, int index) {//Zalo: 0358124452//Name: EMTI 
        //if (index == 2 || index == 1) {//Zalo: 0358124452//Name: EMTI 
        //return;
        //}

        Item item = null;
        if (where == 0) {//Zalo: 0358124452//Name: EMTI 
            item = pl.inventory.itemsBody.get(index);
        } else {//Zalo: 0358124452//Name: EMTI 
            if (pl.getSession().version <= 220) {//Zalo: 0358124452//Name: EMTI 
                index -= (pl.inventory.itemsBody.size() - 7);
            }
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 457) {//Zalo: 0358124452//Name: EMTI 
                quantity = 1;
            } else {//Zalo: 0358124452//Name: EMTI 
                cost /= 4;
            }
            if (cost == 0) {//Zalo: 0358124452//Name: EMTI 
                cost = 1;
            }
            cost *= quantity;

            String text = "Bạn có muốn bán\nx" + quantity
                    + " " + item.template.name + "\nvới giá là " + Util.numberToMoney(cost) + " vàng?";
            Message msg = new Message(7);
            try {//Zalo: 0358124452//Name: EMTI 
                msg.writer().writeByte(where);
                msg.writer().writeShort(index);
                msg.writer().writeUTF(text);
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {//Zalo: 0358124452//Name: EMTI 
                e.printStackTrace();
            }
        }
    }

    public void sellItem(Player pl, int where, int index) {//Zalo: 0358124452//Name: EMTI 
        Item item = null;
        if (where == 0) {//Zalo: 0358124452//Name: EMTI 
            item = pl.inventory.itemsBody.get(index);
        } else {//Zalo: 0358124452//Name: EMTI 
            item = pl.inventory.itemsBag.get(index);
        }
        if (item != null && item.template != null
                && item.template.id != 921
                && item.template.id != 454
                && item.template.id != 194) {//Zalo: 0358124452//Name: EMTI  // Thêm điều kiện kiểm tra id của vật phẩm khác với 921
            int quantity = item.quantity;
            int cost = item.template.gold;
            if (item.template.id == 457) {//Zalo: 0358124452//Name: EMTI 
                quantity = 1;
            } else {//Zalo: 0358124452//Name: EMTI 
                cost /= 4;
            }
            if (cost == 0) {//Zalo: 0358124452//Name: EMTI 
                cost = 1;
            }
            cost *= quantity;

            if (pl.inventory.gold + cost > Inventory.LIMIT_GOLD) {//Zalo: 0358124452//Name: EMTI 
                Service.gI().sendThongBao(pl, "Vàng sau khi bán vượt quá giới hạn");
                return;
            }
            pl.inventory.gold += cost;
            Service.gI().sendMoney(pl);
            Service.gI().sendThongBao(pl, "Đã bán " + item.template.name
                    + " thu được " + Util.numberToMoney(cost) + " vàng");

            if (where == 0) {//Zalo: 0358124452//Name: EMTI 
                InventoryServiceNew.gI().subQuantityItemsBody(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBody(pl);
                Service.gI().Send_Caitrang(pl);
            } else {//Zalo: 0358124452//Name: EMTI 
                InventoryServiceNew.gI().subQuantityItemsBag(pl, item, quantity);
                InventoryServiceNew.gI().sendItemBags(pl);
            }
        } else {//Zalo: 0358124452//Name: EMTI 
            Service.gI().sendThongBao(pl, "Không thể bán vật phẩm này được");
        }
    }

//    private void _________________Nhận_vật_phẩm_từ_rương_đặc_biệt___________() {//Zalo: 0358124452//Name: EMTI 
//        //**********************************************************************
//    }
    private void getItemSideBoxLuckyRound(Player player, List<Item> items, byte type, int index) {//Zalo: 0358124452//Name: EMTI 
        if (index == -1 && items == null || items.isEmpty() || index >= items.size()) {//Zalo: 0358124452//Name: EMTI 
            // Handle the error and notify the user
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }
        if (index >= 0) {
            Item item = items.get(index);

            switch (type) {//Zalo: 0358124452//Name: EMTI 
                case 0: // nhận
                    if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {//Zalo: 0358124452//Name: EMTI 
                            InventoryServiceNew.gI().addItemBag(player, item);
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            InventoryServiceNew.gI().sendItemBags(player);
                            items.remove(index);
                        } else {//Zalo: 0358124452//Name: EMTI 
                            Service.gI().sendThongBao(player, "Hành trang đã đầy");
                        }
                    } else {//Zalo: 0358124452//Name: EMTI 
                        Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case 1: // xóa
                    items.remove(index);
                    Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case 2: // nhận hết
                    for (int i = items.size() - 1; i >= 0; i--) {//Zalo: 0358124452//Name: EMTI 
                        item = items.get(i);
                        if (InventoryServiceNew.gI().addItemBag(player, item)) {//Zalo: 0358124452//Name: EMTI 
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            items.remove(i);
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
            }
            openShopType4(player, player.iDMark.getTagNameShop(), items);
        }
    }

    private void getItemSideMailBox(Player player, List<Item> items, byte type, int index) {//Zalo: 0358124452//Name: EMTI 
        if (index == -1 && items == null || items.isEmpty() || index >= items.size()) {//Zalo: 0358124452//Name: EMTI 
            // Handle the error and notify the user
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }
        if (index >= 0) {
            Item item = items.get(index);

            switch (type) {//Zalo: 0358124452//Name: EMTI 
                case 0: // nhận
                    if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {//Zalo: 0358124452//Name: EMTI 
                            InventoryServiceNew.gI().addItemBag(player, item);
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            InventoryServiceNew.gI().sendItemBags(player);
                            items.remove(index);
                        } else {//Zalo: 0358124452//Name: EMTI 
                            Service.gI().sendThongBao(player, "Hành trang đã đầy");
                        }
                    } else {//Zalo: 0358124452//Name: EMTI 
                        Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case 1: // xóa
                    items.remove(index);
                    Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case 2: // nhận hết
                    for (int i = items.size() - 1; i >= 0; i--) {//Zalo: 0358124452//Name: EMTI 
                        item = items.get(i);
                        if (InventoryServiceNew.gI().addItemBag(player, item)) {//Zalo: 0358124452//Name: EMTI 
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            items.remove(i);
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
            }
            openShopType4(player, player.iDMark.getTagNameShop(), items);
        }
    }

    private void getItemSideRuongPhu(Player player, List<Item> items, byte type, int index) {//Zalo: 0358124452//Name: EMTI 
        if (index == -1 && items == null || items.isEmpty() || index >= items.size()) {//Zalo: 0358124452//Name: EMTI 
            // Handle the error and notify the user
            Service.gI().sendThongBao(player, "Có lỗi xảy ra khi xử lý yêu cầu");
            return;
        }
        if (index >= 0) {
            Item item = items.get(index);

            switch (type) {//Zalo: 0358124452//Name: EMTI 
                case 0: // nhận
                    if (item.isNotNullItem()) {//Zalo: 0358124452//Name: EMTI 
                        if (InventoryServiceNew.gI().getCountEmptyBag(player) != 0) {//Zalo: 0358124452//Name: EMTI 
                            InventoryServiceNew.gI().addItemBag(player, item);
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            InventoryServiceNew.gI().sendItemBags(player);
                            items.remove(index);
                        } else {//Zalo: 0358124452//Name: EMTI 
                            Service.gI().sendThongBao(player, "Hành trang đã đầy");
                        }
                    } else {//Zalo: 0358124452//Name: EMTI 
                        Service.gI().sendThongBao(player, "Item shop bị lỗi vui lòng báo admin");
                    }
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case 1: // xóa
                    items.remove(index);
                    Service.gI().sendThongBao(player, "Xóa vật phẩm thành công");
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
                case 2: // nhận hết
                    for (int i = items.size() - 1; i >= 0; i--) {//Zalo: 0358124452//Name: EMTI 
                        item = items.get(i);
                        if (InventoryServiceNew.gI().addItemBag(player, item)) {//Zalo: 0358124452//Name: EMTI 
                            Service.gI().sendThongBao(player,
                                    "Bạn nhận được " + (item.template.id == 189
                                            ? Util.numberToMoney(item.quantity) + " vàng" : item.template.name));
                            items.remove(i);
                        }
                    }
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;                                //Zalo: 0358124452                                //Name: EMTI 
            }
            openShopType4(player, player.iDMark.getTagNameShop(), items);
        }
    }

}
/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - Girl Béo
 */
