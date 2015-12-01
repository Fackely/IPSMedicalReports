package com.princetonsa.dao.facturacion;
import java.sql.Connection;
import java.util.HashMap;

import util.facturacion.InfoCobertura;

import com.princetonsa.mundo.facturacion.Cobertura;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com    
 * */
public interface CoberturaDao
{
	/**
	 * Insertar un registro de coberturas
	 * @param con
	 * @param Cobertura cobertura
	 * */
	public boolean insertarCobertura(Connection con, Cobertura cobertura);
	
	
	/**
	 * Modifica una cobertura registrada
	 * @param con
	 * @param Cobertura cobertura
	 * */
	public boolean modificarCobertura(Connection con, Cobertura cobertura);
	
	
	/**
	 * Elimina una cobertura registrada
	 * @param con
	 * @param String codigoCobertura
	 * @param int institucion 
	 * */
	
	public boolean eliminarCobertura(Connection con, String codigoCobertura, int institucion);
	
	
	/**
	 * Consulta basica de coberturas por keys
	 * @param con
	 * @param codigoCobertura
	 * @param institucion
	 * */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCoberturaBasica(Connection con, String codigoCobertura, int institucion);
	
	
	/**
	 * Consulta avanzada de coberturas por cada uno de los campos
	 * @param con
	 * @param  
	 * */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCoberturaAvanzada(Connection con,HashMap condicion);
	
	/**
	 * Verifica la existencia del Codigo de Cobertura dentro de otra funcionalidad
	 * @param Connection con
	 * @param String CodigoCobertura 
	 * */
	public boolean verificarDependencia(Connection con, String codigoCobertura);
	
	/**
	 * metodo estatico que evalua la cobertura o no de un servicio para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoCobertura validacionCoberturaServicioEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException;
	
	/**
	 * metodo estatico que evalua la cobertura o no de un servicio para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoServicio
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoCobertura validacionCoberturaServicio(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoServicio, int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException;
	
	/**
	 * metodo estatico que evalua la cobertura o no de un ARTICULO para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoCobertura validacionCoberturaArticuloEntidadSub(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException;
	
	/**
	 * metodo estatico que evalua la cobertura o no de un ARTICULO para un contrato - via ingreso - naturaleza paciente - institucion, 
	 * devuelve un objeto InfoCobertura que contiene los siguientes atributos:
	 * 
	 * -->existe 	= indica si encontro o no parametrizacion de la cobertura -> BOOLEAN
	 * -->incluido 	= indica si esta o no incluido	-> BOOLEAN
	 * -->requiereAutorizacion = indica si requiere o no aut -> BOOLEAN
	 * -->semanasMinimasCotizacion = -> INT  
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param codigoArticulo
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoCobertura validacionCoberturaArticulo(Connection con, long codigoContrato, int codigoViaIngreso, String tipoPaciente, int codigoArticulo, int codigoNaturalezaPaciente, int codigoInstitucion ) throws BDException;


	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @param codigoViaIngreso
	 * @param tipoPaciente
	 * @param codigoServicioExcluyente
	 * @param codigoProgramaExcluyente
	 * @param codigoNaturalezaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoCobertura validacionCoberturaPrograma(Connection con,
			long codigoContrato, int codigoViaIngreso, String tipoPaciente,
			Double codigoPrograma,int codigoNaturalezaPaciente, int codigoInstitucion) throws BDException;

}