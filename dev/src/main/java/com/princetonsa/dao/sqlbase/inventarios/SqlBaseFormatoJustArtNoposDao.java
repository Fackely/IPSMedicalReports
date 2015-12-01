/**
 * 
 */
package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.inventario.DtoAccionCampo;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.EquivalentesDeInventario;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author axioma
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SqlBaseFormatoJustArtNoposDao {

	/**
	 * Objeto para manejar log de la clase
	 * */
	private static Logger logger = Logger
			.getLogger(SqlBaseFormatoJustArtNoposDao.class);

	private static String cadenaConsultaParametrizacionSecciones = "SELECT "
			+ "codigo " + "FROM " + " secciones_jus " + "WHERE "
			+ "mostrar='S' AND servicio='N' " + "AND tipo_jus = 'MEDI' "
			+ " ORDER BY " + " orden";

	private static String cadenaConsultaMedicamentosPos = " (select distinct "
			+ "	ssc.articulo as codigo, "
			+ "	getdescripcionarticulo(ssc.articulo) as nombre,"
			+ "	max (sm.numero_solicitud) as numerosolicitud,"
			//+ "	max(da.fecha)||'' as fecha,"
			+ "	'"
			+ ConstantesBD.acronimoNo
			+ "' as esantecedente "
			+ " from "
			+ "	ingresos i "
			+ "	inner join cuentas c on (c.id_ingreso=i.id) "
			+ "	inner join solicitudes s on (s.cuenta=c.id) "
			+ "	inner join solicitudes_medicamentos sm on (sm.numero_solicitud=s.numero_solicitud) "
			+ " inner join solicitudes_subcuenta ssc ON (ssc.solicitud=s.numero_solicitud) "
			//+ "	inner join admin_medicamentos am on (am.numero_solicitud=sm.numero_solicitud) "
			//+ "	inner join detalle_admin da on (da.administracion=am.codigo) "
			+ "	inner join articulo a on (a.codigo=ssc.articulo) "
			+ "	inner join subgrupo_inventario sgi on (a.subgrupo=sgi.codigo) "
			+ "	inner join naturaleza_articulo na on (a.naturaleza=na.acronimo)"
			+ "	where ";

	private static String cadenaConsultaDatosPacienteXOrden = "SELECT " +
																"oa.fecha as fecha, " +
																"getnomcentrocosto(oa.centro_costo_solicita) as servsolicitadoen, " +
																"c.id_ingreso as ingreso, " +
																"coalesce(getconsecutivoingreso(c.id_ingreso), '') AS ingconsecutivo, " + 
																"p.tipo_identificacion || ' ' || p.numero_identificacion as documentopaciente,p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre as nombrepaciente, " +
																"p.codigo as codigopersona, " +
																"p.fecha_nacimiento as fechanacimiento, " + 
																"vi.nombre as via_ingreso,  " +
																"vi.codigo as codigo_via_ingreso, " +
																"CASE WHEN eg.fecha_egreso is null THEN 0 ELSE 1 END as tieneegreso " +
															  "FROM ordenes_ambulatorias oa " +
																"INNER JOIN cuentas c ON (c.id=oa.cuenta_solicitante) " +
																"INNER JOIN personas p ON c.codigo_paciente=p.codigo  " +
																"INNER JOIN vias_ingreso vi ON vi.codigo = c.via_ingreso  " +
																"LEFT JOIN egresos eg ON eg.cuenta = c.id  " +
															  "WHERE oa.codigo = ? ";

	private static String cadenaConsultaDatosPaciente = "SELECT "
			+ "s.fecha_solicitud as fecha,"
			+ "getnomcentrocosto(s.centro_costo_solicitante) as servsolicitadoen, "
			+ "c.id_ingreso as ingreso,"
			+ "coalesce(getconsecutivoingreso(c.id_ingreso), '') AS ingconsecutivo, "
			+ "p.tipo_identificacion || ' ' || p.numero_identificacion as documentopaciente,"
			+ "p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre as nombrepaciente,"
			+ "p.codigo as codigopersona,"
			+ "p.fecha_nacimiento as fechanacimiento, "
			+ "vi.nombre as via_ingreso, " 
			+ "vi.codigo as codigo_via_ingreso," 
			+ "CASE " 
			+ "    WHEN eg.fecha_egreso is null " 
			+ "    THEN 0 " 
			+ "    ELSE 1 " 
			+ "END as tieneegreso "
			+ "FROM "
			+ "solicitudes s " + "INNER JOIN " + "cuentas c ON c.id=s.cuenta "
			+ "INNER JOIN " + "personas p ON c.codigo_paciente=p.codigo "
			+ "INNER JOIN vias_ingreso vi " 
			+ "ON vi.codigo = c.via_ingreso " 
			+ "LEFT JOIN egresos eg " 
			+ "ON eg.cuenta = c.id "
			+ "WHERE " + "s.numero_solicitud=? ";

	private static String cadenaConsultaDatosMedico = "SELECT "
			+ " m.numero_registro as registromedico,"
			+ " p.tipo_identificacion || ' ' || p.numero_identificacion as documentomedico,"
			+ " p.primer_apellido || ' ' || p.segundo_apellido || ' ' || p.primer_nombre || ' ' || p.segundo_nombre as nombremedico, "
			+ " getespecialidadesmedico(u.login, ', ') as especialidad,"
			+ " jaf.profesional_responsable as codigo_medico,m.firma_digital "
			+ "FROM "
			+ "	justificacion_art_sol jas  "
			+ "INNER JOIN "
			+ " justificacion_art_fijo jaf ON jas.codigo=jaf.justificacion_art_sol "
			+ "INNER JOIN "
			+ " medicos m ON jaf.profesional_responsable = m.codigo_medico "
			+ "INNER JOIN "
			+ " personas p ON jaf.profesional_responsable = p.codigo "
			+ "INNER JOIN "
			+ " usuarios u ON jaf.profesional_responsable = u.codigo_persona ";

	private static String cadenaDatosMedicamentoPos = " SELECT "
			+ " ds.dosis as dosificacion, "
			+ " gettotaladmin(ds.articulo, ds.numero_solicitud) as cantidad, "
			+ " getformafarmaceuticaarticulo(ds.articulo) || '-' || getconcentracionarticulo(ds.articulo) as ffconcentracion, "
			+ " getdosisdiaria(ds.articulo, ds.numero_solicitud) as dosisdiaria, "
			+ " gettiempotratamiento(ds.articulo, ds.numero_solicitud) as tiempotratamiento, "
			+ " ' ' as respuestaclinica, " + " ds.dosis as dosis, "
			+ " ds.frecuencia as frecuencia, "
			+ " ds.tipo_frecuencia as tipofrecuencia, "
			+ " getformafarmaceuticaarticulo(ds.articulo) as ffarma, "
			+ " getconcentracionarticulo(ds.articulo) as concentracion, "
			+ " coalesce(ds.unidosis_articulo, -1) as unidosis " + " FROM "
			+ "	detalle_solicitudes ds" + " WHERE ";

	private static String cadenaConsultaDiagnosticos = "SELECT "
			+ "j.codigo as codigojus,"
			+ "j.acronimo_dx as acronimo,"
			+ "j.tipo_cie, "
			+ "j.tipo_dx, "
			+ "d.nombre "
			+ "FROM "
			+ "justificacion_art_dx j "
			+ "INNER JOIN "
			+ "diagnosticos d ON d.acronimo = j.acronimo_dx AND d.tipo_cie = j.tipo_cie "
			+ "WHERE " + "j.justificacion_art_sol=?";

	private static String[] indicesMap2 = { "subcuenta_", "nombre_" };

	private static String[] indicesMapDistri = { "codigo_" };

	private static String justificacionDistri = "SELECT jar.codigo "
			+ "FROM justificacion_art_resp jar "
			+ "INNER JOIN justificacion_art_sol jas ON (jar.justificacion_art_sol=jas.codigo) "
			+ "INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo "
			+ "WHERE jar.subcuenta=? AND SJ.numero_solicitud=? AND jas.articulo=? ";

	private static String borrarJustificacionDisti = "DELETE FROM justificacion_art_resp WHERE codigo=? ";

	private static String consultaJustificacionAnterior = "SELECT codigo FROM justificacion_art_sol jas " +
			"INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo " +
			"WHERE SJ.numero_solicitud=? AND jas.articulo=? ";

	private static String insertarJustificacionDisti = "INSERT INTO justificacion_art_resp (estado, justificacion_art_sol, subcuenta, cantidad, codigo) "
			+ "VALUES (?,?,?,?,?) ";

	private static String insertarJusPStr = "INSERT INTO jus_pendiente_articulos VALUES (?,?,CURRENT_DATE,"
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?,'MEDI') ";

	private static String consultaJusPStr = "SELECT * FROM jus_pendiente_articulos WHERE numero_solicitud=? AND articulo=? ";

	private static String consultaStrNaturalezaArt = "SELECT na.es_medicamento AS naturalezaa from articulo a INNER JOIN naturaleza_articulo na ON "
			+ "(a.naturaleza = na.acronimo AND a.institucion = na.institucion) "
			+ "WHERE a.codigo = ?";

	private static String[] indicesMapCons = { "sub_cuenta_", "codigo_", "pos_" };

	private static String consultaNoPosArtConv = "SELECT sb.sub_cuenta, c.codigo, (SELECT naturaleza FROM articulo WHERE codigo=?) AS pos "
			+ "FROM sub_cuentas sb "
			+ "INNER JOIN convenios c ON (sb.convenio=c.codigo) "
			+ "WHERE sb.sub_cuenta=? AND c.requiere_justificacion_art = 'S' ";

	private static String consultaNoPosArtConv2 = "SELECT sb.sub_cuenta, c.codigo, (SELECT naturaleza FROM articulo WHERE codigo=?) AS pos "
			+ "FROM sub_cuentas sb "
			+ "INNER JOIN convenios c ON (sb.convenio=c.codigo) "
			+ "WHERE sb.sub_cuenta=? "
			+ "AND c.req_just_art_nopos_dif_med = 'S' ";

	// Metodos de la clase
	
	
	
	
	public static  List<String> consultarValoresRadioInsumos(Connection con,Integer codigoSolicitudOrden, Integer codigoJustificacion, boolean esOrdenAmbulatoria){

		List<String> listavaloresRadio = new ArrayList<String>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			String consulta = "   SELECT  valor   "+ 
			"   FROM inventarios.justificacion_art_sol jas "+
			"    INNER JOIN inventarios.justificacion_art_param jap "+
							  "    ON (jas.codigo=jap.justificacion_art_sol) ";
			
			/*if(esOrdenAmbulatoria){
				consulta += " WHERE jas.orden_ambulatoria = ? ";
			}else{
				consulta += " WHERE jas.numero_solicitud = ? ";
			}*/
			
			if(esOrdenAmbulatoria){
				consulta+=" INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = jas.codigo  " 
						+ "WHERE OAS.codigo_orden = ? ";
			}else{
				consulta+=" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo " 
						+ "WHERE SJ.numero_solicitud =  ? ";
			}
			consulta += " 	AND jas.codigo = ? "+
						" 	AND valor is not null "+
						" 	AND valor in ('"+ConstantesBD.acronimoSi+"','"+ConstantesBD.acronimoNo+"') "+
						" ORDER BY jap.codigo DESC ";

			ps = con.prepareStatement(consulta);
			ps.setInt(1, codigoSolicitudOrden);
			ps.setInt(2, codigoJustificacion);

			rs = ps.executeQuery();

			while(rs.next()){
				listavaloresRadio.add(rs.getString("valor"));
			}

		} catch (SQLException e){
            logger.error("ERROR SQLException consultarValoresRadioInsumos: ", e);

        }catch(Exception ex){
			logger.error("ERROR Exception consultarValoresRadioInsumos: ", ex);

        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
				if(rs != null){
					rs.close();
		}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement/ResultSet", se);
			}
		}
		return listavaloresRadio;

	}

	@SuppressWarnings("unused")
	public static HashMap obtenerFormularioParametrizado(Connection con,
			FormatoJustArtNopos fjan, PersonaBasica paciente,
			UsuarioBasico usuario) {
		HashMap formulario = new HashMap();
		formulario.put("numRegistros", 0);
		HashMap seccionesMap = cargarSeccionesFormularioParametrizado(con);
		formulario.put("mapasecciones", seccionesMap);

		HashMap mapaSeccionX = new HashMap();

		HashMap mapaPaciente = new HashMap();
		
		
		if(fjan!=null && fjan.getCodigoSolicitud()!=null && fjan.getCodigoJustificacion()!=null ){
			
			if(fjan.isProvieneOrdenAmbulatoria())
				formulario.put("valoresRadioInsumos",consultarValoresRadioInsumos(con, Integer.valueOf(fjan.getCodigoOrden().trim()),Integer.valueOf( fjan.getCodigoJustificacion().trim()),true));
			else
				formulario.put("valoresRadioInsumos",consultarValoresRadioInsumos(con, Integer.valueOf(fjan.getCodigoSolicitud().trim()),Integer.valueOf( fjan.getCodigoJustificacion().trim()),false));
		}

		formulario.put("codigoSolicitudNoPos", fjan.getCodigoSolicitud());
		formulario.put("codigoJustificacionNoPos",fjan.getCodigoJustificacion());
		
		// Cargando datos parametrizables del formualrio
		for (int i = 0; i < Utilidades.convertirAEntero(seccionesMap.get(
				"numRegistros").toString()); i++) {

			
			if(fjan.isProvieneOrdenAmbulatoria()){
				mapaSeccionX = cargarCamposSeccionX(con,seccionesMap.get("codigo_" + i).toString(), fjan.getCodigoOrden(), fjan.getCodigoJustificacion(), true);
			}else{
				mapaSeccionX = cargarCamposSeccionX(con,seccionesMap.get("codigo_" + i).toString(), fjan.getCodigoSolicitud(), fjan.getCodigoJustificacion(), false);
			}
			
			mapaSeccionX.put("seccionx", seccionesMap.get("codigo_" + i));
			// Cargar los campos hijos de los posibles check.
			if(fjan.isProvieneOrdenAmbulatoria()){
				mapaSeccionX = cargarCamposHijos(mapaSeccionX, con,fjan.getCodigoOrden(), fjan.getMedicamentoNoPos(),fjan.isProvieneOrdenAmbulatoria());
			}else{
				mapaSeccionX = cargarCamposHijos(mapaSeccionX, con,fjan.getCodigoSolicitud(), fjan.getMedicamentoNoPos(),fjan.isProvieneOrdenAmbulatoria());
			}
			// logger.info("MAPA SECCION "+i+"\n\n\n"+mapaSeccionX);

			// aqui va la asigancion de valores de mapaseccionx a formulario map
			// con hijos incluidos

			for (int y = 0; y < Utilidades.convertirAEntero(mapaSeccionX
					.get("numRegistros") + ""); y++) {
				formulario.put("campo_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("campo_" + y));
				formulario.put("seccion_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("seccion_" + y));
				formulario.put("tipo_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("tipo_" + y));
				formulario.put("requerido_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("requerido_" + y));
				formulario.put("valor_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("valor_" + y));
				formulario.put("mostrar_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("mostrar_" + y));
				formulario.put("etiquetacampo_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("etiquetacampo_" + y).toString()
								.replaceAll("\n", " "));
				formulario.put("valorcampo_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("valorcampo_" + y));
				formulario.put("opcioncampo_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("opcioncampo_" + y));
				formulario.put("etiquetaseccion_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("etiquetaseccion_" + y).toString()
								.replaceAll("\n", " "));
				formulario.put("tamanio_"
						+ mapaSeccionX.get("seccionx").toString() + "_" + y,
						mapaSeccionX.get("tamanio_" + y).toString()
								.replaceAll("\n", " "));
				formulario.put(
						"parametrizacionjus_"
								+ mapaSeccionX.get("seccionx").toString() + "_"
								+ y,
						mapaSeccionX.get("parametrizacionjus_" + y));
				formulario.put(
						"tieneaccion_"
								+ mapaSeccionX.get("seccionx").toString() + "_"
								+ y,
						mapaSeccionX.get("tieneaccion_" + y));
				
				formulario.put("seccionx_"
						+ mapaSeccionX.get("seccionx").toString(),
						mapaSeccionX.get("seccionx"));

				if (mapaSeccionX.containsKey("mapahijos_" + y)) {

					for (int g = 0; g < Utilidades
							.convertirAEntero(((HashMap) mapaSeccionX
									.get("mapahijos_" + y)).get("numRegistros")
									.toString()); g++) {
						// logger.info("<<<<<< TIENE HIJOS >>>>");
						formulario.put(
								"codigo_"
										+ mapaSeccionX.get("seccionx")
												.toString()
										+ "_"
										+ ((HashMap) mapaSeccionX
												.get("mapahijos_" + y)).get(
												"campopadre_" + g).toString()
										+ "_" + g,
								((HashMap) mapaSeccionX.get("mapahijos_" + y))
										.get("codigo_" + g).toString());
						formulario.put(
								"etiqueta_"
										+ mapaSeccionX.get("seccionx")
												.toString()
										+ "_"
										+ ((HashMap) mapaSeccionX
												.get("mapahijos_" + y)).get(
												"campopadre_" + g).toString()
										+ "_" + g,
								((HashMap) mapaSeccionX.get("mapahijos_" + y))
										.get("etiqueta_" + g).toString()
										.replaceAll("\n", " "));
						formulario.put(
								"campopadre_"
										+ mapaSeccionX.get("seccionx")
												.toString()
										+ "_"
										+ ((HashMap) mapaSeccionX
												.get("mapahijos_" + y)).get(
												"campopadre_" + g).toString()
										+ "_" + g,
								((HashMap) mapaSeccionX.get("mapahijos_" + y))
										.get("campopadre_" + g).toString());
						formulario.put(
								"valorcampo_"
										+ mapaSeccionX.get("seccionx")
												.toString()
										+ "_"
										+ ((HashMap) mapaSeccionX
												.get("mapahijos_" + y)).get(
												"campopadre_" + g).toString()
										+ "_" + g,
								((HashMap) mapaSeccionX.get("mapahijos_" + y))
										.get("valorcampo_" + g).toString());
					}
					formulario
							.put("numRegistros_"
									+ mapaSeccionX.get("seccionx").toString()
									+ "_"
									+ mapaSeccionX.get("campo_" + y).toString(),
									Utilidades
											.convertirAEntero(((HashMap) mapaSeccionX
													.get("mapahijos_" + y))
													.get("numRegistros")
													.toString()));

				}

				formulario.put(
						"numRegistros",
						Utilidades.convertirAEntero(formulario
								.get("numRegistros") + "") + 1);

			}
			formulario.put("numRegistros_"
					+ mapaSeccionX.get("seccionx").toString(),
					mapaSeccionX.get("numRegistros"));

		}
		int numR = Utilidades.convertirAEntero(formulario.get("numRegistros")
				+ "");
		// Cargando datos fijos del formulario dependiendo del flujo que llama
		// al formulario

		if (fjan.getEmisor().equals("solicitud")) {
			// logger.info("\n\n\n [SIN solicitud]"+fjan.getCodigoSolicitud());
			formulario = CargarDatosPaciente(con, paciente, usuario, formulario);

		} else if (fjan.getEmisor().equals("modificar")
				|| fjan.getEmisor().equals("consultar")) {
			logger.info("\n\n\n [CON solicitud]" + fjan.getCodigoSolicitud());

			if(fjan.isProvieneOrdenAmbulatoria()){
				formulario = CargarDatosPacienteSolicitud(con,fjan.getCodigoOrden(), formulario,fjan.getMedicamentoNoPos(),fjan.isProvieneOrdenAmbulatoria());
			}else{
				formulario = CargarDatosPacienteSolicitud(con,fjan.getCodigoSolicitud(), formulario,fjan.getMedicamentoNoPos(),fjan.isProvieneOrdenAmbulatoria());
			}	
				
		} else if (fjan.getEmisor().equals("ingresar")) {
			logger.info("\n\n\n [CON solicitud en ingresar]");
			formulario = CargarDatosPacienteSolicitud(con,fjan.getCodigoSolicitud(), formulario, paciente, usuario);
		}

		formulario.put("numRegistros", numR++);
		formulario.put("numSecciones", seccionesMap.get("numRegistros"));

		return formulario;
	}

	@SuppressWarnings("static-access")
	private static HashMap CargarDatosPacienteSolicitud(Connection con,
			String codigoSolicitud, HashMap formulario, PersonaBasica paciente,
			UsuarioBasico usuario) {

		HashMap mapaAux = new HashMap();
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(cadenaConsultaDatosPaciente,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codigoSolicitud));
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));

			formulario.put("nombre", mapaAux.get("nombrepaciente_0"));
			formulario.put("documento", mapaAux.get("documentopaciente_0"));
			formulario.put("ingreso", mapaAux.get("ingreso_0"));
			formulario.put("ingconsecutivo", mapaAux.get("ingconsecutivo_0"));
			formulario.put(
					"fecha",
					UtilidadFecha.conversionFormatoFechaAAp(mapaAux.get(
							"fecha_0").toString()));
			formulario.put("centrocosto", mapaAux.get("servsolicitadoen_0"));
			formulario.put("viaIngreso", mapaAux.get("via_ingreso_0"));
			formulario.put("codigoViaIngreso", mapaAux.get("codigo_via_ingreso_0").toString());
			
			/*Segun DCU-583 solo se debe permitir activar el campo tiempo tratamiento en la consulta y/o modificacion*/
			formulario.put("tieneegreso","0");
			
			formulario.put("codigopersona", mapaAux.get("codigopersona_0"));
			formulario.put(
					"fechanacimiento",
					UtilidadFecha.conversionFormatoFechaAAp(mapaAux.get(
							"fechanacimiento_0").toString()));
			// logger.info("\n\n\n\n [consultaDatosPaciente] "+cadenaConsultaDatosPaciente);

			// calculo de edad

			formulario.put("edad", UtilidadFecha.calcularEdadDetallada(
					formulario.get("fechanacimiento").toString(), formulario
							.get("fecha").toString()));
		} catch (Exception e) {
			logger.info("[ErrorConsultaDatosPaciente Ingresar]" + e);
		}

		formulario.put("nommedico", usuario.getNombreUsuario());
		formulario
				.put("especialidad", usuario.getEspecialidadesMedico(usuario));
		formulario.put("tipoid", usuario.getCodigoTipoIdentificacion() + "-"
				+ usuario.getNumeroIdentificacion());
		formulario.put("registromed", usuario.getNumeroRegistroMedico());
		formulario.put("codcuenta", paciente.getCodigoCuenta());
		formulario.put("codigomed", usuario.getCodigoPersona());

		HashMap mapa1 = new HashMap();
		mapa1.put("numRegistros", 0);
		String consulta = "SELECT max(codigo) as codigo from justificacion_art_sol ";
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}

		if ((mapa1.get("codigo_0").toString().equals(""))) {
			//formulario.put("consecutivojus", 0);
		} else {
			//formulario.put("consecutivojus", Utilidades.convertirAEntero(mapa1
				//	.get("codigo_0").toString()) + 1);
		}
		formulario
				.put("estadojus",
						ValoresPorDefecto
								.getIntegridadDominio(ConstantesIntegridadDominio.acronimoJustificado));
		formulario.put("codigoestadojus",
				ConstantesIntegridadDominio.acronimoJustificado);

		return formulario;
	}

	/**
	 * Metodo para cargar los datos fijos (seccion paciente- seccion medico) del
	 * formulario cuando el flujo hace necesario cargarlos dependiendo del
	 * numero de solicitud o de la orden ambulatoria
	 * 
	 * @param codigoSolicitudOrden
	 * @param formulario
	 * @param string
	 * @param esOrdenAmbulatoria
	 * @return
	 */
	private static HashMap CargarDatosPacienteSolicitud(Connection con,
			String codigoSolicitudOrden, HashMap formulario, String articulo, boolean esOrdenAmbulatoria) {
		logger.info("===> El artículo en CargarDatosPacienteSolicitud es = "
				+ articulo);

		HashMap mapaAux = new HashMap();
		HashMap mapaAux1 = new HashMap();
		
		PreparedStatementDecorator ps = null;
		PreparedStatementDecorator ps1 = null;
		PreparedStatementDecorator ps5 = null;
		
		try {

			ps = new PreparedStatementDecorator(
					con.prepareStatement(esOrdenAmbulatoria ? cadenaConsultaDatosPacienteXOrden : cadenaConsultaDatosPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, Utilidades.convertirAEntero(codigoSolicitudOrden));
			
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

			formulario.put("nombre", mapaAux.get("nombrepaciente_0"));
			formulario.put("documento", mapaAux.get("documentopaciente_0"));
			formulario.put("ingreso", mapaAux.get("ingreso_0"));
			formulario.put("ingconsecutivo", mapaAux.get("ingconsecutivo_0")+ "");

			formulario.put("fecha",UtilidadFecha.conversionFormatoFechaAAp(mapaAux.get("fecha_0").toString()));
			formulario.put("centrocosto", mapaAux.get("servsolicitadoen_0"));
			
			formulario.put("viaIngreso", mapaAux.get("via_ingreso_0"));
			formulario.put("codigoViaIngreso", mapaAux.get("codigo_via_ingreso_0").toString());
			
			/*Segun DCU-583 solo se debe permitir activar el campo tiempo tratamiento en la consulta y/o modificacion*/
			formulario.put("tieneegreso",mapaAux.get("tieneegreso_0").toString());
			
			formulario.put("codigopersona", mapaAux.get("codigopersona_0"));
			formulario.put("fechanacimiento",UtilidadFecha.conversionFormatoFechaAAp(mapaAux.get("fechanacimiento_0").toString()));

			// calculo de edad

			formulario.put("edad", UtilidadFecha.calcularEdadDetallada(formulario.get("fechanacimiento").toString(), formulario.get("fecha").toString()));


			String cadena = cadenaConsultaDatosMedico;
			//cadena += esOrdenAmbulatoria == true ? " WHERE jas.orden_ambulatoria = ? " : " WHERE jas.numero_solicitud = ? ";
			if(esOrdenAmbulatoria){
				cadena+=" INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = jas.codigo  " +
						" WHERE OAS.codigo_orden = ? ";
			}else{
				cadena+=" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo " +
						" WHERE SJ.numero_solicitud = ? ";
			}
			
			
			
			ps1 = new PreparedStatementDecorator(
					con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps1.setInt(1, Utilidades.convertirAEntero(codigoSolicitudOrden));
			
			mapaAux1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery()));

			formulario.put("nommedico", mapaAux1.get("nombremedico_0"));
			formulario.put("especialidad", mapaAux1.get("especialidad_0"));
			formulario.put("tipoid", mapaAux1.get("documentomedico_0"));
			formulario.put("registromed", mapaAux1.get("registromedico_0"));
			formulario.put("codigomed", mapaAux1.get("codigo_medico_0"));
			formulario.put("firmadigital", mapaAux1.get("firma_digital_0"));

			HashMap mapa1 = new HashMap();
			mapa1.put("numRegistros", 0);
			String consulta = " SELECT "
					+ "jas.consecutivo, "
					+ "jar.estado, " 
					+ "CASE " +
						"WHEN jas.ORDEN_AMBULATORIA is null " +
						"THEN '" +ConstantesBD.acronimoNo+"' " +
						"ELSE '" +ConstantesBD.acronimoSi+"' "
					+ "END as es_orden_ambulatoria "
								+ "FROM "
					+ "justificacion_art_sol jas "
								+ "INNER JOIN "
								+ "justificacion_art_resp jar ON (jas.codigo=jar.justificacion_art_sol) ";
								//+ "WHERE " + "jas.articulo=" + articulo + " AND ";
								
			//consulta += esOrdenAmbulatoria == true ? "jas.orden_ambulatoria = " + codigoSolicitudOrden : "jas.numero_solicitud = " + codigoSolicitudOrden;				
			
			if(esOrdenAmbulatoria){
				consulta+=" INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = jas.codigo  " 
						+ "WHERE jas.articulo=" + articulo 
						+ " AND OAS.codigo_orden = ? ";
			}else{
				consulta+=" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo " 
						+ "WHERE jas.articulo=" + articulo 
						+ " AND SJ.numero_solicitud = ? ";
			}
			
			try {
				ps5 = new PreparedStatementDecorator(
						con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps5.setInt(1, Utilidades.convertirAEntero(codigoSolicitudOrden));
				
				mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps5.executeQuery()));
			
			} catch (SQLException e) {
				logger.info("ERROR CargarDatosPacienteSolicitud" + e);
			}
			logger.info("[Consulta justificacion_art_sol]" + consulta);
			
			formulario.put("consecutivojus", Utilidades.convertirAEntero(mapa1.get("consecutivo_0").toString()));
			formulario.put("estadojus", mapa1.get("estado_0").toString());
			formulario.put("esOrdenAmbulatoria",mapa1.get("es_orden_ambulatoria_0"));

		} catch (SQLException e) {
            logger.error("ERROR SQLException CargarDatosPacienteSolicitud: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception CargarDatosPacienteSolicitud: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
				if(ps1 != null){
					ps1.close();
		}
				if(ps5 != null){
					ps5.close();
				}
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}

		return formulario;
	}

	/**
	 * Metodo que carga los campos hijos de los posibles check o select con sus
	 * respectivos valores y los inserta en un mapa en el mapa de seccion
	 * 
	 * @param mapaSeccionX
	 * @return
	 */
	private static HashMap cargarCamposHijos(HashMap mapaSeccionX,Connection con, String codigoSolicitudOrden, String articulo, boolean esOrdenAmbulatoria) {

		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");

		PreparedStatementDecorator ps = null;

		String cadena = "SELECT "
							+ "codigo, "
							+ "etiqueta, "
							+ "campo_padre  as campopadre, ";
		
					if(esOrdenAmbulatoria){	
						cadena += "coalesce (getobtenerValorCampoXOrden1(codigo,?,?),coalesce(getobtenerValorDefectoCampo(codigo),'N')) as valorcampo ";
					}else{
						cadena += "coalesce (getobtenerValorCampo1(codigo,?,?),coalesce(getobtenerValorDefectoCampo(codigo),'N')) as valorcampo ";
					}	
						cadena += "FROM campos_jus "
								+ "WHERE "
									+ " campo_padre=? AND servicio='N' AND tipo_jus = 'MEDI'";
		
		try {
			for (int i = 0; i < Utilidades.convertirAEntero(mapaSeccionX.get("numRegistros") + ""); i++) {
				ps = new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				/**
				 * SELECT codigo, etiqueta,campo_padre as campopadre,coalesce
				 * (getobtenerValorCampo1(codigo,?,?),
				 * coalesce(getobtenerValorDefectoCampo(codigo),'N')) as
				 * valorcampo FROM "campos_jus WHERE campo_padre=? and
				 * servicio='N'
				 */

				ps.setInt(1, Utilidades.convertirAEntero(codigoSolicitudOrden));
				ps.setInt(2, Utilidades.convertirAEntero(articulo));
				ps.setInt(3, Utilidades.convertirAEntero(mapaSeccionX.get("campo_"+ i)+ ""));
				
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				
				if (Utilidades.convertirAEntero(mapa.get("numRegistros") + "") > 0)
					mapaSeccionX.put("mapahijos_" + i, mapa);

			}
		} catch (SQLException e){
            logger.error("ERROR SQLException cargarCamposHijos: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception cargarCamposHijos: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}

		return mapaSeccionX;
	}

	/**
	 * Metodo que carga los datos fijos de la seccion informacion del paciente y
	 * del medico, tomando los datos en sesion
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	private static HashMap CargarDatosPaciente(Connection con,
			PersonaBasica pacienteCargado, UsuarioBasico usuario, HashMap mapa) {

		logger.info("vvvvvvvvvvv");
		logger.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
		logger.info("Id Ingreso del Paciente : "
				+ pacienteCargado.getCodigoIngreso());
		logger.info("Cosnecutivo Ingreso del Paciente : "
				+ pacienteCargado.getConsecutivoIngreso());
		// Utilidades.imprimirMapa(mapa);

		mapa.put("nombre", pacienteCargado.getApellidosNombresPersona());
		mapa.put("documento",
				pacienteCargado.getCodigoTipoIdentificacionPersona() + " "
						+ pacienteCargado.getNumeroIdentificacionPersona());
		mapa.put("ingreso", pacienteCargado.getCodigoIngreso());
		mapa.put("ingconsecutivo", pacienteCargado.getConsecutivoIngreso());
		mapa.put("fecha", UtilidadFecha.getFechaActual());
		mapa.put(
				"edad",
				UtilidadFecha.calcularEdadDetallada(
						pacienteCargado.getFechaNacimiento(),
						UtilidadFecha.getFechaActual()));
		Cuenta cuenta=new Cuenta();
		String[] valores=null;
		try {
			valores = cuenta.consultarAreaYViaIngreso(con, pacienteCargado.getCodigoCuenta());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(valores!=null){
			mapa.put("codigoCentroCosto",valores[0]);
			mapa.put("centrocosto",valores[1] );
			mapa.put("codigoViaIngreso",valores[2]);
			mapa.put("viaIngreso",valores[3]);
		}else{
			mapa.put("codigoCentroCosto",null);
			mapa.put("centrocosto","" );
			mapa.put("codigoViaIngreso",null);
			mapa.put("viaIngreso",null);
		}
		
		mapa.put("tieneegreso","0");
		
		mapa.put("nommedico", usuario.getNombreUsuario());
		mapa.put("especialidad", usuario.getEspecialidadesMedico(usuario));
		mapa.put("tipoid", usuario.getCodigoTipoIdentificacion() + "-"
				+ usuario.getNumeroIdentificacion());
		mapa.put("registromed", usuario.getNumeroRegistroMedico());
		mapa.put("codigopersona", pacienteCargado.getCodigoPersona());
		mapa.put("codcuenta", pacienteCargado.getCodigoCuenta());
		mapa.put("codigomed", usuario.getCodigoPersona());

		/*HashMap mapa1 = new HashMap();
		mapa.put("numRegistros", 0);
//		String consulta = "SELECT max(codigo) as codigo from justificacion_art_sol ";
		String consulta = "select consecutivo as codigo from justificacion_art_sol  where codigo =(SELECT max(codigo) from justificacion_art_sol )";
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}

		if ((mapa1.get("codigo_0").toString().equals(""))) {
			//mapa.put("consecutivojus", 0);
		} else {
			//mapa.put("consecutivojus", Utilidades.convertirAEntero(mapa1.get(
				//	"codigo_0").toString()) + 1);
		}*/

		mapa.put(
				"estadojus",
				ValoresPorDefecto
						.getIntegridadDominio(ConstantesIntegridadDominio.acronimoJustificado));
		mapa.put("codigoestadojus",
				ConstantesIntegridadDominio.acronimoJustificado);

		return mapa;
	}

	/**
	 * Metodo para cargar los campos de cada una de las secciones visibles segun
	 * la parametrizacion
	 * 
	 * @param con
	 * @param object
	 * @return
	 */
	private static HashMap cargarCamposSeccionX(Connection con,
			String codigoSeccion, String codigoSolicitudOrden,String codigoJus, boolean esOrdenAmbulatoria) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		
		PreparedStatementDecorator ps = null;
		
		String consulta = "SELECT "
				+ " pj.codigo as parametrizacionjus,"
				+ " pj.requerido, "
				+ " pj.tipo_html AS tipo,"
				+ " pj.seccion, "
				+ " pj.campo, "
				+ " pj.mostrar, "
				+ " getobtenerEtiquetaSeccion(pj.seccion) as etiquetaseccion, "
				+ " getobtenerEtiquetaCampo(pj.campo, 'N') as etiquetacampo, "
				+ " coalesce (getobtenerOpcionCampo(pj.campo),'null') as opcioncampo, ";
		
			if(esOrdenAmbulatoria){
				consulta += " coalesce (getobtenerValorCampoXOrden(pj.campo,?,?),getobtenerValorDefectoCampo(pj.campo)) as valorcampo, ";
			}else{
				consulta += " coalesce (getobtenerValorCampo(pj.campo,?,?),getobtenerValorDefectoCampo(pj.campo)) as valorcampo, ";
			}
			
			consulta +=" cj.tamanio as tamanio, " +
					"CASE " +
						//"WHEN ac.CODIGO is not null " +
						"WHEN (SELECT count (*) from acciones_campos ac where ac.campo_accion = cj.codigo and ac.SERVICIO_CAMPO_ACCION = cj.servicio)>0 "+
						"THEN '" +ConstantesBD.acronimoSi+"' "+
						"ELSE '" +ConstantesBD.acronimoNo+"' "+
					"END as tieneaccion "
				+ "  FROM  "
				+ " parametrizacion_jus pj "
				+ "	inner join "
				+ " campos_jus cj ON (pj.campo=cj.codigo and pj.servicio=cj.servicio) " 
				//+ " LEFT JOIN acciones_campos ac on (ac.campo_accion = cj.codigo and ac.SERVICIO_CAMPO_ACCION = cj.servicio) "
				+ " WHERE "
				+ "  pj.mostrar='S' and pj.seccion=? and pj.servicio='N' and cj.servicio='N'"
				+ " ORDER BY  " + " pj.seccion, pj.orden ";
		
		try {
			ps = new PreparedStatementDecorator(
					con.prepareStatement(
							consulta,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			// logger.info("CONSULTA PARA CARGAR MAPA SX \n\n\n"+cadenaConsultaParametrizacionFormulario+codigoSeccion+" - "+solicitud);
			ps.setDouble(1, Utilidades.convertirADouble(codigoSolicitudOrden));
			ps.setDouble(2, Utilidades.convertirADouble(codigoJus));
			ps.setDouble(3, Utilidades.convertirADouble(codigoSeccion));
			
			logger.info(consulta);
						
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));

		} catch (SQLException e){
            logger.error("ERROR SQLException cargarCamposSeccionX: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception cargarCamposSeccionX: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
		}
		}
		return mapa;
	}

	/**
	 * 
	 * Metodo para cargar las secciones parametrizadas del formulario
	 * 
	 * @param con
	 * @return
	 */
	private static HashMap cargarSeccionesFormularioParametrizado(Connection con) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(
							cadenaConsultaParametrizacionSecciones,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			logger.info("-->" + cadenaConsultaParametrizacionSecciones);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (SQLException e) {
			logger.info("ERROR cargarSeccionesFormularioParametrizado " + e);
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static boolean existejustificacion(Connection con,
			FormatoJustArtNopos fjan, PersonaBasica paciente) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		String cadena = "SELECT "
				+ " jas.fecha_modifica as fecham,"
				+ " jas.tiempo_tratamiento as tiempot "
				+ " FROM "
				+ "	justificacion_art_sol jas "
				+ " INNER JOIN "
				+ "	admin_medicamentos am ON (jas.numero_solicitud=am.numero_solicitud) "
				+ " INNER JOIN "
				+ "	detalle_admin da ON (da.administracion=am.codigo) "
				+ " WHERE "
				+ " jas.articulo="
				+ fjan.getMedicamentoNoPos()
				+ " "
				+ " and jas.dosis="
				+ fjan.getDosis()
				+ " "
				+ " and jas.frecuencia="
				+ fjan.getFrecuencia()
				+ " "
				+ " and da.fecha>="
				+ UtilidadFecha.incrementarDiasAFecha(UtilidadFecha
						.conversionFormatoFechaABD(UtilidadFecha
								.getFechaActual(con)), -1, true) + " "
				+ " ORDER BY " + " jas.fecha_modifica DESC ";

		logger.info("consulta validacion 1 tiempo tratamiento" + cadena);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(cadena, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (SQLException e) {
			logger.info("ERROR existejustificacion " + e);
		}
		// logger.info("hay justificaciones >>>"+mapa);
		if (Utilidades.convertirAEntero(mapa.get("numRegistros") + "") > 0) {
			logger.info("hay registros de justificacion");
			int dias = UtilidadFecha.numeroDiasEntreFechas(mapa.get("fecham_0")
					+ "", UtilidadFecha.getFechaActual());
			if (Utilidades.convertirAEntero(mapa.get("tiempot_0") + "") < dias) {
				// logger.info("toca nueva justificacion");
				return true;
			}
		} else {
			return false;
		}

		return false;

	}

	/**
	 * 
	 * @param con
	 * @param numJustificacion
	 * @return
	 */
	public static HashMap consultarDiagnosticos(Connection con,
			int numJustificacion) {
		HashMap mapa = new HashMap();
		HashMap mapaAux = new HashMap();
		mapaAux.put("numRegistros", 0);
		String dxComplicacion = "";
		HashMap dxDefinitivos = new HashMap();
		int countRelacionados = 0;
		String seleccionados = "";
		int i = 0;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(cadenaConsultaDiagnosticos,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(numJustificacion + ""));
			// logger.info("consulta diagnosticos >>>>>>"+cadenaConsultaDiagnosticos+"----"+numJustificacion);
			mapaAux = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
			// logger.info("mapa diagnosticos >>>"+mapaAux);
			for (i = 0; i < Utilidades.convertirAEntero(mapaAux.get(
					"numRegistros").toString()); i++) {
				if (mapaAux.get("tipo_dx_" + i).toString().equals("RELA")) {
					dxDefinitivos.put(
							"relacionado_" + countRelacionados,
							mapaAux.get("acronimo_" + i)
									+ ConstantesBD.separadorSplit
									+ mapaAux.get("tipo_cie_" + i)
									+ ConstantesBD.separadorSplit
									+ mapaAux.get("nombre_" + i));
					dxDefinitivos.put("checkbox_" + countRelacionados, "true");
					seleccionados = seleccionados + "'"
							+ mapaAux.get("acronimo_" + i) + "',";
					countRelacionados++;
				} else if (mapaAux.get("tipo_dx_" + i).toString()
						.equals("COMP"))
					dxComplicacion = mapaAux.get("acronimo_" + i)
							+ ConstantesBD.separadorSplit
							+ mapaAux.get("tipo_cie_" + i)
							+ ConstantesBD.separadorSplit
							+ mapaAux.get("nombre_" + i);
				else if (mapaAux.get("tipo_dx_" + i).toString().equals("PRIN"))
					dxDefinitivos.put(
							"principal",
							mapaAux.get("acronimo_" + i)
									+ ConstantesBD.separadorSplit
									+ mapaAux.get("tipo_cie_" + i)
									+ ConstantesBD.separadorSplit
									+ mapaAux.get("nombre_" + i));
			}
			seleccionados = seleccionados + "'-1'";

			dxDefinitivos.put("seleccionados", seleccionados);
			mapa.put("diagnosticoComplicacion", dxComplicacion);
			mapa.put("diagnosticosDefinitivos", dxDefinitivos);
			mapa.put("numDiagnosticosDefinitivos", countRelacionados);

		} catch (SQLException e) {
			logger.info("ERROR consultarDiagnosticos " + e);
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap cargarMedicamentosNoPos(Connection con,
			FormatoJustArtNopos fjan, PersonaBasica paciente) {

		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("<<Cargar Medicamentos NO POS. ");
		logger.info("<<Emisor: " + fjan.getEmisor());

		HashMap mapa = new HashMap();
		mapa.put("numRegistros", 0);
		String adjuntos = "";
		String consulta = "";
		HashMap m = new HashMap();

		if (fjan.getEmisor().equals("solicitud")
				|| fjan.getEmisor().equals("ingresar")
				) {
			consulta = "select"
					+ "	a.descripcion as nombre,"
					+ "	a.codigo as codigoarticulo,"
					+ "	getformafarmaceuticaarticulo(a.codigo) || '-' || getconcentracionarticulo(a.codigo) as ffconcentracion,"
					+ "	getgrupoterapeutico(a.subgrupo) as grupoterapeutico,"
					+ "	getgrupoterapeuticocodigo(a.subgrupo) as codigogrupotera, "
					+ "	aim.tiempo_resp_esperado as tiemporespuestaesperado, "
					+ "	a.registro_invima as registroinvima, "
					+ "	replace(aim.efecto_deseado, '\n', ' ') as efectodeseadotratamiento, "
					+ "	replace(aim.efecto_secundario, '\n', ' ') as efectossecundarios, "
					+ "	replace(aim.bibliografia, '\n', ' ') as bibliografia, "
					+ "	replace(aim.observaciones, '\n', ' ') as observaciones,"
					+ "	getformafarmaceuticaarticulo(a.codigo) as ffarma,"
					+ "	getconcentracionarticulo(a.codigo) as concentracion, getNombreTipoFrecuencia2(?) as nombretipofrecuencia "
					+ "   FROM "
					+ "	articulo a"
					+ "	left outer join "
					+ "	articulos_info_medica aim on (a.codigo=aim.codigo_articulo) "
					+ "   where" + "	codigo=" + fjan.getMedicamentoNoPos();

			try {
				
				String adj = "select nombre_archivo from doc_adj_articulo where cod_articulo="
						+ fjan.getMedicamentoNoPos();
				PreparedStatementDecorator ps = new PreparedStatementDecorator(
						con.prepareStatement(adj, ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));
				
				m = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
						.executeQuery()));
				// logger.info("consulta adjuntos >>>"+adj);

				for (int j = 0; j < Utilidades.convertirAEntero(m.get(
						"numRegistros").toString()); j++) {
					if (j == 0)
						adjuntos += m.get("nombre_archivo_" + j);
					else
						adjuntos += " - " + m.get("nombre_archivo_" + j);
				}

			} catch (Exception e) {
				logger.info("error cargando documentos adjuntos >>>" + e);
			}
		}

		else if (fjan.getEmisor().equals("modificar")
				|| fjan.getEmisor().equals("consultar")
				) {
			
			
			
			if(fjan.isProvieneOrdenAmbulatoria()){
				consulta =" SELECT  "
						+ " 	janp.codigo as codigojus, "
						+ "		janp.consecutivo as consecutivojus, "
						+ "		getdescripcionarticulo(janp.articulo) as nombre,"
						+ " 	janp.articulo as codigoarticulo,"
						+ " 	janp.forma_farmaceutica || '-' || janp.concentracion as ffconcentracion, "
						+ " 	janp.grupo_terapeutico as codigogrupotera, "
						+ "		inventarios.getnombregrupoterapeutico(janp.grupo_terapeutico,getdescripcionarticulo(janp.articulo)) AS grupoterapeutico, "
						+ " 	janp.tiempo_respuesta as tiemporespuestaesperado,"
						+ " 	janp.registro_invima as registroinvima, "
						+ "		janp.efecto_deseado as efectodeseadotratamiento, "
						+ "		janp.efectos_secundarios as efectossecundarios,"
						+ "		janp.bibliografia as bibliografia,"
						+ " 	CASE WHEN janp.dosis <> 0 AND janp.frecuencia <> 0 THEN janp.dosis || '' ELSE janp.dosificacion END as dosificacion, "
						+ "		jas.cantidad as cantidad,"
						
						+ " 	janp.tiempo_tratamiento as tiempotratamiento, "
						
						+ "		janp.codigo as codigojustificacion,"
						+ "		janp.forma_farmaceutica as ffarma,"
						+ "		janp.concentracion  as concentracion,"
						+ "		janp.unidosis as unidosis,"
						+ "		janp.unidosis as unidosisl,"
						+ "		janp.frecuencia as frecuencia,"
						+ "		janp.tipo_frecuencia as tipofrecuencia,"
						+ "		janp.documentosadj as adjuntos, "
						+ "		replace(aim.observaciones, '\n', ' ') as observaciones "
						+ " FROM  "
						+ "		justificacion_art_sol janp "
						+ "		INNER JOIN  justificacion_art_resp jas on (janp.codigo=jas.justificacion_art_sol)"
						+ "     INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = janp.codigo "
						+ "		INNER JOIN ordenes_ambulatorias oa ON (oa.codigo=OAS.CODIGO_ORDEN) "
						+ "		INNER JOIN cuentas c ON c.id=oa.cuenta_solicitante "
						+ "		INNER JOIN vias_ingreso vi ON vi.codigo = c.via_ingreso "
						+ "		LEFT JOIN egresos eg ON eg.cuenta = c.id "
						+ "		LEFT JOIN articulos_info_medica aim ON (janp.articulo=aim.codigo_articulo) " 
						+ " WHERE oa.codigo = "+ fjan.getCodigoOrden() 
						+ "		and jas.subcuenta = "+ fjan.getSubcuenta() 
						+ " 	and janp.articulo = "+ fjan.getMedicamentoNoPos();
				
			}else{
			String consultaTiempoTratamiento="SELECT count(*) from " +
					"(SELECT " +
					"to_char(da.fecha,'YYYY-MM-DD') as fecha " +
					"FROM detalle_admin da " +
					"inner join admin_medicamentos am " +
					"on da.administracion = am.codigo " +
					"where  am.numero_solicitud = " +fjan.getCodigoSolicitud()+
					" group by da.fecha ) t_tratamiento ";
			
			consulta = "SELECT  "
					+ " 	janp.codigo as codigojus, "
					+ "	janp.consecutivo as consecutivojus, "
					+ "	getdescripcionarticulo(janp.articulo) as nombre,"
					+ " 	janp.articulo as codigoarticulo,"
					+ " 	janp.forma_farmaceutica || '-' || janp.concentracion as ffconcentracion, "
					+ " 	janp.grupo_terapeutico as codigogrupotera, "
					+ "	inventarios.getnombregrupoterapeutico(janp.grupo_terapeutico,getdescripcionarticulo(janp.articulo)) AS grupoterapeutico, "
					+ " 	janp.tiempo_respuesta as tiemporespuestaesperado,"
					+ " 	janp.registro_invima as registroinvima, "
					+ "	janp.efecto_deseado as efectodeseadotratamiento, "
					+ "	janp.efectos_secundarios as efectossecundarios,"
					+ "	janp.bibliografia as bibliografia,"
					+ " 	CASE WHEN janp.dosis <> 0 AND janp.frecuencia <> 0 THEN "
					+ " 		janp.dosis || ''  "
					+ "	ELSE janp.dosificacion END as dosificacion, "
					+ "	jas.cantidad as cantidad,"
					
					+ "CASE " +
						"WHEN eg.fecha_egreso is null " +
							"and (vi.codigo="+ConstantesBD.codigoViaIngresoUrgencias+" OR vi.codigo="+ConstantesBD.codigoViaIngresoHospitalizacion+")"+
						"THEN "+
						    "janp.tiempo_tratamiento " +
						"ELSE " 
						+ "CASE " +
								"WHEN ("+consultaTiempoTratamiento+")>0 " +
								"THEN ("+consultaTiempoTratamiento+") " +
								"ELSE janp.tiempo_tratamiento "
							+ "END	 " +
					"END "
					+ "as tiempotratamiento,"
					
					+ "	janp.codigo as codigojustificacion,"
					+ "	janp.forma_farmaceutica as ffarma,"
					+ "	janp.concentracion  as concentracion,"
					+ "	janp.unidosis as unidosis,"
					+ "	janp.unidosis as unidosisl,"
					+ "	janp.frecuencia as frecuencia,"
					+ "	janp.tipo_frecuencia as tipofrecuencia,"
					+ "	janp.documentosadj as adjuntos, "
					+ "	replace(aim.observaciones, '\n', ' ') as observaciones "
					+ " FROM  "
					+ "	justificacion_art_sol janp "
					+ "INNER JOIN  "
					+ "	justificacion_art_resp jas on (janp.codigo=jas.justificacion_art_sol)"
					+ " INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = janp.codigo "
					+ "INNER JOIN  "
					+ "	solicitudes sol on (sol.numero_solicitud=SJ.numero_solicitud) "
					+ "INNER JOIN cuentas c " 
					+ "ON c.id=sol.cuenta "
					+ "INNER JOIN vias_ingreso vi "
					+ "ON vi.codigo = c.via_ingreso "
					+ "LEFT JOIN egresos eg "
					+ "ON eg.cuenta = c.id "
					+ "LEFT JOIN articulos_info_medica aim "
					+ "ON (janp.articulo=aim.codigo_articulo) " 
					+ "WHERE " + "	sol.numero_solicitud="
					+ fjan.getCodigoSolicitud() + " " + "	and jas.subcuenta="
					+ fjan.getSubcuenta() + " " + " and janp.articulo="
					+ fjan.getMedicamentoNoPos();
		}
			
		}
		logger.info("consulta: " + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			if(consulta.contains("?")){
				try{

					ps.setInt(1,Integer.valueOf( fjan.getTipoFrecuencia()));

				}catch (Exception e) {
					ps.setInt(1,ConstantesBD.codigoNuncaValido);
				}
			}
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}

		// este el ke carga la mezcla y ...
		if (fjan.getEmisor().equals("solicitud")) {
			logger.info("hhhhhhhhHHHHHHHHHHHHHHHHHHhhhhhhhhhhh Estado Emisor: Solicitud");
			// mapa.put("dosificacion_0",
			// fjan.getDosis()+" "+fjan.getUnidosisl() + " " +
			// fjan.getFrecuencia() + " " + fjan.getTipoFrecuencia());

			if (UtilidadCadena.noEsVacio(fjan.getDosis() + ""))
				mapa.put("dosificacion_0", fjan.getDosis() + "");
			else
				mapa.put("dosificacion_0", "");

			if (UtilidadCadena.noEsVacio(fjan.getUnidosisl() + "")) {
				mapa.put("unidosisl_0", fjan.getUnidosisl() + "");
			} else
				mapa.put("unidosisl_0", "");

			if (UtilidadCadena.noEsVacio(fjan.getUnidosis() + "")) {
				mapa.put("unidosis_0", fjan.getUnidosis() + "");
			} else
				mapa.put("unidosis_0", "");

			if (UtilidadCadena.noEsVacio(fjan.getFrecuencia() + "")) {
				mapa.put("frecuencia_0", fjan.getFrecuencia() + "");
				logger.info("Encontro valor de frecuencia");
			} else
				mapa.put("frecuencia_0", "");

			if (UtilidadCadena.noEsVacio(fjan.getTipoFrecuencia() + ""))
				mapa.put("tipofrecuencia_0", fjan.getTipoFrecuencia());
			else
				mapa.put("tipofrecuencia_0", "");

			mapa.put("cantidad_0", fjan.getTotalUnidades());
			mapa.put("tiempotratamiento_0", fjan.getTiempoTratamiento());
			mapa.put("nombretipofrecuencia", mapa.get("nombretipofrecuencia_0"));
		}

		else {
			if (fjan.getEmisor().equals("ingresar")) {
				// logger.info("\n\n\n\n\n\n >>>>>"+fjan.getSubcuenta()+","+fjan.getCodigoSolicitud()+","+fjan.getMedicamentoNoPos()+","+fjan.getCuenta());

				mapa.put("dosificacion_0", "");
				mapa.put("unidosis_0", ConstantesBD.codigoNuncaValido + "");
				mapa.put("adjuntos_0", adjuntos);
				mapa.put(
						"cantidad_0",
						cargarCantidad(con, fjan.getSubcuenta(),
								fjan.getCodigoSolicitud(),
								fjan.getMedicamentoNoPos(), fjan.getCuenta())
								+ "");
				if(!mapa.containsKey("tiempotratamiento_0")){
					mapa.put("tiempotratamiento_0", "");
				}
			}
		}

		/*
		 * logger.info("medica no pos: " + fjan.getMedicamentoNoPos());
		 * logger.info("Dosis: " + fjan.getDosis()); logger.info("Unidosis : " +
		 * fjan.getUnidosis()); logger.info("Unidosis l: " +
		 * fjan.getUnidosisl()); logger.info("Frecuencia: " +
		 * fjan.getFrecuencia()); logger.info("Tipo Frecuencia: " +
		 * fjan.getTipoFrecuencia());
		 */

		logger.info("MAPA DE MEDICAMENTOS NO POS >>>>" + mapa);
		return mapa;
	}

	
	
	
	/**
	 * Actualiza la cantidad de la justificacion resp para una mezcla, este
	 * llamdo se hace desde el despacho de med, donde solamente puede haber un
	 * despacho final, es decir, en ese punto no puede haber distribucion, por
	 * tal motivo solo es requerido el numero solicitud articulo
	 * 
	 * @param con
	 * @param cantidad
	 * @param numeroSolicitud
	 * @param articulo
	 * @return
	 */
	public static void actualizarCantidadJustificacionRespMezcla(
			Connection con, int cantidad, int numeroSolicitud, int articulo) {
		String consulta = "UPDATE justificacion_art_resp SET cantidad=? WHERE justificacion_art_sol IN" +
				"(SELECT jas.codigo FROM justificacion_art_sol jas " +
				"INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo "+
				"WHERE SJ.numero_solicitud=? and jas.articulo=?) ";

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setInt(1, cantidad);
			ps.setInt(2, numeroSolicitud);
			ps.setInt(3, articulo);
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.info("ERROR actualizarCantidadJustificacionRespMezcla " + e);
		}
		// NO RETORNA NADA PORQUE PUEDE LLAMAR A UN ARTICULO QUE NO TENGA JUSTI.
	}

	/**
	 * 
	 * @param subcuenta
	 * @param codigoSolicitud
	 * @param medicamentoNoPos
	 * @param cuenta
	 */
	private static int cargarCantidad(Connection con, String subcuenta,
			String codigoSolicitud, String medicamentoNoPos, String cuenta) {

		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");

		/*
		 * logger.info("\n\n\n\n\n codigo_solicitud >>>>"+codigoSolicitud);
		 * logger.info("\n\n\n\n\n Subcuenta >>>>"+subcuenta);
		 * logger.info("\n\n\n\n\n Medicamento >>>>"+medicamentoNoPos);
		 * logger.info("\n\n\n\n\n cuenta >>>>"+cuenta);
		 */

		String completar = "";
		if (System.getProperty("TIPOBD").equals("ORACLE")) {
			completar = " from dual ";
		}

		String egreso = "select	getexisteegreso("
				+ cuenta
				+ ") as hayegreso, "
				+ // se verifica si hay egreso
				"	getviaingresocuenta("
				+ cuenta
				+ ") as viaingreso, "
				+ // via ingreso
				"	gettotaladminfarresponsable("
				+ codigoSolicitud
				+ ","
				+ medicamentoNoPos
				+ ","
				+ subcuenta
				+ ") as adminresponsable,"
				+ // cantidad administrada por responsable
				"	gettotaladminfarmacia("
				+ medicamentoNoPos
				+ ","
				+ codigoSolicitud
				+ ","
				+ ValoresPorDefecto.getValorFalseParaConsultas()
				+ ") as adminfarmacia,"
				+ // cantidad administrada por farmacia
				"	gettotalConsumoMaterialesCx(" + medicamentoNoPos + ","
				+ codigoSolicitud + ") as consumoqx, " + "	getdespacho("
				+ medicamentoNoPos + "," + codigoSolicitud
				+ ") as cantdespacho, " + "	gettiposolicitud("
				+ codigoSolicitud + ") as tiposol " + completar; // cantidad
																	// administrada
																	// por
																	// solicitud
																	// de
																	// cirugia

		logger.info("consulta info ingresos >>>>" + egreso);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(egreso, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));

			// logger.info("mapa de cantidad >>>>>"+mapa);

			if (mapa.get("hayegreso_0").toString()
					.equals("" + ConstantesBD.acronimoTrueCorto + ""))
				;
			{
				if (mapa.get("viaingreso_0")
						.toString()
						.equals("" + ConstantesBD.codigoViaIngresoUrgencias
								+ "")
						|| mapa.get("viaingreso_0")
								.toString()
								.equals(""
										+ ConstantesBD.codigoViaIngresoHospitalizacion
										+ "")) {
					if (mapa.get("tiposol_0")
							.toString()
							.equals(""
									+ ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
									+ "")) {
						// return
						// Utilidades.convertirAEntero(mapa.get("adminresponsable_0").toString());
						return Utilidades.convertirAEntero(mapa.get(
								"adminfarmacia_0").toString());
					} else if (mapa
							.get("tiposol_0")
							.toString()
							.equals(""
									+ ConstantesBD.codigoTipoSolicitudCirugia
									+ "")) {
						return Utilidades.convertirAEntero(mapa.get(
								"consumoqx_0").toString());
					} else if (mapa
							.get("tiposol_0")
							.toString()
							.equals(""
									+ ConstantesBD.codigoTipoSolicitudMedicamentos
									+ "")) {
						return Utilidades.convertirAEntero(mapa.get(
								"adminfarmacia_0").toString());
					} else {
						return ConstantesBD.codigoNuncaValido;
					}

				}else{
					Integer cantidad=0;
					if(mapa.get("cantdespacho_0")!=null &&!mapa.get("cantdespacho_0").toString().isEmpty()){
						cantidad=Integer.valueOf(String.valueOf(mapa.get("cantdespacho_0")));
					}
					return cantidad;
				}

			}

			// ********* ESTO QUE?? --> QUEDO COMENTARIADO 
//			if (mapa.get("hayegreso_0").toString()
//					.equals(ConstantesBD.acronimoFalseCorto))
//				;
//			{
//
//			}

		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}

		return 0;

	}

	/**
	 * 
	 * @param con
	 * @param paciente
	 * @return
	 */
	private static String cargarIngresos(Connection con, PersonaBasica paciente) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		logger.info("33333333333333333333333333333333");
		logger.info("333333333333333333333333333333333333333333333333333333333333333333");
		String ingresos = "select id as codigo from ingresos where estado='CER' and  codigo_paciente ="
				+ paciente.getCodigoPersona()
				+ " order by fecha_ingreso,hora_ingreso DESC "
				+ ValoresPorDefecto.getValorLimit1() + " 1";
		if ((System.getProperty("TIPOBD") + "").equals("ORACLE")) {
			ingresos = "select * from (select id as codigo from ingresos where estado='CER' and  codigo_paciente ="
					+ paciente.getCodigoPersona()
					+ " "
					+ " order by fecha_ingreso,hora_ingreso DESC) where rownum <=1 ";
		}else{
			if ((System.getProperty("TIPOBD") + "").equals("POSTGRESQL")) {
				ingresos = "select id as codigo from ingresos where estado='CER' and  codigo_paciente ="
						+ paciente.getCodigoPersona()
						+ " order by fecha_ingreso,hora_ingreso DESC "
						+ ValoresPorDefecto.getValorLimit1() + " 1";
			}
		}

		logger.info("consulta de ingresos >>>>" + ingresos);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(ingresos, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}
		String cadenaIngresos = crearCadena(mapa);
		cadenaIngresos += " " + paciente.getCodigoIngreso();

		return cadenaIngresos;
	}

	/**
	 * 
	 * @param con
	 * @param paciente
	 * @return
	 */
	private static String cargarIngresos(Connection con, String codigoPersona,
			String consecutivoIngreso,String ingresos) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");

		logger.info("consulta de ingresos >>>>" + ingresos);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(ingresos, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			mapa = UtilidadBD.cargarValueObject(rs);
			ps.close();
			rs.close();
		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}
		String cadenaIngresos = crearCadena(mapa);
		cadenaIngresos += " " + consecutivoIngreso;

		return cadenaIngresos;

	}

	/**
	 * 
	 * @param mapa
	 * @return
	 */
	private static String crearCadena(HashMap mapa) {
		String cadena = "";
		int x;
		for (x = 0; x < Utilidades.convertirAEntero(mapa.get("numRegistros")
				.toString()); x++) {
			cadena += mapa.get("codigo_" + x);
			cadena += ",";
		}
		return cadena;
	}

	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap cargarInfoMedicamentosPos(Connection con,
			HashMap mapa2, String mapa) {
		HashMap mapa1 = new HashMap();
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("datos conceatenados > " + mapa);
		String datos[] = mapa.split(ConstantesBD.separadorSplit);
		String consulta = "";
		if (datos[2].equals(ConstantesBD.acronimoNo)) {
			if (datos[3].equals(ConstantesBD.acronimoNo)) {
				consulta = cadenaDatosMedicamentoPos + " ds.articulo="
						+ datos[0] + " and ds.numero_solicitud=" + datos[1];
				logger.info("datos3 acro no: ");
				// logger.info("Sql: " + consulta);
			} else if (datos[3].equals(ConstantesBD.acronimoSi)) {
				// se carga cuando viene de un antecedente
				// "	getformafarmaceuticaarticulo(am.codigo_articulo) || '-' || getconcentracionarticulo(am.codigo_articulo) as ffconcentracion, "
				// +

				consulta = "select  "
						+ "	am.codigo_articulo as codarticu, "
						+ "	am.dosificacion as dosificacion, "
						+ "	am.cantidad as cantidad, "
						+ "	getformafarmaceuticaarticulo(am.codigo_articulo) || '-' || getconcentracionarticulo(am.codigo_articulo) as ffconcentracion, "
						+ "	am.dosis_diaria as dosisdiaria,"
						+ "	am.tiempo_tratamiento as tiempotratamiento,"
						+ "	' ' as respuestaclinica,"
						+ " 	am.unidosis as unidosis,"
						+ " 	am.unidosis as unidosisl,"
						+ "	am.frecuencia as frecuencia,"
						+ "	am.tipo_frecuencia as tipofrecuencia,"
						+ "	' ' as ffarma," + "	' ' as concentracion"
						+ " from " + "	ant_medicamentos am " + " inner join "
						+ "	articulo a on (a.codigo=am.codigo) " + " where "
						+ "	am.codigo=" + datos[1] + " "
						+ "	and am.codigo_articulo=" + datos[0];

				logger.info("datos3 acro si: ");
			}
		} else if (datos[2].equals(ConstantesBD.acronimoSi)) {
			// se consulta cuando se carga un antecedente
			consulta = "select  "
					+ " 	jap.dosificacion as dosificacion, "
					+ "	jap.cantidad as cantidad, "
					+ "	getformafarmaceuticaarticulo(jap.articulo) || '-' || getconcentracionarticulo(jap.articulo) as ffconcentracion, "
					+ "	jap.dosis_diaria as dosisdiaria, "
					+ "	jap.tiempo_tratamiento as tiempotratamiento, "
					+ "	jap.respuesta_clinica as respuestaclinica, "
					+ " 	coalesce(jap.dosis, '') as dosis, "
					+ " 	coalesce(jap.unidosis, '') as unidosis, "
					+ "	coalesce(jap.frecuencia, '') as frecuencia, "
					+ "	coalesce(jap.tipo_frecuencia, '') as tipofrecuencia, "
					+ "	coalesce(jap.forma_farmaceutica, '') as ffarma, "
					+ "	coalesce(jap.concentracion, '') as concentracion"
					+ " from "
					+ "	justificacion_art_pos jap "
					+ " inner join "
					+ "	justificacion_art_sol jas on (jap.justificacion_art_sol=jas.codigo) "
					+ " INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo "
					+ " where SJ.numero_solicitud=" + datos[1] + " "
					+ "	and jap.articulo=" + datos[0];

			logger.info("datos2 acro si: ");
		}
		logger.info("consulta datos medicamentos pos utilizados sqlbase>>>"
				+ consulta);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}

		logger.info("FFCONCENTRACION: "
				+ mapa1.get("ffconcentracion_" + 0).toString());
		logger.info("DOSIFICACION: "
				+ mapa1.get("dosificacion_" + 0).toString() + "long: "
				+ mapa1.get("dosificacion_" + 0).toString().length());

		if (mapa1.get("dosis_" + 0) != null)
			mapa2.put("dosis", mapa1.get("dosis_" + 0).toString());
		else
			mapa2.put("dosis", "");

		// se almacena el codigo de la unidosis seleccionada de acuerdo al
		// articulo y de la tabla unidosis_x_articulo
		if (mapa1.get("unidosis_" + 0) != null)
			mapa2.put("unidosis", mapa1.get("unidosis_" + 0).toString());
		else
			mapa2.put("unidosis", "");

		// se obtiene el nombre de la Unidosis (acronimo)
		if (mapa1.get("unidosisl_" + 0) != null) 
			mapa2.put("unidosisNom", mapa1.get("unidosisl_" + 0).toString());
		else
			mapa2.put("unidosisNom", "");

		if (mapa1.get("tipofrecuencia_" + 0) != null)
			mapa2.put("tipofrecuencia", mapa1.get("tipofrecuencia_" + 0)
					.toString());
		else
			mapa2.put("tipofrecuencia", "");

		mapa2.put("dosificacion", mapa1.get("dosificacion_" + 0).toString());
		mapa2.put("dosisdiaria", mapa1.get("dosisdiaria_" + 0).toString());
		mapa2.put("cantidad", mapa1.get("cantidad_" + 0).toString());
		mapa2.put("ffconcentracion", mapa1.get("ffconcentracion_" + 0)
				.toString());
		mapa2.put("tiempotratamiento", mapa1.get("tiempotratamiento_" + 0)
				.toString());
		mapa2.put("ffarma", mapa1.get("ffarma_" + 0).toString());
		mapa2.put("concentracion", mapa1.get("concentracion_" + 0).toString());
		mapa2.put("respuestaclinica", mapa1.get("respuestaclinica_" + 0)
				.toString());
		mapa2.put("frecuencia", mapa1.get("frecuencia_" + 0).toString());
		// mapa2.put("tipofrecuencia",
		// mapa1.get("tipofrecuencia_"+0).toString());
		// mapa2.put("dosis", mapa1.get("dosis_"+0).toString());
		// mapa2.put("unidosis", mapa1.get("unidosis_"+0).toString());
		return mapa2;
	}

	/**
	 * 
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap cargarMedicamentosPos(Connection con,
			FormatoJustArtNopos fjan, PersonaBasica paciente,String ingresos1) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		HashMap mapa1 = new HashMap();
		mapa1.put("numRegistros", "0");
		String ingresos = "";

		if (fjan.getEmisor().equals("solicitud")
				|| fjan.getEmisor().equals("ingresar")) {
			ingresos = cargarIngresos(con, paciente);
		} else if (fjan.getEmisor().equals("modificar")
				|| fjan.getEmisor().equals("consultar")) {
			ingresos = cargarIngresos(con,
					fjan.getFormularioMap().get("codigopersona") + "", fjan
							.getFormularioMap().get("ingreso") + "",ingresos1);
			String consultaparam = "select  " + "	jap.codigo as codigojus,"
					+ "	jap.articulo as codigo, "
					+ "	getdescripcionarticulo(jap.articulo) as nombre, "
					+ "	jas.numero_solicitud as numerosolicitud," + "	'"
					+ ConstantesBD.acronimoNo
					+ "' as esantecedente, "
					+ "   jap.respuesta_clinica as respuestaclinica, "
					+

					"jap.frecuencia as frecuencia,"
					+ "jap.tipo_frecuencia as tipo_frecuencia,"
					+ "jap.dosis as dosis,"
					+ "jas.forma_farmaceutica as for_farmaceutica,"
					+ "jas.concentracion as concentracion,"
					+ "jap.dosis_diaria as dosis_diaria,"
					+ "jap.cantidad as cantidad,"
					+ "jap.tiempo_tratamiento as ttratamiento, "
					+ "jap.dosificacion as dosificacion, "
					+ "jap.unidosis as unidosis "
					+

					" from "
					+ "	justificacion_art_pos jap "
					+ " inner join "
					+ "	justificacion_art_sol jas on (jap.justificacion_art_sol=jas.codigo) ";
					/*+ "where "
					+ "jas.articulo = " + fjan.getMedicamentoNoPos() + " AND ";*/
					
			//consultaparam += fjan.isProvieneOrdenAmbulatoria() == true ? "jas.orden_ambulatoria = "+fjan.getCodigoOrden() : "jas.numero_solicitud = "+fjan.getCodigoSolicitud();
			
			if(fjan.isProvieneOrdenAmbulatoria()){
				consultaparam+=" INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = jas.codigo  " 
						+ "WHERE jas.articulo=" + fjan.getMedicamentoNoPos() 
						+ " AND OAS.codigo_orden = "+fjan.getCodigoOrden();
			}else{
				consultaparam+=" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo " 
						+ "WHERE jas.articulo=" + fjan.getMedicamentoNoPos() 
						+ " AND SJ.numero_solicitud = "+fjan.getCodigoSolicitud();
			}
		
			try {
				PreparedStatementDecorator ps = new PreparedStatementDecorator(
						con.prepareStatement(consultaparam,
								ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));
				mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
						.executeQuery()));
			} catch (SQLException e) {
				logger.info("ERROR " + e);
			}
			// logger.info("mapa param >>>"+mapa);
		}

		if (!fjan.getEmisor().equals("consultar")) {
			String consulta = cadenaConsultaMedicamentosPos
					+ "  i.id IN (-1, "
					+ ingresos
					+ ") and sgi.grupo="
					+ ((HashMap) fjan.getMedicamentosNoPos())
							.get("codigogrupotera_" + 0)
					+ " and na.es_pos='"
					+ ValoresPorDefecto.getValorTrueCortoParaConsultas()
					+ "' and na.es_medicamento='"
					+ ConstantesBD.acronimoSi
					+ "' group by ssc.articulo	) "
					+ " UNION "
					+ "	(SELECT "
					+ "		am.codigo_articulo as codigo,"
					+ "		am.nombre as nombre,"
					+ "		am.codigo as numerosolicitud,"
					//+ "		'' as fecha,"
					+ "		'"
					+ ConstantesBD.acronimoSi
					+ "' as esantecedente "
					+ "	FROM "
					+ "		ant_medicamentos am "
					+ "	inner join articulo a on (a.codigo=am.codigo_articulo) "
					+ "	inner join subgrupo_inventario sgi on (a.subgrupo=sgi.codigo) "
					+ "	inner join naturaleza_articulo na on (a.naturaleza=na.acronimo)"
					+ "	WHERE "
					+ "		 codigo_paciente="
					+ paciente.getCodigoPersona()
					+ "  "
					+ " and sgi.grupo="
					+ ((HashMap) fjan.getMedicamentosNoPos())
							.get("codigogrupotera_" + 0) + " "
					+ " and na.es_pos='" + ValoresPorDefecto.getValorTrueCortoParaConsultas()
					+ "' " + " and na.es_medicamento='"
					+ ConstantesBD.acronimoSi + "')";
			logger.info("\n\n\n\n consulta de 1 medicamentos sqlbase>>>"
					+ consulta);
			try {
				PreparedStatementDecorator ps = new PreparedStatementDecorator(
						con.prepareStatement(consulta,
								ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));
				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
						.executeQuery()));
			} catch (SQLException e) {
				logger.info("ERROR " + e);
			}
		}
		if (fjan.getEmisor().equals("modificar")
				|| fjan.getEmisor().equals("consultar")) {
			mapa.put("codigoparam", mapa1.get("codigo_0"));
			mapa.put("nombreparam", mapa1.get("nombre_0"));
			mapa.put("codigojusparam", mapa1.get("codigojus_0"));
			mapa.put("numerosolicitudparam", mapa1.get("numerosolicitud_0"));
			mapa.put("respuestaclinica", mapa1.get("respuestaclinica_0"));
			mapa.put("esantecedente", mapa1.get("esantecedente_0"));

			mapa.put("dosificacion", mapa1.get("dosificacion_0"));
			mapa.put("frecuencia", mapa1.get("frecuencia_0"));
			mapa.put("ffarma", mapa1.get("for_farmaceutica_0"));
			mapa.put("concentracion", mapa1.get("concentracion_0"));
			mapa.put("dosisdiaria", mapa1.get("dosis_diaria_0"));
			mapa.put("cantidad", mapa1.get("cantidad_0"));
			mapa.put("tiempotratamiento", mapa1.get("ttratamiento_0"));
			mapa.put("dosis", mapa1.get("dosis_0"));
			mapa.put("tipofrecuencia", mapa1.get("tipo_frecuencia_0"));
			mapa.put("unidosis", mapa1.get("unidosis_0"));

			/*
			 * Se comenta la siguiente linea por la Tarea 49897ya que el valor
			 * de numRegistros se estaba resetiandoen uno siempre que se iba a
			 * consultar o modificar
			 */
			// mapa.put("numRegistros", 1);
		}
		// logger.info("mapa param y el resto >>>"+mapa);
		return mapa;
	}

	/**
	 * @param con
	 * @param fjan
	 * @param paciente
	 * @return
	 */
	public static HashMap cargarSustitutosNoPos(Connection con,
			FormatoJustArtNopos fjan, PersonaBasica paciente) {
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", 0);
		String consulta = "";
		
		PreparedStatementDecorator ps = null;
		PreparedStatementDecorator ps1 = null;
		// estaba el if comentariado
		if (fjan.getEmisor().equals("solicitud") || fjan.getEmisor().equals("ingresar")) {

			consulta = "SELECT "
					+ "	snp.articulo_sustituto as codigo, "
					+ "	' ' as resumenhistoria, "
					+ "	getdescripcionarticulo(snp.articulo_sustituto) as nombre, "
					+ "	snp.dosificacion as dosificacion, "
					+ "	snp.dosis_diaria as dosisdiaria, "
					+ "	snp.tiempo_tratamiento as tiempotratamiento, "
					+ "	snp.num_dosis_equivalentes as numdossisequiv, "
					+ "	getformafarmaceuticaarticulo(snp.articulo_sustituto) || '-' || getconcentracionarticulo(snp.articulo_sustituto) as ffconcentracion, "
					+ "	getgrupoterapeutico(a.subgrupo) as grupoterapeutico "
					+ " FROM " + "	sustitutos_nopos snp " + " INNER JOIN  "
					+ "	articulo a ON (snp.articulo_sustituto=a.codigo)	"
					+ " WHERE " + "	articulo_ppal="
					+ fjan.getMedicamentoNoPos();
		
		} else if (fjan.getEmisor().equals("modificar") || fjan.getEmisor().equals("consultar")) {
			
			consulta = " SELECT "
					+ "	jaf.codigo as codigojus,"
					+ "	jaf.medicamento_sustituto as codigo, "
					+ "	jaf.observacion as resumenhistoria, "
					+ "	getdescripcionarticulo(jaf.medicamento_sustituto) as nombre, "
					+ "	snp.dosificacion as dosificacion, "
					+ "	snp.dosis_diaria as dosisdiaria, "
					+ "	snp.tiempo_tratamiento as tiempotratamiento, "
					+ "	COALESCE(" +
					((System.getProperty("TIPOBD").equals("POSTGRESQL"))?"btrim(":"")+
					"to_char(jaf.num_dosis_eq" +
					((System.getProperty("TIPOBD").equals("POSTGRESQL"))?",'999'":"")+
					") " +
					((System.getProperty("TIPOBD").equals("POSTGRESQL"))?",' ')":"")+
					",snp.num_dosis_equivalentes) as numdossisequiv, "
					+ "	getformafarmaceuticaarticulo(snp.articulo_sustituto) || '-' || getconcentracionarticulo(snp.articulo_sustituto) as ffconcentracion,"
					+ "	getgrupoterapeutico(a.subgrupo) as grupoterapeutico "
					+ " FROM "
					+ "	justificacion_art_sol jas "
					+ " INNER JOIN "
					+ "	justificacion_art_fijo jaf ON (jas.codigo=jaf.justificacion_art_sol) "
					+ " INNER JOIN "
					+ "	sustitutos_nopos snp ON (jaf.medicamento_sustituto=snp.articulo_sustituto) "
					+ " INNER JOIN "
					+ "	articulo a ON (a.codigo=snp.articulo_sustituto)";
					//+ " WHERE " + "	jas.articulo="+fjan.getMedicamentoNoPos()+" ";

			//consulta += fjan.isProvieneOrdenAmbulatoria() ? "AND jas.orden_ambulatoria = "+fjan.getCodigoOrden() : " AND jas.numero_solicitud = "+fjan.getCodigoSolicitud() ;
			
			if(fjan.isProvieneOrdenAmbulatoria()){
				consulta+=" INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = jas.codigo  " 
						+ "WHERE jas.articulo=" + fjan.getMedicamentoNoPos() 
						+ " AND OAS.codigo_orden =  "+fjan.getCodigoOrden();
			}else{
				consulta+=" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo " 
						+ "WHERE jas.articulo=" + fjan.getMedicamentoNoPos() 
						+ " AND SJ.numero_solicitud =  "+fjan.getCodigoSolicitud();
			}
		}

		logger.info("\n\n\n Consulta de datos sustitutos medicamento no pos >>>>"+ consulta);

		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

			if (Utilidades.convertirAEntero(mapa.get("numRegistros").toString()) == 0) {
				// aca cambie la ciudad de origen+
				consulta = "SELECT "
						+ "	snp.articulo_sustituto as codigo, "
						+ "	' ' as resumenhistoria, "
						+ "	getdescripcionarticulo(snp.articulo_sustituto) as nombre, "
						+ "	snp.dosificacion as dosificacion, "
						+ "	snp.dosis_diaria as dosisdiaria, "
						+ "	snp.tiempo_tratamiento as tiempotratamiento, "
						+ "	snp.num_dosis_equivalentes as numdossisequiv, "
						+ "	getformafarmaceuticaarticulo(snp.articulo_sustituto) || '-' || getconcentracionarticulo(snp.articulo_sustituto) as ffconcentracion, "
						+ "	getgrupoterapeutico(a.subgrupo) as grupoterapeutico "
						+ " FROM " + "	sustitutos_nopos snp " + " INNER JOIN  "
						+ "	articulo a ON (snp.articulo_sustituto=a.codigo)	"
						+ " WHERE " + "	articulo_ppal="
						+ fjan.getMedicamentoNoPos();

				logger.info("consulta de datos en la tabla fijos >>>"+ consulta);

				ps1 = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery()));

			}

		} catch (SQLException e) {
            logger.error("ERROR SQLException cargarSustitutosNoPos: ", e);

        }catch(Exception ex){
			logger.error("ERROR Exception cargarSustitutosNoPos: ", ex);
		
        }finally{
			try{
				if(ps != null){
					ps.close();
				}	
				if(ps1 != null){
					ps1.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}

		return mapa;
	}

	/**
	 * Consultar la observacion de resumen que justifica el uso de medicamentos
	 * NO POS
	 * 
	 * @param con
	 * @param codigoSolicitudOrden
	 * @param codigoArticulo
	 * @param esOrdenAmbulatoria
	 * @return
	 * @throws Exception
	 */
	public static String consultarObservacionesResumenNoPOS(Connection con,
			String codigoSolicitudOrden, String codigoArticulo, boolean esOrdenAmbulatoria) throws Exception {
		
		PreparedStatement ps = null;
		ResultSet rs = null; 
		try {
			String observaciones = "";
			String consulta = " SELECT "
					+ "	jaf.observacion as resumenhistoria"
					+ " FROM "
					+ "	justificacion_art_sol jas "
					+ " INNER JOIN "
					+ "	justificacion_art_fijo jaf ON (jas.codigo=jaf.justificacion_art_sol) ";
							//+ "WHERE  jas.articulo=" + codigoArticulo + " AND ";
			
			//consulta += esOrdenAmbulatoria ? "jas.orden_ambulatoria = "+ codigoSolicitudOrden : "jas.numero_solicitud = "+codigoSolicitudOrden;

			if(esOrdenAmbulatoria){
				consulta+=" INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = jas.codigo  " 
						+ "WHERE jas.articulo=" + codigoArticulo 
						+ " AND OAS.codigo_orden = ? ";
			}else{
				consulta+=" INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo " 
						+ "WHERE jas.articulo=" + codigoArticulo 
						+ " AND SJ.numero_solicitud = ? ";
			}
			
			ps = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			ps.setInt(1, Utilidades.convertirAEntero(codigoSolicitudOrden));
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				observaciones = rs.getString("resumenhistoria");
			}
			return observaciones;
			
		} catch (SQLException e){
            logger.error("ERROR SQLException obtenerUnidadesAgendaValidasXUsuario: ", e);
			throw e;
     
        }catch(Exception ex){
			logger.error("ERROR Exception obtenerUnidadesAgendaValidasXUsuario: ", ex);
			throw ex;
		
        }finally{
			try{
				if(ps != null){
					ps.close();
		}
				if(rs != null){
					rs.close();
				}	
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
	}

	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% INSERCION DEL FORMULARIO
	// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	/**
	 * Actualiza la cantidad de justificacion
	 * 
	 * @param Connection
	 *            con
	 * @param HashMap
	 *            parametros
	 * */
	public static boolean actualizarCantidadJustificacion(Connection con,
			HashMap parametros) {
		String cadena = "UPDATE justificacion_art_sol SET dosis = ? WHERE codigo = ? ";

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(cadena, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("cantidad")
					.toString()));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigoPk")
					.toString()));

			if (ps.executeUpdate() > 0)
				return true;
		} catch (Exception e) {
			logger.info("ERROR " + e);
		}

		return false;
	}

	// ********************************************************************************

	/**
	 * 
	 * @param con
	 * @param numerosolicitud
	 * @param mapa
	 * @param observacionesGenerales
	 * @param estado
	 * @return
	 */
	// frecuencia estaba en int
	@SuppressWarnings("unused")
	public static int insertarJustificacion(Connection con,
			int numerosolicitud, int ordenAmbulatoria, HashMap mapa,
			HashMap MedicamentosNoPosMap, HashMap MedicamentosPosMap,
			HashMap SustitutosNoPosMap, HashMap DiagnosticosDefinitivos, int i,
			int institucion, String observacionesGenerales, String estado,
			int articulo, String observaciones, String dosis, String unidosis,
			int frecuencia, String tipoFrecuencia, String via,
			double cantidadSolicitada, String diasTratamiento, String usuario) {
		

		DaoFactory myFactory = DaoFactory.getDaoFactory(System
				.getProperty("TIPOBD"));

		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

		PreparedStatementDecorator ingresoSolicitud = null;
		PreparedStatementDecorator insertarSolicitudUOrdenXJust=null;
		PreparedStatementDecorator codigojussol = null;
		PreparedStatementDecorator ingresojussub2 = null;
		PreparedStatementDecorator ingresoSolicitud1 = null;
		PreparedStatementDecorator pst = null;
		PreparedStatementDecorator ingresoSolicitud4 = null;
		PreparedStatementDecorator ingresoSolicitud5 = null;
		PreparedStatementDecorator pst2 = null;
		PreparedStatementDecorator pst3 = null;
		PreparedStatementDecorator ingresojusparam5 = null;
		ResultSetDecorator rsCodigojussol = null;
		
		try {
			if (estado.equals(ConstantesBD.inicioTransaccion)) {
				myFactory.beginTransaction(con);
			}

			int modificar = ConstantesBD.codigoNuncaValido;
			if (numerosolicitud != ConstantesBD.codigoNuncaValido){
				//modificar = modificarSiExiste(con, i, numerosolicitud);
				if(consultarCodigoJustificacionSolicitud(con, numerosolicitud, articulo)!=null){
					modificar = 1;
				}
			}

			String cadena = "";
			String consecutivo = "";
			String sentenciaJustificacionSolicitudOrden="";
			// insercion JUSTIFIFACION articulo NOpos justificacion_art_sol
			logger.info("indicativo modificar >>>>" + modificar);
			logger.info("contains keys >>>>"
					+ MedicamentosNoPosMap.containsKey(i + "_codigojus_0")
					+ "<------>"
					+ MedicamentosNoPosMap.containsKey(i + "_consecutivojus_0"));

			Integer codigoJustificacion=null;
			
			if (modificar <= 0) {
				consecutivo = UtilidadBD
						.obtenerValorConsecutivoDisponible(
								ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos,
								institucion);
				
				codigoJustificacion=UtilidadBD.obtenerSiguienteValorSecuencia(con,
						"seq_justificacion_art_sol");
				
				cadena = "INSERT INTO justificacion_art_sol " + "("
						+ "	codigo,";
				//Se quita tras la solucion para la mt 4568
				/*if (numerosolicitud != ConstantesBD.codigoNuncaValido)
					cadena += "	numero_solicitud,";
				else
					cadena += " orden_ambulatoria, ";*/
				cadena += "	articulo," + " 	frecuencia," + "	tipo_frecuencia,";
				if (!dosis.equals(ConstantesBD.codigoNuncaValido + "")
						&& !dosis.equals(""))
					cadena += "	dosis,";
				cadena += "	unidosis," + " 	forma_farmaceutica,"
						+ " 	concentracion," + " 	tiempo_tratamiento,"
						+ " 	grupo_terapeutico,";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_tiemporespuestaesperado_0"))
					cadena += "	tiempo_respuesta, ";
				if (MedicamentosNoPosMap.containsKey(i + "_registroinvima_0"))
					cadena += "	registro_invima, ";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_efectodeseadotratamiento_0"))
					cadena += "	efecto_deseado, ";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_efectossecundarios_0"))
					cadena += "	efectos_secundarios,";
				if (MedicamentosNoPosMap.containsKey(i + "_bibliografia_0"))
					cadena += "	bibliografia, ";
				cadena += "	fecha_modifica, "
						+ "	usuario_modifica,"
						+ "	hora_modifica, "
						+ "	consecutivo,"
						+ "	dosificacion,"
						+ "	documentosadj,"
						+ "	fecha,"
						+ "	institucion,"
						+ "	tipo_jus )"
						+ " VALUES"
						+ "(	"
						+ " "
						+ codigoJustificacion + ",";
				//Se quita tras la solucion para la mt 4568
				/*if (numerosolicitud != ConstantesBD.codigoNuncaValido)
					cadena += " " + numerosolicitud + ",";
				else
					cadena += " " + ordenAmbulatoria + ",";*/
				cadena += " " + articulo + "," + " " + frecuencia + "," + " #COMILLA_SIMPLE#"
						+ tipoFrecuencia + "#COMILLA_SIMPLE#,";
				if (!dosis.equals(ConstantesBD.codigoNuncaValido + "")
						&& !dosis.equals(""))
					cadena += " " + dosis + ",";
				cadena += " #COMILLA_SIMPLE#" + unidosis + "#COMILLA_SIMPLE#," + " #COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_ffarma_0") + "#COMILLA_SIMPLE#,"
						+ " #COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_concentracion_0")
						+ "#COMILLA_SIMPLE#,";

				if (!UtilidadTexto.isEmpty(MedicamentosNoPosMap.get(
						i + "_tiempotratamiento_0").toString())
						&& !MedicamentosNoPosMap
								.get(i + "_tiempotratamiento_0").toString()
								.equals(ConstantesBD.codigoNuncaValido + "")
						&& !MedicamentosNoPosMap
								.get(i + "_tiempotratamiento_0").toString()
								.equals(""))
					cadena += " "
							+ MedicamentosNoPosMap.get(i
									+ "_tiempotratamiento_0") + ",";
				else
					cadena += "0,";

				if (MedicamentosNoPosMap.containsKey(i + "_codigogrupotera_0")
						&& !MedicamentosNoPosMap.get(i + "_codigogrupotera_0")
								.toString().equals(""))
					cadena += " "
							+ MedicamentosNoPosMap
									.get(i + "_codigogrupotera_0") + ",";
				else
					cadena += " " + ConstantesBD.codigoNuncaValido + ",";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_tiemporespuestaesperado_0"))
					cadena += " #COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i
									+ "_tiemporespuestaesperado_0") + "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i + "_registroinvima_0"))
					cadena += " #COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i + "_registroinvima_0")
							+ "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_efectodeseadotratamiento_0"))
					cadena += " #COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i
									+ "_efectodeseadotratamiento_0") + "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_efectossecundarios_0"))
					cadena += " #COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i
									+ "_efectossecundarios_0") + "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i + "_bibliografia_0"))
					cadena += " #COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i + "_bibliografia_0")
							+ "#COMILLA_SIMPLE#,";

				cadena += "   CURRENT_DATE," + " #COMILLA_SIMPLE#" + usuario + "#COMILLA_SIMPLE#," + "	"
						+ ValoresPorDefecto.getSentenciaHoraActualBD().replace("'", "#COMILLA_SIMPLE#") + ","
						+ " " + consecutivo + "," + " #COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_dosificacion_0")
						+ "#COMILLA_SIMPLE#, " + " #COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_adjuntos_0") + "#COMILLA_SIMPLE#, "
						+ "   CURRENT_DATE, " + " " + institucion + ", "
						+ " #COMILLA_SIMPLE#MEDI#COMILLA_SIMPLE# )" + "";
				
				sentenciaJustificacionSolicitudOrden="";
				
				StringBuffer insertarSolJust=null;
				if (numerosolicitud != ConstantesBD.codigoNuncaValido){
					insertarSolJust=new StringBuffer("INSERT INTO INVENTARIOS.SOL_X_JUST_ART_SOL (NUMERO_SOLICITUD,CODIGO_JUSTIFICACION) ");
					insertarSolJust.append("values (")
					.append(numerosolicitud)
					.append(", ")
					.append(codigoJustificacion)
					.append(") ");
				}else{
					insertarSolJust=new StringBuffer("INSERT INTO INVENTARIOS.ORD_AMB_JUST_ART_SOL (CODIGO_ORDEN,CODIGO_JUSTIFICACION) ");
					insertarSolJust.append("values (")
					.append(ordenAmbulatoria)
					.append(", ")
					.append(codigoJustificacion)
					.append(") ");
				}
				
				sentenciaJustificacionSolicitudOrden=insertarSolJust.toString();
				
			} else if (modificar > 0
					&& MedicamentosNoPosMap.containsKey(i + "_codigojus_0")
					&& MedicamentosNoPosMap
							.containsKey(i + "_consecutivojus_0")) {

				cadena = "UPDATE justificacion_art_sol SET " + ""
						//+ "	numero_solicitud=" + numerosolicitud + ","
						+ "	articulo=" + articulo + ", ";
				cadena += frecuencia > 0 ? " 	frecuencia=" + frecuencia + ","
						: " ";
				cadena += !UtilidadTexto.isEmpty(tipoFrecuencia) ? "	tipo_frecuencia=#COMILLA_SIMPLE#"
						+ tipoFrecuencia + "#COMILLA_SIMPLE#,"
						: " ";
				cadena += (!UtilidadTexto.isEmpty(dosis) && Utilidades
						.convertirAEntero(dosis) > 0) ? "	dosis=" + dosis + ","
						: " ";
				cadena += "	unidosis=#COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_unidosis_0") + "#COMILLA_SIMPLE#, "
						+ " 	forma_farmaceutica=#COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_ffarma_0") + "#COMILLA_SIMPLE#,"
						+ " 	concentracion=#COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_concentracion_0")
						+ "#COMILLA_SIMPLE#," + " 	grupo_terapeutico="
						+ MedicamentosNoPosMap.get(i + "_codigogrupotera_0")
						+ ",";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_tiemporespuestaesperado_0"))
					cadena += "	tiempo_respuesta=#COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i
									+ "_tiemporespuestaesperado_0") + "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i + "_registroinvima_0"))
					cadena += "	registro_invima= #COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i + "_registroinvima_0")
							+ "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_efectodeseadotratamiento_0"))
					cadena += "	efecto_deseado=#COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i
									+ "_efectodeseadotratamiento_0") + "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i
						+ "_efectossecundarios_0"))
					cadena += "	efectos_secundarios=#COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i
									+ "_efectossecundarios_0") + "#COMILLA_SIMPLE#,";
				if (MedicamentosNoPosMap.containsKey(i + "_bibliografia_0"))
					cadena += "	bibliografia=#COMILLA_SIMPLE#"
							+ MedicamentosNoPosMap.get(i + "_bibliografia_0")
							+ "#COMILLA_SIMPLE#,";

				if (!UtilidadTexto.isEmpty(diasTratamiento))
					cadena += "	tiempo_tratamiento="
							+ Utilidades.convertirAEntero(diasTratamiento + "")
							+ ",";
				else {
					cadena += "tiempo_tratamiento=987, ";
					logger.info("<<tiempo de tratamiento paila.");
				}

				cadena += "	fecha_modifica= CURRENT_DATE,"
						+ "	usuario_modifica=#COMILLA_SIMPLE#" + usuario + "#COMILLA_SIMPLE#,"
						+ "	hora_modifica="
						+ ValoresPorDefecto.getSentenciaHoraActualBD().replace("'", "#COMILLA_SIMPLE#") + ","
						+ "	dosificacion=#COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_dosificacion_0")
						+ "#COMILLA_SIMPLE#," + "	documentosadj=#COMILLA_SIMPLE#"
						+ MedicamentosNoPosMap.get(i + "_adjuntos_0") + "#COMILLA_SIMPLE# "
						+ " WHERE ";
						/*+ " " + " WHERE " + "	numero_solicitud="
						+ numerosolicitud + " and " + "	articulo=" + articulo
						+ "  ";*/
				
				codigoJustificacion=consultarCodigoJustificacionSolicitud(con, numerosolicitud, articulo);
				if(codigoJustificacion!=null){
					cadena+=" codigo = "+codigoJustificacion;
				}
				
			}

			logger.info("sentencia insercion a justificacion_art_sol :--> "	+ cadena);
			cadena=cadena.replace("'", " ");
			cadena=cadena.replace("#COMILLA_SIMPLE#", "'");
			ingresoSolicitud = new PreparedStatementDecorator(
					con.prepareStatement(cadena, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			int resultado = ingresoSolicitud.executeUpdate();
			
			if(resultado > 0 && modificar <= 0){
				
				insertarSolicitudUOrdenXJust=new PreparedStatementDecorator(
						con.prepareStatement(sentenciaJustificacionSolicitudOrden, ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));
				
				resultado = insertarSolicitudUOrdenXJust.executeUpdate();
			}

			logger.info("Resultado : " + resultado);
			logger.info("Consecutivo : " + consecutivo);

			if (resultado > 0 && !consecutivo.equals(""))
				UtilidadBD
						.cambiarUsoFinalizadoConsecutivo(
								con,
								ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos,
								institucion, consecutivo,
								ConstantesBD.acronimoSi,
								ConstantesBD.acronimoSi);
			else if (!consecutivo.equals(""))
				UtilidadBD
						.cambiarUsoFinalizadoConsecutivo(
								con,
								ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos,
								institucion, consecutivo,
								ConstantesBD.acronimoNo,
								ConstantesBD.acronimoNo);

			// consultamos el codigo de justificacion_art_sol para la adicion en
			// las demas tablas
			/*String cadenaCodigo = "SELECT MAX(codigo) as codigo FROM justificacion_art_sol WHERE ";
			if (numerosolicitud != ConstantesBD.codigoNuncaValido){
				cadenaCodigo += "numero_solicitud=" + numerosolicitud;
			}
			else{
				cadenaCodigo += "orden_ambulatoria=" + ordenAmbulatoria;
			}

			
			cadenaCodigo=cadenaCodigo.replace("'", " ");
			cadenaCodigo=cadenaCodigo.replace("#COMILLA_SIMPLE#", "'");
			codigojussol = new PreparedStatementDecorator(
					con.prepareStatement(cadenaCodigo,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			rsCodigojussol = new ResultSetDecorator(codigojussol.executeQuery());
			HashMap mapau = UtilidadBD.cargarValueObject(rsCodigojussol);

			logger.info("sentencia para codigo art__sol >>>>>" + cadenaCodigo);
			
			if (modificar > 0
					&& MedicamentosNoPosMap.containsKey(i + "_codigojus_0")
					&& MedicamentosNoPosMap
							.containsKey(i + "_consecutivojus_0")) {
				mapau.put("codigo_0", MedicamentosNoPosMap.get(i + "_codigojus_0"));
			}*/

			if (resultado > 0) {

				if (modificar > 0
						&& MedicamentosNoPosMap.containsKey(i + "_codigojus_0")
						&& MedicamentosNoPosMap.containsKey(i + "_consecutivojus_0")) {

					cadena = "UPDATE justificacion_art_resp SET" + " "
							+ "estado=#COMILLA_SIMPLE#" + mapa.get(i + "_estadojus") + "#COMILLA_SIMPLE#,"
							+ "cantidad= "
							+ MedicamentosNoPosMap.get(i + "_cantidad_0") + ""
							+ " " + " WHERE " + " justificacion_art_sol="
							+ MedicamentosNoPosMap.get(i + "_codigojus_0")
							+ " and " + " subcuenta="
							+ mapa.get(i + "_subcuenta") + "" + "";

				} else {
					cadena = "INSERT INTO justificacion_art_resp "
							+ "( "
							+ "codigo,"
							+ "estado,"
							+ "justificacion_art_sol, "
							+ "subcuenta, "
							+ "cantidad "
							+ ") "
							+ " VALUES"
							+ "(	"
							+ ""
							+ UtilidadBD.obtenerSiguienteValorSecuencia(con,
									"seq_justificacion_art_resp") + "," + " #COMILLA_SIMPLE#"
							+ ConstantesIntegridadDominio.acronimoJustificado
							+ "#COMILLA_SIMPLE#," + " " + codigoJustificacion + "," + " "
							+ mapa.get(i + "_subcuenta") + "," + " "
							+ MedicamentosNoPosMap.get(i + "_cantidad_0") + ""
							+ " ) " + "";
				}

				logger.info("sentencia insercion articulos sustitutos no pos y observaCIONES >>>>>> \n " + cadena);
				
				
				cadena=cadena.replace("'", " ");
				cadena=cadena.replace("#COMILLA_SIMPLE#", "'");
				
				
				ingresojussub2 = new PreparedStatementDecorator(
						con.prepareStatement(cadena,
								ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));
				resultado = ingresojussub2.executeUpdate();
			}

			if (resultado > 0) {

				// insercion articulo pos

				String[] articulopos = MedicamentosPosMap.get(i + "_articulo")
						.toString().split(ConstantesBD.separadorSplit);

				if (articulopos.length >= 1
						&& !UtilidadTexto.isEmpty(articulopos[0].toString())) {

					logger.info("datos del mapa de medicamentos \n " + MedicamentosPosMap);

					if (MedicamentosPosMap.containsKey(i + "_frecuencia")
							&& (MedicamentosPosMap.get(i + "_frecuencia")
									.toString().equals(" ") || MedicamentosPosMap
									.get(i + "_frecuencia").toString()
									.equals(""))) {
						MedicamentosPosMap.put(i + "_frecuencia", 0);
					}
					if (MedicamentosPosMap.containsKey(i + "_dosisdiaria")
							&& MedicamentosPosMap.get(i + "_dosisdiaria")
									.toString().equals(" ")) {
						MedicamentosPosMap.put(i + "_dosisdiaria", 0);
					}

					if (MedicamentosPosMap
							.containsKey(i + "_tiempotratamiento")
							&& MedicamentosPosMap.get(i + "_tiempotratamiento")
									.toString().equals("")) {
						MedicamentosPosMap.put(i + "_tiempotratamiento", 0);
						logger.info("Se Altero el Tiempo de Tratamiento por Cero!");
					}
					if (MedicamentosPosMap.containsKey(i + "_cantidad")
							&& MedicamentosPosMap.get(i + "_cantidad")
									.toString().equals("")) {
						MedicamentosPosMap.put(i + "_cantidad", 0);
					}

					if (!articulopos[0].trim().equals(
							ConstantesBD.codigoNuncaValido + "")) {
						if (modificar > 0
								&& MedicamentosNoPosMap.containsKey(i
										+ "_codigojus_0")
								&& MedicamentosNoPosMap.containsKey(i
										+ "_consecutivojus_0")
								&& Utilidades
										.convertirAEntero(MedicamentosPosMap
												.get(i + "_codigojusparam")
												+ "") > 0) {
							// MedicamentosPosMap.get(i+"_frecuencia") + "#COMILLA_SIMPLE# "+

							cadena = "UPDATE justificacion_art_pos SET "
									+ "articulo="
									+ articulopos[0]
									+ ","
									+ "frecuencia=#COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_frecuencia")
									+ "#COMILLA_SIMPLE#,"
									+ "tipo_frecuencia=#COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_tipofrecuencia")
									+ "#COMILLA_SIMPLE#,"
									+ "dosis=#COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_dosis")
									+ "#COMILLA_SIMPLE#,"
									+ "forma_farmaceutica=#COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_ffarma")
									+ "#COMILLA_SIMPLE#,"
									+ "concentracion=#COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_concentracion")
									+ "#COMILLA_SIMPLE#,"
									+ "dosis_diaria= #COMILLA_SIMPLE#"
									+ MedicamentosPosMap
											.get(i + "_dosisdiaria")
									+ "#COMILLA_SIMPLE#,"
									+ "cantidad=#COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_cantidad")
									+ "#COMILLA_SIMPLE#,"
									+ "tiempo_tratamiento=#COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_tiempotratamiento")
									+ "#COMILLA_SIMPLE#, "
									+ "dosificacion= #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_dosificacion")
									+ "#COMILLA_SIMPLE#, "
									+ "unidosis= #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_unidosis")
									+ "#COMILLA_SIMPLE# "
									+ "  WHERE  "
									+ " codigo="
									+ MedicamentosPosMap.get(i
											+ "_codigojusparam") + " " + " ";
						} else {

							cadena = "INSERT INTO justificacion_art_pos " + "("
									+ "codigo, " + "justificacion_art_sol, "
									+ "articulo, " + "frecuencia, "
									+ "tipo_frecuencia, " + "dosis, "
									+ "forma_farmaceutica, "
									+ "concentracion, " + "dosis_diaria, "
									+ "cantidad, " + "tiempo_tratamiento, "
									+ "respuesta_clinica," + "dosificacion, "
									+ "unidosis " + ") " + " VALUES" + "("
									+ " "
									+ UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_justificacion_art_pos")
									+ ","
									+ " "
									+ codigoJustificacion
									+ ","
									+ " "
									+ articulopos[0]
									+ ","
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_frecuencia")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_tipofrecuencia")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_dosis")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_ffarma")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_concentracion")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap
											.get(i + "_dosisdiaria")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_cantidad")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_tiempotratamiento")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ mapa.get(i
											+ "_respuestaClinicaParaclinica")
									+ "#COMILLA_SIMPLE#,"
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i
											+ "_dosificacion")
									+ "#COMILLA_SIMPLE#, "
									+ " #COMILLA_SIMPLE#"
									+ MedicamentosPosMap.get(i + "_unidosis")
									+ "#COMILLA_SIMPLE#" + " ) " + "";
						}

						logger.info("sentencia insercion articulos pos >>>>>> \n " + cadena);
						
						cadena=cadena.replace("'", " ");
						cadena=cadena.replace("#COMILLA_SIMPLE#", "'");
						
						ingresoSolicitud1 = new PreparedStatementDecorator(
								con.prepareStatement(cadena,
										ConstantesBD.typeResultSet,
										ConstantesBD.concurrencyResultSet));
						resultado = ingresoSolicitud1.executeUpdate();

						if (Utilidades.convertirAEntero(MedicamentosPosMap.get(
								i + "_numRegistros").toString()) == 0
								&& resultado > 0) {
							// como es nuevo antecedente se isnerta en la tabla
							// ant_medicamentos si ya existiera lo modificaria
							logger.info("Entra a insertar el antecedente");
							int ant = insertarAntecedente(con, mapa,
									MedicamentosPosMap, articulopos, i);
						}

						// preguntar si es antecedente y hace parte del select
						// hacer update a la tabla.
						// logger.info("resultado"+resultado);
						// logger.info("\n\n\n\n\n\nmapa del formato >>>>>>"+mapa);
					}

				}
			}
			// insercion articulo sustitutos no pos
			if (resultado > 0) {
				// Utilidades.imprimirMapa(SustitutosNoPosMap);

				if (Utilidades.convertirAEntero(SustitutosNoPosMap.get(i
						+ "_numRegistros")
						+ "") == 0) {
					SustitutosNoPosMap.put(
							i
									+ "_codigo_"
									+ SustitutosNoPosMap.get(i
											+ "_indexSelectSustitutos"), null);
				}

				if (modificar > 0
						&& MedicamentosNoPosMap.containsKey(i + "_codigojus_0")
						&& MedicamentosNoPosMap.containsKey(i
								+ "_consecutivojus_0")) {
					String sentenciaEliminadx = "delete from justificacion_art_fijo where justificacion_art_sol="
							+ codigoJustificacion + "";
					
					sentenciaEliminadx=sentenciaEliminadx.replace("'", " ");
					sentenciaEliminadx=sentenciaEliminadx.replace("#COMILLA_SIMPLE#", "'");
					
					
					pst = new PreparedStatementDecorator(
							con.prepareStatement(sentenciaEliminadx,
									ConstantesBD.typeResultSet,
									ConstantesBD.concurrencyResultSet));
					pst.executeUpdate();
					logger.info("sentencia elimina dx >>>>>"
							+ sentenciaEliminadx);
				}

				cadena = "INSERT INTO justificacion_art_fijo " + "( "
						+ "codigo," + "justificacion_art_sol, "
						+ "observacion, ";
				if (SustitutosNoPosMap.containsKey(i + "_codigo_"
						+ SustitutosNoPosMap.get(i + "_indexSelectSustitutos"))
						&& !(SustitutosNoPosMap.get(i
								+ "_codigo_"
								+ SustitutosNoPosMap.get(i
										+ "_indexSelectSustitutos")) + "")
								.equals(""))
					cadena += "medicamento_sustituto,";
				cadena += "profesional_responsable,NUM_DOSIS_EQ "
						+ ") "
						+ " VALUES"
						+ "(	"
						+ " "
						+ UtilidadBD.obtenerSiguienteValorSecuencia(con,
								"seq_justificacion_art_fijo")
						+ ","
						+ " "
						+ codigoJustificacion
						+ ","
						+ " #COMILLA_SIMPLE#"
						+ UtilidadTexto.deshacerCodificacionHTML(mapa.get(i
								+ "_observacionesResumen")
								+ "") + "#COMILLA_SIMPLE#,";
				if (SustitutosNoPosMap.containsKey(i + "_codigo_"
						+ SustitutosNoPosMap.get(i + "_indexSelectSustitutos"))
						&& !(SustitutosNoPosMap.get(i
								+ "_codigo_"
								+ SustitutosNoPosMap.get(i
										+ "_indexSelectSustitutos")) + "")
								.equals(""))
					cadena += " "
							+ SustitutosNoPosMap.get(i
									+ "_codigo_"
									+ SustitutosNoPosMap.get(i
											+ "_indexSelectSustitutos")) + ",";
				cadena += " #COMILLA_SIMPLE#"
						+ mapa.get(i + "_codigomed")
						+ "#COMILLA_SIMPLE#, "
						+ (SustitutosNoPosMap.get(i + "_numdossisequiv_0") != null
								&& !SustitutosNoPosMap
										.get(i + "_numdossisequiv_0")
										.toString().trim().equals("") ? SustitutosNoPosMap
								.get(i + "_numdossisequiv_0").toString().trim() : null)
						+ " ) " + "";

				logger.info("sentencia insercion articulos sustitutos no pos y observaCIONES >>>>>> \n "
						+ cadena);
				
				cadena=cadena.replace("'", " ");
				cadena=cadena.replace("#COMILLA_SIMPLE#", "'");
				
				
				PreparedStatementDecorator ingresoSolicitud2 = new PreparedStatementDecorator(
						con.prepareStatement(cadena,
								ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));
				resultado = ingresoSolicitud2.executeUpdate();

			}
			// insercion diagnosticos
			if (resultado > 0) {
				// principal
				String[] dx = { "null", "null" };
				if (DiagnosticosDefinitivos.containsKey(i + "_principal")) {
					dx = DiagnosticosDefinitivos.get(i + "_principal")
							.toString().split(ConstantesBD.separadorSplit);

					if (modificar > 0
							&& MedicamentosNoPosMap.containsKey(i
									+ "_codigojus_0")
							&& MedicamentosNoPosMap.containsKey(i
									+ "_consecutivojus_0")) {
						String sentenciaEliminadx = "delete from justificacion_art_dx where justificacion_art_sol="
								+ codigoJustificacion + "";
						
						
						
						sentenciaEliminadx=sentenciaEliminadx.replace("'", " ");
						sentenciaEliminadx=sentenciaEliminadx.replace("#COMILLA_SIMPLE#", "'");
						pst3 = new PreparedStatementDecorator(
								con.prepareStatement(sentenciaEliminadx,
										ConstantesBD.typeResultSet,
										ConstantesBD.concurrencyResultSet));
						pst3.executeUpdate();
						logger.info("sentencia elimina dx >>>>>"
								+ sentenciaEliminadx);
					}

					if (dx.length >= 2) {
						cadena = "INSERT INTO justificacion_art_dx "
								+ "( "
								+ "codigo, "
								+ "justificacion_art_sol, "
								+ "acronimo_dx, "
								+ "tipo_cie, "
								+ "tipo_dx"
								+ ") "
								+ " VALUES"
								+ "(	"
								+ " "
								+ UtilidadBD.obtenerSiguienteValorSecuencia(
										con, "seq_justificacion_art_dx") + ","
								+ " " + codigoJustificacion + "," + " #COMILLA_SIMPLE#"
								+ dx[0] + "#COMILLA_SIMPLE#," + " " + dx[1] + "," + "#COMILLA_SIMPLE#PRIN#COMILLA_SIMPLE#"
								+ " ) " + "";

						logger.info("sentencia insercion dx principal pos >>>>>> \n "
								+ cadena);
						
						
						cadena=cadena.replace("'", " ");
						cadena=cadena.replace("#COMILLA_SIMPLE#", "'");
						PreparedStatementDecorator ingresoSolicitud3 = new PreparedStatementDecorator(
								con.prepareStatement(cadena,
										ConstantesBD.typeResultSet,
										ConstantesBD.concurrencyResultSet));
						resultado = ingresoSolicitud3.executeUpdate();
					}
				}
				if (resultado > 0) {
					// complicacion
					if (DiagnosticosDefinitivos
							.containsKey(i + "_complicacion")) {
						dx = DiagnosticosDefinitivos.get(i + "_complicacion")
								.toString().split(ConstantesBD.separadorSplit);
						if (dx.length >= 2) {
							cadena = "INSERT INTO justificacion_art_dx "
									+ "( "
									+ "codigo,"
									+ "justificacion_art_sol, "
									+ "acronimo_dx, "
									+ "tipo_cie, "
									+ "tipo_dx"
									+ ") "
									+ " VALUES"
									+ "(	"
									+ " "
									+ UtilidadBD
											.obtenerSiguienteValorSecuencia(
													con,
													"seq_justificacion_art_dx")
									+ "," + " " + codigoJustificacion + ","
									+ " #COMILLA_SIMPLE#" + dx[0] + "#COMILLA_SIMPLE#," + " " + dx[1] + ","
									+ "#COMILLA_SIMPLE#COMP#COMILLA_SIMPLE#" + " ) " + "";

							logger.info("sentencia insercion dx complicacion pos >>>>>> \n "
									+ cadena);
							
							cadena=cadena.replace("'", " ");
							cadena=cadena.replace("#COMILLA_SIMPLE#", "'");
							
							ingresoSolicitud4 = new PreparedStatementDecorator(
									con.prepareStatement(cadena,
											ConstantesBD.typeResultSet,
											ConstantesBD.concurrencyResultSet));
							resultado = ingresoSolicitud4.executeUpdate();
						}
					}
				}
				if (resultado > 0) {

					for (int x = 0; x < Utilidades
							.convertirAEntero(DiagnosticosDefinitivos.get(
									i + "_numdiagnosticos").toString()); x++) {
						// relacionados
						if (DiagnosticosDefinitivos.containsKey(i
								+ "_relacionado_" + x)) {
							logger.info("entro a dx relacionados "
									+ DiagnosticosDefinitivos.get(
											i + "_checkbox_" + x).toString());
							if (DiagnosticosDefinitivos
									.get(i + "_checkbox_" + x).toString()
									.equals("true")) {
								dx = DiagnosticosDefinitivos
										.get(i + "_relacionado_" + x)
										.toString()
										.split(ConstantesBD.separadorSplit);
								if (dx.length >= 2) {
									cadena = "INSERT INTO justificacion_art_dx "
											+ "( "
											+ "codigo,"
											+ "justificacion_art_sol, "
											+ "acronimo_dx, "
											+ "tipo_cie, "
											+ "tipo_dx"
											+ ") "
											+ " VALUES"
											+ "(	"
											+ " "
											+ UtilidadBD
													.obtenerSiguienteValorSecuencia(
															con,
															"seq_justificacion_art_dx")
											+ ","
											+ " "
											+ codigoJustificacion
											+ ","
											+ " #COMILLA_SIMPLE#"
											+ dx[0]
											+ "#COMILLA_SIMPLE#,"
											+ " "
											+ dx[1]
											+ ","
											+ "#COMILLA_SIMPLE#RELA#COMILLA_SIMPLE#"
											+ " ) "
											+ "";

									logger.info("sentencia insercion dx relacionados >>>>>> \n "
											+ cadena);
									
									
									cadena=cadena.replace("'", " ");
									cadena=cadena.replace("#COMILLA_SIMPLE#", "'");
									ingresoSolicitud5 = new PreparedStatementDecorator(
											con.prepareStatement(
													cadena,
													ConstantesBD.typeResultSet,
													ConstantesBD.concurrencyResultSet));
									resultado = ingresoSolicitud5
											.executeUpdate();
								}
							}
						}
					}
				}

			}

			if (resultado > 0) {
				if (modificar > 0
						&& MedicamentosNoPosMap.containsKey(i + "_codigojus_0")
						&& MedicamentosNoPosMap.containsKey(i
								+ "_consecutivojus_0")) {
					String sentenciaEliminaparam = "delete from justificacion_art_param where justificacion_art_sol="
							+ codigoJustificacion + "";
					
					
					sentenciaEliminaparam=sentenciaEliminaparam.replace("'", " ");
					sentenciaEliminaparam=sentenciaEliminaparam.replace("#COMILLA_SIMPLE#", "'");
					
					
					pst2 = new PreparedStatementDecorator(
							con.prepareStatement(sentenciaEliminaparam,
									ConstantesBD.typeResultSet,
									ConstantesBD.concurrencyResultSet));
					pst2.executeUpdate();
					logger.info("sentencia elimina datos parametrizables  >>>>>"
							+ sentenciaEliminaparam);
				}

				// insercion de check#COMILLA_SIMPLE#s parametrizables

				// /logger.info("numRegistros J: "+Utilidades.convertirAEntero(mapa.get(i+"_numSecciones")+""));
				for (int j = 0; j < Utilidades.convertirAEntero(mapa.get(i
						+ "_numSecciones")
						+ ""); j++) {
					int codigoSeccion = Integer.parseInt(((HashMap) mapa.get(i
							+ "_mapasecciones")).get("codigo_" + j).toString());
					// logger.info("numRegistros T: "+Utilidades.convertirAEntero((mapa.get(i+"_numRegistros_"+codigoSeccion))+""));
					for (int t = 0; t < Utilidades.convertirAEntero((mapa.get(i
							+ "_numRegistros_" + codigoSeccion))
							+ ""); t++) {

						// logger.info("valor campo mapa #COMILLA_SIMPLE#"+i+"_numRegistros_"+codigoSeccion+"_"+mapa.get(i+"_campo_"+codigoSeccion+"_"+t)+"#COMILLA_SIMPLE# : "+mapa.containsKey(i+"_numRegistros_"+codigoSeccion+"_"+mapa.get(i+"_campo_"+codigoSeccion+"_"+t)));

						/*if (mapa.containsKey(i
								+ "_numRegistros_"
								+ codigoSeccion
								+ "_"
								+ mapa.get(i + "_campo_" + codigoSeccion + "_"
										+ t))) {*/
							/*
							 * switch(codigoSeccion) { case
							 * ConstantesBD.JusSeccionMedicamentosPos:
							 */
							if (mapa.containsKey(i + "_tipo_" + codigoSeccion
									+ "_" + t)
									&& mapa.get(i + "_tipo_" + codigoSeccion
											+ "_" + t) != null
									&& !mapa.get(
											i + "_tipo_" + codigoSeccion + "_"
													+ t).toString()
											.equals("LABE")
											&&UtilidadTexto.getBoolean(mapa.get(i+"_mostrar_"+codigoSeccion+"_"+t))) {
								if (mapa.get(
										i + "_tipo_" + codigoSeccion + "_" + t)
										.toString().equals("CHEC")&&mapa.containsKey(i
												+ "_numRegistros_"
												+ codigoSeccion
												+ "_"
												+ mapa.get(i + "_campo_" + codigoSeccion + "_"
														+ t))) {
									for (int x = 0; x < Utilidades
											.convertirAEntero(mapa.get(i
													+ "_numRegistros_"
													+ codigoSeccion
													+ "_"
													+ mapa.get(i + "_campo_"
															+ codigoSeccion
															+ "_" + t))
													+ ""); x++) {

										if (mapa.get(
												i
														+ "_campopadre_"
														+ codigoSeccion
														+ "_"
														+ mapa.get(
																i
																		+ "_campo_"
																		+ codigoSeccion
																		+ "_"
																		+ t)
																.toString()
														+ "_" + x)
												.toString()
												.equals(mapa.get(
														i + "_campo_"
																+ codigoSeccion
																+ "_" + t)
														.toString())) {
											cadena = "INSERT INTO justificacion_art_param "
													+ "("
													+ "codigo, "
													+ "justificacion_art_sol, "
													+ "valor, "
													+ "seccion, "
													+ "etiqueta_seccion,"
													+ "campo,"
													+ "etiqueta_campo,"
													+ "parametrizacion_jus,"
													+ "institucion"
													+ ") "
													+ " VALUES" + "(	" + " "
													+ UtilidadBD
															.obtenerSiguienteValorSecuencia(
																	con,
																	"seq_justificacion_art_param")
													+ ","
													+ " "
													+ codigoJustificacion
													+ ","
													+ " #COMILLA_SIMPLE#"
													+ mapa.get(i
															+ "_valorcampo_"
															+ codigoSeccion
															+ "_"
															+ mapa.get(
																	i
																			+ "_campo_"
																			+ codigoSeccion
																			+ "_"
																			+ t)
																	.toString()
															+ "_" + x)
													+ "#COMILLA_SIMPLE#,"
													+ " "
													+ codigoSeccion
													+ ","
													+ " #COMILLA_SIMPLE#"
													+ mapa.get(i
															+ "_etiquetaseccion_"
															+ codigoSeccion
															+ "_" + t)
													+ "#COMILLA_SIMPLE#,"
													+ " "
													+ mapa.get(i
															+ "_codigo_"
															+ codigoSeccion
															+ "_"
															+ mapa.get(
																	i
																			+ "_campo_"
																			+ codigoSeccion
																			+ "_"
																			+ t)
																	.toString()
															+ "_" + x)
													+ ","
													+ " #COMILLA_SIMPLE#"
													+ mapa.get(i
															+ "_etiqueta_"
															+ codigoSeccion
															+ "_"
															+ mapa.get(
																	i
																			+ "_campo_"
																			+ codigoSeccion
																			+ "_"
																			+ t)
																	.toString()
															+ "_" + x)
													+ "#COMILLA_SIMPLE#,"
													+ " "
													+ mapa.get(i
															+ "_parametrizacionjus_"
															+ codigoSeccion
															+ "_" + t)
													+ ","
													+ " "
													+ institucion
													+ " "
													+ " ) " + "";
											logger.info("sentencia insercion datos parametrizables mas corto >>>>>> \n "
													+ cadena);
											
											cadena=cadena.replace("'"," ");
											cadena=cadena.replace("#COMILLA_SIMPLE#","'");
											
											
											ingresojusparam5 = new PreparedStatementDecorator(
													con.prepareStatement(
															cadena,
															ConstantesBD.typeResultSet,
															ConstantesBD.concurrencyResultSet));
											resultado = ingresojusparam5
													.executeUpdate();
										}
									}
								} else {
									cadena = "INSERT INTO justificacion_art_param "
											+ "("
											+ "codigo, "
											+ "justificacion_art_sol, "
											+ "valor, "
											+ "seccion, "
											+ "etiqueta_seccion,"
											+ "campo,"
											+ "etiqueta_campo,"
											+ "parametrizacion_jus,"
											+ "institucion"
											+ ") "
											+ " VALUES"
											+ "(	" + " "
											+ UtilidadBD
													.obtenerSiguienteValorSecuencia(
															con,
															"seq_justificacion_art_param")
											+ ","
											+ " "
											+ codigoJustificacion
											+ ","
											+ " #COMILLA_SIMPLE#"
											+ mapa.get(i + "_valorcampo_"
													+ codigoSeccion + "_" + t)
											+ "#COMILLA_SIMPLE#,"
											+ " "
											+ codigoSeccion
											+ ","
											+ " #COMILLA_SIMPLE#"
											+ mapa.get(i + "_etiquetaseccion_"
													+ codigoSeccion + "_" + t)
											+ "#COMILLA_SIMPLE#,"
											+ " "
											+ mapa.get(
													i + "_campo_"
															+ codigoSeccion
															+ "_" + t)
													.toString()
											+ ","
											+ " #COMILLA_SIMPLE#"
											+ mapa.get(i + "_etiquetacampo_"
													+ codigoSeccion + "_" + t)
											+ "#COMILLA_SIMPLE#,"
											+ " "
											+ mapa.get(i
													+ "_parametrizacionjus_"
													+ codigoSeccion + "_" + t)
											+ ","
											+ " "
											+ institucion
											+ " "
											+ " ) " + "";
									logger.info("sentencia insercion datos parametrizables >>>>>> \n "
											+ cadena);
									
									cadena=cadena.replace("'"," ");
									cadena=cadena.replace("#COMILLA_SIMPLE#","'");
									
									ingresojusparam5 = new PreparedStatementDecorator(
											con.prepareStatement(
													cadena,
													ConstantesBD.typeResultSet,
													ConstantesBD.concurrencyResultSet));
									resultado = ingresojusparam5
											.executeUpdate();
								}
							}
					}
				}
			}

			return resultado;
		} catch (SQLException e) {
			logger.error("Error ingresando la justificacion de la solicitud de medicamentos " + e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			logger.error("Error ingresando la justificacion de la solicitud de medicamentos " + e);
			throw new RuntimeException(e);
		} finally {
			try {
				if(ingresoSolicitud != null) {
					ingresoSolicitud.close();
				}
				if(insertarSolicitudUOrdenXJust!=null){
					insertarSolicitudUOrdenXJust.close();
				}
				if (codigojussol != null) {
					codigojussol.close();
				}
				if (ingresojussub2 != null) {
					ingresojussub2.close();
				}
				if (ingresoSolicitud1 != null) {
					ingresoSolicitud1.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (ingresoSolicitud4 != null) {
					ingresoSolicitud4.close();
				}
				if (ingresoSolicitud5 != null) {
					ingresoSolicitud5.close();
				}
				if (pst2 != null) {
					pst2.close();
				}
				if (pst3 != null) {
					pst3.close();
				}
				if (ingresojusparam5 != null) {
					ingresojusparam5.close();
				}
				if (rsCodigojussol != null) {
					rsCodigojussol.close();
				}
			} catch (SQLException e) {
				Log4JManager.error("ERROR CERRANDO PS - RS " , e);
			}
	
		}
		
		
	}

	
	/**
	 * Permite consultar el codigo de una justificacion no pos de un articulo que proviene de una solicitud
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @param codigoArticulo
	 * @return
	 * @author jeilones
	 * @created 3/12/2012
	 */
	public static Integer consultarCodigoJustificacionSolicitud(Connection con,int codigoSolicitud,int codigoArticulo){
		PreparedStatement ps=null;
		ResultSet rs=null;
		try{
			StringBuffer consultarJustificacion=new StringBuffer("SELECT JUST.CODIGO AS CODIGO FROM INVENTARIOS.JUSTIFICACION_ART_SOL JUST ")
				.append("INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SOL_X_JUST ")
				.append("ON SOL_X_JUST.CODIGO_JUSTIFICACION = JUST.CODIGO ")
				.append("WHERE SOL_X_JUST.NUMERO_SOLICITUD = ? ")
				.append("AND JUST.ARTICULO = ? ");
			
			ps=con.prepareStatement(consultarJustificacion.toString());
			
			ps.setInt(1, codigoSolicitud);
			ps.setInt(2, codigoArticulo);
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				
				int codigoJustificacion=rs.getInt("CODIGO");
				
				return codigoJustificacion;
			}
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * Inserta antecedente
	 * 
	 * @param con
	 * @param mapa
	 * @param MedicamentosPosMap
	 * @param articulopos
	 * @param i
	 * @return
	 */
	private static int insertarAntecedente(Connection con, HashMap mapa,
			HashMap MedicamentosPosMap, String[] articulopos, int i) {

		int resultado = 0;
		try {

			int codigo = buscarAntecedente(con, mapa, articulopos, i);

			logger.info("consulto el codigo" + codigo);

			int numRegistros = existeAntecedentes(con, mapa, articulopos, i);

			logger.info("consulto si existen antecedentes para el articulo "
					+ numRegistros);

			// Se insertan si no existen los registros en las tablas de nivel
			// superior a ant_medicamentos

			int ins = 1, ins1 = 1;

			if (existeAntecedentesPaciente(
					con,
					Utilidades.convertirAEntero(mapa.get(i + "_codigopersona")
							+ "")) < 1)
				ins = insertarAntecedentesPaciente(
						con,
						Utilidades.convertirAEntero(mapa.get(i
								+ "_codigopersona")
								+ ""));
			if (existeAnteceMedicamentos(
					con,
					Utilidades.convertirAEntero(mapa.get(i + "_codigopersona")
							+ "")) < 1)
				ins1 = insertarAnteceMedicamentos(
						con,
						Utilidades.convertirAEntero(mapa.get(i
								+ "_codigopersona")
								+ ""));

			if (numRegistros == 0 && ins > 0 && ins1 > 0) {
				String cadena = "INSERT INTO ant_medicamentos " + "( "
						+ "codigo_paciente, " + "codigo, " + "nombre, "
						+ "codigo_articulo, " + "dosificacion, "
						+ "frecuencia, " + "dosis_diaria, " + "cantidad, "
						+ "tiempo_tratamiento, " + "tipo_frecuencia, "
						+ "unidosis, " + "fecha_inicio, "
						+ "fecha_finalizacion " + ") " + " VALUES " + "(	"
						+ " "
						+ mapa.get(i + "_codigopersona")
						+ ","
						+ " "
						+ codigo
						+ ","
						+ " getdescripcionarticulo("
						+ articulopos[0]
						+ "),"
						+ " '"
						+ articulopos[0]
						+ "',"
						+ " '"
						+ MedicamentosPosMap.get(i + "_dosificacion")
						+ "',"
						+ " '"
						+ MedicamentosPosMap.get(i + "_frecuencia")
						+ "',"
						+ " '"
						+ MedicamentosPosMap.get(i + "_dosisdiaria")
						+ "',"
						+ " '"
						+ MedicamentosPosMap.get(i + "_cantidad")
						+ "',"
						+ " '"
						+ MedicamentosPosMap.get(i + "_tiempotratamiento")
						+ "', "
						+ " '"
						+ MedicamentosPosMap.get(i + "_tipofrecuencia")
						+ "', "
						+ " '"
						+ MedicamentosPosMap.get(i + "_unidosis")
						+ "', " + "'', '' " + " ) " + "";

				logger.info("sentencia insercion en antecedentes >>>>>> \n "
						+ cadena);
				PreparedStatementDecorator ingresoSolicitud2 = new PreparedStatementDecorator(
						con.prepareStatement(cadena,
								ConstantesBD.typeResultSet,
								ConstantesBD.concurrencyResultSet));
				resultado = ingresoSolicitud2.executeUpdate();
				logger.info("inserto el antecedente: " + resultado);
			}
		} catch (Exception e) {
			logger.info("error insertando antecedente " + e);
		}

		return resultado;

	}

	/**
	 * @param con
	 * @param i
	 * @return
	 */
	private static int insertarAnteceMedicamentos(Connection con, int i) {
		int resultado = 0;
		try {
			String cadena = "INSERT INTO antece_medicamentos(codigo_paciente, fecha, hora) "
					+ " VALUES ("
					+ i
					+ ",'"
					+ Utilidades.capturarFechaBD()
					+ "','" + UtilidadFecha.getHoraActual() + "')" + "";

			logger.info("sentencia insercion en antecedentes >>>>>> \n "
					+ cadena);
			PreparedStatementDecorator ingresoSolicitud2 = new PreparedStatementDecorator(
					con.prepareStatement(cadena, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			resultado = ingresoSolicitud2.executeUpdate();
			logger.info("inserto el antecedente" + resultado);
		} catch (Exception e) {
			logger.info("error insertando antecedente paciente " + e);
		}

		return resultado;

	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @return
	 */
	private static int insertarAntecedentesPaciente(Connection con, int i) {

		int resultado = 0;
		try {

			String cadena = "INSERT INTO antecedentes_pacientes " + "( "
					+ " codigo_paciente " + ") " + " VALUES " + "(	" + " " + i
					+ " " + " ) ";

			logger.info("sentencia insercion en antecedentes  medicamentos>>>>>> \n "
					+ cadena);
			PreparedStatementDecorator ingresoSolicitud2 = new PreparedStatementDecorator(
					con.prepareStatement(cadena, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			resultado = ingresoSolicitud2.executeUpdate();
			logger.info("inserto el antecedente" + resultado);
		} catch (Exception e) {
			logger.info("error insertando antecedente " + e);
		}

		return resultado;
	}

	/**
	 * consulta si existen antecedentes de antece_medicamentos de una persona
	 * 
	 * @param con
	 * @param mapa
	 * @param articulopos
	 * @return
	 */
	private static int existeAnteceMedicamentos(Connection con, int i) {
		HashMap mapa1 = new HashMap();
		String consulta = "select * from antece_medicamentos where codigo_paciente="
				+ i;
		logger.info("\n\n\n\n [antece_medicamentos] \n\n\n\n" + consulta);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (Exception e) {
			logger.info("error consultando antecedentes medicamentos" + e);
		}
		return Utilidades.convertirAEntero(mapa1.get("numRegistros") + "");
	}

	/**
	 * consulta si existen antecedentes de antece_medicamentos de una persona
	 * 
	 * @param con
	 * @param mapa
	 * @param articulopos
	 * @return
	 */
	private static int existeAntecedentesPaciente(Connection con, int i) {
		HashMap mapa1 = new HashMap();
		String consulta = "select * from antecedentes_pacientes where codigo_paciente="
				+ i;
		logger.info("\n\n\n\n [antecedentes_pacientes] \n\n\n\n" + consulta);
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (Exception e) {
			logger.info("error consultando antecedentes paciente" + e);
		}
		return Utilidades.convertirAEntero(mapa1.get("numRegistros") + "");
	}

	/**
	 * consulta si existen antecedentes de medicamentos de una persona
	 * 
	 * @param con
	 * @param mapa
	 * @param articulopos
	 * @return
	 */
	private static int existeAntecedentes(Connection con, HashMap mapa,
			String[] articulopos, int i) {
		HashMap mapa1 = new HashMap();
		String consulta = "select * from ant_medicamentos where codigo_paciente="
				+ mapa.get(i + "_codigopersona");
		String comp = " and codigo_articulo=" + articulopos[0];
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta + comp,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
		} catch (Exception e) {
			logger.info("error consultando antecedentes " + e);
		}
		return Utilidades.convertirAEntero(mapa1.get("numRegistros") + "");
	}

	/**
	 * Busca antecedentes_medicametnos de una persona
	 * 
	 * @param con
	 * @param mapa
	 * @param articulopos
	 * @return
	 */
	private static int buscarAntecedente(Connection con, HashMap mapa,
			String[] articulopos, int i) {
		HashMap mapa1 = new HashMap();
		int res = 0;
		String consulta = "select max(codigo) as codigo from ant_medicamentos where codigo_paciente="
				+ mapa.get(i + "_codigopersona");
		logger.info(i + "consulta antecedentes persona >>>>>" + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			mapa1 = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()));
			logger.info("mapa aconsulta antecedente >>>>" + mapa1);
		} catch (Exception e) {
			logger.info("error consultando antecedentes " + e);
		}

		if (mapa1.get("codigo_0").toString().equals(""))
			res = 1;
		else
			res = (Utilidades.convertirAEntero(mapa1.get("codigo_0") + "") + 1);

		return res;
	}

	/**
	 * Consulta subcuentas de una solicitud (articulo nopos) justificada
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> subCuentas(Connection con,
			int numeroSolicitudOrden, int art, boolean esArticulo, boolean esOrdenAmbulatoria) {
		HashMap<String, Object> mapaP = new HashMap<String, Object>();

		PreparedStatementDecorator pst2 = null;

		String consulta;

		if (esArticulo) {
			consulta = "SELECT distinct "
					+ "jar.subcuenta, "
					+ "sub.convenio,"
					+ "getObtenerNombreSubCuenta(jar.subcuenta) AS nombre "
					+ "FROM justificacion_art_resp jar "
					+ "INNER JOIN "
					+ "sub_cuentas sub "
					+ "ON (jar.subcuenta=sub.sub_cuenta) "
					+ "INNER JOIN "
					+ "justificacion_art_sol jas "
					+ "ON (jar.justificacion_art_sol=jas.codigo) "
					
					+ (esOrdenAmbulatoria ?"INNER JOIN INVENTARIOS.ORD_AMB_JUST_ART_SOL OAS ON OAS.CODIGO_JUSTIFICACION = jas.codigo ":"INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo ")
					
					+"WHERE jas.articulo=? ";
			
			consulta += esOrdenAmbulatoria ? " AND OAS.CODIGO_ORDEN = ?" : " AND SJ.NUMERO_SOLICITUD = ?";
			
			
			
		} else {
			consulta = "SELECT distinct "
					+ "jsr.sub_cuenta AS subcuenta, "
					+ "sub.convenio,"
					+ "getObtenerNombreSubCuenta(jsr.sub_cuenta) AS nombre "
					+ "FROM justificacion_serv_resp jsr "
					+ "INNER JOIN "
					+ "sub_cuentas sub "
					+ "ON (jsr.sub_cuenta=sub.sub_cuenta) "
					+ "INNER JOIN "
					+ "justificacion_serv_sol jss "
					+ "ON (jsr.justificacion_serv_sol=jss.codigo) WHERE jss.servicio=? ";
		
			consulta += esOrdenAmbulatoria ? " AND jss.orden_ambulatoria = ?" : " AND jss.solicitud = ?";
		
		}

		try {
			pst2 = new PreparedStatementDecorator(con.prepareStatement(
					consulta, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			pst2.setInt(1, art);
			pst2.setInt(2, numeroSolicitudOrden);

			mapaP = UtilidadBD.cargarValueObject(
					new ResultSetDecorator(pst2.executeQuery()), true, true);

			mapaP.put("INDICES", indicesMap2);

		} catch (SQLException e) {
            logger.error("ERROR SQLException subCuentas: ", e);
     
        }catch(Exception ex){
			logger.error("ERROR Exception subCuentas: ", ex);
		
        }finally{
			try{
				if(pst2 != null){
					pst2.close();
				}		
			}catch (SQLException se) {
				logger.error("###########  Error close PreparedStatement", se);
			}
		}
		return mapaP;
	}

	// %%%%%%%%%%%%%%%%%%%%%%% anexo 584 INGRESAR CONSULTAR/MODIFICAR
	// JUSTIFICACION ARTICULOS NO POS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
	/**
	 * Metodo que carga informacion de ingreso
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @param tipocodigo
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRangocon(
			Connection con, HashMap ingresosMap, HashMap filtro,
			String articulos, String fechaini, String fechafin, int tipocodigo,String where,String group) {
		String[] viapac = filtro.get("viaIngreso").toString()
				.split(ConstantesBD.separadorSplit);

		logger.info("datos de la consulta filtros \n\n\n\n "
				+ "mapa filtros >>>>" + filtro + "\n\n\n\n"
				+ "cadena articulos>>>>> " + articulos + "\n\n\n\n" + "fechas "
				+ fechaini + "--------" + fechafin);
		logger.info("Entramos a  cargarInfoIngresoConsultarModificarRangocon \n\n\n\n ");
		String consulta = "select  ";
		if (filtro.get("tipoRompimiento").toString().equals("centro_costo_")) {
			consulta += "	s.centro_costo_solicitante as codigo_centro_costo,"
					+ "	getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,";
		}
		consulta += "	getdescarticulosincodigo(jas.articulo) as articulo,"
				+ "	SUM (gettotaladminfarmacia(jas.articulo, jas.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+")) as cantotadmin,"
				+ "	SUM (jar.cantidad) as cantsol,"
				+ "	SUM (getValorTotCargoAsociadoTot(s.numero_solicitud,jas.articulo)) as precioventot,"
				+ "	SUM(gettotaladminfarmacia(jas.articulo, jas.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+") * getcostounitario(jas.numero_solicitud, s.tipo,jas.articulo)) as preciocostot, "
				+ "	jas.articulo as codigo_art,"
				+ "   CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END as control,";

		if (filtro.get("tipoRompimiento").toString().equals("piso_"))
			consulta += "	getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,";

		if (filtro.get("tipoRompimiento").toString().equals("estadojus_"))
			consulta += "	getintegridaddominio(jar.estado) as estadojus, ";

		consulta += "	getnombrepersona(jaf.profesional_responsable) as codigomed "
				+ " FROM "
				+ "	ingresos i "
				+ " inner join "
				+ "	cuentas c on (c.id_ingreso=i.id) "
				+ " inner join "
				+ "	solicitudes s on (c.id=s.cuenta) "
				+ " inner join "
				+ "	sub_cuentas sc on (sc.ingreso=i.id) "
				+ " INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.NUMERO_SOLICITUD = s.numero_solicitud "
				+ " inner join "
				+ "	justificacion_art_sol jas on (jas.codigo= SJ.codigo_justificacion) "
				+ " inner join "
				+ "	justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "
				+ " inner join "
				+ "	articulo art on (art.codigo=jas.articulo) ";
		consulta += "	  inner join "
				+ "		justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol) ";
		consulta += where;

		if (!viapac[0].toString().equals("-1")) {
			consulta += " and c.via_ingreso=" + viapac[0] + " ";
		}
		if (!viapac[0].toString().equals("-1")
				&& !viapac[1].toString().equals("")) {
			consulta += " and c.tipo_paciente='" + viapac[1] + "' ";
		}
		if (!filtro.get("centrocosto").toString().equals("")) {
			consulta += " and c.area=" + filtro.get("centrocosto") + " ";
		}
		if (!filtro.get("convenio").toString().equals("")) {
			consulta += " and sc.convenio=" + filtro.get("convenio") + " ";
		}
		if (!filtro.get("estadojus").toString()
				.equals(ConstantesBD.codigoNuncaValido + "")) {
			consulta += " and jar.estado='" + filtro.get("estadojus") + "' ";
		}
		if (!filtro.get("piso").toString().equals("")) {
			consulta += " and getcodigopisocuenta(c.id,c.via_ingreso)="
					+ filtro.get("piso") + " ";
		}
		if (!filtro.get("profesional").toString().equals("")) {
			consulta += " and jaf.profesional_responsable ="
					+ filtro.get("profesional") + " ";
		}
		if (!articulos.equals("")) {
			consulta += " and jas.articulo IN ("
					+ ConstantesBD.codigoNuncaValido + "," + articulos + ""
					+ ConstantesBD.codigoNuncaValido + ") ";
		}
		// falta el filtro por profecional responsable de la justificacion

		consulta += group;

		logger.info("consulta info ingresos >>>>" + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ingresosMap = UtilidadBD.cargarValueObject(new ResultSetDecorator(
					ps.executeQuery()));

		} catch (Exception e) {
			logger.info("error consultando articulos del ingreso " + e
					+ "\n\n\n" + consulta);
		}

		return ingresosMap;
	}

	/**
	 * Metodo q consulta informacion del ingreso
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @param tipocodigo
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRango(
			Connection con, HashMap ingresosMap, HashMap filtro,
			String articulos, String fechaini, String fechafin, int tipocodigo,String where ) {
		String[] viapac = filtro.get("viaIngreso").toString()
				.split(ConstantesBD.separadorSplit);

		logger.info("datos de la consulta filtros \n\n\n\n "
				+ "mapa filtros >>>>" + filtro + "\n\n\n\n"
				+ "cadena articulos>>>>> " + articulos + "\n\n\n\n" + "fechas "
				+ fechaini + "--------" + fechafin);

		String consulta = " SELECT distinct * "
				+ "   FROM "
				+ "   (select  "
				+ "	s.centro_costo_solicitante as codigo_centro_costo,"
				+ "	getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,"
				+ "	getConvenioSubcuenta(jar.subcuenta) as codigo_convenio,"
				+ "	getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id) as convenio,"
				+ "	s.numero_solicitud as solicitud,"
				+ "	s.fecha_solicitud as fecha,"
				+ "	getdescarticulosincodigo(jas.articulo) as articulo,"
				+ "	i.id as ingreso,"
				+ "	coalesce(getconsecutivoingreso(i.id), '') as ingconsecutivo, "
				+ // 987
				"	jar.subcuenta as subcuenta,"
				+ "	getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad,"
				+ "	c.via_ingreso as codviaingreso,"
				+ "	c.tipo_paciente as codtipopaciente,"
				+ "	case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1  "
				+ "	then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente) "
				+ "	else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,  "
				+ "	i.fecha_ingreso || '/' || i.hora_ingreso as fechahora, "
				+ "	i.consecutivo as noingreso,"
				+ "	coalesce(inventarios.getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta)||'','PENDIENTE') as valor,"
				+ "	getCantidadTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) as cantidadcargada,"
				+ "	getidpaciente(i.codigo_paciente) as tipoid,"
				+ "	getnombrepersona(i.codigo_paciente) as nombrepac,"
				+ "	jas.articulo as codigoart, "
				+ "	c.id as codigocuenta,"
				+ "	i.codigo_paciente as codpaciente, "
				+ "	getcamacuenta(c.id,c.via_ingreso) as cama,"
				+ "	jas.consecutivo as nojus,"
				+ "	jar.cantidad as cantotorden,"
				+ "	getdespacho(jas.articulo, SJ.numero_solicitud) as cantotdespacho,"
				+

				// "	gettotaladminfarmacia(jas.articulo, SJ.numero_solicitud,false) as cantotadmin,"
				// +
				"	gettotaladminfarmacia(jas.articulo, SJ.numero_solicitud,"
				+ ValoresPorDefecto.getValorFalseParaConsultas()
				+ ") as cantotadmin,"
				+

				"	jas.tiempo_tratamiento as tiempotratamiento,"
				+ "	gettotaladminfarresponsable(SJ.numero_solicitud,jas.articulo,jar.subcuenta) as cantotconv,"
				+ "	inventarios.getValorTotalCargoAsociado(s.numero_solicitud,jas.articulo,jar.subcuenta) * gettotaladminfarresponsable(SJ.numero_solicitud,jas.articulo,jar.subcuenta) as precioventot,"
				+ "	getcostounitario(SJ.numero_solicitud, s.tipo,jas.articulo) as costounitario,"
				+ "	gettotaladminfarmacia(jas.articulo, SJ.numero_solicitud,"
				+ ValoresPorDefecto.getValorFalseParaConsultas()
				+ ") * getcostounitario(SJ.numero_solicitud, s.tipo,jas.articulo) as preciocostot, "
				+ "	jas.articulo as codigo_art,"
				+ "	getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) as piso,"
				+ "	getintegridaddominio(jar.estado) as estadojus, "
				+ "   CASE WHEN art.categoria=1 THEN 'NO' WHEN art.categoria=2 THEN 'SI' ELSE '' END as control, "
				+ " inventarios.getequivalentes (SJ.numero_solicitud,jas.articulo) as cod_art_sust ";
		consulta += " FROM "
				+ "	ingresos i "
				+ " inner join "
				+ "	cuentas c on (c.id_ingreso=i.id) "
				+ " inner join "
				+ "	solicitudes s on (c.id=s.cuenta) "
				+ " inner join "
				+ "	sub_cuentas sc on (sc.ingreso=i.id) "
				+ " INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.NUMERO_SOLICITUD = s.numero_solicitud "
				+ " inner join "
				+ "	justificacion_art_sol jas on (jas.codigo=SJ.codigo_justificacion) "
				+ " inner join "
				+ "	justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "
				+ " inner join "
				+ "	articulo art on (art.codigo=jas.articulo) ";
		if (!filtro.get("profesional").toString().equals("")) {
			consulta += " inner join justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol) ";
		}

		consulta += where;

		if (!viapac[0].toString().equals("-1")) {
			consulta += " and c.via_ingreso=" + viapac[0] + " ";
		}
		if (!viapac[0].toString().equals("-1")
				&& !viapac[1].toString().equals("")) {
			consulta += " and c.tipo_paciente='" + viapac[1] + "' ";
		}
		if (!filtro.get("centrocosto").toString().equals("")) {
			consulta += " and c.area=" + filtro.get("centrocosto") + " ";
		}
		if (!filtro.get("convenio").toString().equals("")) {
			consulta += " and getConvenioSubcuenta(jar.subcuenta)="
					+ filtro.get("convenio") + " ";
		}
		if (!filtro.get("estadojus").toString()
				.equals(ConstantesBD.codigoNuncaValido + "")) {
			consulta += " and jar.estado='" + filtro.get("estadojus") + "' ";
		}
		if (!filtro.get("piso").toString().equals("")) {
			consulta += " and getcodigopisocuenta(c.id,c.via_ingreso)="
					+ filtro.get("piso") + " ";
		}
		if (!filtro.get("profesional").toString().equals("")) {
			consulta += " and jaf.profesional_responsable ="
					+ filtro.get("profesional") + " ";
		}
		if (!articulos.equals("")) {
			consulta += " and jas.articulo IN ("
					+ ConstantesBD.codigoNuncaValido + "," + articulos + ""
					+ ConstantesBD.codigoNuncaValido + ") ";
		}

		/*
		 * consulta+="GROUP BY "+ "sc.convenio," +
		 * "s.centro_costo_solicitante,"+ "jar.estado," + "piso," +
		 * "s.numero_solicitud," + "s.fecha_solicitud," + "i.id ," +
		 * "sc.sub_cuenta ," + "c.via_ingreso ," + "c.tipo_paciente," +
		 * "i.consecutivo," + "jas.articulo," + "c.id," + "i.codigo_paciente," +
		 * "jas.consecutivo," + "jar.cantidad," + "jas.tiempo_tratamiento," +
		 * "i.fecha_ingreso," + "i.hora_ingreso," + "jas.numero_solicitud," +
		 * "s.tipo," + "jar.subcuenta, " + "art.categoria " +
		 */
		consulta += " ) tabla ORDER BY " + "  tabla.convenio,  "
				+ "  tabla.fechahora," + "  tabla.convenio ";

		logger.info("consulta info ingresos >>>>" + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ingresosMap = UtilidadBD.cargarValueObject(new ResultSetDecorator(
					ps.executeQuery()));

			
			int total=Utilidades.convertirAEntero(ingresosMap.get(
					"numRegistros").toString());
			logger.info("num registros ingreos map para cargar la cantidad >>>>"
					+ total);
			for (int i = 0; i < total; i++) {
				
				/*
				 * MT 5285 consultar informacion de articulo equivalente
				 * codigo[0] = codigo articulo equivalente
				 * codigo[1] = codigo articulo principal
				 * */
					
				String codigos[];
				if(ingresosMap.get("cod_art_sust_"+i)!=null&&!UtilidadTexto.isEmpty(ingresosMap.get("cod_art_sust_"+i).toString())){
					codigos=ingresosMap.get("cod_art_sust_"+i).toString().split("@");
					HashMap mapa = EquivalentesDeInventario.consultarArticulo(con, Integer.parseInt(codigos[1]), Integer.parseInt(codigos[0]));
					
					ingresosMap.put("codigo_articulo_principal_"+i, codigos[1]);
					ingresosMap.put("desc_articulo_principal_"+i, mapa.get("descArtPrincipal"));
				}
				
				logger.info("datos pa consultar la cantidad >>>"
						+ ingresosMap.get("subcuenta_" + i).toString()
						+ "******"
						+ ingresosMap.get("solicitud_" + i).toString()
						+ "******"
						+ ingresosMap.get("codigoart_" + i).toString()
						+ "******"
						+ ingresosMap.get("codigocuenta_" + i).toString());

				ingresosMap
						.put("cantidad_" + i,
								cargarCantidad(con,
										ingresosMap.get("subcuenta_" + i)
												.toString(),
										ingresosMap.get("solicitud_" + i)
												.toString(),
										ingresosMap.get("codigoart_" + i)
												.toString(),
										ingresosMap.get("codigocuenta_" + i)
												.toString()));
				logger.info("cantidad >>>>" + ingresosMap.get("cantidad_" + i));
			}

		} catch (Exception e) {
			logger.info("error consultando articulos del ingreso " + e
					+ "\n\n\n" + consulta);
		}

		return ingresosMap;
	}

	/**
	 * Metodo q consulta informacion del ingreso
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @param tipocodigo
	 * @return
	 */
	public static HashMap cargarInfoIngresoRango(Connection con,
			HashMap ingresosMap, HashMap filtro, String articulos,
			String fechaini, String fechafin, int tipocodigo,String where) {
		String[] viapac = filtro.get("viaIngreso").toString()
				.split(ConstantesBD.separadorSplit);

		logger.info("datos de la consulta filtros \n\n\n\n "
				+ "mapa filtros >>>>" + filtro + "\n\n\n\n"
				+ "cadena articulos>>>>> " + articulos + "\n\n\n\n" + "fechas "
				+ fechaini + "--------" + fechafin);

		String consulta = "select  "
				+ "	s.centro_costo_solicitante as codigo_centro_costo,"
				+ "	getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,"
				+ "	sc.convenio as codigo_convenio,"
				+ "	getnombreconvenioresponsable(sc.convenio, i.id) as convenio,"
				+ "	s.numero_solicitud as solicitud,"
				+ "	s.fecha_solicitud as fecha,"
				+ "	getdescarticulosincodigo(jpa.articulo) as articulo,"
				+ "	i.id as ingreso, "
				+ "	coalesce(getconsecutivoingreso(i.id), '') as ingconsecutivo, "
				+ // 987
				"	sc.sub_cuenta as subcuenta,"
				+ "	getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad,"
				+ "	c.via_ingreso as codviaingreso,"
				+ "	c.tipo_paciente as codtipopaciente,"
				+ "	case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1  "
				+ "	then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente) "
				+ "	else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,  "
				+ "	i.fecha_ingreso || '/' || i.hora_ingreso as fechahora, "
				+ "	i.consecutivo as noingreso,"
				+ "	coalesce(getValorTotalCargoAsociado(s.numero_solicitud,jpa.articulo,sc.sub_cuenta)||'','PENDIENTE') as valor,"
				+ "	getCantidadTotalCargoAsociado(s.numero_solicitud,jpa.articulo,sc.sub_cuenta) as cantidadcargada,"
				+ "	getidpaciente(i.codigo_paciente) as tipoid,"
				+ "	getnombrepersona(i.codigo_paciente) as nombrepac,"
				+ "	jpa.articulo as codigoart, " + "	c.id as codigocuenta,"
				+ "	i.codigo_paciente as codpaciente," + "	 ";
		if (tipocodigo == 1) {
			consulta += "	jpa.articulo as codigo_art ";
		} else if (tipocodigo == 2) {
			consulta += "	art.codigo_interfaz as codigo_art ";
		} else if (tipocodigo == 3) {
			consulta += "coalesce(jpa.articulo || '',' ') || ' - ' || coalesce(art.codigo_interfaz || '', ' ') as codigo_art";
		}
		consulta +=where;

		
		
		if (!viapac[0].toString().equals("-1")) {
			consulta += " and c.via_ingreso=" + viapac[0] + " ";
		}
		if (!viapac[0].toString().equals("-1")
				&& !viapac[1].toString().equals("")) {
			consulta += " and c.tipo_paciente='" + viapac[1] + "' ";
		}
		if (!filtro.get("centrocosto").toString().equals("")) {
			consulta += " and c.area=" + filtro.get("centrocosto") + " ";
		}
		if (!filtro.get("convenio").toString().equals("")) {
			consulta += " and sc.convenio=" + filtro.get("convenio") + " ";
		}
		if (!articulos.equals("")) {
			consulta += " and jpa.articulo IN ("
					+ ConstantesBD.codigoNuncaValido + "," + articulos + ""
					+ ConstantesBD.codigoNuncaValido + ") ";
		}

		consulta += "ORDER BY  fechahora";

		logger.info("consulta info ingresos >>>>" + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ingresosMap = UtilidadBD.cargarValueObject(new ResultSetDecorator(
					ps.executeQuery()));

			logger.info("num registros ingreos map para cargar la cantidad >>>>"
					+ Utilidades.convertirAEntero(ingresosMap.get(
							"numRegistros").toString()));
			for (int i = 0; i < Utilidades.convertirAEntero(ingresosMap.get(
					"numRegistros").toString()); i++) {
				logger.info("datos pa consultar la cantidad >>>"
						+ ingresosMap.get("subcuenta_" + i).toString()
						+ "******"
						+ ingresosMap.get("solicitud_" + i).toString()
						+ "******"
						+ ingresosMap.get("codigoart_" + i).toString()
						+ "******"
						+ ingresosMap.get("codigocuenta_" + i).toString());

				ingresosMap
						.put("cantidad_" + i,
								cargarCantidad(con,
										ingresosMap.get("subcuenta_" + i)
												.toString(),
										ingresosMap.get("solicitud_" + i)
												.toString(),
										ingresosMap.get("codigoart_" + i)
												.toString(),
										ingresosMap.get("codigocuenta_" + i)
												.toString()));
				logger.info("++++++++-----------------+++++++++++++");
				logger.info("cantidad >>>>" + ingresosMap.get("cantidad_" + i));
				logger.info("ingreso: " + ingresosMap.get("ingreso_" + i));
			}

		} catch (Exception e) {
			logger.info("error consultando articulos del ingreso " + e
					+ "\n\n\n" + consulta);
		}

		return ingresosMap;
	}

	/**
	 * Metodo q consulta informacion del ingreso
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public static HashMap cargarInfoIngresoJus(Connection con,
			HashMap ingresosMap, String ingreso, String cuenta) {
		String consulta = "(select  "
				+ "	s.centro_costo_solicitante ||'' as codigo_centro_costo,"
				+ "	getnomcentrocosto(s.centro_costo_solicitante)||'' as centro_costo,"
				+ "	getConvenioSubcuenta(jar.subcuenta)||'' as codigo_convenio,"
				+ "	getnombreconvenioresponsable(getConvenioSubcuenta(jar.subcuenta), i.id)||'' as convenio,"
				+ "	s.numero_solicitud||'' as solicitud,"
				+ "	s.fecha_solicitud||'' as fecha,"
				+ "	jas.articulo||'' as codigo_articulo,"
				+ "	jas.unidosis as unidosis,"
				+ "	getdescarticulosincodigo(jas.articulo)||'' as articulo,"
				+ "	i.id||'' as ingreso,"
				+ "	coalesce(getconsecutivoingreso(i.id), '') as ingconsecutivo, "
				+ // 987
				"	jar.subcuenta||'' as subcuenta,"
				+ "	getSubcuentaCantidadJus(s.numero_solicitud)||'' as subcuentacantidad,"
				+ "	jas.fecha_modifica||'' as fechajus,"
				+ "	jas.consecutivo||'' as nojus, "
				+ "	getnombrepersona(jaf.profesional_responsable)||'' as profesional, "
				+ "	jas.tipo_jus AS tipo_jus "
				+ " from "
				+ "	ingresos i "
				+ " inner join "
				+ "	cuentas c on (c.id_ingreso=i.id) "
				+ " inner join "
				+ "	solicitudes s on (c.id=s.cuenta) "
				+ " inner join "
				+ "	sub_cuentas sc on (sc.ingreso=i.id) "
				
				+ "INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SOL_X_JUST "
				+ "ON SOL_X_JUST.NUMERO_SOLICITUD=S.NUMERO_SOLICITUD "
				+ "INNER JOIN JUSTIFICACION_ART_SOL JAS "
				+ "ON (jas.CODIGO=SOL_X_JUST.CODIGO_JUSTIFICACION) "
				
				/*+ " inner join "
				+ "	justificacion_art_sol jas on (s.numero_solicitud=jas.numero_solicitud) "*/
				
				+ " inner join "
				+ "	justificacion_art_fijo jaf on (jas.codigo=jaf.justificacion_art_sol) "
				+ " inner join "
				+ "	justificacion_art_resp jar on (jas.codigo=jar.justificacion_art_sol) "
				+ " where " + "	i.id="
				+ ingreso
				+ " and c.id="
				+ cuenta
				+ " and sc.nro_prioridad=1"
				+ " and s.estado_historia_clinica!="
				+ ConstantesBD.codigoEstadoHCAnulada
				+ ")"
				+ "UNION "
				+ "(select  "
				+ "	'' as codigo_centro_costo,"
				+ "	'' as centro_costo,"
				+ "	'' as codigo_convenio,"
				+ "	'' as convenio,"
				+ "	s.numero_solicitud||'' as solicitud,"
				+ "	s.fecha_solicitud||'' as fecha,"
				+ "	jpa.articulo||'' as codigo_articulo,"
				+ "	'' as unidosis,"
				+ "	getdescarticulosincodigo(jpa.articulo)||'' as articulo,"
				+ "	i.id||'' as ingreso,"
				+ "	coalesce(getconsecutivoingreso(i.id), '') as ingconsecutivo, "
				+ // 987
				"	sc.sub_cuenta||'' as subcuenta,"
				+ "	getSubcuentaCantidadJus(s.numero_solicitud)||'' as subcuentacantidad, "
				+ "	'' as fechajus,"
				+ "	'' as nojus, "
				+ "	'' as profesional, "
				+ "	'' AS tipo_jus "
				+ " from "
				+ "	ingresos i "
				+ " inner join "
				+ "	cuentas c on (c.id_ingreso=i.id) "
				+ " inner join "
				+ "	solicitudes s on (c.id=s.cuenta) "
				+ " inner join "
				+ "	sub_cuentas sc on (sc.ingreso=i.id) "
				+ " inner join "
				+ "	jus_pendiente_articulos jpa on (s.numero_solicitud=jpa.numero_solicitud) "
				+ " where "
				+ "	i.id="
				+ ingreso
				+ " and c.id="
				+ cuenta
				+ " and sc.nro_prioridad=1"
				+ " 	and s.estado_historia_clinica!="
				+ ConstantesBD.codigoEstadoHCAnulada
				+ ") "
				+ "UNION "
				+ "(select distinct "
				+ "	'' as codigo_centro_costo,"
				+ "	'' as centro_costo,"
				+ "	'' as codigo_convenio,"
				+ "	'' as convenio,"
				+ "	s.numero_solicitud||'' as solicitud,"
				+ "	s.fecha_solicitud||'' as fecha,"
				+ "	das.articulo||'' as codigo_articulo,"
				+ "	'' as unidosis,"
				+ "	getdescarticulosincodigo(das.articulo)||'' as articulo,"
				+ "	i.id||'' as ingreso,"
				+ "	coalesce(getconsecutivoingreso(i.id), '') as ingconsecutivo, "
				+ // 987
				"	sc.sub_cuenta||'' as subcuenta,"
				+ "	getSubcuentaCantidadJus(s.numero_solicitud)||'' as subcuentacantidad, "
				+ "	'' as fechajus,"
				+ "	'' as nojus, "
				+ "	'x' as profesional, "
				+ "	'' AS tipo_jus "
				+ " from "
				+ "	ingresos i "
				+ " inner join "
				+ "	cuentas c on (c.id_ingreso=i.id) "
				+ " inner join "
				+ "	solicitudes s on (c.id=s.cuenta) "
				+ " inner join "
				+ "	sub_cuentas sc on (sc.ingreso=i.id) "
				+ " inner join "
				+ "	desc_atributos_solicitud das on (s.numero_solicitud=das.numero_solicitud) "
				+ " WHERE "
				+ "	i.id="
				+ ingreso
				+ " "
				+
				// "	and c.id="+cuenta+" " +
				"	and sc.nro_prioridad=1"
				+ " 	and s.estado_historia_clinica!="
				+ ConstantesBD.codigoEstadoHCAnulada + ") ";

		logger.info("*********--------------------*************-----------------********");
		logger.info("consulta info ingresos >>>>" + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ingresosMap = UtilidadBD.cargarValueObject(new ResultSetDecorator(
					ps.executeQuery()));
		} catch (Exception e) {
			logger.info("error consultando articulos del ingreso " + e
					+ "\n\n\n" + consulta);
		}

		return ingresosMap;
	}

	/**
	 * Metodo q consulta informacion del ingreso
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param ingreso
	 * @param cuenta
	 * @return
	 */
	public static HashMap cargarInfoIngreso(Connection con,
			HashMap ingresosMap, String ingreso, String cuenta) {
		String consulta = "select  "
				+ "	s.centro_costo_solicitante as codigo_centro_costo,"
				+ "	getnomcentrocosto(s.centro_costo_solicitante) as centro_costo,"
				+ "	sc.convenio as codigo_convenio,"
				+ "	getnombreconvenio(sc.convenio) as convenio,"
				+ "	s.numero_solicitud as solicitud,"
				+ "	s.fecha_solicitud as fecha,"
				+ "	jpa.articulo as codigo_articulo,"
				+ "	getdescarticulosincodigo(jpa.articulo) as articulo,"
				+ "	i.id as ingreso,"
				+ "	coalesce(getconsecutivoingreso(i.id), '') as ingconsecutivo, "
				+ // 987
				"	sc.sub_cuenta as subcuenta,"
				+ "	getSubcuentaCantidadJus(s.numero_solicitud) as subcuentacantidad, "
				+ "	jpa.tipo_jus AS tipo_jus, "
				+ "	djpa.cantidad AS cantidad "
				+ " from "
				+ "	ingresos i "
				+ " inner join "
				+ "	cuentas c on (c.id_ingreso=i.id) "
				+ " inner join "
				+ "	solicitudes s on (c.id=s.cuenta) "
				+ " inner join "
				+ "	sub_cuentas sc on (sc.ingreso=i.id) "
				+ " inner join "
				+ "	jus_pendiente_articulos jpa on (s.numero_solicitud=jpa.numero_solicitud) "
				+ " left outer join "
				+ "	det_jus_pendiente_articulos djpa on (djpa.jus_pendiente_articulos=jpa.codigo) "
				+ " where " + "	i.id=" + ingreso + " and c.id=" + cuenta
				+ " and sc.nro_prioridad=1"
				+ " and s.estado_historia_clinica!="
				+ ConstantesBD.codigoEstadoHCAnulada;

		logger.info("+++++++++************+++++++++++++++***********");
		logger.info("consulta info ingresos >>>>" + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ingresosMap = UtilidadBD.cargarValueObject(new ResultSetDecorator(
					ps.executeQuery()));
		} catch (Exception e) {
			logger.info("error consultando articulos del ingreso " + e
					+ "\n\n\n" + consulta);
		}

		return ingresosMap;
	}

	/**
	 * Metodo para insertar (cantidad) por responsable de la cuenta
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @param cantidad
	 * @param articulo
	 * @return
	 */

	public static boolean ingresarResponsable(Connection con,
			String numeroSolicitud, int subcuenta, int cantidad, int articulo) {
		int u = 0;
		try {
			// Consultamos el codigo de la solicitud de justificacion
			String consultarCodJustArtSol = "SELECT jas.codigo AS codigo from justificacion_art_sol jas " 
					+ " INNER JOIN INVENTARIOS.SOL_X_JUST_ART_SOL SJ ON SJ.CODIGO_JUSTIFICACION = jas.codigo "
					+ " where jas.articulo="
					+ articulo + " and SJ.numero_solicitud=" + numeroSolicitud;

			int codJustArtSol = ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator ps;
			ps = new PreparedStatementDecorator(con.prepareStatement(
					consultarCodJustArtSol, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				codJustArtSol = rs.getInt("codigo");

			// Se ingresa el responsable
			String insertarResponsable = "INSERT INTO justificacion_art_resp "
					+ "( " + "estado," + "justificacion_art_sol, "
					+ "subcuenta, " + "cantidad " + ") " + " VALUES" + "(	"
					+ " '" + ConstantesIntegridadDominio.acronimoJustificado
					+ "'," + " " + codJustArtSol + "," + " " + subcuenta + ","
					+ " " + cantidad + "" + " ) " + "";
			ps = new PreparedStatementDecorator(con.prepareStatement(
					insertarResponsable, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			u = ps.executeUpdate();

		} catch (SQLException e) {
			logger.info("ERROR " + e);
		}
		if (u > 0)
			return true;
		else
			return false;
	}

	/**
	 * Metodo para revisar la existencia de juistificaciones y modificarlas
	 * dependiendo la Distribucion Retorna 0: Cuando ya existe una justificación
	 * pendiente para la solicitud y artículo dado Retorna 1: Cuando se inserta
	 * el registro de justificacion en la tabla de responsables
	 * (justificacion_art_resp) Retorna 2: Cuando se inserto una justificación
	 * pendiente y se necesita realizar la justificación
	 * 
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public static int revisarJustificacionDistribucion(Connection con,
			String numSol, String subCuenta, String cantidad, String articulo,
			String usuario) {
		HashMap mapaCD = new HashMap();
		HashMap mapaID = new HashMap();
		HashMap mapaCC = new HashMap();
		PreparedStatementDecorator ps, psb, psi, psc, psp, psc2;
		try {
			ps = new PreparedStatementDecorator(con.prepareStatement(
					justificacionDistri, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(subCuenta));
			ps.setInt(2, Utilidades.convertirAEntero(numSol));
			ps.setInt(3, Utilidades.convertirAEntero(articulo));
			logger.info("===>Consulta: " + justificacionDistri
					+ " ===>SubCuenta: " + subCuenta + " ===>NumSol: " + numSol
					+ " ===>Articulo: " + articulo);
			mapaCD = UtilidadBD.cargarValueObject(
					new ResultSetDecorator(ps.executeQuery()), true, true);
			mapaCD.put("INDICES", indicesMapDistri);

			if (Utilidades.convertirAEntero(mapaCD.get("numRegistros")
					.toString()) > 0) {
				for (int i = 0; i < Utilidades.convertirAEntero(mapaCD.get(
						"numRegistros").toString()); i++) {
					try {
						psb = new PreparedStatementDecorator(
								con.prepareStatement(borrarJustificacionDisti,
										ConstantesBD.typeResultSet,
										ConstantesBD.concurrencyResultSet));
						psb.setDouble(
								1,
								Utilidades.convertirADouble(mapaCD
										.get("codigo_" + i) + ""));
						psb.executeUpdate();
					} catch (SQLException e) {
						logger.info("ERROR. Eliminando Justificaciones para distribucion de la cuenta>>>>>>>>>"
								+ e);
					}
				}
				try {
					psc = new PreparedStatementDecorator(con.prepareStatement(
							consultaJustificacionAnterior,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
					psc.setInt(1, Utilidades.convertirAEntero(numSol));
					psc.setInt(2, Utilidades.convertirAEntero(articulo));
					mapaID = UtilidadBD.cargarValueObject(
							new ResultSetDecorator(psc.executeQuery()), true,
							true);
					mapaID.put("INDICES", indicesMapDistri);
				} catch (SQLException e) {
					logger.info("ERROR. Tomando el codigo de jus_ar_sol para distribucion de la cuenta>>>>>>>>>"
							+ e);
				}
				try {
					psi = new PreparedStatementDecorator(con.prepareStatement(
							insertarJustificacionDisti,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
					/**
					 * INSERT INTO justificacion_art_resp (estado,
					 * justificacion_art_sol, subcuenta, cantidad, codigo)
					 */
					psi.setString(1,
							ConstantesIntegridadDominio.acronimoJustificado);
					psi.setDouble(
							2,
							Utilidades.convertirADouble(mapaID.get("codigo_0")
									+ ""));
					psi.setInt(3, Utilidades.convertirAEntero(subCuenta));
					psi.setInt(4, Utilidades.convertirAEntero(cantidad));
					psi.setDouble(5, Utilidades.convertirADouble(UtilidadBD
							.obtenerSiguienteValorSecuencia(con,
									"seq_justificacion_art_resp")
							+ ""));
					if (psi.executeUpdate() > 0) {
						return 1;
					}
				} catch (SQLException e) {
					logger.info("ERROR. Insertando nueva justificacion para distribucion de la cuenta>>>>>>>>>"
							+ e);
				}
			} else {
				try {
					psc2 = new PreparedStatementDecorator(con.prepareStatement(
							consultaJusPStr, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
					psc2.setInt(1, Utilidades.convertirAEntero(numSol));
					psc2.setInt(2, Utilidades.convertirAEntero(articulo));
					mapaCC = UtilidadBD.cargarValueObject(
							new ResultSetDecorator(psc2.executeQuery()), true,
							true);
					if (Utilidades.convertirAEntero(mapaCC.get("numRegistros")
							.toString()) <= 0) {
						try {
							psp = new PreparedStatementDecorator(
									con.prepareStatement(insertarJusPStr,
											ConstantesBD.typeResultSet,
											ConstantesBD.concurrencyResultSet));

							/**
							 * INSERT INTO jus_pendiente_articulos VALUES
							 * (?,?,CURRENT_DATE,
							 * "+ValoresPorDefecto.getSentenciaHoraActualBD()+"
							 * ,?)
							 */

							psp.setInt(1, Utilidades.convertirAEntero(numSol));
							psp.setInt(2, Utilidades.convertirAEntero(articulo));
							psp.setString(3, usuario);

							int resultado = psp.executeUpdate();
							if (resultado > 0) {
								return 2;
							}
						} catch (SQLException e) {
							logger.info("ERROR. Insertando justificacion pendiente para distribucion de la cuenta>>>>>>>>>"
									+ e);
						}
					} else {
						logger.info("YA EXISTE UNA JUSTIFICACION PENDIENTE CON NUMERO SOL >>>>"
								+ numSol + ">>>>Y ARTICULO >>>>>>" + articulo);
					}
				} catch (SQLException e) {
					logger.info("ERROR. Consultando justificacion pendiente para distribucion de la cuenta>>>>>>>>>"
							+ e);
				}
			}
		} catch (SQLException e) {
			logger.info("ERROR. Consulta existencia de justificacion para distribucion de la cuenta>>>>>>>>>"
					+ e);
		}
		return 0;
	}

	/**
	 * Metodo para validar Convenio y Articulo NoPos
	 * 
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public static boolean validarArtConvJustificacion(Connection con,
			String articulo, String subCuenta) {
		HashMap<String, Object> resultadosNP = new HashMap<String, Object>();
		PreparedStatementDecorator ps, ps2;
		ResultSetDecorator rs;
		String natu = "";
		try {
			ps2 = new PreparedStatementDecorator(con.prepareStatement(
					consultaStrNaturalezaArt, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			ps2.setString(1, articulo);

			rs = new ResultSetDecorator(ps2.executeQuery());
			if (rs.next()) {
				natu = rs.getString("naturalezaa");
			}
		} catch (SQLException e) {
			logger.error(e
					+ " Error en la consulta de la Naturaleza del Articulo");
			return false;
		}
		if (natu.equals(ConstantesBD.acronimoNo)) {
			try {
				ps = new PreparedStatementDecorator(con.prepareStatement(
						consultaNoPosArtConv2, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(articulo));
				ps.setLong(2, Utilidades.convertirALong(subCuenta));

				resultadosNP = UtilidadBD.cargarValueObject(
						new ResultSetDecorator(ps.executeQuery()), true, true);

				resultadosNP.put("INDICES", indicesMapCons);

				if (Utilidades.convertirAEntero(resultadosNP
						.get("numRegistros").toString()) > 0
						&& !resultadosNP.get("pos_0").equals("12")) {
					return true;
				}
			} catch (SQLException e) {
				logger.error(e
						+ " Error en validacion de articulo NOPOS con requiere jus dif med");
			}
		} else {
			try {
				ps = new PreparedStatementDecorator(con.prepareStatement(
						consultaNoPosArtConv, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(articulo));
				ps.setLong(2, Utilidades.convertirALong(subCuenta));

				resultadosNP = UtilidadBD.cargarValueObject(
						new ResultSetDecorator(ps.executeQuery()), true, true);

				resultadosNP.put("INDICES", indicesMapCons);

				if (Utilidades.convertirAEntero(resultadosNP
						.get("numRegistros").toString()) > 0
						&& !resultadosNP.get("pos_0").equals("12")) {
					return true;
				}
			} catch (SQLException e) {
				logger.error(e
						+ " Error en validacion de articulo NOPOS sin requiere jus dif med");
			}
		}

		return false;
	}

	/**
	 * Método que evalua si el convenio dado tiene parametrizado la
	 * justificacion de artículos en 'S' ó en 'N'
	 * 
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public static String requiereJustificacionArticuloConvenio(Connection con,
			String codConvenio) {
		String requiereJustificacionArticulos = "", cadena = "";
		cadena = "SELECT requiere_justificacion_art AS justarticulos FROM convenios WHERE codigo = ? ";
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(cadena, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(codConvenio));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				return rs.getString("justarticulos");
		} catch (SQLException e) {
			logger.error(e
					+ " ERROR CONSULTANDO SI EL CONVENIO DADO REQUIERE JUSTIFICACIÓN DE ARTÍCULOS");
		}
		return requiereJustificacionArticulos;
	}

	/**
	 * Metodo para cuando se esta buscando por rangos y se selecciona tipo de
	 * reporte consolidado cual = 0 por centro costo cual = 1 por piso cual = 2
	 * por estado justificacion
	 * 
	 * @param cual
	 * @return
	 */
	public static String consultaCompletaConsolidado(int cual, String cadena) {
		String sqlFinal = "";
		String cadena1 = "", cadena2 = "bandera ";

		switch (cual) {
		case 0:
			cadena1 = "s.centro_costo_solicitante as codigo_centro_costo, getnomcentrocosto(s.centro_costo_solicitante) ";
			cadena2 = "s.centro_costo_solicitante ";
			break;

		case 1:
			cadena1 = "getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) ";
			//MT6527  se agrega cadena 2 para el GROUP BY
			cadena2 = "getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso)) ";
			// cadena2 = "centro_costo, "; //piso
			break;

		case 2:
			cadena1 = " getintegridaddominio(jar.estado) ";
			//MT6527  se agrega cadena 2 para el GROUP BY
			cadena2 =" getintegridaddominio(jar.estado) ";
			// cadena2 = "centro_costo, "; //estadojus
			break;
		}
		sqlFinal = cadena.replaceAll("12=12", cadena1);
		sqlFinal = sqlFinal.replaceAll("11=11", cadena2);

		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("Cual: " + cual);
		logger.info("<< Sql Final " + sqlFinal);

		return sqlFinal;
	}

	/**
	 * Método que inserta el registro del responsable de la justificación del
	 * articulo con su correspondiente cantidad
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public static boolean insertarResponsableJustificacion(Connection con,
			int numeroSolicitud, int articulo, String subCuenta,
			String cantidad, int codigoJustificacionSolicitud) {
		PreparedStatement pst = null;
		boolean resultado = false;
		try {
			logger.info("############## Inicio insertarResponsableJustificacion");
			String cadena = "INSERT INTO justificacion_art_resp (codigo, estado, justificacion_art_sol, subcuenta, cantidad) VALUES (?, ?, ?, ?, ?)";

			pst = con.prepareStatement(cadena, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet);
			pst.setDouble(1, Utilidades.convertirADouble(UtilidadBD
					.obtenerSiguienteValorSecuencia(con,
							"seq_justificacion_art_resp")
					+ ""));
			pst.setString(2, ConstantesIntegridadDominio.acronimoJustificado);
			pst.setDouble(3, codigoJustificacionSolicitud);
			pst.setInt(4, Utilidades.convertirAEntero(subCuenta));
			pst.setInt(5, Utilidades.convertirAEntero(cantidad));
			if (pst.executeUpdate() > 0) {
				resultado = true;
			}
		} catch (SQLException sqe) {
			logger.error(
					"############## SQLException ERROR insertarResponsableJustificacion",
					sqe);
		} catch (Exception e) {
			logger.error(
					"############## ERROR insertarResponsableJustificacion", e);
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException se) {
				logger.error(
						"###########  Error close ResulSet - PreparedStatement",
						se);
			}
		}
		logger.info("############## Fin insertarResponsableJustificacion");
		return resultado;
	}

	/**
	 * Método que inserta el registro del responsable de la justificación del
	 * servicio con su correspondiente cantidad
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public static boolean insertarResponsableJustificacionServicio(
			Connection con, int numeroSolicitud, int servicio,
			String subCuenta, String cantidad, int codigoJustificacionSolicitud) throws BDException {
		PreparedStatement pst = null;
		boolean resultado = false;
		try {
			Log4JManager.info("############## Inicio insertarResponsableJustificacionServicio");
			String cadena = "INSERT INTO justificacion_serv_resp (codigo, justificacion_serv_sol, sub_cuenta, estado, cantidad) VALUES (?, ?, ?, ?, ?)";

			pst = con.prepareStatement(cadena, ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet);
			pst.setLong(1, Utilidades.convertirALong(UtilidadBD
					.obtenerSiguienteValorSecuencia(con,
							"seq_justificacion_serv_resp")
					+ ""));
			pst.setLong(2, codigoJustificacionSolicitud);
			pst.setInt(3, Utilidades.convertirAEntero(subCuenta));
			pst.setString(4, ConstantesIntegridadDominio.acronimoJustificado);
			pst.setDouble(5, Utilidades.convertirADouble(cantidad));
			if (pst.executeUpdate() > 0) {
				resultado = true;
			}
		} 
		catch (SQLException sqe) {
			Log4JManager.error(sqe);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, sqe);
		} 
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		} 
		finally {
			try {
				if (pst != null) {
					pst.close();
				}
			} catch (SQLException se) {
				Log4JManager.error("###########  Error close ResulSet - PreparedStatement",	se);
			}
		}
		Log4JManager.info("############## Fin insertarResponsableJustificacionServicio");
		return resultado;
	}

	/**
	 * Metodo que consulta una justificación historica
	 * 
	 * @param con
	 * @param medicamentoNoPos2
	 * @param codigoSolicitud2
	 * @return
	 */
	public static HashMap<String, Object> consultarJustificacionHistorica(
			Connection con, String medicamentoNoPos, String codigoSolicitud) {
		HashMap justificacionHistorica = new HashMap();
		String consulta = "SELECT " + "das.descripcion as valor,"
				+ "das.numero_solicitud," + "asol.nombre as etiqueta, "
				+ "getdescarticulosincodigo(das.articulo) as articulo "
				+ "FROM " + "desc_atributos_solicitud das " + "INNER JOIN "
				+ "atributos_solicitud asol ON (asol.codigo = das.atributo) "
				+ "WHERE ";
		if (UtilidadTexto.isEmpty(codigoSolicitud)) {
			consulta = consulta + "das.articulo = " + medicamentoNoPos;
		} else {
			consulta = consulta + "das.numero_solicitud = " + codigoSolicitud
					+ " " + "AND " + "das.articulo = " + medicamentoNoPos;
		}

		logger.info("consultarJustificacionHistorica / " + consulta);

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					con.prepareStatement(consulta, ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			justificacionHistorica = UtilidadBD
					.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} catch (Exception e) {
			logger.info("ERROR! consultarJustificacionHistorica / " + e);
		}
		return justificacionHistorica;
	}

	public static List<DtoAccionCampo> consultarAccionesCampo(Connection con,
			int idCampo, String servicio) {
		String consultarAccionesCampo = "SELECT codigo," + "  campo_accion,"
				+ "  servicio_campo_accion," + "  campo_afectado,"
				+ "  servicio_campo_afectado," + "  accion,"
				+ "  visibilidad_inicial," + "  valor_validar "
				+ "FROM acciones_campos " + "WHERE" + " campo_accion= ?"
				+ " and servicio_campo_accion = ?";
		List<DtoAccionCampo> accionesCampo = new ArrayList<DtoAccionCampo>(0);
		try {
			PreparedStatement ps = con.prepareStatement(consultarAccionesCampo,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet);
			ps.setInt(1, idCampo);
			ps.setString(2, servicio);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DtoAccionCampo accionCampo = new DtoAccionCampo();
				accionCampo.setCodigo(rs.getLong("codigo"));
				accionCampo.setCampoAccion(rs.getLong("campo_accion"));
				accionCampo.setServicioAccion(rs
						.getString("servicio_campo_accion"));
				accionCampo.setCampoAfectado(rs.getLong("campo_afectado"));
				accionCampo.setServicioAfectado(rs
						.getString("servicio_campo_afectado"));
				accionCampo.setAccion(rs.getInt("accion"));
				accionCampo.setVisibilidadInicial(rs
						.getInt("visibilidad_inicial"));
				accionCampo.setValorValidar(rs.getString("valor_validar"));

				accionesCampo.add(accionCampo);
			}
		} catch (Exception e) {
			logger.info("ERROR! consultarAccionesCampo / " + e);
		}
		return accionesCampo;
	}

}