/*
 * 22 Oct 2005
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez R
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Generar - Consultar - Modificar - Anular Petición
 */
public interface PeticionDao {
	
		  

	/**
	 * Método implementado para cargar los datos generales de las peticiones de un paciente
	 * @param con
	 * @param paciente
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarDatosGeneralesPeticion(Connection con,int paciente,HashMap filtro);

	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarPedidoPeticion(Connection con,HashMap campos);
	
	/**
	 * Método que asigna el numero del pedido asociado a una petición
	 * @param con
	 * @return
	 */
	public int insertarPedidoPeticion(Connection con,HashMap campos);
	
	/**
	 * Metodo para realizar la consulta de todas las peticiones de cirugia del sistema
	 * (si el codigoPeticion > 0 entonces restringe busqueda por este criterio)
	 * @param con
	 * @param codigoPaciente
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarDatosGeneralesPeticion2(Connection con, int codigoPaciente, int codigoPeticion, HashMap filtro);
	
	/**
	 * Método para conlsutar las peticionnes generales del Cirugias ó de un paciente Especifico.  
	 * @param con
	 * @param paciente
	 * @param Filtro 
	 * @param codigoCuenta --> Para saber si la cuenta está abierta o cerrada se utiliza en preanestesia
	 * 													  Si es igual a -1 se ignora esto
	 * @param codigoCentroAtencion --> Se utiliza en preantestesia para filtrar o no las peticiones de acuerdo a si está
	 * 														abierta o cerrada la cuenta del paciente
	 * @param HashMap filtro
	 * @return
	 */
	
	public HashMap cargarPeticionesCirugias(Connection con,int paciente, int filtro, int codigoCuenta, int codigoCentroAtencion, HashMap filtroMap);
	
	/**
	 * Metodo para cargar el encabezado del detalle de la peticion de cirugia 
	 * @param con
	 * @param Nro Peticion
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarEncabezadoPeticion(Connection con, int NroPeticion, HashMap filtro);
	
	
	/**
	 * Metodo para cargar los servicios de una peticion  
	 * @param con
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarServiciosPeticion(Connection con, int NroPeticion,int indicador, HashMap filtro);
	
	/**
	 * Metodo para cargar los profesionales de la peticion de cirugia 
	 * @param con
	 * @param numeroPeticion
	 * @param HashMap filtro
	 * @return
	 */
	public HashMap cargarProfesionalesPeticion(Connection con,int NroPeticion,HashMap filtro);
	
	/**
	 * Metodo para cargar los materiales de la peticion de cirugia 
	 * @param con
	 * @param numeroPeticion
	 * @param HashMap filtros
	 * @return
	 */
	public HashMap cargarMaterialesPeticion(Connection con,int NroPeticion, HashMap filtros);
	
	
	
	/**
	 * Metodo para realizar la consulta de peticiones segun parametros 
	 * @param con
	 * @param nroIniServicio
	 * @param nroFinServicio
	 * @param fechaIniPeticion
	 * @param fechaFinServicio
	 * @param fechaIniCirugia
	 * @param fechaFinCirugia
	 * @param profesional
	 * @param estadoPeticion
	 * @param origen
	 * @param centroAtención --> Centro de atención seleccionado en la búsqueda
	 * @param codigoCentroAtencion --> Se utiliza para realizar el filtro de las peticiones por centro de atención en preanestesia,
	 * 																	del usuario en sesión
	 * @param String programable
	 * @return
	 */
	public HashMap consultarPeticiones(Connection con, int nroIniServicio, int nroFinServicio, String fechaIniPeticion, String fechaFinServicio,
													   String fechaIniCirugia, String fechaFinCirugia, int profesional, int estadoPeticion, String origen, int centroAtencion, int codigoCentroAtencion,
													   String programable);

	/**
	 * Método para insertar una petición de corugías en la BD a traves de HashMaps
	 * @param con Conexión con la BD
	 * @param mapaPeticionEncabezado HashMap con los datos del encabezado
	 * @param mapaPeticionServicios HashMap con los datos de los servicios
	 * @param mapaPeticionProfesionales HashMap con los datos de los profesionales participantes
	 * @param mapaPeticionMateriales HashMap con los datos de los materiales especiales
	 * @param codigoPersona Persona a la cual se le desea ingresar la petición
	 * @param usuario Usuario del sistema
	 * @param esContinuarTransaccion, boolean que indica si la transaccion ya fue inicializada
	 * @return Número de inserciónes en la BD (posición 0) y codigo de la petición (posición 1)
	 */
	public int[] insertar( Connection con, 
									HashMap mapaPeticionEncabezado, 
									HashMap mapaPeticionServicios, 
									HashMap mapaPeticionProfesionales, 
									HashMap mapaPeticionMateriales, 
									int codigoPersona,
									int idIngreso,
									UsuarioBasico usuario,
									boolean esContinuarTransaccion, 
									boolean esModificar);

	/**
	 * Metodo que carga los servicios dados los codigos de peticiones
	 * @param con
	 * @param codigosPeticionesSeparadosPorComas
	 * @return
	 */
	public HashMap cargarServiciosDadasPeticiones(Connection con, String codigosPeticionesSeparadosPorComas);
	
	/**
	 * Metodo que carga los materiales especiales PARAMETRIZADOS de una peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public HashMap cargarMaterialesEspeciales2(Connection con, String codigoPeticion);
	
	/**
	 * Metodo que actualiza (fecha Estimada Cx- Duracion Aprox) informacion de la peticion
	 * @param con
	 * @param fechaEstimadaCirugia
	 * @param duracion
	 * @param codigoPeticion
     * @param requiereUci
	 * @return
	 */
	public boolean actualizarFechaDuracionRequiereUciPeticion(Connection con, String fechaEstimadaCirugia, String duracion, String codigoPeticion, String requiereUci);
	 
	/**
	 * metodo que carga el encabezaod de la peticion sin restrcciones, a menos de que se especifiquen los
	 * codigos de la peticion.
	 * @param con
	 * @param codigosPeticionesSeparadosPorComas
	 * @param HashMap filtros
	 * @return
	 */
	public Collection cargarEncabezadoPeticionSinRestricciones(Connection con, String codigosPeticionesSeparadosPorComas, HashMap filtros);
	
	/**
	 * Método para anular la petición de cirugías
	 * @param con
	 * @param numeroPeticion
	 * @param motivoAnulacion
	 * @return Mayor que 0 si la anulación fue correcta
	 */
	public int anularPeticion(Connection con, int numeroPeticion, int motivoAnulacion, String comentario, String loginUsuario);

       /**
     * metodo que actualiza el estado de la peticion
     * @param con
     * @param codigoEstado
     * @param codigoPeticion
     * @return
     */
    public boolean actualizarEstadoPeticion(Connection con, int codigoEstado, String codigoPeticion);
    
    /**
	 * Método que consulta lso apellidos y nombre del paciente de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public String getApellidosNombresPacientePeticion(Connection con,int codigoPeticion);	
	
	/**
 	 * Método para desasociar la petición de la solicitud
 	 * @param con Conexión con la BD
 	 * @param numeroSolicitud
 	 * @return si se pudo actualizar
 	 */
	public boolean desAsociarPeticionSolicitud(Connection con, int numeroSolicitud);
}