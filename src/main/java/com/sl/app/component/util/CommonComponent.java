package com.sl.app.component.util;

import com.sl.entity.Menu;
import com.vaadin.flow.component.Component;


public abstract class CommonComponent extends Component{
	
		private static final long serialVersionUID = -1489796323160495508L;
		
		public Menu selectedMenu;

		public CommonComponent(){
			super();
		}
		
		public Menu getSelectedMenu() {
			return selectedMenu;
		}

		public void setSelectedMenu(Menu selectedMenu) {
			this.selectedMenu = selectedMenu;
		}

		public abstract void initLayout();
		
		public abstract void initContent();
		
		public abstract void registerHandler();
		
		
		
}
