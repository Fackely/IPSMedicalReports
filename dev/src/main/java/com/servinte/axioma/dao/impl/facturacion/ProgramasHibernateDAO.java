package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.facturacion.IProgramasDAO;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.delegate.odontologia.programas.ProgramasDelegate;

public class ProgramasHibernateDAO implements IProgramasDAO {

	ProgramasDelegate delegate;

	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * 
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ProgramasHibernateDAO() {
		delegate = new ProgramasDelegate();
	}

	/**
	 * 
	 * M&eacute;todo encargado de listar los programas registrados en el sistema
	 * 
	 * @return ArrayList<Programas>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Programas> listarProgramas() {
		return delegate.listarProgramas();
	}

}
