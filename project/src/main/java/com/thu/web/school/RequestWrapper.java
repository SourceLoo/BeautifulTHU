package com.thu.web.school;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public final class RequestWrapper extends HttpServletRequestWrapper {   
      
	public RequestWrapper(HttpServletRequest servletRequest) {   
		super(servletRequest);   
	}   
	
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramMap = super.getParameterMap();
		Set<String> keySet = paramMap.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String[] str = paramMap.get(key);
			for(int i = 0; i < str.length; i++) {
				str[i] = cleanXSS(str[i]);
			}
			paramMap.put(key, str);
		}
		return paramMap;
	}
    
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);   
		if (values==null)  {   
			return null;   
		}   
		int count = values.length;   
		String[] encodedValues = new String[count];   
		for (int i = 0; i < count; i++) {   
			encodedValues[i] = cleanXSS(values[i]);   
		}     
		return encodedValues;    
	}   
   
	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);   
		if (value == null) {   
			return null;    
		}   
		return cleanXSS(value);   
	}   
 
	public String getHeader(String name) {
		String value = super.getHeader(name);   
		if (value == null) { 
			return null;  
		} 
		return cleanXSS(value);
	}   

	private String cleanXSS(String value) {
		/*value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");   
		//value = value.replaceAll("(", "&#40;").replaceAll(")", "&#41;");   
		value = value.replaceAll("'", "&#39;");   
		value = value.replaceAll("\"", "&#39;");  */
		value = value.replaceAll("(?i)eval", "");  
		value = value.replaceAll("(?i)alert", "");
		value = value.replaceAll("(?i)prompt", "");
		value = value.replaceAll("(?i)confirm", ""); 
		value = value.replaceAll("(?i)javascript", "");   
		value = value.replaceAll("(?i)script", "");  
		value = value.replaceAll("(?i)onClick", "");  
		value = value.replaceAll("(?i)onDblClick", "");  
		value = value.replaceAll("(?i)onMouseDown", "");  
		value = value.replaceAll("(?i)onMouseUp", "");  
		value = value.replaceAll("(?i)onMouseOver", "");  
		value = value.replaceAll("(?i)onMouseMove", "");  
		value = value.replaceAll("(?i)onMouseOut", "");  
		value = value.replaceAll("(?i)onKeyPress", "");  
		value = value.replaceAll("(?i)onKeyDown", "");  
		value = value.replaceAll("(?i)onKeyUp", "");  
		value = value.replaceAll("(?i)onAbort", "");  
		value = value.replaceAll("(?i)onBeforeUnload", "");  
		value = value.replaceAll("(?i)onError", "");  
		value = value.replaceAll("(?i)onLoad", "");  
		value = value.replaceAll("(?i)onMove", "");  
		value = value.replaceAll("(?i)onResize", "");  
		value = value.replaceAll("(?i)onScroll", "");  
		value = value.replaceAll("(?i)onStop", "");  
		value = value.replaceAll("(?i)onUnload", "");  
		value = value.replaceAll("(?i)onBlur", "");  
		value = value.replaceAll("(?i)onChange", "");  
		value = value.replaceAll("(?i)onFocus", "");  
		value = value.replaceAll("(?i)onReset", "");  
		value = value.replaceAll("(?i)onSubmit", "");  
		value = value.replaceAll("(?i)onBounce", "");  
		value = value.replaceAll("(?i)onFinish", "");  
		value = value.replaceAll("(?i)onStart", "");  
		value = value.replaceAll("(?i)onBeforeCopy", "");  
		value = value.replaceAll("(?i)onBeforeCut", "");  
		value = value.replaceAll("(?i)onBeforeEditFocus", "");  
		value = value.replaceAll("(?i)onBeforePaste", "");  
		value = value.replaceAll("(?i)onBeforeUpdate", "");  
		value = value.replaceAll("(?i)onContextMenu", "");  
		value = value.replaceAll("(?i)onCopy", "");  
		value = value.replaceAll("(?i)onCut", "");  
		value = value.replaceAll("(?i)onDrag", "");  
		value = value.replaceAll("(?i)onDragDrop", "");  
		value = value.replaceAll("(?i)onDragEnd", "");  
		value = value.replaceAll("(?i)onDragEnter", "");  
		value = value.replaceAll("(?i)onDragLeave", "");
		value = value.replaceAll("(?i)onDragOver", "");
		value = value.replaceAll("(?i)onDragStart", "");
		value = value.replaceAll("(?i)onDrop", "");
		value = value.replaceAll("(?i)onLoseCapture", "");
		value = value.replaceAll("(?i)onPaste", "");
		value = value.replaceAll("(?i)onSelect", "");
		value = value.replaceAll("(?i)onSelectStart", "");
		value = value.replaceAll("(?i)onAfterUpdate", "");
		value = value.replaceAll("(?i)onCellChange", "");
		value = value.replaceAll("(?i)onDataAvailable", "");
		value = value.replaceAll("(?i)onDatasetChanged", "");
		value = value.replaceAll("(?i)onDatasetComplete", "");
		value = value.replaceAll("(?i)onErrorUpdate", "");
		value = value.replaceAll("(?i)onRowEnter", "");
		value = value.replaceAll("(?i)onRowExit", "");
		value = value.replaceAll("(?i)onRowsDelete", "");
		value = value.replaceAll("(?i)onRowsInserted", "");
		value = value.replaceAll("(?i)onAfterPrint", "");
		value = value.replaceAll("(?i)onBeforePrint", "");
		value = value.replaceAll("(?i)onFilterChange", "");
		value = value.replaceAll("(?i)onHelp", "");
		value = value.replaceAll("(?i)onPropertyChange", "");
		value = value.replaceAll("(?i)onReadyStateChange", "");
		return cleanSQL(value);
	}    
	
	private String cleanSQL(String value) {
		value = value.replaceAll("(?i)exec ", "");
		value = value.replaceAll("(?i)execute ", "");
		value = value.replaceAll("(?i)insert ", "");
		value = value.replaceAll("(?i)create ", "");
		value = value.replaceAll("(?i)delete ", "");
		value = value.replaceAll("(?i)update ", "");
		value = value.replaceAll("(?i)count ", "");
		value = value.replaceAll("(?i)drop ", "");
		value = value.replaceAll("(?i)chr ", "");
		value = value.replaceAll("(?i)mid ", "");
		value = value.replaceAll("(?i)master ", "");
		value = value.replaceAll("(?i)truncate ", "");
		value = value.replaceAll("(?i)char ", "");
		value = value.replaceAll("(?i)declare ", "");
		value = value.replaceAll("(?i)sitename ", "");
		value = value.replaceAll("(?i)net user ", "");
		value = value.replaceAll("(?i)xp_cmdshell ", "");
		value = value.replaceAll("(?i)table ", "");
		value = value.replaceAll("(?i)grant ", "");
		value = value.replaceAll("(?i)use ", "");
		value = value.replaceAll("(?i)group_concat ", "");
		value = value.replaceAll("(?i)column_name ", "");
		value = value.replaceAll("(?i)information_schema.columns ", "");
		value = value.replaceAll("(?i)table_schema ", "");
		value = value.replaceAll("(?i)union ", "");
		return value;
	}
}  
