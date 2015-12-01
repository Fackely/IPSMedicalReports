/*
 * @(#)PostgresqlAntecedenteMorbidoMedicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;

/**
 * Interfaz para acceder a la fuente de datos, la parte de Antecedentes Mórbidos
 * Quirurgicos
 *
 * @version 1.0, Agosto 12, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface AntecedenteMorbidoQuirurgicoDao
{
	
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, int codigo, String nombre, String fecha, String causa, String complicaciones, String recomendaciones, String observaciones, String estado)throws SQLException;
	
	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, int codigo, String fecha, String causa, String complicaciones, String recomendaciones, String observaciones, String estado) throws SQLException;
	
	public ResultadoBoolean existeAntecedente(Connection con, int codigoPaciente, int codigo);

}
