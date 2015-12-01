package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * 
 * @author axioma
 *
 */
public class DtoContactoEmpresa implements Serializable , Cloneable  {
	
	private Double codigo; 
	private int empresa;
	
	private String direccion;
	private String telefono;
	private String email;
	private String cargo;
	private String usuarioModifica;
	private String fechaModifica;
	private String horaModifica;
	private int institucion;
	private String nombre;
	
	
	
	public DtoContactoEmpresa(){
			clean();
				
	}

	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	           
	        }
	        return obj;
	    }
	

/**
 * 
 */

	private void clean() {
		this.codigo=ConstantesBD.codigoNuncaValidoDouble;
		this.empresa =ConstantesBD.codigoNuncaValido;;
		this.nombre="";
		this.direccion="";
		this.telefono="";
		this.email="";
		this.cargo = "";
		this.usuarioModifica="";
		this.fechaModifica="";
		this.horaModifica="";
		this.institucion= ConstantesBD.codigoNuncaValido;
	}
	
	
	
	
	
	/**
	 * 
	 * @param codigo
	 */
	public void setCodigo(Double codigo) {
		this.codigo = codigo;
	}
	public Double getCodigo() {
		return codigo;
	}
	
/**
 * 	
 * @param empresa
 */

	
	/**
	 * 
	 */
	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getEmpresa() {
		return empresa;
	}
	
	
	/**
	 * 
	 * @param direccion
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDireccion() {
		return direccion;
	}
	
    /**
     * 
     * @param telefono
     */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTelefono() {
		return telefono;
	}
	
	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}
	
	
	/**
	 * 
	 * @param cargo
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCargo() {
		return cargo;
	}
	
	/**
	 * 
	 * @param usuarioModifica
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}
	
	
	/**
	 * 
	 * @param fechaModifica
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getFechaModifica() {
		return UtilidadFecha.validarFecha(this.fechaModifica)?UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica):"";
	}
	
	/**
	 * 
	 * @param horaModifica
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	
	/**
	 * 
	 * @param institucion
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getInstitucion() {
		return institucion;
	}
	
	
	/**
	 * 
	 * @param nombre
	 */

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return
	 */

	public String getNombre() {
		return nombre;
	}
	
	

}
