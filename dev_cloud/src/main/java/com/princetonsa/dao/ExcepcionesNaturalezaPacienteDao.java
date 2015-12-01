/*
 * ExcepcionesNaturalezaPacienteDao.java 
 * Autor			:  mdiaz
 * Creado el	:  17-ago-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import util.ResultadoBoolean;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 17-ago-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public interface ExcepcionesNaturalezaPacienteDao {

	public int insertar(Connection con, String codigoRegimen, int codigoNaturaleza, boolean indicativoExcepcion);
	
	public int modificar(Connection con, int codigo, String codigoRegimen, int codigoNaturaleza, boolean indicativoExcepcion);
	
	public int eliminar(Connection con, int codigo);
	
	public HashMap buscar(Connection con, String[] selectedColumns, String where, String orderBy);
	
	public HashMap buscar(Connection con, int codigo);

	/*
	 * *************************************** OJO *********************************************
	 * @author Juan David Ram�rez L�pez
	 *
	 * Princeton S.A.
	 * 
	 * Este m�todo es utilizado por facturaci�n, favor consultar para su modificaci�n
	 * Atte: Juan David
	 */
	/**
	 * M�todo para buscar la excepcion de farmacia de acuerdo
	 * al tipo de regimen y la naturaleza del paciente
	 * @param con Conexi�n con la BD
	 * @param tipoRegimen Tipo del regimen del paciente
	 * @param naturalezaPaciente Naturaleza del paciente
	 * @return true si tiene excepci�n, false de lo contrario, false con descripci�n
	 * (Codificada ApplicationReources) en caso de error
	 */
	public ResultadoBoolean buscarExcepcionPorNaturaleza(Connection con, String tipoRegimen, int naturalezaPaciente);

}
