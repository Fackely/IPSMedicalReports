package com.servinte.axioma.servicio.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.FinalidadesServicio;

public interface IFinalidadesServicioServicio {

	/**
	 * Este Método se encarga de consultar las finalidades del servicio
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesServicio>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<FinalidadesServicio> consultarFinalidadesServicio();
}
