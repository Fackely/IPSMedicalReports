package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.UbicacionGeograficaDao;
import com.princetonsa.dao.sqlbase.SqlBaseUbicacionGeograficaDao;

public class PostgresqlUbicacionGeograficaDao implements UbicacionGeograficaDao {
	
	
	
//////////////////////////////INICIO CONSULTAR//////////////////////////////////

	public HashMap consultarBarrio(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.consultarBarrio(con, vo);
	}

	public HashMap consultarCiudad(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.consultarCiudad(con, vo);
	}

	public HashMap consultarDepartamento(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.consultarDepartamento(con, vo);
	}

	public HashMap consultarLocalidad(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.consultarLocalidad(con, vo);
	}

	public HashMap consultarPais(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.consultarPais(con, vo);
	}
	
//////////////////////////////FIN CONSULTAR//////////////////////////////////
	
	
	
	
	
//////////////////////////////INICIO ELIMINACION////////////////////////////////////

	public boolean eliminarBarrio(Connection con, int codigo) {
		
		return SqlBaseUbicacionGeograficaDao.eliminarBarrrio(con, codigo);
	}

	public boolean eliminarCiudad(Connection con, String codigo_ciudad, String codigo_departamento, String codigo_pais) {
		
		return SqlBaseUbicacionGeograficaDao.eliminarCiudad(con, codigo_ciudad,codigo_departamento,codigo_pais);
	}

	public boolean eliminarDepartamento(Connection con, String codigo_departamento,String codigo_pais) {
		
		return SqlBaseUbicacionGeograficaDao.eliminarDepartamento(con, codigo_departamento,codigo_pais);
	}

	public boolean eliminarLocalidad(Connection con, String codigo_localidad, String codigo_ciudad, String codigo_departamento, String codigo_pais) {
		
		return SqlBaseUbicacionGeograficaDao.eliminarLocalidad(con, codigo_localidad, codigo_ciudad,codigo_departamento,codigo_pais);
	}

	public boolean eliminarPais(Connection con, String codigo) {
		
		return SqlBaseUbicacionGeograficaDao.eliminarPais(con, codigo);
	}
	
//////////////////////////////FIN ELIMINACION////////////////////////////////////
	
	
	
	
	
//////////////////////////////INICIO INSERTAR//////////////////////////////////

	public boolean insertarBarrio(Connection con, HashMap vo) {
		
		String cadenaInsertarBarrio="INSERT INTO barrios (codigo_departamento, codigo_ciudad, codigo_barrio, descripcion, codigo_pais, codigo_localidad, usuario_modifica, fecha_modifica, hora_modifica, codigo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NEXTVAL('seq_barrios'))";
		return SqlBaseUbicacionGeograficaDao.insertarBarrio(con, vo, cadenaInsertarBarrio);
	}

	public boolean insertarCiudad(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.insertarCiudad(con, vo);
	}

	public boolean insertarDepartamento(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.insertarDepartamento(con, vo);
	}

	public boolean insertarLocalidad(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.insertarLocalidad(con, vo);
	}

	public boolean insertarPais(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.insertarPais(con, vo);
	}
	
//////////////////////////////FIN INSERTAR//////////////////////////////////
	
	
	
	
	
//////////////////////////////INICIO MODIFICAR////////////////////////////////////

	public boolean modificarBarrio(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.modificarBarrio(con, vo);
	}

	public boolean modificarCiudad(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.modificarCiudad(con, vo);
	}

	public boolean modificarDepartamento(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.modificarDepartamento(con, vo);
	}

	public boolean modificarLocalidad(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.modificarLocalidad(con, vo);
	}

	public boolean modificarPais(Connection con, HashMap vo) {
		
		return SqlBaseUbicacionGeograficaDao.modificarPais(con, vo);
	}
	
//////////////////////////////FIN MODIFICAR////////////////////////////////////

}
