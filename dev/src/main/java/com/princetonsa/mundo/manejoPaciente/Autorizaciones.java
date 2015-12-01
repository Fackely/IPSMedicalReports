package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.AutorizacionesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.AutorizacionesDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAutorizacionesDao;
import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentaAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoRespAutorizacion;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.pdf.SolicitudAutorizacionPdf;
import com.servinte.axioma.fwk.exception.IPSException;

public class Autorizaciones
{
	
	static Logger logger = Logger.getLogger(Autorizaciones.class);
	
	public static final String codInternoAutoSolicAmbula = "1";
	public static final String codInternoAutoSolicSerMed = "2";
	public static final String codInternoAutoAdmEst = "3";
				
	public static final String codColorAnulado = "#ffe28a";
	public static final String codColorSolicitado = "#ffffcb";
	public static final String codColorAutorizado = "#c2ffbd";
	public static final String codColorNegado = "#ffd8d8";
	
	private HashMap mapaAux;
	
	
	/**
	 * instancia del DAO
	 * @return
	 */
	public static AutorizacionesDao getAutorizacionesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionesDao();
	}
	
	public Autorizaciones()
	{
		reset();		
	}
	
	public void reset()
	{
		mapaAux = new HashMap();
	}
	
	/**
	 * inicializa el mapa de indicadores
	 * */
	public static HashMap inicializarIndicadoresOperacionesMap(
			HashMap mapaOrig,
			String estadoSolicitudAuto,
			String tipoTramite,			
			boolean esMultipleSolicitud,
			boolean tienVigenciaActiva,
			boolean esActivaOrdenMedica )
	{
		mapaOrig.put("mensajeExito","");
		mapaOrig.put("mostrarMenuEnvio",ConstantesBD.acronimoNo);		
		mapaOrig.put("mostrarMenuNuevaSolicitud",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuNuevaSolicitudMultiple",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuNuevaRespuesta",ConstantesBD.acronimoNo);		
		mapaOrig.put("mostrarMenuAnulacion",ConstantesBD.acronimoNo);		
		mapaOrig.put("modificarInfoSolicitud",ConstantesBD.acronimoSi);		
		mapaOrig.put("modificarInfoEnvio",ConstantesBD.acronimoNo);		
		mapaOrig.put("mostrarConfirmarEnvio",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarImpresion",ConstantesBD.acronimoNo);
				
		mapaOrig.put("mostrarMenuConsultaSolicitud",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaEnvio",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaRespuesta",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaAnulacion",ConstantesBD.acronimoNo);
		mapaOrig.put("actualizarAtras",ConstantesBD.acronimoNo);
		
		
		if(!esMultipleSolicitud)
		{
			if(tipoTramite.equals(ConstantesIntegridadDominio.acronimoInterna) || 
					tipoTramite.equals(ConstantesIntegridadDominio.acronimoExterna))
			{
				if(estadoSolicitudAuto.equals(""))
				{				
					mapaOrig.put("mostrarMenuNuevaSolicitud",ConstantesBD.acronimoSi);
					mapaOrig.put("mostrarMenuNuevaRespuesta",ConstantesBD.acronimoSi);				
				}
				else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
				{
					mapaOrig.put("mostrarMenuEnvio",ConstantesBD.acronimoSi);		
					mapaOrig.put("mostrarMenuNuevaRespuesta",ConstantesBD.acronimoSi);
					mapaOrig.put("mostrarImpresion",ConstantesBD.acronimoSi);
				}
				else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoNegado))
				{
					mapaOrig.put("mostrarMenuNuevaSolicitud",ConstantesBD.acronimoSi);
					mapaOrig.put("mostrarImpresion",ConstantesBD.acronimoSi);
					
					if(tienVigenciaActiva && esActivaOrdenMedica)
					{				
						mapaOrig.put("mostrarMenuAnulacion",ConstantesBD.acronimoSi);
					}
				}
				else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoAutorizado))
				{
					mapaOrig.put("mostrarImpresion",ConstantesBD.acronimoSi);
					
					if(!tienVigenciaActiva && esActivaOrdenMedica)
					{
						mapaOrig.put("mostrarMenuNuevaSolicitud",ConstantesBD.acronimoSi);				
					}
					else if(esActivaOrdenMedica)
					{
						mapaOrig.put("mostrarMenuAnulacion",ConstantesBD.acronimoSi);
					}
				}
				else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
				{
					mapaOrig.put("mostrarMenuNuevaSolicitud",ConstantesBD.acronimoSi);				
					mapaOrig.put("mostrarMenuNuevaRespuesta",ConstantesBD.acronimoSi);				
				}
			}		
			else
			{
				mapaOrig.put("mostrarMenuNuevaSolicitud",ConstantesBD.acronimoSi);
				mapaOrig.put("mostrarMenuNuevaRespuesta",ConstantesBD.acronimoSi);
			}
							
			if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
			{
				mapaOrig.put("modificarInfoSolicitud",ConstantesBD.acronimoNo);		
				mapaOrig.put("mostrarConfirmarEnvio",ConstantesBD.acronimoSi);
				mapaOrig.put("modificarInfoEnvio",ConstantesBD.acronimoSi);
			}	
		}
		else
		{
			mapaOrig.put("mostrarMenuNuevaSolicitudMultiple",ConstantesBD.acronimoSi);
		}
				
		logger.info("..:Valores de inicializacion de indicadores >> estado solicitud >> "+estadoSolicitudAuto+" tipo tramite >> "+tipoTramite+" >> esMul >> "+esMultipleSolicitud+" Mapa >> "+mapaOrig+" tiene vigencia activa "+tienVigenciaActiva);
		
		return mapaOrig;
	}
	
	//*************************************************************************************
	
	/**
	 * inicializa el mapa de indicadores para admisiones/estancia
	 * */
	public static HashMap inicializarIndicadoresOperacionesAdmEstMap(
			HashMap mapaOrig,
			String estadoSolicitudAuto,
			String tipoTramite,
			boolean tienVigenciaActiva)
	{
		mapaOrig.put("mensajeExito","");
		mapaOrig.put("mostrarMenuEnvioAE",ConstantesBD.acronimoNo);		
		mapaOrig.put("mostrarMenuNuevaSolicitudAE",ConstantesBD.acronimoNo);		
		mapaOrig.put("mostrarMenuNuevaRespuestaAE",ConstantesBD.acronimoNo);		
		mapaOrig.put("mostrarMenuAnulacionAE",ConstantesBD.acronimoNo);	
		mapaOrig.put("mostrarImpresionAE",ConstantesBD.acronimoNo);		
		mapaOrig.put("mostrarMenuHistoricosAE",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaSolicitud",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaEnvio",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaRespuesta",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaAnulacion",ConstantesBD.acronimoNo);	
		mapaOrig.put("actualizarAtras",ConstantesBD.acronimoNo);
		
		if(tipoTramite.equals(ConstantesIntegridadDominio.acronimoInterna) || 
				tipoTramite.equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			if(estadoSolicitudAuto.equals(""))
			{
				mapaOrig.put("mostrarMenuNuevaSolicitudAE",ConstantesBD.acronimoSi);
				mapaOrig.put("mostrarMenuNuevaRespuestaAE",ConstantesBD.acronimoSi);				
			}
			else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
			{
				mapaOrig.put("mostrarMenuNuevaSolicitudAE",ConstantesBD.acronimoSi);
				mapaOrig.put("mostrarMenuNuevaRespuestaAE",ConstantesBD.acronimoSi);
				
				mapaOrig.put("mostrarMenuEnvioAE",ConstantesBD.acronimoSi);				
				mapaOrig.put("mostrarImpresionAE",ConstantesBD.acronimoSi);
			}
			else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoNegado))
			{				
				mapaOrig.put("mostrarImpresionAE",ConstantesBD.acronimoSi);
				
				if(tienVigenciaActiva)								
					mapaOrig.put("mostrarMenuAnulacionAE",ConstantesBD.acronimoSi);
				
			}
			else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				mapaOrig.put("mostrarMenuNuevaSolicitudAE",ConstantesBD.acronimoSi);	
				mapaOrig.put("mostrarImpresionAE",ConstantesBD.acronimoSi);
				
				if(tienVigenciaActiva)								
					mapaOrig.put("mostrarMenuAnulacionAE",ConstantesBD.acronimoSi);				
				
			}
			else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			{						
				mapaOrig.put("mostrarMenuNuevaRespuestaAE",ConstantesBD.acronimoSi);				
			}
			
			mapaOrig.put("mostrarMenuHistoricosAE",ConstantesBD.acronimoSi);
		}		
		else
		{
			mapaOrig.put("mostrarMenuNuevaSolicitudAE",ConstantesBD.acronimoSi);
			mapaOrig.put("mostrarMenuNuevaRespuestaAE",ConstantesBD.acronimoSi);
		}	
				
		logger.info("..:Valores de inicializacion de indicadores >> estado solicitud >> "+estadoSolicitudAuto+" tipo tramite >> "+tipoTramite+" Mapa >> "+mapaOrig+" tiene vigencia activa "+tienVigenciaActiva);
		
		return mapaOrig;
	}
	
	//*************************************************************************************
	
	
	/**
	 * inicializa el mapa de indicadores
	 * @param HashMap mapaOrig
	 * @param String estadoSolicitudAuto
	 * @param String tipoTramite
	 * */
	public static HashMap inicializarIndicadoresConsultaMap(
			HashMap mapaOrig,
			String estadoSolicitudAuto,
			String tipoTramite)
	{	
		mapaOrig.put("mostrarMenuConsultaSolicitud",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaEnvio",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaRespuesta",ConstantesBD.acronimoNo);
		mapaOrig.put("mostrarMenuConsultaAnulacion",ConstantesBD.acronimoNo);	
						
		if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
		{
			mapaOrig.put("mostrarMenuConsultaAnulacion",ConstantesBD.acronimoSi);
			mapaOrig.put("mostrarMenuConsultaRespuesta",ConstantesBD.acronimoSi);
			
			if(tipoTramite.equals(ConstantesIntegridadDominio.acronimoInterna))
			{
				mapaOrig.put("mostrarMenuConsultaSolicitud",ConstantesBD.acronimoSi);
				mapaOrig.put("mostrarMenuConsultaEnvio",ConstantesBD.acronimoSi);
				
			}			
		}
		else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
		{
			mapaOrig.put("mostrarMenuConsultaSolicitud",ConstantesBD.acronimoSi);
			mapaOrig.put("mostrarMenuConsultaEnvio",ConstantesBD.acronimoSi);		
		}
		else if(estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoAutorizado) || 
					estadoSolicitudAuto.equals(ConstantesIntegridadDominio.acronimoNegativo))
		{
			mapaOrig.put("mostrarMenuConsultaRespuesta",ConstantesBD.acronimoSi);
			
			if(tipoTramite.equals(ConstantesIntegridadDominio.acronimoInterna))
			{
				mapaOrig.put("mostrarMenuConsultaSolicitud",ConstantesBD.acronimoSi);
				mapaOrig.put("mostrarMenuConsultaEnvio",ConstantesBD.acronimoSi);
				
			}
		}	
				
		logger.info("..:Valores de inicializacion de indicadores >> estado solicitud >> "+estadoSolicitudAuto+" tipo tramite >> "+tipoTramite+" >> "+mapaOrig);		
		return mapaOrig;
	}
	
	//*************************************************************************************
	
	/**
	 * M�todo para preperar los datos para enviar
	 * @param Connection con
	 * @param DtoAutorizacion autorizacionDto
	 * @param DtoAutorizacion autorizacionDtoAux
	 * @param String [] codigosEvaluar
	 * @param String tipoOrdenIndicador
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static DtoAutorizacion prepararDtoAuxRespAutorizacion(
			Connection con,
			DtoAutorizacion autorizacionDto,
			DtoAutorizacion autorizacionDtoAux,
			String [] codigosEvaluar,			
			String tipoOrdenIndicador,
			UsuarioBasico usuario
			)
	{		
		autorizacionDtoAux = new DtoAutorizacion();
		
		//no existe informacion previa de la solicitud
		if(autorizacionDto.getDetalle().size()<=0)
			autorizacionDto.getDetalle().add(new DtoDetAutorizacion());
		
		autorizacionDtoAux.setCodigoPK(autorizacionDtoAux.getCodigoPK());
		autorizacionDtoAux.setTipo(autorizacionDto.getTipo());
		autorizacionDtoAux.setConsecutivo(autorizacionDto.getConsecutivo());
		autorizacionDtoAux.setAnioConsecutivo(autorizacionDto.getAnioConsecutivo());
		autorizacionDtoAux.setIdIngreso(autorizacionDto.getIdIngreso());
		autorizacionDtoAux.setIdSubCuenta(autorizacionDto.getIdSubCuenta());
		autorizacionDtoAux.setCodigoConvenio(autorizacionDto.getCodigoConvenio());
		autorizacionDtoAux.setIdCuenta(autorizacionDto.getIdCuenta());
		autorizacionDtoAux.setUsuarioModifica(usuario);
		autorizacionDtoAux.setDetalle(new ArrayList<DtoDetAutorizacion>());
		autorizacionDtoAux.getDetalle().add(autorizacionDto.getDetalle().get(0));		
		autorizacionDtoAux.getDetalle().get(0).setRespuestaDto(new DtoRespAutorizacion());
		autorizacionDtoAux.getDetalle().get(0).setCodigoEvaluar(autorizacionDto.getDetalle().get(0).getCodigoEvaluar());
		autorizacionDtoAux.getDetalle().get(0).setDetCargo(autorizacionDto.getDetalle().get(0).getDetCargo());
		autorizacionDtoAux.getDetalle().get(0).setCodigoEvaluar(codigosEvaluar[0].toString());
		autorizacionDtoAux.getDetalle().get(0).setTipoOrden(tipoOrdenIndicador);
		
		//Carga la informaci�n de la respuesta
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaAutorizacion(UtilidadFecha.getFechaActual());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setHoraAutorizacion(UtilidadFecha.getHoraActual());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setCantidadSolicitada(autorizacionDto.getDetalle().get(0).getCantidad()+"");
		
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setNombrePersonaRegistro(usuario.getNombreUsuario());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setPersonaRegistro(usuario.getLoginUsuario());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setCodCargoPersRegistro(usuario.getCodigoCargo()+"");
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setNombreCargoPersRegistro(usuario.getNombreCargo()); 
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setDetAutorizacion(autorizacionDto.getDetalle().get(0).getCodigoPK());
				
		logger.info("\n");
		logger.info("..:Cargando informacion basica del servicio/articulo. codigo evaluar >> "+codigosEvaluar[0].toString()+" tipo Orden Ind>> "+tipoOrdenIndicador);
		
		//carga informaci�n basica del servicio/articulo
		autorizacionDtoAux.getDetalle().set(0,
				Autorizaciones.cargarDatosBasicosSolicitud(
						con,
						codigosEvaluar[0].toString(),
						autorizacionDto.getDetalle().get(0),
						tipoOrdenIndicador));
		
		//Carga la informaci�n de la fecha Inicial de la solicitud
		if(!autorizacionDto.getDetalle().get(0).getFechaSolicitaOrden().equals(""))
		{
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaInicialAutorizada(autorizacionDto.getDetalle().get(0).getFechaSolicitaOrden());
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaFinalAutorizada(autorizacionDto.getDetalle().get(0).getFechaSolicitaOrden());
		}
		else
		{
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaInicialAutorizada(UtilidadFecha.getFechaActual());
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaFinalAutorizada(UtilidadFecha.getFechaActual());
		}
		
		//1 = ambulatoria, 2 = servicio/articulo
		if(tipoOrdenIndicador.equals(codInternoAutoSolicAmbula) || tipoOrdenIndicador.equals(codInternoAutoSolicSerMed))
			autorizacionDtoAux.setTipo(ConstantesIntegridadDominio.acronimoSolicitud);
	
		return autorizacionDtoAux;		
	}
	
	//*************************************************************************************
	
	/**
	 * M�todo para preperar los datos para enviar
	 * @param Connection con
	 * @param DtoAutorizacion autorizacionDto
	 * @param DtoAutorizacion autorizacionDtoAux
	 * @param String [] codigosEvaluar
	 * @param String tipoOrdenIndicador
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static DtoAutorizacion prepararDtoAuxRespAutorizacionAE(
			Connection con,
			DtoAutorizacion autorizacionDto,
			DtoAutorizacion autorizacionDtoAux,						
			String tipoAutorizacion,
			int codigoIngreso,
			int idCuenta,			
			int codigoSubCuenta,	
			UsuarioBasico usuario)
	{
		autorizacionDtoAux = new DtoAutorizacion();
		
		//no existe informacion previa de la solicitud
		if(autorizacionDto.getDetalle().size()<=0)
			autorizacionDto.getDetalle().add(new DtoDetAutorizacion());
		
		autorizacionDtoAux.setCodigoPK(autorizacionDtoAux.getCodigoPK());
		autorizacionDtoAux.setTipo(autorizacionDto.getTipo());
		autorizacionDtoAux.setConsecutivo(autorizacionDto.getConsecutivo());
		autorizacionDtoAux.setAnioConsecutivo(autorizacionDto.getAnioConsecutivo());
		autorizacionDtoAux.setIdIngreso(autorizacionDto.getIdIngreso());
		autorizacionDtoAux.setIdSubCuenta(autorizacionDto.getIdSubCuenta());
		autorizacionDtoAux.setCodigoConvenio(autorizacionDto.getCodigoConvenio());
		autorizacionDtoAux.setIdCuenta(autorizacionDto.getIdCuenta());
		autorizacionDtoAux.setUsuarioModifica(usuario);
		autorizacionDtoAux.setDetalle(new ArrayList<DtoDetAutorizacion>());
		autorizacionDtoAux.getDetalle().add(autorizacionDto.getDetalle().get(0));		
		autorizacionDtoAux.getDetalle().get(0).setRespuestaDto(new DtoRespAutorizacion());
		autorizacionDtoAux.getDetalle().get(0).setCodigoEvaluar(autorizacionDto.getDetalle().get(0).getCodigoEvaluar());
		autorizacionDtoAux.getDetalle().get(0).setDetCargo(autorizacionDto.getDetalle().get(0).getDetCargo());		
		autorizacionDtoAux.getDetalle().get(0).setTipoOrden(codInternoAutoAdmEst);
		autorizacionDtoAux.getDetalle().get(0).setTipoServicio("");
		
		//Carga la informaci�n de la respuesta
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaAutorizacion(UtilidadFecha.getFechaActual());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setHoraAutorizacion(UtilidadFecha.getHoraActual());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setCantidadSolicitada(autorizacionDto.getDetalle().get(0).getCantidad()+"");
		
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setNombrePersonaRegistro(usuario.getNombreUsuario());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setPersonaRegistro(usuario.getLoginUsuario());
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setCodCargoPersRegistro(usuario.getCodigoCargo()+"");
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setNombreCargoPersRegistro(usuario.getNombreCargo()); 
		autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setDetAutorizacion(autorizacionDto.getDetalle().get(0).getCodigoPK());
								
		//Carga la informaci�n de la fecha Inicial de la solicitud
		if(!autorizacionDto.getFechaSolicitud().equals(""))
		{
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaInicialAutorizada(autorizacionDto.getFechaSolicitud());
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaFinalAutorizada(autorizacionDto.getFechaSolicitud());
		}
		else
		{
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaInicialAutorizada(UtilidadFecha.getFechaActual());
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setFechaFinalAutorizada(UtilidadFecha.getFechaActual());
		}
		
		//carga la cantidad solicitada
		if(Utilidades.convertirAEntero(autorizacionDto.getCodigoPK()) <= 0)
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setCantidadSolicitada("0");
		else if(autorizacionDto.getDetalle().get(0).getCantidad() <= 0)
			autorizacionDtoAux.getDetalle().get(0).getRespuestaDto().setCantidadSolicitada("0");
		
		autorizacionDtoAux.setTipo(tipoAutorizacion);
		
		HashMap temp = getFechaAdmisionCuenta(
				con,
				idCuenta, 
				codigoIngreso,
				codigoSubCuenta);
		
		autorizacionDtoAux.setFechaAdmision(temp.get("fecha").toString());
		autorizacionDtoAux.setHoraAdmision(temp.get("hora").toString());
	
		return autorizacionDtoAux;		
	}
	
	//*************************************************************************************************
	
	/**
	 * Consulta la fecha de admision de la Cuenta
	 * @param Connection con
	 * @param String cuenta
	 * @param String ingreso
	 * @param String codigoSubcuenta
	 * */
	private static HashMap getFechaAdmisionCuenta(Connection con,int cuenta,int codigoIdIngreso,int codigoSubcuenta)
	{
		HashMap tmp = new HashMap();	
		tmp.put("codigoCuenta",cuenta);
		tmp.put("codigoSubcuenta",codigoSubcuenta);
		tmp.put("codigoIdIngreso",codigoIdIngreso);		
		return getAutorizacionesDao().getFechaAdmisionCuenta(con, tmp);
	}
	
	//*************************************************************************************************
	
	/**
	 * M�todo para preperar los datos para enviar
	 * @param Connection con
	 * @param DtoAutorizacion autorizacion
	 * @param String [] codigosEvaluar
	 * @param String tipoOrden
	 * @param int codigoIngreso
	 * @param int idCuenta
	 * @param int codigoSubCuenta	 
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static DtoAutorizacion prepararDtoAutorizacion(
			Connection con,
			DtoAutorizacion autorizacionOrigen,
			String [] codigosEvaluar,			
			String tipoOrdenIndicador,			
			int codigoIngreso,
			int idCuenta,
			int codigoSubCuenta,		
			UsuarioBasico usuario) throws IPSException
	{
		DtoAutorizacion autorizacion = new DtoAutorizacion();
		
		try
		{
			PropertyUtils.copyProperties(autorizacion,autorizacionOrigen);
		}catch(Exception e){
			logger.warn(e);
		}
		
		//*************************************************************
		//Informaci�n basica en el caso en que no se encuente registro
		if(Utilidades.convertirAEntero(autorizacion.getCodigoPK()) <= 0)
		{
			logger.info("..:No se encontro informaci�n en autorizaciones");
			autorizacion.setIdIngreso(codigoIngreso+"");

			ArrayList<HashMap<String,Object>> array = UtilidadesManejoPaciente.obtenerConveniosXIngreso(con,codigoIngreso,codigoSubCuenta+"");

			if(array.size()>0)
			{
				autorizacion.setCodigoConvenio(Utilidades.convertirAEntero(((HashMap)array.get(0)).get("codigo").toString()));
			}
			else{				
				autorizacion.setCodigoConvenio(ConstantesBD.codigoNuncaValido);
			}
			
			autorizacion.setIdCuenta(idCuenta+"");
			autorizacion.setIdSubCuenta(codigoSubCuenta+"");
			autorizacion.setUsuarioSolicitud(usuario);
			autorizacion.setPersonaSolicita(new InfoDatosInt(usuario.getCodigoPersona()));
			
			//Inserta los codigos por evaluar
			for(String codigo:codigosEvaluar)
			{
				DtoDetAutorizacion det = new DtoDetAutorizacion();
				det.setCodigoEvaluar(codigo);
				det.setTipoOrden(tipoOrdenIndicador);
				autorizacion.getDetalle().add(det);
			}			
			
			//*****************************************************************		
			//Si no se ha registrado informaci�n del anexo t�cnico y no hay tipo de cobertura se consulta de la sub cuenta			
			if(autorizacion.getCodigoTipoCobertura()<=0)
			{
				autorizacion.setTipoCobertura(UtilidadesManejoPaciente.obtenerTipoCoberturaSubCuenta(
						con, 
						codigoSubCuenta+""));
				
				if(autorizacion.getCodigoTipoCobertura()>0)
				{
					autorizacion.setCoberturaSaludResponsable(true);					
					autorizacion.setNombreCobertura(autorizacion.getTipoCobertura().getNombre());
					autorizacion.setCodigoTipoCobertura(autorizacion.getTipoCobertura().getCodigo());
				}
				else
					autorizacion.setCoberturaSaludResponsable(false);
			}		
			
			//*****************************************************************		
			//captura la informacion del codigo de via de ingreso
			int codigoViaIngreso = Cuenta.obtenerCodigoViaIngresoCuenta(con, idCuenta+"");
			
			//Si no se ha registro informaci�n del anexo t�cnico y no hay origen de atencion
			if(autorizacion.getEstado().equals("")
					&& autorizacion.getCodigoOrigenAtencion()<=0)
			{
				String tipoEvento = Cuenta.obtenerCodigoTipoEventoCuenta(con, idCuenta+"");
				autorizacion.setOrigenAtencion(obtenerOrigenAtencionXCuenta(con, idCuenta+"", codigoViaIngreso+"", tipoEvento));

				if(autorizacion.getCodigoOrigenAtencion()>0)
					autorizacion.setOrigenAtencionGuardada(true);
				else
					autorizacion.setOrigenAtencionGuardada(false);
			}

			//*****************************************************************
			//Si no se han registrado diagn�sticos se realiza la validaci�n
			if(autorizacion.getEstado().equals("")
					&& autorizacion.getDiagnosticos().size()==0)
			{
				ArrayList<Diagnostico> diagnosticos = UtilidadesHistoriaClinica.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso,false);
				for(Diagnostico diagnostico:diagnosticos)
				{
					DtoDiagAutorizacion diagAutorizacion = new DtoDiagAutorizacion();
					diagAutorizacion.setDiagnostico(diagnostico);
					
					autorizacion.getDiagnosticos().add(diagAutorizacion);				
				}

				if(autorizacion.getDiagnosticos().size()>0)
					autorizacion.setPuedoModificarDiagnosticos(false);
				else				
					autorizacion.setPuedoModificarDiagnosticos(true);			

				logger.info("..:obtiene los diagnosticos >> tama�o ."+autorizacion.getDiagnosticos().size()+" >> puedomodicardiag: "+autorizacion.isPuedoModificarDiagnosticos());
			}	
			
			autorizacion.setMapaDiagnosticos(new HashMap());
			autorizacion.diagnosticosDtoToHashMap();
			
			//Consulta la informaci�n del formato de solicitud del convenio
			HashMap datos = consultarInfoConvenio(con, codigoSubCuenta+"");
			if(Utilidades.convertirAEntero(datos.get("numRegistros").toString())>0)
				autorizacion.setFormatoPresenSolEnvioXConve(datos.get("formato_autorizacion").toString());			
			else
				autorizacion.setFormatoPresenSolEnvioXConve(ConstantesIntegridadDominio.acronimoFormatoEstandar);	
			
			//1 = ambulatoria, 2 = servicio/articulo
			if(tipoOrdenIndicador.equals(codInternoAutoSolicAmbula) || tipoOrdenIndicador.equals(codInternoAutoSolicSerMed))
				autorizacion.setTipo(ConstantesIntegridadDominio.acronimoSolicitud); 
		}
		else
		{
			//Si solo tiene un detalle se carga el estado
			if(autorizacion.getDetalle().size()==1 
					&& !autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(""))
				autorizacion.setEstado(autorizacion.getDetalle().get(0).getEstadoSolDetAuto());
			
			autorizacion.setMapaDiagnosticos(new HashMap());
			autorizacion.diagnosticosDtoToHashMap();
			autorizacion.setUsuarioModifica(usuario);
			
			//se copia el ultimo env�o			
			if(autorizacion.getDetalle().size() > 0 &&
				autorizacion.getDetalle().get((autorizacion.getDetalle().size()-1)).getEnvios().size() > 0)
			{
				int posDetalle = autorizacion.getDetalle().size()-1;				
								
				if(autorizacion.getDetalle().get(posDetalle).getEnvios().size()>0)
				{
					autorizacion.setEnvioSolicitud(autorizacion.getDetalle().get(posDetalle).getEnvios().get(0));			
					autorizacion.getEnvioSolicitud().getEntidadEnvio().setValue(autorizacion.getEnvioSolicitud().getEntidadEnvio().getCodigo()+ConstantesBD.separadorSplit+(autorizacion.getEnvioSolicitud().isEsEmpresa()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo));
				}
			}	
			
			logger.info("..:Carga informacion del ultimo envio : tama�o detalle >>  "+autorizacion.getDetalle().size()+" Info Envio actual >> "+autorizacion.getEnvioSolicitud().getEntidadEnvio()+" >> "+autorizacion.getEnvioSolicitud().getMedioEnvio());
			
			//actualiza el posible formato de solicitud con que fue guardado
			if(autorizacion.getCodigoTipoServicioSolicitado() > 0 && 
					autorizacion.getCodigoTipoCobertura() > 0 && 
						autorizacion.getCodigoOrigenAtencion() > 0)
				autorizacion.setFormatoPresenSolEnvioXConve(ConstantesIntegridadDominio.acronimoAnexo3Res003047);
			else
				autorizacion.setFormatoPresenSolEnvioXConve(ConstantesIntegridadDominio.acronimoFormatoEstandar);
		}
		
		autorizacion.setUsuarioModifica(usuario);
		autorizacion.getEnvioSolicitud().setConfirmarEnvio(true);
		
		return autorizacion;		
	}
	
	//***************************************************************************************	
	
	/**
	 * M�todo para preperar los datos para enviar Admision Estancia
	 * @param Connection con
	 * @param DtoAutorizacion autorizacion
	 * @param String [] codigosEvaluar
	 * @param String tipoOrden
	 * @param int codigoIngreso
	 * @param int idCuenta
	 * @param int codigoConvenio
	 * @param int codigoSubCuenta	 
	 * @param UsuarioBasico usuario
	 * @return
	 */
	public static DtoAutorizacion prepararDtoAutorizacionAE(
			Connection con,
			DtoAutorizacion autorizacionOrigen,						
			String tipoAutorizacion,			
			int codigoIngreso,
			int idCuenta,
			int codigoConvenio,
			int codigoSubCuenta,		
			UsuarioBasico usuario) throws IPSException
	{
		DtoAutorizacion autorizacion = new DtoAutorizacion();
		
		try
		{
			PropertyUtils.copyProperties(autorizacion,autorizacionOrigen);
		}catch(Exception e){
			logger.warn(e);
		}
		
		//*************************************************************
		//Informaci�n basica en el caso en que no se encuente registro
		if(Utilidades.convertirAEntero(autorizacion.getCodigoPK()) <= 0)
		{
			logger.info("..:No se encontro informaci�n en autorizaciones");
			autorizacion.setIdIngreso(codigoIngreso+"");

			if(codigoConvenio <= 0)
			{
				ArrayList<HashMap<String,Object>> array = UtilidadesManejoPaciente.obtenerConveniosXIngreso(con,codigoIngreso,codigoSubCuenta+"");

				if(array.size()>0)
				{
					autorizacion.setCodigoConvenio(Utilidades.convertirAEntero(((HashMap)array.get(0)).get("codigo").toString()));
					codigoConvenio = autorizacion.getCodigoConvenio();
				}
				else				
					autorizacion.setCodigoConvenio(ConstantesBD.codigoNuncaValido);			
			}
			else
				autorizacion.setCodigoConvenio(codigoConvenio);
			
			autorizacion.setIdCuenta(idCuenta+"");
			autorizacion.setIdSubCuenta(codigoSubCuenta+"");
			autorizacion.setUsuarioSolicitud(usuario);
			autorizacion.setPersonaSolicita(new InfoDatosInt(usuario.getCodigoPersona()));
					
			//*****************************************************************		
			//Si no se ha registrado informacion del anexo t�cnico y no hay tipo de cobertura se consulta de la sub cuenta			
			if(autorizacion.getCodigoTipoCobertura()<=0)
			{
				autorizacion.setTipoCobertura(UtilidadesManejoPaciente.obtenerTipoCoberturaSubCuenta(
						con, 
						codigoSubCuenta+""));
				
				if(autorizacion.getCodigoTipoCobertura()>0)
				{
					autorizacion.setCoberturaSaludResponsable(true);
					autorizacion.setNombreCobertura(autorizacion.getTipoCobertura().getNombre());
					autorizacion.setCodigoTipoCobertura(autorizacion.getTipoCobertura().getCodigo());
				}
				else
					autorizacion.setCoberturaSaludResponsable(false);
			}		
			
			//*****************************************************************		
			//captura la informacion del codigo de via de ingreso
			int codigoViaIngreso = Cuenta.obtenerCodigoViaIngresoCuenta(con, idCuenta+"");
			
			//Si no se ha registro informaci�n del anexo t�cnico y no hay origen de atencion
			if(autorizacion.getEstado().equals("")
					&& autorizacion.getCodigoOrigenAtencion()<=0)
			{
				String tipoEvento = Cuenta.obtenerCodigoTipoEventoCuenta(con, idCuenta+"");
				autorizacion.setOrigenAtencion(obtenerOrigenAtencionXCuenta(con, idCuenta+"", codigoViaIngreso+"", tipoEvento));

				if(autorizacion.getCodigoOrigenAtencion()>0)
					autorizacion.setOrigenAtencionGuardada(true);
				else
					autorizacion.setOrigenAtencionGuardada(false);
			}

			//*****************************************************************
			//Si no se han registrado diagn�sticos se realiza la validaci�n
			if(autorizacion.getEstado().equals("")
					&& autorizacion.getDiagnosticos().size()==0)
			{
				ArrayList<Diagnostico> diagnosticos = UtilidadesHistoriaClinica.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso,false);
				for(Diagnostico diagnostico:diagnosticos)
				{
					DtoDiagAutorizacion diagAutorizacion = new DtoDiagAutorizacion();
					diagAutorizacion.setDiagnostico(diagnostico);
					
					autorizacion.getDiagnosticos().add(diagAutorizacion);				
				}

				if(autorizacion.getDiagnosticos().size()>0)
					autorizacion.setPuedoModificarDiagnosticos(false);
				else				
					autorizacion.setPuedoModificarDiagnosticos(true);			

				logger.info("..:obtiene los diagnosticos >> tama�o ."+autorizacion.getDiagnosticos().size()+" >> puedomodicardiag: "+autorizacion.isPuedoModificarDiagnosticos());
			}	
			
			autorizacion.setMapaDiagnosticos(new HashMap());
			autorizacion.diagnosticosDtoToHashMap();
			
			//Consulta la informaci�n del formato de solicitud del convenio
			HashMap datos = consultarInfoConvenio(con, codigoSubCuenta+"");
			if(Utilidades.convertirAEntero(datos.get("numRegistros").toString())>0)
				autorizacion.setFormatoPresenSolEnvioXConve(datos.get("formato_autorizacion").toString());			
			else
				autorizacion.setFormatoPresenSolEnvioXConve(ConstantesIntegridadDominio.acronimoFormatoEstandar);	
			
			autorizacion.setTipo(tipoAutorizacion); 
		}
		else
		{
			//Si solo tiene un detalle se carga el estado
			if(autorizacion.getDetalle().size()==1 
					&& !autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(""))
			{
				autorizacion.setEstado(autorizacion.getDetalle().get(0).getEstadoSolDetAuto());
				autorizacion.setCantidadServicioAE(autorizacion.getDetalle().get(0).getCantidad()+"");
				autorizacion.setCodigoServicioAE(autorizacion.getDetalle().get(0).getServicioArticulo().getCodigo()+"");
				autorizacion.setNombreServicioAE(autorizacion.getDetalle().get(0).getServicioArticulo().getNombre());
			}
			
			autorizacion.setMapaDiagnosticos(new HashMap());
			autorizacion.diagnosticosDtoToHashMap();
			autorizacion.setUsuarioModifica(usuario);
			
			//se copia el ultimo env�o
			if(autorizacion.getDetalle().size() > 0 &&
				autorizacion.getDetalle().get((autorizacion.getDetalle().size()-1)).getEnvios().size() > 0)
			{
				int posDetalle = autorizacion.getDetalle().size()-1;				
								
				if(autorizacion.getDetalle().get(posDetalle).getEnvios().size()>0)
				{
					autorizacion.setEnvioSolicitud(autorizacion.getDetalle().get(posDetalle).getEnvios().get(0));			
					autorizacion.getEnvioSolicitud().getEntidadEnvio().setValue(autorizacion.getEnvioSolicitud().getEntidadEnvio().getCodigo()+ConstantesBD.separadorSplit+(autorizacion.getEnvioSolicitud().isEsEmpresa()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo));
				}
			}	
			
			logger.info("..:Carga informacion del ultimo envio : tama�o detalle >>  "+autorizacion.getDetalle().size()+" Info Envio actual >> "+autorizacion.getEnvioSolicitud().getEntidadEnvio()+" >> "+autorizacion.getEnvioSolicitud().getMedioEnvio());
			
			//actualiza el posible formato de solicitud con que fue guardado
			if(autorizacion.getCodigoTipoServicioSolicitado() > 0 && 
					autorizacion.getCodigoTipoCobertura() > 0 && 
						autorizacion.getCodigoOrigenAtencion() > 0)
			{
				autorizacion.setFormatoPresenSolEnvioXConve(ConstantesIntegridadDominio.acronimoAnexo3Res003047);
				autorizacion.setCoberturaSaludResponsable(true);
			}
			else
				autorizacion.setFormatoPresenSolEnvioXConve(ConstantesIntegridadDominio.acronimoFormatoEstandar);
			
			
		}
		
		autorizacion.setUsuarioModifica(usuario);
		autorizacion.getEnvioSolicitud().setConfirmarEnvio(true);
		
		return autorizacion;		
	}
	
	//***************************************************************************************
	
	/**
	 * Consultar los detalles de una autorizacion basados en el ingreso
	 * @param Connection con
	 * @param int codigoDetAutorizacion
	 * @param int codDetCarAmb
	 * @param int codigoInstitucion
	 * @param int tipoOrden
	 */
	public static DtoAutorizacion cargarAutorizacion(
			Connection con,
			int codigoDetAutorizacion,
			int codDetCarAmb,
			int codigoInstitucion,
			String tipoOrden,
			boolean activo)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoDetAutorizacion",codigoDetAutorizacion);
		parametros.put("codigoInstitucion",codigoInstitucion);
		
		parametros.put("codDetCarAmb",codDetCarAmb);
		parametros.put("tipoOrden",tipoOrden);		
		parametros.put("activo",activo?ConstantesBD.acronimoSi:"");
		
		return getAutorizacionesDao().cargarAutorizacion(con, parametros);
	}	
	
	//*******************************************************************************************
	
	
	/**
	 * Consultar la autorizacion con informacion del encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static DtoAutorizacion cargarAutorizacionXEncabezado(
			Connection con,
			String codigoPkAuto,
			String codigoCuenta,
			String subCuenta,
			String tipoAuto,
			int codigoInstitucion,
			boolean activo)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkAuto",codigoPkAuto);						
		parametros.put("codigoCuenta",codigoCuenta);
		parametros.put("subCuenta",subCuenta);
		parametros.put("tipoAuto",tipoAuto);
		parametros.put("codigoInstitucion",codigoInstitucion);
		parametros.put("activo",activo?ConstantesBD.acronimoSi:"");		
		
		return getAutorizacionesDao().cargarAutorizacionXEncabezado(con, parametros);
	}
	
	//*******************************************************************************************
	
	/**
	 * calcula la fecha actual mas el incremento 
	 * @param String vigencia
	 * @param String tipoVigencia
	 * */
	public static InfoDatosString calcularIncrementoFechaVigencia(
			String vigencia,
			String tipoVigencia,
			String fecha,
			String hora)
	{		
		if(Utilidades.convertirAEntero(vigencia) > 0 && 
				!tipoVigencia.equals(""))
		{
			int minutos = 0;
			
			if(tipoVigencia.equals(ConstantesIntegridadDominio.acronimoUnidadMedidaDias))
				minutos = Utilidades.convertirAEntero(vigencia) * 1440;
			else if(tipoVigencia.equals(ConstantesIntegridadDominio.acronimoHoras2))
				minutos = Utilidades.convertirAEntero(vigencia) * 60;

			String [] result = UtilidadFecha.incrementarMinutosAFechaHora(fecha,hora,minutos,false);

			if(!result[0].toString().equals("") && 
					!result[1].toString().equals(""))
			{
				return new InfoDatosString(result[0].toString(),result[1].toString());
			}
		}		

		return new InfoDatosString(fecha,hora);
	}
	
	//*******************************************************************************************
	
	/**
	 * Indica si la vigencia es activa o no 
	 * @param String vigencia
	 * @param String tipoVigencia
	 * @param String fechaFinalAutorizacion (opcional)
	 * */
	public static boolean tieneActivaVigencia(
			String vigencia,
			String tipoVigencia,
			String fechaFinalAutorizacion,
			String fechaAutorizacion,
			String horaAutorizacion) 
	{
		logger.info("..:Calculo vigencia activa >> vigencia >> "+vigencia+" tipo vigencia >> "+tipoVigencia+" fechaFinAutor >> "+fechaAutorizacion+" fechaAutori >> "+fechaAutorizacion+" horaAuto >> "+horaAutorizacion);
		if(Utilidades.convertirAEntero(vigencia) > 0 && 
				!tipoVigencia.equals(""))
		{
			int minutos = 0;
			
			if(tipoVigencia.equals(ConstantesIntegridadDominio.acronimoUnidadMedidaDias))			
				minutos = Utilidades.convertirAEntero(vigencia) * 1440;			
			else if(tipoVigencia.equals(ConstantesIntegridadDominio.acronimoHoras2))			
				minutos = Utilidades.convertirAEntero(vigencia) * 60;
			
			String fechaActual = UtilidadFecha.getFechaActual();
			String horaActual = UtilidadFecha.getHoraActual();
			String [] result = UtilidadFecha.incrementarMinutosAFechaHora(fechaAutorizacion,horaAutorizacion,minutos,false);
			
			if(!result[0].toString().equals("") && 
					!result[1].toString().equals(""))
			{
				if(UtilidadFecha.compararFechas(fechaActual,horaActual, result[0].toString(), result[1].toString()).isTrue())
					return false;
				else
					return true;
			}
		}
		else if(!fechaFinalAutorizacion.toString().equals(""))
		{
			String fechaActual = UtilidadFecha.getFechaActual();
			if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaActual,fechaFinalAutorizacion))
				return true;
			else
				return false;
		}
		
		return false;
	}
	
	//*******************************************************************************************
	
	/**
	 * determina si la vigencia se encuentra activa o no
	 * @param DtoAutorizacion dto
	 * */
	public static boolean tieneActivaVigencia(DtoAutorizacion dto)
	{
		if(Utilidades.convertirAEntero(dto.getCodigoPK()) <=0 
				|| Utilidades.convertirAEntero(dto.getDetalle().get(0).getRespuestaDto().getDetAutorizacion()) <=0)
			return false;
		else
		{
			if(Utilidades.convertirAEntero(dto.getDetalle().get(0).getRespuestaDto().getVigencia()) > 0 && 
				dto.getDetalle().get(0).getRespuestaDto().getTipoVigencia().getCodigo() > 0)
			{
				return tieneActivaVigencia(
						dto.getDetalle().get(0).getRespuestaDto().getVigencia(), 
						dto.getDetalle().get(0).getRespuestaDto().getTipoVigencia().getNombre(), 
						"",
						dto.getDetalle().get(0).getRespuestaDto().getFechaAutorizacion(),
						dto.getDetalle().get(0).getRespuestaDto().getHoraAutorizacion());				
			}
			else if(!dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada().equals("") && 
				!dto.getDetalle().get(0).getRespuestaDto().getFechaFinalAutorizada().equals(""))
			{
				
				return tieneActivaVigencia(
						"", 
						"", 
						dto.getDetalle().get(0).getRespuestaDto().getFechaFinalAutorizada(),
						"",
						"");			
			}
			else			
				return true;							
		}	
	}
	
	//*******************************************************************************************
	
	/**
	 * M�todo para obtener el origen de atencion verificando
	 * la informacion de la cuenta del paciente
	 * @param con
	 * @param String idCuenta
	 * @param String viaIngreso
	 * @param String tipoEvento
	 * @return
	 */
	public static InfoDatosInt obtenerOrigenAtencionXCuenta(Connection con,String idCuenta,String viaIngreso,String tipoEvento)
	{
		HashMap parametros = new HashMap(); 
		parametros.put("idCuenta", idCuenta);
		parametros.put("viaIngreso", viaIngreso);
		parametros.put("tipoEvento", tipoEvento);
		return getAutorizacionesDao().obtenerOrigenAtencionXCuenta(con, parametros);
	}
	
	//*******************************************************************************************
	
	/**
	 * Aplica validaciones antes de guardar la informacion
	 * @param DtoAutorizacion dto 
	 * */
	public static ActionErrors evaluarAccionGuardarSolEnviAuto(DtoAutorizacion dto)
	{
		ActionErrors errores = new ActionErrors();
		
		if(dto.getFormatoPresenSolEnvioXConve().equals(ConstantesIntegridadDominio.acronimoAnexo3Res003047))
		{
			//Valida el tipo de servicios solicitados		
			if(dto.getCodigoTipoServicioSolicitado() <= 0)
				errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Servico Solitado "));
	
			//Valida la cobertura en salud
			if(dto.getCodigoTipoCobertura() <= 0)
				errores.add("descripcion",new ActionMessage("errors.required","La Cobertura en Salud "));
			
			//Origen de la atencion
			if(dto.getCodigoOrigenAtencion() <= 0)
				errores.add("descripcion",new ActionMessage("errors.required","El origen de la Atenci�n "));
			
			int cont = 0;
			for(DtoDiagAutorizacion elemento:dto.getDiagnosticos())
			{
				if(elemento.getDiagnostico().isPrincipal() && !elemento.getDiagnostico().getAcronimo().equals(""))
					cont++;
			}
			
			if(cont<=0)
				errores.add("descripcion",new ActionMessage("errors.required","El Diagn�stico Principal "));
			
			cont = 0;
			for(DtoDiagAutorizacion elemento:dto.getDiagnosticos())
			{
				if(!elemento.getDiagnostico().isPrincipal() && !elemento.isEliminado())
					cont++;
			}
			if(cont > 2)
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Maximo Numero de Diagnosticos Relacionados (Seleccionados) Permitidos es de 2"));
		}
		
		//valida la persona que solicita
		if(dto.getUsuarioSolicitud().getCodigoPersona() <= 0)
			errores.add("descripcion",new ActionMessage("errors.required","La Persona que Solicita "));		
	
		if(dto.getEnvioSolicitud().getEntidadEnvio().getValue().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Entidad del Env�o "));
		
		if(dto.getEnvioSolicitud().getMedioEnvio().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Medio de Env�o "));	
		
		if(dto.getTipo().equals(ConstantesIntegridadDominio.acronimoEstancia) 
				|| dto.getTipo().equals(ConstantesIntegridadDominio.acronimoAdmision))
		{
			if(Utilidades.convertirAEntero(dto.getCodigoServicioAE()) <= 0)
				errores.add("descripcion",new ActionMessage("errors.required","El Servicio "));
			
			if(dto.getFormatoPresenSolEnvioXConve().equals(ConstantesIntegridadDominio.acronimoAnexo3Res003047))
			{
				if(Utilidades.convertirAEntero(dto.getCantidadServicioAE()) <= 0)
					errores.add("descripcion",new ActionMessage("errors.required","La cantidad del Servicio "));
			}
		}
		
		return errores;
	}
	
	
	//*******************************************************************************************
	
	/**
	 * Verifica los datos de la anulaci�n
	 * @param DtoRespAutorizacion dto
	 * */
	public static ActionErrors evaluarAccionAnularRespSol(DtoRespAutorizacion dto)
	{
		ActionErrors errores = new ActionErrors();
		
		if(dto.getFechaAnulacion().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Anulaci�n "));
		else if(!UtilidadFecha.validarFecha(dto.getFechaAnulacion()))
			errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Anulaci�n "));
		
		if(dto.getHoraAnulacion().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Hora de Anulaci�n "));
		else if(!UtilidadFecha.validacionHora(dto.getHoraAnulacion()).puedoSeguir)
			errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido"," de Anulaci�n "));
			
		if(errores.isEmpty())
		{
			if(!UtilidadFecha.compararFechas(dto.getFechaAnulacion(),dto.getHoraAnulacion(),dto.getFechaAutorizacion(),dto.getHoraAutorizacion()).isTrue())
				errores.add("descripcion",new ActionMessage("errors.notEspecific","La Fecha de Anulaci�n ["+dto.getFechaAnulacion()+" "+dto.getHoraAnulacion()+"] debe ser Mayor/Igual a la Fecha de Autorizaci�n de la Respuesta ["+dto.getFechaAutorizacion()+" "+dto.getHoraAutorizacion()+"] "));
			
			if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),dto.getFechaAnulacion(),dto.getHoraAnulacion()).isTrue())
				errores.add("descripcion",new ActionMessage("errors.notEspecific","La Fecha de Anulaci�n ["+dto.getFechaAnulacion()+" "+dto.getHoraAnulacion()+"] debe ser Menor/Igual a la Fecha de Actual ["+UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()+"] "));
		}
		
		if(dto.getMotivoAnulacion().trim().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Motivo de Anulaci�n "));
		
		return errores;	
	}
	
	//*******************************************************************************************
	
	/**
	 * Aplica validaciones antes de guardar la informacion
	 * @param DtoRespAutorizacion dto 
	 * */
	public static ActionErrors evaluarAccionGuardarRespSolAutoAsigCita(DtoRespAutorizacion dto)
	{
		ActionErrors errores = new ActionErrors();		
		
		// valida es estado de la respuesta
		if(!dto.getEstadorespauto().equals(""))
		{
			if(dto.getFechaAutorizacion().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Autorizaci�n "));
			else if(!UtilidadFecha.validarFecha(dto.getFechaAutorizacion()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Autorizaci�n "));
			if(dto.getHoraAutorizacion().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Hora de Autorizaci�n "));
			else if(!UtilidadFecha.validacionHora(dto.getHoraAutorizacion()).puedoSeguir)
				errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido"," de Autorizaci�n "));
			
			if(dto.getEstadorespauto().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				/*
				 * Comentado x tarea 135285
				 * 
				if(dto.getTipoCobertura().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Cobertura "));
				if(dto.getValorCobertura().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El Valor de la Cobertura "));
				if(dto.getTipoPagoPaciente().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Pago el Paciente "));
				if(dto.getValorPagoPaciente().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El Valor de la Cobertura "));
				*/
				
				if(dto.getServicio().getNombre().equals(ConstantesBD.codigoServicioCamaEstancias)){
					if(dto.getFechaInicialAutorizada().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial Autorizada "));
					if(dto.getFechaFinalAutorizada().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final Autorizada "));
					if(!dto.getVigencia().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Vigencia NO "));
					if(dto.getTipoVigencia().getCodigo()!=ConstantesBD.codigoNuncaValido)
						errores.add("descripcion",new ActionMessage("errors.required","EL Tipo Vigencia NO "));
					dto.setCantidadAutorizada("1");
				}else
					if(dto.getCantidadAutorizada().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Cantidad Autorizada "));
			}
		}else{
			errores.add("descripcion",new ActionMessage("errors.required","Estado de Autorizaci�n "));
		}
		
		// valida vigencia y tipo si es el caso
		if(dto.getVigencia().equals("")){
			if(dto.getTipoVigencia().getCodigo()==ConstantesBD.codigoNuncaValido)
				errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Vigencia "));
		}
		
		// valida numero de autorizacion
		if(dto.getNumeroAutorizacion().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El N�mero de Autorizaci�n "));
		
		// valida la persono qe autoriza
		if(dto.getPersonaAutoriza().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","La Persona que Autoriza "));
		
		
		return errores;
	}
	
	//**********************************************************************
	/**
	 * Aplica validaciones antes de guardar la informacion de la respuesta desde autorizaciones
	 * @param DtoRespAutorizacion dto 
	 * */
	public static ActionErrors validarGuardarRespuestaSol(DtoAutorizacion dto,String tipoOrdenInd)
	{
		ActionErrors errores = new ActionErrors();
		boolean indFechaHoraAuto = true, indFechaIniAuto = true;
		
		// valida el estado de la respuesta
		if(!dto.getDetalle().get(0).getRespuestaDto().getEstadorespauto().equals(""))
		{
			//Solicitud serv/med
			if(tipoOrdenInd.equals(codInternoAutoSolicSerMed))
				if(Utilidades.convertirAEntero(dto.getDetalle().get(0).getDetCargo())<=0)
					errores.add("descripcion",new ActionMessage("errors.required","El codigo del Detalle del Cargo "));

			//vigencia
			if(!dto.getDetalle().get(0).getRespuestaDto().getVigencia().equals("") && 
					dto.getDetalle().get(0).getRespuestaDto().getTipoVigencia().getCodigo() < 0)
				errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Vigencia "));
			
			if(dto.getDetalle().get(0).getRespuestaDto().getFechaAutorizacion().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Autorizaci�n "));
				indFechaHoraAuto = false;
			}
			else if(!UtilidadFecha.validarFecha(dto.getDetalle().get(0).getRespuestaDto().getFechaAutorizacion()))
			{
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Autorizaci�n "));
				indFechaHoraAuto = false;
			}
			
			if(dto.getDetalle().get(0).getRespuestaDto().getHoraAutorizacion().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Hora de Autorizaci�n "));
				indFechaHoraAuto = false;
			}
			else if(!UtilidadFecha.validacionHora(dto.getDetalle().get(0).getRespuestaDto().getHoraAutorizacion()).puedoSeguir)
			{
				errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido"," de Autorizaci�n "));
				indFechaHoraAuto = false;
			}
			
			if(dto.getDetalle().get(0).getRespuestaDto().getNumeroAutorizacion().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Numero de Autorizaci�n "));
			
			if(dto.getDetalle().get(0).getCantidad() <= 0 
					&& !tipoOrdenInd.equals(codInternoAutoAdmEst))
				errores.add("descripcion",new ActionMessage("errors.required","La Cantidad Solicitada "));
						
			if(dto.getDetalle().get(0).getRespuestaDto().getEstadorespauto().equals(ConstantesIntegridadDominio.acronimoAutorizado+""))
			{
				if(dto.getDetalle().get(0).getRespuestaDto().getCantidadAutorizada().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Cantidad Autorizada "));
				
				if(dto.getDetalle().get(0).getTipoServicio().equals(ConstantesBD.codigoServicioCamaEstancias+"") || 
						tipoOrdenInd.equals(codInternoAutoAdmEst))
				{
					//las fechas para los servicios de estancia son las mismas
					if(!tipoOrdenInd.equals(codInternoAutoAdmEst))
						dto.getDetalle().get(0).getRespuestaDto().setFechaFinalAutorizada(dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada());
						
					if(dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada().equals(""))
					{
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial de Autorizaci�n "));
						indFechaIniAuto = false;
					}
					else if(!UtilidadFecha.validarFecha(dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada()))
					{
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial de Autorizaci�n "));
						indFechaIniAuto = false;
					}
					
					if(dto.getDetalle().get(0).getRespuestaDto().getFechaFinalAutorizada().equals(""))
					{
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final de Autorizaci�n "));
						indFechaIniAuto = false;
					}
					else if(!UtilidadFecha.validarFecha(dto.getDetalle().get(0).getRespuestaDto().getFechaFinalAutorizada()))
					{
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Autorizaci�n "));
						indFechaIniAuto = false;
					}
					
					//validaciones de rangos
					if(tipoOrdenInd.equals(codInternoAutoAdmEst))
					{
						if(!dto.getFechaAdmision().equals("") 
								&& !dto.getHoraAdmision().equals("") 
									&& indFechaIniAuto 
										&& indFechaHoraAuto )
						{	
							if(!UtilidadFecha.compararFechas(
									dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada(),
									dto.getHoraAdmision(),
									dto.getFechaAdmision(),
									dto.getHoraAdmision()).isTrue())
							{
								errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," Inicial de Autorizaci�n "+dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada()," de Admisiones "+dto.getFechaAdmision()));								
							}
						}
					}
					else
					{
						if(indFechaIniAuto && indFechaHoraAuto )
						{
							if(!UtilidadFecha.compararFechas(
									dto.getDetalle().get(0).getRespuestaDto().getFechaAutorizacion(),
									dto.getDetalle().get(0).getRespuestaDto().getHoraAutorizacion(),
									dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada(),
									dto.getDetalle().get(0).getRespuestaDto().getHoraAutorizacion()).isTrue())
							{
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Inicial de Autorizaci�n "+dto.getDetalle().get(0).getRespuestaDto().getFechaInicialAutorizada()," de Autorizaci�n "+dto.getDetalle().get(0).getRespuestaDto().getFechaAutorizacion()));								
							}
						}
					}
				}
				
				//Cobertura
				if(!dto.getDetalle().get(0).getRespuestaDto().getValorCobertura().equals("") && 
						dto.getDetalle().get(0).getRespuestaDto().getTipoCobertura().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Cobertura "));
				
				//Pago Paciente
				if(!dto.getDetalle().get(0).getRespuestaDto().getValorPagoPaciente().equals("") && 
						dto.getDetalle().get(0).getRespuestaDto().getTipoPagoPaciente().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Pago Paciente "));			
			}
			
			if(dto.getDetalle().get(0).getRespuestaDto().getPersonaAutoriza().trim().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Persona que Autoriza "));		
			
			if(!dto.getDetalle().get(0).getRespuestaDto().getPersonaRecibe().trim().equals("") && 
					Utilidades.convertirAEntero(dto.getDetalle().get(0).getRespuestaDto().getCargoPersRecibe()) < 0)
				errores.add("descripcion",new ActionMessage("errors.required","El Cargo de la Persona que Recibe "));
						
			if(tipoOrdenInd.equals(codInternoAutoAdmEst))
			{
				if(dto.getDetalle().get(0).getServicioArticulo().getCodigo() <= 0 
						&& Utilidades.convertirAEntero(dto.getCodigoServicioAE()) <= 0)
					errores.add("descripcion",new ActionMessage("errors.required","El Servicio "));				
			}
		}
		else{
			errores.add("descripcion",new ActionMessage("errors.required","Estado de Autorizaci�n "));
		}
		
		return errores;
	}
	
	//**********************************************************************
	
	/**
	 * Actualizar una Autorizacion
	 * @param Connection con
	 * @param DtoAutorizacion dtoAutorizacion
	 * @param String [] codigosInsertar
	 */
	public static int actualizarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion,String [] codigosInsertar)
	{			
		//Actualiza la informacion de la autorizacion
		if(getAutorizacionesDao().actualizarAutorizacion(con, dtoAutorizacion) >0)
		{
												
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	//**********************************************************************
	
	/**
	 * Insertar Respuesta de autorizaci�n
	 * @param Connection con
	 * @param DtoRespAutorizacion dto
	 * */
	public static int insertarRespuestaAutorizacion(Connection con,DtoRespAutorizacion dto)
	{
		return getAutorizacionesDao().insertarRespuestaAutorizacion(con,dto);
	}
	
	//**********************************************************************
	
	/**
	 * Insertar una Autorizacion
	 * @param Connection con
	 * @param DtoAutorizacion dtoAutorizacion
	 * @param UsuarioBasico usuario
	 */
	public InfoDatosInt insertarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, AutorizacionesForm forma)
	{		
		int codigopk = getAutorizacionesDao().insertarAutorizacion(con, dtoAutorizacion);		
		int codigoDetalle = ConstantesBD.codigoNuncaValido; 
		int codigoRespuesta = ConstantesBD.codigoNuncaValido;
		
		if(codigopk > 0)
		{
			this.mapaAux.put("codigoPkAuto", codigopk);			
			
			//Detalle
			for(DtoDetAutorizacion elemento:dtoAutorizacion.getDetalle())
			{
				elemento.setAutorizacion(codigopk+"");
				
				if(dtoAutorizacion.getTipo().equals(ConstantesIntegridadDominio.acronimoAdmision) || 
						dtoAutorizacion.getTipo().equals(ConstantesIntegridadDominio.acronimoEstancia))
					codigoDetalle = getAutorizacionesDao().insertarDetalleAutorizacionAE(con,elemento);
				else
					codigoDetalle = getAutorizacionesDao().insertarDetalleAutorizacion(con,elemento);
				
				this.mapaAux.put(elemento.getCodigoEvaluar(),codigoDetalle);
				
				logger.info("..:codigo nuevo detalle autorizacion >>> "+codigoDetalle);
				
				
				
				if(codigoDetalle <= 0)
					return new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
				else
				{					
					if(!dtoAutorizacion.getEnvioSolicitud().getMedioEnvio().equals(""))
					{
						logger.info("..:Insertar informacion del envio para el detalle >> "+codigoDetalle);
						dtoAutorizacion.getEnvioSolicitud().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
						dtoAutorizacion.getEnvioSolicitud().setHoraModifica(UtilidadFecha.getHoraActual());
						dtoAutorizacion.getEnvioSolicitud().setUsuarioModifica(dtoAutorizacion.getUsuarioModifica());
						dtoAutorizacion.getEnvioSolicitud().setDetAutorizacion(codigoDetalle+"");
						
						//********************************************************************************************************************
				        // Generacion del archivo XML de Solicitud Autorizaciones
						String pathArchivoIncoxml = "";
						
						
						if(dtoAutorizacion.getEnvioSolicitud().getMedioEnvio().equals(ConstantesIntegridadDominio.acronimoIntercambioElectDatos))
						{
				        	if((pathArchivoIncoxml= xmlReportSolAuto(con, usuario, paciente, request, forma,codigopk))==null)
				        	{
				        		dtoAutorizacion.setPathArchivoIncoXml(pathArchivoIncoxml);
				        		dtoAutorizacion.setArchivoInconGenerado(ConstantesBD.acronimoSi);
				        	}else
				        		dtoAutorizacion.setArchivoInconGenerado(ConstantesBD.acronimoNo);
				        }else
				        	dtoAutorizacion.setArchivoInconGenerado(ConstantesBD.acronimoNo);
						dtoAutorizacion.getEnvioSolicitud().setUrlArchivoIncoXmlDes(pathArchivoIncoxml);
						logger.info("path Archivo: "+dtoAutorizacion.getEnvioSolicitud().getUrlArchivoIncoXmlDes());
				        // Fin Generacion del archivo XML de Inconsitencias
				        //********************************************************************************************************************
						
						if(getAutorizacionesDao().insertarEnvioAutorizacion(con,dtoAutorizacion.getEnvioSolicitud()) <= 0)
						{
							logger.info("error al ingresar la informaci�n de envio para el datalle >> "+codigoDetalle);
							return new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
						}
					}
					
					// se ingresa la respuesta relaciona al detalle de autorizacion
					if(!elemento.getRespuestaDto().getNumeroAutorizacion().equals(""))
					{
						logger.info("..:se inserta respuesta autorizacion");
						elemento.getRespuestaDto().setDetAutorizacion(String.valueOf(codigoDetalle));
						codigoRespuesta = getAutorizacionesDao().insertarRespuestaAutorizacion(con, elemento.getRespuestaDto());
						
						if(codigoRespuesta >= 0)
						{						
							if(elemento.getRespuestaDto().getAdjuntos().size()>0)
							{
								//se ingresa los adjuntos relacionados a la respuesta
								for(DtoAdjAutorizacion adjresauto:elemento.getRespuestaDto().getAdjuntos())
								{
									if((!adjresauto.getNombreArchivo().equals(""))&&
											(!adjresauto.getNombreOriginal().equals(""))&&
												(!adjresauto.isEliminado()))
									{										
										adjresauto.setAutorizacion(String.valueOf(codigoRespuesta));
										adjresauto.setUsuarioModifica(elemento.getUsuarioModifica());
										if(insertarAdjuntoRespAutorizacion(con, adjresauto)<=0)
										{
											logger.info("Error al ingresar los archivos adjuntos de respuesta");
											return new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
										}
									}
								}
							}
						}
						else
							return new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");	
					}
				}					
			}		
			
			logger.info("..:Insertar Diagnosticos, numero : "+dtoAutorizacion.getDiagnosticos().size());
			//Se insertan los diagnosticos
			for(DtoDiagAutorizacion elemento:dtoAutorizacion.getDiagnosticos())
			{
				if(!elemento.isEliminado())
				{
					elemento.setAutorizacion(codigopk+"");
					elemento.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					elemento.setHoraModifica(UtilidadFecha.getHoraActual());
					elemento.setUsuarioModifica(dtoAutorizacion.getUsuarioSolicitud());					
				
					if(getAutorizacionesDao().insertarDiagnosticoAutorizacion(con,elemento) <= 0)
						return new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
				}
			}
			
			//Se inserta los archivos adjuntos			
			for(DtoAdjAutorizacion elemento:dtoAutorizacion.getAdjuntos())
			{
				if(!elemento.isEliminado())
				{
					elemento.setAutorizacion(codigopk+"");
					elemento.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					elemento.setHoraModifica(UtilidadFecha.getHoraActual());
					elemento.setUsuarioModifica(dtoAutorizacion.getUsuarioSolicitud());

					if(!elemento.getNombreArchivo().equals("") && 
						!elemento.getNombreOriginal().equals(""))
					{
						if(getAutorizacionesDao().insertarAdjuntoAutorizacion(con,elemento) <= 0)
							return new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
					}
				}
			}

			return new InfoDatosInt(codigopk,codigoDetalle+"");
		}
		
		return new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
	}
	
	//**********************************************************************
	
	/**
	 * guarda informacion de la seccion envio
	 * @param Connection con
	 * @param DtoAutorizacion dto
	 * */
	public static int guardarInfoSeccionEnvio(Connection con,DtoAutorizacion dto)
	{
		//Se confirma la informaci�n del env�o
		if(dto.getEnvioSolicitud().isConfirmarEnvio())
		{
			dto.getEnvioSolicitud().setDetAutorizacion(dto.getDetalle().get(0).getCodigoPK());
			dto.getEnvioSolicitud().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			dto.getEnvioSolicitud().setHoraModifica(UtilidadFecha.getHoraActual());
			dto.getEnvioSolicitud().setUsuarioModifica(dto.getUsuarioModifica());
			
			if(getAutorizacionesDao().insertarEnvioAutorizacion(con,dto.getEnvioSolicitud()) <= 0)
				return ConstantesBD.codigoNuncaValido;
		}

		//actualiza la informaci�n de la observaci�n
		if(!dto.getNuevaObservacion().equals(""))
			if(updateObservacionAutorizacion(
					con,
					dto.getCodigoPK(),
					dto.getObservaciones(),
					dto.getNuevaObservacion(),
					dto.getUsuarioModifica()) <= 0)
				return ConstantesBD.codigoNuncaValido;

		//actualiza la informaci�n de los archivos adjuntos
		for(DtoAdjAutorizacion elemento:dto.getAdjuntos())
		{				
			if(elemento.isEliminado() && 
					Utilidades.convertirAEntero(elemento.getCodigoPK().toString())>0)
			{			
				if(deleteArchivoAdjAutorizacion(con,elemento.getCodigoPK())<=0)
					return ConstantesBD.codigoNuncaValido;
			}
			else if(!elemento.isEliminado()
					&& Utilidades.convertirAEntero(elemento.getCodigoPK().toString())<=0)
			{
				elemento.setAutorizacion(dto.getCodigoPK()+"");
				elemento.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				elemento.setHoraModifica(UtilidadFecha.getHoraActual());
				elemento.setUsuarioModifica(dto.getUsuarioSolicitud());

				if(!elemento.getNombreArchivo().equals("") && 
					!elemento.getNombreOriginal().equals(""))
				{
					if(getAutorizacionesDao().insertarAdjuntoAutorizacion(con,elemento) <= 0)
						return ConstantesBD.codigoNuncaValido;
				}
			}			
		}
		return 1;
	}

	//**********************************************************************	
	
	/**
	 * Actualiza la observacion de autorizacion
	 * @param Connection con
	 * @param String codigoPkAutorizacion
	 * @param String observaciones
	 * @param String nuevaObservacion
	 * @param UsuarioBasico usuarioModifica
	 * */
	public static int updateObservacionAutorizacion(
			Connection con,
			String codigoPkAutorizacion,
			String observaciones,
			String nuevaObservacion,
			UsuarioBasico usuarioModifica)
	{
		HashMap parametros = new HashMap();
		parametros.put("usuario_modifica", usuarioModifica.getLoginUsuario());
		parametros.put("codigo", codigoPkAutorizacion);
		observaciones = UtilidadTexto.deshacerCodificacionHTML(UtilidadTexto.agregarTextoAObservacion(observaciones,nuevaObservacion,usuarioModifica,true));	
		parametros.put("observaciones",observaciones.length()>4000?observaciones.subSequence(0,3999):observaciones);
		
		if(getAutorizacionesDao().updateObservacionAutorizacion(con, parametros) <= 0)
			return ConstantesBD.codigoNuncaValido;
		
		return 1;
	}	
	//*********************************************************************
	
	/**
	 * Actualizar Autorizacion Detalle de Autorizacion y detalle cargos
	 * @param con
	 * @param parametros
	 * @return
	 */
	
	public static int actualizarAutorizacionyDetalle (Connection con, String codigoOrden, String codigoCuenta, String codigoConvenioCargo, String numSolicitud, String codigoDetCargo, String codigoSubCuenta, String articulo )
	{
		HashMap parametros=new HashMap();
		parametros.put("codordenambulatoria", codigoOrden);
		parametros.put("cuenta", codigoCuenta);
		parametros.put("convenio", codigoConvenioCargo);
		parametros.put("numsolicitud", numSolicitud);
		parametros.put("detcargo", codigoDetCargo);
		parametros.put("articulo", articulo);
		parametros.put("subcuenta", codigoSubCuenta);
			
		return getAutorizacionesDao().actualizarAutorizacionyDetalle(con, parametros);
	}
	//**********************************************************************
	
	/**
	 * Eliminacion de Archivos Adjuntos a la Solicitud de Autorizacion
	 */
	public static int deleteArchivoAdjAutorizacion(Connection con,String codigopk)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigo_pk", codigopk);
		
		return getAutorizacionesDao().deleteArchivoAdjAutorizacion(con, parametros);
	}
	
	//**********************************************************************
	
	
	/**
	 * M�todo para obtener la persona que hace la solicitud 
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static HashMap<String, Object> obtenerPersonaSolicitad(Connection con, String numero_solicitud , String tipo_solicitud,String tipoOrdenInd){
		HashMap parametros = new HashMap();
		parametros.put("numero_solicitud", numero_solicitud);
		parametros.put("tipo_solicitud", tipo_solicitud); 
		parametros.put("tipoOrdenInd", tipoOrdenInd);
		return getAutorizacionesDao().obtenerPersonaSolicitad(con, parametros);
	}
	
	//**************************************************************************
	
	/**
	 * Modificar El Estado del Detalle de la Autorizacion
	 * @param Connection con
	 * @param String estado
	 * @param String loginUsuario
	 * @param String codigoDetalleAuto
	 */
	public static int updateEstadoDetAutorizacion(Connection con,String estado,String loginUsuario,String codigoDetalleAuto) 
	{
		 HashMap parametros = new HashMap();
		 parametros.put("estado",estado);
		 parametros.put("usuario_modifica",loginUsuario);
		 parametros.put("codigo",codigoDetalleAuto);		 
		 return getAutorizacionesDao().updateEstadoDetAutorizacion(con, parametros);
	}
	
	//**************************************************************************
	
	/**
	 * M�todo para inserta adjuntos de una respuesta  
	 * @param con
	 * @param DtoAdjAutorizacion dtoAdjAutorizacion
	 * @return
	 */
	public static int insertarAdjuntoRespAutorizacion(Connection con, DtoAdjAutorizacion dtoAdjAutorizacion){
		return getAutorizacionesDao().insertarAdjuntoRespAutorizacion(con, dtoAdjAutorizacion);
	}
	
	//*****************************************************************************
	
	/**
	 * consulta la informacion del usuario que solicita autorizacion
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public static UsuarioBasico getUsarioSolicitaAutorizacion(
			Connection con,
			String numeroSolicitud,
			String tipoSolicitud,
			String tipoOrdenInd,
			UsuarioBasico usuario)
	{
		if(ValoresPorDefecto.getValoresDefectoUsuarioaReportarenSolicitAuto(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoPersonaqueSolicista))
		{
			HashMap parametros =  obtenerPersonaSolicitad(con, numeroSolicitud, tipoSolicitud ,tipoOrdenInd);
			
			if(Utilidades.convertirAEntero(parametros.get("codigo_persona_sol").toString())>0)
			{
				UsuarioBasico usuarioBas = new UsuarioBasico();
				
				try{
					usuarioBas.cargarUsuarioBasico(con,Utilidades.convertirAEntero(parametros.get("codigo_persona_sol").toString()));
				}catch (Exception e) {
					logger.info("error en getUsarioSolicitaAutorizacion "+e);
				}
				
				return usuarioBas;
			}
			else
				return usuario;
			
		}
		else if(ValoresPorDefecto.getValoresDefectoUsuarioaReportarenSolicitAuto(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoUsuarioqueTramitaSolicitud))		
			return usuario;		
		
		return usuario;
	}	
	
	//*****************************************************************************
	/**
	 * M�todo para modificar la cantidad del Detalle Autorizacion  
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static int updateCantidadAutoDetAutorizacion(Connection con, int cantidad, String loginUsuModifica, String codigo){
		HashMap parametros = new HashMap();
		parametros.put("cantidad_autorizacion", cantidad);
		parametros.put("usuario_modifica", loginUsuModifica);
		parametros.put("codigo", codigo);
		return getAutorizacionesDao().updateCantidadAutoDetAutorizacion(con, parametros);
	}
	
	//********************************************************************************
	
	/**
	 * M�todo para modificar la cantidad autorizada de la Repuesta de la Autorizacion  
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static int updateCantidadAutoRespAutorizacion(Connection con, int cantidadAutorizada, String codRespAuto){
		HashMap parametros = new HashMap();
		parametros.put("cantidad_autorizada", cantidadAutorizada);
		parametros.put("det_autorizacion", codRespAuto);
		return getAutorizacionesDao().updateCantidadAutoRespAutorizacion(con, parametros);
	}
	
	//***********************************************************************************
	
	/**
	 * Consulta informacion basica del detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @param DtoDetAutorizacion dtoDetAutorizacion
	 * */
	public static DtoDetAutorizacion cargarDatosBasicosSolicitud(
			Connection con,
			String codDetCarAmb,
			DtoDetAutorizacion dtoDetAutorizacion,
			String tipoOrden)
	{
		HashMap parametros = new HashMap();
		parametros.put("codDetCarAmb",codDetCarAmb);
		parametros.put("tipoOrden",tipoOrden);
		parametros = getAutorizacionesDao().datosBasicosSolicitud(con, parametros);
		
		int numRegistros = Utilidades.convertirAEntero(parametros.get("numRegistros").toString());
		
		if(numRegistros > 0)
		{
			if(Utilidades.convertirAEntero(dtoDetAutorizacion.getCodigoPK())<=0)
			{
				dtoDetAutorizacion.setNumeroSolicitud(parametros.get("numeroSolicitud_0").toString());
				dtoDetAutorizacion.setOrdenAmbulatoria(parametros.get("ordenAmbulatoria_0").toString());
				dtoDetAutorizacion.setDetCargo(parametros.get("detCargo_0").toString());
				dtoDetAutorizacion.setCantidad(Utilidades.convertirAEntero(parametros.get("cantidad_0").toString()));
				dtoDetAutorizacion.getRespuestaDto().setCantidadSolicitada(parametros.get("cantidad_0").toString());
				
				if(Utilidades.convertirAEntero(parametros.get("servicio_0").toString()) > 0)
				{
					dtoDetAutorizacion.getServicioArticulo().setCodigo(Utilidades.convertirAEntero(parametros.get("servicio_0").toString()));								
					dtoDetAutorizacion.getServicioCx().setCodigo(Utilidades.convertirAEntero(parametros.get("servicioCx_0").toString()));
					dtoDetAutorizacion.getTipoAsocio().setCodigo(Utilidades.convertirAEntero(parametros.get("tipoAsocio_0").toString()));
					dtoDetAutorizacion.setEsServicio(2);
				}
				else
				{
					dtoDetAutorizacion.getServicioArticulo().setCodigo(Utilidades.convertirAEntero(parametros.get("articulo_0").toString()));
					dtoDetAutorizacion.setEsServicio(3);
				}
			}	
			else
			{
				logger.info("..:Existe informaci�n previa del detalle autorizaciones");
			}
			
			dtoDetAutorizacion.setTipoSolicitud(parametros.get("tipoSolicitud_0").toString());
			dtoDetAutorizacion.setEstadoHCSerArt(parametros.get("estado_0").toString());
			dtoDetAutorizacion.setTipoServicio(parametros.get("tipoServicio_0").toString());			
			dtoDetAutorizacion.setFechaSolicitaOrden(parametros.get("fechaSolicitud_0").toString());
			dtoDetAutorizacion.setCodigoPkAnterior(parametros.get("codigoDetAuto_0").toString());
			dtoDetAutorizacion.setUrgente(UtilidadTexto.getBoolean(parametros.get("urgente_0").toString())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		}
		else
		{
			logger.info("..:No encontro informacion del servicio/articulo para actualizar");
		}
		
		return dtoDetAutorizacion;
	}
	
	//**********************************************************************************************
	
	/**
	 * consulta la informacion del convenio
	 * @param Connection con
	 * @param String codigoSubCuenta
	 */
	public static HashMap consultarInfoConvenio(Connection con,String codigoSubCuenta)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoSubCuenta", codigoSubCuenta);
		return getAutorizacionesDao().consultarInfoConvenio(con, parametros);
	}		
	
	//********************************************************************************************
	
	/**
	 * Insertar Detalle Autorizacion Estancia
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static int insertarDetAutorizacionEstancia(Connection con, DtoDetAutorizacionEst dtoDetAutorizacionEst){
		return getAutorizacionesDao().insertarDetAutorizacionEstancia(con, dtoDetAutorizacionEst);
	}
	
	//********************************************************************************************
	
	/**
	 * Busca Estancia y Valida
	 * @param con
	 * @param HashMap parametros
	 * @return HashMap
	 */
	public static ArrayList<DtoDetAutorizacionEst> cargarInfoBasicaDetAutorizacionEstancia(
			Connection con,		 
			int servicio, 
			String sub_cuenta, 
			String fecha_ini, 
			String fecha_fin, 
			String tipoProceso)
			
	{
		HashMap parametros = new HashMap();
			
		parametros.put("sub_cuenta", sub_cuenta);
		
		if(tipoProceso.equals(ConstantesIntegridadDominio.acronimoManual))
		{
			parametros.put("servicio", servicio);
			parametros.put("fecha_ini", fecha_ini);
			parametros.put("fecha_fin", fecha_fin);
		}
		
		parametros.put("tipo_consulta",tipoProceso);
		return getAutorizacionesDao().cargarInfoBasicaDetAutorizacionEstancia(con, parametros);
	}
	
	//********************************************************************************************
	
	/**
	 * Insertar Solicitud de Autorizaciones de Nivel ADM/EST
	 * @param con
	 * @param ArrayList<DtoDetAutorizacionEst>
	 * @param DtoDetAutorizacion
	 * @return ArrayList<DtoDetAutorizacionEst>
	 */
	public static ArrayList<DtoDetAutorizacionEst> insertarSolAutorizcionAdmEst(Connection con, ArrayList<DtoDetAutorizacionEst> dto, DtoDetAutorizacion dtoDetAutorizaciones)
	{
		ArrayList<DtoDetAutorizacionEst> dtopreguntar = new ArrayList<DtoDetAutorizacionEst>();
		DtoDetAutorizacionEst dtoAdmEst = new DtoDetAutorizacionEst();
		String codigoPK = dtoDetAutorizaciones.getCodigoPK();
		String loginUsuMod = dtoDetAutorizaciones.getUsuarioModifica().getLoginUsuario();
		int cantidadInsert = obtenerCantidadAutorizadaDetAuto(con, Utilidades.convertirAEntero(dtoDetAutorizaciones.getCodigoPK()));
		for(int i=0; i < Utilidades.convertirAEntero(dtoDetAutorizaciones.getRespuestaDto().getCantidadAutorizada());i++){
			dtoAdmEst = (DtoDetAutorizacionEst) dto.get(i);
			if(!dtoAdmEst.getPreguntar().equals(ConstantesBD.acronimoSi)){
				
				if((!dtoAdmEst.getCodigo().equals(""))&&dtoAdmEst.isRemplazarResp()) // update en det_autorizciones_estancia
					updateActivoDetAutorizacionEst(con, ConstantesBD.acronimoNo, dtoAdmEst.getCodigo());
				
				if((!dtoAdmEst.getDetAutorizacion().equals(""))&&dtoAdmEst.isRemplazarResp())// update en det_autorizaciones
					updateActivoDetAutorizacion(con, ConstantesBD.acronimoNo, loginUsuMod, dtoAdmEst.getDetAutorizacion());
				
				dtoAdmEst.setDetAutorizacion(codigoPK);
				dtoAdmEst.setActivo(ConstantesBD.acronimoSi);
				if(insertarDetAutorizacionEstancia(con, dtoAdmEst) > 0) // hacer el update de la cantidad
					cantidadInsert-=1;
				else
					logger.info("No se  Pudo Generar Autorizacion de la Solicitud >>>> "+dtoAdmEst.getNumeroSolicitud());
			}else // preguntar si desea remplazar la respuesta
				dtoAdmEst.setPreguntar(ConstantesBD.acronimoNo);
				dtopreguntar.add(dtoAdmEst);
		}
		updateCantidadAutoDetAutorizacion(con, cantidadInsert, loginUsuMod, codigoPK);
		return dtopreguntar;
	}
	
	//********************************************************************************************
	
	/**
	 * Modificar el campo Activo de la det_autorizaciones
	 */
	public static int updateActivoDetAutorizacion(Connection con, String activo, String login, String codigo){
		HashMap parametros = new HashMap();
		parametros.put("activo", activo);
		parametros.put("usuario_modifica", login);
		parametros.put("codigo", codigo);
		return getAutorizacionesDao().updateActivoDetAutorizacion(con, parametros);
	}
	
	//********************************************************************************************
	
	/**
	 * Cambiar el campo Activo del det_autorizaciones_estancia
	 * @param con
	 * @param HashMap parametros
	 * @return int
	 */
	public static int updateActivoDetAutorizacionEst(Connection con, String activo, String cod_det_auto_est){
		HashMap parametros = new HashMap();
		parametros.put("activo", activo);
		parametros.put("cod_det_auto_est", cod_det_auto_est);
		return getAutorizacionesDao().updateActivoDetAutorizacionEst(con, parametros);
	}
	
	/**
	 * Insertar Solicitud de Autorizaciones de Nivel ADM/EST y de Forma Automatica
	 * @param con
	 * @param ArrayList<DtoDetAutorizacionEst>
	 * @param cantida
	 * @param Usuario
	 * @return ActionErrors
	 */
	public static ActionErrors insertarSolAutorizcionAdmEstAutomatica(Connection con, ArrayList<DtoDetAutorizacionEst> dto, UsuarioBasico usuario)
	{
		ActionErrors errores = new ActionErrors();		
		DtoDetAutorizacionEst dtoAdmEst = new DtoDetAutorizacionEst();
		String loginUsuMod = usuario.getLoginUsuario();
		int cantidadInsert = 0;
		for(int i = 0; i< dto.size() ;i++)
		{
			dtoAdmEst = (DtoDetAutorizacionEst) dto.get(i);
			String codigo_det_auto = dtoAdmEst.getDetAutorizacion();
			if((!dtoAdmEst.getDetAutorizacion().equals(""))&&(dtoAdmEst.getCodigo().equals("")))// Insertar
			{
				cantidadInsert=obtenerCantidadAutorizadaDetAuto(con, Utilidades.convertirAEntero(codigo_det_auto));
				if(cantidadInsert>0)
				{
					dtoAdmEst.setActivo(ConstantesBD.acronimoSi);
					if(insertarDetAutorizacionEstancia(con, dtoAdmEst) > 0)// hacer el update de la cantidad
					{ 
						cantidadInsert-=1;
						updateCantidadAutoDetAutorizacion(con, cantidadInsert, loginUsuMod, codigo_det_auto);
					}else{
						errores.add("descripcion",new ActionMessage("errors.warnings","No se Pudo Generar Autorizci�n del la Solicitud ["+dtoAdmEst.getNumeroSolicitud()+"]"));
						logger.info("No se  Pudo Generar Autorizacion de la Solicitud >>>> "+dtoAdmEst.getNumeroSolicitud());
					}
				}
			}
		}
		return errores;
	}
	
	/**
	 * Consulta la Cantidad que falta por Autorizar
	 * @param HashMap parametros
	 * @return int
	 */
	public static int obtenerCantidadAutorizadaDetAuto(Connection con, int cod_det_auto){
		HashMap parametros = new HashMap();
		parametros.put("cod_det_auto", cod_det_auto);
		return SqlBaseAutorizacionesDao.obtenerCantidadAutorizadaDetAuto(con, parametros);
	}
	
	
	
	//********************************************************************************************
	
	/**
	 * transpasa la informacion del dto
	 * @param DtoAutorizacion dtoOriginal
	 * */
	public static DtoAutorizacion cargarDtoDatosOtroDto(DtoAutorizacion dtoOriginal)
	{
		DtoAutorizacion dto = new DtoAutorizacion();
		
		dto.setCodigoPK(dtoOriginal.getCodigoPK());
		dto.setIdCuenta(dtoOriginal.getIdCuenta());
		dto.setIdSubCuenta(dtoOriginal.getIdSubCuenta());
		dto.setEstado(dtoOriginal.getEstado());
		
		
		return dto;
	}	
	//********************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param boolean activo
	 * @param String usuario_modifica
	 * @param String codigoPkDetAut
	 * @return int
	 * @ 
	 */
	public static int updateActivoDetAutorizacion(Connection con,boolean activo,String usuario_modifica,String codigoPkDetAut)
	{
		HashMap parametros = new HashMap();
		parametros.put("activo", activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("usuario_modifica", usuario_modifica);
		parametros.put("codigo", codigoPkDetAut);		
		return getAutorizacionesDao().updateActivoDetAutorizacion(con, parametros);
	}
	
	//********************************************************************************************	

	/**
	 * M�todo para asociar una cuenta a autorizaciones sin cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int asociarCuentaAAutorizacionesSinCuenta(Connection con,int codigoCuenta,int codigoIngreso)
	{
		HashMap<String, Object> campos = new HashMap<String, Object>();
		campos.put("codigoCuenta",codigoCuenta);
		campos.put("codigoIngreso",codigoIngreso);
		return getAutorizacionesDao().asociarCuentaAAutorizacionesSinCuenta(con, campos);
		
	}
	
	//********************************************************************************************
	
	/**
	 * Actualiza el estado de la solicitud anulado
	 * */
	public static int updateAnulacionDetAutorizacion(
			Connection con,
			String estado,
			String codigoPkDetAutorizacion,
			String loginUsuario,
			String fechaAnulacion,
			String horaAnulacion,
			String motivoAnulacion)			
	{
		HashMap parametros = new HashMap();
		parametros.put("estado",estado);
		parametros.put("horaAnulacion",horaAnulacion);
		parametros.put("fechaAnulacion",fechaAnulacion);
		parametros.put("usuarioAnulacion",loginUsuario);
		parametros.put("motivoAnulacion",motivoAnulacion);
		parametros.put("codigoPkDetalle",codigoPkDetAutorizacion);		
		
		return getAutorizacionesDao().updateAnulacionDetAutorizacion(con, parametros);
	}
	
	//***************************************************************************************************
	
	/**
	 * Actualiza el numero de autorizacion en detalle cargo
	 * @param Connection con
	 * @param String numeroAutorizacion
	 * @param String codigoDetalleCargo
	 * */
	public static int actualizarAutorizacionServicioArticulo(Connection con,String numeroAutorizacion,String codigoDetalleCargo)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroAutorizacion",numeroAutorizacion);
		parametros.put("codigoDetalleCargo",codigoDetalleCargo);
		
		return getAutorizacionesDao().actualizarAutorizacionServicioArticulo(con,parametros);
	}
	
	//*********************************************************************************************************
	
	/**
	 * Modificar la cobertura en salud de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public static int updateCoberturaSaludSubCuenta(Connection con,String codigoTipoCobertura,String codigoSubCuenta)
	{
		HashMap parametros = new HashMap();
		parametros.put("tipoCobertura", codigoTipoCobertura);
		parametros.put("subCuenta", codigoSubCuenta);
		
		return getAutorizacionesDao().updateCoberturaSaludSubCuenta(con, parametros);
	}
	
	//*********************************************************************************************************
	
	
	/**
	 * verifica si una orden medica se encuentra habilitada para realizar el proceso
	 * @param String tipoOrdenMedica
	 * @param String TipoOrdenInd
	 * @param String estado 
	 * */
	public static boolean esOrdenActiva(String TipoOrdenInd,String tipoOrdenMedica,String tipoServicio,String estado)
	{
		boolean resultado = false;
		
		//solicitude servicios/articulos
		if(TipoOrdenInd.equals(codInternoAutoSolicSerMed))
		{
			if(tipoServicio.equals(ConstantesBD.codigoServicioCamaEstancias+""))
				resultado = true;
			else if(tipoOrdenMedica.equals(ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+"") || 
				tipoOrdenMedica.equals(ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+""))			
				resultado = true;
			else if(tipoOrdenMedica.equals(ConstantesBD.codigoTipoSolicitudMedicamentos+"") && 
					!estado.equals(ConstantesBD.codigoEstadoHCAdministrada+""))
				resultado = true;
			else if(!estado.equals(ConstantesBD.codigoEstadoHCRespondida+"") && 
						!estado.equals(ConstantesBD.codigoEstadoHCInterpretada+""))
				resultado = true;						
		}
		else if(TipoOrdenInd.equals(codInternoAutoSolicAmbula))
		{
			if(!estado.equals(ConstantesBD.codigoEstadoOrdenAmbulatoriaRespondida+""))
				resultado = true;
		}
		
		logger.info("\n");
		logger.info("..:EsOrdenActiva >> tipoOrden >> "+TipoOrdenInd+" TipoOrdenMedica >> "+tipoOrdenMedica+" Tipo Servicio >> "+tipoServicio+" estado >> "+estado+" esOrdenActiva >> "+resultado);
		
		return resultado;		
	}

		
	//*********************************************************************************************************
	
	/**
	 * asocia las estancias a un detalle de autorizaciones
	 * @param Connection con
	 * @param ArrayList<DtoDetAutorizacionEst> lista
	 * @param String codigoDetAutori
	 * */
	public static ArrayList<DtoDetAutorizacionEst> cargarListadoEstanciasPorPreguntar(
			Connection con,
			ArrayList<DtoDetAutorizacionEst> lista,
			String codigoDetAutori,
			UsuarioBasico usuario)
	{
		String previoDetAuto = "0";
		ArrayList<DtoDetAutorizacionEst> dtopreguntar = new ArrayList<DtoDetAutorizacionEst>();		
		int cantidadInsert = obtenerCantidadAutorizadaDetAuto(con, Utilidades.convertirAEntero(codigoDetAutori));
		int cantidadInsertOld = cantidadInsert;
		
		logger.info("..:Proceso de estancia. Cantidad de registros por evaluar "+lista.size()+". cantidad a llenar "+cantidadInsert);
		
		for(DtoDetAutorizacionEst dto : lista)
		{
			if(!dto.getPreguntar().equals(ConstantesBD.acronimoSi))
			{
				dto.setDetAutorizacion(codigoDetAutori);
				dto.setActivo(ConstantesBD.acronimoSi);
				if(insertarDetAutorizacionEstancia(con,dto)>0)
					cantidadInsert-=1;
				else
					logger.info("..:Error insertando estancia en la autorizacion");					
				
				if(cantidadInsert <= 0)
				{
					updateCantidadAutoDetAutorizacion(con, cantidadInsert,usuario.getLoginUsuario(),codigoDetAutori);
					logger.info("..:se completo las estancias solicitadas ");
					return new ArrayList<DtoDetAutorizacionEst>();
				}
				
			}
			else if(dto.getPreguntar().equals(ConstantesBD.acronimoSi))			
				dtopreguntar.add(dto);		
		}
		
		if(cantidadInsert!=cantidadInsertOld)
			updateCantidadAutoDetAutorizacion(con,cantidadInsert,usuario.getLoginUsuario(),codigoDetAutori);
		
		return dtopreguntar;
	}
	
	//*********************************************************************************************************
	
	/**
	 * asocia las estancias a un detalle de autorizaciones
	 * @param Connection con
	 * @param ArrayList<DtoDetAutorizacionEst> lista
	 * @param String codigoDetAutori
	 * */
	public static boolean insertarEstanciasPorPreguntar(
			Connection con,
			ArrayList<DtoDetAutorizacionEst> lista,
			String codigoDetAutori,
			UsuarioBasico usuario)
	{
		String previoDetAuto = "0";
		ArrayList<DtoDetAutorizacionEst> dtopreguntar = new ArrayList<DtoDetAutorizacionEst>();		
		int cantidadInsert = obtenerCantidadAutorizadaDetAuto(con, Utilidades.convertirAEntero(codigoDetAutori));
		int cantidadInsertOld = cantidadInsert;
		
		for(DtoDetAutorizacionEst dto : lista)
		{
			if(dto.getPreguntar().equals(ConstantesBD.acronimoSi) 
					&& dto.isRemplazarResp())
			{
				if(Utilidades.convertirAEntero(dto.getCodigo())>0)
				{
					if(updateActivoDetAutorizacionEst(con,ConstantesBD.acronimoNo,dto.getCodigo())<=0)
						return false;
				}
				
				if(Utilidades.convertirAEntero(dto.getDetAutorizacion())>0)
				{
					if(updateActivoDetAutorizacion(con,ConstantesBD.acronimoNo,usuario.getLoginUsuario(),dto.getDetAutorizacion())<=0)
						return false;
				}

				dto.setDetAutorizacion(codigoDetAutori);
				dto.setActivo(ConstantesBD.acronimoSi);

				if(insertarDetAutorizacionEstancia(con,dto)>0)
				{
					cantidadInsert-=1;
				}
				else
					logger.info("..:Error insertando estancia en la autorizaci�n");

				if(cantidadInsert <= 0)
				{
					updateCantidadAutoDetAutorizacion(con, cantidadInsert,usuario.getLoginUsuario(),codigoDetAutori);
					logger.info("..:se completo las estancias solicitadas ");
					return true;
				}				
			}					
		}
		
		if(cantidadInsert!=cantidadInsertOld)
			updateCantidadAutoDetAutorizacion(con,cantidadInsert,usuario.getLoginUsuario(),codigoDetAutori);
		
		return true;
	}
	
	//*********************************************************************************************************
	
	/**
	 * Consulta Informe Tecnico de Solicitd de Autorizacion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static DtoAutorizacion caragarInformeTecnico(Connection con, int codigo_autorizacion, String codigoInstitucion){
		HashMap parametros = new HashMap();
		parametros.put("autorizacion", codigo_autorizacion);
		parametros.put("codigoInstitucion", codigoInstitucion);
		return getAutorizacionesDao().caragarInformeTecnico(con, parametros);
	}
	
	//*********************************************************************************************************
	
	/**
	 * @param int codigoViaIngreso
	 * @param boolean esAsocioPendiente
	 * @param String codigoTipoPaciente
	 * @param boolean fueAsociada
	 * */
	public static String getIndicadorCuenta(String codigoViaIngreso,String codigoTipoPaciente,boolean esAsocioPendiente,boolean fueAsociada)
	{
		String resultado = "";
		String temp = "";

		
		if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoAmbulatorios+""))		
			resultado = "AMB";

		if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoConsultaExterna+""))					
			resultado = "CE";

		if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
			resultado = "URG";
		
		if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		{			
			resultado = "HOSP";
			
			if(codigoTipoPaciente.equals(ConstantesBD.tipoPacienteHospitalizado))					
				temp = "HOSP";
			else if(codigoTipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
				temp = "CX AMB";
			
			if(esAsocioPendiente)
				resultado ="HOSP "+temp+" /HOSP";
			
			if(fueAsociada)
			{
				if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
					resultado = "HOSP /HOSP "+temp;
				
				if(codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+""))
					resultado =  "URG /HOSP "+temp;				
			}			
		}
		
		return resultado;
	}
	
	//*********************************************************************************************************
	
	/**
	 * Consulta listado de cuentas
	 * @param Connection con
	 * @param String codigoSubcuenta
	 * @param String codigoIdIngreso 
	 */
	public static ArrayList<DtoCuentaAutorizacion> cargarListadoCuentas(Connection con,String codigoSubcuenta,String codigoIdIngreso) 
	{
		HashMap parametros = new HashMap();
		boolean cuentaHospADM = false, cuentaHospEST = false, existeCuentaHosp = false;
		String temp = "";
		parametros.put("codigoSubcuenta", codigoSubcuenta);
		parametros.put("codigoIdIngreso", codigoIdIngreso);
		
		ArrayList<DtoCuentaAutorizacion> lista = new ArrayList<DtoCuentaAutorizacion>();
		lista = getAutorizacionesDao().cargarListadoCuentas(con, parametros);

		for(int i = 0 ; i<lista.size(); i++)
		{
			DtoCuentaAutorizacion dto = lista.get(i);
			 
			//color del estado
			if(dto.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
				lista.get(i).setColorEstado("#ffe28a");
			
			if(dto.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
				lista.get(i).setColorEstado("#ffffcb");
			
			if(dto.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
				lista.get(i).setColorEstado("#c2ffbd");
			
			if(dto.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoNegado))
				lista.get(i).setColorEstado("#ffd8d8");
			
			if(dto.isEsBD())
			{
				if(dto.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoAmbulatorios+""))
				{
					dto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAdmision);
					dto.setIndViaIngCuenta("AMB");
				}
		
				if(dto.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoConsultaExterna+""))
				{
					dto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAdmision);
					dto.setIndViaIngCuenta("CE");
				}
	
				if(dto.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
				{
					dto.setIndViaIngCuenta("URG");
					dto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAdmision);
					
					if(dto.isEsAsocioPendiente())
					{
						DtoCuentaAutorizacion dtoNueva = new DtoCuentaAutorizacion();
						dtoNueva.setIdCuenta(dto.getIdCuenta());						
						dtoNueva.setIdIngreso(dto.getIdIngreso());	
						dtoNueva.setCodigoSubCuenta(dto.getCodigoSubCuenta());
						dtoNueva.setIndViaIngCuenta("URG/HOSP");
						dtoNueva.setCodigoConvenio(dto.getCodigoConvenio());
						dtoNueva.setNombreConvenio(dto.getNombreConvenio());				
						dtoNueva.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAdmision);
						dtoNueva.setEsBD(false);
						lista.add(dtoNueva);
						
						dtoNueva = new DtoCuentaAutorizacion();
						dtoNueva.setIdCuenta(dto.getIdCuenta());						
						dtoNueva.setIdIngreso(dto.getIdIngreso());	
						dtoNueva.setCodigoSubCuenta(dto.getCodigoSubCuenta());
						dtoNueva.setIndViaIngCuenta("URG/HOSP");
						dtoNueva.setCodigoConvenio(dto.getCodigoConvenio());
						dtoNueva.setNombreConvenio(dto.getNombreConvenio());
						dtoNueva.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoEstancia);
						dtoNueva.setEsBD(false);
						lista.add(dtoNueva);
					}
				}
				
				if(dto.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
				{
					cuentaHospADM = false;
					cuentaHospEST = false;
					existeCuentaHosp = true;
					temp = "";
					
					dto.setIndViaIngCuenta("HOSP");
					if(dto.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))					
						temp = "HOSP";
					else if(dto.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
						temp = "CX AMB";
					
					if(dto.isEsAsocioPendiente())
						dto.setIndViaIngCuenta("HOSP "+temp+" /HOSP");
					
					if(dto.isFueAsociada())
					{
						if(dto.getViaIngCuentaAsocio().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
							dto.setIndViaIngCuenta("HOSP /HOSP "+temp);
						
						if(dto.getViaIngCuentaAsocio().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
							dto.setIndViaIngCuenta("URG /HOSP "+temp);				
					}
					
					//busca si se encuentra los registros para la estancia y admision
					for(DtoCuentaAutorizacion dtoHos : lista)
					{
						if(dto.getIdCuenta().equals(dtoHos.getIdCuenta()))
						{							
							if(dtoHos.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAdmision))
								cuentaHospADM = true;
							
							if(dtoHos.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstancia))
								cuentaHospEST = true;				
						}
					}
					
					if(!cuentaHospADM && 
						!cuentaHospEST)
					{
						//toma la cuenta original y la uso
						dto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAdmision);
						cuentaHospADM = true;
					}
	
					if(!cuentaHospADM)
					{
						DtoCuentaAutorizacion dtoNueva = new DtoCuentaAutorizacion();
						dtoNueva.setIdCuenta(dto.getIdCuenta());
						dtoNueva.setCodigoTipoPaciente(dto.getCodigoTipoPaciente());
						dtoNueva.setIdIngreso(dto.getIdIngreso());					
						dtoNueva.setCodigoSubCuenta(dto.getCodigoSubCuenta());
						dtoNueva.setCodigoViaIngreso(dto.getCodigoViaIngreso());
						dtoNueva.setCodigoConvenio(dto.getCodigoConvenio());
						dtoNueva.setNombreConvenio(dto.getNombreConvenio());				
						dtoNueva.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAdmision);
						dtoNueva.setIndViaIngCuenta(dto.getIndViaIngCuenta());
						dtoNueva.setEsBD(false);
						lista.add(dtoNueva);	
					}
					
					if(!cuentaHospEST)
					{
						DtoCuentaAutorizacion dtoNueva = new DtoCuentaAutorizacion();
						dtoNueva.setIdCuenta(dto.getIdCuenta());
						dtoNueva.setCodigoTipoPaciente(dto.getCodigoTipoPaciente());
						dtoNueva.setIdIngreso(dto.getIdIngreso());
						dtoNueva.setCodigoSubCuenta(dto.getCodigoSubCuenta());
						dtoNueva.setCodigoViaIngreso(dto.getCodigoViaIngreso());
						dtoNueva.setCodigoConvenio(dto.getCodigoConvenio());
						dtoNueva.setNombreConvenio(dto.getNombreConvenio());
						dtoNueva.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoEstancia);
						dtoNueva.setIndViaIngCuenta(dto.getIndViaIngCuenta());
						dtoNueva.setEsBD(false);
						lista.add(dtoNueva);
					}
				}
			}
		}
		
		return lista;
	}

	//*********************************************************************************************************
	/**
	 * @param String estado
	 * */
	public static String getColorEstado(String estado)
	{
		//color del estado
		if(estado.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			return codColorAnulado;
		
		if(estado.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
			return codColorSolicitado;
		
		if(estado.equals(ConstantesIntegridadDominio.acronimoAutorizado))
			return codColorAutorizado;
		
		if(estado.equals(ConstantesIntegridadDominio.acronimoEstadoNegado))
			return codColorNegado;
		
		return "";
	}
	
	//*********************************************************************************************************
	
	/**
	 * Consultar listado de solicitudes enviadas
	 * @param Connection con
	 * @param String cuenta
	 * @param String tipo
	 */
	public static ArrayList<DtoAutorizacion> cargarListadoAutorizaciones(Connection con,String cuenta,String tipo)
	{
		HashMap parametros = new HashMap();
		parametros.put("cuenta", cuenta);
		parametros.put("tipo", tipo);
		
		return getAutorizacionesDao().cargarListadoAutorizaciones(con, parametros);
	}
	
	//*********************************************************************************************************
	

	public HashMap getMapaAux() {
		return mapaAux;
	}

	public void setMapaAux(HashMap mapaAux) {
		this.mapaAux = mapaAux;
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
}