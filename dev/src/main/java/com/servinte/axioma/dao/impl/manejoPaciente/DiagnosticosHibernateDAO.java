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
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public DiagnosticosHibernateDAO(){
		delegate = new DiagnosticosDelegate();
	}
	
	
	/**
	 * Este Método se encarga de consultar los diagnósticos
	 * registrados en el sistema
	 * 
	 * @return ArrayList<Diagnosticos>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<Diagnosticos> consultarDiagnosticos(){
		return delegate.consultarDiagnosticos();
	}
	
	/**
	 * Este Método se encarga los acronimos de los diagnosticos
	 * 
	 * @return List<String>
	 * 
	 * @author, Fabián Becerra
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
