package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

import util.UtilidadFecha;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;

/**
 * Dto con los datos de la entrega de la autorizacion
 * @author hermorhu
 * @created 20-feb-2013
 */
public class AutorizacionEntregaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String fechaEntrega;
	private String horaEntrega;
	private DtoUsuarioPersona usuarioEntrega;
	private String personaRecibe;
	private String observaciones;
	private long idAutorizacionEntidadSub;
	
	/**
	 * 
	 */
	public AutorizacionEntregaDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Constructor utilizado para la consulta de datos de la entrega de la autorizacion original
	 * @param id
	 * @param fechaEntrega
	 * @param horaEntrega
	 * @param usuarioEntrega
	 * @param personaRecibe
	 * @param observaciones
	 */
	public AutorizacionEntregaDto(int id, Date fechaEntrega,
			String horaEntrega, String primerApellidoUsuarioEntrega,
			String segundoApellidoUsuarioEntrega, String primerNombreUsuarioEntrega,
			String segundoNombreUsuarioEntrega, String personaRecibe,
			String observaciones) {
		super();
		this.id = id;
		this.fechaEntrega = UtilidadFecha.conversionFormatoFechaAAp(fechaEntrega);
		this.horaEntrega = horaEntrega;

		this.usuarioEntrega = new DtoUsuarioPersona();
		this.usuarioEntrega.setNombreOrganizado(primerNombreUsuarioEntrega+" "+segundoNombreUsuarioEntrega+" "+primerApellidoUsuarioEntrega+" "+segundoApellidoUsuarioEntrega);
		
		this.personaRecibe = personaRecibe;
		this.observaciones = observaciones;
	}


	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the fechaEntrega
	 */
	public String getFechaEntrega() {
		return fechaEntrega;
	}
	
	/**
	 * @param fechaEntrega the fechaEntrega to set
	 */
	public void setFechaEntrega(String fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
	/**
	 * @return the horaEntrega
	 */
	public String getHoraEntrega() {
		return horaEntrega;
	}
	
	/**
	 * @param horaEntrega the horaEntrega to set
	 */
	public void setHoraEntrega(String horaEntrega) {
		this.horaEntrega = horaEntrega;
	}
	
	/**
	 * @return the usuarioEntrega
	 */
	public DtoUsuarioPersona getUsuarioEntrega() {
		return usuarioEntrega;
	}
	
	/**
	 * @param usuarioEntrega the usuarioEntrega to set
	 */
	public void setUsuarioEntrega(DtoUsuarioPersona usuarioEntrega) {
		this.usuarioEntrega = usuarioEntrega;
	}
	
	/**
	 * @return the personaRecibe
	 */
	public String getPersonaRecibe() {
		return personaRecibe;
	}
	
	/**
	 * @param personaRecibe the personaRecibe to set
	 */
	public void setPersonaRecibe(String personaRecibe) {
		this.personaRecibe = personaRecibe;
	}
	
	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}
	
	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the idAutorizacionEntidadSub
	 */
	public long getIdAutorizacionEntidadSub() {
		return idAutorizacionEntidadSub;
	}

	/**
	 * @param idAutorizacionEntidadSub the idAutorizacionEntidadSub to set
	 */
	public void setIdAutorizacionEntidadSub(long idAutorizacionEntidadSub) {
		this.idAutorizacionEntidadSub = idAutorizacionEntidadSub;
	}
	
}
