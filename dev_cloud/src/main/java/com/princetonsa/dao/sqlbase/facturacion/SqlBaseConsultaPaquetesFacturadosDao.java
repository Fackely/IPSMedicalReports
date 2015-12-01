package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;


import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

public class SqlBaseConsultaPaquetesFacturadosDao 
{
	
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseConsultaPaquetesFacturadosDao.class);
	
	
	/**
	 * 
	 */
	private static final String cadenaBusquedaPaquetesFacturados="SELECT " +
																"getdescripcionpaquete(pc.paquete,pc.institucion) as descripcionpaquete," +
																"pc.paquete as codigopaquete, " +
																"dfs.valor_total as valorpaquete, " +
																"f.consecutivo_factura as factura, " +
																"f.fecha as fechafactura, " +
																"f.hora as horafactura, " +
																"getnombreconvenio(f.convenio) as convenio, " +
																"f.usuario as usuariofactura," +
																"dfs.codigo as detallefactura, " +
																"(select sum(dc.valor_total_cargado) from det_cargos dc where dc.cargo_padre=(select dc1.codigo_detalle_cargo from det_cargos dc1 where dc1.solicitud=dfs.solicitud and dc1.servicio=dfs.servicio and dc1.sub_cuenta=f.sub_cuenta and dc1.eliminado='N') ) as valorcontenidopaquete " +
																"FROM facturas f " +
																"inner join det_factura_solicitud dfs on(f.codigo=dfs.factura) " +
																"inner join paquetizacion p on(p.numero_solicitud_paquete=dfs.solicitud) " +
																"inner join paquetes_convenio pc on(p.codigo_paquete_convenio=pc.codigo) " +
																"WHERE f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ";
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaDetallePaquetes="SELECT solicitud as solicitud, " +
															  "case when articulo is null then servicio  else articulo end as serviarticulo, " +
															  "case when articulo is null then getnombreservicio(servicio,0)  else getdescarticulo(articulo) end as descserviarticulo, " +
															  "cantidad_cargo as cantidad, " +
															  "valor_cargo as valorunitario, " +
															  "valor_total as valortotal, " +
															  "'"+ConstantesBD.acronimoNo+"' as escirugia, " +
															  "codigo_det_fact as codigodetfact " +
															  "FROM paquetizacion_det_factura " +
															  "WHERE codigo_det_fact=? and servicio_cx is null " +
															  "UNION " +
															  "SELECT  solicitud as solicitud, " +
															  "servicio_cx as serviarticulo, " +
															  "getnombreservicio(servicio_cx,0) as descserviarticulo, " +
															  "1 as cantidad, " +
															  "sum(valor_cargo) as valorunitario, " +
															  "sum(valor_total) as valortotal, " +
															  "'"+ConstantesBD.acronimoSi+"' as escirugia, " +
															  "codigo_det_fact as codigodetfact " + 
															  "FROM paquetizacion_det_factura " +
															  "WHERE codigo_det_fact=? and servicio_cx is not null  group by solicitud,servicio_cx,codigo_det_fact ";
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaAsociosCirugia="SELECT " +
															 "servicio as servicio, " +
															 "getnombreservicio(servicio,0) as nombreservicio, " +
															 "valor_asocio as valorasocio " +
															 "FROM paquetizacion_det_factura " +
															 "WHERE codigo_det_fact=? and solicitud=? and servicio_cx=? and tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" ";
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoConvenio
	 * @param codigoPaquete
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static HashMap consultarPaquetesFacturados(Connection con, String codigoConvenio, String codigoPaquete, String fechaInicial, String fechaFinal) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaBusquedaPaquetesFacturados;
		if((!fechaInicial.equals("")) && (!fechaFinal.equals("")))
		{
			cadena+=" AND f.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
		}
		if(!codigoConvenio.equals(""))
		{
			cadena+=" AND f.convenio="+codigoConvenio;
		}
		if(!codigoPaquete.equals(""))
		{
			cadena+=" AND pc.paquete='"+codigoPaquete+"'";
		}
		
		//cadena+="  order by codigo_fac_var";
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("CADENA >>>>>>>>>>>>>>"+cadena);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param detalleFactura
	 * @return
	 */
	public static HashMap consultarDetallePaquetes(Connection con, String detalleFactura) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetallePaquetes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaDetallePaquetes);
				logger.info("detalleFactura >>>>>>>>"+detalleFactura);
				
				ps.setInt(1, Utilidades.convertirAEntero(detalleFactura));
				ps.setInt(2, Utilidades.convertirAEntero(detalleFactura));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LOS PAQUETES FACTURADOS "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public static HashMap consultarAsociosCirugia(Connection con, String solicitud, String servicioCx, String codDetFactura) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAsociosCirugia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaAsociosCirugia);
				logger.info("solicitud >>>>>>>>"+solicitud);
				logger.info("detalleFactura >>>>>>>>"+servicioCx);
				
				ps.setInt(1, Utilidades.convertirAEntero(codDetFactura));
				ps.setInt(2, Utilidades.convertirAEntero(solicitud));
				ps.setInt(3, Utilidades.convertirAEntero(servicioCx));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LOS PAQUETES FACTURADOS "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	
	
}
