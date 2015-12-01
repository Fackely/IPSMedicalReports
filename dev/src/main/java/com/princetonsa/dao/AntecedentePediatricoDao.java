/*
* @(#)AdmisionUrgenciasDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje:	Java
* Compilador:	J2SDK 1.4.1_01
*
*/

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;

/**
* Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta
* el servicio de acceso a datos para el objeto <code>AntecedentePediatrico</code>.
*
* @version	1.1, Jul 21, 2003
* @author	<a href="mailto:sandra@princetonsa.com">Sandra Moya</a>,
*			<a href="mailto:oscar@princetonsa.com">Oscar López</a>,
*			<a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
*/
public interface AntecedentePediatricoDao
{
	/**
	* Método que permite cargar un antecedente pediatrico de un paciente
	*
	* @param ac_con				Conexión abierta con una fuente de datos
	* @param ai_codigoPaciente	Código del paciente
	* @return ResultSetDecorator con la información del antecedente pediatrico
	* @throws SQLException
	*/
	public ResultSetDecorator cargarAntecedentePediatrico(
		Connection	ac_con,
		int			ai_codigoPaciente
	)throws SQLException;

	public String consultarNombrePresentacionNacimiento(Connection con, int codigo);
	public String consultarNombreUbicacionFeto(Connection con, int codigo);
	public String consultarNombreTipoTrabajoParto(Connection con, int codigo);
	public String consultarNombreRiesgoApgar(Connection con, int codigo);

	public String consultarObservacionInmunizacion(
		Connection	ac_con,
		int			ai_codigoPaciente,
		int			ai_codigo
	);

	public ResultSetDecorator consultarAntPediatricoInmunizacionesDosis(
		Connection	ac_con,
		int			ai_codigoPaciente
	);

	public ResultSetDecorator consultarAntPediatricoInmunizacionesObser(
		Connection	ac_con,
		int			ai_codigoPaciente
	);

	public ResultSetDecorator consultarAntPediatricoTiposParto(Connection ac_con, int ai_codigoPaciente);

	/** Obtiene todas las opciones de embarazo de un paciente */
	public ResultSetDecorator consultarEmbarazoOpciones(Connection ac_con, int ai_codigoPaciente);

	public ResultSetDecorator consultarInfoMadre(Connection ac_con, int ai_codigoPaciente);
	public ResultSetDecorator consultarInfoPadre(Connection ac_con, int ai_codigoPaciente);

	/**
	* Este método inserta una nuevo antecedente pediatrico en una fuente de datos.
	* @param con una conexión abierta con una fuente de datos
	*/
	public int insertarAntecedentePediatrico
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
		ArrayList		aal_embarazoOpciones
	)throws SQLException;

	public int insertarInfoMadre
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
		String 		as_otroMetodo
	)throws SQLException;

	/** Inserta un registro de información del padre */
	public int insertarInfoPadre
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
		String		as_numeroIdentificacion
	)throws SQLException;

	/**
	* Método que permite modificar una admisión de urgencias
	*
	* @param con una conexión abierta con una fuente de datos
	* @return int 0 si salio mal o si no el número registros modificados
	* @throws SQLException
	*/
	public int modificarAntecedentePediatrico
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
		ArrayList		aal_embarazoOpciones
	)throws SQLException;

	public int modificarInfoMadre
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
		String		as_otroMetodo
	)throws SQLException;

	/** Modifica un registro de información del padre */
	public int modificarInfoPadre
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
		String		as_numeroIdentificacion
	)throws SQLException;
	
	/**
	 * Para cargar las patologias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarPatologias(Connection con, int codigoPersona);


	//--------------------------------------------------PARA LA HISTORIA DE ATENCIONES--------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------
	public ResultSetDecorator cargarAntecedentePediatricoRS(Connection con, HashMap mapa) throws SQLException;

	
}