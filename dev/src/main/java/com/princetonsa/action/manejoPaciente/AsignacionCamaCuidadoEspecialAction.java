package com.princetonsa.action.manejoPaciente;


import java.sql.Connection;
import java.util.HashMap;

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

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.AsignacionCamaCuidadoEspecialForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Evoluciones;
import com.princetonsa.mundo.manejoPaciente.AsignacionCamaCuidadoEspecial;


/**
 *@author Luis Gabriel Chavez S.
 *lgchavez@princetonsa.com 
 */
 
public class AsignacionCamaCuidadoEspecialAction extends Action
{
	/**
	 * Para manjar los logger de la clase AsignacionCamaCuidadoEspecialAction
	 */
	Logger logger = Logger.getLogger(AsignacionCamaCuidadoEspecialAction.class);
	

	
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
		if (form instanceof AsignacionCamaCuidadoEspecialForm) 
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
			AsignacionCamaCuidadoEspecialForm forma = (AsignacionCamaCuidadoEspecialForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			// se instancia el mmundo
			AsignacionCamaCuidadoEspecial mundo = new AsignacionCamaCuidadoEspecial();
			
			
			ActionErrors errores = new ActionErrors();
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	 EL ESTADO DE AsignacionCamaCuidadoEspecialForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			
			/*-------------------------------------------------------------------
			 * 					ESTADOS PARA EL CENSO DE CAMAS
			 -------------------------------------------------------------------*/
			
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			
			if (estado == null)
			{
				//se saca un log con el siguiente texto.
				logger.warn("Estado no Valido dentro del Flujo de Censo de Camas (null)");
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
					forma.setTipoMonitoreo(UtilidadesManejoPaciente.obtenerTiposMonitoreo(connection, usuario.getCodigoInstitucionInt()));
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
			else
				/*----------------------------------------------
				 * ESTADO =================>>>>>  listarPacientes
				 ---------------------------------------------*/
				
				if (estado.equals("listarPacientes"))
				{	
					forma.setListadoPacientes(mundo.asignacionCamaCuidadoEspecialDao().consultaPacientes(connection, Integer.parseInt(forma.getMonitoreo())));
					Utilidades.imprimirMapa(forma.getListadoPacientes());
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
			else
				/*----------------------------------------------
				 * ESTADO =================>>>>>  ingresar
				 ---------------------------------------------*/
				
				if (estado.equals("ingresar"))
				{	
					logger.info("Entro [Detalle Ingresar]");
					forma.setOperacionTrue(false);
					mundo.cargarDetalle(connection, forma, usuario);
					Utilidades.imprimirMapa(forma.getDetalleIngresar());
					int	codpaciente=Utilidades.convertirAEntero(forma.getDetalleIngresar().get("codigoPaciente_0")+"");
	    			paciente.setCodigoPersona(codpaciente);
			        paciente.cargar(connection, codpaciente);
			        paciente.cargarPaciente(connection, codpaciente, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
			
					forma.setDetalleIngresar("fechaHoraOrden", forma.getListadoPacientes("fechaHoraOrden_"+forma.getIndex()));
					if (forma.getCCosto().size()<1)
					{
						//Hacer Error de que no se puede ejecutar el proceso porque no hay centros de costo parametrizados 
						//para la via de ingreso Hospitalizzacion tipo paciente Hospitalizado Centro de Atencion XXXXX tipo monitoreo XXXX
						logger.info("\n\n [ERROR NO HAY CENTROS COSTO QUE CUMPLAN LAS VALIDACIONES]");
						errores.add("motivo", new ActionMessage("error.manejoPaciente.problemasCentrosCosto",
								"Hospitalizacion","Hospitalizado",usuario.getCentroAtencion()+"",forma.getListadoPacientes("tipoMonitoreo_"+forma.getIndex())+""));
					}
					if (!errores.isEmpty())
						{
						saveErrors(request, errores);
						forma.reset();
						forma.setTipoMonitoreo(UtilidadesManejoPaciente.obtenerTiposMonitoreo(connection, usuario.getCodigoInstitucionInt()));
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("principal");
						}
					
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("detalle");
				}
			else
				/*----------------------------------------------
				 * ESTADO =================>>>>>  guardar
				 ---------------------------------------------*/
				
				if (estado.equals("guardar"))
				{	
					logger.info("Entro [guardar]");
					int error=0;
					//crea el registro de ingreso con estado activo e indicativo manual al area de cuidados intensivos
					HashMap datos = new HashMap();
					Evoluciones evo = new Evoluciones();
					datos.put("ingreso", forma.getListadoPacientes("idingreso_"+forma.getIndex()));
					datos.put("monitoreo", forma.getMonitoreo());
					datos.put("usuarior", usuario.getLoginUsuario());
					datos.put("fechar", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().toString()));
					datos.put("horar", UtilidadFecha.getHoraActual());
					datos.put("usuariom", usuario.getLoginUsuario());
					//datos.put("evoorden", forma.getListadoPacientes("evolucion_"+forma.getIndex()));
					datos.put("evoorden", evo.obtenerCodigoUltimaEvolucion(connection, Utilidades.convertirAEntero(forma.getDetalleIngresar("cuenta_0")+"")));
					//datos.put("valoorden", "");
					//datos.put("evoorden", "");
					//datos.put("valoracion", "");
					datos.put("cCosto", forma.getDetalleIngresar("cCosto"));
					logger.info("[MAPA INSERTAR]");
					Utilidades.imprimirMapa(datos);
					
					UtilidadBD.iniciarTransaccion(connection);
					logger.info("\n\n\n\n [ INICIO TRANSACCION ] \n\n\n\n ");
					
					error=mundo.guardarIngresoCuidadosEspeciales(connection, datos);
					
					if (error>0){
					
					//Asignar el centro de costo nuevo para el paciente que viene del centro de osto asociado al tipo de monitoreo.
						if (Utilidades.actualizarAreaCuenta(connection, Utilidades.convertirAEntero(forma.getListadoPacientes("idCuenta_"+forma.getIndex()).toString()), Utilidades.convertirAEntero(forma.getDetalleIngresar("cCosto").toString()))){
							//Hacer el traslado de cama	
							errores=mundo.trasladoPaciente(forma, connection, paciente, usuario);
							}
						else{
							logger.info("\n\n [ERROR MODIFICACION DE AREA EN LA CUENTA]");
							errores.add("[ERROR MODIFICACION DE AREA EN LA CUENTA]", new ActionMessage("error.manejoPaciente.problemasIngresar"," la modificación del area de la cuenta"));
						}
					
					}else
					{
						logger.info("\n\n [ERROR REGISTRO INGRESO CUIDADO ESPECIAL]");
						errores.add("[ERROR REGISTRO INGRESO CUIDADO ESPECIAL]", new ActionMessage("error.manejoPaciente.problemasIngresar"," el registro de ingreso cuidado especial "));
					}

					if (!errores.isEmpty())
						{
			            UtilidadBD.abortarTransaccion(connection);
			            logger.info("\n\n\n\n [ ERROR ABORTANDO TRANSACCION ] \n\n\n\n ");
						saveErrors(request, errores);
						}
					else
					{
						int	codpaciente=Utilidades.convertirAEntero(forma.getDetalleIngresar().get("codigoPaciente_0")+"");
		    			paciente.setCodigoPersona(codpaciente);
				        paciente.cargar(connection, codpaciente);
				        paciente.cargarPaciente(connection, codpaciente, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
						Utilidades.imprimirMapa(forma.getDetalleIngresar());
				        
				        UtilidadBD.finalizarTransaccion(connection);
						forma.setOperacionTrue(true);
						logger.info("\n\n\n\n [ TRANSACCION FINALIZADA 100% ] \n\n\n\n ");
					}
					return mapping.findForward("detalle");
				}
    		else
    			/*----------------------------------------------
				 * ESTADO =================>>>>>  ordenar
				 ---------------------------------------------*/
				if (estado.equals("ordenar"))
				{
					accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("principal");
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



	private void accionOrdenarMapa(AsignacionCamaCuidadoEspecialForm forma) {

		Utilidades.imprimirMapa(forma.getListadoPacientes());
		
		String [] indicesMap = {
			   					"identPac_",	
			   					"fechaHoraOrden_",
								"fechaNacimiento_",
								"convenio_",
								"paciente_",
								"cama_" ,
								"sexoPac_",
								"idCuenta_",
								"centroCosto_",
								"diagnosticoPpal_",
								"idingreso_",
								"ingreso_",
								"tipoMonitoreo_",
								"profesional_"
								};

		int numReg = Integer.parseInt(forma.getListadoPacientes().get("numRegistros").toString());
		forma.setListadoPacientes(Listado.ordenarMapa(indicesMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoPacientes(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.getListadoPacientes().put("numRegistros",numReg);
		forma.getListadoPacientes().put("INDICES_MAPA",indicesMap);
		
	}
}