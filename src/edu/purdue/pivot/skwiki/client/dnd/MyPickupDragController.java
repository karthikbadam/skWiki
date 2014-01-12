package edu.purdue.pivot.skwiki.client.dnd;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class MyPickupDragController extends PickupDragController{

	public MyPickupDragController(AbsolutePanel boundaryPanel,
			boolean allowDroppingOnBoundaryPanel) {
		super(boundaryPanel, allowDroppingOnBoundaryPanel);
		// TODO Auto-generated constructor stub
	}

	@Override
	  public void dragEnd() {
	   
	    super.dragEnd();
	    /* 
	    if(context.vetoException!=null)
	    {
	    	com.google.gwt.user.client.Window.alert("no on drop");
	    }
	    else
	    {
	    	com.google.gwt.user.client.Window.alert("on drop");
	    }*/
	  }
	
	
	
}
