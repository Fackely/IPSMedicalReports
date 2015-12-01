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
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.LiberacionCamasIngresosCerradosForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.LiberacionCamasIngresosCerrados;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class LiberacionCamasIngresosCerradosAction extends Action
{
	/**
	 * Para manjar los logger de la clase LiberacionCamasIngresosCerradosAction
	 */
	Logger logger = Logger.getLogger(LiberacionCamasIngresosCerradosAction.class);
	

	
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
			if (form instanceof LiberacionCamasIngresosCerradosForm) 
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
				LiberacionCamasIngresosCerradosForm forma = (LiberacionCamasIngresosCerradosForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia el mundo
				LiberacionCamasIngresosCerrados mundo = new LiberacionCamasIngresosCerrados();

				//manejo de indieces
				String [] indicesListado = mundo.indicesListado;

				logger.info("\n\n***************************************************************************");
				logger.info("EL ESTADO DE LiberacionCamasIngresosCerradosForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");


				/*-------------------------------------------------------------------
				 * 					ESTADOS PARA APERTURA INGRESOS
			 -------------------------------------------------------------------*/


				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
				if (estado == null)
				{

					//se saca un log con el siguiente texto.
					logger.warn("Estado no Valido dentro del Flujo de apertura ingresos (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);

					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				else
					/*----------------------------------------------
					 * ESTADO =================>>>>>  EMPEZAR
				 ---------------------------------------------*/

					if (estado.equals("empezar"))
					{

						forma.reset();
						forma.setListadoIngresos(mundo.consultaIngresos(connection, usuario.getCodigoCentroAtencion()+""));

						if (Utilidades.convertirAEntero(forma.getListadoIngresos("numRegistros")+"")==1)
						{
							forma.setIndex("0");
							forma.setEstado("detalle");
							forma.setDetalle(mundo.consultaDetalle(connection, usuario.getCodigoCentroAtencion()+"",forma.getListadoIngresos(indicesListado[1]+forma.getIndex())+""));
							UtilidadBD.cerrarConexion(connection);
							return mapping.findForward("principal");

						}
						else
						{
							UtilidadBD.cerrarConexion(connection);
							return mapping.findForward("listado");
						}

					}
					else
						/*----------------------------------------------
						 * ESTADO =================>>>>>  DETALLE
					 ---------------------------------------------*/

						if (estado.equals("detalle"))
						{						
							forma.setDetalle(mundo.consultaDetalle(connection, usuario.getCodigoCentroAtencion()+"",forma.getListadoIngresos(indicesListado[1]+forma.getIndex())+""));
							UtilidadBD.cerrarConexion(connection);
							return mapping.findForward("principal");
						}
						else
							/*----------------------------------------------
							 * ESTADO =================>>>>>  GUARDAR
						 ---------------------------------------------*/

							if (estado.equals("guardar"))
							{						
								mundo.guardar(connection, forma, usuario);
								UtilidadBD.cerrarConexion(connection);
								return mapping.findForward("principal");
							}
							else/*------------------------------
							 * 		ESTADO ==> REDIRRECCION
					  		 -------------------------------*/
								if (estado.equals("redireccion"))
								{
									UtilidadBD.cerrarConexion(connection);
									response.sendRedirect(forma.getLinkSiguiente());
									return null;
								}
								else/*------------------------------
								 * 		ESTADO ==> ORDENAR
						  		 -------------------------------*/
									if(estado.equals("ordenar"))
									{

										return mundo.accionOrdenarMapa(connection, forma, mapping);
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