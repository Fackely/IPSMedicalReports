package com.princetonsa.dto.inventario;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;

/**
 * Encaps&uacute;la la respuesta a la informaci&oacute;n Entrega/Autorización de medicamentos e insumos
 * 
 * @author Cristhian Murillo
 */
public class DtoAutorizacionEntSubcontratadasCapitacion extends DTOAutorEntidadSubcontratadaCapitacion implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	
	/*--------------------------*/
	/* ATRIBUTOS DTO			*/
	/*--------------------------*/
	
	//Autorizacion
	
	private String estado2;
	private Boolean urgente;	
	private String primerNombrePersonaRecibe;
	private String primerApellidoPersonaRecibe;
	
	//	Auto-IngresoEstancia
	private int diasAutorizados;
	private Date fechaInicioAutorizacion;
	
	//	Auto-EntidadesSubcontratadas
	
	//	Auto-CapitacionSubcontratada
	private int indicadorPrioridad;
		
	// Orden / Solicitud
	private Integer numeroOrden;
	private Integer consecutivoOrden;
	private Date fechaOrden;
	private String primerNombreProfesionalOrdena;
	private String primerApellidoProfesionalOrdena;
	private String diagnosticoOrden; 
	private Integer numCuenta;
	private int camaCama;
	private String camaHabitacion;
	private String camaPiso;
	private boolean entregaDirectaPaciente;
	private boolean entregaParcial;
	private boolean entregaTotal;
	private int codigoPaciente;
	private Integer codigoCentroCostoSolicitado;
	private String nombreCentroCostoSolicitado;

	
	
	// Otros Atributos
	/**
	 * Tiene un fin muy genérico, puede ser utilizado para determinar condiciones de búsqueda, asignación o validaciones.
	 * Valores Actuales:
	 * 	- ConstantesIntegridadDominio.acronimoArticulo = "ART"; (Usado como parámetro de búsqueda en BD).
	 */
	private String valorGenerico;  
	/** * Utilizada para validar si en la lista de Articulos existen Medicamentos (esto es de acuerdo a la naturaleza del articulo) */
	boolean existenArticulosMedicamentos;
	/** * Utilizada para validar si en la lista de Articulos existen Insumos (esto es de acuerdo a la naturaleza del articulo)  */
	boolean existenArticulosInsumos;
	/** * Código de la autorización de Ingreso Estancia en caso de tenerla  */
	private Long autorizacionIngresoEstancia;
	/** * Código del despacho asociado  */
	private Integer numeroDespacho;
	
	
	
	
	/**
	 * Constructor
	 */
	public DtoAutorizacionEntSubcontratadasCapitacion()
	{		
		super();
		this.estado2							= "";		
		this.urgente							= false;		
		this.primerNombrePersonaRecibe			= "";
		this.primerApellidoPersonaRecibe		= "";
		
		//	Auto-IngresoEstancia
		this.diasAutorizados					= ConstantesBD.codigoNuncaValido;
		this.fechaInicioAutorizacion			= null;
		
		//	Auto-EntidadesSubcontratadas
		
		//	Auto-CapitacionSubcontratada
		this.indicadorPrioridad					= ConstantesBD.codigoNuncaValido;
		
		// Orden / Solicitud
		this.numeroOrden						= null;
		this.consecutivoOrden					= null;
		this.fechaOrden							= null;
		this.primerNombreProfesionalOrdena		= "";
		this.primerApellidoProfesionalOrdena	= "";
		this.diagnosticoOrden					= ""; 
		this.numCuenta							= null;
		this.camaCama							= ConstantesBD.codigoNuncaValido;
		this.camaHabitacion						= "";
		this.camaPiso							= "";
		this.entregaDirectaPaciente				= false;
		this.entregaParcial						= false;
		this.entregaTotal						= false;
		this.codigoPaciente						= ConstantesBD.codigoNuncaValido;
		this.codigoCentroCostoSolicitado		= null;
		this.nombreCentroCostoSolicitado		= "";
	
		// Otros Atributos
		this.valorGenerico						= "";  
		this.existenArticulosMedicamentos		= false;
		this.existenArticulosInsumos			= false;
		this.autorizacionIngresoEstancia		= null;
		this.numeroDespacho						= null;
	}


	public Boolean getUrgente() {
		return urgente;
	}


	public void setUrgente(Boolean urgente) {
		this.urgente = urgente;
	}


	public String getLoginUsuarioAutoriza() {
		if(this.getUsuarioAutoriza()!=null){
			return this.getUsuarioAutoriza().getLogin();
		}		
		return "";
	}


	public void setLoginUsuarioAutoriza(String loginUsuarioAutoriza) {
		if(this.getUsuarioAutoriza()==null){
			this.setUsuarioAutoriza(new DtoUsuarioPersona());			
		}
		this.getUsuarioAutoriza().setLogin(loginUsuarioAutoriza);
	}


	public String getPrimerNombreUsuarioAutoriza() {
		
		if(this.getUsuarioAutoriza()!=null){
			return this.getUsuarioAutoriza().getNombre();
		}		
		return "";
	}


	public void setPrimerNombreUsuarioAutoriza(String primerNombreUsuarioAutoriza) {
		
		if(this.getUsuarioAutoriza()==null){
			this.setUsuarioAutoriza(new DtoUsuarioPersona());			
		}
		
		this.getUsuarioAutoriza().setNombre(primerNombreUsuarioAutoriza);
	}


	public String getPrimerApellidoUsuarioAutoriza() {
		if(this.getUsuarioAutoriza()!=null){
			return this.getUsuarioAutoriza().getApellido();
		}		
		return "";		
	}


	public void setPrimerApellidoUsuarioAutoriza(
			String primerApellidoUsuarioAutoriza) {
		
		if(this.getUsuarioAutoriza()==null){
			this.setUsuarioAutoriza(new DtoUsuarioPersona());			
		}		
		this.getUsuarioAutoriza().setApellido(primerApellidoUsuarioAutoriza);
	}


	public int getDiasAutorizados() {
		return diasAutorizados;
	}


	public void setDiasAutorizados(int diasAutorizados) {
		this.diasAutorizados = diasAutorizados;
	}


	public Date getFechaInicioAutorizacion() {
		return fechaInicioAutorizacion;
	}


	public void setFechaInicioAutorizacion(Date fechaInicioAutorizacion) {
		this.fechaInicioAutorizacion = fechaInicioAutorizacion;
	}


	public int getIndicadorPrioridad() {
		return indicadorPrioridad;
	}


	public void setIndicadorPrioridad(int indicadorPrioridad) {
		this.indicadorPrioridad = indicadorPrioridad;
	}


	public long getCodigoEntidadSubcontratada() {		
		if(this.getDtoEntidadSubcontratada()!=null){
			return this.getDtoEntidadSubcontratada().getCodigoPk();
		}		
		return ConstantesBD.codigoNuncaValidoLong;
	}


	public void setCodigoEntidadSubcontratada(long codigoEntidadSubcontratada) {
		if(this.getDtoEntidadSubcontratada()==null){
			this.setDtoEntidadSubcontratada(new DtoEntidadSubcontratada());
		}
		
		this.getDtoEntidadSubcontratada().setCodigoPk(codigoEntidadSubcontratada);
	}


	public String getConsecutivoEntidadSubcontratada() {
		if(this.getDtoEntidadSubcontratada()!=null){
			return this.getDtoEntidadSubcontratada().getConsecutivo();
		}		
		return "";
	}


	public void setConsecutivoEntidadSubcontratada(
			String consecutivoEntidadSubcontratada) {
		if(this.getDtoEntidadSubcontratada()==null){
			this.setDtoEntidadSubcontratada(new DtoEntidadSubcontratada());
		}
		this.getDtoEntidadSubcontratada().setConsecutivo(consecutivoEntidadSubcontratada);
	}


	public String getRazonSocialEntidadSubcontratada() {
		if(this.getDtoEntidadSubcontratada()!=null){
			return this.getDtoEntidadSubcontratada().getRazonSocial();
		}		
		return "";
	}


	public void setRazonSocialEntidadSubcontratada(
			String razonSocialEntidadSubcontratada) {
		if(this.getDtoEntidadSubcontratada()==null){
			this.setDtoEntidadSubcontratada(new DtoEntidadSubcontratada());
		}
		this.getDtoEntidadSubcontratada().setRazonSocial(razonSocialEntidadSubcontratada);
	}


	public String getDireccionEntidadSubcontratada() {
		
		if(this.getDtoEntidadSubcontratada()!=null){
			return this.getDtoEntidadSubcontratada().getDireccion();
		}		
		return "";
	}


	public void setDireccionEntidadSubcontratada(
			String direccionEntidadSubcontratada) {
		if(this.getDtoEntidadSubcontratada()==null){
			this.setDtoEntidadSubcontratada(new DtoEntidadSubcontratada());
		}		
		this.getDtoEntidadSubcontratada().setDireccion(direccionEntidadSubcontratada);
	}


	public String getTelefonoEntidadSubcontratada() {
		if(this.getDtoEntidadSubcontratada()!=null){
			return this.getDtoEntidadSubcontratada().getTelefono();
		}		
		return "";
	}


	public void setTelefonoEntidadSubcontratada(String telefonoEntidadSubcontratada) {
		if(this.getDtoEntidadSubcontratada()==null){
			this.setDtoEntidadSubcontratada(new DtoEntidadSubcontratada());
		}		
		this.getDtoEntidadSubcontratada().setTelefono(telefonoEntidadSubcontratada);
	}


	public Integer getNumeroOrden() {
		return numeroOrden;
	}


	public void setNumeroOrden(Integer numeroOrden) {
		this.numeroOrden = numeroOrden;
	}


	public Date getFechaOrden() {
		return fechaOrden;
	}


	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}


	public String getDiagnosticoOrden() {
		return diagnosticoOrden;
	}


	public void setDiagnosticoOrden(String diagnosticoOrden) {
		this.diagnosticoOrden = diagnosticoOrden;
	}


	public Integer getNumCuenta() {
		return numCuenta;
	}


	public void setNumCuenta(Integer numCuenta) {
		this.numCuenta = numCuenta;
	}


	public int getCamaCama() {
		return camaCama;
	}


	public void setCamaCama(int camaCama) {
		this.camaCama = camaCama;
	}


	public String getCamaHabitacion() {
		return camaHabitacion;
	}


	public void setCamaHabitacion(String camaHabitacion) {
		this.camaHabitacion = camaHabitacion;
	}


	public String getCamaPiso() {
		return camaPiso;
	}


	public void setCamaPiso(String camaPiso) {
		this.camaPiso = camaPiso;
	}


	public boolean isEntregaDirectaPaciente() {
		return entregaDirectaPaciente;
	}


	public void setEntregaDirectaPaciente(boolean entregaDirectaPaciente) {
		this.entregaDirectaPaciente = entregaDirectaPaciente;
	}


	public boolean isEntregaParcial() {
		return entregaParcial;
	}


	public void setEntregaParcial(boolean entregaParcial) {
		this.entregaParcial = entregaParcial;
	}


	public boolean isEntregaTotal() {
		return entregaTotal;
	}


	public void setEntregaTotal(boolean entregaTotal) {
		this.entregaTotal = entregaTotal;
	}

	public String getPrimerNombreProfesionalOrdena() {
		return primerNombreProfesionalOrdena;
	}


	public void setPrimerNombreProfesionalOrdena(
			String primerNombreProfesionalOrdena) {
		this.primerNombreProfesionalOrdena = primerNombreProfesionalOrdena;
	}


	public String getPrimerApellidoProfesionalOrdena() {
		return primerApellidoProfesionalOrdena;
	}


	public void setPrimerApellidoProfesionalOrdena(
			String primerApellidoProfesionalOrdena) {
		this.primerApellidoProfesionalOrdena = primerApellidoProfesionalOrdena;
	}


	public String getPrimerNombrePersonaRecibe() {
		return primerNombrePersonaRecibe;
	}


	public void setPrimerNombrePersonaRecibe(String primerNombrePersonaRecibe) {
		this.primerNombrePersonaRecibe = primerNombrePersonaRecibe;
	}


	public String getPrimerApellidoPersonaRecibe() {
		return primerApellidoPersonaRecibe;
	}


	public void setPrimerApellidoPersonaRecibe(String primerApellidoPersonaRecibe) {
		this.primerApellidoPersonaRecibe = primerApellidoPersonaRecibe;
	}


	public int getCodigoPaciente() {
		return codigoPaciente;
	}


	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	public String getValorGenerico() {
		return valorGenerico;
	}


	public void setValorGenerico(String valorGenerico) {
		this.valorGenerico = valorGenerico;
	}


	public String getEstado2() {
		return estado2;
	}


	public void setEstado2(String estado2) {
		this.estado2 = estado2;
	}


	public boolean isExistenArticulosMedicamentos() {
		return existenArticulosMedicamentos;
	}


	public void setExistenArticulosMedicamentos(boolean existenArticulosMedicamentos) {
		this.existenArticulosMedicamentos = existenArticulosMedicamentos;
	}


	public boolean isExistenArticulosInsumos() {
		return existenArticulosInsumos;
	}


	public void setExistenArticulosInsumos(boolean existenArticulosInsumos) {
		this.existenArticulosInsumos = existenArticulosInsumos;
	}


	public Long getAutorizacionIngresoEstancia() {
		return autorizacionIngresoEstancia;
	}


	public void setAutorizacionIngresoEstancia(Long autorizacionIngresoEstancia) {
		this.autorizacionIngresoEstancia = autorizacionIngresoEstancia;
	}


	public Integer getConsecutivoOrden() {
		return consecutivoOrden;
	}


	public void setConsecutivoOrden(Integer consecutivoOrden) {
		this.consecutivoOrden = consecutivoOrden;
	}


	public Integer getCodigoCentroCostoSolicitado() {
		return codigoCentroCostoSolicitado;
	}


	public void setCodigoCentroCostoSolicitado(Integer codigoCentroCostoSolicitado) {
		this.codigoCentroCostoSolicitado = codigoCentroCostoSolicitado;
	}


	public String getNombreCentroCostoSolicitado() {
		return nombreCentroCostoSolicitado;
	}


	public void setNombreCentroCostoSolicitado(String nombreCentroCostoSolicitado) {
		this.nombreCentroCostoSolicitado = nombreCentroCostoSolicitado;
	}


	public Integer getNumeroDespacho() {
		return numeroDespacho;
	}


	public void setNumeroDespacho(Integer numeroDespacho) {
		this.numeroDespacho = numeroDespacho;
	}



	
}
