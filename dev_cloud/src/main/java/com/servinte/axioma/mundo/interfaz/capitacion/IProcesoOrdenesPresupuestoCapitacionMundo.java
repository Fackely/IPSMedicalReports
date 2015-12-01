package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;

public interface IProcesoOrdenesPresupuestoCapitacionMundo {
	
	/**
	 * Método principal que realiza todo el proceso de órdenes, invocando los métodos de consulta de órdenes
	 * y los métodos de generación de totales e inconsistencias
	 * @param DtoProcesoPresupuestoCapitado contiene los filtros de la consulta
	 * @return ArrayList<DtoTotalProcesoPresupuestoCapitado> lista de totales e inconsistencias ordenadas por fecha y convenio-contrato  
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoOrdenes(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado);
	
	
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaPorServicio();


	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaPorArticulo();


	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> obtenerListaInconsistenciasProcesoOrdenes();


}
