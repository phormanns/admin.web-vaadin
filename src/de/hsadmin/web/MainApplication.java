package de.hsadmin.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Assertion;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.Terminal;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window.Notification;

import de.hsadmin.web.config.LocaleConfig;
import de.hsadmin.web.config.ModuleConfig;

public class MainApplication extends Application implements HttpServletRequestListener, TabSheet.SelectedTabChangeListener {

	private static final long serialVersionUID = 1L;
	
	private HttpSession httpSession;
	private ServletContext servletContext;
	private AttributePrincipal userPrincipal;
	private LocaleConfig localeConfig;
	private Remote remote;
	private Map<String, GenericModule> modules;


	@Override
	public void init() {
		localeConfig = new LocaleConfig(Locale.getDefault(), "main");
		remote = new Remote(this);
		Window mainWindow = new Window(localeConfig.getText("applicationtitle"));
		TabSheet tabs = new TabSheet();
		tabs.setWidth(100.0f, Sizeable.UNITS_PERCENTAGE);
		tabs.setHeight(200.0f, Sizeable.UNITS_PERCENTAGE);
		String modulesParamString = getContextParam("hsarmodules");
		modules = new HashMap<String, GenericModule>();
		GenericModule firstModule = null;
		for (String className : modulesParamString.split(",")) {
			try {
				GenericModule module = (GenericModule) Class.forName(className).newInstance();
				module.setApplication(this);
				if (firstModule == null) {
					firstModule = module;
				}
				ModuleConfig moduleConfig = module.getModuleConfig();
				String label = moduleConfig.getLabel("moduletitle");
				modules.put(label, module);
				tabs.addTab(module.getComponent(), label, new ThemeResource(moduleConfig.getLabel("moduleicon")));
			} catch (Exception e) {
				showSystemException(e);
			}
		}
		tabs.addListener(this);
		mainWindow.addComponent(tabs);
		setMainWindow(mainWindow);
		setErrorHandler(new Terminal.ErrorListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void terminalError(Terminal.ErrorEvent event) {
				event.getThrowable().printStackTrace();
			}
		});
		try {
			firstModule.reload();
		} catch (HsarwebException e) {
			showSystemException(e);
		}
	}

	public String getProxyTicket() {
		return userPrincipal.getProxyTicketFor(servletContext.getInitParameter("backendURL"));
	}

	public String getContextParam(String string) {
		return servletContext.getInitParameter(string);
	}

	public Object getLogin() {
		return userPrincipal.getName();
	}

	public Remote getRemote() {
		return remote;
	}

	public LocaleConfig getLocaleConfig() {
		return localeConfig;
	}

	@Override
	public void onRequestStart(HttpServletRequest request,
			HttpServletResponse response) {
		httpSession = request.getSession();
		servletContext = httpSession.getServletContext();
		userPrincipal = ((Assertion) httpSession.getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION)).getPrincipal();
	}

	@Override
	public void onRequestEnd(HttpServletRequest request,
			HttpServletResponse response) {
		
	}

	@Override
	public void selectedTabChange(SelectedTabChangeEvent event) {
		TabSheet tabSheet = event.getTabSheet();
		Component selectedTab = tabSheet.getSelectedTab();
		Tab tab = tabSheet.getTab(selectedTab);
		GenericModule module = modules.get(tab.getCaption());
		try {
			module.reload();
		} catch (HsarwebException e) {
			showSystemException(e);
		}
	}

	public void showUserException(Exception e) {
		getMainWindow().showNotification("Anwendungs-Fehler", "<br/ >" + e.getMessage(), Notification.TYPE_WARNING_MESSAGE);			
	}

	public void showSystemException(Exception e) {
		getMainWindow().showNotification("System-Fehler", "<br />Bitte informieren Sie den Support<br/ >" + e.getMessage(), Notification.TYPE_ERROR_MESSAGE);			
	}

}
