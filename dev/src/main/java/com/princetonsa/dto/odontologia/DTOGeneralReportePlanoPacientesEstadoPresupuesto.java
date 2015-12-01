package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 2/11/2010
 */
public class DTOGeneralReportePlanoPacientesEstadoPresupuesto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JRBeanCollectionDataSource dsPacientesEstadoPresupuesto;
	private JRBeanCollectionDataSource dsDetallePacientesEstadoPresupuesto;
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsPacientesEstadoPresupuesto
	
	 * @return retorna la variable dsPacientesEstadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public JRBeanCollectionDataSource getDsPacientesEstadoPresupuesto() {
		return dsPacientesEstadoPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsPacientesEstadoPresupuesto
	
	 * @param valor para el atributo dsPacientesEstadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsPacientesEstadoPresupuesto(
			JRBeanCollectionDataSource dsPacientesEstadoPresupuesto) {
		this.dsPacientesEstadoPresupuesto = dsPacientesEstadoPresupuesto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsDetallePacientesEstadoPresupuesto
	
	 * @return retorna la variable dsDetallePacientesEstadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public JRBeanCollectionDataSource getDsDetallePacientesEstadoPresupuesto() {
		return dsDetallePacientesEstadoPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsDetallePacientesEstadoPresupuesto
	
	 * @param valor para el atributo dsDetallePacientesEstadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsDetallePacientesEstadoPresupuesto(
			JRBeanCollectionDataSource dsDetallePacientesEstadoPresupuesto) {
		this.dsDetallePacientesEstadoPresupuesto = dsDetallePacientesEstadoPresupuesto;
	}
	

}
