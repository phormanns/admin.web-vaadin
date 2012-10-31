<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>HSAdmin Web Client</title>
	<script type="text/javascript">
	  var vaadin = {
	    vaadinConfigurations: {
	      'hsarweb': {
		        appUri:'/hsarweb/MainApplication',
		        pathInfo: '/',
		        themeUri: '/hsarweb/VAADIN/themes/hs',
		        versionInfo : {}
	      }
	    }};
	</script>
	<script language='javascript' src='/hsarweb/VAADIN/widgetsets/com.vaadin.terminal.gwt.DefaultWidgetSet/com.vaadin.terminal.gwt.DefaultWidgetSet.nocache.js'>
	</script>
  	<link rel="stylesheet" type="text/css" href="/hsarweb/VAADIN/themes/reindeer/styles.css"/>
  	<!--#include virtual="/hostsharing/includes/head.html" -->
</head>
<body>
	<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>
	<!--#include virtual="/hostsharing/includes/header.html" -->
	<div id="hsarweb" style="width:100%;height:800px;"
		class="v-app v-app-loading v-theme-reindeer v-app-MainApplication"> </div>
	<!--#include virtual="/hostsharing/includes/footer.html" -->
</body>
</html>