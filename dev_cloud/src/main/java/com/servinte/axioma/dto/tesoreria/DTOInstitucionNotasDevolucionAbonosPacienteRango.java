package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Esta clase almacena los registros de instituciones de las notas de devolución
 * abonos paciente por rango.
 * 
 * @author Luis Fernando Hincapié Ospina
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
	 * Método encargado de obtener el valor del atributo codigoInstitucion.
	 * 
	 * @return codigoInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public long getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo codigoInstitucion.
	 * 
	 * @param codigoInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setCodigoInstitucion(long codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo nombreInstitucion.
	 * 
	 * @return nombreInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo nombreInstitucion.
	 * 
	 * @param nombreInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * listaNotasDevolucionesAbonos.
	 * 
	 * @return listaNotasDevolucionesAbonos
	 * @author Luis Fernando Hincapié Ospina
	 */
	public ArrayList<DTONotaDevolucionAbonosPacienteRango> getListaNotasDevolucionesAbonos() {
		return listaNotasDevolucionesAbonos;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * listaNotasDevolucionesAbonos.
	 * 
	 * @param listaNotasDevolucionesAbonos
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setListaNotasDevolucionesAbonos(
			ArrayList<DTONotaDevolucionAbonosPacienteRango> listaNotasDevolucionesAbonos) {
		this.listaNotasDevolucionesAbonos = listaNotasDevolucionesAbonos;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * totalValorNotaDevolucion.
	 * 
	 * @return totalValorNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public BigDecimal getTotalValorNotaDevolucion() {
		return totalValorNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * totalValorNotaDevolucion.
	 * 
	 * @param totalValorNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setTotalValorNotaDevolucion(BigDecimal totalValorNotaDevolucion) {
		this.totalValorNotaDevolucion = totalValorNotaDevolucion;
	}

}
