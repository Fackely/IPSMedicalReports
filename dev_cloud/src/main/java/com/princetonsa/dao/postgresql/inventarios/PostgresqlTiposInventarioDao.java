package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.TiposInventarioDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseTiposInventarioDao;

public class PostgresqlTiposInventarioDao implements  TiposInventarioDao
{

	public  HashMap consultarClaseInventario(Connection con, HashMap vo)
	{
		return SqlBaseTiposInventarioDao.consultarClaseInventario(con, vo);
	}

	public  boolean modificarClaseInventario(Connection con, HashMap vo)
	{
		return SqlBaseTiposInventarioDao.modificarClaseInventario(con, vo);
	}

	public  boolean insertarClaseInventario(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO inventarios.clase_inventario (codigo,nombre,institucion,cuenta_inventario,cuenta_costo,rubro_presupuestal,codigo_interfaz) values (?,?,?,?,?,?,?)";
		return SqlBaseTiposInventarioDao.insertarClaseInventario(con, vo, cadena);
	}

	public  boolean eliminarClaseInventario(Connection con, String codigo, String institucion)
	{
		return SqlBaseTiposInventarioDao.eliminarClaseInventario(con, codigo, institucion);
	}
	/////////////////////////////////
	
	public  HashMap consultarGruposInventario(Connection con, HashMap vo)
	{
		return SqlBaseTiposInventarioDao.consultarGruposInventario(con, vo);
	}	
	
	public  boolean modificarGruposInventario(Connection con, HashMap vo)
	{
		return SqlBaseTiposInventarioDao.modificarGruposInventario(con, vo);
	}
	
	public  boolean insertarGruposInventario(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO inventarios.grupo_inventario (codigo,clase,nombre,institucion,cuenta_inventario,cuenta_costo,rubro_presupuestal, aplica_cargos_directos) values (?,?,?,?,?,?,?,?)";
		return SqlBaseTiposInventarioDao.insertarGruposInventario(con, vo, cadena);
	}	
	
	public  boolean eliminarGruposInventario(Connection con, String codigo, String institucion, String clase)
	{
		return SqlBaseTiposInventarioDao.eliminarGruposInventario(con, codigo, institucion, clase);
	}	
		////////////////////////////////
	public  HashMap consultarSubGruposInventario(Connection con, HashMap vo)
	{
		return SqlBaseTiposInventarioDao.consultarSubGruposInventario(con, vo);
	}		
	
	public  boolean modificarSubGruposInventario(Connection con, HashMap vo)
	{
		return SqlBaseTiposInventarioDao.modificarSubGruposInventario(con, vo);
	}		
	
	public  boolean insertarSubGruposInventario(Connection con, HashMap vo)
	{
		//String cadena="INSERT INTO inventarios.subgrupo_inventario (subgrupo,grupo,clase,nombre,institucion,cuenta_inventario,cuenta_costo) values (?,?,?,?,?,?,?)";
		//return SqlBaseTiposInventarioDao.insertarSubGruposInventario(con, vo, cadena);
		String cadena="INSERT INTO inventarios.subgrupo_inventario (codigo,subgrupo,grupo,clase,nombre,institucion,cuenta_inventario,cuenta_costo,rubro_presupuestal) values (nextval('SEQ_SUBGRUPO_INVENTARIO'),?,?,?,?,?,?,?,?)";
		return SqlBaseTiposInventarioDao.insertarSubGruposInventario(con, vo, cadena);
		
	}	
	
	public  boolean eliminarSubGruposInventario(Connection con, String codigo, String institucion, String subgrupo, String grupo, String clase)
	{
		return SqlBaseTiposInventarioDao.eliminarSubGruposInventario(con, codigo, institucion, subgrupo, grupo, clase);
	}

	@Override
	public boolean eliminarRubro(Connection con, int codigoEliminar, String tabla) 
	{
		return SqlBaseTiposInventarioDao.eliminarRubro(con, codigoEliminar, tabla);
	}		
	
	
}
