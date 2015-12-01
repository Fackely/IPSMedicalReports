package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;


/**
 * 
 * @author axioma
 *
 */
public class SqlBaseBeneficiariosTarjetaCli  
{
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseBeneficiariosTarjetaCli.class);
	
		
	/**
	 * 
	 */
	private static String insertarStr= "insert into odontologia.beneficiario_tarjeta_cliente (" +
																			"codigo_pk,"+  //1
																			"venta_tarjeta_cliente,"+ //2 
																			"serial,"+ //3
																			"indicativo_principal,"+ //4 
																			"tipo_indentificacion,"+  //5
																			"numero_identificacion,"+//6
																			"primer_apellido,"+//7
																			"segundo_apellido,"+//8
																			"primer_nombre,"+ //9
																			"segundo_nombre ,"+//10
																			"parentezco ,"+//11
																			"estado_tarjeta,"+//12 
																			"indicador_alidado ,"+//13 
																			"alidado_odontologico ,"+//14
																			"tipo_tarjeta_cliente  ,"+//15
																			"observaciones ,"+//16
																			"fecha_modifica ,"+//17
																			"hora_modifica ,"+//18
																			"usuario_modifica," + //19
																			"institucion, " +//20
																			"consecutivo," + //21
																			"num_tarjeta "+//22
																			 " )values "+ 
																			"(? , ? , ? , ? , ? , ? , ? , ? , ? , ? ," + //10
																			 " ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? )" ; //22
		 
	

	/**
	 * 
	 * @param tipoIdentificacion
	 * @param numeroIdenficiacion
	 * @return
	 */
	public static HashMap consultar(String tipoIdentificacion, String numeroIdenficiacion )
	{
		
		 String consultaIdBeneficiarios="SELECT tipo_indentificacion AS tipoId, numero_identificacion AS numId " +
												"FROM  odontologia.beneficiario_tarjeta_cliente where tipo_indentificacion <>  " +
												"'"+tipoIdentificacion+"' AND numero_identificacion='"+numeroIdenficiacion+"'" ;
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
	
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			ps= new PreparedStatementDecorator(con,consultaIdBeneficiarios);
			logger.info("\n\nConsulta Beneficiarios>>>>>>>>> "+ps);				
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);						
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO ENCABEZADO GLOSA POR GLOSA SISTEMA------>>>>>>"+e);
			e.printStackTrace();
		}
	
		return resultados;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(Connection con, DtoBeneficiarioCliente dto )
	{
		
		/*
		
		logger.info("CONSECUTIVO "+dto.getConsecutivo());
		//logger.info("LO QUE VA ENTRAR"+UtilidadLog.obtenerString(dto, true));
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
		int cosecutivoSerial=0;
		int codigoTarjetaCliente=0;
		boolean actualizarSerial= false;
		try 
		{
			//Objetos de Conexion
			
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_benef_tar_client"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, insertarStr );
			
			ps.setDouble(1,secuencia);
			if(dto.getVentaTarjetaCliente() > 0)
			{
				ps.setDouble(2, dto.getVentaTarjetaCliente());
			}
			else
			{
				ps.setNull(2, Types.DOUBLE);
				
		    }
		    	
			if(UtilidadTexto.isEmpty(dto.getSerial()) && dto.getTipoTarjetaCliente().getCodigoPk().intValue()>0)
			{ 
				cosecutivoSerial=obtenerConsecutivoSerialTarjetaCliente(dto.getTipoTarjetaCliente().getCodigoPk().intValue(), con);	
				
				if(cosecutivoSerial>0)		    	
				{
					ps.setDouble(3, Utilidades.convertirADouble(cosecutivoSerial+""));
					actualizarSerial=true;
				}
				else
				{
					ps.setNull(3, Types.DOUBLE);		    	
				}
			}else
			{
				cosecutivoSerial=Utilidades.convertirAEntero(dto.getSerial()+"");
				ps.setDouble(3, Utilidades.convertirADouble(dto.getSerial()));
			}
			
			
			if(!UtilidadTexto.isEmpty(dto.getIndicativoPrincipal()))
			{
				ps.setString(4,dto.getIndicativoPrincipal());
			}
			else
			{
				ps.setNull(4, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(dto.getTipoIndentificacion()))
			{	
      			ps.setString(5, dto.getTipoIndentificacion());
      		}
      		else
      		{
      			ps.setNull(5, Types.VARCHAR);
      		}
      		
      		if(!UtilidadTexto.isEmpty(dto.getNumeroIdentificacion()))
      		{	
      			ps.setString(6, dto.getNumeroIdentificacion());
      		}
      		else
      		{
      			ps.setNull(6, Types.VARCHAR);
      		}
      		
      		if(!UtilidadTexto.isEmpty(dto.getPrimerApellido()))
      		{	
      			ps.setString(7, dto.getPrimerApellido());
      		}
      		else
      		{
      			ps.setNull(7, Types.VARCHAR);
      		}
      		
      		if(!UtilidadTexto.isEmpty(dto.getSegundoApellido()))
      		{	
      			ps.setString(8, dto.getSegundoApellido());
      		}
      		else
      		{
      			ps.setNull(8, Types.VARCHAR);
      		}
      		
      		if(!UtilidadTexto.isEmpty(dto.getPrimerNombre()))
      		{	
      			ps.setString(9, dto.getPrimerNombre());
      		}
      		else
      		{
      			ps.setNull(9, Types.VARCHAR);
      		}
      		
      		if(!UtilidadTexto.isEmpty(dto.getSegundoNombre()))
      		{	
      			ps.setString(10, dto.getSegundoNombre());
      		}
      		else
      		{
      			ps.setNull(10, Types.VARCHAR);
      		}
      		
      		logger.info("dto.getParentezco()-->"+dto.getParentezco());
      		
      		if(dto.getParentezco() > 0)
      		{
      			ps.setInt(11, dto.getParentezco());
      		}
      		else
      		{
      			ps.setNull(11, Types.CHAR);
      		}
      		
      		if(UtilidadTexto.isEmpty(dto.getEstadoTarjeta()))
      			ps.setNull(12, Types.VARCHAR);
      		else
      			ps.setString(12, dto.getEstadoTarjeta());
      		
      		ps.setString(13, dto.getIndicadorAlidado());
      		
      		if(dto.getAlidadoOdontologico().getCodigo()>0)
      		{	
      			ps.setDouble(14, dto.getAlidadoOdontologico().getCodigo());
      		}
      		else
      		{
      			ps.setNull(14, Types.DOUBLE);
      		}
			
      		if(dto.getTipoTarjetaCliente().getCodigoPk()>0)
      		{	
      			codigoTarjetaCliente=Utilidades.convertirAEntero(dto.getTipoTarjetaCliente().getCodigoPk()+"");
      			ps.setDouble(15, dto.getTipoTarjetaCliente().getCodigoPk());
      		}
      		else
      		{
      			ps.setNull(15, Types.DOUBLE);
      		}
      		if(!UtilidadTexto.isEmpty(dto.getObservaciones()))
      		{	
      			ps.setString(16, dto.getObservaciones());
      		}
      		else
      		{
      			ps.setNull(16, Types.VARCHAR);
      		}
            ps.setString(17, dto.getFechaModificaFromatoBD());
            ps.setString(18,dto.getHoraModifica());
            ps.setString(19, dto.getUsuarioModifica());
            ps.setInt(20, dto.getInstitucion());
           
            if(dto.getConsecutivo()>0)
            {	
            	ps.setInt(21, dto.getConsecutivo());
            }
            else
            {
            	ps.setNull(21, Types.INTEGER);
            }
            
            if(!dto.getNumTarjeta().equals("") && Utilidades.convertirAEntero(dto.getNumTarjeta())>0)
            {	
            	ps.setInt(22,Utilidades.convertirAEntero(dto.getNumTarjeta()));
            }
            else
            {
            	ps.setNull(22, Types.INTEGER);
            }
            
            logger.info("*********************************************************************************");
            logger.info(" ------------------------------GUARDAR BENEFICIARIO------------------------------");
            logger.info("\n");
            logger.info(ps);
            logger.info("\n");
            
			if(ps.executeUpdate()>0)
			{
				ps.close();
				if(actualizarSerial && codigoTarjetaCliente>0)
				{
					if(actualizarConsecutivoSerialTarjetaCliente(cosecutivoSerial,codigoTarjetaCliente, con))
					{	
						return secuencia;
					}
				}else
				{
					return secuencia;	
				}
			}
			ps.close();
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR en insert " + e);
		}
		
		return secuencia;
		*/
		return 0;
		
	}//fin 
																		
	/**
	 * Activa las tarjetas de los beneficiarios asociados a una venta
	 * @param con Conexión con la BD
	 * @param codigoTarjeta Código de la tarjeta para la cual se van a activar los beneficiarios
	 * @return true en caso de actualizar correctamente, false de lo contrario.
	 */
	public static boolean activarTarjetaBeneficiario(Connection con, int codigoTarjeta)
	{
		String sentencia=
				"UPDATE " +
					"odontologia.beneficiario_tarjeta_cliente " +
				"SET " +
					"estado_tarjeta='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
				"WHERE " +
					"venta_tarjeta_cliente=?";
		PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
		try
		{
			psd.setInt(1, codigoTarjeta);
			return psd.executeUpdate()>0;
		} catch (SQLException e)
		{
			Log4JManager.error("Error activando las tarjetas de los beneficiarios", e);
		}
		finally
		{
			psd.cerrarPreparedStatement();
		}
		return false;
	}
 
	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoBeneficiarioCliente dtoNuevo, DtoBeneficiarioCliente dtoWhere) 
	{
		/*
		boolean retorna=false;
		String consultaStr = "UPDATE odontologia.beneficiario_tarjeta_cliente  set codigo_pk=codigo_pk ";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getNumeroIdentificacion())?" , numero_identificacion= '"+dtoNuevo.getNumeroIdentificacion()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getTipoIndentificacion())?" , TIPO_INDENTIFICACION = '"+dtoNuevo.getTipoIndentificacion()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getPrimerNombre())?" , primer_nombre= '"+dtoNuevo.getPrimerNombre()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getSegundoNombre())?" , segundo_nombre= '"+dtoNuevo.getSegundoNombre()+"' ":",segundo_nombre=NULL";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getFechaModifica())?" , fecha_modifica= '"+dtoNuevo.getFechaModificaFromatoBD()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getPrimerNombre())?" , primer_apellido= '"+dtoNuevo.getPrimerApellido()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getSegundoNombre())?" , segundo_apellido= '"+dtoNuevo.getSegundoApellido()+"' ":" , segundo_apellido=NULL";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getObservaciones())?" , observaciones= '"+dtoNuevo.getObservaciones()+"' ":" , observaciones=NULL";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getHoraModifica())?" , hora_modifica= '"+dtoNuevo.getHoraModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getUsuarioModifica())?" , usuario_modifica= '"+dtoNuevo.getUsuarioModifica()+"' ":"";
		consultaStr+= !UtilidadTexto.isEmpty(dtoNuevo.getEstadoTarjeta())?" , estado_tarjeta= '"+dtoNuevo.getEstadoTarjeta()+"' ":"";
		consultaStr+=(!UtilidadTexto.isEmpty(dtoNuevo.getNumTarjeta()) && Utilidades.convertirAEntero(dtoNuevo.getNumTarjeta())>0)?" , num_tarjeta="+dtoNuevo.getNumTarjeta():"";
		
		consultaStr+= ( dtoNuevo.getParentezco() > ConstantesBD.codigoNuncaValido )?" , parentezco= "+dtoNuevo.getParentezco()+" ":"";
		consultaStr+=" where 1=1 ";
		consultaStr+=(dtoNuevo.getCodigoPk()>0)?" AND codigo_pk="+dtoNuevo.getCodigoPk(): "";
		consultaStr+= (dtoWhere.getVentaTarjetaCliente()>ConstantesBD.codigoNuncaValido)?" and venta_tarjeta_cliente= "+dtoNuevo.getVentaTarjetaCliente():"";
		
		logger.info("MODIFICAR BENEFICIARIOS\n\n\n");
		logger.info(consultaStr);
		logger.info("\n\n\n\n");
		/////cuidado, hacen un update masivo ,,,,,,,,,,,,
		
		if(dtoNuevo.getCodigoPk()<=0 && dtoNuevo.getVentaTarjetaCliente()<=0)
		{
			return false;
		}
		
		
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			ResultSetDecorator rs = null;
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consultaStr);
			retorna=ps.executeUpdate() >0; 
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Beneficiarios Tar Cli" + e);
		}
		return retorna;
		*/
		return true;
	}
	
	
	
	
	
	
	
   /**
    * 
    * @param dto
    * @return
    */
	public static ArrayList<DtoBeneficiarioCliente> cargar(DtoBeneficiarioCliente dto)
	{
		/*
		
		
		ArrayList<DtoBeneficiarioCliente> arrayDto= new ArrayList<DtoBeneficiarioCliente>();
		String consultaStr="select "+
							"bt.codigo_pk as codigoPk,"+
							"coalesce(bt.venta_tarjeta_cliente, "+ConstantesBD.codigoNuncaValido+") as ventaTarjetaCliente ,"+
							"(select tarj.nombre from odontologia.tipos_tarj_cliente tarj where tarj.codigo_pk=bt.tipo_tarjeta_cliente) as nombreTarjeta, "+
							"bt.serial as serial,"+
							"bt.indicativo_principal as indicativoPrincipal,"+
							"coalesce(bt.tipo_indentificacion, '') as tipo_indentificacion,"+
							"coalesce(bt.numero_identificacion, '') as numeroIdentificacion,"+
							"coalesce(bt.primer_apellido, '') as primerApellido,"+
							"coalesce(bt.segundo_apellido,'') as segundoApellido,"+
							"coalesce(bt.primer_nombre,'') as primerNombre,"+
							"coalesce(bt.segundo_nombre,'') as segundoNombre,"+
							"coalesce(bt.parentezco, "+ConstantesBD.codigoNuncaValido+") as parentezco ," +
							"(select p.descripcion from odontologia.parentezco p where p.codigo_pk=bt.parentezco) as nombreparentezco, "+
							" bt.estado_tarjeta  as estadoTarjeta,"+
							"bt.indicador_alidado  as indicador_alidado, "+
							"coalesce(bt.alidado_odontologico, "+ConstantesBD.codigoNuncaValido+") as alidado_odontologico,"+ 
							"coalesce(bt.tipo_tarjeta_cliente, "+ConstantesBD.codigoNuncaValido+") as tipo_tarjeta_cliente ,"+
							"coalesce(vt.tipo_venta, '') as tipo_venta ,"+
							"coalesce(bt.observaciones,'') as observaciones,"+
							"to_char(bt.fecha_modifica, 'DD/MM/YYYY') as fecha_modifica,"+
							"bt.hora_modifica as hora_modifica,"+
							"bt.usuario_modifica as usuario_modifica,   " +
							"bt.institucion as institucion, " +
							"bt.num_tarjeta as numtarjeta,  " +
							//"(select * from )" +
							"coalesce(bt.consecutivo, "+ConstantesBD.codigoNuncaValido+") as consecutivo " +
							"from odontologia.beneficiario_tarjeta_cliente  bt " +
							"LEFT OUTER JOIN  odontologia.venta_tarjeta_cliente vt ON (bt.venta_tarjeta_cliente = vt.codigo_pk) " +
							"INNER JOIN odontologia.tipos_tarj_cliente tc ON (tc.codigo_pk=bt.tipo_tarjeta_cliente) " +
							" where 1=1 ";
		
		consultaStr+=(dto.getCodigoPk()>0)?" AND bt.codigo_pk="+dto.getCodigoPk():"";
		consultaStr+=(dto.getVentaTarjetaCliente()>0)?" AND bt.venta_tarjeta_cliente="+dto.getVentaTarjetaCliente():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getSerial())?"":" AND bt.serial='"+dto.getSerial()+"' ";
		consultaStr+=UtilidadTexto.isEmpty(dto.getIndicativoPrincipal())?"":" AND bt.indicativo_principal='"+dto.getIndicativoPrincipal()+"' ";
		consultaStr+=UtilidadTexto.isEmpty(dto.getTipoIndentificacion())?"":" AND bt.tipo_indentificacion='"+dto.getTipoIndentificacion()+"' ";
		consultaStr+=UtilidadTexto.isEmpty(dto.getNumeroIdentificacion())?"":" AND bt.numero_identificacion='"+dto.getNumeroIdentificacion()+"' ";
	
		 consultaStr+=UtilidadTexto.isEmpty(dto.getPrimerApellido())?"":" AND upper(bt.primer_apellido) like upper ('%"+dto.getPrimerApellido()+"%') ";
			
		 consultaStr+=UtilidadTexto.isEmpty(dto.getSegundoApellido())?"":" AND upper(bt.segundo_apellido) like upper ('%"+dto.getSegundoApellido()+"%') ";
			
		 consultaStr+=UtilidadTexto.isEmpty(dto.getPrimerNombre())?"":"  AND upper(bt.primer_nombre) like upper ('%"+dto.getPrimerNombre()+"%') ";
		
		 consultaStr+=UtilidadTexto.isEmpty(dto.getPrimerApellido())?"":" AND upper(bt.segundo_nombre) like upper ('%"+dto.getPrimerApellido()+"%') ";
		 
		 
		consultaStr+=(dto.getParentezco()>0)?" AND parentezco="+dto.getParentezco()+"":"";		 
		consultaStr+=UtilidadTexto.isEmpty(dto.getEstadoTarjeta())?"":" AND estado_tarjeta='"+dto.getEstadoTarjeta()+"'";		 
		consultaStr+=UtilidadTexto.isEmpty(dto.getIndicadorAlidado())?"":"  AND bt.indicador_alidado='"+dto.getIndicadorAlidado()+"' ";
		consultaStr+=(dto.getAlidadoOdontologico().getCodigo()>0)?" AND bt.alidado_odontologico="+dto.getAlidadoOdontologico().getCodigo():"";
																						
		consultaStr+=(dto.getTipoTarjetaCliente().getCodigoPk()>0)?" AND bt.tipo_tarjeta_cliente="+dto.getTipoTarjetaCliente().getCodigoPk():"";
		consultaStr+=(!UtilidadTexto.isEmpty(dto.getTipoTarjetaCliente().getAliado())) ?" AND  tc.aliado='"+dto.getTipoTarjetaCliente().getAliado()+"'": "";
		consultaStr+=UtilidadTexto.isEmpty(dto.getTipoVenta())?"":"  AND vt.tipo_venta='"+dto.getTipoVenta()+"' ";
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?"":" AND bt.fecha_modifica='"+dto.getFechaModificaFromatoBD()+"'";		
		consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?"":" AND bt.hora_modifica='"+dto.getHoraModifica()+"' ";
		consultaStr+=(dto.getInstitucion()>0)?" AND bt.institucion="+dto.getInstitucion():"";
		consultaStr+=(!UtilidadTexto.isEmpty(dto.getNumTarjeta()) && Utilidades.convertirAEntero(dto.getNumTarjeta())>0)?" AND bt.num_tarjeta="+dto.getNumTarjeta():"";
		
	    logger.info("\n\n\n\n\n SQL cargarBeneficiarios / "+consultaStr);
	
	    
	    
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by bt.codigo_pk ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoBeneficiarioCliente newdto = new DtoBeneficiarioCliente();
				newdto.setCodigoPk(rs.getDouble("codigoPk"));
				newdto.setVentaTarjetaCliente(rs.getDouble("ventaTarjetaCliente"));
				newdto.setSerial(rs.getString("serial"));
				newdto.setIndicativoPrincipal(rs.getString("indicativoPrincipal"));
				newdto.setTipoIndentificacion(rs.getString("tipo_indentificacion"));
				newdto.setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
				newdto.setPrimerApellido(rs.getString("primerApellido"));
				newdto.setSegundoApellido(rs.getString("segundoApellido"));
				newdto.setPrimerNombre(rs.getString("primerNombre"));
				newdto.setSegundoNombre(rs.getString("segundoNombre"));
				newdto.setParentezco(rs.getInt("parentezco"));
				newdto.setEstadoTarjeta(rs.getString("estadoTarjeta"));
				newdto.setIndicadorAlidado(rs.getString("indicador_alidado"));
				newdto.getAlidadoOdontologico().setCodigo(rs.getDouble("alidado_odontologico"));
				newdto.getTipoTarjetaCliente().setCodigoPk(rs.getDouble("tipo_tarjeta_cliente"));
				newdto.getTipoTarjetaCliente().setNombre(rs.getString("nombreTarjeta"));
				newdto.setTipoVenta(rs.getString("tipo_venta"));
				newdto.setObservaciones(rs.getString("observaciones"));
			    newdto.setFechaModifica(rs.getString("fecha_modifica"));
			    newdto.setHoraModifica(rs.getString("hora_modifica"));
				newdto.setUsuarioModifica(rs.getString("usuario_modifica"));
				newdto.setInstitucion(rs.getInt("institucion"));
				newdto.setConsecutivo(rs.getInt("consecutivo"));
				newdto.setNombreParentezco(rs.getString("nombreParentezco"));
				newdto.setNumTarjeta(rs.getInt("numtarjeta")+"");
				arrayDto.add(newdto);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("error en carga==> "+e);
			
		}
		
		return arrayDto;
		*/
		return  new ArrayList<DtoBeneficiarioCliente>();
	}
	    

	
	   /**
	    * 
	    * @param dto
	    * @return
	    */
		public static ArrayList<DtoBeneficiarioCliente> cargarAvanzadoEmpresa(DtoBeneficiarioCliente dtoBeneficiario, InfoDatosStr dtoCompardor){
			
			/*
			ArrayList<DtoBeneficiarioCliente> arrayDto= new ArrayList<DtoBeneficiarioCliente>();
			String consultaStr="select  bt.serial as serial  , bt.primer_nombre as primer_nombre , bt.primer_apellido as primer_apellido , bt.numero_identificacion as numero_identificacion , bt.indicativo_principal as indicativo_principal, bt.num_tarjeta as numtarjeta  from odontologia.beneficiario_tarjeta_cliente  bt " ;
			consultaStr+="left outer join odontologia.venta_tarjeta_cliente vt ON (bt.venta_tarjeta_cliente = vt.codigo_pk)";
			consultaStr+="left outer join facturacion.terceros ter ON (ter.codigo = vt.tercero)";
					
			consultaStr+="  where 1=1 ";
			
			  
			

			 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getNumeroIdentificacion())?"":" AND bt.numero_identificacion='"+dtoBeneficiario.getNumeroIdentificacion()+"' ";
			 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getTipoIndentificacion())?"":" AND tipo_indentificacion='"+dtoBeneficiario.getTipoIndentificacion()+"' ";
			 
			 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getPrimerApellido())?"":" AND upper(bt.primer_apellido) like upper ('%"+dtoBeneficiario.getPrimerApellido()+"%') ";
			
			 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getSegundoApellido())?"":" AND upper(bt.segundo_apellido) like upper ('%"+dtoBeneficiario.getSegundoApellido()+"%') ";
				
			 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getPrimerNombre())?"":"  AND upper(bt.primer_nombre) like upper ('%"+dtoBeneficiario.getPrimerNombre()+"%') ";
			
			 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getSegundoNombre())?"":" AND upper(bt.segundo_nombre) like upper ('%"+dtoBeneficiario.getSegundoNombre()+"%') ";
			 
			 consultaStr+=UtilidadTexto.isEmpty(dtoCompardor.getCodigo())?"":" AND upper(ter.numero_identificacion) like upper ('%"+dtoCompardor.getCodigo()+"%') ";
			 
			 consultaStr+=UtilidadTexto.isEmpty(dtoCompardor.getNombre())?"":" AND upper(ter.descripcion) like upper ('%"+dtoCompardor.getNombre()+"%') ";
			 
			 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getTipoVenta())?"":"  AND vt.tipo_venta='"+dtoBeneficiario.getTipoVenta()+"' ";
			
			 logger.info("\n\n\n\n\n SQL cargarBeneficiarios / "+consultaStr);
				
			    try 
				 {
					Connection con = UtilidadBD.abrirConexion();
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by bt.serial",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
					while(rs.next())
					 {
						DtoBeneficiarioCliente newdto = new DtoBeneficiarioCliente();
						
						newdto.setSerial(rs.getString("serial"));
						newdto.setNumTarjeta(rs.getString("numtarjeta"));
						newdto.setIndicativoPrincipal(rs.getString("indicativo_principal"));
						
						newdto.setNumeroIdentificacion(rs.getString("numero_identificacion"));
					
						
						newdto.setPrimerNombre(rs.getString("primer_nombre"));
						newdto.setPrimerApellido(rs.getString("primer_apellido"));
						
						
						
				
						arrayDto.add(newdto);
					 }
				 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				 }
				catch (SQLException e) 
				{
					e.printStackTrace();
					logger.error("error en carga==> "+e);
					
				}
				
				return arrayDto;
			*/
			return new ArrayList<DtoBeneficiarioCliente>();
			
			
			
			
			
		}
		
		
			/**
		    * 
		    * @param dto
		    * @return
		    */
			public static ArrayList<DtoBeneficiarioCliente> cargarAvanzadoFamiliar(DtoBeneficiarioCliente dtoBeneficiario, DtoBeneficiarioCliente dtoPrincipal){
				
				/*
				
				logger.info("******************************************************************************************* SE RECIBE ***************************************************************");
				logger.info(UtilidadLog.obtenerString(dtoBeneficiario, true));
				logger.info(UtilidadLog.obtenerString(dtoPrincipal, true));
				
				ArrayList<DtoBeneficiarioCliente> arrayDto= new ArrayList<DtoBeneficiarioCliente>();
				String consultaStr="select  bt.serial as serial  , bt.primer_nombre as primer_nombre , bt.primer_apellido as primer_apellido , bt.numero_identificacion as numero_identificacion , bt.indicativo_principal as indicativo_principal,  bt.num_tarjeta as numtarjeta from odontologia.beneficiario_tarjeta_cliente  bt LEFT OUTER JOIN  odontologia.venta_tarjeta_cliente vt ON (bt.venta_tarjeta_cliente = vt.codigo_pk) "  ;
				
						
				consultaStr+="  where 1=1 ";
				
				  
				
				
				 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getNumeroIdentificacion())?"":" AND bt.numero_identificacion='"+dtoBeneficiario.getNumeroIdentificacion()+"' ";
				 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getTipoIndentificacion())?"":" AND tipo_indentificacion='"+dtoBeneficiario.getTipoIndentificacion()+"' ";
				 
				 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getPrimerApellido())?"":" AND upper(bt.primer_apellido) like upper ('%"+dtoBeneficiario.getPrimerApellido()+"%') ";
				
				 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getSegundoApellido())?"":" AND upper(bt.segundo_apellido) like upper ('%"+dtoBeneficiario.getSegundoApellido()+"%') ";
					
				 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getPrimerNombre())?"":"  AND upper(bt.primer_nombre) like upper ('%"+dtoBeneficiario.getPrimerNombre()+"%') ";
				
				 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getPrimerApellido())?"":" AND upper(bt.segundo_nombre) like upper ('%"+dtoBeneficiario.getPrimerApellido()+"%') ";
				 
				 consultaStr+=UtilidadTexto.isEmpty(dtoBeneficiario.getTipoVenta())?"":"  AND vt.tipo_venta='"+dtoBeneficiario.getTipoVenta()+"' ";
				 
				 consultaStr+= " AND bt.indicativo_principal='"+ConstantesBD.acronimoNo+"'";
				 
				 consultaStr+=" UNION ";
				
				  consultaStr+="select  bt.serial as serial  , bt.primer_nombre as primer_nombre , bt.primer_apellido as primer_apellido , bt.numero_identificacion as numero_identificacion , bt.indicativo_principal as indicativo_principal,  bt.num_tarjeta as numtarjeta  from odontologia.beneficiario_tarjeta_cliente  bt  LEFT OUTER JOIN  odontologia.venta_tarjeta_cliente vt ON (bt.venta_tarjeta_cliente = vt.codigo_pk) " ;
					
					
					consultaStr+="  where 1=1 ";
					
					  
					

					 consultaStr+=UtilidadTexto.isEmpty(dtoPrincipal.getNumeroIdentificacion())?"":" AND bt.numero_identificacion='"+dtoPrincipal.getNumeroIdentificacion()+"' ";
					 consultaStr+=UtilidadTexto.isEmpty(dtoPrincipal.getTipoIndentificacion())?"":" AND tipo_indentificacion='"+dtoPrincipal.getTipoIndentificacion()+"' ";
					 
					 consultaStr+=UtilidadTexto.isEmpty(dtoPrincipal.getPrimerApellido())?"":" AND upper(bt.primer_apellido) like upper ('%"+dtoPrincipal.getPrimerApellido()+"%') ";
					
					 consultaStr+=UtilidadTexto.isEmpty(dtoPrincipal.getSegundoApellido())?"":" AND upper(bt.segundo_apellido) like upper ('%"+dtoPrincipal.getSegundoApellido()+"%') ";
						
					 consultaStr+=UtilidadTexto.isEmpty(dtoPrincipal.getPrimerNombre())?"":"  AND upper(bt.primer_nombre) like upper ('%"+dtoPrincipal.getPrimerNombre()+"%') ";
					
					 consultaStr+=UtilidadTexto.isEmpty(dtoPrincipal.getPrimerApellido())?"":" AND upper(bt.segundo_nombre) like upper ('%"+dtoPrincipal.getPrimerApellido()+"%') ";
					 
					 consultaStr+=UtilidadTexto.isEmpty(dtoPrincipal.getTipoVenta())?"":"  AND vt.tipo_venta='"+dtoPrincipal.getTipoVenta()+"' ";
					 
					 consultaStr+= " AND bt.indicativo_principal='"+ConstantesBD.acronimoSi+"'";
					 
				
				 logger.info("\n\n\n\n\n SQL cargarBeneficiarios / "+consultaStr);
					
				    try 
					 {
						Connection con = UtilidadBD.abrirConexion();
						PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by serial",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
						while(rs.next())
						 {
							DtoBeneficiarioCliente newdto = new DtoBeneficiarioCliente();
							
							newdto.setSerial(rs.getString("serial"));
							newdto.setNumeroIdentificacion(rs.getString("numtarjeta"));
							newdto.setIndicativoPrincipal(rs.getString("indicativo_principal"));
							
							newdto.setNumeroIdentificacion(rs.getString("numero_identificacion"));
						
							
							newdto.setPrimerNombre(rs.getString("primer_nombre"));
							newdto.setPrimerApellido(rs.getString("primer_apellido"));
							
							
							
							
					
							arrayDto.add(newdto);
						 }
					 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
					 }
					catch (SQLException e) 
					{
						e.printStackTrace();
						logger.error("error en carga==> "+e);
						
					}
					
					return arrayDto;
				
				
				
				
				
			
				*/
			 return new ArrayList<DtoBeneficiarioCliente>();
			}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminar( DtoBeneficiarioCliente dto)
	{
		String consultaStr="DELETE FROM odontologia.beneficiario_tarjeta_cliente WHERE 1=1 ";
		consultaStr+=(dto.getCodigoPk()> 0)?" AND codigo_pk= "+ dto.getCodigoPk():"";
		consultaStr+=(dto.getVentaTarjetaCliente()>0)?" AND venta_tarjeta_cliente="+dto.getVentaTarjetaCliente()+" ": "" ;
		//cuidado con un delete masivo
		if(dto.getCodigoPk()<0)
			return false;
		
		logger.info("Eliminar Beneficiarios "+consultaStr);
		
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
			logger.error("ERROR EN Beneficiarios ");
			e.printStackTrace();
		}
		return false;
	}
	

	
	
	
	/**
	 * 
	 * @param dto
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigo_pk
	 * @return
	 */
	public  static ArrayList<DtoBeneficiarioCliente> consultarAvanzadaBeneficiarios(DtoBeneficiarioCliente dto, double serialInicial,  double serialFinal , double codigo_pk, int institucion)
	{
		
		
		/***
		 * FALTA VENTA EMPRESARIAL 
		 */
		/*
		logger.info("**********************************	CONSULTA BENEFICIARIOS POR VENTAS EMPRESARIALES*******************************************************");
		
		ArrayList<DtoBeneficiarioCliente> arrayDto= new ArrayList<DtoBeneficiarioCliente>();
			/*String consultaStr="select distinct be.serial as serial,  " +
					" be.primer_apellido as primerApellido," +
					" be.segundo_apellido as segundoApellido , " +
					" be.primer_nombre as primerNombre, " +
					" be.segundo_nombre as segundoNombre , " +
					" be.estado_tarjeta  as estadoTarjeta " +
					"from  odontologia.beneficiario_tarjeta_cliente be  INNER JOIN odontologia.venta_tarjeta_cliente  vtc " +
					" ON (be.venta_tarjeta_cliente=vtc.codigo_pk)   " +
					" and  ( (vtc.serial_inicial between ? and ?) or (vtc.serial_final between ? and ? ) )  and vtc.codigo_pk=?";
			
			String consultaStr= "select distinct be.serial as serial,   be.primer_apellido as primerApellido, be.segundo_apellido as segundoApellido ,"+ 
					" be.primer_nombre as primerNombre,  be.segundo_nombre as segundoNombre ,  be.estado_tarjeta  as estadoTarjeta "+
					" be.num_tarjeta " +
					"from  odontologia.beneficiario_tarjeta_cliente be  INNER JOIN odontologia.venta_tarjeta_cliente  vtc "+
					" ON (be.venta_tarjeta_cliente=vtc.codigo_pk)    where  ( be.serial between ? and ?)  and  vtc.codigo_pk=?  and " +
					" vtc.institucion= ?";
			
			
			 consultaStr+=UtilidadTexto.isEmpty(dto.getPrimerApellido())?"":" and upper(be.primer_apellido) like  upper('%"+dto.getPrimerApellido()+"%') ";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getSegundoApellido())?"":" and upper(be.segundo_apellido) like upper('%"+dto.getSegundoApellido()+"%') ";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getPrimerNombre())?"":" and  upper(be.primer_nombre)  like  upper('%"+dto.getPrimerNombre()+"%') ";
			 consultaStr+=UtilidadTexto.isEmpty(dto.getSegundoApellido())?"":" and upper(be.segundo_nombre) like upper('%"+dto.getSegundoNombre()+"%') ";
			
			 
			 logger.info("***********************************************************************************");
			 logger.info("***********************************LOGGER CONSULTA DE BENEFICIARIOS:************************************************");
			 logger.info("log consulta beneficiarios        "+consultaStr);
			 logger.info("serialInicial="+serialInicial );
			 logger.info("serialFinal="+serialFinal);
			 logger.info("serialcodigo="+codigo_pk );
			 logger.info("***********************************LOGGE DE BENEFICIARIOS:************************************************");
			 
			 logger.info(UtilidadLog.obtenerString(dto, true));
			 
			 
			 try 
			 {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con ,consultaStr);
				ps.setDouble(1, serialInicial);
				ps.setDouble(2, serialFinal);
				ps.setDouble(3, codigo_pk);
				ps.setInt(4, institucion);
				logger.info("******************************************************************************************");
				logger.info("\n----------------------------------------CONSULTA AVANZADA ------------------------------");
				logger.info(ps);
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
				
				while(rs.next())
				 {
					DtoBeneficiarioCliente newdto = new DtoBeneficiarioCliente();
					newdto.setPrimerApellido(rs.getString("primerApellido"));
					newdto.setSegundoApellido(rs.getString("segundoApellido"));
					newdto.setPrimerNombre(rs.getString("primerNombre"));
					newdto.setSegundoNombre(rs.getString("segundoNombre"));
					newdto.setEstadoTarjeta(rs.getString("estadoTarjeta"));
					newdto.setSerial(rs.getString("serial"));
					newdto.setNumTarjeta(rs.getString("numtarjeta"));
					arrayDto.add(newdto);
				 }
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			 }
			catch (SQLException e) 
			{
				e.printStackTrace();
				logger.error("error en carga==> "+e);
				
			}
			
			return arrayDto;		
		*/
		return new ArrayList<DtoBeneficiarioCliente>();
	
	}
	
	/**
	 * Validar si Exiten Seriales para una Institucion.
	 * @param seriales
	 * @param institucion
	 * @return
	 */
	public static ArrayList<Double> validarSerialesBeneficiarios(ArrayList<Double> seriales, int institucion)
	{
	logger.info("****************************************************************************************************");
	logger.info("VALIDAR SERIALES BENEFICIARIOS");
	ArrayList<Double> listaSeriales = new ArrayList<Double>();
	
	if(seriales.size()>0){
		String consultaStr= "select serial as serial from odontologia.beneficiario_tarjeta_cliente where serial  in ("+UtilidadTexto.convertirArrayDoubleACodigosSeparadosXComas(seriales)+") and institucion=? and indicador_alidado='"+ConstantesBD.acronimoNo+"'";
		 try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				listaSeriales.add(rs.getDouble("serial"));
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("Consultar -->"+ps);	
		 }
		 catch (Exception e) {
				logger.error("error en carga==> ", e);
		}
	}
	else
	{
		logger.info("--No Existen seriales institucion"+institucion);
	}
	
	return listaSeriales;
	}
	
	
	
	
	public static ArrayList<Double> validarNumTarjetasBeneficiarios(ArrayList<Double> numTarjetas, int institucion)
	{
	logger.info("****************************************************************************************************");
	logger.info("VALIDAR TARJETAS BENEFICIARIOS");
	ArrayList<Double> listaNumTarjetas = new ArrayList<Double>();
	
	if(numTarjetas.size()>0){
		String consultaStr= "SELECT num_tarjeta as numtarjeta from odontologia.beneficiario_tarjeta_cliente where num_tarjeta in ("+UtilidadTexto.convertirArrayDoubleACodigosSeparadosXComas(numTarjetas)+") and institucion=? and indicador_alidado='"+ConstantesBD.acronimoNo+"'";
		 try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				listaNumTarjetas.add(rs.getDouble("numtarjeta"));
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("Consultar -->"+ps);	
		 }
		 catch (Exception e) {
				logger.error("error en carga==> ", e);
		}
	}
	else
	{
		logger.info("--No Existen NumTarjetas institucion"+institucion);
	}
	
	return listaNumTarjetas;
	}
	
	
	
	
	

	
	/**
	 * VALIDA RANGOS RANGOS DE BENEFICIARIOS
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigoVenta
	 * @param institucion
	 * @return
	 */
	public static int  validarRangoSeriales(double serialInicial, double serialFinal,double codigoVenta,  int institucion){
					
		
			int cantidad = ConstantesBD.codigoNuncaValido;
			String consultaStr="select count(0) as cantidad  " +
							"	from odontologia.venta_tarjeta_cliente vtc INNER JOIN odontologia.beneficiario_tarjeta_cliente be " +
							"	ON (vtc.codigo_pk=be.venta_tarjeta_cliente) " +
							"	where vtc.serial_inicial<=? and vtc.serial_final>=?  and vtc.codigo_pk=? and vtc.institucion=?";
			 try 
			 {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
				ps.setDouble(1, serialInicial);
				ps.setDouble(2, serialFinal);
				ps.setDouble(3, codigoVenta);
				ps.setInt(4, institucion);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				 {
					cantidad=rs.getInt("cantidad");
				 }
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
				logger.info("Consultar -->"+ps);
			 }
			 catch (Exception e) {
					logger.error("error en carga==> ", e);
			}
											
		
		return cantidad;
	}
	
	
	/**
	 * Retorna true si encontro seriales Registrados de los contraio false
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigoInstitucion
	 * @return
	 */
	
	public static boolean validarSerialesRangoBeneficiarios(double serialInicial, double serialFinal, int codigoInstitucion ){
		
		boolean retorna = false;
		String consultaSt= " select  count(0) as cantidad from  odontologia.beneficiario_tarjeta_cliente" +
						" where institucion=? and  (serial between ? and ?) ";
		
		
		try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaSt);
			ps.setInt(1, codigoInstitucion);
			ps.setDouble(2, serialInicial);
			ps.setDouble(3, serialFinal);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			 {
				if (rs.getInt("cantidad")>0)
				{
					retorna=true;
				}
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("Consultar -->"+ps);
		 }
		 catch (Exception e) {
				logger.error("error en carga==> ", e);
		}
		
		return retorna;
		
	}
	
	
	/**
	 * Retorna true si encontro numTarjeta Registrados de los contrario false
	 * @param numTarjetalInicial
	 * @param numTarjetaFinal
	 * @param codigoInstitucion
	 * @return
	 */
public static boolean validarNumTarjetasRangoBeneficiarios(double numTarjetaInicial, double numTarjetaFinal, int codigoInstitucion ){
		
		boolean retorna = false;
		String consultaSt= " select  count(0) as cantidad from  odontologia.beneficiario_tarjeta_cliente" +
						" where institucion=? and  (num_tarjeta between ? and ?) ";
		
		
		try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaSt);
			ps.setInt(1, codigoInstitucion);
			ps.setDouble(2, numTarjetaInicial);
			ps.setDouble(3, numTarjetaFinal);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			 {
				if (rs.getInt("cantidad")>0)
				{
					retorna=true;
				}
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			logger.info("\n\n Consultar -->"+ps);
		 }
		 catch (Exception e) {
				logger.error("error en carga==> ", e);
		}
		
		return retorna;
		
	}
	
	
	
/*
	public static boolean  ( ArrayList<String> listaCodigos)
	{
		/*
		
		logger.info("\n\n\n\n\n*********************************************************************************");
		logger.info("  Eliminar beneficiario_tc_paciente ");
		String consultaStr="DELETE FROM odontologia.beneficiario_tc_paciente where 1=1 ";
		if(listaCodigos.size()>0)
		{
			consultaStr+="AND codigo_beneficiario in ("+UtilidadTexto.convertirArrayStringACodigosSeparadosXComas(listaCodigos)+")";  
		} 
		else
		{
			return false;
		}
		
	    try 
	    {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con ,  consultaStr);
			ps.executeUpdate();
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
			logger.info("Eliminar "+consultaStr);
			return true;
		} 
		catch (SQLException e) 
		{
			logger.error("ERROR EN Beneficiarios ");
			e.printStackTrace();
		}
		return false;
		
		return tryue 
	}*/
	
	
	/**
	 * 	VALIDA SI EXISTEN BENEFICIARIOS PARA UN TARJETA CLIENTE 
	 * @param dto
	 * @return
	 */
	public static boolean existeTarjetasBeneficiarios(DtoBeneficiarioCliente dto)
	{
	
		boolean retornar= false;
		
		String consultaStr="select "+
							"count(bt.codigo_pk) as cantidad "+
							"from odontologia.beneficiario_tarjeta_cliente  bt " +
							"where 1=1 " ;
							
		consultaStr+=(dto.getInstitucion()>0)?"AND  bt.institucion="+dto.getInstitucion():"";
		consultaStr+=(dto.getTipoTarjetaCliente().getCodigoPk()>0)?" AND  bt.tipo_tarjeta_cliente="+dto.getTipoTarjetaCliente().getCodigoPk()+" " : " ";
		consultaStr+=(dto.getVentaTarjetaCliente()>0)? "AND bt.venta_tarjeta_cliente="+dto.getVentaTarjetaCliente()+" " : "";
	    logger.info("\n\n\n\n\n SQL cargarBeneficiarios / "+consultaStr);
	    
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
			 {
				if ( rs.getInt("cantidad")>0)
				{
					retornar= true;
				}
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("error en carga==> "+e);
			
		}
		
		return retornar;
	}
	
	
	/**
	 * Obtiene el Consecutivo Serial de la tarjeta
	 * @param codigoTarjetaCliente
	 * @param con
	 * @return
	 */
	public static int obtenerConsecutivoSerialTarjetaCliente(int codigoTarjetaCliente, Connection con)
	{
		String consultaStr=" SELECT coalesce(consecutivo_serial,"+ConstantesBD.codigoNuncaValido+") as serial FROM tipos_tarj_cliente WHERE codigo_pk = "+codigoTarjetaCliente+"";
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaStr);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int resultado=ConstantesBD.codigoNuncaValido;
			if (rs.next())
			{
				resultado=rs.getInt("serial");
				
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, null);
			return resultado;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("error en carga==> ",e);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Metodo para actulizar el Consecutivo Serial Tarjeta
	 * @param consecutivoSerial
	 * @param con Conexi&oacute;n con la BD
	 * @return
	 */
	public static boolean actualizarConsecutivoSerialTarjetaCliente(int consecutivoSerial, int codtarjeta, Connection con )
	{
		boolean retorna= false;
		int consecutivoNuevo= consecutivoSerial+1;
		
		String consultaStr = "UPDATE tipos_tarj_cliente SET consecutivo_serial = "+consecutivoNuevo+"  WHERE codigo_pk = "+codtarjeta+"";
		try 
		{
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con, consultaStr);
			logger.info("Actualizacion"+ps);
			retorna=ps.executeUpdate()>0;
			ps.close();
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN actualizar Consecutivo Serial Tarjeta Cliente " , e);
		}
		return retorna;
		
	}
	

}
