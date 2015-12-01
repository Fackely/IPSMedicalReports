package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DtoTarifasPorEsquemaTarifario;
import com.servinte.axioma.orm.EsquemasTarifarios;

/**
 * Esta clase se encarga de de definir los m&eacute;todos para la entidad
 * Esquema Tarifario
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 11/11/2010
 */
public interface IEsquemasTarifariosDAO {

	/**
	 * M&eacute;todo encargado de listar los esquemas tarifarios registrados en
	 * el sistema
	 * 
	 * @param todos
	 * @param procedimientos
	 * @return ArrayList<EsquemasTarifarios>
	 * @autor Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<EsquemasTarifarios> listarEsquemasTarifarios(
			boolean todos, boolean procedimientos);
	
	
	/**
	 * Metodo que permite listar los esquemas tarifarios que se encuentren activos y que sean de servicios
	 * @author Diana Ruiz
	 * @return
	 */
	
	public ArrayList<EsquemasTarifarios> listaEsquemaTarifarioServicios();
	

	/**
	 * M&eacute;todo encargado de obtener las tarifas por esquemas tarifarios.
	 * 
	 * @param dto
	 * @return ArrayList<DTOTarifasServicios>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DTOTarifasServicios> obtenerTarifasEsquemaTarifario(
			DtoReporteTarifasPorEsquemaTarifario dto);

	/**
	 * M&eacute;todo encargado de consultar todas la tarifas de un esquema
	 * tarifario determinado.
	 * 
	 * @param codigoEsquemaTarifario
	 * @return ArrayList<DtoTarifasPorEsquemaTarifario>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DtoTarifasPorEsquemaTarifario> obtenerTarifas(
			int codigoEsquemaTarifario);

}
