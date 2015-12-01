/*
 * @(#)SqlBaseAntecedentesGinecoObstetricosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas) las BD
 * utilizadas en axioma, en general se trata de las consultas que utilizan SQL
 * estándar. Métodos particulares a Antecedentes Gineco Obstetricos
 * 
 * @version 1.0, Mar 31, 2004
 */
@SuppressWarnings({"rawtypes","deprecation"})
public class SqlBaseAntecedentesGinecoObstetricosDao {

	/**
	 * Log para manejar los problemas de esta clase
	 */
	private static Logger logger = Logger
			.getLogger(SqlBaseAntecedentesGinecoObstetricosDao.class);

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un
	 * Antecedente GinecoObstetrico en una base de datos Genérica.
	 */
	private static final String insertarAntecedenteGinecoObstetricoStr = "INSERT INTO ant_gineco_obste (codigo_paciente, edad_menarquia, otra_edad_menarquia, edad_menopausia, otra_edad_menopausia, observaciones, edad_inic_vida_sexual, edad_inic_vida_obstetrica, fecha, hora, usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar en la
	 * tabla guia (antecedentes_paciente) una entrada, en una base de datos
	 * Genérica.
	 */
	private static final String insertarAntecedentePacienteStr = "INSERT INTO antecedentes_pacientes (codigo_paciente) VALUES(?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar un
	 * Antecedente GinecoObstetrico en una BD Genérica.
	 */
	private static final String modificarAntecedenteGinecoObstetricoStr = "UPDATE ant_gineco_obste SET edad_menarquia = ?, otra_edad_menarquia = ?, edad_menopausia = ?, otra_edad_menopausia = ?, observaciones = ?, edad_inic_vida_sexual = ?, edad_inic_vida_obstetrica = ?, fecha = CURRENT_DATE, hora = "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", usuario = ? WHERE codigo_paciente = ? ";

	/***************************************************************************
	 * / Cadena constante con el <i>statement</i> necesario para modificar un
	 * las observaciones sobre un método anticonceptivo en particular en una BD
	 * Genérica.
	 */
	// private static final String modificarMetodosAnticonceptivosStr = "UPDATE
	// met_anticon_ant_go SET descripcion = ? WHERE codigo_paciente = ? AND
	// anticonceptivo = ?";
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar un
	 * Antecedente GinecoObstetrico en una BD Genérica.
	 */
	
	private static final String cargarAntecedenteGinecoObstetricoStr = "SELECT " +
			"antgin.otra_edad_menarquia AS otraEdadMenarquia, " +
			"edadmenar.codigo AS codigoEdadMenarquia," +
			"edadmenar.nombre AS edadMenarquia, " +
			"antgin.otra_edad_menopausia AS otraEdadMenopausia, " +
			"edadmenop.codigo AS codigoEdadMenopausia, " +
			"edadmenop.nombre AS edadMenopausia, " +
			"antgin.observaciones, " +
			"antgin.edad_inic_vida_sexual AS inicioVidaSexual, " +
			"edad_inic_vida_obstetrica AS inicioVidaObstetrica, " +
			"antgin.fecha, " +
			"antgin.hora, " +
			"antgin.usuario " +
			"FROM " +
			"ant_gineco_obste antgin, rangos_edad_menarquia edadmenar, rangos_edad_menopausia edadmenop WHERE antgin.edad_menarquia = edadmenar.codigo AND antgin.edad_menopausia = edadmenop.codigo AND codigo_paciente = ? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los datos
	 * historicos de Antecedente GinecoObstetrico en una BD Genérica.
	 */

	private static final String cargarHistoricoStr = ""
			+ "	SELECT hist.duracion_menstruacion AS duracionMenstruacion, hist.dolor_menstruacion AS dolorMenstruacion, "
			+ "		   to_char(hist.fecha_ultima_regla, 'YYYY-MM-DD') AS fechaUltimaRegla, hist.concepto_menstruacion AS codigoConceptoMenstruacion, "
			+ "		   conmens.nombre AS conceptoMenstruacion, hist.fecha_ultima_mamografia AS fechaUltimaMamografia, "
			+ "		   hist.descripcion_ultima_mamografia AS descUltimaMamografia, hist.fecha_ultima_ecografia AS fechaUltimaEcografia, "
			+ "		   hist.descripcion_ultima_ecografia AS descUltimaEcografia, hist.fecha_ultima_citologia AS fechaUltimaCitologia,"
			+ "		   hist.descripcion_ultima_citologia AS descUltimaCitologia, hist.fecha_ultima_densimetria_osea AS fechaUltDensimetriaOsea, "
			+ "		   hist.desc_ultima_densimetria_osea AS descUltDensimentriaOsea, hist.descripcion_ultimo_proc_gine AS descUltimoProcGin,"
			+ "		   hist.observaciones_menstruacion AS obsMenstruacion, hist.ciclo_menstrual AS cicloMenstrual,"
			+ "		   hist.g_informacion_embarazos AS gInfoEmbarazos, hist.p_informacion_embarazos AS pInfoEmbarazos, "
			+ "		   hist.p2500 as p2500, hist.p2500, hist.p4000 as p4000,hist.a_informacion_embarazos AS aInfoEmbarazos, "
			+ "		   hist.mayora2 AS mayora2, hist.c_informacion_embarazos AS cInfoEmbarazos, "
			+ "		   hist.v_informacion_embarazos AS vInfoEmbarazos, hist.m_informacion_embarazos AS mInfoEmbarazos, "
			+ "		   hist.finembarazoanterior AS finembarazoanterior, hist.finembarazomayor1o5 AS finembarazomayor1o5, "
			+ "		   hist.prematuros AS prematuros, hist.ectropicos AS ectropicos, hist.multiples AS multiples, "
			+ "		   hist.fecha_creacion AS fechaCreacion, hist.hora_creacion AS horaCreacion,"
			+ "		   hist.vag,    "
			+ "		   hist.retencion_placentaria, hist.infeccion_postparto, hist.malformacion,  hist.muerte_perinatal, 	"
			+ "		   case when hist.sangrado_anormal is null then '' else hist.sangrado_anormal end as sangrado_anormal , case when hist.flujo_vaginal is null then '' else hist.flujo_vaginal end as flujo_vaginal ,  case when hist.enfer_trans_sexual is null then '' else hist.enfer_trans_sexual end as enfer_trans_sexual ,  case when hist.cual_enfer_trans_sex is null then '' else hist.cual_enfer_trans_sex end as cual_enfer_trans_sex, "
			+ "		   case when hist.cirugia_gineco is null then '' else hist.cirugia_gineco end as cirugia_gineco , case when hist.cual_cirugia_gineco is null then '' else hist.cual_cirugia_gineco end as cual_cirugia_gineco ,  case when hist.historia_infertilidad is null then '' else hist.historia_infertilidad end as historia_infertilidad ,  case when hist.cual_histo_infertilidad is null then '' else hist.cual_histo_infertilidad end as cual_histo_infertilidad," 
			+ "		   hist.tipo_embarazo as tipoembarazo, muertos_antes_1semana as muertosantes1semana, muertos_despues_1semana as muertosdespues1semana,vivos_actualmente as vivosactualmente " 
			+ "		   FROM ant_gineco_histo hist "
			+ "				INNER JOIN conceptos_menstruacion conmens ON (hist.concepto_menstruacion=conmens.codigo) "
			+ "					 WHERE hist.codigo_paciente=? "
			+ "						   ORDER BY hist.codigo";

	/**
	 * Sentencia para cargar los historicos de una valoracion específica
	 */
	private static final String cargarHistoricoValoracionStr = ""
			+ "	SELECT hist.duracion_menstruacion AS duracionMenstruacion, hist.dolor_menstruacion AS dolorMenstruacion,					"
			+ "		   to_char(hist.fecha_ultima_regla, 'YYYY-MM-DD') AS fechaUltimaRegla, hist.concepto_menstruacion AS codigoConceptoMenstruacion, 				"
			+ "		   conmens.nombre AS conceptoMenstruacion, hist.fecha_ultima_mamografia AS fechaUltimaMamografia,						"
			+ "		   hist.descripcion_ultima_mamografia AS descUltimaMamografia, hist.fecha_ultima_ecografia AS fechaUltimaEcografia,		"
			+ "		   hist.descripcion_ultima_ecografia AS descUltimaEcografia, hist.fecha_ultima_citologia AS fechaUltimaCitologia, "
			+ "		   hist.descripcion_ultima_citologia AS descUltimaCitologia, hist.fecha_ultima_densimetria_osea AS fechaUltDensimetriaOsea, "
			+ "		   hist.desc_ultima_densimetria_osea AS descUltDensimentriaOsea, hist.descripcion_ultimo_proc_gine AS descUltimoProcGin,"
			+ "		   hist.observaciones_menstruacion AS obsMenstruacion, hist.ciclo_menstrual AS cicloMenstrual, "
			+ "		   hist.g_informacion_embarazos AS gInfoEmbarazos, hist.p_informacion_embarazos AS pInfoEmbarazos, "
			+ "		   hist.p2500 as p2500, hist.p2500, hist.p4000 as p4000,hist.a_informacion_embarazos AS aInfoEmbarazos,"
			+ "		   hist.mayora2 AS mayora2, hist.c_informacion_embarazos AS cInfoEmbarazos,  "
			+ "		   hist.v_informacion_embarazos AS vInfoEmbarazos, hist.m_informacion_embarazos AS mInfoEmbarazos, "
			+ "		   hist.finembarazoanterior AS finembarazoanterior, hist.finembarazomayor1o5 AS finembarazomayor1o5, "
			+ "		   hist.prematuros AS prematuros, hist.ectropicos AS ectropicos, hist.multiples AS multiples,"
			+ "		   hist.fecha_creacion AS fechaCreacion, hist.hora_creacion AS horaCreacion, "
			+ "		   hist.vag,    "
			+ "		   hist.retencion_placentaria, hist.infeccion_postparto, hist.malformacion,  hist.muerte_perinatal, 	"
			+ "		   case when hist.sangrado_anormal is null then '' else hist.sangrado_anormal end as sangrado_anormal, case when hist.flujo_vaginal is null then '' else hist.flujo_vaginal end as flujo_vaginal, case when hist.enfer_trans_sexual is null then '' else hist.enfer_trans_sexual end as enfer_trans_sexual , case when hist.cual_enfer_trans_sex is null then '' else hist.cual_enfer_trans_sex end as cual_enfer_trans_sex , "
			+ "		   case when hist.cirugia_gineco is null then '' else hist.cirugia_gineco end as cirugia_gineco , case when hist.cual_cirugia_gineco is null then '' else hist.cual_cirugia_gineco end as cual_cirugia_gineco ,  case when hist.historia_infertilidad is null then '' else hist.historia_infertilidad end as historia_infertilidad ,  case when hist.cual_histo_infertilidad is null then '' else hist.cual_histo_infertilidad end as cual_histo_infertilidad, "
			+ "		   hist.tipo_embarazo as tipoembarazo, muertos_antes_1semana as muertosantes1semana, muertos_despues_1semana as muertosdespues1semana,vivos_actualmente as vivosactualmente " 
			+ "		   FROM ant_gineco_histo hist "
			+ "				INNER JOIN conceptos_menstruacion conmens ON(hist.concepto_menstruacion=conmens.codigo) "
			+ "				INNER JOIN val_historia_menstrual vhm ON(vhm.codigo_paciente=hist.codigo_paciente AND hist.codigo=vhm.historico_ant) "
			+ "					 WHERE hist.concepto_menstruacion=conmens.codigo "
			+ "					   AND hist.codigo_paciente=? "
			+ "					   AND vhm.valoracion=?		";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los
	 * embarazos propios de este Antecedente GinecoObstetrico en una BD
	 * Genérica.
	 */
	private static final String cargarEmbarazosStr = "SELECT emb.codigo, emb.meses_gestacion AS mesesGestacion, emb.fecha_terminacion AS fechaTerminacion, tiptpar.nombre AS trabajoParto, emb.trabajo_parto AS codigoTrabajoParto, emb.otro_trabajo_parto AS otroTrabajoParto, emb.duracion_trabajo_parto AS duracion, emb.tiempo_ruptura_membranas AS ruptura, emb.legrado AS legrado FROM ant_gineco_embarazo emb, tipos_trabajo_parto tiptpar WHERE emb.trabajo_parto = tiptpar.codigo AND emb.codigo_paciente = ? AND emb.fin_embarazo="
			+ ValoresPorDefecto.getValorTrueParaConsultas()
			+ " ORDER BY emb.codigo";

	/**
	 * Sentencia para cargar las complicaciones del embrazo
	 */
	private static final String cargarComplicacionesEmbarazoStr = "SELECT cg.codigo AS codigo, cg.complicacion AS codigoComplicacion, comp.nombre AS nombreComplicacion from complicaciones_gineco cg INNER JOIN complicaciones_embarazo comp ON(comp.codigo=cg.complicacion) WHERE cg.antecedente=? AND cg.paciente=?";

	/**
	 * Sentencia para cargar las complicaciones OTRAS del embrazo
	 */
	private static final String cargarOtrasComplicacionesEmbarazoStr = "SELECT codigo AS codigo, otra_complicacion AS complicacion FROM compli_gineco_otra WHERE antecedente=? AND paciente=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los
	 * métodos anticonceptivos en una BD Genérica.
	 */
	private static final String cargarMetodosAnticonceptivosStr = "SELECT antmet.anticonceptivo AS codigoMetodoAnticon, met.nombre AS metodoAnticonceptivo, antmet.descripcion FROM met_anticon_ant_go antmet, metodos_anticonceptivos met WHERE antmet.anticonceptivo=met.codigo AND antmet.codigo_paciente=? AND to_timestamp(fecha||' '||hora,'YYYY MM DD HH24:MI') = (SELECT  max(to_timestamp(fecha||' '||hora,'YYYY MM DD HH24:MI')) FROM met_anticon_ant_go)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para mirar si existe
	 * un Antecedente GinecoObstetrico en particular en una BD Genérica.
	 */
	private static final String existeAntecedenteStr = "SELECT count(1) AS numResultados FROM ant_gineco_obste WHERE codigo_paciente = ? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para mirar si existe
	 * una entrada en la tabla antecedentes_pacientes para esta paciente en
	 * particular en una BD Genérica.
	 */
	private static final String existeAntecedentePacienteStr = "SELECT count(1)  as numResultados FROM antecedentes_pacientes WHERE codigo_paciente = ? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para mirar si existe
	 * un Antecedente GinecoObstetrico en particular en una BD Genérica.
	 */
	private static final String cargarUltInfoEmbarazosStr = ""
			+ " SELECT g_informacion_embarazos AS gInfoEmbarazos, p_informacion_embarazos AS pInfoEmbarazos, 						 "
			+ "		 p2500 AS p2500, p4000 AS p4000, a_informacion_embarazos AS aInfoEmbarazos, mayora2 AS mayora2, 			 "
			+ "		 c_informacion_embarazos AS cInfoEmbarazos,  v_informacion_embarazos AS vInfoEmbarazos, 	  	     "
			+ "		 m_informacion_embarazos AS mInfoEmbarazos, finembarazoanterior AS finembarazoanterior, 		 "
			+ "		 finembarazomayor1o5 AS finembarazomayor1o5, prematuros AS prematuros, ectropicos AS ectropicos, 			 "
			+ "		 multiples AS multiples, vag as vag, 			 "
			+ "		 retencion_placentaria as retencion_placentaria, infeccion_postparto as infeccion_postparto, 				 "
			+ "		 malformacion as malformacion,  muerte_perinatal as muerte_perinatal, "
			+ "		 tipo_embarazo as tipoembarazo, muertos_antes_1semana as muertosantes1semana, muertos_despues_1semana as muertosdespues1semana,vivos_actualmente as vivosactualmente " 
			+ "		 FROM ant_gineco_histo 																						 "
			+ "			  WHERE codigo_paciente = ? 																			 "
			+ "				    ORDER BY codigo DESC ";

	/**
	 * Sentencia para ingresar los datos de la historia menstrual
	 */
	private static final String insertarDatosHistMenstrualStr = "" +
			"INSERT INTO " +
			"ant_gineco_histo(" +
			"codigo_paciente," +
			"codigo," +
			"duracion_menstruacion," +
			"dolor_menstruacion," +
			"fecha_ultima_regla," +
			"concepto_menstruacion," +
			"observaciones_menstruacion," +
			"ciclo_menstrual," +
			"fecha_creacion," +
			"hora_creacion) " +
			"VALUES(?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") ";

	/**
	 * Sentencia para actualizar los antecedentes desde la valoracion
	 */
	private static final String actualizarAntecedentesDesdeValoracion = "UPDATE ant_gineco_obste SET edad_menarquia=?, edad_menopausia=?, otra_edad_menarquia=?, otra_edad_menopausia=? WHERE codigo_paciente=?";

	/**
	 * Implementación de la inserción de un Antecedente GinecoObstetrico para
	 * una BD Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosDao#insertar(Connection,
	 *      String, String, int, String, int, String, String, String)
	 */
	public static int insertar(
			Connection con,
			int codigoPaciente,
			int codigoEdadMenarquia, 
			String otraEdadMenarquia,
			int codigoEdadMenopausia, 
			String otraEdadMenopausia,
			String observaciones, 
			int inicVidaSexual, 
			int inicVidaObstetrica,
			String loginUsuario,
			ArrayList metodosAnticonceptivos,
			String secuenciaMetodosAnticonceptivos) throws SQLException 
		{
			
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp0 = 0, resp1 = 0, resp2 = 0, i;

		// Debemos empezar una transacción
		con.setAutoCommit(false);
		myFactory.beginTransaction(con);
		resp0 = 1;

		// Si este paciente NO tiene antecedentes previos (de cualquier tipo)
		// debemos insertar una entrada en la tabla guia antecedentes_paciente

		if (!existeAntecedentePaciente(con, codigoPaciente)) {
			PreparedStatementDecorator insertarAntecedentePacienteStatement = new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAntecedentePacienteStatement.setInt(1, codigoPaciente);
			resp1 = insertarAntecedentePacienteStatement.executeUpdate();
			insertarAntecedentePacienteStatement.close();
		} else {
			// Si no hubo necesidad de entrar, debemos poner que todo salio bien
			resp1 = 1;
		}

		PreparedStatementDecorator insertarAntecedenteGinecoObstetricoStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedenteGinecoObstetricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarAntecedenteGinecoObstetricoStatement.setInt(1, codigoPaciente);
		insertarAntecedenteGinecoObstetricoStatement.setInt(2,codigoEdadMenarquia);
		insertarAntecedenteGinecoObstetricoStatement.setString(3,otraEdadMenarquia);
		insertarAntecedenteGinecoObstetricoStatement.setInt(4,codigoEdadMenopausia);
		insertarAntecedenteGinecoObstetricoStatement.setString(5,otraEdadMenopausia);
		insertarAntecedenteGinecoObstetricoStatement.setString(6, observaciones);
		if (inicVidaSexual == 0) {
			insertarAntecedenteGinecoObstetricoStatement.setString(7, null);
		} else {
			insertarAntecedenteGinecoObstetricoStatement.setInt(7,
					inicVidaSexual);
		}
		if (inicVidaObstetrica == 0) {
			insertarAntecedenteGinecoObstetricoStatement.setString(8, null);
		} else {
			insertarAntecedenteGinecoObstetricoStatement.setInt(8,
					inicVidaObstetrica);
		}

		insertarAntecedenteGinecoObstetricoStatement.setString(9, loginUsuario);
		resp2 = insertarAntecedenteGinecoObstetricoStatement.executeUpdate();
		insertarAntecedenteGinecoObstetricoStatement.close();
		if (resp0 < 1 || resp1 < 1 || resp2 < 1) {
			// Terminamos la transaccion, sea con un rollback o un commit.
			myFactory.abortTransaction(con);
			return 0;
		}

		if (metodosAnticonceptivos != null) {
			InfoDatos metodoAnticonceptivo;
			for (i = 0; i < metodosAnticonceptivos.size(); i++) {
				metodoAnticonceptivo = (InfoDatos) metodosAnticonceptivos
						.get(i);

				// Ahora reviso si el método anticonceptivo es diferente de -1
				// caso en que no debo insertarlo

				if (metodoAnticonceptivo.getCodigo() != -1) {
					String insertarMetodoAnticonceptivoStr = "INSERT INTO met_anticon_ant_go (codigo, codigo_paciente, anticonceptivo, descripcion, fecha, hora, usuario) VALUES ("
							+ secuenciaMetodosAnticonceptivos
							+ ", ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";
					PreparedStatementDecorator insertarMetodoAnticonceptivoStatement = new PreparedStatementDecorator(con.prepareStatement(insertarMetodoAnticonceptivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					insertarMetodoAnticonceptivoStatement.setInt(1,
							codigoPaciente);
					insertarMetodoAnticonceptivoStatement.setInt(2,
							metodoAnticonceptivo.getCodigo());
					insertarMetodoAnticonceptivoStatement.setString(3,
							metodoAnticonceptivo.getDescripcion());
					insertarMetodoAnticonceptivoStatement.setString(4,
							loginUsuario);
					int resp=insertarMetodoAnticonceptivoStatement.executeUpdate();
					insertarMetodoAnticonceptivoStatement.close();
					if (resp < 1) {
						// Terminamos la transaccion, sea con un rollback o un
						// commit.
						myFactory.abortTransaction(con);
						return 0;
					}
				}

			}

		}
		myFactory.endTransaction(con);
		con.setAutoCommit(true);

		return resp1;
	}

	/**
	 * Implementación de la inserción de un Antecedente GinecoObstetrico para
	 * una BD Genérica (Definiendo Transaccionalidad)
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosDao#insertarTransaccional(Connection,
	 *      String, String, int, String, int, String, String, String, String)
	 */
	public static int insertarTransaccional(Connection con, int codigoPaciente,
			int codigoEdadMenarquia, String otraEdadMenarquia,
			int codigoEdadMenopausia, String otraEdadMenopausia,
			String observaciones, int inicVidaSexual, int inicVidaObstetrica,
			String loginUsuario, ArrayList metodosAnticonceptivos,
			String secuenciaMetodosAnticonceptivos, String estado)
			throws SQLException {
		int resp0 = 0, resp1 = 0, resp2 = 0, i;

		try {
			DaoFactory myFactory = DaoFactory.getDaoFactory(System
					.getProperty("TIPOBD"));

			if (estado == null) {
				throw new SQLException(
						"Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar")) {
				con.setAutoCommit(false);
				myFactory.beginTransaction(con);
				resp0 = 1;
			} else {
				// De todas maneras así no sea transacción debo dejar en claro
				// que todo salio bien
				resp0 = 1;
			}

			if (!existeAntecedentePaciente(con, codigoPaciente)) {
				PreparedStatementDecorator insertarAntecedentePacienteStatement = new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				insertarAntecedentePacienteStatement.setInt(1, codigoPaciente);
				resp1 = insertarAntecedentePacienteStatement.executeUpdate();
				insertarAntecedentePacienteStatement.close();
			} else {
				// Si no hubo necesidad de entrar, debemos poner que todo salio
				// bien
				resp1 = 1;
			}

			PreparedStatementDecorator insertarAntecedenteGinecoObstetricoStatement = new PreparedStatementDecorator(con.prepareStatement(insertarAntecedenteGinecoObstetricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAntecedenteGinecoObstetricoStatement.setInt(1,
					codigoPaciente);
			insertarAntecedenteGinecoObstetricoStatement.setInt(2,
					codigoEdadMenarquia);
			insertarAntecedenteGinecoObstetricoStatement.setString(3,
					otraEdadMenarquia);
			insertarAntecedenteGinecoObstetricoStatement.setInt(4,
					codigoEdadMenopausia);
			insertarAntecedenteGinecoObstetricoStatement.setString(5,
					otraEdadMenopausia);
			insertarAntecedenteGinecoObstetricoStatement.setString(6,
					observaciones);
			insertarAntecedenteGinecoObstetricoStatement.setInt(7,
					inicVidaSexual);
			insertarAntecedenteGinecoObstetricoStatement.setInt(8,
					inicVidaObstetrica);
			insertarAntecedenteGinecoObstetricoStatement.setString(9,
					loginUsuario);
			resp2 = insertarAntecedenteGinecoObstetricoStatement
					.executeUpdate();
			insertarAntecedenteGinecoObstetricoStatement.close();
			if (metodosAnticonceptivos != null) {
				InfoDatos metodoAnticonceptivo;
				for (i = 0; i < metodosAnticonceptivos.size(); i++) {
					metodoAnticonceptivo = (InfoDatos) metodosAnticonceptivos
							.get(i);

					if (metodoAnticonceptivo.getCodigo() != -1) {
						String insertarMetodoAnticonceptivoStr = "INSERT INTO met_anticon_ant_go (codigo, codigo_paciente, anticonceptivo, descripcion, fecha, hora, usuario) VALUES ("
								+ secuenciaMetodosAnticonceptivos
								+ ", ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?)";
						PreparedStatementDecorator insertarMetodoAnticonceptivoStatement = new PreparedStatementDecorator(con.prepareStatement(insertarMetodoAnticonceptivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						insertarMetodoAnticonceptivoStatement.setInt(1,
								codigoPaciente);
						insertarMetodoAnticonceptivoStatement.setInt(2,
								metodoAnticonceptivo.getCodigo());
						insertarMetodoAnticonceptivoStatement.setString(3,
								metodoAnticonceptivo.getDescripcion());
						insertarMetodoAnticonceptivoStatement.setString(4,
								loginUsuario);
						int resp=insertarMetodoAnticonceptivoStatement.executeUpdate();
						insertarMetodoAnticonceptivoStatement.close();
						if (resp < 1) {
							// Terminamos la transaccion, sea con un rollback o
							// un commit.
							myFactory.abortTransaction(con);
							return 0;
						}
					}
				}

			}

			if (resp0 < 1 || resp1 < 1 || resp2 < 1) {
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			} else {
				if (estado.equals("finalizar")) {
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}
			}
			return resp1;
		} catch (SQLException e) {
			// Por alguna razón no salió bien
			logger.warn(e);

			throw e;
		}

	}

	/**
	 * Implementación de la búsqueda de antecendentes ginecoobstetricos para una
	 * base de datos Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosDao#existeAntecedente(Connection,
	 *      String, String)
	 */
	public static boolean existeAntecedente(Connection con, int codigoPaciente)
			throws SQLException {
		boolean result=false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			pst = con.prepareStatement(existeAntecedenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
	
			rs = pst.executeQuery();
	
			if (rs.next()) {
				if (rs.getInt("numResultados") < 1) {
					result=false;
				} else {
					result=true;
				}
			} else {
				// Si esta consulta no da un resultado (es un count) es porque algo
				// muy, pero
				// muy malo esta pasando
				throw new SQLException(
						"Error Desconocido en la busqueda de un Antecedente ginecoobstetrico");
			}
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarPatologias", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	

	/**
	 * Implementación de la modificación transaccional de antecedente
	 * ginecoobstetrico para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#modificarTransaccional
	 *      (Connection , int , int , String , int , String , String , int , int ,
	 *      String , ArrayList , String, String, String, String ) throws
	 *      SQLException
	 */
	public static int modificarTransaccional(Connection con,
			int codigoPaciente, int codigoEdadMenarquia,
			String otraEdadMenarquia, int codigoEdadMenopausia,
			String otraEdadMenopausia, String observaciones,
			int inicVidaSexual, int inicVidaObstetrica, String loginUsuario,
			ArrayList metodosAnticonceptivos,
			String secuenciaMetodosAnticonceptivos, String estado)
			throws SQLException {
		int resp0 = 0, resp1 = 0, i;

		try {
			DaoFactory myFactory = DaoFactory.getDaoFactory(System
					.getProperty("TIPOBD"));

			if (estado == null) {
				throw new SQLException(
						"Error SQL: No se seleccionó ningun estado para la transacción");
			}

			if (estado.equals("empezar")) {
				con.setAutoCommit(false);
				myFactory.beginTransaction(con);
				resp0 = 1;
			} else {
				// De todas maneras así no sea transacción debo dejar en claro
				// que todo salio bien
				resp0 = 1;
			}

			PreparedStatementDecorator modificarAntecedenteGinecoObstetricoStatement = new PreparedStatementDecorator(con.prepareStatement(modificarAntecedenteGinecoObstetricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificarAntecedenteGinecoObstetricoStatement.setInt(1,
					codigoEdadMenarquia);
			modificarAntecedenteGinecoObstetricoStatement.setString(2,
					otraEdadMenarquia);
			modificarAntecedenteGinecoObstetricoStatement.setInt(3,
					codigoEdadMenopausia);
			modificarAntecedenteGinecoObstetricoStatement.setString(4,
					otraEdadMenopausia);
			modificarAntecedenteGinecoObstetricoStatement.setString(5,
					observaciones);
			modificarAntecedenteGinecoObstetricoStatement.setInt(6,
					inicVidaSexual);
			modificarAntecedenteGinecoObstetricoStatement.setInt(7,
					inicVidaObstetrica);
			modificarAntecedenteGinecoObstetricoStatement.setString(8,
					loginUsuario);
			modificarAntecedenteGinecoObstetricoStatement.setInt(9,
					codigoPaciente);
			resp1 = modificarAntecedenteGinecoObstetricoStatement
					.executeUpdate();
			modificarAntecedenteGinecoObstetricoStatement.close();
			if (metodosAnticonceptivos != null) {
				InfoDatos metodoAnticonceptivo;
				for (i = 0; i < metodosAnticonceptivos.size(); i++) {
					metodoAnticonceptivo = (InfoDatos) metodosAnticonceptivos
							.get(i);

					// Si el código de este método anticonceptivo es -1 NO debo
					// insertarlo
					if (metodoAnticonceptivo.getCodigo() != -1) {

						String insertarMetodoAnticonceptivoStr = "INSERT INTO met_anticon_ant_go (codigo, codigo_paciente, anticonceptivo, descripcion, fecha, hora, usuario) VALUES ("
								+ secuenciaMetodosAnticonceptivos
								+ ", ?, ?, ?, CURRENT_DATE, " + "to_char(current_timestamp, 'HH24:MI')" + ", ?)";
						PreparedStatementDecorator insertarMetodoAnticonceptivoStatement = new PreparedStatementDecorator(con.prepareStatement(insertarMetodoAnticonceptivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						insertarMetodoAnticonceptivoStatement.setInt(1,
								codigoPaciente);
						insertarMetodoAnticonceptivoStatement.setInt(2,
								metodoAnticonceptivo.getCodigo());
						insertarMetodoAnticonceptivoStatement.setString(3,
								metodoAnticonceptivo.getDescripcion());
						insertarMetodoAnticonceptivoStatement.setString(4,
								loginUsuario);
						int resp =insertarMetodoAnticonceptivoStatement.executeUpdate();
						insertarMetodoAnticonceptivoStatement.close();
						if (resp < 1) {
							// Terminamos la transaccion, sea con un rollback o
							// un commit.
							myFactory.abortTransaction(con);
							return 0;
						}
						// }
					}

				}

			}

			if (resp0 < 1 || resp1 < 1) {
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			} else {
				if (estado.equals("finalizar")) {
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}
			}
			return resp1;
		} catch (SQLException e) {
			// Por alguna razón no salió bien
			logger.warn(e);
			throw e;
		}
	}

	/**
	 * Implementación de la modificación transaccional de antecedente
	 * ginecoobstetrico para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#modificar
	 *      (Connection , int , int , String , int , String , String , int , int ,
	 *      String , ArrayList ) throws SQLException
	 */
	public static int modificar(Connection con, int codigoPaciente,
			int codigoEdadMenarquia, String otraEdadMenarquia,
			int codigoEdadMenopausia, String otraEdadMenopausia,
			String observaciones, int inicVidaSexual, int inicVidaObstetrica,
			String loginUsuario, ArrayList metodosAnticonceptivos,
			String secuenciaMetodosAnticonceptivos) throws SQLException {
		DaoFactory myFactory = DaoFactory.getDaoFactory(System
				.getProperty("TIPOBD"));
		int resp0 = 0, resp1 = 0, i;

		// Debemos empezar una transacción
		con.setAutoCommit(false);
		myFactory.beginTransaction(con);
		resp0 = 1;

		PreparedStatementDecorator modificarAntecedenteGinecoObstetricoStatement = new PreparedStatementDecorator(con.prepareStatement(modificarAntecedenteGinecoObstetricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		modificarAntecedenteGinecoObstetricoStatement.setInt(1,
				codigoEdadMenarquia);
		modificarAntecedenteGinecoObstetricoStatement.setString(2,
				otraEdadMenarquia);
		modificarAntecedenteGinecoObstetricoStatement.setInt(3,
				codigoEdadMenopausia);
		modificarAntecedenteGinecoObstetricoStatement.setString(4,
				otraEdadMenopausia);
		modificarAntecedenteGinecoObstetricoStatement.setString(5,
				observaciones);
		modificarAntecedenteGinecoObstetricoStatement.setInt(6, inicVidaSexual);
		modificarAntecedenteGinecoObstetricoStatement.setInt(7,
				inicVidaObstetrica);
		modificarAntecedenteGinecoObstetricoStatement
				.setString(8, loginUsuario);
		modificarAntecedenteGinecoObstetricoStatement.setInt(9, codigoPaciente);

		resp1 = modificarAntecedenteGinecoObstetricoStatement.executeUpdate();
		modificarAntecedenteGinecoObstetricoStatement.close();
		if (resp0 < 1 || resp1 < 1) {
			// Terminamos la transaccion, sea con un rollback o un commit.
			myFactory.abortTransaction(con);
			return 0;
		}

		if (metodosAnticonceptivos != null) {
			InfoDatos metodoAnticonceptivo;
			for (i = 0; i < metodosAnticonceptivos.size(); i++) {
				metodoAnticonceptivo = (InfoDatos) metodosAnticonceptivos
						.get(i);
				// Si el código es -1 NO debo insertarlo
				if (metodoAnticonceptivo.getCodigo() != -1) {

					String insertarMetodoAnticonceptivoStr = "INSERT INTO met_anticon_ant_go (codigo, codigo_paciente, anticonceptivo, descripcion, fecha, hora, usuario) VALUES ("
							+ secuenciaMetodosAnticonceptivos
							+ ", ?, ?, ?, CURRENT_DATE, "+ "to_char(current_timestamp, 'HH24:MI')" +", ?)";
					PreparedStatementDecorator insertarMetodoAnticonceptivoStatement = new PreparedStatementDecorator(con.prepareStatement(insertarMetodoAnticonceptivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					insertarMetodoAnticonceptivoStatement.setInt(1,
							codigoPaciente);
					insertarMetodoAnticonceptivoStatement.setInt(2,
							metodoAnticonceptivo.getCodigo());
					insertarMetodoAnticonceptivoStatement.setString(3,
							metodoAnticonceptivo.getDescripcion());
					insertarMetodoAnticonceptivoStatement.setString(4,
							loginUsuario);

					int resp=insertarMetodoAnticonceptivoStatement.executeUpdate();
					insertarMetodoAnticonceptivoStatement.close();
					if (resp < 1) {
						// Terminamos la transaccion, sea con un rollback o un
						// commit.
						myFactory.abortTransaction(con);
						return 0;
					}
					// }
				}
			}
		}

		myFactory.endTransaction(con);
		con.setAutoCommit(true);

		return resp1;
	}

	

	/**
	 * Implementación de la revisión si existe antecedente de paciente para una
	 * BD Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#existeAntecedentePaciente(Connection ,
	 *      int ) throws SQLException
	 */
	public static boolean existeAntecedentePaciente(Connection con,
			int codigoPaciente) throws SQLException {
		PreparedStatement pst=null;
		ResultSet rs=null;
		boolean result=false;
		try{
			pst = con.prepareStatement(existeAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
	
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getInt("numResultados") < 1) {
					result= false;
				} else {
					result= true;
				}
			} else {
				// Si esta consulta no da un resultado (es un count) es porque algo
				// muy, pero
				// muy malo esta pasando
				throw new SQLException(
						"Error Desconocido en la busqueda de un Antecedente general para una paciente");
			}
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR existeAntecedentePaciente", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	    return result;
	}

	/**
	 * Implementación de cargar antecedente ginecoobstetrico para una BD
	 * Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#existeAntecedentePaciente(Connection ,
	 *      int ) throws SQLException
	 */
	public static ResultSetDecorator cargar(Connection con, int codigoPersona)
			throws SQLException {
		PreparedStatementDecorator cargarAntecedenteGinecoObstetricoStatement = new PreparedStatementDecorator(con.prepareStatement(cargarAntecedenteGinecoObstetricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarAntecedenteGinecoObstetricoStatement.setInt(1, codigoPersona);

		return new ResultSetDecorator(cargarAntecedenteGinecoObstetricoStatement.executeQuery());
	}

	/**
	 * Implementación de cargar el histórico de antecedente ginecoobstetrico
	 * para una BD Genérica
	 * 
	 * @param numeroSolicitud
	 *            Este numero de solicitu carga especificamente los datos
	 *            historicos iungresados desde dicha solicitud, si es 0 carga
	 *            todos los datos históricos
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarHistorico
	 *      (Connection , int, int )
	 */
	public static ResultSetDecorator cargarHistorico(Connection con, int codigoPaciente,
			int numeroSolicitud) {
		try {
			PreparedStatementDecorator cargarHistoricoStatement;
			if (numeroSolicitud == 0)
			{
				cargarHistoricoStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarHistoricoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarHistoricoStatement.setInt(1, codigoPaciente);
			}
			else
			{
				cargarHistoricoStatement =  new PreparedStatementDecorator(con.prepareStatement(cargarHistoricoValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				cargarHistoricoStatement.setInt(1, codigoPaciente);
				cargarHistoricoStatement.setInt(2, numeroSolicitud);
			}
			return new ResultSetDecorator(cargarHistoricoStatement.executeQuery());
		} catch (SQLException e) {
			logger
					.error("Error cargando el histórico de los antecedentes "
							+ e);
			return null;
		}
	}

	/**
	 * Implementación de cargar embarazos para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarEmbarazos
	 *      (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator cargarEmbarazos(Connection con, int codigoPaciente)
			throws SQLException {
		PreparedStatementDecorator cargarEmbarazosStatement = new PreparedStatementDecorator(con.prepareStatement(cargarEmbarazosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarEmbarazosStatement.setInt(1, codigoPaciente);
		return new ResultSetDecorator(cargarEmbarazosStatement.executeQuery());

	}

	/**
	 * Implementación de cargar complicaciones de los embarazos para una BD
	 * Genérica
	 * 
	 */
	public static Collection cargarComplicaciones(Connection con,
			int codigoPaciente, int codigoEmbarazo) {
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		Collection result=null;
		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(cargarComplicacionesEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoEmbarazo);
			pst.setInt(2, codigoPaciente);
			rs=new ResultSetDecorator(pst.executeQuery());
			result=UtilidadBD.resultSet2Collection(rs);
		} catch(Exception e){
		    Log4JManager.error("ERROR cargarComplicaciones", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	/**
	 * Implementación de cargar complicaciones otras de los embarazos para una
	 * BD Genérica
	 */
	public static Collection cargarComplicacionesOtras(Connection con,
			int codigoPaciente, int codigoEmbarazo) {
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		Collection result=null;
		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(cargarOtrasComplicacionesEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoEmbarazo);
			pst.setInt(2, codigoPaciente);
			rs=new ResultSetDecorator(pst.executeQuery());
			result=UtilidadBD.resultSet2Collection(rs);
		} catch(Exception e){
		    Log4JManager.error("ERROR cargarComplicacionesOtras", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	    return result;
	}

	/**
	 * Implementación de cargar métodos anticonceptivos para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarMetodosAnticonceptivos
	 *      (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator cargarMetodosAnticonceptivos(Connection con,
			int codigoPaciente) throws SQLException {
		PreparedStatementDecorator cargarMetodosAnticonceptivosStatement = new PreparedStatementDecorator(con.prepareStatement(cargarMetodosAnticonceptivosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		cargarMetodosAnticonceptivosStatement.setInt(1, codigoPaciente);
		
		logger.info("consulta: "+cargarMetodosAnticonceptivosStr.replace("?",codigoPaciente+""));
		return new ResultSetDecorator(cargarMetodosAnticonceptivosStatement.executeQuery());
	}

	/**
	 * Implementación de cargar la última información sobre embarazos para una
	 * BD Genérica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarUltInformacionEmbarazos
	 *      (Connection , int ) throws SQLException
	 */
	public static ResultSetDecorator cargarUltInformacionEmbarazos(Connection con,
			int codigoPaciente) throws SQLException {
		logger.info("cargarUltInfoEmbarazosStr->"+cargarUltInfoEmbarazosStr);
		PreparedStatementDecorator cargarInfoEmbarazos = new PreparedStatementDecorator(con.prepareStatement(cargarUltInfoEmbarazosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarInfoEmbarazos.setInt(1, codigoPaciente);

		return new ResultSetDecorator(cargarInfoEmbarazos.executeQuery());
	}

	/**
	 * Método que inserta los datos propios de la valoración gineco-obstetrica
	 * 
	 * @param con
	 * @param edadMenarquia
	 * @param edadMenopausia
	 * @param otraEdadMenarquia
	 * @param otraEdadMenopausia
	 * @param cilcoMenstrual
	 * @param duracionMenstruacion
	 * @param furAnt
	 * @param dolorMenstruacion
	 * @param conceptoMenstruacion
	 * @param observacionesMenstruacion
	 * @param codigoPersona
	 * @param numeroSolicitud
	 */
	public static int inseretarDatosHistMenstrual(Connection con,
			int edadMenarquia, int edadMenopausia, String otraEdadMenarquia,
			String otraEdadMenopausia, String cilcoMenstrual,
			String duracionMenstruacion, String furAnt,
			String dolorMenstruacion, int conceptoMenstruacion,
			String observacionesMenstruacion, int codigoPersona,
			int numeroSolicitud) 
	{
		int resp = ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		PreparedStatement pst4=null;
		ResultSet rs=null;
		try 
		{
			pst =  con.prepareStatement(actualizarAntecedentesDesdeValoracion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			//Se verifica si es diferente de otra
			if (edadMenarquia != 0) 
			{
				pst.setInt(1, edadMenarquia);
				pst.setNull(3, Types.VARCHAR);
			} 
			else if (otraEdadMenarquia != null && !otraEdadMenarquia.equals("")) 
			{
				pst.setInt(1, edadMenarquia);
				pst.setString(3, otraEdadMenarquia);
			} 
			else 
			{
				pst.setInt(1, -1);
				pst.setNull(3, Types.VARCHAR);
			}
			
			//Se verifica si es diferente de otra
			if (edadMenopausia != 0) 
			{
				pst.setInt(2, edadMenopausia);
				pst.setNull(4, Types.VARCHAR);
			} 
			else if (otraEdadMenopausia != null && !otraEdadMenopausia.equals("")) 
			{
				pst.setInt(2, edadMenopausia);
				pst.setString(4, otraEdadMenopausia);
			} 
			else 
			{
				pst.setInt(2, -1);
				pst.setNull(4, Types.VARCHAR);
			}
			
			pst.setInt(5, codigoPersona);
			resp = pst.executeUpdate();
			if (resp == 0) 
				logger.error("No se actualizó ningún dato en los antecedentes gineco-obsetetricos");
			else
			{
			
				//Se consulta el consecutivo del historico menstrual
				String obtenerCodigo = "SELECT CASE WHEN max(codigo) IS NULL THEN 1 ELSE max(codigo)+1 END AS codigo FROM ant_gineco_histo WHERE codigo_paciente=?";
				pst2 =  con.prepareStatement(obtenerCodigo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.setInt(1, codigoPersona);
				rs = pst2.executeQuery();
				int codigo = 0;
				if (rs.next())
					codigo = rs.getInt("codigo");
	
				pst3 =  con.prepareStatement(insertarDatosHistMenstrualStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst3.setInt(1, codigoPersona);
				pst3.setInt(2, codigo);
				if (!duracionMenstruacion.equals("")) 
					pst3.setInt(3, Integer.parseInt(duracionMenstruacion));
				else 
					pst3.setNull(3, Types.INTEGER);
				
				if (dolorMenstruacion.equals("")) 
					pst3.setObject(4, null);
				else 
					pst3.setBoolean(4, UtilidadTexto.getBoolean(dolorMenstruacion));
				
				if (furAnt.equals(""))
					pst3.setNull(5, Types.DATE);
				 else
					pst3.setString(5, UtilidadFecha.conversionFormatoFechaABD(furAnt));
	
				pst3.setInt(6, conceptoMenstruacion);
				pst3.setString(7, observacionesMenstruacion);
				if (!cilcoMenstrual.equals("")) 
					pst3.setInt(8, Integer.parseInt(cilcoMenstrual));
				else 
					pst3.setNull(8, Types.INTEGER);
				
				resp = pst3.executeUpdate();
				if (resp == 0) 
					logger.error("No se insertó ningún dato en los antecedentes gineco-obsetetricos");
				
	
				if (resp > 0) 
				{
					String insertarRelacionValHistroriaStr = "INSERT INTO val_historia_menstrual(valoracion, historico_ant, codigo_paciente) VALUES(?, ?, ?)";
					pst4 = con.prepareStatement(insertarRelacionValHistroriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst4.setInt(1, numeroSolicitud);
					pst4.setInt(2, codigo);
					pst4.setInt(3, codigoPersona);
					resp = pst4.executeUpdate();
				}
				if (resp < 1) 
					logger.error("Error insertando los datos de la relacion val -historia menstrual (Desde valoración)");
				
			}
		} 
		catch(Exception e){
		    Log4JManager.error("ERROR inseretarDatosHistMenstrual", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst4 != null){
					pst4.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}

	/**
	 * Metodo para llamar desde Historia de Atenciones.
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargar(Connection con, HashMap mapa)
			throws SQLException {

		// -- Los parametros de Busqueda
		String fechaInicio = UtilidadCadena.noEsVacio(mapa.get("fechaInicio")
				+ "") ? mapa.get("fechaInicio") + "" : "";
		String horaInicio = UtilidadCadena.noEsVacio(mapa.get("horaInicio")
				+ "") ? mapa.get("horaInicio") + "" : "";
		String fechaFinal = UtilidadCadena.noEsVacio(mapa.get("fechaFinal")
				+ "") ? mapa.get("fechaFinal") + "" : "";
		String horaFinal = UtilidadCadena.noEsVacio(mapa.get("horaFinal") + "") ? mapa
				.get("horaFinal")
				+ ""
				: "";

		// -- Para insertar las condiciones de Busqueda a las consultas.
		String cadFechas = "";

		if (!fechaInicio.equals("")) {
			if (!horaInicio.equals("")) {
				cadFechas = " AND antgin.fecha ||'-'|| antgin.hora >= '"
						+ fechaInicio + "'-'" + horaInicio + "'";
			} else {
				cadFechas = "  AND antgin.fecha >= '" + fechaInicio + "'";
			}
		}
		if (!fechaFinal.equals("")) {
			if (!horaFinal.equals("")) {
				cadFechas += " AND antgin.fecha ||'-'|| antgin.hora <= '"
						+ fechaFinal + "'-'" + horaFinal + "'";
			} else {
				cadFechas += " AND antgin.fecha <= '" + fechaFinal + "'";
			}
		}

		String consulta = "	SELECT antgin.otra_edad_menarquia AS otraEdadMenarquia, edadmenar.codigo AS codigoEdadMenarquia,	"
				+ "		   edadmenar.nombre AS edadMenarquia, antgin.otra_edad_menopausia AS otraEdadMenopausia, 		"
				+ "		   edadmenop.codigo AS codigoEdadMenopausia, edadmenop.nombre AS edadMenopausia, 				"
				+ "		   antgin.observaciones, antgin.edad_inic_vida_sexual AS inicioVidaSexual,"
				+ "		   edad_inic_vida_obstetrica AS inicioVidaObstetrica,  substr( antgin.fecha,0,9) AS fecha, "
				+ "		   antgin.hora, antgin.usuario "
				+ "		   FROM ant_gineco_obste antgin, "
				+ "				rangos_edad_menarquia edadmenar, "
				+ "				rangos_edad_menopausia edadmenop "
				+ "				WHERE antgin.edad_menarquia = edadmenar.codigo "
				+ "				  AND antgin.edad_menopausia = edadmenop.codigo "
				+ "				  AND codigo_paciente = "
				+ mapa.get("paciente")
				+ cadFechas;

		PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(stm.executeQuery());
	}

	/**
	 * Cargar Historico
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param numeroSolicitud
	 * @return
	 */
	public static ResultSetDecorator cargarHistorico(Connection con, HashMap mapa) {
		try {

			// -- Los parametros de Busqueda
			String fechaInicio = UtilidadCadena.noEsVacio(mapa
					.get("fechaInicio")
					+ "") ? mapa.get("fechaInicio") + "" : "";
			String horaInicio = UtilidadCadena.noEsVacio(mapa.get("horaInicio")
					+ "") ? mapa.get("horaInicio") + "" : "";
			String fechaFinal = UtilidadCadena.noEsVacio(mapa.get("fechaFinal")
					+ "") ? mapa.get("fechaFinal") + "" : "";
			String horaFinal = UtilidadCadena.noEsVacio(mapa.get("horaFinal")
					+ "") ? mapa.get("horaFinal") + "" : "";

			// -- Para insertar las condiciones de Busqueda a las consultas.
			String cadFechas = "";

			if (!fechaInicio.equals("")) {
				if (!horaInicio.equals("")) {
					cadFechas = " AND  hist.fecha_creacion ||'-'|| hist.hora_creacion >= '"
							+ fechaInicio + "'-'" + horaInicio + "'";
				} else {
					cadFechas = "  AND  hist.fecha_creacion >= '" + fechaInicio
							+ "'";
				}
			}
			if (!fechaFinal.equals("")) {
				if (!horaFinal.equals("")) {
					cadFechas += " AND  hist.fecha_creacion ||'-'|| hist.hora_creacion <= '"
							+ fechaFinal + "'-'" + horaFinal + "'";
				} else {
					cadFechas += " AND  hist.fecha_creacion <= '" + fechaFinal
							+ "'";
				}
			}

			String consulta = "		SELECT hist.duracion_menstruacion AS duracionMenstruacion, 			 "
					+ "			   hist.dolor_menstruacion AS dolorMenstruacion,				 "
					+ "			   hist.fecha_ultima_regla AS fechaUltimaRegla, 				 "
					+ "			   hist.concepto_menstruacion AS codigoConceptoMenstruacion,	 "
					+ "			   conmens.nombre AS conceptoMenstruacion, 						 "
					+ "			   hist.fecha_ultima_mamografia AS fechaUltimaMamografia, 		 "
					+ "			   hist.descripcion_ultima_mamografia AS descUltimaMamografia,	 "
					+ "			   hist.fecha_ultima_ecografia AS fechaUltimaEcografia, 		 "
					+ "			   hist.descripcion_ultima_ecografia AS descUltimaEcografia, "
					+ "			   hist.fecha_ultima_citologia AS fechaUltimaCitologia,"
					+ "			   hist.descripcion_ultima_citologia AS descUltimaCitologia, "
					+ "			   hist.fecha_ultima_densimetria_osea AS fechaUltDensimetriaOsea, "
					+ "			   hist.desc_ultima_densimetria_osea AS descUltDensimentriaOsea,"
					+ "			   hist.descripcion_ultimo_proc_gine AS descUltimoProcGin, "
					+ "			   hist.observaciones_menstruacion AS obsMenstruacion, "
					+ "			   hist.ciclo_menstrual AS cicloMenstrual, "
					+ "			   hist.g_informacion_embarazos AS gInfoEmbarazos, "
					+ "			   hist.p_informacion_embarazos AS pInfoEmbarazos, "
					+ "			   hist.p2500 as p2500, hist.p2500, hist.p4000 as p4000,"
					+ "			   hist.a_informacion_embarazos AS aInfoEmbarazos, hist.mayora2 AS mayora2, "
					+ "			   hist.c_informacion_embarazos AS cInfoEmbarazos, "
					+ "			   hist.v_informacion_embarazos AS vInfoEmbarazos,"
					+ "			   hist.m_informacion_embarazos AS mInfoEmbarazos, "
					+ "			   hist.finembarazoanterior AS finembarazoanterior, "
					+ "			   hist.finembarazomayor1o5 AS finembarazomayor1o5, hist.prematuros AS prematuros,"
					+ " 		   hist.ectropicos AS ectropicos, hist.multiples AS multiples,"
					+ " 		   hist.fecha_creacion AS fechaCreacion, "
					+ "			   hist.hora_creacion AS horaCreacion, "
					+ "		   	   tipo_embarazo as tipoembarazo, " 
					+ "			   muertos_antes_1semana as muertosantes1semana," 
					+ "			   muertos_despues_1semana as muertosdespues1semana,vivos_actualmente as vivosactualmente " 
					+ "			   FROM ant_gineco_histo hist, "
					+ "					conceptos_menstruacion conmens "
					+ "					WHERE hist.concepto_menstruacion=conmens.codigo "
					+ "					  AND hist.codigo_paciente= "
					+ mapa.get("paciente")
					+ cadFechas
					+ "						  ORDER BY hist.codigo";

			PreparedStatementDecorator stm;
			stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			// stm.setInt(1, UtilidadCadena.vInt(mapa.get("paciente")+""));

			return new ResultSetDecorator(stm.executeQuery());
		} catch (SQLException e) {
			logger
					.error("Error cargando el histórico de los antecedentes "
							+ e);
			return null;
		}
	}

	/**
	 * Para Resumen de Atenciones
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarEmbarazos(Connection con, HashMap mapa)
			throws SQLException {
		// -- Los parametros de Busqueda
		String fechaInicio = UtilidadCadena.noEsVacio(mapa.get("fechaInicio")
				+ "") ? mapa.get("fechaInicio") + "" : "";
		String horaInicio = UtilidadCadena.noEsVacio(mapa.get("horaInicio")
				+ "") ? mapa.get("horaInicio") + "" : "";
		String fechaFinal = UtilidadCadena.noEsVacio(mapa.get("fechaFinal")
				+ "") ? mapa.get("fechaFinal") + "" : "";
		String horaFinal = UtilidadCadena.noEsVacio(mapa.get("horaFinal") + "") ? mapa
				.get("horaFinal")
				+ ""
				: "";

		// -- Para insertar las condiciones de Busqueda a las consultas.
		String cadFechas = "";

		if (!fechaInicio.equals("")) {
			if (!horaInicio.equals("")) {
				cadFechas = " AND  ago.fecha ||'-'|| ago.hora >= '"
						+ fechaInicio + "'-'" + horaInicio + "'";
			} else {
				cadFechas = "  AND  ago.fecha >= '" + fechaInicio + "'";
			}
		}
		if (!fechaFinal.equals("")) {
			if (!horaFinal.equals("")) {
				cadFechas += " AND  ago.fecha ||'-'|| ago.hora <= '"
						+ fechaFinal + "'-'" + horaFinal + "'";
			} else {
				cadFechas += " AND  ago.fecha <= '" + fechaFinal + "'";
			}
		}

		String consulta = " SELECT emb.codigo, emb.meses_gestacion AS mesesGestacion,										"
				+ "		   emb.fecha_terminacion AS fechaTerminacion, tiptpar.nombre AS trabajoParto,				"
				+ "		   emb.trabajo_parto AS codigoTrabajoParto, emb.otro_trabajo_parto AS otroTrabajoParto,		"
				+ "		   emb.duracion_trabajo_parto AS duracion, emb.tiempo_ruptura_membranas AS ruptura, 		"
				+ "		   emb.legrado AS legrado																	"
				+ "		   FROM ant_gineco_embarazo emb																"
				+ "				INNER JOIN tipos_trabajo_parto tiptpar ON ( emb.trabajo_parto = tiptpar.codigo )	"
				+ "				INNER JOIN ant_gineco_obste ago  ON ( ago.codigo_paciente = emb.codigo_paciente )	"
				+ "					 WHERE emb.codigo_paciente = "
				+ mapa.get("paciente")
				+ cadFechas
				+ "				  	   AND emb.fin_embarazo="
				+ ValoresPorDefecto.getValorTrueParaConsultas()
				+ "					       ORDER BY emb.codigo													";

		PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(stm.executeQuery());
	}

	/**
	 * Implementación de cargar complicaciones de los embarazos para una BD
	 * Genérica
	 * 
	 */
	public static Collection cargarComplicaciones(Connection con, HashMap mapa) {
		PreparedStatementDecorator stm=null;
		ResultSetDecorator rs=null;
		Collection result=null;

		// -- Los parametros de Busqueda
		String fechaInicio = UtilidadCadena.noEsVacio(mapa.get("fechaInicio")
				+ "") ? mapa.get("fechaInicio") + "" : "";
		String horaInicio = UtilidadCadena.noEsVacio(mapa.get("horaInicio")
				+ "") ? mapa.get("horaInicio") + "" : "";
		String fechaFinal = UtilidadCadena.noEsVacio(mapa.get("fechaFinal")
				+ "") ? mapa.get("fechaFinal") + "" : "";
		String horaFinal = UtilidadCadena.noEsVacio(mapa.get("horaFinal") + "") ? mapa
				.get("horaFinal")
				+ ""
				: "";

		// -- Para insertar las condiciones de Busqueda a las consultas.
		String cadFechas = "";

		if (!fechaInicio.equals("")) {
			if (!horaInicio.equals("")) {
				cadFechas = " AND  ago.fecha ||'-'|| ago.hora >= '"
						+ fechaInicio + "'-'" + horaInicio + "'";
			} else {
				cadFechas = "  AND  ago.fecha >= '" + fechaInicio + "'";
			}
		}
		if (!fechaFinal.equals("")) {
			if (!horaFinal.equals("")) {
				cadFechas += " AND  ago.fecha ||'-'|| ago.hora <= '"
						+ fechaFinal + "'-'" + horaFinal + "'";
			} else {
				cadFechas += " AND  ago.fecha <= '" + fechaFinal + "'";
			}
		}

		try {
			String consulta = " SELECT cg.codigo AS codigo, cg.complicacion AS codigoComplicacion,						 "
					+ "		   comp.nombre AS nombreComplicacion 												 "
					+ "		   FROM complicaciones_gineco cg 													 "
					+ "				INNER JOIN complicaciones_embarazo comp ON(comp.codigo=cg.complicacion) 	 "
					+ "				INNER JOIN ant_gineco_embarazo age ON ( cg.antecedente = age.codigo AND cg.paciente = codigo_paciente )  "
					+ "				INNER JOIN ant_gineco_obste ago ON ( ago.codigo_paciente = age.codigo_paciente )  "
					+ "					 WHERE cg.antecedente = "
					+ mapa.get("embarazo")
					+ "					   AND cg.paciente = " + mapa.get("paciente");

			stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(stm.executeQuery());
			result=UtilidadBD.resultSet2Collection(rs);
		} 
		catch(Exception e){
		    Log4JManager.error("ERROR cargarComplicaciones", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(stm != null){
					stm.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	/**
	 * Implementación de cargar complicaciones otras de los embarazos para una
	 * BD Genérica
	 */
	public static Collection cargarComplicacionesOtras(Connection con,
			HashMap mapa) {
		// -- Los parametros de Busqueda
		String fechaInicio = UtilidadCadena.noEsVacio(mapa.get("fechaInicio")
				+ "") ? mapa.get("fechaInicio") + "" : "";
		String horaInicio = UtilidadCadena.noEsVacio(mapa.get("horaInicio")
				+ "") ? mapa.get("horaInicio") + "" : "";
		String fechaFinal = UtilidadCadena.noEsVacio(mapa.get("fechaFinal")
				+ "") ? mapa.get("fechaFinal") + "" : "";
		String horaFinal = UtilidadCadena.noEsVacio(mapa.get("horaFinal") + "") ? mapa
				.get("horaFinal")
				+ ""
				: "";

		// -- Para insertar las condiciones de Busqueda a las consultas.
		String cadFechas = "";

		if (!fechaInicio.equals("")) {
			if (!horaInicio.equals("")) {
				cadFechas = " AND  ago.fecha ||'-'|| ago.hora >= '"
						+ fechaInicio + "'-'" + horaInicio + "'";
			} else {
				cadFechas = "  AND  ago.fecha >= '" + fechaInicio + "'";
			}
		}
		if (!fechaFinal.equals("")) {
			if (!horaFinal.equals("")) {
				cadFechas += " AND  ago.fecha ||'-'|| ago.hora <= '"
						+ fechaFinal + "'-'" + horaFinal + "'";
			} else {
				cadFechas += " AND  ago.fecha <= '" + fechaFinal + "'";
			}
		}

		PreparedStatementDecorator stm=null;
		ResultSetDecorator rs=null;
		Collection result=null;
		try {
			String consulta = "	SELECT cgo.codigo AS codigo, cgo.otra_complicacion AS complicacion 	"
					+ "		   FROM compli_gineco_otra cgo								"
					+ "				INNER JOIN ant_gineco_embarazo age ON ( cgo.antecedente = age.codigo AND cgo.paciente = codigo_paciente )  "
					+ "				INNER JOIN ant_gineco_obste ago ON ( ago.codigo_paciente = age.codigo_paciente )  "
					+ "				  	 WHERE antecedente = "
					+ mapa.get("antecedente")
					+ "				 	   AND paciente = " + mapa.get("paciente");

			stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(stm.executeQuery());
			result=UtilidadBD.resultSet2Collection(rs);
		}catch(Exception e){
		    Log4JManager.error("ERROR cargarComplicacionesOtras", e);
	    }
	    finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(stm != null){
					stm.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	/**
	 * Para Historia de Atenciones.
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarMetodosAnticonceptivos(Connection con,
			HashMap mapa) throws SQLException {
		// -- Los parametros de Busqueda
		String fechaInicio = UtilidadCadena.noEsVacio(mapa.get("fechaInicio")
				+ "") ? mapa.get("fechaInicio") + "" : "";
		String horaInicio = UtilidadCadena.noEsVacio(mapa.get("horaInicio")
				+ "") ? mapa.get("horaInicio") + "" : "";
		String fechaFinal = UtilidadCadena.noEsVacio(mapa.get("fechaFinal")
				+ "") ? mapa.get("fechaFinal") + "" : "";
		String horaFinal = UtilidadCadena.noEsVacio(mapa.get("horaFinal") + "") ? mapa
				.get("horaFinal")
				+ ""
				: "";

		// -- Para insertar las condiciones de Busqueda a las consultas.
		String cadFechas = "";

		if (!fechaInicio.equals("")) {
			if (!horaInicio.equals("")) {
				cadFechas = " AND  antmet.fecha ||'-'|| antmet.hora >= '"
						+ fechaInicio + "'-'" + horaInicio + "'";
			} else {
				cadFechas = "  AND  antmet.fecha >= '" + fechaInicio + "'";
			}
		}
		if (!fechaFinal.equals("")) {
			if (!horaFinal.equals("")) {
				cadFechas += " AND  antmet.fecha ||'-'|| antmet.hora <= '"
						+ fechaFinal + "'-'" + horaFinal + "'";
			} else {
				cadFechas += " AND  antmet.fecha <= '" + fechaFinal + "'";
			}
		}

		String consulta = "	SELECT antmet.anticonceptivo AS codigoMetodoAnticon, met.nombre AS metodoAnticonceptivo, "
				+ "		   antmet.descripcion "
				+ "		   FROM met_anticon_ant_go antmet, "
				+ "				metodos_anticonceptivos met "
				+ "				WHERE antmet.anticonceptivo = met.codigo "
				+ "				  AND antmet.codigo_paciente = "
				+ mapa.get("paciente")
				+ cadFechas
				+ "				  AND antmet.fecha ||''|| antmet.hora = (SELECT max(fecha ||''|| hora) FROM met_anticon_ant_go)";

		PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(stm.executeQuery());
	}

}
