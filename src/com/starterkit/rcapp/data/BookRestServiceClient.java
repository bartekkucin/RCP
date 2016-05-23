package com.starterkit.rcapp.data;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import com.starterkit.rcapp.data.to.BookTo;

/**
 * REST client for the book REST service.
 *
 * @author Leszek
 */
public interface BookRestServiceClient {

	//String serverURL = "http://localhost:8080/webstore";
	//BookRestServiceClient INSTANCE = new BookRestServiceClientImpl(serverURL);
	
	/**
	 * Gets books with given title prefix.
	 *
	 * @param titlePrefix
	 *            title prefix
	 * @return list of books
	 */
	List<BookTo> findBooksByTitle(String titlePrefix);

	/**
	 * Saves given book.
	 *
	 * @param book
	 *            book
	 * @return saved book
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	BookTo saveBook(BookTo book) throws  IOException;
	
	void deleteBook(Long bookID);

	List<BookTo> findBooksByTitleAndAuthor(String author, String title);
}
