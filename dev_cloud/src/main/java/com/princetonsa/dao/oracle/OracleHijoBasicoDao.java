/*
 * @(#)OracleHijoBasicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
 
 package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import com.princetonsa.dao.HijoBasicoDao;
import com.princetonsa.dao.sqlbase.SqlBaseHijoBasicoDao;

/**
 * Método que implementa toda la funcionalidad relacionada con la BD para el
 * objeto <i>HijoBasico</i> en la BD Oracle
 *
 * @version 1.0, Apr 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class OracleHijoBasicoDao implements HijoBasicoDao
{
	/**
	 * Implementación de la inserción de un hijo (antecedentes Gineco-
	 * Obstetricos) para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#insertar(Connection, int, int, int, boolean, String, boolean, boolean, String)
	 */
	public int insertar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException
	{
		return SqlBaseHijoBasicoDao.insertar (con, codigoPaciente, numeroEmbarazo, numeroHijo, vivo, otroTipoPartoVaginal, cesarea, aborto, otroTipoParto, tiposPartoVaginal, peso, sexo, lugar) ;
	}

	/**
	 * Implementación de la inserción de un hijo (antecedentes Gineco-
	 * Obstetricos), permitiendo definir el nivel de transaccionalidad para 
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#insertarTransaccional(Connection, int, int, int, boolean, String, boolean, boolean, String, String)
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException
	{
		return SqlBaseHijoBasicoDao.insertarTransaccional (con, codigoPaciente, numeroEmbarazo, numeroHijo, vivo, otroTipoPartoVaginal, cesarea, aborto, otroTipoParto, tiposPartoVaginal, estado, peso, sexo, lugar) ;
	}

	/**
	 * Implementación de la modificación de un hijo (antecedentes Gineco-
	 * Obstetricos) para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#modificar(Connection, int, int, int, boolean, String, boolean, boolean, String, ArrayList, String, int, String)
	 */
	public int modificar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException
	{
		return SqlBaseHijoBasicoDao.modificar (con, codigoPaciente, numeroEmbarazo, numeroHijo, vivo, otroTipoPartoVaginal, cesarea, aborto, otroTipoParto, tiposPartoVaginal, peso, sexo, lugar) ;
	}

	/**
	 * Implementación de la modificación de un hijo (antecedentes Gineco-
	 * Obstetricos), permitiendo definir el nivel de transaccionalidad para 
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#modificarTransaccional(Connection, int, int, int, boolean, String, boolean, boolean, String, ArrayList, String, String, int, String)
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException
	{
		return SqlBaseHijoBasicoDao.modificarTransaccional (con, codigoPaciente, numeroEmbarazo, numeroHijo, vivo, otroTipoPartoVaginal, cesarea, aborto, otroTipoParto, tiposPartoVaginal, estado, peso, sexo, lugar) ;
	}


	/**
	 * Implementación de cargar tipos de parto vaginal para 
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#cargarTiposPartoVaginal(Connection, int, int, int)
	 */
	public ResultSetDecorator cargarTiposPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException
	{
		return SqlBaseHijoBasicoDao.cargarTiposPartoVaginal (con, codigoPaciente, numeroEmbarazo, numeroHijo) ;
	}

	/**
	 * Implementación de la búsqueda de un hijo en la Base de Datos Oracle
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#existeHijo(Connection, int, int,
	 * int)
	 */
	public boolean existeHijo (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException
	{
		return SqlBaseHijoBasicoDao.existeHijo (con, codigoPaciente, numeroEmbarazo, numeroHijo) ;
	}

	/**
	 * Implementación de la búsqueda de un tipo de parto vaginal 
	 * en la Base de Datos Oracle
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#existeTipoPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, int codigoTipoParto) throws SQLException
	 */
	public boolean existeTipoPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, int codigoTipoParto) throws SQLException
	{
		return SqlBaseHijoBasicoDao.existeTipoPartoVaginal (con, codigoPaciente, numeroEmbarazo, numeroHijo, codigoTipoParto) ;
	}

}
