/**
 * 
 */
package com.princetonsa.action.historiaClinica;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.HistoricoAtencionesForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.historiaClinica.HistoricoAtenciones;

/**
 * 
 * @author axioma
 *
 */
public class HistoricoAtencionesAction extends Action 
{

	private Logger logger=Logger.getLogger(HistoricoAtencionesAction.class);
	
	public ActionForward execute(	ActionMapping mapping, 	
	        ActionForm form, 
	        HttpServletRequest request, 
	        HttpServletResponse response) throws Exception
	{
		if(form instanceof HistoricoAtencionesForm)
		{
			HistoricoAtencionesForm forma=(HistoricoAtencionesForm)form;
			logger.info("ESTADO-->"+forma.getEstado());
			if(forma.getEstado().equals("cargarHistorico"))
			{
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				if(paciente == null||paciente.getCodigoPersona()<0)
				{
					return ComunAction.accionSalirCasoError(mapping,request,null,logger,"Error paciente no cargado","errors.paciente.noCargado",true);
				}
				forma.setCodigoPaciente(paciente.getCodigoPersona());
				HistoricoAtenciones historico=new HistoricoAtenciones(forma.getCodigoPaciente());
				forma.setHistoricoAtenciones(historico.getHistoricoAtenciones());
				logger.info("TAMA HISTORICOS : "+forma.getHistoricoAtenciones().size());
				return mapping.findForward("principal");
			}
			else
			{
				logger.warn("Estado no valido dentro del flujo de HistoricoAtencionesForm: "+forma.getEstado());
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de HistoricoAtencionesForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}    
	}
}
