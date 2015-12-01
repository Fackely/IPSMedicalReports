package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.FinalidadesServicio;

public interface IFinalidadesServicioDAO {

	/**
	 * Este M�todo se encarga de consultar las finalidades del servicio
	 * registradas en el sistema
	 * 
	 * @return ArrayList<FinalidadesServicio>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<FinalidadesServicio> consultarFinalidadesServicio();
}
