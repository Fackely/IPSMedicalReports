/*
 * @(#)SqlBaseAntecedentePediatricoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
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
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.antecedentes.Dosis;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;
import com.princetonsa.mundo.antecedentes.InmunizacionesPediatricas;

/**
 * Esta clase implementa la funcionalidad com�n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL est�ndar. M�todos particulares a Antecedente Pediatrico
 *
 *	@version 1.0, Abril 01, 2004
*/
@SuppressWarnings("rawtypes")
public class SqlBaseAntecedentePediatricoDao 
{
	/**
	* Cadena constante con el <i>statement</i> necesario para insertar un tipo parto de un
	* antecedente pediatrico
	*/
	private static final String is_insertarTipoParto =
		"INSERT "	+
		"INTO "		+	"ant_ped_tip_parto"	+
		"("			+
			"codigo_paciente,"			+
			"tipo_parto,"				+
			"motivo"					+
		")"								+
		"VALUES(?,?,?)";

	/**
	* Cadena constante con el <i>statement</i> necesario para modificar un tipo parto de un
	* antecedente pediatrico
	*/
	private static final String is_modificarTipoParto =
		"UPDATE "	+	"ant_ped_tip_parto "			+
		"SET "		+	"motivo"			+	"=? "			+
		"WHERE "	+	"codigo_paciente"	+	"=?" + " AND "	+
						"tipo_parto"		+	"=?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para mirar si
	 * existe una entrada en la tabla antecedentes_pacientes para esta
	 * paciente en particular en una BD Gen�rica.
	 */
	private static final String existeAntecedentePacienteStr = "SELECT count(1)  as numResultados FROM antecedentes_pacientes WHERE codigo_paciente = ? ";



	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar
	 * en la tabla guia (antecedentes_paciente) una entrada, en una base de
	 * datos Gen�rica.
	 */
	private static final String insertarAntecedentePacienteStr = "INSERT INTO antecedentes_pacientes (codigo_paciente) VALUES(?)";

	
	/**
    * Cadena constante con el <i>statement</i> necesario para insertar una dosis para una
	* inmunizacion de un antecedente pediatrico
	*/
	private static final String is_insertarInmunizacionesDosis =
			"INSERT INTO "							+
			"ant_ped_inmu_dosis"	+
			"("							+
				"codigo_paciente,"		+
				"tipo_inmunizacion,"	+
				"numero_dosis,"			+
				"fecha"					+
			")"							+
			"VALUES(?,?,?,?)";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para insertar una observacion para una
	* inmunizacion de un antecedente pediatrico
	*/
	private static final String is_insertarInmunizacionesObser =
			"INSERT INTO "				+
			"ant_ped_inmu_obser" +
			"("							+
				"codigo_paciente,"		+
				"tipo_inmunizacion,"	+
				"observacion"			+
			")"							+
			"VALUES(?,?,?)";		
			
	/**
	* Cadena constante con el <i>statement</i> necesario para modificar una observacion para una
	* inmunizacion de un antecedente pediatrico
	*/
	private static final String is_modificarInmunizacionesObser =
			"UPDATE "	+	"ant_ped_inmu_obser "	+
			"SET "		+	"observacion"		+ "=? " +
			"WHERE "	+	"codigo_paciente"	+ "=?"	+	" AND "	+
							"tipo_inmunizacion"	+ "=?";
		
	/**
	* Cadena constante con el <i>statement</i> necesario para modificar una dosis para una
	* inmunizacion de un antecedente pediatrico
	*/
	private static final String is_modificarInmunizacionesDosis =
		"UPDATE "	+	"ant_ped_inmu_dosis "	+
		"SET "		+	"fecha"		+ "=? " +
		"WHERE "	+	"codigo_paciente"	+ "=?"	+	" AND "	+
						"tipo_inmunizacion"	+ "=?"	+	" AND "	+
						"numero_dosis"		+ "=?";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar un antecedente pediatrico
	*/
	private static final String is_consultarAntecedentePediatrico =
		"SELECT "	+	"* "						+
		"FROM "		+	"antecedentes_pediatricos "	+
		"WHERE "	+	"codigo_paciente"	+ "=?";

	/**
	* Cadena constante con el <i>statement</i> necesario para consultar el nombre de una
	* presentacion de nacimiento
	*/
	private static final String is_consultarNombrePresentacionNacimiento =
		"SELECT nombre FROM presenta_nacimiento WHERE codigo=?";

	private static final String is_consultarNombreUbicacionFeto =
		"SELECT nombre FROM ubicaciones_feto WHERE codigo=?";
		
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar la observacion de un tipo de
	* inmunizacion
	*/
	private static final String is_consultarInmunizacionObservacion =
			"SELECT "	+	"observacion "							+
			"FROM "		+	"ant_ped_inmu_obser "	+
			"WHERE "	+	"codigo_paciente"	+	"=? " + "AND "	+
							"tipo_inmunizacion"	+	"=?";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar el nombre de un tipo de
	* trabajo de parto
	*/
	private static final String is_consultarNombreTipoTrabajoParto =
		"SELECT nombre FROM tipos_trabajo_parto WHERE codigo=?";
		
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar el nombre de un riegoApgar
	*/
	private static final String is_consultarNombreRiesgoApgar =
		"SELECT nombre FROM riesgos_apgar WHERE codigo=?";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar las dosis de una vacuna para
	* un paciente con antecedente pediatrico
	*/
	private static final String is_consultarInmunizacionesDosis =
		"SELECT "	+	"tipo_inmunizacion,"					+
						"apid.numero_dosis AS numero_dosis,"	+
						"fecha,"								+
						"nombre "								+
		"FROM "		+	"ant_ped_inmu_dosis " +	"apid,"	+
						"tipos_inmunizacion "					+
		"WHERE "	+	"codigo_paciente"	+ "=?" 				+	" AND "	+
						"codigo"			+ "=tipo_inmunizacion "			+
		"ORDER BY "	+	"tipo_inmunizacion";
			
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar las observaciones de las
	* vacunas de un paciente con antecedente pediatrico
	*/
	private static final String is_consultarInmunizacionesObservacion =
		"SELECT "	+	"tipo_inmunizacion,"	+
						"nombre,"				+
						"observacion "			+
		"FROM "		+	"ant_ped_inmu_obser,"	+
						"tipos_inmunizacion "					+
		"WHERE "	+	"codigo_paciente"	+ "=?" 				+	" AND "	+
						"codigo"			+ "=tipo_inmunizacion "			+
		"ORDER BY "	+	"tipo_inmunizacion";

	/**
	* Cadena constante con el <i>statement</i> necesario para consultar nombre y motivo de un tipo
	* de parto
	*/
	private static final String is_consultarTiposParto =
		"SELECT "	+	"tipo_parto,"	+
						"motivo,"		+
						"nombre "		+
		"FROM "		+	"ant_ped_tip_parto,"	+
						"tipos_parto "					+
		"WHERE "	+	"codigo_paciente"	+ "=?"				+ " AND " +
						"codigo"			+ "=tipo_parto "	+
		"ORDER BY "	+	"tipo_parto";

	/**
	* Cadena constante con el <i>statement</i> necesario para consultar la
	* informaci�n de todas las opciones de embarazo
	*/
	private static final String is_consultarEmbarazoOpciones =
		"SELECT "	+	"* "					+
		"FROM "		+	"ant_ped_embarazo "		+
		"WHERE "	+	"codigo_paciente"		+ "=?";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar la informacion de la madre
	* del paciente que tiene antecedentes pediatricos
	*/
	private static final String is_consultarInfoMadre =
		"SELECT "	+	"* "					+
		"FROM "		+	"ant_ped_info_madre,"	+
						"tipos_sangre "			+
		"WHERE "	+	"codigo_paciente"		+ "=?"				+ " AND"	+
						"("						+
							"tipo_sangre"		+ " IS NULL"		+ " OR"		+
							"("					+
								"tipo_sangre"	+ " IS NOT NULL"	+ " AND "	+
								"tipo_sangre"	+ "=codigo"		+
							")"					+
						")";
	
	/**
	* Cadena constante con el <i>statement</i> necesario para consultar la informacion del padre del
	* paciente que tiene antecedentes pediatricos
	*/
	private static final String is_consultarInfoPadre =
		"SELECT "	+	"* "					+
		"FROM "		+	"ant_ped_info_padre,"	+
						"tipos_sangre "			+
		"WHERE "	+	"codigo_paciente"		+ "=?"				+ " AND"	+
						"("						+
							"tipo_sangre"		+ " IS NULL"		+ " OR"		+
							"("					+
								"tipo_sangre"	+ " IS NOT NULL"	+ " AND "	+
								"tipo_sangre"	+ "=codigo"		+
							")"					+
						")";

	/**
	* Cadena constante con el <i>statement</i> necesario para insertar un antecedente pediatrico
	*/
	private static final String is_insertarAntecedentePediatrico =
		"INSERT INTO "				+
		"antecedentes_pediatricos"	+
		"("							+
			"codigo_paciente,"						+
			"presentacion_nacimiento,"				+
			"otra_presentacion_nacimiento,"			+
			"ubicacion_feto,"						+
			"otra_ubicacion_feto,"					+
			"duracion_parto_mins,"					+
			"duracion_parto_horas,"					+
			"ruptura_membranas_mins,"				+
			"ruptura_membranas_horas,"				+
			"caracteristicas_liq_amniotico,"		+
			"sufrimiento_fetal,"					+
			"causas_sufrimiento_fetal,"				+
			"anestesia_medicamentos,"				+
			"tipo_trabajo_parto,"					+
			"comentario_tipo_trabajo_parto,"		+
			"otro_tipo_parto,"						+
			"motivo_tipo_parto,"					+
			"tiempo_nacimiento,"					+
			"caracteristicas_placenta,"				+
			"talla,"								+
			"peso,"									+
			"perimetro_cefalico,"					+
			"perimetro_toracico,"					+
			"apgar_minuto1,"						+
			"apgar_minuto5,"						+
			"apgar_minuto10,"						+
			"riesgo_apgar_minuto1,"					+
			"riesgo_apgar_minuto5,"					+
			"riesgo_apgar_minuto10,"				+
			"embarazos_anteriores_otros,"			+
			"incompatibilidad_abo,"					+
			"incompatibilidad_rh,"					+
			"macrosomicos,"							+
			"malformaciones_congenitas,"			+
			"mortinatos,"							+
			"muertes_fetales_tempranas,"			+
			"prematuros,"							+
			"codigo_serologia,"						+
			"descripcion_serologia,"				+
			"codigo_test_sullivan,"					+
			"descripcion_test_sullivan,"			+
			"control_prenatal,"						+
			"frecuencia_control_prenatal,"			+
			"lugar_control_prenatal,"				+
			"duracion_expulsivo_horas,"				+
			"duracion_expulsivo_mins,"				+
			"amnionitis,"							+
			"amnionitis_factor_anormal,"			+
			"anestesia,"							+
			"anestesia_tipo,"						+
			"nst,"									+
			"nst_descripcion,"						+
			"otros_examenes_trabajo_parto,"			+
			"perfil_biofisico,"						+
			"perfil_biofisico_descripcion,"			+
			"ptc,"									+
			"ptc_descripcion,"						+
			"complicaciones_parto,"					+
			"muestra_cordon_umbilical,"				+
			"mues_cor_umbilical_des,"	+
			"cordon_umb_carac,"		+
			"cordon_umbilical_descripcion,"			+
			"gemelo,"								+
			"gemelo_descripcion,"					+
			"intrauterino_peg,"						+
			"intrauterino_peg_causa,"				+
			"intrauterino_anormalidad,"				+
			"intrauterino_anormalidad_causa,"		+
			"intrauterino_armonico,"				+
			"intrauterino_armonico_causa,"			+
			"reanimacion,"							+
			"reanimacion_aspiracion,"				+
			"reanimacion_medicamentos,"				+
			"sano,"									+
			"sano_descripcion,"						+
			"sexo_descripcion,"						+
			"edad_gestacional,"						+
			"edad_gestacional_descripcion,"			+
			"liq_amniotico_claro,"					+
			"liq_amniotico_meconiado,"				+
			"liq_amniotico_meconiado_grado,"		+
			"liq_amniotico_sanguinolento,"			+
			"liq_amniotico_fetido,"					+
			"placenta_caracteristicas,"				+
			"plac_catac_desc,"	+
			"observaciones,"						+
			"login_usuario,"						+
			"fecha"									+
		")"							+
		"VALUES("	+	"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"	+
						"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"	+
						"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"	+
						"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"	+
						"?,?,?,?,?,?,?,?)";
						
	/**
	* Cadena constante con el <i>statement</i> necesario para insertar la informacion de la madre
	* del paciente en la tabla ant_ped_info_madre, que contiene la infomracion de todas madres de
	* los pacientes que tienen antecedentes pediatricos
	*/
	private static final String is_insertarInfoMadre =
		"INSERT INTO " 			+
		"ant_ped_info_madre"	+
		"("						+
			"codigo_paciente,"				+
			"edad,"							+
			"tipo_sangre,"					+
			"semanas_gestacion,"			+
			"obs_patologias,"				+
			"obs_examenes_medicamentos,"	+
			"observaciones,"				+
			"confiable_semanas_gestacion,"	+
			"fpp,"							+
			"fur,"							+
			"info_embarazos_a,"				+
			"info_embarazos_c,"				+
			"info_embarazos_g,"				+
			"info_embarazos_m,"				+
			"info_embarazos_p,"				+
			"info_embarazos_v,"				+
			"metodo_semanas_gestacion,"		+
			"primer_apellido,"				+
			"primer_nombre,"				+
			"segundo_apellido,"				+
			"segundo_nombre,"				+
			"tipo_identificacion,"			+
			"numero_identificacion,"			+
			"detalle_otro_metodo"			+
		")"						+
		"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	* Cadena constante con el <i>statement</i> necesario para consultar la informaci�n de una opci�n
	* de embarazo
	*/
	private static final String is_consultarEmbarazoOpcion =
		"SELECT "	+	"* "								+
		"FROM "		+	"ant_ped_embarazo "					+
		"WHERE "	+	"codigo_paciente"					+ "=?" + " AND " +
						"codigo_categoria_embarazo"			+ "=?" + " AND " +
						"cod_cat_embarazo_opcion"	+ "=?";

	/**
	* Cadena constante con el <i>statement</i> necesario para insertar una
	* opc��n de embarazo
	*/
	private static final String is_insertarEmbarazoOpcion =
		"INSERT INTO "		+
		"ant_ped_embarazo"	+
		"("					+
			"codigo_paciente,"					+
			"codigo_categoria_embarazo,"		+
			"cod_cat_embarazo_opcion,"	+
			"valor_campo1,"						+
			"valor_campo2,"						+
			"valor_campo3,"						+
			"existio"							+
		")"					+
		"VALUES(?,?,?,?,?,?,?)";


	/**
	* Cadena constante con el <i>statement</i> necesario para insertar un un paciente en la tabla
	* antecedentes_pacientes, que contiene todos los pacientes con algun antecedente ingresado en
	* el sistema.
	*/
	private static final String is_insertarAntecedente =
		"INSERT INTO antecedentes_pacientes(codigo_paciente)VALUES(?)";

	/**
	* Cadena constante con el <i>statement</i> necesario para consultar si un paciente tiene un
	* antecedente registrado en el sistema
	*/
	private static final String is_consultarAntecedente =
		"SELECT "	+	"* "						+
		"FROM "		+	"antecedentes_pacientes "	+
		"WHERE "	+	"codigo_paciente"	+ "=?";

	/**
	* Cadena constante con el <i>statement</i> necesario para insertar la informacion del padre del
	* paciente en la tabla ant_ped_info_madre, que contiene la infomracion de todas madres de los
	* pacientes que tienen antecedentes pediatricos
	*/
	private static final String is_insertarInfoPadre =
		"INSERT INTO "			+
		"ant_ped_info_padre"	+
		"("						+
			"codigo_paciente,	"			+
			"edad,"							+
			"consanguinidad,"				+
			"tipo_sangre,"					+
			"primer_apellido,"				+
			"primer_nombre,"				+
			"segundo_apellido,"				+
			"segundo_nombre,"				+
			"tipo_identificacion,"			+
			"numero_identificacion"			+
		")"						+
		"VALUES(?,?,?,?,?,?,?,?,?,?)";

	

	/**
	* Cadena constante con el <i>statement</i> necesario para modificar la informacion del padre del paciente
	* en la tabla ant_ped_info_madre, que contiene la infomracion de todas madres de los pacientes que tienen
	* antecedentes pediatricos
	*/
	private static final String is_modificarEmbarazoOpcion =
		"UPDATE "	+	"ant_ped_embarazo "					+
		"SET "		+	"valor_campo1"						+ "=?," +
						"valor_campo2"						+ "=?," +
						"valor_campo3"						+ "=?," +
						"existio"							+ "=? " +
		"WHERE "	+	"codigo_paciente"					+ "=?"	+ " AND " +
						"codigo_categoria_embarazo"			+ "=?"	+ " AND " +
						"cod_cat_embarazo_opcion"	+ "=?";

	/**
	* Cadena constante con el <i>statement</i> necesario para modificar la informacion de la madre
	* del paciente en la tabla ant_ped_info_madre, que contiene la infomracion de todas madres de
	* los pacientes que tienen antecedentes pediatricos
	*/
	private static final String is_modificarInfoMadre =
		"UPDATE "	+	"ant_ped_info_madre "			+
		"SET "		+	"edad"							+ "=?," +
						"tipo_sangre"					+ "=?," +
						"semanas_gestacion"				+ "=?," +
						"obs_patologias"				+ "=?," +
						"obs_examenes_medicamentos"		+ "=?," +
						"observaciones"					+ "=?," +
						"confiable_semanas_gestacion"	+ "=?," +
						"fpp"							+ "=?," +
						"fur"							+ "=?," +
						"info_embarazos_a"				+ "=?," +
						"info_embarazos_c"				+ "=?," +
						"info_embarazos_g"				+ "=?," +
						"info_embarazos_m"				+ "=?," +
						"info_embarazos_p"				+ "=?," +
						"info_embarazos_v"				+ "=?," +
						"metodo_semanas_gestacion"		+ "=?," +
						"primer_apellido"				+ "=?," +
						"primer_nombre"					+ "=?," +
						"segundo_apellido"				+ "=?," +
						"segundo_nombre"				+ "=?," +
						"tipo_identificacion"			+ "=?," +
						"numero_identificacion"			+ "=?, " +
						"detalle_otro_metodo"			+ "=? " +
		"WHERE "	+	"codigo_paciente"				+ "=?";

	/**
	* Cadena constante con el <i>statement</i> necesario para modificar la informacion del padre del paciente
	* en la tabla ant_ped_info_madre, que contiene la infomracion de todas madres de los pacientes que tienen
	* antecedentes pediatricos
	*/
	private static final String is_modificarInfoPadre =
		"UPDATE "	+	"ant_ped_info_padre "			+
		"SET "		+	"edad"							+ "=?," +
						"consanguinidad"				+ "=?," +
						"tipo_sangre"					+ "=?," +
						"primer_apellido"				+ "=?," +
						"primer_nombre"					+ "=?," +
						"segundo_apellido"				+ "=?," +
						"segundo_nombre"				+ "=?," +
						"tipo_identificacion"			+ "=?," +
						"numero_identificacion"			+ "=? " +
		"WHERE "	+	"codigo_paciente"				+ "=?";


	
	@SuppressWarnings("deprecation")
	private  static boolean modificarAntTiposParto(
		Connection		con,
		PersonaBasica	paciente,
		ArrayList tiposParto
	)throws SQLException
	{
		boolean				lb_accionExitosa=true;
		InfoDatosBD			lidbd_tipoParto;
		PreparedStatement	lps_insertar=null;
		PreparedStatement  lps_modificar=null;
		
		try{
			lps_insertar 		=  con.prepareStatement(is_insertarTipoParto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			lps_modificar		=  con.prepareStatement(is_modificarTipoParto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	
			lps_insertar.setInt(1, paciente.getCodigoPersona() );
			lps_modificar.setInt(2, paciente.getCodigoPersona() );
	
			if(tiposParto == null)
				return true;
	
			for(int i = 0; i < tiposParto.size(); i++)
			{
				lidbd_tipoParto = (InfoDatosBD)tiposParto.get(i);
	
				/* Si esta en la base de datos se actualiza */
				if(lidbd_tipoParto.getEstaEnBD() == 't')
				{
	
					lps_modificar.setInt(3, lidbd_tipoParto.getCodigo() );
					lps_modificar.setString(1, lidbd_tipoParto.getDescripcion() );
	
					if(lps_modificar.executeUpdate() == 0){
						lb_accionExitosa = false;
					}
				}
				/* En caso contrario no esta en la base de datos se debe insertar */
				else
				{
					lps_insertar.setInt(2, lidbd_tipoParto.getCodigo() );
					lps_insertar.setString(3, lidbd_tipoParto.getDescripcion() );
	
					if(lps_insertar.executeUpdate() == 0){
						lb_accionExitosa = false;
					}
				}
			}
	
			/*
				Una vez insertado se puede devolver en la pagina y pretender modificar algo por eso es
				necesario que el objeto sepa que ya fue insertado a la base de datos
			*/
			if(lb_accionExitosa)
			{
				for(int i = 0; i < tiposParto.size(); i++)
				{
					lidbd_tipoParto = (InfoDatosBD)tiposParto.get(i);
					lidbd_tipoParto.setEstaEnBD('t');
				}
			}
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarAntTiposParto", e);
	    }
	    finally{
			try{
				if(lps_modificar != null){
					lps_modificar.close();
				}
				if(lps_insertar != null){
					lps_insertar.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return lb_accionExitosa;
	}

	private static boolean modificarInmunizaciones(
		Connection		con,
		PersonaBasica	paciente,
		ArrayList		inmunizacionesPediatricas
	)throws SQLException
	{
		ArrayList					lasDosis;
		boolean						lb_accionExitosa=true;
		Dosis						laDosis;
		InmunizacionesPediatricas	inmunizaciones;
		PreparedStatement			lps_insertarDosis=null;
		PreparedStatement			lps_insertarObservaciones=null;
		PreparedStatement			lps_modificarDosis=null;
		PreparedStatement			lps_modificarObservaciones=null;
		try{
			/* Para ingresar */
			lps_insertarDosis			=  con.prepareStatement(is_insertarInmunizacionesDosis,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			lps_insertarObservaciones	=  con.prepareStatement(is_insertarInmunizacionesObser,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	
			/* Para modificar */
			lps_modificarObservaciones	=  con.prepareStatement(is_modificarInmunizacionesObser,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			lps_modificarDosis			=  con.prepareStatement(is_modificarInmunizacionesDosis,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	
			lps_insertarDosis.setInt(1, paciente.getCodigoPersona() );
			lps_insertarObservaciones.setInt(1, paciente.getCodigoPersona() );
			lps_modificarDosis.setInt(2, paciente.getCodigoPersona() );
			lps_modificarObservaciones.setInt(2, paciente.getCodigoPersona() );
	
			if(inmunizacionesPediatricas == null)
				return true;
	
			for(int i = 0; i < inmunizacionesPediatricas.size(); i++)
			{
				inmunizaciones = (InmunizacionesPediatricas) inmunizacionesPediatricas.get(i);
	
				/* Primero inserto su observaci�n en caso de que sea cadena no vacia o nula */
				if(
					inmunizaciones.getObservaciones() != null	&&
					!inmunizaciones.getObservaciones().equals("")
				)
				{
					/* Si est� en la bd se modifica */
					if(inmunizaciones.getEstaEnBD() == 't')
					{
						lps_modificarObservaciones.setInt(
							3,
							inmunizaciones.getCodigoInmunizacion()
						);
						lps_modificarObservaciones.setString(
							1,
							inmunizaciones.getObservaciones()
						);
	
						if(lps_modificarObservaciones.executeUpdate() == 0)
							lb_accionExitosa = false;
					}
					else
					{
						lps_insertarObservaciones.setInt(2, inmunizaciones.getCodigoInmunizacion() );
						lps_insertarObservaciones.setString(3, inmunizaciones.getObservaciones() );
	
						if(lps_insertarObservaciones.executeUpdate() == 0)
							lb_accionExitosa = false;
					}
				}
	
				/*
					Un caso que no se deber�a dar, es que no tenga comentario que ingresar ni dosis y de
					todas maneras se creo un objeto inmunizaciones pediatricas Este if tambien atrapa el
					caso en que se tenia observacion y esta no se pudo insertar en la bd
				*/
				if(inmunizaciones.getDosis() == null && !lb_accionExitosa)
					return false;
	
				if( (lasDosis = inmunizaciones.getDosis() ) != null)
				{
					for(int j = 0; j < lasDosis.size(); j++)
					{
						laDosis = (Dosis)lasDosis.get(j);
	
						/* Si no es una dosis debe ser un refuerzo */
						if(laDosis.getNumeroDosis() < 1 && !laDosis.isRefuerzo() )
							return false;
	
						/* Dada fecha o no se inserta */
						if(laDosis.getNumeroDosis() < 1)
						{
							/*
								Caso es refuerzo: ya esta en la bd se debe modificar. Este caso
								�nicamente se tiene si fue ingresado con fecha vac�a y ahora se desea
								agregar.  No se pueden modificar ninguno de los otros datos
							*/
	//OJO: valdria la pena revisar la consulta
							if(laDosis.getEstaEnBD() == 't')
							{
								/* N�mero de dosis -1 indica que es un refuerzo */
								lps_modificarDosis.setInt(3, inmunizaciones.getCodigoInmunizacion() );
								lps_modificarDosis.setInt(4, laDosis.getNumeroDosis() );
								lps_modificarDosis.setString(1, laDosis.getFecha() );
	
								if(lps_modificarDosis.executeUpdate() == 0)
									lb_accionExitosa = false;
							}
							/* Caso contrario se debe ingresar */
							else
							{
								/* N�mero de dosis -1 indica que es un refuerzo */
								lps_insertarDosis.setInt(2, inmunizaciones.getCodigoInmunizacion() );
								lps_insertarDosis.setInt(3, laDosis.getNumeroDosis() );
								lps_insertarDosis.setString(4, laDosis.getFecha() );
	
								if(lps_insertarDosis.executeUpdate() == 0)
									lb_accionExitosa = false;
							}
						}
						/* Caso es una dosis (con n�mero de dosis) */
						else
						{
							/*
								Ya esta en la bd se debe modificar. Este caso �nicamente se tiene si fue
								ingresado con fecha vacia y ahora se desea agregar No se pueden
								modificar ninguno de los otros datos
							*/
	//OJO: valdria la pena revisar la consulta
							if(laDosis.getEstaEnBD() == 't')
							{
								lps_modificarDosis.setInt(3, inmunizaciones.getCodigoInmunizacion() );
								lps_modificarDosis.setInt(4, laDosis.getNumeroDosis() );
								lps_modificarDosis.setString(1, laDosis.getFecha() );
	
								if(lps_modificarDosis.executeUpdate() == 0)
									lb_accionExitosa = false;
							}
							/* Caso contrario se debe modificar */
							else
							{
								lps_insertarDosis.setInt(2, inmunizaciones.getCodigoInmunizacion() );
								lps_insertarDosis.setInt(3, laDosis.getNumeroDosis() );
								lps_insertarDosis.setString(4, laDosis.getFecha() );
	
								if(lps_insertarDosis.executeUpdate() == 0)
									lb_accionExitosa = false;
							}
						}
					}
				}
			}
	
			/*
				Si todas las modificaciones y/o inserciones salieron bien, debe poner el estado de las
				inmunizaciones en EstaEnBD= true
			*/
			if(lb_accionExitosa)
			{
				for(int i = 0; i < inmunizacionesPediatricas.size(); i++)
				{
					inmunizaciones = (InmunizacionesPediatricas)inmunizacionesPediatricas.get(i);
					inmunizaciones.setEstaEnBD('t');
	
					if(inmunizaciones.getDosis() != null)
					{
						lasDosis = inmunizaciones.getDosis();
	
						for(int j = 0; j < lasDosis.size(); j++)
						{
							laDosis = (Dosis)lasDosis.get(j);
							laDosis.setEstaEnBD('t');
						}
					}
				}
			}
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarAntTiposParto", e);
	    }
	    finally{
			try{
				if(lps_insertarDosis != null){
					lps_insertarDosis.close();
				}
				if(lps_insertarObservaciones != null){
					lps_insertarObservaciones.close();
				}
				if(lps_modificarDosis != null){
					lps_modificarDosis.close();
				}
				if(lps_modificarObservaciones != null){
					lps_modificarObservaciones.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return lb_accionExitosa;
	}

	/**
	* Este m�todo implementa cargarAntecedentePediatrico para PostgresSQL o Hsqldb
	* @see com.princetonsa.dao.AntecedentePediatricoDao#cargarAntecedentePediatrico()
	*/
	public static ResultSetDecorator cargarAntecedentePediatrico(Connection	ac_con,	int ai_codigoPaciente, DaoFactory myFactory )throws SQLException
	{
		try
		{
			PreparedStatementDecorator lps_ps;

			if(ac_con == null || ac_con.isClosed() )
				ac_con = myFactory.getConnection();
	
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultarAntecedentePediatrico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_ps.setInt(1, ai_codigoPaciente);

			return new ResultSetDecorator(lps_ps.executeQuery());
		}
		catch(Exception le_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(le_e);
			return null;
		}
	}

	public static String consultarNombrePresentacionNacimiento(Connection con, int codigo, DaoFactory myFactory)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		String result="";
		try
		{
			if(con == null || con.isClosed() ){
				con = myFactory.getConnection();
			}
			pst = con.prepareStatement(is_consultarNombrePresentacionNacimiento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigo);
			rs=pst.executeQuery();
			if( rs.next() )
				result=rs.getString("nombre");
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarNombrePresentacionNacimiento", e);
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

	public static String consultarNombreUbicacionFeto(Connection con, int codigo, DaoFactory myFactory)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		String result="";
		try
		{
			if(con == null || con.isClosed() ){
				con = myFactory.getConnection();
			}
			pst = con.prepareStatement(is_consultarNombreUbicacionFeto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigo);
			rs=pst.executeQuery();
			if( rs.next() )
				result= rs.getString("nombre");
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarNombreUbicacionFeto", e);
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

	public static String consultarObservacionInmunizacion(
		Connection	ac_con,
		int			ai_codigoPaciente,
		int			ai_codigo,
		DaoFactory myFactory
	)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		String result="";
		try
		{
			if(ac_con == null || ac_con.isClosed() ){
				ac_con = myFactory.getConnection();
			}
			pst = ac_con.prepareStatement(is_consultarInmunizacionObservacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, ai_codigoPaciente);
			pst.setInt(2, ai_codigo);
			rs= pst.executeQuery();
			if( rs.next() )
				result=rs.getString("observacion");
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarObservacionInmunizacion", e);
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
	
	public static String consultarNombreTipoTrabajoParto(Connection ac_con, int ai_codigo, DaoFactory myFactory)
		{
		PreparedStatement pst=null;
		ResultSet rs=null;
		String result="";
			try
			{
				if(ac_con == null || ac_con.isClosed() ){
					ac_con = myFactory.getConnection();
				}
				pst = ac_con.prepareStatement(is_consultarNombreTipoTrabajoParto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, ai_codigo);
				rs=pst.executeQuery();
				
				if(rs.next() )
					result= rs.getString("nombre");
			}
			catch(Exception e)
		    {
			    Log4JManager.error("ERROR consultarNombreTipoTrabajoParto", e);
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

	/** Este m�todo implementa consultarNombreRiesgoApgar para PostgresSQL o Hsqldb */
	public static String consultarNombreRiesgoApgar(Connection ac_con, int ai_codigo,  DaoFactory myFactory)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		String result="";
		try
		{
			if(ac_con == null || ac_con.isClosed() ){
				ac_con = myFactory.getConnection();
			}

			pst = ac_con.prepareStatement(is_consultarNombreRiesgoApgar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, ai_codigo);

			rs = pst.executeQuery();

			if(rs.next() ){
				result=rs.getString("nombre");
			}

		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR consultarNombreRiesgoApgar", e);
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

	/** Este m�todo implementa consultarAntPediatricoInmunizacionesDosis para PostgresSQL o Hsqldb */
	public static ResultSetDecorator consultarAntPediatricoInmunizacionesDosis(
		Connection	ac_con,
		int			ai_codigoPaciente,
	   	DaoFactory myFactory
	)
	{
		try
		{
			PreparedStatementDecorator	lps_ps;

			if(ac_con == null || ac_con.isClosed() )
				ac_con = myFactory.getConnection();

			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultarInmunizacionesDosis,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_ps.setInt(1, ai_codigoPaciente);

			return new ResultSetDecorator(lps_ps.executeQuery());
		}
		catch(Exception le_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(le_e);
			return null;
		}
	}

	/** Este m�todo implementa consultarAntPediatricoInmunizacionesObser para PostgresSQL o Hsqldb */
	public static ResultSetDecorator consultarAntPediatricoInmunizacionesObser(
		Connection	ac_con,
		int			ai_codigoPaciente,
		DaoFactory myFactory
	)
	{
		try
		{
			PreparedStatementDecorator	lps_ps;

			if(ac_con == null || ac_con.isClosed() )
				ac_con = myFactory.getConnection();

			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultarInmunizacionesObservacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_ps.setInt(1, ai_codigoPaciente);

			return new ResultSetDecorator(lps_ps.executeQuery());
		}
		catch(Exception le_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(le_e);
			return null;
		}
	}

	/** Este m�todo implementa consultarAntPediatricoTiposParto para PostgresSQL o Hsqldb */
	public static ResultSetDecorator consultarAntPediatricoTiposParto(Connection ac_con, int ai_codigoPaciente, DaoFactory myFactory)
	{
		try
		{
			PreparedStatementDecorator	lps_ps;

			if(ac_con == null || ac_con.isClosed() )
				ac_con = myFactory.getConnection();

			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultarTiposParto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_ps.setInt(1, ai_codigoPaciente);

			return new ResultSetDecorator(lps_ps.executeQuery());
		}
		catch(Exception le_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(le_e);
			return null;
		}
	}

	/** Obtiene todas las opciones de embarazo de un paciente */
	public static ResultSetDecorator consultarEmbarazoOpciones(Connection ac_con, int ai_codigoPaciente, DaoFactory myFactory)
	{
		try
		{
			PreparedStatementDecorator lps_ps;

			if(ac_con == null || ac_con.isClosed() )
				ac_con = myFactory.getConnection();

			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultarEmbarazoOpciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_ps.setInt(1, ai_codigoPaciente);

			return new ResultSetDecorator(lps_ps.executeQuery());
		}
		catch(Exception le_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(le_e);
			return null;
		}
	}

	/** Obtiene la informaci�n de la madre */
	public static  ResultSetDecorator consultarInfoMadre(
		Connection	con,
		int			ai_codigoPaciente,
		DaoFactory myFactory
	)
	{
		try
		{
			PreparedStatementDecorator lps_ps;

			if(con == null || con.isClosed() )
				con = myFactory.getConnection();

			lps_ps =  new PreparedStatementDecorator(con.prepareStatement(is_consultarInfoMadre,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_ps.setInt(1, ai_codigoPaciente);

			return new ResultSetDecorator(lps_ps.executeQuery());
		}
		catch(Exception le_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(le_e);
			return null;
		}
	}

	/** Obtiene la informaci�n del padre */
	public static ResultSetDecorator consultarInfoPadre(Connection ac_con, int ai_codigoPaciente, DaoFactory myFactory)
	{
		PreparedStatementDecorator lps_ps;

		try
		{
			if(ac_con == null || ac_con.isClosed() )
				ac_con = myFactory.getConnection();

			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_consultarInfoPadre,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			lps_ps.setInt(1, ai_codigoPaciente);

			return new ResultSetDecorator(lps_ps.executeQuery());
		}
		catch(Exception le_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(le_e);
			return null;
		}
	}
	/**
	* Este m�todo implementa insertarAntecedentePediatrico para PostgresSQL o Hsqldb
	* @see com.princetonsa.dao.AntecedentePediatricoDao#insertarAntecedentePediatrico()
	*/
	public static int insertarAntecedentePediatrico
	(
		Connection		con,
		PersonaBasica	paciente,
		int				presentacionNacimiento,
		String			otraPresentacionNacimiento,
		int				ubicacionFeto,
		String			otraUbicacionFeto,
		int				duracionPartoMins,
		int				duracionPartoHoras,
		int				rupturaMembranasMins,
		int				rupturaMembranasHoras,
		String			caracteristicasLiqAmniotico,
		String			sufrimientoFetal,
		String			causasSufrimientoFetal,
		String			anestesiaMedicamentos,
		int				tipoTrabajoParto,
		String			comentarioTipoTrabajoParto,
		String			otroTipoParto,
		String			motivoTipoPartoOtro,
		String			tiempoNacimiento,
		String			caracteristicasPlacenta,
		int				talla,
		int				peso,
		float			perimetroCefalico,
		float			perimetroToracico,
		int				apgarMinuto1,
		int				apgarMinuto5,
		int				apgarMinuto10,
		int				riesgoApgarMinuto1,
		int				riesgoApgarMinuto5,
		int				riesgoApgarMinuto10,
		String			as_embarazosAnterioresOtros,
		String			as_incompatibilidadABO,
		String			as_incompatibilidadRH,
		String			as_macrosomicos,
		String			as_malformacionesCongenitas,
		String			as_mortinatos,
		String			as_muertesFetalesTempranas,
		String			as_prematuros,
		int				ai_codigoSerologia,
		String			as_descripcionSerologia,
		int				ai_codigoTestSullivan,
		String			as_descripcionTestSullivan,
		String			as_controlPrenatal,
		String			as_frecuenciaControlPrenatal,
		String			as_lugarControlPrenatal,
		int				ai_duracionExpulsivoHoras,
		int				ai_duracionExpulsivoMinutos,
		String			as_amnionitis,
		String			as_amnionitisFactorAnormal,
		String			as_anestesia,
		String			as_anestesiaTipo,
		String			as_nst,
		String			as_nstDescripcion,
		String			as_otrosExamenesTrabajoParto,
		String			as_perfilBiofisico,
		String			as_perfilBiofisicoDescripcion,
		String			as_ptc,
		String			as_ptcDescripcion,
		String			as_complicacionesParto,
		String			as_muestraCordonUmbilical,
		String			as_muestraCordonUmbilicalDescripcion,
		String			as_cordonUmbilicalCaracteristicas,
		String			as_cordonUmbilicalDescripcion,
		String			as_gemelo,
		String			as_gemeloDescripcion,
		String			as_intrauterinoPeg,
		String			as_intrauterinoPegCausa,
		int				ai_intrauterinoAnormalidad,
		String			as_intrauterinoAnormalidadCausa,
		String			as_intrauterinoArmonico,
		String			as_intrauterinoArmonicoCausa,
		String			as_reanimacion,
		String			as_reanimacionAspiracion,
		String			as_reanimacionMedicamentos,
		String			as_sano,
		String			as_sanoDescripcion,
		String			as_sexoDescripcion,
		int				ai_edadGestacional,
		String			as_edadGestacionalDescripcion,
		String			as_liqAmnioticoClaro,
		String			as_liqAmnioticoMeconiado,
		String			as_liqAmnioticoMeconiadoGrado,
		String			as_liqAmnioticoSanguinolento,
		String			as_liqAmnioticoFetido,
		String			as_placentaCaracteristicas,
		String			as_placentaCaracteristicasDescripcion,
		String			observaciones,
		String			loginUsuario,
		String			fecha,
		ArrayList		tiposParto,
		ArrayList		inmunizacionesPediatricas,
		InfoMadre		infoMadre,
		InfoPadre		aip_infoPadre,
		ArrayList		aal_embarazoOpciones,
		DaoFactory myFactory
	)throws SQLException
	{
		boolean				lb_insertoAntecedente;
		boolean				insertoAntTiposParto;
		boolean				insertoInmunizaciones;
		boolean				lb_insertoOpcionesEmbarazo;
		boolean				resp0;
		boolean				resp1;
		boolean				resp2;
		boolean				sufrimientoFetal_boolean;
		int					returnedVal;
		PreparedStatementDecorator	lps_consultarAntecedente;
		PreparedStatementDecorator	lps_insertarAntecedente;
		PreparedStatementDecorator	lps_insertarAntecedentePediatrico;

		insertoAntTiposParto = insertoInmunizaciones = false;
		lb_insertoOpcionesEmbarazo = sufrimientoFetal_boolean = false;
		resp0 = resp1 = resp2 = false;

		lb_insertoAntecedente	= true;
		returnedVal				= 0;

		/* Iniciamos la transacci�n */
		resp0 = myFactory.beginTransaction(con);

		/* Consultar si el paciente ya tiene un antecedente registrado */
		lps_consultarAntecedente =  new PreparedStatementDecorator(con.prepareStatement(is_consultarAntecedente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/*
			Insertar el paciente en el sistema para notificar que ya tiene un antecedente en el
			sistema
		*/
		lps_insertarAntecedente =  new PreparedStatementDecorator(con.prepareStatement(is_insertarAntecedente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Inserci�n de datos de antecedentes pediatricos */
		lps_insertarAntecedentePediatrico =  new PreparedStatementDecorator(con.prepareStatement(is_insertarAntecedentePediatrico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		lps_consultarAntecedente.setInt(1, paciente.getCodigoPersona() );
		lps_insertarAntecedentePediatrico.setInt(1, paciente.getCodigoPersona() );

		if (!existeAntecedentePaciente(con,paciente.getCodigoPersona()))
		{
			PreparedStatementDecorator insertarAntecedentePacienteStatement= new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarAntecedentePacienteStatement.setInt(1, paciente.getCodigoPersona());
			insertarAntecedentePacienteStatement.executeUpdate();
		}


		/*
			presentacionNacimiento = -1, indica que no se inicializo este campo
			presentacionNacimiento = 0, indica que se inicializ� en el tipo 'Otro'
		*/
		if(presentacionNacimiento != -1 && presentacionNacimiento != 0)
		{
			lps_insertarAntecedentePediatrico.setInt(2, presentacionNacimiento);
			lps_insertarAntecedentePediatrico.setString(3, null);
		}
		else
		{
			lps_insertarAntecedentePediatrico.setString(2, null);

			/*
				Si es 'Otra' presentacion nacimiento, debe tener algo en el campo
				otraPresentacionNacimiento
			*/
			if(presentacionNacimiento == 0)
				lps_insertarAntecedentePediatrico.setString(3, otraPresentacionNacimiento);
			else
				lps_insertarAntecedentePediatrico.setString(3, null);
		}

		/*
			ubicacionFeto = -1, indica que no se inicializo este campo
			ubicacionFeto = 0, indica que se inicializ� en el tipo 'Otro'
		*/
		if(ubicacionFeto != -1 && ubicacionFeto != 0)
		{
			lps_insertarAntecedentePediatrico.setInt(4, ubicacionFeto);
			lps_insertarAntecedentePediatrico.setString(5, null);
		}
		else
		{
			lps_insertarAntecedentePediatrico.setString(4, null);

			/* Si es 'Otra' ubicacionFeto, debe tener algo en el campo otraUbicacionFeto */
			if(ubicacionFeto == 0)
				lps_insertarAntecedentePediatrico.setString(5, otraUbicacionFeto);
			else
				lps_insertarAntecedentePediatrico.setString(5, null);
		}

		/* Campos enteros que tengan el valor -1, indican que no se inicializo este campo */
		if(duracionPartoMins != -1)
			lps_insertarAntecedentePediatrico.setInt(6, duracionPartoMins);
		else
			lps_insertarAntecedentePediatrico.setString(6, null);

		if(duracionPartoHoras != -1)
			lps_insertarAntecedentePediatrico.setInt(7, duracionPartoHoras);
		else
			lps_insertarAntecedentePediatrico.setString(7, null);

		if(rupturaMembranasMins != -1)
			lps_insertarAntecedentePediatrico.setInt(8, rupturaMembranasMins);
		else
			lps_insertarAntecedentePediatrico.setString(8, null);

		if(rupturaMembranasHoras != -1)
			lps_insertarAntecedentePediatrico.setInt(9, rupturaMembranasHoras);
		else
			lps_insertarAntecedentePediatrico.setString(9, null);

		lps_insertarAntecedentePediatrico.setString(10, caracteristicasLiqAmniotico);

		/* Si se inicializo el campo de sufrimiento fetal */
		if(sufrimientoFetal.equalsIgnoreCase("true") || sufrimientoFetal.equalsIgnoreCase("false") )
		{
			if(sufrimientoFetal.equalsIgnoreCase("true") )
				sufrimientoFetal_boolean = true;
			else if(sufrimientoFetal.equalsIgnoreCase("false") )
			{
				sufrimientoFetal_boolean	= false;
				causasSufrimientoFetal		= null;
			}

			lps_insertarAntecedentePediatrico.setBoolean(11, sufrimientoFetal_boolean);
			lps_insertarAntecedentePediatrico.setString(12, causasSufrimientoFetal);
		}
		else
		{
			/*
				Si no se inicializo el campo de sufrimiento fetal no se tiene en cuenta las causas
				del sufrimiento fetal. O tiene cualquier valor diferente a yes o no
			*/
			lps_insertarAntecedentePediatrico.setString(11, null);
			lps_insertarAntecedentePediatrico.setString(12, null);
		}

		lps_insertarAntecedentePediatrico.setString(13, anestesiaMedicamentos);

		/*
			Insercion del tipo de trabajo de parto. Caso no se selecciono ningun trabajo
			de parto, tampoco se tiene en cuenta el comentario
		*/
		if(tipoTrabajoParto == -1)
		{
			lps_insertarAntecedentePediatrico.setString(14, null);
			lps_insertarAntecedentePediatrico.setString(15, null);
		}
		else
		{
			/* De lo contrario debe venir un tipoTrabajoParto v�lido */
			lps_insertarAntecedentePediatrico.setInt(14, tipoTrabajoParto);
			lps_insertarAntecedentePediatrico.setString(15, comentarioTipoTrabajoParto);
		}

		/*
			Campos de texto no llenos pueden estar como cadenas vacias e indican que no se
			inicializ� este campo en la bd inserto null en caso de ser FK a otras tablas. Si eligio
			otro tipo parto debe especificar el campo otroTipoParto que corresponde a la pregunta
			"cu�l?". Sino especifico nada en otroTIpoParto no se inserta el motivoTIpoPartoOtro
		*/
		if( (otroTipoParto == null) || (otroTipoParto != null && otroTipoParto.equals("") ) )
		{
			otroTipoParto		= null;
			motivoTipoPartoOtro	= null;
		}

		lps_insertarAntecedentePediatrico.setString(16, otroTipoParto);
		lps_insertarAntecedentePediatrico.setString(17, motivoTipoPartoOtro);

		if(tiempoNacimiento != null && tiempoNacimiento.equals("") )
			lps_insertarAntecedentePediatrico.setString(18, null);
		else
			lps_insertarAntecedentePediatrico.setString(18, tiempoNacimiento);

		lps_insertarAntecedentePediatrico.setString(19, caracteristicasPlacenta);

		if(talla != -1)
			lps_insertarAntecedentePediatrico.setInt(20, talla);
		else
			lps_insertarAntecedentePediatrico.setString(20, null);

		if(peso != -1)
			lps_insertarAntecedentePediatrico.setInt(21, peso);
		else
			lps_insertarAntecedentePediatrico.setString(21, null);

		if(perimetroCefalico != -1)
			lps_insertarAntecedentePediatrico.setFloat(22, perimetroCefalico);
		else
			lps_insertarAntecedentePediatrico.setString(22, null);

		if(perimetroToracico != -1)
			lps_insertarAntecedentePediatrico.setFloat(23, perimetroToracico);
		else
			lps_insertarAntecedentePediatrico.setString(23, null);

		/* Si se especifico un apgar se debe obligatoriamente especificar el riesgo */
		if(apgarMinuto1 != -1 && riesgoApgarMinuto1 != -1)
		{
			lps_insertarAntecedentePediatrico.setInt(24, apgarMinuto1);
			lps_insertarAntecedentePediatrico.setInt(27, riesgoApgarMinuto1);
		}
		else
		{
			lps_insertarAntecedentePediatrico.setString(24, null);
			lps_insertarAntecedentePediatrico.setString(27, null);
		}

		if(apgarMinuto5 != -1 && riesgoApgarMinuto5 != -1)
		{
			lps_insertarAntecedentePediatrico.setInt(25, apgarMinuto5);
			lps_insertarAntecedentePediatrico.setInt(28, riesgoApgarMinuto5);
		}
		else
		{
			lps_insertarAntecedentePediatrico.setString(25, null);
			lps_insertarAntecedentePediatrico.setString(28, null);
		}

		if(apgarMinuto10 != -1 && riesgoApgarMinuto10 != -1)
		{
			lps_insertarAntecedentePediatrico.setInt(26, apgarMinuto10);
			lps_insertarAntecedentePediatrico.setInt(29, riesgoApgarMinuto10);
		}
		else
		{
			lps_insertarAntecedentePediatrico.setString(26, null);
			lps_insertarAntecedentePediatrico.setString(29, null);
		}

		lps_insertarAntecedentePediatrico.setString(30, as_embarazosAnterioresOtros);
		lps_insertarAntecedentePediatrico.setString(31, as_incompatibilidadABO);
		lps_insertarAntecedentePediatrico.setString(32, as_incompatibilidadRH);
		lps_insertarAntecedentePediatrico.setString(33, as_macrosomicos);
		lps_insertarAntecedentePediatrico.setString(34, as_malformacionesCongenitas);
		lps_insertarAntecedentePediatrico.setString(35, as_mortinatos);
		lps_insertarAntecedentePediatrico.setString(36, as_muertesFetalesTempranas);
		lps_insertarAntecedentePediatrico.setString(37, as_prematuros);

		lps_insertarAntecedentePediatrico.setInt(38, ai_codigoSerologia);
		lps_insertarAntecedentePediatrico.setString(39, as_descripcionSerologia);
		lps_insertarAntecedentePediatrico.setInt(40, ai_codigoTestSullivan);
		lps_insertarAntecedentePediatrico.setString(41, as_descripcionTestSullivan);
		lps_insertarAntecedentePediatrico.setString(42, as_controlPrenatal);
		lps_insertarAntecedentePediatrico.setString(43, as_frecuenciaControlPrenatal);
		lps_insertarAntecedentePediatrico.setString(44, as_lugarControlPrenatal);

		lps_insertarAntecedentePediatrico.setInt(45, ai_duracionExpulsivoHoras);
		lps_insertarAntecedentePediatrico.setInt(46, ai_duracionExpulsivoMinutos);
		lps_insertarAntecedentePediatrico.setString(47, as_amnionitis);
		lps_insertarAntecedentePediatrico.setString(48, as_amnionitisFactorAnormal);
		lps_insertarAntecedentePediatrico.setString(49, as_anestesia);
		lps_insertarAntecedentePediatrico.setString(50, as_anestesiaTipo);
		lps_insertarAntecedentePediatrico.setString(51, as_nst);
		lps_insertarAntecedentePediatrico.setString(52, as_nstDescripcion);
		lps_insertarAntecedentePediatrico.setString(53, as_otrosExamenesTrabajoParto);
		lps_insertarAntecedentePediatrico.setString(54, as_perfilBiofisico);
		lps_insertarAntecedentePediatrico.setString(55, as_perfilBiofisicoDescripcion);
		lps_insertarAntecedentePediatrico.setString(56, as_ptc);
		lps_insertarAntecedentePediatrico.setString(57, as_ptcDescripcion);

		lps_insertarAntecedentePediatrico.setString(58, as_complicacionesParto);
		lps_insertarAntecedentePediatrico.setString(59, as_muestraCordonUmbilical);
		lps_insertarAntecedentePediatrico.setString(60, as_muestraCordonUmbilicalDescripcion);
		lps_insertarAntecedentePediatrico.setString(61, as_cordonUmbilicalCaracteristicas);
		lps_insertarAntecedentePediatrico.setString(62, as_cordonUmbilicalDescripcion);
		lps_insertarAntecedentePediatrico.setString(63, as_gemelo);
		lps_insertarAntecedentePediatrico.setString(64, as_gemeloDescripcion);
		lps_insertarAntecedentePediatrico.setString(65, as_intrauterinoPeg);
		lps_insertarAntecedentePediatrico.setString(66, as_intrauterinoPegCausa);
		lps_insertarAntecedentePediatrico.setInt(67, ai_intrauterinoAnormalidad);
		lps_insertarAntecedentePediatrico.setString(68, as_intrauterinoAnormalidadCausa);
		lps_insertarAntecedentePediatrico.setString(69, as_intrauterinoArmonico);
		lps_insertarAntecedentePediatrico.setString(70, as_intrauterinoArmonicoCausa);
		lps_insertarAntecedentePediatrico.setString(71, as_reanimacion);
		lps_insertarAntecedentePediatrico.setString(72, as_reanimacionAspiracion);
		lps_insertarAntecedentePediatrico.setString(73, as_reanimacionMedicamentos);
		lps_insertarAntecedentePediatrico.setString(74, as_sano);
		lps_insertarAntecedentePediatrico.setString(75, as_sanoDescripcion);
		lps_insertarAntecedentePediatrico.setString(76, as_sexoDescripcion);

		lps_insertarAntecedentePediatrico.setInt(77, ai_edadGestacional);
		lps_insertarAntecedentePediatrico.setString(78, as_edadGestacionalDescripcion);
		lps_insertarAntecedentePediatrico.setString(79, as_liqAmnioticoClaro);
		lps_insertarAntecedentePediatrico.setString(80, as_liqAmnioticoMeconiado);
		lps_insertarAntecedentePediatrico.setString(81, as_liqAmnioticoMeconiadoGrado);
		lps_insertarAntecedentePediatrico.setString(82, as_liqAmnioticoSanguinolento);
		lps_insertarAntecedentePediatrico.setString(83, as_liqAmnioticoFetido);
		lps_insertarAntecedentePediatrico.setString(84, as_placentaCaracteristicas);
		lps_insertarAntecedentePediatrico.setString(85, as_placentaCaracteristicasDescripcion);

		lps_insertarAntecedentePediatrico.setString(86, observaciones);
		lps_insertarAntecedentePediatrico.setString(87, loginUsuario);
		lps_insertarAntecedentePediatrico.setString(88, fecha);

		try
		{
			/*
				Verificar si el paciente ya tiene antecedente en el sistema. Si no tiene antecedente
				se inserta primero
			*/
			ResultSetDecorator rs=new ResultSetDecorator(lps_consultarAntecedente.executeQuery());
			if(!rs.next())
			{
				lps_insertarAntecedente.setInt(1, paciente.getCodigoPersona() );
				lb_insertoAntecedente = (lps_insertarAntecedente.executeUpdate() == 1);
				lps_insertarAntecedente.close();
			}
			rs.close();
			lps_consultarAntecedente.close();	
			
			if(lb_insertoAntecedente)
			{
				returnedVal = lps_insertarAntecedentePediatrico.executeUpdate();

				/* Insertando los tipos de Parto */
				insertoAntTiposParto = modificarAntTiposParto(con, paciente, tiposParto);

				/* Insertando las inmunizaciones */
				insertoInmunizaciones =
					modificarInmunizaciones(con, paciente, inmunizacionesPediatricas);

				/* Insertando las opciones de embarazo */
				lb_insertoOpcionesEmbarazo =
					modificarEmbarazoOpciones(con, paciente, aal_embarazoOpciones);
			}

			/* Insertando la informacion de la madre y el padre */
			resp2 = (infoMadre.insertar(con) > 0) && (aip_infoPadre.insertar(con) > 0);
		}
		catch(SQLException sql_e)
		{
			final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
			il_logger.warn(sql_e);
			myFactory.abortTransaction(con);
			sql_e.printStackTrace();
			throw sql_e;
		}

		resp1 = (returnedVal > 0);
		lps_insertarAntecedentePediatrico.close();

		/* Terminamos la transacci�n, bien sea con un commit o un rollback */
		if(
			resp0 && resp1 && resp2	&&
			insertoAntTiposParto	&&
			insertoInmunizaciones	&&
			lb_insertoOpcionesEmbarazo
		)
			myFactory.endTransaction(con);
		else
			myFactory.abortTransaction(con);

		return returnedVal;
	}

	/** Inserta un registro de informaci�n de la madre */
	public static  int insertarInfoMadre
	(
		Connection	con,
		int			ai_codigoPaciente,
		int			edad,
		int			tipoSangre,
		float		semanasGestacion,
		String		obsPatologias,
		String		obsExamenesMedicamentos,
		String		observaciones,
		int			ai_metodoSemanasGestacion,
		String		as_confiableSemanasGestacion,
		String		as_fpp,
		String		as_fur,
		String		as_infoEmbarazoA,
		String		as_infoEmbarazoC,
		String		as_infoEmbarazoG,
		String		as_infoEmbarazoM,
		String		as_infoEmbarazoP,
		String		as_infoEmbarazoV,
		String		as_primerApellido,
		String		as_primerNombre,
		String		as_segundoApellido,
		String		as_segundoNombre,
		String		as_tipoIdentificacion,
		String		as_numeroIdentificacion,
		DaoFactory myFactory,
		String 		as_otroMetodo
	)throws SQLException
	{
		PreparedStatement pst=null;
		int resp=ConstantesBD.codigoNuncaValido;
		try{
			if(con == null || con.isClosed() ){
				con =myFactory.getConnection();
			}
	
			pst =  con.prepareStatement(is_insertarInfoMadre,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, ai_codigoPaciente);
			pst.setInt(2, edad);
			pst.setInt(17, ai_metodoSemanasGestacion);
	
			if(tipoSangre > -1)
				pst.setInt(3, tipoSangre);
			else
				pst.setNull(3, Types.INTEGER);
	
			pst.setFloat(4, semanasGestacion);
	
			pst.setString(5, obsPatologias);
			pst.setString(6, obsExamenesMedicamentos);
			pst.setString(7, observaciones);
			pst.setString(8, as_confiableSemanasGestacion);
	
			if(as_fpp == null || as_fpp.equals("") )
				pst.setNull(9, Types.DATE);
			else
				pst.setString(9, UtilidadFecha.conversionFormatoFechaABD(as_fpp) );
	
			if(as_fur == null || as_fur.equals("") )
				pst.setNull(10, Types.DATE);
			else
				pst.setString(10, UtilidadFecha.conversionFormatoFechaABD(as_fur) );
	
			pst.setString(11, as_infoEmbarazoA);
			pst.setString(12, as_infoEmbarazoC);
			pst.setString(13, as_infoEmbarazoG);
			pst.setString(14, as_infoEmbarazoM);
			pst.setString(15, as_infoEmbarazoP);
			pst.setString(16, as_infoEmbarazoV);
			pst.setString(18, as_primerApellido);
			pst.setString(19, as_primerNombre);
			pst.setString(20, as_segundoApellido);
			pst.setString(21, as_segundoNombre);
			pst.setString(22, as_tipoIdentificacion);
			pst.setString(23, as_numeroIdentificacion);
			pst.setString(24, as_otroMetodo);
			resp=pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR insertarInfoMadre", e);
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}

	/** Inserta un registro de informaci�n del padre */
	public static int insertarInfoPadre
	(
		Connection	ac_con,
		int			ai_codigoPaciente,
		int			ai_edad,
		int			ai_tipoSangre,
		String		as_consanguinidad,
		String		as_primerApellido,
		String		as_primerNombre,
		String		as_segundoApellido,
		String		as_segundoNombre,
		String		as_tipoIdentificacion,
		String		as_numeroIdentificacion,
		DaoFactory myFactory
	)throws SQLException
	{
		PreparedStatement pst =null;
		int resp=ConstantesBD.codigoNuncaValido;

		try{
			if(ac_con == null || ac_con.isClosed() ){
				ac_con = myFactory.getConnection();
			}
	
			pst = new PreparedStatementDecorator(ac_con.prepareStatement(is_insertarInfoPadre,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, ai_codigoPaciente);
			pst.setInt(2, ai_edad);
	
			if(ai_tipoSangre > -1)
				pst.setInt(4, ai_tipoSangre);
			else
				pst.setNull(4, Types.INTEGER);
	
			pst.setString(3, as_consanguinidad);
			pst.setString(5, as_primerApellido);
			pst.setString(6, as_primerNombre);
			pst.setString(7, as_segundoApellido);
			pst.setString(8, as_segundoNombre);
			pst.setString(9, as_tipoIdentificacion);
			pst.setString(10, as_numeroIdentificacion);
	
			resp=pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR insertarInfoPadre", e);
	    }
	    finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}

	/**
	* Este m�todo implementa modificarAntecedentePediatrico para PostgresSQL o Hsqldb
	* @see com.princetonsa.dao.AntecedentePediatricoDao#modificarAntecedentePediatrico()
	*/
	public static  int modificarAntecedentePediatrico
	(
		Connection		con,
		PersonaBasica	paciente,
		int				presentacionNacimiento,
		String			otraPresentacionNacimiento,
		int				ubicacionFeto,
		String			otraUbicacionFeto,
		int				duracionPartoMins,
		int				duracionPartoHoras,
		int				rupturaMembranasMins,
		int				rupturaMembranasHoras,
		String			caracteristicasLiqAmniotico,
		String			sufrimientoFetal,
		String			causasSufrimientoFetal,
		String			anestesiaMedicamentos,
		int				tipoTrabajoParto,
		String			comentarioTipoTrabajoParto,
		String			otroTipoParto,
		String			motivoTipoPartoOtro,
		String			tiempoNacimiento,
		String			caracteristicasPlacenta,
		int				talla,
		int				peso,
		float			perimetroCefalico,
		float			perimetroToracico,
		int				apgarMinuto1,
		int				apgarMinuto5,
		int				apgarMinuto10,
		int				riesgoApgarMinuto1,
		int				riesgoApgarMinuto5,
		int				riesgoApgarMinuto10,
		String			as_embarazosAnterioresOtros,
		String			as_incompatibilidadABO,
		String			as_incompatibilidadRH,
		String			as_macrosomicos,
		String			as_malformacionesCongenitas,
		String			as_mortinatos,
		String			as_muertesFetalesTempranas,
		String			as_prematuros,
		int				ai_codigoSerologia,
		String			as_descripcionSerologia,
		int				ai_codigoTestSullivan,
		String			as_descripcionTestSullivan,
		String			as_controlPrenatal,
		String			as_frecuenciaControlPrenatal,
		String			as_lugarControlPrenatal,
		int				ai_duracionExpulsivoHoras,
		int				ai_duracionExpulsivoMinutos,
		String			as_amnionitis,
		String			as_amnionitisFactorAnormal,
		String			as_anestesia,
		String			as_anestesiaTipo,
		String			as_nst,
		String			as_nstDescripcion,
		String			as_otrosExamenesTrabajoParto,
		String			as_perfilBiofisico,
		String			as_perfilBiofisicoDescripcion,
		String			as_ptc,
		String			as_ptcDescripcion,
		String			as_complicacionesParto,
		String			as_muestraCordonUmbilical,
		String			as_muestraCordonUmbilicalDescripcion,
		String			as_cordonUmbilicalCaracteristicas,
		String			as_cordonUmbilicalDescripcion,
		String			as_gemelo,
		String			as_gemeloDescripcion,
		String			as_intrauterinoPeg,
		String			as_intrauterinoPegCausa,
		int				ai_intrauterinoAnormalidad,
		String			as_intrauterinoAnormalidadCausa,
		String			as_intrauterinoArmonico,
		String			as_intrauterinoArmonicoCausa,
		String			as_reanimacion,
		String			as_reanimacionAspiracion,
		String			as_reanimacionMedicamentos,
		String			as_sano,
		String			as_sanoDescripcion,
		String			as_sexoDescripcion,
		int				ai_edadGestacional,
		String			as_edadGestacionalDescripcion,
		String			as_liqAmnioticoClaro,
		String			as_liqAmnioticoMeconiado,
		String			as_liqAmnioticoMeconiadoGrado,
		String			as_liqAmnioticoSanguinolento,
		String			as_liqAmnioticoFetido,
		String			as_placentaCaracteristicas,
		String			as_placentaCaracteristicasDescripcion,
		String			observaciones,
		String			loginUsuario,
		String			fecha,
		ArrayList		tiposParto,
		ArrayList		inmunizacionesPediatricas,
		InfoMadre		infoMadre,
		InfoPadre		aip_infoPadre,
		ArrayList		aal_embarazoOpciones,
		DaoFactory myFactory
	)throws SQLException
	{
		boolean				modificoAntTiposParto;
		boolean				modificoInmunizaciones;
		boolean				lb_modificoOpcionesEmbarazo;
		boolean				resp0;
		boolean				resp1;
		boolean				resp2;
		boolean				resp3;
		boolean				sufrimientoFetal_boolean;
		int					returnedVal;
		PreparedStatementDecorator	modificarAntecedentePediatrico;

		modificoAntTiposParto = modificoInmunizaciones = false;
		lb_modificoOpcionesEmbarazo = sufrimientoFetal_boolean = false;
		resp0 = resp1 = resp2 = resp3 = false;

		returnedVal	= 0;

		/* Iniciamos la transacci�n */
		resp0 = myFactory.beginTransaction(con);

		/* modificaci�n de datos de antecedentes pediatricos */
		String is_modificarAntecedentePediatrico = obtenerCadenaUpdate();
		modificarAntecedentePediatrico =  new PreparedStatementDecorator(con.prepareStatement(is_modificarAntecedentePediatrico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/*
			presentacionNacimiento = -1, indica que no se inicializo este campo
			presentacionNacimiento = 0, indica que se inicializ� en el tipo 'Otro'
		*/
		if(presentacionNacimiento != -1 && presentacionNacimiento != 0)
		{
			modificarAntecedentePediatrico.setInt(1, presentacionNacimiento);
			modificarAntecedentePediatrico.setString(2, null);
		}
		else
		{
			modificarAntecedentePediatrico.setString(1, null);

			/*
				Si es 'Otra' presentacion nacimiento, debe tener algo en el campo
				otraPresentacionNacimiento
			*/
			if(presentacionNacimiento == 0)
				modificarAntecedentePediatrico.setString(2, otraPresentacionNacimiento);
			else
				modificarAntecedentePediatrico.setString(2, null);
		}

		/*
			ubicacionFeto = -1, indica que no se inicializo este campo
			ubicacionFeto = 0, indica que se inicializ� en el tipo 'Otro'
		*/
		if(ubicacionFeto != -1 && ubicacionFeto != 0)
		{
			modificarAntecedentePediatrico.setInt(3, ubicacionFeto);
			modificarAntecedentePediatrico.setString(4, null);
		}
		else
		{
			modificarAntecedentePediatrico.setString(3, null);

			/* Si es 'Otra' ubicacion feto, debe tener algo en el campo otraUbicacionFeto */
			if(ubicacionFeto == 0)
				modificarAntecedentePediatrico.setString(4, otraUbicacionFeto);
			else
				modificarAntecedentePediatrico.setString(4, null);
		}

		/* Campos enteros que tengan el valor -1, indican que no se inicializo este campo */
		if(duracionPartoMins != -1)
			modificarAntecedentePediatrico.setInt(5, duracionPartoMins);
		else
			modificarAntecedentePediatrico.setString(5, null);

		if(duracionPartoHoras != -1)
			modificarAntecedentePediatrico.setInt(6, duracionPartoHoras);
		else
			modificarAntecedentePediatrico.setString(6, null);

		if(rupturaMembranasMins != -1)
			modificarAntecedentePediatrico.setInt(7, rupturaMembranasMins);
		else
			modificarAntecedentePediatrico.setString(7, null);

		if(rupturaMembranasHoras != -1)
			modificarAntecedentePediatrico.setInt(8, rupturaMembranasHoras);
		else
			modificarAntecedentePediatrico.setString(8, null);

		modificarAntecedentePediatrico.setString(9, caracteristicasLiqAmniotico);

		/* Si se inicializo el campo de sufrimiento fetal */
		if(sufrimientoFetal.equalsIgnoreCase("true") || sufrimientoFetal.equalsIgnoreCase("false") )
		{
			if(sufrimientoFetal.equalsIgnoreCase("true") )
				sufrimientoFetal_boolean = true;
			else if(sufrimientoFetal.equalsIgnoreCase("false") )
			{
				sufrimientoFetal_boolean	= false;
				causasSufrimientoFetal		= null;
			}

			modificarAntecedentePediatrico.setBoolean(10, sufrimientoFetal_boolean);
			modificarAntecedentePediatrico.setString(11, causasSufrimientoFetal);
		}
		else
		{
			/*
				Si no se inicializo el campo de sufrimiento fetal no se tiene en cuenta las causas
				del sufrimiento fetal. O tiene cualquier valor diferente a yes o no
			*/
			modificarAntecedentePediatrico.setString(10, null);
			modificarAntecedentePediatrico.setString(11, null);
		}

		modificarAntecedentePediatrico.setString(12, anestesiaMedicamentos);

		/*
			Insercion del tipo de trabajo de parto. Caso no se selecciono ningun
			trabajo de parto, tampoco se tiene en cuenta el comentario
		*/
		if(tipoTrabajoParto == -1)
		{
			modificarAntecedentePediatrico.setString(13, null);
			modificarAntecedentePediatrico.setString(14, null);
		}
		else
		{
			/* De lo contrario debe venir un tipoTrabajoParto valido */
			modificarAntecedentePediatrico.setInt(13, tipoTrabajoParto);
			modificarAntecedentePediatrico.setString(14, comentarioTipoTrabajoParto);
		}

		/*
			Campos de texto no llenos pueden estar como cadenas vacias e indican que no se
			inicializ� este campo en la bd inserto null en caso de ser FK a otras tablas.
			Si eligio otro tipo parto debe especificar el campo otroTipoParto que corresponde
			a la pregunta "cu�l?". Si no especifico nada en otroTIpoParto no se inserta el
			motivoTIpoPartoOtro
		*/
		if( (otroTipoParto == null) || (otroTipoParto != null && otroTipoParto.equals("") ) )
		{
			otroTipoParto		= null;
			motivoTipoPartoOtro	= null;
		}

		modificarAntecedentePediatrico.setString(15, otroTipoParto);
		modificarAntecedentePediatrico.setString(16, motivoTipoPartoOtro);

		if(tiempoNacimiento != null && tiempoNacimiento.equals("") )
			modificarAntecedentePediatrico.setString(17, null);
		else
			modificarAntecedentePediatrico.setString(17, tiempoNacimiento);

		modificarAntecedentePediatrico.setString(18, caracteristicasPlacenta);

		if(talla != -1)
			modificarAntecedentePediatrico.setInt(19, talla);
		else
			modificarAntecedentePediatrico.setString(19, null);

		if(peso != -1)
			modificarAntecedentePediatrico.setInt(20, peso);
		else modificarAntecedentePediatrico.setString(20, null);

		if(perimetroCefalico != -1)
				modificarAntecedentePediatrico.setFloat(21, perimetroCefalico);
		else
			modificarAntecedentePediatrico.setString(21, null);

		if(perimetroToracico != -1)
			modificarAntecedentePediatrico.setFloat(22, perimetroToracico);
		else
			modificarAntecedentePediatrico.setString(22, null);

		/* Si se especifico un apgar se debe obligatoriamente especificar el riesgo */
		if(apgarMinuto1 != -1 && riesgoApgarMinuto1 != -1)
		{
			modificarAntecedentePediatrico.setInt(23, apgarMinuto1);
			modificarAntecedentePediatrico.setInt(26, riesgoApgarMinuto1);
		}
		else
		{
			modificarAntecedentePediatrico.setString(23, null);
			modificarAntecedentePediatrico.setString(26, null);
		}

		if(apgarMinuto5 != -1 && riesgoApgarMinuto5 != -1)
		{
			modificarAntecedentePediatrico.setInt(24, apgarMinuto5);
			modificarAntecedentePediatrico.setInt(27, riesgoApgarMinuto5);
		}
		else
		{
			modificarAntecedentePediatrico.setString(24, null);
			modificarAntecedentePediatrico.setString(27, null);
		}

		if(apgarMinuto10 != -1 && riesgoApgarMinuto10 != -1)
		{
			modificarAntecedentePediatrico.setInt(25, apgarMinuto10);
			modificarAntecedentePediatrico.setInt(28, riesgoApgarMinuto10);
		}
		else
		{
			modificarAntecedentePediatrico.setString(25, null);
			modificarAntecedentePediatrico.setString(28, null);
		}

		modificarAntecedentePediatrico.setString(29, as_embarazosAnterioresOtros);
		modificarAntecedentePediatrico.setString(30, as_incompatibilidadABO);
		modificarAntecedentePediatrico.setString(31, as_incompatibilidadRH);
		modificarAntecedentePediatrico.setString(32, as_macrosomicos);
		modificarAntecedentePediatrico.setString(33, as_malformacionesCongenitas);
		modificarAntecedentePediatrico.setString(34, as_mortinatos);
		modificarAntecedentePediatrico.setString(35, as_muertesFetalesTempranas);
		modificarAntecedentePediatrico.setString(36, as_prematuros);

		modificarAntecedentePediatrico.setInt(37, ai_codigoSerologia);
		modificarAntecedentePediatrico.setString(38, as_descripcionSerologia);
		modificarAntecedentePediatrico.setInt(39, ai_codigoTestSullivan);
		modificarAntecedentePediatrico.setString(40, as_descripcionTestSullivan);
		modificarAntecedentePediatrico.setString(41, as_controlPrenatal);
		modificarAntecedentePediatrico.setString(42, as_frecuenciaControlPrenatal);
		modificarAntecedentePediatrico.setString(43, as_lugarControlPrenatal);

		modificarAntecedentePediatrico.setInt(44, ai_duracionExpulsivoHoras);
		modificarAntecedentePediatrico.setInt(45, ai_duracionExpulsivoMinutos);
		modificarAntecedentePediatrico.setString(46, as_amnionitis);
		modificarAntecedentePediatrico.setString(47, as_amnionitisFactorAnormal);
		modificarAntecedentePediatrico.setString(48, as_anestesia);
		modificarAntecedentePediatrico.setString(49, as_anestesiaTipo);
		modificarAntecedentePediatrico.setString(50, as_nst);
		modificarAntecedentePediatrico.setString(51, as_nstDescripcion);
		modificarAntecedentePediatrico.setString(52, as_otrosExamenesTrabajoParto);
		modificarAntecedentePediatrico.setString(53, as_perfilBiofisico);
		modificarAntecedentePediatrico.setString(54, as_perfilBiofisicoDescripcion);
		modificarAntecedentePediatrico.setString(55, as_ptc);
		modificarAntecedentePediatrico.setString(56, as_ptcDescripcion);

		modificarAntecedentePediatrico.setString(57, as_complicacionesParto);
		modificarAntecedentePediatrico.setString(58, as_muestraCordonUmbilical);
		modificarAntecedentePediatrico.setString(59, as_muestraCordonUmbilicalDescripcion);
		modificarAntecedentePediatrico.setString(60, as_cordonUmbilicalCaracteristicas);
		modificarAntecedentePediatrico.setString(61, as_cordonUmbilicalDescripcion);
		modificarAntecedentePediatrico.setString(62, as_gemelo);
		modificarAntecedentePediatrico.setString(63, as_gemeloDescripcion);
		modificarAntecedentePediatrico.setString(64, as_intrauterinoPeg);
		modificarAntecedentePediatrico.setString(65, as_intrauterinoPegCausa);
		modificarAntecedentePediatrico.setInt(66, ai_intrauterinoAnormalidad);
		modificarAntecedentePediatrico.setString(67, as_intrauterinoAnormalidadCausa);
		modificarAntecedentePediatrico.setString(68, as_intrauterinoArmonico);
		modificarAntecedentePediatrico.setString(69, as_intrauterinoArmonicoCausa);
		modificarAntecedentePediatrico.setString(70, as_reanimacion);
		modificarAntecedentePediatrico.setString(71, as_reanimacionAspiracion);
		modificarAntecedentePediatrico.setString(72, as_reanimacionMedicamentos);
		modificarAntecedentePediatrico.setString(73, as_sano);
		modificarAntecedentePediatrico.setString(74, as_sanoDescripcion);
		modificarAntecedentePediatrico.setString(75, as_sexoDescripcion);

		modificarAntecedentePediatrico.setInt(76, ai_edadGestacional);
		modificarAntecedentePediatrico.setString(77, as_edadGestacionalDescripcion);
		modificarAntecedentePediatrico.setString(78, as_liqAmnioticoClaro);
		modificarAntecedentePediatrico.setString(79, as_liqAmnioticoMeconiado);
		modificarAntecedentePediatrico.setString(80, as_liqAmnioticoMeconiadoGrado);
		modificarAntecedentePediatrico.setString(81, as_liqAmnioticoSanguinolento);
		modificarAntecedentePediatrico.setString(82, as_liqAmnioticoFetido);
		modificarAntecedentePediatrico.setString(83, as_placentaCaracteristicas);
		modificarAntecedentePediatrico.setString(84, as_placentaCaracteristicasDescripcion);

		modificarAntecedentePediatrico.setString(85, observaciones);
		modificarAntecedentePediatrico.setString(86, loginUsuario);
		modificarAntecedentePediatrico.setString(87, fecha);

		modificarAntecedentePediatrico.setInt(88, paciente.getCodigoPersona() );

		try
		{
			returnedVal = modificarAntecedentePediatrico.executeUpdate();

			/* Modificando o Insertando los tipos de Parto */
			modificoAntTiposParto = modificarAntTiposParto(con, paciente, tiposParto);

			/* Modificando o Insertando las inmunizaciones */
			modificoInmunizaciones =
				modificarInmunizaciones(con, paciente, inmunizacionesPediatricas);

			/* Modificando opciones de embarazo */
			lb_modificoOpcionesEmbarazo =
				modificarEmbarazoOpciones(con, paciente, aal_embarazoOpciones);

			resp2 = (infoMadre.modificar(con) > 0);
			resp3 = (aip_infoPadre.modificar(con) > 0);
		}
		catch(SQLException sql_e)
		{
			myFactory.abortTransaction(con);
			sql_e.printStackTrace();
			throw sql_e;
		}

		resp1 = (returnedVal > 0);
		modificarAntecedentePediatrico.close();

		/* Terminamos la transacci�n, bien sea con un commit o un rollback */
		if(
			resp0 && resp1 && resp2 && resp3	&&
			modificoAntTiposParto				&&
			modificoInmunizaciones				&&
			lb_modificoOpcionesEmbarazo
		)
			myFactory.endTransaction(con);
		else
			myFactory.abortTransaction(con);

		return returnedVal;
	}

	/** Modifica la informaci�n de opciones de embarazo del paciente */
	private static boolean modificarEmbarazoOpciones(
		Connection		ac_con,
		PersonaBasica	apb_paciente,
		ArrayList		aal_embarazoOpciones
	)throws SQLException
	{
		boolean				lb_exito;
		InfoDatos			lid_opcion;
		PreparedStatementDecorator	lps_consultar;
		PreparedStatementDecorator	lps_insertar;
		PreparedStatementDecorator	lps_modificar;
		ResultSetDecorator			lsr_consultar;
		String[]			lsa_codigo;

		lb_exito		= true;

		lps_consultar	= new PreparedStatementDecorator(ac_con.prepareStatement(is_consultarEmbarazoOpcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		lps_insertar	= new PreparedStatementDecorator(ac_con.prepareStatement(is_insertarEmbarazoOpcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		lps_modificar	= new PreparedStatementDecorator(ac_con.prepareStatement(is_modificarEmbarazoOpcion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Asignar la identificaci�n del paciente sobre el cual se va a trabajar */
		lps_consultar.setInt(1, apb_paciente.getCodigoPersona() );

		lps_insertar.setInt(1, apb_paciente.getCodigoPersona() );

		lps_modificar.setInt(5, apb_paciente.getCodigoPersona() );

		/* no hay datos para insertar/actualizar */
		if(aal_embarazoOpciones == null)
			return lb_exito;

		/* Insertar / actualizar los datos presentes */
		for(int li_i = 0; li_i < aal_embarazoOpciones.size() && lb_exito; li_i++)
		{
			/* Obtener la opci�n-campo sobre la cual se va a trabajar */
			lid_opcion	= (InfoDatos)aal_embarazoOpciones.get(li_i);
			lsa_codigo	= lid_opcion.getAcronimo().split("-");

			/* Varificar que el identificador de la opci�n-campo tenga tres posiciones */
			if(lsa_codigo.length == 3)
			{
				/* Asignar el valor de la opci�n de embarazo */
				lps_consultar.setInt(2, Integer.parseInt(lsa_codigo[0]) );
				lps_consultar.setInt(3, Integer.parseInt(lsa_codigo[1]) );

				if( (lsr_consultar = new ResultSetDecorator(lps_consultar.executeQuery()) ).next() )
				{
					/* Asignar el valor de la opci�n de embarazo a actualizar */
					lps_modificar.setInt(6, Integer.parseInt(lsa_codigo[0]) );
					lps_modificar.setInt(7, Integer.parseInt(lsa_codigo[1]) );

					/* Limpiar el valor de los campos de la opci�n a actualizar */
					lps_modificar.setString(1, lsr_consultar.getString("valor_campo1") );
					lps_modificar.setString(2, lsr_consultar.getString("valor_campo2") );
					lps_modificar.setString(3, lsr_consultar.getString("valor_campo3") );
					lps_modificar.setString(4, lsr_consultar.getString("existio") );

					/* Ajustar el valor del campo a insertar */
					lps_modificar.setString(
						Integer.parseInt(lsa_codigo[2]), lid_opcion.getValue()
					);

					if(lps_modificar.executeUpdate() != 1)
						lb_exito = false;
				}
				else
				{
					/* Asignar el valor de la opci�n de embarazo a insertar */
					lps_insertar.setInt(2, Integer.parseInt(lsa_codigo[0]) );
					lps_insertar.setInt(3, Integer.parseInt(lsa_codigo[1]) );

					/* Limpiar el valor de los campos a insertar */
					lps_insertar.setString(4, null);
					lps_insertar.setString(5, null);
					lps_insertar.setString(6, null);
					lps_insertar.setString(7, "true");

					/* Ajustar el valor del campo a insertar */
					lps_insertar.setString(
						Integer.parseInt(lsa_codigo[2]) + 3, lid_opcion.getValue()
					);

					if(lps_insertar.executeUpdate() != 1)
						lb_exito = false;
				}
			}
		}
		lps_consultar.close();
		lps_insertar.close();
		lps_modificar.close();
		return lb_exito;
	}

	public static  int modificarInfoMadre
	(
		Connection	con,
		int			ai_codigoPaciente,
		int			edad,
		int			tipoSangre,
		float		semanasGestacion,
		String		obsPatologias,
		String		obsExamenesMedicamentos,
		String		observaciones,
		int			ai_metodoSemanasGestacion,
		String		as_confiableSemanasGestacion,
		String		as_fpp,
		String		as_fur,
		String		as_infoEmbarazoA,
		String		as_infoEmbarazoC,
		String		as_infoEmbarazoG,
		String		as_infoEmbarazoM,
		String		as_infoEmbarazoP,
		String		as_infoEmbarazoV,
		String		as_primerApellido,
		String		as_primerNombre,
		String		as_segundoApellido,
		String		as_segundoNombre,
		String		as_tipoIdentificacion,
		String		as_numeroIdentificacion,
		DaoFactory myFactory,
		String 		as_otroMetodo
	)throws SQLException
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
		try{
			if(con == null || con.isClosed() ){
				con = myFactory.getConnection();
			}
			pst = con.prepareStatement(is_modificarInfoMadre,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	
			pst.setInt(1, edad);
			pst.setInt(16, ai_metodoSemanasGestacion);
			pst.setInt(24, ai_codigoPaciente);
	
			if(tipoSangre > -1)
				pst.setInt(2, tipoSangre);
			else
				pst.setNull(2, Types.INTEGER);
	
			pst.setFloat(3, semanasGestacion);
	
			pst.setString(4, obsPatologias);
			pst.setString(5, obsExamenesMedicamentos);
			pst.setString(6, observaciones);
			pst.setString(7, as_confiableSemanasGestacion);
	
			pst.setString(10, as_infoEmbarazoA);
			pst.setString(11, as_infoEmbarazoC);
			pst.setString(12, as_infoEmbarazoG);
			pst.setString(13, as_infoEmbarazoM);
			pst.setString(14, as_infoEmbarazoP);
			pst.setString(15, as_infoEmbarazoV);
			pst.setString(17, as_primerApellido);
			pst.setString(18, as_primerNombre);
			pst.setString(19, as_segundoApellido);
			pst.setString(20, as_segundoNombre);
			pst.setString(21, as_tipoIdentificacion);
			pst.setString(22, as_numeroIdentificacion);
			pst.setString(23, as_otroMetodo);
	
			if(as_fpp == null || as_fpp.equals("") )
				pst.setNull(8, Types.DATE);
			else
				pst.setString(8, UtilidadFecha.conversionFormatoFechaABD(as_fpp) );
	
			if(as_fur == null || as_fur.equals("") )
				pst.setNull(9, Types.DATE);
			else
				pst.setString(9, UtilidadFecha.conversionFormatoFechaABD(as_fur) );
	
			result=pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarInfoMadre", e);
	    }
	    finally{
			try{
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

	/** Inserta un registro de informaci�n del padre */
	public static int modificarInfoPadre
	(
		Connection	ac_con,
		int			ai_codigoPaciente,
		int			ai_edad,
		int			ai_tipoSangre,
		String		as_consanguinidad,
		String		as_primerApellido,
		String		as_primerNombre,
		String		as_segundoApellido,
		String		as_segundoNombre,
		String		as_tipoIdentificacion,
		String		as_numeroIdentificacion,
		DaoFactory myFactory
	)throws SQLException
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
		try{

			if(ac_con == null || ac_con.isClosed() ){
				ac_con = myFactory.getConnection();
			}	
			pst = ac_con.prepareStatement(is_modificarInfoPadre,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	
			pst.setInt(1, ai_edad);
	
			if(ai_tipoSangre > -1)
				pst.setInt(3, ai_tipoSangre);
			else
				pst.setNull(3, Types.INTEGER);
	
			pst.setInt(10, ai_codigoPaciente);
	
			pst.setString(2, as_consanguinidad);
			pst.setString(4, as_primerApellido);
			pst.setString(5, as_primerNombre);
			pst.setString(6, as_segundoApellido);
			pst.setString(7, as_segundoNombre);
			pst.setString(8, as_tipoIdentificacion);
			pst.setString(9, as_numeroIdentificacion);
	
			result=pst.executeUpdate();
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarInfoPadre", e);
	    }
	    finally{
			try{
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
	 * Implementaci�n de la revisi�n si existe antecedente de paciente
	 * para una BD Gen�rica
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#existeAntecedentePaciente(Connection , int ) throws SQLException
	 */
	public static boolean existeAntecedentePaciente(Connection con, int codigoPaciente) throws SQLException
	{
		PreparedStatement pst= null;
		ResultSet rs=null;
		boolean result=false;
		try{
			pst=con.prepareStatement(existeAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoPaciente);
			
			rs=pst.executeQuery();
			if (rs.next())
			{
				if (rs.getInt("numResultados")<1)
				{
					result=false;
				}
				else
				{
					result=true;
				}
			}
		}
		catch(Exception e)
	    {
		    Log4JManager.error("ERROR modificarAntTiposParto", e);
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
	 * 
	 * @param ac_con
	 * @param ai_codigoPaciente
	 * @param myFactory
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator cargarAntecedentePediatricoRS(Connection con, HashMap mapa) throws SQLException
		{
			String consulta = "SELECT count(1) 									" +
							  "       FROM antecedentes_pediatricos				" +
							  "            WHERE codigo_paciente = ? " + mapa.get("paciente");			
		
			try
			{
				PreparedStatementDecorator stm =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				return new ResultSetDecorator(stm.executeQuery());
			}
			catch(Exception le_e)
			{
				final Logger il_logger = Logger.getLogger(SqlBaseAntecedentePediatricoDao.class);
				il_logger.warn(le_e);
				return null;
			}
		}

	/**
	 * Para cargar las patologias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public static HashMap consultarPatologias(Connection con, int codigoPersona)
	{
		String consulta =  "  SELECT ca.nombre AS categoria, c.nombre AS compli     " +
					 	   "		 FROM cat_emb_opciones c 						" +	
					 	   "		      INNER JOIN categorias_embarazo ca ON ( ca.codigo = c.codigo_categoria_embarazo  )   " + 
					 	   "	          INNER JOIN ant_ped_embarazo a ON ( a.codigo_categoria_embarazo = c.codigo_categoria_embarazo AND a.cod_cat_embarazo_opcion = c.codigo ) " +  
					 	   "		   	   	   WHERE a.codigo_paciente = " + codigoPersona  +  
					 	   "				  	   	 ORDER BY ca.nombre";	
		
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		HashMap mapaRetorno=null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs=new ResultSetDecorator(pst.executeQuery());
			mapaRetorno= UtilidadBD.cargarValueObject(rs,true,false);
			
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
		return mapaRetorno;
	}
	
	
	/**
	* Cadena constante con el <i>statement</i> necesario para modificar un antecedente pediatrico
	*/
	private static String obtenerCadenaUpdate()
	{
		String is_modificarAntecedentePediatrico =
			"UPDATE "	+	"antecedentes_pediatricos "				+
			"SET "		+	"presentacion_nacimiento"				+	"=?,"	+
							"otra_presentacion_nacimiento"			+	"=?,"	+
							"ubicacion_feto"						+	"=?,"	+
							"otra_ubicacion_feto"					+	"=?,"	+
							"duracion_parto_mins"					+	"=?,"	+
							"duracion_parto_horas"					+	"=?,"	+
							"ruptura_membranas_mins"				+	"=?,"	+
							"ruptura_membranas_horas"				+	"=?,"	+
							"caracteristicas_liq_amniotico"			+	"=?,"	+
							"sufrimiento_fetal"						+	"=?,"	+
							"causas_sufrimiento_fetal"				+	"=?,"	+
							"anestesia_medicamentos"				+	"=?,"	+
							"tipo_trabajo_parto"					+	"=?,"	+
							"comentario_tipo_trabajo_parto"			+	"=?,"	+
							"otro_tipo_parto"						+	"=?,"	+
							"motivo_tipo_parto"						+	"=?,"	+
							"tiempo_nacimiento"						+	"=?,"	+
							"caracteristicas_placenta"				+	"=?,"	+
							"talla"									+	"=?,"	+
							"peso"									+	"=?,"	+
							"perimetro_cefalico"					+	"=?,"	+
							"perimetro_toracico"					+	"=?,"	+
							"apgar_minuto1"							+	"=?,"	+
							"apgar_minuto5"							+	"=?,"	+
							"apgar_minuto10"						+	"=?,"	+
							"riesgo_apgar_minuto1"					+	"=?,"	+
							"riesgo_apgar_minuto5"					+	"=?,"	+
							"riesgo_apgar_minuto10"					+	"=?,"	+
							"embarazos_anteriores_otros"			+	"=?,"	+
							"incompatibilidad_abo"					+	"=?,"	+
							"incompatibilidad_rh"					+	"=?,"	+
							"macrosomicos"							+	"=?,"	+
							"malformaciones_congenitas"				+	"=?,"	+
							"mortinatos"							+	"=?,"	+
							"muertes_fetales_tempranas"				+	"=?,"	+
							"prematuros"							+	"=?,"	+
							"codigo_serologia"						+	"=?,"	+
							"descripcion_serologia"					+	"=?,"	+
							"codigo_test_sullivan"					+	"=?,"	+
							"descripcion_test_sullivan"				+	"=?,"	+
							"control_prenatal"						+	"=?,"	+
							"frecuencia_control_prenatal"			+	"=?,"	+
							"lugar_control_prenatal"				+	"=?,"	+
							"duracion_expulsivo_horas"				+	"=?,"	+
							"duracion_expulsivo_mins"				+	"=?,"	+
							"amnionitis"							+	"=?,"	+
							"amnionitis_factor_anormal"				+	"=?,"	+
							"anestesia"								+	"=?,"	+
							"anestesia_tipo"						+	"=?,"	+
							"nst"									+	"=?,"	+
							"nst_descripcion"						+	"=?,"	+
							"otros_examenes_trabajo_parto"			+	"=?,"	+
							"perfil_biofisico"						+	"=?,"	+
							"perfil_biofisico_descripcion"			+	"=?,"	+
							"ptc"									+	"=?,"	+
							"ptc_descripcion"						+	"=?,"	+
							"complicaciones_parto"					+	"=?,"	+
							"muestra_cordon_umbilical"				+	"=?,"	+
							"mues_cor_umbilical_des"				+	"=?,"	+
							"cordon_umb_carac"						+	"=?,"	+
							"cordon_umbilical_descripcion"			+	"=?,"	+
							"gemelo"								+	"=?,"	+
							"gemelo_descripcion"					+	"=?,"	+
							"intrauterino_peg"						+	"=?,"	+
							"intrauterino_peg_causa"				+	"=?,"	+
							"intrauterino_anormalidad"				+	"=?,"	+
							"intrauterino_anormalidad_causa"		+	"=?,"	+
							"intrauterino_armonico"					+	"=?,"	+
							"intrauterino_armonico_causa"			+	"=?,"	+
							"reanimacion"							+	"=?,"	+
							"reanimacion_aspiracion"				+	"=?,"	+
							"reanimacion_medicamentos"				+	"=?,"	+
							"sano"									+	"=?,"	+
							"sano_descripcion"						+	"=?,"	+
							"sexo_descripcion"						+	"=?,"	+
							"edad_gestacional"						+	"=?,"	+
							"edad_gestacional_descripcion"			+	"=?,"	+
							"liq_amniotico_claro"					+	"=?,"	+
							"liq_amniotico_meconiado"				+	"=?,"	+
							"liq_amniotico_meconiado_grado"			+	"=?,"	+
							"liq_amniotico_sanguinolento"			+	"=?,"	+
							"liq_amniotico_fetido"					+	"=?,"	+
							"placenta_caracteristicas"				+	"=?,"	+
							"plac_catac_desc"						+	"=?,"	+
							"observaciones"							+	"=?,"	+
							"login_usuario"							+	"=?,"	+
							"fecha"									+	"=?, "	+
							"hora='"								+UtilidadFecha.getHoraActual()+"' "+
							
						"WHERE "	+	"codigo_paciente"						+	"=?";
		
		return is_modificarAntecedentePediatrico;
	}

}
