/*
 * @(#)Arqueos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.tesoreria.ArqueosDao;

/**
 * Clase para el manejo de arqueos
 * @version 1.0, Abril 25, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class Arqueos 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ArqueosDao arqueosDao;
	
	/**
     * fecha de arqueo - fecha cierre 
     */
    private String fechaArqueadaDDMMYYYY; 
    
    /**
     * login del usuario/cajero al cual se le va 
     * ha generar el arqueo - cierre 
     */
    private String loginUsuarioCajero;
    
    /**
     * nombres del usuario cajero;
     */
    private String nombresUsuarioCajero;
    
    /**
     * consecutivo (PK) de la caja
     */
    private String consecutivoCaja;
	
    /**
     * fecha de generacion del arqueo
     */
    private String fechaGeneracionArqueoDDMMYYYY;
    
    /**
     * hora de generacion del arqueo
     */
	private String horaGeneracionArqueo;
    
	/**
     * login del usuario genera el cual va 
     * ha generar el arqueo - cierre 
     */
    private String loginUsuarioGenera;
    
    /**
     * nombres del usuario cajero;
     */
    private String nombresUsuarioGenera;
	
    /**
     * consecutivo (PK) de la caja
     */
    private String consecutivoCajaPpal;
	
    /**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
		this.fechaArqueadaDDMMYYYY="";
		this.loginUsuarioCajero="";
		this.consecutivoCaja="";
		this.fechaGeneracionArqueoDDMMYYYY="";
		this.horaGeneracionArqueo="";
		this.nombresUsuarioCajero="";
		this.loginUsuarioGenera="";
		this.nombresUsuarioGenera="";
		this.consecutivoCajaPpal="";
	}
	
	/**
	 * constructor de la clase
	 *
	 */
	public Arqueos()
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
			arqueosDao = myFactory.getArqueosDao();
			wasInited = (arqueosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * inserta el arqueo definitivo
	 * @param con
	 * @param consecutivoArqueoDefinitivo
	 * @param codigoInstitucion
	 * @param loginUsurioGenera
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarArqueoDefinitivo(Connection con, String consecutivoArqueoDefinitivo, int codigoInstitucion, String loginUsurioGenera ) throws SQLException 
	{
		return arqueosDao.insertarArqueoDefinitivo(	con, 
													consecutivoArqueoDefinitivo, 
													codigoInstitucion, 
													loginUsurioGenera, 
													this.fechaGeneracionArqueoDDMMYYYY, 
													this.horaGeneracionArqueo, 
													this.consecutivoCaja, 
													this.loginUsuarioCajero,
													this.fechaArqueadaDDMMYYYY);
	}

	 /**
     * Insercion del cierre caja
     * @param con
     * @param consecutivoCierreCaja
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @param fechaCierreDDMMYYYY
     * @param horaCierre
     * @param consecutivoCaja
     * @param loginUsuarioCajero
     * @param fechaCerradaDDMMYYYY
     * @return
     */
    public boolean  insertarCierreCaja( Connection con,
    									String consecutivoCierreCaja,
                                        int codigoInstitucion,
                                        String loginUsuarioGenera
                                       )
    {
    	return arqueosDao.insertarCierreCaja(	con, 
												consecutivoCierreCaja, 
												codigoInstitucion, 
												loginUsuarioGenera, 
												this.fechaGeneracionArqueoDDMMYYYY, 
												this.horaGeneracionArqueo, 
												this.consecutivoCaja, 
												this.loginUsuarioCajero,
												this.fechaArqueadaDDMMYYYY,
												this.consecutivoCajaPpal);
    }
	
	/**
     * actualiza el campo arqueo definitivo de la tabla recibos caja
     * @param con
     * @param consecutivoArqueoDefinitivo
     * @param numeroReciboCaja
     * @param codigoInstitucion
     * @return
     */
    public boolean  actualizarRecibosCajaCampoArqueoDefinitivo(  	Connection con,
		                                               				String consecutivoArqueoDefinitivo,
		                                               				String numeroReciboCaja,
		                                               				int codigoInstitucion
		                                                   		)
    {
    	return arqueosDao.actualizarRecibosCajaCampoArqueoDefinitivo(con, consecutivoArqueoDefinitivo, numeroReciboCaja, codigoInstitucion);
    }
    
    /**
     * actualiza el campo cierre caja de la tabla recibos caja
     * @param con
     * @param consecutivoCierreCaja
     * @param numeroReciboCaja
     * @param codigoInstitucion
     * @return
     */
    public boolean  actualizarRecibosCajaCampoCierreCaja(  	Connection con,
                                               				String consecutivoCierreCaja,
                                               				String numeroReciboCaja,
                                               				int codigoInstitucion
                                               			)
    {
    	return arqueosDao.actualizarRecibosCajaCampoCierreCaja(con, consecutivoCierreCaja, numeroReciboCaja, codigoInstitucion);
    }
	
    /**
	 * medodo para consultar los recibos caja que no cumplen con el arqueo definitivo para un
     * cajero - caja y fecha dada, retorna String con los numero Recibos caja separados por comas, 
	 * o "" si todos ya tienen el arqueo definitivo o "ERROR" en caso de exception
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaArqueoDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @return String con los numero Recibos caja separados por comas, 
	 * 			o "" si todos ya tienen el arqueo definitivo
	 * 			o "ERROR" en caso de exception
	 */
	public String estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha(	Connection con,
																			int codigoInstitucion,
																			String fechaArqueoDDMMYYYY,
																			String loginUsuarioCajero,
																			String consecutivoCaja
																		)
	{
		return arqueosDao.estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha(con, codigoInstitucion, fechaArqueoDDMMYYYY, loginUsuarioCajero, consecutivoCaja);
	}
	
	/**
	 * metodo que evalua la existencia del cierre caja 
     * para un cajero - caja - fecha determinados
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaArqueoDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean existeCierreCaja(	Connection con,
											int codigoInstitucion,
											String fechaArqueoDDMMYYYY,
											String loginUsuarioCajero,
											String consecutivoCaja
										)
	{
		//@todo arreglo para suba, nunca debe validar esto
		return false;
		//return arqueosDao.existeCierreCaja(con, codigoInstitucion, fechaArqueoDDMMYYYY, loginUsuarioCajero, consecutivoCaja);
	}
	
	/**
	 * metodo que realiza la busqueda de los arqueos y de los cierres caja
	 * @param con
	 * @param fechaInicialConsultaArqueosCierres
	 * @param fechaFinalConsultaArqueosCierres
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @param codigoTipoArqueoStr
	 * @param codigoInstitucion
	 * @return
	 */
	public Collection busquedaArqueosCierres(	Connection con,
												String fechaInicialConsultaArqueosCierres,
												String fechaFinalConsultaArqueosCierres,
												String loginUsuarioCajero,
												String consecutivoCaja,
												String codigoTipoArqueoStr,
												int codigoInstitucion,
												int codigoCentroAtencion,
												String consecutivoCajaPpal)
	{
		return arqueosDao.busquedaArqueosCierres(con, fechaInicialConsultaArqueosCierres, fechaFinalConsultaArqueosCierres, loginUsuarioCajero, consecutivoCaja, codigoTipoArqueoStr, codigoInstitucion, codigoCentroAtencion, consecutivoCajaPpal);
	}
	
	
	/**
	 * Metodo que consulta el resumen de un cierre de caja
	 * @param con
	 * @param codigoInstitucion
	 * @param consecutivo
	 * @return
	 */
	public boolean resumenCierreCaja(Connection con, int codigoInstitucion, String consecutivo)
	{
		ResultSetDecorator rs= arqueosDao.resumenCierreCaja(con, codigoInstitucion, consecutivo);
		if(rs!=null)
		{
			try 
			{
				if(rs.next())
				{
					this.setLoginUsuarioGenera(rs.getString("loginusuariogenera"));
					this.setNombresUsuarioGenera(rs.getString("usuariogenera"));
					this.setFechaGeneracionArqueoDDMMYYYY(rs.getString("fechacierre"));
					this.setHoraGeneracionArqueo(rs.getString("horacierre"));
					this.setConsecutivoCaja(rs.getString("consecutivocaja"));
					this.setLoginUsuarioCajero(rs.getString("logincajero"));
					this.setNombresUsuarioCajero(rs.getString("cajero"));
					this.setFechaArqueadaDDMMYYYY(rs.getString("fechacerrada"));
					this.setConsecutivoCajaPpal(rs.getString("consecutivocajappal"));
					return true;
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	
	/**
	 * Metodo que consulta el resumen de un arqueo definitivo
	 * @param con
	 * @param codigoInstitucion
	 * @param consecutivo
	 * @return
	 */
	public boolean resumenArqueoDefinitivo(Connection con, int codigoInstitucion, String consecutivo)
	{
		ResultSetDecorator rs= arqueosDao.resumenArqueoDefinitivo(con, codigoInstitucion, consecutivo);
		if(rs!=null)
		{
			try 
			{
				if(rs.next())
				{
					this.setLoginUsuarioGenera(rs.getString("loginusuariogenera"));
					this.setNombresUsuarioGenera(rs.getString("usuariogenera"));
					this.setFechaGeneracionArqueoDDMMYYYY(rs.getString("fechaarqueo"));
					this.setHoraGeneracionArqueo(rs.getString("horaarqueo"));
					this.setConsecutivoCaja(rs.getString("consecutivocaja"));
					this.setLoginUsuarioCajero(rs.getString("logincajero"));
					this.setNombresUsuarioCajero(rs.getString("cajero"));
					this.setFechaArqueadaDDMMYYYY(rs.getString("fechaarqueada"));
					return true;
				}
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	
	
	/**
	 * @return Returns the consecutivoCaja.
	 */
	public String getConsecutivoCaja() {
		return consecutivoCaja;
	}

	/**
	 * @param consecutivoCaja The consecutivoCaja to set.
	 */
	public void setConsecutivoCaja(String consecutivoCaja) {
		this.consecutivoCaja = consecutivoCaja;
	}

	/**
	 * @return Returns the fechaArqueadaDDMMYYYY.
	 */
	public String getFechaArqueadaDDMMYYYY() {
		return fechaArqueadaDDMMYYYY;
	}

	/**
	 * @param fechaArqueadaDDMMYYYY The fechaArqueadaDDMMYYYY to set.
	 */
	public void setFechaArqueadaDDMMYYYY(String fechaArqueadaDDMMYYYY) {
		this.fechaArqueadaDDMMYYYY = fechaArqueadaDDMMYYYY;
	}

	/**
	 * @return Returns the fechaGeneracionArqueoDDMMYYYY.
	 */
	public String getFechaGeneracionArqueoDDMMYYYY() {
		return fechaGeneracionArqueoDDMMYYYY;
	}

	/**
	 * @param fechaGeneracionArqueoDDMMYYYY The fechaGeneracionArqueoDDMMYYYY to set.
	 */
	public void setFechaGeneracionArqueoDDMMYYYY(
			String fechaGeneracionArqueoDDMMYYYY) {
		this.fechaGeneracionArqueoDDMMYYYY = fechaGeneracionArqueoDDMMYYYY;
	}

	/**
	 * @return Returns the horaGeneracionArqueo.
	 */
	public String getHoraGeneracionArqueo() {
		return horaGeneracionArqueo;
	}

	/**
	 * @param horaGeneracionArqueo The horaGeneracionArqueo to set.
	 */
	public void setHoraGeneracionArqueo(String horaGeneracionArqueo) {
		this.horaGeneracionArqueo = horaGeneracionArqueo;
	}

	/**
	 * @return Returns the loginUsuarioCajero.
	 */
	public String getLoginUsuarioCajero() {
		return loginUsuarioCajero;
	}

	/**
	 * @param loginUsuarioCajero The loginUsuarioCajero to set.
	 */
	public void setLoginUsuarioCajero(String loginUsuarioCajero) {
		this.loginUsuarioCajero = loginUsuarioCajero;
	}

	/**
	 * @return Returns the loginUsuarioGenera.
	 */
	public String getLoginUsuarioGenera() {
		return loginUsuarioGenera;
	}

	/**
	 * @param loginUsuarioGenera The loginUsuarioGenera to set.
	 */
	public void setLoginUsuarioGenera(String loginUsuarioGenera) {
		this.loginUsuarioGenera = loginUsuarioGenera;
	}

	/**
	 * @return Returns the nombresUsuarioCajero.
	 */
	public String getNombresUsuarioCajero() {
		return nombresUsuarioCajero;
	}

	/**
	 * @param nombresUsuarioCajero The nombresUsuarioCajero to set.
	 */
	public void setNombresUsuarioCajero(String nombresUsuarioCajero) {
		this.nombresUsuarioCajero = nombresUsuarioCajero;
	}

	/**
	 * @return Returns the nombresUsuarioGenera.
	 */
	public String getNombresUsuarioGenera() {
		return nombresUsuarioGenera;
	}

	/**
	 * @param nombresUsuarioGenera The nombresUsuarioGenera to set.
	 */
	public void setNombresUsuarioGenera(String nombresUsuarioGenera) {
		this.nombresUsuarioGenera = nombresUsuarioGenera;
	}

	/**
	 * @return the consecutivoCajaPpal
	 */
	public String getConsecutivoCajaPpal() {
		return consecutivoCajaPpal;
	}

	/**
	 * @param consecutivoCajaPpal the consecutivoCajaPpal to set
	 */
	public void setConsecutivoCajaPpal(String consecutivoCajaPpal) {
		this.consecutivoCajaPpal = consecutivoCajaPpal;
	}

	/**
	 * 
	 * @param conLocal
	 * @param codigoInstitucionInt
	 * @param fechaDDMMYYYY
	 * @param loginUsuarioCajero2
	 * @param consecutivoCaja2
	 * @return
	 */
	public static ArrayList<Integer> estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha(
			Connection con, int codigoInstitucion,
			String fechaArqueoDDMMYYYY, String loginUsuarioCajero,
			String consecutivoCaja)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArqueosDao().estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha(con, codigoInstitucion, fechaArqueoDDMMYYYY, loginUsuarioCajero, consecutivoCaja);
	}
	
}