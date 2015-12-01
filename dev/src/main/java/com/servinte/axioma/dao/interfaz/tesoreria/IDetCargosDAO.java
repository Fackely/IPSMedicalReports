package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoCargosCuenta;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;

public interface IDetCargosDAO {

	/**
	 * Este método consulta las solicitudes de ordenes medicas para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro párametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesMedicas
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro);
	
	/**
	 * Este método consulta las solicitudes de Cirugia para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro párametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesCirugia 
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCirugiaCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro);
}
