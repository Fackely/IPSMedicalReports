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
import com.princetonsa.dto.odontologia.DtoDetalleDescuentoOdontologico;

public class SqlBaseDetalleDescuentoOdontologicoDao {

private static Logger logger = Logger.getLogger(SqlBaseDetalleDescuentoOdontologicoDao.class);
	

/**
 * CADENA para insertar
 */
private static String inserccionStr = " INSERT INTO odontologia.det_descuentos_odon (" +
																						"codigo ,"+ //1
																						"consecutivo_descuento , " +//2
																						"valor_min_presupuesto , " +//3
																						"valor_max_presupuesto , " +//4
																						"dias_vigencia_descuento , " +//5
																						"tipo_usuario_autoriza , " +//6
																						"fecha_modifica , " +//7
																						"hora_modifica , " +//8
																						"porcentaje_descuento  , " +//9
																						"usuario_modifica ) " +//10
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
																						"?  ) ";//10


/**
 * 
 * @param DescuentoOdontologico
 * @return
 */
public static boolean modificar(DtoDetalleDescuentoOdontologico dtoNuevo, DtoDetalleDescuentoOdontologico dtoWhere) 
{
	boolean retorna=false;
	String consultaStr = "UPDATE odontologia.det_descuentos_odon  set codigo=codigo";
	
	consultaStr+= (dtoNuevo.getConsecutivoDescuento() > ConstantesBD.codigoNuncaValido)?" , consecutivo_descuento= "+dtoNuevo.getConsecutivoDescuento():"";
	consultaStr+= (dtoNuevo.getValorMinimoPresupuesto() > ConstantesBD.codigoNuncaValido)?" , valor_min_presupuesto= "+dtoNuevo.getValorMinimoPresupuesto():"";
	consultaStr+= (dtoNuevo.getValorMaximoPresupuesto() > ConstantesBD.codigoNuncaValido)?" , valor_max_presupuesto= "+dtoNuevo.getValorMaximoPresupuesto():"";
	consultaStr+= (dtoNuevo.getPorcentajeDescuento() > ConstantesBD.codigoNuncaValido)?" , porcentaje_descuento= "+dtoNuevo.getPorcentajeDescuento():"";
	consultaStr+= (dtoNuevo.getDiasVigencia() > ConstantesBD.codigoNuncaValido)?" , dias_vigencia_descuento= "+dtoNuevo.getDiasVigencia():"";
	consultaStr+= (Double.parseDouble(dtoNuevo.getTipoUsuarioAutoriza().getCodigo()) > ConstantesBD.codigoNuncaValido)?" , tipo_usuario_autoriza= "+Double.parseDouble(dtoNuevo.getTipoUsuarioAutoriza().getCodigo()):"";
	
	
	consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModificaBD()+"' ":"";
	
	consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";

	consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
	
	consultaStr+=" where 1=1 ";
	
	consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and codigo= "+dtoWhere.getCodigo():"";
	
	consultaStr+= (dtoWhere.getConsecutivoDescuento()>ConstantesBD.codigoNuncaValido)?" and consecutivo_descuento= "+dtoWhere.getConsecutivoDescuento():"";
		
	
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
public static ArrayList<DtoDetalleDescuentoOdontologico> cargar(DtoDetalleDescuentoOdontologico dtoWhere) 
{
	ArrayList<DtoDetalleDescuentoOdontologico> arrayDto = new ArrayList<DtoDetalleDescuentoOdontologico>();
	
	
	String consultaStr = 	"SELECT " +
	                                "deo.codigo as codigo , " +
									"deo.consecutivo_descuento as consecutivo_descuento , " +
									"deo.tipo_usuario_autoriza as tipo_usuario_autoriza , " +
									"deo.valor_min_presupuesto as valor_min_presupuesto , "+
									"(select tu.descripcion from odontologia.tipos_usuarios tu where tu.codigo=deo.tipo_usuario_autoriza) as nombre_tipo_usuario ,"+ 
									"deo.valor_max_presupuesto as valor_max_presupuesto , "+
									"deo.dias_vigencia_descuento as dias_vigencia_descuento , "+
									"deo.porcentaje_descuento as porcentaje_descuento , " +
									"deo.fecha_modifica as fecha_modifica , " +
									"deo.hora_modifica as hora_modifica , " +
									"deo.usuario_modifica as usuario_modifica,  " +
									"(select count(1) from  odontologia.presupuesto_dcto_odon pdo where pdo.det_dcto_odo=deo.codigo) as puedoeliminar " +
								    " from " +
									"odontologia.det_descuentos_odon deo  " +
								"where " +
									"1=1 ";
									
								
	consultaStr+= (dtoWhere.getCodigo()>ConstantesBD.codigoNuncaValido)?" and deo.codigo= "+dtoWhere.getCodigo():"";
	consultaStr+= (dtoWhere.getConsecutivoDescuento() >ConstantesBD.codigoNuncaValido)?" and deo.consecutivo_descuento= "+dtoWhere.getConsecutivoDescuento():"";
	

	try 
	{
		logger.info("\n\n\n\n\n SQL cargar Det Des odontologico / " + consultaStr);
		Connection con = UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=null;
		rs = new ResultSetDecorator(ps.executeQuery());
		while (rs.next()) 
		{
			DtoDetalleDescuentoOdontologico dto = new DtoDetalleDescuentoOdontologico();
			dto.setCodigo(rs.getDouble("codigo"));
			dto.setConsecutivoDescuento(rs.getDouble("consecutivo_descuento"));
			dto.setTipoUsuarioAutoriza(new InfoDatosStr(String.valueOf(rs.getDouble("tipo_usuario_autoriza")),rs.getString("nombre_tipo_usuario")));
			dto.setValorMinimoPresupuesto(rs.getDouble("valor_min_presupuesto"));
			dto.setValorMaximoPresupuesto(rs.getDouble("valor_max_presupuesto"));
			dto.setDiasVigencia(rs.getDouble("dias_vigencia_descuento"));
			dto.setPorcentajeDescuento(rs.getDouble("porcentaje_descuento"));
			dto.setFechaModifica(rs.getDate("fecha_modifica").toString());
			dto.setHoraModifica(rs.getString("hora_modifica"));
			dto.setUsuarioModifica(rs.getString("usuario_modifica"));
			
			dto.setPuedoEliminar(rs.getInt("puedoeliminar")<=0);
			
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
 * @param dtoWhere
 * @return
 */
public static boolean eliminar(DtoDetalleDescuentoOdontologico dtoWhere) 
{
	String consultaStr = "DELETE FROM  odontologia.det_descuentos_odon  WHERE 1=1 ";
	
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
		logger.error("ERROR EN eliminar  des odo "+ e);
	
	}
	return false;
}


/**
 * 
 * @param dto
 * @return
 */
public static double guardar(DtoDetalleDescuentoOdontologico dto) 
{
	
	double secuencia = ConstantesBD.codigoNuncaValidoDouble;
	try 
	{
		
		
		
		Connection con = UtilidadBD.abrirConexion();
		secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_descuentos_odon");
		ResultSetDecorator rs = null;
		PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setDouble(1, secuencia);
		
			
	    ps.setDouble(2, dto.getConsecutivoDescuento());
	    
	    ps.setDouble(3, dto.getValorMinimoPresupuesto());
	    
	    ps.setDouble(4, dto.getValorMaximoPresupuesto());
	    
	    ps.setDouble(5, dto.getDiasVigencia());
	    
	    ps.setDouble(6, Double.parseDouble(dto.getTipoUsuarioAutoriza().getCodigo()));
	    
	   
		
		ps.setDate(7, Date.valueOf(dto.getFechaModificaFromatoBD()));
		ps.setString(8, dto.getHoraModifica());
		ps.setDouble(9, dto.getPorcentajeDescuento());
		ps.setString(10, dto.getUsuarioModifica());
		

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

	public static boolean existeRangoPresupuesto(double consecutivo, double centroAtencion, double ValorMinimo, double ValorMaximo,double codigo)
	{
		String consultaStr="SELECT codigo FROM odontologia.det_descuentos_odon dtodo  INNER JOIN odontologia.descuentos_odon enc on (dtodo.consecutivo_descuento="+consecutivo+")  WHERE dtodo.consecutivo_descuento="+consecutivo+" and enc.centro_atencion="+centroAtencion+" and( ("+ValorMinimo+" between valor_min_presupuesto and valor_max_presupuesto) or ("+ValorMaximo+" between valor_min_presupuesto and valor_max_presupuesto) )"; 
	
		if(codigo > ConstantesBD.codigoNuncaValido){
			consultaStr+="and dtodo.codigo <> "+codigo;
		}
	
		try 
		{
			logger.info("Consulta existe Rango"+consultaStr);	
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				return true;
			}
	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error consulta existe rango " + e);
		}
		return false;
	}


}

	

