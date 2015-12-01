/*
 * @(#)RadicaionCuentasCobroCapitacion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.RadicacionCuentasCobroCapitacionDao;

/**
 * Clase para el manejo de radicacion cuentas cobro capitacion
 * @version 1.0, Julio 04, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class RadicacionCuentasCobroCapitacion 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RadicacionCuentasCobroCapitacionDao radicacionDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RadicacionCuentasCobroCapitacion.class);

	/**
	 * fecha radicacion 
	 */
	private String fechaRadicacionFormatApp;
	
	/**
	 * numero radicacion
	 */
	private String numeroRadicacion;
	
	/**
	 * observaciones
	 */
	private String observaciones;
	
	/**
	 * numero Cuenta cobro
	 */
	private String numeroCuentaCobro;
	
	/**
	 * codigoInstitucion
	 */
	private int codigoInstitucion;
	
	/**
	 * usuario que radica
	 */
	private String loginUsuario;
	
	/**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
		this.fechaRadicacionFormatApp="";
		this.numeroRadicacion="";
		this.observaciones="";
		this.numeroCuentaCobro="";
		this.codigoInstitucion=0;
		this.loginUsuario="";
	}
	
	/**
	 * Constructor vacio
	 *
	 */
	public RadicacionCuentasCobroCapitacion()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
		if (myFactory != null)
		{
			radicacionDao = myFactory.getRadicacionCuentasCobroCapitacionDao();
			wasInited = (radicacionDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Busqueda de las cuentas cobro a radicar
	 * @param con
	 * @param criteriosBusquedaMap ( keys= cuentaCobro, codigoConvenio, fechaInicial, fechaFinal), 
	 * 								las fechas deben estar en formato aplicacion
	 * @return
	 */
	public Collection busquedaCuentasCobroARadicar(Connection con, HashMap criteriosBusquedaMap)
	{
		Collection coleccion=null;
		try
		{
			coleccion=radicacionDao.busquedaCuentasCobroARadicar(con, criteriosBusquedaMap);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo convenio " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * metodo que inserta la radicacion de cuentas cobro capitacion
	 * (LLENAR LOS ATRIBUTOS DEL OBJETO fechaRadicacionFormatApp, 
	 * 	numeroRadicacion, loginUsuario, observaciones, numeroCuentaCobro, institucion)
	 * @return
	 */
	public boolean insertarRadicacionCxC (	Connection con )
	{
		return radicacionDao.insertarRadicacionCxC(con, this.fechaRadicacionFormatApp, this.numeroRadicacion, this.loginUsuario, this.observaciones, this.numeroCuentaCobro, this.codigoInstitucion);
	}

	/**
	 * @return Returns the codigoInstitucion.
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion The codigoInstitucion to set.
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return Returns the fechaRadicacionFormatApp.
	 */
	public String getFechaRadicacionFormatApp() {
		return fechaRadicacionFormatApp;
	}

	/**
	 * @param fechaRadicacionFormatApp The fechaRadicacionFormatApp to set.
	 */
	public void setFechaRadicacionFormatApp(String fechaRadicacionFormatApp) {
		this.fechaRadicacionFormatApp = fechaRadicacionFormatApp;
	}

	/**
	 * @return Returns the loginUsuario.
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario The loginUsuario to set.
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return Returns the numeroCuentaCobro.
	 */
	public String getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}

	/**
	 * @param numeroCuentaCobro The numeroCuentaCobro to set.
	 */
	public void setNumeroCuentaCobro(String numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}

	/**
	 * @return Returns the numeroRadicacion.
	 */
	public String getNumeroRadicacion() {
		return numeroRadicacion;
	}

	/**
	 * @param numeroRadicacion The numeroRadicacion to set.
	 */
	public void setNumeroRadicacion(String numeroRadicacion) {
		this.numeroRadicacion = numeroRadicacion;
	}

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
}
