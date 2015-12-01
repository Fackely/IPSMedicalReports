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
import util.facturacion.InfoTarifaVigente;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;
import com.princetonsa.dto.odontologia.DtoServicioHonorarios;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.odontologia.ServicioHonorario;
import com.servinte.axioma.fwk.exception.IPSException;


public class SqlBaseDetalleServicioHonorarioDao {
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseDetalleServicioHonorarioDao.class);
	
	
	/**
	 * CADENA para insertar
	 */
	private static String inserccionStr = " INSERT INTO odontologia.det_serv_honora_esp_serv( " +
																										"codigo , " + //1
            																			                "codigo_honorario ," + //2
																								         "especialidad , "+ // 3 
			                                                                                             "fecha_modifica ,"+ //4 
			                                                                                          	  "hora_modifica ,"+ //5
			                                                                                          	  "porcentaje_participacion ,"+//6
			                                                                                          	  "servicio, "+ //7
			                                                                                          	   "usuario_modifica,"+//8
			                                                                                          	   "valor_participacion," +//9
			                                                                                          	 "cuenta_honorarios," + //10
			                                                                                          	  "cuenta_ingreso_inst," + //11			                                                                                          	 
			                                                                                           	"cuenta_vig_ant_inst)" +//12
			                                                                                          	   "values ("+
			                                                                                          	    "? ,"+//1
			                                                                                          	    "?, " + //2
			                                                                                          	   " ? ,"+ //3
			                                                                                          	   " ? ," + //4
			                                                                                          	   " ? , "+ //5
			                                                                                          	   " ? ,"+ //6
			                                                                                                "? , "+ //7
			                                                                                                "? , "+//8
			                                                                                                "? , "+//9
			                                                                                                "? , "+//10
			                                                                                                "? , "+//11
			                                                                                                "? ) ";//12;
	
	
	
	/**
	 * 
	 * 
	 */
	public static double guardar(DtoDetalleServicioHonorarios dto) 
	{
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_det_serv_hon_esp_ser");
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, secuencia);
			ps.setDouble(2,dto.getCodigoHonorario());
			
			if(dto.getEspecialidad().getCodigo()>0)
			{	
				ps.setInt(3, dto.getEspecialidad().getCodigo());
			}
			else
			{
				ps.setNull(3, Types.INTEGER);
			}
			ps.setDate(4, Date.valueOf(dto.getFechaModificaFromatoBD()));

			ps.setString(5, dto.getHoraModifica());
			
			if(dto.getPorcentajeParticipacion()>ConstantesBD.codigoNuncaValidoDouble) 
			{	
				ps.setDouble(6, dto.getPorcentajeParticipacion());
			}
			else
			{
				ps.setNull(6, Types.DOUBLE);
			}
			
			if(dto.getServicio().getCodigo()>ConstantesBD.codigoNuncaValidoDouble)
			{	
				ps.setInt(7, dto.getServicio().getCodigo());
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
			
			if(!UtilidadTexto.isEmpty(dto.getArrayCuentasContables().get(0).getCodigo()))
			{			
				ps.setString(10, dto.getArrayCuentasContables().get(0).getCodigo());
			}
			else
			{
				ps.setNull(10, Types.VARCHAR );
			}
			if(!UtilidadTexto.isEmpty(dto.getArrayCuentasContables().get(1).getCodigo()))
			{	
				ps.setString(11, dto.getArrayCuentasContables().get(1).getCodigo());
			}
			else
			{
				ps.setNull(11, Types.VARCHAR );
			}
			if(!UtilidadTexto.isEmpty(dto.getArrayCuentasContables().get(2).getCodigo()))
			{
				ps.setString(12, dto.getArrayCuentasContables().get(2).getCodigo());
			}
			else
			{
				ps.setNull(12, Types.VARCHAR );
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
	public static boolean modificar(DtoDetalleServicioHonorarios dtoNuevo, DtoDetalleServicioHonorarios dtoWhere , boolean siVacioUpdateNull) 
	{
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.det_serv_honora_esp_serv set codigo=codigo ";
					
		consultaStr+= (dtoNuevo.getCodigoHonorario()>0)?" , codigo_honorario= "+dtoNuevo.getCodigoHonorario():"";
		consultaStr+= (dtoNuevo.getEspecialidad().getCodigo() > 0)?" , especialidad= '"+dtoNuevo.getEspecialidad().getCodigo()+"' ":( (siVacioUpdateNull)?" , especialidad= null ":"" );
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModificaFromatoBD())?" , fecha_modifica= '"+dtoNuevo.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		consultaStr+= (dtoNuevo.getPorcentajeParticipacion()>-1)?" , porcentaje_participacion= "+dtoNuevo.getPorcentajeParticipacion(): ( (siVacioUpdateNull)?" , porcentaje_participacion= null ":"" );
		consultaStr+= (dtoNuevo.getServicio().getCodigo()>0)?" , servicio= "+dtoNuevo.getServicio().getCodigo(): ( (siVacioUpdateNull)?" , servicio= null ":"" );
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoNuevo.getValorParticipacion()>-1)?" , valor_participacion= "+dtoNuevo.getValorParticipacion(): ( (siVacioUpdateNull)?" , valor_participacion= null ":"");
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getArrayCuentasContables().get(0).getCodigo())?" , cuenta_honorarios= '"+dtoNuevo.getArrayCuentasContables().get(0).getCodigo()+"' ":( (siVacioUpdateNull)?" , cuenta_honorarios= null ":"");
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getArrayCuentasContables().get(1).getCodigo())?" , cuenta_ingreso_inst= '"+dtoNuevo.getArrayCuentasContables().get(1).getCodigo()+"' ":( (siVacioUpdateNull)?" , cuenta_ingreso_inst= null ":"");
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getArrayCuentasContables().get(2).getCodigo())?" ,  cuenta_vig_ant_inst= '"+dtoNuevo.getArrayCuentasContables().get(2).getCodigo()+"' ":( (siVacioUpdateNull)?" , cuenta_vig_ant_inst= null ":"") ;
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoHonorario()>0)?" and codigo_honorario= "+dtoWhere.getCodigoHonorario():"";
		consultaStr+= (dtoWhere.getEspecialidad().getCodigo() > 0)?" and especialidad= '"+dtoWhere.getEspecialidad().getCodigo()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getPorcentajeParticipacion()>-1)?" and porcentaje_participacion= "+dtoWhere.getPorcentajeParticipacion():"";
		consultaStr+= (dtoWhere.getServicio().getCodigo()>0)?" ,and servicio= "+dtoWhere.getServicio().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoWhere.getValorParticipacion()>-1)?" and valor_participacion= "+dtoWhere.getValorParticipacion():"";
		
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
			logger.error("ERROR EN actualizar el detalle honorario" + e);
			
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleServicioHonorarios> cargar(DtoDetalleServicioHonorarios dtoWhere) throws IPSException
	{
		
		Connection conTemporal = UtilidadBD.abrirConexion();
		int codigoEsquemaTarifario= obtenerEsquemaTarifarioHonorarios(dtoWhere.getCodigoHonorario());
		int codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(conTemporal, codigoEsquemaTarifario);
		
		ArrayList<DtoDetalleServicioHonorarios> arrayDto = new ArrayList<DtoDetalleServicioHonorarios>();
		String consultaStr = 	"SELECT " +
										"codigo, " +
										"codigo_honorario, " +
										"especialidad, " +
										"administracion.getnombreespecialidad(especialidad) as nombre_especialidad , " +
										"facturacion.getcodigopropservicio(servicio, "+codigoTipoTarifario+")||' - '|| getnombreservicio(servicio,"+codigoTipoTarifario+")"+" as nombre_servicio , " +
										"interfaz.getdesccuentascontables(cuenta_honorarios) as desc_cuenta_honorarios ,"+
                                     	"interfaz.getdesccuentascontables(cuenta_vig_ant_inst) as desc_cuenta_vig_ant_inst , "+
                                     	"interfaz.getdesccuentascontables(cuenta_ingreso_inst) as desc_cuenta_ingreso_inst , "+
										"fecha_modifica, " +
										"hora_modifica, " +
										"porcentaje_participacion, " +
										"servicio, "+
										"usuario_modifica ," +
										"valor_participacion,  " +
										"cuenta_honorarios," + 
                                    	"cuenta_ingreso_inst," + 			                                                                                          	 
                                     	"cuenta_vig_ant_inst " +
                                     	
                                     	
									"from " +
										"odontologia.det_serv_honora_esp_serv " +
									"where " +
										"1=1";
										
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
	    consultaStr+= (dtoWhere.getCodigoHonorario()>0)?" and codigo_honorario= "+dtoWhere.getCodigoHonorario():"";
	    consultaStr+= (dtoWhere.getEspecialidad().getCodigo() > 0)?" and especialidad= "+dtoWhere.getEspecialidad().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getPorcentajeParticipacion()>0)?" and porcentaje_participacion= "+dtoWhere.getPorcentajeParticipacion():"";
	    consultaStr+= (dtoWhere.getServicio().getCodigo()>0)?" and servicio= "+dtoWhere.getServicio().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoWhere.getValorParticipacion()>0)?" and valor_participacion= "+dtoWhere.getValorParticipacion():"";
		consultaStr+= " order by  servicio asc";
		UtilidadBD.closeConnection(conTemporal)	;	
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar Detalle Honorario / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoDetalleServicioHonorarios dto = new DtoDetalleServicioHonorarios();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setCodigoHonorario(rs.getDouble("codigo_honorario"));
				dto.setEspecialidad(new InfoDatosInt(rs.getInt("especialidad"), rs.getString("nombre_especialidad")));
				dto.setFechaModifica(rs.getDate("fecha_modifica").toString());

				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setPorcentajeParticipacion(rs.getDouble("porcentaje_participacion"));
				dto.setServicio(new InfoDatosInt(rs.getInt("servicio"),rs.getString("nombre_servicio")));
				dto.setUsuarioModifica(rs.getString("usuario_modifica"));
				dto.setValorParticipacion(rs.getDouble("valor_participacion"));
				
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

				codigoEsquemaTarifario= obtenerEsquemaTarifarioHonorarios(dto.getCodigoHonorario());
			    codigoTipoTarifario= EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
			    InfoTarifaVigente infoTarifa= Cargos.obtenerTarifaBaseServicio(con, codigoTipoTarifario, dto.getServicio().getCodigo(), codigoEsquemaTarifario, "");
				dto.setTarifa(infoTarifa.getValorTarifa());
				dto.setFechaVigenciaTarifa(infoTarifa.getFechaVigencia());
               
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
	 * @param codigoHonorario
	 * @return
	 */
	private static int obtenerEsquemaTarifarioHonorarios(double codigoHonorario)
	{
		int esquemaTarifario=ConstantesBD.codigoNuncaValido;
		DtoServicioHonorarios dto= new DtoServicioHonorarios();
		dto.setCodigo(codigoHonorario);
		ArrayList<DtoServicioHonorarios> array= ServicioHonorario.cargar(dto);
		esquemaTarifario=(array.size()>0)?array.get(0).getEsquemaTarifario().getCodigo():ConstantesBD.codigoNuncaValido;
		return esquemaTarifario;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static boolean eliminar(DtoDetalleServicioHonorarios dtoWhere) 
	{
		String consultaStr = "DELETE FROM odontologia.det_serv_honora_esp_serv WHERE 1=1 ";
		
		consultaStr+= (dtoWhere.getCodigo()>0)?" and codigo= "+dtoWhere.getCodigo():"";
		consultaStr+= (dtoWhere.getCodigoHonorario()>0)?" and codigo_honorario= "+dtoWhere.getCodigoHonorario():"";
		consultaStr+= (dtoWhere.getEspecialidad().getCodigo() > 0)?" and especialidad= "+dtoWhere.getEspecialidad().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getFechaModificaFromatoBD())?" and fecha_modifica= '"+dtoWhere.getFechaModificaFromatoBD()+"' ":"";		
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getHoraModifica())?" and hora_modifica= '"+dtoWhere.getHoraModifica()+"' ":"";
		consultaStr+= (dtoWhere.getPorcentajeParticipacion()>0)?" and porcentaje_participacion= "+dtoWhere.getPorcentajeParticipacion():"";
		consultaStr+= (dtoWhere.getServicio().getCodigo()>0)?" and servicio= "+dtoWhere.getServicio().getCodigo():"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoWhere.getUsuarioModifica())?" and usuario_modifica= '"+dtoWhere.getUsuarioModifica()+"' ":"";
		consultaStr+= (dtoWhere.getValorParticipacion()>0)?" and valor_participacion= "+dtoWhere.getValorParticipacion():"";
		
		
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
			logger.error("ERROR en eliminar Detalle honorario "  + e);
			
		}
		return false;
	}
	
	
	 
		public static boolean existeDetalleServicio(double codigoHonorario, int servicio , int especialidad, double codigoNotIn)
		{
			boolean resultado=false;
			String consultaStr="SELECT codigo from odontologia.det_serv_honora_esp_serv where 1=1 ";
			consultaStr+= (codigoHonorario>0)?" and codigo_honorario= "+codigoHonorario:" and codigo_honorario is null ";
			consultaStr+= (especialidad > 0)?" and especialidad= "+especialidad:" and especialidad is null ";
			consultaStr+= (servicio > 0)?" and  servicio= "+servicio:" and  servicio is null ";
			
			consultaStr+= (codigoNotIn>0)?" and  codigo<> "+codigoNotIn:"";
			
			logger.info("\n\n\nEXISTE DETALLE SERVICIO? "+consultaStr+"\n\n\n\n\n");
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
