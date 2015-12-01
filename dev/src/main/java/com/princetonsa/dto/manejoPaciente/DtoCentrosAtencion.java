package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ValoresPorDefecto;

/**
 * Dto para el manejo de centros de atenci&oacute;n, m&oacute;dulo tesorer&iacute;a
 * @author Yennifer Guerrero
 * @since 7 Jul 2010
 *
 */
public class DtoCentrosAtencion implements Serializable{
	
	
	/**
	 * Atributo que almacena el n&uacute;mero consecutivo correspondiente a un centro de atenci&oacute;n.
	 */
	private int  consecutivo;        
	
	/**
	 * Atributo que almacena el c&oacute;digo de un determinado centro de atenci&oacute;n. 
	 */
	private String codigo ;            
	
	/**
	 * Atributo que almacena la descripci&oacute;n de un centro de atenci&oacute;n. 
	 */
	private String descripcion ;      
	
	/**
	 * Atributo que almacena un valor que indica si el centro de atenci&oacute;n se encuentra activo. 
	 */
	private boolean activo;             
	
	/**
	 * Atributo que almacena el c&oacute;digo de la instituci&oacute;n. 
	 */
	private int codInstitucion ;    
	
	/**
	 * 
	 */
	private int  codupgd ;            
	/**
	 * 
	 */
	private String codigoInstSirc  ; 
	/**
	 * 
	 */
	private double empresaInstitucion ; 
	/**
	 * 
	 */
	private String direccion  ;         
	/**
	 * 
	 */
	private String  pais ;
	/**
	 * 
	 */
	private String nombrePais;
	/**
	 * 
	 */
	private String  departamento ;     
	/**
	 * 
	 */
	private String  ciudad ;      
	/**
	 * 
	 */
	private String  telefono  ;    
	/**
	 * 
	 */
	private double  regionCobertura ;    
	/**
	 * 
	 */
	private double categoriaAtencion ; 
	
	/**
	 * Descripci&oacute;n de la ciudad a la cual pertenece un determinado
	 * centro de atenci&oacute;
	 */
	private String descripcionCiudad;
	
	/**
	 * Descripci&oacute;n de la regi&oacute;n a la cual pertenece un determinado
	 * centro de atenci&oacute;
	 */
	private String descripcionRegion;
	
	/**
	 * Atributo que almacena la descripci&oacute;n de la empresa instituci&oacute;n
	 */
	private String descripcionEmpresaInstitucion;
	
	/**
	 * Atributo que indica si la instituci&oacute;n asociada
	 * al centro de atenci&oacute;n es multiempresa o no.
	 */
	private String esMultiEmpresa;

	
	public DtoCentrosAtencion(){
		reset();
	}
	
	
	
	public DtoCentrosAtencion(int consecutivo, String descripcion,String descripcionCiudad, String descripcionRegion, boolean activo) {
		super();
		reset();
		this.consecutivo = consecutivo;
		this.descripcion = descripcion;
		this.activo = activo;
		this.descripcionCiudad = descripcionCiudad;
		this.descripcionRegion = descripcionRegion;
	}



	/**
	 * 
	 */
	void reset(){
			
			this.consecutivo=0;        
			this.codigo="";             
			this.descripcion="";       
			//boolean activo=              
			this.codInstitucion=0;     
			this.codupgd =0;            
			this.codigoInstSirc="";   
			this.empresaInstitucion=0; 
			this.direccion="";           
			this.pais ="";                
			this.departamento="";       
			this.ciudad="";              
			this.telefono="";           
			this.regionCobertura=0;    
			this.categoriaAtencion =0;
			this.nombrePais="";
	}

	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
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
		
		esMultiEmpresa = (ValoresPorDefecto.getInstitucionMultiempresa(this.codInstitucion));
	}

	/**
	 * @return the codupgd
	 */
	public int getCodupgd() {
		return codupgd;
	}

	/**
	 * @param codupgd the codupgd to set
	 */
	public void setCodupgd(int codupgd) {
		this.codupgd = codupgd;
	}

	/**
	 * @return the codigoInstSirc
	 */
	public String getCodigoInstSirc() {
		return codigoInstSirc;
	}

	/**
	 * @param codigoInstSirc the codigoInstSirc to set
	 */
	public void setCodigoInstSirc(String codigoInstSirc) {
		this.codigoInstSirc = codigoInstSirc;
	}

	/**
	 * @return the empresaInstitucion
	 */
	public double getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	/**
	 * @param empresaInstitucion the empresaInstitucion to set
	 */
	public void setEmpresaInstitucion(double empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the departamento
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the regionCobertura
	 */
	public double getRegionCobertura() {
		return regionCobertura;
	}

	/**
	 * @param regionCobertura the regionCobertura to set
	 */
	public void setRegionCobertura(double regionCobertura) {
		this.regionCobertura = regionCobertura;
	}

	/**
	 * @return the categoriaAtencion
	 */
	public double getCategoriaAtencion() {
		return categoriaAtencion;
	}

	/**
	 * @param categoriaAtencion the categoriaAtencion to set
	 */
	public void setCategoriaAtencion(double categoriaAtencion) {
		this.categoriaAtencion = categoriaAtencion;
	}

	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	public String getNombrePais() {
		return nombrePais;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionCiudad
	 * 
	 * @return  Retorna la variable descripcionCiudad
	 */
	public String getDescripcionCiudad() {
		return descripcionCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionCiudad
	 * 
	 * @param  valor para el atributo descripcionCiudad 
	 */
	public void setDescripcionCiudad(String descripcionCiudad) {
		this.descripcionCiudad = descripcionCiudad;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionRegion
	 * 
	 * @return  Retorna la variable descripcionRegion
	 */
	public String getDescripcionRegion() {
		return descripcionRegion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionRegion
	 * 
	 * @param  valor para el atributo descripcionRegion 
	 */
	public void setDescripcionRegion(String descripcionRegion) {
		this.descripcionRegion = descripcionRegion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo descripcionEmpresaInstitucion
	 * 
	 * @return  Retorna la variable descripcionEmpresaInstitucion
	 */
	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo descripcionEmpresaInstitucion
	 * 
	 * @param  valor para el atributo descripcionEmpresaInstitucion 
	 */
	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo esMultiEmpresa
	 * 
	 * @return  Retorna la variable esMultiEmpresa
	 */
	public String getEsMultiEmpresa() {
		return esMultiEmpresa;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo esMultiEmpresa
	 * 
	 * @param  valor para el atributo esMultiEmpresa 
	 */
	public void setEsMultiEmpresa(String esMultiEmpresa) {
		this.esMultiEmpresa = esMultiEmpresa;
	}
	
	
	
}
