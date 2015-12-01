package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesCapitacionSubDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoAutorizacionesPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionCapitacionSubMundo;

/**
 * Clase que se encarga de realizar los calculos de cantidad y valor de las autorizaciones
 * generadas a la  Capitación sub-contratada y a entidades subcontratadas.
 * 
 * @author Camilo Gómez
 */
public class ProcesoAutorizacionesPresupuestoCapitacionMundo implements IProcesoAutorizacionesPresupuestoCapitacionMundo 
{

	/**contiene los codigos de los servicios y de los articulos para no volver a calcular sobre los mismos*/
	private ArrayList<Integer> listaFinalCodigos=new ArrayList<Integer>();
	
	
	IAutorizacionesCapitacionSubDAO dao;
	
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaServicio;
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> totalListaAgrupadaArticulo;
	private ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistenciasProcesoAutoriz;
	private IAutorizacionCapitacionSubMundo autorizacionCapitacionSubMundo=ManejoPacienteFabricaMundo.crearAutorizacionCapitacionSubMundo();
		
	/**
	 * Método constructor de la clase
	 * 
	 * @author Camilo Gómez
	 */
	public ProcesoAutorizacionesPresupuestoCapitacionMundo()
	{
		dao=ManejoPacienteDAOFabrica.crearAutorizacionCapitacion();
		
		this.totalListaAgrupadaServicio = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.totalListaAgrupadaServicio = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.listaInconsistenciasProcesoAutoriz=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
	}
	
	
	
	/**
	 * Metodo que se encarga de realizar los calculos de cantidad y valor de las autorizaciones
	 * generadas a la  Capitación sub-contratada.
	 * 
	 * @param dtoProcesoPresupuestoCapitado
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoAutorizacion(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado) 
	{				
		//Envio de parametros para consultar por cada dia de rango->convenio-contrato.
		if(dtoProcesoPresupuestoCapitado.getConvenio()!=null){
			dtoProcesoPresupuestoCapitado.setConvenio(dtoProcesoPresupuestoCapitado.getConvenio());
		}
		if(dtoProcesoPresupuestoCapitado.getContrato()!=null){
			dtoProcesoPresupuestoCapitado.setContrato(dtoProcesoPresupuestoCapitado.getContrato());
		}
		dtoProcesoPresupuestoCapitado.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
		
		//Se consulta las autorizaciones de capitacion Sub que se generaron en el rango de fechas seleccionado para el cierre
		ArrayList<DtoConsultaProcesoAutorizacion> listaResultadoConsultaProcesosCierre=new ArrayList<DtoConsultaProcesoAutorizacion>();
		ArrayList<DtoConsultaProcesoAutorizacion> listaAcumuladoArticulos=new ArrayList<DtoConsultaProcesoAutorizacion>();
		ArrayList<DtoConsultaProcesoAutorizacion> listaAcumuladoServicios=new ArrayList<DtoConsultaProcesoAutorizacion>();
		ArrayList<DtoConsultaProcesoAutorizacion> listaInconsistencias=new ArrayList<DtoConsultaProcesoAutorizacion>();
		
		listaAcumuladoServicios=autorizacionCapitacionSubMundo.obtenerAutorizacionesCapitacionServicios(dtoProcesoPresupuestoCapitado);
		listaAcumuladoArticulos=autorizacionCapitacionSubMundo.obtenerAutorizacionesCapitacionArticulos(dtoProcesoPresupuestoCapitado);
		
		listaResultadoConsultaProcesosCierre.addAll(listaAcumuladoServicios);
		listaResultadoConsultaProcesosCierre.addAll(listaAcumuladoArticulos);
		
		ArrayList<DtoTotalProcesoPresupuestoCapitado>listaTotalProcesoPresupuestoCapitado=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		listaTotalProcesoPresupuestoCapitado=this.procesarTotalesFinal(listaResultadoConsultaProcesosCierre,dtoProcesoPresupuestoCapitado.getInstitucion());
		
		listaInconsistencias=autorizacionCapitacionSubMundo.obtenerAutorizacionesCapitaServiArtiInconsistentes(dtoProcesoPresupuestoCapitado);
		ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaFinalinconsistencias =new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		listaFinalinconsistencias=this.procesarInconsistencias(listaInconsistencias);
		
		this.organizarListas(listaTotalProcesoPresupuestoCapitado,listaFinalinconsistencias);
		
		return listaTotalProcesoPresupuestoCapitado;
	}
	
	
	/**
	 * Metodo que se encarga de recorrer la consulta para calcular los totales de las inconsistencias, cantidades y valores 
	 * por cada autorizacion de articulo y servicio en la fecha -> convenio -> contrato -> servicio/articulo
	 * 
	 * @param listaResultadoConsultaProcesosCierre
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> procesarTotales(ArrayList<DtoConsultaProcesoAutorizacion> listaResultadoConsultaProcesosCierre,DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado)
	{
		//Dto para almacenar los servicios por 	(total-nivelAtencion-grupo-grupoNivel)
		//Dto para almacenar los articulos por  (total-nivelAtencion-clase-claseNivel)
		DtoTotalProcesoPresupuestoCapitado 	dtoTotal	=new DtoTotalProcesoPresupuestoCapitado();
		
		//listas finales de las autorizaciones
		ArrayList<DtoTotalProcesoPresupuestoCapitado> 	listaValoreTotales	=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		ArrayList<DtoTotalProcesoPresupuestoCapitado>	listaFinalDiaTotal	=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		
		ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>	listaInconsistencias		=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		ArrayList<Integer>contrato=new ArrayList<Integer>();
		listaFinalCodigos=new ArrayList<Integer>();
		
		Date fechaBase = dtoProcesoPresupuestoCapitado.getFechaInicio();
		
		// Los totales se hacen por [fecha(día) - convenio - contrato] (en ese orden)
		while(fechaBase.before(dtoProcesoPresupuestoCapitado.getFechaFin()) || fechaBase.equals(dtoProcesoPresupuestoCapitado.getFechaFin()))
		{
			contrato =new ArrayList<Integer>();
			
			for (DtoConsultaProcesoAutorizacion listaResultado : listaResultadoConsultaProcesosCierre) 
			{				
				if(listaResultado.getFecha().equals(fechaBase))
				{					
					listaValoreTotales=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
					listaInconsistencias=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
					listaFinalCodigos=new ArrayList<Integer>();
					
					if(!contrato.contains(listaResultado.getContrato()))
					{
						for (DtoConsultaProcesoAutorizacion listaContratos : listaResultadoConsultaProcesosCierre) 
						{	
							if(listaContratos.getFecha().equals(fechaBase))
							{
								if(!contrato.contains(listaContratos.getContrato()))
									listaFinalCodigos=new ArrayList<Integer>();
								
								
								if(!listaFinalCodigos.contains(listaContratos.getCodigoArticulo()) &&!listaFinalCodigos.contains(listaContratos.getCodigoServicio()))
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
										dtoTotal = calcularTotales(listaContratos, listaResultadoConsultaProcesosCierre);
										listaValoreTotales.add(dtoTotal);
										
										/*
										//2. Totales de Autorizaciones de Servicios Y Articulos por Nivel de Atención.
										dtoTotal=new DtoTotalProcesoPresupuestoCapitado();
										dtoTotal = calcularTotalesNivelAtencion(listaContratos, listaResultadoConsultaProcesosCierre);
										listaValoreTotales.add(dtoTotal);
										
										//3. Totales de Autorizaciones de Servicios por Grupo de Servicios.Y Articulos por Clase de Inventario
										dtoTotal=new DtoTotalProcesoPresupuestoCapitado();
										dtoTotal= calcularTotalesGrupoYClase(listaContratos, listaResultadoConsultaProcesosCierre);
										listaValoreTotales.add(dtoTotal);
										
										//4. Totales de Autorizaciones de Servicios por Nivel de Atención y Grupo de Servicios.Y Articulos por Nivel de Atención y Clase de Inventarios.
										dtoTotal=new DtoTotalProcesoPresupuestoCapitado();
										dtoTotal= calcularTotalesNivelGrupoYNivelClase(listaContratos, listaResultadoConsultaProcesosCierre);
										listaValoreTotales.add(dtoTotal);
										*/
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
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> validarInconsistenciaRegistro(DtoConsultaProcesoAutorizacion listaContratos)
	{
		ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistencias=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		if(listaContratos.getCodigoServicio()!=null )
		{//SERVICIOS
			
			//Se valida la tarifa del servicio
			if(listaContratos.getTarifaServicio()==null)
			{
				listaInconsistencias.add(agregarInconsistencia(listaContratos, ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa,"No está definida la tarifa"));												
			}
			
		}else if(listaContratos.getCodigoArticulo()!=null )
		{//ARTICULOS
			
			//Se valida la tarifa del articulo
			if(listaContratos.getTarifaArticulo()==null)
			{
				listaInconsistencias.add(agregarInconsistencia(listaContratos, ConstantesIntegridadDominio.acronimoTipoInconsistenciaNoDefinidaTarifa,"No está definida la tarifa"));
			}
			//Se valida el nivel de atencion del articulo
			//if(listaContratos.getNivelAtencionArticulo()==null)
			if(listaContratos.getCodNivelAtencionArticulo()==null)
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
	 * @return DtoInconsistenciasProcesoPresupuestoCapitado
	 * @author Camilo Gomez
	 */
	public DtoInconsistenciasProcesoPresupuestoCapitado agregarInconsistencia(DtoConsultaProcesoAutorizacion listaResultado,
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
		dtoInconsistenciasProcesoPresupuestoCapitado.setFecha(listaResultado.getFecha());
		dtoInconsistenciasProcesoPresupuestoCapitado.setCodigoConvenio(listaResultado.getConvenio());
		dtoInconsistenciasProcesoPresupuestoCapitado.setCodigoContrato(listaResultado.getContrato());
		
		return dtoInconsistenciasProcesoPresupuestoCapitado;
	}
	
	
	/**
	 * Metodo que se encarga de calcular los totales de las autorizaciones de los SERVICIOS Y ARTICULOS
	 *  
	 * 1. Totales de Autorizaciones de Servicios.
	 * 1. Totales de Autorizaciones de Medicamentos e Insumos.
	 * 
	 * @param listaResultado
	 * @param listaResultadoConsultaProcesosCierre
	 * @return DtoTotalProcesoPresupuestoCapitado
	 */	
	public DtoTotalProcesoPresupuestoCapitado calcularTotales(DtoConsultaProcesoAutorizacion listaContratos,
			ArrayList<DtoConsultaProcesoAutorizacion>listaResultadoConsultaProcesosCierre)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
		ArrayList<Integer> listaCodigos=new ArrayList<Integer>();
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoAutorizacion listaTipo : listaResultadoConsultaProcesosCierre)
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
						if(listaTipo.getCantidadServicio()!=null && listaTipo.getTarifaServicio()!=null)
						{
							if(listaTipo.getFechaAnulacion()!=null)
							{
								if(listaTipo.getFechaAnulacion().equals(listaContratos.getFecha()))
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()-listaTipo.getCantidadServicio();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaServicio().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
									
									dtoTotalProcesoPresupuestoCapitado.setCodigoServicio(listaTipo.getCodigoServicio());//-------
									dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
									dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
									dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
								}
								else
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadServicio();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);
	
									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaServicio().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
									
									dtoTotalProcesoPresupuestoCapitado.setCodigoServicio(listaContratos.getCodigoServicio());//-------
									dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
									dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
									dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
								}
							}else
							{
								cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadServicio();
								dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

								valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaServicio().doubleValue();
								dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								
								dtoTotalProcesoPresupuestoCapitado.setCodigoServicio(listaTipo.getCodigoServicio());//-------								
								dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
								dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
								dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
							}
							

							
						}
					}
					if(!listaCodigos.contains(listaContratos.getCodigoServicio()))
						listaCodigos.add(listaContratos.getCodigoServicio());
					
				}else if(listaContratos.getCodigoArticulo()!=null && listaTipo.getCodigoArticulo()!=null )
				{//ARTICULOS
					
					if(listaContratos.getCodigoArticulo().intValue()==listaTipo.getCodigoArticulo().intValue())				
					{
						dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalArticulo);
						if(listaTipo.getCantidadArticulo()!=null && listaTipo.getTarifaArticulo()!=null)
						{
							if(listaTipo.getFechaAnulacion()!=null)
							{
								if(listaTipo.getFechaAnulacion().equals(listaContratos.getFecha()))
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()-listaTipo.getCantidadArticulo();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									//valorTotal=dtoTotalProcesoPresupuestoCapitado.getValor()+dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
									
									dtoTotalProcesoPresupuestoCapitado.setCodigoArticulo(listaContratos.getCodigoArticulo());//-------
									
									dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
									dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
									dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
									
								}else
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadArticulo();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									//valorTotal=dtoTotalProcesoPresupuestoCapitado.getValor()+dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
									
									dtoTotalProcesoPresupuestoCapitado.setCodigoArticulo(listaContratos.getCodigoArticulo());//-------
									
									dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
									dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
									dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
								}
							}
							else
							{
								cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadArticulo();
								dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

								//valorTotal=dtoTotalProcesoPresupuestoCapitado.getValor()+dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
								valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
								dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								
								dtoTotalProcesoPresupuestoCapitado.setCodigoArticulo(listaContratos.getCodigoArticulo());//-------
								
								dtoTotalProcesoPresupuestoCapitado.setContrato(listaTipo.getContrato());
								dtoTotalProcesoPresupuestoCapitado.setConvenio(listaTipo.getConvenio());
								dtoTotalProcesoPresupuestoCapitado.setFecha(listaTipo.getFecha());
							}
							
							
						}
						
					}
					if(!listaCodigos.contains(listaContratos.getCodigoArticulo()))
							listaCodigos.add(listaContratos.getCodigoArticulo());
				}
			}
		}
		listaFinalCodigos.addAll(listaCodigos);
				
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
	
	/**
	 * Metodo que se encarga de calcular los totales de las autorizaciones de los SERVICIOS Y ARTICULOS
	 * 
	 * 2. Totales de Autorizaciones de Servicios por Nivel de Atención.
	 * 2. Totales de Autorizaciones de Medicamentos e Insumos por Nivel de Atención.
	 * 
	 * @param listaResultado
	 * @param dtoTotalProcesoPresupuestoCapitado
	 * @return
	 */
	public DtoTotalProcesoPresupuestoCapitado calcularTotalesNivelAtencion(DtoConsultaProcesoAutorizacion listaContratos,
			ArrayList<DtoConsultaProcesoAutorizacion> listaResultadoConsultaProcesosCierre)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoAutorizacion listaTipo : listaResultadoConsultaProcesosCierre)
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
								if(listaTipo.getCantidadServicio()!=null && listaTipo.getTarifaServicio()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadServicio();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaServicio().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getCodNivelAtencionServicio());
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
								if(listaTipo.getCantidadArticulo()!=null && listaTipo.getTarifaArticulo()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadArticulo();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getCodNivelAtencionArticulo());
							}
						}
					}
				}
			}
		}				
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
	
	
	/**
	 * Metodo que se encarga de calcular los totales de las autorizaciones de los SERVICIOS Y ARTICULOS
	 * 
	 * 3. Totales de Autorizaciones de Servicios por Grupo de Servicios.
	 * 3. Totales de Autorizaciones de Medicamentos e Insumos por Clase de Inventarios.
	 *  
	 * @param listaResultado
	 * @param dtoTotalProcesoPresupuestoCapitado
	 * @return
	 */
	public DtoTotalProcesoPresupuestoCapitado calcularTotalesGrupoYClase(DtoConsultaProcesoAutorizacion listaContratos,
			ArrayList<DtoConsultaProcesoAutorizacion> listaResultadoConsultaProcesosCierre
			)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoAutorizacion listaTipo : listaResultadoConsultaProcesosCierre)
		{
			if(listaContratos.getFecha().toString().equals(listaTipo.getFecha().toString())&&
					listaContratos.getConvenio().intValue()==listaTipo.getConvenio().intValue()&&
					listaContratos.getContrato().intValue()==listaTipo.getContrato().intValue())
			{	
				if(listaContratos.getCodigoServicio()!=null && listaTipo.getCodigoServicio()!=null)
				{//SERVICIOS
					if(listaContratos.getCodigoServicio().intValue()==listaTipo.getCodigoServicio().intValue())				
					{
						if(listaContratos.getGrupoServicio()!=null)
						{
							if(listaContratos.getGrupoServicio().equals(listaTipo.getGrupoServicio()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalGrupoServicio);
								if(listaTipo.getCantidadServicio()!=null && listaTipo.getTarifaServicio()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadServicio();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaServicio().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setGrupoServicio(listaTipo.getCodGrupoServicio());
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
								if(listaTipo.getCantidadArticulo()!=null && listaTipo.getTarifaArticulo()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadArticulo();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setClaseInventario(listaTipo.getCodClaseInventario());
							}
						}
					}
				}
			}
		}				
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
	
	/**
	 * Metodo que se encarga de calcular los totales de las autorizaciones de los SERVICIOS Y ARTICULOS
	 * 
	 * 4. Totales de Autorizaciones de Servicios por Nivel de Atención y Grupo de Servicios.
	 * 4. Totales de Autorizaciones de Medicamentos e Insumos por Nivel de Atención y Clase de Inventarios. 
	 *  
	 * @param listaResultado
	 * @param dtoTotalProcesoPresupuestoCapitado
	 * @return
	 */
	public DtoTotalProcesoPresupuestoCapitado calcularTotalesNivelGrupoYNivelClase(DtoConsultaProcesoAutorizacion listaContratos,
			ArrayList<DtoConsultaProcesoAutorizacion> listaResultadoConsultaProcesosCierre)
	{
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		for(DtoConsultaProcesoAutorizacion listaTipo : listaResultadoConsultaProcesosCierre)
		{
			if(listaContratos.getFecha().toString().equals(listaTipo.getFecha().toString())&&
					listaContratos.getConvenio().intValue()==listaTipo.getConvenio().intValue()&&
					listaContratos.getContrato().intValue()==listaTipo.getContrato().intValue())
			{	
				if(listaContratos.getCodigoServicio()!=null && listaTipo.getCodigoServicio()!=null)
				{//SERVICIOS
					if(listaContratos.getCodigoServicio().intValue()==listaTipo.getCodigoServicio().intValue())				
					{
						if(listaContratos.getNivelAtencionServicio()!=null && listaContratos.getGrupoServicio()!=null  )
						{
							if(listaContratos.getNivelAtencionServicio().equals(listaTipo.getNivelAtencionServicio()) && 
									listaContratos.getGrupoServicio().equals(listaTipo.getGrupoServicio()))
							{
								dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionGrupoServicio); 
								if(listaTipo.getCantidadServicio()!=null && listaTipo.getTarifaServicio()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadServicio();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaServicio().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getCodNivelAtencionServicio());
								dtoTotalProcesoPresupuestoCapitado.setGrupoServicio(listaTipo.getCodGrupoServicio());
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
								if(listaTipo.getCantidadArticulo()!=null && listaTipo.getTarifaArticulo()!=null)
								{
									cantidadTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()+listaTipo.getCantidadArticulo();
									dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

									valorTotal=dtoTotalProcesoPresupuestoCapitado.getCantidadTotal()*listaTipo.getTarifaArticulo().doubleValue();
									dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
								}
								dtoTotalProcesoPresupuestoCapitado.setNivelAtencion(listaTipo.getCodNivelAtencionArticulo());
								dtoTotalProcesoPresupuestoCapitado.setClaseInventario(listaTipo.getCodClaseInventario());
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
	@SuppressWarnings("unused")
	private void separarListasTotales(ArrayList<DtoTotalProcesoPresupuestoCapitado> listaFinalTotal)
	{
		for (DtoTotalProcesoPresupuestoCapitado finalTotal : listaFinalTotal) 
		{
			if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalServicio)){
				this.totalListaAgrupadaServicio.add(finalTotal);
			}else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalArticulo)){
				this.totalListaAgrupadaServicio.add(finalTotal);
			}
			/*else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionServicio)){
				this.totalListaAgrupadaServicioNivelAtencion.add(finalTotal);
			}else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionArticulo)){
				this.totalListaAgrupadaArticuloNivelAtencion.add(finalTotal);
			}else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalGrupoServicio)){
				this.totalListaAgrupadaServicioGrupoServicio.add(finalTotal);
			}else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalClaseInventario)){
				this.totalListaAgrupadaArticuloClaseInventario.add(finalTotal);
			}else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionGrupoServicio)){
				this.totalListaAgrupadaServicioNivelAtencionGrupoServicio.add(finalTotal);
			}else if(finalTotal.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalNivelAtencionClaseInventario)){
				this.totalListaAgrupadaArticuloNivelAtencionClaseInventario.add(finalTotal);
			}*/
		}
	}
	
	
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

	
	
	
	/******************************************************************************************************************************/
	
	private void organizarListas(ArrayList<DtoTotalProcesoPresupuestoCapitado> listaProcesoAutoriz,
			ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaFinalinconsistencias)
	{
		this.totalListaAgrupadaServicio = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.totalListaAgrupadaArticulo = new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.listaInconsistenciasProcesoAutoriz = new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		//Agrega a listas de Servicios y Articulos
		for(DtoTotalProcesoPresupuestoCapitado dtoTotalProceso:listaProcesoAutoriz)
		{
			if(dtoTotalProceso.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalServicio))
				this.totalListaAgrupadaServicio.add(dtoTotalProceso);
			if(dtoTotalProceso.getTipoTotal().equals(ConstantesIntegridadDominio.acronimoTipoTotalArticulo))
				this.totalListaAgrupadaArticulo.add(dtoTotalProceso);
		}
		//Agrega a lista Inconsistencias
		for(DtoInconsistenciasProcesoPresupuestoCapitado dtoInconsistencia: listaFinalinconsistencias)
		{
			this.listaInconsistenciasProcesoAutoriz.add(dtoInconsistencia);
		}
		
	}



	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> getListaInconsistenciasProcesoAutoriz() {
		return listaInconsistenciasProcesoAutoriz;
	}



	public void setListaInconsistenciasProcesoAutoriz(
			ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistenciasProcesoAutoriz) {
		this.listaInconsistenciasProcesoAutoriz = listaInconsistenciasProcesoAutoriz;
	}



	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> obtenerListaInconsistenciasProcesoAutoriz() {
		return listaInconsistenciasProcesoAutoriz;
	}
	
	
	/**
	 * Metodo que se encarga de recorrer la consulta para calcular los totales de las cantidades y valores 
	 * por cada autorizacion de articulo y servicio en la fecha -> convenio -> contrato -> servicio/articulo
	 * 
	 * @param listaResultadoConsultaProcesosCierre
	 * @param institucion
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> procesarTotalesFinal(ArrayList<DtoConsultaProcesoAutorizacion> listaResultadoConsultaProcesosCierre,
			int institucion)
	{
		DtoTotalProcesoPresupuestoCapitado 	dtoTotal	=new DtoTotalProcesoPresupuestoCapitado();
		
		//listas finales de las autorizaciones
		ArrayList<DtoTotalProcesoPresupuestoCapitado> 	listaValoreTotales	=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		ArrayList<DtoTotalProcesoPresupuestoCapitado>	listaFinalDiaTotal	=new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		
		for (DtoConsultaProcesoAutorizacion listaResultado : listaResultadoConsultaProcesosCierre) 
		{				
			//Resta para el Servicio o Articulo la sumatoria de la sumatoria de las autorizaciones Anuladas
			dtoTotal =new DtoTotalProcesoPresupuestoCapitado();
			dtoTotal = calcularTotalesFinal(listaResultado,institucion);
			listaValoreTotales.add(dtoTotal);
				
			dtoTotal.setFecha(listaResultado.getFecha());
			dtoTotal.setConvenio(listaResultado.getConvenio());
			dtoTotal.setContrato(listaResultado.getContrato());
			dtoTotal.setListaTotales(listaValoreTotales);
				listaFinalDiaTotal.add(dtoTotal);	
		}
		return listaFinalDiaTotal;
	}
	
	/**
	 * Metodo que se encarga de recorrer la consulta para calcular los totales de las INCONSISTENCIAS 
	 * por cada autorizacion de articulo y servicio en la fecha -> convenio -> contrato -> servicio/articulo
	 * 
	 * @param listaResultadoConsultaProcesosCierre
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> procesarInconsistencias(ArrayList<DtoConsultaProcesoAutorizacion> listaResultadoConsultaProcesosCierre)
	{
		ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>	listaInconsistencias=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		
		for (DtoConsultaProcesoAutorizacion listaResultado : listaResultadoConsultaProcesosCierre) 
		{				
			ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>inconsistencias=new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
			inconsistencias=validarInconsistenciaRegistro(listaResultado);
			
			if(!Utilidades.isEmpty(inconsistencias))
			{
				listaInconsistencias.addAll(inconsistencias);
			}
		}
		return listaInconsistencias;
	}
	
	/**
	 * Metodo que se encarga de calcular los totales de las autorizaciones de los SERVICIOS Y ARTICULOS
	 * y restar las autorizaciones en estado Anulada para el Cononvenio/Contrato-Servicio/Articulo 
	 * 1. Totales de Autorizaciones de Servicios.
	 * 1. Totales de Autorizaciones de Medicamentos e Insumos.
	 * 
	 * @param listaResultado
	 * @return
	 */	
	public DtoTotalProcesoPresupuestoCapitado calcularTotalesFinal(DtoConsultaProcesoAutorizacion lista,int institucion){
		
		DtoTotalProcesoPresupuestoCapitado dtoTotalProcesoPresupuestoCapitado	=new DtoTotalProcesoPresupuestoCapitado();		
				
		dtoTotalProcesoPresupuestoCapitado.setValor(0.0);
		dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(0);
		Double valorTotal=new Double(0);
		Integer cantidadTotal=new Integer(0);
		
		//Parametros de busqueda para autorizaciones Anuladas para cada dia de acumulado de Cierre
		DtoProcesoPresupuestoCapitado dtoBusquedaAnuladas	= new DtoProcesoPresupuestoCapitado();
		dtoBusquedaAnuladas.setInstitucion(institucion);
		dtoBusquedaAnuladas.setFechaInicio(lista.getFecha());
		dtoBusquedaAnuladas.setFechaFin(lista.getFecha());
		dtoBusquedaAnuladas.setConvenio(lista.getConvenio());
		dtoBusquedaAnuladas.setContrato(lista.getContrato());
		dtoBusquedaAnuladas.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoEstadoAnulado);
		if(lista.getCodigoArticulo()!=null)
			dtoBusquedaAnuladas.setCodArticulo(lista.getCodigoArticulo());
		else
			dtoBusquedaAnuladas.setCodServicio(lista.getCodigoServicio());
		
		
		if(lista.getCodigoServicio()!=null)
		{//SERVICIOS
			
			dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalServicio);
			dtoTotalProcesoPresupuestoCapitado.setCodigoServicio(lista.getCodigoServicio());//-------
			dtoTotalProcesoPresupuestoCapitado.setContrato(lista.getContrato());
			dtoTotalProcesoPresupuestoCapitado.setConvenio(lista.getConvenio());
			dtoTotalProcesoPresupuestoCapitado.setFecha(lista.getFecha());
			dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(lista.getCantidadServicio());
			dtoTotalProcesoPresupuestoCapitado.setValor(lista.getTarifaServicio().doubleValue());
			
			//**CONSULTA SI HAY AUTORIZACIONES ANULADAS PARA LOS SERVICIOS 
			ArrayList<DtoConsultaProcesoAutorizacion> listaAutorizAnulada=new ArrayList<DtoConsultaProcesoAutorizacion>();
			//MT6715
			ArrayList<DtoConsultaProcesoAutorizacion> listaAutorizaciones=new ArrayList<DtoConsultaProcesoAutorizacion>();
		
			listaAutorizAnulada = autorizacionCapitacionSubMundo.obtenerAutorizacionesCapitacionServicioAnulada(dtoBusquedaAnuladas);
			//MT6715
			listaAutorizaciones = autorizacionCapitacionSubMundo.obtenerAutorizacionesCapitacionServiciosEstado(dtoBusquedaAnuladas);
			
			if(!Utilidades.isEmpty(listaAutorizAnulada))
			{
				if(listaAutorizAnulada.get(0).getFechaAnulacion().equals(lista.getFecha()))
				{
					//MT6715
					cantidadTotal=listaAutorizaciones.get(0).getSumaCantidad()-listaAutorizAnulada.get(0).getCantidadServicio();
					dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);
					//MT6715
					valorTotal=listaAutorizaciones.get(0).getSumaValor().doubleValue()-listaAutorizAnulada.get(0).getTarifaServicio().doubleValue();
					dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
				}
			}
			
		}else if(lista.getCodigoArticulo()!=null )
		{//ARTICULOS
			
			dtoTotalProcesoPresupuestoCapitado.setTipoTotal(ConstantesIntegridadDominio.acronimoTipoTotalArticulo);
			dtoTotalProcesoPresupuestoCapitado.setCodigoArticulo(lista.getCodigoArticulo());//-------						
			dtoTotalProcesoPresupuestoCapitado.setContrato(lista.getContrato());
			dtoTotalProcesoPresupuestoCapitado.setConvenio(lista.getConvenio());
			dtoTotalProcesoPresupuestoCapitado.setFecha(lista.getFecha());
			dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(lista.getCantidadArticulo());
			dtoTotalProcesoPresupuestoCapitado.setValor(lista.getTarifaArticulo().doubleValue());
			
			//**CONSULTA SI HAY AUTORIZACIONES ANULADAS PARA LOS ARTICULOS 
			ArrayList<DtoConsultaProcesoAutorizacion> listaAutorizAnulada=new ArrayList<DtoConsultaProcesoAutorizacion>();
			listaAutorizAnulada = autorizacionCapitacionSubMundo.obtenerAutorizacionesCapitacionArticuloAnulada(dtoBusquedaAnuladas);
			
			if(!Utilidades.isEmpty(listaAutorizAnulada))
			{
				if(listaAutorizAnulada.get(0).getFechaAnulacion().equals(lista.getFecha()))
				{
					cantidadTotal=lista.getCantidadArticulo()-listaAutorizAnulada.get(0).getCantidadArticulo();
					dtoTotalProcesoPresupuestoCapitado.setCantidadTotal(cantidadTotal);

					valorTotal=lista.getTarifaArticulo().doubleValue()-listaAutorizAnulada.get(0).getTarifaArticulo().doubleValue();
					dtoTotalProcesoPresupuestoCapitado.setValor(valorTotal);
				}
			}
		}
		return 	dtoTotalProcesoPresupuestoCapitado;		
	}
}
