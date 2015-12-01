package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoEquivalentesHallazgoProgramaServicio;


public class SqlBaseDetalleHallazgoVsProgramaServicioDao {

	
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger( SqlBaseDetalleHallazgoVsProgramaServicioDao.class);
	
	/**
	 * CADENA para insertar
	 */


    
	
	
	private static String inserccionStr = "INSERT INTO odontologia.det_hall_prog_ser (   " +
																					"codigo , " +//1
																					"hallazgo_vs_prog_ser , " +//2
																					"orden , " +//3
																					"programa , " +//4
																					"servicio , " +//5
																					"numero_superficies  , " +//6
																					"por_defecto  , " +//7
																					
																					"fecha_modifica , " +//8
																					"hora_modifica , " +//9
																					
																					"usuario_modifica," + //10
																					"permite_tratar_varias_veces) " +//11
																		"values (" +
																					"? , " +//1
																					"? , " +//2
																					"? , " +//3
																					"? , " +//4
																					"? , " +//5
																					"? , " +//6
																					"? , " +//7
																					"? , " +//8
																					"? , " +//9
																					
																					
																					"? ,  " +//10
																					"? ) ";//11


	 
	
	
	
	private static String inserccionEquivalentesStr = "INSERT INTO odontologia.equivalentes_hall_prog_ser (   " +
																					"codigo , " +//1
																					"det_hall_prog_ser , " +//2
																					"det_hall_prog_ser_2 , " +//3
																					"fecha_modifica , " +//4
																					"hora_modifica , " +//5
																					
																					"usuario_modifica ) " +//6
																					"values (" +
																					"? , " +//1
																					"? , " +//2
																					"? , " +//3
																					"? , " +//4
																					"? , " +//5
																					"? ) ";//6

	
	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoDetalleHallazgoProgramaServicio dtoNuevo, DtoDetalleHallazgoProgramaServicio dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.det_hall_prog_ser set codigo=codigo";
		
		consultaStr+= (dtoNuevo.getHallazgoVsProgramaServicio() > ConstantesBD.codigoNuncaValido)?" , hallazgo_vs_prog_ser= "+dtoNuevo.getHallazgoVsProgramaServicio():"";
		
		consultaStr+= ((dtoNuevo.getPrograma().getCodigo() > ConstantesBD.codigoNuncaValido)&&(dtoNuevo.getPrograma().getCodigo() > 0))?" , programa= "+dtoNuevo.getPrograma().getCodigo():Types.DOUBLE;
		
		consultaStr+= (dtoNuevo.getOrden() > ConstantesBD.codigoNuncaValido)?" , orden= "+dtoNuevo.getOrden():"";
		
		consultaStr+= ((dtoNuevo.getServicio().getCodigo() > ConstantesBD.codigoNuncaValido)&&(dtoNuevo.getServicio().getCodigo() > 0))?" , servicio= "+dtoNuevo.getServicio().getCodigo():Types.INTEGER;
		
		consultaStr+= (dtoNuevo.getNumeroSuperficies() > ConstantesBD.codigoNuncaValido)?" , numero_superficies= "+dtoNuevo.getNumeroSuperficies():"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getPorDefecto())?" , por_defecto= '"+dtoNuevo.getPorDefecto()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getPermiteTratarVariasVeces())?" , permite_tratar_varias_veces= '"+dtoNuevo.getPermiteTratarVariasVeces()+"' ":"";
		
		//consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getPorDefecto())?" , permite_tratar_varias_veces= '"+dtoNuevo.getPorDefecto()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			logger.info("modificar-->"+consultaStr);
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar det hall vs" + e);
			
		}
		return retorna;
	}
	
	/**
	 * Verifica si el los programas enviados por par&acute;metros son equivalentes
	 * @param programaServicio1
	 * @param programaServicio2
	 * @param hallazgo
	 * @param utilizaPrograma
	 * @return true en caso de ser equivalentes, false de lo contrario
	 * @author Juan David Ram&iacute;rez
	 * @since 2010-05-13
	 */
	public static boolean programasServiciosEquivalentes(BigDecimal programaServicio1, BigDecimal programaServicio2, double hallazgo, boolean utilizaPrograma)
	{
		String nombreColumna=(utilizaPrograma)?"programa":"servicio";
		
		String consultaStr="SELECT " +
								"d."+nombreColumna+" AS programa_servicio " +
							"FROM " +
								"odontologia.equivalentes_hall_prog_ser e " +
								"INNER JOIN odontologia.det_hall_prog_ser d ON(d.codigo=e.det_hall_prog_ser) " +
								"INNER JOIN odontologia.det_hall_prog_ser d1 ON(d1.codigo=e.det_hall_prog_ser_2) " +
								"INNER JOIN odontologia.hallazgos_vs_prog_ser h ON (h.codigo=d.hallazgo_vs_prog_ser and h.codigo=d1.hallazgo_vs_prog_ser) " +
							"WHERE " +
									"d.programa=? " +
								"AND d1.programa=? " +
								"AND h.hallazgo=?";
		try{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consultaStr);
			ps.setBigDecimal(1, programaServicio1);
			ps.setBigDecimal(2, programaServicio2);
			ps.setDouble(3, hallazgo);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			boolean resultado=rs.next();
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			return resultado;
		}catch (SQLException e) {
			logger.error("Error verificando si programas son equivalentes ",e);
			return false;
		}
	}
	
	/**
	 * 
	 * @param programaServicio
	 * @param hallazgo
	 * @param utilizaPrograma
	 * @return
	 */
	public static ArrayList<Double> obtenerEquivalentesProgServ(BigDecimal programaServicio, double hallazgo, boolean utilizaPrograma)
	{
		ArrayList<Double> lista= new ArrayList<Double>();
		String nombreColumna= (utilizaPrograma)?"programa":"servicio";
		
		String consultaStr="SELECT " +
								"d."+nombreColumna+" AS programa_servicio " +
							"FROM " +
								"odontologia.equivalentes_hall_prog_ser e " +
								"INNER JOIN odontologia.det_hall_prog_ser d ON(d.codigo=e.det_hall_prog_ser) " +
								"INNER JOIN odontologia.det_hall_prog_ser d1 ON(d1.codigo=e.det_hall_prog_ser_2) " +
								"INNER JOIN odontologia.hallazgos_vs_prog_ser h ON (h.codigo=d.hallazgo_vs_prog_ser and h.codigo=d1.hallazgo_vs_prog_ser) " +
							"WHERE " +
								"d1."+nombreColumna+"="+programaServicio+" " +
								"and h.hallazgo="+hallazgo+" " +
							"UNION " +
							"SELECT " +
								"d1."+nombreColumna+" " +
							"FROM " +
								"odontologia.equivalentes_hall_prog_ser e " +
								"INNER JOIN odontologia.det_hall_prog_ser d ON(d.codigo=e.det_hall_prog_ser) " +
								"INNER JOIN odontologia.det_hall_prog_ser d1 ON(d1.codigo=e.det_hall_prog_ser_2) " +
								"INNER JOIN odontologia.hallazgos_vs_prog_ser h ON (h.codigo=d.hallazgo_vs_prog_ser and h.codigo=d1.hallazgo_vs_prog_ser) " +
							"WHERE " +
								"d."+nombreColumna+"="+programaServicio+" " +
								"and h.hallazgo="+hallazgo+" ";
		
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar eq halla vs / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				if(rs.getDouble("programa_servicio")!=programaServicio.doubleValue())
				{	
					lista.add(rs.getDouble("programa_servicio"));
				}	
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga hall vs Odon==> " + e);
		}
		return lista;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoEquivalentesHallazgoProgramaServicio> cargarEquivalentes(DtoEquivalentesHallazgoProgramaServicio dtoWhere , String tipo) 
	{
		ArrayList<DtoEquivalentesHallazgoProgramaServicio> arrayDto = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		String consultaStr = 	"SELECT " +
										"ehs.codigo as codigo , " +
										"ehs.det_hall_prog_ser   as det_hall_prog_ser  , "+
										"ehs.det_hall_prog_ser_2  as det_hall_prog_ser_2 , ";
										
										
										
	    if(tipo.equals("Programa"))
	    {							
		consultaStr +=		"(select pro.nombre from odontologia.programas pro where pro.codigo  = (select dth.programa from odontologia.det_hall_prog_ser dth where dth.codigo = ehs.det_hall_prog_ser )) as nombre_det_hall_prog_ser , ";
		consultaStr +=		"(select pro.nombre from odontologia.programas pro where pro.codigo  = (select dth.programa from odontologia.det_hall_prog_ser dth where dth.codigo = ehs.det_hall_prog_ser_2 )) as nombre_det_hall_prog_ser_2 , ";
	   
	    
	    }else{
		
		consultaStr +=	    "facturacion.getnombreservicio( (select dth.servicio from odontologia.det_hall_prog_ser dth where dth.codigo = ehs.det_hall_prog_ser )  ,0)  as nombre_det_hall_prog_ser , " ;
		consultaStr +=	    "facturacion.getnombreservicio( (select dth.servicio from odontologia.det_hall_prog_ser dth where dth.codigo = ehs.det_hall_prog_ser_2 )  ,0)  as nombre_det_hall_prog_ser_2 , " ;
		
	    }
	    						
										consultaStr +=				"ehs.fecha_modifica as fecha_modifica , " +
										"ehs.hora_modifica as hora_modifica , " +
										"ehs.usuario_modifica as usuario_modifica " +
									"from " +
										" odontologia.equivalentes_hall_prog_ser  ehs  " +
									"where " +
										"1=1 ";
		
		
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and ehs.codigo= "+dtoWhere.getCodigo():"";
	    consultaStr+= (dtoWhere.getDetallehallazgo().getCodigo() >ConstantesBD.codigoNuncaValido)?" and ehs.det_hall_prog_ser= "+dtoWhere.getDetallehallazgo().getCodigo():"";
	    consultaStr+=(dtoWhere.getDetallehallazgo2().getCodigo())>0?" and  ehs.det_hall_prog_ser_2="+dtoWhere.getDetallehallazgo2().getCodigo(): " ";
		
		
		
	
		
		consultaStr+=" order by ehs.codigo desc";
		
		Log4JManager.info("\n CONSULTAEQUIVALENTES --->\n"+consultaStr);
		
		
		try 
		{
			//logger.info("\n\n\n\n\n SQL cargar eq halla vs / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoEquivalentesHallazgoProgramaServicio dto = new DtoEquivalentesHallazgoProgramaServicio();
				dto.setCodigo(rs.getDouble("codigo"));
			    dto.setDetallehallazgo(new InfoDatosDouble(rs.getDouble("det_hall_prog_ser"),rs.getString("nombre_det_hall_prog_ser")));
                dto.setDetallehallazgo2(new InfoDatosDouble(rs.getDouble("det_hall_prog_ser_2"),rs.getString("nombre_det_hall_prog_ser_2")));
           
				dto.getDatosfechaUsuarioModifica().setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.getDatosfechaUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
				
				dto.getDatosfechaUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga hall vs Odon==> " + e);
		}
		return arrayDto;
	}
	

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleHallazgoProgramaServicio> cargar(DtoDetalleHallazgoProgramaServicio dtoWhere , int codigoInstitucion) 
	{
		ArrayList<DtoDetalleHallazgoProgramaServicio> arrayDto = new ArrayList<DtoDetalleHallazgoProgramaServicio>();
		String consultaStr = 	"SELECT " +
										"dhps.codigo as codigo , " +
										"dhps.hallazgo_vs_prog_ser as hallazgo_vs_prog_ser , "+
										"dhps.orden as orden , "+
										"dhps.programa as  programa , "+
										"dhps.servicio as  servicio , "+
										"dhps.numero_superficies as  numero_superficies , "+
										"dhps.por_defecto as  por_defecto , "+
								
										"(select pro.nombre from odontologia.programas pro where pro.codigo  = dhps.programa) as nombre_programa ,"+ 
										"facturacion.getnombreservicio(dhps.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)+")  as nombre_servicio , " +
										
										"dhps.fecha_modifica as fecha_modifica , " +
										"dhps.hora_modifica as hora_modifica , " +
										"dhps.usuario_modifica as usuario_modifica," +
										"dhps.permite_tratar_varias_veces as  permite_tratar_varias_veces , "+
										"coalesce(p.codigo_programa,'') AS codigoprograma " +
									"from " +
										" odontologia.det_hall_prog_ser  dhps  " +
									"left outer join " +
										"odontologia.programas p on (p.codigo=dhps.programa) " +
									"where " +
										"1=1 ";
		
		
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and dhps.codigo= "+dtoWhere.getCodigo():"";
		
		
		consultaStr+= (dtoWhere.getHallazgoVsProgramaServicio() > ConstantesBD.codigoNuncaValido)?" and dhps.hallazgo_vs_prog_ser = "+dtoWhere.getHallazgoVsProgramaServicio():"";
		
		consultaStr+=" order by dhps.codigo desc"; 
		
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar halla vs / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoDetalleHallazgoProgramaServicio dto = new DtoDetalleHallazgoProgramaServicio();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setHallazgoVsProgramaServicio(rs.getDouble("hallazgo_vs_prog_ser"));
				dto.setOrden(rs.getDouble("orden"));
				dto.setPorDefecto(rs.getString("por_defecto"));
				dto.setNumeroSuperficies(rs.getDouble("numero_superficies"));
				dto.setPrograma(new InfoDatosDouble(rs.getDouble("programa"),rs.getString("nombre_programa")));
				dto.setServicio(new InfoDatosDouble(rs.getDouble("servicio"),rs.getString("nombre_servicio")));
			   
				dto.getDatosfechaUsuarioModifica().setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.getDatosfechaUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
				
				dto.getDatosfechaUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setCodigoPrograma(rs.getString("codigoprograma"));
				dto.setPermiteTratarVariasVeces(rs.getString("permite_tratar_varias_veces"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga hall vs Odon==> " + e);
		}
		return arrayDto;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDetalleHallazgoProgramaServicio dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.det_hall_prog_ser  WHERE 1=1 ";
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		logger.info("elmi-->"+consultaStr);
		if(dtoWhere.getCodigo()<=0)
		{
			return false;
		}
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= null;
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar  emi tar odo "+ e);
		
		}
		return false;
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminarEquivalentes(DtoEquivalentesHallazgoProgramaServicio dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.equivalentes_hall_prog_ser  WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		consultaStr+= (dtoWhere.getDetallehallazgo().getCodigo() >ConstantesBD.codigoNuncaValido)?" and det_hall_prog_ser= "+dtoWhere.getDetallehallazgo().getCodigo():"";
		
		consultaStr+= (dtoWhere.getDetallehallazgo2().getCodigo() >ConstantesBD.codigoNuncaValido)?" and det_hall_prog_ser_2= "+dtoWhere.getDetallehallazgo2().getCodigo():" or det_hall_prog_ser_2= "+dtoWhere.getDetallehallazgo().getCodigo();
		
		logger.info("elmi-->"+consultaStr);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs= null;
			Log4JManager.info("Eliminar Equivalentes"+ps);
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar  emi tar odo "+ e);
		
		}
		return false;
	}

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar( DtoDetalleHallazgoProgramaServicio dto , Connection con) 
	{
		logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_hall_prog_ser");
			dto.setCodigo(secuencia);
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, secuencia);				
		    ps.setDouble(2, dto.getHallazgoVsProgramaServicio());		    
		    ps.setDouble(3, dto.getOrden());
		    
		    if(dto.getPrograma().getCodigo() > 0){
		    	
		    	ps.setDouble(4, dto.getPrograma().getCodigo());	
		    	
		    }else{
		    	
			   ps.setNull(4, Types.DOUBLE);
		    }
		    
		    if(dto.getServicio().getCodigo() > 0){
		    	
		    	ps.setDouble(5, dto.getServicio().getCodigo());
		    	
		    }else{
		    	
			   ps.setNull(5, Types.DOUBLE);
			   
		    }
		    
		    if(dto.getNumeroSuperficies() > ConstantesBD.codigoNuncaValido){
		    	
		    	ps.setDouble(6, dto.getNumeroSuperficies());
		    	
		    }else{
		    	
			   ps.setNull(6, Types.DOUBLE);
		    }
		    
		    if(!UtilidadTexto.isEmpty(dto.getPorDefecto())){
		    	
		    	ps.setString(7, dto.getPorDefecto());
		    	
		    }else{
		    	
		    	ps.setString(7, ConstantesBD.acronimoNo);
		    }
		  
			ps.setString(8, dto.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(9, dto.getDatosfechaUsuarioModifica().getHoraModifica());
			
			ps.setString(10, dto.getDatosfechaUsuarioModifica().getUsuarioModifica());
 
			ps.setString(11, dto.getPermiteTratarVariasVeces());
			
			logger.info(inserccionStr);
			if (ps.executeUpdate() > 0) 
			{
				ps.close();
				return dto.getCodigo();
			}
			
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert det halla vs " + e + dto.getCodigo());
		}
		return dto.getCodigo();
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarEquivalente( DtoEquivalentesHallazgoProgramaServicio dto , Connection con) 
	{
		
		
		
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
		
			
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_equivalentes_hall_prog_ser");
			dto.setCodigo(secuencia);
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, inserccionEquivalentesStr);
			ps.setDouble(1, secuencia);				
		    ps.setDouble(2, dto.getDetallehallazgo().getCodigo());		    
		    ps.setDouble(3, dto.getDetallehallazgo2().getCodigo());
		    ps.setString(4, dto.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD());
			ps.setString(5, dto.getDatosfechaUsuarioModifica().getHoraModifica());
			ps.setString(6, dto.getDatosfechaUsuarioModifica().getUsuarioModifica());
 
			
			
			//logger.info("sql Inser "+ps);
			if (ps.executeUpdate() > 0) 
			{
				ps.close();
				return dto.getCodigo();
			}
			ps.close();
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert det halla vs prog "+ dto.getCodigo(), e);
			
			
		}
		return dto.getCodigo();
	}

	/**
	 * Carga el n&uacute;mero de superficies a las cuales aplica el programa en el hallazgo seleccionado
	 * @param detalleHallazgoProgramaServicio {@link DtoDetalleHallazgoProgramaServicio} Información con el hallazgo &oacute; el codigo_pk del DetalleHallazgoProgramaServicio
	 * @param codigoHallazgo int C&oacute;digo del hallazgo buscado
	 * @return N&uacute;mero de superficies para las que aplica el programa
	 * @author Juan David Ram&iacute;rez
	 * @since 2010-05-14
	 */
	public static int consultarNumeroSuperficiesAplica(DtoDetalleHallazgoProgramaServicio detalleHallazgoProgramaServicio, int codigoHallazgo)
	{
		Connection con=UtilidadBD.abrirConexion();
		String sentencia=
				"SELECT " +
					"numero_superficies AS numero_superficies " +
				"FROM " +
					"odontologia.det_hall_prog_ser det " +
				"INNER JOIN " +
					"hallazgos_vs_prog_ser hall " +
						"ON(hall.codigo=det.hallazgo_vs_prog_ser) " +
				"WHERE " +
						"det.programa=? " +
					"AND " +
						"hall.hallazgo=?";
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setDouble(1, detalleHallazgoProgramaServicio.getPrograma().getCodigo());
			psd.setInt(2, codigoHallazgo);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			int numeroSuperficies=0;
			if(rsd.next())
			{
				numeroSuperficies=rsd.getInt("numero_superficies");
			}
			psd.close();
			rsd.close();
			UtilidadBD.closeConnection(con);
			return numeroSuperficies;
		}
		catch (SQLException e) {
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean existenHallazgosPlanTratamiento( DtoDetalleHallazgoProgramaServicio dto)
	{
		boolean retorno=Boolean.FALSE;
		
		
			String consulta=" select  count(0) as cantidad  from odontologia.programas_servicios_plan_t pspt inner join programas p on(pspt.programa=p.codigo and pspt.activo='"+ConstantesBD.acronimoSi+"' ) " +
					"		inner join odontologia.det_hall_prog_ser dhps on (dhps.programa=p.codigo and  dhps.codigo=? )  ";
			Connection con =UtilidadBD.abrirConexion();
			Log4JManager.info(consulta);
			
			
			try
			{
				
				PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consulta);
				psd.setDouble(1, dto.getCodigo());
				
				Log4JManager.info("Consulta -->"+psd);
				ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
				
			
				if(rsd.next())
				{
					if(rsd.getInt("cantidad")>0)
					{
						retorno=Boolean.TRUE;
					}
				}
				
				UtilidadBD.cerrarObjetosPersistencia(psd, rsd, con);
			}
			catch(SQLException e)
			{
				
				Log4JManager.info(" Error "+e);
			}
			
			
		
		return retorno;
	}
	
	
	
	
	

	/**
	 * METODO QUE VALIDA IS UN DETALLA DE HALLAZGO ESTA REGISTRADO EN EN PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 */
	public static  boolean existeDetalleAsociadoPlanTratamiento(DtoDetalleHallazgoProgramaServicio detalleHallazgo, boolean aplicaServicio){
		
		boolean retorno=Boolean.FALSE;
		
		String consulta=" select  count(0) as cantidad "+  
						"	FROM " +
						" odontologia.det_plan_tratamiento dplan " +
						"	inner join "+
						" odontologia.programas_servicios_plan_t  pspt on(pspt.det_plan_tratamiento=dplan.codigo_pk) " +
						"	inner join " +
						" odontologia.hallazgos_odontologicos ho on(dplan.hallazgo=ho.consecutivo) " +
						"	inner join " +
						" odontologia.hallazgos_vs_prog_ser hps on(hps.hallazgo=ho.consecutivo)  "+ 
						" 	INNER JOIN  "+  
						" odontologia.det_hall_prog_ser dhps ON (hps.codigo=dhps.hallazgo_vs_prog_ser)  "+
						"	where dhps.codigo=? ";
		
			if(aplicaServicio)
			{
				consulta+=" and pspt.programa="+detalleHallazgo.getPrograma().getCodigo();
			}
			else
			{
				consulta+=" and pspt.servicio="+detalleHallazgo.getServicio().getCodigo();
			}
			
			
			Log4JManager.info("Valdiacion Eliminacion Detalle plan-->->"+consulta);
		
		try
		{
		
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consulta);
			ps.setDouble(1, detalleHallazgo.getCodigo());
			
			Log4JManager.info(ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next()) 
			{
				if( rs.getInt("cantidad")>0)
				{
					retorno=Boolean.TRUE;
				}
			}
		
		}
		catch (SQLException  e) 
		{
			Log4JManager.info(e);
		}
		
	
		return retorno;
	}
	
	
	 
	
	
}
