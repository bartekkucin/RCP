package com.starterkit.rcpapp.views;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.starterkit.rcapp.data.BookRestServiceClient;
import com.starterkit.rcapp.data.impl.BookRestServiceClientImpl;
import com.starterkit.rcapp.data.to.BookStatus;
import com.starterkit.rcapp.data.to.BookTo;
import com.starterkit.rcapp.helpers.BookDataHelper;

public class BookAddView extends ViewPart {

	public static final String ADD_BOOK_ID = "com.starterkit.rcpapp.addbookview";
	private BookTo book2save = new BookTo();

	public BookAddView() {
	}

	String serverURL = "http://localhost:8080/webstore";
	private final BookDataHelper bookData = BookDataHelper.getInstance();
	private BookRestServiceClient client = new BookRestServiceClientImpl(serverURL);
	Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	private Text titleText;
	private Text authorText;
	private Combo combo;
	private Button btnSaveBook;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(3, false));
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		Label lblAddBook = new Label(parent, SWT.NONE);
		lblAddBook.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblAddBook.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		lblAddBook.setText("Add Book");
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		Label lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setText("Title:");
		new Label(parent, SWT.NONE);

		titleText = new Text(parent, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text.widthHint = 449;
		titleText.setLayoutData(gd_text);

		Label lblAuthors = new Label(parent, SWT.NONE);
		lblAuthors.setText("Authors:");
		new Label(parent, SWT.NONE);

		authorText = new Text(parent, SWT.BORDER);
		GridData gd_text_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_text_1.widthHint = 450;
		authorText.setLayoutData(gd_text_1);

		Label lblStatus = new Label(parent, SWT.NONE);
		lblStatus.setText("Status:");
		new Label(parent, SWT.NONE);

		combo = new Combo(parent, SWT.READ_ONLY);
		String[] bookStatuses = { BookStatus.FREE.name(), BookStatus.LOAN.name(), BookStatus.MISSING.name() };
		combo.setItems(bookStatuses);
		combo.setText(bookStatuses[0]);
		GridData gd_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_combo.widthHint = 146;
		combo.setLayoutData(gd_combo);
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		btnSaveBook = new Button(parent, SWT.NONE);
		btnSaveBook.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				book2save.setStatus(BookStatus.valueOf(combo.getText()));
				if (titleText.getText().isEmpty() || authorText.getText().isEmpty() || combo.getText().isEmpty()) {
					MessageDialog.openError(shell, "Error", "Some fields are empty!");
				} else {
				try {
					IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
							.findView(BookTableView.ID);
					BookTableView view = (BookTableView) part;
					client.saveBook(book2save);
					List<BookTo> books = client.findBooksByTitleAndAuthor("", "");
					bookData.getBooks().clear();
					bookData.getBooks().addAll(books);
					view.getTableViewer().refresh();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}});
		btnSaveBook.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnSaveBook.setText("Save");
		

		bindValues();

	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void bindValues() {


		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				String titleText = (String) value;

				if (titleText == null || titleText.isEmpty()) {
					return ValidationStatus.error("Title is blank!");
				}
				return ValidationStatus.ok();
			}
		});

		DataBindingContext ctx = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(titleText);
		IObservableValue modelValue = BeanProperties.value(BookTo.class, "title").observe(book2save);
		Binding titleBindValue = ctx.bindValue(widgetValue, modelValue, strategy, null);
		ControlDecorationSupport.create(titleBindValue, SWT.TOP | SWT.LEFT);

		UpdateValueStrategy strategy2 = new UpdateValueStrategy();
		strategy2.setBeforeSetValidator(new IValidator() {

			@Override
			public IStatus validate(Object value) {
				String authorText = (String) value;

				if (authorText == null || authorText.isEmpty()) {
					return ValidationStatus.error("Author is blank!");
				}
				return ValidationStatus.ok();
			}
		});

		widgetValue = WidgetProperties.text(SWT.Modify).observe(authorText);
		modelValue = BeanProperties.value(BookTo.class, "authors").observe(book2save);
		Binding authorBindValue = ctx.bindValue(widgetValue, modelValue, strategy2, null);
		ControlDecorationSupport.create(authorBindValue, SWT.TOP | SWT.LEFT);

	}

}
