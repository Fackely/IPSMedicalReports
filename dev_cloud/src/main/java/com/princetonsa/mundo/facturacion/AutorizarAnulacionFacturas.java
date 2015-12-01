package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.AutorizarAnulacionFacturasDao;

public class AutorizarAnulacionFacturas 
{

	
	private AutorizarAnulacionFacturasDao objetoDao;
	
	
	public AutorizarAnulacionFacturas()
	{
		init(System.getProperty("TIPOBD"));
	}

	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getAutorizarAnulacionFacturasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}


	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param codigoCentroAtencion
	 * @return
	 */
	public HashMap consultarListadoSolicitudes(Connection con, String loginUsuario, int centroAtencion, int codigoInstitucion) 
	{
		return objetoDao.consultarListadoSolicitudes(con, loginUsuario, centroAtencion, codigoInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	public HashMap consultarDetalleSolicitud(Connection con, String codigoSolicitud) 
	{
		return objetoDao.consultarDetalleSolicitud(con, codigoSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarAutorizacion(Connection con, HashMap vo) 
	{
		return objetoDao.insertarAutorizacion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public HashMap consultarResumenAutorizar(Connection con, String codigoSolicitud) 
	{
		return objetoDao.consultarResumenAutorizar(con, codigoSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param consecutivoFactura
	 * @param numeroAutorizacion
	 * @param usuarioAutoriza
	 * @return
	 */
	public HashMap consultarListadoAprobadas(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String numeroAutorizacion, String usuarioAutoriza) 
	{
		return objetoDao.consultarListadoAprobadas(con, fechaInicial, fechaFinal, consecutivoFactura, numeroAutorizacion, usuarioAutoriza);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarAnulacionAutorizacion(Connection con, HashMap vo) 
	{
		return objetoDao.insertarAnulacionAutorizacion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param consecutivoFactura
	 * @param estadoAutorizacion
	 * @param usuarioAutoriza
	 * @param motivoAnulacion
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultaListadoAutorizaciones(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String estadoAutorizacion, String usuarioAutoriza, String motivoAnulacion, String centroAtencion) 
	{
		return objetoDao.consultaListadoAutorizaciones(con, fechaInicial, fechaFinal, consecutivoFactura, estadoAutorizacion, usuarioAutoriza, motivoAnulacion, centroAtencion);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap consultaDetalleAutorizacion(Connection con, String numeroSolicitud) 
	{
		return objetoDao.consultaDetalleAutorizacion(con, numeroSolicitud);
	}
	
	
}
