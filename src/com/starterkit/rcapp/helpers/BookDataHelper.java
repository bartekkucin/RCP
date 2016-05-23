package com.starterkit.rcapp.helpers;

import java.util.ArrayList;

import org.eclipse.core.databinding.observable.list.WritableList;

import com.starterkit.rcapp.data.to.BookTo;

public class BookDataHelper {
	
	private static BookDataHelper instance = null;
	   protected BookDataHelper() {

	   }
	   public static BookDataHelper getInstance() {
	      if(instance == null) {
	         instance = new BookDataHelper();
	      }
	      return instance;
	   }
	   
	   private final WritableList books = new WritableList(new ArrayList<BookTo>(), BookTo.class);
	   
	   public WritableList getBooks() {
			return books;
		}

}
