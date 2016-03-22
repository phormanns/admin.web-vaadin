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
	
	void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	void setType(String type) {
		this.type = type;
	}

	public int getMinLength() {
		return minLength;
	}

	void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getValidationRegexp() {
		return validationRegexp;
	}

	void setValidationRegexp(String validationRegexp) {
		this.validationRegexp = validationRegexp;
	}

	public int getDisplaySequence() {
		return displaySequence;
	}

	void setDisplaySequence(int displaySequence) {
		this.displaySequence = displaySequence;
	}

	public DisplayPolicy getDisplayVisible() {
		return displayVisible;
	}

	void setDisplayVisible(final String displayVisible) {
		this.displayVisible = DisplayPolicy.valueOf(displayVisible.toUpperCase());
	}

	public ReadWritePolicy getReadwriteable() {
		return readwriteable;
	}

	void setReadwriteable(final String readwriteable) {
		this.readwriteable = ReadWritePolicy.valueOf(readwriteable.toUpperCase());
	}

	public SearchPolicy getSearchable() {
		return searchable;
	}

	void setSearchable(final String searchable) {
		this.searchable = SearchPolicy.valueOf(searchable.toUpperCase());
	}
	
	@Override
	public String toString() {
		return "PropertyInfo " + module + "." + name;
	}
}
