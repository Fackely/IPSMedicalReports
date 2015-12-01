
/*
 * Creado   14/07/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;


/**
 * Implementación sql genérico de todos los metodos de acceso 
 * a la fuente de datos para cierre saldo inicial cartera
 *
 * @version 1.0, 14/07/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseCierreSaldoInicialCarteraDao 
{
    /**
	 * Variable para manejar los log de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCierreSaldoInicialCarteraDao.class);
	
    /**
     * consulta para obtener las facturas menores a un rango de fecha dado
     */
    private static String obtenerFacturasCierreStr="SELECT fact.codigo as codigofact," +
    		"fact.consecutivo_factura as consecutivo_factura," +
    		"fact.fecha as fecha," +
    		"fact.convenio as codigoconvenio," +
    		"fact.numero_cuenta_cobro," +   		
    		"valor_cartera as valor_cartera," +
    		"valor_pagos as valor_pagos," +
    		"ajustes_credito as ajustes_credito," +
    		"ajustes_debito as ajustes_debito " +    		
    		"from facturas fact " +
    		"inner join convenios con on(con.codigo=fact.convenio) "+
    		"where "+
    		" fact.institucion = ? and factura_cerrada = "+ValoresPorDefecto.getValorFalseParaConsultas() +
    		" and tipo_contrato <> "+ConstantesBD.codigoTipoContratoCapitado+" ";
    
    /**
     * filtro de cierre inicial
     */
    private static String cierreInicialStr=" AND (fact.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
    		"OR fact.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+") " +
    		"and to_char(fecha, 'yyyy-mm') <= ? order by fact.convenio";
    
    /**
     * filtro de cierre mensual
     */
    private static String cierreMensualStr=" AND fact.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" and to_char(fecha, 'yyyy-mm') = ? order by fact.convenio";
    
        
    /**
     * query para insertar los valores del cierre de saldo inicial por convenio.
     */
    private static String insertarCierreSaldoXConvenioStr=" INSERT INTO cierre_saldos_convenio" +
																					    		"(cierre_saldo,convenio," +
																					    		"valor_cartera,valor_ajuste_debito," +
																					    		"valor_ajuste_credito,valor_pago) " +
																					    		"VALUES (?,?,?,?,?,?)";
    
    /**
	 * Obtiene el último cod de la sequence 
	 */
	private final static String ultimaSequenciaStr= "SELECT MAX(codigo) AS seq_cierres_saldos FROM cierres_saldos where institucion = ?";
	
	
	/**
	 * consulta del resumen de la generación de cierre
	 */
	private final static String resumenCierreStr="select anio_cierre as year," +
			"mes_cierre as mes," +
			"fecha_generacion as fecha_generacion," +
			"hora_generacion as hora_generacion," +
			"usuario as usuario," +
			"observaciones as observaciones from cierres_saldos where codigo=?";
	
	/**
	 * consultar si existe cierre anual.
	 */
	private final static String existeCierreAnulaMensualStr="SELECT COUNT(1) as cierre FROM cierres_saldos where " +
			"anio_cierre=? AND mes_cierre=? " +
			"AND institucion=? AND tipo_cierre=?";
	
	/**
	 * cosulta para el ultimo registro de cierre cartea realizado
	 */
	private final static String ultimoCierreRealizadoStr="SELECT MAX(mes_cierre) as mes_cierre FROM cierres_saldos WHERE anio_cierre=? and tipo_cierre=? and institucion=?";
	
	/**
	 * query para las facturas anuladas.
	 */
	private final static String facturasAnuladasStr="SELECT a.codigo as codigo,f.valor_cartera as valor_cartera," +
			"f.convenio as convenio from anulaciones_facturas a " +
			"inner join facturas f on(a.codigo=f.codigo) " +
			"where to_char(a.fecha_grabacion,'yyyy-mm')=? and to_char(f.fecha,'yyyy-mm')<? " +
			"AND estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+" AND a.institucion=?";


	/**
	 * Query para consultar los meses de cierre mensual
	 */
	private final static String cierresMensualesStr="SELECT mes_cierre as mes_cierre from cierres_saldos where anio_cierre=? AND tipo_cierre=? AND institucion=?";
	
	/**
	 * Query para consultar los valores del cierre
	 */
	private final static String valoresCierresAnioStr=" SELECT convenio as convenio," +
			"sum(valor_cartera) as valor_cartera," +
			"sum(valor_ajuste_debito) as valor_ajuste_debito," +
			"sum(valor_ajuste_credito) as valor_ajuste_credito," +
			"sum(valor_pago) as valor_pago  " +
			"FROM cierre_saldos_convenio csc " +
			"inner join cierres_saldos cs on (csc.cierre_saldo=cs.codigo) " +
			"where cs.institucion=? AND anio_cierre=? group by convenio";
			
	/**
	 * Query para consultar el codigo del cierre que sera eliminado
	 */
	private final static String codigoCierresSaldoActualizarStr="SELECT codigo as codigo_cierre FROM cierres_saldos " +
			"WHERE anio_cierre=? AND mes_cierre=? AND tipo_cierre='"+ConstantesBD.codigoTipoCierreMensualStr+"' AND institucion=?";
	
	/**
	 * Query para eliminar los valores de cierres por convenio
	 */
	private final static String eliminarCierreSaldoXConvenioStr="DELETE FROM cierre_saldos_convenio WHERE cierre_saldo=?";
	
	/**
	 * Query para eliminar los datos de cierres saldos
	 */
	private final static String eliminarCierresSaldosStr="DELETE FROM cierres_saldos WHERE codigo = ?";
	
	/**
	 * Consulta todos los cierres mensuales detallados.
	 * La funcion <code>getsaldoanteriorofinalcarteraxconvenio(int,int,int,int,int)</code> el ultimo parametro
	 * que recibe es un indicador para realizar la consulta de saldoFinal o saldoAnterior,
	 * para saldoFinal se envia 0 y para saldoAnterior 1.
	 */
	private final static String consultarCierresMensualesXConvenioStr="SELECT csc.convenio AS convenio," +
			"csc.valor_cartera AS valor_cartera," +
			"csc.valor_ajuste_debito AS valor_ajuste_debito," +
			"csc.valor_ajuste_credito AS valor_ajuste_credito," +
			"csc.valor_pago AS valor_pago," +
			"cs.anio_cierre AS anio_cierre," +
			"cs.mes_cierre AS mes_cierre," +
			"cs.codigo as codigo_cierre, " +
			"getsalantcarteraxconvenio(csc.convenio,cs.anio_cierre,cs.mes_cierre,cs.institucion) as saldo_Anterior," +
			"getsalantcarteraxconvenio(csc.convenio,cs.anio_cierre,cs.mes_cierre,cs.institucion)+ csc.valor_cartera + csc.valor_ajuste_debito -  csc.valor_ajuste_credito - csc.valor_pago as saldo_Final, " +
			"getNombreEmpresaFact(csc.convenio,?) as nombre_empresa," +
			"getnombreconvenio(csc.convenio) as nombre_convenio " +
			"FROM cierre_saldos_convenio csc " +
			"INNER JOIN cierres_saldos cs on(cs.codigo=csc.cierre_saldo)  " +
			"WHERE cs.tipo_cierre in ('"+ConstantesBD.codigoTipoCierreMensualStr+"','"+ConstantesBD.codigoTipoCierreSaldoInicialStr+"') AND cs.institucion=? ";
	   
		
	/**
     * Metodo para obtener las facturas menores a una fecha,
     * segun la institucion, que aun no tengan cierre, y se 
     * encuentren en estado facturadas.
	 * @param con
	 * @param fecha String
	 * @param institucion int 
	 * @param esCierreInicial boolean, true para cierre inicial, false para cierre mensual
	 * @return ResultSet
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#cargarFacutras(java.sql.Connection,String,int)
	 * @author jarloc
	 */
	public static ResultSetDecorator cargarFacutras(Connection con, String fecha, int institucion,boolean esCierreInicial) 
	{
		try
		{
			String consulta="";
			if(esCierreInicial)
			{	
			    consulta = obtenerFacturasCierreStr + cierreInicialStr;
			    
			    logger.info("LA CONSULTA 1  >>>> "+consulta);
			    
			}  
			else
			{	
			    consulta = obtenerFacturasCierreStr + cierreMensualStr;
			    logger.info("LA CONSULTA 2 >>>> "+consulta);
			}   
			
		    PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    logger.info("LA FECHA >>>>> "+fecha);
		    
			pst.setInt(1,institucion);
			pst.setString(2,fecha);			
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error consultando Facturas"+e);
		}
		return null;
	}
	
	/**
	 * Metodo implementado para insertar los datos generales
	 * de un cierre de saldos iniciales de cartera.
	 * @param con Connection, conexion con la fuente de datos
	 * @param year String, año del cierre
	 * @param mes String, mes del cierre
	 * @param obser String, observaciones
	 * @param tipoCierre String, tipo de cierre
	 * @param usuario String, login del usuario
	 * @param institucion int,codigo de la institución
	 * @param query String, Cadena con la consulta
	 * @return boolean, true inserto.
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#insertarCierresSaldos(java.sql.Connection,String,String,String,String,String,int,String)
	 * @author jarloc
	 */
	public static boolean insertarCierresSaldos(Connection con, 
												        String year,String mes,
												        String obser,String tipoCierre,
												        String usuario,int institucion,
												        String query)
	{
	    int r=0;
	    try
	    {
	      PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	      ps.setInt(1,institucion);
	      ps.setString(2,year);
	      ps.setString(3,mes);
	      ps.setString(4,obser);
	      ps.setString(5,tipoCierre);
	      ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
	      ps.setString(7,UtilidadFecha.getHoraActual());
	      ps.setString(8,usuario);	
	      r = ps.executeUpdate();
	      if(r<0)
	          return false;
	      else
	          return true;
	    }
	    catch(SQLException e)
	    {
			logger.error(e+"Error insertarCierresSaldos"+e.toString());
			return false;
		}	    
	}
	
	/**
	 * metodo implementado para insertar los valores 
	 * por convenio, de un cierre de saldos iniciales
	 * de cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codCierre int, código del cierre
	 * @param codConvenio int, código del convenio
	 * @param vlrCartera double, valor de cartera
	 * @param vlrAjusteDebito double, valor de ajustes debito
	 * @param vlrAjusteCredito double, valor de ajustes credito
	 * @param vlrPago double, valor de pagos	 
	 * @return boolean, true si inserto.
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#cargarFacutras(java.sql.Connection,int,int,double,double,double,double)
	 * @author jarloc
	 */
	public static boolean insertarValoresCierreXConvenio(Connection con, 
															        int codCierre,int codConvenio,
															        double vlrCartera,double vlrAjusteDebito, 
															        double vlrAjusteCredito, double vlrPago)
	{
	    int r=0;
	    try
	    {
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarCierreSaldoXConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 
	        ps.setInt(1,codCierre);
	        ps.setInt(2,codConvenio);
	        ps.setDouble(3,vlrCartera);
	        ps.setDouble(4,vlrAjusteDebito);
	        ps.setDouble(5,vlrAjusteCredito);
	        ps.setDouble(6,vlrPago);
	        r = ps.executeUpdate();
	        if(r<0)
	            return false;
	        else 
	            return true;
	    }
	    catch(SQLException e)
	    {
			logger.error(e+"Error insertarValoresCierreXConvenio"+e.toString());
			return false;
		}    
	}
	
	/**
	 * metodo para consultar el ultimo código
	 * de la secuencia 
	 * @param con Connection, conexión con la fuente de datos
	 * @return ResultSet
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#ultimoCodigoCierre(java.sql.Connection)
	 * @author jarloc
	 */
	public static ResultSetDecorator ultimoCodigoCierre (Connection con,int institucion)
	{
	    try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(ultimaSequenciaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            return new ResultSetDecorator(ps.executeQuery());
        } catch (SQLException e) {           
            e.printStackTrace();
            return null;
        } 
	}
	
	/**
	 * metodo para cerrar las facturas,
	 * actualizado el campo de cierre en false
	 * @param con Connection con la fuente de datos
	 * @param query String, cadena con la consulta segun 
	 * sea para oracle o postgres
	 * @return boolean, true si actualizo
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#cerrarFacturas(java.sql.Connection)
	 * @author jarloc
	 */
	public static boolean cerrarFacturas (Connection con, String query,int codFact)
	{
	    int r = 0;
	    try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codFact);
            r=ps.executeUpdate();
            if(r<=0)
	            return false;
	        else 
	            return true;
        } catch (SQLException e) {           
            e.printStackTrace();
            return false;
        }	    
	}
	
	/**
	 * Metodo paa consultar el cierre insertado
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo  int
	 * @return ResultSet
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#cargarResumenCierre(java.sql.Connection)
	 * @author jarloc
	 */
	public static ResultSetDecorator cargarResumenCierre(Connection con, int codigo)
	{
	    PreparedStatementDecorator ps;
        try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement(resumenCierreStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigo);
            return new ResultSetDecorator(ps.executeQuery());
        } catch (SQLException e) 
        {        
            e.printStackTrace();
            return null;
        }	   	    
	}
	
	/**
	 * Metodo para consultar si ya fue ejecutado
	 * un cierre anual o mensual.
	 * @param con Connection, conexión con la fuente de datos
	 * @param year String, año del cierre
	 * @param mes String, mes del cierre
	 * @param tipoCierre String, tipo del cierre (Anual-Inicial-Mensual)
	 * @param institucion int, código de la institución
	 * @return boolean, true si posee cierre
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#existeCierreAnualMensual(java.sql.Connection,int,int,String,int)
	 * @author jarloc
	 */
	public static boolean existeCierreAnualMensual(Connection con,int year,int mes,String tipoCierre,int institucion)
	{
	    PreparedStatementDecorator ps;
	    try 
        {
	        ps =  new PreparedStatementDecorator(con.prepareStatement(existeCierreAnulaMensualStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,year);
	        ps.setInt(2,mes);
	        ps.setInt(3,institucion);
	        ps.setString(4,tipoCierre);	 
	        ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	        {
	            if(rs.getInt("cierre")!=0)
	                return true;
	            else
	                return false; 
	        }
	        else
	            return false;
        }
	    catch (SQLException e) 
        {        
            e.printStackTrace();
            return false;
        }       
	}
	
	/**
	 * 
	 * @param con Connection, conexión con la fuente de datos
	 * @param year int, año del cierre
	 * @param tipoCierre String, tipo del cierre(Anual-Mensual-Inicial)
	 * @param institucion int, código de la institución.
	 * @return int, mes del cierre
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#ultimoCierreGenerado(java.sql.Connection,int,String,int)
	 * @author jarloc
	 */
	public static int ultimoCierreGenerado(Connection con,int year,String tipoCierre,int institucion)
	{
	    PreparedStatementDecorator ps;
	    try 
        {
	        ps =  new PreparedStatementDecorator(con.prepareStatement(ultimoCierreRealizadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
	        ps.setInt(1,year);
	        ps.setString(2,tipoCierre);
	        ps.setInt(3,institucion);
	        ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	            return rs.getInt("mes_cierre");
	        else
	            return 0; 
	                
        }
	    catch (SQLException e) 
        {        
            e.printStackTrace();   
            return 0;
        } 
	}
	
	/**
	 * Metodo para consultar las facturas que fueron anuladas
	 * en el mes del cierre y que poseen fecha de generacion de
	 * la factura anterior a la fecha del cierre.
	 * @param con Connection, conexión con la fuente de datos
	 * @param institucion int, código de la institución
	 * @param fecha String, fecha del cierre
	 * @return ResultSet
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#facturasAnuladasEnMesDeCierre(java.sql.Connection,int,String)
	 * @author jarloc
	 */
	public static ResultSetDecorator facturasAnuladasEnMesDeCierre(Connection con, int institucion, String fecha)
	{
	    try
	    {
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(facturasAnuladasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));  
	        ps.setString(1,fecha);
	        ps.setString(2,fecha);
	        ps.setInt(3,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch (SQLException e) 
        {        
            e.printStackTrace();   
            return null;
        }  
	}
	
	/**
	 * Metodo para consultar los meses de cierre
	 * generados en el año.
	 * @param con Connection
	 * @param institucion in
	 * @param tipoCierre String
	 * @param anioCierre int
	 * @return ResultSet
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#cierresMensuales(java.sql.Connection,int,String,int)
	 * @author jarloc 
	 */
	public static ResultSetDecorator cierresMensuales (Connection con,int institucion,String tipoCierre,int anioCierre)
	{
	    try
	    {
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cierresMensualesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,anioCierre);
	        ps.setString(2,tipoCierre);
	        ps.setInt(3,institucion);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch (SQLException e) 
        {        
            e.printStackTrace();   
            return null;
        }  
	}
	
	/**
	 * Metodo para consultar el codigo del cierre
	 * que sera eliminado.
	 * @param con Connection
	 * @param yearCierre int
	 * @param mesCierre int
	 * @param institucion int
	 * @return int, codigo del cierre
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#obtenerCodigoCierreActualizar(java.sql.Connection,int,int,int)
	 * @author jarloc
	 */
	public static int obtenerCodigoCierreActualizar (Connection con, int yearCierre,int mesCierre,int institucion)
	{	    
	    try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(codigoCierresSaldoActualizarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,yearCierre);
            ps.setInt(2,mesCierre);
            ps.setInt(3,institucion);
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
                return rs.getInt("codigo_cierre");
            else
                return ConstantesBD.codigoNuncaValido;
            
        } catch (SQLException e) 
        {       
            e.printStackTrace();
            return ConstantesBD.codigoNuncaValido;
        }        	    
	}
	
	/**
	 * Metodo para realizar la eliminación en la
	 * tabla de cierres_saldos,cuado se recalculan los
	 * valores por convenio de un cierre mensual.
	 * @param con Connection 
	 * @param codigoCierre int
	 * @return boolean, true si realizo actualizacion
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#actualizarCierresSaldos(java.sql.Connection,int)
	 * @author jarloc
	 */
	public static boolean eliminarCierresSaldos(Connection con,int codigoCierre)
	{
	    int r=0;
	    try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarCierresSaldosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoCierre);
            r=ps.executeUpdate();
            if(r<=0)
                return false;
            else
                return true;
        } catch (SQLException e) 
        {            
            e.printStackTrace();
            return false;
        }	    
	}
	
	/**
	 * Metodo para realizar la eliminación de
	 * los valoeres de la tabla de cierre_saldos_convenio
	 * @param con Connection	  
	 * @param codigoCierre int	 
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#actualizarCierresSaldosXConvenio(java.sql.Connection,int)
	 * @author jarloc
	 */
	public static boolean eliminarCierresSaldosXConvenio(Connection con,int codigoCierre)
	{
	    int r=0;
	    try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarCierreSaldoXConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            ps.setInt(1,codigoCierre);            
            r=ps.executeUpdate();
            if(r<=0)
                return false;
            else
                return true;
        } catch (SQLException e) 
        {
            e.printStackTrace();
            return false;
        }    
	}
	
	/**
	 * Metodo para consultar los cierres que se 
	 * generaron durante el año
	 * @param con Connection 
	 * @param institucion int 
	 * @param anioCierre int
	 * @return ResultSet
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#consultarCierresGeneradosEnElAnio(java.sql.Connection,int,int)
	 * @author jarloc
	 */
	public static ResultSetDecorator consultarCierresGeneradosEnElAnio(Connection con,int institucion,int anioCierre)
	{
	    try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(valoresCierresAnioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            ps.setInt(2,anioCierre);
            return new ResultSetDecorator(ps.executeQuery());
        } catch (SQLException e) 
        {            
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * Metodo para realizar la busqueda avanzada
	 * de los cierre de cartera mensuales, filtarndo
	 * por convenio,año ó mes.
	 * @param con Connection
	 * @param anioCierre int
	 * @param mesCierre int 
	 * @param institucion int
	 * @param convenio String[] 
	 * @return ResultSet
	 * @see com.princetonsa.dao.CierreSaldoInicialCarteraDao#consultaAvanzadaCierresMensuales(java.sql.Connection,int,int,int,String[])
	 * @author jarloc
	 */
	public static ResultSetDecorator consultaAvanzadaCierresMensuales(Connection con,int anioCierre,int mesCierre,int institucion,String[] convenio)
	{
	    ResultSetDecorator rs = null;
	    String consulta=consultarCierresMensualesXConvenioStr;
	    String order=" ORDER BY cs.mes_cierre";	    
	    try 
	    {         
	        if(convenio.length!=0)
	        {
	          consulta+=" AND ( ";
	          for(int i=0;i<convenio.length;i++)
	          {
	              consulta+="csc.convenio="+Integer.parseInt(convenio[i]);
	              if(i<convenio.length-1)
	                  consulta+=" OR ";	              
	          }
	          consulta+=" ) ";
	        }
	        if(anioCierre!=-1)
	            consulta+=" AND cs.anio_cierre="+anioCierre;
	        if(mesCierre!=-1)
	            consulta+=" AND cs.mes_cierre="+mesCierre;	                
	      
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta+order,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,ConstantesBD.codigoTipoRegimenParticular+"");
            ps.setInt(2,institucion);
            return new ResultSetDecorator(ps.executeQuery());
	    }
		 catch (SQLException e) 
	    {            
	        e.printStackTrace();	        
	    }
	    return rs;
	}

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static String obtenerFechaCierreSaldoIncialCartera(Connection con, int institucion)
	{
		String cadena="select mes_cierre||'/'||anio_cierre as fecha from cierres_saldos where tipo_cierre='"+ConstantesBD.codigoTipoCierreSaldoInicialStr+"' and institucion="+institucion;
		try 
	    {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	return rs.getString(1);
        } catch (SQLException e) 
        {            
            e.printStackTrace();
        }
        return "";
	}

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarResumenCierreSaldoInicial(Connection con, int institucion)
	{
		String cadena="select anio_cierre as year,mes_cierre as mes,fecha_generacion as fecha_generacion,hora_generacion as hora_generacion,usuario as usuario,observaciones as observaciones from cierres_saldos where tipo_cierre='"+ConstantesBD.codigoTipoCierreSaldoInicialStr+"' and institucion="+institucion;
		PreparedStatementDecorator ps;
        try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            return new ResultSetDecorator(ps.executeQuery());
        } catch (SQLException e) 
        {        
            e.printStackTrace();
            return null;
        }	   	    

	}


	/**
	 * 
	 * @param con
	 * @param fecha
	 * @param institucion
	 * @param esCierreInicial
	 * @return
	 */
	public static int[] validarExistenFacturas(Connection con,String fecha, int institucion, boolean esCierreInicial) 
	{
		int[] resultado={0,0};
		try
		{
			String consulta="select count(fact.codigo) as facturas,count(fact.NUMERO_CUENTA_COBRO) as cuentascobro "+
							"from facturas fact " +
				    		"inner join convenios con on(con.codigo=fact.convenio) "+
				    		"where "+
				    		" fact.institucion = ? and factura_cerrada = "+ValoresPorDefecto.getValorFalseParaConsultas() +
				    		" and tipo_contrato <> "+ConstantesBD.codigoTipoContratoCapitado+" ";;
			if(esCierreInicial)
			{	
			    consulta = consulta + " AND (fact.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
	    		"OR fact.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionAnulada+") " +
	    		"and to_char(fecha, 'yyyy-mm') <= ? ";
			    
			    logger.info("LA CONSULTA 1  >>>> "+consulta);
			    
			}  
			else
			{	
			    consulta = consulta + " AND fact.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" and to_char(fecha, 'yyyy-mm') = ? ";
			    logger.info("LA CONSULTA 2 >>>> "+consulta);
			}   
			
		    PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    logger.info("LA FECHA >>>>> "+fecha);
		    
			pst.setInt(1,institucion);
			pst.setString(2,fecha);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resultado[0]=rs.getInt(1);
				resultado[1]=rs.getInt(2);
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e){
			logger.error("Error consultando Facturas"+e);
		}
		return resultado;
	}
	
}
