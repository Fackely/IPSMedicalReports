package com.princetonsa.action.facturacion;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.actionform.facturacion.DescuentosOdontologicosForm;


public class DescuentosOdontologicosAction extends Action
{
	/**
	 * 
	 */
	private Logger logger=Logger.getLogger(DescuentosOdontologicosAction.class);
	
	public ActionForward execute(	ActionMapping mapping, 	
									ActionForm form, 
									HttpServletRequest request, 
									HttpServletResponse response) throws Exception
	{
		
		if(form instanceof DescuentosOdontologicosForm)
		{
			
			DescuentosOdontologicosForm forma=(DescuentosOdontologicosForm) form;
			
			
			
			String estado=forma.getEstado();

			logger.info("ESTADO -->"+estado);
			
			if(estado.equals("empezar"))
			{
				forma.reset();
				
			
			
				return mapping.findForward("principal");
			}
		}
		
		return mapping.findForward("principal");

		
	}
}

