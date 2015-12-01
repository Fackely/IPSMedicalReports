package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.servinte.axioma.fwk.exception.IPSException;

public interface CopiarTarifasEsquemaTarifarioDao {

	/**
	 * 
	 * @param con
	 * @param tarifarioOrigen
	 * @param institucion 
	 * @return
	 */
	public abstract HashMap<String, Object> obtenerTarifario(Connection con, String tarifarioOrigen, String institucion);
	
	/**
	 * 
	 * @param con
	 * @param tarifarioOrigen
	 * @param institucion 
	 * @param tarifarioDestino 
	 * @return
	 */
	public abstract HashMap<String, Object> consultarTarifas(Connection con, String tarifarioOrigen, String esinventario, String tarifarioOficial);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 */
	public abstract boolean insertarInventarioPorcentaje(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException;

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarInventario(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param tipoLiquidacion 
	 * @param chequeo 
	 * @param porcentaje 
	 * @return
	 */
	public abstract boolean insertarTarifasIss(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param tipoLiquidacion 
	 * @param chequeo 
	 * @param porcentaje 
	 * @return
	 */
	public abstract boolean insertarTarifasSoat(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 */
	public abstract boolean insertarTarifasIssValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException;
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param porcentaje
	 * @param chequeo
	 * @return
	 */
	public abstract boolean insertarTarifasSoatValor(Connection con, HashMap vo, String porcentaje, String chequeo) throws IPSException;
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cantidadEsquema
	 * @param unidades
	 * @return
	 */
	public abstract boolean insertarTarifasIssUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException;
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @param cantidadEsquema
	 * @param unidades
	 * @return
	 */
	public abstract boolean insertarTarifasSoatUnidades(Connection con, HashMap vo, String cantidadEsquema, String unidades) throws IPSException;
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public boolean existeTarifaInventarios(Connection con, int codigoEsquematarifario, int codigoArticulo);
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquematarifario
	 * @param codigoArticulo
	 * @return
	 */
	public boolean existeTarifaServicios(Connection con, int codigoEsquematarifario, int codigoServicio, int codigoTarifario);
	
	
}
