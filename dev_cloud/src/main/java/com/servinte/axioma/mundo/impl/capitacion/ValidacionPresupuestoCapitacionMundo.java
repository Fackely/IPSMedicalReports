package com.servinte.axioma.mundo.impl.capitacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.capitacion.ConstantesCapitacion;

import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.dto.capitacion.DtoValidacionPresupuesto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteGruServMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenServMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValidacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.Servicios;

/**
 *  Clase que define la lógica de negocio relacionada con la
 *  Validación de Presupuesto de Capitación para Autorizaciones de
 *  Capitación Subcontratada
 * 
 * @version 1.0, Agu 29, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public class ValidacionPresupuestoCapitacionMundo implements IValidacionPresupuestoCapitacionMundo{

	/** * Contiene la lista de mensajes correspondiente a esta validación */
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ValidacionPresupuestoCapitacion");
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValidacionPresupuestoCapitacionMundo#validarPresupuestoCapitacionServicio(java.lang.Integer)
	 */
	@Override
	public DtoValidacionPresupuesto validarPresupuestoCapitacionServicio(
			ServicioAutorizacionOrdenDto dtoServicio, Integer codigoConvenio, String nombreConvenio, Integer codigoContrato, String numeroContrato) {
		DtoValidacionPresupuesto dtoValidacionPresupuesto = new DtoValidacionPresupuesto();
		boolean existeParametrizacionPresupuesto=false;
		boolean existeCierre=false;
		boolean presupuestoDetallado=false;
		boolean presupuestoGeneral=false;
		Double valorParametrizacionPresupuesto=0D;
		Double valorCierreTemporal=0D;
		Double valorAutorizar=0D;
		Double valorTarifa=0D;
		try{
		
			Calendar fechaActual = Calendar.getInstance();
			IServiciosMundo serviciosMundo = FacturacionFabricaMundo.crearServiciosMundo();
			
			Servicios servicioEntity = serviciosMundo.obtenerServicioPorId(dtoServicio.getCodigo());
			//Se valida si la fecha actual corresponde al primer día del mes
			if(fechaActual.get(Calendar.DAY_OF_MONTH) == 1){
				IParametrizacionPresupuestoCapitacionMundo parametrizacionPresupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
				//Validar que exista informacion de parametrización de presupuesto y de que tipo
				//Se valida primero si existe parametrización Detallada
				Double valorDetallado=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoDetalladoServicios(
						codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
						servicioEntity.getGruposServicios().getCodigo(), servicioEntity.getNivelAtencion().getConsecutivo());
				if(valorDetallado != null){
					presupuestoDetallado=true;
					presupuestoGeneral=false;
					existeParametrizacionPresupuesto=true;
					valorParametrizacionPresupuesto=valorDetallado;
				}
				else{
					//Se valida que exista parametrización General
					Double valorGeneral=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(
							codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
							servicioEntity.getNivelAtencion().getConsecutivo(), ConstantesCapitacion.subSeccionServicio);
					if(valorGeneral != null){
						presupuestoDetallado=false;
						presupuestoGeneral=true;
						existeParametrizacionPresupuesto=true;
						valorParametrizacionPresupuesto=valorGeneral;
					}
					else{
						//En caso de que no exista parametrización de presupuesto
						presupuestoDetallado=false;
						presupuestoGeneral=false;
						existeParametrizacionPresupuesto=false;
					}
				}
				
				if(existeParametrizacionPresupuesto){
					ICierreTempNivelAteGruServMundo cierreTempNivelGrupoServicioMundo = CapitacionFabricaMundo.crearCierreTempNivelAteGruServMundo();
					ICierreTempNivelAtenServMundo cierreTempNivelServicioMundo = CapitacionFabricaMundo.crearCierreTempNivelAtenServMundo();
					//Se valida que exista información de la Temporal Cierres Ordenes Medicas 
					//de Autorizaciones para la fecha del sistema
					//Se valida primero si se parametrizó presupuesto General
					Double cierreTemp=null;
					if(presupuestoGeneral){
						cierreTemp=cierreTempNivelServicioMundo.obtenerValorCierreTemporalNivelServicios(codigoContrato, 
								fechaActual.getTime(), servicioEntity.getNivelAtencion().getConsecutivo());
					}
					else if(presupuestoDetallado){
						cierreTemp=cierreTempNivelGrupoServicioMundo.obtenerValorCierreTemporalNivelGrupoServicios(codigoContrato, 
										fechaActual.getTime(), servicioEntity.getGruposServicios().getCodigo(), 
										servicioEntity.getNivelAtencion().getConsecutivo());
					}
					if(cierreTemp != null){
						valorCierreTemporal=cierreTemp;
					}
					else{
						valorCierreTemporal=0D;
					}
					//Se valida el Tope de Presupuesto
					if(dtoServicio.getValorTarifa() != null){
						valorTarifa=dtoServicio.getValorTarifa().doubleValue() * dtoServicio.getCantidad();
					}
					valorAutorizar=valorCierreTemporal+valorTarifa;
					if(valorAutorizar <= valorParametrizacionPresupuesto){
						dtoValidacionPresupuesto.setValido(true);
						dtoValidacionPresupuesto.setMensaje(null);
						dtoValidacionPresupuesto.setCodigoValidacion(null);
						dtoValidacionPresupuesto.setNombreServicioArticulo(null);
						dtoValidacionPresupuesto.setFechaValidacion(null);
						dtoValidacionPresupuesto.setConvenioValidacion(null);
						dtoValidacionPresupuesto.setContratoValidacion(null);
						dtoValidacionPresupuesto.setAnioValidacion(null);
						dtoValidacionPresupuesto.setMesValidacion(null);
					}
					else{
						dtoValidacionPresupuesto.setValido(false);
						ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.valorServiciosSobrepasaTope", 
								String.valueOf(dtoServicio.getCodigo()), nombreConvenio, numeroContrato, String.valueOf(fechaActual.get(Calendar.YEAR)));
						dtoValidacionPresupuesto.setMensajeError(error);
						
						/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
													"ValidacionPresupuestoCapitacion.valorServiciosSobrepasaTope", 
													new Object[]{dtoServicio.getCodigo(), nombreConvenio, numeroContrato, fechaActual.get(Calendar.YEAR)}));*/
						dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoSobrepasaTope);
						dtoValidacionPresupuesto.setNombreServicioArticulo(dtoServicio.getDescripcion());
						dtoValidacionPresupuesto.setFechaValidacion(null);
						dtoValidacionPresupuesto.setConvenioValidacion(null);
						dtoValidacionPresupuesto.setContratoValidacion(null);
						dtoValidacionPresupuesto.setAnioValidacion(null);
						dtoValidacionPresupuesto.setMesValidacion(null);
					}
				}
				else{
					dtoValidacionPresupuesto.setValido(false);
					ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.noParametrizacionPresupuestoServicio", 
							String.valueOf(dtoServicio.getCodigo()), nombreConvenio, numeroContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), 
							UtilidadFecha.obtenerNombreMesProperties(fechaActual.get(Calendar.MONTH)));
					dtoValidacionPresupuesto.setMensajeError(error);
					
					/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
												"ValidacionPresupuestoCapitacion.noParametrizacionPresupuestoServicio", 
												new Object[]{dtoServicio.getCodigo(),nombreConvenio, numeroContrato, fechaActual.get(Calendar.YEAR), 
														UtilidadFecha.obtenerNombreMesProperties(fechaActual.get(Calendar.MONTH))}));*/
					dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoNoParametrizacion);
					dtoValidacionPresupuesto.setNombreServicioArticulo(dtoServicio.getDescripcion());
					dtoValidacionPresupuesto.setFechaValidacion(null);
					dtoValidacionPresupuesto.setConvenioValidacion(nombreConvenio);
					dtoValidacionPresupuesto.setContratoValidacion(numeroContrato);
					dtoValidacionPresupuesto.setAnioValidacion(String.valueOf(fechaActual.get(Calendar.YEAR)));
					dtoValidacionPresupuesto.setMesValidacion(UtilidadFecha.obtenerNombreMesProperties(fechaActual.get(Calendar.MONTH)));
				}
			}
			else{
				ICierrePresupuestoCapitacionoMundo cierrePresupuestoMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
				Calendar fechaAnterior = Calendar.getInstance();
				//Se resta un dia a la fecha actual para obtener la fecha anterior
				fechaAnterior.add(Calendar.DAY_OF_MONTH, -1);
				DtoTotalProcesoPresupuestoCapitado params = new DtoTotalProcesoPresupuestoCapitado();
				DtoProcesoPresupuestoCapitado paramsLog = new DtoProcesoPresupuestoCapitado();
				params.setFecha(fechaAnterior.getTime());
				params.setContrato(codigoContrato);
				params.setConvenio(codigoConvenio);
				paramsLog.setFechaCierre(fechaAnterior.getTime());
				paramsLog.setContrato(codigoContrato);
				paramsLog.setConvenio(codigoConvenio);
				//Para buscar por contrato y por convenio
				paramsLog.setCaseParaBusquedaLog(3);
				//Se Valida si existe cierre para la fecha Anterior
				List<DtoTotalProcesoPresupuestoCapitado> cierre=cierrePresupuestoMundo.obtenerCierresPresupuestocapitacion(params);
				if(cierre != null && !cierre.isEmpty()){
					existeCierre=true;
				}
				else{
					ArrayList<DtoLogCierrePresuCapita> log=cierrePresupuestoMundo.obtenerLogs(paramsLog);
					if(log != null && !log.isEmpty()){
						existeCierre=true;
					}
				}
				if(existeCierre){
					IParametrizacionPresupuestoCapitacionMundo parametrizacionPresupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
					//Validar que exista informacion de parametrización de presupuesto y de que tipo
					//Se valida primero si exite parametrización Detallada
					Double valorDetallado=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoDetalladoServicios(
							codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
							servicioEntity.getGruposServicios().getCodigo(), servicioEntity.getNivelAtencion().getConsecutivo());
					if(valorDetallado != null){
						presupuestoDetallado=true;
						presupuestoGeneral=false;
						existeParametrizacionPresupuesto=true;
						valorParametrizacionPresupuesto=valorDetallado;
					}
					else{
						//Se valida que exista parametrización General
						Double valorGeneral=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(
								codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
								servicioEntity.getNivelAtencion().getConsecutivo(), ConstantesCapitacion.subSeccionServicio);
						if(valorGeneral != null){
							presupuestoDetallado=false;
							presupuestoGeneral=true;
							existeParametrizacionPresupuesto=true;
							valorParametrizacionPresupuesto=valorGeneral;
						}
						else{
							//En caso de que no exista parametrización de presupuesto
							presupuestoDetallado=false;
							presupuestoGeneral=false;
							existeParametrizacionPresupuesto=false;
						}
					}
					if(existeParametrizacionPresupuesto){
						//Se valida que exista información de Cierre de Autorizaciones para la fecha anterior del sistema 
						DtoTotalProceso totalAutorizado=null;
						Double valorCierreAutorizaciones=0D;
						if(presupuestoGeneral){
							totalAutorizado=cierrePresupuestoMundo.obtenerTotalServiciosPorNivelPorProceso(codigoContrato, 
									servicioEntity.getNivelAtencion().getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
									fechaAnterior.getTime(), fechaAnterior.getTime()).get(0);
						}
						else if(presupuestoDetallado){
							totalAutorizado=cierrePresupuestoMundo.obtenerTotalServiciosPorNivelPorGrupoPorProceso(codigoContrato, 
									servicioEntity.getNivelAtencion().getConsecutivo(), servicioEntity.getGruposServicios().getCodigo(),
									ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion, fechaAnterior.getTime(), 
									fechaAnterior.getTime()).get(0);
						}
						valorCierreAutorizaciones=totalAutorizado.getTotalProceso().doubleValue();
						ICierreTempNivelAteGruServMundo cierreTempNivelGrupoServicioMundo = CapitacionFabricaMundo.crearCierreTempNivelAteGruServMundo();
						ICierreTempNivelAtenServMundo cierreTempNivelServicioMundo = CapitacionFabricaMundo.crearCierreTempNivelAtenServMundo();
						//Se valida que exista información de la Temporal Cierres Ordenes Medicas 
						//de Autorizaciones para la fecha del sistema
						//Se valida primero si se parametrizó presupuesto General
						Double cierreTemp=null;
						if(presupuestoGeneral){
							cierreTemp=cierreTempNivelServicioMundo.obtenerValorCierreTemporalNivelServicios(codigoContrato, 
									fechaActual.getTime(), servicioEntity.getNivelAtencion().getConsecutivo());
						}
						else if(presupuestoDetallado){
							cierreTemp=cierreTempNivelGrupoServicioMundo.obtenerValorCierreTemporalNivelGrupoServicios(codigoContrato, 
											fechaActual.getTime(), servicioEntity.getGruposServicios().getCodigo(), 
											servicioEntity.getNivelAtencion().getConsecutivo());
						}
						if(cierreTemp != null){
							valorCierreTemporal=cierreTemp;
						}
						else{
							valorCierreTemporal=0D;
						}
						//Se valida el Tope de Presupuesto
						if(dtoServicio.getValorTarifa()!= null){
							valorTarifa=dtoServicio.getValorTarifa().doubleValue() * dtoServicio.getCantidad();
						}
						valorAutorizar=valorCierreAutorizaciones+valorCierreTemporal+valorTarifa;
						if(valorAutorizar <= valorParametrizacionPresupuesto){
							dtoValidacionPresupuesto.setValido(true);
							dtoValidacionPresupuesto.setMensaje(null);
							dtoValidacionPresupuesto.setCodigoValidacion(null);
							dtoValidacionPresupuesto.setNombreServicioArticulo(null);
							dtoValidacionPresupuesto.setFechaValidacion(null);
							dtoValidacionPresupuesto.setConvenioValidacion(null);
							dtoValidacionPresupuesto.setContratoValidacion(null);
							dtoValidacionPresupuesto.setAnioValidacion(null);
							dtoValidacionPresupuesto.setMesValidacion(null);
						}
						else{
							dtoValidacionPresupuesto.setValido(false);
							ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.valorServiciosSobrepasaTope", 
									String.valueOf(dtoServicio.getCodigo()), nombreConvenio, numeroContrato, String.valueOf(fechaActual.get(Calendar.YEAR)));
							dtoValidacionPresupuesto.setMensajeError(error);
							
							/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
												"ValidacionPresupuestoCapitacion.valorServiciosSobrepasaTope", 
												new Object[]{dtoServicio.getCodigo(), nombreConvenio, numeroContrato, fechaActual.get(Calendar.YEAR)}));*/
							dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoSobrepasaTope);
							dtoValidacionPresupuesto.setNombreServicioArticulo(dtoServicio.getDescripcion());
							dtoValidacionPresupuesto.setFechaValidacion(null);
							dtoValidacionPresupuesto.setConvenioValidacion(null);
							dtoValidacionPresupuesto.setContratoValidacion(null);
							dtoValidacionPresupuesto.setAnioValidacion(null);
							dtoValidacionPresupuesto.setMesValidacion(null);
						}
					}
					else{
						dtoValidacionPresupuesto.setValido(false);
						ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.noParametrizacionPresupuestoServicio", 
								String.valueOf(dtoServicio.getCodigo()), nombreConvenio, numeroContrato, String.valueOf(fechaActual.get(Calendar.YEAR)),
								UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)));
						dtoValidacionPresupuesto.setMensajeError(error);
						
						/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
													"ValidacionPresupuestoCapitacion.noParametrizacionPresupuestoServicio", 
													new Object[]{dtoServicio.getCodigo(), nombreConvenio, numeroContrato, fechaAnterior.get(Calendar.YEAR), 
															UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH))}));*/
						dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoNoParametrizacion);
						dtoValidacionPresupuesto.setNombreServicioArticulo(dtoServicio.getDescripcion());
						dtoValidacionPresupuesto.setFechaValidacion(null);
						dtoValidacionPresupuesto.setConvenioValidacion(nombreConvenio);
						dtoValidacionPresupuesto.setContratoValidacion(numeroContrato);
						dtoValidacionPresupuesto.setAnioValidacion(String.valueOf(fechaAnterior.get(Calendar.YEAR)));
						dtoValidacionPresupuesto.setMesValidacion(UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)));
					}
				}
				else{
					dtoValidacionPresupuesto.setValido(false);
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.noExisteCierreServicio", 
							String.valueOf(dtoServicio.getCodigo()), format.format(fechaAnterior.getTime()), nombreConvenio, numeroContrato, 
							String.valueOf(fechaAnterior.get(Calendar.YEAR)), UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)));
					dtoValidacionPresupuesto.setMensajeError(error);
					
					/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
												"ValidacionPresupuestoCapitacion.noExisteCierreServicio", 
												new Object[]{dtoServicio.getCodigo(), format.format(fechaAnterior.getTime()), nombreConvenio, numeroContrato, 
														fechaAnterior.get(Calendar.YEAR), UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH))}));*/
					dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoNoCierre);
					dtoValidacionPresupuesto.setNombreServicioArticulo(dtoServicio.getDescripcion());
					dtoValidacionPresupuesto.setFechaValidacion(format.format(fechaAnterior.getTime()));
					dtoValidacionPresupuesto.setConvenioValidacion(nombreConvenio);
					dtoValidacionPresupuesto.setContratoValidacion(numeroContrato);
					dtoValidacionPresupuesto.setAnioValidacion(String.valueOf(fechaAnterior.get(Calendar.YEAR)));
					dtoValidacionPresupuesto.setMesValidacion(UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)));
				}
			}
			
			return dtoValidacionPresupuesto;
		}
		catch(Exception e){
			Log4JManager.error("ERROR validarPresupuestoCapitacionServicio",e);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValidacionPresupuestoCapitacionMundo#validarPresupuestoCapitacionArticulo(java.lang.Integer)
	 */
	@Override
	public DtoValidacionPresupuesto validarPresupuestoCapitacionArticulo(
			MedicamentoInsumoAutorizacionOrdenDto dtoArticulo, Integer codigoConvenio, String nombreConvenio, Integer codigoContrato, String numeroContrato) {

		DtoValidacionPresupuesto dtoValidacionPresupuesto = new DtoValidacionPresupuesto();
		boolean existeParametrizacionPresupuesto=false;
		boolean existeCierre=false;
		boolean presupuestoDetallado=false;
		boolean presupuestoGeneral=false;
		Double valorParametrizacionPresupuesto=0D;
		Double valorCierreTemporal=0D;
		Double valorAutorizar=0D;
		Double valorTarifa=0D;
		
		try{
			Calendar fechaActual = Calendar.getInstance();
			IArticulosMundo articulosMundo = InventarioMundoFabrica.crearArticulosMundo();
			IClaseInventarioDAO claseInventarioDAO = InventarioDAOFabrica.crearClaseInventarioDAO();
			
			Articulo articuloEntity = articulosMundo.obtenerArticuloPorId(dtoArticulo.getCodigo());
			
			//Validar que contenga nivel de atencion MT 3302 DCU 1180 v1.3
			if(articuloEntity.getNivelAtencion()!= null){				
			
				int codigoClaseInventario= claseInventarioDAO.obtenerClaseInventarioPorSungrupo(articuloEntity.getSubgrupo()).getCodigo();
				//Se valida si la fecha actual corresponde al primer día del mes
				if(fechaActual.get(Calendar.DAY_OF_MONTH) == 1){
					IParametrizacionPresupuestoCapitacionMundo parametrizacionPresupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
					//Validar que exista informacion de parametrización de presupuesto y de que tipo
					//Se valida primero si exite parametrización Detallada
					Double valorDetallado=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoDetalladoArticulos(
							codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
							codigoClaseInventario, articuloEntity.getNivelAtencion().getConsecutivo());
					if(valorDetallado != null){
						presupuestoDetallado=true;
						presupuestoGeneral=false;
						existeParametrizacionPresupuesto=true;
						valorParametrizacionPresupuesto=valorDetallado;
					}
					else{
						//Se valida que exista parametrización General
						Double valorGeneral=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(
								codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
								articuloEntity.getNivelAtencion().getConsecutivo(), ConstantesCapitacion.subSeccionArticulo);
						if(valorGeneral != null){
							presupuestoDetallado=false;
							presupuestoGeneral=true;
							existeParametrizacionPresupuesto=true;
							valorParametrizacionPresupuesto=valorGeneral;
						}
						else{
							//En caso de que no exista parametrización de presupuesto
							presupuestoDetallado=false;
							presupuestoGeneral=false;
							existeParametrizacionPresupuesto=false;
						}
					}
					
					if(existeParametrizacionPresupuesto){
						ICierreTempNivelAteClaseInvArtMundo cierreTempNivelClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
						ICierreTempNivelAtenArtMundo cierreTempNivelArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAtenArtMundo();
						//Se valida que exista información de la Temporal Cierres Ordenes Medicas 
						//de Autorizaciones para la fecha del sistema
						//Se valida primero si se parametrizó presupuesto General
						Double cierreTemp=null;
						if(presupuestoGeneral){
							cierreTemp=cierreTempNivelArticuloMundo.obtenerValorCierreTemporalNivelArticulos(codigoContrato, 
									fechaActual.getTime(), articuloEntity.getNivelAtencion().getConsecutivo());
						}
						else if(presupuestoDetallado){
							cierreTemp=cierreTempNivelClaseInvArticuloMundo.obtenerValorCierreTemporalNivelClaseInventarioArticulo(
											codigoContrato, fechaActual.getTime(), codigoClaseInventario, 
											articuloEntity.getNivelAtencion().getConsecutivo());
						}
						if(cierreTemp != null){
							valorCierreTemporal=cierreTemp;
						}
						else{
							valorCierreTemporal=0D;
						}
						//Se valida el Tope de Presupuesto
						if(dtoArticulo.getValorTarifa() != null){
							valorTarifa=dtoArticulo.getValorTarifa().doubleValue() * dtoArticulo.getCantidadSolicitada();
						}
						valorAutorizar=valorCierreTemporal+valorTarifa;
						if(valorAutorizar <= valorParametrizacionPresupuesto){
							dtoValidacionPresupuesto.setValido(true);
							dtoValidacionPresupuesto.setMensaje(null);
							dtoValidacionPresupuesto.setCodigoValidacion(null);
							dtoValidacionPresupuesto.setNombreServicioArticulo(null);
							dtoValidacionPresupuesto.setFechaValidacion(null);
							dtoValidacionPresupuesto.setConvenioValidacion(null);
							dtoValidacionPresupuesto.setContratoValidacion(null);
							dtoValidacionPresupuesto.setAnioValidacion(null);
							dtoValidacionPresupuesto.setMesValidacion(null);
						}
						else{
							dtoValidacionPresupuesto.setValido(false);
							ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.valorArticulosSobrepasaTope", 
									dtoArticulo.getDescripcion());
							dtoValidacionPresupuesto.setMensajeError(error);
							
							/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
														"ValidacionPresupuestoCapitacion.valorArticulosSobrepasaTope", 
														new Object[]{dtoArticulo.getDescripcion()}));*/
							dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoSobrepasaTope);
							dtoValidacionPresupuesto.setNombreServicioArticulo(dtoArticulo.getDescripcion());
							dtoValidacionPresupuesto.setFechaValidacion(null);
							dtoValidacionPresupuesto.setConvenioValidacion(null);
							dtoValidacionPresupuesto.setContratoValidacion(null);
							dtoValidacionPresupuesto.setAnioValidacion(null);
							dtoValidacionPresupuesto.setMesValidacion(null);
						}
					}
					else{
						
						dtoValidacionPresupuesto.setValido(false);
						ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.noParametrizacionPresupuestoArticulo", 
								String.valueOf(dtoArticulo.getCodigo()), nombreConvenio, numeroContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), 
								UtilidadFecha.obtenerNombreMesProperties(fechaActual.get(Calendar.MONTH)));
						dtoValidacionPresupuesto.setMensajeError(error);
						
						/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
													"ValidacionPresupuestoCapitacion.noParametrizacionPresupuestoArticulo", 
													new Object[]{dtoArticulo.getCodigo(), nombreConvenio, numeroContrato, fechaActual.get(Calendar.YEAR), 
															UtilidadFecha.obtenerNombreMesProperties(fechaActual.get(Calendar.MONTH))}));*/
						dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoNoParametrizacion);
						dtoValidacionPresupuesto.setNombreServicioArticulo(dtoArticulo.getDescripcion());
						dtoValidacionPresupuesto.setFechaValidacion(null);
						dtoValidacionPresupuesto.setConvenioValidacion(nombreConvenio);
						dtoValidacionPresupuesto.setContratoValidacion(numeroContrato);
						dtoValidacionPresupuesto.setAnioValidacion(String.valueOf(fechaActual.get(Calendar.YEAR)));
						dtoValidacionPresupuesto.setMesValidacion(UtilidadFecha.obtenerNombreMesProperties(fechaActual.get(Calendar.MONTH)));
					}
				}
				else{
					ICierrePresupuestoCapitacionoMundo cierrePresupuestoMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
					Calendar fechaAnterior = Calendar.getInstance();
					//Se resta un dia a la fecha actual para obtener la fecha anterior
					fechaAnterior.add(Calendar.DAY_OF_MONTH, -1);
					DtoTotalProcesoPresupuestoCapitado params = new DtoTotalProcesoPresupuestoCapitado();
					DtoProcesoPresupuestoCapitado paramsLog = new DtoProcesoPresupuestoCapitado();
					params.setFecha(fechaAnterior.getTime());
					params.setContrato(codigoContrato);
					params.setConvenio(codigoConvenio);
					paramsLog.setFechaCierre(fechaAnterior.getTime());
					paramsLog.setContrato(codigoContrato);
					paramsLog.setConvenio(codigoConvenio);
					//Para buscar por contrato y por convenio
					paramsLog.setCaseParaBusquedaLog(3);
					//Se Valida si existe cierre para la fecha Anterior
					List<DtoTotalProcesoPresupuestoCapitado> cierre=cierrePresupuestoMundo.obtenerCierresPresupuestocapitacion(params);
					if(cierre != null && !cierre.isEmpty()){
						existeCierre=true;
					}
					else{
						ArrayList<DtoLogCierrePresuCapita> log=cierrePresupuestoMundo.obtenerLogs(paramsLog);
						if(log != null && !log.isEmpty()){
							existeCierre=true;
						}
					}
					if(existeCierre){
						IParametrizacionPresupuestoCapitacionMundo parametrizacionPresupuestoMundo = CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
						//Validar que exista informacion de parametrización de presupuesto y de que tipo
						//Se valida primero si exite parametrización Detallada
						Double valorDetallado=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoDetalladoArticulos(
								codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
								codigoClaseInventario, articuloEntity.getNivelAtencion().getConsecutivo());
						if(valorDetallado != null){
							presupuestoDetallado=true;
							presupuestoGeneral=false;
							existeParametrizacionPresupuesto=true;
							valorParametrizacionPresupuesto=valorDetallado;
						}
						else{
							//Se valida que exista parametrización General
							Double valorGeneral=parametrizacionPresupuestoMundo.obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(
									codigoContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), fechaActual.get(Calendar.MONTH), 
									articuloEntity.getNivelAtencion().getConsecutivo(), ConstantesCapitacion.subSeccionArticulo);
							if(valorGeneral != null){
								presupuestoDetallado=false;
								presupuestoGeneral=true;
								existeParametrizacionPresupuesto=true;
								valorParametrizacionPresupuesto=valorGeneral;
							}
							else{
								//En caso de que no exista parametrización de presupuesto
								presupuestoDetallado=false;
								presupuestoGeneral=false;
								existeParametrizacionPresupuesto=false;
							}
						}
						if(existeParametrizacionPresupuesto){
							//Se valida que exista información de Cierre de Autorizaciones para la fecha anterior del sistema 
							DtoTotalProceso totalAutorizado=null;
							Double valorCierreAutorizaciones=0D;
							if(presupuestoGeneral){
								totalAutorizado=cierrePresupuestoMundo.obtenerTotalArticulosPorNivelPorProceso(codigoContrato, 
										articuloEntity.getNivelAtencion().getConsecutivo(), ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion,
										fechaAnterior.getTime(), fechaAnterior.getTime()).get(0);
							}
							else if(presupuestoDetallado){
								totalAutorizado=cierrePresupuestoMundo.obtenerTotalArticulosPorNivelPorClasePorProceso(codigoContrato, 
										articuloEntity.getNivelAtencion().getConsecutivo(), codigoClaseInventario,
										ConstantesIntegridadDominio.acronimoTipoProcesoAutorizacion, fechaAnterior.getTime(), 
										fechaAnterior.getTime()).get(0);
							}
							valorCierreAutorizaciones=totalAutorizado.getTotalProceso().doubleValue();
							ICierreTempNivelAteClaseInvArtMundo cierreTempNivelClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
							ICierreTempNivelAtenArtMundo cierreTempNivelArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAtenArtMundo();
							//Se valida que exista información de la Temporal Cierres Ordenes Medicas 
							//de Autorizaciones para la fecha del sistema
							//Se valida primero si se parametrizó presupuesto General
							Double cierreTemp=null;
							if(presupuestoGeneral){
								cierreTemp=cierreTempNivelArticuloMundo.obtenerValorCierreTemporalNivelArticulos(codigoContrato, 
										fechaActual.getTime(), articuloEntity.getNivelAtencion().getConsecutivo());
							}
							else if(presupuestoDetallado){
								cierreTemp=cierreTempNivelClaseInvArticuloMundo.obtenerValorCierreTemporalNivelClaseInventarioArticulo(
												codigoContrato,	fechaActual.getTime(), codigoClaseInventario, 
												articuloEntity.getNivelAtencion().getConsecutivo());
							}
							if(cierreTemp != null){
								valorCierreTemporal=cierreTemp;
							}
							else{
								valorCierreTemporal=0D;
							}
							//Se valida el Tope de Presupuesto
							if(dtoArticulo.getValorTarifa() != null){
								valorTarifa=dtoArticulo.getValorTarifa().doubleValue() * dtoArticulo.getCantidadSolicitada();
							}
							valorAutorizar=valorCierreAutorizaciones+valorCierreTemporal+valorTarifa;
							if(valorAutorizar <= valorParametrizacionPresupuesto){
								dtoValidacionPresupuesto.setValido(true);
								dtoValidacionPresupuesto.setMensaje(null);
								dtoValidacionPresupuesto.setCodigoValidacion(null);
								dtoValidacionPresupuesto.setNombreServicioArticulo(null);
								dtoValidacionPresupuesto.setFechaValidacion(null);
								dtoValidacionPresupuesto.setConvenioValidacion(null);
								dtoValidacionPresupuesto.setContratoValidacion(null);
								dtoValidacionPresupuesto.setAnioValidacion(null);
								dtoValidacionPresupuesto.setMesValidacion(null);
							}
							else{
								dtoValidacionPresupuesto.setValido(false);
								ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.valorArticulosSobrepasaTope", 
										String.valueOf(dtoArticulo.getCodigo()), nombreConvenio, numeroContrato, String.valueOf(fechaActual.get(Calendar.YEAR)));
								dtoValidacionPresupuesto.setMensajeError(error);
								
								/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
															"ValidacionPresupuestoCapitacion.valorArticulosSobrepasaTope", 
															new Object[]{dtoArticulo.getCodigo(),nombreConvenio, numeroContrato, fechaActual.get(Calendar.YEAR)}));*/
								dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoSobrepasaTope);
								dtoValidacionPresupuesto.setNombreServicioArticulo(dtoArticulo.getDescripcion());
								dtoValidacionPresupuesto.setFechaValidacion(null);
								dtoValidacionPresupuesto.setConvenioValidacion(null);
								dtoValidacionPresupuesto.setContratoValidacion(null);
								dtoValidacionPresupuesto.setAnioValidacion(null);
								dtoValidacionPresupuesto.setMesValidacion(null);
							}
						}
						else{
							dtoValidacionPresupuesto.setValido(false);
							ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.noParametrizacionPresupuestoArticulo", 
									nombreConvenio, numeroContrato, String.valueOf(fechaAnterior.get(Calendar.YEAR)), UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)),
									dtoArticulo.getDescripcion());
							dtoValidacionPresupuesto.setMensajeError(error);
							
							/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
														"ValidacionPresupuestoCapitacion.noParametrizacionPresupuestoArticulo", 
														new Object[]{nombreConvenio, numeroContrato, fechaAnterior.get(Calendar.YEAR), 
																UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)),
																dtoArticulo.getDescripcion()}));*/
							dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoNoParametrizacion);
							dtoValidacionPresupuesto.setNombreServicioArticulo(dtoArticulo.getDescripcion());
							dtoValidacionPresupuesto.setFechaValidacion(null);
							dtoValidacionPresupuesto.setConvenioValidacion(nombreConvenio);
							dtoValidacionPresupuesto.setContratoValidacion(numeroContrato);
							dtoValidacionPresupuesto.setAnioValidacion(String.valueOf(fechaAnterior.get(Calendar.YEAR)));
							dtoValidacionPresupuesto.setMesValidacion(UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)));
						}
					}
					else{
						dtoValidacionPresupuesto.setValido(false);
						SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
						ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.noExisteCierreArticulo", 
								String.valueOf(dtoArticulo.getCodigo()), format.format(fechaAnterior.getTime()), nombreConvenio, numeroContrato, String.valueOf(fechaActual.get(Calendar.YEAR)), 
								UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)));
						dtoValidacionPresupuesto.setMensajeError(error);
						
						/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
													"ValidacionPresupuestoCapitacion.noExisteCierreArticulo", 
													new Object[]{dtoArticulo.getCodigo(), format.format(fechaAnterior.getTime()), nombreConvenio, numeroContrato, 
															fechaAnterior.get(Calendar.YEAR), UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH))}));*/
						dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoNoCierre);
						dtoValidacionPresupuesto.setNombreServicioArticulo(dtoArticulo.getDescripcion());
						dtoValidacionPresupuesto.setFechaValidacion(format.format(fechaAnterior.getTime()));
						dtoValidacionPresupuesto.setConvenioValidacion(nombreConvenio);
						dtoValidacionPresupuesto.setContratoValidacion(numeroContrato);
						dtoValidacionPresupuesto.setAnioValidacion(String.valueOf(fechaAnterior.get(Calendar.YEAR)));
						dtoValidacionPresupuesto.setMesValidacion(UtilidadFecha.obtenerNombreMesProperties(fechaAnterior.get(Calendar.MONTH)));
					}
				}
			}else{
				dtoValidacionPresupuesto.setValido(false);
				ErrorMessage error = new ErrorMessage("errors.validacionPresupuesto.noExisteNivelAtencionArticulo", 
						dtoArticulo.getDescripcion());
				dtoValidacionPresupuesto.setMensajeError(error);
				/*dtoValidacionPresupuesto.setMensaje(messageResource.getMessage(
						"ValidacionPresupuestoCapitacion.noExisteNivelAtencionArticulo", 
						new Object[]{dtoArticulo.getDescripcion()}));*/
				dtoValidacionPresupuesto.setCodigoValidacion(ConstantesCapitacion.codigoValPresupuestoNoNivelAtencion);
				dtoValidacionPresupuesto.setNombreServicioArticulo(dtoArticulo.getDescripcion());
				dtoValidacionPresupuesto.setFechaValidacion(null);
				dtoValidacionPresupuesto.setConvenioValidacion(null);
				dtoValidacionPresupuesto.setContratoValidacion(null);
				dtoValidacionPresupuesto.setAnioValidacion(null);
				dtoValidacionPresupuesto.setMesValidacion(null);
			}
			return dtoValidacionPresupuesto;
		}
		catch(Exception e){
			Log4JManager.error("ERROR validarPresupuestoCapitacionArticulo",e);
		}
		return null;
	}
}
