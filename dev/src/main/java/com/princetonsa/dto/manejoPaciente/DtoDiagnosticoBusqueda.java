/**
 * 
 */
package com.princetonsa.dto.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;

import util.Utilidades;



/**
 * clase utilizada para administrar la busqueda centralizada 
 * de diagnosticos por nombre o código.
 * 
 * @author Cristhian Murillo
 */
public class DtoDiagnosticoBusqueda 
{

	/** * Mapa para manejar los diagnosticos relacionados */
	private HashMap<String, Object> diagnosticosRelacionados;
	
	/** * Variable que obtiene el listado de los diagnosticos seleccionados */
	private String diagnosticosSeleccionados;
	
	/** * valoracionUrgencias  */
	private DtoValoracionUrgencias valoracionUrgencias;
	
	/** * Tipos de diagnósticos  */
	private ArrayList<HashMap<String, Object>> tiposDiagnostico;
	
	
	
	/**
	 * Constructor de la clase.
	 * @author Cristhian Murillo
	 */
	public DtoDiagnosticoBusqueda() 
	{
		this.diagnosticosSeleccionados		= "";
		this.diagnosticosRelacionados		= new HashMap<String, Object>();
		this.valoracionUrgencias 			= new DtoValoracionUrgencias();
		this.tiposDiagnostico				= new ArrayList<HashMap<String,Object>>();
	}
	

	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}


	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}
	
	
	public HashMap<String, Object> getDiagnosticosRelacionados() {
		return diagnosticosRelacionados;
	}
	

	public void setDiagnosticosRelacionados(
			HashMap<String, Object> diagnosticosRelacionados) {
		this.diagnosticosRelacionados = diagnosticosRelacionados;
	}
	
	
	/**
	 * Método para asignar el número de dx relacionados
	 * @param numRegistros
	 */
	public void setNumDiagRelacionados(int numRegistros)
	{
		this.diagnosticosRelacionados.put("numRegistros",numRegistros);
	}
	
	/**
	 * Método para obtener el número de diagnósticos relacionados
	 * @return
	 */
	public int getNumDiagRelacionados()
	{
		return Utilidades.convertirAEntero(this.getDiagnosticosRelacionados("numRegistros")+"", true);
	}
	
	
	/**
	 * @return the diagnosticosRelacionados
	 */
	public Object getDiagnosticosRelacionados(String key) {
		return diagnosticosRelacionados.get(key);
	}


	public DtoValoracionUrgencias getValoracionUrgencias() {
		return valoracionUrgencias;
	}


	public void setValoracionUrgencias(DtoValoracionUrgencias valoracionUrgencias) {
		this.valoracionUrgencias = valoracionUrgencias;
	}


	public ArrayList<HashMap<String, Object>> getTiposDiagnostico() {
		return tiposDiagnostico;
	}


	public void setTiposDiagnostico(
			ArrayList<HashMap<String, Object>> tiposDiagnostico) {
		this.tiposDiagnostico = tiposDiagnostico;
	}
}
