/*
* @(#)AntecedentePediatricoForm.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje   : Java
* Compilador : J2SDK 1.4.1_01
*
*/

package com.princetonsa.actionform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.InfoDatos;
import util.InfoDatosBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.antecedentes.AntecedentePediatrico;
import com.princetonsa.mundo.antecedentes.Dosis;
import com.princetonsa.mundo.antecedentes.InfoMadre;
import com.princetonsa.mundo.antecedentes.InfoPadre;
import com.princetonsa.mundo.antecedentes.InmunizacionesPediatricas;
/**
* <code>ValidatorForm</code> para el ingreso de datos de
* <code>AntecedentePediatrico</code>
*
* @version 1.1, Jul 21, 2003
* @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>,
* <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
*/
@SuppressWarnings({"rawtypes","deprecation"})
public class AntecedentePediatricoForm extends ValidatorForm implements Serializable
{
	/** *  */
	private static final long serialVersionUID = 1L;

	private AntecedentePediatrico antPed =  new AntecedentePediatrico();
	
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Consecutivos disponibles
	 */
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(AntecedentePediatricoForm.class);

	/* Seccion informacion de la madre durante el embarazo */
	private String	edadMadre								= "";
	private String	codigoTipoSangreMadre					= "";
	private	 int		ii_codigoMetodoMadre				= 0;
	private String	is_confiableSemanasMadre				= "";
	private String	is_fppMadre								= "";
	private String	is_furMadre								= "";
	private String	is_infoEmbarazoAMadre					= "";
	private String	is_infoEmbarazoCMadre					= "";
	private String	is_infoEmbarazoGMadre					= "";
	private String	is_infoEmbarazoMMadre					= "";
	private String	is_infoEmbarazoPMadre					= "";
	private String	is_infoEmbarazoVMadre					= "";
	private String	is_numeroIdentificacionMadre			= "";
	private String	is_primerApellidoMadre					= "";
	private String	is_primerNombreMadre					= "";
	private String	is_segundoApellidoMadre					= "";
	private String	is_segundoNombreMadre					= "";
	private String	is_tipoIdentificacionMadre				= "";
	private String	nombreTipoSangreMadre					= "";
	private String	observacionesEmbarazoMadre				= "";
	private String	observacionesEmbarazoMadreAnteriores	= "";
	private String	obsExamenesMedicamentosMadre			= "";
	private String	obsExamenesMedicamentosMadreAnteriores	= "";
	private String	obsPatologiasMadre						= "";
	private String	obsPatologiasMadreAnteriores			= "";
	private String	semanasGestacion						= "";
	private String	textareaMotivosParto					= "";
	private String	textareaObservacionesInmunizacion		= "";
	private String descipcionOtrosMetodos;

	
	// tipos parto e inmunizaciones
	private ArrayList tiposPartoList = new ArrayList();
	private ArrayList inmunizacionesList = new ArrayList(); 
	
	
	/* Seccion informacion del padre */
	private String	is_codigoTipoSangrePadre		= "";
	private String	is_consanguinidadPadre			= "";
	private String	is_edadPadre					= "";
	private String	is_nombreTipoSangrePadre		= "";
	private String	is_numeroIdentificacionPadre	= "";
	private String	is_primerApellidoPadre			= "";
	private String	is_primerNombrePadre			= "";
	private String	is_segundoApellidoPadre			= "";
	private String	is_segundoNombrePadre			= "";
	private String	is_tipoIdentificacionPadre		= "";

	/**
	* La accion que se va a ejecutar en esta forma. Puede ser "ingresar",
	* "modificar" o "cargar"
	*/
	private String accion = "";

	/** La parte de horas de la duración del parto (forma HH:mm) */
	private String duracionPartoHoras = "";

	/** La parte de minutos de la duración del parto (forma HH:mm) */
	private String duracionPartoMinutos = "";

	/** Pareja código-nombre con los datos del tipo de trabajo de parto */
	private String tipoTrabajoParto_cn = "";

	/**
	* Comentario asociado al tipo de trabajo de parto. Sólo puede haber un sólo
	* tipo de trabajo de parto, luego sólo puede haber un sólo comentario
	* asociado a éste
	*/
	private String comentarioTipoTrabajoParto = "";

	/** Pareja codigo-nombre con la presentación del nacimiento */
	private String presentacionNacimiento_cn = "";

	/**
	* Si la presentación no corresponde a una de las predefinidas, en este
	* campo se indica cómo fue
	*/
	private String otraPresentacionNacimiento = "";

	/** Pareja codigo-nombre con la ubicación del feto */
	private String ubicacionFeto_cn = "";

	/**
	* Si la ubicación del feto no corresponde a una de las predefinidas, en este
	* campo se indica cómo fue
	*/
	private String otraUbicacionFeto = "";

	/**
	* Cuántas horas antes del parto se dio la ruptura de membranas
	* (forma HH:mm)
	*/
	private String rupturaMembranasHoras = "";

	/**
	* Cuántos minutos antes del parto se dió la ruptura de membranas
	* (forma HH:mm)
	*/
	private String rupturaMembranasMinutos = "";

	/** Descripción de las características del líquitdo amniótico */
	private String caracteristicasLiquidoAmniotico = "";

	/** Indica si hubo o no sufrimiento fetal */
	private String sufrimientoFetal = "";

	/**
	* Si el anterior atributo es "true", proporciona una descripción de
	* las causas del sufrimiento fetal
	*/
	private String causasSufrimientoFetal = "";

	/** Descripción de la anestesia y medicamentos usados durante el parto */
	private String anestesiaMedicamentos = "";

	/** Hora de nacimiento del niño (forma HH:mm) */
	private String horaNacimiento = "";

	/** Talla del infante, en centímetros */
	private String talla = "";

	/** Peso del niño, en gramos */
	private String peso = "";

	/** Perímetro cefálico del infante, en centímetros */
	private String perimetroCefalico = "";

	/** Perímetro torácico del infante, en centímetros */
	private String perimetroToracico = "";

	/** Descripción con las características de la placenta */
	private String caracteristicasPlacenta = "";

	/** Mapa con los tipos de parto que tuvo el niño */
	private Map tiposParto = new HashMap();
	private Map tiposPartoCarga = new HashMap();

	/**
	* Mapa con los motivos o explicaciones de los tipos de parto que tuvo el
	* niño
	*/
	private Map motivosTiposParto = new HashMap();
	private Map motivosTiposPartoCarga = new HashMap();

	/**
	* Si el tipo de parto no corresponde a uno de los predefinidos, en este
	* campo se indica cómo fue
	*/
	private String otroTipoParto = "";

	/** Valor del APGAR en el minuto 1 (entre 1-10) */
	private String apgarMinuto1 = "";

	/**
	* Pareja código-nombre con la valoración del  riesgo  APGAR, para el
	* minuto 1
	*/
	private String riesgoApgarMinuto1_cn = "";

	/** Valor del APGAR en el minuto 5 (entre 1-10) */
	private String apgarMinuto5 = "";

	/**
	* Pareja código-nombre con la valoración del  riesgo  APGAR,  para el
	* minuto 5
	*/
	private String riesgoApgarMinuto5_cn = "";

	/** Valor del APGAR en el minuto 10 (entre 1-10) */
	private String apgarMinuto10 = "";

	/**
	* Pareja código-nombre con la valoración del  riesgo  APGAR,  para el
	* minuto 10
	*/
	private String riesgoApgarMinuto10_cn = "";

	/**
	* Mapa con las inmunizaciones pediátricas del niño que tienen una dosis u
	* observacion
	*/
	private Map inmunizaciones = new HashMap();

	/** Mapa con las dosis de las inmunizaciones pediátricas del niño */
	private Map inmunizacionesPediatricas = new HashMap();
	private Map inmunizacionesPediatricasCarga = new HashMap();

	/** Mapa con las observaciones para cada una de las inmunizaciones pediátricas */
	private Map observacionesInmunizacionesPediatricas = new HashMap();
	private Map observacionesInmunizacionesPediatricasCarga = new HashMap();

	/** Mapa con las fechas de las dosis de las inmunizaciones pediatricas */
	private Map fechasDosisInmunizacionesPediatricas = new HashMap();
	private Map fechasDosisInmunizacionesPediatricasCarga = new HashMap();

	/**
	* Observaciones generales de un antecedente que vienen de carga.
	* Se califican como anteriores y  no se pueden modificar
	*/
	private String observacionesGeneralesAnteriores = "";

	/** Observaciones generales de este antecedente */
	private String observacionesGenerales = "";

	/**
	* Los tipos de parto se generan dinámicamente, usando como llave un char,
	* de la forma tiposParto([a-z]). Este String indica hasta cuál letra utilizamos.
	*/
	private String maxTiposParto = "";

	/**
	* Las inmunizaciones se generan dinámicamente, usando como llave un char,
	* de la forma inmunizacionesPediatricas([a-z]). Este String indica hasta
	* cuál letra utilizamos.
	*/
	private String maxVacunas = "";

	/** Indica si hubo o no un error en esta página */
	private String wasError = "";

	/** Código de la serologia del embarazo actual */
	private int ii_codigoSerologia;

	/** Código del test de O'Sullivan del embarazo actual */
	private int ii_codigoTestSullivan;

	/** Mapa con los valores de los campos de las opciones de embarazo */
	private final Map im_categoriaEmbarazoOpcionCampo = new HashMap();

	/** Indicador de amnionitis en el trabajo de parto */
	private String is_amnionitis;

	/** Descripción del factor anormal de amnionitis en el trabajo de parto */
	private String is_amnionitisFactorAnormal;

	/** Indicador de uso de anestesia en el trabajo de parto */
	private String is_anestesia;

	/** Descripción del tipo de anestesia usada en el trabajo de parto */
	private String is_anestesiaTipo;

	/** Descripcion de las complicaciones del parto */
	private String is_complicacionesParto;

	/** Campo indicador de control prenatal */
	private String is_controlPrenatal;

	/** Características del cordón umbilical */
	private String is_cordonUmbilicalCaracteristicas;

	/** Descripción del cordón umbilical */
	private String is_cordonUmbilicalDescripcion;

	/** Descripción de la serologia del embarazo actual */
	private String is_descripcionSerologia;

	/** Descripción del test de O'Sullivan del embarazo actual */
	private String is_descripcionTestSullivan;

	/** La parte de horas de la duración expulsivo (forma HH:mm) */
	private String is_duracionExpulsivoHoras;

	/** La parte de minutos de la duración expulsivo (forma HH:mm) */
	private String is_duracionExpulsivoMinutos;

	/** Edad gestacional */
	private String is_edadGestacional;

	/** Descripcion de la edad gestacional */
	private String is_edadGestacionalDescripcion;

	/** Campo Otros de la sección de embarazos anteriores */
	private String is_embarazosAnterioresOtros = "";

	/** Frecuencia del control prenatal */
	private String is_frecuenciaControlPrenatal;

	/** Indicador de si es o no gemelo */
	private String is_gemelo;

	/** Descripción del atributo gemelo */
	private String is_gemeloDescripcion;

	/** Campo Incompatibilidad ABO de la sección de embarazos anteriores */
	private String is_incompatibilidadABO = "";

	/** Campo Incompatibilidad RH de la sección de embarazos anteriores */
	private String is_incompatibilidadRH = "";

	/** Indicador de crecimiento intrauterino */
	private String is_intrauterinoPeg;

	/** Causa del indicador de crecimiento intrauterino */
	private String is_intrauterinoPegCausa;

	/** Porcentaje de anormalidad del crecimiento intrauterino */
	private String is_intrauterinoAnormalidad;

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

	/** Lugar del control prenatal */
	private String is_lugarControlPrenatal;

	/** Campo Macrosómicos de la sección de embarazos anteriores */
	private String is_macrosomicos = "";

	/** Campo Malformaciones Congénitas de la sección de embarazos anteriores */
	private String is_malformacionesCongenitas = "";

	/** Campo Mortinatos de la sección de embarazos anteriores */
	private String is_mortinatos = "";

	/** Campo Muertes Fetales Tempranas de la sección de embarazos anteriores */
	private String is_muertesFetalesTempranas = "";

	/** Indicador de toma de muestras del cordón umbilical */
	private String is_muestraCordonUmbilical = "";

	/** Descripción del indicador de toma de muestras del cordón umbilical */
	private String is_muestraCordonUmbilicalDescripcion = "";

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

	/** Caracteríscas de la placenta */
	private String is_placentaCaracteristicas;

	/** Descripción de las características de la placenta */
	private String is_placentaCaracteristicasDescripcion;

	/** Campo Prematuros de la sección de embarazos anteriores */
	private String is_prematuros = "";

	/** Indicador de ptc en el trabajo de parto */
	private String is_ptc;

	/** Descripción del tipo de ptc en el trabajo de parto */
	private String is_ptcDescripcion;

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

	/** Descripción del genero */
	private String is_sexoDescripcion;
	
	private String ocultarCabezotes;
	
	/**
	 * Variable para almacenar los otro Embarazos Anteriores 
	 */
	private HashMap mapaOtros; 

	/**
	 * Variable para almacenar los otro Embarazos Anteriores 
	 */
	private HashMap mapaPato;
	
	/**
	* Inicializa el form con la informacion de la madre que viene en el objeto AntecedentePediatrico
	* @param antecedente Objeto AntecedentePediatrico
	* @param antecedenteForm El form AntecedentePediatricoForm
	*/
	private void cargarFormInfoMadre(AntecedentePediatrico antecedente, AntecedentePediatricoForm antecedenteForm)
	{
		InfoMadre lim_madre;

		lim_madre = antecedente.getInfoMadre();

		if(lim_madre.getTipoSangre() != null)
		{
			antecedenteForm.setCodigoTipoSangreMadre(lim_madre.getTipoSangre().getCodigo() + "");
			antecedenteForm.setNombreTipoSangreMadre(lim_madre.getTipoSangre().getValue() );
		}
		else
			antecedenteForm.setCodigoTipoSangreMadre(null);

		if(lim_madre.getEdad() > 0)
			antecedenteForm.setEdadMadre(lim_madre.getEdad() + "");

		if(lim_madre.getIdentificacion() != null)
		{
			antecedenteForm.setNumeroIdentificacionMadre(lim_madre.getIdentificacion().getNumeroId().trim() );
			antecedenteForm.setTipoIdentificacionMadre(lim_madre.getIdentificacion().getTipoId().trim() );
		}

		if(lim_madre.getSemanasGestacion() > 0)
			antecedenteForm.setSemanasGestacion(lim_madre.getSemanasGestacion() + "");

		antecedenteForm.setCodigoMetodoSemanasGestacionMadre(lim_madre.getCodigoMetodoSemanasGestacion() );
		antecedenteForm.setConfiableSemanasGestacionMadre(lim_madre.getConfiableSemanasGestacion() );
		antecedenteForm.setFppMadre(lim_madre.getFpp() );
		antecedenteForm.setFurMadre(lim_madre.getFur() );
		antecedenteForm.setInfoEmbarazoAMadre(lim_madre.getInfoEmbarazoA() );
		antecedenteForm.setInfoEmbarazoCMadre(lim_madre.getInfoEmbarazoC() );
		antecedenteForm.setInfoEmbarazoGMadre(lim_madre.getInfoEmbarazoG() );
		antecedenteForm.setInfoEmbarazoMMadre(lim_madre.getInfoEmbarazoM() );
		antecedenteForm.setInfoEmbarazoPMadre(lim_madre.getInfoEmbarazoP() );
		antecedenteForm.setInfoEmbarazoVMadre(lim_madre.getInfoEmbarazoV() );
		antecedenteForm.setObservacionesEmbarazoMadreAnteriores(lim_madre.getObservaciones() );
		antecedenteForm.setObsExamenesMedicamentosMadreAnteriores(lim_madre.getObsExamenesMedicamentos() );
		antecedenteForm.setObsPatologiasMadreAnteriores(lim_madre.getObsPatologias() );
		antecedenteForm.setObservacionesEmbarazoMadreAnteriores(lim_madre.getObservaciones() );
		antecedenteForm.setObsExamenesMedicamentosMadreAnteriores(lim_madre.getObsExamenesMedicamentos() );
		antecedenteForm.setObsPatologiasMadreAnteriores(lim_madre.getObsPatologias() );
		antecedenteForm.setPrimerApellidoMadre(lim_madre.getPrimerApellido() );
		antecedenteForm.setPrimerNombreMadre(lim_madre.getPrimerNombre() );
		antecedenteForm.setSegundoApellidoMadre(lim_madre.getSegundoApellido() );
		antecedenteForm.setSegundoNombreMadre(lim_madre.getSegundoNombre() );
	}

	/**
	* Inicializa el form con la informacion del padre que viene en el objeto AntecedentePediatrico
	* @param antecedente Objeto AntecedentePediatrico
	* @param antecedenteForm El form AntecedentePediatricoForm
	*/
	private void cargarFormInfoPadre(AntecedentePediatrico antecedente, AntecedentePediatricoForm antecedenteForm)
	{
		InfoPadre lip_padre;

		lip_padre = antecedente.getInfoPadre();

		if(lip_padre.getTipoSangre() != null)
		{
			antecedenteForm.setCodigoTipoSangrePadre(lip_padre.getTipoSangre().getCodigo() + "");
			antecedenteForm.setNombreTipoSangrePadre(lip_padre.getTipoSangre().getValue() );
		}
		else
			antecedenteForm.setCodigoTipoSangrePadre(null);

		if(lip_padre.getEdad() > 0)
			antecedenteForm.setEdadPadre(lip_padre.getEdad() + "");

		if(lip_padre.getIdentificacion() != null)
		{
			antecedenteForm.setNumeroIdentificacionPadre(lip_padre.getIdentificacion().getNumeroId().trim() );
			antecedenteForm.setTipoIdentificacionPadre(lip_padre.getIdentificacion().getTipoId().trim() );
		}

		antecedenteForm.setConsanguinidadPadre(lip_padre.getConsanguinidad() );
		antecedenteForm.setPrimerApellidoPadre(lip_padre.getPrimerApellido() );
		antecedenteForm.setPrimerNombrePadre(lip_padre.getPrimerNombre() );
		antecedenteForm.setSegundoApellidoPadre(lip_padre.getSegundoApellido() );
		antecedenteForm.setSegundoNombrePadre(lip_padre.getSegundoNombre() );
	}

	/**
	* Retorna la acción a ejecutarse con este form.
	* @return la acción a ejecutarse con este form
	*/
	public String getAccion()
	{
		return accion;
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

	/**
	* Retorna la anestesia y los medicamentos usados durante el parto.
	* @return la anestesia y los medicamentos usados durante el parto
	*/
	public String getAnestesiaMedicamentos()
	{
		return anestesiaMedicamentos;
	}

	/** Obtiene el tipo de anestesia usada en el trabajo de parto */
	public String getAnestesiaTipo()
	{
		return is_anestesiaTipo;
	}

	/**
	* Retorna el valor del APGAR para el minuto 1.
	* @return el valor del APGAR para el minuto 1
	*/
	public String getApgarMinuto1()
	{
		return apgarMinuto1;
	}

	/**
	* Retorna el valor del APGAR para el minuto 5.
	* @return el valor del APGAR para el minuto 5
	*/
	public String getApgarMinuto5()
	{
		return apgarMinuto5;
	}

	/**
	* Retorna las características del líquido amniótico.
	* @return las características del líquido amniótico
	*/
	public String getCaracteristicasLiquidoAmniotico()
	{
		return caracteristicasLiquidoAmniotico;
	}

	/**
	* Retorna la características de la placenta.
	* @return la características de la placenta
	*/
	public String getCaracteristicasPlacenta()
	{
		return caracteristicasPlacenta;
	}

	/**
	* Retorna las causas del sufrimiento fetal.
	* @return las causas del sufrimiento fetal
	*/
	public String getCausasSufrimientoFetal()
	{
		return causasSufrimientoFetal;
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

	/**
	* Retorna el comentario sobre el tipo de trabajo de parto.
	* @return comentario sobre el tipo de trabajo de parto
	*/
	public String getComentarioTipoTrabajoParto()
	{
		return comentarioTipoTrabajoParto;
	}

	/** Obtiene la descripcion de las complicaciones del parto */
	public String getComplicacionesParto()
	{
		return is_complicacionesParto;
	}

	/** Obtiene la consanguinidad del padre */
	public String getConsanguinidadPadre()
	{
		return is_consanguinidadPadre;
	}

	/** Obtiene el control prenatal */
	public String getControlPrenatal()
	{
		return is_controlPrenatal;
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
	public String getDuracionExpulsivoHoras()
	{
		return is_duracionExpulsivoHoras;
	}

	/** Obtiene el número de minutos de duración del expulsivo */
	public String getDuracionExpulsivoMinutos()
	{
		return is_duracionExpulsivoMinutos;
	}

	/**
	* Retorna la duración del parto en horas.
	* @return la duración del parto en horas
	*/
	public String getDuracionPartoHoras()
	{
		return duracionPartoHoras;
	}

	/**
	* Retorna la duración del parto en minutos.
	* @return la duración del parto en minutos
	*/
	public String getDuracionPartoMinutos()
	{
		return duracionPartoMinutos;
	}

	/** Obtiene la edad gestacional */
	public String getEdadGestacional()
	{
		return is_edadGestacional;
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

	/**
	* Rettorna un Map con las fechas de las inmunizaciones pediátricas.
	* @returnlas fechas de las inmunizaciones pediátricas
	*/
	public Map getFechasDosisInmunizacionesPediatricas()
	{
		return fechasDosisInmunizacionesPediatricas;
	}

	/** Obtiene la frecuencia del control prenatal */
	public String getFrecuenciaControlPrenatal()
	{
		return is_frecuenciaControlPrenatal;
	}

	/**
	* Retorna la hora de nacimiento del niño.
	* @return la hora de nacimiento del niño
	*/
	public String getHoraNacimiento()
	{
		return horaNacimiento;
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

	/**
	* Retorna un Map con las inmunizaciones.
	* @return las inmunizaciones
	*/
	public Map getInmunizaciones()
	{
		return inmunizaciones;
	}

	/**
	* Retorna un Map con las inmunizaciones pediátricas.
	* @return las inmunizaciones pediátricas
	*/
	public Map getInmunizacionesPediatricas()
	{
		return inmunizacionesPediatricas;
	}

	/**
	* Retorna un mapa con las la inmunizaciones pediátricas aplicadas al niño.
	* @param key llave para recuperar una inmunización en particular
	* @return la inmunización pediátrica aplicada al niño
	*/
	public String getInmunizacionesPediatricas(String key)
	{
		return (String) this.inmunizacionesPediatricas.get(key);
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
	public String getIntrauterinoAnormalidad()
	{
		return is_intrauterinoAnormalidad;
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
		return is_lugarControlPrenatal;
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

	/**
	* Retorna el String máximo usado como llave de tiposParto.
	* @return el String máximo usado como llave de tiposParto
	*/
	public String getMaxTiposParto()
	{
		return maxTiposParto;
	}

	/**
	* Retorna el String máximo usado como llave de inmunizacionesPediatricas.
	* @return el String máximo usado como llave de inmunizacionesPediatricas
	*/
	public String getMaxVacunas()
	{
		return maxVacunas;
	}

	/** Obtiene el valor de el campo Mortinatos de la sección de embarazos anteriores */
	public String getMortinatos()
	{
		return is_mortinatos;
	}

	/**
	* Retorna los motivos de un tipo de parto.
	* @return Map con los motivos de un tipo de parto
	*/
	public Map getMotivosTiposParto()
	{
		return motivosTiposParto;
	}

	/**
	* Retorna un tipo de parto en particular.
	* @param key llave para recuperar un tipo de parto en particular
	* @return un tipo de parto en particular
	*/
	public String getMotivosTiposParto(String key)
	{
		return (String) motivosTiposParto.get(key);
	}

	/**
	* Retorna un tipo de parto en particular.
	* @param key llave para recuperar un tipo de parto en particular
	* @return un tipo de parto en particular
	*/
	public String getMotivosTiposParto(char key)
	{
		return (String) motivosTiposParto.get(key + "");
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

	/**
	* Retorna las observaciones generales.
	* @return observaciones generales
	*/
	public String getObservacionesGenerales()
	{
		return observacionesGenerales;
	}

	/**
	* Retorna las observaciones generales anteriores.
	* @return las observaciones generales anteriores
	*/
	public String getObservacionesGeneralesAnteriores()
	{
		return observacionesGeneralesAnteriores;
	}

	/**
	* Retorna el Map con las observaciones de inmunizaciones pediátricas.
	* @return las observaciones de inmunizaciones pediátricas
	*/
	public Map getObservacionesInmunizacionesPediatricas()
	{
		return observacionesInmunizacionesPediatricas;
	}

	/**
	* Retorna una observación en particular para una inmunización recibida por el niño.
	* @param key llave para recuperar una inmunización inmunización en particular
	* @return una observación en particular para una inmunización recibida por el niño
	*/
	public String getObservacionesInmunizacionesPediatricas(String key)
	{
		return (String) observacionesInmunizacionesPediatricas.get(key);
	}

	/**
	* En caso que la presentación de nacimiento sea "otra", retorna un texto diciendo cuál fue.
	* @return presentación del nacimiento
	*/
	public String getOtraPresentacionNacimiento()
	{
		return otraPresentacionNacimiento;
	}

	/** Obtiene los otros tipos de exámenes del trabajo de parto */
	public String getOtrosExamenesTrabajoParto()
	{
		return is_otrosExamenesTrabajoParto;
	}

	/**
	* En caso que el tipo de parto sea "otro", retorna un texto diciendo cuál fue.
	* @return tipo del parto
	*/
	public String getOtroTipoParto()
	{
		return otroTipoParto;
	}

	/** Obtiene el perfil biofísico del trabajo de parto */
	public String getPerfilBiofisico()
	{
		return is_perfilBiofisico;
	}

	/** Obtiene el perfil biofísico del trabajo de parto */
	public String getPerfilBiofisicoDescripcion()
	{
		return is_perfilBiofisicoDescripcion;
	}

	/**
	* Retorna el peso del niño.
	* @return el peso del niño
	*/
	public String getPeso()
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

	/**
	* Retorna el código-nombre de la presentacion del nacimiento.
	* @return el código-nombre de la presentacion del nacimiento
	*/
	public String getPresentacionNacimiento_cn()
	{
		return presentacionNacimiento_cn;
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

	/**
	* Retorna el código-nombre del riesgo APGAR para el minuto 1.
	* @return el código-nombre del riesgo APGAR para el minuto 1
	*/
	public String getRiesgoApgarMinuto1_cn()
	{
		return riesgoApgarMinuto1_cn;
	}

	/**
	* Retorna el código-nombre del riesgo APGAR para el minuto 5.
	* @return el código-nombre del riesgo APGAR para el minuto 5
	*/
	public String getRiesgoApgarMinuto5_cn()
	{
		return riesgoApgarMinuto5_cn;
	}

	/**
	* Retorna las horas antes del parto de la ruptura de membranas.
	* @return la hora de la ruptura de membranas
	*/
	public String getRupturaMembranasHoras()
	{
		return rupturaMembranasHoras;
	}

	/**
	* Retorna los minutos antes del parto de la ruptura de membranas.
	* @return los minutos de la ruptura de membranas
	*/
	public String getRupturaMembranasMinutos()
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

	/**
	* Retorna si hubo o no sufrimiento fetal.
	* @return "true" si hubo sufrimiento fetal, "false" si no.
	*/
	public String getSufrimientoFetal()
	{
		return sufrimientoFetal;
	}

	/**
	* Retorna la talla del niño.
	* @return la talla del niño
	*/
	public String getTalla()
	{
		return talla;
	}

	/**
	* Retorna los Tipos de Parto.
	* @return Map con los tipos de parto
	*/
	public Map getTiposParto()
	{
		return tiposParto;
	}

	/**
	* Retorna un tipo de parto en particular.
	* @param key llave para recuperar un tipo de parto en particular
	* @return un tipo de parto en particular
	*/
	public String getTiposParto(String key)
	{
		return (String) tiposParto.get(key);
	}

	/**
	* Retorna un tipo de parto en particular.
	* @param key llave para recuperar un tipo de parto en particular
	* @return un tipo de parto en particular
	*/
	public String getTiposParto(char key)
	{
		return (String) tiposParto.get(key + "");
	}

	/**
	* Retorna el código-nombre del tipo de trabajo de parto.
	* @return código-nombre del tipo de trabajo de parto
	*/
	public String getTipoTrabajoParto_cn()
	{
		return tipoTrabajoParto_cn;
	}

	/**
	* Retorna el valor del flag 'wasError'
	* @return "true" si el método validate() encontró un error; "false" si no.
	*/
	public String getWasError()
	{
		return wasError;
	}

	/**
	* Establece la acción a ejecutarse con este form.
	* @param accion la acción a ejecutarse que se desea establecer
	*/
	public void setAccion(String accion)
	{
		this.accion = accion;
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

	/**
	* Establece la anestesia y medicamentos utilizados durante el parto.
	* @param anestesiaMedicamentos la anestesia y medicamentos a establecer
	*/
	public void setAnestesiaMedicamentos(String anestesiaMedicamentos)
	{
		this.anestesiaMedicamentos = anestesiaMedicamentos;
	}

	/** Asigna el tipo de anestesia usada en el trabajo de parto */
	public void setAnestesiaTipo(String as_anestesiaTipo)
	{
		if(as_anestesiaTipo != null)
			is_anestesiaTipo = as_anestesiaTipo.trim();
	}

	/**
	* Establece el valor de APGAR para el minuto 1.
	* @param apgarMinuto1 el APGAR para el minuto 1 a establecer
	*/
	public void setApgarMinuto1(String apgarMinuto1)
	{
		this.apgarMinuto1 = apgarMinuto1;
	}

	/**
	* Establece el valor de APGAR para el minuto 5.
	* @param apgarMinuto5 el APGAR para el minuto 5 a establecer
	*/
	public void setApgarMinuto5(String apgarMinuto5)
	{
		this.apgarMinuto5 = apgarMinuto5;
	}

	/**
	* Establece las características del líquido amniotico.
	* @param caracteristicasLiquidoAmniotico las características del líquido amniotico a establecer
	*/
	public void setCaracteristicasLiquidoAmniotico(String caracteristicasLiquidoAmniotico)
	{
		this.caracteristicasLiquidoAmniotico = caracteristicasLiquidoAmniotico;
	}

	/**
	* Establece las características de la placenta.
	* @param caracteristicasPlacenta las características de la placenta a establecer
	*/
	public void setCaracteristicasPlacenta(String caracteristicasPlacenta)
	{
		this.caracteristicasPlacenta = caracteristicasPlacenta;
	}

	/**
	* Establece las causas del sufrimiento fetal.
	* @param causasSufrimientoFetal las causas del sufrimiento fetal a establecer
	*/
	public void setCausasSufrimientoFetal(String causasSufrimientoFetal)
	{
		this.causasSufrimientoFetal = causasSufrimientoFetal;
	}

	/** Asigna el codigo de la serología del embarazo actual */
	public void setCodigoSerologia(int ai_codigoSerologia)
	{
		ii_codigoSerologia = (ai_codigoSerologia > 0) ? ai_codigoSerologia: -1;
	}

	/** Asigna el codigo del test de O'Sullivan del embarazo actual */
	public void setCodigoTestSullivan(int ai_codigoTestSullivan)
	{
		ii_codigoTestSullivan = (ai_codigoTestSullivan > 0) ? ai_codigoTestSullivan: -1;
	}

	/**
	* Establece el comentario para el tipo de trabajo de parto.
	* @param comentarioTipoTrabajoParto el comentario para el tipo de trabajo
	* de parto a establecer.
	*/
	public void setComentarioTipoTrabajoParto(String comentarioTipoTrabajoParto)
	{
		this.comentarioTipoTrabajoParto = comentarioTipoTrabajoParto;
	}

	/** Asigna la descripcion de las complicaciones del parto */
	public void setComplicacionesParto(String as_complicacionesParto)
	{
		if(as_complicacionesParto != null)
			is_complicacionesParto = as_complicacionesParto.trim();
	}

	/** Asigna la consangionidad del padre */
	public void setConsanguinidadPadre(String as_consanguinidadPadre)
	{
		if(as_consanguinidadPadre != null)
			is_consanguinidadPadre = as_consanguinidadPadre.trim();
	}

	/** Asigna el control prenatal */
	public void setControlPrenatal(String as_controlPrenatal)
	{
		if(as_controlPrenatal != null)
			is_controlPrenatal = as_controlPrenatal.trim();
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
	public void setDuracionExpulsivoHoras(String as_duracionExpulsivoHoras)
	{
		if(as_duracionExpulsivoHoras != null)
			is_duracionExpulsivoHoras = as_duracionExpulsivoHoras.trim();
	}

	/** Asigna el número de minutos de duración del expulsivo */
	public void setDuracionExpulsivoMinutos(String as_duracionExpulsivoMinutos)
	{
		if(as_duracionExpulsivoMinutos != null)
			is_duracionExpulsivoMinutos = as_duracionExpulsivoMinutos.trim();
	}

	/**
	* Establece la duración del parto en horas.
	* @param duracionPartoHoras la duración del parto en horas a establecer
	*/
	public void setDuracionPartoHoras(String duracionPartoHoras)
	{
		this.duracionPartoHoras = duracionPartoHoras;
	}

	/**
	* Establece la duración del parto en minutos.
	* @param duracionPartoMinutos la duración del parto en minutos a establecer
	*/
	public void setDuracionPartoMinutos(String duracionPartoMinutos)
	{
		this.duracionPartoMinutos = duracionPartoMinutos;
	}

	/** Asigna la edad gestacional */
	public void setEdadGestacional(String as_edadGestacional)
	{
		if(as_edadGestacional != null)
			is_edadGestacional = as_edadGestacional.trim();
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
	* Establece un Map con las fechas de las dosis de
	* inmunizaciones pediátricas.
	* @param fechasDosisInmunizacionesPediatricas las fechas de las dosis de
	* inmunizaciones pediátricas a establecer
	*/
	public void setFechasDosisInmunizacionesPediatricas(Map fechasDosisInmunizacionesPediatricas)
	{
		this.fechasDosisInmunizacionesPediatricas = fechasDosisInmunizacionesPediatricas;
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

	/**
	* Establece la hora de nacimiento.
	* @param horaNacimiento la hora de nacimiento a establecer
	*/
	public void setHoraNacimiento(String horaNacimiento)
	{
		this.horaNacimiento = horaNacimiento;
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
	/**
	* Establece un Map con las inmunizaciones.
	* @param inmunizaciones las inmunizaciones a establecer
	*/
	public void setInmunizaciones(Map inmunizaciones)
	{
		this.inmunizaciones = inmunizaciones;
	}

	/**
	* Establece un Map con las inmunizaciones pediátricas.
	* @param inmunizacionesPediatricas las inmunizaciones pediátricas a establecer
	*/
	public void setInmunizacionesPediatricas(Map inmunizacionesPediatricas)
	{
		this.inmunizacionesPediatricas = inmunizacionesPediatricas;
	}

	/**
	* Establece una inmunización pediátrica, dada su llave.
	* @param key llave para guardar esta inmunización pediátrica
	* @param inmunizacionPediatrica la inmunización pediátrica a establecer
	*/
	@SuppressWarnings("unchecked")
	public void setInmunizacionesPediatricas(String key, String inmunizacionPediatrica)
	{
		this.inmunizacionesPediatricas.put(key, inmunizacionPediatrica);
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
	public void setIntrauterinoAnormalidad(String as_intrauterinoAnormalidad)
	{
		if(as_intrauterinoAnormalidad != null)
			is_intrauterinoAnormalidad = as_intrauterinoAnormalidad.trim();
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
			is_lugarControlPrenatal = as_lugarControlPrenatal.trim();
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

	/**
	* Establece el String máximo usado como llave de tiposParto.
	* @param maxTiposParto el String máximo usado como llave de tiposParto a establecer
	*/
	public void setMaxTiposParto(String maxTiposParto)
	{
		this.maxTiposParto = maxTiposParto;
	}

	/**
	* Establece el String máximo usado como llave de inmunizacionesPediatricas.
	* @param maxVacunas el String máximo usado como llave de inmunizacionesPediatricas a establecer
	*/
	public void setMaxVacunas(String maxVacunas)
	{
		this.maxVacunas = maxVacunas;
	}

	/** Asigna el valor del campo Mortinatos de la sección embarazos anteriores */
	public void setMortinatos(String as_mortinatos)
	{
		if(as_mortinatos != null)
			is_mortinatos = as_mortinatos.trim();
	}

	/**
	* Establece el motivo de un tipo de parto.
	* @param key llave para guardar este motivo de tipo de parto
	* @param tipoParto el motivo de tipo de parto a establecer
	*/
	@SuppressWarnings("unchecked")
	public void setMotivosTiposParto(String key, String motivoTipoParto)
	{
		this.motivosTiposParto.put(key, motivoTipoParto);
	}

	/**
	* Establece un Map con los motivos de los tipos de parto.
	* @param motivosTiposParto los motivos de los tipos de parto a establecer
	*/
	public void setMotivosTiposParto(Map motivosTiposParto)
	{
		this.motivosTiposParto = motivosTiposParto;
	}

	/**
	* Establece el motivo de un tipo de parto.
	* @param key llave para guardar este motivo de tipo de parto
	* @param tipoParto el motivo de tipo de parto a establecer
	*/
	@SuppressWarnings("unchecked")
	public void setMotivosTiposParto(char key, String motivoTipoParto)
	{
		this.motivosTiposParto.put(key + "", motivoTipoParto);
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

	/**
	* Establece las observaciones generales.
	* @param observacionesGenerales las observaciones generales a establecer
	*/
	public void setObservacionesGenerales(String observacionesGenerales)
	{
		this.observacionesGenerales = observacionesGenerales;
	}

	/**
	* Establece las observaciones generales anteriores.
	* @param observacionesGeneralesAnteriores las observaciones generales anteriores a establecer
	*/
	public void setObservacionesGeneralesAnteriores(String observacionesGeneralesAnteriores)
	{
		this.observacionesGeneralesAnteriores = observacionesGeneralesAnteriores;
	}

	/**
	* Establece las observaciones para las inmunizaciones pediátricas.
	* @param key llave para guardar esta observación de inmunización pediátrica
	* @param tipoParto la observación de inmunización pediátrica a establecer
	*/
	@SuppressWarnings("unchecked")
	public void setObservacionesInmunizacionesPediatricas(String key, String observacionesInmunizacionesPediatricas)
	{
		this.observacionesInmunizacionesPediatricas.put(key, observacionesInmunizacionesPediatricas);
	}

	/**
	* Establece un Map con las observaciones para las inmunizaciones
	* pediátricas.
	* @param observacionesInmunizacionesPediatricas las observaciones para las inmunizaciones pediátricas a establecer
	*/
	public void setObservacionesInmunizacionesPediatricas(Map observacionesInmunizacionesPediatricas)
	{
		this.observacionesInmunizacionesPediatricas = observacionesInmunizacionesPediatricas;
	}

	/**
	* Establece las observaciones para las inmunizaciones pediátricas.
	* @param key llave para guardar esta observación de inmunización pediátrica
	* @param tipoParto la observación de inmunización pediátrica a establecer
	*/
	@SuppressWarnings("unchecked")
	public void setObservacionesInmunizacionesPediatricas(char key, String observacionesInmunizacionesPediatricas)
	{
		this.observacionesInmunizacionesPediatricas.put(key + "", observacionesInmunizacionesPediatricas);
	}

	/**
	* Establece la otra presentación de nacimiento.
	* @param otraPresentacionNacimiento la otra presentación de nacimiento a establecer
	*/
	public void setOtraPresentacionNacimiento(String otraPresentacionNacimiento)
	{
		this.otraPresentacionNacimiento = otraPresentacionNacimiento;
	}

	/** Asigna los otros tipos de exámenes del trabajo de parto */
	public void setOtrosExamenesTrabajoParto(String as_otrosExamenesTrabajoParto)
	{
		if(as_otrosExamenesTrabajoParto != null)
			is_otrosExamenesTrabajoParto = as_otrosExamenesTrabajoParto.trim();
	}

	/**
	* Establece el otro tipo de parto.
	* @param otroTipoParto el otro tipo de parto a establecer
	*/
	public void setOtroTipoParto(String otroTipoParto)
	{
		this.otroTipoParto = otroTipoParto;
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

	/**
	* Establece el peso.
	* @param peso el peso a establecer
	*/
	public void setPeso(String peso)
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

	/**
	* Establece el código-nombre de la presentacion de nacimiento.
	* @param presentacionNacimiento_cn el código-nombre de la presentacion de nacimiento a establecer
	*/
	public void setPresentacionNacimiento_cn(String presentacionNacimiento_cn)
	{
		this.presentacionNacimiento_cn = presentacionNacimiento_cn;
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

	/**
	* Establece el código-nombre del riesgo APGAR para el minuto 1.
	* @param riesgoApgarMinuto1_cn el código-nombre del riesgo APGAR para el minuto 1 a establecer
	*/
	public void setRiesgoApgarMinuto1_cn(String riesgoApgarMinuto1_cn)
	{
		this.riesgoApgarMinuto1_cn = riesgoApgarMinuto1_cn;
	}

	/**
	* Establece el código-nombre del riesgo APGAR para el minuto 5.
	* @param riesgoApgarMinuto1_cn el código-nombre del riesgo APGAR para el minuto 5 a establecer
	*/
	public void setRiesgoApgarMinuto5_cn(String riesgoApgarMinuto5_cn)
	{
		this.riesgoApgarMinuto5_cn = riesgoApgarMinuto5_cn;
	}

	/**
	* Establece las horas antes del parto de la ruptura de membranas.
	* @param rupturaMembranasHoras la hora de ruptura de membranas a establecer
	*/
	public void setRupturaMembranasHoras(String rupturaMembranasHoras)
	{
		this.rupturaMembranasHoras = rupturaMembranasHoras;
	}

	/**
	* Establece los minutos antes del parto de la ruptura de membranas.
	* @param rupturaMembranasHoras los minutos de ruptura de membranas a establecer
	*/
	public void setRupturaMembranasMinutos(String rupturaMembranasMinutos)
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

	/**
	* Establece si hubo o no sufrimiento fetal ("true" o "false").
	* @param sufrimientoFetal el sufrimiento fetal a establecer
	*/
	public void setSufrimientoFetal(String sufrimientoFetal)
	{
		this.sufrimientoFetal = sufrimientoFetal;
	}

	/**
	* Establece la talla.
	* @param talla la talla a establecer
	*/
	public void setTalla(String talla)
	{
		this.talla = talla;
	}

	/**
	* Establece un tipo de parto.
	* @param key llave para guardar este tipo de parto
	* @param tipoParto el tipo de parto a establecer
	*/
	@SuppressWarnings("unchecked")
	public void setTiposParto(String key, String tipoParto)
	{
		this.tiposParto.put(key, tipoParto);
	}

	/**
	* Establece un Map con los tipos de parto.
	* @param tiposParto los tipos de parto a establecer
	*/
	public void setTiposParto(Map tiposParto)
	{
		this.tiposParto = tiposParto;
	}

	/**
	* Establece un tipo de parto.
	* @param key llave para guardar este tipo de parto
	* @param tipoParto el tipo de parto a establecer
	*/
	@SuppressWarnings("unchecked")
	public void setTiposParto(char key, String tipoParto)
	{
		this.tiposParto.put(key + "", tipoParto);
	}

	/**
	* Establece la pareja código-nombre para el tipo de trabajo de parto.
	* @param tipoTrabajoParto_cn la pareja código-nombre para el tipo de
	* trabajo de parto a establecer.
	*/
	public void setTipoTrabajoParto_cn(String tipoTrabajoParto_cn)
	{
		this.tipoTrabajoParto_cn = tipoTrabajoParto_cn;
	}

	/**
	* Establece el valor del flag 'wasError'.
	* @param wasError "true" si el método validate() encontró un error; "false" si no
	*/
	public void setWasError(String wasError)
	{
		this.wasError = wasError;
	}

	
	
	
	
	/**
	 * @return Returns the inmunizacionesList.
	 */
	public ArrayList getInmunizacionesList() {
		return inmunizacionesList;
	}
	/**
	 * @param inmunizacionesList The inmunizacionesList to set.
	 */
	public void setInmunizacionesList(ArrayList inmunizacionesList) {
		this.inmunizacionesList = inmunizacionesList;
	}
	/**
	 * @return Returns the tiposPartoList.
	 */
	public ArrayList getTiposPartoList() {
		return tiposPartoList;
	}
	/**
	 * @param tiposPartoList The tiposPartoList to set.
	 */
	public void setTiposPartoList(ArrayList tiposPartoList) {
		this.tiposPartoList = tiposPartoList;
	}
	/**
	* Establece en valores vacíos, mas no nulos, los atributos de este objeto.
	*/
	public void clean()
	{
		antPed = new AntecedentePediatrico();
		accion = "";
		duracionPartoHoras = "";
		duracionPartoMinutos = "";
		tipoTrabajoParto_cn = "";
		comentarioTipoTrabajoParto = "";
		presentacionNacimiento_cn = "";
		otraPresentacionNacimiento = "";
		ubicacionFeto_cn = "";
		otraUbicacionFeto = "";
		rupturaMembranasHoras = "";
		rupturaMembranasMinutos = "";
		caracteristicasLiquidoAmniotico = "";
		sufrimientoFetal = "";
		causasSufrimientoFetal = "";
		anestesiaMedicamentos = "";
		horaNacimiento = "";
		talla = "";
		peso = "";
		perimetroCefalico = "";
		perimetroToracico = "";
		caracteristicasPlacenta = "";
		tiposParto = new HashMap();
		tiposPartoCarga = new HashMap();
		motivosTiposParto = new HashMap();
		motivosTiposPartoCarga = new HashMap();
		otroTipoParto = "";
		apgarMinuto1 = "";
		riesgoApgarMinuto1_cn = "";
		apgarMinuto5 = "";
		riesgoApgarMinuto5_cn = "";
		apgarMinuto10 = "";
		riesgoApgarMinuto10_cn = "";
		inmunizaciones = new HashMap();
		inmunizacionesPediatricas = new HashMap();
		inmunizacionesPediatricasCarga = new HashMap();
		observacionesInmunizacionesPediatricas = new HashMap();
		observacionesInmunizacionesPediatricasCarga = new HashMap();
		fechasDosisInmunizacionesPediatricas = new HashMap();
		fechasDosisInmunizacionesPediatricasCarga = new HashMap();
		observacionesGeneralesAnteriores = "";
		observacionesGenerales = "";
		maxTiposParto = "";
		maxVacunas = "";
		wasError = "";
		textareaMotivosParto = "";
		textareaObservacionesInmunizacion = "";

		//Seccion informacion de la madre durante el embarazo
		codigoTipoSangreMadre					= "";
		edadMadre								= "";
		ii_codigoMetodoMadre					= 0;
		is_confiableSemanasMadre				= "";
		is_fppMadre								= "";
		is_furMadre								= "";
		is_infoEmbarazoAMadre					= "";
		is_infoEmbarazoCMadre					= "";
		is_infoEmbarazoGMadre					= "";
		is_infoEmbarazoMMadre					= "";
		is_infoEmbarazoPMadre					= "";
		is_infoEmbarazoVMadre					= "";
		is_numeroIdentificacionMadre			= "";
		is_primerApellidoMadre					= "";
		is_primerNombreMadre					= "";
		is_segundoApellidoMadre					= "";
		is_segundoNombreMadre					= "";
		is_tipoIdentificacionMadre				= "";
		nombreTipoSangreMadre					= "";
		observacionesEmbarazoMadre				= "";
		observacionesEmbarazoMadreAnteriores	= "";
		obsExamenesMedicamentosMadre			= "";
		obsExamenesMedicamentosMadreAnteriores	= "";
		obsPatologiasMadre						= "";
		obsPatologiasMadreAnteriores			= "";
		semanasGestacion						= "";

		/* Sección de información del padre */
		is_codigoTipoSangrePadre		= "";
		is_consanguinidadPadre			= "";
		is_edadPadre					= "";
		is_nombreTipoSangrePadre		= "";
		is_numeroIdentificacionPadre	= "";
		is_primerApellidoPadre			= "";
		is_primerNombrePadre			= "";
		is_segundoApellidoPadre			= "";
		is_segundoNombrePadre			= "";
		is_tipoIdentificacionPadre		= "";

		/* Información de embarazos anteriores */
		is_embarazosAnterioresOtros	= "";
		is_incompatibilidadABO		= "";
		is_incompatibilidadRH		= "";
		is_macrosomicos				= "";
		is_malformacionesCongenitas	= "";
		is_mortinatos				= "";
		is_muertesFetalesTempranas	= "";
		is_prematuros				= "";

		/* Información de embarazo actual */
		ii_codigoSerologia				= -1;
		ii_codigoTestSullivan			= -1;
		is_controlPrenatal				= "";
		is_descripcionSerologia			= "";
		is_descripcionTestSullivan		= "";
		is_frecuenciaControlPrenatal	= "";
		is_lugarControlPrenatal			= "";

		/* Información de las opciones de embarazos */
		im_categoriaEmbarazoOpcionCampo.clear();

		is_duracionExpulsivoHoras		= "";
		is_duracionExpulsivoMinutos		= "";
		is_anestesia					= "";
		is_anestesiaTipo				= "";
		is_amnionitis					= "";
		is_amnionitisFactorAnormal		= "";
		is_nst							= "";
		is_nstDescripcion				= "";
		is_otrosExamenesTrabajoParto	= "";
		is_perfilBiofisico				= "";
		is_perfilBiofisicoDescripcion	= "";
		is_ptc							= "";
		is_ptcDescripcion				= "";

		/* Información de atención del parto */
		is_complicacionesParto					= "";
		is_cordonUmbilicalCaracteristicas		= "";
		is_cordonUmbilicalDescripcion			= "";
		is_gemelo								= "";
		is_gemeloDescripcion					= "";
		is_intrauterinoPeg						= "";
		is_intrauterinoPegCausa					= "";
		is_intrauterinoAnormalidad				= "";
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

		is_edadGestacional						= "";
		is_edadGestacionalDescripcion			= "";
		is_liqAmnioticoClaro					= "";
		is_liqAmnioticoMeconiado				= "";
		is_liqAmnioticoMeconiadoGrado			= "";
		is_liqAmnioticoSanguinolento			= "";
		is_liqAmnioticoFetido					= "";
		is_placentaCaracteristicas				= "";
		is_placentaCaracteristicasDescripcion	= "";
	
		tiposPartoList.clear(); 
		inmunizacionesList.clear();
		
		
		this.mapaOtros = new HashMap();
		this.mapaPato = new HashMap();
		}

	/**
	* Este método inicializa en valores vacíos los atributos del antecedente
	* pediátrico. Si ya se habían insertado antecedentes pediátricos para un
	* paciente dado, se pre-cargan sus datos. Si no, se inicializan en valores
	* vacíos todos los atributos de este form.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que está siendo procesado en
	* este momento
	*/
	@SuppressWarnings("unchecked")
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		AntecedentePediatrico antecedente;

		if(mapping == null); // NOP para evitar el warning "The argument mapping is never read"

		antecedente = (AntecedentePediatrico)request.getAttribute("antecedentePediatrico");

		//Si el paciente tiene antecedente pediatrico, llenamos los datos del form con la informacion que viene de su antecedente
		if(antecedente != null)
		{
			ArrayList					inmunizaciones;
			ArrayList					lal_embarazoOpciones;
			ArrayList					lasDosis;
			ArrayList					tiposParto;
			Dosis						laDosis;
			InfoDatos					lid_opcion;
			InfoDatosBD					tipoParto;
			InmunizacionesPediatricas	inmunizacion;
			int							codigoParto;
			int							numeroDosis;
			String						obsInmunizacion;
			String						otroTipoParto;

			validate(mapping, request);
			clean();

			antPed = antecedente;

			/* Esta 'accion' se pone en 'modificar' */
			accion = "modificar";

			/* Sección información de la madre durante el embarazo */
			cargarFormInfoMadre(antecedente, this);

			/* Sección información del padre */
			cargarFormInfoPadre(antecedente, this);

			/* Solo se inicializan aquellos que vienen llenos */
			if(antecedente.getDuracionPartoHoras() != -1)
				duracionPartoHoras = antecedente.getDuracionPartoHoras() + "";

			if(antecedente.getDuracionPartoMinutos() != -1)
				duracionPartoMinutos = antecedente.getDuracionPartoMinutos() + "";

			if(!antecedente.getAnestesiaMedicamentos().equals("") )
				anestesiaMedicamentos = antecedente.getAnestesiaMedicamentos();

			if(antecedente.getApgarMinuto1() != -1)
			{
				apgarMinuto1			= antecedente.getApgarMinuto1() + "";
				riesgoApgarMinuto1_cn	= antecedente.getRiesgoApgarMinuto1().getCodigo() + "-" + antecedente.getRiesgoApgarMinuto1().getValue();
			}
			if(antecedente.getApgarMinuto5() != -1)
			{
				apgarMinuto5			= antecedente.getApgarMinuto5() + "";
				riesgoApgarMinuto5_cn	= antecedente.getRiesgoApgarMinuto5().getCodigo() + "-" + antecedente.getRiesgoApgarMinuto5().getValue();
			}

			if(antecedente.getApgarMinuto10() != -1)
			{
				apgarMinuto10			= antecedente.getApgarMinuto10() + "";
				riesgoApgarMinuto10_cn	= antecedente.getRiesgoApgarMinuto10().getCodigo() + "-" + antecedente.getRiesgoApgarMinuto10().getValue();
			}

			/* Sección de embarazos anteriores */
			setEmbarazosAnterioresOtros(antecedente.getEmbarazosAnterioresOtros() );
			setIncompatibilidadABO(antecedente.getIncompatibilidadABO() );
			setIncompatibilidadRH(antecedente.getIncompatibilidadRH() );
			setMacrosomicos(antecedente.getMacrosomicos() );
			setMalformacionesCongenitas(antecedente.getMalformacionesCongenitas() );
			setMortinatos(antecedente.getMortinatos() );
			setMuertesFetalesTempranas(antecedente.getMuertesFetalesTempranas() );
			setPrematuros(antecedente.getPrematuros() );

			/* Sección de embarazo actual */
			setCodigoSerologia(antecedente.getCodigoSerologia() );
			setCodigoTestSullivan(antecedente.getCodigoTestSullivan() );
			setControlPrenatal(antecedente.getControlPrenatal() );
			setDescripcionSerologia(antecedente.getDescripcionSerologia() );
			setDescripcionTestSullivan(antecedente.getDescripcionTestSullivan() );
			setFrecuenciaControlPrenatal(antecedente.getFrecuenciaControlPrenatal() );
			setLugarControlPrenatal(antecedente.getLugarControlPrenatal() );

			/* Sección de trabajo de parto */
			setAmnionitis(antecedente.getAmnionitis() );
			setAmnionitisFactorAnormal(antecedente.getAmnionitisFactorAnormal() );
			setAnestesia(antecedente.getAnestesia() );
			setAnestesiaTipo(antecedente.getAnestesiaTipo() );

			if(antecedente.getDuracionExpulsivoHoras() != -1)
				setDuracionExpulsivoHoras(Integer.toString(antecedente.getDuracionExpulsivoHoras() ) );
			if(antecedente.getDuracionExpulsivoMinutos() != -1)
				setDuracionExpulsivoMinutos(Integer.toString(antecedente.getDuracionExpulsivoMinutos() ) );

			setNst(antecedente.getNst() );
			setNstDescripcion(antecedente.getNstDescripcion() );
			setOtrosExamenesTrabajoParto(antecedente.getOtrosExamenesTrabajoParto() );
			setPerfilBiofisico(antecedente.getPerfilBiofisico() );
			setPerfilBiofisicoDescripcion(antecedente.getPerfilBiofisicoDescripcion() );
			setPtc(antecedente.getPtc() );
			setPtcDescripcion(antecedente.getPtcDescripcion() );

			/* Información de la sección atención del parto */
			if(antecedente.getEdadGestacional() != -1)
				setEdadGestacional(Integer.toString(antecedente.getEdadGestacional() ) );

			if(antecedente.getIntrauterinoAnormalidad() != -1)
				setIntrauterinoAnormalidad(Integer.toString(antecedente.getIntrauterinoAnormalidad() ) );

			setComplicacionesParto(antecedente.getComplicacionesParto() );
			setCordonUmbilicalCaracteristicas(antecedente.getCordonUmbilicalCaracteristicas() );
			setCordonUmbilicalDescripcion(antecedente.getCordonUmbilicalDescripcion() );
			setEdadGestacionalDescripcion(antecedente.getEdadGestacionalDescripcion() );
			setGemelo(antecedente.getGemelo() );
			setGemeloDescripcion(antecedente.getGemeloDescripcion() );
			setIntrauterinoPeg(antecedente.getIntrauterinoPeg() );
			setIntrauterinoPegCausa(antecedente.getIntrauterinoPegCausa() );
			setIntrauterinoAnormalidadCausa(antecedente.getIntrauterinoAnormalidadCausa() );
			setIntrauterinoArmonico(antecedente.getIntrauterinoArmonico() );
			setIntrauterinoArmonicoCausa(antecedente.getIntrauterinoArmonicoCausa() );
			setLiqAmnioticoClaro(antecedente.getLiqAmnioticoClaro() );
			setLiqAmnioticoMeconiado(antecedente.getLiqAmnioticoMeconiado() );
			setLiqAmnioticoMeconiadoGrado(antecedente.getLiqAmnioticoMeconiadoGrado() );
			setLiqAmnioticoSanguinolento(antecedente.getLiqAmnioticoSanguinolento() );
			setLiqAmnioticoFetido(antecedente.getLiqAmnioticoFetido() );
			setMuestraCordonUmbilical(antecedente.getMuestraCordonUmbilical() );
			setMuestraCordonUmbilicalDescripcion(antecedente.getMuestraCordonUmbilicalDescripcion() );
			setPlacentaCaracteristicas(antecedente.getPlacentaCaracteristicas() );
			setPlacentaCaracteristicasDescripcion(antecedente.getPlacentaCaracteristicasDescripcion() );
			setReanimacion(antecedente.getReanimacion() );
			setReanimacionAspiracion(antecedente.getReanimacionAspiracion() );
			setReanimacionMedicamentos(antecedente.getReanimacionMedicamentos() );
			setSano(antecedente.getSano() );
			setSanoDescripcion(antecedente.getSanoDescripcion() );
			setSexoDescripcion(antecedente.getSexoDescripcion() );

			if(!antecedente.getCaracteristicasLiquidoAmniotico().equals("") )
				caracteristicasLiquidoAmniotico = antecedente.getCaracteristicasLiquidoAmniotico();

			if(!antecedente.getCaracteristicasPlacenta().equals("") )
				caracteristicasPlacenta = antecedente.getCaracteristicasPlacenta();

			if(antecedente.getSufrimientoFetal().equals("true") )
			{
				sufrimientoFetal		= "yes";
				causasSufrimientoFetal	= antecedente.getCausasSufrimientoFetal();
			}
			else if(antecedente.getSufrimientoFetal().equals("false") )
				sufrimientoFetal = "no";

			if(!antecedente.getHoraNacimiento().equals("") )
				horaNacimiento = antecedente.getHoraNacimiento();

			if(antecedente.getRupturaMembranasHoras() != -1)
				rupturaMembranasHoras = antecedente.getRupturaMembranasHoras() + "";

			if(antecedente.getRupturaMembranasMinutos() != -1)
				rupturaMembranasMinutos = antecedente.getRupturaMembranasMinutos() + "";

			if(antecedente.getTalla() != -1)
				talla = antecedente.getTalla() + "";

			if(antecedente.getPeso() != -1)
				peso = antecedente.getPeso() + "";

			if(antecedente.getPerimetroCefalico() != -1)
				perimetroCefalico = antecedente.getPerimetroCefalico() + "";

			if(antecedente.getPerimetroToracico() != -1)
				perimetroToracico = antecedente.getPerimetroToracico() + "";

			if(antecedente.getTipoTrabajoParto() != null)
			{
				tipoTrabajoParto_cn			= antecedente.getTipoTrabajoParto().getCodigo() + "-" + antecedente.getTipoTrabajoParto().getValue();
				comentarioTipoTrabajoParto	= antecedente.getTipoTrabajoParto().getDescripcion();
			}

			/* Si pone otra presentacion nacimiento esta obligado a explicar cual es esa otra */
			if(antecedente.getPresentacionNacimiento() == null && !antecedente.getOtraPresentacionNacimiento().equals("") )
			{
				otraPresentacionNacimiento = antecedente.getOtraPresentacionNacimiento();

				/* Debe quedar seleccionado en el select la opcion 'Otro' */
				presentacionNacimiento_cn = "0-Otra";
			}
			else if(antecedente.getPresentacionNacimiento() != null)
				/* Si tiene alguna presentacion nacimiento diferente a 'Otra' */
				presentacionNacimiento_cn = antecedente.getPresentacionNacimiento().getCodigo() + "-" + antecedente.getPresentacionNacimiento().getValue();

			/* Si pone otra ubicacion feto esta obligado a explicar cual es esa otra */
			if(antecedente.getUbicacionFeto() == null && !antecedente.getOtraUbicacionFeto().equals("") )
			{
				otraUbicacionFeto = antecedente.getOtraUbicacionFeto();

				/* Debe quedar seleccionado en el select la opcion 'Otro' */
				ubicacionFeto_cn = "0-Otra";
			}
			else if(antecedente.getUbicacionFeto() != null)
				/* Si tiene alguna ubicacion feto diferente a 'Otra' */
				ubicacionFeto_cn = antecedente.getUbicacionFeto().getCodigo() + "-" + antecedente.getUbicacionFeto().getValue();

			/* Si tiene tipos de parto los debo poner en el Map. Tener en cuenta las observaciones */
			if( (tiposParto = antecedente.getTiposParto() ) != null)
			{
				
				
				for(int i = 0, size = tiposParto.size(); i < size; i++)
				{
					/* El problema es que no se a que letra corresponde el codigo del parto */
					tipoParto	= (InfoDatosBD) tiposParto.get(i);
					codigoParto	= tipoParto.getCodigo();

					/* OJO y si codigoParto es cero? */
					this.tiposParto.put(codigoParto + "", codigoParto + "-" + tipoParto.getValue() );
					motivosTiposParto.put(codigoParto + "", tipoParto.getDescripcion() );
				
				
					tiposPartoList.add(tipoParto);
					
				}
			}

			// OJO: Si se pone otro tipo de parto esta obligado a especificar cuál
			if( ( otroTipoParto = antecedente.getOtroTipoParto() ) != null && !otroTipoParto.trim().equals("") )
			{
				this.otroTipoParto = otroTipoParto;

				/* Si tiene escrito un motivo de este tipo de parto se debe incluir en el Map */
				this.tiposParto.put("0", "0-Otro");

				/* Se debe meter su comentario en un cuadernito para que vea lo anterior que sabemos que no es modificable */
				motivosTiposParto.put("0", antecedente.getMotivoTipoPartoOtro() );
				
				tipoParto	= new InfoDatosBD(0, "Otro", antecedente.getMotivoTipoPartoOtro());
				tiposPartoList.add(tipoParto);
			}

			/*
				Si se tienen dosis de inmunizaciones se deben poner en el Map, siguiendo esta convención :
				Dosis               :=> key = dosis_b_1, valor = 1_BCG
				FechasDosis         :=> key = fecha_b_1, valor = <la fecha>
				ObservacionesInmuni :=> key = b,         valor = <la observacion>
			*/
			if( (inmunizaciones = antecedente.getInmunizacionesPediatricas() ) != null)
			{
				
				this.inmunizacionesList.addAll(inmunizaciones); 
				
				for(int i = 0, size = inmunizaciones.size(); i < size; i++)
				{
					inmunizacion = (InmunizacionesPediatricas)inmunizaciones.get(i);
					this.inmunizaciones.put(inmunizacion.getCodigoInmunizacion() + "", "true");

					/* Inicializando las observaciones para las inmunizaciones que las tienen */
					//obsInmunizacion = darObservacionesHTML(inmunizacion.getObservaciones() );
					obsInmunizacion=inmunizacion.getObservaciones() ;
					observacionesInmunizacionesPediatricasCarga.put(inmunizacion.getCodigoInmunizacion() + "", obsInmunizacion);
					lasDosis = inmunizacion.getDosis();

					if(lasDosis != null)
					{
						for(int j = 0, dosisSize = lasDosis.size(); j < dosisSize; j++)
						{
							laDosis		= (Dosis)lasDosis.get(j);
							numeroDosis	= laDosis.getNumeroDosis();

							inmunizacionesPediatricas.put("dosis_" + inmunizacion.getCodigoInmunizacion()+ "_" + numeroDosis, inmunizacion.getCodigoInmunizacion() + "_" + inmunizacion.getNombreInmunizacion() );
							if(laDosis.getFecha() != null && !laDosis.getFecha().trim().equals("") )
								fechasDosisInmunizacionesPediatricas.put("fecha_" + inmunizacion.getCodigoInmunizacion() + "_" + numeroDosis, laDosis.getFecha() );
						}
					}
				}
			}

			/* Cargar la información de opciones de embarazo */
			lal_embarazoOpciones = antecedente.getEmbarazoOpcionCampos();

			for(int li_i = 0, li_tam = lal_embarazoOpciones.size(); li_i < li_tam; li_i++)
			{
				lid_opcion	= (InfoDatos)lal_embarazoOpciones.get(li_i);
				
				setEmbarazoOpcionCampo(lid_opcion.getAcronimo(), lid_opcion.getValue() );
			}

			/* Iniciando las observaciones anteriores en caso de que hayan */
			//observacionesGeneralesAnteriores = darObservacionesHTML(antecedente.getObservaciones(false) );
			observacionesGeneralesAnteriores =antecedente.getObservaciones(false);
			/*
				Estos son valores que no inicializan en este punto :
				this.observacionesGenerales = "";
				this.maxTiposParto = "";
				this.maxVacunas = "";
			*/
			tiposPartoCarga.putAll(this.tiposParto);
			motivosTiposPartoCarga.putAll(motivosTiposParto);
			inmunizacionesPediatricasCarga.putAll(inmunizacionesPediatricas);
			fechasDosisInmunizacionesPediatricasCarga.putAll(fechasDosisInmunizacionesPediatricas);

			/* OJO : Si vamos a pre-popular el form con un jsp de setup previo, esto no es necesario */
			request.getSession().setAttribute("antecedentePediatrico", antecedente);
		}
		else if(accion.equals("") )
		{
			clean();

			/* Paciente actual */
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

			if(paciente == null || paciente.getCodigoPersona() < 0)
			{
				accion ="error";
				super.setValidatorResults(null);
			}
			else
				accion = "ingresar";
		}
	}

	public void validarCamposOtros(HttpServletRequest request)
	{
		if(request.getParameter("otraUbicacionFeto" ) == null )
			otraUbicacionFeto = "";
		if(request.getParameter("otraPresentacionNacimiento" ) == null )
			otraPresentacionNacimiento = "";
		if(request.getParameter("otroTipoParto" ) == null)
			otroTipoParto = "";
		if(request.getParameter("causasSufrimientoFetal") == null)
			causasSufrimientoFetal = "";
	}

	/* ******************** WARNING ***********************/
	/*
	/**
	* Retorna las observaciones generales anteriores, para ser mostradas
	* en la página HTML.
	* @return las observaciones generales anteriores.
	*/
	/*private static String darObservacionesHTML(String observacionesBD) {

		if(observacionesBD != null && !observacionesBD.trim().equals("") )
		{
			// Constantes usadas para dar formato a las observaciones
			final String c1 = ", ";
			final char   c2 = '\n';
			final String c4 = "\n\n";
			final String c5 = "Especialidades: ";
			final String c6 =". ";
			StringTokenizer observaciones, datosObservacion;
			StringBuffer observacionesEncoded = new StringBuffer();
			String fechaObservacion, horaObservacion, medicoObservacion, registroMedicoObservacion, observacion;
			String especialidadesStr = "";

			/*
				Antes de mostrar la cadena de observaciones se debe interpretar la informacion que esta guardada, teniendo en cuenta
				que las observaciones se guardan en el siguiente formato :
				<fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>_&_<fecha>|<hora>|<nombreMedico>|<numRegistro>|<observacion>
			*/

	/*		observaciones = new StringTokenizer(observacionesBD, "_&_");
			while (observaciones.hasMoreTokens() )
			{
				observacion = observaciones.nextToken();
				
				datosObservacion = new StringTokenizer(observacion, "|");
				fechaObservacion = datosObservacion.nextToken();
				horaObservacion = datosObservacion.nextToken();
				//El medico va a tener las especialidades, formato:
				//[nombre_medico]:[especi1], [espe2]
				// Estos dos puntos aparecen en caso de que tenga especialidades
				medicoObservacion = datosObservacion.nextToken();
				if(medicoObservacion.indexOf(':') != -1){
					especialidadesStr = c6+c5+medicoObservacion.substring(medicoObservacion.indexOf(':')+1);
					medicoObservacion = medicoObservacion.substring(0, medicoObservacion.indexOf(':') );
				}
				else especialidadesStr = "";
				//El registro del medico incluye el texto 'RM' y 'MP' que indica si el numero es un Registro medico
				//o una matriculo profesional
				registroMedicoObservacion = datosObservacion.nextToken();
				observacion = datosObservacion.nextToken();

				//OJO arreglar el formato en que se va a mostrar
				observacionesEncoded.append(fechaObservacion).append(c1).append(horaObservacion).append(c2).append(observacion).append(c2).append(medicoObservacion).append(especialidadesStr).append(c6).append(registroMedicoObservacion).append(c4);

			}

			String resp = observacionesEncoded.toString();
			return resp.substring(0, resp.length() - 2);
		}
		else {
			return "";
		}
	}*/

	public String getTextareaMotivosParto()
	{
		return textareaMotivosParto;
	}

	public String getTextareaObservacionesInmunizacion()
	{
		return textareaObservacionesInmunizacion;
	}

	public void setTextareaMotivosParto(String textareaMotivosParto)
	{
		this.textareaMotivosParto = textareaMotivosParto;
	}

	public void setTextareaObservacionesInmunizacion(String textareaObservacionesInmunizacion)
	{
		this.textareaObservacionesInmunizacion = textareaObservacionesInmunizacion;
	}

	public AntecedentePediatrico getAntPed()
	{
		return antPed;
	}

	public void setAntPed(AntecedentePediatrico antPed)
	{
		this.antPed = antPed;
	}

	public Map getTiposPartoCarga()
	{
		return tiposPartoCarga;
	}

	public void setTiposPartoCarga(Map tiposPartoCarga)
	{
		this.tiposPartoCarga = tiposPartoCarga;
	}

	public Map getFechasDosisInmunizacionesPediatricasCarga()
	{
		return fechasDosisInmunizacionesPediatricasCarga;
	}

	public Map getInmunizacionesPediatricasCarga()
	{
		return inmunizacionesPediatricasCarga;
	}

	public void setFechasDosisInmunizacionesPediatricasCarga(Map fechasDosisInmunizacionesPediatricasCarga)
	{
		this.fechasDosisInmunizacionesPediatricasCarga =
			fechasDosisInmunizacionesPediatricasCarga;
	}

	public void setInmunizacionesPediatricasCarga(Map inmunizacionesPediatricasCarga)
	{
		this.inmunizacionesPediatricasCarga = inmunizacionesPediatricasCarga;
	}

	public String getApgarMinuto10()
	{
		return apgarMinuto10;
	}

	public String getRiesgoApgarMinuto10_cn()
	{
		return riesgoApgarMinuto10_cn;
	}

	public void setApgarMinuto10(String string)
	{
		apgarMinuto10 = string;
	}

	public void setRiesgoApgarMinuto10_cn(String string)
	{
		riesgoApgarMinuto10_cn = string;
	}

	public String getPerimetroCefalico()
	{
		return perimetroCefalico;
	}

	public String getPerimetroToracico()
	{
		return perimetroToracico;
	}

	public void setPerimetroCefalico(String string)
	{
		perimetroCefalico = string;
	}

	public void setPerimetroToracico(String string)
	{
		perimetroToracico = string;
	}

	public String getOtraUbicacionFeto()
	{
		return otraUbicacionFeto;
	}

	public String getUbicacionFeto_cn()
	{
		return ubicacionFeto_cn;
	}

	public void setOtraUbicacionFeto(String string)
	{
		otraUbicacionFeto = string;
	}

	public void setUbicacionFeto_cn(String string)
	{
		ubicacionFeto_cn = string;
	}

	/** Obtiene el código del método de semanas de gestación de la madre*/
	public int getCodigoMetodoSemanasGestacionMadre()
	{
		return ii_codigoMetodoMadre;
	};

	/** Obtiene el código del tipo de sangre de la madre */
	public String getCodigoTipoSangreMadre()
	{
		return codigoTipoSangreMadre;
	}

	/** Obtiene el código del tipo de sangre del padre */
	public String getCodigoTipoSangrePadre()
	{
		return is_codigoTipoSangrePadre;
	}

	/** Obtiene el indicador de confiabilidad de las semanas de gestación de la madre */
	public String getConfiableSemanasGestacionMadre()
	{
		return is_confiableSemanasMadre;
	};

	/** Obtiene la edad de la madre al momento de la gestación */
	public String getEdadMadre()
	{
		return edadMadre;
	}

	/** Obtiene la edad del padre */
	public String getEdadPadre()
	{
		return is_edadPadre;
	}

	/** Obtiene los valores de los campos de la opciones de embarazo */
	public Map getEmbarazoOpcionCampos()
	{
		return im_categoriaEmbarazoOpcionCampo;
	}

	/**
	* Obtiene el valor del campo de las opciones de
	* embarazo identificado con la llave as_llave
	* @param as_llave Identificador del campo a obtener
	*/
	public String getEmbarazoOpcionCampo(String as_llave)
	{
		if(as_llave != null && !(as_llave = as_llave.trim() ).equals("") )
			return (String)im_categoriaEmbarazoOpcionCampo.get(as_llave);
		else
			return "";
	}

	/** Obtiene la fecha PP de la madre */
	public String getFppMadre()
	{
		return is_fppMadre;
	}

	/** Obtiene la fehca UR de la madre */
	public String getFurMadre()
	{
		return is_furMadre;
	}

	/** Obtiene la infomación A de los embarazos de la madre */
	public String getInfoEmbarazoAMadre()
	{
		return is_infoEmbarazoAMadre;
	}

	/** Obtiene la infomación C de los embarazos de la madre */
	public String getInfoEmbarazoCMadre()
	{
		return is_infoEmbarazoCMadre;
	}

	/** Obtiene la infomación G de los embarazos de la madre */
	public String getInfoEmbarazoGMadre()
	{
		return is_infoEmbarazoGMadre;
	}

	/** Obtiene la infomación M de los embarazos de la madre */
	public String getInfoEmbarazoMMadre()
	{
		return is_infoEmbarazoMMadre;
	}

	/** Obtiene la infomación P de los embarazos de la madre */
	public String getInfoEmbarazoPMadre()
	{
		return is_infoEmbarazoPMadre;
	}

	/** Obtiene la infomación V de los embarazos de la madre */
	public String getInfoEmbarazoVMadre()
	{
		return is_infoEmbarazoVMadre;
	}

	/** Obtiene el nombre del tipo de sangre de la madre */
	public String getNombreTipoSangreMadre()
	{
		return nombreTipoSangreMadre;
	}

	/** Obtiene el nombre del tipo de sangre del padre */
	public String getNombreTipoSangrePadre()
	{
		return is_nombreTipoSangrePadre;
	}

	public Map getMotivosTiposPartoCarga()
	{
		return motivosTiposPartoCarga;
	}

	/** Obtiene el número de identificación de la madre */
	public String getNumeroIdentificacionMadre()
	{
		return is_numeroIdentificacionMadre;
	}

	/** Obtiene el número de identificación del padre */
	public String getNumeroIdentificacionPadre()
	{
		return is_numeroIdentificacionPadre;
	}

	public String getObservacionesEmbarazoMadre()
	{
		return observacionesEmbarazoMadre;
	}

	public String getObservacionesEmbarazoMadreAnteriores()
	{
		return observacionesEmbarazoMadreAnteriores;
	}

	public Map getObservacionesInmunizacionesPediatricasCarga()
	{
		return observacionesInmunizacionesPediatricasCarga;
	}

	public String getObsExamenesMedicamentosMadre()
	{
		return obsExamenesMedicamentosMadre;
	}

	public String getObsExamenesMedicamentosMadreAnteriores()
	{
		return obsExamenesMedicamentosMadreAnteriores;
	}

	public String getObsPatologiasMadre()
	{
		return obsPatologiasMadre;
	}

	public String getObsPatologiasMadreAnteriores()
	{
		return obsPatologiasMadreAnteriores;
	}

	/** Obtiene el primer apellido de la madre */
	public String getPrimerApellidoMadre()
	{
		return is_primerApellidoMadre;
	}

	/** Obtiene el primer apellido del padre */
	public String getPrimerApellidoPadre()
	{
		return is_primerApellidoPadre;
	}

	/** Obtiene el primer nombre de la madre */
	public String getPrimerNombreMadre()
	{
		return is_primerNombreMadre;
	}

	/** Obtiene el primer nombre del padre */
	public String getPrimerNombrePadre()
	{
		return is_primerNombrePadre;
	}

	/** Obtiene el segundo apellido del madre */
	public String getSegundoApellidoMadre()
	{
		return is_segundoApellidoMadre;
	}

	/** Obtiene el segundo apellido del padre */
	public String getSegundoApellidoPadre()
	{
		return is_segundoApellidoPadre;
	}

	/** Obtiene el segundo nombre de la madre */
	public String getSegundoNombreMadre()
	{
		return is_segundoNombreMadre;
	}

	/** Obtiene el segundo nombre del padre */
	public String getSegundoNombrePadre()
	{
		return is_segundoNombrePadre;
	}

	public String getSemanasGestacion()
	{
		return semanasGestacion;
	}

	/** Obtiene el tipo de identificación de la madre */
	public String getTipoIdentificacionMadre()
	{
		return is_tipoIdentificacionMadre;
	}

	/** Obtiene el tipo de identificación del padre */
	public String getTipoIdentificacionPadre()
	{
		return is_tipoIdentificacionPadre;
	}

	/** Asigna el código del método de semanas de gestación de la madre*/
	public void setCodigoMetodoSemanasGestacionMadre(int ai_codigoMetodoMadre)
	{
		if(ai_codigoMetodoMadre > 0)
			ii_codigoMetodoMadre = ai_codigoMetodoMadre;
	};

	/** Asigna el indicador de confiabilidad de las semanas de gestación */
	public void setConfiableSemanasGestacionMadre(String as_confiableSemanasMadre)
	{
		if(as_confiableSemanasMadre != null)
			is_confiableSemanasMadre = as_confiableSemanasMadre.trim();
	};

	/** Asigna el código del tipo de sangre de la madre */
	public void setCodigoTipoSangreMadre(String string)
	{
		codigoTipoSangreMadre = (string != null) ? string.trim() : "-1";
	}

	/** Asigna el código del tipo de sangre del padre */
	public void setCodigoTipoSangrePadre(String as_codigoTipoSangrePadre)
	{
		is_codigoTipoSangrePadre =
			(as_codigoTipoSangrePadre != null) ? as_codigoTipoSangrePadre.trim() : "-1";
	}

	/** Asigna la edad de la madre al momento de la gestación */
	public void setEdadMadre(String string)
	{
		edadMadre = string;
	}

	/** Asigna la edad del padre */
	public void setEdadPadre(String as_edadPadre)
	{
		if(as_edadPadre != null)
			is_edadPadre = as_edadPadre.trim();
	}

	/**
	* Asigna el valor del campo de las opciones de
	* embarazo identificado por la llave as_llave
	* @param as_llave Identificador del campo a asignar
	* @param as_valor Valor del campo
	*/
	@SuppressWarnings("unchecked")
	public void setEmbarazoOpcionCampo(String as_llave, String as_valor)
	{
		if
		(
			as_llave != null && !(as_llave = as_llave.trim() ).equals("") &&
			as_valor != null && !(as_valor = as_valor.trim() ).equals("")
		)
			im_categoriaEmbarazoOpcionCampo.put(as_llave, as_valor);
	}

	/** Asigna la fecha PP de la madre */
	public void setFppMadre(String as_fppMadre)
	{
		if(as_fppMadre != null)
			is_fppMadre = as_fppMadre.trim();
	}

	/** Asigna la fecha UR de la madre */
	public void setFurMadre(String as_furMadre)
	{
		if(as_furMadre != null)
			is_furMadre = as_furMadre.trim();
	}

	/** Asigna la información A de los embarazos de la madre */
	public void setInfoEmbarazoAMadre(String as_infoEmbarazoAMadre)
	{
		if(as_infoEmbarazoAMadre != null)
			is_infoEmbarazoAMadre = as_infoEmbarazoAMadre.trim();
	}

	/** Asigna la información C de los embarazos de la madre */
	public void setInfoEmbarazoCMadre(String as_infoEmbarazoCMadre)
	{
		if(as_infoEmbarazoCMadre != null)
			is_infoEmbarazoCMadre = as_infoEmbarazoCMadre.trim();
	}

	/** Asigna la información G de los embarazos de la madre */
	public void setInfoEmbarazoGMadre(String as_infoEmbarazoGMadre)
	{
		if(as_infoEmbarazoGMadre != null)
			is_infoEmbarazoGMadre = as_infoEmbarazoGMadre.trim();
	}

	/** Asigna la información M de los embarazos de la madre */
	public void setInfoEmbarazoMMadre(String as_infoEmbarazoMMadre)
	{
		if(as_infoEmbarazoMMadre != null)
			is_infoEmbarazoMMadre = as_infoEmbarazoMMadre.trim();
	}

	/** Asigna la información P de los embarazos de la madre */
	public void setInfoEmbarazoPMadre(String as_infoEmbarazoPMadre)
	{
		if(as_infoEmbarazoPMadre != null)
			is_infoEmbarazoPMadre = as_infoEmbarazoPMadre.trim();
	}

	/** Asigna la información V de los embarazos de la madre */
	public void setInfoEmbarazoVMadre(String as_infoEmbarazoVMadre)
	{
		if(as_infoEmbarazoVMadre != null)
			is_infoEmbarazoVMadre = as_infoEmbarazoVMadre.trim();
	}

	/** Obtiene el nombre del tipo de sangre de la madre */
	public void setNombreTipoSangreMadre(String string)
	{
		nombreTipoSangreMadre = string;
	}

	/** Obtiene el nombre del tipo de sangre del padre */
	public void setNombreTipoSangrePadre(String as_nombreTipoSangrePadre)
	{
		if(as_nombreTipoSangrePadre != null)
			is_nombreTipoSangrePadre = as_nombreTipoSangrePadre.trim();
	}

	public void setMotivosTiposPartoCarga(Map map)
	{
		motivosTiposPartoCarga = map;
	}

	/** Asigna el número de identificación de la madre */
	public void setNumeroIdentificacionMadre(String as_numeroIdentificacionMadre)
	{
		if(as_numeroIdentificacionMadre != null)
			is_numeroIdentificacionMadre = as_numeroIdentificacionMadre.trim();
	}

	/** Asigna el número de identificación del padre */
	public void setNumeroIdentificacionPadre(String as_numeroIdentificacionPadre)
	{
		if(as_numeroIdentificacionPadre != null)
			is_numeroIdentificacionPadre = as_numeroIdentificacionPadre.trim();
	}

	public void setObservacionesEmbarazoMadre(String string)
	{
		observacionesEmbarazoMadre = string;
	}

	public void setObservacionesEmbarazoMadreAnteriores(String string)
	{
		observacionesEmbarazoMadreAnteriores = string;
	}

	public void setObservacionesInmunizacionesPediatricasCarga(Map map)
	{
		observacionesInmunizacionesPediatricasCarga = map;
	}

	public void setObsExamenesMedicamentosMadre(String string)
	{
		obsExamenesMedicamentosMadre = string;
	}

	public void setObsExamenesMedicamentosMadreAnteriores(String string)
	{
		obsExamenesMedicamentosMadreAnteriores = string;
	}

	public void setObsPatologiasMadre(String string)
	{
		obsPatologiasMadre = string;
	}

	public void setObsPatologiasMadreAnteriores(String string)
	{
		obsPatologiasMadreAnteriores = string;
	}

	/** Asigna el primer apellido de la madre */
	public void setPrimerApellidoMadre(String as_primerApellidoMadre)
	{
		if(as_primerApellidoMadre != null)
			is_primerApellidoMadre = as_primerApellidoMadre.trim();
	}

	/** Asigna el primer apellido del padre */
	public void setPrimerApellidoPadre(String as_primerApellidoPadre)
	{
		if(as_primerApellidoPadre != null)
			is_primerApellidoPadre = as_primerApellidoPadre.trim();
	}

	/** Asigna el primer nombre de la madre */
	public void setPrimerNombreMadre(String as_primerNombreMadre)
	{
		if(as_primerNombreMadre != null)
			is_primerNombreMadre = as_primerNombreMadre.trim();
	}

	/** Asigna el primer nombre del padre */
	public void setPrimerNombrePadre(String as_primerNombrePadre)
	{
		if(as_primerNombrePadre != null)
			is_primerNombrePadre = as_primerNombrePadre.trim();
	}

	/** Asigna el segundo apellido de la madre */
	public void setSegundoApellidoMadre(String as_segundoApellidoMadre)
	{
		if(as_segundoApellidoMadre != null)
			is_segundoApellidoMadre = as_segundoApellidoMadre.trim();
	}

	/** Asigna el segundo apellido del padre */
	public void setSegundoApellidoPadre(String as_segundoApellidoPadre)
	{
		if(as_segundoApellidoPadre != null)
			is_segundoApellidoPadre = as_segundoApellidoPadre.trim();
	}

	/** Asigna el segundo nombre de la madre */
	public void setSegundoNombreMadre(String as_segundoNombreMadre)
	{
		if(as_segundoNombreMadre != null)
			is_segundoNombreMadre = as_segundoNombreMadre.trim();
	}

	/** Asigna el segundo nombre del padre */
	public void setSegundoNombrePadre(String as_segundoNombrePadre)
	{
		if(as_segundoNombrePadre != null)
			is_segundoNombrePadre = as_segundoNombrePadre.trim();
	}

	public void setSemanasGestacion(String string)
	{
		semanasGestacion = string;
	}

	/** Asigna el tipo de identificación de la madre */
	public void setTipoIdentificacionMadre(String as_tipoIdentificacionMadre)
	{
		if(as_tipoIdentificacionMadre != null)
			is_tipoIdentificacionMadre = as_tipoIdentificacionMadre.trim();
	}

	/** Asigna el tipo de identificación del padre */
	public void setTipoIdentificacionPadre(String as_tipoIdentificacionPadre)
	{
		if(as_tipoIdentificacionPadre != null)
			is_tipoIdentificacionPadre = as_tipoIdentificacionPadre.trim();
	}
	/**
	 * @return Returns the ocultarCabezotes.
	 */
	public String getOcultarCabezotes() {
		return ocultarCabezotes;
	}
	/**
	 * @param ocultarCabezotes The ocultarCabezotes to set.
	 */
	public void setOcultarCabezotes(String ocultarCabezotes) {
		this.ocultarCabezotes = ocultarCabezotes;
	}
	/**
	* Valida las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validacón encontrados. Si no se encontraron errores de validación, retorna
	* <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que está siendo procesado en
	* este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores
	* encontrados al validar este formulario, o <code>null</code> si no se encontraron
	* errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		/*
			Establecemos radio buttons y checkboxes que fueron des-seleccionados
			en este request, en un valor apropiado.
		*/
		if (this.accion.equalsIgnoreCase("resumenIngreso"))
		{
			return null;
		}
		
		if(!this.accion.equalsIgnoreCase("volverAModificar") )
		{
			Collection	coll;
			Iterator	iter;
			String		key;

			validarCamposOtros(request);

			coll	= (Collection)getTiposParto().keySet();
			iter	= coll.iterator();
			key		= null;

			while(iter.hasNext() )
				if
				(
					request.getParameter("tiposParto(" + (key = (String)iter.next() )+ ")") == null &&
					tiposPartoCarga.get(key + "") == null
				)
				{
					setTiposParto(key, "false");
					setMotivosTiposParto(key, "");
				}

			iter	= (coll = (Collection)getInmunizacionesPediatricas().keySet() ).iterator();
			key		= null;

			while(iter.hasNext() )
				if
				(
					request.getParameter("inmunizacionesPediatricas(" + (key = (String)iter.next() )+ ")") == null &&
					inmunizacionesPediatricasCarga.get(key) == null
				)
					setInmunizacionesPediatricas(key, "false");
		}

		if(accion.equalsIgnoreCase("cargar") || accion.equalsIgnoreCase("resumen") )
		{
			super.setValidatorResults(null);
			return null;
		}

		if
		(
			accion.equalsIgnoreCase("consultarInmunizaciones")	||
			accion.equalsIgnoreCase("consultarTiposParto")		||
			accion.equalsIgnoreCase("volverAModificar")
		)
		{
			super.setValidatorResults(null);
			return null;
		}

		/* Validamos usando las validaciones estándar definidas en validation.xml */
		ActionErrors errors =  new ActionErrors();//super.validate(mapping, request);
		
		//--------Se realizan validaciones para guardar la información -------//
		if (accion.equals("ingresar") || accion.equals("modificar"))
		{
			//------ Se valida el formato de la hora de nacimiento -----------//
			if (UtilidadCadena.noEsVacio(this.horaNacimiento))
			{
				if (!UtilidadFecha.validacionHora(this.horaNacimiento).puedoSeguir)
				{
				errors.add("Formato hora invalido", new ActionMessage("errors.formatoHoraInvalido","de Nacimiento"));
				}
			}
			
			//*********VALIDACION SECCION APGAR***********************************************
			if(!this.getApgarMinuto1().equals("")&&this.getRiesgoApgarMinuto1_cn().equals(""))
				errors.add("Riesgo Minuto 1 requerido",new ActionMessage("errors.required","El riesgo en el campo Minuto Uno"));
			
			if(!this.getApgarMinuto5().equals("")&&this.getRiesgoApgarMinuto5_cn().equals(""))
				errors.add("Riesgo Minuto 5 requerido",new ActionMessage("errors.required","El riesgo en el campo Minuto Cinco"));
			
			if(!this.getApgarMinuto10().equals("")&&this.getRiesgoApgarMinuto10_cn().equals(""))
				errors.add("Riesgo Minuto 10 requerido",new ActionMessage("errors.required","El riesgo en el campo Minuto Diez"));
			//**********************************************************************************
			
		}

		/* Por convención, si no se encuentran errores, retornamos null */
		if(errors.isEmpty())
		{
			request.setAttribute("wasError", "false");
			errors=null;
		}
		else
			request.setAttribute("wasError", "true");

		return errors;
	}

	public HashMap getMapaOtros() 
	{
		return mapaOtros;
	}

	public void setMapaOtros(HashMap mapaOtros) 
	{
		this.mapaOtros = mapaOtros;
	}
	public Object getMapaOtros(String key) 
	{
		return mapaOtros.get(key);
	}

	@SuppressWarnings("unchecked")
	public void setMapaOtros(String key, String valor)
	{
		this.mapaOtros.put(key, valor);
	}	
	
	
	/**
	 * @return Retorna mapa para almacenar las patologias registradas para mostrarlas en el resumen.  
	 */
	public HashMap getMapaPato()
	{
		return this.mapaPato;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaPato(HashMap mapa) {
		this.mapaPato = mapa;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public String getMapaPato(String key)
	{
		return mapaPato.get(key)+"";
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	@SuppressWarnings("unchecked")
	public void setMapaPato(String key, Object obj) 
	{
		this.mapaPato.put(key, obj);
	}

	/**
	 * @return the descipcionOtrosMetodos
	 */
	public String getDescipcionOtrosMetodos() {
		return descipcionOtrosMetodos;
	}

	/**
	 * @param descipcionOtrosMetodos the descipcionOtrosMetodos to set
	 */
	public void setDescipcionOtrosMetodos(String descipcionOtrosMetodos) {
		this.descipcionOtrosMetodos = descipcionOtrosMetodos;
	}

}