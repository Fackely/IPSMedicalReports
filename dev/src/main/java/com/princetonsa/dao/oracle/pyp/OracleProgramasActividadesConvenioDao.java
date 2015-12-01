/*
 * Ago 08, 2006
 */
package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ProgramasActividadesConvenioDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseProgramasActividadesConvenioDao;

/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Programas y Actividades por convenio
 */
public class OracleProgramasActividadesConvenioDao implements
		ProgramasActividadesConvenioDao 
{
	/**
	 * M�todo implementado para consultar los programas y actividades por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con,HashMap campos)
	{
		return SqlBaseProgramasActividadesConvenioDao.consultar(con,campos);
	}
	
	/**
	 * M�todo implementado para insertar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos)
	{
		campos.put("secuencia","seq_prog_act_pyp_convenio.nextval");
		return SqlBaseProgramasActividadesConvenioDao.insertar(con,campos);
	}
	
	/**
	 * M�todo implementado para modificar un programa y actividad por convenio
	 * 
	 * @param con
	 * @return
	 */
	public int modificar(Connection con,HashMap campos)
	{
		return SqlBaseProgramasActividadesConvenioDao.modificar(con,campos);
	}
	
	/**
	 * M�todo implementado para eliminar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos)
	{
		return SqlBaseProgramasActividadesConvenioDao.eliminar(con,campos);
	}
	
	/**
	 * M�todo implementado para cargar  las actividades de un programa
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarActividadesPrograma(Connection con,HashMap campos)
	{
		return SqlBaseProgramasActividadesConvenioDao.cargarActividadesPrograma(con,campos);
	}

}
