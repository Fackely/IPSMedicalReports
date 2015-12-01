package com.princetonsa.dto.inventario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.ConstantesBD;

/**
 * Encaps&uacute;la la respuesta a la informaci&oacute;n de los servicios para las autorizaciones
 * 
 * @author Cristhian Murillo
 */
/**
 * @author ricruico
 *
 */
public class DtoServiciosAutorizaciones implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	/*--------------------------*/
	/* PATRIBUTOS DTO			*/
	/*--------------------------*/
	
	// ServicioS  ---------------------------------------------------------
	private Integer codigoServicio;
	private String codigoPropietario;
	private String descripcionServicio;
	private int nivelAutorizacion;
	private int cantidadAutorizadaServicio;	
	private Integer numeroOrden;
	private Integer consecutivoOrdenMed;
	private String consecutivoOrdenMedStr;
	private Long numeroOrdenLong;
	private String numeroOrdenStr;
	private Date fechaOrden;
	private String horaOrden;
	private BigDecimal valorServicio;
	private BigDecimal valorAutorizado;
	private long autorizacionPropia;
	private int cantidadSolicitada;	
	private Integer finalidadServicio;
	private Character urgente;
	private String descripcionNivelAutorizacion;
	private String descripcionNivelAtencion;
	/** Indica que ha sido seleccionado o que es valido*/
	private boolean flag;
	private boolean valido;
	/** aplica para las peticiones e indica el número (prioridad) a la que corresponde el servicio */
	private Integer numeroServicio;
	private int codigoGrupoServicio;
	private String nombreGrupoServicio;
		
	// Diagnostico	
	private String diagnostico;		
	private String acronimoDx;
	private Integer tipoCieDx;	
	private String descripcionDiagnostico;
	
	// Grupo Servicio
	private Byte numDiasUrgente;
	private String acroDiasUrgente;
	private Byte numDiasNormal;
	private String acroDiasNormal;
	
	private Integer codigoTipoMonto;
	
	private Long numeroOrdenAmb;
	
	private String consecutivoOrdenAmb;
	
	private Date fechaOrdenAmb;
	
	private Integer numeroPeticion;
	
	private Date fechaPeticion;
	
	// Diagnostico	Ordenes Ambulatorias
	
	private String diagnosticoOrdenAmb;		
	
	private Integer tipoCieDxOrdenAmb;	
	
	private String descripcionDiagnosticoOrdenAmb;
	
	// Diagnostico	Peticiones
	
	private String diagnosticoPeticion;		
	
	private Integer tipoCieDxPeticion;	
	
	private String descripcionDiagnosticoPeticion;
	
	//Diagnostico	Peticiones
	
	private String diagnosticoIngEst;		
	
	private Integer tipoCieDxIngEst;	
	
	private String descripcionDiagnosticoIngEst;
	
	
	/**
	 * Atributo necesario para solucionar incidencia 1759
	 */
	private int codigoCentroCostoSolicitado;
	
	/**
	 * Atributo necesario para solucionar incidencia 1759
	 */
	private int codigoViaIngreso;
	
	/**Atributo que almacena las observaciones de la solicitud del servicio*/
	private String observaciones;
	
	/**Atributo que almacena el contrato del convenio responsable*/
	private Integer contratoConvenioResponsable;
	
	/**
	 * Define si el servicio fue solicitado, proviene de una orden ambulatoria, de una peticion o de un ingreso estancia
	 */
	private long tipoAutorizacion;
	
	/**
	 * Codigo que tiene la orden ambulatoria o solicitud o peticion
	 */
	private Long codigoOrdenSolPet;
	
	
	/**
	 * Especialidad del servicio
	 */
	private int especialidad;
	/**
	 * Tipo de servicio 
	 */
	private String tipoServicio;
	/**
	 * Grupo en el que se encuentra el servicio
	 */
	private int grupoServicio;
	
	/**
	 * Identificador del ingreso con el que se registro la orden del servicio
	 */
	private long idIngreso;
	/**
	 * Identificador de la cuenta con la que se registro la orden del servicio
	 */
	private long codigoCuenta;
	/**
	 * Tipo de paciente registrado en la cuenta de la solicitud del servicio
	 */
	private String tipoPaciente;
	
	private int tipoSolicitud;
	
	private boolean pyp;
	
	/**
	 * Constructor
	 */
	public DtoServiciosAutorizaciones()
	{
		this.codigoServicio					= null;
		this.codigoPropietario				= null;
		this.descripcionServicio			= "";
		this.nivelAutorizacion				= ConstantesBD.codigoNuncaValido;
		this.cantidadAutorizadaServicio		= ConstantesBD.codigoNuncaValido;
		this.codigoCentroCostoSolicitado	= ConstantesBD.codigoNuncaValido;
		this.codigoViaIngreso	= ConstantesBD.codigoNuncaValido;
		this.diagnostico					= "";
		this.numeroOrden					= null;
		this.consecutivoOrdenMed			= null;
		this.consecutivoOrdenMedStr			= null;
		this.numeroOrdenLong				= null;
		this.numeroOrdenStr					= null;
		this.fechaOrden						= null;
		this.horaOrden						="";
		this.valorServicio					= null;
		this.valorAutorizado				= null;
		this.finalidadServicio				= null;
		this.urgente						= null;
		this.flag							= false;
		this.valido							= false;
		this.numeroServicio					= null;
		
		this.acronimoDx						= "";;
		this.tipoCieDx						= null;
		this.descripcionDiagnostico         = "";
		
		this.numDiasUrgente					= null;
		this.acroDiasUrgente				= null;
		this.numDiasNormal					= null;
		this.acroDiasNormal					= null;
		
		this.codigoTipoMonto				= null;
		
		this.numeroOrdenAmb					= null;
		this.consecutivoOrdenAmb			= null;
		this.fechaOrdenAmb					= null;
		this.numeroPeticion					= null;
		this.fechaPeticion					= null;
		
		this.diagnosticoOrdenAmb			= "";;
		this.tipoCieDxOrdenAmb				= null;
		this.descripcionDiagnosticoOrdenAmb = "";
		
		this.diagnosticoPeticion			= "";;
		this.tipoCieDxPeticion				= null;
		this.descripcionDiagnosticoPeticion = "";
		
		this.diagnosticoIngEst				= "";;
		this.tipoCieDxIngEst				= null;
		this.descripcionDiagnosticoIngEst	= "";
		this.observaciones					= "";
		this.tipoSolicitud					= ConstantesBD.codigoNuncaValido;
		this.setContratoConvenioResponsable(ConstantesBD.codigoNuncaValido);
		
	}


	
	public Integer getCodigoServicio() {
		return codigoServicio;
	}


	public String getCodigoPropietario() {
		return codigoPropietario;
	}



	public void setCodigoPropietario(String codigoPropietario) {
		this.codigoPropietario = codigoPropietario;
	}



	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	public String getDescripcionServicio() {
		return descripcionServicio;
	}


	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}
	
	public int getCantidadAutorizadaServicio() {
		return cantidadAutorizadaServicio;
	}


	public void setCantidadAutorizadaServicio(int cantidadAutorizadaServicio) {
		this.cantidadAutorizadaServicio = cantidadAutorizadaServicio;
	}


	public String getDiagnostico() {
		return diagnostico;
	}


	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}


	public Integer getNumeroOrden() {
		return numeroOrden;
	}


	public void setNumeroOrden(Integer numeroOrden) {
		this.numeroOrden = numeroOrden;
		if(numeroOrden != null){
			this.numeroOrdenLong = Long.valueOf(numeroOrden.longValue());
			this.numeroOrdenStr = String.valueOf(numeroOrden.intValue());
		}
		else{
			this.numeroOrdenLong = null;
			this.numeroOrdenStr = null;
		}
	}


	public Date getFechaOrden() {
		return fechaOrden;
	}


	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}


	public BigDecimal getValorServicio() {
		return valorServicio;
	}


	public void setValorServicio(BigDecimal valorServicio) {
		this.valorServicio = valorServicio;
	}

	public BigDecimal getValorAutorizado() {
		return valorAutorizado;
	}



	public void setValorAutorizado(BigDecimal valorAutorizado) {
		this.valorAutorizado = valorAutorizado;
	}



	public Integer getFinalidadServicio() {
		return finalidadServicio;
	}


	public void setFinalidadServicio(Integer finalidadServicio) {
		this.finalidadServicio = finalidadServicio;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo autorizacionPropia
	
	 * @return retorna la variable autorizacionPropia 
	 * @author Angela Maria Aguirre 
	 */
	public long getAutorizacionPropia() {
		return autorizacionPropia;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo autorizacionPropia
	
	 * @param valor para el atributo autorizacionPropia 
	 * @author Angela Maria Aguirre 
	 */
	public void setAutorizacionPropia(long autorizacionPropia) {
		this.autorizacionPropia = autorizacionPropia;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadSolicitada
	
	 * @return retorna la variable cantidadSolicitada 
	 * @author Angela Maria Aguirre 
	 */
	public int getCantidadSolicitada() {
		return cantidadSolicitada;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadSolicitada
	
	 * @param valor para el atributo cantidadSolicitada 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadSolicitada(int cantidadSolicitada) {
		this.cantidadSolicitada = cantidadSolicitada;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAutorizacion
	
	 * @return retorna la variable nivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutorizacion() {
		return nivelAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutorizacion
	
	 * @param valor para el atributo nivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacion(int nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo urgente
	
	 * @return retorna la variable urgente 
	 * @author Angela Maria Aguirre 
	 */
	public Character getUrgente() {
		return urgente;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo urgente
	
	 * @param valor para el atributo urgente 
	 * @author Angela Maria Aguirre 
	 */
	public void setUrgente(Character urgente) {
		this.urgente = urgente;
	}


	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}


	public String getAcronimoDx() {
		return acronimoDx;
	}


	public void setAcronimoDx(String acronimoDx) {
		this.acronimoDx = acronimoDx;
	}


	public Integer getTipoCieDx() {
		return tipoCieDx;
	}


	public void setTipoCieDx(Integer tipoCieDx) {
		this.tipoCieDx = tipoCieDx;
	}



	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcionNivelAutorizacion
	
	 * @return retorna la variable descripcionNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionNivelAutorizacion() {
		return descripcionNivelAutorizacion;
	}



	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcionNivelAutorizacion
	
	 * @param valor para el atributo descripcionNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionNivelAutorizacion(String descripcionNivelAutorizacion) {
		this.descripcionNivelAutorizacion = descripcionNivelAutorizacion;
	}



	public boolean isValido() {
		return valido;
	}



	public void setValido(boolean valido) {
		this.valido = valido;
	}



	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcionDiagnostico
	
	 * @return retorna la variable descripcionDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionDiagnostico() {
		return descripcionDiagnostico;
	}



	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcionDiagnostico
	
	 * @param valor para el atributo descripcionDiagnostico 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionDiagnostico(String descripcionDiagnostico) {
		this.descripcionDiagnostico = descripcionDiagnostico;
	}



	public void setHoraOrden(String horaOrden) {
		this.horaOrden = horaOrden;
	}



	public String getHoraOrden() {
		return horaOrden;
	}



	/**
	 * @return valor de numDiasUrgente
	 */
	public Byte getNumDiasUrgente() {
		return numDiasUrgente;
	}



	/**
	 * @param numDiasUrgente el numDiasUrgente para asignar
	 */
	public void setNumDiasUrgente(Byte numDiasUrgente) {
		this.numDiasUrgente = numDiasUrgente;
	}



	/**
	 * @return valor de acroDiasUrgente
	 */
	public String getAcroDiasUrgente() {
		return acroDiasUrgente;
	}



	/**
	 * @param acroDiasUrgente el acroDiasUrgente para asignar
	 */
	public void setAcroDiasUrgente(String acroDiasUrgente) {
		this.acroDiasUrgente = acroDiasUrgente;
	}



	/**
	 * @return valor de numDiasNormal
	 */
	public Byte getNumDiasNormal() {
		return numDiasNormal;
	}



	/**
	 * @param numDiasNormal el numDiasNormal para asignar
	 */
	public void setNumDiasNormal(Byte numDiasNormal) {
		this.numDiasNormal = numDiasNormal;
	}



	/**
	 * @return valor de acroDiasNormal
	 */
	public String getAcroDiasNormal() {
		return acroDiasNormal;
	}



	/**
	 * @param acroDiasNormal el acroDiasNormal para asignar
	 */
	public void setAcroDiasNormal(String acroDiasNormal) {
		this.acroDiasNormal = acroDiasNormal;
	}

	public void setConsecutivoOrdenMed(Integer consecutivoOrdenMed) {
		this.consecutivoOrdenMed = consecutivoOrdenMed;
	}



	public Integer getConsecutivoOrdenMed() {
		return consecutivoOrdenMed;
	}
	
	/**
	 * @return valor de numeroOrdenLong
	 */
	public Long getNumeroOrdenLong() {
		return numeroOrdenLong;
	}


	/**
	 * @param numeroOrdenLong el numeroOrdenLong para asignar
	 */
	public void setNumeroOrdenLong(Long numeroOrdenLong) {
		this.numeroOrdenLong = numeroOrdenLong;
		if(this.numeroOrdenLong!=null){
			this.numeroOrden	= this.numeroOrdenLong.intValue();
		}else{
			this.numeroOrden = null;
		}
	}

	/**
	 * @return valor de numeroOrdenStr
	 */
	public String getNumeroOrdenStr() {
		return numeroOrdenStr;
	}

	/**
	 * @param numeroOrdenStr el numeroOrdenStr para asignar
	 */
	public void setNumeroOrdenStr(String numeroOrdenStr) {
		this.numeroOrdenStr = numeroOrdenStr;
		this.numeroOrden = Integer.parseInt(this.numeroOrdenStr);
	}



	
	/**
	 * @return valor de consecutivoOrdenMedStr
	 */
	public String getConsecutivoOrdenMedStr() {
		return consecutivoOrdenMedStr;
	}



	/**
	 * @param consecutivoOrdenMedStr el consecutivoOrdenMedStr para asignar
	 */
	public void setConsecutivoOrdenMedStr(String consecutivoOrdenMedStr) {
		this.consecutivoOrdenMedStr = consecutivoOrdenMedStr;
		this.consecutivoOrdenMed = Integer.parseInt(consecutivoOrdenMedStr);
	}


	/**
	 * @return the numeroOrdenAmb
	 */
	public Long getNumeroOrdenAmb() {
		return numeroOrdenAmb;
	}



	/**
	 * @param numeroOrdenAmb the numeroOrdenAmb to set
	 */
	public void setNumeroOrdenAmb(Long numeroOrdenAmb) {
		this.numeroOrdenAmb = numeroOrdenAmb;
	}



	/**
	 * @return the fechaOrdenAmb
	 */
	public Date getFechaOrdenAmb() {
		return fechaOrdenAmb;
	}



	/**
	 * @param fechaOrdenAmb the fechaOrdenAmb to set
	 */
	public void setFechaOrdenAmb(Date fechaOrdenAmb) {
		this.fechaOrdenAmb = fechaOrdenAmb;
	}



	/**
	 * @return the numeroPeticion
	 */
	public Integer getNumeroPeticion() {
		return numeroPeticion;
	}



	/**
	 * @param numeroPeticion the numeroPeticion to set
	 */
	public void setNumeroPeticion(Integer numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}



	/**
	 * @return the fechaPeticion
	 */
	public Date getFechaPeticion() {
		return fechaPeticion;
	}



	/**
	 * @param fechaPeticion the fechaPeticion to set
	 */
	public void setFechaPeticion(Date fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}



	/**
	 * @return the diagnosticoOrdenAmb
	 */
	public String getDiagnosticoOrdenAmb() {
		return diagnosticoOrdenAmb;
	}



	/**
	 * @param diagnosticoOrdenAmb the diagnosticoOrdenAmb to set
	 */
	public void setDiagnosticoOrdenAmb(String diagnosticoOrdenAmb) {
		this.diagnosticoOrdenAmb = diagnosticoOrdenAmb;
	}



	/**
	 * @return the tipoCieDxOrdenAmb
	 */
	public Integer getTipoCieDxOrdenAmb() {
		return tipoCieDxOrdenAmb;
	}



	/**
	 * @param tipoCieDxOrdenAmb the tipoCieDxOrdenAmb to set
	 */
	public void setTipoCieDxOrdenAmb(Integer tipoCieDxOrdenAmb) {
		this.tipoCieDxOrdenAmb = tipoCieDxOrdenAmb;
	}



	/**
	 * @return the descripcionDiagnosticoOrdenAmb
	 */
	public String getDescripcionDiagnosticoOrdenAmb() {
		return descripcionDiagnosticoOrdenAmb;
	}



	/**
	 * @param descripcionDiagnosticoOrdenAmb the descripcionDiagnosticoOrdenAmb to set
	 */
	public void setDescripcionDiagnosticoOrdenAmb(
			String descripcionDiagnosticoOrdenAmb) {
		this.descripcionDiagnosticoOrdenAmb = descripcionDiagnosticoOrdenAmb;
	}



	/**
	 * @return the diagnosticoPeticion
	 */
	public String getDiagnosticoPeticion() {
		return diagnosticoPeticion;
	}



	/**
	 * @param diagnosticoPeticion the diagnosticoPeticion to set
	 */
	public void setDiagnosticoPeticion(String diagnosticoPeticion) {
		this.diagnosticoPeticion = diagnosticoPeticion;
	}



	/**
	 * @return the tipoCieDxPeticion
	 */
	public Integer getTipoCieDxPeticion() {
		return tipoCieDxPeticion;
	}



	/**
	 * @param tipoCieDxPeticion the tipoCieDxPeticion to set
	 */
	public void setTipoCieDxPeticion(Integer tipoCieDxPeticion) {
		this.tipoCieDxPeticion = tipoCieDxPeticion;
	}



	/**
	 * @return the descripcionDiagnosticoPeticion
	 */
	public String getDescripcionDiagnosticoPeticion() {
		return descripcionDiagnosticoPeticion;
	}



	/**
	 * @param descripcionDiagnosticoPeticion the descripcionDiagnosticoPeticion to set
	 */
	public void setDescripcionDiagnosticoPeticion(
			String descripcionDiagnosticoPeticion) {
		this.descripcionDiagnosticoPeticion = descripcionDiagnosticoPeticion;
	}



	/**
	 * @return the diagnosticoIngEst
	 */
	public String getDiagnosticoIngEst() {
		return diagnosticoIngEst;
	}



	/**
	 * @param diagnosticoIngEst the diagnosticoIngEst to set
	 */
	public void setDiagnosticoIngEst(String diagnosticoIngEst) {
		this.diagnosticoIngEst = diagnosticoIngEst;
	}



	/**
	 * @return the tipoCieDxIngEst
	 */
	public Integer getTipoCieDxIngEst() {
		return tipoCieDxIngEst;
	}



	/**
	 * @param tipoCieDxIngEst the tipoCieDxIngEst to set
	 */
	public void setTipoCieDxIngEst(Integer tipoCieDxIngEst) {
		this.tipoCieDxIngEst = tipoCieDxIngEst;
	}



	/**
	 * @return the descripcionDiagnosticoIngEst
	 */
	public String getDescripcionDiagnosticoIngEst() {
		return descripcionDiagnosticoIngEst;
	}



	/**
	 * @param descripcionDiagnosticoIngEst the descripcionDiagnosticoIngEst to set
	 */
	public void setDescripcionDiagnosticoIngEst(String descripcionDiagnosticoIngEst) {
		this.descripcionDiagnosticoIngEst = descripcionDiagnosticoIngEst;
	}


	/**
	 * @return valor de numeroServicio
	 */
	public Integer getNumeroServicio() {
		return numeroServicio;
	}



	/**
	 * @param numeroServicio el numeroServicio para asignar
	 */
	public void setNumeroServicio(Integer numeroServicio) {
		this.numeroServicio = numeroServicio;
	}



	/**
	 * @return the codigoCentroCostoSolicitado
	 */
	public int getCodigoCentroCostoSolicitado() {
		return codigoCentroCostoSolicitado;
	}



	/**
	 * @param codigoCentroCostoSolicitado the codigoCentroCostoSolicitado to set
	 */
	public void setCodigoCentroCostoSolicitado(int codigoCentroCostoSolicitado) {
		this.codigoCentroCostoSolicitado = codigoCentroCostoSolicitado;
	}



	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}



	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}



	/**
	 * @return the consecutivoOrdenAmb
	 */
	public String getConsecutivoOrdenAmb() {
		return consecutivoOrdenAmb;
	}



	/**
	 * @param consecutivoOrdenAmb the consecutivoOrdenAmb to set
	 */
	public void setConsecutivoOrdenAmb(String consecutivoOrdenAmb) {
		this.consecutivoOrdenAmb = consecutivoOrdenAmb;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}



	public String getObservaciones() {
		return observaciones;
	}



	public void setContratoConvenioResponsable(Integer contratoConvenioResponsable) {
		this.contratoConvenioResponsable = contratoConvenioResponsable;
	}



	public Integer getContratoConvenioResponsable() {
		return contratoConvenioResponsable;
	}



	public void setCodigoTipoMonto(Integer codigoTipoMonto) {
		this.codigoTipoMonto = codigoTipoMonto;
	}



	public Integer getCodigoTipoMonto() {
		return codigoTipoMonto;
	}



	/**
	 * @return the tipoAutorizacion
	 */
	public long getTipoAutorizacion() {
		return tipoAutorizacion;
	}



	/**
	 * @param tipoAutorizacion the tipoAutorizacion to set
	 */
	public void setTipoAutorizacion(long tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}



	/**
	 * @return the codigoOrdenSolPet
	 */
	public Long getCodigoOrdenSolPet() {
		return codigoOrdenSolPet;
	}



	/**
	 * @param codigoOrdenSolPet the codigoOrdenSolPet to set
	 */
	public void setCodigoOrdenSolPet(Long codigoOrdenSolPet) {
		this.codigoOrdenSolPet = codigoOrdenSolPet;
	}



	/**
	 * @return the especialidad
	 */
	public int getEspecialidad() {
		return especialidad;
	}



	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}



	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}



	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}



	/**
	 * @return the grupoServicio
	 */
	public int getGrupoServicio() {
		return grupoServicio;
	}



	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(int grupoServicio) {
		this.grupoServicio = grupoServicio;
	}



	/**
	 * @return the idIngreso
	 */
	public long getIdIngreso() {
		return idIngreso;
	}



	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(long idIngreso) {
		this.idIngreso = idIngreso;
	}



	/**
	 * @return the codigoCuenta
	 */
	public long getCodigoCuenta() {
		return codigoCuenta;
	}



	/**
	 * @param codigoCuenta the codigoCuenta to set
	 */
	public void setCodigoCuenta(long codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}



	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}



	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}



	/**
	 * @return the descripcionNivelAtencion
	 */
	public String getDescripcionNivelAtencion() {
		return descripcionNivelAtencion;
	}



	/**
	 * @param descripcionNivelAtencion the descripcionNivelAtencion to set
	 */
	public void setDescripcionNivelAtencion(String descripcionNivelAtencion) {
		this.descripcionNivelAtencion = descripcionNivelAtencion;
	}



	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}



	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}



	/**
	 * @return the pyp
	 */
	public boolean isPyp() {
		return pyp;
	}



	/**
	 * @param pyp the pyp to set
	 */
	public void setPyp(boolean pyp) {
		this.pyp = pyp;
	}



	public int getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}



	public void setCodigoGrupoServicio(int codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}



	public String getNombreGrupoServicio() {
		return nombreGrupoServicio;
	}



	public void setNombreGrupoServicio(String nombreGrupoServicio) {
		this.nombreGrupoServicio = nombreGrupoServicio;
	}

	
}
