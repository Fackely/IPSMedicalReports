package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.TiposInventarioDao;

public class TiposInventario {

	private TiposInventarioDao objetoDao;
	
	private HashMap mapaClaseInventarioTemp;
	private HashMap mapaGrupoInventarioTemp;
	private HashMap mapaSubGrupoInventarioTemp;
	

	public TiposInventario()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));		
	}
	
	private void init(String tipoBD)
	{
		if (objetoDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao = myFactory.getTiposInventarioDao();
		}	
	}

	/**
	 * 
	 *
	 */
	private void reset()
	{
		this.mapaClaseInventarioTemp=new HashMap();
		this.mapaGrupoInventarioTemp=new HashMap();
		this.mapaSubGrupoInventarioTemp=new HashMap();
		
		
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean insertarClaseInventario(Connection con,HashMap vo)
	{
		return objetoDao.insertarClaseInventario(con, vo);
	}
	
	public boolean modificarClaseInventario(Connection con,HashMap vo)
	{
		return objetoDao.modificarClaseInventario(con, vo);
	}
	
	public HashMap consultarClaseInventario(Connection con,HashMap vo)
	{
		this.setMapaClaseInventarioTemp(objetoDao.consultarClaseInventario(con, vo));
		return (HashMap)this.mapaClaseInventarioTemp.clone();
	}

	public boolean eliminarClaseInventario(Connection con, String codigo, String institucion)
	{
		return objetoDao.eliminarClaseInventario(con, codigo, institucion);
	}

	public HashMap consultarClaseInventarioEspecifico(Connection con, String codigo, String institucion)
	{
		HashMap vo=new HashMap();
		vo.put("codigo", codigo);
		vo.put("institucion", institucion);
		return objetoDao.consultarClaseInventario(con, vo);
	}
	/////////////////
	public boolean insertarGrupoInventario(Connection con,HashMap vo)
	{
		return objetoDao.insertarGruposInventario(con, vo);
	}
	
	public boolean modificarGrupoInventario(Connection con,HashMap vo)
	{
		return objetoDao.modificarGruposInventario(con, vo);
	}
	
	public HashMap consultarGrupoInventario(Connection con,HashMap vo)
	{
		this.setMapaGrupoInventarioTemp(objetoDao.consultarGruposInventario(con, vo));
		return (HashMap)this.getMapaGrupoInventarioTemp().clone();
	}

	public boolean eliminarGrupoInventario(Connection con, String codigo, String institucion, String clase)
	{
		return objetoDao.eliminarGruposInventario(con, codigo, institucion, clase);
	}

	public HashMap consultarGrupoInventarioEspecifico(Connection con, String consecutivo)
	{
		HashMap vo=new HashMap();
		vo.put("consecutivo", consecutivo);
		return objetoDao.consultarGruposInventario(con, vo);
	}
	
	/////////////////
	public boolean insertarSubGrupoInventario(Connection con,HashMap vo)
	{
		return objetoDao.insertarSubGruposInventario(con, vo);
	}
	
	public boolean modificarSubGrupoInventario(Connection con,HashMap vo)
	{
		return objetoDao.modificarSubGruposInventario(con, vo);
	}
	
	public HashMap consultarSubGrupoInventario(Connection con,HashMap vo)
	{
		this.setMapaSubGrupoInventarioTemp(objetoDao.consultarSubGruposInventario(con, vo));
		return (HashMap)this.mapaSubGrupoInventarioTemp.clone();
	}

	public boolean eliminarSubGrupoInventario(Connection con, String codigo, String institucion, String subgrupo, String grupo ,String clase)
	{
		return objetoDao.eliminarSubGruposInventario(con, codigo, institucion, subgrupo, grupo, clase);
	}

	public HashMap consultarSubGrupoInventarioEspecifico(Connection con, String consecutivo)
	{
		HashMap vo=new HashMap();
		vo.put("consecutivo", consecutivo);
		return objetoDao.consultarSubGruposInventario(con, vo);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public HashMap getMapaClaseInventarioTemp() {
		return mapaClaseInventarioTemp;
	}

	public void setMapaClaseInventarioTemp(HashMap mapaClaseInventarioTemp) {
		this.mapaClaseInventarioTemp = mapaClaseInventarioTemp;
	}

	public HashMap getMapaGrupoInventarioTemp() {
		return mapaGrupoInventarioTemp;
	}

	public void setMapaGrupoInventarioTemp(HashMap mapaGrupoInventarioTemp) {
		this.mapaGrupoInventarioTemp = mapaGrupoInventarioTemp;
	}

	public HashMap getMapaSubGrupoInventarioTemp() {
		return mapaSubGrupoInventarioTemp;
	}

	public void setMapaSubGrupoInventarioTemp(HashMap mapaSubGrupoInventarioTemp) {
		this.mapaSubGrupoInventarioTemp = mapaSubGrupoInventarioTemp;
	}

	public boolean eliminarRubro(int codigoEliminar, String tabla) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean resultado=objetoDao.eliminarRubro(con, codigoEliminar, tabla);
		UtilidadBD.closeConnection(con);
		return resultado;
		
	}

	
}
