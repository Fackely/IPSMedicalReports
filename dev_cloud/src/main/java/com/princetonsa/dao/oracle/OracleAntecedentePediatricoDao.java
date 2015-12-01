/*
* @(#)OracleAntecedentePediatricoDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao.oracle;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.AntecedentePediatricoDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentePediatricoDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;

/**
* Esta clase implementa el contrato estipulado en <code>AntecedentePediatricoDao</code>,
* proporcionando los servicios de acceso a una base de datos Oracle requeridos por la clase
* <code>AntecedentePediatrico</code>
*
* @version	1.1, Jul 21, 2003
* @author	<a href="mailto:sandra@princetonsa.com">Sandra Moya</a>,
*			<a href="mailto:oscar@princetonsa.com">Oscar López</a>,
*			<a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
*/
@SuppressWarnings("rawtypes")
public class OracleAntecedentePediatricoDao implements AntecedentePediatricoDao, Serializable
{
	/** *  */
	private static final long serialVersionUID = 1L;

	/**
	* Este método implementa cargarAntecedentePediatrico para Oracle
	* @see com.princetonsa.dao.AntecedentePediatricoDao#cargarAntecedentePediatrico()
	*/
	public ResultSetDecorator cargarAntecedentePediatrico(Connection	ac_con, int	ai_codigoPaciente)throws SQLException
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.cargarAntecedentePediatrico(ac_con,ai_codigoPaciente,myFactory);
	}

	public String consultarNombrePresentacionNacimiento(Connection con, int codigo)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarNombrePresentacionNacimiento(con,codigo,myFactory);
	}

	public String consultarNombreUbicacionFeto(Connection con, int codigo)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarNombreUbicacionFeto(con,codigo,myFactory);
	}

	public String consultarObservacionInmunizacion(
		Connection	ac_con,
		int			ai_codigoPaciente,
		int			ai_codigo
	)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarObservacionInmunizacion(ac_con,ai_codigoPaciente,ai_codigo,myFactory);
	}

	public String consultarNombreTipoTrabajoParto(Connection ac_con, int ai_codigo)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarNombreTipoTrabajoParto(ac_con,ai_codigo, myFactory);
	}

	/** Este método implementa consultarNombreRiesgoApgar para Oracle */
	public String consultarNombreRiesgoApgar(Connection ac_con, int ai_codigo)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarNombreRiesgoApgar(ac_con,ai_codigo,myFactory);	
	}

	/** Este método implementa consultarAntPediatricoInmunizacionesDosis para Oracle */
	public ResultSetDecorator consultarAntPediatricoInmunizacionesDosis(
		Connection	ac_con,
		int			ai_codigoPaciente
	)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarAntPediatricoInmunizacionesDosis(ac_con,ai_codigoPaciente,myFactory);
	}

	/** Este método implementa consultarAntPediatricoInmunizacionesObser para Oracle */
	public ResultSetDecorator consultarAntPediatricoInmunizacionesObser(
		Connection	ac_con,
		int			ai_codigoPaciente
	)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarAntPediatricoInmunizacionesObser(ac_con,ai_codigoPaciente,myFactory);
	}

	/** Este método implementa consultarAntPediatricoTiposParto para Oracle */
	public ResultSetDecorator consultarAntPediatricoTiposParto(Connection ac_con, int ai_codigoPaciente)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarAntPediatricoTiposParto(ac_con, ai_codigoPaciente,myFactory);
	}

	/** Obtiene todas las opciones de embarazo de un paciente */
	public ResultSetDecorator consultarEmbarazoOpciones(Connection ac_con, int ai_codigoPaciente)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarEmbarazoOpciones(ac_con, ai_codigoPaciente,myFactory);		
	}

	/** Obtiene la información de la madre */
	public ResultSetDecorator consultarInfoMadre(
		Connection	con,
		int			ai_codigoPaciente
	)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarInfoMadre(con,ai_codigoPaciente,myFactory);
	}

	/** Obtiene la información del padre */
	public ResultSetDecorator consultarInfoPadre(Connection ac_con, int ai_codigoPaciente)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.consultarInfoPadre(ac_con,ai_codigoPaciente,myFactory);
	}

	/**
	* Este método implementa insertarAntecedentePediatrico para Oracle
	* @see com.princetonsa.dao.AntecedentePediatricoDao#insertarAntecedentePediatrico()
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
	)throws SQLException
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.insertarAntecedentePediatrico(con,paciente,presentacionNacimiento,
																																otraPresentacionNacimiento,ubicacionFeto,
																																otraUbicacionFeto, duracionPartoMins,
																																duracionPartoHoras,rupturaMembranasMins,
																																rupturaMembranasHoras,	caracteristicasLiqAmniotico,
																																sufrimientoFetal,	causasSufrimientoFetal,
																																anestesiaMedicamentos,tipoTrabajoParto,
																																comentarioTipoTrabajoParto,otroTipoParto,
																																motivoTipoPartoOtro,	tiempoNacimiento,
																																caracteristicasPlacenta,talla,
																																peso,	perimetroCefalico,
																																perimetroToracico,	apgarMinuto1,
																																apgarMinuto5,apgarMinuto10,
																																riesgoApgarMinuto1,	riesgoApgarMinuto5,
																																riesgoApgarMinuto10,as_embarazosAnterioresOtros,
																																as_incompatibilidadABO,as_incompatibilidadRH,
																																as_macrosomicos,as_malformacionesCongenitas,
																																as_mortinatos,as_muertesFetalesTempranas,
																																as_prematuros,	ai_codigoSerologia,
																																as_descripcionSerologia,	ai_codigoTestSullivan,
																																as_descripcionTestSullivan,	as_controlPrenatal,
																																as_frecuenciaControlPrenatal,	as_lugarControlPrenatal,
																																ai_duracionExpulsivoHoras,	ai_duracionExpulsivoMinutos,
																																as_amnionitis,as_amnionitisFactorAnormal,
																																as_anestesia,	as_anestesiaTipo,
																																as_nst,as_nstDescripcion,
																																as_otrosExamenesTrabajoParto,as_perfilBiofisico,
																																as_perfilBiofisicoDescripcion,as_ptc,
																																as_ptcDescripcion,as_complicacionesParto,
																																as_muestraCordonUmbilical,as_muestraCordonUmbilicalDescripcion,
																																as_cordonUmbilicalCaracteristicas,
																																as_cordonUmbilicalDescripcion,
																																as_gemelo,	as_gemeloDescripcion,
																																as_intrauterinoPeg, as_intrauterinoPegCausa,
																																ai_intrauterinoAnormalidad,as_intrauterinoAnormalidadCausa,
																																as_intrauterinoArmonico,as_intrauterinoArmonicoCausa,
																																as_reanimacion,	as_reanimacionAspiracion,
																																as_reanimacionMedicamentos,as_sano,
																																as_sanoDescripcion,	as_sexoDescripcion,
																																ai_edadGestacional,	as_edadGestacionalDescripcion,
																																as_liqAmnioticoClaro, as_liqAmnioticoMeconiado,
																																as_liqAmnioticoMeconiadoGrado,	as_liqAmnioticoSanguinolento,
																																as_liqAmnioticoFetido,	as_placentaCaracteristicas,
																																as_placentaCaracteristicasDescripcion,
																																observaciones,	loginUsuario,	fecha,	tiposParto,
																																inmunizacionesPediatricas,	infoMadre,		aip_infoPadre,
																																aal_embarazoOpciones, myFactory);
	}

	/** Inserta un registro de información de la madre */
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
	)throws SQLException
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.insertarInfoMadre(con,	ai_codigoPaciente,	edad, tipoSangre,
																											semanasGestacion, obsPatologias,
																											obsExamenesMedicamentos,observaciones,
																											ai_metodoSemanasGestacion,
																											as_confiableSemanasGestacion,
																											as_fpp,as_fur,as_infoEmbarazoA,
																											as_infoEmbarazoC,as_infoEmbarazoG,
																											as_infoEmbarazoM,	as_infoEmbarazoP,
																											as_infoEmbarazoV,as_primerApellido,
																											as_primerNombre,	as_segundoApellido,
																											as_segundoNombre,	as_tipoIdentificacion,
																											as_numeroIdentificacion,myFactory, as_otroMetodo);
	}

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
	)throws SQLException
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.insertarInfoPadre(ac_con,ai_codigoPaciente,	ai_edad,
																											ai_tipoSangre, as_consanguinidad,
																											as_primerApellido,as_primerNombre,
																											as_segundoApellido,	as_segundoNombre,
																											as_tipoIdentificacion,	as_numeroIdentificacion,
																											myFactory);
	}

	/**
	* Este método implementa modificarAntecedentePediatrico para Oracle
	* @see com.princetonsa.dao.AntecedentePediatricoDao#modificarAntecedentePediatrico()
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
	)throws SQLException
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.modificarAntecedentePediatrico(con,paciente,presentacionNacimiento,
																																	otraPresentacionNacimiento,ubicacionFeto,
																																	otraUbicacionFeto, duracionPartoMins,
																																	duracionPartoHoras,rupturaMembranasMins,
																																	rupturaMembranasHoras,	caracteristicasLiqAmniotico,
																																	sufrimientoFetal,	causasSufrimientoFetal,
																																	anestesiaMedicamentos,tipoTrabajoParto,
																																	comentarioTipoTrabajoParto,otroTipoParto,
																																	motivoTipoPartoOtro,	tiempoNacimiento,
																																	caracteristicasPlacenta,talla,
																																	peso,	perimetroCefalico,
																																	perimetroToracico,	apgarMinuto1,
																																	apgarMinuto5,apgarMinuto10,
																																	riesgoApgarMinuto1,	riesgoApgarMinuto5,
																																	riesgoApgarMinuto10,as_embarazosAnterioresOtros,
																																	as_incompatibilidadABO,as_incompatibilidadRH,
																																	as_macrosomicos,as_malformacionesCongenitas,
																																	as_mortinatos,as_muertesFetalesTempranas,
																																	as_prematuros,	ai_codigoSerologia,
																																	as_descripcionSerologia,	ai_codigoTestSullivan,
																																	as_descripcionTestSullivan,	as_controlPrenatal,
																																	as_frecuenciaControlPrenatal,	as_lugarControlPrenatal,
																																	ai_duracionExpulsivoHoras,	ai_duracionExpulsivoMinutos,
																																	as_amnionitis,as_amnionitisFactorAnormal,
																																	as_anestesia,	as_anestesiaTipo,
																																	as_nst,as_nstDescripcion,
																																	as_otrosExamenesTrabajoParto,as_perfilBiofisico,
																																	as_perfilBiofisicoDescripcion,as_ptc,
																																	as_ptcDescripcion,as_complicacionesParto,
																																	as_muestraCordonUmbilical,as_muestraCordonUmbilicalDescripcion,
																																	as_cordonUmbilicalCaracteristicas,
																																	as_cordonUmbilicalDescripcion,
																																	as_gemelo,	as_gemeloDescripcion,
																																	as_intrauterinoPeg, as_intrauterinoPegCausa,
																																	ai_intrauterinoAnormalidad,as_intrauterinoAnormalidadCausa,
																																	as_intrauterinoArmonico,as_intrauterinoArmonicoCausa,
																																	as_reanimacion,	as_reanimacionAspiracion,
																																	as_reanimacionMedicamentos,as_sano,
																																	as_sanoDescripcion,	as_sexoDescripcion,
																																	ai_edadGestacional,	as_edadGestacionalDescripcion,
																																	as_liqAmnioticoClaro, as_liqAmnioticoMeconiado,
																																	as_liqAmnioticoMeconiadoGrado,	as_liqAmnioticoSanguinolento,
																																	as_liqAmnioticoFetido,	as_placentaCaracteristicas,
																																	as_placentaCaracteristicasDescripcion,
																																	observaciones,	loginUsuario,	fecha,	tiposParto,
																																	inmunizacionesPediatricas,	infoMadre,		aip_infoPadre,
																																	aal_embarazoOpciones, myFactory);
	}


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
	)throws SQLException
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.modificarInfoMadre(	con,	ai_codigoPaciente,	edad,
																												tipoSangre,	semanasGestacion,
																												obsPatologias,obsExamenesMedicamentos,
																												observaciones,	ai_metodoSemanasGestacion,
																												as_confiableSemanasGestacion,	as_fpp,
																												as_fur, as_infoEmbarazoA,
																												as_infoEmbarazoC,as_infoEmbarazoG,
																												as_infoEmbarazoM,	as_infoEmbarazoP,
																												as_infoEmbarazoV,	as_primerApellido,
																												as_primerNombre,	as_segundoApellido,
																												as_segundoNombre,	as_tipoIdentificacion,
																												as_numeroIdentificacion,	myFactory, as_otroMetodo);
	}

	/** Inserta un registro de información del padre */
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
	)throws SQLException
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		return SqlBaseAntecedentePediatricoDao.modificarInfoPadre(	ac_con, ai_codigoPaciente,ai_edad,
																												ai_tipoSangre,as_consanguinidad,
																												as_primerApellido,	as_primerNombre,
																												as_segundoApellido,	as_segundoNombre,
																												as_tipoIdentificacion,	as_numeroIdentificacion,
																												myFactory);	
	}

	/**
	 * Para cargar las patologias.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarPatologias(Connection con, int codigoPersona)
	{
		return SqlBaseAntecedentePediatricoDao.consultarPatologias(con, codigoPersona);
	}

	//--------------------------------------------------PARA LA HISTORIA DE ATENCIONES--------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------------------------------------------------------
	public ResultSetDecorator cargarAntecedentePediatricoRS(Connection con, HashMap mapa) throws SQLException
	{
		return SqlBaseAntecedentePediatricoDao.cargarAntecedentePediatricoRS(con, mapa);
	}
	
}