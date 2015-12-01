package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class InfoTarifaServicioPresupuesto implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8110215294039804536L;

	/**
	 * 
	 */
	private int servicio;
	
	/**
	 * 
	 */
	private int estadoFacturacion;
	
	/**
	 * 
	 */
	private String error;
	
	/**
	 * (SIN RESTA DE LOS DESCUENTOS)
	 */
	private BigDecimal valorTarifaUnitaria;

	/**
	 * 
	 */
	private BigDecimal valorDescuentoComercial;
	
	/**
	 * 
	 */
	private BigDecimal valorDescuentoPromocionUnitario;
	
	/**
	 * 
	 */
	private BigDecimal valorDescuentoBonoUnitario;
	
	/**
	 * 
	 */
	private double porcentajeDescuentoPromocionUnitario;
	
	/**
	 * 
	 */
	private double porcentajeDecuentoBonoUnitario;
	
	/**
	 * 
	 */
	private String metodoAjuste;
	
	/**
	 * 
	 */
	private BigDecimal valorHonorarioPromocion;
	
	/**
	 * 
	 */
	private double porcentajeHonorarioPromocion;
	
	/**
	 * Ojo este dato debemos revisar si se carga,
	 * debido al cambio en documentacion 
	 */
	private double porcentajeDctoOdontologicoCALCULADO;
	
	/**
	 * 
	 */
	private int detallePaqueteOdonConvenio;
	
	/**
	 * 
	 */
	private int esquemaTarifarioPaquete;
	
	
	/**
	 * Código del esquema tarifario asociado
	 */
	private int esquemaTarifario;
	
	
	/**
	 * 
	 * @param errores
	 * @param valorTarifaUnitaria
	 */
	public InfoTarifaServicioPresupuesto()
	{
		this.error = "";
		this.valorTarifaUnitaria = new BigDecimal(0);
		this.valorDescuentoComercial = new BigDecimal(0);
		this.servicio= ConstantesBD.codigoNuncaValido;
		this.estadoFacturacion= ConstantesBD.codigoNuncaValido;
		this.valorDescuentoPromocionUnitario= new BigDecimal(0);
		this.valorDescuentoBonoUnitario= new BigDecimal(0);
		this.porcentajeDecuentoBonoUnitario= 0.0;
		this.porcentajeDescuentoPromocionUnitario=0.0;
		this.metodoAjuste="";
		this.valorHonorarioPromocion= BigDecimal.ZERO;
		this.porcentajeHonorarioPromocion=0;
		this.porcentajeDctoOdontologicoCALCULADO= 0;
		this.detallePaqueteOdonConvenio=0;
		this.esquemaTarifarioPaquete=0;
		
		this.esquemaTarifario = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the valorTarifa
	 */
	public BigDecimal getValorTarifaUnitaria()
	{
		if(!UtilidadTexto.isEmpty(this.error))
		{	
			return BigDecimal.ZERO;
		}	
		return valorTarifaUnitaria;
	}
	

	/**
	 * @return the valorTarifa
	 */
	public BigDecimal getValorTarifaUnitariaConDctoComercial()
	{
		if(!UtilidadTexto.isEmpty(this.error))
			return new BigDecimal(0);
		return valorTarifaUnitaria.subtract(this.getValorDescuentoComercial());
	}

	/**
	 * @return the valorTarifa
	 */
	public String getValorTarifaUnitariaConDctoComercialFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorTarifaUnitariaConDctoComercial()+"");
	}
	
	/**
	 * @param valorTarifa the valorTarifa to set
	 */
	public void setValorTarifaUnitaria(BigDecimal valorTarifa)
	{
		this.valorTarifaUnitaria = valorTarifa;
	}

	/**
	 * @return the servicio
	 */
	public int getServicio()
	{
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(int servicio)
	{
		this.servicio = servicio;
	}

	/**
	 * @return the valorDescuentoComercial
	 */
	public BigDecimal getValorDescuentoComercial()
	{
		if(!UtilidadTexto.isEmpty(this.error))
		{	
			return BigDecimal.ZERO;
		}	
		return valorDescuentoComercial;
	}

	/**
	 * @param valorDescuentoComercial the valorDescuentoComercial to set
	 */
	public void setValorDescuentoComercial(BigDecimal valorDescuentoComercial)
	{
		this.valorDescuentoComercial = valorDescuentoComercial;
	}

	/**
	 * @return the estadoFacturacion
	 */
	public int getEstadoFacturacion()
	{
		return estadoFacturacion;
	}

	/**
	 * @param estadoFacturacion the estadoFacturacion to set
	 */
	public void setEstadoFacturacion(int estadoFacturacion)
	{
		this.estadoFacturacion = estadoFacturacion;
	}

	/**
	 * @return the error
	 */
	public String getError()
	{
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error)
	{
		this.error = error;
	}

	/**
	 * @return the valorDescuentoPromocionUnitario
	 */
	public BigDecimal getValorDescuentoPromocionUnitario()
	{
		return valorDescuentoPromocionUnitario;
	}

	/**
	 * @return the valorDescuentoBonoUnitario
	 */
	public BigDecimal getValorDescuentoBonoUnitario()
	{
		return valorDescuentoBonoUnitario;
	}

	/**
	 * @return the porcentajeDescuentoPromocionUnitario
	 */
	public double getPorcentajeDescuentoPromocionUnitario()
	{
		return porcentajeDescuentoPromocionUnitario;
	}

	/**
	 * @return the porcentajeDecuentoBonoUnitario
	 */
	public double getPorcentajeDecuentoBonoUnitario()
	{
		return porcentajeDecuentoBonoUnitario;
	}

	/**
	 * @param valorDescuentoPromocionUnitario the valorDescuentoPromocionUnitario to set
	 */
	public void setValorDescuentoPromocionUnitario(
			BigDecimal valorDescuentoPromocionUnitario)
	{
		this.valorDescuentoPromocionUnitario = valorDescuentoPromocionUnitario;
	}

	/**
	 * @param valorDescuentoBonoUnitario the valorDescuentoBonoUnitario to set
	 */
	public void setValorDescuentoBonoUnitario(BigDecimal valorDescuentoBonoUnitario)
	{
		this.valorDescuentoBonoUnitario = valorDescuentoBonoUnitario;
	}

	/**
	 * @param porcentajeDescuentoPromocionUnitario the porcentajeDescuentoPromocionUnitario to set
	 */
	public void setPorcentajeDescuentoPromocionUnitario(
			double porcentajeDescuentoPromocionUnitario)
	{
		this.porcentajeDescuentoPromocionUnitario = porcentajeDescuentoPromocionUnitario;
	}

	/**
	 * @param porcentajeDecuentoBonoUnitario the porcentajeDecuentoBonoUnitario to set
	 */
	public void setPorcentajeDecuentoBonoUnitario(
			double porcentajeDecuentoBonoUnitario)
	{
		this.porcentajeDecuentoBonoUnitario = porcentajeDecuentoBonoUnitario;
	}

	/**
	 * @return the metodoAjuste
	 */
	public String getMetodoAjuste()
	{
		return metodoAjuste;
	}

	/**
	 * @param metodoAjuste the metodoAjuste to set
	 */
	public void setMetodoAjuste(String metodoAjuste)
	{
		this.metodoAjuste = metodoAjuste;
	}

	/**
	 * @return the valorHonorarioPromocion
	 */
	public BigDecimal getValorHonorarioPromocion()
	{
		return valorHonorarioPromocion;
	}

	/**
	 * @return the porcentajeHonorarioPromocion
	 */
	public double getPorcentajeHonorarioPromocion()
	{
		return porcentajeHonorarioPromocion;
	}

	/**
	 * @param valorHonorarioPromocion the valorHonorarioPromocion to set
	 */
	public void setValorHonorarioPromocion(BigDecimal valorHonorarioPromocion)
	{
		this.valorHonorarioPromocion = valorHonorarioPromocion;
	}

	/**
	 * @param porcentajeHonorarioPromocion the porcentajeHonorarioPromocion to set
	 */
	public void setPorcentajeHonorarioPromocion(double porcentajeHonorarioPromocion)
	{
		this.porcentajeHonorarioPromocion = porcentajeHonorarioPromocion;
	}

	public BigDecimal getValorTarifaTotalConDctos()
	{
		Log4JManager.info(
				"Valor total = "+this.getValorTarifaUnitaria()+" - "+
				this.getValorDescuentoBonoUnitario()+" - "+
				this.getValorDescuentoPromocionUnitario()+" - "+
				this.getValorDescuentoComercial()+" - "+
				this.getValorDctoOdontologicoCALCULADO()+
				" = "+this.getValorTarifaUnitaria().subtract(this.getValorDescuentoBonoUnitario()).subtract(this.getValorDescuentoPromocionUnitario()).subtract(this.getValorDescuentoComercial()).subtract(this.getValorDctoOdontologicoCALCULADO())
		);
		
		return this.getValorTarifaUnitaria().subtract(this.getValorDescuentoBonoUnitario()).subtract(this.getValorDescuentoPromocionUnitario()).subtract(this.getValorDescuentoComercial()).subtract(this.getValorDctoOdontologicoCALCULADO());
	}
	
	public String getValorTarifaTotalConDctosFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorTarifaTotalConDctos()+"");
	}

	public BigDecimal getValorTarifaUnitariaConDctoBono()
	{
		return this.getValorTarifaUnitaria().subtract(this.getValorDescuentoBonoUnitario()).subtract(this.getValorDescuentoComercial());
	}
	
	public BigDecimal getValorTarifaUnitariaConDctoPromocion()
	{
		return this.getValorTarifaUnitaria().subtract(this.getValorDescuentoPromocionUnitario()).subtract(this.getValorDescuentoComercial());
	}
	
	/**
	 * @return the porcentajeDctoOdontologicoCALCULADO
	 */
	public double getPorcentajeDctoOdontologicoCALCULADO() {
		return porcentajeDctoOdontologicoCALCULADO;
	}

	/**
	 * @param porcentajeDctoOdontologicoCALCULADO the porcentajeDctoOdontologicoCALCULADO to set
	 */
	public void setPorcentajeDctoOdontologicoCALCULADO(
			double porcentajeDctoOdontologicoCALCULADO) {
		this.porcentajeDctoOdontologicoCALCULADO = porcentajeDctoOdontologicoCALCULADO;
	}
	
	/**
	 * @return the porcentajeDctoOdontologicoCALCULADO
	 */
	public BigDecimal getValorDctoOdontologicoCALCULADO() 
	{
		if(this.porcentajeDctoOdontologicoCALCULADO>0)
		{
			return this.getValorTarifaUnitaria().multiply( new BigDecimal(this.porcentajeDctoOdontologicoCALCULADO/100)); 
		}
		return BigDecimal.ZERO;
	}

	/**
	 * @return the detallePaqueteOdonConvenio
	 */
	public int getDetallePaqueteOdonConvenio() {
		return detallePaqueteOdonConvenio;
	}

	/**
	 * @param detallePaqueteOdonConvenio the detallePaqueteOdonConvenio to set
	 */
	public void setDetallePaqueteOdonConvenio(int detallePaqueteOdonConvenio) {
		this.detallePaqueteOdonConvenio = detallePaqueteOdonConvenio;
	}

	/**
	 * @return the esquemaTarifarioPaquete
	 */
	public int getEsquemaTarifarioPaquete() {
		return esquemaTarifarioPaquete;
	}

	/**
	 * @param esquemaTarifarioPaquete the esquemaTarifarioPaquete to set
	 */
	public void setEsquemaTarifarioPaquete(int esquemaTarifarioPaquete) {
		this.esquemaTarifarioPaquete = esquemaTarifarioPaquete;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(int esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public int getEsquemaTarifario() {
		return esquemaTarifario;
	}
}