package com.princetonsa.dao.sqlbase;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import com.princetonsa.decorator.PreparedStatementDecorator;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Busqueda.BusquedaInstitucionesSircGenericaAction;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class SqlBaseBusquedaInstitucionesSircDao
{
	
	/**
	 * Objeto para manejar el log de la clase
	 * */
	static Logger logger = Logger.getLogger(BusquedaInstitucionesSircGenericaAction.class);
	
	/**
	 * Cadena de consulta de instituciones SIRC
	 * */	
	static final String cadenaConsultarInstitucionesSirc = "SELECT isi.codigo AS codigo, " +
														   "isi.institucion AS institucion, " +
														   "isi.descripcion AS descripcion, " +
														   "ns.descripcion AS descripcionnservicio," +
														   "isi.nivel_servicio AS nivelservicio, " +
														   "isi.tipo_red AS tipored, " +
														   "isi.tipo_inst_referencia AS tipoinstreferencia, " +
														   "isi.tipo_inst_ambulancia AS tipoinstambulancia," +
														   "isi.activo as activo, "; 
														   
	
	/**
	 * String vetor de indices de mapa
	 * */
	static final String[] indicesInstitucionesSircMap = {"codigo_","institucion_","descripcion_","descripcionnservicio_","nivelservicio_","tipored_","tipoinstreferencia_","tipoinstambulancia_","activo_","yafueseleccionado_"};
	
	/**
	 * Consulta de instituciones SIRC
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarInstitucioneSirc(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = "";		
		
				
		if(!parametros.get("codigoInstitucionesSirInsert").equals(""))
		{
			cadena =" CASE WHEN isi.codigo IN ("+parametros.get("codigoInstitucionesSirInsert").toString()+" '"+ConstantesBD.codigoNuncaValido+"') " +
					" THEN 'true' " +
					" ELSE 'false' " +
					" END AS yafueseleccionado ";		
		}
		else
			cadena = " 'false' AS yafueseleccionado ";
		
						
		cadena = cadenaConsultarInstitucionesSirc +cadena+" FROM historiaclinica.instituciones_sirc isi " +
				"LEFT OUTER JOIN nivel_atencion ns ON (ns.consecutivo = isi.nivel_servicio )  WHERE isi.institucion = "+parametros.get("institucion");		
		
		if(parametros.containsKey("codigo"))
			cadena+=" AND isi.codigo = "+parametros.get("codigo").toString();
		if(parametros.containsKey("descripcion"))
			cadena+=" AND isi.descripcion LIKE '%"+parametros.get("descripcion").toString()+"%' ";
		if(parametros.containsKey("nivelservicio"))
			cadena+=" AND isi.nivel_servicio= "+parametros.get("nivelservicio").toString();
		if(parametros.containsKey("tipored"))
			cadena+=" AND isi.tipo_red ='"+parametros.get("tipored").toString()+"' ";
		if(parametros.containsKey("tipoinstreferencia"))
			cadena+=" AND isi.tipo_inst_referencia = '"+parametros.get("tipoinstreferencia")+"'";
		if(parametros.containsKey("tipoinstambulancia"))
			cadena+=" AND isi.tipo_inst_ambulancia = '"+parametros.get("tipoinstambulancia")+"' ";
		
		if(parametros.containsKey("opcionNotInt"))
		{			
			if(parametros.get("opcionNotInt").equals("1"))
				cadena+= " AND isi.codigo || '-' || isi.institucion NOT IN (SELECT CASE WHEN codigo_inst_sirc IS NULL THEN '' ELSE codigo_inst_sirc || '-' || cod_institucion END from administracion.centro_atencion) ";				
		}
		
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		mapa.put("INDICES_MAPA",indicesInstitucionesSircMap);
		return mapa;
	}
}