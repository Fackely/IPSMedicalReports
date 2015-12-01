package com.servinte.axioma.dao.impl.facturacion;


import com.servinte.axioma.dao.interfaz.facturacion.ITarifasEntidadSubDAO;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.TarifasEntidadSub;
import com.servinte.axioma.orm.delegate.facturacion.TarifasEntidadSubDelegate;

/**
 * Esta clase se encarga de implementar los métodos de
 * negocio para la entidad tipos_contrato
 * 
 * @author Angela Maria Aguirre
 * @since 4/11/2010
 */
public class TarifasEntidadSubHibernateDAO implements ITarifasEntidadSubDAO {
	
	TarifasEntidadSubDelegate tarifasEntidadDelegate;
	
	public TarifasEntidadSubHibernateDAO() {
		tarifasEntidadDelegate=new TarifasEntidadSubDelegate();
	}
		
	/**
	 * 
	 * Implementacion del metodo persist de la super clase TarifasEntidadSubHome
	 * @param TarifasEntidadSub tarifasEntidadSub
	 * @return boolean 
	 * @author, Diana Ruiz
	 *
	 */
	
	public boolean sincronizarTarifasEntidadSub (TarifasEntidadSub tarifasEntidadSub) {
		return tarifasEntidadDelegate.sincronizarTarifasEntidadSub(tarifasEntidadSub);
	}
	

}
