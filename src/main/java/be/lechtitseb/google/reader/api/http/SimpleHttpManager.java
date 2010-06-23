package be.lechtitseb.google.reader.api.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import be.lechtitseb.google.reader.api.core.Constants;
import be.lechtitseb.google.reader.api.model.authentication.ICredentials;
import be.lechtitseb.google.reader.api.model.authentication.OAuthCredentials;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;

/**
 * Helper class that executes HTTP GET/POST requests based on Apache Commons
 * HttpClient
 */
public class SimpleHttpManager implements HttpManager {
	private static final Logger LOG =
			Logger.getLogger(SimpleHttpManager.class.getName());
	private List<Cookie> cookies = null;
	//private HttpMethodRetryHandler retryHandler = null;
	private HttpClient httpClient = null;
	//private HttpConnectionManager manager = null;
	private ICredentials credentials = null;
	private OAuthConsumer consumer;
	
	
	public SimpleHttpManager() {
		//manager = new MultiThreadedHttpConnectionManager();
		cookies = new ArrayList<Cookie>();
		httpClient = getHttpClient();
		//retryHandler = getRetryHandler();
	}
	
	/**
	 * if we provide credentials then sign the request with the credentials
	 * @param credentials
	 */
	public SimpleHttpManager(OAuthCredentials credentials) {
		this();
/*		consumer = new CommonsHttpOAuthConsumer(credentials.getCONSUMER_KEY(), 
				credentials.getCONSUMER_SECRET());
*/
		consumer = new CommonsHttpOAuthConsumer("anonymous","anonymous");
		// 4/3qW_V9-Z3-3RVEmzxrHf67bpsaHj
		
		System.out.println(credentials.getAUTH_KEY());
		System.out.println(credentials.getAUTH_SECRET());
		consumer.setTokenWithSecret(credentials.getAUTH_KEY(), credentials.getAUTH_SECRET());
		

		
	}

	public void addCookie(Cookie cookie) {
		if (cookie != null) {
			cookies.add(cookie);
		} else {
			LOG.debug("Some psycho tried to add a null cookie to the list");
		}
	}

	public void clearCookies() {
		cookies.clear();
	}

	private byte[] download(HttpUriRequest request, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {
		//@TODO add the query string
		
		
		BasicHttpParams httpParams = new BasicHttpParams();
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		
		for(NameValuePair pair : toNameValuePairArray(parameters)){
			httpParams.setParameter(pair.getName(),pair.getValue());
		}
		request.setParams(httpParams);

		
		
		
		//request.setParams(toNameValuePairArray(parameters));
/*		request.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				retryHandler);
		*/
//		httpClient.setHttpRequestRetryHandler(myRetryHandler);

		//@TODO enable
/*		if (useCookies) {
			HttpState initialState = new HttpState();
			for (Cookie c : cookies) {
				initialState.addCookie(c);
			}
			httpClient.setState(initialState);
		}*/
        // sign the request (consumer is a Signpost DefaultOAuthConsumer)
        try {
			consumer.sign(request);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				String error =
						String.format("Expected 200 OK. Received %d %s",
								response.getStatusLine().getStatusCode(),response.
								getStatusLine().getReasonPhrase());
				throw new GoogleReaderException(error);
			}

	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            

	        	byte[] buffer = EntityUtils.toByteArray(entity);
		        entity.consumeContent();
		        return buffer;
	        }
			
		} catch (Throwable error) {
			throw new GoogleReaderException(error);
		} finally {
			//@TODO this was originally calling release connection
			//need to check low level resources are release
			//request.
			
		}
		return null;
	}

	public byte[] download(String url) throws GoogleReaderException {
		return download(new HttpGet(url), null, false);
	}

	public byte[] download(String url, List<Parameter> parameters)
			throws GoogleReaderException {
		return download(new HttpGet(url), parameters, false);
	}

	public byte[] download(String url, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {
		return download(new HttpGet(url), parameters, useCookies);
	}

	private String execute(HttpUriRequest request, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {

		
		System.out.println(request.getMethod());
		
		URL myuri;
		URI uri;
		
		try {
			try {
				myuri = new URL(request.getURI().toString());
				uri = URIUtils.createURI(myuri.getProtocol(), myuri.getHost(), -1, 
						myuri.getPath(), toQueryString(parameters),"");
				//@TODO why is this doing that... 
				if(request.getMethod().equalsIgnoreCase("GET"))
				{
					if((uri.toString().charAt(uri.toString().length()-1) )=='#'){
						request = new HttpGet(uri.toString().substring(0,uri.toString().length()-1));
					}else{
						request = new HttpGet(uri);					
					}
				}else{ 
					if(request.getMethod().equalsIgnoreCase("POST")){

						if((uri.toString().charAt(uri.toString().length()-1) )=='#'){
							request = new HttpPost(uri.toString().substring(0,uri.toString().length()-1));
						}else{
							request = new HttpPost(uri);					
						}	
					}
				}
			} catch (MalformedURLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		System.out.println(request.getURI());
		
		/*if (useCookies) {
			HttpState initialState = new HttpState();
			for (Cookie c : cookies) {
				initialState.addCookie(c);
			}
			httpClient.setState(initialState);
		}
		*/        // sign the request (consumer is a Signpost DefaultOAuthConsumer)
        try {
			consumer.sign(request);
		} catch (OAuthMessageSignerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				String error =
						String.format("Expected 200 OK. Received %d %s",
								response.getStatusLine().getStatusCode(),response.
								getStatusLine().getReasonPhrase());
				throw new GoogleReaderException(error);
			}
			
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {

	        	String buffer = EntityUtils.toString(entity);
		        entity.consumeContent();
		        return buffer;
	        }else{
				throw new GoogleReaderException(
				"Expected response content, got null");
	        }

		}catch(Exception error){
			throw new GoogleReaderException(error);
		} finally {
			
		}
	}

	public String get(String url) throws GoogleReaderException {
		return get(url, null);
	}

	public String get(String url, List<Parameter> parameters)
			throws GoogleReaderException {
		return get(url, parameters, false);
	}

	public String get(String url, List<Parameter> parameters, boolean useCookies)
			throws GoogleReaderException {
		return execute(new HttpGet(url), parameters, useCookies);
	}

	private byte[] getBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
		return out.toByteArray();
	}

	private String getContent(InputStream in) throws IOException {
		InputStreamReader isreader =
				new InputStreamReader(in, Constants.HTTP_CHARSET_VALUE);
		StringBuilder sb = new StringBuilder();
		int ch;
		while ((ch = isreader.read()) > -1) {
			sb.append((char) ch);
		}
		in.close();
		return sb.toString();
	}

	private HttpClient getHttpClient() {
		//HttpClient client = new HttpClient(manager);
		//HttpClient client = new DefaultHttpClient(new SingleClientConnManager());
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(Constants.HTTP_CHARSET_PARAMETER,
				Constants.HTTP_CHARSET_VALUE);
		client.getParams().setParameter(Constants.USER_AGENT_PARAMETER,
				Constants.USER_AGENT_VALUE);
		//HttpState initialState = new HttpState();
		for (Cookie c : cookies) {
			//initialState.addCookie(c);
		}
		//client.setState(initialState);
		return client;
	}

	HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
	        if (executionCount >= 5) {
	            // Do not retry if over max retry count
	            return false;
	        }
	        if (exception instanceof NoHttpResponseException) {
	            // Retry if the server dropped connection on us
	            return true;
	        }
	        if (exception instanceof SSLHandshakeException) {
	            // Do not retry on SSL handshake exception
	            return false;
	        }
	        HttpRequest request = (HttpRequest) context.getAttribute(
	                ExecutionContext.HTTP_REQUEST);
	        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest); 
	        if (idempotent) {
	            // Retry if the request is considered idempotent 
	            return true;
	        }
	        return false;
	    }




	};

	

	public String post(String url) throws GoogleReaderException {
		return post(url, null);
	}

	public String post(String url, List<Parameter> parameters)
			throws GoogleReaderException {
		return post(url, parameters, false);
	}

	public String post(String url, List<Parameter> parameters,
			boolean useCookies) throws GoogleReaderException {
		return execute(new HttpPost(url), parameters, useCookies);
	}

	private BasicNameValuePair[] toNameValuePairArray(List<Parameter> in) {
		if (in == null) {
			return new BasicNameValuePair[] {};
		}
		List<BasicNameValuePair> out = new ArrayList<BasicNameValuePair>();
		for (Parameter parameter : in) {
			if (parameter.hasName() && parameter.hasValue()) {
				out.add(new BasicNameValuePair(parameter.getName(), parameter
						.getValue().toString()));
			}
		}
		return (BasicNameValuePair[]) out.toArray(new BasicNameValuePair[out.size()]);
	}

	private String toQueryString(List<Parameter> in) {
		if (in == null) {
			return "";
		}
		//List<BasicNameValuePair> out = new ArrayList<BasicNameValuePair>();
		StringBuffer strbuf = new StringBuffer("");
		for (Parameter parameter : in) {
			if (parameter.hasName() && parameter.hasValue()) {
				if(!(strbuf.length()==0)){
					strbuf.append("&");
				}
				strbuf.append(parameter.getName());
				strbuf.append("=");
				strbuf.append(parameter.getValue());

				 
				//strbuf.
			}
		}
		return strbuf.toString();
	}
}