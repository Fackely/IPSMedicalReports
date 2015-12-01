package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosStr;
import util.UtilidadBD;
import util.UtilidadLog;
import util.UtilidadTexto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;

public class SqlBaseVentaTarjetaCliente {
	
private static Logger logger = Logger.getLogger(SqlBaseVentaTarjetaCliente.class);
	


private static String consultaExisteRango =" select " +
												"count(1) " +
											"from " +
												"odontologia.venta_tarjeta_cliente vtc " +
												"INNER JOIN odontologia.tipos_tarj_cliente t ON (t.codigo_pk=vtc.tipo_tarjeta) " +
											"where " +
												"vtc.institucion = ? " + //1
												"and " +
												"(" +
													"(?  between vtc.serial_inicial and vtc.serial_final )   " + //2
													"or  " + 
													"(? between vtc.serial_inicial and vtc.serial_final) " +  //3
													"or " +
													"(vtc.serial_inicial between ? and ?) " +  // 4 y 5 
													"or " +
													"(vtc.serial_final between ? and ? ) " +  //6 y 7
												")  " +
												"and vtc.estado_venta <> '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"'" + 
												"Union " +
											"select " +
												"count(1) " +
											"from " +
												"odontologia.beneficiario_tarjeta_cliente btc " +
												"INNER JOIN odontologia.tipos_tarj_cliente t ON (t.codigo_pk=btc.tipo_tarjeta_cliente)" +
											"where " +
												"(btc.serial between ? and ? ) " +  //8 y 9
												"and btc.institucion = ? " +  //10
												"and t.aliado= ? "; //11



private static String InsertarStr= "insert into odontologia.venta_tarjeta_cliente (" +
																					" codigo_pk, "+ //1
																					" tipo_venta ,"+ //2
																					" deudor ,"+ //3
																					" tipo_tarjeta ,"+//4 
																					" codigo_servicio ,"+//5
																					" serial_inicial ,"+//6
																					" serial_final,"+//7
																					" valor_unitario_tarjeta ,"+ //8
																					" cantidad,"+ //9
																					" valor_total_tarjetas ,"+//10
																					" observaciones,"+ //11
																					" usuario_vendedor ,"+ //12
																					" anio_numero_venta,"+ //13
																					" estado_venta ,"+ //14
																					" factura_varia ,"+//15
																					" fecha_venta ,"+//16
																					" hora_venta ,"+//17
																					" fecha_modifica ,"+//18
																					" hora_modifica ,"+//19
																					" usuario_modifica ," + //20
																					" institucion, " + //21
																					" tercero, "+//22	
																					" numero_venta "+//23
																					" )values "+ 
																					"(? , ? , ? , ? , ? , ? , ? , ? , ? , ?, " + //10
																					" ? , ? , ? , ? , ? , ? , ? , ? , ? , ? " + //20
																					" , ? , ?, ?)" ; //22
		 
	

	
	
	/**
	 * 
	 * @param dto
	 * @return
		 
	        
	       
	        
	       
	       
		
	 */
	public static double guardar(Connection con, DtoVentaTarjetasCliente dto )
	{
		
		
		/*
		double secuencia=ConstantesBD.codigoNuncaValidoDouble;
	 	
		try 
		{
			secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_venta_tarjeta"); // Por ejecutar
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con ,InsertarStr );
			ps.setDouble(1,secuencia);
			ps.setString(2, dto.getTipoVenta());
			//ps.setInt(3,dto.getDeudor());
			ps.setDouble(4, dto.getTipoTarjeta());
			if(dto.getCodigoServicio()>0)
			{
				ps.setInt(5, dto.getCodigoServicio());
			}
			else
			{
				ps.setNull(5, Types.INTEGER);
			}
		
			if(dto.getSerialInicial().doubleValue()>0)
			{
				ps.setDouble(6, dto.getSerialInicial().doubleValue());	
			}
			else
			{
				ps.setNull(6, Types.NUMERIC);	
			}
		
			if(dto.getSerialFinal().doubleValue()>0)
			{
				ps.setDouble(7, dto.getSerialFinal().doubleValue());
			}
			else
			{
				ps.setNull(7, Types.NUMERIC);
			}	
			ps.setDouble(8, dto.getValorUnitarioTarjeta());
			ps.setInt(9, dto.getCantidad());
			ps.setDouble(10,dto.getValorTotalTarjetas());
			
			
			if(!UtilidadTexto.isEmpty(dto.getObservaciones()))
			{	
				ps.setString(11,dto.getObservaciones());
			}
			else
			{
				ps.setNull(11, Types.VARCHAR);
			}
			ps.setString(12,dto.getUsuarioVendedor());
			//ps.setString(13,dto.getAnioNumeroVenta());
			ps.setString(14,dto.getEstadoVenta());
			ps.setInt(15,dto.getFacturaVaria());
			//ps.setString(16,dto.getFechaVentaBD());
			//ps.setString(17,dto.getHoraVenta());
			ps.setString(18,dto.getFechaModificaFromatoBD());
			ps.setString(19,dto.getHoraModifica());
			ps.setString(20,dto.getUsuarioModifica());
			ps.setInt(21, dto.getInstitucion());
			/*
			if(UtilidadTexto.isEmpty(dto.getTercero()))
			{
				ps.setNull(22, Types.INTEGER);
			}
		//	else 
			{
			//	ps.setInt(22,Utilidades.convertirAEntero(dto.getTercero()));
			}
			ps.setString(23, dto.getNumeroVenta());
			
			logger.info(" sql insertar Venta-->"+ps);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return secuencia;
			}
			ps.close();
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.error("ERROR en insert " + e);
		}
		
		return secuencia;*/
		return 0;
		
	}//fin 
																		

    /**
     * 
     * @param dto
     * @return
     */
	public static boolean modificar( DtoVentaTarjetasCliente dto )
	{
		
		/*
		boolean retorna=false;
		
		String consultaStr ="UPDATE odontologia.venta_tarjeta_cliente  set codigo=codigo "+
																	"codigo_pk =? "+  			//1
																	"tipo_venta =?"+ 			//2
																	"deudor =?,"+				//3
																	"tipo_tarjeta =?,"+ 		//4	
																	"codigo_servicio=?,"+		//5
																	"serial_inicial=?,"+		//6
																	"serial_final=?,"+			//7
																	"valor_unitario_tarjeta =?,"+//8
																	"cantidad  =?,"+			//9
																	"valor_total_tarjetas =?,"+	//10	
																	"observaciones=?,"+			//11
																	"usuario_vendedor =?,"+		//12
																	"anio_numero_venta =?,"+	//13	
																	"estado_venta =?,"+			//14
																	"factura_varia =?,"+		//15
																	"fecha_venta=?,"+			//16
																	"hora_venta=?,"+			//17
																	"fecha_modifica =?,"+		//18
																	"hora_modifica =?,"+		//19
																	"usuario_modifica=?, " +	//20
																	"institucion =?, " +		//21
																	"tercero=? "+				//22
																	"where codigo_pk="+dto.getCodigoPk();
		
		logger.info("Update---------------"+consultaStr);
	    try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1,dto.getCodigoPk());
			ps.setString(2, dto.getTipoVenta());
			ps.setInt(3,dto.getDeudor());
			ps.setDouble(4, dto.getTipoTarjeta());
			ps.setInt(5, dto.getCodigoServicio());
			if(dto.getSerialInicial().doubleValue()<=0)
			{
				ps.setNull(6, Types.NUMERIC);
			}
			else
			{	
				ps.setDouble(6, dto.getSerialInicial().doubleValue());
			}
			if(dto.getSerialFinal().doubleValue()<=0)
			{	
				ps.setNull(7, Types.NUMERIC);
			}
			else
			{
				ps.setDouble(7, dto.getSerialFinal().doubleValue());
			}	
				
			ps.setDouble(8, dto.getValorUnitarioTarjeta());
			ps.setInt(9, dto.getCantidad());
			ps.setDouble(10,dto.getValorTotalTarjetas());
			
			if(!UtilidadTexto.isEmpty(dto.getObservaciones()))
			{
				ps.setString(11,dto.getObservaciones());
			}
			else
			{
				ps.setNull(11, Types.VARCHAR);
			}
			ps.setString(12,dto.getUsuarioVendedor());
			ps.setString(13,dto.getAnioNumeroVenta());
			ps.setString(14,dto.getEstadoVenta());
			ps.setInt(15,dto.getFacturaVaria());
			ps.setString(16,dto.getFechaVenta());
			ps.setString(17,dto.getHoraVenta());
			ps.setString(18,dto.getFechaModifica());
			ps.setString(19,dto.getHoraModifica());
			ps.setString(20,dto.getUsuarioModifica());
			
			retorna=ps.executeUpdate() >0;
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, null, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN updateDetalleCargo 1 "+e);
		}
		return retorna;
		
		*/
		return true;
			
	}
	
	
	
	/**
	 *  FALTA BUSCAR POR EL CODIGO DE LA TARJETA CLIENTE OJO WHILE MALO
	 * @param serial
	 * @param institucion
	 * @return
	 */
	public static InfoDatosStr retornarVenta(String serial, int institucion, double codigoTarjetaCliente, double codigoBeneficiario ){
		logger.info("********************************************************************");
		logger.info("---------------------------- CARGAR TIPO DE VENTA--------------------");
		logger.info("*********************************************************************");
		InfoDatosStr retornar= new InfoDatosStr();
		String consultaStr="select v.tercero as tercero , v.codigo_pk as codigo_pk , v.tipo_venta as tipo_venta  " +
							" from odontologia.venta_tarjeta_cliente v  where 1=1 ";
							if(!UtilidadTexto.isEmpty(serial))
							{
								consultaStr+=" AND ("+serial+" between v.serial_inicial and v.serial_final ) ";
							}
							consultaStr+=" AND v.institucion ="+institucion+"  AND  v.tipo_tarjeta="+codigoTarjetaCliente;
				
		logger.info("CONSULTA RETORNAR VENTA");
		logger.info(consultaStr);
	
		try 
		 {
				Connection con = UtilidadBD.abrirConexion();
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consultaStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
				 {
					retornar.setCodigo(rs.getDouble("codigo_pk")+"");
					retornar.setNombre(rs.getInt("tercero")+"");
					retornar.setDescripcion(rs.getString("tipo_venta"));
				 }
				 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			e.printStackTrace();
			
		}
		
		logger.info(UtilidadLog.obtenerString(retornar, true));
		return retornar;
				
		   
	}
	
	
   /**
    * 
    * @param dto
    * @return
    */
	public static ArrayList<DtoVentaTarjetasCliente> cargar( DtoVentaTarjetasCliente dto)
	{
		
		ArrayList<DtoVentaTarjetasCliente> arrayDto= new ArrayList<DtoVentaTarjetasCliente>();
		
		
		/*
		String consultaStr="select " +
							" ve.codigo_pk as codigo_pk, "+ //1
							" ve.tipo_venta as tipo_venta ,"+ //2
							" ve.deudor as deudor ,"+ //3
							
							" ve.tercero as tercero ," +
								
							" (select tarj.nombre from odontologia.tipos_tarj_cliente tarj where tarj.codigo_pk=ve.tipo_tarjeta) as nombreTarjeta, "+
							" ve.codigo_servicio as codigo_servicio ,"+//6
							
							" ve.serial_inicial as serial_inicial ,"+//7
							" ve.serial_final as serial_final,"+//8
							" ve.valor_unitario_tarjeta as valor_unitario_tarjeta,"+ //9
							" ve.cantidad as cantidad,"+ //10
							" ve.valor_total_tarjetas as valor_total_tarjetas ,"+//11
							" ve.observaciones as observaciones ,"+ //12
							" ve.usuario_vendedor as usuario_vendedor ,"+ //13
							" ve.anio_numero_venta as anio_numero_venta ,"+ //14
							" ve.estado_venta as estado_venta  ,"+ //15
							
							" ve.factura_varia as factura_varia  ," +
							" (select fact.consecutivo from facturasvarias.facturas_varias fact where codigo_fac_var=ve.factura_varia) as consecutivoFactura , "+//16
							
							" ve.tipo_tarjeta as tipo_tarjeta , "+
							" ve.fecha_venta as fecha_venta ,"+//17
							" ve.hora_venta as hora_venta ,"+//18
							" ve.fecha_modifica as fecha_modifica  ,"+//19
							" ve.hora_modifica as hora_modifica ,"+//20
							" ve.usuario_modifica as  usuario_modifica, " +//21
							" ve.institucion as institucion , "+//22
							" ve.numero_venta as  numero_venta, "+//23
							" fv.consecutivo as numerofactura, "+	
							" fv.valor_factura as valorfactura,"+
							
							
							"CASE " +
							"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"'   THEN getnombrepersona(d.codigo_paciente) " +
							"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa) " +
							"WHEN (d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' and   d.es_empresa <> '"+ConstantesBD.acronimoSi+"') THEN getdescripciontercero(d.codigo_tercero) " +
							
							"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  coalesce (d.primer_apellido, '') ||' ' || coalesce(d.segundo_apellido, '') || ' ' || coalesce(d.primer_nombre, '' )|| ' '|| coalesce(d.segundo_nombre ) " +
						"END AS comprador," +
						
							"CASE " +
							"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
							"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnitempresa(d.codigo_empresa)  " +
							"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
							"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
						"END AS idComprador, " +
						
							" case when d.codigo_paciente  is not null  then getTipoId(d.codigo_paciente) " +
							" when d.codigo_tercero  is not null then '"+EmunTiposIdentificacion.NI+"'" +
							" when d.codigo_empresa  is not null then '"+EmunTiposIdentificacion.NI+"'  " +
							" else  d.tipo_identificacion end as tipoIdentificacion , " +
							
							
							"administracion.getnombreusuario(ve.usuario_vendedor) as usuariovendedor "+
							
							" FROM "+ 
							
							" facturasvarias.facturas_varias fv "+
							
							" INNER JOIN odontologia.venta_tarjeta_cliente  ve ON  (ve.factura_varia=fv.codigo_fac_var)"+ 
							" INNER JOIN  facturasvarias.deudores d ON (ve.deudor=d.codigo) " +
							" LEFT OUTER JOIN administracion.personas pe ON (pe.codigo=d.codigo_paciente)  " +
							
							" WHERE 1=1";
		
		
		
		consultaStr+=(dto.getCodigoPk()>0)?" and  ve.codigo_pk="+dto.getCodigoPk():" ";
		consultaStr+=UtilidadTexto.isEmpty(dto.getTipoVenta())?"":" AND ve.tipo_venta='"+dto.getTipoVenta()+"'";
		consultaStr+=(dto.getDeudor()>0)?" AND ve.deudor="+dto.getDeudor():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getTercero())?"":" AND ve.tercero="+dto.getTercero();	 
		consultaStr+=(dto.getTipoTarjeta()>0)?" AND ve.tipo_tarjeta="+dto.getTipoTarjeta()+"":"";
		consultaStr+=(dto.getCodigoServicio()>0)?" AND ve.codigo_servicio="+dto.getCodigoServicio()+"":"";
		consultaStr+=(dto.getSerialInicial().doubleValue()>0)?" AND ve.serial_inicial="+dto.getSerialInicial()+"":"";
		consultaStr+=(dto.getSerialFinal().doubleValue()>0)?" AND ve.serial_final="+dto.getSerialFinal()+"":"";				
		consultaStr+=(dto.getValorTotalTarjetas()>0)?" AND ve.valor_unitario_tarjeta="+dto.getValorTotalTarjetas()+"":"";				
		consultaStr+=(dto.getCantidad()>0)?" AND ve.cantidad="+dto.getCantidad()+"":"";				
		consultaStr+=(dto.getValorTotalTarjetas()>0)?" AND ve.valor_total_tarjetas="+dto.getValorTotalTarjetas()+"":"";				
		consultaStr+=UtilidadTexto.isEmpty(dto.getObservaciones())?"":" and ve.observaciones='"+dto.getObservaciones()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioVendedor())?"":" and ve.usuario_vendedor='"+dto.getUsuarioVendedor()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getAnioNumeroVenta())?"":" and ve.anio_numero_venta='"+dto.getAnioNumeroVenta()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getEstadoVenta())?"":" and ve.estado_venta='"+dto.getEstadoVenta()+"'";
		//consultaStr+=(dto.getFacturaVaria()>0)?" AND ve.factura_varia="+dto.getFacturaVaria()+"":"";		 
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaVenta())?"":" AND ve.fecha_venta='"+dto.getFechaVenta()+"'";		 
		consultaStr+=UtilidadTexto.isEmpty(dto.getHoraVenta())?"":" AND ve.hora_venta='"+dto.getHoraVenta()+"'";		
		consultaStr+=UtilidadTexto.isEmpty(dto.getFechaModifica())?"":" AND ve.fecha_modifica='"+dto.getFechaModifica()+"'";		
		consultaStr+=UtilidadTexto.isEmpty(dto.getHoraModifica())?"":" AND ve.hora_modifica='"+dto.getHoraModifica()+"'";		
		consultaStr+=UtilidadTexto.isEmpty(dto.getHoraVenta())?"":" AND ve.usuario_modifica='"+dto.getHoraVenta()+"'";	
		consultaStr+=(dto.getInstitucion()>0)?" AND ve.institucion="+dto.getInstitucion()+"":""; 
		consultaStr+=(dto.getFacturaVaria()>0)?" AND fv.consecutivo="+dto.getFacturaVaria()+"":""; /// manejo del consecutivo
		consultaStr+=(dto.getCodigoPkFacturaVaria()>0)? " AND  ve.factura_varia= "+dto.getCodigoPkFacturaVaria()+" ": "";
		consultaStr+=( !UtilidadTexto.isEmpty(dto.getNumeroVenta()))?"  and ve.numero_venta='"+dto.getNumeroVenta()+"' ": "";


		 	*/
		
		String consultaStr="select " +
							" ve.codigo_pk as codigo_pk, "+ //1
							" ve.tipo_venta as tipo_venta,"+ //2
							" ve.deudor as deudor," +
							" ve.tipo_tarjeta " +
							" from odontologia.venta_tarjeta_cliente ve ";
						
		String where = "";
		
		if(dto.getTipoTarjeta()>0){
			
			where = "where ve.tipo_tarjeta="+dto.getTipoTarjeta();
		}

		
		if(!UtilidadTexto.isEmpty(dto.getFechaInicialConsulta()) && !UtilidadTexto.isEmpty(dto.getFechaFinalConsulta()))
		{
			//consultaStr+=" AND ve.fecha_venta between '"+dto.getFechaInicialConsulta()+"' AND '"+dto.getFechaFinalConsulta()+"' ";
		
			if(!where.isEmpty()){
				
				where += " AND ";
			
			}else{
				
				where += " WHERE ";
			}
		
			where += " ve.fecha_venta between '"+dto.getFechaInicialConsulta()+"' AND '"+dto.getFechaFinalConsulta()+"' ";
			
		}
		
		consultaStr += where;
	    try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr+" ORDER  BY ve.tipo_tarjeta  ");
			logger.info("\n\n\n\n\n SQL Cargar Ventas   --/ "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			 {
				
				DtoVentaTarjetasCliente newdto = new DtoVentaTarjetasCliente();
				newdto.setCodigoPk(rs.getDouble("codigo_pk"));
				newdto.setTipoTarjeta(rs.getInt("tipo_tarjeta"));
						
				arrayDto.add(newdto);
			 }
		 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
			
		}
		
		return arrayDto;
		//return null;
	}
          
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminar( DtoVentaTarjetasCliente dto)
	{
		String consultaStr="DELETE FROM odontologia.venta_tarjeta_cliente WHERE 1=1 ";
	    
		consultaStr+=(dto.getCodigoPk()> 0)?" AND codigo= "+ dto.getCodigoPk():"";
		logger.info("Eliminar odontologia.seq_emision_bonos_desc"+consultaStr);
		
		///cuidado delete masivo....
		if(dto.getCodigoPk()<= 0)
			return false;
	    
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
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param Institucion
	 * @param aliado
	 * @return
	 */
	public static boolean existeRangoEnVenta(double rangoInicial , double rangoFinal , int institucion, String aliado)
	{
		boolean retorna= false;
		  try 
		  {
		  logger.info("*****************************************************************************************");
		  logger.info("existeRangoEnVenta--->"+consultaExisteRango+" ---> rangoInicial-->"+rangoInicial+" rangoFinal-->"+rangoFinal+" institucion->"+institucion);
		  Connection con = UtilidadBD.abrirConexion();
		  PreparedStatementDecorator ps =  new PreparedStatementDecorator(con, consultaExisteRango);
			
		  	ps.setInt(1, institucion);
			ps.setDouble(2, rangoInicial);
			ps.setDouble(3, rangoFinal);
			ps.setDouble(4, rangoInicial);
			ps.setDouble(5, rangoFinal);
			ps.setDouble(6, rangoInicial);
			ps.setDouble(7, rangoFinal);
			ps.setDouble(8, rangoInicial);				
			ps.setDouble(9, rangoFinal);
			ps.setInt(10, institucion);	
			ps.setString(11, aliado);
	
				
				
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())
				{
					if(rs.getInt(1)>0)
					{	
						retorna= true;
					}	
				}
			 logger.info("Consulta------------->"+ps);
			 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		
			 }
			catch (SQLException e) 
			{
				logger.error("error en carga==> "+e.getErrorCode());
				
			}
			return retorna;
	}
	
	/*
	private static ArrayList<DtoVentaTarjetasCLiente>  consultar( DtoVentaTarjetasCLiente dto)

	{
		String consultaStr="SELECT "+ 
						"	fv.consecutivo as numerofactura,"+	
						"	fv.valor_factura as valorfactura,"+
						"	coalesce(d.primer_apellido,'') ||' '|| coalesce(d.segundo_apellido,'')  ||' '|| coalesce(d.primer_nombre, '') ||' '|| coalesce(d.segundo_nombre, '') as comprador, "+
						"	coalesce(d.tipo_identificacion,'') ||' '|| coalesce(d.numero_identificacion,'') as idcomprador, "+
						"	coalesce(d.numero_identificacion,'') as numeroidentificacion, "+
						"	administracion.getnombreusuario(ve.usuario_vendedor) as usuariovendedor, "+
						"	ve.estado_venta "+
						" FROM "+ 
						" facturasvarias.facturas_varias fv "+
						"	INNER JOIN odontologia.venta_tarjeta_cliente  ve ON  (ve.factura_varia=fv.codigo_fac_var)"+ 
						"	INNER JOIN facturasvarias.deudores d ON (ve.deudor=d.codigo) AND WHERE 1=1";
			
			consultaStr+=UtilidadTexto.isEmpty(dto.getUsuarioVendedor())?"":" and usuario_vendedor='"+dto.getUsuarioVendedor()+"'";
			consultaStr+=UtilidadTexto.isEmpty(dto.getEstadoVenta())?"":" and estado_venta='"+dto.getEstadoVenta()+"'";
			consultaStr+=(dto.getTipoTarjeta()>0)?" AND tipo_tarjeta="+dto.getTipoTarjeta()+"":"";
			consultaStr+=(dto.getFacturaVaria()>0)?" AND factura_varia="+dto.getFacturaVaria()+"":"";
			consultaStr+=UtilidadTexto.isEmpty(dto.getTercero())?"":" AND tercero="+dto.getTercero();	
			
			if(!UtilidadTexto.isEmpty(dto.getFechaInicialConsulta()) && !UtilidadTexto.isEmpty(dto.getFechaFinalConsulta()))
					{
					consultaStr+= "AND ve.fecha_venta  between '"+dto.getFechaInicialConsulta()+"' and '"+dto.getFechaFinalConsulta()+"'; ";
					}
			
		return null;
	}**/
	
	
	

}
