/*
* @(#)AntecedentePediatrico.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*
*/

package com.princetonsa.mundo.antecedentes;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import util.ConstantesBD;
import util.Encoder;
import util.InfoDatos;
import util.InfoDatosBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.AntecedentePediatricoDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
* Esta clase encapsula los atributos y funcionalidades de un
* <code>AntecedentePediatrico</code>.
*
* @version 1.1, Jul 21, 2003
* @author <a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
*/
@SuppressWarnings("rawtypes")
public class AntecedentePediatrico implements /*AccesoBD,*/Serializable
{
	
	/*****************************VALIDADORES SECCIONES*************/
	/*
	 * NO SE USAN
	private boolean existeAntecedenteMaterno;
	private boolean existeAntecedentePaterno;
	private boolean existeEmbarazosAnteriores;
	private boolean existeEmbarazoActual;
	private boolean existeTrabajoParto;
	private boolean existeAtencionParto;
	private boolean existeApgar;
	private boolean existeInmunizaciones;
	private boolean existeObservaciones;
	*/
	
	/** *  */
	private static final long serialVersionUID = 1L;


	/****************INFORMACION MATERNA**************************/
	/** Objeto que maneja la informacion de la madre durante el embarazo */
	private InfoMadre infoMadre;
	
	
	/****************INFORMACION PATERNA**************************/
	/** Objeto que maneja la informacion del padre */
	private InfoPadre infoPadre;
	
	/****************EMBARAZOS ANTERIORES**************************/
	/** Campo indicador de control prenatal */
	private String controlPrenatal;
	/** Lugar del control prenatal */
	private String lugarControlPrenatal;
	/** Campo Incompatibilidad ABO de la sección de embarazos anteriores */
	private String is_incompatibilidadABO;
	/** Campo Incompatibilidad RH de la sección de embarazos anteriores */
	private String is_incompatibilidadRH;
	/** Campo Macrosómicos de la sección de embarazos anteriores */
	private String is_macrosomicos;
	/** Campo Malformaciones Congénitas de la sección de embarazos anteriores */
	private String is_malformacionesCongenitas;
	/** Campo Mortinatos de la sección de embarazos anteriores */
	private String is_mortinatos;
	/** Campo Muertes Fetales Tempranas de la sección de embarazos anteriores */
	private String is_muertesFetalesTempranas;
	/** Campo Otros de la sección de embarazos anteriores */
	private String is_embarazosAnterioresOtros;
	/** Campo Prematuros de la sección de embarazos anteriores */
	private String is_prematuros;

	/****************EMBARAZO ACTUAL**************************/
	/** Código de la serologia del embarazo actual */
	private int ii_codigoSerologia;

	/** Código del test de O'Sullivan del embarazo actual */
	private int ii_codigoTestSullivan;
	/** Descripción de la serologia del embarazo actual */
	private String is_descripcionSerologia;

	/** Descripción del test de O'Sullivan del embarazo actual */
	private String is_descripcionTestSullivan;
	
	/****************TRABAJO DE PARTO**************************/
	/** Indicador de amnionitis en el trabajo de parto */
	private String is_amnionitis;
	/** Descripción del factor anormal de amnionitis en el trabajo de parto */
	private String is_amnionitisFactorAnormal;
	/** Indicador de uso de anestesia en el trabajo de parto */
	private String is_anestesia;
	/** Descripción del tipo de anestesia usada en el trabajo de parto */
	private String is_anestesiaTipo;
	/**
	* El id de este <code>InfoDatos</code> contiene el código del
	* tipo de trabajo de parto, el value, su nombre y la descripcion
	* el comentario sobre el trabajo de parto seleccionado
	*/
	private InfoDatos tipoTrabajoParto;
	/**
	* El id de este <code>InfoDatos</code> contiene el código
	* de la ubicación del feto, y el value, su nombre
	*/
	private InfoDatos ubicacionFeto;
	/** La parte de horas de la duración del parto (forma HH:mm) */
	private int duracionPartoHoras;
	/** La parte de minutos de la duración del parto (forma HH:mm) */
	private int duracionPartoMinutos;
	/** La parte de minutos de la ruptura de membranas (forma HH:mm) */
	private int rupturaMembranasMinutos;
	/** * La parte de horas de la ruptura de membranas (forma HH:mm) */
	private int rupturaMembranasHoras;
	/** Comentario sobre las características del líquido amniótico */
	private String caracteristicasLiquidoAmniotico;
	/**
	* Indica si hubo (<b>"true"</b>) o no (<b>"false"</b>) sufrimiento
	* fetal o ninguno de los anteriores, cadena vacia
	*/
	private String sufrimientoFetal;
	/**
	* Si el anterior atributo es <b>true</b>, proporciona una
	* descripción de las causas del sufrimiento fetal
	*/
	private String causasSufrimientoFetal;
	/** Descripción de la anestesia y medicamentos usados durante el parto */
	private String anestesiaMedicamentos;
	/**
	* Si la presentación no corresponde a una de las
	* predefinidas, en este campo se indica cómo fue
	*/
	private String otraPresentacionNacimiento;
	/**
	* El id de este <code>InfoDatos</code> contiene el código de
	* la presentación del nacimiento, y el value, su nombre
	*/
	private InfoDatos presentacionNacimiento;
	/**
	* Si la ubicación del feto no corresponde a una de las
	* predefinidas, en este campo se pone una descripción.
	*/
	private String otraUbicacionFeto;
	/** La parte de minutos de la duración expulsivo (forma HH:mm) */
	private int ii_duracionExpulsivoMinutos;
	/** La parte de horas de la duración expulsivo (forma HH:mm) */
	private int ii_duracionExpulsivoHoras;
	/** ArrayList de objetos InfoDatos */
	private ArrayList tiposParto;
	/** Indicador de nst en el trabajo de parto */
	private String is_nst;
	/** Descripción del tipo de nst en el trabajo de parto */
	private String is_nstDescripcion;
	/** Otros exámenes en el trabajo de parto */
	private String is_otrosExamenesTrabajoParto;
	/** Indicador del perfil biofísico en el trabajo de parto */
	private String is_perfilBiofisico;
	/** Descripción del perfil biofísico en el trabajo de parto */
	private String is_perfilBiofisicoDescripcion;
	/** Indicador de ptc en el trabajo de parto */
	private String is_ptc;
	/** Descripción del tipo de ptc en el trabajo de parto */
	private String is_ptcDescripcion;


	/****************ATENCION DEL PARTO**************************/
	/**
	* Si el tipo de parto no corresponde a uno de los
	* predefinidos, en este campo se indica cómo fue
	*/
	private String otroTipoParto;
	/** Motivo del tipo del parto */
	private String motivoTipoPartoOtro;
	/** Valores de los campos de opciones de embarazo */
	private final ArrayList categoriaEmbarazoOpcionCampo = new ArrayList();
	/** Hora de nacimiento del niño */
	private String horaNacimiento;
	/** Descripción con las características de la placenta */
	private String caracteristicasPlacenta;
	/** Talla (estatura) del infante, en centímetros */
	private int talla;
	/** Peso del niño, en gramos */
	private int peso;
	/** Perímetro cefálico del niño, en centímetros */
	private float perimetroCefalico;
	/** Perímetro torácico del niño, en centímetros */
	private float perimetroToracico;
	/** Edad Gestacional */
	private int ii_edadGestacional;
	/** Indicador de reanimación */
	private String is_reanimacion;
	/** Indicador de aspiración en reanimación */
	private String is_reanimacionAspiracion;
	/** Indicador de medicamentos en reanimación */
	private String is_reanimacionMedicamentos;
	/** Indicador de si nació o no sano */
	private String is_sano;
	/** Descripción del indicador de si nación o no sano */
	private String is_sanoDescripcion;
	/** Indicador de crecimiento intrauterino */
	private String is_intrauterinoPeg;
	/** Causa del indicador de crecimiento intrauterino */
	private String is_intrauterinoPegCausa;
	/** Causa de la anormalidad del crecimiento intrauterino */
	private String is_intrauterinoAnormalidadCausa;
	/** Indicador de crecimiento intrauterino armónico */
	private String is_intrauterinoArmonico;
	/** Causa del indicador de crecimiento intrauterino */
	private String is_intrauterinoArmonicoCausa;
	/** Indicador de liquido amniotico claro */
	private String is_liqAmnioticoClaro;
	/** Indicador de liquido amniotico meconiado */
	private String is_liqAmnioticoMeconiado;
	/** Grado del liquido amniotico meconiado */
	private String is_liqAmnioticoMeconiadoGrado;
	/** Indicador de liquido amniotico sanguinolento */
	private String is_liqAmnioticoSanguinolento;
	/** Indicador de liquido amniotico fetido */
	private String is_liqAmnioticoFetido;
	/** Indicador de toma de muestras del cordón umbilical */
	private String is_muestraCordonUmbilical;
	/** Descripción del indicador de toma de muestras del cordón umbilical */
	private String is_muestraCordonUmbilicalDescripcion;
	/** Caracteríscas de la placenta */
	private String is_placentaCaracteristicas;
	/** Descripción de las características de la placenta */
	private String is_placentaCaracteristicasDescripcion;
	/** Porcentaje de anormalidad del crecimiento intrauterino */
	private int ii_intrauterinoAnormalidad;
	/** Descripcion de las complicaciones del parto */
	private String is_complicacionesParto;
	/** Características del cordón umbilical */
	private String is_cordonUmbilicalCaracteristicas;
	/** Descripción del cordón umbilical */
	private String is_cordonUmbilicalDescripcion;
	/** Descripcion de la edad gestacional */
	private String is_edadGestacionalDescripcion;
	/** Frecuencia del control prenatal */
	private String is_frecuenciaControlPrenatal;
	/** Indicador de si es o no gemelo */
	private String is_gemelo;
	/** Descripción del atributo gemelo */
	private String is_gemeloDescripcion;
	/** Descripción del genero */
	private String is_sexoDescripcion;
	
	/****************APGAR**************************/
	/** Valor del APGAR en el minuto 1 (entre 1-10) */
		private int apgarMinuto1;
		/** Valor del APGAR en el minuto 5 (entre 1-10) */
		private int apgarMinuto5;
		/** Valor del APGAR en el minuto 10 (entre 1-10) */
		private int apgarMinuto10;
	/**
		* En la descripcion de InfoDatos debe estar 'true'
		* si se quiere que quede seleccionado-chequeado.
		*/
		private InfoDatos riesgoApgarMinuto1;
		/**
		* En la descripcion de InfoDatos debe estar 'true'
		* si se quiere que quede seleccionado-chequeado.
		*/
		private InfoDatos riesgoApgarMinuto5;
		/**
		* En la descripcion de InfoDatos debe estar 'true'
		* si se quiere que quede seleccionado-chequeado.
		*/
		private InfoDatos riesgoApgarMinuto10;
	
	
	/****************INMUNIZACIONES**************************/
	/** ArrayList de objetos InmunizacionesPediatricas */
	private ArrayList inmunizacionesPediatricas;
	
	
	
	/****************OBSERVACIONES**************************/
	/** Observaciones generales de este antecedente */
	private String observaciones;
	
	/** * Fecha */
	private Date fecha;
	
	/** * Hora */
	private String hora;

	
	
/*****************atributos manejo********************************/
/** Usuario básico actualmente logged en el sistema */
	private UsuarioBasico usuarioBasico;
	/** Datos básicos del paciente que se está atendiendo */
	private PersonaBasica paciente;
/**
	* El DAO usado por el objeto <code>AntecedentePediatrico</code>
	* para acceder a la fuente de datos.
	*/
	private static AntecedentePediatricoDao iaps_dao;




	public AntecedentePediatrico()
	{
		clean();
		init(System.getProperty("TIPOBD") );
	}

	@SuppressWarnings("unchecked")
	public void cargar(Connection con, int ai_codigoPaciente) throws SQLException
	{
		Dosis						ld_dosis;
		InmunizacionesPediatricas	li_inmunizaciones;
		int							li_aux;
		int							li_presentacionNacimiento;
		int							tipoInmunizacion_;
		int							tipoInmunizacion_ant;
		PersonaBasica				lpb_paciente;
		ResultSetDecorator					lrs_inmunizacionesDosis;
		ResultSetDecorator					lrs_inmunizacionesObser;
		ResultSetDecorator					lrs_embarazoOpciones;
		ResultSetDecorator					lrs_rs;
		ResultSetDecorator					lrs_tiposParto;
		String						ls_aux;
		String						ls_embarazoOpcionCodigo;
		String						ls_presentacionNacimiento;
		String						ls_tiposInmunizacion;
		String						ls_ubicacionFeto;

		clean();
// Ojo con la hora quitar los :. cambiar formato a la fecha

		/* Para cargar los datos básicos de antecedente pediatrico */
		ld_dosis				= null;
		li_inmunizaciones		= null;
		lpb_paciente			= new PersonaBasica();
		lrs_inmunizacionesDosis	= null;
		lrs_inmunizacionesObser	= null;
		lrs_rs					= iaps_dao.cargarAntecedentePediatrico(con, ai_codigoPaciente);
		lrs_tiposParto			= null;

		/*
			Para llevar la cuenta de las inmunizaciones que ya se les creo un objeto
			InmunizacionesPediatricas
		*/
		tipoInmunizacion_ant	= -2;
		ls_tiposInmunizacion	= "";

		if(lrs_rs.next() )
		{
/*
			OJO es necesario cargar el paciente, se supone que viene por sesion por tanto al cargar
			es mejor hacer set del atributo paciente de sesion
*/
			/* Cargando paciente */
			lpb_paciente.setCodigoPersona(ai_codigoPaciente);
			lpb_paciente.init(System.getProperty("TIPOBD") );
			lpb_paciente.cargar(con);
			setPaciente(lpb_paciente);

			/* Si hay una presentación se inicializa este atributo */
			if( (ls_presentacionNacimiento = lrs_rs.getString("presentacion_nacimiento") ) != null)
			{
				/* Averiguo el nombre de la presentación */
				li_presentacionNacimiento = new Integer(ls_presentacionNacimiento).intValue();
				setPresentacionNacimiento(
					new InfoDatos(
						ls_presentacionNacimiento,
						iaps_dao.consultarNombrePresentacionNacimiento(
							con,
							li_presentacionNacimiento
						)
					)
				);
			}
			/* Puede haber otra presentación nacimiento */
			else if( (ls_aux = lrs_rs.getString("otra_presentacion_nacimiento") )!= null)
				setOtraPresentacionNacimiento(ls_aux);

			/* Si hay una ubicación del feto se inicializa este atributo */
			if( (ls_ubicacionFeto = lrs_rs.getString("ubicacion_feto") ) != null)
			{
				/* Averiguo el nombre de la ubicación */
				setUbicacionFeto(
					new InfoDatos(
						ls_ubicacionFeto,
						iaps_dao.consultarNombreUbicacionFeto(
							con,
							Integer.parseInt(ls_ubicacionFeto)
						)
					)
				);
			}
			/* Puede haber otra ubicación del feto */
			else if( (ls_aux = lrs_rs.getString("otra_ubicacion_feto") ) != null)
				setOtraUbicacionFeto(ls_aux);

			if( (ls_aux = lrs_rs.getString("duracion_expulsivo_horas") ) != null)
				setDuracionExpulsivoHoras(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("duracion_expulsivo_mins") ) != null)
				setDuracionExpulsivoMinutos(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("duracion_parto_horas") ) != null)
				setDuracionPartoHoras(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("duracion_parto_mins") ) != null)
				setDuracionPartoMinutos(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("edad_gestacional") ) != null)
				setEdadGestacional(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("intrauterino_anormalidad") ) != null)
				setIntrauterinoAnormalidad(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("peso") ) != null)
				setPeso(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("ruptura_membranas_horas") ) != null)
				setRupturaMembranasHoras(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("ruptura_membranas_mins") ) != null)
				setRupturaMembranasMinutos(Integer.parseInt(ls_aux) );
			if( (ls_aux = lrs_rs.getString("talla") ) != null)
				setTalla(Integer.parseInt(ls_aux) );

			if( (ls_aux = lrs_rs.getString("perimetro_cefalico") ) != null)
				setPerimetroCefalico(Float.parseFloat(ls_aux) );
			if( (ls_aux = lrs_rs.getString("perimetro_toracico") ) != null)
				setPerimetroToracico(Float.parseFloat(ls_aux) );

			setAmnionitis(lrs_rs.getString("amnionitis") );
			setAmnionitisFactorAnormal(lrs_rs.getString("amnionitis_factor_anormal") );
			setAnestesia(lrs_rs.getString("anestesia") );
			setAnestesiaMedicamentos(lrs_rs.getString("anestesia_medicamentos") );
			setAnestesiaTipo(lrs_rs.getString("anestesia_tipo") );
			setCaracteristicasLiquidoAmniotico(lrs_rs.getString("caracteristicas_liq_amniotico") );
			setCaracteristicasPlacenta(lrs_rs.getString("caracteristicas_placenta") );
			setCodigoSerologia(lrs_rs.getInt("codigo_serologia") );
			setCodigoTestSullivan(lrs_rs.getInt("codigo_test_sullivan") );
			setComplicacionesParto(lrs_rs.getString("complicaciones_parto") );
			setControlPrenatal(lrs_rs.getString("control_prenatal") );
			setCordonUmbilicalCaracteristicas(lrs_rs.getString("cordon_umb_carac") );
			setCordonUmbilicalDescripcion(lrs_rs.getString("cordon_umbilical_descripcion") );
			setDescripcionSerologia(lrs_rs.getString("descripcion_serologia") );
			setDescripcionTestSullivan(lrs_rs.getString("descripcion_test_sullivan") );
			setEdadGestacionalDescripcion(lrs_rs.getString("edad_gestacional_descripcion") );
			setEmbarazosAnterioresOtros(lrs_rs.getString("embarazos_anteriores_otros") );
			setFrecuenciaControlPrenatal(lrs_rs.getString("frecuencia_control_prenatal") );
			setGemelo(lrs_rs.getString("gemelo") );
			setGemeloDescripcion(lrs_rs.getString("gemelo_descripcion") );
			setIncompatibilidadABO(lrs_rs.getString("incompatibilidad_abo") );
			setIncompatibilidadRH(lrs_rs.getString("incompatibilidad_rh") );
			setIntrauterinoPeg(lrs_rs.getString("intrauterino_peg") );
			setIntrauterinoPegCausa(lrs_rs.getString("intrauterino_peg_causa") );
			setIntrauterinoAnormalidadCausa(lrs_rs.getString("intrauterino_anormalidad_causa") );
			setIntrauterinoArmonico(lrs_rs.getString("intrauterino_armonico") );
			setIntrauterinoArmonicoCausa(lrs_rs.getString("intrauterino_armonico_causa") );
			setLiqAmnioticoClaro(lrs_rs.getString("liq_amniotico_claro") );
			setLiqAmnioticoMeconiado(lrs_rs.getString("liq_amniotico_meconiado") );
			setLiqAmnioticoMeconiadoGrado(lrs_rs.getString("liq_amniotico_meconiado_grado") );
			setLiqAmnioticoSanguinolento(lrs_rs.getString("liq_amniotico_sanguinolento") );
			setLiqAmnioticoFetido(lrs_rs.getString("liq_amniotico_fetido") );
			setLugarControlPrenatal(lrs_rs.getString("lugar_control_prenatal") );
			setMacrosomicos(lrs_rs.getString("macrosomicos") );
			setMalformacionesCongenitas(lrs_rs.getString("malformaciones_congenitas") );
			setMortinatos(lrs_rs.getString("mortinatos") );
			setMuertesFetalesTempranas(lrs_rs.getString("muertes_fetales_tempranas") );
			setMuestraCordonUmbilical(lrs_rs.getString("muestra_cordon_umbilical") );
			setMuestraCordonUmbilicalDescripcion(lrs_rs.getString("mues_cor_umbilical_des") );
			setNst(lrs_rs.getString("nst") );
			setNstDescripcion(lrs_rs.getString("nst_descripcion") );
			setObservaciones(lrs_rs.getString("observaciones") );
			setOtrosExamenesTrabajoParto(lrs_rs.getString("otros_examenes_trabajo_parto") );
			setPerfilBiofisico(lrs_rs.getString("perfil_biofisico") );
			setPerfilBiofisicoDescripcion(lrs_rs.getString("perfil_biofisico_descripcion") );
			setPlacentaCaracteristicas(lrs_rs.getString("placenta_caracteristicas") );
			setPlacentaCaracteristicasDescripcion(lrs_rs.getString("plac_catac_desc") );
			setPrematuros(lrs_rs.getString("prematuros") );
			setPtc(lrs_rs.getString("ptc") );
			setPtcDescripcion(lrs_rs.getString("ptc_descripcion") );
			setReanimacion(lrs_rs.getString("reanimacion") );
			setReanimacionAspiracion(lrs_rs.getString("reanimacion_aspiracion") );
			setReanimacionMedicamentos(lrs_rs.getString("reanimacion_medicamentos") );
			setSano(lrs_rs.getString("sano") );
			setSanoDescripcion(lrs_rs.getString("sano_descripcion") );
			setSexoDescripcion(lrs_rs.getString("sexo_descripcion") );

			if( (ls_aux = lrs_rs.getString("tiempo_nacimiento") ) != null)
				setHoraNacimiento(ls_aux.substring(0, ls_aux.lastIndexOf(':') ) );

			/* Si hay otro tipo de parto, inicializo este atributo y el motivo de otro tipo parto */
			if( (ls_aux = lrs_rs.getString("otro_tipo_parto") ) != null)
			{
				setOtroTipoParto(ls_aux);
				setMotivoTipoPartoOtro(lrs_rs.getString("motivo_tipo_parto") );
			}

			/* Buscar los otros tipos de parto asociados al antecedente pedáatrico */
			lrs_tiposParto = iaps_dao.consultarAntPediatricoTiposParto(con, ai_codigoPaciente);
			setTiposParto(new ArrayList() );

			while(lrs_tiposParto.next() )
				getTiposParto().add(new InfoDatosBD (lrs_tiposParto.getString("tipo_parto"), lrs_tiposParto.getString("nombre"), lrs_tiposParto.getString("motivo"), 't') );

			if(getTiposParto().size() == 0)
				setTiposParto(null);

			/* Si hay otro tipo trabajo parto, inicializo este atributo y el comentario de ese trabajo de parto */
			if( (ls_aux = lrs_rs.getString("tipo_trabajo_parto") ) != null)
				setTipoTrabajoParto(
					new InfoDatos(
						ls_aux,
						iaps_dao.consultarNombreTipoTrabajoParto(con, Integer.parseInt(ls_aux) ),
						lrs_rs.getString("comentario_tipo_trabajo_parto")
					)
				);

			/* Si se inicializo apgar debe consultar sus riesgos */
			if( (ls_aux = lrs_rs.getString("apgar_minuto1") ) != null)
			{
				setApgarMinuto1(li_aux = (Integer.parseInt(ls_aux) ) );
				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto1(
					new InfoDatos(
						ls_aux = lrs_rs.getString("riesgo_apgar_minuto1"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo apgar debe consultar sus riesgos */
			if( (ls_aux = lrs_rs.getString("apgar_minuto5") ) != null)
			{
				setApgarMinuto5(li_aux = (Integer.parseInt(ls_aux) ) );
				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto5(
					new InfoDatos(
						ls_aux = lrs_rs.getString("riesgo_apgar_minuto5"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo apgar debe consultar sus riesgos */
			if( (ls_aux = lrs_rs.getString("apgar_minuto10") ) != null)
			{
				setApgarMinuto10(li_aux = (Integer.parseInt(ls_aux) ) );
				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto10(
					new InfoDatos(
						ls_aux = lrs_rs.getString("riesgo_apgar_minuto10"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo sufrimiento fetal y fue true, se ponen las causas */
			if(lrs_rs.getString("sufrimiento_fetal") != null && lrs_rs.getBoolean("sufrimiento_fetal") )
			{
				setSufrimientoFetal("true");
				setCausasSufrimientoFetal(lrs_rs.getString("causas_sufrimiento_fetal") );
			}

			/* Ahora cargamos las inmunizaciones con sus dosis y/o observaciones */
			lrs_inmunizacionesDosis = iaps_dao.consultarAntPediatricoInmunizacionesDosis(con, ai_codigoPaciente);
			lrs_inmunizacionesObser = iaps_dao.consultarAntPediatricoInmunizacionesObser(con, ai_codigoPaciente);

			setInmunizacionesPediatricas(new ArrayList() );

			while(lrs_inmunizacionesDosis.next() )
			{
				if( (tipoInmunizacion_ = lrs_inmunizacionesDosis.getInt("tipo_inmunizacion") ) != tipoInmunizacion_ant)
				{
					li_inmunizaciones = new InmunizacionesPediatricas();
					li_inmunizaciones.setCodigoInmunizacion(tipoInmunizacion_);
					li_inmunizaciones.setNombreInmunizacion(lrs_inmunizacionesDosis.getString("nombre") );
					li_inmunizaciones.setObservaciones(iaps_dao.consultarObservacionInmunizacion(con, ai_codigoPaciente, tipoInmunizacion_));
					li_inmunizaciones.setDosis(new ArrayList() );
					li_inmunizaciones.setEstaEnBD('t');

					getInmunizacionesPediatricas().add(li_inmunizaciones);

					/* Agrego a la cadena de inmunizaciones con dosis el tipo de inmunización */
					ls_tiposInmunizacion += "," + tipoInmunizacion_ + ",";
				}

				ld_dosis = new Dosis();
				ld_dosis.setFecha(lrs_inmunizacionesDosis.getString("fecha") );

				li_aux = lrs_inmunizacionesDosis.getInt("numero_dosis");

				if(li_aux < 0)
					ld_dosis.setRefuerzo(true);

				ld_dosis.setNumeroDosis(li_aux);
				ld_dosis.setEstaEnBD('t');

				li_inmunizaciones.getDosis().add(ld_dosis);

				tipoInmunizacion_ant = tipoInmunizacion_;
			}

			if(getInmunizacionesPediatricas().size() == 0)
				setInmunizacionesPediatricas(null);

			/* Ahora buscando inmunizaciones que no tienen dosis pero que tienen observación */
			while(lrs_inmunizacionesObser.next() )
			{
				if(getInmunizacionesPediatricas() == null)
				{
					setInmunizacionesPediatricas(new ArrayList() );

					li_inmunizaciones = new InmunizacionesPediatricas();
					li_inmunizaciones.setCodigoInmunizacion(lrs_inmunizacionesObser.getInt("tipo_inmunizacion") );
					li_inmunizaciones.setNombreInmunizacion(lrs_inmunizacionesObser.getString("nombre") );
					li_inmunizaciones.setObservaciones(lrs_inmunizacionesObser.getString("observacion") );
					li_inmunizaciones.setEstaEnBD('t');

					getInmunizacionesPediatricas().add(li_inmunizaciones);
				}
				else
				{
					/* ver si esta ya en el arreglo de inmunizaciones pedíatricas */
					tipoInmunizacion_ = lrs_inmunizacionesObser.getInt("tipo_inmunizacion");

					if(ls_tiposInmunizacion.indexOf("," + tipoInmunizacion_ + ",") == ConstantesBD.codigoNuncaValido)
					{
						/* En caso de no estar ahi se debe crear la inmunizacion y agregarla */
						li_inmunizaciones = new InmunizacionesPediatricas();

						li_inmunizaciones.setCodigoInmunizacion(lrs_inmunizacionesObser.getInt("tipo_inmunizacion") );
						li_inmunizaciones.setNombreInmunizacion(lrs_inmunizacionesObser.getString("nombre") );
						li_inmunizaciones.setObservaciones(lrs_inmunizacionesObser.getString("observacion") );
						li_inmunizaciones.setEstaEnBD('t');

						getInmunizacionesPediatricas().add(li_inmunizaciones);
					}
				}
			}

			/* Cargar las opciones de embarazo */
			lrs_embarazoOpciones = iaps_dao.consultarEmbarazoOpciones(con, ai_codigoPaciente);

			while(lrs_embarazoOpciones.next() )
			{
				ls_embarazoOpcionCodigo =
					lrs_embarazoOpciones.getString("codigo_categoria_embarazo") + "-"	+
					lrs_embarazoOpciones.getString("cod_cat_embarazo_opcion")	+ "-";

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo1") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "1", ls_aux) );

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo2") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "2", ls_aux) );

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo3") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "3", ls_aux) );

				if(
					(ls_aux = lrs_embarazoOpciones.getString("existio") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "4", ls_aux) );
			}
		}
	}

	@SuppressWarnings("unchecked")
	public boolean cargar2(Connection con, int ai_codigoPaciente) throws SQLException
	{
		Dosis						laDosis;
		InmunizacionesPediatricas	inmunizaciones;
		int							li_aux;
		int							presentacionNacimientoInt;
		int							tipoInmunizacion_;
		int							tipoInmunizacion_ant;
		PersonaBasica				elPaciente;
		ResultSetDecorator					inmunizacionesDosis_rs;
		ResultSetDecorator					inmunizacionesObser_rs;
		ResultSetDecorator					lrs_embarazoOpciones;
		ResultSetDecorator					rs;
		ResultSetDecorator					tiposParto_rs;
		String						laObservacion;
		String						ls_aux;
		String						ls_embarazoOpcionCodigo;
		String						presentacionNacimientoStr;
		String						tiposInmunizacionStr;
		String						ubicacionFetoStr;

		clean();
		
		/* ojo con la hora quitar los ":" cambiar formato a la fecha */
		
		/* Para cargar los datos básicos de antecedente pediatrico */
		elPaciente				= new PersonaBasica();
		inmunizaciones			= null;
		inmunizacionesDosis_rs	= null;
		inmunizacionesObser_rs	= null;
		laDosis					= null;
		laObservacion			= "";
		rs						= iaps_dao.cargarAntecedentePediatrico(con, ai_codigoPaciente);
		tiposParto_rs			= null;
		tipoInmunizacion_ant	= -2;
		tiposInmunizacionStr	= "";

		
		if(rs.next() )
		{
			/*
			OJO es necesario cargar el paciente, se supone que viene por
			sesion por tanto al cargar es mejor hacer set del atributo
			paciente de sesion
 			*/
			
			/* Cargando paciente */
			elPaciente.setCodigoPersona(ai_codigoPaciente);
			setPaciente(elPaciente);

			/* Si hay una presentación se inicializa este atributo */
			presentacionNacimientoStr = rs.getString("presentacion_nacimiento")+"";
			if( !UtilidadTexto.isEmpty(presentacionNacimientoStr))
			{
				/* Averiguo el nombre de la presentación */
				presentacionNacimientoInt = new Integer(presentacionNacimientoStr).intValue();
				setPresentacionNacimiento(
					new InfoDatos(
						presentacionNacimientoStr,
						iaps_dao.consultarNombrePresentacionNacimiento(
							con,
							presentacionNacimientoInt
						)
					)
				);
			}
			/* Puede haber otra presentación nacimiento */
			else if( !UtilidadTexto.isEmpty(rs.getString("otra_presentacion_nacimiento")))
				setOtraPresentacionNacimiento(rs.getString("otra_presentacion_nacimiento"));

			/* Si hay una ubicación del feto se inicializa este atributo */
			ubicacionFetoStr = rs.getString("ubicacion_feto");
			if( !UtilidadTexto.isEmpty(ubicacionFetoStr))
			{
				/* Averiguo el nombre de la ubicación */
				setUbicacionFeto(
					new InfoDatos(
						ubicacionFetoStr,
						iaps_dao.consultarNombreUbicacionFeto(
							con,
							Integer.parseInt(ubicacionFetoStr)
						)
					)
				);
			}
			/* Puede haber otra ubicación del feto */
			else if(!UtilidadTexto.isEmpty(rs.getString("otra_ubicacion_feto")))
				setOtraUbicacionFeto(rs.getString("otra_ubicacion_feto"));

			if( !UtilidadTexto.isEmpty( rs.getString("duracion_expulsivo_horas") ))
				setDuracionExpulsivoHoras(Integer.parseInt(rs.getString("duracion_expulsivo_horas")) );
			if( !UtilidadTexto.isEmpty( rs.getString("duracion_expulsivo_mins") ) )
				setDuracionExpulsivoMinutos(Integer.parseInt(rs.getString("duracion_expulsivo_mins")) );
			if( !UtilidadTexto.isEmpty( rs.getString("duracion_parto_horas") ) )
				setDuracionPartoHoras(Integer.parseInt(rs.getString("duracion_parto_horas")) );
			if( !UtilidadTexto.isEmpty( rs.getString("duracion_parto_mins") ) )
				setDuracionPartoMinutos(Integer.parseInt(rs.getString("duracion_parto_mins")) );
			if( !UtilidadTexto.isEmpty( rs.getString("edad_gestacional") ) )
				setEdadGestacional(Integer.parseInt(rs.getString("edad_gestacional")) );
			if( !UtilidadTexto.isEmpty( rs.getString("intrauterino_anormalidad") ) )
				setIntrauterinoAnormalidad(Integer.parseInt(rs.getString("intrauterino_anormalidad")) );
			if( !UtilidadTexto.isEmpty( rs.getString("peso") ) )
				setPeso(Integer.parseInt(rs.getString("peso")) );
			if( !UtilidadTexto.isEmpty( rs.getString("ruptura_membranas_horas") ) )
				setRupturaMembranasHoras(Integer.parseInt(rs.getString("ruptura_membranas_horas")) );
			if( !UtilidadTexto.isEmpty( rs.getString("ruptura_membranas_mins") ) )
				setRupturaMembranasMinutos(Integer.parseInt(rs.getString("ruptura_membranas_mins")) );
			if( !UtilidadTexto.isEmpty( rs.getString("talla") ) )
				setTalla(Integer.parseInt(rs.getString("talla")) );

			if( !UtilidadTexto.isEmpty( rs.getString("perimetro_cefalico") ) )
				setPerimetroCefalico(Float.parseFloat(rs.getString("perimetro_cefalico")) );
			if( !UtilidadTexto.isEmpty( rs.getString("perimetro_toracico") ) )
				setPerimetroToracico(Float.parseFloat(rs.getString("perimetro_toracico")) );

			setAmnionitis(rs.getString("amnionitis") );
			setAmnionitisFactorAnormal(rs.getString("amnionitis_factor_anormal") );
			setAnestesia(rs.getString("anestesia") );
			setAnestesiaMedicamentos(rs.getString("anestesia_medicamentos") );
			setAnestesiaTipo(rs.getString("anestesia_tipo") );
			setCaracteristicasLiquidoAmniotico(rs.getString("caracteristicas_liq_amniotico") );
			setCaracteristicasPlacenta(rs.getString("caracteristicas_placenta") );
			setCodigoSerologia(rs.getInt("codigo_serologia") );
			setCodigoTestSullivan(rs.getInt("codigo_test_sullivan") );
			setComplicacionesParto(rs.getString("complicaciones_parto") );
			setControlPrenatal(rs.getString("control_prenatal") );
			setCordonUmbilicalCaracteristicas(rs.getString("cordon_umb_carac") );
			setCordonUmbilicalDescripcion(rs.getString("cordon_umbilical_descripcion") );
			setDescripcionSerologia(rs.getString("descripcion_serologia") );
			setDescripcionTestSullivan(rs.getString("descripcion_test_sullivan") );
			setEdadGestacionalDescripcion(rs.getString("edad_gestacional_descripcion") );
			setEmbarazosAnterioresOtros(rs.getString("embarazos_anteriores_otros") );
			setFrecuenciaControlPrenatal(rs.getString("frecuencia_control_prenatal") );
			setGemelo(rs.getString("gemelo") );
			setGemeloDescripcion(rs.getString("gemelo_descripcion") );
			setIncompatibilidadABO(rs.getString("incompatibilidad_abo") );
			setIncompatibilidadRH(rs.getString("incompatibilidad_rh") );
			setIntrauterinoPeg(rs.getString("intrauterino_peg") );
			setIntrauterinoPegCausa(rs.getString("intrauterino_peg_causa") );
			setIntrauterinoAnormalidadCausa(rs.getString("intrauterino_anormalidad_causa") );
			setIntrauterinoArmonico(rs.getString("intrauterino_armonico") );
			setIntrauterinoArmonicoCausa(rs.getString("intrauterino_armonico_causa") );
			setLiqAmnioticoClaro(rs.getString("liq_amniotico_claro") );
			setLiqAmnioticoMeconiado(rs.getString("liq_amniotico_meconiado") );
			setLiqAmnioticoMeconiadoGrado(rs.getString("liq_amniotico_meconiado_grado") );
			setLiqAmnioticoSanguinolento(rs.getString("liq_amniotico_sanguinolento") );
			setLiqAmnioticoFetido(rs.getString("liq_amniotico_fetido") );
			setLugarControlPrenatal(rs.getString("lugar_control_prenatal") );
			setMacrosomicos(rs.getString("macrosomicos") );
			setMalformacionesCongenitas(rs.getString("malformaciones_congenitas") );
			setMortinatos(rs.getString("mortinatos") );
			setMuertesFetalesTempranas(rs.getString("muertes_fetales_tempranas") );
			setMuestraCordonUmbilical(rs.getString("muestra_cordon_umbilical") );
			setMuestraCordonUmbilicalDescripcion(rs.getString("mues_cor_umbilical_des") );
			setNst(rs.getString("nst") );
			setNstDescripcion(rs.getString("nst_descripcion") );
			setObservaciones(rs.getString("observaciones") );
			setOtrosExamenesTrabajoParto(rs.getString("otros_examenes_trabajo_parto") );
			setPlacentaCaracteristicas(rs.getString("placenta_caracteristicas") );
			setPlacentaCaracteristicasDescripcion(rs.getString("plac_catac_desc") );
			setPerfilBiofisico(rs.getString("perfil_biofisico") );
			setPerfilBiofisicoDescripcion(rs.getString("perfil_biofisico_descripcion") );
			setPrematuros(rs.getString("prematuros") );
			setPtc(rs.getString("ptc") );
			setPtcDescripcion(rs.getString("ptc_descripcion") );
			setReanimacion(rs.getString("reanimacion") );
			setReanimacionAspiracion(rs.getString("reanimacion_aspiracion") );
			setReanimacionMedicamentos(rs.getString("reanimacion_medicamentos") );
			setSano(rs.getString("sano") );
			setSanoDescripcion(rs.getString("sano_descripcion") );
			setSexoDescripcion(rs.getString("sexo_descripcion") );

			if( !UtilidadTexto.isEmpty(rs.getString("tiempo_nacimiento") ) )
			{
				ls_aux= rs.getString("tiempo_nacimiento");
				setHoraNacimiento(ls_aux.substring(0, ls_aux.lastIndexOf(':') ) );
			}	
			/* Si hay otro tipo de parto, inicializo este atributo y el motivo de otro tipo parto */
			if( UtilidadTexto.isEmpty(rs.getString("otro_tipo_parto") ))
			{
				ls_aux = rs.getString("otro_tipo_parto");
				setOtroTipoParto(ls_aux);
				setMotivoTipoPartoOtro(rs.getString("motivo_tipo_parto") );
			}
			
			if(!UtilidadTexto.isEmpty(rs.getString("fecha")+"")){
				setFecha(UtilidadFecha.conversionFormatoFechaStringDate(rs.getString("fecha")+""));
			}
			if(!UtilidadTexto.isEmpty(rs.getString("hora")+"")){
				setHora(rs.getString("hora")+"");
			}
			
			
			
			/* Buscar los otros tipos de parto asociados al antecedente pedáatrico */
			tiposParto_rs = iaps_dao.consultarAntPediatricoTiposParto(con, ai_codigoPaciente);
			setTiposParto(new ArrayList() );

			if(tiposParto_rs != null) 
			{
				while(tiposParto_rs.next() )
				{
					getTiposParto().add( 
						new InfoDatosBD(
							tiposParto_rs.getString("tipo_parto"),
							tiposParto_rs.getString("nombre"),
							tiposParto_rs.getString("motivo"),
							't'
						)
					);
				}
			}
			
			if(getTiposParto().size() == 0)
				setTiposParto(null);
			

			/* Si hay otro tipo trabajo parto, inicializo este atributo y el comentario de ese trabajo de parto */
			if( !UtilidadTexto.isEmpty( rs.getString("tipo_trabajo_parto") ) )
			{
				ls_aux=rs.getString("tipo_trabajo_parto");
			
				setTipoTrabajoParto
				(
					new InfoDatos
					(
						ls_aux,
						iaps_dao.consultarNombreTipoTrabajoParto(con, Integer.parseInt(ls_aux) ),
						rs.getString("comentario_tipo_trabajo_parto")
					)
				);
			}	
			/* Si se inicializo apgar debe consultar sus riesgos */
			if( !UtilidadTexto.isEmpty(rs.getString("apgar_minuto1")))
			{
				ls_aux = rs.getString("apgar_minuto1");
				setApgarMinuto1(li_aux = (Integer.parseInt(ls_aux) ) );

				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto1(
					new InfoDatos(
						ls_aux = rs.getString("riesgo_apgar_minuto1"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo apgar debe consultar sus riesgos */
			if( !UtilidadTexto.isEmpty(rs.getString("apgar_minuto5")))
			{
				ls_aux = rs.getString("apgar_minuto5");
				setApgarMinuto5(li_aux = (Integer.parseInt(ls_aux) ) );
				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto5(
					new InfoDatos(
						ls_aux = rs.getString("riesgo_apgar_minuto5"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo apgar debe consultar sus riesgos */
			if( !UtilidadTexto.isEmpty(rs.getString("apgar_minuto10")))
			{
				ls_aux = rs.getString("apgar_minuto10");
				setApgarMinuto10(li_aux = (Integer.parseInt(ls_aux) ) );
				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto10(
					new InfoDatos(
						ls_aux = rs.getString("riesgo_apgar_minuto10"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo sufrimiento fetal y fue true, se ponen las causas */
			if( !UtilidadTexto.isEmpty(rs.getString("sufrimiento_fetal")) && rs.getBoolean("sufrimiento_fetal") )
			{
				setSufrimientoFetal("true");
				setCausasSufrimientoFetal(rs.getString("causas_sufrimiento_fetal") );
			}
			else if(!UtilidadTexto.isEmpty(rs.getString("sufrimiento_fetal")) && !rs.getBoolean("sufrimiento_fetal") )
				setSufrimientoFetal("false");

			/* Ahora cargamos las inmunizaciones con sus dosis y/o observaciones */
			inmunizacionesDosis_rs = iaps_dao.consultarAntPediatricoInmunizacionesDosis(con, ai_codigoPaciente);
			inmunizacionesObser_rs = iaps_dao.consultarAntPediatricoInmunizacionesObser(con, ai_codigoPaciente);

			setInmunizacionesPediatricas(new ArrayList() );

			while(inmunizacionesDosis_rs.next() )
			{
				if( (tipoInmunizacion_ = inmunizacionesDosis_rs.getInt("tipo_inmunizacion") ) != tipoInmunizacion_ant)
				{
					inmunizaciones = new InmunizacionesPediatricas();
					inmunizaciones.setCodigoInmunizacion(tipoInmunizacion_);
					inmunizaciones.setNombreInmunizacion(inmunizacionesDosis_rs.getString("nombre") );

					laObservacion = iaps_dao.consultarObservacionInmunizacion(con, ai_codigoPaciente, tipoInmunizacion_);

					/* Si no es cadena vacia es porque se encontraba en la bd, se debe asignar y poner estaEnBD en true */
					if(!laObservacion.equals("") )
					{
						inmunizaciones.setObservaciones(laObservacion);
						inmunizaciones.setEstaEnBD('t');
					}

					inmunizaciones.setDosis(new ArrayList() );

					getInmunizacionesPediatricas().add(inmunizaciones);

					/* Agrego a la cadena de inmunizaciones con dosis el tipo de inmunización */
					tiposInmunizacionStr += "," + tipoInmunizacion_ + ",";
				}

				laDosis = new Dosis();
				laDosis.setFecha(inmunizacionesDosis_rs.getString("fecha") );

				li_aux = inmunizacionesDosis_rs.getInt("numero_dosis");

				if(li_aux < 0)
					laDosis.setRefuerzo(true);

				laDosis.setNumeroDosis(li_aux);
				laDosis.setEstaEnBD('t');

				inmunizaciones.getDosis().add(laDosis);

				tipoInmunizacion_ant = tipoInmunizacion_;
			}

			if(getInmunizacionesPediatricas().size() == 0)
				setInmunizacionesPediatricas(null);

			/* Ahora buscando inmunizaciones que no tienen dosis pero que tienen observación */
			while(inmunizacionesObser_rs.next() )
			{
				if(getInmunizacionesPediatricas() == null)
				{
					setInmunizacionesPediatricas(new ArrayList() );

					inmunizaciones = new InmunizacionesPediatricas();
					inmunizaciones.setCodigoInmunizacion(inmunizacionesObser_rs.getInt("tipo_inmunizacion") );
					inmunizaciones.setNombreInmunizacion(inmunizacionesObser_rs.getString("nombre") );
					inmunizaciones.setObservaciones(inmunizacionesObser_rs.getString("observacion") );
					inmunizaciones.setEstaEnBD('t');

					getInmunizacionesPediatricas().add(inmunizaciones);
				}
				else
				{
					/* ver si esta ya en el arreglo de inmunizaciones pedíatricas */
					tipoInmunizacion_ = inmunizacionesObser_rs.getInt("tipo_inmunizacion");

					if(tiposInmunizacionStr.indexOf("," + tipoInmunizacion_ + ",") == ConstantesBD.codigoNuncaValido)
					{
						/* En caso de no estar ahi se debe crear la inmunizacion y agregarla */
						inmunizaciones = new InmunizacionesPediatricas();

						inmunizaciones.setCodigoInmunizacion(inmunizacionesObser_rs.getInt("tipo_inmunizacion") );
						inmunizaciones.setNombreInmunizacion(inmunizacionesObser_rs.getString("nombre") );
						inmunizaciones.setObservaciones(inmunizacionesObser_rs.getString("observacion") );
						inmunizaciones.setEstaEnBD('t');

						getInmunizacionesPediatricas().add(inmunizaciones);
					}
				}
			}

			/* Cargar las opciones de embarazo */
			lrs_embarazoOpciones = iaps_dao.consultarEmbarazoOpciones(con, ai_codigoPaciente);

			while(lrs_embarazoOpciones.next() )
			{
				ls_embarazoOpcionCodigo =
					lrs_embarazoOpciones.getString("codigo_categoria_embarazo") + "-"	+
					lrs_embarazoOpciones.getString("cod_cat_embarazo_opcion")	+ "-";

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo1") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "1", ls_aux) );

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo2") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "2", ls_aux) );

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo3") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "3", ls_aux) );

				if(
					(ls_aux = lrs_embarazoOpciones.getString("existio") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "4", ls_aux) );
			}

			return true;
		}

		return false;
	}

	
	
	/**
	* Este método inicializa en valores vacíos, mas no-nulos los atributos de este objeto.
	*/
	public void clean()
	{
		/**validadores seccion**/
		/*
		 * no se usuan
		this.existeAntecedenteMaterno=false;
		this.existeAntecedentePaterno=false;
		this.existeEmbarazosAnteriores=false;
		this.existeEmbarazoActual=false;
		this.existeTrabajoParto=false;
		this.existeAtencionParto=false;
		this.existeApgar=false;
		this.existeInmunizaciones=false;
		this.existeObservaciones=false;
		*/		
		
		anestesiaMedicamentos			= "";
		apgarMinuto1					= ConstantesBD.codigoNuncaValido;
		apgarMinuto5					= ConstantesBD.codigoNuncaValido;
		apgarMinuto10					= ConstantesBD.codigoNuncaValido;
		caracteristicasLiquidoAmniotico	= "";
		caracteristicasPlacenta			= "";
		causasSufrimientoFetal			= "";
		duracionPartoHoras				= ConstantesBD.codigoNuncaValido;
		duracionPartoMinutos			= ConstantesBD.codigoNuncaValido;
		horaNacimiento					= "";
		infoMadre						= new InfoMadre();
		infoPadre					= new InfoPadre();
		inmunizacionesPediatricas		= null;
		motivoTipoPartoOtro				= "";
		observaciones					= "";
		otraPresentacionNacimiento		= "";
		otraUbicacionFeto				= "";
		otroTipoParto					= "";
		paciente						= new PersonaBasica();
		peso							= ConstantesBD.codigoNuncaValido;
		perimetroCefalico				= ConstantesBD.codigoNuncaValido;
		perimetroToracico				= ConstantesBD.codigoNuncaValido;
		presentacionNacimiento			= null;
		riesgoApgarMinuto1				= null;
		riesgoApgarMinuto5				= null;
		riesgoApgarMinuto10				= null;
		rupturaMembranasHoras			= ConstantesBD.codigoNuncaValido;
		rupturaMembranasMinutos			= ConstantesBD.codigoNuncaValido;
		sufrimientoFetal				= "";
		talla							= ConstantesBD.codigoNuncaValido;
		tiposParto						= null;
		tipoTrabajoParto				= null;
		ubicacionFeto					= null;

		/* Información de embarazos anteriores */
		is_embarazosAnterioresOtros	= "";
		is_incompatibilidadABO		= "";
		is_incompatibilidadRH		= "";
		is_macrosomicos				= "";
		is_malformacionesCongenitas	= "";
		is_mortinatos				= "";
		is_muertesFetalesTempranas	= "";
		is_prematuros				= "";

		/* Infomración de embarazo actual */
		ii_codigoSerologia				= ConstantesBD.codigoNuncaValido;
		ii_codigoTestSullivan			= ConstantesBD.codigoNuncaValido;
		controlPrenatal				= "";
		is_descripcionSerologia			= "";
		is_descripcionTestSullivan		= "";
		is_frecuenciaControlPrenatal	= "";
		lugarControlPrenatal			= "";

		/* Información de los campos de las opciones embarazos */
		categoriaEmbarazoOpcionCampo.clear();

		ii_duracionExpulsivoHoras		= ConstantesBD.codigoNuncaValido;
		ii_duracionExpulsivoMinutos		= ConstantesBD.codigoNuncaValido;
		is_amnionitis					= "";
		is_amnionitisFactorAnormal		= "";
		is_anestesia					= "";
		is_anestesiaTipo				= "";
		is_nst							= "";
		is_nstDescripcion				= "";
		is_otrosExamenesTrabajoParto	= "";
		is_perfilBiofisico				= "";
		is_perfilBiofisicoDescripcion	= "";
		is_ptc							= "";
		is_ptcDescripcion				= "";

		/* Información de atención del parto */
		ii_intrauterinoAnormalidad				= ConstantesBD.codigoNuncaValido;
		is_complicacionesParto					= "";
		is_cordonUmbilicalCaracteristicas		= "";
		is_cordonUmbilicalDescripcion			= "";
		is_gemelo								= "";
		is_gemeloDescripcion					= "";
		is_intrauterinoPeg						= "";
		is_intrauterinoPegCausa					= "";
		is_intrauterinoAnormalidadCausa			= "";
		is_intrauterinoArmonico					= "";
		is_intrauterinoArmonicoCausa			= "";
		is_muestraCordonUmbilical				= "";
		is_muestraCordonUmbilicalDescripcion	= "";
		is_reanimacion							= "";
		is_reanimacionAspiracion				= "";
		is_reanimacionMedicamentos				= "";
		is_sano									= "";
		is_sanoDescripcion						= "";
		is_sexoDescripcion						= "";

		ii_edadGestacional						= ConstantesBD.codigoNuncaValido;
		is_edadGestacionalDescripcion			= "";
		is_liqAmnioticoClaro					= "";
		is_liqAmnioticoMeconiado				= "";
		is_liqAmnioticoMeconiadoGrado			= "";
		is_liqAmnioticoSanguinolento			= "";
		is_liqAmnioticoFetido					= "";
		is_placentaCaracteristicas				= "";
		is_placentaCaracteristicasDescripcion	= "";
	}

	public InmunizacionesPediatricas darInmunizacionPediatrica(int codigoInmunizacion)
	{
		if(inmunizacionesPediatricas == null)
			return null;

		for(int i = 0; i < inmunizacionesPediatricas.size(); i++)
			if( ( (InmunizacionesPediatricas)inmunizacionesPediatricas.get(i) ).getCodigoInmunizacion() == codigoInmunizacion)
				return (InmunizacionesPediatricas)inmunizacionesPediatricas.get(i);

		return null;
	}

	public Dosis darDosisInmunizacionPediatrica(InmunizacionesPediatricas inmunizacion, int numDosis)
	{
		if(inmunizacion.getDosis() == null)
			return null;

		for(int i = 0; i < inmunizacion.getDosis().size(); i++)
			if( ( (Dosis)inmunizacion.getDosis().get(i) ).getNumeroDosis() == numDosis)
				return (Dosis)inmunizacion.getDosis().get(i);

		return null;
	}

	public boolean estaTipoParto(String codigoParto)
	{
		/* Caso otro tipo de parto */
		if(codigoParto.equals("0") && !otroTipoParto.equals("") )
			return true;

		if(tiposParto == null)
			return false;

		/* Buscando en el resto de partos */
		for(int i = 0; i < tiposParto.size(); i++)
			if( ( (InfoDatosBD)tiposParto.get(i) ).getAcronimo().equals(codigoParto) )
				return true;

		return false;
	}

	/** Obtiene el indicador de amnionitis en el trabajo de parto */
	public String getAmnionitis()
	{
		return is_amnionitis;
	}

	/** Obtiene el factor anormal de amnionitis en el trabajo de parto */
	public String getAmnionitisFactorAnormal()
	{
		return is_amnionitisFactorAnormal;
	}

	/** Obtiene el indicador uso de anestesia en el trabajo de parto */
	public String getAnestesia()
	{
		return is_anestesia;
	}

	/** Retorna la anestesiaMedicamentos */
	public String getAnestesiaMedicamentos()
	{
		return anestesiaMedicamentos;
	}

	/**
	* Retorna la anestesiaMedicamentos.
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getAnestesiaMedicamentos(boolean encoded)
	{
		return encoded ? Encoder.encode(anestesiaMedicamentos): anestesiaMedicamentos;
	}

	/** Obtiene el tipo de anestesia usada en el trabajo de parto */
	public String getAnestesiaTipo()
	{
		return is_anestesiaTipo;
	}

	public int getApgarMinuto1()
	{
		return apgarMinuto1;
	}

	public int getApgarMinuto5()
	{
		return apgarMinuto5;
	}

	public int getApgarMinuto10()
	{
		return apgarMinuto10;
	}

	public String getCaracteristicasLiquidoAmniotico()
	{
		return caracteristicasLiquidoAmniotico;
	}

	/**
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getCaracteristicasLiquidoAmniotico(boolean encoded)
	{
		return encoded ? Encoder.encode(caracteristicasLiquidoAmniotico): caracteristicasLiquidoAmniotico;
	}

	public String getCaracteristicasPlacenta()
	{
		return caracteristicasPlacenta;
	}

	/**
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getCaracteristicasPlacenta(boolean encoded)
	{
		return encoded ? Encoder.encode(caracteristicasPlacenta): caracteristicasPlacenta;
	}

	public String getCausasSufrimientoFetal()
	{
		return causasSufrimientoFetal;
	}

	/**
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getCausasSufrimientoFetal(boolean encoded)
	{
		return encoded ? Encoder.encode(causasSufrimientoFetal): causasSufrimientoFetal;
	}

	/** Obtiene el codigo de la serología del embarazo actual */
	public int getCodigoSerologia()
	{
		return ii_codigoSerologia;
	}

	/** Obtiene el codigo del test de O'Sullivan del embarazo actual */
	public int getCodigoTestSullivan()
	{
		return ii_codigoTestSullivan;
	}

	/** Obtiene la descripcion de las complicaciones del parto */
	public String getComplicacionesParto()
	{
		return is_complicacionesParto;
	}

	/** Obtiene el control prenatal */
	public String getControlPrenatal()
	{
		return controlPrenatal;
	}

	/** Obtiene las características del cordón umbilical */
	public String getCordonUmbilicalCaracteristicas()
	{
		return is_cordonUmbilicalCaracteristicas;
	}

	/** Obtiene la descripción del cordón umbilical */
	public String getCordonUmbilicalDescripcion()
	{
		return is_cordonUmbilicalDescripcion;
	}

	/** Obtiene la descripción de la serología del embarazo actual */
	public String getDescripcionSerologia()
	{
		return is_descripcionSerologia;
	}

	/** Obtiene la descripción del test de O'Sullivan del embarazo actual */
	public String getDescripcionTestSullivan()
	{
		return is_descripcionTestSullivan;
	}

	/** Obtiene el número de horas de duración del expulsivo */
	public int getDuracionExpulsivoHoras()
	{
		return ii_duracionExpulsivoHoras;
	}

	/** Obtiene el número de minutos de duración del expulsivo */
	public int getDuracionExpulsivoMinutos()
	{
		return ii_duracionExpulsivoMinutos;
	}

	public int getDuracionPartoHoras()
	{
		return duracionPartoHoras;
	}

	public int getDuracionPartoMinutos()
	{
		return duracionPartoMinutos;
	}

	/** Obtiene la edad gestacional */
	public int getEdadGestacional()
	{
		return ii_edadGestacional;
	}

	/** Obtiene descripcion de la edad gestacional */
	public String getEdadGestacionalDescripcion()
	{
		return is_edadGestacionalDescripcion;
	}

	/** Obtiene el valor de el campo otros de la sección de embarazos anteriores */
	public String getEmbarazosAnterioresOtros()
	{
		return is_embarazosAnterioresOtros;
	}

	/** Obtiene los valores de los campos de la opciones de embarazo */
	public ArrayList getEmbarazoOpcionCampos()
	{
		return categoriaEmbarazoOpcionCampo;
	}

	/**
	* Obtiene el valor del i-ésimo campo de las opciones
	* de embarazo
	* @param ai_i i-ésimo identificador-valor a obtener
	*/
	public InfoDatos getEmbarazoOpcionCampo(int ai_i)
	{
		return (InfoDatos)categoriaEmbarazoOpcionCampo.get(ai_i);
	}

	/** Obtiene la frecuencia del control prenatal */
	public String getFrecuenciaControlPrenatal()
	{
		return is_frecuenciaControlPrenatal;
	}

	/** Obtiene le indicador de si es o no gemelo */
	public String getGemelo()
	{
		return is_gemelo;
	}

	/** Obtiene la descripción del atributo gemelo */
	public String getGemeloDescripcion()
	{
		return is_gemeloDescripcion;
	}

	public String getHoraNacimiento()
	{
		return horaNacimiento;
	}

	/** Obtiene el valor de el campo Incompatibilidad ABO de la sección de embarazos anteriores */
	public String getIncompatibilidadABO()
	{
		return is_incompatibilidadABO;
	}

	/** Obtiene el valor de el campo Incompatibilidad RH de la sección de embarazos anteriores */
	public String getIncompatibilidadRH()
	{
		return is_incompatibilidadRH;
	}

	/** Obtiene la información de la madre */
	public InfoMadre getInfoMadre()
	{
		return infoMadre;
	}

	/** Obtiene la información del padre */
	public InfoPadre getInfoPadre()
	{
		return infoPadre;
	}

	public ArrayList getInmunizacionesPediatricas()
	{
		return inmunizacionesPediatricas;
	}

	/** Obtiene el indicador de crecimiento intrauterino */
	public String getIntrauterinoPeg()
	{
		return is_intrauterinoPeg;
	}

	/** Obtiene el indicador de crecimiento intrauterino */
	public String getIntrauterinoPegCausa()
	{
		return is_intrauterinoPegCausa;
	}

	/** Obtiene el porcentaje de anormalidad del crecimiento intrauterino */
	public int getIntrauterinoAnormalidad()
	{
		return ii_intrauterinoAnormalidad;
	}

	/** Obtiene la causa de la anormalidad del crecimiento intrauterino */
	public String getIntrauterinoAnormalidadCausa()
	{
		return is_intrauterinoAnormalidadCausa;
	}

	/** Obtiene el indicador de crecimiento intrauterino armónico */
	public String getIntrauterinoArmonico()
	{
		return is_intrauterinoArmonico;
	}

	/** Obtiene la causa del indicador de crecimiento intrauterino */
	public String getIntrauterinoArmonicoCausa()
	{
		return is_intrauterinoArmonicoCausa;
	}

	/** Obtiene el indicador de liquido amniotico claro */
	public String getLiqAmnioticoClaro()
	{
		return is_liqAmnioticoClaro;
	}

	/** Obtiene indicador de liquido amniotico meconiado */
	public String getLiqAmnioticoMeconiado()
	{
		return is_liqAmnioticoMeconiado;
	}

	/** Obtiene el grado del liquido amniotico meconiado */
	public String getLiqAmnioticoMeconiadoGrado()
	{
		return is_liqAmnioticoMeconiadoGrado;
	}

	/** Obtiene indicador de liquido amniotico sanguinolento */
	public String getLiqAmnioticoSanguinolento()
	{
		return is_liqAmnioticoSanguinolento;
	}

	/** Obtiene indicador de liquido amniotico fetido */
	public String getLiqAmnioticoFetido()
	{
		return is_liqAmnioticoFetido;
	}

	/** Obtiene el lugar del control prenatal */
	public String getLugarControlPrenatal()
	{
		return lugarControlPrenatal;
	}

	/** Obtiene el valor de el campo Macrosómicos de la sección de embarazos anteriores */
	public String getMacrosomicos()
	{
		return is_macrosomicos;
	}

	/** Obtiene el valor de el campo Malformaciones Congénitas de la sección de embarazos anteriores */
	public String getMalformacionesCongenitas()
	{
		return is_malformacionesCongenitas;
	}

	/** Obtiene el valor de el campo Mortinatos de la sección de embarazos anteriores */
	public String getMortinatos()
	{
		return is_mortinatos;
	}

	public String getMotivoTipoPartoOtro()
	{
		return motivoTipoPartoOtro;
	}

	/**
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getMotivoTipoPartoOtro(boolean encoded)
	{
		return encoded ? Encoder.encode(motivoTipoPartoOtro): motivoTipoPartoOtro;
	}

	/** Obtiene el valor de el campo Muertes Fetales Tempranas de la sección de embarazos anteriores */
	public String getMuertesFetalesTempranas()
	{
		return is_muertesFetalesTempranas;
	}

	/** Obtiene el indicador de muestras del cordón umbilical */
	public String getMuestraCordonUmbilical()
	{
		return is_muestraCordonUmbilical;
	}

	/** Obtiene la descripción del indicador de muestras del cordón umbilical */
	public String getMuestraCordonUmbilicalDescripcion()
	{
		return is_muestraCordonUmbilicalDescripcion;
	}

	/** Obtiene el nst del trabajo de parto */
	public String getNst()
	{
		return is_nst;
	}

	/** Obtiene la descripción de nst del trabajo de parto */
	public String getNstDescripcion()
	{
		return is_nstDescripcion;
	}

	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getObservaciones(boolean encoded)
	{
		return encoded ? Encoder.encode(observaciones): observaciones;
	}

	public String getOtraPresentacionNacimiento()
	{
		return otraPresentacionNacimiento;
	}

	/**
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getOtraPresentacionNacimiento(boolean encoded)
	{
		return encoded ? Encoder.encode(otraPresentacionNacimiento): otraPresentacionNacimiento;
	}

	public String getOtraUbicacionFeto()
	{
		return otraUbicacionFeto;
	}

	/** Obtiene los otros tipos de exámenes del trabajo de parto */
	public String getOtrosExamenesTrabajoParto()
	{
		return is_otrosExamenesTrabajoParto;
	}

	public String getOtroTipoParto()
	{
		return otroTipoParto;
	}

	/**
	* @param encoded especifica si se debe o no retornar esta cadena como
	* <i>character entities</i> de HTML (e.g., "á" como "&amp;aacute;"
	*/
	public String getOtroTipoParto(boolean encoded)
	{
		return encoded ? Encoder.encode(otroTipoParto): otroTipoParto;
	}

	public PersonaBasica getPaciente()
	{
		return paciente;
	}

	/** Obtiene el perfil biofísico del trabajo de parto */
	public String getPerfilBiofisico()
	{
		return is_perfilBiofisico;
	}

	/** Obtiene la descripción del perfil biofísico del trabajo de parto */
	public String getPerfilBiofisicoDescripcion()
	{
		return is_perfilBiofisicoDescripcion;
	}

	public float getPerimetroCefalico()
	{
		return perimetroCefalico;
	}

	public float getPerimetroToracico()
	{
		return perimetroToracico;
	}

	public int getPeso()
	{
		return peso;
	}

	/** Obtiene características de la placenta */
	public String getPlacentaCaracteristicas()
	{
		return is_placentaCaracteristicas;
	}

	/** Obtiene descripción de las características de la placenta */
	public String getPlacentaCaracteristicasDescripcion()
	{
		return is_placentaCaracteristicasDescripcion;
	}

	/** Obtiene el valor de el campo Prematuros de la sección de embarazos anteriores */
	public String getPrematuros()
	{
		return is_prematuros;
	}

	public InfoDatos getPresentacionNacimiento()
	{
		return presentacionNacimiento;
	}

	/** Obtiene el ptc del trabajo de parto */
	public String getPtc()
	{
		return is_ptc;
	}

	/** Obtiene la descripción de ptc del trabajo de parto */
	public String getPtcDescripcion()
	{
		return is_ptcDescripcion;
	}

	/** Obtiene el indicador de reanimación */
	public String getReanimacion()
	{
		return is_reanimacion;
	}

	/** Obtiene el indicador de aspiración en reanimación */
	public String getReanimacionAspiracion()
	{
		return is_reanimacionAspiracion;
	}

	/** Obtiene el indicador de medicamentos en reanimación */
	public String getReanimacionMedicamentos()
	{
		return is_reanimacionMedicamentos;
	}

	public InfoDatos getRiesgoApgarMinuto1()
	{
		return riesgoApgarMinuto1;
	}

	public InfoDatos getRiesgoApgarMinuto5()
	{
		return riesgoApgarMinuto5;
	}

	public InfoDatos getRiesgoApgarMinuto10()
	{
		return riesgoApgarMinuto10;
	}

	public int getRupturaMembranasHoras()
	{
		return rupturaMembranasHoras;
	}

	public int getRupturaMembranasMinutos()
	{
		return rupturaMembranasMinutos;
	}

	/** Obtiene el indicador de si nació o no sano */
	public String getSano()
	{
		return is_sano;
	}

	/** Obtiene la Descripción del indicador de si nación o no sano */
	public String getSanoDescripcion()
	{
		return is_sanoDescripcion;
	}

	/** Obtiene la descripción del genero */
	public String getSexoDescripcion()
	{
		return is_sexoDescripcion;
	}

	public String getSufrimientoFetal()
	{
		return sufrimientoFetal;
	}

	public int getTalla()
	{
		return talla;
	}

	public ArrayList getTiposParto()
	{
		return tiposParto;
	}

	public InfoDatosBD getTiposParto(String codigoParto)
	{
		/* Caso otro tipo de parto */
		if(tiposParto == null)
			return null;

		/* Buscando en el resto de partos */
		for(int i = 0; i < tiposParto.size(); i++)
			if( ( (InfoDatosBD)tiposParto.get(i) ).getAcronimo().equals(codigoParto) )
				return ( (InfoDatosBD)tiposParto.get(i) );

		return null;
	}

	public InfoDatos getTipoTrabajoParto()
	{
		return tipoTrabajoParto;
	}

	public InfoDatos getUbicacionFeto()
	{
		return ubicacionFeto;
	}

	public UsuarioBasico getUsuarioBasico()
	{
		return usuarioBasico;
	}

	/**
	* Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	* @param tipoBD el tipo de base de datos que va a usar este objeto
	* (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	* son los nombres y constantes definidos en <code>DaoFactory</code>.
	* @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	*/
	public boolean init(String tipoBD)
	{
		DaoFactory myFactory;

		if( (myFactory = DaoFactory.getDaoFactory(tipoBD) ) != null)
			iaps_dao = myFactory.getAntecedentePediatricoDao();

		return iaps_dao != null;
	}

	/**
	* Inserta un nuevo antecedente pediatrico en una fuente de datos,
	* reutilizando una conexión existente.
	* Pre: usuarioBasico no puede ser nulo
	* @param con una conexión abierta con una fuente de datos
	* @return 0 si no se pudo realizar la inserción
	*/
	@SuppressWarnings("deprecation")
	public int insertar(Connection con) throws SQLException
	{
		int		presentacionNacimiento_;
		int		riesgoApgarMinuto1_;
		int		riesgoApgarMinuto5_;
		int		riesgoApgarMinuto10_;
		int		ubicacionFeto_;
		int		tipoTrabajoParto_ ;
		String	comentarioTipoTrabajoParto_;

		comentarioTipoTrabajoParto_	= null;
		tipoTrabajoParto_			= ConstantesBD.codigoNuncaValido;

		if(tipoTrabajoParto != null)
		{
			tipoTrabajoParto_			= tipoTrabajoParto.getCodigo();
			comentarioTipoTrabajoParto_	= tipoTrabajoParto.getDescripcion();
		}

		if(presentacionNacimiento == null)
			presentacionNacimiento_ = otraPresentacionNacimiento.trim().equals("") ? ConstantesBD.codigoNuncaValido: 0;
		else
			presentacionNacimiento_ = presentacionNacimiento.getCodigo();

		if(ubicacionFeto == null)
			ubicacionFeto_ = otraUbicacionFeto.trim().equals("") ? ConstantesBD.codigoNuncaValido: 0;
		else
			ubicacionFeto_ = ubicacionFeto.getCodigo();

		riesgoApgarMinuto1_		= riesgoApgarMinuto1 == null ? ConstantesBD.codigoNuncaValido: riesgoApgarMinuto1.getCodigo();
		riesgoApgarMinuto5_		= riesgoApgarMinuto5 == null ? ConstantesBD.codigoNuncaValido: riesgoApgarMinuto5.getCodigo();
		riesgoApgarMinuto10_	= riesgoApgarMinuto10 == null ? ConstantesBD.codigoNuncaValido: riesgoApgarMinuto10.getCodigo();

		return
			iaps_dao.insertarAntecedentePediatrico
			(
				con,
				paciente,
				presentacionNacimiento_,
				otraPresentacionNacimiento,
				ubicacionFeto_,
				otraUbicacionFeto,
				duracionPartoMinutos,
				duracionPartoHoras,
				rupturaMembranasMinutos,
				rupturaMembranasHoras,
				caracteristicasLiquidoAmniotico,
				sufrimientoFetal,
				causasSufrimientoFetal,
				anestesiaMedicamentos,
				tipoTrabajoParto_,
				comentarioTipoTrabajoParto_,
				otroTipoParto,
				motivoTipoPartoOtro,
				horaNacimiento,
				caracteristicasPlacenta,
				talla,
				peso,
				perimetroCefalico,
				perimetroToracico,
				apgarMinuto1,
				apgarMinuto5,
				apgarMinuto10,
				riesgoApgarMinuto1_,
				riesgoApgarMinuto5_,
				riesgoApgarMinuto10_,
				is_embarazosAnterioresOtros,
				is_incompatibilidadABO,
				is_incompatibilidadRH,
				is_macrosomicos,
				is_malformacionesCongenitas,
				is_mortinatos,
				is_muertesFetalesTempranas,
				is_prematuros,
				ii_codigoSerologia,
				is_descripcionSerologia,
				ii_codigoTestSullivan,
				is_descripcionTestSullivan,
				controlPrenatal,
				is_frecuenciaControlPrenatal,
				lugarControlPrenatal,
				ii_duracionExpulsivoHoras,
				ii_duracionExpulsivoMinutos,
				is_amnionitis,
				is_amnionitisFactorAnormal,
				is_anestesia,
				is_anestesiaTipo,
				is_nst,
				is_nstDescripcion,
				is_otrosExamenesTrabajoParto,
				is_perfilBiofisico,
				is_perfilBiofisicoDescripcion,
				is_ptc,
				is_ptcDescripcion,
				is_complicacionesParto,
				is_muestraCordonUmbilical,
				is_muestraCordonUmbilicalDescripcion,
				is_cordonUmbilicalCaracteristicas,
				is_cordonUmbilicalDescripcion,
				is_gemelo,
				is_gemeloDescripcion,
				is_intrauterinoPeg,
				is_intrauterinoPegCausa,
				ii_intrauterinoAnormalidad,
				is_intrauterinoAnormalidadCausa,
				is_intrauterinoArmonico,
				is_intrauterinoArmonicoCausa,
				is_reanimacion,
				is_reanimacionAspiracion,
				is_reanimacionMedicamentos,
				is_sano,
				is_sanoDescripcion,
				is_sexoDescripcion,
				ii_edadGestacional,
				is_edadGestacionalDescripcion,
				is_liqAmnioticoClaro,
				is_liqAmnioticoMeconiado,
				is_liqAmnioticoMeconiadoGrado,
				is_liqAmnioticoSanguinolento,
				is_liqAmnioticoFetido,
				is_placentaCaracteristicas,
				is_placentaCaracteristicasDescripcion,
				observaciones,
				usuarioBasico.getLoginUsuario(),
				UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual() ),
				tiposParto,
				inmunizacionesPediatricas,
				infoMadre,
				infoPadre,
				categoriaEmbarazoOpcionCampo
			);
	}

	@SuppressWarnings("deprecation")
	public int modificar(Connection con)throws SQLException
	{
		int		presentacionNacimiento_;
		int		ubicacionFeto_;
		int		tipoTrabajoParto_ ;
		String	comentarioTipoTrabajoParto_;

		comentarioTipoTrabajoParto_	= null;
		tipoTrabajoParto_			= ConstantesBD.codigoNuncaValido;

		if(tipoTrabajoParto != null)
		{
			tipoTrabajoParto_			= tipoTrabajoParto.getCodigo();
			comentarioTipoTrabajoParto_	= tipoTrabajoParto.getDescripcion();
		}

		if(presentacionNacimiento == null)
			presentacionNacimiento_ = otraPresentacionNacimiento.trim().equals("") ? ConstantesBD.codigoNuncaValido: 0;
		else
			presentacionNacimiento_ = presentacionNacimiento.getCodigo();

		if(ubicacionFeto == null)
			ubicacionFeto_ = otraUbicacionFeto.trim().equals("") ? ConstantesBD.codigoNuncaValido: 0;
		else
			ubicacionFeto_ = ubicacionFeto.getCodigo();

		return iaps_dao.modificarAntecedentePediatrico
		(
			con,
			paciente,
			presentacionNacimiento_,
			otraPresentacionNacimiento,
			ubicacionFeto_,
			otraUbicacionFeto,
			duracionPartoMinutos,
			duracionPartoHoras,
			rupturaMembranasMinutos,
			rupturaMembranasHoras,
			caracteristicasLiquidoAmniotico,
			sufrimientoFetal,
			causasSufrimientoFetal,
			anestesiaMedicamentos,
			tipoTrabajoParto_,
			comentarioTipoTrabajoParto_,
			otroTipoParto,
			motivoTipoPartoOtro,
			horaNacimiento,
			caracteristicasPlacenta,
			talla,
			peso,
			perimetroCefalico,
			perimetroToracico,
			apgarMinuto1,
			apgarMinuto5,
			apgarMinuto10,
			riesgoApgarMinuto1 == null ? ConstantesBD.codigoNuncaValido: riesgoApgarMinuto1.getCodigo(),
			riesgoApgarMinuto5 == null ? ConstantesBD.codigoNuncaValido: riesgoApgarMinuto5.getCodigo(),
			riesgoApgarMinuto10 == null ? ConstantesBD.codigoNuncaValido: riesgoApgarMinuto10.getCodigo(),
			is_embarazosAnterioresOtros,
			is_incompatibilidadABO,
			is_incompatibilidadRH,
			is_macrosomicos,
			is_malformacionesCongenitas,
			is_mortinatos,
			is_muertesFetalesTempranas,
			is_prematuros,
			ii_codigoSerologia,
			is_descripcionSerologia,
			ii_codigoTestSullivan,
			is_descripcionTestSullivan,
			controlPrenatal,
			is_frecuenciaControlPrenatal,
			lugarControlPrenatal,
			ii_duracionExpulsivoHoras,
			ii_duracionExpulsivoMinutos,
			is_amnionitis,
			is_amnionitisFactorAnormal,
			is_anestesia,
			is_anestesiaTipo,
			is_nst,
			is_nstDescripcion,
			is_otrosExamenesTrabajoParto,
			is_perfilBiofisico,
			is_perfilBiofisicoDescripcion,
			is_ptc,
			is_ptcDescripcion,
			is_complicacionesParto,
			is_muestraCordonUmbilical,
			is_muestraCordonUmbilicalDescripcion,
			is_cordonUmbilicalCaracteristicas,
			is_cordonUmbilicalDescripcion,
			is_gemelo,
			is_gemeloDescripcion,
			is_intrauterinoPeg,
			is_intrauterinoPegCausa,
			ii_intrauterinoAnormalidad,
			is_intrauterinoAnormalidadCausa,
			is_intrauterinoArmonico,
			is_intrauterinoArmonicoCausa,
			is_reanimacion,
			is_reanimacionAspiracion,
			is_reanimacionMedicamentos,
			is_sano,
			is_sanoDescripcion,
			is_sexoDescripcion,
			ii_edadGestacional,
			is_edadGestacionalDescripcion,
			is_liqAmnioticoClaro,
			is_liqAmnioticoMeconiado,
			is_liqAmnioticoMeconiadoGrado,
			is_liqAmnioticoSanguinolento,
			is_liqAmnioticoFetido,
			is_placentaCaracteristicas,
			is_placentaCaracteristicasDescripcion,
			observaciones,
			usuarioBasico.getLoginUsuario(),
			UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual() ),
			tiposParto,
			inmunizacionesPediatricas,
			infoMadre,
			infoPadre,
			categoriaEmbarazoOpcionCampo
		);
	}

	/** Asigna el indicador de amnionitis en el trabajo de parto */
	public void setAmnionitis(String as_amnionitis)
	{
		if(as_amnionitis != null)
			is_amnionitis = as_amnionitis.trim();
	}

	/** Asigna el factor anormal de amnionitis en el trabajo de parto */
	public void setAmnionitisFactorAnormal(String as_amnionitisFactorAnormal)
	{
		if(as_amnionitisFactorAnormal != null)
			is_amnionitisFactorAnormal = as_amnionitisFactorAnormal.trim();
	}

	/** Asigna el indicador uso de anestesia en el trabajo de parto */
	public void setAnestesia(String as_anestesia)
	{
		if(as_anestesia != null)
			is_anestesia = as_anestesia.trim();
	}

	public void setAnestesiaMedicamentos(String anestesiaMedicamentos)
	{
		if(anestesiaMedicamentos != null)
			this.anestesiaMedicamentos = anestesiaMedicamentos.trim();
	}

	/** Asigna el tipo de anestesia usada en el trabajo de parto */
	public void setAnestesiaTipo(String as_anestesiaTipo)
	{
		if(as_anestesiaTipo != null)
			is_anestesiaTipo = as_anestesiaTipo.trim();
	}

	public void setApgarMinuto1(int apgarMinuto1)
	{
		this.apgarMinuto1 = apgarMinuto1;
	}

	public void setApgarMinuto5(int apgarMinuto5)
	{
		this.apgarMinuto5 = apgarMinuto5;
	}

	public void setApgarMinuto10(int i)
	{
		apgarMinuto10 = i;
	}

	public void setCaracteristicasLiquidoAmniotico(String caracteristicasLiquidoAmniotico)
	{
		if(caracteristicasLiquidoAmniotico != null)
			this.caracteristicasLiquidoAmniotico = caracteristicasLiquidoAmniotico.trim();
	}

	public void setCaracteristicasPlacenta(String caracteristicasPlacenta)
	{
		if(caracteristicasPlacenta != null)
			this.caracteristicasPlacenta = caracteristicasPlacenta.trim();
	}

	public void setCausasSufrimientoFetal(String causasSufrimientoFetal)
	{
		if(causasSufrimientoFetal != null)
			this.causasSufrimientoFetal = causasSufrimientoFetal.trim();
	}

	/** Asigna el codigo de la serología del embarazo actual */
	public void setCodigoSerologia(int ai_codigoSerologia)
	{
		ii_codigoSerologia = (ai_codigoSerologia > 0) ? ai_codigoSerologia: ConstantesBD.codigoNuncaValido;
	}

	/** Asigna el codigo del test de O'Sullivan del embarazo actual */
	public void setCodigoTestSullivan(int ai_codigoTestSullivan)
	{
		ii_codigoTestSullivan = (ai_codigoTestSullivan > 0) ? ai_codigoTestSullivan: ConstantesBD.codigoNuncaValido;
	}

	/** Asigna la descripcion de las complicaciones del parto */
	public void setComplicacionesParto(String as_complicacionesParto)
	{
		if(as_complicacionesParto != null)
			is_complicacionesParto = as_complicacionesParto.trim();
	}

	/** Asigna el control prenatal */
	public void setControlPrenatal(String as_controlPrenatal)
	{
		if(as_controlPrenatal != null)
			controlPrenatal = as_controlPrenatal.trim();
	}

	/** Asigna las características del cordón umbilical */
	public void setCordonUmbilicalCaracteristicas(String as_cordonUmbilicalCaracteristicas)
	{
		if(as_cordonUmbilicalCaracteristicas != null)
			is_cordonUmbilicalCaracteristicas = as_cordonUmbilicalCaracteristicas.trim();
	}

	/** Asigna la descripción del cordón umbilical */
	public void setCordonUmbilicalDescripcion(String as_cordonUmbilicalDescripcion)
	{
		if(as_cordonUmbilicalDescripcion != null)
			is_cordonUmbilicalDescripcion = as_cordonUmbilicalDescripcion.trim();
	}

	/** Asigna la descripción de la serología del embarazo actual */
	public void setDescripcionSerologia(String as_descripcionSerologia)
	{
		if(as_descripcionSerologia != null)
			is_descripcionSerologia = as_descripcionSerologia.trim();
	}

	/** Asigna la descripción del test de O'Sullivan del embarazo actual */
	public void setDescripcionTestSullivan(String as_descripcionTestSullivan)
	{
		if(as_descripcionTestSullivan != null)
			is_descripcionTestSullivan = as_descripcionTestSullivan.trim();
	}

	/** Asigna el número de horas de duración del expulsivo */
	public void setDuracionExpulsivoHoras(int ai_duracionExpulsivoHoras)
	{
		ii_duracionExpulsivoHoras = (ai_duracionExpulsivoHoras > ConstantesBD.codigoNuncaValido) ? ai_duracionExpulsivoHoras: ConstantesBD.codigoNuncaValido;
	}

	/** Asigna el número de minutos de duración del expulsivo */
	public void setDuracionExpulsivoMinutos(int ai_duracionExpulsivoMinutos)
	{
		ii_duracionExpulsivoMinutos = (ai_duracionExpulsivoMinutos > ConstantesBD.codigoNuncaValido) ? ai_duracionExpulsivoMinutos: ConstantesBD.codigoNuncaValido;
	}

	public void setDuracionPartoHoras(int duracionPartoHoras)
	{
		this.duracionPartoHoras = duracionPartoHoras;
	}

	public void setDuracionPartoMinutos(int duracionPartoMinutos)
	{
		this.duracionPartoMinutos = duracionPartoMinutos;
	}

	/** Asigna la edad gestacional */
	public void setEdadGestacional(int ai_edadGestacional)
	{
		ii_edadGestacional = ai_edadGestacional > ConstantesBD.codigoNuncaValido ? ai_edadGestacional : ConstantesBD.codigoNuncaValido;
	}

	/** Asigna descripcion de la edad gestacional */
	public void setEdadGestacionalDescripcion(String as_edadGestacionalDescripcion)
	{
		if(as_edadGestacionalDescripcion != null)
			is_edadGestacionalDescripcion = as_edadGestacionalDescripcion.trim();
	}

	/** Asigna el valor del campo Otros de la sección embarazos anteriores */
	public void setEmbarazosAnterioresOtros(String as_embarazosAnterioresOtros)
	{
		if(as_embarazosAnterioresOtros != null)
			is_embarazosAnterioresOtros	= as_embarazosAnterioresOtros.trim();
	}

	/**
	* Asigna la dupla identificador - valor del campo de las
	* opciones de embarazo
	* @param aid_campo Identificador - Valor a asignar
	*/
	@SuppressWarnings("unchecked")
	public void setEmbarazoOpcionCampo(InfoDatos aid_campo)
	{
		if(aid_campo != null)
			categoriaEmbarazoOpcionCampo.add(aid_campo);
	}

	/** Asigna la frecuencia del control prenatal */
	public void setFrecuenciaControlPrenatal(String as_frecuenciaControlPrenatal)
	{
		if(as_frecuenciaControlPrenatal != null)
			is_frecuenciaControlPrenatal = as_frecuenciaControlPrenatal.trim();
	}

	/** Asigna le indicador de si es o no gemelo */
	public void setGemelo(String as_gemelo)
	{
		if(as_gemelo != null)
			is_gemelo = as_gemelo.trim();
	}

	/** Asigna la descripción del atributo gemelo */
	public void setGemeloDescripcion(String as_gemeloDescripcion)
	{
		if(as_gemeloDescripcion != null)
			is_gemeloDescripcion = as_gemeloDescripcion.trim();
	}

	public void setHoraNacimiento(String horaNacimiento)
	{
		if(horaNacimiento != null)
			this.horaNacimiento = horaNacimiento.trim();
	}

	/** Asigna el valor del campo Incompatibilidad ABO de la sección embarazos anteriores */
	public void setIncompatibilidadABO(String as_incompatibilidadABO)
	{
		if(as_incompatibilidadABO != null)
			is_incompatibilidadABO	= as_incompatibilidadABO.trim();
	}

	/** Asigna el valor del campo Incompatibilidad RH de la sección embarazos anteriores */
	public void setIncompatibilidadRH(String as_incompatibilidadRH)
	{
		if(as_incompatibilidadRH != null)
			is_incompatibilidadRH	= as_incompatibilidadRH.trim();
	}

	public void setInfoMadre(InfoMadre madre)
	{
		if(madre != null)
			infoMadre = madre;
	}

	public void setInfoPadre(InfoPadre aip_infoPadre)
	{
		if(aip_infoPadre != null)
			infoPadre = aip_infoPadre;
	}

	public void setInmunizacionesPediatricas(ArrayList inmunizacionesPediatricas)
	{
		this.inmunizacionesPediatricas = inmunizacionesPediatricas;
	}

	/** Asigna el indicador de crecimiento intrauterino */
	public void setIntrauterinoPeg(String as_intrauterinoPeg)
	{
		if(as_intrauterinoPeg != null)
			is_intrauterinoPeg = as_intrauterinoPeg.trim();
	}

	/** Asigna el indicador de crecimiento intrauterino */
	public void setIntrauterinoPegCausa(String as_intrauterinoPegCausa)
	{
		if(as_intrauterinoPegCausa != null)
			is_intrauterinoPegCausa = as_intrauterinoPegCausa.trim();
	}

	/** Asigna el porcentaje de anormalidad del crecimiento intrauterino */
	public void setIntrauterinoAnormalidad(int ai_intrauterinoAnormalidad)
	{
		ii_intrauterinoAnormalidad = (ai_intrauterinoAnormalidad > ConstantesBD.codigoNuncaValido || ai_intrauterinoAnormalidad < 101) ? ai_intrauterinoAnormalidad : ConstantesBD.codigoNuncaValido;
	}

	/** Asigna la causa de la anormalidad del crecimiento intrauterino */
	public void setIntrauterinoAnormalidadCausa(String as_intrauterinoAnormalidadCausa)
	{
		if(as_intrauterinoAnormalidadCausa != null)
			is_intrauterinoAnormalidadCausa = as_intrauterinoAnormalidadCausa.trim();
	}

	/** Asigna el indicador de crecimiento intrauterino armónico */
	public void setIntrauterinoArmonico(String as_intrauterinoArmonico)
	{
		if(as_intrauterinoArmonico != null)
			is_intrauterinoArmonico = as_intrauterinoArmonico.trim();
	}

	/** Asigna la causa del indicador de crecimiento intrauterino */
	public void setIntrauterinoArmonicoCausa(String as_intrauterinoArmonicoCausa)
	{
		if(as_intrauterinoArmonicoCausa != null)
			is_intrauterinoArmonicoCausa = as_intrauterinoArmonicoCausa.trim();
	}

	/** Asigna el indicador de liquido amniotico claro */
	public void setLiqAmnioticoClaro(String as_liqAmnioticoClaro)
	{
		if(as_liqAmnioticoClaro != null)
			is_liqAmnioticoClaro = as_liqAmnioticoClaro.trim();
	}

	/** Asigna indicador de liquido amniotico meconiado */
	public void setLiqAmnioticoMeconiado(String as_liqAmnioticoMeconiado)
	{
		if(as_liqAmnioticoMeconiado != null)
			is_liqAmnioticoMeconiado = as_liqAmnioticoMeconiado.trim();
	}

	/** Asigna el grado del liquido amniotico meconiado */
	public void setLiqAmnioticoMeconiadoGrado(String as_liqAmnioticoMeconiadoGrado)
	{
		if(as_liqAmnioticoMeconiadoGrado != null)
			is_liqAmnioticoMeconiadoGrado = as_liqAmnioticoMeconiadoGrado.trim();
	}

	/** Asigna indicador de liquido amniotico sanguinolento */
	public void setLiqAmnioticoSanguinolento(String as_liqAmnioticoSanguinolento)
	{
		if(as_liqAmnioticoSanguinolento != null)
			is_liqAmnioticoSanguinolento = as_liqAmnioticoSanguinolento.trim();
	}

	/** Asigna indicador de liquido amniotico fetido */
	public void setLiqAmnioticoFetido(String as_liqAmnioticoFetido)
	{
		if(as_liqAmnioticoFetido != null)
			is_liqAmnioticoFetido = as_liqAmnioticoFetido.trim();
	}

	/** Asigna el lugar del control prenatal */
	public void setLugarControlPrenatal(String as_lugarControlPrenatal)
	{
		if(as_lugarControlPrenatal != null)
			lugarControlPrenatal = as_lugarControlPrenatal.trim();
	}

	/** Asigna el valor del campo Macrosómicos de la sección embarazos anteriores */
	public void setMacrosomicos(String as_macrosomicos)
	{
		if(as_macrosomicos != null)
			is_macrosomicos	= as_macrosomicos.trim();
	}

	/** Asigna el valor del campo Malformaciones Congénitas de la sección embarazos anteriores */
	public void setMalformacionesCongenitas(String as_malformacionesCongenitas)
	{
		if(as_malformacionesCongenitas != null)
			is_malformacionesCongenitas = as_malformacionesCongenitas.trim();
	}

	/** Asigna el valor del campo Mortinatos de la sección embarazos anteriores */
	public void setMortinatos(String as_mortinatos)
	{
		if(as_mortinatos != null)
			is_mortinatos = as_mortinatos.trim();
	}

	public void setMotivoTipoPartoOtro(String motivoTipoParto)
	{
		if(motivoTipoParto != null)
			this.motivoTipoPartoOtro = motivoTipoParto.trim();
	}

	/** Asigna el valor del campo Muertes Fetales Tempranas de la sección embarazos anteriores */
	public void setMuertesFetalesTempranas(String as_muertesFetalesTempranas)
	{
		if(as_muertesFetalesTempranas != null)
			is_muertesFetalesTempranas = as_muertesFetalesTempranas.trim();
	}

	/** Asigna el indicador de toma de muestras del cordón umbilical */
	public void setMuestraCordonUmbilical(String as_muestraCordonUmbilical)
	{
		if(as_muestraCordonUmbilical != null)
			is_muestraCordonUmbilical = as_muestraCordonUmbilical.trim();
	}

	/** Asigna la descripción del indicador de toma de muestras del cordón umbilical */
	public void setMuestraCordonUmbilicalDescripcion(String as_muestraCordonUmbilicalDescripcion)
	{
		if(as_muestraCordonUmbilicalDescripcion != null)
			is_muestraCordonUmbilicalDescripcion = as_muestraCordonUmbilicalDescripcion.trim();
	}

	/** Asigna el el nst del trabajo de parto */
	public void setNst(String as_nst)
	{
		if(as_nst != null)
			is_nst = as_nst.trim();
	}

	/** Asigna la descripción de nst del trabajo de parto */
	public void setNstDescripcion(String as_nstDescripcion)
	{
		if(as_nstDescripcion != null)
			is_nstDescripcion = as_nstDescripcion.trim();
	}

	public void setObservaciones(String observaciones)
	{
		if(observaciones != null)
			this.observaciones = observaciones.trim();
	}

	public void setOtraPresentacionNacimiento(String otraPresentacionNacimiento)
	{
		if(otraPresentacionNacimiento != null)
			this.otraPresentacionNacimiento = otraPresentacionNacimiento.trim();
	}

	public void setOtraUbicacionFeto(String string)
	{
		if(string != null)
			otraUbicacionFeto = string.trim();
	}

	/** Asigna los otros tipos de exámenes del trabajo de parto */
	public void setOtrosExamenesTrabajoParto(String as_otrosExamenesTrabajoParto)
	{
		if(as_otrosExamenesTrabajoParto != null)
			is_otrosExamenesTrabajoParto = as_otrosExamenesTrabajoParto.trim();
	}

	public void setOtroTipoParto(String otroTipoParto)
	{
		if(otroTipoParto != null)
			this.otroTipoParto = otroTipoParto.trim();
	}

	public void setPaciente(PersonaBasica personaBasica)
	{
		paciente = personaBasica;

		infoMadre.setCodigoHijo(paciente.getCodigoPersona() );
		infoPadre.setCodigoHijo(paciente.getCodigoPersona() );
	}

	/** Asigna el perfil biofísico del trabajo de parto */
	public void setPerfilBiofisico(String as_perfilBiofisico)
	{
		if(as_perfilBiofisico != null)
			is_perfilBiofisico = as_perfilBiofisico.trim();
	}

	/** Asigna la descripción del perfil biofísico del trabajo de parto */
	public void setPerfilBiofisicoDescripcion(String as_perfilBiofisicoDescripcion)
	{
		if(as_perfilBiofisicoDescripcion != null)
			is_perfilBiofisicoDescripcion = as_perfilBiofisicoDescripcion.trim();
	}

	public void setPerimetroCefalico(float f)
	{
		perimetroCefalico = f;
	}

	public void setPerimetroToracico(float f)
	{
		perimetroToracico = f;
	}

	public void setPeso(int peso)
	{
		this.peso = peso;
	}

	/** Asigna características de la placenta */
	public void setPlacentaCaracteristicas(String as_placentaCaracteristicas)
	{
		if(as_placentaCaracteristicas != null)
			is_placentaCaracteristicas = as_placentaCaracteristicas.trim();
	}

	/** Asigna descripción de las características de la placenta */
	public void setPlacentaCaracteristicasDescripcion(String as_placentaCaracteristicasDescripcion)
	{
		if(as_placentaCaracteristicasDescripcion != null)
			is_placentaCaracteristicasDescripcion = as_placentaCaracteristicasDescripcion.trim();
	}

	/** Asigna el valor del campo Prematuros de la sección embarazos anteriores */
	public void setPrematuros(String as_prematuros)
	{
		if(as_prematuros != null)
			is_prematuros = as_prematuros.trim();
	}

	public void setPresentacionNacimiento(InfoDatos presentacionNacimiento)
	{
		this.presentacionNacimiento = presentacionNacimiento;
	}

	/** Asigna el ptc del trabajo de parto */
	public void setPtc(String as_ptc)
	{
		if(as_ptc != null)
			is_ptc = as_ptc.trim();
	}

	/** Asigna la descripción de ptc del trabajo de parto */
	public void setPtcDescripcion(String as_ptcDescripcion)
	{
		if(as_ptcDescripcion != null)
			is_ptcDescripcion = as_ptcDescripcion.trim();
	}

	/** Asigna el indicador de reanimación */
	public void setReanimacion(String as_reanimacion)
	{
		if(as_reanimacion != null)
			is_reanimacion = as_reanimacion.trim();
	}

	/** Asigna el indicador de aspiración en reanimación */
	public void setReanimacionAspiracion(String as_reanimacionAspiracion)
	{
		if(as_reanimacionAspiracion != null)
			is_reanimacionAspiracion = as_reanimacionAspiracion.trim();
	}

	/** Asigna el indicador de medicamentos en reanimación */
	public void setReanimacionMedicamentos(String as_reanimacionMedicamentos)
	{
		if(as_reanimacionMedicamentos != null)
			is_reanimacionMedicamentos = as_reanimacionMedicamentos.trim();
	}

	public void setRiesgoApgarMinuto1(InfoDatos riesgoApgarMinuto1)
	{
		this.riesgoApgarMinuto1 = riesgoApgarMinuto1;
	}

	public void setRiesgoApgarMinuto5(InfoDatos riesgoApgarMinuto5)
	{
		this.riesgoApgarMinuto5 = riesgoApgarMinuto5;
	}

	public void setRiesgoApgarMinuto10(InfoDatos datos)
	{
		riesgoApgarMinuto10 = datos;
	}

	public void setRupturaMembranasHoras(int rupturaMembranasHoras)
	{
		this.rupturaMembranasHoras = rupturaMembranasHoras;
	}

	public void setRupturaMembranasMinutos(int rupturaMembranasMinutos)
	{
		this.rupturaMembranasMinutos = rupturaMembranasMinutos;
	}

	/** Asigna el indicador de si nació o no sano */
	public void setSano(String as_sano)
	{
		if(as_sano != null)
			is_sano = as_sano.trim();
	}

	/** Asigna la Descripción del indicador de si nación o no sano */
	public void setSanoDescripcion(String as_sanoDescripcion)
	{
		if(as_sanoDescripcion != null)
			is_sanoDescripcion = as_sanoDescripcion.trim();
	}

	/** Asigna la descripción del genero */
	public void setSexoDescripcion(String as_sexoDescripcion)
	{
		if(as_sexoDescripcion != null)
			is_sexoDescripcion = as_sexoDescripcion.trim();
	}

	public void setSufrimientoFetal(String sufrimientoFetal)
	{
		if(sufrimientoFetal != null)
			this.sufrimientoFetal = sufrimientoFetal.trim();
	}

	public void setTalla(int talla)
	{
		this.talla = talla;
	}

	public void setTiposParto(ArrayList tiposParto)
	{
		this.tiposParto = tiposParto;
	}

	public void setTipoTrabajoParto(InfoDatos tipoTrabajoParto)
	{
		this.tipoTrabajoParto = tipoTrabajoParto;
	}

	public void setUbicacionFeto(InfoDatos datos)
	{
		ubicacionFeto = datos;
	}

	public void setUsuarioBasico(UsuarioBasico usuarioBasico)
	{
		this.usuarioBasico = usuarioBasico;
	}
	/**
	 * @return
	 */
	public boolean isExisteAntecedenteMaterno() {
	
		return !this.infoMadre.isVacio();
	}

	/**
	 * @return
	 */
	public boolean isExisteAntecedentePaterno() {
		return !this.infoPadre.isVacio();
	}

	/**
	 * @return
	 */
	public boolean isExisteApgar() {
		if(apgarMinuto1!=ConstantesBD.codigoNuncaValido ||
		apgarMinuto5!=ConstantesBD.codigoNuncaValido||
		apgarMinuto10!=ConstantesBD.codigoNuncaValido||
		(riesgoApgarMinuto1!=null)||
		(riesgoApgarMinuto5!=null)||
		(riesgoApgarMinuto10!=null)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @return
	 */
	public boolean isExisteAtencionParto() {
		if((otroTipoParto!=null && !otroTipoParto.trim().equals(""))||
		(motivoTipoPartoOtro!=null && !motivoTipoPartoOtro.trim().equals(""))||
		//(categoriaEmbarazoOpcionCampo!=null && categoriaEmbarazoOpcionCampo.size()>0)||
		(horaNacimiento!=null && !horaNacimiento.trim().equals(""))||
		(caracteristicasPlacenta!=null && !caracteristicasPlacenta.trim().equals(""))||
		(talla!=ConstantesBD.codigoNuncaValido)||
		(peso!=ConstantesBD.codigoNuncaValido)||
		(perimetroCefalico!=ConstantesBD.codigoNuncaValido)||
		(perimetroToracico!=ConstantesBD.codigoNuncaValido)||
		(ii_edadGestacional!=ConstantesBD.codigoNuncaValido)||
		(is_reanimacion!=null && !is_reanimacion.trim().equals(""))||
		(is_reanimacionAspiracion!=null && !is_reanimacionAspiracion.trim().equals(""))||
		(is_reanimacionMedicamentos!=null && !is_reanimacionMedicamentos.trim().equals(""))||
		(is_sano!=null && !is_sano.trim().equals(""))||
		(is_sanoDescripcion!=null && !is_sanoDescripcion.trim().equals(""))||
		(is_intrauterinoPeg!=null && !is_intrauterinoPeg.trim().equals(""))||
		(is_intrauterinoPegCausa!=null && !is_intrauterinoPegCausa.trim().equals(""))||
		(is_intrauterinoAnormalidadCausa!=null && !is_intrauterinoAnormalidadCausa.trim().equals(""))||
		(is_intrauterinoArmonico!=null && !is_intrauterinoArmonico.trim().equals(""))||
		(is_intrauterinoArmonicoCausa!=null && !is_intrauterinoArmonicoCausa.trim().equals(""))||
		(is_liqAmnioticoClaro!=null && !is_liqAmnioticoClaro.trim().equals(""))||
		(is_liqAmnioticoMeconiado!=null && !is_liqAmnioticoMeconiado.trim().equals(""))||
		(is_liqAmnioticoMeconiadoGrado!=null && !is_liqAmnioticoMeconiadoGrado.trim().equals(""))||
		(is_liqAmnioticoSanguinolento!=null && !is_liqAmnioticoSanguinolento.trim().equals(""))||
		(is_liqAmnioticoFetido!=null && !is_liqAmnioticoFetido.trim().equals(""))||
		(is_muestraCordonUmbilical!=null && !is_muestraCordonUmbilical.trim().equals(""))||
		(is_muestraCordonUmbilicalDescripcion!=null && !is_muestraCordonUmbilicalDescripcion.trim().equals(""))||
		(is_placentaCaracteristicas!=null && !is_placentaCaracteristicas.trim().equals(""))||
		(is_placentaCaracteristicasDescripcion!=null && !is_placentaCaracteristicasDescripcion.trim().equals(""))||
		(ii_intrauterinoAnormalidad!=ConstantesBD.codigoNuncaValido)||
		(is_complicacionesParto!=null && !is_complicacionesParto.trim().equals(""))||
		(is_cordonUmbilicalCaracteristicas!=null && !is_cordonUmbilicalCaracteristicas.trim().equals(""))||
		(is_cordonUmbilicalDescripcion!=null && !is_cordonUmbilicalDescripcion.trim().equals(""))||
		(is_edadGestacionalDescripcion!=null && !is_edadGestacionalDescripcion.trim().equals(""))||
		(is_frecuenciaControlPrenatal!=null && !is_frecuenciaControlPrenatal.trim().equals(""))||
		(is_gemelo!=null && !is_gemelo.trim().equals(""))||
		(is_gemeloDescripcion!=null && !is_gemeloDescripcion.trim().equals(""))||
		(is_sexoDescripcion!=null && !is_sexoDescripcion.trim().equals(""))){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @return
	 */
	public boolean isExisteEmbarazoActual() {
	
		if(ii_codigoSerologia!=ConstantesBD.codigoNuncaValido||
		ii_codigoTestSullivan!=ConstantesBD.codigoNuncaValido||
		(is_descripcionSerologia!=null && !is_descripcionSerologia.trim().equals(""))||
		(is_descripcionTestSullivan!=null && !is_descripcionTestSullivan.trim().equals(""))||
		(categoriaEmbarazoOpcionCampo!=null && categoriaEmbarazoOpcionCampo.size()>0)){
			return true;
		}else{
			return false;
		}

	}

	/**
	 * @return
	 */
	public boolean isExisteEmbarazosAnteriores() {
		if((controlPrenatal!=null &&  !controlPrenatal.trim().equals("") ) ||
		(lugarControlPrenatal!=null && !lugarControlPrenatal.trim().equals("")) ||
		(is_incompatibilidadABO!=null &&  !is_incompatibilidadABO.trim().equals("")) ||
		(is_incompatibilidadRH!=null &&  !is_incompatibilidadRH.trim().equals("")) ||
		(is_macrosomicos!=null &&  !is_macrosomicos.trim().equals("")) ||
		(is_malformacionesCongenitas!=null &&  !is_malformacionesCongenitas.trim().equals("")) ||
		(is_mortinatos!=null &&  !is_mortinatos.trim().equals("")) ||
		(is_muertesFetalesTempranas!=null &&  !is_muertesFetalesTempranas.trim().equals("")) ||
		(is_embarazosAnterioresOtros!=null &&  !is_embarazosAnterioresOtros.trim().equals("")) ||
		(is_prematuros!=null &&  !is_prematuros.trim().equals(""))) {
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * @return
	 */
	public boolean isExisteInmunizaciones() {
		if(inmunizacionesPediatricas!=null && inmunizacionesPediatricas.size()>0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @return
	 */
	public boolean isExisteObservaciones() {
		if(observaciones!=null && !observaciones.trim().equals("")){
			return true;
		}else{
			return false;
		}
	
	}

	/**
	 * @return
	 */
	public boolean isExisteTrabajoParto() {
		if((
		(is_amnionitis!=null && !is_amnionitis.trim().equals(""))||
		(is_amnionitisFactorAnormal!=null && !is_amnionitisFactorAnormal.trim().equals(""))||
		(is_anestesia!=null && !is_anestesia.trim().equals(""))||
		(is_anestesiaTipo!=null && !is_anestesiaTipo.trim().equals(""))||
		(tipoTrabajoParto!=null )||
		(ubicacionFeto!=null)||
		(duracionPartoHoras!=ConstantesBD.codigoNuncaValido)||
		(duracionPartoMinutos!=ConstantesBD.codigoNuncaValido)||
		(rupturaMembranasMinutos!=ConstantesBD.codigoNuncaValido)||
		(rupturaMembranasHoras!=ConstantesBD.codigoNuncaValido)||
		(caracteristicasLiquidoAmniotico!=null && !caracteristicasLiquidoAmniotico.trim().equals(""))||
		(sufrimientoFetal!=null && !sufrimientoFetal.trim().equals(""))||
		(causasSufrimientoFetal!=null && !causasSufrimientoFetal.trim().equals(""))||
		(anestesiaMedicamentos!=null && !anestesiaMedicamentos.trim().equals(""))||
		(otraPresentacionNacimiento!=null && !otraPresentacionNacimiento.trim().equals(""))||
		(presentacionNacimiento!=null)||
		(otraUbicacionFeto!=null && !otraUbicacionFeto.trim().equals(""))||
		(ii_duracionExpulsivoMinutos!=ConstantesBD.codigoNuncaValido)||
		(ii_duracionExpulsivoHoras!=ConstantesBD.codigoNuncaValido)||
		(tiposParto!=null && tiposParto.size()>0))||
		(is_nst!=null && !is_nst.trim().equals(""))||
		(is_nstDescripcion!=null && !is_nstDescripcion.trim().equals(""))||
		(is_otrosExamenesTrabajoParto!=null && !is_otrosExamenesTrabajoParto.trim().equals(""))||
		(is_perfilBiofisico!=null && !is_perfilBiofisico.trim().equals(""))||
		(is_perfilBiofisicoDescripcion!=null && !is_perfilBiofisicoDescripcion.trim().equals(""))||
		(is_ptc!=null && !is_ptc.trim().equals(""))||
		(is_ptcDescripcion!=null && !is_ptcDescripcion.trim().equals(""))){
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteAntecedenteMaterno(boolean b) {
		existeAntecedenteMaterno = b;
	}
	*/

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteAntecedentePaterno(boolean b) {
		existeAntecedentePaterno = b;
	}
	*/

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	 public void setExisteApgar(boolean b) {
		existeApgar = b;
	}
	*/

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteAtencionParto(boolean b) {
		existeAtencionParto = b;
	}
	*/

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteEmbarazoActual(boolean b) {
		existeEmbarazoActual = b;
	}
	*/

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteEmbarazosAnteriores(boolean b) {
		existeEmbarazosAnteriores = b;
	}
	*/
	
	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteInmunizaciones(boolean b) {
		existeInmunizaciones = b;
	}
	*/

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteObservaciones(boolean b) {
		existeObservaciones = b;
	}
	*/

	/**
	 * @param b
	 */
	/*
	 *no se usan. 
	public void setExisteTrabajoParto(boolean b) {
		existeTrabajoParto = b;
	}
	*/

	
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public boolean cargarResumenAtencion(Connection con, HashMap mapa) throws SQLException
	{
		int ai_codigoPaciente = UtilidadCadena.vInt(mapa.get("paciente")+"");
		
		Dosis						laDosis;
		InmunizacionesPediatricas	inmunizaciones;
		int							li_aux;
		int							presentacionNacimientoInt;
		int							tipoInmunizacion_;
		int							tipoInmunizacion_ant;
		PersonaBasica				elPaciente;
		ResultSetDecorator					inmunizacionesDosis_rs;
		ResultSetDecorator					inmunizacionesObser_rs;
		ResultSetDecorator					lrs_embarazoOpciones;
		ResultSetDecorator					rs;
		ResultSetDecorator					tiposParto_rs;
		String						laObservacion;
		String						ls_aux;
		String						ls_embarazoOpcionCodigo;
		String						presentacionNacimientoStr;
		String						tiposInmunizacionStr;
		String						ubicacionFetoStr;

		clean();

		/* Para cargar los datos básicos de antecedente pediatrico */
		elPaciente				= new PersonaBasica();
		inmunizaciones			= null;
		inmunizacionesDosis_rs	= null;
		inmunizacionesObser_rs	= null;
		laDosis					= null;
		laObservacion			="";
		rs						= iaps_dao.cargarAntecedentePediatrico(con, ai_codigoPaciente);
		tiposParto_rs			= null;
		tipoInmunizacion_ant	= -2;
		tiposInmunizacionStr	= "";

		if(rs.next() )
		{
			/* Cargando paciente */
			elPaciente.setCodigoPersona(ai_codigoPaciente);
			setPaciente(elPaciente);

			/* Si hay una presentación se inicializa este atributo */
			if( (presentacionNacimientoStr = rs.getString("presentacion_nacimiento") ) != null && Utilidades.convertirAEntero(presentacionNacimientoStr)!=ConstantesBD.codigoNuncaValido)
			{
				/* Averiguo el nombre de la presentación */
				presentacionNacimientoInt = new Integer(presentacionNacimientoStr).intValue();
				setPresentacionNacimiento(
					new InfoDatos(presentacionNacimientoStr,iaps_dao.consultarNombrePresentacionNacimiento(con,presentacionNacimientoInt))					
				);

			//	mapa.put("PresentacionNacimientoStr",presentacionNacimientoStr);
			//	mapa.put("PresentacionNacimiento",iaps_dao.consultarNombrePresentacionNacimiento(con,presentacionNacimientoInt));
				
			}
			/* Puede haber otra presentación nacimiento */
			else if(!UtilidadTexto.isEmpty(ls_aux = rs.getString("otra_presentacion_nacimiento") ))
			{
				setOtraPresentacionNacimiento(ls_aux);
			//	mapa.put("OtraPresentacionNacimiento", ls_aux);
			}
				

			/* Si hay una ubicación del feto se inicializa este atributo */
			if( !UtilidadTexto.isEmpty(ubicacionFetoStr = rs.getString("ubicacion_feto") ) )
			{
				/* Averiguo el nombre de la ubicación */
				setUbicacionFeto(
					new InfoDatos(
						ubicacionFetoStr,
						iaps_dao.consultarNombreUbicacionFeto(con,Integer.parseInt(ubicacionFetoStr))
					)
				);
				
				//mapa.put("UbicacionFetoStr",ubicacionFetoStr);
				//String cad = iaps_dao.consultarNombreUbicacionFeto(con,Integer.parseInt(ubicacionFetoStr));
				//mapa.put("UbicacionFeto",cad);				
			}
			/* Puede haber otra ubicación del feto */
			else if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("otra_ubicacion_feto") ))
			{
				setOtraUbicacionFeto(ls_aux);
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}

			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("duracion_expulsivo_horas") ) )
			{
				setDuracionExpulsivoHoras(Integer.parseInt(ls_aux) );
				//mapa.put("DuracionExpulsivoHoras", ls_aux);
			}
			if(!UtilidadTexto.isEmpty(ls_aux = rs.getString("duracion_expulsivo_mins") ) )
			{
				setDuracionExpulsivoMinutos(Integer.parseInt(ls_aux) );  //---
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}	
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("duracion_parto_horas") ))
			{
				setDuracionPartoHoras(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}	
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("duracion_parto_mins") ))
			{
				setDuracionPartoMinutos(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("edad_gestacional") ))
			{
				setEdadGestacional(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("intrauterino_anormalidad") ))
			{
				setIntrauterinoAnormalidad(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("peso") ))
			{
				setPeso(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("ruptura_membranas_horas") ))
			{
				setRupturaMembranasHoras(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("ruptura_membranas_mins") ))
			{
				setRupturaMembranasMinutos(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}	
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("talla") ))
			{
				setTalla(Integer.parseInt(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}	
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("perimetro_cefalico") ) )
			{
				setPerimetroCefalico(Float.parseFloat(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}	
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("perimetro_toracico") ) )
			{
				setPerimetroToracico(Float.parseFloat(ls_aux) );
				//mapa.put("OtraUbicacionFeto", ls_aux);
			}	

			setAmnionitis(rs.getString("amnionitis") );
			setAmnionitisFactorAnormal(rs.getString("amnionitis_factor_anormal") );
			setAnestesia(rs.getString("anestesia") );
			setAnestesiaMedicamentos(rs.getString("anestesia_medicamentos") );
			setAnestesiaTipo(rs.getString("anestesia_tipo") );
			setCaracteristicasLiquidoAmniotico(rs.getString("caracteristicas_liq_amniotico") );
			setCaracteristicasPlacenta(rs.getString("caracteristicas_placenta") );
			setCodigoSerologia(rs.getInt("codigo_serologia") );
			setCodigoTestSullivan(rs.getInt("codigo_test_sullivan") );
			setComplicacionesParto(rs.getString("complicaciones_parto") );
			setControlPrenatal(rs.getString("control_prenatal") );
			setCordonUmbilicalCaracteristicas(rs.getString("cordon_umb_carac") );
			setCordonUmbilicalDescripcion(rs.getString("cordon_umbilical_descripcion") );
			setDescripcionSerologia(rs.getString("descripcion_serologia") );
			setDescripcionTestSullivan(rs.getString("descripcion_test_sullivan") );
			setEdadGestacionalDescripcion(rs.getString("edad_gestacional_descripcion") );
			setEmbarazosAnterioresOtros(rs.getString("embarazos_anteriores_otros") );
			setFrecuenciaControlPrenatal(rs.getString("frecuencia_control_prenatal") );
			setGemelo(rs.getString("gemelo") );
			setGemeloDescripcion(rs.getString("gemelo_descripcion") );
			setIncompatibilidadABO(rs.getString("incompatibilidad_abo") );
			setIncompatibilidadRH(rs.getString("incompatibilidad_rh") );
			setIntrauterinoPeg(rs.getString("intrauterino_peg") );
			setIntrauterinoPegCausa(rs.getString("intrauterino_peg_causa") );
			setIntrauterinoAnormalidadCausa(rs.getString("intrauterino_anormalidad_causa") );
			setIntrauterinoArmonico(rs.getString("intrauterino_armonico") );
			setIntrauterinoArmonicoCausa(rs.getString("intrauterino_armonico_causa") );
			setLiqAmnioticoClaro(rs.getString("liq_amniotico_claro") );
			setLiqAmnioticoMeconiado(rs.getString("liq_amniotico_meconiado") );
			setLiqAmnioticoMeconiadoGrado(rs.getString("liq_amniotico_meconiado_grado") );
			setLiqAmnioticoSanguinolento(rs.getString("liq_amniotico_sanguinolento") );
			setLiqAmnioticoFetido(rs.getString("liq_amniotico_fetido") );
			setLugarControlPrenatal(rs.getString("lugar_control_prenatal") );
			setMacrosomicos(rs.getString("macrosomicos") );
			setMalformacionesCongenitas(rs.getString("malformaciones_congenitas") );
			setMortinatos(rs.getString("mortinatos") );
			setMuertesFetalesTempranas(rs.getString("muertes_fetales_tempranas") );
			setMuestraCordonUmbilical(rs.getString("muestra_cordon_umbilical") );
			setMuestraCordonUmbilicalDescripcion(rs.getString("mues_cor_umbilical_des") );
			setNst(rs.getString("nst") );
			setNstDescripcion(rs.getString("nst_descripcion") );
			setObservaciones(rs.getString("observaciones") );
			setOtrosExamenesTrabajoParto(rs.getString("otros_examenes_trabajo_parto") );
			setPlacentaCaracteristicas(rs.getString("placenta_caracteristicas") );
			setPlacentaCaracteristicasDescripcion(rs.getString("plac_catac_desc") );
			setPerfilBiofisico(rs.getString("perfil_biofisico") );
			setPerfilBiofisicoDescripcion(rs.getString("perfil_biofisico_descripcion") );
			setPrematuros(rs.getString("prematuros") );
			setPtc(rs.getString("ptc") );
			setPtcDescripcion(rs.getString("ptc_descripcion") );
			setReanimacion(rs.getString("reanimacion") );
			setReanimacionAspiracion(rs.getString("reanimacion_aspiracion") );
			setReanimacionMedicamentos(rs.getString("reanimacion_medicamentos") );
			setSano(rs.getString("sano") );
			setSanoDescripcion(rs.getString("sano_descripcion") );
			setSexoDescripcion(rs.getString("sexo_descripcion") );
			
			

			if(!UtilidadTexto.isEmpty(ls_aux = rs.getString("tiempo_nacimiento") ) )
				setHoraNacimiento(ls_aux.substring(0, ls_aux.lastIndexOf(':') ) );

			/* Si hay otro tipo de parto, inicializo este atributo y el motivo de otro tipo parto */
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("otro_tipo_parto") ))
			{
				setOtroTipoParto(ls_aux);
				setMotivoTipoPartoOtro(rs.getString("motivo_tipo_parto") );
			}

			/* Buscar los otros tipos de parto asociados al antecedente pedáatrico */
			tiposParto_rs = iaps_dao.consultarAntPediatricoTiposParto(con, ai_codigoPaciente);
			setTiposParto(new ArrayList() );

			while(tiposParto_rs.next() )
				getTiposParto().add(
					new InfoDatosBD(
						tiposParto_rs.getString("tipo_parto"),
						tiposParto_rs.getString("nombre"),
						tiposParto_rs.getString("motivo"),
						't'
					)
				);

			if(getTiposParto().size() == 0)
				setTiposParto(null);

			/* Si hay otro tipo trabajo parto, inicializo este atributo y el comentario de ese trabajo de parto */
			if(! UtilidadTexto.isEmpty(ls_aux = rs.getString("tipo_trabajo_parto") ))
				setTipoTrabajoParto
				(
					new InfoDatos
					(
						ls_aux,
						iaps_dao.consultarNombreTipoTrabajoParto(con, Integer.parseInt(ls_aux) ),
						rs.getString("comentario_tipo_trabajo_parto")
					)
				);

			/* Si se inicializo apgar debe consultar sus riesgos */
			if(!UtilidadTexto.isEmpty(ls_aux = rs.getString("apgar_minuto1") ) )
			{
				setApgarMinuto1(li_aux = (Integer.parseInt(ls_aux) ) );

				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto1(
					new InfoDatos(
						ls_aux = rs.getString("riesgo_apgar_minuto1"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo apgar debe consultar sus riesgos */
			if(!UtilidadTexto.isEmpty(ls_aux = rs.getString("apgar_minuto5") ) )
			{
				setApgarMinuto5(li_aux = (Integer.parseInt(ls_aux) ) );
				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto5(
					new InfoDatos(
						ls_aux = rs.getString("riesgo_apgar_minuto5"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo apgar debe consultar sus riesgos */
			if( !UtilidadTexto.isEmpty(ls_aux = rs.getString("apgar_minuto10") ) )
			{
				setApgarMinuto10(li_aux = (Integer.parseInt(ls_aux) ) );
				/* Si puso apgar esta obligado a poner riesgo, la consulta siguiente no puede dar vacio */
				setRiesgoApgarMinuto10(
					new InfoDatos(
						ls_aux = rs.getString("riesgo_apgar_minuto10"),
						iaps_dao.consultarNombreRiesgoApgar(con, Integer.parseInt(ls_aux) )
					)
				);
			}

			/* Si se inicializo sufrimiento fetal y fue true, se ponen las causas */
			if(rs.getString("sufrimiento_fetal") != null && rs.getBoolean("sufrimiento_fetal") )
			{
				setSufrimientoFetal("true");
				setCausasSufrimientoFetal(rs.getString("causas_sufrimiento_fetal") );
			}
			else if(rs.getString("sufrimiento_fetal") != null && !rs.getBoolean("sufrimiento_fetal") )
				setSufrimientoFetal("false");

			/* Ahora cargamos las inmunizaciones con sus dosis y/o observaciones */
			inmunizacionesDosis_rs = iaps_dao.consultarAntPediatricoInmunizacionesDosis(con, ai_codigoPaciente);
			inmunizacionesObser_rs = iaps_dao.consultarAntPediatricoInmunizacionesObser(con, ai_codigoPaciente);

			setInmunizacionesPediatricas(new ArrayList() );

			while(inmunizacionesDosis_rs.next() )
			{
				if( (tipoInmunizacion_ = inmunizacionesDosis_rs.getInt("tipo_inmunizacion") ) != tipoInmunizacion_ant)
				{
					inmunizaciones = new InmunizacionesPediatricas();
					inmunizaciones.setCodigoInmunizacion(tipoInmunizacion_);
					inmunizaciones.setNombreInmunizacion(inmunizacionesDosis_rs.getString("nombre") );

					laObservacion = iaps_dao.consultarObservacionInmunizacion(con, ai_codigoPaciente, tipoInmunizacion_);

					/* Si no es cadena vacia es porque se encontraba en la bd, se debe asignar y poner estaEnBD en true */
					if(!laObservacion.equals("") )
					{
						inmunizaciones.setObservaciones(laObservacion);
						inmunizaciones.setEstaEnBD('t');
					}

					inmunizaciones.setDosis(new ArrayList() );

					getInmunizacionesPediatricas().add(inmunizaciones);

					/* Agrego a la cadena de inmunizaciones con dosis el tipo de inmunización */
					tiposInmunizacionStr += "," + tipoInmunizacion_ + ",";
				}

				laDosis = new Dosis();
				laDosis.setFecha(inmunizacionesDosis_rs.getString("fecha") );

				li_aux = inmunizacionesDosis_rs.getInt("numero_dosis");

				if(li_aux < 0)
					laDosis.setRefuerzo(true);

				laDosis.setNumeroDosis(li_aux);
				laDosis.setEstaEnBD('t');

				inmunizaciones.getDosis().add(laDosis);

				tipoInmunizacion_ant = tipoInmunizacion_;
			}

			if(getInmunizacionesPediatricas().size() == 0)
				setInmunizacionesPediatricas(null);

			/* Ahora buscando inmunizaciones que no tienen dosis pero que tienen observación */
			while(inmunizacionesObser_rs.next() )
			{
				if(getInmunizacionesPediatricas() == null)
				{
					setInmunizacionesPediatricas(new ArrayList() );

					inmunizaciones = new InmunizacionesPediatricas();
					inmunizaciones.setCodigoInmunizacion(inmunizacionesObser_rs.getInt("tipo_inmunizacion") );
					inmunizaciones.setNombreInmunizacion(inmunizacionesObser_rs.getString("nombre") );
					inmunizaciones.setObservaciones(inmunizacionesObser_rs.getString("observacion") );
					inmunizaciones.setEstaEnBD('t');

					getInmunizacionesPediatricas().add(inmunizaciones);
				}
				else
				{
					/* ver si esta ya en el arreglo de inmunizaciones pedíatricas */
					tipoInmunizacion_ = inmunizacionesObser_rs.getInt("tipo_inmunizacion");

					if(tiposInmunizacionStr.indexOf("," + tipoInmunizacion_ + ",") == ConstantesBD.codigoNuncaValido)
					{
						/* En caso de no estar ahi se debe crear la inmunizacion y agregarla */
						inmunizaciones = new InmunizacionesPediatricas();

						inmunizaciones.setCodigoInmunizacion(inmunizacionesObser_rs.getInt("tipo_inmunizacion") );
						inmunizaciones.setNombreInmunizacion(inmunizacionesObser_rs.getString("nombre") );
						inmunizaciones.setObservaciones(inmunizacionesObser_rs.getString("observacion") );
						inmunizaciones.setEstaEnBD('t');

						getInmunizacionesPediatricas().add(inmunizaciones);
					}
				}
			}

			/* Cargar las opciones de embarazo */
			lrs_embarazoOpciones = iaps_dao.consultarEmbarazoOpciones(con, ai_codigoPaciente);

			while(lrs_embarazoOpciones.next() )
			{
				ls_embarazoOpcionCodigo =
					lrs_embarazoOpciones.getString("codigo_categoria_embarazo") + "-"	+
					lrs_embarazoOpciones.getString("cod_cat_embarazo_opcion")	+ "-";

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo1") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
				{
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "1", ls_aux) );
					//mapa.put(ls_embarazoOpcionCodigo + "1", ls_aux);
				}	

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo2") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
				{
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "2", ls_aux) );
					//mapa.put(ls_embarazoOpcionCodigo + "2", ls_aux);
				}	

				if(
					(ls_aux = lrs_embarazoOpciones.getString("valor_campo3") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
				{
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "3", ls_aux) );
					//mapa.put(ls_embarazoOpcionCodigo + "3", ls_aux);
				}	

				if(
					(ls_aux = lrs_embarazoOpciones.getString("existio") ) != null &&
					!(ls_aux = ls_aux.trim() ).equals("")
				)
				{
					setEmbarazoOpcionCampo(new InfoDatos(ls_embarazoOpcionCodigo + "4", ls_aux) );
					//mapa.put(ls_embarazoOpcionCodigo + "4", ls_aux);
				}
			}

			return true;
		}

		return false;
	}

	/**
	 * Metodo para cargar las Patologias Registradas.
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap consultarPatologias(Connection con, int codigoPersona)
	{
		return iaps_dao.consultarPatologias(con, codigoPersona);
	}

	/**
	 * @return valor de fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha el fecha para asignar
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return valor de hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora el hora para asignar
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	
}