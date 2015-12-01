package com.princetonsa.actionform;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionForm;

/**
 * ActionForm que permite capturar datos dinámicos
 *
 * @version 1.0, Apr 14, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class AntecedentesGinecoObstetricosMapForm extends ActionForm
{

	private final Map valores = new HashMap();
	
	public void setValue(String key, Object value) 
	{
		valores.put(key, value);
	}

	public Object getValue(String key) 
	{
		return valores.get(key);
	}
}
