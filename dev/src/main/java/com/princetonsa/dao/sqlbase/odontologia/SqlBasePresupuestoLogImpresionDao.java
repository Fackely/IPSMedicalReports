package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.odontologia.DtoDetalleLogImpresionPresupuesto;
import com.princetonsa.dto.odontologia.DtoLogImpresionPresupuesto;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 4:51:03 PM
 */
public class SqlBasePresupuestoLogImpresionDao 
{
	/**
	 * insertar
	 */
	private static final String insertarEncabezadoStr="INSERT INTO odontologia.presupuesto_log_impresion (" +
															"codigo_pk , " +//1
															"presupuesto , " +//2
															"usuario_modifica , " +//3
															"fecha_modifica , " +//4
															"hora_modifica) " +//5
														"VALUES(?,?,?,?,?) ";
	
	
	/**
	 * insertar
	 */
	private static final String insertarDetalleStr="INSERT INTO odontologia.presupuesto_log_det_imp(" +
															"codigo_pk , " +//1
															"presupuesto_log_impresion, " +//2
															"anexo " +//3
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
	public static BigDecimal insertar(Connection con, DtoLogImpresionPresupuesto dto)
	{
		BigDecimal secuencia= BigDecimal.ZERO;
	 	try 
		{
			secuencia= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_log_imp")); 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarEncabezadoStr);
			dto.setCodigoPk(secuencia);
			ps.setBigDecimal(1, dto.getCodigoPk());
			ps.setBigDecimal(2, dto.getPresupuesto());
			ps.setString(4, dto.getFHU().getUsuarioModifica());
			ps.setString(5, dto.getFHU().getFechaModificaFromatoBD());
			ps.setString(6, dto.getFHU().getHoraModifica());
			
			if(ps.executeUpdate()<=0)
			{
				secuencia= BigDecimal.ZERO;
			}
			else
			{
				for(DtoDetalleLogImpresionPresupuesto dtoDeta: dto.getDetalle())
				{
					dtoDeta.setPresupuestoLogImpresion(secuencia);
					if(!insertarDetalle(con, dtoDeta))
					{
						secuencia= BigDecimal.ZERO;
						break;
					}
				}
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return secuencia;
	}
	
	/**
	 * 
	 * Metodo para insertar el detalle
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static boolean insertarDetalle(Connection con,	DtoDetalleLogImpresionPresupuesto dto) 
	{
		BigDecimal secuencia= BigDecimal.ZERO;
	 	try 
		{
			secuencia= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_log_det_imp")); 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarDetalleStr);
			dto.setCodigoPk(secuencia);
			ps.setBigDecimal(1, dto.getCodigoPk());
			ps.setBigDecimal(2, dto.getPresupuestoLogImpresion());
			ps.setString(3, dto.getAnexo().toString());
			
			if(ps.executeUpdate()<=0)
			{
				secuencia= BigDecimal.ZERO;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
			secuencia= BigDecimal.ZERO;
		}
		return secuencia.doubleValue()>0;
	}
}