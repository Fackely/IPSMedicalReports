package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;

public class SqlBaseDetalleAgrupacionHonorarioDao {

	
private static Logger logger = Logger.getLogger(SqlBaseDetalleAgrupacionHonorarioDao.class);
	
	
	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = " INSERT INTO odontologia.det_agru_honora_esp_serv( " +
																										"codigo , " + //1
            																			                "codigo_honorario ," + //2
																								         "especialidad , "+ // 3 
			                                                                                             "fecha_modifica ,"+ //4 
			                                                                                          	  "hora_modifica ,"+ //5
			                                                                                          	  "porcentaje_participacion ,"+//6
			                                                                                          	  "grupo_servicio, "+ //7
			                                                                                          	   "usuario_modifica,"+//8
			                                                                                          	   "valor_participacion," +//9
			                                                                                          	   "tipo_servicio," + //10
			                                                                                          	  "cuenta_honorarios," + //11
			                                                                                          	  "cuenta_ingreso_inst," + //12			                                                                                          	 
			                                                                                          	"cuenta_vig_ant_inst)" +//13
			                                                                                          	   "values ("+
			                                                                                          	    "? ,"+//1
			                                                                                          	    "?, " + //2
			                                                                                          	   " ? ,"+ //3
			                                                                                          	   " ? ," + //4
			                                                                                          	   " ? , "+ //5
			                                                                                          	   " ? ,"+ //6
			                                                                                                "? , "+ //7
			                                                                                                "? , "+ //8
			                                                                                                "? , "+//9;
			                                                                                                "? , "+//10;
			                                                                                                "? , "+//11;
			                                                                                                "? , "+//12;
	                                                                                                        "? )";//13;
	
	
	
	/**
	 * 
	 * 
	 */
	public static double guardar(DtoDetalleAgrupacionHonorarios dto) 
	{
		
		
		logger.info(UtilidadLog.obtenerString(dto, true));
		
		
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
		    secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_agru_hon_esp_ser");
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1,secuencia );
			ps.setDouble(2,dto.getCodigoHonorario());
			
			if(dto.getEspecialidad().getCodigo()<=0)
			{
				ps.setNull(3, Types.INTEGER);
			}
			else
			{	
				ps.setInt(3, dto.getEspecialidad().getCodigo());
			}	
			
			ps.setDate(4, Date.valueOf(dto.getFechaModificaFromatoBD()));

			ps.setString(5, dto.getHoraModificada());
			
			
			if(dto.getPorcentajeParticipacion()>ConstantesBD.codigoNuncaValidoDouble) 
			{	
				ps.setDouble(6, dto.getPorcentajeParticipacion());
			}
			else
			{
				ps.setNull(6, Types.DOUBLE);
			}
			if(dto.getGrupoServicio().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)
			{	
				ps.setInt(7, dto.getGrupoServicio().getCodigo());
			}
			else
			{
				ps.setNull(7, Types.DOUBLE);
			}
			
			ps.setString(8, dto.getUsuarioModifica());
			
			if(dto.getValorParticipacion()>ConstantesBD.codigoNuncaValidoDouble)
			{	
				ps.setDouble(9, dto.getValorParticipacion());
			}
			else
			{
				ps.setNull(9, Types.DOUBLE);
			}
          
			
			if(!UtilidadTexto.isEmpty(dto.getTipoServicio().getCodigo()))
			{
				ps.setString(10, dto.getTipoServicio().getCodigo());
			}
			else
			{
				ps.setNull(10, Types.VARCHAR );
			}
			
			if(!UtilidadTexto.isEmpty(dto.getArrayCuentasContables().get(0).getCodigo()))
			{			
				ps.setString(11, dto.getArrayCuentasContables().get(0).getCodigo());
			}
			else
			{
				ps.setNull(11, Types.VARCHAR );
			}
			if(!UtilidadTexto.isEmpty(dto.getArrayCuentasContables().get(1).getCodigo()))
			{	
				ps.setString(12, dto.getArrayCuentasContables().get(1).getCodigo());
			}
			else
			{
				ps.setNull(12, Types.VARCHAR );
			}
			if(!UtilidadTexto.isEmpty(dto.getArrayCuentasContables().get(2).getCodigo()))
			{
				ps.setString(13, dto.getArrayCuentasContables().get(2).getCodigo());
			}
			else
			{
				ps.setNull(13, Types.VARCHAR );
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
	
	
	/**
	 * 
	 * @param objetoServicioHonorarios
	 * @return
	 */
	public static boolean modificar(DtoDetalleAgrupacionHonorarios dtoNuevo, DtoDetalleAgrupacionHonorarios dtoWhere, boolean siVacioUpdateNull) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.det_agru_honora_esp_serv set codigo=codigo ";
					
		consultaStr+= (dtoNuevo.getCodigoHonorario()>0)?" , codigo_honorario= "+dtoNuevo.getCodigoHonorario():"";
		consultaStr+= (dtoNuevo.getEspecialidad().getCodigo() > 0)?" , especialidad= '"+dtoNuevo.getEspecialidad().getCodigo()+"' ": ( (siVacioUpdateNull)?" , especialidad= null ":"" );
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModificaFromatoBD())?" , fecha_modifica= '"+dtoNuevo.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModificada())?" , hora_modifica= '"+dtoNuevo.getHoraModificada()+"' ":"";
		consultaStr+= (dtoNuevo.getPorcentajeParticipacion()>-1)?" , porcentaje_participacion= "+dtoNuevo.getPorcentajeParticipacion(): ( (siVacioUpdateNull)?" , porcentaje_participacion= null ":"" ); 
		consultaStr+= (dtoNuevo.getGrupoServicio().getCodigo()>0)?" , grupo_servicio= "+dtoNuevo.getGrupoServicio().getCodigo(): ( (siVacioUpdateNull)?" , grupo_servicio= null ":"" );  
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoNuevo.getValorParticipacion()>-1)?" , valor_participacion= "+dtoNuevo.getValorParticipacion():( (siVacioUpdateNull)?" , valor_participacion= null ":"" );
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getTipoServicio().getCodigo())?" , tipo_servicio = '"+dtoNuevo.getTipoServicio().getCodigo()+"' ":  ( (siVacioUpdateNull)?" , tipo_servicio= null ":"" );
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getArrayCuentasContables().get(0).getCodigo())?" , cuenta_honorarios= '"+dtoNuevo.getArrayCuentasContables().get(0).getCodigo()+"' ":( (siVacioUpdateNull)?" , cuenta_honorarios= null ":"" );
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getArrayCuentasContables().get(1).getCodigo())?" , cuenta_ingreso_inst= '"+dtoNuevo.getArrayCuentasContables().get(1).getCodigo()+"' ":( (siVacioUpdateNull)?" , cuenta_ingreso_inst= null ":"" );   
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getArrayCuentasContables().get(2).getCodigo())?" ,  cuenta_vig_ant_inst= '"+dtoNuevo.getArrayCuentasContables().get(2).getCodigo()+"' ":( (siVacioUpdateNull)?" , cuenta_vig_ant_inst= null ":"" );
		
		consultaStr+=" where 1=1 ";
		
		
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoHonorario()>0)?" and codigo_honorario= "+dtoWhere.getCodigoHonorario():"";
		consultaStr+= (dtoWhere.getEspecialidad().getCodigo() > 0)?" and especialidad= '"+dtoWhere.getEspecialidad().getCodigo()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModificada())?" and hora_modifica= '"+dtoWhere.getHoraModificada()+"' ":"";
		consultaStr+= (dtoWhere.getPorcentajeParticipacion()>-1)?" and porcentaje_participacion= "+dtoWhere.getPorcentajeParticipacion():"";
		consultaStr+= (dtoWhere.getGrupoServicio().getCodigo()>0)?" and grupo_servicio= "+dtoWhere.getGrupoServicio().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoWhere.getValorParticipacion()>-1)?" and valor_participacion= "+dtoWhere.getValorParticipacion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getTipoServicio().getCodigo())?" and tipo_servicio = '"+dtoWhere.getTipoServicio().getCodigo()+"' ":"";
		
		
		
		
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
			logger.error("ERROR EN actualizar el detalle honorario"+e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleAgrupacionHonorarios> cargar(DtoDetalleAgrupacionHonorarios dtoWhere) 
	{
		ArrayList<DtoDetalleAgrupacionHonorarios> arrayDto = new ArrayList<DtoDetalleAgrupacionHonorarios>();
		String consultaStr = 	"SELECT " +
										"codigo, " +
										"codigo_honorario, " +
										"especialidad, " +
										"administracion.getnombreespecialidad(especialidad ) as nombre_especialidad, " +										
										"fecha_modifica, " +
										"hora_modifica, " +
										"porcentaje_participacion, " +
										"historiaclinica.getnombregruposervicio(grupo_servicio) as nombre_grupo_servicio, " +
										"interfaz.getdesccuentascontables(cuenta_honorarios) as desc_cuenta_honorarios ,"+
                                     	"interfaz.getdesccuentascontables(cuenta_vig_ant_inst) as desc_cuenta_vig_ant_inst , "+
                                     	"interfaz.getdesccuentascontables(cuenta_ingreso_inst) as desc_cuenta_ingreso_inst , "+
										"grupo_servicio, "+
										"usuario_modifica, " +
										"valor_participacion,  " +	
										"tipo_servicio as tipo_servicio, " +	
										"historiaclinica.getnombretiposervicio(tipo_servicio) as nombre_tipo_servicio, " +
										"cuenta_honorarios," + 
                                    	"cuenta_ingreso_inst," + 			                                                                                          	 
                                     	"cuenta_vig_ant_inst " +
									"from " +
										"odontologia.det_agru_honora_esp_serv " + 
									"where " +
										"1=1 ";
										
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoHonorario()>0)?" and codigo_honorario= "+dtoWhere.getCodigoHonorario():"";
	    consultaStr+= (dtoWhere.getEspecialidad().getCodigo() > 0)?" and especialidad= "+dtoWhere.getEspecialidad().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModificada())?" and hora_modifica= '"+dtoWhere.getHoraModificada()+"' ":"";
		consultaStr+= (dtoWhere.getPorcentajeParticipacion()>0)?" and porcentaje_participacion= "+dtoWhere.getPorcentajeParticipacion():"";
	    consultaStr+= (dtoWhere.getGrupoServicio().getCodigo()>0)?" and grupo_servicio= "+dtoWhere.getGrupoServicio().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoWhere.getValorParticipacion()>0)?" and valor_participacion= "+dtoWhere.getValorParticipacion():"";
	    consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getTipoServicio().getCodigo())?" and tipo_servicio = '"+dtoWhere.getTipoServicio().getCodigo()+"' ":"";
	   
	    try 
		{
			logger.info("\n\n\n\n\n SQL cargar Detalle Honorario / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr+ " order by getnombregruposervicio(grupo_servicio), getnombretiposervicio(tipo_servicio)  ", ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoDetalleAgrupacionHonorarios dto = new DtoDetalleAgrupacionHonorarios();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setCodigoHonorario(rs.getDouble("codigo_honorario"));
				dto.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad"), rs.getString("nombre_especialidad")));
				dto.setFechaModificada(rs.getDate("fecha_modifica").toString());

				dto.setHoraModificada(rs.getString("hora_modifica"));
				dto.setPorcentajeParticipacion(rs.getDouble("porcentaje_participacion"));
				dto.setGrupoServicio(new InfoDatosInt(rs.getInt("grupo_servicio"),rs.getString("nombre_grupo_servicio")));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setValorParticipacion(rs.getDouble("valor_participacion"));
				dto.setTipoServicio(new InfoDatosStr(rs.getString("tipo_servicio"),rs.getString("nombre_tipo_servicio")));
                 
				DtoCuentaContable dtoCuenta1=new DtoCuentaContable();  
				
				dtoCuenta1.setCodigo(rs.getString("cuenta_honorarios"));
				dtoCuenta1.setDescripcion(rs.getString("desc_cuenta_honorarios"));
				
	             DtoCuentaContable dtoCuenta2=new DtoCuentaContable();  
				
				dtoCuenta2.setCodigo(rs.getString("cuenta_ingreso_inst"));
				dtoCuenta2.setDescripcion(rs.getString("desc_cuenta_ingreso_inst"));
				
                DtoCuentaContable dtoCuenta3=new DtoCuentaContable();  
				
				dtoCuenta3.setCodigo(rs.getString("cuenta_vig_ant_inst"));
				dtoCuenta3.setDescripcion(rs.getString("desc_cuenta_vig_ant_inst"));
				
				ArrayList<DtoCuentaContable> arrayCuentas = new ArrayList<DtoCuentaContable>();
				arrayCuentas.add(dtoCuenta1);
				arrayCuentas.add(dtoCuenta2);
				arrayCuentas.add(dtoCuenta3);				
				dto.setArrayCuentasContables(arrayCuentas);
				
               
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
	public static boolean eliminar(DtoDetalleAgrupacionHonorarios dtoWhere) 
	{
		String consultaStr = "DELETE FROM  odontologia.det_agru_honora_esp_serv WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoHonorario()>0)?" and codigo_honorario= "+dtoWhere.getCodigoHonorario():"";
		consultaStr+= (dtoWhere.getEspecialidad().getCodigo() > 0)?" and especialidad= "+dtoWhere.getEspecialidad().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModificada())?" and hora_modifica= '"+dtoWhere.getHoraModificada()+"' ":"";
		consultaStr+= (dtoWhere.getPorcentajeParticipacion()>0)?" and porcentaje_participacion= "+dtoWhere.getPorcentajeParticipacion():"";
		consultaStr+= (dtoWhere.getGrupoServicio().getCodigo()>0)?" and  grupo_servicio= "+dtoWhere.getGrupoServicio().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoWhere.getValorParticipacion()>0)?" and valor_participacion= "+dtoWhere.getValorParticipacion():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getTipoServicio().getCodigo())?" and tipo_servicio = '"+dtoWhere.getTipoServicio().getCodigo()+"' ":"";
		
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
			logger.error("ERROR en eliminar Detalle honorario "+e);
			
		}
		return false;
	}

	/**
	 * 
	 * @param codigoHonorario
	 * @param grupoServicio
	 * @param tipoServicio
	 * @param especialidad
	 * @return
	 */
	public static boolean existeDetalleAgrupacion(double codigoHonorario, int grupoServicio, String tipoServicio, int especialidad, double codigoNotIn)
	{
		boolean resultado=false;
		String consultaStr="SELECT codigo from odontologia.det_agru_honora_esp_serv where 1=1 ";
		consultaStr+= (codigoHonorario>0)?" and codigo_honorario= "+codigoHonorario:" and codigo_honorario is null ";
		consultaStr+= (especialidad > 0)?" and especialidad= "+especialidad:" and especialidad is null ";
		consultaStr+= (grupoServicio>0)?" and  grupo_servicio= "+grupoServicio:" and  grupo_servicio is null ";
		consultaStr+= !UtilidadTexto.isEmpty(tipoServicio)?" and tipo_servicio = '"+tipoServicio+"' ":" and tipo_servicio is null ";
	
		consultaStr+= (codigoNotIn>0)?" and  codigo<> "+codigoNotIn:"";
		
		logger.info("\n\n\nEXISTE DETAKLLE AGRUPACION? "+consultaStr+"\n\n\n\n\n");
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= null;
			resultado= ps.executeQuery().next();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR en eliminar Detalle honorario "+e);
			
		}
		logger.info("RETORNA---------->"+resultado);
		return resultado;
	}
	
}
