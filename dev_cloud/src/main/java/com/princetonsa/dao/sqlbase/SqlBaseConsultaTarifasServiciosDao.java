
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 24 /May/ 2006
 * Clase para las transacciones de la Consulta de las Tarifas de Servicios
 */
public class SqlBaseConsultaTarifasServiciosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseConsultaTarifasServiciosDao.class);
	
	
	/**
	 * Cadena con el statement necesario para consultar las tarifas de servicios por medio de una busqueda avanzada
	 */
	private static final String  consultaTarifasServiciosStr=" SELECT s.codigo as codigoservicio, " +
															 " (s.codigo||'-'||s.especialidad) as codserviciocompuesto, "+
															 " rs.codigo_propietario as codigopropietario, " +
															 " getcodigopropservicio2(s.codigo,"+ConstantesBD.codigoTarifarioCups+") as codigocupsservicio, " +
															 " getnombreservicio(s.codigo,"+ConstantesBD.codigoTarifarioCups+") as descripcionservicio, " +
															 " s.especialidad as codigoespecialidad, " +
															 " es.nombre as nombreespecialidad, " +
															 " s.tipo_servicio as codigotiposervicio, " +
															 " ts.nombre as nombretiposervicio, " +
															 " s.naturaleza_servicio as acronimonaturaleza, " +
															 " ns.nombre as nombrenaturaleza, " +
															 " s.grupo_servicio as codigogruposervicio, " +
															 " gs.descripcion as nombregruposervicio, " +
															 " CASE WHEN s.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Activo' ELSE 'Inactivo' END as estadoservicio "+
															 " FROM servicios s " +
															 " INNER JOIN referencias_servicio rs ON(s.codigo=rs.servicio and rs.tipo_tarifario=rs.tipo_tarifario) " +
															 " INNER JOIN especialidades es ON(s.especialidad=es.codigo) " +
															 " INNER JOIN tipos_servicio ts ON(s.tipo_servicio=ts.acronimo) " +
															 " INNER JOIN naturalezas_servicio ns ON(s.naturaleza_servicio=ns.acronimo) " +
															 " INNER JOIN grupos_servicios gs ON(s.grupo_servicio=gs.codigo) " +
															 " WHERE 1=1 ";
	
	/**
	 * Cadena con el estatement necesario para consultar el encabezado del detalle de una tarifa de servicios 
	 * para un servicio dado
	 */
	private static final String consultaEncabezadoDetalleStr=" SELECT s.codigo as codigointerno, " +
															 " getcodigopropservicio2(s.codigo, "+ConstantesBD.codigoTarifarioCups+") as codigocups, " +
															 " CASE WHEN s.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Activo' ELSE 'Inactivo' END as estadoservicio, " +
															 " getnombreserviciotarifa(s.codigo, "+ConstantesBD.codigoTarifarioCups+") as descripcioncups " +
															 " FROM servicios s " +
															 " WHERE s.codigo=? ";
	
	/**
	 * Cadena con el statement necesario para consultar del cuerpo del detalle de una tarifa de serivios dado el codigo del servicio
	 * Esta consulta evalua los esquemas tarifarios y los tarifarios oficiales, asi misom evalua las tarifas iss y las tarifas souat segun corresponda filtrando 
	 * solo los que tengan tarifas existentes
	 */
	private static final String consultaCuerpoDetalleStr=" SELECT et.nombre as esquematarifario, " +
														 " et.cantidad as valorbase, " +
														 " tl.nombre as tipoliquidacion, " +
														 " ti.valor_tarifa as valortarifa, " +
														 " ti.tipo_liquidacion as codigotipoliquidacion, " +
														 " tao.nombre as tipomanual, " +
														 " ti.servicio as codigoservicio, " +
														 " ti.esquema_tarifario as codigoesquematarifario, " +
														 " et.tarifario_oficial as codigotarifariooficial, " +
														 " ti.unidades as valorliquidacion, " +
														 " ti.tipo_liquidacion as codigotipoliquidacion," +
														 " getintegridaddominio(ti.liq_asocios) AS liquidar_asocios," +
														 " ti.fecha_vigencia AS fechavigencia " +
														 
														 " from tarifas_iss ti " +
														 " inner join esquemas_tarifarios et on(et.codigo=ti.esquema_tarifario) " +
														 " inner join tipos_liquidacion_soat tl on(tl.codigo=ti.tipo_liquidacion) " +
														 " inner join tarifarios_oficiales tao on(tao.codigo=et.tarifario_oficial) " +
														 " where ti.servicio=? "
														 + " UNION "	+
														 " SELECT et.nombre as esquematarifario, " +
														 " et.cantidad as valorbase, " +
														 " tl.nombre as tipoliquidacion, " +
														 " ts.valor_tarifa as valortarifa, " +
														 " ts.tipo_liquidacion as codigotipoliquidacion, " +
														 " tao.nombre as tipomanual, " +
														 " ts.servicio as codigoservicio, " +
														 " ts.esquema_tarifario as codigoesquematarifario, " +
														 " et.tarifario_oficial as codigotarifariooficial, " +
														 " ts.unidades as valorliquidacion, " +
														 " ts.tipo_liquidacion as codigotipoliquidacion, " +
														 " getintegridaddominio(ts.liq_asocios) AS liquidar_asocios," +
														 " ts.fecha_vigencia AS fechavigencia " +
														 " from tarifas_soat ts " +
														 " inner join esquemas_tarifarios et on(et.codigo=ts.esquema_tarifario) " +
														 " inner join tipos_liquidacion_soat tl on(tl.codigo=ts.tipo_liquidacion) " +
														 " inner join tarifarios_oficiales tao on(tao.codigo=et.tarifario_oficial) " +
														 " where ts.servicio=? ORDER BY codigoesquematarifario, fechavigencia DESC ";	
	
	
	
	
	
	/**
	 * Metodo para realizar la busqueda avanzada de los servicios por los campos
	 * que se especifiquen
	 * @param con
	 * @param codigoInterno
	 * @param codigoCups
	 * @param descripcionCups
	 * @param codigoIss
	 * @param descripcionIss
	 * @param codigoSoat
	 * @param descripcionSoat
	 * @param codigoEspecialidad
	 * @param acronimoTipoServicio
	 * @param acronimoNaturaleza
	 * @param codigoGrupoServicio
	 * @return
	 */
	public static HashMap busquedaServicios (Connection con, String codigoInterno, String tipoTarifario, String codigoServicio, String descripcionServicio, int codigoEspecialidad, String acronimoTipoServicio, String acronimoNaturaleza,int codigoGrupoServicio) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps = null;
		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
		
			String avanzadaStr = "";
			boolean filtroEspecificoTipotarifario=false;
			if(!codigoInterno.trim().equals(""))
			{
				avanzadaStr+=" AND s.codigo = "+codigoInterno;
			}
			if(!UtilidadTexto.isEmpty(tipoTarifario))
			{
				
				avanzadaStr+=" AND rs.tipo_tarifario="+tipoTarifario+" ";
				filtroEspecificoTipotarifario=true;
				if(!UtilidadTexto.isEmpty(codigoServicio))
				{
					avanzadaStr+=" AND rs.codigo_propietario='"+codigoServicio+"' ";
				}
				if(!UtilidadTexto.isEmpty(descripcionServicio))
				{
					avanzadaStr+=" AND UPPER(rs.descripcion) LIKE UPPER('%"+descripcionServicio+"%')";
				}
			}
			if(codigoEspecialidad!=-1)
			{
				avanzadaStr+=" AND s.especialidad="+codigoEspecialidad;
			}
			if(!acronimoTipoServicio.trim().equals("-1"))
			{
				avanzadaStr+=" AND s.tipo_servicio='"+acronimoTipoServicio+"'";
			}
			if(!acronimoNaturaleza.trim().equals("-1"))
			{
				avanzadaStr+=" AND s.naturaleza_servicio='"+acronimoNaturaleza+"'";
			}
			if(codigoGrupoServicio!=-1)
			{
				avanzadaStr+=" AND s.grupo_servicio="+codigoGrupoServicio;
			}
			//colocamos entonces el cups 
			if(!filtroEspecificoTipotarifario)
			{	
				avanzadaStr+=" AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+"  ";
			}		
			
			String consulta= consultaTarifasServiciosStr + avanzadaStr+" ORDER BY rs.descripcion ";
			
			logger.info("consultaTarifasServiciosStr--->"+consulta);
			
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return  UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en Busqueda Avanzada de Tarifas de Servicios : [SqlBaseConsultaTarifasServiciosDao] "+e.toString() );
			return null;
		}	    
	}
	
	/**
	 * Método para consultar el encabzado del detalle de una tarifa de servicios dado el codigo de servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultaEncabezadoDetalle(Connection con, int codigoServicio)  throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaEncabezadoDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoServicio);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, false) ;
	        ps.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del encabezado del detalle de una tarifa de servicios dado el codigo del servicio : [SqlBaseConsultaTarifasServiciosDao]"+e.toString());
			return null;
		}
	}
	
	/**
	 * Método para consultar el cuerpo del detalle de una tarifa de servicios dado el codigo interno del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultaCuerpoDetalle(Connection con, int codigoServicio) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaCuerpoDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n CONSULTA DETALLE-->"+consultaCuerpoDetalleStr+" serv->"+codigoServicio+"\n\n");
			ps.setInt(1, codigoServicio);
			ps.setInt(2, codigoServicio);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())) ;
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del cuerpo del detalle de una tarifa de servicios dado el codigo del servicio : [SqlBaseConsultaTarifasServiciosDao]"+e.toString());
			return null;
		}
	}
	
	
}