package com.starterkit.handlers;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

public class AddBookHandler extends AbstractHandler implements IHandler {

	public static final String ID = "com.starterkit.rcpapp.addbookcommand";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		try {
			return window.getActivePage().showView("com.starterkit.rcpapp.addbookview");
		} catch (PartInitException e) {

			e.printStackTrace();
		}
		return window;
		
		
	}



}
