package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class SqlBaseAprobacionAnulacionAjustesFacturasVariasDao
{

   /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAprobacionAnulacionAjustesFacturasVariasDao.class);
	
	/**
	 * Cadena SELECT para consultar los ajustes de facturas varias en estado 'PEN'---->Pendiente
	 */
	private static String strConSelAjustesFacturasVarias = "SELECT " +
																"afv.codigo AS codigoajuste, " +
																"afv.consecutivo AS numajuste, " +
																"to_char(afv.fecha_ajuste, 'DD/MM/YYYY') AS fechaajuste, " +
																"afv.concepto_ajuste AS codconceptoajuste, " +
																"getnombreconceptoajuste(afv.concepto_ajuste) AS conceptoajuste, " +
																"coalesce(afv.valor_ajuste, 0) AS valorajuste, " +
																"fv.codigo_fac_var AS codigofactura, " +
																"fv.consecutivo AS numfactura, " +
																"CASE " +
																	"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getnombrepersona(d.codigo_paciente)  " +
																	"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa)  " +
																	"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getdescripciontercero(d.codigo_tercero)  " +
																	"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.primer_nombre ||'  '|| d.primer_apellido " +
																"END AS nombredeudor, " + 
																//"getdescripciontercero(fv.deudor) AS nombredeudor, " +
																"CASE " +
																	"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
																	"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnitempresa(d.codigo_empresa)  " +
																	"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
																	"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
																"END  AS identificaciondeudor, " +
																"afv.estado AS estado, " +
																"afv.tipo_ajuste AS tipoajuste " +
															"FROM " +
																"ajus_facturas_varias afv " +
																"INNER JOIN facturas_varias fv ON (fv.codigo_fac_var = afv.factura) " +
																"INNER JOIN deudores  d ON(d.codigo=fv.deudor) " +
															"WHERE " +
																"estado = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' " +
																"AND afv.institucion = ? " +
															"ORDER BY afv.consecutivo  "; 

	/**
	 * Cadena UPDATE para actualizar los campos de aprobaci�n/anulaci�n del ajuste de facturas varias
	 */
	private static String strUpdAjustesFacturasVarias = "UPDATE "+
															"ajus_facturas_varias "+
														"SET "+
															"usuario_aprob_anul = ?, "+
															"motivo_aprob_anul = ?, "+
															"fecha_aprob_anul = ?, "+
															"hora_aprob_anul = ?, "+
															"estado = ? "+
														"WHERE "+
															"codigo = ? ";
	
	/**
	 * Cadena INSERT para ingresar el log tipo base de datos de los par�metros de aprobaci�n/anulaci�n del ajuste de facturas varias
	 */
	private static String strInsAjustesFacturasVarias = "INSERT INTO log_aprob_anul_ajus_fvarias VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; 
	
	/**
	 * Cadena UPDATE para actualizar la sumatoria de los ajustes cr�dito en la tabla facturas varias
	 */
	private static String strUpdFacturasVariasCredito = "UPDATE "+
															"facturas_varias "+
														 "SET "+
															"ajustes_credito = coalesce(ajustes_credito, 0) + ? "+
														 "WHERE "+
															"codigo_fac_var = ? ";
	
	/**
	 * Cadena UPDATE para actualizar la sumatoria de los ajustes debito en la tabla facturas varias
	 */
	private static String strUpdFacturasVariasDebito = "UPDATE "+
															"facturas_varias "+
														 "SET "+
															"ajustes_debito = coalesce(ajustes_debito, 0) + ? "+
														 "WHERE "+
															"codigo_fac_var = ? ";
	
	/**
	 * M�todo para consultar los ajustes pendientes de 
	 * facturas varias, para ser devueltos en un HashMap
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarAjustesFacturasVarias(Connection con, int codigoInstitucion)
	{
		HashMap mapa = new HashMap<String, Object>();
	    mapa.put("numRegistros","0");
	    try
	    {
	    	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSelAjustesFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ps.setInt(1, codigoInstitucion);
	    	logger.info("====>Consulta de Ajustes Facturas Varias: "+strConSelAjustesFacturasVarias);
	        mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	    }
	    catch (SQLException e)
	    {
	        logger.error("ERROR EJECUNTANDO LA CONSULTA DE AJUSTES FACTURAS VAR�ASS");
	        e.printStackTrace();
	    }
	    return (HashMap)mapa.clone();
	}

	/**
	 * M�todo que inserta los datos de aprobaci�n/anulaci�n
	 * del ajuste de facturas varias
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean actualizarAprobacionAnulacionAjusteFacturasVarias(Connection con, HashMap criterios)
	{
		boolean enTransaccion = false;
		try
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdAjustesFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setString(1, criterios.get("usuarioaprobanul")+"");
        	
	  		if(UtilidadCadena.noEsVacio(criterios.get("motivoaprobanul")+""))
        		ps.setString(2, criterios.get("motivoaprobanul")+"");
        	else
        		ps.setNull(2, Types.VARCHAR);
        		
        	ps.setDate(3, Date.valueOf(criterios.get("fechaaprobanul")+""));
        	ps.setString(4, criterios.get("horaaprobanul")+"");
        	ps.setString(5, criterios.get("estado")+"");
        	ps.setDouble(6, Utilidades.convertirADouble(criterios.get("codigoajuste")+""));
        	logger.info("===>Consulta Actualizaci�n Ajustes: "+strUpdAjustesFacturasVarias+" ===>Usuario: "+criterios.get("usuarioaprobanul")+" ===>Motivo: "+criterios.get("motivoaprobanul")+ "===>Fecha: "+criterios.get("fechaaprobanul")+" ===>Hora: "+criterios.get("horaaprobanul")+" ===>Estado: "+criterios.get("estado")+ " ===>Codigo: "+criterios.get("codigoajuste"));
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion)
        	{
        		logger.info("===>Estado Ajuste: "+criterios.get("estado"));
        		//Si el estado es igual a aprobado se debe realizar la actualizaci�n de los ajustes en la tabla facturas varias
        		if((criterios.get("estado")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
        		{
        			logger.info("===>Tipo Ajuste: "+criterios.get("tipoajuste"));
        			//Validamos que tipo de ajuste es para realizar la correspondiente actualizaci�n
        			if((criterios.get("tipoajuste")+"").equals(ConstantesIntegridadDominio.acronimoCredito))
        			{
        				ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdFacturasVariasCredito,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        				ps.setDouble(1, Utilidades.convertirADouble(criterios.get("valorajuste")+""));
        	    		ps.setDouble(2, Utilidades.convertirADouble(criterios.get("codigofactura")+""));
        	    		logger.info("===>Actualizacion Ajustes Credito: "+strUpdFacturasVariasCredito+" ===>Valor Ajuste Facturas: "+criterios.get("valorajuste")+" ===>Codigo Factura: "+criterios.get("codigofactura"));
        	    		enTransaccion = (ps.executeUpdate()>0);
        	        	if(enTransaccion)
        	        	{
        	        		logger.info("SE ACTUALIZO LOS AJUSTES CREDITO DE LA FACTURA VARIA CORRECTAMENTE");
        	        		return true;
        	        	}
        	        	else
        	        		return false;
        			}
        			else if((criterios.get("tipoajuste")+"").equals(ConstantesIntegridadDominio.acronimoDebito))
        			{
        				ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdFacturasVariasDebito,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        				ps.setDouble(1, Utilidades.convertirADouble(criterios.get("valorajuste")+""));
        	    		ps.setDouble(2, Utilidades.convertirADouble(criterios.get("codigofactura")+""));
        	    		logger.info("===>Actualizacion Ajustes Debito: "+strUpdFacturasVariasDebito+" ===>Valor Ajuste Facturas: "+criterios.get("valorajuste")+" ===>Codigo Factura: "+criterios.get("codigofactura"));
        	    		enTransaccion = (ps.executeUpdate()>0);
        	        	if(enTransaccion)
        	        	{
        	        		logger.info("SE ACTUALIZO LOS AJUSTES DEBITO DE LA FACTURA VARIA CORRECTAMENTE");
        	        		return true;
        	        	}
        	        	else
        	        		return false;
        			}
        		}
        		else
        		{
        			logger.info("SE ACTUALIZO EL AJUSTE DE FACTURAS VARIAS CORRECTAMENTE");
            		return true;
        		}
        	}
        	else
        		return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * M�todo que inserta en el log tipo
	 * base de datos
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static boolean generarLogAprobacionAnulacion(Connection con, HashMap criterios)
	{
		boolean enTransaccion = false;
		try
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsAjustesFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturasvarias.seq_log_apro_anu_ajus_fvarias"));
	  		ps.setDate(2, Date.valueOf(criterios.get("fecha")+""));
	  		ps.setString(3, criterios.get("horaaprobanul")+"");
	  		ps.setString(4, criterios.get("usuarioaprobanul")+"");
	  		ps.setString(5, criterios.get("estado")+"");
	  		
	  		if(UtilidadCadena.noEsVacio(criterios.get("motivoaprobanul")+""))
        		ps.setString(6, criterios.get("motivoaprobanul")+"");
        	else
        		ps.setNull(6, Types.VARCHAR);
        		
        	ps.setDate(7, Date.valueOf(criterios.get("fechaaprobanul")+""));
        	ps.setDouble(8, Utilidades.convertirADouble(criterios.get("factura")+""));
        	enTransaccion = (ps.executeUpdate() > 0);
        	if(enTransaccion)
        	{
        		logger.info("SE INSERTO CORRECTAMENTE EL AJUSTE DE FACTURAS VARIAS");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo encargado de generar las clausulas where
	 * de la consulta.
	 * @param criterios
	 * @param ajusTodos
	 * @return
	 */
	public static String obtenerWhere(HashMap criterios)
	{
		String where = " WHERE afv.institucion = "+criterios.get("usuario")+" AND afv.codigo = "+criterios.get("codigoAjusteFactura");
		return where;
	}
	
}