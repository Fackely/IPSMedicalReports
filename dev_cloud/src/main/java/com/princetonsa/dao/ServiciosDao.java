/*
 * @(#)ServiciosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;

import util.Beans.BeanBusquedaServicio;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Servicios</code>.
 *
 * @version 1.0 Nov 27, 2003
 */
public interface ServiciosDao
{
	/**
	 * Método que carga todos los servicios existentes en el sistema
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarServicios (Connection con) throws SQLException;
	
	/**
	 * Método que carga un servicio existente en el sistema, dado su código
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarServicio (Connection con, int codigoServicio) throws SQLException;
	
	/**
	 * Método que carga todos los sexos presentes en el sistema
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarSexos (Connection con) throws SQLException;


	/**
	 * Método que dado un código de servicio, devuelve en un ResultSet
	 * todos los códigos particulares asociados a éste
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio Código del servicio para el cual se desean
	 * cargar los códigos particulares
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarCodigosParticulares (Connection con, int codigoServicio) throws SQLException;
	
	/**
	 * Método que inserta un servicio en la fuente de datos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoEspecialidad Código de la especialidad
	 * para la que este servicio está restringido
	 * @param codigoTipoServicio Código del tipo de servicio
	 * a insertar
	 * @param codigoNaturalezaServicio Código de la
	 * naturaleza del servicio a insertar
	 * @param codigoSexo Código del sexo para el que
	 * este servicio está restringido
	 * @param codigoFormulario Código del formulario 
	 * (no es muy clara la def. de formulario) a seguir?
	 * @param esPos Define si el servicio a insertar pertenece
	 * o no al POS
	 * @param posSubsidiado 
	 * @param activo boolean que indica si este
	 * servicio está activo o no
	 * @return
	 * @throws SQLException
	 */
	public int insertarServicio (Connection con, int codigoEspecialidad, String codigoTipoServicio, String codigoNaturalezaServicio, 
			int codigoSexo, HashMap formularios, boolean esPos, String posSubsidiado, boolean activo, double unidadesUvr,int codigoGrupoServicio, String nivel, String requiereInterpretacion, String costo, String realizaInstitucion,String requiereDiagnostico, int codigoServicioPortatil,int codigoInstitucion, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException;
	
	/**
	 * Inserta un código particular a un servicio 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio Código del servicio
	 * para el que se va a insertar un código particular
	 * @param codigoTipoTarifario Código del tipo
	 * de tarifario a insertar (Ej, código ISS en el sistema)
	 * @param codigoPropietario Código particular
	 * para este servicio en el tarifario definido
	 * @param descripcion Descripción de este servicio
	 * según el tarifario definido
	 * @return
	 * @throws SQLException
	 */
	public int insertarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException;
	
	/**
	 * Inserta un código particular Soat a un servicio 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio Código del servicio
	 * para el que se va a insertar un código particular
	 * @param codigoPropietario Código particular
	 * para este servicio en el tarifario definido
	 * @param unidades Unidades Soat
	 * @param descripcion Descripción de este servicio
	 * según el tarifario definido
	 * @return
	 * @throws SQLException
	 */
	public int insertarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException;

	/**
	 * Modifica (y si no existe inserta) el código 
	 * particular Soat de un servicio 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio Código del servicio
	 * para el que se va a modificar un código particular 
	 * Soat
	 * @param codigoPropietario Código particular
	 * para este servicio en el tarifario definido
	 * @param unidades Unidades Soat
	 * @param descripcion Descripción de este servicio
	 * según el tarifario definido
	 * @return
	 * @throws SQLException
	 */
	public int modificarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException;

	/**
	 * Método que modifica cualquier campo de un servicio 
	 * existente en el sistema
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio Código del servicio a modificar
	 * @param codigoEspecialidad Código de la nueva restricción
	 * por especialidad
	 * @param codigoTipoServicio Nuevo código del tipo de este
	 * servicio
	 * @param codigoNaturalezaServicio Nuevo código de la
	 * naturaleza de este servicio
	 * @param codigoSexo Código de la nueva restricción
	 * por sexo
	 * @param codigoFormulario Nuevo código del formulario
	 * de este servicio
	 * @param esPos Boolean que indica si este servicio quedo
	 * Pos o no Pos
	 * @param esPosSubsidiado 
	 * @param activo Define si este código particular queda activo o
	 * no
	 * @return
	 * @throws SQLException
	 */
	public int modificarServicio (Connection con, int codigoServicio, int codigoEspecialidad, String codigoTipoServicio, String codigoNaturalezaServicio, int codigoSexo,  HashMap formularios, boolean esPos, String esPosSubsidiado, boolean activo, double unidadesUvr, int codigoGrupoServicio, String consecutivoNivel, String requiereInterpretacion, String costo, String realizaInstitucion,String requiereDiagnostico, int codigoPortatilAsociado,int codigoInstitucion, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException;
	
	/**
	 * Módifica todos los datos existentes para un código particular de
	 * un servicio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoServicio Código del servicio a modificar
	 * @param codigoTipoTarifario Código del tipo de tarifario a
	 * modificar
	 * @param codigoPropietario Nuevo Código propietario
	 * @param descripcion Nueva descripción del código
	 * propietario según su tarifario
	 * @return
	 * @throws SQLException
	 */
	public int modificarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException;
	
	/**
	 * Método que carga todos los servicios que cumplan con 
	 * un set de restricciones especificado en un objeto
	 * BeanBusquedaServicio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param restriccion Objeto que almacena todas las 
	 * restricciones de la búsqueda
	 * @param codigoInstitucion 
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarServiciosRestringido (Connection con, BeanBusquedaServicio restriccion, int codigoInstitucion) throws SQLException;
	
	/**
	 * Método que busca los códigos de los servicios ya usados 
	 * en la aplicación, los devuelve en una colección como Strings, 
	 * para que el usuario pueda buscar con los métodos de la colección
	 * de una manera sencilla
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public Collection busquedaServiciosYaUsados (Connection con) throws SQLException;
	
	/**
	 * Implementación del método que carga las unidades Uvr de un 
	 * servicio en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarCodigosParticulares (Connection , int ) throws SQLException 
	 */
	public ResultSetDecorator consultaUnidadesUvr (Connection con, int codigoServicio) throws SQLException;
	
	/**
	 * busqueda de servicios dado el codigo axioma o codigo cups o codigo iss o codigo soat o descripcion cups 
	 * @param con
	 * @param codigoAxioma
	 * @param codigoPropietarioCups
	 * @param codigoPropietarioIss
	 * @param codigoPropietarioSoat
	 * @param descripcionCups
	 * @param codigoSexo
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator busquedaAvanzadaServiciosXCodigos(	Connection con,
							        													String codigoAxioma,
							        													String codigoPropietarioCups,
																						String codigoPropietarioIss,
																						String codigoPropietarioSoat,
																						String descripcionCups,
																						int codigoSexo,
                                                                                        int codigoConvenioResponsable,
                                                                                        int codigoContratoCuenta
																						 ) throws SQLException;
	
	
	/**
	 * Metodo para cargar los servicios automaticos del presupuesto
	 * @param con
	 * @param automatic
	 * @return
	 */
	
	public HashMap cargarServiciosautomatico (Connection con, String automatic);
	
	
	/**
	 * Metodo para modificar los servicios automaticos del presupuesto
	 * @param con
	 * @param codigoServicio
	 * @param automatic
	 * @return
	 */
		
	/**
	 * 
	 */
	public boolean actualizarAutomatico(Connection con, HashMap vo);
	
	
	
	public HashMap consultarTarifarios(Connection con, int codigoServicio); 
	
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public String obtenerCodigoTarifarioServicio (Connection con, int codigoServicio, int tipoTarifario);
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public String obtenerCodigoTarifarioServicioConDesc (Connection con, int codigoServicio, int tipoTarifario);
	
	/**
	 * Método para obtener el arreglo de los formularios para asignarle al servicio
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoPlantillaServDiag> obtenerArregloFormularios(Connection con,int codigoInstitucion);
	
	/**
	 * Método para cargar los formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap<String, Object> cargarFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion);
	
	
	/**
	 * Método para cargar el numero de formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public int getNumeroFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion);
	
	/**
	 * 
	 * @param codigoPropietario
	 * @param tipoTarifario
	 * @return
	 */
	public int obtenerServicioDadoCodigoTarifario(Connection con, String codigoPropietario, int tipoTarifario);

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public String obtenerNombreServicio(Connection con, int codigoServicio,	int tipoTarifario);
	
	
	
	/**
	 * 
	 * @param codigoServicio
	 * @param con
	 * @return
	 */
	public  int  obtenerMinutosDuracionServicio(int codigoServicio, Connection con);
	
	
}
