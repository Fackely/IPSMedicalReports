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
	 * Método que se encarga de insertar una solicitud médica para un medicamento
	 * presente en la fuente de datos. Si el código del centro de costo es 0, no debe
	 * insertar ese dato
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param codigoCentroCostoRespuesta Código del centro de costo
	 * @param codigoMedicamento Código del medicamento que compone esta
	 * solicitud
	 * @param tipoIdMedico Tipo de Identificación del médico que crea esta
	 * solicitud
	 * @param numIdMedico Número de Identificación del médico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente Número de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmacia(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, int codigoCentroCostoRespuesta, int codigoMedicamento, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que se encarga de insertar una solicitud médica para un medicamento
	 * presente en la fuente de datos. Si el código del centro de costo es 0, no debe
	 * insertar ese dato. Recibe un parametro estado (empezar, continuar, finalizar) que
	 * le permite hacer parte de una transacción más grande
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param codigoCentroCostoRespuesta Código del centro de costo
	 * @param codigoMedicamento Código del medicamento que compone esta
	 * solicitud
	 * @param tipoIdMedico Tipo de Identificación del médico que crea esta
	 * solicitud
	 * @param numIdMedico Número de Identificación del médico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente Número de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmaciaTransaccional(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, int codigoCentroCostoRespuesta, int codigoMedicamento, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion, String estado) throws SQLException;
	
	/**
	 * Método que se encarga de insertar una solicitud médica para un medicamento
	 * NO presente en la fuente de datos. Si el código del centro de costo es 0, no debe
	 * insertar ese dato
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param nombre Nombre del medicamento
	 * @param codigoCentroCostoRespuesta Código del centro de costo
	 * @param tipoIdMedico Tipo de Identificación del médico que crea esta
	 * solicitud
	 * @param numIdMedico Número de Identificación del médico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente Número de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmaciaOtro(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, String nombre, int codigoCentroCostoRespuesta, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion) throws SQLException;
	
	/**
	 * Método que se encarga de insertar una solicitud médica para un medicamento
	 * NO presente en la fuente de datos. Si el código del centro de costo es 0, no debe
	 * insertar ese dato. Recibe un parametro estado (empezar, continuar, finalizar) que
	 * le permite hacer parte de una transacción más grande
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param textoSolicitud Texto de la solicitud
	 * @param nombre Nombre del medicamento
	 * @param codigoCentroCostoRespuesta Código del centro de costo
	 * @param tipoIdMedico Tipo de Identificación del médico que crea esta
	 * solicitud
	 * @param numIdMedico Número de Identificación del médico que crea esta
	 * solicitud
	 * @param tipoIdPaciente Tipo de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @param numIdPaciente Número de Identificación del paciente para el que se pide
	 * esta solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudFarmaciaOtroTransaccional(Connection con, int idCuenta, String textoSolicitud, String numeroAutorizacion, String nombre, int codigoCentroCostoRespuesta, String tipoIdMedico, String numIdMedico,String tipoIdPaciente, String numIdPaciente, String codigoInstitucion, String estado) throws SQLException;
	
	/**
	 * Método que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del últmo cambio del texto de la 
	 * solicitud y el médico del último cambio) para una solicitud cuyo 
	 * medicamento esté en la fuente de datos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param codigoNuevoCentroCosto Código del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitud (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto) throws SQLException;
	
	/**
	 * Método que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del últmo cambio del texto de la 
	 * solicitud y el médico del último cambio) para una solicitud cuyo 
	 * medicamento esté en la fuente de datos. Recibe un parametro 
	 * estado (empezar, continuar, finalizar) que le permite hacer parte de 
	 * una transacción más grande
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param codigoNuevoCentroCosto Código del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitudTransaccional (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto, String estado) throws SQLException;
	
	/**
	 * Método que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del últmo cambio del texto de la 
	 * solicitud y el médico del último cambio) para una solicitud cuyo 
	 * medicamento no esté en la fuente de datos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param codigoNuevoCentroCosto Código del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitudOtros (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto) throws SQLException;
	
	/**
	 * Método que se encarga de cambiar el centro de costo de esta solicitud y 
	 * los datos asociados (fecha y hora del últmo cambio del texto de la 
	 * solicitud y el médico del último cambio) para una solicitud cuyo 
	 * medicamento no esté en la fuente de datos. Recibe un parametro 
	 * estado (empezar, continuar, finalizar) que le permite hacer parte de 
	 * una transacción más grande
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param codigoNuevoCentroCosto Código del nuevo centro costo
	 * que puede llenar resultados de la solicitud
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int cambiarCentroCostoSolicitudOtrosTransaccional (Connection con, int codigo, String numeroAutorizacion, int codigoNuevoCentroCosto, String estado) throws SQLException;
	
	/**
	 * Este método actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento este definido en la fuente de datos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param numeroAutorizacion Número de autorización si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificación del médico que actualizó
	 * el texto
	 * @param numIdMedico Número Identificación del médico que actualizó
	 * el texto
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTextoSolicitudFarmacia (Connection con, int codigo, String numeroAutorizacion, String textoSolicitud, String tipoIdMedico, String numIdMedico) throws SQLException;
	
	/**
	 * Este método actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento no este definido en la fuente de datos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param numeroAutorizacion Número de autorización si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificación del médico que actualizó
	 * el texto
	 * @param numIdMedico Número Identificación del médico que actualizó
	 * el texto
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTextoSolicitudFarmaciaOtros (Connection con, int codigo, String numeroAutorizacion, String textoSolicitud, String tipoIdMedico, String numIdMedico) throws SQLException;

	/**
	 * Este método actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento no este definido en la fuente de datos, usando el
	 * estandar para permitirlo hacer parte de una transacción más 
	 * grande
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param numeroAutorizacion Número de autorización si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificación del médico que actualizó
	 * el texto
	 * @param numIdMedico Número Identificación del médico que actualizó
	 * el texto
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int actualizarTextoSolicitudFarmaciaOtrosTransaccional (Connection con, int codigo, String numeroAutorizacion, String textoSolicitud, String tipoIdMedico, String numIdMedico, String estado) throws SQLException;

	/**
	 * Este método actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento este definido en la fuente de datos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param numeroAutorizacion Número de autorización si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificación del médico que actualizó
	 * el texto
	 * @param numIdMedico Número Identificación del médico que actualizó
	 * el texto
	 * @return
	 * @throws SQLException
	 */
	public int actualizarResultadosSolicitudFarmacia (Connection con, int  codigo, String numeroAutorizacion, String textoResultados, String tipoIdProfesionalSalud, String numIdProfesionalSalud) throws SQLException;

	/**
	 * Este método actualiza el texto de una solicitud de farmacia cuyo
	 * medicamento este definido en la fuente de datos, usando el
	 * estandar para permitirlo hacer parte de una transacción más 
	 * grande
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código de la solicitud a modificar
	 * @param numeroAutorizacion Número de autorización si existe
	 * @param textoSolicitud Texto de la solicitud
	 * @param tipoIdMedico Tipo Identificación del médico que actualizó
	 * el texto
	 * @param numIdMedico Número Identificación del médico que actualizó
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
