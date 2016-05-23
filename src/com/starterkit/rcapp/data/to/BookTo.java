package com.starterkit.rcapp.data.to;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Transport object for book.
 *
 * @author Leszek
 */
public class BookTo {
	private Long id;
	private String title;
	private String authors;
	private BookStatus status;

	public static final String FIELD_TITLE = "title";
	public static final String FIELD_AUTHORS = "authors";

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public BookTo() {
	}

	public BookTo(Long id, String title, String authors, BookStatus status) {
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		propertyChangeSupport.firePropertyChange(FIELD_TITLE, this.title, this.title = title);
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		propertyChangeSupport.firePropertyChange(FIELD_AUTHORS, this.authors, this.authors = authors);
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BookTo [id=" + id + ", title=" + title + ", authors=" + authors + ", status=" + status + "]";
	}

}
