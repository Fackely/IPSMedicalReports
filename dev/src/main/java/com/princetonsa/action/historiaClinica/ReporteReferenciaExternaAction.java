package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;

import com.princetonsa.actionform.historiaClinica.ReporteReferenciaExternaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ReporteReferenciaExterna;

/**
 * Anexo 679
 * Creado el 12 de Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class ReporteReferenciaExternaAction extends Action
{
	/**
	 * Loggers de la clase ReporteReferenciaExternaAction
	 * 	 */
	Logger logger = Logger.getLogger(ReporteReferenciaExternaAction.class);
	
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute (ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)throws Exception
	{
		Connection connection = null;
		try {
		if (form instanceof ReporteReferenciaExternaForm ) 
		 {
			connection = UtilidadBD.abrirConexion();
			
			//Verifica si la conexion esta nula
			if (connection == null)
			{
				// de ser asi se envia a una pagina de error. 
				request.setAttribute("CodigoDescripcionError","erros.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			//Se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			//Se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//Obtenemos el valor de la forma.
			ReporteReferenciaExternaForm forma = (ReporteReferenciaExternaForm) form;
			
			//Obtenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			//se obtiene la institucion
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			ReporteReferenciaExterna mundo= new ReporteReferenciaExterna();
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	 EL ESTADO DE ReporteReferenciaExternaForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
		
			/*------------------------------
			 * ESTADO ----> EMPEZAR
			 -------------------------------*/
		 if (estado.equals("empezar"))
		 {
			forma.reset();
			forma.resetMensaje();
			forma.setOperacionTrue(false);
			mundo.empezar(connection, forma, usuario);
			UtilidadBD.cerrarConexion(connection);
			return mapping.findForward("principal");
		 }
		 else
				/*------------------------------
				 * ESTADO ----> cargarInsSirc
				 -------------------------------*/
			 if (estado.equals("cargarInsSirc"))
			 {
				
				mundo.cargarInstitucionesSirc(connection, forma, usuario);
				UtilidadBD.cerrarConexion(connection);
				return mapping.findForward("principal");
			 }
			 else
				 /*------------------------------
					 * ESTADO ----> generar
					 -------------------------------*/
			 if(estado.equals("generar"))
			 {
				 forma.setOperacionTrue(false);
				 HashMap tmp = new HashMap();
				 if (UtilidadCadena.noEsVacio(forma.getCriterios("estadoReferencia")+"") && !(forma.getCriterios("estadoReferencia")).equals(ConstantesBD.codigoNuncaValido+""))
				 {
					logger.info("\n El usuario seleccionó un estado !!! --->>>"+forma.getCriterios("estadoReferencia")+"");
					tmp.putAll(mundo.consultaReporteReferenciaExternaConEstado(connection, forma.getCriterios()));
				 }
				 else
				 {
					logger.info("\n El usuario NO seleccionó un estado !!! --->>>"+forma.getCriterios("estadoReferencia")+"");
					tmp.putAll(mundo.consultaReporteReferenciaExterna(connection, forma.getCriterios()));
				 }
				 //tmp.putAll(mundo.consultaReporteReferenciaExterna(connection, forma.getCriterios()));
				 if((tmp.get("numRegistros")+"").equals("0"))
				 {
					 logger.info("***** Num Registro es = a:"+tmp.get("numRegistros")+"");
					 forma.setMensaje(true);
					 return mapping.findForward("principal");
				 }
				 else
				 {
					 forma.resetMensaje();
					 mundo.generar(connection, forma, usuario, request, mapping, institucion);
					 UtilidadBD.cerrarConexion(connection);
					 return mapping.findForward("principal");
				 }
			 }
		 }
		
		return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
	}
}