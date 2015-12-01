/*
 * Sep 28, 06
 */
package com.princetonsa.action.manejoPaciente;

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

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.TrasladoCentroAtencionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.manejoPaciente.TrasladoCentroAtencion;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Traslado Centro Atención
 */
public class TrasladoCentroAtencionAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(TrasladoCentroAtencionAction.class);
	
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
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof TrasladoCentroAtencionForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				TrasladoCentroAtencionForm trasladoForm =(TrasladoCentroAtencionForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");

				String estado=trasladoForm.getEstado(); 
				logger.warn("\n\n En TrasladoCentroAtencionAction el Estado ["+estado+"] \n\n");

				//verificar si es null (Paciente está cargado)
				if(paciente==null || paciente.getTipoIdentificacionPersona().equals(""))
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);

				//Verificar que el Paciente tenga cuenta Abierta
				if (paciente.getCodigoCuenta()<1)
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no tiene la cuenta abierta", "errors.paciente.cuentaNoAbierta", true);

				//Valida que el ingreso del paciente no se hubiera realizado por Entidades SubContratadas				
				if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
				{				
					request.setAttribute("descripcionError","Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""));
					UtilidadBD.closeConnection(con);				
					return mapping.findForward("paginaError");			
				}		


				if(estado == null)
				{
					trasladoForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Traslado Centro Atencion (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				//estado empezar => inicio flujo funcionalidad
				//estado recargar => deseleccion de centro atencion o area
				else if (estado.equals("empezar")|| estado.equals("recargar")) 
				{
					return accionEmpezar(con,trasladoForm,mapping,paciente,usuario,request);
				}
				else if (estado.equals("parametrizar"))
				{
					return accionParametrizar(con,trasladoForm,mapping,paciente);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,trasladoForm,mapping,paciente,usuario,request);			

				}
				else
				{
					trasladoForm.reset();
					logger.warn("Estado no valido dentro del flujo de TrasladoCentroAtencionAction (null) ");
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
	 * Método implementado para realizar el traslado de centro de atencion de la cuenta del paciente
	 * @param con
	 * @param trasladoForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardar(Connection con, TrasladoCentroAtencionForm trasladoForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		int codigoCama = 0;
		int codigoTraslado = 0;
		int idCuenta = paciente.getCodigoCuenta();
		TrasladoCentroAtencion trasladoCentroAtencion = new TrasladoCentroAtencion();		
		HashMap parametros = new HashMap();
		boolean cambioReserva = false;
		int key=0;
		//************VALIDACIONES CASO HOSPITALIZACION************************
		if(trasladoForm.getViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{
			ActionErrors errores = new ActionErrors();
			try{
			codigoCama=Integer.parseInt(trasladoForm.getDatosCama());
			}
			catch(NumberFormatException e)
			{
				if (trasladoForm.getTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
				{
					key=1;
				}
			}
			if (key!=1)
				{
			    boolean camaOcupada=UtilidadValidacion.esCamaOcupadaRangoFechaHoraMayorIgualDado(con, UtilidadFecha.conversionFormatoFechaABD(trasladoForm.getFechaTraslado()), trasladoForm.getHoraTraslado(),  codigoCama);
		        if(camaOcupada)
		            errores.add("cama ocupada", new ActionMessage("error.trasladocama.camaOcupada"));
		        
			    if(UtilidadValidacion.pacienteTieneTrasladoRangoFechaHoraMayorIgualDado(con, UtilidadFecha.conversionFormatoFechaABD(trasladoForm.getFechaTraslado()),trasladoForm.getHoraTraslado(), idCuenta))
					errores.add("existe traslado posterior", new ActionMessage("error.trasladocama.existeTrasladoAnterior", trasladoForm.getFechaTraslado(), trasladoForm.getHoraTraslado()));
			    
			    if(!errores.isEmpty())
			    {
			    	saveErrors(request,errores);
			    	this.cerrarConexion(con);
			    	trasladoForm.setEstado("parametrizar");
			    	return mapping.findForward("principal");
			    }
				}
		}
		
		logger.info("\n\n\n [TrasladoCentroAtencion] -->CamaNueva:"+codigoCama+"\n\n\n");
		
		
		//**********************************************************************
		
		//**************INSERCION DE DATOS*****************************************
		/**Iniciamos la transaccion**/
	    UtilidadBD.iniciarTransaccion(con);
	    int resp0 = 0, resp1 = 0;
	    TrasladoCamas trasladoCamas= new TrasladoCamas();  
		
		
		//1) Actualizacion de la cama**************************************************************************** 
		if(trasladoForm.getViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{
			//Para tomar el estado definido en valores por defecto para una cama despues de desocuparla******************
		    int tmp=Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(usuario.getCodigoInstitucionInt()));
		    
		    int codigoCamaActual=ConstantesBD.codigoNuncaValido;
		    //Codigo de la cama actual del paciente****************************************************
		    if (trasladoForm.getCamaActual().containsKey("codigo_0"))
		    	codigoCamaActual=Integer.parseInt(trasladoForm.getCamaActual("codigo_0").toString());	
		    

		    logger.info("\n\n\n [TrasladoCentroAtencion] -->CamaAnterior:"+codigoCamaActual+"\n\n\n");
		    //Codigo de la nueva cama que le voy a asignar al paciente********************************
		    int codigoCamaNueva=codigoCama;			    
		    //Se bloquean las camas***********************************
		    ArrayList filtro = new ArrayList();
		    filtro.add(codigoCamaActual+"");
		    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
		    filtro.clear();
		    filtro.add(codigoCamaNueva+"");
		    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
			 
		    //se valida que la nueva cama esté disponible 
		    if(codigoCamaNueva>0 && Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaDisponible && Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaReservada)
			{
				ActionErrors errores = new ActionErrors();  
                errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
                saveErrors(request, errores);
                UtilidadBD.abortarTransaccion(con);
                trasladoForm.setEstado("parametrizar");
                this.cerrarConexion(con);            
                return mapping.findForward("principal"); 
			}
			    
			try
			{
				int exito1=1, exito3=1;
				
				//Cama Antigua-Actual
				boolean exito0=false;
				int exito2=0;
				
			    if (codigoCamaActual>0)
			    {
					//Actualizo la fecha y hora de finalizacion de ocupacion de la cama actual del paciente********************************
					 exito0 = trasladoCamas.actualizarFechaHoraFinalizacionNoTransaccional(con, idCuenta, UtilidadFecha.conversionFormatoFechaABD(trasladoForm.getFechaTraslado()),trasladoForm.getHoraTraslado(),"");    
				    //Modifico el estado de la cama anterior al estado definido en Parametros Generales********************************************************
			    	 exito2 = trasladoCamas.modificarEstadoCama(con,tmp,codigoCamaActual,usuario.getCodigoInstitucionInt());
			    }
			    else 
			    	{
			    	 exito0=true;
			    	 exito2=1;
			    	}
			    
			    
			    //Nueva Cama
			    
		 	    //Insercion del tralado de cama********************************************************************
			    if (codigoCamaNueva>0)
			    {
				exito1 = trasladoCamas.insertarTrasladoCamaPaciente(con, UtilidadFecha.conversionFormatoFechaABD(trasladoForm.getFechaTraslado()), trasladoForm.getHoraTraslado(),codigoCamaNueva,codigoCamaActual,Integer.parseInt(usuario.getCodigoInstitucion()), usuario.getLoginUsuario(), paciente.getCodigoPersona(),idCuenta, paciente.getCodigoConvenio(),"");
				//Modifico el estado de la cama a la cual se le traslada el paciente a estado Ocupada*****************************************************************************************
			    exito3 = trasladoCamas.modificarEstadoCama(con,ConstantesBD.codigoEstadoCamaOcupada,codigoCamaNueva,usuario.getCodigoInstitucionInt());
			    }
			    else if (key==1)
			    {
			    	exito1=1;
			    	exito3=1;
			    }
				 /**************************************************************************************
				  * ESTO FUE AGREGADA POR JHONY ALEXANDER DUQUE PARA EL MANEJO DE LAS CAMAS RESERVADAS
				  ***************************************************************************************/
				    
			    	//aqui se valida si el paciente tiene cama reservada,
					//se ser asi entonces se valida si tomo o no la reserva.
			    
					if (!trasladoForm.getReserva().equals(""))
					{
						logger.info("el valor de los datos son "+trasladoForm.getReserva()+" -- "+trasladoForm.getDatosCama());
						if (trasladoForm.getReserva().equals(trasladoForm.getDatosCama()+""))
						{
							logger.info("\n\n *********** el paciente SI tomo la cama reservada ");
							//se le ingresan los parametros para requeridos para que pueda cambiar el estado
							parametros.put("nuevoEstadoReserva", ConstantesIntegridadDominio.acronimoEstadoOcupado);
							parametros.put("institucion",usuario.getCodigoInstitucionInt());
							parametros.put("codigoCama",trasladoForm.getReserva());
							parametros.put("codigoPaciente",paciente.getCodigoPersona());
							parametros.put("estadoReserva", ConstantesIntegridadDominio.acronimoEstadoActivo);
							parametros.put("nuevoEstadoCama", ConstantesBD.codigoEstadoCamaOcupada);
							cambioReserva=trasladoCamas.cambiarEstaReserva(con, parametros);
						}
						else
						{
							logger.info("\n\n *********** el paciente NO tomo la cama reservada ");
//							se le ingresan los parametros para requeridos para que pueda cambiar el estado
							parametros.put("nuevoEstadoReserva", ConstantesIntegridadDominio.acronimoEstadoCancelado);
							parametros.put("institucion",usuario.getCodigoInstitucionInt());
							parametros.put("codigoCama",trasladoForm.getReserva());
							parametros.put("codigoPaciente",paciente.getCodigoPersona());
							parametros.put("estadoReserva", ConstantesIntegridadDominio.acronimoEstadoActivo);
							parametros.put("nuevoEstadoCama", ConstantesBD.codigoEstadoCamaDisponible);
							cambioReserva=trasladoCamas.cambiarEstaReserva(con, parametros);
						
						}
						if (!cambioReserva)
							logger.info("ocurrio un problema en el cambio de la reserva!!");
					}	
					else
						logger.info("\n\n *********** el paciente No tiene cama reservada ");
			    
					
				logger.info("[exito]"+exito0+"&&"+exito1+">0&&"+exito2+">0&&"+exito3+">0");	
					
			     if(exito0&&exito1>0&&exito2>0&&exito3>0)
					{
					 resp0 = 1;
			    	 codigoTraslado = exito1;
			     }
			     else
			    	 resp0 = 0;
				}
			
			catch(SQLException e)
			{
				logger.error("Error realizando el traslado de cama en accionGuardar de TrasladoCentroAtencionAction: "+e);
				resp0 = 0;
				if (key==1)
					resp0 = 1;
			}
			    	    
			    
		}
		else if(trasladoForm.getViaIngreso().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
		{
			try 
			{
				if(paciente.getCodigoCama()>0)
					resp0 = trasladoCamas.modificarEstadoCama(con, ConstantesBD.codigoEstadoCamaDisponible, paciente.getCodigoCama(), usuario.getCodigoInstitucionInt());
				else
					resp0 = 1;
				
			} 
			catch (SQLException e) 
			{
				logger.error("Error al cambiar el estado de la cama de urgencias en accionGuardar: "+e);
				resp0 = 0;
				if (key==1)
					resp0=1;
			}
			
			if(resp0>0)
				{
				resp0 = trasladoCentroAtencion.desasignarCamaUrgencias(con,idCuenta+"");
				}
			
		}
		
		
		//2) Realizar el traslado de centro atencion: ******************************************************************
		// - Cambiar área de la cuenta
		// - Actualizar la tabla tratantes_cuenta
		// - Actualizar la tabla adjuntos_cuenta (si existe registro)
		Cuenta cuenta = new Cuenta();
		try
		{
			cuenta.cargarCuenta(con,idCuenta+"");
		}
		catch(Exception e)
		{
			logger.error("Error cargando la cuenta en accionGuardar de TrasladoCentroAtencionAction: "+e);
		}
		//Se toma valoracion y medico de la cuenta
		String solValoracion = UtilidadValidacion.getCodigoSolicitudValoracionInicial(con,idCuenta) + "";
		String codigoMedico = "";
		
		DtoValoracion valoracion = Valoraciones.cargarBase(con, solValoracion);
		codigoMedico = valoracion.getProfesional().getCodigoPersona()+"";
		
		
		//Se parametrizan los campos
		this.llenarCampos(trasladoCentroAtencion,trasladoForm,paciente,cuenta,usuario,codigoTraslado,solValoracion,codigoMedico);
		//Se realiza el traslado de centro de atencion
		resp1 = trasladoCentroAtencion.realizarTraslado(con);
		//*********************************************************************************************************
		logger.info("INDICADORES DE PROCESO --------> \n -------->traslado ->"+resp1+"\n -------->cama ->"+resp0);
		
		if((resp0>0&&resp1>0) || (resp1>0&&key==1))
		{
			UtilidadBD.finalizarTransaccion(con);
					
			//se preparan los datos del resumen
			trasladoForm.setNombreNuevoCentroAtencion(Utilidades.obtenerNombreCentroAtencion(con,Integer.parseInt(trasladoForm.getNuevoCentroAtencion())));
			//paciente.setCentroAtencionInt(Integer.parseInt(trasladoForm.getNuevoCentroAtencion()));
			
			trasladoForm.setNombreNuevaArea(Utilidades.obtenerNombreCentroCosto(con,Integer.parseInt(trasladoForm.getNuevaArea()),usuario.getCodigoInstitucionInt()));
			
		}				
		else
		{
			UtilidadBD.abortarTransaccion(con);
			ActionErrors errores = new ActionErrors();
			errores.add("Error realizando el traslado de centro atencion",new ActionMessage("errors.ingresoDatos","el traslado de centro de atención. Por favor intente más tarde"));
			saveErrors(request,errores);
			trasladoForm.setEstado("parametrizar");
		}		
		
		//**************************************************************************
		trasladoForm.setKey(key);		
		this.cerrarConexion(con);	
		
		/**
		 * Inc. 251
		 * Despues de realizar un traslado de centro de atenci&oacute;n exitoso, el sistema debe recargar el 
		 * paciente en sesi&oacute;n teniendo en cuenta que debe conservar las valiciones y flujo de cuando
		 * se quiere cargar un paciente en un centro de atenci&oacute;n difente al del origen.
		 * 
		 * @author Diana Ruiz
		 * @since 27/07/2011
		 * 
		 */
		
		Connection con2=null;
		//SE ABRE CONEXION
		try
		{
			con2 = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
		}
		catch(SQLException e)
		{
			logger.warn("No se pudo abrir la conexión"+e.toString());
		}	
		
		UtilidadesManejoPaciente.cargarPaciente(con2, usuario, paciente, request);		
		request.getSession().setAttribute("pacienteActivo", paciente);		
		this.cerrarConexion(con2);		
		return mapping.findForward("principal");
	
	}

	/**
	 * Método usado para cargar el objeto TrasladoCentroAtencion con los parámetros
	 * @param trasladoCentroAtencion
	 * @param trasladoForm
	 * @param paciente
	 * @param cuenta
	 * @param usuario
	 * @param codigoTraslado
	 * @param solValoracion 
	 * @param codigoMedico 
	 */
	private void llenarCampos(TrasladoCentroAtencion trasladoCentroAtencion, TrasladoCentroAtencionForm trasladoForm, PersonaBasica paciente, Cuenta cuenta, UsuarioBasico usuario, int codigoTraslado, String solValoracion, String codigoMedico) 
	{
		trasladoCentroAtencion.clean();
		trasladoCentroAtencion.setCampos("idCuenta",paciente.getCodigoCuenta());
		trasladoCentroAtencion.setCampos("centroAtencionInicial",cuenta.getCodigoCentroAtencion());
		trasladoCentroAtencion.setCampos("areaInicial",cuenta.getCodigoArea());
		trasladoCentroAtencion.setCampos("nuevoCentroAtencion",trasladoForm.getNuevoCentroAtencion());
		trasladoCentroAtencion.setCampos("nuevaArea",trasladoForm.getNuevaArea());
		trasladoCentroAtencion.setCampos("observaciones",trasladoForm.getObservaciones());
		trasladoCentroAtencion.setCampos("codigoTraslado",codigoTraslado);
		trasladoCentroAtencion.setCampos("usuario",usuario.getLoginUsuario());
		trasladoCentroAtencion.setCampos("institucion",usuario.getCodigoInstitucion());
		trasladoCentroAtencion.setCampos("valoracion",solValoracion);
		trasladoCentroAtencion.setCampos("codigoMedico",codigoMedico);
		
	}

	/**
	 * Método implementado para realizar las validaciones adicionales de acuerdo
	 * a la vía de inrgeso del paciente
	 * @param con
	 * @param trasladoForm
	 * @param mapping
	 * @param paciente
	 * @return
	 */
	private ActionForward accionParametrizar(Connection con, TrasladoCentroAtencionForm trasladoForm, ActionMapping mapping, PersonaBasica paciente) 
	{
		int idCuenta = paciente.getCodigoCuenta();
		trasladoForm.resetHospitalizacion();
		HashMap reserva = new HashMap ();
		//Se verifica la vía de ingreso
		if(trasladoForm.getViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{
			trasladoForm.setTipoPaciente(UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, paciente.getCodigoCuenta()+"").getAcronimo());
			
			trasladoForm.setFechaTraslado(UtilidadFecha.getFechaActual());
			trasladoForm.setHoraTraslado(UtilidadFecha.getHoraActual());
			
			trasladoForm.setSexo(paciente.getCodigoSexo()+"");
			trasladoForm.setEdad(paciente.getEdad()+"");
			
			//Se carga la cama actual del paciente
			TrasladoCamas trasladoCama = new TrasladoCamas();
			trasladoForm.setCamaActual(trasladoCama.cargarCamaActualPaciente(con,paciente.getCodigoPersona()));
			if(trasladoForm.getCamaActual("hora_traslado_0")!=null)
				trasladoForm.setCamaActual("hora_traslado_0",UtilidadFecha.convertirHoraACincoCaracteres(trasladoForm.getCamaActual("hora_traslado_0").toString()));
			
			//Se toma la fecha/hora admision
			trasladoForm.setFechaAdmision(UtilidadFecha.conversionFormatoFechaAAp( Utilidades.obtenerFechaAdmisionHosp(con,idCuenta)));
			trasladoForm.setHoraAdmision(UtilidadFecha.convertirHoraACincoCaracteres(Utilidades.obtenerHoraAdmisionHosp(con,idCuenta)));
			
			//se consulta si el paciente tiene una cama reservada.
			reserva=trasladoCama.cargarCamaReservadaPaciente(con, paciente.getCodigoPersona(), -1);
				logger.info("\n\n *** el los datos de la reserva son "+reserva);
			//si se encuentra una rereseva se guarda para identifcar si se tomo o no.	
			if (reserva.containsKey("codigo") && reserva.get("codigo") != null && !(reserva.get("codigo")+"").equals(""))
				trasladoForm.setReserva(reserva.get("codigo")+"");
			
			if ((reserva.get("centroAtencion")+"").equals(trasladoForm.getNuevoCentroAtencion()))
			{
				trasladoForm.setCamaNueva(reserva);
//			se verifica si el paciente tenia una cama reservada, de ser asi
			//se saca el codigo en una variable en el form par aluego verificar
			//si tomo esta cama o la cambio
			if (trasladoForm.getCamaNueva("codigo") != null && !trasladoForm.getCamaNueva("codigo").equals(""))
			{
				logger.info("\n\n** 1 **");
				trasladoForm.setDatosCama(trasladoForm.getCamaNueva("codigo")+"");
				
				if (trasladoForm.getCamaNueva("esUci").equals("SI"))
					trasladoForm.setCamaNueva("tipoMonitoreo", trasladoForm.getCamaNueva("esUci")+" ("+trasladoForm.getCamaNueva("tipoMonitoreo")+")");
				else
					trasladoForm.setCamaNueva("tipoMonitoreo", "No");
			
					
			}	
		}
			
		}
		
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para iniciar el flujo de la funcionalidad
	 * Traslado Centro Atencion
	 * @param con
	 * @param trasladoForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, TrasladoCentroAtencionForm trasladoForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		if(!trasladoForm.getEstado().equals("recargar"))
		{
			//Flujo usado para el estado EMPEZAR
			
			trasladoForm.reset();
			trasladoForm.setEstado("empezar");
			trasladoForm.setInstitucion(usuario.getCodigoInstitucion());
			trasladoForm.setNuevoCentroAtencion("0");
			trasladoForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
			trasladoForm.setViaIngreso(paciente.getCodigoUltimaViaIngreso()+"");
			trasladoForm.setTipoPaciente(UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, paciente.getCodigoCuenta()+"").getAcronimo());
			//se instancia mundo TrasladoCentroAtencion
			TrasladoCentroAtencion traslado = new TrasladoCentroAtencion();
			
			ActionErrors errores = traslado.validaciones(con,paciente,usuario);
			
			this.cerrarConexion(con);
			if(!errores.isEmpty())
			{
				saveErrors(request,errores);
				return mapping.findForward("paginaErroresActionErrors");
			}
		}
		else
		{
			trasladoForm.setEstado("empezar");
			this.cerrarConexion(con);
		}
		
		return mapping.findForward("principal");
	}

	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
	
	/**
	* Método para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
}
