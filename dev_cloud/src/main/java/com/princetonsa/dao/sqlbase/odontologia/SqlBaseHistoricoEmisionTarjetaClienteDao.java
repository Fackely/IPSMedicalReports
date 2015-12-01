package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoHistoricoEmisionTarjetaCliente;

public class SqlBaseHistoricoEmisionTarjetaClienteDao {

	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = "INSERT INTO odontologia.log_enca_emi_tarjeta_cliente (   " +
																					"codigo , " +//1
																					"enca_emi_tarjeta , " +//2
																					"tipo_tarjeta , " +//3
																					"serial_inicial , " +//4
																					"serial_final , " +//5
																					"fecha_modifica , " +//6
																					"eliminado , " +//7
																					"hora_modifica , " +//8
																					
																					"usuario_modifica ) " +//9
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
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoHistoricoEmisionTarjetaCliente dtoNuevo, DtoHistoricoEmisionTarjetaCliente dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.log_enca_emi_tarjeta_cliente  set codigo=codigo";
		
		if(!UtilidadTexto.isEmpty(dtoNuevo.getTipoTarjeta().getCodigo())){
		consultaStr+= (Double.parseDouble(dtoNuevo.getTipoTarjeta().getCodigo()) > ConstantesBD.codigoNuncaValido)?" , tipo_tarjeta= "+Double.parseDouble(dtoNuevo.getTipoTarjeta().getCodigo()):"";
		}
		consultaStr+= (dtoNuevo.getCodigoEmisionTarjeta() > ConstantesBD.codigoNuncaValido)?" , enca_emi_tarjeta= "+dtoNuevo.getCodigoEmisionTarjeta():"";
		consultaStr+= (dtoNuevo.getSerialInicial()> ConstantesBD.codigoNuncaValido)?" , serial_inicial= "+dtoNuevo.getSerialInicial():"";
		consultaStr+= (dtoNuevo.getSerialFinal()> ConstantesBD.codigoNuncaValido)?" , serial_final= "+dtoNuevo.getSerialFinal():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getEliminado())?" , eliminado= '"+dtoNuevo.getEliminado()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		if(!UtilidadTexto.isEmpty(dtoWhere.getTipoTarjeta().getCodigo())){
		consultaStr+= (Double.parseDouble(dtoWhere.getTipoTarjeta().getCodigo())> ConstantesBD.codigoNuncaValido)?" and tipo_tarjeta= "+Double.parseDouble(dtoWhere.getTipoTarjeta().getCodigo()):"";
		}
		consultaStr+= (dtoWhere.getCodigoEmisionTarjeta() > ConstantesBD.codigoNuncaValido)?" and enca_emi_tarjeta= "+dtoWhere.getCodigoEmisionTarjeta():"";
			
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":" ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getEliminado())?" and eliminado= '"+dtoWhere.getEliminado()+"' ":" ";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			Log4JManager.info("modificar-->"+consultaStr);
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN actualizar log Emision Tarjeta Odontologia", e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoEmisionTarjetaCliente> cargar(DtoHistoricoEmisionTarjetaCliente dtoWhere) 
	{
		ArrayList<DtoHistoricoEmisionTarjetaCliente> arrayDto = new ArrayList<DtoHistoricoEmisionTarjetaCliente>();
		String consultaStr = 	"SELECT " +
										"lemit.codigo as codigo , " +
										"lemit.enca_emi_tarjeta as enca_emi_tarjeta , " +
										"lemit.tipo_tarjeta as tipo_targeta , "+
										"lemit.serial_inicial as serial_inicial , "+
										"lemit.serial_final as serial_final , "+
										
										"(select tar.nombre from odontologia.tipos_tarj_cliente tar where tar.codigo_pk=lemit.tipo_tarjeta) as nombre_tipo_tarjeta ,"+ 
										
										"lemit.fecha_modifica as fecha_modifica , " +
										"lemit.hora_modifica as hora_modifica , " +
										"lemit.eliminado as eliminado , " +
										"lemit.usuario_modifica as usuario_modifica  " +
									"from " +
										"odontologia.log_enca_emi_tarjeta_cliente  lemit  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and lemit.codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoEmisionTarjeta()>ConstantesBD.codigoNuncaValido)?" and lemit.enca_emi_tarjeta= "+dtoWhere.getCodigoEmisionTarjeta():"";
		consultaStr+= (Double.parseDouble(dtoWhere.getTipoTarjeta().getCodigo())>0)?" and lemit.tipo_tarjeta= "+dtoWhere.getTipoTarjeta().getCodigo():"";
		consultaStr+= (dtoWhere.getSerialInicial()>0)?" and lemit.serial_inicial= "+dtoWhere.getSerialInicial():"";
		consultaStr+= (dtoWhere.getSerialFinal()>0)?" and lemit.serial_final = "+dtoWhere.getSerialFinal():"";
		
		consultaStr+=" order by lemit.codigo asc"; 
		try 
		{
			Log4JManager.info("\n\n\n\n\n SQL cargar emison log Tar odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoHistoricoEmisionTarjetaCliente dto = new DtoHistoricoEmisionTarjetaCliente();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setCodigoEmisionTarjeta(rs.getDouble("enca_emi_tarjeta"));
				dto.setTipoTarjeta(new InfoDatosStr(String.valueOf(rs.getDouble("tipo_tarjeta")),rs.getString("nombre_tipo_tarjeta")));
				dto.setSerialInicial(rs.getDouble("serial_inicial"));
				dto.setSerialFinal(rs.getDouble("serial_final"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setEliminado(rs.getString("eliminado"));
				
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("error en carga Des log Odon==> ", e);
		}
		return arrayDto;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoHistoricoEmisionTarjetaCliente dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.log_enca_emi_tarjeta_cliente  WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		
		Log4JManager.info("elmi-->"+consultaStr);
		
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
			Log4JManager.error("ERROR EN eliminar  emi  log tar odo "+ e);
		
		}
		return false;
	}

	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoHistoricoEmisionTarjetaCliente dto) 
	{
		
		
		Log4JManager.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_log_enca_emi_tarjeta_cli ");
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
		
			
			
			ps.setDouble(1, secuencia);	
			ps.setDouble(2, dto.getCodigoEmisionTarjeta());
		    ps.setDouble(3, Double.parseDouble(dto.getTipoTarjeta().getCodigo()));		    
		    ps.setDouble(4, dto.getSerialInicial());
		    ps.setDouble(5, dto.getSerialFinal());
		    		
			ps.setString(6, dto.getFechaModificaFromatoBD());
			ps.setString(7, dto.getEliminado());
			ps.setString(8, dto.getHoraModifica());
			
			ps.setString(9, dto.getUsuarioModifica());
 
			Log4JManager.info(inserccionStr);
			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return dto.getCodigo();
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR en insert log Emi tar + dto.getCodigo()", e );
		}
		return dto.getCodigo();
	}
	
	
	
	
}
