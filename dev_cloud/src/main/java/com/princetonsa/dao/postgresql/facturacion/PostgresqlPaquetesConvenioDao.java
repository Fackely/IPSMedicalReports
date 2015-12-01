package com.princetonsa.dao.postgresql.facturacion;


import java.sql.Connection;
import java.util.HashMap;


import com.princetonsa.dao.sqlbase.facturacion.SqlBasePaquetesConvenioDao;
import com.princetonsa.dao.facturacion.PaquetesConvenioDao;


public class PostgresqlPaquetesConvenioDao implements PaquetesConvenioDao
{
	
	/**
	 * @param con
	 * @param institucion
	 */
	
	public HashMap consultarPaquetesConvenioExistentes(Connection con, HashMap vo) 
	{
		return SqlBasePaquetesConvenioDao.consultarPaquetesConvenioExistentes(con,vo);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertar(Connection con, HashMap vo)
	{
		String cadenaInsertarStr="INSERT INTO paquetes_convenio (codigo,institucion,convenio,contrato, paquete, via_ingreso, tipo_paciente, fecha_inicial_venc, fecha_final_venc, usuario_modifica, fecha_modifica, hora_modifica) VALUES (NEXTVAL('seq_paq_convenios'),?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

		return SqlBasePaquetesConvenioDao.insertar(con,cadenaInsertarStr, vo);
	}
	
	
	/**
	 * 
	 */
	
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBasePaquetesConvenioDao.modificar(con, vo);
	}
	
	
	/**
	 * 
	 */
	
	public boolean eliminarRegistro(Connection con, String codigo)
	{
		return SqlBasePaquetesConvenioDao.eliminarRegistro(con,codigo);
	}


	/**
	 * 
	 */
	public HashMap consultarPaquetesConvenio(Connection con, HashMap vo) 
	{
		return SqlBasePaquetesConvenioDao.consultarPaquetesConvenio(con,vo);
	}
}
