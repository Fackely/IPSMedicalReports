/*
 * Sep 20, 2005
 *
 * 
 */
package com.princetonsa.action.salasCirugia;

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
import com.princetonsa.actionform.salasCirugia.ExcepcionAsocioTipoSalaForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.ExcepcionAsocioTipoSala;

/**
 * @author Sebastián Gómez R
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de ExcepcionAsocioTipoSala
 */
public class ExcepcionAsocioTipoSalaAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ExcepcionAsocioTipoSalaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{

		Connection connection = null;
		try{
			if(form instanceof ExcepcionAsocioTipoSalaForm)
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
				ExcepcionAsocioTipoSalaForm forma = (ExcepcionAsocioTipoSalaForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE ExcepcionesTipoSala ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				/*----------------------------------
				 * 			ESTADO ==> NULL
			 ---------------------------------*/
				if(estado == null)
				{
					forma.reset();
					forma.resetBusqueda();
					forma.resetPager();
					logger.warn("Estado no valido dentro del flujo de Excepciones Asocio Tipo Sala (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("paginaError");
				}
				//*******ESTADOS DE INGRESAR/MODIFICAR EXCEPCIONES ASOCIOS**********************
				else /*------------------------------
				 * 		ESTADO ==> EMPEZAR
				  -------------------------------*/
					if (estado.equals("empezar"))
					{
						return ExcepcionAsocioTipoSala.accionEmpezar(connection,forma,mapping,usuario);
					}
					else /*------------------------------
					 * 		ESTADO ==> BUSCAR
					  -------------------------------*/
						if (estado.equals("buscar"))
						{

							return ExcepcionAsocioTipoSala.accionBuscar(connection, forma, mapping,usuario);
						}
						else /*------------------------------
						 * 		ESTADO ==> NUEVO
						  -------------------------------*/
							if (estado.equals("nuevo"))
							{
								return ExcepcionAsocioTipoSala.accionNuevo(forma, usuario, connection, mapping, request, response, usuario.getCodigoInstitucionInt());
							}
							else /*------------------------------
							 * 		ESTADO ==> GUARDAR
						  -------------------------------*/
								if (estado.equals("guardar"))
								{

									return ExcepcionAsocioTipoSala.accionGuardarRegistros(connection, forma, mapping, usuario);

								}
								else/*------------------------------
								 * 		ESTADO ==> ELIMINAR
						  	 -------------------------------*/
									if(estado.equals("eliminar"))
									{
										return ExcepcionAsocioTipoSala.accionEliminarCampo(forma, request, response, usuario.getCodigoInstitucionInt(),mapping);
									}
									else/*------------------------------
									 * 		ESTADO ==> REDIRRECCION
						  		 -------------------------------*/
										if (estado.equals("redireccion"))
										{
											return ExcepcionAsocioTipoSala.accionRedireccion(connection, forma, response);
										}
										else/*------------------------------
										 * 		ESTADO ==> ORDENAR
							  		 -------------------------------*/
											if(estado.equals("ordenar"))
											{

												return ExcepcionAsocioTipoSala.accionOrdenarMapa(connection, forma, mapping);
											}




				//*****ESTADOS DE CONSULTAR EXCEPCIONES ASOCIOS*************************************
											else/*------------------------------
											 * 		ESTADO ==> CONSULTAR
				  		 -------------------------------*/
												if(estado.equals("consultar"))
												{
													return ExcepcionAsocioTipoSala.accionEmpezarConsulta(connection, forma, mapping, usuario);
												}
												else/*------------------------------
												 * 		ESTADO ==> BUSQUEDAAVANZADA
				  		 -------------------------------*/
													if(estado.equals("busquedaavanzada"))
													{
														return ExcepcionAsocioTipoSala.accionBuscar(connection, forma, mapping, usuario);
													}
													else
													{

														logger.warn("Estado no valido dentro del flujo de Excepciones Asocio Tipo Sala (null) ");
														request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
														UtilidadBD.cerrarConexion(connection);
														return mapping.findForward("paginaError");
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
