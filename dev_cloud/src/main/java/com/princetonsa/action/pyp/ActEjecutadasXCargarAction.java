/*
 * Nov 14, 2006
 */
package com.princetonsa.action.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import util.Listado;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.pyp.ActEjecutadasXCargarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.ActEjecutadasXCargar;
/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Actividades PYP Ejecutadas por Cargar
 */
public class ActEjecutadasXCargarAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ActEjecutadasXCargarAction.class);
	
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

		Connection con=null;
		//SE ABRE CONEXION
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof ActEjecutadasXCargarForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				ActEjecutadasXCargarForm actividadesForm =(ActEjecutadasXCargarForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=actividadesForm.getEstado(); 
				logger.warn("\n\n En ActEjecutadasXCargarAction el Estado ["+estado+"] \n\n");

				if(estado == null)
				{
					actividadesForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Actividades PYP Ejecutadas X Cargar (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,actividadesForm,mapping,usuario);
				}
				//Estado que se carga cuando se selecciona un convenio
				else if (estado.equals("busquedaOrdenes"))
				{
					return accionBusquedaOrdenes(con,actividadesForm,mapping);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,actividadesForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,actividadesForm,response,mapping,request);
				}
				//Estado que carga las ordenes ambulatorias seleccionadas 
				else if (estado.equals("cargarGeneral"))
				{
					return accionCargarGeneral(con,actividadesForm,mapping,usuario,request);
				}
				//Estado que carga el contenido del archivo de inconsistencias
				else if (estado.equals("cargarArchivo"))
				{
					return accionCargarArchivo(con,actividadesForm,mapping);
				}
				//Estado que carga el detalle de una orden ambulatoria
				else if (estado.equals("detalle"))
				{
					return accionDetalle(con,actividadesForm,mapping);
				}
				//Estado que carga a la cuenta la orden ambulatoria del detalle
				else if (estado.equals("cargarDetalle"))
				{
					return accionCargarDetalle(con,actividadesForm,mapping,usuario,request);
				}
				//Estado que recarga el listado de las ordenes ambulatorias
				else if(estado.equals("recargar"))
				{
					if(actividadesForm.getCodigoConvenio()>0)
						actividadesForm.setSeleccion(actividadesForm.getCodigoConvenio()+ConstantesBD.separadorSplit+actividadesForm.getNombreConvenio());
					else
						actividadesForm.setSeleccion(actividadesForm.getNombreConvenio());

					//Se vuelve y consultan las ordenes
					return accionBusquedaOrdenes(con,actividadesForm,mapping);
				}
				else
				{
					actividadesForm.reset();
					logger.warn("Estado no valido dentro del flujo de ActEjecutadasXCargarAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}			
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
}

	
	/**
	 * Método implementado para cargar a la cuenta la orden ambulatoria del detalle
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarDetalle(Connection con, ActEjecutadasXCargarForm actividadesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//Objetos necesarios
		ActEjecutadasXCargar actEjecutadas = new ActEjecutadasXCargar();
		ActionErrors errores = new ActionErrors();
		String mensajes = "";
		int resp0 = 0;
		
		UtilidadBD.iniciarTransaccion(con);
		
		//Se genera la solicitud a partir de la solicitud de referencia
		resp0 = actEjecutadas.generarSolicitudXOrdenAmbulatoria(
			con,
			actividadesForm.getDetalle("idCuenta").toString(),
			actividadesForm.getDetalle(("numeroSolicitud")).toString(), //se envia la solicitud de referencia
			actividadesForm.getDetalle(),
			usuario);
		
		if(resp0>0)
			UtilidadBD.finalizarTransaccion(con);
		else
		{
			UtilidadBD.abortarTransaccion(con);
			actividadesForm.setEstado("detalle");
			
			//Se editan mensajes
			ArrayList erroresGeneracion = actEjecutadas.getErrores();
			for(int l=0;l<erroresGeneracion.size();l++)
			{
				if(!mensajes.equals(""))
					mensajes += ", ";
				mensajes += erroresGeneracion.get(l) + "";
			}
			
			errores.add("Error al generar la solicitud",new ActionMessage("error.pyp.actEjecutadasXCargar.procesoCargueDetalle",actividadesForm.getDetalle("orden"),mensajes));
			saveErrors(request,errores);
		}	
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}


	/**
	 * Método implementado para cargar el detalle de una orden ambulatoria
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ActEjecutadasXCargarForm actividadesForm, ActionMapping mapping) 
	{
		//Se instancia objeto ActEjecutadasXCargar
		ActEjecutadasXCargar actEjecutadas = new ActEjecutadasXCargar();
		int pos = actividadesForm.getPosicion();
		
		//Se consulta el detalle de la orden ambulatoria
		actividadesForm.setDetalle(actEjecutadas.consultarDetalleOrdenAmb(con,actividadesForm.getListadoOrdenes("codigo_orden_"+pos).toString()));
		
		//Se verifica si existe cuenta abierta
		String idCuenta = Utilidades.obtenerIdCuentaValidaIngresoAbiertoCerrado(con,actividadesForm.getListadoOrdenes("codigo_paciente_"+pos).toString());
		Cuenta cuenta = new Cuenta();
		logger.info("idCuenta encontrada=> "+idCuenta);
		if(!idCuenta.equals(""))
		{
			cuenta.cargar(con,idCuenta);
			
			actividadesForm.setMostrarBoton(false);
			//Se verifica si cumple con las condiciones PYP
			//1. La cuenta actual debe ser de CONSULTA EXTERNO
			//2. Debe tener el mismo convenio de la solicitud de referencia de la orden
			//3. El convenio debe ser PYP
			for(int i=0;i<cuenta.getCuenta().getConvenios().length;i++)
			{
				if(cuenta.getCuenta().getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna&&
						cuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()==Integer.parseInt(actividadesForm.getListadoOrdenes("codigo_convenio_"+pos).toString())&&
						UtilidadValidacion.esConvenioPYP(con,cuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+""))
				{
					actividadesForm.setMostrarBoton(true);
					//se asigna id de la cuenta
					actividadesForm.setDetalle("idCuenta",idCuenta);
					//se asigna la solicitud de referencia
					actividadesForm.setDetalle("numeroSolicitud",actividadesForm.getListadoOrdenes("numero_solicitud_"+pos).toString());
				}
			}
			
			
				
		}
		else
			//no se mostrará boton cargar
			actividadesForm.setMostrarBoton(false);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}


	/**
	 * Método implementado para cargar el contenido del archivo de inconsistencias
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarArchivo(Connection con, ActEjecutadasXCargarForm actividadesForm, ActionMapping mapping) 
	{
		//Se instancia ActEjecutadasXCargar
		ActEjecutadasXCargar actEjecutadas = new ActEjecutadasXCargar();
		
		//Se carga el archivo de inconsistencias
		actividadesForm.setArchivo(actEjecutadas.cargarArchivoInconsistencias(actividadesForm.getRutaArchivo()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("archivo");
	}


	/**
	 * Método que carga las ordenes ambulatorias chequeadas
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarGeneral(Connection con, ActEjecutadasXCargarForm actividadesForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//Atributos utilizados
		ArrayList errores = new ArrayList();
		ActEjecutadasXCargar actEjecutadas = new ActEjecutadasXCargar();
		actividadesForm.setRutaArchivo("");
		boolean errorConsecutivoIngreso = false;
		HashMap ultimaCuentaFacturadaPYP = new HashMap();
		
		//Recorrido del listado de ordenes ambulatorias
		for(int i=0;i<actividadesForm.getNumOrdenes();i++)
		{
			//Se verifica si la orden fue seleccionada
			if(UtilidadTexto.getBoolean(actividadesForm.getListadoOrdenes("seleccion_"+i).toString()))
			{
				//Se limpian errores dek objeto actEjecutadas
				actEjecutadas.setErrores(new ArrayList());
				
				//Se verifica si existe cuenta abierta
				String idCuenta = Utilidades.obtenerIdCuentaValidaIngresoAbiertoCerrado(con,actividadesForm.getListadoOrdenes("codigo_paciente_"+i).toString());
				Cuenta cuenta = new Cuenta();
				if(!idCuenta.equals(""))
					cuenta.cargar(con,idCuenta);
				else 
				{
					if(!UtilidadTexto.isEmpty(actividadesForm.getListadoOrdenes("id_cuenta_"+i)+""))
					{
						//Se carga la cuenta de la solicitud asociada para iniciar la creación
						cuenta.cargar(con,actividadesForm.getListadoOrdenes("id_cuenta_"+i).toString());
					}	
					else
					{
						// Este caso ocurre cuando la actividad PYP se ha ejecutado a un paciente con cuenta cerrada, y no tiene asociado como referencia una solicitud de consulta
						ultimaCuentaFacturadaPYP = UtilidadesFacturacion.obtenerInfoUltimaCuentaFacturada(con, Utilidades.convertirAEntero(actividadesForm.getListadoOrdenes("codigo_paciente_"+i)+""), true);
						cuenta.cargar(con,ultimaCuentaFacturadaPYP.get("cuenta").toString());
					}
				}
					
					
				logger.info("\n\n\nSE ENCONTRÓ CUENTA?     "+idCuenta+"\n\n\n\n");
				if(!idCuenta.equals(""))
				{
					//***********CASO CUENTA ABIERTA*******************************************************
					//1) Se verifica que la cuenta tenga el mismo convenio de la solicitud de referencia
					int codigoConvenio = 0;
					for(int j=0;j<cuenta.getCuenta().getConvenios().length;j++)
					{
						if(cuenta.getCuenta().getConvenios()[j].getConvenio().getCodigo()==Integer.parseInt(actividadesForm.getListadoOrdenes("codigo_convenio_"+i).toString()))
							codigoConvenio = cuenta.getCuenta().getConvenios()[j].getConvenio().getCodigo();
					}
					
					if(codigoConvenio>0)
					{
						
						//2) se verifica si el convenio es PYP
						if(UtilidadValidacion.esConvenioPYP(con,codigoConvenio+""))
						{
						
							//3) Se verifica que la cuenta sea de CONSULTA EXTERNA
							if(cuenta.getCuenta().getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
							{
								//4) Se carga el detalle de la orden ambulatoria
								HashMap detalle = actEjecutadas.consultarDetalleOrdenAmb(con,actividadesForm.getListadoOrdenes("codigo_orden_"+i).toString());
								int resp0 = 0;
								UtilidadBD.iniciarTransaccion(con);
								
								//5) Se genera la solicitud a partir de la solicitud de referencia
								resp0 = actEjecutadas.generarSolicitudXOrdenAmbulatoria(
									con,
									idCuenta,
									actividadesForm.getListadoOrdenes("numero_solicitud_"+i).toString(), //se envia la solicitud de referencia
									detalle,
									usuario);
								
								if(resp0>0)
									UtilidadBD.finalizarTransaccion(con);
								else
								{
									UtilidadBD.abortarTransaccion(con);
									ArrayList erroresGeneracion = actEjecutadas.getErrores();
									for(int l=0;l<erroresGeneracion.size();l++)
										errores.add(erroresGeneracion.get(l));
								}	
								
							}
							else
							{
								//Se reporta error de cuenta abierta diferentes a consulta externa
								errores.add("Paciente "+
									actividadesForm.getListadoOrdenes("identificacion_"+i)+
									" tiene cuenta abierta diferente a consulta externa");
							}
						}
						else
						{
							//Se reporta error de que el convenio de la cuenta abierta no es PYP
							errores.add("El convenio de la cuenta actual del paciente "+actividadesForm.getListadoOrdenes("identificacion_"+i)+" no es PYP");
						}
					}
					else
					{
						//Se reporta error de convenios diferentes
						errores.add("El convenio de la orden ambulatoria "+
							actividadesForm.getListadoOrdenes("orden_"+i)+
							" no coincide con el convenio de la cuenta actual del paciente "+
							actividadesForm.getListadoOrdenes("identificacion_"+i));
					}
					//***************************************************************************************
				}
				else
				{
					//*********************CASO CUENTA CERRADA****************************************************************
					//1) Se verifica si se puede crear nueva cuenta
					
					//***************SE TOMA Y SE VALIDA EL CONSECUTIVO DE INGRESO**********************************************************
					String valorConsecutivoIngreso=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt());
					String anioConsecutivoIngreso=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt(),valorConsecutivoIngreso);

					if(!UtilidadCadena.noEsVacio(valorConsecutivoIngreso) ||valorConsecutivoIngreso.equals("-1"))
					{
						errores.add("Falta definir el consecutivo de ingreso para generar nuevos ingresos de paciente. Proceso Cancelado");
						errorConsecutivoIngreso = true;
					}
					else
					{
						try
						{
							Integer.parseInt(valorConsecutivoIngreso);
						}
						catch(Exception e)
						{
							errorConsecutivoIngreso = true;
							logger.error("Error en validacionConsecutivoDisponibleIngreso:  "+e);
							errores.add("El consecutivo de ingresos debe ser numérico. Proceso Cancelado");
						}
					}
					
					String identificacion[] = actividadesForm.getListadoOrdenes("identificacion_"+i).toString().split(" ",2);
					RespuestaValidacion respPrevia;
					respPrevia=UtilidadValidacion.validacionPreviaIngresoPaciente(con, identificacion[0], identificacion[1], usuario.getCodigoInstitucion() );
					
					if(respPrevia.puedoSeguir&&!errorConsecutivoIngreso)
					{
						UtilidadBD.iniciarTransaccion(con);
						int resp0 = 0, resp1 = 0;
						
						//2) Se inserta nueva cuenta
						
						//Se inserta el ingreso
						IngresoGeneral ingreso = new IngresoGeneral(
								usuario.getCodigoInstitucion(), 
								new PersonaBasica(),
								ConstantesIntegridadDominio.acronimoEstadoAbierto,
								usuario.getLoginUsuario(),
								valorConsecutivoIngreso,
								anioConsecutivoIngreso,
								usuario.getCodigoCentroAtencion(),
								"", //sin codigo paciente entidad subcontratada
								"","",""
							);
						ingreso.getPaciente().setCodigoPersona(Integer.parseInt(actividadesForm.getListadoOrdenes("codigo_paciente_"+i).toString()));
						
						int idIngreso = 0;
						
						idIngreso = ingreso.insertarIngresoTransaccional(con, ConstantesBD.continuarTransaccion);
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
						
						
						cuenta.getCuenta().setIdCuenta("");
						cuenta.getCuenta().setIdIngreso(idIngreso+"");
						cuenta.getCuenta().setCodigoEstado(ConstantesBD.codigoEstadoCuentaActiva);
						cuenta.getCuenta().setLoginUsuario(usuario.getLoginUsuario());
						cuenta.getCuenta().setTieneResponsablePaciente(false);
						cuenta.getCuenta().setFechaApertura(UtilidadFecha.getFechaActual(con));
						cuenta.getCuenta().setHoraApertura(UtilidadFecha.getHoraActual(con));
						
						
						// Caso en el cual se debe asignar la via de ingreso consulta externa y el area parametrizada en el parametro Área para apertura de cuenta automática en Cargar Actividades de PyP
						Utilidades.imprimirMapa(actividadesForm.getListadoOrdenes());
						logger.info("\n\n\nANTES DE ASIGNAR INFORMACIÓN AUTOMATICA");
						if(UtilidadTexto.isEmpty(actividadesForm.getListadoOrdenes("id_cuenta_"+i)+"")){
							cuenta.getCuenta().setCodigoArea(ValoresPorDefecto.consultarAreasAperturaCuentaAutoPYP(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()).get(0).getArea());
							cuenta.getCuenta().setCodigoViaIngreso(ConstantesBD.codigoViaIngresoConsultaExterna);
						}	
							
						
						//Se asigna el usuario en cada subCuenta y se prepara la informacion
						for(int j=0;j<cuenta.getCuenta().getConvenios().length;j++)
						{
							
							cuenta.getCuenta().getConvenios()[j].setSubCuenta("");
							cuenta.getCuenta().getConvenios()[j].setLoginUsuario(usuario.getLoginUsuario());
							cuenta.getCuenta().getConvenios()[j].getTitularPoliza().setExisteBd(false);
							cuenta.getCuenta().getConvenios()[j].setSubCuentaVerificacionDerechos(false);
							cuenta.getCuenta().getConvenios()[j].setFacturado(ConstantesBD.acronimoNo);
							cuenta.getCuenta().getConvenios()[j].setIngreso(idIngreso);
							for(int k=0;k<cuenta.getCuenta().getConvenios()[j].getTitularPoliza().getSizeInformacionPoliza();k++)
								cuenta.getCuenta().getConvenios()[j].getTitularPoliza().setInformacionPoliza(
										cuenta.getCuenta().getConvenios()[j].getTitularPoliza().getCodigoInformacionPoliza(k), 
										cuenta.getCuenta().getConvenios()[j].getTitularPoliza().getFechaInformacionPoliza(k),
										cuenta.getCuenta().getConvenios()[j].getTitularPoliza().getAutorizacionInformacionPoliza(k), 
										cuenta.getCuenta().getConvenios()[j].getTitularPoliza().getValorInformacionPoliza(k), 
										cuenta.getCuenta().getConvenios()[j].getTitularPoliza().getUsuarioInformacionPoliza(k), false, false);
						}
						
						ResultadoBoolean resB = cuenta.guardar(con);
						resp0 = resB.isTrue()?1:0;
						idCuenta = resB.getDescripcion(); //Se toma el Id de la nueva cuenta consultada
						//se verifica si se insertó cuenta
						if(resp0>0)
						{
							//3) Se carga el detalle de la orden ambulatoria
							HashMap detalle = actEjecutadas.consultarDetalleOrdenAmb(con,actividadesForm.getListadoOrdenes("codigo_orden_"+i).toString());
							
							//4) Se genera la solicitud de la orden
							resp1 = actEjecutadas.generarSolicitudXOrdenAmbulatoria(
								con,
								idCuenta,
								actividadesForm.getListadoOrdenes("numero_solicitud_"+i).toString(), //se envia la solicitud de referencia
								detalle,
								usuario);
						}
						
						
						if(resp0>0&&resp1>0){
							UtilidadBD.finalizarTransaccion(con);
						}	
						else
						{
							UtilidadBD.abortarTransaccion(con);
							ArrayList erroresGeneracion = actEjecutadas.getErrores();
							for(int l=0;l<erroresGeneracion.size();l++)
								errores.add(erroresGeneracion.get(l));
							logger.info("********** ABORTO TRANSACCIÓN ***********");
						}
					}
					else
					{
						//Se reporta error de cuenta abierta diferentes a consulta externa
						errores.add("Paciente "+actividadesForm.getListadoOrdenes("identificacion_"+i)+" tiene cuenta abierta diferente a consulta externa");
					}
					//*****************************************************************************************************
				}
			}
		}
		
		//*******VERIFICACION DE RESULTADOS***********************************************
		if(errores.size()>0)
		{
			actividadesForm.setEstado("empezar");
			ActionErrors erroresJSP = new ActionErrors();
			erroresJSP.add("Proceso Generó errores",new ActionMessage("error.pyp.actEjecutadasXCargar.procesoCargue"));
			saveErrors(request,erroresJSP);
			
			actividadesForm.setRutaArchivo(actEjecutadas.generarArchivoInconsistencias(con,usuario,errores));
			actividadesForm.setExisteArchivo(actEjecutadas.isExisteArchivo());
		}
		else
		{
			if(actividadesForm.getCodigoConvenio()>0)
				actividadesForm.setSeleccion(actividadesForm.getCodigoConvenio()+ConstantesBD.separadorSplit+actividadesForm.getNombreConvenio());
			else
				actividadesForm.setSeleccion(actividadesForm.getNombreConvenio());
			
			//Se vuelve y consultan las ordenes
			return accionBusquedaOrdenes(con,actividadesForm,mapping);
		}
		//********************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método implementado para paginar el listado de ordenes ambulatorias pendientes de cargar
	 * @param con
	 * @param actividadesForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ActEjecutadasXCargarForm actividadesForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(actividadesForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ActEjecutadasXCargarAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ActEjecutadasXCargarAction", "errors.problemasDatos", true);
		}
	}


	/**
	 * Método implementado para ordenar el listado de ordenes ambulatorios pendientes de cargar
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ActEjecutadasXCargarForm actividadesForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"seleccion_",
				"orden_",
				"fecha_",
				"identificacion_",
				"codigo_paciente_",
				"paciente_",
				"profesional_",
				"especialidad_",
				"id_cuenta_",
				"codigo_convenio_",
				"estado_cuenta_",
				"codigo_orden_",
				"numero_solicitud_"
			};

		
		//Se pasa la fecha a formato BD
		for(int i=0;i<actividadesForm.getNumOrdenes();i++)
			actividadesForm.setListadoOrdenes("fecha_"+i,UtilidadFecha.conversionFormatoFechaABD(actividadesForm.getListadoOrdenes("fecha_"+i).toString()));
		
		actividadesForm.setListadoOrdenes(Listado.ordenarMapa(indices,
				actividadesForm.getIndice(),
				actividadesForm.getUltimoIndice(),
				actividadesForm.getListadoOrdenes(),
				actividadesForm.getNumOrdenes()));
		
		///Se pasa la fecha a formato Aplicacion
		for(int i=0;i<actividadesForm.getNumOrdenes();i++)
			actividadesForm.setListadoOrdenes("fecha_"+i,UtilidadFecha.conversionFormatoFechaAAp(actividadesForm.getListadoOrdenes("fecha_"+i).toString()));
		
		
		actividadesForm.setListadoOrdenes("numRegistros",actividadesForm.getNumOrdenes()+"");
		
		actividadesForm.setUltimoIndice(actividadesForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	private ActionForward accionBusquedaOrdenes(Connection con, ActEjecutadasXCargarForm actividadesForm, ActionMapping mapping) 
	{
		actividadesForm.setOffset(0);
		
		//Se verifica seleccion del convenio
		if(!actividadesForm.getSeleccion().equals("")&&!actividadesForm.getSeleccion().equals("TODOS"))
		{
			String vector[] = actividadesForm.getSeleccion().split(ConstantesBD.separadorSplit);
			actividadesForm.setCodigoConvenio(Integer.parseInt(vector[0]));
			actividadesForm.setNombreConvenio(vector[1]);
			actividadesForm.setSeleccionConvenio(true);
		}
		else if(actividadesForm.getSeleccion().equals("TODOS"))
		{
			actividadesForm.setCodigoConvenio(0);
			actividadesForm.setNombreConvenio("TODOS");
			actividadesForm.setSeleccionConvenio(true);
		}
		else
		{
			actividadesForm.setCodigoConvenio(0);
			actividadesForm.setNombreConvenio("");
			actividadesForm.setSeleccionConvenio(false);
		}
		actividadesForm.setSeleccion("");
		actividadesForm.setSeleccionarTodas(true);
		
		//Se consultan las ordenes ambulatorias pendientes de cargar x convenio
		ActEjecutadasXCargar actEjecutadas = new ActEjecutadasXCargar();
		actividadesForm.setListadoOrdenes(actEjecutadas.consultarOrdenesAmbXConvenio(con,actividadesForm.getCodigoConvenio()+""));
		actividadesForm.setNumOrdenes(Integer.parseInt(actividadesForm.getListadoOrdenes("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implmentado para iniciar el flujo de la funcionalidad
	 * @param con
	 * @param actividadesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActEjecutadasXCargarForm actividadesForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//se resetea la forma
		actividadesForm.reset();
		
		//se toman los max page items
		actividadesForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		//se cargan los convenios
		actividadesForm.setConvenios(Utilidades.obtenerConvenios(con,"","",false,"",false));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}
