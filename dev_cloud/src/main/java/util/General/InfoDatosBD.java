package util.General;

import java.io.Serializable;


public class InfoDatosBD implements Serializable, Cloneable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	private boolean estaBD;
	private boolean esModificable;
	private boolean esEliminable;
	private boolean activo;
	
	private String value;
	private String id;
	
	public InfoDatosBD() 
	{	
		esModificable = true;
		esEliminable = true;
		activo = true;
		estaBD = false;
		value = "";
		id = "";
	}

	public boolean isEstaBD() {
		return estaBD;
	}

	public void setEstaBD(boolean estaBD) {
		this.estaBD = estaBD;
	}

	public boolean isEsModificable() {
		return esModificable;
	}

	public void setEsModificable(boolean esModificable) {
		this.esModificable = esModificable;
	}

	public boolean isEsEliminable() {
		return esEliminable;
	}

	public void setEsEliminable(boolean esEliminable) {
		this.esEliminable = esEliminable;
	}

	public boolean isActivo() {
		return activo;
	}
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}
