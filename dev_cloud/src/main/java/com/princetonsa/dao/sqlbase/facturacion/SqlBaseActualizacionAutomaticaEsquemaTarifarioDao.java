package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;

import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseActualizacionAutomaticaEsquemaTarifarioDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseActualizacionAutomaticaEsquemaTarifarioDao.class);
	
	
	/**
	 * cadena para la insercion Tarifas Inventarios
	 */
	
	private static final String cadenaInsertarInventarioStr="INSERT INTO esq_tar_inventarios_contrato (codigo, contrato, clase_inventario, esquema_tarifario, fecha_vigencia, usuario_modifica, fecha_modifica, hora_modifica, centro_atencion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	/**
	 * cadena para la insercion Tarifas Inventarios
	 */
	
	private static final String cadenaInsertarServicioStr="INSERT INTO esq_tar_procedimiento_contrato (codigo, contrato, grupo_servicio, esquema_tarifario, fecha_vigencia, usuario_modifica, fecha_modifica, hora_modifica, centro_atencion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * 
	 */
	private static final String consultaInventarios="SELECT codigo, contrato, getnomconvcontrato(contrato) as nombreconvenio, clase_inventario as claseinventario, esquema_tarifario as esquematarifario, fecha_vigencia as fechavigencia, centro_atencion as centro_atencion  FROM esq_tar_inventarios_contrato";
	
	/**
	 * 
	 */
	private static final String consultaServicios="SELECT codigo, contrato, getnomconvcontrato(contrato) as nombreconvenio, grupo_servicio as gruposervicio, esquema_tarifario as esquematarifario, fecha_vigencia as fechavigencia, centro_atencion as centro_atencion  FROM esq_tar_procedimiento_contrato";
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaConvenioStr=" SELECT distinct c.codigo as codigoconvenio, " +
															"c.nombre as nombreconvenio, " +
															"ct.codigo as contrato, " +
															"ct.numero_contrato as numerocontrato, " +
															"'"+ConstantesBD.acronimoNo+"' as seleccionado, " +
															"ct.maneja_tarifas_x_ca as manejatarifasca " +
															"FROM convenios c " +
															"inner join contratos ct on(ct.convenio=c.codigo) ";
															
	
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarInventario(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
			ps.setDouble(1, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_esqtarinvcont")+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("contrato").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("clase_inventario")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("clase_inventario").toString()));
			
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
			
			ps.setDate(5, Date.valueOf(vo.get("fecha_vigencia").toString()));
			
			ps.setString(6, vo.get("usuario_modifica").toString());
			
			ps.setDate(7, Date.valueOf(vo.get("fecha_modifica").toString()));
			
			ps.setString(8, vo.get("hora_modifica").toString());
			
			if(Utilidades.convertirAEntero(vo.get("centro_atencion")+"")<=0)
			{
				ps.setNull(9, Types.INTEGER);
			}
			else
			{
				ps.setInt(9, Utilidades.convertirAEntero(vo.get("centro_atencion")+""));
			}
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarServicio(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_esqtarprocont")+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("contrato").toString()));
			
			if(UtilidadTexto.isEmpty(vo.get("grupo_servicio")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("grupo_servicio").toString()));
			
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("esquema_tarifario").toString()));
			
			ps.setDate(5, Date.valueOf(vo.get("fecha_vigencia").toString()));
			
			ps.setString(6, vo.get("usuario_modifica").toString());
			
			ps.setDate(7, Date.valueOf(vo.get("fecha_modifica").toString()));
			
			ps.setString(8, vo.get("hora_modifica").toString());
			
			if(Utilidades.convertirAEntero(vo.get("centro_atencion")+"")<=0)
			{
				ps.setNull(9, Types.INTEGER);
			}
			else
			{
				ps.setInt(9, Utilidades.convertirAEntero(vo.get("centro_atencion")+""));
			}
			
			return ps.executeUpdate()>0;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
		
	}

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaInventarios(Connection con) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaServicios(Connection con) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		mapa.put("numRegistros", "0");
		try 
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaServicios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @param contrato
	 * @param empresa
	 * @param tipoConvenio
	 * @param esquemaServicios
	 * @param esquemaInventarios
	 * @return
	 */
	public static HashMap<String, Object> consultaConveniosVigentes(Connection con, 
																	String convenio, 
																	String contrato, 
																	String empresa, 
																	String tipoConvenio, 
																	String esquemaServicios, 
																	String esquemaInventarios) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaConsultaConvenioStr;
		String where=" WHERE fecha_final>=current_date ";
		
		if(!convenio.equals(""))
		{
			where+=" and c.codigo="+convenio;
		}
		if(!contrato.equals(""))
		{
			where+=" and ct.codigo="+contrato;
		}
		if(!empresa.equals(""))
		{
			where+=" and c.empresa="+empresa;
		}
		if(!tipoConvenio.equals(""))
		{
			where+=" and c.tipo_convenio="+tipoConvenio;
		}
		if(!esquemaServicios.equals(""))
		{
			cadena+=" inner join esq_tar_procedimiento_contrato ep on(ep.contrato=ct.codigo) ";
			where+=" and ep.esquema_tarifario="+esquemaServicios;
		}
		if(!esquemaInventarios.equals(""))
		{
			cadena+=" inner join esq_tar_inventarios_contrato ei on(ei.contrato=ct.codigo) ";
			where+=" and ei.esquema_tarifario="+esquemaInventarios;
		}
		
		where+="  order by nombreconvenio";
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena+where,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("CONSULTA FINAL >>>>>>> "+cadena+where);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

}
