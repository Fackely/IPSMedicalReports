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

import util.UtilidadBD;

import com.princetonsa.actionform.historiaClinica.ReporteReferenciaInternaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ReporteReferenciaInterna;

/**
 * Anexo 678
 * Creado el 2 de Octubre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class ReporteReferenciaInternaAction extends Action
{
	/**
	 * Loggers de la clase ReporteReferenciaInternaAction
	 */
	Logger logger = Logger.getLogger(ReporteReferenciaInternaAction.class);
	
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
			if (form instanceof ReporteReferenciaInternaForm ) 
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
				ReporteReferenciaInternaForm forma = (ReporteReferenciaInternaForm) form;

				//Obtenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				ReporteReferenciaInterna mundo= new ReporteReferenciaInterna();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	 EL ESTADO DE ReporteReferenciaInternaForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				/*------------------------------
				 * ESTADO ----> EMPEZAR
			 -------------------------------*/
				if (estado.equals("empezar"))
				{
					forma.reset();
					forma.setOperacionTrue(false);
					mundo.empezar(connection, forma, usuario);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}

				/*------------------------------
				 * ESTADO ----> cargarInsSirc
				 * -------------------------------*/
				else if (estado.equals("cargarInsSirc"))
				{
					forma.setOperacionTrue(false);
					mundo.cargarInstitucionesSirc(connection, forma, usuario);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}

				/*------------------------------
				 * ESTADO ----> generar
			 -------------------------------*/
				else if(estado.equals("generar"))
				{
					forma.setOperacionTrue(false);
					HashMap tmp = new HashMap();
					tmp.putAll(mundo.consultaReporteReferenciaInterna(connection, forma.getCriterios()));
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