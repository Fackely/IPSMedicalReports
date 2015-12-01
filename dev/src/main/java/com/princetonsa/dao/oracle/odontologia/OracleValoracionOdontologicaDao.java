package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.odontologia.ValoracionOdontologicaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseProgramasOdontologicosDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseValoracionOdontologicaDao;
import com.princetonsa.dto.historiaClinica.DtoPlantillasIngresos;
import com.princetonsa.dto.odontologia.DtoValDiagnosticosOdo;
import com.princetonsa.dto.odontologia.DtoValoracionOdontologica;
import com.princetonsa.dto.odontologia.DtoValoracionesOdonto;
import com.princetonsa.mundo.atencion.Diagnostico;



public class OracleValoracionOdontologicaDao implements ValoracionOdontologicaDao
{
	public double ingresarValoracionesOdonto(Connection con,DtoValoracionesOdonto dto)
	{
		return SqlBaseValoracionOdontologicaDao.ingresarValoracionesOdonto(con,dto);
	}
	
	public boolean ingresarDiagnosticos(Connection con,ArrayList<Diagnostico> diagnostico, HashMap diagnosticosRelacionados, double valoracion)
	{
		return SqlBaseValoracionOdontologicaDao.ingresarDiagnosticos(con,diagnostico, diagnosticosRelacionados, valoracion);
	}
	
	public ArrayList<DtoValDiagnosticosOdo> consultaDiagnosticosValOdo(double valoracion)
	{
		return SqlBaseValoracionOdontologicaDao.consultaDiagnosticosValOdo(valoracion);
	}
	
	public DtoPlantillasIngresos consultarPacienteTieneValoracion(int codigoPaciente, double cita, int plantilla)
	{
		return SqlBaseValoracionOdontologicaDao.consultarPacienteTieneValoracion(codigoPaciente, cita, plantilla);
	}
	
	public DtoValoracionesOdonto consultarValoracionPaciente(int codigoPaciente, double cita, int plantilla)
	{
		return SqlBaseValoracionOdontologicaDao.consultarValoracionPaciente(codigoPaciente,cita, plantilla);
	}
	
	public boolean borrarDx(Connection con,double valoracion)
	{
		return SqlBaseValoracionOdontologicaDao.borrarDx(con,valoracion);
	}
	
	/**
	 * Método implementado para consultar la información de la valoracion odontologica
	 * @param con
	 * @param valoracionOdo
	 */
	public void consultar(Connection con,DtoValoracionesOdonto valoracionOdo)
	{
		SqlBaseValoracionOdontologicaDao.consultar(con, valoracionOdo);
	}
	
	public String obtenerNombreDiagnosticoPrincipal(int codigoDx)
	{
		return SqlBaseValoracionOdontologicaDao.obtenerNombreDiagnosticoPrincipal(codigoDx);
	}
}