package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Interfaz de Generacion Archivo Plano Indicadores Calidad 
 * @author lgchavez@princetonsa.com
 */
public interface RegistroResumenParcialHistoriaClinicaDao {
	
	
	public HashMap consultarNotas(Connection con, HashMap mapa);
	public int insertarNotas(Connection con, HashMap mapa);
	
	public  HashMap consultarNotasAsocio(Connection con, HashMap mapa);
	
	
}