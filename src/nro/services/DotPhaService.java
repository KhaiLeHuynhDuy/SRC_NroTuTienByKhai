package nro.services;

import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.utils.Logger;

public class DotPhaService {

    private static DotPhaService instance;

    private DotPhaService() {
    }

    public static DotPhaService gI() {
        if (instance == null) {
            instance = new DotPhaService();
        }
        return instance;
    }

    public void thucHienDotPha(Player player, int select) {
        try {
            int maxDameg = 18_000_000;
            int maxHpMp = maxDameg * 3;

            // Kiểm tra giới hạn Trúc Cơ Cảnh
            boolean alreadyBeyond = player.nPoint.dameg >= maxDameg
                    || player.nPoint.hpg >= maxHpMp
                    || player.nPoint.mpg >= maxHpMp;

            if (!alreadyBeyond) {
                Service.gI().sendThongBaoOK(player, "Bạn chưa đạt giới hạn chỉ số của Trúc Cơ Cảnh để có thể đột phá!");
                return;
            }
            if (player.capTT != 3) {
                Service.gI().sendThongBaoOK(player, "Lại độ thiên kiếp rồi tới đây đột phá!");
                return;
            }
            if (player.dotpha != 0) {
                Service.gI().sendThongBaoOK(player, "Bạn đã đột phá rồi, không thể thực hiện lại!");
                return;
            }

            // Cho phép đột phá
            switch (select) {
                case 0 -> {
                    if (player.inventory.ruby < 1_000_000) {
                        Service.gI().sendThongBao(player, "Bạn không đủ Hồng ngọc để đột phá thành Pháp Tu");
                        return;
                    } else {
                        player.dotpha = 1; // Pháp Tu
                        player.nPoint.dameg = player.nPoint.dameg + 6_000_000;
                        player.nPoint.hpg = player.nPoint.hpg + 3_000_000;
                        player.nPoint.mpg = player.nPoint.mpg + 3_000_000;
                        player.nPoint.tlNeDon += 15;

                        player.inventory.ruby -= 1_000_000;
                        Service.gI().sendMoney(player);

                        Service.gI().sendThongBaoOK(player, "Bạn đã đột phá thành Pháp Tu!");
                    }
                }
                case 1 -> {
                    if (player.inventory.ruby < 1_000_000) {
                        Service.gI().sendThongBao(player, "Bạn không đủ Hồng ngọc để đột phá thành Pháp Tu");
                        return;
                    } else {
                        player.dotpha = 2; // Thể Tu
                        player.nPoint.dameg = player.nPoint.dameg + 1_000_000;
                        player.nPoint.hpg = player.nPoint.hpg + 18_000_000;
                        player.nPoint.mpg = player.nPoint.mpg + 18_000_000;
                        player.nPoint.tlPST += 25;
                        Service.gI().sendThongBaoOK(player, "Bạn đã đột phá thành Thể Tu!");
                    }
                }
                case 2 -> {
                    if (player.getSession().vnd < 250_000) {
                        Service.gI().sendThongBao(player, "Bạn không đủ COIN để đột phá thành Hồn Tu (cần 250k COIN)");
                        return;
                    } else {
                        player.dotpha = 3; // Hồn Tu
                        player.nPoint.dameg = player.nPoint.dameg + 10_000_000;
                        player.nPoint.defg = player.nPoint.defg + 10_000_000;
                        player.nPoint.hpg = player.nPoint.hpg + 10_000_000;
                        player.nPoint.mpg = player.nPoint.mpg + 10_000_000;

                        Item newItem = ItemService.gI().createNewItem((short) 1708);
                        newItem.itemOptions.add(new Item.ItemOption(30, 1));
                        newItem.itemOptions.add(new Item.ItemOption(202, 0));

                        PlayerDAO.subvnd(player, 250_000);
                        InventoryServiceNew.gI().addItemBag(player, newItem);
                        Service.gI().sendMoney(player);
                        InventoryServiceNew.gI().sendItemBags(player);
                        Service.gI().sendThongBaoOK(player, "Bạn đã đột phá thành Hồn Tu!");
                    }
                }
                default ->
                    Service.gI().sendThongBaoOK(player, "Lựa chọn không hợp lệ.");
            }
        } catch (Exception e) {
            Logger.error("Lỗi khi thực hiện đột phá: " + e.getMessage());
            Service.gI().sendThongBaoOK(player, "Đã xảy ra lỗi khi đột phá, vui lòng thử lại.");
        }
    }

    public String getRealNameDotPha(int level) {
        return switch (level) {
            case 0 ->
                "Chưa Đột Phá";
            case 1 ->
                "Pháp Tu";
            case 2 ->
                "Thể Tu";
            case 3 ->
                "Hồn Tu";
            default ->
                "Không xác định";
        };
    }
}
