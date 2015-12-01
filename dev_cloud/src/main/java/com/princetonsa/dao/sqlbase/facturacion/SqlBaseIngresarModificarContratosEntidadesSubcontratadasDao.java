package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

public class SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao
{	
	private static Logger logger = Logger.getLogger(SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.class);
	
	private static String consultaEntidades = 	"SELECT " +
													"codigo_pk AS codigoPkEntidad, " +
													"codigo AS codigoEntidad, " +
													"razon_social AS razonSocial " +
												"FROM " +
													"entidades_subcontratadas " +
												"WHERE " +
													"activo='"+ConstantesBD.acronimoSi+"'";
	
	private static String consultaInventarios = "SELECT " +
													"codigo AS codigoInventario," +
													"nombre AS nombreInventario " +
												"FROM " +
													"clase_inventario";
	
	private static String consultaEsquema = 	"SELECT " +
													"codigo AS codigoEsquema, " +
													"nombre AS nombreEsquema " +
												"FROM " +
													"esquemas_tarifarios " +
												"WHERE " +
													"es_inventario="+ValoresPorDefecto.getValorTrueParaConsultas();
	
	private static String consultaGruposServicio =	"SELECT " +
														"codigo AS codigoServicio," +
														"descripcion AS descripcionServicio " +
													"FROM " +
														"grupos_servicios";
	
	private static String consultaEsquemaProcedimientos =	"SELECT " +
																"codigo AS codigoEsquemaProc, " +
																"nombre AS nombreEsquemaProc " +
															"FROM " +
																"esquemas_tarifarios " +
															"WHERE " +
																"es_inventario="+ValoresPorDefecto.getValorFalseParaConsultas();
	
	private static String consultaInfoXEntidadInv = "SELECT " +
														"ti.consecutivo AS consecutivoInv, " +
														"ti.clase_inventario AS claseInv, " +
														"ti.esquema_tarifario AS esquemaInv, " +
														"ti.fecha_vigencia AS fechaVigInv " +
														"'N' AS nuevo " +
													"FROM tarifas_inv_con_ent_sub " +
														"ti WHERE ti.contrato_entidad_sub=";
	
	private static String consultaInfoXEntidadServ = 	"SELECT " +
															"tp.consecutivo AS consecutivoServ, " +
															"tp.grupo_servicio AS grupoServ, " +
															"tp.esquema_tarifario AS esquemaServ, " +
															"tp.fecha_vigencia AS fechaVigServ " +
															"'N' AS nuevo " +
														"FROM " +
															"tarifas_proc_con_ent_sub tp " +
														"WHERE " +
															"tp.contrato_entidad_sub="; 
	
	private static String consultaInfoXEntidadEncabezado =	"SELECT " +
																"ce.numero_contrato AS nrocontrato," +
																"ce.valor_contrato AS valorcontrato," +
																"to_char(ce.fecha_inicial, 'dd/MM/YYYY') AS fechaIni," +
																"to_char(ce.fecha_final, 'dd/MM/YYYY') AS fechaFin," +
																"to_char(ce.fecha_firma_contrato,'dd/MM/YYYY') AS fechaFirma, " +
																"ce.observaciones AS observaciones, " +
																"ce.tipo_tarifa AS tipoTarifa"+
															"FROM " +
																"contratos_entidades_sub ce " +
															"WHERE " +
																"ce.entidad_subcontratada="; 
	
	private static String ingresarEncabezado = "INSERT INTO contratos_entidades_sub " +
													"(consecutivo, " +
													"entidad_subcontratada, " +
													"numero_contrato, " +
													"valor_contrato, " +
													"fecha_inicial," +
													"fecha_final, " +
													"fecha_firma_contrato, " +
													"observaciones," +
													"institucion, " +
													"fecha_modifica," +
													"hora_modifica," +
													"usuario_modifica, " +
													"tipo_tarifa )"+
												"VALUES " +
													" (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static String ingresarTarifaInventarios = 	"INSERT INTO tarifas_inv_con_ent_sub " +
															"(consecutivo," +
															"clase_inventario," +
															"esquema_tarifario," +
															"fecha_vigencia," +
															"fecha_modifica," +
															"hora_modifica," +
															"usuario_modifica," +
															"contrato_entidad_sub," +
															"activo) " +
														"VALUES " +
															"(?,?,?,?,?,?,?,?,?)";
	
	private static String ingresarTarifaServicios = 	"INSERT INTO tarifas_proc_con_ent_sub " +
															"(consecutivo," +
															"grupo_servicio," +
															"esquema_tarifario," +
															"fecha_vigencia," +
															"fecha_modifica," +
															"hora_modifica," +
															"usuario_modifica," +
															"contrato_entidad_sub," +
															"activo) " +
														"VALUES " +
															"(?,?,?,?,?,?,?,?,?)";
	
	private static String ingresarLog = 	"INSERT INTO facturacion.log_contratos_entidades_sub " +
												"(consecutivo, " +
												"contrato_entidad_sub," +
												"numero_contrato," +
												"valor_contrato," +
												"fecha_final," +
												"fecha_firma_contrato," +
												"observaciones," +
												"fecha_modifica," +
												"hora_modifica," +
												"usuario_modifica," +												
												"tipo_tarifa) " +
											"VALUES " +
												"(?,?,?,?,?,?,?,?,?,?,?)";
	
	private static String modificarContratosEntidades =		"UPDATE " +
																"contratos_entidades_sub " +
															"SET " +
																"entidad_subcontratada=?," +
																"numero_contrato=?," +
																"valor_contrato=?," +
																"fecha_final=?," +
																"fecha_firma_contrato=?," +
																"observaciones=?, " +
																"fecha_modifica=?," +
																"hora_modifica=?," +
																"usuario_modifica=?, " +
																"tipo_tarifa=? " +
															"WHERE " +
																"consecutivo=?";
	
	private static String eliminarEsquemaInv =		"UPDATE " +
														"tarifas_inv_con_ent_sub " +
													"SET " +
														"activo='"+ConstantesBD.acronimoNo+"', " +
														"fecha_modifica=?," +
														"hora_modifica=?," +
														"usuario_modifica=?," +
														"fecha_inactivacion=?," +
														"hora_inactivacion=?," +
														"usuario_inactivacion=? " +
													"WHERE " +
														"consecutivo=?"; 
	
	private static String eliminarEsquemaServ =		"UPDATE " +
														"tarifas_proc_con_ent_sub " +
													"SET " +
														"activo='"+ConstantesBD.acronimoNo+"', " +
														"fecha_modifica=?," +
														"hora_modifica=?," +
														"usuario_modifica=?," +
														"fecha_inactivacion=?," +
														"hora_inactivacion=?," +
														"usuario_inactivacion=? " +
													"WHERE " +
														"consecutivo=?";
	
	
	@SuppressWarnings("rawtypes")
	public static ArrayList obtenerEntidades(Connection con)
	{
		logger.info("LA CONSULTA PARA LAS ENTIDADES ES--->\n"+consultaEntidades);
		ArrayList<HashMap<String, Object>> entidades = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaEntidades,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigoPkEntidad", rs.getString("codigoPkEntidad"));
				mapa.put("codigoEntidad", rs.getString("codigoEntidad"));
				mapa.put("razonSocial", rs.getString("RazonSocial"));
				entidades.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return entidades;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static ArrayList obtenerClaseInventarios(Connection con)
	{
		logger.info("LA CONSULTA PARA LOS INVENTARIOS--->\n"+consultaInventarios);
		ArrayList<HashMap<String, Object>> inventarios = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaInventarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigoInventario", rs.getString("codigoInventario"));
				mapa.put("nombreInventario", rs.getString("nombreInventario"));
				inventarios.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return inventarios;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static ArrayList obtenerEsquemas(Connection con)
	{
		logger.info("LA CONSULTA PARA LOS ESQUEMAS DE INVENTARIO--->\n"+consultaEsquema);
		ArrayList<HashMap<String, Object>> esquemas = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaEsquema,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigoEsquema", rs.getString("codigoEsquema"));
				mapa.put("nombreEsquema", rs.getString("nombreEsquema"));
				esquemas.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return esquemas;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static ArrayList obtenerGruposServicio(Connection con, String activos)
	{
		logger.info("LA CONSULTA PARA LOS GRUPOS DE SERVICIO--->\n"+consultaGruposServicio);
		String consultaStr=consultaGruposServicio;
		if(!UtilidadTexto.isEmpty(activos))
		{	
			consultaStr+=" WHERE activo= "+UtilidadTexto.getBooleanSegunTipoBD(UtilidadTexto.getBoolean(activos));
		}			
		ArrayList<HashMap<String, Object>> esquemas = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigoServicio", rs.getString("codigoServicio"));
				mapa.put("descripcionServicio", rs.getString("descripcionServicio"));
				esquemas.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return esquemas;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static ArrayList obtenerEsquemasProcedimientos(Connection con)
	{
		logger.info("LA CONSULTA PARA LOS ESQUEMAS DE PROCEDIMIENTOS--->\n"+consultaEsquemaProcedimientos);
		ArrayList<HashMap<String, Object>> esquemasProcedimientos = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaEsquemaProcedimientos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigoEsquemaProc", rs.getString("codigoEsquemaProc"));
				mapa.put("nombreEsquemaProc", rs.getString("nombreEsquemaProc"));
				esquemasProcedimientos.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return esquemasProcedimientos;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static HashMap obtenerInfoXEntidadInv(Connection con, String entidad)
	{
		HashMap infoXEntidad = new HashMap();		
		logger.info("LA CONSULTA DE LOS INVENTARIOS X ENTIDAD ES--->\n"+consultaInfoXEntidadInv+entidad);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaInfoXEntidadInv+entidad, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			infoXEntidad=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaInfoXEntidadInv / "+e);
		}
		Utilidades.imprimirMapa(infoXEntidad);
		return infoXEntidad;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static HashMap obtenerInfoXEntidadServ(Connection con, String entidad)
	{
		HashMap infoXEntidad = new HashMap();
		
		logger.info("LA CONSULTA DE LOS PROCEDIMIENTOS X ENTIDAD ES--->\n"+consultaInfoXEntidadServ+entidad);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaInfoXEntidadServ+entidad, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			infoXEntidad=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaInfoXEntidadServ / "+e);
		}
		Utilidades.imprimirMapa(infoXEntidad);
		return infoXEntidad;
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap consultaInfoXEntidadEncabezado(Connection con, String entidad)
	{
		HashMap infoXEntidad = new HashMap();
		
		logger.info("LA CONSULTA DE LOS PROCEDIMIENTOS X ENTIDAD ES--->\n"+consultaInfoXEntidadEncabezado+entidad);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaInfoXEntidadEncabezado+entidad, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			infoXEntidad=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaInfoXEntidadServ / "+e);
		}
		Utilidades.imprimirMapa(infoXEntidad);
		return infoXEntidad;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static long guardarEncabezado(Connection con, HashMap encabezadoEntidad)
	{
		long transaccionExitosa = 0;
		try {
			logger.info("VALOR DEL CONSECUTIVO DE LA ENT SUB--->"+encabezadoEntidad.get("codigoentidad").toString());
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarEncabezado, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setLong(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_cont_ent_sub"));
			ps.setLong(2,Utilidades.convertirAEntero(encabezadoEntidad.get("codigoentidad").toString()));
			ps.setString(3,encabezadoEntidad.get("nrocontrato").toString());
			ps.setDouble(4,Utilidades.convertirADouble(encabezadoEntidad.get("valorcontrato").toString()));
			ps.setString(5,UtilidadFecha.conversionFormatoFechaABD(encabezadoEntidad.get("fechaini").toString()));
			ps.setString(6,UtilidadFecha.conversionFormatoFechaABD(encabezadoEntidad.get("fechafin").toString()));
			if (!encabezadoEntidad.get("fechafirma").toString().equals(""))
				ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(encabezadoEntidad.get("fechafirma").toString()));
			else
				ps.setNull(7,Types.VARCHAR);
			ps.setString(8,encabezadoEntidad.get("observaciones").toString());
			ps.setString(9,encabezadoEntidad.get("institucion").toString());
			ps.setString(10,UtilidadFecha.conversionFormatoFechaABD(encabezadoEntidad.get("fechamodifica").toString()));
			ps.setString(11,encabezadoEntidad.get("horamodifica").toString());
			ps.setString(12,encabezadoEntidad.get("usuariomodifica").toString());
			ps.setString(13,encabezadoEntidad.get("tipoTarifa").toString());
			if(ps.executeUpdate()>0)
				transaccionExitosa=UtilidadBD.obtenerUltimoValorSecuencia(con, "facturacion.seq_cont_ent_sub");
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / ingresarEncabezado / "+e);
			transaccionExitosa=0;
		}
		logger.info("REALICE LA INSERCION DEL ENCABEZADO DE CONTRATO ENTIDADES SUBCONTRATADAS");
		return transaccionExitosa;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean guardarEsquemasInventarios(Connection con, HashMap esquemaInventarios)
	{
		boolean transaccionExitosa = false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarTarifaInventarios, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setLong(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_tar_inv_con_ent_sub"));
			logger.info("EL ESQUEMA------->"+esquemaInventarios.get("claseinventario").toString());
			if (!esquemaInventarios.get("claseinventario").toString().equals(" "))
				ps.setInt(2,Utilidades.convertirAEntero(esquemaInventarios.get("claseinventario").toString()));
			else
				ps.setNull(2,Types.INTEGER);
			ps.setInt(3,Utilidades.convertirAEntero(esquemaInventarios.get("esquematarinv").toString()));
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(esquemaInventarios.get("fechavigclaseinv").toString()));
			ps.setString(5,UtilidadFecha.conversionFormatoFechaABD(esquemaInventarios.get("fechamodifica").toString()));
			ps.setString(6,esquemaInventarios.get("horamodifica").toString());
			ps.setString(7,esquemaInventarios.get("usuariomodifica").toString());
			ps.setLong(8,Utilidades.convertirALong(esquemaInventarios.get("contratoentidad").toString()));
			ps.setString(9,esquemaInventarios.get("activo").toString());
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / ingresarEncabezado / "+e);
			transaccionExitosa=false;
		}
		logger.info("REALICE LA INSERCION DEL ESQUEMA DE INVENTARIOS");
		return transaccionExitosa;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean guardarEsquemasServicios(Connection con, HashMap esquemaServicio)
	{
		boolean transaccionExitosa = false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarTarifaServicios, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setLong(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_tar_proc_con_ent_sub"));
			if (!esquemaServicio.get("gruposervicio").toString().equals(" "))
				ps.setInt(2,Utilidades.convertirAEntero(esquemaServicio.get("gruposervicio").toString()));
			else
				ps.setNull(2,Types.INTEGER);
			ps.setInt(3,Utilidades.convertirAEntero(esquemaServicio.get("esquematarser").toString()));
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(esquemaServicio.get("fechvigproc").toString()));
			ps.setString(5,UtilidadFecha.conversionFormatoFechaABD(esquemaServicio.get("fechamodifica").toString()));
			ps.setString(6,esquemaServicio.get("horamodifica").toString());
			ps.setString(7,esquemaServicio.get("usuariomodifica").toString());
			ps.setLong(8,Utilidades.convertirALong(esquemaServicio.get("contratoentidad").toString()));
			ps.setString(9,esquemaServicio.get("activo").toString());
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / ingresarEncabezado / "+e);
			transaccionExitosa=false;
		}
		logger.info("REALICE LA INSERCION DEL ESQUEMA DE SERVICIOS");
		return transaccionExitosa;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios)
	{
		HashMap infoContratos = new HashMap();
		infoContratos.put("numRegistros", 0);
		String consultaContratos= 	"SELECT  DISTINCT " +    
		"ces.consecutivo AS consecutivo," +
		"ces.entidad_subcontratada AS entidad, " +
		"ces.numero_contrato AS nrocontrato, " +
		"ces.valor_contrato AS valorcontrato, " +
		"to_char(ces.fecha_inicial,'yyyy-mm-dd') AS fechaini, " +
		"to_char(ces.fecha_final,'yyyy-mm-dd') AS fechafin, " +
		"getdescentitadsubcontratada(ces.entidad_subcontratada) AS nombreentidad, " +
		"ces.observaciones AS observaciones, " +
		"to_char(ces.fecha_firma_contrato,'yyyy-mm-dd') AS fechafirma," +
		"to_char(ces.fecha_modifica,'yyyy-mm-dd') AS fechamodifica," +
		"ces.hora_modifica AS horamodifica," +
		"ces.usuario_modifica AS usuariomodifica, " +
//		"ti.fecha_vigencia AS vigeninventario,"+
//		"ts.fecha_vigencia AS vigenservicio,"+
		//"autori.estado AS estadoautoriz,"+
		"ces.tipo_tarifa AS tipoTarifa " +										
	"FROM " +
		"contratos_entidades_sub ces " +										
	"LEFT OUTER JOIN " +
		"tarifas_inv_con_ent_sub ti ON (ti.contrato_entidad_sub=ces.consecutivo) " +
	"LEFT OUTER JOIN  " +
		"tarifas_proc_con_ent_sub ts ON (ts.contrato_entidad_sub=ces.consecutivo) " +
	/*"LEFT OUTER JOIN "+
		"autorizaciones_entidades_sub autori ON (ces.entidad_subcontratada=autori.entidad_autorizada_sub) "+*/
	"WHERE ";
		
		if (!encabezado.get("codigoentidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
			consultaContratos+=		"ces.entidad_subcontratada="+encabezado.get("codigoentidad").toString()+" AND ";
		
		if (!encabezado.get("nrocontrato").toString().equals(""))
			consultaContratos+=		"ces.numero_contrato='"+encabezado.get("nrocontrato").toString()+"' AND ";
		
		logger.info("\n\n\n--Consultar contratos en SQLBase--> Tipotarifa es: "+encabezado.get("tipoTarifa").toString()+"\n\n\n");
		if (!encabezado.get("tipoTarifa").toString().equals(ConstantesBD.codigoNuncaValido+""))
			consultaContratos+=		"ces.tipo_tarifa='"+encabezado.get("tipoTarifa")+"' AND ";
				
		if (!encabezado.get("fechaini").toString().equals("")&&!encabezado.get("fechafin").toString().equals(""))
			consultaContratos+=		"((ces.fecha_inicial BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fechaini").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fechafin").toString())+"') OR "
									+"(ces.fecha_final BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fechaini").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fechafin").toString())+"')) AND ";
	
		if(encabezado.get("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
		{
			
			if (!inventarios.get("claseinventario").toString().equals(ConstantesBD.codigoNuncaValido+""))
				consultaContratos+=		"ti.clase_inventario="+inventarios.get("claseinventario").toString()+" AND ";
		
			if (!inventarios.get("esquematarinv").toString().equals(ConstantesBD.codigoNuncaValido+""))
				consultaContratos+=		"ti.esquema_tarifario="+inventarios.get("esquematarinv").toString()+" AND ";
		
			if (!inventarios.get("fechavigclaseinv").toString().equals(""))
				consultaContratos+=		"ti.fecha_vigencia='"+UtilidadFecha.conversionFormatoFechaABD(inventarios.get("fechavigclaseinv").toString())+"' AND ";
		
			if (!servicios.get("gruposervicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				consultaContratos+=		"ts.grupo_servicio="+servicios.get("gruposervicio").toString() +" AND ";
		
			if (!servicios.get("esquematarser").toString().equals(ConstantesBD.codigoNuncaValido+""))
				consultaContratos+=		"ts.esquema_tarifario="+servicios.get("esquematarser").toString() +" AND ";
		
			if (!servicios.get("fechvigproc").toString().equals(""))
				consultaContratos+=		"ts.fecha_vigencia='"+UtilidadFecha.conversionFormatoFechaABD(servicios.get("fechvigproc").toString())+"' AND ";
		}
		
		consultaContratos+=		"1=1 ORDER BY fechaini DESC,fechafin DESC ";
		
		logger.info("LA CONSULTA DEL ENCABEZADO DE LOS CONTRATOS ES----->\n"+consultaContratos);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			infoContratos = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaContratos)));
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaContratos / "+e);
		}
		return infoContratos;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarEsquemasInventarios(Connection con)
	{
		HashMap infoInv = new HashMap();
		infoInv.put("numRegistros", 0);
		String consultaInv = 	"SELECT DISTINCT " +
									"ti.consecutivo AS consecutivo, " +
									"ti.clase_inventario AS claseinv, " +
									"ti.esquema_tarifario AS esquemainv, " +
									"ti.fecha_vigencia AS fechavigenciainv, " +
									"ti.contrato_entidad_sub AS contrato, " +
									"getnombreclaseinventario(ti.clase_inventario) AS nombreclaseinv, " +
									"getnombreesquematarifario(ti.esquema_tarifario) AS nombreesquema," +
									"ti.activo AS activo " +
								"FROM " +
									"tarifas_inv_con_ent_sub ti "+
								"ORDER BY fechavigenciainv DESC";
		
		
		logger.info("LA CONSULTA DEL ENCABEZADO DE LOS ESQUEMAS DE INVENTARIOS--->\n"+consultaInv);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			infoInv = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaInv)));
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaContratos / "+e);
		}
		
		return infoInv;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultaEsquemasServicios(Connection con)
	{
		HashMap infoServ = new HashMap();
		infoServ.put("numRegistros", 0);
		String consultaServ = 	"SELECT DISTINCT " +
									"ts.consecutivo AS consecutivo," +
									"ts.grupo_servicio AS gruposerv, " +
									"ts.esquema_tarifario AS esquemaserv, " +
									"ts.fecha_vigencia AS fechavigenciaserv, " +
									"ts.contrato_entidad_sub AS contrato, " +
									"getnombregruposervicio(ts.grupo_servicio) AS descgruposerv," +
									"getnombreesquematarifario(ts.esquema_tarifario) AS nombreesquema," +
									"ts.activo AS activo " +
								"FROM " +
									"tarifas_proc_con_ent_sub ts "+
								"ORDER BY fechavigenciaserv DESC";
		
		logger.info("LA CONSULTA DEL ENCABEZADO DE LOS ESQUEMAS DE SERVICIOS--->\n"+consultaServ);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			infoServ = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaServ)));
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaContratos / "+e);
		}
		
		return infoServ;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean actualizarEncabezado(Connection con, HashMap encabezadoAntiguo)
	{
		boolean transaccionExitosa = false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(ingresarLog, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setLong(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_log_con_ent_sub"));
			ps.setDouble(2,Utilidades.convertirADouble(encabezadoAntiguo.get("consecutivoantiguo").toString()));
			ps.setString(3,encabezadoAntiguo.get("nrocontratoantiguo").toString());
			ps.setDouble(4,Utilidades.convertirADouble(encabezadoAntiguo.get("valorcontratoantiguo").toString()));
			
			logger.info("LO QUE LELGA DE FECHA FINAL--->"+encabezadoAntiguo.get("fechafinantiguo").toString());
			if (!encabezadoAntiguo.get("fechafinantiguo").toString().equals(""))
				ps.setString(5,UtilidadFecha.conversionFormatoFechaABD(encabezadoAntiguo.get("fechafinantiguo").toString()));
			else
				ps.setNull(5, Types.VARCHAR);
			
			logger.info("LO QUE LELGA DE FECHA FIRMA--->"+encabezadoAntiguo.get("fechafirmaantiguo").toString());
			if (!encabezadoAntiguo.get("fechafirmaantiguo").toString().equals(""))
				ps.setString(6,UtilidadFecha.conversionFormatoFechaABD(encabezadoAntiguo.get("fechafirmaantiguo").toString()));
			else
				ps.setNull(6, Types.VARCHAR);
			
			ps.setString(7,encabezadoAntiguo.get("observacionesantiguo").toString());
			ps.setString(8,UtilidadFecha.conversionFormatoFechaABD(encabezadoAntiguo.get("fechamodificaantiguo").toString()));
			ps.setString(9,encabezadoAntiguo.get("horamodificaantiguo").toString());
			ps.setString(10,encabezadoAntiguo.get("usuariomodificaantiguo").toString());
			ps.setString(11,encabezadoAntiguo.get("tipoTarifaantiguo").toString());
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
			logger.info("\n\n\n el tipo Tarifa antiguo es: "+encabezadoAntiguo.get("tipoTarifaantiguo").toString()+"\n\n\n");
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / ingresarLog / "+e);
			transaccionExitosa=false;
		}
		
		logger.info("REALICE LA INSERCION DEL LOG "+encabezadoAntiguo);
	
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarContratosEntidades, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setDouble(1,Utilidades.convertirADouble(encabezadoAntiguo.get("codigoentidad").toString()));
			ps.setString(2,encabezadoAntiguo.get("nrocontrato").toString());
			ps.setDouble(3,Utilidades.convertirADouble(encabezadoAntiguo.get("valorcontrato").toString()));
			
			if (encabezadoAntiguo.get("fechafin").toString().equals(""))
				ps.setNull(4, Types.VARCHAR);
			else
				ps.setString(4,UtilidadFecha.conversionFormatoFechaABD(encabezadoAntiguo.get("fechafin").toString()));
			
			if (encabezadoAntiguo.get("fechafirma").toString().equals(""))
				ps.setNull(5, Types.VARCHAR);
			else
				ps.setString(5,UtilidadFecha.conversionFormatoFechaABD(encabezadoAntiguo.get("fechafirma").toString()));
			
			ps.setString(6,encabezadoAntiguo.get("observaciones").toString());
			ps.setString(7,UtilidadFecha.conversionFormatoFechaABD(encabezadoAntiguo.get("fechamodifica").toString()));
			ps.setString(8,encabezadoAntiguo.get("horamodifica").toString());
			ps.setString(9,encabezadoAntiguo.get("usuariomodifica").toString());
			ps.setDouble(11,Utilidades.convertirADouble(encabezadoAntiguo.get("consecutivo").toString()));
			ps.setString(10,encabezadoAntiguo.get("tipoTarifa").toString());
			logger.info("\n\n\n el tipo Tarifa nuevo es: "+encabezadoAntiguo.get("tipoTarifa").toString()+"\n\n\n");
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / modificarContratosEntidades / "+e);
			transaccionExitosa=false;
		}
		logger.info("REALICE LA ACTUALZIACION DE LOS CONTRATO DE ENTIDADES");
		
		return transaccionExitosa;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean eliminarEsquemasInventarios(Connection con, HashMap datos)
	{
		
		boolean transaccionExitosa = false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarEsquemaInv, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setString(1,UtilidadFecha.conversionFormatoFechaABD(datos.get("fechainactiva").toString()));
			ps.setString(2,datos.get("horainactiva").toString());
			ps.setString(3,datos.get("usuarioinactiva").toString());
			ps.setString(4,UtilidadFecha.conversionFormatoFechaABD(datos.get("fechainactiva").toString()));
			ps.setString(5,datos.get("horainactiva").toString());
			ps.setString(6,datos.get("usuarioinactiva").toString());
			ps.setLong(7,Utilidades.convertirALong(datos.get("consecutivo").toString()));
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / eliminarEsquemaInv / "+e);
			transaccionExitosa=false;
		}
		logger.info("ELIMINE LA TARIFA DE INVENTARIOS");
		return transaccionExitosa;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean eliminarEsquemasServicios(Connection con, HashMap datos)
	{
		
		boolean transaccionExitosa = false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarEsquemaServ, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setString(1,UtilidadFecha.conversionFormatoFechaABD(datos.get("fechainactiva").toString()));
			ps.setString(2,datos.get("horainactiva").toString());
			ps.setString(3,datos.get("usuarioinactiva").toString());
			ps.setString(4,UtilidadFecha.conversionFormatoFechaABD(datos.get("fechainactiva").toString()));
			ps.setString(5,datos.get("horainactiva").toString());
			ps.setString(6,datos.get("usuarioinactiva").toString());
			ps.setLong(7,Utilidades.convertirALong(datos.get("consecutivo").toString()));
			if(ps.executeUpdate()>0)
				transaccionExitosa = true;
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / eliminarEsquemaServ / "+e);
			transaccionExitosa=false;
		}
		logger.info("ELIMINE LA TARIFA DE SERVICIOS");
		return transaccionExitosa;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean verificarTraslapeContratos(Connection con, HashMap encabezado)
	{
		String verificacion = 	"SELECT " +
									"ce.consecutivo "+
								"FROM " +
									"contratos_entidades_sub ce " +
								"WHERE " +
									"('"+UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fechafin").toString())+"' " +
								"BETWEEN " +
									"ce.fecha_inicial " +
								"AND " +
									"ce.fecha_final " +
								"OR " +
									"'"+UtilidadFecha.conversionFormatoFechaABD(encabezado.get("fechaini").toString())+"' " +
								"BETWEEN " +
									"ce.fecha_inicial " +
								"AND " +
									"ce.fecha_final )" +
								"AND " +
									"ce.entidad_subcontratada="+encabezado.get("codigoentidad");
								
								
		logger.info("LA CONSULTA PARA VERIFICAR QUE NO SE TRASLAPEN LAS FECHAS ES------>"+verificacion);

		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(verificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if (new ResultSetDecorator(ps.executeQuery()).next())
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en verificarTraslapeContratos de SqlBaseUtilidadesDao : "+e);
			return false;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean comprobarExistenciaInventario(Connection con, HashMap inventarios, int indice)
	{
		return true;
	}
}
