package com.servinte.axioma.bl.capitacion.impl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;

import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo;
import com.servinte.axioma.bl.facturacion.impl.CoberturaMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.AutorizacionCapitacionMundo;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.delegate.administracion.CatalogoAdministracionDelegate;
import com.servinte.axioma.delegate.capitacion.NivelAutorizacionDelegate;
import com.servinte.axioma.delegate.facturacion.CatalogoFacturacionDelegate;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.ContratosEntidadesSub;
import com.servinte.axioma.orm.EntidadesSubcontratadas;

/**
 * Clase que implementa los servicios de Negocio correspondientes a la lógica asociada a los
 * Niveles de Autorización
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:02 p.m.
 */
public class NivelAutorizacionMundo implements INivelAutorizacionMundo{

	//private static final String MENSAJE_GENERAL="errors.autorizacion.messageGeneral"; 
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo#consultarNivelesAutorizacionUsuario(java.lang.String, int, java.lang.String, boolean)
	 * @author ricruico
	 */
	@Override
	public List<NivelAutorizacionDto> consultarNivelesAutorizacionUsuario(
			String loginUsuario, int codigoPersonaUsuario, String tipoAutorizacion, boolean isActivo, boolean requiereTransaccion)  throws IPSException{
		List<NivelAutorizacionDto> nivelesAutorizacionUsuario=null;
		List<NivelAutorizacionDto> nivelesAutorizacionUsuarioXOcupacion = null;
		try{
			if(requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			NivelAutorizacionDelegate delegate = new NivelAutorizacionDelegate();
			//Se consultan los niveles de autorizacion por el usuario específico
			nivelesAutorizacionUsuario=delegate.consultarNivelesAutorizacionUsuario(loginUsuario, tipoAutorizacion, isActivo);
			if(!nivelesAutorizacionUsuario.isEmpty()){
				for(NivelAutorizacionDto nivelAutorizacion:nivelesAutorizacionUsuario){
					//Para cada nivel se consultan las prioridades
					nivelAutorizacion.setPrioridades(delegate.obtenerPrioridadesNivelAutorizacionUsuario(loginUsuario, nivelAutorizacion.getCodigo()));
				}
				
				//MT5953 hermorhu-validar ocupacion medica 
				nivelesAutorizacionUsuarioXOcupacion = delegate.consultarNivelesAutorizacionOcupacionUsuario(codigoPersonaUsuario, tipoAutorizacion, isActivo);
				if(!nivelesAutorizacionUsuarioXOcupacion.isEmpty()){
					for(NivelAutorizacionDto nivelAutorizacion:nivelesAutorizacionUsuarioXOcupacion){
						//Para cada nivel se consultan las prioridades
						nivelAutorizacion.setPrioridades(delegate.obtenerPrioridadesNivelAutorizacionOcupacionUsuario(codigoPersonaUsuario, nivelAutorizacion.getCodigo()));
					}
				}
				
				nivelesAutorizacionUsuario.addAll(nivelesAutorizacionUsuarioXOcupacion);
			}
			else{
				//Se consultan los niveles de autorización por la ocuapacion médica del usuario
				nivelesAutorizacionUsuario=delegate.consultarNivelesAutorizacionOcupacionUsuario(codigoPersonaUsuario, tipoAutorizacion, isActivo);
				if(!nivelesAutorizacionUsuario.isEmpty()){
					for(NivelAutorizacionDto nivelAutorizacion:nivelesAutorizacionUsuario){
						//Para cada nivel se consultan las prioridades
						nivelAutorizacion.setPrioridades(delegate.obtenerPrioridadesNivelAutorizacionOcupacionUsuario(codigoPersonaUsuario, nivelAutorizacion.getCodigo()));
					}
				}
			}
			if(requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}
		catch (IPSException ipsme) {
			if(requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}	
			throw ipsme;
		}
		catch (Exception e) {
			if(requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}	
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return nivelesAutorizacionUsuario;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo#existeNivelAutorizacionMedicamentoInsumo(java.util.List, java.util.List)
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean existeNivelAutorizacionMedicamentoInsumo(
			List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosOrden,
			List<Integer> nivelesAutorizacionUsuario) throws IPSException {
		boolean existeParametrizacion=false;
		try{
			HibernateUtil.beginTransaction();
			NivelAutorizacionDelegate nivelAutorizacionDelegate= new NivelAutorizacionDelegate();
			List<Integer> codigosMedicamentosInsumos = new ArrayList<Integer>();
			//Se obtienen los codigosPk de los Medicamentos insumos para realizar las validaciones de 
			//niveles de autorización por Medicamento/Insumo específico
			for(MedicamentoInsumoAutorizacionOrdenDto dto:medicamentosInsumosOrden){
				codigosMedicamentosInsumos.add(dto.getCodigo());
			}
			List<Integer> nivelesMedicamentosInsumos=nivelAutorizacionDelegate.existeNivelAutorizacionMedicamentosInsumos(codigosMedicamentosInsumos);
			if(nivelesMedicamentosInsumos != null && !nivelesMedicamentosInsumos.isEmpty()){
				List<Integer> intersec=ListUtils.intersection(nivelesMedicamentosInsumos, nivelesAutorizacionUsuario);
				if(intersec != null && !intersec.isEmpty()){
					existeParametrizacion=true;
				}				
			}
			if(!existeParametrizacion){
				//Se valida uno a uno los medicamentos para verificar si existe parametrización de niveles de autorización
				//agrupado por lo menos para un medicamento/insumo
				for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamentoInsumo:medicamentosInsumosOrden){
					List<Integer> nivelesMedicamentoAgrupado=nivelAutorizacionDelegate.obtenerNivelAutorizacionMedicamentoInsumoAgrupado(
																dtoMedicamentoInsumo.getSubGrupoInventario(), 
																dtoMedicamentoInsumo.getAcronimoNaturaleza());
					if(nivelesMedicamentoAgrupado != null && !nivelesMedicamentoAgrupado.isEmpty()){	
						List<Integer> intersec=ListUtils.intersection(nivelesMedicamentoAgrupado, nivelesAutorizacionUsuario);
						if(intersec != null && !intersec.isEmpty()){
							existeParametrizacion=true;
							break;
						}
					}
					
				}
			}
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return existeParametrizacion;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo#existeNivelAutorizacionServicio(java.util.List, java.util.List)
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean existeNivelAutorizacionServicio(List<ServicioAutorizacionOrdenDto> serviciosOrden,
			List<Integer> nivelesAutorizacionUsuario) throws IPSException {
		boolean existeParametrizacion=false;
		try{
			HibernateUtil.beginTransaction();
			NivelAutorizacionDelegate nivelAutorizacionDelegate= new NivelAutorizacionDelegate();
			List<Integer> codigosServicios = new ArrayList<Integer>();
			//Se obtienen los codigosPk de los servicios para realizar las validaciones de 
			//niveles de autorización por Servicio específico
			for(ServicioAutorizacionOrdenDto dto:serviciosOrden){
				codigosServicios.add(dto.getCodigo());
			}
			List<Integer> nivelesServicios=nivelAutorizacionDelegate.existeNivelAutorizacionServicios(codigosServicios);
			if(nivelesServicios != null && !nivelesServicios.isEmpty()){
				List<Integer> intersec=ListUtils.intersection(nivelesServicios, nivelesAutorizacionUsuario);
				if(intersec != null && !intersec.isEmpty()){
					existeParametrizacion=true;
				}				
			}
			if(!existeParametrizacion){
				//Se valida uno a uno los servicios para verificar si existe parametrización de niveles de autorización
				//agrupado por lo menos para un servicio
				for(ServicioAutorizacionOrdenDto dtoServicio:serviciosOrden){
					List<Integer> nivelesServicioAgrupado=nivelAutorizacionDelegate.obtenerNivelAutorizacionServicioAgrupado(
																dtoServicio.getCodigoEspecialidad(), 
																dtoServicio.getAcronimoTipoServicio(),
																dtoServicio.getCodigoGrupoServicio());
					if(nivelesServicioAgrupado != null && !nivelesServicioAgrupado.isEmpty()){	
						List<Integer> intersec=ListUtils.intersection(nivelesServicioAgrupado, nivelesAutorizacionUsuario);
						if(intersec != null && !intersec.isEmpty()){
							existeParametrizacion=true;
							break;
						}
					}
					
				}
			}
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return existeParametrizacion;
	}


	/**
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo#validarNivelAutorizacionParaAutorizacionAutomatica(java.util.List)
	 */
	@Override
	public List<AutorizacionCapitacionDto> validarNivelAutorizacionParaAutorizacionAutomatica(
			AutorizacionCapitacionDto autorizacionCapitacionDto, boolean requiereTransaccion)
			throws IPSException {
		
		CatalogoAdministracionDelegate catalogoAdministracionDelegate = null;
		CatalogoFacturacionDelegate catalogoFacturacionDelegate = null;
		List<AutorizacionCapitacionDto> listaAutorizacionesAgrupadas = null;
		List<AutorizacionCapitacionDto> listaAutorizacionesValidadas = null;
		List<NivelAutorizacionDto> listaNivelesAutorizacionUsuario = new ArrayList<NivelAutorizacionDto>();
		List<NivelAutorizacionDto> listaNivelesAutorizacionAutomatica = new ArrayList<NivelAutorizacionDto>();
		List<EntidadSubContratadaDto> listaEntidadesSubContratadasExt = new ArrayList<EntidadSubContratadaDto>();
		List<Integer> codigosNivelesAutorizacion = null;
		Map<Long, AutorizacionCapitacionDto> mapaOrdenesXEntidadSubContratada = null;
		EntidadesSubcontratadas entidadSubContratada = null;
		CoberturaMundo coberturaMundo = null;
		AutorizacionCapitacionMundo autorizacionCapitacionMundo = null;
		NivelAutorizacionDto nivelAutorizacionServicioMedicamento = null;
		Date fecha = new Date();
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		boolean existePrioridadServicioMedicamento = false;
		
		try {
			if(requiereTransaccion){
				HibernateUtil.beginTransaction();
			}
			catalogoAdministracionDelegate = new CatalogoAdministracionDelegate();
			catalogoFacturacionDelegate = new CatalogoFacturacionDelegate();
			coberturaMundo = new CoberturaMundo();
			autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
			
			/*Agrupacion de ordenes*/
			listaAutorizacionesAgrupadas = this.agruparOrdenesAutorizacion(autorizacionCapitacionDto);
			
			//Lista de autorizaciones que seran devueltas para su autorizacion
			listaAutorizacionesValidadas = new ArrayList<AutorizacionCapitacionDto>();
			
			//1. NIVELES DE AUTORIZACION POR USUARIO 
			//1.1 NIVEL DE AUTORIZACION DEL USUARIO ESPECIFICO
			//1.2 NIVEL DE AUTORIZACION POR OCUPACION MEDICA
			listaNivelesAutorizacionUsuario = this.consultarNivelesAutorizacionUsuario(
					autorizacionCapitacionDto.getLoginUsuario(), 
					autorizacionCapitacionDto.getCodigoPersonaUsuario(),
					ConstantesIntegridadDominio.acronimoTipoAutorizacionAuto, true, false);
			
			if (!listaNivelesAutorizacionUsuario.isEmpty()) {

				for (AutorizacionCapitacionDto autorizacionDto : listaAutorizacionesAgrupadas) {

					mapaOrdenesXEntidadSubContratada = 
							new HashMap<Long, AutorizacionCapitacionDto>();
					
					listaNivelesAutorizacionAutomatica = new ArrayList<NivelAutorizacionDto>();

					for (OrdenAutorizacionDto ordenAutorizacionDto : autorizacionDto.getOrdenesAutorizar()) {

						/*2.SELECCION DE NIVELES DE AUTORIZACION AUTOMATICAS
						en la consultarNivelesAutorizacionUsuario se realiza la validación por tipo autorización
						y estado activo por lo cual en este punto solo se valida la vía de ingreso */
						for (NivelAutorizacionDto nivelAutorizacionDto : listaNivelesAutorizacionUsuario) {
							/*Si se cumple la validación de la vía de ingreso se realiza el punto
							 * 3. Niveles de Autorización para Servicios, Medicamentos o Insumos a Autorizar*/
							if(ordenAutorizacionDto.getCodigoViaIngreso() == 
									nivelAutorizacionDto.getViaIngreso().getCodigo() && 
									!listaNivelesAutorizacionAutomatica.contains(nivelAutorizacionDto)) {
								listaNivelesAutorizacionAutomatica.add(nivelAutorizacionDto);
							}
						}
					}

					if (!listaNivelesAutorizacionAutomatica.isEmpty()) {

						codigosNivelesAutorizacion = verificarNivelesAutorizacionOrdenes(
								autorizacionDto.getOrdenesAutorizar(), 
								listaNivelesAutorizacionAutomatica, false, autorizacionDto);

						if (codigosNivelesAutorizacion != null && !codigosNivelesAutorizacion.isEmpty()) {

							for (OrdenAutorizacionDto ordenAutorizacionDto : autorizacionDto.getOrdenesAutorizar()) {
								//Para solicitudes de Cirugía y Peticiones, todos los servicios de la 
								// solicitud/petición deben cumplir con las validaciones de los niveles 
								// para continuar con el ítem 4. Buscar y Validar Entidad Subcontratada. 
								// De lo contrario se cancela el proceso de autorización*/
								if (((ordenAutorizacionDto.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia) ||
										(ordenAutorizacionDto.getClaseOrden() == ConstantesBD.claseOrdenPeticion && 
												ordenAutorizacionDto.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)) && 
										!ordenAutorizacionDto.isPuedeAutorizar()) {
									break;
								}

								/*4. Buscar y Validar Entidad Subcontratada*/
								if (ordenAutorizacionDto.getTipoOrden() != ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos) {

									/*Se busca el centro de costo que ejecuta*/
									CentrosCosto centroCosto = catalogoAdministracionDelegate.buscarCentroCostoXId(
											ordenAutorizacionDto.getCodigoCentroCostoEjecuta());

									ordenAutorizacionDto.setTipoEntidadEjecuta(centroCosto.getTipoEntidadEjecuta());

									/*Se evalúa lo definido en el campo 'Tipo de Entidad que ejecuta' en la 
									 * parametrización de Centros de Costo para el Centro de Costo que ejecuta
									 * 
									 * Si está definido como 'Interno' se debe leer lo definido 
									 * en el parámetro 'Entidad Subcontratada Para Centros de Costo Internos' 
									 * en la funcionalidad de Parámetros generales para el módulo de Manejo del paciente*/
									if(!UtilidadTexto.isEmpty(centroCosto.getTipoEntidadEjecuta()) && 
											centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)) {

										String descripcionEntidad = 
												ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(
														autorizacionCapitacionDto.getCodigoInstitucion());

										if(!UtilidadTexto.isEmpty(descripcionEntidad)) {

											String[] entidad = descripcionEntidad.split("-");

											try {

												entidadSubContratada = catalogoFacturacionDelegate.
														buscarEntidadSubContratadaXId(Long.parseLong(entidad[0].trim()));

											} catch (NumberFormatException e) {
												autorizacionDto.setProcesoExitoso(false);
												autorizacionDto.setVerificarDetalleError(true);
												ordenAutorizacionDto.setPuedeAutorizar(false);
												break;
											}

											if(UtilidadTexto.getBoolean(entidadSubContratada.getActivo())) {

												ContratosEntidadesSub contrato = 
														catalogoFacturacionDelegate.obtenerContratoVigenteEntidadSubContratada(
																entidadSubContratada.getCodigoPk(), formatoFecha.parse(formatoFecha.format(fecha)));

												//Entidad subcontratada con contrato vigente
												if(contrato != null && contrato.getConsecutivo() > 0) {		

													int prioridad = Utilidades.convertirAEntero(
															ValoresPorDefecto.getPrioridadEntidadSubcontratada(
																	autorizacionCapitacionDto.getCodigoInstitucion()));

													EntidadSubContratadaDto entidadSubAutorizarCapitacion = new EntidadSubContratadaDto();
													entidadSubAutorizarCapitacion.setCodContratoEntidadSub(contrato.getConsecutivo());
													entidadSubAutorizarCapitacion.setCodEntidadSubcontratada(entidadSubContratada.getCodigoPk());
													entidadSubAutorizarCapitacion.setDireccionEntidad(entidadSubContratada.getDireccion());
													entidadSubAutorizarCapitacion.setRazonSocial(entidadSubContratada.getRazonSocial());
													entidadSubAutorizarCapitacion.setTelefonoEntidad(entidadSubContratada.getTelefono());
													entidadSubAutorizarCapitacion.setTipoTarifa(contrato.getTipoTarifa());

													autorizacionDto.setEntidadSubAutorizarCapitacion(entidadSubAutorizarCapitacion);

													if(!ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()) {

														for (ServicioAutorizacionOrdenDto servicio : 
															ordenAutorizacionDto.getServiciosPorAutorizar()) {

															nivelAutorizacionServicioMedicamento = null;

															if (servicio.isAutorizar() &&
																	servicio.getNivelesAutorizacion() != null && 
																	!servicio.getNivelesAutorizacion().isEmpty()) {
																
																for (NivelAutorizacionDto nivelServicio : servicio.getNivelesAutorizacion()) {

																	if(nivelServicio.getPrioridades().contains(prioridad)) {
																		nivelAutorizacionServicioMedicamento = nivelServicio;
																		existePrioridadServicioMedicamento = true;
																		break;
																	}
																}

																if(existePrioridadServicioMedicamento) {
																	InfoCobertura info = coberturaMundo.validacionCoberturaServicioEntidadSub(
																			contrato.getConsecutivo(), ordenAutorizacionDto.getCodigoViaIngreso(), 
																			autorizacionCapitacionDto.getDatosPacienteAutorizar().getTipoPaciente(), 
																			servicio.getCodigo(), 
																			autorizacionCapitacionDto.getDatosPacienteAutorizar().getNaturalezaPaciente(), 
																			autorizacionCapitacionDto.getCodigoInstitucion());

																	if(info.getIncluido()) {
																		servicio.setNivelAutorizacion(nivelAutorizacionServicioMedicamento);
																		servicio.setAutorizar(true);
																		servicio.setPuedeAutorizar(true);
																	} else {
																		servicio.setAutorizar(false);
																		servicio.setAutorizado(false);
																		servicio.setPuedeAutorizar(false);
																		ordenAutorizacionDto.setPuedeAutorizar(false);
																	}

																} else {
																	servicio.setAutorizar(false);
																	servicio.setAutorizado(false);
																	servicio.setPuedeAutorizar(false);
																	ordenAutorizacionDto.setPuedeAutorizar(false);
																}
															} else {
																servicio.setAutorizar(false);
																servicio.setAutorizado(false);
																servicio.setPuedeAutorizar(false);
																ordenAutorizacionDto.setPuedeAutorizar(false);
															}
														}

													} else if (!ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar().isEmpty()) {

														for (MedicamentoInsumoAutorizacionOrdenDto medicamento : 
															ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()) {

															nivelAutorizacionServicioMedicamento = null;

															if (medicamento.isAutorizar() &&
																	medicamento.getNivelesAutorizacion() != null && 
																	!medicamento.getNivelesAutorizacion().isEmpty()) {

																for (NivelAutorizacionDto nivelServicio : medicamento.getNivelesAutorizacion()) {

																	if(nivelServicio.getPrioridades().contains(prioridad)) {
																		nivelAutorizacionServicioMedicamento = nivelServicio;
																		existePrioridadServicioMedicamento = true;
																		break;
																	}
																}

																if(existePrioridadServicioMedicamento) {
																	InfoCobertura info = coberturaMundo.validacionCoberturaArticuloEntidadSub(
																			contrato.getConsecutivo(), ordenAutorizacionDto.getCodigoViaIngreso(), 
																			autorizacionCapitacionDto.getDatosPacienteAutorizar().getTipoPaciente(), 
																			medicamento.getCodigo(), 
																			autorizacionCapitacionDto.getDatosPacienteAutorizar().getNaturalezaPaciente(), 
																			autorizacionCapitacionDto.getCodigoInstitucion());

																	if(info.getIncluido()) {
																		medicamento.setNivelAutorizacion(nivelAutorizacionServicioMedicamento);
																		medicamento.setAutorizar(true);
																		medicamento.setPuedeAutorizar(true);
																	} else {
																		medicamento.setAutorizar(false);
																		medicamento.setAutorizado(false);
																		medicamento.setPuedeAutorizar(false);
																		ordenAutorizacionDto.setPuedeAutorizar(false);
																	}

																} else {
																	medicamento.setAutorizar(false);
																	medicamento.setAutorizado(false);
																	medicamento.setPuedeAutorizar(false);
																	ordenAutorizacionDto.setPuedeAutorizar(false);
																}
															} else {
																medicamento.setAutorizar(false);
																medicamento.setAutorizado(false);
																medicamento.setPuedeAutorizar(false);
																ordenAutorizacionDto.setPuedeAutorizar(false);
															}
														}
													}

												} else {
													autorizacionDto.setProcesoExitoso(false);
													autorizacionDto.setVerificarDetalleError(true);
													ordenAutorizacionDto.setPuedeAutorizar(false);
													Log4JManager.warning("NO SE ENCONTRO CONTRATO VIGENTE PARA LA ENTIDAD SUBCONTRATADA");
												}
											} else {
												autorizacionDto.setProcesoExitoso(false);
												autorizacionDto.setVerificarDetalleError(true);
												ordenAutorizacionDto.setPuedeAutorizar(false);
												Log4JManager.warning("LA ENTIDAD SUBCONTRATADA NO SE ENCUENTRA VIGENTE: " + 
														entidadSubContratada.getCodigo() + " : " + 
														entidadSubContratada.getRazonSocial());
											}

										} else {
											autorizacionDto.setProcesoExitoso(false);
											autorizacionDto.setVerificarDetalleError(true);
											ordenAutorizacionDto.setPuedeAutorizar(false);
											Log4JManager.warning("NO SE ENCUENTRA DEFINIDO EL PARAMETRO 'ENTIDAD SUBCONTRATADA PARA CENTRO DE COSTOS INTERNOS'");
										}

									} else if(!UtilidadTexto.isEmpty(centroCosto.getTipoEntidadEjecuta()) && 
											(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna) || 
													centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos))) {

										listaEntidadesSubContratadasExt = autorizacionCapitacionMundo.obtenerEntidadesSubContratadasExternas(
												ordenAutorizacionDto.getCodigoCentroCostoEjecuta(), listaNivelesAutorizacionAutomatica, false);

										if (listaEntidadesSubContratadasExt != null && !listaEntidadesSubContratadasExt.isEmpty()) {

											if(!ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()) {

												for (ServicioAutorizacionOrdenDto servicio : 
													ordenAutorizacionDto.getServiciosPorAutorizar()) {

													boolean esEntidadValida = false;
													boolean noAutorizaServicio = false;

													for (EntidadSubContratadaDto entidadSubContratadaDto : listaEntidadesSubContratadasExt) {

														if (servicio.isAutorizar() &&
																servicio.getNivelesAutorizacion() != null && 
																!servicio.getNivelesAutorizacion().isEmpty()) {
															
															NivelAutorizacionDto nivelAutorizacionServicio = new NivelAutorizacionDto();
															//Por cada servicio se verifican los niveles de autorizacion
															//y sus respectivas prioridades para validarlo contra la prioridad de la entidad 
															//subcontratada
															for (NivelAutorizacionDto nivelServicio : servicio.getNivelesAutorizacion()) {

																if (nivelServicio.getPrioridades().contains(
																		entidadSubContratadaDto.getNumeroPrioridad())) {
																	esEntidadValida = true;
																	nivelAutorizacionServicio = nivelServicio;
																	break;
																}
															}

															if (esEntidadValida) {

																InfoCobertura info = coberturaMundo.validacionCoberturaServicioEntidadSub(
																		entidadSubContratadaDto.getCodContratoEntidadSub(), 
																		ordenAutorizacionDto.getCodigoViaIngreso(), 
																		autorizacionCapitacionDto.getDatosPacienteAutorizar().getTipoPaciente(), 
																		servicio.getCodigo(), 
																		autorizacionCapitacionDto.getDatosPacienteAutorizar().getNaturalezaPaciente(), 
																		autorizacionCapitacionDto.getCodigoInstitucion());

																if(info.getIncluido()) {
																	servicio.setNivelAutorizacion(nivelAutorizacionServicio);
																	servicio.setAutorizar(true);
																	servicio.setPuedeAutorizar(true);
																} else {
																	servicio.setAutorizar(false);
																	servicio.setAutorizado(false);
																	servicio.setPuedeAutorizar(false);
																	ordenAutorizacionDto.setPuedeAutorizar(false);
																	noAutorizaServicio = true;
																}

															} else {
																servicio.setAutorizar(false);
																servicio.setAutorizado(false);
																servicio.setPuedeAutorizar(false);
																ordenAutorizacionDto.setPuedeAutorizar(false);
																noAutorizaServicio = true;
															}
															
														} else {
															servicio.setAutorizar(false);
															servicio.setAutorizado(false);
															servicio.setPuedeAutorizar(false);
															ordenAutorizacionDto.setPuedeAutorizar(false);
															noAutorizaServicio = true;
														}
														
														if (esEntidadValida || noAutorizaServicio) {

															if (mapaOrdenesXEntidadSubContratada.containsKey(
																	entidadSubContratadaDto.getCodEntidadSubcontratada())) {

																if ((ordenAutorizacionDto.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia) ||
																		(ordenAutorizacionDto.getClaseOrden() == ConstantesBD.claseOrdenPeticion && 
																		ordenAutorizacionDto.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)) {

																	mapaOrdenesXEntidadSubContratada.get(
																			Long.valueOf(entidadSubContratadaDto.getCodEntidadSubcontratada())).
																			getOrdenesAutorizar().get(0).getServiciosPorAutorizar().add(servicio);

																} else {

																	List<ServicioAutorizacionOrdenDto> listaServicios = 
																			new ArrayList<ServicioAutorizacionOrdenDto>();
																	List<MedicamentoInsumoAutorizacionOrdenDto> listaMedicamentos = 
																			new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
																	listaServicios.add(servicio);
																	OrdenAutorizacionDto nuevaOrden = 
																			this.copiarOrdenAutorizacionDto(ordenAutorizacionDto, 
																					listaServicios, listaMedicamentos);

																	mapaOrdenesXEntidadSubContratada.get(
																			Long.valueOf(entidadSubContratadaDto.getCodEntidadSubcontratada())).
																			getOrdenesAutorizar().add(nuevaOrden);
																}

															} else {
																//Se crea una nueva autorizacion si existen diferentes Entidades SubContratadas
																List<OrdenAutorizacionDto> listaNuevasOrdenes = new ArrayList<OrdenAutorizacionDto>();
																List<ServicioAutorizacionOrdenDto> listaServicios = 
																		new ArrayList<ServicioAutorizacionOrdenDto>();
																List<MedicamentoInsumoAutorizacionOrdenDto> listaMedicamentos = 
																		new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
																listaServicios.add(servicio);
																listaNuevasOrdenes.add(this.copiarOrdenAutorizacionDto(ordenAutorizacionDto, 
																		listaServicios, listaMedicamentos));
																AutorizacionCapitacionDto nuevaAutorizacion =
																		this.copiarAutorizacionCapitacionDto(autorizacionDto, listaNuevasOrdenes);
																nuevaAutorizacion.setEntidadSubAutorizarCapitacion(entidadSubContratadaDto);
																mapaOrdenesXEntidadSubContratada.put(
																		Long.valueOf(entidadSubContratadaDto.getCodEntidadSubcontratada()), nuevaAutorizacion);

															}
															break;
														}
													} 
												}

											} else if (!ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar().isEmpty()) {

												for (MedicamentoInsumoAutorizacionOrdenDto medicamento : 
													ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()) {

													boolean esEntidadValida = false;
													boolean noAutorizaMedicamento = false;
													
													for (EntidadSubContratadaDto entidadSubContratadaDto : listaEntidadesSubContratadasExt) {

														if (medicamento.isAutorizar() &&
																medicamento.getNivelesAutorizacion() != null && 
																!medicamento.getNivelesAutorizacion().isEmpty()) {

															NivelAutorizacionDto nivelAutorizacionMedicamento = new NivelAutorizacionDto();
															//Por cada servicio se verifican los niveles de autorizacion
															//y sus respectivas prioridades para validarlo contra la prioridad de la entidad 
															//subcontratada
															for (NivelAutorizacionDto nivelMedicamento : medicamento.getNivelesAutorizacion()) {

																if (nivelMedicamento.getPrioridades().contains(
																		entidadSubContratadaDto.getNumeroPrioridad())) {
																	esEntidadValida = true;
																	nivelAutorizacionMedicamento = nivelMedicamento;
																	break;
																}
															}
															
															if (esEntidadValida) {

																InfoCobertura info = coberturaMundo.validacionCoberturaArticuloEntidadSub(
																		entidadSubContratadaDto.getCodContratoEntidadSub(), 
																		ordenAutorizacionDto.getCodigoViaIngreso(), 
																		autorizacionCapitacionDto.getDatosPacienteAutorizar().getTipoPaciente(), 
																		medicamento.getCodigo(), 
																		autorizacionCapitacionDto.getDatosPacienteAutorizar().getNaturalezaPaciente(), 
																		autorizacionCapitacionDto.getCodigoInstitucion());

																if(info.getIncluido()) {
																	medicamento.setNivelAutorizacion(nivelAutorizacionMedicamento);
																	medicamento.setAutorizar(true);
																	medicamento.setPuedeAutorizar(true);
																} else {
																	medicamento.setAutorizar(false);
																	medicamento.setAutorizado(false);
																	medicamento.setPuedeAutorizar(false);
																	ordenAutorizacionDto.setPuedeAutorizar(false);
																	noAutorizaMedicamento = true;
																}
															} else {
																medicamento.setAutorizar(false);
																medicamento.setAutorizado(false);
																medicamento.setPuedeAutorizar(false);
																ordenAutorizacionDto.setPuedeAutorizar(false);
																noAutorizaMedicamento = true;
															}
															
														} else {
															medicamento.setAutorizar(false);
															medicamento.setAutorizado(false);
															medicamento.setPuedeAutorizar(false);
															ordenAutorizacionDto.setPuedeAutorizar(false);
															noAutorizaMedicamento = true;
														}
														
														if (esEntidadValida || noAutorizaMedicamento) {
															
															if (mapaOrdenesXEntidadSubContratada.containsKey(
																	entidadSubContratadaDto.getCodEntidadSubcontratada())) {

																mapaOrdenesXEntidadSubContratada.get(
																		Long.valueOf(entidadSubContratadaDto.getCodEntidadSubcontratada())).
																		getOrdenesAutorizar().get(0).getMedicamentosInsumosPorAutorizar().add(medicamento);

															} else {
																//Se crea una nueva autorizacion si existen diferentes Entidades SubContratadas
																List<OrdenAutorizacionDto> listaNuevasOrdenes = new ArrayList<OrdenAutorizacionDto>();
																List<MedicamentoInsumoAutorizacionOrdenDto> listaMedicamentos = 
																		new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
																listaMedicamentos.add(medicamento);
																listaNuevasOrdenes.add(this.copiarOrdenAutorizacionDto(
																		ordenAutorizacionDto, null, listaMedicamentos));
																AutorizacionCapitacionDto nuevaAutorizacion =
																		this.copiarAutorizacionCapitacionDto(autorizacionDto, listaNuevasOrdenes);
																nuevaAutorizacion.setEntidadSubAutorizarCapitacion(entidadSubContratadaDto);
																mapaOrdenesXEntidadSubContratada.put(
																		Long.valueOf(entidadSubContratadaDto.getCodEntidadSubcontratada()), nuevaAutorizacion);

															}
															break;
														}
													}
												}
											}

										} else {
											autorizacionDto.setProcesoExitoso(false);
											autorizacionDto.setVerificarDetalleError(true);
											//Indicar que los servicios/medicamentos no se pueden autorizar
											for (OrdenAutorizacionDto ordenesAutorizar : autorizacionDto.getOrdenesAutorizar()) {
												if(ordenesAutorizar.getServiciosPorAutorizar()!=null &&
														!ordenesAutorizar.getServiciosPorAutorizar().isEmpty()){
													for (ServicioAutorizacionOrdenDto serviciosAutorizar : ordenesAutorizar.getServiciosPorAutorizar()) {
														serviciosAutorizar.setAutorizado(false);
														serviciosAutorizar.setAutorizar(false);
													}
												}else{
													for (MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar : ordenesAutorizar.getMedicamentosInsumosPorAutorizar()) {
														medicamentosAutorizar.setAutorizado(false);
														medicamentosAutorizar.setAutorizar(false);
														Log4JManager.info("");
													}
												}
											}
											Log4JManager.warning("El centro de costo que ejecuta no tiene asociada entidad subcontratada"); 
											break;
										}
									}
								} else {
									//Para Ordenes Ambulatorias de Medicamentos 
									if (autorizacionDto.getEntidadSubAutorizarCapitacion() != null &&
											autorizacionDto.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() != 
											ConstantesBD.codigoNuncaValidoLong) {

										List<DtoCentroCosto> listaCentrosCosto = new ArrayList<DtoCentroCosto>();

										//Se realiza la busqueda de centros de costo por entidad subcontratada
										//donde Tipo Area = Subalmacen 
										listaCentrosCosto = catalogoFacturacionDelegate.obtenerCentrosCostoEntidadSubContratadaXTipoArea(
												autorizacionDto.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada(), 
												ConstantesBD.codigoTipoAreaSubalmacen);

										if (!listaCentrosCosto.isEmpty()) {
											autorizacionDto.setListaCentroCostoEjecOrdenAmbArtic(listaCentrosCosto);
											autorizacionDto.setProcesoExitoso(true);
											
										} else {
											//Si no encuentra centros de costo para la 
											//entidad subcontrada se cancela el proceso
											autorizacionDto.setProcesoExitoso(false);
											autorizacionDto.setVerificarDetalleError(true);
											
											for (OrdenAutorizacionDto ordenAutorizar : autorizacionDto.getOrdenesAutorizar()) {
												for (MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar : ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
													medicamentosAutorizar.setAutorizado(false);
													medicamentosAutorizar.setAutorizar(false);
												}
											}
											Log4JManager.warning("No existen almacenes para la entidad subcontrada"); 
										}

									} else {
										autorizacionDto.setProcesoExitoso(false);
										autorizacionDto.setVerificarDetalleError(true);
									}

								}
							}
							
							//Si se realizo agrupacion por entidad subcontratada para 
							//centro de costo interno se organizan las autorizaciones
							//que se van a retornar
							if (!mapaOrdenesXEntidadSubContratada.isEmpty()) {

								for (Map.Entry<Long, AutorizacionCapitacionDto>  mapa : 
									mapaOrdenesXEntidadSubContratada.entrySet()) {

									for (OrdenAutorizacionDto ordenAutorizar : mapa.getValue().getOrdenesAutorizar()) {

										if (((ordenAutorizar.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia) ||
												(ordenAutorizar.getClaseOrden() == ConstantesBD.claseOrdenPeticion && 
												ordenAutorizar.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)) &&
												!ordenAutorizar.isPuedeAutorizar()) {
											
											mapa.getValue().setProcesoExitoso(false);
											mapa.getValue().setVerificarDetalleError(true);
										} else {

											if(ordenAutorizar.getServiciosPorAutorizar() != null &&
													!ordenAutorizar.getServiciosPorAutorizar().isEmpty()) {

												for (ServicioAutorizacionOrdenDto servicio : 
													ordenAutorizar.getServiciosPorAutorizar()) {

													if (servicio.isAutorizar()) {
														mapa.getValue().setProcesoExitoso(true);
														break;
													} else {
														mapa.getValue().setVerificarDetalleError(true);
													}
												}
											} else if(ordenAutorizar.getMedicamentosInsumosPorAutorizar() != null &&
													!ordenAutorizar.getMedicamentosInsumosPorAutorizar().isEmpty()) {

												for (MedicamentoInsumoAutorizacionOrdenDto medicamento : 
													ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
													if (medicamento.isAutorizar()) {
														mapa.getValue().setProcesoExitoso(true);
														break;
													} else {
														mapa.getValue().setVerificarDetalleError(true);
													}
												}
											}
										}
									}

									listaAutorizacionesValidadas.add(mapa.getValue());
								}
								
							} else {
								//Si Centros de Costo para el Centro de Costo que ejecuta
								//esta definido como 'Interno' se organiza la informacion
								//de las autorizaciones que se van a retornar
								for (OrdenAutorizacionDto ordenAutorizar : autorizacionDto.getOrdenesAutorizar()) {

									if ((ordenAutorizar.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia ||
											(ordenAutorizar.getClaseOrden() == ConstantesBD.claseOrdenPeticion && 
											ordenAutorizar.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)) &&
											!ordenAutorizar.isPuedeAutorizar()) {
										
										autorizacionDto.setProcesoExitoso(false);
										autorizacionDto.setVerificarDetalleError(true);
									} else {

										if(ordenAutorizar.getServiciosPorAutorizar() != null &&
												!ordenAutorizar.getServiciosPorAutorizar().isEmpty()) {

											for (ServicioAutorizacionOrdenDto servicio : 
												ordenAutorizar.getServiciosPorAutorizar()) {

												if (servicio.isAutorizar()) {
													autorizacionDto.setProcesoExitoso(true);
													break;
												} else {
													autorizacionDto.setVerificarDetalleError(true);
												}
											}
										} else if(ordenAutorizar.getMedicamentosInsumosPorAutorizar() != null &&
												!ordenAutorizar.getMedicamentosInsumosPorAutorizar().isEmpty()) {
											for (MedicamentoInsumoAutorizacionOrdenDto medicamento : 
												ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
												if (medicamento.isAutorizar()) {
													autorizacionDto.setProcesoExitoso(true);
													break;
												} else {
													autorizacionDto.setVerificarDetalleError(true);
												}
											}
										}
									}
								}
								
								listaAutorizacionesValidadas.add(autorizacionDto);
							}

						} else {
							autorizacionDto.setProcesoExitoso(false);
							autorizacionDto.setVerificarDetalleError(true);
							Log4JManager.warning("No existen niveles de autorizacion automatica"); 
							
							listaAutorizacionesValidadas.add(autorizacionDto);
						}
					} else {
						autorizacionDto.setProcesoExitoso(false);
						autorizacionDto.setVerificarDetalleError(true);
						Log4JManager.warning("No existen niveles de autorizacion para la via de ingreso"); 
						
						listaAutorizacionesValidadas.add(autorizacionDto);
					}
				}
			} else {
				for (AutorizacionCapitacionDto autorizacionCapitacion : listaAutorizacionesAgrupadas) {
					autorizacionCapitacion.setProcesoExitoso(false);
					autorizacionCapitacion.setVerificarDetalleError(true);
					Log4JManager.warning("No existen niveles de autorizacion para el usuario o profesional"); 
				}
				listaAutorizacionesValidadas = listaAutorizacionesAgrupadas;
			}
			if(requiereTransaccion){
				HibernateUtil.endTransaction();
			}
			return listaAutorizacionesValidadas;
			
		} catch (IPSException ipsme) {
			if(requiereTransaccion){
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		} catch (Exception e) {
			if(requiereTransaccion){
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
	}

	/**
	 * Método encargado de realizar la agrupación de ordenes de servicios por 
	 * centro de costo que ejecuta, PyP y tipo de monto del servicio
	 * @param autorizacionCapitacionDto
	 * @return
	 * @throws IPSException
	 */
	public List<AutorizacionCapitacionDto> agruparOrdenesAutorizacion(
			AutorizacionCapitacionDto autorizacionCapitacionDto)
			throws IPSException {
		
		AutorizacionCapitacionDto autorizacion = null;
		AutorizacionCapitacionDto autorizacionAgrupada = null;
		List<OrdenAutorizacionDto> listaOrdenesAgrupadas = null;
		List<OrdenAutorizacionDto> ordenesConPyP = null;
		List<OrdenAutorizacionDto> ordenesSinPyP = null;
		List<OrdenAutorizacionDto> listaOrdenesAgrupadasTipoMonto = null;
		List<AutorizacionCapitacionDto> listaAutorizaciones = new ArrayList<AutorizacionCapitacionDto>();
		Map<Integer, List<OrdenAutorizacionDto>> mapaOrdenesAgrupadas = new HashMap<Integer, List<OrdenAutorizacionDto>>();
		Map<Integer, List<OrdenAutorizacionDto>> mapaOrdenesAgrupadasXTipoMontoConPyP = null;
		Map<Integer, List<OrdenAutorizacionDto>> mapaOrdenesAgrupadasXTipoMontoSinPyP = null;
		boolean agruparXTipoMonto = true;
		boolean sinAgrupacion = false;
		
		try {

			//Agrupamiento por centro de costo que ejecuta
			for (OrdenAutorizacionDto ordenesAutorizacionDto : autorizacionCapitacionDto.getOrdenesAutorizar()) {

				if ((ordenesAutorizacionDto.getClaseOrden() == ConstantesBD.claseOrdenCargoDirecto &&
						ordenesAutorizacionDto.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCargosDirectosServicios) ||
						(ordenesAutorizacionDto.getClaseOrden() == ConstantesBD.claseOrdenOrdenMedica &&
						ordenesAutorizacionDto.getTipoOrden() == ConstantesBD.codigoTipoSolicitudProcedimiento) ||
						ordenesAutorizacionDto.getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria && 
						ordenesAutorizacionDto.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios) {

					if (mapaOrdenesAgrupadas.containsKey(ordenesAutorizacionDto.getCodigoCentroCostoEjecuta())) {

						mapaOrdenesAgrupadas.get(
								ordenesAutorizacionDto.getCodigoCentroCostoEjecuta()).add(ordenesAutorizacionDto);

					} else {

						listaOrdenesAgrupadas = new ArrayList<OrdenAutorizacionDto>();
						listaOrdenesAgrupadas.add(ordenesAutorizacionDto);
						mapaOrdenesAgrupadas.put(
								ordenesAutorizacionDto.getCodigoCentroCostoEjecuta(), 
								listaOrdenesAgrupadas);

					}

				} else {
					
					sinAgrupacion = true;
					
				}
				
			}
			
			if (!sinAgrupacion) {

				//Agrupamiento por PYP
				for (Map.Entry<Integer, List<OrdenAutorizacionDto>>  mapa : mapaOrdenesAgrupadas.entrySet()) {

					ordenesConPyP = new ArrayList<OrdenAutorizacionDto>();
					ordenesSinPyP = new ArrayList<OrdenAutorizacionDto>();
					mapaOrdenesAgrupadasXTipoMontoConPyP = new HashMap<Integer, List<OrdenAutorizacionDto>>();
					mapaOrdenesAgrupadasXTipoMontoSinPyP = new HashMap<Integer, List<OrdenAutorizacionDto>>();
					for (OrdenAutorizacionDto ordenAutorizacion : mapa.getValue()) {

						if (ordenAutorizacion.isEsPyp()) {
							ordenesConPyP.add(ordenAutorizacion);
						} else {
							ordenesSinPyP.add(ordenAutorizacion);
						}

					}

					//Agrupamiento ordenes ambulatorias de servicios Con PYP
					for (OrdenAutorizacionDto ordenAutorizacion : ordenesConPyP) {

						if (ordenAutorizacion.getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria && 
								ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios) {

							if(ordenAutorizacion.getServiciosPorAutorizar().size() == 1) {

								if(mapaOrdenesAgrupadasXTipoMontoConPyP.containsKey(
										ordenAutorizacion.getServiciosPorAutorizar().get(0).getTipoMonto())) {

									mapaOrdenesAgrupadasXTipoMontoConPyP.get(
											ordenAutorizacion.getServiciosPorAutorizar().get(0).getTipoMonto()).add(ordenAutorizacion);

								} else {

									listaOrdenesAgrupadasTipoMonto = new ArrayList<OrdenAutorizacionDto>();
									listaOrdenesAgrupadasTipoMonto.add(ordenAutorizacion);
									mapaOrdenesAgrupadasXTipoMontoConPyP.put(
											ordenAutorizacion.getServiciosPorAutorizar().get(0).getTipoMonto(), 
											listaOrdenesAgrupadasTipoMonto);
								}

							} else {
								//Si vienen más de dos servicios por orden (Caso que no deberia suceder) 
								//no se realiza el agrupamiento por tipo de monto
								agruparXTipoMonto = false;
								break;
							}

						} else {
							//Si viene una orden diferente a una Orden ambulatoria de servicios
							//no se realiza el agrupamiento por tipo de monto
							agruparXTipoMonto = false;
							break;
						}
					}

					agruparXTipoMonto = true;
					//Agrupamiento ordenes ambulatorias de servicios Sin PYP
					for (OrdenAutorizacionDto ordenAutorizacion : ordenesSinPyP) {

						if (ordenAutorizacion.getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria && 
								ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios) {

							if(ordenAutorizacion.getServiciosPorAutorizar().size() == 1) {

								if(mapaOrdenesAgrupadasXTipoMontoSinPyP.containsKey(
										ordenAutorizacion.getServiciosPorAutorizar().get(0).getTipoMonto())) {

									mapaOrdenesAgrupadasXTipoMontoSinPyP.get(
											ordenAutorizacion.getServiciosPorAutorizar().get(0).getTipoMonto()).add(ordenAutorizacion);

								} else {
									listaOrdenesAgrupadasTipoMonto = new ArrayList<OrdenAutorizacionDto>();
									listaOrdenesAgrupadasTipoMonto.add(ordenAutorizacion);
									mapaOrdenesAgrupadasXTipoMontoSinPyP.put(
											ordenAutorizacion.getServiciosPorAutorizar().get(0).getTipoMonto(), 
											listaOrdenesAgrupadasTipoMonto);
								}

							} else {
								//Si vienen más de dos servicios por orden (Caso que no deberia suceder) 
								//no se realiza el agrupamiento por tipo de monto
								agruparXTipoMonto = false;
								break;
							}

						} else {
							//Si viene una orden diferente a una Orden ambulatoria de servicios
							//no se realiza el agrupamiento por tipo de monto
							agruparXTipoMonto = false;
							break;
						}
					}

					if (agruparXTipoMonto) {

						for (Map.Entry<Integer, List<OrdenAutorizacionDto>>  ordenesAgrupadas : mapaOrdenesAgrupadasXTipoMontoConPyP.entrySet()) {

							autorizacionAgrupada = this.copiarAutorizacionCapitacionDto(
									autorizacionCapitacionDto, ordenesAgrupadas.getValue());
							if (autorizacionAgrupada != null) {
								listaAutorizaciones.add(autorizacionAgrupada);
							}

						}

						for (Map.Entry<Integer, List<OrdenAutorizacionDto>>  ordenesAgrupadas : mapaOrdenesAgrupadasXTipoMontoSinPyP.entrySet()) {

							autorizacionAgrupada = this.copiarAutorizacionCapitacionDto(
									autorizacionCapitacionDto, ordenesAgrupadas.getValue());
							if (autorizacionAgrupada != null) {
								listaAutorizaciones.add(autorizacionAgrupada);
							}

						}

					} else {

						if (!ordenesConPyP.isEmpty()) {

							autorizacionAgrupada = this.copiarAutorizacionCapitacionDto(
									autorizacionCapitacionDto, ordenesConPyP);
							if (autorizacionAgrupada != null) {
								listaAutorizaciones.add(autorizacionAgrupada);
							}

						}

						if (!ordenesSinPyP.isEmpty()) {

							autorizacionAgrupada = this.copiarAutorizacionCapitacionDto(
									autorizacionCapitacionDto, ordenesSinPyP);
							if (autorizacionAgrupada != null) {
								listaAutorizaciones.add(autorizacionAgrupada);
							}

						}

					}					
				}

			} else {
				
				for (OrdenAutorizacionDto ordenesAutorizacionDto : autorizacionCapitacionDto.getOrdenesAutorizar()) {
					
					List<OrdenAutorizacionDto> ordenesAutorizacion = new ArrayList<OrdenAutorizacionDto>();
					
					ordenesAutorizacion.add(ordenesAutorizacionDto);
					autorizacion = this.copiarAutorizacionCapitacionDto(
							autorizacionCapitacionDto, ordenesAutorizacion);
					if (autorizacion != null) {
						listaAutorizaciones.add(autorizacion);
					}
				}
			}
			
		} catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return listaAutorizaciones;
	}
	
	/**
	 * Metodo encargado de crear una copia de una autorizacion
	 * @param autorizacionCapitacionDto
	 * @param listaOrdenesAgrupadas
	 * @return
	 */
	private AutorizacionCapitacionDto copiarAutorizacionCapitacionDto(
			AutorizacionCapitacionDto autorizacionCapitacionDto,
			List<OrdenAutorizacionDto> listaOrdenes) {
		
		if (autorizacionCapitacionDto != null) {
			AutorizacionCapitacionDto copiaAutorizacionCapitacionDto = new AutorizacionCapitacionDto(
					autorizacionCapitacionDto.getTipoAutorizacion(),
					autorizacionCapitacionDto.getCodigoInstitucion(),
					autorizacionCapitacionDto.getFechaVencimientoAutorizacion(),
					autorizacionCapitacionDto.getObservacionesGenerales(),
					autorizacionCapitacionDto.getLoginUsuario(),
					autorizacionCapitacionDto.getCodigoPersonaUsuario(),
					autorizacionCapitacionDto.getCentroAtencion(),
					autorizacionCapitacionDto.isProcesoExitoso(),
					autorizacionCapitacionDto.isVerificarDetalleError(),
					listaOrdenes,
					autorizacionCapitacionDto.getEntidadSubAutorizarCapitacion(),
					autorizacionCapitacionDto.getDatosPacienteAutorizar(),
					autorizacionCapitacionDto.getMontoCobroAutorizacion(),
					autorizacionCapitacionDto.getListaCentroCostoEjecOrdenAmbArtic(),
					autorizacionCapitacionDto.isAutoServArtIngresoEstancia(),
					autorizacionCapitacionDto.getMensajeErrorGeneral(),
					autorizacionCapitacionDto.getCodConvenioRecobro(),
					autorizacionCapitacionDto.getDescripcionConvenioRecobro());
			return copiaAutorizacionCapitacionDto;
		} else {
			return null;
		}
		
	}
	
	
	/**
	 * Metodo encargado de crear una copia de una autorizacion
	 * @param autorizacionCapitacionDto
	 * @param listaOrdenesAgrupadas
	 * @return
	 */
	private OrdenAutorizacionDto copiarOrdenAutorizacionDto(
			OrdenAutorizacionDto ordenOriginal, List<ServicioAutorizacionOrdenDto> serviciosAutorizar,
				List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosAutorizar) {
		
		if (ordenOriginal != null) {
			OrdenAutorizacionDto copiaOrdenAutorizacionDto = new OrdenAutorizacionDto();
			copiaOrdenAutorizacionDto.setCodigoOrden(ordenOriginal.getCodigoOrden());
			copiaOrdenAutorizacionDto.setConsecutivoOrden(ordenOriginal.getConsecutivoOrden());
			copiaOrdenAutorizacionDto.setTipoOrden(ordenOriginal.getTipoOrden());
			copiaOrdenAutorizacionDto.setClaseOrden(ordenOriginal.getClaseOrden());
			copiaOrdenAutorizacionDto.setFechaOrden(ordenOriginal.getFechaOrden());
			copiaOrdenAutorizacionDto.setEsUrgente(ordenOriginal.getEsUrgente());
			copiaOrdenAutorizacionDto.setCodigoCentroCostoEjecuta(ordenOriginal.getCodigoCentroCostoEjecuta());
			copiaOrdenAutorizacionDto.setNombreCentroCostoEjecuta(ordenOriginal.getNombreCentroCostoEjecuta());
			copiaOrdenAutorizacionDto.setCodigoIngreso(ordenOriginal.getCodigoIngreso());
			copiaOrdenAutorizacionDto.setConsecutivoIngreso(ordenOriginal.getConsecutivoIngreso());
			copiaOrdenAutorizacionDto.setFechaPosponer(ordenOriginal.getFechaPosponer());
			copiaOrdenAutorizacionDto.setServiciosPorAutorizar(serviciosAutorizar);
			copiaOrdenAutorizacionDto.setMedicamentosInsumosPorAutorizar(medicamentosAutorizar);
			copiaOrdenAutorizacionDto.setPosponer(ordenOriginal.isPosponer());
			copiaOrdenAutorizacionDto.setCodigoViaIngreso(ordenOriginal.getCodigoViaIngreso());
			copiaOrdenAutorizacionDto.setMostrarLink(ordenOriginal.isMostrarLink());
			copiaOrdenAutorizacionDto.setMostrarPosponer(ordenOriginal.isMostrarPosponer());
			copiaOrdenAutorizacionDto.setMostrarAutoMultiple(ordenOriginal.isMostrarAutoMultiple());
			copiaOrdenAutorizacionDto.setEsPyp(ordenOriginal.isEsPyp());
			copiaOrdenAutorizacionDto.setNombreNivelesAtencion(ordenOriginal.getNombreNivelesAtencion());
			copiaOrdenAutorizacionDto.setTipoEntidadEjecuta(ordenOriginal.getTipoEntidadEjecuta());
			copiaOrdenAutorizacionDto.setContrato(ordenOriginal.getContrato());
			copiaOrdenAutorizacionDto.setMigrado(ordenOriginal.getMigrado());
			copiaOrdenAutorizacionDto.setCheckeadoPosponer(ordenOriginal.isCheckeadoPosponer());
			copiaOrdenAutorizacionDto.setCheckeadoAutoMultiple(ordenOriginal.isCheckeadoAutoMultiple());
			copiaOrdenAutorizacionDto.setCodigoPaciente(ordenOriginal.getCodigoPaciente());
			copiaOrdenAutorizacionDto.setIdentificacionPaciente(ordenOriginal.getIdentificacionPaciente());
			copiaOrdenAutorizacionDto.setNombresApellidosPaciente(ordenOriginal.getNombresApellidosPaciente());
			copiaOrdenAutorizacionDto.setPuedeAutorizar(ordenOriginal.isPuedeAutorizar());
			copiaOrdenAutorizacionDto.setOtroCodigoOrden(ordenOriginal.getOtroCodigoOrden());		
			return copiaOrdenAutorizacionDto;
		} else {
			return null;
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo#verificarNivelesAutorizacionOrdenes(java.util.List, java.util.List, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> verificarNivelesAutorizacionOrdenes(
			List<OrdenAutorizacionDto> ordenesAutorizacion,
			List<NivelAutorizacionDto> nivelesAutorizacionUsuario,
			boolean requiereTransaccion, AutorizacionCapitacionDto autorizacionCapitacionDto)
			throws IPSException {
		
		List<Integer> codigosNivelesAutorizacionUsuario = null;
		List<Integer> codigosNivelesAutorizacionAutomatica = new ArrayList<Integer>();
		List<Integer> codigosNivelesAutorizacionServicioMedicamento = new ArrayList<Integer>();
		List<Integer> codigosNivelesInterseccionUsuarioServMedicamento = null;
		List<NivelAutorizacionDto> nivelesInterseccionUsuarioServMedicamento = new ArrayList<NivelAutorizacionDto>();
		Map<Integer, NivelAutorizacionDto> mapaNivelesAutorizacion = new HashMap<Integer, NivelAutorizacionDto>();
		NivelAutorizacionDelegate nivelAutorizacionDelegate = null;
		boolean puedeAutorizarOrden = true;
		
		try {
			
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			nivelAutorizacionDelegate = new NivelAutorizacionDelegate();
			
			for (NivelAutorizacionDto nivelAutorizacionDto : nivelesAutorizacionUsuario) {
				mapaNivelesAutorizacion.put(nivelAutorizacionDto.getCodigo(), nivelAutorizacionDto);
			}
			
			codigosNivelesAutorizacionUsuario = new ArrayList<Integer>(mapaNivelesAutorizacion.keySet());
			
			for (OrdenAutorizacionDto ordenAutorizacion : ordenesAutorizacion) {
				
				if (!ordenAutorizacion.getServiciosPorAutorizar().isEmpty()) {

					for(ServicioAutorizacionOrdenDto dtoServicio : 
								ordenAutorizacion.getServiciosPorAutorizar()) {

						codigosNivelesInterseccionUsuarioServMedicamento = new ArrayList<Integer>();
						
						//Validación de los niveles de autorización por medicamento-insumo específico
						codigosNivelesAutorizacionServicioMedicamento = 
								nivelAutorizacionDelegate.obtenerNivelAutorizacionServicio(
										dtoServicio.getCodigo());
						
						//se verifica en en los niveles de autorización automática que niveles corresponden
						//a los niveles encontrados por servicio y se agregan a la lista de niveles automáticos
						codigosNivelesInterseccionUsuarioServMedicamento = 
								ListUtils.intersection(codigosNivelesAutorizacionUsuario, 
										codigosNivelesAutorizacionServicioMedicamento);
						
						//Si no se encuentran niveles especificos para el servicios para 
						//los niveles del usuario se realiza la busqueda por agrupacion
						if(codigosNivelesInterseccionUsuarioServMedicamento == null ||
								codigosNivelesInterseccionUsuarioServMedicamento.isEmpty()) {
							//Validación de los niveles de autorización por medicamento-insumo agrupado
							codigosNivelesAutorizacionServicioMedicamento = nivelAutorizacionDelegate.
									obtenerNivelAutorizacionServicioAgrupado(
											dtoServicio.getCodigoEspecialidad(),
											dtoServicio.getAcronimoTipoServicio(),
											dtoServicio.getCodigoGrupoServicio());
						}
						
						//se verifica en en los niveles de autorización automática que niveles corresponden
						//a los niveles encontrados por medicamento y se agregan a la lista de niveles automáticos
						codigosNivelesInterseccionUsuarioServMedicamento = 
								ListUtils.intersection(codigosNivelesAutorizacionUsuario, 
										codigosNivelesAutorizacionServicioMedicamento);

						for (Integer codigoNivelAutorizacionDto : codigosNivelesInterseccionUsuarioServMedicamento) {
							if (mapaNivelesAutorizacion.get(codigoNivelAutorizacionDto) != null) {
								nivelesInterseccionUsuarioServMedicamento.add(
										mapaNivelesAutorizacion.get(codigoNivelAutorizacionDto));
							}
						}
						
						if (!codigosNivelesInterseccionUsuarioServMedicamento.isEmpty()) {
							
							for (Integer codigoNivel : codigosNivelesInterseccionUsuarioServMedicamento) {
								
								if (!codigosNivelesAutorizacionAutomatica.contains(codigoNivel)) {
								
									codigosNivelesAutorizacionAutomatica.add(codigoNivel);
									
								}
							}	
							
							dtoServicio.setNivelesAutorizacion(nivelesInterseccionUsuarioServMedicamento);
							dtoServicio.setPuedeAutorizar(true);
							nivelesInterseccionUsuarioServMedicamento = new ArrayList<NivelAutorizacionDto>();
						} else {
							dtoServicio.setPuedeAutorizar(false);
							puedeAutorizarOrden = false;
						}
					}

				} else {
					
					for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamentoInsumo : 
								ordenAutorizacion.getMedicamentosInsumosPorAutorizar()) {
						
						codigosNivelesInterseccionUsuarioServMedicamento = new ArrayList<Integer>();
						
						//Validación de los niveles de autorización por medicamento-insumo específico
						codigosNivelesAutorizacionServicioMedicamento = 
								nivelAutorizacionDelegate.obtenerNivelAutorizacionMedicamentoInsumo(
										dtoMedicamentoInsumo.getCodigo());

						//se verifica en en los niveles de autorización automática que niveles corresponden
						//a los niveles encontrados por medicamento-insumo y se agregan a la lista de niveles automáticos
						codigosNivelesInterseccionUsuarioServMedicamento = 
								ListUtils.intersection(codigosNivelesAutorizacionUsuario, 
										codigosNivelesAutorizacionServicioMedicamento);
						
						//Si no se encuentran niveles especificos para el medicamento-insumo para 
						//los niveles del usuario se realiza la busqueda por agrupacion
						if(codigosNivelesInterseccionUsuarioServMedicamento == null ||
								codigosNivelesInterseccionUsuarioServMedicamento.isEmpty()) {

							codigosNivelesAutorizacionServicioMedicamento = nivelAutorizacionDelegate.
									obtenerNivelAutorizacionMedicamentoInsumoAgrupado(
											dtoMedicamentoInsumo.getSubGrupoInventario(), 
											dtoMedicamentoInsumo.getAcronimoNaturaleza());
						}
						
						//se verifica en en los niveles de autorización automática que niveles corresponden
						//a los niveles encontrados por medicamento y se agregan a la lista de niveles automáticos
						codigosNivelesInterseccionUsuarioServMedicamento = 
								ListUtils.intersection(codigosNivelesAutorizacionUsuario, 
										codigosNivelesAutorizacionServicioMedicamento);
						
						for (Integer codigoNivelAutorizacionDto : codigosNivelesInterseccionUsuarioServMedicamento) {
							if (mapaNivelesAutorizacion.get(codigoNivelAutorizacionDto) != null) {
								nivelesInterseccionUsuarioServMedicamento.add(
										mapaNivelesAutorizacion.get(codigoNivelAutorizacionDto));
							}
						}
						
						if (!codigosNivelesInterseccionUsuarioServMedicamento.isEmpty()) {
							
							for (Integer codigoNivel : codigosNivelesInterseccionUsuarioServMedicamento) {
								
								if (!codigosNivelesAutorizacionAutomatica.contains(codigoNivel)) {
								
									codigosNivelesAutorizacionAutomatica.add(codigoNivel);
									
								}
							}	
							
							dtoMedicamentoInsumo.setNivelesAutorizacion(nivelesInterseccionUsuarioServMedicamento);
							dtoMedicamentoInsumo.setPuedeAutorizar(true);
						} else {
							dtoMedicamentoInsumo.setPuedeAutorizar(false);
							dtoMedicamentoInsumo.setAutorizar(false);
							dtoMedicamentoInsumo.setAutorizado(false);
							autorizacionCapitacionDto.setVerificarDetalleError(true);
							puedeAutorizarOrden = false;
						}
					}
				}
				
				//valida si alguno de los medicamentos o insumos no pudieron ser autorizados para la orden
				ordenAutorizacion.setPuedeAutorizar(puedeAutorizarOrden);
				
			}

			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
			
		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		}
		catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return codigosNivelesAutorizacionAutomatica;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.INivelAutorizacionMundo#validarNivelesAutorizacionAutorizacionesPoblacionCapitada(com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto, boolean, boolean)
	 */
	@Override
	public void validarNivelesAutorizacionAutorizacionesPoblacionCapitada(
			AutorizacionCapitacionDto autorizacionCapitacionDto,
			boolean esTodosRequeridos,
			boolean requiereTransaccion)
			throws IPSException {
		
		List<NivelAutorizacionDto> listaNivelesAutorizacionUsuario = new ArrayList<NivelAutorizacionDto>();
		List<NivelAutorizacionDto> listaNivelesAutorizacionAutomatica = null;
		List<Integer> nivelesAutorizacionAutomatica = new ArrayList<Integer>();
		NivelAutorizacionDto nivelAutorizacionServicioMedicamento = null;
		boolean generarAutorizacion=false;
		autorizacionCapitacionDto.setProcesoExitoso(true);
		
		try {

			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}

			listaNivelesAutorizacionUsuario = this.consultarNivelesAutorizacionUsuario(
					autorizacionCapitacionDto.getLoginUsuario(), 
					autorizacionCapitacionDto.getCodigoPersonaUsuario(),
					ConstantesIntegridadDominio.acronimoTipoAutorizacionManual, true, requiereTransaccion);

			if (listaNivelesAutorizacionUsuario != null && 
					!listaNivelesAutorizacionUsuario.isEmpty()) {
				
				for (OrdenAutorizacionDto ordenAutorizacionDto : autorizacionCapitacionDto.getOrdenesAutorizar()) {
					
					listaNivelesAutorizacionAutomatica = new ArrayList<NivelAutorizacionDto>();
					/*2.SELECCION DE NIVELES DE AUTORIZACION MANUALES
						en la consultarNivelesAutorizacionUsuario se realiza la validación por tipo autorización
						y estado activo por lo cual en este punto solo se valida la vía de ingreso */
					for (NivelAutorizacionDto nivelAutorizacionDto : listaNivelesAutorizacionUsuario) {
						/*Si se cumple la validación de la vía de ingreso se realiza el punto
						 * 3. Niveles de Autorización para Servicios, Medicamentos o Insumos a Autorizar*/
						if(ordenAutorizacionDto.getCodigoViaIngreso() == 
								nivelAutorizacionDto.getViaIngreso().getCodigo() && 
								!listaNivelesAutorizacionAutomatica.contains(nivelAutorizacionDto)) {
							listaNivelesAutorizacionAutomatica.add(nivelAutorizacionDto);
						}
					}
				}

				if (!listaNivelesAutorizacionAutomatica.isEmpty()) {

					nivelesAutorizacionAutomatica = verificarNivelesAutorizacionOrdenes(
							autorizacionCapitacionDto.getOrdenesAutorizar(), 
							listaNivelesAutorizacionAutomatica, requiereTransaccion, autorizacionCapitacionDto);

					if (!nivelesAutorizacionAutomatica.isEmpty()) {
						for (OrdenAutorizacionDto ordenAutorizacionDto : autorizacionCapitacionDto.getOrdenesAutorizar()) {

							if(!ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()) {

								for (ServicioAutorizacionOrdenDto servicio : 
									ordenAutorizacionDto.getServiciosPorAutorizar()) {

									nivelAutorizacionServicioMedicamento = null;

									for (NivelAutorizacionDto nivelServicio : servicio.getNivelesAutorizacion()) {

										if (autorizacionCapitacionDto.getEntidadSubAutorizarCapitacion() != null) {

											if(nivelServicio.getPrioridades().contains(autorizacionCapitacionDto.
													getEntidadSubAutorizarCapitacion().getNumeroPrioridad())) {
												nivelAutorizacionServicioMedicamento = nivelServicio;
												servicio.setNivelAutorizacion(nivelAutorizacionServicioMedicamento);
												servicio.setAutorizar(true);
												servicio.setPuedeAutorizar(true);
												generarAutorizacion=true;
												autorizacionCapitacionDto.setProcesoExitoso(true);
												break;
											} else {
												servicio.setPuedeAutorizar(false);
												ordenAutorizacionDto.setPuedeAutorizar(false);
												ErrorMessage mensajeErrorGeneralPriori = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorServicio",
														servicio.getCodigo()+"");
												servicio.setMensajeError(mensajeErrorGeneralPriori);
												autorizacionCapitacionDto.setVerificarDetalleError(true);

												if (esTodosRequeridos) {
													autorizacionCapitacionDto.setProcesoExitoso(false);
													ErrorMessage mensajeErrorGeneral = new ErrorMessage(
															"errors.autorizacion.noExisteNivelAutorizacionUsuario");
													autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
													autorizacionCapitacionDto.setVerificarDetalleError(false);
												}
											}
										} else {
											autorizacionCapitacionDto.setProcesoExitoso(false);
											ErrorMessage mensajeErrorGeneral = new ErrorMessage(
													"errors.autorizacion.noExisteNivelAutorizacionUsuario");
											autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
											autorizacionCapitacionDto.setVerificarDetalleError(false);
										}
									}
								}

							} else if (!ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar().isEmpty()) {

								for (MedicamentoInsumoAutorizacionOrdenDto medicamento : 
									ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()) {

									nivelAutorizacionServicioMedicamento = null;

									for (NivelAutorizacionDto nivelMedicamento : medicamento.getNivelesAutorizacion()) {

										if(nivelMedicamento.getPrioridades().contains(autorizacionCapitacionDto.
												getEntidadSubAutorizarCapitacion().getNumeroPrioridad())) {
											nivelAutorizacionServicioMedicamento = nivelMedicamento;
											medicamento.setNivelAutorizacion(nivelAutorizacionServicioMedicamento);
											medicamento.setAutorizar(true);
											medicamento.setPuedeAutorizar(true);
											generarAutorizacion=true;
											autorizacionCapitacionDto.setProcesoExitoso(true);
											break;
										} else {
											medicamento.setPuedeAutorizar(false);
											ordenAutorizacionDto.setPuedeAutorizar(false);
											ErrorMessage mensajeErrorGeneralPriori = new ErrorMessage(
													"errors.autorizacion.noExisteNivelesAutorMedicamentos", medicamento.getCodigo()+"");
											medicamento.setMensajeError(mensajeErrorGeneralPriori);
											autorizacionCapitacionDto.setVerificarDetalleError(true);
											
											if (esTodosRequeridos) {
												autorizacionCapitacionDto.setProcesoExitoso(false);
												ErrorMessage mensajeErrorGeneral = new ErrorMessage(
														"errors.autorizacion.noExisteNivelAutorizacionUsuario");
												autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
												autorizacionCapitacionDto.setVerificarDetalleError(true);
											}
										}
									}
								}
							}
						}
						if(generarAutorizacion&&!esTodosRequeridos){
							autorizacionCapitacionDto.setProcesoExitoso(true);
						}else{
							if(!generarAutorizacion){
								autorizacionCapitacionDto.setProcesoExitoso(false);
							}
						}

					} else {
					autorizacionCapitacionDto.setProcesoExitoso(false);
					ErrorMessage mensajeErrorGeneral = new ErrorMessage(
								"errors.autorizacion.noExisteNivelesAutorServicio");
						autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
						autorizacionCapitacionDto.setVerificarDetalleError(true);
					}
				} else {
					autorizacionCapitacionDto.setProcesoExitoso(false);
					autorizacionCapitacionDto.setVerificarDetalleError(true);
					this.adicionarMensajeServicioArticulo(autorizacionCapitacionDto);
				}
				
			} else {
				autorizacionCapitacionDto.setProcesoExitoso(false);
				autorizacionCapitacionDto.setVerificarDetalleError(true);
				this.adicionarMensajeServicioArticulo(autorizacionCapitacionDto);
			}
			
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}

		} catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		}
		catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
	}
	
	/**
	 * Metodo que permite adicionar a cada servicio a autorizar
	 * @param autorizacionCapitacionDto
	 */
	public void adicionarMensajeServicioArticulo (AutorizacionCapitacionDto autorizacionCapitacionDto){
		
		for (OrdenAutorizacionDto ordenesAutorizar : autorizacionCapitacionDto.getOrdenesAutorizar()) {
			if (ordenesAutorizar.getServiciosPorAutorizar()!=null &&
					!ordenesAutorizar.getServiciosPorAutorizar().isEmpty()){
				for (ServicioAutorizacionOrdenDto serviciosAutorizar : ordenesAutorizar.getServiciosPorAutorizar()) {
					ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorManuales",
							serviciosAutorizar.getCodigo()+"");
					serviciosAutorizar.setMensajeError(mensajeErrorGeneral);
					serviciosAutorizar.setAutorizado(false);
					serviciosAutorizar.setAutorizar(false);
					autorizacionCapitacionDto.setVerificarDetalleError(true);
					
				}
			}else{
				for (MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar : ordenesAutorizar.getMedicamentosInsumosPorAutorizar()) {
					ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorManuales",
							medicamentosAutorizar.getCodigo()+"");
					medicamentosAutorizar.setMensajeError(mensajeErrorGeneral);
					medicamentosAutorizar.setAutorizado(false);
					medicamentosAutorizar.setAutorizar(false);
					autorizacionCapitacionDto.setVerificarDetalleError(true);
				}
			}
		}
	}
	
	/**
	* Metodo de encargado de realizar la validacion para autorizaciones Capitación Subcontratada
	* Autorización Servicios Medicamentos de Ingreso Estancia 
	* En este no se tiene en cuenta la prioridad de los niveles de autorizacion DCU 1105
	* @author ginsotfu
	* @param autorizacionCapitacionDto
	* @param esTodosRequeridos
	* @param requiereTransaccion
	* @throws IPSException
	*
	*/
	public void validarNivelesAutorizacionAutorizacionesPoblacionCapitadaAutorServMedIngresoInstancia(
			AutorizacionCapitacionDto autorizacionCapitacionDto,
			boolean esTodosRequeridos,
			boolean requiereTransaccion)
			throws IPSException {
		

		List<NivelAutorizacionDto> listaNivelesAutorizacionUsuario = new ArrayList<NivelAutorizacionDto>();
		List<NivelAutorizacionDto> listaNivelesAutorizacionAutomatica = null;
		List<Integer> nivelesAutorizacionAutomatica = new ArrayList<Integer>();
		NivelAutorizacionDto nivelAutorizacionServicioMedicamento = null;
		boolean generarAutorizacion=false;
		autorizacionCapitacionDto.setProcesoExitoso(true);
		
		try {

			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}

			listaNivelesAutorizacionUsuario = this.consultarNivelesAutorizacionUsuario(
					autorizacionCapitacionDto.getLoginUsuario(), 
					autorizacionCapitacionDto.getCodigoPersonaUsuario(),
					ConstantesIntegridadDominio.acronimoTipoAutorizacionManual, true, requiereTransaccion);

			if (listaNivelesAutorizacionUsuario != null && 
					!listaNivelesAutorizacionUsuario.isEmpty()) {
				
				for (OrdenAutorizacionDto ordenAutorizacionDto : autorizacionCapitacionDto.getOrdenesAutorizar()) {
					
					listaNivelesAutorizacionAutomatica = new ArrayList<NivelAutorizacionDto>();
					/*2.SELECCION DE NIVELES DE AUTORIZACION MANUALES
						en la consultarNivelesAutorizacionUsuario se realiza la validación por tipo autorización
						y estado activo por lo cual en este punto solo se valida la vía de ingreso */
					for (NivelAutorizacionDto nivelAutorizacionDto : listaNivelesAutorizacionUsuario) {
						/*Si se cumple la validación de la vía de ingreso se realiza el punto
						 * 3. Niveles de Autorización para Servicios, Medicamentos o Insumos a Autorizar*/
						if(ordenAutorizacionDto.getCodigoViaIngreso() == 
								nivelAutorizacionDto.getViaIngreso().getCodigo() && 
								!listaNivelesAutorizacionAutomatica.contains(nivelAutorizacionDto)) {
							listaNivelesAutorizacionAutomatica.add(nivelAutorizacionDto);
						}
					}
				}

				if (!listaNivelesAutorizacionAutomatica.isEmpty()) {

					nivelesAutorizacionAutomatica = verificarNivelesAutorizacionOrdenes(
							autorizacionCapitacionDto.getOrdenesAutorizar(), 
							listaNivelesAutorizacionAutomatica, requiereTransaccion, autorizacionCapitacionDto);

					if (!nivelesAutorizacionAutomatica.isEmpty()) {
						for (OrdenAutorizacionDto ordenAutorizacionDto : autorizacionCapitacionDto.getOrdenesAutorizar()) {

							if(!ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()) {

								for (ServicioAutorizacionOrdenDto servicio : 
									ordenAutorizacionDto.getServiciosPorAutorizar()) {

									nivelAutorizacionServicioMedicamento = null;

									//MT 6046
									if (autorizacionCapitacionDto.getEntidadSubAutorizarCapitacion() != null) {
									//Se agrega if por MT 6046. Si el servicio tiene niveles de autorizacion
										if(servicio.getNivelesAutorizacion() != null){											
											for (NivelAutorizacionDto nivelServicio : servicio.getNivelesAutorizacion()) {

												nivelAutorizacionServicioMedicamento = nivelServicio;
												servicio.setNivelAutorizacion(nivelAutorizacionServicioMedicamento);
												servicio.setAutorizar(true);
												servicio.setPuedeAutorizar(true);
												generarAutorizacion = true;
												autorizacionCapitacionDto.setProcesoExitoso(true);

												if (esTodosRequeridos) {
													autorizacionCapitacionDto.setProcesoExitoso(false);
													ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.noExisteNivelAutorizacionUsuario");
													autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
													autorizacionCapitacionDto.setVerificarDetalleError(false);
												}

											}										
										} else {											
											//MT 6046: Si el servicio no tiene niveles de autorizacion											
											//Se agrega mensaje general
											//hermorhu - se quita para que no duplique el mensaje generado
//											autorizacionCapitacionDto.setMensajeErrorGeneral(new ErrorMessage("errors.autorizacion.messageGeneral"));
											//Se agrega mensaje por servicio
											servicio.setAutorizar(false);
											servicio.setAutorizado(false);
											servicio.setPuedeAutorizar(false);
											ErrorMessage mensajeErrorGeneralPriori = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorServicio",servicio.getCodigo()+"");
											servicio.setMensajeError(mensajeErrorGeneralPriori);
											autorizacionCapitacionDto.setVerificarDetalleError(true);
											
											if (esTodosRequeridos) {
												autorizacionCapitacionDto.setProcesoExitoso(false);
												ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.noExisteNivelAutorizacionUsuario");
												autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
												autorizacionCapitacionDto.setVerificarDetalleError(false);
											}											
											
										}
									} else{
										autorizacionCapitacionDto.setProcesoExitoso(false);
										ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.noExisteNivelAutorizacionUsuario");
										autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
										autorizacionCapitacionDto.setVerificarDetalleError(false);
									}

								}


							} else if (!ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar().isEmpty()) {

								for (MedicamentoInsumoAutorizacionOrdenDto medicamento : 
									ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()) {

									nivelAutorizacionServicioMedicamento = null;

									//hermorhu - MT5992
									//Se valida que para el articulo exista Niveles de Autorizacion
									if(medicamento.getNivelesAutorizacion() != null && !medicamento.getNivelesAutorizacion().isEmpty()) {
									for (NivelAutorizacionDto nivelMedicamento : medicamento.getNivelesAutorizacion()) {

											nivelAutorizacionServicioMedicamento = nivelMedicamento;
											medicamento.setNivelAutorizacion(nivelAutorizacionServicioMedicamento);
											medicamento.setAutorizar(true);
											medicamento.setPuedeAutorizar(true);
											generarAutorizacion=true;
											autorizacionCapitacionDto.setProcesoExitoso(true);
											
											
											if (esTodosRequeridos) {
												autorizacionCapitacionDto.setProcesoExitoso(false);
												ErrorMessage mensajeErrorGeneral = new ErrorMessage(
														"errors.autorizacion.noExisteNivelAutorizacionUsuario");
												autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
												autorizacionCapitacionDto.setVerificarDetalleError(true);
											}
									}
									} else {
										//hermorhu - MT5992
										//Si el articulo no tiene Niveles de Autorizacion		
										
										//Se agrega mensaje por servicio
										medicamento.setAutorizar(false);
										medicamento.setAutorizado(false);
										medicamento.setPuedeAutorizar(false);
										ErrorMessage mensajeErrorGeneralPriori = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorMedicamentos",medicamento.getCodigo()+"");
										medicamento.setMensajeError(mensajeErrorGeneralPriori);
										autorizacionCapitacionDto.setVerificarDetalleError(true);
										
										if (esTodosRequeridos) {
											autorizacionCapitacionDto.setProcesoExitoso(false);
											ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.noExisteNivelAutorizacionUsuario");
											autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);
											autorizacionCapitacionDto.setVerificarDetalleError(false);
										}	
										
									}
								}
							}
						}
						if(generarAutorizacion&&!esTodosRequeridos){
							autorizacionCapitacionDto.setProcesoExitoso(true);
						}else{
							if(!generarAutorizacion){
								autorizacionCapitacionDto.setProcesoExitoso(false);
							}
						}

					}
					else
					{
						autorizacionCapitacionDto.setProcesoExitoso(false);
						/*ErrorMessage mensajeErrorGeneral = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorServicio");
						autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);*/						
						//MT 6046
						if(autorizacionCapitacionDto.getOrdenesAutorizar() != null)
						{
							for (OrdenAutorizacionDto ordenAutorizacionDto : autorizacionCapitacionDto.getOrdenesAutorizar()) 
							{
								if(ordenAutorizacionDto.getServiciosPorAutorizar() != null && !ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()) 
								{
									for (ServicioAutorizacionOrdenDto servicio : ordenAutorizacionDto.getServiciosPorAutorizar()) 
									{
										//Se agrega mensaje por servicio
										servicio.setAutorizar(false);
										servicio.setAutorizado(false);
										servicio.setPuedeAutorizar(false);
										ErrorMessage mensajeErrorGeneralPriori = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorServicio",servicio.getCodigo()+"");
										servicio.setMensajeError(mensajeErrorGeneralPriori);
									}
								}
								//MT 6047
								else if(ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar() != null && !ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar().isEmpty())
								{
									for(MedicamentoInsumoAutorizacionOrdenDto medicamento : ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar())
									{
										//Se agrega mensaje por medicamento
										medicamento.setAutorizar(false);
										medicamento.setAutorizado(false);
										medicamento.setPuedeAutorizar(false);
										ErrorMessage mensajeErrorGeneralPriori = new ErrorMessage("errors.autorizacion.noExisteNivelesAutorServicio", medicamento.getCodigo()+"");
										medicamento.setMensajeError(mensajeErrorGeneralPriori);
									}
								}//FIN MT 6047
							}		
						}//Fin MT 6046	
						autorizacionCapitacionDto.setVerificarDetalleError(true);
					}
				}
				else
				{
					autorizacionCapitacionDto.setProcesoExitoso(false);
					autorizacionCapitacionDto.setVerificarDetalleError(true);
					this.adicionarMensajeServicioArticulo(autorizacionCapitacionDto);
				}
				
			}
			else
			{
				autorizacionCapitacionDto.setProcesoExitoso(false);
				autorizacionCapitacionDto.setVerificarDetalleError(true);
				this.adicionarMensajeServicioArticulo(autorizacionCapitacionDto);
			}
			
			if (requiereTransaccion)
			{
				HibernateUtil.endTransaction();
			}

		}
		catch (IPSException ipsme)
		{
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		}
		catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
	}
}