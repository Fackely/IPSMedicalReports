package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.SolicitarAnulacionFacturasDao;

public class SolicitarAnulacionFacturas 
{

	
	/**
	 * 
	 */
	private SolicitarAnulacionFacturasDao objetoDao;
	
	
	/**
	 * 
	 */
	public SolicitarAnulacionFacturas()
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
			objetoDao=myFactory.getSolicitarAnulacionFacturasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}

	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarFactura(Connection con, String consecutivoFactura, int codigoInstitucion) 
	{
		return objetoDao.consultarFactura(con, consecutivoFactura, codigoInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarSolictud(Connection con, HashMap vo) 
	{
		return objetoDao.insertarSolictud(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	public HashMap consultarResumenSolictud(Connection con, int codigoSolicitud) 
	{
		return objetoDao.consultarResumenSolictud(con, codigoSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param consecutivoFactura
	 * @param codigoSolicitud
	 * @param usuarioAutoriza
	 * @param motivoAnulacion
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultarSolictudesAnular(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String codigoSolicitud, String usuarioAutoriza, String motivoAnulacion, String centroAtencion) 
	{
		return objetoDao.consultarSolictudesAnular(con, fechaInicial, fechaFinal, consecutivoFactura, codigoSolicitud, usuarioAutoriza, motivoAnulacion, centroAtencion);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap consultaDetalleSolicitud(Connection con, String numeroSolicitud) 
	{
		return objetoDao.consultaDetalleSolicitud(con, numeroSolicitud);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarAnulacionSolicitud(Connection con, HashMap vo) 
	{
		return objetoDao.insertarAnulacionSolicitud(con, vo);
	}


	
	
	
	
}
