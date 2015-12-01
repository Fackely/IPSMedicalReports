package com.princetonsa.mundo.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoErroresCargo;
import util.facturacion.InfoTarifa;
import util.facturacion.InfoTarifaVigente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase que implementa el patrón singleton y un método sincronizado
 * para el manejo de concurrencia en Det_Cargos
 * 
 * @author Ricardo Ruiz Combita
 *
 */
public class CargosSingleton {
	
	/**
	 * Instancia única de la clase
	 */
	private static CargosSingleton instance=null;
	
	/**
	 * Atributo para controlar el acceso a los mismo datos
	 */
	private static int numeroSolicitud;
	
	/**
	 * Para hacer logs debug / warn / error de esta Clase.
	 */
	private Logger logger = Logger.getLogger(CargosSingleton.class);
	
	/**
	 * Constructor de la clase
	 */
	public CargosSingleton(){
		
	}

	/**
	 * Método encargado de evaluar si se crea la nueva instancia o se retorna la misma
	 * @return the instance
	 */
	public static CargosSingleton getInstance(int numSolicitud) {
		if(instance==null){
			numeroSolicitud=numSolicitud;
			instance= new CargosSingleton();
			return instance;
		}
		else{
			if(numeroSolicitud==numSolicitud){
				return instance;
			}
			else{
				numeroSolicitud=numSolicitud;
				instance= new CargosSingleton();
				return instance;
			}
		}
	}
	
	
	/**
	 * Método encargado de realizar el proceso de actualización y borrado de los detCargos
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoSubcuenta
	 * @param paquetizado
	 * @param esPortatil
	 * @param codigoTipoSolicitud
	 * @return
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
	public synchronized InfoErroresCargo procesarDetalleCargoServicioArticulo(Connection con, boolean dejarPendiente, boolean esCita,		
																				int numeroSolicitud, int codigoViaIngreso, int codigoContrato,
																				int codigoTipoComplejidad, int codigoInstitucion, String observaciones, 
																				String loginUsuario, double codigoSubcuenta, double codigoSolicitudSubcuenta,
																				String cubierto, double codigoPadrePaquetes, String paquetizado,
																				int cantidadCargada, String requiereAutorizacion, String tipoDistribucion,  
																				int codigoEvolucionOPCIONAL, int codigoServicioOPCIONAL, double valorTarifaOPCIONAL,
																				int codigoConvenioOPCIONAL,	int codigoEsquemaTarifarioOPCIONAL,	int codigoTipoSolicitudOPCIONAL,
																				int codigoCentroCostoSolicitanteOPCIONAL, String numeroAutorizacionOPCIONAL, double porcentajeDescuentoOPCIONAL,
																				double valorUnitarioDescuentoOPCIONAL, boolean esRegistroNuevo, boolean insertarEnBD,
																				String esPortatil, boolean excento,	String fechaCalculoVigenciaOPCIONAL, double porcentajeDctoPromocionServicio, 
																				BigDecimal valorDescuentoPromocionServicio, double porcentajeHonorarioPromocionServicio, 
																				BigDecimal valorHonorarioPromocionServicio,	double programa, double porcentajeDctoBono,
																				BigDecimal valorDescuentoBono, double porcentajeDctoOdontologico, BigDecimal valorDescuentoOdontologico,
																				int detallePaqueteOdonConvenio, Cargos cargos) throws IPSException{
		
		double valorTarifaBase=ConstantesBD.codigoNuncaValidoDouble;
		double valorTarifaTotal=ConstantesBD.codigoNuncaValidoDouble;
		double valorRecargo= ConstantesBD.codigoNuncaValidoDouble;
		double porcentajeRecargo= ConstantesBD.codigoNuncaValidoDouble;
		String metodoAjusteEsquemaTarifario="";
		InfoErroresCargo erroresCargo= new InfoErroresCargo();
		Vector codigosComponentesPaquetes= new Vector();
		String dejarExcento=excento?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo;
		ArrayList<Integer> codDetalleAutorizaciones= new ArrayList<Integer>();
		ArrayList<Integer> codDetalleAutorizacionesEstancia= new ArrayList<Integer>();
		
		try{
			//SE CARGA LA INFORMACION OPCIONAL CUANDO NO EXISTE
			if(codigoTipoSolicitudOPCIONAL<=0){
				codigoTipoSolicitudOPCIONAL = Solicitud.getCodigoTipoSolicitud(con, numeroSolicitud+"");
			}
			if(codigoConvenioOPCIONAL<=0){	
				Contrato contrato= new Contrato();
				contrato.cargar(con, codigoContrato+"");
				codigoConvenioOPCIONAL = contrato.getCodigoConvenio();
			}
			if(codigoCentroCostoSolicitanteOPCIONAL<=0){
				codigoCentroCostoSolicitanteOPCIONAL = Solicitud.obtenerCodigoCentroCostoSolicitante(con, numeroSolicitud+"");
			}
			if (codigoServicioOPCIONAL<=ConstantesBD.codigoServicioNoDefinido){	
				if (codigoTipoSolicitudOPCIONAL==ConstantesBD.codigoTipoSolicitudEvolucion){
					codigoServicioOPCIONAL= cargos.obtenerServicioEvolucion(con, codigoEvolucionOPCIONAL);
				}
				else{
					codigoServicioOPCIONAL= cargos.busquedaCodigoServicioXSolicitud(con, numeroSolicitud, codigoTipoSolicitudOPCIONAL, esPortatil);
				}
			}
			String fechaCalculoVigencia="";
			int codigoCentroAtencionCargo=cargos.obtenerCentroAtencionCargoSolicitud(con, numeroSolicitud);
			if(codigoEsquemaTarifarioOPCIONAL<=0){
				fechaCalculoVigencia=UtilidadTexto.isEmpty(fechaCalculoVigenciaOPCIONAL)?UtilidadFecha.getFechaActual(con):fechaCalculoVigenciaOPCIONAL;
				fechaCalculoVigencia= UtilidadFecha.obtenerFechaSinHora(fechaCalculoVigencia);
				codigoEsquemaTarifarioOPCIONAL = Utilidades.obtenerEsquemasTarifarioServicioArticulo(con, codigoSubcuenta+"", codigoContrato, codigoServicioOPCIONAL, true, fechaCalculoVigencia, codigoCentroAtencionCargo);
			}
			//SE CARGA EL TIPO DE PACIENTE ASOCIADO A LA SOLICITUD.
			String tipoPaciente=UtilidadesManejoPaciente.obtenerTipoPacienteSolicitud(con, numeroSolicitud+"").getAcronimo();
			//SE CARGA EL POSIBLE DETALLE DE CARGO X RESPONSABLE, Y SE ELIMINAMOS PARA GENERARLO NUEVAMENTE
			if(!esRegistroNuevo && insertarEnBD)
			{	
				double codigoDetalleCargo=cargos.obtenerCodigoDetalleCargoXSolicitudSubCuentaServicio(con, numeroSolicitud, codigoSubcuenta, ConstantesBD.acronimoNo/*facturado*/, paquetizado, esPortatil);
				if(codigoDetalleCargo<=0){	
					erroresCargo.setTieneErroresCodigo(true);
					return erroresCargo;
				}
				//si es una solicitud tipo paquete se debe obtener el codigo del detalle del cargo, actualizarle el cargo_padre a null y al final cuando se genera nuevamente actualizarlo con el nuevo
				if(codigoTipoSolicitudOPCIONAL==ConstantesBD.codigoTipoSolicitudPaquetes){
					codigosComponentesPaquetes=cargos.obtenerCargosComponentesPaquete(con, codigoDetalleCargo);
					if(codigosComponentesPaquetes.size()>0){
						cargos.actualizarCargoPadreComponentesPaquetes(con, ConstantesBD.codigoNuncaValidoDoubleNegativo /*para que los coloque null*/, codigosComponentesPaquetes);
					}
				}
				//Antes de eliminar se buscan las autorizaciones que hagan referencia al detalleCargo a eliminar
				//con el fin de evitar errores de integridad
				codDetalleAutorizaciones= cargos.cargarDetAutorizacionesXCargo(con, codigoDetalleCargo); 
				codDetalleAutorizacionesEstancia= cargos.cargarDetAutorizacionesEstanciaXCargo(con, codigoDetalleCargo);
				//Si existen autorizacion estas se desasociacion del detalle cargo que se esta procesando
				if(codDetalleAutorizaciones != null && !codDetalleAutorizaciones.isEmpty()){
					cargos.actualizarCargoDetAutorizaciones(con, ConstantesBD.codigoNuncaValidoDoubleNegativo, codDetalleAutorizaciones);
				}
				if(codDetalleAutorizacionesEstancia != null && !codDetalleAutorizacionesEstancia.isEmpty()){
					cargos.actualizarCargoDetAutorizacionesEstancia(con, ConstantesBD.codigoNuncaValidoDoubleNegativo, codDetalleAutorizacionesEstancia);
				}
				if(!cargos.eliminarDetalleCargoXCodigoDetalle(con, codigoDetalleCargo)){
					erroresCargo.setTieneErroresCodigo(true);
					return erroresCargo;
				}
			}	
		
			//SE ASIGNA EL POSIBLE ERROR DEL TIPO DE COMPLEJIDAD - SERVICIO - ESQUEMA TARIFARIO
			//los errores de tipo complejidad solo se deben agregar cuando el valor de la tarifa opcional es vacia
			if(valorTarifaOPCIONAL<1){
				if(Convenio.convenioManejaComplejidad(con, codigoConvenioOPCIONAL) && codigoTipoComplejidad<=0){
					erroresCargo.setMensajesErrorDetalle("error.tipoComplejidad.noExiste");
				}
				else{
					if(codigoServicioOPCIONAL<=ConstantesBD.codigoServicioNoDefinido){
					    erroresCargo.setMensajesErrorDetalle("error.cargo.noSeEspecificoServicio");
					}
					else{
						if(codigoEsquemaTarifarioOPCIONAL<=0){
						    erroresCargo.setMensajesErrorDetalle("error.cargo.esquemaNoSeleccionado");
						}
					}
				}
			}
			
			if(erroresCargo.getTieneErrores() || dejarPendiente){
				if(insertarEnBD){
					//SE INSERTA UN CARGO INCORRECTO Y SE DEJA EN ESTADO FACTURACION PENDIENTE
					DtoDetalleCargo detalleCargo= new DtoDetalleCargo(ConstantesBD.codigoNuncaValidoDouble, codigoSubcuenta, 
															codigoConvenioOPCIONAL, codigoEsquemaTarifarioOPCIONAL, cantidadCargada, 
															ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble, 
															ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble,
															ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble, 
															porcentajeDescuentoOPCIONAL, valorUnitarioDescuentoOPCIONAL, 
															ConstantesBD.codigoNuncaValidoDouble, requiereAutorizacion, numeroAutorizacionOPCIONAL,
															ConstantesBD.codigoEstadoFPendiente, cubierto, tipoDistribucion, numeroSolicitud, 
															codigoServicioOPCIONAL, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,
															ConstantesBD.codigoNuncaValido, ConstantesBD.acronimoNo, codigoTipoSolicitudOPCIONAL, 
															paquetizado, codigoPadrePaquetes, codigoSolicitudSubcuenta, observaciones, codigoContrato, 
															false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.codigoNuncaValido, 
															ConstantesBD.codigoNuncaValido, esPortatil, dejarExcento, porcentajeDctoPromocionServicio, 
															valorDescuentoPromocionServicio, porcentajeHonorarioPromocionServicio, 
															valorHonorarioPromocionServicio, programa, porcentajeDctoBono,	valorDescuentoBono, 
															porcentajeDctoOdontologico, valorDescuentoOdontologico, detallePaqueteOdonConvenio);
					//Se inserta en la BD el nuevo Detalle Cargo
					detalleCargo.setCodigoDetalleCargo(cargos.insertarDetalleCargos(con, detalleCargo, loginUsuario));
					cargos.setDtoDetalleCargo(detalleCargo);
					//Si se pudo insertar Correctamente
					if(detalleCargo.getCodigoDetalleCargo()!=ConstantesBD.codigoNuncaValidoDouble){
						//Si existen autorizacion estas se asociacion del detalle cargo nuevo que se esta creando
						if(codDetalleAutorizaciones != null && !codDetalleAutorizaciones.isEmpty()){
							cargos.actualizarCargoDetAutorizaciones(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizaciones);
						}
						if(codDetalleAutorizacionesEstancia != null && !codDetalleAutorizacionesEstancia.isEmpty()){
							cargos.actualizarCargoDetAutorizacionesEstancia(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizacionesEstancia);
						}
						//Se insetan en la BD los errres de detalle
						if(erroresCargo.getMensajesErrorDetalle().size() > 0){
							for(int w=0; w<erroresCargo.getMensajesErrorDetalle().size();w++){
								cargos.insertarErrorDetalleCargo(con, detalleCargo.getCodigoDetalleCargo() ,erroresCargo.getMensajesErrorDetalle(w));
							}
						}
						if(codigosComponentesPaquetes.size()>0){
							cargos.actualizarCargoPadreComponentesPaquetes(con, detalleCargo.getCodigoDetalleCargo(), codigosComponentesPaquetes);
						}
					}else{
						erroresCargo.setTieneErroresCodigo(true);
						return erroresCargo;
					}
				}
				return erroresCargo;
			}
		
			//Si no se presentarón errores se calcula el valor de as tarifas
			String metodoAjuste= this.obtenerMetodoAjusteConvenio(con, codigoConvenioOPCIONAL);
			if(!UtilidadTexto.isEmpty(metodoAjuste)){
				metodoAjusteEsquemaTarifario = metodoAjuste;
			}
			else{
				metodoAjusteEsquemaTarifario=obtenerMetodoAjusteEsqTarifario(con, codigoEsquemaTarifarioOPCIONAL, codigoInstitucion);
			}
			//como existe servicio y contrato entonces evaluamos que la tarifa base sea mayor que cero
			int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifarioOPCIONAL);
			if(valorTarifaOPCIONAL>0){
				valorTarifaBase=valorTarifaOPCIONAL;
			}
			else{
				InfoTarifaVigente tarifaVigente=cargos.obtenerTarifaBaseServicio(con, codigoTipoTarifario, codigoServicioOPCIONAL, codigoEsquemaTarifarioOPCIONAL, fechaCalculoVigencia);
				valorTarifaBase=tarifaVigente.getValorTarifa();
			}
			if(valorTarifaBase<=0)
			{
				if(codigoTipoTarifario==ConstantesBD.codigoTarifarioISS){
					erroresCargo.setMensajesErrorDetalle("error.cargo.noHayTarifaEsquemaTarifarioISSCita"+ConstantesBD.separadorTags+codigoServicioOPCIONAL);
				}
				else if (codigoTipoTarifario==ConstantesBD.codigoTarifarioSoat){	
					erroresCargo.setMensajesErrorDetalle("error.cargo.noHayTarifaEsquemaTarifarioSoatCita"+ConstantesBD.separadorTags+codigoServicioOPCIONAL);
				}
			}
		
			if(erroresCargo.getTieneErrores()){
				if(insertarEnBD){
					//SE INSERTA UN CARGO INCORRECTO Y SE DEJA EN ESTADO FACTURACION PENDIENTE
					DtoDetalleCargo detalleCargo= new DtoDetalleCargo(ConstantesBD.codigoNuncaValidoDouble, codigoSubcuenta, 
															codigoConvenioOPCIONAL, codigoEsquemaTarifarioOPCIONAL, cantidadCargada, 
															ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble, 
															ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble,
															ConstantesBD.codigoNuncaValidoDouble, ConstantesBD.codigoNuncaValidoDouble, 
															porcentajeDescuentoOPCIONAL, valorUnitarioDescuentoOPCIONAL, 
															ConstantesBD.codigoNuncaValidoDouble, requiereAutorizacion, numeroAutorizacionOPCIONAL,
															ConstantesBD.codigoEstadoFPendiente, cubierto, tipoDistribucion, numeroSolicitud, 
															codigoServicioOPCIONAL, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,
															ConstantesBD.codigoNuncaValido, ConstantesBD.acronimoNo, codigoTipoSolicitudOPCIONAL, 
															paquetizado, codigoPadrePaquetes, codigoSolicitudSubcuenta, observaciones, codigoContrato, 
															false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.codigoNuncaValido, 
															ConstantesBD.codigoNuncaValido, esPortatil, dejarExcento, porcentajeDctoPromocionServicio, 
															valorDescuentoPromocionServicio, porcentajeHonorarioPromocionServicio, 
															valorHonorarioPromocionServicio, programa, porcentajeDctoBono,
															valorDescuentoBono, porcentajeDctoOdontologico, valorDescuentoOdontologico, 
															detallePaqueteOdonConvenio);
					//Se inserta en la BD el nuevo Detalle Cargo
					detalleCargo.setCodigoDetalleCargo(cargos.insertarDetalleCargos(con, detalleCargo, loginUsuario));
					cargos.setDtoDetalleCargo(detalleCargo);
					//Si se pudo insertar Correctamente
					if(detalleCargo.getCodigoDetalleCargo()!=ConstantesBD.codigoNuncaValidoDouble){
						//Si existen autorizacion estas se asociacion del detalle cargo nuevo que se esta creando
						if(codDetalleAutorizaciones != null && !codDetalleAutorizaciones.isEmpty()){
							cargos.actualizarCargoDetAutorizaciones(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizaciones);
						}
						if(codDetalleAutorizacionesEstancia != null && !codDetalleAutorizacionesEstancia.isEmpty()){
							cargos.actualizarCargoDetAutorizacionesEstancia(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizacionesEstancia);
						}
						//Se insetan en la BD los errres de detalle
						if(erroresCargo.getMensajesErrorDetalle().size() > 0){
							for(int w=0; w<erroresCargo.getMensajesErrorDetalle().size();w++){
								cargos.insertarErrorDetalleCargo(con, detalleCargo.getCodigoDetalleCargo() ,erroresCargo.getMensajesErrorDetalle(w));
							}
						}
						if(codigosComponentesPaquetes.size()>0){
							cargos.actualizarCargoPadreComponentesPaquetes(con, detalleCargo.getCodigoDetalleCargo(), codigosComponentesPaquetes);
						}
					}else{
						erroresCargo.setTieneErroresCodigo(true);
						return erroresCargo;
					}
				}
				return erroresCargo;
			}
		
			//solo aplica la excepción cuando la tarifa no viene como parametro opcional
			valorTarifaTotal=valorTarifaBase;
			if(valorTarifaOPCIONAL<=0){	
				InfoTarifa excepcionTarifa= cargos.obtenerExcepcionesTarifasServicio(con, codigoViaIngreso, tipoPaciente, 
												codigoTipoComplejidad, codigoContrato, codigoServicioOPCIONAL, codigoInstitucion,
												fechaCalculoVigencia, codigoCentroAtencionCargo);  
				if(excepcionTarifa.getExiste()){
					valorTarifaTotal=this.asignarValorTarifaConExcepcion(excepcionTarifa, valorTarifaTotal,valorTarifaBase);
				}
			}	
			else{
				valorTarifaTotal=valorTarifaOPCIONAL;
			}
			if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario) && valorTarifaTotal>0){
				//solo se aplica el método de ajuste cuando el valor de la tarifa es > 0
				valorTarifaTotal=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal);
			}
			//SE Calcula el valor de la tarifa con recargo
			//obtenemos el valor del recargo de la tarifa
			InfoTarifa recargoTarifa= cargos.obtenerRecargoServicio(con, codigoViaIngreso, tipoPaciente, codigoServicioOPCIONAL, 
										codigoContrato, codigoTipoSolicitudOPCIONAL, numeroSolicitud);
			if(recargoTarifa.getExiste()){
				if (recargoTarifa.getPorcentajes().size()>0 && Utilidades.convertirADouble(recargoTarifa.getPorcentajes().get(0)+"")>0){
					porcentajeRecargo=Double.parseDouble(recargoTarifa.getPorcentajes().get(0));
					valorRecargo= valorTarifaTotal*(porcentajeRecargo/100.0);
				}
				if (Utilidades.convertirADouble(recargoTarifa.getValor())>0){
					valorRecargo= Double.parseDouble(recargoTarifa.getValor());
				}
				if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario) && valorRecargo>0){ 
					//solo se aplica método de ajuste cuando el valor del recargo es > 0
					valorRecargo=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorRecargo);
				}
			}
		
			//Se obtiene el descuento de la tarifa
			if(valorUnitarioDescuentoOPCIONAL<0 && porcentajeDescuentoOPCIONAL<0){	
				InfoTarifa descuentoTarifa= cargos.obtenerDescuentoComercialXConvenioServicio(con, codigoViaIngreso, tipoPaciente, 
												codigoContrato, codigoServicioOPCIONAL, codigoInstitucion,fechaCalculoVigencia);
				if(descuentoTarifa.getExiste()){
					if (descuentoTarifa.getPorcentajes().size()>0 && Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0)+"")>0){
						porcentajeDescuentoOPCIONAL=Utilidades.convertirADouble(descuentoTarifa.getPorcentajes().get(0),true);
						valorUnitarioDescuentoOPCIONAL= valorTarifaTotal*(porcentajeDescuentoOPCIONAL/100.0);
					}
					if (Utilidades.convertirADouble(descuentoTarifa.getValor())>0){
						valorUnitarioDescuentoOPCIONAL= Utilidades.convertirADouble(descuentoTarifa.getValor(),true);
					}
					if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario)){
						valorUnitarioDescuentoOPCIONAL=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorUnitarioDescuentoOPCIONAL);
					}
				}
			}
			//Se debe validar que el valor del descuento no supere el valor del cargo total mas los recargos
			//ES UNITARIO X ESTA RAZON NO SE MULTIPLICA POR LA CANTIDAD
			if(valorUnitarioDescuentoOPCIONAL> (valorTarifaTotal+valorRecargo)){
				valorUnitarioDescuentoOPCIONAL=(valorTarifaTotal+valorRecargo);
			}
		
			//Se evalua si es una inclusión - exclusión
			int estado=ConstantesBD.codigoNuncaValido;
			
			HashMap<String,Integer> datosCargoXPartes = null;
			
			if (!excento){
				
				InfoDatosString inclusionesExclusiones= cargos.obtenerInclusionExclusionXConvenioServicio(con, codigoContrato, 
													codigoCentroCostoSolicitanteOPCIONAL, codigoServicioOPCIONAL, 
													codigoInstitucion,fechaCalculoVigencia);
				
				estado=ConstantesBD.codigoEstadoFCargada;
				
				datosCargoXPartes= new HashMap<String, Integer>();
				
				datosCargoXPartes.put("numRegistros", 1); 

				datosCargoXPartes.put("estado_0", estado);
				datosCargoXPartes.put("cantidadServicios_0", cantidadCargada);
				
				
				//valida si el idicativo incluye es Si
				if(inclusionesExclusiones.getAcronimo().equals(ConstantesBD.acronimoSi)){
					
					//Verifica si existe cantidad - si existe se validan los cargos que existen para el paciente en el ingreso con convenio no halla superado el maximo
					//Valida que exista Valor en inclusionesExclusiones
					if(inclusionesExclusiones.getValueInt() != ConstantesBD.codigoNuncaValido){
						int cantidadInclusionesOrdenes = cargos.consultarCantidadInclusionesExclusionesOrdenServicio( con, (int)codigoSubcuenta, codigoServicioOPCIONAL);
					
						//Valida que el Total de las inclusiones sea un valor valido
						if(cantidadInclusionesOrdenes != ConstantesBD.codigoNuncaValido){
							
							//Valida que la cantidad en inclusionesExclusiones sea mayor que la cantidad de Inclusiones en las ordenes
							if(inclusionesExclusiones.getValueInt() > cantidadInclusionesOrdenes){
								
								//Valida cantidad servicios en la orden sea valido
								if(cantidadCargada > 0){
									int totalInclusiones = cantidadInclusionesOrdenes + cantidadCargada;
									
									//Valida cantidad en inclusionesExclusiones sea mayor o igual la cantidad inclusiones en las ordenes mas las de la nueva orden
									if(inclusionesExclusiones.getValueInt() >= totalInclusiones){
										datosCargoXPartes.put("estado_0", ConstantesBD.codigoEstadoFExento);
									
									}else{
										//Se carga la orden por partes
										
										int solicitudSinExclusion = totalInclusiones - inclusionesExclusiones.getValueInt();
										int solicitudExclusion = cantidadCargada - solicitudSinExclusion;
										
										datosCargoXPartes.clear();
										datosCargoXPartes.put("numRegistros", 2); 
										
										datosCargoXPartes.put("estado_0", ConstantesBD.codigoEstadoFCargada);
										datosCargoXPartes.put("cantidadServicios_0", solicitudSinExclusion);
										
										datosCargoXPartes.put("estado_1", ConstantesBD.codigoEstadoFExento);
										datosCargoXPartes.put("cantidadServicios_1", solicitudExclusion);

									}

								}								
								
							}
							
						}
						
					}else{
						//si no existe cantidad todos los servicios se van como excentos
						datosCargoXPartes.put("estado_0", ConstantesBD.codigoEstadoFExento);
					}	
	
				}
				
			}
			
			/*
			 * SE CALCULA EL METODO DE AJUSTE ESQUEMA TARIFARIO
			 * ESTE METODO REDONDEA EL VALOR DE LA TARIFA 
			 * APLICA SOLAMENTE CUANDO  ESTA PARAMETRIZADO EN EL METODO DE AJUSTE ESQUEMA TARIFARIO 
			 * Y EL VALOR DE LA TARIFA ES MAYOR QUE CERO
			 */
			if(!UtilidadTexto.isEmpty(metodoAjusteEsquemaTarifario)&&valorTarifaTotal>0){
				valorTarifaTotal=UtilidadValidacion.aproximarMetodoAjuste(metodoAjusteEsquemaTarifario, valorTarifaTotal);
			}
			
			//Iteramos datosCargoXPartes x si la orden se carga por partes
			for(int i=0 ; i < datosCargoXPartes.get("numRegistros") ; i++ ){
				
				cantidadCargada = datosCargoXPartes.get("cantidadServicios_"+i);
				estado = datosCargoXPartes.get("estado_"+i);
				
				
				if(insertarEnBD){	
					//SE INSERTA UN CARGO CORRECTO Y SE DEJA EN ESTADO FACTURACION CARGADO
					DtoDetalleCargo detalleCargo= new DtoDetalleCargo(ConstantesBD.codigoNuncaValidoDouble, codigoSubcuenta, 
															codigoConvenioOPCIONAL, codigoEsquemaTarifarioOPCIONAL, cantidadCargada, 
															valorTarifaTotal, valorTarifaTotal, 
															(cantidadCargada*valorTarifaTotal), ConstantesBD.codigoNuncaValidoDouble,
															porcentajeRecargo, valorRecargo, 
															porcentajeDescuentoOPCIONAL, valorUnitarioDescuentoOPCIONAL, 
															ConstantesBD.codigoNuncaValidoDouble, requiereAutorizacion, numeroAutorizacionOPCIONAL,
															estado, cubierto, tipoDistribucion, numeroSolicitud, 
															codigoServicioOPCIONAL, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,
															ConstantesBD.codigoNuncaValido, ConstantesBD.acronimoNo, codigoTipoSolicitudOPCIONAL, 
															paquetizado, codigoPadrePaquetes, codigoSolicitudSubcuenta, observaciones, codigoContrato, 
															false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.codigoNuncaValido, 
															ConstantesBD.codigoNuncaValido, esPortatil, dejarExcento, porcentajeDctoPromocionServicio, 
															valorDescuentoPromocionServicio, porcentajeHonorarioPromocionServicio,
															valorHonorarioPromocionServicio, programa, porcentajeDctoBono,
															valorDescuentoBono, porcentajeDctoOdontologico, valorDescuentoOdontologico,
															detallePaqueteOdonConvenio);
					//Se inserta en la BD el nuevo Detalle Cargo
					detalleCargo.setCodigoDetalleCargo(cargos.insertarDetalleCargos(con, detalleCargo, loginUsuario));
					cargos.setDtoDetalleCargo(detalleCargo);
					//Si se pudo insertar Correctamente
					if(detalleCargo.getCodigoDetalleCargo()!=ConstantesBD.codigoNuncaValidoDouble){
						//Si existen autorizacion estas se asociacion del detalle cargo nuevo que se esta creando
						if(codDetalleAutorizaciones != null && !codDetalleAutorizaciones.isEmpty()){
							cargos.actualizarCargoDetAutorizaciones(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizaciones);
						}
						if(codDetalleAutorizacionesEstancia != null && !codDetalleAutorizacionesEstancia.isEmpty()){
							cargos.actualizarCargoDetAutorizacionesEstancia(con, detalleCargo.getCodigoDetalleCargo(), codDetalleAutorizacionesEstancia);
						}
						//Se insetan en la BD los errores del detalle
						if(erroresCargo.getMensajesErrorDetalle().size() > 0){
							for(int w=0; w<erroresCargo.getMensajesErrorDetalle().size();w++){
								cargos.insertarErrorDetalleCargo(con, detalleCargo.getCodigoDetalleCargo() ,erroresCargo.getMensajesErrorDetalle(w));
							}
						}
						if(codigosComponentesPaquetes.size()>0){
							cargos.actualizarCargoPadreComponentesPaquetes(con, detalleCargo.getCodigoDetalleCargo(), codigosComponentesPaquetes);
						}
					}else{
						erroresCargo.setTieneErroresCodigo(true);
						return erroresCargo;
					}
				}	
				else{
					DtoDetalleCargo dtoDetalleCargo = new DtoDetalleCargo(ConstantesBD.codigoNuncaValidoDouble, codigoSubcuenta, 
																codigoConvenioOPCIONAL, codigoEsquemaTarifarioOPCIONAL, 
																cantidadCargada, valorTarifaTotal, valorTarifaTotal/*valorUnitarioCargado*/, 
																(cantidadCargada*valorTarifaTotal) /*valorTotalCargado*/,  
																ConstantesBD.codigoNuncaValidoDouble /*porcentajecargado PERTENECE A LA DISTRIBUCION*/, 
																porcentajeRecargo, valorRecargo/*valorUnitarioRecargo*/, 
																porcentajeDescuentoOPCIONAL, valorUnitarioDescuentoOPCIONAL, 
																ConstantesBD.codigoNuncaValidoDouble /*valorUnitarioIva*/, 
																requiereAutorizacion, "",/* numeroAutorizacionOPCIONAL --- numeroAutorizacion,*/ 
																estado, cubierto, tipoDistribucion, numeroSolicitud, 
																codigoServicioOPCIONAL, ConstantesBD.codigoNuncaValido /*codigoArticulo*/, 
																ConstantesBD.codigoNuncaValido /*codigoServicioCx*/, 
																ConstantesBD.codigoNuncaValido /*codigoTipoAsocio*/, 
																ConstantesBD.acronimoNo /*facturado*/, codigoTipoSolicitudOPCIONAL, 
																paquetizado, codigoPadrePaquetes, codigoSolicitudSubcuenta, 
																observaciones, codigoContrato, false /*filtrarSoloCantidadesMayoresCero*/,
																ConstantesBD.codigoNuncaValido /*det_cx_honorarios*/,
																ConstantesBD.codigoNuncaValido /*det_asocio_cx_salas_mat*/,
																esPortatil, dejarExcento,porcentajeDctoPromocionServicio,
																valorDescuentoPromocionServicio, porcentajeHonorarioPromocionServicio,
																valorHonorarioPromocionServicio, programa, porcentajeDctoBono,
																valorDescuentoBono, porcentajeDctoOdontologico,	valorDescuentoOdontologico,
																detallePaqueteOdonConvenio);
					cargos.setDtoDetalleCargo(dtoDetalleCargo);
					erroresCargo.setTieneErroresCodigo(true);
					
					return erroresCargo;
				}
				
			}
		}	
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch(Exception e){
			Log4JManager.error(e.getMessage(),e);
			erroresCargo.setTieneErroresCodigo(true);
			return erroresCargo;
		}
		return erroresCargo;
	}
	
	
	/**
	 * obtiene  el metodo de ajuste del esquema tarifario
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @return
	 */
	private String obtenerMetodoAjusteConvenio(Connection con, int codigoConvenio) throws IPSException
	{
		Convenio convenio= new Convenio();
		String metodoAjuste="";
		try{
			convenio.cargarResumen(con, codigoConvenio);
			metodoAjuste=convenio.getAjusteServicios();
			if(UtilidadTexto.isEmpty(metodoAjuste)){
				return "";
			}else{
				if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteCentena)){
					return ConstantesBD.metodoAjusteCentena;
				}else if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteDecena)){
					return ConstantesBD.metodoAjusteDecena;
				}else if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoAjusteUnidad)){
					return ConstantesBD.metodoAjusteUnidad;
				}else if(metodoAjuste.equals(ConstantesIntegridadDominio.acronimoSinAjuste)){
					return ConstantesBD.metodoSinAjuste;
				}
				else{
					return "";
				}
			}
		} 
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			return "";
		}
	}
	
	/**
	 * obtiene  el metodo de ajuste del esquema tarifario
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param codigoInstitucion
	 * @return
	 */
	private String obtenerMetodoAjusteEsqTarifario(Connection con, int codigoEsquemaTarifario, int codigoInstitucion) throws IPSException{
		EsquemaTarifario esquema= new EsquemaTarifario();
		try {
			esquema.cargarXcodigo(con, codigoEsquemaTarifario, codigoInstitucion);
			return esquema.getMetodoAjuste().getAcronimo();
		} 
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			return "";
		}
	}
	
	/**
	 * metodo que asigna el valor tarifa con la excepcion
	 * @param excepcionTarifa
	 * @param valorTarifaTotal
	 * @param valorTarifaBase
	 */
	private double asignarValorTarifaConExcepcion(InfoTarifa excepcionTarifa, double valorTarifaTotal, double valorTarifaBase) 
	{
		if(!UtilidadTexto.isEmpty(excepcionTarifa.getNuevaTarifa())){
			valorTarifaTotal= Double.parseDouble( excepcionTarifa.getNuevaTarifa() );
		}	
		else if(excepcionTarifa.getPorcentajes().size()>0){	
			for(int w=0; w<excepcionTarifa.getPorcentajes().size(); w++){	
				valorTarifaTotal= valorTarifaBase * (1.0+( Double.parseDouble(excepcionTarifa.getPorcentajes().get(w))/100.0));
				valorTarifaBase=valorTarifaTotal;
			}	
		}	
		else if(!UtilidadTexto.isEmpty(excepcionTarifa.getValor())){	
			valorTarifaTotal+=Double.parseDouble(excepcionTarifa.getValor());
		}	
		return valorTarifaTotal;
	}

}
