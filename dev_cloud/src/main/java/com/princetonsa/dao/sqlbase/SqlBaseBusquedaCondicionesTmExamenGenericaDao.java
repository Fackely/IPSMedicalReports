package com.princetonsa.dao.sqlbase;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import com.princetonsa.decorator.PreparedStatementDecorator;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;

public class SqlBaseBusquedaCondicionesTmExamenGenericaDao
{	
	/**
	 * Cadena statica contiene sentencia sql de consulta de Condicion de Toma de Medicamento 
	 * */
	private static final String cadenaConsultarCondicionesTmExamenStr = "SELECT codigo_examenct AS codigoexamenct, " +
																  "				institucion AS institucion, " +
																  "				descrip_examenct AS descripcion, " +
																  "				activo_examenct AS activoexamenct, " +
																  "				usuario_modifica AS usuariomodifica, " +
																  "				fecha_modifica AS fechamodifica, " +
																  "				hora_modifica AS horamodifica " +															  "				";
	
	
	/**
	 * Cadena estatica indica los indices del HashMap
	 * */
	private static final String [] indicesMap  = {"codigoexamenct_","institucion_","descripcion_","activoexamenct_","usuariomodifica_","horamodifica_","yafueseleccionado_"};
	
	/**
	 * Consulta Generica de Condiciones de Toma de Examen
	 * @param Connection con
	 * @param Int codigoExamenCt
	 * @param Int institucion
	 * @param String descripcionExamenCt
	 * @param String activo	 
	 * */
	public static HashMap consultarCondicionesTmExamenBasica(Connection con, String codigoExamenCt, int institucion, String descripcionExamenCt, String activo, String codigosExamenesInsertados)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		
		String condicion="";
		
		if(!codigosExamenesInsertados.equals(""))
			condicion=" ,CASE WHEN codigo_examenct IN ("+codigosExamenesInsertados+" "+ConstantesBD.codigoNuncaValido+") " +
		  "				THEN 'true' " +
		  "				ELSE 'false' " +
		  "				END AS yafueseleccionado ";
		else
			condicion=", 'false' AS yafueseleccionado ";
		  

		condicion+= " FROM	facturacion.examen_conditoma WHERE institucion=? ";
		
		if(!codigoExamenCt.equals(""))
			condicion+=" AND codigo_examenct ="+codigoExamenCt+" ";
		if(!descripcionExamenCt.equals(""))
			condicion+=" AND descrip_examenct LIKE UPPER('%"+descripcionExamenCt+"%') ";
		if(!activo.equals(""))
			condicion+=" AND activo_examenct = '"+activo+"' ";
		
		condicion=cadenaConsultarCondicionesTmExamenStr+condicion+" ORDER BY codigo_examenct ";		
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(condicion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	
			ps.setInt(1,institucion);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}		
		
		mapa.put("INDICES_MAPA",indicesMap);
		return mapa;
		
	}
}