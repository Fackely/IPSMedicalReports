package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

/**
 * @author rcancino
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface SalarioMinimoDao
{
	public ResultadoBoolean insertar(Connection con,double salario,String fechaInicial,String fechaFinal) throws SQLException;
	public boolean existeCruzeSalario(Connection con,String fechaInicial,String fechaFinal) throws SQLException;
	public ResultadoCollectionDB consultar(Connection con) throws SQLException;
	public ResultadoBoolean modificar(	Connection con,int codigo,double salario,String fechaInicial,String fechaFinal) throws SQLException;
	public ResultadoCollectionDB consultarVigente (Connection con) throws SQLException;
	
	
}
