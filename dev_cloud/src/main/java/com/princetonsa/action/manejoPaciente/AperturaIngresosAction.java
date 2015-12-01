package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.AperturaIngresosForm;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.AperturaIngresos;





/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class AperturaIngresosAction extends Action
{
	/**
	 * Para manjar los logger de la clase CensoCamasAction
	 */
	Logger logger = Logger.getLogger(AperturaIngresosAction.class);
	

	
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
			if (form instanceof AperturaIngresosForm) 
			{


				connection = UtilidadBD.abrirConexion();
				ActionErrors errores = new ActionErrors();
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
				AperturaIngresosForm forma = (AperturaIngresosForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia el mundo
				AperturaIngresos mundo = new AperturaIngresos();

				//manejo de indieces
				String [] indices = mundo.indicesListado;
				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE AperturaIngresosForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");


				/*-------------------------------------------------------------------
				 * 					ESTADOS PARA APERTURA INGRESOS
			 -------------------------------------------------------------------*/

				/**
				 *  Validacion Cargado Paciente
				 */
				RespuestaValidacion resp = esValidoPacienteCargado(connection, paciente);
				if(!resp.puedoSeguir)
				{
					return ComunAction.accionSalirCasoError(mapping, request, connection, logger, resp.textoRespuesta, resp.textoRespuesta, true);
				}
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
						forma.setListadoIngresos(mundo.colocarLink(connection,mundo.cargarListadoIngresos(connection, paciente.getCodigoPersona()+"",ConstantesBD.codigoNuncaValido+""),usuario));
						return mapping.findForward("listado");
					}
					else
						/*----------------------------------------------
						 * ESTADO =================>>>>>  CARGARINGRESO
					 ---------------------------------------------*/

						if (estado.equals("cargarIngreso"))
						{


							forma.setIngreso(mundo.cargarListadoIngresos(connection, paciente.getCodigoPersona()+"", forma.getListadoIngresos(indices[6]+forma.getIndex())+""));
							///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
							//evalua si el ingreso a abrir es un reingreso y que el ingreso que aparece en el campo reingreso este abierto
							if (Utilidades.convertirAEntero(forma.getListadoIngresos(indices[18]+"0")+"")>0)
								if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(connection, Utilidades.convertirAEntero(forma.getListadoIngresos(indices[18]+"0")+"")).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
									forma.setListadoIngresos(indices[18]+"0",ConstantesBD.codigoNuncaValido);
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							////////////////////////////////////////////////////////////////////////////////////////
							//se valida que no tenga ingresos abiertos en ningun centro de costo 
							///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

							logger.info("\n el valor del reingreso es -> "+forma.getListadoIngresos(indices[18]+"0"));

							if ((forma.getListadoIngresos(indices[18]+"0")+"").equals(ConstantesBD.codigoNuncaValido+""))	
							{
								RespuestaValidacion respuesta=UtilidadValidacion.validacionIngresarIngreso(connection, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona(), usuario.getCodigoInstitucion());
								logger.info(" puedo segir -->"+respuesta.puedoSeguir);
								if(respuesta.puedoSeguir)
								{	
									forma.setMotivoCierre(Utilidades.obtenerMotivosAperturaCierre(connection, ConstantesIntegridadDominio.acronimoAperturaIngreso));
									return mapping.findForward("principal");
								}
								else
								{
									errores.add("descripcion",new ActionMessage("prompt.generico",respuesta.textoRespuesta));
									saveErrors(request, errores);
									return mapping.findForward("listado");
								}
							}
							else
							{
								forma.setMotivoCierre(Utilidades.obtenerMotivosAperturaCierre(connection, ConstantesIntegridadDominio.acronimoAperturaIngreso));
								return mapping.findForward("principal");
							}

						}
						else
							/*----------------------------------------------
							 * ESTADO =================>>>>>  GUARDAR
						 ---------------------------------------------*/

							if (estado.equals("guardar"))
							{

								mundo.guardar(connection, forma, mapping,usuario);
								setObservable(paciente, request, true);
								return mapping.findForward("principal");
							}
							else
								/*----------------------------------------------
								 * ESTADO =================>>>>>  GUARDAR
							 ---------------------------------------------*/

								if (estado.equals("abrirReingreso"))
								{
									mundo.abrirReingreso(connection, forma, mapping, usuario);
									setObservable(paciente, request, true);
									return mapping.findForward("principal");
								}
								else/*----------------------------------------------
								 * ESTADO =================>>>>>  ORDENAR
									 ---------------------------------------------*/
									if (estado.equals("ordenar"))//estado encargado de ordenar el HashMap del censo.
									{
										forma.setListadoIngresos(mundo.accionOrdenarMapa(forma.getListadoIngresos(), forma));
										UtilidadBD.closeConnection(connection);
										return mapping.findForward("listado");	

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
	
	public static RespuestaValidacion esValidoPacienteCargado(Connection con,PersonaBasica paciente)
	{
		
		
		if(paciente.getCodigoPersona()<1)
		{		    
		    return new RespuestaValidacion("errors.paciente.noCargado",false);
		}
		
		
		return new RespuestaValidacion("",true);		
	}
	
	
	/**
	 * Método para hacer que el paciente
	 * pueda ser visto por todos los usuario en la aplicacion
	 * @param paciente
	 */
	private void setObservable(PersonaBasica paciente, HttpServletRequest request, boolean cargarPaciente)
	{
		if(cargarPaciente)
		{	
			/**para cargar el paciente que corresponda**/
			Connection con=UtilidadBD.abrirConexion();
			try {
				paciente.cargar(con,paciente.getCodigoPersona());
			} catch (SQLException e) {
				// @TODO Auto-generated catch block
				e.printStackTrace();
			}
			UtilidadBD.closeConnection(con);
		}	
		
		//Código necesario para registrar este paciente como Observer
		ObservableBD observable = (ObservableBD) getServlet().getServletContext().getAttribute("observable");
		if (observable != null) 
		{
			paciente.setObservable(observable);
			// Si ya lo habíamos añadido, la siguiente línea no hace nada
			observable.addObserver(paciente);
		}
		//Se sube a sesión el paciente activo
		request.getSession().setAttribute("pacienteActivo", paciente);
		
	    if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
	
}