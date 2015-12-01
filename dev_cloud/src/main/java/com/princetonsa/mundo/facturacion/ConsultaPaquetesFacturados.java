package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsultaPaquetesFacturadosDao;

public class ConsultaPaquetesFacturados 
{
	
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	private ConsultaPaquetesFacturadosDao objetoDao;
	
	
	/**
	 * 
	 *
	 */
	public ConsultaPaquetesFacturados()
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
			objetoDao=myFactory.getConsultaPaquetesFacturadosDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}

	
	/**
	 * 
	 * @param con 
	 * @param codigoConvenio
	 * @param codigoPaquete
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap consultarPaquetesFacturados(Connection con, String codigoConvenio, String codigoPaquete, String fechaInicial, String fechaFinal) 
	{
		return objetoDao.consultarPaquetesFacturados(con, codigoConvenio, codigoPaquete, fechaInicial, fechaFinal);
	}

	/**
	 * 
	 * @param con
	 * @param detalleFactura
	 * @return
	 */
	public HashMap consultarDetallePaquetes(Connection con, String detalleFactura) 
	{
		return objetoDao.consultarDetallePaquetes(con, detalleFactura);
	}

	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public HashMap consultarAsociosCirugia(Connection con, String solicitud, String servicioCx, String codDetFactura) 
	{
		return objetoDao.consultarAsociosCirugia(con, solicitud, servicioCx,codDetFactura);
	}
	
	
}
