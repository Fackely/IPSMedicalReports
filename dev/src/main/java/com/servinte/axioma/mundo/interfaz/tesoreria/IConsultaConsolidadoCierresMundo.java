package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.Instituciones;

public interface IConsultaConsolidadoCierresMundo {

	
	
	/**
	 * Metodo que se encarga de consultar las empresas institucion
	 * @param institucion
	 * @return lista de empresas institucion
	 */
	public ArrayList<EmpresasInstitucion> consultarEmpresaInstitucionXinstitucion(Integer institucion);
	
	/**
	 * Metodo que se encarga de consultar todos los centros de atencion
	 * @return lista de centro de atencion
	 */
	public ArrayList<CentroAtencion> consultarTodosCentrosAtencion();
	
	/**
	 * Metodo que se encarga de consultar los consolidados por cierre
	 * @param fecha
	 * @param centroAtencion
	 * @param empresaInstitucion
	 * @return lista de consolidados
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierre(String fecha,String centroAtencion,String empresaInstitucion);
	
	/**
	 * Metodo que se encarga de consultar instituciones por usuario
	 * @param codigo
	 * @return lista de instituciones
	 */
	public Instituciones consultarIntitucionusuario(Integer codigo);

	/**
	 * Metodo que se encarga de consultar centros de atencion por usuario
	 * @param codigo
	 * @return se obtiene el centro de atencion por usuario
	 */
	public CentroAtencion obtenerCentroAtencionUsuario(Integer codigo );
	
	/**
	 * Metodo que se encarga de consultar los cierres por caja cajero
	 * @param fecha
	 * @param centroAtencion
	 * @param empresaInstitucion
	 * @return cierres por caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierreCajaCajero(String fecha,String centroAtencion,String empresaInstitucion);
	
	/**
	 * Metodo que consulta los traslado a caja mayor principal
	 * @param parametros
	 * @return ArrayList<DtoConsolidadoCierreReporte>
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoCajaMayorPrincipal(DtoConsolidadoCierreReporte parametros);
	
	
	/**
	 * Metodo que consulta los traslado a caja mayor principal
	 * @param parametros
	 * @return ArrayList<DtoConsolidadoCierreReporte>
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoHaciaCajaMayor(DtoConsolidadoCierreReporte parametros);
	
	
	
	/**
	 * @return lista de instituciones
	 */
	public ArrayList<Instituciones> listarInstituciones();
	
	/**
	 * Metodo que se encarga de consultar centros de atencion que esten activos
	 * @return centros de atencion
	 */
	public ArrayList<CentroAtencion> listarActivos();
	
	/**
	 * Metodo que se encarga de totalizar por cierres de centro de atencion
	 * @param cierres
	 * @return totales de cierres por centros de atencion
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalesCierreCentroAtencion(ArrayList<DtoConsolidadoCierreReporte> cierres);
	
	/**
	 * Metodo que se encarga de realizar totalizados por cierres de caja - cajeros
	 * @param totales
	 * @return lista con totales de cierres por caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalesXCentroAtencionCajaCajero(ArrayList<DtoConsolidadoCierreReporte> totales);
	
	/**
	 * @param cierres
	 * @return totales cierres caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalesCierreCentroAtencionCajaCajero(ArrayList<DtoConsolidadoCierreReporte> cierres);
	
	/**
	 * @return formas de pago 
	 */
	public List<FormasPago> consultarFormaPago();
	
	/**
	 * Adiciona a la lista los subtotales de cada centro de atencion
	 * @param totales
	 * @param subtotales
	 * @return lista con los subtotales por centro de atencion
	 */
	public ArrayList<DtoConsolidadoCierreReporte>  adicionarSubtotales(ArrayList<DtoConsolidadoCierreReporte> totales,ArrayList<DtoConsolidadoCierreReporte> subtotales);
}
