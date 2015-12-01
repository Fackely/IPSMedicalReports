package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IOtrosSiDAO;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.orm.OtrosSiExclusiones;
import com.servinte.axioma.orm.OtrosSiInclusiones;
import com.servinte.axioma.orm.delegate.odontologia.contrato.OtrosSiDelegate;
import com.servinte.axioma.orm.delegate.odontologia.contrato.OtrosSiExclusionesDelegate;
import com.servinte.axioma.orm.delegate.odontologia.contrato.OtrosSiInclusionesDelegate;

/**
 * @author Cristhian Murillo
 */
public class OtrosSiHibernateDAO implements IOtrosSiDAO 
{

	private OtrosSiDelegate otroSiDelegate;
	private OtrosSiExclusionesDelegate otrosSiExclusionesDelegate;
	private OtrosSiInclusionesDelegate otrosSiInclusionesDelegate;
	
	
	/** * Costructor  */
	public OtrosSiHibernateDAO() 
	{
		otroSiDelegate 				= new OtrosSiDelegate();
		otrosSiExclusionesDelegate 	= new OtrosSiExclusionesDelegate();
		otrosSiInclusionesDelegate 	= new OtrosSiInclusionesDelegate();
	}
	
	

	@Override
	public ArrayList<OtrosSi> obtenerOtroSiOrdenadosMayorMenor(long codPresoOdonto) {
		return otroSiDelegate.obtenerOtroSiOrdenadosMayorMenor(codPresoOdonto);
	}


	@Override
	public void persist(OtrosSi transientInstance) {
		otroSiDelegate.persist(transientInstance);
	}
	
	
	@Override
	public void attachDirty(OtrosSi instance) {
		otroSiDelegate.persist(instance);
	}
	

	@Override
	public void persistOtrosSiExclusiones(OtrosSiExclusiones transientInstance) {
		otrosSiExclusionesDelegate.persistOtrosSiExclusiones(transientInstance);
	}


	@Override
	public void persistOtrosSiInclusiones(OtrosSiInclusiones transientInstance) {
		otrosSiInclusionesDelegate.persist(transientInstance);
	}


	@Override
	public ArrayList<DtoOtroSi> obtenerOtrosSiporPresupuesto(long codPresoOdonto) {
		return otroSiDelegate.obtenerOtrosSiporPresupuesto(codPresoOdonto);
	}

	
}
