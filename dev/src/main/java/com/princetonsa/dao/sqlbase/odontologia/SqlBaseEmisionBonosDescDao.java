package com.princetonsa.dao.sqlbase.odontologia;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoPacienteBonoPresupuesto;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;
import com.princetonsa.dto.odontologia.administracion.DtoBonoDescuento;


import java.sql.Types;




public class SqlBaseEmisionBonosDescDao {

	
	private static Logger logger = Logger.getLogger(SqlBaseEmisionBonosDescDao.class);
	
	private static String InsertarStr= "insert into odontologia.emision_bonos_desc (" +
																			" codigo," +	//1
																			" convenio_patrocinador, " + //2
																			" id," + //3
																			" institucion," + //4
																			" serial_inicial," + //5
																 			" serial_final," + //6
																			" fecha_vigencia_inicial," + //7
																			" fecha_vigencia_final," + //8
																			" programa," + //9
																			" valor_descuento,"  + //10
																			" porcentaje_descuentos," +//11
																			" fecha_modifica," + //12
																			" hora_modifica," + //13
																			"usuario_modifica ," + //14
																			"servicio ) values " +//15
																			"" +
																			"(? , ? , ? , ? , ?" +  //5
																			" , ? , ? , ? , ? , ?" + //5
																			" , ? , ? , ?, ?, ?)" ; //4
		 
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoEmisionBonosDesc dto )
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{
		//Objetos de Conexion

			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_emision_bonos_desc"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , InsertarStr );
			
			logger.info("Secuecia-----------------------------------------------------------------> "+secuencia);
			logger.info("Numero convenio-----------------------------------------------------------------> "+dto.getConvenioPatrocinador().getCodigo());
			logger.info("ID-----------------------------------------------------------------> "+dto.getId());
			logger.info("Institucion-----------------------------------------------------------------> "+dto.getInstitucion());
			logger.info("Serial1 -----------------------------------------------------------------> "+ dto.getSerialInicial());
			logger.info("Serial2-----------------------------------------------------------------> "+dto.getSerialFinal());
			logger.info("Fecha1-----------------------------------------------------------------> "+dto.getFechaVigenciaInicialFormatoBD() );
			logger.info("Fecha2-----------------------------------------------------------------> "+dto.getFechaVigenciaFinal());
			logger.info("Programa-----------------------------------------------------------------> "+dto.getPrograma().getCodigo());
			logger.info("valor-----------------------------------------------------------------> "+dto.getValorDescuento());
			logger.info("Porcentaje-----------------------------------------------------------------> "+dto.getPorcentajeDescuento());
			logger.info("fechaM-----------------------------------------------------------------> "+dto.getFechaModifica());
			logger.info("HoraM-----------------------------------------------------------------> "+dto.getHoraModifica());
			logger.info("Usuam-----------------------------------------------------------------> "+dto.getUsuarioModifica());
			logger.info("servicio----------------------------"+dto.getServicio().getCodigo());
			
			
		
			ps.setDouble(1,secuencia);
			ps.setInt(2, dto.getConvenioPatrocinador().getCodigo());
			ps.setString(3,dto.getId());
			ps.setInt(4, dto.getInstitucion());
			ps.setBigDecimal(5, dto.getSerialInicial());
			ps.setBigDecimal(6, dto.getSerialFinal());
			ps.setString(7, dto.getFechaVigenciaInicialFormatoBD());
			ps.setString(8, dto.getFechaVigenciaFinalFormatoBD());
			
			if(dto.getPrograma().getCodigo()>0)
			{
				ps.setDouble(9, dto.getPrograma().getCodigo());
			}
			else
			{
				ps.setNull(9, Types.NUMERIC);	
				
			}
			
			
			
			if(dto.getValorDescuento()<=0)
				{
					ps.setNull(10, Types.NUMERIC);
				}
			else 
				{
				ps.setDouble(10, dto.getValorDescuento());
				}
			
			
			if(dto.getPorcentajeDescuento()<=0)
				{
				ps.setNull(11,Types.NUMERIC);//validar
				}
			else
				{
				ps.setDouble(11, dto.getPorcentajeDescuento());
				}
			
			ps.setString(12, dto.getFechaModifica());
			ps.setString(13, dto.getHoraModifica());
			ps.setString(14, dto.getUsuarioModifica());
			
			if(dto.getServicio().getCodigo()>0)
			{
				ps.setInt(15, dto.getServicio().getCodigo());
			}
			else
			{
				ps.setNull(15, Types.INTEGER);
			}
			
			
			logger.info("********************************************************************************");
			logger.info("***************************INSERTAR EMISION DE BONOS DE DESCUENTO ***************");
			logger.info("********************************************************************************");
			logger.info(ps);
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
		
	}//fin 
																		

    /**
     * 
     * @param dto
     * @return
     */
	public static boolean modificar( DtoEmisionBonosDesc dto )
	{
		boolean retorna=false;
		String consultaStr ="UPDATE odontologia.emision_bonos_desc set codigo=codigo ";
	   	logger.info("Modificar----------------------------------------------------------------------------------------");
		logger.info("Modificar----------------------------------------------------------------------------------------");
		logger.info("Modificar----------------------------------------------------------------------------------------");

		logger.info("Numero convenio-----------------------------------------------------------------> "+dto.getConvenioPatrocinador().getCodigo());
		logger.info("ID-----------------------------------------------------------------> "+dto.getId());
		logger.info("Institucion-----------------------------------------------------------------> "+dto.getInstitucion());
		logger.info("Serial1 -----------------------------------------------------------------> "+ dto.getSerialInicial());
		logger.info("Serial2-----------------------------------------------------------------> "+dto.getSerialFinal());
		logger.info("Fecha1-----------------------------------------------------------------> "+dto.getFechaVigenciaInicial() );
		logger.info("Fecha2-----------------------------------------------------------------> "+dto.getFechaVigenciaFinal());
		logger.info("Programa-----------------------------------------------------------------> "+dto.getPrograma().getCodigo());
		logger.info("valor-----------------------------------------------------------------> "+dto.getValorDescuento());
		logger.info("Porcentaje-----------------------------------------------------------------> "+dto.getPorcentajeDescuento());
		logger.info("fechaM-----------------------------------------------------------------> "+dto.getFechaModifica());
		logger.info("HoraM-----------------------------------------------------------------> "+dto.getHoraModifica());
		logger.info("Usuam-----------------------------------------------------------------> "+dto.getUsuarioModifica());
		
		consultaStr+=(0<dto.getConvenioPatrocinador().getCodigo())?" , convenio_patrocinador="+dto.getConvenioPatrocinador().getCodigo():"";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getId())?" ":" , id='"+dto.getId()+"'";
	    consultaStr+=(0<dto.getInstitucion())?" , institucion="+dto.getInstitucion():"";
	    consultaStr+=(0<dto.getSerialInicial().doubleValue())?" , serial_inicial="+dto.getSerialInicial():"";
	    consultaStr+=(0<dto.getSerialFinal().doubleValue())?" , serial_final="+dto.getSerialFinal():"";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaVigenciaInicialFormatoBD())?" ":" , fecha_vigencia_inicial='"+dto.getFechaVigenciaInicialFormatoBD()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaVigenciaFinalFormatoBD())?" ":" , fecha_vigencia_final='"+dto.getFechaVigenciaFinalFormatoBD()+"'";
	    consultaStr+=(0<dto.getPrograma().getCodigo())?"   , programa="+dto.getPrograma().getCodigo():"";
	    consultaStr+=(0<dto.getValorDescuento())?" , valor_descuento="+dto.getValorDescuento():" , valor_descuento=0";
	    consultaStr+=(0<dto.getPorcentajeDescuento())?" , porcentaje_descuentos="+dto.getPorcentajeDescuento():", porcentaje_descuentos=0";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" , fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":", hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" , usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getServicio().getCodigo()>0)?", servicio="+dto.getServicio().getCodigo(): "	";
	    consultaStr+=(dto.getCodigo()> 0)?" where codigo= "+ dto.getCodigo():"";
	    logger.info("***********************************************************************************************");
	    logger.info("******************************************MODIFICAR EMISION DE BONOS DE DESCUENTO *****************************************************");
	    logger.info("Update---------------"+consultaStr);
	    try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updateDetalleCargo 1 "+e);
		}
		return retorna;
			
	}
	
	
	
	
   /**
    * 
    * @param dto
    * @return
    */
	public static ArrayList<DtoEmisionBonosDesc> cargar( DtoEmisionBonosDesc dto){
		logger.info("*****************************************************************************************************************************");
		logger.info("***************************************CONSULTANDO EMISION DE BONO DE DESCUENTO************************************************");
		logger.info("****************************************************************************************************************************");
		ArrayList<DtoEmisionBonosDesc> arrayDto= new ArrayList<DtoEmisionBonosDesc>();
		String consultaStr=" select  ebd.codigo as codigo ," +	//1
											" ebd.convenio_patrocinador as convenioPatrocinador, " + //2
											"getnombreconvenio(convenio_patrocinador) as nombreConvenio,  " +//3
											" ebd.id as id ," + //3
											" ebd.institucion as institucion, " + //4
											" ebd.serial_inicial as serialInicial, " + //5
											" ebd.serial_final as serialFinal," + //6
											" to_char(ebd.fecha_vigencia_inicial, 'DD/MM/YYYY') as fechaVigenciaInicial," + //7
											" to_char(ebd.fecha_vigencia_final, 'DD/MM/YYYY') as fechaVigenciaFinal ," + //8
											" ebd.valor_descuento as valorDescuento,"  + //10
											" ebd.porcentaje_descuentos as porcentajeDescuento," +//11
											" ebd.fecha_modifica as fechaModifica," + //12
											" ebd.hora_modifica as  horaModifica ," + //13
											" ebd.usuario_modifica as usuarioModifica,  " + //14
											" ebd.programa as programa," + //9
											" (select nombre from  odontologia.programas where codigo=ebd.programa) as nombrePrograma ," +
											" ebd.servicio as servicio," +
											"getnombreservicio(ebd.servicio,"+ConstantesBD.codigoTarifarioCups+")  as nombreServicio , " +
				 							"getcodigoservicio(ebd.servicio, "+ConstantesBD.codigoTarifarioCups+") as  codigoMostrarServicio " +
				 							" from " +
											" odontologia.emision_bonos_desc ebd " +
											" where	1=1 ";
		
		
		
		
		logger.info("Institucion=------------------------------------------------------------------------- "+dto.getInstitucion());
		consultaStr+=(0<dto.getConvenioPatrocinador().getCodigo())?" AND convenio_patrocinador="+dto.getConvenioPatrocinador().getCodigo():"";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getId())?" ":" AND id='"+dto.getId()+"'";
	    consultaStr+=(0<dto.getInstitucion())?" AND ebd.institucion="+dto.getInstitucion():"";
	    consultaStr+=(0<dto.getSerialInicial().doubleValue())?" AND serial_inicial="+dto.getSerialInicial():"";
	    consultaStr+=(0<dto.getSerialFinal().doubleValue())?" AND serial_final="+dto.getSerialFinal():"";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaVigenciaInicialFormatoBD())?" ":" AND fecha_vigencia_inicial='"+dto.getFechaVigenciaInicialFormatoBD()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaVigenciaFinalFormatoBD())?" ":" AND fecha_vigencia_final='"+dto.getFechaVigenciaFinalFormatoBD()+"'";
	    consultaStr+=(0<dto.getPrograma().getCodigo())?"   AND programa="+dto.getPrograma().getCodigo():"";
	    consultaStr+=(0<dto.getValorDescuento())?" AND valor_descuento="+dto.getValorDescuento():"";
	    consultaStr+=(0<dto.getPorcentajeDescuento())?" AND porcentaje_descuentos="+dto.getPorcentajeDescuento():"";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    consultaStr+=(dto.getServicio().getCodigo()>0)?" servicio"+dto.getServicio().getCodigo():"";
	    consultaStr+="	order by fecha_vigencia_inicial ";
	   	
	    logger.info("\n\n\n\n\n SQL CARGAR BONOS DE DESCUNETO //////\n "+consultaStr);
	
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoEmisionBonosDesc newdto = new DtoEmisionBonosDesc();
				newdto.setCodigo(rs.getDouble("codigo"));
				newdto.getConvenioPatrocinador().setCodigo(rs.getInt("convenioPatrocinador"));
				newdto.getConvenioPatrocinador().setNombre(rs.getString("nombreConvenio"));
				newdto.setId(rs.getString("id"));
				newdto.setInstitucion(rs.getInt("institucion"));
				newdto.setSerialInicial(rs.getBigDecimal("serialInicial"));
				newdto.setSerialFinal(rs.getBigDecimal("serialFinal"));
				newdto.setValorDescuento(rs.getDouble("valorDescuento"));
				newdto.setPorcentajeDescuento(rs.getDouble("porcentajeDescuento"));
				newdto.setFechaVigenciaInicial(rs.getString("fechaVigenciaInicial"));
				newdto.setFechaVigenciaFinal(rs.getString("fechaVigenciaFinal"));
				newdto.getPrograma().setCodigo(rs.getDouble("programa"));
				newdto.getPrograma().setNombre(rs.getString("nombrePrograma"));
				newdto.setFechaModifica(rs.getString("fechaModifica"));
				newdto.setHoraModifica(rs.getString("horaModifica"));
				newdto.setUsuarioModifica(rs.getString("usuarioModifica"));
				newdto.getServicio().setCodigo(rs.getInt("servicio"));
				newdto.getServicio().setNombre(rs.getString("nombreServicio"));
				newdto.getServicio().setDescripcion(rs.getString("codigoMostrarServicio"));// CODIGO A MOSTRA
				logger.info("CONSULTA EMISIION DE BONOS");
				logger.info(ps);
				newdto.logger();
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
	    
          

	public static boolean eliminar( DtoEmisionBonosDesc dto)
	{
		
		String consultaStr="DELETE FROM odontologia.emision_bonos_desc WHERE 1=1 ";
	    
		consultaStr+=(dto.getCodigo()> 0)?" AND codigo= "+ dto.getCodigo():"";
		logger.info("Eliminar odontologia.seq_emision_bonos_desc"+consultaStr);
		/*
		consultaStr+=(0<dto.getConvenioPatrocinador().getCodigo())?" ":" AND convenio_patrocinador="+dto.getConvenioPatrocinador().getCodigo()+" ";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getId())?" ":"  AND id= '"+dto.getId()+"' ";
	    consultaStr+=(0<dto.getInstitucion())?" ":"  AND institucion="+dto.getInstitucion()+" ";
	    consultaStr+=(0<dto.getSerialInicial())?" ":"  AND serial_inicial="+dto.getSerialInicial()+" ";
	    consultaStr+=(0<dto.getSerialFinal())?" ":"  AND serial_final="+dto.getSerialFinal()+" ";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaVigenciaInicial())?" ":" AND fecha_vigencia_inicial='"+dto.getFechaVigenciaInicial()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaVigenciaFinal())?" ":" AND fecha_vigencia_final='"+dto.getFechaVigenciaInicial()+"'";
	    consultaStr+=(0<dto.getPrograma().getCodigo())?" ":" AND programa="+dto.getPrograma().getCodigo()+" ";
	    consultaStr+=(0<dto.getValorDescuento())?" ":" AND valor_descuento="+dto.getValorDescuento()+" ";
	    consultaStr+=(0<dto.getPorcentajeDescuento())?" ":" AND porcentaje_descuentos="+dto.getPorcentajeDescuento()+" ";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?" ":" AND fecha_modifica='"+dto.getFechaModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?" ":" AND hora_modifica='"+dto.getHoraModifica()+"'";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?" ":" AND usuario_modifica='"+dto.getUsuarioModifica()+"'";
	    */
	    
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
			logger.error("ERROR EN eliminarDetalleCargoXSolicitudServArt ");
			e.printStackTrace();
		}
			return false;
	}
	
	
	/**
	 *se puede repetir el id para un mismo convenio siempre y cuando los seriales sean diferentes 
	 */
	public static boolean existeCruceSerialesIdYConvenio(DtoEmisionBonosDesc dto, double codigoPkNotIn)
	{
		String consultaStr="SELECT codigo FROM odontologia.emision_bonos_desc WHERE convenio_patrocinador=? " +
							//comentado x tarea 151037 "and id=? " +
							"and institucion=? and( ("+dto.getSerialInicial()+" between serial_inicial and serial_final) or ("+dto.getSerialFinal()+" between serial_inicial and serial_final) )";
		
		if(codigoPkNotIn>0)
		{
			consultaStr+=" and codigo <> "+codigoPkNotIn;
		}
		
		logger.info(consultaStr);
		
		boolean retorna=false;
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dto.getConvenioPatrocinador().getCodigo());
			//ps.setString(2, dto.getId());
			ps.setInt(2, dto.getInstitucion());
			
			if(ps.executeQuery().next())
			{
				retorna=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR Seriales ");
			e.printStackTrace();
		}
		return retorna;
	}
	
	
	/**
	 *se puede repetir el id para un mismo convenio siempre y cuando los seriales sean diferentes 
	 */
	public static boolean existeCruceFechasConvenioPrograma(DtoEmisionBonosDesc dto, double codigoPkNotIn)
	{
		
		
		
		String consultaStr="SELECT codigo FROM odontologia.emision_bonos_desc WHERE convenio_patrocinador=? and programa=? and institucion=? and( ('"+dto.getFechaVigenciaInicialFormatoBD()+"' between to_char(fecha_vigencia_inicial, 'YYYY-MM-DD') and to_char(fecha_vigencia_final, 'YYYY-MM-DD')) or ('"+dto.getFechaVigenciaFinalFormatoBD()+"' between to_char(fecha_vigencia_inicial, 'YYYY-MM-DD') and to_char(fecha_vigencia_final, 'YYYY-MM-DD') ) )";
		
		
		if(codigoPkNotIn>0)
		{
			consultaStr+=" and codigo <> "+codigoPkNotIn;
		}
		
		logger.info("---------------Consultar existen fecha ----------------------------------");
		logger.info("---Institucion----"+dto.getInstitucion());
		logger.info("---Programa----"+dto.getPrograma().getCodigo());
		logger.info("---Convenio ----"+dto.getConvenioPatrocinador().getCodigo());
		
		
		
		logger.info(consultaStr);
		
		boolean retorna=false;
		try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dto.getConvenioPatrocinador().getCodigo());
			ps.setDouble(2, dto.getPrograma().getCodigo());
			ps.setInt(3, dto.getInstitucion());
			
			if(ps.executeQuery().next())
			{
				retorna=true;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR fechas ");
			e.printStackTrace();
		}
		return retorna;
	}


	public static boolean esSerialVigenteDisponible(Connection con, Integer bono, int codigoConvenio) {
		String sentenciaVigenciaSerial=
						"SELECT " +
							"count(1) AS resultados " +
						"FROM " +
							"odontologia.emision_bonos_desc " +
						"WHERE " +
								"? BETWEEN serial_inicial AND serial_final " +
							"AND " +
								"CURRENT_DATE BETWEEN " +
									"fecha_vigencia_inicial " +
								"AND " +
									"fecha_vigencia_final " +
							"AND " +
								"convenio_patrocinador=?";

		try{
		
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaVigenciaSerial);
			psd.setInt(1, bono);
			psd.setInt(2, codigoConvenio);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=false;
			if(rsd.next())
			{
				if(rsd.getInt("resultados")>0)
				{
					String sentenciaUtilizado=
							"SELECT " +
								"count(1) AS resultados " +
							"FROM " +
								"manejopaciente.sub_cuentas " +
							"WHERE " +
								"bono=?";
					PreparedStatementDecorator psd2=new PreparedStatementDecorator(con, sentenciaUtilizado);
					psd2.setInt(1, bono);
					ResultSetDecorator rsd2=new ResultSetDecorator(psd2.executeQuery());
					if(rsd2.next())
					{
						resultado=rsd2.getInt("resultados")==0;
					}
				}
			}
			rsd.close();
			psd.close();
			return resultado;
		}
		catch (SQLException e) {
			logger.info("error verificando validez del bono odontológico: "+bono, e);
		}
		
		return false;
	}
	
	/**
	 * Método que valida si un serial está vigente o disponible
	 * @param con
	 * @param bono
	 * @param codigoConvenio
	 * @param dtoSubcuentas Dto para verificar el serial diferente al asignado inicialmente
	 * @return Mensaje de error, "" en caso de no existir error
	 */
	public static String esSerialVigenteDisponibleMensajeError(Connection con, Integer bono, int codigoConvenio, DtoSubCuentas dtoSubcuentas) {

		String sentenciaVigenciaSerial=
			"SELECT " +
				"count(1) AS resultados " +
			"FROM " +
				"odontologia.emision_bonos_desc " +
			"WHERE " +
					"? BETWEEN serial_inicial AND serial_final " +
			"AND " +
					"convenio_patrocinador=? ";
		
		String error="";
				
		try{
		   
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaVigenciaSerial);
			psd.setInt(1, bono);
			psd.setInt(2, codigoConvenio);
			logger.info(" \n\n Consulta Es Serial Vigente "+psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=false;
			if(rsd.next())
			{
				if(rsd.getInt("resultados")>0)
				{
					logger.info(" Es Serial Valido ");	
					sentenciaVigenciaSerial=
						"SELECT " +
							"count(1) AS resultados " +
						"FROM " +
							"odontologia.emision_bonos_desc " +
						"WHERE " +
								"? BETWEEN serial_inicial AND serial_final " +
							"AND " +
								"CURRENT_DATE BETWEEN " +
									"fecha_vigencia_inicial " +
								"AND " +
									"fecha_vigencia_final " +
							"AND " +
								"convenio_patrocinador=?";

					PreparedStatementDecorator psd1=new PreparedStatementDecorator(con, sentenciaVigenciaSerial);
					psd1.setInt(1, bono);
					psd1.setInt(2, codigoConvenio);
					logger.info("\n\n Consulta Fechas Vigentes "+psd1);
					ResultSetDecorator rsd1=new ResultSetDecorator(psd1.executeQuery());
					if(rsd1.next())
					{
						if(rsd1.getInt("resultados")>0)
						{	
							logger.info("  ");
							String sentenciaUtilizado=
									"SELECT " +
										"count(1) AS resultados " +
									"FROM " +
										"manejopaciente.sub_cuentas " +
									"WHERE " +
										"bono=? " +
									"AND" +
										" convenio=?" +
									"AND " +
										"sub_cuenta != ?";
							PreparedStatementDecorator psd2=new PreparedStatementDecorator(con, sentenciaUtilizado);
							psd2.setInt(1, bono);
							psd2.setInt(2, codigoConvenio);
							psd2.setInt(3,Utilidades.convertirAEntero(dtoSubcuentas.getSubCuenta()));
							
							logger.info("validacion 2->"+psd2);
							
							ResultSetDecorator rsd2=new ResultSetDecorator(psd2.executeQuery());
							if(rsd2.next())
							{
								resultado=rsd2.getInt("resultados")>0;
								if(resultado)
								{
									error="error.odontologia.serialAsignado";
								}
							}
							UtilidadBD.cerrarObjetosPersistencia(psd2, rsd2, null);

						}
						else{
							error="error.odontologia.serialNoVigente";
						}
					}
					UtilidadBD.cerrarObjetosPersistencia(psd1, rsd1, null);
				}
				else
				{
					error="error.odontologia.noExisteBono";
				}
			}
			UtilidadBD.cerrarObjetosPersistencia(psd, rsd, null);
			return error;
		}
		catch (SQLException e) {
			logger.info("error verificando validez del bono odontológico: "+bono, e);
			return "errors.problemasBd";
		}
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @param aplicaProgramas
	 * @return
	 */
	public static ArrayList<InfoPacienteBonoPresupuesto> consultaEmisionBonos(InfoPacienteBonoPresupuesto dto, boolean aplicaProgramas)
	{
		
		
		logger.info(" \n\n\n\n	MODIFICAR EMISION DE BONOS 	\n\n\n\n");
		ArrayList<InfoPacienteBonoPresupuesto> lista = new ArrayList<InfoPacienteBonoPresupuesto>();
		
		String consultaStr="";
		
		consultaStr="select DISTINCT   "+                                      
		"p.primer_nombre as primerNombre ,"+  
		"p.segundo_nombre as segundoNombre , "+
		"p.primer_apellido as primerApellido, "+
		"p.segundo_apellido as segundoApellido ,  "+
		"coalesce(pre.codigo_pk,0) as codigoPresupuesto ,"+ 
		"ebd2.codigo   as codigoBono , " +
		"sc.bono as bono,"+
		"coalesce(pre.codigo_pk,0) as codigoPkPresupuesto,"+ 
		"coalesce( pre.fecha_modifica||'', '') as fechaContratacion ,"+   
		"ing.consecutivo as consecutivoIngreso ,"+ 
		"manejoPaciente.getFechaIngreso(pre.cuenta ,pre.ingreso) as fechaIngreso "+ 
		"from "+
		"administracion.personas p "+ 
		"INNER JOIN manejopaciente.sub_cuentas sc ON(p.codigo=sc.codigo_paciente)"; 
		
		if( aplicaProgramas)
		{
			consultaStr+=" INNER JOIN odontologia.emision_bonos_desc ebd2  ON(ebd2.codigo="+dto.getCodigoPkBono()+" and sc.bono between ebd2.serial_inicial and ebd2.serial_final AND ebd2.programa="+dto.getCodigoPrograma()+") ";
		}
		else
		{
			consultaStr+=" INNER JOIN odontologia.emision_bonos_desc ebd2  ON(ebd2.codigo="+dto.getCodigoPkBono()+" and sc.bono between ebd2.serial_inicial and ebd2.serial_final AND ebd2.servicio="+dto.getCodigoServcio()+") ";
		}
		consultaStr+="" +
		
		" INNER JOIN  manejopaciente.ingresos ing ON (sc.ingreso=ing.id)"+ 
		" LEFT OUTER JOIN odontologia.presupuesto_odontologico pre ON (pre.codigo_paciente=p.codigo and pre.estado='"+ConstantesIntegridadDominio.acronimoContratadoContratado+"' ) "+  
		
		" LEFT OUTER JOIN odontologia.presupuesto_odo_prog_serv pops ON (pre.codigo_pk=pops.presupuesto)"+ 
		" LEFT OUTER JOIN  odontologia.presupuesto_odo_convenio poc ON(poc.presupuesto_odo_prog_serv=pops.codigo_pk and poc.serial_bono=sc.bono and poc.contratado='"+ConstantesBD.acronimoSi+"') " +
				"" +
		" WHERE 1=1 ";

		
		
		
		
		/*
		
		
		String consultaStr="";
		
		
		consultaStr = " select p.primer_nombre as primerNombre ,  p.segundo_nombre as segundoNombre ,  p.primer_apellido as primerApellido, p.segundo_apellido as segundoApellido , " +
					" pre.codigo_pk as codigoPresupuesto , " +
					"(select ebd2.codigo from   odontologia.emision_bonos_desc ebd2 where poc.serial_bono between ebd2.serial_inicial and ebd2.serial_final";
		
		consultaStr+=dto.getCodigoConvenio()>0?" AND ebd2.convenio_patrocinador="+dto.getCodigoConvenio()+" ": "";
		
		if( aplicaProgramas)
		{
			consultaStr+=dto.getCodigoPrograma()>0?" AND ebd2.programa="+dto.getCodigoPrograma()+" ) as codigoBono ,": " ) as codigoBono ,";
		}
		else
		{
			consultaStr+=dto.getCodigoServcio()>0?"  AND  ebd2.servicio="+dto.getCodigoServcio()+"  ) as codigoBono , ": " ) as codigoBono, ";
		}
				
				
		consultaStr+= 
					" pre.codigo_pk as codigoPkPresupuesto, pre.fecha_modifica as fechaContratacion ,  " +
					" ing.consecutivo as consecutivoIngreso ," +
					" manejoPaciente.getFechaIngreso(pre.cuenta ,pre.ingreso) as fechaIngreso " +
					" from odontologia.presupuesto_odontologico pre INNER JOIN " +
					" manejopaciente.ingresos ing ON (pre.ingreso=ing.id) INNER JOIN" +
					" odontologia.presupuesto_odo_prog_serv pops ON (pre.codigo_pk=pops.presupuesto) INNER JOIN " +
					" odontologia.presupuesto_odo_convenio poc ON(poc.presupuesto_odo_prog_serv=pops.codigo_pk) INNER JOIN " +
					" administracion.personas p ON(p.codigo=pre.codigo_paciente) where pre.estado='"+ConstantesIntegridadDominio.acronimoContratadoContratado+"' and poc.serial_bono>0  ";
		
		*/
		
		//consultaStr+=dto.getCodigoConvenio()>0? " AND poc.convenio="+dto.getCodigoConvenio() : "";
		//consultaStr+="AND poc.contratado='"+ConstantesBD.acronimoSi+"'";
		
		consultaStr+= (!UtilidadTexto.isEmpty(dto.getPrimerNombre()))?" AND  UPPER(p.primer_nombre) LIKE  UPPER('%"+dto.getPrimerNombre()+"%')": "";
		consultaStr+= (!UtilidadTexto.isEmpty(dto.getSegundoNombre()))?" AND  UPPER(p.segundo_nombre) LIKE UPPER('%"+dto.getSegundoNombre()+"%')" : "";
		consultaStr+= (!UtilidadTexto.isEmpty(dto.getPrimerApellido()))?" AND  UPPER(p.primer_Apellido) LIKE UPPER ('%"+dto.getPrimerApellido()+"%')" : "";
		consultaStr+= (!UtilidadTexto.isEmpty(dto.getSegundoApellido()))?" AND  UPPER(p.segundo_Apellido) LIKE UPPER ('%"+dto.getSegundoApellido()+"%')" : "";
		consultaStr+= dto.getBono().doubleValue()>0?"  AND sc.bono="+dto.getBono() : " ";
			
		//consultaStr+=" group by sc.bono";
			logger.info(" \n\n\n\n\n\n 	CONSULTA EMISION DE BONOS ++++" );
			logger.info(" Sql CONSULTA---->"+ consultaStr);
		
		try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				InfoPacienteBonoPresupuesto dtoN = new InfoPacienteBonoPresupuesto();
				dtoN.setCodigoPkBono(rs.getDouble("codigoBono"));
				dtoN.setPrimerNombre(rs.getString("primerNombre"));
				dtoN.setSegundoNombre(rs.getString("segundoNombre"));
				dtoN.setPrimerApellido(rs.getString("primerApellido"));
				dtoN.setSegundoApellido(rs.getString("segundoApellido"));
				dtoN.setCodigoPresupuesto(rs.getDouble("codigoPkPresupuesto")); 
				dtoN.setNumeroIngreso(rs.getInt("consecutivoIngreso"));
				dtoN.setFechaPresupuesto(rs.getString("fechaContratacion"));
				dtoN.setFechaIngreso(rs.getString("fechaIngreso"));
				dtoN.setBono(rs.getBigDecimal("bono"));
				
				lista.add(dtoN);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
	    
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return lista;
	}
	
	/**
	 * 
	 * @param serialInicial
	 * @param serialFinal
	 * @return
	 */
	public static boolean existeSerialSubCuentas(BigDecimal serialInicial , BigDecimal serialFinal, int convenio)
	{
		boolean retorna= false;
		
			String consultaStr=" select count(0) as cantidad from  manejopaciente.sub_cuentas where bono between "+serialInicial+" and "+serialFinal+" and convenio="+convenio+"  " ;
			logger.info("LA CONSULTA PARA VERIFICAR Q LOS BONOS NO SE ESTEN USANDO----->"+consultaStr);
			try 
			 {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consultaStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					if(rs.getInt("cantidad")>0)
					{
						retorna=true;
					}
					else
					{
						retorna=false;
					}
				}
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			}
			catch (SQLException e) 
			{
				logger.error("error en carga==> "+e);
			}
			
			
	 
		return retorna;
	}

	/**
	 * Marcar los bonos como utilizados = 'S' para que no sean utilizados de nuevo en el paciente.
	 * Adicionalmente genera una relación entre el programa donde se utilizó y el bono.
	 * @param con Conexión con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de hacer la modificación correctamente
	 */
	public static boolean marcarBonoUtilizado(Connection con, DtoBonoDescuento bono)
	{
		String sentenciaSQL="UPDATE bonos_conv_ing_pac WHERE codigo_pk=?";
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaSQL);
		try{
			psd.setInt(1, bono.getBonoPaciente());
			Log4JManager.info("Sentencia "+psd);
			return true;
		}catch (SQLException e) {
			Log4JManager.error("Error actualizando el bono del paciente", e);
		}
		finally
		{
			try
			{
				psd.close();
			} catch (SQLException e)
			{
				Log4JManager.error("Error cerrando el prepared statement", e);
			}
		}
		return false;
	}


	/**
	 * Generar una relación entre el programa donde se utilizó y el bono.
	 * @param con Conexión con la BD (Abierta)
	 * @param bono {@link DtoBonoDescuento} Bono que se desea asociar
	 * @return true en caso de generar la relación correctamente
	 */
	public static boolean asociarBonoProgramaConvenio(Connection con, DtoBonoDescuento bono)
	{
		String sentencia="";
		
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		
		
		try
		{
			psd.setInt(1, bono.getBonoPaciente());
		} catch (SQLException e)
		{
			Log4JManager.error("Error relacionando el bono del paciente",e);
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
}