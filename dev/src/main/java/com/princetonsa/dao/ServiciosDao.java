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
	 * M�todo que carga todos los servicios existentes en el sistema
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarServicios (Connection con) throws SQLException;
	
	/**
	 * M�todo que carga un servicio existente en el sistema, dado su c�digo
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarServicio (Connection con, int codigoServicio) throws SQLException;
	
	/**
	 * M�todo que carga todos los sexos presentes en el sistema
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarSexos (Connection con) throws SQLException;


	/**
	 * M�todo que dado un c�digo de servicio, devuelve en un ResultSet
	 * todos los c�digos particulares asociados a �ste
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio C�digo del servicio para el cual se desean
	 * cargar los c�digos particulares
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarCodigosParticulares (Connection con, int codigoServicio) throws SQLException;
	
	/**
	 * M�todo que inserta un servicio en la fuente de datos
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoEspecialidad C�digo de la especialidad
	 * para la que este servicio est� restringido
	 * @param codigoTipoServicio C�digo del tipo de servicio
	 * a insertar
	 * @param codigoNaturalezaServicio C�digo de la
	 * naturaleza del servicio a insertar
	 * @param codigoSexo C�digo del sexo para el que
	 * este servicio est� restringido
	 * @param codigoFormulario C�digo del formulario 
	 * (no es muy clara la def. de formulario) a seguir?
	 * @param esPos Define si el servicio a insertar pertenece
	 * o no al POS
	 * @param posSubsidiado 
	 * @param activo boolean que indica si este
	 * servicio est� activo o no
	 * @return
	 * @throws SQLException
	 */
	public int insertarServicio (Connection con, int codigoEspecialidad, String codigoTipoServicio, String codigoNaturalezaServicio, 
			int codigoSexo, HashMap formularios, boolean esPos, String posSubsidiado, boolean activo, double unidadesUvr,int codigoGrupoServicio, String nivel, String requiereInterpretacion, String costo, String realizaInstitucion,String requiereDiagnostico, int codigoServicioPortatil,int codigoInstitucion, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException;
	
	/**
	 * Inserta un c�digo particular a un servicio 
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio C�digo del servicio
	 * para el que se va a insertar un c�digo particular
	 * @param codigoTipoTarifario C�digo del tipo
	 * de tarifario a insertar (Ej, c�digo ISS en el sistema)
	 * @param codigoPropietario C�digo particular
	 * para este servicio en el tarifario definido
	 * @param descripcion Descripci�n de este servicio
	 * seg�n el tarifario definido
	 * @return
	 * @throws SQLException
	 */
	public int insertarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException;
	
	/**
	 * Inserta un c�digo particular Soat a un servicio 
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio C�digo del servicio
	 * para el que se va a insertar un c�digo particular
	 * @param codigoPropietario C�digo particular
	 * para este servicio en el tarifario definido
	 * @param unidades Unidades Soat
	 * @param descripcion Descripci�n de este servicio
	 * seg�n el tarifario definido
	 * @return
	 * @throws SQLException
	 */
	public int insertarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException;

	/**
	 * Modifica (y si no existe inserta) el c�digo 
	 * particular Soat de un servicio 
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio C�digo del servicio
	 * para el que se va a modificar un c�digo particular 
	 * Soat
	 * @param codigoPropietario C�digo particular
	 * para este servicio en el tarifario definido
	 * @param unidades Unidades Soat
	 * @param descripcion Descripci�n de este servicio
	 * seg�n el tarifario definido
	 * @return
	 * @throws SQLException
	 */
	public int modificarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException;

	/**
	 * M�todo que modifica cualquier campo de un servicio 
	 * existente en el sistema
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio C�digo del servicio a modificar
	 * @param codigoEspecialidad C�digo de la nueva restricci�n
	 * por especialidad
	 * @param codigoTipoServicio Nuevo c�digo del tipo de este
	 * servicio
	 * @param codigoNaturalezaServicio Nuevo c�digo de la
	 * naturaleza de este servicio
	 * @param codigoSexo C�digo de la nueva restricci�n
	 * por sexo
	 * @param codigoFormulario Nuevo c�digo del formulario
	 * de este servicio
	 * @param esPos Boolean que indica si este servicio quedo
	 * Pos o no Pos
	 * @param esPosSubsidiado 
	 * @param activo Define si este c�digo particular queda activo o
	 * no
	 * @return
	 * @throws SQLException
	 */
	public int modificarServicio (Connection con, int codigoServicio, int codigoEspecialidad, String codigoTipoServicio, String codigoNaturalezaServicio, int codigoSexo,  HashMap formularios, boolean esPos, String esPosSubsidiado, boolean activo, double unidadesUvr, int codigoGrupoServicio, String consecutivoNivel, String requiereInterpretacion, String costo, String realizaInstitucion,String requiereDiagnostico, int codigoPortatilAsociado,int codigoInstitucion, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException;
	
	/**
	 * M�difica todos los datos existentes para un c�digo particular de
	 * un servicio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoServicio C�digo del servicio a modificar
	 * @param codigoTipoTarifario C�digo del tipo de tarifario a
	 * modificar
	 * @param codigoPropietario Nuevo C�digo propietario
	 * @param descripcion Nueva descripci�n del c�digo
	 * propietario seg�n su tarifario
	 * @return
	 * @throws SQLException
	 */
	public int modificarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException;
	
	/**
	 * M�todo que carga todos los servicios que cumplan con 
	 * un set de restricciones especificado en un objeto
	 * BeanBusquedaServicio
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param restriccion Objeto que almacena todas las 
	 * restricciones de la b�squeda
	 * @param codigoInstitucion 
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarServiciosRestringido (Connection con, BeanBusquedaServicio restriccion, int codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que busca los c�digos de los servicios ya usados 
	 * en la aplicaci�n, los devuelve en una colecci�n como Strings, 
	 * para que el usuario pueda buscar con los m�todos de la colecci�n
	 * de una manera sencilla
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public Collection busquedaServiciosYaUsados (Connection con) throws SQLException;
	
	/**
	 * Implementaci�n del m�todo que carga las unidades Uvr de un 
	 * servicio en una BD Gen�rica
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
	 * M�todo para obtener el arreglo de los formularios para asignarle al servicio
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoPlantillaServDiag> obtenerArregloFormularios(Connection con,int codigoInstitucion);
	
	/**
	 * M�todo para cargar los formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap<String, Object> cargarFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion);
	
	
	/**
	 * M�todo para cargar el numero de formularios de un servicio
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
