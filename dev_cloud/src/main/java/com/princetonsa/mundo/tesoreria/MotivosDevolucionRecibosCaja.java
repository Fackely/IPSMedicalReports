package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MotivosDevolucionRecibosCajaDao;

public class MotivosDevolucionRecibosCaja
{
	private static Logger logger = Logger.getLogger(MotivosDevolucionRecibosCaja.class);
	
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
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	
	
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

	public String getCodigoAux() {
		return codigoAux;
	}

	public void setCodigoAux(String codigoAux) {
		this.codigoAux = codigoAux;
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
	private static MotivosDevolucionRecibosCajaDao getMotivosDevolucionRecibosCajaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDevolucionRecibosCajaDao();		
	}
	
	/**
	 * Metodo de consulta de los motivos de devoluciones de recibos de caja
	 * @param con
	 * @param codigoIns
	 * @return
	 */	
	public static HashMap<String, Object> consultaMotivosD (Connection con, int codigoInstitucion)
	{
		return getMotivosDevolucionRecibosCajaDao().consultaMotivosD(con, codigoInstitucion);
	}
	
	public static boolean insertar(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDevolucionRecibosCajaDao().insertarMotivosD(con, motivosDevolucionRecibosCaja);
	}
	
	public static boolean modificar(Connection con, MotivosDevolucionRecibosCaja motivosDevolucionRecibosCaja){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDevolucionRecibosCajaDao().modificarMotivosD(con, motivosDevolucionRecibosCaja);
	}
	
	public static boolean eliminar(Connection con, String motivoD){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosDevolucionRecibosCajaDao().eliminarMotivosD(con, motivoD);
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
}