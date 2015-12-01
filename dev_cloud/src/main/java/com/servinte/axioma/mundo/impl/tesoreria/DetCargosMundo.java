package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoCargosCuenta;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetCargosDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetCargosMundo;

public class DetCargosMundo implements IDetCargosMundo{

	
	IDetCargosDAO dao;
	
	public DetCargosMundo(){
		dao = TesoreriaFabricaDAO.crearDetCargos();
	}
	
	/**
	 * Este m�todo consulta las solicitudes de ordenes medicas para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesMedicas
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarSolicitudesCargosCuenta(dtoFiltro);
	}
	
	/**
	 * Este m�todo consulta las solicitudes de Cirugia para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesCirugia 
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCirugiaCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro){
		return dao.consultarSolicitudesCirugiaCargosCuenta(dtoFiltro);
	}
}
