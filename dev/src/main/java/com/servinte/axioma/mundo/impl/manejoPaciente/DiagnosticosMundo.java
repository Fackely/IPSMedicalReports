package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import util.UtilidadTexto;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IDiagnosticosDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IDiagnosticosMundo;
import com.servinte.axioma.orm.Diagnosticos;

public class DiagnosticosMundo implements IDiagnosticosMundo
{

	IDiagnosticosDAO dao;
	
	
	public DiagnosticosMundo(){
		dao = ManejoPacienteDAOFabrica.crearDiagnosticosDAO();
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
		return dao.consultarDiagnosticos();
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
		return dao.obtenerAcronimosDiagnosticosEnSistema();
	}


	
	@Override
	public DtoDiagnostico ultimoDiagnostico(DtoDiagnostico parametros) 
	{
		/* Primero se debe buscar el diagnostico de Evolución 
		 * y si este no exista, se busca el de Ealoración.  */
		DtoDiagnostico ultimoDiagnostico = new DtoDiagnostico();
		ultimoDiagnostico = dao.ultimoDiagnosticoEvolucion(parametros);
		
		if(UtilidadTexto.isEmpty(ultimoDiagnostico.getAcronimoDiagnostico())){
			ultimoDiagnostico = dao.ultimoDiagnosticoValoracion(parametros);
		}
		
		 return ultimoDiagnostico;
	}
	
	
}
