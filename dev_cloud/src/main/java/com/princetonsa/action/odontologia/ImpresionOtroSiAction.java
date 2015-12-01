/**
 * 
 */
package com.princetonsa.action.odontologia;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.actionform.odontologia.ImpresionOtroSiForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author axioma
 *
 */
public class ImpresionOtroSiAction extends Action {
	
	/**
	 * manejo de logs  
	 */
	private Logger logger = Logger.getLogger(ImpresionOtroSiAction.class);
	
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof ImpresionOtroSiForm) 
		{
			ImpresionOtroSiForm forma = (ImpresionOtroSiForm) form;
			logger.info("\n\n************************************************************************");
			logger.info("               PRESUPUESTO ODONTOLOGICO ------>"+forma.getEstado());
			logger.info("************************************************************************\n\n");
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			
			
			if (forma.getEstado().equals("empezar"))
			{
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("imprimir"))
			{
				return this.accionImprimir(request, usuario, mapping);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param request
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionImprimir(HttpServletRequest request, UsuarioBasico usuario, ActionMapping mapping) {

		String nombreRptDesign="OtroSi.rptdesign";
	 		
	 	InstitucionBasica ins = (InstitucionBasica) request.getSession().getAttribute("institucionBasica");
		
		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "odontologia/", nombreRptDesign);

		//TODO COLOCAR LA INFO DEL ENCABEZADO
		
		//comp.insertImageBodyPage(0, 1, usuario.getPathFirmaDigital(), "firmaMedico");
		
		//DEBEMOS CARGAR LAS FIRMAS
		comp.insertGridInBodyPageWithName(1, 0, 2, 1, "grillaFirmas");
		
		//comp.insertLabelInGridOfBodyPage(posRow, posCell, vDataLabels)
		
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		
		comp.updateJDBCParameters(newPathReport);
		
		//TODO PASARLE EL PARAMETRO DEL PRESUPUESTO
		newPathReport += 	"&presupuesto=750";
							

		if (!newPathReport.equals("")) {
			
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}
		
		return mapping.findForward("principal");
	
	}	

}
