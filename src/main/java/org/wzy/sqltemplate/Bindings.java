package org.wzy.sqltemplate;

import java.util.HashMap;

public class Bindings extends HashMap<String,Object>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7290846439659491933L;

	public Bindings bind(String key , Object value ){
		this.put(key, value) ;
		
		return this; 
	}

}
