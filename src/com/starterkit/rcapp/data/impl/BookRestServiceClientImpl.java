package com.starterkit.rcapp.data.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.starterkit.rcapp.data.BookRestServiceClient;
import com.starterkit.rcapp.data.to.BookTo;
import com.starterkit.rcapp.helpers.BooksMapper;

/**
 * REST client for the book REST service.
 * <p>
 * Keep in mind that the methods throw {@link ProcessingException} or
 * {@link WebApplicationException} if an error occurs.
 * </p>
 *
 * @author Leszek
 */
public class BookRestServiceClientImpl implements BookRestServiceClient {

	private static final String keyRequest = "Accept";
	private static final String valueRequest = "application/json";
	private static final MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;
	private String serverURL = "http://localhost:8080/webstore";

	private WebTarget webTarget;

	/**
	 * s Constructs a new instance.
	 *
	 * @param serviceUrl
	 *            REST service URL
	 */
	public BookRestServiceClientImpl(String serviceUrl) {
		Client client = ClientBuilder.newClient();
		webTarget = client.target(serviceUrl);
	}

	@Override
	public List<BookTo> findBooksByTitleAndAuthor(String author, String title) {
		String json = webTarget.path("/books/searchBooks").queryParam("author", author).queryParam("title", title)
				.request(MEDIA_TYPE).get().readEntity(String.class);

		try {
			return BooksMapper.mapJson2BookVoList(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// @Override
	// public BookTo saveBook(BookTo book) {
	// Entity<BookTo> entity = Entity.entity(book, MEDIA_TYPE);
	// return webTarget.path("/books/addBook").request(MEDIA_TYPE).post(entity,
	// BookTo.class);
	// }

	@Override
	public BookTo saveBook(BookTo bookTo) throws MalformedURLException, IOException {

		HttpURLConnection con = createHttpConnection(new URL(serverURL + "/books/addBook"), "POST");
		BufferedReader bufferedReader = communicateByJson(bookTo, con);

		int httpResult = con.getResponseCode();

		if (httpResult == HttpURLConnection.HTTP_OK) {
			return BooksMapper.mapJson2BookVo(bufferedReader.readLine());
		} else {
			return null;
		}
	}

	@Override
	public void deleteBook(Long id) {
		webTarget.path("/books/deleteBook/").path(id.toString()).request(MEDIA_TYPE).delete();

	}

	private HttpURLConnection createHttpConnection(URL url, String requestMethod)
			throws MalformedURLException, IOException, ProtocolException {

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", valueRequest);
		con.setRequestProperty(keyRequest, valueRequest);
		con.setRequestMethod(requestMethod);
		return con;
	}

	private BufferedReader communicateByJson(BookTo bookTo, HttpURLConnection con)
			throws IOException, UnsupportedEncodingException, MalformedURLException {
		OutputStream os = con.getOutputStream();

		os.write(BooksMapper.toJSON(bookTo).toString().getBytes("UTF-8"));
		InputStream is = new BufferedInputStream(con.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

		os.close();

		return bufferedReader;
	}

	@Override
	public List<BookTo> findBooksByTitle(String titlePrefix) {
		// TODO Auto-generated method stub
		return null;
	}

}
