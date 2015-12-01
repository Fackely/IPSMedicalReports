package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoDetallesPacientesPoliconsultadores  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String nombreViasIngreso;
	private String nombreEspecialidad;
	private String tipoServicio;
	private String nombreUnidadAgenda;
	private int cantidadIngresos;
	private int codigoEspecialidad;
	
		
    private ArrayList<DtoDetallesPacientesPoliconsultadores>listaDetalles;
    private JRDataSource JRDDetalles;
    
    private ArrayList<DtoDetallesPacientesPoliconsultadores>listaUnidades;
    private JRDataSource JRDUnidades;
    
    public DtoDetallesPacientesPoliconsultadores(){
    	
    }
    
	/**
	 * @return the listaDetalles
	 */
	public ArrayList<DtoDetallesPacientesPoliconsultadores> getListaDetalles() {
		return listaDetalles;
	}

	/**
	 * @param listaDetalles the listaDetalles to set
	 */
	public void setListaDetalles(ArrayList<DtoDetallesPacientesPoliconsultadores> listaDetalles) {
		this.listaDetalles = listaDetalles;
	}

	/**
	 * @return the jRDDetalles
	 */
	public JRDataSource getJRDDetalles() {
		return JRDDetalles;
	}

	/**
	 * @param jRDDetalles the jRDDetalles to set
	 */
	public void setJRDDetalles(JRDataSource jRDDetalles) {
		JRDDetalles = jRDDetalles;
	}	
	/**
	 * @return the nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	/**
	 * @param nombreEspecialidad the nombreEspecialidad to set
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the nombreViasIngreso
	 */
	public String getNombreViasIngreso() {
		return nombreViasIngreso;
	}

	/**
	 * @param nombreViasIngreso the nombreViasIngreso to set
	 */
	public void setNombreViasIngreso(String nombreViasIngreso) {
		this.nombreViasIngreso = nombreViasIngreso;
	}

	/**
	 * @return the cantidadIngresos
	 */
	public int getCantidadIngresos() {
		return cantidadIngresos;
	}

	/**
	 * @param cantidadIngresos the cantidadIngresos to set
	 */
	public void setCantidadIngresos(int cantidadIngresos) {
		this.cantidadIngresos = cantidadIngresos;
	}

	/**
	 * @return the nombreUnidadAgenda
	 */
	public String getNombreUnidadAgenda() {
		return nombreUnidadAgenda;
	}

	/**
	 * @param nombreUnidadAgenda the nombreUnidadAgenda to set
	 */
	public void setNombreUnidadAgenda(String nombreUnidadAgenda) {
		this.nombreUnidadAgenda = nombreUnidadAgenda;
	}

	/**
	 * @return the codigoEspecialidad
	 */
	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	/**
	 * @param codigoEspecialidad the codigoEspecialidad to set
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * @return the jRDUnidades
	 */
	public JRDataSource getJRDUnidades() {
		return JRDUnidades;
	}

	/**
	 * @param jRDUnidades the jRDUnidades to set
	 */
	public void setJRDUnidades(JRDataSource jRDUnidades) {
		JRDUnidades = jRDUnidades;
	}

	/**
	 * @return the listaUnidades
	 */
	public ArrayList<DtoDetallesPacientesPoliconsultadores> getListaUnidades() {
		return listaUnidades;
	}

	/**
	 * @param listaUnidades the listaUnidades to set
	 */
	public void setListaUnidades(ArrayList<DtoDetallesPacientesPoliconsultadores> listaUnidades) {
		this.listaUnidades = listaUnidades;
	}
}
