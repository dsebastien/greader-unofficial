package be.lechtitseb.google.reader.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import be.lechtitseb.google.reader.api.core.GoogleReader;
import be.lechtitseb.google.reader.api.model.exception.AuthenticationException;

public class GoogleReaderTest {

	@Test
	public void testAuth() throws FileNotFoundException, IOException, AuthenticationException {
		Properties auth = new Properties();
		auth.load( new FileInputStream("auth.properties"));

		GoogleReader gr = new GoogleReader(auth.getProperty("auth.key"),auth.getProperty("auth.secret"));
		gr.login();
		
	}
}
