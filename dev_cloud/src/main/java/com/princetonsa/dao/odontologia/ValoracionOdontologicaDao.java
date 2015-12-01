package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.historiaClinica.DtoPlantillasIngresos;
import com.princetonsa.dto.odontologia.DtoValDiagnosticosOdo;
import com.princetonsa.dto.odontologia.DtoValoracionesOdonto;
import com.princetonsa.mundo.atencion.Diagnostico;

public interface ValoracionOdontologicaDao
{
	public double ingresarValoracionesOdonto(Connection con,DtoValoracionesOdonto dto);
	
	public boolean ingresarDiagnosticos(Connection con,ArrayList<Diagnostico> diagnostico, HashMap diagnosticosRelacionados, double valoracion);
	
	public ArrayList<DtoValDiagnosticosOdo> consultaDiagnosticosValOdo(double valoracion);
	
	public DtoPlantillasIngresos consultarPacienteTieneValoracion(int codigoPaciente, double cita, int plantilla);
	
	public DtoValoracionesOdonto consultarValoracionPaciente(int codigoPaciente, double cita, int plantilla);
	
	public boolean borrarDx(Connection con,double valoracion);
	
	/**
	 * Método implementado para consultar la información de la valoracion odontologica
	 * @param con
	 * @param valoracionOdo
	 */
	public void consultar(Connection con,DtoValoracionesOdonto valoracionOdo);
	
	public String obtenerNombreDiagnosticoPrincipal(int codigoDx);
}