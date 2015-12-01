package util.facturacion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class InfoTarifaVigente implements Serializable 
{
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private double valorTarifa;
	
	/**
	 * 
	 */
	private int tipoLiquidacion;
	
	/**
	 * 
	 */
	private boolean liquidarAsocios;

	/**
	 * 
	 */
	private boolean existe;
	
	/**
	 * 
	 */
	private String fechaVigencia;
	
	/**
	 * 
	 * @param codigo
	 * @param liquidarAsocios
	 * @param tipoLiquidacion
	 * @param valorTarifa
	 */
	public InfoTarifaVigente(int codigo, boolean liquidarAsocios,int tipoLiquidacion, double valorTarifa, String fechaVigencia) 
	{
		super();
		this.codigo = codigo;
		this.liquidarAsocios = liquidarAsocios;
		this.tipoLiquidacion = tipoLiquidacion;
		this.valorTarifa = valorTarifa;
		this.fechaVigencia= fechaVigencia;
		this.existe=true;
	}

	/**
	 * 
	 */
	public InfoTarifaVigente() 
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.liquidarAsocios = false;
		this.tipoLiquidacion = ConstantesBD.codigoNuncaValido;
		this.valorTarifa = 0;
		this.existe=false;
		this.fechaVigencia="";
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the valorTarifa
	 */
	public double getValorTarifa() {
		return valorTarifa;
	}

	/**
	 * @param valorTarifa the valorTarifa to set
	 */
	public void setValorTarifa(double valorTarifa) {
		this.valorTarifa = valorTarifa;
	}

	/**
	 * @return the tipoLiquidacion
	 */
	public int getTipoLiquidacion() {
		return tipoLiquidacion;
	}

	/**
	 * @param tipoLiquidacion the tipoLiquidacion to set
	 */
	public void setTipoLiquidacion(int tipoLiquidacion) {
		this.tipoLiquidacion = tipoLiquidacion;
	}

	/**
	 * @return the liquidarAsocios
	 */
	public boolean isLiquidarAsocios() {
		return liquidarAsocios;
	}

	/**
	 * @param liquidarAsocios the liquidarAsocios to set
	 */
	public void setLiquidarAsocios(boolean liquidarAsocios) {
		this.liquidarAsocios = liquidarAsocios;
	}

	/**
	 * @return the existe
	 */
	public boolean isExiste() {
		return existe;
	}

	/**
	 * @param existe the existe to set
	 */
	public void setExiste(boolean existe) {
		this.existe = existe;
	}

	/**
	 * @return the fechaVigencia
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * @param fechaVigencia the fechaVigencia to set
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}
	
}
