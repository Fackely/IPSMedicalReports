package com.sysmedica.dao;

import java.sql.ResultSet;
import java.sql.Connection;

public interface ArchivosPlanosDao {

	
	public ResultSet consultaCasosDengue(Connection con, int semana, int anyo);
	
	public ResultSet consultaHallazgosDengue(Connection con, int codigo);
	
	public ResultSet consultaCasosIntoxicacion(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosLepra(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosParalisis(Connection con, int semana, int anyo);
	
	public ResultSet consultaExtremidadesParalisis(Connection con, int codigo);
	
	public ResultSet consultaGrupoEdadParalisis(Connection con, int codigo);
	
	public ResultSet consultaCasosRabia(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosSarampion(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosVIH(Connection con, int semana, int anyo);
	
	public ResultSet consultaMecanismosVIH(Connection con, int codigo);
	
	public ResultSet consultaEnfermedadesVIH(Connection con, int codigo);
	
	public ResultSet consultaCasosOfidico(Connection con, int semana, int anyo);
	
	public ResultSet consultaManiLocalesOfidico(Connection con, int codigo);
	
	public ResultSet consultaManiSistemicaOfidico(Connection con, int codigo);
	
	public ResultSet consultaCompliLocalesOfidico(Connection con, int codigo);
	
	public ResultSet consultaCompliSistemicaOfidico(Connection con, int codigo);
	
	public ResultSet consultaCasosMortalidad(Connection con, int semana, int anyo);
	
	public ResultSet consultaAntecedentesMortalidad(Connection con, int codigo);
	
	public ResultSet consultaComplicacionesMortalidad(Connection con, int codigo);
	
	public ResultSet consultaCasosRubCongenita(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosSifilis(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosTetanos(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosTuberculosis(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosDifteria(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosEasv(Connection con, int semana, int anyo);
	
	public ResultSet consultaVacunasEasv(Connection con, int codigo);
	
	public ResultSet consultaHallazgosEasv(Connection con, int codigo);
	
	public ResultSet consultaCasosHepatitis(Connection con, int semana, int anyo);
	
	public ResultSet consultaPoblacionHepatitis(Connection con, int codigo);
	
	public ResultSet consultaSintomasHepatitis(Connection con, int codigo);
	
	public ResultSet consultaCasosLeishmaniasis(Connection con, int semana, int anyo);
	
	public ResultSet consultaTamLesionLeish(Connection con, int codigo);
	
	public ResultSet consultaCasosMalaria(Connection con, int semana, int anyo);
	
	public ResultSet consultaSintomasMalaria(Connection con, int codigo);
	
	public ResultSet consultaTratamientoMalaria(Connection con, int codigo);
	
	public ResultSet consultaCasosMeningitis(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosEsi(Connection con, int semana, int anyo);
	
	public ResultSet consultaCasosEtas(Connection con, int semana, int anyo);
	
	public ResultSet consultaSintomasEtas(Connection con, int codigo);
	
	public ResultSet consultaCasosTosferina(Connection con, int semana, int anyo);
	
	public ResultSet consultaLaboratorios(Connection con, int semana, int anyo);
	
	public ResultSet consultaInfoBasica(Connection con, int semana, int anyo, String tablaFicha);
	
	public ResultSet consultaBrotes(Connection con, int semana, int anyo);
	
	public ResultSet consultaBrotesEtas(Connection con, int semana, int anyo);
}
