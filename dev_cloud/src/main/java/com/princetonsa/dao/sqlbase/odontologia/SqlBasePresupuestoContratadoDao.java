/**
 * 
 */
package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.UtilidadTexto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 5:09:58 PM
 */
public class SqlBasePresupuestoContratadoDao 
{
	/**
	 * insertar
	 */
	private static final String insertarStr="INSERT INTO odontologia.presupuesto_contratado (" +
												" codigo_pk_presupuesto, pie_pagina_presupuesto, consecutivo) " +//3
											"VALUES(?,?,?) ";
	
	/**
	 * 
	 * Metodo para insertar
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal insertar(Connection con, DtoPresupuestoContratado dto)
	{
		BigDecimal secuencia= BigDecimal.ZERO;
	 	try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStr);
			ps.setBigDecimal(1, dto.getCodigoPkPresupuesto());
			if(UtilidadTexto.isEmpty(dto.getPiePaginaPresupuesto()))
			{
				ps.setString(2, dto.getPiePaginaPresupuesto());
			}
			else
			{	
				ps.setString(2, dto.getPiePaginaPresupuesto());
			}	
			ps.setBigDecimal(3, dto.getConsecutivo());
			
			if(ps.executeUpdate()<=0)
			{
				secuencia= BigDecimal.ZERO;
			}
			else
			{
				secuencia=dto.getCodigoPkPresupuesto();
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
			Log4JManager.error(e);
		}
		return secuencia;
	}
	
	/**
	 * 
	 * Metodo para eliminar 
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminar(Connection con, DtoPresupuestoContratado dto)
	{
		boolean retorna= false;
		String eliminarDetalleStr="delete from odontologia.presupuesto_contratado where 1=1 ";
		boolean filtrado= false;
		if(dto.getCodigoPkPresupuesto().doubleValue()>0)
		{
			filtrado=true;
			eliminarDetalleStr+=" and codigo_pk_presupuesto="+dto.getCodigoPkPresupuesto()+" ";
		}
		else if(dto.getConsecutivo().doubleValue()>0)
		{
			filtrado=true;
			eliminarDetalleStr+=" and consecutivo="+dto.getConsecutivo()+" ";
		}
		if(filtrado)
		{	
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarDetalleStr );
				if(ps.executeUpdate()>0)
				{
					retorna= true;
				}
				ps.close();
			}
			catch (SQLException e) 
			{
				Log4JManager.info("ERROR en eliminar ", e);
				retorna= false;
			}
		}	
		return retorna;
	}
	
	/**
	 * 
	 * Metodo para cargar 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoPresupuestoContratado> cargar(Connection con, DtoPresupuestoContratado dto)
	{
		ArrayList<DtoPresupuestoContratado> lista= new ArrayList<DtoPresupuestoContratado>();
		String consultaStr=	" SELECT " +
								"codigo_pk_presupuesto as codigo_pk_presupuesto, " +
								"pie_pagina_presupuesto as pie_pagina_presupuesto, " +
								"consecutivo as consecutivo " +//3
							"FROM " +
								"odontologia.presupuesto_contratado " +
							"where " +
								"1=1 ";
		
		boolean filtrado= false;
		if(dto.getCodigoPkPresupuesto().doubleValue()>0)
		{
			filtrado=true;
			consultaStr+=" and codigo_pk_presupuesto="+dto.getCodigoPkPresupuesto()+" ";
		}
		else if(dto.getConsecutivo().doubleValue()>0)
		{
			filtrado=true;
			consultaStr+=" and consecutivo="+dto.getConsecutivo()+" ";
		}
		
		if(filtrado)
		{	
			try 
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
				ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
				Log4JManager.info("consulta-->"+ps.toString());
				while(rs.next())
				{
					DtoPresupuestoContratado info= new DtoPresupuestoContratado();
					info.setCodigoPkPresupuesto(rs.getBigDecimal("codigo_pk_presupuesto"));
					info.setPiePaginaPresupuesto(rs.getString("pie_pagina_presupuesto"));
					info.setConsecutivo(rs.getBigDecimal("consecutivo"));
					lista.add(info);
				}
				ps.close();
				rs.close();
			}
			catch (SQLException e) 
			{
				Log4JManager.info("ERROR en cargar ", e);
			}
		}	
		return lista;
	}
	
	
	
	

}
