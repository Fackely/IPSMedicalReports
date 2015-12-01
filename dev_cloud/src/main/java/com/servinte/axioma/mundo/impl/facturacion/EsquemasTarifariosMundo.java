package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DtoTarifasPorEsquemaTarifario;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEsquemasTarifariosDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IEsquemasTarifariosMundo;
import com.servinte.axioma.orm.EsquemasTarifarios;

/**
 * Esta clase se encarga de de definir los m&eacute;todos para la entidad
 * Esquema Tarifario
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 11/11/2010
 */
public class EsquemasTarifariosMundo implements IEsquemasTarifariosMundo {

	/**
	 * Instancia de la clase EsquemaTarifarioDao
	 */
	IEsquemasTarifariosDAO dao;

	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public EsquemasTarifariosMundo() {
		dao = FacturacionFabricaDAO.crearEsquemasTarifariosDAO();
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
		return dao.listarEsquemasTarifarios(todos, procedimientos);
	}
	
	/**
	 * Metodo que permite listar los esquemas tarifarios que se encuentren activos y que sean de servicios
	 * @author Diana Ruiz
	 * @return
	 */
	
	public ArrayList<EsquemasTarifarios> listaEsquemaTarifarioServicios(){
		return dao.listaEsquemaTarifarioServicios();
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
		return dao.obtenerTarifasEsquemaTarifario(dto);
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
		return dao.obtenerTarifas(codigoEsquemaTarifario);
	}

}
