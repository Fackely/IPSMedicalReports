/*
 * Creado en 2/09/2004
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudMedicamentosDao;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudMedicamentosDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosEntidadesSubcontratadasDao;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulos;

/**
 * @author Juan David Ram�rez L�pez
 *
 * Princeton S.A.
 */
public class PostgresqlSolicitudMedicamentosDao implements SolicitudMedicamentosDao
{
	/**
	 * M�todo para ingresar la justificaci�n del medicamento
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo
	 * @param descripcion
	 * @param estado
	 * @return
	 */
	/*
	public int ingresarAtributoTransaccional(Connection con, int numeroSolicitud, int articulo, int atributo, String descripcion, String estado)
	{
		return SqlBaseSolicitudMedicamentosDao.ingresarAtributoTransaccional(con, numeroSolicitud, articulo, atributo, descripcion, estado);
	}
	*/
	/**
	 * M�todo para modificar la justificaci�n del medicamento
	 * este m�todo solo modifica un atributo, as� que se llama
	 * cada vez que se quiere modificar uno
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo Par�metro tomado de ConstantesBD
	 * @param descripcion Texto del campo que se desea ingresar
	 * @param estado estado de la transacci�n
	 * @return int mayor que 0 si se modific� correctamente el atributo espec�fico.
	 */
	public int modificarAtributoTransaccional(Connection con, int numeroSolicitud, int articulo, int atributo, String descripcion, String estado)
	{
		return SqlBaseSolicitudMedicamentosDao.modificarAtributoTransaccional(con, numeroSolicitud, articulo, atributo, descripcion, estado);
	}
	
	/**
	 * M�todo para ingresar el detalle de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param observaciones
	 * @param dosis
	 * @param frecuencia
	 * @param tipoFrecuencia
	 * @param via
	 * @param cantidadSolicitada
	 * @param estado
	 * @return
	 */
	public int ingresarDetalleSolicitudTransaccional(Connection con, int numeroSolicitud, int articulo, String observaciones, String dosis, String unidosis, int frecuencia, String tipoFrecuencia, String via, int cantidadSolicitada, String estado, String diasTratamiento)
	{
		return SqlBaseSolicitudMedicamentosDao.ingresarDetalleSolicitudTransaccional(con, numeroSolicitud, articulo, observaciones, dosis, unidosis, frecuencia, tipoFrecuencia, via, cantidadSolicitada, estado, diasTratamiento);
	}
	
	/**
	 * M�todo para modificar el detalle de la solicitud de medicamentos
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param observaciones
	 * @param dosis
	 * @param frecuencia
	 * @param via
	 * @param estado
	 * @return int mayor que 0 si se insert� correctamente el detalle de la solicitud.
	 */
	public int modificarDetalleSolicitudTransaccional(Connection con, int numeroSolicitud, int articulo, String observaciones, String dosis, int frecuencia, String tipoFrecuencia, String via, int cantidadSolicitada, String estado, String unidosis, String diasTratamiento)
	{
		return SqlBaseSolicitudMedicamentosDao.modificarDetalleSolicitudTransaccional(con, numeroSolicitud, articulo, observaciones, dosis, frecuencia, tipoFrecuencia, via, cantidadSolicitada, estado, unidosis, diasTratamiento);
	}

	/**
	 * M�todo para el ingreso de la solicitud de medicamentos
	 * @param con
	 * @param numerosolicitud
	 * @param observacionesGenerales
	 * @param estado
	 * @return
	 */
	public int ingresarSolicitudMedicamentos(Connection con, int numerosolicitud, String observacionesGenerales, String estado, String centroCostoPrincipal, String controlEspecial, String entidadSubcontratada)
	{
		return SqlBaseSolicitudMedicamentosDao.ingresarSolicitudMedicamentos(con, numerosolicitud, observacionesGenerales, estado, centroCostoPrincipal, controlEspecial, entidadSubcontratada);
	} 

	/**
	 * M�todo para la modificaci�n de la solicitud de medicamentos
	 * @param con
	 * @param numerosolicitud
	 * @param observacionesGenerales
	 * @param estado
	 * @return int mayor que 0 si se insert� correctamente la solicitud.
	 */
	public int modificarSolicitudMedicamentos(Connection con, int numerosolicitud, String observacionesGenerales, String estado)
	{
		return SqlBaseSolicitudMedicamentosDao.modificarSolicitudMedicamentos(con, numerosolicitud, observacionesGenerales, estado);
	}

	/**
	 * Consultar el detalle de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @retur Collection con las observaciones generales de la solicitud de medicamentos 
	 */
	public Collection consultarSolicitudMedicamentos(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudMedicamentosDao.consultarSolicitudMedicamentos(con, numeroSolicitud);
	}
	
	/**
	 * Consultar las solicitudes con administracion
	 * @param con
	 * @param numeroSolicitud
	 * @retur Collection con las observaciones generales de la solicitud de medicamentos 
	 */
	public Collection consultarSolicitudesMedicamentoConAdministracion(Connection con, int cuenta)
	{
		return SqlBaseSolicitudMedicamentosDao.consultarSolicitudesMedicamentoConAdministracion(con,cuenta,DaoFactory.POSTGRESQL);
	}
	
	/**
	 * Consultar el detalle de los art�culos que contiene la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoInstitucion
	 * @return Collection con el detalle de los articulos contenidos en la solicitud
	 */
	public Collection consultarDetalleSolicitudArticulos(Connection con, int numeroSolicitud, int codigoInstitucion)
	{
		return SqlBaseSolicitudMedicamentosDao.consultarDetalleSolicitudArticulos(con, numeroSolicitud, codigoInstitucion);
	}
	
	/**
	 * Consultar las solicitudes de un paciente
	 * @param con, conexion a la BD.
	 * @param cuenta, codigo de la cuenta del paciente que se desea consultar
	 * @return Collection, Resultado de la consulta, todas las solicitudes del pacientes.
	 */
	public Collection consultarSolicitudesMedicamento(Connection con, HashMap<String, Object> criteriosBusquedaMap)
	{
		return SqlBaseSolicitudMedicamentosDao.consultarSolicitudesMedicamento(con, criteriosBusquedaMap,DaoFactory.POSTGRESQL);
	}

	/**
	 * M�todo para cargar los insumos que fueron despachados a para una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return Collection con la consulta de los insumos para la solicitud dada
	 */
	public Collection consultarInsumos(Connection con, int numeroSolicitud)
	{
		return SqlBaseSolicitudMedicamentosDao.consultarInsumos(con, numeroSolicitud);
	}
	
	/**
	 * M�todo para cargar los insumos que fueron cargados
	 * a la solicitud en la farmacia
	 * @param con, Connection con la fuente de datos 
	 * @param numeroSolicitud, int
	 * @return Collection con la consulta de los insumos 
	 * @see com.princetonsa.Sqlbase.SqlBaseSolicitudMedicamentosDao#consultarInsumosFarmacia(java.sql.Connection)
	 */
	public Collection consultarInsumosFarmacia(Connection con, int numeroSolicitud)
	{
	    return SqlBaseSolicitudMedicamentosDao.consultarInsumosFarmacia(con,numeroSolicitud);
	}

	/**
	 * M�todo para borrar articulos de una solicitud
	 * El m�todo borra en cascada la descripcion de los articulos.
	 * @param con
	 * @param numeroSolicitud Solicitud a la cual pertenece el art�culo que se desea borrar
	 * @param articulo C�digo del art�culo que se desea borrar
	 * @return Mayor que 0 si se elimin� alg�n art�culo
	 */
	public int eliminarArticulo(Connection con, int numeroSolicitud, int articulo)
	{
		return SqlBaseSolicitudMedicamentosDao.eliminarArticulo(con, numeroSolicitud, articulo);
	}

	/**
	 * M�todo para suspender un medicamento dada la solicitud y el medicamento
	 * @param con Conecci�n con la base de datos
	 * @param numeroSolicitud Solicitud a la cual pertenece el medicamento
	 * @param articulo Articulo que se desea suspender
	 * @param motivo Motivo de la suspensi�n
	 * @param codigoMedico M�dico que realiz� la suspensi�n
	 * @return true si el medicamento se suspendi� correctamente
	 */
	public boolean suspenderMedicamento(Connection con, int numeroSolicitud, int articulo, String motivo, int codigoMedico)
	{
		return SqlBaseSolicitudMedicamentosDao.suspenderMedicamento(con, numeroSolicitud, articulo, motivo, codigoMedico);
	}
	
	/**
	 * M�todo para modificar el motivo se suspensi�n de un
	 * medicamento dada la solicitud y el codigo del medicamento
	 * @param con Conecci�n con la base de datos
	 * @param numeroSolicitud Solicitud a la cual pertenece el medicamento
	 * @param articulo Articulo que se desea suspender
	 * @param motivo Motivo de la suspensi�n
	 * @return true si el medicamento se suspendi� correctamente
	 */
	public boolean modificarMotivoSuspension(Connection con, int numeroSolicitud, int articulo, String motivo)
	{
		return SqlBaseSolicitudMedicamentosDao.modificarMotivoSuspension(con, numeroSolicitud, articulo, motivo);
	}
	
	/**
	 * M�todo para verificar la existencia de un articulo en la solictud
	 * @param con Conecci�n con la BD
	 * @param numeroSolicitud Numero de solicitud en la cual se solicita el articulo
	 * @param articulo C�digo del articulo
	 * @return true si el articulo existe en a solicitud dada, false de lo contrario
	 */
	public boolean existeMedicamento(Connection con, int numeroSolicitud, int articulo)
	{
		return SqlBaseSolicitudMedicamentosDao.existeMedicamento(con, numeroSolicitud, articulo);
	}

	/**
	 * M�todo para borrar atributos en caso de modificaci�n de la justificaci�n
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param atributo
	 * @param estado
	 * @return
	 */
	public int borrarAtributoTransaccional(Connection con, int numeroSolicitud, int articulo, int atributo, String estado)
	{
		return SqlBaseSolicitudMedicamentosDao.borrarAtributoTransaccional(con, numeroSolicitud, articulo, atributo, estado);
	}

	/**
	 * M�todo para actualizar cantidades solicitadas
	 * de una solicitud de medicamentos
	 * @param con Conexi�n con la BD
	 * @param codigo del articulo 
	 * @param cantidad Cantidad solicitada
	 * @param numeroSolicitud Solicitud a modificar
	 * @return int con el n�mero de elementos actualizados
	 * -1 en caso de alg�n error
	 */
	public int actualizarCantidades(Connection con, int codigoArticulo, int cantidad, int numeroSolicitud)
	{
		return SqlBaseSolicitudMedicamentosDao.actualizarCantidades(con, codigoArticulo, cantidad, numeroSolicitud);
	}
	
	/**
	 * Listado de los medicamentos de todas las solicitudes del paciente
	 * @param con
	 * @param cuentaPaciente
	 * @return
	 */
	public Collection consultarListadoMedicamentosPaciente(Connection con, int cuentaPaciente)
	{
		return SqlBaseSolicitudMedicamentosDao.consultarListadoMedicamentosPaciente(con, cuentaPaciente);
	}

	/**
	 * Numero de Convenios Plan Especial asociados al ingreso del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public int conveniosPlanEspecial(Connection con, int codigoIngreso) 
	{
		return SqlBaseSolicitudMedicamentosDao.conveniosPlanEspecial(con , codigoIngreso);
	}
	
	/**
	 * Consulta las solicitudes Historicas
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public HashMap consultarHistorialSolicitudes(Connection con,HashMap parametros)
	{
		return SqlBaseSolicitudMedicamentosDao.consultarHistorialSolicitudes(con, parametros);
	}

	@Override
	public ArrayList<DtoEntidadSubcontratada> obtenerEntidadesSubcontratadasCentroCosto(
			Connection con, String codigoCentroCostoPrincipal,
			int codigoInstitucionInt) {
		
		return SqlBaseSolicitudMedicamentosDao.obtenerEntidadesSubcontratadasCentroCosto(con, codigoCentroCostoPrincipal, codigoInstitucionInt);
	}

	@Override
	public boolean insertarAticulosEquivalenteSolicitud(Connection con,int numeroSolicitud, int articuloEquivalente, int articuloPrincipal) 
	{
		return SqlBaseSolicitudMedicamentosDao.insertarAticulosEquivalenteSolicitud(con, numeroSolicitud, articuloEquivalente,articuloPrincipal);
	}

	@Override
	public ArrayList<DtoArticulos> buscarArticulosXSolicitudMedicamentos(
			Connection conn, int numeroSolicitud) {
		return SqlBaseSolicitudMedicamentosDao.buscarArticulosXSolicitudMedicamentos(conn, numeroSolicitud);
	}

	/**
	 * @see com.princetonsa.dao.SolicitudMedicamentosDao#consultarDatosMedicoAnulacion(java.sql.Connection, java.lang.Integer)
	 */
	public  String consultarDatosMedicoAnulacion(Connection conn,Integer codigoMedico) throws SQLException
	{
		return SqlBaseSolicitudMedicamentosDao.consultarDatosMedicoAnulacion(conn, codigoMedico);
	}
	
}
