/*
 * @(#)PostgresqlServiciosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import util.Beans.BeanBusquedaServicio;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ServiciosDao;
import com.princetonsa.dao.sqlbase.SqlBaseServiciosDao;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;

/**
 * Esta clase implementa el contrato estipulado en <code>ServiciosDao</code>, 
 * proporcionando los servicios de acceso a una base de datos PostgreSQL 
 * requeridos por <code>Servicios</code> y <code>Servicio</code>.
 *
 * @version 1.0 Nov 27, 2003
 */
public class PostgresqlServiciosDao implements ServiciosDao 
{
	/**
	 * 
	 */
	private static final String consultaNombreServicio="SELECT getnombreservicio(?,?) as nom "; 
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un servicio 
	 * en el sistema en una BD PostgreSQL.
	 */
	private static final String insertarServicioStr="INSERT INTO servicios (codigo, especialidad, tipo_servicio, naturaleza_servicio, sexo,  espos,espossubsidiado, activo, unidades_uvr, grupo_servicio, nivel, requiere_interpretacion, costo, realiza_institucion,requiere_diagnostico, portatil_asociado,atencion_odontologica,convencion,minutos_duracion) values (nextval('seq_servicios'), ?, ?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ?)";
	
	/**
	 * Implementación del método que inserta un servicio en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#insertarServicio (Connection , int , String , String , int , int , int , boolean ) throws SQLException 
	 */
	public int insertarServicio (Connection con, int codigoEspecialidad, String codigoTipoServicio, String codigoNaturalezaServicio, int codigoSexo, HashMap formularios, boolean esPos,String esPosSubsidiado, boolean activo, double unidadesUvr, int codigoGrupoServicio, String nivel, String requiereInterpretacion, String costo, String realizaInstitucion, String requiereDiagnostico, int codigoServicioPortatil, int codigoInstitucion, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException
	{
	    return SqlBaseServiciosDao.insertarServicio (con, codigoEspecialidad, codigoTipoServicio, codigoNaturalezaServicio, codigoSexo,  formularios, esPos,esPosSubsidiado, activo, unidadesUvr, codigoGrupoServicio, insertarServicioStr, nivel, requiereInterpretacion, costo, realizaInstitucion, requiereDiagnostico, codigoServicioPortatil, codigoInstitucion, DaoFactory.POSTGRESQL, atencionOdontologica, convencion, minutosDuracion) ;
	}
	
	/**
	 * Implementación del método que modificar un servicio en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#modificarServicio (Connection , int , int , String , String , int , int , int , boolean ) throws SQLException 
	 */
	public int modificarServicio (Connection con, int codigoServicio, int codigoEspecialidad, String codigoTipoServicio, String codigoNaturalezaServicio, int codigoSexo,  HashMap formularios, boolean esPos,String esPosSubsidiado, boolean activo, double unidadesUvr, int codigoGrupoServicio, String consecutivoNivel, String requiereInterpretacion, String costo, String realizaInstitucion,String requiereDiagnostico, int codigoPortatilAsociado,int codigoInstitucion, String atencionOdontologica, int convencion, int minutosDuracion) throws SQLException
	{
		return SqlBaseServiciosDao.modificarServicio (con, codigoServicio, codigoEspecialidad, codigoTipoServicio, codigoNaturalezaServicio, codigoSexo, formularios, esPos,esPosSubsidiado, activo, unidadesUvr, codigoGrupoServicio, consecutivoNivel, requiereInterpretacion, costo, realizaInstitucion, requiereDiagnostico, codigoPortatilAsociado, codigoInstitucion, atencionOdontologica, convencion, minutosDuracion) ;
	}
	
	/**
	 * Implementación del método que carga todos los servicios 
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarServicios (Connection ) throws SQLException 
	 */
	public ResultSetDecorator cargarServicios (Connection con) throws SQLException
	{
		return SqlBaseServiciosDao.cargarServicios (con) ;
	}
	
	/**
	 * Implementación del método que carga un servicio 
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarServicio (Connection , int) throws SQLException 
	 */
	public ResultSetDecorator cargarServicio (Connection con, int codigoServicio) throws SQLException
	{
		return SqlBaseServiciosDao.cargarServicio (con, codigoServicio) ;
	}

	/**
	 * Implementación del método que carga todos los sexos en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarSexos (Connection ) throws SQLException 
	 */
	public HashMap cargarSexos (Connection con) throws SQLException
	{
		return SqlBaseServiciosDao.cargarSexos (con) ;
	}
	
	

	/**
	 * Implementación del método que carga todos los códigos particulares de un
	 * servicio en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarCodigosParticulares (Connection , int ) throws SQLException 
	 */
	public HashMap cargarCodigosParticulares (Connection con, int codigoServicio) throws SQLException
	{
		return SqlBaseServiciosDao.cargarCodigosParticulares (con, codigoServicio) ;
	}
	
	/**
	 * Implementación del método que inserta un codigo particular de servicio 
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#insertarCodigoParticular (Connection , int , int , String , String , boolean ) throws SQLException 
	 */
	public int insertarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException
	{
		return SqlBaseServiciosDao.insertarCodigoParticular (con, codigoServicio, codigoTipoTarifario, codigoPropietario, descripcion) ;
	}
	
	/**
	 * Implementación del método que inserta un codigo particular de servicio 
	 * Soat en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#insertarCodigoParticularSoat (Connection , int , String , double , String ) throws SQLException 
	 */
	public int insertarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException
	{
		return SqlBaseServiciosDao.insertarCodigoParticularSoat (con, codigoServicio, codigoPropietario, unidades, descripcion) ;
	}
	
	/**
	 * Implementación del método que modifica un codigo particular de servicio  
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#existeCodigoParticular (Connection , int , int ) throws SQLException 
	 */
	public int modificarCodigoParticular (Connection con, int codigoServicio, int codigoTipoTarifario, String codigoPropietario, String descripcion) throws SQLException
	{
		return SqlBaseServiciosDao.modificarCodigoParticular (con, codigoServicio, codigoTipoTarifario, codigoPropietario, descripcion) ;
	}
	
  /**
   * Implementación del método que modifica un codigo particular Soat 
   * de servicio en una BD Postgresql
   * 
   * @see com.princetonsa.dao.ServiciosDao#modificarCodigoParticularSoat (Connection , int , String , double , String ) throws SQLException 
   */
	public int modificarCodigoParticularSoat (Connection con, int codigoServicio, String codigoPropietario, double unidades, String descripcion) throws SQLException
	{
		return SqlBaseServiciosDao.modificarCodigoParticularSoat (con, codigoServicio, codigoPropietario, unidades, descripcion) ;
	}
	
	/**
	 * Implementación del método que carga los servicios restringidos
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#endTransaction (Connection ) throws SQLException 
	 */
	public HashMap cargarServiciosRestringido (Connection con, BeanBusquedaServicio restriccion, int institucion) throws SQLException
	{
		return SqlBaseServiciosDao.cargarServiciosRestringido (con, restriccion, institucion) ;
	}

	/**
	 * Implementación del método que busca los códigos de los servicios
	 * ya usados en una BD Hsqldb
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#busquedaServiciosYaUsados (Connection ) throws SQLException 
	 */
	public Collection busquedaServiciosYaUsados (Connection con) throws SQLException
	{
		return SqlBaseServiciosDao.busquedaServiciosYaUsados (con) ;
	}
	
	/**
	 * Implementación del método que carga las unidades Uvr de un 
	 * servicio en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.ServiciosDao#cargarCodigosParticulares (Connection , int ) throws SQLException 
	 */
	public ResultSetDecorator consultaUnidadesUvr (Connection con, int codigoServicio) throws SQLException
	{
		return SqlBaseServiciosDao.consultaUnidadesUvr(con,codigoServicio);
	}
	
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
	 * @throws SQLExceptionobjetoDao
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
													  ) throws SQLException
	{
	    return SqlBaseServiciosDao.busquedaAvanzadaServiciosXCodigos(con, codigoAxioma, codigoPropietarioCups, codigoPropietarioIss, codigoPropietarioSoat, descripcionCups, codigoSexo, codigoConvenioResponsable, codigoContratoCuenta);
	}

	/**
	 * 
	 */
	public HashMap consultarTarifarios(Connection con, int codigoServicio) 
	{
		return SqlBaseServiciosDao.consultarTarifarios(con, codigoServicio);
	}
	
	/**
	 * Metodo para cargar los servicios automaticos del presupuesto
	 * @param con
	 * @param automatic
	 * @return
	 * @throws SQLException
	 */	
	public HashMap cargarServiciosautomatico (Connection con, String automatic)
	{
		return SqlBaseServiciosDao.cargarServiciosautomatico(con, automatic);
	}
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 * Metodo que actualiza los registros que se van a mostrar
	 * Solo aquellos que poseen en el campo automatico el acronimoBDsi
	 */
	public boolean actualizarAutomatico (Connection con, HashMap vo)
	{
		return SqlBaseServiciosDao.actualizarAutomatico(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public String obtenerCodigoTarifarioServicio (Connection con, int codigoServicio, int tipoTarifario)
	{
		return SqlBaseServiciosDao.obtenerCodigoTarifarioServicio(con, codigoServicio, tipoTarifario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public String obtenerCodigoTarifarioServicioConDesc (Connection con, int codigoServicio, int tipoTarifario)
	{
		return SqlBaseServiciosDao.obtenerCodigoTarifarioServicioConDesc(con, codigoServicio, tipoTarifario);
	}
	
	/**
	 * Método para obtener el arreglo de los formularios para asignarle al servicio
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoPlantillaServDiag> obtenerArregloFormularios(Connection con,int codigoInstitucion)
	{
		return SqlBaseServiciosDao.obtenerArregloFormularios(con, codigoInstitucion);
	}
	
	/**
	 * Método para cargar los formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap<String, Object> cargarFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion)
	{
		return SqlBaseServiciosDao.cargarFormulariosServicio(con, codigoServicio, codigoInstitucion);
	}
	
	/**
	 * Método para cargar el numero de formularios de un servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public int getNumeroFormulariosServicio(Connection con,int codigoServicio,int codigoInstitucion)
	{
		return SqlBaseServiciosDao.getNumeroFormulariosServicio(con, codigoServicio, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param codigoPropietario
	 * @param tipoTarifario
	 * @return
	 */
	public int obtenerServicioDadoCodigoTarifario(Connection con, String codigoPropietario, int tipoTarifario)
	{
		return SqlBaseServiciosDao.obtenerServicioDadoCodigoTarifario(con, codigoPropietario, tipoTarifario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tipoTarifario
	 * @return
	 */
	public String obtenerNombreServicio(Connection con, int codigoServicio, int tipoTarifario)
	{
		return SqlBaseServiciosDao.obtenerNombreServicio(con, codigoServicio, tipoTarifario, consultaNombreServicio);
	}

	@Override
	public int obtenerMinutosDuracionServicio(int codigoServicio, Connection con) {
		
		return SqlBaseServiciosDao.obtenerMinutosDuracionServicio(codigoServicio, con);
	}
}