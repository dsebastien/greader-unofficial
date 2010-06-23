package be.lechtitseb.google.reader.api.model.authentication;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;

public class OAuthCredentials implements ICredentials,CredentialsProvider {

	
	public String getCONSUMER_KEY() {
		return CONSUMER_KEY;
	}


	public void setCONSUMER_KEY(String cONSUMERKEY) {
		CONSUMER_KEY = cONSUMERKEY;
	}


	public String getCONSUMER_SECRET() {
		return CONSUMER_SECRET;
	}


	public void setCONSUMER_SECRET(String cONSUMERSECRET) {
		CONSUMER_SECRET = cONSUMERSECRET;
	}


	public String getAUTH_KEY() {
		return AUTH_KEY;
	}


	public void setAUTH_KEY(String aUTHKEY) {
		AUTH_KEY = aUTHKEY;
	}


	public String getAUTH_SECRET() {
		return AUTH_SECRET;
	}


	public void setAUTH_SECRET(String aUTHSECRET) {
		AUTH_SECRET = aUTHSECRET;
	}


	private String CONSUMER_KEY;
	private String CONSUMER_SECRET;
	private String AUTH_KEY;
	private String AUTH_SECRET;
	
	public OAuthCredentials(String cONSUMERKEY, String cONSUMERSECRET,
			String aUTHKEY, String aUTHSECRET) {
		CONSUMER_KEY = cONSUMERKEY;
		CONSUMER_SECRET = cONSUMERSECRET;
		AUTH_KEY = aUTHKEY;
		AUTH_SECRET = aUTHSECRET;
	}


	public void clearAuthentication() {
		// TODO Auto-generated method stub
		
	}


	public boolean hasAuthentication() {
		// @TODO add a check on credentials
		return true;
	}


	public void clear() {
		// TODO Auto-generated method stub
		
	}


	public Credentials getCredentials(AuthScope authscope) {
		// TODO Auto-generated method stub
		return null;
	}


	public void setCredentials(AuthScope authscope, Credentials credentials) {
		// TODO Auto-generated method stub
		
	}
	
	


}
