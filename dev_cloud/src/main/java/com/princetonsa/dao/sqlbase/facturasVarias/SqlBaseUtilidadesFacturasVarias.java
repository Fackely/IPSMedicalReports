package com.princetonsa.dao.sqlbase.facturasVarias;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.administracion.SqlBaseTiposRetencionDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;

/**
 * @author Víctor Hugo Gómez L.
 */
public class SqlBaseUtilidadesFacturasVarias {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseTiposRetencionDao.class);
	
	/***
	 * ObtenerConceptosFraVarias
	 * @param con
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(Connection con, int institucion, boolean activo) 
	{
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String consulta= "SELECT " +
				"consecutivo," +
				"codigo," +
				"descripcion," +
				"cuenta_contable_debito AS cuenta_con_deb," +
				"activo," +
				"cuenta_contable_credito AS cuenta_con_cre," +
				"tercero," +
				"tipo_concepto " +
				"FROM conceptos_facturas_varias " +
				"WHERE institucion = ? ";
				
		try
		{
			if(activo)
				consulta+=" AND activo = ? ";
			
			consulta+=" ORDER BY descripcion ASC ";
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			if(activo)
				pst.setString(2, ConstantesBD.acronimoSi);
				
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{		
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("consecutivo", rs.getObject("consecutivo"));
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("descripcion", rs.getObject("descripcion"));
				mapa.put("cuenta_contable_debito", rs.getObject("cuenta_con_deb"));
				mapa.put("activo", rs.getObject("activo"));
				mapa.put("cuenta_contable_credito", rs.getObject("cuenta_con_cre"));
				mapa.put("tercero", rs.getObject("tercero"));
				mapa.put("tipo_concepto", rs.getObject("tipo_concepto"));
				list.add(mapa);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error ObtenerConceptosFraVarias !!!!!!\n La Consulta: "+consulta);
			logger.error(e);			
		}
		return list;
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param activo
	 * @param tipoConcepto
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(Connection con, int institucion, boolean activo, String tipoConcepto) 
	{
		logger.info("----------------------------------------------------------");
		logger.info("---------------------Obtener Conceptos Varias-------------");
		logger.info("----------------------------------------------------------");
		logger.info("-----------------"+tipoConcepto+"-------------------------");
		logger.info("-----------------"+institucion+"--------------------------");
		
		
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		String consulta= "SELECT " +
				"consecutivo," +
				"codigo," +
				"descripcion," +
				"cuenta_contable_debito AS cuenta_con_deb," +
				"activo," +
				"cuenta_contable_credito AS cuenta_con_cre," +
				"tercero," +
				"tipo_concepto " +
				"FROM conceptos_facturas_varias " +
				"WHERE institucion = ? "+
			    "AND tipo_concepto=?"+
				"AND activo=?";
				
		
		logger.info(consulta);
		try
		{
			
			
			consulta+=" ORDER BY descripcion ASC ";
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, institucion);
			pst.setString(2,tipoConcepto);
			
			if(activo)
			{
				pst.setString(3, ConstantesBD.acronimoSi);
			}
			else
			{
				pst.setString(3, ConstantesBD.acronimoNo);
			}
			
				
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{		
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("consecutivo", rs.getObject("consecutivo"));
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("descripcion", rs.getObject("descripcion"));
				mapa.put("cuenta_contable_debito", rs.getObject("cuenta_con_deb"));
				mapa.put("activo", rs.getObject("activo"));
				mapa.put("cuenta_contable_credito", rs.getObject("cuenta_con_cre"));
				mapa.put("tercero", rs.getObject("tercero"));
				mapa.put("tipo_concepto", rs.getObject("tipo_concepto"));
				list.add(mapa);
			}
			rs.close();
			pst.close();
		}catch(SQLException e){
			logger.info("Error ObtenerConceptosFraVarias !!!!!!\n La Consulta: "+consulta);
			logger.error(e);			
		}
		return list;
	}
	
	
	
	/**
	 * 
	 * @param consecutivoFacturaVaria
	 * @param institucion
	 * @return
	 */
	public static BigDecimal obtenerPkFacturaVaria( BigDecimal consecutivoFacturaVaria , int institucion){
		
		String consulta= "select codigo_fac_var as codigoPkFactura from facturasvarias.facturas_varias where consecutivo=? and institucion=?";
		
		logger.info("\n\n\n");
		logger.info("\t\t OBTENER CODIGO PK FACTURA VARIA");
		
		logger.info("\n\n\n");
		
		BigDecimal nRetorna= BigDecimal.ZERO;
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst=new PreparedStatementDecorator(con , consulta);
			pst.setInt(1, consecutivoFacturaVaria.intValue());
			pst.setInt(2,institucion);
			
			logger.info(pst);
			logger.info("\n\n\n");
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{		
				nRetorna=rs.getBigDecimal("codigoPkFactura");
			}
			UtilidadBD.cerrarObjetosPersistencia(null, rs, con);
		}
		
		catch(SQLException e){
			logger.info("Error ObtenerConceptosFraVarias !!!!!!\n La Consulta: "+consulta);
			logger.error(e);			
		}
		
		return nRetorna;
		
	}
	
	
}
