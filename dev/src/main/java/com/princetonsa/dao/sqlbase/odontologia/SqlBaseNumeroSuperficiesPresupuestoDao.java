package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.odontologia.InfoNumSuperficiesPresupuesto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetallePresupuestoPlanNumSuperficies;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresupuestoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * May 7, 2010 - 11:01:34 AM
 */
public class SqlBaseNumeroSuperficiesPresupuestoDao 
{
	/** 
	 * insertar encabezado 
	 */
	private static String insertEncabezadoStr="insert into  odontologia.presu_plan_tto_num_super " +
											"(" +
												"codigo_pk , " +		//1
												"presupuesto , " +		//2
												"seccion , " +			//3
												"pieza_dental , " +		//4
												"programa , " +			//5
												"hallazgo , " +			//6
												"usuario_modifica , " +	//7
												"fecha_modifica , " +	//8
												"hora_modifica,  " +	//9
												"color_letra "+				//10
											") "+ 
											"values" +
											"(" +
												"?," +//1
												"?," +//2
												"?," +//3
												"?," +//4
												"?," +//5
												"?," +//6
												"?," +//7
												"?, " +//8
												"?, "+ //9
												"? "+ //10
											")";
	
	/** 
	 * insertar detalle 
	 */
	private static String insertDetalleStr="insert into  odontologia.presu_det_ptto_num_super " +
											"(" +
												" codigo_pk, " +			//1
												"presu_plan_tto_num_super, " +			//2
												"superficie, " +						//3
												"usuario_modifica, " +					//4
												"fecha_modifica, " +					//5
												"hora_modifica  " +						//6	
											") "+ 
											"values" +
											"(" +
												"?," +//1
												"?," +//2
												"?," +//3
												"?," +//4
												"?," +//5
												"?"+//6
											")";
	
	/**
	 * 
	 * Metodo para insertar el encabezado
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal insertarEncabezado(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto)
	{
		BigDecimal secuencia= BigDecimal.ZERO;
	 	try 
		{
			secuencia= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_ptto_num_super")); 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertEncabezadoStr );
			ps.setBigDecimal(1, secuencia);
			ps.setBigDecimal(2, dto.getPresupuesto());
			ps.setString(3, dto.getSeccion().getAcronimo());
			ps.setInt(4, dto.getPiezaDental().getCodigo());
			ps.setInt(5, dto.getPrograma().getCodigo());
			ps.setInt(6, dto.getHallazgo().getCodigo());
			ps.setString(7, dto.getFHU().getUsuarioModifica());
			ps.setString(8, dto.getFHU().getFechaModificaFromatoBD());
			ps.setString(9, dto.getFHU().getHoraModifica());
			ps.setString(10, dto.getColor());
			
			if(ps.executeUpdate()<=0)
			{
				secuencia= BigDecimal.ZERO;
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
	public static boolean insertarDetalle(Connection con, DtoDetallePresupuestoPlanNumSuperficies dto)
	{
		boolean retorna= false;
		BigDecimal secuencia= BigDecimal.ZERO;
		try 
		{
			secuencia= new BigDecimal(UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_presu_dpns"));
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertDetalleStr );
			ps.setBigDecimal(1, secuencia);
			ps.setBigDecimal(2, dto.getCodigoEncabezadoPresuPlanTtoNumSuperficies());
			ps.setInt(3, dto.getSuperficie().getCodigo());
			ps.setString(4, dto.getFHU().getUsuarioModifica());
			ps.setString(5, dto.getFHU().getFechaModificaFromatoBD());
			ps.setString(6, dto.getFHU().getHoraModifica());
			
			if(ps.executeUpdate()>0)
			{
				retorna= true;
			}
			
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
			retorna= false;
		}
		return retorna;
	}
	
	/**
	 * 
	 * Metodo para eliminar los detalle x codigo encabezado
	 * @param con
	 * @param codigoEncabezadoPresuPlanTtoNumSuperficies
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminarEncabezado(Connection con, BigDecimal codigoPk)
	{
		String eliminarDetalleStr="delete from odontologia.presu_plan_tto_num_super where codigo_pk=? ";
		boolean retorna= eliminarDetalleXEncabezado(con, codigoPk);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarDetalleStr );
			ps.setBigDecimal(1, codigoPk);
			if(ps.executeUpdate()>0)
			{
				retorna= true;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
			retorna= false;
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * Metodo para eliminar los detalle x codigo encabezado
	 * @param con
	 * @param codigoEncabezadoPresuPlanTtoNumSuperficies
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminarDetalleXEncabezado(Connection con, BigDecimal codigoEncabezadoPresuPlanTtoNumSuperficies)
	{
		String eliminarDetalleStr="delete from odontologia.presu_det_ptto_num_super where presu_plan_tto_num_super=? ";
		boolean retorna= true;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, eliminarDetalleStr );
			ps.setBigDecimal(1, codigoEncabezadoPresuPlanTtoNumSuperficies);
			ps.executeUpdate();
			ps.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
			retorna= false;
		}
		return retorna;
	}
	
	/**
	 * 
	 * Metodo para realizar la busqueda de la estructura de numero de superficies
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> busquedaEncabezado(	Connection con, 
																								DtoPresupuestoPlanTratamientoNumeroSuperficies dto)
	{
		ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> lista= new ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies>();
		String consultaStr= " SELECT " +
								"e.codigo_pk as codigo_pk, " +
								"e.presupuesto as presupuesto, " +
								"e.seccion as seccion, " +
								"getintegridaddominio(e.seccion) as nombreseccion, " +
								"e.pieza_dental as pieza_dental, " +
								"p.descripcion as nombrepieza, " +
								"e.programa as programa, " +
								"pr.codigo_programa ||' '||pr.nombre as nombreprograma, " +
								"e.hallazgo as hallazgo, " +
								"ho.nombre as nombrehallazgo, " +
								"e.usuario_modifica as usuario_modifica, " +
								"to_char(e.fecha_modifica, 'DD/MM/YYYY') as fecha_modifica, " +
								"e.hora_modifica  as  hora_modifica, " +
								"e.color_letra as color " +
							"from " +
								"odontologia.presu_plan_tto_num_super e " +
								"INNER JOIN odontologia.pieza_dental p on(p.codigo_pk=e.pieza_dental) " +
								"INNER JOIN odontologia.programas pr ON(pr.codigo=e.programa) " +
								"INNER JOIN odontologia.hallazgos_odontologicos ho on(ho.consecutivo=e.hallazgo) " +
							"where " +
								"1=1 ";
		
		consultaStr+= (dto.getCodigoPk().doubleValue()>0)? " and e.codigo_pk = "+dto.getCodigoPk(): "";
		consultaStr+= (dto.getPresupuesto().doubleValue()>0)? " and e.presupuesto = "+dto.getCodigoPk(): "";
		consultaStr+= (!UtilidadTexto.isEmpty(dto.getSeccion().getAcronimo()))? " and e.seccion = '"+dto.getSeccion().getAcronimo()+"' ": "";
		consultaStr+= (dto.getPiezaDental().getCodigo()>0)? " and e.pieza_dental = "+dto.getPiezaDental()+" ": "";
		consultaStr+= (dto.getPrograma().getCodigo()>0)? " and e.programa = "+dto.getPrograma().getCodigo()+" ": "";
		consultaStr+= (dto.getHallazgo().getCodigo()>0)? " and e.hallazgo = "+dto.getHallazgo().getCodigo()+" ": "";
		consultaStr+= (dto.getColor().isEmpty())? " and e.color_letra = '"+dto.getColor()+"' ": "";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoPresupuestoPlanTratamientoNumeroSuperficies dto1= new DtoPresupuestoPlanTratamientoNumeroSuperficies();
				dto1.setCodigoPk(rs.getBigDecimal("codigo_pk"));
				dto1.setListaDetalleSuperficies(busquedaDetalle(con, new DtoDetallePresupuestoPlanNumSuperficies(dto1.getCodigoPk())));
				dto1.setFHU(new DtoInfoFechaUsuario(rs.getString("hora_modifica"), rs.getString("fecha_modifica"), rs.getString("usuario_modifica")));
				dto1.setHallazgo(new InfoDatosInt(rs.getInt("hallazgo"), rs.getString("nombrehallazgo")));
				dto1.setPiezaDental(new InfoDatosInt(rs.getInt("pieza_dental"), rs.getString("nombrepieza")));
				dto1.setPresupuesto(rs.getBigDecimal("presupuesto"));
				dto1.setPrograma(new InfoDatosInt(rs.getInt("programa"), rs.getString("nombreprograma")));
				dto1.setSeccion(new InfoIntegridadDominio(rs.getString("programa"), rs.getString("nombreprograma")));
				dto1.setColor(rs.getString("color"));
				lista.add(dto1);
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return lista;
	}

	/**
	 * 
	 * Metodo para realizar la busqueda x detalle
	 * @param dto1
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoDetallePresupuestoPlanNumSuperficies> busquedaDetalle(Connection con, DtoDetallePresupuestoPlanNumSuperficies dto) 
	{
		ArrayList<DtoDetallePresupuestoPlanNumSuperficies> lista= new ArrayList<DtoDetallePresupuestoPlanNumSuperficies>();
		String consultaStr="select " +
								"d.codigo_pk as codigo_pk, " +
								"d.presu_plan_tto_num_super as presu_plan_tto_num_super, " +
								"d.superficie as superficie, " +
								"s.nombre as nombresuperficie, " +
								"d.usuario_modifica as usuario_modifica, " +
								"to_char(d.fecha_modifica, 'dd/mm/yyyy') as fecha, " +
								"d.hora_modifica as hora " +
							"FROM " +
								"odontologia.presu_det_ptto_num_super d " +
								"INNER JOIN historiaclinica.superficie_dental s on(s.codigo=d.superficie) " +
							"where 1=1 ";
		
		consultaStr+=(dto.getCodigoEncabezadoPresuPlanTtoNumSuperficies().doubleValue()>0)?" and d.presu_plan_tto_num_super= "+dto.getCodigoEncabezadoPresuPlanTtoNumSuperficies():"";
		consultaStr+=(dto.getCodigoPk().doubleValue()>0)?" and d.codigo_pk= "+dto.getCodigoPk():"";
		consultaStr+=(dto.getSuperficie().getCodigo()>0)?" and d.superficie= "+dto.getSuperficie().getCodigo():"";
	
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoDetallePresupuestoPlanNumSuperficies dto1= new DtoDetallePresupuestoPlanNumSuperficies();
				dto1.setCodigoEncabezadoPresuPlanTtoNumSuperficies(rs.getBigDecimal("presu_plan_tto_num_super"));
				dto1.setCodigoPk(rs.getBigDecimal("codigo_pk"));
				dto1.setFHU(new DtoInfoFechaUsuario(rs.getString("hora"), rs.getString("fecha"), rs.getString("usuario_modifica")));
				dto1.setSuperficie(new InfoDatosInt(rs.getInt("superficie"), rs.getString("nombresuperficie")));
				lista.add(dto1);
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return lista;
	}
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal obtenerCodigoPresuPlanTtoProgSer(Connection con, DtoPlanTratamientoPresupuesto dto)
	{
		BigDecimal retorna= BigDecimal.ZERO;
		String consulta=" SELECT " +
							"min(codigo_pk) " +
						"FROM " +
							"odontologia.presu_plan_tto_prog_ser " +
						"WHERE " +
							"det_plan_tratamiento=? " +
							"and programa=? " +
							"and presupuesto=? " +
							"and activo='"+ConstantesBD.acronimoSi+"' ";
		
		consulta+= (dto.getProgramaServicioPlanTratamientoFK().doubleValue()>0)?" and programa_servicio_plan_t="+dto.getProgramaServicioPlanTratamientoFK()+" ":" and programa_servicio_plan_t is null ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, dto.getDetPlanTratamiento());
			ps.setDouble(2, dto.getPrograma().doubleValue());
			ps.setBigDecimal(3, dto.getPresupuesto());
						
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			Log4JManager.info(ps.toString());
			
			if(rs.next())
			{
				retorna= rs.getBigDecimal(1);
			}
			
			Log4JManager.info("retornma--->"+retorna);
			
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return retorna;
							
	}
	
	/**
	 * 
	 * Metodo para cargar la informacion de numSuperficies del plan tratamiento presupuesto
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPresupuesto(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie)
	{
		ArrayList<InfoNumSuperficiesPresupuesto> lista= new ArrayList<InfoNumSuperficiesPresupuesto>();
		String consultaStr=	" SELECT " +
								"p1.superficie as codigosuperficie, " +
								"'' as nombresuperficie, " +
								"p.color_letra as color " +
							"FROM " +
								"odontologia.presu_plan_tto_num_super p " +
								"INNER JOIN odontologia.presu_det_ptto_num_super p1 on(p.codigo_pk=p1.presu_plan_tto_num_super) " +
							"where " +
								"p.presupuesto=? " +
								"and p.seccion=? " +
								"and p.pieza_dental=? " +
								"and p.programa=? " +
								"and p.hallazgo=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setBigDecimal(1, dto.getPresupuesto());
			ps.setString(2, dto.getSeccion().getAcronimo());
			ps.setInt(3, dto.getPiezaDental().getCodigo());
			ps.setInt(4, dto.getPrograma().getCodigo());
			ps.setInt(5, dto.getHallazgo().getCodigo());
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			Log4JManager.info("consulta-->"+ps.toString());
			
			String colorXDefecto="";
			while(rs.next())
			{
				InfoNumSuperficiesPresupuesto info= new InfoNumSuperficiesPresupuesto();
				info.setCodigoSuperficie(rs.getInt("codigosuperficie"));
				info.setActivo(true);
				info.setColor(rs.getString("color"));
				info.setMarcarXDefecto(codigoSuperficie==info.getCodigoSuperficie());
				if(info.isMarcarXDefecto())
				{
					colorXDefecto=info.getColor();
				}
				info.setNombreSuperficie(rs.getString("nombresuperficie"));
				lista.add(info);
			}
			
			//teniendo el por defecto evaluamos si es modificable
			for(InfoNumSuperficiesPresupuesto info: lista)
			{
				if(info.getColor().equals(colorXDefecto))
				{
					if(info.isMarcarXDefecto())
					{
						info.setModificable(false);
					}
					else
					{
						info.setModificable(true);
					}
				}
				else
				{
					info.setModificable(false);
				}
			}
			
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return lista;
	}

	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPlanTratamiento(Connection con, DtoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie)
	{
		ArrayList<InfoNumSuperficiesPresupuesto> lista= new ArrayList<InfoNumSuperficiesPresupuesto>();
		String consultaStr=	" SELECT " +
								"p1.superficie_dental as codigosuperficie, " +
								"'' as nombresuperficie, " +
								"p.color_letra as color " +
							"FROM " +
								"odontologia.programas_hallazgo_pieza p " +
								"INNER JOIN odontologia.superficies_x_programa p1 on(p.codigo_pk=p1.prog_hallazgo_pieza) " +
							"where " +
								"p.plan_tratamiento=? " +
								"and p.seccion=? " +
								"and p.pieza_dental=? " +
								"and p.programa=? " +
								"and p.hallazgo=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setBigDecimal(1, dto.getPlanTratamiento());
			ps.setString(2, dto.getSeccion().getAcronimo());
			ps.setInt(3, dto.getPiezaDental().getCodigo());
			ps.setInt(4, dto.getPrograma().getCodigo());
			ps.setInt(5, dto.getHallazgo().getCodigo());
			
			Log4JManager.info(ps.toString());
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			String colorXDefecto="";
			while(rs.next())
			{
				InfoNumSuperficiesPresupuesto info= new InfoNumSuperficiesPresupuesto();
				info.setCodigoSuperficie(rs.getInt("codigosuperficie"));
				info.setActivo(true);
				info.setColor(rs.getString("color"));
				info.setMarcarXDefecto(codigoSuperficie==info.getCodigoSuperficie());
				if(info.isMarcarXDefecto())
				{
					colorXDefecto=info.getColor();
				}
				info.setNombreSuperficie(rs.getString("nombresuperficie"));
				lista.add(info);
			}
			
			//teniendo el por defecto evaluamos si es modificable
			for(InfoNumSuperficiesPresupuesto info: lista)
			{
				if(info.getColor().equals(colorXDefecto))
				{
					if(info.isMarcarXDefecto())
					{
						info.setModificable(false);
					}
					else
					{
						info.setModificable(true);
					}
				}
				else
				{
					info.setModificable(false);
				}
			}
			
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return lista;
	}
	
	
	/**
	 * 
	 * Metodo para .......
	 * @param superficiesAplicaPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static String obtenerColorLetra(ArrayList<InfoNumSuperficiesPresupuesto> superficiesAplicaPresupuesto) 
	{
		for(InfoNumSuperficiesPresupuesto dto: superficiesAplicaPresupuesto)
		{
			if(dto.isMarcarXDefecto())
			{
				return dto.getColor();
			}
		}
		return "";
	}
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dtoBusqueda
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static DtoSuperficiesPorPrograma obtenerSuperficieXProgramaPlanTratamiento(Connection con, DtoSuperficiesPorPrograma dtoBusqueda)
	{
		DtoSuperficiesPorPrograma retorna= new DtoSuperficiesPorPrograma();
		String consultaStr=	" SELECT " +
								"s.codigo_pk as codigo_pk," +
								"s.superficie_dental as superficie_dental," +
								"s.prog_hallazgo_pieza as prog_hallazgo_pieza," +
								"s.det_plan_trata as det_plan_trata, " +
								"php.color_letra as color_letra " +
							"from " +
								"odontologia.programas_hallazgo_pieza php " +
								"INNER JOIN odontologia.superficies_x_programa s ON(s.prog_hallazgo_pieza=php.codigo_pk) " +
							"where " +
								"det_plan_trata=? " +
								"and superficie_dental=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setInt(1, dtoBusqueda.getDetPlanTratamiento());
			ps.setInt(2, dtoBusqueda.getSuperficieDental());
			Log4JManager.info(ps.toString());
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna.setDetPlanTratamiento(rs.getInt("det_plan_trata"));
				retorna.setProgHallazgoPieza(new DtoProgHallazgoPieza(rs.getInt("prog_hallazgo_pieza"), rs.getString("color_letra")));
				retorna.setSuperficieDental(rs.getInt("superficie_dental"));
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dtoBusqueda
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static DtoProgHallazgoPieza obtenerProgramaHallazgoPiezaPlanTratamiento(Connection con, DtoProgHallazgoPieza dtoBusqueda)
	{
		DtoProgHallazgoPieza retorna= null;
		String consultaStr=	"select  " +
								"codigo_pk as codigo_pk  , " +
								"plan_tratamiento as plan_tratamiento, " +
								"programa as programa, " +
								"hallazgo as hallazgo, " +
								"pieza_dental as pieza_dental, " +
								"seccion as seccion, " +
								"color_letra as color_letra " +
							"from " +
								"odontologia.programas_hallazgo_pieza " +
							"where " +
							"1=1";
		
		if(dtoBusqueda.getCodigoPk()>0)
		{
			consultaStr+=" and codigo_pk= "+dtoBusqueda.getCodigoPk();
		}
		else
		{
			consultaStr+=(!UtilidadTexto.isEmpty(dtoBusqueda.getColorLetra()))?" and color_letra= '"+dtoBusqueda.getColorLetra()+"' ":" ";
			consultaStr+=(dtoBusqueda.getHallazgo()>0)?" and hallazgo= "+dtoBusqueda.getHallazgo()+" ":" ";
			consultaStr+=(!UtilidadTexto.isEmpty(dtoBusqueda.getSeccion()))?" and seccion= '"+dtoBusqueda.getSeccion()+"' ":" ";
			consultaStr+=(dtoBusqueda.getPiezaDental()>0)?" and pieza_dental= '"+dtoBusqueda.getPiezaDental()+"' ":" and pieza_dental is null ";
			consultaStr+=(dtoBusqueda.getPrograma()>0)?" and programa= '"+dtoBusqueda.getPrograma()+"' ":" ";
			consultaStr+=(dtoBusqueda.getPlanTratamiento()>0)?" and plan_tratamiento= '"+dtoBusqueda.getPlanTratamiento()+"' ":" ";
		}	
		
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			Log4JManager.info(ps.toString());
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				retorna= new DtoProgHallazgoPieza();
				retorna.setCodigoPk(rs.getInt("codigo_pk"));
				retorna.setPlanTratamiento(rs.getInt("plan_tratamiento"));
				retorna.setColorLetra(rs.getString("color_letra"));
				retorna.setHallazgo(rs.getInt("hallazgo"));
				retorna.setPiezaDental(rs.getInt("pieza_dental"));
				retorna.setPrograma(rs.getInt("programa"));
				retorna.setSeccion(rs.getString("seccion"));
				retorna.setSuperficiesPorPrograma(obtenerSuperficiesXProgramaPlanTratamiento(con, rs.getBigDecimal("codigo_pk")));
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return retorna;
	}
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dtoBusqueda
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<DtoSuperficiesPorPrograma> obtenerSuperficiesXProgramaPlanTratamiento(Connection con, BigDecimal codigoPkEnca)
	{
		ArrayList<DtoSuperficiesPorPrograma> lista= new ArrayList<DtoSuperficiesPorPrograma>();
		String consultaStr=	" SELECT " +
								"s.codigo_pk as codigo_pk," +
								"s.superficie_dental as superficie_dental," +
								"s.prog_hallazgo_pieza as prog_hallazgo_pieza," +
								"s.det_plan_trata as det_plan_trata, " +
								"php.color_letra as color_letra " +
							"from " +
								"odontologia.programas_hallazgo_pieza php " +
								"INNER JOIN odontologia.superficies_x_programa s ON(s.prog_hallazgo_pieza=php.codigo_pk) " +
							"where " +
								"prog_hallazgo_pieza=? ";
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr );
			ps.setBigDecimal(1, codigoPkEnca);
			Log4JManager.info(ps.toString());
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				DtoSuperficiesPorPrograma retorna= new DtoSuperficiesPorPrograma();
				retorna.setDetPlanTratamiento(rs.getInt("det_plan_trata"));
				retorna.setProgHallazgoPieza(new DtoProgHallazgoPieza(rs.getInt("prog_hallazgo_pieza"), rs.getString("color_letra")));
				retorna.setSuperficieDental(rs.getInt("superficie_dental"));
				lista.add(retorna);
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			Log4JManager.info("ERROR en insert ", e);
		}
		return lista;
	}
	
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean guardarSuperficiesPlanTratamiento(Connection con, DtoSuperficiesPorPrograma dto)
	{
		boolean resultado= false;
		String insertarSuperficie=
					"INSERT INTO odontologia.superficies_x_programa (" +
						"codigo_pk, "+
						"superficie_dental, "+
						"prog_hallazgo_pieza, "+
						"usuario_modifica, "+
						"fecha_modifica, "+
						"hora_modifica, " +
						"det_plan_trata" +
					") " +
					"VALUES(" +
						"?, ?, ?, ?, ?, ?, ?" +
					")";
	
		int seqSeuperficiesXPrograma=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_superficies_x_programa");
		
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, insertarSuperficie);
			ps.setDouble(1, seqSeuperficiesXPrograma);
			ps.setInt(2, dto.getSuperficieDental());
			ps.setInt(3, dto.getProgHallazgoPieza().getCodigoPk());
			ps.setString(4, dto.getInfoFechaUsuario().getUsuarioModifica());
			ps.setString(5, dto.getInfoFechaUsuario().getFechaModificaFromatoBD());
			ps.setString(6, dto.getInfoFechaUsuario().getHoraModifica());
			ps.setDouble(7, dto.getDetPlanTratamiento());
	
			resultado=ps.executeUpdate()>0;
			
			Log4JManager.info(ps.toString());
			
			ps.close();
				
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			resultado= false;
		}
				
		return resultado;
	}
}