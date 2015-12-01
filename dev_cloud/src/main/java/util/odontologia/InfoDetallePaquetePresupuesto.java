/**
 * 
 */
package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.UtilidadTexto;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 11:51:59 AM
 */
public class InfoDetallePaquetePresupuesto implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4986015524345352890L;
	
	/**
	 * codigo pk de la tabla prog_paquete_odontolo
	 */
	private int codigoPkProgPaqueteOdontologico;
	
	/**
	 * 
	 */
	private BigDecimal codigoPkPrograma;
	
	/**
	 * 
	 */
	private String nombrePrograma;
	
	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * 
	 */
	private int codigoPkPaquete;
	
	/**
	 * 	
	 */
	private ArrayList<InfoServiciosProgramaPaquetePresupuesto> listaServicios;
	
	/**
	 * Constructor de la clase
	 */
	public InfoDetallePaquetePresupuesto() {
		this.codigoPkProgPaqueteOdontologico = 0;
		this.codigoPkPrograma = BigDecimal.ZERO;
		this.nombrePrograma = "";
		this.cantidad = 0;
		this.codigoPkPaquete= 0;
		this.listaServicios= new ArrayList<InfoServiciosProgramaPaquetePresupuesto>();
	}

	/**
	 * @return the codigoPkProgPaqueteOdontologico
	 */
	public int getCodigoPkProgPaqueteOdontologico() {
		return codigoPkProgPaqueteOdontologico;
	}

	/**
	 * @param codigoPkProgPaqueteOdontologico the codigoPkProgPaqueteOdontologico to set
	 */
	public void setCodigoPkProgPaqueteOdontologico(
			int codigoPkProgPaqueteOdontologico) {
		this.codigoPkProgPaqueteOdontologico = codigoPkProgPaqueteOdontologico;
	}

	/**
	 * @return the codigoPkPrograma
	 */
	public BigDecimal getCodigoPkPrograma() {
		return codigoPkPrograma;
	}

	/**
	 * @param codigoPkPrograma the codigoPkPrograma to set
	 */
	public void setCodigoPkPrograma(BigDecimal codigoPkPrograma) {
		this.codigoPkPrograma = codigoPkPrograma;
	}

	/**
	 * @return the nombrePrograma
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * @param nombrePrograma the nombrePrograma to set
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the valorUnitarioNetoPrograma
	 */
	public BigDecimal getValorUnitarioNetoPrograma() 
	{
		BigDecimal valorUnitarioNetoPrograma= BigDecimal.ZERO;
		for(InfoServiciosProgramaPaquetePresupuesto infoServicio: this.getListaServicios())
		{
			valorUnitarioNetoPrograma= valorUnitarioNetoPrograma.add(infoServicio.getValorTotalNeto()); 
		}
		return valorUnitarioNetoPrograma;
	}

	/**
	 * @param valorUnitarioNetoPrograma the valorUnitarioNetoPrograma to set
	 */
	public String getValorTotalNetoProgramaFormateado()
	{
		return UtilidadTexto.formatearValores(this.getValorTotalNetoPrograma().doubleValue());
	}
	
	/**
	 * @param valorUnitarioNetoPrograma the valorUnitarioNetoPrograma to set
	 */
	public BigDecimal getValorTotalNetoPrograma() {
		return this.getValorUnitarioNetoPrograma().multiply(new BigDecimal( this.getCantidad()));
	}

	/**
	 * @return the listaServicios
	 */
	public ArrayList<InfoServiciosProgramaPaquetePresupuesto> getListaServicios() {
		return listaServicios;
	}

	/**
	 * @param listaServicios the listaServicios to set
	 */
	public void setListaServicios(
			ArrayList<InfoServiciosProgramaPaquetePresupuesto> listaServicios) {
		this.listaServicios = listaServicios;
	}
	
	public String getErroresTarifa()
	{
		for(InfoServiciosProgramaPaquetePresupuesto servicio: this.getListaServicios())
		{
			if(!servicio.isExisteTarifa())
			{
				return "NO EXISTE TARIFA";
			}
		}
		return "";
	}

	/**
	 * @return the codigoPkPaquete
	 */
	public int getCodigoPkPaquete() {
		return codigoPkPaquete;
	}

	/**
	 * @param codigoPkPaquete the codigoPkPaquete to set
	 */
	public void setCodigoPkPaquete(int codigoPkPaquete) {
		this.codigoPkPaquete = codigoPkPaquete;
	}
}
