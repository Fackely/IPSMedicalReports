package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.ConsultaFacturasVariasDao;

public class ConsultaFacturasVarias 
{

	
	/**
	 * 
	 */
	private ConsultaFacturasVariasDao objetoDao;
	
	
	/**
	 * 
	 *
	 */
	public ConsultaFacturasVarias()
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
			objetoDao=myFactory.getConsultaFacturasVariasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param factura
	 * @param estadosFactura
	 * @param tipoDeudor
	 * @param deudor
	 * @return
	 */
	public HashMap BusquedaFacturas(Connection con, String fechaInicial, String fechaFinal, String factura, String estadosFactura, String tipoDeudor, String deudor) 
	{
		return  objetoDao.BusquedaFacturas(con, fechaInicial, fechaFinal, factura, estadosFactura, tipoDeudor, deudor);
		
	}
	
	/**
	 * 
	 * @param con
	 * @param factura
	 * @param tipoDeudor
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleFactura(Connection con,int factura, String tipoDeudor)
	{
		return objetoDao.consultaDetalleFactura(con, factura, tipoDeudor);
	}
	
	

}
