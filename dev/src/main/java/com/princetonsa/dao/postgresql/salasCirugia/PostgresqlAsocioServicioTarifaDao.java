package com.princetonsa.dao.postgresql.salasCirugia;

import java.sql.Connection;
import java.util.HashMap; 

import com.princetonsa.dao.salasCirugia.AsocioServicioTarifaDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseAsocioServicioTarifaDao;

public class PostgresqlAsocioServicioTarifaDao implements AsocioServicioTarifaDao
{
	
	/**
	 * Cadena de inserccion de Encabezado Asocios Servicio Tarifa
	 * */
	private static final String strInsertarEncabAsociosServ = "INSERT INTO asocios_servicios_tarifa(codigo,institucion,esq_tar_particular,esq_tar_general,convenio,fecha_inicial,fecha_final,usuario_modifica,fecha_modifica,hora_modifica) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?)"; 
	
	/**
	 * Cadena de inserccion de Detalle Asocios Servicio Tarifa
	 * */
	private static final String strInsertarDetAsociosServ = "INSERT INTO det_aso_servicios_tarifa(codigo,codigo_aso_serv_tarifa,tipo_servicio,especialidad,grupo_servicio,servicio,asocio,usuario_modifica,fecha_modifica,hora_modifica,liquidar_por) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	
	
	/**
	 * Método para cargar el listado de los asocios servicios tarifa
	 * parametrizadas por institución
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public HashMap cargarEncabAsociosServ(Connection con,HashMap parametros)
	{
		return SqlBaseAsocioServicioTarifaDao.cargarEncabAsociosServ(con, parametros);
	}
	
	/**
	 * Método usado para modificar un registros de encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public boolean actualizarEncaAsociosSer(
			Connection con,
			HashMap parametros)
	{
		return SqlBaseAsocioServicioTarifaDao.actualizarEncaAsociosSer(con, parametros);		
	}
	
	/**
	 * Método para eliminar un porcentaje de Asocios Servicios de Tarifa
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarEncaAsociosServ(Connection con,int codigo)
	{
		return SqlBaseAsocioServicioTarifaDao.eliminarEncaAsociosServ(con, codigo);		
	}
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Asocios Servicios de Tarifa
	 * @param con
	 * @param HashMap parametros
	 * @param insertarPorcentajeStr
	 */
	public int insertarEncaAsociosServ(
			   Connection con,
			   HashMap parametros)
	{
		return SqlBaseAsocioServicioTarifaDao.insertarEncaAsociosServ(con, parametros, strInsertarEncabAsociosServ);
	}
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarDetAsociosServicios(Connection con, HashMap parametros)
	{
		return SqlBaseAsocioServicioTarifaDao.insertarDetAsociosServicios(con, parametros, strInsertarDetAsociosServ );
	}
	
	/**
	 * Modificar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public  boolean modificarDetAsociosServ(Connection con, HashMap parametros)
	{
		return SqlBaseAsocioServicioTarifaDao.modificarDetAsociosServ(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public  boolean EliminarDetAsociosServ(Connection con, HashMap parametros)
	{
		return SqlBaseAsocioServicioTarifaDao.EliminarDetAsociosServ(con, parametros);	
	}
	
	/**
	 * Consultar informacion tabla Detalle Asocios Servicios de Tarifa
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public  HashMap consultarDetAsociosServ(Connection con, HashMap parametros)
	{
		return SqlBaseAsocioServicioTarifaDao.consultarDetAsociosServ(con, parametros);
	}
}