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
import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;


public class SqlBaseDescuentoOdontologicoDao {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDescuentoOdontologicoDao.class);
	
	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = "INSERT INTO odontologia.descuentos_odon (   " +
																					"consecutivo , " +//1
																					"centro_atencion , " +//2
																					"fecha_ini_vigencia , " +//3
																					"fecha_fin_vigencia , " +//4
																					"fecha_modifica , " +//5
																					"hora_modifica , " +//6
																					"institucion  , " +//7
																					"usuario_modifica) " +//8
																		"values (" +
																					"? , " +//1
																					"? , " +//2
																					"? , " +//3
																					"? , " +//4
																					"? , " +//5
																					"? , " +//6
																					"? , " +//7
																					"? ) ";//8

	
	
//	/**
//	 * CADENA para insertar
//	 */
//	private static String inserccionAtencionStr = "INSERT INTO odontologia.descuentos_odon_aten (   " +
//																					"consecutivo , " +//1
//																					"centro_atencion , " +//2
//																					"dias_vigencia , " +//3
//																					"nivel_autorizacion , " +//4
//																					"porcentaje_dcto , " +//5
//																					"fecha_modifica , " +//6
//																					"hora_modifica , " +//7
//																					"institucion  , " +//8
//																					"usuario_modifica) " +//9
//																		"values (" +
//																					"? , " +//1
//																					"? , " +//2
//																					"? , " +//3
//																					"? , " +//4
//																					"? , " +//5
//																					"? , " +//6
//																					"? , " +//7
//																					"? , " +//8
//																					"? ) ";//9


	
	
	
	/**
	 * 
	 * 
	 */
	
	
	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificarAtencion(DtoDescuentoOdontologicoAtencion dtoNuevo, DtoDescuentoOdontologicoAtencion dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.descuentos_odon_aten  set consecutivo=consecutivo";
		
		consultaStr+= (dtoNuevo.getCentroAtencion().getCodigo() > ConstantesBD.codigoNuncaValido)?" , centro_atencion= "+dtoNuevo.getCentroAtencion().getCodigo():"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModifica()+"' ":"";
		
		consultaStr+= (dtoNuevo.getNivelAutorizacion().getCodigo() > 0)?" , nivel_autorizacion= "+dtoNuevo.getNivelAutorizacion().getCodigo() +" ":"";
		
		consultaStr+= (dtoNuevo.getPorcentajeDescuento() > 0)?" , porcentaje_dcto= "+dtoNuevo.getPorcentajeDescuento() +" ":"";
		
		consultaStr+= (dtoNuevo.getDiasVigencia() > 0)?" , dias_vigencia = "+dtoNuevo.getDiasVigencia() +" ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		consultaStr+= (dtoNuevo.getInstitucion()>ConstantesBD.codigoNuncaValido)?" , institucion= "+dtoNuevo.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and consecutivo= "+dtoWhere.getConsecutivo():"";
		
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()> 0)?" and centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":" ";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and institucion= "+dtoWhere.getInstitucion()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		
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
			logger.error("ERROR EN actualizar Descuentos Odontologia" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoDescuentosOdontologicos dtoNuevo, DtoDescuentosOdontologicos dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.descuentos_odon  set consecutivo=consecutivo";
		
		consultaStr+= (dtoNuevo.getCentroAtencion().getCodigo() > ConstantesBD.codigoNuncaValido)?" , centro_atencion= "+dtoNuevo.getCentroAtencion().getCodigo():"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaInicioVigenciaFromatoBD())?" , fecha_ini_vigencia= '"+dtoNuevo.getFechaInicioVigenciaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaFinVigenciaFromatoBD())?" , fecha_fin_vigencia= '"+dtoNuevo.getFechaFinVigenciaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		consultaStr+= (dtoNuevo.getInstitucion()>ConstantesBD.codigoNuncaValido)?" , institucion= "+dtoNuevo.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and consecutivo= "+dtoWhere.getConsecutivo():"";
		
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()> 0)?" and centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaInicioVigenciaFromatoBD())?" and fecha_ini_vigencia= '"+dtoWhere.getFechaInicioVigenciaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaFinVigenciaFromatoBD())?" and fecha_fin_vigencia= '"+dtoWhere.getFechaFinVigenciaFromatoBD()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":" ";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and institucion= "+dtoWhere.getInstitucion()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		
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
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDescuentoOdontologicoAtencion> cargarAtencion(DtoDescuentoOdontologicoAtencion dtoWhere) 
	{
		ArrayList<DtoDescuentoOdontologicoAtencion> arrayDto = new ArrayList<DtoDescuentoOdontologicoAtencion>();
		String consultaStr = 	"SELECT " +
										"deod.consecutivo as consecutivo , " +
										"deod.centro_atencion as centro_atencion , " +
										"getnomcentroatencion(deod.centro_atencion) as nombre_centro_atencion ,"+ 
										"deod.porcentaje_dcto as porcentaje_dcto ,"+
										"deod.nivel_autorizacion as nivel_autorizacion ,"+
										"(select tu.descripcion from odontologia.tipos_usuarios tu where tu.codigo=deod.nivel_autorizacion) as nombre_nivel_autorizacion ,"+ 
										"deod.dias_vigencia as dias_vigencia ,"+
										"deod.institucion as institucion , " +
										"deod.fecha_modifica as fecha_modifica , " +
										"deod.hora_modifica as hora_modifica , " +
										"deod.usuario_modifica as usuario_modifica, " +
										"(select count(1) from  odontologia.presupuesto_dcto_odon pdo where pdo.det_dcto_odo_aten=deod.consecutivo) as puedoeliminar " +
									"from " +
										"odontologia.descuentos_odon_aten  deod  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and deod.consecutivo= "+dtoWhere.getConsecutivo():"";
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and deod.centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and deod.fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and deod.hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and deod.institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and deod.usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
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
				DtoDescuentoOdontologicoAtencion dto = new DtoDescuentoOdontologicoAtencion();
				dto.setConsecutivo(rs.getDouble("consecutivo"));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"),rs.getString("nombre_centro_atencion")));
				dto.setPorcentajeDescuento(rs.getDouble("porcentaje_dcto"));
				dto.setNivelAutorizacion(new InfoDatosDouble(rs.getDouble("nivel_autorizacion"), rs.getString("nombre_nivel_autorizacion")));
				dto.setDiasVigencia(rs.getInt("dias_vigencia"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setPuedoEliminar(rs.getInt("puedoeliminar")<=0);
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
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDescuentosOdontologicos> cargar(DtoDescuentosOdontologicos dtoWhere) 
	{
		ArrayList<DtoDescuentosOdontologicos> arrayDto = new ArrayList<DtoDescuentosOdontologicos>();
		String consultaStr = 	"SELECT " +
										"deod.consecutivo as consecutivo , " +
										"deod.centro_atencion as centro_atencion , " +
										"getnomcentroatencion(deod.centro_atencion) as nombre_centro_atencion ,"+ 
										"to_char(deod.fecha_ini_vigencia,'DD/MM/YYYY') as fecha_ini_vigencia , "+
										"to_char(deod.fecha_fin_vigencia,'DD/MM/YYYY') as fecha_fin_vigencia , "+
										"deod.institucion as institucion , " +
										"deod.fecha_modifica as fecha_modifica , " +
										"deod.hora_modifica as hora_modifica , " +
										"deod.usuario_modifica as usuario_modifica " +
									"from " +
										"odontologia.descuentos_odon  deod  " + 
									"where " +
										"1=1 ";
										
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and deod.consecutivo= "+dtoWhere.getConsecutivo():"";
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and deod.centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaFinVigenciaFromatoBD())?" and deod.fecha_fin_vigencia= '"+dtoWhere.getFechaFinVigenciaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaInicioVigenciaFromatoBD())?" and deod.fecha_ini_vigencia= '"+dtoWhere.getFechaInicioVigenciaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and deod.fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and deod.hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and deod.institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and deod.usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
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
				DtoDescuentosOdontologicos dto = new DtoDescuentosOdontologicos();
				dto.setConsecutivo(rs.getDouble("consecutivo"));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"),rs.getString("nombre_centro_atencion")));
				dto.setFechaFinVigencia(rs.getString("fecha_fin_vigencia"));
				dto.setFechaInicioVigencia(rs.getString("fecha_ini_vigencia"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
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
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDescuentosOdontologicos dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.descuentos_odon WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and consecutivo= "+dtoWhere.getConsecutivo():"";
		
		
		logger.info("elmi-->"+consultaStr);
		
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
			logger.error("ERROR EN eliminar  des odo "+ e);
		
		}
		return false;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminarAtencion(DtoDescuentoOdontologicoAtencion dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.descuentos_odon_aten WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getConsecutivo()>ConstantesBD.codigoNuncaValido)?" and consecutivo= "+dtoWhere.getConsecutivo():"";
		
		
		logger.info("elmi-->"+consultaStr);
		
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
			logger.error("ERROR EN eliminar  des odo "+ e);
		
		}
		return false;
	}

	
	
	
	/**
	 * CADENA para insertar
	 */
	private static String inserccionAtencionStr2 = "INSERT INTO odontologia.descuentos_odon_aten (   " +
																					"consecutivo , " +//1
																					"centro_atencion , " +//2
																					"dias_vigencia , " +//3
																					"nivel_autorizacion , " +//4
																					"porcentaje_dcto , " +//5
																					"fecha_modifica , " +//6
																					"hora_modifica , " +//7
																					"institucion  , " +//8
																					"usuario_modifica) " +//9
																		"values (" +
																					"? , " +//1
																					"? , " +//2
																					"? , " +//3
																					"? , " +//4
																					"? , " +//5
																					"? , " +//6
																					"? , " +//7
																					"? , " +//8
																					"? ) ";//9

	
	/**
	 * 
	 * 
	 */
	public static double guardarAtencion(DtoDescuentoOdontologicoAtencion dto) 
	{
		
		
		logger.info(UtilidadLog.obtenerString(dto, true));
		
		
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionAtencionStr2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			ps.setDouble(1, dto.getConsecutivo());				
		    ps.setInt(2, dto.getCentroAtencion().getCodigo());		    
		    ps.setInt(3, dto.getDiasVigencia());
		    ps.setDouble(4, dto.getNivelAutorizacion().getCodigo());		
		    ps.setDouble(5, dto.getPorcentajeDescuento());
			ps.setString(6, dto.getFechaModificaFromatoBD());
			ps.setString(7, dto.getHoraModifica());
			ps.setInt(8, dto.getInstitucion());
			ps.setString(9, dto.getUsuarioModifica());
			
			logger.info("cadena >> "+inserccionStr+" "+UtilidadLog.obtenerString(dto, true));
			
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
	public static double guardar(DtoDescuentosOdontologicos dto) 
	{
		
		
		logger.info(UtilidadLog.obtenerString(dto, true));
		
		
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			ps.setDouble(1, dto.getConsecutivo());				
		    ps.setInt(2, dto.getCentroAtencion().getCodigo());		    
		    ps.setString(3, dto.getFechaInicioVigenciaFromatoBD());
		    ps.setString(4, dto.getFechaFinVigenciaFromatoBD());			
			ps.setString(5, dto.getFechaModificaFromatoBD());
			ps.setString(6, dto.getHoraModifica());
			ps.setInt(7, dto.getInstitucion());
			ps.setString(8, dto.getUsuarioModifica());
			
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
	 */

	public static boolean existeCruceFechas(DtoDescuentosOdontologicos  dto, double codigoPkNotIn , int centroAtencion)
	{

		String consultaStr="SELECT consecutivo FROM odontologia.descuentos_odon WHERE " +
				"(('"+dto.getFechaInicioVigenciaFromatoBD()+" "+"00:00"+"' " +
				"between to_char(fecha_ini_vigencia, 'YYYY-MM-DD')  || ' ' || '00:00' " +
				"and to_char(fecha_fin_vigencia, 'YYYY-MM-DD') || ' ' || '23:59'  )"+
                 " or ('"+dto.getFechaFinVigenciaFromatoBD()+"23:59"+"' " +
				 "between to_char(fecha_ini_vigencia, 'YYYY-MM-DD')  || ' ' || '00:00' " +
				 "and to_char(fecha_fin_vigencia, 'YYYY-MM-DD') || ' ' || '23:59'  ))";
		
		if(codigoPkNotIn>0)
		{
			consultaStr+=" and consecutivo <> "+codigoPkNotIn;
		}
		if(centroAtencion > 0)
		{
			consultaStr+=" and centro_atencion = "+centroAtencion;
		}
		
		logger.info(consultaStr);
		
		boolean retorna=false;
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			if(ps.executeQuery().next())
			{
				retorna=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			} 
		catch (SQLException e) 
		{
			logger.error("ERROR fechas ");
			e.printStackTrace();
		}
		return retorna;
	}

	
	
	
}
