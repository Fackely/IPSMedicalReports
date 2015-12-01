package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.util.birt.reports.DesignEngineApi;

public class SqlBaseVentasEmpresaConvenioDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger = Logger.getLogger(SqlBaseVentasEmpresaConvenioDao.class);
	
	/**
	 * 
	 */
	private static String cadenaConsultaVentasCentroCosto = "SELECT " +
															"f.convenio as convenio, " +
															"getnombreconvenio(f.convenio) as nombreconvenio, " +
															"sum(f.valor_total) as valorsuma, " +
															"e.razon_social as nombreempresa, " +
															"t.numero_identificacion as nit " +
															"FROM facturas f " +
															"INNER JOIN convenios c on(c.codigo=f.convenio) " +
															"INNER JOIN empresas e on(e.codigo=c.empresa) " +
															"INNER JOIN terceros t on(t.codigo=e.tercero) " +
															"WHERE f.centro_aten=? AND f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	
	
	
	/**
	 * 
	 * @param con
	 * @param comp
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param empresa
	 * @param convenio
	 * @return
	 */
	public static String cambiarConsulta(Connection con, DesignEngineApi comp, String fechaInicial, String fechaFinal, String empresa, String convenio) 
	{
		
		String cadena=" f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" AND to_char(f.fecha, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+ "' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
		
		if(!empresa.equals(""))
		{
			cadena+=" AND c.empresa="+empresa;
		}
		if(!convenio.equals(""))
		{
			cadena+=" AND f.convenio="+convenio;
		}
		
		cadena+=" group by f.convenio, e.razon_social, t.numero_identificacion";
		
		return cadena;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap consultarVentasCentroCosto(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaConsultaVentasCentroCosto;
		
		cadena+=" AND to_char(f.fecha, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechainicial")+"")+ "' AND '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechafinal")+"")+"' ";
		
		if(!(vo.get("empresa")+"").equals(""))
		{
			cadena+=" AND c.empresa="+vo.get("empresa");
		}
		if(!(vo.get("convenio")+"").equals(""))
		{
			cadena+=" AND f.convenio="+vo.get("convenio") ;
		}
		
		cadena+=" group by f.convenio, e.razon_social, t.numero_identificacion ";
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			busqueda.setInt(1, Utilidades.convertirAEntero(vo.get("centroatencion")+""));
			logger.info("cadena >>>>>>> "+cadena);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

	
}
