package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.TiposTratamientosOdontologicosDao;
import com.princetonsa.mundo.inventarios.SustitutosNoPos;

public class TiposTratamientosOdontologicos
{	
	private static Logger logger = Logger.getLogger(TiposTratamientosOdontologicos.class);
	
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * 
	 */
	private String codigoAux;
	
	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	private String activo="1";
	
	/**
	 *	 
	 */
	private String institucion;
	
	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @return
	 */
	private static TiposTratamientosOdontologicosDao getTiposTratamientosOdontologicosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposTratamientosOdontologicosDao();		
	}
	
	/**
	 * Metodo de consulta de los tipos de tratamiento odontologicos
	 * @param con
	 * @param codigoArtPpal
	 * @return
	 */	
	public static HashMap<String, Object> consultaTiposT (Connection con, int codigoInstitucion)
	{
		return getTiposTratamientosOdontologicosDao().consultaTiposT(con, codigoInstitucion);
	}
	
	/**
	 * 
	 */
	public static boolean eliminar(Connection con, String tipoT)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposTratamientosOdontologicosDao().eliminarTipoT(con, tipoT);
	}
	
	public static boolean insertar(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposTratamientosOdontologicosDao().insertarTiposT(con, tiposTratamientosOdontologicos);
	}
	
	
	public static boolean modificar(Connection con, TiposTratamientosOdontologicos tiposTratamientosOdontologicos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposTratamientosOdontologicosDao().modificarTiposT(con, tiposTratamientosOdontologicos);
	}

	public String getCodigoAux() {
		return codigoAux;
	}

	public void setCodigoAux(String codigoAux) {
		this.codigoAux = codigoAux;
	}
}