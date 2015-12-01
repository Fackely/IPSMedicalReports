/*
 * Ene 18, 2006
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de Grupos Servicios
 */
public interface GruposServiciosDao 
{
	/**
	 * Método implementado para consultar los grupos de servicios
	 * de una institucion
	 * @param con
	 * @return
	 */
	public HashMap consultarGrupos(Connection con, int codInstitucion);
	
	/**
	 * Método usado para realizar una búsqueda avanzada de
	 * grupos de servicios
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param descripcion
	 * @param activo
	 * @param multiple
	 * @param tipo
	 * @param tipoSalaStandar
	 * @param numMesesUrgente
	 * @param acroMesesUrgente
	 * @param numMesesNormal
	 * @param acroMesesNormal
	 * @param tipoMonto
	 * @return
	 */
	public  HashMap busquedaGrupos(Connection con,int codigo,String acronimo,String descripcion,String activo, String multiple, String tipo, String tipoSalaStandar,
						String numMesesUrgente, String acroMesesUrgente, String numMesesNormal,String acroMesesNormal, int tipoMonto);	
	
	/**
	 * Método implementado para insertar un registro de
	 * grupos de servicios
	 * @param con
	 * @param acronimo
	 * @param descripcion
	 * @param activo
	 * @param multiple
	 * @param tipo
	 * @param codInstitucion
	 * @param tipoSalaStandar
	 * @param numMesesUrgente
	 * @param acroMesesUrgente
	 * @param numMesesNormal
	 * @param acroMesesNormal
	 * @param tipoMonto
	 * @return si la transacción es exitosa retorna el consecutivo del registro.
	 */
	public int insertar(Connection con,String acronimo,String descripcion,boolean activo, boolean multiple, String tipo ,int codInstitucion, String tipoSalaStandar,
						String numMesesUrgente, String acroMesesUrgente, String numMesesNormal,String acroMesesNormal, int tipoMonto);
	
	/**
	 * Método implementado para modificar un registro de
	 * grupos de servicios
	 * 
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @param multiple
	 * @param tipo
	 * @param tipoSalaStandar
	 * @param numMesesUrgente
	 * @param acroMesesUrgente
	 * @param numMesesNormal
	 * @param acroMesesNormal
	 * @param tipoMonto
	 * @return
	 */
	public int modificar(Connection con,String descripcion,boolean activo, int codigo, boolean multiple, String tipo, String tipoSalaStandar,
					String numMesesUrgente, String acroMesesUrgente, String numMesesNormal,String acroMesesNormal, int tipoMonto);
	
	/**
	 * Método implementado para eliminar un registro de grupos de servicios
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminar(Connection con,int codigo);
	
	/**
	 * Método encargado de obtener los tipos de salas con es_quirurgica = true
	 * @param Coneection con
	 * @param int codInstitucion
	 * @return HashMap con los resultados de la obtención de los tipos de salas con es_quirurgica = true
	 */
	public HashMap obtenerListaSalas(Connection con, int codInstitucion);
	
	/**
	 * Método encargado de obtener los tipos de montos 
	 * @param Coneection con
	 * @return HashMap 
	 */
	
	public HashMap obtenerListaTiposMontos(Connection con);
}
