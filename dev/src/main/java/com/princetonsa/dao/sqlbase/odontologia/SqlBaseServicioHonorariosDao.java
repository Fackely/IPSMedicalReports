package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoServicioHonorarios;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseServicioHonorariosDao 
{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseServicioHonorariosDao.class);
	
	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = " INSERT INTO odontologia.honorarios_esp_serv( " +
																							"codigo , " +//1
																							"esquema_tarifario , " +//2
																							"fecha_modifica , " +//3
																							"hora_modifica , " +//4
																							"institucion  , " +//5
																							"usuario_modifica, " +//6
																							"centro_atencion ) " +//7
																							"values ( " +
																							"? , " +//1
																							"? , " +//2
																							"? , " +//3
																							"? , " +//4
																							"? , " +//5
																							"? , " +//6
																							"? ) ";//7


	/**
	 * 
	 * @param objetoServicioHonorarios
	 * @return
	 */
	public static boolean modificar(DtoServicioHonorarios dtoNuevo, DtoServicioHonorarios dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.honorarios_esp_serv set codigo=codigo ";
		
		if(dtoNuevo.getEsquemaTarifario().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=", esquema_tarifario is null ";
		}
		else
		{	
			consultaStr+= (dtoNuevo.getEsquemaTarifario().getCodigo()>0)?" , esquema_tarifario= "+dtoNuevo.getEsquemaTarifario().getCodigo():"";
		}
		
		if(dtoNuevo.getCentroAtencion().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=", centro_atencion is null ";
		}
		else
		{	
			consultaStr+= (dtoNuevo.getCentroAtencion().getCodigo()>0)?" , centro_atencion= "+dtoNuevo.getCentroAtencion().getCodigo():"";
		}
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModificaFromatoBD())?" , fecha_modifica= '"+dtoNuevo.getFechaModificaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		consultaStr+= (dtoNuevo.getInstitucion()>ConstantesBD.codigoNuncaValido)?" , institucion= "+dtoNuevo.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		
		
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
		
		if(dtoWhere.getEsquemaTarifario().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and esquema_tarifario= null ";
		}
		else
		{	
			consultaStr+= (dtoWhere.getEsquemaTarifario().getCodigo()>0)?" and esquema_tarifario= "+dtoWhere.getEsquemaTarifario().getCodigo():"";
		}
		
		if(dtoWhere.getCentroAtencion().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=", centro_atencion is null ";
		}
		else
		{	
			consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" , centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		}
		
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
			logger.error("ERROR EN actualizar el honorario" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoServicioHonorarios> cargar(	DtoServicioHonorarios dtoWhere) 
	{
		ArrayList<DtoServicioHonorarios> arrayDto = new ArrayList<DtoServicioHonorarios>();
		String consultaStr = 	"SELECT " +
										"hes.codigo, " +
										"coalesce(hes.esquema_tarifario, "+ConstantesBD.codigoNuncaValido+") as esquema_tarifario, " +
										"case when hes.esquema_tarifario is null then 'Todos' else getnombreesquematarifario(hes.esquema_tarifario) end as nombre_esquema_tarifario, " +
										"coalesce(hes.centro_atencion, "+ConstantesBD.codigoNuncaValido+") as centro_atencion, " +
										"case when hes.centro_atencion is null then 'Todos' else getnomcentroatencion(hes.centro_atencion) end as nombre_centro_atencion, " +
										"hes.institucion, " +
										"hes.fecha_modifica, " +
										"hes.hora_modifica, " +
										"hes.usuario_modifica " +
									"from " +
										"odontologia.honorarios_esp_serv hes " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>0)?" and hes.codigo= "+dtoWhere.getCodigo():"";
		
		if(dtoWhere.getEsquemaTarifario().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+= " AND hes.esquema_tarifario is null ";
		}
		else
		{
			consultaStr+= (dtoWhere.getEsquemaTarifario().getCodigo()>0)?" and hes.esquema_tarifario= "+dtoWhere.getEsquemaTarifario().getCodigo():" ";
		}	
		if(dtoWhere.getCentroAtencion().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and hes.centro_atencion is null ";
		}
		else
		{	
			consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and hes.centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		}
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and hes.fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hes.hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and hes.institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and hes.usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+=" order by hes.esquema_tarifario, hes.centro_atencion "; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Honorario / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoServicioHonorarios dto = new DtoServicioHonorarios();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setEsquemaTarifario(new InfoDatosInt(rs.getInt("esquema_tarifario"), rs.getString("nombre_esquema_tarifario")));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"), rs.getString("nombre_centro_atencion")));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga==> " + e);
		}
		return arrayDto;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoServicioHonorarios dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.honorarios_esp_serv WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
		
		if(dtoWhere.getEsquemaTarifario().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and esquema_tarifario is null ";
		}
		else
		{	
			consultaStr+= (dtoWhere.getEsquemaTarifario().getCodigo()>0)?" and esquema_tarifario= "+dtoWhere.getEsquemaTarifario().getCodigo():"";
		}
		
		if(dtoWhere.getCentroAtencion().getCodigo()==ConstantesBD.codigoNuncaValido)
		{
			consultaStr+=" and centro_atencion is null ";
		}
		else
		{	
			consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		}
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and institucion= "+dtoWhere.getInstitucion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		
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
			logger.error("ERROR EN eliminar  honorario "+ e);
		
		}
		return false;
	}

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoServicioHonorarios dto) 
	{
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_honorarios_esp_serv");
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, secuencia);
			
			if(dto.getEsquemaTarifario().getCodigo()>0)
			{	
				ps.setInt(2, dto.getEsquemaTarifario().getCodigo());
			}	
			else
			{	
				ps.setNull(2, Types.INTEGER);
			}	
			ps.setDate(3, Date.valueOf(dto.getFechaModificaFromatoBD()));
			ps.setString(4, dto.getHoraModifica());
			ps.setInt(5, dto.getInstitucion());
			ps.setString(6, dto.getUsuarioModifica());

			if(dto.getCentroAtencion().getCodigo()>0)
			{	
				ps.setInt(7, dto.getCentroAtencion().getCodigo());
			}	
			else
			{	
				ps.setNull(7, Types.INTEGER);
			}
			
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return secuencia;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
			secuencia = ConstantesBD.codigoNuncaValidoDouble;
		}
		return secuencia;
	}
	
	
	

}