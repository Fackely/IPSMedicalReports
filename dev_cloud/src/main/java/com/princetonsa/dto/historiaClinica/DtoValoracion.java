/*
 * Abr 29, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoOdontograma;

import com.princetonsa.dto.historiaClinica.componentes.DtoAntecedentesOdontologicosAnt;
import com.princetonsa.dto.historiaClinica.componentes.DtoHistoriaMenstrual;
import com.princetonsa.dto.historiaClinica.componentes.DtoOftalmologia;
import com.princetonsa.dto.historiaClinica.componentes.DtoPediatria;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;

/**
 * Data Transfer Object: Valoración
 * @author Sebastián Gómez R.
 *
 */
public class DtoValoracion implements Serializable 
{
	/**
	 * Número de solicitu de la vlaoracion 
	 */
	private String numeroSolicitud;
	private InfoDatosInt estadoHistoriaClinica; 
	private String edad;
	private Boolean fueAccidenteTrabajo;
	private String fechaGrabacion;
	private String horaGrabacion;
	private String fechaValoracion;
	private String horaValoracion;
	private InfoDatosInt causaExterna;
	private UsuarioBasico profesional;
	private String controlPostOperatorio;
	private boolean cuidadoEspecial;
	private String especialidadProfResponde;
	
	/**
	 * MT 5568
	 * @author javrammo
	 * visualizar el area el paciente en las valoraciones
	 */
	private InfoDatosInt datoAreaPaciente;
	/**
	 * Fin MT 5568
	 */

	
	/**
	 * DTO interno que contiene la información de consulta
	 */
	private DtoValoracionConsulta valoracionConsulta;
	
	/**
	 * Arreglo que contiene los signos vitales de la valoracion
	 */
	private ArrayList<SignoVital> signosVitales;
	
	/**
	 * Arreglo que contiene las revisiones sistemas generales
	 */
	private ArrayList<DtoRevisionSistema> revisionesSistemas;
	
	/**
	 * Arreglo que contiene los diagnosticos de la valoracion
	 */
	private ArrayList<Diagnostico> diagnosticos;
	
	/**
	 * Arreglo que contiene todos los tipos de valoraciones de la valoración incluyendo históricos
	 */
	private ArrayList<DtoValoracionObservaciones> observaciones;
	
	/**
	 * Usado para la evaluacion del campo accidente de trabajo
	 */
	private InfoDatosString tipoEvento;
	
	/**
	 * Variable que indica si se puede abrir la sección de pyp
	 */
	private boolean abrirPYP;
	
	//*********************COMPONENTES DE LA VALORACION************************************
	//Componente de Ginecología
	private DtoHistoriaMenstrual historiaMenstrual;
	//Componente de Oftalmologia
	private DtoOftalmologia oftalmologia;
	//Componentes de Pediatria
	private DtoPediatria pediatria;
	//Componente Odontograma Diagnostico
	private InfoOdontograma infoCompOdont;
	// Componente Antecedentes Odontológicos
	private DtoAntecedentesOdontologicosAnt antecedentesOdonto;
	// Componente Antecedente Odontologico
	private InfoAntecedenteOdonto antecedenteOdontologico;
	// Componente Indice de Placa
	private DtoComponenteIndicePlaca compIndicePlaca;
	//***************************************************************************************
	
	
	/**
	 * variableas para manejar el envio a epicrisis.
	 */
	private String enviarEpicrisisInformacionGeneral;
	private String enviarEpicrisisMotivoConsulta;
	private String enviarEpicrisisEnfermedadActual;
	private String enviarEpicrisisRevisionPorSistema;
	private String enviarEpicrisisExamenFisicoUrgencias;
	private String enviarEpicrisisCausaExterna;
	private String enviarEpicrisisFinalidadConsulta;
	private String enviarEpicrisisConductaValoracion;
	private String enviarEpicrisisDiagnosticos;
	private String enviarEpicrisisObservaciones;	
	private String enviarEpicrisisConceptoConsulta;
	private String enviarEpicrisisIncapacidadFuncioal;
	private String enviarEpicrisisProximoControl;
	
	
	/**
	 *Atributo que contiene el valor de estado de embarazo  
	 */
	private String  estadoEmbarazada;
	
	
	
	/**
	 *atributo que contiene el registro medico del profesional que modifica el embarazo 
	 */
	private String registroMedico;
	
	
	/**
	 * atributo que contiene el usuario del profesional de la salud 
	 */
	private String usuarioProfesionalSalud;
	
	
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * atributos para vistaValoracion urgencias
	 */
	/**
	 * se crea para las observaciones etiqueta Comentarios Generales
	 */
	public  final String COMENTARIOS_GENERALES = "Comentarios Generales";
	/**
	 * se crea para las observaciones etiqueta Plan Diagnostico Terapeutico
	 */
	public  final String PLAN_DIAGNOSTICO_TERAPEUTICO = "Plan Diagnostico Terapeutico";
	/**
	 * se crea para las observaciones etiqueta Explique Dos Deberes y Derechos
	 */
	public  final String EXPLIQUE_DOS_DEBERES_DERECHOS = "Explique Dos Deberes y Derechos";
	
	
	/**
	 * atributo que contiene la fechaValoracionUrgencias
	 */
	private String fechaValoracionUrgencias;
	/**
	 * atributo que contiene la horaValoracionUrgencias
	 */
	private String horaValoracionUrgencias;
	/**
	 * atributo que contiene el nombre del profesional
	 */
	private String profesion;
	/**
	 * atributo que contiene las especialidades
	 */
	private String especialidades;
	/**
	 * atributo que contiene el registro medico
	 */
	private String registromedico;
	/**
	 * atributo que contiene la conducta a segir
	 */
	private String observacion;
	/**
	 * atributo que contiene la etiqueta de la observacion
	 */
	private String label;
	/**
	 * atributo que contiene la fecha y la hora  de la valoracion
	 */
	private Date fechaHoraValoracionUrgencias;
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * atributos para vistaValoracion observacion
	 */
	/**
	 * atributo que contiene el tipo de observacio
	 */
	private String tipo;
	/**
	 * atributo que contiene el valor de la observacion
	 */
	private String valor;
	/**
	 * atributo que contiene fechaValoracionObservacion
	 */
	private String fechaValoracionObservacion;
	/**
	 * atributo que contiene usuario
	 */
	
	private String usuario;
	/**
	 * atributo que contiene horaValoracionObservacion
	 */
	private String horaValoracionObservacion;
	/**
	 * atributo que contiene fechaHoraObservacion
	 */
	private Date fechaHoraObservacion;
	/**
	 * atributo que contiene fechaHora
	 */
	private Date fechaHora;
	/**
	 * atributo que contiene formatoFechaHora
	 */
	private String formatoFechaHora;
	/**
	 * atributo que contiene desc_conducta_valoracion
	 */
	private String desc_conducta_valoracion;
	/**
	 * atributo que contiene vistaobservaciones
	 */
	private List <DtoValoracion> vistaobservaciones;
	/**
	 * atributo que contiene imprimeFechaHora
	 */
	private Boolean imprimeFechaHora;
	
	/**
	 * atributo que contiene el consecutivo
	 */
	private String consecutivo;
	
	/**
	 * atributo que contiene la etiqueta de observaciones
	 * 
	 */
    private String etiquetaObservaciones;
   
    /**
	 * atributo que contiene valorObservaciones
	 * 
	 */
	
	private String valorObservaciones;
	

	/**
	 * Método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.numeroSolicitud = "";
		this.estadoHistoriaClinica = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.edad = "";
		this.fueAccidenteTrabajo = null;
		this.fechaGrabacion = "";
		this.horaGrabacion = "";
		this.fechaValoracion = "";
		this.horaValoracion = "";
		this.causaExterna = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.profesional = new UsuarioBasico();
		this.signosVitales = null;
		this.revisionesSistemas = null;
		this.diagnosticos = null;
		this.valoracionConsulta = null;
		this.observaciones = null;
		this.controlPostOperatorio = "";
		this.especialidadProfResponde="";
		
		this.tipoEvento = new InfoDatosString("","");
		this.abrirPYP = false;
		
		
		//********COMPONENTES DE LA VALORACION************************************
		this.historiaMenstrual = null;
		this.oftalmologia = null;
		this.pediatria = null;
		this.infoCompOdont = null;
		this.antecedentesOdonto= null;
		this.antecedenteOdontologico = null;
		this.compIndicePlaca = null;
		
		this.cuidadoEspecial = false;
		

		this.enviarEpicrisisInformacionGeneral=ConstantesBD.acronimoNo;
		this.enviarEpicrisisMotivoConsulta=ConstantesBD.acronimoNo;
		this.enviarEpicrisisEnfermedadActual=ConstantesBD.acronimoNo;
		this.enviarEpicrisisRevisionPorSistema=ConstantesBD.acronimoNo;
		this.enviarEpicrisisExamenFisicoUrgencias=ConstantesBD.acronimoNo;
		this.enviarEpicrisisCausaExterna=ConstantesBD.acronimoNo;
		this.enviarEpicrisisFinalidadConsulta=ConstantesBD.acronimoNo;
		this.enviarEpicrisisConductaValoracion=ConstantesBD.acronimoNo;
		this.enviarEpicrisisDiagnosticos=ConstantesBD.acronimoNo;
		this.enviarEpicrisisObservaciones=ConstantesBD.acronimoNo;
		this.enviarEpicrisisConceptoConsulta=ConstantesBD.acronimoNo;
		this.enviarEpicrisisIncapacidadFuncioal=ConstantesBD.acronimoNo;
		this.enviarEpicrisisProximoControl=ConstantesBD.acronimoNo;
		this.datoAreaPaciente = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
	}
	
	/**
	 * Consttructor de la valoracion
	 *
	 */
	public DtoValoracion()
	{
		this.clean();
	}

	/**
	 * @return the causaExterna
	 */
	public int getCodigoCausaExterna() {
		return causaExterna.getCodigo();
	}


	/**
	 * @param causaExterna the causaExterna to set
	 */
	public void setCodigoCausaExterna(int causaExterna) {
		this.causaExterna.setCodigo(causaExterna);
	}
	
	/**
	 * @return the causaExterna
	 */
	public String getNombreCausaExterna() {
		return causaExterna.getNombre();
	}


	/**
	 * @param causaExterna the causaExterna to set
	 */
	public void setNombreCausaExterna(String causaExterna) {
		this.causaExterna.setNombre(causaExterna);
	}
	
	/**
	 * Se usa para mensaje de alerta de la causa externa
	 * @return the causaExterna
	 */
	public String getDescripcionCausaExterna() {
		return causaExterna.getDescripcion();
	}


	/**
	 * @param causaExterna the causaExterna to set
	 */
	public void setDescripcionCausaExterna(String causaExterna) {
		this.causaExterna.setDescripcion(causaExterna);
	}


	/**
	 * @return the edad
	 */
	public String getEdad() {
		return edad;
	}


	/**
	 * @param edad the edad to set
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}




	/**
	 * @return the fechaGrabacion
	 */
	public String getFechaGrabacion() {
		return fechaGrabacion;
	}


	/**
	 * @param fechaGrabacion the fechaGrabacion to set
	 */
	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}


	/**
	 * @return the fechaValoracion
	 */
	public String getFechaValoracion() {
		return fechaValoracion;
	}


	/**
	 * @param fechaValoracion the fechaValoracion to set
	 */
	public void setFechaValoracion(String fechaValoracion) {
		this.fechaValoracion = fechaValoracion;
	}


	/**
	 * @return the horaGrabacion
	 */
	public String getHoraGrabacion() {
		return horaGrabacion;
	}


	/**
	 * @param horaGrabacion the horaGrabacion to set
	 */
	public void setHoraGrabacion(String horaGrabacion) {
		this.horaGrabacion = horaGrabacion;
	}


	/**
	 * @return the horaValoracion
	 */
	public String getHoraValoracion() {
		return horaValoracion;
	}


	/**
	 * @param horaValoracion the horaValoracion to set
	 */
	public void setHoraValoracion(String horaValoracion) {
		this.horaValoracion = horaValoracion;
	}


	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}


	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	/**
	 * @return the profesional
	 */
	public UsuarioBasico getProfesional() {
		return profesional;
	}


	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(UsuarioBasico profesional) {
		this.profesional = profesional;
	}

	/**
	 * @return the revisionesSistemas
	 */
	public ArrayList<DtoRevisionSistema> getRevisionesSistemas() {
		if(revisionesSistemas == null)
		{
			revisionesSistemas = new ArrayList<DtoRevisionSistema>();
		}
		return revisionesSistemas;
	}

	/**
	 * @param revisionesSistemas the revisionesSistemas to set
	 */
	public void setRevisionesSistemas(ArrayList<DtoRevisionSistema> revisionesSistemas) {
		this.revisionesSistemas = revisionesSistemas;
	}
	
	/**
	 * Método para saber cuantas revisiones x sistemas hay
	 * @return
	 */
	public int getNumRevisionesSistemas()
	{
		return this.getRevisionesSistemas().size();
	}

	/**
	 * @return the signosVitales
	 */
	public ArrayList<SignoVital> getSignosVitales() {
		if(signosVitales == null)
		{
			signosVitales = new ArrayList<SignoVital>();
		}
		return signosVitales;
	}

	/**
	 * @param signosVitales the signosVitales to set
	 */
	public void setSignosVitales(ArrayList<SignoVital> signosVitales) {
		this.signosVitales = signosVitales;
	}

	/**
	 * @return the valoracionConsulta
	 */
	public DtoValoracionConsulta getValoracionConsulta() {
		if(valoracionConsulta == null)
		{
			valoracionConsulta = new DtoValoracionConsulta();
		}
		return valoracionConsulta;
	}

	/**
	 * @param valoracionConsulta the valoracionConsulta to set
	 */
	public void setValoracionConsulta(DtoValoracionConsulta valoracionConsulta) {
		this.valoracionConsulta = valoracionConsulta;
	}

	/**
	 * @return the diagnosticos
	 */
	public ArrayList<Diagnostico> getDiagnosticos() {
		if(diagnosticos == null)
		{
			diagnosticos = new ArrayList<Diagnostico>();
		}
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	/**
	 * @return the observaciones
	 */
	public ArrayList<DtoValoracionObservaciones> getObservaciones() {
		if(observaciones == null)
		{
			observaciones = new ArrayList<DtoValoracionObservaciones>();
		}
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(ArrayList<DtoValoracionObservaciones> observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the estadoHistoriaClinica
	 */
	public int getCodigoEstadoHistoriaClinica() {
		return estadoHistoriaClinica.getCodigo();
	}

	/**
	 * @param estadoHistoriaClinica the estadoHistoriaClinica to set
	 */
	public void setCodigoEstadoHistoriaClinica(int estadoHistoriaClinica) {
		this.estadoHistoriaClinica.setCodigo(estadoHistoriaClinica);
	}
	
	/**
	 * @return the estadoHistoriaClinica
	 */
	public String getNombreEstadoHistoriaClinica() {
		return estadoHistoriaClinica.getNombre();
	}

	/**
	 * @param estadoHistoriaClinica the estadoHistoriaClinica to set
	 */
	public void setNombreEstadoHistoriaClinica(String estadoHistoriaClinica) {
		this.estadoHistoriaClinica.setNombre(estadoHistoriaClinica);
	}

	/**
	 * @return the tipoEvento
	 */
	public String getCodigoTipoEvento() {
		return tipoEvento.getCodigo();
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setCodigoTipoEvento(String tipoEvento) {
		this.tipoEvento.setCodigo(tipoEvento);
	}
	
	/**
	 * @return the tipoEvento
	 */
	public String getNombreTipoEvento() {
		return tipoEvento.getNombre();
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setNombreTipoEvento(String tipoEvento) {
		this.tipoEvento.setNombre(tipoEvento);
	}

	/**
	 * @return the fueAccidenteTrabajo
	 */
	public boolean getEventoFueAccidenteTrabajo() {
		if(fueAccidenteTrabajo==null||!fueAccidenteTrabajo){
			return false;
		}
		return fueAccidenteTrabajo;
	}

	/**
	 * @return the fueAccidenteTrabajo
	 */
	public Boolean getFueAccidenteTrabajo() {
		return fueAccidenteTrabajo;
	}

	/**
	 * @param fueAccidenteTrabajo the fueAccidenteTrabajo to set
	 */
	public void setFueAccidenteTrabajo(Boolean fueAccidenteTrabajo) {
		this.fueAccidenteTrabajo = fueAccidenteTrabajo;
	}

	/**
	 * @return the abrirPYP
	 */
	public boolean isAbrirPYP() {
		return abrirPYP;
	}

	/**
	 * @param abrirPYP the abrirPYP to set
	 */
	public void setAbrirPYP(boolean abrirPYP) {
		this.abrirPYP = abrirPYP;
	}

	/**
	 * @return the historiaMenstrual
	 */
	public DtoHistoriaMenstrual getHistoriaMenstrual() {
		if(this.historiaMenstrual == null)
		{
			this.historiaMenstrual = new DtoHistoriaMenstrual();
		}
		return historiaMenstrual;
	}

	/**
	 * @param historiaMenstrual the historiaMenstrual to set
	 */
	public void setHistoriaMenstrual(DtoHistoriaMenstrual historiaMenstrual) {
		this.historiaMenstrual = historiaMenstrual;
	}

	/**
	 * @return the oftalmologia
	 */
	public DtoOftalmologia getOftalmologia() {
		if(this.oftalmologia == null)
		{
			this.oftalmologia = new DtoOftalmologia();
		}
		return oftalmologia;
	}

	/**
	 * @param oftalmologia the oftalmologia to set
	 */
	public void setOftalmologia(DtoOftalmologia oftalmologia) {
		this.oftalmologia = oftalmologia;
	}

	/**
	 * @return the pediatria
	 */
	public DtoPediatria getPediatria() {
		if(this.pediatria == null)
		{
			this.pediatria = new DtoPediatria();
		}
		return pediatria;
	}

	/**
	 * @param pediatria the pediatria to set
	 */
	public void setPediatria(DtoPediatria pediatria) {
		this.pediatria = pediatria;
	}

	/**
	 * @return the controlPostOperatorio
	 */
	public String getControlPostOperatorio() {
		return controlPostOperatorio;
	}

	/**
	 * @param controlPostOperatorio the controlPostOperatorio to set
	 */
	public void setControlPostOperatorio(String controlPostOperatorio) {
		this.controlPostOperatorio = controlPostOperatorio;
	}

	/**
	 * @return the cuidadoEspecial
	 */
	public boolean isCuidadoEspecial() {
		return cuidadoEspecial;
	}

	/**
	 * @param cuidadoEspecial the cuidadoEspecial to set
	 */
	public void setCuidadoEspecial(boolean cuidadoEspecial) {
		this.cuidadoEspecial = cuidadoEspecial;
	}

	/**
	 * Método que verifica si existe información en la sección de observaciones
	 * @return
	 */
	public boolean isExisteInformacionSeccionObservaciones()
	{
		boolean existe = false;
		
		for(DtoValoracionObservaciones observaciones:this.getObservaciones())
			if(
					(
						observaciones.getTipo().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico)||
						observaciones.getTipo().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales)||
						observaciones.getTipo().equals(ConstantesIntegridadDominio.acronimoPronostico)
					)
					&&
					!observaciones.getValor().trim().equals("")
				)
				existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si exoste plan diagnóstico o terapéutico
	 * @return
	 */
	public boolean isExistePlanDiagnosticoTerapeutico()
	{
		boolean existe = false;
		
		for(DtoValoracionObservaciones observaciones:this.getObservaciones())
			if(observaciones.getTipo().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico))
				existe = true;
		
		return existe;
	}

	/**
	 * Método que verifica si exoste plan diagnóstico o terapéutico
	 * @return
	 */
	public boolean isExisteComentariosGenerales()
	{
		boolean existe = false;
		
		for(DtoValoracionObservaciones observaciones:this.getObservaciones())
			if(observaciones.getTipo().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales))
				existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si exoste plan diagnóstico o terapéutico
	 * @return
	 */
	public boolean isExistePronostico()
	{
		boolean existe = false;
		
		for(DtoValoracionObservaciones observaciones:this.getObservaciones())
			if(observaciones.getTipo().equals(ConstantesIntegridadDominio.acronimoPronostico))
				existe = true;
		
		return existe;
	}

	/**
	 * @return the especialidadProfResponde
	 */
	public String getEspecialidadProfResponde() {
		return especialidadProfResponde;
	}

	/**
	 * @param especialidadProfResponde the especialidadProfResponde to set
	 */
	public void setEspecialidadProfResponde(String especialidadProfResponde) {
		this.especialidadProfResponde = especialidadProfResponde;
	}

	/**
	 * @return the antecedentesOdonto
	 */
	public DtoAntecedentesOdontologicosAnt getAntecedentesOdonto() {
		if(this.antecedentesOdonto == null)
		{
			this.antecedentesOdonto = new DtoAntecedentesOdontologicosAnt();
		}
		return antecedentesOdonto;
	}

	/**
	 * @param antecedentesOdonto the antecedentesOdonto to set
	 */
	public void setAntecedentesOdonto(
			DtoAntecedentesOdontologicosAnt antecedentesOdonto) {
		this.antecedentesOdonto = antecedentesOdonto;
	}

	/**
	 * @return the antecedenteOdontologico
	 */
	public InfoAntecedenteOdonto getAntecedenteOdontologico() {
		if(this.antecedenteOdontologico == null)
		{
			this.antecedenteOdontologico = new InfoAntecedenteOdonto();
		}
		return antecedenteOdontologico;
	}

	/**
	 * @param antecedenteOdontologico the antecedenteOdontologico to set
	 */
	public void setAntecedenteOdontologico(
			InfoAntecedenteOdonto antecedenteOdontologico) {
		this.antecedenteOdontologico = antecedenteOdontologico;
	}

	public InfoOdontograma getInfoCompOdont() {
		if(this.infoCompOdont == null)
		{
			this.infoCompOdont = new InfoOdontograma();
		}
		return infoCompOdont;
	}

	public void setInfoCompOdont(InfoOdontograma infoCompOdont) {
		this.infoCompOdont = infoCompOdont;
	}

	/**
	 * @return the compIndicePlaca
	 */
	public DtoComponenteIndicePlaca getCompIndicePlaca() {
		if(this.compIndicePlaca == null)
		{
			this.compIndicePlaca = new DtoComponenteIndicePlaca();
		}
		return compIndicePlaca;
	}

	/**
	 * @param compIndicePlaca the compIndicePlaca to set
	 */
	public void setCompIndicePlaca(DtoComponenteIndicePlaca compIndicePlaca) {
		this.compIndicePlaca = compIndicePlaca;
	}

	/**
	 * @return the estadoEmbarazada
	 */
	public String getEstadoEmbarazada() {
		return estadoEmbarazada;
	}

	/**
	 * @param estadoEmbarazada the estadoEmbarazada to set
	 */
	public void setEstadoEmbarazada(String estadoEmbarazada) {
		this.estadoEmbarazada = estadoEmbarazada;
	}

	/**
	 * @return the registroMedico
	 */
	public String getRegistroMedico() {
		return registroMedico;
	}

	/**
	 * @param registroMedico the registroMedico to set
	 */
	public void setRegistroMedico(String registroMedico) {
		this.registroMedico = registroMedico;
	}

	/**
	 * @return the usuarioProfesionalSalud
	 */
	public String getUsuarioProfesionalSalud() {
		return usuarioProfesionalSalud;
	}

	/**
	 * @param usuarioProfesionalSalud the usuarioProfesionalSalud to set
	 */
	public void setUsuarioProfesionalSalud(String usuarioProfesionalSalud) {
		this.usuarioProfesionalSalud = usuarioProfesionalSalud;
	}
	
	public String getEnviarEpicrisisInformacionGeneral() {
		return enviarEpicrisisInformacionGeneral;
	}

	public void setEnviarEpicrisisInformacionGeneral(
			String enviarEpicrisisInformacionGeneral) {
		this.enviarEpicrisisInformacionGeneral = enviarEpicrisisInformacionGeneral;
	}

	public String getEnviarEpicrisisMotivoConsulta() {
		return enviarEpicrisisMotivoConsulta;
	}

	public void setEnviarEpicrisisMotivoConsulta(
			String enviarEpicrisisMotivoConsulta) {
		this.enviarEpicrisisMotivoConsulta = enviarEpicrisisMotivoConsulta;
	}

	public String getEnviarEpicrisisEnfermedadActual() {
		return enviarEpicrisisEnfermedadActual;
	}

	public void setEnviarEpicrisisEnfermedadActual(
			String enviarEpicrisisEnfermedadActual) {
		this.enviarEpicrisisEnfermedadActual = enviarEpicrisisEnfermedadActual;
	}

	public String getEnviarEpicrisisRevisionPorSistema() {
		return enviarEpicrisisRevisionPorSistema;
	}

	public void setEnviarEpicrisisRevisionPorSistema(
			String enviarEpicrisisRevisionPorSistema) {
		this.enviarEpicrisisRevisionPorSistema = enviarEpicrisisRevisionPorSistema;
	}

	public String getEnviarEpicrisisExamenFisicoUrgencias() {
		return enviarEpicrisisExamenFisicoUrgencias;
	}

	public void setEnviarEpicrisisExamenFisicoUrgencias(
			String enviarEpicrisisExamenFisicoUrgencias) {
		this.enviarEpicrisisExamenFisicoUrgencias = enviarEpicrisisExamenFisicoUrgencias;
	}

	public String getEnviarEpicrisisCausaExterna() {
		return enviarEpicrisisCausaExterna;
	}

	public void setEnviarEpicrisisCausaExterna(String enviarEpicrisisCausaExterna) {
		this.enviarEpicrisisCausaExterna = enviarEpicrisisCausaExterna;
	}

	public String getEnviarEpicrisisFinalidadConsulta() {
		return enviarEpicrisisFinalidadConsulta;
	}

	public void setEnviarEpicrisisFinalidadConsulta(
			String enviarEpicrisisFinalidadConsulta) {
		this.enviarEpicrisisFinalidadConsulta = enviarEpicrisisFinalidadConsulta;
	}

	public String getEnviarEpicrisisConductaValoracion() {
		return enviarEpicrisisConductaValoracion;
	}

	public void setEnviarEpicrisisConductaValoracion(
			String enviarEpicrisisConductaValoracion) {
		this.enviarEpicrisisConductaValoracion = enviarEpicrisisConductaValoracion;
	}

	public String getEnviarEpicrisisDiagnosticos() {
		return enviarEpicrisisDiagnosticos;
	}

	public void setEnviarEpicrisisDiagnosticos(String enviarEpicrisisDiagnosticos) {
		this.enviarEpicrisisDiagnosticos = enviarEpicrisisDiagnosticos;
	}

	public String getEnviarEpicrisisObservaciones() {
		return enviarEpicrisisObservaciones;
	}

	public void setEnviarEpicrisisObservaciones(String enviarEpicrisisObservaciones) {
		this.enviarEpicrisisObservaciones = enviarEpicrisisObservaciones;
	}

	public String getEnviarEpicrisisConceptoConsulta() {
		return enviarEpicrisisConceptoConsulta;
	}

	public void setEnviarEpicrisisConceptoConsulta(
			String enviarEpicrisisConceptoConsulta) {
		this.enviarEpicrisisConceptoConsulta = enviarEpicrisisConceptoConsulta;
	}

	public String getEnviarEpicrisisIncapacidadFuncioal() {
		return enviarEpicrisisIncapacidadFuncioal;
	}

	public void setEnviarEpicrisisIncapacidadFuncioal(
			String enviarEpicrisisIncapacidadFuncioal) {
		this.enviarEpicrisisIncapacidadFuncioal = enviarEpicrisisIncapacidadFuncioal;
	}

	public String getEnviarEpicrisisProximoControl() {
		return enviarEpicrisisProximoControl;
	}

	public void setEnviarEpicrisisProximoControl(
			String enviarEpicrisisProximoControl) {
		this.enviarEpicrisisProximoControl = enviarEpicrisisProximoControl;
	}

	/**
	 * @return the datoAreaPaciente
	 */
	public InfoDatosInt getDatoAreaPaciente() {
		return datoAreaPaciente;
	}

	/**
	 * @param datoAreaPaciente the datoAreaPaciente to set
	 */
	public void setDatoAreaPaciente(InfoDatosInt datoAreaPaciente) {
		this.datoAreaPaciente = datoAreaPaciente;
	}
		
	
	/**
	 * Alberto Ovalle
	 * mt5749
	 * atributo para vistaValoracion
	 */
	
	
	public String getFechaValoracionUrgencias() {
		return fechaValoracionUrgencias;
	}

	public void setFechaValoracionUrgencias(String fechaValoracionUrgencias) {
		this.fechaValoracionUrgencias = fechaValoracionUrgencias;
	}

	public String getHoraValoracionUrgencias() {
		return horaValoracionUrgencias;
	}

	public void setHoraValoracionUrgencias(String horaValoracionUrgencias) {
		this.horaValoracionUrgencias = horaValoracionUrgencias;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(String especialidades) {
		this.especialidades = especialidades;
	}

	public String getRegistromedico() {
		return registromedico;
	}

	public void setRegistromedico(String registromedico) {
		this.registromedico = registromedico;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getFechaValoracionObservacion() {
		return fechaValoracionObservacion;
	}

	public void setFechaValoracionObservacion(String fechaValoracionObservacion) {
		this.fechaValoracionObservacion = fechaValoracionObservacion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	
	public String getDesc_conducta_valoracion() {
		return desc_conducta_valoracion;
	}

	public void setDesc_conducta_valoracion(String desc_conducta_valoracion) {
		this.desc_conducta_valoracion = desc_conducta_valoracion;
	}
	
	public Date getFechaHoraValoracionUrgencias() {
		return fechaHoraValoracionUrgencias;
	}

	public void setFechaHoraValoracionUrgencias(
			Date fechaHoraValoracionUrgencias) {
		this.fechaHoraValoracionUrgencias = fechaHoraValoracionUrgencias;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getHoraValoracionObservacion() {
		return horaValoracionObservacion;
	}

	public void setHoraValoracionObservacion(String horaValoracionObservacion) {
		this.horaValoracionObservacion = horaValoracionObservacion;
	}
	public Date getFechaHoraObservacion() {
		return fechaHoraObservacion;
	}

	public void setFechaHoraObservacion(Date fechaHoraObservacion) {
		this.fechaHoraObservacion = fechaHoraObservacion;
	}

	public List<DtoValoracion> getVistaobservaciones() {
		if(vistaobservaciones == null)
		{
			vistaobservaciones = new ArrayList<DtoValoracion>();
		}
		return vistaobservaciones;
	}
	
	public void setVistaobservaciones(List<DtoValoracion> vistaobservaciones) {
		this.vistaobservaciones = vistaobservaciones;
	}

	public Boolean getImprimeFechaHora() {
			return imprimeFechaHora;
	}

	public void setImprimeFechaHora(Boolean imprimirFechaHora) {
		this.imprimeFechaHora = imprimirFechaHora;
	}

	
	public String getFormatoFechaHora() {
		return formatoFechaHora;
	}

	public void setFormatoFechaHora(String formatoFechaHora) {
		this.formatoFechaHora = formatoFechaHora;
	}

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}
	
	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	public String getEtiquetaObservaciones() {
		return etiquetaObservaciones;
	}
	/**
	 * Alberto Ovalle 
	 * mt5749
	 * @param etiquetaObservaciones
	 */

	public void setEtiquetaObservaciones(String etiquetaObservaciones) {
			if(etiquetaObservaciones !=null) {
			DtoValoracion dtoValoracion = new DtoValoracion();
			if(etiquetaObservaciones.trim().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales)) {
				etiquetaObservaciones = dtoValoracion.COMENTARIOS_GENERALES;
			} else if(etiquetaObservaciones.trim().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico)) {
				etiquetaObservaciones = dtoValoracion.PLAN_DIAGNOSTICO_TERAPEUTICO;
			}else if(etiquetaObservaciones.trim().equals(ConstantesIntegridadDominio.acronimoPronostico)) {
				etiquetaObservaciones = dtoValoracion.EXPLIQUE_DOS_DEBERES_DERECHOS;
			}
			else{
				etiquetaObservaciones = "";
			}
		}
		this.etiquetaObservaciones = etiquetaObservaciones;
	}
	
	
	public String getValorObservaciones() {
		return valorObservaciones;
	}

	public void setValorObservaciones(String valorObservaciones) {
		if(valorObservaciones !=null) {
			
			if(this.tipo.trim().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales)) {
				this.valorObservaciones = valorObservaciones;
			} else if(this.tipo.trim().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico)) {
				this.valorObservaciones = valorObservaciones;
			}else if(this.tipo.trim().equals(ConstantesIntegridadDominio.acronimoPronostico)) {
				this.valorObservaciones = valorObservaciones;
			}
			else{
				valorObservaciones = "";
			}
		}
		
		this.valorObservaciones = valorObservaciones;
	}

}
