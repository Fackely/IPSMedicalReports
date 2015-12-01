package util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PwdAuthentificator extends Authenticator {
	 
	private PasswordAuthentication passwordAuthentication;
 
	public PwdAuthentificator(String user, String pwd) {
		this.passwordAuthentication = new PasswordAuthentication(user, pwd);
	}
 
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return passwordAuthentication;
	}
 
}

