package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoCargosCuenta;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.servinte.axioma.dao.interfaz.tesoreria.IDetCargosDAO;
import com.servinte.axioma.orm.delegate.tesoreria.DetCargosDelegate;

public class DetCargosDAO implements IDetCargosDAO{

	
	private DetCargosDelegate delegate;
	
	public DetCargosDAO (){
		delegate=new DetCargosDelegate();
	}
	
	/**
	 * Este m�todo consulta las solicitudes de ordenes medicas para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesMedicas
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro){
		return delegate.consultarSolicitudesCargosCuenta(dtoFiltro);
	}
	
	
	/**
	 * Este m�todo consulta las solicitudes de Cirugia para cargos a la cuenta 
	 * 
	 * @param DtoProcesoPresupuestoCapitado dtoFiltro p�rametros de consulta
	 * @return ArrayList<DtoConsultaProcesoCargosCuenta> listadoOrdenesCirugia 
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoConsultaProcesoCargosCuenta> consultarSolicitudesCirugiaCargosCuenta(DtoProcesoPresupuestoCapitado dtoFiltro){
		return delegate.consultarSolicitudesCirugiaCargosCuenta(dtoFiltro);
	}
}
