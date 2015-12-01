package com.princetonsa.dto.facturacion;

import java.io.Serializable;


/**
 * @author axioma
 */
public class DTOEstanciaViaIngCentroCosto implements Serializable 
{
    /** *  */
	private static final long serialVersionUID = 1L;
	private long codigoPk;
    private int viaIngreso;
    private String nombreViaIngreso;
    private int centroCosto;
    private String nombreCentroCosto;
    private long entidadSubcontratada;
    
    private boolean activo; 
    
    
    
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


	public DTOEstanciaViaIngCentroCosto(){
    	
    	this.setActivo(Boolean.TRUE);
    }
    
    
	/**
	 * @return Retorna el atributo codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}
	/**
	 * @param codigoPk Asigna el atributo codigoPk
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * @return Retorna el atributo viaIngreso
	 */
	public int getViaIngreso() {
		return viaIngreso;
	}
	/**
	 * @param viaIngreso Asigna el atributo viaIngreso
	 */
	public void setViaIngreso(int viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
	/**
	 * @return Retorna el atributo nombreViaIngreso
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}
	/**
	 * @param nombreViaIngreso Asigna el atributo nombreViaIngreso
	 */
	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}
	/**
	 * @return Retorna el atributo centroCosto
	 */
	public int getCentroCosto() {
		return centroCosto;
	}
	/**
	 * @param centroCosto Asigna el atributo centroCosto
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}
	/**
	 * @return Retorna el atributo nombreCentroCosto
	 */
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}
	/**
	 * @param nombreCentroCosto Asigna el atributo nombreCentroCosto
	 */
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}
	/**
	 * @return Retorna el atributo entidadSubcontratada
	 */
	public long getEntidadSubcontratada() {
		return entidadSubcontratada;
	}
	/**
	 * @param entidadSubcontratada Asigna el atributo entidadSubcontratada
	 */
	public void setEntidadSubcontratada(long entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (activo ? 1231 : 1237);
		result = prime * result + centroCosto;
		result = prime * result + (int) (codigoPk ^ (codigoPk >>> 32));
		result = prime * result
				+ (int) (entidadSubcontratada ^ (entidadSubcontratada >>> 32));
		result = prime
				* result
				+ ((nombreCentroCosto == null) ? 0 : nombreCentroCosto
						.hashCode());
		result = prime
				* result
				+ ((nombreViaIngreso == null) ? 0 : nombreViaIngreso.hashCode());
		result = prime * result + viaIngreso;
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEstanciaViaIngCentroCosto other = (DTOEstanciaViaIngCentroCosto) obj;
		if (activo != other.activo)
			return false;
		if (centroCosto != other.centroCosto)
			return false;
		if (codigoPk != other.codigoPk)
			return false;
		if (entidadSubcontratada != other.entidadSubcontratada)
			return false;
		if (nombreCentroCosto == null) {
			if (other.nombreCentroCosto != null)
				return false;
		} else if (!nombreCentroCosto.equals(other.nombreCentroCosto))
			return false;
		if (nombreViaIngreso == null) {
			if (other.nombreViaIngreso != null)
				return false;
		} else if (!nombreViaIngreso.equals(other.nombreViaIngreso))
			return false;
		if (viaIngreso != other.viaIngreso)
			return false;
		return true;
	}
    
    
    
    
    
    
   

	
	
}
