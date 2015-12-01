package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.sqlbase.SqlBaseCentrosCostoDao;
import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

public class SqlBaseVentasCentroCostoDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger = Logger.getLogger(SqlBaseCentrosCostoDao.class);
	
	/**
	 * 
	 */
	private static final String cadenaConsultaVentasCentroCostoStr= "SELECT " +
																	"s.centro_costo_solicitante as centrocostosolicita, " +
																	"getnomcentrocosto(s.centro_costo_solicitante) as nombreccsolicita, " +
																	"s.centro_costo_solicitado as centrocostoejecuta, " +
																	"getnomcentrocosto(s.centro_costo_solicitado) as nombreccejecuta, " +
																	"getcuentacontable(g.cuenta_ingreso) as cuenta, " +
																	"(sum(d.valor_cargo*cantidad_cargo)) as valortotal, " +
																	"(sum(d.valor_dcto_comercial*d.cantidad_cargo)) as descuentocomercial, " +
																	"sum(d.valor_total) as ventastotal " +
																	"FROM det_factura_solicitud d " +
																	"INNER JOIN facturas f on(f.codigo=d.factura) " +
																	"INNER JOIN solicitudes s on(s.numero_solicitud=d.solicitud) " +
																	"LEFT OUTER JOIN grupo_servicio_cue_ingr g on(g.centro_costo=s.centro_costo_solicitado and g.grupo_servicio=-1) " +
																	"WHERE f.centro_aten=? AND f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	
	
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param centroCosto
	 * @return
	 */
	public static String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String centroCosto) 
	{
		String cadena=" f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" AND to_char(f.fecha, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+ "' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
		
		if(!centroCosto.equals(""))
		{
			cadena+=" AND s.centro_costo_solicitante="+centroCosto;
		}
		
		cadena+=" group by s.centro_costo_solicitante, s.centro_costo_solicitante, s.centro_costo_solicitado, g.cuenta_ingreso";
		
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
		
		String cadena=cadenaConsultaVentasCentroCostoStr;
		
		cadena+=" AND to_char(f.fecha, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechainicial")+"")+ "' AND '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechafinal")+"")+"' ";
		
		if(!(vo.get("centrocosto")+"").equals(""))
		{
			cadena+=" AND s.centro_costo_solicitante="+vo.get("centrocosto");
		}
		
		cadena+=" group by s.centro_costo_solicitante, s.centro_costo_solicitante, s.centro_costo_solicitado, g.cuenta_ingreso ";
		
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
