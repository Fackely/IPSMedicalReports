/*
 * @(#)SolicitudFarmaciaDao.java
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

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar 
 * la clase que presta el servicio de acceso a datos para el objeto 
 * <code>SolicitudFarmaciaDao</code>.
 * 
 *	@version 1.0, Sep 10, 2003
 */
public interface SolicitudFarmaciaDao 
{
	/**
	 * M�todo que se encarga de insertar una solicitud m�dica para un medicamento
	 * presente en la fuente de datos. Si el c�digo del centro de costo es 0, no debe
	 * insertar ese dato
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param codigoCentroCostoRespuesta C�digo del centro de costo
	 * @param codigoMedicamento C�digo del medicamento que compone esta
	 * solicitud
	 * @param tipoIdMedico Tipo de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param numIdMedico N�mero de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente N�mero de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmacia(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, int codigoCentroCostoRespuesta, int codigoMedicamento, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que se encarga de insertar una solicitud m�dica para un medicamento
	 * presente en la fuente de datos. Si el c�digo del centro de costo es 0, no debe
	 * insertar ese dato. Recibe un parametro estado (empezar, continuar, finalizar) que
	 * le permite hacer parte de una transacci�n m�s grande
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param codigoCentroCostoRespuesta C�digo del centro de costo
	 * @param codigoMedicamento C�digo del medicamento que compone esta
	 * solicitud
	 * @param tipoIdMedico Tipo de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param numIdMedico N�mero de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente N�mero de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmaciaTransaccional(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, int codigoCentroCostoRespuesta, int codigoMedicamento, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion, String estado) throws SQLException;
	
	/**
	 * M�todo que se encarga de insertar una solicitud m�dica para un medicamento
	 * NO presente en la fuente de datos. Si el c�digo del centro de costo es 0, no debe
	 * insertar ese dato
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param nombre Nombre del medicamento
	 * @param codigoCentroCostoRespuesta C�digo del centro de costo
	 * @param tipoIdMedico Tipo de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param numIdMedico N�mero de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente N�mero de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmaciaOtro(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, String nombre, int codigoCentroCostoRespuesta, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que se encarga de insertar una solicitud m�dica para un medicamento
	 * NO presente en la fuente de datos. Si el c�digo del centro de costo es 0, no debe
	 * insertar ese dato. Recibe un parametro estado (empezar, continuar, finalizar) que
	 * le permite hacer parte de una transacci�n m�s grande
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param nombre Nombre del medicamento
	 * @param codigoCentroCostoRespuesta C�digo del centro de costo
	 * @param tipoIdMedico Tipo de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param numIdMedico N�mero de Identificaci�n del m�dico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente N�mero de Identificaci�n del paciente para el que se pide
	 * esta solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmaciaOtroTransaccional(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, String nombre, int codigoCentroCostoRespuesta, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion, String estado) throws SQLException;
	
	/**
	 * M�todo que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del �ltmo cambio del texto de la 
	 * solicitud y el m�dico del �ltimo cambio) para una solicitud cuyo 
	 * medicamento est� en la fuente de datos
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param codigoNuevoCentroCosto C�digo del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitud (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto) throws SQLException;
	
	/**
	 * M�todo que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del �ltmo cambio del texto de la 
	 * solicitud y el m�dico del �ltimo cambio) para una solicitud cuyo 
	 * medicamento est� en la fuente de datos. Recibe un parametro 
	 * estado (empezar, continuar, finalizar) que le permite hacer parte de 
	 * una transacci�n m�s grande
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param codigoNuevoCentroCosto C�digo del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitudTransaccional (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto, String estado) throws SQLException;
	
	/**
	 * M�todo que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del �ltmo cambio del texto de la 
	 * solicitud y el m�dico del �ltimo cambio) para una solicitud cuyo 
	 * medicamento no est� en la fuente de datos
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param codigoNuevoCentroCosto C�digo del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitudOtros (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto) throws SQLException;
	
	/**
	 * M�todo que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del �ltmo cambio del texto de la 
	 * solicitud y el m�dico del �ltimo cambio) para una solicitud cuyo 
	 * medicamento no est� en la fuente de datos. Recibe un parametro 
	 * estado (empezar, continuar, finalizar) que le permite hacer parte de 
	 * una transacci�n m�s grande
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param codigoNuevoCentroCosto C�digo del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitudOtrosTransaccional (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto, String estado) throws SQLException;
	
	/**
	 * Este m�todo actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento este definido en la fuente de datos
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param numeroAutorizacion N�mero de autorizaci�n si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @param numIdMedico N�mero Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTextoSolicitudFarmacia (Connection con, int codigo, String numeroAutorizacion, String textoSolicitud, String tipoIdMedico, String numIdMedico) throws SQLException;
	
	/**
	 * Este m�todo actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento no este definido en la fuente de datos
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param numeroAutorizacion N�mero de autorizaci�n si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @param numIdMedico N�mero Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTextoSolicitudFarmaciaOtros (Connection con, int codigo, String numeroAutorizacion, String textoSolicitud, String tipoIdMedico, String numIdMedico) throws SQLException;

	/**
	 * Este m�todo actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento no este definido en la fuente de datos, usando el
	 * estandar para permitirlo hacer parte de una transacci�n m�s 
	 * grande
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param numeroAutorizacion N�mero de autorizaci�n si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @param numIdMedico N�mero Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTextoSolicitudFarmaciaOtrosTransaccional (Connection con, int codigo, String numeroAutorizacion, String textoSolicitud, String tipoIdMedico, String numIdMedico, String estado) throws SQLException;

	/**
	 * Este m�todo actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento este definido en la fuente de datos
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param numeroAutorizacion N�mero de autorizaci�n si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @param numIdMedico N�mero Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @return
	 * @throws SQLException
	 */
	public int actualizarResultadosSolicitudFarmacia (Connection con, int  codigo, String numeroAutorizacion, String textoResultados, String tipoIdProfesionalSalud, String numIdProfesionalSalud) throws SQLException;

	/**
	 * Este m�todo actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento este definido en la fuente de datos, usando el
	 * estandar para permitirlo hacer parte de una transacci�n m�s 
	 * grande
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigo C�digo de la solicitud a modificar
	 * @param numeroAutorizacion N�mero de autorizaci�n si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @param numIdMedico N�mero Identificaci�n del m�dico que actualiz�
	 * el texto
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTextoSolicitudFarmaciaTransaccional (Connection con, int codigo, String numeroAutorizacion, String textoSolicitud, String tipoIdMedico, String numIdMedico, String estado) throws SQLException;
	public int actualizarInterpretacionSolicitudFarmaciaTransaccional (Connection con, int codigo, String numeroAutorizacion, String textoInterpretacion, String tipoIdMedico, String numIdMedico, String estado) throws SQLException;
	public int actualizarResultadosSolicitudFarmaciaOtros (Connection con, int  codigo, String numeroAutorizacion, String textoResultados, String tipoIdProfesionalSalud, String numIdProfesionalSalud) throws SQLException;
	public int actualizarResultadosSolicitudFarmaciaOtrosTransaccional (Connection con, int  codigo, String numeroAutorizacion, String textoResultados, String tipoIdProfesionalSalud, String numIdProfesionalSalud, String estado) throws SQLException;
	public int actualizarInterpretacionSolicitudFarmacia (Connection con, int codigo, String numeroAutorizacion, String textoInterpretacion, String tipoIdMedico, String numIdMedico) throws SQLException;	
	public int actualizarResultadosSolicitudFarmaciaTransaccional (Connection con, int  codigo, String numeroAutorizacion, String textoResultados, String tipoIdProfesionalSalud, String numIdProfesionalSalud, String estado) throws SQLException;
	public int actualizarInterpretacionSolicitudFarmaciaOtros (Connection con, int codigo, String numeroAutorizacion, String textoInterpretacion, String tipoIdMedico, String numIdMedico) throws SQLException;
	public int actualizarInterpretacionSolicitudFarmaciaOtrosTransaccional (Connection con, int codigo, String numeroAutorizacion, String textoInterpretacion, String tipoIdMedico, String numIdMedico, String estado) throws SQLException;

	public ResultSetDecorator cargarSolicitudFarmacia (Connection con, int codigoSolicitud) throws SQLException;
	public ResultSetDecorator cargarSolicitudFarmaciaOtros (Connection con, int codigoSolicitud) throws SQLException;
}
