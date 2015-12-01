/**
 * 
 */
package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.InfoPorcentajeValor;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoPresupuestoCuotasEspecialidad;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleCuotasEspecialidad;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleCuotasEspecialidad.ETipoCuota;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 5:33:23 PM
 */
public class SqlBasePresupuestoCuotasEspecialidadDao 
{
	/**
	 * insertar
	 */
	private static final String insertarEncabezadoStr="INSERT INTO odontologia.presupuesto_cuotas_esp (" +
															"codigo_pk , " +//1
															"presupuesto_contratado , " +//2
															"especialidad , " +//3
															"tipo_valor , " +//4
															"usuario_modifica , " +//5
															"fecha_modifica , " +//6
															"hora_modifica ) " +//7
														"VALUES(?,?,?,?,?,?,?) ";
	
	
	/**
	 * insertar
	 */
	private static final String insertarDetalleStr="INSERT INTO odontologia.presupuesto_det_cuotas_esp(" +
															" codigo_pk , " +//1
															"presupuesto_cuotas_esp ," +//2
															"tipo_cuota, " +//3
															"nro_cuotas ," +//4
															"porcentaje, " +//5
															"valor) " +//6
														"VALUES(?,?,?,?,?,?) ";
	
	
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
	public static BigDecimal insertar(Connection con, DtoPresupuestoCuotasEspecialidad dto)
	{
		BigDecimal secuencia= BigDecimal.ZERO;
	 	try 
		{
			secuencia= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_cuotas_esp")); 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarEncabezadoStr);
			dto.setCodigoPk(secuencia);
			ps.setBigDecimal(1, dto.getCodigoPk());
			ps.setBigDecimal(2, dto.getPresupuestoContratado());
			ps.setInt(3, dto.getEspecialidad());
			ps.setString(4, dto.getTipoValor().toString());
			ps.setString(5, dto.getFHU().getUsuarioModifica());
			ps.setString(6, dto.getFHU().getFechaModificaFromatoBD());
			ps.setString(7, dto.getFHU().getHoraModifica());
			
			if(ps.executeUpdate()<=0)
			{
				secuencia= BigDecimal.ZERO;
			}
			else
			{
				for(DtoPresupuestoDetalleCuotasEspecialidad dtoDeta: dto.getDetalle())
				{
					dtoDeta.setPresupuestoCuotasEspecialidad(secuencia);
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
	private static boolean insertarDetalle(Connection con,	DtoPresupuestoDetalleCuotasEspecialidad dto) 
	{
		BigDecimal secuencia= BigDecimal.ZERO;
	 	try 
		{
			secuencia= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_detcuotaodoesp")); 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarDetalleStr);
			dto.setCodigoPk(secuencia);
			ps.setBigDecimal(1, dto.getCodigoPk());
			ps.setBigDecimal(2, dto.getPresupuestoCuotasEspecialidad());
			ps.setString(3, dto.getTipoCuota().toString());
			ps.setInt(4, dto.getNroCuotas());
			
			if(dto.getValorOporcentaje().getValor().doubleValue()>0)
			{
				ps.setNull(5, Types.NUMERIC);
				ps.setBigDecimal(6,dto.getValorOporcentaje().getValor());
			}
			else
			{
				ps.setDouble(5,  dto.getValorOporcentaje().getPorcentaje());
				ps.setNull(6, Types.NUMERIC);
			}
			
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
	public static boolean eliminar(Connection con, BigDecimal codigoPkPresupuestoContratado)
	{
		boolean retorna= true;
		String eliminarDetalleStr="delete from odontologia.presupuesto_det_cuotas_esp " +
										"where " +
											"presupuesto_cuotas_esp in " +
											"(" +
												"select " +
													"codigo_pk " +
												"from " +
													"odontologia.presupuesto_cuotas_esp " +
												"where " +
													"presupuesto_contratado=?" +
											")";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarDetalleStr );
			ps.setBigDecimal(1, codigoPkPresupuestoContratado);
			ps.executeUpdate();
			retorna= eliminarEncabezado(con, codigoPkPresupuestoContratado);
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en eliminar ", e);
			retorna= false;
		}
		return retorna;
	}

	/**
	 * 
	 * Metodo para eliminar el encabezado
	 * @param con
	 * @param codigoPkPresupuestoContratado
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static boolean eliminarEncabezado(Connection con,BigDecimal codigoPkPresupuestoContratado) 
	{
		boolean retorna= false;
		String eliminarDetalleStr="delete from odontologia.presupuesto_cuotas_esp " +
												"where " +
													"presupuesto_contratado=?";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarDetalleStr );
			ps.setBigDecimal(1, codigoPkPresupuestoContratado);
			if(ps.executeUpdate()>0)
			{
				retorna= eliminarEncabezado(con, codigoPkPresupuestoContratado);
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en eliminar ", e);
			retorna= false;
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
	public static ArrayList<DtoPresupuestoCuotasEspecialidad> cargar(Connection con, DtoPresupuestoCuotasEspecialidad dto)
	{
		ArrayList<DtoPresupuestoCuotasEspecialidad> lista= new ArrayList<DtoPresupuestoCuotasEspecialidad>();
		String consultaStr=	" SELECT " +
								"codigo_pk as codigo_pk, " +//1
								"presupuesto_contratado as presupuesto_contratado, " +//2
								"especialidad as especialidad, " +//3
								"tipo_valor as tipo_valor " +//3
							"FROM " +
								"odontologia.presupuesto_cuotas_esp " +
							"where " +
								"1=1 ";
		
		boolean filtrado= false;
		if(dto.getCodigoPk().doubleValue()>0)
		{
			filtrado=true;
			consultaStr+=" and codigo_pk="+dto.getCodigoPk()+" ";
		}
		if(dto.getPresupuestoContratado().doubleValue()>0)
		{
			filtrado=true;
			consultaStr+=" and presupuesto_contratado="+dto.getPresupuestoContratado()+" ";
		}
		if(dto.getEspecialidad()>0)
		{
			consultaStr+=" and especialidad="+dto.getEspecialidad()+" ";
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
					DtoPresupuestoCuotasEspecialidad info= new DtoPresupuestoCuotasEspecialidad();
					info.setCodigoPk(rs.getBigDecimal("codigo_pk"));
					info.setPresupuestoContratado(rs.getBigDecimal("presupuesto_contratado"));
					info.setEspecialidad(rs.getInt("especialidad"));
					info.setTipoValor(rs.getString("tipo_valor"));
					
					info.setDetalle(cargarDetalle(con, info.getCodigoPk()));
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

	/**
	 * 
	 * Metodo para cargar el detalle
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<DtoPresupuestoDetalleCuotasEspecialidad> cargarDetalle(Connection con, BigDecimal codigoPk) 
	{
		ArrayList<DtoPresupuestoDetalleCuotasEspecialidad> lista= new ArrayList<DtoPresupuestoDetalleCuotasEspecialidad>();
		String consultaStr=	" SELECT " +
								"codigo_pk as codigo_pk, " +//1
								"presupuesto_cuotas_esp as presupuesto_cuotas_esp," +//2
								"tipo_cuota as tipo_cuota, " +//3
								"nro_cuotas as nro_cuotas ," +//4
								"coalesce(porcentaje, 0) as porcentaje, " +//5
								"coalesce(valor,0) as valor  " +//3
							"FROM " +
								"odontologia.presupuesto_det_cuotas_esp " +
							"where " +
								"presupuesto_cuotas_esp=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setBigDecimal(1, codigoPk);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info("consulta-->"+ps.toString());
			while(rs.next())
			{
				DtoPresupuestoDetalleCuotasEspecialidad info= new DtoPresupuestoDetalleCuotasEspecialidad();
				info.setCodigoPk(rs.getBigDecimal("codigo_pk"));
				info.setPresupuestoCuotasEspecialidad(rs.getBigDecimal("presupuesto_cuotas_esp"));
				for(ETipoCuota tipo: DtoPresupuestoDetalleCuotasEspecialidad.ETipoCuota.values())
				{
					if(tipo.toString().equals(rs.getString("tipo_cuota")))
					{
						info.setTipoCuota(tipo);
						break;
					}
				}
				
				info.setNroCuotas(rs.getInt("nro_cuotas"));
				InfoPorcentajeValor valorOpor= new InfoPorcentajeValor();
				valorOpor.setValor(rs.getBigDecimal("valor"));
				valorOpor.setPorcentaje(rs.getDouble("porcentaje"));
				info.setValorOporcentaje(valorOpor);
				lista.add(info);
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return lista;
	}
	
	/**
	 * 
	 * Metodo para proponer los datos que vienen de la parametrica de cuotas 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static DtoPresupuestoCuotasEspecialidad proponerCargar(Connection con, int especialidad, int institucion)
	{
		DtoPresupuestoCuotasEspecialidad info= new DtoPresupuestoCuotasEspecialidad();
		String consultaStr=	"  SELECT " +
								"codigo_pk as codigo_pk, " +
								"especialidad as especialidad, " +
								"tipo_valor as tipo_valor " +
							"FROM " +
								"odontologia.cuotas_odont_especialidad " +
							"where " +
								"especialidad=? " +
								"AND institucion=? " ;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setInt(1, especialidad);
			ps.setInt(2, institucion);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info("consulta-->"+ps.toString());
			if(rs.next())
			{
				info.setCodigoPk(BigDecimal.ZERO);
				info.setPresupuestoContratado(BigDecimal.ZERO);
				info.setEspecialidad(rs.getInt("especialidad"));
				info.setTipoValor(rs.getString("tipo_valor"));
				info.setDetalle(proponerDetalle(con, rs.getBigDecimal("codigo_pk")));
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return info;
	}
	
	/**
	 * 
	 * Metodo para cargar el detalle
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<DtoPresupuestoDetalleCuotasEspecialidad> proponerDetalle(Connection con, BigDecimal codigoPk) 
	{
		ArrayList<DtoPresupuestoDetalleCuotasEspecialidad> lista= new ArrayList<DtoPresupuestoDetalleCuotasEspecialidad>();
		String consultaStr=	" SELECT " +
								"tipo_cuota AS tipo_cuota, " +
								"nro_cuotas AS nro_cuotas, " +
								"coalesce(porcentaje,0) AS porcentaje, " +
								"coalesce(valor,0) AS valor " +
							"from " +
								"odontologia.detalle_cuotas_odonto_esp " +
							"WHERE " +
								"codigo_pk_cuota_odont=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setBigDecimal(1, codigoPk);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info("consulta-->"+ps.toString());
			while(rs.next())
			{
				DtoPresupuestoDetalleCuotasEspecialidad info= new DtoPresupuestoDetalleCuotasEspecialidad();
				info.setCodigoPk(BigDecimal.ZERO);
				info.setPresupuestoCuotasEspecialidad(BigDecimal.ZERO);
				for(ETipoCuota tipo: DtoPresupuestoDetalleCuotasEspecialidad.ETipoCuota.values())
				{
					if(tipo.toString().equals(rs.getString("tipo_cuota")))
					{
						info.setTipoCuota(tipo);
						break;
					}
				}
				
				info.setNroCuotas(rs.getInt("nro_cuotas"));
				InfoPorcentajeValor valorOpor= new InfoPorcentajeValor();
				valorOpor.setValor(rs.getBigDecimal("valor"));
				valorOpor.setPorcentaje(rs.getDouble("porcentaje"));
				info.setValorOporcentaje(valorOpor);
				lista.add(info);
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return lista;
	}
	
	/**
	 * 
	 * Metodo para cargar las especialidades de los programas del presupuesto contratado o precontratado
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<Integer> cargarEspecialidadesProgramaPresupuesto(Connection con, BigDecimal codigoPkPresupuesto)
	{
		ArrayList<Integer> lista= new ArrayList<Integer>();
		String consultaStr= "SELECT " +
								"DISTINCT p.especialidad as especialidad " +
							"FROM " +
								"odontologia.presupuesto_odo_prog_serv pops " +
								"INNER JOIN odontologia.presupuesto_odo_convenio poc ON(poc.presupuesto_odo_prog_serv=pops.codigo_pk) " +
								"INNER JOIN odontologia.programas p ON(p.codigo=pops.programa) " +
							"WHERE " +
								"pops.presupuesto=? ";
		
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setBigDecimal(1, codigoPkPresupuesto);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			Log4JManager.info("consulta-->"+ps.toString());
			while(rs.next())
			{
				lista.add(rs.getInt("especialidad"));
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en cargar ", e);
		}
		return lista;
	}
}
