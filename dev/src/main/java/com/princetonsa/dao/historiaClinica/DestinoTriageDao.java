/*
 * Mayo 31, 2006
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de Destinos TRiage
 */
public interface DestinoTriageDao 
{
	/**
	 * Método implementado para cargar los destinos del paciente
	 * @param con
	 * @param institucion
	 * @param codigo
	 * @return
	 */
	public HashMap cargar(Connection con,String institucion,String codigo);
	
	/**
	 * Método implementado para insertar un destino triage
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param indicadorAdmiUrg
	 * @param institucion
	 * @return
	 */
	public int insertar(Connection con,String codigo,String nombre,boolean indicadorAdmiUrg,String institucion);
	
	/**
	 * Método implementado para modificar un destino Triage
	 * @param con
	 * @param nombre
	 * @param indicadorAdminUrg
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int modificar(Connection con,String nombre,boolean indicadorAdminUrg, String codigo,String institucion);
	
	/**
	 * Método implementado para eliminar un registro destino triage
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con,String codigo,String institucion);
}
