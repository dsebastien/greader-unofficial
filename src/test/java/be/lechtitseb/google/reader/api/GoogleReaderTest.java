package be.lechtitseb.google.reader.api;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import be.lechtitseb.google.reader.api.core.GoogleReader;
import be.lechtitseb.google.reader.api.model.authentication.OAuthCredentials;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;
import be.lechtitseb.google.reader.api.model.item.Item;


public class GoogleReaderTest {


		private final String SERVICE = "reader";
		private final String SOURCE = "greader-unofficial";

		public GoogleReaderTest() throws FileNotFoundException, IOException {

			Properties auth = new Properties();
			auth.load( new FileInputStream("auth.properties"));

			GoogleReader gr = new GoogleReader(new OAuthCredentials(
					"anonymous",					//consumer key
	                "anonymous",					//consumer secret
	                auth.getProperty("auth.key"),		//auth key
	                auth.getProperty("auth.secret")		//auth secret
				)
			);

			try {
				List<Item> myItems = gr.search("Finance", 10);
				
				for (Item item : myItems){
					System.out.println(item.getId());
					System.out.println(item.getNumericId());
					//try and get the item using the return id
					
					Item item1 = gr.getItem(item.getId());
					System.out.println("using id "+item1.getTitle());
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
		 * @throws IOException 
		 * @throws FileNotFoundException 
		 */
		public static void main(String[] args) throws FileNotFoundException, IOException {
			// TODO Auto-generated method stub

			GoogleReaderTest grc = new GoogleReaderTest();
			
			
		}

	}
