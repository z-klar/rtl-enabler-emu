package cz.zk.springmvcdemo.tool;

import cz.zk.springmvcdemo.model.BtnMessage;

import java.util.ArrayList;

public class tools {

    private ArrayList<BtnMessage> canMessages = new ArrayList<>();

    private static BtnMessage [] btnMessages = {
        new BtnMessage("END_CMD", 0),
        new BtnMessage("CONTEXT_MENU", 01),
        new BtnMessage("MENU_UP_NEXT_SCREEN", 02),
        new BtnMessage("MENU_DOWN_NEXT_SCREEN", 3),
        new BtnMessage("UP", 4),
        new BtnMessage("DOWN", 5),
        new BtnMessage("UP_THUMBWHEEL", 6),
        new BtnMessage("OK_THUMBWHEEL_BUTTON", 7),
        new BtnMessage("CANCEL_ESCAPE", 8),
        new BtnMessage("MAIN_MENU", 9),
        new BtnMessage("SIDE_MENU_LEFT", 0x0A),
        new BtnMessage("SIDE_MENU_RIGHT", 0x0B),
        new BtnMessage("FAS_MENU", 0x0C),
        new BtnMessage("LEFT_RIGHT_THUMBWHEEL", 0x0D),
        new BtnMessage("VOLUME_UP", 0x10),
        new BtnMessage("VOLUME_DOWN", 0x11),
        new BtnMessage("VOLUME_UP_THUMBWHEEL", 0x12),
        new BtnMessage("VOLUME_THUMBWHEEL_BUTTON", 0x13),
        new BtnMessage("AUDIO_SOURCE", 0x14),
        new BtnMessage("ARROW_A_UP_RIGHT", 0x15),
        new BtnMessage("ARROW_A_DOWN_LEFT", 0x16),
        new BtnMessage("ARROW_B_UP_RIGHT", 0x17),
        new BtnMessage("ARROW_B_DOWN_LEFT", 0x18),
        new BtnMessage("PTT_PUSHTOTALK", 0x19),
        new BtnMessage("PTT_CANCEL", 0x1A),
        new BtnMessage("ROUT_INFO", 0x1B),
        new BtnMessage("HOOK", 0x1C),
        new BtnMessage("HANG_UP", 0x1D),
        new BtnMessage("OFF_HOOK", 0x1E),
        new BtnMessage("LIGHT_ON_OFF", 0x1F),
        new BtnMessage("MUTE", 0x20),
        new BtnMessage("JOKER1", 0x21)
    };

    public static String getMflButtonDescrByCode(int code) {
        for(BtnMessage bm : btnMessages) {
            if(bm.getCode() == code) {
                return bm.getName();
            }
        }
        return "Not known";
    }


}
