/*
 * Mayo 25, 2007
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoInfoAmparosReclamacion;
import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.manejoPaciente.DtoResponsablePaciente;
import com.princetonsa.mundo.CuentasPaciente;
import com.princetonsa.mundo.Paciente;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurips;
import com.servinte.axioma.dto.manejoPaciente.DtoCertAtenMedicaFurpro;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;


/**
 * 
 * @author sgomezobtenerDatosPaciente
 * Objeto usado para el acceso común a la fuente de datos
 * de utilidades propias del módulo de MANEJO PACIENTE
 */
public class SqlBaseUtilidadesManejoPacienteDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesManejoPacienteDao.class);
	
	/**
	 * cadena para la insercion de reservar cama
	 */
	private static final String cadenaInsertarStr="INSERT INTO reservar_cama (codigo, centro_atencion, codigo_paciente, codigo_cama, fecha_ocupacion, institucion, usuario_modifica,fecha_modifica, hora_modifica, estado, observaciones) VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"',?)";
	
	/**
	 * cadena para la insercion del log reservar cama
	 */
	private static final String cadenaLogStr="INSERT INTO log_reservar_cama (codigo, codigo_paciente, nombres_paciente, tipo_identificacion, numero_identificacion, cod_centro_atencion_sel, centro_atencion_sel, codigo_cama, cama, fecha_ocupacion, fecha, hora, usuario, cod_centro_atencion_res,centro_atencion_res,codigo_reserva) VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * cadena para la modificacion de reservar cama
	 */
	private static final String cadenaModificarStr= "UPDATE reservar_cama SET centro_atencion=?, codigo_cama=?, fecha_ocupacion=?, estado=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?, observaciones = ? WHERE codigo=?";
	
	/**
	 * consulta la info de reserva cama, EN CASO DE AGREGAR UN NUEVO CAMPO ENTONCES agregarlo al <INDICES_MAPA>
	 */
	private static final String consultarReservarCamaStr="SELECT " +
																	"rc.codigo as codigo," +
																	"rc.centro_atencion as codigocentroatencion, " +
																	"getnomcentroatencion(rc.centro_atencion) As nombrecentroatencion, " +
																	"rc.codigo_paciente as codigopaciente, " +
																	"rc.codigo_cama as codigocama, " +
																	"getDescripcionCama2(rc.codigo_cama) as nombrecama, " +
																	"CASE WHEN rc.fecha_ocupacion IS NULL THEN '' ELSE to_char(rc.fecha_ocupacion,'"+ConstantesBD.formatoFechaAp+"') END as fechaocupacion, " +
																	"rc.institucion as institucion," +
																	"rc.observaciones " +
																	"FROM " +
																		" reservar_cama rc " +
																	"WHERE " +
																		" rc.codigo_paciente=? AND rc.institucion = ?  and estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
	
	/**
	 * String de Consulta de los estados de las camas.
	 */
	private static final String strCadenaConsultaEstadoCamas = "SELECT " +
			"ec.codigo AS codigo," +
			"ec.nombre AS nombre " +
			"FROM estados_cama ec";
	
	
	
	
	/**
	 * String de consulta de los pisos.
	 */
	private static final String strCadenaConsultaPisos = "SELECT " +
			"p.codigo_piso AS codigopiso," +
			"p.nombre AS nombre," +
			"p.centro_atencion AS idcentroatencion," +
			"getnomcentroatencion(p.centro_atencion) AS nombrecentroatencion," +
			"p.codigo AS codigo " +
			"FROM pisos p	 WHERE p.institucion=? ";	
	
	
	/**
	 * String de consulta de datos de la cama
	 */
	private static final String strCadenaConsultacamas = "SELECT "+
			"getnomhabitacioncama(c.codigo) AS habitacion," +
			"getnombretipohabitacion(c.habitacion) AS tipo_habitacion," +
			"c.numero_cama As numero_cama,"+
			"getnomcentrocosto(c.centro_costo) AS nombre_centro_costo," +
			"c.codigo AS codigo_cama,"+
			"getnombrepiso(h.piso) AS piso," +
			"tuc.nombre AS tipo_usuario," +
			"h.centro_atencion AS centro_atencion," +
			"c.estado As estado_cama," +
			"getnombreestadocama(c.estado) As nombre_estado_cama "+
			"FROM camas1 c INNER JOIN tipos_usuario_cama tuc ON (tuc.codigo=c.tipo_usuario_cama) " +
			" INNER JOIN  habitaciones h ON (h.codigo=c.habitacion) " +
			"WHERE c.codigo=? AND c.institucion=?";
	
	
	
	
	/**
	 * String encargado de consultar los montos de cobro
	 * permitiendo filtrar por diferentes campos.
	 */
	private static final String strCadenaConsultaMontoCobro ="SELECT " +
																 " mc.codigo AS codigo," +
																 " mc.convenio AS convenio," +
																 " mc.via_ingreso AS viaIngreso," +
																 " mc.tipo_afiliado AS tipoAfiliado," +
																 " getnombretipoafiliado(mc.tipo_afiliado) AS nombreTipoAfiliado," +
																 " mc.estrato_social AS estratoSocial," +
																 " getnombretipomonto(mc.tipo_monto) AS tipoMonto," +
																 " CASE WHEN mc.valor IS NULL THEN mc.porcentaje ||' %' ELSE mc.valor ||''  END  AS valor," +
																 " mc.porcentaje AS porcentaje," +
																 " mc.activo AS activo " +
															" FROM montos_cobro mc " +
															" WHERE 1=1 ";
	
	
	
	/**
	 * String encargado de consultar los tipos de paciente,
	 * tiene una funcion encargada de devolver el nombre
	 * del tipo del paciente.
	 */
	private static final String strCadenaConsultaTiposPaciente = "SELECT " +
																	 " tpvi.tipo_paciente AS acronimo," +
																	 " getnombretipopaciente(tpvi.tipo_paciente) AS nombre " +
																 " FROM tip_pac_via_ingreso tpvi " +
																 " WHERE tpvi.via_ingreso=?";
	
	
	
	
	
	
	private static final String strCadenaConsultaAeraIngresoXViaIngreso = "SELECT " +
																			  " ccvi.centro_costo AS codigo," +
																			  " getnomcentrocosto(ccvi.centro_costo) AS nombre," +
																			  " ccvi.centro_costo AS centroCosto," +
																			  " ccvi.via_ingreso AS viaIngreso, " +
																			  " ccvi.tipo_paciente AS tipoPaciente " +
																		  " FROM centro_costo_via_ingreso ccvi " +
																		  " INNER JOIN centros_costo cc ON (cc.codigo=ccvi.centro_costo) " +
																		  " WHERE ccvi.institucion=? AND cc.centro_atencion=? ";
	
	private static  final String strCadenaConsultaFechaAperturaCuenta = " SELECT " +
																				" to_char(c.fecha_apertura,'dd/mm/yyyy') As fechaApertura" +
																			" FROM cuentas c" +
																				" WHERE c.id=?";  
	
	
	
	private static final String strCadenaConsultaFechaAperturaIgreso=" SELECT " +
																			  " to_char(i.fecha_ingreso,'dd/mm/yyyy') As fechaApertura " +
																			  " FROM ingresos i " +
																			  " WHERE i.id=?";
	
	/**
	 * Consulta la informacion de las cuentas de un paciente
	 * */
	private static final String getCuentasPacieteStr = "" +
			"SELECT " +
			"c.via_ingreso AS viaIngreso," +
			"vi.nombre AS descripcionViaIngreso," +
			"c.tipo_paciente AS tipoPaciente," +
			"tp.nombre AS descripcionTipoPaciente," +
			"c.id AS cuenta," +
			"c.estado_cuenta AS estadoCuenta," +
			"getnombreestadocuenta(c.estado_cuenta) AS descripcionEstadoCuenta," +
			"c.id_ingreso AS idIngreso " +			
			"FROM cuentas c " +
			"LEFT OUTER JOIN vias_ingreso vi ON (vi.codigo = c.via_ingreso) " +
			"LEFT OUTER JOIN tipos_paciente tp ON (tp.acronimo = c.tipo_paciente) ";	
	
	
	
	private static final String strCuentasXIngreso=
			" SELECT c.id||'' As cuentas " +
			"  FROM " +
			" cuentas c " +
			" WHERE c.id_ingreso=? ";
	

	
	
	/**
	 * encargada de consultar si un registro de sol_cirugia_por_servicio se encuentra en informacion_parto
	 */
	private static final String strexisteinfoparto =" SELECT consecutivo As consec FROM informacion_parto WHERE cirugia=?";
	
	
	private static final String strdatosIngresos=" SELECT i.centro_atencion As centro_atencion, " +
														" i.codigo_paciente AS codigo_paciente, " +
														" i.consecutivo As consecutivo," +
														" i.anio_consecutivo As anio_consecutivo, " +
														" i.estado As estado " +
												"FROM ingresos i where i.id =? And i.institucion=?";
	
	/**
	 * Consulta los convenios de un paciente a partir de su ingreso
	 * */
	private static final String strConveniosXIng = "SELECT " +
			"DISTINCT " +
			"s.convenio," +
			"getnombreconvenio(s.convenio) as nombre," +
			"s.sub_cuenta, " +
			"s.nro_prioridad " +
			"FROM sub_cuentas s WHERE ingreso = ? ";
	
	
	/**
	 * consulta la persona/s que solicita una order 
	 * */
	private static final String strProfesionalesSolicita = "SELECT DISTINCT " +
			"s.codigo_medico," +
			"getnombrepersona(s.codigo_medico) AS nombre " +
			"FROM solicitudes s " +
			"WHERE s.numero_solicitud IN (?) AND s.codigo_medico IS NOT NULL ";
	
	/**
	 * Método para obtener el codigo del medico tratante
	 * @param con
	 * @param ingreso
	 */
	public static int obtenerCodigoMedicoTratante(Connection con, int ingreso) {
		int codigoMedicoTratante=ConstantesBD.codigoNuncaValido;
		String consulta = "SELECT " +
								"getcodigomedicotratante(c.id) AS codigomedico " +
							"FROM " +
								"ingresos i " +
							"INNER JOIN " +
								"cuentas c ON (c.id_ingreso=i.id) " +
							"WHERE " +
								"i.id="+ingreso;
		try
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				codigoMedicoTratante = rs.getInt("codigomedico");
		} catch (SQLException e) {
			logger.error("Error / obtenerCodigoMedicoTratante / "+e);
		}
		
		return codigoMedicoTratante;
	}	
	/**
	 * Metodo encargado de consultar algunos datos del ingreso.
	 * @param connection
	 * @param ingreso
	 * @return HashMap
	 * 
	 */
	public static HashMap obtenerDatosIngreso (Connection connection, String ingreso,String institucion)
	{
		HashMap result = new HashMap ();
		result.put("numRegistros", 0);
		String cadena=strdatosIngresos;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//ingreso
			ps.setInt(1, Utilidades.convertirAEntero(ingreso));
			//institucion
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		}
		catch (Exception e) 
		{
			logger.warn("\n problema consultando los datos del ingreso "+ingreso+"  "+e);
		}
		return result;
	}
	
	
	/**
	 * Metodo encargado de consultar si un registro de sol_cirugia_por_servicio se encuentra
	 * en la tabla de informacion_parto
	 * @param connection
	 * @param codigoSolCxServ
	 * @return consecutivo informacion parto (-1 si no encontro nada)
	 */
	public static int obtenerConsecutivoPartoXcodigoSolCxServ (Connection connection, String codigoSolCxServ )
	{
		logger.info("\n  entre a obtenerConsecutivoPartoXcodigoSolCxServ -->"+codigoSolCxServ);
		int consecutivo=ConstantesBD.codigoNuncaValido;
		String cadena = strexisteinfoparto;
		
		logger.info("cadena --> "+cadena);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps =  connection.prepareStatement(cadena);
			ps.setInt(1, Utilidades.convertirAEntero(codigoSolCxServ));
			rs=ps.executeQuery();
			if (rs.next()){
				consecutivo=rs.getInt("consec");
			}
		}
		catch (SQLException e) 
		{
			logger.info("\n problem en obtenerConsecutivoPartoXcodigoSolCxServ "+e);
		}finally{
			if(ps != null){ 
				try {
					ps.close();
				} catch (SQLException e2) {
					logger.error("Error al cerrar el recurso ", e2);
				}
			}

			if(rs!=null){
                       try{
                              rs.close();
                       }catch(SQLException sqlException){
                              logger.error("Error al cerrar el recurso cambio metodo ", sqlException);
                       }
            }
		}
		
		return consecutivo;
	}
	
	/**
	 * Metodo encargado de consultar y devolver las cuentas de un ongreso
	 * separadas por comas. 
	 * @param connection
	 * @param ingreso
	 * @return
	 */
	public static String obtenerCuentasXIngreso (Connection connection, String ingreso)
	{
		logger.info("\n entre a obtenerCuentasXIngreso -->"+strCuentasXIngreso+"--> "+ingreso);
		String cadena=strCuentasXIngreso;
		String resultado="";
		PreparedStatementDecorator ps  = null ;
		ResultSetDecorator rs = null;
		try 
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			/**
			 * MT 8349
			 * @author javrammo
			 * El metodo rs.isLast() fallaba, al parcer problemas del driver
			 */
			List<String> cuentas = new ArrayList<String>();
			//ingreso
			ps.setObject(1, ingreso);
			rs=new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{	
				cuentas.add(rs.getString(1));
			}	
			
			resultado = UtilidadTexto.convertirArrayStringACodigosSeparadosXComasSinComillas((ArrayList<String>) cuentas);
			
			logger.info("--->"+resultado);
		}
		catch (SQLException e)
		{
			logger.info("\n problema consultando las cuentas de un ingreso "+e);
		}
		finally{
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return resultado;
	}
	

	/**
	 * Metodo encargado de consultar la fecha de apertura del ingreso
	 * @param connection
	 * @param ingreso
	 * @return fecha apertura
	 * @modificado javrammo
	 */
	public static String obtenerFechaAperturaIngreso (Connection connection, String ingreso)
	{
		logger.info("\n entre a obtenerFechaAperturaIngreso --> "+ingreso);
		String cadena=strCadenaConsultaFechaAperturaIgreso;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps =  connection.prepareStatement(cadena);
			//ingreso
			ps.setObject(1, ingreso);
			rs=ps.executeQuery();
			if (rs.next()){
				return rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			logger.info("\n problema consultando la fecha de apertura del ingreso "+e);
		}
		finally{
			if(ps != null){ 
				try {
					ps.close();
				} catch (SQLException e2) {
					logger.error("Error al cerrar el recurso ", e2);
				}
			}

			if(rs!=null){
                       try{
                              rs.close();
                       }catch(SQLException sqlException){
                              logger.error("Error al cerrar el recurso cambio metodo ", sqlException);
                       }
            }
		}
		
		return "";
	}
	
	
	/**
	 * Metodo encargado de consultar la fecha de apertura de la cuenta.
	 * @param connection
	 * @param cuenta
	 * @return fecha apertura
	 */
	public static String obtenerFechaAperturaCuenta (Connection connection, String cuenta)
	{
		logger.info("\n entre a obtenerFechaAperturaCuenta --> "+cuenta);
		String cadena=strCadenaConsultaFechaAperturaCuenta;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//cuenta
			ps.setInt(1, Utilidades.convertirAEntero(cuenta));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				return rs.getString(1);
			
		}
		catch (SQLException e)
		{
			logger.info("\n problema consultando la fecha de apertura de la cuenta "+e);
		}
		return "";
	}
	
	
	
	
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * 16/01/2008
	 * Metodo encargado de consultar las areas de ingreso
	 * por via de ingreso.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------------
	 * 			KEY'S DEL HASHMAP CRITERIOS
	 * -----------------------------------------------
	 *  -- institucion --> Requerido
	 *  -- viaIngreso --> Opcional
	 *  -- centroCosto --> Opcional
	 *  -- centroAtencion --> Requerido
	 * @return rrayList<HashMap<String, Object>> areaIngreso
	 * ----------------------------------------------------
	 * KEY'S DEL HASHMAP DENTRO DEL ARRAY AREA INGRESO
	 * ----------------------------------------------------
	 * -- codigo
	 * -- nombre
	 * -- centroCosto
	 * -- viaIngreso
	 */
	public static ArrayList<HashMap<String, Object>>ObtenerAreaIngreso (Connection connection,HashMap criterios)
	{
		logger.info("\n entro a obtener area ingreso "+criterios);
		ArrayList<HashMap<String, Object>> areaIngreso = new ArrayList<HashMap<String,Object>>();
		String cadena = strCadenaConsultaAeraIngresoXViaIngreso,where="";
		//via de ingreso
		if (UtilidadCadena.noEsVacio(criterios.get("viaIngreso")+"") && !(criterios.get("viaIngreso")+"").equals(ConstantesBD.codigoNuncaValido+"") )
			where+=" AND ccvi.via_ingreso="+criterios.get("viaIngreso"); 
		//centro costo
		if (UtilidadCadena.noEsVacio(criterios.get("centroCosto")+"") && !(criterios.get("centroCosto")+"").equals(ConstantesBD.codigoNuncaValido+"") )
			where+=" AND ccvi.centro_costo="+criterios.get("centroCosto");
		//tipo paciente
		if (UtilidadCadena.noEsVacio(criterios.get("tipoPaciente")+"") && !(criterios.get("tipoPaciente")+"").equals(ConstantesBD.codigoNuncaValido+"") )
			where+=" AND ccvi.tipo_paciente='"+criterios.get("tipoPaciente")+"'"; 
		
		
		cadena+=where;
		logger.info("\n consulta de ObtenerAreaIngreso ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			//centro atencion
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("centroAtencion")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap mapa = new HashMap ();
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("nombre", rs.getObject("nombre"));
				mapa.put("centroCosto", rs.getObject("centroCosto"));
				mapa.put("viaIngreso", rs.getObject("viaIngreso"));
				mapa.put("tipoPaciente", rs.getObject("tipoPaciente"));
			
				areaIngreso.add(mapa);
			}
			
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando las Areas de ingreso por via de ingreso "+e);
		}
		
		return areaIngreso;
	}
	
	
	/**
	 * Metodo encargado de consultar todos los tipos de 
	 * paciente
	 * @param connection
	 * @return ArrayList<HashMap<String, Object>> TiposPaciente
	 * ----------------------------------------------------
	 * KEY'S DEL HASHMAP DEL ARRAYLIST DE TIPOS PACIENTE
	 * ----------------------------------------------------
	 * -- acronimo
	 * -- nombre
	 */
	public static ArrayList<HashMap<String, Object>>ObtenerTiposPaciente (Connection connection,int viaIngreso)
	{
		ArrayList<HashMap<String, Object>> TiposPaciente = new ArrayList<HashMap<String,Object>>();
		String cadena = strCadenaConsultaTiposPaciente;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1, viaIngreso);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap mapa = new HashMap ();
				mapa.put("acronimo", rs.getObject("acronimo"));
				mapa.put("nombre", rs.getObject("nombre"));
			
				TiposPaciente.add(mapa);
			}
			
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando los tipos de paciente "+e);
		}
		
		return TiposPaciente;
	}
	
	
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * 15/01/2008
	 * Metodo encargado de consultar los tipos de 
	 * monto de cobro pudiendo filtar por difentes
	 * campos
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 *  -- activo --> Opcional 
	 *  -- convenio --> Opcional 
	 *  -- viaIngreso --> Opcional 
	 *  -- tipoAfiliado --> Opcional 
	 * @return ArrayList<HashMap<String,Objet>>
	 * --------------------------------------------------------
	 * 	KEY'S DEL MAPA DENTRO DEL ARRAY QUE DEVUELVE EL METODO
	 * --------------------------------------------------------
	 * -- codigo
	 * -- convenio
	 * -- viaIngreso
	 * -- tipoAfiliado
	 * -- estratoSocial
	 * -- tipoMonto
	 * -- valor
	 * -- porcentaje
	 * -- activo
	 * -- nombreTipoAfiliado
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMontosCobro (Connection connection,HashMap criterios)
	{
		ArrayList<HashMap<String, Object>> montoCobro = new ArrayList<HashMap<String,Object>>();
		String cadena = strCadenaConsultaMontoCobro, where="", order="";
		boolean filtrarMontosVigentes = false;
		HashMap<String, Object> mapaAux = new HashMap<String, Object>();
		
		
		/**
		 * Se omite por el desarrollo del anexo 774
		 * Ahora se validan los montos activos por la fecha de vigencia
		//activo
		if (criterios.containsKey("activo") && !(criterios.get("activo")+"").equals("") && !(criterios.get("activo")+"").equals("-1") )
			where += " AND mc.activo="+criterios.get("activo"); 
		*/
	
		//convenio
		if (criterios.containsKey("convenio") && !(criterios.get("convenio")+"").equals("") && !(criterios.get("convenio")+"").equals("-1") )
			where += " AND mc.convenio="+criterios.get("convenio"); 
		//via ingreso
		if (criterios.containsKey("viaIngreso") && !(criterios.get("viaIngreso")+"").equals("") && !(criterios.get("viaIngreso")+"").equals("-1") )
			where += " AND mc.via_ingreso="+criterios.get("viaIngreso"); 
		//tipo afiliado
		if (criterios.containsKey("tipoAfiliado") && !(criterios.get("tipoAfiliado")+"").equals("") )
			where += " AND mc.tipo_afiliado='"+criterios.get("tipoAfiliado")+"' ";
		
		//fecha referencia (Filtrar Montos vigentes)
		if (criterios.containsKey("fechaReferencia") && !(criterios.get("fechaReferencia")+"").equals("") ){
			where += " AND mc.vigencia_inicial <= to_date('"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaReferencia").toString())+"','yyyy-mm-dd')"; 
			order += " ORDER BY mc.vigencia_inicial DESC ";
			filtrarMontosVigentes = true;
		}
		
		cadena+= where+order;
		logger.info("\n consulta montos de cobro ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				if(filtrarMontosVigentes && !mapaAux.containsKey(rs.getString("viaIngreso")+"_"+rs.getString("convenio"))){
					HashMap<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("codigo", rs.getObject("codigo"));
					mapa.put("convenio", rs.getObject("convenio"));
					mapa.put("viaIngreso", rs.getObject("viaIngreso"));
					mapa.put("tipoAfiliado", rs.getObject("tipoAfiliado"));
					mapa.put("estratoSocial", rs.getObject("estratoSocial"));
					mapa.put("tipoMonto", rs.getObject("tipoMonto"));
					mapa.put("valor", rs.getObject("valor"));
					mapa.put("porcentaje", rs.getObject("porcentaje"));
					mapa.put("nombreTipoAfiliado", rs.getObject("nombreTipoAfiliado"));
					Utilidades.imprimirMapa(mapa);
					montoCobro.add(mapa);
				}
				else if(!filtrarMontosVigentes){
					HashMap<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("codigo", rs.getObject("codigo"));
					mapa.put("convenio", rs.getObject("convenio"));
					mapa.put("viaIngreso", rs.getObject("viaIngreso"));
					mapa.put("tipoAfiliado", rs.getObject("tipoAfiliado"));
					mapa.put("estratoSocial", rs.getObject("estratoSocial"));
					mapa.put("tipoMonto", rs.getObject("tipoMonto"));
					mapa.put("valor", rs.getObject("valor"));
					mapa.put("porcentaje", rs.getObject("porcentaje"));
					mapa.put("nombreTipoAfiliado", rs.getObject("nombreTipoAfiliado"));
					Utilidades.imprimirMapa(mapa);
					montoCobro.add(mapa);
				}
			}	
			mapaAux.put(rs.getString("viaIngreso")+"_"+rs.getString("convenio"), "");
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando los montos de cobro "+e);
		}
		
		return montoCobro;
	}
	
	
	/**
	 * Adicionado por Julio Hernandez
	 * 24/09/2009
	 * Metodo encargado de consultar los tipos de 
	 * monto de cobro pudiendo filtar por difentes
	 * campos
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 *  -- activo --> Opcional 
	 *  -- convenio --> Opcional 
	 *  -- viaIngreso --> Opcional 
	 *  -- tipoAfiliado --> Opcional 
	 * @return ArrayList<HashMap<String,Objet>>
	 * --------------------------------------------------------
	 * 	KEY'S DEL MAPA DENTRO DEL ARRAY QUE DEVUELVE EL METODO
	 * --------------------------------------------------------
	 * -- codigo
	 * -- convenio
	 * -- viaIngreso
	 * -- tipoAfiliado
	 * -- estratoSocial
	 * -- tipoMonto
	 * -- valor
	 * -- porcentaje
	 * -- activo
	 * -- nombreTipoAfiliado
	 */
	public static ArrayList<HashMap<String, Object>> obtenerMontosCobroVigentes (Connection connection,HashMap criterios)
	{
		ArrayList<HashMap<String, Object>> montoCobro = new ArrayList<HashMap<String,Object>>();
		String cadena = strCadenaConsultaMontoCobro, where="";
		boolean filtrarMontosVigentes = false;
		HashMap<String, Object> mapaAux = new HashMap<String, Object>();
		
	
		//convenio
		if (criterios.containsKey("convenio") && !(criterios.get("convenio")+"").equals("") && !(criterios.get("convenio")+"").equals("-1") )
			where += " AND mc.convenio="+criterios.get("convenio"); 
		//via ingreso
		if (criterios.containsKey("viaIngreso") && !(criterios.get("viaIngreso")+"").equals("") && !(criterios.get("viaIngreso")+"").equals("-1") )
			where += " AND mc.via_ingreso="+criterios.get("viaIngreso"); 
		//tipo afiliado
		if (criterios.containsKey("tipoAfiliado") && !(criterios.get("tipoAfiliado")+"").equals("") )
			where += " AND mc.tipo_afiliado='"+criterios.get("tipoAfiliado")+"' ";
		
		//filtro de busqueda para montos vigentes referencia (Filtrar Montos vigentes) TAREA 100533
			where += " AND to_char(mc.vigencia_inicial,'yyyy-mm-dd') = (SELECT to_char(MAX(mc.vigencia_inicial),'yyyy-mm-dd') FROM montos_cobro mc WHERE 1=1 ";
			
			//convenio
			if (criterios.containsKey("convenio") && !(criterios.get("convenio")+"").equals("") && !(criterios.get("convenio")+"").equals("-1") )
				where += " AND mc.convenio="+criterios.get("convenio"); 
			//via ingreso
			if (criterios.containsKey("viaIngreso") && !(criterios.get("viaIngreso")+"").equals("") && !(criterios.get("viaIngreso")+"").equals("-1") )
				where += " AND mc.via_ingreso="+criterios.get("viaIngreso"); 
			//tipo afiliado
			if (criterios.containsKey("tipoAfiliado") && !(criterios.get("tipoAfiliado")+"").equals("") )
				where += " AND mc.tipo_afiliado='"+criterios.get("tipoAfiliado")+"' ";
			
			where+=") "; 
			
		//Fin del filtro
			
		
		cadena+= where;
		logger.info("\n consulta montos de cobro ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				if(filtrarMontosVigentes && !mapaAux.containsKey(rs.getString("viaIngreso")+"_"+rs.getString("convenio"))){
					HashMap<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("codigo", rs.getObject("codigo"));
					mapa.put("convenio", rs.getObject("convenio"));
					mapa.put("viaIngreso", rs.getObject("viaIngreso"));
					mapa.put("tipoAfiliado", rs.getObject("tipoAfiliado"));
					mapa.put("estratoSocial", rs.getObject("estratoSocial"));
					mapa.put("tipoMonto", rs.getObject("tipoMonto"));
					mapa.put("valor", rs.getObject("valor"));
					mapa.put("porcentaje", rs.getObject("porcentaje"));
					mapa.put("nombreTipoAfiliado", rs.getObject("nombreTipoAfiliado"));
					Utilidades.imprimirMapa(mapa);
					montoCobro.add(mapa);
				}
				else if(!filtrarMontosVigentes){
					HashMap<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("codigo", rs.getObject("codigo"));
					mapa.put("convenio", rs.getObject("convenio"));
					mapa.put("viaIngreso", rs.getObject("viaIngreso"));
					mapa.put("tipoAfiliado", rs.getObject("tipoAfiliado"));
					mapa.put("estratoSocial", rs.getObject("estratoSocial"));
					mapa.put("tipoMonto", rs.getObject("tipoMonto"));
					mapa.put("valor", rs.getObject("valor"));
					mapa.put("porcentaje", rs.getObject("porcentaje"));
					mapa.put("nombreTipoAfiliado", rs.getObject("nombreTipoAfiliado"));
					Utilidades.imprimirMapa(mapa);
					montoCobro.add(mapa);
				}
			}	
			mapaAux.put(rs.getString("viaIngreso")+"_"+rs.getString("convenio"), "");
		}
		catch (Exception e) 
		{
			logger.info("\n problema consultando los montos de cobro "+e);
		}
		
		return montoCobro;
	}
	
	
	/**
	 * Metodo adicionado por Jhony Alexander Duque A.
	 * Metodo encargado de consultar Datos de la cama.
	 * Este Metodo recibe un hashmap con dos parametros
	 * -------------------------------------
	 *      KEY'S DEL HASHMAP PARAMETROS
	 * -------------------------------------
	 * --codigoCama --> Requerido
	 * -->institucion --> Requerido
	 * Devuelve un Mapa con los siguientes Key's
	 *  habitacion,tipoHabitacion,numeroCama,
	 *  nombreCentroCosto,codigoCama,piso,
	 *  tipoUsuario, centroAtencion,
	 *  estadoCama,nombreEstadoCama
	 * @param connection
	 * @param parametros
	 * @return mapa
	 */
	public static HashMap obtenerDatosCama (Connection connection, HashMap parametros)
	{
		HashMap mapa = new HashMap ();
		logger.info("\n\n ** el valor de los parametros en obtenerDatosCama "+parametros);
		String cadena =strCadenaConsultacamas;
		logger.info("\n cadena -->"+cadena);
		try {
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigoCama")+""));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			
		} catch (SQLException e) {
			logger.error("\n \n *** error al consultar los datos de la cama "+e);
		}
		
		
		
		return mapa;
	}
	
	/**
	 * Metodo que retorna los pisos filtrandolos
	 * por institucion o centro de atencion que son
	 * los valores que pueden ir en el hashmap.
	 * los key's en el hashmap parametros deben llevar los
	 * siguientes nombres --> institucion y/o centroatencion
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerPisos (Connection connection, HashMap parametros)
	{
		ArrayList<HashMap<String, Object>> pisos  = new ArrayList<HashMap<String,Object>>();
		String cadena = strCadenaConsultaPisos;
		if (!parametros.containsKey("institucion"))
			return null;
		else
			if (parametros.containsKey("centroatencion"))
				cadena +=" AND P.centro_atencion="+Utilidades.convertirAEntero(parametros.get("centroatencion").toString())+"";
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try 
		{
			logger.info("consulta pisos ==> "+cadena);
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap mapa = new HashMap ();
				mapa.put("codigopiso", rs.getObject("codigopiso"));
				mapa.put("nombre", rs.getObject("nombre"));
				mapa.put("idcentroatencion", rs.getObject("idcentroatencion"));
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("nombrecentroatencion", rs.getObject("nombrecentroatencion"));
				
				pisos.add(mapa);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return pisos;
	}
	
	
	
	
	
	
	
	/**
	 * Metodo que retona un Arraylist de HashMap con los
	 * Centros de Atencion.
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param codigoInstitucion
	 * @param filtrarInsSirc
	 *  S --> filtrar los que tienen instucion sirc
	 *  N --> los que no tengan institucion sirc  
	 *  "" --> para no filtrar por institucion sirc 
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosAtencion(Connection con, int codigoInstitucion,String filtrarInsSirc) 
	{
		ArrayList<HashMap<String, Object>>centosAtencion = new ArrayList<HashMap<String,Object>>();
		if(codigoInstitucion == -2)
		{
			logger.info("===> Entré a la validación, el centro atención es -2");
			String cadena=	" SELECT consecutivo AS consecutivo, descripcion AS descripcion from centro_atencion where activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			if (UtilidadCadena.noEsVacio(filtrarInsSirc))
				if (UtilidadTexto.getBoolean(filtrarInsSirc))
					cadena+=" AND codigo_inst_sirc is not null";
				else
					cadena+=" AND codigo_inst_sirc is null";
			
			cadena+=" order by descripcion";
				
			try 
			{
				logger.info("consulta Centros Atencion ==> "+cadena);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoInstitucion);
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
				while (rs.next())
				{
					HashMap<String, Object> mapa = new HashMap<String, Object> ();
					mapa.put("consecutivo", rs.getObject("consecutivo"));
					mapa.put("descripcion", rs.getObject("descripcion"));
					
					centosAtencion.add(mapa);
					
				}
				
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		else
		{
			String cadena=	" SELECT consecutivo AS consecutivo, descripcion AS descripcion from centro_atencion where cod_institucion=? and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			if (UtilidadCadena.noEsVacio(filtrarInsSirc))
				if (UtilidadTexto.getBoolean(filtrarInsSirc))
					cadena+=" AND codigo_inst_sirc is not null";
				else
					cadena+=" AND codigo_inst_sirc is null";
			
			cadena+=" order by descripcion";
				
			try 
			{
				logger.info("consulta Centros Atencion ==> "+cadena);
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoInstitucion);
				ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
				while (rs.next())
				{
					HashMap<String, Object> mapa = new HashMap<String, Object> ();
					mapa.put("consecutivo", rs.getObject("consecutivo"));
					mapa.put("descripcion", rs.getObject("descripcion"));
					
					centosAtencion.add(mapa);
					
				}
				
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		
		}
		return centosAtencion;
	}
	

	
	/**
	 *TODO CAMBIAR DE POSICION 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoCentrosAtencion> obtenerCentrosAtencion(DtoCentrosAtencion dto )
	
	{
		ArrayList<DtoCentrosAtencion> arrayDto = new ArrayList<DtoCentrosAtencion>();
		String consultaStr= " Select 	consecutivo  as consecutivo ," +
													 " codigo as codigo ," +              
													 " descripcion as descripcion ," +        
													 " activo as activo," +            
													 " cod_institucion as codInstitucion ," +     
													 " codupgd as codupgd ," +            
													 " codigo_inst_sirc  as codigoInstSirc ," +    
													 " empresa_institucion  as empresaInstitucion," +
													 " direccion as direccion ," +          
													 " pais as pais," +                
													 " departamento as departamento ," +        
													 " ciudad  as ciudad," +             
													 " telefono as telefono ," +         
													 " region_cobertura as regionConbertura," +    
													 " categoria_atencion as categoriaAtencion " +
													 "from administracion.centro_atencion"+
													" where 1=1 " +
													"" ;
		
		consultaStr+=(0<dto.getConsecutivo())?" AND consecutivo ="+dto.getConsecutivo():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getCodigo())?" ":" AND codigo='"+dto.getCodigo()+"'";
		consultaStr+=UtilidadTexto.isEmpty(dto.getDescripcion())?" ":" AND descripcion='"+dto.getDescripcion()+"'";
		consultaStr+=(0<dto.getCodInstitucion())?" AND cod_institucion ="+dto.getCodInstitucion():"";
		consultaStr+=(0<dto.getCodupgd())?" AND codupgd ="+dto.getCodupgd():"";
		consultaStr+=UtilidadTexto.isEmpty(dto.getCodigoInstSirc())?"":"AND codigo_inst_sirc ='"+dto.getCodigoInstSirc()+"'" ;
		consultaStr+=(0<dto.getEmpresaInstitucion())?" AND empresa_institucion ="+dto.getEmpresaInstitucion():"";
		consultaStr+= UtilidadTexto.isEmpty(dto.getDireccion())?"": " AND direccion ='"+dto.getDireccion()+"'";
		consultaStr+= UtilidadTexto.isEmpty(dto.getPais())?"": " AND pais ='"+dto.getPais()+"'";
		consultaStr+= UtilidadTexto.isEmpty(dto.getDepartamento())?"": " AND departamento ='"+dto.getDepartamento()+"'";
		consultaStr+= UtilidadTexto.isEmpty(dto.getCiudad())?"":  " AND ciudad ='"+dto.getCiudad()+"'";
		
		consultaStr+= UtilidadTexto.isEmpty(dto.getTelefono())?"": " AND telefono ='"+dto.getTelefono()+"'";
		consultaStr+=(dto.getRegionCobertura()>0)?" AND  region_cobertura ="+dto.getRegionCobertura():" ";
		consultaStr+=(0<dto.getCategoriaAtencion())?" AND  categoria_atencion ="+dto.getCategoriaAtencion():" ";
		consultaStr+=" AND  activo='"+dto.isActivo()+"'"; //TODO MALO 
		
		

		logger.info("\n\n\n\n***********************************************************************************************");
		logger.info(" ----------------------------CARGANDO CENTROS DE ATENCION------------------------------------- \n\n\n");
		logger.info("\n\n\n CONSULTA   "+consultaStr);
		logger.info("-------------------\n\n\n\n\n\n");
		
		logger.info("-- Consultando Centro ATENCION -----------------");
		
		 try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by descripcion",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoCentrosAtencion newdto = new DtoCentrosAtencion();
				newdto.setConsecutivo(rs.getInt("consecutivo"));
				newdto.setCodigo(rs.getString("codigo"));
				newdto.setDescripcion(rs.getString("descripcion"));
				newdto.setCodInstitucion(rs.getInt("codInstitucion"));
				newdto.setCodupgd(rs.getInt("codupgd"));
				newdto.setCodigoInstSirc(rs.getString("codigoInstSirc"));
				newdto.setEmpresaInstitucion(rs.getInt("empresaInstitucion"));
				newdto.setDireccion(rs.getString("direccion"));
				newdto.setPais(rs.getString("pais"));
				newdto.setDepartamento(rs.getString("departamento"));
				newdto.setCiudad(rs.getString("ciudad"));
				newdto.setTelefono(rs.getString("telefono"));
				newdto.setRegionCobertura(rs.getDouble("regionConbertura"));
				newdto.setCategoriaAtencion(rs.getDouble("categoriaAtencion"));
				arrayDto.add(newdto);
			 }
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e2) {
					logger.error("Error al cerrar el recurso ", e2);
				}
			}
			if(ps != null){ 
				try {
					ps.close();
				} catch (SQLException e2) {
					logger.error("Error al cerrar el recurso ", e2);
				}
			}

			if(rs!=null){
                       try{
                              rs.close();
                       }catch(SQLException sqlException){
                              logger.error("Error al cerrar el recurso cambio metodo ", sqlException);
                       }
            }
		 }
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return arrayDto;
	}
	
	
	
	
	
	/**
	 *Metodo para retornar los pacientes 
	 *recibe un dto Paciente 
	 * @param obj
	 * @return Lista de pacientes
	 */
	
	public static ArrayList<Paciente> obtenerDatosPaciente(Paciente obj){
	
		ArrayList<Paciente> arrayDto = new ArrayList<Paciente>();
		
	String consultaStr="select " +
			"pa.codigo_paciente as codigoPaciente" + //1
			", pa.zona_domicilio as zonaDomicilio ," +//2
			" pa.ocupacion as ocupacion, " +//3
			"pa.tipo_sangre as tipoSangre," +//4
			
			" pa.observaciones as observaciones" +//5
			", pa.foto as foto" +//6
			" ,pa.esta_vivo as estaVivo" +//7
			" , pa.centro_atencion_pyp as centroAtencionPyp  " +//8
			" ,pa.lee_escribe as leeEscribe" +//9
			"  , pa.etnia as etnia ,"+//10
	        " pa.estudio as estudio" +//11
	        ", pa.fecha_muerte as fechaMuerte " +//12
	        ", pa.grupo_poblacional as grupoPoblacional," +//13
	        " pa.hora_muerte as horaMuerte,  " +//14
	        "pa.certificado_defuncion , " +//15
	        "pa.historia_clinica " +//16
	        " , pa.anio_historia_clinica , " +//17
	        "pa.religion ," +//18
	        "pa.histo_sistema_anterior, " +//19
	        " pe.primer_nombre as primerNombre," +
	        " pe.segundo_nombre as segundoNombre," +
	        " pe.primer_apellido as primerApellido," +
	        " pe.segundo_apellido as segundoApellido ,"+
	        " pe.tipo_identificacion as tipoIdentificacion,"+
	        " pe.tipo_persona as tipoPesona," +
	        " pe.numero_identificacion as numeroIdentificacion," +
	        " coalesce(pa.centro_atencion_duenio,"+ConstantesBD.codigoNuncaValido+") AS centroAtencionDuenio "+
	        " from" +
	        "	 administracion.personas pe  left outer join manejopaciente.pacientes pa ON "+
	        "(pa.codigo_paciente=pe.codigo) where 1=1 " ;
	
	consultaStr+=UtilidadTexto.isEmpty(obj.getTipoIdentificacion())?"":"AND tipo_identificacion ='"+obj.getTipoIdentificacion()+"'";
	consultaStr+=UtilidadTexto.isEmpty(obj.getNumeroIdentificacion())?"":" AND numero_identificacion ='"+obj.getNumeroIdentificacion()+"'";
	
	logger.info("Cargar PACIENTES------------------------------------------------------>");
	logger.info("/////////////////////////////////////////////////////////////////////////");
	logger.info(consultaStr);
	logger.info("/////////////////////////////////////////////////////////////////////////");
	
	
	
	 try 
	 {
		Connection con = UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr+" order by codigo_paciente",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		while(rs.next())
		 {
			Paciente newdto = new Paciente();
			newdto.setCodigoPersona(rs.getInt("codigoPaciente"));
			newdto.setZonaDomicilio(rs.getString("zonaDomicilio"));
			newdto.setOcupacion(rs.getString("ocupacion"));
			newdto.setTipoSangre(rs.getString("tipoSangre"));
			newdto.setFoto(rs.getString("foto"));
			newdto.setCentro_atencion(rs.getInt("centroAtencionPyp"));
			newdto.setLee_escribe(rs.getBoolean("leeEscribe"));
			newdto.setEtnia(rs.getInt("etnia"));
			newdto.setEstudio(rs.getInt("estudio"));
			//newdto.set)(rs.getString("fechaMuerte"));
			newdto.setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
			newdto.setTipoIdentificacion(rs.getString("tipoIdentificacion"));
			newdto.setPrimerNombrePersona(rs.getString("primerNombre"));
			newdto.setSegundoNombrePersona(rs.getString("segundoNombre"));
			newdto.setPrimerApellidoPersona(rs.getString("primerApellido"));
			newdto.setSegundoApellidoPersona(rs.getString("segundoApellido"));
			newdto.setTipoPersona(rs.getString("tipoPesona"));
			newdto.setCentroAtencionDuenio(rs.getInt("centroAtencionDuenio"));
			logger.info("primernombreDTO>> "+newdto.getPrimerNombrePersona()+"  deBD >>"+rs.getString("primerNombre"));
			
			arrayDto.add(newdto);
		 }
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e2) {
				logger.error("Error al cerrar el recurso ", e2);
			}
		}
		if(ps != null){ 
			try {
				ps.close();
			} catch (SQLException e2) {
				logger.error("Error al cerrar el recurso ", e2);
			}
		}

		if(rs!=null){
                   try{
                          rs.close();
                   }catch(SQLException sqlException){
                          logger.error("Error al cerrar el recurso cambio metodo ", sqlException);
                   }
        }
	 }
	catch (SQLException e) 
	{
		logger.error("error en carga==> "+e);
	}
	
	return arrayDto;
	   
	
	
	}	

	
	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de atencion.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion, viaingtipopac.
	 * @author Jhony Alexander Duque A.
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido para todos = 0
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion)
	{
		ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
		
		String tipoArea=tiposArea;
		if(tiposArea.indexOf(ConstantesBD.separadorSplit)>0)
		{
			String[] temp=tiposArea.split(ConstantesBD.separadorSplit);
			tipoArea=temp[0];
			for(int i=1;i<temp.length;i++)
			{
				tipoArea+="','"+temp[i];
			}
		}
		/**
		* Tipo Modificacion: Segun incidencia MT6477
		* Autor: Jesús Darío Ríos
		* usuario: jesrioro
		* Fecha: 19/02/2013
		* Descripcion: se  cambia  el  esquema 
		*  para  la  funcion getViaIngresoTipoPacCentroCost
		**/
		String cadena="select cc.codigo as codigo,coalesce (inventarios.GETVIAINGRESOTIPOPACCENTROCOST (cc.codigo),'-1') as viaingtipopac ,cc.nombre as nomcentrocosto,cc.tipo_area as codigotipoarea,ta.nombre as nombretipoarea,case when es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as activo,identificador,case when manejo_camas = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as manejacamas,cc.unidad_funcional as unidadfuncional,uf.descripcion as descunidadfuncional,cc.centro_atencion as codcentroatencion,getnomcentroatencion(cc.centro_atencion) as nomcentroatencion from centros_costo cc inner join tipos_area ta on(cc.tipo_area=ta.codigo) inner join unidades_funcionales uf on(uf.acronimo=cc.unidad_funcional and uf.institucion=cc.institucion) where cc.codigo>0 and cc.institucion=? ";
		if(!tipoArea.trim().equals(""))
		{
			cadena+=" AND cc.tipo_area in('"+tipoArea+"')";
		}
		if(!todos)
		{
			cadena+=" and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
		}
		if(centroAtencion>0)
		{
			cadena+=" and cc.centro_atencion="+centroAtencion;
		}
		try
		{
			logger.info("consulta Centros Costo ==> "+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by cc.nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa = new HashMap<String, Object> ();
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("viaingtipopac", rs.getObject("viaingtipopac"));
				mapa.put("nomcentrocosto", rs.getObject("nomcentrocosto"));
				mapa.put("codigotipoarea", rs.getObject("codigotipoarea"));
				mapa.put("nombretipoarea", rs.getObject("nombretipoarea"));
				mapa.put("activo", rs.getObject("activo"));
				mapa.put("identificador", rs.getObject("identificador"));
				mapa.put("manejacamas", rs.getObject("manejacamas"));
				mapa.put("unidadfuncional", rs.getObject("unidadfuncional"));
				mapa.put("descunidadfuncional", rs.getObject("descunidadfuncional"));
				mapa.put("codcentroatencion", rs.getObject("codcentroatencion"));
				mapa.put("nomcentroatencion", rs.getObject("nomcentroatencion"));
				
				centrosAtencion.add(mapa);
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return centrosAtencion;
	}

	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de atencion.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion, viaingtipopac.
	 * @author Jhony Alexander Duque A.
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido para todos = 0
	 * @param manejanCamas --> Opcional
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosCosto(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion, String manejanCamas)
	{
		ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
		
		String tipoArea=tiposArea;
		if(tiposArea.indexOf(ConstantesBD.separadorSplit)>0)
		{
			String[] temp=tiposArea.split(ConstantesBD.separadorSplit);
			tipoArea=temp[0];
			for(int i=1;i<temp.length;i++)
			{
				tipoArea+="','"+temp[i];
			}
		}
		String cadena="select cc.codigo as codigo,coalesce (getViaIngresoTipoPacCentroCost (cc.codigo),'-1') as viaingtipopac ,cc.nombre as nomcentrocosto,cc.tipo_area as codigotipoarea,ta.nombre as nombretipoarea,case when es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as activo,identificador,case when manejo_camas = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as manejacamas,cc.unidad_funcional as unidadfuncional,uf.descripcion as descunidadfuncional,cc.centro_atencion as codcentroatencion,getnomcentroatencion(cc.centro_atencion) as nomcentroatencion from centros_costo cc inner join tipos_area ta on(cc.tipo_area=ta.codigo) inner join unidades_funcionales uf on(uf.acronimo=cc.unidad_funcional and uf.institucion=cc.institucion) where cc.codigo>0 and cc.institucion=? ";
		if(!tipoArea.trim().equals(""))
			cadena+=" AND cc.tipo_area in('"+tipoArea+"')";
		if(!todos)
			cadena+=" and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
		if(centroAtencion>0)
			cadena+=" and cc.centro_atencion="+centroAtencion;
		
		if (UtilidadCadena.noEsVacio(manejanCamas))
			if(UtilidadTexto.getBoolean(manejanCamas))
				cadena+=" and cc.manejo_camas='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"'";
		
		try
		{
			logger.info("consulta Centros Costo ==> "+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by cc.nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa = new HashMap<String, Object> ();
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("viaingtipopac", rs.getObject("viaingtipopac"));
				mapa.put("nomcentrocosto", rs.getObject("nomcentrocosto"));
				mapa.put("codigotipoarea", rs.getObject("codigotipoarea"));
				mapa.put("nombretipoarea", rs.getObject("nombretipoarea"));
				mapa.put("activo", rs.getObject("activo"));
				mapa.put("identificador", rs.getObject("identificador"));
				mapa.put("manejacamas", rs.getObject("manejacamas"));
				mapa.put("unidadfuncional", rs.getObject("unidadfuncional"));
				mapa.put("descunidadfuncional", rs.getObject("descunidadfuncional"));
				mapa.put("codcentroatencion", rs.getObject("codcentroatencion"));
				mapa.put("nomcentroatencion", rs.getObject("nomcentroatencion"));
				
				centrosAtencion.add(mapa);
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return centrosAtencion;
	}

	
	
	/**
	 * Metodo que devuelve un Arraylist de HashMap
	 * con la informacion de los centros de atencion.
	 * Contiene los siguientes Key's -> codigo , nomcentrocosto,
	 * codigotipoarea,nombretipoarea,activo,identificador,manejacamas,
	 * unidadfuncional,descunidadfuncional,codcentroatencion,
	 * nomcentroatencion, viaingtipopac.
	 * @author lgchavez@princetonsa.com xplanner 16/07/08
	 * @param con --> Requerido
	 * @param institucion --> Requerido
	 * @param tiposArea -->No Requerido
	 * @param todos --> False o true
	 * @param centroAtencion --> Requerido para todos = 0
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCentrosCostoViaingreso(Connection con, int institucion, String tiposArea, boolean todos, int centroAtencion)
	{
		ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
		
		String tipoArea=tiposArea;
		if(tiposArea.indexOf(ConstantesBD.separadorSplit)>0)
		{
			String[] temp=tiposArea.split(ConstantesBD.separadorSplit);
			tipoArea=temp[0];
			for(int i=1;i<temp.length;i++)
			{
				tipoArea+="','"+temp[i];
			}
		}
		
		String cadena="select cc.codigo as codigo," +
							 "cc.nombre as nomcentrocosto,cc.tipo_area as codigotipoarea," +
							 "ta.nombre as nombretipoarea," +
							 "case when es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as activo," +
							 "identificador," +
							 "case when manejo_camas = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as manejacamas," +
							 "cc.unidad_funcional as unidadfuncional," +
							 "uf.descripcion as descunidadfuncional," +
							 "cc.centro_atencion as codcentroatencion," +
							 "getnomcentroatencion(cc.centro_atencion) as nomcentroatencion," +
							 "ccvi.via_ingreso||'@@@@@'|| ccvi.tipo_paciente as viaingtipopac " +
						"from " +
							"centros_costo cc " +
						"inner join tipos_area ta on(cc.tipo_area=ta.codigo) " +
						"inner join unidades_funcionales uf on(uf.acronimo=cc.unidad_funcional and uf.institucion=cc.institucion) " +
						"inner join centro_costo_via_ingreso ccvi on (cc.codigo=ccvi.centro_costo)" +
						"where " +
							"cc.codigo>0 " +
							"and cc.institucion=? ";
		
		if(!tipoArea.trim().equals(""))
		{
			cadena+=" AND cc.tipo_area in('"+tipoArea+"')";
		}
		if(!todos)
		{
			cadena+=" and cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+"";
		}
		if(centroAtencion>0)
		{
			cadena+=" and cc.centro_atencion="+centroAtencion;
		}
		try
		{
			logger.info("consulta Centros Costo via ingreso ==> "+cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena+" order by cc.nombre",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa = new HashMap<String, Object> ();
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("viaingtipopac", rs.getObject("viaingtipopac"));
				mapa.put("nomcentrocosto", rs.getObject("nomcentrocosto"));
				mapa.put("codigotipoarea", rs.getObject("codigotipoarea"));
				mapa.put("nombretipoarea", rs.getObject("nombretipoarea"));
				mapa.put("activo", rs.getObject("activo"));
				mapa.put("identificador", rs.getObject("identificador"));
				mapa.put("manejacamas", rs.getObject("manejacamas"));
				mapa.put("unidadfuncional", rs.getObject("unidadfuncional"));
				mapa.put("descunidadfuncional", rs.getObject("descunidadfuncional"));
				mapa.put("codcentroatencion", rs.getObject("codcentroatencion"));
				mapa.put("nomcentroatencion", rs.getObject("nomcentroatencion"));
				
				centrosAtencion.add(mapa);
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return centrosAtencion;
	}
	
	
	
	
	
	
	/**
	 * Metodo que me devuelve un arraylist de Hashmap
	 * con la informacion de los estados de la cama.
	 * Los key's del HashMap son:
	 * -codigo
	 * -nombre
	 * @author Adicionado por Jhony Alexander Duque A.
	 * @param connection
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstadosCama (Connection connection) 
	{
		ArrayList<HashMap<String, Object>> estados = new ArrayList<HashMap<String,Object>>();
		String consulta = strCadenaConsultaEstadoCamas;
		
		try 
		{
			logger.info("consulta estados de la cama  ==> "+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap<String, Object> mapa = new HashMap<String, Object> ();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("nombre", rs.getObject("nombre"));
				estados.add(mapa);
			}
			
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
			logger.error("Error al obtener los estados de la cama: "+e);
		}
		
		return estados;
	}	
		
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarReservarCama(Connection con, String codigoPaciente, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= consultarReservarCamaStr;
		
		try
		{						
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(codigoPaciente));
			ps.setInt(2,codigoInstitucion);
			logger.info("consulta reserva: "+consultarReservarCamaStr+", codigoPaciente: "+codigoPaciente+", codigoInstitucion: "+codigoInstitucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	
	/**
	 * Metodo implementado para realizar la insercion de la reserva de cama y el log de base de datos
	 * @param con
	 * @param HashMap mapa
	 * */
	public static int insertarReservarCama(Connection con,HashMap mapa)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int consecutivo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_reservar_cama");
			
			/**
			 * INSERT INTO reservar_cama (
			 * codigo, 
			 * centro_atencion, 
			 * codigo_paciente, 
			 * codigo_cama, 
			 * fecha_ocupacion, 
			 * institucion, 
			 * usuario_modifica,
			 * fecha_modifica, 
			 * hora_modifica, 
			 * estado, 
			 * observaciones) VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, '
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(consecutivo+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("codCentroAtencionSel")+""));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigoPaciente")+""));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigoCama")+""));
			if(mapa.get("fechaOcupacion")==null||!mapa.get("fechaOcupacion").toString().equals(""))
				ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechaOcupacion").toString())));
			else
				ps.setNull(5, Types.DATE);
			
			ps.setInt(6, Utilidades.convertirAEntero(mapa.get("institucion")+""));
			ps.setString(7, mapa.get("usuarioModifica")+"");
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(9, UtilidadFecha.getHoraActual());
			ps.setString(10, mapa.get("observaciones")+"");
		
			logger.info("valor del consecutivo >> "+consecutivo);
			
			if(ps.executeUpdate()>0)
			{
				mapa.put("codigo",consecutivo);				
				logReservarCama(con, mapa);
				return consecutivo;
			}	
		}
		catch(SQLException e)
		{
			logger.info("Error en insertarReservarCama: "+e);	  
		}
		return 0;
	}
	
	
	/**
	 * Metodo implementado para realizar la Modificacion de la reserva de cama
	 * @param con
	 * @param HashMap mapa
	 */
	public static boolean modificarReservarCama(Connection con, HashMap mapa)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificarStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE reservar_cama SET 
			 * centro_atencion=?, 
			 * codigo_cama=?, 
			 * fecha_ocupacion=?, 
			 * estado=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=?, 
			 * observaciones = ? 
			 * WHERE codigo=?
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("codCentroAtencionSel")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("codigoCama")+""));
			if(mapa.get("fechaOcupacion")==null||!mapa.get("fechaOcupacion").toString().equals(""))
				ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechaOcupacion").toString())));
			else
				ps.setNull(3, Types.DATE);
				
			ps.setString(4,mapa.get("estado")+"");
			ps.setString(5,mapa.get("usuarioModifica")+"");
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(7,UtilidadFecha.getHoraActual());
			ps.setString(8,mapa.get("observaciones")+"");
			ps.setDouble(9, Utilidades.convertirADouble(mapa.get("codigo")+""));
			
			if(ps.executeUpdate() >0)
			{
				logger.info("LA MODIFICACION FUE EXITOSA!! ");
				logReservarCama(con, mapa);
				return true;
			}
		}
		catch(SQLException e)
		{
			logger.info("Error en modificarReservarCama: "+e);
		}
		return false;
	}
	
	/**
	 * Metodo q mameja la informacion del log de base de datos
	 * @param con
	 * @param mapa
	 * @return
	 */
	private static boolean logReservarCama(Connection con, HashMap mapa)
	{
		
		  try
		  {
			  PreparedStatementDecorator ps1=  new PreparedStatementDecorator(con.prepareStatement(cadenaLogStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			  
			  /**
			   * INSERT INTO log_reservar_cama (
			   * codigo, 
			   * codigo_paciente, 
			   * nombres_paciente, 
			   * tipo_identificacion, 
			   * numero_identificacion, 
			   * cod_centro_atencion_sel, 
			   * centro_atencion_sel, 
			   * codigo_cama, 
			   * cama, 
			   * fecha_ocupacion, 
			   * fecha, 
			   * hora, 
			   * usuario, 
			   * cod_centro_atencion_res,
			   * centro_atencion_res,
			   * codigo_reserva) VALUES (?, ?, ?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			   */
			  
			  
			  ps1.setDouble(1,Utilidades.convertirADouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_reservar_cama")+""));
			  ps1.setInt(2,Utilidades.convertirAEntero(mapa.get("codigoPaciente")+""));
			  ps1.setString(3,mapa.get("nombresPaciente")+"");
			  ps1.setString(4,mapa.get("tipoIdentificacion")+"");
			  ps1.setString(5, mapa.get("numeroIdentificacion")+"");
			  ps1.setInt(6,Utilidades.convertirAEntero(mapa.get("codCentroAtencionSel")+""));
			  ps1.setString(7,mapa.get("centroAtencionSel")+"");
			  ps1.setInt(8,Utilidades.convertirAEntero(mapa.get("codigoCama")+""));
			  ps1.setString(9,mapa.get("cama")+"");
			  if(mapa.get("fechaOcupacion")==null||!mapa.get("fechaOcupacion").toString().equals(""))
					ps1.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechaOcupacion").toString())));
				else
					ps1.setNull(10, Types.DATE);
		
			  ps1.setDate(11,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			  ps1.setString(12,UtilidadFecha.getHoraActual());
			  ps1.setString(13,mapa.get("usuarioModifica")+"");
			  ps1.setInt(14,Utilidades.convertirAEntero(mapa.get("codCentroAtencionRes")+""));
			  ps1.setString(15,mapa.get("centroAtencionRes")+"");
			  ps1.setDouble(16,Utilidades.convertirADouble(mapa.get("codigo")+""));			  
			  
			  if(ps1.executeUpdate() >0)
					return true;
		  }
			catch(SQLException e)
			{
				logger.info("Error en modificarReservarCama: "+e);
			}
			return false;
	}
			 	 
	
	

	/**
	 * Método implementado para obtener las camas segun el filtro definido
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap obtenerCamas(Connection con, HashMap campos)
	{
		try
		{
			
			HashMap resultado = new HashMap();
			
			String consulta = "SELECT DISTINCT "+ 
				"c.codigo As codigo, " +				
				"coalesce(getnomhabitacioncama(c.codigo),'') AS habitacion," +
				"c.habitacion AS codigo_habitacion, "+
				"c.numero_cama AS numero_cama, " +
				"CASE WHEN c.descripcion IS NULL THEN '' ELSE c.descripcion END AS  descripcion, "+
				"c.estado AS codigo_estado, "+
				"CASE WHEN c.es_uci = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Sí' ELSE 'No' END AS es_uci, "+
				"c.tipo_usuario_cama AS codigo_usuario_cama, "+
				"c.centro_costo AS codigo_centro_costo, "+
				"getnomcentrocosto(c.centro_costo) AS nombre_centro_costo, "+
				"tu.nombre AS nombre_usuario_cama, " +
				"CASE WHEN tu.sexo IS NULL THEN ' ' ELSE CASE WHEN tu.sexo = "+ConstantesBD.codigoSexoMasculino+" THEN 'M' ELSE 'F' END END AS sexo, " +
				"tu.sexo AS sexocama," +
				"tu.ind_sexo_restrictivo AS isr, "+
				"tu.edad_inicial AS edad_inicial, "+
				"tu.edad_final AS edad_final, "+
				"ec.nombre As nombre_estado," +
				"coalesce(getnombrepisocama(c.codigo),'') as piso," +
				"hab.piso as codigo_piso," +
				"coalesce(getnomtipohabitacioncama(c.codigo),'') as tipo_habitacion," +
				"CASE WHEN c.es_uci = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Sí (' || manejopaciente.gettipomonitoreocama(c.codigo) || ')' ELSE 'No' END AS tipo_monitoreo "+ 
				"FROM camas1 c "+
				" LEFT JOIN reservar_cama rc ON (c.codigo=rc.codigo_cama) "+
				" INNER JOIN habitaciones hab ON(hab.codigo=c.habitacion) "+
				"INNER JOIN tipos_usuario_cama tu ON (tu.codigo=c.tipo_usuario_cama AND tu.institucion=c.institucion) "+ 
				"INNER JOIN estados_cama ec ON (ec.codigo=c.estado) "+ 
				"WHERE c.institucion = "+campos.get("institucion");
			
			
			if(!campos.get("piso").toString().equals(""))
				consulta += " AND hab.piso = "+campos.get("piso")+" ";
			
			if(campos.get("fechaNacimiento")!=null 
					&& !campos.get("fechaNacimiento").toString().equals(""))
			{
				int edadDias = UtilidadFecha.numeroDiasEntreFechas(campos.get("fechaNacimiento").toString(),UtilidadFecha.getFechaActual());
				consulta += " AND ("+edadDias+" >= tu.edad_inicial AND "+edadDias+" <= tu.edad_final ) ";
			}			
			
			if(campos.get("sexo")!=null&&!campos.get("sexo").toString().equals(""))
				consulta += " AND (tu.sexo = "+campos.get("sexo").toString()+" OR tu.sexo IS NULL)";
						
			if(campos.get("estado")!=null&&!campos.get("estado").toString().equals("")) {
				
				if(campos.get("codigoCentroAtencion")!=null&&!campos.get("codigoCentroAtencion").toString().equals("")&&
						campos.get("paciente")!=null&&!campos.get("paciente").toString().equals("")&&
						campos.get("codigoEstadoCamaReservada")!=null&&!campos.get("codigoEstadoCamaReservada").toString().equals("")&&
						campos.get("acronimoEstadoActivo")!=null&&!campos.get("acronimoEstadoActivo").toString().equals("")){
					consulta += " AND ((c.estado= "+campos.get("codigoEstadoCamaReservada")+" and rc.codigo_paciente= "+campos.get("paciente")+" and rc.estado= '"+campos.get("acronimoEstadoActivo")+"' and rc.centro_atencion="+campos.get("codigoCentroAtencion")+") or (c.estado= "+campos.get("estado")+" ))";					
				}					
				else {
				consulta += " AND c.estado IN ("+campos.get("estado")+") ";
				}			
			}
					
			if(campos.get("codigoCentroAtencion")!=null&&!campos.get("codigoCentroAtencion").toString().equals("")&&
					campos.get("paciente")!=null&&!campos.get("paciente").toString().equals("")&&
					campos.get("codigoEstadoCamaReservada")!=null&&!campos.get("codigoEstadoCamaReservada").toString().equals("")&&
					campos.get("acronimoEstadoActivo")!=null&&!campos.get("acronimoEstadoActivo").toString().equals(""))				
				consulta += " AND ((c.estado= "+campos.get("codigoEstadoCamaReservada")+" and rc.codigo_paciente= "+campos.get("paciente")+" and rc.estado= '"+campos.get("acronimoEstadoActivo")+"' and rc.centro_atencion="+campos.get("codigoCentroAtencion")+") or (c.estado= "+campos.get("estado")+" ))";					
				
			
			if (campos.get("codigoCentroCosto")!=null&&!campos.get("codigoCentroCosto").toString().equals(""))
				consulta += " AND c.centro_costo = "+campos.get("codigoCentroCosto");
			
			if(campos.get("codigoCentroAtencion")!=null&&!campos.get("codigoCentroAtencion").toString().equals("")&&
					campos.get("viaIngreso")!=null&&!campos.get("viaIngreso").toString().equals("")&&
					campos.get("tipoPaciente")!=null&&!campos.get("tipoPaciente").toString().equals(""))
				consulta += " AND c.centro_costo IN ("+
					"SELECT c.codigo " +
					"FROM centros_costo c " +
					"INNER JOIN centro_costo_via_ingreso cv ON(c.codigo=cv.centro_costo) " +
					"WHERE cv.via_ingreso = "+campos.get("viaIngreso")+" and cv.tipo_paciente = '"+campos.get("tipoPaciente")+"' and c.centro_atencion = "+campos.get("codigoCentroAtencion")+") ";
			
			if(campos.get("codigoCentroAtencion")!=null&&!campos.get("codigoCentroAtencion").toString().equals("")&&
					(campos.get("viaIngreso")==null||campos.get("viaIngreso").toString().equals("")||campos.get("tipoPaciente")==null||campos.get("tipoPaciente").toString().equals("")))
				consulta += " AND c.centro_costo IN ("+
					"SELECT codigo " +
					"FROM centros_costo " +
					"WHERE centro_atencion = "+campos.get("codigoCentroAtencion")+") ";
			
			//Validacion movimiento camas
			if(!campos.get("fechaMovimiento").toString().equals("") && !campos.get("horaMovimiento").toString().equals(""))
			{
			    consulta +=" AND c.codigo NOT IN (" +
			    	"SELECT codigo_nueva_cama " +
			    	"from traslado_cama " +
			    	"where " +
			    	"(" +
			    		"(fecha_asignacion>'"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaMovimiento").toString())+"') " +
			    		"or " +
			    		"(fecha_asignacion='"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaMovimiento").toString())+"' AND hora_asignacion>='"+campos.get("horaMovimiento")+"')" +
			    	") AND codigo_nueva_cama = c.codigo" +
			    ")";
			}
			
			if(!campos.get("asignableAdmision").toString().equals(""))
				consulta += " and c.asignable_admision = '"+campos.get("asignableAdmision")+"' ";

			consulta += " ORDER BY c.habitacion, c.numero_cama";
			
			
			logger.info("CONSULTA DE CAMAS=> "+consulta);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			if(campos.get("sexo")!=null&&!campos.get("sexo").toString().equals("")&&!campos.get("sexo").toString().equals("-1"))
				resultado = accionValidacionesSexo(con,campos,UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta))));
			else
				resultado = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)));
			
			String [] indice_resultado = {"codigo_","habitacion_","numero_cama_","descripcion_","codigo_estado_","es_uci_","codigo_usuario_cama_",
					"codigo_centro_costo_","nombre_centro_costo_","nombre_usuario_cama_","sexo_","sexocama_","isr_","edad_inicial_","edad_final_",
					"nombre_estado_","piso_","tipo_habitacion_","descripcion_","tipo_monitoreo_","codigo_piso_"};
			
			resultado.put("INDICES_MAPA",indice_resultado);
		
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCamas: "+e);
			return null;
		}
	}
	
	
	
	
	/**
	 * Validaciones de restricciones de sexo en las camas
	 * @param Connection con
	 * @param ReservarCamaForm forma,
	 * @param UsuarioBasico usuario 
	 * @param PersonaBasica paciente 
	 * */
	public static HashMap accionValidacionesSexo(Connection con,
											     HashMap camposMap,									    
											     HashMap camasMap)
	{
		PreparedStatementDecorator ps;
		HashMap camasCompanerasMap;
		HashMap resultadoMap = new HashMap();
		
		int numRegistros = Utilidades.convertirAEntero(camasMap.get("numRegistros").toString());
		int numRegistrosMap = 0;
		int numRegistrosResultadoMap = 0;
		
		 
		
		String consultaReservacamas = "SELECT "+ 
			"c.codigo AS codigo," +
			"c.estado AS estado," +
			"getSexoCama (c.codigo,c.estado,"+camposMap.get("codigoCentroAtencion").toString()+","+camposMap.get("institucion").toString()+") AS sexo "+					 
			"FROM camas1 c " +						 
			"WHERE c.institucion = "+camposMap.get("institucion")+" AND c.habitacion = ? AND c.codigo <> ? " +
					" AND (c.estado = "+ConstantesBD.codigoEstadoCamaOcupada+" OR c.estado="+ConstantesBD.codigoEstadoCamaReservada+") ";						
			
		if(numRegistros>0)
		{
			for(int i=0 ; i<numRegistros; i++)		
			{								
				//sexo con restriccion 
				if(!camasMap.get("sexocama_"+i).equals(""))
				{
					//evaluo Sexo del paciente igual al Sexo de la cama
					if(camposMap.get("sexo").toString().equals(camasMap.get("sexocama_"+i).toString()))					
						camasMap.put("esValido_"+i,ConstantesBD.acronimoSi);					
					else
						camasMap.put("esValido_"+i,ConstantesBD.acronimoNo);						
				}
				//sexo sin restriccion 
				else if(camasMap.get("sexocama_"+i).equals(""))
				{
					//indicativo de sexo restrictivo igual No
					if(camasMap.get("isr_"+i).equals(ConstantesBD.acronimoNo))				
						camasMap.put("esValido_"+i,ConstantesBD.acronimoSi);
					//indicativo de sexo restrictivo igual Si
					else if(camasMap.get("isr_"+i).equals(ConstantesBD.acronimoSi))
					{
						try{
							//se toman las compañeras de cama de la habitacion
							 ps =  new PreparedStatementDecorator(con.prepareStatement(consultaReservacamas, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							 
							 if(!camasMap.get("codigo_habitacion_"+i).toString().equals(""))
								 ps.setInt(1,Utilidades.convertirAEntero(camasMap.get("codigo_habitacion_"+i).toString()));
							 else
								 ps.setInt(1,0);
							 
							 ps.setInt(2,Utilidades.convertirAEntero(camasMap.get("codigo_"+i).toString()));						 
							 
							 camasCompanerasMap = new HashMap();
							 camasCompanerasMap = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
							 
							 numRegistrosMap = Utilidades.convertirAEntero(camasCompanerasMap.get("numRegistros").toString());
							 
							 //no existen camnas en los estados ocupado o reservado
							 if(numRegistrosMap == 0)							 
								 camasMap.put("esValido_"+i,ConstantesBD.acronimoSi);
							 else
							 {
								 //Recorre el listado de camas compañeras
								 for(int j=0; j<numRegistrosMap;j++)
								 {
									 camasMap.put("esValido_"+i,ConstantesBD.acronimoSi);
									 
									 if(!camasCompanerasMap.get("sexo_"+j).toString().equals(camposMap.get("sexo").toString()))
									 {
										 camasMap.put("esValido_"+i,ConstantesBD.acronimoNo);
										 j = numRegistrosMap;
									 }
								 }								 
							 }							 						 
						 
						}
						catch(SQLException e){
							logger.error("Error : "+e);
						}
					}
				}
			}
		}
		else{
			return camasMap;
		}
		
		
		
		
		//actualiza las camas que cumplan con las validaciones		
		resultadoMap.put("numRegistros","0");
		numRegistrosResultadoMap = 0;
		for(int i=0 ; i<numRegistros; i++)		
		{
			if(camasMap.get("esValido_"+i).toString().equals(ConstantesBD.acronimoSi))
			{
				resultadoMap.put("codigo_"+numRegistrosResultadoMap,camasMap.get("codigo_"+i));								
				resultadoMap.put("habitacion_"+numRegistrosResultadoMap,camasMap.get("habitacion_"+i));
				resultadoMap.put("numero_cama_"+numRegistrosResultadoMap,camasMap.get("numero_cama_"+i));
				resultadoMap.put("descripcion_"+numRegistrosResultadoMap,camasMap.get("descripcion_"+i));
				resultadoMap.put("codigo_estado_"+numRegistrosResultadoMap,camasMap.get("codigo_estado_"+i));
				resultadoMap.put("es_uci_"+numRegistrosResultadoMap,camasMap.get("es_uci_"+i));
				resultadoMap.put("codigo_usuario_cama_"+numRegistrosResultadoMap,camasMap.get("codigo_usuario_cama_"+i));
				resultadoMap.put("codigo_centro_costo_"+numRegistrosResultadoMap,camasMap.get("codigo_centro_costo_"+i));
				resultadoMap.put("nombre_centro_costo_"+numRegistrosResultadoMap,camasMap.get("nombre_centro_costo_"+i));
				resultadoMap.put("nombre_usuario_cama_"+numRegistrosResultadoMap,camasMap.get("nombre_usuario_cama_"+i));
				resultadoMap.put("sexo_"+numRegistrosResultadoMap,camasMap.get("sexo_"+i));
				resultadoMap.put("sexocama_"+numRegistrosResultadoMap,camasMap.get("sexocama_"+i));
				resultadoMap.put("isr_"+numRegistrosResultadoMap,camasMap.get("isr_"+i));
				resultadoMap.put("edad_inicial_"+numRegistrosResultadoMap,camasMap.get("edad_inicial_"+i));
				resultadoMap.put("edad_final_"+numRegistrosResultadoMap,camasMap.get("edad_final_"+i));
				resultadoMap.put("nombre_estado_"+numRegistrosResultadoMap,camasMap.get("nombre_estado_"+i));
				resultadoMap.put("piso_"+numRegistrosResultadoMap,camasMap.get("piso_"+i));
				resultadoMap.put("tipo_habitacion_"+numRegistrosResultadoMap,camasMap.get("tipo_habitacion_"+i));		
				resultadoMap.put("codigo_piso_"+numRegistrosResultadoMap,camasMap.get("codigo_piso_"+i));
				resultadoMap.put("tipo_monitoreo_"+numRegistrosResultadoMap,camasMap.get("tipo_monitoreo_"+i));
				numRegistrosResultadoMap++;
			}
		}
		
		resultadoMap.put("numRegistros",numRegistrosResultadoMap);
		
		
		return resultadoMap;
	}	
	
	
	
	/**
	 * Método implementado para obtener los origenes de la admision
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerOrigenesAdmision(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "select codigo,nombre from ori_admision_hospi";
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("nombre",rs.getObject("nombre"));
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.info("Error en obtenerOrigenesAdmision: "+e);
			
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return resultado;
	}
	
	
	
	
	/**
	 * Metodo Adicionado por Jhony Alexander Duque A.
	 * 11/01/2008
	 * Metodo encargado de obtener las secciones de parametros
	 * entidades subcontratadas.
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerSeccionesParametrosEntidadesSubcontratadas(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultado = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT  codigo,nombre from param_entidad_subcontratada";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			int i=0;
			while (rs.next())
			{
				HashMap<String, Object> mapa=new HashMap<String, Object>();
				mapa.put("codigo",rs.getObject("codigo"));
				mapa.put("nombre",rs.getObject("nombre"));
				resultado.add(i, mapa);
			}
		}
		catch(SQLException e)
		{
			logger.info("Error en obtenerSeccionesParametrosEntidadesSubcontratadas: "+e);
			
		}
		return resultado;
	}
	
	
	
	
	/**
	 * Método que consulta los datos de la cama reservada de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarCamaReservada(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "SELECT " +
				"rc.codigo AS codigo_reserva," +
				"rc.centro_atencion AS centro_atencion," +
				"to_char(rc.fecha_ocupacion, 'DD/MM/YYYY') AS fecha_ocupacion,"+ 
				"cam.codigo as codigo, "+ 
				"cam.numero_cama as numero_cama, "+
				"getDescripcionCama2(rc.codigo_cama) as nombre_cama, " +
				"cam.centro_costo as centro_costo, "+ 
				"estcam.nombre as estado_cama, "+ 
				"getnomcentrocosto(cam.centro_costo) as nombre_centro_costo, "+ 
				"tipuscam.nombre as tipo_usuario, "+ 
				"coalesce(tipuscam.sexo,0) AS codigo_sexo, "+
				"coalesce(getdescripcionsexo(tipuscam.sexo),'') AS nombre_sexo, "+
				"tipuscam.ind_sexo_restrictivo AS isr, "+
				"tipuscam.edad_inicial AS edad_inicial, "+
				"tipuscam.edad_final AS edad_final, "+
				"cam.descripcion as descripcion, "+
				"coalesce(getnomhabitacioncama(cam.codigo),'') AS habitacion," +
				"coalesce(getnomtipohabitacioncama(cam.codigo),'') As tipo_habitacion, " +
				"coalesce(getnombrepisocama(cam.codigo),'') AS piso, " + 
				"CASE WHEN cam.es_uci = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'SI' ELSE 'NO' END AS es_uci, "+ 
				"CASE WHEN cam.es_uci = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN getnombretipomonitoreo(sc.tipo_monitoreo) ELSE '' END AS tipo_monitoreo "+ 
				"from reservar_cama rc "+ 
				"INNER JOIN camas1 cam ON(cam.codigo=rc.codigo_cama) "+ 
				"INNER JOIN estados_cama estcam on(cam.estado=estcam.codigo) "+ 
				"INNER JOIN tipos_usuario_cama tipuscam ON(cam.tipo_usuario_cama=tipuscam.codigo and cam.institucion=tipuscam.institucion) "+ 
				"LEFT OUTER JOIN servicios_cama sc ON(sc.codigo_cama=cam.codigo) "+ 
				"WHERE "+ 
				"rc.codigo_paciente = "+campos.get("codigoPaciente")+" and rc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";  
				
			if (campos.containsKey("centroAtencion") && campos.get("centroAtencion") != null && !(campos.get("centroAtencion")+"").equals("-1") && !campos.get("centroAtencion").equals("") )
				consulta+=" and rc.centro_atencion = "+campos.get("centroAtencion");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), false, true);
	        st.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCamaReservada: "+e);
			return null;
		}
	}
	
	/**
	 * Método que libera una cama reserva de un paciente y le cambia su estado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean liberarReservaCama(Connection con,HashMap campos)
	{
		try
		{
			
			boolean exito0 = false, exito1 = false;
			String consulta = "UPDATE reservar_cama SET estado = '"+campos.get("estado")+"' WHERE " +
				"codigo = "+campos.get("codigoReserva");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			if(st.executeUpdate(consulta)>0)
				exito0 = true;
			
			//Se actualiza el estado de la cama a disponible
			if(exito0)
			{
				consulta = "UPDATE camas1 SET estado = "+(campos.get("estado").toString().equals(ConstantesIntegridadDominio.acronimoEstadoOcupado)?ConstantesBD.codigoEstadoCamaOcupada:ConstantesBD.codigoEstadoCamaDisponible)+" WHERE codigo = "+campos.get("codigoCama");
				st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				if(st.executeUpdate(consulta)>0)
					exito1 = true;
			}
			
			if(exito0&&exito1)
				return true;
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en liberarReservaCama: "+e);
			return false;
		}
	}
	
	/**
	 * Método que consulta las etnias activas del sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEtnias(Connection con)
	{
		ArrayList<HashMap<String, Object>> etnias = new ArrayList<HashMap<String,Object>>();
		Statement st =  null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigo, nombre FROM manejopaciente.etnia WHERE activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" order by nombre";
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				etnias.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEtnias: "+e);
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return etnias;
	}
	
	/**
	 * Método que consulta las religiones parametrizadas por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerReligiones(Connection con,int institucion)
	{
		ArrayList<HashMap<String, Object>> religiones = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigo, descripcion FROM manejopaciente.religiones WHERE activo = '"+ConstantesBD.acronimoSi+"' AND institucion = "+institucion+" ORDER BY descripcion";
			
			st = con.createStatement( );
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("descripcion", rs.getObject("descripcion"));
				
				religiones.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerReligiones: "+e);
			
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
			
		}
		return religiones;
	}
	
	/**
	 * Método que consulta los estudios parametrizados del sistema
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstudios(Connection con, int tipoBD)
	{
		ArrayList<HashMap<String, Object>> estudios = new ArrayList<HashMap<String,Object>>();
		String consulta = "";
		switch(tipoBD)
		{
		case DaoFactory.ORACLE:
			consulta = "select codigo,nombre from manejopaciente.estudio where activo = 1 order by nombre";
			break;
		case DaoFactory.POSTGRESQL:
			consulta = "select codigo,nombre from manejopaciente.estudio where activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" order by nombre";
			break;
		}
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				
				estudios.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEstudios: "+e);
		}finally{
		
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return estudios;
	}
	
	/**
	 * Método que consulta los convenios de un paciente, que están asociados a la estructura convenios_paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap consultarConveniosPaciente(Connection con,String codigoPaciente)
	{
		Statement st =  null;
		ResultSetDecorator rs = null; 
		try   
		{
			String consulta = "(" +
					"SELECT "+ 
					"cp.codigo_paciente, " + 
					"cp.convenio AS codigo_convenio, "+ 
					" coalesce(cp.estrato_social, "+ConstantesBD.codigoNuncaValido+") AS codigo_estrato_social, "+
					"'' as tipoafiliado," +
					"'' as codigo_naturaleza,"+
					"coalesce(to_char(cp.fecha_afiliacion,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_afiliacion, "+ 
					"c.nombre AS nombre_convenio, "+
					"c.tipo_regimen AS codigo_tipo_regimen, " +
					"CASE WHEN c.empresa_institucion IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.empresa_institucion END AS empresas_institucion, "+				 
					"getnomtiporegimen(c.tipo_regimen) AS nombre_tipo_regimen, "+
					"CASE WHEN c.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_pyp, "+
					"CASE WHEN c.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_capitado "+ 
					"FROM convenios_pacientes cp "+ 
					"INNER JOIN convenios c ON(c.codigo=cp.convenio) "+ 
					"WHERE "+ 
					"cp.codigo_paciente = "+codigoPaciente+" AND getEsConvenioPacVigente(cp.codigo_paciente,cp.convenio,c.tipo_contrato) = '"+ConstantesBD.acronimoSi+"' " +
				")" +
				"UNION " +
				"(" +
					"SELECT DISTINCT "+ 
					"uxc.persona AS codigo_paciente, " +
					"conv.codigo AS codigo_convenio," +
					"uxc.CLASIFICACION_SOCIO_ECONOMICA AS codigo_estrato_social," +
					"uxc.tipo_afiliado as tipoafiliado, "+
					"uxc.NATURALEZA_PACIENTES||'' as codigo_naturaleza,"+
					"'' AS fecha_afiliacion, "+
					"conv.nombre AS nombre_convenio, "+
					"conv.tipo_regimen As codigo_tipo_regimen," +
					"coalesce(conv.empresa_institucion,"+ConstantesBD.codigoNuncaValido+") AS empresas_institucion, "+
					"getnomtiporegimen(conv.tipo_regimen) AS nombre_tipo_regimen, "+
					"CASE WHEN conv.pyp = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_pyp, "+
					"CASE WHEN conv.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_capitado "+ 
					"FROM usuario_x_convenio uxc "+ 
					"INNER JOIN contratos c ON(c.codigo=uxc.contrato) "+ 
					"INNER JOIN convenios conv ON(c.convenio=conv.codigo) "+ 
					"WHERE "+ 
					"uxc.persona = "+codigoPaciente+" AND "+ 
					"(to_char(CURRENT_DATE, 'YYYY-MM-DD') BETWEEN to_char(uxc.fecha_inicial, 'YYYY-MM-DD') AND to_char(uxc.fecha_final, 'YYYY-MM-DD')) AND " +
					"conv.codigo NOT IN (" +
						"SELECT cp.convenio " +
						"FROM convenios_pacientes cp " +
						"INNER JOIN convenios conve ON(conve.codigo=cp.convenio) " +
						"WHERE cp.codigo_paciente = "+codigoPaciente+" AND " +
						"getEsConvenioPacVigente(cp.codigo_paciente,conve.codigo,conve.tipo_contrato) = '"+ConstantesBD.acronimoSi+"'" +
						") AND " +
					"getEsConvenioPacVigente(uxc.persona,conv.codigo,conv.tipo_contrato) = '"+ConstantesBD.acronimoSi+"'" +
				")";
				
			logger.info("SqlBaseUtilidadesManejoPacienteDao(1853) Consulta: "+consulta);
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(rs, true, true);
	     
			return mapaRetorno;
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarConveniosPaciente: "+e);
			return null;
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);	
			
		}
	}
	
	/**
	 * Método que consulta los tipos de paciente definidos por vía de ingreso
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposPacienteXViaIngreso(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			if(campos.containsKey("codigoViaIngreso")&&Utilidades.convertirAEntero(campos.get("codigoViaIngreso")+"")>0)
			{
				String consulta = "SELECT "+ 
					"vi.via_ingreso as viaIngreso, "+ 
					"vi.tipo_paciente as tipoPaciente, "+ 
					"tp.nombre as nombreTipoPaciente "+ 
					"from tip_pac_via_ingreso vi "+ 
					"INNER JOIN tipos_paciente tp ON(tp.acronimo=vi.tipo_paciente) "+ 
					"where "+ 
					"vi.via_ingreso = "+campos.get("codigoViaIngreso"); 
					
				
				if(campos.containsKey("tipoPaciente") 
						&& !campos.get("tipoPaciente").equals(""))
				{
					consulta+=" AND vi.tipo_paciente = '"+campos.get("tipoPaciente").toString()+"' ";
				}
				
				consulta+=" order by tp.nombre";
				
				logger.info("la consulta de tipos de paciente es=> "+consulta);
				st = con.createStatement();
				rs = new ResultSetDecorator(st.executeQuery(consulta));
				while(rs.next())
				{
					HashMap<String, Object> elemento = new HashMap<String, Object>();
					elemento.put("codigoViaIngreso", rs.getObject("viaIngreso"));
					elemento.put("codigoTipoPaciente", rs.getObject("tipoPaciente"));
					elemento.put("nombreTipoPaciente", rs.getObject("nombreTipoPaciente"));
					resultados.add(elemento);
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTiposPacienteXViaIngreso: "+e);
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para cargar el responsable del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static DtoResponsablePaciente cargarResponsablePaciente(Connection con,HashMap campos)
	{
		DtoResponsablePaciente responsable = new DtoResponsablePaciente();
		try
		{
			String consulta = "SELECT "+
				"rp.codigo, "+ 
				"rp.numero_identificacion, "+ 
				"rp.tipo_identificacion, "+ 
				"coalesce(rp.direccion,'') As direccion, "+ 
				"rp.telefono, "+ 
				"rp.relacion_paciente, "+ 
				"coalesce(rp.codigo_pais_doc,'') AS codigo_pais_doc, "+
				"coalesce(getdescripcionpais(rp.codigo_pais_doc),'') AS descripcion_pais_doc, "+
				"coalesce(rp.codigo_ciudad_doc,'') AS codigo_ciudad_doc, "+
				"coalesce(rp.codigo_depto_doc,'') AS codigo_depto_doc, "+
				"CASE WHEN rp.codigo_ciudad_doc IS NULL THEN '' ELSE getnombreciudad(rp.codigo_pais_doc,rp.codigo_depto_doc,rp.codigo_ciudad_doc) || ' (' || getnombredepto(rp.codigo_pais_doc,rp.codigo_depto_doc) || ')' END AS descripcion_ciudad_doc, "+
				"rp.primer_apellido, "+
				"coalesce(rp.segundo_apellido,'') AS segundo_apellido, "+
				"rp.primer_nombre, "+
				"coalesce(rp.segundo_nombre,'') AS segundo_nombre, "+
				"coalesce(rp.codigo_pais,'') AS codigo_pais, "+
				"coalesce(getdescripcionpais(rp.codigo_pais),'') AS descripcion_pais, "+
				"coalesce(rp.codigo_ciudad,'') AS codigo_ciudad, "+
				"coalesce(rp.codigo_depto,'') AS codigo_depto, "+ 
				"CASE WHEN rp.codigo_ciudad IS NULL THEN '' ELSE getnombreciudad(rp.codigo_pais,rp.codigo_depto,rp.codigo_ciudad) || ' (' || getnombredepto(rp.codigo_pais,rp.codigo_depto) || ')' END AS descripcion_ciudad, " +
				"coalesce(rp.codigo_barrio||'','') AS codigo_barrio," +
				"coalesce(getdescripcionbarrio(rp.codigo_barrio),'') As descripcion_barrio, "+
				"coalesce(to_char(rp.fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_nacimiento "+ 
				"FROM responsables_pacientes rp " +
				"WHERE "; 
			
			consulta += "rp.tipo_identificacion = '"+campos.get("tipoIdentificacion")+"' AND rp.numero_identificacion = '"+campos.get("numeroIdentificacion")+"'";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				responsable.setCodigo(rs.getString("codigo"));
				responsable.setNumeroIdentificacion(rs.getString("numero_identificacion"));
				responsable.setTipoIdentificacion(rs.getString("tipo_identificacion"));
				responsable.setDireccion(rs.getString("direccion"));
				responsable.setTelefono(rs.getString("telefono"));
				responsable.setRelacionPaciente(rs.getString("relacion_paciente"));
				responsable.setCodigoPaisExpedicion(rs.getString("codigo_pais_doc"));
				responsable.setDescripcionPaisExpedicion(rs.getString("descripcion_pais_doc"));
				responsable.setCodigoCiudadExpedicion(rs.getString("codigo_ciudad_doc"));
				responsable.setCodigoDeptoExpedicion(rs.getString("codigo_depto_doc"));
				responsable.setDescripcionCiudadExpedicion(rs.getString("descripcion_ciudad_doc"));
				responsable.setPrimerApellido(rs.getString("primer_apellido"));
				responsable.setSegundoApellido(rs.getString("segundo_apellido"));
				responsable.setPrimerNombre(rs.getString("primer_nombre"));
				responsable.setSegundoNombre(rs.getString("segundo_nombre"));
				responsable.setCodigoPais(rs.getString("codigo_pais"));
				responsable.setCodigoDepto(rs.getString("codigo_depto"));
				responsable.setCodigoCiudad(rs.getString("codigo_ciudad"));
				responsable.setDescripcionPais(rs.getString("descripcion_pais"));
				responsable.setCodigoBarrio(rs.getString("codigo_barrio"));
				responsable.setDescripcionBarrio(rs.getString("descripcion_barrio"));
				responsable.setFechaNacimiento(rs.getString("fecha_nacimiento"));
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarResponsablePaciente: "+e);
			
		}
		return responsable;
	}
	
	/**
	 * Método que obtiene los profesiones de la salud por institucion mas otros filtros
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionales(Connection con,HashMap campos)
	{
		String consulta = "";
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
				consulta = "SELECT DISTINCT "+
				"per.primer_apellido, "+
				"coalesce(per.segundo_apellido,'') AS segundo_apellido, "+
				"per.primer_nombre, "+
				"coalesce(per.segundo_nombre,'') As segundo_nombre, "+
				"per.tipo_identificacion, "+
				"per.numero_identificacion, "+
				"med.codigo_medico "+
				"FROM personas per " +
				"INNER JOIN medicos med ON(med.codigo_medico=per.codigo) "+
				"INNER JOIN medicos_instituciones medins ON(medins.codigo_medico=med.codigo_medico) " +
				"LEFT OUTER JOIN administracion.especialidades_medicos em ON (em.codigo_medico=med.codigo_medico) " +
				"WHERE " +
				"medins.codigo_institucion= "+campos.get("institucion");
			
			//Verifica si se debe validar que los profesionales deben estar activos
			if(UtilidadTexto.getBoolean(campos.get("activo").toString()))
				consulta += " AND med.codigo_medico || '-'|| medins.codigo_institucion NOT IN (SELECT mi.codigo_medico || '-' || mi.codigo_institucion from medicos_inactivos mi WHERE mi.codigo_medico=med.codigo_medico AND mi.codigo_institucion=medins.codigo_institucion) ";
			
			//Verifica si se debe validar la ocupacion del medico
			if(UtilidadTexto.getBoolean(campos.get("ocupacion").toString()))
				consulta += " AND med.ocupacion_medica IN("+
					ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(Utilidades.convertirAEntero(campos.get("institucion").toString()),true)+","+
					ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(Utilidades.convertirAEntero(campos.get("institucion").toString()),true)+") ";
			
			//Se verifica si se puede filtrar por centro de costo
			if(!campos.get("centroCosto").toString().equals(""))
				consulta += " AND med.codigo_medico IN (SELECT u.codigo_persona FROM usuarios u INNER JOIN centros_costo_usuario cu ON(cu.usuario=u.login) WHERE u.codigo_persona = med.codigo_medico AND cu.centro_costo = "+campos.get("centroCosto")+")";
			
			//Se filtra por profesionales ya ingresados
			if(!campos.get("codigosInsertados").toString().equals(""))
				consulta += " AND med.codigo_medico NOT IN ("+campos.get("codigosInsertados").toString()+ConstantesBD.codigoNuncaValido+") ";
			
			//Se filtra por las especialidades del medico
			if(campos.containsKey("especialidades") && !campos.get("especialidades").toString().equals("") && !campos.get("especialidades").toString().equals(ConstantesBD.codigoNuncaValido+""))
				consulta += " AND me.codigo_especialidad IN ("+campos.get("especialidades")+") ";
			
			
			consulta += "  ORDER BY per.primer_apellido,coalesce(per.segundo_apellido,''),per.primer_nombre,coalesce(per.segundo_nombre,'')";
			
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("primerApellido",rs.getString("primer_apellido"));
				elemento.put("segundoApellido",rs.getString("segundo_apellido")==null?"":rs.getString("segundo_apellido"));
				elemento.put("primerNombre",rs.getString("primer_nombre"));
				elemento.put("segundoNombre",rs.getString("segundo_nombre")==null?"":rs.getString("segundo_nombre"));
				elemento.put("tipoIdentificacion",rs.getString("tipo_identificacion"));
				elemento.put("numeroIdentificacion",rs.getString("numero_identificacion"));
				elemento.put("codigoMedico",rs.getString("codigo_medico"));
				
				resultados.add(elemento);
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerProfesionales: "+e+" >> "+consulta);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return resultados;
	}
	
	
	/**
	 * Método que verifica si es requerido ingresar al documento de garantía 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esRequeridoDocumentoGarantia(Connection con,HashMap campos)
	{
		boolean esRequerido = false;
		try
		{
			//*************VERIFICACION DE VIAS DE INGRESO X TIPOS DE PACIENTE****************************************
			String consulta = "SELECT garantia FROM garantia_paciente WHERE codigo_via_ingreso = "+campos.get("codigoViaIngreso")+" AND acronimo_tipo_paciente = '"+campos.get("codigoTipoPaciente")+"'";
			String garantia = ConstantesBD.acronimoNo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				garantia = rs.getString("garantia");
			//***********VERIFICACION DE TIPOS DE IDENTIFICACION*****************************************
			String exceptoDeudor = ConstantesBD.acronimoSi;
			consulta = "SELECT excento_deudor FROM tipos_id_institucion WHERE acronimo = '"+campos.get("codigoTipoIdentificacion")+"' and institucion = "+campos.get("institucion");
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				exceptoDeudor = rs.getString("excento_deudor");
				
			if(UtilidadTexto.getBoolean(garantia)&&!UtilidadTexto.getBoolean(exceptoDeudor))
				esRequerido = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en esRequeridoDocumentoGarantia: "+e);
		}
		return esRequerido;
	}
	
	/**
	 * Método que verifica si es requerido el deudor
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esRequeridoDeudor(Connection con,HashMap campos)
	{
		boolean esRequerido = false;
		try
		{
			//**************************VERIFICACION DE TIPO DE REGIMEN*****************************************************************
			String consulta = "SELECT requiere_deudor FROM tipos_regimen WHERE acronimo = '"+campos.get("codigoTipoRegimen")+"'";
			String requeridoDeudor = ConstantesBD.acronimoNo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				requeridoDeudor = rs.getString("requiere_deudor");
			//**************************************************************************************************
			//***********VERIFICACION DE TIPOS DE IDENTIFICACION*****************************************
			String exceptoDeudor = ConstantesBD.acronimoSi;
			consulta = "SELECT excento_deudor FROM tipos_id_institucion WHERE acronimo = '"+campos.get("codigoTipoIdentificacion")+"' and institucion = "+campos.get("institucion");
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				exceptoDeudor = rs.getString("excento_deudor");
			
			if((campos.get("codigoTipoRegimen").toString().equals(ConstantesBD.codigoTipoRegimenParticular+"")||UtilidadTexto.getBoolean(requeridoDeudor))&&!UtilidadTexto.getBoolean(exceptoDeudor))
				esRequerido = true;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en esRequeridoDeudor: "+e);
		}
		return esRequerido;
	}
	
	/**
	 * Método que consulta las habitaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerHabitaciones(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT " +
				"h.codigo," +
				"h.codigo_habitac," +
				"h.piso," +
				"h.nombre," +
				"th.nombre AS tipo_habitacion " +
				"FROM habitaciones h " +
				"INNER JOIN tipo_habitacion th ON(th.codigo=h.tipo_habitacion AND th.institucion=h.institucion) " +
				"WHERE " +
				"h.institucion = "+campos.get("institucion")+" AND " +
				"h.centro_atencion = "+campos.get("centroAtencion");
			if(Utilidades.convertirAEntero(campos.get("piso").toString())>0)
				consulta += " AND h.piso = "+campos.get("piso");
			
			consulta += " ORDER BY nombre ";
			
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("codigoHabitacion",rs.getObject("codigo_habitac"));
				elemento.put("codigoPiso",rs.getObject("piso"));
				elemento.put("nombre",rs.getObject("nombre"));
				elemento.put("tipoHabitacion",rs.getObject("tipo_habitacion"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerHabitaciones: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return resultados;
	}
	
	/**
	 * Método para consultar los tipos de habitaciones
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposHabitaciones(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo,institucion,nombre FROM tipo_habitacion WHERE institucion = "+campos.get("institucion")+" order by nombre";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("institucion", rs.getObject("institucion"));
				elemento.put("nombre", rs.getObject("nombre"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("error en obtenerTiposHabitaciones: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que consulta el tipo paciente de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static InfoDatosString obtenerTipoPacienteCuenta(Connection con,HashMap campos)
	{
		InfoDatosString resultado = new InfoDatosString();
		PreparedStatement pst =null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT " +
				"tipo_paciente AS codigo," +
				"getnombretipopaciente(tipo_paciente) AS descripcion " +
				"from cuentas WHERE id = "+campos.get("idCuenta");
			
			pst = con.prepareStatement(consulta);
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				resultado.setAcronimo(rs.getString("codigo"));
				resultado.setNombre(rs.getString("descripcion"));
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerTipoPacienteCuenta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerTipoPacienteCuenta", e);
		}
		finally{
			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return resultado;
	}
	
	
	
	/**
	 * Método que consulta el tipo paciente de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static InfoDatosString obtenerTipoPacienteSolicitud(Connection con,HashMap campos) throws BDException
	{
		InfoDatosString resultado = new InfoDatosString();
		PreparedStatement pst=null;
		ResultSet rs=null;
		
		try	{
			Log4JManager.info("############## Inicio obtenerTipoPacienteSolicitud");
			String consulta ="select " +
									"c.tipo_paciente as codigo, " +
									"getnombretipopaciente(c.tipo_paciente) as descripcion " +
								"from " +
									"cuentas c " +
								"inner join " +
									"solicitudes s on c.id=s.cuenta"+
								" WHERE s.numero_solicitud = "+campos.get("nSolicitud");
			
			pst = con.prepareStatement(consulta);
			rs = pst.executeQuery();
			
			if(rs.next())
			{
				resultado.setAcronimo(rs.getString("codigo"));
				resultado.setNombre(rs.getString("descripcion"));
			}
		}
		catch(SQLException sqe){
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		Log4JManager.info("############## Fin obtenerTipoPacienteSolicitud");
		
		return resultado;
	}
	
	
	
	
	/**
	 * Método que consulta el id de la cuenta de un paciente que pertenezca a un ingreso abierto,
	 * una via de ingreso especifica, un centro de atencion especifico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String getIdCuentaIngresoAbiertoPaciente(Connection con,HashMap campos)
	{
		try
		{
			String idCuenta = "";
			String consulta = "SELECT "+ 
				"c.id AS id_cuenta "+ 
				"FROM ingresos i "+ 
				"INNER JOIN cuentas c ON(c.id_ingreso=i.id) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
				"WHERE "+ 
				"i.codigo_paciente = "+campos.get("codigoPaciente")+" AND "+ 
				"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' AND "+ 
				"c.via_ingreso = "+campos.get("codigoViaIngreso")+" AND "+ 
				"cc.centro_atencion = "+campos.get("codigoCentroAtencion");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				idCuenta = rs.getString("id_cuenta");
			
			return idCuenta;
		}
		catch(SQLException e)
		{
			logger.error("Error en getIdCuentaIngresoAbiertoPaciente: "+e);
			return "";
		}
	}
	
	/**
	 * Método que consulta el centro de atencion de la cuenta del ingreso abierto del paciente.
	 * 
	 * Nota* Se envian como parámetros el codgio del pacietne y las vias de ingreso que se desean filtrar
	 * se pasan separadas por comas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int getCentroAtencionCuentaIngresoAbiertoPaciente(Connection con,HashMap campos)
	{
		try	
		{
			int codigoCentroAtencion = 0;
			String consulta = "SELECT "+ 
				"cc.centro_atencion AS codigo_centro_atencion "+ 
				"FROM ingresos i "+ 
				"INNER JOIN cuentas c ON(c.id_ingreso=i.id) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+ 
				"WHERE "+ 
				"i.codigo_paciente = "+campos.get("codigoPaciente")+" AND "+ 
				"i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' AND "+ 
				"c.via_ingreso IN ("+campos.get("viasIngreso")+") "+ 
				"ORDER BY c.id DESC ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				codigoCentroAtencion = rs.getInt("codigo_centro_atencion");
			
			return codigoCentroAtencion;
				
		}
		catch(SQLException e)
		{
			logger.info("Error en getCentroAtencionCuentaIngresoAbiertoPaciente: "+e);
			return -1;
		}
	}
	
	/**
	 * Método que realiza la isnerción del log de cambio de estado de camas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarLogCambioEstadoCama(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst  = null;
		try
		{
			String consulta = "INSERT INTO log_estados_cama " +
				"(codigo,cama,estado_original,estado_nuevo,fecha,hora,usuario,cuenta,fecha_grabacion,hora_grabacion,institucion,centro_atencion) " +
				"VALUES " +
				"("+campos.get("secuencia")+",?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoCama").toString()));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoEstadoOriginal").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("codigoEstadoNuevo").toString()));
			pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fecha").toString())));
			pst.setString(5,campos.get("hora").toString());
			pst.setString(6,campos.get("usuario").toString());
			if(!campos.get("idCuenta").toString().equals(""))
				pst.setInt(7,Utilidades.convertirAEntero(campos.get("idCuenta").toString()));
			else
				pst.setNull(7,Types.INTEGER);
			pst.setInt(8,Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString()));
			pst.setInt(9,Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString()));
			logger.info("PASÓ POR AQUI!!!!!");
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarLogCambioEstadoCama: "+e);
			return 0;
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
		}
	}
	
	/**
	 * Método que consulta las camas por habitación
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerCamasXHabitacion(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = " SELECT codigo,numero_cama,descripcion from camas1 WHERE habitacion = "+campos.get("codigoHabitacion")+" ORDER BY descripcion";
			
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				
				elemento.put("codigo",rs.getString("codigo"));
				elemento.put("numeroCama",rs.getString("numero_cama"));
				elemento.put("descripcion",rs.getString("descripcion"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCamasXHabitacion: "+e);
			
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para consultar las entidades subcontratadas parametrizadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEntidadesSubcontratadas(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT ent.codigo_pk,ent.codigo,ent.institucion,ent.razon_social, ent.activo " +
				"FROM entidades_subcontratadas ent";
			
			//Verifica la existencia de fecha para ser evaluada entre las vigencias
			if(campos.containsKey("fecha")
					&& campos.get("fecha").equals(""))			
				consulta+=" INNER JOIN det_entidades_subcontratadas det ON (det.codigo_ent_sub = ent.codigo_pk AND ('"+campos.get("fecha").toString()+" BETWEEN det.fecha_inicial AND det.fecha_final)) ";
			
			consulta+=" WHERE ent.institucion = "+campos.get("codigoInstitucion")+" order by ent.razon_social ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("consecutivo", rs.getObject("codigo_pk"));
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("institucion", rs.getObject("institucion"));
				elemento.put("descripcion", rs.getObject("razon_social"));
				elemento.put("activo", rs.getObject("activo"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEntidadesSubcontratadas: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método implementado para consultar los usuarios que han registrado ingresos en la estructura de Pacientes entidades subcontratadas 
	 * 
	 * Key's login,nombre
	 * @param con
	 * @param HashMap campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerUsuariosEntidadesSubcontratadas(Connection con,HashMap campos)	
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT pac.usuario_modifica AS login, getnombreusuario2(pac.usuario_modifica) AS nombre " +
				"FROM pac_entidades_subcontratadas pac WHERE pac.institucion = "+campos.get("institucion").toString()+" GROUP BY login, nombre ORDER BY nombre ASC ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("login", rs.getObject("login"));
				elemento.put("nombre", rs.getObject("nombre"));				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerUsuariosEntidadesSubcontratadas: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método uqe consultar el valor de un parámetro general de los planos entidades subcontratadas 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String getValorParametroGeneralPlanosEntidadSubcontratada(Connection con,HashMap campos)
	{
		String resultado = "";
		try
		{
			String consulta = "SELECT "+
				"coalesce(valor,'') AS valor "+ 
				"FROM det_param_entid_subcontratada "+ 
				"WHERE codigo_param = "+campos.get("seccion")+" and nombre = '"+campos.get("campo")+"' and institucion = "+campos.get("codigoInstitucion")+" ";
			
			if(Utilidades.convertirAEntero(campos.get("codigoConvenio").toString(),true)>0)
				consulta += " and convenio ="+campos.get("codigoConvenio")+" ";
			
			if(Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString(),true)>0)
				consulta += " and via_ingreso ="+campos.get("codigoViaIngreso")+" ";
			
			if(Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString(),true)>0)
				consulta += " and centro_atencion ="+campos.get("codigoCentroAtencion")+" ";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				resultado = rs.getString("valor");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getValorParametroGeneralPlanoEntidadSubcontratada: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método implementado para obtener los constratos vigentes de un usuario capitado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerContratosVigentesUsuarioCapitado(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		Statement st = null;
		ResultSetDecorator rs =  null;
		try
		{
			String consulta = "SELECT "+  
				"c.codigo as codigo, "+
				"c.convenio as convenio, "+
				"c.fecha_inicial as fechainicial, "+
				"c.fecha_final as fechafinal, "+
				"c.numero_contrato as numerocontrato, "+ 
				"c.valor as valor, "+ 
				"c.acumulado as acumulado, "+
				"c.tipo_pago as tipopago, "+ 
				"c.upc as upc, "+ 
				"c.porcentaje_pyp as porcentajepyp, "+ 
				"c.contrato_secretaria as contratosecretaria, "+ 
				"c.porcentaje_upc as porcentajeupc, "+
				"uxc.CLASIFICACION_SOCIO_ECONOMICA AS codigoestratosocial," +
				"uxc.tipo_afiliado as tipoafiliado," +
				"uxc.NATURALEZA_PACIENTES as naturalezapaciente, "+
				"c.base as base, "+ 
				"c.paciente_paga_atencion as pacientepagaatencion " +
				"from usuario_x_convenio uxc "+ 
				"INNER JOIN contratos c ON(c.codigo=uxc.contrato) "+ 
				"where "+ 
				"uxc.persona = "+campos.get("codigoPaciente")+" AND "+ 
				"(to_char(current_date, 'YYYY-MM-DD') between to_char(uxc.fecha_inicial, 'YYYY-MM-DD') and to_char(uxc.fecha_final, 'YYYY-MM-DD')) AND "+ 
				"c.convenio="+campos.get("codigoConvenio")+" AND "+ 
				"c.numero_contrato is not null "+/*(and c.numero_contrato <> ''))*/  
				"order by c.numero_contrato desc";
			
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigo", rs.getString("codigo"));
				mapa.put("convenio", rs.getString("convenio"));
				mapa.put("fechainicial", rs.getString("fechainicial"));
				mapa.put("fechafinal", rs.getString("fechafinal"));
				mapa.put("numerocontrato", rs.getString("numerocontrato"));
				mapa.put("valor", rs.getString("valor"));
				mapa.put("acumulado", rs.getString("acumulado"));
				mapa.put("tipopago", rs.getString("tipopago"));
				mapa.put("naturalezapaciente", rs.getString("naturalezapaciente"));
				mapa.put("upc", rs.getString("upc"));
				mapa.put("porcentajepyp", rs.getString("porcentajepyp"));
				mapa.put("contratosecretaria", rs.getString("contratosecretaria"));
				mapa.put("porcentajeupc", rs.getString("porcentajeupc"));
				mapa.put("base", rs.getString("base"));
				mapa.put("codigoestratosocial", rs.getString("codigoestratosocial"));
				mapa.put("tipoafiliado", rs.getString("tipoafiliado"));
				mapa.put("pacientepagaatencion", rs.getString("pacientepagaatencion"));
				resultados.add(mapa);
			}
			logger.info("Convenios capitados "+consulta+" pac: "+campos.get("codigoPaciente")+" conv "+campos.get("codigoConvenio"));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerContratosVigentesUsuarioCapitado: "+e);
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return resultados;
	}
	
	/**
	 * Método para obtener la descripcion de un tipo cie
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerDescripcionTipoCie(Connection con,int codigo)
	{
		String descripcion = "";
		try
		{
			String consulta = "SELECT codigo_real AS nombre FROM tipos_cie WHERE codigo = "+codigo;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				descripcion = rs.getString("nombre");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDescripcionTipoCie: "+e);
		}
		return descripcion;
	}
	
	/**
	 * Adicionado por: Giovanny Arias
	 * 07/03/08
	 * Método para obtener la cama asignada a una admision segun el codigo de la cuenta
	 * @param con
	 * @param codigo cuenta
	 * @return codigo cama
	 */
	public static int obtenerCamaAdmision(Connection con,int codigo)
	{
		int cama = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = "SELECT cama AS cama FROM admisiones_hospi WHERE cuenta = "+codigo;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				cama = rs.getInt("cama");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCamaAdmision: "+e);
		}
		return cama;
	}
	
	/**
	 * Método para consultar los estados de la cuenta
	 * @param con
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerEstadosCuenta(Connection con)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT codigo,nombre from estados_cuenta";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("error en obtenerEstadosCuentas: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para obtener los constratos de un usuario capitado
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerContratosUsuarioCapitado(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			String consulta = "SELECT "+  
				"c.codigo as codigo, "+
				"c.convenio as convenio, "+
				"c.fecha_inicial as fechainicial, "+
				"c.fecha_final as fechafinal, "+
				"c.numero_contrato as numerocontrato, "+ 
				"c.valor as valor, "+ 
				"c.acumulado as acumulado, "+
				"c.tipo_pago as tipopago, "+ 
				"c.upc as upc, "+ 
				"c.porcentaje_pyp as porcentajepyp, "+ 
				"c.contrato_secretaria as contratosecretaria, "+ 
				"c.porcentaje_upc as porcentajeupc, "+
				"uxc.CLASIFICACION_SOCIO_ECONOMICA AS codigoestratosocial," +
				"uxc.tipo_afiliado as tipoafiliado," +
				"uxc.NATURALEZA_PACIENTES as naturalezapaciente, "+
				"c.base as base, "+ 
				"c.paciente_paga_atencion as pacientepagaatencion " +
				"from usuario_x_convenio uxc "+ 
				"INNER JOIN contratos c ON(c.codigo=uxc.contrato) "+ 
				"where "+ 
				"uxc.persona = "+campos.get("codigoPaciente")+" AND "+ 
				"c.convenio="+campos.get("codigoConvenio")+" AND "+ 
				"c.numero_contrato is not null "+/*(and c.numero_contrato <> ''))*/  
				"order by c.numero_contrato desc";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String, Object> mapa =new HashMap<String, Object>();
				mapa.put("codigo", rs.getString("codigo"));
				mapa.put("convenio", rs.getString("convenio"));
				mapa.put("fechainicial", rs.getString("fechainicial"));
				mapa.put("fechafinal", rs.getString("fechafinal"));
				mapa.put("numerocontrato", rs.getString("numerocontrato"));
				mapa.put("valor", rs.getString("valor"));
				mapa.put("acumulado", rs.getString("acumulado"));
				mapa.put("tipopago", rs.getString("tipopago"));
				mapa.put("naturalezapaciente", rs.getString("naturalezapaciente"));
				mapa.put("upc", rs.getString("upc"));
				mapa.put("porcentajepyp", rs.getString("porcentajepyp"));
				mapa.put("contratosecretaria", rs.getString("contratosecretaria"));
				mapa.put("porcentajeupc", rs.getString("porcentajeupc"));
				mapa.put("base", rs.getString("base"));
				mapa.put("codigoestratosocial", rs.getString("codigoestratosocial"));
				mapa.put("tipoafiliado", rs.getString("tipoafiliado"));
				mapa.put("pacientepagaatencion", rs.getString("pacientepagaatencion"));
				resultados.add(mapa);
			}
			logger.info("Convenios capitados "+consulta+" pac: "+campos.get("codigoPaciente")+" conv "+campos.get("codigoConvenio"));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerContratosVigentesUsuarioCapitado: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implmentado para listar los tipos de monitoreo
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposMonitoreo(Connection con,int codigoInstitucion)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT codigo,nombre,prioridad_cobro from tipo_monitoreo where institucion = ? order by nombre";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,codigoInstitucion);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("nombre", rs.getObject("nombre"));
				elemento.put("prioridad_cobro", rs.getObject("prioridad_cobro"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTiposMonitoreo: "+e);
		}finally{
			if(pst != null){ 
				try {
					pst.close();
				} catch (SQLException e2) {
					logger.error("Error al cerrar el recurso ", e2);
				}
			}

			if(rs!=null){
                       try{
                              rs.close();
                       }catch(SQLException sqlException){
                              logger.error("Error al cerrar el recurso cambio metodo ", sqlException);
                       }
            }
		}
		return resultados;
	}
	
	/**
	 * Método que retorna si un paciente tiene o no, un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return
	 */
	public static int tienePreingresoPendiente(Connection con,int codigoInstitucion, int codigoPaciente)
	{
		int codigoIngreso = ConstantesBD.codigoNuncaValido;
		String consulta = "SELECT id FROM manejopaciente.ingresos WHERE institucion=? AND codigo_paciente=? AND preingreso=?";
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs = null;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,codigoInstitucion);
			pst.setInt(2,codigoPaciente);
			pst.setString(3,ConstantesIntegridadDominio.acronimoEstadoPendiente);
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigoIngreso = rs.getInt("id");
		}
		catch(SQLException e)
		{
			logger.error("Error en tienePreingresoPendiente: "+e);
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return codigoIngreso;
	}
	
	/**
	 * Método que retorna si un ingreso tiene o no, un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean ingresoConPreingresoPendiente(Connection con,int codigoInstitucion, int codigoIngreso)
	{
		String consulta = "SELECT id FROM ingresos WHERE institucion=? AND id=? AND preingreso=?";
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs = null;
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,codigoInstitucion);
			pst.setInt(2,codigoIngreso);
			pst.setString(3,ConstantesIntegridadDominio.acronimoEstadoPendiente);
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en tienePreingresoPendiente: "+e);
		}finally{
			if(pst != null){ 
				try {
					pst.close();
				} catch (SQLException e2) {
					logger.error("Error al cerrar el recurso ", e2);
				}
			}

			if(rs!=null){
                       try{
                              rs.close();
                       }catch(SQLException sqlException){
                              logger.error("Error al cerrar el recurso cambio metodo ", sqlException);
                       }
            }
		}
		return false;
	}
	
	/**
	 * Método que retorna si un paciente tiene o no, un preingreso pendiente
	 * @param con
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return
	 */
	public static int tienePreingresoPendienteXCentroAtencion(Connection con,int codigoInstitucion, int centroAtencion, int codigoPaciente)
	{
		PreparedStatementDecorator pst= null;
		ResultSetDecorator rs = null;
		int codigoIngreso = ConstantesBD.codigoNuncaValido;
		String consulta = "SELECT id FROM manejopaciente.ingresos WHERE institucion=? AND centro_atencion=? AND codigo_paciente=? AND preingreso=?";
		try {
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,codigoInstitucion);
			pst.setInt(2,centroAtencion);
			pst.setInt(3,codigoPaciente);
			pst.setString(4,ConstantesIntegridadDominio.acronimoEstadoPendiente);
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				codigoIngreso = rs.getInt("id");
		}
		catch(SQLException e)
		{
			logger.error("Error en tienePreingresoPendiente: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return codigoIngreso;
	}
	
	/**
	 * Método implementado para obtener el origen de admision de una cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerOrigenAdmisionCuenta(Connection con,int codigoCuenta)
	{
		InfoDatosInt origen = new InfoDatosInt();
		try
		{
			String consulta = "SELECT origen_admision AS codigo_origen_admision, getnomorigenadmision(origen_admision) as nombre_origen_admision FROM cuentas WHERE id = "+codigoCuenta;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				origen.setCodigo(rs.getInt("codigo_origen_admision"));
				origen.setNombre(rs.getString("nombre_origen_admision"));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerOrigenAdmisionCuenta: "+e);
		}
		return origen;
	}
	
	/**
	 * @param con
	 * @param Tipo_BD 
	 * @param codigoInstitucio
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean actualizarEstadoPreingreso(Connection con, int ingreso, String estado, String usuario, int Tipo_BD)
	{
		try
		{
		String consulta="";
			switch(Tipo_BD)
			{
			case DaoFactory.ORACLE:
				if (estado.equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
					consulta = "UPDATE ingresos SET preingreso=?, fecha_preingreso_gen=CURRENT_DATE, hora_preingreso_gen=substr(to_char(sysdate , 'hh:mm'),1,5), usuario_preingreso_gen=?, reapertura_automatica='"+ConstantesBD.acronimoNo+"', estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' WHERE id=?";
				else if (estado.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
					consulta = "UPDATE ingresos SET preingreso=?, fecha_preingreso_pen=CURRENT_DATE, hora_preingreso_pen=substr(to_char(sysdate , 'hh:mm'),1,5), usuario_preingreso_pen=? WHERE id=?";
				break;
			case DaoFactory.POSTGRESQL:
				if (estado.equals(ConstantesIntegridadDominio.acronimoEstadoGenerado))
					consulta = "UPDATE ingresos SET preingreso=?, fecha_preingreso_gen=CURRENT_DATE, hora_preingreso_gen="+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_preingreso_gen=?, reapertura_automatica='"+ConstantesBD.acronimoNo+"', estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' WHERE id=?";
				else if (estado.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
					consulta = "UPDATE ingresos SET preingreso=?, fecha_preingreso_pen=CURRENT_DATE, hora_preingreso_pen="+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_preingreso_pen=? WHERE id=?";
				break;
				default:
					break;
			
			}
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,estado);
			pst.setString(2,usuario);
			pst.setInt(3,ingreso);
			pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarEstadoPreingreso: "+e);
			return false;
		}
		return true;
	}
	
	/**
	 * Método implementado para consultar si el ingreso tuvo un cierre manual
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean tieneIngresoManual(Connection con,int codigoIngreso)
	{
		PreparedStatementDecorator pst =  null;
		ResultSetDecorator rs =  null;
		try
		{
			String consulta = "SELECT id FROM ingresos WHERE id=? AND cierre_manual=?";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,codigoIngreso);
			pst.setString(2,ConstantesBD.acronimoSi);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				return true;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneIngresoManual: "+e);
		}finally{
			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return false;
	}
	
	/**
	 * Método implementado para consultar el reingreso de un ingreso; si no tiene retorna (-1)
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static int obtenerReingreso(Connection con, int ingreso){
		int reingreso = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "SELECT coalesce(reingreso,"+ConstantesBD.codigoNuncaValido+") as reingreso  FROM ingresos WHERE id=?";
			pst = con.prepareStatement(consulta);
			pst.setInt(1,ingreso);
			
			rs = pst.executeQuery();
			if (rs.next()){
				reingreso = rs.getInt("reingreso");
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerReingreso",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerReingreso", e);
		}
		finally{
			
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return reingreso;
	}
	
	/**
	 * Método implementado para obtener los ingresosCerradosPendientesXFacturar
	 * (si el ingreso se encuentra en estado cerrado con indicativo de cierre manual y cuenta en estado activa, asociada o facturacion parcial)
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static String ingresosCerradosPendientesXFacturar(Connection con, int paciente){
		String ingresos = "";
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT i.consecutivo as ingreso FROM ingresos i INNER JOIN cuentas c ON (c.id_ingreso = i.id) WHERE i.codigo_paciente=? AND i.cierre_manual='"+ConstantesBD.acronimoSi+"' AND c.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaAsociada+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+")";
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,paciente);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				ingresos += (ingresos.equals("")?"":",") + rs.getString("ingreso");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en ingresosCerradosPendientesXFacturar: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return ingresos;
	}
	
	/**
	 * Método para verificar si es requerido la verificación de derechos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean esRequeridaVerificacionDerechos(Connection  con,HashMap campos)
	{
		boolean requerido = false, requerido0 = false, requerido1 = false;
		try
		{
			
			
			//********Se toman los campos***********************************************
			int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
			String codigoTipoRegimen = campos.get("codigoTipoRegimen").toString();
			//***************************************************************************
			
			//*******Se consulta si es requerida la verificación de derechos x via ingreso***************
			String consulta = "SELECT verificacion_derechos FROM vias_ingreso WHERE codigo = "+codigoViaIngreso;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				requerido0 = UtilidadTexto.getBoolean(rs.getString("verificacion_derechos"));
			//********************************************************************************************
			//*******Se consulta si es requerida la verificación de derechos x tipo regimen***************
			consulta = "SELECT req_verific_derechos FROM tipos_regimen WHERE acronimo = '"+codigoTipoRegimen+"' ";
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			rs =  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				requerido1 = UtilidadTexto.getBoolean(rs.getString("req_verific_derechos"));
			//********************************************************************************************
			
			if(requerido0&&requerido1)
				requerido = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en esRequeridaVerificacionDerechos: "+e);
			requerido = false;
		}
		
		return requerido;
	}
	
	/**
	 * Método que verifica si existe verificación de derechos por la subcuenta
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean existeVerificacionDerechosSubcuenta(Connection con,String subCuenta)
	{
		boolean existe = false;
		try
		{
			String consulta = "SELECT CASE WHEN count(1) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS existe FROM verificaciones_derechos WHERE sub_cuenta = "+Utilidades.convertirALong(subCuenta);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				existe = UtilidadTexto.getBoolean(rs.getString("existe"));
		}
		catch(SQLException e)
		{
			logger.error("Error en existeVerificacionDerechosSubcuenta: "+e);
		}
		
		return existe;
	}
	
	/**
	 * Método paara verificar si existe un ingreso cuidado especial activo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean existeIngresoCuidadoEspecialActivo(Connection con,HashMap campos)
	{
		boolean existe = false;
		try
		{
			//***********SE TOMAN LOS CAMPOS****************************************
			String codigoIngreso = campos.get("codigoIngreso").toString();
			String indicador = campos.get("indicador").toString();
			//**********************************************************************
			String consulta = "SELECT count(1) as cuenta from ingresos_cuidados_especiales WHERE ingreso = "+codigoIngreso+" and estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
			if(!indicador.equals(""))
				consulta += " and indicativo = '"+indicador+"'";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				existe = rs.getInt("cuenta")>0?true:false;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeIngresoCuidadoEspecial: "+e);
		}
		return existe;
	}
	

	/**
	 * Método que verifica si el ingreso es de control postoperatorio
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static boolean esIngresoControlPostOperatorioCx(Connection con,String codigoIngreso)
	{
		
		boolean existe = false;
		try
		{
			String consulta = "SELECT coalesce(control_post_operatorio_cx,'') AS control_post_opera FROM ingresos WHERE id = "+codigoIngreso;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				existe = UtilidadTexto.getBoolean(rs.getString("control_post_opera"));
		}
		catch(SQLException e)
		{
			logger.error("Error en esIngresoControlPostOperatorioCx: "+e);
		}
		return existe;
	}
	
	/**
	 * Método implementado para obtener el codigo del tipo de monitoreo del ingreso a cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial(Connection con,HashMap campos)
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		try
		{
			int idIngreso = Utilidades.convertirAEntero(campos.get("idIngreso").toString());
			String consulta = "SELECT tipo_monitoreo from ingresos_cuidados_especiales where ingreso = ? AND estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,idIngreso);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				resultado = rs.getInt("tipo_monitoreo");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial: "+e);
			
		}
		return resultado;
	}
	
	/**
	 * Método que actualiza el tipo de monitoreo de un ingresi cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarTipoMonitoreoIngresoCuidadoEspecial(Connection con,HashMap campos)
	{
		int resultado = 0;
		try
		{
			String tipoMonitoreo = campos.get("codigoTipoMonitoreo").toString();
			String idIngreso = campos.get("idIngreso").toString();
			String usuario = campos.get("usuario").toString();
			String codigoCentroCosto = campos.get("codigoCentroCosto").toString();
			
			String consulta = "UPDATE ingresos_cuidados_especiales SET " +
				"tipo_monitoreo = "+tipoMonitoreo+", " +
				"fecha_modifica = CURRENT_DATE, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"centro_costo = "+codigoCentroCosto+" ," +
				"usuario_modifica = '"+usuario+"' " +
				"WHERE ingreso = "+idIngreso;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado = st.executeUpdate(consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarTipoMonitoreoIngresoCuidadoEspecial: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método implementado para reversar un ingreso cuidado especial
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean reversarIngresoCuidadoEspecial(Connection con,HashMap campos)
	{
		boolean exito = false;
		try
		{
			//************SE TOMAN LOS CAMPOS****************************
			String idIngreso = campos.get("idIngreso").toString();
			String usuario = campos.get("usuario").toString();
			//************************************************************
			
			String consulta = "select codigo FROM ingresos_cuidados_especiales WHERE ingreso = "+idIngreso+" order by codigo desc";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				int codigo = rs.getInt("codigo");
				consulta = "UPDATE ingresos_cuidados_especiales SET " +
					"estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' , " +
					"fecha_finaliza = ?, " +
					"hora_finaliza = ?, " +
					"usuario_finaliza = ?, " +
					"fecha_modifica = current_date, " +
					"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" , " +
					"usuario_modifica = ? where codigo = ?";
				
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setNull(1,Types.DATE);
				pst.setNull(2,Types.VARCHAR);
				pst.setNull(3,Types.VARCHAR);
				pst.setString(4,usuario);
				pst.setInt(5,codigo);
				
				if(pst.executeUpdate()>0)
					exito = true;
			}
			else
				//Si no se encontró es exitoso pues no había ingreso cuidado especial
				exito = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en reversarIngresoCuidadoEspecial: "+e);
		}
		
		return exito;
	}
	
	/**
	 * Método implementado para obtener los tipos de reporte
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerTiposReporte(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			//*****SE TOMAN LOS PARAMETROS********************************************
			int codigoFuncionalidad = Utilidades.convertirAEntero(campos.get("codigoFuncionalidad").toString());
			String parametrizable = campos.get("parametrizable").toString();
			//*************************************************************************
			
			
			String consulta = "SELECT codigo,nombre as descripcion,tipo_rango,unidad_medida,posicion FROM reportes_rangos_estadisticos ";
			String seccionWHERE = "";
			if(codigoFuncionalidad>0)
				seccionWHERE += " WHERE funcionalidad = "+codigoFuncionalidad;
			if(!parametrizable.equals(""))
				seccionWHERE += (seccionWHERE.equals("")?" WHERE ":" AND ") + " parametrizable = '"+parametrizable+"'";
			
			consulta += seccionWHERE + " ORDER BY descripcion";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getObject("codigo"));
				elemento.put("descripcion", rs.getObject("descripcion"));
				elemento.put("tipoRango", rs.getObject("tipo_rango"));
				elemento.put("unidadMedida", rs.getObject("unidad_medida"));
				elemento.put("posicion", rs.getObject("posicion"));
				resultados.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTiposReporte: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implementado para insertar el log de reportes
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarLogReportes(Connection con,HashMap campos)
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		try
		{
			//**********SE TOMAN LOS PARÁMETROS******************************
			int codigoTipoReporte = Utilidades.convertirAEntero(campos.get("codigoTipoReporte").toString());
			String loginUsuario = campos.get("loginUsuario").toString();
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			//****************************************************************
			
			String consulta = "INSERT INTO log_reportes (codigo_pk,reporte,usuario,fecha,hora,institucion) VALUES (?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_log_reportes");
			pst.setInt(1,consecutivo);
			pst.setInt(2,codigoTipoReporte);
			pst.setString(3,loginUsuario);
			pst.setInt(4,codigoInstitucion);
			
			resultado = pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarLogReportes: "+e);
			resultado = ConstantesBD.codigoNuncaValido;
		}
		return resultado;
	}
	
	/**
	 * Carga todas las cuentas del paciente
	 * @param Connection con
	 * @param String codigoPaciente
	 * */
	public static ArrayList cuentasPaciente(Connection con,HashMap parametros)
	{
		String cadena = getCuentasPacieteStr;
		
		cadena +=" WHERE c.codigo_paciente = "+parametros.get("codigoPaciente").toString();		
		cadena += " ORDER BY c.fecha_apertura DESC, c.hora_apertura DESC, c.id DESC ";
		
		ArrayList array = new ArrayList();
		CuentasPaciente cuenta;
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				cuenta = new CuentasPaciente();				
				cuenta.setCodigoViaIngreso(rs.getString("viaIngreso"));
				cuenta.setDescripcionViaIngreso(rs.getString("descripcionViaIngreso"));
				cuenta.setCodigoTipoPaciente(rs.getString("tipoPaciente"));
				cuenta.setDescripcionTipoPaciente(rs.getString("descripcionTipoPaciente"));
				cuenta.setCodigoCuenta(rs.getString("cuenta"));
				cuenta.setEstadoCuenta(rs.getString("estadoCuenta"));
				cuenta.setDescripcionEstadoCuenta(rs.getString("descripcionEstadoCuenta"));
				cuenta.setCodigoIngreso(rs.getInt("idIngreso")+"");
				
				array.add(cuenta);
			}			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return array;
	}	
	
	/**
	 * Inserta el log de las autorizaciones ingresadas al paciente
	 * @param Connection con
	 * @param parametros
	 * */
	public static boolean insertarLogAutorizacionIngresoEvento(
			Connection con,
			HashMap parametros)
	{
		String cadena = "INSERT INTO log_autorizacion_ing_even VALUES (?,?,?,?,?,?,?,?,?) ";
		PreparedStatementDecorator ps = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setString(1,parametros.get("autorizacion").toString());
			
			if(parametros.get("codigoPersona").toString().equals("") || Utilidades.convertirAEntero(parametros.get("codigoPersona").toString()) <=0)
				ps.setNull(2,Types.INTEGER);
			else
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPersona").toString()));
			
			ps.setString(3,parametros.get("numeroid").toString());
			ps.setString(4,parametros.get("tipoid").toString());
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setInt(6,Utilidades.convertirAEntero(parametros.get("funcionalidad").toString()));
			ps.setString(7,parametros.get("usuario").toString());
			ps.setDate(8,Date.valueOf(parametros.get("fecha").toString()));
			ps.setString(9,parametros.get("hora").toString());	
			
			if(ps.executeUpdate() > 0)
				return true;
		}
		catch(SQLException e)
		{
			logger.info("valor de los parametros >> "+parametros);
			e.printStackTrace();
		}
		finally
		{
			if(ps != null){ 
				try {
					ps.close();
				} catch (SQLException e2) {
					logger.error("Error al cerrar el recurso ", e2);
				}
			}
		}
		return false;
	}
	
	/**
	 * Método para obtener el identificador del centro de costo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static String obtenerIdentificadorCentroCosto(Connection con,int codigo)
	{
		String identificador = "";
		try
		{
			String consulta = "SELECT coalesce(identificador,'') as identificador from centros_costo where codigo = "+codigo;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				identificador = rs.getString("identificador");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerIdentificadorCentroCosto: "+e);
		}
		return identificador;
	}
	
	/**
	 * Método para obtener el número de carnet del ingreso del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static String obtenerNroCarnetIngresoPaciente(Connection con,int codigoIngreso)
	{
		String nroCarnet = "";
		Statement st = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT coalesce(nro_carnet,'') as nro_carnet " +
				"from sub_cuentas where ingreso = "+codigoIngreso+" and nro_prioridad = 1";
			st = con.createStatement();
			rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				nroCarnet = rs.getString("nro_carnet");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerNroCarnetIngresoPaciente: "+e);
		}
		finally
		{
			UtilidadBD.cerrarObjetosPersistencia(st, rs, null);
		}
		return nroCarnet;
	}
	
	/**
	 * Método para consultar el codigo paciente de un ingreso
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static int obtenerCodigoPacienteXIngreso(Connection con,int codigoIngreso)
	{
		int codigoPaciente = 0;
		try
		{
			String consulta = " SELECT codigo_paciente from ingresos where id = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoIngreso);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				codigoPaciente = rs.getInt("codigo_paciente");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoPacienteXIngreso: "+e);
		}
		return codigoPaciente;
	}
	
	
	/**
	 * Método que verifica si está el centro de costo asociado a un tipo de monitoreo específico
	 * @param con
	 * @param centroCosto
	 * @param tipoMonitoreo
	 * @return
	 */
	public static boolean estaCentroCostoEnTipoMonitoreo(Connection con,int centroCosto,int tipoMonitoreo)
	{
		boolean estaCentroCosto = false;
		try
		{
			String consulta = "SELECT count(1) as cuenta FROM centros_costo_x_tipo_m WHERE centro_costo = "+centroCosto+" AND tipo_monitoreo = "+tipoMonitoreo;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					estaCentroCosto = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en : "+e);
		}
		return estaCentroCosto;
	}
	
	/**
	 * Metodo encargado de consultar el codigo de interfaz segun la via de ingreso y el tipo de paciente.
	 * @param connection
	 * @param ingreso
	 * @return String
	 */
	public static String obtenerCodInterfazXViaIngresoTipoPac(Connection connection, int viaIngreso, String tipoPaciente) {
		String codInterfaz = "  ";
		try
		{
			String consulta = "SELECT coalesce(codigo_interfaz, '  ') AS codigo_interfaz FROM garantia_paciente WHERE codigo_via_ingreso="+viaIngreso+" AND acronimo_tipo_paciente='"+tipoPaciente+"'";
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				codInterfaz = rs.getString("codigo_interfaz");
		}
		catch(SQLException e)
		{
			logger.error("Error / obtenerCodInterfazXViaIngresoTipoPac / "+e);
		}
		return codInterfaz;
	}
	
	/**
	 * Metodo encargado de consultar el codigo de interfaz segun la cuenta.
	 * @param cuenta
	 * @return String
	 */
	public static String obtenerCodInterfazXCuenta(Connection connection, String cuenta) {
		String codInterfaz = "  ";
		try
		{
			String consulta = "SELECT coalesce(gp.codigo_interfaz, '  ') AS codigo_interfaz FROM cuentas c INNER JOIN garantia_paciente gp ON (gp.codigo_via_ingreso=c.via_ingreso AND  gp.acronimo_tipo_paciente=c.tipo_paciente) WHERE c.id="+cuenta;
			Statement st = connection.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				codInterfaz = rs.getString("codigo_interfaz");
		}
		catch(SQLException e)
		{
			logger.error("Error / obtenerCodInterfazXViaIngresoTipoPac / "+e);
		}
		return codInterfaz;
	}
	
	
	/**
	 * Método implementado para cargar los tipos de servicios solicitados
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String,Object>> obtenerTiposServicioSolicitados(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			//*************************SE TOMAN LOS PARÁMETROS*************************************+
			int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
			boolean activo = UtilidadTexto.getBoolean(campos.get("activo").toString());
			//****************************************************************************************
			
			String consulta = "SELECT codigo,nombre FROM tipos_ser_sol WHERE institucion = "+codigoInstitucion;
			
			if(activo)
			{
				consulta += " and activo = '"+ConstantesBD.acronimoSi+"'";
			}
			consulta += " ORDER BY nombre";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				resultados.add(elemento);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTiposServicioSolicitados: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método que consulta el tipo de cobertura de una sub cuenta
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public static InfoDatosInt obtenerTipoCoberturaSubCuenta(Connection con,String idSubCuenta)
	{
		InfoDatosInt coberturaSalud = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		
		try
		{
			String consulta = "SELECT " +
				"cs.codigo as codigo, " +
				"cs.nombre as nombre " +
				"from sub_cuentas sc " +
				"inner join coberturas_salud cs ON(cs.codigo = sc.tipo_cobertura) " +
				"WHERE " +
				"sc.sub_cuenta = "+idSubCuenta;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
			{
				coberturaSalud.setCodigo(rs.getInt("codigo"));
				coberturaSalud.setNombre(rs.getString("nombre"));
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoCoberturaSubCuenta: "+e);
		}
		return coberturaSalud;
	}
	
	
	/**
	 * Método para obtener las coberturas salud por tipo de regimen de una sub_cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCoberturasSaludXTipoRegimenSubCuenta(Connection con,HashMap campos)
	{
		
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			//******************SE TOMAN LOS PARÁMETROS****************************************************
			int idSubCuenta = Integer.parseInt(campos.get("idSubCuenta").toString());
			int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
			//**********************************************************************************************
			
			String consulta = "SELECT "+ 
				"cs.codigo as codigo, "+
				"cs.nombre as nombre "+  
				"from sub_cuentas sc "+ 
				"inner join convenios c on(c.codigo = sc.convenio) "+ 
				"inner join cober_salud_tipo_reg cstr ON(cstr.tipo_regimen = c.tipo_regimen AND cstr.activo = '"+ConstantesBD.acronimoSi+"' and cstr.institucion = ?) "+ 
				"INNER JOIN coberturas_salud cs ON(cs.codigo = cstr.cobertura) "+ 
				"WHERE sc.sub_cuenta = ?";
			
			logger.info("consulta >> "+consulta+" "+idSubCuenta);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoInstitucion);
			pst.setInt(2,idSubCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				resultados.add(elemento);
			}
			
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCoberturasSaludXTipoRegimenSubCuenta: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para obtener las coberturas de salud x el tipo de régimen
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCoberturasSaludXTipoRegimen(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			//******************SE TOMAN LOS PARÁMETROS****************************************************
			String codigoTipoRegimen = campos.get("codigoTipoRegimen").toString();
			int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
			//**********************************************************************************************
			
			String consulta = "SELECT "+ 
				"cs.codigo as codigo, "+
				"cs.nombre as nombre "+  
				"from cober_salud_tipo_reg cstr  "+ 
				"INNER JOIN coberturas_salud cs ON(cs.codigo = cstr.cobertura) "+ 
				"WHERE cstr.tipo_regimen = ? AND cstr.activo = '"+ConstantesBD.acronimoSi+"' and cstr.institucion = ? ";
			
			logger.info("consulta >> "+consulta+" "+codigoTipoRegimen+" "+codigoInstitucion);
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setString(1,codigoTipoRegimen);
			pst.setInt(2,codigoInstitucion);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo",rs.getObject("codigo"));
				elemento.put("nombre",rs.getObject("nombre"));
				resultados.add(elemento);
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCoberturasSaludXTipoRegimen: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return resultados;
	} 
	
	
	/**
	 * Obtiene los convenios por ingreso
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<HashMap<String,Object>> obtenerConveniosXIngreso(Connection con, HashMap parametros)
	{
		ArrayList<HashMap<String,Object>> array = new ArrayList<HashMap<String,Object>>();
		String cadena = strConveniosXIng;
		
		if(parametros.containsKey("codigoSubcuenta") && 
				Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()) > 0)
			cadena += " AND s.sub_cuenta = "+parametros.get("codigoSubcuenta").toString();
		
		logger.info("valor de la cadena >> "+cadena);
			
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("codigo",rs.getString("convenio"));
				mapa.put("descripcion",rs.getString("nombre"));
				mapa.put("sub_cuenta",rs.getString("sub_cuenta"));
				mapa.put("nro_prioridad",rs.getString("nro_prioridad"));
				array.add(mapa);
			}
			
			ps.close();
			rs.close();
		}
		catch (Exception e) {
			logger.error("Error en obtenerConveniosXIngreso "+parametros);
		}		
		
		return array;
	}
	
	/**
	 * obtiene los profesionales que ordenan solicitud/es
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<HashMap<String,Object>> obtenerProfSolicitaSolicitudes(Connection con, HashMap parametros)
	{
		ArrayList<HashMap<String,Object>> array = new ArrayList<HashMap<String,Object>>();
		
		String cadena = strProfesionalesSolicita;
		
		if(parametros.get("esDetCargos").toString().equals(ConstantesBD.acronimoSi))		
			cadena = cadena.replace("?"," SELECT d.solicitud FROM det_cargos d WHERE d.codigo_detalle_cargo IN ("+parametros.get("codigoEvaluar")+") ");	
		else		
			cadena = cadena.replace("?",parametros.get("codigoEvaluar").toString());			
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap datos = new HashMap();
				datos.put("codigo",rs.getString(1));
				datos.put("nombre",rs.getString(2));				
				array.add(datos);
			}
		}
		catch (Exception e) {
			logger.info("error en obtenerProfSolicitaSolicitudes >> "+e+" "+cadena);
		}
		
		return array;
	}
	
	/**
	 * Método para obtener los convenios del ingreso y su informacion de autorizacion asociada
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoAutorizacion> obtenerConveniosIngresoAutorizacionesAdmision(Connection con,HashMap<String,Object> campos)
	{
		ArrayList<DtoAutorizacion> arreglo = new ArrayList<DtoAutorizacion>();
		try
		{
			//**************SE TOMAN PARÁMETROS***********************************************
			int codigoIngreso = Utilidades.convertirAEntero(campos.get("codigoIngreso").toString());
			int codigoCuenta = Utilidades.convertirAEntero(campos.get("codigoCuenta").toString());
			boolean filtrarSinCuenta = UtilidadTexto.getBoolean(campos.get("filtrarSinCuenta").toString());
			//**********************************************************************************
			
			String consulta = "SELECT "+
				"c.nombre || ' (Contrato: ' || getnumerocontrato(sc.contrato) || ')' as nombre_convenio, " +
				"c.req_auto_serv_add as requiere_autorizacion, "+
				"CASE WHEN a.codigo IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' END AS tiene_autorizacion, "+
				"coalesce(a.codigo||'','') as codigo_autorizacion, "+
				"coalesce(da.estado,'') as estado "+ 
				"FROM " +
				"cuentas cue " +
				"INNER JOIN sub_cuentas sc ON(sc.ingreso=cue.id_ingreso) "+ 
				"INNER JOIN convenios c ON(c.codigo = sc.convenio) "+ 
				"LEFT OUTER JOIN autorizaciones a on(a.sub_cuenta = sc.sub_cuenta and a.tipo = '"+ConstantesIntegridadDominio.acronimoAdmision+"' and " +
				//Para saber si también se deben tomar auitorizaciones que aun no tienen cuenta asociada
					(filtrarSinCuenta?"(a.cuenta = cue.id or a.cuenta is null)":"a.cuenta = cue.id") +") "+ 
				"LEFT OUTER JOIN det_autorizaciones da ON(da.autorizacion = a.codigo and da.activo = '"+ConstantesBD.acronimoSi+"') "+ 
				"WHERE "+ 
				"cue.id_ingreso = ? and cue.id = ?";
			
			consulta += "ORDER BY a.codigo desc";
			
			PreparedStatementDecorator pst =new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoIngreso);
			pst.setInt(2,codigoCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoAutorizacion autorizacion = new DtoAutorizacion();
				autorizacion.setNombreConvenio(rs.getString("nombre_convenio"));
				autorizacion.setCodigoPK(rs.getString("codigo_autorizacion"));
				autorizacion.setRequiereAutorizacionConvenio(UtilidadTexto.getBoolean(rs.getString("requiere_autorizacion")));
				autorizacion.setTieneAutorizacionRegistrada(UtilidadTexto.getBoolean(rs.getString("tiene_autorizacion")));
				
				DtoDetAutorizacion detAutorizacion = new DtoDetAutorizacion();
				detAutorizacion.setEstadoSolDetAuto(rs.getString("estado"));
				
				autorizacion.getDetalle().add(detAutorizacion);
				
				//Se verifica que el convenio no se haya asociado
				boolean convenioRepetido = false;
				for(DtoAutorizacion elemento:arreglo)
				{
					if(elemento.getNombreConvenio().equals(autorizacion.getNombreConvenio())&&
						elemento.isTieneAutorizacionRegistrada()==autorizacion.isTieneAutorizacionRegistrada()&&
						(
								elemento.getDetalle().get(0).getEstadoSolDetAuto().equals(autorizacion.getDetalle().get(0).getEstadoSolDetAuto())
								||
								(!elemento.getDetalle().get(0).getEstadoSolDetAuto().equals(autorizacion.getDetalle().get(0).getEstadoSolDetAuto())
									&&
									!elemento.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoAutorizado)
									&&
									!autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoAutorizado))
						)
					)
					{
						convenioRepetido = true;
					}
				}
				
				if(!convenioRepetido)
				{
					arreglo.add(autorizacion);
				}
			}
			pst.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConveniosAutorizacionesAdmision: "+e);
		}
		return arreglo;
	}
	
	/**
	 * Método para obtener el estado de la autorizacion de admisión de una subcuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoAutorizacion> obtenerAutorizacionesAdmisionSubCuenta(Connection con,HashMap<String,Object> campos)
	{
		ArrayList<DtoAutorizacion> autorizaciones = new ArrayList<DtoAutorizacion>();
		try
		{
			//*************SE TOMAN PARÁMETROS ********************************************
			long idSubCuenta = Long.parseLong(campos.get("idSubCuenta").toString());
			//******************************************************************************
			
			String consulta = " SELECT " +
				"da.estado as estado, " +
				"coalesce(getnombreviaingresotipopac(a.cuenta),'') as nombre_via_ingreso," +
				"coalesce(a.cuenta||'','') as cuenta  " +
				"from autorizaciones a " +
				"inner join det_autorizaciones da on(da.autorizacion = a.codigo and da.activo = '"+ConstantesBD.acronimoSi+"') " +
				"WHERE " +
				"a.sub_cuenta = ? and a.tipo = '"+ConstantesIntegridadDominio.acronimoAdmision+"' order by a.codigo desc";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,idSubCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoAutorizacion autorizacion = new DtoAutorizacion();
				autorizacion.setNombreViaIngreso(rs.getString("nombre_via_ingreso"));
				autorizacion.setIdCuenta(rs.getString("cuenta"));
				DtoDetAutorizacion detAutorizacion = new DtoDetAutorizacion();
				detAutorizacion.setEstadoSolDetAuto(rs.getString("estado"));
				autorizacion.getDetalle().add(detAutorizacion);
				
				autorizaciones.add(autorizacion);
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEstadoAutorizacionAdmisionSubCuenta: "+e);
		}
		return autorizaciones;
	}
	
	
	/**
	 * Método para obtener el tipo de entidad del centro de costo que ejecuta
	 * @param con
	 * @param codigoCentroCosto
	 * @return
	 */
	public static String obtenerTipoEntidadEjecutaCentroCosto(Connection con,int codigoCentroCosto)
	{
		String tipoEntidad = "";
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "SELECT coalesce(tipo_entidad_ejecuta,'') as tipo_entidad from centros_costo where codigo = ?";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,codigoCentroCosto);
			
			logger.info("SQL / obtenerTipoEntidadEjecutaCentroCosto / "+consulta);
			logger.info("codigoCentroCosto "+codigoCentroCosto);
			
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				tipoEntidad = rs.getString("tipo_entidad");
			}
		
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTipoEntidadEjecutaCentroCosto: "+e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		return tipoEntidad;
	}
	
	/**
	 * Método para cargar la descripcion de la entidaad subcontratad de la autorizacion de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultarDescripcionEntidadSubcontratadaAutorizacionSolicitud(Connection con,String numeroSolicitud)
	{
		String nombreEntidadSubcontratada = "";
		try
		{
			String consulta = "SELECT getdescentitadsubcontratada(entidad_autorizada_sub) as nombre from autorizaciones_entidades_sub WHERE numero_solicitud = ? order by consecutivo desc";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroSolicitud));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				nombreEntidadSubcontratada = rs.getString("nombre");
			}
			pst.close();
			rs.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDescripcionEntidadSubcontratadaAutorizacionSolicitud: "+e);
		}
		return nombreEntidadSubcontratada;
	}
	
	/**
	 * Método para cargar los datos básicos de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static DtoCuentas consultarDatosCuenta(Connection con,BigDecimal idCuenta)
	{
		DtoCuentas cuenta = new DtoCuentas();
		try
		{
			String consulta = "SELECT " +
				"c.id as id_cuenta," +
				"c.id_ingreso as id_ingreso," +
				"c.tipo_paciente as codigo_tipo_paciente," +
				"c.via_ingreso as codigo_via_ingreso, " +
				"c.codigo_paciente  as codigo_paciente  " +
				"FROM cuentas c WHERE c.id = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1, idCuenta.intValue());
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta.setIdCuenta(rs.getString("id_cuenta"));
				cuenta.setIdIngreso(rs.getString("id_ingreso"));
				cuenta.setCodigoTipoPaciente(rs.getString("codigo_tipo_paciente"));
				cuenta.setCodigoViaIngreso(rs.getInt("codigo_via_ingreso"));
				cuenta.setCodigoPaciente(rs.getString("codigo_paciente"));
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDatosCuenta ",e);
		}
		
		return cuenta;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerAutorizacionIngresoXFactura(Connection con, String codigoFactura) {
		String autorizacion = "";
		try
		{
			String consulta = 	"SELECT " +
									"tabl.numeroautorizacion " +
								"FROM " +
									"(" +
										"SELECT " +
											"coalesce (respauto.numero_autorizacion||'','') as numeroautorizacion " +
										"FROM " +
											"facturas f " +
										"INNER JOIN " +
											"cuentas cu ON (cu.id=f.cuenta) " +
										"INNER JOIN " +
											"sub_cuentas subcuen ON (cu.id_ingreso=subcuen.ingreso AND subcuen.nro_prioridad = 1) " +
										"INNER JOIN " +
											"autorizaciones aut ON (aut.sub_cuenta = subcuen.sub_cuenta AND aut.tipo = '"+ConstantesIntegridadDominio.acronimoAdmision+"') " +
										"INNER JOIN " +
											"det_autorizaciones detaut ON (aut.codigo = detaut.autorizacion AND detaut.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' AND detaut.activo   = '"+ConstantesBD.acronimoSi+"') " +
										"INNER JOIN resp_autorizaciones respauto ON (respauto.det_autorizacion = detaut.codigo) " +
										"WHERE f.codigo = ? " +
									"UNION " +
										"SELECT " +
											"coalesce(verif.numero_verificacion,'') as numeroautorizacion " +
										"FROM " +
											"facturas f " +
										"INNER JOIN " +
											"cuentas cuent ON (cuent.id=f.cuenta) " +
										"INNER JOIN " +
											"sub_cuentas subc ON (subc.ingreso = cuent.id_ingreso) " +
										"INNER JOIN " +
											"verificaciones_derechos verif ON (verif.sub_cuenta = subc.sub_cuenta ) " +
										"WHERE " +
											"f.codigo = ? " +
									") tabl " +
								"ORDER BY tabl.numeroautorizacion";

			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoFactura));
			pst.setInt(2,Utilidades.convertirAEntero(codigoFactura));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				autorizacion = rs.getString("numeroautorizacion");
		
			pst.close();
			rs.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerAutorizacionIngreso: "+e);
		}
		return autorizacion;
	}
	
	/**
	 * Método para verificar si un paciente tiene un ingreso abierto sin importar a cual centro de atención hace parte
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean tienePacienteIngresoAbierto(Connection con,BigDecimal codigoPaciente)
	{
		boolean resultado = false;
		try
		{
			String consulta = "SELECT count(1) as cuenta from manejopaciente.ingresos WHERE codigo_paciente = ? and estado = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setBigDecimal(1, codigoPaciente);
			pst.setString(2,ConstantesIntegridadDominio.acronimoEstadoAbierto);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					resultado = true;
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en tienePacienteIngresoAbierto: ",e);
		}
		return resultado;
	}
	
	/**
	 * 
	 * Metodo para obtener El centro de atencion duenio del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static int obtenerCentroAtencionDuenioPaciente(Connection con, int codigoPaciente)
	{
		String consulta="select centro_atencion_duenio as centro from manejopaciente.pacientes where codigo_paciente=? ";
		int retorna=ConstantesBD.codigoNuncaValido;
		
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1, codigoPaciente);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				retorna= rs.getInt("centro");
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDatosCuenta ",e);
		}
		return retorna;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static DtoInfoAmparosReclamacion consultarInformacionAmparosReclamacion(Connection con, String ingreso) 
	{
		String cadenaSql = "SELECT " +
								"m.numero_registro AS registro_medico, " +
								"pm.primer_apellido AS primer_ape_med, " +
								"pm.segundo_apellido AS segundo_ape_med, " +
								"pm.primer_nombre AS primer_nom_med, " +
								"pm.segundo_nombre AS segundo_nom_med, " +
								"pm.tipo_identificacion AS tipo_id_med, " +
								"pm.numero_identificacion AS numero_id_med, " +
								"getDx(c.id, c.via_ingreso, 'dx_ppal') AS dx_ingreso, " +
								"getDx(c.id, c.via_ingreso, 'dx_rel_1') AS dx_ingreso_rel_1, " +
								"getDx(c.id, c.via_ingreso, 'dx_rel_2') AS dx_ingreso_rel_2, " +
								"getDx(c.id, c.via_ingreso, 'dx_egreso') AS dx_egreso, " +
								"getDx(c.id, c.via_ingreso, 'dx_egreso_rel_1') AS dx_egreso_rel_1, " +
								"getDx(c.id, c.via_ingreso, 'dx_egreso_rel_2') AS dx_egreso_rel_2 " +
							"FROM " +
								"ingresos i " +
							"INNER JOIN " +
								"cuentas c ON (c.id_ingreso = i.id) "+
							"LEFT OUTER JOIN " +
								"personas pm ON (pm.codigo = getcodigomedicotratante(c.id)) " +
							"LEFT OUTER JOIN " +
								"medicos m ON (m.codigo_medico = pm.codigo) " +
							"WHERE " +
								"i.id = " + ingreso + " "+
								"AND c.id = (SELECT MAX(cu.id) FROM cuentas cu WHERE cu.id_ingreso = "+ingreso+") ";
		
		DtoInfoAmparosReclamacion info=new DtoInfoAmparosReclamacion();
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,cadenaSql);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			DtoDiagnostico diag;
			String diagTemporal="";
			if(rs.next())
			{
				diag= new DtoDiagnostico();
				diagTemporal=rs.getString("dx_ingreso");
				if(!UtilidadTexto.isEmpty(diagTemporal) && diagTemporal.indexOf("-")>0)
				{
					String[] temporal=diagTemporal.split("-");
					diag.setAcronimoDiagnostico(temporal[0]);
					diag.setTipoCieDiagnostico(temporal[1]);
					diag.setDescripcionDiagnostico(obtenerDescripcionDiagnostico(con,diag.getAcronimoDiagnostico(),diag.getTipoCieDiagnostico()));
				}
				info.setDiagnosticoIngresoPrincipal(diag);
				
				diag= new DtoDiagnostico();
				diagTemporal=rs.getString("dx_egreso");
				if(!UtilidadTexto.isEmpty(diagTemporal) && diagTemporal.indexOf("-")>0)
				{
					String[] temporal=diagTemporal.split("-");
					diag.setAcronimoDiagnostico(temporal[0]);
					diag.setTipoCieDiagnostico(temporal[1]);
					diag.setDescripcionDiagnostico(obtenerDescripcionDiagnostico(con,diag.getAcronimoDiagnostico(),diag.getTipoCieDiagnostico()));
				}
				info.setDiagnosticoEgresoPrincipal(diag);

				diag= new DtoDiagnostico();
				diagTemporal=rs.getString("dx_ingreso_rel_1");
				if(!UtilidadTexto.isEmpty(diagTemporal) && diagTemporal.indexOf("-")>0)
				{
					String[] temporal=diagTemporal.split("-");
					diag.setAcronimoDiagnostico(temporal[0]);
					diag.setTipoCieDiagnostico(temporal[1]);
					diag.setDescripcionDiagnostico(obtenerDescripcionDiagnostico(con,diag.getAcronimoDiagnostico(),diag.getTipoCieDiagnostico()));
				}
				info.getDiagnosticosIngresoRelacionados().add(diag);

				diag= new DtoDiagnostico();
				diagTemporal=rs.getString("dx_ingreso_rel_2");
				if(!UtilidadTexto.isEmpty(diagTemporal) && diagTemporal.indexOf("-")>0)
				{
					String[] temporal=diagTemporal.split("-");
					diag.setAcronimoDiagnostico(temporal[0]);
					diag.setTipoCieDiagnostico(temporal[1]);
					diag.setDescripcionDiagnostico(obtenerDescripcionDiagnostico(con,diag.getAcronimoDiagnostico(),diag.getTipoCieDiagnostico()));
				}
				info.getDiagnosticosIngresoRelacionados().add(diag);

				diag= new DtoDiagnostico();
				diagTemporal=rs.getString("dx_egreso_rel_1");
				if(!UtilidadTexto.isEmpty(diagTemporal) && diagTemporal.indexOf("-")>0)
				{
					String[] temporal=diagTemporal.split("-");
					diag.setAcronimoDiagnostico(temporal[0]);
					diag.setTipoCieDiagnostico(temporal[1]);
					diag.setDescripcionDiagnostico(obtenerDescripcionDiagnostico(con,diag.getAcronimoDiagnostico(),diag.getTipoCieDiagnostico()));
				}
				info.getDiagnosticosEgresoRelacionados().add(diag);

				diag= new DtoDiagnostico();
				diagTemporal=rs.getString("dx_egreso_rel_2");
				if(!UtilidadTexto.isEmpty(diagTemporal) && diagTemporal.indexOf("-")>0)
				{
					String[] temporal=diagTemporal.split("-");
					diag.setAcronimoDiagnostico(temporal[0]);
					diag.setTipoCieDiagnostico(temporal[1]);
					diag.setDescripcionDiagnostico(obtenerDescripcionDiagnostico(con,diag.getAcronimoDiagnostico(),diag.getTipoCieDiagnostico()));
				}
				info.getDiagnosticosEgresoRelacionados().add(diag);
				
				info.setPrimerNombreMedicoTratante(rs.getString("primer_nom_med"));
				info.setPrimerApellidoMedicoTratante(rs.getString("primer_ape_med"));
				info.setSegundoNombreMedicoTratante(rs.getString("segundo_nom_med"));
				info.setSegundoApellidoMedicoTratante(rs.getString("segundo_ape_med"));
				info.setTipoIdMedicoTratante(rs.getString("tipo_id_med"));
				info.setNumeroIdMedicoTratante(rs.getString("numero_id_med"));
				info.setNumeroRegistroMedico(rs.getString("registro_medico"));

			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			Log4JManager.error("error",e);
		}
		
		return info;
	}
		
	/**
	 * 
	 * @param con
	 * @param acronimoDiagnostico
	 * @param tipoCieDiagnostico
	 * @return
	 */
	private static String obtenerDescripcionDiagnostico(Connection con,String acronimoDiagnostico, String tipoCieDiagnostico) 
	{
		String consulta="select nombre from diagnosticos where acronimo='"+acronimoDiagnostico+"' and tipo_cie="+tipoCieDiagnostico;
		String nombre="";
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			DtoDiagnostico diag;
			String diagTemporal="";
			if(rs.next())
			{
				nombre=rs.getString(1);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			Log4JManager.error("error",e);
		}
		return nombre;
	}
	
	/**
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoIngresos> consultarInformacionAmparosReclamacion(int codigoPaciente) 
	{
		ArrayList<DtoIngresos> resultado=new ArrayList<DtoIngresos>();
		String consulta=" SELECT  " +
								" id as idingreso," +
								" consecutivo as consecutivoingreso," +
								" to_char(fecha_ingreso,'dd/mm/yyyy') as fechaingreso, " +
								" hora_ingreso as horaingreso," +
								" i.estado as estadoingreso " +
						" from ingresos i " +
						" inner join notas_administrativas na on na.ingreso=i.id " +
						" where codigo_paciente="+codigoPaciente+" group by id,consecutivo,fecha_ingreso,hora_ingreso,estado order by i.id  ";
		
		Connection con=UtilidadBD.abrirConexion();
		
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoIngresos dto=new DtoIngresos();
				dto.setIngreso(rs.getInt("idingreso"));
				dto.setConsecutivoIngreso(rs.getString("consecutivoingreso"));
				dto.setFechaIngreso(rs.getString("fechaingreso"));
				dto.setHoraIngreso(rs.getString("horaingreso"));
				dto.setEstadoIngreso(rs.getString("estadoingreso"));
				String subConsulta="select " +
											" c.tipo_paciente as tipopaciente," +
											" tp.nombre as nombretipopaciente," +
											" c.via_ingreso as viaingreso," +
											" vi.nombre as nombreviaingreso," +
											" c.area as codigoarea," +
											" cc.nombre as nombrearea " +
								" from manejopaciente.cuentas c " +
								" inner join vias_ingreso vi on c.via_ingreso=vi.codigo " +
								" inner join tipos_paciente tp on tp.acronimo=c.tipo_paciente " +
								" inner join centros_costo cc on (cc.codigo=c.area) " +
								" where id_ingreso="+dto.getIngreso()+
								" order by c.id desc";
				PreparedStatementDecorator psInterno=new PreparedStatementDecorator(con,subConsulta);
				ResultSetDecorator rsInterno=new ResultSetDecorator(psInterno.executeQuery());
				if(rsInterno.next())
				{
					dto.setViaIngreso(rsInterno.getInt("viaingreso"));
					dto.setNombreViaIngreso(rsInterno.getString("nombreviaingreso"));
					dto.setTipoPaciente(rsInterno.getString("tipopaciente"));
					dto.setNombreTipoPaciente(rsInterno.getString("nombretipopaciente"));
					dto.setCodigoAreaIngreso(rsInterno.getInt("codigoarea"));
					dto.setNombreAreaIngreso(rsInterno.getString("nombrearea"));
				}
				rsInterno.close();
				psInterno.close();
				resultado.add(dto);
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			logger.info("error en consulta",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoIngresos> consultarIngresosRegistrosEventosYAccidentes(int codigoPaciente) 
	{
		ArrayList<DtoIngresos> resultado=new ArrayList<DtoIngresos>();
		String consulta=" SELECT  " +
								" id as idingreso," +
								" i.consecutivo as consecutivoingreso," +
								" to_char(i.fecha_ingreso,'dd/mm/yyyy') as fechaingreso, " +
								" i.hora_ingreso as horaingreso," +
								" i.estado as estadoingreso," +
								" i.centro_atencion as codcentroatencion," +
								" ca.descripcion as desccentroatencion," +
								" case when  (irec.codigo_registro is null and ira.codigo_registro is not null) then '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' else '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' end as tipoevento," +
								" case when  (irec.codigo_registro is null and ira.codigo_registro is not null) then 'Acc Trans.' else 'Eve Catas.' end as desctipoevento," +
								" case when  (irec.codigo_registro is null and ira.codigo_registro is not null) then rat.estado else rec.estado end as estadoevento," +
								" coalesce(irec.codigo_registro,ira.codigo_registro) as codigoevento " +
						" from ingresos i " +
						" inner join centro_atencion ca on i.centro_atencion=ca.consecutivo " +
						" left outer join ingresos_registro_accidentes ira on ira.ingreso=i.id " +
						" left outer join manejopaciente.registro_accidentes_transito rat on rat.codigo=ira.codigo_registro " +
						" left outer join ingresos_reg_even_catastrofico irec on irec.ingreso=i.id " +
						" left outer join manejopaciente.registro_evento_catastrofico rec on rec.codigo=irec.codigo_registro" +
						" where codigo_paciente="+codigoPaciente+" and (irec.codigo_registro is not null or ira.codigo_registro is not null) group by i.id,i.consecutivo,i.fecha_ingreso,i.hora_ingreso,i.estado,i.centro_atencion,ca.descripcion,irec.codigo_registro,ira.codigo_registro,rat.estado,rec.estado order by i.id  ";
		
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			Log4JManager.info("consulta-->"+consulta);
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoIngresos dto=new DtoIngresos();
				dto.setIngreso(rs.getInt("idingreso"));
				dto.setConsecutivoIngreso(rs.getString("consecutivoingreso"));
				dto.setFechaIngreso(rs.getString("fechaingreso"));
				dto.setHoraIngreso(rs.getString("horaingreso"));
				dto.setEstadoIngreso(rs.getString("estadoingreso"));
				dto.setCodigoCentroAtencion(rs.getInt("codcentroatencion"));
				dto.setDescCentroAtencion(rs.getString("desccentroatencion"));
				dto.setTipoEvento(rs.getString("tipoevento"));
				dto.setDescTipoEvento(rs.getString("desctipoevento"));
				dto.setCodigoEvento(rs.getString("codigoevento"));
				dto.setEstadoEvento(rs.getString("estadoevento"));
				String subConsulta="select " +
											" c.tipo_paciente as tipopaciente," +
											" tp.nombre as nombretipopaciente," +
											" c.via_ingreso as viaingreso," +
											" vi.nombre as nombreviaingreso," +
											" c.area as codigoarea," +
											" cc.nombre as nombrearea " +
								" from manejopaciente.cuentas c " +
								" inner join vias_ingreso vi on c.via_ingreso=vi.codigo " +
								" inner join tipos_paciente tp on tp.acronimo=c.tipo_paciente " +
								" inner join centros_costo cc on (cc.codigo=c.area) " +
								" where id_ingreso="+dto.getIngreso()+
								" order by c.id desc";
				PreparedStatementDecorator psInterno=new PreparedStatementDecorator(con,subConsulta);
				ResultSetDecorator rsInterno=new ResultSetDecorator(psInterno.executeQuery());
				if(rsInterno.next())
				{
					dto.setViaIngreso(rsInterno.getInt("viaingreso"));
					dto.setNombreViaIngreso(rsInterno.getString("nombreviaingreso"));
					dto.setTipoPaciente(rsInterno.getString("tipopaciente"));
					dto.setNombreTipoPaciente(rsInterno.getString("nombretipopaciente"));
					dto.setCodigoAreaIngreso(rsInterno.getInt("codigoarea"));
					dto.setNombreAreaIngreso(rsInterno.getString("nombrearea"));
				}
				rsInterno.close();
				psInterno.close();
				resultado.add(dto);
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			logger.info("error en consulta",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public static ArrayList<DtoFactura> consultarFacturasIngresosConveniosAseguradoras(
			int ingreso) {
		ArrayList<DtoFactura> resultado=new ArrayList<DtoFactura>();
		String consulta="SELECT " +
							" f.codigo as codigo," +
							" f.consecutivo_factura as consfactura," +
							" to_char(f.fecha,'dd/mm/yyyy') as fecha," +
							" f.hora as hora," +
							" conv.codigo as codigoresponsable," +
							" conv.nombre as responsable," +
							" f.valor_convenio as valorconvenio " +
						" from facturas f " +
						" inner join cuentas c on f.cuenta=c.id " +
						" inner join convenios conv on f.convenio=conv.codigo " +
						" inner join tipos_convenio tc on (conv.tipo_convenio=tc.codigo and tc.aseg_at_ec='"+ConstantesBD.acronimoSi+"') " +
						" where c.id_ingreso="+ingreso+" and estado_facturacion<>"+ConstantesBD.codigoEstadoFacturacionAnulada+" order by f.codigo  ";
		
		Connection con=UtilidadBD.abrirConexion();
		
		try
		{
			Log4JManager.info("consulta-->"+consulta);
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoFactura dto=new DtoFactura();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setConsecutivoFactura(rs.getDouble("consfactura"));
				dto.setConsecutivoFacturaStr(rs.getString("consfactura"));
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setConvenio(new InfoDatosInt(rs.getInt("codigoresponsable"),rs.getString("responsable")));
				dto.setValorConvenio(rs.getDouble("valorconvenio"));
				resultado.add(dto);
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			logger.info("error en consulta",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public static String facturaTieneReclamacionRadicadaPrevia(int codigo) 
	{
		String resultado="";
		String consulta="SELECT " +
							" nro_radicado  " +
						" from manejopaciente.reclamaciones_acc_eve_fact " +
						" where " +
							" codigo_factura =  " +codigo+
							" and estado='"+ConstantesIntegridadDominio.acronimoRadicado+"' " +
						" order by codigo_pk desc";
		Log4JManager.info(consulta);
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				resultado=rs.getString(1);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			Log4JManager.error("error",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param ingreso
	 * @param tipoBd 
	 * @return
	 */
	public static DtoCertAtenMedicaFurips cargarCetificacionMedicaFurips(int ingreso, int tipoBd) 
	{
		DtoCertAtenMedicaFurips resultado=new DtoCertAtenMedicaFurips();
		String consultaCuentaVias="SELECT id,via_ingreso from cuentas where id_ingreso="+ingreso+" order by id asc";
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaCuentaVias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				//de la cuenta y la via inicial se toma la informacion de diagnosticos de ingreso
				int cuentaInicial=rs.getInt(1);
				int viaInicial=rs.getInt(2);
				
				resultado.setViaIngreso(viaInicial);
				
				rs.last();
				//de la cuenta y la via final se toma la informacion de diagnosticos de egreso y los datos del medico
				int cuentaFinal=rs.getInt(1);
				int viaFinal=rs.getInt(2);
				
				
				///evaluacion para urgencias
				if(viaInicial==ConstantesBD.codigoViaIngresoUrgencias)
				{
					String consultaDxIng="SELECT " +
												" vd.acronimo_diagnostico," +
												" vd.tipo_cie_diagnostico," +
												" med.numero_registro as registro," +
												" per.tipo_identificacion as tipoid," +
												" per.numero_identificacion as numeroid," +
												" per.primer_nombre as primernombre, " +
												" per.segundo_nombre as segundonombre, " +
												" per.primer_apellido as primerapellido," +
												" per.segundo_apellido as segundoapellido " +
										" from valoraciones_urgencias vu " +
										" inner join valoraciones v on vu.numero_solicitud=v.numero_solicitud " +
										" inner join  val_diagnosticos vd on vd.valoracion=v.numero_solicitud " +
										" inner join solicitudes sol on sol.numero_solicitud=v.numero_solicitud " +
										" inner join administracion.medicos med on v.codigo_medico=med.codigo_medico " +
										" inner join personas per on (per.codigo=med.codigo_medico) " +
										" where sol.cuenta="+cuentaInicial+" order by vd.numero asc ";
					PreparedStatementDecorator psDx=new PreparedStatementDecorator(con,consultaDxIng);
					ResultSetDecorator rsDx=new ResultSetDecorator(psDx.executeQuery());
					int cont=0;
					while (rsDx.next())
					{
						//dx principal
						if(cont==0)
						{
							resultado.setAcronimoDxIngreso(rsDx.getString(1));
							resultado.setTipoCieDxIngreso(rsDx.getInt(2));
							
							//se asigna la siguiente informacion en caso de que no tenga egreso la info del medico se toma de la val inicial
							resultado.setNumeroRegistroMedico(rsDx.getString("registro"));
							resultado.setTipoDocumentoMedico(rsDx.getString("tipoid"));
							resultado.setNumeroDocumentoMedico(rsDx.getString("numeroid"));
							resultado.setPrimerNombreMedico(rsDx.getString("primernombre"));
							resultado.setSegundoNombreMedico(rsDx.getString("segundonombre"));
							resultado.setPrimerApellidoMedico(rsDx.getString("primerapellido"));
							resultado.setSegundoApellidoMedico(rsDx.getString("segundoapellido"));
							
							cont++;
						}
						else if(cont==1)
						{
							resultado.setAcronimoDxRel1Ingreso(rsDx.getString(1));
							resultado.setTipoCieDxRel1Ingreso(rsDx.getInt(2));
							cont++;
						}
						else if(cont==2)
						{
							resultado.setAcronimoDxRel2Ingreso(rsDx.getString(1));
							resultado.setTipoCieDxRel2Ingreso(rsDx.getInt(2));
							cont++;
							break;
						}
					}
					rsDx.close();
					psDx.close();
				}
				///evaluacion para HOSPITALIZACION
				if(viaInicial==ConstantesBD.codigoViaIngresoHospitalizacion)
				{
					String consultaDxIng="SELECT " +
												" vd.acronimo_diagnostico," +
												" vd.tipo_cie_diagnostico," +
												" med.numero_registro as registro," +
												" per.tipo_identificacion as tipoid," +
												" per.numero_identificacion as numeroid," +
												" per.primer_nombre as primernombre, " +
												" per.segundo_nombre as segundonombre, " +
												" per.primer_apellido as primerapellido," +
												" per.segundo_apellido as segundoapellido " +
										" from val_hospitalizacion vh " +
										" inner join valoraciones v on vh.numero_solicitud=v.numero_solicitud " +
										" inner join  val_diagnosticos vd on vd.valoracion=v.numero_solicitud " +
										" inner join solicitudes sol on sol.numero_solicitud=v.numero_solicitud " +
										" inner join administracion.medicos med on v.codigo_medico=med.codigo_medico " +
										" inner join personas per on (per.codigo=med.codigo_medico) " +
										" where sol.cuenta="+cuentaInicial+" order by vd.numero asc  ";
					PreparedStatementDecorator psDx=new PreparedStatementDecorator(con,consultaDxIng);
					ResultSetDecorator rsDx=new ResultSetDecorator(psDx.executeQuery());
					int cont=0;
					while (rsDx.next())
					{
						//dx principal
						if(cont==0)
						{
							resultado.setAcronimoDxIngreso(rsDx.getString(1));
							resultado.setTipoCieDxIngreso(rsDx.getInt(2));
							
							//se asigna la siguiente informacion en caso de que no tenga egreso la info del medico se toma de la val inicial
							resultado.setNumeroRegistroMedico(rsDx.getString("registro"));
							resultado.setTipoDocumentoMedico(rsDx.getString("tipoid"));
							resultado.setNumeroDocumentoMedico(rsDx.getString("numeroid"));
							resultado.setPrimerNombreMedico(rsDx.getString("primernombre"));
							resultado.setSegundoNombreMedico(rsDx.getString("segundonombre"));
							resultado.setPrimerApellidoMedico(rsDx.getString("primerapellido"));
							resultado.setSegundoApellidoMedico(rsDx.getString("segundoapellido"));
							
							cont++;
						}
						else if(cont==1)
						{
							resultado.setAcronimoDxRel1Ingreso(rsDx.getString(1));
							resultado.setTipoCieDxRel1Ingreso(rsDx.getInt(2));
							cont++;
						}
						else if(cont==2)
						{
							resultado.setAcronimoDxRel2Ingreso(rsDx.getString(1));
							resultado.setTipoCieDxRel2Ingreso(rsDx.getInt(2));
							cont++;
							break;
						}
					}
					rsDx.close();
					psDx.close();
				}
				//se continua con los dx de egreso y la informacion del medico cuando la cuenta final es de urgencias.
				if(viaFinal==ConstantesBD.codigoViaIngresoUrgencias || viaFinal==ConstantesBD.codigoViaIngresoHospitalizacion)
				{
					String consultaDxEgre="SELECT " +
												" ed.acronimo_diagnostico," +
												" ed.tipo_cie_diagnostico," +
												" med.numero_registro as registro, " +
												" per.tipo_identificacion as tipoid," +
												" per.numero_identificacion as numeroid, " +
												" per.primer_nombre as primernombre, " +
												" per.segundo_nombre as segundonombre,  " +
												" per.primer_apellido as primerapellido, " +
												" per.segundo_apellido as segundoapellido  " +
										" from evoluciones evo  " +
										" inner join  evol_diagnosticos ed on ed.evolucion=evo.codigo  " +
										" inner join valoraciones v on evo.valoracion=v.numero_solicitud " +
										" inner join solicitudes sol on sol.numero_solicitud=v.numero_solicitud " +
										" inner join administracion.medicos med on evo.codigo_medico=med.codigo_medico  " +
										" inner join personas per on (per.codigo=med.codigo_medico) " +
										" where evo.orden_salida='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' and sol.cuenta="+cuentaFinal+" order by ed.numero asc ";
					int cont=0;
					PreparedStatementDecorator psDx=new PreparedStatementDecorator(con,consultaDxEgre);
					ResultSetDecorator rsDx=new ResultSetDecorator(psDx.executeQuery());
					while (rsDx.next())
					{
						//dx principal
						if(cont==0)
						{
							resultado.setAcronimoDxEgreso(rsDx.getString(1));
							resultado.setTipoCieDxEgreso(rsDx.getInt(2));
							resultado.setNumeroRegistroMedico(rsDx.getString("registro"));
							resultado.setTipoDocumentoMedico(rsDx.getString("tipoid"));
							resultado.setNumeroDocumentoMedico(rsDx.getString("numeroid"));
							resultado.setPrimerNombreMedico(rsDx.getString("primernombre"));
							resultado.setSegundoNombreMedico(rsDx.getString("segundonombre"));
							resultado.setPrimerApellidoMedico(rsDx.getString("primerapellido"));
							resultado.setSegundoApellidoMedico(rsDx.getString("segundoapellido"));
							cont++;
						}
						else if(cont==1)
						{
							resultado.setAcronimoDxRel1Egreso(rsDx.getString(1));
							resultado.setTipoCieDxRel1Egreso(rsDx.getInt(2));
							cont++;
						}
						else if(cont==2)
						{
							resultado.setAcronimoDxRel2Egreso(rsDx.getString(1));
							resultado.setTipoCieDxRel2Egreso(rsDx.getInt(2));
							cont++;
							break;
						}
					}
					if(viaFinal==ConstantesBD.codigoViaIngresoUrgencias && resultado.getAcronimoDxEgreso().isEmpty())
					{
						resultado.setAcronimoDxEgreso(resultado.getAcronimoDxIngreso());
						resultado.setTipoCieDxEgreso(resultado.getTipoCieDxIngreso());
						resultado.setAcronimoDxRel1Egreso(resultado.getAcronimoDxRel1Ingreso());
						resultado.setTipoCieDxRel1Egreso(resultado.getTipoCieDxRel1Ingreso());
						resultado.setAcronimoDxRel2Egreso(resultado.getAcronimoDxRel2Ingreso());
						resultado.setTipoCieDxRel2Egreso(resultado.getTipoCieDxRel2Ingreso());
					}
					rsDx.close();
					psDx.close();
				}

				if(viaInicial==ConstantesBD.codigoViaIngresoConsultaExterna)
				{
					String consultaIngresos="select " +
													" ingreso " +
											" from ingresos_registro_accidentes " +
											" where codigo_registro in (select codigo_registro from ingresos_registro_accidentes where ingreso="+ingreso+") " +
											" UNION " +
											" select " +
													" ingreso " +
											" from ingresos_reg_even_catastrofico " +
											" where codigo_registro in (select codigo_registro from ingresos_reg_even_catastrofico where ingreso="+ingreso+")";
					PreparedStatementDecorator psIngresos=new PreparedStatementDecorator(con,consultaIngresos);
					ResultSetDecorator rsIngresos=new ResultSetDecorator(psIngresos.executeQuery());
					String ingresosStr=ConstantesBD.codigoNuncaValido+"";
					while (rsIngresos.next())
					{
						ingresosStr=ingresosStr+","+rsIngresos.getInt(1);
					}
					rsIngresos.close();
					psIngresos.close();
					
					String consultaValoracion=	"SELECT  " +
													" max(v.numero_solicitud) " +
												" from valoraciones v " +
												" inner join solicitudes sol on sol.numero_solicitud=v.numero_solicitud " +
												" inner join cuentas c on sol.cuenta=c.id " +
												" where c.id_ingreso in ("+ingresosStr+")";
					
					PreparedStatementDecorator psVal=new PreparedStatementDecorator(con,consultaValoracion);
					ResultSetDecorator rsVal=new ResultSetDecorator(psVal.executeQuery());
					if (rsVal.next())
					{
						String consultaDx="SELECT  " +
													" vd.acronimo_diagnostico," +
													" vd.tipo_cie_diagnostico," +
													" med.numero_registro as registro," +
													" per.tipo_identificacion as tipoid," +
													" per.numero_identificacion as numeroid," +
													" per.primer_nombre as primernombre, " +
													" per.segundo_nombre as segundonombre, " +
													" per.primer_apellido as primerapellido," +
													" per.segundo_apellido as segundoapellido " +
										" from valoraciones v " +
										" inner join  val_diagnosticos vd on vd.valoracion=v.numero_solicitud " +
										" inner join administracion.medicos med on v.codigo_medico=med.codigo_medico " +
										" inner join personas per on (per.codigo=med.codigo_medico) " +
										" where v.numero_solicitud="+rsVal.getInt(1)+" order by vd.numero asc ";
						int cont=0;
						PreparedStatementDecorator psDx=new PreparedStatementDecorator(con,consultaDx);
						ResultSetDecorator rsDx=new ResultSetDecorator(psDx.executeQuery());
						while (rsDx.next())
						{
							//dx principal
							if(cont==0)
							{
								resultado.setAcronimoDxIngreso(rsDx.getString(1));
								resultado.setTipoCieDxIngreso(rsDx.getInt(2));
								resultado.setNumeroRegistroMedico(rsDx.getString("registro"));
								resultado.setTipoDocumentoMedico(rsDx.getString("tipoid"));
								resultado.setNumeroDocumentoMedico(rsDx.getString("numeroid"));
								resultado.setPrimerNombreMedico(rsDx.getString("primernombre"));
								resultado.setSegundoNombreMedico(rsDx.getString("segundonombre"));
								resultado.setPrimerApellidoMedico(rsDx.getString("primerapellido"));
								resultado.setSegundoApellidoMedico(rsDx.getString("segundoapellido"));
								cont++;
							}
							else if(cont==1)
							{
								resultado.setAcronimoDxRel1Ingreso(rsDx.getString(1));
								resultado.setTipoCieDxRel1Ingreso(rsDx.getInt(2));
								cont++;
							}
							else if(cont==2)
							{
								resultado.setAcronimoDxRel2Ingreso(rsDx.getString(1));
								resultado.setTipoCieDxRel2Ingreso(rsDx.getInt(2));
								cont++;
								break;
							}
						}
						
					}
					rsVal.close();
					psVal.close();
					
				}else{
					if(viaInicial==ConstantesBD.codigoViaIngresoAmbulatorios){
						StringBuffer consultarDxUltOrdenAmb=new StringBuffer("SELECT "); 
						consultarDxUltOrdenAmb.append("OA.FECHA, ")
								.append("OA.HORA, ") 
								.append("OA.ACRONIMO_DIAGNOSTICO as acronimo, ")
								.append("OA.TIPO_CIE_DIAGNOSTICO as dx, ")
								.append("med.numero_registro as registro , ")
								.append("PERMED.tipo_identificacion as tipoid, ")
								.append("PERMED.numero_identificacion as numeroid, ")
								.append("PERMED.primer_apellido as primerapellido, ")
								.append("PERMED.segundo_apellido as segundoapellido, ")
								.append("PERMED.primer_nombre as primernombre, ")
								.append("PERMED.segundo_nombre as segundonombre ")
							.append("FROM PERSONAS PER ")
								.append("INNER JOIN CUENTAS CU ")
								.append("ON CU.CODIGO_PACIENTE = PER.CODIGO ")
								.append("INNER JOIN ORDENES_AMBULATORIAS OA ")
								.append("ON OA.CUENTA_SOLICITANTE = CU.ID ")
								.append("INNER JOIN USUARIOS US ")
								.append("ON US.LOGIN = oa.usuario_solicita ")
								.append("INNER JOIN PERSONAS PERMED ")
								.append("ON PERMED.codigo=us.codigo_persona ")
								.append("INNER JOIN MEDICOS MED ")
								.append("ON med.codigo_medico= permed.codigo ")
								.append("INNER JOIN INGRESOS ING ")
								.append("ON cu.id_ingreso = ing.id ")
								.append("WHERE ing.id = ? ")
							.append("ORDER BY OA.FECHA,OA.HORA DESC ");
						if(tipoBd==DaoFactory.ORACLE){
							consultarDxUltOrdenAmb=new StringBuffer("SELECT * FROM (").append(consultarDxUltOrdenAmb).append(") WHERE ROWNUM<=1 ");
						}else{
							if(tipoBd==DaoFactory.POSTGRESQL){
								consultarDxUltOrdenAmb=new StringBuffer("SELECT * FROM (").append(consultarDxUltOrdenAmb).append(") resultado LIMIT 1 ");
							}
						}
						
						PreparedStatementDecorator psDx=new PreparedStatementDecorator(con,consultarDxUltOrdenAmb.toString());
						psDx.setInt(1, ingreso);
						
						ResultSetDecorator rsDx=new ResultSetDecorator(psDx.executeQuery());
						if (rsDx.next())
						{
							resultado.setAcronimoDxIngreso(rsDx.getString("acronimo"));
							resultado.setTipoCieDxIngreso(rsDx.getInt("dx"));
							resultado.setAcronimoDxEgreso(rsDx.getString("acronimo"));
							resultado.setTipoCieDxEgreso(rsDx.getInt("dx"));
							resultado.setNumeroRegistroMedico(rsDx.getString("registro"));
							resultado.setTipoDocumentoMedico(rsDx.getString("tipoid"));
							resultado.setNumeroDocumentoMedico(rsDx.getString("numeroid"));
							resultado.setPrimerNombreMedico(rsDx.getString("primernombre"));
							resultado.setSegundoNombreMedico(rsDx.getString("segundonombre"));
							resultado.setPrimerApellidoMedico(rsDx.getString("primerapellido"));
							resultado.setSegundoApellidoMedico(rsDx.getString("segundoapellido"));
								
						}
						
						rsDx.close();
						psDx.close();
					
					}
				}
				
				Log4JManager.info("id cuenta "+cuentaInicial+" via ingreso: "+viaInicial);
				Log4JManager.info("id cuenta "+cuentaFinal+" via ingreso: "+viaFinal);
				
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("error consultando las cuentas",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	/**
	 * 
	 */
	public static DtoCertAtenMedicaFurpro cargarCetificacionMedicaFurpro(int ingreso) 
	{
		DtoCertAtenMedicaFurpro resultado=new DtoCertAtenMedicaFurpro();
		String consultaCuentaVias="SELECT id,via_ingreso from cuentas where id_ingreso="+ingreso+" order by id asc";
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			PreparedStatement ps=con.prepareStatement(consultaCuentaVias, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				//de la cuenta y la via inicial se toma la informacion de diagnosticos de ingreso
				int cuentaInicial=rs.getInt(1);
				int viaInicial=rs.getInt(2);
				rs.last();
				//de la cuenta y la via final se toma la informacion de diagnosticos de egreso y los datos del medico
				int cuentaFinal=rs.getInt(1);
				int viaFinal=rs.getInt(2);
				
				
				//se continua con los dx de egreso y la informacion del medico cuando la cuenta final es de urgencias.
				if(viaFinal==ConstantesBD.codigoViaIngresoUrgencias || viaFinal==ConstantesBD.codigoViaIngresoHospitalizacion)
				{
					String consultaDxEgre="SELECT " +
												" ed.acronimo_diagnostico," +
												" ed.tipo_cie_diagnostico," +
												" med.numero_registro as registro, " +
												" per.tipo_identificacion as tipoid," +
												" per.numero_identificacion as numeroid, " +
												" per.primer_nombre as primernombre, " +
												" per.segundo_nombre as segundonombre,  " +
												" per.primer_apellido as primerapellido, " +
												" per.segundo_apellido as segundoapellido  " +
										" from evoluciones evo  " +
										" inner join  evol_diagnosticos ed on ed.evolucion=evo.codigo  " +
										" inner join valoraciones v on evo.valoracion=v.numero_solicitud " +
										" inner join solicitudes sol on sol.numero_solicitud=v.numero_solicitud " +
										" inner join administracion.medicos med on evo.codigo_medico=med.codigo_medico  " +
										" inner join personas per on (per.codigo=med.codigo_medico) " +
										" where evo.orden_salida='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' and sol.cuenta="+cuentaFinal+" order by ed.numero asc ";
					int cont=0;
					PreparedStatementDecorator psDx=new PreparedStatementDecorator(con,consultaDxEgre);
					ResultSetDecorator rsDx=new ResultSetDecorator(psDx.executeQuery());
					while (rsDx.next())
					{
						//dx principal
						if(cont==0)
						{
							resultado.setAcronimoDxEgreso(rsDx.getString(1));
							resultado.setTipoCieDxEgreso(rsDx.getInt(2));
							cont++;
						}
						else if(cont==1)
						{
							resultado.setAcronimoDxRel1Egreso(rsDx.getString(1));
							resultado.setTipoCieDxRel1Egreso(rsDx.getInt(2));
							cont++;
						}
						else if(cont==2)
						{
							resultado.setAcronimoDxRel2Egreso(rsDx.getString(1));
							resultado.setTipoCieDxRel2Egreso(rsDx.getInt(2));
							cont++;
						}
						else if(cont==3)
						{
							resultado.setAcronimoDxRel3Egreso(rsDx.getString(1));
							resultado.setTipoCieDxRel3Egreso(rsDx.getInt(2));
							cont++;
						}
						else if(cont==4)
						{
							resultado.setAcronimoDxRel4Egreso(rsDx.getString(1));
							resultado.setTipoCieDxRel4Egreso(rsDx.getInt(2));
							cont++;
							break;
						}
					}
					rsDx.close();
					psDx.close();
				}

				
				Log4JManager.info("id cuenta "+cuentaInicial+" via ingreso: "+viaInicial);
				Log4JManager.info("id cuenta "+cuentaFinal+" via ingreso: "+viaFinal);
				
			}
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			Log4JManager.error("error consultando las cuentas",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}
	
	

	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap<String, String> obtenerUltimosSignosVitalesRE(Connection con, int idCuenta) 
	{
		HashMap<String,String> mapa=new HashMap<String, String>();
		mapa.put("numRegistros", "0");
		String consultaCodigoRE="select max(ehre.codigo) from enca_histo_registro_enfer  ehre INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) where re.cuenta = "+idCuenta;
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consultaCodigoRE);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				String consultaSignos="SELECT " +
											" ehre.codigo AS codigo_histoenfer, " +
											" TO_CHAR(ehre.fecha_registro, 'dd/mm/yyyy') AS fecha_registro, " +
											" ehre.hora_registro  ||'' AS hora_registro,  " +
											" svre.fc   AS fc,  " +
											" svre.fr   AS fr,  " +
											" svre.pas  AS pas,  " +
											" svre.pad  AS pad,   " +
											" svre.pam  AS pam,   " +
											" svre.temp AS temp " +
										" FROM enca_histo_registro_enfer ehre " +
										" INNER JOIN registro_enfermeria re ON (ehre.registro_enfer=re.codigo) " +
										" INNER JOIN signo_vital_reg_enfer svre ON (ehre.codigo  =svre.codigo_histo_enfer) " +
										" WHERE ehre.codigo= "+rs.getInt(1);
				PreparedStatementDecorator psSignos=new PreparedStatementDecorator(con,consultaSignos);
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(psSignos.executeQuery()), false, true);
				psSignos.close();
			}
			rs.close();
			ps.close();
			
		}
		catch(Exception e)
		{
			Log4JManager.error("Error.",e);
		}
		return mapa;
	}
}