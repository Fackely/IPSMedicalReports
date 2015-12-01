package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoCargosCuenta;
import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetCargosMundo;

public class ProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo implements IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo
{
	
	/**contiene los codigos de los servicios y de los articulos para no volver a calcular sobre los mismos*/
	private ArrayList<Integer> listaFinalCodigosArticulos;
	private ArrayList<Integer> listaFinalCodigosServicios;
	
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaServicio;
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaArticulo;
	private ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistenciasProcesoCargos;
	
	
	/**
	 * Constructor de la clase
	 */
	public ProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo() 
	{
		this.listaFinalCodigosArticulos = new ArrayList<Integer>();
		this.listaFinalCodigosServicios = new ArrayList<Integer>();
		this.totalListaAgrupadaServicio = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.totalListaAgrupadaArticulo = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.listaInconsistenciasProcesoCargos=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
	}
	
	
	
	/** 
	 * Metodo que se encarga de realizar los calculos de cantidad y valor de los cargos a la cuenta
	 * generadas.
	 * 
	 * @param dtoProcesoPresupuestoCapitado
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoCargosCuenta(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado)
	{
		//UtilidadTransaccion.getTransaccion().begin();
	 	
		Log4JManager.info("Fecha inicio	: "+dtoProcesoPresupuestoCapitado.getFechaInicio());
		Log4JManager.info("Fecha fin		: "+dtoProcesoPresupuestoCapitado.getFechaFin());
		Log4JManager.info("Convenio		: "+dtoProcesoPresupuestoCapitado.getConvenio());
		Log4JManager.info("Contrato		: "+dtoProcesoPresupuestoCapitado.getContrato());
		
				
		ArrayList<DtoConsultaProcesoCargosCuenta> listaResultadoOrdenesCirugia			=new ArrayList<DtoConsultaProcesoCargosCuenta>();
		ArrayList<DtoConsultaProcesoCargosCuenta> listaResultadoOrdenesMedicas			=new ArrayList<DtoConsultaProcesoCargosCuenta>();
		
		IDetCargosMundo detCargosMundo=TesoreriaFabricaMundo.crearDetCargosMundo();
		//CONSULTA DE LAS ORDENES MEDICAS Y DE LAS ORDENES AMBULATORIAS
		listaResultadoOrdenesMedicas		=detCargosMundo.consultarSolicitudesCargosCuenta(dtoProcesoPresupuestoCapitado);
		listaResultadoOrdenesCirugia		=detCargosMundo.consultarSolicitudesCirugiaCargosCuenta(dtoProcesoPresupuestoCapitado);
		//FIXME Se elimina consulta de ordenes ambulatorias ya que al generarse una orden se asocia a una solicitud y ya se estan consultado
		
		//Se adicionan las consultas a la lista final 
		ArrayList<DtoConsultaProcesoCargosCuenta> listaResultadoConsultaProcesosCierre	=new ArrayList<DtoConsultaProcesoCargosCuenta>();
			listaResultadoConsultaProcesosCierre.addAll(listaResultadoOrdenesMedicas);
			listaResultadoConsultaProcesosCierre.addAll(listaResultadoOrdenesCirugia);
		
		ArrayList<DtoTotalProcesoPresupuestoCapitado>listaTotalProcesoPresupuestoCapitado=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		listaTotalProcesoPresupuestoCapitado=this.procesarTotales(listaResultadoConsultaProcesosCierre, dtoProcesoPresupuestoCapitado);
			
		//UtilidadTransaccion.getTransaccion().commit();
		
		//separarListasTotales(listaTotalProcesoPresupuestoCapitado);
		this.organizarListas(listaTotalProcesoPresupuestoCapitado);
		
		return listaTotalProcesoPresupuestoCapitado;
	}
	
	
	/**
	 * Metodo que se encarga de recorrer la consulta para calcular los totales de las inconsistencias, cantidades y valores 
	 * por cada cargo a la cuenta en la fecha -> convenio -> contrato
	 * 
	 * @param dtoProcesoPresupuestoCapitado
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> procesarTotales(ArrayList<DtoConsultaProcesoCargosCuenta> listaResultadoOrdenesAmbMedicas,DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado)
	{

		//Dto para almacenar los servicios por 	(total-nivelAtencion-grupo-grupoNivel)
		//Dto para almacenar los articulos por  (total-nivelAtencion-clase-claseNivel)
		DtoTotalProcesoPresupuestoCapitado 	dtoTotal	=new DtoTotalProcesoPresupuestoCapitado();
		
		//listas finales de las autorizaciones
		ArrayList<DtoTotalProcesoPresupuestoCapitado> 	listaValoreTotales	=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		ArrayList<DtoTotalProcesoPresupuestoCapitado>	listaFinalDiaTotal	=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		
		ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>	listaInconsistencias		=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		ArrayList<Integer>contrato = new ArrayList<Integer>();
		listaFinalCodigosArticulos = new ArrayList<Integer>();
		listaFinalCodigosServicios = new ArrayList<Integer>();
		
		Date fechaBase = dtoProcesoPresupuestoCapitado.getFechaInicio();
		
		// Los totales se hacen por [fecha(día) - convenio - contrato] (en ese orden)
		while(fechaBase.before(dtoProcesoPresupuestoCapitado.getFechaFin()) || fechaBase.equals(dtoProcesoPresupuestoCapitado.getFechaFin()))
		{
			contrato =new ArrayList<Integer>();
			
			for (DtoConsultaProcesoCargosCuenta listaResultado : listaResultadoOrdenesAmbMedicas) 
			{				
				if(listaResultado.getFecha().equals(fechaBase))
				{					
					listaValoreTotales=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
					listaInconsistencias=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
					listaFinalCodigosArticulos = new ArrayList<Integer>();
					listaFinalCodigosServicios = new ArrayList<Integer>();
					
					if(!contrato.contains(listaResultado.getContrato()))
					{
						for (DtoConsultaProcesoCargosCuenta listaContratos : listaResultadoOrdenesAmbMedicas) 
						{	
							if(listaContratos.getFecha().equals(fechaBase))
							{	
								if(!contrato.contains(listaContratos.getContrato()))
								{
									listaFinalCodigosArticulos = new ArrayList<Integer>();
									listaFinalCodigosServicios = new ArrayList<Integer>();
								}
								
								if(!listaFinalCodigosArticulos.contains(listaContratos.getCodigoArticulo()) && !listaFinalCodigosServicios.contains(listaContratos.getCodigoServicio()))
								{
									ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>inconsistencias=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
									inconsistencias=validarInconsistenciaRegistro(listaContratos);
									
									if(!Utilidades.isEmpty(inconsistencias))
									{
										listaInconsistencias.addAll(inconsistencias);
									}else
									{
										//1. Totales de Autorizaciones de Servicios Y Articulos
										dtoTotal=new DtoTotalProcesoPresupuestoCapitado();
										dtoTotal = calcularTotales(listaContratos, listaResultadoOrdenesAmbMedicas);
										listaValoreTotales.add(dtoTotal);
										
										//2. Totales de Autorizaciones de Servicios.Y Articulos por Nivel de Atención.
										/*dtoTotal=new DtoTotalProcesoPresupuestoCapitado();
										dtoTotal = calcularTotalesNivelAtencion(listaContratos, listaResultadoOrdenesAmbMedicas);
										listaValoreTotales.add(dtoTotal);
										
										//3. Totales de Autorizaciones de Servicios por Grupo de Servicios.Y Articulos por Clase de Inventario
										dtoTotal=new DtoTotalProcesoPresupuestoCapitado();
										dtoTotal= calcularTotalesGrupoYClase(listaContratos, listaResultadoOrdenesAmbMedicas);
										listaValoreTotales.add(dtoTotal);
										
										//4. Totales de Autorizaciones de Servicios por Nivel de Atención y Grupo de Servicios.Y Articulos por Nivel de Atención y Clase de Inventarios.
										dtoTotal=new DtoTotalProcesoPresupuestoCapitado();
										dtoTotal= calcularTotalesNivelGrupoYNivelClase(listaContratos, listaResultadoOrdenesAmbMedicas);
										listaValoreTotales.add(dtoTotal);*/
									}
								}
								contrato.add(listaContratos.getContrato());//-->Se adiciona el contrato
							}
						}
						//Se diciona para la Fecha -ConvenioContrato -Inconsistencias -Totales
						dtoTotal.setFecha(listaResultado.getFecha());
						dtoTotal.setConvenio(listaResultado.getConvenio());
						dtoTotal.setContrato(listaResultado.getContrato());
						dtoTotal.setListaTotales(listaValoreTotales);
						dtoTotal.setListaInconsistencias(listaInconsistencias);
							listaFinalDiaTotal.add(dtoTotal);		
					}
				}				
			} 
			fechaBase = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(fechaBase), 1, false));
		}
								
		return listaFinalDiaTotal;	
	}
	
	
	/**
	 * Metodo que se encarga de validar cada registro en busca de inconsistencias generadas de las autorizaciones. 
	 * 
	 * @param listaResultado
	 * @param tipoInconsistencia
	 * @param nombreInconsistencia
	 * @return
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> validarInconsistenciaRegistro(DtoConsultaProcesoCargosCuenta listaContratos)
	{
		ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistencias=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		// hermorhu - MT6579
		// Validacion de Tarifa para Servicio/Articulos no aplica	
		
//		if(listaContratos.getCodigoServicio()!=null )
//		{//SERVICIOS
//			
//			//Se valida la tarifa del servicio
//			if(listaContratos.getTarifa()==null)
//			{
//				listaInconsistencias.add(agregarInconsistencia(listaContratos, ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa,"No está definida la tarifa"));												
//			}
//			
//		}else
		if(listaContratos.getCodigoArticulo()!=null )
		{//ARTICULOS
			
//			//Se valida la tarifa del articulo
//			if(listaContratos.getTarifa()==null)
//			{
//				listaInconsistencias.add(agregarInconsistencia(listaContratos, ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa,"No está definida la tarifa"));
//			}
			//Se valida el nivel de atencion del articulo
			if(listaContratos.getNivelAtencionArticulo()==null)
			{
				listaInconsistencias.add(agregarInconsistencia(listaContratos, ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidoNivelAtencion,"No está definido el nivel de atención para los medicamentos e insumos"));
			}
		}
				
		return 	listaInconsistencias;	
	}
	
	
	/**
	 * Metodo que se encarga de agregar inconsistencias generadas duarante la validacion
	 * de las autorizaciones. 
	 * 
	 * @param listaResultado
	 * @param tipoInconsistencia
	 * @param nombreInconsistencia
	 * @return
	 */
	public DtoInconsistenciasProcesoPresupuestoCapitado agregarInconsistencia(DtoConsultaProcesoCargosCuenta listaResultado,
			String tipoInconsistencia,String nombreInconsistencia)
	{		
		//se agreagan las inconsistencias de los servicios y articulos
		DtoInconsistenciasProcesoPresupuestoCapitado dtoInconsistenciasProcesoPresupuestoCapitado=new DtoInconsistenciasProcesoPresupuestoCapitado();

		String codigo="";
		String nombre="";
		if(listaResultado.getCodigoArticulo()!=null)
		{	
			codigo=listaResultado.getCodigoArticulo().toString();
			nombre=listaResultado.getNombreArticulo();
		}
		else
		{	codigo=listaResultado.getCodigoServicio().toString();
			nombre=listaResultado.getNombreServicio();
		}
		
		dtoInconsistenciasProcesoPresupuestoCapitado.setServicioMedicamento(codigo+" - "+nombre);
		dtoInconsistenciasProcesoPresupuestoCapitado.setTipoInconsistencia(tipoInconsistencia);
		dtoInconsistenciasProcesoPresupuestoCapitado.setDescripcion(nombreInconsistencia+" '"+nombre+"'");	
		dtoInconsistenciasProcesoPresupuestoCapitado.setCodigoContrato(listaResultado.getContrato());
		dtoInconsistenciasProcesoPresupuestoCapitado.setCodigoConvenio(listaResultado.getConvenio());
		dtoInconsistenciasProcesoPresupuestoCapitado.setFecha(listaResultado.getFecha());
		
		return dtoInconsistenciasProcesoPresupuestoCapitado;
	}
	
	
	/**
	 * Metodo que se encarga de calcular los totales de los cargos a la cuenta de los SERVICIOS Y ARTICULOS
	 *  
	 * 1. Totales de Cargos de Servicios.
	 * 1. Totales de Cargos de Medicamentos e Insumos.
	 * 
	 * @param listaResultado
	 * @return
	 */	
	public DtoTotalProcesoPresupuestoCapitado calcularTotales(DtoConsultaProcesoCargosCuenta listaContratos,
			ArrayList<DtoConsultaProcesoCargosCuenta>listaResultadoOrdenesAmbMedicas)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
		ArrayList<Integer> listaCodigosArticulos = new ArrayList<Integer>();
		ArrayList<Integer> listaCodigosServicios = new ArrayList<Integer>();
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoCargosCuenta listaTipo : listaResultadoOrdenesAmbMedicas)
		{
			if(listaContratos.getFecha().toString().equals(listaTipo.getFecha().toString())&&
					listaContratos.getConvenio().intValue()==listaTipo.getConvenio().intValue()&&
					listaContratos.getContrato().intValue()==listaTipo.getContrato().intValue())
			{	
				
				if(listaContratos.getCodigoServicio()!=null && listaTipo.getCodigoServicio()!=null)
				{//SERVICIOS
					if(listaContratos.getCodigoServicio().intValue()==listaTipo.getCodigoServicio().intValue())				
					{												
						dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalServicio);
						if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
						{
							cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad();
							dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

							/*valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
							dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);*/
							
							/*************************/
							valorTotal=listaTipo.getCantidad()*listaTipo.getTarifa().doubleValue();							
							dtoTotalProcesoPresupuestoCapitado.setValor(dtoTotalProcesoPresupuestoCapitado.getValor()+valorTotal);
							/*************************/
							
							dtoTotalProcesoPresupuestoCapitado.setCodigoServicio(listaTipo.getCodigoServicio());
							dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
							dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
							dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
						}
					}
					if(!listaCodigosServicios.contains(listaContratos.getCodigoServicio()))
					{
						listaCodigosServicios.add(listaContratos.getCodigoServicio());
					}
					
				}else if(listaContratos.getCodigoArticulo()!=null && listaTipo.getCodigoArticulo()!=null )
				{//ARTICULOS
					
					if(listaContratos.getCodigoArticulo().intValue()==listaTipo.getCodigoArticulo().intValue())				
					{
						dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalArticulo);
						if(listaTipo.getCantidad()==null)
							listaTipo.setCantidad(0);
						if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
						{
							cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad().intValue();
							dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

							/*valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
							dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);*/
							
							/*************************/
							valorTotal=listaTipo.getCantidad()*listaTipo.getTarifa().doubleValue();							
							dtoTotalProcesoPresupuestoCapitado.setValor(dtoTotalProcesoPresupuestoCapitado.getValor()+valorTotal);
							/*************************/
							
							dtoTotalProcesoPresupuestoCapitado.setCodigoArticulo(listaTipo.getCodigoArticulo());
							//dtoTotalProcesoPresupuestoCapitado.setCodigoServicio(listaTipo.getCodigoServicio());
							dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
							dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
							dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
						}
					}
					if(!listaCodigosArticulos.contains(listaContratos.getCodigoArticulo()))
					{
						listaCodigosArticulos.add(listaContratos.getCodigoArticulo());
					}
				}
			}
		}
		listaFinalCodigosServicios.addAll(listaCodigosServicios);
		listaFinalCodigosArticulos.addAll(listaCodigosArticulos);
				
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
	
	
	/**
	 * Metodo que se encarga de calcular los totales de los Cargos de los SERVICIOS Y ARTICULOS 
	 * 
	 * 2. Totales de Cargos de Servicios por Nivel de Atención.
	 * 2. Totales de Cargos de Medicamentos e Insumos por Nivel de Atención.
	 * 
	 * @param listaResultado
	 * @param dtoTotalProcesoPresupuestoCapitado
	 * @return
	 */
	public DtoTotalProcesoPresupuestoCapitado calcularTotalesNivelAtencion(DtoConsultaProcesoCargosCuenta listaContratos,
			ArrayList<DtoConsultaProcesoCargosCuenta> listaResultadoConsultaProcesosCierre)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoCargosCuenta listaTipo : listaResultadoConsultaProcesosCierre)
		{
			if(listaContratos.getFecha().toString().equals(listaTipo.getFecha().toString())&&
					listaContratos.getConvenio().intValue()==listaTipo.getConvenio().intValue()&&
					listaContratos.getContrato().intValue()==listaTipo.getContrato().intValue())
			{	
				if(listaContratos.getCodigoServicio()!=null && listaTipo.getCodigoServicio()!=null)
				{//SERVICIOS
					if(listaContratos.getCodigoServicio().intValue()==listaTipo.getCodigoServicio().intValue())				
					{
						if(listaContratos.getNivelAtencionServicio()!=null)
						{
							if(listaContratos.getNivelAtencionServicio().equals(listaTipo.getNivelAtencionServicio()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionServicio);
								if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getNivelAtencionServicio());
							}
						}
					}
				}else if(listaContratos.getCodigoArticulo()!=null && listaTipo.getCodigoArticulo()!=null )
				{//ARTICULOS										
					if(listaContratos.getCodigoArticulo().intValue()==listaTipo.getCodigoArticulo().intValue())				
					{		
						if(listaContratos.getNivelAtencionArticulo()!=null)
						{
							if(listaContratos.getNivelAtencionArticulo().equals(listaTipo.getNivelAtencionArticulo()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionArticulo);
								if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getNivelAtencionArticulo());
							}
						}
					}
				}
			}
		}				
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
	
	
	/**
	 * Metodo que se encarga de calcular los totales de los Cargos de los SERVICIOS Y ARTICULOS
	 * 
	 * 3. Totales de Cargos de Servicios por Grupo de Servicios.
	 * 3. Totales de Cargos de Medicamentos e Insumos por Clase de Inventarios.
	 *  
	 * @param listaResultado
	 * @param dtoTotalProcesoPresupuestoCapitado
	 * @return
	 */
	public DtoTotalProcesoPresupuestoCapitado calcularTotalesGrupoYClase(DtoConsultaProcesoCargosCuenta listaContratos,
			ArrayList<DtoConsultaProcesoCargosCuenta> listaResultadoConsultaProcesosCierre
			)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoCargosCuenta listaTipo : listaResultadoConsultaProcesosCierre)
		{
			if(listaContratos.getFecha().toString().equals(listaTipo.getFecha().toString())&&
					listaContratos.getConvenio().intValue()==listaTipo.getConvenio().intValue()&&
					listaContratos.getContrato().intValue()==listaTipo.getContrato().intValue())
			{	
				if(listaContratos.getCodigoServicio()!=null && listaTipo.getCodigoServicio()!=null)
				{//SERVICIOS
					if(listaContratos.getCodigoServicio().intValue()==listaTipo.getCodigoServicio().intValue())				
					{
						if(listaContratos.getCodigoGrupoServicio()!=null)
						{
							if(listaContratos.getCodigoGrupoServicio().equals(listaTipo.getCodigoGrupoServicio()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalGrupoServicio);
								if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setGrupoServicio(listaTipo.getCodigoGrupoServicio());
							}
						}
					}
				}else if(listaContratos.getCodigoArticulo()!=null && listaTipo.getCodigoArticulo()!=null )
				{//ARTICULOS	
										
					if(listaContratos.getCodigoArticulo().intValue()==listaTipo.getCodigoArticulo().intValue())				
					{		
						if(listaContratos.getClaseInventario()!=null)
						{											
							if(listaContratos.getClaseInventario().equals(listaTipo.getClaseInventario()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalClaseInventario);
								if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setClaseInventario(listaTipo.getClaseInventario());
							}
						}
					}
				}
			}
		}				
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
	
	/**
	 * Metodo que se encarga de calcular los totales de los Cargos de los SERVICIOS Y ARTICULOS
	 * 
	 * 4. Totales de Cargos de Servicios por Nivel de Atención y Grupo de Servicios.
	 * 4. Totales de Cargos de Medicamentos e Insumos por Nivel de Atención y Clase de Inventarios. 
	 *  
	 * @param listaResultado
	 * @param dtoTotalProcesoPresupuestoCapitado
	 * @return
	 */
	public DtoTotalProcesoPresupuestoCapitado calcularTotalesNivelGrupoYNivelClase(DtoConsultaProcesoCargosCuenta listaContratos,
			ArrayList<DtoConsultaProcesoCargosCuenta> listaResultadoConsultaProcesosCierre)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoCargosCuenta listaTipo : listaResultadoConsultaProcesosCierre)
		{
			if(listaContratos.getFecha().toString().equals(listaTipo.getFecha().toString())&&
					listaContratos.getConvenio().intValue()==listaTipo.getConvenio().intValue()&&
					listaContratos.getContrato().intValue()==listaTipo.getContrato().intValue())
			{	
				if(listaContratos.getCodigoServicio()!=null && listaTipo.getCodigoServicio()!=null)
				{//SERVICIOS
					if(listaContratos.getCodigoServicio().intValue()==listaTipo.getCodigoServicio().intValue())				
					{
						if(listaContratos.getNivelAtencionServicio()!=null && listaContratos.getCodigoGrupoServicio()!=null  )
						{
							if(listaContratos.getNivelAtencionServicio().equals(listaTipo.getNivelAtencionServicio()) && 
									listaContratos.getCodigoGrupoServicio().equals(listaTipo.getCodigoGrupoServicio()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionGrupoServicio);
								if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getNivelAtencionServicio());
								dtoTotalProcesoPresupuestoCapitado.setGrupoServicio(listaTipo.getCodigoGrupoServicio());
							}
						}
					}
				}else if(listaContratos.getCodigoArticulo()!=null && listaTipo.getCodigoArticulo()!=null )
				{//ARTICULOS										
					if(listaContratos.getCodigoArticulo().intValue()==listaTipo.getCodigoArticulo().intValue())				
					{		
						if(listaContratos.getNivelAtencionArticulo()!=null && listaContratos.getClaseInventario()!=null)
						{
							if(listaContratos.getNivelAtencionArticulo().equals(listaTipo.getNivelAtencionArticulo()) && listaContratos.getClaseInventario().equals(listaTipo.getClaseInventario()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionClaseInventario);
								if(listaTipo.getCantidad()!=null && listaTipo.getTarifa()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidad();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifa().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getNivelAtencionArticulo());
								dtoTotalProcesoPresupuestoCapitado.setClaseInventario(listaTipo.getClaseInventario());
							}
						}
					}
				}
			}
		}				
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
	
	
	
	/**
	 * Separa de forma individual las listas de las agrupaciones realizadas.
	 * 
	 * @autor Cristhian Murillo
	 */
	/*private void separarListasTotales(ArrayList<DtoTotalProcesoPresupuestoCapitado> listaFinalTotal)
	{
		for (DtoTotalProcesoPresupuestoCapitado finalTotal : listaFinalTotal) 
		{
			if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalServicio)){
				this.totalListaAgrupadaServicio.add(finalTotal);
			}else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalArticulo)){
				this.totalListaAgrupadaArticulo.add(finalTotal);
			}
		}
	}*/
	
	
	/**
	 * @return valor de totalListaAgrupadaServicio
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaServicio() {
		return totalListaAgrupadaServicio;
	}

	/**
	 * @return valor de totalListaAgrupadaArticulo
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaArticulo() {
		return totalListaAgrupadaArticulo;
	}

	
	private void organizarListas(ArrayList<DtoTotalProcesoPresupuestoCapitado> listaProcesoCargos)
	{
		this.totalListaAgrupadaServicio = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.totalListaAgrupadaArticulo = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.listaInconsistenciasProcesoCargos = new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		
		for(DtoTotalProcesoPresupuestoCapitado dtoTotalProceso:listaProcesoCargos)
		{
			for(DtoInconsistenciasProcesoPresupuestoCapitado dtoInconsistencia: dtoTotalProceso.getListaInconsistencias())
			{
				/*dtoInconsistencia.setFecha(dtoTotalProceso.getFecha());
				dtoInconsistencia.setCodigoConvenio(dtoTotalProceso.getConvenio());
				dtoInconsistencia.setCodigoContrato(dtoTotalProceso.getContrato());*/
				
				this.listaInconsistenciasProcesoCargos.add(dtoInconsistencia);
			}
			
			for(DtoTotalProcesoPresupuestoCapitado dtoTotal:dtoTotalProceso.getListaTotales())
			{
				if(dtoTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalServicio))
					this.totalListaAgrupadaServicio.add(dtoTotal);
				if(dtoTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalArticulo))
					this.totalListaAgrupadaArticulo.add(dtoTotal);
			}
		}
		
	}
	
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> obtenerListaInconsistenciasProcesoCargos() {
		return listaInconsistenciasProcesoCargos;
	}
	
}
