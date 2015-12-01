package com.sysmedica.dao.postgresql;

import com.sysmedica.dao.ArchivosPlanosDao;
import com.sysmedica.dao.sqlbase.SqlBaseArchivosPlanosDao;
import java.sql.Connection;
import java.sql.ResultSet;

public class PostgresqlArchivosPlanosDao implements ArchivosPlanosDao {

	public ResultSet consultaCasosDengue(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosDengue(con, semana, anyo);
	}
	
	
	public ResultSet consultaHallazgosDengue(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultaHallazgosDengue(con, codigo);
	}
	
	
	public ResultSet consultaCasosIntoxicacion(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosIntoxicacion(con, semana, anyo);
	}
	
	

	public ResultSet consultaCasosLepra(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosLepra(con, semana, anyo);
	}
	
	
	public ResultSet consultaCasosParalisis(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosParalisis(con, semana, anyo);
	}
	
	
	public ResultSet consultaExtremidadesParalisis(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultaExtremidadParalisis(con, codigo);
	}
	
	
	public ResultSet consultaGrupoEdadParalisis(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarGrupoEdadParalisis(con, codigo);
	}
	

	public ResultSet consultaCasosRabia(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosRabia(con, semana, anyo);
	}
	
	
	public ResultSet consultaCasosSarampion(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosSarampion(con, semana, anyo);
	}
	

	public ResultSet consultaCasosVIH(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosVIH(con, semana, anyo);
	}
	

	public ResultSet consultaMecanismosVIH(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarMecanismoVIH(con, codigo);
	}
	

	public ResultSet consultaEnfermedadesVIH(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarEnfermedadesVIH(con, codigo);
	}
	
	
	public ResultSet consultaCasosOfidico(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosOfidico(con, semana, anyo);
	}
	
	public ResultSet consultaManiLocalesOfidico(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarManiLocalesOfidico(con, codigo);
	}
	
	public ResultSet consultaManiSistemicaOfidico(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarManiSistemicaOfidico(con, codigo);
	}
	
	public ResultSet consultaCompliLocalesOfidico(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarCompliLocalesOfidico(con, codigo);
	}
	
	public ResultSet consultaCompliSistemicaOfidico(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarCompliSistemicaOfidico(con, codigo);
	}
	
	

	public ResultSet consultaCasosMortalidad(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosMortalidad(con, semana, anyo);
	}
	

	public ResultSet consultaAntecedentesMortalidad(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarAntecedentesMortalidad(con, codigo);
	}
	

	public ResultSet consultaComplicacionesMortalidad(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarComplicacionesMortalidad(con, codigo);
	}
	
	
	

	public ResultSet consultaCasosRubCongenita(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosRubCongenita(con, semana, anyo);
	}
	
	

	public ResultSet consultaCasosSifilis(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosSifilis(con, semana, anyo);
	}
	
	

	public ResultSet consultaCasosTetanos(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosTetanos(con, semana, anyo);
	}
	
	

	public ResultSet consultaCasosTuberculosis(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosTuberculosis(con, semana, anyo);
	}
	

	public ResultSet consultaCasosDifteria(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosDifteria(con, semana, anyo);
	}
	

	public ResultSet consultaCasosEasv(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosEasv(con, semana, anyo);
	}
	
	

	public ResultSet consultaVacunasEasv(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarVacunasEasv(con, codigo);
	}
	

	public ResultSet consultaHallazgosEasv(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarHallazgosEasv(con, codigo);
	}
	
	

	public ResultSet consultaCasosHepatitis(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosHepatitis(con, semana, anyo);
	}
	
	

	public ResultSet consultaPoblacionHepatitis(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarPoblacionHepatitis(con, codigo);
	}
	
	

	public ResultSet consultaSintomasHepatitis(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarSintomasHepatitis(con, codigo);
	}
	
	

	public ResultSet consultaCasosLeishmaniasis(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosLeishmaniasis(con, semana, anyo);
	}
	
	

	public ResultSet consultaTamLesionLeish(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarTamLesionLeish(con, codigo);
	}
	
	
	

	public ResultSet consultaCasosMalaria(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosMalaria(con, semana, anyo);
	}
	
	
	

	public ResultSet consultaSintomasMalaria(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarSintomasMalaria(con, codigo);
	}
	
	

	public ResultSet consultaTratamientoMalaria(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarTratamientoMalaria(con, codigo);
	}
	
	
	

	public ResultSet consultaCasosMeningitis(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosMeningitis(con, semana, anyo);
	}
	
	

	public ResultSet consultaCasosEsi(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosEsi(con, semana, anyo);
	}
	
	
	

	public ResultSet consultaCasosEtas(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosEtas(con, semana, anyo);
	}
	
	

	public ResultSet consultaSintomasEtas(Connection con, int codigo)
	{
		return SqlBaseArchivosPlanosDao.consultarSintomasEtas(con, codigo);
	}
	
	
	

	public ResultSet consultaCasosTosferina(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaCasosTosferina(con, semana, anyo);
	}
	
	
	

	public ResultSet consultaLaboratorios(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaLaboratorios(con, semana, anyo);
	}
	
	
	

	public ResultSet consultaInfoBasica(Connection con, int semana, int anyo, String tablaFicha)
	{
		return SqlBaseArchivosPlanosDao.consultaInfoBasica(con, semana, anyo, tablaFicha);
	}
	
	
	
	

	public ResultSet consultaBrotes(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaBrotes(con, semana, anyo);
	}
	
	
	

	public ResultSet consultaBrotesEtas(Connection con, int semana, int anyo)
	{
		return SqlBaseArchivosPlanosDao.consultaBrotesEtas(con, semana, anyo);
	}
}
