package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.historiaClinica.TiposTratamientosOdontologicos;

public interface TiposTratamientosOdontologicosDao
{
	/**
	 * Metodo de consulta de los tipos de TRatamientos odontologicos
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */
	public HashMap<String, Object> consultaTiposT (Connection con, int codigoInstitucion);
	
	/**
	 * 
	 */
	public boolean eliminarTipoT(Connection con, String tipoT);
	
	/**
	 * 
	 */
	public boolean insertarTiposT(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos);
	
	/**
	 * 
	 */
	public boolean modificarTiposT(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos);
}