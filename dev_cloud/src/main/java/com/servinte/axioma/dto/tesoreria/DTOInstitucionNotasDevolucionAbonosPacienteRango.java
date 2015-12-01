package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Esta clase almacena los registros de instituciones de las notas de devoluci�n
 * abonos paciente por rango.
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 07/03/2011
 */
public class DTOInstitucionNotasDevolucionAbonosPacienteRango implements
		Serializable {

	private static final long serialVersionUID = 1L;
	private long codigoInstitucion;
	private String nombreInstitucion;
	private ArrayList<DTONotaDevolucionAbonosPacienteRango> listaNotasDevolucionesAbonos;
	private BigDecimal totalValorNotaDevolucion;

	/**
	 * M�todo encargado de obtener el valor del atributo codigoInstitucion.
	 * 
	 * @return codigoInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public long getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo codigoInstitucion.
	 * 
	 * @param codigoInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setCodigoInstitucion(long codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo nombreInstitucion.
	 * 
	 * @return nombreInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo nombreInstitucion.
	 * 
	 * @param nombreInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * listaNotasDevolucionesAbonos.
	 * 
	 * @return listaNotasDevolucionesAbonos
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public ArrayList<DTONotaDevolucionAbonosPacienteRango> getListaNotasDevolucionesAbonos() {
		return listaNotasDevolucionesAbonos;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * listaNotasDevolucionesAbonos.
	 * 
	 * @param listaNotasDevolucionesAbonos
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setListaNotasDevolucionesAbonos(
			ArrayList<DTONotaDevolucionAbonosPacienteRango> listaNotasDevolucionesAbonos) {
		this.listaNotasDevolucionesAbonos = listaNotasDevolucionesAbonos;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * totalValorNotaDevolucion.
	 * 
	 * @return totalValorNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public BigDecimal getTotalValorNotaDevolucion() {
		return totalValorNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * totalValorNotaDevolucion.
	 * 
	 * @param totalValorNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setTotalValorNotaDevolucion(BigDecimal totalValorNotaDevolucion) {
		this.totalValorNotaDevolucion = totalValorNotaDevolucion;
	}

}
