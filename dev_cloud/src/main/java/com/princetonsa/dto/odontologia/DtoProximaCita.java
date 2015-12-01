package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.odontologia.InfoServicios;

/**
 * 
 * @author axioma
 *
 */
public class DtoProximaCita implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * Listado con los detalles de los programas y sus respectivos
	 * servicios
	 */
	private ArrayList<DtoDetalleProximaCita> listaDetalle;	
	
	/**
	 * Atributo que indica si la cita fue registrada o no.
	 */
	private boolean guardoProximaCita;
	
	/**
	 * Atributo que indica si se ha seleccionado al menos un servicio
	 * para asociar a la proxima cita.
	 */
	private boolean existenServiciosAsociados;
	
	
	/**
	 * 
	 * Constructor
	 */
	public DtoProximaCita() 
	{
		super();
		this.fecha = "";
		this.listaDetalle= new ArrayList<DtoDetalleProximaCita>();
		this.guardoProximaCita= false;
		this.existenServiciosAsociados = false;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<InfoServicios> getTodosServicios()
	{
		ArrayList<InfoServicios> servicios= new ArrayList<InfoServicios>();
		for(DtoDetalleProximaCita detalle: this.getListaDetalle())
		{
			for(InfoServicios serv:  detalle.getServicios())
			{
				if(serv.getServicio().getActivo())
				{	
					
					servicios.add(serv);
				}	
			}
		}
		
		setExistenServiciosAsociados(servicios.size() > 0 ? true : false);
		
		return servicios;
	}
	
	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the listaDetalle
	 */
	public ArrayList<DtoDetalleProximaCita> getListaDetalle() {
		return listaDetalle;
	}
	
	/**
	 * Devuelve un listado con los detalles que fueron involucrados en la 
	 * programación de la próxima cita.
	 * 
	 * @return the listaDetalle
	 */
	public ArrayList<DtoDetalleProximaCita> getDetallesAsociadosCita() {
		
		ArrayList<DtoDetalleProximaCita> detallesAsociadosCita= new ArrayList<DtoDetalleProximaCita>();
		
		for(DtoDetalleProximaCita detalle: this.getListaDetalle())
		{
			for(InfoServicios serv:  detalle.getServicios())
			{
				if(serv.getServicio().getActivo())
				{	
					detallesAsociadosCita.add(detalle);
					break;
				}	
			}
		}
		
		return detallesAsociadosCita;
	}

	/**
	 * @param listaDetalle the listaDetalle to set
	 */
	public void setListaDetalle(ArrayList<DtoDetalleProximaCita> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}

	/**
	 * @return the guardoProximaCita
	 */
	public boolean isGuardoProximaCita() {
		return guardoProximaCita;
	}

	/**
	 * @return the guardoProximaCita
	 */
	public boolean getGuardoProximaCita() {
		return guardoProximaCita;
	}
	
	/**
	 * @param guardoProximaCita the guardoProximaCita to set
	 */
	public void setGuardoProximaCita(boolean guardoProximaCita) {
		this.guardoProximaCita = guardoProximaCita;
	}

	/**
	 * @param existenServiciosAsociados the existenServiciosAsociados to set
	 */
	public void setExistenServiciosAsociados(boolean existenServiciosAsociados) {
		this.existenServiciosAsociados = existenServiciosAsociados;
	}

	/**
	 * @return the existenServiciosAsociados
	 */
	public boolean isExistenServiciosAsociados() {
		return existenServiciosAsociados;
	}
}