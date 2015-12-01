package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ParametrosEntidadesSubContratadasDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseParametrosEntidadesSubContratadasDao;

public  class PostgresqlParametrosEntidadesSubContratadasDao implements ParametrosEntidadesSubContratadasDao
{
	/**
	 * Metodo encargado de consultar los datos
	 * del la tabla det_param_entid_subcontratada
	 * @param
	 * HashMap parametros
	 * ----------------------------------------------
	 * 			KEY'S DEL HASHMAP PARAMETROS
	 * ----------------------------------------------
	 *  -- codigoParam1_ --> Requerido
	 *  -- convenio2_ --> Opcional
	 *  -- viaIngreso4_ --> Opcional
	 *  -- institucion6_ --> Requerido
	 *  @return 
	 *  HashMap mapa 
	 *  -----------------------------------------------
	 *  			KEY'S DEL HASHMAP MAPA
	 *  -----------------------------------------------
	 *  -- codigo0_
	 *  -- codigoParam1_
	 *  -- convenio2_
	 *  -- valor3_
	 *  -- viaIngreso4_
	 *  -- nombre5_
	 *  -- institucion6_
	 *  -- usuarioModifica7_
	 */
	public HashMap consultarParametros (Connection connection, HashMap parametros)
	{
		return SqlBaseParametrosEntidadesSubContratadasDao.consultarParametros(connection, parametros);
	}
	
	/**
	 * Metodo encargado de guardar los datos en la tabla 
	 * det_param_entid_subcontratada
	 * @param connection
	 * @param datos
	 * ---------------------------------------------
	 * 			KEY'S DEL HASHMAP DATOS
	 * ---------------------------------------------
	 * -- codigoParam1_
	 * -- convenio2_
	 * -- valor3_
	 * -- usuarioModifica7_
	 * -- viaIngreso4_
	 * -- institucion6_
	 * -- nombre5_
	 @return boolean 
	 */
	public boolean guardarParametros (Connection connection, HashMap datos)
	{
		return SqlBaseParametrosEntidadesSubContratadasDao.guardarParametros(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de modificar los datos en la tabla 
	 * det_param_entid_subcontratada, puediendo filtrarlos
	 * por convenio, via de ingreso, institucion, nombre, seccion
	 * @param connection
	 * @param datos
	 * ---------------------------------------------
	 * 			KEY'S DEL HASHMAP DATOS
	 * ---------------------------------------------
	 * -- codigoParam1_ --> Requerido
	 * -- convenio2_ --> Opcional
	 * -- valor3_ --> --> Opcional
	 * -- usuarioModifica7_ -->Requerido
	 * -- viaIngreso4_ --> Opcional
	 * -- institucion6_ -->Requerido
	 * -- nombre5_ -->Requerido
	 @return boolean 
	 */
	public boolean modificarParametros (Connection connection, HashMap datos)
	{
		return SqlBaseParametrosEntidadesSubContratadasDao.modificarParametros(connection, datos);
	}
}