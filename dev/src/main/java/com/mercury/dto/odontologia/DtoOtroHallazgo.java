package com.mercury.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;

/**
 * Manejo de los hallazgo almacenados en la secci&oacute;n otros
 * hallazgos de Tratamientos Odontol&oacute;gicos (Mercury) 
 * @author Juan David Ram&iacute;rez
 * @since 29 Mayo 2010
 */
public class DtoOtroHallazgo implements Serializable, Cloneable
{
	/**
	 * Versi&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Llave primaria del hallazgo encontrado
	 */
	private int codigoPk;

	/**
	 * Hallazgo encontrado
	 */
	private DtoHallazgoOdontologico hallazgo;

	/**
	 * Pieza dental en la que se encontr&oacute; el hallazgo
	 */
	private DtoPiezaDental pieza;
	
	/**
	 * Superficie en la que se encontr&oacute; el hallazgo
	 */
	private DtoSuperficie superficie;
	
	/**
	 * Fecha del hallazgo
	 */
	private String fecha;
	
	/**
	 * Hora del hallazgo
	 */
	private String hora;
	
	private String archivoConvencion;
	
	private String colorBorde;
	
	/**
	 * Listado de las superficies que aplican a la pieza dental seleccionada
	 */
	private ArrayList<DtoSectorSuperficieCuadrante> listaSuperficies=new ArrayList<DtoSectorSuperficieCuadrante>();
	
	public DtoOtroHallazgo()
	{
		codigoPk=0;
		hallazgo=null;
		pieza=null;
		superficie=null;
		fecha=null;
		hora=null;
		archivoConvencion=null;
		colorBorde=null;
	}
	
	public DtoOtroHallazgo clone() throws CloneNotSupportedException
	{
		DtoOtroHallazgo nuevo=(DtoOtroHallazgo) super.clone();
		nuevo.setSuperficie(this.getSuperficie().clone());
		nuevo.setPieza(this.getPieza().clone());
		return nuevo;
	}

	/**
	 * @return Retorna el atributo codigoPk
	 */
	public int getCodigoPk()
	{
		return codigoPk;
	}

	/**
	 * @param codigoPk Asigna el atributo codigoPk
	 */
	public void setCodigoPk(int codigoPk)
	{
		this.codigoPk = codigoPk;
	}

	/**
	 * @return Retorna el atributo hallazgo
	 */
	public DtoHallazgoOdontologico getHallazgo()
	{
		return hallazgo;
	}

	/**
	 * @param hallazgo Asigna el atributo hallazgo
	 */
	public void setHallazgo(DtoHallazgoOdontologico hallazgo)
	{
		this.hallazgo = hallazgo;
	}

	/**
	 * @return Retorna el atributo pieza
	 */
	public DtoPiezaDental getPieza()
	{
		return pieza;
	}

	/**
	 * @param pieza Asigna el atributo pieza
	 */
	public void setPieza(DtoPiezaDental pieza)
	{
		this.pieza = pieza;
	}

	/**
	 * @return Retorna el atributo superficie
	 */
	public DtoSuperficie getSuperficie()
	{
		return superficie;
	}

	/**
	 * @param superficie Asigna el atributo superficie
	 */
	public void setSuperficie(DtoSuperficie superficie)
	{
		this.superficie = superficie;
	}

	/**
	 * @return Retorna el atributo fecha
	 */
	public String getFecha()
	{
		return fecha;
	}

	/**
	 * @param fecha Asigna el atributo fecha
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	/**
	 * @return Retorna el atributo hora
	 */
	public String getHora()
	{
		return hora;
	}

	/**
	 * @param hora Asigna el atributo hora
	 */
	public void setHora(String hora)
	{
		this.hora = hora;
	}

	public ArrayList<DtoSectorSuperficieCuadrante> getListaSuperficies() {
		return listaSuperficies;
	}

	public void setListaSuperficies(ArrayList<DtoSectorSuperficieCuadrante> listaSuperficies) {
		this.listaSuperficies = listaSuperficies;
	}

	/**
	 * @return the archivoConvencion
	 */
	public String getArchivoConvencion() {
		return archivoConvencion;
	}

	/**
	 * @param archivoConvencion the archivoConvencion to set
	 */
	public void setArchivoConvencion(String archivoConvencion) {
		this.archivoConvencion = archivoConvencion;
	}

	/**
	 * @return the colorBorde
	 */
	public String getColorBorde() {
		return colorBorde;
	}

	/**
	 * @param colorBorde the colorBorde to set
	 */
	public void setColorBorde(String colorBorde) {
		this.colorBorde = colorBorde;
	}

}
