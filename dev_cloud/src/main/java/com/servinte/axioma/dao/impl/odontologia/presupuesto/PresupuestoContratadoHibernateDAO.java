package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IPresupuestoContratadoDAO;
import com.servinte.axioma.orm.delegate.odontologia.presupuesto.PresupuestoContratadoDelegate;

public class PresupuestoContratadoHibernateDAO implements IPresupuestoContratadoDAO {
	
	private PresupuestoContratadoDelegate delegate;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PresupuestoContratadoHibernateDAO() {
		delegate = new PresupuestoContratadoDelegate();
	}
	
	@Override
	public List<DtoPresupuestoContratado> obtenerContratosPresupuestoOdonto(DtoPresupuestoContratado dto){
		return delegate.obtenerContratosPresupuestoOdonto(dto);
	}

}
