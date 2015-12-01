package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseConsultaMovimientosConsignacionesDao 
{
	
	
	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseConsultaMovimientosConsignacionesDao.class);
	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param almacen
	 * @param proveedor
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoCodigo
	 * @return
	 */
	public static HashMap consultarMovimientosConsignaciones(Connection con, String centroAtencion, String almacen, String proveedor, String fechaInicial, String fechaFinal, String tipoCodigo, String cadenaSql) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadenaSql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("CADENA >>>>>>>>>>>>>>"+cadenaSql);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("ERROR ", e);
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param almacen
	 * @param proveedor
	 * @param centroAtencion 
	 * @return
	 */
	public static String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String almacen, String proveedor, String centroAtencion) 
	{
		String cadena=" AND cc.centro_atencion = "+centroAtencion+" AND vmi.fechaelaboracion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+ "' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
		
		if(!almacen.equals(""))
		{
			cadena+=" AND vmi.almacen='"+almacen+"'";
		}
		if(!proveedor.equals(""))
		{
			cadena+=" AND vmi.proveedorcompra='"+proveedor+"'";
		}
		cadena+="  group by t.numero_identificacion,t.descripcion,a.codigo,a.codigo_interfaz,a.descripcion,a.costo_promedio";
		return cadena;
	}

	
}
