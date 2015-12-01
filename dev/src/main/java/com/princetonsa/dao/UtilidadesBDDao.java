/*
 * Created on 6/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @version 1.0, 6/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public interface UtilidadesBDDao
{
	
	/**
	 * Metodo que indica si una consulta esta o no bloqueada (select for update) en caso de que sea verdadero entonces retorna true,
	 * POR FAVOR SOLO USAR ESTE METODO A NIVEL DEL DAO
	 * @param consulta
	 * @return
	 */
	public boolean estaConsultaBloqueada(String consulta);

	/**
	 * Metodo que devuelve la cantidad de veces
	 * que es utilizado un key en una tabla
	 * @param connection
	 * @param tabla
	 * @param nombreKey
	 * @param valuekey
	 * @return
	 */
	public int estaUtilizadoEnTabla (Connection connection,String tabla,String nombreKey, int valuekey);

	/**
	 * 
	 * @return
	 */
	public boolean actualizarNumeroProcesosBD();
	
	/**
	 * Método implementado para cerrar objetos de persistencia
	 * @param ps
	 * @param rs
	 * @param con
	 */
	public void cerrarObjetosPersistencia(Statement ps,ResultSet rs,Connection con);
	
}
