package com.princetonsa.dto.administracion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoMotivoCitaPaciente;
import com.servinte.axioma.orm.Ciudades;

@SuppressWarnings("serial")
public class DtoPaciente extends DtoPersonas implements Serializable{

	private String numeroHistoriaClinica;
	private String anioHistoriaClincia;
	private String codigoPaisResidencia;
	private String codigoCiudadResidencia;
	private String codigoDepartamentoResidencia;
	private String codigoBarrio;
	private String nombreBarrio;
	private String criterioBarrio;
	private String codigoLocalidad;
	private String nombreLocalidad;
	private String direccion;
	private String telefonoFijo;
	private String telefonoCelular;
	private String email;
	private String zonaDomicilio;
	private String ocupacion;
	private String estadoCivil;
	private String centroAtencion;
	private String convenio;
	private String nomConvenio;
	private String etnia;
	private String religion;
	private String leeEscribe;
	private String estudio;
	private String grupoPoblacional;
	private boolean ingresoEdad; //para saber si se debe ingresar edad
	private String anios;
	private String seleccionEdad;
	private String foto;
	private boolean existePaciente; //indicador para saber si el paciente ya existe o es nuevo
	private boolean identificacionAutomatica; //me indica si maneja identificación automática
	private String activo;
	private int nroHijos;
	private ArrayList<DtoBeneficiarioPaciente> beneficiariosPac;
	private DtoMotivoCitaPaciente motivoCitaPaciente;
	private Ciudades ciudadVivienda;
	private String ciudadDeptoPais;
	
	private ArrayList<DtoSeccionConvenioPaciente> listaDtoSeccionConvenioPaciente;
	
	
	
	//*************ATRIBUTOS USADOS PARA EL FLUJO DE HISTORIA CLINICA ODONTOLOGICA***********************************************
	/*
	 * Hasta el momento se está usando para la impresion de la atención de historia clínica de odontologia
	 */
	private BigDecimal codigoCita;
	private BigDecimal idIngreso;
	private String edadIngreso;
	private String nombreCentroAtencion;
	private String nombresResponsables;
	private String nombreTipoAfiliado;
	private String nombreViaIngreso;
	//****************************************************************************************************************************
	
	
	//**********ATRIBUTOS PARA AGREGAR EN EL FORMATO IMPRESIÓN PLANTILLAS ATENCION ODONTOLÓGICA 1079*******//
	private String fechaNacimiento;
	private String edadPaciente;
	private String fechaIngreso;
	private String nombreCiudadResidencia;
	private String fechaEgreso;
	
	public DtoPaciente()
	{
		this.reset();
	}
	
	
	public void reset()
	{
		this.numeroHistoriaClinica= new String("");
		this.anioHistoriaClincia= new String("");
		this.codigoPaisResidencia= new String("");
		this.codigoCiudadResidencia= new String("");
		this.codigoBarrio= new String("");
		this.nombreBarrio= new String("");
		this.criterioBarrio= new String("");
		this.codigoLocalidad= new String("");
		this.nombreLocalidad= new String("");
		this.direccion= new String("");
		this.telefonoFijo= new String("");
		this.telefonoCelular= new String("");
		this.email= new String("");
		this.zonaDomicilio= new String("");
		this.ocupacion= new String("");
		this.estadoCivil= new String("");
		this.centroAtencion= new String("");
		this.etnia= new String("");
		this.religion= new String("");
		this.leeEscribe=new String("");
		this.estudio= new String("");
		this.grupoPoblacional= new String("");
		this.anios= new String("");
		this.foto= new String("");
		this.existePaciente=false; //indicador para saber si el paciente ya existe o es nuevo
		this.identificacionAutomatica=false; 
		this.anios=new String("");
		this.ingresoEdad=false;
		this.seleccionEdad=new String("");
		this.convenio=new String("");
		this.nomConvenio=new String("");
		this.activo = new String("");
		this.nroHijos=0;
		this.beneficiariosPac = new ArrayList<DtoBeneficiarioPaciente>();
		this.motivoCitaPaciente= new DtoMotivoCitaPaciente();
		
		// Anexo 860 - 1.12
		this.ciudadVivienda = new Ciudades();
		this.codigoDepartamentoResidencia = new String("");
		this.ciudadDeptoPais = "";	// Contiene en orden los codigos de la ciudad, departamento y pais separados por ConstantesBd.separadorSplit
		this.listaDtoSeccionConvenioPaciente = new ArrayList<DtoSeccionConvenioPaciente>();
		//------------------
		
		///*************ATRIBUTOS USADOS PARA EL FLUJO DE HISTORIA CLINICA ODONTOLOGICA***********************************************
		this.codigoCita = new BigDecimal(0);
		this.idIngreso = new BigDecimal(0);
		this.edadIngreso = "";
		this.nombreCentroAtencion = "";
		this.nombresResponsables = "";
		this.nombreTipoAfiliado = "";
		this.nombreViaIngreso = "";
		//****************************************************************************************************************************
	}


	/**
	 * Obtiene el valor del atributo numeroHistoriaClinica
	 *
	 * @return Retorna atributo numeroHistoriaClinica
	 */
	public String getNumeroHistoriaClinica()
	{
		return numeroHistoriaClinica;
	}


	/**
	 * Establece el valor del atributo numeroHistoriaClinica
	 *
	 * @param valor para el atributo numeroHistoriaClinica
	 */
	public void setNumeroHistoriaClinica(String numeroHistoriaClinica)
	{
		this.numeroHistoriaClinica = numeroHistoriaClinica;
	}


	/**
	 * Obtiene el valor del atributo anioHistoriaClincia
	 *
	 * @return Retorna atributo anioHistoriaClincia
	 */
	public String getAnioHistoriaClincia()
	{
		return anioHistoriaClincia;
	}


	/**
	 * Establece el valor del atributo anioHistoriaClincia
	 *
	 * @param valor para el atributo anioHistoriaClincia
	 */
	public void setAnioHistoriaClincia(String anioHistoriaClincia)
	{
		this.anioHistoriaClincia = anioHistoriaClincia;
	}


	/**
	 * Obtiene el valor del atributo codigoPaisResidencia
	 *
	 * @return Retorna atributo codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia()
	{
		return codigoPaisResidencia;
	}


	/**
	 * Establece el valor del atributo codigoPaisResidencia
	 *
	 * @param valor para el atributo codigoPaisResidencia
	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia)
	{
		this.codigoPaisResidencia = codigoPaisResidencia;
	}


	/**
	 * Obtiene el valor del atributo codigoCiudadResidencia
	 *
	 * @return Retorna atributo codigoCiudadResidencia
	 */
	public String getCodigoCiudadResidencia()
	{
		return codigoCiudadResidencia;
	}


	/**
	 * Establece el valor del atributo codigoCiudadResidencia
	 *
	 * @param valor para el atributo codigoCiudadResidencia
	 */
	public void setCodigoCiudadResidencia(String codigoCiudadResidencia)
	{
		this.codigoCiudadResidencia = codigoCiudadResidencia;
	}


	/**
	 * Obtiene el valor del atributo codigoDepartamentoResidencia
	 *
	 * @return Retorna atributo codigoDepartamentoResidencia
	 */
	public String getCodigoDepartamentoResidencia()
	{
		return codigoDepartamentoResidencia;
	}


	/**
	 * Establece el valor del atributo codigoDepartamentoResidencia
	 *
	 * @param valor para el atributo codigoDepartamentoResidencia
	 */
	public void setCodigoDepartamentoResidencia(String codigoDepartamentoResidencia)
	{
		this.codigoDepartamentoResidencia = codigoDepartamentoResidencia;
	}


	/**
	 * Obtiene el valor del atributo codigoBarrio
	 *
	 * @return Retorna atributo codigoBarrio
	 */
	public String getCodigoBarrio()
	{
		return codigoBarrio;
	}


	/**
	 * Establece el valor del atributo codigoBarrio
	 *
	 * @param valor para el atributo codigoBarrio
	 */
	public void setCodigoBarrio(String codigoBarrio)
	{
		this.codigoBarrio = codigoBarrio;
	}


	/**
	 * Obtiene el valor del atributo nombreBarrio
	 *
	 * @return Retorna atributo nombreBarrio
	 */
	public String getNombreBarrio()
	{
		return nombreBarrio;
	}


	/**
	 * Establece el valor del atributo nombreBarrio
	 *
	 * @param valor para el atributo nombreBarrio
	 */
	public void setNombreBarrio(String nombreBarrio)
	{
		this.nombreBarrio = nombreBarrio;
	}


	/**
	 * Obtiene el valor del atributo criterioBarrio
	 *
	 * @return Retorna atributo criterioBarrio
	 */
	public String getCriterioBarrio()
	{
		return criterioBarrio;
	}


	/**
	 * Establece el valor del atributo criterioBarrio
	 *
	 * @param valor para el atributo criterioBarrio
	 */
	public void setCriterioBarrio(String criterioBarrio)
	{
		this.criterioBarrio = criterioBarrio;
	}


	/**
	 * Obtiene el valor del atributo codigoLocalidad
	 *
	 * @return Retorna atributo codigoLocalidad
	 */
	public String getCodigoLocalidad()
	{
		return codigoLocalidad;
	}


	/**
	 * Establece el valor del atributo codigoLocalidad
	 *
	 * @param valor para el atributo codigoLocalidad
	 */
	public void setCodigoLocalidad(String codigoLocalidad)
	{
		this.codigoLocalidad = codigoLocalidad;
	}


	/**
	 * Obtiene el valor del atributo nombreLocalidad
	 *
	 * @return Retorna atributo nombreLocalidad
	 */
	public String getNombreLocalidad()
	{
		return nombreLocalidad;
	}


	/**
	 * Establece el valor del atributo nombreLocalidad
	 *
	 * @param valor para el atributo nombreLocalidad
	 */
	public void setNombreLocalidad(String nombreLocalidad)
	{
		this.nombreLocalidad = nombreLocalidad;
	}


	/**
	 * Obtiene el valor del atributo direccion
	 *
	 * @return Retorna atributo direccion
	 */
	public String getDireccion()
	{
		return direccion;
	}


	/**
	 * Establece el valor del atributo direccion
	 *
	 * @param valor para el atributo direccion
	 */
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}


	/**
	 * Obtiene el valor del atributo telefonoFijo
	 *
	 * @return Retorna atributo telefonoFijo
	 */
	public String getTelefonoFijo()
	{
		return telefonoFijo+"";
	}


	/**
	 * Establece el valor del atributo telefonoFijo
	 *
	 * @param valor para el atributo telefonoFijo
	 */
	public void setTelefonoFijo(String telefonoFijo)
	{
		this.telefonoFijo = telefonoFijo;
	}


	/**
	 * Obtiene el valor del atributo telefonoCelular
	 *
	 * @return Retorna atributo telefonoCelular
	 */
	public String getTelefonoCelular()
	{
		return telefonoCelular;
	}


	/**
	 * Establece el valor del atributo telefonoCelular
	 *
	 * @param valor para el atributo telefonoCelular
	 */
	public void setTelefonoCelular(String telefonoCelular)
	{
		this.telefonoCelular = telefonoCelular;
	}


	/**
	 * Obtiene el valor del atributo email
	 *
	 * @return Retorna atributo email
	 */
	public String getEmail()
	{
		return email;
	}


	/**
	 * Establece el valor del atributo email
	 *
	 * @param valor para el atributo email
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}


	/**
	 * Obtiene el valor del atributo zonaDomicilio
	 *
	 * @return Retorna atributo zonaDomicilio
	 */
	public String getZonaDomicilio()
	{
		return zonaDomicilio;
	}


	/**
	 * Establece el valor del atributo zonaDomicilio
	 *
	 * @param valor para el atributo zonaDomicilio
	 */
	public void setZonaDomicilio(String zonaDomicilio)
	{
		this.zonaDomicilio = zonaDomicilio;
	}


	/**
	 * Obtiene el valor del atributo ocupacion
	 *
	 * @return Retorna atributo ocupacion
	 */
	public String getOcupacion()
	{
		return ocupacion;
	}


	/**
	 * Establece el valor del atributo ocupacion
	 *
	 * @param valor para el atributo ocupacion
	 */
	public void setOcupacion(String ocupacion)
	{
		this.ocupacion = ocupacion;
	}


	/**
	 * Obtiene el valor del atributo estadoCivil
	 *
	 * @return Retorna atributo estadoCivil
	 */
	public String getEstadoCivil()
	{
		return estadoCivil;
	}


	/**
	 * Establece el valor del atributo estadoCivil
	 *
	 * @param valor para el atributo estadoCivil
	 */
	public void setEstadoCivil(String estadoCivil)
	{
		this.estadoCivil = estadoCivil;
	}


	/**
	 * Obtiene el valor del atributo centroAtencion
	 *
	 * @return Retorna atributo centroAtencion
	 */
	public String getCentroAtencion()
	{
		return centroAtencion;
	}


	/**
	 * Establece el valor del atributo centroAtencion
	 *
	 * @param valor para el atributo centroAtencion
	 */
	public void setCentroAtencion(String centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}


	/**
	 * Obtiene el valor del atributo convenio
	 *
	 * @return Retorna atributo convenio
	 */
	public String getConvenio()
	{
		return convenio;
	}


	/**
	 * Establece el valor del atributo convenio
	 *
	 * @param valor para el atributo convenio
	 */
	public void setConvenio(String convenio)
	{
		this.convenio = convenio;
	}


	/**
	 * Obtiene el valor del atributo nomConvenio
	 *
	 * @return Retorna atributo nomConvenio
	 */
	public String getNomConvenio()
	{
		return nomConvenio;
	}


	/**
	 * Establece el valor del atributo nomConvenio
	 *
	 * @param valor para el atributo nomConvenio
	 */
	public void setNomConvenio(String nomConvenio)
	{
		this.nomConvenio = nomConvenio;
	}


	/**
	 * Obtiene el valor del atributo etnia
	 *
	 * @return Retorna atributo etnia
	 */
	public String getEtnia()
	{
		return etnia;
	}


	/**
	 * Establece el valor del atributo etnia
	 *
	 * @param valor para el atributo etnia
	 */
	public void setEtnia(String etnia)
	{
		this.etnia = etnia;
	}


	/**
	 * Obtiene el valor del atributo religion
	 *
	 * @return Retorna atributo religion
	 */
	public String getReligion()
	{
		return religion;
	}


	/**
	 * Establece el valor del atributo religion
	 *
	 * @param valor para el atributo religion
	 */
	public void setReligion(String religion)
	{
		this.religion = religion;
	}


	/**
	 * Obtiene el valor del atributo leeEscribe
	 *
	 * @return Retorna atributo leeEscribe
	 */
	public String getLeeEscribe()
	{
		return leeEscribe;
	}


	/**
	 * Establece el valor del atributo leeEscribe
	 *
	 * @param valor para el atributo leeEscribe
	 */
	public void setLeeEscribe(String leeEscribe)
	{
		this.leeEscribe = leeEscribe;
	}


	/**
	 * Obtiene el valor del atributo estudio
	 *
	 * @return Retorna atributo estudio
	 */
	public String getEstudio()
	{
		return estudio;
	}


	/**
	 * Establece el valor del atributo estudio
	 *
	 * @param valor para el atributo estudio
	 */
	public void setEstudio(String estudio)
	{
		this.estudio = estudio;
	}


	/**
	 * Obtiene el valor del atributo grupoPoblacional
	 *
	 * @return Retorna atributo grupoPoblacional
	 */
	public String getGrupoPoblacional()
	{
		return grupoPoblacional;
	}


	/**
	 * Establece el valor del atributo grupoPoblacional
	 *
	 * @param valor para el atributo grupoPoblacional
	 */
	public void setGrupoPoblacional(String grupoPoblacional)
	{
		this.grupoPoblacional = grupoPoblacional;
	}


	/**
	 * Obtiene el valor del atributo ingresoEdad
	 *
	 * @return Retorna atributo ingresoEdad
	 */
	public boolean isIngresoEdad()
	{
		return ingresoEdad;
	}


	/**
	 * Establece el valor del atributo ingresoEdad
	 *
	 * @param valor para el atributo ingresoEdad
	 */
	public void setIngresoEdad(boolean ingresoEdad)
	{
		this.ingresoEdad = ingresoEdad;
	}


	/**
	 * Obtiene el valor del atributo anios
	 *
	 * @return Retorna atributo anios
	 */
	public String getAnios()
	{
		return anios;
	}


	/**
	 * Establece el valor del atributo anios
	 *
	 * @param valor para el atributo anios
	 */
	public void setAnios(String anios)
	{
		this.anios = anios;
	}


	/**
	 * Obtiene el valor del atributo seleccionEdad
	 *
	 * @return Retorna atributo seleccionEdad
	 */
	public String getSeleccionEdad()
	{
		return seleccionEdad;
	}


	/**
	 * Establece el valor del atributo seleccionEdad
	 *
	 * @param valor para el atributo seleccionEdad
	 */
	public void setSeleccionEdad(String seleccionEdad)
	{
		this.seleccionEdad = seleccionEdad;
	}


	/**
	 * Obtiene el valor del atributo foto
	 *
	 * @return Retorna atributo foto
	 */
	public String getFoto()
	{
		return foto;
	}


	/**
	 * Establece el valor del atributo foto
	 *
	 * @param valor para el atributo foto
	 */
	public void setFoto(String foto)
	{
		this.foto = foto;
	}


	/**
	 * Obtiene el valor del atributo existePaciente
	 *
	 * @return Retorna atributo existePaciente
	 */
	public boolean isExistePaciente()
	{
		return existePaciente;
	}


	/**
	 * Establece el valor del atributo existePaciente
	 *
	 * @param valor para el atributo existePaciente
	 */
	public void setExistePaciente(boolean existePaciente)
	{
		this.existePaciente = existePaciente;
	}


	/**
	 * Obtiene el valor del atributo identificacionAutomatica
	 *
	 * @return Retorna atributo identificacionAutomatica
	 */
	public boolean isIdentificacionAutomatica()
	{
		return identificacionAutomatica;
	}


	/**
	 * Establece el valor del atributo identificacionAutomatica
	 *
	 * @param valor para el atributo identificacionAutomatica
	 */
	public void setIdentificacionAutomatica(boolean identificacionAutomatica)
	{
		this.identificacionAutomatica = identificacionAutomatica;
	}


	/**
	 * Obtiene el valor del atributo activo
	 *
	 * @return Retorna atributo activo
	 */
	public String getActivo()
	{
		return activo;
	}


	/**
	 * Establece el valor del atributo activo
	 *
	 * @param valor para el atributo activo
	 */
	public void setActivo(String activo)
	{
		this.activo = activo;
	}


	/**
	 * Obtiene el valor del atributo nroHijos
	 *
	 * @return Retorna atributo nroHijos
	 */
	public int getNroHijos()
	{
		return nroHijos;
	}


	/**
	 * Establece el valor del atributo nroHijos
	 *
	 * @param valor para el atributo nroHijos
	 */
	public void setNroHijos(int nroHijos)
	{
		this.nroHijos = nroHijos;
	}


	/**
	 * Obtiene el valor del atributo beneficiariosPac
	 *
	 * @return Retorna atributo beneficiariosPac
	 */
	public ArrayList<DtoBeneficiarioPaciente> getBeneficiariosPac()
	{
		return beneficiariosPac;
	}


	/**
	 * Establece el valor del atributo beneficiariosPac
	 *
	 * @param valor para el atributo beneficiariosPac
	 */
	public void setBeneficiariosPac(
			ArrayList<DtoBeneficiarioPaciente> beneficiariosPac)
	{
		this.beneficiariosPac = beneficiariosPac;
	}


	/**
	 * Obtiene el valor del atributo motivoCitaPaciente
	 *
	 * @return Retorna atributo motivoCitaPaciente
	 */
	public DtoMotivoCitaPaciente getMotivoCitaPaciente()
	{
		return motivoCitaPaciente;
	}


	/**
	 * Establece el valor del atributo motivoCitaPaciente
	 *
	 * @param valor para el atributo motivoCitaPaciente
	 */
	public void setMotivoCitaPaciente(DtoMotivoCitaPaciente motivoCitaPaciente)
	{
		this.motivoCitaPaciente = motivoCitaPaciente;
	}


	/**
	 * Obtiene el valor del atributo ciudadVivienda
	 *
	 * @return Retorna atributo ciudadVivienda
	 */
	public Ciudades getCiudadVivienda()
	{
		return ciudadVivienda;
	}


	/**
	 * Establece el valor del atributo ciudadVivienda
	 *
	 * @param valor para el atributo ciudadVivienda
	 */
	public void setCiudadVivienda(Ciudades ciudadVivienda)
	{
		this.ciudadVivienda = ciudadVivienda;
	}


	/**
	 * Obtiene el valor del atributo ciudadDeptoPais
	 *
	 * @return Retorna atributo ciudadDeptoPais
	 */
	public String getCiudadDeptoPais()
	{
		return ciudadDeptoPais;
	}


	/**
	 * Establece el valor del atributo ciudadDeptoPais
	 *
	 * @param valor para el atributo ciudadDeptoPais
	 */
	public void setCiudadDeptoPais(String ciudadDeptoPais)
	{
		this.ciudadDeptoPais = ciudadDeptoPais;
	}


	/**
	 * Obtiene el valor del atributo listaDtoSeccionConvenioPaciente
	 *
	 * @return Retorna atributo listaDtoSeccionConvenioPaciente
	 */
	public ArrayList<DtoSeccionConvenioPaciente> getListaDtoSeccionConvenioPaciente()
	{
		return listaDtoSeccionConvenioPaciente;
	}


	/**
	 * Establece el valor del atributo listaDtoSeccionConvenioPaciente
	 *
	 * @param valor para el atributo listaDtoSeccionConvenioPaciente
	 */
	public void setListaDtoSeccionConvenioPaciente(
			ArrayList<DtoSeccionConvenioPaciente> listaDtoSeccionConvenioPaciente)
	{
		this.listaDtoSeccionConvenioPaciente = listaDtoSeccionConvenioPaciente;
	}


	/**
	 * Obtiene el valor del atributo codigoCita
	 *
	 * @return Retorna atributo codigoCita
	 */
	public BigDecimal getCodigoCita()
	{
		return codigoCita;
	}


	/**
	 * Establece el valor del atributo codigoCita
	 *
	 * @param valor para el atributo codigoCita
	 */
	public void setCodigoCita(BigDecimal codigoCita)
	{
		this.codigoCita = codigoCita;
	}


	/**
	 * Obtiene el valor del atributo idIngreso
	 *
	 * @return Retorna atributo idIngreso
	 */
	public BigDecimal getIdIngreso()
	{
		return idIngreso;
	}


	/**
	 * Establece el valor del atributo idIngreso
	 *
	 * @param valor para el atributo idIngreso
	 */
	public void setIdIngreso(BigDecimal idIngreso)
	{
		this.idIngreso = idIngreso;
	}


	/**
	 * Obtiene el valor del atributo edadIngreso
	 *
	 * @return Retorna atributo edadIngreso
	 */
	public String getEdadIngreso()
	{
		return edadIngreso;
	}


	/**
	 * Establece el valor del atributo edadIngreso
	 *
	 * @param valor para el atributo edadIngreso
	 */
	public void setEdadIngreso(String edadIngreso)
	{
		this.edadIngreso = edadIngreso;
	}


	/**
	 * Obtiene el valor del atributo nombreCentroAtencion
	 *
	 * @return Retorna atributo nombreCentroAtencion
	 */
	public String getNombreCentroAtencion()
	{
		return nombreCentroAtencion;
	}


	/**
	 * Establece el valor del atributo nombreCentroAtencion
	 *
	 * @param valor para el atributo nombreCentroAtencion
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion)
	{
		this.nombreCentroAtencion = nombreCentroAtencion;
	}


	/**
	 * Obtiene el valor del atributo nombresResponsables
	 *
	 * @return Retorna atributo nombresResponsables
	 */
	public String getNombresResponsables()
	{
		return nombresResponsables;
	}


	/**
	 * Establece el valor del atributo nombresResponsables
	 *
	 * @param valor para el atributo nombresResponsables
	 */
	public void setNombresResponsables(String nombresResponsables)
	{
		this.nombresResponsables = nombresResponsables;
	}


	/**
	 * Obtiene el valor del atributo nombreTipoAfiliado
	 *
	 * @return Retorna atributo nombreTipoAfiliado
	 */
	public String getNombreTipoAfiliado()
	{
		return nombreTipoAfiliado;
	}


	/**
	 * Establece el valor del atributo nombreTipoAfiliado
	 *
	 * @param valor para el atributo nombreTipoAfiliado
	 */
	public void setNombreTipoAfiliado(String nombreTipoAfiliado)
	{
		this.nombreTipoAfiliado = nombreTipoAfiliado;
	}


	/**
	 * Obtiene el valor del atributo nombreViaIngreso
	 *
	 * @return Retorna atributo nombreViaIngreso
	 */
	public String getNombreViaIngreso()
	{
		return nombreViaIngreso;
	}


	/**
	 * Establece el valor del atributo nombreViaIngreso
	 *
	 * @param valor para el atributo nombreViaIngreso
	 */
	public void setNombreViaIngreso(String nombreViaIngreso)
	{
		this.nombreViaIngreso = nombreViaIngreso;
	}


	/**
	 * Obtiene el valor del atributo fechaNacimiento
	 *
	 * @return Retorna atributo fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * Establece el valor del atributo fechaNacimiento
	 *
	 * @param valor para el atributo fechaNacimiento
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * Establece el valor del atributo edadPaciente
	 *
	 * @param valor para el atributo edadPaciente
	 */
	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}


	/**
	 * Obtiene el valor del atributo edadPaciente donde se calcula
	 * la edad basandose en la fecha de nacimiento
	 *
	 * @return Retorna atributo edadPaciente
	 */
	public String getEdadPaciente() {
		if(fechaNacimiento!=null){
			String fechaActual=UtilidadFecha.getFechaActual();
			this.edadPaciente=UtilidadFecha.calcularEdadDetallada(fechaNacimiento, fechaActual);
		}else
			this.edadPaciente="";
		return edadPaciente;
	}

	/**
	 * Establece el valor del atributo fechaIngreso del paciente
	 *
	 * @param valor para el atributo fechaIngreso
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	/**
	 * Obtiene el valor del atributo fechaIngreso del paciente
	 *
	 * @return Retorna atributo fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * Establece el valor del atributo nombreCiudadResidencia del paciente
	 *
	 * @param valor para el atributo nombreCiudadResidencia
	 */
	public void setNombreCiudadResidencia(String nombreCiudadResidencia) {
		this.nombreCiudadResidencia = nombreCiudadResidencia;
	}

	/**
	 * Obtiene el valor del atributo nombreCiudadResidencia del paciente
	 *
	 * @return Retorna atributo nombreCiudadResidencia
	 */
	public String getNombreCiudadResidencia() {
		return nombreCiudadResidencia;
	}

	/**
	 * Establece el valor del atributo fechaEgreso o fin de tratamiento
	 *
	 * @param valor para el atributo fechaEgreso
	 */
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	/**
	 * Obtiene el valor del atributo fechaEgreso o fin de tratamiento 
	 *
	 * @return Retorna atributo fechaEgreso
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}


	
}
