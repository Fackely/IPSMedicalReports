package util.odontologia;

import java.io.Serializable;

import util.InfoDatosInt;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * May 10, 2010 - 4:00:57 PM
 */
public class InfoNumSuperficiesPresupuesto implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -844891836075223366L;

	/**
	 * 
	 */
	private int codigoSuperficie;
	
	/**
	 * 
	 */
	private String nombreSuperficie;
	
	/**
	 * 
	 */
	private String color;
	
	/**
	 * 
	 */
	private boolean activo;
	
	/**
	 * 
	 */
	private boolean modificable;
	
	/**
	 * 
	 */
	private boolean marcarXDefecto;

	/**
	 * 
	 * Constructor de la clase
	 */
	public InfoNumSuperficiesPresupuesto() {
		super();
		this.codigoSuperficie = 0;
		this.nombreSuperficie = "";
		this.color = "";
		this.activo = false;
		this.modificable = false;
		this.marcarXDefecto= false;
	}

	public InfoNumSuperficiesPresupuesto(InfoDatosInt superficieOPCIONAL, boolean marcarXDefecto) 
	{
		this.codigoSuperficie = superficieOPCIONAL.getCodigo();
		this.nombreSuperficie = superficieOPCIONAL.getNombre();
		this.color = "";
		this.activo = superficieOPCIONAL.getActivo();
		this.modificable = true;
		if(marcarXDefecto)
		{
			this.modificable= false;
		}
		this.marcarXDefecto = marcarXDefecto;
		
	}

	/**
	 * @return the codigoSuperficie
	 */
	public int getCodigoSuperficie() {
		return codigoSuperficie;
	}

	/**
	 * @param codigoSuperficie the codigoSuperficie to set
	 */
	public void setCodigoSuperficie(int codigoSuperficie) {
		this.codigoSuperficie = codigoSuperficie;
	}

	/**
	 * @return the nombreSuperficie
	 */
	public String getNombreSuperficie() {
		return nombreSuperficie;
	}

	/**
	 * @param nombreSuperficie the nombreSuperficie to set
	 */
	public void setNombreSuperficie(String nombreSuperficie) {
		this.nombreSuperficie = nombreSuperficie;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @return the activo
	 */
	public boolean getActivo() {
		return activo;
	}
	
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the modificable
	 */
	public boolean isModificable() {
		return modificable;
	}

	/**
	 * @return the modificable
	 */
	public boolean getModificable() {
		return modificable;
	}
	
	/**
	 * @param modificable the modificable to set
	 */
	public void setModificable(boolean modificable) {
		this.modificable = modificable;
	}

	/**
	 * @return the marcarXDefecto
	 */
	public boolean isMarcarXDefecto() {
		return marcarXDefecto;
	}

	/**
	 * @param marcarXDefecto the marcarXDefecto to set
	 */
	public void setMarcarXDefecto(boolean marcarXDefecto) {
		this.marcarXDefecto = marcarXDefecto;
	}

	/**
	 * 
	 * Metodo para .......
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public InfoNumSuperficiesPresupuesto copiar()
	{
		InfoNumSuperficiesPresupuesto retorna= new InfoNumSuperficiesPresupuesto(); 
		retorna.setActivo(this.activo);
		retorna.setCodigoSuperficie(this.codigoSuperficie);
		retorna.setColor(this.color);
		retorna.setMarcarXDefecto(this.marcarXDefecto);
		retorna.setModificable(this.modificable);
		retorna.setNombreSuperficie(this.getNombreSuperficie());
		return retorna;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InfoNumSuperficiesPresupuesto [activo=" + activo
				+ ", codigoSuperficie=" + codigoSuperficie + ", color=" + color
				+ ", marcarXDefecto=" + marcarXDefecto + ", modificable="
				+ modificable + ", nombreSuperficie=" + nombreSuperficie + "]";
	}
	
	
}
