/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.InfoDatosInt;
import util.InfoIntegridadDominio;

/**
 * 
 * @author Wilson Rios 
 *
 * May 7, 2010 - 10:39:15 AM
 */
public class DtoPresupuestoPlanTratamientoNumeroSuperficies implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5404424314609276293L;

	/**
	 * codigo pk
	 */
	private BigDecimal codigoPk;
	
	/**
	 * 
	 * Constructor de la clase
	 * @param presupuesto
	 * @param seccion
	 * @param piezaDental
	 * @param programa
	 * @param hallazgo
	 * @param loginUsuario
	 * @param color
	 */
	public DtoPresupuestoPlanTratamientoNumeroSuperficies(
			BigDecimal presupuesto,
			InfoIntegridadDominio seccion,
			InfoDatosInt piezaDental,
			InfoDatosInt programa,
			InfoDatosInt hallazgo,
			String loginUsuario,
			String color) {
		super();
		this.codigoPk = BigDecimal.ZERO;
		this.presupuesto = presupuesto;
		this.seccion = seccion;
		this.piezaDental = piezaDental;
		this.programa = programa;
		this.hallazgo = hallazgo;
		this.FHU = new DtoInfoFechaUsuario(loginUsuario);
		this.listaDetalleSuperficies = new ArrayList<DtoDetallePresupuestoPlanNumSuperficies>();
		this.color = color;
	}

	/**
	 * presupuesto
	 */
	private BigDecimal presupuesto;
	
	/**
	 * seccion
	 */
	private InfoIntegridadDominio seccion;
	
	/**
	 * pieza dental
	 */
	private InfoDatosInt piezaDental;
	
	/**
	 * programa
	 */
	private InfoDatosInt programa;
	
	/**
	 * hallazgo
	 */
	private InfoDatosInt hallazgo;
	
	/**
	 * fecha - hora y usuario
	 */
	private DtoInfoFechaUsuario FHU;
	
	/**
	 * detalles
	 */
	private ArrayList<DtoDetallePresupuestoPlanNumSuperficies> listaDetalleSuperficies;
	
	/**
	 * 
	 */
	private String color;
	
	/**
	 * Constructor de la clase
	 */
	public DtoPresupuestoPlanTratamientoNumeroSuperficies() 
	{
		this.codigoPk= BigDecimal.ZERO;
		this.presupuesto= BigDecimal.ZERO;
		this.seccion= new InfoIntegridadDominio();
		this.piezaDental= new InfoDatosInt();
		this.programa= new InfoDatosInt();
		this.hallazgo= new InfoDatosInt();
		this.FHU= new DtoInfoFechaUsuario();
		this.color="";
	}

	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the presupuesto
	 */
	public BigDecimal getPresupuesto() {
		return presupuesto;
	}

	/**
	 * @param presupuesto the presupuesto to set
	 */
	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}

	/**
	 * @return the seccion
	 */
	public InfoIntegridadDominio getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(InfoIntegridadDominio seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the piezaDental
	 */
	public InfoDatosInt getPiezaDental() {
		return piezaDental;
	}

	/**
	 * @param piezaDental the piezaDental to set
	 */
	public void setPiezaDental(InfoDatosInt piezaDental) {
		this.piezaDental = piezaDental;
	}

	/**
	 * @return the programa
	 */
	public InfoDatosInt getPrograma() {
		return programa;
	}

	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(InfoDatosInt programa) {
		this.programa = programa;
	}

	/**
	 * @return the hallazgo
	 */
	public InfoDatosInt getHallazgo() {
		return hallazgo;
	}

	/**
	 * @param hallazgo the hallazgo to set
	 */
	public void setHallazgo(InfoDatosInt hallazgo) {
		this.hallazgo = hallazgo;
	}

	/**
	 * @return the fHU
	 */
	public DtoInfoFechaUsuario getFHU() {
		return FHU;
	}

	/**
	 * @param fHU the fHU to set
	 */
	public void setFHU(DtoInfoFechaUsuario fHU) {
		FHU = fHU;
	}

	/**
	 * @return the listaDetalleSuperficies
	 */
	public ArrayList<DtoDetallePresupuestoPlanNumSuperficies> getListaDetalleSuperficies() {
		return listaDetalleSuperficies;
	}

	/**
	 * @param listaDetalleSuperficies the listaDetalleSuperficies to set
	 */
	public void setListaDetalleSuperficies(
			ArrayList<DtoDetallePresupuestoPlanNumSuperficies> listaDetalleSuperficies) {
		this.listaDetalleSuperficies = listaDetalleSuperficies;
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

	
}
