package com.servinte.axioma.mundo.impl.capitacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.inventario.DtoArticulos;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.fabrica.ordenes.OrdenesFabricaMundo;
import com.servinte.axioma.mundo.fabrica.salasCirugia.SalasCirugiaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoOrdenesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.mundo.interfaz.inventario.IArticulosMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.ISolicitudesMundo;
import com.servinte.axioma.mundo.interfaz.salasCirugia.IPeticionQxMundo;
import com.servinte.axioma.persistencia.UtilidadTransaccion;



public class ProcesoOrdenesPresupuestoCapitacionMundo implements IProcesoOrdenesPresupuestoCapitacionMundo 
{
	 ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaPorServicio= new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
	 ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaPorArticulo = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
	 ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistenciasProcesoOrdenes= new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
	 private int esquemaTarServiciosValorizarOrden;
	 private int esquemaTarMedicamentosValorizarOrden;
	 private String descripcionEsquemaTarServiciosValorizarOrden;
	 private String descripcionEsquemaTarMedicamentosValorizarOrden;
	
	/**
	 * Este método se encarga de recorrer la consulta de órdenes médicas o ambulatorias
	 * y generar los totales y las inconsistencias del proceso órdenes 
	 * @param listaOrdenes listado de órdenes ambulatorias y médicas 
	 * @param institucion institución del usuario en sesión
	 * @return  ArrayList<DtoTotalProcesoPresupuestoCapitado> listas de totales e inconsistencias ordenadas por día y convenio-contrato
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> generarTotalesInconsistenciasProcesoOrdenes(
			ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenes, DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado)
	{
		
		Date fechaBase = dtoProcesoPresupuestoCapitado.getFechaInicio();
		ArrayList<Integer> contratos=new ArrayList<Integer>();
		
		// SE RECORRE EL RANGO DE FECHAS
		while(fechaBase.before(dtoProcesoPresupuestoCapitado.getFechaFin()) || fechaBase.equals(dtoProcesoPresupuestoCapitado.getFechaFin()))
		{
			contratos=new ArrayList<Integer>();
			for(DtoResultadoConsultaProcesosCierre dtoResultadoOrden:listaOrdenes)
			{
				if((dtoResultadoOrden.getFecha() != null && dtoResultadoOrden.getFecha().equals(fechaBase)) || dtoResultadoOrden.getFechaAnulacion().equals(fechaBase))
				{
					if(!contratos.contains(dtoResultadoOrden.getContrato()))
					{
						//SE RECORRE LA LISTA PARA CONTRATOS
						for (DtoResultadoConsultaProcesosCierre dtoResultadoOrdenParaContrato:listaOrdenes) 
						{
							if(dtoResultadoOrden.getContrato().intValue() == dtoResultadoOrdenParaContrato.getContrato().intValue()
//									&&UtilidadFecha.conversionFormatoFechaAAp(dtoResultadoOrden.getFecha()).equals(UtilidadFecha.conversionFormatoFechaAAp(dtoResultadoOrdenParaContrato.getFecha()))
									)
							{
								//SE GENERAN INCONSISTENCIAS DEL REGISTRO
								DtoInconsistenciasProcesoPresupuestoCapitado dtoInconsistencia=validarInconsistenciasOrdenes(dtoResultadoOrdenParaContrato, dtoProcesoPresupuestoCapitado.getInstitucion(), listaOrdenes, dtoProcesoPresupuestoCapitado.getFechaInicio());
								if(dtoInconsistencia!=null)
								{
									if(!UtilidadTexto.isEmpty(dtoInconsistencia.getTipoInconsistencia()))
										listaInconsistenciasProcesoOrdenes.add(dtoInconsistencia);
									else //SI NO SE GENERARON INCONSISTENCIAS CALCULA LOS TOTALES
									{
										if(dtoResultadoOrdenParaContrato.getCodigoArticulo()!=null){
											if(this.validarTotales(totalListaPorArticulo, dtoResultadoOrdenParaContrato))
												this.totalListaPorArticulo.add(this.generarTotalPorArticulo(dtoResultadoOrdenParaContrato, listaOrdenes, dtoProcesoPresupuestoCapitado.getInstitucion(), dtoProcesoPresupuestoCapitado.getFechaInicio()));
										}
										else
											if(dtoResultadoOrdenParaContrato.getCodigoServicio()!=null)
												if(this.validarTotales(totalListaPorServicio, dtoResultadoOrdenParaContrato))
													this.totalListaPorServicio.add(this.generarTotalPorServicio(dtoResultadoOrdenParaContrato, listaOrdenes, dtoProcesoPresupuestoCapitado.getInstitucion(), dtoProcesoPresupuestoCapitado.getFechaInicio()));
									}
								}
							}
						}
						contratos.add(dtoResultadoOrden.getContrato());
					}
				}
			}
			fechaBase = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(fechaBase), 1, false));
		}
		return null;
	}
	
	/**
	 * Validar si en las listas de totales no se ha calculado los valores para la misma fecha, contrato, y servicio o articulo
	 * @param lista lista totales por articulo o servicio
	 * @param dto registro a comparar
	 * @param isArticulo true si es articulo, false si es servicio
	 * @return true si se debe calcular los valores, false si ya han sido calculados
	 */
	public boolean validarTotales(ArrayList<DtoTotalProcesoPresupuestoCapitado> lista, DtoResultadoConsultaProcesosCierre dto)
	{
		if(dto.getCodigoArticulo()!=null)
		{
			for(DtoTotalProcesoPresupuestoCapitado totalArticulo: lista)
			{
				if((totalArticulo.getFecha().equals(dto.getFecha()) || totalArticulo.getFecha().equals(dto.getFechaAnulacion()))&&totalArticulo.getContrato().equals(dto.getContrato())
						&&totalArticulo.getCodigoArticulo().equals(dto.getCodigoArticulo()))
					return false;
			}
		}
		else
		{
			for(DtoTotalProcesoPresupuestoCapitado totalServicio: lista)
			{
				if((totalServicio.getFecha().equals(dto.getFecha()) || totalServicio.getFecha().equals(dto.getFechaAnulacion()))&&totalServicio.getContrato().equals(dto.getContrato())
						&&totalServicio.getCodigoServicio().equals(dto.getCodigoServicio()))
					return false;
			}
		}
		return true;
	}		
	
	/**
	 * Este método genera los totales del articulo en el registro enviado como parámetro
	 * para la misma fecha y convenio - contrato 
	 * @param DtoTotalProcesoPresupuestoCapitado registro que contiene el articulo a calcular el valor total
	 * @param ArrayList<DtoResultadoConsultaProcesosCierre> listado completo de las ordenes 
	 * @param institucion
	 * @param fechaCierre
	 * @return DtoTotalProcesoPresupuestoCapitado dto que contiene los totales por articulo del registro enviado
	 */
	public DtoTotalProcesoPresupuestoCapitado generarTotalPorArticulo(DtoResultadoConsultaProcesosCierre dtoResultadoOrdenPorContrato, ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenes, int institucion, Date fechaCierre){
		
		DtoTotalProcesoPresupuestoCapitado dtoValorTotal = new DtoTotalProcesoPresupuestoCapitado();
		dtoValorTotal.setValor(0.0);
		dtoValorTotal.setCantidadTotal(0);
		
		dtoValorTotal.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalArticulo);
		dtoValorTotal.setCodigoArticulo(dtoResultadoOrdenPorContrato.getCodigoArticulo());
		dtoValorTotal.setFecha(dtoResultadoOrdenPorContrato.getFecha());
		dtoValorTotal.setConvenio(dtoResultadoOrdenPorContrato.getConvenio());
		dtoValorTotal.setContrato(dtoResultadoOrdenPorContrato.getContrato());
			
		for (DtoResultadoConsultaProcesosCierre dtoResultadoConsultaProcesosCierre : listaOrdenes) 
		{
			//--- Si es de la misma fecha, mismo convenio y mismo contrato genera los totales de ese servicio o articulo
			if(
//				UtilidadFecha.conversionFormatoFechaAAp(dtoResultadoConsultaProcesosCierre.getFecha()).equals(UtilidadFecha.conversionFormatoFechaAAp(dtoResultadoOrdenPorContrato.getFecha()))&&
					dtoResultadoConsultaProcesosCierre.getConvenio().intValue()==dtoResultadoOrdenPorContrato.getConvenio().intValue()
					&&dtoResultadoConsultaProcesosCierre.getContrato().intValue()==dtoResultadoOrdenPorContrato.getContrato().intValue())
			{ 
				//Si es articulo y tiene esquema tarifario
				if(dtoResultadoConsultaProcesosCierre.getCodigoArticulo()!=null&&dtoResultadoConsultaProcesosCierre.getEsquemaTarifarioArticulo()!=null)
				{
					//MT6578 si el articulo viene de una orden ambulatoria y tiene asociada una solicitud no se toma en cuenta en el calculo				
						if((dtoResultadoConsultaProcesosCierre.getCodigoOrden()==null && dtoResultadoConsultaProcesosCierre.getCodigoPeticion()==null && dtoResultadoConsultaProcesosCierre.getNumeroSolicitud()!=null)
								||(dtoResultadoConsultaProcesosCierre.getCodigoOrden()!=null && dtoResultadoConsultaProcesosCierre.getCodigoPeticion()==null && dtoResultadoConsultaProcesosCierre.getNumeroSolicitud()==null)
								||(dtoResultadoConsultaProcesosCierre.getCodigoOrden()==null && dtoResultadoConsultaProcesosCierre.getCodigoPeticion()!=null && dtoResultadoConsultaProcesosCierre.getNumeroSolicitud()==null))
						 {								
						
					//Si el articulo corresponde al registro enviado y el esquema tarifario es el del parametro general se suman  
					if(dtoResultadoConsultaProcesosCierre.getCodigoArticulo().intValue()==dtoResultadoOrdenPorContrato.getCodigoArticulo().intValue()
							&&this.esquemaTarMedicamentosValorizarOrden==dtoResultadoConsultaProcesosCierre.getEsquemaTarifarioArticulo().intValue())
					{
						//Si tienen cantidad y tarifa definida
						if(dtoResultadoConsultaProcesosCierre.getCantidadServArt()!=null&&dtoResultadoConsultaProcesosCierre.getTarifa()!=null)
						{
							
							if(dtoResultadoConsultaProcesosCierre.getFechaAnulacion() != null) {
								//hermorhu - MT6601
								//Se valida que la fecha de Anulacion sea distinta a la fecha de la orden
								if(dtoResultadoConsultaProcesosCierre.getFecha() == null || !dtoResultadoConsultaProcesosCierre.getFechaAnulacion().equals(dtoResultadoConsultaProcesosCierre.getFecha())) {
									//Se valida que la fecha de anulacion sea igual a la del cierre
									if(dtoResultadoConsultaProcesosCierre.getFechaAnulacion().equals(fechaCierre)) {
										//En el caso que para la fecha de cierre se halla hecho una anulacion para un servicio con orden de fecha anterior se setea es la fecha de la anulacion
										dtoValorTotal.setFecha(dtoResultadoConsultaProcesosCierre.getFechaAnulacion());
										
										dtoValorTotal.setValor(dtoValorTotal.getValor()-(dtoResultadoConsultaProcesosCierre.getTarifa()*dtoResultadoConsultaProcesosCierre.getCantidadServArt()));
										dtoValorTotal.setCantidadTotal(dtoValorTotal.getCantidadTotal()-dtoResultadoConsultaProcesosCierre.getCantidadServArt().intValue());
									}
									//Si la anulacion no esta dentro del rango de fechas a realizar el cierre
									else {
										dtoValorTotal.setValor(dtoValorTotal.getValor()+(dtoResultadoConsultaProcesosCierre.getTarifa()*dtoResultadoConsultaProcesosCierre.getCantidadServArt()));
										dtoValorTotal.setCantidadTotal(dtoValorTotal.getCantidadTotal()+dtoResultadoConsultaProcesosCierre.getCantidadServArt().intValue());
									}
								}

							}else {
								dtoValorTotal.setValor(dtoValorTotal.getValor()+(dtoResultadoConsultaProcesosCierre.getTarifa()*dtoResultadoConsultaProcesosCierre.getCantidadServArt()));
								dtoValorTotal.setCantidadTotal(dtoValorTotal.getCantidadTotal()+dtoResultadoConsultaProcesosCierre.getCantidadServArt().intValue());
							}
							
						}
					}
				}
			}
				
		}
		}
		return dtoValorTotal;
	}
	
	/**
	 * Este método genera los totales del servicio en el registro enviado como parámetro
	 * para la misma fecha y convenio - contrato 
	 * @param DtoTotalProcesoPresupuestoCapitado registro que contiene el servicio a calcular el valor total
	 * @param ArrayList<DtoResultadoConsultaProcesosCierre> listado completo de las ordenes 
	 * @param institucion
	 * @param fechaCierre
	 * @return DtoTotalProcesoPresupuestoCapitado dto que contiene los totales por servicio del registro enviado
	 */
	public DtoTotalProcesoPresupuestoCapitado generarTotalPorServicio(DtoResultadoConsultaProcesosCierre dtoResultadoOrdenPorContrato, ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenes, int institucion, Date fechaCierre){
		
		DtoTotalProcesoPresupuestoCapitado dtoValorTotal = new DtoTotalProcesoPresupuestoCapitado();
		dtoValorTotal.setValor(0.0);
		dtoValorTotal.setCantidadTotal(0);
		
		dtoValorTotal.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalServicio);
		dtoValorTotal.setCodigoServicio(dtoResultadoOrdenPorContrato.getCodigoServicio());
		dtoValorTotal.setFecha(dtoResultadoOrdenPorContrato.getFecha());
		dtoValorTotal.setConvenio(dtoResultadoOrdenPorContrato.getConvenio());
		dtoValorTotal.setContrato(dtoResultadoOrdenPorContrato.getContrato());
		
		for (DtoResultadoConsultaProcesosCierre dtoResultadoConsultaProcesosCierre : listaOrdenes) 
		{
			//--- Si es de la misma fecha, mismo convenio y mismo contrato genera los totales de ese servicio o articulo
			if(
//				UtilidadFecha.conversionFormatoFechaAAp(dtoResultadoConsultaProcesosCierre.getFecha()).equals(UtilidadFecha.conversionFormatoFechaAAp(dtoResultadoOrdenPorContrato.getFecha()))&&
					dtoResultadoConsultaProcesosCierre.getConvenio().intValue()==dtoResultadoOrdenPorContrato.getConvenio().intValue()
					&&dtoResultadoConsultaProcesosCierre.getContrato().intValue()==dtoResultadoOrdenPorContrato.getContrato().intValue())
			{ 
				//Si es servicio
				if(dtoResultadoConsultaProcesosCierre.getCodigoServicio()!=null)
				{
					//MT6578: si el servicio viene de una orden ambulatoria o peticiony tiene asociada una solicitud no se toma en cuenta en el calculo
					if((dtoResultadoConsultaProcesosCierre.getCodigoOrden()==null && dtoResultadoConsultaProcesosCierre.getCodigoPeticion()==null && dtoResultadoConsultaProcesosCierre.getNumeroSolicitud()!=null)
						||(dtoResultadoConsultaProcesosCierre.getCodigoOrden()!=null && dtoResultadoConsultaProcesosCierre.getCodigoPeticion()==null && dtoResultadoConsultaProcesosCierre.getNumeroSolicitud()==null)
						||(dtoResultadoConsultaProcesosCierre.getCodigoOrden()==null && dtoResultadoConsultaProcesosCierre.getCodigoPeticion()!=null && dtoResultadoConsultaProcesosCierre.getNumeroSolicitud()==null))
					{
					//Si el servicio corresponde al registro enviado   
					if(dtoResultadoConsultaProcesosCierre.getCodigoServicio().intValue()==dtoResultadoOrdenPorContrato.getCodigoServicio().intValue()
							&&dtoResultadoConsultaProcesosCierre.getCantidadServArt()!=null&&dtoResultadoConsultaProcesosCierre.getTarifa()!=null)
						{
							if(dtoResultadoConsultaProcesosCierre.getFechaAnulacion()!= null) {
								//hermorhu - MT6601
								//Se valida que la fecha de Anulacion sea distinta a la fecha de la orden o que la fecha de la orden sea null caso cuando se trae las anulaciones de ordenes amb y cirugia 
								if(dtoResultadoConsultaProcesosCierre.getFecha() == null || !dtoResultadoConsultaProcesosCierre.getFechaAnulacion().equals(dtoResultadoConsultaProcesosCierre.getFecha())) {
									//Se valida que la fecha de anulacion sea igual a la del cierre
									if(dtoResultadoConsultaProcesosCierre.getFechaAnulacion().equals(fechaCierre)) {
										//En el caso que para la fecha de cierre se halla hecho una anulacion para un articulo con orden de fecha anterior se setea es la fecha de la anulacion
										dtoValorTotal.setFecha(dtoResultadoConsultaProcesosCierre.getFechaAnulacion());
										
										dtoValorTotal.setValor(dtoValorTotal.getValor()-(dtoResultadoConsultaProcesosCierre.getTarifa()*dtoResultadoConsultaProcesosCierre.getCantidadServArt()));
										dtoValorTotal.setCantidadTotal(dtoValorTotal.getCantidadTotal()-dtoResultadoConsultaProcesosCierre.getCantidadServArt().intValue());
									}
									//Si la anulacion no esta dentro del rango de fechas a realizar el cierre
									else {
										dtoValorTotal.setValor(dtoValorTotal.getValor()+(dtoResultadoConsultaProcesosCierre.getTarifa()*dtoResultadoConsultaProcesosCierre.getCantidadServArt()));
										dtoValorTotal.setCantidadTotal(dtoValorTotal.getCantidadTotal()+dtoResultadoConsultaProcesosCierre.getCantidadServArt().intValue());
									}
								}
								
							}else {
								dtoValorTotal.setValor(dtoValorTotal.getValor()+(dtoResultadoConsultaProcesosCierre.getTarifa()*dtoResultadoConsultaProcesosCierre.getCantidadServArt()));
								dtoValorTotal.setCantidadTotal(dtoValorTotal.getCantidadTotal()+dtoResultadoConsultaProcesosCierre.getCantidadServArt().intValue());
							}
						}
				}
			}
		}
		}
		return dtoValorTotal;
	}
	
	/**
	 * Este método se encarga de validar las inconsistencias de un registro, las inconsistencias pueden ser no definida la tarifa,
	 * no definido los parámetros generales de esquemas tarifarios para servicios o articulos, y no definido el nivel de atención 
	 * @param DtoResultadoConsultaProcesosCierre contiene un registro de la consulta para validar sus inconsistencias
	 * @param institucion institución del usuario en sesión
	 * @return DtoInconsistenciasProcesoPresupuestoCapitado inconsistencia generada en el registro
	 */
	public DtoInconsistenciasProcesoPresupuestoCapitado validarInconsistenciasOrdenes(DtoResultadoConsultaProcesosCierre dtoResultadoOrdenPorContrato,
			int institucion, ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenes , Date fechaCierre)
	{
		DtoInconsistenciasProcesoPresupuestoCapitado dtoInconsistencia = new DtoInconsistenciasProcesoPresupuestoCapitado();

		//hermorhu - MT6061 
		//La fecha de la inconsistencia en el caso de las anuladas no pede ser la fecha de la orden si no de la anulacion (misma fecha de cierre) 
//		dtoInconsistencia.setFecha(dtoResultadoOrdenPorContrato.getFecha());
		dtoInconsistencia.setFecha(fechaCierre);

		dtoInconsistencia.setCodigoConvenio(dtoResultadoOrdenPorContrato.getConvenio());
		dtoInconsistencia.setCodigoContrato(dtoResultadoOrdenPorContrato.getContrato());
		boolean continuar=true;
		
		//INCONSISTENCIAS PARA ARTICULOS
		if(dtoResultadoOrdenPorContrato.getCodigoArticulo()!=null)
		{
			//Se comprueba primero que el registro no se ha validado para generar las inconsistencias
			for(DtoInconsistenciasProcesoPresupuestoCapitado listaIncons: listaInconsistenciasProcesoOrdenes)
			{
				if(listaIncons.getServicioMedicamento().equals(dtoResultadoOrdenPorContrato.getCodigoArticulo()+"-"+dtoResultadoOrdenPorContrato.getNombreArticulo())
						&&listaIncons.getCodigoConvenio().equals(dtoResultadoOrdenPorContrato.getConvenio())&&listaIncons.getCodigoContrato().equals(dtoResultadoOrdenPorContrato.getContrato())
//						&&listaIncons.getFecha().equals(dtoResultadoOrdenPorContrato.getFecha())
						)
				{
					continuar=false;
				}
			}
			
			if(continuar)
			{
				//NIVEL ATENCION
				if(dtoResultadoOrdenPorContrato.getNivelAtencionArticulo()==null)
				{
					dtoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidoNivelAtencion);
					dtoInconsistencia.setDescripcion("No se encuentra definido el nivel de atención para el medicamento-insumo \""+dtoResultadoOrdenPorContrato.getNombreArticulo()+"\"");
					dtoInconsistencia.setServicioMedicamento(dtoResultadoOrdenPorContrato.getCodigoArticulo()+"-"+dtoResultadoOrdenPorContrato.getNombreArticulo());
					
				}
				else
					//PARAMETRO GENERAL
					if(this.esquemaTarMedicamentosValorizarOrden==ConstantesBD.codigoNuncaValido)
					{
						dtoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidoParametro);
						dtoInconsistencia.setDescripcion("No se encuentra definido el parámetro general \"Esquema Tarifario de Medicamentos e Insumos para Valorizar las Ordenes\"");
						dtoInconsistencia.setServicioMedicamento(dtoResultadoOrdenPorContrato.getCodigoArticulo()+"-"+dtoResultadoOrdenPorContrato.getNombreArticulo());
					}
					else
					{
						//TARIFA EN EL ESQUEMA TARIFARIO DEL PARAMETRO
						boolean tieneEsquema=false;
						for(DtoResultadoConsultaProcesosCierre dtoOrdenes:listaOrdenes)
						{
							if(
//								UtilidadFecha.conversionFormatoFechaAAp(dtoOrdenes.getFecha()).equals(UtilidadFecha.conversionFormatoFechaAAp(dtoResultadoOrdenPorContrato.getFecha()))&&
									dtoOrdenes.getConvenio().intValue()==dtoResultadoOrdenPorContrato.getConvenio().intValue()
									&&dtoOrdenes.getContrato().intValue()==dtoResultadoOrdenPorContrato.getContrato().intValue())
							{
								if(dtoOrdenes.getCodigoArticulo()!=null&&dtoOrdenes.getEsquemaTarifarioArticulo()!=null)
								{
									if(dtoOrdenes.getCodigoArticulo().intValue()==dtoResultadoOrdenPorContrato.getCodigoArticulo().intValue()
											&&this.esquemaTarMedicamentosValorizarOrden==dtoOrdenes.getEsquemaTarifarioArticulo().intValue())
									{
										tieneEsquema=true;
										if(dtoResultadoOrdenPorContrato.getTarifa()==null)
										{
											dtoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa);
											dtoInconsistencia.setDescripcion("No se encuentra definida la tarifa en el esquema tarifario \""+this.descripcionEsquemaTarMedicamentosValorizarOrden+"\"");
											dtoInconsistencia.setServicioMedicamento(dtoResultadoOrdenPorContrato.getCodigoArticulo()+"-"+dtoResultadoOrdenPorContrato.getNombreArticulo());
											break;
										}
									}
								}
							}
						}
						if(!tieneEsquema)
						{
							dtoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa);
							dtoInconsistencia.setDescripcion("No se encuentra definida la tarifa en el esquema tarifario \""+this.descripcionEsquemaTarMedicamentosValorizarOrden+"\"");
							dtoInconsistencia.setServicioMedicamento(dtoResultadoOrdenPorContrato.getCodigoArticulo()+"-"+dtoResultadoOrdenPorContrato.getNombreArticulo());
						}
					}
			}
					
		}
		//INCONSISTENCIAS PARA SERVICIOS
		else
		if(dtoResultadoOrdenPorContrato.getCodigoServicio()!=null){
			
			//Se comprueba primero que el registro no se ha validado para generar las inconsistencias
			for(DtoInconsistenciasProcesoPresupuestoCapitado listaIncons: listaInconsistenciasProcesoOrdenes)
			{
				if(listaIncons.getServicioMedicamento().equals(dtoResultadoOrdenPorContrato.getCodigoServicio()+"-"+dtoResultadoOrdenPorContrato.getNombreServicio())
						&&listaIncons.getCodigoConvenio().equals(dtoResultadoOrdenPorContrato.getConvenio())&&listaIncons.getCodigoContrato().equals(dtoResultadoOrdenPorContrato.getContrato())
//						&&listaIncons.getFecha().equals(dtoResultadoOrdenPorContrato.getFecha())
						)
				{
					continuar=false;
				}
			}
			
			if(continuar)
			{
				if(this.esquemaTarServiciosValorizarOrden==ConstantesBD.codigoNuncaValido)
				{
					dtoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidoParametro);
					dtoInconsistencia.setDescripcion("No se encuentra definido el parámetro general \"Esquema Tarifario de Servicios para Valorizar las Ordenes\"");
					dtoInconsistencia.setServicioMedicamento(dtoResultadoOrdenPorContrato.getCodigoServicio()+"-"+dtoResultadoOrdenPorContrato.getNombreServicio());
				}
				else
					//MT 6438 se agrega la validación del tipo de liquidación si es diferente a valor se genera una inconsistencia 
					
					if(dtoResultadoOrdenPorContrato.getCodigoTipoLiquidacion()!= ConstantesBD.codigoTipoLiquidacionSoatValor || dtoResultadoOrdenPorContrato.getCodigoTipoLiquidacion()==ConstantesBD.codigoNuncaValido)
					{
							dtoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa);
							dtoInconsistencia.setDescripcion("No se encuentra definido el tipo de liquidación vigente como valor en el esquema tarifario \""+this.descripcionEsquemaTarServiciosValorizarOrden+"\"");
							dtoInconsistencia.setServicioMedicamento(dtoResultadoOrdenPorContrato.getCodigoServicio()+"-"+dtoResultadoOrdenPorContrato.getNombreServicio());
					}
				
				
				//MT 6532-6438 
					else
						if(dtoResultadoOrdenPorContrato.getTarifa()==null||dtoResultadoOrdenPorContrato.getTarifa()==ConstantesBD.codigoNuncaValidoDouble)
						{
								dtoInconsistencia.setTipoInconsistencia(ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa);
								dtoInconsistencia.setDescripcion("No se encuentra definida la tarifa en el esquema tarifario \""+this.descripcionEsquemaTarServiciosValorizarOrden+"\"");
								dtoInconsistencia.setServicioMedicamento(dtoResultadoOrdenPorContrato.getCodigoServicio()+"-"+dtoResultadoOrdenPorContrato.getNombreServicio());
			}
		}
		}
		
		if(!continuar)
			return null;
		else
			return dtoInconsistencia;
	}
	
	
	
	/**
	 * Método principal que realiza todo el proceso de órdenes, invocando los métodos de consulta de órdenes
	 * y los métodos de generación de totales e inconsistencias
	 * @param DtoProcesoPresupuestoCapitado contiene los filtros de la consulta
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado> lista de totales e inconsistencias ordenadas por fecha y convenio-contrato  
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoOrdenes(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado) 
	{
		IOrdenesAmbulatoriasMundo mundoOrdenesAmbulatorias=OrdenesFabricaMundo.crearOrdenesAmbulatoriasMundo();
		ISolicitudesMundo solicitudMundo=OrdenesFabricaMundo.crearSolicitudesMundo();
		
		String esquemaTarServicios=ValoresPorDefecto.getEsquemaTariServiciosValorizarOrden(dtoProcesoPresupuestoCapitado.getInstitucion());
		
		if(UtilidadTexto.isEmpty(esquemaTarServicios))
			this.esquemaTarServiciosValorizarOrden=ConstantesBD.codigoNuncaValido;
		else{
			this.esquemaTarServiciosValorizarOrden=Utilidades.convertirAEntero(esquemaTarServicios.split("-")[0]);
			this.descripcionEsquemaTarServiciosValorizarOrden=esquemaTarServicios.split("-")[1];
		}
		
		String esquemaTarArticulos=ValoresPorDefecto.getEsquemaTariMedicamentosValorizarOrden(dtoProcesoPresupuestoCapitado.getInstitucion());
		
		if(UtilidadTexto.isEmpty(esquemaTarArticulos))
			this.esquemaTarMedicamentosValorizarOrden=ConstantesBD.codigoNuncaValido;
		else{
			this.esquemaTarMedicamentosValorizarOrden=Utilidades.convertirAEntero(esquemaTarArticulos.split("-")[0]);
			this.descripcionEsquemaTarMedicamentosValorizarOrden=esquemaTarArticulos.split("-")[1];
		}
		
		//CONSULTAR PETICIONES DE ARTICULOS
		IPeticionQxMundo peticionMundo= SalasCirugiaFabricaMundo.crearPeticionQxMundo();
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticionesArticulos=peticionMundo.consultarPeticionesArticulos(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticionesArticulosDefinitiva= this.obtenerTarifasServiciosArticulos(listaPeticionesArticulos);
		
		//CONSULTAR PETICIONES DE SERVICIOS
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticionesServicios=peticionMundo.consultarPeticionesServicios(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticionesServiciosDefinitiva= this.obtenerTarifasServiciosArticulos(listaPeticionesServicios);
		
		//CONSULTAR ORDENES AMBULATORIAS
		ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenesAmbulatorias=mundoOrdenesAmbulatorias.consultarOrdenesAmbulatorias(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenesAmbulatoriasDefinitiva= this.obtenerTarifasServiciosArticulos(listaOrdenesAmbulatorias); 
			
		//MT6601 - hermorhu
		//Consulta que trae las ordenes medicas anuladas con solo el numero de orden 
		//para que no presente conflicto con MT6578 
		//CONSULTAR ORDENES MEDICAS ANULADAS DE ORDEN AMBULATORIA
		ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenesMedicasAnuladas=solicitudMundo.consultarSolicitudesOrdenAmbAnuladas(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenesMedicasAnuladasDefinitiva= this.obtenerTarifasServiciosArticulos(listaOrdenesMedicasAnuladas);
		
		//CONSULTAR ORDENES MEDICAS
		ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenesMedicas=solicitudMundo.consultarSolicitudesEnSistema(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenesMedicasDefinitiva= this.obtenerTarifasServiciosArticulos(listaOrdenesMedicas); 
		
		//MT6601 - hermorhu
		//Consulta que trae las solicitudes de cirugia anuladas con solo el numero de peticion 
		//para que no presente conflicto con MT6578 
		//CONSULTAR SOLICITUDES CIRUGIA ANUALADAS
		ArrayList<DtoResultadoConsultaProcesosCierre> listaSolicitudesCirugiaAnuladas=solicitudMundo.consultarSolicitudesCirugiasAnuladas(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaSolicitudesCirugiaDefinitiva=this.obtenerTarifasServiciosArticulos(listaSolicitudesCirugiaAnuladas);
		
		//MT6601 - hermorhu
		//Consulta que trae las peticiones de articulos anulados (cuando se anula la solicitud de cirugia) con solo el numero de peticion
		//para que no presente conflicto con MT6578 
		//CONSULTAR ANULACION DE PETICIONES DE ARTICULOS CONVERTIDA EN ORDEN
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticionArticulosAnulados = peticionMundo.consultarPeticionesArticulosAnulados(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticionesArticulosAnuladosDefinitiva= this.obtenerTarifasServiciosArticulos(listaPeticionArticulosAnulados);
		
		ArrayList<DtoResultadoConsultaProcesosCierre> listaOrdenesAmbMedicas=new ArrayList<DtoResultadoConsultaProcesosCierre>();
		listaOrdenesAmbMedicas.addAll(listaOrdenesAmbulatoriasDefinitiva);
		listaOrdenesAmbMedicas.addAll(listaOrdenesMedicasAnuladasDefinitiva);
		listaOrdenesAmbMedicas.addAll(listaOrdenesMedicasDefinitiva);
		listaOrdenesAmbMedicas.addAll(listaSolicitudesCirugiaDefinitiva);
		listaOrdenesAmbMedicas.addAll(listaPeticionesArticulosDefinitiva);
		listaOrdenesAmbMedicas.addAll(listaPeticionesServiciosDefinitiva);
		listaOrdenesAmbMedicas.addAll(listaPeticionesArticulosAnuladosDefinitiva);
		
		//REALIZAR PROCESO ORDENES
		this.generarTotalesInconsistenciasProcesoOrdenes(listaOrdenesAmbMedicas, dtoProcesoPresupuestoCapitado);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return null;
	}
	
	/**
	 * Metodo que obtiene las tarifas de los servicios SOAT o ISS y de articulos para conocer su fecha de vigencia actual
	 * @param lista lista Ordenes Solicitudes o Peticiones
	 * @return lista con los registros definitivos
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> obtenerTarifasServiciosArticulos(ArrayList<DtoResultadoConsultaProcesosCierre> lista) 
	{
		IServiciosMundo servicioMundo=FacturacionFabricaMundo.crearServiciosMundo();
		IArticulosMundo articuloMundo=InventarioMundoFabrica.crearArticulosMundo();
		ArrayList<DtoResultadoConsultaProcesosCierre> listaDefinitiva= new ArrayList<DtoResultadoConsultaProcesosCierre>();
		
		for(DtoResultadoConsultaProcesosCierre dtoResultado:lista)
		{
			if(dtoResultado.getCodigoServicio()!=null){
				//MT 6532-6438
				DtoServicios dtoServ=servicioMundo.obtenerTarifaIssVigente(this.esquemaTarServiciosValorizarOrden, dtoResultado.getCodigoServicio());//obtenerTarifaISSVigenteServicios(dtoResultado.getCodigoServicio(), this.esquemaTarServiciosValorizarOrden);
								
				if(dtoServ!=null)
				{
					dtoResultado.setTarifa(dtoServ.getValorTarifa());
					//MT 6532-6438
					dtoResultado.setCodigoTipoLiquidacion(dtoServ.getCodigoTipoLiquidacion());
				}
				else
				{ 	//MT 6532-6438
					 dtoServ=servicioMundo.obtenerTarifaSoatVigente( this.esquemaTarServiciosValorizarOrden, dtoResultado.getCodigoServicio());
					//dtoServ=servicioMundo.obtenerTarifaSOATVigenteServicios(dtoResultado.getCodigoServicio(), this.esquemaTarServiciosValorizarOrden);
					if(dtoServ!=null){
						dtoResultado.setTarifa(dtoServ.getValorTarifa());
						//MT 6532-6438
						dtoResultado.setCodigoTipoLiquidacion(dtoServ.getCodigoTipoLiquidacion());
					}
					else
					{
						dtoResultado.setTarifa(ConstantesBD.codigoNuncaValidoDouble);
						dtoResultado.setCodigoTipoLiquidacion(ConstantesBD.codigoNuncaValido);
					}
				}
				//guardo cantidad por servicio = 1
				dtoResultado.setCantidadServiciosSolicitudes(new BigDecimal(1));
				listaDefinitiva.add(dtoResultado);
			}else
				if(dtoResultado.getCodigoArticulo()!=null)
				{
					DtoArticulos articulo=articuloMundo.obtenerTarifaVigenteArticulos(dtoResultado.getCodigoArticulo(), this.esquemaTarMedicamentosValorizarOrden);
					if(articulo!=null)
					{
						dtoResultado.setTarifa(articulo.getValorTarifa());
						//MT6749 se elimina validaciones ya que se realizan mas adelante
						//if(dtoResultado.getEsquemaTarifarioArticulo()!=null&&dtoResultado.getFechaVigenciaTarifaArticulo()!=null)
					//	{
					//		if(dtoResultado.getEsquemaTarifarioArticulo().intValue()==this.esquemaTarMedicamentosValorizarOrden)
					//		{
					//			if(!dtoResultado.getFechaVigenciaTarifaArticulo().equals(articulo.getFechaVigenciaTarifa()))
					//			{
					//				dtoResultado.setEsquemaTarifarioArticulo(ConstantesBD.codigoNuncaValido);
					//			}
								
								
						//	}
					
						//}
				
					}
					else
								{
									dtoResultado.setEsquemaTarifarioArticulo(ConstantesBD.codigoNuncaValido);
								}
					
					listaDefinitiva.add(dtoResultado);
				}
		}
		return listaDefinitiva;
	}
	
	/**
	 * Devuelve la lista de servicios con totales
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaPorServicio() {
		return totalListaPorServicio;
	}
	
	/**
	 * Devuelve la lista de articulos con totales
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaPorArticulo() {
		return totalListaPorArticulo;
	}
	
	/**
	 * Devuelve la lista de inconsistencias encontradas en el proceso
	 * @return ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>
	 */
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> obtenerListaInconsistenciasProcesoOrdenes() {
		return listaInconsistenciasProcesoOrdenes;
	}

	/**
	 * Devuelve el codigo definido en el parámetro esquema tarifario de servicios 
	 * para valorizar las ordenes
	 * @return esquemaTarServiciosValorizarOrden
	 */
	public int getEsquemaTarServiciosValorizarOrden() {
		return esquemaTarServiciosValorizarOrden;
	}

	/**
	 * Devuelve el codigo definido en el parámetro esquema tarifario de medicamentos 
	 * para valorizar las ordenes
	 * @return esquemaTarMedicamentosValorizarOrden
	 */
	public int getEsquemaTarMedicamentosValorizarOrden() {
		return esquemaTarMedicamentosValorizarOrden;
	}

	/**
	 * Devuelve la descripcion definido en el parámetro esquema tarifario de servicios 
	 * para valorizar las ordenes
	 * @return esquemaTarMedicamentosValorizarOrden
	 */
	public String getDescripcionEsquemaTarServiciosValorizarOrden() {
		return descripcionEsquemaTarServiciosValorizarOrden;
	}

	/**
	 * Devuelve la descripcion definido en el parámetro esquema tarifario de medicamentos 
	 * para valorizar las ordenes
	 * @return esquemaTarMedicamentosValorizarOrden
	 */
	public String getDescripcionEsquemaTarMedicamentosValorizarOrden() {
		return descripcionEsquemaTarMedicamentosValorizarOrden;
	}
}
