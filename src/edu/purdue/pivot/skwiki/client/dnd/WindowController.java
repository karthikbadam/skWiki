/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.purdue.pivot.skwiki.client.dnd;

import java.util.ArrayList;
import java.util.Iterator;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.purdue.pivot.skwiki.shared.AbstractLayoutHistory;
import edu.purdue.pivot.skwiki.shared.ChangePosHistory;
import edu.purdue.pivot.skwiki.shared.ChangeSizeHistory;
import gwtupload.client.PreloadedImage;

final public class WindowController {

	private final AbsolutePanel boundaryPanel;

	private MyWindowController pickupDragController;

	private MyResizeDragController resizeDragController;

	final ArrayList<AbstractLayoutHistory> layoutHistoryList;

	private class MyResizeDragController extends ResizeDragController {
		public MyResizeDragController(AbsolutePanel boundaryPanel)

		{
			super(boundaryPanel);
		}

		/*
		 * @Override public void dragMove() { super.dragMove();
		 * 
		 * String id = windowPanel.editPanel.getID();
		 * 
		 * ChangeSizeHistory changeSizeHistory = new ChangeSizeHistory(id,
		 * windowPanel.getContentWidth(), windowPanel.getContentHeight());
		 * 
		 * layoutHistoryList.add(changeSizeHistory); }
		 */

		@Override
		public void dragEnd() {
			super.dragEnd();
		
			String id;
			if (windowPanel.contentPanel instanceof PreloadedImage) {
				PreloadedImage image = ((PreloadedImage)windowPanel.contentPanel); 
				id = image.getUniqId();
				
			} else {
				id = windowPanel.editPanel.getID();
			}
		
			ChangeSizeHistory changeSizeHistory = new ChangeSizeHistory(id,
					windowPanel.getContentWidth(),
					windowPanel.getContentHeight());

			layoutHistoryList.add(changeSizeHistory);
		}

	}

	private class MyWindowController extends PickupDragController {
		public MyWindowController(AbsolutePanel boundaryPanel) {
			// ArrayList<AbstractLayoutHistory> layoutHistoryList)
			super(boundaryPanel, true);
		}
		
		
		
		@Override
		public void dragEnd() {

			WidgetLocation currentDraggableLocation = getDraggableLocation();

			for (Iterator<Widget> iterator = context.selectedWidgets.iterator(); iterator
					.hasNext();) {
				Widget widget = iterator.next();
				String id;
				if (((WindowPanel) widget).contentPanel instanceof PreloadedImage) {
					PreloadedImage image = ((PreloadedImage)((WindowPanel) widget).contentPanel); 
					id = image.getUniqId();
				} else {
					id = ((WindowPanel) widget).editPanel.getID();
				}
				
				//TODO
				ChangePosHistory changePosHistory = new ChangePosHistory(id,
						currentDraggableLocation.getLeft(),
						currentDraggableLocation.getTop());

				layoutHistoryList.add(changePosHistory);
				System.out.println("Drag end");
			}

			/*
			 * String id = ( (WindowPanel)movablePanel).editPanel.getID();
			 * ChangePosHistory changePosHistory = new ChangePosHistory(id,
			 * currentDraggableLocation.getLeft(),
			 * currentDraggableLocation.getTop());
			 * 
			 * layoutHistoryList.add(changePosHistory);
			 */

			super.dragEnd();
		}

		/*
		 * void updateLayoutHistoryList() {
		 * 
		 * AbsolutePanel parent = (AbsolutePanel) getParent(); Location location
		 * = new WidgetLocation(this, parent); }
		 */

	}

	public WindowController(AbsolutePanel boundaryPanel,
			ArrayList<AbstractLayoutHistory> layoutHistoryList) {
		this.boundaryPanel = boundaryPanel;

		// pickupDragController = new PickupDragController(boundaryPanel, true);
		pickupDragController = new MyWindowController(boundaryPanel);
		pickupDragController.setBehaviorConstrainedToBoundaryPanel(true);
		pickupDragController.setBehaviorMultipleSelection(false);

		resizeDragController = new MyResizeDragController(boundaryPanel);
		resizeDragController.setBehaviorConstrainedToBoundaryPanel(true);
		resizeDragController.setBehaviorMultipleSelection(false);

		this.layoutHistoryList = layoutHistoryList;
	}

	/*
	 * public Location getLocation() {
	 * 
	 * AbsolutePanel parent = (AbsolutePanel) getParent(); Location location =
	 * new WidgetLocation(this, parent);
	 * 
	 * return location; }
	 */
	public AbsolutePanel getBoundaryPanel() {
		return boundaryPanel;
	}

	public PickupDragController getPickupDragController() {
		return pickupDragController;
	}

	public ResizeDragController getResizeDragController() {
		return resizeDragController;
	}
}
