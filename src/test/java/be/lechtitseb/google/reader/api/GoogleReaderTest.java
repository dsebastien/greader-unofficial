package be.lechtitseb.google.reader.api;


import java.util.List;

import be.lechtitseb.google.reader.api.core.GoogleReader;
import be.lechtitseb.google.reader.api.model.authentication.OAuthCredentials;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;
import be.lechtitseb.google.reader.api.model.item.Item;


public class GoogleReaderTest {


		// set some vars to use for prototyping
		private final String EDIT_URL = "https://www.google.com/accounts/ClientLogin";

		private final String SERVICE = "reader";
		private final String SOURCE = "greader-unofficial";

		public GoogleReaderTest() {

	/*		//getAuth();
			GoogleReader gr = new GoogleReader(new OAuthCredentials(
					"matthiaskaeppler.de",					//consumer key
	                "etpfOSfQ4e9xnfgOJETy4D56",				//consumer secret
					"1/FDrHHMUxMPLpzbhOIUbFCj_LlITMWKO0Imj-jjjzUU0",	//auth key
					"Vx10gFW9PC+GqYUGCBBOThFR"							//auth secret
				)
			);*/

			GoogleReader gr = new GoogleReader(new OAuthCredentials(
					"anonymous",					//consumer key
	                "anonymous",					//consumer secret
					"YOUR AUTH KEY",		//auth key
					"YOUR AUTH SECRET"							//auth secret
				)
			);
	/*		try {
				gr.login();
			} catch (AuthenticationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			try {
				List<Item> myItems = gr.search("Finance", 10);
				
				for (Item item : myItems){
					System.out.println(item.getId());
					System.out.println(item.getNumericId());
					//try and get the item using the return id
					
					//Item item1 = gr.getItem(item.getId());
					//System.out.println("using id "+item1.getTitle());
				}
			} catch (GoogleReaderException e) {
				System.out.println("there was an error");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
	/*		try {
				List<Label> labels = gr.getLabels();
				for( Label label : labels){
					System.out.println(label.getName());
				}
			} catch (GoogleReaderException e) {
				System.out.println("there was an error");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}*/
			
			
			
		}


		
		
		/**
		 * @param args
		 */
		public static void main(String[] args) {
			// TODO Auto-generated method stub

			GoogleReaderTest grc = new GoogleReaderTest();
			
			
		}

	}
