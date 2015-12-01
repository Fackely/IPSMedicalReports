/**
 * 
 */
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseRegistroDevolucionRecibosCajaDao;
import com.princetonsa.dao.tesoreria.RegistroDevolucionRecibosCajaDao;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoProcesoDevolucionRC;

/**
 * @author Jorge ARmando Osorio Velasquez
 *
 */
public class RegistroDevolucionRecibosCaja 
{

	private RegistroDevolucionRecibosCajaDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public RegistroDevolucionRecibosCaja()
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param property
	 */
	private void init(String tipoBD) 
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		objetoDao=myFactory.getRegistroDevolucionRecibosCajaDao();
	}

	public HashMap<String, Object> consultarRecibosCaja(HashMap<String, Object> vo) 
	{
		return objetoDao.consultarRecibosCaja(vo);
	}

	/**
	 * 
	 * @param vo
	 * @return
	 */
	public boolean guardarDevolucion(HashMap<String, Object> vo) 
	{
		return objetoDao.guardarDevolucion(vo);
	}
	
	/**
	 * se obtiene el valor del concepto de ingreso de tesoreria
	 * @param parametros
	 * @return String 
	 */
	public String getValorConceptoIngTesoreria(String codigo_cit, String cod_institucion, int cod_tipo_ingreso)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigo_cit", codigo_cit);
		parametros.put("cod_institucion", cod_institucion);
		parametros.put("cod_tipo_ingreso", cod_tipo_ingreso);
		return SqlBaseRegistroDevolucionRecibosCajaDao.getValorConceptoIngTesoreria(parametros);
	}
	
	
	
	/**
	 * 
	 * @param numeroRecibo
	 * @return
	 */
	 public   ArrayList<DtoVentaTarjetasCliente> consultaFacturas(String numeroRecibo){
		 
		 return objetoDao.consultaFacturas(numeroRecibo);
	 }
	 
	/**
	 * 
	 * @param con
	 * @param arqueo
	 * @param cierre
	 */
	public static boolean actualizarEstadoArqueoCierreDevol(Connection con, String arqueo, String cierreCaja, ArrayList<Integer> codigosPkDevol)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroDevolucionRecibosCajaDao().actualizarEstadoArqueoCierreDevol(con, arqueo, cierreCaja, codigosPkDevol);
	}
	
	/**
	 * 
	 */
	public static DtoProcesoDevolucionRC estaEnProcesoDevolucion(String nroRC,int institucion, String idSesionOPCIONAL, boolean igualSession)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroDevolucionRecibosCajaDao().estaEnProcesoDevolucion(nroRC, institucion, idSesionOPCIONAL, igualSession);
	}
	
	/**
	 * 
	 */
	public static boolean empezarBloqueoDevolucion(String nroRC,int institucion,String loginUsuario,String idSesion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroDevolucionRecibosCajaDao().empezarBloqueoDevolucion(nroRC, institucion, loginUsuario, idSesion);
	}
	
	/**
	 * 
	 */
	public static boolean cancelarBloqueoDevolucion(String nroRC, int institucion, String idSesion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroDevolucionRecibosCajaDao().cancelarBloqueoDevolucion(nroRC, institucion, idSesion);
	}
	
	

}
