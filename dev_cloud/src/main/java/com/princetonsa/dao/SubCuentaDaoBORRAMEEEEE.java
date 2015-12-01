/*
 * Creado en 23/11/2004
 *
 * Princeton S.A.
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.princetonsa.decorator.ResultSetDecorator;


/**
 * @author Juan David Ram�rez L�pez
 *
 * Princeton S.A.
 */
public interface SubCuentaDaoBORRAMEEEEE
{
	/**
	 * M�todo que carga los datos de una subcuenta
	 * @param con Conexi�n con la Base de Datos
	 * @param idSubcuenta Codigo de la subcuenta que se desea cargar
	 * @return Colecci�n con todos los datos de una subcuenta
	 */
	public Collection cargar(Connection con, int idSubcuenta);
	
	/**
	 * M�todo que inserta una subcuenta en el sistema
	 * @param con Conexi�n con la BD
	 * @param convenio Convenio del paciente
	 * @param tipoPaciente
	 * @param codigoContrato 
	 * @param naturaleza
	 * @param montoCobro
	 * @param poliza N�mero de poliza
	 * @param carnet N�mero de carnet
	 * @param idCuentaMadre Id de la cuenta que fue distribuida
	 * @param usuario Usuario que hizo la distribuci�n
	 * @param esUnida Boolean que indica si esta subcuenta es
	 * unida o no
	 * @param es
	 * @return El c�digo de la subcuenta insertada, 0 si no se insert� nada en la BD
	 */
	public int insertar(Connection con, int convenio, String codigoContrato,String tipoPaciente, int naturaleza, int montoCobro, String poliza, String carnet,int idCuentaMadre,double total, String usuario, boolean esUnida);
	
	/**
	 * M�todo que lista todas las subcuentas de un paciente
	 * @param con Conexi�n con la BD
	 * @param codigoPaciente
	 * @param cuentaMadre
	 * @return Collection con todas las subcuebtas de un paciente
	 */
	public Collection listar(Connection con, int codigoPaciente, int cuentaMadre, boolean esConsulta);
	
	/**
	 * M�todo que cambia los estados de una cuenta hija y el de su
	 * respectiva cuenta madre
	 * Si cualquiera de los estados recibe como per�metro un numero menor
	 * � igual a 0 entonces no modificar� el estado
	 * @param con Conexi�n con la BD
	 * @param idSubCuenta
	 * @param nuevoEstadoCuentaHija
	 * @param nuevoEstadoCuentaMadre
	 * @return true si se realizaron los cambios correctamente
	 */
	public boolean cambiarEstados(Connection con, int idSubCuenta, int nuevoEstadoCuentaHija, int nuevoEstadoCuentaMadre);
	
	/**
	 * M�todo para modificar las subcuentas
	 * @param con Conexi�n con la BD
	 * @param subCuenta �digo de la subcuenta
	 * @param convenio C�digo del convenio
	 * @param tipoPaciente Acr�nimo del tipo de paciente
	 * @param codigoContrato 
	 * @param naturaleza Acr�nimo de la naturaleza del paciente
	 * @param montoCobro C�digo del monto de cobro
	 * @param poliza N�mero de Poliza
	 * @param carnet N�mero de Carnet
	 * @return true si se hizo alguna modificaci�n, false de lo contrario
	 */
	public boolean modificar(Connection con, int subCuenta, int convenio, String codigoContrato, String tipoPaciente, int naturaleza, int montoCobro, String poliza, String carnet);
	
	/**
	 * Metodo para realizar la insercion de la relacion entre solicitud subcuenta
	 * @param servicio @todo
	 * @param institucion 
	 * @param con, Conexion
	 * @param sub_cuenta, Codigo de la subcuenta
	 * @param solicitud, Codigo de la solicitud
	 * @return, >0 si la inserccion es correcta, 0 si se produjo un error.
	 */
	 public int insertarSolicitudesSubcuentas(Connection con,  int sub_cuenta,int solicitud, int servicio, String institucion);

	 /**
	  * Metodo para realizar la insercion de un responsable de una subcuenta 
	  * @param con, conexion
	  * @param sub_cuenta, codigo de la subcuenta
	  * @param numeroIdentificacion, numero de identificacion del responsable
	  * @param tipoIdentificacion, tipo de identificacion del responsable.
	  * @param nombre, nombre del responsable.
	  * @param telefono, telefono del responsable.
	  * @param codCiudad, codigo de la ciudad.
	 * @param codCiudad2
	  * @param direccion, direccion.
	  * @param Empresa, empresa dode trabaja el responsable
	  * @param telefonoTrabajo, telefono del trabajo del responsable
	  * @return 0, si se presenta error en la insercion.
	  */
	public int insertarResponsablesSubCuenta(Connection con, int sub_cuenta, String numeroIdentificacion, String tipoIdentificacion, String nombre, String telefono, String codDepto, String codCiudad2, String direccion, String empresa, String telefonoTrabajo);
	
	/**
	 * Metodo que me indica si existe un responsable para esta subcuenta.
	 * @param con
	 * @param idSubCuenta
	 * @return
	 * @throws SQLException
	 */
	public boolean existeResponsableSubCuenta (Connection con, String idSubCuenta);
	
	/**
	 * Metodo para cargar el responsable de una subcuenta
	 * @param con
	 * @param idSubCuenta
	 * @return, ResultSetDecorator de la subcuenta.
	 */
	public ResultSetDecorator cargarResponsableSubCuenta(Connection con, String idSubCuenta);
	
	/**
	 * Metodo para cargar el responsable de una cuenta
	 * @param con
	 * @param idCuenta
	 * @return, ResultSetDecorator de la cuenta.
	 */
	public ResultSetDecorator cargarResponsableCuenta(Connection con, String idCuenta);
	
	/**
	 * Metodo para eliminar una subcuenta, dado su Id.
	 * @param con, Conexion.
	 * @param subCuenta, Id de la subcuenta a eliminar
	 * @return, true si se eliminfo, false si no
	 */
	public boolean eliminarSubcuenta(Connection con, int subCuenta);
	
	/**
	 * Metodo para eliminar  subcuenta, dado su cuenta
	 * @param con, Conexion.
	 * @param cuenta, Id de la cuenta 
	 * @return, true si se elimino, false si no
	 */
	public boolean eliminarSubcuentaDadoCuenta(Connection con, int idCuenta);
	
	/**
	 * Metodo para eliminar una responsables_subcuenta, dado el id(Codigo Subcuenta), y el numero
	 * de identificacion del responable.
	 * @param con, Conexion.
	 * @param id, Id de la subcuenta a eliminar
	 * @param ni, numero identificacion del responsable particular
	 * @return, true si se eliminado, false si no
	 */
	public boolean eliminarResponsableSubcuenta(Connection con, int id,String ni);
	
	/**
	 * Metodo para eliminar todas las solicitudes de una subcuenta
	 * @param con
	 * @param subCuenta
	 * @return true or false, dependiendo si se pudo eliminar o no
	 */
	public boolean eliminarSolicitudesSubcuenta(Connection con, int subCuenta);

	/**
	 * M�todo para listar las cuentas que ya han sido distribuidas
	 * @param con Conexi�n con la BD
	 * @param codigoPaciente C�digo del paciente al cual se le listar�an las solicitudes
	 * @return Collection con el listado de las solicitudes
	 */
	public Collection listarCuentasDistribuidas(Connection con, int codigoPaciente);
	
	/**
	 * M�todo para consultar el tipo de distribucion de una cuenta
	 * Retorna true si la distribucion es unida false si es independiete
	 * @param con Conexi�n con la BD
	 * @param cuenta codigo de la cuenta
	 * @return boolean, resultado de la consulta.
	 */
	public boolean tipoDistribucionCuenta(Connection con, int cuenta);

	/**
	 * M�todo que carga la subcuenta enfocada a la 
	 * informaci�n manejada en el objeto 
	 * 
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idSubcuenta C�digo de la subcuenta
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarSubCuenta2 (Connection con, int idSubcuenta) throws SQLException;
	
	/**
	 * Metodo que cambia el atributo distribuidad de una cuenta.
	 * por el valor que se envie.
	 * @param con.
	 * @param distribuida, valor del campo
	 * @param idCuenta, id de la cuenta a actualizar.
	 * @return
	 */
	public int cambiarAtributoDistribuidaCuent(Connection con,boolean distribuida,int idCuenta);
	
	/**
	 * M�todo que busca los datos de porcentaje o 
	 * valor del monto de cobro y si este es por 
	 * porcentaje o valor
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param idSubcuenta C�digo del monto de cobro
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator busquedaReferenciasPagoMontoCobro (Connection con, int idMontoCobro) throws SQLException;
	
	/***
	 * M�todo que cabia el estado de una subcuenta
	 * @param con
	 * @param codSubcuenta
	 * @param codEstado
	 * @return
	 */
	public  int cambiarEstadoCuentaHija(Connection con, int codSubcuenta, int codEstado) throws SQLException;

	 /**
	  * Metodo para realizar la insercion de un responsable de una subcuenta 
	  * @param con, conexion
	  * @param sub_cuenta, codigo de la subcuenta
	  * @param requisito, el requisito necesario de la subcuenta.	  
	  * @param estado, hace referencia al estado en que se encuentra el requisito, es
	  * decir si se cumple o no. 
	  * return 0 si no la inserto, de lo contrario retorna el codigo de la subcuenta
	 */
	public int insertarRequisitoSubCuenta(Connection con, int sub_cuenta, int requisito, boolean estado);
	/** Adiciones de Sebasti�n
	 * Lista subcuentas con su n�mero de factura
	 * Nota* Aquellas subcuentas que no est�n facturadas tendr�n este campo null
	 * @param con
	 * @param cuenta
	 * @return Colecci�n de subcuentas con su consecutivo factura 
	 */
	public Collection listarConConsecutivoFactura(Connection con,int cuenta,String codviaingreso);

	/**
	 * Metodo que elimina todos los requisitos ingresados a una subcuenta.
	 * @param con, Conexion
	 * @param subCuenta, Subcuenta.
	 */
	public boolean eliminarLosRequisitosSubCuenta(Connection con, int subCuenta);

	 /**
	  * Metodo Caso ESPECIAL
	  * Metodo utilizado para el ingreso de las solicitudes de una subcuenta.
	  * para el caso de asocio de cuentas con distribucion independiente, y en 
	  * el cual solo se realizo distribucion a una de las cuentas, la otra debe
	  * ingresarse como una replica de la cuenta madre, y la relacion de las 
	  * solicitudes sub_cuentas, es igua a la de la cuenta madre.
	 * @param institucion 
	 * @param con, Conexion
	  * @param sub_cuenta,SubCuenta.
	  * @return el estado de la transaccion, true si todo salio bien y se puede continuar con la transaccion.
	  */
	 public boolean insertarSolicitudesCasoUnaSubCuenta(Connection con,int sub_cuenta, String institucion);
	 
		/**
	 * Metodo que actualiza el estado a cerrada de subcuentas que pertenecen a una cuenta madre dada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public boolean actualizarSubCuentasDeUnaFacturaACerrada(Connection con, int idCuenta); 
	
	
	/**
	 * Empezar en una cuenta el proceso de distribucion
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @return
	 */
	public int empezarProcesoDistribucion(Connection con, int idCuenta, String loginUsuario);
	
	/**
	 * Metodo que cambia el estado de una cuenta a "Cuenta en Proceso de Distribucion"
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int cuentaProcesoDistribucion(Connection con,int idCuenta);
	
	/**
	 * M�todo que cancela todos los procesos de distribucion en proceso
	 * @param con Conexi�n con la BD
	 * @return numero de cancelaciones
	 */
	public int cancelarTodosLosProcesosDeDistribucion(Connection con);
	
	
	/**
	 * M�todo para cancelar el proceso de distribucion (transaccional)
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return numero mayor que cero (0) si se realiz� correctamente la cancelaci�n
	 */
	public int cancelarProcesoDistribucionTransaccional(Connection con, int idCuenta, String estado);
	
	/**
	 * M�todo para termina el proceso de distribucion
	 * Si el estado es null, se ejecuta no transaccional
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return mayor que cero (0) si se realiz� correctamente el proceso de facturaci�n
	 */
	public int finalizarProcesoDistribuciontransaccional(Connection con, int idCuenta, String estado);

	/**
	 * M�todo que termina el proceso de distribuci�n de la cuenta!
	 * NO CAMBIA ESTADOS
	 * @param con
	 * @param codigoCuentaPaciente
	 */
	public boolean terminarProcesoDistribuciontransaccional(Connection con, int codigoCuentaPaciente);

	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int obtenerEstadoOriginalCuenta(Connection con, int codigoCuenta);

}
