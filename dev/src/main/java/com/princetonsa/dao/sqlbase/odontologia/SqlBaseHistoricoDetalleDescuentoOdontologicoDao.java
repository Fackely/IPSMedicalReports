package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dto.odontologia.DtoHistoricoDetalleDescuentoOdontologico;

public class SqlBaseHistoricoDetalleDescuentoOdontologicoDao {

	private static Logger logger = Logger.getLogger(SqlBaseHistoricoDetalleDescuentoOdontologicoDao.class);
	

	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = " INSERT INTO odontologia.his_det_descuentos_odon (" +
																							"codigo ,"+ //1
																							"consecutivo_descuento , " +//2
																							"valor_min_presupuesto , " +//3
																							"valor_min_pre_mod , " +//4
																							"valor_max_presupuesto , " +//5
																							"valor_max_pre_mod , " +//6
																							"dias_vigencia_descuento , " +//7
																							"dias_vigencia_desc_mod , " +//8
																							"tipo_usuario_autoriza , " +//9
																							"tipo_usuario_aut_mod , " +//10
																							"fecha_modifica , " +//11
																							"hora_modifica , " +//12
																							"porcentaje_descuento  , " +//13
																							"porcentaje_descuento_mod  , " +//14
																							"eliminado  , " +//15
																							"detalle  , " +//16
																							"usuario_modifica ) " +//17
																				"values ( " +
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
																							"? , " +//14
																							"? , " +//15
																							"? , " +//16
																							
																							"?  ) ";//17


	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(DtoHistoricoDetalleDescuentoOdontologico dtoNuevo, DtoHistoricoDetalleDescuentoOdontologico dtoWhere) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.his_det_descuentos_odon  set codigo=codigo";
		
		consultaStr+=  ",eliminado = '"+dtoNuevo.getEliminado()+"'";
		
		
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";

		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
		
		consultaStr+= (dtoWhere.getConsecutivoDescuento()>ConstantesBD.codigoNuncaValido)?" and consecutivo_descuento= "+dtoWhere.getConsecutivoDescuento():"";
		
		consultaStr+= (dtoWhere.getDetalle()>ConstantesBD.codigoNuncaValido)?" and detalle= "+dtoWhere.getDetalle():"";
			
		
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
			logger.error("ERROR EN actualizar Detalle Descuentos Odontologia" + e);
			
		}
		return retorna;
	}

	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoHistoricoDetalleDescuentoOdontologico> cargar(DtoHistoricoDetalleDescuentoOdontologico dtoWhere) 
	{
		ArrayList<DtoHistoricoDetalleDescuentoOdontologico> arrayDto = new ArrayList<DtoHistoricoDetalleDescuentoOdontologico>();
		
		
		String consultaStr = 	"SELECT " +
		                                "deo.codigo as codigo , " +
										"deo.consecutivo_descuento as consecutivo_descuento , " +
										"deo.tipo_usuario_autoriza as tipo_usuario_autoriza , " +
										"deo.tipo_usuario_aut_mod as tipo_usuario_aut_mod , " +
										"deo.valor_min_presupuesto as valor_min_presupuesto , "+
										"deo.valor_min_pre_mod as valor_min_pre_mod , "+
										"(select tu.descripcion from odontologia.tipos_usuarios tu where tu.codigo=deo.tipo_usuario_autoriza) as nombre_tipo_usuario ,"+ 
										"(select tu.descripcion from odontologia.tipos_usuarios tu where tu.codigo=deo.tipo_usuario_aut_mod) as nombre_tipo_usuario_mod ,"+ 
										"deo.valor_max_presupuesto as valor_max_presupuesto , "+
										"deo.valor_max_pre_mod as valor_max_pre_mod , "+
										"deo.dias_vigencia_descuento as dias_vigencia_descuento , "+
										"deo.dias_vigencia_desc_mod as dias_vigencia_desc_mod , "+
										"deo.porcentaje_descuento as porcentaje_descuento , " +
										"deo.porcentaje_descuento_mod as porcentaje_descuento_mod , " +
										"deo.fecha_modifica as fecha_modifica , " +
										"deo.hora_modifica as hora_modifica , " +
										"deo.eliminado as eliminado , " +
										"deo.detalle as detalle , " +
										"deo.usuario_modifica as usuario_modifica   " +
									    " from  " +
										"odontologia.his_det_descuentos_odon deo  " +
									"where  " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and deo.codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getConsecutivoDescuento() >ConstantesBD.codigoNuncaValido)?" and deo.consecutivo_descuento= "+dtoWhere.getConsecutivoDescuento():"";
		consultaStr+= (dtoWhere.getDetalle() >ConstantesBD.codigoNuncaValido)?" and deo.detalle= "+dtoWhere.getDetalle():"";

		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Det Des odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoHistoricoDetalleDescuentoOdontologico dto = new DtoHistoricoDetalleDescuentoOdontologico();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setConsecutivoDescuento(rs.getDouble("consecutivo_descuento"));
				dto.setTipoUsuarioAutoriza(new InfoDatosStr(String.valueOf(rs.getDouble("tipo_usuario_autoriza")),rs.getString("nombre_tipo_usuario")));
				dto.setTipoUsuarioAutorizaMod(new InfoDatosStr(String.valueOf(rs.getDouble("tipo_usuario_aut_mod")),rs.getString("nombre_tipo_usuario_mod")));
				dto.setValorMinimoPresupuesto(rs.getDouble("valor_min_presupuesto"));
				dto.setValorMinimoPresupuestoMod(rs.getDouble("valor_min_pre_mod"));
				dto.setValorMaximoPresupuesto(rs.getDouble("valor_max_presupuesto"));
				dto.setValorMaximoPresupuestoMod(rs.getDouble("valor_max_pre_mod"));
				dto.setDiasVigencia(rs.getDouble("dias_vigencia_descuento"));
				dto.setDiasVigenciaMod(rs.getDouble("dias_vigencia_desc_mod"));
				dto.setPorcentajeDescuento(rs.getDouble("porcentaje_descuento"));
				dto.setPorcentajeDescuentoMod(rs.getDouble("porcentaje_descuento_mod"));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setDetalle(rs.getDouble("detalle"));
				dto.setEliminado(rs.getString("eliminado"));
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga det Des Odon==> " + e);
		}
		return arrayDto;
	}


	

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoHistoricoDetalleDescuentoOdontologico dto) 
	{
		
	
		
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
			
			
			
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_his_det_descuentos_odon");
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			ps.setDouble(1, secuencia);
			
				
		    ps.setDouble(2, dto.getConsecutivoDescuento());
		    
		    ps.setDouble(3, dto.getValorMinimoPresupuesto());
		    
		    ps.setDouble(4, dto.getValorMinimoPresupuestoMod());
		    
		    ps.setDouble(5, dto.getValorMaximoPresupuesto());
		    
		    ps.setDouble(6, dto.getValorMaximoPresupuestoMod());
		    
		    ps.setDouble(7, dto.getDiasVigencia());
		    
		    ps.setDouble(8, dto.getDiasVigenciaMod());
		    
		    ps.setDouble(9, Double.parseDouble(dto.getTipoUsuarioAutoriza().getCodigo()));
		    
		    ps.setDouble(10, Double.parseDouble(dto.getTipoUsuarioAutorizaMod().getCodigo()));
			
			ps.setString(11, dto.getFechaModificaFromatoBD());
			ps.setString(12, dto.getHoraModifica());
			ps.setDouble(13, dto.getPorcentajeDescuento());
			ps.setDouble(14, dto.getPorcentajeDescuentoMod());
			ps.setString(15, dto.getEliminado());
			ps.setDouble(16, dto.getDetalle());
			ps.setString(17, dto.getUsuarioModifica());
			
			

			if (ps.executeUpdate() > 0) 
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return secuencia;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en insert Des Odo " + e);
			secuencia = ConstantesBD.codigoNuncaValidoDouble;
		}
		return secuencia;
	}


	}
		