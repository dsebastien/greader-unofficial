package be.lechtitseb.google.reader.api.model.authentication;


/**
 * Google specific credentials
 */
public class GoogleCredentials extends BasicCredentials implements ICredentials {
	private String sid = "";
	private String lSid = ""; // Unused for now it seems

	public GoogleCredentials(String username, String password) {
		super(username, password);
	}

	/**
	 * Clear authentication information (sid, lsid)
	 */
	public void clearAuthentication() {
		setSid(null);
		setLSid(null);
	}
	
	@Override
	public void clearCredentials() {
		super.clearCredentials();
	}

	public String getLSid() {
		return lSid;
	}

	public String getSid() {
		return sid;
	}

	/**
	 * Whether we have SID + LSID or not
	 */
	public boolean hasAuthentication() {
		return (!sid.equals("") && !lSid.equals(""));
	}

	public void setLSid(String lSid) {
		if (lSid == null) {
			this.lSid = "";
		} else {
			this.lSid = lSid;
		}
	}

	public void setSid(String sid) {
		if (sid == null) {
			this.sid = "";
		} else {
			this.sid = sid;
		}
	}
}
