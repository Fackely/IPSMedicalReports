package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
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
import com.princetonsa.dto.odontologia.DtoDetalleEmisionesTarjetaCliente;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.servinte.axioma.mundo.impl.administracion.CentroCostoMundo;

public class SqlBaseDetalleEmisionTarjetaClienteDao {

	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDetalleEmisionTarjetaClienteDao.class);
	
	/**
	 * CADENA para insertar
	 */
	 

	
	 
	 
	private static String inserccionStr = "INSERT INTO odontologia.det_emi_tarjeta_cliente (   " +
																					"codigo , " +//1
																					"enca_emi_tarjeta , " +//2
																					"serial_inicial , " +//3
																					"serial_final , " +//4
																					"centro_atencion  , " +//5
																					"institucion  , " +//6
																					"usuario_responsable  , " +//7
																					"fecha_modifica , " +//8
																					"hora_modifica , " +//9																			
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
	public static boolean modificar(DtoDetalleEmisionesTarjetaCliente dtoNuevo, DtoDetalleEmisionesTarjetaCliente  dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.det_emi_targeta_cliente  set codigo=codigo";
		
		consultaStr+= (dtoNuevo.getCentroAtencion().getCodigo() > ConstantesBD.codigoNuncaValido)?" , centro_atencion= "+dtoNuevo.getCentroAtencion().getCodigo():"";
		consultaStr+= (dtoNuevo.getCodigoEmisiontargeta() > ConstantesBD.codigoNuncaValido)?" , enca_emi_tarjeta= "+dtoNuevo.getCodigoEmisiontargeta():"";
		consultaStr+= (dtoNuevo.getSerialInicial().doubleValue()> ConstantesBD.codigoNuncaValido)?" , serial_inicial= "+dtoNuevo.getSerialInicial():"";
		consultaStr+= (dtoNuevo.getSerialFinal().doubleValue()> ConstantesBD.codigoNuncaValido)?" , serial_final= "+dtoNuevo.getSerialFinal():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModificaFromatoBD()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioResponsable())?" , usuario_responsable= '"+dtoNuevo.getUsuarioResponsable()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoEmisiontargeta()>ConstantesBD.codigoNuncaValido)?" and enca_emi_tarjeta= "+dtoWhere.getCodigoEmisiontargeta():"";
		
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
			logger.error("ERROR EN actualizar det Emision Targeta Odontologia" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleEmisionesTarjetaCliente> cargar(DtoDetalleEmisionesTarjetaCliente dtoWhere) 
	{
		ArrayList<DtoDetalleEmisionesTarjetaCliente> arrayDto = new ArrayList<DtoDetalleEmisionesTarjetaCliente>();
		String consultaStr = 	"SELECT " +
										"demit.codigo as codigo , " +
										"demit.enca_emi_tarjeta as enca_emi_tarjeta , " +
										"demit.centro_atencion as centro_atencion , "+
										"demit.serial_inicial as serial_inicial , "+
										"demit.serial_final as serial_final , "+
										"demit.institucion as institucion , "+
										
										"getnomcentroatencion(demit.centro_atencion) as nombre_centro_atencion ,"+ 
										
										"demit.fecha_modifica as fecha_modifica , " +
										"demit.hora_modifica as hora_modifica , " +
										"demit.usuario_responsable as usuario_responsable , " +
										"demit.usuario_modifica as usuario_modifica " +
									"from " +
										" odontologia.det_emi_tarjeta_cliente  demit  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and demit.codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCentroAtencion().getCodigo()>0)?" and demit.centro_atencion= "+dtoWhere.getCentroAtencion().getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoEmisiontargeta()>0)?" and demit.enca_emi_tarjeta= "+dtoWhere.getCodigoEmisiontargeta():"";
		consultaStr+= (dtoWhere.getSerialInicial().doubleValue()>0)?" and demit.serial_inicial= "+dtoWhere.getSerialInicial():"";
		consultaStr+= (dtoWhere.getSerialFinal().doubleValue()>0)?" and demit.serial_final = "+dtoWhere.getSerialFinal():"";
		
		consultaStr+=" order by demit.serial_inicial asc"; 
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoDetalleEmisionesTarjetaCliente dto = new DtoDetalleEmisionesTarjetaCliente();
				
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setCodigoEmisiontargeta(rs.getDouble("enca_emi_tarjeta"));
				dto.setCentroAtencion(new InfoDatosInt(rs.getInt("centro_atencion"),rs.getString("nombre_centro_atencion")));
				dto.setSerialInicial(rs.getBigDecimal("serial_inicial"));
				dto.setSerialFinal(rs.getBigDecimal("serial_final"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setInstitucion(rs.getInt("institucion"));
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
	public static boolean eliminar(DtoDetalleEmisionesTarjetaCliente dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.det_emi_targeta_cliente  WHERE 1=1 ";
		
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
	public static double guardar(DtoDetalleEmisionesTarjetaCliente dto) 
	{
		
		
		
	
		
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		logger.info(UtilidadLog.obtenerString(dto, true));
		
		
		try 
		{
		
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_emi_targeta_cli");
			ResultSetDecorator rs = null;
		
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
		
			
			
			ps.setDouble(1, secuencia);				
			ps.setDouble(2, dto.getCodigoEmisiontargeta());	    
		    ps.setDouble(3, dto.getSerialInicial().doubleValue());
		    ps.setDouble(4, dto.getSerialFinal().doubleValue());
		    ps.setInt(5, dto.getCentroAtencion().getCodigo());	
		    ps.setInt(6, dto.getInstitucion());
		    ps.setString(7, dto.getUsuarioResponsable());
			ps.setString(8, dto.getFechaModificaFromatoBD());
			ps.setString(9, dto.getHoraModifica());			
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
			
			
			logger.error("ERROR en insert det Emi tar " + e + dto.getCodigo());
			
			
		}
		return dto.getCodigo();
	}
	

	/**
	 * 
	 * @param institucion
	 * @param encabezado
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public static boolean fueraRango(int institucion, double encabezado, double ValorMinimo, double ValorMaximo)
	{
		String consultaStr="SELECT denca.codigo FROM odontologia.det_emi_targeta_cliente denca  INNER JOIN administracion.instituciones ct on (ct.codigo="+institucion+" )  INNER JOIN odontologia.det_emi_targeta_cliente enca on (enca.codigo="+encabezado+" ) WHERE denca.centro_atencion=ct.consecutivo  and( ("+ValorMinimo+" < enca.serial_inicial) or ("+ValorMaximo+" > enca.serial_final) )"; 
		try 
		{
			logger.info("Consulta fuera Rango"+consultaStr);	
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error consulta existen detalles" + e);

		}
		return false;
	}
	
	
	/**
	 * 
	 * @param codigo
	 * @param institucion
	 * @param ValorMinimo
	 * @param ValorMaximo
	 * @return
	 */
	public static boolean existeRangoSeriales(double codigo, int institucion, double ValorMinimo, double ValorMaximo)
	{
		String consultaStr="SELECT denca.codigo FROM odontologia.det_emi_tarjeta_cliente denca  INNER JOIN administracion.instituciones ct on (ct.codigo="+institucion+" )  WHERE denca.institucion=ct.codigo  and( ("+ValorMinimo+" between denca.serial_inicial and denca.serial_final) or ("+ValorMaximo+" between denca.serial_inicial and denca.serial_final) )"; 

		if(codigo > ConstantesBD.codigoNuncaValido){
			consultaStr+="and denca.codigo <> "+codigo;
		}

		try 
		{
			logger.info("Consulta existe Rango"+consultaStr);	
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error consulta existen detalles" + e);

		}
		return false;
	}



	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static BigDecimal cargarSerialMayor(DtoDetalleEmisionesTarjetaCliente dto)
	{
		BigDecimal tmpSerial= BigDecimal.ZERO;
		String consultaS= "select max(serial_final) as serialMaximo from odontologia.det_emi_targeta_cliente " +
		"	where enca_emi_tarjeta="+dto.getCodigoEmisiontargeta() ;

		try 
		{

			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consultaS);
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				tmpSerial= rs.getBigDecimal("serialMaximo");
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);

		}
		catch (SQLException e) 
		{
			logger.error("error consulta existen detalles" + e);

		}
		return tmpSerial;
	}

	
	
	
}
