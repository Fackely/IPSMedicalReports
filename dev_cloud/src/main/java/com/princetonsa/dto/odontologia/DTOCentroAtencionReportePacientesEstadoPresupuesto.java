package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 15/10/2010
 */
public class DTOCentroAtencionReportePacientesEstadoPresupuesto implements
		Serializable {
	
	private static final long serialVersionUID = 1L;
	private int codigoCentroAtencion;
	private String nombreCompletoCentroAtencion;
	private long totalPresupuestoCentroAtencion;
	private long codigoInstitucion;
	private String nombreInstitucion;
	private ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstados;
	private JRBeanCollectionDataSource dsListaEstados;
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoCentroAtencion
	
	 * @return retorna la variable codigoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoCentroAtencion
	
	 * @param valor para el atributo codigoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaEstados
	
	 * @return retorna la variable listaEstados 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> getListaEstados() {
		return listaEstados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaEstados
	
	 * @param valor para el atributo listaEstados 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaEstados(
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstados) {
		this.listaEstados = listaEstados;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsListaEstados
	
	 * @return retorna la variable dsListaEstados 
	 * @author Angela Maria Aguirre 
	 */
	public JRBeanCollectionDataSource getDsListaEstados() {
		return dsListaEstados;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsListaEstados
	
	 * @param valor para el atributo dsListaEstados 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsListaEstados(JRBeanCollectionDataSource dsListaEstados) {
		this.dsListaEstados = dsListaEstados;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo totalPresupuestoCentroAtencion
	
	 * @return retorna la variable totalPresupuestoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public long getTotalPresupuestoCentroAtencion() {
		return totalPresupuestoCentroAtencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo totalPresupuestoCentroAtencion
	
	 * @param valor para el atributo totalPresupuestoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTotalPresupuestoCentroAtencion(
			long totalPresupuestoCentroAtencion) {
		this.totalPresupuestoCentroAtencion = totalPresupuestoCentroAtencion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreCompletoCentroAtencion
	
	 * @return retorna la variable nombreCompletoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreCompletoCentroAtencion() {
		return nombreCompletoCentroAtencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCompletoCentroAtencion
	
	 * @param valor para el atributo nombreCompletoCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreCompletoCentroAtencion(String nombreCompletoCentroAtencion) {
		this.nombreCompletoCentroAtencion = nombreCompletoCentroAtencion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoInstitucion
	
	 * @return retorna la variable codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoInstitucion() {
		return codigoInstitucion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	
	 * @param valor para el atributo codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoInstitucion(long codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreInstitucion
	
	 * @return retorna la variable nombreInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreInstitucion() {
		return nombreInstitucion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreInstitucion
	
	 * @param valor para el atributo nombreInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}
	
	

}
