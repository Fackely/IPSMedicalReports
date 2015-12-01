package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.odontologia.InfoServicios;

/**
 * 
 * @author axioma
 *
 */
public class DtoDetalleProximaCita implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6814218279816132256L;

	/**
	 * si la institucion no usa programas entonces este campo es null
	 */
	private InfoDatosDouble programa;
	
	/**
	 * 
	 */
	private InfoDatosInt piezaDental;
	
	/**
	 * 
	 */
	private ArrayList<InfoServicios> servicios;
   
	/**
	 * Atributo que contiene toda la información asociada al servicio con el fin de 
	 * determinar cuales servicios cumplen con las condiciones para realizar la respectiva
	 * actualización del programa hallazgo pieza
	 */
	private DtoPresupuestoPiezas pieza;
		
	/**
	 * 
	 */
	private ArrayList<InfoDatosDouble> superficies;
	
	/**
	 * Atributo que indica si el detalle se encuentra asociado a la programación
	 * de la próxima cita.
	 */
	private boolean asociadoProximaCita;
	
	/**
	 * 
	 * @param fecha
	 * @param programa
	 * @param piezaDental
	 * @param servicios
	 */
	public DtoDetalleProximaCita() 
	{
		super();
		this.programa = new InfoDatosDouble();
		this.piezaDental = new InfoDatosInt();
		this.servicios = new ArrayList<InfoServicios>();
		this.superficies= new ArrayList<InfoDatosDouble>();
		this.pieza = new DtoPresupuestoPiezas();
	}

	/**
	 * @return the programa
	 */
	public InfoDatosDouble getPrograma() {
		return programa;
	}

	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(InfoDatosDouble programa) {
		this.programa = programa;
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
	 * @return the servicios
	 */
	public ArrayList<InfoServicios> getServicios() {
		return servicios;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(ArrayList<InfoServicios> servicios) {
		this.servicios = servicios;
	}

	/**
	 * @return the superficies
	 */
	public ArrayList<InfoDatosDouble> getSuperficies() {
		return superficies;
	}

	/**
	 * @param superficies the superficies to set
	 */
	public void setSuperficies(ArrayList<InfoDatosDouble> superficies) {
		this.superficies = superficies;
	}

	/**
	 * 
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public String getNombreSuperficies()
	{
		String retorna="";
		for(InfoDatosDouble info: this.getSuperficies())
		{
			if(!retorna.isEmpty())
			{	
				retorna+=", ";
			}
			retorna+= info.getNombre();
		}
		return retorna;
	}

	/**
	 * @param asociadoProximaCita the asociadoProximaCita to set
	 */
	public void setAsociadoProximaCita(boolean asociadoProximaCita) {
		this.asociadoProximaCita = asociadoProximaCita;
	}

	/**
	 * @return the asociadoProximaCita
	 */
	public boolean isAsociadoProximaCita() {
		return asociadoProximaCita;
	}

	/**
	 * @param pieza the pieza to set
	 */
	public void setPieza(DtoPresupuestoPiezas pieza) {
		this.pieza = pieza;
	}

	/**
	 * @return the pieza
	 */
	public DtoPresupuestoPiezas getPieza() {
		return pieza;
	}

}