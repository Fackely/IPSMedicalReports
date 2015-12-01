/*
* @(#)ProcedimientoAction.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.action.solicitudes;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.laboratorios.InterfazLaboratorios;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.interconsultas.SolicitarForm;

import com.princetonsa.dao.DaoFactory;

import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientos;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.ordenesmedicas.solicitudes.Procedimiento;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.pdf.RespuestaProcedimientoPdf;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.Medicos;

/**
* Controla el flujo de operaciones sobre una solicitud de procedimiento
*
* @version 1.0, Sep 10, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm
*/
public class ProcedimientoAction extends Action
{
	/** Objeto que permite el manejo de archivos de registro */
	private transient Logger il_logger = Logger.getLogger(ProcedimientoAction.class);

	/** Identificadores del tipo de validación sobre el usuario médico */
	private transient static final int ii_MEDICO					= -1;
	private transient static final int ii_MEDICO_TRATANTE			= 0;
	private transient static final int ii_MEDICO_ADJUNTO			= 1;
	private transient static final int ii_MEDICO_TRATANTE_ADJUNTO	= 2;
	
	/** Definición de constantes del estado del flujo */
	private static final int ii_ESTADO_INVALIDO					= -1;
	private static final int ii_ESTADO_GUARDAR_MODIFICACION		= 0;
	private static final int ii_ESTADO_PREPARAR_MODIFICACION	= 2;
	private static final int ii_ESTADO_RESUMEN					= 3;
	private static final int ii_ESTADO_IMPRESION				= 4;

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping		mapping,
		ActionForm			form,
		HttpServletRequest	request,
		HttpServletResponse	response
	)throws Exception
	{		
		/* Validar que el tipo de formulario recibido sea el esperado */
		if(form instanceof SolicitarForm)
		{
			@SuppressWarnings("unused")
			int				li_estado;
			SolicitarForm	forma;
			String			estado;

			forma	= (SolicitarForm)form;
			estado	= forma.getEstado();
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			il_logger.warn("\n\n El Estado en ProcedimientoAction [" + estado + "]----------------------\n\n");

			
			if (estado.equals("guardarModificacion"))
			{
				return guardarModificacion(mapping, forma, request);
			}else
			if (estado.equals("modificar"))
			{
				return prepararModificacion(mapping, forma, request);
			}else
			if (estado.equals("resumen"))
			{
				/* Mostrar el resumen de la solicitud de procedimiento */
				return resumen(mapping, forma, request,usuario);
			}else
			if (estado.equals("imprimir"))
			{
				return this.accionImprimir(forma.getNumeroSolicitud(), mapping, request);
			}else
				if (!UtilidadCadena.noEsVacio(estado))
				{
					if(il_logger.isDebugEnabled() )
						il_logger.debug(
							"Estado inválido en el flujo de Solicitud de Procedimientos"
						);
					return error(mapping, request, "errors.estadoInvalido", true);
				}
			
		}

		/* El formulario recibido no corresponde al tipo esperado */
		return error(mapping, request, "errors.accesoInvalido", true);
	}

	
	/**
	 * Metodo para la impresion de la respuesta a la solicitud de un procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param mapping
	 * @param medico
	 * @param pacienteActivo
	 * @param request
	 * @return Action Forward
	 * @throws SQLException
	 */
	private ActionForward accionImprimir(int numeroSolicitud, ActionMapping mapping,
										HttpServletRequest request) 
	{

		
		UsuarioBasico medico=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		Connection con=null;
		try
		{
			String nombreArchivo;
			Random r=new Random();
			nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
			
			
			con= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			SolicitudProcedimiento solicitud=new SolicitudProcedimiento();
			Procedimiento procedimiento=new Procedimiento();
			 solicitud.setInstitucion(medico.getCodigoInstitucion());
			 solicitud.cargarSolicitudProcedimiento(con, numeroSolicitud);
			
			/**if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCRespondida || solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCInterpretada)
			{
				procedimiento.cargar(con, numeroSolicitud);
				procedimiento.setNumeroSolicitud(numeroSolicitud);
				procedimiento.getDocumentosAdjuntos().cargarDocumentosAdjuntos(con,numeroSolicitud, true, "");
			}**/
			RespuestaProcedimientoPdf.pdfImprimirRespuesta(con, ValoresPorDefecto.getFilePath()+nombreArchivo, solicitud, medico, paciente, procedimiento);
			request.setAttribute("nombreArchivo", nombreArchivo);
			request.setAttribute("nombreVentana", "Procedimientos");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("abrirPdf");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return ComunAction.accionSalirCasoError(mapping, request, con, Logger.getLogger(ProcedimientoAction.class), "errors.problemasBD", "errors.problemasBD", true);
		}
	
	}

	/**
	* Abre una conexión a una fuente de datos
	* ab_iniciarTransaccion indicador de si la conexión debe o no iniciar una transacción
	* @throws SQLException
	*/
	private Connection abrirConexion(boolean ab_iniciarTransaccion)throws SQLException
	{
		Connection	lc_con;
		DaoFactory	ldf_df;

		/* Obtener una conexión a la fuente de datos */
		ldf_df	= DaoFactory.getDaoFactory(System.getProperty("TIPOBD") );
		lc_con	= ldf_df.getConnection();

		/* Iniciar una transacción si es necesario */
		if(ab_iniciarTransaccion)
		{
			if(!ldf_df.beginTransaction(lc_con) )
				lc_con = null;
		}

		return lc_con;
	}

	/** Cargar una solicitud de procedimiento desde una funente de datos */
	@SuppressWarnings({ "unchecked", "static-access" })
	private boolean cargarSolicitud(
		SolicitarForm	forma,
		Connection		ac_con,
		UsuarioBasico usuario	
	)throws Exception
	{
		boolean					lb_resp;
		SolicitudProcedimiento	lsp_solicitud;

		/* Obtener la solicitud a visualizar */
		lsp_solicitud = new SolicitudProcedimiento();
		lsp_solicitud.setInstitucion(usuario.getCodigoInstitucion());
		lsp_solicitud.valANulacion(ac_con,forma.getNumeroSolicitud());
		
		forma.setValAnulacion(lsp_solicitud.valANulacion(ac_con,forma.getNumeroSolicitud()));

		
		lb_resp =
			lsp_solicitud.cargarSolicitudProcedimiento(ac_con, forma.getNumeroSolicitud() );
			

		if(lb_resp)
		{
			/* Establecer los datos en el formulario para su posterior visulización */
			forma.reset();
			forma.setCentroCostoSolicitante(
				lsp_solicitud.getCentroCostoSolicitante().getNombre()
			);
			//forma.setCodigoEstadoFacturacion(lsp_solicitud.getEstadoFacturacion().getCodigo() );
			forma.setCodigoEstadoHistoriaClinica(
				lsp_solicitud.getEstadoHistoriaClinica().getCodigo()
			);
			forma.setComentario(lsp_solicitud.getComentario());
			forma.setFechaGrabacion(lsp_solicitud.getFechaGrabacion() );
			forma.setFechaSolicitud(lsp_solicitud.getFechaSolicitud() );
			forma.setHoraGrabacion(lsp_solicitud.getHoraGrabacion() );
			forma.setHoraSolicitud(lsp_solicitud.getHoraSolicitud() );
			forma.setMotivoAnulacion(
				lsp_solicitud.getMotivoAnulacion()
			);
			forma.setNombreCentroCostoSolicitado(
				lsp_solicitud.getCentroCostoSolicitado().getNombre()
			);
			forma.setNombreEspecialidadSolicitante(
				lsp_solicitud.getEspecialidadSolicitante().getNombre()
			);
			forma.setNombreOcupacionSolicitada(
				lsp_solicitud.getOcupacionSolicitado().getNombre()
			);
			
			forma.setNumeroServicios(1);
			forma.setNumeroSolicitud(lsp_solicitud.getNumeroSolicitud() );
			forma.setOtroServicio(lsp_solicitud.getNombreOtros() );
			forma.setUrgenteOtros(lsp_solicitud.isUrgente() );
			forma.setValores(
				"codigo_0", new Integer(lsp_solicitud.getCodigoServicioSolicitado() )
			);
			forma.setValores(
				"especialidad_0", lsp_solicitud.getEspecialidadSolicitada().getNombre()
			);
			forma.setValores("nombre_0", lsp_solicitud.getNombreServicioSolicitado() );
			forma.setValores("urgente_0", new Boolean(lsp_solicitud.isUrgente() ) );

			///////////////////////////////////////////////////////////////////////////////////
			//portatil
			forma.setValores("portatil_0", lsp_solicitud.getPortatil());
			
			//motivo anulacion portatil
			forma.setValores("motivoanulport_0", lsp_solicitud.getMotivoAnulacionPort());
						
			if (lsp_solicitud.getPortatil().equals(ConstantesBD.codigoNuncaValido+""))
			{
				forma.setValores("portatilCheckBox_0", false);
				forma.setPortatilClone(false);
				if (!lsp_solicitud.getMotivoAnulacionPort().equals(""))
					forma.setCapaMotivoAnulacion(true);
				else
					forma.setCapaMotivoAnulacion(false);
				
				//portatil
				forma.setValores("portatil_0", lsp_solicitud.obtenerPortatilServicio(ac_con, lsp_solicitud.getCodigoServicioSolicitado()));

			}
			else
			{
				forma.setValores("portatilCheckBox_0", true);
				forma.setPortatilClone(true);
				forma.setCapaMotivoAnulacion(false);
			}
			///////////////////////////////////////////////////////////////////////////////////
			
			forma.setFrecuencia(lsp_solicitud.getFrecuencia());
			forma.setTipoFrecuencia(lsp_solicitud.getTipoFrecuencia());
			forma.setMultiple(lsp_solicitud.getMultiple());
			forma.setValores("finalidad_0", lsp_solicitud.getNombreFinalidad() );
			forma.setFinalidad(lsp_solicitud.getFinalidad());
			forma.setNombreFinalidad(lsp_solicitud.getNombreFinalidad());
			forma.setJustificacionSolicitud(lsp_solicitud.getJustificacionSolicitud());
			
			cargarServiciosArticulosIncluidos(ac_con, forma, lsp_solicitud, usuario);
			
		}

		return lb_resp;
	}

	/**
	 * 
	 * @param ac_con
	 * @param forma
	 * @param solicitud 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarServiciosArticulosIncluidos(Connection con,SolicitarForm forma, SolicitudProcedimiento solicitud, UsuarioBasico usuario) 
	{		
		//segun documentacion (2008-09-24) los flujos de ordenes amb y pyp no deben cargar los servicios incluidos
		il_logger.info("codigo servicio->"+solicitud.getCodigoServicioSolicitado());
			
		il_logger.info("\n\n*****************************CARGAR INFORMACION DE LOS SERVICIOS Y ARTICULOS INCLUIDOS**************************************");
			
		forma.setServiciosIncluidos(
				RespuestaProcedimientos.cargarServiciosIncluidosSolicitud(con, solicitud.getNumeroSolicitud()+"", usuario.getCodigoInstitucionInt()));
		
		Utilidades.imprimirMapa(forma.getServiciosIncluidos());
			
		forma.setArticulosIncluidos(RespuestaProcedimientos.cargarArticulosIncluidosSolicitud(con, solicitud.getNumeroSolicitud()+""));
				
		//Carga el Dto de Solicitud de Procedimientos
		forma.setArrayArticuloIncluidoDto(RespuestaProcedimientos.cargarArticulosIncluidosSolicitudDto(con,solicitud.getNumeroSolicitud()+""));		
		forma.setArticulosIncluidos("abrir_seccion_art_incluidos",ConstantesBD.acronimoSi);
		forma.setJustificacionMap(new HashMap());
		forma.setHiddens("");		
		
		Utilidades.imprimirMapa(forma.getArticulosIncluidos());
		il_logger.info("\n\n*****************************FINNNNNNN CARGAR INFORMACION DE LOS SERVICIOS Y ARTICULOS INCLUIDOS**************************************");
	}


	/**
	* Cerrar una conexión con una fuente de datos
	* @param ac_con Conexión con la fuente de datos a cerrar
	* @throws SQLException
	*/
	private void cerrarConexion(Connection ac_con)throws SQLException
	{
		if(ac_con != null && !ac_con.isClosed() )
			UtilidadBD.closeConnection(ac_con);
			
	}

	/**
	* Cerrar una conexión con una fuente de datos
	* @param ac_con					Conexión con la fuente de datos a cerrar
	* @param ab_terminarTransaccion	Indica si antes de cerrar la conexión de datos se debe cerrar la
	*								transacción
	* @param ab_transaccionExitosa	Indica como se debe terminar la transaccion
	* @throws SQLException
	*/
	private void cerrarConexion(
		Connection	ac_con,
		boolean		ab_terminarTransaccion,
		boolean		ab_transaccionExitosa
	)throws SQLException
	{
		if(ac_con != null && !ac_con.isClosed() )
		{
			if(ab_terminarTransaccion)
			{
				DaoFactory	ldf_df;

				/* Obtener una instacia del onjeto de acceso a base de datos */
				ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD") );

				if(ab_transaccionExitosa)
					ldf_df.endTransaction(ac_con);
				else
					ldf_df.abortTransaction(ac_con);
			}
			UtilidadBD.closeConnection(ac_con);
		}
	}

	/** Retorna la página de error, con un mensaje para el usuario */
	private ActionForward error(
		ActionMapping		aam_mapping,
		HttpServletRequest	ahsr_request,
		String				ls_error,
		boolean				lb_parametrizado
	)
	{
		/* Determinar que como se debe visualizar el error */
		ahsr_request.setAttribute(
			lb_parametrizado ? "codigoDescripcionError" : "descripcionError",
			ls_error
		);
		
		/* Obtener la página de errores */
		return aam_mapping.findForward("paginaError");
	}

	/**
	 * Actualiza en la fuente de datos la solicitud de procedimiento
	 * 
	 * @param aam_mapping
	 * @param asf_form
	 * @param ahsr_request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	private ActionForward guardarModificacion(ActionMapping		aam_mapping,SolicitarForm		asf_form,
		HttpServletRequest	ahsr_request)throws Exception
	{
		Connection	lc_con	= null;
		String		ls_error= null;
		boolean cambio=false;
		boolean ingresarAlertaEnfermeria = false;
		String logAnt="",logDes="";
		ActionMessages errores = new ActionMessages();
		try{
			/* Validar si el usuario y el paciente cargado cumplen los requisitos necesarios */
			ls_error = validar(ahsr_request, lc_con = abrirConexion(true), ii_MEDICO_TRATANTE_ADJUNTO);
	
			if(ls_error == null)
			{
				UsuarioBasico usuario=(UsuarioBasico)ahsr_request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente=(PersonaBasica)ahsr_request.getSession().getAttribute("pacienteActivo");
				
				boolean					lb_resp;
				SolicitudProcedimiento	lsp_solicitud;
	
				//ObjetoReferencia logReferencia=new ObjetoReferencia();
				
				lsp_solicitud	= new SolicitudProcedimiento();				//-mundo	
				lb_resp			= llenarSolicitud(asf_form, ahsr_request, lsp_solicitud, lc_con); //-Informacion del mundo
	
	
				/* Determinar si los estados de la solicitud son válidos para su modificación */
				if(	(lsp_solicitud.getEstadoHistoriaClinica().getCodigo() != ConstantesBD.codigoEstadoHCSolicitada 
						&& lsp_solicitud.getEstadoHistoriaClinica().getCodigo() != ConstantesBD.codigoEstadoHCTomaDeMuestra)  
							&& !(asf_form.getMultiple() && (asf_form.getCodigoEstadoHistoriaClinica() == ConstantesBD.codigoEstadoHCRespondida || asf_form.getCodigoEstadoHistoriaClinica() == ConstantesBD.codigoEstadoHCInterpretada)))
				{
					/* Cerrar el acceso a base de datos */
					cerrarConexion(lc_con, true, false);
	
					/* El estado de historia clínica de la solicitud debe ser 'Solicitada' */
						return error(aam_mapping, ahsr_request,"error.validacionessolicitud.modificar.falloCasoSolicitado",true);
				}
				
				//ya no se debe tener en cuenta el estado de facturacion
				/*else if(lsp_solicitud.getEstadoFacturacion().getCodigo() != ConstantesBD.codigoEstadoFCargada && lsp_solicitud.getEstadoFacturacion().getCodigo() != ConstantesBD.codigoEstadoFPendiente && asf_form.getCodigoEstadoFacturacion() != ConstantesBD.codigoEstadoFExterno && !asf_form.getMultiple())
				{
					/* Cerrar el acceso a base de datos 
					cerrarConexion(lc_con, true, false);
	
					/* El estado de facturación de la solicitud debe ser 'Pendiente'  o 'Externo'
					return
							error(
								aam_mapping,
								ahsr_request,
								"error.validacionessolicitud.modificar.falloCasoPendiente",
								true
							);
				}*/
				else
				{
					/* Guardar los cambios de la solicitud */
	
					
					/*---------------------------------------------------------------
					 * ########## SE VERIFICA SI SE ANULA EL PORTATIL ##############
					 ---------------------------------------------------------------*/
					System.out.print("\n -------------------------------------------------------------- \n");
					System.out.print("\n motivo anulacion --> "+asf_form.getValores("motivoanulport_0"));
					System.out.print("\n portatil clone --> "+asf_form.isPortatilClone());
					System.out.print("\n portatil checkbox --> "+asf_form.getValores("portatilCheckBox_0")+"");
					
					if (UtilidadCadena.noEsVacio(asf_form.getValores("portatil_0")+"") 
							&& !(asf_form.getValores("portatil_0")+"").equals(ConstantesBD.codigoNuncaValido+"")
								&&  lb_resp 
										&& (asf_form.getValores("motivoAnu_0")+"").length()>0  
											&& !UtilidadTexto.getBoolean(asf_form.getValores("portatilCheckBox_0")+""))
					{
						HashMap datos = new HashMap ();
						datos.put("portatil", ConstantesBD.codigoNuncaValido);
						datos.put("motAnuPort", asf_form.getValores("motivoanulport_0"));
						datos.put("numeroSolicitud", lsp_solicitud.getNumeroSolicitud());
						
						lb_resp=lsp_solicitud.anularAprobarPortatil(lc_con, datos);
						
						//se borra el cargo generado al portatil y la solicitud subcuenta
						Cargos.borrarCargoPortatil(lc_con, lsp_solicitud.getNumeroSolicitud());
						//Se elimina el IF por la MT 6021, un procedimiento que no tenga asignacion de portatil no tiene porque arrojar error
						/*if(!Cargos.borrarCargoPortatil(lc_con, lsp_solicitud.getNumeroSolicitud()))
						{
							UtilidadBD.abortarTransaccion(lc_con);
							UtilidadBD.closeConnection(lc_con);
							return error(aam_mapping, ahsr_request, "error.validacionessolicitud.modificar.falloCasoSolicitado", true);
						}*/
						Log4JManager.info("\n ELIMINADO CARGO PORTATIL***********");
	
						ingresarAlertaEnfermeria = true;
					}
					
					/*---------------------------------------------------------------
					 * ################# FIN ANULACION  DEL PORTATIL ################
					 ---------------------------------------------------------------*/
					
					/*---------------------------------------------------------------
					 * ########## SE VERIFICA SI SE APRUEBA EL PORTATIL ##############
					 ---------------------------------------------------------------*/
					if (lb_resp && !asf_form.isPortatilClone() && UtilidadTexto.getBoolean(asf_form.getValores("portatilCheckBox_0")+""))
					{
						HashMap datos = new HashMap ();
						datos.put("portatil", asf_form.getValores("portatil_0"));
						datos.put("motAnuPort", "");
						datos.put("numeroSolicitud", lsp_solicitud.getNumeroSolicitud());
						
						lb_resp=lsp_solicitud.anularAprobarPortatil(lc_con, datos);
						lsp_solicitud.setPortatil(asf_form.getValores("portatil_0")+"");
						
						boolean dejarPendiente=!UtilidadValidacion.esServicioViaIngresoCargoSolicitud(lc_con, paciente.getCodigoUltimaViaIngreso()+"", lsp_solicitud.getPortatil(), usuario.getCodigoInstitucion());
						
						Cargos cargos= new Cargos();
				    	boolean inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(		lc_con, 
							    																				usuario, 
							    																				paciente, 
							    																				dejarPendiente/*dejarPendiente*/, 
							    																				lsp_solicitud.getNumeroSolicitud(), 
							    																				ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
							    																				paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
							    																				lsp_solicitud.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
							    																				lsp_solicitud.getPortatilInt()/*codigoServicioOPCIONAL*/, 
							    																				1 /*cantidadServicioOPCIONAL*/, 
							    																				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
							    																				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
							    																				/* "" -- numeroAutorizacionOPCIONAL*/
							    																				ConstantesBD.acronimoSi/*esPortatil*/,
							    																				false,lsp_solicitud.getFechaSolicitud(),"");
						
				    	if(!inserto)
						{
				    		UtilidadBD.abortarTransaccion(lc_con);
							UtilidadBD.closeConnection(lc_con);
							return error(aam_mapping, ahsr_request, "error.validacionessolicitud.modificar.falloCasoSolicitado", true);
						}
						Log4JManager.info("\n INSERTOOOO CARGO PORTATIL***********");
				    	
						ingresarAlertaEnfermeria = true;
					}
					
					
					/*---------------------------------------------------------------
					 * ############### FIN APROBACION DEL PORTATIL ##################
					 ---------------------------------------------------------------*/
					
					
					/*
						La solicitud va a ser anulada. Anular la solicitud y obviar los demás cambios
					*/
					if(lb_resp && lsp_solicitud.getMotivoAnulacion().length() > 0)
					{
							/**FIXME INICIO ANULACION SOLICITUD Y AUTORIZACION----------------------------------------------*/
							//Se anula la solicitud
							//lb_resp = lsp_solicitud.anularSolicitudTransaccional(lc_con, "") != -1;
									
							//Validaciones para la solicitud que se anula, si esta asociada a una autorizacion
							cargarInfoParaAnulacionAutorizacion(lsp_solicitud,usuario,errores);
							/**FIN ANULACION SOLICITUD Y AUTORIZACION-------------------------------------------------------*/
							
							if ( !lb_resp ){ 
							return ComunAction.accionSalirCasoError(aam_mapping, ahsr_request, lc_con, il_logger, "SolicitudProcedimientosAnulada", "error.validacionessolicitud.interpretar.solicitudYaAnulada", true);
							}else
						{
							//****************LLAMADO A INTERFAZ DE LABORATORIOS***************************
							HashMap infoInterfaz = new HashMap();
							infoInterfaz.put("numeroDocumento",lsp_solicitud.getNumeroDocumento()+"");
							infoInterfaz.put("fechaSolicitud",lsp_solicitud.getFechaSolicitud());
							infoInterfaz.put("codigoMedico",lsp_solicitud.getCodigoMedicoSolicitante()+"");
							infoInterfaz.put("nombreMedico",UtilidadValidacion.obtenerNombrePersona(lc_con,lsp_solicitud.getCodigoMedicoSolicitante()));
							infoInterfaz.put("institucion",usuario.getCodigoInstitucion());
							infoInterfaz.put("numeroSolicitud_0",lsp_solicitud.getNumeroSolicitud()+"");
							infoInterfaz.put("codigoCUPS_0",Utilidades.obtenerCodigoPropietarioServicio(lc_con,lsp_solicitud.getCodigoServicioSolicitado()+"",ConstantesBD.codigoTarifarioCups));
							infoInterfaz.put("estado_0",ConstantesBD.codigoEstadoHCAnulada+"");
							infoInterfaz.put("centroCosto_0",lsp_solicitud.getCentroCostoSolicitado().getCodigo()+"");
							infoInterfaz.put("urgente_0",lsp_solicitud.getUrgente()+"");
							Cama cama = new Cama();
							cama.cargarCama(lc_con,paciente.getCodigoCama()+"");
							infoInterfaz.put("numeroCama",cama.getNumeroCama());
							infoInterfaz.put("numRegistros","1");
							InterfazLaboratorios.anulacionRegistroArchivo(infoInterfaz,paciente,ValoresPorDefecto.getCliente());
							//**************************************************************************
							ingresarAlertaEnfermeria = true;
						}
					}
					else
					{
						/* Adicionar un comentario a la solicitud de procedimiento */
						if( lb_resp && lsp_solicitud.isAdicionarComentario() ) {
							lb_resp = lsp_solicitud.modificarSolicitudProcedimiento(lc_con, usuario);
						}
						if(!lsp_solicitud.getComentario().equals(lsp_solicitud.getComentarioAdicional())) {
							ingresarAlertaEnfermeria = true;
						}
						/* Adicionar un nuevo comentario en la justificación de la solicitud*/
						if(!asf_form.getJustificacionSolicitudNueva().trim().equals(""))
						{
							Solicitud.actualizarJustificacionSolicitud(
									lc_con,
									UtilidadTexto.agregarTextoAObservacionFechaGrabacion(asf_form.getJustificacionSolicitud(),asf_form.getJustificacionSolicitudNueva(),usuario,false),
									lsp_solicitud.getNumeroSolicitud()+"");
							ingresarAlertaEnfermeria = true;
						}
						
						/* Actualizar el número de autorización de la solicitud */
						/*
						if(lb_resp)
							lb_resp = lsp_solicitud.modificarNumeroAutorizacion(lc_con);	
						*/					
						///hacemos la modificacion de las cantidades de los articulos incluidos
						if(lb_resp)
						{
							for(int w=0; w<Utilidades.convertirAEntero(asf_form.getArticulosIncluidos("numRegistros")+"");w++)
							{
								int cantidad= Utilidades.convertirAEntero(asf_form.getArticulosIncluidos("cantidad_"+w)+"");
								int articulo= Utilidades.convertirAEntero(asf_form.getArticulosIncluidos("articulo_"+w)+"");
								int solicitudPpal= Utilidades.convertirAEntero(asf_form.getArticulosIncluidos("solicitudPpal_"+w)+"");
								
								lb_resp= Servicios_ArticulosIncluidosEnOtrosProcedimientos.modificarCantidadArticulosIncluidosSolicitudProcedimientos(lc_con, solicitudPpal, articulo, cantidad, usuario.getLoginUsuario());
							}
						}
					}
					
					//----Cambio Montoya
					//-Verificar si Hubo Cambios en los datos (de ser asi se genera el log) 
					//- Finalizar - Anular - Comentarios 
	
					logAnt="\n ==== MODIFICACION EN SOLICITUD DE PROCEDIMIENTOS ===== ";
					logAnt = logAnt + "\n  Numero de la Orden Medica  [ " + asf_form.getConsecutivoOrdenesMedicas() +" ]";
					logAnt = logAnt + "\n  Codigo Procedimiento Modificado [ " + lsp_solicitud.getCodigoServicioSolicitado()  +" ]";								
					logDes="\n ==== INFORMACION DESPUES DE LA MODIFICACION ===== ";
	
					//if ( !asf_form.getComentario().trim().equals(lsp_solicitud.getComentario()) )
					
					if ( ( !asf_form.getComentario().trim().equals(asf_form.getComentarioAdicional().trim()) && lsp_solicitud.getComentario().trim().equals(""))
						 || 	
						 (	!asf_form.getComentario().trim().equals(lsp_solicitud.getComentario()) )  ) 
						{ 
								  logAnt = logAnt + "\n Comentario [ " + lsp_solicitud.getComentario() + " ] "; 
								  logDes = logDes + "\n Comentario [ " + asf_form.getComentario()  + " ] "; //lsp_solicitud.getComentario();
						  cambio = true;
						}
					
					if ( asf_form.getFinalizar() )  
						{
						  logAnt = logAnt + "\n Solicitud de Procedimiento Finalizado ";
						  logDes = logDes + "\n Solicitud de Procedimiento Sin Finalizar "; 
						  lsp_solicitud.finalizarSolicitudMultiple(lc_con, lsp_solicitud.getNumeroSolicitud());
						  cambio = true;
						}
					
					
					if ( asf_form.isAnularSolicitud() )  
						{
						  logAnt = logAnt +  "\n Solicitud de Procedimiento Sin Anular ";
						  logDes = logDes + "\n Solicitud de Procedimiento Anulada";
						  
						  
						  if (!asf_form.getMotivoAnulacion().trim().equals("")){
						    logDes = logDes + "\n Motivo Anulación " + asf_form.getMotivoAnulacion();
						  }
						  
						  cambio = true;
						}
					
					
					if (cambio)
					{
						logAnt = logAnt + logDes; 	
						LogsAxioma.enviarLog(ConstantesBD.logComentarioProcedimientoCodigo, logAnt, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
					}
	
					/**
					 * Se inserta el registro de alerta para registro de enfermería MT-3438
					 */
					if (ingresarAlertaEnfermeria && UtilidadValidacion.esMedico(usuario).equals("") && 
							lsp_solicitud.getEstadoHistoriaClinica().getCodigo() == ConstantesBD.codigoEstadoHCSolicitada) {
						OrdenMedica ordenMedica = new OrdenMedica();
						ordenMedica.registrarAlertaOrdenMedica(lc_con, 
								ConstantesBD.seccionProcedimientos, 
								new Long(paciente.getCodigoCuenta()), usuario.getLoginUsuario());
						
					}
					//----Cambio Montoya
					
					/* Cerrar el acceso a base de datos */
					cerrarConexion(lc_con, true, lb_resp);
	
					/* Determinar el éxito de la operación */
					if(lb_resp){
						saveErrors(ahsr_request, errores);
						return resumen(aam_mapping, asf_form, ahsr_request,usuario);
					}else{
						return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
					}
				}
			}
			else
			{
				/* Alguna de las validaciones no se cumplió */
				cerrarConexion(lc_con, true, false);
				return error(aam_mapping, ahsr_request, ls_error, true);
			}
		}catch(IPSException ipse){
			this.cerrarConexion(lc_con, true, false);
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString(),ipse.getParamsMsg()));
			if(!errores.isEmpty()){
				saveErrors(ahsr_request, errores);
			}
			return aam_mapping.findForward("modificar");
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}finally{
			this.cerrarConexion(lc_con, true, false);
			this.cerrarConexion(lc_con);
		}
		if(!errores.isEmpty()){
			saveErrors(ahsr_request, errores);
		}
		return error(aam_mapping, ahsr_request, ls_error, true);
	}

	/** Cargar una solicitud de procedimiento desde una funente de datos */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean llenarSolicitud(
		SolicitarForm			asf_form,
		HttpServletRequest		ahsr_request,
		SolicitudProcedimiento	asp_solicitud,
		Connection				ac_con
	)throws Exception
	{
		UsuarioBasico medico=(UsuarioBasico)ahsr_request.getSession().getAttribute("usuarioBasico");
		boolean lb_resp;
		asp_solicitud.setInstitucion(medico.getCodigoInstitucion());
		/* Obtener la solicitud */
		lb_resp = asp_solicitud.cargarSolicitudProcedimiento(ac_con, asf_form.getNumeroSolicitud() );

		/* Actualizar los datos de la solicitud con los datos del formulario */
		if(lb_resp)
		{
			//String log="\n-========= COMENTARIO ANTES DE LA MODIFICACIÓN ==============-\n"+
				//	   " \n Comentario  " + asp_solicitud.getComentario();
			
			
			//logReferencia.setStringReferencia(log);
			
			String			ls_comentario;
			UsuarioBasico	lub_medico;

			lub_medico = (UsuarioBasico)ahsr_request.getSession().getAttribute("usuarioBasico");
			ls_comentario = asf_form.getComentario();
			
			//Añadir un comentario cuando se modifica una solicitud
			asp_solicitud.setComentarioAdicional(ls_comentario);
			asp_solicitud.setJustificacionSolicitudNueva(asf_form.getJustificacionSolicitudNueva());
			//asp_solicitud.setNumeroAutorizacion("");

			if(	asf_form.isAnularSolicitud() )
			{
				asp_solicitud.setCodigoMedicoAnulacion(lub_medico.getCodigoPersona() );
				asp_solicitud.setMotivoAnulacion(asf_form.getMotivoAnulacion() );
				

				
				
				///cambio en pyp y ordenes ambulatorias////
		        if(Utilidades.esSolicitudPYP(ac_con,asf_form.getNumeroSolicitud()))
		        {
		        	String codActProg=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(ac_con,asf_form.getNumeroSolicitud());
		        	if(Integer.parseInt(Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(ac_con,asf_form.getNumeroSolicitud()+""))>0)
		        	{
		        		Utilidades.actualizarEstadoActividadProgramaPypPaciente(ac_con,codActProg,ConstantesBD.codigoEstadoProgramaPYPProgramado,lub_medico.getLoginUsuario(),"");
		        		Utilidades.asignarSolicitudToActividadPYP(ac_con,Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(ac_con,asf_form.getNumeroSolicitud()+""),asf_form.getNumeroSolicitud()+"");
		        	}
		        	else
		        	{
		        		Utilidades.actualizarEstadoActividadProgramaPypPaciente(ac_con,codActProg,ConstantesBD.codigoEstadoProgramaPYPCancelado,lub_medico.getLoginUsuario(),"SOLICITUD CANCELADA");
		        	}
		        }
		        //si la solicitud es de orden ambulatoria
		        String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(ac_con,asf_form.getNumeroSolicitud()+"");
		        if(Integer.parseInt(codigoOrden)>0)
		    	{
		        	OrdenesAmbulatorias.actualizarEstadoOrdenAmbulatoria(ac_con,codigoOrden,ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
		        	HashMap campos=new HashMap();
		        	campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
		        	campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		        	campos.put("numeroOrden",codigoOrden);
		        	OrdenesAmbulatorias.actualizarSolicitudEnOrdenAmbulatoria(ac_con,campos);
		    	}
		        ///fin cambio en pyp y ordenes ambulatorias////
			}
		}

		return lb_resp;
	}

	/** Muestra una solicitud de procedimiento en modo de modificación */
	private ActionForward prepararModificacion(
		ActionMapping		aam_mapping,
		SolicitarForm		asf_form,
		HttpServletRequest	ahsr_request
	)throws Exception
	{
		
		il_logger.warn("\n\n\n\n **PREPARAR MODIFICACION!!!");

		UsuarioBasico usuario=(UsuarioBasico)ahsr_request.getSession().getAttribute("usuarioBasico");
		Connection	lc_con;
		String		ls_error;
		
		/* Validar si el usuario y el paciente cargado cumplen los requisitos necesarios */
		ls_error = validar(ahsr_request, lc_con = abrirConexion(false), ii_MEDICO_TRATANTE_ADJUNTO);

		if(ls_error == null)
		{
			boolean lb_resp;

			lb_resp = cargarSolicitud(asf_form, lc_con, usuario);
			
			/* Cerrar el acceso a base de datos */
			cerrarConexion(lc_con);

			/* Determinar si los estados de la solicitud son válidos para su modificación */
			if(lb_resp)
			{
				
				/* El estado de historia clínica de la solicitud debe ser 'Solicitada' */
				if((asf_form.getCodigoEstadoHistoriaClinica() != ConstantesBD.codigoEstadoHCSolicitada && asf_form.getCodigoEstadoHistoriaClinica() != ConstantesBD.codigoEstadoHCTomaDeMuestra) && !(asf_form.getMultiple() && (asf_form.getCodigoEstadoHistoriaClinica() == ConstantesBD.codigoEstadoHCRespondida || asf_form.getCodigoEstadoHistoriaClinica() == ConstantesBD.codigoEstadoHCInterpretada)))
				{	
					return
						error(
							aam_mapping,
							ahsr_request,
							"error.validacionessolicitud.modificar.falloCasoSolicitado",
							true
						);
				}	
				/* El estado de facturación de la solicitud debe ser 'Pendiente' o 'Externo'*/
				/*para ese machetazo q se invento calidad no debe verificar los estados
				else if((asf_form.getCodigoEstadoFacturacion() != ConstantesBD.codigoEstadoFPendiente && asf_form.getCodigoEstadoFacturacion() != ConstantesBD.codigoEstadoFExterno) && !asf_form.getMultiple())
				{	
					return
						error(
							aam_mapping,
							ahsr_request,
							"error.validacionessolicitud.modificar.falloCasoPendiente",
							true
						);
				}*/	
				else
				{
					asf_form.setEstado("guardarModificacion");
					return aam_mapping.findForward("modificar");
				}
			}
			else
				return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
		}
		else
		{
			// Alguna de las validaciones no se cumplió 
			cerrarConexion(lc_con);
			return error(aam_mapping, ahsr_request, ls_error, true);
		}
	}

	/** Muestra el resumen de una solicitud de procedimiento */
	private ActionForward resumen(
		ActionMapping		aam_mapping,
		SolicitarForm		asf_form,
		HttpServletRequest	ahsr_request,
		UsuarioBasico usuario
	)throws Exception
	{
		Connection	lc_con;		
		lc_con = abrirConexion(false);
		
		/*
		 * ************************************ OJO ************************************
		 * En todos los listados hay validaciones de usuarios, pacientes, cuentas, estados, etc
		 * Así que en este punto creo que no son necesarias las validaciones ya que es
		 * sólo resumen, de esta manera el resumen de atenciones no tiene
		 * problema con las cuentas que ya están cerradas
		 */
		
		/* Validar si el usuario y el paciente cargado cumplen los requisitos necesarios */
		//if( (ls_error = validar(ahsr_request, lc_con = abrirConexion(false), ii_MEDICO) ) == null)
		{
			boolean lb_resp;
			
			lb_resp = cargarSolicitud(asf_form, lc_con, usuario);

			/* Cerrar el acceso a base de datos */
			cerrarConexion(lc_con);

			/* Determinar el éxito de la operación */
			if(lb_resp)		
				return aam_mapping.findForward("resumen");
			else
				return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
		}
		/*else
		{
			/* Alguna de las validaciones no se cumplió */
		/*	cerrarConexion(lc_con);
			return error(aam_mapping, ahsr_request, ls_error, true);
		}*/
	}

	/**
	* Realiza validaciones generales sobre el usuario de la aplicación
	* @param ai_validacionMedico	Tipo de validación a realiar sobre el médico
	* @return Codigop de error si no se cumple alguna validación. Null de de contrario
	*/
	public String validar(
		HttpServletRequest	ahsr_request,
		Connection			ac_con,
		int					ai_validacionMedico
	)throws Exception
	{
		HttpSession		lhs_sesion;
		PersonaBasica	lpb_paciente;
		String			ls_error;
		UsuarioBasico	lub_medico;

		lhs_sesion	= ahsr_request.getSession();
		ls_error	= null;

		/* Validar acceso a la fuente de datos */
		if(ac_con == null)
		{
			/* Error al intentar obtener una conexión a la fuente de datos */
			if(il_logger.isDebugEnabled() )
				il_logger.debug("No se pudo obtener conexión a la fuente de datos");

			ls_error = "errors.problemasBd";
		}
		/* Determinar si existe un usuario válido */
		else if( (lub_medico = (UsuarioBasico)lhs_sesion.getAttribute("usuarioBasico") ) == null)
		{
			il_logger.warn("Usuario no cargado");
			ls_error = "errors.usuario.noCargado";
		}
		/* Validar que el usuario sea médico */
		else if(!UtilidadValidacion.esProfesionalSalud(lub_medico) )
		{
			il_logger.warn("Usuario no es es profesional de la salud");
			ls_error = "errors.usuario.noAutorizado";
		}
		/* Validar que exista un paciente cargado en sesión */
		else if(
			(lpb_paciente = (PersonaBasica)lhs_sesion.getAttribute("pacienteActivo") ) == null ||
			lpb_paciente.getTipoIdentificacionPersona().equals("")
		)
		{
			il_logger.warn("El paciente no es válido");
			ls_error = "errors.paciente.noCargado";
		}
		/* Validar que la cuenta del paciente sea válida */
		else if(lpb_paciente.getCodigoCuenta() < 1)
		{
			il_logger.warn("El paciente no tiene la cuenta abierta");
			ls_error = "errors.paciente.cuentaNoAbierta";
		}
		else if(lpb_paciente.getCodigoArea()==ConstantesBD.codigoCentroCostoAmbulatorios || lpb_paciente.getCodigoArea()==ConstantesBD.codigoCentroCostoConsultaExterna)
		{
			ls_error = UtilidadValidacion.esMedico(lub_medico); 
        	if(!ls_error.equals(""))
        		return ls_error;
        	else
        		ls_error = null;
		}
		/* Realizar validaciones adicionales sobre el médico */
		else if(ai_validacionMedico != ii_MEDICO)
		{
			/* El médico debe ser tratante de la cuenta */
			String mensaje = UtilidadValidacion.esMedicoTratante(ac_con, lub_medico, lpb_paciente);
			if(
				ai_validacionMedico == ii_MEDICO_TRATANTE &&
				!mensaje.equals("")
			)
			{
				il_logger.warn("El médico no pertence al grupo tratante del paciente");
				ls_error = mensaje;
			}
			/* El médico deber ser adjunto a la cuenta */
			else if(
				ai_validacionMedico == ii_MEDICO_ADJUNTO &&
				!UtilidadValidacion.esAdjuntoCuenta(
					ac_con, lpb_paciente.getCodigoCuenta(), lub_medico.getLoginUsuario()
				)
			)
			{
				il_logger.warn("El médico no pertence al grupo adjunto del paciente");
				ls_error = "error.validacionessolicitud.medicoNoAdjunto";
			}
			/* El médico debe ser adjunto o tratante de la cuenta */
			else if(
				ai_validacionMedico == ii_MEDICO_TRATANTE_ADJUNTO &&
				!UtilidadValidacion.esMedicoTratante(ac_con, lub_medico, lpb_paciente).equals("") &&
				!UtilidadValidacion.esAdjuntoCuenta(
					ac_con, lpb_paciente.getCodigoCuenta(), lub_medico.getLoginUsuario()
				)
			)
			{
				il_logger.warn("El médico no pertence al grupo tratante o adjunto del paciente");
				String mensajeError = UtilidadValidacion.esMedicoTratante(ac_con, lub_medico, lpb_paciente);
				//se verifica si no se ha definido la ocupación médico especialista y/o general
				// de parámetros generales
				if(mensajeError.equals("errors.noOcupacionMedica"))
					ls_error = mensajeError;
				else
					ls_error = "error.validacionessolicitud.medicoNoTratanteNiAdjunto";
			}
		}

		return ls_error;
	}
	
	/**
	 * Metodo que se encarga de enviar los datos necesarios para validar la anulacion de la
	 * autorizacion
	 * 
	 * @param mundoInterconsulta
	 * @param usuario
	 * @throws IPSException
	 */
	public void cargarInfoParaAnulacionAutorizacion(SolicitudProcedimiento mundoProcedimiento,
			UsuarioBasico usuario, ActionMessages errores)throws IPSException
	{
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		AnulacionAutorizacionSolicitudDto anulacionDto	= null;
		ManejoPacienteFacade manejoPacienteFacade		= null;
		Medicos medicos	= null;
		try{
			anulacionDto= new AnulacionAutorizacionSolicitudDto();
			anulacionDto.setMotivoAnulacion(mundoProcedimiento.getMotivoAnulacion());
			anulacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
			anulacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
			medicos		= new Medicos(); 
			medicos.setCodigoMedico(usuario.getCodigoPersona());
			anulacionDto.setMedicoAnulacion(medicos);
			anulacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
			anulacionDto.setNumeroSolicitud(mundoProcedimiento.getNumeroSolicitud());
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			listaAutorizacionCapitacion	= manejoPacienteFacade.validarAnulacionAutorizacionCapitaSolictud(anulacionDto, 
					ConstantesBD.claseOrdenOrdenMedica,ConstantesBD.codigoTipoSolicitudProcedimiento,null,usuario.getCodigoInstitucionInt());
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
			{	//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
			
		}catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
}