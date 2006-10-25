package com.fcg.xmpptcp.common;

import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.codec.binary.Hex;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

public class XMPP2TCPPumpThread extends Thread  {
	private Socket socket = null;
	private Chat chat = null;

	public XMPP2TCPPumpThread(Chat chat, Socket socket) {
		this.socket = socket;
		this.chat = chat;
	}

	public void run() {
		try {
			OutputStream os = this.socket.getOutputStream();
			do {
				Message messageObj = chat.nextMessage();
				String message = messageObj.getBody();
				if (message.equals("_start")) continue;
				if (message.equals("_end")) break;
				byte[] bytes = Hex.decodeHex(message.toCharArray());
				os.write(bytes);
			} while (true);
			this.socket.shutdownOutput();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
