package cz.zk.springmvcdemo.service;

import cz.zk.springmvcdemo.globalData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;

import java.util.Map;

@Slf4j
@MessageEndpoint
public class UdpInboundMessageHandler {
	
	@Autowired
	globalData gd;

	@ServiceActivator(inputChannel = "udpPort7777")
	public void handleMessage(Message message, @Headers Map<String, Object> headerMap) {
		byte[] msg = (byte[]) message.getPayload();
		String logrecord = "ABT Msg: ";
		for(int i=0; i< msg.length; i++)
			logrecord += String.format("%02X ", msg[i]);
		log.debug(logrecord);

		if(msg[7] == 0x66) {   // HOME
			log.debug("HOME button " + msg[8]);
			gd.BtnAbtHome = (msg[8] == 1);
		}
		else if(msg[7] == 0x1a) {   // MENU
			log.debug("MENU button " + msg[8]);
			gd.BtnAbtMenu = (msg[8] == 1);
		}
		else if(msg[7] == 0x38) {   // Power
			log.debug("POWER button " + msg[8]);
			gd.BnAbtPower = (msg[8] == 1);
		}
		else if(msg[7] == 0x00) {
			String smsg = getCoords(msg);
			log.debug(smsg);
			gd.BtnAbtTouch = smsg;
		}
	}

	@ServiceActivator(inputChannel = "udpPort8888")
	public void handleMessage8888(Message message, @Headers Map<String, Object> headerMap) {
		byte[] msg = (byte[]) message.getPayload();
		String logrecord = "MFL Msg: ";
		for(int i=0; i< msg.length; i++)
			logrecord += String.format("%02X ", msg[i]);
		log.info(logrecord);

	}
	/**
     *
     * @param msg
     * @return
     */
	private String getCoords(byte[] msg) {
		String spom = "";
		int x1 = 64 * (msg[9] & 0x0f);  // dolni 4 bity = horni bity X
		int x2 = (msg[10] >> 2) & 0x3f;
		int x = x1 + x2;
		int y1 = 256 * (msg[10] & 0x03);
		int y2 = msg[11] & 0xff;
		int y = y1 + y2;
		if(msg[8] == 0x11) spom = "Touch PRESS: ";
		else spom = "Touch RELEASE: ";
		spom += String.format("X=%d  Y=%d", 2 * x, y);
		return spom;
	}

}
