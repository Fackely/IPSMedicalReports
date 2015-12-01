/*
 * Created on Feb 9, 2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.actionform.triage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author sebastián gómez rivillas
 *
 * Formulario que maneja ingreso, aconsulta, modificación y reporte de Triage
 */
public class TriageForm extends ValidatorForm {
	

	Logger logger = Logger.getLogger(TriageForm.class);

	/**	 * Sin errores en el validate	 */
	private boolean errores;
	
	
	//*********ATRIBUTOS GENERALES*******************************
	/**
	 * Institucion
	 */
	private String institucion;
	/**
	 * código de persona de paciente existente en triage
	 */
	private int codigoPersona;
	/**
	 * Indicador que verifica si el paciente estaba registrado para Triage
	 */
	private boolean tieneTriage;
	/**
	 * Almacena el consecutivo del registro pacientes_triage
	 */
	private String codigoPacienteTriage;
	//************************************************************
	/**
	 * Indica si el paciente no respondió el llamado (boolean)
	 */
	private String noRespondioLlamado;
	/**
	 * <code>consecutivo</code> del triage (parte entera)
	 */
	private String consecutivo;
	/**
	 * <code>consecutivo_fecha</code> del triage (parte de la fecha)
	 */
	private String consecutivo_fecha;
	/**
	 * <code>fecha</code> del sistema
	 */
	private String fecha;
	/**
	 * <code>hora</code> del sistema
	 */
	private String hora;
	/**
	 * <code>primer_nombre</code> del paciente
	 */
	private String primer_nombre;
	/**
	 * <code>segundo_nombre</code> del paciente
	 */
	private String segundo_nombre;
	/**
	 * <code>primer_apellido</code> del paciente
	 */
	private String primer_apellido;
	/**
	 * <code>segundo_apellido</code> del paciente
	 */
	private String segundo_apellido;
	
	/**
	 * <code>fecha_nacimiento</code> del paciente
	 */
	private String fecha_nacimiento;
	/**
	 * <code>tipo_id</code>, acronimo y nombre del tipo de identificacion (separados por "-")
	 */
	private String tipo_id;
	/**
	 * <code>numero_id</code>, numero de identificacion
	 */
	private String numero_id;
	/**
	 * <code>convenio</code>, codigo de convenio y descripcion (separados por "-")
	 */
	private String convenio;
	/**
	 *  <code>otro_conveio</code> otro convenio (opcional)
	 */
	private String otro_convenio;
	/**
	 * <code>accidenteTrabajo</code>
	 */
	private String accidenteTrabajo;
	/**
	 * <code>arpAfiliado</code>
	 */
	private String arpAfiliado;
	/**
	 * <code>tipo_afiliado</code> acrónimo y descripción de afiliado (separados por "-")
	 */
	private String tipo_afiliado;
	/**
	 * <code>id_cotizante</code>, identificación cotizante en caso de que sea beneficiario
	 */
	private String id_cotizante;
	/**
	 * <code>motivo_consulta</code>
	 */
	private String motivo_consulta;
	
	/**
	 * Signo sintoma
	 */
	private String signoSintoma;
	/**
	 * <code>categorias_triage</code> , codigo y descripción de la categoría dle triage
	 */
	private String categorias_triage;
	/**
	 *  <code>antecedentes</code>
	 */
	private String antecedentes;
	/**
	 * <code>destino</code>, codigo y descirpción del destino del paciente
	 */
	private String destino;
	/**
	 * Sala de Espera
	 */
	private String sala;
	/**
	 *<code>observaciones_generales</code>
	 */
	private String observaciones_generales;
	
	/**
	 * <code>nuevas_observaciones_generales</code> usada para la opción de modificar
	 */
	private String nuevas_observaciones_generales;
	
	/**
	 * <code>login</code> del usuario
	 */
	private String login;
	/**
	 *  <code>usuario</code>, profesional de la salud, Registro / especialidades
	 */
	private String usuario;
	
	/** SIGNOS VITALES DEL TRIAGE*/
	/**
	 * <code>pulso</code>
	 */
	/**
	 * Mapa donde se almacenan los signos vitales
	 */
	private HashMap signosVitales = new HashMap();
	/**
	 * Número de signos vitales
	 */
	private int numSignos;
	/** FIN SIGNOS VITALES TRIAGE*/
	/**
	 * Colección de <code>triage</code>
	 */
	private Collection triage;
	
	/**
	 * <code>estado</code> del action
	 */
	private String estado;
	
	/**
	 * <code>edad</code> calculada a partir de la fehca de nacimiento
	 */
	private String edad;
	
	/**
	 * Arreglo con los convenios tipo ARP
	 */
	private HashMap conveniosArp = new HashMap();
	/*
	 * Variable susadas para la ordenación
	 */
	private String columna;
	private String ultimaPropiedad;
	
	/**
	 * 
	 */
	private int codigoClasificacionTriage;
	
	/**
	 * 
	 */
	private String descripcionClasificacionTriage;
	

	
	/*
	 * Variables usadas para el reporte
	 */
	private String fechaInicial;
	private String fechaFinal;
	private int codigoClasificacion;
	private int codigoDestino;
	private String admision;
	private int codigoSala;
	private String colornombre;
	private String signosintoma;
	private String nombresala;
	private boolean existeAdmision;
	private int centroAtencion;
	private String nombreCentroAtencion;
	
	
	//********************RESET Y VALIDATE*******************************************
	
	
	/**
	 * Objeto implementado para mostrar las personas
	 * que tienen el mismo numero id pero diferente tipo identificacion
	 * cuando se ingresa un paciente
	 */
	private HashMap mapaNumerosId = new HashMap();
	
	
	/**
	 * Limpia el Formulario
	 */
	public void reset(){
		this.codigoClasificacionTriage=ConstantesBD.codigoNuncaValido;
		this.descripcionClasificacionTriage="";
		//**atributos generales**********
		this.setInstitucion("0");
		this.setCodigoPersona(-1);
		this.setTieneTriage(false);
		this.setCodigoPacienteTriage("");
		this.setCodigoClasificacion(-1);
		this.setCodigoDestino(-1);
		this.setAdmision("");
		this.setCodigoSala(-1);
		this.setColornombre("");
		this.setSignosintoma("");
		this.setNombresala("");
		this.setExisteAdmision(false);
		//**********************************
		this.setNoRespondioLlamado("false");
		this.setConsecutivo("");
		this.setConsecutivo_fecha("");
		this.setFecha("");
		this.setHora("");
		this.setPrimer_nombre("");
		this.setSegundo_nombre("");
		this.setPrimer_apellido("");
		this.mapaNumerosId = new HashMap();
		this.setSegundo_apellido("");
		this.setFecha_nacimiento("");
		this.setTipo_id("0- ");
		this.setNumero_id("");
		this.setConvenio("0- ");
		this.setOtro_convenio("");
		this.setAccidenteTrabajo(ConstantesBD.acronimoNo);
		this.setArpAfiliado("");
		this.setTipo_afiliado("0- ");
		this.setId_cotizante("");
		this.setMotivo_consulta("");
		this.setSignoSintoma("");
		this.setCategorias_triage("");
		this.setAntecedentes("");
		this.setDestino("");
		this.setSala("");
		this.setObservaciones_generales("");
		this.setLogin("");
		this.setUsuario("");
		this.setSignosVitales(new HashMap());
		this.setNumSignos(0);
		this.setTriage(new ArrayList());
		this.setEdad("");
		this.setUltimaPropiedad("");
		this.setColumna("");
		this.setNuevas_observaciones_generales("");
		this.setFechaFinal("");
		this.setFechaInicial("");
		this.centroAtencion = 0;
		this.nombreCentroAtencion = "";
		
		this.conveniosArp = new HashMap();
		
	 	this.errores = false;
	}

	
	/**
	 * Función de validación: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		//boolean error = false;
		
		logger.info("2222 LLEGO AL VALIDATE CON EL ESTADO: " + " 3 :" + estado );
		if(this.estado.equals("guardar"))
		{
			if(UtilidadTexto.isEmpty(this.getPrimer_nombre())){
				errores.add("Primer Nombre", new ActionMessage("errors.required", "Primer Nombre"));
			}
			if(UtilidadTexto.isEmpty(this.getPrimer_apellido())){
				errores.add("Primer Apellido", new ActionMessage("errors.required", "Primer Apellido"));
			}
			if(UtilidadTexto.isEmpty(this.getFecha_nacimiento())){
				errores.add("Fecha de Nacimiento", new ActionMessage("errors.required", "Fecha de Nacimiento"));
			}
			if(this.getConvenio().equals("0-Otra")){
				if(UtilidadTexto.isEmpty(this.getOtro_convenio())){
					errores.add("El convenio independiente", new ActionMessage("errors.required", "El convenio independiente"));
				}
			}
			if(this.getNoRespondioLlamado().equals("false")){
				
				if(UtilidadTexto.isEmpty(this.getMotivo_consulta())){
					errores.add("Sistemas Motivo Consulta Urgencia", new ActionMessage("errors.required", "Sistemas Motivo Consulta Urgencia"));
				}
				if(UtilidadTexto.isEmpty(this.getSignoSintoma())){
					errores.add("Signos y Síntomas por Sistema", new ActionMessage("errors.required", "Signos y Síntomas por Sistema"));
				}
				if(UtilidadTexto.isEmpty(this.getCategorias_triage())){
					errores.add("Calificación", new ActionMessage("errors.required", "Calificación"));
				}
				
				if(UtilidadTexto.isEmpty(this.getDestino())){
					errores.add("Destino", new ActionMessage("errors.required", "Destino"));
				}
				else{
					String [] destinos=this.getDestino().split(ConstantesBD.separadorSplit);
					if(destinos[3].equals("t") || destinos[3].equals("true") || destinos[3].equals("1")){
						if(UtilidadTexto.isEmpty(this.getSala())){
							errores.add("sala de espera", new ActionMessage("errors.required", "Sala de espera"));
						}
					}
				}
				
				
				
			}
		}
		
		if(this.estado.equals("buscar"))
		{
			if(!this.getFechaInicial().trim().equals("") && !this.getFechaFinal().trim().equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaInicial().trim()))
				{
					errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido", this.getFechaInicial()+""));
					this.errores = true;

				}
				if(!UtilidadFecha.validarFecha(this.getFechaFinal().trim()))
				{
					this.errores =  true;
					errores.add("errors.formatoFechaInvalido", new ActionMessage("errors.formatoFechaInvalido", this.getFechaFinal()+""));
				}
				if(!this.errores)
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.getFechaFinal()), UtilidadFecha.conversionFormatoFechaAAp(this.getFechaInicial())))
					{
						errores.add("Fecha Final menor a Fecha Incial", new ActionMessage("errors.fechaAnteriorIgualActual","Final", " Inicial"));
						//this.errores = true;
					}
				}
			}
			if(this.getFechaInicial().trim().equals(""))
			{
				errores.add("La Fecha Inicial", new ActionMessage("errors.required", "La Fecha Inicial"));
				//this.errores = true;
			}
			if(this.getFechaFinal().trim().equals(""))
			{
				errores.add("La Fecha Final", new ActionMessage("errors.required", "La Fecha Final"));
			}
			if(!this.errores)
			{
				if((!this.getFechaInicial().trim().equals("")&&!this.getFechaFinal().trim().equals("")))
				{
					int nroMeses = UtilidadFecha.numeroMesesEntreFechas(this.getFechaInicial(), this.getFechaFinal(),true);
					//nroMeses = nroMeses  + 1;
					if (nroMeses > 3)
					{
						errores.add("rango consulta mayor", new ActionMessage("error.triage.consultaTriage.rangoMayorTresMeses"));
					}
				}
			}
		}
		
		
		
		//*********************************************************************************
		if(this.estado.equals("imprimirreporte")) {
			logger.info("ENTRO AL VALIDATE DE IMPRIMIR REPORTE");
			
			if ( (this.getFechaInicial().trim().equals("")) || (this.getFechaInicial().trim().equals(null)) )	{
				errores.add("La Fecha Inicial", new ActionMessage("errors.required", "La Fecha Inicial"));
				this.errores = true; //false
			}
				
			if ( (this.getFechaFinal().trim().equals("")) || (this.getFechaFinal().trim().equals(null)) )	{
				errores.add("La Fecha Final", new ActionMessage("errors.required", "La Fecha Final"));
				this.errores = true; //false
			}

			if(!UtilidadTexto.isEmpty(this.fechaInicial) && !UtilidadTexto.isEmpty(this.fechaFinal)) {
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial)) {
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
					this.errores = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal)) {
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
					this.errores = true;
				}

				if(!centinelaErrorFechas) {
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal)) {
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial " + this.fechaInicial, "Final " + this.fechaFinal));
						this.errores = true;
					}

					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual().toString())) {
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final " + this.fechaFinal, "Actual " + UtilidadFecha.getFechaActual()));
						this.errores = true;
					}

					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual().toString())) {
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial " + this.fechaInicial, "Actual " + UtilidadFecha.getFechaActual()));
						this.errores = true;
					}

					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal()) >= 3) {
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Reportes Triage", "3", "90"));
						this.errores = true;
					}
				}
			}
			
			
			
		}
		
		logger.info("------------------------");
		logger.info("" + errores);

		if(errores.isEmpty()) {
			this.errores = false;
		}
		
		
		return errores;
	}
	//********************SETTERS Y GETTERS******************************************+
	
	
	
	/**
	 * @return Returns the antecedentes.
	 */
	public String getAntecedentes() {
		return antecedentes;
	}
	/**
	 * @param antecedentes The antecedentes to set.
	 */
	public void setAntecedentes(String antecedentes) {
		this.antecedentes = antecedentes;
	}
	/**
	 * @return Returns the categorias_triage.
	 */
	public String getCategorias_triage() {
		return categorias_triage;
	}
	/**
	 * @param categorias_triage The categorias_triage to set.
	 */
	public void setCategorias_triage(String categorias_triage) {
		this.categorias_triage = categorias_triage;
	}
	/**
	 * @return Returns the consecutivo.
	 */
	public String getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return Returns the consecutivo_fecha.
	 */
	public String getConsecutivo_fecha() {
		return consecutivo_fecha;
	}
	/**
	 * @param consecutivo_fecha The consecutivo_fecha to set.
	 */
	public void setConsecutivo_fecha(String consecutivo_fecha) {
		this.consecutivo_fecha = consecutivo_fecha;
	}
	/**
	 * @return Returns the convenio.
	 */
	public String getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the destino.
	 */
	public String getDestino() {
		return destino;
	}
	/**
	 * @param destino The destino to set.
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the fecha_nacimiento.
	 */
	public String getFecha_nacimiento() {
		return fecha_nacimiento;
	}
	/**
	 * @param fecha_nacimiento The fecha_nacimiento to set.
	 */
	public void setFecha_nacimiento(String fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}
	/**
	 * @return Returns the hora.
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @param hora The hora to set.
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	/**
	 * @return Returns the id_cotizante.
	 */
	public String getId_cotizante() {
		return id_cotizante;
	}
	/**
	 * @param id_cotizante The id_cotizante to set.
	 */
	public void setId_cotizante(String id_cotizante) {
		this.id_cotizante = id_cotizante;
	}
	/**
	 * @return Returns the login.
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return Returns the motivo_consulta.
	 */
	public String getMotivo_consulta() {
		return motivo_consulta;
	}
	/**
	 * @param motivo_consulta The motivo_consulta to set.
	 */
	public void setMotivo_consulta(String motivo_consulta) {
		this.motivo_consulta = motivo_consulta;
	}
	/**
	 * @return Returns the numero_id.
	 */
	public String getNumero_id() {
		return numero_id;
	}
	/**
	 * @param numero_id The numero_id to set.
	 */
	public void setNumero_id(String numero_id) {
		this.numero_id = numero_id;
	}
	
	/**
	 * @return Returns the observaciones_generales.
	 */
	public String getObservaciones_generales() {
		return observaciones_generales;
	}
	/**
	 * @param observaciones_generales The observaciones_generales to set.
	 */
	public void setObservaciones_generales(String observaciones_generales) {
		this.observaciones_generales = observaciones_generales;
	}
	/**
	 * @return Returns the otro_convenio.
	 */
	public String getOtro_convenio() {
		return otro_convenio;
	}
	/**
	 * @param otro_convenio The otro_convenio to set.
	 */
	public void setOtro_convenio(String otro_convenio) {
		this.otro_convenio = otro_convenio;
	}
	/**
	 * @return Returns the primer_apellido.
	 */
	public String getPrimer_apellido() {
		return primer_apellido;
	}
	/**
	 * @param primer_apellido The primer_apellido to set.
	 */
	public void setPrimer_apellido(String primer_apellido) {
		this.primer_apellido = primer_apellido;
	}
	/**
	 * @return Returns the primer_nombre.
	 */
	public String getPrimer_nombre() {
		return primer_nombre;
	}
	/**
	 * @param primer_nombre The primer_nombre to set.
	 */
	public void setPrimer_nombre(String primer_nombre) {
		this.primer_nombre = primer_nombre;
	}
	/**
	 * @return Returns the segundo_apellido.
	 */
	public String getSegundo_apellido() {
		return segundo_apellido;
	}
	/**
	 * @param segundo_apellido The segundo_apellido to set.
	 */
	public void setSegundo_apellido(String segundo_apellido) {
		this.segundo_apellido = segundo_apellido;
	}
	/**
	 * @return Returns the segundo_nombre.
	 */
	public String getSegundo_nombre() {
		return segundo_nombre;
	}
	/**
	 * @param segundo_nombre The segundo_nombre to set.
	 */
	public void setSegundo_nombre(String segundo_nombre) {
		this.segundo_nombre = segundo_nombre;
	}
	
	/**
	 * @return Returns the tipo_afiliado.
	 */
	public String getTipo_afiliado() {
		return tipo_afiliado;
	}
	/**
	 * @param tipo_afiliado The tipo_afiliado to set.
	 */
	public void setTipo_afiliado(String tipo_afiliado) {
		this.tipo_afiliado = tipo_afiliado;
	}
	
	public String getacronimoTipoId() {
		return tipo_id.split(ConstantesBD.separadorSplit)[0];
	}
	
	
	/**
	 * @return Returns the tipo_id.
	 */
	public String getTipo_id() {
		return tipo_id;
	}
	/**
	 * @param tipo_id The tipo_id to set.
	 */
	public void setTipo_id(String tipo_id) {
		this.tipo_id = tipo_id;
	}
	/**
	 * @return Returns the triage.
	 */
	public Collection getTriage() {
		return triage;
	}
	/**
	 * @param triage The triage to set.
	 */
	public void setTriage(Collection triage) {
		this.triage = triage;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @return Returns the edad.
	 */
	public String getEdad() {
		return edad;
	}
	/**
	 * @param edad The edad to set.
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}
	
	
	/**
	 * @return Returns the numSignos.
	 */
	public int getNumSignos() {
		return numSignos;
	}
	/**
	 * @param numSignos The numSignos to set.
	 */
	public void setNumSignos(int numSignos) {
		this.numSignos = numSignos;
	}
	/**
	 * @return Returns the signosVitales.
	 */
	public HashMap getSignosVitales() {
		return signosVitales;
	}
	/**
	 * @param signosVitales The signosVitales to set.
	 */
	public void setSignosVitales(HashMap signosVitales) {
		this.signosVitales = signosVitales;
	}
	
	/**
	 * @return Retorna un elemento del mapa signosVitales.
	 */
	public Object getSignosVitales(String key) {
		return signosVitales.get(key);
	}
	/**
	 * @param asigna un elemento al mapa signosVitales.
	 */
	public void setSignosVitales(String key,Object obj) {
		this.signosVitales.put(key,obj);
	}
	/**
	 * @return Returns the columna.
	 */
	public String getColumna() {
		return columna;
	}
	/**
	 * @param columna The columna to set.
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}
	/**
	 * @return Returns the ultimapropiedad.
	 */
	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}
	/**
	 * @param ultimapropiedad The ultimapropiedad to set.
	 */
	public void setUltimaPropiedad(String ultimapropiedad) {
		this.ultimaPropiedad = ultimapropiedad;
	}
	/**
	 * @return Returns the nuevas_observaciones_generales.
	 */
	public String getNuevas_observaciones_generales() {
		return nuevas_observaciones_generales;
	}
	/**
	 * @param nuevas_observaciones_generales The nuevas_observaciones_generales to set.
	 */
	public void setNuevas_observaciones_generales(
			String nuevas_observaciones_generales) {
		this.nuevas_observaciones_generales = nuevas_observaciones_generales;
	}
	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the codigoPersona.
	 */
	public int getCodigoPersona() {
		return codigoPersona;
	}
	/**
	 * @param codigoPersona The codigoPersona to set.
	 */
	public void setCodigoPersona(int codigoPersona) {
		this.codigoPersona = codigoPersona;
	}
	/**
	 * @return Returns the tieneTriage.
	 */
	public boolean isTieneTriage() {
		return tieneTriage;
	}
	/**
	 * @param tieneTriage The tieneTriage to set.
	 */
	public void setTieneTriage(boolean tieneTriage) {
		this.tieneTriage = tieneTriage;
	}
	/**
	 * @return Returns the codigoPacienteTriage.
	 */
	public String getCodigoPacienteTriage() {
		return codigoPacienteTriage;
	}
	/**
	 * @param codigoPacienteTriage The codigoPacienteTriage to set.
	 */
	public void setCodigoPacienteTriage(String codigoPacienteTriage) {
		this.codigoPacienteTriage = codigoPacienteTriage;
	}
	/**
	 * @return Returns the noRespondioLlamado.
	 */
	public String getNoRespondioLlamado() {
		return noRespondioLlamado;
	}
	/**
	 * @param noRespondioLlamado The noRespondioLlamado to set.
	 */
	public void setNoRespondioLlamado(String noRespondioLlamado) {
		this.noRespondioLlamado = noRespondioLlamado;
	}
	/**
	 * @return Returns the signoSintoma.
	 */
	public String getSignoSintoma() {
		return signoSintoma;
	}
	/**
	 * @param signoSintoma The signoSintoma to set.
	 */
	public void setSignoSintoma(String signoSintoma) {
		this.signoSintoma = signoSintoma;
	}
	
	/**
	 * @return Returns the admision.
	 */
	public String getAdmision()
	{
		return admision;
	}
	/**
	 * @param admision The admision to set.
	 */
	public void setAdmision(String admision)
	{
		this.admision=admision;
	}
	/**
	 * @return Returns the codigoClasificacion.
	 */
	public int getCodigoClasificacion()
	{
		return codigoClasificacion;
	}
	/**
	 * @param codigoClasificacion The codigoClasificacion to set.
	 */
	public void setCodigoClasificacion(int codigoClasificacion)
	{
		this.codigoClasificacion=codigoClasificacion;
	}
	/**
	 * @return Returns the codigoDestino.
	 */
	public int getCodigoDestino()
	{
		return codigoDestino;
	}
	/**
	 * @param codigoDestino The codigoDestino to set.
	 */
	public void setCodigoDestino(int codigoDestino)
	{
		this.codigoDestino=codigoDestino;
	}
	/**
	 * @return Returns the codigoSala.
	 */
	public int getCodigoSala()
	{
		return codigoSala;
	}
	/**
	 * @param codigoSala The codigoSala to set.
	 */
	public void setCodigoSala(int codigoSala)
	{
		this.codigoSala=codigoSala;
	}
	/**
	 * @return Returns the colornombre.
	 */
	public String getColornombre()
	{
		return colornombre;
	}
	/**
	 * @param colornombre The colornombre to set.
	 */
	public void setColornombre(String colornombre)
	{
		this.colornombre=colornombre;
	}
	/**
	 * @return Returns the signosintoma.
	 */
	public String getSignosintoma()
	{
		return signosintoma;
	}
	/**
	 * @param signosintoma The signosintoma to set.
	 */
	public void setSignosintoma(String signosintoma)
	{
		this.signosintoma=signosintoma;
	}
	/**
	 * @return Returns the nombresala.
	 */
	public String getNombresala()
	{
		return nombresala;
	}
	/**
	 * @param nombresala The nombresala to set.
	 */
	public void setNombresala(String nombresala)
	{
		this.nombresala=nombresala;
	}
	/**
	 * @return Returns the sala.
	 */
	public String getSala() {
		return sala;
	}
	/**
	 * @param sala The sala to set.
	 */
	public void setSala(String sala) {
		this.sala = sala;
	}
	/**
	 * @return Returns the existeAdmision.
	 */
	public boolean isExisteAdmision()
	{
		return existeAdmision;
	}
	/**
	 * @param existeAdmision The existeAdmision to set.
	 */
	public void setExisteAdmision(boolean existeAdmision)
	{
		this.existeAdmision=existeAdmision;
	}
	/**
	 * @return Returns the centroAtencion.
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return Returns the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}
	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}
	/**
	 * @return the accidenteTrabajo
	 */
	public String getAccidenteTrabajo() {
		return accidenteTrabajo;
	}
	/**
	 * @param accidenteTrabajo the accidenteTrabajo to set
	 */
	public void setAccidenteTrabajo(String accidenteTrabajo) {
		this.accidenteTrabajo = accidenteTrabajo;
	}
	/**
	 * @return the arpAfiliado
	 */
	public String getArpAfiliado() {
		return arpAfiliado;
	}
	/**
	 * @param arpAfiliado the arpAfiliado to set
	 */
	public void setArpAfiliado(String arpAfiliado) {
		this.arpAfiliado = arpAfiliado;
	}
	/**
	 * @return the conveniosArp
	 */
	public HashMap getConveniosArp() {
		return conveniosArp;
	}
	/**
	 * @param conveniosArp the conveniosArp to set
	 */
	public void setConveniosArp(HashMap conveniosArp) {
		this.conveniosArp = conveniosArp;
	}
	
	/**
	 * @return Retorna elemento del mapa conveniosArp
	 */
	public Object getConveniosArp(String key) {
		return conveniosArp.get(key);
	}
	/**
	 * @param Asigna elemento al mapa conveniosArp 
	 */
	public void setConveniosArp(String key,Object obj) {
		this.conveniosArp.put(key,obj);
	}


	public String getDescripcionClasificacionTriage() {
		return descripcionClasificacionTriage;
	}


	public void setDescripcionClasificacionTriage(
			String descripcionClasificacionTriage) {
		this.descripcionClasificacionTriage = descripcionClasificacionTriage;
	}


	public int getCodigoClasificacionTriage() {
		return codigoClasificacionTriage;
	}


	public void setCodigoClasificacionTriage(int codigoClasificacionTriage) {
		this.codigoClasificacionTriage = codigoClasificacionTriage;
	}



	/**
	 * @return the mapaNumerosId
	 */
	public HashMap getMapaNumerosId() {
		return mapaNumerosId;
	}

	/**
	 * @param mapaNumerosId the mapaNumerosId to set
	 */
	public void setMapaNumerosId(HashMap mapaNumerosId) {
		this.mapaNumerosId = mapaNumerosId;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaNumerosId
	 */
	public Object getMapaNumerosId(String key) {
		return mapaNumerosId.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaNumerosId 
	 */
	public void setMapaNumerosId(String key,Object obj) {
		this.mapaNumerosId.put(key,obj);
	}

	
	
}
