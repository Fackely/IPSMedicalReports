package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
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

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.AutorizacionesForm;
import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.pdf.SolicitudAutorizacionPdf;
import com.servinte.axioma.fwk.exception.IPSException;

/** * 
 * ** Nota: Se solicita para proximas modificaciones, guardar la estructura del codigo y comentar debidamente los cambios y nuevas lineas.
 */
public class AutorizacionesAction extends Action
{
	Logger logger = Logger.getLogger(AutorizacionesAction.class);	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof AutorizacionesForm) 
			{			 

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//ActionErrors
				ActionErrors errores = new ActionErrors();

				AutorizacionesForm forma = (AutorizacionesForm)form;
				String estado = forma.getEstado();

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("menuOperaciones"))
				{
					forma.setIndicadoresMap("mensajeExito","");
					accionMenuOperaciones(con,forma,usuario,request,true);				

					if(!accionEvaluarConveio(con, forma))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("convenios");
					}				 

					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuOperacion");
				}
				else if(estado.equals("volverMenuOperaciones"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuOperacion");
				}
				else if(estado.equals("continuarOperaciones"))
				{
					accionMenuOperaciones(con,forma,usuario,request,false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuOperacion");
				}
				//Opciones para las Autorizaciones de Estanci y de Admision
				else if(estado.equals("cargarCuentasPac"))
				{
					accionCargarCuentaPac(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoCuentas");
				}
				else if(estado.equals("menuOperacionesAdmEst"))
				{
					accionMenuOperacionesAdmEst(con,forma,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuOperacionAdmEst");
				}
				else if(estado.equals("volverMenuOperacionesAE"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuOperacionAdmEst");
				}
				//****************************************************************
				//Opciones de nueva solicitud 
				else if(estado.equals("nuevaSolicitud"))
				{
					if(evaluarSolicitudSubcuenta(con, forma, mapping,false,true))
					{
						UtilidadBD.closeConnection(con);
						forma.setEstado("continuarNuevaSolicitud"); 
						return mapping.findForward("convenios");
					}
					else if(!accionEvaluarFormato(con, forma, request,false))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("menuOperacion");
					}

					accionNuevaSolicitud(con,forma,usuario,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("solicitud");
				}	
				else if(estado.equals("continuarNuevaSolicitud"))
				{
					accionNuevaSolicitud(con,forma,usuario,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("solicitud");
				}
				else if(estado.equals("guardarNuevaSolEnv"))
				{
					if(accionGuardarSolEnv(con,forma,usuario,paciente,request))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("envio");
					}			 
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("solicitud");
					}
				}
				else if(estado.equals("continuarNuevaSolicitudMultiple"))
				{
					accionNuevaSolicitudMult(con,forma,usuario,request,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("solicitudMul");
				}
				else if(estado.equals("nuevaSolicitudMultiple"))
				{
					if(!accionEvaluarFormato(con, forma, request,false))
					{	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("menuOperacion");
					}

					accionNuevaSolicitudMult(con,forma,usuario,request,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("solicitudMul");
				}
				else if(estado.equals("guardarNuevaSolEnvMult"))
				{
					if(accionGuardarSolEnvMul(con,forma,usuario,paciente,request))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("reumenSolicitudMul");
					}			 
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("solicitudMul");
					}				 
				}
				//*****************************************************************
				//Opciones para el Envío
				else if(estado.equals("abrirEnvio"))
				{				 
					accionAbrirEnvio(con,forma,usuario,request);			 
					UtilidadBD.closeConnection(con);

					if(forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoEstancia) || 
							forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoAdmision))
						return mapping.findForward("envioAE");
					else
						return mapping.findForward("envio");
				}			
				else if(estado.equals("guardarEnvioSolEnv"))
				{
					accionGuardarEnvioSolEnv(con,forma,usuario, paciente, request);
					UtilidadBD.closeConnection(con);

					if(forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoEstancia) || 
							forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoAdmision))
						return mapping.findForward("envioAE");					 
					else
						return mapping.findForward("envio");	 
				}
				else if(estado.equals("iniciarAdjuntarArchivos"))
				{
					return accionIniciarAdjuntarArchivos(con,forma,mapping,usuario);
				}
				//*****************************************************************
				//Operaciones para respuesta
				else if(estado.equals("abrirRespuesta"))
				{
					if(evaluarSolicitudSubcuenta(con, forma, mapping,true,false))
					{
						UtilidadBD.closeConnection(con);
						forma.setEstado("continuarAbrirRespuesta"); 
						return mapping.findForward("convenios");
					}

					accionAbrirRespuesta(con,forma,usuario,request,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("respuesta");
				}
				else if(estado.equals("continuarAbrirRespuesta"))
				{
					accionAbrirRespuesta(con,forma,usuario,request,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("respuesta");
				}
				else if(estado.equals("guardarResSol"))
				{
					if(accionGuardarRespuestaSol(con,forma,usuario,paciente,request))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("resumenRespuesta");
					}
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("respuesta");
					}
				}
				else if(estado.equals("iniciarAdjuntarArchivosRes"))
				{
					return accionIniciarAdjuntarArchivosRes(con,forma,mapping,usuario);
				}
				//*****************************************************************
				//Opciones para anular
				else if(estado.equals("abrirAnular"))
				{
					accionAnularRespuestaSol(con,forma,usuario);
					UtilidadBD.closeConnection(con);

					if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
						return mapping.findForward("anulacionRespuestaAE");
					else
						return mapping.findForward("anulacionRespuesta");
				}
				else if(estado.equals("guardarAnulaSol"))
				{
					if(accionGuardarAnularRespSol(con,forma,usuario,request))
					{
						UtilidadBD.closeConnection(con);
						if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
							return mapping.findForward("resumenAnulacionResAE");
						else
							return mapping.findForward("resumenAnulacionRes");
					}
					else
					{
						UtilidadBD.closeConnection(con);
						if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
							return mapping.findForward("anulacionRespuestaAE");
						else
							return mapping.findForward("anulacionRespuesta");
					}
				}
				//************************************************************************
				else if(estado.equals("filtroMedioEnvio"))
				{
					accionFiltrarMediosEnvios(con, forma, response,true,false);				
				} 			 
				else if(estado.equals("filtroMedioEnvioNueva"))
				{
					accionFiltrarMediosEnvios(con, forma, response,true,true);				
				}
				//*************************************************************************
				//Opciones para Consultar
				else if(estado.equals("consultaMenuOperaciones"))
				{
					accionConsultaMenuOperaciones(con,forma,usuario,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuConsulta");
				}
				else if(estado.equals("consultarSolicitud"))
				{				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("conSolicitud");
				}
				else if(estado.equals("consultarEnvio"))
				{				
					UtilidadBD.closeConnection(con);
					return mapping.findForward("conEnvio");
				}
				else if(estado.equals("consultarResp"))
				{				
					UtilidadBD.closeConnection(con);
					return mapping.findForward("conRespuesta");
				}
				else if(estado.equals("consultarAnulacion"))
				{				
					UtilidadBD.closeConnection(con);
					return mapping.findForward("conAnulacion");
				}
				else if(estado.equals("volverMenuConsulta"))
				{				
					UtilidadBD.closeConnection(con);
					return mapping.findForward("menuConsulta");
				}			 
				//*************************************************************************
				//Imprimir
				else if(estado.equals("imprimir"))
				{
					return accionImprimir(con,mapping,usuario,paciente,request, forma);
				}
				else if(estado.equals("calcularFechaIncre"))
				{
					accionEstadoCalcularFechaIncre(con,forma,response);
				}
				//*************************************************************************
				//Opciones de nueva solicitud Admision Estancia
				else if(estado.equals("continuarNuevaSolicitudAE"))
				{ 
					accionNuevaSolicitudAE(con,forma,usuario,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("solicitudAE");
				}
				else if(estado.equals("nuevaSolicitudAE"))
				{
					if(!accionEvaluarFormato(con, forma, request,false))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("menuOperacionAdmEst");
					}

					accionNuevaSolicitudAE(con,forma,usuario,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("solicitudAE");
				}
				else if(estado.equals("guardarNuevaSolEnvAE"))
				{
					if(accionGuardarSolEnvAE(con,forma,usuario,paciente,request))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("envioAE");
					}			 
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("solicitudAE");
					}
				}
				//***************************************************************************
				//Operaciones para respuesta
				else if(estado.equals("abrirRespuestaAE"))
				{
					accionAbrirRespuesta(con,forma,usuario,request,paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("respuestaAE");
				}
				else if(estado.equals("guardarResSolAE"))
				{
					if(accionGuardarRespuestaSol(con,forma,usuario,paciente,request))
					{
						HashMap mapa = procesoEstanciasAutorizacion(con, forma, usuario);
						if(!UtilidadTexto.getBoolean(mapa.get("exito").toString()) && 
								UtilidadTexto.getBoolean(mapa.get("preguntar").toString()))
						{
							UtilidadBD.closeConnection(con);
							return mapping.findForward("preguntarAE");	
						}
						else
						{
							forma.getIndicadoresMap().put("actualizarAtras",ConstantesBD.acronimoSi);
							forma.getIndicadoresMap().put("estadoActualizarAtras","cargarIngreso");
							UtilidadBD.closeConnection(con);
							return mapping.findForward("resumenRespuestaAE");
						}			 
					}
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("respuestaAE");
					}
				}
				else if(estado.equals("reemplazarSolAdmEst"))
				{
					if(!accionReemplazarSolAdmEsta(con,forma,usuario,request))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("preguntarAE");	
					}
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("resumenRespuestaAE");
					}				 
				}
				else if(estado.equals("listadoHistoricoAE"))
				{
					accionListadoHistoricoAE(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoHistoricoAE");
				}
				else if(estado.equals("volverResumenResAdmEst"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenRespuestaAE");
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
	
	//**************************************************************
	/**
	 * Método implementado para realizar la impresión de Solicitudes de Autorizacion
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param id_informe
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, AutorizacionesForm forma) 
	{		
		logger.info("cod auto - "+forma.getAutorizacionDto().getCodigoPK());
		
		if(Utilidades.convertirAEntero(forma.getCodigoPkAutoInd()) < 0)
		{
			if(Utilidades.convertirAEntero(forma.getCodigoPkAutoCon()) > 0)
				forma.setCodigoPkAutoInd(forma.getCodigoPkAutoCon());
			else
			{
				if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0)
					forma.setCodigoPkAutoInd(forma.getAutorizacionDto().getCodigoPK());
				else
					logger.info("No se encontro el codigo pk de la autorizacion");
			}
		}
		
		UtilidadBD.closeConnection(con);
		
		if(forma.getAutorizacionDto().getImpresion().equals(ConstantesIntegridadDominio.acronimoImpresionSolAuto)
				||forma.getAutorizacionDtoAux().getImpresion().equals(ConstantesIntegridadDominio.acronimoImpresionSolAuto))
		{
			if(forma.getAutorizacionDto().getFormatoPresenSolEnvioXConve().equals(ConstantesIntegridadDominio.acronimoAnexo3Res003047))
	    		request.setAttribute("nombreArchivo", SolicitudAutorizacionPdf.pdfInformeTecnicoSolicitudAutorizacion(usuario, request, paciente, forma));
	    	else if(forma.getAutorizacionDto().getFormatoPresenSolEnvioXConve().equals(ConstantesIntegridadDominio.acronimoFormatoEstandar))
	    		request.setAttribute("nombreArchivo", SolicitudAutorizacionPdf.pdfInformeEstandarSolicitudAutorizacion(usuario, request, paciente, forma));
		}else if(forma.getAutorizacionDto().getImpresion().equals(ConstantesIntegridadDominio.acronimoImpresionRespAuto)
				||forma.getAutorizacionDtoAux().getImpresion().equals(ConstantesIntegridadDominio.acronimoImpresionRespAuto)){
			request.setAttribute("nombreArchivo", SolicitudAutorizacionPdf.pdfInformeRespustaSolicitudAutorizacion(usuario, request, paciente, forma));
		}
		request.setAttribute("nombreVentana", "Solicitud Autorizaciones");
    	return mapping.findForward("abrirPdf");
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param cod_informe_inco
	 * @param forma
	 * @return
	 */
	private static String xmlReportSolAuto(Connection con, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request,
			AutorizacionesForm forma, int codigo) {
		
		String  archivoXMLReport = SolicitudAutorizacionPdf.xmlInformeSolAuto(con, usuario, request, paciente, forma, codigo);
		return archivoXMLReport;
	}
	
	//**********************************************************************
	
	/**
	 * menu de operaciones 
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionMenuOperaciones(
			Connection con,
			AutorizacionesForm forma,
			UsuarioBasico usuario,
			HttpServletRequest request,
			boolean evaluarSubcuenta)
	{
		logger.info("..:valor de indicadores >> "+forma.getIndicadoresMap());
				
		forma.reset();
		ActionErrors errores = new ActionErrors();
	
		if(forma.getCodigosEvaluarArray().length > 0)
		{
			//Una sola solicitud
			if(forma.getCodigosEvaluarArray().length == 1)
			{
				forma.setAutorizacionDto(
					Autorizaciones.cargarAutorizacion(
						con,
						Utilidades.convertirAEntero(forma.getCodigoDetAutoInd()),
						Utilidades.convertirAEntero(forma.getCodigosEvaluarArray()[0].toString()),
						usuario.getCodigoInstitucionInt(),
						forma.getTipoOrdenInd(),
						true));

				//Verifica la vigencia de la respuesta si la posee
				boolean vigencia = Autorizaciones.tieneActivaVigencia(forma.getAutorizacionDto());				
				if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0)
					forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().setTieneActivaVigencia(vigencia);
				
				//Valida estado de la orden medica
				boolean esOrdenActiva = true;			
				if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0)
				{
					//Actualiza información del detalle 
					forma.getAutorizacionDto().getDetalle().set(0,
							Autorizaciones.cargarDatosBasicosSolicitud(
								con,
								forma.getCodigosEvaluarArray()[0].toString(),
								forma.getAutorizacionDto().getDetalle().get(0),
								forma.getTipoOrdenInd()
								));
					
					//actualiza el indicador de urgente de la orden
					forma.getAutorizacionDto().setPrioridadAtencion(forma.getAutorizacionDto().getDetalle().get(0).getUrgente());
					
					esOrdenActiva = Autorizaciones.esOrdenActiva(
							forma.getTipoOrdenInd(),
							forma.getAutorizacionDto().getDetalle().get(0).getTipoSolicitud(),
							forma.getAutorizacionDto().getDetalle().get(0).getTipoServicio(),
							forma.getAutorizacionDto().getDetalle().get(0).getEstadoHCSerArt());
					
					forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().setEsOrdenMedicaActiva(esOrdenActiva);
				}			
				
				//inicializa el mapa de indicadores
				forma.setIndicadoresMap(Autorizaciones.inicializarIndicadoresOperacionesMap(
						forma.getIndicadoresMap(),
						forma.getAutorizacionDto().getEstado(),
						forma.getAutorizacionDto().getTipoTramite(),
						forma.getCodigosEvaluarArray().length>1?true:false,
						vigencia,
						esOrdenActiva
						));
				
				//evalua si posee información 
				if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0)
				{
					if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoSolicAmbula) 
							&& evaluarSubcuenta)				
						forma.setSubCuentaInd(forma.getAutorizacionDto().getIdSubCuenta());					
				}
			}
			else
			{
				//multiples solicitudes, solo para solicitudes sin estado				
				//inicializa el mapa de indicadores
				forma.setIndicadoresMap(Autorizaciones.inicializarIndicadoresOperacionesMap(
						forma.getIndicadoresMap(),
						"",
						"",
						forma.getCodigosEvaluarArray().length>1?true:false,
						false,
						true
						));	
				
				if(evaluarSubcuenta && 
						forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoSolicAmbula))				
					forma.setSubCuentaInd(ConstantesBD.codigoNuncaValido+"");			
			}	
		}
		else
		{			
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No Existen Solicitudes a Autorizar"));
			saveErrors(request, errores);
		}
	}
	
	//**********************************************************************
	
	/**
	 * consulta la informacion 
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * */
	public void accionMenuOperacionesAdmEst(Connection con,AutorizacionesForm forma,UsuarioBasico usuario)	
	{
		forma.reset();
		
		forma.setAutorizacionDto(
			Autorizaciones.cargarAutorizacionXEncabezado(
					con,
					forma.getCodigoPkAutoInd(),
					forma.getCuentaInd(),
					forma.getSubCuentaInd(),
					forma.getTipoAutoInd(),
					usuario.getCodigoInstitucionInt(),
					true));				

		//Verifica la vigencia de la respuesta si la posee
		boolean vigencia = Autorizaciones.tieneActivaVigencia(forma.getAutorizacionDto());				
		if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0)
			forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().setTieneActivaVigencia(vigencia);
			
		//inicializa el mapa de indicadores
		forma.setIndicadoresMap(Autorizaciones.inicializarIndicadoresOperacionesAdmEstMap(
				forma.getIndicadoresMap(),
				forma.getAutorizacionDto().getEstado(),
				forma.getAutorizacionDto().getTipoTramite(),
				vigencia));
	}
	 
	//**********************************************************************
	
	/**
	 * menu de consulta 
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionConsultaMenuOperaciones(
			Connection con,
			AutorizacionesForm forma,
			UsuarioBasico usuario,
			HttpServletRequest request) throws IPSException
	{
		forma.reset();
		logger.info("..:valor de indicadores >> codigo auto >> "+forma.getCodigoPkAutoCon()+" >> codigo detalle auto "+forma.getCodigoDetAutoCon()+" orden tipo >> "+forma.getTipoOrdenInd());	
		ActionErrors errores = new ActionErrors();
	
		if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst) && 
				Utilidades.convertirAEntero(forma.getCodigoPkAutoCon()) > 0)
		{
			forma.setAutorizacionDto(
					Autorizaciones.cargarAutorizacionXEncabezado(
							con,
							forma.getCodigoPkAutoCon(),
							"",
							"",
							"",
							usuario.getCodigoInstitucionInt(),
							forma.getFuncInd().equals("autoHistoFijoAE")?false:true));
			
			//inicializa el mapa de indicadores
			forma.setIndicadoresConsultaMap(Autorizaciones.inicializarIndicadoresConsultaMap(
					forma.getIndicadoresMap(),
					forma.getAutorizacionDto().getEstado(), 
					forma.getAutorizacionDto().getTipoTramite()));
			
			forma.setAutorizacionDto(
					Autorizaciones.prepararDtoAutorizacionAE(
							con,
							forma.getAutorizacionDto(),
							"",
							ConstantesBD.codigoNuncaValido,
							ConstantesBD.codigoNuncaValido,
							ConstantesBD.codigoNuncaValido,
							ConstantesBD.codigoNuncaValido,
							usuario));
		}
		else
		{
			if(Utilidades.convertirAEntero(forma.getCodigoDetAutoCon()) > 0)
			{
					forma.setAutorizacionDto(
						Autorizaciones.cargarAutorizacion(
							con,
							Utilidades.convertirAEntero(forma.getCodigoDetAutoCon()),
							ConstantesBD.codigoNuncaValido,
							usuario.getCodigoInstitucionInt(),
							"",
							false));			
					
					//inicializa el mapa de indicadores
					forma.setIndicadoresConsultaMap(Autorizaciones.inicializarIndicadoresConsultaMap(
							forma.getIndicadoresMap(),
							forma.getAutorizacionDto().getEstado(), 
							forma.getAutorizacionDto().getTipoTramite()));
					
					forma.setAutorizacionDto(
							Autorizaciones.prepararDtoAutorizacion(
									con,
									forma.getAutorizacionDto(), 
									null,
									"",
									ConstantesBD.codigoNuncaValido, 
									ConstantesBD.codigoNuncaValido,
									ConstantesBD.codigoNuncaValido,
									usuario));
			}
			else
			{			
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No Existen Codigo en Detalle de Autorizacio"));
				saveErrors(request, errores);
			}
		}
	}
	
	//**********************************************************************

	
	/**
	 * Evalua la accion a tomar a partir de los codigos recibidos 
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param ActionMapping mapping
	 * @param UsuarioBasico usuario
	 * */
	private boolean accionEvaluarConveio(Connection con,AutorizacionesForm forma)
	{
		//si no existe informacion del convenio se solicita al usuario
		if(Utilidades.convertirAEntero(forma.getSubCuentaInd())<0)
		{			
			forma.setConveniosArray(UtilidadesManejoPaciente.obtenerConveniosXIngreso(
					con,
					Utilidades.convertirAEntero(forma.getIngresoInd()),
					""));
			UtilidadBD.closeConnection(con);
			return false;
		}

		return true;
	}
	
	//**********************************************************************
	
	/**
	 * Evalua la accion a tomar a partir de los codigos recibidos 
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param ActionMapping mapping
	 * @param UsuarioBasico usuario
	 * */
	private boolean accionEvaluarFormato(Connection con,AutorizacionesForm forma,HttpServletRequest request,boolean cargarTodosConvenios)
	{
		//si no existe informacion del convenio se solicita al usuario
		if(Utilidades.convertirAEntero(forma.getSubCuentaInd())>0)
		{
			//Consulta la información del formato de solicitud del convenio
			HashMap datos = Autorizaciones.consultarInfoConvenio(con, forma.getSubCuentaInd()+"");
			if(Utilidades.convertirAEntero(datos.get("numRegistros").toString())>0)
			{
				if(datos.get("formato_autorizacion").toString().equals(""))
				{
					if(!cargarTodosConvenios)
					{
						forma.setConveniosArray(UtilidadesManejoPaciente.obtenerConveniosXIngreso(
								con,
								Utilidades.convertirAEntero(forma.getIngresoInd()),
								forma.getSubCuentaInd()));
					}
					
					ActionErrors errores = new ActionErrors();
					errores.add("descripcion",new ActionMessage("errors.notEspecific","No se tiene definido Formato de autorización para el convenio "+datos.get("nombre").toString()+". Proceso cancelado"));
					saveErrors(request, errores);
					return false;
				}
			}
		}

		return true;
	}
	
	//**********************************************************************
	
	/**
	 * carga la nueva solicitud
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private void accionNuevaSolicitud(Connection con,AutorizacionesForm forma,UsuarioBasico usuario,HttpServletRequest request) throws IPSException
	{	
		forma.setAutorizacionDtoAux(
				Autorizaciones.prepararDtoAutorizacion(
						con,
						forma.getAutorizacionDtoAux(), 
						forma.getCodigosEvaluarArray(),
						forma.getTipoOrdenInd(),
						Utilidades.convertirAEntero(forma.getIngresoInd()),
						Utilidades.convertirAEntero(forma.getCuentaInd()),
						Utilidades.convertirAEntero(forma.getSubCuentaInd()),
						usuario));
			
			//Se cargan los arreglos
			forma.setTiposSerSolArray(UtilidadesManejoPaciente.obtenerTiposServicioSolicitados(con, usuario.getCodigoInstitucionInt(), true));

			//Se actualiza la información del convenio en caso de que no exista
				forma.setConvenioInd(forma.getAutorizacionDtoAux().getCodigoConvenio()+"");			

			//Si no hay coberturas se carga el arreglo de coberturas
			if(forma.getAutorizacionDtoAux().getCodigoTipoCobertura()<=0)
			{
				forma.setCoberturasSaludArray(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimenSubCuenta(
						con,
						forma.getSubCuentaInd(), 
						usuario.getCodigoInstitucionInt()));
			}
			
			//Si no hay origen atencion se carga el arreglo de causas externas
			if(forma.getAutorizacionDtoAux().getCodigoOrigenAtencion()<=0)
				forma.setOrigenesAtencionArray(UtilidadesHistoriaClinica.obtenerCausasExternas(con,true));
			
			//Se cargan las entidades de envío
			forma.setEntidadesEnvioArray(UtilidadesManejoPaciente.cargarEntidadesEnvio(
					con,
					usuario.getCodigoInstitucionInt(),
					Utilidades.convertirAEntero(forma.getConvenioInd())));
			
			//recarga la información de envío si existe información
			if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoPK())>0 
					&& !forma.getAutorizacionDtoAux().getEnvioSolicitud().getEntidadEnvio().getValue().equals(""))
			{
				forma.setConvenioEnvioInd(forma.getAutorizacionDtoAux().getEnvioSolicitud().getEntidadEnvio().getValue());
				accionFiltrarMediosEnvios(con, forma,null,false,true);
			}
					
			//se carga la información de los profesionales que solicitan
			forma.setProfesionalesSolictaArray(UtilidadesManejoPaciente.obtenerProfSolicitaSolicitudes(
					con,
					forma.getCodigosEvaluarXComas(), 
					forma.getTipoOrdenInd().equals("1")?false:true));
			
			if(forma.getAutorizacionDtoAux().getDetalle().size() == 0)
				forma.getAutorizacionDtoAux().getDetalle().add(new DtoDetAutorizacion());

			//carga información basica del servicio/articulo
			forma.getAutorizacionDtoAux().getDetalle().set(0,
					Autorizaciones.cargarDatosBasicosSolicitud(
							con,
							forma.getCodigosEvaluarArray()[0].toString(), 
							forma.getAutorizacionDtoAux().getDetalle().get(0),
							forma.getTipoOrdenInd()));
			
			//actualiza el indicador de urgente de la orden
			forma.getAutorizacionDtoAux().setPrioridadAtencion(forma.getAutorizacionDtoAux().getDetalle().get(0).getUrgente());
			
			//Carga el usuario modifica
			forma.getAutorizacionDtoAux().setUsuarioSolicitud(Autorizaciones.getUsarioSolicitaAutorizacion(
					con,
					forma.getCodigosEvaluarArray()[0].toString(),
					forma.getAutorizacionDtoAux().getDetalle().get(0).getTipoOrden()+"",
					forma.getTipoOrdenInd(),
					usuario));		 
	}
	
	//**********************************************************************
	
	/**
	 * carga la nueva solicitud de admision estancia
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private void accionNuevaSolicitudAE(Connection con,AutorizacionesForm forma,UsuarioBasico usuario,PersonaBasica paciente) throws IPSException
	{
		forma.setAutorizacionDtoAux(new DtoAutorizacion());
		forma.setAutorizacionDtoAux(
				Autorizaciones.prepararDtoAutorizacionAE(
						con,
						forma.getAutorizacionDto(),
						forma.getTipoAutoInd(),
						Utilidades.convertirAEntero(forma.getIngresoInd()),
						Utilidades.convertirAEntero(forma.getCuentaInd()),
						Utilidades.convertirAEntero(forma.getConvenioInd()),
						Utilidades.convertirAEntero(forma.getSubCuentaInd()),
						usuario));
			
		//Se cargan los arreglos
		forma.setTiposSerSolArray(UtilidadesManejoPaciente.obtenerTiposServicioSolicitados(con, usuario.getCodigoInstitucionInt(), true));

		//Se actualiza la información del convenio en caso de que no exista
			forma.setConvenioInd(forma.getAutorizacionDtoAux().getCodigoConvenio()+"");			

		//Si no hay coberturas se carga el arreglo de coberturas
		if(forma.getAutorizacionDtoAux().getCodigoTipoCobertura()<=0)
		{
			forma.setCoberturasSaludArray(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimenSubCuenta(
					con,
					forma.getSubCuentaInd(), 
					usuario.getCodigoInstitucionInt()));
		}
		
		//Si no hay origen atencion se carga el arreglo de causas externas
		forma.setOrigenesAtencionArray(UtilidadesHistoriaClinica.obtenerCausasExternas(con,true));
		
		//Se cargan las entidades de envío
		forma.setEntidadesEnvioArray(UtilidadesManejoPaciente.cargarEntidadesEnvio(
				con,
				usuario.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(forma.getConvenioInd())));
		
		//recarga la información de envío si existe información
		if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoPK())>0 
				&& !forma.getAutorizacionDtoAux().getEnvioSolicitud().getEntidadEnvio().getValue().equals(""))
		{
			forma.setConvenioEnvioInd(forma.getAutorizacionDtoAux().getEnvioSolicitud().getEntidadEnvio().getValue());
			accionFiltrarMediosEnvios(con, forma,null,false,true);
		}
					
		if(forma.getAutorizacionDtoAux().getDetalle().size() == 0)
			forma.getAutorizacionDtoAux().getDetalle().add(new DtoDetAutorizacion());
		
		//Evalua autorizacion
		if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoPK()) > 0)
		{
			if(forma.getAutorizacionDtoAux().getDiagnosticos().size() > 0)
				forma.getAutorizacionDtoAux().setPuedoModificarDiagnosticos(true);
		}
		
		//Carga el usuario modifica
		forma.getAutorizacionDtoAux().setUsuarioSolicitud(usuario);
		//Informacion de la cama
		forma.getAutorizacionDtoAux().getCama().setCodigo(paciente.getCodigoCama());
		
		logger.info("valor de la cama >> "+forma.getAutorizacionDtoAux().getCama().getCodigo());
		
		//la informacion de la respuesta no se carga para el ingreso de una nueva solicitud
		if(forma.getAutorizacionDtoAux().getDetalle().size() > 0)
			forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setNumeroAutorizacion("");
	}
	
	//**********************************************************************
	
	/**
	 * Evalua la accion a tomar a partir de los codigos recibidos 
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param ActionMapping mapping
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private void accionAbrirEnvio(Connection con,AutorizacionesForm forma,UsuarioBasico usuario,HttpServletRequest request) throws IPSException
	{
		if(forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoEstancia) || 
				forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoAdmision))
		{
			forma.setAutorizacionDto(
				Autorizaciones.prepararDtoAutorizacionAE(
						con,
						forma.getAutorizacionDto(),
						forma.getTipoAutoInd(),
						Utilidades.convertirAEntero(forma.getIngresoInd()),
						Utilidades.convertirAEntero(forma.getCuentaInd()),
						Utilidades.convertirAEntero(forma.getConvenioInd()),
						Utilidades.convertirAEntero(forma.getSubCuentaInd()),
						usuario));
		}
		else
		{
			forma.setAutorizacionDto(
					Autorizaciones.prepararDtoAutorizacion(
							con,
							forma.getAutorizacionDto(), 
							forma.getCodigosEvaluarArray(),
							forma.getTipoOrdenInd(),
							Utilidades.convertirAEntero(forma.getIngresoInd()),
							Utilidades.convertirAEntero(forma.getCuentaInd()),
							Utilidades.convertirAEntero(forma.getSubCuentaInd()),
							usuario));
			
			//se carga la información de los profesionales que solicitan
			forma.setProfesionalesSolictaArray(UtilidadesManejoPaciente.obtenerProfSolicitaSolicitudes(
					con,
					forma.getCodigosEvaluarXComas(), 
					forma.getTipoOrdenInd().equals("1")?false:true));
		}
			
		//Se cargan los arreglos
		forma.setTiposSerSolArray(UtilidadesManejoPaciente.obtenerTiposServicioSolicitados(con, usuario.getCodigoInstitucionInt(), true));

		//Se actualiza la informacion del convenio en caso de que no exista
			forma.setConvenioInd(forma.getAutorizacionDto().getCodigoConvenio()+"");			

		//Si no hay coberturas se carga el arreglo de coberturas
		if(forma.getAutorizacionDto().getCodigoTipoCobertura()<=0)
		{
			forma.setCoberturasSaludArray(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimenSubCuenta(
					con,
					forma.getConvenioInd(), 
					usuario.getCodigoInstitucionInt()));
		}
		
		//Si no hay origen atencion se carga el arreglo de causas externas
		if(forma.getAutorizacionDto().getCodigoOrigenAtencion()<=0)
			forma.setOrigenesAtencionArray(UtilidadesHistoriaClinica.obtenerCausasExternas(con,true));
		
		//Se cargan las entidades de envío
		forma.setEntidadesEnvioArray(UtilidadesManejoPaciente.cargarEntidadesEnvio(
				con,
				usuario.getCodigoInstitucionInt(),
				Utilidades.convertirAEntero(forma.getConvenioInd())));
		
		//recarga la información de envío si existe información
		if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0 
				&& !forma.getAutorizacionDto().getEnvioSolicitud().getEntidadEnvio().getValue().equals(""))
		{
			forma.setConvenioEnvioInd(forma.getAutorizacionDto().getEnvioSolicitud().getEntidadEnvio().getValue());
			accionFiltrarMediosEnvios(con, forma,null,false,false);
		}
				
			
	}
	
	//**********************************************************************
	
	/**
	 * Accion guardar solicitud de envío de autorización
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private boolean accionGuardarSolEnv(Connection con,AutorizacionesForm forma,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException
	{
		ActionErrors errores = new ActionErrors();
			
		if(forma.getAutorizacionDtoAux().isPuedoModificarDiagnosticos())
			forma.getAutorizacionDtoAux().diagnosticosHashMapToDto(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(), usuario);
		
		errores = Autorizaciones.evaluarAccionGuardarSolEnviAuto(forma.getAutorizacionDtoAux());
		
		if(!errores.isEmpty())
		{
			if(forma.getAutorizacionDtoAux().isPuedoModificarDiagnosticos())
				forma.getAutorizacionDtoAux().setDiagnosticos(new ArrayList<DtoDiagAutorizacion>());
			
			saveErrors(request, errores);
			return false;
		}
		
		//Informacion previa al ingreso		
		forma.getAutorizacionDtoAux().setTipoTramite(ConstantesIntegridadDominio.acronimoInterna);
		forma.getAutorizacionDtoAux().setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitado);
		forma.getAutorizacionDtoAux().setUsuarioModifica(usuario);
		forma.getAutorizacionDtoAux().setUsuarioSolicitud(usuario);
		
		//Detalle
		for(DtoDetAutorizacion elemento:forma.getAutorizacionDtoAux().getDetalle())
		{
			elemento.setUsuarioModifica(usuario);
			elemento.setActivo(ConstantesBD.acronimoSi);
			elemento.setEstadoSolDetAuto(forma.getAutorizacionDtoAux().getEstado());			
		}
		
		//inicia una transacción
		UtilidadBD.iniciarTransaccion(con);
		Autorizaciones autorizaciones = new Autorizaciones();
		InfoDatosInt codigoAutoyDetalle = autorizaciones.insertarAutorizacion(
				con,
				forma.getAutorizacionDtoAux(),
				usuario,
				paciente, request, forma);
		
		if(codigoAutoyDetalle.getCodigo() > 0)
		{
			//Actualiza la informacion de la cobertura en caso de que se pudiera modificar
			if(!forma.getAutorizacionDtoAux().isCoberturaSaludResponsable() 
					&&  forma.getAutorizacionDtoAux().getCodigoTipoCobertura() > 0 )
			{
				if(Autorizaciones.updateCoberturaSaludSubCuenta(
						con,
						forma.getAutorizacionDtoAux().getCodigoTipoCobertura()+"",
						forma.getSubCuentaInd())<=0)
				{
					UtilidadBD.abortarTransaccion(con);
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al actualizar la Cobertura del Convenio"));
					saveErrors(request, errores);
					return false;
				}
			}
			
			//Inactiva las solicitudes anteriores en caso de que existiera alguna
			if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0)
			{
				logger.info("..:Actualizar a no activo el estado del detalle ["+forma.getAutorizacionDto().getDetalle().get(0).getCodigoPK()+"] de la solicitud "+forma.getAutorizacionDto().getCodigoPK()+" >> nueva solicitud >> "+codigoAutoyDetalle.getCodigo());
				
				if(Autorizaciones.updateActivoDetAutorizacion(
						con,
						false,
						usuario.getLoginUsuario(),
						forma.getAutorizacionDto().getDetalle().get(0).getCodigoPK())>0)
				{					
					UtilidadBD.finalizarTransaccion(con);
					//carga la informacion de los consecutivos generados
					forma.setCodigoDetAutoInd(codigoAutoyDetalle.getNombre());
					
					//Llena información de los indicadores para lectura de otras funcionalidades			
					adicionarIndicadoresOtrasFunc(
							forma,
							ConstantesIntegridadDominio.acronimoSolicitud,
							forma.getCodigosEvaluarArray()[0].toString(),
							codigoAutoyDetalle.getNombre(),
							ConstantesIntegridadDominio.acronimoEstadoSolicitado,
							"",
							"",
							"",
							"");
					
					logger.info("valor del mapa >> "+forma.getIndicadoresOtrasFunc());
					accionRecargarInformacionDto(con, forma, usuario);
					return true;
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al actualizar la anterior solicitud a inactiva"));
					saveErrors(request, errores);
					return false;
				}			
			}
			else
			{
				UtilidadBD.finalizarTransaccion(con);
				
				//Llena información de los indicadores para lectura de otras funcionalidades			
				adicionarIndicadoresOtrasFunc(
						forma,
						ConstantesIntegridadDominio.acronimoSolicitud,
						forma.getCodigosEvaluarArray()[0].toString(),
						codigoAutoyDetalle.getNombre(),
						ConstantesIntegridadDominio.acronimoEstadoSolicitado,
						"",
						"",
						"",
						"");
								
				//carga la informacion de los consecutivos generados
				forma.setCodigoPkAutoInd(codigoAutoyDetalle.getCodigo()+"");
				forma.setCodigoDetAutoInd(codigoAutoyDetalle.getNombre());
				accionRecargarInformacionDto(con, forma, usuario);
				logger.info("valor del mapa >> "+forma.getIndicadoresOtrasFunc());
				return true;
			}
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar la Autorizacion"));
			saveErrors(request, errores);
			return false;
		}
	}
	
	//**********************************************************************
	
	/**
	 * Accion guardar solicitud de envío de autorización de cama o estancia
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private boolean accionGuardarSolEnvAE(Connection con,AutorizacionesForm forma,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getAutorizacionDtoAux().isPuedoModificarDiagnosticos())
			forma.getAutorizacionDtoAux().diagnosticosHashMapToDto(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(), usuario);
		
		errores = Autorizaciones.evaluarAccionGuardarSolEnviAuto(forma.getAutorizacionDtoAux());
		
		if(!errores.isEmpty())
		{
			if(forma.getAutorizacionDtoAux().isPuedoModificarDiagnosticos())
				forma.getAutorizacionDtoAux().setDiagnosticos(new ArrayList<DtoDiagAutorizacion>());
			
			saveErrors(request, errores);
			return false;
		}
		
		//Información previa al ingreso		
		forma.getAutorizacionDtoAux().setTipoTramite(ConstantesIntegridadDominio.acronimoInterna);
		forma.getAutorizacionDtoAux().setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitado);
		forma.getAutorizacionDtoAux().setUsuarioModifica(usuario);
		forma.getAutorizacionDtoAux().setUsuarioSolicitud(usuario);
		
		//Detalle
		for(DtoDetAutorizacion elemento:forma.getAutorizacionDtoAux().getDetalle())
		{
			elemento.setUsuarioModifica(usuario);
			elemento.setActivo(ConstantesBD.acronimoSi);
			elemento.setEstadoSolDetAuto(forma.getAutorizacionDtoAux().getEstado());
			
			if(forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoEstancia) 
					|| forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoAdmision))
			{
				elemento.getServicioArticulo().setCodigo(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoServicioAE()));
				elemento.setCantidad(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCantidadServicioAE()));				
			}
		}
		
		//inicia una transacción
		UtilidadBD.iniciarTransaccion(con);
		Autorizaciones autorizaciones = new Autorizaciones();
		logger.info("valor de la cama antes de guardar >> "+forma.getAutorizacionDtoAux().getCama().getCodigo());
		InfoDatosInt codigoAutoyDetalle = autorizaciones.insertarAutorizacion(
				con,
				forma.getAutorizacionDtoAux(),
				usuario,
				paciente, request, forma);
		
		if(codigoAutoyDetalle.getCodigo() > 0)
		{
			//Actualiza la informacion de la cobertura en caso de que se pudiera modificar
			if(!forma.getAutorizacionDtoAux().isCoberturaSaludResponsable() 
					&&  forma.getAutorizacionDtoAux().getCodigoTipoCobertura() > 0 )
			{
				if(Autorizaciones.updateCoberturaSaludSubCuenta(
						con,
						forma.getAutorizacionDtoAux().getCodigoTipoCobertura()+"",
						forma.getSubCuentaInd())<=0)
				{
					UtilidadBD.abortarTransaccion(con);
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al actualizar la Cobertura del Convenio"));
					saveErrors(request, errores);
					return false;
				}
			}
			
			//Inactiva las solicitudes anteriores en caso de que existiera alguna
			if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())>0)
			{
				logger.info("..:Actualizar a no activo el estado del detalle ["+forma.getAutorizacionDto().getDetalle().get(0).getCodigoPK()+"] de la solicitud "+forma.getAutorizacionDto().getCodigoPK()+" >> nueva solicitud >> "+codigoAutoyDetalle.getCodigo());
				
				if(Autorizaciones.updateActivoDetAutorizacion(
						con,
						false,
						usuario.getLoginUsuario(),
						forma.getAutorizacionDto().getDetalle().get(0).getCodigoPK())>0)
				{
					UtilidadBD.finalizarTransaccion(con);
					//carga la informacion de los consecutivos generados
					forma.setCodigoDetAutoInd(codigoAutoyDetalle.getNombre());
					forma.setCodigoPkAutoInd(codigoAutoyDetalle.getCodigo()+"");
					
					//Llena información de los indicadores para lectura de otras funcionalidades			
					adicionarIndicadoresOtrasFunc(
							forma,
							forma.getTipoAutoInd(),
							forma.getCuentaInd(),
							codigoAutoyDetalle.getCodigo()+"",
							ConstantesIntegridadDominio.acronimoEstadoSolicitado,
							"",
							"",
							"",
							"");
					
					logger.info("valor del mapa >> "+forma.getIndicadoresOtrasFunc());
					accionRecargarInformacionDto(con, forma, usuario);
					return true;
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al actualizar la anterior solicitud a inactiva"));
					saveErrors(request, errores);
					return false;
				}			
			}
			else
			{
				UtilidadBD.finalizarTransaccion(con);
				
				//Llena información de los indicadores para lectura de otras funcionalidades			
				adicionarIndicadoresOtrasFunc(
						forma,
						forma.getTipoAutoInd(),
						forma.getCuentaInd(),
						codigoAutoyDetalle.getCodigo()+"",
						ConstantesIntegridadDominio.acronimoEstadoSolicitado,
						"",
						"",
						"",
						"");
								
				//carga la informacion de los consecutivos generados
				forma.setCodigoDetAutoInd(codigoAutoyDetalle.getNombre());
				forma.setCodigoPkAutoInd(codigoAutoyDetalle.getCodigo()+"");
				accionRecargarInformacionDto(con, forma, usuario);
				logger.info("valor del mapa >> "+forma.getIndicadoresOtrasFunc());
				return true;
			}
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar la Autorizacion"));
			saveErrors(request, errores);
			return false;
		}
	}
	
	//**********************************************************************
	
	/**
	 * Accion guardar solicitud de envío de autorización
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private boolean accionGuardarSolEnvMul(Connection con,AutorizacionesForm forma,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
			
		if(forma.getAutorizacionDtoAux().isPuedoModificarDiagnosticos())
			forma.getAutorizacionDtoAux().diagnosticosHashMapToDto(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(), usuario);
		
		errores = Autorizaciones.evaluarAccionGuardarSolEnviAuto(forma.getAutorizacionDtoAux());
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return false;
		}
		
		//Informacion previa al ingreso		
		forma.getAutorizacionDtoAux().setTipoTramite(ConstantesIntegridadDominio.acronimoInterna);
		forma.getAutorizacionDtoAux().setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitado);
		forma.getAutorizacionDtoAux().setUsuarioModifica(usuario);
		forma.getAutorizacionDtoAux().setUsuarioSolicitud(usuario);
		
		//Detalle
		for(DtoDetAutorizacion elemento:forma.getAutorizacionDtoAux().getDetalle())
		{
			elemento.setUsuarioModifica(usuario);
			elemento.setActivo(ConstantesBD.acronimoSi);
			elemento.setEstadoSolDetAuto(forma.getAutorizacionDtoAux().getEstado());			
		}
				
		//inicia una transacción
		UtilidadBD.iniciarTransaccion(con);
		
		Autorizaciones autorizaciones = new Autorizaciones();
		InfoDatosInt codigoAutoyDetalle = autorizaciones.insertarAutorizacion(
				con,
				forma.getAutorizacionDtoAux(),
				usuario,
				paciente, request, forma);
		
		if(codigoAutoyDetalle.getCodigo() > 0)
		{
			//Actualiza la información de la cobertura en caso de que se pudiera modificar
			if(!forma.getAutorizacionDtoAux().isCoberturaSaludResponsable() 
					&&  forma.getAutorizacionDtoAux().getCodigoTipoCobertura() > 0 )
			{
				if(Autorizaciones.updateCoberturaSaludSubCuenta(
						con,
						forma.getAutorizacionDtoAux().getCodigoTipoCobertura()+"",
						forma.getSubCuentaInd())<=0)
				{
					UtilidadBD.abortarTransaccion(con);
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al actualizar la Cobertura del Convenio"));
					saveErrors(request, errores);
					return false;
				}
			}
			
			//Actualiza los registroa anteriores a N
			for(DtoDetAutorizacion dtodet : forma.getAutorizacionDtoAux().getDetalle())
			{
				if(Utilidades.convertirAEntero(dtodet.getCodigoPkAnterior()) > 0)
				{
					logger.info("..:Actualizar a no activo el estado del detalle ["+dtodet.getCodigoPkAnterior()+"] de la solicitud ");
					
					if(Autorizaciones.updateActivoDetAutorizacion(
							con,
							false,
							usuario.getLoginUsuario(),
							dtodet.getCodigoPkAnterior())<=0)					
					{
						UtilidadBD.abortarTransaccion(con);
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al actualizar la anterior solicitud a inactiva"));
						saveErrors(request, errores);
						return false;
					}
				}		
			}
			
			//Llena información de los indicadores para lectura de otras funcionalidades
			for(DtoDetAutorizacion dtodet : forma.getAutorizacionDtoAux().getDetalle())
			{
				if(autorizaciones.getMapaAux().containsKey(dtodet.getCodigoEvaluar()) && 
						Utilidades.convertirAEntero(autorizaciones.getMapaAux().get(dtodet.getCodigoEvaluar()).toString())>0)
				{
					adicionarIndicadoresOtrasFunc(
							forma,
							ConstantesIntegridadDominio.acronimoSolicitud,
							dtodet.getCodigoEvaluar(),
							autorizaciones.getMapaAux().get(dtodet.getCodigoEvaluar()).toString(),
							ConstantesIntegridadDominio.acronimoEstadoSolicitado,
							"",
							"",
							"",
							"");				
				}
			}
			
			logger.info("valor del mapa >> "+forma.getIndicadoresOtrasFunc());			
			UtilidadBD.finalizarTransaccion(con);
			return true;
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al insertar la Autorizacion"));
			saveErrors(request, errores);
			return false;
		}
	}
	
	//**********************************************************************
	
	/**
	 * Nueva solicitud multiple
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private void accionNuevaSolicitudMult(Connection con,AutorizacionesForm forma,UsuarioBasico usuario,HttpServletRequest request,PersonaBasica paciente) throws IPSException
	{
		forma.setAutorizacionDtoAux(new DtoAutorizacion());
		
		forma.setAutorizacionDtoAux(
				Autorizaciones.prepararDtoAutorizacion(
						con,
						forma.getAutorizacionDtoAux(), 
						forma.getCodigosEvaluarArray(),
						forma.getTipoOrdenInd(),
						Utilidades.convertirAEntero(forma.getIngresoInd()),
						Utilidades.convertirAEntero(forma.getCuentaInd()),
						Utilidades.convertirAEntero(forma.getSubCuentaInd()),
						usuario));
			
			//Informacion de la cama 
			forma.getAutorizacionDtoAux().getCama().setCodigo(paciente.getCodigoCama());
			
			//Se cargan los arreglos
			forma.setTiposSerSolArray(UtilidadesManejoPaciente.obtenerTiposServicioSolicitados(con, usuario.getCodigoInstitucionInt(), true));

			//Se actualiza la información del convenio en caso de que no exista
				forma.setConvenioInd(forma.getAutorizacionDtoAux().getCodigoConvenio()+"");			

			//Si no hay coberturas se carga el arreglo de coberturas
			if(forma.getAutorizacionDtoAux().getCodigoTipoCobertura()<=0)
			{
				forma.setCoberturasSaludArray(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimenSubCuenta(
						con,
						forma.getSubCuentaInd(), 
						usuario.getCodigoInstitucionInt()));
			}
			
			//Si no hay origen atencion se carga el arreglo de causas externas
			if(forma.getAutorizacionDtoAux().getCodigoOrigenAtencion()<=0)
				forma.setOrigenesAtencionArray(UtilidadesHistoriaClinica.obtenerCausasExternas(con,true));
			
			//Se cargan las entidades de envío
			forma.setEntidadesEnvioArray(UtilidadesManejoPaciente.cargarEntidadesEnvio(
					con,
					usuario.getCodigoInstitucionInt(),
					Utilidades.convertirAEntero(forma.getConvenioInd())));
			
			//recarga la información de envío si existe información
			if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoPK())>0 
					&& !forma.getAutorizacionDtoAux().getEnvioSolicitud().getEntidadEnvio().getValue().equals(""))
			{
				forma.setConvenioEnvioInd(forma.getAutorizacionDtoAux().getEnvioSolicitud().getEntidadEnvio().getValue());
				accionFiltrarMediosEnvios(con, forma,null,false,true);
			}
					
			//se carga la información de los profesionales que solicitan
			forma.setProfesionalesSolictaArray(UtilidadesManejoPaciente.obtenerProfSolicitaSolicitudes(
					con,
					forma.getCodigosEvaluarXComas(), 
					forma.getTipoOrdenInd().equals("1")?false:true));
			
			if(forma.getAutorizacionDtoAux().getDetalle().size() == 0)
				forma.getAutorizacionDtoAux().getDetalle().add(new DtoDetAutorizacion());
			
			for(DtoDetAutorizacion dtod : forma.getAutorizacionDtoAux().getDetalle())
			{
				//carga información basica del servicio/articulo
				dtod = Autorizaciones.cargarDatosBasicosSolicitud(
							con,
							dtod.getCodigoEvaluar(),
							dtod,							
							forma.getTipoOrdenInd());
				
				if(dtod.getUrgente().equals(ConstantesBD.acronimoSi))
					forma.getAutorizacionDtoAux().setPrioridadAtencion(ConstantesBD.acronimoSi);
			}
			
			//Verifica el usuario que solicita
			if(ValoresPorDefecto.getValoresDefectoUsuarioaReportarenSolicitAuto(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoPersonaqueSolicista) 
					&& Utilidades.convertirAEntero(forma.getProfeSolInd().toString()) > 0)
			{
				try
				{
					UsuarioBasico usuarioAux = new UsuarioBasico();
					usuarioAux.cargarUsuarioBasico(con,Utilidades.convertirAEntero(forma.getProfeSolInd().toString()));
					forma.getAutorizacionDtoAux().setUsuarioSolicitud(usuarioAux);
				}
				catch (Exception e) {
					logger.info("error cargando usuario >> "+e);
					forma.getAutorizacionDtoAux().setUsuarioSolicitud(usuario);
				}
			}
			else
			{
				//Carga el usuario modifica
				forma.getAutorizacionDtoAux().setUsuarioSolicitud(usuario);
			}					
	}
	
	//**********************************************************************
	
	/**
	 * Método implementado para iniciar la adicion de archivos adjuntos
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionIniciarAdjuntarArchivos(
			Connection connection,
			AutorizacionesForm forma, 
			ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		logger.info("..:valor de posadjuntos >> "+forma.getPosAdjuntos()+" valor de nombre atri >> "+forma.getNombreAtri());
		//Si es un nuevo adjunto se adiciona a la estructura
		if(forma.getPosAdjuntos()==ConstantesBD.codigoNuncaValido)
		{
			if(forma.getNombreAtri().equals("res"))
			{
				DtoAdjAutorizacion adjAutorizacion = new DtoAdjAutorizacion();
				adjAutorizacion.setUsuarioModifica(usuario);
				forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getAdjuntos().add(adjAutorizacion);
				forma.setPosAdjuntos(forma.getAutorizacionDtoAux().getAdjuntos().size()-1);
			}
			else if(forma.getNombreAtri().equals("nuev"))
			{
				DtoAdjAutorizacion adjAutorizacion = new DtoAdjAutorizacion();
				adjAutorizacion.setUsuarioModifica(usuario);
				forma.getAutorizacionDtoAux().getAdjuntos().add(adjAutorizacion);
				forma.setPosAdjuntos(forma.getAutorizacionDtoAux().getAdjuntos().size()-1);
			}
			else
			{
				DtoAdjAutorizacion adjAutorizacion = new DtoAdjAutorizacion();
				adjAutorizacion.setUsuarioModifica(usuario);
				forma.getAutorizacionDto().getAdjuntos().add(adjAutorizacion);
				forma.setPosAdjuntos(forma.getAutorizacionDto().getAdjuntos().size()-1);
			}	
		}
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("seleccionarArchivo");
	}	
	
//**********************************************************************
	
	/**
	 * Método implementado para iniciar la adicion de archivos adjuntos
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionIniciarAdjuntarArchivosRes(
			Connection connection,
			AutorizacionesForm forma, 
			ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		//Si es un nuevo adjunto se adiciona a la estructura
		if(forma.getPosAdjuntosResp()==ConstantesBD.codigoNuncaValido)
		{
			DtoAdjAutorizacion adjAutorizacion = new DtoAdjAutorizacion();
			adjAutorizacion.setUsuarioModifica(usuario);
			forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().getAdjuntos().add(adjAutorizacion);
			forma.setPosAdjuntosResp(forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().getAdjuntos().size()-1);
		}
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("seleccionarArchivo");
	}	
	//*****************************************************************
	
	/**
	 * Abre la anulacion de la respuesta de solicitud
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasicousuario
	 * */
	private void accionAnularRespuestaSol(Connection con,AutorizacionesForm forma,UsuarioBasico usuario)
	{
		//carga el dto auxiliar
		forma.setAutorizacionDtoAux(new DtoAutorizacion());
		forma.getAutorizacionDtoAux().setCodigoPK(forma.getAutorizacionDto().getCodigoPK());
		
		forma.getAutorizacionDtoAux().getDetalle().add(new DtoDetAutorizacion());
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setDetAutorizacion(forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().getDetAutorizacion());
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setFechaAutorizacion(forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().getFechaAutorizacion());
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setHoraAutorizacion(forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().getHoraAutorizacion());
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setFechaAnulacion(UtilidadFecha.getFechaActual());
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setHoraAnulacion(UtilidadFecha.getHoraActual());
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setUsuarioAnulacion(usuario);
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().setMotivoAnulacion("");
		forma.getAutorizacionDtoAux().getDetalle().get(0).setDetCargo(forma.getAutorizacionDto().getDetalle().get(0).getDetCargo());
	}
	
	//*****************************************************************
	
	/**
	 * Guarda la informacion de la anulacion
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private boolean accionGuardarAnularRespSol(Connection con,AutorizacionesForm forma,UsuarioBasico usuario,HttpServletRequest request)
	{
		ActionErrors errores = Autorizaciones.evaluarAccionAnularRespSol(forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto());

		if(errores.isEmpty())
		{
			if(Autorizaciones.updateAnulacionDetAutorizacion(
					con,
					ConstantesIntegridadDominio.acronimoEstadoAnulado,
					forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getDetAutorizacion(),
					forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getUsuarioAnulacion().getLoginUsuario(),
					forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getFechaAnulacion(),
					forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getHoraAnulacion(),
					forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getMotivoAnulacion())>0)
			{
				if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo())>0)
				{
					logger.info("..:Se actualizara a vacio el numero de autorización en el det_cargo >>  "+forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo());
									  
					if(Autorizaciones.actualizarAutorizacionServicioArticulo(
						con,
						"",
						forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo()) <= 0)
					{						
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Numero de Autorización en el Detalle del Cargo"));
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						return false;
					}
				}
				
				UtilidadBD.finalizarTransaccion(con);				
								
				//Llena información de los indicadores para lectura de otras funcionalidades
				if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
				{
					adicionarIndicadoresOtrasFunc(
							forma,
							forma.getTipoAutoInd(),
							forma.getCuentaInd(),
							forma.getAutorizacionDtoAux().getCodigoPK()+"",							
							ConstantesIntegridadDominio.acronimoEstadoAnulado,					
							"",
							"",
							"",
							"");
				}
				else
				{
					//Llena información de los indicadores para lectura de otras funcionalidades			
					adicionarIndicadoresOtrasFunc(
							forma,
							ConstantesIntegridadDominio.acronimoSolicitud,
							forma.getCodigosEvaluarArray()[0].toString(),
							forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getDetAutorizacion(),
							ConstantesIntegridadDominio.acronimoEstadoAnulado,
							"",
							"",
							"",
							"");
				}
				
				logger.info("valor del mapa >> "+forma.getIndicadoresOtrasFunc());				
				forma.setIndicadoresMap("mensajeExito","Información de Anulación almacenada con Exito");
				return true;
			}
			else
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Guardar la Anulación"));
				UtilidadBD.abortarTransaccion(con);
				saveErrors(request, errores);
				return false;
			}
		}
		else
		{
			saveErrors(request, errores);
			return false;
		}
	}
	
	//*****************************************************************
	
	/**
	 * Guarda la información de envios
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionGuardarEnvioSolEnv(Connection con,AutorizacionesForm forma,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException
	{
		String pathArchivoIncoxml = "";
		UtilidadBD.iniciarTransaccion(con);
		forma.setIndicadoresMap("mensajeExito","");
		
		//********************************************************************************************************************
        // Generacion del archivo XML de Solicitud Autorizaciones
		if(forma.getAutorizacionDto().getEnvioSolicitud().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
		{
        	if((pathArchivoIncoxml= xmlReportSolAuto(con, usuario, paciente, request, forma, 
        			Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())))==null)
        	{
        		forma.getAutorizacionDto().setPathArchivoIncoXml(pathArchivoIncoxml);
        		forma.getAutorizacionDto().setArchivoInconGenerado(ConstantesBD.acronimoSi);
        	}else
        		forma.getAutorizacionDto().setArchivoInconGenerado(ConstantesBD.acronimoSi);
        }else
        	forma.getAutorizacionDto().setArchivoInconGenerado(ConstantesBD.acronimoSi);
		forma.getAutorizacionDto().getEnvioSolicitud().setUrlArchivoIncoXmlDes(pathArchivoIncoxml);
		logger.info("path Archivo: "+forma.getAutorizacionDto().getEnvioSolicitud().getUrlArchivoIncoXmlDes());
        // Fin Generacion del archivo XML de Inconsitencias
        //********************************************************************************************************************
		
		if(Autorizaciones.guardarInfoSeccionEnvio(
				con,
				forma.getAutorizacionDto())>0)
		{
			UtilidadBD.finalizarTransaccion(con);			
			accionRecargarInformacionDto(con, forma, usuario);
			forma.setIndicadoresMap("mensajeExito","Información de Envío almacenada con Exito");
		}
		else
			UtilidadBD.abortarTransaccion(con);		
	}	
	
	//**********************************************************************
	
	/**
	 * Guarda la información de envios
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionAbrirRespuesta(
			Connection con,
			AutorizacionesForm forma,
			UsuarioBasico usuario,
			HttpServletRequest request,
			PersonaBasica paciente) throws IPSException
	{
		logger.info("valor de indicadores >> "+forma.getIndicadoresMap());		
		
		ActionErrors errores = new ActionErrors();
		
		if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
		{
			//carga la información de los cargos de usuarios
			forma.setCargosUsuarioArray(UtilidadesAdministracion.obtenerCargosInstitucion(
					con,
					usuario.getCodigoInstitucion(),
					ConstantesBD.acronimoSi));

			//Carga la información de tipos de vigencia
			forma.setTiposVigenciaArray(UtilidadesAdministracion.obtenerTiposVigencia(con,usuario.getCodigoInstitucion(),ConstantesBD.acronimoSi,""));
			
			//si no existe información registrada se debe cargar el dto principal con datos basicos
			if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())<=0)
				forma.setAutorizacionDto(Autorizaciones.prepararDtoAutorizacionAE(
						con,
						forma.getAutorizacionDto(),
						forma.getTipoAutoInd(),
						Utilidades.convertirAEntero(forma.getIngresoInd()),
						Utilidades.convertirAEntero(forma.getCuentaInd()),
						Utilidades.convertirAEntero(forma.getConvenioInd()),
						Utilidades.convertirAEntero(forma.getSubCuentaInd()), 
						usuario));				
			
			//Carga el dto para la respuesta
			forma.setAutorizacionDtoAux(Autorizaciones.prepararDtoAuxRespAutorizacionAE(
					con,
					forma.getAutorizacionDto(),
					forma.getAutorizacionDtoAux(),
					forma.getTipoAutoInd(),
					Utilidades.convertirAEntero(forma.getIngresoInd()),
					Utilidades.convertirAEntero(forma.getCuentaInd()),
					Utilidades.convertirAEntero(forma.getSubCuentaInd()),
					usuario));
			
			forma.getAutorizacionDtoAux().getCama().setCodigo(paciente.getCodigoCama());
			
		}
		else
		{	
			logger.info("\nvalor de las comas >> "+forma.getCodigosEvaluarXComas());
			
			if(forma.getCodigosEvaluarArray().length > 0)
			{
				//Una sola solicitud
				if(forma.getCodigosEvaluarArray().length == 1)
				{
					//carga la información de los cargos de usuarios
					forma.setCargosUsuarioArray(UtilidadesAdministracion.obtenerCargosInstitucion(
							con,
							usuario.getCodigoInstitucion(),
							ConstantesBD.acronimoSi));
	
					//Carga la información de tipos de vigencia
					forma.setTiposVigenciaArray(UtilidadesAdministracion.obtenerTiposVigencia(con,usuario.getCodigoInstitucion(),ConstantesBD.acronimoSi,""));
					
					//si no existe informacion registrada se debe cargar el dto principal con datos basicos
					if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())<=0)
						forma.setAutorizacionDto(Autorizaciones.prepararDtoAutorizacion(
								con,
								forma.getAutorizacionDto(), 
								forma.getCodigosEvaluarArray(),
								forma.getTipoOrdenInd(),
								Utilidades.convertirAEntero(forma.getIngresoInd()),
								Utilidades.convertirAEntero(forma.getCuentaInd()),
								Utilidades.convertirAEntero(forma.getSubCuentaInd()),
								usuario));
					
					//Carga el dto para la respuesta
					forma.setAutorizacionDtoAux(Autorizaciones.prepararDtoAuxRespAutorizacion(
							con,
							forma.getAutorizacionDto(),
							forma.getAutorizacionDtoAux(),
							forma.getCodigosEvaluarArray(),
							forma.getTipoOrdenInd(),
							usuario));
					
					forma.getAutorizacionDtoAux().getCama().setCodigo(paciente.getCodigoCama());
				}
			}
			else
			{			
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No Existen Solicitudes a Autorizar"));
				saveErrors(request, errores);
			}
		}
	}
	
	//**********************************************************************	
	
	/**
	 * evalua si debe solicitar o no la informacion de la subcuenta
	 * */
	public boolean evaluarSolicitudSubcuenta(
			Connection con,
			AutorizacionesForm forma,
			ActionMapping mapping,
			boolean vieneRespuesta,
			boolean vieneSolicitud)
	{
		//solicita el codigo de la subcuenta al cual debe generar la solicitud
		if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoSolicAmbula))
		{	
			if(vieneRespuesta)
			{
				if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK()) > 0)
				{  					
					if(!forma.getAutorizacionDto().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
					{
						forma.setSubCuentaInd(ConstantesBD.codigoNuncaValido+"");						
						if(!accionEvaluarConveio(con, forma))			
							return true;					
					}
				}
			}
			else if(vieneSolicitud)
			{
				if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK()) > 0)
				{
					forma.setSubCuentaInd(ConstantesBD.codigoNuncaValido+"");			
					if(!accionEvaluarConveio(con, forma))			
						return true;				
				}
			}		
		}
		
		return false;
	}
	
	//**********************************************************************
	/**
	 * 
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	public boolean accionReemplazarSolAdmEsta(Connection con, AutorizacionesForm forma,UsuarioBasico usuario,HttpServletRequest request)
	{
		UtilidadBD.iniciarTransaccion(con);
		
		if(!Autorizaciones.insertarEstanciasPorPreguntar(
				con,
				forma.getListadoEstanciasDisp(),
				forma.getCodigoDetAutoInd(),
				usuario))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Reemplazar las Solicitudes Seleccionadas"));
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			return false;
		}
		else
		{
			forma.getIndicadoresMap().put("actualizarAtras",ConstantesBD.acronimoSi);
			forma.getIndicadoresMap().put("estadoActualizarAtras","cargarIngreso");
		}
		
		UtilidadBD.finalizarTransaccion(con);		
		return true;
	}
	
	
	//**********************************************************************
	
	/**
	 * Carga el listado de historicos
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * */
	 public void accionListadoHistoricoAE(Connection con,AutorizacionesForm forma)
	 {
		 forma.setListadoHistoArray(Autorizaciones.cargarListadoAutorizaciones(
				 con, 
				 forma.getAutorizacionDto().getIdCuenta(),
				 "'"+ConstantesIntegridadDominio.acronimoEstancia+"','"+ConstantesIntegridadDominio.acronimoAdmision+"'"));
	 }
	 
	//**********************************************************************
	
	/**
	 * Carga el listado de estancias pendientes por autorizar
	 * @parama Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * */
	public HashMap procesoEstanciasAutorizacion(Connection con, AutorizacionesForm forma,UsuarioBasico usuario)
	{
		HashMap respuesta = new HashMap();
		respuesta.put("exito",true);
		respuesta.put("preguntar",false);
		
		if(forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getEstadorespauto().equals(ConstantesIntegridadDominio.acronimoAutorizado))
		{
			//Si es de Admision/Estancia se buscan los servicios de estancia para asociar
			if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
			{
				forma.setListadoEstanciasDisp(Autorizaciones.cargarInfoBasicaDetAutorizacionEstancia(
					con,						 
					forma.getAutorizacionDtoAux().getDetalle().get(0).getServicioArticulo().getCodigo(),
					forma.getSubCuentaInd(),
					forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada(),
					forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().getFechaFinalAutorizada(),
					ConstantesIntegridadDominio.acronimoManual));
							
				forma.setListadoEstanciasDisp(Autorizaciones.cargarListadoEstanciasPorPreguntar(
						con, 
						forma.getListadoEstanciasDisp(), 
						forma.getCodigoDetAutoInd(),
						usuario));
				
				logger.info("..:Proceso estancia. cantidad de registros por preguntar >> "+forma.getListadoEstanciasDisp().size());
				
				if(forma.getListadoEstanciasDisp().size() <= 0)
				{
					respuesta.put("exito",true);
					return respuesta;
				}
				else
				{
					respuesta.put("exito",false);
					respuesta.put("preguntar",true);
					return respuesta;
				}
			}
		}

		return respuesta;
	}
	//*********************************************************************************************
	
	/**
	 * Guarda la información de envios
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	private boolean accionGuardarRespuestaSol(
			Connection con,
			AutorizacionesForm forma,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			HttpServletRequest request)
	{
		Autorizaciones autorizaciones = new Autorizaciones();
		
		//actualizar tipo dia del tipo vigencia
		forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getTipoVigencia().setNombre(
				getTipoDiaTipoVigencia(
						forma,
						forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getTipoVigencia().getCodigo()+""));
		
		//Valida campos requeridos 
		ActionErrors errores = Autorizaciones.validarGuardarRespuestaSol(forma.getAutorizacionDtoAux(),forma.getTipoOrdenInd());
		InfoDatosInt info = new InfoDatosInt();
		
		if(!errores.isEmpty())
		{			
			saveErrors(request, errores);
			return false;
		}

		UtilidadBD.iniciarTransaccion(con);

		if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK())<=0)
		{
			//la respuesta es externa para el caso en que no exista solicitud
			forma.getAutorizacionDtoAux().setTipoTramite(ConstantesIntegridadDominio.acronimoExterna);

			//Detalle
			for(DtoDetAutorizacion elemento:forma.getAutorizacionDtoAux().getDetalle())
			{
				elemento.setUsuarioModifica(usuario);
				elemento.setActivo(ConstantesBD.acronimoSi);
				elemento.setEstadoSolDetAuto(forma.getAutorizacionDtoAux().getEstado());

				if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
				{
					if(forma.getAutorizacionDtoAux().getDetalle().get(0).getServicioArticulo().getCodigo() <=0)
					{
						elemento.getServicioArticulo().setCodigo(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoServicioAE()));
						elemento.setCantidad(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCantidadServicioAE()));
					}
				}
			}

			info = autorizaciones.insertarAutorizacion(con,forma.getAutorizacionDtoAux(),usuario,
					paciente, request, forma);
			if(info.getCodigo() <= 0)
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Ingresar la Respuesta a la Solicitud"));
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
				return false;
			}
			else
			{
				if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
				{
					//actualiza la cantidad autorizada en el detalla 
					if(Autorizaciones.updateCantidadAutoDetAutorizacion(
							con, 
							Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getCantidadAutorizada()),
							usuario.getLoginUsuario(),
							info.getNombre())<=0)
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la cantidad Autorizada en el Detalle de Solicitud"));
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						return false;
					}
				}
				
				//actualiza el estado del detalle
				if(Autorizaciones.updateEstadoDetAutorizacion(
						con,
						forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getEstadorespauto(),
						usuario.getLoginUsuario(),
						info.getNombre()) <=0 )
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Estado de la Solicitud de Autorización"));
					saveErrors(request, errores);
					UtilidadBD.abortarTransaccion(con);
					return false;
				}
				else if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo())>0)
				{
					logger.info("..:Se actualizara el numero de autorización en el det_cargo >>  "+forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo());
									  
					if(Autorizaciones.actualizarAutorizacionServicioArticulo(
						con,
						forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getNumeroAutorizacion(),
						forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo()) <= 0)
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Numero de Autorización en el Detalle del Cargo"));
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						return false;
					}
				}
			}
			
			//carga la informacion de los consecutivos generados
			forma.setCodigoPkAutoInd(info.getCodigo()+"");
			forma.setCodigoDetAutoInd(info.getNombre());
			forma.setIndicadoresMap("mensajeExito","Información de Respuesta almacenada con Exito");
		}
		else
		{
			//si se va a dar respuesta a una solicitud en estado anulada se ingresara como una nueva solicitud tipo Externa
			if(forma.getAutorizacionDto().getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			{
				logger.info("\n");
				logger.info("..:Se respondera una Solicitud que se encuentra en estado Anulada. det cargo "+forma.getAutorizacionDtoAux().getDetalle().size()+" "+forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo());
				//Se crea una nueva solicitud externa
				forma.getAutorizacionDtoAux().setTipoTramite(ConstantesIntegridadDominio.acronimoExterna);
				
				//Detalle
				for(DtoDetAutorizacion elemento:forma.getAutorizacionDtoAux().getDetalle())
				{
					elemento.setUsuarioModifica(usuario);
					elemento.setActivo(ConstantesBD.acronimoSi);
					elemento.setEstadoSolDetAuto(forma.getAutorizacionDtoAux().getEstado());	
					
					if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
					{
						if(forma.getAutorizacionDtoAux().getDetalle().get(0).getServicioArticulo().getCodigo() <=0)
						{
							elemento.getServicioArticulo().setCodigo(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoServicioAE()));
							elemento.setCantidad(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCantidadServicioAE()));				
						}
					}
				}
				
				info = autorizaciones.insertarAutorizacion(con,forma.getAutorizacionDtoAux(),usuario,
						paciente, request, forma);
				if(info.getCodigo() <= 0)
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Ingresar la Respuesta a la Solicitud EXTERNA"));
					saveErrors(request, errores);
					UtilidadBD.abortarTransaccion(con);
					return false;
				}
				else
				{					
					if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
					{
						//actualiza la cantidad autorizada en el detalla 
						if(Autorizaciones.updateCantidadAutoDetAutorizacion(
								con, 
								Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getCantidadAutorizada()),
								usuario.getLoginUsuario(),
								info.getNombre())<=0)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la cantidad Autorizada en el Detalle de Solicitud"));
							saveErrors(request, errores);
							UtilidadBD.abortarTransaccion(con);
							return false;
						}
					}
					
					//actualiza el estado del detalle
					if(Autorizaciones.updateEstadoDetAutorizacion(
							con,
							forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getEstadorespauto(),
							usuario.getLoginUsuario(),
							info.getNombre()) <=0 )
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Estado de la Solicitud de Autorización EXTERNA"));
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						return false;
					}
					else if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo())>0)
					{
						logger.info("..:Se actualizara el numero de autorización en el det_cargo >>  "+forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo());
										  
						if(Autorizaciones.actualizarAutorizacionServicioArticulo(
							con,
							forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getNumeroAutorizacion(),
							forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo()) <= 0)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Numero de Autorización en el Detalle del Cargo"));
							saveErrors(request, errores);
							UtilidadBD.abortarTransaccion(con);
							return false;
						}
					}
				}
				
				logger.info("..:Actualizar a no activo el estado del detalle ["+forma.getAutorizacionDto().getDetalle().get(0).getCodigoPK()+"] de la solicitud "+forma.getAutorizacionDto().getCodigoPK()+" >> nueva solicitud >> "+info.getCodigo());
				
				if(Autorizaciones.updateActivoDetAutorizacion(
						con,
						false,
						usuario.getLoginUsuario(),
						forma.getAutorizacionDto().getDetalle().get(0).getCodigoPK())<=0)
				{
					UtilidadBD.abortarTransaccion(con);
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al actualizar la anterior solicitud a inactiva"));
					saveErrors(request, errores);
					return false;
				}						
				
				//carga la información de los consecutivos generados
				forma.setCodigoDetAutoInd(info.getNombre());
				forma.setCodigoPkAutoInd(info.getCodigo()+"");
			}
			else
			{
				if(Autorizaciones.insertarRespuestaAutorizacion(con,forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto()) <= 0)
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Ingresar la Respuesta a la Solicitud"));
					saveErrors(request, errores);
					UtilidadBD.abortarTransaccion(con);
					return false;
				}
				else
				{					
					if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
					{
						//actualiza la cantidad autorizada en el detalla 
						if(Autorizaciones.updateCantidadAutoDetAutorizacion(
								con, 
								Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getCantidadAutorizada()),
								usuario.getLoginUsuario(),
								forma.getAutorizacionDtoAux().getDetalle().get(0).getCodigoPK())<=0)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar la cantidad Autorizada en el Detalle de Solicitud"));
							saveErrors(request, errores);
							UtilidadBD.abortarTransaccion(con);
							return false;
						}
					}
					
					//actualiza el estado del detalle
					if(Autorizaciones.updateEstadoDetAutorizacion(
							con,
							forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getEstadorespauto(),
							usuario.getLoginUsuario(),
							forma.getAutorizacionDtoAux().getDetalle().get(0).getCodigoPK()) <=0)
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Estado de la Solicitud de Autorización"));
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						return false;
					}
					else if(Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo())>0)
					{
						logger.info("..:Se actualizara el numero de autorización en el det_cargo >>  "+forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo());
										  
						if(Autorizaciones.actualizarAutorizacionServicioArticulo(
							con,
							forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getNumeroAutorizacion(),
							forma.getAutorizacionDtoAux().getDetalle().get(0).getDetCargo()) <= 0)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Actualizar el Numero de Autorización en el Detalle del Cargo"));
							saveErrors(request, errores);
							UtilidadBD.abortarTransaccion(con);
							return false;
						}
					}					
				}
			}

			info = new InfoDatosInt(
					Utilidades.convertirAEntero(forma.getAutorizacionDtoAux().getCodigoPK()),
					forma.getAutorizacionDtoAux().getDetalle().get(0).getCodigoPK());
			
			forma.setIndicadoresMap("mensajeExito","Información de Respuesta almacenada con Exito");
		}
		
		String fecha = "", hora = "",vigencia = "", tipoDiaVigencia = "";
		
		if(forma.getAutorizacionDtoAux().getDetalle().get(0).getTipoServicio().equals(ConstantesBD.codigoServicioCamaEstancias+""))
		{
			fecha = forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getFechaFinalAutorizada();
			hora = forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getHoraAutorizacion();
			vigencia = "";
			tipoDiaVigencia = ""; 
		}			
		else
		{
			fecha = forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getFechaAutorizacion();
			hora = forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getHoraAutorizacion();
			vigencia = forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getVigencia();
			tipoDiaVigencia = forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getTipoVigencia().getNombre(); 
		}		
		
		//Llena información de los indicadores para lectura de otras funcionalidades
		if(forma.getTipoOrdenInd().equals(Autorizaciones.codInternoAutoAdmEst))
		{
			adicionarIndicadoresOtrasFunc(
					forma,
					forma.getTipoAutoInd(),
					forma.getCuentaInd(),
					info.getCodigo()+"",
					forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getEstadorespauto(),					
					"",
					"",
					"",
					"");
		}
		else
		{
			adicionarIndicadoresOtrasFunc(
					forma,
					ConstantesIntegridadDominio.acronimoSolicitud,
					forma.getCodigosEvaluarArray()[0].toString(),
					info.getNombre(),
					forma.getAutorizacionDtoAux().getDetalle().get(0).getRespuestaDto().getEstadorespauto(),
					vigencia,
					tipoDiaVigencia,
					fecha,
					hora);
		}
		
		logger.info("valor del mapa >> "+forma.getIndicadoresOtrasFunc());	
		UtilidadBD.finalizarTransaccion(con);	
		return true;
	}

	//**********************************************************************
	
	/**
	 * Recarga la informacion del Dto
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * @param UsuarioBasico usuario
	 * */
	private void accionRecargarInformacionDto(
			Connection con,
			AutorizacionesForm forma,
			UsuarioBasico usuario) throws IPSException
	{
		if(forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoEstancia) || 
			forma.getTipoAutoInd().equals(ConstantesIntegridadDominio.acronimoAdmision))
		{
			forma.setAutorizacionDto(
					Autorizaciones.cargarAutorizacionXEncabezado(
							con, 
							forma.getCodigoPkAutoInd(),
							forma.getCuentaInd(),
							forma.getSubCuentaInd(),
							forma.getTipoAutoInd(),
							usuario.getCodigoInstitucionInt(),
							true));
			
			//Verifica la vigencia de la respuesta si la posee				
			forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().setTieneActivaVigencia(Autorizaciones.tieneActivaVigencia(forma.getAutorizacionDto()));
			
			//inicializa el mapa de indicadores
			forma.setIndicadoresMap(Autorizaciones.inicializarIndicadoresOperacionesAdmEstMap(
					forma.getIndicadoresMap(),
					forma.getAutorizacionDto().getEstado(),
					forma.getAutorizacionDto().getTipoTramite(),
					forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().isTieneActivaVigencia()));
			
			//carga algunos datos iniciales
			if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK()) >= 0)
			{
				forma.setAutorizacionDto(
					Autorizaciones.prepararDtoAutorizacionAE(
							con, 
							forma.getAutorizacionDto(),
							forma.getTipoAutoInd(), 
							ConstantesBD.codigoNuncaValido, 
							ConstantesBD.codigoNuncaValido,
							ConstantesBD.codigoNuncaValido,
							ConstantesBD.codigoNuncaValido,
							usuario));			
			}
		}
		else
		{
			forma.setAutorizacionDto(
					Autorizaciones.cargarAutorizacion(
						con,
						Utilidades.convertirAEntero(forma.getCodigoDetAutoInd()),
						Utilidades.convertirAEntero(forma.getCodigosEvaluarArray()[0].toString()),
						usuario.getCodigoInstitucionInt(),
						forma.getTipoOrdenInd(),
						true));
			
			//Verifica la vigencia de la respuesta si la posee				
			forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().setTieneActivaVigencia(Autorizaciones.tieneActivaVigencia(forma.getAutorizacionDto()));
			
			//inicializa el mapa de indicadores
			forma.setIndicadoresMap(Autorizaciones.inicializarIndicadoresOperacionesMap(
					forma.getIndicadoresMap(),
					forma.getAutorizacionDto().getEstado(),
					forma.getAutorizacionDto().getTipoTramite(),					
					forma.getCodigosEvaluarArray().length>1?true:false,
					forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().isTieneActivaVigencia(),
					forma.getAutorizacionDto().getDetalle().get(0).getRespuestaDto().isEsOrdenMedicaActiva()));
			
			//carga algunos datos iniciales
			if(Utilidades.convertirAEntero(forma.getAutorizacionDto().getCodigoPK()) >= 0)
			{
				forma.setAutorizacionDto(
					Autorizaciones.prepararDtoAutorizacion(
							con,
							forma.getAutorizacionDto(), 
							null,
							"",
							ConstantesBD.codigoNuncaValido, 
							ConstantesBD.codigoNuncaValido,
							ConstantesBD.codigoNuncaValido,
							usuario));			
			}
		}	
			
		//recarga la información de envío
		forma.setConvenioEnvioInd(forma.getAutorizacionDto().getEnvioSolicitud().getEntidadEnvio().getValue());
		accionFiltrarMediosEnvios(con, forma,null,false,false);		
	}
	
	//**********************************************************************
	
	/**
	 * Adiciona un nuevo indicador
	 * @param AutorizacionesForm forma
	 * @param String codigoEvaluar
	 * @param String codigoDetAuto
	 * @param String estadoAuto
	 * */
	public  void adicionarIndicadoresOtrasFunc(
			AutorizacionesForm forma,
			String tipoAuto,
			String codigoEvaluar,
			String codigoDetAuto,
			String estadoAuto,
			String vigencia,
			String tipoVigencia,
			String fecha,
			String hora)
	{
		forma.setIndicadoresOtrasFunc("codigoDetAuto_"+codigoEvaluar,codigoDetAuto);		
		forma.setIndicadoresOtrasFunc("estadoAuto_"+codigoEvaluar,estadoAuto);
		forma.setIndicadoresOtrasFunc("tipoAuto_"+codigoEvaluar,tipoAuto);
		
		if(tipoAuto.equals(ConstantesIntegridadDominio.acronimoEstancia) || 
				tipoAuto.equals(ConstantesIntegridadDominio.acronimoAdmision))
			forma.setIndicadoresOtrasFunc("codigosEvaluar",codigoEvaluar);
		else
			forma.setIndicadoresOtrasFunc("codigosEvaluar",forma.getCodigosEvaluarXComas());
		
		forma.setIndicadoresOtrasFunc("fechaVigencia_"+codigoEvaluar,"");
		forma.setIndicadoresOtrasFunc("funcion",forma.getFuncInd());
		forma.setIndicadoresOtrasFunc("actiMultiple_"+codigoEvaluar,ConstantesBD.acronimoNo);
		
		if(estadoAuto.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
		{
			forma.setIndicadoresOtrasFunc("colorEstadoAuto_"+codigoEvaluar,Autorizaciones.codColorSolicitado);
			forma.setIndicadoresOtrasFunc("nombreEstadoAuto_"+codigoEvaluar,"Solicitada(o)");
			forma.setIndicadoresOtrasFunc("actiMultiple_"+codigoEvaluar,ConstantesBD.acronimoSi);
		}
		
		if(estadoAuto.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
		{
			forma.setIndicadoresOtrasFunc("colorEstadoAuto_"+codigoEvaluar,Autorizaciones.codColorAnulado);
			forma.setIndicadoresOtrasFunc("nombreEstadoAuto_"+codigoEvaluar,"Anulado(a)");
			forma.setIndicadoresOtrasFunc("actiMultiple_"+codigoEvaluar,ConstantesBD.acronimoSi);
		}	
		
		if(estadoAuto.equals(ConstantesIntegridadDominio.acronimoAutorizado))
		{			
			InfoDatosString info = Autorizaciones.calcularIncrementoFechaVigencia(
					vigencia,
					tipoVigencia,
					fecha,
					hora);
			
			forma.setIndicadoresOtrasFunc("fechaVigencia_"+codigoEvaluar,info.getCodigo()+" "+info.getNombre());
			
			forma.setIndicadoresOtrasFunc("fechaVigencia","");
			forma.setIndicadoresOtrasFunc("colorEstadoAuto_"+codigoEvaluar,Autorizaciones.codColorAutorizado);
			forma.setIndicadoresOtrasFunc("nombreEstadoAuto_"+codigoEvaluar,"Autorizado(a)");
			
			forma.setIndicadoresOtrasFunc("actiMultiple_"+codigoEvaluar,
					Autorizaciones.tieneActivaVigencia(
							vigencia,
							tipoVigencia,
							fecha,
							fecha,
							hora)?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi);
		} 
		
		if(estadoAuto.equals(ConstantesIntegridadDominio.acronimoEstadoNegado))
		{
			InfoDatosString info = Autorizaciones.calcularIncrementoFechaVigencia(
					vigencia,
					tipoVigencia,
					fecha,
					hora);			
			
			forma.setIndicadoresOtrasFunc("fechaVigencia_"+codigoEvaluar,info.getCodigo()+" "+info.getNombre());
			
			forma.setIndicadoresOtrasFunc("colorEstadoAuto_"+codigoEvaluar,Autorizaciones.codColorNegado);
			forma.setIndicadoresOtrasFunc("nombreEstadoAuto_"+codigoEvaluar,"Negada(o)");			
			forma.setIndicadoresOtrasFunc("actiMultiple_"+codigoEvaluar,ConstantesBD.acronimoSi);
		}		
	}
	
	//**********************************************************************
	
	/**
	 * Consulta el listado de cuentas del paciente
	 * @param Connection con
	 * @param AutorizacionesForm forma
	 * */
	public void accionCargarCuentaPac(Connection con,AutorizacionesForm forma)
	{
		//consulta la información de las cuentas
		forma.setListadoCuentasArray(Autorizaciones.cargarListadoCuentas(
				con, 
				forma.getSubCuentaInd(),
				forma.getIngresoInd()));
	}
	
	//**********************************************************************
	/**
	 * calcula el incremento de una fecha
	 * @param AutorizacionesForm forma
	 * @param HttpServletRequest request
	 * */
	private ActionForward accionEstadoCalcularFechaIncre(Connection con,AutorizacionesForm forma,HttpServletResponse response)
	{
		String resultado = "";
		
		if(UtilidadFecha.validarFecha(forma.getFechaInd()))
		{
			InfoDatosString info = Autorizaciones.calcularIncrementoFechaVigencia(
					forma.getCantidadInd(),
					ConstantesIntegridadDominio.acronimoUnidadMedidaDias, 
					forma.getFechaInd(), 
					UtilidadFecha.getHoraActual());
			
			resultado = 
			"<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
				"<id-hidden>fechaFinalAutorizada</id-hidden>" +		
				"<contenido-hidden>"+info.getCodigo()+"</contenido-hidden>"+	
			"</infoid>"+
			"</respuesta>";
		}
		else
		{
			resultado = 
				"<respuesta>" +
				"<infoid>" +
					"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
					"<id-hidden>fechaFinalAutorizada</id-hidden>" +		
					"<contenido-hidden></contenido-hidden>"+	
				"</infoid>"+
				"</respuesta>";
		}
			
		UtilidadBD.closeConnection(con);
		
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionEstadoCalcularFechaIncre: "+e);
		}
		
		return null;		
	}
	
	//**********************************************************************
	/**
	 * Método para filtrar los Medios de Envio de un informe
	 * @param con
	 * @param RegistroEnvioInfAtencionIniUrgForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarMediosEnvios(
			Connection con,
			AutorizacionesForm forma, 
			HttpServletResponse response,
			boolean enviarPorAjax,
			boolean nuevaSolicitud) 
	{

		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>medioEnvio</id-select>" +
				"<id-arreglo>medios</id-arreglo>" +
			"</infoid>" ;
				
		String convenioEnvioFiltro = !forma.getConvenioEnvioInd().equals("")?forma.getConvenioEnvioInd().toString():"";
		int codigo = ConstantesBD.codigoNuncaValido;
		boolean esConvenio = false;
				
		if(!convenioEnvioFiltro.equals(""))
		{
			if(convenioEnvioFiltro.split(ConstantesBD.separadorSplit).length > 1)
			{
				if((convenioEnvioFiltro.split(ConstantesBD.separadorSplit)[1]).equals(ConstantesBD.acronimoSi))
				{					
					esConvenio = false;
					codigo = Utilidades.convertirAEntero(convenioEnvioFiltro.split(ConstantesBD.separadorSplit)[0]);
					if(nuevaSolicitud)
						forma.getAutorizacionDtoAux().getEnvioSolicitud().setEsEmpresa(true);
					else
						forma.getAutorizacionDto().getEnvioSolicitud().setEsEmpresa(true);
				}
				else
				{
					esConvenio = true;
					codigo = Utilidades.convertirAEntero(convenioEnvioFiltro.split(ConstantesBD.separadorSplit)[0]);
					
					if(nuevaSolicitud)
						forma.getAutorizacionDtoAux().getEnvioSolicitud().setEsEmpresa(false);
					else
						forma.getAutorizacionDto().getEnvioSolicitud().setEsEmpresa(false);
				}
			}
			else
				codigo = Utilidades.convertirAEntero(convenioEnvioFiltro);
		}
				
		if(!esConvenio)
		{
			//inicializo el array
			forma.setMediosEnvioArray(new ArrayList<HashMap<String,Object>>());
			
			HashMap mapa = new HashMap();
			mapa.put("codigo", ConstantesIntegridadDominio.acronimoFax);
			mapa.put("descripcion","FAX");
			forma.getMediosEnvioArray().add(mapa);
			
			mapa = new HashMap();		
			mapa.put("codigo", ConstantesIntegridadDominio.acronimoEmail);
			mapa.put("descripcion","Correo Electronico");
			forma.getMediosEnvioArray().add(mapa);
			
			mapa = new HashMap();
			mapa.put("codigo", ConstantesIntegridadDominio.acronimoIntercambioElectDatos);
			mapa.put("descripcion","Intercambio Electrónico de Datos (EDI)");
			forma.getMediosEnvioArray().add(mapa);
			
			resultado += "<medios>";
				resultado += "<codigo>"+ConstantesIntegridadDominio.acronimoFax+"</codigo>";
				resultado += "<descripcion>FAX</descripcion>";
			resultado += "</medios>";
			
			resultado += "<medios>";
				resultado += "<codigo>"+ConstantesIntegridadDominio.acronimoEmail+"</codigo>";
				resultado += "<descripcion>Correo Electronico</descripcion>";
			resultado += "</medios>";
				
			resultado += "<medios>";
				resultado += "<codigo>"+ConstantesIntegridadDominio.acronimoIntercambioElectDatos+"</codigo>";
				resultado += "<descripcion>Intercambio Electronico de Datos (EDI)</descripcion>";
			resultado += "</medios>";			
			
			resultado += "</respuesta>";
		}
		else
		{
			forma.setMediosEnvioArray(Convenio.cargarMediosEnvio(con, codigo));		
			for(HashMap elemento:forma.getMediosEnvioArray())
			{
				resultado += "<medios>";
					resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
					resultado += "<descripcion>"+elemento.get("descripcion")+"</descripcion>";
				resultado += "</medios>";
			}
			
			resultado += "</respuesta>";
		}
			
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		if(enviarPorAjax)
		{
			UtilidadBD.closeConnection(con);
			
			try
			{
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
		        response.getWriter().write(resultado);
			}
			catch(IOException e)
			{
				logger.error("Error al enviar respuesta AJAX en accionFiltrarMediosEnvios: "+e);
			}
		}
		
		return null;
	}
	
	//*****************************************************************
	
	/**
	 * @param AutorizacionesForm forma
	 * @param String codigo
	 * */
	public String getTipoDiaTipoVigencia(AutorizacionesForm forma,String codigo)
	{
		if(Utilidades.convertirAEntero(codigo) > 0)
		{
			for(HashMap mapa : forma.getTiposVigenciaArray())
			{
				if(mapa.get("codigo").toString().equals(codigo))
				{
					return mapa.get("tipo").toString();	
				}
			}
		}

		return "";
	}
	
	//*****************************************************************
}