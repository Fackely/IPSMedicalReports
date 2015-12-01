package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.actionform.manejoPaciente.OcupacionDiariaCamasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.OcupacionDiariaCamas;

/**
 * Fecha Enero - 2008
 * @author Jhony Alexander Duque A.
 *
 */

public class OcupacionDiariaCamasAction extends Action 
{

	/**
	 * Para manjar los logger de la clase OcupacionDiariaCamasAction
	 */
	Logger logger = Logger.getLogger(OcupacionDiariaCamasAction.class);
	

	
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute (ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws Exception
									{

		Connection connection = null;
		try{
			if (form instanceof OcupacionDiariaCamasForm) 
			{


				connection = UtilidadBD.abrirConexion();

				//se verifica si la conexion esta nula
				if (connection == null)
				{
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//optenemos el valor de la forma.
				OcupacionDiariaCamasForm forma = (OcupacionDiariaCamasForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia el mundo
				OcupacionDiariaCamas mundo = new OcupacionDiariaCamas();
				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE OcupacionDiariaCamasForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");


				/*-------------------------------------------------------------------
				 * 					ESTADOS PARA OCUPACION DIARIA DE CAMAS
			 -------------------------------------------------------------------*/


				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/

				if (estado == null)
				{

					//se formatean los valores de la forma
					forma.reset();
					//se saca un log con el siguiente texto.
					logger.warn("Estado no Valido dentro del Flujo de Censo de Camas (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);

					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				else/*----------------------------------------------
				 * ESTADO =================>>>>> EMPEZAR
				 ---------------------------------------------*/
					if (estado.equals("empezar"))
					{

						mundo.empezar(connection, forma, usuario);
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("principal");
					}
					else/*----------------------------------------------
					 * ESTADO =================>>>>> GENERAR
					 ---------------------------------------------*/
						if (estado.equals("generar"))
						{
							return mundo.generar(connection, usuario, forma, request, institucion,mapping);
						}

			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
}