package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import org.axioma.util.log.Log4JManager;

import net.sf.jasperreports.engine.JRDataSource;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class DtoPresupuestosOdontologicosContratadosConPromocion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el c&oacute;digo del presupuesto
	 */
	private long codigoPkPresupuesto;
	
	/**
	 * Atributo que almacena el consecutivo del presupuesto
	 */
	private Long consecutivoPresupuesto;
	/**
	 * Atributo que almacena el codigopk de la promocion
	 */
	private long codigoPkPromocion;
	
	/**
	 * Atributo que almacena el codigo de la promocion
	 */
	private int codigoPromocion;
	/**
	 * atributo que almacena el nombre de la promocion 
	 */
	private String nombrePromocion;
	/**
	 * Atributo que almacena el estado del presupuesto odontol&oacute;gico.
	 */
	private String estadoPresupuesto;
	
	/**
	 * Atributo que almacena el nombre de la empresa
	 * instituci&oacute;n.
	 */
	private String descripcionEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la instituci&oacute;n a la que
	 * pertenece el centro de atenci&oacute;n.
	 */
	private long codigoInstitucion;
	
	/**
	 * Atributo que almacena el nombre de un centro de atenci&oacute;n
	 * que contrat&oacute;
	 */
	private String descCentroAtencionContrato;
	
	/**
	 * Atributo que almacena el consecutivo del centro de atenci&oacute;n
	 * que contrat&oacute;
	 */
	private int consCentroAtencionContrato;
	
	
	private String vigencia;
	
	/**
	 * Atributo que almacena el nombre de un pa&iacute;s
	 */
	private String descripcionPais;
	
	/**
	 * Atributo que almacena el nombre de una ciudad
	 */
	private String descripcionCiudad;
	
	/**
	 * atributo que almacena la descripci&oacute;n de 
	 * una regi&oacute;n de cobertura.
	 */
	private String descripcionRegionCobertura;
	
	/**
	 * Atributo que almacena el consecutivo del centro 
	 */
	
	private int consCentroAtencionDuenio;
	
	/**
	 * Atributo que almacena el nombre de un centro de atenci&oacute;n
	 */
	private String descCentroAtencionDuenio;
	
	/**
	 * Atributo que almacena la fecha del contrato
	 */
	
	private Date fechaContrato;
	
	/**
	 * Atributo que almacena el n&uacute;mero de contrato del
	 * presupuesto contratado.
	 */
	private long numeroContrato;
	
	/**
	 * Atributo que almacena el c&oacute;digo de un paciente.
	 */
	private int codigoPaciente;

	/**
	 * Atributo que almacena el tipo de identificaci&oacute;n 
	 * de la persona.
	 */
	private String tipoId;
	
	/**
	 * Atributo que almacena el n&uacute;mero de identificaci&oacute;n 
	 * de la persona.
	 */ 
	private String numeroId;
	
	/**
	 * Atributo que almacena el primer nombre de la persona.
	 */
	private String primerNombre;
	
	/**
	 * Atributo que almacena el segundo nombre de la persona.
	 */
	private String segundoNombre;
	
	/**
	 * Atributo que almacena el primer apellido de la persona.
	 */
	private String primerApellido;
	
	/**
	 * Atributo que almacena el segundo apellido de la persona.
	 */
	private String segundoApellido;
	
	/**
	 * Atributo que almacena el nombre completo del
	 * paciente.
	 */
	private String nombreCompletoPaciente;
	
	/**
	 * Atributo que almacena el login del profesional 
	 * que contrat&oacute;
	 */
	private String loginProfesionalContrato;

	/**
	 * Atributo que almacena el primer nombre del
	 * profesional que contrat&oacute;.
	 */
	private String primerNombreProfesionalContrato;
	
	/**
	 * Atributo que almacena el segundo nombre del
	 * profesional que contrat&oacute;
	 */
	private String segundoNombreProfesionalContrato;
	
	/**
	 * Atributo que almacena el primer apellido del
	 * profesional que contrat&oacute;
	 */
	private String primerApellidoProfesionalContrato;
	
	/**
	 * Atributo que almacena el segundo apellido del
	 * profesional que contrat&oacute;
	 */
	private String segundoApellidoProfesionalContrato;
	
	/**
	 * Atributo que almacena el nombre completo del
	 * profesional que contrat&oacute;
	 */
	private String nombreCompletoProfesionalContrato;
		
	/**
	 * Atributo que almacena el codigo pk del programa
	 */
	
	private long codigoPkPrograma;
	
	/**
	 * Atributo que almacena el cdigo del programa
	 */
	private String codigoPrograma;
	
	private String nombrePrograma;
	/**
	 * Atributo que almacena el valor del presupuesto contratado.
	 */
	private double valorPresupuesto;
	/**
	 * 
	 */
	private double valDiscPromo;
	/**
	 * 
	 */
	private double totalPromociones;
	/**
	 * 
	 */
	private int consecutivoCA;
	/**
	 * Atributo que almacena el tipo y número de identificación
	 * de un paciente. 
	 */
	private String ayudanteNumeroId;
	
	 /**
     * permite obtener el nombre del estado del presupuesto.
     */
    private String ayudanteEstadoPresupuesto;
    
    /**
     * 
     */
   private String valorFormateado;
   /**
    * 
    */
   private String valorFormateadopromo;
	
   	/**
	 * Atributo que almacena la fecha del inicio de la promocion
	 */
	private Date fechaInicioPromocion;
	/**
	 * Atributo que almacena la fecha del finalizacion de la promocion
	 */
	private Date fechaFinalizaPromocion;
   
   private String nombreInstitucion;
	
   /***Solo para plano***/
   private String fechaInicial;
   private String fechaFinal;
     /** Objeto jasper para el reporte en plano */
   transient private JRDataSource dsListadoResultadoPlano;
   private int totalEstadoContratado;
   
   public Date getFechaInicioPromocion() {
	return fechaInicioPromocion;
}

public void setFechaInicioPromocion(Date fechaInicioPromocion) {
	this.fechaInicioPromocion = fechaInicioPromocion;
}

public Date getFechaFinalizaPromocion() {
	return fechaFinalizaPromocion;
}

public void setFechaFinalizaPromocion(Date fechaFinalizaPromocion) {
	this.fechaFinalizaPromocion = fechaFinalizaPromocion;
}

private String descPaisOriginalPlano;
   /***Solo para plano***/
	
   /**
	 * Constructor del DtoPresupuestosOdontologicosContratados
	 */   
	
	public DtoPresupuestosOdontologicosContratadosConPromocion(){

		this.codigoPkPresupuesto=ConstantesBD.codigoNuncaValidoLong;
		this.consecutivoPresupuesto=ConstantesBD.codigoNuncaValidoLong;
		this.estadoPresupuesto="";
		this.descripcionEmpresaInstitucion="";
		this.setCodigoInstitucion(ConstantesBD.codigoNuncaValido);
		this.descCentroAtencionContrato="";
		this.consCentroAtencionContrato=ConstantesBD.codigoNuncaValido;
		this.descripcionPais="";
		this.descripcionCiudad="";
		this.descripcionRegionCobertura="";
		this.consCentroAtencionDuenio=ConstantesBD.codigoNuncaValido;
		this.descCentroAtencionDuenio="";
		this.fechaContrato=new Date();
		this.numeroContrato=ConstantesBD.codigoNuncaValidoLong;
		this.codigoPaciente=ConstantesBD.codigoNuncaValido;
		this.tipoId="";
		this.numeroId="";
		this.primerNombre="";
		this.segundoNombre="";
		this.primerApellido="";
		this.segundoApellido="";
		this.nombreCompletoPaciente="";
		this.loginProfesionalContrato="";
		this.primerNombreProfesionalContrato="";
		this.segundoNombreProfesionalContrato="";
		this.primerApellidoProfesionalContrato="";
		this.segundoApellidoProfesionalContrato="";
		this.nombreCompletoProfesionalContrato="";
		this.codigoPkPrograma=ConstantesBD.codigoNuncaValidoLong;
		this.codigoPrograma="";
		this.nombrePrograma="";
		this.codigoPromocion=ConstantesBD.codigoNuncaValido;
		this.codigoPkPromocion=ConstantesBD.codigoNuncaValido;
		this.valorPresupuesto= ConstantesBD.codigoNuncaValidoDouble;
		this.valDiscPromo=ConstantesBD.codigoNuncaValido;
		this.consecutivoCA=ConstantesBD.codigoNuncaValido;
		this.ayudanteNumeroId="";
		this.valorFormateado= "";
		this.valorFormateadopromo="";
		this.nombreInstitucion="";
		this.fechaInicioPromocion=new Date();
		this.fechaFinalizaPromocion=new Date();
		/***Solo para Plano***/
		this.setFechaInicial("");
		this.setFechaFinal("");
		this.setTotalEstadoContratado(ConstantesBD.codigoNuncaValido);
		this.descPaisOriginalPlano="";
		/***Solo para Plano***/
	}

public long getCodigoPkPresupuesto() {
	return codigoPkPresupuesto;
}

public void setCodigoPkPresupuesto(long codigoPkPresupuesto) {
	this.codigoPkPresupuesto = codigoPkPresupuesto;
}

public Long getConsecutivoPresupuesto() {
	return consecutivoPresupuesto;
}

public void setConsecutivoPresupuesto(Long consecutivoPresupuesto) {
	this.consecutivoPresupuesto = consecutivoPresupuesto;
}

public long getCodigoPkPromocion() {
	return codigoPkPromocion;
}

public void setCodigoPkPromocion(long codigoPkPromocion) {
	this.codigoPkPromocion = codigoPkPromocion;
}



public int getCodigoPromocion() {
	return codigoPromocion;
}

public void setCodigoPromocion(int codigoPromocion) {
	this.codigoPromocion = codigoPromocion;
}

public String getNombrePromocion() {
	return nombrePromocion;
}

public void setNombrePromocion(String nombrePromocion) {
	this.nombrePromocion = nombrePromocion;
}

public String getEstadoPresupuesto() {
	return estadoPresupuesto;
}

public void setEstadoPresupuesto(String estadoPresupuesto) {this.estadoPresupuesto = estadoPresupuesto;
try{
	if (!UtilidadTexto.isEmpty(estadoPresupuesto)) {
		this.setAyudanteEstadoPresupuesto(ValoresPorDefecto.getIntegridadDominio(estadoPresupuesto).toString());
		//this.setEstadoPresupuesto(ValoresPorDefecto.getIntegridadDominio(estadoPresupuesto).toString());
	}}
	catch (Exception e) {
		Log4JManager.error(e);
	} 
}

public String getDescripcionEmpresaInstitucion() {
	return descripcionEmpresaInstitucion;
}

public void setDescripcionEmpresaInstitucion(
		String descripcionEmpresaInstitucion) {
	this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
}

public long getCodigoInstitucion() {
	return codigoInstitucion;
}

public void setCodigoInstitucion(long codigoInstitucion) {
	this.codigoInstitucion = codigoInstitucion;
}

public String getDescCentroAtencionContrato() {
	return descCentroAtencionContrato;
}

public void setDescCentroAtencionContrato(String descCentroAtencionContrato) {
	this.descCentroAtencionContrato = descCentroAtencionContrato;
}

public int getConsCentroAtencionContrato() {
	return consCentroAtencionContrato;
}

public void setConsCentroAtencionContrato(int consCentroAtencionContrato) {
	this.consCentroAtencionContrato = consCentroAtencionContrato;
}

public String getVigencia() {
	return vigencia;
}

public void setVigencia(String vigencia) {
	this.vigencia = vigencia;
}

public String getDescripcionPais() {
	return descripcionPais;
}

public void setDescripcionPais(String descripcionPais) {
	this.descripcionPais = descripcionPais;
}

public String getDescripcionCiudad() {
	return descripcionCiudad;
}

public void setDescripcionCiudad(String descripcionCiudad) {
	this.descripcionCiudad = descripcionCiudad;
}

public String getDescripcionRegionCobertura() {
	return descripcionRegionCobertura;
}

public void setDescripcionRegionCobertura(String descripcionRegionCobertura) {
	this.descripcionRegionCobertura = descripcionRegionCobertura;
}

public int getConsCentroAtencionDuenio() {
	return consCentroAtencionDuenio;
}

public void setConsCentroAtencionDuenio(int consCentroAtencionDuenio) {
	this.consCentroAtencionDuenio = consCentroAtencionDuenio;
}

public String getDescCentroAtencionDuenio() {
	return descCentroAtencionDuenio;
}

public void setDescCentroAtencionDuenio(String descCentroAtencionDuenio) {
	this.descCentroAtencionDuenio = descCentroAtencionDuenio;
}

public Date getFechaContrato() {
	return fechaContrato;
}

public void setFechaContrato(Date fechaContrato) {
	this.fechaContrato = fechaContrato;
}

public long getNumeroContrato() {
	return numeroContrato;
}

public void setNumeroContrato(long numeroContrato) {
	this.numeroContrato = numeroContrato;
}

public int getCodigoPaciente() {
	return codigoPaciente;
}

public void setCodigoPaciente(int codigoPaciente) {
	this.codigoPaciente = codigoPaciente;
}

public String getTipoId() {
	return tipoId;
}

public void setTipoId(String tipoId) {
	this.tipoId = tipoId;
}

public String getNumeroId() {
	return numeroId;
}

public void setNumeroId(String numeroId) {
	this.numeroId = numeroId;
}

public String getPrimerNombre() {
	return primerNombre;
}

public void setPrimerNombre(String primerNombre) {
	this.primerNombre = primerNombre;
}

public String getSegundoNombre() {
	return segundoNombre;
}

public void setSegundoNombre(String segundoNombre) {
	this.segundoNombre = segundoNombre;
}

public String getPrimerApellido() {
	return primerApellido;
}

public void setPrimerApellido(String primerApellido) {
	this.primerApellido = primerApellido;
}

public String getSegundoApellido() {
	return segundoApellido;
}

public void setSegundoApellido(String segundoApellido) {
	this.segundoApellido = segundoApellido;
}

public String getNombreCompletoPaciente() {
	
	if (segundoNombre.equals(null)) {
		segundoNombre = "";
	}
	if (segundoApellido.equals(null)) {
		segundoApellido = "";
	}
	
	this.nombreCompletoPaciente = primerNombre + " " + segundoNombre + " " +
		primerApellido + " " + segundoApellido; 
	return nombreCompletoPaciente;
}

public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
	this.nombreCompletoPaciente = nombreCompletoPaciente;
}

public String getLoginProfesionalContrato() {
	return loginProfesionalContrato;
}

public void setLoginProfesionalContrato(String loginProfesionalContrato) {
	this.loginProfesionalContrato = loginProfesionalContrato;
}

public String getPrimerNombreProfesionalContrato() {
	return primerNombreProfesionalContrato;
}

public void setPrimerNombreProfesionalContrato(
		String primerNombreProfesionalContrato) {
	this.primerNombreProfesionalContrato = primerNombreProfesionalContrato;
}

public String getSegundoNombreProfesionalContrato() {
	return segundoNombreProfesionalContrato;
}

public void setSegundoNombreProfesionalContrato(
		String segundoNombreProfesionalContrato) {
	this.segundoNombreProfesionalContrato = segundoNombreProfesionalContrato;
}

public String getPrimerApellidoProfesionalContrato() {
	return primerApellidoProfesionalContrato;
}

public void setPrimerApellidoProfesionalContrato(
		String primerApellidoProfesionalContrato) {
	this.primerApellidoProfesionalContrato = primerApellidoProfesionalContrato;
}

public String getSegundoApellidoProfesionalContrato() {
	return segundoApellidoProfesionalContrato;
}

public void setSegundoApellidoProfesionalContrato(
		String segundoApellidoProfesionalContrato) {
	this.segundoApellidoProfesionalContrato = segundoApellidoProfesionalContrato;
}

public String getNombreCompletoProfesionalContrato() {
	return nombreCompletoProfesionalContrato;
}

public void setNombreCompletoProfesionalContrato(
		String nombreCompletoProfesionalContrato) {
	this.nombreCompletoProfesionalContrato = nombreCompletoProfesionalContrato;
}

public long getCodigoPkPrograma() {
	return codigoPkPrograma;
}

public void setCodigoPkPrograma(long codigoPkPrograma) {
	this.codigoPkPrograma = codigoPkPrograma;
}

public String getCodigoPrograma() {
	return codigoPrograma;
}

public void setCodigoPrograma(String codigoPrograma) {
	this.codigoPrograma = codigoPrograma;
}

public double getValorPresupuesto() {
	return valorPresupuesto;
}

public void setValorPresupuesto(double valorPresupuesto) {
	this.valorPresupuesto = valorPresupuesto;
}

public double getValDiscPromo() {
	return valDiscPromo;
}

public void setValDiscPromo(double valDiscPromo) {
	this.valDiscPromo = valDiscPromo;
}

public int getConsecutivoCA() {
	return consecutivoCA;
}

public void setConsecutivoCA(int consecutivoCA) {
	this.consecutivoCA = consecutivoCA;
}

public String getAyudanteNumeroId() {
	this.ayudanteNumeroId = tipoId + " " + numeroId;
	return ayudanteNumeroId;
}

public void setAyudanteNumeroId(String ayudanteNumeroId) {
	this.ayudanteNumeroId = ayudanteNumeroId;
}

public String getAyudanteEstadoPresupuesto() {
	return ayudanteEstadoPresupuesto;
}

public void setAyudanteEstadoPresupuesto(String ayudanteEstadoPresupuesto) {
	this.ayudanteEstadoPresupuesto = ayudanteEstadoPresupuesto;
}

public String getValorFormateado() {
	
	this.valorFormateado=UtilidadTexto.formatearValores(valorPresupuesto);
	
	return valorFormateado;
}

public void setValorFormateado(String valorFormateado) {
	this.valorFormateado = valorFormateado;
}

public String getValorFormateadopromo() {
	
	this.valorFormateadopromo=UtilidadTexto.formatearValores(valDiscPromo);
	
	return valorFormateadopromo;
}

public void setValorFormateadopromo(String valorFormateadopromo) {
	this.valorFormateadopromo = valorFormateadopromo;
}

public String getNombreInstitucion() {
	return nombreInstitucion;
}

public void setNombreInstitucion(String nombreInstitucion) {
	this.nombreInstitucion = nombreInstitucion;
}

public String getFechaInicial() {
	return fechaInicial;
}

public void setFechaInicial(String fechaInicial) {
	this.fechaInicial = fechaInicial;
}

public String getFechaFinal() {
	return fechaFinal;
}

public void setFechaFinal(String fechaFinal) {
	this.fechaFinal = fechaFinal;
}

public JRDataSource getDsListadoResultadoPlano() {
	return dsListadoResultadoPlano;
}

public void setDsListadoResultadoPlano(JRDataSource dsListadoResultadoPlano) {
	this.dsListadoResultadoPlano = dsListadoResultadoPlano;
}

public int getTotalEstadoContratado() {
	return totalEstadoContratado;
}

public void setTotalEstadoContratado(int totalEstadoContratado) {
	this.totalEstadoContratado = totalEstadoContratado;
}

public String getDescPaisOriginalPlano() {
	return descPaisOriginalPlano;
}

public void setDescPaisOriginalPlano(String descPaisOriginalPlano) {
	this.descPaisOriginalPlano = descPaisOriginalPlano;
}

public void setNombrePrograma(String nombrePrograma) {
	this.nombrePrograma = nombrePrograma;
}

public String getNombrePrograma() {
	return nombrePrograma;
}

public void setTotalPromociones(double totalPromociones) {
	this.totalPromociones = totalPromociones;
}

public double getTotalPromociones() {
	return totalPromociones;
}
   
}
