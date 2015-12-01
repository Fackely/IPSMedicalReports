package com.servinte.axioma.bl.manejoPaciente.impl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;
import util.facturacion.InfoTarifaYExcepcion;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.princetonsa.dto.cargos.DTOCalculoTarifaServicioArticulo;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.inventario.DTOEsqTarInventarioContrato;
import com.princetonsa.dto.inventario.DTOEsqTarProcedimientoContrato;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.princetonsa.dto.inventario.DtoClaseInventario;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.servinte.axioma.bl.facturacion.impl.CatalogoFacturacionMundo;
import com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.bl.ordenes.impl.OrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.impl.SolicitudesMundo;
import com.servinte.axioma.bl.ordenes.interfaz.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.bl.ordenes.interfaz.ISolicitudesMundo;
import com.servinte.axioma.bl.salasCirugia.impl.PeticionesMundo;
import com.servinte.axioma.bl.salasCirugia.interfaz.IPeticionesMundo;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarInventariosContratoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarProcedimientoContratoDAO;
import com.servinte.axioma.delegate.manejoPaciente.AutorizacionesDelegate;
import com.servinte.axioma.dto.capitacion.DtoValidacionPresupuesto;
import com.servinte.axioma.dto.facturacion.BusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValidacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.AutoCapiXCentroCosto;
import com.servinte.axioma.orm.AutoCapiXCentroCostoId;
import com.servinte.axioma.orm.AutoEntsubOrdenambula;
import com.servinte.axioma.orm.AutoEntsubPeticiones;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubMontos;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.AutorizacionesEstanciaCapita;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.CierreTempClaseInvArt;
import com.servinte.axioma.orm.CierreTempGrupoServicio;
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;
import com.servinte.axioma.orm.CierreTempServArt;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.FinalidadesServicio;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSub;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.OrdenesAmbulatorias;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.PeticionQx;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.orm.delegate.facturacion.EntidadesSubcontratadasDelegate;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempGrupoServicioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteGruServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempServArtServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;

/**
 * Clase que implementa métodos correpondientes a la generación de la autorización automática y manual de 
 * capitación subcontratada
 *  
 * @author DiaRuiPe
 * @version 1.0
 * @created 03-jul-2012 03:34:37 p.m.
 */
public class ProcesoGeneracionAutorizacionMundo implements IProcesoGeneracionAutorizacionMundo {
	
	public ProcesoGeneracionAutorizacionMundo() {}
	
	private static final int DIASVIGENCIA=30;
	
	/**
	 * Método que permite realizar las validaciones necesarias para la generación de la autorización capitación subcontratada
	 * definido en el DCU 1106. 
	 * @param connection
	 * @param autorizacionEntSubCapita
	 */
	@Override
	public AutorizacionCapitacionDto generacionAutorizacion(AutorizacionCapitacionDto autorizacionEntSubCapita, boolean requiereTransaccion) throws IPSException{
		
		boolean continuarProcesoAutorizacion=true;
		Connection con=null; 	
		
		try{
		
			if (requiereTransaccion){
			HibernateUtil.beginTransaction();
			}
			
			con = UtilidadBD.abrirConexion();	
			Log4JManager.info("COMIENZA PROCESO PARA AUTORIZACIÓN DE CAPITA");
			//Si la autorización es manual se verifica que el parametro fecha inicio cierre orden medica se encuentre 
			//definido 
			String fechaInicioCierreOrdenMedica="";
			if (autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
				fechaInicioCierreOrdenMedica = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(autorizacionEntSubCapita.getCodigoInstitucion());
				if(UtilidadTexto.isEmpty(fechaInicioCierreOrdenMedica)){
					//Si el parametro no se encuentra definido no se puede generar autorización
					continuarProcesoAutorizacion=false;
					autorizacionEntSubCapita.setProcesoExitoso(false);
					ErrorMessage error = new ErrorMessage("errors.autorizacion.parametroFechaCierreOrdenMedica");
					autorizacionEntSubCapita.setMensajeErrorGeneral(error);
					Log4JManager.info("EL PARAMETRO FECHA INICIO ORDEN MEDICA NO SE ENCUENTRA DEFINIDO. PROCESO DE AUTORIZACIÓN CANCELADO");
				}
			}
			
			//Para las autorizaciones automaticas verifico si existe montos de cobro
			
			if (autorizacionEntSubCapita.getDatosPacienteAutorizar().isCuentaManejaMontos() && continuarProcesoAutorizacion){
				if (autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica)){
					if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria ||
					autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getClaseOrden() == ConstantesBD.claseOrdenPeticion){
						//Realizo la búsqueda del monto de cobro para la orden a autorizar
						buscarMontoCobro(autorizacionEntSubCapita,requiereTransaccion);
						continuarProcesoAutorizacion= autorizacionEntSubCapita.isProcesoExitoso();
					}
				}
			}		
		
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual calculo la tarifa para los Servicios/MedicamentosInsumos a Autorizar
			if (continuarProcesoAutorizacion){ 
				//Valido que la entidad subcontratada sea diferente de otra y que el tipo de tarifa de la entidad subcontratada es Convenio Paciente
				if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaConvenioPaciente)){
					//Calculo la tarifa para el Servicio/MedicamentoInsumo
					calcularTarifaArticuloServicioConvenio(autorizacionEntSubCapita, con);
					continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
				} 
				
				//Valido que la entidad subcontratada sea diferente de otra y que el tipo de tarifa de la entidad subcontratada es propia
				if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia)){
					//Calculo la tarifa para el Servicio/MedicamentoInsumo
					calcularTarifaArticuloServicioEntSub(con, autorizacionEntSubCapita);						
					continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
				}
			}
			
			
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo la validación de presupuesto para la autorización automatica
			//Valido que la entidad subcontratada sea diferente de otra
			if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getConvenio().isConvenioManejaPresupuesto()){
				if (continuarProcesoAutorizacion &&
						autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica) &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0){
					//Obtengo el valor del parametro general ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapitaAutomatica
					String disponibleSaldoPresupCapita= ValoresPorDefecto.getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(autorizacionEntSubCapita.getCodigoInstitucion());
					//Si el parametro es diferente de NO se realiza el proceso de validación de presupuesto DCU1180
					if (disponibleSaldoPresupCapita.isEmpty() ||
							disponibleSaldoPresupCapita != ConstantesBD.acronimoNo){
						validarPresupuestoCapitacion(autorizacionEntSubCapita);
						continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
					}				
				}
				
				//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo la validación de presupuesto para la autorización manual	
				//Valido que la entidad subcontratada sea diferente de otra
				if (continuarProcesoAutorizacion &&
						autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual) &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0){
					//Si la fecha Actual es menor a la fecha Inicio Cierre Ordenes Médicas
					//no se valida el presupuesto
					Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
					Date fechaInicioCOM = UtilidadFecha.conversionFormatoFechaStringDate(fechaInicioCierreOrdenMedica);
					if(fechaActual.after(fechaInicioCOM)){
						//Si la fecha del sistema es mayor a la fecha del parametro Inicio Cierre Ordenes Médicas se realiza el proceso de validación de presupuesto
						validarPresupuestoCapitacion(autorizacionEntSubCapita);
						continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
					}
				}
			}
			
			//Verifico si la entidad subcontratada es Otra caso en el cual la autorización es temporal
			if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() <= 0){
				for (OrdenAutorizacionDto odenesAutorizar : autorizacionEntSubCapita.getOrdenesAutorizar()) {
					autorizacionEntSubCapita.setProcesoExitoso(true);
					//Verifico si se autorizan servicios
					if (odenesAutorizar.getServiciosPorAutorizar() !=null &&
							!odenesAutorizar.getServiciosPorAutorizar().isEmpty()){
						for (ServicioAutorizacionOrdenDto serviciosAutorizar: odenesAutorizar.getServiciosPorAutorizar()) {
							serviciosAutorizar.setAutorizado(true);
						}
					}else {
						for (MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar: odenesAutorizar.getMedicamentosInsumosPorAutorizar()) {
							medicamentosAutorizar.setAutorizado(true);
						}
					}
				} 
			}
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo el calculo del valor del monto para la autorización, solo para servicios
			//Medicamentos e insumos diferentes a ingreso estancia MT 5337.
			//Valido que este proceso solo se ejecute para autorizaciones que no sean temporales MT 5181.
			
			
				if (continuarProcesoAutorizacion && 
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&					
						!autorizacionEntSubCapita.isAutoServArtIngresoEstancia()){
					calcularMontoCobro(autorizacionEntSubCapita);
					continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
				}
			
					
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual se calcula la fecha de vencimiento y se genera la autorización
			if (continuarProcesoAutorizacion){
				//Calculo la fecha de vencimiento para las autorizaciones automáticas
				if (autorizacionEntSubCapita.getTipoAutorizacion() == ConstantesIntegridadDominio.acronimoAutomatica){
					Date fechaVencimiento = calcularFechaVencimientoAutorAutomaticas(autorizacionEntSubCapita.getOrdenesAutorizar(), autorizacionEntSubCapita.getCodigoInstitucion());
					autorizacionEntSubCapita.setFechaVencimientoAutorizacion(fechaVencimiento);
				}
				//Genero la autorización
				guardarProcesoAutorizacion(con, autorizacionEntSubCapita);
			
				//Valido si la entidad subcontratada es diferente de otra 
				//MT 5485
				if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0){
					this.generarAcumuladoCierreTemporal(autorizacionEntSubCapita);
				}
			}
		
			//Validar si la autorización es la primera del día
			eliminarAcumuladoCierreTemporal(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getCodigo());
			if (requiereTransaccion){
				HibernateUtil.endTransaction();
			}			
			
			//TODO falta validar cuando se cancela la autorización se debe anular el consecutivo utilizado
			return autorizacionEntSubCapita;
		}
		catch (IPSException ipse) {			
			HibernateUtil.abortTransaction();
			Log4JManager.error(ipse.getMessage(),ipse);	
			throw ipse;			
		} 
		catch (Exception e){
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);			
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IProcesoGeneracionAutorizacionMundo#generarAutorizacionServicioTemporal(com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto, boolean)
	 */
	public void generarAutorizacionServicioTemporal(AutorizacionCapitacionDto autorizacionEntSubCapita, boolean requiereTransaccion) throws IPSException{
		
		Connection con =null;
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			con = UtilidadBD.abrirConexion();
			
			//Si la autorización es manual se verifica que el parametro fecha inicio cierre orden medica se encuentre 
			//definido 
			String fechaInicioCierreOrdenMedica="";
			if (autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
				fechaInicioCierreOrdenMedica = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(autorizacionEntSubCapita.getCodigoInstitucion());
				if(UtilidadTexto.isEmpty(fechaInicioCierreOrdenMedica)){
					//Si el parametro no se encuentra definido no se puede generar autorización
					autorizacionEntSubCapita.setProcesoExitoso(false);
					ErrorMessage error = new ErrorMessage("errors.autorizacion.parametroFechaCierreOrdenMedica");
					autorizacionEntSubCapita.setMensajeErrorGeneral(error);
					Log4JManager.info("EL PARAMETRO FECHA INICIO ORDEN MEDICA NO SE ENCUENTRA DEFINIDO. PROCESO DE AUTORIZACIÓN CANCELADO");
				}
			}
			if(autorizacionEntSubCapita.isProcesoExitoso()){
				if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaConvenioPaciente)){
					calcularTarifaArticuloServicioConvenio(autorizacionEntSubCapita, con);
				}else{
					if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
							autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&
							autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia)){
						calcularTarifaArticuloServicioEntSub(con, autorizacionEntSubCapita);
					}
				}
			}
			//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo la validación de presupuesto para la autorización automatica
			//Valido que la entidad subcontratada sea diferente de otra
			if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getConvenio().isConvenioManejaPresupuesto()){
				if(autorizacionEntSubCapita.isProcesoExitoso()){
					if (autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica) &&
							autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
							autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0){
						//Obtengo el valor del parametro general ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapitaAutomatica
						String disponibleSaldoPresupCapita= ValoresPorDefecto.getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(autorizacionEntSubCapita.getCodigoInstitucion());
						//Si el parametro es diferente de NO se realiza el proceso de validación de presupuesto DCU1180
						if (disponibleSaldoPresupCapita.isEmpty() ||
								disponibleSaldoPresupCapita != ConstantesBD.acronimoNo){
							validarPresupuestoCapitacion(autorizacionEntSubCapita);
						}
					}else{
						//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo la validación de presupuesto para la autorización manual	
						//Valido que la entidad subcontratada sea diferente de otra
						if (autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual) &&
								autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0){
							//Si la fecha Actual es menor a la fecha Inicio Cierre Ordenes Médicas
							//no se valida el presupuesto
							Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
							Date fechaInicioCOM = UtilidadFecha.conversionFormatoFechaStringDate(fechaInicioCierreOrdenMedica);
							if(fechaActual.after(fechaInicioCOM)){
								//Si la fecha del sistema es mayor a la fecha del parametro Inicio Cierre Ordenes Médicas se realiza el proceso de validación de presupuesto
								validarPresupuestoCapitacion(autorizacionEntSubCapita);
							}
						}
					}
				}
			}
			//Verifico si la entidad subcontratada es Otra caso en el cual la autorización es temporal
			if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() <= 0){
				for (OrdenAutorizacionDto odenesAutorizar : autorizacionEntSubCapita.getOrdenesAutorizar()) {
					//Verifico si se autorizan servicios
					if (odenesAutorizar.getServiciosPorAutorizar() !=null &&
							!odenesAutorizar.getServiciosPorAutorizar().isEmpty()){
						for (ServicioAutorizacionOrdenDto serviciosAutorizar: odenesAutorizar.getServiciosPorAutorizar()) {
							serviciosAutorizar.setAutorizado(true);
						}
					}else {
						for (MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar: odenesAutorizar.getMedicamentosInsumosPorAutorizar()) {
							medicamentosAutorizar.setAutorizado(true);
						}
					}
				} 
			}
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo el calculo del valor del monto para la autorización, solo para servicios
			//Medicamentos e insumos diferentes a ingreso estancia MT 5337.
			//Valido que este proceso solo se ejecute para autorizaciones que no sean temporales MT 5181.
			if (autorizacionEntSubCapita.isProcesoExitoso() && 
					autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&					
					!autorizacionEntSubCapita.isAutoServArtIngresoEstancia()){
				calcularMontoCobro(autorizacionEntSubCapita);
			}
			
			//Validar si la autorización es la primera del día
			if(autorizacionEntSubCapita.isProcesoExitoso()){
				eliminarAcumuladoCierreTemporal(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getCodigo());
			}
			
		}catch (IPSException ipse) {			
			HibernateUtil.abortTransaction();
			Log4JManager.error(ipse.getMessage(),ipse);	
			throw ipse;			
		} 
		catch (Exception e){
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);			
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
			UtilidadBD.closeConnection(con);
		}
	}
	
	/**
	 * Método que permite validar y generar la autorización de entidad subcontratada generada por la capita
	 * @param autorizacionEntSubCapita
	 * @throws IPSException 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void generarProcesoAutorizacionEstanciaAutomaticaCargosPendientes(AutorizacionCapitacionDto autorizacionEntSubCapita,
			EstanciaAutomatica estanciaAutomatica, HashMap mapaCuentas, UsuarioBasico usuario, int servicio, int pos, boolean esEstancia, Cargos cargos) throws IPSException{
		
		Log4JManager.info("COMIENZA PROCESO PARA AUTORIZACIÓN DE CAPITA");
		boolean continuarProcesoAutorizacion=true;
		Connection con=null; 	
		
		try{
			HibernateUtil.beginTransaction();
			con = UtilidadBD.abrirConexion();	
			ManejoPacienteFacade procesoGenerarEstancia = new ManejoPacienteFacade();
			
			//Para las ordenes de tipo estancia automatica se obtiene la entidad subcontratada y vía de ingreso
			//para la generación de la autorización.
			obtenerEntiSubViaIngreso(con, autorizacionEntSubCapita);
			continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual calculo la tarifa para los Servicios/MedicamentosInsumos a Autorizar
			if (continuarProcesoAutorizacion){ 
				//Valido que la entidad subcontratada sea diferente de otra y que el tipo de tarifa de la entidad subcontratada es Convenio Paciente
				if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaConvenioPaciente)){
					//Calculo la tarifa para el Servicio/MedicamentoInsumo
					calcularTarifaArticuloServicioConvenio(autorizacionEntSubCapita, con);
					continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
				} 
				
				//Valido que la entidad subcontratada sea diferente de otra y que el tipo de tarifa de la entidad subcontratada es propia
				if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0 &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getTipoTarifa().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia)){
					//Calculo la tarifa para el Servicio/MedicamentoInsumo
					calcularTarifaArticuloServicioEntSub(con, autorizacionEntSubCapita);						
					continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
				}
			}
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo la validación de presupuesto para la autorización automatica
			//Valido que la entidad subcontratada sea diferente de otra
			if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getConvenio().isConvenioManejaPresupuesto()){
				if (continuarProcesoAutorizacion &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion() != null &&
						autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0){
					//Obtengo el valor del parametro general ValidarDisponibilidadSaldoPresupuestoAutorizacionesCapitaAutomatica
					String disponibleSaldoPresupCapita= ValoresPorDefecto.getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(autorizacionEntSubCapita.getCodigoInstitucion());
					//Si el parametro es diferente de NO se realiza el proceso de validación de presupuesto DCU1180
					if (disponibleSaldoPresupCapita.isEmpty() ||
							disponibleSaldoPresupCapita != ConstantesBD.acronimoNo){
						validarPresupuestoCapitacion(autorizacionEntSubCapita);
						continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
					}				
				}
			}

			//Verifico si puedo continuar con el proceso de autorización caso en el cual realizo el calculo del valor del monto para la autorización.
			if (continuarProcesoAutorizacion){
				calcularMontoCobro(autorizacionEntSubCapita);
				continuarProcesoAutorizacion=autorizacionEntSubCapita.isProcesoExitoso();
			}
			
			//Verifico si puedo continuar con el proceso de autorización caso en el cual se calcula la fecha de vencimiento y se genera la autorización
			if (continuarProcesoAutorizacion){
				//Calculo la fecha de vencimiento para las autorizaciones automáticas
				if (autorizacionEntSubCapita.getTipoAutorizacion() == ConstantesIntegridadDominio.acronimoAutomatica){
					Date fechaVencimiento = calcularFechaVencimientoAutorAutomaticas(autorizacionEntSubCapita.getOrdenesAutorizar(), autorizacionEntSubCapita.getCodigoInstitucion());
					autorizacionEntSubCapita.setFechaVencimientoAutorizacion(fechaVencimiento);
				}
			}
			
			if (cargos!=null && !continuarProcesoAutorizacion){
				cargos.getInfoErroresCargo().setMensajesErrorDetalle("errors.autorizacion.noGeneroCargoPendientePorConsecutivos");
			}
			
			//Verifico si las validaciones son exitosas, en este caso genero la solicitud y la autorización
			if (continuarProcesoAutorizacion){
				//Verifico si es una estancia automatica
				if (esEstancia){
					//Genero la solicitud y los cargos de estancia automatica
					int resp = procesoGenerarEstancia.generarEstanciaAutomatica(con, estanciaAutomatica, pos, mapaCuentas, usuario, servicio);
					
					//Valido si se genero la solicitud y los cargos de estancia automatica
					if(resp > 0){
						UtilidadBD.finalizarTransaccion(con);
						estanciaAutomatica.setGeneroEstancia(true);
						//Genero la autorización
						con = UtilidadBD.abrirConexion();
						Long numeroSolicitud = (long) estanciaAutomatica.getNumeroSolicitud();
						autorizacionEntSubCapita.getOrdenesAutorizar().get(0).setCodigoOrden(numeroSolicitud);
						guardarProcesoAutorizacion(con, autorizacionEntSubCapita);
					}else {
						UtilidadBD.abortarTransaccion(con);
					}
				} else { //Si no es estancia automatica es generación de cargos pendientes
					cargos.recalcularCargoServicio(con, cargos.getDtoDetalleCargo().getNumeroSolicitud(), usuario, ConstantesBD.codigoNuncaValido, 
							cargos.getDtoDetalleCargo().getObservaciones(), cargos.getDtoDetalleCargo().getCodigoServicio(), cargos.getDtoDetalleCargo().getCodigoSubcuenta(), ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/,
							false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, ""/*esPortatil*/,Cargos.obtenerFechaCalculoCargo(con, cargos.getDtoDetalleCargo().getNumeroSolicitud()));
					//Verfico si la generación del cargo no tuvo errores
					if(!cargos.getInfoErroresCargo().getTieneErrores()){
						//Guardo la autorización de entidad subcontratada generada por la capita
						guardarProcesoAutorizacion(con, autorizacionEntSubCapita);
					}
				}
				
			}

			//Validar si la autorización es la primera del día
			eliminarAcumuladoCierreTemporal(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getCodigo());
			this.generarAcumuladoCierreTemporal(autorizacionEntSubCapita);
			HibernateUtil.endTransaction();
			
		}catch (IPSException ipse) {
			HibernateUtil.abortTransaction();
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(ipse.getMessage(),ipse);	
			throw ipse;	
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);			
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Método que pemite obtener la entidada subcontratada y vía de ingreso para las ordenes de estancia automatica y 
	 * valoraciones cuando la generación de la autorización es automática.
	 * @param listaOrdenesAutorizar 
	 * @param codIngreso
	 * @param tipoPaciente
	 * @param viaIngreso
	 * @throws Exception 
	 */
	private void obtenerEntiSubViaIngreso(Connection con, AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException{
		
		/*Para este método se utilizaran los siguientes datos del DTO AutorizacionCapitacionDto:
		List<OrdenAutorizacionDto> listaOrdenesAutorizar, int codIngreso, String tipoPaciente, int viaIngreso, 
		int codInstitucion, int naturalezaPaciente 
		 */
		
		try{
			int codigoCentroCostoEjecuta = ConstantesBD.codigoNuncaValido;
			String tipoEntidadEjecuta="";
			boolean continuarProcesoAutorizacion=true;
			long codigoContratoEntSub;	
			EntidadesSubcontratadasDelegate entSubDele=null;
			List<DtoEntidadSubcontratada> listaEntidadesSub=null;
			DtoEntidadSubcontratada entidadSubDto= null;
			EntidadSubContratadaDto entidadSubAutorizar=null;
			boolean aplicaCobertura=false;
			DtoContratoEntidadSub contratoEntSub = null;
			List<MedicamentoInsumoAutorizacionOrdenDto> medicamentosInsumosCubierto=null; 
			entSubDele= new EntidadesSubcontratadasDelegate();
			contratoEntSub = new DtoContratoEntidadSub();
			
			//Recorro la lista de ordenes autorizar para traer el centro de costo que responde, tipo de entidad que ejecuta y los Servicios/MedicamentosInsumos a autorizar		
			for (OrdenAutorizacionDto ordenAutorizacion : autorizacionEntSubCapita.getOrdenesAutorizar()) {
				
				codigoCentroCostoEjecuta=ordenAutorizacion.getCodigoCentroCostoEjecuta();
				tipoEntidadEjecuta= ordenAutorizacion.getTipoEntidadEjecuta();
							
				//Valido si la entidad que ejecuta es interna o externa
				if(!UtilidadTexto.isEmpty(tipoEntidadEjecuta) && 
						tipoEntidadEjecuta.equals(ConstantesIntegridadDominio.acronimoInterna)){
					
					//Obtengo al entidad subcontratada para centros de costo internos
					String entidadSubCentCostInterno=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(autorizacionEntSubCapita.getCodigoInstitucion());
					
					//Verifico si la entidad subcontratada se encuentra definida
					if(!UtilidadTexto.isEmpty(entidadSubCentCostInterno)){
						
						String[] entidad=entidadSubCentCostInterno.split("-");					
						EntidadesSubcontratadas entidadSubInterno=entSubDele.findById(Long.parseLong(entidad[0].trim()));
						
						//Verifico si la entidad subcontratada se encuentra activa y con un contrato vigente
						if(UtilidadTexto.getBoolean(entidadSubInterno.getActivo())){
							contratoEntSub = CargosEntidadesSubcontratadas.obtenerContratoVigenteEntidadSubcontratada(con, entidadSubInterno.getCodigoPk()+"", UtilidadFecha.getFechaActual());
							//Valido que el contrato se encuentre vigente
							if(contratoEntSub != null){
								codigoContratoEntSub=Utilidades.convertirALong(contratoEntSub.getConsecutivo());
								listaEntidadesSub=new ArrayList<DtoEntidadSubcontratada>();
								entidadSubDto=new DtoEntidadSubcontratada();
								entidadSubDto.setCodigoPk(entidadSubInterno.getCodigoPk());
								entidadSubDto.setContratoEntSub(codigoContratoEntSub);	
								entidadSubDto.setTipotarifa(contratoEntSub.getTipoTarifa());
								listaEntidadesSub.add(entidadSubDto);
							} else { //Sin contrato Vigente
								ErrorMessage error = new ErrorMessage("errors.autorizacion.parametroEntidadCentroCostoInternoSinContrato");
								autorizacionEntSubCapita.setMensajeErrorGeneral(error);
								continuarProcesoAutorizacion=false;
								autorizacionEntSubCapita.setProcesoExitoso(continuarProcesoAutorizacion);
								autorizacionEntSubCapita.setVerificarDetalleError(continuarProcesoAutorizacion);
								Log4JManager.info(autorizacionEntSubCapita.getMensajeErrorGeneral().getErrorKey()+" PROCESO AUTORIZACIÓN CAPITA CANCELADO");
								break;
							}
						}else {//Si no se encuentra activo la entidad subcontratada
							ErrorMessage error = new ErrorMessage("errors.autorizacion.parametroEntidadCentroCostoInternoNoActiva");
							autorizacionEntSubCapita.setMensajeErrorGeneral(error);
							continuarProcesoAutorizacion=false;
							autorizacionEntSubCapita.setProcesoExitoso(continuarProcesoAutorizacion);
							autorizacionEntSubCapita.setVerificarDetalleError(continuarProcesoAutorizacion);
							break;
						}
					}
					else {//Si el parametro entidad subcontratada para centros de costo internos no se encuentra definido
						ErrorMessage error = new ErrorMessage("errors.autorizacion.parametroEntidadCentroCostoInternoSinDefinir");
						autorizacionEntSubCapita.setMensajeErrorGeneral(error);
						continuarProcesoAutorizacion=false;
						autorizacionEntSubCapita.setProcesoExitoso(continuarProcesoAutorizacion);
						autorizacionEntSubCapita.setVerificarDetalleError(continuarProcesoAutorizacion);
						break;
					}
				}else{//Si el centro de costo es de tipo Externo o Ambos.
					
					//Se buscan las entidades subcontratadas activas y con contrato vigente asociadas al centro de costo que ejecuta la orden
					listaEntidadesSub=entSubDele.listarEntidadesSubXCentroCostoActivoContratoVigente(codigoCentroCostoEjecuta);
					//Valido si se encontro alguna entidad subcontratada
					if (listaEntidadesSub == null || listaEntidadesSub.isEmpty()){
						ErrorMessage error = new ErrorMessage("errors.autorizacion.parametroEntidadCentroCostoExternoSinEntidad");
						autorizacionEntSubCapita.setMensajeErrorGeneral(error);
						continuarProcesoAutorizacion=false;
						autorizacionEntSubCapita.setProcesoExitoso(continuarProcesoAutorizacion);
						autorizacionEntSubCapita.setVerificarDetalleError(continuarProcesoAutorizacion);
						break;
					}
				}
					
				//Valido si la entidad subcontratada cubre los Servicios/MedicamentosInsumos a autorizar
				if (continuarProcesoAutorizacion){	
					
					medicamentosInsumosCubierto = new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
					entidadSubAutorizar = new EntidadSubContratadaDto();
					for (DtoEntidadSubcontratada listaEntidadesSubc : listaEntidadesSub) {
						
						int contratoEntSubc=(int) listaEntidadesSubc.getContratoEntSub();
						//verifico si la o las ordenes autorizar es de Servicios
						if(ordenAutorizacion.getServiciosPorAutorizar() != null && 
								!ordenAutorizacion.getServiciosPorAutorizar().isEmpty())
						{
							for (ServicioAutorizacionOrdenDto listaServiciosAutorizar : ordenAutorizacion.getServiciosPorAutorizar()) {
								
								//Valido la cobertura de la entidad subcontratada
								InfoCobertura infCobertura=Cobertura.validacionCoberturaServicioEntidadSub(con,contratoEntSubc, 
										ordenAutorizacion.getCodigoViaIngreso(),autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoPaciente(), 
										listaServiciosAutorizar.getCodigo(), autorizacionEntSubCapita.getDatosPacienteAutorizar().getNaturalezaPaciente(), 
										autorizacionEntSubCapita.getCodigoInstitucion());
								//Valido si el Servicio se encuentra cubierto
								if(!infCobertura.incluido()){
									ErrorMessage error = new ErrorMessage("errors.autorizacion.noValidaCoberturaEntidadSub", 
											String.valueOf(listaServiciosAutorizar.getCodigo()));
									autorizacionEntSubCapita.setMensajeErrorGeneral(error);
									listaServiciosAutorizar.setAutorizado(false);
									break;
								}else{//Si aplica cobertura
									aplicaCobertura=true;
									listaServiciosAutorizar.setAutorizado(true);
									break;
								}
							}
							
							if (aplicaCobertura){
								entidadSubAutorizar.setCodEntidadSubcontratada(listaEntidadesSubc.getCodigoPk());
								entidadSubAutorizar.setCodContratoEntidadSub(listaEntidadesSubc.getContratoEntSub());
								entidadSubAutorizar.setTipoTarifa(listaEntidadesSubc.getTipotarifa());							
							}
							
							break;
							
						}else{//La o las ordenes autorizar son Medicamentos e Insumos
							for (MedicamentoInsumoAutorizacionOrdenDto listaMedicamentosInsumosAutorizar : ordenAutorizacion.getMedicamentosInsumosPorAutorizar()) {
								
								InfoCobertura infCobertura=Cobertura.validacionCoberturaArticuloEntidadSub(con,contratoEntSubc, 
										ordenAutorizacion.getCodigoViaIngreso(),autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoPaciente(),
										listaMedicamentosInsumosAutorizar.getCodigo(), autorizacionEntSubCapita.getDatosPacienteAutorizar().getNaturalezaPaciente(), 
										autorizacionEntSubCapita.getCodigoInstitucion());
								//Valido si el Medicamento/Insumo se encuentra cubierto
								if(!infCobertura.incluido()){
									ErrorMessage error = new ErrorMessage("errors.autorizacion.noValidaCoberturaEntidadSub", 
											String.valueOf(listaMedicamentosInsumosAutorizar.getCodigo()));
									autorizacionEntSubCapita.setMensajeErrorGeneral(error);
									listaMedicamentosInsumosAutorizar.setAutorizado(false);
									medicamentosInsumosCubierto.add(listaMedicamentosInsumosAutorizar);
									break;
								}else{//Si aplica cobertura
									aplicaCobertura=true;
									listaMedicamentosInsumosAutorizar.setAutorizado(true);
									break;
								}
							}
							if (aplicaCobertura){
								entidadSubAutorizar.setCodEntidadSubcontratada(listaEntidadesSubc.getCodigoPk());
								entidadSubAutorizar.setCodContratoEntidadSub(listaEntidadesSubc.getContratoEntSub());
								entidadSubAutorizar.setTipoTarifa(listaEntidadesSubc.getTipotarifa());							
							}	
						}
					}
				}
			}
		
				
			if(aplicaCobertura){
				autorizacionEntSubCapita.setVerificarDetalleError(false);
				autorizacionEntSubCapita.setProcesoExitoso(true);
				autorizacionEntSubCapita.setEntidadSubAutorizarCapitacion(entidadSubAutorizar);
			}else {
				Log4JManager.info("LA ENTIDAD SUBCONTRATADA NO CUBRE EL SERVICIO/ARTICULO DE LA ORDEN. PROCESO DE AUTORIZACIÓN CANCELADO");
			}
				
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
	}
		
	
	/**
	 * Método para obtener los montos de cobro para las autorizaciones automaticas  
	 * @param datosPaciente
	 * @throws IPSException 
	 */
	private void buscarMontoCobro(AutorizacionCapitacionDto autorizacionEntSubCapita,boolean requiereTransaccion) throws IPSException{
		/*
		 * Para este método utilizo la siguiente información contenida en la AutorizacionCapitacionDto:
		 * EstadoCuentaPaciente
		 * convenioResponsable
		 * víaIngreso
		 * TipoAfiliado
		 * TipoPaciente
		 * Clasificación Socieconomica
		 * Naturaleza Paciente
		 * TipoMonto
		 */
	
		MontoCobroDto montoCobro= null;
		try {
			ICatalogoFacturacionMundo catalogoFacturacionMundo = new CatalogoFacturacionMundo();
			FiltroBusquedaMontosCobroDto filtroBusquedaMontosCobro = new FiltroBusquedaMontosCobroDto();
			
			//Envio la información para realizar la búsqueda de montos de cobro
			filtroBusquedaMontosCobro.setCuentaAbierta(autorizacionEntSubCapita.getDatosPacienteAutorizar().isCuentaAbierta());
			filtroBusquedaMontosCobro.setConvenio(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getConvenio().getCodigo());
			filtroBusquedaMontosCobro.setViaIngreso(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getCodigoViaIngreso());
			filtroBusquedaMontosCobro.setTipoAfiliado(autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoAfiliado());
			filtroBusquedaMontosCobro.setTipoPaciente(autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoPaciente());
			filtroBusquedaMontosCobro.setClasificacionSocioEconomica(autorizacionEntSubCapita.getDatosPacienteAutorizar().getClasificacionSocieconomica());
			if (autorizacionEntSubCapita.getDatosPacienteAutorizar().getNaturalezaPaciente() != null){
				filtroBusquedaMontosCobro.setNaturalezaPaciente(autorizacionEntSubCapita.getDatosPacienteAutorizar().getNaturalezaPaciente());
			}
			
			if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getServiciosPorAutorizar()!=null &&
					!autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getServiciosPorAutorizar().isEmpty()){
				if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getServiciosPorAutorizar().get(0).getTipoMonto() != null){
					filtroBusquedaMontosCobro.setTipoMonto(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getServiciosPorAutorizar().get(0).getTipoMonto());
					filtroBusquedaMontosCobro.setAutorizacionCapitacionSubcontratada(true);
				}
			}
			
			//Realizo la búsqueda de los montos de cobro
			List<BusquedaMontosCobroDto> resultadoMontosCobro = catalogoFacturacionMundo.buscarMontosCobro(filtroBusquedaMontosCobro,requiereTransaccion);
			
			//Verifico si se encuentro mas de 1 monto de cobro
			if(resultadoMontosCobro.size() == 1){
				//Si solo se encontro un monto de cobro se asigna la información al montoCobroDto
				montoCobro = new MontoCobroDto();
				montoCobro.setPorcentajeMonto(resultadoMontosCobro.get(0).getPorcentaje());
				montoCobro.setTipoDetalleMonto(resultadoMontosCobro.get(0).getTipoDetalle());
				montoCobro.setTipoMontoNombre(resultadoMontosCobro.get(0).getTipoMontoNombre());
				montoCobro.setTipoMonto(resultadoMontosCobro.get(0).getTipoMontoID());
				montoCobro.setValorMonto(resultadoMontosCobro.get(0).getValor());
				montoCobro.setCantidadMonto(resultadoMontosCobro.get(0).getCantidadMonto());
				montoCobro.setCodDetalleMonto(resultadoMontosCobro.get(0).getDetalleCodigo());
				autorizacionEntSubCapita.setMontoCobroAutorizacion(montoCobro);
			}
			
			if(resultadoMontosCobro.size() > 1){
				ErrorMessage error = new ErrorMessage("errors.autorizacion.existeVariosMontos");
				autorizacionEntSubCapita.setMensajeErrorGeneral(error);
				autorizacionEntSubCapita.setProcesoExitoso(false);
				autorizacionEntSubCapita.setVerificarDetalleError(false);
				Log4JManager.info("SE ENCONTRARON VARIOS MONTOS DE COBRO. PROCESO DE AUTORIZACIÓN CANCELADO");
			}
			
			if(resultadoMontosCobro.size() < 1){
				ErrorMessage error = new ErrorMessage("errors.autorizacion.noExisteMontos");
				autorizacionEntSubCapita.setMensajeErrorGeneral(error);
				autorizacionEntSubCapita.setProcesoExitoso(false);
				autorizacionEntSubCapita.setVerificarDetalleError(false);
				Log4JManager.info("NO SE ENCONTRARON MONTOS DE COBRO. PROCESO DE AUTORIZACIÓN CANCELADO");
			}
			
			
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	
	/**
	 * Método que permite calcular la tarifa para los Servicios/MedicamentosInsumos a autorizar cuando 
	 * la tarifa de la entidad subcontratada es 'Convenio Paciente'. 
	 * @param listaOrdenesAutorizar
	 * @param tipoPaciente
	 * @param entidadSubcontratadaAutorizar
	 * @param con
	 * @throws IPSException 
	 */
	
	public void calcularTarifaArticuloServicioConvenio(AutorizacionCapitacionDto autorizacionEntSubCapita, Connection con) throws IPSException{

		/*Para este método se utilizaran los siguientes datos del DTO AutorizacionCapitacionDto:
		EntidadSubContratadaDto entidadSubcontratadaAutorizar,List<OrdenAutorizacionDto> listaOrdenesAutorizar 
		String tipoPaciente, int codInstitucion, int viaIngreso, int codCuenta, int centroAtencion
		 */
		try{
			IEsqTarProcedimientoContratoDAO esquemaTarProcedimiento= InventarioDAOFabrica.crearEsqTarProcedimientoContrato();
			IEsqTarInventariosContratoDAO esquemaTarInventarios = InventarioDAOFabrica.crearEsqTarInventariosContrato();
			IServiciosMundo servicioMundo = FacturacionFabricaMundo.crearServiciosMundo();
			DTOEsqTarProcedimientoContrato esquemaTarServicios = null;
			DtoContrato contrato=null;
			InfoDatosInt tipoComplejidad = new InfoDatosInt();
			String esquemaTarifarioAutocapitaSubCirugiasNoCurentos = null;
			List<Integer> listaTiposLiquidacion = null;
			List<String> listaTiposServicio = null;
			CargosEntidadesSubcontratadas tarifaEntidadSub = new CargosEntidadesSubcontratadas();
			DTOCalculoTarifaServicioArticulo calculoTarifaServArti = new DTOCalculoTarifaServicioArticulo();
			DtoEntidadSubcontratada entidadSub=new DtoEntidadSubcontratada();
			double tarifa = ConstantesBD.codigoNuncaValidoDouble;
			BigDecimal valorTarifa= BigDecimal.ZERO;
			DTOEsqTarInventarioContrato esquemaTarifarioInventario= null;
			autorizacionEntSubCapita.setProcesoExitoso(true);
			boolean encontroTarifa = false;
			boolean noExisteTarifaCx = false;
			//sanbarga MT6703 atributo para almacenar el valor de la autorización 
			BigDecimal valorAutorizacion=new BigDecimal(0.0);
			
			listaTiposLiquidacion = new ArrayList<Integer>();
			listaTiposLiquidacion.add(ConstantesBD.codigoTipoLiquidacionSoatGrupo);
			listaTiposLiquidacion.add(ConstantesBD.codigoTipoLiquidacionSoatUvr);
			
			listaTiposServicio = new ArrayList<String>();
			listaTiposServicio.add(ConstantesBD.codigoServicioNoCruentos+"");
			listaTiposServicio.add(ConstantesBD.codigoServicioQuirurgico+"");
			
			//Obtengo el tipo de complejidad
			tipoComplejidad = Utilidades.obtenerTipoComplejidadCuenta(con,autorizacionEntSubCapita.getDatosPacienteAutorizar().getCuenta());
			
			for (OrdenAutorizacionDto ordenAutorizacion : autorizacionEntSubCapita.getOrdenesAutorizar()) {
				
				//Verfico si son servicios a autorizar
				if (ordenAutorizacion.getServiciosPorAutorizar() != null && !ordenAutorizacion.getServiciosPorAutorizar().isEmpty()){
					//Verifico si se encuentra esquema tarifario				
					for (ServicioAutorizacionOrdenDto servicioAutorizar : ordenAutorizacion.getServiciosPorAutorizar()) {
					//Busco el esquema tarifario servicios para el convenioContrato
						
						/*
						 * MT 5233
						 * 
						 * Se debe consultar si el contrato maneja tarifa por centro de costo, en caso de no tener se sigue el flujo normal
						 * 
						 * */
						
						
						if(ordenAutorizacion.getContrato().getManejaTarifaCentroAtencion() != null && 
								UtilidadTexto.getBoolean(ordenAutorizacion.getContrato().getManejaTarifaCentroAtencion())){
							ICatalogoFacturacionMundo catalogoFacturacionMundo=new CatalogoFacturacionMundo();
							esquemaTarServicios = catalogoFacturacionMundo.obtenerEsquemaTarifarioProcedimientosContrato(
									ordenAutorizacion.getContrato().getCodigo(), 
									autorizacionEntSubCapita.getCentroAtencion(), 
									UtilidadFecha.getFechaActualTipoBD());
							/*
							 * Si el esquema tarifario tiene restriccion al grupo de servicio 
							 * se valida que el servicio tenga el mismo grupo de servicio
							 * */
							if(esquemaTarServicios!=null&&esquemaTarServicios.getCodigoGrupoServicio()!=null
									&&servicioAutorizar.getCodigoGrupoServicio().intValue()!=esquemaTarServicios.getCodigoGrupoServicio().intValue()){
								esquemaTarServicios=null;
							}
						
						}else{
							esquemaTarServicios=new DTOEsqTarProcedimientoContrato();
							contrato=new DtoContrato();
							contrato.setCodigo(ordenAutorizacion.getContrato().getCodigo());
							esquemaTarServicios.setContrato(contrato);				
							esquemaTarServicios = esquemaTarProcedimiento.buscarEsquematarifarioPorContrato(esquemaTarServicios);
						}
						
					if(esquemaTarServicios!=null){
							//verifico que el servicio este chequeado para autorizar
							if(servicioAutorizar.isAutorizar()){
								//Verifico si el tipo de liquidación del servicio es UVR o Grupo

								////List<DtoServicios> listaServiciosCirugiasNoCruentosGrupoUVR = new ArrayList<DtoServicios>();								
								//listaServiciosCirugiasNoCruentosGrupoUVR = servicioMundo.obtenerServicioLiquidacionPorEsquema(servicioAutorizar.getCodigo(), esquemaTarServicios.getCodigoEsquema(), listaTiposLiquidacion, listaTiposServicio);																			
								//MT6587
								int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, esquemaTarServicios.getCodigoEsquema());
								DtoServicios  tarifaVigente=null;
								if(codigoTipoTarifario==ConstantesBD.codigoTarifarioISS )
								{
									
									  tarifaVigente= servicioMundo.obtenerTarifaIssVigente(esquemaTarServicios.getCodigoEsquema(), servicioAutorizar.getCodigo());
								}
								else{
									if(codigoTipoTarifario==ConstantesBD.codigoTarifarioSoat)
									{									
										tarifaVigente= servicioMundo.obtenerTarifaSoatVigente(esquemaTarServicios.getCodigoEsquema(), servicioAutorizar.getCodigo());
									}
									else{
										noExisteTarifaCx = true;
										ErrorMessage error = new ErrorMessage("errors.autorizacion.sinTarifaDefinida", 
												String.valueOf(servicioAutorizar.getCodigo()));
										autorizacionEntSubCapita.setMensajeErrorGeneral(error);
										servicioAutorizar.setMensajeError(error);
										servicioAutorizar.setAutorizado(false);
										autorizacionEntSubCapita.setVerificarDetalleError(true);
										Log4JManager.info("NO SE ENCUENTRA LA TARIFA DEFINIDA PARA EL SERVICIO: "+servicioAutorizar.getCodigo()+" .PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
										break;
									}
								}	
								
								//MT6798 se agrega validación
								if(tarifaVigente!=null && tarifaVigente.getCodigoTipoLiquidacion()==ConstantesBD.codigoTipoLiquidacionSoatValor)
								{
									//Si no existe información quiere decir que el tipo de liquidación del servicio No es UVR o Grupo, por tanto se calcula la tarifa con el metodo
									//centralizado Calculo Tarifa del Cargo Modificado DCU438
									InfoTarifaYExcepcion infoTarifaYExcepcion=Cargos.obtenerValorTarifaYExcepcion(
											con, ordenAutorizacion.getContrato().getConvenio().getCodigo(),
											ordenAutorizacion.getContrato().getCodigo(), esquemaTarServicios.getCodigoEsquema(),
											autorizacionEntSubCapita.getCodigoInstitucion(),servicioAutorizar.getCodigo(), true, autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoPaciente(),
											ordenAutorizacion.getCodigoViaIngreso(),tipoComplejidad.getCodigo(), "", autorizacionEntSubCapita.getCentroAtencion());								
									tarifa =infoTarifaYExcepcion.getValorTarifaTotalConExcepcion();
									valorTarifa= BigDecimal.valueOf(tarifa);
									//Valido si se pudo generar la tarifa para el servicio
									if (tarifa != ConstantesBD.codigoNuncaValidoDouble && 
											tarifa != ConstantesBD.codigoNuncaValidoDoubleNegativo){
										//sanbarga MT6703 calculo de las autorizaciones
										valorAutorizacion=valorAutorizacion.add(valorTarifa.multiply(BigDecimal.valueOf(servicioAutorizar.getCantidad())));
										autorizacionEntSubCapita.setValorAutorizacion(valorAutorizacion);
										servicioAutorizar.setValorTarifa(valorTarifa);										
										autorizacionEntSubCapita.setProcesoExitoso(true);
										servicioAutorizar.setAutorizado(true);
										encontroTarifa=true;
									}else{//Si la tarifa no fue obtenida no se genera autorización para este servicio
										noExisteTarifaCx = true;
										ErrorMessage error = new ErrorMessage("errors.autorizacion.sinTarifaDefinida", 
												String.valueOf(servicioAutorizar.getCodigo()));
										servicioAutorizar.setMensajeError(error);
										servicioAutorizar.setAutorizado(false);
										autorizacionEntSubCapita.setVerificarDetalleError(true);
										Log4JManager.info("NO SE ENCUENTRA LA TARIFA DEFINIDA PARA EL SERVICIO: "+servicioAutorizar.getCodigo()+" .PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
									}
									
								}else {
									//MT 6545 quita la validación según control de cambio del DCU 799 que me indica que solo se debe validar que el tipo de liquidación solo sea valor
									//Si hay resultados el servicio Tipo Liquidación como: Grupo o UVR 
									//Validar si esta definido el parámetro general Esquema Tarifario para Autorizaciones Capitación Subcontratada de Cirugías y No Cruentos
									esquemaTarifarioAutocapitaSubCirugiasNoCurentos = ValoresPorDefecto.getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos(autorizacionEntSubCapita.getCodigoInstitucion());
									if(!UtilidadTexto.isEmpty(esquemaTarifarioAutocapitaSubCirugiasNoCurentos)){
										/*//Si esta definido se debe realizar el cálculo de la Tarifa utilizando el método Cálculo Tarifa Entidades Subcontratadas DCU799								
										entidadSub.setConsecutivo(autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()+"");
										calculoTarifaServArti.setEntidadSubcontratada(entidadSub);
										calculoTarifaServArti.setFechaVigencia(UtilidadFecha.getFechaActual());
										calculoTarifaServArti.setEsServicio(true);
										EsquemaTarifario esquema = new EsquemaTarifario();
										esquema.setCodigo(Integer.parseInt(esquemaTarifarioAutocapitaSubCirugiasNoCurentos));
										calculoTarifaServArti.setCodigoArticuloServicio(servicioAutorizar.getCodigo());
										tarifa = tarifaEntidadSub.calcularTarifaEntidadSubcontratada(con, calculoTarifaServArti, esquema);									
										valorTarifa= BigDecimal.valueOf(tarifa);
										//Valido si se pudo generar la tarifa para el servicio
										if (tarifa != ConstantesBD.codigoNuncaValidoDouble ||
												tarifa != ConstantesBD.codigoNuncaValidoDoubleNegativo){
											servicioAutorizar.setValorTarifa(valorTarifa);									
											servicioAutorizar.setAutorizado(true);
											encontroTarifa=true;
										}else{*/
										
											noExisteTarifaCx = true;
											ErrorMessage error = new ErrorMessage("errors.autorizacion.sinTarifaDefinida", 
													String.valueOf(servicioAutorizar.getCodigo()));
											autorizacionEntSubCapita.setMensajeErrorGeneral(error);
											//MT6545
											servicioAutorizar.setMensajeError(error);
											servicioAutorizar.setAutorizado(false);
											autorizacionEntSubCapita.setVerificarDetalleError(true);
											Log4JManager.info("NO SE ENCUENTRA LA TARIFA DEFINIDA PARA EL SERVICIO: "+servicioAutorizar.getCodigo()+" .PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
											break;
										
										
									}else{ //No esta definido el parámetro general Esquema Tarifario para Autorizaciones Capitación Subcontratada de Cirugías y No Cruentos								
										ErrorMessage error = new ErrorMessage("errors.autorizacion.sinEsquemaTarifarioCxNoCruentos", 
												String.valueOf(servicioAutorizar.getCodigo()));
										autorizacionEntSubCapita.setMensajeErrorGeneral(error);
										//MT6545
										servicioAutorizar.setMensajeError(error);
										autorizacionEntSubCapita.setVerificarDetalleError(false);
										autorizacionEntSubCapita.setProcesoExitoso(false);
										Log4JManager.info("NO SE ENCUENTRA DEFINIDO EL PARAMETRO GENERAL ESQUEMA TARIFARIO PARA AUTORIZACIONES CAPITACIÓN SUBCONTRATADA DE CIRUGÍAS" +
												" Y NO CRUENTOS. PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
									}
								}
							}
					} else { //No existe esquema tarifario de procedimientos					 
						ErrorMessage error = new ErrorMessage("errors.autorizacion.sinEsquemaTarifarioProcedimientos");
						autorizacionEntSubCapita.setMensajeErrorGeneral(error);
						//MT6545
						servicioAutorizar.setMensajeError(error);
						autorizacionEntSubCapita.setProcesoExitoso(false);
						autorizacionEntSubCapita.setVerificarDetalleError(false);
						Log4JManager.info("NO SE ENCUENTRA DEFINIDO EL ESQUEMA TARIFARIO DE PROCEDIMIENTOS. PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
						break;
					}
					}
				} else { //En caso de ser Medicamentos/Insumos a autorizar
					autorizacionEntSubCapita.setProcesoExitoso(false);
					for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizar : ordenAutorizacion.getMedicamentosInsumosPorAutorizar()) {
							
						/*
						 * MT 5233
						 * 
						 * Se debe consultar si el contrato maneja tarifa por centro de costo, en caso de no tener se sigue el flujo normal
						 * 
						 * */
						
						if(ordenAutorizacion.getContrato().getManejaTarifaCentroAtencion()!=null&&UtilidadTexto.getBoolean(ordenAutorizacion.getContrato().getManejaTarifaCentroAtencion())){
							ICatalogoFacturacionMundo catalogoFacturacionMundo=new CatalogoFacturacionMundo();
							esquemaTarifarioInventario = catalogoFacturacionMundo.obtenerEsquemaTarifarioInventariosContrato(
									ordenAutorizacion.getContrato().getCodigo(), 
									autorizacionEntSubCapita.getCentroAtencion(), 
									UtilidadFecha.getFechaActualTipoBD());
							/*
							 * Si el esquema tarifario tiene restriccion a la clase de inventario 
							 * se valida que el articulo tenga la mismo clase de inventario
							 * */
							if(esquemaTarifarioInventario!=null&&esquemaTarifarioInventario.getCodigoClaseInventario()!=null
									&&medicamentoInsumoAutorizar.getClaseInventario().intValue()!=esquemaTarifarioInventario.getCodigoClaseInventario().intValue()){
								esquemaTarifarioInventario=null;
							}
						
						}else{
							//Realizo la búsqueda del esquema tarifario de Medicamentos e Insumos
							esquemaTarifarioInventario = new DTOEsqTarInventarioContrato();	
							contrato=new DtoContrato();
							contrato.setCodigo(ordenAutorizacion.getContrato().getCodigo());
							esquemaTarifarioInventario.setContrato(contrato);
							esquemaTarifarioInventario = esquemaTarInventarios.buscarEsquematarifarioPorContrato(esquemaTarifarioInventario);
						}
					
						//Verifico que se encuentre el esquema tarifario
						if(esquemaTarifarioInventario!=null){
								//verifico que el Medicamento/Insumo este chequeado para autorizar
								if(medicamentoInsumoAutorizar.isAutorizar()){
									//Obtengo el valor del tarifa para el Medicamento/Insumo
									InfoTarifaYExcepcion infoTarifaYExcepcion=Cargos.obtenerValorTarifaYExcepcion(
											con, ordenAutorizacion.getContrato().getConvenio().getCodigo(),
											ordenAutorizacion.getContrato().getCodigo(), esquemaTarifarioInventario.getCodigoEsquema(),
											autorizacionEntSubCapita.getCodigoInstitucion(),medicamentoInsumoAutorizar.getCodigo(), false, autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoPaciente(),
											ordenAutorizacion.getCodigoViaIngreso(),tipoComplejidad.getCodigo(), "", autorizacionEntSubCapita.getCentroAtencion());								
									tarifa = infoTarifaYExcepcion.getValorTarifaTotalConExcepcion();
									valorTarifa= BigDecimal.valueOf(tarifa);
									//Verifico el valor de la tarifa
									if (tarifa != ConstantesBD.codigoNuncaValidoDouble ){
										//sanbarga MT6703 el calculo de la autorización
										valorAutorizacion=valorAutorizacion.add(valorTarifa.multiply(BigDecimal.valueOf(medicamentoInsumoAutorizar.getCantidad())));
										autorizacionEntSubCapita.setValorAutorizacion(valorAutorizacion);
										medicamentoInsumoAutorizar.setValorTarifa(valorTarifa);									
										autorizacionEntSubCapita.setProcesoExitoso(true);
										medicamentoInsumoAutorizar.setAutorizado(true);
										encontroTarifa=true;
									}else{//Si la tarifa no fue obtenida no se genera autorización para este servicio
										noExisteTarifaCx = true;
										ErrorMessage error = new ErrorMessage("errors.autorizacion.sinTarifaDefinida", 
												String.valueOf(medicamentoInsumoAutorizar.getCodigo()));
										medicamentoInsumoAutorizar.setMensajeError(error);
										medicamentoInsumoAutorizar.setAutorizado(false);
										autorizacionEntSubCapita.setVerificarDetalleError(true);
										Log4JManager.info("NO SE ENCUENTRA LA TARIFA DEFINIDA PARA EL MEDICAMENTO/INSUMO: "+medicamentoInsumoAutorizar.getCodigo()+" .PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
									}
								}
						}else{//No existe esquema tarifario de Inventarios
							ErrorMessage error = new ErrorMessage("errors.autorizacion.sinEsquemaTarifarioInventario");
							autorizacionEntSubCapita.setMensajeErrorGeneral(error);
							autorizacionEntSubCapita.setProcesoExitoso(false);
							autorizacionEntSubCapita.setVerificarDetalleError(false);
							Log4JManager.info("NO SE ENCUENTRA DEFINIDO EL ESQUEMA TARIFARIO DE INVENTARIOS. PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
							break;
						}
					}
				}
				
				if(!autorizacionEntSubCapita.isProcesoExitoso()){
						break;
				}
			}
			
			//Valido si no se pudo generar tarifa para todos los Servicios/Articulos
			if (!encontroTarifa){
				autorizacionEntSubCapita.setProcesoExitoso(false);
			}
			
			//Si la orden a autorizar es de Cx valido si al menos para 1 servicio no se pudo calcular tarifa, en este caso
			//se debe cancelar proceso para todos los servicios
			if(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia){
				if (noExisteTarifaCx){
					autorizacionEntSubCapita.setProcesoExitoso(false);
				}
			}
		} 
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		} 
	}

	/**
	 * Método que permite calcular la tarifa para los Servicios/MedicamentosInsumos a autorizar cuando 
	 * la tarifa de la entidad subcontratada es 'Propia'.
	 * @param listaOrdenesAutorizar
	 * @param entidadSubcontratada
	 * @param con
	 * @throws IPSException 
	 */
	
	
	public void calcularTarifaArticuloServicioEntSub(Connection con, AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException{
			
		//Para este método se utilizaran los siguientes datos del DTO AutorizacionCapitacionDto:
		//EntidadSubContratadaDto entidadSubcontratadaAutorizar,List<OrdenAutorizacionDto> listaOrdenesAutorizar, 
		
		try{
			CargosEntidadesSubcontratadas tarifaEntidadSub = new CargosEntidadesSubcontratadas();
			DTOCalculoTarifaServicioArticulo calculoTarifaServArti = new DTOCalculoTarifaServicioArticulo();
			DtoEntidadSubcontratada entidadSub=new DtoEntidadSubcontratada();
			double tarifa = ConstantesBD.codigoNuncaValidoDouble;
			BigDecimal Valortarifa = BigDecimal.ZERO;
			boolean tarifaObtenida = false;
			boolean noExisteTarifaCx = false;
			
			for (OrdenAutorizacionDto ordenAutorizacion : autorizacionEntSubCapita.getOrdenesAutorizar()) {
				
				//Verfico si son servicios a autorizar
				if (ordenAutorizacion.getServiciosPorAutorizar() != null && !ordenAutorizacion.getServiciosPorAutorizar().isEmpty()){
					for (ServicioAutorizacionOrdenDto servicioAutorizar : ordenAutorizacion.getServiciosPorAutorizar()) {
						//verifico que el servicio este chequeado para autorizar
						if (servicioAutorizar.isAutorizar()){
							//Busco la tarifa para el servicio
							entidadSub.setConsecutivo(autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()+"");
							calculoTarifaServArti.setEntidadSubcontratada(entidadSub);
							calculoTarifaServArti.setFechaVigencia(UtilidadFecha.getFechaActual());
							calculoTarifaServArti.setEsServicio(true);
							EsquemaTarifario esquema = new EsquemaTarifario();
							calculoTarifaServArti.setCodigoArticuloServicio(servicioAutorizar.getCodigo());
							tarifa = tarifaEntidadSub.calcularTarifaEntidadSubcontratada(con, calculoTarifaServArti, esquema);						
							Valortarifa= BigDecimal.valueOf(tarifa);
							//Valido si se pudo generar la tarifa para el servicio
							if (tarifa == ConstantesBD.codigoNuncaValidoDoubleNegativo ||
									tarifa == ConstantesBD.codigoNuncaValidoDouble){
								noExisteTarifaCx = true;
								ErrorMessage error = new ErrorMessage("errors.autorizacion.sinTarifaDefinida", 
										String.valueOf(servicioAutorizar.getCodigo()));
								servicioAutorizar.setMensajeError(error);
								servicioAutorizar.setAutorizado(false);
								autorizacionEntSubCapita.setVerificarDetalleError(true);
								Log4JManager.info("LA TARIFA NO FUE OBTENIDA PARA EL SERVICIO "+ servicioAutorizar.getCodigo()+". PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
							} else {//Si la tarifa no fue obtenida no se genera autorización para este servicio
								servicioAutorizar.setValorTarifa(Valortarifa);									
								autorizacionEntSubCapita.setProcesoExitoso(true);
								servicioAutorizar.setAutorizado(true);
								tarifaObtenida = true;
							}
						}
					}
				} else {//En caso de ser Medicamentos/Insumos a autorizar
					for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizar : ordenAutorizacion.getMedicamentosInsumosPorAutorizar()) {
						//verifico que el Medicamento/Insumo este chequeado para autorizar
						if (medicamentoInsumoAutorizar.isAutorizar()){
							//Busco la tarifa para el servicio
							entidadSub.setConsecutivo(autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()+"");
							calculoTarifaServArti.setEntidadSubcontratada(entidadSub);
							calculoTarifaServArti.setFechaVigencia(UtilidadFecha.getFechaActual());
							calculoTarifaServArti.setEsServicio(false);
							EsquemaTarifario esquema = new EsquemaTarifario();
							calculoTarifaServArti.setCodigoArticuloServicio(medicamentoInsumoAutorizar.getCodigo());
							tarifa = tarifaEntidadSub.calcularTarifaEntidadSubcontratada(con, calculoTarifaServArti, esquema);
							Valortarifa= BigDecimal.valueOf(tarifa);
							//Valido si se pudo generar la tarifa para el servicio
							if (tarifa != ConstantesBD.codigoNuncaValidoDoubleNegativo ||
									tarifa != ConstantesBD.codigoNuncaValidoDouble){
								medicamentoInsumoAutorizar.setValorTarifa(Valortarifa);									
								autorizacionEntSubCapita.setProcesoExitoso(true);
								medicamentoInsumoAutorizar.setAutorizado(true);
								tarifaObtenida = true;
							}else{//Si la tarifa no fue obtenida no se genera autorización para este MedicamentoInsumo
								noExisteTarifaCx = true;
								ErrorMessage error = new ErrorMessage("errors.autorizacion.sinTarifaDefinida", 
										String.valueOf(medicamentoInsumoAutorizar.getCodigo()));
								medicamentoInsumoAutorizar.setMensajeError(error);
								medicamentoInsumoAutorizar.setAutorizado(false);
								autorizacionEntSubCapita.setVerificarDetalleError(true);
								Log4JManager.info("LA TARIFA NO FUE OBTENIDA PARA EL SERVICIO "+ medicamentoInsumoAutorizar.getCodigo()+". PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
							}
						}
					}
				}	
			}
			
			//Valido si no se obtuvo la tarifa para todos los Servicios/Articulos
			if (!tarifaObtenida){
				autorizacionEntSubCapita.setProcesoExitoso(false);
			}
			//Si la orden a autorizar es de Cx valido si al menos para 1 servicio no se pudo calcular tarifa, en este caso
			//se debe cancelar proceso para todos los servicios
			if(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia){
				if (noExisteTarifaCx){
					autorizacionEntSubCapita.setProcesoExitoso(false);
				}
			}
		}
		catch (Exception e) {
			Log4JManager.error(e.getCause(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	
	/**
	 * Método que realiza la validación de presupuesto para los Servicios/MedicamentosInsumos a Autorizar
	 * @param realizarEliminacion
	 * @param fechaCierreOrdenMedica
	 * @throws IPSException 
	 */
	
	
	public void validarPresupuestoCapitacion(AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException{
		//Para este método se utilizaran los siguientes datos del DTO AutorizacionCapitacionDto:
		//List<OrdenAutorizacionDto> listaOrdenesAutorizar
		try{
			IValidacionPresupuestoCapitacionMundo validacionPresupuestoMundo = CapitacionFabricaMundo.crearValidacionPresupuestoCapitacionMundo();
			boolean exitosaValidacionPresupuesto=false;
			
			for (OrdenAutorizacionDto ordenAutorizacion : autorizacionEntSubCapita.getOrdenesAutorizar()) {
				//Valido que el convenio de la cobertura para los Servicios/MedicamentosInsumos manejen capitación subcontratada
				if (ordenAutorizacion.getContrato().getConvenio().isConvenioManejaCapiSub()){
					//Verfico si son servicios a autorizar
					if (ordenAutorizacion.getServiciosPorAutorizar() != null && !ordenAutorizacion.getServiciosPorAutorizar().isEmpty()){
						for(ServicioAutorizacionOrdenDto serviciosAutorizar : ordenAutorizacion.getServiciosPorAutorizar()){
							//Valido cuales son los servicios a autorizar 
							if(serviciosAutorizar.isAutorizado() && serviciosAutorizar.isAutorizar()){
								//Verifico la validación de presupuesto para el servicio
								DtoValidacionPresupuesto dtoValidacionPresupuestoServicio = validacionPresupuestoMundo
										.validarPresupuestoCapitacionServicio(serviciosAutorizar, ordenAutorizacion.getContrato().getConvenio().getCodigo(), 
												ordenAutorizacion.getContrato().getConvenio().getNombre(), ordenAutorizacion.getContrato().getCodigo(), 
												ordenAutorizacion.getContrato().getNumero());
								
								//Verifico si la validación del presupuesto para el servicio es correcta
								if(!dtoValidacionPresupuestoServicio.isValido()){
									//No se puede autorizar este servicio
									serviciosAutorizar.setMensajeError(dtoValidacionPresupuestoServicio.getMensajeError());
									serviciosAutorizar.setAutorizado(false);
									autorizacionEntSubCapita.setVerificarDetalleError(true);
									Log4JManager.info("FALLO VALIDACIÓN PRESUPUESTO POR LO SIGUIENTE: "+dtoValidacionPresupuestoServicio.getMensajeError().getErrorKey()+ 
											" .PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
								}else{//La validación de presupuesto para el servicio es correcta								
									serviciosAutorizar.setAutorizado(true);
									exitosaValidacionPresupuesto=true;
								}
							}
						}	
					}else{//Valido los Medicamentos e Insumos
						
						for (MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar: ordenAutorizacion.getMedicamentosInsumosPorAutorizar()) {
							//Valido cuales son los Medicamentos/Insumos a Autorizar
							if(medicamentosAutorizar.isAutorizado() && medicamentosAutorizar.isAutorizar()){
								//Realizo la validación de presupuesto
								DtoValidacionPresupuesto dtoValidacionPresupuestoArticulo= validacionPresupuestoMundo
										.validarPresupuestoCapitacionArticulo(medicamentosAutorizar, ordenAutorizacion.getContrato().getConvenio().getCodigo(), 
												ordenAutorizacion.getContrato().getConvenio().getNombre(), ordenAutorizacion.getContrato().getCodigo(), 
												ordenAutorizacion.getContrato().getNumero());
								//Valido si el Medicamento/Insumo
								if(!dtoValidacionPresupuestoArticulo.isValido()){
									//No se puede autorizar este Medicamento/Insumo
									medicamentosAutorizar.setMensajeError(dtoValidacionPresupuestoArticulo.getMensajeError());
									medicamentosAutorizar.setAutorizado(false);
									autorizacionEntSubCapita.setVerificarDetalleError(true);
									Log4JManager.info("FALLO VALIDACIÓN PRESUPUESTO POR LO SIGUIENTE: "+dtoValidacionPresupuestoArticulo.getMensajeError().getErrorKey()+ 
											" .PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
								}else { //La validación de presupuesto para los Medicamentos/Insumos es correcta
									medicamentosAutorizar.setAutorizado(true);
									exitosaValidacionPresupuesto=true;
								}
							}
						}
					}
				}	
			}
			
			//Valido si algun Servicio/MedicamentoInsumo paso la validación de presupuesto
			if (!exitosaValidacionPresupuesto){
				autorizacionEntSubCapita.setProcesoExitoso(false);
			}
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	
	/**
	 * Método para calcular el monto de cobro para la autorización 
	 * @param montoCobro
	 * @throws IPSException 
	 */
	public void calcularMontoCobro(AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException{
		
		ICatalogoFacturacionMundo catalogoFacturacionMundo = new CatalogoFacturacionMundo();
		boolean continuarCalculoMonto=true;
		Double valorMontoCalculado= Double.valueOf(0);
		boolean esPyp = false;
		try{
			
			//En caso de que el valor del monto y el porcentaje del monto llegue = 0, se asigna a alguno de los dos valores null
			//Este caso se presenta para las autorizaciones automaticas por la implementación de la consulta no afecta la logica del negocio
			if (autorizacionEntSubCapita.getMontoCobroAutorizacion().getPorcentajeMonto() != null &&
					autorizacionEntSubCapita.getMontoCobroAutorizacion().getValorMonto() != null){
				if (autorizacionEntSubCapita.getMontoCobroAutorizacion().getValorMonto() > autorizacionEntSubCapita.getMontoCobroAutorizacion().getPorcentajeMonto()){
					autorizacionEntSubCapita.getMontoCobroAutorizacion().setPorcentajeMonto(null);
				}else{
					autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMonto(null);
				}
			}
			
			//Valido si la orden tiene marca PyP
			if(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).isEsPyp()){
				//El valor del monto debe ser 0.0
				autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMontoCalculado(0.0);
				autorizacionEntSubCapita.setProcesoExitoso(true);
				continuarCalculoMonto=false;
				esPyp = true;
			}
			ManejoPacienteFacade manejoPacienteFacade= new ManejoPacienteFacade();
			//Verifico si para la cuenta del paciente no aplica los montos
			if(!autorizacionEntSubCapita.getDatosPacienteAutorizar().isCuentaManejaMontos()){
				//Tomo el valor de porcentaje definido en la sub_cuenta del paciente
				//if (autorizacionEntSubCapita.getDatosPacienteAutorizar().getPorcentajeMontoCuenta() == null){
					//sanbarga MT6703 se agrega validación si paciente paga atención y se elimina las validaciones existentes
					
			    	Boolean isPacientePagaAtencion = manejoPacienteFacade.consultarSiPacientePagaAtencion(autorizacionEntSubCapita.getDatosPacienteAutorizar().getContrato().getCodigo(), false);
					if(isPacientePagaAtencion)
						{
						autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMontoCalculado(autorizacionEntSubCapita.getValorAutorizacion().doubleValue());//.setMontoCobroAutorizacion(dtoMonto); 
						}  
					else
					    {
						
						autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMontoCalculado(0.0);
					    }
						
					
					//autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMontoCalculado(0.0);
					autorizacionEntSubCapita.setProcesoExitoso(true);
					continuarCalculoMonto=false;
			/*	}else {
					autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMontoCalculado(autorizacionEntSubCapita.getDatosPacienteAutorizar().getPorcentajeMontoCuenta());
					autorizacionEntSubCapita.setProcesoExitoso(true);
					continuarCalculoMonto=false;
				}*/
			}
			
					
			if(continuarCalculoMonto){
				//Valido si el monto es General
				if(autorizacionEntSubCapita.getMontoCobroAutorizacion().getTipoDetalleMonto().equals(ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN)){
					//Valido si esta definido el valor del monto
					if(autorizacionEntSubCapita.getMontoCobroAutorizacion().getValorMonto() != null &&
							autorizacionEntSubCapita.getMontoCobroAutorizacion().getCantidadMonto() != null){
						//Multiplico el valor del monto por la cantidad
						valorMontoCalculado= autorizacionEntSubCapita.getMontoCobroAutorizacion().getValorMonto()*autorizacionEntSubCapita.getMontoCobroAutorizacion().getCantidadMonto();
					}
					
					//Valido si esta definido el porcentaje del monto
					if(autorizacionEntSubCapita.getMontoCobroAutorizacion().getPorcentajeMonto() != null &&
							autorizacionEntSubCapita.getMontoCobroAutorizacion().getCantidadMonto() != null){
						
						Double sumatoriaValorTarifa = Double.valueOf(0);
						for (OrdenAutorizacionDto ordenAutorizar : autorizacionEntSubCapita.getOrdenesAutorizar()) {
							//Verifico si la orden a autorizar es de servicios
							if (ordenAutorizar.getServiciosPorAutorizar()!=null && !ordenAutorizar.getServiciosPorAutorizar().isEmpty()){
								for (ServicioAutorizacionOrdenDto servicioAutorizar : ordenAutorizar.getServiciosPorAutorizar()) {
									//Verifico cual de los servicios aplica para autorizar
									if (servicioAutorizar.isAutorizar() && servicioAutorizar.isAutorizado()){
										//Realizo la sumatoria del valor total de cargos
										sumatoriaValorTarifa = sumatoriaValorTarifa + servicioAutorizar.getValorTarifa().doubleValue();
									}
								}
							}else{//La orden autorizar es de Medicamentos/Insumos
								for (MedicamentoInsumoAutorizacionOrdenDto medicamentoAutorizar : ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
									//Verifico cual de los Medicamentos/Insumos aplica para autorizar
									if (medicamentoAutorizar.isAutorizar() && medicamentoAutorizar.isAutorizado()){
										//Realizo la sumatoria del valor total de cargos
										sumatoriaValorTarifa = sumatoriaValorTarifa + medicamentoAutorizar.getValorTarifa().doubleValue();
									}
								}
							}
						}
						
						//Multiplico (Valor de la autorización ) X (Cantidad de Montos * Porcentaje)
						valorMontoCalculado= (sumatoriaValorTarifa *
								(autorizacionEntSubCapita.getMontoCobroAutorizacion().getCantidadMonto()*autorizacionEntSubCapita.getMontoCobroAutorizacion().getPorcentajeMonto()))/100;
					}
					autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMontoCalculado(valorMontoCalculado);
				}
				else{//El monto es detallado
					//Realizo el calculo del monto detallado definido en el DCU 1001
					BusquedaMontosCobroDto montoCobro = new BusquedaMontosCobroDto();
					montoCobro.setDetalleCodigo(autorizacionEntSubCapita.getMontoCobroAutorizacion().getCodDetalleMonto());
					boolean esServicio=false;
					
					for (OrdenAutorizacionDto ordenAutorizar : autorizacionEntSubCapita.getOrdenesAutorizar()) {
						//Verifico si la orden a autorizar es de servicios
						if (ordenAutorizar.getServiciosPorAutorizar()!=null && !ordenAutorizar.getServiciosPorAutorizar().isEmpty()){
							esServicio= true;
							valorMontoCalculado = catalogoFacturacionMundo.valorCobrarPacienteServicios(montoCobro, ordenAutorizar.getServiciosPorAutorizar());
						} else {//La orden autorizar es de Medicamentos/Insumos
							valorMontoCalculado = catalogoFacturacionMundo.valorCobrarPacienteMedicamentosInsumos(montoCobro, ordenAutorizar.getMedicamentosInsumosPorAutorizar());
						}
					}
					
					//Verifico si algun servicio/articulo no se pudo calcular el monto
					for (OrdenAutorizacionDto ordenAutorizar : autorizacionEntSubCapita.getOrdenesAutorizar()) {
						//Verifico si la orden a autorizar es de servicios
						if (esServicio){
							for (ServicioAutorizacionOrdenDto serviciosAutorizar : ordenAutorizar.getServiciosPorAutorizar()) {
								if (!serviciosAutorizar.isAutorizado()){
									autorizacionEntSubCapita.setVerificarDetalleError(true);
								}
							}
						} else {//La orden autorizar es de Medicamentos/Insumos
							for (MedicamentoInsumoAutorizacionOrdenDto medicamentoAutorizar : ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
								if (!medicamentoAutorizar.isAutorizado()){
									autorizacionEntSubCapita.setVerificarDetalleError(true);
								}
							}
						}
					}
					
				}
				continuarCalculoMonto=false;
			}
			
			//verifico si se pudo calcular el monto de cobro detallado
			if (!esPyp){
			if(autorizacionEntSubCapita.getDatosPacienteAutorizar().isCuentaManejaMontos()){
					if(valorMontoCalculado != ConstantesBD.codigoNuncaValidoDoubleNegativo && !continuarCalculoMonto ){
						autorizacionEntSubCapita.getMontoCobroAutorizacion().setValorMontoCalculado(valorMontoCalculado);
						autorizacionEntSubCapita.setProcesoExitoso(true);
					} else { //No se genero el calculo del monto para la autorización
						autorizacionEntSubCapita.setVerificarDetalleError(true);
						autorizacionEntSubCapita.setProcesoExitoso(false);
						Log4JManager.info("NO SE GENERO EL VALOR DEL CALCULO DEL MONTO DETALLADO. PROCESO DE AUTORIZACIÓN DE CAPITACIÓN CANCELADO");
					}
				}
			}
			
			//Si la orden a autorizar es de Cx valido si al menos para 1 servicio no se pudo calcular tarifa, en este caso
			//se debe cancelar proceso para todos los servicios
			if(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia){
				for (OrdenAutorizacionDto ordenAutorizar : autorizacionEntSubCapita.getOrdenesAutorizar()) {
					for (ServicioAutorizacionOrdenDto serviciosConMonto : ordenAutorizar.getServiciosPorAutorizar()) {
						if(!serviciosConMonto.isAutorizado()){
							autorizacionEntSubCapita.setProcesoExitoso(false);
						}
					}
				}
			}
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}

	
	/**
	 * Método que calcula la fecha de vencimiento de la autorización 
	 * @param listaAticulosAutorizar
	 * @param listaServiciosAutorizar
	 * @param codInstitucion
	 * @throws IPSException 
	 */
	private Date calcularFechaVencimientoAutorAutomaticas(List<OrdenAutorizacionDto> listaOrdenesAutorizar, int codInstitucion) throws IPSException{
		
		int diasCalculoVigencia=0;
		Date fechaVencimientoAutorizacion = new Date();
		String fechaAutorizacion = UtilidadFecha.getFechaActual();
		
		try{
				if (listaOrdenesAutorizar.get(0).getServiciosPorAutorizar() != null &&
						!listaOrdenesAutorizar.get(0).getServiciosPorAutorizar().isEmpty()){
					//Obtengo el valor del parametro general para calcular los dias de vigencia para la autorización de servicios
					String valorParametroDiasVigencia = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(codInstitucion);
					if(!UtilidadTexto.isEmpty(valorParametroDiasVigencia)){						
						diasCalculoVigencia = Integer.valueOf(valorParametroDiasVigencia);
					}else{
						diasCalculoVigencia = DIASVIGENCIA;
					}	
					fechaVencimientoAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
							UtilidadFecha.incrementarDiasAFecha(fechaAutorizacion, diasCalculoVigencia, false));
				}else{//La orden a autorizar es de MedicamentosInsumos
					//Obtengo el valor del parametro general para calcular los dias de vigencia para la autorización de MedicamentosInsumos
					String valorParametroDiasVigencia = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(codInstitucion);
					if(!UtilidadTexto.isEmpty(valorParametroDiasVigencia)){						
						diasCalculoVigencia = Integer.valueOf(valorParametroDiasVigencia);
					}else{
						diasCalculoVigencia = DIASVIGENCIA;
					}						

					fechaVencimientoAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
							UtilidadFecha.incrementarDiasAFecha(fechaAutorizacion, diasCalculoVigencia, false));
				}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
		return fechaVencimientoAutorizacion;
	}

	/**
	 * Método que permite borrar los registros de la tabla de cierre temporal donde la fecha
	 * de cierre sea anterior a la fecha del sistema
	 * @return
	 * @throws IPSException 
	 */
	public void eliminarAcumuladoCierreTemporal(int codContrato) throws IPSException{
		
		try{
			DTOBusquedaCierreTemporalServicio dtoParametrosServicios = null ;
			DTOBusquedaCierreTemporalArticulo dtoParametrosArticulos = null;
			ICierreTempServArtServicio cierreServArtServicio = CapitacionFabricaServicio.crearCierreTempServArtServicio();
			ICierreTempNivelAtenServServicio cierreNivelAtenServServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenServServicio();
			ICierreTempGrupoServicioServicio cierreGrupoServServicio = CapitacionFabricaServicio.crearCierreTempGrupoServicioServicio();
			ICierreTempNivelAteGruServServicio cierreNivelGrupoSerServicio = CapitacionFabricaServicio.crearCierreTempNivelAteGruServServicio();
			
			ICierreTempClaseInvArtMundo cierreClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempClaseInvArtMundo();
			ICierreTempNivelAtenArtServicio cierreNivelArticuloServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenArtServicio();
			ICierreTempNivelAteClaseInvArtMundo cierreNivelAteClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
			
			String fechaActual = UtilidadFecha.getFechaActual();
			dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
			dtoParametrosServicios.setCodigoContrato(codContrato);
			dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();
			dtoParametrosArticulos.setCodigoClaseInventario(codContrato);
			dtoParametrosArticulos.setCodigoClaseInventario(ConstantesBD.codigoNuncaValido);
			
			ArrayList<CierreTempServArt> cierreServicioArticulo = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
			ArrayList<CierreTempNivelAtenServ> cierreNivelServicio = cierreNivelAtenServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
			ArrayList<CierreTempGrupoServicio> cierreGrupoServicio = cierreGrupoServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
			ArrayList<CierreTempNivelAteGruServ> cierreNivelGrupoServicio =  cierreNivelGrupoSerServicio.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametrosServicios);
			
			ArrayList<CierreTempClaseInvArt> cierreClaseInvArticulo = cierreClaseInvArticuloMundo.buscarCierreTemporalClaseInventarioArticulo(dtoParametrosArticulos);
			ArrayList<CierreTempNivelAtenArt> cierreNivelAtencionArticulo = cierreNivelArticuloServicio.buscarCierreTemporalNivelAtencion(dtoParametrosArticulos);
			ArrayList<CierreTempNivAteClInvArt> cierreNivelAteClaseInvArticulo =  cierreNivelAteClaseInvArticuloMundo.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametrosArticulos);

			//Se eliminan los registros de los acumulados de las tablas temporales,
			//donde la fecha de cierre sea anterior a la fecha actual
			if(!Utilidades.isEmpty(cierreServicioArticulo)){
				for (CierreTempServArt cierre : cierreServicioArticulo) {
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
						cierreServArtServicio.eliminarRegistro(cierre);
					}
						
				}
			}
			
			if(!Utilidades.isEmpty(cierreNivelServicio)){
				for (CierreTempNivelAtenServ cierre : cierreNivelServicio) {
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
						cierreNivelAtenServServicio.eliminarRegistro(cierre);
					}
						
				}
			}
			
			if(!Utilidades.isEmpty(cierreGrupoServicio)){
				for (CierreTempGrupoServicio cierre : cierreGrupoServicio) {
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
						cierreGrupoServServicio.eliminarRegistro(cierre);
					}
						
				}
			}	
			
			if(!Utilidades.isEmpty(cierreNivelGrupoServicio)){
				for (CierreTempNivelAteGruServ cierre : cierreNivelGrupoServicio) {
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
						cierreNivelGrupoSerServicio.eliminarRegistro(cierre);
					}
						
				}
			}	
			
			if(!Utilidades.isEmpty(cierreClaseInvArticulo)){
				for (CierreTempClaseInvArt cierre : cierreClaseInvArticulo) {
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
						cierreClaseInvArticuloMundo.eliminarRegistro(cierre);
					}
						
				}
			}
			
			if(!Utilidades.isEmpty(cierreNivelAtencionArticulo)){
				for (CierreTempNivelAtenArt cierre : cierreNivelAtencionArticulo) {
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
						cierreNivelArticuloServicio.eliminarRegistro(cierre);
					}
						
				}
			}
			
			if(!Utilidades.isEmpty(cierreNivelAteClaseInvArticulo)){
				for (CierreTempNivAteClInvArt cierre : cierreNivelAteClaseInvArticulo) {
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(
							UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
						cierreNivelAteClaseInvArticuloMundo.eliminarRegistro(cierre);
					}
						
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
		
	/**
	 * 
	 * @param autorizacionCapitacion
	 * @throws Exception 
	 */
	public void generarAcumuladoCierreTemporal(AutorizacionCapitacionDto autorizacionCapitacion) throws IPSException{
		
		try{
			ICierreTempServArtServicio cierreServArtServicio = CapitacionFabricaServicio.crearCierreTempServArtServicio();
			ICierreTempNivelAtenServServicio cierreNivelAtenServServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenServServicio();
			ICierreTempGrupoServicioServicio cierreGrupoServServicio = CapitacionFabricaServicio.crearCierreTempGrupoServicioServicio();
			ICierreTempNivelAteGruServServicio cierreNivelGrupoSerServicio = CapitacionFabricaServicio.crearCierreTempNivelAteGruServServicio();
			
			ICierreTempClaseInvArtMundo cierreClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempClaseInvArtMundo();
			ICierreTempNivelAtenArtServicio cierreNivelArticuloServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenArtServicio();
			ICierreTempNivelAteClaseInvArtMundo cierreNivelAteClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
			IServiciosServicio servServicio = FacturacionServicioFabrica.crearServiciosServicio();
			IArticuloDAO articuloDAO = InventarioDAOFabrica.crearArticuloDAO();
			IClaseInventarioDAO claseInventarioDAO = InventarioDAOFabrica.crearClaseInventarioDAO();
					
			DTOBusquedaCierreTemporalServicio dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
			DTOBusquedaCierreTemporalArticulo dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();		
			BigDecimal valorTotal= BigDecimal.ZERO;
			Servicios servicio =null;		
			long cantidadSolicitada=1;
			ArrayList<CierreTempNivelAteGruServ> listaCierreGrupoNivelServicio =null;
			CierreTempNivelAteGruServ cierreGrupoNivelServicio =null;
			ArrayList<CierreTempGrupoServicio> listaCierreGrupoServicio = null;
			CierreTempGrupoServicio cierreGrupoServicio = null;
			ArrayList<CierreTempNivelAtenServ> listaCierreNivelServicio = null;
			CierreTempNivelAtenServ cierreNivelServicio = null;
			ArrayList<CierreTempServArt> listaCierreServArt=null;
			CierreTempServArt cierreServArt = null;
			DtoArticulos dtoArt = null;		
			ArrayList<CierreTempClaseInvArt> listaCierreClaseInvArt =null;
			CierreTempClaseInvArt cierreClaseInvArt =null;
			ArrayList<CierreTempNivelAtenArt> listaCierreNivelAtencion=null;
			CierreTempNivelAtenArt cierreNivelAtencion=null;
			ArrayList<CierreTempNivAteClInvArt> listaCierreNivelAteClaseInvArticulo =null;
			CierreTempNivAteClInvArt cierreNivelAteClaseInvArticulo =null;
			
			for (OrdenAutorizacionDto ordenAutorizada : autorizacionCapitacion.getOrdenesAutorizar()) {
				//Valido si son servicios a autorizados
				if (ordenAutorizada.getServiciosPorAutorizar() != null && !ordenAutorizada.getServiciosPorAutorizar().isEmpty()){
					for (ServicioAutorizacionOrdenDto serviciosAutorizados : ordenAutorizada.getServiciosPorAutorizar()) {
						//Verifico cuales son los servicios que se autorizan
						if (serviciosAutorizados.isAutorizado()){
							//Valido que los servicios 
							dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();					
							servicio = servServicio.obtenerServicioPorId(serviciosAutorizados.getCodigo());
							dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoSiChar);
							dtoParametrosServicios.setCodigoContrato(ordenAutorizada.getContrato().getCodigo());					
								
							listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
							if(Utilidades.isEmpty(listaCierreServArt)){
								Contratos contrato = new Contratos();						
								cierreServArt = new CierreTempServArt();
								contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
								cierreServArt.setContratos(contrato);
								cierreServArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreServArt.setValorAcumulado(0L);
								cierreServArt.setCierreServicio(ConstantesBD.acronimoSiChar);
							}else{
								cierreServArt = listaCierreServArt.get(0);
							}
															
							if(serviciosAutorizados.getCantidad()>1){
								cantidadSolicitada = serviciosAutorizados.getCantidad();
							}
							Log4JManager.info("");
							valorTotal = serviciosAutorizados.getValorTarifa().multiply(
									new BigDecimal(cantidadSolicitada));
							cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado()+ (valorTotal.doubleValue()));
							cierreServArtServicio.attachDirty(cierreServArt);					
							dtoParametrosServicios.setCodigoNivelAtencion(servicio.getNivelAtencion().getConsecutivo());					
							listaCierreNivelServicio = cierreNivelAtenServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
							if(Utilidades.isEmpty(listaCierreNivelServicio)){
								Contratos contrato = new Contratos();						
								cierreNivelServicio = new CierreTempNivelAtenServ();
								NivelAtencion nivelAtencion = new NivelAtencion();
								contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
								cierreNivelServicio.setContratos(contrato);
								cierreNivelServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreNivelServicio.setValorAcumulado(0L);						
								nivelAtencion.setConsecutivo(servicio.getNivelAtencion().getConsecutivo());
								cierreNivelServicio.setNivelAtencion(nivelAtencion);
							}else{
								cierreNivelServicio = listaCierreNivelServicio.get(0);
							}
								
							cierreNivelServicio.setValorAcumulado(cierreNivelServicio.getValorAcumulado() +
									valorTotal.doubleValue());
							cierreNivelAtenServServicio.sincronizarCierreTemporal(cierreNivelServicio);
							dtoParametrosServicios.setCodigoGrupoServicio(servicio.getGruposServicios().getCodigo());
							listaCierreGrupoServicio = cierreGrupoServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
							
							if(Utilidades.isEmpty(listaCierreGrupoServicio)){
								Contratos contrato = new Contratos();						
								cierreGrupoServicio = new CierreTempGrupoServicio();
								GruposServicios grupoServicio = new GruposServicios();
								contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
								cierreGrupoServicio.setContratos(contrato);
								cierreGrupoServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreGrupoServicio.setValorAcumulado(0L);						
								grupoServicio.setCodigo(servicio.getGruposServicios().getCodigo());
								cierreGrupoServicio.setGruposServicios(grupoServicio);
							}else{
								cierreGrupoServicio=listaCierreGrupoServicio.get(0);
							}
							cierreGrupoServicio.setValorAcumulado(cierreGrupoServicio.getValorAcumulado() + 
									valorTotal.doubleValue());
							cierreGrupoServServicio.sincronizarCierreTemporal(cierreGrupoServicio);
							dtoParametrosServicios.setCodigoGrupoServicio(servicio.getGruposServicios().getCodigo());
							listaCierreGrupoNivelServicio = 
								cierreNivelGrupoSerServicio.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametrosServicios);
							
							if(Utilidades.isEmpty(listaCierreGrupoNivelServicio)){
								Contratos contrato = new Contratos();						
								cierreGrupoNivelServicio = new CierreTempNivelAteGruServ();
								NivelAtencion nivelAtencion = new NivelAtencion();
								GruposServicios grupoServicio = new GruposServicios();
								contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
								nivelAtencion.setConsecutivo(servicio.getNivelAtencion().getConsecutivo());
								grupoServicio.setCodigo(servicio.getGruposServicios().getCodigo());
								cierreGrupoNivelServicio.setContratos(contrato);
								cierreGrupoNivelServicio.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreGrupoNivelServicio.setValorAcumulado(0L);
								cierreGrupoNivelServicio.setNivelAtencion(nivelAtencion);
								cierreGrupoNivelServicio.setGruposServicios(grupoServicio);
							}else{
								cierreGrupoNivelServicio = listaCierreGrupoNivelServicio.get(0);
							}
							cierreGrupoNivelServicio.setValorAcumulado(cierreGrupoNivelServicio.getValorAcumulado()+ 
									valorTotal.doubleValue());
							cierreNivelGrupoSerServicio.sincronizarCierreTemporal(cierreGrupoNivelServicio);
							cantidadSolicitada=1;
						}
					}
				}else{//Son medicamentos
					
					if(ordenAutorizada.getMedicamentosInsumosPorAutorizar() != null){
					for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizado : ordenAutorizada.getMedicamentosInsumosPorAutorizar()) {
						
						//Verifico caules de los MedicamentosInsumos se deben autorizar
						if (medicamentoInsumoAutorizado.isAutorizado()){
							dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();
							dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
							DtoClaseInventario dtoClaseInventario = new DtoClaseInventario();
							
							dtoArt = articuloDAO.consultarArticuloPorID(medicamentoInsumoAutorizado.getCodigo());
							dtoClaseInventario = claseInventarioDAO.obtenerClaseInventarioPorSungrupo(dtoArt.getCodigoSubGrupoArticulo());
							medicamentoInsumoAutorizado.setSubGrupoInventario(dtoArt.getCodigoSubGrupoArticulo());
							medicamentoInsumoAutorizado.setClaseInventario(dtoClaseInventario.getCodigo());
							medicamentoInsumoAutorizado.setNombreClaseInventario(dtoClaseInventario.getNombre());
							
							dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoNoChar);
							dtoParametrosServicios.setCodigoContrato(ordenAutorizada.getContrato().getCodigo());
							
							listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
							
							if(Utilidades.isEmpty(listaCierreServArt)){
								Contratos contrato = new Contratos();						
								cierreServArt = new CierreTempServArt();
								
								contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
								cierreServArt.setContratos(contrato);
								cierreServArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
								cierreServArt.setValorAcumulado(0L);		
								cierreServArt.setCierreServicio(ConstantesBD.acronimoNoChar);
							}else{
								cierreServArt = listaCierreServArt.get(0);
							}
														
							if(medicamentoInsumoAutorizado.getCantidad()>1){
								cantidadSolicitada = medicamentoInsumoAutorizado.getCantidad();
							}
							valorTotal = medicamentoInsumoAutorizado.getValorTarifa().multiply(
									new BigDecimal(cantidadSolicitada));
							
							cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado()+ (valorTotal.doubleValue()));
							cierreServArtServicio.attachDirty(cierreServArt);		
								
							if(medicamentoInsumoAutorizado.getClaseInventario() != ConstantesBD.codigoNuncaValido){
							
								dtoParametrosArticulos.setCodigoContrato(ordenAutorizada.getContrato().getCodigo());
								dtoParametrosArticulos.setCodigoClaseInventario(medicamentoInsumoAutorizado.getClaseInventario());
								
								listaCierreClaseInvArt = cierreClaseInvArticuloMundo.buscarCierreTemporalClaseInventarioArticulo(dtoParametrosArticulos);
								
								if(Utilidades.isEmpty(listaCierreClaseInvArt)){
									Contratos contrato = new Contratos();						
									ClaseInventario claseInventario = new ClaseInventario();
									
									cierreClaseInvArt = new CierreTempClaseInvArt();
									
									contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
									claseInventario.setCodigo(medicamentoInsumoAutorizado.getClaseInventario());
															
									cierreClaseInvArt.setContratos(contrato);
									cierreClaseInvArt.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
									cierreClaseInvArt.setValorAcumulado(BigDecimal.ZERO);
									cierreClaseInvArt.setClaseInventario(claseInventario);
								}else{
									cierreClaseInvArt = listaCierreClaseInvArt.get(0);
								}
								
								cierreClaseInvArt.setValorAcumulado(cierreClaseInvArt.getValorAcumulado().add(valorTotal));
								cierreClaseInvArticuloMundo.sincronizarCierreTemporal(cierreClaseInvArt);
							}						
							
							if(dtoArt.getNivelAtencion()!=null && dtoArt.getNivelAtencion()!=ConstantesBD.codigoNuncaValidoLong){
								dtoParametrosArticulos.setCodigoNivelAtencion(dtoArt.getNivelAtencion());
								listaCierreNivelAtencion = cierreNivelArticuloServicio.buscarCierreTemporalNivelAtencion(dtoParametrosArticulos);
								
								if(Utilidades.isEmpty(listaCierreNivelAtencion)){
									Contratos contrato = new Contratos();						
									cierreNivelAtencion = new CierreTempNivelAtenArt();
									NivelAtencion nivelAtencion = new NivelAtencion();
									
									contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
									nivelAtencion.setConsecutivo(dtoArt.getNivelAtencion());
															
									cierreNivelAtencion.setContratos(contrato);
									cierreNivelAtencion.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
									cierreNivelAtencion.setValorAcumulado(0L);						
									cierreNivelAtencion.setNivelAtencion(nivelAtencion);
								}else{
									cierreNivelAtencion = listaCierreNivelAtencion.get(0);
								}
								
								cierreNivelAtencion.setValorAcumulado(cierreNivelAtencion.getValorAcumulado()+ valorTotal.doubleValue());
								
								cierreNivelArticuloServicio.sincronizarCierreTemporal(cierreNivelAtencion);
								
								if(medicamentoInsumoAutorizado.getClaseInventario() != ConstantesBD.codigoNuncaValido){
									listaCierreNivelAteClaseInvArticulo = 
										cierreNivelAteClaseInvArticuloMundo.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametrosArticulos);
									
									if(Utilidades.isEmpty(listaCierreNivelAteClaseInvArticulo)){
										Contratos contrato = new Contratos();
										ClaseInventario claseInventario = new ClaseInventario();
										NivelAtencion nivelAtencion = new NivelAtencion();
										cierreNivelAteClaseInvArticulo = new CierreTempNivAteClInvArt();
																		
										contrato.setCodigo(ordenAutorizada.getContrato().getCodigo());
										claseInventario.setCodigo(medicamentoInsumoAutorizado.getClaseInventario());
										nivelAtencion.setConsecutivo(dtoArt.getNivelAtencion());
																
										cierreNivelAteClaseInvArticulo.setContratos(contrato);
										cierreNivelAteClaseInvArticulo.setFechaCierre(UtilidadFecha.getFechaActualTipoBD());
										cierreNivelAteClaseInvArticulo.setValorAcumulado(BigDecimal.ZERO);
										cierreNivelAteClaseInvArticulo.setClaseInventario(claseInventario);							
										cierreNivelAteClaseInvArticulo.setNivelAtencion(nivelAtencion);
									}else{
										cierreNivelAteClaseInvArticulo = listaCierreNivelAteClaseInvArticulo.get(0);
									}
										
									cierreNivelAteClaseInvArticulo.setValorAcumulado(cierreNivelAteClaseInvArticulo.getValorAcumulado().add(valorTotal));
									
									cierreNivelAteClaseInvArticuloMundo.sincronizarCierreTemporal(cierreNivelAteClaseInvArticulo);
								}	
							}
							cantidadSolicitada=1;
						}
					}
				}
			}
			}
		}catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param autorizacionEntSubCapita
	 * @throws IPSException
	 */
		
	public void guardarProcesoAutorizacion(Connection con, AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException{
		
		try{
			List<OrdenAutorizacionDto> ordenesAutorizar=null;
			AutorizacionesEntidadesSub autorizacionesEntidadesSub = null;
			AutorizacionesCapitacionSub autorizacionCapiSub = null;
			String tipoEntEjecuta="";
			boolean esServicio=false;
			boolean requiereTransaccion = false;
			ordenesAutorizar = new ArrayList<OrdenAutorizacionDto>();
			//Verifico cuales ordenes tienen servicios o medicamentos/insumos que se autorizan
          	for (OrdenAutorizacionDto ordenAutorizar  : autorizacionEntSubCapita.getOrdenesAutorizar()) {
				//Indico que la orden que se autoriza no es migrada de una orden ambulatoria
				ordenAutorizar.setMigrado(ConstantesBD.acronimoNoChar);
				tipoEntEjecuta = ordenAutorizar.getTipoEntidadEjecuta();
				//Verifico si la autorización es de servicios
				if (ordenAutorizar.getServiciosPorAutorizar() != null &&
						!ordenAutorizar.getServiciosPorAutorizar() .isEmpty()){
					esServicio=true;
					for (ServicioAutorizacionOrdenDto servicioAutorizar : ordenAutorizar.getServiciosPorAutorizar()) {
						//Verifico cuales servicios se deben autorizar
						if (servicioAutorizar.isAutorizado()){
							ordenesAutorizar.add(ordenAutorizar);
							break;
						}
					}
				}else{//La autorización es de medicamentosInsumos
					for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumo : ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
						//Verifico cuales medicamentosInsumos se deben autorizar
						if (medicamentoInsumo.isAutorizado()){
							ordenesAutorizar.add(ordenAutorizar);
							break;
						}
					}
				}
			}
			
			//Verifico si hay ordenes a las que se pueda generar autorización
			if (ordenesAutorizar != null &&
					!ordenesAutorizar.isEmpty()){
			//Genero la autorización de Entidad Subcontratada
			autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
			autorizacionesEntidadesSub = generarAutorEntSub(con, autorizacionEntSubCapita);
			
			//Genero el asocio de la autorización con las ordenes medicas, ambulatorias, cargos directos y peticiones
			for (OrdenAutorizacionDto ordenAutorizacion : ordenesAutorizar) {
				
				//Valido que la autorización no sea para servicios medicamentos e insumos de ingreso estancia
				if (!autorizacionEntSubCapita.isAutoServArtIngresoEstancia()){
					//Genero el asocio de la autorización con la orden medica
					if (ordenAutorizacion.getClaseOrden() == ConstantesBD.claseOrdenOrdenMedica &&
								ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudInterconsulta ||
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia ||
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCita ||
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudProcedimiento ||
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudMedicamentos){ 
						asociarSolicitudAutorizacion(ordenesAutorizar, autorizacionesEntidadesSub);
							asociarDetalleSolicitudesAutorizaciones(ordenesAutorizar, autorizacionesEntidadesSub, requiereTransaccion);
							break;
					}
					
					//Genero el asocio de la autorización con los cargos directos
					if (ordenAutorizacion.getClaseOrden() == ConstantesBD.claseOrdenCargoDirecto &&
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia ||
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos){
						asociarSolicitudAutorizacion(ordenesAutorizar, autorizacionesEntidadesSub);
							asociarDetalleSolicitudesAutorizaciones(ordenesAutorizar, autorizacionesEntidadesSub, requiereTransaccion);	
							break;
					}
					
					//Genero el asocio de la autorización con la solicitud en caso de ser de estancia automatica
					if (ordenAutorizacion.getClaseOrden() == ConstantesBD.claseOrdenCargoDirecto &&
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCargosDirectosServicios ||
							ordenAutorizacion.getTipoOrden() == ConstantesBD.codigoTipoSolicitudEstancia){
						asociarSolicitudAutorizacion(ordenesAutorizar, autorizacionesEntidadesSub);
						break;
					}
					
					//Genero el asocio de la autorización con la orden ambulatoria
					if (ordenAutorizacion.getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria){
						asociarOrdenAmbulatoriaAutorizacion(ordenesAutorizar, autorizacionesEntidadesSub);
						asociarDetalleOrdenAmbulatoriaAutorizaciones(ordenesAutorizar, autorizacionesEntidadesSub);
						break;
					}
					
					//Genero el asocio de la autorización con la petición
					if (ordenAutorizacion.getClaseOrden() == ConstantesBD.claseOrdenPeticion){
						asociarPeticionAutorizacion(ordenesAutorizar, autorizacionesEntidadesSub);
						asociarDetallePeticionAutorizaciones(ordenesAutorizar, autorizacionesEntidadesSub);
						break;
					}
				}
			}
			
			//Genero la autorización de capitación subcontrada
			autorizacionCapiSub = new AutorizacionesCapitacionSub();
			autorizacionCapiSub = generarAutorizacionCapitacionSubcontratada(con, autorizacionEntSubCapita, autorizacionesEntidadesSub, tipoEntEjecuta);
			
			//En caso de que la autorización sea de servicios medicamentos e insumos de ingreso estancia se debe asociar 
			//la autorización 
			if (autorizacionEntSubCapita.isAutoServArtIngresoEstancia()){
				generarAutorizacionSerArtIngresoEstancia(autorizacionEntSubCapita, autorizacionCapiSub);
			}
			
			//Genero el asocio de la autorización de Entidad subcontrada con los Servicios/MedicamentosInsumos que se autorizan
			if (esServicio){
				generarAutorizacionEntSubServicios(autorizacionEntSubCapita, autorizacionesEntidadesSub);
			} else {
				generarAutorizacionEntSubArticulos(autorizacionEntSubCapita, autorizacionesEntidadesSub);
			}
			
			
			//En caso de que la autorización sea de servicios medicamentos e insumos de ingreso estancia no se debe generar
			//el registro de montos para la autorización.
			if (!autorizacionEntSubCapita.isAutoServArtIngresoEstancia()){
				//Genero el asocio de la autorización con el monto de cobro generado
				generarAutorizacionEntSubMontos(autorizacionEntSubCapita, autorizacionesEntidadesSub);
			}
			
			//Genero el asocio de la autorización con el centro de costo que responde o ejecuta la autorización
			if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria){
				//verifico si es una orden ambulatoria de Medicamentos/Insumos
				if (autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica)){
					if(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getMedicamentosInsumosPorAutorizar() != null &&
							!autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getMedicamentosInsumosPorAutorizar().isEmpty()){
						for (DtoCentroCosto centroCosto : autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic()) {
							generarAutorizacionEntSubCentoCosto(centroCosto.getCodigoCentroCosto(), autorizacionesEntidadesSub);
						} 
					}
					else{
						generarAutorizacionEntSubCentoCosto(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getCodigoCentroCostoEjecuta(), autorizacionesEntidadesSub);
					}
				}else{
					generarAutorizacionEntSubCentoCosto(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getCodigoCentroCostoEjecuta(), autorizacionesEntidadesSub);
				}
			}else{
				generarAutorizacionEntSubCentoCosto(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getCodigoCentroCostoEjecuta(), autorizacionesEntidadesSub);
			}
			
			
			//Genero el historico de la autorización
			generarAutorizacionEntSubHistorico(autorizacionEntSubCapita, autorizacionesEntidadesSub, autorizacionCapiSub,false);
		}
			
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
			
		
	}
	
	/**
	 * Método que genera la autorización de Entidad Subcontratada
	 * @param con
	 * @param autorizacionEntSubCapita
	 * @return
	 * @throws IPSException
	 */

	private AutorizacionesEntidadesSub generarAutorEntSub(Connection con, AutorizacionCapitacionDto autorizacionEntSubCapita) throws IPSException
	{
		AutorizacionesEntidadesSub autorizacionesEntidadesSub = null;
		try{
			AutorizacionesDelegate autorizacionesDelegate = null;
			EntidadesSubcontratadas entidadSubcontratada = new EntidadesSubcontratadas();
			Convenios convenio = new Convenios();
			Instituciones institucion = new Instituciones();
			Pacientes paciente = new Pacientes();
			String consecutivo="";
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
			String horaActual = UtilidadFecha.getHoraActual();
			
			//Si la autorización es automática para una orden ambulatoria o petición no se genera consecutivo ya que funcionalmente no se debe 
			//generar autorización
			if (autorizacionEntSubCapita.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutomatica)){
				if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getClaseOrden() != ConstantesBD.claseOrdenOrdenAmbulatoria &&
						autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getClaseOrden() != ConstantesBD.claseOrdenPeticion){
					
					//Obtengo el consecutivo de Entidad Subcontratada
					consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAutorizacionEntiSub, autorizacionEntSubCapita.getCodigoInstitucion()); 
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAutorizacionEntiSub, 
							Integer.valueOf(autorizacionEntSubCapita.getCodigoInstitucion()), 
							consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					
					autorizacionEntSubCapita.setConsecutivoAutorizacion(consecutivo);
				}
			}else{
				//Obtengo el consecutivo de Entidad Subcontratada
				consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAutorizacionEntiSub, autorizacionEntSubCapita.getCodigoInstitucion()); 
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAutorizacionEntiSub, 
						Integer.valueOf(autorizacionEntSubCapita.getCodigoInstitucion()), 
						consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				
				autorizacionEntSubCapita.setConsecutivoAutorizacion(consecutivo);
			}
			
			//Envio la entidad Subcontratada
			if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() == 0){
				entidadSubcontratada.setCodigoPk(ConstantesBD.codigoNuncaValidoLong);
			}else {
			entidadSubcontratada.setCodigoPk(autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada());
			}
			
			//Obtengo el convenio de la cobertura para la orden
			convenio.setCodigo(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getContrato().getConvenio().getCodigo());
			
			//Obtengo la institución
			institucion.setCodigo(autorizacionEntSubCapita.getCodigoInstitucion());
			
			//Obtengo el paciente
			paciente.setCodigoPaciente(autorizacionEntSubCapita.getDatosPacienteAutorizar().getCodigoPaciente());
			autorizacionesDelegate= new AutorizacionesDelegate();
			
			//Envio información para la autorización a la entidad autorizacionesEntidadesSub	 
			autorizacionesEntidadesSub=new AutorizacionesEntidadesSub();
			autorizacionesEntidadesSub.setFechaAutorizacion(fechaActual);
			autorizacionesEntidadesSub.setHoraAutorizacion(horaActual);
			autorizacionesEntidadesSub.setEntidadesSubcontratadas(entidadSubcontratada);
			autorizacionesEntidadesSub.setConsecutivoAutorizacion(consecutivo);
			autorizacionesEntidadesSub.setConvenios(convenio);
			autorizacionesEntidadesSub.setFechaVencimiento(autorizacionEntSubCapita.getFechaVencimientoAutorizacion());
			autorizacionesEntidadesSub.setObservaciones(autorizacionEntSubCapita.getObservacionesGenerales());
			
			if (autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic() != null &&
					!autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic().isEmpty()){

				if (autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic().get(0).getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos)){
					//Verifico si la entidad que autoriza es la misma definida en el parametro Entidad Subcontratada para Centro de Costos Internos
					String entidadSubCentCostInterno=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(autorizacionEntSubCapita.getCodigoInstitucion());
					String[] entidad=entidadSubCentCostInterno.split("-");					
					Long entidadSubcontratadaInterna = Long.parseLong(entidad[0].trim());
					if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()==entidadSubcontratadaInterna){
						autorizacionesEntidadesSub.setTipo(ConstantesIntegridadDominio.acronimoInterna);
					}else{
						autorizacionesEntidadesSub.setTipo(ConstantesIntegridadDominio.acronimoExterna);
					}
				} else {
					autorizacionesEntidadesSub.setTipo(autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic().get(0).getTipoEntidadEjecuta());
				}
			} else{
				if (autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos)){
					//Verifico si la entidad que autoriza es la misma definida en el parametro Entidad Subcontratada para Centro de Costos Internos
					String entidadSubCentCostInterno=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(autorizacionEntSubCapita.getCodigoInstitucion());
					String[] entidad=entidadSubCentCostInterno.split("-");					
					Long entidadSubcontratadaInterna = Long.parseLong(entidad[0].trim());
					if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()==entidadSubcontratadaInterna){
						autorizacionesEntidadesSub.setTipo(ConstantesIntegridadDominio.acronimoInterna);
					}else{
						autorizacionesEntidadesSub.setTipo(ConstantesIntegridadDominio.acronimoExterna);
					}
				} else {
					autorizacionesEntidadesSub.setTipo(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getTipoEntidadEjecuta());
				}
			}
			
			autorizacionesEntidadesSub.setEstado(ConstantesIntegridadDominio.acronimoAutorizado);
			autorizacionesEntidadesSub.setContabilizado(ConstantesBD.acronimoNo);
			autorizacionesEntidadesSub.setContabilizadoAnulacion(ConstantesBD.acronimoNo);
			autorizacionesEntidadesSub.setFechaModificacion(fechaActual);
			autorizacionesEntidadesSub.setHoraModificacion(horaActual);
			autorizacionesEntidadesSub.setUsuarioModificacion(autorizacionEntSubCapita.getLoginUsuario());
			autorizacionesEntidadesSub.setInstituciones(institucion);
			
			
			if (autorizacionEntSubCapita.getCentroAtencion() != 0){
				CentroAtencion centroAtencion = new CentroAtencion();
				centroAtencion.setConsecutivo(autorizacionEntSubCapita.getCentroAtencion());
				autorizacionesEntidadesSub.setCentroAtencion(centroAtencion);
			}
			
			autorizacionesEntidadesSub.setPacientes(paciente);
			autorizacionesDelegate.generarAutorizacionEntidadSub(autorizacionesEntidadesSub);
			autorizacionEntSubCapita.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
			autorizacionEntSubCapita.setFechaAutorizacion(fechaActual);
			
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
		return autorizacionesEntidadesSub;
		
	}
	
	
	/**
	 * Método que permite asociar la autorización de entidad subcontratada a la solicitud por la cual se genera dicha 
	 * autorización
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException 
	 */
	public void asociarSolicitudAutorizacion(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException
	{
		try{
			AutorizacionesDelegate autorizacionesDelegate = null;
			AutoEntsubSolicitudes autoEntsubSolicitudes	= null;
			Solicitudes solicitudes = null;
			
			for (OrdenAutorizacionDto ordenAutorizar : ordenesAutorizar) {
				solicitudes	=new Solicitudes();
				autoEntsubSolicitudes=new AutoEntsubSolicitudes();
				autorizacionesDelegate=new AutorizacionesDelegate();
				//Obtengo el codigo de la orden
				solicitudes.setNumeroSolicitud(ordenAutorizar.getCodigoOrden().intValue());
				//Envio información para el asocio de la orden a la entidad autoEntsubSolicitudes
				autoEntsubSolicitudes.setSolicitudes(solicitudes);
				autoEntsubSolicitudes.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
				autoEntsubSolicitudes.setMigrado(ordenAutorizar.getMigrado());
				//Genero el asocio de la autorización con la orden médica
				autorizacionesDelegate.generarAutoEntSubSolicitudes(autoEntsubSolicitudes);
			}
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite asociar al detalle de la solicitud la autorización de capitación subcontratada
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException 
	 */
	public void asociarDetalleSolicitudesAutorizaciones(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub, boolean requiereTransaccion) throws IPSException{
		
		try{
			ISolicitudesMundo solicitudesMundo	= new SolicitudesMundo();
			for (OrdenAutorizacionDto ordenAutorizacionDto : ordenesAutorizar) {
				//Verifico si la autorización es de servicios
				if (ordenAutorizacionDto.getServiciosPorAutorizar()!=null &&
						!ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()){
					for (ServicioAutorizacionOrdenDto servicioAutorizacionDto : ordenAutorizacionDto.getServiciosPorAutorizar()){
						if (servicioAutorizacionDto.isAutorizado()){
							solicitudesMundo.asociarSolicitudesAutorizaciones(autorizacionesEntidadesSub, ordenAutorizacionDto.getTipoOrden(), 
									servicioAutorizacionDto.getCodigo(), ordenAutorizacionDto.getCodigoOrden().intValue(), requiereTransaccion);
						}
					}
						
				}
				else{//La autorización es de medicamentos
					for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizacionDto : ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()){
						if (medicamentoInsumoAutorizacionDto.isAutorizado()){
							solicitudesMundo.asociarSolicitudesAutorizaciones(autorizacionesEntidadesSub, ordenAutorizacionDto.getTipoOrden(), 
									medicamentoInsumoAutorizacionDto.getCodigo(), ordenAutorizacionDto.getCodigoOrden().intValue(), requiereTransaccion);
						}
					}
						
				}
			}
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	/**
	 * Método que actualiza la autorizacion de Entidad Subcontratada con el consecutivo del parametro general
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	private void actualizarConsecutivoAutorizacionesEntidadesSub(long consecutivo,String consecutivoAutorizacion)throws IPSException
	{
		AutorizacionesDelegate delegate	=null;
		AutorizacionesEntidadesSub  autorizacionesEntidadesSub= null; 
		try {
			delegate			= new AutorizacionesDelegate();
			autorizacionesEntidadesSub = delegate.obtenerAutorizacionEntidadSub(consecutivo);
			autorizacionesEntidadesSub.setConsecutivoAutorizacion(consecutivoAutorizacion);
			delegate.actualizarAutorizacionEntidadSub(autorizacionesEntidadesSub);
			
		}catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite asociar la orden ambulatoria con la autorización de capitación subcontratada
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void asociarOrdenAmbulatoriaAutorizacion(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException
	{
		try{
			AutorizacionesDelegate autorizacionesDelegate = null;
			AutoEntsubOrdenambula autoEntSubOrdenAmbula	= null;
			OrdenesAmbulatorias ordenAmbulatoria =null;
			
			for (OrdenAutorizacionDto ordenAutorizar : ordenesAutorizar) {
				ordenAmbulatoria = new OrdenesAmbulatorias();
				autoEntSubOrdenAmbula=new AutoEntsubOrdenambula();
				autorizacionesDelegate=new AutorizacionesDelegate();
				//Obtengo el codigo de la orden
				ordenAmbulatoria.setCodigo(ordenAutorizar.getCodigoOrden());
				//Envio información para el asocio de la orden a la entidad autoEntsubSolicitudes
				autoEntSubOrdenAmbula.setOrdenesAmbulatorias(ordenAmbulatoria);
				autoEntSubOrdenAmbula.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
				//Genero el asocio de la autorización con la orden médica
				autorizacionesDelegate.generarAutorizacionAmbulatoria(autoEntSubOrdenAmbula);
			}
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	
	/**
	 * Método que permite asociar el detalle de la orden ambulatoria con la autorización de capitación subcontratada
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void asociarDetalleOrdenAmbulatoriaAutorizaciones(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException{
		
		try{
			IOrdenesAmbulatoriasMundo ordenAmbulatoriaMundo	= new OrdenesAmbulatoriasMundo();
			for (OrdenAutorizacionDto ordenAutorizacionDto : ordenesAutorizar) {
				//Verifico si la autorización es de servicios
				if (ordenAutorizacionDto.getServiciosPorAutorizar()!=null &&
						!ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()){
					for (ServicioAutorizacionOrdenDto servicioAutorizacionDto : ordenAutorizacionDto.getServiciosPorAutorizar()){
						if (servicioAutorizacionDto.isAutorizado()){
							ordenAmbulatoriaMundo.asociarOrdenAmbulatoriaAutorizaciones(autorizacionesEntidadesSub, 
									true, servicioAutorizacionDto.getCodigo(), ordenAutorizacionDto.getCodigoOrden(), false);
						}
					}
						
				}
				else{//La autorización es de medicamentos
					for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizacionDto : ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()){
						if (medicamentoInsumoAutorizacionDto.isAutorizado()){
							ordenAmbulatoriaMundo.asociarOrdenAmbulatoriaAutorizaciones(autorizacionesEntidadesSub, 
									false, medicamentoInsumoAutorizacionDto.getCodigo(), ordenAutorizacionDto.getCodigoOrden(), false);
						}
					}
				}
			}
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite asociar la petición con la autorización de capitación subcontratada
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void asociarPeticionAutorizacion(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException
	{
		try{
			AutorizacionesDelegate autorizacionesDelegate = null;
			AutoEntsubPeticiones autoEntSubPeticion	= null;
			PeticionQx peticionQx = null;
			
			for (OrdenAutorizacionDto ordenAutorizar : ordenesAutorizar) {
				autoEntSubPeticion = new AutoEntsubPeticiones();
				autorizacionesDelegate = new AutorizacionesDelegate();
				peticionQx = new PeticionQx();
				//Obtengo el codigo de la orden
				peticionQx.setCodigo(ordenAutorizar.getCodigoOrden().intValue());
				//Envio información para el asocio de la orden a la entidad autoEntsubSolicitudes
				autoEntSubPeticion.setPeticionQx(peticionQx);
				autoEntSubPeticion.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
				//Genero el asocio de la autorización con la orden médica
				autorizacionesDelegate.generarAutorizacionPeticion(autoEntSubPeticion);
			}
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	
	/**
	 * Método que permite asociar el detalle de la petición con la autorización de capitación subcontratada
	 * @param ordenesAutorizar
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void asociarDetallePeticionAutorizaciones(List<OrdenAutorizacionDto> ordenesAutorizar, AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException{
		
		try{
			IPeticionesMundo peticionesMundo = new PeticionesMundo();
			for (OrdenAutorizacionDto ordenAutorizacionDto : ordenesAutorizar) {
				for (ServicioAutorizacionOrdenDto servicioAutorizacionDto : ordenAutorizacionDto.getServiciosPorAutorizar()){
					if (servicioAutorizacionDto.isAutorizado()){
						peticionesMundo.asociarServicioPeticionAutorizacion(autorizacionesEntidadesSub, ordenAutorizacionDto.getCodigoOrden().intValue(), servicioAutorizacionDto.getCodigo());
					}
				}
			}
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite generar la autorización de capitación subcontratada
	 * @param con
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub 
	 * @param tipoEntEjecuta
	 * @throws IPSException
	 */
	public AutorizacionesCapitacionSub generarAutorizacionCapitacionSubcontratada(Connection con, AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesEntidadesSub autorizacionesEntidadesSub, String tipoEntEjecuta) throws IPSException{
		
		try{
			
			AutorizacionesCapitacionSub autorizacionesCapitacionSub = null;
			AutorizacionesDelegate autorizacionesDelegate = null;
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
			String horaActual = UtilidadFecha.getHoraActual();
			Usuarios usuarioAutoriza = null;
			
			long consecutivo = ConstantesBD.codigoNuncaValidoLong;
			//Obtengo el consecutivo de capitación subcontratada
			String consecutivoCapitacion = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, Integer.valueOf(autorizacionEntSubCapita.getCodigoInstitucion()));
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, 
					ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, 
					Integer.valueOf(autorizacionEntSubCapita.getCodigoInstitucion()), 
					consecutivoCapitacion, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			consecutivo = Utilidades.convertirALong(consecutivoCapitacion);
			
			autorizacionesDelegate = new AutorizacionesDelegate();
			//Envio información para la autorización a la entidad autorizacionesEntidadesSub	 
			autorizacionesCapitacionSub = new  AutorizacionesCapitacionSub();
			autorizacionesCapitacionSub.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			autorizacionesCapitacionSub.setConsecutivo(consecutivo);
			autorizacionesCapitacionSub.setTipoAutorizacion(autorizacionEntSubCapita.getTipoAutorizacion());
			//Se asigna el convenio recobro
			if(autorizacionEntSubCapita.isConvenioRecobro() &&
					autorizacionEntSubCapita.getCodConvenioRecobro() != null &&
					autorizacionEntSubCapita.getCodConvenioRecobro() != ConstantesBD.codigoNuncaValido){
				Convenios convenioRecobro = new Convenios();
				convenioRecobro.setCodigo(autorizacionEntSubCapita.getCodConvenioRecobro());
				autorizacionesCapitacionSub.setConvenios(convenioRecobro);
			}
			autorizacionesCapitacionSub.setOtroConvenioRecobro(autorizacionEntSubCapita.getDescripcionConvenioRecobro());
			//Verifico si la autorización es temporal
			if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()  <= 0){
				autorizacionesCapitacionSub.setIndicativoTemporal(ConstantesBD.acronimoSiChar);
				autorizacionesCapitacionSub.setDescripcionEntidad(autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getRazonSocial());
				autorizacionesCapitacionSub.setDireccionEntidad(autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getDireccionEntidad());
				autorizacionesCapitacionSub.setTelefonoEntidad(autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getTelefonoEntidad());
			}else {
				autorizacionesCapitacionSub.setIndicativoTemporal(ConstantesBD.acronimoNoChar);
			}
			
			
			if (autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic() != null &&
					!autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic().isEmpty()){
				if (autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic().get(0).getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos)){

					//Verifico si la entidad que autoriza es la misma definida en el parametro Entidad Subcontratada para Centro de Costos Internos
					String entidadSubCentCostInterno=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(autorizacionEntSubCapita.getCodigoInstitucion());
					String[] entidad=entidadSubCentCostInterno.split("-");					
					Long entidadSubcontratadaInterna = Long.parseLong(entidad[0].trim());
					if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()==entidadSubcontratadaInterna){
						autorizacionesCapitacionSub.setTipoEntQueSeAuto(ConstantesIntegridadDominio.acronimoInterna);
					}else{
						autorizacionesCapitacionSub.setTipoEntQueSeAuto(ConstantesIntegridadDominio.acronimoExterna);
					}
				} else {
					autorizacionesCapitacionSub.setTipoEntQueSeAuto(autorizacionEntSubCapita.getListaCentroCostoEjecOrdenAmbArtic().get(0).getTipoEntidadEjecuta());
				}
			}else {
				if (tipoEntEjecuta.equals(ConstantesIntegridadDominio.acronimoAmbos)){
					//Verifico si la entidad que autoriza es la misma definida en el parametro Entidad Subcontratada para Centro de Costos Internos
					String entidadSubCentCostInterno=ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(autorizacionEntSubCapita.getCodigoInstitucion());
					String[] entidad=entidadSubCentCostInterno.split("-");					
					Long entidadSubcontratadaInterna = Long.parseLong(entidad[0].trim());
					if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada()==entidadSubcontratadaInterna){
						autorizacionesCapitacionSub.setTipoEntQueSeAuto(ConstantesIntegridadDominio.acronimoInterna);
					}else{
						autorizacionesCapitacionSub.setTipoEntQueSeAuto(ConstantesIntegridadDominio.acronimoExterna);
					}
				} else {
					autorizacionesCapitacionSub.setTipoEntQueSeAuto(tipoEntEjecuta);
				}
			}

			if (autorizacionEntSubCapita.getDatosPacienteAutorizar().getClasificacionSocieconomica()!=0){
				EstratosSociales clasificacionSocioEcono = new EstratosSociales();	
				clasificacionSocioEcono.setCodigo(autorizacionEntSubCapita.getDatosPacienteAutorizar().getClasificacionSocieconomica());
				autorizacionesCapitacionSub.setEstratosSociales(clasificacionSocioEcono);
			}
			
			if(!UtilidadTexto.isEmpty(autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoAfiliado())){
				TiposAfiliado tipoAfiliado=new TiposAfiliado();
				String cadenaTipoAf = autorizacionEntSubCapita.getDatosPacienteAutorizar().getTipoAfiliado();
				char tipoAfili = cadenaTipoAf.charAt(0);
				tipoAfiliado.setAcronimo(tipoAfili);
				autorizacionesCapitacionSub.setTiposAfiliado(tipoAfiliado);
			}
			
			autorizacionesCapitacionSub.setObservaciones(autorizacionEntSubCapita.getObservacionesGenerales());
			usuarioAutoriza = new Usuarios();
			usuarioAutoriza.setLogin(autorizacionEntSubCapita.getLoginUsuario());
			
			//autorizacionesCapitacionSub.setUsuarioAutoriza(usuarioAutoriza);
			autorizacionesCapitacionSub.setHoraAutoriza(horaActual);
			autorizacionesCapitacionSub.setFechaAutoriza(fechaActual);
			
			autorizacionEntSubCapita.setConsecutivoAutorizacionCapita(consecutivoCapitacion);
			autorizacionesDelegate.generarAutorizacionCapitacion(autorizacionesCapitacionSub);
			return autorizacionesCapitacionSub;
		}
		catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite guardar el detalle de la autorización de servicios generada por la capita.
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	
	public void generarAutorizacionEntSubServicios(AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException{
		try{
			AutorizacionesEntSubServi autoEntSubServicios=null;
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			Servicios servicio = null;
			FinalidadesServicio finalidadServicio = null;
			NivelAutorizacion nivelAutorizacion=null;
			Contratos contratoConvenioResponsable = null;
			contratoConvenioResponsable = new Contratos();
			for (OrdenAutorizacionDto ordenAutorizar: autorizacionEntSubCapita.getOrdenesAutorizar()) {
				for (ServicioAutorizacionOrdenDto servicioAutorizar : ordenAutorizar.getServiciosPorAutorizar()) {
					//Verifico si el servicio aplica para autorizar
					if (servicioAutorizar.isAutorizado()){
						autoEntSubServicios = new AutorizacionesEntSubServi();
						servicio = new Servicios();
						servicio.setCodigo(servicioAutorizar.getCodigo());
						//Envio la información a la entidad autoEntSubServicios
						autoEntSubServicios.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
						autoEntSubServicios.setServicios(servicio);
						if (servicioAutorizar.getCantidad()!=null){
							autoEntSubServicios.setCantidad(servicioAutorizar.getCantidad().intValue());
						}
						String urgente=String.valueOf(servicioAutorizar.getUrgente());
						if(urgente.trim().isEmpty()){
							autoEntSubServicios.setUrgente(ConstantesBD.acronimoNoChar);
						}
						else{
							autoEntSubServicios.setUrgente(servicioAutorizar.getUrgente());
						}
						if(servicioAutorizar.getFinalidad()!=null &&
								servicioAutorizar.getFinalidad() != ConstantesBD.codigoNuncaValido){
							finalidadServicio = new FinalidadesServicio();
							finalidadServicio.setCodigo(servicioAutorizar.getFinalidad());
							autoEntSubServicios.setFinalidadesServicio(finalidadServicio);	
						}	
						autoEntSubServicios.setValorTarifa(servicioAutorizar.getValorTarifa());
						if(servicioAutorizar.getNivelAutorizacion() != null)
						{
							nivelAutorizacion = new NivelAutorizacion();
							nivelAutorizacion.setCodigoPk(servicioAutorizar.getNivelAutorizacion().getCodigo());
							autoEntSubServicios.setNivelAutorizacion(nivelAutorizacion);
						}
						if (ordenAutorizar.getContrato().getCodigo()!= 0){
							contratoConvenioResponsable.setCodigo(ordenAutorizar.getContrato().getCodigo());
							autoEntSubServicios.setContratos(contratoConvenioResponsable);
						}
						autorizacionesDelegate.generarAutoEntSubServicios(autoEntSubServicios);
					}
				}
			}
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	
	/**
	 * Método que permite guardar el detalle de la autorización de Articulos generada por la capita.
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	
	public void generarAutorizacionEntSubArticulos(AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException{
		try{
			AutorizacionesEntSubArticu autoEntSubArticulos=null;
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			Articulo articulo = null;
			NivelAutorizacion nivelAutorizacion=null;
			Contratos contratoConvenioResponsable = null;
			contratoConvenioResponsable = new Contratos();
			
			for (OrdenAutorizacionDto ordenAutorizar: autorizacionEntSubCapita.getOrdenesAutorizar()) {
				for (MedicamentoInsumoAutorizacionOrdenDto medicamentoAutorizar : ordenAutorizar.getMedicamentosInsumosPorAutorizar()) {
					//Verifico si el servicio aplica para autorizar
					if (medicamentoAutorizar.isAutorizado()){
						autoEntSubArticulos = new AutorizacionesEntSubArticu();
						articulo = new Articulo();
						articulo.setCodigo(medicamentoAutorizar.getCodigo());
						//Envio la información a la entidad autoEntSubServicios
						autoEntSubArticulos.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
						autoEntSubArticulos.setArticulo(articulo);
						autoEntSubArticulos.setDosis(medicamentoAutorizar.getDosis());
						if (medicamentoAutorizar.getFrecuencia() == null){
							autoEntSubArticulos.setFrecuencia(null);
						}else {
						autoEntSubArticulos.setFrecuencia(medicamentoAutorizar.getFrecuencia().intValue());
						}
						autoEntSubArticulos.setDiasTratamiento(medicamentoAutorizar.getDiasTratamiento());
						autoEntSubArticulos.setVia(medicamentoAutorizar.getViaAdministracion());
						
						if (medicamentoAutorizar.getCantidad() != null){
							autoEntSubArticulos.setNroDosisTotal(medicamentoAutorizar.getCantidad().intValue());
						}else {
							autoEntSubArticulos.setNroDosisTotal(null);
						}
						
						autoEntSubArticulos.setTipoFrecuencia(medicamentoAutorizar.getTipoFrecuencia());
						if(medicamentoAutorizar.getNivelAutorizacion() != null)
						{
							nivelAutorizacion = new NivelAutorizacion();
							nivelAutorizacion.setCodigoPk(medicamentoAutorizar.getNivelAutorizacion().getCodigo());
							autoEntSubArticulos.setNivelAutorizacion(nivelAutorizacion);
						}
						//Este dato se agrega porque se necesita un estado para saber si el articulo ha sido despachado
						autoEntSubArticulos.setEstado(ConstantesIntegridadDominio.acronimoEstadoPendiente);
						if (ordenAutorizar.getContrato().getCodigo()!= 0){
							contratoConvenioResponsable.setCodigo(ordenAutorizar.getContrato().getCodigo());
							autoEntSubArticulos.setContratos(contratoConvenioResponsable);
						}
						
						autoEntSubArticulos.setValorTarifa(medicamentoAutorizar.getValorTarifa());
						
						autorizacionesDelegate.generarAutoEntSubArticulos(autoEntSubArticulos);
					}
				}
			}
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite guardar los montos generados por la autorización de capitación subcontratada
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void generarAutorizacionEntSubMontos(AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException{
		try{
			AutorizacionesDelegate autorizacionesDelegate = null;
			AutorizacionesEntSubMontos autorizacionEntSubMontos = null;
			
			//Envio la información a la entidad AutorizacionesEntSubMontos
			autorizacionEntSubMontos = new AutorizacionesEntSubMontos();
			autorizacionEntSubMontos.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			
			if(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getCodigoViaIngreso() != 0){
				ViasIngreso viasIngreso;	
				viasIngreso = new ViasIngreso();
				viasIngreso.setCodigo(autorizacionEntSubCapita.getOrdenesAutorizar().get(0).getCodigoViaIngreso());
				autorizacionEntSubMontos.setViasIngreso(viasIngreso);
			}

			if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() > 0){
				autorizacionEntSubMontos.setValormonto(autorizacionEntSubCapita.getMontoCobroAutorizacion().getValorMontoCalculado());
			}

			autorizacionEntSubMontos.setPorcentajemonto(autorizacionEntSubCapita.getMontoCobroAutorizacion().getPorcentajeMonto());
			autorizacionEntSubMontos.setTipomonto(autorizacionEntSubCapita.getMontoCobroAutorizacion().getTipoMonto());
			autorizacionEntSubMontos.setTipodetallemonto(autorizacionEntSubCapita.getMontoCobroAutorizacion().getTipoDetalleMonto());
			
			
			if (autorizacionEntSubCapita.getDatosPacienteAutorizar().isCuentaManejaMontos()){
				if (autorizacionEntSubCapita.getMontoCobroAutorizacion().getCodDetalleMonto() != null &&
						autorizacionEntSubCapita.getMontoCobroAutorizacion().getCodDetalleMonto() != 0){
					DetalleMonto detalleMonto = new DetalleMonto();
					detalleMonto.setDetalleCodigo(autorizacionEntSubCapita.getMontoCobroAutorizacion().getCodDetalleMonto());
					autorizacionEntSubMontos.setDetalleMonto(detalleMonto);
					autorizacionEntSubMontos.setCantidadMonto(autorizacionEntSubCapita.getMontoCobroAutorizacion().getCantidadMonto());
				}
			}
			
			autorizacionesDelegate = new AutorizacionesDelegate();
			autorizacionesDelegate.generarAutorizacionesEntSubMontos(autorizacionEntSubMontos);
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite guardar los centros de costo que ejecutan la autorización de entidad subcontratada
	 * generada por la capitación
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void generarAutorizacionEntSubCentoCosto(int centroCosto, AutorizacionesEntidadesSub autorizacionesEntidadesSub) throws IPSException{
		try{
			AutorizacionesDelegate autorizacionesDelegate = null;
			AutoCapiXCentroCosto autoCapiXCentroCosto = null;
			AutoCapiXCentroCostoId autoCapiXCentroCostoId = null;
			CentrosCosto centrosCosto = null;
			
			//Envio la información a la entidad correspondiente
			autoCapiXCentroCostoId = new AutoCapiXCentroCostoId();
			autoCapiXCentroCostoId.setAutoEntSub(autorizacionesEntidadesSub.getConsecutivo());
			autoCapiXCentroCostoId.setCentroCosto(centroCosto);
			autoCapiXCentroCosto = new AutoCapiXCentroCosto();
			autoCapiXCentroCosto.setId(autoCapiXCentroCostoId);
			centrosCosto = new CentrosCosto();
			centrosCosto.setCodigo(centroCosto);
			autoCapiXCentroCosto.setCentrosCosto(centrosCosto);
			autoCapiXCentroCosto.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			
			autorizacionesDelegate = new AutorizacionesDelegate();
			autorizacionesDelegate.generarAutoCapiXCentroCosto(autoCapiXCentroCosto);
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que permite generar el historico de las autorizaciones
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesEntidadesSub
	 * @throws IPSException
	 */
	public void generarAutorizacionEntSubHistorico(AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesEntidadesSub autorizacionesEntidadesSub, AutorizacionesCapitacionSub autorizacionCapiSub,
			boolean historicoAnular) throws IPSException{
		try{
			AutorizacionesDelegate autorizacionesDelegate = null;
			HistoAutorizacionCapitaSub hisAutoCapiSub = null;
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
			String horaActual = UtilidadFecha.getHoraActual();
			
			//Envio la iformación a la entidad HistoAutorizacionCapitaSub
			hisAutoCapiSub = new HistoAutorizacionCapitaSub();
			hisAutoCapiSub.setAutorizacionesCapitacionSub(autorizacionCapiSub);
			hisAutoCapiSub.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			hisAutoCapiSub.setConsecutivo(autorizacionCapiSub.getConsecutivo());
			hisAutoCapiSub.setTipoAutorizacion(autorizacionCapiSub.getTipoAutorizacion());
			
			if(autorizacionEntSubCapita.isConvenioRecobro() &&
				autorizacionEntSubCapita.getCodConvenioRecobro() != null &&
				autorizacionEntSubCapita.getCodConvenioRecobro() != ConstantesBD.codigoNuncaValido){
				Convenios convenioRecobro = new Convenios();
				convenioRecobro.setCodigo(autorizacionEntSubCapita.getCodConvenioRecobro());
				hisAutoCapiSub.setConvenios(convenioRecobro);
			}
			hisAutoCapiSub.setOtroConvenioRecobro(autorizacionEntSubCapita.getDescripcionConvenioRecobro());
			hisAutoCapiSub.setIndicativoTemporal(autorizacionCapiSub.getIndicativoTemporal());
			hisAutoCapiSub.setDescripcionEntidad(autorizacionCapiSub.getDescripcionEntidad());
			hisAutoCapiSub.setDireccionEntidad(autorizacionCapiSub.getDireccionEntidad());
			hisAutoCapiSub.setTelefonoEntidad(autorizacionCapiSub.getTelefonoEntidad());
			Usuarios usuarioModifica = new Usuarios();
			usuarioModifica.setLogin(autorizacionEntSubCapita.getLoginUsuario());
			hisAutoCapiSub.setUsuarios(usuarioModifica);
			hisAutoCapiSub.setFechaModifica(fechaActual);
			hisAutoCapiSub.setHoraModifica(horaActual);
			hisAutoCapiSub.setObservaciones(autorizacionEntSubCapita.getObservacionesGenerales());
			if(historicoAnular){
				hisAutoCapiSub.setAccionRealizada(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			}else{
			hisAutoCapiSub.setAccionRealizada(ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar);
			}
			hisAutoCapiSub.setFechaVencimiento(autorizacionEntSubCapita.getFechaVencimientoAutorizacion());
			autorizacionesDelegate = new AutorizacionesDelegate();
			autorizacionesDelegate.generarHistorialAutorCapitacionSub(hisAutoCapiSub);
			
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	
	/**
	 * Método que se encarga de verificar si se generará autorizacion para la solicitud
	 * Este metodo realiza transacciones granulares, es decir por cada operacion de
	 * consulta, actualizacion o eliminacion requerida se abre y cierra la transaccion
	 * para garantizar la atomicidad de las mismas y un correcto manejo de la transacción
	 * al momento de guardar la autorización 
	 * 
	 * @param autorizacionCapitacionDto
	 * @throws IPSException
	 * @author Camilo Gomez
	 */
	public List<AutorizacionCapitacionDto> verificarGenerarAutorizacionCapitacion(AutorizacionCapitacionDto autorizacionCapitacionDto)throws IPSException
	{
		IConvenioServicio convenioServicio	= null;
		Convenios convenioResponsable		= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		AutorizacionesDelegate autorizacionesDelegate 		= null;
		List<AutorizacionPorOrdenDto> listaOrdenAsociadaAutoriz	= null;
		List<OrdenAutorizacionDto> listOrdeneAsociarAutorizar 	= null;
		AutorizacionesEntidadesSub autorizacionesEntidadesSub 	= null;
		AutorizacionCapitacionMundo autorizacionCapitacionMundo = null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacionDto = null;
		boolean validarGenerarAutorizacion	= false;
		List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosPorAutorizar = null;
		List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
		try{
			HibernateUtil.beginTransaction();
			convenioServicio		= FacturacionServicioFabrica.crearConvenioServicio();
			listaOrdenesAutorizar	= new ArrayList<OrdenAutorizacionDto>();
			autorizacionesDelegate 	= new AutorizacionesDelegate();
			
			for (OrdenAutorizacionDto ordenVerificar: autorizacionCapitacionDto.getOrdenesAutorizar()) {
				//hermorhu -MT5959 (No se valida si el servicio esta cubierto cuando se genera la autorización)
				listaArticulosPorAutorizar = new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
				listaServiciosPorAutorizar = new ArrayList<ServicioAutorizacionOrdenDto>();
				boolean bandConvenioContrato = true;
				//Validacion cobertura de los servicios/articulos de la orden
				if(ordenVerificar.getMedicamentosInsumosPorAutorizar() != null && !ordenVerificar.getMedicamentosInsumosPorAutorizar().isEmpty()) {
					for(MedicamentoInsumoAutorizacionOrdenDto articulo : ordenVerificar.getMedicamentosInsumosPorAutorizar()) {
						if(articulo.isCubierto()) {
							listaArticulosPorAutorizar.add(articulo);
						}
					}
					ordenVerificar.getMedicamentosInsumosPorAutorizar().clear();
					ordenVerificar.getMedicamentosInsumosPorAutorizar().addAll(listaArticulosPorAutorizar);
				} else if(ordenVerificar.getServiciosPorAutorizar() != null && !ordenVerificar.getServiciosPorAutorizar().isEmpty()) {
					for(ServicioAutorizacionOrdenDto servicio : ordenVerificar.getServiciosPorAutorizar()) {
						if(servicio.isCubierto()) {
							listaServiciosPorAutorizar.add(servicio);
						}
					}
					ordenVerificar.getServiciosPorAutorizar().clear();
					ordenVerificar.getServiciosPorAutorizar().addAll(listaServiciosPorAutorizar);
					
					//Validacion para las solicitudes de cirugia que todos los servicios tengan el mismo convenio/contrato
					if(ordenVerificar.getTipoOrden() == ConstantesBD.codigoTipoSolicitudCirugia) {
						for(ServicioAutorizacionOrdenDto servicio : ordenVerificar.getServiciosPorAutorizar()) {
							if(ordenVerificar.getServiciosPorAutorizar().get(0).getCodigoContrato().intValue() != servicio.getCodigoContrato().intValue()){
								bandConvenioContrato = false;
								break;
							}
						}
					}
				}
				
				//Validacion Convenio
				if(((ordenVerificar.getServiciosPorAutorizar() != null && !ordenVerificar.getServiciosPorAutorizar().isEmpty()) ||
					(ordenVerificar.getMedicamentosInsumosPorAutorizar() != null && !ordenVerificar.getMedicamentosInsumosPorAutorizar().isEmpty())) &&
					bandConvenioContrato) {
				convenioResponsable = convenioServicio.findById(ordenVerificar.getContrato().getConvenio().getCodigo());
				if(convenioResponsable != null && ((convenioResponsable.getTiposContrato().getCodigo())==ConstantesBD.codigoTipoContratoCapitado) &&         					
						(convenioResponsable.getCapitacionSubcontratada()==ConstantesBD.acronimoSiChar))
				{
					ordenVerificar.getContrato().getConvenio().setConvenioManejaCapiSub(true);
					ordenVerificar.getContrato().getConvenio().setCodigoTipoContrato(ConstantesBD.codigoTipoContratoCapitado);
					listaOrdenesAutorizar.add(ordenVerificar);
				}
			}
			}
			HibernateUtil.endTransaction();
			
			//Si el convenio es capitado y maneja capitacion Sub continua con las validacion de generacion
			if(listaOrdenesAutorizar != null && !listaOrdenesAutorizar.isEmpty())
			{	
				//Se reemplaza la lista con las ordenes que pasaron la validacion del convenio
				autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
				
				HibernateUtil.beginTransaction();
				//Si se esta generando desde una OrdenAmbulatoria, solo viene una orden.
				List<String> estadosAutorizacion = new ArrayList<String>();
				estadosAutorizacion.add(ConstantesIntegridadDominio.acronimoAutorizado);
				if(autorizacionCapitacionDto.getOrdenesAutorizar().get(0).getClaseOrden()==ConstantesBD.claseOrdenOrdenAmbulatoria){
					listaOrdenAsociadaAutoriz = autorizacionesDelegate.existeAutorizacionCapitaOrdenAmbulatoria(
							autorizacionCapitacionDto.getOrdenesAutorizar().get(0).getOtroCodigoOrden(), estadosAutorizacion);
					
				}else if(autorizacionCapitacionDto.getOrdenesAutorizar().get(0).getClaseOrden()==ConstantesBD.claseOrdenPeticion){
					listaOrdenAsociadaAutoriz = autorizacionesDelegate.existeAutorizacionCapitaPeticion(
							Integer.parseInt(autorizacionCapitacionDto.getOrdenesAutorizar().get(0).getOtroCodigoOrden()+""),estadosAutorizacion);
				}
				HibernateUtil.endTransaction();
				
				int claseOrden = autorizacionCapitacionDto.getOrdenesAutorizar().get(0).getClaseOrden();
				
				if( claseOrden == ConstantesBD.claseOrdenOrdenMedica || (listaOrdenAsociadaAutoriz == null || listaOrdenAsociadaAutoriz.isEmpty()))
				{//I. Validacion cuando no tiene autorizacion Asociada o no tiene llamado desde orden ambulatoria.
					
					if(claseOrden == ConstantesBD.claseOrdenOrdenAmbulatoria){
					//Cuando tiene llamado desde OA pero esta no tiene autorizacion, entonces se generar autorizacion a partir de la solicitud(O.Medica)
					for (OrdenAutorizacionDto orden : autorizacionCapitacionDto.getOrdenesAutorizar()) {
						orden.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
					}
					}
					validarGenerarAutorizacion = true;
					
				}else if(claseOrden == ConstantesBD.claseOrdenPeticion && (listaOrdenAsociadaAutoriz == null || listaOrdenAsociadaAutoriz.isEmpty()))
				{//I. Validacion cuando no tiene autorizacion Asociada o no tiene llamado desde Peticion.
					
					//Cuando tiene llamado desde Peticion pero esta no tiene autorizacion, entonces se generar autorizacion a partir de la solicitud(O.Medica)
					for (OrdenAutorizacionDto orden : autorizacionCapitacionDto.getOrdenesAutorizar()) {						
						orden.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
					}
					validarGenerarAutorizacion = true;
				}
				
				
				autorizacionCapitacionMundo = new AutorizacionCapitacionMundo();
				if(validarGenerarAutorizacion){
					//I. Validacion cuando no tiene autorizacion Asociada a PETICION
					listaAutorizacionCapitacionDto = 
							autorizacionCapitacionMundo.validarGenerarAutorizacionCapitacion(
									autorizacionCapitacionDto);
				
				}else{
					//List<Long>listaConsecutivoRepetido	= new ArrayList<Long>();
					//ordenAutorizacionDto 				= new OrdenAutorizacionDto();
					
					HibernateUtil.beginTransaction();
					for (OrdenAutorizacionDto ordenAsociar: autorizacionCapitacionDto.getOrdenesAutorizar())
					{
						List<Long>listaConsecutivoRepetido	= new ArrayList<Long>();
						//for (AutorizacionPorOrdenDto ordenAutorizada: listaOrdenAsociadaAutoriz)
						//{
							if(!UtilidadTexto.isEmpty(listaOrdenAsociadaAutoriz.get(0).getConsecutivoAutorizacion()))
							{//II. validacion cuando Existe una Autorización de Capitación de la Orden, entonces se debe asociar 
							 //la Solicitud: en AutoEntSubSolicitudes y en el detalle del tipo de solicitud
								listOrdeneAsociarAutorizar	= new ArrayList<OrdenAutorizacionDto>();
								
								listOrdeneAsociarAutorizar.add(ordenAsociar);//--
								if(!listaConsecutivoRepetido.contains(listaOrdenAsociadaAutoriz.get(0).getConsecutivo())){
									listaConsecutivoRepetido.add(listaOrdenAsociadaAutoriz.get(0).getConsecutivo());
									
									//ordenAutorizacionDto.setCodigoOrden(ordenAsociar.getCodigoOrden());
									ordenAsociar.setMigrado(ConstantesBD.acronimoSiChar);//--
									autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
									autorizacionesEntidadesSub.setConsecutivo(listaOrdenAsociadaAutoriz.get(0).getConsecutivo().intValue());
									//Asocia la autorizacion con la Orden
									this.asociarSolicitudAutorizacion(listOrdeneAsociarAutorizar,autorizacionesEntidadesSub);
								}
								
								//servicioAutorizacionOrdenDto.setAutorizado(true);
								if(ordenAsociar.getServiciosPorAutorizar()!=null 
										&& !ordenAsociar.getServiciosPorAutorizar().isEmpty()){
									ordenAsociar.getServiciosPorAutorizar().get(0).setAutorizado(true);
								}else if(ordenAsociar.getMedicamentosInsumosPorAutorizar()!=null &&
										!ordenAsociar.getMedicamentosInsumosPorAutorizar().isEmpty()){
									for(MedicamentoInsumoAutorizacionOrdenDto medicaInsumo: ordenAsociar.getMedicamentosInsumosPorAutorizar()){
										boolean autorizado=true;
										if((ordenAsociar.getClaseOrden() == ConstantesBD.claseOrdenOrdenMedica
												|| ordenAsociar.getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria)
												&& ordenAsociar.getTipoOrden() == ConstantesBD.codigoTipoSolicitudMedicamentos){
											if(!autorizacionesDelegate.existeAutorizacionMedicamentoOrden(ordenAsociar.getCodigoOrden().intValue(), medicaInsumo.getCodigo(), listaOrdenAsociadaAutoriz.get(0).getConsecutivo())){
												autorizado=false;//validar cuantos vienen por orden
											}
										}
										medicaInsumo.setAutorizado(autorizado);
									}
								}
								
								//ordenAsociar.setServiciosPorAutorizar(listServicios);
								//ordenAsociar.setTipoOrden(ConstantesBD.codigoTipoSolicitudProcedimiento);
								
								//Asocia la autorizacion con el detalle de la orden
								this.asociarDetalleSolicitudesAutorizaciones(listOrdeneAsociarAutorizar,autorizacionesEntidadesSub, false);
								
							}else
							{//III. Validacion cuando Existe una Autorización de Capitación de la Orden, pero no de Entidad Subcontratada 
							 //entonces se debe asociar la Solicitud: Se actualiza el consecutivoAutorizacion
								
								if(listaOrdenAsociadaAutoriz.get(0).getConsecutivoAutorizacion() == null)
								{
					 				//setear el consecutivo
									String consecutivoAutorizacionEntSub = UtilidadBD.obtenerValorConsecutivoDisponible(
					        				ConstantesBD.nombreConsecutivoAutorizacionEntiSub,
					        				autorizacionCapitacionDto.getCodigoInstitucion());
					 				
									if((!UtilidadTexto.isEmpty(consecutivoAutorizacionEntSub) && UtilidadTexto.isNumber(consecutivoAutorizacionEntSub) &&
											Integer.parseInt(consecutivoAutorizacionEntSub)!=ConstantesBD.codigoNuncaValido))
									{
										//actualiza el consecutivo de la autorizacion de entidad subcontratada
										this.actualizarConsecutivoAutorizacionesEntidadesSub(
												listaOrdenAsociadaAutoriz.get(0).getConsecutivo(), consecutivoAutorizacionEntSub);
										
										//Asocio la autorización de la orden ambulatoria a la solicitud
										listOrdeneAsociarAutorizar	= new ArrayList<OrdenAutorizacionDto>();
										listOrdeneAsociarAutorizar.add(ordenAsociar);//--
										
										if(!listaConsecutivoRepetido.contains(listaOrdenAsociadaAutoriz.get(0).getConsecutivo())){
											listaConsecutivoRepetido.add(listaOrdenAsociadaAutoriz.get(0).getConsecutivo());
											
											//ordenAutorizacionDto.setCodigoOrden(ordenAsociar.getCodigoOrden());
											ordenAsociar.setMigrado(ConstantesBD.acronimoSiChar);//--
											autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
											autorizacionesEntidadesSub.setConsecutivo(listaOrdenAsociadaAutoriz.get(0).getConsecutivo().intValue());
											//Asocia la autorizacion con la Orden
											this.asociarSolicitudAutorizacion(listOrdeneAsociarAutorizar,autorizacionesEntidadesSub);
										}
										
										//servicioAutorizacionOrdenDto.setAutorizado(true);
										if(ordenAsociar.getServiciosPorAutorizar()!=null 
												&& !ordenAsociar.getServiciosPorAutorizar().isEmpty()){
											ordenAsociar.getServiciosPorAutorizar().get(0).setAutorizado(true);
										}else if(ordenAsociar.getMedicamentosInsumosPorAutorizar()!=null &&
												!ordenAsociar.getMedicamentosInsumosPorAutorizar().isEmpty()){
											for(MedicamentoInsumoAutorizacionOrdenDto medicaInsumo: ordenAsociar.getMedicamentosInsumosPorAutorizar()){
												boolean autorizado=true;
												if((ordenAsociar.getClaseOrden() == ConstantesBD.claseOrdenOrdenMedica
														|| ordenAsociar.getClaseOrden() == ConstantesBD.claseOrdenOrdenAmbulatoria)
														&& ordenAsociar.getTipoOrden() == ConstantesBD.codigoTipoSolicitudMedicamentos){
													if(!autorizacionesDelegate.existeAutorizacionMedicamentoOrden(ordenAsociar.getCodigoOrden().intValue(), medicaInsumo.getCodigo(), listaOrdenAsociadaAutoriz.get(0).getConsecutivo())){
														autorizado=false;//validar cuantos vienen por orden
													}
												}
												medicaInsumo.setAutorizado(autorizado);//
											}
										}
										
										//ordenAsociar.setServiciosPorAutorizar(listServicios);
										//ordenAsociar.setTipoOrden(ConstantesBD.codigoTipoSolicitudProcedimiento);
										
										//Asocia la autorizacion con el detalle de la orden
										this.asociarDetalleSolicitudesAutorizaciones(listOrdeneAsociarAutorizar,autorizacionesEntidadesSub, false);
										
										
										
									}else{
										/*ErrorMessage mensajeErrorGeneral = new ErrorMessage(
												"errors.autorizacion.messageGeneral");
										autorizacionCapitacionDto.setMensajeErrorGeneral(mensajeErrorGeneral);*/
										
										autorizacionCapitacionDto.setProcesoExitoso(false);
										autorizacionCapitacionDto.setVerificarDetalleError(true);
										
										if(ordenAsociar.getServiciosPorAutorizar()!=null && !ordenAsociar.getServiciosPorAutorizar().isEmpty()){
											ordenAsociar.getServiciosPorAutorizar().get(0).setAutorizar(false);
											ordenAsociar.getServiciosPorAutorizar().get(0).setAutorizado(false);
											
											Log4JManager.info("EL SERVICIO NO SE PUEDE AUTORIZAR [No se genero consecutivo Autorizacion Entidad Sub] "
											+ordenAsociar.getServiciosPorAutorizar().get(0).getDescripcion());
										
										}else if(ordenAsociar.getMedicamentosInsumosPorAutorizar()!=null && !ordenAsociar.getMedicamentosInsumosPorAutorizar().isEmpty()){
											for(MedicamentoInsumoAutorizacionOrdenDto medicaInsumo: ordenAsociar.getMedicamentosInsumosPorAutorizar()){
												medicaInsumo.setAutorizar(false);
												medicaInsumo.setAutorizado(false);
												
												Log4JManager.info("EL MEDICAMENTO/INSUMO NO SE PUEDE AUTORIZAR [No se genero consecutivo Autorizacion Entidad Sub] "
												+ medicaInsumo.getDescripcion());
											}
										}
									}
								}	
							}
						//}
					}
					autorizacionCapitacionMundo.obtenerMensajesServiciosMedicamentosInsumosAutorizar(autorizacionCapitacionDto,false);
					listaAutorizacionCapitacionDto = new ArrayList<AutorizacionCapitacionDto>();
					listaAutorizacionCapitacionDto.add(autorizacionCapitacionDto);
					
					HibernateUtil.endTransaction();
				}
			}
			
		}catch (IPSException ipsme) {
			Log4JManager.error(ipsme.getMessage(),ipsme);
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			HibernateUtil.abortTransaction();
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
		return listaAutorizacionCapitacionDto;
	}
	
	/**
	 * Metodo que permite generar el asocio de la autorización de ingreso estancia con la autorizacion
	 * de capitación subcontratada.
	 * 
	 * @param autorizacionEntSubCapita
	 * @param autorizacionesCapitacionSub
	 * @throws IPSException
	 */
	
	public void generarAutorizacionSerArtIngresoEstancia(AutorizacionCapitacionDto autorizacionEntSubCapita, 
			AutorizacionesCapitacionSub autorizacionesCapitacionSub) throws IPSException{
		try{
			AutorizacionesDelegate autorizacionesDelegate = new AutorizacionesDelegate();
			AutorizacionesIngreEstancia autorizacionesIngreEstancia = new AutorizacionesIngreEstancia();
			AutorizacionesEstanciaCapita autorizacionesEstanciaCapita = new AutorizacionesEstanciaCapita();
			
			autorizacionesIngreEstancia.setCodigoPk(autorizacionEntSubCapita.getCodAutorIngresoEstancia());
			autorizacionesEstanciaCapita.setAutorizacionesCapitacionSub(autorizacionesCapitacionSub);
			autorizacionesEstanciaCapita.setAutorizacionesIngreEstancia(autorizacionesIngreEstancia);
			
			autorizacionesDelegate.generarAsocioAutoSerArtIngresoEstancia(autorizacionesEstanciaCapita);
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	
}