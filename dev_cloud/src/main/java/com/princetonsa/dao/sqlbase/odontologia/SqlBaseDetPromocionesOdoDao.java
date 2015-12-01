package com.princetonsa.dao.sqlbase.odontologia;

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

import com.ibm.icu.util.ULocale.Type;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;

public class SqlBaseDetPromocionesOdoDao {
	
	  private static Logger logger = Logger.getLogger( SqlBaseDetPromocionesOdoDao.class);
			

	  
	  
	  /**
		 * CADENA para insertar 	 
		 * */
	   	private static String insertarStr=" INSERT INTO odontologia.det_promociones_odo (" +	   
																			" 	codigo_pk , "+ //1
																			"	promocion_odontologia, "+ //2
																			" 	region_centro_atencion , "+ //3
																			"	pais_centro_atencion , "+ //4
																			"	ciudad_centro_atencion , "+ //5
																			"	depto_centro_atencion , "+ //6
																			"	categoria_centro_atencion , "+ //7
																			"	programa_odontologico , "+ //8
																			"	edad_inicial, "+ //9
																			"	edad_final, "+ //10
																			"	sexo ,"+ //11
																			"	estado_civil ,  "+ //12
																			"	nro_hijos , "+ //13
																			"	ocupacion_paciente ,"+ //14
																			"	porcentaje_descuento ,"+ //15
																			"	valor_descuento , "+ //16
																			"	porcentaje_honorario , "+ //17
																			"	valor_honorario   , "+  //18 
																			"  	fecha_modifica, "+	//19
																			"	hora_modifica  , "+ //20
																			"	usuario_modifica ,"+ //21
																			"	servicio  "+ //22
																			"	) values ( " +		"? , ? , ? , ? , ? , ? , ? , ? , ? , ? , " +//10
																									"? , ? , ? , ? , ? , ? , ? , ? , ? , ? , " +//10
																									"? ,  ? ) " ;//2
	   	
	
	   	/**
	   	 * 
	   	 */
	   	private static String insertarStr2=" INSERT INTO odontologia.log_det_promociones_odo (" +	   
																			" 	codigo_pk , "+ //1
																			"	promocion_odontologia, "+ //2
																			" 	region_centro_atencion , "+ //3
																			"	pais_centro_atencion , "+ //4
																			"	ciudad_centro_atencion , "+ //5
																			"	depto_centro_atencion , "+ //6
																			"	categoria_centro_atencion , "+ //7
																			"	programa_odontologico , "+ //8
																			"	edad_inicial, "+ //9
																			"	edad_final, "+ //10
																			"	sexo ,"+ //11
																			"	estado_civil ,  "+ //12
																			"	nro_hijos , "+ //13
																			"	ocupacion_paciente ,"+ //14
																			"	porcentaje_descuento ,"+ //15
																			"	valor_descuento , "+ //16
																			"	porcentaje_honorario , "+ //17
																			"	valor_honorario   , "+  //18 
																			"  	fecha_modifica, "+	//19
																			"	hora_modifica  , "+ //20
																			"	usuario_modifica, "+ //21
																			"   det_promocion_odo "+ //22
																			"	) values ( " +		"? , ? , ? , ? , ? , ? , ? , ? , ? , ? , " +//10
																									"? , ? , ? , ? , ? , ? , ? , ? , ? , ? , " +//10
																									"? ,  ?  ) " ;//2
	   	/**
		 * 
		 * @param dto
		 * @return
		 */
		public static int guardar( DtoDetPromocionOdo dto) 
		{
			int secuencia=ConstantesBD.codigoNuncaValido;
			
			logger.info("*********************************************************************************************");
			logger.info("							GUARDAR DETALLE PROMOCIONES ODONTOLOGICAS ");
			logger.info("*********************************************************************************************");
				
			
			
			try 
			{
				//Objetos de Conexion
				Connection con = UtilidadBD.abrirConexion();   
				ResultSetDecorator rs = null;
				secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_det_promociones_odo"); // Por ejecutar
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStr);
				//UtilidadLog.obtenerString(dto , true);
				
				ps.setInt(1, secuencia);
				ps.setInt(2, dto.getPromocionOdontologia());
				if(0<dto.getRegionCentroAtencion())
				{
					ps.setDouble(3, dto.getRegionCentroAtencion());	
				}
				else
				{
					ps.setNull(3, Types.NUMERIC);
				}
				
				if(UtilidadTexto.isEmpty(dto.getPaisCentroAtencion()))
				{
					ps.setNull(4,Types.VARCHAR);
				}
				else
				{
				ps.setString(4, dto.getPaisCentroAtencion());
				}
			
				if(UtilidadTexto.isEmpty(dto.getCiudadCentroAtencion()))
				{
				ps.setNull(5,Types.VARCHAR);//emun falta
				}
				else
				{	ps.setString(5, dto.getCiudadCentroAtencion());//emun falta
				}
				
				
				if(UtilidadTexto.isEmpty(dto.getDeptoCentroAtencion()))
				ps.setNull(6, Types.VARCHAR);
				else
				ps.setString(6, dto.getDeptoCentroAtencion());
					
				
				if(0>=dto.getCategoriaCentroAtencion())
				{
					ps.setNull(7,Types.NUMERIC );
				}
				else
					ps.setDouble(7, dto.getCategoriaCentroAtencion());
				
				if(0>=dto.getProgramaOdontologico().getCodigo())
				{
					ps.setNull(8, Types.NUMERIC);
				}
				else	
				ps.setDouble(8, dto.getProgramaOdontologico().getCodigo());
				
				
				if(0>=dto.getEdadInicial())
				{
					ps.setNull(9, Types.INTEGER);
				}
				else
					ps.setInt(9, dto.getEdadInicial());
				
				
				if(0>=dto.getEdadFinal())
				{
					ps.setNull(10,Types.INTEGER);
				}
			   	else
					ps.setInt(10, dto.getEdadFinal());
				
				
		        if(0>=dto.getSexo())
		        	ps.setNull(11, Types.INTEGER);
		        else
		        	ps.setInt(11, dto.getSexo());
		        
		        
		        if(UtilidadTexto.isEmpty(dto.getEstadoCivil()))
		            ps.setNull(12, Types.VARCHAR);
		        else
		        	ps.setString(12, dto.getEstadoCivil());
				
		        
		        if(0>=dto.getNroHijos())
		        	ps.setNull(13,Types.INTEGER);
		        else
		        	ps.setInt(13, dto.getNroHijos());
		        
		       
		        if(0>=dto.getOcupacionPaciente())
		          ps.setNull(14, Types.INTEGER);
		        	else
		          ps.setInt(14, dto.getOcupacionPaciente());
				
		        if(0>=dto.getPorcentajeDescuento())
		        ps.setNull(15, Types.DOUBLE);
		        else
		        ps.setDouble(15, dto.getPorcentajeDescuento());
				
		        
		        if(0>=dto.getValorDescuento())
		            ps.setNull(16, Types.NUMERIC);
		         else
		        	ps.setDouble(16, dto.getValorDescuento());
				
		        
		        if(0>=dto.getPorcentajeHonorarios())
		        	ps.setNull(17, Types.NUMERIC);
		        else
		        ps.setDouble(17, dto.getPorcentajeHonorarios());
		        
		        
				
		        if(0>=dto.getValorHonorario())
		        	ps.setNull(18,Types.NUMERIC);
		        else
		        ps.setDouble(18, dto.getValorHonorario());
				
		        
		        ps.setString(19, dto.getFechaModificada());
				ps.setString(20, dto.getHoraModificada());
				ps.setString(21, dto.getUsuarioModifica());
				
				if(dto.getServicio().getCodigo() > 0)
				{
					ps.setDouble(22, dto.getServicio().getCodigo());
				}else{
					
					ps.setNull(22,Types.NUMERIC);
				}
				
				logger.info("\n\n\n\n SqlDET LOG"+ps+"\n\n\n\n");
				
				
				if(ps.executeUpdate()>0)
				{
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs , con);
					return secuencia;
				}
				
				logger.info("\n\n ********************************************** SQL INSERTAR DETALLE DETALLE ODONTOLOGICO \n\n"+ps+"\n\n\n\n\n\n\n\n");
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
		public static int guardarLog( DtoDetPromocionOdo dto) 
		{
		
			logger.info("	GUARDAR LOG DET PROMOCION ");
			
			int secuencia=ConstantesBD.codigoNuncaValido;
		
			try 
			{
				//Objetos de Conexion
				Connection con = UtilidadBD.abrirConexion();   
				ResultSetDecorator rs = null;
				secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.log_seq_promociones_odonto"); // Por ejecutar
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStr2);
				ps.setInt(1, secuencia);
	
				ps.setInt(2, dto.getPromocionOdontologia());
				
				
				if(0<dto.getRegionCentroAtencion())
				{
					ps.setDouble(3, dto.getRegionCentroAtencion());	
				}
				else
				{
					ps.setNull(3, Types.NUMERIC);
				}
				
				if(UtilidadTexto.isEmpty(dto.getPaisCentroAtencion()))
				{
					ps.setNull(4,Types.VARCHAR);
				}
				else
				{
				ps.setString(4, dto.getPaisCentroAtencion());
				}
				
				
				
				if(UtilidadTexto.isEmpty(dto.getCiudadCentroAtencion()))
				{
				ps.setNull(5,Types.VARCHAR);//emun falta
				}
				else
				{	ps.setString(5, dto.getCiudadCentroAtencion());//emun falta
				}
				
				
				if(UtilidadTexto.isEmpty(dto.getDeptoCentroAtencion()))
				ps.setNull(6, Types.VARCHAR);
				else
				ps.setString(6, dto.getDeptoCentroAtencion());
					
				
				if(0>=dto.getCategoriaCentroAtencion())
				{
					ps.setNull(7,Types.NUMERIC );
				}
				else
					ps.setDouble(7, dto.getCategoriaCentroAtencion());
				
				if(0>=dto.getProgramaOdontologico().getCodigo())
				{
					ps.setNull(8, Types.NUMERIC);
				}
				else	
				ps.setDouble(8, dto.getProgramaOdontologico().getCodigo());
				
				
				if(0>=dto.getEdadInicial())
				{
					ps.setNull(9, Types.INTEGER);
				}
				else
					ps.setInt(9, dto.getEdadInicial());
				
				
				if(0>=dto.getEdadFinal())
				{
					ps.setNull(10,Types.INTEGER);
				}
			   	else
					ps.setInt(10, dto.getEdadFinal());
				
				
		        if(0>=dto.getSexo())
		        	ps.setNull(11, Types.INTEGER);
		        else
		        	ps.setInt(11, dto.getSexo());
		        
		        
		        if(UtilidadTexto.isEmpty(dto.getEstadoCivil()))
		            ps.setNull(12, Types.VARCHAR);
		        else
		        	ps.setString(12, dto.getEstadoCivil());
				
		        
		        if(0>=dto.getNroHijos())
		        	ps.setNull(13,Types.INTEGER);
		        else
		        	ps.setInt(13, dto.getNroHijos());
		        
		       
		        if(0>=dto.getOcupacionPaciente())
		          ps.setNull(14, Types.INTEGER);
		        	else
		          ps.setInt(14, dto.getOcupacionPaciente());
				
		        if(0>=dto.getPorcentajeDescuento())
		        ps.setNull(15, Types.DOUBLE);
		        else
		        ps.setDouble(15, dto.getPorcentajeDescuento());
				
		        
		        if(0>=dto.getValorDescuento())
		            ps.setNull(16, Types.NUMERIC);
		         else
		        	ps.setDouble(16, dto.getValorDescuento());
				
		       
		        
		        if(0>=dto.getPorcentajeHonorarios())
		        	ps.setNull(17, Types.NUMERIC);
		        else
		        ps.setDouble(17, dto.getPorcentajeHonorarios());
		        
		        
				
		        if(0>=dto.getValorHonorario())
		        	ps.setNull(18,Types.NUMERIC);
		        else
		        ps.setDouble(18, dto.getValorHonorario());
				
		        
		        ps.setString(19, dto.getFechaModificada());
				ps.setString(20, dto.getHoraModificada());
				ps.setString(21, dto.getUsuarioModifica());
				ps.setInt(22, dto.getDetPromocionOdo());
				
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
		 * Actualizar Tarjetas 
		 * @param dto
		 * @return true o false -- llegado el caso
		 */
		public static boolean modificar( DtoDetPromocionOdo dto )
		{
			
			
			
			boolean retorna=false;
			String consultaStr ="UPDATE odontologia.det_promociones_odo  set codigo_pk = codigo_pk ,"+
			"	promocion_odontologia = ? , "+ //1
			" 	region_centro_atencion  = ?, "+ //2
			"	pais_centro_atencion  = ?, "+ //3
			"	ciudad_centro_atencion  = ?, "+ //4
			"	depto_centro_atencion  = ?, "+ //5
			"	categoria_centro_atencion  = ?, "+ //6
			"	programa_odontologico  = ?, "+ //7
			"	edad_inicial  = ?, "+ //8
			"	edad_final = ?, "+ //9
			"	sexo  = ?,"+ //10
			"	estado_civil  = ?,  "+ //11
			"	nro_hijos  = ? , "+ //12
			"	ocupacion_paciente = ? ,"+ //13
			"	porcentaje_descuento = ? ,"+ //14
			"	valor_descuento  = ?, "+ //15
			"	porcentaje_honorario  = ?, "+ //16
			"	valor_honorario= ?, "+  //17
			"  	fecha_modifica = ?, "+	//18
			"	hora_modifica = ?  , "+ //19
			"	usuario_modifica = ? ," +//20
			"    servicio =  ?    "+//21
			"    where codigo_pk="+dto.getCodigoPk();       
			
		    logger.info("\n\n\n\n\n SQL  actualizar Detalle promociones Odontologicas / ");
	        
			try 
			{
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con,  consultaStr);
				

				
				ps.setInt(1, dto.getPromocionOdontologia());
				
				
				if(dto.getRegionCentroAtencion()>0)
				{
					ps.setDouble(2, dto.getRegionCentroAtencion());	
				}
				else
				{
					ps.setNull(2, Types.NUMERIC);
				}
				
				if(!UtilidadTexto.isEmpty(dto.getPaisCentroAtencion()))
				{
					if( dto.getPaisCentroAtencion().equals("-1"))
					{
						ps.setNull(3,Types.VARCHAR);
					}
					else
					{
						ps.setString(3, dto.getPaisCentroAtencion());
					}
				}
				else
				{
					ps.setNull(3, Types.VARCHAR);
				}
				
			
				
				if(UtilidadTexto.isEmpty(dto.getCiudadCentroAtencion()))
				{
					ps.setNull(4,Types.VARCHAR);//emun falta
				}
				else
				{	
					ps.setString(4, dto.getCiudadCentroAtencion());//emun falta
				}

				
				
				if(UtilidadTexto.isEmpty(dto.getDeptoCentroAtencion()))
					ps.setNull(5, Types.VARCHAR);
				else
					ps.setString(5, dto.getDeptoCentroAtencion());
					
				
				if(0>=dto.getCategoriaCentroAtencion())
				{
					ps.setNull(6,Types.NUMERIC );
				}
				else
					ps.setDouble(6, dto.getCategoriaCentroAtencion());
				
			
				
				if(0>=dto.getProgramaOdontologico().getCodigo())
				{
					ps.setNull(7, Types.NUMERIC);
				}
				else	
				ps.setDouble(7, dto.getProgramaOdontologico().getCodigo());
				
				
				if(0>=dto.getEdadInicial())
				{
					ps.setNull(8, Types.INTEGER);
				}
				else
					ps.setInt(8, dto.getEdadInicial());
				
				
				
				if(0>=dto.getEdadFinal())
				{
					ps.setNull(9,Types.INTEGER);
				}
			   	else
					ps.setInt(9, dto.getEdadFinal());
				
				
		        if(0>=dto.getSexo())
		        	ps.setNull(10, Types.INTEGER);
		        else
		        	ps.setInt(10, dto.getSexo());
		        
		        
		        if(UtilidadTexto.isEmpty(dto.getEstadoCivil()))
		            ps.setNull(11, Types.VARCHAR);
		        else
		        	ps.setString(11, dto.getEstadoCivil());
				
		        
		        if(0>=dto.getNroHijos())
		        	ps.setNull(12,Types.INTEGER);
		        else
		        	ps.setInt(12, dto.getNroHijos());
		        
		       
		        if(0>=dto.getOcupacionPaciente())
		          ps.setNull(13, Types.INTEGER);
		        	else
		          ps.setInt(13, dto.getOcupacionPaciente());
				
		        
		        if(0>=dto.getPorcentajeDescuento())
		        ps.setNull(14, Types.DOUBLE);
		        else
		        ps.setDouble(14, dto.getPorcentajeDescuento());
				
		        
		        if(0>=dto.getValorDescuento())
		            ps.setNull(15, Types.NUMERIC);
		         else
		        	ps.setDouble(15, dto.getValorDescuento());
				
		       
		        
		        if(dto.getPorcentajeHonorarios()<=0)
		        	ps.setNull(16, Types.NUMERIC);
		        else
		        ps.setDouble(16, dto.getPorcentajeHonorarios());
		        
		        
				
		        if(0>=dto.getValorHonorario())
		        	ps.setNull(17,Types.NUMERIC);
		        else
		        	ps.setDouble(17, dto.getValorHonorario());
				
		        
		        ps.setString(18, dto.getFechaModificada());
				ps.setString(19, dto.getHoraModificada());
				ps.setString(20, dto.getUsuarioModifica());
				
				
				
				if(dto.getServicio().getCodigo() > 0)
				{
					ps.setDouble(21, dto.getServicio().getCodigo());
					
				}
				else	
					ps.setNull(21, Types.NUMERIC);
				
				logger.info("*********************************************************************************************");
				logger.info("UPDATE PROMOCIONES ODONTOLOGICAS");
				logger.info("*********************************************************************************************");
				logger.info(ps+"\n\n\n\n\n");
				logger.info("*********************************************************************************************");
				
				if(ps.executeUpdate()>0)
				{
					SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null , con);
					return true;
				}
				
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			}
			catch (SQLException e) 
			{
				logger.error("ERROR EN updateDetalle  1 "+e);
			}
			return retorna;
		}	 
		
		
		
		
		
		/**
		 * 
		 * @param dto
		 * @return
		 */
		public static ArrayList<DtoDetPromocionOdo> cargar( DtoDetPromocionOdo dto) 
		{
			
			logger.info("Consulta Cargar Objeto-------------------------------------->");
			
			ArrayList<DtoDetPromocionOdo> arrayDto= new ArrayList<DtoDetPromocionOdo>();
			String consultaStr= "select codigo_pk as codigoPk , "+ //2
					"	dodo.promocion_odontologia as promocionOdontologica, "+ //3
					" 	dodo.region_centro_atencion as regionCentroAtencion, "+ //
						
					"(select ca.descripcion from  administracion.categorias_atencion ca where codigo=dodo.categoria_centro_atencion) as nombreCategoriaCentroAtencion ,"+
					
					"(select  rc.descripcion  from administracion.regiones_cobertura rc where rc.codigo=dodo.region_centro_atencion) as nombreRegionCentroAtencion ,"+	
					
					"(select p.descripcion  from  administracion.paises p where p.codigo_pais=dodo.pais_centro_atencion)  as nombrePaisCentroAtencion,"+
					
					"(select  c.descripcion from  administracion.ciudades c where c.codigo_ciudad=dodo.ciudad_centro_atencion and  c.codigo_departamento=dpo.depto_centro_atencion ) as nombreCiudadCentroAtencion, "+
					
					"	dodo.pais_centro_atencion as paisCentroAtencion, "+ //5
					"	dodo.ciudad_centro_atencion as ciudadCentroAtencion, "+ //6
					"	dodo.depto_centro_atencion as deptoCentroAtencion, "+ //7
					"	dodo.categoria_centro_atencion as categoriaCentroAtencion, "+
					"	dodo.servicio as servicio, "+
					"   facturacion.getnombreservicio(dodo.servicio ,0)  as nombre_servicio , " +		
					"	dodo.programa_odontologico as programaOdontologico, "+ //9
					"	dodo.edad_inicial as edadInicial, "+ //10
					"	dodo.edad_final as edadFinal, "+ //11
					"	dodo.sexo  as sexo, "+ //12
					"	dodo.estado_civil as estadoCivil,  "+ //13
					"	dodo.nro_hijos as nroHijos, "+ //14
					"	dodo.ocupacion_paciente as ocupacionPaciente,"+ //15
					"	dodo.porcentaje_descuento as porcentajeDescuento,"+ //16
					"	dodo.valor_descuento as valorDescuento, "+ //17
					"	dodo.porcentaje_honorario  as porcentajeHonorario, "+ //18
					"	dodo.valor_honorario   as valorHonorario, "+  //19 
					"  	dodo.fecha_modifica as fechaModifica, "+	//20
					"	dodo.hora_modifica  as horaModifica, "+ //21
					"	dodo.usuario_modifica as usuarioModifica, "+ //22
					"   coalesce(op.nombre,'') as nombrePrograma "+ //23
								"from " +
									"odontologia.det_promociones_odo dodo  " +
									"left outer join odontologia.programas op on(op.codigo = dodo.programa_odontologico) " +
								"where " +
									"1=1 ";
			
			
			 consultaStr+=(dto.getPromocionOdontologia()>0)?" AND  promocion_odontologia= "+dto.getPromocionOdontologia()+" ":"";
			 
			 consultaStr+= dto.getCodigoPk()>0?" AND codigo_pk = "+ dto.getCodigoPk(): " ";
			 /*
			 consultaStr+=(0<dto.getRegionCentroAtencion())?" AND  region_centro_atencion="+dto.getRegionCentroAtencion()+"" : "";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getPaisCentroAtencion())?"":" AND  pais_centro_atencion='"+dto.getPaisCentroAtencion()+"' ";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getCiudadCentroAtencion())?"": " AND  ciudad_centro_atencion='"+dto.getCiudadCentroAtencion()+"' ";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getDeptoCentroAtencion())?"": " ADANDN  depto_centro_atencion ='"+dto.getDeptoCentroAtencion()+"' ";
			 consultaStr+=(0<dto.getCategoriaCentroAtencion())?" AND categoria_centro_atencion = "+dto.getCategoriaCentroAtencion()+"":""; 
			 consultaStr+=(0<dto.getProgramaOdontologico().getCodigo())?" ADN programa_odontologico ="+dto.getProgramaOdontologico()+" ":"";
			 consultaStr+=(0<dto.getEdadInicial())?" AND edad_inicial="+dto.getEdadInicial() :""; 
			 consultaStr+=(0<dto.getEdadFinal())?" AND edad_final ="+dto.getEdadFinal():""; 
			 //consultaStr+=UtilidadTexto.isEmpty(dto.getSexo())?", sexo="+dto.getSexo() :""; 
			 consultaStr+= UtilidadTexto.isEmpty(dto.getEstadoCivil())?" ":" AND estado_civil ='"+dto.getEstadoCivil()+"'";
			 consultaStr+=(0<dto.getEdadFinal())?" AND nro_hijos="+dto.getNroHijos():""; 
			 consultaStr+=(0<dto.getOcupacionPaciente())?" AND ocupacion_paciente ="+dto.getOcupacionPaciente():"";
			 consultaStr+=(0<dto.getPorcentajeDescuento())?" AND porcentaje_descuento="+dto.getPorcentajeDescuento():"";
			 consultaStr+=(0<dto.getValorDescuento())?" AND valor_descuento="+dto.getValorDescuento():"";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getRequierePagoInicial())?"":" AND requiere_pago_inicial ='"+dto.getRequierePagoInicial()+"'";
			 consultaStr+=(0<dto.getPorcentajePagoInicial())?"AND  porcentaje_pago_inicial ="+dto.getPorcentajePagoInicial():"";
			 consultaStr+=(0<dto.getDiasMaxpagoInicial())?"AND dias_max_pago_inicial ="+dto.getDiasMaxpagoInicial(): "";
			 consultaStr+=(0<dto.getPorcentajeHonorarios())?" AND porcentaje_honorario ="+dto.getPorcentajeHonorarios():""; 
			 consultaStr+=(0<dto.getValorHonorario())?" AND valor_honorario ="+dto.getValorHonorario(): "";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModificada())?" ": "AND fecha_modifica ='"+dto.getFechaModificada()+"'";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?" ": "AND hora_modifica ='"+dto.getHoraModificada()+"'";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ": "AND usuario_modifica ='"+dto.getUsuarioModifica()+"'";
			*/
			
			 
			
	           logger.info("\n\n\n\n\n Detalle Promocion Odontologia --------> / "+consultaStr);
		     try 
			 {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_pk",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					
					DtoDetPromocionOdo newDto= new DtoDetPromocionOdo();
			
					newDto.setCodigoPk(rs.getInt("codigoPk"));
					newDto.setPromocionOdontologia(rs.getInt("promocionOdontologica"));
					newDto.setRegionCentroAtencion(rs.getDouble("regionCentroAtencion"));
					newDto.setPaisCentroAtencion(rs.getString("paisCentroAtencion"));
					newDto.setCiudadCentroAtencion(rs.getString("ciudadCentroAtencion")+"-"+rs.getString("deptoCentroAtencion"));
					newDto.setDeptoCentroAtencion(rs.getString("deptoCentroAtencion"));
					newDto.setCategoriaCentroAtencion(rs.getDouble("categoriaCentroAtencion"));
					newDto.getProgramaOdontologico().setCodigo(rs.getDouble("programaOdontologico"));
					newDto.getProgramaOdontologico().setNombre(rs.getString("nombrePrograma"));
					newDto.setEdadInicial(rs.getInt("edadInicial"));
					newDto.setEdadFinal(rs.getInt("edadFinal"));
					newDto.setSexo(rs.getInt("sexo"));
					newDto.setEstadoCivil(rs.getString("estadoCivil"));
					newDto.setNroHijos(rs.getInt("nroHijos"));
					newDto.setOcupacionPaciente(rs.getInt("ocupacionPaciente"));
					newDto.setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));
					newDto.setValorDescuento(rs.getDouble("valorDescuento"));
					newDto.setValorHonorario(rs.getDouble("valorHonorario"));
					newDto.setServicio(new InfoDatosDouble(rs.getDouble("servicio"),rs.getString("nombre_servicio")));
					newDto.setNombreCategoria(rs.getString("nombreCategoriaCentroAtencion"));
					newDto.setNombreRegion(rs.getString("nombreRegionCentroAtencion"));
					newDto.setNombreCiudad(rs.getString("nombreCiudadCentroAtencion"));
					newDto.setNombrePais(rs.getString("nombrePaisCentroAtencion"));
					arrayDto.add(newDto);
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
		
		public static DtoDetPromocionOdo cargarObjeto( DtoDetPromocionOdo dto) 
		{
			
			String consultaStr= "select codigo_pk as codigoPk , "+ //2
			"	dodo.promocion_odontologia as promocionOdontologica, "+ //3
			" 	dodo.region_centro_atencion as regionCentroAtencion, "+ //4
			"	dodo.pais_centro_atencion as paisCentroAtencion, "+ //5
			"	dodo.ciudad_centro_atencion as ciudadCentroAtencion, "+ //6
			"	dodo.depto_centro_atencion as deptoCentroAtencion, "+ //7
			"	dodo.categoria_centro_atencion as categoriaCentroAtencion, "+ //8
			"	dodo.programa_odontologico as programaOdontologico, "+ //9
			"	dodo.edad_inicial as edadInicial, "+ //10
			"	dodo.edad_final as edadFinal, "+ //11
			"	dodo.sexo  as sexo, "+ //12
			"	dodo.estado_civil as estadoCivil,  "+ //13
			"	dodo.servicio as servicio, "+
			"   facturacion.getnombreservicio(dodo.servicio ,0)  as nombre_servicio , " +	
			"	dodo.nro_hijos as nroHijos, "+ //14
			"	dodo.ocupacion_paciente as ocupacionPaciente,"+ //15
			"	dodo.porcentaje_descuento as porcentajeDescuento,"+ //16
			"	dodo.valor_descuento as valorDescuento, "+ //17
			"	dodo.porcentaje_honorario  as porcentajeHonorario, "+ //21
			"	dodo.valor_honorario   as valorHonorario, "+  //22   
			"  	dodo.fecha_modifica as fechaModifica, "+	//23
			"	dodo.hora_modifica  as horaModifica, "+ //24
			"	dodo.usuario_modifica as usuarioModifica, "+ //25
			"   coalesce(op.nombre,'') as nombrePrograma "+
						"from " +
							"odontologia.det_promociones_odo dodo  " +
							"left outer join odontologia.programas op on(op.codigo = dodo.programa_odontologico) " +
						"where " +
							"1=1 ";
			
			
			 consultaStr+=(0<dto.getPromocionOdontologia())?" AND  promocion_odontologia= "+dto.getPromocionOdontologia()+" ":"";
			 consultaStr+=(dto.getCodigoPk()> 0)?" AND codigo_pk = "+ dto.getCodigoPk():"";
			
	           logger.info("\n\n\n\n\n Detalle Promocion Odontologia --------> / "+consultaStr);
		    
	           DtoDetPromocionOdo newDto= new DtoDetPromocionOdo();
	           try 
			 {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_pk",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				{
					
			
					newDto.setCodigoPk(rs.getInt("codigoPk"));
					newDto.setPromocionOdontologia(rs.getInt("promocionOdontologica"));
					newDto.setRegionCentroAtencion(rs.getDouble("regionCentroAtencion"));
					newDto.setPaisCentroAtencion(rs.getString("paisCentroAtencion"));
					newDto.setCiudadCentroAtencion(rs.getString("ciudadCentroAtencion")+"-"+rs.getString("deptoCentroAtencion"));
					newDto.setDeptoCentroAtencion(rs.getString("deptoCentroAtencion"));
					newDto.setCategoriaCentroAtencion(rs.getDouble("categoriaCentroAtencion"));
					newDto.getProgramaOdontologico().setCodigo(rs.getDouble("programaOdontologico"));
					newDto.setEdadInicial(rs.getInt("edadInicial"));
					newDto.setEdadFinal(rs.getInt("edadFinal"));
					newDto.setSexo(rs.getInt("sexo"));
					newDto.setEstadoCivil(rs.getString("estadoCivil"));
					newDto.setNroHijos(rs.getInt("nroHijos"));
					newDto.setOcupacionPaciente(rs.getInt("ocupacionPaciente"));
					newDto.setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));
					newDto.setValorDescuento(rs.getDouble("valorDescuento"));
					newDto.setPorcentajeHonorarios(rs.getDouble("porcentajeHonorario"));
					newDto.setValorHonorario(rs.getDouble("valorHonorario"));
					newDto.getProgramaOdontologico().setNombre(rs.getString("nombrePrograma"));
					newDto.setServicio(new InfoDatosDouble(rs.getDouble("servicio"),rs.getString("nombre_servicio")));
					UtilidadLog.obtenerString(newDto, true);
				
				}
				
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			catch (SQLException e) 
			{
				logger.error("error en carga==> "+e);
			}
			
			return newDto;
		}
		
		
		/**
		 * 
		 * @param dto
		 * @return
		 */
		public static boolean eliminar( DtoDetPromocionOdo dto)
		{
			String consultaStr="DELETE FROM odontologia.tipos_tarj_cliente WHERE 1=1 ";

				consultaStr+=(0<dto.getCodigoPk())?" AND codigo_pk = "+dto.getCodigoPk():"";
			 
				consultaStr+=(0<dto.getPromocionOdontologia())?" AND  promocion_odontologia= "+dto.getPromocionOdontologia()+" ":"";
			 consultaStr+=(0<dto.getRegionCentroAtencion())?" AND region_centro_atencion="+dto.getRegionCentroAtencion()+"" : "";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getPaisCentroAtencion())?"":" AND pais_centro_atencion='"+dto.getPaisCentroAtencion()+"' ";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getCiudadCentroAtencion())?"": " AND ciudad_centro_atencion='"+dto.getCiudadCentroAtencion()+"' ";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getDeptoCentroAtencion())?"": " AND depto_centro_atencion ='"+dto.getDeptoCentroAtencion()+"' ";
			 consultaStr+=(0<dto.getCategoriaCentroAtencion())?" , categoria_centro_atencion = "+dto.getCategoriaCentroAtencion()+"":""; 
			 consultaStr+=(0<dto.getProgramaOdontologico().getCodigo())?" AND programa_odontologico ="+dto.getProgramaOdontologico()+" ":"";
			 consultaStr+=(0<dto.getEdadInicial())?" AND edad_inicial="+dto.getEdadInicial() :""; 
			 consultaStr+=(0<dto.getEdadFinal())?" AND edad_final ="+dto.getEdadFinal():""; 
			 //consultaStr+=UtilidadTexto.isEmpty(dto.getSexo())?", sexo="+dto.getSexo() :""; 
			 consultaStr+= UtilidadTexto.isEmpty(dto.getEstadoCivil())?" ":" AND estado_civil ='"+dto.getEstadoCivil()+"'";
			 consultaStr+=(0<dto.getEdadFinal())?" AND nro_hijos="+dto.getNroHijos():""; 
			 consultaStr+=(0<dto.getOcupacionPaciente())?" AND ocupacion_paciente ="+dto.getOcupacionPaciente():"";
			 consultaStr+=(0<dto.getPorcentajeDescuento())?" AND porcentaje_descuento="+dto.getPorcentajeDescuento():"";
			 consultaStr+=(0<dto.getValorDescuento())?" AND valor_descuento="+dto.getValorDescuento():"";
			 consultaStr+=(0<dto.getPorcentajeHonorarios())?" AND porcentaje_honorario ="+dto.getPorcentajeHonorarios():""; 
			 consultaStr+=(0<dto.getValorHonorario())?"	 AND valor_honorario ="+dto.getValorHonorario(): "";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModificada())?" ": "AND fecha_modifica ='"+dto.getFechaModificada()+"'";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?" ": "AND hora_modifica ='"+dto.getHoraModificada()+"'";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ": "AND usuario_modifica ='"+dto.getUsuarioModifica()+"'";
		    
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
				logger.error("ERROR EN eliminar Detalle Promocion  ");
				e.printStackTrace();
			}
			return false;
			
		}
		
		
		
		

}
