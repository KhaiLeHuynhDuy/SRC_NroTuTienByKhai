package nro.services;

import nro.models.item.Item;
import nro.models.player.Player;
import java.util.Random;
import nro.jdbc.daos.PlayerDAO;
import nro.utils.Logger;

public class BinhCanhService {

    private static BinhCanhService instance;
    private final Random rand;

    // 0:Sơ Kỳ → 1, 1:Trung Kỳ → 2, 2:Hậu Kỳ → 3(Đỉnh Phong)
    private static final int MAX_BinhCanh = 3;

    private static final int ITEM_ID_REQUIRED = 457;
    private static final int[] ITEM_REQUIRE = {1, 2, 3};
    private static final int[] SUCCESS_RATE = {90, 70, 50};

    // Mặc định hệ số power requirement khi capTT != 1,2
    private static final long[] DEFAULT_POWER_REQUIRE = {
        22_250_000_000_000L,
        22_450_000_000_000L,
        22_750_000_000_000L
    };

    // Yêu cầu power theo capTT: [0]=Luyện Khí (capTT=1), [1]=Trúc Cơ (capTT=2)
    private static final long[][] REALM_POWER_REQUIRE = {
        {250_000_000_000L, 450_000_000_000L, 750_000_000_000L}, // capTT=1
        {1_200_000_000_000L, 1_700_000_000_000L, 2_100_000_000_000L},
        {5_200_000_000_000L, 9_700_000_000_000L, 11_110_000_000_000L}// capTT=3
    };

    // Thêm bonus nếu Thiên Đạo Trúc Cơ
    private static final long TRUC_CODAN_BONUS = 500_000_000_000L;

    public static BinhCanhService gI() {
        if (instance == null) {
            instance = new BinhCanhService();
        }
        return instance;
    }

    private BinhCanhService() {
        rand = new Random();
    }

    public void process(Player player, int times) {
        try {
            // VALIDATION TỔNG
            if (player.capTT <= 0) {
                Service.gI().sendThongBao(player, "Phàm nhân mà độ cái gì thiên kiếp ?");
                return;
            }

            if (player.capCS >= MAX_BinhCanh) {
                Service.gI().sendThongBao(player, "Bạn đã đạt Đỉnh Phong, hãy độ kiếp lên cảnh giới mới");
                return;
            }

            int initialLevel = player.capCS;
            int totalItemsUsed = 0;
            int successCount = 0;
            boolean reachedMax = false;

            for (int i = 0; i < times; i++) {
                // VALIDATION TRƯỚC MỖI LẦN
                if (player.capCS >= MAX_BinhCanh) {
                    reachedMax = true;
                    break;
                }

                int currLevel = player.capCS;
                int targetLevel = currLevel + 1;

                // 1. KIỂM TRA VẬT PHẨM
                int needItem = ITEM_REQUIRE[currLevel];
                Item goldBar = findItem(player);
                if (goldBar == null || goldBar.quantity < needItem) {
                    Service.gI().sendThongBao(player,
                            String.format("Cần %,d Thỏi vàng cho đột phá %s → %s",
                                    needItem,
                                    getRealNameBinhCanh(currLevel),
                                    getRealNameBinhCanh(targetLevel)
                            )
                    );
                    break;
                }

                // 2. KIỂM TRA TU VI
                long needPower = calculateRequiredPower(player.capTT, currLevel, player.isUseTrucCoDan);
                if (player.nPoint.power < needPower) {
                    Service.gI().sendThongBao(player,
                            String.format("Tu vi cần: %,d\nHiện tại: %,d",
                                    needPower,
                                    player.nPoint.power)
                    );
                    break;
                }

                // 3. TRỪ VẬT PHẨM
                InventoryServiceNew.gI().subQuantityItemsBag(player, goldBar, needItem);
                totalItemsUsed += needItem;

                // 4. TÍNH TOÁN TỈ LỆ
                boolean success = rand.nextInt(100) < SUCCESS_RATE[currLevel];
                if (success) {
                    player.capCS = (byte) targetLevel;
                    int newplayerpower = 1_500_000;
                    long oldplayerpower = player.nPoint.power;
                    long subplayerpower = newplayerpower - oldplayerpower;
                    PlayerService.gI().sendTNSM(player, (byte) 0, subplayerpower);
                    player.nPoint.power = newplayerpower;
                    successCount++;
                    PlayerDAO.updatePlayer(player);
                    InventoryServiceNew.gI().sendItemBags(player);
                    break;
                }

                // 5. KIỂM TRA LẠI SAU KHI NÂNG
                if (player.capCS >= MAX_BinhCanh) {
                    reachedMax = true;
                    break;
                }
            }

            // THÔNG BÁO KẾT QUẢ
            String resultMsg = String.format(
                    "Kết quả đột phá %d lần:\n- Thành công: %d/%d\n- Dùng: %,d Thỏi vàng",
                    times, successCount, times, totalItemsUsed
            );

            if (reachedMax) {
                resultMsg += "\n|5|ĐẠT ĐỈNH PHONG|";
            }

            Service.gI().sendThongBao(player, resultMsg);
            InventoryServiceNew.gI().sendItemBags(player);

        } catch (Exception e) {
            Logger.logException(BinhCanhService.class, e, "Lỗi đột phá bình cảnh");
            Service.gI().sendThongBao(player, "Lỗi hệ thống, vui lòng báo GM");
        }
    }

    /**
     * Tính power yêu cầu theo cảnh giới bên độ kiếp (capTT)
     */
    private long calculateRequiredPower(int capTT, int level, boolean isTrucCoDan) {
        long base;
        if (capTT == 1) {
            base = REALM_POWER_REQUIRE[0][level];
        } else if (capTT == 2) {
            base = REALM_POWER_REQUIRE[1][level];
        } else {
            base = DEFAULT_POWER_REQUIRE[level];
        }
        if (capTT == 2 && isTrucCoDan) {
            base += TRUC_CODAN_BONUS;
        }
        return base;
    }

    private Item findItem(Player player) {
        for (Item it : player.inventory.itemsBag) {
            if (it != null && it.template.id == ITEM_ID_REQUIRED) {
                return it;
            }
        }
        return null;
    }

    public String getRealNameBinhCanh(int level) {
        switch (level) {
            case 0:
                return "Sơ Kỳ";
            case 1:
                return "Trung Kỳ";
            case 2:
                return "Hậu Kỳ";
            case 3:
                return "Đỉnh Phong";
            default:
                return "Không xác định";
        }
    }
}
