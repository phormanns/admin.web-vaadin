package de.hsadmin.rpc;

import java.io.Serializable;

import de.hsadmin.rpc.enums.DisplayPolicy;
import de.hsadmin.rpc.enums.ReadWritePolicy;
import de.hsadmin.rpc.enums.SearchPolicy;

public class PropertyInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String module;
	private String name;
	private String type;
	private int minLength;
	private int maxLength;
	private String validationRegexp;
	private int displaySequence;
	private DisplayPolicy displayVisible;
	private ReadWritePolicy readwriteable;
	private SearchPolicy searchable;
	
	public String getModule() {
		return module;
	}
	
	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getValidationRegexp() {
		return validationRegexp;
	}

	public void setValidationRegexp(String validationRegexp) {
		this.validationRegexp = validationRegexp;
	}

	public int getDisplaySequence() {
		return displaySequence;
	}

	public void setDisplaySequence(int displaySequence) {
		this.displaySequence = displaySequence;
	}

	public DisplayPolicy getDisplayVisible() {
		return displayVisible;
	}

	public void setDisplayVisible(final String displayVisible) {
		this.displayVisible = DisplayPolicy.valueOf(displayVisible.toUpperCase());
	}

	public ReadWritePolicy getReadwriteable() {
		return readwriteable;
	}

	public void setReadwriteable(final String readwriteable) {
		this.readwriteable = ReadWritePolicy.valueOf(readwriteable.toUpperCase());
	}

	public SearchPolicy getSearchable() {
		return searchable;
	}

	public void setSearchable(final String searchable) {
		this.searchable = SearchPolicy.valueOf(searchable.toUpperCase());
	}
	
}
