package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;

public class SqlBaseConsultarContratosEntidadesSubcontratadasDao
{	
	private static Logger logger = Logger.getLogger(SqlBaseConsultarContratosEntidadesSubcontratadasDao.class);
	
	private static String consultaEntidades = 	"SELECT " +
													"codigo_pk AS codigoPkEntidad, " +
													"codigo AS codigoEntidad, " +
													"razon_social AS razonSocial " +
												"FROM " +
													"entidades_subcontratadas ";

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
													"es_inventario="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
	
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
																"es_inventario="+ValoresPorDefecto.getValorFalseParaConsultas()+"";
	
	
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
	
	
	public static ArrayList obtenerGruposServicio(Connection con)
	{
		logger.info("LA CONSULTA PARA LOS GRUPOS DE SERVICIO--->\n"+consultaGruposServicio);
		ArrayList<HashMap<String, Object>> esquemas = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaGruposServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	
	public static HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios)
	{
		HashMap infoContratos = new HashMap();
		infoContratos.put("numRegistros", 0);
		String consultaContratos= 	"SELECT  DISTINCT " +  
										"ces.consecutivo AS consecutivo," +
										"ces.entidad_subcontratada AS entidad, " +
										"ces.numero_contrato AS nrocontrato, " +
										"ces.valor_contrato AS valorcontrato, " +
										"ces.fecha_inicial AS fechaini, " +
										"ces.fecha_final AS fechafin, " +
										"getdescentitadsubcontratada(ces.entidad_subcontratada) AS nombreentidad, " +
										"ces.observaciones AS observaciones, " +
										"ces.fecha_firma_contrato AS fechafirma," +
										"ces.fecha_modifica AS fechamodifica," +
										"ces.hora_modifica AS horamodifica," +
										"ces.usuario_modifica AS usuariomodifica, " +
										"ti.fecha_vigencia AS vigeninventario,"+
										"ts.fecha_vigencia AS vigenservicio,"+
										"autori.estado AS estadoautoriz,"+
										"ces.tipo_tarifa AS tipoTarifa " +										
									"FROM " +
										"contratos_entidades_sub ces " +
									"LEFT OUTER JOIN " +
										"tarifas_inv_con_ent_sub ti ON (ti.contrato_entidad_sub=ces.consecutivo) " +
									"LEFT OUTER JOIN  " +
										"tarifas_proc_con_ent_sub ts ON (ts.contrato_entidad_sub=ces.consecutivo) " +
										"LEFT OUTER JOIN "+
										"autorizaciones_entidades_sub autori ON (ces.entidad_subcontratada=autori.entidad_autorizada_sub) "+
									"WHERE ";
		
		if (!encabezado.get("codigoentidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
			consultaContratos+=		"ces.entidad_subcontratada="+encabezado.get("codigoentidad").toString()+" AND ";
		
		if (!encabezado.get("nrocontrato").toString().equals(""))
			consultaContratos+=		"ces.numero_contrato='"+encabezado.get("nrocontrato").toString()+"' AND ";
		
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
		consultaContratos+=		"1=1 ORDER BY fechaini DESC,fechafin DESC";
		
		
		logger.info("LA CONSULTA DEL ENCABEZADO DE LOS CONTRATOS ES--->\n"+consultaContratos);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			infoContratos = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaContratos)));
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaContratos / "+e);
		}
		return infoContratos;
	}
	
	@SuppressWarnings("rawtypes")
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
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaInv,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			infoInv = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultaContratos / "+e);
		}
		
		return infoInv;
	}
	
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
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo de de tarifa 
	 * manejado en el contrato de una entidad subcontratada, a través
	 * de la entidad subcontratada y el número de contrato
	 * 
	 * @param Connection con, DtoContratoEntidadSub dto
	 * @return DtoContratoEntidadSub
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static DtoContratoEntidadSub  consultarTipoTarifaEntidadSubcontratada(Connection con, DtoContratoEntidadSub dto)
	{
		Date fechaActual = new Date(Calendar.getInstance().getTime().getTime());		
		String query = 	"SELECT tipo_tarifa " +									
								"FROM facturacion.contratos_entidades_sub "+
								"where entidad_subcontratada = ? and numero_contrato = ? and fecha_final >= ? " ;		
		try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setLong(1,dto.getEntidad().getCodigoPk());
			ps.setString(2, dto.getNumeroContrato());
			ps.setDate(3, fechaActual);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				dto.setTipoTarifa(rs.getString(1));
			}
			
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consulta Contratos de Entidades Subcontratadas / "+e);
		}
		return dto;
	}
	
	
}