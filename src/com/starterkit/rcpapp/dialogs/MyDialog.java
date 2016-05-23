package com.starterkit.rcpapp.dialogs;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.starterkit.rcapp.data.to.BookStatus;
import com.starterkit.rcapp.data.to.BookTo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MyDialog extends TitleAreaDialog {
	
	private String bookTitle;
	private String bookAuthor;
	private BookStatus bookStatus;
	private Text text;
	private Text text_1;
	private Text text_2;
	

	public MyDialog(Shell parentShell, BookTo book) {
		super(parentShell);
		this.bookTitle = book.getTitle();
		this.bookAuthor = book.getAuthors();
		this.bookStatus = book.getStatus();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	  protected Control createDialogArea(Composite parent) {
	    Composite area = (Composite) super.createDialogArea(parent);
	    Composite container = new Composite(area, SWT.NONE);
	    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    GridLayout layout = new GridLayout(2, false);
	    container.setLayout(layout);
	    
	    Label lblInfoModalTitleLabel = new Label(container, SWT.NONE);
	    lblInfoModalTitleLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    lblInfoModalTitleLabel.setText("Title: ");
	    
	    text = new Text(container, SWT.BORDER);
	    text.setText(this.bookTitle);
	    text.setEditable(false);
	    text.setEnabled(false);
	    text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    Label lblInfoModalAuthorLabel = new Label(container, SWT.NONE);
	    lblInfoModalAuthorLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    lblInfoModalAuthorLabel.setText("Author: ");
	    
	    text_1 = new Text(container, SWT.BORDER);
	    text_1.setText(this.bookAuthor);
	    text_1.setEditable(false);
	    text_1.setEnabled(false);
	    text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	    
	    Label lblInfoModalStatusLabel = new Label(container, SWT.NONE);
	    lblInfoModalStatusLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	    lblInfoModalStatusLabel.setText("Status: ");
	    
	    text_2 = new Text(container, SWT.BORDER);
	    text_2.setText(this.bookStatus.toString());
	    text_2.setEditable(false);
	    text_2.setEnabled(false);
	    text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));


	    return area;
	  }
	
	@Override
	public void create() {
		super.create();
		setTitle("Book Information");
		setMessage("Selected Book Information", IMessageProvider.INFORMATION);

		
	}

}
