package com.starterkit.rcpapp.views;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.starterkit.rcapp.data.BookRestServiceClient;
import com.starterkit.rcapp.data.impl.BookRestServiceClientImpl;
import com.starterkit.rcapp.data.to.BookTo;
import com.starterkit.rcapp.helpers.BookDataHelper;

public class BookMenuViewPart extends ViewPart {
	private Text title;
	private Text author;
	String serverURL = "http://localhost:8080/webstore";
	private final BookDataHelper bookData = BookDataHelper.getInstance();
	private BookRestServiceClient client = new BookRestServiceClientImpl(serverURL);
	public static final String MENU_BOOK_ID = "com.starterkit.rcpapp.bookmenuview";
	Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

	public BookMenuViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(2, false));

		Label lblMojalabelka = new Label(parent, SWT.NONE);
		lblMojalabelka.setText("Title:");

		title = new Text(parent, SWT.BORDER);
		title.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setText("Authors:");

		author = new Text(parent, SWT.BORDER);
		author.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		Button btnSearchBooks = new Button(parent, SWT.NONE);
		GridData gd_btnSearchBooks = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnSearchBooks.widthHint = 135;
		btnSearchBooks.setLayoutData(gd_btnSearchBooks);
		btnSearchBooks.setText("Search");

		Button btnDeleteBook = new Button(parent, SWT.NONE);
		btnDeleteBook.setText("Delete Book");
		new Label(parent, SWT.NONE);

		btnDeleteBook.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.findView(BookTableView.ID);
				if (part instanceof BookTableView) {
					BookTableView view = (BookTableView) part;
					BookTo book = (BookTo) view.getTableViewer().getStructuredSelection().getFirstElement();
					if (book == null) {
						MessageDialog.openError(shell, "Error", "You have to select book firstly!");
					} else {
						client.deleteBook(book.getId());
						List<BookTo> books = client.findBooksByTitleAndAuthor(author.getText(), title.getText());
						bookData.getBooks().clear();
						bookData.getBooks().addAll(books);
						view.getTableViewer().refresh();
					}
				}
			}

		});

		btnSearchBooks.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				List<BookTo> books = client.findBooksByTitleAndAuthor(author.getText(), title.getText());
				bookData.getBooks().clear();
				bookData.getBooks().addAll(books);
				IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.findView(BookTableView.ID);
				if (part instanceof BookTableView) {
					BookTableView view = (BookTableView) part;
					view.getTableViewer().refresh();
				}

			}
		});

	}

	public static IViewPart getView(IWorkbenchWindow window, String viewId) {
		IViewReference[] refs = window.getActivePage().getViewReferences();
		for (IViewReference viewReference : refs) {
			if (viewReference.getId().equals(viewId)) {
				return viewReference.getView(true);
			}
		}
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
