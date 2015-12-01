package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.bl.capitacion.impl.NivelAutorizacionMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.AutorizacionCapitacionMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.IngresoMundo;
import com.servinte.axioma.bl.manejoPaciente.impl.ProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IIngresoMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dao.impl.administracion.CentroAtencionHibernateDAO;
import com.servinte.axioma.dao.impl.administracion.CentroCostosDAO;
import com.servinte.axioma.dao.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadesConsultaHibernateDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroAtencionDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaDAO;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.DtoValidacionGeneracionAutorizacionCapitada;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IValidacionGeneracionAutorizacionCapitadaMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;

/**
 * Esta clase se encarga de definir los métodos para la 
 * validaci&oacute;n y generaci&oacute;n de la autorizaci&oacute;n de capitaci&oacute;n 
 * subcontratada para ordenes ambulatorias,
 * y peticiones Qx. DCU 966 
 * @author Diana Ruiz
 * @since 21/06/2011
 */
public class ValidacionGeneracionAutorizacionCapitadaMundo implements IValidacionGeneracionAutorizacionCapitadaMundo
{

	/**
	 * M&eacute;todo que permite validar y generar la autorizaci&oacute;n para la poblaci&oacute;n capitada.
	 * @param Connection con 
	 * @param DtoValidacionGeneracionAutorizacionCapitada dtoValidacionGeneracionAutorizacionCapitada.   
	 * @throws Exception 
	 */
	
	public List<AutorizacionCapitacionDto> generarValidacionGeneracionAutorizacionCapitada(List<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionGeneracionAutorizacionCapitada, UsuarioBasico usuario) throws Exception{
		Connection con=null;
		AutorizacionCapitacionDto generacionAutorizacion = null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		OrdenAutorizacionDto ordenAutorizar = null;
		ServicioAutorizacionOrdenDto servicioAutorizar = null;
		MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar = null;
		
		try {
			HibernateUtil.beginTransaction();
			con = UtilidadBD.abrirConexion();
			
			IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo = new ProcesoGeneracionAutorizacionMundo();
			IIngresoMundo ingresoMundo = new IngresoMundo();
			IAutorizacionCapitacionMundo autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
			IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();
			NivelAutorizacionMundo validarNivelAutorizacion = new NivelAutorizacionMundo();
			Convenios convenioResponsable = null;
			boolean continuarProceso = true;
			String viaIngresoParametro ="";
			String tipoPaciente = "";
			generacionAutorizacion = new AutorizacionCapitacionDto();
			listaAutorizacionCapitacion = new ArrayList<AutorizacionCapitacionDto>();
			boolean manejaPresupuesto=false;
			
			
			//Continuo con la validación verificando si el convenio es Capitado/Maneja capitación subcontratada y ademas cubre al menos 1 
			// Servicio/Articulo de la orden

			if (continuarProceso) {
				continuarProceso=false;
				convenioResponsable = convenioServicio.findById(listaValidacionGeneracionAutorizacionCapitada.get(0).getConvenio());
				if (convenioResponsable.getManejaPresupCapitacion()!=null){
					if (convenioResponsable.getManejaPresupCapitacion() == ConstantesBD.acronimoSiChar){
						manejaPresupuesto=true;
					}
				}
				if(convenioResponsable != null 
						&& convenioResponsable.getCapitacionSubcontratada() != null
						&& convenioResponsable.getTiposContrato().getCodigo()== ConstantesBD.codigoTipoContratoCapitado 
						&& convenioResponsable.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar)){
				
					for (DtoValidacionGeneracionAutorizacionCapitada validacionGeneracionAutorizacionCapitada : listaValidacionGeneracionAutorizacionCapitada) {
						//Validar si el convenio del paciente es capitado, maneja capitaci&oacute;n subcontratada 
							
							//Verifico si son servicios a autorizar
							if (validacionGeneracionAutorizacionCapitada.getServicios() != null &&
								!validacionGeneracionAutorizacionCapitada.getServicios().isEmpty()){ 
								//Verifico si algun servicio se encuentra cubierto
								for (DtoServicios servicio : validacionGeneracionAutorizacionCapitada.getServicios()) {
									if (servicio.getServicioCubierto().equals(ConstantesBD.acronimoSi)){
										validacionGeneracionAutorizacionCapitada.setOrdenAutorizada(true);
										continuarProceso=true;
									}
									else{
										validacionGeneracionAutorizacionCapitada.setOrdenAutorizada(false);
									}
								}
							}else { //Se autorizan Medicamentos e Insumos
								if (validacionGeneracionAutorizacionCapitada.getArticulos() != null &&
									!validacionGeneracionAutorizacionCapitada.getArticulos().isEmpty()){
									//Verifico si algun articulo se encuentra cubierto
									for (DtoArticulos articulo : validacionGeneracionAutorizacionCapitada.getArticulos()) {
										if (articulo.getArticuloCubierto().equals(ConstantesBD.acronimoSi)){
											validacionGeneracionAutorizacionCapitada.setOrdenAutorizada(true);
											continuarProceso=true;
										}
										else{
											validacionGeneracionAutorizacionCapitada.setOrdenAutorizada(false);
										}
									}
								}
							}
						}
					//Valido si algun Servicio/Articulo se encuentra cubierto
					if (!continuarProceso){
						ErrorMessage error = new ErrorMessage("errors.autorizacion.servArtiNoCubierto");
					generacionAutorizacion.setMensajeErrorGeneral(error);
					generacionAutorizacion.setProcesoExitoso(false);
					generacionAutorizacion.setVerificarDetalleError(false);
					listaAutorizacionCapitacion.add(generacionAutorizacion);
				}
				}else {//El convenio no es capitado o no maneja capitación subcontratada
					generacionAutorizacion.setProcesoExitoso(false);
					generacionAutorizacion.setVerificarDetalleError(false);
					listaAutorizacionCapitacion.add(generacionAutorizacion);
					continuarProceso=false;
				}
			}
			
			
			//Verifico si se encuentra definido el centro de atención asignado al paciente
			if (continuarProceso){
				int CentroAtencionAsignado = listaValidacionGeneracionAutorizacionCapitada.get(0).getPaciente().getCodigoCentroAtencionPYP();
				if(CentroAtencionAsignado!=ConstantesBD.codigoNuncaValido && CentroAtencionAsignado >0 ){
					continuarProceso=true;
				}else {
					continuarProceso=false;
					ErrorMessage error = new ErrorMessage("errors.autorizacion.noExisteCentroAtencionAsignado");
					generacionAutorizacion.setMensajeErrorGeneral(error);
					generacionAutorizacion.setProcesoExitoso(false);
					generacionAutorizacion.setVerificarDetalleError(false);
					listaAutorizacionCapitacion.add(generacionAutorizacion);
				}
			}
			
			/*
			 * Continuo con el proceso verificando si se encuentra definida la via de ingreso en el parametro general
			 * Vía Ingreso para validación de Nivel de Autorización Capitación
			 * Envio la información correspondiente al dto AutorizacionCapitacionDto
			 */
			if (continuarProceso){
				continuarProceso=false;
				//viaIngresoParametro = validarParametroViaIngresoNivelAutorizacion(listaValidacionGeneracionAutorizacionCapitada.get(0).getTipoOrden(), usuario.getCodigoInstitucionInt());
				if (listaValidacionGeneracionAutorizacionCapitada.get(0).getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria)){
					viaIngresoParametro = ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
					tipoPaciente = ValoresPorDefecto.getTipoPacienteValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
				}
				if (listaValidacionGeneracionAutorizacionCapitada.get(0).getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx)){
					viaIngresoParametro = ValoresPorDefecto.getViaIngresoValidacionesPeticiones(usuario.getCodigoInstitucionInt());
					tipoPaciente = ValoresPorDefecto.getTipoPacienteValidacionesPeticiones(usuario.getCodigoInstitucionInt());
				}

				if (!UtilidadTexto.isEmpty(viaIngresoParametro)&&
						!UtilidadTexto.isEmpty(tipoPaciente)){
					continuarProceso = true;
				}
				//Se envia la información correspondiente al dto de AutorizacionCapitacionDto
	 			//Se envia el encabezado de la autorización
				
	 			DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
	 			//Se envia el detalle de la autorización
	 			for (DtoValidacionGeneracionAutorizacionCapitada validacionGeneracionAutorizacionCapitada : listaValidacionGeneracionAutorizacionCapitada) {
	 				//Envio la información de la orden
	     			ordenAutorizar = new OrdenAutorizacionDto();
	     			if (continuarProceso){
	     				ordenAutorizar.setCodigoViaIngreso(Integer.parseInt(viaIngresoParametro));
	     			}
	     			ordenAutorizar.setEsPyp(validacionGeneracionAutorizacionCapitada.isPyp());
	     			ordenAutorizar.getContrato().getConvenio().setCodigo(validacionGeneracionAutorizacionCapitada.getConvenio());
	     			ordenAutorizar.getContrato().setCodigo(validacionGeneracionAutorizacionCapitada.getContratoConvenioResponsable());
	   				ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(true);
	   				ordenAutorizar.getContrato().getConvenio().setConvenioManejaPresupuesto(manejaPresupuesto);
	     			ordenAutorizar.setMigrado(ConstantesBD.acronimoNoChar);
	     			ordenAutorizar.setConsecutivoOrden(validacionGeneracionAutorizacionCapitada.getConsecutivoOrden());
	     			ordenAutorizar.setCodigoOrden((long)validacionGeneracionAutorizacionCapitada.getOrdenAmbulatoria());
	     			
	     			if (validacionGeneracionAutorizacionCapitada.getServicios() != null &&
	     					!validacionGeneracionAutorizacionCapitada.getServicios().isEmpty()){
	     				
	     				for (DtoServicios serviciosAutorizar : validacionGeneracionAutorizacionCapitada.getServicios()) {
	     					if (serviciosAutorizar.getServicioCubierto().equals(ConstantesBD.acronimoSi)){
		     					//Envio el servicio a autorizar
		             			servicioAutorizar = new ServicioAutorizacionOrdenDto();
		             			servicioAutorizar.setCodigo(serviciosAutorizar.getCodigoServicio());
		             			servicioAutorizar.setTipoMonto(serviciosAutorizar.getTipoMonto());
		             			if (serviciosAutorizar.isUrgente()){
		             				servicioAutorizar.setUrgente(ConstantesBD.acronimoSiChar);
		             				ordenAutorizar.setEsUrgente(true);
		             			}else{
		             				servicioAutorizar.setUrgente(ConstantesBD.acronimoNoChar);
		             				ordenAutorizar.setEsUrgente(false);
		             			}
		             			
		             			servicioAutorizar.setCantidad((long)serviciosAutorizar.getCantidad());
		             			servicioAutorizar.setAcronimoTipoServicio(serviciosAutorizar.getAcronimoTipoServicio());
		             			servicioAutorizar.setCodigoEspecialidad(serviciosAutorizar.getCodigoEspecialidad());
		             			servicioAutorizar.setCodigoGrupoServicio(serviciosAutorizar.getCodigoGrupoServicio());
	             			
	             				servicioAutorizar.setAutorizar(true);
	             				ordenAutorizar.getServiciosPorAutorizar().add(servicioAutorizar);
	             			}
	             			
	             			if (validacionGeneracionAutorizacionCapitada.getTipoOrden().equals(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx)){
	             				ordenAutorizar.setClaseOrden(ConstantesBD.claseOrdenPeticion);
		             			ordenAutorizar.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
	             			}else{
	             				ordenAutorizar.setClaseOrden(ConstantesBD.claseOrdenOrdenAmbulatoria);
		             			ordenAutorizar.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
	             			}
	             			ordenAutorizar.setCodigoCentroCostoEjecuta(validacionGeneracionAutorizacionCapitada.getCentroCostoResponde());
						}
	     				generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
	     				
	     			}else {//Son medicamentos
	     				for (DtoArticulos articulosAutorizar : validacionGeneracionAutorizacionCapitada.getArticulos()) {
	     					if (articulosAutorizar.getArticuloCubierto().equals(ConstantesBD.acronimoSi)){
								//Envio el medicamento a autorizar
		     					medicamentosAutorizar = new MedicamentoInsumoAutorizacionOrdenDto();
		     					medicamentosAutorizar.setCodigo(articulosAutorizar.getCodigoArticulo());
		     					medicamentosAutorizar.setCantidad((long)articulosAutorizar. getCantidadArticulo());
		     					
		     					medicamentosAutorizar.setDiasTratamiento(articulosAutorizar.getDiasTratamiento());
		     					medicamentosAutorizar.setDosis(articulosAutorizar.getDosis());
		     					medicamentosAutorizar.setDescripcion(articulosAutorizar.getDescripcionArticulo());
		     					
		     					medicamentosAutorizar.setClaseInventario(articulosAutorizar.getClaseArticulo());
		     					medicamentosAutorizar.setSubGrupoInventario(articulosAutorizar.getCodigoSubGrupoArticulo());
	     					
	     						medicamentosAutorizar.setAutorizar(true);
	     						ordenAutorizar.getMedicamentosInsumosPorAutorizar().add(medicamentosAutorizar);
	     					}
	     					
	     					ordenAutorizar.setClaseOrden(ConstantesBD.claseOrdenOrdenAmbulatoria);
	             			ordenAutorizar.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos);
						}
	     				generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
	     			}
	 			}
	 			
	 			//Envio los datos del paciente para la autorización de capita
	 			datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
	 			//Se obtiene la información de el tipo Afiliado, la clasificación Socioeconómica y Naturaleza del paciente 
	 			InfoSubCuentaDto dtoDatosSubCuenta=ingresoMundo.consultarInfoSubCuentaPorIngresoPorConvenio(listaValidacionGeneracionAutorizacionCapitada.get(0).getCodIngreso(),
	 					listaValidacionGeneracionAutorizacionCapitada.get(0).getConvenio(), false);
	 			datosPacienteAutorizar.setTipoPaciente(tipoPaciente);
	 			if(dtoDatosSubCuenta.getCodigoTipoAfiliado() != null){
	 				datosPacienteAutorizar.setTipoAfiliado(dtoDatosSubCuenta.getCodigoTipoAfiliado()+"");
	 			}
	 			if(dtoDatosSubCuenta.getCodigoEstratoSocial() != null){
	 				datosPacienteAutorizar.setClasificacionSocieconomica(dtoDatosSubCuenta.getCodigoEstratoSocial());
	 			}
	 			datosPacienteAutorizar.setCodConvenioCuenta(listaValidacionGeneracionAutorizacionCapitada.get(0).getPaciente().getCodigoConvenio());
	 			datosPacienteAutorizar.setCuenta(listaValidacionGeneracionAutorizacionCapitada.get(0).getPaciente().getCodigoCuenta());
	 			datosPacienteAutorizar.setNaturalezaPaciente(dtoDatosSubCuenta.getCodigoNaturaleza());
	 			datosPacienteAutorizar.setCodigoPaciente(listaValidacionGeneracionAutorizacionCapitada.get(0).getPaciente().getCodigoPersona());
	 			datosPacienteAutorizar.setCuentaAbierta(false);
	 			datosPacienteAutorizar.setCuentaManejaMontos(dtoDatosSubCuenta.isSubCuentaManejaMontos());
	 			datosPacienteAutorizar.setCentroAtencionAsignadoPaciente(listaValidacionGeneracionAutorizacionCapitada.get(0).getPaciente().getCodigoCentroAtencionPYP());
	 			
	 			//Envio la información correspondiente al dto del proceso de autorización
	 			//generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
	 			generacionAutorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
	 			generacionAutorizacion.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
	 			generacionAutorizacion.setLoginUsuario(usuario.getLoginUsuario());
	 			generacionAutorizacion.setCentroAtencion(usuario.getCodigoCentroAtencion());
	 			generacionAutorizacion.setCodigoPersonaUsuario(usuario.getCodigoPersona());
	 			generacionAutorizacion.setDatosPacienteAutorizar(datosPacienteAutorizar);
	 			
	 			//Si no esta definida la via de ingreso se genera un mensaje de error cancelando el proceso
	 			if (!continuarProceso){
	 				continuarProceso=false;
	     			generacionAutorizacion.setProcesoExitoso(false);
	     			generacionAutorizacion.setVerificarDetalleError(true);
	     			autorizacionCapitacionMundo.obtenerMensajesServiciosMedicamentosInsumosAutorizar(generacionAutorizacion, false);
	     			listaAutorizacionCapitacion.add(generacionAutorizacion);
	 			}
			}
			
			//Continuo con el proceso verificando si se encuentran definidos los consecutivos de capita y de entidad subcontratada
			if(continuarProceso){
				String consecutivoAutoEntSub = "";
	     		String consecutivoAutoCapiSub = "";
	   			//Verifico si se encuentra definido el consecutivo de Autorizaciones de Entidad Subcontratada 
	     		consecutivoAutoEntSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
							con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub, usuario.getCodigoInstitucionInt());
	     			
	     		//Verifico si se encuentra definido el consecutivo de capitación Subcontratada 
	     		consecutivoAutoCapiSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
							con, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuario.getCodigoInstitucionInt());
	     		if (!UtilidadTexto.isEmpty(consecutivoAutoEntSub) && !UtilidadTexto.isEmpty(consecutivoAutoCapiSub)) 
	     		{
	     			continuarProceso=true;
	     		} else {
	     			continuarProceso=false;
	     			generacionAutorizacion.setProcesoExitoso(false);
	     			generacionAutorizacion.setVerificarDetalleError(true);
	     			autorizacionCapitacionMundo.obtenerMensajesServiciosMedicamentosInsumosAutorizar(generacionAutorizacion, false);
	     			listaAutorizacionCapitacion.add(generacionAutorizacion);
	     		}
			}
			
			//Continuo con la validación buscando la entidad subcontratada
			if (continuarProceso){
				boolean informacionEncontrata=false;
				informacionEncontrata = busquedaParametrosServiciosArticulos(con, generacionAutorizacion, usuario);
				if (informacionEncontrata){
					continuarProceso = true;
					autorizacionCapitacionMundo.obtenerMensajesServiciosMedicamentosInsumosAutorizar(generacionAutorizacion, false);
					generacionAutorizacion.setProcesoExitoso(continuarProceso);
				} else {
					continuarProceso=false;
	     			generacionAutorizacion.setProcesoExitoso(false);
	     			generacionAutorizacion.setVerificarDetalleError(true);
	     			autorizacionCapitacionMundo.obtenerMensajesServiciosMedicamentosInsumosAutorizar(generacionAutorizacion, false);
	     			listaAutorizacionCapitacion.add(generacionAutorizacion);
				}
			}
				
			
			//Continuo con el proceso realizando llamado al proceso 1115
			if (continuarProceso){
				
				if (listaValidacionGeneracionAutorizacionCapitada.get(0).getArticulos() != null &&
						!listaValidacionGeneracionAutorizacionCapitada.get(0).getArticulos().isEmpty()){
					EntidadSubContratadaDto entidadSubcontratada = null;
					entidadSubcontratada = new EntidadSubContratadaDto();
					entidadSubcontratada = autorizacionCapitacionMundo.verificarEntidadSubContratadaParametrizada
							(String.valueOf(generacionAutorizacion.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()), false);

					if (entidadSubcontratada != null){
						generacionAutorizacion.setEntidadSubAutorizarCapitacion(entidadSubcontratada);
						//Se realiza la validación correspondiente al proceso 1115
		     			listaAutorizacionCapitacion = validarNivelAutorizacion.validarNivelAutorizacionParaAutorizacionAutomatica(generacionAutorizacion, false);
					}else {
						continuarProceso=false;
		     			generacionAutorizacion.setProcesoExitoso(false);
		     			generacionAutorizacion.setVerificarDetalleError(false);
		     			listaAutorizacionCapitacion.add(generacionAutorizacion);
					}
				}else {
					listaAutorizacionCapitacion = validarNivelAutorizacion.validarNivelAutorizacionParaAutorizacionAutomatica(generacionAutorizacion, false);
				}
			}
			
			//Continuo el proceso llamando al proceso 1106 para generar la autorización
			if (continuarProceso){
				if (listaAutorizacionCapitacion != null && !listaAutorizacionCapitacion.isEmpty()){
					//Itero la lista de ordenes agrupadas segun el proceso 1115 
					for (AutorizacionCapitacionDto autorizacionCapitacionDto : listaAutorizacionCapitacion) {
						//Verifico si las ordenes se pueden autorizar
						if (autorizacionCapitacionDto.isProcesoExitoso()){
							//Se hace el llamado al proceso 1106 
							procesoGeneracionAutorizacionMundo.generacionAutorizacion(autorizacionCapitacionDto, false);
						}else{
							for (OrdenAutorizacionDto ordenesAutorizar : autorizacionCapitacionDto.getOrdenesAutorizar()) {
								if (ordenesAutorizar.getServiciosPorAutorizar()!=null &&
										!ordenesAutorizar.getServiciosPorAutorizar().isEmpty()){
									for (ServicioAutorizacionOrdenDto serviciosAutorizar : ordenesAutorizar.getServiciosPorAutorizar()) {
										serviciosAutorizar.setAutorizado(false);
										serviciosAutorizar.setAutorizar(false);
									}
								}else{
									for (MedicamentoInsumoAutorizacionOrdenDto medicamentosInsuAutorizar : ordenesAutorizar.getMedicamentosInsumosPorAutorizar()) {
										medicamentosInsuAutorizar.setAutorizado(false);
										medicamentosInsuAutorizar.setAutorizar(false);
									}
								}
							} 
						}
						autorizacionCapitacionMundo.obtenerMensajesServiciosMedicamentosInsumosAutorizar(autorizacionCapitacionDto, false);
					} 
				}
			}
			
			HibernateUtil.endTransaction();
			
		} catch(IPSException ipse) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(ipse.getMessage(),ipse);
			throw ipse;
		} catch(Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return listaAutorizacionCapitacion;
		
 	 }	
   

   /**
	 * M&eacute;todo que permite realizar la b&uacute;squeda de la siguiente informaci&oacuet;n para los servicios o medicamentos e insumos:
	 * Entidad Subcontratada o
	 * Centros de Costo que responde y 
	 * Vía de Ingreso.  
	 * @param con
	 * @param dtoValidacionGeneracionAutorizacionCapitada
	 * @param usuario
	 */
public boolean busquedaParametrosServiciosArticulos (Connection con,AutorizacionCapitacionDto generacionAutorizacionCapita,UsuarioBasico usuario) {				
		
		boolean continuarProceso=false;
		try {
			ICentroAtencionDAO centroAtencion= new CentroAtencionHibernateDAO();
			IUnidadesConsultaDAO unidadConsulta= new UnidadesConsultaHibernateDAO();
			ICentroCostosDAO centroCostoDAO = new CentroCostosDAO();
			IServiciosMundo grupoTipoServicio= FacturacionFabricaMundo.crearServiciosMundo();
			List<DtoUnidadesConsulta> unidadesConsulta = new ArrayList<DtoUnidadesConsulta>();
			ArrayList<DtoCentroCosto> listaCentrosCosto = new ArrayList<DtoCentroCosto>();		
			DtoServicios servicioGrupoTipo = new DtoServicios();
			ArrayList<DtoCentroCosto> listaCentroCostoUnidadesConsulta = new ArrayList<DtoCentroCosto>();
			DtoCentroCosto centroCosto = new DtoCentroCosto();
			CentroAtencion centroAtencionEncontrado = null;
			boolean centroCostoEncontrado= false;
			int banderaCCosto = 0;
			int CentroAtencionAsignado = generacionAutorizacionCapita.getDatosPacienteAutorizar().getCentroAtencionAsignadoPaciente();
			
			//Se verifica si lo que se va ha autoriza es Medicamentos e Insumos o Servicios.
			if (generacionAutorizacionCapita.getOrdenesAutorizar().get(0).getMedicamentosInsumosPorAutorizar() != null &&
					!generacionAutorizacionCapita.getOrdenesAutorizar().get(0).getMedicamentosInsumosPorAutorizar().isEmpty()){

				//Se verifica si el paciente tiene información definida en el campo 'Centro de Atención Asignado'.
				if(CentroAtencionAsignado!=ConstantesBD.codigoNuncaValido && CentroAtencionAsignado >0 ){

					//Se verifica si para el centro de atención asignado al paciente en la parametrica centro de atención
					//se encuentra definido el campo 'Entidad Subcontratada de Farmacia'
					centroAtencionEncontrado = centroAtencion.findById(CentroAtencionAsignado);
					if (centroAtencionEncontrado.getEntidadesSubcontratadas() != null && 
							centroAtencionEncontrado.getEntidadesSubcontratadas().getCodigoPk()!= ConstantesBD.codigoNuncaValidoLong && 
							centroAtencionEncontrado.getEntidadesSubcontratadas().getCodigoPk() > 0){
						generacionAutorizacionCapita.getEntidadSubAutorizarCapitacion().setCodEntidadSubcontratada(centroAtencionEncontrado.getEntidadesSubcontratadas().getCodigoPk());
						continuarProceso = true;
						banderaCCosto++;
					}	else {
						continuarProceso = false;
						for (OrdenAutorizacionDto ordenesAutorizar : generacionAutorizacionCapita.getOrdenesAutorizar()) {
							ordenesAutorizar.setPuedeAutorizar(false);
							for (MedicamentoInsumoAutorizacionOrdenDto medicamentoAutorizar : ordenesAutorizar.getMedicamentosInsumosPorAutorizar()) {
								medicamentoAutorizar.setAutorizado(false);
								medicamentoAutorizar.setAutorizar(false);
								generacionAutorizacionCapita.setVerificarDetalleError(true);
							}
						}
					}
				}				
			} else { //Si son servicios
				for (OrdenAutorizacionDto ordenesAutorizar : generacionAutorizacionCapita.getOrdenesAutorizar()) {
					for (ServicioAutorizacionOrdenDto serviciosAutorizar : ordenesAutorizar.getServiciosPorAutorizar()) {
						//Busco para el servicio el tipo y grupo de servicio
						servicioGrupoTipo=grupoTipoServicio.obtenerTipoGrupoServicio(serviciosAutorizar.getCodigo());
						serviciosAutorizar.setAcronimoTipoServicio(servicioGrupoTipo.getAcronimoTipoServicio());
						serviciosAutorizar.setCodigoGrupoServicio(servicioGrupoTipo.getCodigoGrupoServicio());
						//MT 6029
						if(  servicioGrupoTipo.getTipoMonto()!=ConstantesBD.codigoNuncaValido){
						serviciosAutorizar.setTipoMonto(servicioGrupoTipo.getTipoMonto());
						}
						
						
						//Verificar si el servicio es de tipo consulta
						if (serviciosAutorizar.getAcronimoTipoServicio().charAt(0) == ConstantesBD.codigoServicioInterconsulta){
							//Verificar si el servicio solicitado se encuentra asociado a una unidad de Agenda

							unidadesConsulta = unidadConsulta.listaUnidadesConsulta(serviciosAutorizar.getCodigo());
							if (unidadesConsulta != null){					
								//Verificar en la parametrica 'Centros de Costo por Unidades de Agenda' si existe información para la unidad de consulta.
								for (DtoUnidadesConsulta unidadConsultas : unidadesConsulta){
									if (centroCostoEncontrado==false){
										DtoCentroCosto centroCostoUnidad= new DtoCentroCosto();
										//centroCostoUnidad.setCodServicio(serviciosAutorizar.getCodigo());
										centroCostoUnidad.setCodigoCentroAtencion(CentroAtencionAsignado);
										centroCostoUnidad.setCodigoUnidadConsulta(unidadConsultas.getCodigo());
										listaCentroCostoUnidadesConsulta = centroCostoDAO.listaCentroCostoUnidadConsulta(centroCostoUnidad);						
										if(serviciosAutorizar.isAutorizar() && listaCentroCostoUnidadesConsulta != null && listaCentroCostoUnidadesConsulta.size() == 1){
											centroCosto = listaCentroCostoUnidadesConsulta.get(0);	
											ordenesAutorizar.setCodigoCentroCostoEjecuta(centroCosto.getCodigoCentroCosto());
											continuarProceso = true;
											ordenesAutorizar.setPuedeAutorizar(false);
											serviciosAutorizar.setAutorizado(true);
											serviciosAutorizar.setAutorizar(true);
											banderaCCosto++;
										}else { //Existe mas de un centro de costo
											serviciosAutorizar.setAutorizado(false);
											serviciosAutorizar.setAutorizar(false);
											ordenesAutorizar.setPuedeAutorizar(false);
											generacionAutorizacionCapita.setVerificarDetalleError(true);
											break;
										}
									}
								}						
							}				
						}else{//Para los dem&aacute;s tipos de servicios
							//Verificar en la parametrica 'Centro Costo por Grupo Servicio'
							listaCentrosCosto= centroCostoDAO.listaCentroCostoGrupoServicio(serviciosAutorizar.getCodigoGrupoServicio(), CentroAtencionAsignado );
							//Si existe informaci&oacute;n para el servicio verificar que solo exista un centro de costo.					
							if(serviciosAutorizar.isAutorizar() && listaCentrosCosto != null && listaCentrosCosto.size() == 1){
								centroCosto = listaCentrosCosto.get(0);					
								ordenesAutorizar.setCodigoCentroCostoEjecuta(centroCosto.getCodigoCentroCosto());
								continuarProceso = true;
								ordenesAutorizar.setPuedeAutorizar(true);
								serviciosAutorizar.setAutorizado(true);
								serviciosAutorizar.setAutorizar(true);
								banderaCCosto++;
							}else {
								serviciosAutorizar.setAutorizado(false);
								serviciosAutorizar.setAutorizar(false);
								ordenesAutorizar.setPuedeAutorizar(false);
								generacionAutorizacionCapita.setVerificarDetalleError(true);
							}
						}
					}
				}
			}
			
			//Verifico si al menos un servicio fue correcto
			if (banderaCCosto > 0 ){
				continuarProceso = true;
			}else {
				continuarProceso = false;
			}
			
		} catch(Exception e) {
			Log4JManager.error("Validaciones Generacion Autorizacion Capitada: " + e);
		}
		return continuarProceso;
	}// Fin método
		
	
	/**
	 * Método que se encarga de validar la descripción del servicio/art&iacute;culo pendiente por autorizar si fall&oacute; en :
	 * El proceso 966, mostrar SERVICIO + dias tramite del grupo de servicio.
	 *   
	 *  @author Camilo Gómez
	 */	
	
	@SuppressWarnings("unused")
	private String validacionImpresionDescripcionServicio(int solicitud,int servicio,HttpServletRequest request, boolean procesoGeneracion)
	{		
		DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizacionCapitacionOrdenAmbulatoria=new DtoAutorizacionCapitacionOrdenAmbulatoria();
		try {
			//Paso la validacion de contrato=Capitado y capitacionSubcontratada='S' entonces 
			//aplica para autorizacion Capita Orden Ambulatoria
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			IAutorizacionCapitacionOrdenesAmbulatoriasMundo autorizacionCapitacionOrdenesAmbulatoriasMundo=CapitacionFabricaMundo.crearAutorizacionCapitacionOrdenesAmbulatoriasMundo();
			dtoAutorizacionCapitacionOrdenAmbulatoria.setCodigoServicioAutorizar(servicio);//solicitudGenerada.getCodigoServicioCitaAutorizar());
			dtoAutorizacionCapitacionOrdenAmbulatoria.setNumeroSolicitudAutorizar(solicitud);//solicitudGenerada.getNumeroSolicitudCitaAutorizar());
			dtoAutorizacionCapitacionOrdenAmbulatoria.setOrdenAmbulatoria(true);
			if(procesoGeneracion)
				dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(true);//indica si viene desde las validaciones de -Generacion Autorizacion- se debe agregar a la descripcion, el ACRONIMO
			else
				dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(false);//indica si viene desde las validaciones de -Consecutivos y Nivel- se debe agregar a la descripcion, los DIAS

			autorizacionCapitacionOrdenesAmbulatoriasMundo.validarDescripcionServicio(dtoAutorizacionCapitacionOrdenAmbulatoria, ins);
		} catch(Exception e) {
			Log4JManager.error("Validaciones Generacion Autorizacion Capitada: " + e);
		}
		return dtoAutorizacionCapitacionOrdenAmbulatoria.getNombreServicioAutorizar();
	}	
	
	/**
	 * Método que valida el parametro general de via de ingreso de nivel de autorizacion para Ordenes Ambulatorias y Peticion 
	 * @param tipoOrden
	 * @param institucion
	 * @return viaIngresoParametro
	 */
	private String validarParametroViaIngresoNivelAutorizacion(String tipoOrden,int institucion)
	{
		String viaIngresoParametro=null;
		if(tipoOrden.equals(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria)){
			viaIngresoParametro=ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatorias(institucion);
		}else if(tipoOrden.equals(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx)){
			viaIngresoParametro=ValoresPorDefecto.getViaIngresoValidacionesPeticiones(institucion);
		}
		return viaIngresoParametro;
	}
	
}// Fin clase
