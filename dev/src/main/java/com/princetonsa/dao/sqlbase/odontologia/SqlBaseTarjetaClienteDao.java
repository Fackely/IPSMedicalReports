package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoFiltroReporteIngresosTarjetasCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta;
import com.servinte.axioma.dto.odontologia.ventaTarjeta.DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta.DtoInfoVentaTarjeta;

/**
 * 
 * @author axioma
 *
 */
public class SqlBaseTarjetaClienteDao  
{
    private static Logger logger = Logger.getLogger( SqlBaseTarjetaClienteDao.class);
	
	/**
	 * CADENA para insertar 	 
	 * */
   	private static String insertarTarjetaCliente=" INSERT INTO odontologia.tipos_tarj_cliente (" +
																								"codigo_pk , " +			//1
																								"codigo_tipo_tarj , " +		//2
																								"institucion , " +			//3
																								"nombre , " +				//4
																								"aliado , " +				//5
																								"convenio , " +				//6
																								"servicio_personal, "+		//7
																								"fecha_modifica, " +		//8
																								"hora_modifica , " +		//9
																								"usuario_modifica, " +		//10
																								" servicio_empresarial , "+		//11	
																								"servicio_familiar, "+		//12	
																								"num_beneficiarios_fam, "+		//13
																								"consecutivo_serial "+		//14
																								
																								") values ( " +
																								"? , ? , ? , ? , ? , " +//5
																								"? , ? , ? , ? , ? , " +//10
																								"?,  ? ,  ?,  ? ) " ;//14
   	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar( DtoTarjetaCliente dto) 
	{
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	    
		try 
		{
			//Objetos de Conexion
			Connection con = UtilidadBD.abrirConexion();   
			ResultSetDecorator rs = null;
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_tipos_tarj_cliente"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarTarjetaCliente ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("Secuencia-->"+secuencia+ "Codigo: "+dto.getCodigoTipoTarj()+ " CodInts: "+dto.getInstitucion()+ " Nombre: " +dto.getNombre()  
			+ " ServicioEmpresarial --> "+ dto.getServicioEmpresarial().getCodigo()+ " Alidado--> "+ dto.getAliado()+ " Usuario--> "+ dto.getUsuarioModifica() + 
			"ServicioPersona--> "+dto.getServicioPersonal().getCodigo()+
			"ServicioFamiliar--> "+dto.getServicioFamiliar().getCodigo()+
			"Beneficiarios-->"+dto.getNumBeneficiariosFam()+ "                "+ insertarTarjetaCliente);
			
			ps.setDouble(1, secuencia);
			ps.setString(2, dto.getCodigoTipoTarj());
			ps.setInt(3, dto.getInstitucion());
			ps.setString(4, dto.getNombre());
			ps.setString(5, dto.getAliado());//emun falta
			ps.setInt(6, dto.getConvenio().getCodigo());
			
			if(dto.getServicioPersonal().getCodigo()>0)
			{	
				ps.setInt(7, dto.getServicioPersonal().getCodigo());
			}
			else
			{
				ps.setNull(7, Types.INTEGER);
			}
			ps.setString(8, dto.getFechaModificadaFormatoBD());
		    ps.setString(9, dto.getHoraModificada());
		    ps.setString(10, dto.getUsuarioModifica());
		    if(dto.getServicioEmpresarial().getCodigo()>0)
		    {
		    	ps.setInt(11, dto.getServicioEmpresarial().getCodigo());
			 }
		    else
		    {
		    	ps.setNull(11, Types.INTEGER);
		    }	
		   
		    if(dto.getServicioFamiliar().getCodigo()>0)
		    {
		    	logger.info("entro error ");
		    	ps.setInt(12, dto.getServicioFamiliar().getCodigo());
			}
		    else 
		    {
		    	ps.setNull(12, Types.INTEGER);
			}
		    if(dto.getNumBeneficiariosFam()>0)
		    {
		    	ps.setInt(13, dto.getNumBeneficiariosFam());
		    }
		    else
		    {
		    	ps.setNull(13, Types.INTEGER);
		    }
		    
		    if(dto.getConsecutivoSerial()>0)
		    {
		    	ps.setInt(14, dto.getConsecutivoSerial());
		    }
		    else
		    {
		    	ps.setNull(14, Types.INTEGER);
		    }
		    
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
	public static boolean modificar( DtoTarjetaCliente dto )
	{
		boolean retorna=false;
		String consultaStr ="UPDATE odontologia.tipos_tarj_cliente set codigo_pk=codigo_pk ";
		
		consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoTipoTarj())?"":" , codigo_tipo_tarj ='"+dto.getCodigoTipoTarj()+"'";
		consultaStr+=(0<dto.getInstitucion())?" , institucion= "+dto.getInstitucion():" ";
		consultaStr+= UtilidadTexto.isEmpty(dto.getNombre())?"":" , nombre ='"+dto.getNombre()+"'";
		consultaStr+= UtilidadTexto.isEmpty(dto.getAliado())?"":", aliado= '"+dto.getAliado()+"'";
		consultaStr+=(0<dto.getConvenio().getCodigo())?" , convenio ="+dto.getConvenio().getCodigo():"";
		consultaStr+= UtilidadTexto.isEmpty(dto.getFechaModificada())?"":" , fecha_modifica ='"+dto.getFechaModificadaFormatoBD()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?"":"  , hora_modifica= '"+dto.getHoraModificada()+"' ";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?"":" , usuario_modifica= '"+dto.getUsuarioModifica()+"'";
		
		if(dto.getAliado().equals(ConstantesBD.acronimoSi))
		{
			consultaStr+= ", servicio_familiar=null, servicio_personal=null, servicio_empresarial=null, num_beneficiarios_fam =null  ";    
		}
		else
		{	
			consultaStr+=(0<dto.getServicioFamiliar().getCodigo())?", servicio_familiar="+dto.getServicioFamiliar().getCodigo():" , servicio_familiar=null ";
			consultaStr+=(0<dto.getServicioPersonal().getCodigo())?", servicio_personal="+dto.getServicioPersonal().getCodigo():" , servicio_personal= null ";
			consultaStr+=(0<dto.getNumBeneficiariosFam())?", num_beneficiarios_fam = "+dto.getNumBeneficiariosFam():"  ,  num_beneficiarios_fam = null ";
			if(dto.getEsModificableConsecutivoSerial().equals(ConstantesBD.acronimoSi))
			{
				consultaStr+=(0<dto.getConsecutivoSerial())?", consecutivo_serial= "+dto.getConsecutivoSerial():"  ,  consecutivo_serial = null ";
			} 
			consultaStr+=(0<dto.getServicioEmpresarial().getCodigo())?", servicio_empresarial="+dto.getServicioEmpresarial().getCodigo():" , servicio_empresarial=null ";
			
		}	
		
		consultaStr+=(dto.getCodigoPk()> 0)?" where codigo_pk = "+ dto.getCodigoPk():"";
		
		logger.info("\n\n\n\n\n SQL  actualizar TarjetaCliente / "+consultaStr);
		
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
	public static ArrayList<DtoTarjetaCliente> cargar( DtoTarjetaCliente dto) 
	{
		ArrayList<DtoTarjetaCliente> arrayDto= new ArrayList<DtoTarjetaCliente>();
		String consultaStr=
							"SELECT " +
								"codigo_pk, " +
								"codigo_tipo_tarj, " +
								"institucion, " +
								"nombre, " +
								"aliado, " +
								"convenio , " +
								"getnombreconvenio(convenio) as nombre_convenio ," +
								"coalesce(servicio_personal, "+ConstantesBD.codigoNuncaValido+") as servicio_personal, " +
								"case when servicio_personal is null then '' else getcodigopropservicio(servicio_personal, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") ||' - '|| getnombreservicio(servicio_personal, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombre_servicio_personal, " +
								"coalesce(servicio_empresarial, "+ConstantesBD.codigoNuncaValido+") as servicio_empresarial, " +
								"case when servicio_empresarial is null then '' else getcodigopropservicio(servicio_empresarial, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") ||' - '|| getnombreservicio(servicio_empresarial, " +ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombre_servicio_empresarial, " +
								"coalesce(servicio_familiar, "+ConstantesBD.codigoNuncaValido+") as servicio_familiar, " +
								"case when servicio_familiar is null then '' else getcodigopropservicio(servicio_familiar, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+ ")   ||' - '|| getnombreservicio(servicio_familiar, "+ ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombre_servicio_familiar, "+
								"to_char(fecha_modifica, 'dd/mm/yyyy') as fecha_modifica, " +
								"hora_modifica , " +
								"usuario_modifica, " +
								"coalesce(num_beneficiarios_fam, "+ConstantesBD.codigoNuncaValido+") as num_beneficiarios_fam, " +
								"coalesce(consecutivo_serial, "+ConstantesBD.codigoNuncaValido+") as consecutivo_serial " +
							"FROM " +
								"odontologia.tipos_tarj_cliente  " +
							"WHERE " +
								"1=1 ";
      
		 consultaStr+=(0<dto.getCodigoPk())?" AND codigo_pk = "+dto.getCodigoPk():"";
		 consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoTipoTarj())?"":" AND codigo_tipo_tarj ='"+dto.getCodigoTipoTarj()+"'";
         consultaStr+=(0<dto.getInstitucion())?" AND institucion= "+dto.getInstitucion():"";
         consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?"":" AND nombre ='"+dto.getNombre()+"'";
         consultaStr+=UtilidadTexto.isEmpty(dto.getAliado())?"":" AND aliado ='"+dto.getAliado()+"'";
         consultaStr+=(0< dto.getConvenio().getCodigo())?" AND convenio= "+dto.getConvenio().getCodigo():"";
         consultaStr+=(0<dto.getServicioEmpresarial().getCodigo())?" AND servicio_empresarial= "+dto.getServicioEmpresarial().getCodigo():"";
         consultaStr+=(0<dto.getServicioPersonal().getCodigo())?" AND servicio_personal= "+dto.getServicioPersonal().getCodigo():"";
         consultaStr+=(0<dto.getServicioFamiliar().getCodigo())?" AND servicio_familiar= "+dto.getServicioFamiliar().getCodigo():"";
         consultaStr+= UtilidadTexto.isEmpty(dto.getFechaModificada())?"":" AND fecha_modifica ='"+dto.getFechaModificada()+"' ";
	     consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?"":" AND hora_modifica= '"+dto.getHoraModificada()+"' ";
	     consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?"":" AND usuario_modifica= '"+dto.getUsuarioModifica()+"' ";
	     
	     
	  
	     logger.info("\n\n\n\n\n SQL cargarTarjetaCliente / "+consultaStr);
	     try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_tipo_tarj ASC",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				 DtoTarjetaCliente dtoTarjetaCliente= new DtoTarjetaCliente();
				 dtoTarjetaCliente.setCodigoPk(rs.getDouble("codigo_pk"));
				 dtoTarjetaCliente.setCodigoTipoTarj(rs.getString("codigo_tipo_tarj"));
				 dtoTarjetaCliente.setInstitucion(rs.getInt("institucion"));
				 dtoTarjetaCliente.setNombre(rs.getString("nombre"));
				 dtoTarjetaCliente.setAliado(rs.getString("aliado"));
				 dtoTarjetaCliente.setConvenio(new InfoDatosInt(rs.getInt("convenio"), rs.getString("nombre_convenio")));
				 dtoTarjetaCliente.getConvenio().setDescripcion(dtoTarjetaCliente.getConvenio().getNombre());
				 dtoTarjetaCliente.setServicioPersonal(new InfoDatosInt(rs.getInt("servicio_personal"), rs.getString("nombre_servicio_personal")));
				 dtoTarjetaCliente.setFechaModificada(rs.getString("fecha_modifica"));
			     dtoTarjetaCliente.setHoraModificada(rs.getString("hora_modifica"));
			     dtoTarjetaCliente.setServicioEmpresarial(new InfoDatosInt(rs.getInt("servicio_empresarial"),rs.getString("nombre_servicio_empresarial") ));
			     dtoTarjetaCliente.setServicioFamiliar(new InfoDatosInt(rs.getInt("servicio_familiar"), rs.getString("nombre_servicio_familiar")));
			     dtoTarjetaCliente.setNumBeneficiariosFam(rs.getInt("num_beneficiarios_fam"));
			     dtoTarjetaCliente.setConsecutivoSerial(rs.getInt("consecutivo_serial"));
			     dtoTarjetaCliente.setEsModificableConsecutivoSerial(esModificableConsecutivoSerial(rs.getDouble("codigo_pk"))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			     arrayDto.add(dtoTarjetaCliente);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> ",e);
		}
		
		return arrayDto;
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoTarjetaCliente> cargarConvenio( DtoTarjetaCliente dto,boolean modificar) 
	{
		
		logger.info(UtilidadLog.obtenerStringHerencia(dto, true));
		logger.info("********************************************************************************************************");
		logger.info("**************************************	VALIDAR CONVENIOS ***********************************************");
		
		ArrayList<DtoTarjetaCliente> arrayDto= new ArrayList<DtoTarjetaCliente>();
		String consultaStr= "select " +
								"codigo_pk, " +
								"codigo_tipo_tarj, " +
								"institucion, " +
								"nombre, " +
								"aliado, " +
								"convenio , " +
								"getnombreconvenio(convenio) as nombre_convenio ," +
								"coalesce(servicio_personal, "+ConstantesBD.codigoNuncaValido+") as servicio_personal, " +
								"case when servicio_personal is null then '' else getcodigopropservicio(servicio_personal, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") ||' - '|| getnombreservicio(servicio_personal, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombre_servicio_personal, " +
								"coalesce(servicio_empresarial, "+ConstantesBD.codigoNuncaValido+") as servicio_empresarial, " +
								"case when servicio_empresarial is null then '' else getcodigopropservicio(servicio_empresarial, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") ||' - '|| getnombreservicio(servicio_empresarial, " +ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombre_servicio_empresarial, " +
								"coalesce(servicio_familiar, "+ConstantesBD.codigoNuncaValido+") as servicio_familiar, " +
								"case when servicio_familiar is null then '' else getcodigopropservicio(servicio_familiar, "+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+ ")   ||' - '|| getnombreservicio(servicio_familiar, "+ ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion())+") end as nombre_servicio_familiar, "+
								"to_char(fecha_modifica, 'dd/mm/yyyy') as fecha_modifica, " +
								"hora_modifica , " +
								"usuario_modifica, " +
								"coalesce(num_beneficiarios_fam, "+ConstantesBD.codigoNuncaValido+") as num_beneficiarios_fam, " +
								"coalesce(consecutivo_serial, "+ConstantesBD.codigoNuncaValido+") as consecutivo_serial " +
							"from " +
								"odontologia.tipos_tarj_cliente  " +
							"where " +
								"1=1 ";
								if(modificar)
								{
									consultaStr+=" and codigo_pk <> "+dto.getCodigoPk()+" and convenio="+dto.getConvenio().getCodigo();
								}
								else
								{
									consultaStr+=" and convenio="+dto.getConvenio().getCodigo();
								}
	  
	     logger.info("\n\n\n\n\n SQL cargarTarjetaCliente / "+consultaStr);
	     try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				 DtoTarjetaCliente dtoTarjetaCliente= new DtoTarjetaCliente();
				 dtoTarjetaCliente.setCodigoPk(rs.getDouble("codigo_pk"));
				 dtoTarjetaCliente.setCodigoTipoTarj(rs.getString("codigo_tipo_tarj"));
				 dtoTarjetaCliente.setInstitucion(rs.getInt("institucion"));
				 dtoTarjetaCliente.setNombre(rs.getString("nombre"));
				 dtoTarjetaCliente.setAliado(rs.getString("aliado"));
				 dtoTarjetaCliente.setConvenio(new InfoDatosInt(rs.getInt("convenio"), rs.getString("nombre_convenio")));
				 dtoTarjetaCliente.setServicioPersonal(new InfoDatosInt(rs.getInt("servicio_personal"), rs.getString("nombre_servicio_personal")));
				 dtoTarjetaCliente.setFechaModificada(rs.getString("fecha_modifica"));
			     dtoTarjetaCliente.setHoraModificada(rs.getString("hora_modifica"));
			     dtoTarjetaCliente.setServicioEmpresarial(new InfoDatosInt(rs.getInt("servicio_empresarial"),rs.getString("nombre_servicio_empresarial") ));
			     dtoTarjetaCliente.setServicioFamiliar(new InfoDatosInt(rs.getInt("servicio_familiar"), rs.getString("nombre_servicio_familiar")));
			     dtoTarjetaCliente.setNumBeneficiariosFam(rs.getInt("num_beneficiarios_fam"));
			     dtoTarjetaCliente.setConsecutivoSerial(rs.getInt("consecutivo_serial"));
			     dtoTarjetaCliente.setEsModificableConsecutivoSerial(esModificableConsecutivoSerial(rs.getDouble("codigo_pk"))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			     
			     arrayDto.add(dtoTarjetaCliente);
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
	public static boolean eliminar( DtoTarjetaCliente dto)
	{
		String consultaStr="DELETE FROM odontologia.tipos_tarj_cliente WHERE 1=1 ";

		consultaStr+=(0<dto.getCodigoPk())?" AND codigo_pk = "+dto.getCodigoPk():"";
        consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoTipoTarj())?"":" AND codigo_tipo_tarj ='"+dto.getCodigoTipoTarj()+"'";
        consultaStr+=(0<dto.getInstitucion())?" AND institucion= "+dto.getInstitucion():"";
        consultaStr+=UtilidadTexto.isEmpty(dto.getNombre())?"":" AND  nombre ='"+dto.getNombre()+"'";
        consultaStr+=UtilidadTexto.isEmpty(dto.getAliado())?"":" AND  aliado ='"+dto.getAliado()+"'";
        consultaStr+=(0< dto.getConvenio().getCodigo())?" AND convenio= "+dto.getConvenio().getCodigo():"";
        consultaStr+=(0<dto.getServicioEmpresarial().getCodigo())?" AND servicio_empresarial= "+dto.getServicioEmpresarial().getCodigo():"";
        consultaStr+=(0<dto.getServicioPersonal().getCodigo())?" AND servicio_personal= "+dto.getServicioPersonal().getCodigo():"";
        consultaStr+=(0<dto.getServicioFamiliar().getCodigo())?" AND servicio_familiar= "+dto.getServicioFamiliar().getCodigo():"";
        consultaStr+= UtilidadTexto.isEmpty(dto.getFechaModificada())?"":" AND fecha_modifica ='"+dto.getFechaModificada()+"' ";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModificada())?"":"  AND hora_modifica= '"+dto.getHoraModificada()+"' ";
	    consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioModifica())?"":" AND usuario_modifica= '"+dto.getUsuarioModifica()+"' ";
	    
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
	 * 
	 * @param contrato
	 * @return
	 */
	public static int obtenerEsquemaTarifarioTarjetaCliente(double contrato) 
	{
		int codigoEsquemaTarifario= ConstantesBD.codigoNuncaValido;
		String consultaStr=" SELECT " +
								"etpc.esquema_tarifario " +
							"FROM " +
								"inventarios.esq_tar_procedimiento_contrato etpc " +
							"where " +
								"etpc.fecha_vigencia <= CURRENT_DATE " +
								"AND etpc.contrato=? " +
							"order by etpc.fecha_vigencia desc ";
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, contrato);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				codigoEsquemaTarifario= rs.getInt(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("\n\nobtenerEsquemaTarifarioTarjetaCliente----> "+consultaStr);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN nobtenerEsquemaTarifarioTarjetaCliente "+e);
		}
		return codigoEsquemaTarifario;
	}
	
	
	/**
	 * 
	 * @param codigoPkTarjetaCliente
	 * @return
	 */
	public static int obtenerContratoTarjetaCliente(double codigoPkTarjetaCliente) 
	{
		int codigoContrado= ConstantesBD.codigoNuncaValido;
		
		String consultaStr=" SELECT " +
								"c.codigo  as codigoContrato " +
							"FROM " +
								"odontologia.tipos_tarj_cliente ttc " +
								"INNER JOIN facturacion.view_contratos_vigentes vcv ON (ttc.convenio=vcv.convenio) " +
								"INNER JOIN facturacion.contratos c ON(c.codigo=vcv.codigo) " +
								"INNER JOIN inventarios.esq_tar_procedimiento_contrato etpc ON (etpc.contrato=c.codigo) " +
							"where " +
								"etpc.fecha_vigencia <= CURRENT_DATE " +
								"and ttc.codigo_pk=? " +
							"order by etpc.fecha_vigencia desc ";
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consultaStr);
			ps.setDouble(1, codigoPkTarjetaCliente);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				codigoContrado= rs.getInt(1);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("*************************************************************************************************************");
			logger.info("**********************************************	CODIGO CONTRATO	***************************************************************");
			logger.info("*************************************************************************************************************");
			logger.info("\n\n Codigo Contrato----> "+ps);
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN nobtenerEsquemaTarifarioTarjetaCliente "+e);
		}
		return codigoContrado;
	}
	
	
	/**
	 * 
	 * @param codigoPkTarjetaCliente
	 * @return
	 */
	public static boolean esModificableConsecutivoSerial(Double codigoPkTarjetaCliente)
	{
	    boolean esModificable=true;
	    
	    String consultaStr= " SELECT ttc1.codigo_pk " +
	    		            "   FROM odontologia.tipos_tarj_cliente  ttc1 " +
		                    "   INNER JOIN odontologia.enca_emi_tarjeta_cliente emitarj ON (emitarj.tipo_tarjeta=ttc1.codigo_pk) " +
		                    "   WHERE  ttc1.codigo_pk = ? " +
		                    " UNION   " +
		                    " SELECT ttc2.codigo_pk " +
	    		            "   FROM odontologia.tipos_tarj_cliente  ttc2 " +
		                    "   INNER JOIN odontologia.venta_tarjeta_cliente  ventarjcl ON(ventarjcl.tipo_tarjeta=ttc2.codigo_pk) " +
		                    "   WHERE  ttc2.codigo_pk = ? " ;
	        
		                    
		                    try 
		            		{
		            			Connection con = UtilidadBD.abrirConexion();
		            			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consultaStr);
		            			ps.setDouble(1, codigoPkTarjetaCliente);
		            			ps.setDouble(2, codigoPkTarjetaCliente);
		            			logger.info(ps);
		            			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
		            			
		            			if(rs.next())
		            			{
		            				esModificable=false;
		            			}
		            			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		            		} 
							catch (SQLException e) 
							{
								logger.error("ERROR EN esModificableConsecutivoSerial "+e);
							}	
							    
								return esModificable;
	 }

	
	/**
	 * 
	 */
	private static String cadenaConsultaReporte="SELECT " +
									" coalesce(ei.codigo,-1) as codigoempresainstitucion," +
									" coalesce(ei.razon_social,'') as descripcionempresainstitucion," +
									" ca.codigo as codigoca," +
									" ca.consecutivo as consecutivoca," +
									" ca.descripcion as descripcionca," +
									" ca.ciudad as codigociudad," +
									" ciu.descripcion as descripcionciudad," +
									" d.codigo_departamento as codigodepartamento," +
									" d.descripcion as descripciondepartamento," +
									" p.codigo_pais as codigopais," +
									" p.descripcion as descripcionpais," +
									" rc.codigo as codigoregion," +
									" rc.descripcion as descripcionregion," +
									" vtc.tipo_tarjeta as tipotarjeta," +
									" tt.nombre as nombretipotarjeta," +
									" case when vtc.tipo_venta ='PER' then 1 else case when vtc.tipo_venta ='AFAM' then 2 else 3 end end as orden," +
									" vtc.tipo_venta as tipoventa," +
									" btc.serial as serialinicial," +
									" btc.serial+vtc.cantidad-1 as serialfinal," +
									" fv.consecutivo as consecutivofactura," +
									" vtc.cantidad as cantidad," +
									" to_char(vtc.fecha_venta,'dd/mm/yyyy') as fechaventa," +
									" getnombreusuario(vtc.usuario_vendedor) as usuarioventa, " +
									" conv.nombre as nombreconvenio," +
									" vtc.valor_total_tarjetas as valortotaltarjetas," +
									" per.primer_nombre as primernombre," +
									" per.segundo_nombre as segundonombre," +
									" per.primer_apellido as primerapellido," +
									" per.segundo_apellido as segundoapellido, " +
									" sex.nombre as sexoComprador, " +
									" per.fecha_nacimiento as fechaNcto " +									
					" from venta_tarjeta_cliente vtc " +
					" inner join facturas_varias fv on (vtc.codigo_fac_var=fv.codigo_fac_var) " +
					" inner join centro_atencion ca on (fv.centro_atencion=ca.consecutivo) " +
					" inner join ciudades ciu on (ciu.codigo_ciudad=ca.ciudad and ciu.codigo_departamento=ca.departamento and ciu.codigo_pais=ca.pais)  " +
					" inner join departamentos d on (ca.departamento=d.codigo_departamento)  " +
					" inner join paises p on(p.codigo_pais=ca.pais)  " +
					" inner join regiones_cobertura rc on (ca.region_cobertura=rc.codigo) " +
					" inner join tipos_tarj_cliente tt on (tt.codigo_pk=vtc.tipo_tarjeta)  " +
					" inner join contratos c on (c.codigo=vtc.codigo_contrato) " +
					" inner join convenios conv on (conv.codigo=c.convenio) " +
					" inner join deudores deu on (deu.codigo=vtc.deudor) " +
					" inner join personas per on (deu.codigo_paciente=per.codigo) " +
					" left join sexo sex on (per.sexo=sex.codigo) " +
					" left outer join beneficiario_tarjeta_cliente btc on(btc.venta_tarjeta_cliente=vtc.codigo_pk and btc.indicativo_principal='"+ConstantesBD.acronimoSi+"') " +
					" left outer join empresas_institucion ei on(ca.empresa_institucion=ei.codigo)";

	
	private static String orderConsultaReporte=" order by " +
													" ei.codigo," +
													" ei.razon_social," +
													" ca.codigo," +
													" ca.consecutivo," +
													" ca.descripcion," +
													" ca.ciudad," +
													" ciu.descripcion," +
													" d.codigo_departamento," +
													" d.descripcion," +
													" p.codigo_pais," +
													" p.descripcion," +
													" rc.codigo," +
													" rc.descripcion," +
													" orden," +
													" tt.nombre," +
													" vtc.tipo_venta," +
													" serialinicial";
	
	
	/**
	 * 
	 * @param codigoPkTarjetaCliente
	 * @return
	 */
	public static ArrayList<DtoResultadoReporteVentaTarjetas> consultarDatosReporte(DtoFiltroReporteIngresosTarjetasCliente dtoFiltro)
	{

		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoResultadoReporteVentaTarjetas> resultado=new ArrayList<DtoResultadoReporteVentaTarjetas>();
		try
		{
			String consulta=cadenaConsultaReporte+construirFiltro(dtoFiltro)+orderConsultaReporte;
			PreparedStatementDecorator ps1=new PreparedStatementDecorator(con,consulta);
			Log4JManager.info("consulta\n"+ps1);
			ResultSetDecorator rs1=new ResultSetDecorator(ps1.executeQuery());
			int codigoEmpresaInstitucionAnterior=-2;
			int codigoCentroAtencionAnterior=-2;
			int codigoTipoTarjetaAnterior=-2;
			String tipoVentaAnterior="";
			while(rs1.next())
			{
				DtoResultadoReporteVentaTarjetas dto;
				DtoCentroAtencionReporte dtoCA;
				DtoTiposTarjetas dtoTT;
				DtoClaseVentaTarjeta dtoCV;
				
				//Cargando la info de empresa institucion
				if(rs1.getInt("codigoempresainstitucion")!=codigoEmpresaInstitucionAnterior)
				{
					dto=new DtoResultadoReporteVentaTarjetas();
					dto.setCodigoEmpresaInstitucion(rs1.getInt("codigoempresainstitucion"));
					dto.setDescripcionEmpresaInstitucion(rs1.getString("descripcionempresainstitucion"));
					dto.setCentrosAtencion(new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte>());
					resultado.add(dto);
					
					//si es una nueva empresa, se deben inicializar las variables de anteriores
					codigoCentroAtencionAnterior=-2;
					codigoTipoTarjetaAnterior=-2;
					tipoVentaAnterior="";
				}
				codigoEmpresaInstitucionAnterior=rs1.getInt("codigoempresainstitucion");
				dto=resultado.get(resultado.size()-1);
					
				//cargar el centro atencion.
				if(codigoCentroAtencionAnterior!=rs1.getInt("consecutivoca"))
				{
					dtoCA=dto.new DtoCentroAtencionReporte();
					dtoCA.setCodigoCentroAtencion(rs1.getString("codigoca"));
					dtoCA.setConsecutivoCentroAtencion(rs1.getInt("consecutivoca"));
					dtoCA.setDescripcionCentroAtencion(rs1.getString("descripcionca"));
					dtoCA.setCodigoCiudadCA(rs1.getInt("codigociudad"));
					dtoCA.setDescripcionCiudadCA(rs1.getString("descripcionciudad"));
					dtoCA.setCodigoDepartamento(rs1.getInt("codigodepartamento"));
					dtoCA.setDescripcionDepartamento(rs1.getString("descripciondepartamento"));
					dtoCA.setCodigoPais(rs1.getInt("codigopais"));
					dtoCA.setDescripcionPais(rs1.getString("descripcionpais"));
					dtoCA.setCodigoRegionCA(rs1.getInt("codigoregion"));
					dtoCA.setDescripcionRegionCA(rs1.getString("descripcionregion"));
					dtoCA.setTiposTarjetas(new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas>());
					dto.getCentrosAtencion().add(dtoCA);

					//si es un nuevo centro atencion, se deben inicializar las variables de anteriores
					codigoTipoTarjetaAnterior=-2;
					tipoVentaAnterior="";
				}
				codigoCentroAtencionAnterior=rs1.getInt("consecutivoca");
				dtoCA=dto.getCentrosAtencion().get(dto.getCentrosAtencion().size()-1);
				
				//cargar tipos tarjeta
				if(codigoTipoTarjetaAnterior!=rs1.getInt("tipotarjeta"))
				{
					dtoTT=dtoCA.new DtoTiposTarjetas();
					dtoTT.setCodigoTipoTarjeta(rs1.getInt("tipotarjeta"));
					dtoTT.setDescripcionTipoTarjeta(rs1.getString("nombretipotarjeta"));
					dtoTT.setClaseVentaTarjeta(new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta>());
					dtoCA.getTiposTarjetas().add(dtoTT);
					tipoVentaAnterior="";
				}
				codigoTipoTarjetaAnterior=rs1.getInt("tipotarjeta");
				dtoTT=dtoCA.getTiposTarjetas().get(dtoCA.getTiposTarjetas().size()-1);
				

				//Cargar tipo venta de la tarjeta
				if(!tipoVentaAnterior.equals(rs1.getString("tipoventa")))
				{
					dtoCV=dtoTT.new DtoClaseVentaTarjeta();
					dtoCV.setClaseTarjeta(rs1.getString("tipoventa"));
					dtoCV.setInfoVentaTarjeta(new ArrayList<DtoResultadoReporteVentaTarjetas.DtoCentroAtencionReporte.DtoTiposTarjetas.DtoClaseVentaTarjeta.DtoInfoVentaTarjeta>());
					dtoTT.getClaseVentaTarjeta().add(dtoCV);
				}
				tipoVentaAnterior=rs1.getString("tipoventa");
				dtoCV=dtoTT.getClaseVentaTarjeta().get(dtoTT.getClaseVentaTarjeta().size()-1);
				
				
				//Cargar informacion de la venta.
				DtoInfoVentaTarjeta dtoIVT=dtoCV.new DtoInfoVentaTarjeta();
				dtoIVT.setSerialInicial(rs1.getString("serialinicial"));
				dtoIVT.setSerialFinal(rs1.getString("serialfinal"));
				dtoIVT.setNroFactura(rs1.getString("consecutivofactura"));
				dtoIVT.setCantidad(rs1.getInt("cantidad"));
				dtoIVT.setFechaVenta(rs1.getString("fechaventa"));
				dtoIVT.setUsuarioVendedor(rs1.getString("usuarioventa"));
				dtoIVT.setConvenioTarifa(rs1.getString("nombreconvenio"));
				dtoIVT.setValorTotalVenta(rs1.getDouble("valortotaltarjetas"));
				dtoIVT.setPrimerNombreComprador(rs1.getString("primernombre"));
				dtoIVT.setSegundoNombreComprador(rs1.getString("segundonombre"));
				dtoIVT.setPrimerApellidoComprador(rs1.getString("primerapellido"));
				dtoIVT.setSegundoApellidoComprador(rs1.getString("segundoapellido"));
				dtoIVT.setSexoComprador(rs1.getString("sexoComprador"));
				dtoIVT.setEdadComprador(rs1.getString("fechaNcto"));
				dtoCV.getInfoVentaTarjeta().add(dtoIVT);
			}
			rs1.close();
			ps1.close();
		}
		catch (SQLException e)
		{
			Log4JManager.error("error", e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
		
	}







	private static String construirFiltro(
			DtoFiltroReporteIngresosTarjetasCliente dtoFiltro) {
		String cadena=" where 1=1 ";
		
		if(!UtilidadTexto.isEmpty(dtoFiltro.getCodigoPaisResidencia()))
			cadena=cadena+" and ca.pais='"+dtoFiltro.getCodigoPaisResidencia()+"'";
		if(!UtilidadTexto.isEmpty(dtoFiltro.getCodigoPais()))
			cadena=cadena+" and ca.pais='"+dtoFiltro.getCodigoPais()+"'";
		if(!UtilidadTexto.isEmpty(dtoFiltro.getCodigoCiudad()) && 
				!dtoFiltro.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido + ""))
			cadena=cadena+" and ca.ciudad='"+dtoFiltro.getCodigoCiudad()+"'";
		if(!UtilidadTexto.isEmpty(dtoFiltro.getCodigoDpto())&& 
				!dtoFiltro.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido + ""))
			cadena=cadena+" and ca.departamento='"+dtoFiltro.getCodigoDpto()+"'";
		if(dtoFiltro.getCodigoRegion()>0)
			cadena=cadena+" and rc.codigo="+dtoFiltro.getCodigoRegion();
		if(dtoFiltro.getCodigoEmpresaInstitucion()>0)
			cadena=cadena+" and ca.empresa_institucion="+dtoFiltro.getCodigoEmpresaInstitucion();
		if(dtoFiltro.getConsecutivoCentroAtencion()>0)
			cadena=cadena+" and ca.consecutivo="+dtoFiltro.getConsecutivoCentroAtencion();
		
		if(!UtilidadTexto.isEmpty(dtoFiltro.getUsuarioVendedor()) &&
				!dtoFiltro.getUsuarioVendedor().trim().equals(ConstantesBD.codigoNuncaValido + ""))
		{
			cadena=cadena+" and vtc.usuario_vendedor='"+dtoFiltro.getUsuarioVendedor()+"'";
		}
		if(!UtilidadTexto.isEmpty(dtoFiltro.getClaseVenta()))
		{
			cadena=cadena+" and vtc.tipo_venta='"+dtoFiltro.getClaseVenta()+"'";
			
		}
		if(dtoFiltro.getTipoTarjeta()>0)
		{
			cadena=cadena+" and vtc.tipo_tarjeta="+dtoFiltro.getTipoTarjeta();
		}
		if(dtoFiltro.getCodigoConvenio()>0)
		{
			cadena=cadena+" and conv.codigo="+dtoFiltro.getCodigoConvenio();
		}
		if(dtoFiltro.getFechaInicial() != null && dtoFiltro.getFechaFinal() != null)
		{
			cadena=cadena+" and vtc.fecha_venta between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaFinal())+"'";
		}
		if(!UtilidadTexto.isEmpty(dtoFiltro.getConsecutivoInicial())&&!UtilidadTexto.isEmpty(dtoFiltro.getConsecutivoFinal()))
		{
			cadena=cadena+" and fv.consecutivo between "+dtoFiltro.getConsecutivoInicial()+" and "+dtoFiltro.getConsecutivoFinal();
		}
		
		if(dtoFiltro.getRangoEdadInicial() != null && dtoFiltro.getRangoEdadFinal() != null)
		{
			
			String fechaActual = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) ;
			
			if (dtoFiltro.getRangoEdadInicial().trim().equals(dtoFiltro.getRangoEdadFinal())) {				
				cadena=cadena+" and trunc((to_date('" + fechaActual+"', 'yyyy-mm-dd') - per.fecha_nacimiento)/365) = '"+ dtoFiltro.getEdadInicial()+"'";
			} else {
				cadena=cadena+" and trunc((to_date('" + fechaActual+"', 'yyyy-mm-dd') - per.fecha_nacimiento)/365) between '"+dtoFiltro.getEdadInicial()+"' and '"+dtoFiltro.getEdadFinal()+"'";
			}
			
		}
		
		int sexoComprador = Integer.parseInt(dtoFiltro.getSexoComprador());
		
		if(sexoComprador > 0)
		{
			cadena=cadena+" and per.sexo = '"+dtoFiltro.getSexoComprador()+"'";
		}
		
		cadena=cadena+"and vtc.valor_total_tarjetas > 0";
		
		
		return cadena;
	}


}