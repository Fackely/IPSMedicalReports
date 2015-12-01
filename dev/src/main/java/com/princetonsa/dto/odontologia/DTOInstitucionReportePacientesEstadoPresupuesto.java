package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 15/10/2010
 */
public class DTOInstitucionReportePacientesEstadoPresupuesto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long codigoInstitucion;
	private String nombreInstitucion;
	private long totalPresupuestoInstitucion;	
	private ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto> listaCentroAtencion;
	private JRBeanCollectionDataSource dsListaCentroAtrencion;
	
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
	 * del atributo listaCentroAtencion
	
	 * @return retorna la variable listaCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto> getListaCentroAtencion() {
		return listaCentroAtencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaCentroAtencion
	
	 * @param valor para el atributo listaCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaCentroAtencion(
			ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto> listaCentroAtencion) {
		this.listaCentroAtencion = listaCentroAtencion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsListaCentroAtrencion
	
	 * @return retorna la variable dsListaCentroAtrencion 
	 * @author Angela Maria Aguirre 
	 */
	public JRBeanCollectionDataSource getDsListaCentroAtrencion() {
		return dsListaCentroAtrencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsListaCentroAtrencion
	
	 * @param valor para el atributo dsListaCentroAtrencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsListaCentroAtrencion(
			JRBeanCollectionDataSource dsListaCentroAtrencion) {
		this.dsListaCentroAtrencion = dsListaCentroAtrencion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo totalPresupuestoInstitucion
	
	 * @return retorna la variable totalPresupuestoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public long getTotalPresupuestoInstitucion() {
		return totalPresupuestoInstitucion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo totalPresupuestoInstitucion
	
	 * @param valor para el atributo totalPresupuestoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTotalPresupuestoInstitucion(long totalPresupuestoInstitucion) {
		this.totalPresupuestoInstitucion = totalPresupuestoInstitucion;
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
