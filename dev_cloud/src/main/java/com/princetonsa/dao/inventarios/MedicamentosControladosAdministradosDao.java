package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre de 2008
 */

public interface MedicamentosControladosAdministradosDao {

	/**
	 * @param con, voy
	 * @return */
	public abstract HashMap generarResultados(Connection con, HashMap voy);
	
	
	/** Consultar Medicamentos Controlados Administrados
	 * @param (con, criterios)
	 * @return */
	public abstract HashMap consultarMediControAdmin(Connection con, HashMap criterios);
}