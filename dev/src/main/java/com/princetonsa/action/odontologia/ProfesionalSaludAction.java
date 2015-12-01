/**
 * @autor Jorge Armando Agudelo Quintero
 */
package com.princetonsa.action.odontologia;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.actionform.odontologia.ProfesionalSaludForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author axioma
 *
 */
public class ProfesionalSaludAction extends Action{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) {

		if(form instanceof ProfesionalSaludForm){
			
			ActionErrors errores=new ActionErrors();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			Medico medico= (Medico) request.getSession().getAttribute("medico");
			
			if(medico!=null){
				
				Log4JManager.info("---------------------------------------------------------------------------------- " +
						"medico en sesion " + medico.getApellidos());
			}
			
			ProfesionalSaludForm forma=(ProfesionalSaludForm) form;
			
			InstitucionBasica institucionBasica=(InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			String estado=forma.getEstado();
			
			Log4JManager.info("Estado en profesional action: "+estado);
		
			if(estado.equals("guardarProfesional")){

				UtilidadTransaccion.getTransaccion().begin();
				
				Log4JManager.info("---------------------------------------------------------------------------------- entre a guardar el profesional");
				
				return mapping.findForward("principal");
			
				
				
				
				
			}else{ //secci&oacute;n de errores
				
				errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
				errores.add("valor requerido", new ActionMessage("errors.usuarioSinRolFuncionalidad",usuario.getNombreUsuario(), "esta funcionalidad"));
				saveErrors(request, errores);
			}
		}
		
		return mapping.findForward("paginaError");
	}

}
