/*
 * @(#)SqlBaseDespachoMedicamentosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para un el despacho de medicamentos
 *
 * @version 1.0, Agosto 31 / 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */

public class SqlBaseDespachoMedicamentosDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseDespachoMedicamentosDao.class);


	/**
	 *  Información básica de las solicitudes de medicamentos 
	 */
	private final static String listadoSolicitudesStr = "SELECT " +
														"CASE WHEN sol.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN  'urgente' " +
														"ELSE 'NO' END  AS identificadorPrioridad, " +
														"to_char(sol.fecha_solicitud,'yyyy-mm-dd') AS fechaSolicitud, " +
														"sol.hora_solicitud AS horaSolicitud, " +
														"sol.numero_solicitud AS numeroSolicitud, " +
														"getnombrepersona(per.codigo) AS nombresPaciente, " +
														"per.codigo AS codigoPaciente,  " +
														"getnombrepersona(pers.codigo) AS medicoSolicitante, " +
														"cen.nombre AS centroCostoSolicitante, " +
														"sol.consecutivo_ordenes_medicas AS orden, " +
                                                        "sol.centro_costo_solicitado AS codigoCentroCostoSolicitado, " +
                                                        "cen1.nombre AS nombreCentroCostoSolicitado, " +
                                                        "esSolMedSinCant(sol.numero_solicitud) || '' AS sin_cant, " +
                                                        "CASE WHEN solm.orden_dieta IS NULL THEN '' ELSE solm.orden_dieta ||'' END AS mezclaOrdenDieta, " +
                                                        "CASE WHEN sol.pyp IS NULL THEN '' ELSE (CASE WHEN sol.pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE '' END) END AS esPyP, " +
                                                        "coalesce(getcamacuenta(cue.id, cue.via_ingreso), '') as infocama, " +
                                                        "oas.orden as ordenAmb " +
														"FROM ordenes.solicitudes sol  " +
														"INNER JOIN manejopaciente.cuentas cue ON (sol.cuenta=cue.id " +
														"AND cue.estado_cuenta in ( "+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+","+ConstantesBD.codigoEstadoCuentaAsociada+")) " +
														"INNER JOIN administracion.personas per ON (cue.codigo_paciente = per.codigo) " +
														"INNER JOIN administracion.personas pers ON (sol.codigo_medico=pers.codigo) " +
														"INNER JOIN administracion.centros_costo cen ON (sol.centro_costo_solicitante = cen.codigo ) " +
                                                        "INNER JOIN administracion.centros_costo cen1 ON (sol.centro_costo_solicitado = cen1.codigo ) " +
                                                        "INNER JOIN ordenes.solicitudes_medicamentos solm ON (solm.numero_solicitud=sol.numero_solicitud and pendiente_completar='"+ConstantesBD.acronimoNo+"') " +
                                                        "LEFT JOIN ordenes.ordenes_amb_solicitudes oas ON(oas.numero_solicitud = sol.numero_solicitud) "+
                                                        " WHERE " +
														" sol.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+" " ;
														
																			
	
	/**
	 *  Información de la creación de la solicitud
	 */
	//"sol.numero_autorizacion AS numeroAutorizacion, " +
	private final static String infoSolicitudStr = 	"SELECT " +
																	    "to_char(sol.fecha_solicitud,'yyyy-mm-dd') AS fechaSolicitud, " +
																	    "sol.hora_solicitud AS horaSolicitud, " +
																	    "sol.numero_solicitud AS numeroSolicitud, " +
																	    "sol.consecutivo_ordenes_medicas AS orden, " +
																	    "getnombrepersona(per.codigo) AS medicoSolicitante, " +
																	    "est.nombre AS estadoMedico, " +
																	    "sm.observaciones_generales AS observacionesGenerales " +
																	    "FROM solicitudes sol INNER JOIN cuentas cue ON (sol.cuenta=cue.id) " +
																	    "INNER JOIN personas per ON (sol.codigo_medico = per.codigo) " +
																	    "INNER JOIN estados_sol_his_cli est ON (est.codigo=sol.estado_historia_clinica) " +
																	    "INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud = sol.numero_solicitud) " +
																	    "WHERE sol.numero_solicitud = ? ";
	
	/**
	 * Elimina un sustituto con despacho total 0
	 */
	private final static String eliminarSustitutoConDespachoCeroStr=		"delete from detalle_despachos where " +
																										"articulo = ? " +
																										"and cantidad= 0 " +
																										"and despacho " +
																										"in " +
																										"(select orden from despacho where numero_solicitud = ?)";

	/**
	 * Actualiza el estado médico de la solicitud en (Administrada-Despachada) 
	 * y su correspondiente Número de Autorización
	 */
	//"numero_autorizacion=?  " +
	private final static String cambiarEstadoMedicoSolicitudYNumAutorizacionStr= 	"update solicitudes " +
																															"set estado_historia_clinica = ? " +
																															" where numero_solicitud=? "; 
	
	/**
	 * Obtiene el último cod de la sequence para poder hacer la inserción del DETALLE del despacho
	 */
	private final static String ultimaSequenciaDespachoStr= "SELECT MAX(orden) AS sequenciaDespacho FROM despacho ";
	
	/**
	 * Inserta el detalle del Despacho
	 */
	private final static String insercionDetalleDespachoStr= 	"INSERT INTO detalle_despachos " +
																	"(articulo, art_principal, despacho, cantidad, costo_unitario, lote, fecha_vencimiento, tipo_despacho, almacen_consignacion, proveedor_compra, proveedor_catalogo,saldos_dosis,costo_donacion) " +
																	"VALUES (?, ?, ?, ?, (SELECT costo_promedio FROM articulo WHERE codigo=?), ?, ?, ?, ?, ?, ? , ?, (SELECT costo_donacion FROM articulo WHERE codigo=?)) ";
	
	/**
	 *  Consulta los articulos (Principales - Sustitutos) que ya han sido insertados
	 *  para restringir la búsqueda en el tag
	 */
	private final static String articulosInsertadosStr= 	"SELECT " +
																				"dets.articulo " +
																				"from " +
																				"detalle_solicitudes dets " +
																				"where " +
																				"dets.numero_solicitud=?" +
																				"UNION " +
																				"select " +
																				"detd.articulo " +
																				"from detalle_despachos detd " +
																				"INNER JOIN despacho des ON (detd.despacho=des.orden) " +
																				"where des.numero_solicitud=? ";
	/**
	 * Consulta los Insumos que han sido insertados.
	 */
	private final static String articulosInsumosStr= "SELECT DISTINCT" +
															" ins.codigo," +
															" ins.codigointerfaz," +
															" ins.descripcion," +
															" ins.unidad_medida," +
															" ins.cantidad," +
															" CASE WHEN dets.numero_solicitud IS NOT NULL THEN "+ValoresPorDefecto.getValorTrueParaConsultas()+" ELSE "+ValoresPorDefecto.getValorFalseParaConsultas()+" END AS es_solicitado," +
															" dets.cantidad AS cantidad_solicitada "+
														" FROM "+	
															"( SELECT " +
																			"ds.articulo AS codigo, " +
																			"coalesce(a.codigo_interfaz, '') as codigointerfaz, " +
																			"a.descripcion, " +
																			"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidad_medida, " +
																			"inventarios.getInfoArticulos(ds.numero_solicitud,a.codigo) as cantidad  " +
																			"from " +
																			"detalle_solicitudes ds " +
																			"inner join articulo a on(a.codigo=ds.articulo) " +
																			"inner join naturaleza_articulo na on(a.naturaleza=na.acronimo and a.institucion=na.institucion)"+
																			"where ds.numero_solicitud=? " +
																			"and na.es_medicamento='"+ConstantesBD.acronimoNo+"' " +
																			"UNION " +
																			"SELECT " +
																			"dd.articulo AS codigo, " +
																			"coalesce(a.codigo_interfaz, '') as codigointerfaz, " +
																			"a.descripcion, " +
																			"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidad_medida, " +
																			"inventarios.getInfoArticulos(desp.numero_solicitud,a.codigo) as cantidad " +
																			"from " +
																			"detalle_despachos dd " +
																			"INNER JOIN despacho desp ON (desp.orden=dd.despacho) " +
																			"INNER JOIN articulo a ON (a.codigo=dd.articulo) " +
																			"inner join naturaleza_articulo na on(a.naturaleza=na.acronimo and a.institucion=na.institucion)"+
																			"where " +
																			"na.es_medicamento='"+ConstantesBD.acronimoNo+"' " +
																			"and desp.numero_solicitud=? )  ins "+
														"LEFT OUTER JOIN detalle_solicitudes dets ON(dets.articulo=ins.codigo AND dets.numero_solicitud=?)";
		
	
	
	
	
	/**
	 * Actualiza el estado Facturaciï¿½n de la solicitud en (Anulada) 
	 * cuando no se le genera cargo por que el despacho es igual a 0(cero)
	 */
	private final static String cambiarEstadoFacturacionSolicitudStr=" UPDATE det_cargos SET estado = ?  WHERE solicitud=? ";
	
	/**
	 * 
	 */
	private static String consultaStrNP="SELECT DISTINCT * FROM det_cargos dtc " +
																"INNER JOIN convenios c ON (dtc.convenio = c.codigo) " +
																"INNER JOIN articulo a ON (dtc.articulo = a.codigo) " + 
																"INNER JOIN naturaleza_articulo na ON (a.naturaleza = na.acronimo) " +
										"WHERE dtc.solicitud = ? AND na.es_pos = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' AND " +
												"a.codigo=?";
	
	private static String insertarJusPStr="INSERT INTO jus_pendiente_articulos VALUES (?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	
	/**
	 * Inserta la info básica del despacho
	 * @param con
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esDirecto
	 * @param numeroSolicitud
	 * @return
	 */
	public static int insertarDespachoBasico			(	Connection con, 
																				String  usuario, 
																				boolean esDirecto,
																				int numeroSolicitud,
																				String insercionDespachosStr,
																				String nombrePersonaRecibe) 
	{
		try
		{		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insercionDespachosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,usuario);
			ps.setBoolean(2,esDirecto);
			ps.setInt(3,numeroSolicitud);
			ps.setString(4, nombrePersonaRecibe);
			
			if (ps.executeUpdate()>0)
			{
				return cargarUltimoCodigoSequence(con);
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseDespachoMedicamentosDao "+e.toString() );
			return 0; 
		}
	}
	
	/**
	 * Consulta los articulos (Principales - Sustitutos) que ya han sido insertados
	 * para restringir la búsqueda en el tag
	 * @param con
	 * @return
	 */
	public static Collection articulosInsertados(Connection con, int numeroSolicitud)
	{
		Collection coleccion=new ArrayList();
		try
		{
			PreparedStatementDecorator cargarInsertadosStatement= new PreparedStatementDecorator(con.prepareStatement(articulosInsertadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarInsertadosStatement.setInt(1,numeroSolicitud);
			cargarInsertadosStatement.setInt(2,numeroSolicitud);
			ResultSetDecorator rs= new ResultSetDecorator(cargarInsertadosStatement.executeQuery());
			
			while(rs.next())
			{
				coleccion.add(rs.getString("articulo"));
			}
		}
		catch(Exception e)
		{
			logger.warn("Error en consulta de datos: SqlBaseDespachoMedicamentosDao" +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}

	
	/**
	 * Carga el ultimo despacho  insertado   (table= despacho))
	 * @param con
	 * @return
	 */
	public static int cargarUltimoCodigoSequence(Connection con)
	{
		int ultimoCodigoSequence=0;
		try
		{
			PreparedStatementDecorator cargarUltimoStatement= new PreparedStatementDecorator(con.prepareStatement(ultimaSequenciaDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(cargarUltimoStatement.executeQuery());
			if(rs.next())
			{
				ultimoCodigoSequence=rs.getInt("sequenciaDespacho");
				logger.info("ultimo codigo secuencias >>> "+ultimoCodigoSequence);
				return ultimoCodigoSequence;
			}
			else
			{
				return 0;
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del último código del despacho: SqlBaseDespachoMedicamentosDao "+e.toString());
			return 0;
		}
	}
	
	public static boolean validarNOPOS(Connection con, int numeroSolicitud, int codigoArticulo, boolean esMedicamento)
	{
		HashMap<String, Object> resultadosNP = new HashMap<String, Object>();
		try
		{
			String consultaNoPOS=consultaStrNP;
			if(esMedicamento){
				consultaNoPOS+= " AND c.requiere_justificacion_art = 'S' ";
			}else{
				consultaNoPOS+=	" AND c.req_just_art_nopos_dif_med = 'S' ";
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaNoPOS,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoArticulo);
		
			resultadosNP = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true, true);
			
			if(Integer.parseInt(resultadosNP.get("numRegistros").toString())>0)
			{
				logger.info("entrooo 11111111>>>>>>>>>>>>>>");
				return true;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean insertarJusP(Connection con, int numeroSolicitud, int codigoArticulo, String usuario)
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarJusPStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoArticulo);
			ps.setString(3, usuario);
			
			if(ps.executeUpdate()>0){
				logger.info("entrooo 333333333333>>>>>>>>>>>>>>");
				return true;
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Inserta el detalle del despacho
	 * @param con
	 * @param articulo
	 * @param articuloPrincipal
	 * @param cantidad
	 * @param saldoDosis 
	 * @return
	 */
	public static int insertarDetalleDespacho			(	Connection con, 
															int articulo, 
															int articuloPrincipal, 
															int cantidad,
															int codigoSequenciaDespacho,
															String lote,
															String fechaVencimiento,
															String tipoDespacho,
															String almacenConsignacion,
															String proveedorCompra,
															String proveedorCatalogo, double saldoDosis ) 
	{
		logger.info("CODIGO ARTICULO>>>>>>>>>>>>"+articulo);
		logger.info("CODIGO ARTICULO PRINCIPAL>>>>>>>>>>>>"+articuloPrincipal);
		int resp=0;
		try
		{		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
					
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insercionDetalleDespachoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,articulo);
			ps.setInt(2,articuloPrincipal);
			ps.setInt(3,codigoSequenciaDespacho);
			ps.setInt(4,cantidad);
            ps.setInt(5, articulo);
            
            if(UtilidadTexto.isEmpty(lote))
            {
            	ps.setObject(6, null);
            }
            else
            {	
            	ps.setString(6, lote);
            }	
            if(UtilidadFecha.esFechaValidaSegunAp(fechaVencimiento))
            {
            	ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento)));
            }
            else
            {
            	ps.setObject(7, null);
            }
            
            if(UtilidadTexto.isEmpty(tipoDespacho))
            	ps.setNull(8, Types.VARCHAR);
            else
            	ps.setString(8, tipoDespacho);
            
            if(UtilidadTexto.isEmpty(almacenConsignacion))
            	ps.setNull(9, Types.INTEGER);
            else
            	ps.setString(9, almacenConsignacion);
            
            if(UtilidadTexto.isEmpty(proveedorCompra))
            	ps.setNull(10, Types.VARCHAR);
            else
            	ps.setString(10, proveedorCompra);
            
            if(UtilidadTexto.isEmpty(proveedorCatalogo))
            	ps.setNull(11, Types.VARCHAR);
            else
            	ps.setString(11, proveedorCatalogo);
            
            ps.setDouble(12, Utilidades.convertirADouble(saldoDosis+"",true));
            
            ps.setInt(13, articulo);
            
            resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseDespachoMedicamentosDao "+e.toString() );
			resp=0;
		}
		return resp;
	}
	
	/**
	 * Cuando un User adiciona sustitutos a los medicamentos originales, entonces se tiene que añadir más filas a la collection,
	 * para hacer esto se adaptó éste método, el cual en principio reutiliza la consulta listadoMedicamentosStr,
	 * y por medio del 'numeroOriginal y el numeroSustituto' hace un UNION adicionandole el valor del sustituto 
	 * a la colección.
	 * @param con, Connection, Conexión con la fuente de datos
	 * @param numeroSolicitud, número de la solicitud,
	 * @param coleccionNumerosSustitutos, Collection, contiene el numeroSustituto-numeroOriginal
	 * @param codigoInstitucion Código de la institución del usuario
	 * @return ResulSet, con la lista de los medicamentos originales y sus sustitutos
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultaGenerada (Connection con, int numeroSolicitud, Collection coleccionNumerosSustitutos, int codigoInstitucion, int codigoArticulo) throws SQLException
	{
		
		String listadoMedicamentosStr=		"(SELECT " +
								" a.codigo AS codigo, " +
								" coalesce(a.codigo_interfaz,'') as codigointerfaz, " +
								"a.descripcion AS descripcion, " +
								"coalesce(a.concentracion, '') AS concentracion, " +
								"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
								"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidadMedida, " +
								"(SELECT eqi.cantidad FROM equivalentes_inventario eqi WHERE a.codigo = eqi.articulo_equivalente AND eqi.articulo_ppal="+codigoArticulo+") AS cantidadeq, " +
								"ds.dosis as dosis, " +
								"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END  as unidosis, " +
								"coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
								"coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
								"esarticulomultidosis(a.codigo) as esmultidosis, " +
								"ds.dias_tratamiento as diastratamiento, " +
								"ds.frecuencia as frecuencia, " +
								"ds.tipo_frecuencia as tipoFrecuencia, " +
								"ds.cantidad as cantidad, " +
								"ds.via as via, " +
								"ds.observaciones as observaciones, " +
								"na.es_pos AS es_pos, " +
								"CASE WHEN getDespacho(a.codigo, ds.numero_solicitud) IS NULL THEN 0 ELSE getDespacho(a.codigo, ds.numero_solicitud) END as despacho_total, " +
								"CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
								" ";
		
		int[] codigosJustificacion=Utilidades.buscarCodigosJustificaciones(con, codigoInstitucion, false, true);
		for(int i=0; i<codigosJustificacion.length; i++)
		{
			listadoMedicamentosStr+=
				"getNopos("+codigosJustificacion[i]+",a.codigo, ds.numero_solicitud) AS just"+codigosJustificacion[i]+", " ;
		}
		listadoMedicamentosStr+=
								"'vacio' AS codigoSustitutoPrincipal, " +
								"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS estaAlmacenadoBD, " +
								"sa.motivo AS motivoSuspension, " +
								"CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END AS esSuspendido, " +
                                "getTotalExisArticulosXAlmacen( sol.centro_costo_solicitado , a.codigo, "+codigoInstitucion+") AS existenciaArticuloXAlmacen, " +
                                "cc.nombre AS almacen, "+
                                "coalesce(onp.unidad_volumen,'') as unidadvolumenmezcla, "+
                                "na.acronimo acronimo_naturaleza "+
								"from " +
								"articulo a " +
								"inner join detalle_solicitudes ds on (a.codigo=ds.articulo  and ds.articulo_principal is null) " +
								"inner join naturaleza_articulo na on (a.naturaleza=na.acronimo and a.institucion=na.institucion) "+
								"inner join solicitudes_medicamentos sm on (ds.numero_solicitud=sm.numero_solicitud) " +
                                "inner join solicitudes sol on (sol.numero_solicitud=sm.numero_solicitud) " +
                                "inner join centros_costo cc on (cc.codigo=sol.centro_costo_solicitado) " +
                                "LEFT OUTER JOIN suspension_articulo sa ON (a.codigo=sa.articulo AND sa.numero_solicitud=ds.numero_solicitud) " +
                                "LEFT OUTER JOIN orden_nutricion_parente onp ON(onp.codigo_historico_dieta=sm.orden_dieta and onp.articulo=a.codigo) " +
                                "where " +
								"na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
								"AND ds.numero_solicitud =? ) " +
								"UNION ALL " +
								"(SELECT distinct (detd.articulo) AS codigo, " +
								" coalesce(a.codigo_interfaz,'') as codigointerfaz, " +
								"a.descripcion AS descripcion, " +
								"coalesce(a.concentracion, '') AS concentracion, " +
								"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
								"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidadMedida, " +
								"(SELECT eqi.cantidad FROM equivalentes_inventario eqi WHERE a.codigo = eqi.articulo_equivalente AND eqi.articulo_ppal="+codigoArticulo+") AS cantidadeq, " +
								"ds.dosis as dosis, " +
								"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END  as unidosis, " +
								"coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
								"coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
								"esarticulomultidosis(a.codigo) as esmultidosis, " +
								"ds.dias_tratamiento as diastratamiento, " +
								"ds.frecuencia as frecuencia, " +
								"ds.tipo_frecuencia as tipoFrecuencia, " +
								"ds.cantidad as cantidad, " +
								"ds.via as via, " +
								"ds.observaciones as observaciones, " +
								"na.es_pos AS es_pos, " +
								"CASE WHEN getDespacho(a.codigo, ds.numero_solicitud) IS NULL THEN 0 ELSE getDespacho(a.codigo, ds.numero_solicitud) END as despacho_total, " +
								"CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
								"";
		
		for(int i=0; i<codigosJustificacion.length; i++)
		{
			listadoMedicamentosStr+=
				"getNopos("+codigosJustificacion[i]+",a.codigo, ds.numero_solicitud) AS just"+codigosJustificacion[i]+", " ;
		}
		listadoMedicamentosStr+=
								"detd.articulo ||'@'|| detd.art_principal AS codigoSustitutoPrincipal,  " +
								"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS estaAlmacenadoBD," +
								"sa.motivo AS motivoSuspension, " +
								"CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END AS esSuspendido, " +
                                "getTotalExisArticulosXAlmacen( sol.centro_costo_solicitado , a.codigo, "+codigoInstitucion+") AS existenciaArticuloXAlmacen, " +
                                "cc.nombre AS almacen, "+
                                "coalesce(onp.unidad_volumen,'') as unidadvolumenmezcla, "+
                                "na.acronimo acronimo_naturaleza "+
								"from " +
								"detalle_despachos detd " +
								"INNER JOIN  despacho desp on (detd.despacho=desp.orden) " +
								"INNER JOIN articulo a on (a.codigo=detd.articulo ) " +
								"INNER JOIN detalle_solicitudes ds ON (ds.articulo=detd.art_principal AND ds.numero_solicitud= desp.numero_solicitud  and ds.articulo_principal is null) " +
								"inner join solicitudes_medicamentos sm on (ds.numero_solicitud=sm.numero_solicitud) " +
                                "INNER JOIN solicitudes sol on (sol.numero_solicitud=ds.numero_solicitud) " +
                                "INNER JOIN centros_costo cc on (cc.codigo=sol.centro_costo_solicitado) " +
                                "INNER JOIN naturaleza_articulo na on (na.acronimo = a.naturaleza) " +
								"LEFT OUTER JOIN suspension_articulo sa ON(a.codigo=sa.articulo AND sa.numero_solicitud=desp.numero_solicitud) " +
								"LEFT OUTER JOIN orden_nutricion_parente onp ON(onp.codigo_historico_dieta=sm.orden_dieta and onp.articulo=a.codigo) " +
								"where " +
								"na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
								"and desp.numero_solicitud = ? " +
								"and detd.articulo<>detd.art_principal) ";

		String consultaGenerada=listadoMedicamentosStr;
        //Este temporal contendrá      temporal[0]=numeroSustituto     temporal[1]=numeroOriginal
		int temporal[];
        Iterator it=coleccionNumerosSustitutos.iterator();
        //CONSULTA DE LOS SUSTITUTOS
        while (it.hasNext())
        {
        	temporal=(int[])it.next();
        	consultaGenerada=consultaGenerada+" UNION ALL " +
        															"(SELECT a.codigo AS codigo, " +
        															"a.descripcion AS descripcion, " +
        															" coalesce(a.codigo_interfaz,'') as codigointerfaz, " +
        															"coalesce(a.concentracion,'') AS concentracion, " +
																	"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
																	"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidadMedida, " +
																	"(SELECT eqi.cantidad FROM equivalentes_inventario eqi WHERE a.codigo = eqi.articulo_equivalente AND eqi.articulo_ppal="+temporal[1]+") AS cantidadeq, " +
																	"ds.dosis as dosis, " +
																	"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END  as unidosis, " +
																	"coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
																	"coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
																	"esarticulomultidosis(a.codigo) as esmultidosis, " +
																	"ds.dias_tratamiento as diastratamiento, " +
        															"ds.frecuencia as frecuencia, " +
        															"ds.tipo_frecuencia as tipoFrecuencia, " +
        															"ds.cantidad as cantidad, " +
        															"ds.via as via, " +
        															"ds.observaciones as observaciones, " +
																	"na.es_pos AS es_pos, " +
        															"CASE WHEN getDespacho(a.codigo, ds.numero_solicitud) IS NULL THEN 0 ELSE getDespacho(a.codigo, ds.numero_solicitud)  END as despacho_total, " +
        															"CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
        															"" ;
        															//En caso de que se necesite heredar la info de justificacion del articulo principal entonces se cambia va.codigo por temporal[1]
    		for(int i=0; i<codigosJustificacion.length; i++)
    		{
    			consultaGenerada+=
    				"getNopos("+codigosJustificacion[i]+",a.codigo, ds.numero_solicitud) AS just"+codigosJustificacion[i]+", " ;
    		}
    		consultaGenerada+=
        															"  '"+ temporal[0] +"@"+ temporal[1] +"' AS codigoSustitutoPrincipal, " +
        															"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS estaAlmacenadoBD, " +
        															"sa.motivo AS motivoSuspension, " +
																	"CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END AS esSuspendido, " +
                                                                    "getTotalExisArticulosXAlmacen( sol.centro_costo_solicitado , a.codigo, "+codigoInstitucion+") AS existenciaArticuloXAlmacen, " +
                                                                    "cc.nombre AS almacen, "+
                                                                    "'' as unidadvolumenmezcla, "+
                                                                    "na.acronimo acronimo_naturaleza "+
        															"from " +
        															"articulo a " +
        															"INNER JOIN detalle_solicitudes ds ON (ds.articulo=" + temporal[1] + " AND (ds.numero_solicitud=" + numeroSolicitud + ")  and ds.articulo_principal is null) " +
                                                                    "INNER JOIN solicitudes sol on (sol.numero_solicitud=ds.numero_solicitud) " +
                                                                    "INNER JOIN centros_costo cc on (cc.codigo=sol.centro_costo_solicitado) " +
                                                                    "INNER JOIN naturaleza_articulo na on (na.acronimo = a.naturaleza) " +
        															"LEFT OUTER JOIN suspension_articulo sa ON(a.codigo=sa.articulo AND sa.numero_solicitud=ds.numero_solicitud) " +
        															"where a.codigo=" + temporal[0] +") ";
        }
        
        logger.info("\n consulta --> "+consultaGenerada);
        PreparedStatementDecorator listadoMStatement= new PreparedStatementDecorator(con.prepareStatement(consultaGenerada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        listadoMStatement.setInt(1,numeroSolicitud);
        listadoMStatement.setInt(2,numeroSolicitud);
        return new ResultSetDecorator(listadoMStatement.executeQuery());
	}
	
	/**
	 * Cuando un User adiciona sustitutos a los medicamentos originales, entonces se tiene que añadir más filas a la collection,
	 * para hacer esto se adaptó éste método, el cual en principio reutiliza la consulta listadoMedicamentosStr,
	 * y por medio del 'numeroOriginal y el numeroSustituto' hace un UNION adicionandole el valor del sustituto 
	 * a la colección.
	 * @param con, Connection, Conexión con la fuente de datos
	 * @param numeroSolicitud, número de la solicitud,
	 * @param coleccionNumerosSustitutos, Collection, contiene el numeroSustituto-numeroOriginal
	 * @param codigoInstitucion Código de la institución del usuario
	 * @return ResulSet, con la lista de los medicamentos originales y sus sustitutos
	 * @throws SQLException
	 */
	public static ResultSetDecorator consultaEquivalentes (Connection con, int codSus, int codPrin) throws SQLException
	{
		String consultaEq="SELECT eqi.cantidad FROM equivalentes_inventario eqi WHERE "+codSus+" = eqi.articulo_equivalente AND eqi.articulo_ppal="+codPrin+"";
		PreparedStatementDecorator listadoMStatement= new PreparedStatementDecorator(con.prepareStatement(consultaEq,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(listadoMStatement.executeQuery());
	}
	
	/**
	 * Carga el Listado de las solicitudes internas de medicamentos
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoCentroCostoUser, int, codigo del centro de costo del usuario (filtro)
	 * @param filtrarPorCantidad Filtrar si se muestran o no las solicitudes en las cuales no se ha ingresado cantidad
	 * @return ResulSet list
	 */
	public static ResultSetDecorator listadoSolicitudesMedicamentos(Connection con, HashMap<String, Object> criteriosBusquedaMap) 
			//int codigoCentroCostoUser, int codigoPersona, boolean filtrarPorCantidad, int codigoFiltroAreaCentroCostoSolicitante)
	{
		try 
		{
			String listadoGeneradoStr=listadoSolicitudesStr;
			listadoGeneradoStr+=" AND sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+" "; 
			
			
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+""))))
			{
				listadoGeneradoStr+="AND (" +
							"sol.cuenta NOT IN (SELECT cuenta from manejopaciente.egresos where cuenta=sol.cuenta  and usuario_responsable is not null) " +
							"OR " +
							"(cue.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" AND getcuentafinal(cue.id) IS NULL)" +
							")";
			}
			
			if(Integer.parseInt(criteriosBusquedaMap.get("codigoPersona").toString())>0)
				listadoGeneradoStr+=" AND per.codigo = "+criteriosBusquedaMap.get("codigoPersona").toString()+" ";
			
			if(Integer.parseInt(criteriosBusquedaMap.get("codigoCentroCostoUser").toString())>0)
				listadoGeneradoStr+=" AND  sol.centro_costo_solicitado =  "+criteriosBusquedaMap.get("codigoCentroCostoUser")+" ";
			
			//filtrado por area
			if(Integer.parseInt(criteriosBusquedaMap.get("areaFiltro").toString())>0)
				listadoGeneradoStr+=" AND cue.area = "+criteriosBusquedaMap.get("areaFiltro")+" ";
				//listadoGeneradoStr+=" AND sol.centro_costo_solicitante = "+criteriosBusquedaMap.get("areaFiltro").toString();
			
			//filtrado por piso, habitacion cama
			if(Integer.parseInt(criteriosBusquedaMap.get("camaFiltro").toString())>0)
				listadoGeneradoStr+=" AND  cue.id in (SELECT h.cuenta from ordenes.his_camas_cuentas h where h.codigocama="+criteriosBusquedaMap.get("camaFiltro")+") ";
			else
			{
				if(Integer.parseInt(criteriosBusquedaMap.get("habitacionFiltro").toString())>0)
					listadoGeneradoStr+=" AND  cue.id in (SELECT h.cuenta from ordenes.his_camas_cuentas h where h.codigopkhabitacion="+criteriosBusquedaMap.get("habitacionFiltro")+") ";
				else
				{
					if(Integer.parseInt(criteriosBusquedaMap.get("pisoFiltro").toString())>0)
						listadoGeneradoStr+=" AND  cue.id in (SELECT h.cuenta from ordenes.his_camas_cuentas h where h.codigopkpiso="+criteriosBusquedaMap.get("pisoFiltro")+") ";
				}
			}
			
			String orden=" ORDER BY fechaSolicitud ASC, horaSolicitud ASC ";
			
			if(UtilidadTexto.getBoolean(criteriosBusquedaMap.get("filtrarPorCantidad").toString()))
			{
				listadoGeneradoStr+=" AND esSolMedSinCantNoMezclas(sol.numero_solicitud)=0";
			}
			listadoGeneradoStr+=orden;
			
			
			logger.info("\n\n**********************************************************************************************************");
			logger.info("listadoSolicitudesMedicamentos---> "+listadoGeneradoStr);
			logger.info("**********************************************************************************************************\n\n");
			
			PreparedStatementDecorator listadoStatement= new PreparedStatementDecorator(con.prepareStatement(listadoGeneradoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			return new ResultSetDecorator(listadoStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del listado de las solicitudes internas de medicamentos: SqlBaseDespachoMedicamentosDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Carga la Información de la creación de la solicitud o Carga el Listado de la info de medicamentos solicitado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud, int, numero de la solicitud 
	 * @param esMedicamentos, boolean, determina el tipo de consulta
	 * @param codigoInstitucion Código de la institución a la cual pertenece el usuario
	 * @return ResulSet list
	 */
	public static ResultSetDecorator infoSolicitud(Connection con, int numeroSolicitud, boolean esMedicamentos, int codigoInstitucion, int codigoArticulo)
	{
		String listadoMedicamentosStr=		"(SELECT " +
								" a.codigo AS codigo, " +
								"to_char(sol.fecha_solicitud,'yyyy-mm-dd') AS fechasolicitud, "+
								"sol.hora_solicitud AS horasolicitud, "+
								"coalesce(a.codigo_interfaz, '') as codigointerfaz, " +
								"a.descripcion AS descripcion, (SELECT eqi.cantidad FROM equivalentes_inventario eqi WHERE a.codigo = eqi.articulo_equivalente AND eqi.articulo_ppal="+codigoArticulo+") AS cantidadeq, " +
								"CASE WHEN a.concentracion IS NULL THEN '' ELSE a.concentracion END AS concentracion, " +
								"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
								"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidadMedida, " +
								"ds.dosis as dosis, " +
								"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END  as unidosis, " +
								"coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
								"coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
								"esarticulomultidosis(a.codigo) as esmultidosis, " +
								"ds.dias_tratamiento as diastratamiento, " +
								"ds.frecuencia as frecuencia, " +
								"ds.tipo_frecuencia as tipoFrecuencia, " +
								"ds.cantidad as cantidad, " +
								"ds.via as via, " +
								"ds.observaciones as observaciones, " +
								"CASE WHEN getDespacho(a.codigo, ds.numero_solicitud) IS NULL THEN 0 ELSE getDespacho(a.codigo, ds.numero_solicitud) END as despacho_total, " +
								"CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
								"na.es_pos AS es_pos, ";
						
				int[] codigosJustificacion=Utilidades.buscarCodigosJustificaciones(con, codigoInstitucion, false, true);
				for(int i=0; i<codigosJustificacion.length; i++)
				{
				listadoMedicamentosStr+=
				"getNoPos("+codigosJustificacion[i]+",a.codigo, ds.numero_solicitud) AS just"+codigosJustificacion[i]+", " ;
				}
				listadoMedicamentosStr+=
								"'vacio' AS codigoSustitutoPrincipal, " +
								"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS estaAlmacenadoBD, " +
								"sa.motivo AS motivoSuspension, " +
								
								" CASE WHEN sm.orden_dieta is null  THEN CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END ELSE CASE WHEN getessuspendidamezcla(sm.orden_dieta)!='S' THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END end AS esSuspendido, "+
                               
								
								"getTotalExisArticulosXAlmacen( sol.centro_costo_solicitado , a.codigo, "+codigoInstitucion+") AS existenciaArticuloXAlmacen, " +
                                "cc.nombre AS almacen, " +
                                "coalesce(onp.unidad_volumen,'') as unidadvolumenmezcla "+
								"from " +
								"solicitudes sol " +
								"inner join detalle_solicitudes ds on (ds.numero_solicitud=sol.numero_solicitud  and ds.articulo_principal is null) " +
								"inner join solicitudes_medicamentos sm on (ds.numero_solicitud=sm.numero_solicitud) " +
								"inner join articulo a on (a.codigo=ds.articulo) " +
								"inner join centros_costo cc on (cc.codigo=sol.centro_costo_solicitado) " +
								"inner join naturaleza_articulo na ON (na.acronimo = a.naturaleza ) " +
								"LEFT OUTER JOIN suspension_articulo sa ON(a.codigo=sa.articulo AND sa.numero_solicitud=ds.numero_solicitud) " +
								"LEFT OUTER JOIN orden_nutricion_parente onp ON(onp.codigo_historico_dieta=sm.orden_dieta and onp.articulo=a.codigo) " +
								"where " +
								"ds.numero_solicitud =? " +
								"AND na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
								") " +
								"UNION ALL " +
								"(SELECT "+
								"distinct (detd.articulo) AS codigo, " +
								"to_char(sol.fecha_solicitud,'yyyy-mm-dd') AS fechasolicitud, "+
								"sol.hora_solicitud AS horasolicitud, "+
								"coalesce(a.codigo_interfaz,'') as codigointerfaz, " +
								"a.descripcion AS descripcion, (SELECT eqqi.cantidad FROM equivalentes_inventario eqqi WHERE a.codigo = eqqi.articulo_equivalente AND eqqi.articulo_ppal=detd.art_principal) AS cantidadeq, " +
								"CASE WHEN a.concentracion IS NULL THEN '' ELSE a.concentracion END AS concentracion, " +
								"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
								"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END AS unidadMedida, " +
								"ds.dosis as dosis, " +
								"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END  as unidosis, " +
								"coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
								"coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
								"esarticulomultidosis(a.codigo) as esmultidosis, " +
								"ds.dias_tratamiento as diastratamiento, " +
								"ds.frecuencia as frecuencia, " +
								"ds.tipo_frecuencia as tipoFrecuencia, " +
								"ds.cantidad as cantidad, " +
								"ds.via as via, " +
								"ds.observaciones as observaciones, " +
								"CASE WHEN getDespacho(a.codigo, ds.numero_solicitud) IS NULL THEN 0 ELSE getDespacho(a.codigo, ds.numero_solicitud) END  as despacho_total, " +
								"CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
								"na.es_pos AS es_pos, ";
				for(int i=0; i<codigosJustificacion.length; i++)
				{
				listadoMedicamentosStr+=
				"getNoPos("+codigosJustificacion[i]+",a.codigo, ds.numero_solicitud) AS just"+codigosJustificacion[i]+", " ;
				}
				listadoMedicamentosStr+=
								"detd.articulo ||'@'|| detd.art_principal AS codigoSustitutoPrincipal,  " +
								"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS estaAlmacenadoBD," +
								"sa.motivo AS motivoSuspension, " +
								
								" CASE WHEN sm.orden_dieta is null  THEN CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END ELSE CASE WHEN getessuspendidamezcla(sm.orden_dieta)!='S' THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END end AS esSuspendido, "+
								
                                
								"getTotalExisArticulosXAlmacen( sol.centro_costo_solicitado , a.codigo, "+codigoInstitucion+") AS existenciaArticuloXAlmacen, " +
                                "cc.nombre AS almacen, "+
                                "coalesce(onp.unidad_volumen,'') as unidadvolumenmezcla "+
								"from " +
								"detalle_despachos detd " +
								"INNER JOIN  despacho desp ON (detd.despacho=desp.orden) " +
								"INNER JOIN articulo a ON (a.codigo=detd.articulo ) " +
								"INNER JOIN detalle_solicitudes ds ON (ds.articulo=detd.art_principal AND ds.numero_solicitud= desp.numero_solicitud and ds.articulo_principal is null) " +
								"inner join solicitudes_medicamentos sm on (ds.numero_solicitud=sm.numero_solicitud) " +
                                "INNER JOIN solicitudes sol on (sol.numero_solicitud=ds.numero_solicitud) " +
                                "INNER JOIN centros_costo cc on (cc.codigo=sol.centro_costo_solicitado) " +
                                "inner join naturaleza_articulo na ON (na.acronimo = a.naturaleza and na.institucion=a.institucion) " +
								"LEFT OUTER JOIN suspension_articulo sa ON(a.codigo=sa.articulo AND sa.numero_solicitud=desp.numero_solicitud) " +
								"LEFT OUTER JOIN orden_nutricion_parente onp ON(onp.codigo_historico_dieta=sm.orden_dieta and onp.articulo=a.codigo) " +
								"where " +
								"na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
								"and desp.numero_solicitud = ? " +
								"and detd.articulo<>detd.art_principal) ";
		try
		{
		    if(esMedicamentos)
		    {
		        PreparedStatementDecorator listadoMStatement= new PreparedStatementDecorator(con.prepareStatement(listadoMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		        listadoMStatement.setInt(1,numeroSolicitud);
		        listadoMStatement.setInt(2,numeroSolicitud);

		        logger.info("\n\n\n\n DETALLE DESPACHO------->"+listadoMedicamentosStr+"---->"+numeroSolicitud+"\n\n\n");
		        
		        return new ResultSetDecorator(listadoMStatement.executeQuery());
		    }
		    else
		    {
		        PreparedStatementDecorator infoStatement= new PreparedStatementDecorator(con.prepareStatement(infoSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		        infoStatement.setInt(1,numeroSolicitud);
		        
		        logger.info("\n\n\n\n DETALLE DESPACHO------->"+infoSolicitudStr+"---->"+numeroSolicitud+"\n\n\n");
		        return new ResultSetDecorator(infoStatement.executeQuery());
		    }
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en cargar la info de la solicitud de medicamentos: SqlBaseDespachoMedicamentosDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Carga los registros existentes de Insumos en la BD.
	 * @param con, Connection, conexion con la fuente de datos.
	 * @param numeroSolicitud, int numero de la solicitud.
	 * @return, ResultSet
	 */
	public static ResultSetDecorator infoInsumos(Connection con, int numeroSolicitud)
	{
	    try
	    {
	    	logger.info("-->"+articulosInsumosStr+"--"+numeroSolicitud);
	        PreparedStatementDecorator listadoInsumosStatement= new PreparedStatementDecorator(con.prepareStatement(articulosInsumosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        listadoInsumosStatement.setInt(1,numeroSolicitud);
	        listadoInsumosStatement.setInt(2,numeroSolicitud);
	        listadoInsumosStatement.setInt(3,numeroSolicitud);
	        return new ResultSetDecorator(listadoInsumosStatement.executeQuery());	        
	    }
	   catch(SQLException e)
	   {
	       logger.error(e+" Error en cargar la info de Insumos: SqlBaseDespachoMedicamentosDao "+e.toString());
	       return null;
	   }
	   
	}

	/**
	 * Método que elimina un sustituto con despacho total 0 
	 */
	public static int eliminarSustitutoConDespachoCero(Connection con, int articulo, int numeroSolicitud)
	{
		int resp=0;	
		try
		{
				if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL: Conexión cerrada");
				}
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(eliminarSustitutoConDespachoCeroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				ps.setInt(1,articulo);
				ps.setInt(2,numeroSolicitud);
				
				resp=ps.executeUpdate();
			}
			catch(SQLException e)
			{
				logger.warn(e+" Error en el borrado de datos: SqlBaseDespachoMedicamentosDao "+e.toString());
				resp=0;			
			}	
			return resp;	
	}
	
	/**
	 * Método que Actualiza el estado médico de la solicitud en (Administrada-Despachada) 
	 * y su correspondiente Número de Autorización
	 */
	public static int cambiarEstadoMedicoSolicitudYNumAutorizacion(Connection con, int estadoMedico, /*String numeroAutorizacion, */int numeroSolicitud)
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoMedicoSolicitudYNumAutorizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1,estadoMedico);
			//ps.setString(2,numeroAutorizacion);
			ps.setInt(2,numeroSolicitud);
				
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualización de datos: SqlBaseDespachoMedicamentosDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	
	/**
	 * Método que pone la solicitud en estado anulada cuando no se genera ningun cargo por que la cantidad despachada 
	 * o adnminstrada es igual a cero.
	 * @param con
	 * @param estadoFacturacion
	 * @param numeroSolicitud
	 * @return
	 */
	public static int cambiarEstadoFacturacionSolicitud(Connection con, int estadoFacturacion, int numeroSolicitud)
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoFacturacionSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			ps.setInt(1,estadoFacturacion);
			ps.setInt(2,numeroSolicitud);
			
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualización de datos: SqlBaseDespachoMedicamentosDao "+e.toString());
			resp=0;			
		}	
		return resp;	
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean dosisConsistentesSolicitud(Connection con, int numeroSolicitud)
	{
		String cadena=" (SELECT  " +
							"a.codigo AS codigo, " +
							"a.descripcion AS descripcion, " +
							"ds.dosis as dosis, " +
							"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END  as unidosis, " +
							"ds.dias_tratamiento as diastratamiento,  " +
							"ds.cantidad as cantidad " +
						"from " +
							"solicitudes sol " +
							"inner join detalle_solicitudes ds on (ds.numero_solicitud=sol.numero_solicitud  and ds.articulo_principal is null) " +
							"inner join articulo a on (a.codigo=ds.articulo) " +
							"inner join naturaleza_articulo na ON (na.acronimo = a.naturaleza and na.institucion=a.institucion) " +
						"where " +
							"ds.numero_solicitud = ? " +
							"AND na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
							"and ds.cantidad=0" +
						") " +
						"UNION ALL " +
						"(SELECT " +
							"distinct (detd.articulo) AS codigo, " +
							"a.descripcion AS descripcion, " +
							"ds.dosis as dosis, " +
							"CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE ds.unidosis_articulo||'' END  as unidosis, " +
							"ds.dias_tratamiento as diastratamiento, " +
							"ds.cantidad as cantidad " +
						"from " +
							"detalle_despachos detd " +
							"INNER JOIN  despacho desp ON (detd.despacho=desp.orden) " +
							"INNER JOIN articulo a ON (a.codigo=detd.articulo ) " +
							"INNER JOIN detalle_solicitudes ds ON (ds.articulo=detd.art_principal AND ds.numero_solicitud= desp.numero_solicitud  and ds.articulo_principal is null) " +
							"INNER JOIN solicitudes sol on (sol.numero_solicitud=ds.numero_solicitud) " +
							"inner join naturaleza_articulo na ON (na.acronimo = a.naturaleza ) " +
						"where " +
							"na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
							"and desp.numero_solicitud = ? " +
							"and detd.articulo<>detd.art_principal " +
							"and ds.cantidad=0) ";
		
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,numeroSolicitud);
		    ps.setInt(2,numeroSolicitud);
		    ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
		    
		    while(rs.next())
		    {
		    	boolean esDosisValida=true;
		    	try
		    	{
		    		Double.parseDouble(rs.getString("dosis"));
		    	}
		    	catch(Exception e)
		    	{
		    		esDosisValida=false;
		    		logger.info("error->"+e);
		    	}
		    	
		    	boolean existeInfoCantidadUnidosis= UtilidadValidacion.articuloTieneCantidadUnidosisActiva(con, rs.getInt("codigo"));
		    	if(!esDosisValida && existeInfoCantidadUnidosis)
		    	{
		    		logger.info("retorna false");
		    		return false;
		    	}
		    }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		logger.info("retorna true");
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param unidadmedida 
	 * @param dosis
	 * @param sumarDosis
	 * @return
	 */
	public static boolean actualizarDosisSobrantes(Connection con, int codigoCentroCosto, int codigoArticulo, String unidadmedida, double dosis, boolean sumarDosis) 
	{
		try
    	{
			//cuando se suma a la dosis, si no existe se inserta, si existe se actualiza
			double varDosis=Utilidades.convertirADouble(dosis+"",true);
			if(sumarDosis)
			{
				String cadenaTemp="SELECT centro_costo from saldos_dosis_disponibles where centro_costo = ? and articulo=? and unidad_medida=?";
				PreparedStatementDecorator psTemp= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				psTemp.setInt(1, codigoCentroCosto);
				psTemp.setInt(2, codigoArticulo);
				psTemp.setString(3, unidadmedida);
				ResultSetDecorator rsTemp=new ResultSetDecorator(psTemp.executeQuery());
				
				//existe entonces se debe actualizar
				if(rsTemp.next())
				{
					String cadena="UPDATE saldos_dosis_disponibles set dosis_disponible=dosis_disponible+"+varDosis+"  where centro_costo = ? and articulo=? and unidad_medida=?";
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
					ps.setInt(1, codigoCentroCosto);
					ps.setInt(2, codigoArticulo);
					ps.setString(3, unidadmedida);
					ps.executeUpdate();
					return true;
				}
				else //debo insertar como nuevo.
				{
					String cadena="insert into saldos_dosis_disponibles (centro_costo,articulo,unidad_medida,dosis_disponible) values(?,?,?,?)";
					PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
					ps.setInt(1, codigoCentroCosto);
					ps.setInt(2, codigoArticulo);
					ps.setString(3, unidadmedida);
					ps.setObject(4, varDosis+"");
					ps.executeUpdate();
					return true;
				}
					
			}
			else
			{
				String cadena="UPDATE saldos_dosis_disponibles set dosis_disponible=dosis_disponible-"+varDosis+"  where centro_costo = ? and articulo=? and unidad_medida=?";
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
				ps.setInt(1, codigoCentroCosto);
				ps.setInt(2, codigoArticulo);
				ps.setString(3, unidadmedida);
				ps.executeUpdate();
				return true;
			}
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param unidadMedida
	 * @return
	 */
	public static double obtenerDosisSaldoArticulo(Connection con, int codigoCentroCosto, String codigoArticulo, String unidadMedida) 
	{
		try
		{
			String cadenaTemp="SELECT dosis_disponible from saldos_dosis_disponibles where centro_costo = ? and articulo=? and unidad_medida=?";
			PreparedStatementDecorator psTemp= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psTemp.setInt(1, codigoCentroCosto);
			psTemp.setObject(2, codigoArticulo+"");
			psTemp.setString(3, unidadMedida);
			ResultSetDecorator rsTemp=new ResultSetDecorator(psTemp.executeQuery());
			if(rsTemp.next())
				return rsTemp.getDouble(1);
		}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return 0;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeDespachocConEntregaDirPac(Connection con, int numeroSolicitud)
	{
		String consulta="SELECT numero_solicitud from despacho where es_directo= "+ValoresPorDefecto.getValorTrueParaConsultas()+" and numero_solicitud=?";
		Log4JManager.info(consulta);
		try
		{
			PreparedStatementDecorator psTemp= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psTemp.setInt(1, numeroSolicitud);
			ResultSetDecorator rsTemp=new ResultSetDecorator(psTemp.executeQuery());
			if(rsTemp.next())
				return true;
		}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return false;	
	}
	
}
