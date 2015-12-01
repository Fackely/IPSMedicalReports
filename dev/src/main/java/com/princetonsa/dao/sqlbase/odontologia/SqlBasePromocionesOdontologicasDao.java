package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.odontologia.InfoPromocionPresupuestoServPrograma;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetCaPromocionesOdo;
import com.princetonsa.dto.odontologia.DtoDetConvPromocionesOdo;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas ;
import com.princetonsa.mundo.odontologia.DetCaPromocionesOdo;
import com.princetonsa.mundo.odontologia.DetConvPromocionesOdo;



public class SqlBasePromocionesOdontologicasDao {


	

	private static Logger logger = Logger.getLogger(SqlBasePromocionesOdontologicasDao.class);
	
	private static String InsertarStr=" insert into odontologia.promociones_odontologicas(	codigo_pk ," + //1
																							"nombre ," + //2
																							"fecha_inicial_vigencia , " + //3
																							"fecha_final_vigencia , " + //4
																							"hora_inicial_vigencia ," + //5
																							"hora_final_vigencia ," +  //6
																							"activo ," + //7
																							"fecha_modifica ," + //8
																							"hora_modifica ," + //9
																							" usuario_modifica," +
																							" fecha_generacion," +
																							" institucion )" + //10
																							"values (? , ?, ? , ?, ?, ? ,? , ? ,? , ?, ?, ?)"; //10 
	
	

	private static String InsertarStr2=" insert into odontologia.log_promociones_odontologicas(	codigo_pk ," + //1
	"nombre ," + //2
	"fecha_inicial_vigencia , " + //3
	"fecha_final_vigencia , " + //4
	"hora_inicial_vigencia ," + //5
	"hora_final_vigencia ," +  //6
	"activo ," + //7
	"fecha_modifica ," + //8
	"hora_modifica ," + //9
	" usuario_modifica," + //10
	"promocion_odontologica" + //11
	" )" + 
	
	"values (? , ? , ? , ?, ? , ? , ? , ? ,? , ? , ?)"; //11

	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoPromocionesOdontologicas  dto)
	{
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		
		logger.info("Guadar Informacion-------------");
		logger.info(InsertarStr);
		logger.info(UtilidadLog.obtenerString(dto, true));
	 	
		try 
		{
		//Objetos de Conexion
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_promociones_odonto"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(InsertarStr ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("codigo=="+secuencia);
				
			ps.setDouble(1,secuencia);
			ps.setString(2,dto.getNombre());
			ps.setString(3,dto.getFechaVigenciaInicialFormatoBD());
			ps.setString(4,dto.getFechaVigenciaFinalFormatoBD());
		
			if(UtilidadTexto.isEmpty(dto.getHoraInicialVigencia()))
			{
				ps.setNull(5, Types.VARCHAR);
			}
			else
			{
				ps.setString(5,dto.getHoraInicialVigencia());
			}
		
			if(UtilidadTexto.isEmpty(dto.getHoraFinalVigencia()))
			{
				ps.setNull(6,Types.VARCHAR);
			}
			else
			{
				ps.setString(6,dto.getHoraFinalVigencia());
			}	
			ps.setString(7,dto.getActivo());
			ps.setString(8,dto.getFechaModificaBD());
			ps.setString(9,dto.getHoraModificada());
			ps.setString(10,dto.getUsuarioModifica());
			
		
			ps.setString(11, dto.getFechaGeneracionBD());
			ps.setInt(12, dto.getInstitucion());
			
			logger.info("inser promocion "+ps);
			
			if(ps.executeUpdate()>0)
				{
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
					return secuencia;
				}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		
		return secuencia;
		
	}
	
	
	public static double guardarLog(DtoPromocionesOdontologicas  dto)
	{
		
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		
		logger.info("Guadar Informacion-------------");
		logger.info(InsertarStr2);
		logger.info(UtilidadLog.obtenerString(dto, true));
	 	
		try 
		{
		//Objetos de Conexion
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.log_seq_promociones_odonto"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,InsertarStr2 );
			logger.info("codigo=="+secuencia);
				
			ps.setDouble(1,secuencia);
			ps.setString(2,dto.getNombre());
			ps.setString(3,dto.getFechaVigenciaInicialFormatoBD());
			ps.setString(4,dto.getFechaVigenciaFinalFormatoBD());
		
			if(UtilidadTexto.isEmpty(dto.getHoraInicialVigencia()))
			{
				ps.setNull(5, Types.VARCHAR);
			}
			else
			{
				ps.setString(5,dto.getHoraInicialVigencia());
			}
		
			if(UtilidadTexto.isEmpty(dto.getHoraFinalVigencia()))
			{
				ps.setNull(6,Types.VARCHAR);
			}
			else
			{
				ps.setString(6,dto.getHoraFinalVigencia());
			}	
			ps.setString(7,dto.getActivo());
			ps.setString(8,dto.getFechaModificaBD());
			ps.setString(9,dto.getHoraModificada());
			ps.setString(10,dto.getUsuarioModifica());
			ps.setInt(11, dto.getPromocionOdontologica());
			
			
			logger.info("LOG PROMOCION ODONTOLOGICA :::::  \n\n\n\n"+ps+"\n\n\n\n");
			
			if(ps.executeUpdate()>0)
				{
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
					return secuencia;
				}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		
		return secuencia;
		
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	
	public static ArrayList<DtoPromocionesOdontologicas> cargar( DtoPromocionesOdontologicas dto){
		ArrayList<DtoPromocionesOdontologicas> arrayDto= new ArrayList<DtoPromocionesOdontologicas>();
		String consultaStr=" select  codigo_pk as codigoPK," + //1
									"nombre as nombre," + //2
									"to_char(po.fecha_inicial_vigencia, 'DD/MM/YYYY') as fechaInicialVigencia , " + //3
									"to_char(po.fecha_final_vigencia, 'DD/MM/YYYY') as fechaFinalVigencia, " + //4
									"hora_inicial_vigencia as horaInicialVigencia ," + //5
									"hora_final_vigencia as horaFinalVigencia," +  //6
									"activo as activo," + //7
									"fecha_modifica as fechaModificada," + //8
									"hora_modifica as horaModifica ," + //9
									"usuario_modifica as usuarioModifica, " +
									"CASE WHEN ( select count(0) from odontologia.det_promociones_odo dtp  where dtp.promocion_odontologia=po.codigo_pk)>0 then 'S' else 'N' end as existeDetalle " + //10
									"from odontologia.promociones_odontologicas po " +
									"where " +
									"1=1 ";
	 	
		consultaStr+= dto.getCodigoPk() > 0 ? " AND codigo_pk= "+dto.getCodigoPk() : "";
		consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?" ":" AND nombre='"+dto.getNombre()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModificada())?" ":" AND fecha_modifica='"+dto.getFechaModificada()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?" ":" AND hora_modifica='"+dto.getHoraModificada()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND  usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaInicialVigencia())?" ":" AND fecha_inicial_vigencia="+dto.getFechaInicialVigencia();
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaFinalVigencia())?" ":" AND fecha_final_vigencia="+dto.getFechaFinalVigencia();
	   
	    
	    logger.info("\n\n\n\n\n SQL cargarPromociones Odontologicas/ "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by fecha_inicial_vigencia desc",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoPromocionesOdontologicas newdto = new DtoPromocionesOdontologicas();
				newdto.setCodigoPk(rs.getInt("codigoPk"));
				newdto.setNombre(rs.getString("nombre"));
				newdto.setFechaInicialVigencia(rs.getString("fechaInicialVigencia"));
				newdto.setFechaFinalVigencia(rs.getString("fechaFinalVigencia"));
				newdto.setHoraInicialVigencia(rs.getString("horaInicialVigencia"));
				newdto.setHoraFinalVigencia(rs.getString("horaFinalVigencia"));
				newdto.setFechaModificada(rs.getString("fechaModificada"));
				newdto.setHoraModificada(rs.getString("horaModifica"));
				newdto.setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.setActivo(rs.getString("activo")); 
				newdto.setExisteDetalle(rs.getString("existeDetalle"));
				arrayDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}
	
	
/**
 * 
 * @param dto
 * @return
 */
	public static boolean modificar( DtoPromocionesOdontologicas dto )
	{
		boolean retorna=false;
		String consultaStr ="UPDATE odontologia.promociones_odontologicas set codigo_pk=codigo_pk ";

		consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?" ":" , nombre='"+dto.getNombre()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaInicialVigencia())?" ":" , fecha_inicial_vigencia='"+dto.getFechaVigenciaInicialFormatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaFinalVigencia())?" ":" , fecha_final_vigencia='"+dto.getFechaVigenciaFinalFormatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getHoraInicialVigencia())?" ":" , hora_inicial_vigencia='"+dto.getHoraInicialVigencia() +"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getHoraFinalVigencia())?" ":" , hora_final_vigencia='"+dto.getHoraFinalVigencia()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getActivo())?" ":" , activo='"+dto.getActivo()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModificada())?" ":" ,fecha_modifica='"+dto.getFechaModificada()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?" ":" , hora_modifica='"+dto.getHoraModificada()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" ,  usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getCodigoPk()> 0)?" where codigo_pk= "+ dto.getCodigoPk():"";
	    logger.info("\n\n\n\n Update \n"+consultaStr+"\n");

	    
	    try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updatePrograma "+e);
		}
		return retorna;
			
	}
   
	/**
	 * 
	 * @param dto
	 * @return
	 */
public static boolean eliminar( DtoPromocionesOdontologicas dto){
		
		String consultaStr="DELETE FROM  odontologia.promociones_odontologicas WHERE 1=1 ";
		

		consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?" ":" AND nombre='"+dto.getNombre()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModificada())?" ":" AND fecha_modifica='"+dto.getFechaModificada()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?" ":" AND hora_modifica='"+dto.getHoraModificada()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND  usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getCodigoPk()> 0)?" AND codigo_pk= "+ dto.getCodigoPk():"";
	    logger.info(consultaStr);
	    
		    
	    try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN eliminar Programas  ");
			e.printStackTrace();
		}
			return false;
		}
	

		/**
		 * 
		 */

		public static boolean existeCruceFechas(DtoPromocionesOdontologicas  dto, double codigoPkNotIn)
		{

			
			String fechaInicial= dto.getFechaVigenciaInicialFormatoBD()+" "+( dto.getHoraInicialVigencia().equals("") ? "00:00" : dto.getHoraInicialVigencia());
			String fechaFinal=   dto.getFechaVigenciaFinalFormatoBD()+" "+(dto.getHoraFinalVigencia().equals("")?"23:59":dto.getHoraFinalVigencia());
			
			
			
				/*			
				"SELECT codigo_pk FROM odontologia.promociones_odontologicas" +
							" WHERE  nombre=? and " +
							
						
							
							"	('"+fechaInicial+"' " +
										"between " +
													" to_char(fecha_inicial_vigencia, '"+ConstantesBD.formatoFechaBD+"') || ' ' || coalesce(hora_inicial_vigencia,'00:00') " +
												" and " +
													" to_char(fecha_final_vigencia, '"+ConstantesBD.formatoFechaBD+"') || ' ' || coalesce(hora_final_vigencia,'23:59')  " +
								" or " +
									"('"+fechaFinal+"' " +
										"between " +
													"to_char(fecha_inicial_vigencia, 'YYYY-MM-DD')  || ' ' || coalesce(hora_inicial_vigencia,'00:00') " +
												"and " +
												"	to_char(fecha_final_vigencia, 'YYYY-MM-DD') || ' ' || coalesce(hora_final_vigencia,'23:59') ) )"+
								
												" or"+
													"(to_char(fecha_inicial_vigencia, 'YYYY-MM-DD')  || ' ' || coalesce(hora_inicial_vigencia,'00:00')  " +
															"between " +
																		"'"+fechaInicial+
																	"and " +
																		"'"+fechaFinal+"')"+ 
								 "or"+
												 	"(to_char(fecha_final_vigencia, 'YYYY-MM-DD')  || ' ' || coalesce(hora_inicial_vigencia,'00:00') " +
												 			" between '"+fechaInicial+"'" +
												 						" and " +
												 						"'"+fechaFinal+"')"; 
			
			
						*/
			
			String consultaStr=	"SELECT "	+ 
								"codigo_pk FROM odontologia.promociones_odontologicas WHERE  nombre=? and " +
								"("	+
								" ( "	+
								" ( "	+
								" '"+fechaInicial+"'"	+ 
					
									" between  to_char(fecha_inicial_vigencia, 'YYYY-MM-DD') || ' ' || coalesce(hora_inicial_vigencia,'00:00')"	+  
											" and "	+  	
												"to_char(fecha_final_vigencia, 'YYYY-MM-DD') || ' ' || coalesce(hora_final_vigencia,'23:59')"+   
								" ) "+
								" or "	+
								" ('"+fechaFinal+"'"	+ 
					
								" between to_char(fecha_inicial_vigencia, 'YYYY-MM-DD')  || ' ' || coalesce(hora_inicial_vigencia,'00:00')"	+
								" 	and 	"	+
								" 	to_char(fecha_final_vigencia, 'YYYY-MM-DD') || ' ' || coalesce(hora_final_vigencia,'23:59') )"	+ 
								" ) "	+
					
								" or   "	+  
								" ( "	+
								" to_char(fecha_inicial_vigencia, 'YYYY-MM-DD')  || ' ' || coalesce(hora_inicial_vigencia,'00:00') "+  
					
								" between '"+fechaInicial+"' and '"+fechaFinal+"' "+ 
								" ) "+
					
								" or ("+
								" to_char(fecha_final_vigencia, 'YYYY-MM-DD')  || ' ' || coalesce(hora_final_vigencia,'23:59')  " +
								
								" between '"+fechaInicial+"' and '"+fechaFinal+"') )"; 

			
			
			if(codigoPkNotIn>0)
			{
				consultaStr+=" and codigo_pk <> "+codigoPkNotIn;
			}
			
			logger.info("\n\n\n\n\n CONSULTA PROMOCIONES ODONTOLOGICAS \n\n "+consultaStr+"\n\n\n\n");
			
			boolean retorna=false;
			try 
		    {
				Connection con = UtilidadBD.abrirConexion();
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
				ps.setString(1, dto.getNombre());
				
				logger.info(" VALIDACION EXISTEN CRUECES DE FECHA \n\n\n\n "+ps+" \n\n\n\n ");
				
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

		/**
		 * Metodo para obtener los desucnetos de promociones para aplicarlos a los calculos de los cargos de odontologia
		 * 
		 * key= fechabd, hora, programa, servicio, regionCentroAntencion, paisCentroAtencion, ciudadCentroAtencion
		 * deptoCentroAtencion, centroAtencion, convenio, edad, sexo, estadocivil, numeroHijos, ocupacion
		 * fechainiciopromocion
		 * 
		 * 
		 * @param vo
		 * @return
		 */
		public static ArrayList<InfoPromocionPresupuestoServPrograma> obtenerValorDescuentoPromociones(HashMap<String, String> vo)
		{
			ArrayList<InfoPromocionPresupuestoServPrograma> array= new ArrayList<InfoPromocionPresupuestoServPrograma>();
			String consultaStr= "SELECT " +
									"dpo.codigo_pk as codigopkdetalle, " +
									"coalesce(dpo.porcentaje_descuento, "+ConstantesBD.codigoNuncaValido+") as porcentaje, " +
									"coalesce(dpo.valor_descuento, "+ConstantesBD.codigoNuncaValido+") as valor, " +
									"po.nombre as nombre, " +
									"coalesce(dpo.porcentaje_honorario, "+ConstantesBD.codigoNuncaValido+") as porcentajehonorario, " +
									"coalesce(dpo.valor_honorario, "+ConstantesBD.codigoNuncaValido+") as valorhonorario " +
									// "dpo.requiere_pago_inicial as requierepago, " +
									//"coalesce(dpo.dias_max_pago_inicial) as dias_max_pago_inicial, " +
								//	"coalesce(dpo.porcentaje_pago_inicial) as porcentaje_pago_inicial "+
								"FROM " +
									"odontologia.promociones_odontologicas po " +
									"INNER JOIN odontologia.det_promociones_odo dpo ON(dpo.promocion_odontologia = po.codigo_pk) " +
									"LEFT OUTER JOIN odontologia.det_conv_promociones_odo dcpo ON (dpo.codigo_pk= dcpo.det_promocion_odo and dcpo.activo='"+ConstantesBD.acronimoSi+"') " +
									"LEFT OUTER JOIN odontologia.det_ca_promociones_odo dcapo ON(dcapo.det_promocion_odo= dpo.codigo_pk and dcapo.activo='"+ConstantesBD.acronimoSi+"') " +
								"WHERE " +
									"po.activo='"+ConstantesBD.acronimoSi+"' " +
									"AND ('"+vo.get("fechabd")+"' between po.fecha_inicial_vigencia and po.fecha_final_vigencia) ";
			
			if(vo.containsKey("hora"))						
				consultaStr+= " AND ('"+vo.get("hora")+"' BETWEEN coalesce(po.hora_inicial_vigencia||'', '00:00') and coalesce(po.hora_final_vigencia||'', '24:00')) ";
			
			
			if(vo.containsKey("programa"))
				consultaStr+=		"AND dpo.programa_odontologico="+vo.get("programa")+" ";
			else
				consultaStr+=		"AND dpo.servicio="+vo.get("servicio")+" ";
									
			consultaStr+=			"AND (dpo.region_centro_atencion="+vo.get("regionCentroAntencion")+" or dpo.region_centro_atencion is null) " +
									//"AND (dpo.pais_centro_atencion='"+vo.get("paisCentroAtencion")+"' or dpo.pais_centro_atencion is null) " +
									"and (dpo.ciudad_centro_atencion='"+vo.get("ciudadCentroAtencion")+"' or dpo.ciudad_centro_atencion is null) " +
									"and (dpo.depto_centro_atencion='"+vo.get("deptoCentroAtencion")+"' or dpo.depto_centro_atencion is null) " +
									"and (dcapo.centro_atencion="+vo.get("centroAtencion")+" or  dcapo.centro_atencion is null) " +
									"and (dcpo.convenio="+vo.get("convenio")+" or dcpo.convenio is null) " +
									"and ("+vo.get("edad")+" between coalesce(dpo.edad_inicial, 0) and coalesce(dpo.edad_final, 999)) " +
									"and (dpo.sexo="+vo.get("sexo")+" or dpo.sexo is null) " +
									"and (dpo.estado_civil='"+vo.get("estadocivil")+"' or dpo.estado_civil is null) ";
			
			consultaStr+=		"and ("+vo.get("nro_hijos")+" = dpo.nro_hijos or dpo.nro_hijos is null) ";
			
			consultaStr+=			"and (dpo.ocupacion_paciente="+vo.get("ocupacion")+" or dpo.ocupacion_paciente is null) " +
									"ORDER BY dpo.porcentaje_descuento, dpo.valor_descuento, po.nombre ";
			
			logger.info("\n\n obtenerValorDescuentoPromociones-->"+consultaStr+" \n\n");
			
			try 
		    {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())
				{
					InfoPromocionPresupuestoServPrograma info= new InfoPromocionPresupuestoServPrograma();
					
					info.setDetPromocion(rs.getInt("codigopkdetalle"));
					
					// int diasMaxPagoInicial= rs.getInt("dias_max_pago_inicial");
					
					/*
					if(diasMaxPagoInicial>0 && UtilidadTexto.getBoolean(rs.getString("requierepago")))
					{	
						info.setFechaLimite(UtilidadFecha.incrementarDiasAFecha(vo.get("fechainiciopromocion") , diasMaxPagoInicial, false));
					}	
					*/
					info.setPorcentajeHonorario(rs.getDouble("porcentajehonorario"));
					info.setValorHonorario(rs.getBigDecimal("valorhonorario"));
					info.setPorcentajePromocion(rs.getDouble("porcentaje"));
					info.setValorPromocion(rs.getBigDecimal("valor"));
					
					if(info.getValorPromocion().doubleValue()>0)
					{
						info.setSeleccionadoPorcentaje(false);
					}
					else if(info.getPorcentajePromocion()>0)
					{
						info.setSeleccionadoPorcentaje(true);
					}
					
					logger.info("sel prom-->"+info.isSeleccionadoPorcentaje());
					
					info.setNombre(rs.getString("nombre"));
					//info.setPorcentajeHonorarioCALCULADO(new BigDecimal(info.getPorcentajeHonorario()));
					array.add(info);
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			return array;
		}
		
		
	
		
		
		/**
		 * 
		 * @param dto
		 * @param codigoInstitucion
		 * @return
		 */
		public static ArrayList<DtoPromocionesOdontologicas> consultaAvanzadaPromociones (DtoPromocionesOdontologicas dto)
		{
			ArrayList<DtoPromocionesOdontologicas> listaPromociones = new ArrayList<DtoPromocionesOdontologicas>();
			
		
			String consultaStr= "select distinct po.codigo_pk as codigoPromocion, " +
								"po.nombre as nombrePromocion, " +
								"po.fecha_inicial_vigencia as fechaInicialVigencia," +
								" po.fecha_final_vigencia as fechaFinalVigencia," +
								" po.hora_inicial_vigencia as horaInicialVigencia,"+
								"po.hora_final_vigencia as horaFinalVigencia ," +
								" po.activo as activo ," +
								"po.fecha_generacion as fechaGeneracion, " +
								"dpo.codigo_pk as codigoDetPromociones , " +
								"dpo.region_centro_atencion as regionCentroAtencion,"+ 
								
								"dpo.pais_centro_atencion as paisCentroAtencion, " +
								"dpo.ciudad_centro_atencion as ciudadCentroAtencion," +
								"dpo.depto_centro_atencion as deptoCentroAtencion ,"+
								"dpo.categoria_centro_atencion as categoriaCentroAtencion ," +
								
								"(select ca.descripcion from  administracion.categorias_atencion ca where codigo=dpo.categoria_centro_atencion) as nombreCategoria ,"+
								"(select  rc.descripcion  from administracion.regiones_cobertura rc where rc.codigo=dpo.region_centro_atencion) as nombreRegion ,"+	
								"(select p.descripcion  from  administracion.paises p where p.codigo_pais=dpo.pais_centro_atencion)  as nombrePais,"+
								"(select  c.descripcion from  administracion.ciudades c where c.codigo_ciudad=dpo.ciudad_centro_atencion and  c.codigo_departamento=dpo.depto_centro_atencion ) as nombreCiudad, "+
								
								"dpo.programa_odontologico as programa ," +
								"(select p.codigo_programa ||' '|| p.nombre   from odontologia.programas p where codigo=dpo.programa_odontologico ) as nombrePrograma, " +
								"coalesce(dpo.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio ,"+
								"case when dpo.servicio is null then '' else getcodigopropservicio" +
								"( dpo.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") ||' - '|| " +
								"getnombreservicio(dpo.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombreServicio, " +
								"dpo.edad_inicial as edadInicial ," +
								" dpo.edad_final as edadFinal," +
								"(select ec.nombre from estados_civiles ec where ec.acronimo=dpo.estado_civil) as nombreEstadoCivil,"+
								"dpo.sexo as sexo," +
								"(select s.nombre from sexo s where s.codigo=dpo.sexo) as nombreSexo," +
								"dpo.estado_civil as estadoCivil," +
								"dpo.nro_hijos as nroHijos," +
								"dpo.ocupacion_paciente as ocupacionPaciente," +
								"(select ocpa.nombre from manejopaciente.ocupaciones ocpa where ocpa.codigo=dpo.ocupacion_paciente) as nombreOcupacion, " +
								"dpo.porcentaje_descuento as porcentajeDescuento, "+
								"dpo.valor_descuento as valorDescuento ," +
								"dpo.porcentaje_honorario as porcentajeHonorarios ," +
								" dpo.valor_honorario as valorHonorarios "+
								
								"from  odontologia.promociones_odontologicas po left outer JOIN odontologia.det_promociones_odo dpo ON (po.codigo_pk=dpo.promocion_odontologia) left outer JOIN  odontologia.det_ca_promociones_odo	 dcp ON(dcp.det_promocion_odo=dpo.codigo_pk)";
								
								/*
								 * 
								 * SI HAY QUE HACER EL CAMBIO
								if(aplicaProgramas)
								{
									consultaStr+= " left outer JOIN  odontologia.programas pro  ON( dpo.)   pro.especialidad="; 
								}
								else
								{
									consultaStr+= " left outer JOIN ";
								}*/
								
								
								consultaStr+= "  WHERE 1=1 ";
								
			
				//FALTA
			
			 consultaStr+=!UtilidadTexto.isEmpty(dto.getFechaInicialVigencia())?" AND po.fecha_inicial_vigencia>='"+dto.getFechaVigenciaInicialFormatoBD()+"'": "";
			 consultaStr+=!UtilidadTexto.isEmpty(dto.getFechaFinalVigencia())?" AND po.fecha_final_vigencia<='"+dto.getFechaVigenciaFinalFormatoBD() +"'": "";
			 consultaStr+= dto.getDtoDetPromociones().getEdadInicial()>0?" AND  dpo.edad_inicial="+ dto.getDtoDetPromociones().getEdadInicial(): " ";
			 consultaStr+= dto.getDtoDetPromociones().getEdadFinal()>0?" AND  dpo.edad_final="+ dto.getDtoDetPromociones().getEdadFinal(): " ";
			 consultaStr+=!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getPaisCentroAtencion())?" AND dpo.pais_centro_atencion='"+dto.getDtoDetPromociones().getPaisCentroAtencion()+"'": " ";
			 consultaStr+=!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getCiudadCentroAtencion())?"AND dpo.ciudad_centro_atencion='"+dto.getDtoDetPromociones().getCiudadCentroAtencion()+"'": " ";
			 consultaStr+= (dto.getDtoDetPromociones().getRegionCentroAtencion()>0)?"AND dpo.region_centro_atencion="+dto.getDtoDetPromociones().getRegionCentroAtencion(): " ";
			 // falta el cento de atencion
			 consultaStr+= dto.getDtoDetPromociones().getSexo()>0?"AND  dpo.sexo="+dto.getDtoDetPromociones().getSexo(): "";
			 consultaStr+= !UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getEstadoCivil())?" AND dpo.estado_civil='"+dto.getDtoDetPromociones().getEstadoCivil()+"'":" "; 
			 consultaStr+=  dto.getDtoDetPromociones().getProgramaOdontologico().getCodigo()>0?"AND dpo.programa_odontologico="+dto.getDtoDetPromociones().getProgramaOdontologico().getCodigo(): "";
			 consultaStr+=  dto.getDtoDetPromociones().getServicio().getCodigo()>0?" AND dpo.servicio="+dto.getDtoDetPromociones().getServicio().getCodigo(): "";
			 consultaStr+= " AND po.activo='"+dto.getActivo()+"'"; 
			 consultaStr+= dto.getDtoDetPromociones().getOcupacionPaciente()>0?" AND dpo.ocupacion_paciente="+dto.getDtoDetPromociones().getOcupacionPaciente(): " ";
			 consultaStr+= dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getCodigo()>0? "and  ( dcp.centro_atencion="+dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getCodigo()+ " OR dcp.centro_atencion is null) ":" ";
			 consultaStr+= "  ";
			 
			 if(!UtilidadTexto.isEmpty(dto.getFechaGeneracionInicialBD())  && (UtilidadTexto.isEmpty(dto.getFechaGeneracionFinalBD())) )
			 {	 
				 consultaStr+=" and  ( fecha_generacion  between   '"+dto.getFechaGeneracionInicialBD()+"'  and  '"+dto.getFechaGeneracionFinalBD()+"') ";
			 }
			 
			 consultaStr+= "  order by  po.fecha_inicial_vigencia";
			 //falta fecha generacion
			
			 
			
			 logger.info(" CONSULTA PROMOCIONES ODONTOLOGICAS \n\n\n"+consultaStr+"\n\n\n");
			 
			 
			 
			 
			
			
			try 
		    {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
				ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())
				{
					DtoPromocionesOdontologicas newDto = new DtoPromocionesOdontologicas();
					
					newDto.setCodigoPk(rs.getInt("codigoPromocion"));
					newDto.setNombre(rs.getString("nombrePromocion"));
					newDto.setFechaInicialVigencia(rs.getString("fechaInicialVigencia"));
					newDto.setFechaFinalVigencia(rs.getString("fechaFinalVigencia"));
					newDto.setHoraInicialVigencia(rs.getString("horaInicialVigencia"));
					newDto.setHoraFinalVigencia(rs.getString("horaFinalVigencia"));
					newDto.setActivo(rs.getString("activo"));
					newDto.setFechaGeneracion(rs.getString("fechaGeneracion"));
					
					newDto.getDtoDetPromociones().setCodigoPk(rs.getInt("codigoDetPromociones"));
					
					if(newDto.getDtoDetPromociones().getCodigoPk()>0)
					{
						/*
						 *CARGA LA LISTA DE DTO DETALLES CONVENIOS 
						 */
						DtoDetConvPromocionesOdo dtoDetConv = new DtoDetConvPromocionesOdo();
						dtoDetConv.setDetPromocionOdo(newDto.getDtoDetPromociones().getCodigoPk());
						dtoDetConv.setActivo(ConstantesBD.acronimoSi);
						newDto.getDtoDetPromociones().setListDetConv(DetConvPromocionesOdo.cargar(dtoDetConv));
						
						/*
						 *CARGAR DETALLE CENTRO ATENCION 
						 */
						DtoDetCaPromocionesOdo dtoDetCa = new DtoDetCaPromocionesOdo();
						dtoDetCa.setDetPromocionOdo(newDto.getDtoDetPromociones().getCodigoPk());
						dtoDetCa.setActivo(ConstantesBD.acronimoSi);
						newDto.getDtoDetPromociones().setListDetCa(DetCaPromocionesOdo.cargar(dtoDetCa));
					}
					
					
					newDto.getDtoDetPromociones().setRegionCentroAtencion(rs.getDouble("regionCentroAtencion"));
					newDto.getDtoDetPromociones().setPaisCentroAtencion(rs.getString("paisCentroAtencion"));
					newDto.getDtoDetPromociones().setCiudadCentroAtencion(rs.getString("ciudadCentroAtencion"));
					newDto.getDtoDetPromociones().setDeptoCentroAtencion(rs.getString("deptoCentroAtencion"));
					newDto.getDtoDetPromociones().setProgramaOdontologico(new InfoDatosDouble(rs.getDouble("programa"), rs.getString("nombrePrograma")));
					newDto.getDtoDetPromociones().setServicio( new  InfoDatosDouble( rs.getDouble("servicio"), rs.getString("nombreServicio")) );
					newDto.getDtoDetPromociones().setEdadInicial(rs.getInt("edadInicial"));
					newDto.getDtoDetPromociones().setEdadFinal(rs.getInt("edadFinal"));
					newDto.getDtoDetPromociones().setSexo(rs.getInt("sexo"));
					newDto.getDtoDetPromociones().setEstadoCivil(rs.getString("estadoCivil"));
					newDto.getDtoDetPromociones().setNroHijos(rs.getInt("nroHijos"));
					newDto.getDtoDetPromociones().setOcupacionPaciente(rs.getInt("ocupacionPaciente"));
					newDto.getDtoDetPromociones().setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));
					newDto.getDtoDetPromociones().setValorDescuento(rs.getDouble("valorDescuento"));
					newDto.getDtoDetPromociones().setPorcentajeHonorarios(rs.getDouble("porcentajeHonorarios"));
					newDto.getDtoDetPromociones().setValorHonorario(rs.getDouble("valorHonorarios"));
					newDto.getDtoDetPromociones().setNombreEstadoCivil(rs.getString("nombreEstadoCivil"));
					newDto.getDtoDetPromociones().setNombreSexo(rs.getString("nombreSexo"));
					newDto.getDtoDetPromociones().setNombreOcupacion(rs.getString("nombreOcupacion"));
					newDto.getDtoDetPromociones().setNombreCategoria(rs.getString("nombreCategoria"));
					newDto.getDtoDetPromociones().setNombreCiudad(rs.getString("nombreCiudad"));
					newDto.getDtoDetPromociones().setNombrePais(rs.getString("nombrePais"));
					newDto.getDtoDetPromociones().setNombreRegion(rs.getString("nombreRegion"));
					
					
					
					listaPromociones.add(newDto);
					
					
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			return listaPromociones;
		}
		
		
		
		
		/**
		 * RETORNA LA CONSULTA PARA EL REPORTE 
		 * @param dto
		 * @return
		 */
		public static String consultaAvanzadaPromocionesReporte (DtoPromocionesOdontologicas dto, boolean aplicaProgramas)
		{
			
			logger.info("\n\n\n\n aplicacion---------"+aplicaProgramas);
			String 	consultaStr= 
						"select distinct po.codigo_pk as codigoPromocion, "+ 
						"po.nombre as nombrePromocion, "+
						"po.fecha_inicial_vigencia as fechaInicialVigencia,"+ 		
						
						
						
						"po.fecha_final_vigencia as fechaFinalVigencia, "+
						"po.hora_inicial_vigencia as horaInicialVigencia,"+
						"po.hora_final_vigencia as horaFinalVigencia , "+
						"po.activo as activo ,"+
						"dpo.codigo_pk as codigoDetPromociones ,"+ 
						
						"(select ca.descripcion from  administracion.categorias_atencion ca where codigo=dpo.categoria_centro_atencion) as categoriacentroatencion ,"+
						
						"(select  rc.descripcion  from administracion.regiones_cobertura rc where rc.codigo=dpo.region_centro_atencion) as regioncentroatencion ,"+	
						
						"(select p.descripcion  from  administracion.paises p where p.codigo_pais=dpo.pais_centro_atencion)  as paiscentroatencion,"+
						
						"(select  c.descripcion from  administracion.ciudades c where c.codigo_ciudad=dpo.ciudad_centro_atencion and  c.codigo_departamento=dpo.depto_centro_atencion ) as ciudadCentroAtencion, "+
						
						"dpo.depto_centro_atencion as deptoCentroAtencion ," +
						
						
						"dpo.programa_odontologico as programa ,";
		 	
					 	if(aplicaProgramas)
						{
							consultaStr+="(select p.codigo_programa ||' '|| p.nombre   from odontologia.programas p where p.codigo=dpo.programa_odontologico ) as nombreprogramaservicio, ";
						}
						else
						{
							consultaStr+="coalesce(dpo.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio ,"+
							"case when dpo.servicio is null then '' else getcodigopropservicio" +
							"( dpo.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") ||' - '|| " +
							"getnombreservicio(dpo.servicio, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombreprogramaservicio, ";
						} 
			
			
		 	consultaStr+= "dpo.edad_inicial as edadInicial , "+
						"dpo.edad_final as edadFinal,"+
						"(select ec.nombre from estados_civiles ec where ec.acronimo=dpo.estado_civil) as nombreEstadoCivil,"+
						"dpo.sexo as sexo,"+
						"(select s.nombre from sexo s where s.codigo=dpo.sexo) as nombreSexo, "+
						"dpo.estado_civil as estadoCivil, "+
						"dpo.nro_hijos as nroHijos,"+
						"dpo.ocupacion_paciente as ocupacionPaciente,"+
						"(select ocpa.nombre from manejopaciente.ocupaciones ocpa where ocpa.codigo=dpo.ocupacion_paciente) as nombreOcupacion,"+ 
						"dpo.porcentaje_descuento as porcentajeDescuento, "+
						"dpo.valor_descuento as valorDescuento ,"+
						"dpo.porcentaje_honorario as porcentajeHonorarios ,"+ 
						"dpo.valor_honorario as valorHonorarios "+
						"from  odontologia.promociones_odontologicas po INNER JOIN odontologia.det_promociones_odo dpo ON (po.codigo_pk=dpo.promocion_odontologia) INNER  		JOIN  		odontologia.det_ca_promociones_odo dcp ON(dcp.det_promocion_odo=dpo.codigo_pk) WHERE 1=1      AND po.activo='S' "   ;
			
			
			
			consultaStr+=!UtilidadTexto.isEmpty(dto.getFechaInicialVigencia())?" AND po.fecha_inicial_vigencia>='"+dto.getFechaVigenciaInicialFormatoBD()+"'": "";
			consultaStr+=!UtilidadTexto.isEmpty(dto.getFechaFinalVigencia())?" AND po.fecha_final_vigencia<='"+dto.getFechaVigenciaFinalFormatoBD() +"'": "";
			consultaStr+=!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getPaisCentroAtencion())?" AND dpo.pais_centro_atencion='"+dto.getDtoDetPromociones().getPaisCentroAtencion()+"'": " ";
			consultaStr+=!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getCiudadCentroAtencion())?" AND dpo.ciudad_centro_atencion='"+dto.getDtoDetPromociones().getCiudadCentroAtencion()+"'": " ";
			consultaStr+= (dto.getDtoDetPromociones().getRegionCentroAtencion()>0)?" AND dpo.region_centro_atencion="+dto.getDtoDetPromociones().getRegionCentroAtencion(): " ";
			consultaStr+= dto.getDtoDetPromociones().getSexo()>0?" AND  dpo.sexo="+dto.getDtoDetPromociones().getSexo(): "";
			consultaStr+= !UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getEstadoCivil())?" AND dpo.estado_civil='"+dto.getDtoDetPromociones().getEstadoCivil()+"'":" "; 
			consultaStr+=  dto.getDtoDetPromociones().getProgramaOdontologico().getCodigo()>0?" AND dpo.programa_odontologico="+dto.getDtoDetPromociones().getProgramaOdontologico().getCodigo(): "";
			consultaStr+=  dto.getDtoDetPromociones().getServicio().getCodigo()>0?" AND dpo.servicio="+dto.getDtoDetPromociones().getServicio().getCodigo(): "";
			consultaStr+= " AND po.activo='"+dto.getActivo()+"' "; 
			consultaStr+= dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getCodigo()>0? " and  ( dcp.centro_atencion="+dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getCodigo()+ " OR dcp.centro_atencion is null) ":" ";
			consultaStr+= "   ";
			consultaStr+=" and  ( fecha_generacion  between   '"+dto.getFechaGeneracionInicialBD()+"'  and  '"+dto.getFechaGeneracionFinalBD()+"')";  
			consultaStr+= "  order by  po.fecha_inicial_vigencia ";
			// TODO MIRAR SI FALTA LA INSTITUCION?? 
			//No hay una notificacion clara por esos no se coloca la institucion 
			
			
			return consultaStr;
		}
		
		
			
		 	
			/**
			 * 
			 * @param dto
			 * @return
			 */
		public static double guardarLogConsulta(DtoPromocionesOdontologicas  dto, String tipoReporte, String ruta)
		{
				
				double secuencia=ConstantesBD.codigoNuncaValidoDouble;
				
				logger.info("Guadar Informacion-------------");
				logger.info(InsertarStr);
				
				logger.info(UtilidadLog.obtenerString(dto, true));
			 	
				
				String insertarLog=
				
				"insert into  odontologia.log_consul_repor_promo "+
															 	"(codigo_pk , "+ //1
															 	"region_centro_atencion , "+ //2
															 	"pais_centro_atencion , "+ //3
															 	"ciudad_centro_atencion , "+ //4
															 	"depto_centro_atencion , "+  //5
															 	"categoria_centro_atencion ,"+ //6
															 	"programa_odontologico , "+//7
															 	"edad_inicial , "+ //8
															 	"edad_final , "+ //9
															 	"sexo , "+ //10
															 	"estado_civil ,"+ //11
															 	"nro_hijos , "+ //12
															 	"ocupacion_paciente , "+//13
															 	"porcentaje_descuento , "+ //14
															 	"valor_descuento , "+ //15
															 	"porcentaje_honorario , "+//16
															 	"valor_honorario, "+ //17
															 	"fecha_modifica , "+ //18
															 	"hora_modifica , "+//19
															 	"usuario_modifica , "+//20
															 	"servicio , "+//21
															 	"codigo_centro , "+//22
															 	"nombre_centro , "+//23
															 	"fecha_inicial_vigencia , "+//24
															 	"fecha_final_vigencia , "+//25
															 	"fecha_generacion_inicial , "+//26
															 	"fecha_generacion_fecha, "+//27
															 	"activo, "+//28
															 	"institucion ," +
															 	"tipo_reporte ," +
															 	"ruta " +
															 	") values " +//29
															 	"(?,?,?,?,?,?,?,?,?,?," +
															 	" ?,?,?,?,?,?,?,?,?,?," +
															 	" ?,?,?,?,?,?,?,?,?, ?, ?)" ;
				
			
			 	
				try 
				{
				//Objetos de Conexion
					Connection con = UtilidadBD.abrirConexion();   
					ResultSetDecorator rs = null;
					secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_consul_repor_promo"); // Por ejecutar
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarLog);
					logger.info("codigo=="+secuencia);
						
					ps.setDouble(1,secuencia);
					
					
					if(dto.getDtoDetPromociones().getRegionCentroAtencion()>0)
					{
						ps.setDouble(2,dto.getDtoDetPromociones().getRegionCentroAtencion() );
					}
					else
					{
						ps.setNull(2, Types.DOUBLE);	
					}
					
					
					if(!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getPaisCentroAtencion()))
					{
						ps.setString(3,dto.getDtoDetPromociones().getPaisCentroAtencion());
					}
					else
					{
						ps.setNull(3, Types.VARCHAR);		
					}
					
					
					if(!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getCiudadCentroAtencion()))
					{
						ps.setString(4, dto.getDtoDetPromociones().getCiudadCentroAtencion());
					}
					else
					{
						ps.setNull(4, Types.VARCHAR);
					}
					
					
					if(!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getDeptoCentroAtencion()))
					{
						ps.setString(5, dto.getDtoDetPromociones().getDeptoCentroAtencion());
					}
					else
					{
						ps.setNull(5, Types.VARCHAR);
					}
					
					if( dto.getDtoDetPromociones().getCategoriaCentroAtencion()>0)
					{
						ps.setDouble(6, dto.getDtoDetPromociones().getCategoriaCentroAtencion());
					}
					else
					{
						ps.setNull(6, Types.VARCHAR);
					}
					
					
					if(dto.getDtoDetPromociones().getProgramaOdontologico().getCodigo()>0)
					{
						ps.setDouble(7, dto.getDtoDetPromociones().getProgramaOdontologico().getCodigo());
					}
					else
					{
						ps.setNull(7, Types.DOUBLE);
					}
					
					if(dto.getDtoDetPromociones().getEdadInicial()>0)
					{
						ps.setInt(8, dto.getDtoDetPromociones().getEdadInicial());
					}
					else
					{
						ps.setNull(8,Types.INTEGER);
					}
					
					if(dto.getDtoDetPromociones().getEdadFinal()>0)
					{
						ps.setInt(9, dto.getDtoDetPromociones().getEdadFinal());
					}
					else
					{
						ps.setNull(9, Types.INTEGER);
					}
					
					if(dto.getDtoDetPromociones().getSexo()>0)
					{
						ps.setInt(10,dto.getDtoDetPromociones().getSexo());
					}
					else
					{
						ps.setNull(10, Types.INTEGER);
					}
					
					if(!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getEstadoCivil()))
					{
						ps.setString(11, dto.getDtoDetPromociones().getEstadoCivil());
					}
					else
					{
						ps.setNull(11,Types.VARCHAR);
					}
					
					if(dto.getDtoDetPromociones().getNroHijos()>0)
					{
						ps.setInt(12, dto.getDtoDetPromociones().getNroHijos());
					}
					else
					{
						ps.setNull(12, Types.INTEGER);
					}
					
					if(dto.getDtoDetPromociones().getOcupacionPaciente()>0)
					{
						ps.setInt(13, dto.getDtoDetPromociones().getOcupacionPaciente());
					}
					else
					{
						ps.setNull(13,Types.INTEGER);
					}
					
					if(dto.getDtoDetPromociones().getPorcentajeDescuento()>0)
					{
						ps.setDouble(14, dto.getDtoDetPromociones().getPorcentajeDescuento());
					}
					else
					{
						ps.setNull(14, Types.DOUBLE);
					}
					
					
					if(dto.getDtoDetPromociones().getValorDescuento()>0)
					{
						ps.setDouble(15, dto.getDtoDetPromociones().getValorDescuento());
					}
					else
					{
						ps.setNull(15,Types.DOUBLE);
					}
					
					
					if( dto.getDtoDetPromociones().getPorcentajeHonorarios()>0)
					{
						ps.setDouble(16, dto.getDtoDetPromociones().getPorcentajeHonorarios());
					}
					else
					{
						ps.setNull(16, Types.DOUBLE);
					}
				
					
					if(dto.getDtoDetPromociones().getValorHonorario()>0)
					{
						ps.setDouble(17, dto.getDtoDetPromociones().getValorHonorario());
					}
					else
					{
						ps.setNull(17, Types.DOUBLE);
					}
					
					
					
					ps.setString(18, dto.getFechaModificaBD());
					ps.setString(19, dto.getHoraModificada());
					ps.setString(20, dto.getUsuarioModifica());
					
					
					if(dto.getDtoDetPromociones().getServicio().getCodigo()>0)
					{
						ps.setDouble(21, dto.getDtoDetPromociones().getServicio().getCodigo());
					}
					else
					{
						ps.setNull(21, Types.DOUBLE);
					}
					
					
					if( dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getCodigo()>0)
					{
						ps.setInt(22, dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getCodigo());
					}
					else
					{
						ps.setNull(22, Types.DOUBLE);
					}
					
					if(!UtilidadTexto.isEmpty(dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getNombre()))
					{
						ps.setString(23, dto.getDtoDetPromociones().getDtoDetCaPromociones().getCentroAtencion().getNombre());
					}
					else
					{
						ps.setNull(23, Types.VARCHAR);
					}
					
					ps.setString(24,dto.getFechaInicialVigenciaBD());
					ps.setString(25,dto.getFechaVigenciaFinalFormatoBD());
					ps.setString(26,dto.getFechaGeneracionInicialBD() );
					ps.setString(27,dto.getFechaGeneracionFinalBD());
					ps.setString(28,dto.getActivo());
					ps.setInt(29,dto.getInstitucion());
					ps.setString(30, tipoReporte);
					ps.setString(31, ruta);
					
					logger.info(" LOG INSERTAR CONSULTA AVANZADA PROMOCIONES \n\n\n"+	ps+"\n");
				
					
					if(ps.executeUpdate()>0)
						{
							SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
							return secuencia;
						}
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
					
				}
				catch (SQLException e) 
				{
					logger.error("ERROR en insert " + e);
				}
				
				return secuencia;
				
			}
		 
		
	
		
		
}
