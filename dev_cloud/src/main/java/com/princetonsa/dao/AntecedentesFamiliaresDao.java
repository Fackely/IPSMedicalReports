package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.decorator.ResultSetDecorator;







public interface AntecedentesFamiliaresDao
{
	/***
	 * Este método inserta los tipos predefinidos de antecedentes familiares que
	 * han sido chequeados por el usuario y las observaciones específicas a cada
	 * tipo
	 */
	public int insertarFamiliaresTipos (Connection con, int codigoPaciente, int tipoEnfFamiliar, String observaciones, String parentesco) throws SQLException;
	


	/**
	 * Este método inserta otros tipos de antecedentes familiares, específicos
	 * para este paciente
	 */
	public int  insertarFamiliaresOtros (Connection con, int codigoPaciente, int codigo, String nombre,String observaciones, String parentesco)throws SQLException;
	
		
	/**
	 * Este método inserta, las observaciones generales a todo el formulario de
	 * Antecedentes Familiares
	 */
	public int insertarFamiliares(Connection con, int codigoPaciente, String observacionesGenerales)throws SQLException;

	
	
	
	/**
	 *  Este   método carga desde la bd  los tipos predefinidos para
	 * antecedentes familiares
	 */
	public ResultSetDecorator cargarFamiliaresTipos (Connection con, int codigoPaciente)throws SQLException;
	


	/** 
	 * Este método carga desde la bd otros tipos de antecedentes familiares que hayan sido insertados para ese paciente
	 */
	
	public ResultSetDecorator  cargarFamiliaresOtros (Connection con, int codigoPaciente)throws SQLException;
	
		
	/**
	 * Este método carga desde la bd , las observaciones generales a todo el
	 * formulario de Antecedentes Familiares
	 */
	public  ResultSetDecorator cargarFamiliares(Connection con, int codigoPaciente)throws SQLException;


	public int modificarFamiliaresTipos (Connection con, int codigoPaciente, int tipoEnfFamiliar, String observaciones)throws SQLException;
		
	public int modificarFamiliares(Connection con, int codigoPaciente, String observacionesGenerales)throws SQLException;
	
	public int  modificarFamiliaresOtros(Connection con, int codigoPaciente, int codigo,String observaciones)throws SQLException;
}
