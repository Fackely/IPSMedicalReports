package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface TiposInventarioDao {

	public abstract HashMap consultarClaseInventario(Connection con, HashMap vo);

	public abstract boolean modificarClaseInventario(Connection con, HashMap vo);

	public abstract boolean insertarClaseInventario(Connection con, HashMap vo);

	public abstract boolean eliminarClaseInventario(Connection con, String codigo, String institucion);
	/////////////////////////////////
	
	public abstract HashMap consultarGruposInventario(Connection con, HashMap vo);
	
	public abstract boolean modificarGruposInventario(Connection con, HashMap vo);
	
	public abstract boolean insertarGruposInventario(Connection con, HashMap vo);
	
	public abstract boolean eliminarGruposInventario(Connection con, String codigo, String institucion, String clase);
		////////////////////////////////
	public abstract HashMap consultarSubGruposInventario(Connection con, HashMap vo);
	
	public abstract boolean modificarSubGruposInventario(Connection con, HashMap vo);
	
	public abstract boolean insertarSubGruposInventario(Connection con, HashMap vo);
	
	public abstract boolean eliminarSubGruposInventario(Connection con, String codigo, String institucion, String subgrupo, String grupo, String clase);

	public abstract boolean eliminarRubro(Connection con, int codigoEliminar, String tabla);
	
}
