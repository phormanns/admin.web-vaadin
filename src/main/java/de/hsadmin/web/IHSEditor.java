package de.hsadmin.web;

import com.vaadin.ui.Component;

public interface IHSEditor extends Component {

	public void setValue(String string);

	public Object getValue();

}
