package com.servinte.axioma.dao.impl.capitacion;

import com.servinte.axioma.dao.interfaz.capitacion.IContratoCargueDAO;
import com.servinte.axioma.orm.ContratoCargue;
import com.servinte.axioma.orm.delegate.capitacion.ContratoCargueDelegate;

public class ContratoCargueHibernateDAO implements IContratoCargueDAO{

	ContratoCargueDelegate delegate;
	
	
	/**
	 * Método constructor de la clase
	 * 
	 * @author Ricardo Ruiz
	 */
	public ContratoCargueHibernateDAO(){
		delegate = new ContratoCargueDelegate();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IContratoCargueDAO#guardarContratoCargue(com.servinte.axioma.orm.ContratoCargue)
	 */
	@Override
	public void guardarContratoCargue(ContratoCargue contratoCargue) {
		delegate.guardarContratoCargue(contratoCargue);
	}

}
