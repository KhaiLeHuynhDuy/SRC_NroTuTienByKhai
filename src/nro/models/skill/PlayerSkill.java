package nro.models.skill;

import java.util.ArrayList;
import java.util.List;
import nro.models.player.Player;
import nro.services.Service;
import nro.network.io.Message;

public class PlayerSkill {

    private Player player;
    public List<Skill> skills;
    public Skill skillSelect;

    public static final int TIME_MUTIL_CHUONG = 60000;

    public PlayerSkill(Player player) {
        this.player = player;
        skills = new ArrayList<>();
    }

    public Skill getSkillbyId(int id) {
        for (Skill skill : skills) {
            if (skill.template.id == id) {
                return skill;
            }
        }
        return null;
    }

    public byte[] skillShortCut = new byte[10];

    public void sendSkillShortCut() {
        Message msg;
        try {
            msg = Service.gI().messageSubCommand((byte) 61);
            msg.writer().writeUTF("KSkill");
            msg.writer().writeInt(skillShortCut.length);
            msg.writer().write(skillShortCut);
            player.sendMessage(msg);
            msg.cleanup();
            msg = Service.gI().messageSubCommand((byte) 61);
            msg.writer().writeUTF("OSkill");
            msg.writer().writeInt(skillShortCut.length);
            msg.writer().write(skillShortCut);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean prepareQCKK;
    public boolean prepareTuSat;
    public boolean prepareLaze;

    public long lastTimePrepareQCKK;
    public long lastTimePrepareTuSat;
    public long lastTimePrepareLaze;

    public byte getIndexSkillSelect() {
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.KAIOKEN:
            case Skill.LIEN_HOAN:
                return 1;
            case Skill.KAMEJOKO:
            case Skill.ANTOMIC:
            case Skill.MASENKO:
                return 2;
            default:
                return 3;
        }
    }

    public byte getSizeSkill() {
        byte size = 0;
        for (Skill skill : skills) {
            if (skill.skillId != -1) {
                size++;
            }
        }
        return size;
    }
//     public int getCoolDown() {
//        if (player.isPet) {
//            switch (skillSelect.template.id) {
//                case Skill.DRAGON:
//                case Skill.DEMON:
//                case Skill.GALICK:
//                case Skill.KAIOKEN:
//                    return 500;
//                case Skill.KAMEJOKO:
//                case Skill.ANTOMIC:
//                case Skill.MASENKO:
//                    return 700;
//            }
//        }
//        if (player.isBoss) {
//            return 0;
//        }
//        return skillSelect.coolDown;
//    }

    public void dispose() {
        if (this.skillSelect != null) {
            this.skillSelect.dispose();
        }
        if (this.skills != null) {
            for (Skill skill : this.skills) {
                skill.dispose();
            }
            this.skills.clear();
        }
        this.player = null;
        this.skillSelect = null;
        this.skills = null;
    }
}
