package com.starterkit.rcpapp.views;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.starterkit.rcapp.data.to.BookTo;
import com.starterkit.rcapp.helpers.BookDataHelper;
import com.starterkit.rcpapp.dialogs.EditBookDialog;
import com.starterkit.rcpapp.dialogs.MyDialog;

public class BookTableView extends ViewPart {
	
	private TableViewer tableViewer;
	private Table table;
	Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

	public BookTableView() {
	}

	public static final String ID = "com.starterkit.rcpapp.view";

	BookDataHelper bookData = BookDataHelper.getInstance();

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	class ViewLabelProvider extends LabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}

	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(Composite parent) {
		getSite().setSelectionProvider(tableViewer);
		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new ViewLabelProvider());

		initInput(tableViewer);
		createColumns(parent, tableViewer);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event event) {

				Point p = new Point(event.x, event.y);
				TableItem item = table.getItem(p);
				if(item != null) {
				BookTo book2edit = (BookTo) item.getData();
				setUpContextMenu(book2edit);
				}

			}
		});
		setUpDoubleClickHandler();

	}

	private void setUpContextMenu(BookTo book2edit) {
		MenuManager popManager = new MenuManager();
		IAction menuAction = new NewRowAction(book2edit);
		popManager.add(menuAction);
		Menu menu = popManager.createContextMenu(table);
		table.setMenu(menu);
	}

	private void setUpDoubleClickHandler() {
		// TODO Auto-generated method stub
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				BookTo book = (BookTo) selection.getFirstElement();
				Dialog bookDialog = new MyDialog(shell, book);
				bookDialog.open();

			}
		});
	}

	private void createColumns(Composite parent, TableViewer viewer) {
		// TODO Auto-generated method stub
		TableViewerColumn colBookId = new TableViewerColumn(viewer, SWT.NONE);
		colBookId.getColumn().setText("Id");
		colBookId.getColumn().setWidth(100);
		colBookId.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				BookTo book = (BookTo) element;
				return String.valueOf(book.getId().intValue());
			}

		});

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		TableViewerColumn colBookTitle = new TableViewerColumn(viewer, SWT.NONE);
		colBookTitle.getColumn().setText("Title");
		colBookTitle.getColumn().setWidth(200);
		colBookTitle.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				BookTo book = (BookTo) element;
				return book.getTitle();
			}

		});

		TableViewerColumn colBookAuthors = new TableViewerColumn(viewer, SWT.NONE);
		colBookAuthors.getColumn().setText("Authors");
		colBookAuthors.getColumn().setWidth(200);
		colBookAuthors.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				BookTo book = (BookTo) element;
				return book.getAuthors();
			}

		});

		TableViewerColumn colBookStatus = new TableViewerColumn(viewer, SWT.NONE);
		colBookStatus.getColumn().setText("Status");
		colBookStatus.getColumn().setWidth(200);
		colBookStatus.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				BookTo book = (BookTo) element;
				return book.getStatus().toString();
			}

		});

	}

	private void initInput(TableViewer tableViewer) {
		ViewerSupport.bind(tableViewer, bookData.getBooks(),
				BeanProperties.values(new String[] { "id", "title", "author", "status" }));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	private class NewRowAction extends Action {

		private BookTo bookTemp;

		public NewRowAction(BookTo book2Edit) {
			super("Edit Book");
			this.bookTemp = book2Edit;
		}

		public void run() {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			Dialog bookEditDialog = new EditBookDialog(shell, bookTemp);
			bookEditDialog.open();

		}
	}
}