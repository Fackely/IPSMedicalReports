package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.servinte.axioma.fwk.exception.IPSException;

public interface ConsultaTarifasDao
{
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param especialidad
	 * @param tipoServicio
	 * @param grupo
	 * @return
	 */
	public HashMap buscar(Connection con, String codigo,String descripcion,String especialidad, String tipoServicio, String grupo,int codigoEsquemaTarifario) throws IPSException;
	
	/**
	 * 
	 * @param con
	 * @param codigoArticulo
	 * @param descripcionArticulo
	 * @param clase
	 * @param grupoArticulo
	 * @param subgrupo
	 * @param naturaleza
	 * @return
	 */
	public HashMap buscarArticulo(Connection con, String codigoArticulo,String descripcionArticulo,String clase, String grupoArticulo, String subgrupo, String naturaleza,int esquemaTarifario) throws IPSException;
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultaServicio(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultaArticulo(Connection con, HashMap vo);

	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param esServicio
	 * @return
	 */
	public HashMap<String,Object> consultarEsquemasVigenciasContrato(Connection con, int contrato, boolean esServicio);

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tarifarioOficial
	 * @param fechaVigencia 
	 * @param institucion 
	 * @param contrato 
	 * @param convenio 
	 * @param grupoServicio 
	 * @return
	 */
	public HashMap consultaServicioTarifaFinal(Connection con, int codigoServicio, int tarifarioOficial,int esquemaTarifario, int convenio, int contrato, int institucion, String fechaVigencia, int grupoServicio) throws IPSException;

	/**
	 * 
	 * @param con
	 * @param codigoArti
	 * @param esquemaTarifario
	 * @param convenio
	 * @param contrato
	 * @param institucion
	 * @param fechaVigencia
	 * @param claseInventario 
	 * @return
	 */
	public HashMap consultaArticulosTarifaFinal(Connection con, int codigoArti, int esquemaTarifario, int convenio, int contrato, int institucion, String fechaVigencia, int claseInventario) throws IPSException;

	/**
	 * Metodo encargado de armar la consulta de analisis de costo
	 * @author Jhony Alexander Duque A.
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- institucion  --> Requerido
	 * -- codigoArticulo  --> opcional
	 * -- descripcionArticulo  --> opcional
	 * -- codigoInterfaz  --> opcional
	 * -- clase  --> opcional
	 * -- grupo  --> opcional
	 * -- subgrupo  --> opcional
	 * -- naturaleza  --> opcional
	 * -- esquemaTarifario  --> opcional
	 * @return String
	 */
	public String obtenerConsulta (HashMap criterios);
	
}
