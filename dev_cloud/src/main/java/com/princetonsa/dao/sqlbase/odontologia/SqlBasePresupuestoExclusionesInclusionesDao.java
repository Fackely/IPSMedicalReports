package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.odontologia.InfoDetalleHistoricoIncExcPresupuesto;
import util.odontologia.InfoInclusionExclusionBoca;
import util.odontologia.InfoInclusionExclusionPiezaSuperficie;
import util.odontologia.InfoSeccionInclusionExclusion;
import util.odontologia.InfoSuperficiePkDetPlan;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetalleExclusionSuperficies;
import com.princetonsa.dto.odontologia.DtoExclusionPresupuesto;
import com.princetonsa.dto.odontologia.DtoInclusionesPresupuesto;
import com.princetonsa.dto.odontologia.DtoPiezaDental;
import com.princetonsa.dto.odontologia.DtoSuperficieDental;

/**
 * 
 * @author axioma
 *
 */
public class SqlBasePresupuestoExclusionesInclusionesDao 
{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBasePresupuestoExclusionesInclusionesDao.class);
	
	/** 
	 * 
	 */
	private static String insertStrExclusion="insert into  odontologia.exclusiones_presupuesto " +
											"(" +
												" codigo_pk, " +
												"programa, " +
												"servicio, " +
												"valor," +
												"exclu_presu_encabezado " +
											") "+ 
											"values" +
											"(" +
												"?," +//1
												"?," +//2
												"?," +//3
												"?," +//4
												"? "+ //5
											")";
	
	private static String insertarDetalleExclusion=" insert into odontologia.det_exclusiones_superficies (codigo_pk, exclusion_presupuesto, det_plan_tratamiento) values (?,?,?)";
	
	/**
	 * 
	 */
	private static String insertStrInclusion="	insert into  odontologia.inclusiones_presupuesto" +
												"( " +
													" codigo_pk , " +
													" valor," +
													" inclu_presu_encabezado," +
													" programa_hallazgo_pieza " +
												")" +
												"values" +
												"(" +
													"?," +//1
													"?," +//2
													"?," +//3
													"? " +//3
												")";
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double guardarExclusion(Connection con, DtoExclusionPresupuesto dto  )
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		if(dto.getCodigoExcluPresuEncabezado() > ConstantesBD.codigoNuncaValidoLong){
 			
			try 
			{
				secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_exclusiones_presupuesto"); // Por ejecutar
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertStrExclusion );
				
				ps.setBigDecimal(1, new BigDecimal(secuencia));
				
				if(dto.isUtilizaProgramas())
				{
					ps.setInt(2, dto.getProgramaOservicio());
					ps.setNull(3, Types.INTEGER);
				}
				else
				{
					ps.setNull(2, Types.INTEGER);
					ps.setInt(3, dto.getProgramaOservicio());
				}
				
				ps.setBigDecimal(4, dto.getValor());
			
				ps.setLong(5, dto.getCodigoExcluPresuEncabezado());
				
				if(ps.executeUpdate()<=0)
				{
					ps.close();
					return ConstantesBD.codigoNuncaValidoDoubleNegativo;
				}
				logger.info("INSERTAR Exclusion "+ps);
				ps.close();
				dto.setCodigoPk(new BigDecimal(secuencia));
				
				for(DtoDetalleExclusionSuperficies dtoDetalle: dto.getDetalleSuperficies())
				{
					dtoDetalle.setCodigoPkExclusionPresupuesto(dto.getCodigoPk());
					if(guardarDetalleExclusion(con, dtoDetalle)<=0)
					{
						return ConstantesBD.codigoNuncaValidoDoubleNegativo;
					}
				}
				
			}
			catch (SQLException e) 
			{
				logger.error("ERROR en insert ", e);
				secuencia=0;
			}
 		}

		return secuencia;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	private static double guardarDetalleExclusion(Connection con, DtoDetalleExclusionSuperficies dto  )
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	try 
		{
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_det_exclusiones_sup"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarDetalleExclusion );
			ps.setBigDecimal(1, new BigDecimal(secuencia));
			ps.setBigDecimal(2, dto.getCodigoPkExclusionPresupuesto());
			ps.setBigDecimal(3, dto.getCodigoPkDetPlanTratamiento());
			dto.setCodigoPk(new BigDecimal(secuencia));
			
			if(ps.executeUpdate()<=0)
			{
				ps.close();
				return ConstantesBD.codigoNuncaValidoDoubleNegativo;
			}
			logger.info("INSERTAR Exclusion "+ps);
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert ", e);
			secuencia=0;
		}
			
		return secuencia;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double guardarInclusion(Connection con, DtoInclusionesPresupuesto dto )
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_inclusiones_presupuesto"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertStrInclusion );
			ps.setBigDecimal(1, new BigDecimal(secuencia));
			ps.setBigDecimal(2, dto.getValor());
			ps.setLong(3, dto.getCodigoIncluPresuEncabezado());
			ps.setLong(4, dto.getCodigoProgramaHallazgoPieza());
			
			logger.info("INSERTAR Inclusion "+ps);
			
			if(ps.executeUpdate()<=0)
			{
				ps.close();
				return ConstantesBD.codigoNuncaValidoDoubleNegativo;
			}
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert ", e);
			secuencia=0;
		}
		return secuencia;
	}

	/**
	 * 
	 * @param planTratamiento
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoSeccionInclusionExclusion cargarInclusionesPlanTratamiento(
			BigDecimal planTratamiento,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{

		InfoSeccionInclusionExclusion info= new InfoSeccionInclusionExclusion();
		info.setListaPiezasDentalesSuperficies(obtenerListaPiezasDentalesInclusion(planTratamiento, estadoProgramasServiciosPlanTratamiento, estadoAutorizacion, utilizaProgramas));
		info.setListaBoca(obtenerListaBocaInclusion(planTratamiento, estadoProgramasServiciosPlanTratamiento, estadoAutorizacion, utilizaProgramas));
		return info;
	}

	/**
	 * 
	 * @param planTratamiento
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	private static ArrayList<InfoInclusionExclusionPiezaSuperficie> obtenerListaPiezasDentalesInclusion(
			BigDecimal planTratamiento,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{
		ArrayList<InfoInclusionExclusionPiezaSuperficie> array= new ArrayList<InfoInclusionExclusionPiezaSuperficie>();
		ArrayList<String> listaAgrupacion= new ArrayList<String>();
		HashMap<String, Integer> mapPos= new HashMap<String, Integer>();
		String unique="";
		
		String consultaStr= (utilizaProgramas)? "SELECT t.codigo_detalle, t.codigo_pieza, t.nombre_pieza,  t.codigo_superficie, "+
												"t.nombre_superficie, t.hallazgo, t.codigo_programa_servicio, t.codigo_mostrar, t.nombre_programa_servicio, t.seccion, "+
												"t.codigoprogramahallazgopieza, "+
												"(SELECT CASE WHEN (SELECT DISTINCT count (inclusiones.programa_hallazgo_pieza) "+
												"  FROM odontologia.inclusiones_presupuesto inclusiones INNER JOIN odontologia.inclu_presu_encabezado encabezado "+
												"  ON 	(inclusiones.inclu_presu_encabezado = encabezado.codigo_pk AND encabezado.estado = '"+ConstantesIntegridadDominio.acronimoPrecontratado+"' AND "+
												"inclusiones.programa_hallazgo_pieza = t.codigoprogramahallazgopieza) ) > 0 THEN 'S' ELSE 'N' END ) AS precontratada "+
												"FROM "+
												"( "+
													"SELECT DISTINCT " +
													"dpt.codigo_pk as codigo_detalle, " +
													"dpt.pieza_dental as codigo_pieza, " +
													"p.descripcion as nombre_pieza,  " +
													"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codigo_superficie, " +
													"historiaclinica.getNombreSuperficie(ssc.sector, dpt.pieza_dental) as nombre_superficie, " +
													"dpt.hallazgo as hallazgo, "+
													"pspt.programa as codigo_programa_servicio, "+
													"prog.codigo_programa as codigo_mostrar, " +
													"prog.nombre as nombre_programa_servicio,  "+
													"dpt.seccion as seccion, " +
													"(	select " +
															"php.codigo_pk " +
														"FROM " +
															"odontologia.programas_hallazgo_pieza php " +
															"INNER JOIN odontologia.superficies_x_programa sxp ON(sxp.prog_hallazgo_pieza=php.codigo_pk) " +
														"where " +
															" sxp.det_plan_trata = dpt.codigo_pk " +
															"AND pspt.programa = php.programa " +
													") as codigoprogramahallazgopieza " +
													"FROM " +
														"odontologia.det_plan_tratamiento dpt " +
														"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=dpt.pieza_dental) " +
														"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
														"INNER JOIN odontologia.programas prog ON(prog.codigo=pspt.programa) " +
														"LEFT OUTER JOIN historiaclinica.superficie_dental sd ON(sd.codigo=dpt.superficie) " +
														"LEFT OUTER JOIN odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=sd.codigo AND dpt.pieza_dental=ssc.pieza) " +
													"WHERE " +
														"dpt.plan_tratamiento=? " +
														"and dpt.seccion in('"+ConstantesIntegridadDominio.acronimoOtro+"', '"+ConstantesIntegridadDominio.acronimoDetalle+"') " +
														"AND pspt.estado_programa='"+estadoProgramasServiciosPlanTratamiento+"' " +
														"AND pspt.inclusion='"+ConstantesBD.acronimoSi+"' " +
														(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_inclu='"+estadoAutorizacion+"' AND pspt.estado_autorizacion_exclu IS NULL ":" ") +
														"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
														"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
													"order by " +
														"p.descripcion "+
													") t"
												:
												"SELECT DISTINCT " +
													"dpt.codigo_pk as codigo_detalle, " +
													"dpt.pieza_dental as codigo_pieza, " +
													"p.descripcion as nombre_pieza,  " +
													"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codigo_superficie, " +
													"historiaclinica.getNombreSuperficie(ssc.sector, dpt.pieza_dental) as nombre_superficie, " +
													"dpt.hallazgo as hallazgo, "+
													"pspt.servicio as codigo_programa_servicio, " +
													"getnombreservicio(pspt.servicio, "+ConstantesBD.codigoTarifarioCups+") as nombre_programa_servicio,  " +
													"dpt.seccion as seccion " +
												"FROM " +
													"odontologia.det_plan_tratamiento dpt " +
													"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=dpt.pieza_dental) " +
													"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
													"LEFT OUTER JOIN historiaclinica.superficie_dental sd ON(sd.codigo=dpt.superficie) " +
													"LEFT OUTER JOIN odontologia.sector_superficie_cuadrante ssc ON(ssc.superficie=sd.codigo AND dpt.pieza_dental=ssc.pieza) " +
												"WHERE " +
													"dpt.plan_tratamiento=? " +
													"and dpt.seccion in('"+ConstantesIntegridadDominio.acronimoOtro+"', '"+ConstantesIntegridadDominio.acronimoDetalle+"') " +
													"AND pspt.estado_servicio='"+estadoProgramasServiciosPlanTratamiento+"' " +
													"AND pspt.inclusion='"+ConstantesBD.acronimoSi+"' " +
													(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_inclu='"+estadoAutorizacion+"' AND pspt.estado_autorizacion_exclu IS NULL ":" ") +
													"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
													"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
												"order by  " +
													"p.descripcion ";
													
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, planTratamiento);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				unique=	rs.getBigDecimal("codigoprogramahallazgopieza")
						+"_"+rs.getString("seccion")
						+"_"+rs.getDouble("codigo_pieza")
						+"_"+rs.getBigDecimal("hallazgo")
						+"_"+rs.getDouble("codigo_programa_servicio");
				
				if(!listaAgrupacion.contains(unique))
				{
					listaAgrupacion.add(unique);
					InfoInclusionExclusionPiezaSuperficie info= new InfoInclusionExclusionPiezaSuperficie();
					info.setCodigoPkProgramaHallazgoPieza(rs.getBigDecimal("codigoprogramahallazgopieza"));
					info.setActivo(false);
					info.setPiezaDental(new DtoPiezaDental(rs.getDouble("codigo_pieza"), rs.getString("nombre_pieza"), "", ""));
					info.setProgramaOservicio(new InfoDatosDouble(rs.getDouble("codigo_programa_servicio"), rs.getString("nombre_programa_servicio"), rs.getString("codigo_mostrar")));
					info.setPrecontratada(rs.getString("precontratada"));
					DtoSuperficieDental dtoSuperficie= new DtoSuperficieDental(rs.getDouble("codigo_superficie"), rs.getString("nombre_superficie"), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
					info.getSuperficies().add(new InfoSuperficiePkDetPlan(dtoSuperficie, rs.getBigDecimal("codigo_detalle")));
					info.setHallazgo(rs.getBigDecimal("hallazgo"));
					info.setSeccion(rs.getString("seccion"));
					array.add(info);
					mapPos.put(unique, array.size()-1);
				}	
				else
				{
					DtoSuperficieDental dtoSuperficie= new DtoSuperficieDental(rs.getDouble("codigo_superficie"), rs.getString("nombre_superficie"), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
					array.get(mapPos.get(unique)).getSuperficies().add(new InfoSuperficiePkDetPlan(dtoSuperficie, rs.getBigDecimal("codigo_detalle")));
				}
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return array;
	}
	
	/**
	 * 
	 * @param planTratamiento
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	private static ArrayList<InfoInclusionExclusionBoca> obtenerListaBocaInclusion(
			BigDecimal planTratamiento,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{
		ArrayList<InfoInclusionExclusionBoca> array= new ArrayList<InfoInclusionExclusionBoca>();
		String consultaStr= (utilizaProgramas)? "SELECT t.codigo_detalle, codigo_programa_servicio, "+
												"t.codigo_mostrar, t.hallazgo, t.nombre_programa_servicio, "+
												"t.codigoprogramahallazgopieza, "+
												"(SELECT CASE WHEN (SELECT DISTINCT count (inclusiones.programa_hallazgo_pieza) "+
												" FROM odontologia.inclusiones_presupuesto inclusiones INNER JOIN odontologia.inclu_presu_encabezado encabezado "+
												" ON (inclusiones.inclu_presu_encabezado = encabezado.codigo_pk AND encabezado.estado = '"+ConstantesIntegridadDominio.acronimoPrecontratado+"' AND "+
												" inclusiones.programa_hallazgo_pieza = t.codigoprogramahallazgopieza) ) > 0 THEN 'S' ELSE 'N' END ) AS precontratada "+
												"FROM "+
												"( "+"SELECT DISTINCT " +
													"dpt.codigo_pk as codigo_detalle, " +
													"pspt.programa as codigo_programa_servicio, " +
													"prog.codigo_programa as codigo_mostrar, " +
													"prog.nombre as nombre_programa_servicio,  "+
													"dpt.hallazgo as hallazgo, " +
													"(	select " +
															"php.codigo_pk " +
														"FROM " +
															"odontologia.programas_hallazgo_pieza php " +
															"INNER JOIN odontologia.superficies_x_programa sxp ON(sxp.prog_hallazgo_pieza=php.codigo_pk) " +
														"where " +
															" sxp.det_plan_trata = dpt.codigo_pk " +
															"AND pspt.programa = php.programa " +
													") as codigoprogramahallazgopieza " +
													"FROM " +
														"odontologia.det_plan_tratamiento dpt " +
														"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
														"INNER JOIN odontologia.programas prog ON(prog.codigo=pspt.programa) " +
														
													"WHERE " +
														"dpt.plan_tratamiento=? " +
														"and dpt.seccion ='"+ConstantesIntegridadDominio.acronimoBoca+"' " +
														"AND pspt.estado_programa='"+estadoProgramasServiciosPlanTratamiento+"' " +
														"AND pspt.inclusion='"+ConstantesBD.acronimoSi+"' " +
														(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_inclu='"+estadoAutorizacion+"' AND pspt.estado_autorizacion_exclu IS NULL ":" ") +
														"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
														"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
													"order by " +
														"prog.nombre "+
												") t"
												:
												"SELECT DISTINCT " +
													"dpt.codigo_pk as codigo_detalle, " +
													"pspt.servicio as codigo_programa_servicio, " +
													"getnombreservicio(pspt.servicio, "+ConstantesBD.codigoTarifarioCups+") as nombre_programa_servicio,  " +
													"dpt.hallazgo as hallazgo " +
												"FROM " +
													"odontologia.det_plan_tratamiento dpt " +
													"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
												"WHERE " +
													"dpt.plan_tratamiento=? " +
													"and dpt.seccion ='"+ConstantesIntegridadDominio.acronimoBoca+"' " +
													"AND pspt.estado_servicio='"+estadoProgramasServiciosPlanTratamiento+"' " +
													"AND pspt.inclusion='"+ConstantesBD.acronimoSi+"' " +
													(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_inclu='"+estadoAutorizacion+"' AND pspt.estado_autorizacion_exclu IS NULL ":" ") +
													"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
													"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
												"order by  " +
													"pspt.servicio ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, planTratamiento);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoInclusionExclusionBoca info= new InfoInclusionExclusionBoca();
				info.setCodigoPkDetPlanTratamiento(rs.getBigDecimal("codigo_detalle"));
				info.setActivo(false);
				info.setProgramaOservicio(new InfoDatosDouble(rs.getDouble("codigo_programa_servicio"), rs.getString("nombre_programa_servicio"), rs.getString("codigo_mostrar")));
				info.setHallazgo(rs.getBigDecimal("hallazgo"));
				info.setCodigoPkProgramaHallazgoPieza(rs.getBigDecimal("codigoprogramahallazgopieza"));
				info.setPrecontratada(rs.getString("precontratada"));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return array;
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	public static InfoSeccionInclusionExclusion cargarExclusionesPresupuesto(
			BigDecimal codigoPresupuesto,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{
		InfoSeccionInclusionExclusion info= new InfoSeccionInclusionExclusion();
		info.setListaPiezasDentalesSuperficies(obtenerListaPiezasDentalesExclusion(codigoPresupuesto, estadoProgramasServiciosPlanTratamiento, estadoAutorizacion, utilizaProgramas));
		info.setListaBoca(obtenerListaBocaExclusion(codigoPresupuesto, estadoProgramasServiciosPlanTratamiento,estadoAutorizacion, utilizaProgramas));
		return info;
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	private static ArrayList<InfoInclusionExclusionPiezaSuperficie> obtenerListaPiezasDentalesExclusion(
			BigDecimal codigoPresupuesto,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{
		//primero hacemos una consulta de todas las exclusiones a nivel de plan de tratamiento

		ArrayList<InfoInclusionExclusionPiezaSuperficie> array= new ArrayList<InfoInclusionExclusionPiezaSuperficie>();
		ArrayList<String> listaAgrupacion= new ArrayList<String>();
		HashMap<String, Integer> mapPos= new HashMap<String, Integer>();
		String unique="";
		
		
		String consultaStr= (utilizaProgramas)? 
												"SELECT DISTINCT " +
													"dpt.codigo_pk as codigo_detalle, " +
													"dpt.plan_tratamiento as plan_tratamiento, " +
													"dpt.pieza_dental as codigo_pieza, " +
													"p.descripcion as nombre_pieza,  " +
													"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codigo_superficie, " +
													"coalesce(sd.nombre, '') as nombre_superficie, " +
													"dpt.hallazgo as hallazgo, " +
													"dpt.seccion as seccion, "+
													"pspt.programa as codigo_programa_servicio, " +
													"prog.codigo_programa ||' - '|| prog.nombre as nombre_programa_servicio, " +
													"(	select " +
															"php.codigo_pk " +
														"FROM " +
															"odontologia.programas_hallazgo_pieza php " +
															"INNER JOIN odontologia.superficies_x_programa sxp ON(sxp.prog_hallazgo_pieza=php.codigo_pk) " +
														"where " +
															"php.plan_tratamiento=dpt.plan_tratamiento " +
															"and php.programa=pspt.programa " +
															"and php.hallazgo=dpt.hallazgo " +
															"and php.pieza_dental=dpt.pieza_dental " +
															"and php.seccion=dpt.seccion " +
															"and sxp.superficie_dental= dpt.superficie" +
													") as codigoprogramahallazgopieza " + 
												"FROM " +
													"odontologia.presupuesto_odontologico pre " +
													"INNER JOIN odontologia.det_plan_tratamiento dpt ON(pre.plan_tratamiento=dpt.plan_tratamiento) " +
													"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=dpt.pieza_dental) " +
													"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
													"INNER JOIN odontologia.programas prog ON(prog.codigo=pspt.programa) " +
													"LEFT OUTER JOIN historiaclinica.superficie_dental sd ON(sd.codigo=dpt.superficie) " +
												"WHERE " +
													"pre.codigo_pk=? " +
													"and dpt.seccion in('"+ConstantesIntegridadDominio.acronimoOtro+"', '"+ConstantesIntegridadDominio.acronimoDetalle+"') " +
													"AND pspt.estado_programa='"+estadoProgramasServiciosPlanTratamiento+"' " +
													"AND pspt.exclusion='"+ConstantesBD.acronimoSi+"' " +
													(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_exclu='"+estadoAutorizacion+"' ":" ") +
													"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
													"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
												"order by " +
													"p.descripcion "
												:
												"SELECT DISTINCT " +
													"dpt.codigo_pk as codigo_detalle, " +
													"dpt.plan_tratamiento as plan_tratamiento, " +
													"dpt.pieza_dental as codigo_pieza, " +
													"p.descripcion as nombre_pieza,  " +
													"coalesce(dpt.superficie, "+ConstantesBD.codigoNuncaValido+") as codigo_superficie, " +
													"coalesce(sd.nombre, '') as nombre_superficie, " +
													"dpt.hallazgo as hallazgo, " +
													"dpt.seccion as seccion, "+
													"pspt.servicio as codigo_programa_servicio, " +
													"getnombreservicio(pspt.servicio, "+ConstantesBD.codigoTarifarioCups+") as nombre_programa_servicio  " +
												"FROM " +
													"odontologia.presupuesto_odontologico pre " +
													"INNER JOIN odontologia.det_plan_tratamiento dpt ON(pre.plan_tratamiento=dpt.plan_tratamiento) " +
													"INNER JOIN odontologia.pieza_dental p ON(p.codigo_pk=dpt.pieza_dental) " +
													"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
													"LEFT OUTER JOIN historiaclinica.superficie_dental sd ON(sd.codigo=dpt.superficie) " +
												"WHERE " +
													"pre.codigo_pk=? " +
													"and dpt.seccion in('"+ConstantesIntegridadDominio.acronimoOtro+"', '"+ConstantesIntegridadDominio.acronimoDetalle+"') " +
													"AND pspt.estado_servicio='"+estadoProgramasServiciosPlanTratamiento+"' " +
													"AND pspt.exclusion='"+ConstantesBD.acronimoSi+"' " +
													(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_exclu='"+estadoAutorizacion+"' ":" ") +
													"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
													"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
												"order by  " +
													"p.descripcion ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPresupuesto);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				////x cada resultado que devuelva debemos evaluar si está en el presupuesto odontologico
				BigDecimal codigoPkPresupuestoProgServ= SqlBasePresupuestoOdontologico.existeProgramaServicioPlanTratamientoEnPresupuesto(rs.getInt("codigo_pieza"), rs.getInt("codigo_superficie"), rs.getInt("hallazgo"), rs.getBigDecimal("codigo_programa_servicio"), utilizaProgramas, codigoPresupuesto,rs.getString("seccion"));
				
				unique=	rs.getBigDecimal("codigoprogramahallazgopieza")
				+"_"+rs.getString("seccion")
				+"_"+rs.getDouble("codigo_pieza")
				+"_"+rs.getBigDecimal("hallazgo")
				+"_"+rs.getDouble("codigo_programa_servicio");
				
				if(codigoPkPresupuestoProgServ.doubleValue()>0)
				{
					if(!listaAgrupacion.contains(unique))
					{
						listaAgrupacion.add(unique);
						InfoInclusionExclusionPiezaSuperficie info= new InfoInclusionExclusionPiezaSuperficie();
						info.setCodigoPkProgramaHallazgoPieza(rs.getBigDecimal("codigoprogramahallazgopieza"));
						info.setActivo(false);
						info.setPiezaDental(new DtoPiezaDental(rs.getDouble("codigo_pieza"), rs.getString("nombre_pieza"), "", ""));
						info.setProgramaOservicio(new InfoDatosDouble(rs.getDouble("codigo_programa_servicio"), rs.getString("nombre_programa_servicio")));
						DtoSuperficieDental dtoSuperficie= new DtoSuperficieDental(rs.getDouble("codigo_superficie"), rs.getString("nombre_superficie"), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
						info.getSuperficies().add(new InfoSuperficiePkDetPlan(dtoSuperficie, rs.getBigDecimal("codigo_detalle")));
						info.setHallazgo(rs.getBigDecimal("hallazgo"));
						info.setSeccion(rs.getString("seccion"));
						info.setCodigoPkPresupuestoProgServ(codigoPkPresupuestoProgServ);
						
						info.setValorTarifa(obtenerTarifaProgramaServicioPresupuesto(codigoPresupuesto, info.getProgramaOservicio().getCodigo(), utilizaProgramas));
						
						array.add(info);
						mapPos.put(unique, array.size()-1);
					}	
					else
					{
						DtoSuperficieDental dtoSuperficie= new DtoSuperficieDental(rs.getDouble("codigo_superficie"), rs.getString("nombre_superficie"), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido);
						array.get(mapPos.get(unique)).getSuperficies().add(new InfoSuperficiePkDetPlan(dtoSuperficie, rs.getBigDecimal("codigo_detalle")));
					}	
				}	
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return array;
	
	}
	
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @param codigoProgramaServicio
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal obtenerTarifaProgramaServicioPresupuesto(	BigDecimal codigoPresupuesto, Double codigoProgramaServicio,boolean utilizaProgramas) 
	{
		BigDecimal retorna= BigDecimal.ZERO;
		
		//FIXME debo filtrar x pieza - hallazgo superficie del programa
		String consultaStr="select " +
								"coalesce(poc.valor_unitario,0) as valorunitario, " +
								"coalesce(poc.valor_descuento_prom,0) as valordctoprom, " +
								"coalesce(poc.valor_descuento_bono,0) as valordctobono, " +
								"coalesce(poc.dcto_comercial_unitario,0) as valordctocomercial " +
							"FROM " +
								"odontologia.presupuesto_odo_prog_serv pops " +
								"INNER JOIN odontologia.presupuesto_odo_convenio poc on(poc.presupuesto_odo_prog_serv=pops.codigo_pk) " +
							"where pops.presupuesto=?  "+
							"and poc.contratado='"+ConstantesBD.acronimoSi+"' ";
		
		consultaStr+=(utilizaProgramas)?" and pops.programa=? ": " and pops.servicio=? ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPresupuesto);
			ps.setDouble(2, codigoProgramaServicio);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				double dctoOdon=0;
				BigDecimal valorDctoOdon= BigDecimal.ZERO;
				if(rs.getBigDecimal("valordctoprom").doubleValue()<=0 && rs.getBigDecimal("valordctobono").doubleValue()<=0)
				{	
					dctoOdon= SqlBasePresupuestoOdontologico.obtenerPorcentajeDctoOdonContratadoPresupuesto(codigoPresupuesto);
				}
				if(dctoOdon>0)
				{
					valorDctoOdon= (rs.getBigDecimal("valorunitario").subtract(rs.getBigDecimal("valordctocomercial"))).multiply(new BigDecimal(dctoOdon/100));
				}
				
				retorna= rs.getBigDecimal("valorunitario").subtract(rs.getBigDecimal("valordctocomercial")).subtract(rs.getBigDecimal("valordctoprom")).subtract(rs.getBigDecimal("valordctobono")).subtract(valorDctoOdon);	
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return retorna;
	}


	/**
	 * 
	 * @param codigoPresupuesto
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	private static ArrayList<InfoInclusionExclusionBoca> obtenerListaBocaExclusion(
			BigDecimal codigoPresupuesto,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{
		ArrayList<InfoInclusionExclusionBoca> array= new ArrayList<InfoInclusionExclusionBoca>();
		
		String consultaStr= (utilizaProgramas)? "SELECT DISTINCT  " +
													"dpt.codigo_pk as codigo_detalle, " +
													"pspt.programa as codigo_programa_servicio, " +
													"dpt.hallazgo as hallazgo, " +
													"prog.codigo_programa ||' - '|| prog.nombre as nombre_programa_servicio  " +
												"FROM " +
													"odontologia.presupuesto_odontologico pre " +
													"INNER JOIN odontologia.det_plan_tratamiento dpt ON(pre.plan_tratamiento=dpt.plan_tratamiento) " +
													"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
													"INNER JOIN odontologia.programas prog ON(prog.codigo=pspt.programa) " +
												"WHERE " +
													"pre.codigo_pk=? " +
													"and dpt.seccion ='"+ConstantesIntegridadDominio.acronimoBoca+"' " +
													"AND pspt.estado_programa='"+estadoProgramasServiciosPlanTratamiento+"' " +
													"AND pspt.exclusion='"+ConstantesBD.acronimoSi+"' " +
													(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_exclu='"+estadoAutorizacion+"' ":" ") +
													"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
													"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
												"order by " +
													"prog.codigo_programa ||' - '|| prog.nombre "
												:
												"SELECT DISTINCT " +
													"dpt.codigo_pk as codigo_detalle, " +
													"pspt.servicio as codigo_programa_servicio, " +
													"dpt.hallazgo as hallazgo, " +
													"getnombreservicio(pspt.servicio, "+ConstantesBD.codigoTarifarioCups+") as nombre_programa_servicio  " +
												"FROM " +
													"odontologia.presupuesto_odontologico pre " +
													"INNER JOIN odontologia.det_plan_tratamiento dpt ON(pre.plan_tratamiento=dpt.plan_tratamiento) " +
													"INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento=dpt.codigo_pk) " +
												"WHERE " +
													"pre.codigo_pk=? " +
													"and dpt.seccion ='"+ConstantesIntegridadDominio.acronimoBoca+"' " +
													"AND pspt.estado_servicio='"+estadoProgramasServiciosPlanTratamiento+"' " +
													"AND pspt.exclusion='"+ConstantesBD.acronimoSi+"' " +
													(!estadoAutorizacion.isEmpty()?"AND pspt.estado_autorizacion_exclu='"+estadoAutorizacion+"' ":" ") +
													"AND dpt.activo='"+ConstantesBD.acronimoSi+"' "+
													"AND pspt.activo='"+ConstantesBD.acronimoSi+"' "+
												"order by  " +
													"pspt.servicio";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPresupuesto);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				BigDecimal codigoPkPresupuestoProgServ= SqlBasePresupuestoOdontologico.existeProgramaServicioPlanTratamientoEnPresupuesto(ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, rs.getInt("hallazgo"), rs.getBigDecimal("codigo_programa_servicio"), utilizaProgramas, codigoPresupuesto,ConstantesIntegridadDominio.acronimoBoca);
				
				if(codigoPkPresupuestoProgServ.doubleValue()>0)
				{
					InfoInclusionExclusionBoca info= new InfoInclusionExclusionBoca();
					info.setCodigoPkDetPlanTratamiento(rs.getBigDecimal("codigo_detalle"));
					info.setActivo(false);
					info.setProgramaOservicio(new InfoDatosDouble(rs.getDouble("codigo_programa_servicio"), rs.getString("nombre_programa_servicio")));
					info.setHallazgo(rs.getBigDecimal("hallazgo"));
					info.setCodigoPkPresupuestoProgServ(codigoPkPresupuestoProgServ);
					info.setValorTarifa(obtenerTarifaProgramaServicioPresupuesto(codigoPresupuesto, info.getProgramaOservicio().getCodigo(), utilizaProgramas));
					array.add(info);
				}	
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return array;
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	//FIXME este metodo no sirve - Determinar si se mantiene o no?
	public static ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoExclusiones(BigDecimal codigoPresupuesto, int institucion)
	{

		ArrayList<InfoDetalleHistoricoIncExcPresupuesto> array= new ArrayList<InfoDetalleHistoricoIncExcPresupuesto>();
		String consultaStr=" SELECT " +
								"DISTINCT " +
								"ex.codigo_pk as codigo_pk, " +
								"TO_CHAR(encabezado.fecha, 'dd/mm/yyyy') as fecha, " +
								"CASE WHEN ex.programa IS NULL THEN getnombreservicio(ex.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") else (select p.nombre from odontologia.programas p where p.codigo=ex.programa) end as programa_o_servicio, " +
								"ex.valor as valor, " +
								"getnombreusuario2(encabezado.usuario) as responsable " +
							"FROM " +
								"odontologia.exclusiones_presupuesto ex, odontologia.exclu_presu_encabezado encabezado " +
							"WHERE " +
								"ex.exclu_presu_encabezado = encabezado.codigo_pk AND encabezado.presupuesto=? ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPresupuesto);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDetalleHistoricoIncExcPresupuesto info= new InfoDetalleHistoricoIncExcPresupuesto();
				info.setFecha(rs.getString("fecha"));
				info.setPiezaDentalOboca(cargarPiezaDentalDetalleExclusion(rs.getBigDecimal("codigo_pk")));
				info.setProgramaOservicio(rs.getString("programa_o_servicio"));
				info.setResponsable(rs.getString("responsable"));
				info.setSuperficie(cargarSuperficiesDetalleExclusion(rs.getBigDecimal("codigo_pk")));
				info.setValorFormateado(UtilidadTexto.formatearValores(rs.getDouble("valor")));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return array;
	}

	/**
	 * 
	 * Metodo para .......
	 * @param bigDecimal
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static String cargarPiezaDentalDetalleExclusion(BigDecimal codigoEnca) 
	{
		String retorna="";
		String consulta="select DISTINCT " +
							"coalesce(pd.descripcion, '') as pieza_dental " +
						"FROM " +
							"odontologia.det_exclusiones_superficies des " +
							"INNER JOIN odontologia.exclusiones_presupuesto ep ON(ep.codigo_pk=des.exclusion_presupuesto) " +
							"INNER JOIN odontologia.det_plan_tratamiento dpt ON(des.det_plan_tratamiento=dpt.codigo_pk) " +
							"LEFT OUTER JOIN odontologia.pieza_dental pd ON(pd.codigo_pk=dpt.pieza_dental) " +
						"where " +
							"ep.codigo_pk=? ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, codigoEnca);
			logger.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				retorna= rs.getString("pieza_dental");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return retorna;
	}

	/**
	 * 
	 * Metodo para .......
	 * @param bigDecimal
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static String cargarSuperficiesDetalleExclusion(BigDecimal codigoEnca) 
	{
		String retorna="";
		String consulta="select DISTINCT " +
							"coalesce(sd.nombre, '') as superficie " +
						"FROM " +
							"odontologia.det_exclusiones_superficies des " +
							"INNER JOIN odontologia.exclusiones_presupuesto ep ON(ep.codigo_pk=des.exclusion_presupuesto) " +
							"INNER JOIN odontologia.det_plan_tratamiento dpt ON(des.det_plan_tratamiento=dpt.codigo_pk) " +
							"LEFT OUTER JOIN historiaclinica.superficie_dental sd ON(sd.codigo=dpt.superficie) " +
						"where " +
							"ep.codigo_pk=? ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consulta);
			ps.setBigDecimal(1, codigoEnca);
			logger.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				retorna+=(retorna.isEmpty())?"":", ";
				retorna+= rs.getString("superficie");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return retorna;
	}

	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoInclusiones(BigDecimal codigoPresupuesto, int institucion)
	{
		ArrayList<InfoDetalleHistoricoIncExcPresupuesto> array= new ArrayList<InfoDetalleHistoricoIncExcPresupuesto>();
		String consultaStr=" SELECT " +
								"DISTINCT " +
									"pops.codigo_pk as codigopops, " +
									"ip.codigo_pk as codinc, " +
									"TO_CHAR(encabezado.fecha, 'dd/mm/yyyy') as fecha, " +
									"coalesce(pd.descripcion, '') as pieza_dental, " +
									"CASE WHEN pops.programa IS NULL THEN getnombreservicio(pops.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") else (select p.nombre from odontologia.programas p where p.codigo=pops.programa) end as programa_o_servicio, " +
									"ip.valor as valor, " +
									"getnombreusuario2(encabezado.usuario) as responsable " +
								"FROM " +
									"odontologia.inclusiones_presupuesto ip " +
									"INNER JOIN odontologia.inclu_presu_encabezado encabezado ON (ip.inclu_presu_encabezado = encabezado.codigo_pk AND encabezado.estado = '"+ConstantesIntegridadDominio.acronimoContratado+"')  " +
									"INNER JOIN odontologia.presupuesto_odo_prog_serv pops on(pops.inclusion=ip.codigo_pk)  " +
									"INNER JOIN odontologia.presupuesto_piezas pp ON(pp.presupuesto_odo_prog_serv=pops.codigo_pk) " +
									"LEFT OUTER JOIN odontologia.pieza_dental pd ON(pd.codigo_pk=pp.pieza) " +
								"where " +
									"pops.presupuesto=? ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPresupuesto);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				InfoDetalleHistoricoIncExcPresupuesto info= new InfoDetalleHistoricoIncExcPresupuesto();
				info.setFecha(rs.getString("fecha"));
				info.setPiezaDentalOboca(rs.getString("pieza_dental"));
				info.setProgramaOservicio(rs.getString("programa_o_servicio"));
				info.setResponsable(rs.getString("responsable"));
				info.setSuperficie(obtenerSuperficiesInclusion(rs.getBigDecimal("codigopops")));
				info.setValorFormateado(UtilidadTexto.formatearValores(rs.getDouble("valor")));
				array.add(info);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return array;
	}

	/**
	 * 	
	 * Metodo para .......
	 * @param bigDecimal
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static String obtenerSuperficiesInclusion(BigDecimal codigoPkPresupuestoProgServ) 
	{
		String resultado="";
		String consultaStr=" SELECT " +
								"coalesce(sd.nombre, '') as superficie " +
							"FROM " +
								"odontologia.presupuesto_odo_prog_serv pops " +
								"INNER JOIN odontologia.presupuesto_piezas pp ON(pp.presupuesto_odo_prog_serv=pops.codigo_pk) " +
								"LEFT OUTER JOIN odontologia.pieza_dental pd ON(pd.codigo_pk=pp.pieza) " +
								"LEFT OUTER JOIN historiaclinica.superficie_dental sd ON(sd.codigo=pp.superficie) " +
							"where " +
								"pops.codigo_pk=? ";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, codigoPkPresupuestoProgServ);
			
			logger.info(ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				resultado+= (resultado.isEmpty())?"":", ";
				resultado+= rs.getString("superficie");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
	    catch (SQLException e) 
		{
			logger.error("error en carga==> ", e);
		}
	    return resultado;
	}
	
	
}
