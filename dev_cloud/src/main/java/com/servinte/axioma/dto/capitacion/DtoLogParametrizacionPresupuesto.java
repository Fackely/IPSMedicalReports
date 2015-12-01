package com.servinte.axioma.dto.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.LogDetalleParamPresup;
import com.servinte.axioma.orm.LogParamPresupuestoCap;

/**
 * Dto que contiene el log de la parametrización del presupuesto
 * y los detalles del mismo
 * @author diecorqu
 *
 */
public class DtoLogParametrizacionPresupuesto {
	
	/**
	 * Log Parametrización del presupuesto
	 */
	private LogParamPresupuestoCap logParametrizacion;
	
	/**
	 * Lista de detalles del log de parametrización
	 */
	private ArrayList<LogDetalleParamPresup> listaLogDetalleParametrizacion;
	
	
	
	public DtoLogParametrizacionPresupuesto() {
		this.logParametrizacion = new LogParamPresupuestoCap();
		this.listaLogDetalleParametrizacion = new ArrayList<LogDetalleParamPresup>();
	}
	
	public DtoLogParametrizacionPresupuesto(
			LogParamPresupuestoCap logParametrizacion,
			ArrayList<LogDetalleParamPresup> listaLogDetalleParametrizacion) {
		super();
		this.logParametrizacion = logParametrizacion;
		this.listaLogDetalleParametrizacion = listaLogDetalleParametrizacion;
	}



	public LogParamPresupuestoCap getLogParametrizacion() {
		return logParametrizacion;
	}

	public void setLogParametrizacion(LogParamPresupuestoCap logParametrizacion) {
		this.logParametrizacion = logParametrizacion;
	}

	public ArrayList<LogDetalleParamPresup> getListaLogDetalleParametrizacion() {
		return listaLogDetalleParametrizacion;
	}

	public void setListaLogDetalleParametrizacion(
			ArrayList<LogDetalleParamPresup> listaLogDetalleParametrizacion) {
		this.listaLogDetalleParametrizacion = listaLogDetalleParametrizacion;
	}
	
	

}
