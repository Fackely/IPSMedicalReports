package com.princetonsa.dao.postgresql.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.salasCirugia.AsociosXUvrDao;
import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseAsociosXUvrDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class PostgresqlAsociosXUvrDao implements AsociosXUvrDao {

	public boolean actualizarDetCodigosAsociosXUvr(Connection con,HashMap<String, Object> vo) 
	{
		return SqlBaseAsociosXUvrDao.actualizarDetCodigosAsociosXUvr(con, vo);
	}

	public HashMap consultarAsociosXUvrTipoSala(Connection con,	int esquemaTarifario, int convenio,int codigoAsocioXUvr) 
	{
		return SqlBaseAsociosXUvrDao.consultarAsociosXUvrTipoSala(con, esquemaTarifario, convenio, codigoAsocioXUvr);
	}

	public HashMap consultarDetalleAsociosXUvr(Connection con,int codigoAsocioUvr, int tipoAsocio, int esquemaTarifario,int convenio,String tipoServicio,int tipoAnestesia,String ocupacion,String especialidad,String tipoEspecialista,int tipoLiquidacion) 
	{
		return SqlBaseAsociosXUvrDao.consultarDetalleAsociosXUvr(con, codigoAsocioUvr, tipoAsocio, esquemaTarifario, convenio,tipoServicio,tipoAnestesia,ocupacion,especialidad,tipoEspecialista,tipoLiquidacion);
	}

	public HashMap consultarVigenciasXConvenio(Connection con, int convenio) 
	{
		return SqlBaseAsociosXUvrDao.consultarVigenciasXConvenio(con, convenio);
	}

	public boolean insertarAsociosUvrMaestro(Connection con, HashMap vo,boolean guardarXTipoAsocio) 
	{
		return SqlBaseAsociosXUvrDao.insertarAsociosUvrMaestro(con, vo,guardarXTipoAsocio);
	}

	public int insertarDetalleAsocioXUvr(Connection con, HashMap vo) 
	{
		return SqlBaseAsociosXUvrDao.insertarDetalleAsocioXUvr(con, vo);
	}
	
	public boolean insertarAsociosXUvrXTipoAsocio(Connection con,HashMap vo,int codigoAsocioUvr)
	{
		return SqlBaseAsociosXUvrDao.insertarAsociosXUvrXTipoAsocio(con, vo, codigoAsocioUvr);
	}
	
	/**
	 * Metodo para la actualizacion de los detalles de asocios por uvr
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarDetalle(Connection con,HashMap vo)
	{
		return SqlBaseAsociosXUvrDao.modificarDetalle(con, vo);
	}
	
	/**
	 * Metodo para la eliminacion del detalle de asocios por uvr
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarDetalle(Connection con,int codigo)
	{
		return SqlBaseAsociosXUvrDao.eliminarDetalle(con, codigo);
	}
	
	/**
	 * Metodo que elimina un registro del segundo maestro que es asocio por uvr por tipo de asocio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAsocioXUvrTipoAsocio(Connection con,int codigo)
	{
		return SqlBaseAsociosXUvrDao.eliminarAsocioXUvrTipoAsocio(con, codigo);
	}

	/**
	 * Metodo que elimina un asocio por uvr cuando este viene por convenio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAsocioUvrXConvenio(Connection con,int codigo)
	{
		return SqlBaseAsociosXUvrDao.eliminarAsocioUvrXConvenio(con, codigo);
	}
	
	/**
	 * Metodo que modifica las vigencias por convenio
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarVigencias(Connection con,HashMap vo)
	{
		return SqlBaseAsociosXUvrDao.modificarVigencias(con, vo); 
	}
	
	/**
	 * Metodo encargado de mopdificar asocios por sala
	 * @param connection
	 * @param tipoAsocio
	 * @param tipoSala
	 * @param codigo
	 * @return
	 */
	public boolean modificarAsociosUvrSala (Connection connection,String tipoAsocio, String tipoSala, String codigo,String liquidarPor)
	{
		return SqlBaseAsociosXUvrDao.modificarAsociosUvrSala(connection, tipoAsocio, tipoSala, codigo,liquidarPor);
	}
}
