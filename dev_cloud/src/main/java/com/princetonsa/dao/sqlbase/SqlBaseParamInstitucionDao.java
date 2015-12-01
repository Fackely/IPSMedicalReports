
/*
 * Creado   22/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.math.BigDecimal;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.RangosConsecutivos;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

/**
 * Implementación sql genérico de todas las funciones 
 * de acceso a la fuente de datos
 *
 * @version 1.0, 22/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 * 
 * 
 * Modificado el 22/01/2008
 * por Jhony Alexander Duque A
 * por el documento:
 * Cambios en funcionalidades x unidad medica santa fe anexo 542
 */
public class SqlBaseParamInstitucionDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseParamInstitucionDao.class);
	
	/**
	 * String para la inserci&oacute;n en la BD
	 */
	public static String insertarInstiStr = "INSERT INTO instituciones (codigo," +
																		"nit," +
																		"razon_social," +
																		"departamento," +
																		"ciudad," +
																		"direccion," +
																		"telefono," +
																		"cod_min_salud," +
																		"actividad_eco," +
																		"resolucion," +
																		"pref_factura," +
																		"rgo_inic_fact," +
																		"rgo_fin_fact," +
																		"path," +
																		"encabezado," +
																		"pie," +
																		"pais," +
																		"logo," +
																		"institucion_publica," +
																		"cod_emp_trans_esp," +
																		"ubicacion_logo_reportes," +
																		"indicativo," +
																		"extension," +
																		"celular, " +
																		"codigo_interfaz ," +
																		"representante_legal ," +
																		"nivel_logo," +
																		"resolucion_factura_varia, " +
															            "pref_factura_varia, " +
															            "rgo_inic_fact_varia, " +
															            "rgo_fin_fact_varia, "+
															            "encabezado_factura_varia, "+
															            "pie_factura_varia, " +
															            "pie_amb_med) "+
																		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * String de insercion de una empresa en la BD
	 */
	private static final String insertarEmpresaStr ="INSERT INTO empresas_institucion ( institucion, nit,razon_social, departamento, ciudad, direccion, " +
																		"telefono, cod_min_salud, actividad_eco, resolucion, pref_factura, " +
																		"rgo_inic_fact, rgo_fin_fact, encabezado, pie, pais, logo, vigente, " +
																		"usuario_modifica, fecha_modifica, hora_modifica, tipo_identificacion, " +
																		"valor_consecutivo_fact, anio_vigencia, digito_verificacion, institucion_publica,ubicacion_logo_reportes,codigo_interfaz ," +
																		" imprimir_firmas_emp , representante" +
																		", codigo, " +
																		"resolucion_factura_varia, " +
															            "pref_factura_varia, " +
															            "rgo_inic_fact_varia, " +
															            "rgo_fin_fact_varia, "+
															            "encabezado_factura_varia, "+
															            "pie_factura_varia, " +
															            "pie_amb_med) " +
																		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	
	
	
	
	/**
	 * Almacena la consulta de instituciones
	 */
	public static String consultarInstitucion = "SELECT " +
															"i.codigo as codigo, " +
															"i.nit as nit, " +
															"i.digito_verificacion as digv, " +
															"i.razon_social as razon, " +
															"i.departamento as depto, " +
															"i.ciudad as ciudad, " +
															"i.direccion as direccion, " +
															"i.telefono as telefono, " +
															"CASE WHEN i.cod_min_salud IS NULL THEN '' ELSE i.cod_min_salud END as minSalud, " +
															"CASE WHEN i.actividad_eco IS NULL THEN '' ELSE i.actividad_eco END as actividad, " +
															"CASE WHEN i.resolucion IS NULL THEN '' ELSE i.resolucion END as resolucion, " +
															"CASE WHEN i.pref_factura IS NULL THEN '' ELSE i.pref_factura END as prefijo, " +
															"i.rgo_inic_fact as rango_inicial, " +
															"i.rgo_fin_fact as rango_final, " +
															"i.tipo_identificacion as tipo_nit, "+
															"administracion.getnombretipoidentificacion(i.tipo_identificacion) as nombre_nit, "+
															"i.path as path, "+
															"i.encabezado AS encabezado, "+
															"i.pie AS pie, " +
															"i.pie_his_cli AS pie_his_cli, " +
															"coalesce(i.pais,'') AS pais," +
															"CASE WHEN i.logo IS NULL THEN '' ELSE i.logo END AS logo,  " +
															"i.ubicacion_logo_reportes As ubicacion_logo_reportes, " +
															"CASE WHEN i.formato_impresion IS NULL THEN '' ELSE i.formato_impresion END AS formato_impresion, " +
															"CASE WHEN i.institucion_publica = 'S' THEN 'Publica' ELSE 'Privada' END AS tipo_institucion, " +
															"coalesce (i.cod_emp_trans_esp,'') As cod_emp_trans_esp," +
															"coalesce (i.indicativo,'') AS indicativo," +
															"coalesce (i.extension,'') AS extension," +
															"coalesce (i.celular,'') AS celular," +
															"coalesce (i.codigo_interfaz,"+ConstantesBD.codigoNuncaValido+") AS codinterfaz, " +
															"representante_legal as representante_legal, " +
															"i.nivel_logo AS nivel_logo," +
															"i.resolucion_factura_varia AS resolucion_factura_varia, " +
												            "i.pref_factura_varia AS pref_factura_varia, " +
												            "i.rgo_inic_fact_varia AS rgo_inic_fact_varia, " +
												            "i.rgo_fin_fact_varia AS rgo_fin_fact_varia, "+
												            "i.encabezado_factura_varia AS encabezado_factura_varia, "+
												            "i.pie_factura_varia AS pie_factura_varia, " +
												            "i.pie_amb_med AS pie_amb_med, " ;
															
	/**
	 * String para filtrar los nombres de ciudad y depto de una institucion
	 */
	public static String consultaUnaInstitucion = 	"ti.nombre as descripcionTipoIdentificacion," +
                                                    "getNombreDepto(?,?) as nombreDepto, " +
													"getNombreCiudad(?,?,?) as nombreCiudad, " +
													"getdescripcionpais(?) as nombrePais " +
													"from administracion.instituciones i " +
                                                    "inner join administracion.tipos_identificacion ti on(ti.acronimo=i.tipo_identificacion) " +
                                                    "where i.codigo = ?";
													
	
	/**
	 * String pra filtrar los nombres de ciudad y depto de todas las instituciones
	 */
	public static String consultaTodasInstitucion = 	"ti.nombre as descripcionTipoIdentificacion," +
                                                        "getNombreDepto(i.pais,i.departamento) as nombreDepto, " +
														"getNombreCiudad(i.pais,i.departamento,i.ciudad) as nombreCiudad, " +
														"getdescripcionpais(i.pais) as nombrePais " +
														"from administracion.instituciones i " +
                                                        "inner join administracion.tipos_identificacion ti on(ti.acronimo=i.tipo_identificacion) " +
                                                        "where i.codigo = ?";
	
	/**
	 * String de consulta de los datos de las empresas
	 */
	private static final String consultaEmpresasStr ="SELECT " +
														" ei.codigo AS codigo1, " +
														" ei.nit AS nit2, " +
														" ei.digito_verificacion AS digv32, " +
														" ei.institucion AS institucion3," +
														" ei.razon_social AS razon_social4, " +
														" ei.razon_social AS desc_ent28," +
														" ei.departamento AS depto5, " +
														" ei.ciudad AS ciudad6, " +
														" ei.direccion AS direccion7, " +
														" ei.telefono AS telefono8, " +
														" CASE WHEN ei.cod_min_salud IS NULL THEN '' ELSE ei.cod_min_salud END AS min_salud9, " +
														" CASE WHEN ei.actividad_eco IS NULL THEN '' ELSE ei.actividad_eco END AS actividad10, " +
														" CASE WHEN ei.resolucion IS NULL THEN '' ELSE ei.resolucion END AS resolucion11, " +
														" CASE WHEN ei.pref_factura IS NULL THEN '' ELSE ei.pref_factura END AS prefijo12, " +
														" ei.rgo_inic_fact as rango_inicial13, " +
														" ei.rgo_fin_fact as rango_final14, " +
														" ei.tipo_identificacion as tipo_nit15, "+
														" ti.nombre as nombre_tipo_nit16, " +
														" ei.logo AS logo17,"+
														" ei.vigente AS vigente18,"+
														" ei.encabezado AS encabezado19, "+
														" ei.pie AS pie20, " +
														" coalesce(ei.pais,'') AS pais21, " +
														" ti.nombre as des_tipo_ident22," +
                                                        " getNombreDepto(ei.pais,ei.departamento) as nombre_depto23, " +
														" getNombreCiudad(ei.pais,ei.departamento,ei.ciudad) as nombre_ciudad24, " +
														" getdescripcionpais(ei.pais) as nombre_pais25," +
														" ei.ciudad || '@@@@@'|| ei.departamento AS nom_ciudad29," +
														" CASE WHEN ei.institucion_publica = 'S' THEN 'Publica' ELSE 'Privada' END AS tipoins33, " +
														"'"+ConstantesBD.acronimoSi+"' AS esta_bd26, " +
														" ei.cod_emp_trans_esp As cod_emp_trans_esp34," +
														" ei.ubicacion_logo_reportes As ubicacion_logo_reportes35, " +
														" ei.pie_his_cli AS pie_his_cli36, " +
														" ei.codigo_interfaz AS codinterfaz37 ," +
														" ei.representante AS representante38 ," +
														" ei.imprimir_firmas_emp AS imprimir_firmas_emp39, " +
														" i.razon_social AS razon_social_inst_basica, " +
														" i.codigo AS institucion_basica, " +
														" i.nivel_logo AS nivel_logo, " +
														" ei.resolucion_factura_varia AS resolucion_factura_varia, " +
											            " ei.pref_factura_varia AS pref_factura_varia, " +
											            " ei.rgo_inic_fact_varia AS rgo_inic_fact_varia, " +
											            " ei.rgo_fin_fact_varia AS rgo_fin_fact_varia, "+
											            " ei.encabezado_factura_varia AS encabezado_factura_varia, "+
											            " ei.pie_factura_varia AS pie_factura_varia, " +
											            " ei.pie_amb_med AS pie_amb_med " +
													" FROM empresas_institucion ei " +
                                                    " INNER JOIN tipos_identificacion ti ON (ti.acronimo=ei.tipo_identificacion) " +
                                                    " INNER JOIN instituciones i ON(i.codigo=ei.institucion) " +
                                                    " WHERE 1=1 ";
	/**
	 * String de modificacion de las empresas
	 */
	private static final String modificarEmpresaStr =" UPDATE empresas_institucion SET razon_social=?, nit=?, tipo_identificacion=?," + //3
															" pais=?, departamento=?, ciudad=?, direccion=?, telefono=?," + //8
															" cod_min_salud=?, actividad_eco=?, resolucion=?, pref_factura=?," + //12
															" rgo_inic_fact=?, rgo_fin_fact=?, encabezado=?, pie=?, logo=?," + //17
															" vigente=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?, " + //21
															" digito_verificacion=?, institucion_publica=?, cod_emp_trans_esp=?," +//24
															" ubicacion_logo_reportes=?, pie_his_cli=? , codigo_interfaz=? , " +//27
															" representante=?, imprimir_firmas_emp=?," + //29
															" resolucion_factura_varia=?, " + 
												            " pref_factura_varia =?, " +
												            " rgo_inic_fact_varia =?, " +
												            " rgo_fin_fact_varia =?, "+
												            " encabezado_factura_varia =?, "+
												            " pie_factura_varia =?, " + // 35
												            " pie_amb_med =? " +//36
															" WHERE codigo=? "; // 37
	
	
	private static final String eliminarEmpresaStr =" DELETE FROM  empresas_institucion WHERE codigo=?";
	
	/**
	 * indices para el manejo de las empresas
	 */
	private static final String [] indicesEmpresa=ParametrizacionInstitucion.indicesEmpresa;
	
	/**
	 * Cadena para la insercion de los tipos de moneda por cada institucion
	 */
	public static String insertarTiposMonedaInstitucion="INSERT INTO tipos_moneda_institucion (institucion,tipo_moneda,fecha_inicial,fecha_modificacion,hora_modificacion,usuario_modificacion) VALUES (?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * Cadena para la consulta de los tipos de monedas por institucion
	 */
	public static String consultarTiposMonedaInstitucion="SELECT institucion,tipo_moneda,tipo_moneda as tipo_moneda_viejo,to_char(fecha_inicial, 'DD/MM/YYYY') as fecha_inicial, '"+ConstantesBD.acronimoSi+"' as estabd FROM tipos_moneda_institucion WHERE institucion=?";
	
	/**
	 * Cadena para la modificacion de los tipos de moneda por cada institucion
	 */
	public static String modificarTiposMonedaInstitucion="UPDATE tipos_moneda_institucion SET tipo_moneda=?, fecha_inicial=?, fecha_modificacion=CURRENT_DATE,hora_modificacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_modificacion=? WHERE institucion=? AND tipo_moneda=?";
	
	/**
	 * Cadena para la elminacion de los tipos de monedas por institucion
	 */
	public static String eliminarTiposMonedaInstitucion="DELETE FROM tipos_moneda_institucion WHERE tipo_moneda=?";
	
	/**
	 * Metodo encargado de eliminar
	 * una empresa
	 * @param connection
	 * @param datos
	 * -----------------------
	 *	 KEY'S HASHMAP DATOS
	 * -----------------------
	 * -- codigo1_
	 * @return
	 */
	public static boolean eliminarEmpresas (Connection connection,HashMap datos)
	{
		logger.info("\n **** ENTRO A ELIMINAREMPRESAS *****>>"+datos);
		
		
		String cadena = eliminarEmpresaStr;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo empresa
			ps.setObject(1, datos.get(indicesEmpresa[1]));
		
			if (ps.executeUpdate()>0)
				return true; 
		}
		catch (Exception e) 
		{
			logger.error("\n Problema eliminar Empresas "+e);
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de modificar los datos de la Empresas En la BD
	 * @param connection
	 * @param datos
	 * ------------------------------------------
	 * 			KEY'S HASHMAP DATOS 
	 * ------------------------------------------
	 * -- nit2_ --> Requerido
	 * -- razonSocial4_ --> Requerido
	 * -- depto5_ --> Requerido
	 * -- ciudad6_ --> Requerido
	 * -- direccion7_ --> Requerido
	 * -- telefono8_ --> Requerido
	 * -- minSalud9_ --> Opcional
	 * -- actividad10_ --> Opcional
	 * -- resolucion11_ --> Opcional
	 * -- prefijo12_ --> Opcional
	 * -- rangoInicial13_ --> Opcional
	 * -- rangoFinal14_ --> Opcional
	 * -- tipoNit15_ --> Requerido
	 * -- logo17_ --> Requerido
	 * -- vigente18_ --> Requerido
	 * -- encabezado19_ --> Opcional
	 * -- pie20_ --> Opcional
	 * -- pais21_ --> Requerido
	 * -- usuarioModifica27_ --> Requerido
	 * -- codigo1_ --> Requerido
	 * 
	 * --- Sección Facturas Varias --- 
	 * 
	 * -- resolucion_factura_varia --> Opcional - 40
	 * -- pref_factura_varia --> Opcional - 41
	 * -- rgo_inic_fact_varia --> Opcional - 42
	 * -- rgo_fin_fact_varia --> Opcional - 43
	 * -- encabezado_factura_varia --> Opcional - 44
	 * -- pie_factura_varia --> Opcional - 45
	 * 
	 * @return
	 */
	public static boolean modificarEmpresas (Connection connection,HashMap datos)
	{
		logger.info("\n **** ENTRO A MODICAREMPRESAS *****>>"+datos);
		
		String cadena = modificarEmpresaStr;
	
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection, cadena);
			
			/****************************************************
			 * los datos insegresados al preparedstatement sin
			 * validacion son de caracter obligatorios y se validan
			 * en el form.
			 ****************************************************/
		
			//razon social
			ps.setObject(1, datos.get(indicesEmpresa[4]));
			//nit
			ps.setObject(2, datos.get(indicesEmpresa[2]));
			//tipo identificacion
			ps.setObject(3, datos.get(indicesEmpresa[15]));
			//pais
			ps.setObject(4, datos.get(indicesEmpresa[21]));
			//departamento
			ps.setObject(5, datos.get(indicesEmpresa[5]));
			//ciudad
			ps.setObject(6, datos.get(indicesEmpresa[6]));
			//direccion
			ps.setObject(7, datos.get(indicesEmpresa[7]));
			//telefono
			ps.setObject(8, datos.get(indicesEmpresa[8]));
			//codigo min salud
			if (datos.containsKey(indicesEmpresa[9]) && !(datos.get(indicesEmpresa[9])+"").equals("") && !(datos.get(indicesEmpresa[9])+"").equals("-1"))
				ps.setObject(9, datos.get(indicesEmpresa[9]));
			else
				ps.setNull(9, Types.VARCHAR);
			//actividad economica
			if (datos.containsKey(indicesEmpresa[10]) && !(datos.get(indicesEmpresa[10])+"").equals("") && !(datos.get(indicesEmpresa[10])+"").equals("-1"))
				ps.setObject(10, datos.get(indicesEmpresa[10]));
			else
				ps.setNull(10, Types.VARCHAR);
			//resolucion
			if (datos.containsKey(indicesEmpresa[11]) && !(datos.get(indicesEmpresa[11])+"").equals("") && !(datos.get(indicesEmpresa[11])+"").equals("-1"))
				ps.setObject(11, datos.get(indicesEmpresa[11]));
			else
				ps.setNull(11, Types.VARCHAR);
			//prefijo factura
			if (datos.containsKey(indicesEmpresa[12]) && !(datos.get(indicesEmpresa[12])+"").equals("") && !(datos.get(indicesEmpresa[12])+"").equals("-1"))
				ps.setObject(12, datos.get(indicesEmpresa[12]));
			else
				ps.setNull(12, Types.VARCHAR);
			//rango inicial factura
			if (datos.containsKey(indicesEmpresa[13]) && !(datos.get(indicesEmpresa[13])+"").equals("") && !(datos.get(indicesEmpresa[13])+"").equals("-1"))
				ps.setObject(13, datos.get(indicesEmpresa[13]));
			else
				ps.setNull(13, Types.NUMERIC);
			//rango final factura
			if (datos.containsKey(indicesEmpresa[14]) && !(datos.get(indicesEmpresa[14])+"").equals("") && !(datos.get(indicesEmpresa[14])+"").equals("-1"))
				ps.setObject(14, datos.get(indicesEmpresa[14]));
			else
				ps.setNull(14, Types.NUMERIC);
			//encabezado
			if (datos.containsKey(indicesEmpresa[19]) && !(datos.get(indicesEmpresa[19])+"").equals("") && !(datos.get(indicesEmpresa[19])+"").equals("-1"))
				ps.setObject(15, datos.get(indicesEmpresa[19]));
			else
				ps.setNull(15, Types.VARCHAR);
			//pie
			if (datos.containsKey(indicesEmpresa[20]) && !(datos.get(indicesEmpresa[20])+"").equals("") && !(datos.get(indicesEmpresa[20])+"").equals("-1"))
				ps.setObject(16, datos.get(indicesEmpresa[20]));
			else
				ps.setNull(16, Types.VARCHAR);
			
			//Logo
			ps.setObject(17, datos.get(indicesEmpresa[17]));
			//vigente
			ps.setObject(18, datos.get(indicesEmpresa[18]));
			//usuario modifica
			ps.setObject(19, datos.get(indicesEmpresa[27]));
			//fecha modifica
			ps.setObject(20, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			//hora modifica
			ps.setObject(21, UtilidadFecha.getHoraActual());
			//digito verificacion
			if(datos.get(indicesEmpresa[32]).toString().equals(""))
			{
				ps.setObject(22, null);
			}
			else
			{
				ps.setObject(22, Utilidades.convertirAEntero(datos.get(indicesEmpresa[32]).toString()));
			}
			//Tipo institucion
			if(datos.get(indicesEmpresa[33]).toString().equals(""+ConstantesBD.codigoNuncaValido))
			{
				ps.setObject(23, null);
			}
			else
			{
				ps.setObject(23, datos.get(indicesEmpresa[33]).toString());
			}
			//cod_emp_trans_esp
			ps.setString(24, datos.get(indicesEmpresa[34])+"");//este dato no debe llegar vacio, esto se valida desde el
			 
			//ubicacion_logo_reportes
			ps.setString(25, datos.get(indicesEmpresa[35])+"");
			
			//pie_his_cli
			ps.setString(26, datos.get(indicesEmpresa[36])+"");
			
			//codigo Interfaz
			if (datos.containsKey(indicesEmpresa[37]) && !(datos.get(indicesEmpresa[37])+"").equals("") && !(datos.get(indicesEmpresa[37])+"").equals("-1"))
				ps.setObject(27, datos.get(indicesEmpresa[37]));
			else
				ps.setNull(27, Types.NUMERIC);
			
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[38])+""))
			{
				ps.setString(28, datos.get(indicesEmpresa[38]+"").toString())	;
			}
			else
			{
				ps.setNull(28, Types.VARCHAR);
			}

			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[39])+""))
			{
				ps.setString(29, datos.get(indicesEmpresa[39]).toString())	;
			}
			else
			{
				ps.setNull(29, Types.VARCHAR);
			}
			
			/**
			 * Sección Facturas Varias
			 */
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[40])+""))
			{
		    	ps.setString(30, datos.get(indicesEmpresa[40])+"");
			}
			else
			{
				ps.setNull(30, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[41])+""))
			{
				ps.setString(31, datos.get(indicesEmpresa[41])+"");
			}
			else
			{
				ps.setNull(31, Types.VARCHAR);
			}
			
			if(datos.get(indicesEmpresa[42])+""!=null
				&& !UtilidadTexto.isEmpty(datos.get(indicesEmpresa[42])+"")
				&& UtilidadTexto.isNumber(datos.get(indicesEmpresa[42])+""))
			{
				ps.setBigDecimal(32, new BigDecimal(datos.get(indicesEmpresa[42])+""));
			}
			else
			{
				ps.setNull(32, Types.NUMERIC);
			}
			
			if(datos.get(indicesEmpresa[43])+""!=null  
					&& !UtilidadTexto.isEmpty(datos.get(indicesEmpresa[43])+"")
					&& UtilidadTexto.isNumber(datos.get(indicesEmpresa[43])+""))
			{
				ps.setBigDecimal(33, new BigDecimal(datos.get(indicesEmpresa[43])+""));
			}
			else
			{
				ps.setNull(33, Types.NUMERIC);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[44])+""))
			{
		    	ps.setString(34, datos.get(indicesEmpresa[44])+"");
			}
			else
			{
				ps.setNull(34, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[45])+""))
			{
				ps.setString(35, datos.get(indicesEmpresa[45])+"");
			}
			else
			{
				ps.setNull(35, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[46])+""))//FIXME
			{
				ps.setString(36, datos.get(indicesEmpresa[46])+"");
			}
			else
			{
				ps.setNull(36, Types.VARCHAR);
			}
			
			//codigo empresa_institucion
			ps.setObject(37, datos.get(indicesEmpresa[1]));
			
			
			Log4JManager.info("imprmir++"+ps);
			
			if (ps.executeUpdate()>0)
				return true; 
		} 
		catch (SQLException e)
		{
			logger.error("\n Problema modificando Empresas "+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de consultar las empresas 
	 * pertenecientes a una institucion. 
	 * @param connection
	 * @param criterios
	 * --------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * --------------------------------------
	 * -- codigo1_
	 * -- institucion3_
	 * 
	 * @return HashMap mapa
	 * ----------------------------------
	 * 		KEY'S DEL HASHMAP MAPA
	 * ----------------------------------
	 * codigo1_, nit2_ , institucion3_ , razonSocial4_,
	 * depto5_, ciudad6_, direccion7_, telefono8_,
	 * minSalud9_, actividad10_, resolucion11_, prefijo12_,
	 * rangoInicial13_, rangoFinal14_, tipoNit15_, 
	 * nombreTipoNit16_, logo17_, vigente18_, encabezado19_,
	 * pie20_, pais21_, desTipoIdent22_, nombreDepto23_,
	 * nombreCiudad24_, nombrePais25_, estaBd26_, descEnt28_
	 */
	public static HashMap consultarEmpresas (Connection connection, HashMap criterios)
	{
		logger.info("\n*******************************");
		logger.info("\n ENTRO A CONSULTAR EMPRESAS SQLBASE \n");
		logger.info("*******************************\n");
		
		HashMap mapa = new HashMap ();
		String cadena = consultaEmpresasStr,where="";
		//codigo empresa
		if (criterios.containsKey(indicesEmpresa[1]) && !(criterios.get(indicesEmpresa[1])+"").equals("") && !(criterios.get(indicesEmpresa[1])+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			where+=" AND ei.codigo="+criterios.get(indicesEmpresa[1]);
		}
		//codigo institucion
		if (criterios.containsKey(indicesEmpresa[3]) && !(criterios.get(indicesEmpresa[3])+"").equals("") && !(criterios.get(indicesEmpresa[3])+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			where+=" AND ei.institucion="+criterios.get(indicesEmpresa[3]);
		}
		
		cadena+=where;
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try
		{
			ps =  new PreparedStatementDecorator(connection, cadena);
			rs = new ResultSetDecorator(ps.executeQuery());
			logger.info("\n \n La cadena de consulta de empresas es ==> "+ps);
			mapa = UtilidadBD.cargarValueObject(rs,true,true);
		}
		catch (SQLException e)
		{
			logger.error("\n Problema consultando Empresas ", e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		logger.info("\n al salir de buscar buscarEmpresas " +mapa);
		return mapa;
	}
	
	
	/**
	 * Metodo encargado de insertar Empresas En la BD
	 * @param connection
	 * @param datos
	 * ------------------------------------------
	 * 			KEY'S HASHMAP DATOS 
	 * ------------------------------------------
	 * -- nit2_ --> Requerido
	 * -- institucion3_ --> Requerido
	 * -- razonSocial4_ --> Requerido
	 * -- depto5_ --> Requerido
	 * -- ciudad6_ --> Requerido
	 * -- direccion7_ --> Requerido
	 * -- telefono8_ --> Requerido
	 * -- minSalud9_ --> Opcional
	 * -- actividad10_ --> Opcional
	 * -- resolucion11_ --> Opcional
	 * -- prefijo12_ --> Opcional
	 * -- rangoInicial13_ --> Opcional
	 * -- rangoFinal14_ --> Opcional
	 * -- tipoNit15_ --> Requerido
	 * -- logo17_ --> Requerido
	 * -- vigente18_ --> Requerido
	 * -- encabezado19_ --> Opcional
	 * -- pie20_ --> Opcional
	 * -- pais21_ --> Requerido
	 * -- usuarioModifica27_ --> Requerido
	 * 
	 * 
	 * 	 * --- Sección Facturas Varias --- 
	 * 
	 * -- resolucion_factura_varia --> Opcional - 40
	 * -- pref_factura_varia --> Opcional - 41
	 * -- rgo_inic_fact_varia --> Opcional - 42
	 * -- rgo_fin_fact_varia --> Opcional - 43
	 * -- encabezado_factura_varia --> Opcional - 44
	 * -- pie_factura_varia --> Opcional - 45
	 * 
	 * --pie_amb_med -->opcional - 46
	 * 
	 * @return
	 */
	public static double insertarEmpresas (Connection connection, HashMap datos)
	{
		logger.info("\n*******************************");
		logger.info("\n ENTRO A INSERTAR EMPRESAS SQLBASE \n" +datos);
		logger.info("*******************************\n");
		
		String cadena = insertarEmpresaStr;
		
		double secuencia = ConstantesBD.codigoNuncaValidoDouble;
		
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection , cadena);
			/****************************************************
			 * los datos insegresados al preparedstatement sin
			 * validacion son de caracter obligatorios y se validan
			 * en el form.
			 ****************************************************/
			
			secuencia= UtilidadBD.obtenerSiguienteValorSecuencia(connection, "facturacion.seq_empresas_institucion");

			//institucion
			ps.setInt(1, Integer.parseInt(datos.get(indicesEmpresa[3])+""));
			//nit
			ps.setString(2, datos.get(indicesEmpresa[2])+"");
			//razon social
			ps.setString(3, datos.get(indicesEmpresa[4])+"");
			//departamento
			ps.setString(4, datos.get(indicesEmpresa[5])+"");
			//ciudad
			ps.setString(5, datos.get(indicesEmpresa[6])+"");
			//direccion
			ps.setString(6, datos.get(indicesEmpresa[7])+"");
			//telefono
			ps.setInt(7, Integer.parseInt(datos.get(indicesEmpresa[8])+""));
			//codigo min salud
			if (datos.containsKey(indicesEmpresa[9]) && !(datos.get(indicesEmpresa[9])+"").equals("") && !(datos.get(indicesEmpresa[9])+"").equals("-1"))
				ps.setString(8, datos.get(indicesEmpresa[9])+"");
			else
				ps.setNull(8, Types.VARCHAR);
			//actividad economica
			if (datos.containsKey(indicesEmpresa[10]) && !(datos.get(indicesEmpresa[10])+"").equals("") && !(datos.get(indicesEmpresa[10])+"").equals("-1"))
				ps.setString(9, datos.get(indicesEmpresa[10])+"");
			else
				ps.setNull(9, Types.VARCHAR);
			//resolucion
			if (datos.containsKey(indicesEmpresa[11]) && !(datos.get(indicesEmpresa[11])+"").equals("") && !(datos.get(indicesEmpresa[11])+"").equals("-1"))
				ps.setString(10, datos.get(indicesEmpresa[11])+"");
			else
				ps.setNull(10, Types.VARCHAR);
			//prefijo factura
			if (datos.containsKey(indicesEmpresa[12]) && !(datos.get(indicesEmpresa[12])+"").equals("") && !(datos.get(indicesEmpresa[12])+"").equals("-1"))
				ps.setString(11, datos.get(indicesEmpresa[12])+"");
			else
				ps.setNull(11, Types.VARCHAR);
			//rango inicial factura
			if (datos.containsKey(indicesEmpresa[13]) && !(datos.get(indicesEmpresa[13])+"").equals("") && !(datos.get(indicesEmpresa[13])+"").equals("-1"))
				ps.setInt(12, Integer.parseInt(datos.get(indicesEmpresa[13])+""));
			else
				ps.setNull(12, Types.NUMERIC);
			//rango final factura
			if (datos.containsKey(indicesEmpresa[14]) && !(datos.get(indicesEmpresa[14])+"").equals("") && !(datos.get(indicesEmpresa[14])+"").equals("-1"))
				ps.setInt(13, Integer.parseInt(datos.get(indicesEmpresa[14])+""));
			else
				ps.setNull(13, Types.NUMERIC);
			//encabezado
			if (datos.containsKey(indicesEmpresa[19]) && !(datos.get(indicesEmpresa[19])+"").equals("") && !(datos.get(indicesEmpresa[19])+"").equals("-1"))
				ps.setString(14, datos.get(indicesEmpresa[19])+"");
			else
				ps.setNull(14, Types.VARCHAR);
			//pie
			if (datos.containsKey(indicesEmpresa[20]) && !(datos.get(indicesEmpresa[20])+"").equals("") && !(datos.get(indicesEmpresa[20])+"").equals("-1"))
				ps.setString(15, datos.get(indicesEmpresa[20])+"");
			else
				ps.setNull(15, Types.VARCHAR);
			//pais
			ps.setString(16, datos.get(indicesEmpresa[21])+"");
			
			//Logo
			ps.setString(17, datos.get(indicesEmpresa[17])+"");
			
			//vigente
			if(datos.get(indicesEmpresa[18])!=null){
				ps.setString(18, datos.get(indicesEmpresa[18])+"");
			}
			
			//usuario modifica
			ps.setString(19, datos.get(indicesEmpresa[27])+"");
			
			//fecha modifica
			ps.setDate(20, new Date(UtilidadFecha.getFechaActualTipoBD().getTime()));
			//hora modifica
			ps.setString(21, UtilidadFecha.getHoraActual());
			//tipo identificacion
			ps.setString(22, datos.get(indicesEmpresa[15])+"");
			//valor consecutivo factura
			ps.setNull(23, Types.NUMERIC);
			//anio vigencia
			ps.setNull(24, Types.VARCHAR);
			//digito Verificacion
			if(datos.get(indicesEmpresa[32]).toString().equals(""))
			{
				ps.setObject(25, null);
			}
			else
			{
				ps.setInt(25, Utilidades.convertirAEntero(datos.get(indicesEmpresa[32]).toString()));
			}
//			digito Verificacion
			if(datos.get(indicesEmpresa[33]).toString().equals(""+ConstantesBD.codigoNuncaValido))
			{
				ps.setString(26, null);
			}
			else
			{
				ps.setString(26, datos.get(indicesEmpresa[33]).toString());
			}
			//ubicacion_logo_reportes
			ps.setString(27, datos.get(indicesEmpresa[35])+"");
			
			//codigo Interfaz
			if(datos.get(indicesEmpresa[37]).toString().equals(""))
			{
				ps.setObject(28, null);
			}
			else
			{
				ps.setInt(28, Utilidades.convertirAEntero(datos.get(indicesEmpresa[37]).toString()));
			}
			
			// 29, imprimir_firmas_emp             29 ,5.0
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[39])+""))
			{
				ps.setString(29,datos.get(indicesEmpresa[39])+"");
			}
			else
			{
				ps.setNull(29, Types.VARCHAR);
			}
			
			// 30, representante
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[38])+""))
			{
				ps.setString(30, datos.get(indicesEmpresa[38])+"" );
			}
			else
			{
				ps.setNull(30, Types.VARCHAR);
			}
			
			/*
			 * CODIGO PK
			 */
			ps.setDouble(31, secuencia);

			/**
			 * Sección Facturas Varias
			 */
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[40])+""))
			{
		    	ps.setString(32, datos.get(indicesEmpresa[40])+"");
			}
			else
			{
				ps.setNull(32, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[41])+""))
			{
				ps.setString(33, datos.get(indicesEmpresa[41])+"");
			}
			else
			{
				ps.setNull(33, Types.VARCHAR);
			}
			
			if(datos.get(indicesEmpresa[42])+""!=null  
				&& !UtilidadTexto.isEmpty(datos.get(indicesEmpresa[42])+"")
				&& UtilidadTexto.isNumber(datos.get(indicesEmpresa[42])+""))
			{
				ps.setBigDecimal(34, new BigDecimal(datos.get(indicesEmpresa[42])+""));
			}
			else
			{
				ps.setNull(34, Types.NUMERIC);
			}
			
			if(datos.get(indicesEmpresa[43])+""!=null 
				&& !UtilidadTexto.isEmpty(datos.get(indicesEmpresa[43])+"")
				&& UtilidadTexto.isNumber(datos.get(indicesEmpresa[43])+""))
			{
				ps.setBigDecimal(35, new BigDecimal(datos.get(indicesEmpresa[43])+""));
			}
			else
			{
				ps.setNull(35, Types.NUMERIC);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[44])+""))
			{
		    	ps.setString(36, datos.get(indicesEmpresa[44])+"");
			}
			else
			{
				ps.setNull(36, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[45])+""))
			{
				ps.setString(37, datos.get(indicesEmpresa[45])+"");
			}
			else
			{
				ps.setNull(37, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(datos.get(indicesEmpresa[46])+""))
			{
				ps.setString(38, datos.get(indicesEmpresa[46])+"");
			}
			else
			{
				ps.setNull(38, Types.VARCHAR);
			}

			logger.info("	INSERTAR MULTIEMPRESA -->"+ps);
			
			
			if (ps.executeUpdate()>0)
			{
				return secuencia;
			}
				 
						
		}
		catch (Exception e) 
		{
			logger.error("\n Problema ingresando Empresas "+e);
		}
		
		
		return ConstantesBD.codigoNuncaValidoDouble;
	}
	
	

	/**
	 * Metodo que mustra si una empresa esta siendo usada 
	 * o no, para poderla eliminar.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean esEmpresaUsada(Connection con, String codigo)
	{
		logger.info("\n entro a es usado "+codigo);
		boolean enTransaccion=UtilidadBD.iniciarTransaccionSinMensaje(con);
		String cadena="DELETE FROM empresas_institucion where codigo="+codigo;
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			enTransaccion=ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			enTransaccion=false;
		}
		UtilidadBD.abortarTransaccionSinMensaje(con);
		return enTransaccion;
	}
	
	

	
	/**
	 * Inserta los datos de una institución
	 * @param encabezado @todo
	 * @param pie @todo
	 * @param con, Connection con la fuente de datos
	 * @param codigo, Código de la institución
	 * @param nit, Nit de la institución
	 * @param razon, Razon Social de la institución
	 * @param depto, Departamento al que pertenece la ciudad
	 * @param ciudad, Ciudad de la institución
	 * @param direccion, Dirección de la institución
	 * @param telefono, Telefono de la institución
	 * @param codMinSalud,  Codigo del Ministerio de Salud
	 * @param actividadEco, Actividad Economica
	 * @param resolucion, Número de Resolución 
	 * @param prefijo, Prefijo de la factura
	 * @param rangoInic, Rango inicial de la factura
	 * @param rangoFin, Rango Final de la factura
	 * @param codEmpTransEsp
	 * @param pieFacturaVaria 
	 * @param encabezadoFacturaVaria 
	 * @param rangoFinFacturaVaria 
	 * @param rangoInicFacturaVaria 
	 * @param prefijoFacturaVaria 
	 * @param resolucionFacturaVaria 
	 * @param String ubicacionLogo
	 * @param String indicativo
	 * @param String extension
	 * @param String celular
	 * @return int, 0 no efectivo, >0 efectivo.
	 * @see com.princetonsa.dao.ParamInstitucionDao#insertar(java.sql.Connection, int,String,String,int,int,String,String,int,String,String,String,String,int,int)
	 */
	public static int insertarInstitucion (Connection con, 
													        int codigo,
													        String nit,
													        String razon,
													        String depto,
													        String ciudad,
													        String direccion,
													        String telefono,
													        String codMinSalud,
													        String actividadEco,
													        String resolucion,
													        String prefijo,
													        int rangoInic,
													        int rangoFin,
															String path,
															String encabezado,
															String pie,
															String pais,
															String logo,
															String tipoins,
															String codEmpTransEsp,
															String ubicacionLogo,
															String indicativo,
															String extension,
															String celular, 
															String codigoInterfaz,
															String representanteLegal ,
															String niveLogo, 
															String resolucionFacturaVaria, 
															String prefijoFacturaVaria, 
															BigDecimal rangoInicFacturaVaria, 
															BigDecimal rangoFinFacturaVaria, 
															String encabezadoFacturaVaria, 
															String pieFacturaVaria )
	{
		int resp = 0;
	    
	    try
	    {
	    	if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
		      
	    	PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarInstiStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		      
	    	ps.setInt(1,codigo);
	    	ps.setString(2,nit);
	    	ps.setString(3,razon);
	    	ps.setString(4,depto);
	    	ps.setString(5,ciudad);
	    	ps.setString(6,direccion);
	    	ps.setString(7,telefono);
	    	ps.setString(8,codMinSalud);
	    	ps.setString(9,actividadEco);
	    	ps.setString(10,resolucion);
	    	ps.setString(11,prefijo);
	    	ps.setInt(12,rangoInic);
	    	ps.setInt(13,rangoFin);
	    	ps.setString(14,path);
	    	ps.setString(15,encabezado);
	    	ps.setString(16,pie);
	    	ps.setString(17,pais);
	    	ps.setString(18,logo);
	    	ps.setString(19,tipoins);
	    	ps.setString(20, codEmpTransEsp);
	    	ps.setString(21, ubicacionLogo);
		      
		    if(indicativo.equals(""))
		    	ps.setNull(22,Types.VARCHAR);
		    else
		    	ps.setString(22,indicativo);
		      
		    if(extension.equals(""))
		    	ps.setNull(23,Types.VARCHAR);
		    else
		    	ps.setString(23,extension);
		    		  
		    if(celular.equals(""))
		    	ps.setNull(24,Types.VARCHAR);
		    else
		    	ps.setString(24,celular);
		      
		    if(codigoInterfaz.equals(""))
		    	ps.setInt(25,Types.INTEGER);
		    else
	    	  ps.setInt(25,Utilidades.convertirAEntero(codigoInterfaz));
		      
		    if(UtilidadTexto.isEmpty(representanteLegal))
		    {
		    	ps.setNull(26, Types.VARCHAR );
		    }
		    else
		    {
		    	ps.setString(26,representanteLegal);
		    }
		      
		      
		    if(UtilidadTexto.isEmpty(niveLogo))
		    {
		    	ps.setNull(27, Types.VARCHAR);
		    }
		    else
		    {
		    	ps.setString(27, niveLogo);  
		    }
		      
		      
		    if(!UtilidadTexto.isEmpty(resolucionFacturaVaria))
			{
		    	ps.setString(28, resolucionFacturaVaria);
			}
			else
			{
				ps.setNull(28, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(prefijoFacturaVaria))
			{
				ps.setString(29, prefijoFacturaVaria);
			}
			else
			{
				ps.setNull(29, Types.VARCHAR);
			}
			
			if(rangoInicFacturaVaria!=null  && rangoInicFacturaVaria.doubleValue()>0)
			{
				ps.setBigDecimal(30, rangoInicFacturaVaria);
			}
			else
			{
				ps.setNull(30, Types.NUMERIC);
			}
			
			if(rangoFinFacturaVaria!=null && rangoFinFacturaVaria.doubleValue()>0)
			{
				ps.setBigDecimal(31, rangoFinFacturaVaria);
			}
			else
			{
				ps.setNull(31, Types.NUMERIC);
			}
			
			if(!UtilidadTexto.isEmpty(encabezadoFacturaVaria))
			{
		    	ps.setString(32, encabezadoFacturaVaria);
			}
			else
			{
				ps.setNull(32, Types.VARCHAR);
			}
			
			if(!UtilidadTexto.isEmpty(pieFacturaVaria))
			{
				ps.setString(33, pieFacturaVaria);
			}
			else
			{
				ps.setNull(33, Types.VARCHAR);
			}
			
		      
		    resp=ps.executeUpdate();
		      
		}
	    catch (SQLException e)
	    {
	        logger.warn(e+" Error en la inserción de datos: SqlBaseParamInstitucionDao "+e.toString() );
		    resp=0;
	    }
	    return resp;
	    
	}
	
	/**
	 * Metodo que realiza la consulta de uno ó varios registros de instituciones.
	 * @param con, Connection con la fuente de datos.
	 * @param codigo, Codigo de la institución.
	 * @param codigo_depto, Codigo del departamento.
	 * @param codigo_ciudad, Codigo de la ciudad
	 * @param consultaUno, Boolean indica que tipo de consulta se realiza.
	 * @return ResultSet, con el resultado.
	 * @see com.princetonsa.dao.ParamInstitucionDao#consultaInstituciones(java.sql.Connection,int,int,int,boolean)
	 */
	public static ResultSetDecorator consultaInstituciones (Connection con, int codigo, String codigo_pais, String codigo_depto,String codigo_ciudad,boolean consultaUno)
	{
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
	        
	        if(consultaUno)
	        {
	            ps=  new PreparedStatementDecorator(con.prepareStatement(consultarInstitucion + consultaUnaInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            logger.info("INST->"+consultarInstitucion + consultaUnaInstitucion+" PAIS->"+codigo_pais+" depto->"+codigo_depto+" ciudad->"+codigo_ciudad+" cod->"+codigo);
	            
	            ps.setString(1,codigo_pais);
	            ps.setString(2,codigo_depto);
	            ps.setString(3,codigo_ciudad);
	            ps.setInt(4,codigo);
	        }
	        else if(!consultaUno)
	        {
	            ps=  new PreparedStatementDecorator(con.prepareStatement(consultarInstitucion + consultaTodasInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            logger.info("INST->"+consultarInstitucion + consultaTodasInstitucion+" cod->"+codigo);
	            ps.setInt(1,codigo);
	        }
	       
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en la consulta de instituciones, con el codigo->"+codigo+" "+e.toString() );
		   return null;
	    }
	}
	
	/**
	 * modifica los datos de una institución
	 * @param encabezado @todo
	 * @param pie @todo
	 * @param con, Connection con la fuente de datos
	 * @param codigo, Código de la institución
	 * @param nit, Nit de la institución
	 * @param nombreNit, String con el nombre del tipo de identificacion
	 * @param razon, Razon Social de la institución
	 * @param depto, Departamento al que pertenece la ciudad
	 * @param ciudad, Ciudad de la institución
	 * @param direccion, Dirección de la institución
	 * @param telefono, Telefono de la institución
	 * @param codMinSalud,  Codigo del Ministerio de Salud
	 * @param actividadEco, Actividad Economica
	 * @param resolucion, Número de Resolución 
	 * @param prefijo, Prefijo de la factura
	 * @param rangoInic, Rango inicial de la factura
	 * @param rangoFin, Rango Final de la factura
	 * @param String indicativo
	 * @param String extension
	 * @param String celular
	 * @return boolean, false no efectivo, true efectivo.
	 * @throws SQLException
	 * @see com.princetonsa.dao.ParamInstitucionDao#insertar(java.sql.Connection, int,String,String,int,int,String,String,int,String,String,String,String,int,int)
	 */
	public static boolean modificar (Connection con,int codigo,
														        String nit,
														        String digv,
														        String nombreNit,
														        String razon,
														        String depto,
														        String ciudad,
														        String direccion,
														        String telefono,
														        String codMinSalud,
														        String actividadEco,
														        String resolucion,
														        String prefijo,
														        int rangoInic,
														        int rangoFin,
																String path, 
																String encabezado, 
																String pie,
																String pieHisCli,
																String pais,
																String logo,
																String tipoIns,
																String codEmpTransEsp,
																String ubicacionLogo,
																String indicativo,
																String extension,
																String celular,
																String codigoInterfaz,
																String representanteLegal ,
																String nivelLogo,
																String resolucionFacturaVaria, 
																String prefijoFacturaVaria, 
																BigDecimal rangoInicFacturaVaria, 
																BigDecimal rangoFinFacturaVaria, 
																String encabezadoFacturaVaria, 
																String pieFacturaVaria,
																String pieAmbMedicamentos)
	{
	    int resp = 0;
	    boolean esPrimero=true;
	    
	    String modificarStr="UPDATE instituciones SET ";
	    
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en modificar SqlBaseParamInstitucionDao : Conexión cerrada");
			}
			
		    if(!nit.equals(""))
			{	
			    modificarStr+=" nit = '"+nit+"'";
			    esPrimero=false;
			}	
		    if(!nombreNit.equals(""))
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" tipo_identificacion = '"+nombreNit+"'";					
			}
		    if(!digv.equals(""))
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" digito_verificacion = "+digv+"";					
			}
		    else
		    {
		    	if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" digito_verificacion = -1";
		    }
		    
		    
/*********************************************************************************
 * 					Cambio Anexo 680
 *********************************************************************************/		    
		    
		    if(!tipoIns.equals(""+ConstantesBD.codigoNuncaValido))
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" institucion_publica = '"+tipoIns+"' ";					
			}
		    else
		    {
		    	if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" institucion_publica = '"+ConstantesBD.codigoNuncaValido+"' ";
		    }
		    
/************************************************************************************
 * 					Fin Anexo 680
 ************************************************************************************/
		    
		    if(!razon.equals(""))
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" razon_social = '"+razon+"'";					
			}	
		    if(Integer.parseInt(depto) > 0)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" departamento = '"+depto+"'";					
			}	
		    if(Integer.parseInt(ciudad) > 0)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" ciudad = '"+ciudad+"'";					
			}	
		    if(!direccion.equals(""))
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" direccion = '"+direccion+"'";					
			}	
		    if(!telefono.equals(""))
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" telefono = '"+telefono+"'";					
			}	
		    //if(!codMinSalud.equals(""))
			//{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" cod_min_salud = '"+codMinSalud+"'";	
			   
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" cod_emp_trans_esp = '"+codEmpTransEsp+"'";	
			//}	
		    //if(!actividadEco.equals(""))
			//{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" actividad_eco = '"+actividadEco+"'";					
			//}	
		    //if(!resolucion.equals(""))
			//{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" resolucion = '"+resolucion+"'";					
			//}	
		    //if(!prefijo.equals(""))
			//{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" pref_factura = '"+prefijo+"'";					
			//}
		    if(rangoInic > 0)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" rgo_inic_fact = '"+rangoInic+"'";					
			}	
		    else if(rangoInic==ConstantesBD.codigoNuncaValido)
		    {
		        if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" rgo_inic_fact = null ";
		    }
		    if(rangoFin > 0)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" rgo_fin_fact = '"+rangoFin+"'";					
			}
		    else if(rangoFin==ConstantesBD.codigoNuncaValido)
		    {
		        if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" rgo_fin_fact = null ";
		    }
		    //if(!path.equals(""))
		   // {
		    	if(!esPrimero)
		    		modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" path = '"+path+"'";
		    //}
	    	if(!esPrimero)
	    		modificarStr+=" , ";
			    
		    esPrimero=false;
		    modificarStr+=" encabezado = '"+encabezado+"'";

		    if(!esPrimero)
		    	modificarStr+=" , ";
			    
		    esPrimero=false;
		    modificarStr+=" pie = '"+pie+"'";
		    
		    if(!pieHisCli.equals("")){
			    if(!esPrimero)
			    	modificarStr+=" , ";
			    esPrimero=false;
			    modificarStr+=" pie_his_cli = '"+pieHisCli+"'";
		    }
		    
		    if(Integer.parseInt(pais) > 0)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" pais = '"+pais+"'";					
			}
		    
		    if(!esPrimero)
	    		modificarStr+=" , ";
		    
		    esPrimero=false;
		    modificarStr+=" logo = '"+logo+"'";		    
		    
		    if(!esPrimero)
	    		modificarStr+=" , ";
		    
		    esPrimero=false;
		    modificarStr+=" ubicacion_logo_reportes = '"+ubicacionLogo+"'";		
		    
		    if(!esPrimero)
	    		modificarStr+=" , ";
		    
		    esPrimero=false;
		    modificarStr+=" indicativo = '"+indicativo+"' ";	
		    
		    if(!esPrimero)
	    		modificarStr+=" , ";
		    
		    esPrimero=false; 
		    modificarStr+=" extension = '"+extension+"' ";
		    
		    if(!esPrimero)
	    		modificarStr+=" , ";
		    
		    esPrimero=false;
		    modificarStr+=" celular = '"+celular+"' ";	
		    
		   if(!codigoInterfaz.equals(""))
		   {
		    if(Integer.parseInt(codigoInterfaz) > 0)
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" codigo_interfaz = "+codigoInterfaz+" ";					
			}
		   }else
			{	
			    if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" codigo_interfaz = "+ConstantesBD.codigoNuncaValido+" ";					
			}
		    
		   // CAMBIOS 
		   
		   
		   if(! UtilidadTexto.isEmpty(representanteLegal) )
		   {
			   if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" representante_legal = '"+representanteLegal+"' ";
		   }
		   
		   
		   
		   if(! UtilidadTexto.isEmpty(nivelLogo) )
		   {
			   if(!esPrimero)
			        modificarStr+=" , ";
			    
			    esPrimero=false;
			    modificarStr+=" nivel_logo = '"+nivelLogo+"' ";
		   }
		   
		 
           if(!esPrimero){
		        modificarStr+=" , ";
		   }
			    
		   esPrimero=false;
		   modificarStr+=" resolucion_factura_varia = '"+resolucionFacturaVaria+"'";					


           if(!esPrimero){
		        modificarStr+=" , ";
		   }
			
           esPrimero=false;
		   modificarStr+=" pref_factura_varia = '"+prefijoFacturaVaria+"'";					

		   if(rangoInicFacturaVaria!=null && rangoInicFacturaVaria.intValue() > 0)
		   {
			   if(!esPrimero){
			        modificarStr+=" , ";
			   }
		    
			   esPrimero=false;
			   modificarStr+=" rgo_inic_fact_varia = "+rangoInicFacturaVaria.intValue();					
		   }	
		   else{
			   
			   if(!esPrimero){
			        modificarStr+=" , ";
			   }
		    
			   esPrimero=false;
			   modificarStr+=" rgo_inic_fact_varia = null ";
		   }
		   
		   if(rangoFinFacturaVaria!=null && rangoFinFacturaVaria.intValue() > 0)
		   {	
			   if(!esPrimero){
			        modificarStr+=" , ";
			   }
		    
			   esPrimero=false;
			   modificarStr+=" rgo_fin_fact_varia = "+rangoFinFacturaVaria.intValue();					
		   
		   }else{
			   
			   if(!esPrimero){
			        modificarStr+=" , ";
			   }
		    
			   esPrimero=false;
			   modificarStr+=" rgo_fin_fact_varia = null ";
		   }

		   if(!esPrimero){
		        modificarStr+=" , ";
		   }
		    
		   esPrimero=false;
		   modificarStr+=" encabezado_factura_varia = '"+encabezadoFacturaVaria+"'";

		   if(!esPrimero){
		        modificarStr+=" , ";
		   }
		    
		   esPrimero=false;
		   modificarStr+=" pie_factura_varia = '"+pieFacturaVaria+"'"; 
		   
		   if(!pieAmbMedicamentos.equals("")){
			    if(!esPrimero)
			    	modificarStr+=" , ";
			    esPrimero=false;
			    modificarStr+=" pie_amb_med = '"+pieAmbMedicamentos+"'";
		    }
		   
		   modificarStr+=" WHERE codigo = '"+codigo+"'";
		    
		   logger.info("\n cadena --> "+modificarStr);
		   PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		   resp=ps.executeUpdate();
			
		   if(resp>0){
			   
			   return true;
			   
		   }else{
			   
			   return false;
		   }
	   }
	   catch(SQLException e){
		   logger.warn(e+"Error en la modificación de datos: SqlBaseParamInstitucionDao "+e.toString()+" "+modificarStr);
		   return false;			
	   }	
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static String obtenerPrefijoFacturas(Connection con, int codigoInstitucion)
	{
		String consulta="SELECT pref_factura as prefijo from instituciones where codigo=?";
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1, codigoInstitucion);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	        	return rs.getString("prefijo");
		}
		catch (SQLException e) 
		{
			logger.error("error consultando el prefijo de factura");
			e.printStackTrace();
		}
        return "";
        
	}
	
	/**
	 * Insertar los tipos de monedas por institucion
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarTiposMonedaInstitucion(Connection con,HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarTiposMonedaInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("tipo_moneda")+""));
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_inicial")+""));
			ps.setString(4, vo.get("usuario")+"");
			if(ps.executeUpdate()>0);
				return true;
		}
		catch (SQLException e) 
		{
			 e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Consultar los tipos de moneda por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarTiposMonedaInstitucion(Connection con,int institucion)
	{
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposMonedaInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Modificar tipos de moneda por institucion
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarTiposMonedaInstitucion(Connection con,HashMap vo)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarTiposMonedaInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("tipo_nuevo")+""));
			ps.setString(2, UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_inicial")+""));
			ps.setString(3, vo.get("usuario")+"");
			ps.setInt(4, Utilidades.convertirAEntero(vo.get("institucion")+""));
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("tipo_viejo")+""));
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Eliminar los tipos de monedas por institucion
	 * @param con
	 * @param tipoMoneda
	 * @return
	 */
	public static boolean eliminarTiposMonedaInstitucion(Connection con,int tipoMoneda)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarTiposMonedaInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tipoMoneda);
			return ps.executeUpdate()>0;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Consultar los tipos de moneda por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap obtenerEmpresasInstitucion(Connection con,int institucion)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps=null;
		try
		{
			String consultaStr= "SELECT codigo as codigo, razon_social as razon_social FROM empresas_institucion WHERE institucion=? ";
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			logger.info("Cadena busqueda EmpresasInstitucion >> "+consultaStr +" Institucion "+institucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.info("Fallo busqueda EmpresasInstitucion >> ");
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static boolean esInstitucionPublica(Connection con, int institucion)
	{
		String consulta="select institucion_publica from instituciones where codigo=?";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return true;
	}


	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public static RangosConsecutivos obtenerRangosFacturacionXInstitucion(	int institucion) 
	{
		RangosConsecutivos rango= null;
		String consulta=" SELECT " +
							"coalesce(rgo_inic_fact,"+ConstantesBD.codigoNuncaValido+") as rangoinicial, " +
							"coalesce(rgo_fin_fact,"+ConstantesBD.codigoNuncaValido+") as rangofinal " +
						"from " +
							"administracion.instituciones " +
						"where " +
							"codigo=?";
		
		try
		{			
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				rango= new RangosConsecutivos(rs.getBigDecimal(1), rs.getBigDecimal(2));
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		return rango;
	}
	

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoEmpresasInstitucion> listaInstitucionEmpresa(DtoEmpresasInstitucion dto)
	{
		ArrayList<DtoEmpresasInstitucion> newLista = new ArrayList<DtoEmpresasInstitucion>();

		String consulta=" select "+
		" ei.codigo as codigo, "+                  
		" ei.institucion as institucion, "+       
		" ei.razon_social  as razonSocial , "+         
		" ei.pais as pais ,"+                   
		" ei.departamento as departamento ,"+            
		" ei.direccion as direccion ,"+              
		" ei.telefono as telefono ,"+               
		" ei.cod_min_salud as codMinSalud,  "+          
		" ei.actividad_eco  as    actividadEco, "+      
		" ei.resolucion as resolucion,  "+             
		" ei.pref_factura as prefFactura , "+           
		" ei.rgo_inic_fact as rgoInicFact, "+           
		" ei.rgo_fin_fact as rgoFinFact , "+          
		" ei.encabezado  as encabezado , "+            
		" ei.pie as pie, "+                     
		" ei.logo as logo , "+                  
		"  ei.vigente as vigente, "+                 
		"  ei.usuario_modifica  as usuarioModifica, "+      
		"  ei.fecha_modifica  as fechaModifica ,"+        
		"  ei.hora_modifica as horaModifica, "+
		"  ei.nit as nit  ,  "+                
		"  ei.ciudad as ciudad ,"+                 
		"  ei.tipo_identificacion as tipoIdentificacion , "+    
		"  ei.valor_consecutivo_fact as valorConsecutivoFact  ,"+
		"  ei.anio_vigencia as anioVigencia ,"+          
		"  ei.digito_verificacion as digitoVerificacion, "+      
		"  ei.institucion_publica as institucionPublica , "+    
		"  ei.cod_emp_trans_esp as codEmpTransEsp ,   "+    
		"  ei.ubicacion_logo_reportes as ubicacionLogoReportes, "+
		"  ei.pie_his_cli as pieHisCli, "+           
		"  ei.codigo_interfaz as codigoInterfaz,    " +
		"  ei.resolucion_factura_varia AS resolucion_factura_varia, " +
        "  ei.pref_factura_varia AS pref_factura_varia, " +
        "  ei.rgo_inic_fact_varia AS rgo_inic_fact_varia, " +
        "  ei.rgo_fin_fact_varia AS rgo_fin_fact_varia, "+
        "  ei.encabezado_factura_varia AS encabezado_factura_varia, "+
        "  ei.pie_factura_varia AS pie_factura_varia " +
		"  from " +
		"  facturacion.empresas_institucion  ei " +
		"  where 1=1"; 
		
		consulta+=dto.getInstitucion()>0?" AND ei.institucion="+dto.getInstitucion(): " "; //validacion de cadenas??
		consulta+=dto.getCodigo().doubleValue()>0? "and  ei.codigo="+dto.getCodigo():"";
		consulta+=" order by ei.razon_social";
		
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			logger.info("Consulta \n\n\n\n\n"+ps);
			
			while(rs.next())
			{
				DtoEmpresasInstitucion newDto= new DtoEmpresasInstitucion();
				newDto.setCodigo(rs.getBigDecimal("codigo"));
				newDto.setInstitucion(rs.getInt("institucion"));
				newDto.setRazonSocial(rs.getString("razonSocial"));
				newDto.setPais(rs.getString("pais"));
				newDto.setDepartamento(rs.getString("departamento"));
				newDto.setDireccion(rs.getString("direccion"));
				newDto.setTelefono(rs.getString("telefono"));
				newDto.setCodMinSalud(rs.getString("codMinSalud"));
				newDto.setActividadEco(rs.getString("actividadEco"));
				newDto.setResolucion(rs.getString("resolucion"));
				newDto.setPrefFactura(rs.getString("prefFactura"));
				newDto.setRgoInicFact(rs.getBigDecimal("rgoInicFact"));
				newDto.setRgoFinFact(rs.getBigDecimal("rgoFinFact"));
				newDto.setEncabezado(rs.getString("encabezado"));
				newDto.setPie(rs.getString("pie"));
				newDto.setLogo(rs.getString("logo"));
				newDto.setVigente(rs.getString("vigente"));
				newDto.setUsuarioModifica(rs.getString("usuarioModifica"));
				newDto.setHora_modifica(rs.getString("horaModifica"));
				newDto.setNit(rs.getString("nit"));
				newDto.setCiudad(rs.getString("ciudad"));
				newDto.setTipoIdentificacion(rs.getString("tipoIdentificacion"));
				newDto.setValorConsecutivoFact(rs.getDouble("valorConsecutivoFact"));
				newDto.setAnioVigencia(rs.getString("anioVigencia"));
				newDto.setDigitoVerificacion(rs.getInt("digitoVerificacion"));
				newDto.setInstitucionPublica(rs.getString("institucionPublica"));
				newDto.setCodEmpTransEsp(rs.getString("codEmpTransEsp"));  
				newDto.setUbicacionLogoReportes(rs.getString("ubicacionLogoReportes"));
				newDto.setPieHisCli(rs.getString("pieHisCli"));	
				newDto.setCodigoInterfaz(rs.getInt("codigoInterfaz"));
				
				newDto.setResolucionFacturaVaria(rs.getString("resolucion_factura_varia"));
				newDto.setPrefFacturaVaria(rs.getString("pref_factura_varia"));
				newDto.setRgoInicFactVaria(rs.getBigDecimal("rgo_inic_fact_varia"));
				newDto.setRgoFinFactVaria(rs.getBigDecimal("rgo_fin_fact_varia"));
				newDto.setEncabezadoFacturaVaria(rs.getString("encabezado_factura_varia"));
				newDto.setPieFacturaVaria(rs.getString("pie_factura_varia"));
				
				newLista.add(newDto);	
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
						
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		return newLista;
	}
	
	/**
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static Boolean obtenerNivelLogo(Connection con , Integer codigoInstitucion) throws SQLException{
		Boolean esbasica= true;
		String nivelLogo="";

		String consulta = " select nivel_logo from instituciones where codigo = ? ";

		PreparedStatement ps = con.prepareStatement(consulta);
		ps.setInt(1, codigoInstitucion);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			if(rs.getString("nivel_logo")!=null){
				nivelLogo=rs.getString("nivel_logo");

				if(nivelLogo.equals("N")){
					esbasica=false;
				}
			}
		}
		return esbasica;
	}
}
