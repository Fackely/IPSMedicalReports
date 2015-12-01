package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;



import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;




/**
 * 
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */
public class SqlBaseConceptosPagoPoolesXConvenioDao
{
	/**
	 * para manejar el log en la clase
	 */
	static Logger logger = Logger.getLogger(SqlBaseConceptosPagoPoolesXConvenioDao.class);
	
	/*-----------------------------------------------------
	 * 	ATRIBUTOS DE CONCEPTO DE PAGO DE POOLES X CONVENIO
	 * ---------------------------------------------------*/	
	
	
	/**
	 * Cadena de insercion de Conceptos de Pago de Pooles X Convenio
	 */
	private static final String strCadenaInsercionConceptosPagoPoolesXConvenio = "INSERT INTO concept_pag_pool_x_conv (codigo, pool, convenio, concepto,  tipo_servicio, porcentaje, cuenta_contable, institucion, usuario_modifica, fecha_modifica, hora_modifica) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Cadena de Eliminacion de Conceptos de Pago de Pooles X Convenio 
	 */
	private static final String strCadenaEliminacionConceptosPagoPoolesXConvenio = "DELETE FROM concept_pag_pool_x_conv WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de Modificacion de Conceptos de Pago de Pooles X Convenio
	 */
	private static final String strCadenaModificacionConceptosPagoPoolesXConvenio ="UPDATE concept_pag_pool_x_conv SET  pool=?, convenio=?, concepto=?,tipo_servicio=?, porcentaje=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? ";
	

	
	/**
	 * Cadena de Consulta de Conceptos de Pago de Pooles
	 */
	private static final String strCadenaConsultaConceptosPagoPoolesXConvenio  = "SELECT " +
			"cppxc.codigo AS codigoconceptoxconvenio," +
			"cppxc.pool AS codigopool," +
			"cppxc.convenio AS codigoconvenio," +
			"cppxc.concepto||'"+ConstantesBD.separadorSplit+"'||cpp.tipo_concepto AS codigoconcepto," +
			"cppxc.tipo_servicio AS codigotiposervicio," +
			"cppxc.porcentaje AS porcentaje," +
			"cppxc.cuenta_contable AS codigocuentacontable," +
			"cppxc.institucion AS institucion," +
			"cpp.tipo_concepto AS tipoconcepto," +
		    "'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM concept_pag_pool_x_conv cppxc " +
			"INNER JOIN conceptos_pagos_pooles cpp ON (cpp.codigo=cppxc.concepto) ";
	
	/**
	 * Cadena de indices para le ordenamiento
	 */
	private static final String [] indicesMapa ={"codigoconceptoxconvenio_","codigopool_","codigoconvenio_",
		"codigoconcepto_","codigotiposervicio_","porcentaje_","codigocuentacontable_","institucion_","estabd_","tipoconcepto_"};

	
	/**
	 * cadena de consulta de la descripcion de los datos para el log tipo archivo
	 */							
	private static final String strCadenaConsultaDesDatosLog = "SELECT " +
			"getdescripcionpool (?) AS nombrepool," +
			"getnombreconvenio (?) AS nombreconvenio," +
			"getdesconceppagpool (?) AS nombreconcepto," +
			"getnombretiposervicio (?) AS nombretiposervicico," +
			"getNombreInstitucion (?) AS nombreinstitucion " ;
		
										
	private static final String [] indicesMapaLog = {"nombrepool_","nombreconvenio_","nombreconcepto_","nombretiposervicico_","nombreinstitucion_","nombrecuentacontable_","codigoconceptoxconvenio_","porcentaje_",""};										
																												
																													
																													
																													
																													
																												  
	
	/*-----------------------------------------------------
	 * 	FIN ATRIBUTOS DE CONCEPTO DE PAGO DE POOLES X CONVENIO
	 * ---------------------------------------------------*/	
	
	
	/*-----------------------------------------------------
	 * 	METODOS DE CONCEPTO DE PAGO DE POOLES X CONVENIO
	 * ---------------------------------------------------*/	
	
	/**
	 * Metodo encargado de consultar los nombreso descripcion para generar el log 
	 * tipo archivo; a este metodo me le entra un HashMap de parametros y devuelve 
	 * con la respuesta llamado Mapa.
	 * El HashMap parametros cuenta con los siguientes key's:
	 * --------------------------------
	 *       KEY'S DE PARAMETROS
	 * --------------------------------
	 * --codigopool --> Requerido
	 * --codigoconvenio --> Requerido
	 * --codigoconcepto --> Requerido
	 * --codigotiposervicio --> Requerido
	 * --institucion --> Requerido
	 * --codigocuentacontable --> Opcional
	 * 
	 * -------------------------------
	 * 		  KEY'S DE MAPA
	 * -------------------------------
	 * nombrepool, nombrepool, nombreconcepto,
	 * nombretiposervicico, nombreinstitucion, 
	 * nombrecuentacontable.
	 */
	public static HashMap consultarInfoLog (Connection connection, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena=strCadenaConsultaDesDatosLog;
		logger.info("entro a consultarInfoLog "+parametros);
		try 
		{
			if (parametros.containsKey("codigocuentacontable"))
				if (parametros.get("codigocuentacontable")!=null && !parametros.get("codigocuentacontable").toString().equals(""))
					cadena += ", getnombrecuentcont("+Utilidades.convertirAEntero(parametros.get("codigocuentacontable")+"")+") AS nombrecuentacontable "; 
			else
				cadena +=", '' AS nombrecuentacontable ";
			
			logger.info("Cadena de consulta de consultarInfoLog ==> "+cadena);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigopool")+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigoconvenio")+""));
			ps.setString(3, parametros.get("codigoconcepto").toString());
			ps.setString(4, parametros.get("codigotiposervicio").toString());
			ps.setInt(5, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));	
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA", indicesMapaLog);
		
	
		
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de ingresar los datos en la tabla
	 * concept_pag_pool_x_conv.
	 * @param connection
	 * @param parametros
	 * @return boolean
	 * ------------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * ------------------------------------------------
	 * --codigopool --> Requerido
	 * --codigoconvenio --> Requerido
	 * --codigoconcepto --> Requerido
	 * --codigotiposervicio --> Requerido
	 * --porcentaje --> Requerido
	 * --institucion --> Requerido
	 * --usuario --> Requerido
	 *--codigocuentacontable --> Opcional
	 */
	public static boolean insertarConceptosPagoPoolesXConvenio (Connection connection, HashMap parametros)
	{
		String cadena = strCadenaInsercionConceptosPagoPoolesXConvenio;
		
		logger.info("cadena de insercion de insertarConceptosPagoPoolesXConvenio ==> "+cadena);
		logger.info("El valor del hashmap"+parametros);
						 
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			  
			/**
			 * INSERT INTO concept_pag_pool_x_conv 
			 * (codigo, 
			 * pool, 
			 * convenio, 
			 * concepto,  
			 * tipo_servicio, 
			 * porcentaje, 
			 * cuenta_contable, 
			 * institucion, 
			 * usuario_modifica, 
			 * fecha_modifica, 
			 * hora_modifica) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			 */
				
			
			  ps.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_con_pag_pool_x_conv"));
			  ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigopool").toString()));
			  ps.setInt(3, Utilidades.convertirAEntero(parametros.get("codigoconvenio").toString()));
			  ps.setString(4, parametros.get("codigoconcepto").toString());
			  ps.setString(5, parametros.get("codigotiposervicio").toString());
			  ps.setDouble(6, Utilidades.convertirADouble(parametros.get("porcentaje").toString()));
			  if (!parametros.get("codigocuentacontable").toString().equals(""))
				 ps.setInt(7, Utilidades.convertirAEntero(parametros.get("codigocuentacontable").toString()));
			 else
				 ps.setNull(7, Types.NULL);
			  ps.setInt(8, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			  ps.setString(9, parametros.get("usuario").toString());
			  ps.setDate(10,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			  ps.setString(11,UtilidadFecha.getHoraActual());
			
			  if (ps.executeUpdate()>0)
					return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de modificar los datos de la tabla 
	 * modificarConceptosPagosPoolesXConvenio.
	 * @param connection
	 * @param parametros
	 * @return boolean
	 * -------------------------------------------
	 * 				KEY'S DE PARAMETROS
	 * -------------------------------------------
	 * --codigoconceptoxconvenio --> Requerido
	 * --institucion --> Requerido
	 * --codigopool --> Requerido
	 * --codigoconvenio --> Requerido
	 * --codigoconcepto --> Requerido
	 * --codigotiposervicio --> Requerido
	 * --porcentaje --> Requerido
	 * --usuario --> Requerido
	 * --codigocuentacontable --> Opcional
	 */
	public static boolean modificarConceptosPagosPoolesXConvenio (Connection  connection, HashMap parametros)
	{
		String cadena =strCadenaModificacionConceptosPagoPoolesXConvenio, where = " WHERE codigo=? AND institucion=? ";;
		logger.info("El valor del hashmap"+parametros);
		if (parametros.containsKey("codigocuentacontable"))
			 if (!parametros.get("codigocuentacontable").toString().equals(""))
				 cadena+= ",cuenta_contable="+Utilidades.convertirAEntero(parametros.get("codigocuentacontable").toString());
			 else
				 cadena+= ",cuenta_contable="+null;
		cadena+=where;
		logger.info("cadena de insercion de modificarConceptosPagosPoolesXConvenio ==> "+cadena);
		
		logger.info("El valor del hashmap"+parametros);
		
		try 
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			  
			/**
			 * UPDATE concept_pag_pool_x_conv SET  
			 * pool=?, 
			 * convenio=?, 
			 * concepto=?,
			 * tipo_servicio=?, 
			 * porcentaje=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=? 
			 */
			
			  ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigopool").toString()));
			  ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigoconvenio").toString()));
			  ps.setString(3, parametros.get("codigoconcepto").toString());
			  ps.setString(4, parametros.get("codigotiposervicio").toString());
			  ps.setDouble(5, Utilidades.convertirADouble(parametros.get("porcentaje").toString()));
			  ps.setString(6, parametros.get("usuario").toString());
			  ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			  ps.setString(8,UtilidadFecha.getHoraActual());
			  ps.setInt(9, Utilidades.convertirAEntero(parametros.get("codigoconceptoxconvenio").toString()));
			  ps.setInt(10, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			  		
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de Consutlar los conceptos por 
	 * pago de pooles por convenio; esta consulta se puede
	 * filtar por los siguientes criterios que vienen en 
	 * el HashMap parametros.
	 * ----------------------------------
	 * 			KEY'S PARAMETROS
	 * ----------------------------------
	 * --institucion --> Requerido
	 * --codigopool --> Opcional
	 * --codigoconvenio --> Opcional
	 * -----------------------------------
	 * 	KEY'S DEL MAPA QUE DEVUELVE
	 * -----------------------------------
	 * codigoconceptoxconvenio, codigopool, 
	 * codigoconvenio, codigoconcepto, 
	 * codigotiposervicio, porcentaje, 
	 * codigocuentacontable, institucion, 
	 * estabd
	 * @param connection
	 * @param parametros
	 * @return mapa
	 */
	public static HashMap consultarConceptosPagosPoolesXConvenio (Connection connection, HashMap parametros)
	{ 
		String cadena = strCadenaConsultaConceptosPagoPoolesXConvenio,where=" WHERE cppxc.institucion = ?";
		HashMap mapa = new HashMap ();
		
		if (parametros.containsKey("codigopool"))
			 if (!parametros.get("codigopool").toString().equals(""))
				 where+= " AND cppxc.pool="+Utilidades.convertirAEntero(parametros.get("codigopool").toString());
		
		if (parametros.containsKey("codigoconvenio"))
			 if (!parametros.get("codigoconvenio").toString().equals(""))
				 where+= " AND cppxc.convenio="+Utilidades.convertirAEntero(parametros.get("codigoconvenio").toString());
		
		
		if (parametros.containsKey("codigoconceptoxconvenio"))
			 if (!parametros.get("codigoconceptoxconvenio").toString().equals(""))
				 where+= " AND cppxc.codigo="+Utilidades.convertirAEntero(parametros.get("codigoconceptoxconvenio").toString());
		
		
		cadena+=where+" ORDER BY cppxc.concepto DESC";
		logger.info("cadena de consutla de consultarConceptosPagosPoolesXConvenio ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));	
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA", indicesMapa);
		
		return mapa;
	}
	
	
	/**
	 * Metodo encargado de elimiar un registro de conceptos
	 * pagos pooles por convenio.
	 * ------------------------------
	 * 		KEY'S PARAMETROS
	 * ------------------------------
	 * --codigoconceptoxconvenio --> Requerido
	 * --institucion --> Requerido
	 * @param connection
	 * @param parametros
	 * @return boolean
	 */
	public static boolean eliminarConceptosPagosPoolesXConvenio (Connection connection, HashMap parametros)
	{
		String cadena = strCadenaEliminacionConceptosPagoPoolesXConvenio;
		
		logger.info("cadena de consutla de eliminarConceptosPagosPooles ==> "+cadena);
		logger.info("El valor del hashmap"+parametros);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * DELETE FROM concept_pag_pool_x_conv WHERE codigo=? AND institucion=?
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigoconceptoxconvenio").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*-----------------------------------------------------
	 * FIN METODOS DE CONCEPTO DE PAGO DE POOLES X CONVENIO
	 * ---------------------------------------------------*/	
	

}
