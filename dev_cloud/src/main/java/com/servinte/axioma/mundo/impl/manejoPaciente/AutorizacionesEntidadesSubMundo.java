/**
 * 
 */
package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoClaseInventario;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoTotalesOrdenesEntidadesSub;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IClaseInventarioDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntidadesSubMundo;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;


/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Autorizaciones Entidad Subcontratada
 * 
 * @author Angela Maria Aguirre
 * @since 09/01/2011
 */
public class AutorizacionesEntidadesSubMundo implements
		IAutorizacionesEntidadesSubMundo {

	
	
	IAutorizacionesEntidadesSubDAO dao;
	
	/**
	 * Método constructor de la clase
	 */
	public AutorizacionesEntidadesSubMundo(){
		dao = ManejoPacienteDAOFabrica.crearAutorizacionesEntidadesSubDAO();
	}
	

	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorización de capitación
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto) {
		
		return dao.obtenerAutorizacionEntidadSubCapitacionPorID(dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorización de capitación, 
	 * estas autorizaciones son generadas en un ingreso estancia
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
		return dao.obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(dto);
	}
	

	/** Este Método se encarga de actualizar el detalle de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionEntidadSub(AutorizacionesEntidadesSub autorizacion){
		return dao.actualizarAutorizacionEntidadSub(autorizacion);
	}
	
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id) {
		return dao.obtenerAutorizacionesEntidadesSubPorId(id);
	}
	
	/**
	 * Obtener autorización por el consecutivo de autorizacion 
	 * 
	 * @param consecutivoAutorizacion
	 * @return
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionEntSubPorConsecutivoAutorizacion (String consecutivoAutorizacion){
		return dao.obtenerAutorizacionEntSubPorConsecutivoAutorizacion(consecutivoAutorizacion);
	}
	
	
	/**
	 * 
	 * Este método se encarga de buscar las autorizaciones en estado autorizado 
	 * según un contrato dado
	 *  
	 * @Author Angela Aguirre
	 */
	public ArrayList<AutorizacionesEntidadesSub> obtenerAutorizacionesContratoID(DtoContrato contrato){
		return dao.obtenerAutorizacionesContratoID(contrato);
	}
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada sin importar su vigencia.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubSinVigencia(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas)
	{
		return dao.obtenerAutorizacionesPorEntSubSinVigencia(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones entidades subcontratadas y de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado.
	 * Proceso Autorizaciones anexo 1027 
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author Camilo Gómez
	 */	
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesEntSubServiArti(DtoProcesoPresupuestoCapitado dto){
		return dao.obtenerAutorizacionesEntSubServiArti(dto);
	}
	
	/** 
	 * Este Método se encarga de consultar las ordenes autorizadas a entidades subcontratadas  
	 * Anexo 925
	 * 
	 * @param DtoBusquedaTotalOrdenesAutorizadasEntSub dto
	 * @return ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> obtenerOrdenesAutorizadasEntSub(DtoBusquedaTotalOrdenesAutorizadasEntSub dto){
		return dao.obtenerOrdenesAutorizadasEntSub(dto);
	}

	/**
	 * Este Método se encarga de consultar ordenar y calcular los totales de las ordenes autorizadas de entidades subcontratadas
	 * 
	 * @param DtoBusquedaTotalOrdenesAutorizadasEntSub dto
	 * @return ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> 
	 */
	public DtoConsultaTotalOrdenesAutorizadasEntSub ordenarOrdenesAutorizadasEntSub(DtoBusquedaTotalOrdenesAutorizadasEntSub dto)
	{		
		DtoConsultaTotalOrdenesAutorizadasEntSub 	consolidado					=new DtoConsultaTotalOrdenesAutorizadasEntSub();
		
		DtoConsultaTotalOrdenesAutorizadasEntSub 	dtoEntidad					=new DtoConsultaTotalOrdenesAutorizadasEntSub();
		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>	listaEntidadesTotal	=new ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>();
		DtoInconsistenciasProcesoPresupuestoCapitado inconsistenciaNivel		=new DtoInconsistenciasProcesoPresupuestoCapitado();
		//inconsistenciaNivel.setDescripcion("");		
		
		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaAutorizaciones = null;
		//consulta de las autorizaciones de entidades Sub
		IAutorizacionesEntidadesSubMundo autorizacionesEntidadesSubMundo=ManejoPacienteFabricaMundo.crearAutorizacionEntidadesSubMundo(); 
		listaAutorizaciones=autorizacionesEntidadesSubMundo.obtenerOrdenesAutorizadasEntSub(dto);
		
		//Consulta la clase de inventarios para cada articulo y se setea a la consulta de las autorizaciones ENTIDADES SUBCONTRATADAS
		DtoClaseInventario dtoClaseInventarioEntSub=new DtoClaseInventario();					
		IClaseInventarioDAO claseInventarioDAOEntSub=InventarioDAOFabrica.crearClaseInventarioDAO();
		
		if(!Utilidades.isEmpty(listaAutorizaciones))
		{
			for (DtoConsultaTotalOrdenesAutorizadasEntSub dtoConsultaTotal: listaAutorizaciones) 
			{
				dtoConsultaTotal.setTipoConsulta(dto.getTipoConsulta());
				dto.setNombreEntidadSub(dtoConsultaTotal.getNombreEntidadSub());
				
				if(dtoConsultaTotal.getCodigoArticulo()!=null && dtoConsultaTotal.getSubGrupoInventario()!=null)
				{
					dtoClaseInventarioEntSub=claseInventarioDAOEntSub.obtenerClaseInventarioPorSungrupo(dtoConsultaTotal.getSubGrupoInventario());				
					dtoConsultaTotal.setClaseInventario(dtoClaseInventarioEntSub.getNombre());
				}
				BigDecimal valorTotal= new BigDecimal(0);
				BigDecimal valor= new BigDecimal(0);
				if(dtoConsultaTotal.getCodigoArticulo()!=null)
				{
					dtoConsultaTotal.setNivelAtencion(dtoConsultaTotal.getNivelAtencionArticulo());
					valor=(dtoConsultaTotal.getValorUnitario()!=null?dtoConsultaTotal.getValorUnitario():new BigDecimal(0));
					int cantidad= (dtoConsultaTotal.getCantidadArticulo()!=null?dtoConsultaTotal.getCantidadArticulo():0);
					valorTotal= valor.multiply(new BigDecimal(cantidad));
					dtoConsultaTotal.setValorArticulo(valorTotal);
				}
				else 
					if(dtoConsultaTotal.getCodigoServicio()!=null)
					{
						dtoConsultaTotal.setNivelAtencion(dtoConsultaTotal.getNivelAtencionServicio());
						valor=(dtoConsultaTotal.getValorUnitario()!=null?dtoConsultaTotal.getValorUnitario():new BigDecimal(0));
						int cantidad= (dtoConsultaTotal.getCantidadServicio()!=null?dtoConsultaTotal.getCantidadServicio():0);
						valorTotal= valor.multiply(new BigDecimal(cantidad));
						dtoConsultaTotal.setValorServicio(valorTotal);
					}
						
				
				if(dtoConsultaTotal.getNivelAtencion()==null)
				{
					dtoEntidad=new DtoConsultaTotalOrdenesAutorizadasEntSub();
					inconsistenciaNivel=new DtoInconsistenciasProcesoPresupuestoCapitado();
					inconsistenciaNivel.setDescripcion(dtoConsultaTotal.getNombreArticulo().equals(null)?dtoConsultaTotal.getNombreServicio():dtoConsultaTotal.getNombreArticulo());
					dtoEntidad.setInconsistencia(inconsistenciaNivel);					
						listaEntidadesTotal.add(dtoEntidad);
						
					//consolidado.setConsolidado(listaEntidadesTotal);					
					//return consolidado;
				}
			}
			if(!Utilidades.isEmpty(listaEntidadesTotal))
			{
				consolidado.setConsolidado(listaEntidadesTotal);
				return consolidado;
			}
		}else{			
			return null;
		}
		
		//System.out.println("---------- SIN ORDENAR ------------");
		//pintaLista(listaAutorizaciones);

		//System.out.println("---------- ORDEN NATURAL DEFINIDO en compareTo ------------");
		//System.out.println("entid \t conv \t nivel \t \t arti \t servi \t estado \t grupo \t\t\t\t\t clase \t\t nomArticulo \t\t nomnServicio");
		//Collections.sort(listaAutorizaciones);
		//pintaLista(listaAutorizaciones);
		
		if(dto.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaNivelAtencion))
		{	
			//HashMap<String, DtoTotalesOrdenesEntidadesSub> sumNiveles = calcularNiveles(listaAutorizaciones);
			//consolidado.setSumNiveles(sumNiveles);
			consolidado.setConsolidado(listaAutorizaciones);			
		}else if(dto.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaGrupoServicioClaseInventario))
		{			
			//HashMap<String, DtoTotalesOrdenesEntidadesSub> sumGrupoClase= calcularGrupoYClase(listaAutorizaciones);
			//consolidado.setSumNiveles(sumGrupoClase);
			consolidado.setConsolidado(listaAutorizaciones);
		}else if(dto.getTipoConsulta().equals(ConstantesIntegridadDominio.acronimoTipoConsultaDetallado)){
			//HashMap<String, DtoTotalesOrdenesEntidadesSub> sumDetallado= calcularDetallado(listaAutorizaciones);
			//consolidado.setSumNiveles(sumDetallado);
			consolidado.setConsolidado(listaAutorizaciones);
		}

		return consolidado;		
	}
	
	
	/**
	 * Metodo que se encarga de calcular la sumatoria de las cantidades y valores de los servicios y articulos
	 * por FIXME Nivel de Atención
	 * 
	 * @param listaAutorizaciones
	 * @return HashMap<String, DtoTotalesOrdenesEntidadesSub>
	 * @author Camilo Gómez
	 */
	public static HashMap<String, DtoTotalesOrdenesEntidadesSub> calcularNiveles(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>listaAutorizaciones)
	{
		HashMap<String, DtoTotalesOrdenesEntidadesSub> sumNiveles = new HashMap<String, DtoTotalesOrdenesEntidadesSub>();
		Long entidad = listaAutorizaciones.get(0).getCodigoEntidadSub();
		String convenio = listaAutorizaciones.get(0).getCodigoConvenio().toString();
		String nivel = listaAutorizaciones.get(0).getNivelAtencion();
		String key = convenio+"-"+nivel;		
		
		/************dtoTotalesOrdenesEntidadesSub**********************/
		DtoTotalesOrdenesEntidadesSub dtoTotal=new DtoTotalesOrdenesEntidadesSub();
		dtoTotal=inicializarTotalesServiciosArticulos();
				
		//Niveles		
		Integer totalNivelAutorizado = 0;
		Integer totalNivelAnulado = 0;
		BigDecimal valorNivelAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorNivelAnulado=new  BigDecimal(0.0).setScale(2);
		//Convenios
		Integer totalConvenioAutorizado = 0;
		Integer totalConvenioAnulado = 0;
		BigDecimal valorConvenioAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorConvenioAnulado=new  BigDecimal(0.0).setScale(2);
		//Entidades
		Integer totalEntidadAutorizado = 0;
		Integer totalEntidadAnulado = 0;
		BigDecimal valorEntidadAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorEntidadAnulado=new  BigDecimal(0.0).setScale(2);
		//General
		Integer totalGeneralAutorizado = 0;
		Integer totalGeneralAnulado = 0;
		BigDecimal valorGeneralAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorGeneralAnulado=new  BigDecimal(0.0).setScale(2);
		/***************************************************************/
		
		for (DtoConsultaTotalOrdenesAutorizadasEntSub lista : listaAutorizaciones) 
		{
			if(!UtilidadTexto.isEmpty(lista.getNivelAtencion()))
			{
				if(lista.getCodigoEntidadSub().equals(entidad))//--->Por Entidad
				{
					if(convenio.equalsIgnoreCase(lista.getCodigoConvenio().toString()))//--->Por Convenio
					{
						if(nivel.equalsIgnoreCase(lista.getNivelAtencion()))//--->Por Nivel
						{
							if(lista.getCodigoServicio()!=null)
							{//TOTALES POR SERVICIOS
								if(lista.getValorServicio()!=null && lista.getCantidadServicio()!=null)
								{
									if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
									{
										dtoTotal.setTotalServicioAutorizado(dtoTotal.getTotalServicioAutorizado()+lista.getCantidadServicio());
										dtoTotal.setValorServicioAutorizado(dtoTotal.getValorServicioAutorizado().add(lista.getValorServicio()));
										
									}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
									{
										dtoTotal.setTotalServicioAnulado(dtoTotal.getTotalServicioAnulado()+lista.getCantidadServicio());
										dtoTotal.setValorServicioAnulado(dtoTotal.getValorServicioAnulado().add(lista.getValorServicio()));
									}
								}
							}else if(lista.getCodigoArticulo()!= null)
							{//TOTALES POR ARTICULOS
								if(lista.getValorArticulo()!=null && lista.getCantidadArticulo()!=null)
								{
									if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
									{
										dtoTotal.setTotalArticuloAutorizado(dtoTotal.getTotalArticuloAutorizado()+lista.getCantidadArticulo());
										dtoTotal.setValorArticuloAutorizado(dtoTotal.getValorArticuloAutorizado().add(lista.getValorArticulo()));
									}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
									{
										dtoTotal.setTotalArticuloAnulado(dtoTotal.getTotalArticuloAnulado()+lista.getCantidadArticulo());
										dtoTotal.setValorArticuloAnulado(dtoTotal.getValorArticuloAnulado().add(lista.getValorArticulo()));
									}
								}
							}
						}else
						{	//--------------------------------TOTALES NIVELES--------------------------------
							totalNivelAutorizado = dtoTotal.getTotalArticuloAutorizado()+dtoTotal.getTotalServicioAutorizado();
							totalNivelAnulado	 = dtoTotal.getTotalArticuloAnulado()+dtoTotal.getTotalServicioAnulado();
							valorNivelAutorizado = dtoTotal.getValorArticuloAutorizado().add(dtoTotal.getValorServicioAutorizado());
							valorNivelAnulado 	 = dtoTotal.getValorArticuloAnulado().add(dtoTotal.getValorServicioAnulado());
							
							//------------------------------TOTALES POR CONVENIO-------------------------------------
							totalConvenioAutorizado += totalNivelAutorizado;
							valorConvenioAutorizado = valorConvenioAutorizado.add(valorNivelAutorizado);
							totalConvenioAnulado +=totalNivelAnulado;
							valorConvenioAnulado = valorConvenioAnulado.add(valorNivelAnulado);
							
							
							//Autorizados
								dtoTotal.getTotalNivelAutorizado().put(nivel, totalNivelAutorizado);
								dtoTotal.getValorNivelAutorizado().put(nivel, valorNivelAutorizado);
							//Anulados
								dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
								dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
								
												
							//totalConvenio = totalNivelServicio+totalNivelArticulos;
							sumNiveles.put(key, dtoTotal);/***********************---->Adiciona Sumatoria y TotalNivel*/
							nivel = lista.getNivelAtencion();
							key = convenio+"-"+nivel;
							dtoTotal=inicializarTotalesServiciosArticulos();
							if(lista.getCodigoServicio()!=null)
							{	
								if(lista.getValorServicio()!=null && lista.getCantidadServicio()!=null)
								{
									if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
									{	
										dtoTotal.setTotalServicioAutorizado(dtoTotal.getTotalServicioAutorizado()+lista.getCantidadServicio());
										dtoTotal.setValorServicioAutorizado(dtoTotal.getValorServicioAutorizado().add(lista.getValorServicio()));
									}else if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
									{
										dtoTotal.setTotalServicioAnulado(dtoTotal.getTotalServicioAnulado()+lista.getCantidadServicio());
										dtoTotal.setValorServicioAnulado(dtoTotal.getValorServicioAnulado().add(lista.getValorServicio()));
									}
								}							
							}else if(lista.getCodigoArticulo()!= null)
							{
								if(lista.getValorArticulo()!=null && lista.getCantidadArticulo()!=null)
								{	
									if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
									{
										dtoTotal.setTotalArticuloAutorizado(dtoTotal.getTotalArticuloAutorizado()+lista.getCantidadArticulo());
										dtoTotal.setValorArticuloAutorizado(dtoTotal.getValorArticuloAutorizado().add(lista.getValorArticulo()));
									}else if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
									{																	
										dtoTotal.setTotalArticuloAnulado(dtoTotal.getTotalArticuloAnulado()+lista.getCantidadArticulo());
										dtoTotal.setValorArticuloAnulado(dtoTotal.getValorArticuloAnulado().add(lista.getValorArticulo()));
									}
								}	
							}
							//dtoTotal =new DtoTotalesOrdenesEntidadesSub();
							//totales = new ArrayList<Integer>();
							totalNivelAutorizado = 0;
							totalNivelAnulado	 = 0;
							valorNivelAutorizado = new BigDecimal(0.0).setScale(2);
							valorNivelAnulado 	 = new BigDecimal(0.0).setScale(2);
						}
					}else
					{	//---------------------------------TOTALES NIVELES-----------------------------------------
						totalNivelAutorizado = dtoTotal.getTotalArticuloAutorizado()+dtoTotal.getTotalServicioAutorizado();
						totalNivelAnulado	 = dtoTotal.getTotalArticuloAnulado()+dtoTotal.getTotalServicioAnulado();
						valorNivelAutorizado = dtoTotal.getValorArticuloAutorizado().add(dtoTotal.getValorServicioAutorizado());
						valorNivelAnulado 	 = dtoTotal.getValorArticuloAnulado().add(dtoTotal.getValorServicioAnulado());
						
						//Autorizados
							dtoTotal.getTotalNivelAutorizado().put(nivel, totalNivelAutorizado);
							dtoTotal.getValorNivelAutorizado().put(nivel, valorNivelAutorizado);
						//Anulados
							dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
							dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
						
						
						//------------------------------TOTALES POR CONVENIO-------------------------------------
						totalConvenioAutorizado += totalNivelAutorizado;
						valorConvenioAutorizado = valorConvenioAutorizado.add(valorNivelAutorizado);
						totalConvenioAnulado +=totalNivelAnulado;
						valorConvenioAnulado = valorConvenioAnulado.add(valorNivelAnulado);
						
						//-------------------------------TOTALES POR ENTIDAD--------------------------------------
						totalEntidadAutorizado += totalConvenioAutorizado;
						valorEntidadAutorizado = valorEntidadAutorizado.add(valorConvenioAutorizado);
						totalEntidadAnulado +=totalConvenioAnulado;
						valorEntidadAnulado = valorEntidadAnulado.add(valorConvenioAnulado);
						
						//Autorizados	
							dtoTotal.getTotalConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), totalConvenioAutorizado);
							dtoTotal.getValorConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), valorConvenioAutorizado);
						//Anulado
							dtoTotal.getTotalConvenioAnulado().put(Utilidades.convertirAEntero(convenio), totalConvenioAnulado);
							dtoTotal.getValorConvenioAnulado().put(Utilidades.convertirAEntero(convenio), valorConvenioAnulado);
						
						sumNiveles.put(key, dtoTotal);/***********************---->Adiciona Sumatoria,TotalNivel y TotalConvenio*/
						//totales = new ArrayList<Integer>();
						dtoTotal=inicializarTotalesServiciosArticulos();
						if(lista.getCodigoServicio()!=null)
						{
							if(lista.getValorServicio()!=null && lista.getCantidadServicio()!=null)
							{	
								if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									dtoTotal.setTotalServicioAutorizado(dtoTotal.getTotalServicioAutorizado()+lista.getCantidadServicio());
									dtoTotal.setValorServicioAutorizado(dtoTotal.getValorServicioAutorizado().add(lista.getValorServicio()));
								}else if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									dtoTotal.setTotalServicioAnulado(dtoTotal.getTotalServicioAnulado()+lista.getCantidadServicio());
									dtoTotal.setValorServicioAnulado(dtoTotal.getValorServicioAnulado().add(lista.getValorServicio()));
								}
							}
						}else if(lista.getCodigoArticulo()!= null)
						{	
							if(lista.getValorServicio()!=null && lista.getCantidadServicio()!=null)
							{	
								if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									dtoTotal.setTotalArticuloAutorizado(dtoTotal.getTotalArticuloAutorizado()+lista.getCantidadArticulo());
									dtoTotal.setValorArticuloAutorizado(dtoTotal.getValorArticuloAutorizado().add(lista.getValorArticulo()));
								}else if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									dtoTotal.setTotalArticuloAnulado(dtoTotal.getTotalArticuloAnulado()+lista.getCantidadArticulo());
									dtoTotal.setValorArticuloAnulado(dtoTotal.getValorArticuloAnulado().add(lista.getValorArticulo()));
								}
							}
						}
											
						//totalesConvenios.add(totalConvenio);
						convenio = lista.getCodigoConvenio().toString();
						nivel = lista.getNivelAtencion();
						key = convenio+"-"+nivel;
						//totalConvenio = 0;
						totalNivelAutorizado = 0;
						totalNivelAnulado	 = 0;
						valorNivelAutorizado = new BigDecimal(0.0).setScale(2);
						valorNivelAnulado 	 = new BigDecimal(0.0).setScale(2);
						totalConvenioAnulado=0;
						totalConvenioAutorizado=0;
						valorConvenioAutorizado=new BigDecimal(0.0).setScale(2);
						valorConvenioAnulado=new BigDecimal(0.0).setScale(2);
					}
				}else
				{
					//---------------------------------TOTALES NIVELES-----------------------------------------
					totalNivelAutorizado = dtoTotal.getTotalArticuloAutorizado()+dtoTotal.getTotalServicioAutorizado();
					totalNivelAnulado	 = dtoTotal.getTotalArticuloAnulado()+dtoTotal.getTotalServicioAnulado();
					valorNivelAutorizado = dtoTotal.getValorArticuloAutorizado().add(dtoTotal.getValorServicioAutorizado());
					valorNivelAnulado 	 = dtoTotal.getValorArticuloAnulado().add(dtoTotal.getValorServicioAnulado());
					
					//Autorizados
						dtoTotal.getTotalNivelAutorizado().put(nivel, totalNivelAutorizado);
						dtoTotal.getValorNivelAutorizado().put(nivel, valorNivelAutorizado);
					//Anulados
						dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
						dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
					
					
					//------------------------------TOTALES POR CONVENIO-------------------------------------
					totalConvenioAutorizado += totalNivelAutorizado;
					valorConvenioAutorizado = valorConvenioAutorizado.add(valorNivelAutorizado);
					totalConvenioAnulado +=totalNivelAnulado;
					valorConvenioAnulado = valorConvenioAnulado.add(valorNivelAnulado);
					
					//Autorizados	
						dtoTotal.getTotalConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), totalConvenioAutorizado);
						dtoTotal.getValorConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), valorConvenioAutorizado);
					//Anulado
						dtoTotal.getTotalConvenioAnulado().put(Utilidades.convertirAEntero(convenio), totalConvenioAnulado);
						dtoTotal.getValorConvenioAnulado().put(Utilidades.convertirAEntero(convenio), valorConvenioAnulado);
						
					//-------------------------------TOTALES POR ENTIDAD--------------------------------------
					totalEntidadAutorizado += totalConvenioAutorizado;
					valorEntidadAutorizado = valorEntidadAutorizado.add(valorConvenioAutorizado);
					totalEntidadAnulado +=totalConvenioAnulado;
					valorEntidadAnulado = valorEntidadAnulado.add(valorConvenioAnulado);
					
					//Autorizados	
						dtoTotal.getTotalEntidadAutorizado().put(entidad, totalEntidadAutorizado);
						dtoTotal.getValorEntidadAutorizado().put(entidad, valorEntidadAutorizado);
					//Anulado
						dtoTotal.getTotalEntidadAnulado().put(entidad, totalEntidadAnulado);
						dtoTotal.getValorEntidadAnulado().put(entidad, valorEntidadAnulado);
					
					//-------------------------------TOTALES POR GENERAL--------------------------------------
					totalGeneralAutorizado += totalEntidadAutorizado;
					valorGeneralAutorizado = valorGeneralAutorizado.add(valorEntidadAutorizado);
					totalGeneralAnulado += totalEntidadAnulado;
					valorGeneralAnulado = valorGeneralAnulado.add(valorEntidadAnulado);
						
						
					sumNiveles.put(key, dtoTotal);/***********************---->Adiciona Sumatoria,TotalNivel,TotalConvenio y TotalEntidad*/
					
					dtoTotal=inicializarTotalesServiciosArticulos();
					
					if(lista.getCodigoServicio()!=null)
					{
						if(lista.getValorServicio()!=null && lista.getCantidadServicio()!=null)
						{	
							if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
							{
								dtoTotal.setTotalServicioAutorizado(dtoTotal.getTotalServicioAutorizado()+lista.getCantidadServicio());
								dtoTotal.setValorServicioAutorizado(dtoTotal.getValorServicioAutorizado().add(lista.getValorServicio()));
							}else if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
							{
								dtoTotal.setTotalServicioAnulado(dtoTotal.getTotalServicioAnulado()+lista.getCantidadServicio());
								dtoTotal.setValorServicioAnulado(dtoTotal.getValorServicioAnulado().add(lista.getValorServicio()));
							}
						}
					}else if(lista.getCodigoArticulo()!= null)
					{	
						if(lista.getValorServicio()!=null && lista.getCantidadServicio()!=null)
						{	
							if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoAutorizado))
							{
								dtoTotal.setTotalArticuloAutorizado(dtoTotal.getTotalArticuloAutorizado()+lista.getCantidadArticulo());
								dtoTotal.setValorArticuloAutorizado(dtoTotal.getValorArticuloAutorizado().add(lista.getValorArticulo()));
							}else if(lista.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
							{
								dtoTotal.setTotalArticuloAnulado(dtoTotal.getTotalArticuloAnulado()+lista.getCantidadArticulo());
								dtoTotal.setValorArticuloAnulado(dtoTotal.getValorArticuloAnulado().add(lista.getValorArticulo()));
							}
						}
					}
										
					//totalesConvenios.add(totalConvenio);
					entidad = lista.getCodigoEntidadSub();
					convenio = lista.getCodigoConvenio().toString();
					nivel = lista.getNivelAtencion();					
					key = convenio+"-"+nivel;
					//totalConvenio = 0;
					
					totalNivelAutorizado = 0;
					totalNivelAnulado	 = 0;
					valorNivelAutorizado = new BigDecimal(0.0).setScale(2);
					valorNivelAnulado 	 = new BigDecimal(0.0).setScale(2);
					totalConvenioAnulado=0;
					totalConvenioAutorizado=0;
					valorConvenioAutorizado=new BigDecimal(0.0).setScale(2);
					valorConvenioAnulado=new BigDecimal(0.0).setScale(2);
					/*totalEntidadAutorizado+= 0;
					valorEntidadAutorizado = new BigDecimal(0.0).setScale(2);
					totalEntidadAnulado =0;
					valorEntidadAnulado = new BigDecimal(0.0).setScale(2);*/
				}
			}else
				continue;
		}
		//---------------------------------TOTALES NIVELES-----------------------------------------
		totalNivelAutorizado = dtoTotal.getTotalArticuloAutorizado()+dtoTotal.getTotalServicioAutorizado();
		totalNivelAnulado	 = dtoTotal.getTotalArticuloAnulado()+dtoTotal.getTotalServicioAnulado();
		valorNivelAutorizado = dtoTotal.getValorArticuloAutorizado().add(dtoTotal.getValorServicioAutorizado());
		valorNivelAnulado 	 = dtoTotal.getValorArticuloAnulado().add(dtoTotal.getValorServicioAnulado());
		
		//Autorizados
			dtoTotal.getTotalNivelAutorizado().put(nivel, totalNivelAutorizado);
			dtoTotal.getValorNivelAutorizado().put(nivel, valorNivelAutorizado);
		//Anulados
			dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
			dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
		
		
		//------------------------------TOTALES POR CONVENIO-------------------------------------
		totalConvenioAutorizado += totalNivelAutorizado;
		valorConvenioAutorizado = valorConvenioAutorizado.add(valorNivelAutorizado);
		totalConvenioAnulado +=totalNivelAnulado;
		valorConvenioAnulado = valorConvenioAnulado.add(valorNivelAnulado);
		
		//Autorizados	
			dtoTotal.getTotalConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), totalConvenioAutorizado);
			dtoTotal.getValorConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), valorConvenioAutorizado);
		//Anulado
			dtoTotal.getTotalConvenioAnulado().put(Utilidades.convertirAEntero(convenio), totalConvenioAnulado);
			dtoTotal.getValorConvenioAnulado().put(Utilidades.convertirAEntero(convenio), valorConvenioAnulado);
			
		//-------------------------------TOTALES POR ENTIDAD--------------------------------------
		totalEntidadAutorizado += totalConvenioAutorizado;
		valorEntidadAutorizado = valorEntidadAutorizado.add(valorConvenioAutorizado);
		totalEntidadAnulado +=totalConvenioAnulado;
		valorEntidadAnulado = valorEntidadAnulado.add(valorConvenioAnulado);
		
		//Autorizados	
			dtoTotal.getTotalEntidadAutorizado().put(entidad, totalEntidadAutorizado);
			dtoTotal.getValorEntidadAutorizado().put(entidad, valorEntidadAutorizado);
		//Anulado
			dtoTotal.getTotalEntidadAnulado().put(entidad, totalEntidadAnulado);
			dtoTotal.getValorEntidadAnulado().put(entidad, valorEntidadAnulado);
		
		//-------------------------------TOTALES GENERAL--------------------------------------			
			totalGeneralAutorizado += totalEntidadAutorizado;
			valorGeneralAutorizado = valorGeneralAutorizado.add(valorEntidadAutorizado);
			totalGeneralAnulado += totalEntidadAnulado;
			valorGeneralAnulado = valorGeneralAnulado.add(valorEntidadAnulado);
		//Autorizados
			dtoTotal.setTotalGeneralAutorizado(totalGeneralAutorizado);
			dtoTotal.setValorGeneralAutorizado(valorGeneralAutorizado);
		//Anulado
			dtoTotal.setTotalGeneralAnulado(totalGeneralAnulado);		
			dtoTotal.setValorGeneralAnulado(valorGeneralAnulado);
		
		sumNiveles.put(key, dtoTotal);/***********************---->Adiciona Sumatoria,TotalNivel,TotalConvenio,TotalEntidad y TotalGeneral*/
		
		return sumNiveles;
		
	}
	

	/**
	 * Metodo que se encarga de inicializar los totales de las sumatorias para cada nivel de atencion
	 * @param dtoTotal
	 * @return DtoTotalesOrdenesEntidadesSub dtoTotal
	 */
	public static DtoTotalesOrdenesEntidadesSub inicializarTotalesServiciosArticulos()
	{
		DtoTotalesOrdenesEntidadesSub dtoTotal=new DtoTotalesOrdenesEntidadesSub();
		//---------------------Inicializa por Nivel de Atencion------------------------
		//Servicios
		dtoTotal.setTotalServicioAutorizado(0);
		dtoTotal.setValorServicioAutorizado(new BigDecimal(0.00).setScale(2));
		dtoTotal.setTotalServicioAnulado(0);
		dtoTotal.setValorServicioAnulado(new BigDecimal(0.00).setScale(2));
		//Articulos
		dtoTotal.setTotalArticuloAutorizado(0);
		dtoTotal.setValorArticuloAutorizado(new BigDecimal(0.00).setScale(2));
		dtoTotal.setTotalArticuloAnulado(0);
		dtoTotal.setValorArticuloAnulado(new BigDecimal(0.00).setScale(2));
		
		//---------------------Inicializa por Grupo y Clase Inventario-------------------
		//Grupo
		dtoTotal.setTotalGrupoAuto(new HashMap<String, Integer>());
		dtoTotal.setValorGrupoAuto(new HashMap<String, BigDecimal>());
		dtoTotal.setTotalGrupoAnul(new HashMap<String, Integer>());
		dtoTotal.setValorGrupoAnul(new HashMap<String, BigDecimal>());
		//Clase
		dtoTotal.setTotalClaseAuto(new HashMap<String, Integer>());
		dtoTotal.setValorClaseAuto(new HashMap<String, BigDecimal>());
		dtoTotal.setTotalClaseAnul(new HashMap<String, Integer>());
		dtoTotal.setValorClaseAnul(new HashMap<String, BigDecimal>());
		
		//---------------------Inicializa por Detalle de Servicios y Articulos-----------
		//Servicio detalle
		dtoTotal.setTotalServicioAuto(new HashMap<Integer, Integer>());
		dtoTotal.setValorServicioAuto(new HashMap<Integer, BigDecimal>());
		dtoTotal.setTotalServicioAnul(new HashMap<Integer, Integer>());
		dtoTotal.setValorServicioAnul(new HashMap<Integer, BigDecimal>());
		//Articulo detalle
		dtoTotal.setTotalArticuloAuto(new HashMap<Integer, Integer>());
		dtoTotal.setValorArticuloAuto(new HashMap<Integer, BigDecimal>());
		dtoTotal.setTotalArticuloAnul(new HashMap<Integer, Integer>());
		dtoTotal.setValorArticuloAnul(new HashMap<Integer, BigDecimal>());
		
		dtoTotal.setContadorServicios(0);
		dtoTotal.setContadorArticulos(0);
		
		return dtoTotal;
	}
	
	
	
	
	
	/**
	 * Metodo que se encarga de calcular la sumatoria de las cantidades y valores de los servicios y articulos
	 * por FIXME GrupoServicio y ClaseInventario
	 * 
	 * @param listaAutorizaciones
	 * @return HashMap<String, DtoTotalesOrdenesEntidadesSub>
	 * @author Camilo Gómez
	 */
	public static HashMap<String, DtoTotalesOrdenesEntidadesSub> calcularGrupoYClase(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>listaAutorizaciones)
	{
		HashMap<String, DtoTotalesOrdenesEntidadesSub> sumGrupoClase = new HashMap<String, DtoTotalesOrdenesEntidadesSub>();
		
		String convenio = listaAutorizaciones.get(0).getCodigoConvenio().toString();
		String nivel = listaAutorizaciones.get(0).getNivelAtencion();
		String key = convenio+"-"+nivel;
		String grupoSer="";//listaAutorizaciones.get(0).getGrupoServicio();
		String claseInv="";//listaAutorizaciones.get(0).getClaseInventario();		
		
		/************dtoTotalesOrdenesEntidadesSub**********************/
		DtoTotalesOrdenesEntidadesSub dtoTotal=new DtoTotalesOrdenesEntidadesSub();
		dtoTotal=inicializarTotalesServiciosArticulos();
		
		//contadores de autorizaciones de servicios y articulos 
		int contadorAutoriz=0;
		//int conArti=0;
		
		//Totales por Grupo Servicio
		Integer totalGrupoAutorizado=0;
		BigDecimal valorGrupoAutorizado=new  BigDecimal(0.0).setScale(2);
		Integer totalGrupoAnulado=0;
		BigDecimal valorGrupoAnulado=new  BigDecimal(0.0).setScale(2);
		//Totales por Clase Inventario
		Integer totalClaseAutorizado=0;			
		BigDecimal valorClaseAutorizado=new  BigDecimal(0.0).setScale(2);
		Integer totalClaseAnulado=0;
		BigDecimal valorClaseAnulado=new  BigDecimal(0.0).setScale(2);
		//Niveles		
		Integer totalNivelAutorizado = 0;
		Integer totalNivelAnulado = 0;
		BigDecimal valorNivelAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorNivelAnulado=new  BigDecimal(0.0).setScale(2);
		//Convenios
		Integer totalConvenioAutorizado = 0;
		Integer totalConvenioAnulado = 0;
		BigDecimal valorConvenioAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorConvenioAnulado=new  BigDecimal(0.0).setScale(2);		
		//General
		Integer totalGeneralAutorizado = 0;
		Integer totalGeneralAnulado = 0;
		BigDecimal valorGeneralAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorGeneralAnulado=new  BigDecimal(0.0).setScale(2);
		/***************************************************************/
		
		for (DtoConsultaTotalOrdenesAutorizadasEntSub lista : listaAutorizaciones) 
		{
			if(!UtilidadTexto.isEmpty(lista.getNivelAtencion()))
			{
				if(convenio.equalsIgnoreCase(lista.getCodigoConvenio().toString()))//--->Por Convenio
				{
					if(nivel.equalsIgnoreCase(lista.getNivelAtencion()))//--->Por Nivel
					{
						if(lista.getCodigoServicio()!=null)
						{//SERVICIOS
							if(lista.getGrupoServicio()==null)
								continue;
							
							if(lista.getCantidadServicio()!=null && lista.getValorServicio()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalGrupoAuto().containsKey(lista.getGrupoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalGrupoAuto().put(lista.getGrupoServicio(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio());
										temValor = temValor.add(lista.getValorServicio().setScale(2));
											dtoTotal.getValorGrupoAuto().put(lista.getGrupoServicio(), temValor.setScale(2));
										
										totalGrupoAutorizado +=dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
										valorGrupoAutorizado  =valorGrupoAutorizado.add(dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio()));
											
										totalNivelAutorizado +=totalGrupoAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorGrupoAutorizado.setScale(2));
																				
									}else{
										dtoTotal.getTotalGrupoAuto().put(lista.getGrupoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorGrupoAuto().put(lista.getGrupoServicio(), lista.getValorServicio().setScale(2));
										
										totalGrupoAutorizado +=dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
										valorGrupoAutorizado  =valorGrupoAutorizado.add(dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio()));
											
										totalNivelAutorizado +=totalGrupoAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorGrupoAutorizado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
																	
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalGrupoAnul().containsKey(lista.getGrupoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalGrupoAnul().put(lista.getGrupoServicio(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio());
										temValor = temValor.add(lista.getValorServicio().setScale(2));
											dtoTotal.getValorGrupoAnul().put(lista.getGrupoServicio(), temValor.setScale(2));
											
										totalGrupoAnulado +=dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
										valorGrupoAnulado  =valorGrupoAnulado.add(dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio())); 
											
										totalNivelAnulado +=totalGrupoAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorGrupoAnulado.setScale(2));
										
									}else{
										dtoTotal.getTotalGrupoAnul().put(lista.getGrupoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorGrupoAnul().put(lista.getGrupoServicio(), lista.getValorServicio().setScale(2));
										
										totalGrupoAnulado +=dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
										valorGrupoAnulado  =valorGrupoAnulado.add(dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio())); 
											
										totalNivelAnulado +=totalGrupoAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorGrupoAnulado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);										
									}
								}
							}
						}else if(lista.getCodigoArticulo()!=null)
						{//ARTICULOS	
							if(lista.getClaseInventario()!=null)
								continue;
							
							if(lista.getCantidadArticulo()!=null && lista.getValorArticulo()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalClaseAuto().containsKey(lista.getClaseInventario()))
									{	
										Integer temCantidad = dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalClaseAuto().put(lista.getClaseInventario(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorClaseAuto().get(lista.getClaseInventario());
										temValor = temValor.add(lista.getValorArticulo().setScale(2));
											dtoTotal.getValorClaseAuto().put(lista.getClaseInventario(), temValor.setScale(2));
											
										totalClaseAutorizado +=	dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
										valorClaseAutorizado  =valorClaseAutorizado.add(dtoTotal.getValorClaseAuto().get(lista.getClaseInventario()));
											
										totalNivelAutorizado +=totalClaseAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorClaseAutorizado);
										
									}else{
										dtoTotal.getTotalClaseAuto().put(lista.getClaseInventario(), lista.getCantidadArticulo());
										dtoTotal.getValorClaseAuto().put(lista.getClaseInventario(), lista.getValorArticulo().setScale(2));
										
										totalClaseAutorizado +=	dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
										valorClaseAutorizado  =valorClaseAutorizado.add(dtoTotal.getValorClaseAuto().get(lista.getClaseInventario()));
											
										totalNivelAutorizado +=totalClaseAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorClaseAutorizado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
																	
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalClaseAnul().containsKey(lista.getClaseInventario()))
									{	
										Integer temCantidad = dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalClaseAnul().put(lista.getClaseInventario(), temCantidad);										
										BigDecimal temValor = dtoTotal.getValorClaseAnul().get(lista.getClaseInventario());
										temValor = temValor.add(lista.getValorArticulo());
											dtoTotal.getValorClaseAnul().put(lista.getClaseInventario(), temValor.setScale(2));
										
										totalClaseAnulado +=dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
										valorClaseAnulado  =valorClaseAnulado.add(dtoTotal.getValorClaseAnul().get(lista.getClaseInventario()));
											
										totalNivelAnulado +=totalClaseAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorClaseAnulado.setScale(2));
										
									}else{
										dtoTotal.getTotalClaseAnul().put(lista.getClaseInventario(), lista.getCantidadArticulo());
										dtoTotal.getValorClaseAnul().put(lista.getClaseInventario(), lista.getValorArticulo().setScale(2));
										
										totalClaseAnulado +=dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
										valorClaseAnulado  =valorClaseAnulado.add(dtoTotal.getValorClaseAnul().get(lista.getClaseInventario()));
											
										totalNivelAnulado +=totalClaseAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorClaseAnulado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}
							}
						}
					}else
					{//CAMBIO DE NIVEL-SE GUARDA TOTAL DE LOS GRUPOS
						
						//------------------TOTAL GRUPO SERVICIOS - CLASE INVENTARIOS-----------------------
							//grupo
							dtoTotal.getCantidadGrupoAuto().put(nivel, totalGrupoAutorizado);
							dtoTotal.getTarifaGrupoAuto().put(nivel, valorGrupoAutorizado);
							dtoTotal.getCantidadGrupoAnul().put(nivel, totalGrupoAnulado);
							dtoTotal.getTarifaGrupoAnul().put(nivel, valorGrupoAnulado);
							//clase
							dtoTotal.getCantidadClaseAuto().put(nivel, totalClaseAutorizado);
							dtoTotal.getTarifaClaseAuto().put(nivel, valorClaseAutorizado);
							dtoTotal.getCantidadClaseAnul().put(nivel, totalClaseAnulado);
							dtoTotal.getTarifaClaseAnul().put(nivel, valorClaseAnulado);
							
						//-------------------------TOTAL NIVELES--------------------------------------------						
							dtoTotal.getTotalNivelAutorizado().put(nivel,totalNivelAutorizado);
							dtoTotal.getValorNivelAutorizado().put(nivel,valorNivelAutorizado);
							dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
							dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
						
						//---------------------------------TOTALES CONVENIO-----------------------------------------
							totalConvenioAutorizado +=totalNivelAutorizado;
							valorConvenioAutorizado  =valorConvenioAutorizado.add(valorNivelAutorizado);
							totalConvenioAnulado += totalNivelAnulado;
							valorConvenioAnulado  = valorConvenioAnulado.add(valorNivelAnulado);
												
						sumGrupoClase.put(key, dtoTotal);
						key=lista.getCodigoConvenio()+"-"+lista.getNivelAtencion();
						dtoTotal= inicializarTotalesServiciosArticulos();
						nivel=lista.getNivelAtencion();
						grupoSer=lista.getGrupoServicio();
						claseInv=lista.getClaseInventario();
						
						totalGrupoAutorizado=0;
						valorGrupoAutorizado=new BigDecimal(0.0).setScale(2);
						totalGrupoAnulado=0;
						valorGrupoAnulado=new BigDecimal(0.0).setScale(2);
						totalClaseAutorizado=0;
						valorClaseAutorizado=new BigDecimal(0.0).setScale(2);
						totalClaseAnulado=0;
						valorClaseAnulado=new BigDecimal(0.0).setScale(2);
						totalNivelAutorizado=0;
						valorNivelAutorizado=new BigDecimal(0.0).setScale(2);
						totalNivelAnulado=0;
						valorNivelAnulado=new BigDecimal(0.0).setScale(2);
						
						if(lista.getCodigoServicio()!=null)
						{//SERVICIOS
							if(lista.getGrupoServicio()==null)
								continue;
							
							if(lista.getCantidadServicio()!=null && lista.getValorServicio()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalGrupoAuto().containsKey(lista.getGrupoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalGrupoAuto().put(lista.getGrupoServicio(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio());
										temValor = temValor.add(lista.getValorServicio().setScale(2));
											dtoTotal.getValorGrupoAuto().put(lista.getGrupoServicio(), temValor.setScale(2));
										
										totalGrupoAutorizado +=dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
										valorGrupoAutorizado  =valorGrupoAutorizado.add(dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio()));
											
										totalNivelAutorizado +=totalGrupoAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorGrupoAutorizado.setScale(2));
																				
									}else{
										dtoTotal.getTotalGrupoAuto().put(lista.getGrupoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorGrupoAuto().put(lista.getGrupoServicio(), lista.getValorServicio().setScale(2));
										
										totalGrupoAutorizado +=dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
										valorGrupoAutorizado  =valorGrupoAutorizado.add(dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio()));
											
										totalNivelAutorizado +=totalGrupoAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorGrupoAutorizado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
																	
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalGrupoAnul().containsKey(lista.getGrupoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalGrupoAnul().put(lista.getGrupoServicio(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio());
										temValor = temValor.add(lista.getValorServicio().setScale(2));
											dtoTotal.getValorGrupoAnul().put(lista.getGrupoServicio(), temValor.setScale(2));
											
										totalGrupoAnulado +=dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
										valorGrupoAnulado  =valorGrupoAnulado.add(dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio())); 
											
										totalNivelAnulado +=totalGrupoAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorGrupoAnulado.setScale(2));
										
									}else{
										dtoTotal.getTotalGrupoAnul().put(lista.getGrupoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorGrupoAnul().put(lista.getGrupoServicio(), lista.getValorServicio().setScale(2));
										
										totalGrupoAnulado +=dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
										valorGrupoAnulado  =valorGrupoAnulado.add(dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio())); 
											
										totalNivelAnulado +=totalGrupoAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorGrupoAnulado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);										
									}
								}
							}
						}else if(lista.getCodigoArticulo()!=null)
						{//ARTICULOS
							if(lista.getClaseInventario()!=null)
								continue;
							
							if(lista.getCantidadArticulo()!=null && lista.getValorArticulo()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalClaseAuto().containsKey(lista.getClaseInventario()))
									{	
										Integer temCantidad = dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalClaseAuto().put(lista.getClaseInventario(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorClaseAuto().get(lista.getClaseInventario());
										temValor = temValor.add(lista.getValorArticulo().setScale(2));
											dtoTotal.getValorClaseAuto().put(lista.getClaseInventario(), temValor.setScale(2));
											
										totalClaseAutorizado +=	dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
										valorClaseAutorizado  =valorClaseAutorizado.add(dtoTotal.getValorClaseAuto().get(lista.getClaseInventario()));
											
										totalNivelAutorizado +=totalClaseAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorClaseAutorizado);
										
									}else{
										dtoTotal.getTotalClaseAuto().put(lista.getClaseInventario(), lista.getCantidadArticulo());
										dtoTotal.getValorClaseAuto().put(lista.getClaseInventario(), lista.getValorArticulo().setScale(2));
										
										totalClaseAutorizado +=	dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
										valorClaseAutorizado  =valorClaseAutorizado.add(dtoTotal.getValorClaseAuto().get(lista.getClaseInventario()));
											
										totalNivelAutorizado +=totalClaseAutorizado;
										valorNivelAutorizado  =valorNivelAutorizado.add(valorClaseAutorizado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
																	
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalClaseAnul().containsKey(lista.getClaseInventario()))
									{	
										Integer temCantidad = dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalClaseAnul().put(lista.getClaseInventario(), temCantidad);										
										BigDecimal temValor = dtoTotal.getValorClaseAnul().get(lista.getClaseInventario());
										temValor = temValor.add(lista.getValorArticulo());
											dtoTotal.getValorClaseAnul().put(lista.getClaseInventario(), temValor.setScale(2));
										
										totalClaseAnulado +=dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
										valorClaseAnulado  =valorClaseAnulado.add(dtoTotal.getValorClaseAnul().get(lista.getClaseInventario()));
											
										totalNivelAnulado +=totalClaseAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorClaseAnulado.setScale(2));
										
									}else{
										dtoTotal.getTotalClaseAnul().put(lista.getClaseInventario(), lista.getCantidadArticulo());
										dtoTotal.getValorClaseAnul().put(lista.getClaseInventario(), lista.getValorArticulo().setScale(2));
										
										totalClaseAnulado +=dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
										valorClaseAnulado  =valorClaseAnulado.add(dtoTotal.getValorClaseAnul().get(lista.getClaseInventario()));
											
										totalNivelAnulado +=totalClaseAnulado;
										valorNivelAnulado  =valorNivelAnulado.add(valorClaseAnulado.setScale(2));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}
							}
						}
					}
				}else//CAMBIO DE CONVENIO
				{	//------------------TOTAL GRUPO SERVICIOS - CLASE INVENTARIOS-----------------------
					//grupo
					dtoTotal.getCantidadGrupoAuto().put(nivel, totalGrupoAutorizado);
					dtoTotal.getTarifaGrupoAuto().put(nivel, valorGrupoAutorizado);
					dtoTotal.getCantidadGrupoAnul().put(nivel, totalGrupoAnulado);
					dtoTotal.getTarifaGrupoAnul().put(nivel, valorGrupoAnulado);
					//clase
					dtoTotal.getCantidadClaseAuto().put(nivel, totalClaseAutorizado);
					dtoTotal.getTarifaClaseAuto().put(nivel, valorClaseAutorizado);
					dtoTotal.getCantidadClaseAnul().put(nivel, totalClaseAnulado);
					dtoTotal.getTarifaClaseAnul().put(nivel, valorClaseAnulado);
					
					//-------------------------TOTAL NIVELES--------------------------------------------					
					dtoTotal.getTotalNivelAutorizado().put(nivel,totalNivelAutorizado);
					dtoTotal.getValorNivelAutorizado().put(nivel,valorNivelAutorizado);
					dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
					dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
													
					//-------------------------TOTAL CONVENIO--------------------------------------------
						totalConvenioAutorizado +=totalNivelAutorizado;
						valorConvenioAutorizado  =valorConvenioAutorizado.add(valorNivelAutorizado);
						totalConvenioAnulado +=totalNivelAnulado;
						valorConvenioAnulado  =valorConvenioAnulado.add(valorNivelAnulado);
						
					dtoTotal.getTotalConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), totalConvenioAutorizado);
					dtoTotal.getValorConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), valorConvenioAutorizado);
					dtoTotal.getTotalConvenioAnulado().put(Utilidades.convertirAEntero(convenio), totalConvenioAnulado);
					dtoTotal.getValorConvenioAnulado().put(Utilidades.convertirAEntero(convenio), valorConvenioAnulado);
					
					//------------------------TOTAL GENERAL------------------------------------------------
					totalGeneralAutorizado += dtoTotal.getTotalConvenioAutorizado().get(Utilidades.convertirAEntero(convenio));
					valorGeneralAutorizado  = valorGeneralAutorizado.add(dtoTotal.getValorConvenioAutorizado().get(Utilidades.convertirAEntero(convenio)));
					totalGeneralAnulado += dtoTotal.getTotalConvenioAnulado().get(Utilidades.convertirAEntero(convenio));
					valorGeneralAnulado  = valorGeneralAnulado.add(dtoTotal.getValorConvenioAnulado().get(Utilidades.convertirAEntero(convenio)));
					
					
					sumGrupoClase.put(key, dtoTotal);///////////////////////////////////////////////////////////aca?
					key=lista.getCodigoConvenio()+"-"+lista.getNivelAtencion();
					dtoTotal= inicializarTotalesServiciosArticulos();
					grupoSer=lista.getGrupoServicio();
					claseInv=lista.getClaseInventario();
					nivel=lista.getNivelAtencion();
					convenio=lista.getCodigoConvenio().toString();
					
					totalGrupoAutorizado=0;
					valorGrupoAutorizado=new BigDecimal(0.0).setScale(2);
					totalGrupoAnulado=0;
					valorGrupoAnulado=new BigDecimal(0.0).setScale(2);
					totalClaseAutorizado=0;
					valorClaseAutorizado=new BigDecimal(0.0).setScale(2);
					totalClaseAnulado=0;
					valorClaseAnulado=new BigDecimal(0.0).setScale(2);
					totalNivelAutorizado=0;
					valorNivelAutorizado=new BigDecimal(0.0).setScale(2);
					totalNivelAnulado=0;
					valorNivelAnulado=new BigDecimal(0.0).setScale(2);
					totalConvenioAutorizado=0;
					valorConvenioAutorizado=new BigDecimal(0.0).setScale(2);
					totalConvenioAnulado=0;
					valorConvenioAnulado=new BigDecimal(0.0).setScale(2);
					
					if(lista.getCodigoServicio()!=null)
					{//SERVICIOS
						if(lista.getGrupoServicio()==null)
							continue;
						
						if(lista.getCantidadServicio()!=null && lista.getValorServicio()!=null)
						{
							if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
							{
								if(dtoTotal.getTotalGrupoAuto().containsKey(lista.getGrupoServicio()))
								{	
									Integer temCantidad = dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
									temCantidad += lista.getCantidadServicio(); 
										dtoTotal.getTotalGrupoAuto().put(lista.getGrupoServicio(), temCantidad);
										
									BigDecimal temValor = dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio());
									temValor = temValor.add(lista.getValorServicio().setScale(2));
										dtoTotal.getValorGrupoAuto().put(lista.getGrupoServicio(), temValor.setScale(2));
									
									totalGrupoAutorizado +=dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
									valorGrupoAutorizado  =valorGrupoAutorizado.add(dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio()));
										
									totalNivelAutorizado +=totalGrupoAutorizado;
									valorNivelAutorizado  =valorNivelAutorizado.add(valorGrupoAutorizado.setScale(2));
																			
								}else{
									dtoTotal.getTotalGrupoAuto().put(lista.getGrupoServicio(), lista.getCantidadServicio());
									dtoTotal.getValorGrupoAuto().put(lista.getGrupoServicio(), lista.getValorServicio().setScale(2));
									
									totalGrupoAutorizado +=dtoTotal.getTotalGrupoAuto().get(lista.getGrupoServicio());
									valorGrupoAutorizado  =valorGrupoAutorizado.add(dtoTotal.getValorGrupoAuto().get(lista.getGrupoServicio()));
										
									totalNivelAutorizado +=totalGrupoAutorizado;
									valorNivelAutorizado  =valorNivelAutorizado.add(valorGrupoAutorizado.setScale(2));
									
									contadorAutoriz++;
									dtoTotal.setContadorAutorizaciones(contadorAutoriz);
								}
																
							}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
							{
								if(dtoTotal.getTotalGrupoAnul().containsKey(lista.getGrupoServicio()))
								{	
									Integer temCantidad = dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
									temCantidad += lista.getCantidadServicio(); 
										dtoTotal.getTotalGrupoAnul().put(lista.getGrupoServicio(), temCantidad);
										
									BigDecimal temValor = dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio());
									temValor = temValor.add(lista.getValorServicio().setScale(2));
										dtoTotal.getValorGrupoAnul().put(lista.getGrupoServicio(), temValor.setScale(2));
										
									totalGrupoAnulado +=dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
									valorGrupoAnulado  =valorGrupoAnulado.add(dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio())); 
										
									totalNivelAnulado +=totalGrupoAnulado;
									valorNivelAnulado  =valorNivelAnulado.add(valorGrupoAnulado.setScale(2));
									
								}else{
									dtoTotal.getTotalGrupoAnul().put(lista.getGrupoServicio(), lista.getCantidadServicio());
									dtoTotal.getValorGrupoAnul().put(lista.getGrupoServicio(), lista.getValorServicio().setScale(2));
									
									totalGrupoAnulado +=dtoTotal.getTotalGrupoAnul().get(lista.getGrupoServicio());
									valorGrupoAnulado  =valorGrupoAnulado.add(dtoTotal.getValorGrupoAnul().get(lista.getGrupoServicio())); 
										
									totalNivelAnulado +=totalGrupoAnulado;
									valorNivelAnulado  =valorNivelAnulado.add(valorGrupoAnulado.setScale(2));
									
									contadorAutoriz++;
									dtoTotal.setContadorAutorizaciones(contadorAutoriz);										
								}
							}
						}
					}else if(lista.getCodigoArticulo()!=null)
					{//ARTICULOS	
						if(lista.getClaseInventario()!=null)
							continue;
						
						if(lista.getCantidadArticulo()!=null && lista.getValorArticulo()!=null)
						{
							if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
							{
								if(dtoTotal.getTotalClaseAuto().containsKey(lista.getClaseInventario()))
								{	
									Integer temCantidad = dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
									temCantidad += lista.getCantidadArticulo(); 
										dtoTotal.getTotalClaseAuto().put(lista.getClaseInventario(), temCantidad);
										
									BigDecimal temValor = dtoTotal.getValorClaseAuto().get(lista.getClaseInventario());
									temValor = temValor.add(lista.getValorArticulo().setScale(2));
										dtoTotal.getValorClaseAuto().put(lista.getClaseInventario(), temValor.setScale(2));
										
									totalClaseAutorizado +=	dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
									valorClaseAutorizado  =valorClaseAutorizado.add(dtoTotal.getValorClaseAuto().get(lista.getClaseInventario()));
										
									totalNivelAutorizado +=totalClaseAutorizado;
									valorNivelAutorizado  =valorNivelAutorizado.add(valorClaseAutorizado);
									
								}else{
									dtoTotal.getTotalClaseAuto().put(lista.getClaseInventario(), lista.getCantidadArticulo());
									dtoTotal.getValorClaseAuto().put(lista.getClaseInventario(), lista.getValorArticulo().setScale(2));
									
									totalClaseAutorizado +=	dtoTotal.getTotalClaseAuto().get(lista.getClaseInventario());
									valorClaseAutorizado  =valorClaseAutorizado.add(dtoTotal.getValorClaseAuto().get(lista.getClaseInventario()));
										
									totalNivelAutorizado +=totalClaseAutorizado;
									valorNivelAutorizado  =valorNivelAutorizado.add(valorClaseAutorizado.setScale(2));
									
									contadorAutoriz++;
									dtoTotal.setContadorAutorizaciones(contadorAutoriz);
								}
																
							}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
							{
								if(dtoTotal.getTotalClaseAnul().containsKey(lista.getClaseInventario()))
								{	
									Integer temCantidad = dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
									temCantidad += lista.getCantidadArticulo(); 
										dtoTotal.getTotalClaseAnul().put(lista.getClaseInventario(), temCantidad);										
									BigDecimal temValor = dtoTotal.getValorClaseAnul().get(lista.getClaseInventario());
									temValor = temValor.add(lista.getValorArticulo());
										dtoTotal.getValorClaseAnul().put(lista.getClaseInventario(), temValor.setScale(2));
									
									totalClaseAnulado +=dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
									valorClaseAnulado  =valorClaseAnulado.add(dtoTotal.getValorClaseAnul().get(lista.getClaseInventario()));
										
									totalNivelAnulado +=totalClaseAnulado;
									valorNivelAnulado  =valorNivelAnulado.add(valorClaseAnulado.setScale(2));
									
								}else{
									dtoTotal.getTotalClaseAnul().put(lista.getClaseInventario(), lista.getCantidadArticulo());
									dtoTotal.getValorClaseAnul().put(lista.getClaseInventario(), lista.getValorArticulo().setScale(2));
									
									totalClaseAnulado +=dtoTotal.getTotalClaseAnul().get(lista.getClaseInventario());
									valorClaseAnulado  =valorClaseAnulado.add(dtoTotal.getValorClaseAnul().get(lista.getClaseInventario()));
										
									totalNivelAnulado +=totalClaseAnulado;
									valorNivelAnulado  =valorNivelAnulado.add(valorClaseAnulado.setScale(2));
									
									contadorAutoriz++;
									dtoTotal.setContadorAutorizaciones(contadorAutoriz);
								}
							}
						}
					}
				}
				
			}else
				continue;
		}
		//------------------TOTAL GRUPO SERVICIOS - CLASE INVENTARIOS-----------------------
		//grupo
		dtoTotal.getCantidadGrupoAuto().put(nivel, totalGrupoAutorizado);
		dtoTotal.getTarifaGrupoAuto().put(nivel, valorGrupoAutorizado);
		dtoTotal.getCantidadGrupoAnul().put(nivel, totalGrupoAnulado);
		dtoTotal.getTarifaGrupoAnul().put(nivel, valorGrupoAnulado);
		//clase
		dtoTotal.getCantidadClaseAuto().put(nivel, totalClaseAutorizado);
		dtoTotal.getTarifaClaseAuto().put(nivel, valorClaseAutorizado);
		dtoTotal.getCantidadClaseAnul().put(nivel, totalClaseAnulado);
		dtoTotal.getTarifaClaseAnul().put(nivel, valorClaseAnulado);
		
		//-------------------------TOTAL NIVELES--------------------------------------------		
		dtoTotal.getTotalNivelAutorizado().put(nivel,totalNivelAutorizado);
		dtoTotal.getValorNivelAutorizado().put(nivel,valorNivelAutorizado);
		dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
		dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
										
		//-------------------------TOTAL CONVENIO--------------------------------------------
		totalConvenioAutorizado +=totalNivelAutorizado;
		valorConvenioAutorizado  =valorConvenioAutorizado.add(valorNivelAutorizado);
		totalConvenioAnulado +=totalNivelAnulado;
		valorConvenioAnulado  =valorConvenioAnulado.add(valorNivelAnulado);
		
		dtoTotal.getTotalConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), totalConvenioAutorizado);
		dtoTotal.getValorConvenioAutorizado().put(Utilidades.convertirAEntero(convenio), valorConvenioAutorizado);
		dtoTotal.getTotalConvenioAnulado().put(Utilidades.convertirAEntero(convenio), totalConvenioAnulado);
		dtoTotal.getValorConvenioAnulado().put(Utilidades.convertirAEntero(convenio), valorConvenioAnulado);
		
		////------------------------TOTAL GENERAL------------------------------------------------
		totalGeneralAutorizado += totalConvenioAutorizado;
		valorGeneralAutorizado  = valorGeneralAutorizado.add(valorConvenioAutorizado);
		totalGeneralAnulado += totalConvenioAnulado;
		valorGeneralAnulado  = valorGeneralAnulado.add(valorConvenioAnulado);
		//Autorizados
			dtoTotal.setTotalGeneralAutorizado(totalGeneralAutorizado);
			dtoTotal.setValorGeneralAutorizado(valorGeneralAutorizado);
		//Anulado
			dtoTotal.setTotalGeneralAnulado(totalGeneralAnulado);		
			dtoTotal.setValorGeneralAnulado(valorGeneralAnulado);
		
		sumGrupoClase.put(key, dtoTotal);/***********************---->Adiciona Sumatoria,TotalNivel,TotalConvenio,TotalEntidad y TotalGeneral*/
		
		return sumGrupoClase;
		
	}
	
	/**
	 * Metodo que se encarga de calcular la sumatoria de las cantidades y valores de los servicios y articulos
	 * por FIXME Detallado
	 * 
	 * @param listaAutorizaciones
	 * @return HashMap<String, DtoTotalesOrdenesEntidadesSub>
	 * @author Camilo Gómez
	 */
	/*public static HashMap<String, DtoTotalesOrdenesEntidadesSub> calcularDetallado(ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>listaAutorizaciones)
	{
		HashMap<String, DtoTotalesOrdenesEntidadesSub> sumDetallado= new HashMap<String, DtoTotalesOrdenesEntidadesSub>();
		
		String nivel = listaAutorizaciones.get(0).getNivelAtencion();
		String key = nivel;
		//Integer servicio=listaAutorizaciones.get(0).getCodigoServicio();
		//Integer articulo=listaAutorizaciones.get(0).getCodigoArticulo();		
		
		/************dtoTotalesOrdenesEntidadesSub**********************/
		/*DtoTotalesOrdenesEntidadesSub dtoTotal=new DtoTotalesOrdenesEntidadesSub();
		dtoTotal=inicializarTotalesServiciosArticulos();
		
		//contadores de autorizaciones de servicios y articulos 
		int contadorAutoriz=0;
		//int conArti=0;
		//Total Servicios
		Integer totalServicioAutorizado = 0;
		Integer totalServicioAnulado = 0;
		BigDecimal valorServicioAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorServicioAnulado=new  BigDecimal(0.0).setScale(2);
		//Total Articulos
		Integer totalArticuloAutorizado = 0;
		Integer totalArticuloAnulado = 0;
		BigDecimal valorArticuloAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorArticuloAnulado=new  BigDecimal(0.0).setScale(2);
		//Niveles		
		Integer totalNivelAutorizado = 0;
		Integer totalNivelAnulado = 0;
		BigDecimal valorNivelAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorNivelAnulado=new  BigDecimal(0.0).setScale(2);
		//General
		Integer totalGeneralAutorizado = 0;
		Integer totalGeneralAnulado = 0;
		BigDecimal valorGeneralAutorizado=new  BigDecimal(0.0).setScale(2);
		BigDecimal valorGeneralAnulado=new  BigDecimal(0.0).setScale(2);
		/***************************************************************/
		
		/*for (DtoConsultaTotalOrdenesAutorizadasEntSub lista : listaAutorizaciones) 
		{
			if(!UtilidadTexto.isEmpty(lista.getNivelAtencion()))
			{
				if(nivel.equalsIgnoreCase(lista.getNivelAtencion()))//--->Por Nivel
				{
					if(lista.getCodigoServicio()!=null)//-->Por Servicio
					{
						//if(servicio.equals(lista.getCodigoServicio()))
						{
							if(lista.getCantidadServicio()!=null && lista.getValorServicio()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalServicioAuto().containsKey(lista.getCodigoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalServicioAuto().put(lista.getCodigoServicio(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio());
										temValor = temValor.add(lista.getValorServicio());
											dtoTotal.getValorServicioAuto().put(lista.getCodigoServicio(), temValor);
											
										totalServicioAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorServicioAutorizado  =valorServicioAutorizado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio())); 
										
										totalNivelAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio())); 
										
									}else{
										dtoTotal.getTotalServicioAuto().put(lista.getCodigoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorServicioAuto().put(lista.getCodigoServicio(), lista.getValorServicio());
										
										totalServicioAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorServicioAutorizado  =valorServicioAutorizado.add(dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio())); 
										
										totalNivelAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalServicioAnul().containsKey(lista.getCodigoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalServicioAnul().put(lista.getCodigoServicio(), temCantidad);
										BigDecimal temValor = dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio());
										temValor = temValor.add(lista.getValorServicio());
											dtoTotal.getValorServicioAnul().put(lista.getCodigoServicio(), temValor);
											
										totalServicioAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());	
										valorServicioAnulado  =valorServicioAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
										
										totalNivelAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
											
									}else{
										dtoTotal.getTotalServicioAnul().put(lista.getCodigoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorServicioAnul().put(lista.getCodigoServicio(), lista.getValorServicio());
																			
										totalServicioAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());	
										valorServicioAnulado  =valorServicioAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
										
										totalNivelAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}
							}
						}
						
					}else if(lista.getCodigoArticulo()!=null)//-->Por Articulo
					{
						//if(articulo.equals(lista.getCodigoArticulo()))
						{	
							if(lista.getCantidadArticulo()!=null && lista.getValorArticulo()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalArticuloAuto().containsKey(lista.getCodigoArticulo()))
									{	
										Integer temCantidad = dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalArticuloAuto().put(lista.getCodigoArticulo(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo());
										temValor = temValor.add(lista.getValorArticulo());
											dtoTotal.getValorArticuloAuto().put(lista.getCodigoArticulo(), temValor);
											
										totalArticuloAutorizado += dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorArticuloAutorizado  = valorArticuloAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
											
										totalNivelAutorizado +=dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
											
									}else{
										dtoTotal.getTotalArticuloAuto().put(lista.getCodigoArticulo(), lista.getCantidadArticulo());
										dtoTotal.getValorArticuloAuto().put(lista.getCodigoArticulo(), lista.getValorArticulo());
										
										totalArticuloAutorizado += dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorArticuloAutorizado  = valorArticuloAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
											
										totalNivelAutorizado +=dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
																	
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalArticuloAnul().containsKey(lista.getCodigoArticulo()))
									{
										Integer temCantidad = dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalArticuloAnul().put(lista.getCodigoArticulo(), temCantidad);
										BigDecimal temValor = dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo());
										temValor = temValor.add(lista.getValorArticulo());
											dtoTotal.getValorArticuloAnul().put(lista.getCodigoArticulo(), temValor);
											
										totalArticuloAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorArticuloAnulado  =valorArticuloAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
										totalNivelAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
									}else{
										dtoTotal.getTotalArticuloAnul().put(lista.getCodigoArticulo(), lista.getCantidadArticulo());
										dtoTotal.getValorArticuloAnul().put(lista.getCodigoArticulo(), lista.getValorArticulo());
										
										totalArticuloAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorArticuloAnulado  =valorArticuloAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
										totalNivelAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}
							}
						}
					}
				}else
				{
					//-------------------------TOTAL SERVICIOS-TOTAL ARTICULOS--------------------------
					//servicios
					dtoTotal.getCantidadServiciosAuto().put(nivel, totalServicioAutorizado);
					dtoTotal.getTarifaServiciosAuto().put(nivel, valorServicioAutorizado);
					dtoTotal.getCantidadServiciosAnul().put(nivel, totalServicioAnulado);
					dtoTotal.getTarifaServiciosAnul().put(nivel, valorServicioAnulado);
					//articulos
					dtoTotal.getCantidadArticulosAuto().put(nivel, totalArticuloAutorizado);
					dtoTotal.getTarifaArticulosAuto().put(nivel, valorArticuloAutorizado);
					dtoTotal.getCantidadArticulosAnul().put(nivel, totalArticuloAnulado);
					dtoTotal.getTarifaArticulosAnul().put(nivel, valorArticuloAnulado);
					
					//-------------------------TOTAL NIVELES--------------------------------------------					
					dtoTotal.getTotalNivelAutorizado().put(nivel,totalNivelAutorizado);
					dtoTotal.getValorNivelAutorizado().put(nivel,valorNivelAutorizado);
					dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
					dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
					
					//-------------------------TOTAL GENERAL--------------------------------------------
					totalGeneralAutorizado +=totalNivelAutorizado;
					valorGeneralAutorizado  =valorGeneralAutorizado.add(valorNivelAutorizado).setScale(2); 
					totalGeneralAnulado +=totalNivelAnulado;
					valorGeneralAnulado  =valorGeneralAnulado.add(valorNivelAnulado).setScale(2);
														
					
					sumDetallado.put(key, dtoTotal);
					key=lista.getNivelAtencion();
					dtoTotal= inicializarTotalesServiciosArticulos();
					nivel=lista.getNivelAtencion();
					
					totalServicioAutorizado=new Integer(0);
					valorServicioAutorizado=new BigDecimal(0.0).setScale(2);
					totalServicioAnulado=new Integer(0);
					valorServicioAnulado=new BigDecimal(0.0).setScale(2);
					totalArticuloAutorizado=new Integer(0);
					valorArticuloAutorizado=new BigDecimal(0.0).setScale(2);
					totalArticuloAnulado=new Integer(0);
					valorArticuloAnulado=new BigDecimal(0.0).setScale(2);	
					
					totalNivelAutorizado=0;
					valorNivelAutorizado=new BigDecimal(0.0).setScale(2);
					totalNivelAnulado=0;
					valorNivelAnulado = new BigDecimal(0.0).setScale(2);
					
					//conServi=0;
					//conArti=0;
					
					if(lista.getCodigoServicio()!=null)//-->Por Servicio
					{
						//if(servicio.equals(lista.getCodigoServicio()))
						{
							if(lista.getCantidadServicio()!=null && lista.getValorServicio()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalServicioAuto().containsKey(lista.getCodigoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalServicioAuto().put(lista.getCodigoServicio(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio());
										temValor = temValor.add(lista.getValorServicio());
											dtoTotal.getValorServicioAuto().put(lista.getCodigoServicio(), temValor);
											
										totalServicioAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorServicioAutorizado  =valorServicioAutorizado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio())); 
										
										totalNivelAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio())); 
										
									}else{
										dtoTotal.getTotalServicioAuto().put(lista.getCodigoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorServicioAuto().put(lista.getCodigoServicio(), lista.getValorServicio());
										
										totalServicioAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorServicioAutorizado  =valorServicioAutorizado.add(dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio())); 
										
										totalNivelAutorizado +=dtoTotal.getTotalServicioAuto().get(lista.getCodigoServicio());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorServicioAuto().get(lista.getCodigoServicio()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalServicioAnul().containsKey(lista.getCodigoServicio()))
									{	
										Integer temCantidad = dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());
										temCantidad += lista.getCantidadServicio(); 
											dtoTotal.getTotalServicioAnul().put(lista.getCodigoServicio(), temCantidad);
										BigDecimal temValor = dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio());
										temValor = temValor.add(lista.getValorServicio());
											dtoTotal.getValorServicioAnul().put(lista.getCodigoServicio(), temValor);
											
										totalServicioAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());	
										valorServicioAnulado  =valorServicioAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
										
										totalNivelAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
											
									}else{
										dtoTotal.getTotalServicioAnul().put(lista.getCodigoServicio(), lista.getCantidadServicio());
										dtoTotal.getValorServicioAnul().put(lista.getCodigoServicio(), lista.getValorServicio());
																			
										totalServicioAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());	
										valorServicioAnulado  =valorServicioAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
										
										totalNivelAnulado +=dtoTotal.getTotalServicioAnul().get(lista.getCodigoServicio());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorServicioAnul().get(lista.getCodigoServicio()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}
							}
						}						
					}else if(lista.getCodigoArticulo()!=null)//-->Por Articulo
					{
						//if(articulo.equals(lista.getCodigoArticulo()))
						{	
							if(lista.getCantidadArticulo()!=null && lista.getValorArticulo()!=null)
							{
								if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoAutorizado))
								{
									if(dtoTotal.getTotalArticuloAuto().containsKey(lista.getCodigoArticulo()))
									{	
										Integer temCantidad = dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalArticuloAuto().put(lista.getCodigoArticulo(), temCantidad);
											
										BigDecimal temValor = dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo());
										temValor = temValor.add(lista.getValorArticulo());
											dtoTotal.getValorArticuloAuto().put(lista.getCodigoArticulo(), temValor);
											
										totalArticuloAutorizado += dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorArticuloAutorizado  = valorArticuloAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
											
										totalNivelAutorizado +=dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
											
									}else{
										dtoTotal.getTotalArticuloAuto().put(lista.getCodigoArticulo(), lista.getCantidadArticulo());
										dtoTotal.getValorArticuloAuto().put(lista.getCodigoArticulo(), lista.getValorArticulo());
										
										totalArticuloAutorizado += dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorArticuloAutorizado  = valorArticuloAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
											
										totalNivelAutorizado +=dtoTotal.getTotalArticuloAuto().get(lista.getCodigoArticulo());
										valorNivelAutorizado  =valorNivelAutorizado.add(dtoTotal.getValorArticuloAuto().get(lista.getCodigoArticulo()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
																	
								}else if(lista.getEstadoAutorizacion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado))
								{
									if(dtoTotal.getTotalArticuloAnul().containsKey(lista.getCodigoArticulo()))
									{
										Integer temCantidad = dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										temCantidad += lista.getCantidadArticulo(); 
											dtoTotal.getTotalArticuloAnul().put(lista.getCodigoArticulo(), temCantidad);
										BigDecimal temValor = dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo());
										temValor = temValor.add(lista.getValorArticulo());
											dtoTotal.getValorArticuloAnul().put(lista.getCodigoArticulo(), temValor);
											
										totalArticuloAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorArticuloAnulado  =valorArticuloAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
										totalNivelAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
									}else{
										dtoTotal.getTotalArticuloAnul().put(lista.getCodigoArticulo(), lista.getCantidadArticulo());
										dtoTotal.getValorArticuloAnul().put(lista.getCodigoArticulo(), lista.getValorArticulo());
										
										totalArticuloAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorArticuloAnulado  =valorArticuloAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
										totalNivelAnulado +=dtoTotal.getTotalArticuloAnul().get(lista.getCodigoArticulo());
										valorNivelAnulado  =valorNivelAnulado.add(dtoTotal.getValorArticuloAnul().get(lista.getCodigoArticulo()));
										
										contadorAutoriz++;
										dtoTotal.setContadorAutorizaciones(contadorAutoriz);
									}
								}
							}
						}
					}
				}
			}else{
				continue;
			}
		}
		//-------------------------TOTAL SERVICIOS-TOTAL ARTICULOS--------------------------
		//servicios
		dtoTotal.getCantidadServiciosAuto().put(nivel, totalServicioAutorizado);
		dtoTotal.getTarifaServiciosAuto().put(nivel, valorServicioAutorizado);
		dtoTotal.getCantidadServiciosAnul().put(nivel, totalServicioAnulado);
		dtoTotal.getTarifaServiciosAnul().put(nivel, valorServicioAnulado);
		//articulos
		dtoTotal.getCantidadArticulosAuto().put(nivel, totalArticuloAutorizado);
		dtoTotal.getTarifaArticulosAuto().put(nivel, valorArticuloAutorizado);
		dtoTotal.getCantidadArticulosAnul().put(nivel, totalArticuloAnulado);
		dtoTotal.getTarifaArticulosAnul().put(nivel, valorArticuloAnulado);
		
		//-------------------------TOTAL NIVELES--------------------------------------------		
		dtoTotal.getTotalNivelAutorizado().put(nivel,totalNivelAutorizado);
		dtoTotal.getValorNivelAutorizado().put(nivel,valorNivelAutorizado);
		dtoTotal.getTotalNivelAnulado().put(nivel, totalNivelAnulado);
		dtoTotal.getValorNivelAnulado().put(nivel, valorNivelAnulado);
										
				
		//-------------------------TOTAL GENERAL--------------------------------------------
		totalGeneralAutorizado +=totalNivelAutorizado;
		valorGeneralAutorizado  =valorGeneralAutorizado.add(valorNivelAutorizado).setScale(2); 
		totalGeneralAnulado +=totalNivelAnulado;
		valorGeneralAnulado  =valorGeneralAnulado.add(valorNivelAnulado).setScale(2);
		//Autorizados
			dtoTotal.setTotalGeneralAutorizado(totalGeneralAutorizado);
			dtoTotal.setValorGeneralAutorizado(valorGeneralAutorizado);
		//Anulado
			dtoTotal.setTotalGeneralAnulado(totalGeneralAnulado);		
			dtoTotal.setValorGeneralAnulado(valorGeneralAnulado);
		
		sumDetallado.put(key, dtoTotal);/***********************---->Adiciona Sumatoria,TotalNivel,TotalConvenio,TotalEntidad y TotalGeneral*/
		
	/*	return sumDetallado;
		
	}*/
}
