/*
 * @author Jorge Armando Osorio Velasquez.
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public interface InformacionRecienNacidosDao
{

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarSolicitudes(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public abstract HashMap consultarHijosCirugia(Connection con, String cirugia);

	/**
	 * 
	 * @param con
	 * @param codigoInfoParto
	 * @param institucion 
	 * @return
	 */
	public abstract HashMap consultarHijo(Connection con, String codigoInfoParto, String institucion);

	
	/**
	 * 
	 * @param con
	 * @param map 
	 */
	public abstract boolean actualizarInformacionRecienNacido(Connection con, HashMap map);

	
	/**
	 * 
	 * @param con
	 * @param map
	 */
	public abstract boolean insertarInformacionGeneral(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public abstract HashMap consultarReanimacion(Connection con);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public abstract HashMap consultarTamizacionNeonatal(Connection con);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public abstract HashMap consultarAdaptacionNeonatalInmediata(Connection con, String codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public abstract HashMap consultarExamenesFisicos(Connection con, String codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public abstract HashMap consultarDiagnosticoRecienNacido(Connection con, String codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public abstract HashMap consultarSano(Connection con, String codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public abstract HashMap consultarApgar(Connection con, String codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract boolean esInformacionHijoFinalizada(Connection con, String consecutivo);
	
	/**
	 * 
	 * @param con
	 * @param codCx
	 * @return
	 */
	public abstract Vector obtenerConsecutivosInfoRecienNacidoDadoCx(Connection con, String codCx, String buscarFinalizada);

}
