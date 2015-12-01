package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleEmisionTarjetaCliente;

public class SqlBaseHistoricoDetalleEmisionTarjetaClienteDao {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDetalleEmisionTarjetaClienteDao.class);
	
	/**
	 * CADENA para insertar
	 */
	 
	
	
	 
	 
	private static String inserccionStr = "INSERT INTO odontologia.log_det_emi_tarjeta_cliente(   " +
																					"codigo , " +//1
																					"det_emi_tarjeta , " +//2
																					"serial_inicial , " +//3
																					"serial_final , " +//4
																					"centro_atencion  , " +//5
																					"usuario_responsable  , " +//6
																					"fecha_modifica , " +//7
																					"hora_modifica , " +//8		
																					"eliminado , " +//9	
																					"usuario_modifica ) " +//10
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
																					"? ) ";//10


	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoHistoricoDetalleEmisionTarjetaCliente dtoNuevo, DtoHistoricoDetalleEmisionTarjetaCliente  dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.log_det_emi_tarjeta_cliente  set codigo=codigo";
		
		consultaStr+= (dtoNuevo.getCentroAtencion().getCodigo() > 0)?" , centro_atencion= "+dtoNuevo.getCentroAtencion().getCodigo():"";
		consultaStr+= (dtoNuevo.getCodigoDetalleEmision() > ConstantesBD.codigoNuncaValido)?" , det_emi_tarjeta= "+dtoNuevo.getCodigoDetalleEmision():"";
		consultaStr+= (dtoNuevo.getSerialInicial()> ConstantesBD.codigoNuncaValido)?" , serial_inicial= "+dtoNuevo.getSerialInicial():"";
		consultaStr+= (dtoNuevo.getSerialFinal()> ConstantesBD.codigoNuncaValido)?" , serial_final= "+dtoNuevo.getSerialFinal():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getEliminado())?" , eliminado= '"+dtoNuevo.getEliminado()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioResponsable())?" , usuario_responsable= '"+dtoNuevo.getUsuarioResponsable()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoDetalleEmision()>ConstantesBD.codigoNuncaValido)?" and det_emi_tarjeta= "+dtoWhere.getCodigoDetalleEmision():"";
		
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()> 0)?" and centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
			
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":" ";
		
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
			logger.error("ERROR EN actualizar his det Emision tarjeta Odontologia" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoDetalleEmisionTarjetaCliente> cargar(DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) 
	{
		ArrayList<DtoHistoricoDetalleEmisionTarjetaCliente> arrayDto = new ArrayList<DtoHistoricoDetalleEmisionTarjetaCliente>();
		String consultaStr = 	"SELECT " +
										"hdemit.codigo as codigo , " +
										"hdemit.det_emi_tarjeta as det_emi_tarjeta , " +
										"hdemit.centro_atencion as centro_atencion , "+
										"hdemit.serial_inicial as serial_inicial , "+
										"hdemit.serial_final as serial_final , "+
										
										"getnomcentroatencion(hdemit.centro_atencion) as nombre_centro_atencion ,"+ 
										
										"hdemit.fecha_modifica as fecha_modifica , " +
										"hdemit.hora_modifica as hora_modifica , " +
										"hdemit.usuario_responsable as usuario_responsable , " +
										"hdemit.eliminado as eliminado , " +
										"hdemit.usuario_modifica as usuario_modifica  " +
									"from " +
										"odontologia.log_det_emi_tarjeta_cliente  hdemit  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and hdemit.codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and hdemit.centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoDetalleEmision()>0)?" and hdemit.det_emi_tarjeta= "+dtoWhere.getCodigoDetalleEmision():"";
		consultaStr+= (dtoWhere.getSerialInicial()>0)?" and hdemit.serial_inicial= "+dtoWhere.getSerialInicial():"";
		consultaStr+= (dtoWhere.getSerialFinal()>0)?" and hdemit.serial_final = "+dtoWhere.getSerialFinal():"";
		
		consultaStr+=" order by hdemit.codigo asc"; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoHistoricoDetalleEmisionTarjetaCliente dto = new DtoHistoricoDetalleEmisionTarjetaCliente();
				
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setCodigoDetalleEmision(rs.getDouble("det_emi_tarjeta"));
				
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"),rs.getString("nombre_centro_atencion")));
				dto.setSerialInicial(rs.getDouble("serial_inicial"));
				dto.setSerialFinal(rs.getDouble("serial_final"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setEliminado(rs.getString("eliminado"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setUsuarioResponsable(rs.getString("usuario_responsable"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga det env==> " + e);
		}
		return arrayDto;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoHistoricoDetalleEmisionTarjetaCliente dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.log_det_emi_tarjeta_cliente  WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		
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
			logger.error("ERROR EN eliminar  det emi tar odo "+ e);
		
		}
		return false;
	}

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoHistoricoDetalleEmisionTarjetaCliente dto) 
	{
		
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		logger.info(UtilidadLog.obtenerString(dto, true));
		
		
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_log_det_emi_tarjeta_cli");
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			
			
			ps.setDouble(1, secuencia);				
			ps.setDouble(2, dto.getCodigoDetalleEmision() );	    
		    ps.setDouble(3, dto.getSerialInicial());
		    ps.setDouble(4, dto.getSerialFinal());
		    ps.setInt(5, dto.getCentroAtencion().getCodigo());	
		    ps.setString(6, dto.getUsuarioResponsable());
			ps.setString(7, dto.getFechaModifica());
			ps.setString(8, dto.getHoraModifica());
			ps.setString(9, dto.getEliminado());
			
			ps.setString(10, dto.getUsuarioModifica());
 
			
			
			logger.info(inserccionStr);
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return dto.getCodigo();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert log det Emi tar " + e + dto.getCodigo());
			
			
		}
		return dto.getCodigo();
	}
	
	
	
}
