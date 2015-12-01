package com.princetonsa.dao.sqlbase.util;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoTiposMoneda;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseUtilConversionMonedasDao 
{
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseUtilConversionMonedasDao.class);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param mostrarMonedaManejaInstitucion
	 * @param tipoBD 
	 * @return
	 */
	public static HashMap obtenerTiposMonedaTagMap( Connection con, int codigoInstitucion, boolean mostrarMonedaManejaInstitucion, int tipoBD)
	{
		HashMap mapa= new HashMap();
		mapa.put("numRegistros", "0");
		String consulta=" SELECT " +
							"tm.codigo AS codigopktipomoneda, " +
							"tm.descripcion AS descripciontipomoneda, " +
							"tm.simbolo AS simbolotipomoneda, " +
							"coalesce(fcm.codigo, "+ConstantesBD.codigoNuncaValido+") AS codigofactorconversion, " +
							"coalesce(fcm.factor,"+ConstantesBD.codigoNuncaValido+") as factorconversion ";
							
		String consultaFROMPostgresql=
						"FROM " +
							"tipos_moneda tm " +
							"left outer join factor_conversion_monedas fcm on(fcm.codigo_moneda=tm.codigo AND (CURRENT_DATE >= fcm.fecha_inicial AND CURRENT_DATE <= fcm.fecha_final)) ";
		
		String consultaFROMOracle=
			"FROM " +
				"tipos_moneda tm " +
				"left outer join factor_conversion_monedas fcm on(fcm.codigo_moneda=tm.codigo AND (TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') >= fcm.fecha_inicial AND TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD') <= fcm.fecha_final)) ";
		
		String consultaWHERE=
						"WHERE   " +
							"tm.institucion = "+codigoInstitucion+" " +
							" " ;
		
		switch(tipoBD){
			case DaoFactory.ORACLE:
				consulta += consultaFROMOracle + consultaWHERE;
				break;
				
			case DaoFactory.POSTGRESQL:
				consulta += consultaFROMPostgresql + consultaWHERE;
				break;
				
			default:
				break;
		}
							
		if(!mostrarMonedaManejaInstitucion)
		{
			consulta+=" AND tm.codigo <> "+obtenerTipoMonedaManejaInstitucion(con, codigoInstitucion, tipoBD).getCodigo()+" ";
		}
		
		consulta+=" order by descripciontipomoneda, simbolotipomoneda" ;
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n obtenerTiposMonedaTagMap-->"+consulta+"\n");
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));  
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del tipos moneda tag map ");
			e.printStackTrace();
			mapa= new HashMap();
			mapa.put("numRegistros", "0");
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoBD 
	 * @return
	 */
	public static DtoTiposMoneda obtenerTipoMonedaManejaInstitucion(Connection con, int codigoInstitucion, int tipoBD)
	{
		DtoTiposMoneda dto= new DtoTiposMoneda();
		String consulta=" SELECT " +
							"coalesce(tmi.tipo_moneda, "+ConstantesBD.codigoNuncaValido+") as tipomonedainstitucion, " +
							"tm.codigo_tipo_moneda as codigotipomoneda, " +
							"tm.descripcion as descripciontipomoneda, " +
							"tm.simbolo " +
						" FROM " +
							"tipos_moneda_institucion tmi " +
							"inner join tipos_moneda tm on (tm.codigo=tmi.tipo_moneda) ";
						
		String whereOracle = " WHERE " +
							"tmi.institucion="+codigoInstitucion+" " +
							"AND tmi.fecha_inicial <= TO_DATE(TO_CHAR(SYSDATE,'YYYYMMDD'),'YYYYMMDD')  " +
							"AND rownum = 1 "+
						"ORDER BY tmi.fecha_inicial DESC";
		
		String wherePostgreSql = " WHERE " +
								"tmi.institucion="+codigoInstitucion+" " +
								"AND tmi.fecha_inicial <= CURRENT_DATE  " +
							"ORDER BY tmi.fecha_inicial DESC "+ValoresPorDefecto.getValorLimit1()+" 1";
		
		switch(tipoBD){
			case DaoFactory.ORACLE:
				consulta += whereOracle;
				break;
			
			case DaoFactory.POSTGRESQL:
				consulta += wherePostgreSql;
				break;
			
			default:
				break;
		}
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigo(rs.getInt("tipomonedainstitucion"));
				dto.setCodigoTipoMoneda(rs.getString("codigotipomoneda"));
				dto.setDescripcion(rs.getString("descripciontipomoneda"));
				dto.setInstitucion(codigoInstitucion);
				dto.setSimbolo(rs.getString("simbolo"));
				return dto;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del tipos moneda tag map ");
			e.printStackTrace();
		}
		return dto;
	}
		
}
