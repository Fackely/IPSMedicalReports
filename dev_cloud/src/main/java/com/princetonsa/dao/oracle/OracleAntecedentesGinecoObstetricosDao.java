/*
 * @(#)OracleAntecedentesGinecoObstetricosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.AntecedentesGinecoObstetricosDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesGinecoObstetricosDao;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta clase implementa el contrato estipulado en
 * <code>AntecedentesGinecoObstetricosDao</code>, y presta los servicios de acceso
 * a una base de datos Oracle requeridos por la clase <code>AntecedenteGinecoObstetrico</code>.
 * 
 * @version 1.0, Apr 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
@SuppressWarnings("rawtypes")
public class OracleAntecedentesGinecoObstetricosDao implements AntecedentesGinecoObstetricosDao
{

	/**
	 * Implementación de la inserción de un Antecedente GinecoObstetrico para
	 * una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosDao#insertar(Connection, String, String, int, String, int, String, String, String)
	 */
	public int insertar (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos) throws SQLException
	{
		String secuenciaMetodosAnticonceptivos="seq_met_anticon_ant_go.nextval";
		return SqlBaseAntecedentesGinecoObstetricosDao.insertar (con, codigoPaciente, codigoEdadMenarquia, otraEdadMenarquia, codigoEdadMenopausia, otraEdadMenopausia, observaciones, inicVidaSexual, inicVidaObstetrica, loginUsuario, metodosAnticonceptivos, secuenciaMetodosAnticonceptivos) ;
	}

	/**
	 * Implementación de la inserción de un Antecedente GinecoObstetrico para
	 * una BD Oracle (Definiendo Transaccionalidad)
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosDao#insertarTransaccional(Connection, String, String, int, String, int, String, String, String, String)
	 */
	public int insertarTransaccional (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos, String estado) throws SQLException
	{
		String secuenciaMetodosAnticonceptivos="seq_met_anticon_ant_go.nextval";
		return SqlBaseAntecedentesGinecoObstetricosDao.insertarTransaccional (con, codigoPaciente, codigoEdadMenarquia, otraEdadMenarquia, codigoEdadMenopausia, otraEdadMenopausia, observaciones, inicVidaSexual, inicVidaObstetrica, loginUsuario, metodosAnticonceptivos, secuenciaMetodosAnticonceptivos, estado) ;
	}

	/**
	 * Implementación de la búsqueda de antecendentes ginecoobstetricos para una
	 * base de datos Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricosDao#existeAntecedente(Connection, String, String)
	 */
	public boolean existeAntecedente (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.existeAntecedente (con, codigoPaciente) ;
	}

	

	/**
	 * Implementación de la modificación transaccional de antecedente
	 * ginecoobstetrico para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#modificarTransaccional (Connection , int , int , String , int , String , String , int , int , String , ArrayList , String, String, String, String ) throws SQLException
	 */
	public int modificarTransaccional (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos, String estado) throws SQLException
	{
		String secuenciaMetodosAnticonceptivos="seq_met_anticon_ant_go.nextval";
		return SqlBaseAntecedentesGinecoObstetricosDao.modificarTransaccional (con, codigoPaciente, codigoEdadMenarquia, otraEdadMenarquia, codigoEdadMenopausia, otraEdadMenopausia, observaciones, inicVidaSexual, inicVidaObstetrica, loginUsuario, metodosAnticonceptivos,secuenciaMetodosAnticonceptivos, estado) ;
	}

	/**
	 * Implementación de la modificación transaccional de antecedente
	 * ginecoobstetrico para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#modificar (Connection , int , int , String , int , String , String , int , int , String , ArrayList ) throws SQLException
	 */
	public int modificar (Connection con, int codigoPaciente, int codigoEdadMenarquia, String otraEdadMenarquia, int codigoEdadMenopausia, String otraEdadMenopausia, String observaciones, int inicVidaSexual, int inicVidaObstetrica, String loginUsuario, ArrayList metodosAnticonceptivos) throws SQLException
	{
		String secuenciaMetodosAnticonceptivos="seq_met_anticon_ant_go.nextval";
		return SqlBaseAntecedentesGinecoObstetricosDao.modificar (con, codigoPaciente, codigoEdadMenarquia, otraEdadMenarquia, codigoEdadMenopausia, otraEdadMenopausia, observaciones, inicVidaSexual, inicVidaObstetrica, loginUsuario, metodosAnticonceptivos, secuenciaMetodosAnticonceptivos) ;
	}

	

	/**
	 * Implementación de la revisión si existe antecedente de paciente
	 * para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#existeAntecedentePaciente(Connection , int ) throws SQLException
	 */
	public boolean existeAntecedentePaciente(Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.existeAntecedentePaciente(con, codigoPaciente) ;
	}

	/**
	 * Implementación de cargar antecedente ginecoobstetrico 
	 * para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#existeAntecedentePaciente(Connection , int ) throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, int codigoPersona) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargar(con, codigoPersona) ;
	}

	
	
	/**
	 * Implementación de cargar el histórico de antecedente ginecoobstetrico 
	 * para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarHistorico (Connection , int, int ) throws SQLException
	 */
	public ResultSetDecorator cargarHistorico (Connection con, int codigoPaciente, int numeroSolicitud) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarHistorico (con, codigoPaciente, numeroSolicitud) ;
	}

	/**
	 * Implementación de cargar embarazos 
	 * para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarEmbarazos (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator cargarEmbarazos (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarEmbarazos (con, codigoPaciente) ;
	}

	/**
	 * Implementación de cargar métodos anticonceptivos 
	 * para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarMetodosAnticonceptivos (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator cargarMetodosAnticonceptivos (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarMetodosAnticonceptivos (con, codigoPaciente) ;
	}
 
	/**
	 * Implementación de cargar la última información sobre embarazos 
	 * para una BD Oracle
	 * 
	 * @see com.princetonsa.dao.AntecedentesGinecoObstetricos#cargarUltInformacionEmbarazos (Connection , int ) throws SQLException
	 */
	public ResultSetDecorator cargarUltInformacionEmbarazos (Connection con, int codigoPaciente) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarUltInformacionEmbarazos (con, codigoPaciente) ;
	} 

	/**
	 * Implementación de cargar complicaciones de los embarazos 
	 * para una BD Genérica
	 */
	public Collection cargarComplicaciones(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarComplicaciones(con, codigoPaciente, codigoEmbarazo);
	}

	/**
	 * Implementación de cargar complicaciones otras de los embarazos 
	 * para una BD Genérica
	 */
	public Collection cargarComplicacionesOtras(Connection con, int codigoPaciente, int codigoEmbarazo)
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarComplicacionesOtras(con, codigoPaciente, codigoEmbarazo);
	}

	/**
	 * Método que inserta los datos propios de la valoración gineco-obstetrica
	 * @param con
	 * @param edadMenarquia
	 * @param edadMenopausia
	 * @param otraEdadMenarquia
	 * @param otraEdadMenopausia
	 * @param cilcoMenstrual
	 * @param duracionMenstruacion
	 * @param furAnt
	 * @param dolorMenstruacion
	 * @param conceptoMenstruacion
	 * @param observacionesMenstruacion
	 * @param codigoPersona
	 * @param numeroSolicitud
	 */
	public int inseretarDatosHistMenstrual(Connection con, int edadMenarquia, int edadMenopausia, String otraEdadMenarquia, String otraEdadMenopausia, String cilcoMenstrual, String duracionMenstruacion, String furAnt, String dolorMenstruacion, int conceptoMenstruacion, String observacionesMenstruacion, int codigoPersona, int numeroSolicitud)
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.inseretarDatosHistMenstrual(con, edadMenarquia, edadMenopausia, otraEdadMenarquia, otraEdadMenopausia, cilcoMenstrual, duracionMenstruacion, furAnt, dolorMenstruacion, conceptoMenstruacion, observacionesMenstruacion, codigoPersona, numeroSolicitud);
	}
	
	
	/**
	 * Metodo para cargar Resumen de Atenciones.
	 * @param con
	 * @param mapa
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargar(Connection con, HashMap mapa) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargar(con, mapa);
	}

	/**
	 * Metodo para cargar Resumen de Atenciones.
	 * @param con
	 * @param mapa
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarHistorico (Connection con, HashMap mapa) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarHistorico (con, mapa) ;
	}
	
	
	/**
	 * Para resumen de Atenciones.
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarEmbarazos (Connection con, HashMap mapa) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarEmbarazos (con, mapa) ;
	}

	/**
	 * Para Resumen de Atenciones 
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return
	 */
	public Collection cargarComplicaciones(Connection con, HashMap mapa)
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarComplicaciones(con, mapa);
	}
	
	/**
	 * Para Resumen de Atenciones.
	 * @param con
	 * @param codigoPaciente
	 * @param codigoEmbarazo
	 * @return
	 */
	public Collection cargarComplicacionesOtras(Connection con,  HashMap mapa)
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarComplicacionesOtras(con, mapa);
	}
	
	/**
	 * Para historia de Anteciones 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarMetodosAnticonceptivos (Connection con, HashMap mapa) throws SQLException
	{
		return SqlBaseAntecedentesGinecoObstetricosDao.cargarMetodosAnticonceptivos (con, mapa);
	}
}