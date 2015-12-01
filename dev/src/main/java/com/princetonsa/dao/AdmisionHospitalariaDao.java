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
		 * M�todo que permite insertar una admisi�n hospitalaria
	 	 * @param con una conexi�n abierta con una fuente de datos
		 * @param origen Origen de la admisi�n
		 * @param codigoMedico C�digo del m�dico responsable
		 * de este ingreso
		 * @param codigoCama C�digo de la cama donde se va a internar al
		 * paciente
		 * @param codigoDiagnostico C�digo del diagnostico que justifica esta
		 * admisi�n hospitalaria
		 * @param codigoCIEDiagnostico C�digo CIE del diagnostico que justifica
		 * esta admisi�n hospitalaria
		 * @param codigoCausaExterna C�digo de la causa externa por la que se
		 * hospitaliz� al paciente
		 * @param numeroAutorizacion N�mero de autorizaci�n entregado por la
		 * EPS/IPS que permite la hospitalizaci�n del paciente
		 * @param loginUsuario Login del usuario que hace esta operaci�n
		 * @param cuenta C�digo de la cuenta a la que pertenece esta admisi�n
		 * @param hora Hora en la que se efectuo la hospitalizaci�n
		 * @return int con el n�mero de filas insertados (0 si no se puede
		 * insertar)
		 * @throws SQLException
		 */

		public int insertar(Connection con, int origen, int codigoMedico,int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, int cuenta, String hora, String fecha) throws SQLException;

		/**
		 * M�todo que permite insertar una admisi�n hospitalaria soportando
		 * Transaccionalidad definida por el usuario a trav�s del par�metro
		 * estado
		 * 
	 	 * @param con una conexi�n abierta con una fuente de datos
		 * @param origen Origen de la admisi�n
		 * @param codigoMedico C�digo del m�dico responsable
		 * de este ingreso
		 * @param codigoCama C�digo de la cama donde se va a internar al
		 * paciente
		 * @param codigoDiagnostico C�digo del diagnostico que justifica esta
		 * admisi�n hospitalaria
		 * @param codigoCIEDiagnostico C�digo CIE del diagnostico que justifica
		 * esta admisi�n hospitalaria
		 * @param codigoCausaExterna C�digo de la causa externa por la que se
		 * hospitaliz� al paciente
		 * @param numeroAutorizacion N�mero de autorizaci�n entregado por la
		 * EPS/IPS que permite la hospitalizaci�n del paciente
		 * @param loginUsuario Login del usuario que hace esta operaci�n
		 * @param cuenta C�digo de la cuenta a la que pertenece esta admisi�n
		 * @param hora Hora en la que se efectuo la hospitalizaci�n
	  	 * @param estado estado de la transaccion (empezar, continuar,
	  	 * finalizar)
		 * @return int con el n�mero de filas insertados (0 si no se puede
		 * insertar)
		 * @throws SQLException
		 */		
		public int insertarTransaccional(Connection con, int origen, int codigoMedico, int codigoCama, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */String loginUsuario, int cuenta, String hora, String fecha, String estado, int institucion) throws SQLException;
		
		/**
		 * M�todo que permite cargar una admisi�n hospitalaria
		 * 
		 * @param con una conexi�n abierta con una fuente de datos
		 * @param codigoAdmisionHospitalaria C�digo con el que quedo la admisi�n
		 * a cargar
		 * @return ResultSetDecorator con la informaci�n de la admisi�n hospitalaria
		 * @throws SQLException
		 */
		public ResultSetDecorator cargar(Connection con, int codigoAdmisionHospitalaria) throws SQLException;
		
		/**
		 * M�todo que permite modificar una admisi�n hospitalaria
		 * 
		 * @param con una conexi�n abierta con una fuente de datos
		 * @param origen Origen de la admisi�n
		 * @param codigoDiagnostico C�digo del diagnostico que justifica esta
		 * admisi�n hospitalaria
		 * @param codigoCIEDiagnostico C�digo CIE del diagnostico que justifica
		 * esta admisi�n hospitalaria
		 * @param codigoCausaExterna C�digo de la causa externa por la que se
		 * hospitaliz� al paciente
		 * @param numeroAutorizacion N�mero de autorizaci�n entregado por la
		 * EPS/IPS que permite la hospitalizaci�n del paciente
		 * @param codigoAdmisionHospitalaria C�digo con el que quedo la admisi�n
		 * a modificar
		 * @return int 0 si salio mal o si no el n�mero de admisiones
		 * modificadas
		 * @throws SQLException
		 */
		public int modificar(Connection con,  int origen, String codigoDiagnostico, String codigoCIEDiagnostico, int codigoCausaExterna, /*String numeroAutorizacion, */int codigoAdmisionHospitalaria) throws SQLException;

		/**
		 * M�todo que permite pasar una admisi�n de hospitalizaci�n a egreso. Para
		 * esto se debe 
		 * 1. Pasar la cama usada a estado de desinfecci�n.
		 * 2. Cambiar el estado de la admisi�n a egresada
		 * 
		 * Todo esto debe ser en modo transaccional
		 * 
		 * @param con una conexi�n abierta con una fuente de datos
		 * @param numeroAdmision C�digo con el que quedo la admisi�n
		 * a a la cual se le quiere liberar cama
		 * @param estado estado de la transaccion (empezar, continuar,
	  	 * finalizar)
		 * @return
		 * @throws SQLException
		 */
		public int pasarAdmisionAEgresoTransaccional (Connection con, int numeroAdmision, String estado, int institucion) throws SQLException;
		
		/**
		 * M�todo que permite hacer una reversi�n de egreso para una admisi�n de 
		 * hospitalizaci�n a egreso. Para esto se debe 
		 * 
		 * 1. Pasar la cama seleccionada a estado de ocupada.
		 * 2. Cambiar el estado de la admisi�n a hospitalizada
		 * 3. Actualizar la cama en la admisi�n
		 * 
		 * Todo esto debe ser en modo transaccional
		 * 
		 * @param con una conexi�n abierta con una fuente de datos
		 * @param idCuenta C�digo de la cuenta a la que pertenece
		 * esta admisi�n
		 * @param codigoCama C�digo de la cama que va a ocupar
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
		 * Carga en una cadena la fecha y hora de la �ltima asignaci�n o cambio
		 * de cama para el paciente dado.
		 * @param 	Connection, con
		 * @param 	int, codigoPaciente. C�digo del paciente
		 * @throws 	SQLException
		 */
		public String cargarUltimaFechaHoraRegistroCama(Connection con, int codigoPaciente) throws SQLException;
		
		/**
		 * M�todo que obtiene la informaci�n de la cama actual del paciente
		 * @param con
		 * @param codigoAdmision
		 * @param codigoPaciente
		 * @return
		 * @throws SQLException
		 */
		public String[] getCama(Connection con, int codigoAdmision, int codigoPaciente) throws SQLException;
		
		/**
		 * Carga informaci�n b�sica de la �ltima admisi�n del paciente
		 * @param 	Connection, con
		 * @param 	int, codigoPaciente
		 * @return 		ResultSet
		 * @throws SQLException
		 */		
		public ResultSetDecorator cargarUltimaAdmision(Connection con, int codigoPaciente) throws SQLException;
		
		/**
		 * M�todo para obtener los datos de la cama actual en la que est� el paciente 
		 * @param con
		 * @param codigoPaciente
		 * @return Collection -> Con los datos de la cama actual del paciente
		 */
		public Collection consultarDatosCamaActual(Connection con, int codigoPaciente);
		
}
