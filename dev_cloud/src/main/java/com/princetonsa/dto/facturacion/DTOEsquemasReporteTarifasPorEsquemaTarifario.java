package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de determinar las tarfias por esquema tarifario.
 * 
 * @author Luis Fernando Hincapi&eacute; Ospina
 * @since 19/11/2010
 */
public class DTOEsquemasReporteTarifasPorEsquemaTarifario implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private int codigoEsquemaTarifario;
	private String nombreEsquemaTarifario;
	private long codigoPrograma;
	ArrayList<DtoTarifasPorEsquemaTarifario> listaTarifas;
	ArrayList<DTOTarifasServicios> listaTarifasServicios;
	private JRBeanCollectionDataSource dsListaTarifas;

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * codigoEsquemaTarifario.
	 * 
	 * @return codigoEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public int getCodigoEsquemaTarifario() {
		return codigoEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoEsquemaTarifario.
	 * 
	 * @param codigoEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario) {
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * nombreEsquemaTarifario.
	 * 
	 * @return nombreEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public String getNombreEsquemaTarifario() {
		return nombreEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * nombreEsquemaTarifario.
	 * 
	 * @param nombreEsquemaTarifario
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario) {
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo codigoPrograma.
	 * 
	 * @return codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public long getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * codigoPrograma.
	 * 
	 * @param codigoPrograma
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setCodigoPrograma(long codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo listaTarifas.
	 * 
	 * @return listaTarifas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DtoTarifasPorEsquemaTarifario> getListaTarifas() {
		return listaTarifas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo listaTarifas.
	 * 
	 * @param listaTarifas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaTarifas(ArrayList<DtoTarifasPorEsquemaTarifario> listaTarifas) {
		this.listaTarifas = listaTarifas;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo
	 * listaTarifasServicios.
	 * 
	 * @return listaTarifasServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DTOTarifasServicios> getListaTarifasServicios() {
		return listaTarifasServicios;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * listaTarifasServicios.
	 * 
	 * @param listaTarifasServicios
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setListaTarifasServicios(
			ArrayList<DTOTarifasServicios> listaTarifasServicios) {
		this.listaTarifasServicios = listaTarifasServicios;
	}

	/**
	 * M&eacute;todo encargado de obtener el valor del atributo dsListaTarifas.
	 * 
	 * @return dsListaTarifas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public JRBeanCollectionDataSource getDsListaTarifas() {
		return dsListaTarifas;
	}

	/**
	 * M&eacute;todo encargado de establecer el valor del atributo
	 * dsListaTarifas.
	 * 
	 * @param dsListaTarifas
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public void setDsListaTarifas(JRBeanCollectionDataSource dsListaTarifas) {
		this.dsListaTarifas = dsListaTarifas;
	}

}
