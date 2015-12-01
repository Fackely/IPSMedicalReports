package com.servinte.axioma.bl.facturacion.facade;
import java.util.ArrayList;
import java.util.List;

import com.servinte.axioma.bl.facturacion.impl.CatalogoFacturacionMundo;
import com.servinte.axioma.bl.facturacion.impl.ConvenioContratoMundo;
import com.servinte.axioma.bl.facturacion.impl.DistribucionCuentaMundo;
import com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo;
import com.servinte.axioma.bl.facturacion.interfaz.IConvenioContratoMundo;
import com.servinte.axioma.bl.facturacion.interfaz.IDistribucionCuentaMundo;
import com.servinte.axioma.dto.administracion.CentroCostoDto;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.BusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.DetalleServicioDto;
import com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.facturacion.InfoCreacionHistoricoCargosDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.impl.facturacion.convenio.ConveniosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.inventario.InventarioServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IGruposServiciosServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IClaseInventarioServicio;


/**
 * Clase Fachada que provee todos los servicios de lógica de negocio del módulo de
 * Facturación a todos los Action de la Capa Web
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class FacturacionFacade {

	
	/**
	 * Servicio que obtiene la lista de contratos parametrizados
	 * en el sistema que estan asociados al convenio pasado por parámetro
	 * 
	 * @param codigoConvenio
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<ContratoDto> consultarContratosVigentesPorConvenio(int codigoConvenio)  throws IPSException{
		IConvenioContratoMundo convenioContratoMundo= new ConvenioContratoMundo();
		return convenioContratoMundo.consultarContratosVigentesPorConvenio(codigoConvenio);
	}

	
	/**
	 * Servicio que obtiene la lista de convenios parametrizados
	 * en el sistema que cumplan con los parámetros de búsqueda
	 * 
	 * @param institucion
	 * @param tipoContrato
	 * @param manejaCapitacionSubcontratada
	 * @param isActivo
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<ConvenioDto> consultarConveniosPorInstitucion(int institucion, Integer tipoContrato, Character manejaCapitacionSubcontratada, boolean isActivo)  throws IPSException{
		IConvenioContratoMundo convenioContratoMundo= new ConvenioContratoMundo();
		return convenioContratoMundo.consultarConveniosPorInstitucion(institucion, tipoContrato, manejaCapitacionSubcontratada, isActivo);
	}

	/**
	 * Método encargado de obtener los diferentes centros de costo o farmacias de acuerdo a los grupos de servicio
	 * o unidad de consulta de los servicios de las ordenes
	 * 
	 * @param centroAtencionPaciente
	 * @param ordenesPorAutorizar
	 * @param nivelesAutorizacion
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<CentroCostoDto> obtenerCentrosCostoEntidadSubcontratadaPorOrdenes(int centroAtencionPaciente, List<OrdenAutorizacionDto> ordenesPorAutorizar, List<NivelAutorizacionDto> nivelesAutorizacion)  throws IPSException{
		ICatalogoFacturacionMundo catalogoFacturacionMundo = new CatalogoFacturacionMundo();
		return catalogoFacturacionMundo.obtenerCentrosCostoEntidadSubcontratadaPorOrdenes(centroAtencionPaciente, ordenesPorAutorizar,
												nivelesAutorizacion);
	}
	
	/**
	 * Método que obtiene la parametrización del detalle del servicio de acuerdo
	 * al tarifario oficial
	 * 
	 * @param codigoServicio
	 * @param codigotarifario
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public DetalleServicioDto obtenerDetalleServicioXTarifarioOficial(int codigoServicio, int codigotarifario) throws IPSException{
		ICatalogoFacturacionMundo catalogoFacturacionMundo=new CatalogoFacturacionMundo();
		return catalogoFacturacionMundo.obtenerDetalleServicioXTarifarioOficial(codigoServicio, codigotarifario);
	}
	
	/**
	 * Metodo encargado de obtener los montos de cobro para el convenio seleccionado.<br/>
	 * DCU 986
	 * 
	 * @param filtroBusquedaMontosCobro
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public List<BusquedaMontosCobroDto> buscarMontosCobro(FiltroBusquedaMontosCobroDto filtroBusquedaMontosCobro) throws IPSException {
		ICatalogoFacturacionMundo catalogoFacturacionMundo=new CatalogoFacturacionMundo();
		return catalogoFacturacionMundo.buscarMontosCobro(filtroBusquedaMontosCobro,true);
	}
	
		
   public ArrayList<Convenios> listarConveniosActivos () throws IPSException{
	   IConveniosMundo conveniosMundo=new ConveniosMundo();
	   return  conveniosMundo.listarConveniosActivos();
   }
   
   public ArrayList<Convenios> listarConvenios () throws IPSException{
	   IConveniosMundo conveniosMundo=new ConveniosMundo();
	   return  conveniosMundo.listarConvenios();
   }
   
   public ArrayList<GruposServicios> gruposServicio () throws IPSException{
   IGruposServiciosServicio gruposServicio = FacturacionServicioFabrica.crearGruposServiciosServicio();
   return gruposServicio.buscarGruposServicioActivos();
   }
   public ArrayList<GruposServicios> gruposServicioTodo () throws IPSException{
   IGruposServiciosServicio gruposServicio = FacturacionServicioFabrica.crearGruposServiciosServicio();
   return gruposServicio.buscarGruposServicio();
   }
   
   public ArrayList<ClaseInventario> listarClaseInventario() throws IPSException{
	IClaseInventarioServicio claseInventarioServicio = InventarioServicioFabrica.crearClaseInventarioServicio();
	return claseInventarioServicio.buscarClaseInventario();
   }
    
   /**
  	 * Metodo encargado de consultar los grupos de servicio 
  	 * 
  	 * @param 
  	 * @return List<GrupoServicioDto>
  	 * @throws IPSException
  	 * @author ginsotfu
  	 */
   public List<GrupoServicioDto> consultarGruposServicio() throws IPSException{
	 IGruposServiciosServicio gruposServicio = FacturacionServicioFabrica.crearGruposServiciosServicio();
	 return gruposServicio.consultarGruposServicio();
	}
   
   /**
	 * Metodo encargado de obtener el grupo de servicio de un servicio 
	 * 
	 * @param codigoServicio
	 * @return GrupoServicioDto
	 * @throws IPSException
	 * @author ginsotfu
	 */
   public GrupoServicioDto consultaGrupoServicioxServicio(int codigoServicio) throws IPSException{
		ICatalogoFacturacionMundo catalogoFacturacionMundo=new CatalogoFacturacionMundo();
		return catalogoFacturacionMundo.consultaGrupoServicioxServicio(codigoServicio);
	}
   

   /**
    * Metodo encargado de crear el historico de los cargos en una distribucion de cuentas
    * @param infoCreacionHistoricoCargosDto
    * @return
    * @throws IPSException
    * @author hermorhu
    * @created 24-Nov-2012 
    */
   public boolean guardarHistoricoCargosXLiquidacionDistribucion(InfoCreacionHistoricoCargosDto infoCreacionHistoricoCargosDto) throws IPSException {
	   IDistribucionCuentaMundo mundoDistribucionCuenta = new DistribucionCuentaMundo();
	   return mundoDistribucionCuenta.guardarHistoricoCargosXLiquidacionDistribucion(infoCreacionHistoricoCargosDto);
   }
   
}