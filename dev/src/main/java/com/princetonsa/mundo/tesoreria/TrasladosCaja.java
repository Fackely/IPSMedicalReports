package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.tesoreria.TrasladosCajaDao;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class TrasladosCaja 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TrasladosCajaDao trasladosCajaDao;
	
	/**
	 * 
	 */
	private String consecutivoTraslado="";
	
	/**
	 * caja ppal
	 */
	private String cajaPpal;
	
	/**
	 * 
	 */
	private String fechaTraslado;
	
	/**
	 * 
	 */
	private String cajaMayor;
	
	/**
	 * 
	 */
	private HashMap trasladosCajaMap;
	
	/**
	 * 
	 */
	private String fechaGeneracionTraslado;
	
	/**
	 * 
	 */
	private String horaGeneracionTraslado;
	
	 /**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
		this.cajaPpal="";
		this.fechaTraslado="";
		this.cajaMayor="";
		this.trasladosCajaMap=new HashMap();
		this.fechaGeneracionTraslado="";
		this.horaGeneracionTraslado="";
		this.consecutivoTraslado="";
	}
	
	/**
	 * constructor de la clase
	 *
	 */
	public TrasladosCaja() 
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
			trasladosCajaDao = myFactory.getTrasladosCajaDao();
			wasInited = (trasladosCajaDao != null);
		}
		return wasInited;
	}

	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param fechaTraslado
	 * @param cajaPpal
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap busquedaCierresCajasParaTraslado(Connection con, String loginUsuario,  String fechaTraslado, String cajaPpal, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladosCajaDao().busquedaCierresCajasParaTraslado(con, loginUsuario, fechaTraslado, cajaPpal, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTraslado
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap resumen(Connection con, String consecutivoTraslado, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladosCajaDao().resumen(con, consecutivoTraslado, codigoInstitucion);
	}
	
	/**
	 * metodo que inserta el traslado basico y devuelve el consecutivo de insercion, si es "" entonces error
	 * @param con
	 * @param codigoInstitucion
	 * @param fechsTraslado
	 * @param cajaPpal
	 * @param cajaMayor
	 * @param loginUsuario
	 * @param fechaGeneracionTraslado
	 * @param horaGeneracionTraslado
	 * @return
	 */
	public String insertarTrasladoCaja(	Connection con,
										int codigoInstitucion, 
										String loginUsuario 
										)
	{
		return trasladosCajaDao.insertarTrasladoCaja(con, codigoInstitucion, this.fechaTraslado, this.cajaPpal, this.cajaMayor, loginUsuario, this.fechaGeneracionTraslado, this.horaGeneracionTraslado);
	}
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean insertarDetalleTrasladoCaja(Connection con, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		return trasladosCajaDao.insertarDetalleTrasladoCaja(con, this.trasladosCajaMap, consecutivoTrasladoCaja, codigoInstitucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean actualizarCierreCaja(Connection con, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		return trasladosCajaDao.actualizarCierreCaja(con, trasladosCajaMap, consecutivoTrasladoCaja, codigoInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listado(Connection con, HashMap criteriosBusquedaMap, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladosCajaDao().listado(con, criteriosBusquedaMap, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public static String[] obtenerFechaHoraUsuarioGeneracionTraslado(Connection con, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladosCajaDao().obtenerFechaHoraUsuarioGeneracionTraslado(con, consecutivoTrasladoCaja, codigoInstitucion);
	}
	
	/**
	 * @return the cajaMayor
	 */
	public String getCajaMayor() {
		return cajaMayor;
	}

	/**
	 * @param cajaMayor the cajaMayor to set
	 */
	public void setCajaMayor(String cajaMayor) {
		this.cajaMayor = cajaMayor;
	}

	/**
	 * @return the cajaPpal
	 */
	public String getCajaPpal() {
		return cajaPpal;
	}

	/**
	 * @param cajaPpal the cajaPpal to set
	 */
	public void setCajaPpal(String cajaPpal) {
		this.cajaPpal = cajaPpal;
	}

	/**
	 * @return the fechaGeneracionTraslado
	 */
	public String getFechaGeneracionTraslado() {
		return fechaGeneracionTraslado;
	}

	/**
	 * @param fechaGeneracionTraslado the fechaGeneracionTraslado to set
	 */
	public void setFechaGeneracionTraslado(String fechaGeneracionTraslado) {
		this.fechaGeneracionTraslado = fechaGeneracionTraslado;
	}

	/**
	 * @return the fechaTraslado
	 */
	public String getFechaTraslado() {
		return fechaTraslado;
	}

	/**
	 * @param fechaTraslado the fechaTraslado to set
	 */
	public void setFechaTraslado(String fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}

	/**
	 * @return the horaGeneracionTraslado
	 */
	public String getHoraGeneracionTraslado() {
		return horaGeneracionTraslado;
	}

	/**
	 * @param horaGeneracionTraslado the horaGeneracionTraslado to set
	 */
	public void setHoraGeneracionTraslado(String horaGeneracionTraslado) {
		this.horaGeneracionTraslado = horaGeneracionTraslado;
	}

	/**
	 * @return the trasladoCajaMap
	 */
	public HashMap getTrasladosCajaMap() {
		return trasladosCajaMap;
	}

	/**
	 * @param trasladoCajaMap the trasladoCajaMap to set
	 */
	public void setTrasladosCajaMap(HashMap trasladoCajaMap) {
		this.trasladosCajaMap = trasladoCajaMap;
	}

	/**
	 * @return the consecutivoTraslado
	 */
	public String getConsecutivoTraslado() {
		return consecutivoTraslado;
	}

	/**
	 * @param consecutivoTraslado the consecutivoTraslado to set
	 */
	public void setConsecutivoTraslado(String consecutivoTraslado) {
		this.consecutivoTraslado = consecutivoTraslado;
	}
	
	
}
