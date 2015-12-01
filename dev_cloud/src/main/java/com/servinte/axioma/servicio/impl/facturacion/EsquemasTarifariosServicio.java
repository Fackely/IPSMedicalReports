package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DtoTarifasPorEsquemaTarifario;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEsquemasTarifariosMundo;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.servicio.interfaz.facturacion.IEsquemasTarifariosServicio;

/**
 * Esta clase se encarga de de definir los m&eacute;todos para la entidad
 * Esquema Tarifario
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 11/11/2010
 */
public class EsquemasTarifariosServicio implements IEsquemasTarifariosServicio {

	IEsquemasTarifariosMundo mundo;

	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public EsquemasTarifariosServicio() {
		mundo = FacturacionFabricaMundo.crearEsquemasTarifariosMundo();
	}

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
			boolean todos, boolean procedimientos) {
		return mundo.listarEsquemasTarifarios(todos, procedimientos);
	}

	/**
	 * M&eacute;todo encargado de obtener las tarifas por esquemas tarifarios.
	 * 
	 * @param dto
	 * @return ArrayList<DTOTarifasServicios>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@Override
	public ArrayList<DTOTarifasServicios> obtenerTarifasEsquemaTarifario(
			DtoReporteTarifasPorEsquemaTarifario dto) {
		return mundo.obtenerTarifasEsquemaTarifario(dto);
	}

	/**
	 * M&eacute;todo encargado de consultar todas la tarifas de un esquema
	 * tarifario determinado.
	 * 
	 * @param codigoEsquemaTarifario
	 * @return ArrayList<DtoTarifasPorEsquemaTarifario>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	@Override
	public ArrayList<DtoTarifasPorEsquemaTarifario> obtenerTarifas(
			int codigoEsquemaTarifario) {
		return mundo.obtenerTarifas(codigoEsquemaTarifario);
	}

}
