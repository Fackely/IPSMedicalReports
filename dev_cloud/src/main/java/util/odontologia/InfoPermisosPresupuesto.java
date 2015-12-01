package util.odontologia;

import java.io.Serializable;

/**
 * 
 * Clase para Definir los permisos que tiene un usuario con respecto a las actividades que con respecto al presupuesto
 * 
 * @author wilson Rios, Carvajal 
 *
 */
public class InfoPermisosPresupuesto implements Serializable
{
	/**
	 * Atributo para identificar si se puede modificar
	 */
	public boolean modificar;
	
	/**
	 * Atributo para saber si se puede Contratar contratar
	 */
	public boolean contratar;
	
	/**
	 * Atributo para saber si se puede Contratar suspender
	 */
	public boolean suspender;
	
	/**
	 * Atributo para saber si se puede Contratar reactivar
	 */
	public boolean reactivar;
	
	/**
	 * Atributo para saber si se puede Contratar Cancelar
	 */
	public boolean cancelar;
	
	/**
	 * Atributo para saber si se puede Contratar Copiar
	 */
	public boolean copiar;
	
	/**
	 * Atributo para saber si se puede Contratar Precontratar
	 */
	private boolean preContratar;

	/**
	 * Contrutor
	 * @param modificar
	 * @param contratar
	 * @param suspender
	 * @param reactivar
	 * @param cancelar
	 * @param copiar
	 */
	public InfoPermisosPresupuesto()
	{
		super();
		this.modificar = false;
		this.contratar = false;
		this.suspender = false;
		this.reactivar = false;
		this.cancelar = false;
		this.copiar = false;
		this.setPreContratar(Boolean.FALSE);
	}
	
	

	/**
	 * @return the modificar
	 */
	public boolean isModificar()
	{
		return modificar;
	}

	/**
	 * @return the contratar
	 */
	public boolean isContratar()
	{
		return contratar;
	}

	/**
	 * @return the suspender
	 */
	public boolean isSuspender()
	{
		return suspender;
	}

	/**
	 * @return the reactivar
	 */
	public boolean isReactivar()
	{
		return reactivar;
	}

	/**
	 * @return the cancelar
	 */
	public boolean isCancelar()
	{
		return cancelar;
	}

	/**
	 * @return the copiar
	 */
	public boolean isCopiar()
	{
		return copiar;
	}

	/**
	 * @param modificar the modificar to set
	 */
	public void setModificar(boolean modificar)
	{
		this.modificar = modificar;
	}

	/**
	 * @param contratar the contratar to set
	 */
	public void setContratar(boolean contratar)
	{
		this.contratar = contratar;
	}

	/**
	 * @param suspender the suspender to set
	 */
	public void setSuspender(boolean suspender)
	{
		this.suspender = suspender;
	}

	/**
	 * @param reactivar the reactivar to set
	 */
	public void setReactivar(boolean reactivar)
	{
		this.reactivar = reactivar;
	}

	/**
	 * @param cancelar the cancelar to set
	 */
	public void setCancelar(boolean cancelar)
	{
		this.cancelar = cancelar;
	}

	/**
	 * @param copiar the copiar to set
	 */
	public void setCopiar(boolean copiar)
	{
		this.copiar = copiar;
	}
	
	/**
	 * @return the modificar
	 */
	public boolean getModificar()
	{
		return modificar;
	}

	/**
	 * @return the contratar
	 */
	public boolean getContratar()
	{
		return contratar;
	}

	/**
	 * @return the suspender
	 */
	public boolean getSuspender()
	{
		return suspender;
	}

	/**
	 * @return the reactivar
	 */
	public boolean getReactivar()
	{
		return reactivar;
	}

	/**
	 * @return the cancelar
	 */
	public boolean getCancelar()
	{
		return cancelar;
	}

	/**
	 * @return the copiar
	 */
	public boolean getCopiar()
	{
		return copiar;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getExistePermiso()
	{
		return this.modificar || this.contratar ||	this.suspender || this.reactivar || this.cancelar || this.copiar;
	}



	public void setPreContratar(boolean preContratar) {
		this.preContratar = preContratar;
	}



	public boolean isPreContratar() {
		return preContratar;
	}
	
}
