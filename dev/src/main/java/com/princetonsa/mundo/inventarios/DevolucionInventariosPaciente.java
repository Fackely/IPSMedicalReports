package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.DevolucionInventariosPacienteDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class DevolucionInventariosPaciente {
	
	/**
	 * Objeto para manejar la conexion dao a la bd
	 */
	DevolucionInventariosPacienteDao objetoDao;
	
	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getDevolucionInventariosPacienteDao();
		}
	}
	
	/**
	 * Constructor de la clase
	 *
	 */
	public DevolucionInventariosPaciente()
	{
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param codigoCentroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultarIngresoCuentaPaciente(Connection con, int codigoPersona,int codigoCentroAtencion)
	{
		return objetoDao.consultarIngresoCuentaPaciente(con, codigoPersona, codigoCentroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarPedidos(Connection con, int codigoPersona)
	{
		return objetoDao.consultarPedidos(con, codigoPersona);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> consultarSolicitudes(Connection con, int codigoPersona)
	{
		return objetoDao.consultarSolicitudes(con, codigoPersona);
	}
	
	/**
	 * 
	 * @param con
	 * @param despacho
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleSolicitudes(Connection con,int despacho)
	{
		return objetoDao.consultaDetalleSolicitudes(con, despacho);
	}
	
	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public HashMap<String, Object> consultarFechaHoraDespachoSolicitudes(Connection con,int solicitud)
	{
		return objetoDao.consultarFechaHoraDespachoSolicitudes(con, solicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarDevolucionSolicitudes(Connection con, HashMap vo)
	{
		return objetoDao.insertarDevolucionSolicitudes(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarDetalleDevolucionSolicitudes(Connection con, HashMap vo)
	{
		return objetoDao.insertarDetalleDevolucionSolicitudes(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap<String, Object> consultaDevolucionSolicitudes(Connection con,int codigo)
	{
		return objetoDao.consultaDevolucionSolicitudes(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleDevolucionSolicitudes(Connection con,int codigo)
	{
		return objetoDao.consultaDetalleDevolucionSolicitudes(con, codigo);
	}

}
