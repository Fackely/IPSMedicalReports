/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.capitacion.CierresCarteraCapitacion;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Implementación sql genérico de todos los metodos de acceso 
 * a la fuente de datos para cierre cartera capitacion
 *
 * @version 1.0,
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class SqlBaseCierresCarteraCapitacionDao 
{
	 /**
	 * Variable para manejar los log de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCierresCarteraCapitacionDao.class);

	/**
	 * consulta del resumen de la generación de cierre capitacion
	 */
	private final static String resumenCierreCapitacionStr=	"select anio_cierre as year," +
															"mes_cierre as mes," +
															"fecha_generacion as fecha_generacion," +
															"substr(hora_generacion, 0, 6) as hora_generacion," +
															"usuario as usuario," +
															"observaciones as observaciones " +
															"from cierres_saldos_capitacion " +
															"where codigo=? and institucion=?";
	
	/**
	 * verifica la existencia de cuentas de cobro radicadas dada una fecha y mes cierre
	 */
	private final static String existenCxCCapitadasRadicadasDadaFechaStr="SELECT numero_cuenta_cobro FROM cuentas_cobro_capitacion where estado="+ConstantesBD.codigoEstadoCarteraRadicada+" and to_char(fecha_inicial,'yyyy-mm')<=? and to_char(fecha_final,'yyyy-mm')<=? and institucion=?";
	
	 /**
	 * Obtiene el último cod de la sequence 
	 */
	private final static String ultimaSequenciaStr= "SELECT MAX(codigo) AS seq_cierres_saldos_cap FROM cierres_saldos_capitacion where institucion = ? and tipo_cierre = ? ";
	
	/**
	 * inserta el cierre de saldo inicial
	 */
	private final static String insertCierreCarteraCapitacionStr=" insert into cierres_saldos_capitacion (codigo, institucion, anio_cierre, mes_cierre, observaciones, tipo_cierre, fecha_generacion, hora_generacion, usuario) values(?, ?, ?, ?, ?, ?, CURRENT_DATE, ?, ?)";
	
	/**
	 * Inserta el detalle del cierre cartera x convenio
	 */
	private final static String insertDetalleCierreCarteraXConvenioStr=" INSERT INTO cierre_saldos_conv_capita (cierre_saldo_capita, convenio, valor_cartera, valor_ajuste_debito, valor_ajuste_credito, valor_pago) VALUES(?, ?, ?, ?, ? , ?) ";
	
	/**
	 * metodo para evaluar si las CxC estan radicadas para un anio dado
	 */
	private final static String estanCxCRadicadasParaCierreAnualStr=" SELECT numero_cuenta_cobro as numerocuentacobro FROM cuentas_cobro_capitacion WHERE institucion= ? and estado not in("+ConstantesBD.codigoEstadoCarteraRadicada+","+ConstantesBD.codigoEstadoCarteraAnulado+") and to_char(fecha_elaboracion, 'yyyy')=?";
	
	/**
	 * Metodo paa consultar el cierre insertado
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo  
	 * @param codigoInstitucion
	 * @return ResultSet
	 */
	public static ResultSetDecorator cargarResumenCierreCarteraCapitacion(Connection con, String codigo, int codigoInstitucion)
	{
	    PreparedStatementDecorator ps;
	    try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement(resumenCierreCapitacionStr));
            ps.setDouble(1,Utilidades.convertirADouble(codigo));
            ps.setInt(2, codigoInstitucion);
            return new ResultSetDecorator(ps.executeQuery());
        } 
        catch (SQLException e) 
        {        
            e.printStackTrace();
            return null;
        }	   	    
	}

	
	/**
	 * metodo para consultar el ultimo código
	 * de la secuencia 
	 * @param con Connection, conexión con la fuente de datos
	 * @return ResultSet
	 */
	public static ResultSetDecorator ultimoCodigoCierre (Connection con,int institucion, String tipoCierre)
	{
	    try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(ultimaSequenciaStr));
            ps.setInt(1,institucion);
            ps.setString(2, tipoCierre);
            return new ResultSetDecorator(ps.executeQuery());
        } 
	    catch (SQLException e) 
        {           
            e.printStackTrace();
            return null;
        } 
	}
	
	/**
	 * metodo que verifica la existencia de cuentas de cobro radicadas dada una fecha y mes cierre
	 * @param con
	 * @param yearCierre
	 * @param mesCierre
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existenCxCCapitadasRadicadasDadaFecha(Connection con, String yearCierre, String mesCierre, int codigoInstitucion )
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs= null;
		try 
	    {
            ps= new PreparedStatementDecorator(con.prepareStatement(existenCxCCapitadasRadicadasDadaFechaStr));
            ps.setString(1, yearCierre+"-"+mesCierre);
            ps.setString(2, yearCierre+"-"+mesCierre);
            ps.setInt(3, codigoInstitucion);
            rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	return true;
        } 
		catch (SQLException e) 
		{           
            e.printStackTrace();
        }finally{
			
        	try{
        		if(ps!=null){
        			ps.close();					
        		}
        	}catch(SQLException sqlException){
        		logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
        	}
        	
        	try{
        		if(rs!=null){
        			rs.close();					
        		}
        	}catch(SQLException sqlException){
        		logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
        	}
		}
		return false;
	}
	
	/**
	 * metodo que inserta el cierre de cartera capitacion
	 * RETORNA el codigo insertado
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @param mesCierre
	 * @param observaciones
	 * @param tipoCierre
	 * @param loginUsuario
	 * @return
	 */
	public static int insertCierreCarteraCapitacion(Connection con, int codigoInstitucion, String yearCierre, String mesCierre, String observaciones, String tipoCierre, String loginUsuario )
	{
		PreparedStatementDecorator ps= null;
		try
		{
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_cierres_saldos_cap");
			ps=  new PreparedStatementDecorator(con.prepareStatement(insertCierreCarteraCapitacionStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ps.setInt(2, codigoInstitucion);
			ps.setInt(3, Utilidades.convertirAEntero(yearCierre));
			ps.setInt(4, Utilidades.convertirAEntero(mesCierre));
			ps.setString(5, observaciones);
			ps.setString(6, tipoCierre);
			ps.setObject(7, UtilidadFecha.getHoraSegundosActual());
			ps.setString(8, loginUsuario);
			if(ps.executeUpdate()>0)
				return codigo;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
			}
			
		}
		return -1;
	}
	
	/**
	 * Inserta el detalle del cierre cartera x convenio
	 * @param con
	 * @param codigoCierre
	 * @param codigoConvenio
	 * @param valorCartera
	 * @param valorAjusteDebito
	 * @param valorAjusteCredito
	 * @param valorPago
	 * @return
	 */
	public static boolean insertDetalleCierreCarteraXConvenio(Connection con, String codigoCierre, int codigoConvenio, String valorCartera, String valorAjusteDebito, String valorAjusteCredito, String valorPago )
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(insertDetalleCierreCarteraXConvenioStr));
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoCierre));
			ps.setInt(2, codigoConvenio);
			ps.setDouble(3, Utilidades.convertirADouble(valorCartera));
			ps.setDouble(4, Utilidades.convertirADouble(valorAjusteDebito));
			ps.setDouble(5, Utilidades.convertirADouble(valorAjusteCredito));
			ps.setDouble(6, Utilidades.convertirADouble(valorPago));
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	/**
	 * metodo que evalua si existe o no cierre para una anio y fecha dados
	 * @param con
	 * @param yearCierre
	 * @param tipoCierre
	 * @return
	 */
	public static boolean existeCierre(Connection con, String yearCierre, String tipoCierre, int codigoInstitucion)
	{
		String existeCierreStr=" SELECT codigo FROM cierres_saldos_capitacion WHERE tipo_cierre=? and institucion=? ";
		if(!yearCierre.equals(""))
		{
			existeCierreStr+=" and anio_cierre="+yearCierre;
		}
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(existeCierreStr));
			ps.setString(1, tipoCierre);
			ps.setInt(2, codigoInstitucion);
			if(ps.executeQuery().next())
				return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
			}
			
		}
		return false;
	}
	
	/**
	 * metodo que obtiene los valores de los pagos - ajustes debito credito y valor cartera - para posteriormente insertarlos en el cierre 
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @param mesCierre
	 * @return
	 */
	public static HashMap getPagosAjustesValorCarteraXConvenio(Connection con, int codigoInstitucion, String yearCierre, String mesCierre)
	{
		HashMap mapa= new HashMap();
		String fecha=yearCierre+"-"+mesCierre;
		String consulta=	"SELECT convenio as codigoconvenio, " +
							"CASE WHEN sum(saldo_cuenta) IS NULL THEN 0 ELSE sum(saldo_cuenta) END as valorcartera, " +
							"getValorPagosApliCxC2Cierre(convenio, "+ConstantesBD.codigoEstadoCarteraRadicada+", '"+fecha+"', "+codigoInstitucion+") as valorpagos, " +
							"getValorAjustesApliCxC2Cierre(convenio, "+ConstantesBD.codigoEstadoCarteraRadicada+", '"+fecha+"', "+codigoInstitucion+", "+ConstantesBD.codigoAjusteDebitoCuentaCobro+") as valorajustesdebito,  " +
							"getValorAjustesApliCxC2Cierre(convenio, "+ConstantesBD.codigoEstadoCarteraRadicada+", '"+fecha+"', "+codigoInstitucion+", "+ConstantesBD.codigoAjusteCreditoCuentaCobro+") as valorajustescredito   " +
							"from " +
							"cuentas_cobro_capitacion " +
							"where " +
							"estado="+ConstantesBD.codigoEstadoCarteraRadicada+" " +
							"and to_char(fecha_inicial,'yyyy-mm')<='"+fecha+"' " +
							"and to_char(fecha_final,'yyyy-mm')<='"+fecha+"' " +
							"and institucion="+codigoInstitucion+" group by convenio";
		PreparedStatementDecorator ps= null;
		
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	
	/**
	 * metodo para evaluar si las CxC estan radicadas para un anio dado
	 * @param con
	 * @param yearCierre
	 * @return
	 */
	public static HashMap estanCxCRadicadasParaCierreAnual (Connection con, String yearCierre, int codigoInstitucion)
	{
		PreparedStatementDecorator ps= null;
		HashMap mapa= new HashMap();
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(estanCxCRadicadasParaCierreAnualStr));
			ps.setInt(1, codigoInstitucion);
			ps.setString(2, yearCierre);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	/**
	 * metodo que obtiene el resumen de los valores de los cierres de cartera para un anio y tipo cierre dado
	 * @param con
	 * @param yearCierre
	 * @param codigoInstitucion
	 * @param tipoCierre
	 * @return
	 */
	public static HashMap getResumenValoresCierresCartera(Connection con, String yearCierre, int codigoInstitucion, String tipoCierre)
	{
		String consulta=" select " +
						"csc.mes_cierre as mescierre, " +
						"cscc.convenio as codigoconvenio, " +
						"cscc.valor_cartera as valorcartera, " +
						"cscc.valor_ajuste_debito as valorajustedebito, " +
						"cscc.valor_ajuste_credito as valorajustecredito, " +
						"cscc.valor_pago as valorpago " +
						"from " +
						"cierre_saldos_conv_capita cscc " +
						"INNER JOIN cierres_saldos_capitacion csc ON (cscc.cierre_saldo_capita=csc.codigo) " +
						"WHERE csc.institucion= " +codigoInstitucion+" "+
						"and csc.anio_cierre='"+Utilidades.convertirAEntero(yearCierre)+"' and csc.tipo_cierre='"+tipoCierre+"'";
		
		HashMap mapa= new HashMap();
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	/**
	 * metodo que obtiene los valores de los pagos - ajustes debito credito y valor cartera -  para el cierre anual
	 *  
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @return
	 */
	public static HashMap getPagosAjustesValorCarteraXConvenioCierreAnual(Connection con, int codigoInstitucion, String yearCierre)
	{
		String fechaInicialYYYY_MM=""; 
		String fechaFinalYYYY_MM="";
		String tipoCierre="";
		
		if(CierresCarteraCapitacion.existeCierre((Integer.parseInt(yearCierre)-1)+"", ConstantesBD.codigoTipoCierreAnualStr, codigoInstitucion))
    	{
    		fechaInicialYYYY_MM=yearCierre+"-01";
    		fechaFinalYYYY_MM=yearCierre+"-12";
    		tipoCierre=ConstantesBD.codigoTipoCierreAnualStr;
    	}
    	//3.2 en caso contrario entonces se toma la informacion del cierre de saldos iniciales del anio del cierre 
    	else
    	{
    		HashMap mapaValoresIniciales=CierresCarteraCapitacion.getResumenValoresCierresCartera(yearCierre, codigoInstitucion, ConstantesBD.codigoTipoCierreSaldoInicialStr);
    		fechaInicialYYYY_MM=yearCierre+"-"+mapaValoresIniciales.get("mescierre_0").toString();;
    		fechaFinalYYYY_MM=yearCierre+"-12";
    		tipoCierre=ConstantesBD.codigoTipoCierreSaldoInicialStr;
    	}
		
		HashMap mapa= new HashMap();
		String consulta=	"SELECT " +
								"codigoconvenio, " +
								"sum(valorcartera) as valorcartera, " +
								"sum(valorpagos) as valorpagos, " +
								"sum(valorajustesdebito) as valorajustesdebito, " +
								"sum(valorajustescredito) as valorajustescredito " +
								"from " +
								"( "+
								//tabla temporal
									"(" +
										"SELECT " +
										"convenio as codigoconvenio, " +
										"CASE WHEN sum(saldo_cuenta) IS NULL THEN 0 ELSE sum(saldo_cuenta) END as valorcartera, " +
										"getValorPagosApliCxC2Anual(convenio, "+ConstantesBD.codigoEstadoCarteraRadicada+", '"+fechaInicialYYYY_MM+"', '"+fechaFinalYYYY_MM+"', "+codigoInstitucion+") as valorpagos, " +
										"getValorAjustesApliCxC2Anual(convenio, "+ConstantesBD.codigoEstadoCarteraRadicada+", '"+fechaInicialYYYY_MM+"', '"+fechaFinalYYYY_MM+"', "+codigoInstitucion+", "+ConstantesBD.codigoAjusteDebitoCuentaCobro+") as valorajustesdebito,  " +
										"getValorAjustesApliCxC2Anual(convenio, "+ConstantesBD.codigoEstadoCarteraRadicada+", '"+fechaInicialYYYY_MM+"', '"+fechaFinalYYYY_MM+"', "+codigoInstitucion+", "+ConstantesBD.codigoAjusteCreditoCuentaCobro+") as valorajustescredito   " +
										"from " +
										"cuentas_cobro_capitacion " +
										"where " +
										"estado="+ConstantesBD.codigoEstadoCarteraRadicada+" " +
										"and to_char(fecha_inicial,'yyyy-mm')>='"+fechaInicialYYYY_MM+"' " +
										"and to_char(fecha_inicial,'yyyy-mm')<='"+fechaFinalYYYY_MM+"' " +
										"and to_char(fecha_final,'yyyy-mm')>='"+fechaInicialYYYY_MM+"' " +
										"and to_char(fecha_final,'yyyy-mm')<='"+fechaFinalYYYY_MM+"' " +
										"and institucion="+codigoInstitucion+" group by convenio " +
								     ")" +
								     " UNION ALL " +
								     "(" +
								     	"select " +
										"cscc.convenio as codigoconvenio, " +
										"cscc.valor_cartera as valorcartera, " +
										"cscc.valor_ajuste_debito as valorajustedebito, " +
										"cscc.valor_ajuste_credito as valorajustecredito, " +
										"cscc.valor_pago as valorpago " +
										"from " +
										"cierre_saldos_conv_capita cscc " +
										"INNER JOIN cierres_saldos_capitacion csc ON (cscc.cierre_saldo_capita=csc.codigo) " +
										"WHERE csc.institucion= " +codigoInstitucion+" "+
										"and csc.anio_cierre='"+yearCierre+"' and csc.tipo_cierre='"+tipoCierre+"' " +
									")" +
								") tabla group by codigoconvenio ";
	
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseCierresCarteraCapitacionDao "+sqlException.toString() );
			}
			
		}
		return mapa;
	}
	
	
}