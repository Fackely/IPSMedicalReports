package com.princetonsa.dao.postgresql.consultaExterna;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.consultaExterna.MotivosCitaDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMotivosCitaDao;
import com.princetonsa.dto.consultaExterna.DtoMotivosCita;


public class PostgresqlMotivosCitaDao implements MotivosCitaDao
{
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(PostgresqlMotivosCitaDao.class);
	
	public ArrayList<DtoMotivosCita> consultarMotivosCita()
	{
		return SqlBaseMotivosCitaDao.consultarMotivosCita();
	}
	
	public int insertarMotivoCita(String codigo, String descripcion, String activo, int institucion, String usuario, String tipoMotivo)
	{
		return SqlBaseMotivosCitaDao.insertarMotivoCita(codigo, descripcion, activo, institucion, usuario, tipoMotivo);
	}
	
	public boolean modificarMotivoCita(String codigo, String descripcion,String activo, int codigoPk, String usuario, String tipoMotivo)
	{
		return SqlBaseMotivosCitaDao.modificarMotivoCita(codigo, descripcion, activo, codigoPk,usuario, tipoMotivo);
	}
	
	public boolean insertarLogMotivoCita(String codigo, String descripcion,String activo, String eliminado, String usuario, int motivoCita, String tipoMotivo)
	{
		return SqlBaseMotivosCitaDao.insertarLogMotivoCita(codigo, descripcion,activo, eliminado, usuario, motivoCita, tipoMotivo);
	}
	
	public boolean eliminarMotivoCita(int codigoPk)
	{
		return SqlBaseMotivosCitaDao.eliminarMotivoCita(codigoPk);
	}
}