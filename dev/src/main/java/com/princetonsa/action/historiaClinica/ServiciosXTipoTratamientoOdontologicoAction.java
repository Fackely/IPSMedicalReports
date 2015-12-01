package com.princetonsa.action.historiaClinica;

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
import util.UtilidadSesion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.historiaClinica.ServiciosXTipoTratamientoOdontologicoForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ServiciosXTipoTratamientoOdontologico;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class ServiciosXTipoTratamientoOdontologicoAction extends Action
{
	/**
	 * Para manjar los logger de la clase ServiciosXTipoTratamientoOdontologicoAction
	 */
	Logger logger = Logger.getLogger(ServiciosXTipoTratamientoOdontologicoAction.class);
	

	
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute (ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws Exception
	{
		
		Connection connection = null;
		try {
		if (form instanceof ServiciosXTipoTratamientoOdontologicoForm) 
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
			ServiciosXTipoTratamientoOdontologicoForm forma = (ServiciosXTipoTratamientoOdontologicoForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			// se instancia el mundo
			ServiciosXTipoTratamientoOdontologico mundo = new ServiciosXTipoTratamientoOdontologico();
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	           EL ESTADO DE ServiciosXTipoTratamientoOdontologicoForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
		
			/*-------------------------------------------------------------------
			 * 		ESTADOS PARA SERVICIOS X TIPO TRATAMIENTO ODONTOLOGICO
			 -------------------------------------------------------------------*/
			
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			
			if (estado == null)
			{
				
				//se saca un log con el siguiente texto.
				logger.warn("Estado no Valido dentro del Flujo de Servicios X Tipo Tratamiento Odontologico (null)");
				//se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				//se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);
				
				//se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			}
			else/*----------------------------------------------
				 * ESTADO =================>>>>>  EMPEZAR
				 ---------------------------------------------*/
				if (estado.equals("empezar"))
				{
					//se limpia la forma
					forma.reset();
					//se cargan los tipos de tratamientos odonologicos
					mundo.empezar(connection, forma, usuario);
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);
					//se retorna el error al forward paginaError.
					return mapping.findForward("principal");
				}
				else/*----------------------------------------------
					 * ESTADO =================>>>>>  BUSCAR
					 ---------------------------------------------*/
					if (estado.equals("buscar"))
					{
						
						//se buscan los datos ya parametrizados
						mundo.buscar(connection, forma, usuario);
						//se cierra la conexion con la BD
						UtilidadBD.cerrarConexion(connection);
						//se retorna el error al forward paginaError.
						return mapping.findForward("principal");
					}
					else/*----------------------------------------------
						 * ESTADO =================>>>>>  NUEVO
						 ---------------------------------------------*/
						if (estado.equals("nuevo"))
						{
							mundo.nuevo(forma, usuario);
							//se cierra la conexion con la BD
							UtilidadBD.cerrarConexion(connection);
							//se retorna el error al forward paginaError.
							return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getServXTipTratOdont("numRegistros").toString()), response, request, "serviciosXTipoTratamientoOdontologico.jsp", true);
						}
						else/*----------------------------------------------
							 * ESTADO =================>>>>>  GUARDAR
							 ---------------------------------------------*/
							if (estado.equals("guardar"))
							{
								mundo.guardar(connection, forma, usuario);
								//se cierra la conexion con la BD
								UtilidadBD.cerrarConexion(connection);
								//se retorna el error al forward paginaError.
								return mapping.findForward("principal");
							}
							else/*----------------------------------------------
								 * ESTADO =================>>>>>  ELIMINAR
								 ---------------------------------------------*/
								if (estado.equals("eliminar"))
								{
									
									//se cierra la conexion con la BD
									UtilidadBD.cerrarConexion(connection);
									//se retorna el error al forward paginaError.
									return mundo.accionEliminarCampo(forma, request, response, usuario.getCodigoInstitucionInt());
								}
								else /*----------------------------
									 * 	ESTADO ==> REDIRECCION
									 ----------------------------*/
									if (estado.equals("redireccion"))
									{
										
										UtilidadBD.closeConnection(connection);
										response.sendRedirect(forma.getLinkSiguiente());
										return null;
									}
			
			
			
			/*-------------------------------------------------------------------
			 * 		FIN ESTADOS PARA SERVICIOS X TIPO TRATAMIENTO ODONTOLOGICO
			 -------------------------------------------------------------------*/
			
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