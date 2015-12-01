package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOTarifasServicios;
import com.princetonsa.dto.facturacion.DtoReporteTarifasPorEsquemaTarifario;
import com.princetonsa.dto.facturacion.DtoTarifasPorEsquemaTarifario;
import com.servinte.axioma.dao.interfaz.facturacion.IEsquemasTarifariosDAO;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.delegate.facturacion.EsquemasTarifariosDelegate;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos para la entidad Esquema
 * Tarifario
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 11/11/2010
 */
public class EsquemasTarifariosHibernateDAO implements IEsquemasTarifariosDAO {

	/**
	 * Instancia de la clase EsquemasTarifariosDelegate
	 */
	EsquemasTarifariosDelegate delegate;

	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public EsquemasTarifariosHibernateDAO() {
		delegate = new EsquemasTarifariosDelegate();
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
		return delegate.listarEsquemasTarifarios(todos, procedimientos);
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
		return delegate.obtenerTarifasEsquemaTarifario(dto);
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
		return delegate.obtenerTarifas(codigoEsquemaTarifario);
	}
	
	/**
	 * Metodo que permite listar los esquemas tarifarios que se encuentren activos y que sean de servicios
	 * @author Diana Ruiz
	 * @return
	 */

	@Override
	public ArrayList<EsquemasTarifarios> listaEsquemaTarifarioServicios() {	
		return delegate.listaEsquemaTarifarioServicios();
	}

}
