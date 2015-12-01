package util.facturacion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class InfoTarifaTarjetaCliente extends InfoTarifaVigente implements Serializable 
{
	/**
	 * 
	 */
	private int codigoServicio;

	/**
	 * 
	 */
	private int esquemaTarifario;
	
	
	/**
	 * 
	 */
	private int codigoContrato;
	

	/**
	 * 
	 */
	public InfoTarifaTarjetaCliente() 
	{
		super();
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
		this.esquemaTarifario=ConstantesBD.codigoNuncaValido;
		this.setCodigoContrato(ConstantesBD.codigoNuncaValido);
	}

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean existeServicio()
	{
		if(this.getCodigoServicio()>0)
			return true;
		return false;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public int getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(int esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	public boolean existeEsquemaTarifario() {
		
		return this.esquemaTarifario>0;
	}

	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public int getCodigoContrato() {
		return codigoContrato;
	}
	
	
	public boolean existeCodigoContrato(){
		if(this.getCodigoContrato()>0)
		{
			return true;
		}
		return false;
	}
	
}
