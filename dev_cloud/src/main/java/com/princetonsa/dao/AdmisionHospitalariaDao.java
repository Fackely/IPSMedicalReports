package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.decorator.ResultSetDecorator;



/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>AdmisionHospitalaria</code>.
 *
 * @version 1.0, Mar 4, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 */
public interface AdmisionHospitalariaDao 
{
		/**
		 * Método que permite insertar una admisión hospitalaria
	 	 * @param con una conexión abierta con una fuente de datos
		 * @param origen Origen de la admisión
		 * @param codigoMedico Código del médico responsable
		 * de este ingreso
		 * @param codigoCama Código de la cama donde se va a internar al
		 * paciente
		 * @param codigoDiagnostico Código del diagnostico que justifica esta
		 * admisión hospitalaria
		 * @param codigoCIEDiagnostico Código CIE del diagnostico que justifica
		 * esta admisión hospitalaria
		 * @param codigoCausaExterna Código de la causa externa por la que se
		 * hospitalizó al paciente
		 * @param numeroAutorizacion Número de autorización entregado por la
		 * EPS/IPS que permite la hospitalización del paciente
		 * @param loginUsuario Login del usuario que hace esta operación
		 * @param cuenta Código de la cuenta a la que pertenece esta admisión
		 * @param hora Hora en la que se efectuo la hospitalización
		 * @return int con el número de filas insertados (0 si no se puede
		 * insertar)
		 * @throws SQLException
		 */

		public int insertar(Connection con, int origen, int codigoMedico,int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, int cuenta, String hora, String fecha) throws SQLException;

		/**
		 * Método que permite insertar una admisión hospitalaria soportando
		 * Transaccionalidad definida por el usuario a través del paràmetro
		 * estado
		 * 
	 	 * @param con una conexión abierta con una fuente de datos
		 * @param origen Origen de la admisión
		 * @param codigoMedico Código del médico responsable
		 * de este ingreso
		 * @param codigoCama Código de la cama donde se va a internar al
		 * paciente
		 * @param codigoDiagnostico Código del diagnostico que justifica esta
		 * admisión hospitalaria
		 * @param codigoCIEDiagnostico Código CIE del diagnostico que justifica
		 * esta admisión hospitalaria
		 * @param codigoCausaExterna Código de la causa externa por la que se
		 * hospitalizó al paciente
		 * @param numeroAutorizacion Número de autorización entregado por la
		 * EPS/IPS que permite la hospitalización del paciente
		 * @param loginUsuario Login del usuario que hace esta operación
		 * @param cuenta Código de la cuenta a la que pertenece esta admisión
		 * @param hora Hora en la que se efectuo la hospitalización
	  	 * @param estado estado de la transaccion (empezar, continuar,
	  	 * finalizar)
		 * @return int con el número de filas insertados (0 si no se puede
		 * insertar)
		 * @throws SQLException
		 */		
		public int insertarTransaccional(Connection con, int origen, int codigoMedico, int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, int cuenta, String hora, String fecha, String estado, int institucion) throws SQLException;
		
		/**
		 * Método que permite cargar una admisión hospitalaria
		 * 
		 * @param con una conexión abierta con una fuente de datos
		 * @param codigoAdmisionHospitalaria Código con el que quedo la admisión
		 * a cargar
		 * @return ResultSetDecorator con la información de la admisión hospitalaria
		 * @throws SQLException
		 */
		public ResultSetDecorator cargar(Connection con, int codigoAdmisionHospitalaria) throws SQLException;
		
		/**
		 * Método que permite modificar una admisión hospitalaria
		 * 
		 * @param con una conexión abierta con una fuente de datos
		 * @param origen Origen de la admisión
		 * @param codigoDiagnostico Código del diagnostico que justifica esta
		 * admisión hospitalaria
		 * @param codigoCIEDiagnostico Código CIE del diagnostico que justifica
		 * esta admisión hospitalaria
		 * @param codigoCausaExterna Código de la causa externa por la que se
		 * hospitalizó al paciente
		 * @param numeroAutorizacion Número de autorización entregado por la
		 * EPS/IPS que permite la hospitalización del paciente
		 * @param codigoAdmisionHospitalaria Código con el que quedo la admisión
		 * a modificar
		 * @return int 0 si salio mal o si no el número de admisiones
		 * modificadas
		 * @throws SQLException
		 */
		public int modificar(Connection con,  int origen, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */int codigoAdmisionHospitalaria) throws SQLException;

		/**
		 * Método que permite pasar una admisión de hospitalización a egreso. Para
		 * esto se debe 
		 * 1. Pasar la cama usada a estado de desinfección.
		 * 2. Cambiar el estado de la admisión a egresada
		 * 
		 * Todo esto debe ser en modo transaccional
		 * 
		 * @param con una conexión abierta con una fuente de datos
		 * @param numeroAdmision Código con el que quedo la admisión
		 * a a la cual se le quiere liberar cama
		 * @param estado estado de la transaccion (empezar, continuar,
	  	 * finalizar)
		 * @return
		 * @throws SQLException
		 */
		public int pasarAdmisionAEgresoTransaccional (Connection con, int numeroAdmision, String estado, int institucion) throws SQLException;
		
		/**
		 * Método que permite hacer una reversión de egreso para una admisión de 
		 * hospitalización a egreso. Para esto se debe 
		 * 
		 * 1. Pasar la cama seleccionada a estado de ocupada.
		 * 2. Cambiar el estado de la admisión a hospitalizada
		 * 3. Actualizar la cama en la admisión
		 * 
		 * Todo esto debe ser en modo transaccional
		 * 
		 * @param con una conexión abierta con una fuente de datos
		 * @param idCuenta Código de la cuenta a la que pertenece
		 * esta admisión
		 * @param codigoCama Código de la cama que va a ocupar
		 * el paciente
		 * @param estado estado de la transaccion (empezar, continuar,
	  	 * finalizar)
		 * @return
		 * @throws SQLException
		 */
		public int reversarEgresoYAdmisionTransaccional (Connection con, int idCuenta, int codigoCama, String estado) throws SQLException;
		
		/**
		 * Ingresa una cama a una admision
		 * @param 	Connection, con
		 * @param 	int, Codigo Cama
		 * @param 	int, Codigo Admision
		 */
		public int cambiarCama(Connection con, int cama, int admision);
		
		/**
		 * 
		 * @param con
		 * @param medico
		 * @param admision
		 * @return
		 */
		public int actualizarMedico(Connection con, int medico, int admision);
		
		/**
		 * 
		 * @param con
		 * @param numeroCuenta
		 * @return
		 * @throws SQLException
		 */
		public ResultSetDecorator getBasicoAdmision(Connection con, int numeroCuenta) throws SQLException;		

		/**
		 * Carga en una cadena la fecha y hora de la última asignación o cambio
		 * de cama para el paciente dado.
		 * @param 	Connection, con
		 * @param 	int, codigoPaciente. Código del paciente
		 * @throws 	SQLException
		 */
		public String cargarUltimaFechaHoraRegistroCama(Connection con, int codigoPaciente) throws SQLException;
		
		/**
		 * Método que obtiene la información de la cama actual del paciente
		 * @param con
		 * @param codigoAdmision
		 * @param codigoPaciente
		 * @return
		 * @throws SQLException
		 */
		public String[] getCama(Connection con, int codigoAdmision, int codigoPaciente) throws SQLException;
		
		/**
		 * Carga información básica de la última admisión del paciente
		 * @param 	Connection, con
		 * @param 	int, codigoPaciente
		 * @return 		ResultSet
		 * @throws SQLException
		 */		
		public ResultSetDecorator cargarUltimaAdmision(Connection con, int codigoPaciente) throws SQLException;
		
		/**
		 * Método para obtener los datos de la cama actual en la que está el paciente 
		 * @param con
		 * @param codigoPaciente
		 * @return Collection -> Con los datos de la cama actual del paciente
		 */
		public Collection consultarDatosCamaActual(Connection con, int codigoPaciente);
		
}
