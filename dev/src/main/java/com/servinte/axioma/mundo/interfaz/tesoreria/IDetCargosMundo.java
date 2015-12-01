package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoCargosCuenta;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;

public interface IDetCargosMundo {

	
	/**
	 * Este m�todo consulta las solicitudes de ORDENES MEDICAS para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesMedicas
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro);
	

	/**
	 * Este m�todo consulta las solicitudes de Cirugia para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesCirugia 
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCirugiaCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro);
	
}
