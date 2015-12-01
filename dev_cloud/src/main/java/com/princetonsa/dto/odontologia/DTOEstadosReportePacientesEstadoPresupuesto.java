package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 15/10/2010
 */
public class DTOEstadosReportePacientesEstadoPresupuesto implements
		Serializable {
	
	private static final long serialVersionUID = 1L;
	private String nombreEstado;
	private Long cantidadPresupuesto;
	ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> listaPacientes;
	private JRBeanCollectionDataSource dsListaPacientes;
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreEstado
	
	 * @return retorna la variable nombreEstado 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreEstado() {
		return nombreEstado;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreEstado
	
	 * @param valor para el atributo nombreEstado 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadPresupuesto
	
	 * @return retorna la variable cantidadPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public Long getCantidadPresupuesto() {
		return cantidadPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadPresupuesto
	
	 * @param valor para el atributo cantidadPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadPresupuesto(Long cantidadPresupuesto) {
		this.cantidadPresupuesto = cantidadPresupuesto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaPacientes
	
	 * @return retorna la variable listaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> getListaPacientes() {
		return listaPacientes;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaPacientes
	
	 * @param valor para el atributo listaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaPacientes(
			ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> listaPacientes) {
		this.listaPacientes = listaPacientes;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsListaPacientes
	
	 * @return retorna la variable dsListaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public JRBeanCollectionDataSource getDsListaPacientes() {
		return dsListaPacientes;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsListaPacientes
	
	 * @param valor para el atributo dsListaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsListaPacientes(JRBeanCollectionDataSource dsListaPacientes) {
		this.dsListaPacientes = dsListaPacientes;
	}

	
}
