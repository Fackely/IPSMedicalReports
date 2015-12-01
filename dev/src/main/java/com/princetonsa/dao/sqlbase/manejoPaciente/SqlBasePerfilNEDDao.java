package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoCamposPerfilNed;
import com.princetonsa.dto.manejoPaciente.DtoPerfilNed;

/**
 * 
 * @author axioma
 *
 */
public class SqlBasePerfilNEDDao 
{

	
	/**
	 * 
	 */
	
	
	private static Logger logger = Logger.getLogger(SqlBasePerfilNEDDao.class);
	
	/**
	 * CADENA para insertar
	 */
	 



	 
	private static String inserccionStr = "INSERT INTO manejopaciente.perfil_ned (   " +
																					"codigo_pk , " +//1
																					"codigo_paciente , " +//2
																					"escala , " +//3
																					"escala_factor_prediccion , " +//4
																					"valor_total , " +//5
																					"institucion , " +//6
																					"fecha_modifica , " +//7
																					"fecha_registro , " +//8
																					"hora_modifica , " +//9
																					"hora_registro , " +//10
																					"usuario_modifica," +//11
																					"usuario_registro  ) " +//12
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
	 * 
	 * @param dto
	 * @return
	 */
	
	private static String inserccionCamposStr = "INSERT INTO manejopaciente.campos_perfil_ned (   " +
	
	
	"codigo_pk , " +//1
	"perfil_ned , " +//2
	"escala_campo_seccion , " +//3
	"valor , " +//4	
	"observaciones , " +//5	
	"fecha_modifica , " +//6	
	"hora_modifica , " +//7
	
	"usuario_modifica  ) " +//8
"values (" +
	"? , " +//1
	"? , " +//2
	"? , " +//3
	"? , " +//4
	"? , " +//5
	"? , " +//6
	"? , " +//7
	
	
	"? ) ";//8



/**
* 
* @param dto
* @return
*/
	public static double guardar(Connection con, DtoPerfilNed dto) 
	{
		
		
		logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"manejopaciente.seq_perfil_ned");
			dto.setCodigoPk(secuencia);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,inserccionStr);
			
			
			
		
			
			
		
		
			ps.setDouble(1, secuencia);				
		    ps.setDouble(2, dto.getCodigoPaciente());		    
		    ps.setDouble(3, dto.getEscala());
		    ps.setDouble(4, dto.getEscalaFactorPrediccion());
		    ps.setDouble(5, dto.getValorTotal());
		    ps.setDouble(6, dto.getInstitucion());
		    ps.setString(7, dto.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD());
		    ps.setString(8, dto.getDatosfechaUsuarioRegistro().getFechaModificaFromatoBD());
		    ps.setString(9, dto.getDatosfechaUsuarioModifica().getHoraModifica());
		    ps.setString(10, dto.getDatosfechaUsuarioRegistro().getHoraModifica());
		    ps.setString(11, dto.getDatosfechaUsuarioModifica().getUsuarioModifica());
		    ps.setString(12, dto.getDatosfechaUsuarioRegistro().getUsuarioModifica());
		    
		  
			
			
			logger.info(ps);
			if (ps.executeUpdate() > 0) 
			{
				ps.close();
				return dto.getCodigoPk();
			}
			ps.close();
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert Emi tar " + e + dto.getCodigoPk());
			
			
		}
		return dto.getCodigoPk();
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	
	public static double guardarCampo(Connection con, DtoCamposPerfilNed dto) 
	{
		
		
		logger.info(UtilidadLog.obtenerString(dto, true));
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		try 
		{
		
			secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con,"manejopaciente.seq_campos_perfil_ned");
			dto.setCodigoPk(secuencia);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(inserccionCamposStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			
			
		
			ps.setDouble(1, secuencia);				
		    ps.setDouble(2, dto.getCodigoPerfilNed());		    
		    ps.setDouble(3, dto.getEscalaCampoSeccion());
		  
		  
		    if(dto.getValor() != null){
		    ps.setBigDecimal(4, dto.getValor());
		    }else{
		    	ps.setNull(4, Types.NUMERIC);
		    }
		    
		    ps.setString(5, dto.getObservaciones());
		 
		    ps.setString(6, dto.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD());
		   
		    ps.setString(7, dto.getDatosfechaUsuarioModifica().getHoraModifica());
		  
		    ps.setString(8, dto.getDatosfechaUsuarioModifica().getUsuarioModifica());
		   
		    
		  
			
			
			logger.info(inserccionStr);
			if (ps.executeUpdate() > 0) 
			{
				ps.close();
				return dto.getCodigoPk();
			}
			ps.close();
		} 
		catch (SQLException e) 
		{
			
			
			logger.error("ERROR en insert PEr NED " + e + dto.getCodigoPk());
			
			
		}
		return dto.getCodigoPk();
	}
	
	
	
	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	
	public static boolean modificarCampos(Connection con, DtoCamposPerfilNed dtoNuevo, DtoCamposPerfilNed dtoWhere) 
	{
		
		
		
		
		
		boolean retorna=false;
		String consultaStr = "UPDATE manejopaciente.campos_perfil_ned  set codigo_pk=codigo_pk";
		
		consultaStr+= (dtoNuevo.getCodigoPerfilNed() > ConstantesBD.codigoNuncaValido)?" , perfil_ned= "+dtoNuevo.getCodigoPerfilNed():"";
		
		consultaStr+= (dtoNuevo.getEscalaCampoSeccion()> ConstantesBD.codigoNuncaValido)?" , escala_campo_seccion= "+dtoNuevo.getEscalaCampoSeccion():"";
		consultaStr+= (dtoNuevo.getValor() != null)?" , valor= "+dtoNuevo.getValor():" , valor = null ";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD()+"' ":"";
		
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica()+"' ":"";
		
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica()+"' ":"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getObservaciones())?" , observaciones= '"+dtoNuevo.getObservaciones()+"' ":"";
		
		
		
		
		
		consultaStr+=" where 1=1 ";
		consultaStr+= (dtoWhere.getCodigoPk()>ConstantesBD.codigoNuncaValido)?" and codigo_pk= "+dtoWhere.getCodigoPk():"";
		consultaStr+= (dtoWhere.getCodigoPerfilNed()>ConstantesBD.codigoNuncaValido)?" and perfil_ned= "+dtoWhere.getCodigoPerfilNed():"";
		consultaStr+= (dtoWhere.getEscalaCampoSeccion()>0)?" and escala_campo_seccion= "+dtoWhere.getEscalaCampoSeccion():"";
		
		
		try 
		{
			logger.info("modificar-->"+consultaStr);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			ps.close();
			
		} catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar perfil paciente (NED)" + e);
			
		}
		return retorna;
	}
	


	/**
	 * 
	 * @param DescuentoOdontologico
	 * @return
	 */
	public static boolean modificar(Connection con, DtoPerfilNed dtoNuevo, DtoPerfilNed dtoWhere) 
	{
		
		
		
		
		
		
		boolean retorna=false;
		String consultaStr = "UPDATE manejopaciente.perfil_ned  set codigo_pk=codigo_pk";
		
		consultaStr+= (dtoNuevo.getCodigoPaciente() > ConstantesBD.codigoNuncaValido)?" , codigo_paciente= "+dtoNuevo.getCodigoPaciente():"";
		consultaStr+= (dtoNuevo.getEscala() > ConstantesBD.codigoNuncaValido)?" , escala= "+dtoNuevo.getEscala():"";
		consultaStr+= (dtoNuevo.getEscalaFactorPrediccion()> ConstantesBD.codigoNuncaValido)?" , escala_factor_prediccion= "+dtoNuevo.getEscalaFactorPrediccion():"";
		consultaStr+= (dtoNuevo.getValorTotal()> ConstantesBD.codigoNuncaValido)?" , valor_total= "+dtoNuevo.getValorTotal():"";
		consultaStr+= (dtoNuevo.getInstitucion()> ConstantesBD.codigoNuncaValido)?" , institucion= "+dtoNuevo.getInstitucion():"";
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getFechaModificaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioRegistro().getFechaModifica())?" , fecha_registro= '"+dtoNuevo.getDatosfechaUsuarioRegistro().getFechaModificaFromatoBD()+"' ":"";
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getHoraModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioRegistro().getHoraModifica())?" , hora_registro= '"+dtoNuevo.getDatosfechaUsuarioRegistro().getHoraModifica()+"' ":"";
		
		
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getDatosfechaUsuarioModifica().getUsuarioModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getDatosfechaUsuarioRegistro().getUsuarioModifica())?" , usuario_registro= '"+dtoNuevo.getDatosfechaUsuarioRegistro().getUsuarioModifica()+"' ":"";
		
		
		
		
		consultaStr+=" where 1=1 ";
		
		consultaStr+= " and codigo_pk= "+dtoWhere.getCodigoPk();
		
		
		
		try 
		{
			logger.info("modificar-->"+consultaStr);
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0; 
			ps.close();
			
		} catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar perfil paciente (NED)" + e);
			
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoPerfilNed> cargar(DtoPerfilNed dtoWhere) 
	{
		
		
		
		
		
		ArrayList<DtoPerfilNed> arrayDto = new ArrayList<DtoPerfilNed>();
		
		String consultaStr = 	"SELECT " +
										"pn.codigo_pk as codigo_pk , " +
										"pn.codigo_paciente as codigo_paciente , "+
										"pn.escala as escala , " +
										"pn.escala_factor_prediccion as escala_factor_prediccion , "+
										"pn.valor_total as valor_total , "+
										"pn.institucion as institucion , "+
										"pn.fecha_modifica as fecha_modifica , "+
										"pn.fecha_registro as fecha_registro , "+
										"pn.hora_modifica as hora_modifica , "+
										"pn.hora_registro as hora_registro , "+
										"pn.usuario_modifica as usuario_modifica , "+
										"pn.usuario_registro as usuario_registro  "+
										
										
										
										
									"from " +
										"manejopaciente.perfil_ned  pn  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigoPk()>ConstantesBD.codigoNuncaValido)?" and pn.codigo_pk= "+dtoWhere.getCodigoPk():"";
		consultaStr+= (dtoWhere.getCodigoPaciente()>ConstantesBD.codigoNuncaValido)?" and pn.codigo_paciente= "+dtoWhere.getCodigoPaciente():"";
		consultaStr+= (dtoWhere.getInstitucion()>ConstantesBD.codigoNuncaValido)?" and pn.institucion= "+dtoWhere.getInstitucion():"";
	
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoPerfilNed dto = new DtoPerfilNed();
				dto.setCodigoPk(rs.getDouble("codigo_pk"));
				dto.setCodigoPaciente(rs.getDouble("codigo_paciente"));
				dto.setEscala(rs.getDouble("escala"));
				dto.setEscalaFactorPrediccion(rs.getDouble("escala_factor_prediccion"));
				dto.setValorTotal(rs.getDouble("valor_total"));
				dto.setInstitucion(rs.getInt("institucion"));
			
			
				dto.getDatosfechaUsuarioModifica().setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.getDatosfechaUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
				
				dto.getDatosfechaUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
				
				dto.getDatosfechaUsuarioRegistro().setFechaModifica(rs.getDate("fecha_registro").toString());
				dto.getDatosfechaUsuarioRegistro().setHoraModifica(rs.getString("hora_registro"));
				
				dto.getDatosfechaUsuarioRegistro().setUsuarioModifica(rs.getString("usuario_registro"));
				
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga per (NED)==> " + e);
		}
		return arrayDto;
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoCamposPerfilNed> cargarCampos(DtoCamposPerfilNed dtoWhere) 
	{
		
		
		
		
		
		ArrayList<DtoCamposPerfilNed> arrayDto = new ArrayList<DtoCamposPerfilNed>();
		

		
		String consultaStr = 	"SELECT " +
										"cpn.codigo_pk as codigo_pk , " +
										"cpn.perfil_ned as perfil_ned , "+
										"cpn.escala_campo_seccion as escala_campo_seccion , " +
										
										"cpn.valor as valor , "+
										
										"cpn.fecha_modifica as fecha_modifica , "+
									
										"cpn.hora_modifica as hora_modifica , "+
										
										"cpn.usuario_modifica as usuario_modifica,  "+
										
										"cpn.observaciones as observaciones  "+
										
										
										
										
										
									"from " +
										"manejopaciente.campos_perfil_ned  cpn  " +
									"where " +
										"1=1 ";
										
									
		consultaStr+= (dtoWhere.getCodigoPk()>ConstantesBD.codigoNuncaValido)?" and cpn.codigo_pk= "+dtoWhere.getCodigoPk():"";
		consultaStr+= (dtoWhere.getCodigoPerfilNed()>ConstantesBD.codigoNuncaValido)?" and cpn.perfil_ned= "+dtoWhere.getCodigoPerfilNed():"";
		
	
		try 
		{
			logger.info("\n\n\n\n\n SQL cargar emison Tar odontologico / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=null;
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next()) 
			{
				DtoCamposPerfilNed dto = new DtoCamposPerfilNed();
				dto.setCodigoPk(rs.getDouble("codigo_pk"));
				dto.setCodigoPerfilNed(rs.getDouble("perfil_ned"));
				dto.setEscalaCampoSeccion(rs.getDouble("escala_campo_seccion"));
				
				dto.setValor(rs.getBigDecimal("valor"));
				
			dto.setObservaciones(rs.getString("observaciones"));
			
				dto.getDatosfechaUsuarioModifica().setFechaModifica(rs.getDate("fecha_modifica").toString());
				dto.getDatosfechaUsuarioModifica().setHoraModifica(rs.getString("hora_modifica"));
				
				dto.getDatosfechaUsuarioModifica().setUsuarioModifica(rs.getString("usuario_modifica"));
				
				
				
				
				arrayDto.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga per (NED)==> " + e);
		}
		return arrayDto;
	}
	
	/**
	 * 
	 * @param codigoPkPerfil
	 * @return
	 */
	public static InfoDatosDouble cargarTotalesFactoresPrediccion(double codigoPkPerfil)
	{
		InfoDatosDouble info= new InfoDatosDouble();
		String consultaStr=" SELECT " +
								"pn.valor_total as valor_total,  " +
								"efp.nombre as nombre " +
							"FROM " +
								"manejopaciente.perfil_ned pn " +
								"INNER JOIN historiaclinica.escalas_factores_pred efp ON(efp.codigo_pk = pn.escala_factor_prediccion) " +
							"where " +
								"pn.codigo_pk="+codigoPkPerfil;
		
		try 
		{
			logger.info("\n\n\n\n\n totales escala / " + consultaStr);
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(consultaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			if (rs.next()) 
			{
				info.setCodigo(rs.getDouble(1));
				info.setNombre(rs.getString(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		} 
		catch (SQLException e) 
		{
			logger.error("error en carga per (NED)==> " + e);
		}
		return info;
	}
}
