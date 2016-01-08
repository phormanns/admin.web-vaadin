package de.hsadmin.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import com.vaadin.data.Validator;

import de.hsadmin.model.IRemote;
import de.hsadmin.model.TicketService;
import de.hsadmin.rpc.HSAdminSession;
import de.hsadmin.rpc.ModulesManager;
import de.hsadmin.rpc.PropertyInfo;
import de.hsadmin.rpc.RpcException;
import de.hsadmin.rpc.enums.ReadWritePolicy;

public class GenericEditorFactory implements IEditorFactory, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public IHSEditor getEditor(final String action, final PropertyInfo propertyInfo, final HSAdminSession session, final Map<String, String> whereContext) 
	{
		final String inputName = propertyInfo.getName();
		final String module = propertyInfo.getModule();
		if ("domain".equals(module)) {
			if ("user".equals(inputName)) {
				return getSelectFromRemote(action, propertyInfo, session, "user", whereContext);
			}
			if ("domainoptions".equals(inputName)) {
				return getDomainOptionsEditor(action, propertyInfo, session, whereContext);
			}
		}
		if ("emailaddress".equals(module) || "emailalias".equals(module)) {
			if ("target".equals(inputName)) {
				return getEMailTargetEditor(action, propertyInfo, session, whereContext);
			}
		}
		if ("mysqldb".equals(module)) {
			if ("owner".equals(inputName)) {
				return getSelectFromRemote(action, propertyInfo, session, "mysqluser", whereContext);
			}
		}
		if ("postgresqldb".equals(module)) {
			if ("owner".equals(inputName)) {
				return getSelectFromRemote(action, propertyInfo, session, "postgresqluser", whereContext);
			}
		}
		return getEditor(action, propertyInfo);
	}


	private IHSEditor getDomainOptionsEditor(final String action, final PropertyInfo propertyInfo, final HSAdminSession session, final Map<String, String> whereContext) {
		return PanelToolbar.ACTION_EDIT.equals(action) ? new DomainOptionsEditor(propertyInfo, session, whereContext) : new NullEditor();
	}


	private IHSEditor getEMailTargetEditor(final String action, final PropertyInfo propertyInfo, final HSAdminSession session, final Map<String, String> whereContext) 
	{
		return new EMailTargetEditor(action, propertyInfo, session, whereContext);
	}


	@Override
	public IHSEditor getEditor(final String action, final PropertyInfo propertyInfo) 
	{
		final String inputName = propertyInfo.getName();
		if ("password".equals(inputName)) {
			return getPasswordField(action, propertyInfo);
		}
		if ("shell".equals(inputName)) {
			return getShellSelect(action, propertyInfo);
		}
		final String module = propertyInfo.getModule();
		if ("user".equals(module) || "emailalias".equals(module)) {
			if ("name".equals(inputName)) {
				return getPacPrefixedField(action, propertyInfo, '-');
			}
		}
		if ("mysqluser".equals(module) || "postgresqluser".equals(module)) {
			if ("name".equals(inputName)) {
				return getPacPrefixedField(action, propertyInfo, '_');
			}
		}
		if ("mysqldb".equals(module) || "postgresqldb".equals(module)) {
			if ("name".equals(inputName)) {
				return getPacPrefixedField(action, propertyInfo, '_');
			}
			if ("encoding".equals(inputName)) {
				return getSelectField(action, propertyInfo, "UTF8", "LATIN1");
			}
		}
		return getTextField(action, propertyInfo);
	}

	private IHSEditor getSelectField(final String action, final PropertyInfo propertyInfo, final String... items) 
	{
		final HSSelect field = new HSSelect(propertyInfo.getName(), Arrays.asList(items));
		field.setWidth("100%");
		field.setEnabled(isWriteAble(propertyInfo, action));
		return field;
	}


	private IHSEditor getPacPrefixedField(final String action, final PropertyInfo propertyInfo, final char delimiter) 
	{
		final HSPacPrefixedField field = new HSPacPrefixedField(propertyInfo.getName(), delimiter);
		field.setWidth("100%");
		field.setValue("xyz00-");
		enableAndValidate(action, propertyInfo, field);
		return field;
	}

	private IHSEditor getShellSelect(final String action, final PropertyInfo propertyInfo) 
	{
		final String[] items = new String[] { "/bin/false", "/bin/bash", "/bin/csh", "/bin/dash", "/bin/ksh", "/bin/tcsh", "/bin/zsh", "/usr/bin/passwd", "/usr/bin/scponly" };
		final HSSelect field = new HSSelect(propertyInfo.getName(), 7, Arrays.asList(items));
		field.setWidth("100%");
		field.setEnabled(isWriteAble(propertyInfo, action));
		return field;
	}

	private IHSEditor getSelectFromRemote(final String action, final PropertyInfo propertyInfo, final HSAdminSession session, final String module, final Map<String, String> whereContext) 
	{
		final List<String> selectList = new ArrayList<>();
		try {
			final String grantingTicket = session.getGrantingTicket();
			final TicketService ticketService = session.getTicketService();
			final String serviceTicket = ticketService.getServiceTicket(grantingTicket);
			final ModulesManager modulesManager = session.getModulesManager();
			final IRemote proxy = modulesManager.proxy(module);
			final String user = session.getUser();
			final List<Map<String, Object>> list = proxy.search(user, serviceTicket, whereContext);
			for (Map<String, Object> usr : list) {
				selectList.add(usr.get("name").toString());
			}
		} catch (RpcException | XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final HSSelect field = new HSSelect(propertyInfo.getName(), selectList);
		field.setEnabled(isWriteAble(propertyInfo, action));
		field.setWidth("100%");
		return field;
	}

	private IHSEditor getPasswordField(final String action, final PropertyInfo propertyInfo) 
	{
		final HSPasswordField field = new HSPasswordField(propertyInfo.getName(), "new".equals(action));
		field.setWidth("100%");
		field.setEnabled("new".equals(action) || "edit".equals(action));
		return field;
	}

	private IHSEditor getTextField(final String action, final PropertyInfo propertyInfo) 
	{
		final HSTextField field = new HSTextField(propertyInfo.getName());
		field.setWidth("100%");
		enableAndValidate(action, propertyInfo, field);
		return field;
	}

	private void enableAndValidate(final String action, final PropertyInfo propertyInfo, final IHSEditor field) 
	{
		if (isWriteAble(propertyInfo, action)) {
			final String regexp = propertyInfo.getValidationRegexp();
			final int minLength = propertyInfo.getMinLength();
			final int maxLength = propertyInfo.getMaxLength();
			field.addValidator(new Validator() {
				private static final long serialVersionUID = 1L;
				@Override
				public void validate(Object value) throws InvalidValueException {
					final String inputString = (String) value;
					if (inputString == null || !inputString.matches(regexp)) {
						throw new InvalidValueException("input must match " + regexp);
					}
					if (inputString.length() < minLength) {
						throw new InvalidValueException("minimal length is " + minLength);
					}
					if (inputString.length() > maxLength) {
						throw new InvalidValueException("maximal length is " + maxLength);
					}
				}
			});
		} else {
			field.setEnabled(false);
		}
	}

	private boolean isWriteAble(PropertyInfo propertyInfo, String action) {
		if (PanelToolbar.ACTION_VIEW.equals(action)) {
			return false;
		}
		return (ReadWritePolicy.WRITEONCE.equals(propertyInfo.getReadwriteable()) && PanelToolbar.ACTION_NEW.equals(action)) ||
				 (ReadWritePolicy.READWRITE.equals(propertyInfo.getReadwriteable()) && ( PanelToolbar.ACTION_EDIT.equals(action) || PanelToolbar.ACTION_NEW.equals(action) ));
	}

}
