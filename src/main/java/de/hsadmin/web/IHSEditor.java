package de.hsadmin.web;

import java.util.Map;

import com.vaadin.data.Validator;
import com.vaadin.ui.Component;

public interface IHSEditor extends Component {

	public void setValues(final Map<String, Object> valuesMap);

	public Object getValue();

	public void addValidator(Validator validator);

}
