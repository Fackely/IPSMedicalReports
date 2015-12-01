package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologicoAtencion;

public class SqlBaseHistoricoDescuentoOdontologicoDao {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseHistoricoDescuentoOdontologicoDao.class);
	
	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = "INSERT INTO odontologia.his_descuentos_odon (   " +
	                                                                                "codigo , " +//1
																					"consecutivo , " +//2
																					"centro_atencion , " +//3
																					"fecha_ini_vigencia , " +//4
																					"fecha_ini_vigencia_mod , " +//5
																					"fecha_fin_vigencia , " +//6
																					"fecha_fin_vigencia_mod , " +//7
																					"fecha_modifica , " +//8
																					"hora_modifica , " +//9
																					"institucion  , " +//10
																					"eliminado  , " +//11
																					"usuario_modifica) " +//12
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
																					"? , " +//10
																					"? , " +//11
																					"? ) ";//12
	
	

	/**
	 * CADENA para insertar
	 */
	
	
    
	private static String inserccionAtencionStr = "INSERT INTO odontologia.his_descuentos_odon_aten (   " +
	                                                                                "codigo , " +//1
																					"consecutivo , " +//2
																					"centro_atencion , " +//3
																					"porcentaje_dcto , " +//4
																					"porcentaje_dcto_mod , " +//5
																					"nivel_autorizacion , " +//6
																					"nivel_autorizacion_mod , " +//7
																					"dias_vigencia , " +//8			
																					"dias_vigencia_mod , " +//9			
																					"fecha_modifica , " +//10
																					"hora_modifica , " +//11
																					"institucion  , " +//12
																					"eliminado  , " +//13
																					"usuario_modifica) " +//14
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
																					"? , " +//10
																					"? , " +//11
																					"? , " +//12
																					"? , " +//13
																					
																					"? ) ";//14


	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoHistoricoDescuentoOdontologico dtoNuevo, DtoHistoricoDescuentoOdontologico dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.his_descuentos_odon  set consecutivo=consecutivo";
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getEliminado())?" , eliminado= '"+dtoNuevo.getEliminado()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and consecutivo= "+dtoWhere.getConsecutivo():"";
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			logger.info("modificar-->"+consultaStr);
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
	
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Descuentos Odontologia" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * 
	 */
	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dtoNuevo, DtoHistoricoDescuentoOdontologicoAtencion dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.his_descuentos_odon_aten  set consecutivo=consecutivo";
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getEliminado())?" , eliminado= '"+dtoNuevo.getEliminado()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and consecutivo= "+dtoWhere.getConsecutivo():"";
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			logger.info("modificar-->"+consultaStr);
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
	
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Descuentos Odontologia atencion" + e);
			
		}
		return retorna;
	}
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> cargarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dtoWhere) 
	{
		
		
		ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> arrayDto = new ArrayList<DtoHistoricoDescuentoOdontologicoAtencion>();
		String consultaStr = 	"SELECT " +
		                                "deod.codigo as codigo , " +
										"deod.consecutivo as consecutivo , " +
										"deod.centro_atencion as centro_atencion , "+
										"getnomcentroatencion(deod.centro_atencion) as nombre_centro_atencion ,"+ 
										"deod.porcentaje_dcto as porcentaje_dcto ,"+
										"deod.nivel_autorizacion as nivel_autorizacion ,"+
										"(select tu.descripcion from odontologia.tipos_usuarios tu where tu.codigo=deod.nivel_autorizacion) as nombre_nivel_autorizacion ,"+ 
										"(select tu.descripcion from odontologia.tipos_usuarios tu where tu.codigo=deod.nivel_autorizacion_mod) as nombre_nivel_autorizacion_mod ,"+ 
										"deod.porcentaje_dcto_mod as porcentaje_dcto_mod ,"+
										"deod.nivel_autorizacion_mod as nivel_autorizacion_mod ,"+
										"deod.dias_vigencia as dias_vigencia ,"+
										"deod.dias_vigencia_mod as dias_vigencia_mod ,"+										
										"deod.institucion as institucion , " +
										"deod.fecha_modifica as fecha_modifica , " +
										"deod.hora_modifica as hora_modifica , " +
										"deod.eliminado as eliminado , " +
										"deod.usuario_modifica as usuario_modifica " +
										
									"from " +
										"odontologia.his_descuentos_odon_aten  deod  " +
									"where " +
										"1=1 ";
										
		consultaStr+= (dtoWhere.getCodigo() > ConstantesBD.codigoNuncaValido)?" and deod.codigo= "+dtoWhere.getConsecutivo():"";									
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and deod.consecutivo= "+dtoWhere.getConsecutivo():"";
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and deod.centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and deod.fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and deod.hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and deod.institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and deod.usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getEliminado())?" and deod.eliminado= '"+dtoWhere.getEliminado()+"' ":"";
		consultaStr+=" order by deod.consecutivo asc"; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Des odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoHistoricoDescuentoOdontologicoAtencion dto = new DtoHistoricoDescuentoOdontologicoAtencion();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setConsecutivo(rs.getDouble("consecutivo"));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"),rs.getString("nombre_centro_atencion")));
				dto.setPorcentajeDescuento(rs.getDouble("porcentaje_dcto"));
				dto.setPorcentajeDescuentoMod(rs.getDouble("porcentaje_dcto_mod"));
				dto.setNivelAutorizacion(new InfoDatosDouble(rs.getDouble("nivel_autorizacion"), rs.getString("nombre_nivel_autorizacion")));
				dto.setNivelAutorizacionMod(new InfoDatosDouble(rs.getDouble("nivel_autorizacion_mod"), rs.getString("nombre_nivel_autorizacion_mod")));
				dto.setDiasVigencia(rs.getInt("dias_vigencia"));
				dto.setDiasVigenciaMod(rs.getInt("dias_vigencia_mod"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setEliminado(rs.getString("eliminado"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Des Odon==> " + e);
		}
		return arrayDto;
	}
	
	
	/**
	 * 
	 */
	public static ArrayList<DtoHistoricoDescuentoOdontologico> cargar(DtoHistoricoDescuentoOdontologico dtoWhere) 
	{
		ArrayList<DtoHistoricoDescuentoOdontologico> arrayDto = new ArrayList<DtoHistoricoDescuentoOdontologico>();
		String consultaStr = 	"SELECT " +
		                                "deod.codigo as codigo , " +
										"deod.consecutivo as consecutivo , " +
										"deod.centro_atencion as centro_atencion , "+
										"getnomcentroatencion(deod.centro_atencion) as nombre_centro_atencion ,"+ 
										"to_char(deod.fecha_ini_vigencia,'DD/MM/YYYY') as fecha_ini_vigencia , "+
										"to_char(deod.fecha_ini_vigencia_mod,'DD/MM/YYYY') as fecha_ini_vigencia_mod , "+
										"to_char(deod.fecha_fin_vigencia,'DD/MM/YYYY') as fecha_fin_vigencia , "+
										"to_char(deod.fecha_fin_vigencia_mod,'DD/MM/YYYY') as fecha_fin_vigencia_mod, "+
										"deod.institucion as institucion , " +
										"deod.fecha_modifica as fecha_modifica , " +
										"deod.hora_modifica as hora_modifica , " +
										"deod.eliminado as eliminado , " +
										"deod.usuario_modifica as usuario_modifica " +
										
									"from " +
										"odontologia.his_descuentos_odon  deod  " +
									"where " +
										"1=1 ";
										
		consultaStr+= (dtoWhere.getCodigo() > ConstantesBD.codigoNuncaValido)?" and deod.codigo= "+dtoWhere.getConsecutivo():"";									
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and deod.consecutivo= "+dtoWhere.getConsecutivo():"";
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and deod.centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaFinVigenciaFromatoBD())?" and deod.fecha_fin_vigencia= '"+dtoWhere.getFechaFinVigenciaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaInicioVigenciaFromatoBD())?" and deod.fecha_ini_vigencia= '"+dtoWhere.getFechaInicioVigenciaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and deod.fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and deod.hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and deod.institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and deod.usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getEliminado())?" and deod.eliminado= '"+dtoWhere.getEliminado()+"' ":"";
		consultaStr+=" order by deod.consecutivo asc"; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Des odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoHistoricoDescuentoOdontologico dto = new DtoHistoricoDescuentoOdontologico();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setConsecutivo(rs.getDouble("consecutivo"));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"),rs.getString("nombre_centro_atencion")));
				dto.setFechaFinVigencia(rs.getString("fecha_fin_vigencia"));
				dto.setFechaFinVigenciaMod(rs.getString("fecha_fin_vigencia_mod"));
				dto.setFechaInicioVigencia(rs.getString("fecha_ini_vigencia"));
				dto.setFechaInicioVigenciaMod(rs.getString("fecha_ini_vigencia_mod"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setEliminado(rs.getString("eliminado"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga Des Odon==> " + e);
		}
		return arrayDto;
	}
	
	
	
	
	
	

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoHistoricoDescuentoOdontologico dto) 
	{
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			
			ResultSetDecorator rs = null;
			double secuencia = ConstantesBD.codigoNuncaValidoDouble;
			
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_his_descuentos_odon");
			dto.setCodigo(secuencia);
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			logger.info(UtilidadLog.obtenerString(dto, true));
			
			ps.setDouble(1, secuencia);	
			ps.setDouble(2, dto.getConsecutivo());				
		    ps.setInt(3, dto.getCentroAtencion().getCodigo());		    
		    ps.setString(4, dto.getFechaInicioVigencia());
		    ps.setString(5, dto.getFechaInicioVigenciaMod());
		    ps.setString(6, dto.getFechaFinVigencia());
		    ps.setString(7, dto.getFechaFinVigenciaMod());
			ps.setString(8, dto.getFechaModificaFromatoBD());
			ps.setString(9, dto.getHoraModifica());
			ps.setInt(10, dto.getInstitucion());
			ps.setString(11, dto.getEliminado());
			ps.setString(12, dto.getUsuarioModifica());
			
 			logger.info("cadena >> "+inserccionStr+" "+dto.getCentroAtencion().getCodigo()+" "+dto.getFechaInicioVigenciaFromatoBD()+" "+dto.getFechaFinVigenciaFromatoBD()+" "+dto.getFechaModificaFromatoBD()+" "+dto.getHoraModifica()+" "+dto.getInstitucion()+" "+dto.getUsuarioModifica());
			
			logger.info(inserccionStr);
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return dto.getConsecutivo();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert Des Odo " + e + dto.getConsecutivo());
			
			
		}
		return dto.getConsecutivo();
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarAtencion(DtoHistoricoDescuentoOdontologicoAtencion dto) 
	{
		try 
		{
		   logger.info(UtilidadLog.obtenerStringHerencia(dto, true));
			Connection con = UtilidadBD.abrirConexion();
			
			ResultSetDecorator rs = null;
			double secuencia = ConstantesBD.codigoNuncaValidoDouble;
			
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_his_descuentos_odon_aten");
			dto.setCodigo(secuencia);
			
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionAtencionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			
			
			logger.info(UtilidadLog.obtenerString(dto, true));
			
			ps.setDouble(1, secuencia);	
			ps.setDouble(2, dto.getConsecutivo());				
		    ps.setInt(3, dto.getCentroAtencion().getCodigo());		    
		    ps.setDouble(4, dto.getPorcentajeDescuento());
		    ps.setDouble(5, dto.getPorcentajeDescuentoMod());
		    ps.setDouble(6, dto.getNivelAutorizacion().getCodigo());
		    ps.setDouble(7, dto.getNivelAutorizacionMod().getCodigo());
		    ps.setInt(8, dto.getDiasVigencia());
		    ps.setInt(9, dto.getDiasVigenciaMod());
			ps.setString(10, dto.getFechaModificaFromatoBD());
			ps.setString(11, dto.getHoraModifica());
			ps.setInt(12, dto.getInstitucion());
			ps.setString(13, dto.getEliminado());
			ps.setString(14, dto.getUsuarioModifica());
			
 			
			
			logger.info(inserccionAtencionStr);
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return dto.getConsecutivo();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert Des Odo " + e + dto.getConsecutivo());
			
			
		}
		return dto.getConsecutivo();
	}
}