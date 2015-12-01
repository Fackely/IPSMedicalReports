package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDocsAutorizacionSaldoMora;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoMotivosAutSaldoMora;
import com.princetonsa.mundo.PersonaBasica;

public class SqlBaseAutorizacionIngresoPacienteSaldoMoraDao
{
	private static Logger logger = Logger.getLogger(SqlBaseAutorizacionIngresoPacienteSaldoMoraDao.class);
	
	public static String	consultaDocsPacienteDeudor	 =	"SELECT " +
																"dg.ingreso," +
																"dg.consecutivo," +
																"dg.anio_consecutivo," +
																"dg.tipo_documento," +
																"dg.valor," +
																"to_char(df.fecha_inicio,'dd/mm/yyyy') AS fechainicio," +
																"df.nro_coutas," +
																"df.dias_por_couta AS dias," +
																"df.codigo_pk AS datofinanciacion," +
																"d.tipo_identificacion AS tipoiddeudor," +
																"d.numero_identificacion AS numiddeudor, " +
																"coalesce(d.primer_nombre,'') AS primernomdeudor," +
																"coalesce(d.segundo_nombre,'') AS segundonomdeudor," +
																"coalesce(d.primer_apellido,'') AS primerapedeudor," +
																"coalesce(d.segundo_apellido,'') AS segundoapedeudor," +
																"getnombrepersona(d.codigo_paciente) AS nombrepaciente, " +
																"f.consecutivo_factura AS consecutivofac," +
																"ca.consecutivo AS centroatencion," +
																"ca.descripcion AS nombrecentro," +
																"i.hora_ingreso AS horaingreso," +
																"to_char(i.fecha_ingreso,'dd/mm/yyyy') AS fechaingreso," +
																"i.codigo_paciente AS codpaciente,"+
																"dg.valor-(" +
																"SELECT " +
																	"SUM(apcp.valor)" +
																"FROM " +
																	"carterapaciente.aplicac_pagos_cartera_pac apcp " +
																"INNER JOIN " +
																	"carterapaciente.datos_financiacion df ON(df.codigo_pk =apcp.datos_financiacion) " +
																"INNER JOIN " +
																	"carterapaciente.deudores_datos_finan ddf ON(ddf.datos_financiacion=df.codigo_pk) " +
																"WHERE " +
																	"ddf.num_id_deudor=d.numero_identificacion" +
																") AS total " +
															"FROM " +
																"administracion.personas ps " +
																"INNER JOIN " +
																	"carterapaciente.deudorco d ON (d.codigo_paciente=ps.codigo) " +
																"INNER JOIN " +
																	"manejopaciente.ingresos i ON (i.id=d.ingreso) " +
																"INNER JOIN " +
																	"administracion.centro_atencion ca ON (ca.consecutivo=i.centro_atencion) " +
																"INNER JOIN " +
																	"carterapaciente.deudores_datos_finan ddf ON (ddf.ingreso=d.ingreso AND ddf.institucion=d.institucion AND ddf.clase_deudor=d.clase_deudorco AND ddf.num_id_deudor=d.numero_identificacion) " +
																"INNER JOIN " +
																	"carterapaciente.datos_financiacion df ON (df.codigo_pk=ddf.datos_financiacion) " +
																"INNER JOIN " +
																	"carterapaciente.documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) " +
																"INNER JOIN " +
																	"facturas f ON (f.codigo=df.codigo_factura) ";
	
	public static String consultaCuotas	=	"SELECT DISTINCT "+
												"cdf.codigo_pk,"+
												"cdf.dato_financiacion,"+
												"cdf.valor_couta,"+
												"cdf.nro_cuota,"+
												"coalesce((SELECT SUM(dapcp.valor) FROM carterapaciente.det_apli_pagos_cartera_pac dapcp WHERE dapcp.cuota_dato_financiacion=cdf.codigo_pk ),0) AS totalaplicacion, "+
												"cdf.valor_couta-coalesce((SELECT SUM(dapcp.valor) FROM carterapaciente.det_apli_pagos_cartera_pac dapcp WHERE dapcp.cuota_dato_financiacion=cdf.codigo_pk ),0) AS diferencia "+
												"FROM "+ 
												"carterapaciente.cuotas_datos_financiacion cdf "+
												"LEFT OUTER JOIN "+ 
												"carterapaciente.det_apli_pagos_cartera_pac  dapcp ON (dapcp.cuota_dato_financiacion=cdf.codigo_pk) "+
												"INNER JOIN "+ 
												"carterapaciente.datos_financiacion df ON (df.codigo_pk= cdf.dato_financiacion) "+
												"INNER JOIN "+ 
												"carterapaciente.documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) ";
	
	public static String consultaMotivosAutSaldoMora	=	"SELECT " +
																"codigo_pk AS codigo," +
																"descripcion, " +
																"institucion " +
															"FROM " +
																"carterapaciente.motivos_aut_saldo_mora ";
	
	public static String insertarAutorizacionIngreso	=	"INSERT INTO " +
																"carterapaciente.autorizacion_saldo_mora " +
															"(" +
																"codigo_pk," +//1
																"fecha," +//2
																"hora," +//3
																"via_ingreso," +//4
																"tipos_paciente," +//5
																"horas_vigencia," +//6
																"persona_autoriza," +//7
																"motivo," +//8
																"observaciones," +//9
																"codigo_paciente," +//10
																"fecha_modifica," +//11
																"hora_modifica," +//12
																"usuario_modifica," +//13
																"ingreso," +//14
																"fecha_ingreso," +//15
																"hora_ingreso," +//16
																"institucion," +//17
																"centro_atencion" +//18
															") " +
															"VALUES " +
															"(" +
																"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
															")";
											
	public static String existeAutorizacionConViaIngreso	=	"SELECT " +
																	"asm.codigo_pk AS codigo " +
																"FROM " +
																	"carterapaciente.autorizacion_saldo_mora asm " +
																"WHERE " +
																	"asm.via_ingreso=? " +
																"AND " +
																	"asm.codigo_paciente=? ";
	
	public static String consultaAutorizaciones	=	"SELECT DISTINCT " +
														"asm.codigo_pk AS codigo," +
														"to_char(asm.fecha,'dd/mm/yyyy') AS fecha," +
														"asm.hora," +
														"asm.via_ingreso AS viaingreso," +
														"getnombretipopaciente(asm.tipos_paciente) AS tipopaciente," +
														"asm.horas_vigencia AS horasvig," +
														"asm.persona_autoriza AS personaautoriza," +
														"asm.motivo ," +
														"asm.observaciones," +
														"asm.codigo_paciente AS codpac," +
														"to_char(asm.fecha_modifica,'dd/mm/yyyy') AS fechamodifica," +
														"asm.hora_modifica AS horamodifica," +
														"asm.usuario_modifica AS usuariomodifica," +
														"asm.ingreso," +
														"to_char(asm.fecha_ingreso,'dd/mm/yyyy') AS fechaingreso," +
														"asm.hora_ingreso AS horaingreso," +
														"asm.institucion," +
														"asm.centro_atencion AS centroatencion," +
														"pe.numero_identificacion AS numid," +
														"pe.tipo_identificacion AS tipoid," +
														"getnombrepersona(pe.codigo) AS nombrepaciente," +
														"getnombreviaingreso(asm.via_ingreso) AS nombreviaingreso," +
														"masm.descripcion AS descmotivo " +
													"FROM " +
														"carterapaciente.autorizacion_saldo_mora asm " +
													"INNER JOIN " +
														"manejopaciente.pacientes p ON (p.codigo_paciente=asm.codigo_paciente) " +
													"INNER JOIN " +
														"administracion.personas pe ON (pe.codigo=p.codigo_paciente) " +
													"INNER JOIN " +
														"carterapaciente.motivos_aut_saldo_mora masm ON(masm.codigo_pk=asm.motivo) ";
	
	public static String insertarDocsAutorizacionSaldoM	="	INSERT INTO " +
																"carterapaciente.docs_autorizacion_saldo_m " +
															"(" +
																"autorizacion_saldo_mora," +
																"datos_financiacion," +
																"saldo" +
															") " +
															"VALUES " +
															"(" +
																"?,?,?" +
															")";
	
	
	public static boolean insertarDocsAutorizacionSaldoM(DtoDocsAutorizacionSaldoMora dto)
	{
		boolean transaccionExitosa;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDocsAutorizacionSaldoM, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setDouble(1, dto.getAutorizacionSaldoMora());
			ps.setDouble(2, dto.getDatosFinanciacion());
			ps.setBigDecimal(3, dto.getSaldo());
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				transaccionExitosa=true;
			}
			else
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				transaccionExitosa=false;
			}
			
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarAutorizacionIngreso==> "+e);
			return transaccionExitosa=false;
		}
		
		return transaccionExitosa;
	}
	
	public static ArrayList<DtoAutorizacionSaldoMora> consultaAutorizaciones(DtoAutorizacionSaldoMora dto)
	{
		ArrayList<DtoAutorizacionSaldoMora> lista=new ArrayList<DtoAutorizacionSaldoMora>();
		String consulta=consultaAutorizaciones;
		consulta+="	WHERE ";
		
		if (!dto.getFecha().equals("")&&!dto.getFechaFinal().equals(""))
			consulta+=" (asm.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFecha())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaFinal())+"') AND ";
		
		if (dto.getViaIngreso()!=ConstantesBD.codigoNuncaValido)
			consulta+=" asm.via_ingreso="+dto.getViaIngreso()+" AND ";
		
		if(!dto.getTiposPaciente().equals(""))
			consulta+="	asm.tipos_paciente='"+dto.getTiposPaciente()+"' AND ";
		
		if (!dto.getTipoId().equals(""))
			consulta+=" pe.tipo_identificacion='"+dto.getTipoId()+"' AND ";
		
		if (!dto.getIdPaciente().equals(""))
			consulta+=" pe.numero_identificacion='"+dto.getIdPaciente()+"' AND ";
		
		consulta+=" 1=1 ORDER BY fecha,hora ASC ";
		
		logger.info("LA CONSULTA------->"+consulta);
		
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoAutorizacionSaldoMora dtoCon=new DtoAutorizacionSaldoMora();
				dtoCon.setCodigoPk(rs.getDouble("codigo"));
				dtoCon.setFecha(rs.getString("fecha"));
				dtoCon.setHora(rs.getString("hora"));
				dtoCon.setViaIngreso(rs.getInt("viaingreso"));
				dtoCon.setTiposPaciente(rs.getString("tipopaciente"));
				dtoCon.setHorasVigencia(rs.getString("horasvig"));
				dtoCon.setPersonaAutoriza(rs.getString("personaautoriza"));
				dtoCon.setMotivo(rs.getDouble("motivo"));
				dtoCon.setObservaciones(rs.getString("observaciones"));
				dtoCon.setCodigoPaciente(rs.getInt("codpac"));
				dtoCon.setFechaModifica(rs.getString("fechamodifica"));
				dtoCon.setHoraModifica(rs.getString("horamodifica"));
				dtoCon.setUsuarioModifica(rs.getString("usuariomodifica"));
				dtoCon.setIngreso(rs.getInt("ingreso"));
				dtoCon.setFechaIngreso(rs.getString("fechaingreso"));
				dtoCon.setHoraIngreso(rs.getString("horaingreso"));
				dtoCon.setInstitucion(rs.getInt("institucion"));
				dtoCon.setCentroAtencion(rs.getInt("centroatencion"));
				dtoCon.setIdPaciente(rs.getString("numid"));
				dtoCon.setTipoId(rs.getString("tipoid"));
				dtoCon.setNombrePaciente(rs.getString("nombrepaciente"));
				dtoCon.setNombreViaIngreso(rs.getString("nombreviaingreso"));
				dtoCon.setDescripcionTipoPaciente(ValoresPorDefecto.getIntegridadDominio(rs.getString("tipopaciente"))+"");
				dtoCon.setDescripcionMotivo(rs.getString("descmotivo"));
				
				
				lista.add(dtoCon);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaAutorizaciones==> "+e);
		}
		
		return lista;
	}
	
	
	public static ArrayList<DtoAutorizacionSaldoMora> existeAutorizacionConViaIngreso(DtoAutorizacionSaldoMora dto)
	{
		ArrayList<DtoAutorizacionSaldoMora> lista=new ArrayList<DtoAutorizacionSaldoMora>();
		logger.info("LA CONSULTA----->"+existeAutorizacionConViaIngreso+"\n"+dto.getViaIngreso()+"\n"+dto.getCodigoPaciente());
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(existeAutorizacionConViaIngreso, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, dto.getViaIngreso());
			ps.setInt(2, dto.getCodigoPaciente());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoAutorizacionSaldoMora dtoAut = new DtoAutorizacionSaldoMora();
				dto.setCodigoPk(rs.getInt("codigo"));
				lista.add(dtoAut);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,rs, con);
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarAutorizacionIngreso==> "+e);
		}
		return lista;
	}
	
	public static double insertarAutorizacionIngreso (DtoAutorizacionSaldoMora dto)
	{
		double secuenciaAutorizacion=ConstantesBD.codigoNuncaValidoDouble;
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			secuenciaAutorizacion=UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_autorizacion_saldo_mora");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarAutorizacionIngreso, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			logger.info("***-->"+secuenciaAutorizacion);
			logger.info("***-->"+dto.getFecha());
			logger.info("***-->"+dto.getHora());
			logger.info("***-->"+dto.getViaIngreso());
			logger.info("***-->"+dto.getTiposPaciente());
			logger.info("***-->"+dto.getHorasVigencia());
			logger.info("***-->"+dto.getPersonaAutoriza());
			logger.info("***-->"+dto.getMotivo());
			logger.info("***-->"+dto.getObservaciones());
			logger.info("***-->"+dto.getCodigoPaciente());
			logger.info("***-->"+dto.getFechaModifica());
			logger.info("***-->"+dto.getHoraModifica());
			logger.info("***-->"+dto.getUsuarioModifica());
			logger.info("***-->"+dto.getIngreso());
			logger.info("***-->"+dto.getFechaIngreso());
			logger.info("***-->"+dto.getHoraIngreso());
			logger.info("***-->"+dto.getInstitucion());
			logger.info("***-->"+dto.getCentroAtencion());
			
			
			ps.setDouble(1, secuenciaAutorizacion);
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(dto.getFecha()));
			ps.setString(3, dto.getHora());
			ps.setInt(4, dto.getViaIngreso());
			ps.setString(5, dto.getTiposPaciente());
			ps.setString(6, dto.getHorasVigencia());
			ps.setString(7, dto.getPersonaAutoriza());
			ps.setDouble(8, dto.getMotivo());
			ps.setString(9, dto.getObservaciones());
			ps.setInt(10, dto.getCodigoPaciente());
			ps.setString(11, dto.getFechaModifica());
			ps.setString(12, dto.getHoraModifica());
			ps.setString(13, dto.getUsuarioModifica());
			ps.setInt(14, dto.getIngreso());
			ps.setString(15, dto.getFechaIngreso());
			ps.setString(16, dto.getHoraIngreso());
			ps.setInt(17, dto.getInstitucion());
			ps.setInt(18, dto.getCentroAtencion());
			
			if(ps.executeUpdate()>0)
			{
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
				return secuenciaAutorizacion;
			}
			else
			{
				secuenciaAutorizacion=ConstantesBD.codigoNuncaValidoDouble;
				SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps,null, con);
			}
		}
		catch (SQLException e) 
		{
			logger.error("ERROR EN insertarAutorizacionIngreso==> "+e);
		}
		
		return secuenciaAutorizacion;
	}
	
	public static ArrayList<DtoMotivosAutSaldoMora> consultaMotivosAutSaldoMora()
	{
		ArrayList<DtoMotivosAutSaldoMora> listadoMotivos = new ArrayList<DtoMotivosAutSaldoMora>();
		Connection con = UtilidadBD.abrirConexion();
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaMotivosAutSaldoMora,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoMotivosAutSaldoMora dto=new DtoMotivosAutSaldoMora();
				dto.setCodigoPk(rs.getDouble("codigo"));
				dto.setDescripcion(rs.getString("descripcion"));
				dto.setInstitucion(rs.getInt("institucion"));
				listadoMotivos.add(dto);
			 }
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaCuotas==> "+e);
		}
		
		return listadoMotivos;
	}
	
	
	public static ArrayList<DtoCuotasDatosFinanciacion> consultaCuotas (DtoDocumentosGarantia dto)
	{
		ArrayList<DtoCuotasDatosFinanciacion> listadoCuotasXDG	= new ArrayList<DtoCuotasDatosFinanciacion>()	;
		Connection con = UtilidadBD.abrirConexion();
		String consulta =	consultaCuotas;
		
		consulta+="	WHERE dg.ingreso="+dto.getIngreso()+" AND ";
		consulta+="	dg.consecutivo='"+dto.getConsecutivo()+"' AND ";
		consulta+="	dg.tipo_documento='"+dto.getTipoDocumento()+"' ";
		
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			logger.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoCuotasDatosFinanciacion dtoDoc= new DtoCuotasDatosFinanciacion();
				dtoDoc.setCodigoPK(rs.getInt("codigo_pk"));
				dtoDoc.setDatosFinanciacion(rs.getInt("dato_financiacion"));
				dtoDoc.setValorCuota(rs.getBigDecimal("valor_couta"));
				dtoDoc.setNroCuota(rs.getInt("nro_cuota"));
				dtoDoc.setTotalAplicado(rs.getBigDecimal("totalaplicacion"));
				listadoCuotasXDG.add(dtoDoc);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaCuotas==> "+e);
		}
		return listadoCuotasXDG;
	}
	
	public static ArrayList<DtoDocumentosGarantia> consultaDocsPacienteDeudor(int codPaciente)
	{
		ArrayList<DtoDocumentosGarantia> listadoDocs = new ArrayList<DtoDocumentosGarantia>();
		Connection con = UtilidadBD.abrirConexion();
		String consulta =	consultaDocsPacienteDeudor;
		
		consulta+=	"WHERE ps.codigo="+codPaciente+" AND ";
		consulta+=	"dg.cartera='"+ConstantesBD.acronimoSi+"' AND ";
		consulta+=	"dg.tipo_documento IN ('"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"','"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"') AND ";
		consulta+=	"to_char(dg.fecha_generacion,'dd/mm/yyyy') <= '"+UtilidadFecha.getFechaActual()+"' AND ";
		consulta+=	"dg.estado NOT IN ('"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"','"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"') AND ";
		consulta+=	"d.clase_deudorco IN ('"+ConstantesIntegridadDominio.acronimoDeudor+"')";
		
		
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoDocumentosGarantia dto=new DtoDocumentosGarantia();
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setAnioConsecutivo(rs.getString("anio_consecutivo"));
				dto.setTipoDocumento(rs.getString("tipo_documento"));
				dto.setValorDoc(rs.getBigDecimal("valor"));
				dto.setFechaInicioDatoFin(rs.getString("fechainicio"));
				dto.setNroCuotasDatoFin(rs.getInt("nro_coutas"));
				dto.setDiasCuota(rs.getInt("dias"));
				dto.setPrimerNombre(rs.getString("primernomdeudor"));
				dto.setSegundoNombre(rs.getString("segundonomdeudor"));
				dto.setPrimerApellido(rs.getString("primerapedeudor"));
				dto.setSegundoApellido(rs.getString("segundoapedeudor"));
				dto.setNomPaciente(rs.getString("nombrepaciente"));
				dto.setConsecutivoFactura(rs.getInt("consecutivofac"));
				dto.setValorTotal(rs.getBigDecimal("total"));
				dto.setCentroAtencion(rs.getString("nombrecentro"));
				dto.setHoraIngreso(rs.getString("horaingreso"));
				dto.setFechaIngreso(rs.getString("fechaingreso"));
				dto.setCodigoPaciente(rs.getInt("codpaciente"));
				dto.setDatoFinanciacion(rs.getDouble("datofinanciacion"));
				listadoDocs.add(dto);
				
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaDocsPacienteDeudor==> "+e);
		}
				
		
		return listadoDocs;
	}

	public static boolean actualizarDatosAutoricacion(DtoAutorizacionSaldoMora dto, PersonaBasica paciente) {
		try{
			Connection con = UtilidadBD.abrirConexion();
			String sentencia=
				"UPDATE " +
					"carterapaciente.autorizacion_saldo_mora " +
					"SET " +
						"ingreso=?, " +
						"fecha_ingreso=CURRENT_DATE, " +
						"hora_ingreso="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
					"WHERE " +
						"via_ingreso=? AND tipos_paciente=? AND codigo_paciente=?";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, paciente.getCodigoIngreso());
			psd.setInt(2, paciente.getCodigoUltimaViaIngreso());
			psd.setString(3, paciente.getCodigoTipoPaciente());
			psd.setInt(4, paciente.getCodigoPersona());
			logger.info(psd);
			int resultado=psd.executeUpdate();
			psd.close();
			UtilidadBD.cerrarConexion(con);
			return resultado>0;
		}catch (SQLException e) {
			logger.error("Error actualizando los datos de la autorización", e);
			return false;
		}
	}
}
