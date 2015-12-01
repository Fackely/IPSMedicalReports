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

import util.ResultadoBoolean;
import util.UtilidadBD;

import com.princetonsa.actionform.manejoPaciente.ParametrosEntidadesSubContratadasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ParametrosEntidadesSubContratadas;





public class ParametrosEntidadesSubContratadasAction extends Action
{
	/**
	 * Para manjar los logger de la clase ParametrosEntidadesSubContratadasAction
	 */
	Logger logger = Logger.getLogger(ParametrosEntidadesSubContratadasAction.class);
	

	
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception 
{
		Connection connection = null;
		try{
		if (form instanceof ParametrosEntidadesSubContratadasForm) 
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
			ParametrosEntidadesSubContratadasForm forma = (ParametrosEntidadesSubContratadasForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			forma.setMensaje(new ResultadoBoolean(false));
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	           EL ESTADO DE ParametrosEntidadesSubContratadasForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			
			/*-------------------------------------------------------------------
			 * 		ESTADOS PARA EL PARAMETROS ENTIDADES SUBCONTRATADOS
			 -------------------------------------------------------------------*/
			
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			
			if (estado == null)
			{
				//se saca un log con el siguiente texto.
				logger.warn("Estado no Valido dentro del Flujo de parametros entidades subcontratadas (null)");
				//se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				//se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);
				
				//se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			}
			
			else/*----------------------------------------------
				 * ESTADO =================>>>>>  INICIAL
				 ---------------------------------------------*/
				if (estado.equals("inicial"))
				{ 
					return ParametrosEntidadesSubContratadas.initParametros(connection, forma, mapping);
				}
				else/*----------------------------------------------
					 * ESTADO =================>>>>>  CARGARSECCION
					 ---------------------------------------------*/
					if (estado.equals("cargarSeccion"))
					{ 
						return ParametrosEntidadesSubContratadas.cargarSeccion(connection, forma, mapping, usuario);
					}
					else
						/*----------------------------------------------
						 * ESTADO =================>>>>>  CARGARSUBSECCIONVIAINGRESO
						 ---------------------------------------------*/
						if (estado.equals("cargarSubSeccionViaingreso"))
						{ 
							return ParametrosEntidadesSubContratadas.cargarSubSeccionViasIngreso(connection, forma, mapping, usuario);
						}
						else
							/*----------------------------------------------
							 * ESTADO =================>>>>>  CARGARVALORESSECCION
							 ---------------------------------------------*/
							if (estado.equals("cargarValoresSeccion"))
							{ 
								return ParametrosEntidadesSubContratadas.cargarValoresSeccion(connection, forma, usuario,mapping);
							}
							else
								/*----------------------------------------------
								 * ESTADO =================>>>>>  GUARDARSINVIAINGRESO
								 ---------------------------------------------*/
								if (estado.equals("guardar"))
								{ 
									return ParametrosEntidadesSubContratadas.accionGuardar(connection, forma, usuario, mapping);
								}
								else
									/*----------------------------------------------
									 * ESTADO =================>>>>>  GUARDARSINVIAINGRESO
									 ---------------------------------------------*/
									if (estado.equals("cargarSubSeccionCentrosAtencion"))
									{ 
										return ParametrosEntidadesSubContratadas.cargarSubseccionCentrosAtencion(connection, forma, mapping, usuario);
									}
									else
										/*----------------------------------------------
										 * ESTADO =================>>>>>  buscarEspecialidadXProfesional
										 ---------------------------------------------*/
										if (estado.equals("buscarEspecialidadXProfesional"))
										{ 
											return ParametrosEntidadesSubContratadas.buscarEspecialidadXProfesional(connection, forma, mapping, usuario);
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
