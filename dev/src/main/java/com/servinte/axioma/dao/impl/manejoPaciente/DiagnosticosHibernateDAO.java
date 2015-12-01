package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IDiagnosticosDAO;
import com.servinte.axioma.orm.Diagnosticos;
import com.servinte.axioma.orm.delegate.manejoPaciente.DiagnosticosDelegate;

/**
 * 
 */
public class DiagnosticosHibernateDAO implements IDiagnosticosDAO
{

	DiagnosticosDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Fabi�n Becerra
	 */
	public DiagnosticosHibernateDAO(){
		delegate = new DiagnosticosDelegate();
	}
	
	
	/**
	 * Este M�todo se encarga de consultar los diagn�sticos
	 * registrados en el sistema
	 * 
	 * @return ArrayList<Diagnosticos>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<Diagnosticos> consultarDiagnosticos(){
		return delegate.consultarDiagnosticos();
	}
	
	/**
	 * Este M�todo se encarga los acronimos de los diagnosticos
	 * 
	 * @return List<String>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public List<String> obtenerAcronimosDiagnosticosEnSistema(){
		return delegate.obtenerAcronimosDiagnosticosEnSistema();
	}
	

	@Override
	public DtoDiagnostico ultimoDiagnosticoEvolucion(DtoDiagnostico parametros) {
		return delegate.ultimoDiagnosticoEvolucion(parametros);
	}

	
	@Override
	public DtoDiagnostico ultimoDiagnosticoValoracion(DtoDiagnostico parametros) {
		return delegate.ultimoDiagnosticoValoracion(parametros);
	}
	
	
}
