package com.starterkit.rcpapp.dialogs;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import com.starterkit.rcapp.data.BookRestServiceClient;
import com.starterkit.rcapp.data.impl.BookRestServiceClientImpl;
import com.starterkit.rcapp.data.to.BookStatus;
import com.starterkit.rcapp.data.to.BookTo;
import com.starterkit.rcapp.helpers.BookDataHelper;
import com.starterkit.rcpapp.views.BookTableView;

public class EditBookDialog extends TitleAreaDialog {

	private Long id;
	private String title;
	private String authors;
	private BookStatus status;
	private Text titleText;
	private Text authorText;
	private Combo combo;
	private BookTo book;
	private Text idText;
	String serverURL = "http://localhost:8080/webstore";
	private final BookDataHelper bookData = BookDataHelper.getInstance();
	private BookRestServiceClient client = new BookRestServiceClientImpl(serverURL);

	public EditBookDialog(Shell parentShell, BookTo book2edit) {
		super(parentShell);
		this.id = book2edit.getId();
		this.title = book2edit.getTitle();
		this.authors = book2edit.getAuthors();
		this.status = book2edit.getStatus();
		this.book = book2edit;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		Label lblIdLabel = new Label(container, SWT.NONE);
		lblIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblIdLabel.setText("ID: ");

		idText = new Text(container, SWT.BORDER);
		idText.setText(String.valueOf(this.id));
		idText.setEditable(false);
		idText.setEnabled(false);
		idText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblInfoModalTitleLabel = new Label(container, SWT.NONE);
		lblInfoModalTitleLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInfoModalTitleLabel.setText("Title: ");

		titleText = new Text(container, SWT.BORDER);
		titleText.setText(String.valueOf(this.title));
		titleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblInfoModalAuthorLabel = new Label(container, SWT.NONE);
		lblInfoModalAuthorLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInfoModalAuthorLabel.setText("Author: ");

		authorText = new Text(container, SWT.MULTI | SWT.H_SCROLL | SWT.WRAP | SWT.BORDER);
		authorText.setText(this.authors);
		authorText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblInfoModalStatusLabel = new Label(container, SWT.NONE);
		lblInfoModalStatusLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblInfoModalStatusLabel.setText("Status: ");

		combo = new Combo(container, SWT.READ_ONLY);
		String[] bookStatuses = { BookStatus.FREE.name(), BookStatus.LOAN.name(), BookStatus.MISSING.name() };
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		combo.setItems(bookStatuses);
		combo.setText(this.book.getStatus().name());
		gd_combo.widthHint = 146;
		combo.setLayoutData(gd_combo);

		bindValues();

		return area;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Book Editor");
		setMessage("Selected Book Information", IMessageProvider.INFORMATION);

	}

	private void bindValues() {

		DataBindingContext ctx = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(titleText);
		IObservableValue modelValue = BeanProperties.value(BookTo.class, "id").observe(book);
		ctx.bindValue(widgetValue, modelValue);

		widgetValue = WidgetProperties.text(SWT.Modify).observe(titleText);
		modelValue = BeanProperties.value(BookTo.class, "title").observe(book);
		ctx.bindValue(widgetValue, modelValue);

		// Bind the age including a validator
		widgetValue = WidgetProperties.text(SWT.Modify).observe(authorText);
		modelValue = BeanProperties.value(BookTo.class, "authors").observe(book);

		Binding bindValue = ctx.bindValue(widgetValue, modelValue);
		// add some decorations
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

	}

	@Override
	protected void okPressed() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		if (titleText.getText().isEmpty() || authorText.getText().isEmpty() || combo.getText().isEmpty()) {
			MessageDialog.openError(shell, "Error", "Some fields are empty!");
		} else {
			book.setStatus(BookStatus.valueOf(combo.getText()));
			book.setId(this.id);
			IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.findView(BookTableView.ID);
			BookTableView view = (BookTableView) part;
			try {
				BookTo bookEdited = client.saveBook(book);
				List<BookTo> books = client.findBooksByTitleAndAuthor("", "");
				bookData.getBooks().clear();
				bookData.getBooks().addAll(books);
				view.getTableViewer().refresh();
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.okPressed();
		}
	}

}
