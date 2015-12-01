package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * DTO Deudor. Este DTO hace referencia a  los deudores del sistema
 * @author Víctor Gómez L.
 *
 */
public class DtoDeudor implements Serializable
{
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String codigoPaciente;
	
	private String tipoIdentificacionPac;
	
	private String numeroIdentificacionPac;
	
	private String nombreCompletoPac;

	private String primerApellidoPac;
	
	private String primerNombrePac;
	
	private String tipoIdentificacionDeu;
	
	private String numeroIdentificacionDeu;

	private String primerApellidoDeu;
	
	private String segundoApellidoDeu;
	
	private String primerNombreDeu;
	
	private String segundoNombreDeu;
	
	private int codInstitucion;
	
	private int ingreso;
	
	private String claseDeudor; 
	
	private int  codigoTercero;
	
	private String telefono;
	
	private String direccion;
	
	private int codigoPkDeudor;
	
	
	// atributo de validacion 
	private String rompimiento;
	
	//Elementos Agregados para realziar la búsqueda en el anexo 766 
	private int centroAtencion;
	
	private DtoDocumentosGarantia dtoDocsGarantia;
	
	private  String factura;
	
	private String nombreCentroAtencion;
	

	public DtoDeudor()
	{
		this.reset();
	}

	
	public void reset()
	{
		this.codigoPaciente = "";
		this.tipoIdentificacionPac = "";
		this.numeroIdentificacionPac = "";
		this.nombreCompletoPac = "";
		this.primerApellidoPac = "";
		this.primerNombrePac = "";
		this.tipoIdentificacionDeu = "";
		this.numeroIdentificacionDeu = "";
		this.primerApellidoDeu = "";
		this.segundoApellidoDeu = "";
		this.primerNombreDeu = "";
		this.segundoNombreDeu = "";
		this.codInstitucion = ConstantesBD.codigoNuncaValido;
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.claseDeudor = "";
		this.codigoTercero=ConstantesBD.codigoNuncaValido;
		this.telefono="";
		this.direccion="";
		
		// atributo de validacion 
		this.rompimiento = ConstantesBD.acronimoNo;
		

		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.dtoDocsGarantia= new DtoDocumentosGarantia();
		this.factura="";
		this.nombreCentroAtencion="";
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the tipoIdentificacionPac
	 */
	public String getTipoIdentificacionPac() {
		return tipoIdentificacionPac;
	}

	/**
	 * @param tipoIdentificacionPac the tipoIdentificacionPac to set
	 */
	public void setTipoIdentificacionPac(String tipoIdentificacionPac) {
		this.tipoIdentificacionPac = tipoIdentificacionPac;
	}

	/**
	 * @return the numeroIdentificacionPac
	 */
	public String getNumeroIdentificacionPac() {
		return numeroIdentificacionPac;
	}

	/**
	 * @param numeroIdentificacionPac the numeroIdentificacionPac to set
	 */
	public void setNumeroIdentificacionPac(String numeroIdentificacionPac) {
		this.numeroIdentificacionPac = numeroIdentificacionPac;
	}

	/**
	 * @return the tipoIdentificacionDeu
	 */
	public String getTipoIdentificacionDeu() {
		return tipoIdentificacionDeu;
	}

	/**
	 * @param tipoIdentificacionDeu the tipoIdentificacionDeu to set
	 */
	public void setTipoIdentificacionDeu(String tipoIdentificacionDeu) {
		this.tipoIdentificacionDeu = tipoIdentificacionDeu;
	}

	/**
	 * @return the numeroIdentificacionDeu
	 */
	public String getNumeroIdentificacionDeu() {
		return numeroIdentificacionDeu;
	}

	/**
	 * @param numeroIdentificacionDeu the numeroIdentificacionDeu to set
	 */
	public void setNumeroIdentificacionDeu(String numeroIdentificacionDeu) {
		this.numeroIdentificacionDeu = numeroIdentificacionDeu;
	}

	/**
	 * @return the primerApellidoDeu
	 */
	public String getPrimerApellidoDeu() {
		return primerApellidoDeu;
	}

	/**
	 * @param primerApellidoDeu the primerApellidoDeu to set
	 */
	public void setPrimerApellidoDeu(String primerApellidoDeu) {
		this.primerApellidoDeu = primerApellidoDeu;
	}

	/**
	 * @return the segundoApellidoDeu
	 */
	public String getSegundoApellidoDeu() {
		return segundoApellidoDeu;
	}

	/**
	 * @param segundoApellidoDeu the segundoApellidoDeu to set
	 */
	public void setSegundoApellidoDeu(String segundoApellidoDeu) {
		this.segundoApellidoDeu = segundoApellidoDeu;
	}

	/**
	 * @return the primerNombreDeu
	 */
	public String getPrimerNombreDeu() {
		return primerNombreDeu;
	}

	/**
	 * @param primerNombreDeu the primerNombreDeu to set
	 */
	public void setPrimerNombreDeu(String primerNombreDeu) {
		this.primerNombreDeu = primerNombreDeu;
	}

	/**
	 * @return the segundoNombreDeu
	 */
	public String getSegundoNombreDeu() {
		return segundoNombreDeu;
	}

	/**
	 * @param segundoNombreDeu the segundoNombreDeu to set
	 */
	public void setSegundoNombreDeu(String segundoNombreDeu) {
		this.segundoNombreDeu = segundoNombreDeu;
	}

	/**
	 * @return the nombreCompletoPac
	 */
	public String getNombreCompletoPac() {
		return nombreCompletoPac;
	}

	/**
	 * @param nombreCompletoPac the nombreCompletoPac to set
	 */
	public void setNombreCompletoPac(String nombreCompletoPac) {
		this.nombreCompletoPac = nombreCompletoPac;
	}

	/**
	 * @return the primerApellidoPac
	 */
	public String getPrimerApellidoPac() {
		return primerApellidoPac;
	}

	/**
	 * @param primerApellidoPac the primerApellidoPac to set
	 */
	public void setPrimerApellidoPac(String primerApellidoPac) {
		this.primerApellidoPac = primerApellidoPac;
	}

	/**
	 * @return the primerNombrePac
	 */
	public String getPrimerNombrePac() {
		return primerNombrePac;
	}

	/**
	 * @param primerNombrePac the primerNombrePac to set
	 */
	public void setPrimerNombrePac(String primerNombrePac) {
		this.primerNombrePac = primerNombrePac;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDatosPaciente()
	{
		if((!this.tipoIdentificacionPac.equals("") && !this.numeroIdentificacionPac.equals(""))
				|| !this.primerApellidoPac.equals("")
				|| !this.primerNombrePac.equals(""))
			return true;
		else
			return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isDatosDeudor()
	{
		if((!this.tipoIdentificacionDeu.equals("") && !this.numeroIdentificacionDeu.equals(""))
				|| !this.primerApellidoDeu.equals("")
				|| !this.primerNombreDeu.equals(""))
			return true;
		else
			return false;
	}

	/**
	 * @return the rompimiento
	 */
	public String getRompimiento() {
		return rompimiento;
	}

	/**
	 * @param rompimiento the rompimiento to set
	 */
	public void setRompimiento(String rompimiento) {
		this.rompimiento = rompimiento;
	}

	/**
	 * @return the codInstitucion
	 */
	public int getCodInstitucion() {
		return codInstitucion;
	}

	/**
	 * @param codInstitucion the codInstitucion to set
	 */
	public void setCodInstitucion(int codInstitucion) {
		this.codInstitucion = codInstitucion;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the claseDeudor
	 */
	public String getClaseDeudor() {
		return claseDeudor;
	}

	/**
	 * @param claseDeudor the claseDeudor to set
	 */
	public void setClaseDeudor(String claseDeudor) {
		this.claseDeudor = claseDeudor;
	}

	public void setCodigoTercero(int codigoTercero) {
		this.codigoTercero = codigoTercero;
	}

	public int getCodigoTercero() {
		return codigoTercero;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public DtoDocumentosGarantia getDtoDocsGarantia() {
		return dtoDocsGarantia;
	}

	public void setDtoDocsGarantia(DtoDocumentosGarantia dtoDocsGarantia) {
		this.dtoDocsGarantia = dtoDocsGarantia;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String factura) {
		this.factura = factura;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setCodigoPkDeudor(int codigoPkDeudor) {
		this.codigoPkDeudor = codigoPkDeudor;
	}

	public int getCodigoPkDeudor() {
		return codigoPkDeudor;
	}
	
	
	
	
}
