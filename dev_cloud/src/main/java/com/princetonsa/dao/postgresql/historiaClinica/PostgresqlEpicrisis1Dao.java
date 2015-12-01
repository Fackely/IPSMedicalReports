package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;

import com.princetonsa.dao.Epicrisis1Dao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEpicrisis1Dao;
import com.princetonsa.dto.epicrisis.DtoEpicrisis1;
import com.princetonsa.dto.epicrisis.DtoEpicrisisSecciones;
import com.princetonsa.dto.epicrisis.DtoNotasAclaratoriasEpicrisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlEpicrisis1Dao implements Epicrisis1Dao 
{
	/**
	 * 
	 */
	private final String consultaEpicrisis=" SELECT " +
									"usuario_modifica as usuariomodifica, " +
									"finalizada as finalizada, " +
									"coalesce(contenido_encabezado,'') as contenidoencabezado, " +
									"to_char(fecha_contenido, 'DD/MM/YYYY') as fechacontenido, " +
									"coalesce(hora_contenido,'') as horacontenido " +
								"from " +
									"epicrisis1 " +
								"where ingreso=? ";
	
	/**
	 * obtiene los tipos de evolucion dados los combinados de via ingreso - tipo paciente
	 * @param con
	 * @param viasIngresoTiposPaciente
	 * @return
	 */
	public HashMap<Object, Object> obtenerTiposEvolucion(Connection con, ArrayList<InfoDatosInt> viasIngresoTiposPaciente )
	{
		return SqlBaseEpicrisis1Dao.obtenerTiposEvolucion(con, viasIngresoTiposPaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @return
	 */
	public HashMap<Object, Object> cargarCuadroTexto(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, boolean cargarTodas, boolean cargarValoracionesIniciales)
	{
		return SqlBaseEpicrisis1Dao.cargarCuadroTexto(con, cuentas, criteriosBusquedaMap, ingreso, cargarTodas, cargarValoracionesIniciales);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @return
	 */
	public HashMap<Object, Object> obtenerFechaHoraSolicitudes(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, HashMap<Object, Object> valoracionesInicialesAmostrar)
	{
		return SqlBaseEpicrisis1Dao.obtenerFechaHoraSolicitudes(con, cuentas, criteriosBusquedaMap, ingreso, valoracionesInicialesAmostrar, "::TEXT", " order by psf.orden ");
	}
	
	/**
	 * Metodo que carga el detalle de la epicrisis, es un mapa que contiene los dto de epicrisis dependiendo del tipo de evolucion - solicitud dada,
	 * para optimizar recursos siempre se carga los detalles que esten dentro del rango del paginador (metodologia JIT).
	 * @param con
	 * @param fechasHorasMap
	 * @param indiceInicialPager
	 * @param indiceFinalPager
	 * @return
	 */
	public HashMap<Object, Object> cargarDetalleEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int indiceInicialPager, int indiceFinalPager, UsuarioBasico usuario, PersonaBasica paciente)
	{
		return SqlBaseEpicrisis1Dao.cargarDetalleEpicrisis(con, fechasHorasMap, indiceInicialPager, indiceFinalPager, usuario, paciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.insertarEpicrisisEventosAdversos(con, cuadroTextoMap, indice, loginUsuario);
	}
		
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.modificarEpicrisisEventosAdversos(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.insertarEpicrisisSolicitudes(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.modificarEpicrisisSolicitudes(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.insertarEpicrisisAdminMed(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.modificarEpicrisisAdminMed(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.insertarEpicrisisEvoluciones(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		return SqlBaseEpicrisis1Dao.modificarEpicrisisEvoluciones(con, cuadroTextoMap, indice, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param codigoTipoEvolucion
	 * @return
	 */
	public boolean existeEpicrisis(Connection con, String codigoPk, int codigoTipoEvolucion)
	{
		return SqlBaseEpicrisis1Dao.existeEpicrisis(con, codigoPk, codigoTipoEvolucion);
	}

	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public  boolean actualizarIndicativoEnviadoEpicrisisEventosAdversos(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicativoEnviadoEpicrisisEventosAdversos(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public  boolean actualizarIndicativoEnviadoEpicrisisProcedimientos(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicativoEnviadoEpicrisisProcedimientos(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public  boolean actualizarIndicativoEnviadoEpicrisisAdminMed(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicativoEnviadoEpicrisisAdminMed(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxServicio(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicaticoEnviadoEpicrisisCxServicio(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxSalidaPaciente(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicaticoEnviadoEpicrisisCxSalidaPaciente(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxNotasEnfermeria(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param tipoEvolucion
	 * @return
	 */
	public boolean eliminarEpicrisis(Connection con, String codigoPk, int tipoEvolucion)
	{
		return SqlBaseEpicrisis1Dao.eliminarEpicrisis(con, codigoPk, tipoEvolucion);
	}
	
	/**
	 * 
	 */
	public boolean actualizarEpicrisis(Connection con, DtoEpicrisis1 dto)
	{
		return SqlBaseEpicrisis1Dao.actualizarEpicrisis(con, dto);
	}
	
	/**
	 * 
	 */
	public boolean insertarEncabezadoEpicrisis(Connection con, DtoEpicrisis1 dto)
	{
		return SqlBaseEpicrisis1Dao.insertarEncabezadoEpicrisis(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean existeEncabezadoEpicrisis(Connection con, int ingreso)
	{
		return SqlBaseEpicrisis1Dao.existeEncabezadoEpicrisis(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean existeFinalizacionEpicrisis(Connection con, int ingreso)
	{
		return SqlBaseEpicrisis1Dao.existeFinalizacionEpicrisis(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double insertarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto)
	{
		return SqlBaseEpicrisis1Dao.insertarNotaAclaratoria(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarNotaAclaratoria(Connection con, double codigo)
	{
		return SqlBaseEpicrisis1Dao.eliminarNotaAclaratoria(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto )
	{
		return SqlBaseEpicrisis1Dao.modificarNotaAclaratoria(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public HashMap<Object, Object> cargarNotasAclaratorias (Connection con, int ingreso)
	{
		return SqlBaseEpicrisis1Dao.cargarNotasAclaratorias(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap<Object, Object> cargarMedicoElaboraEpicrisis(Connection con, int ingreso)
	{
		return SqlBaseEpicrisis1Dao.cargarMedicoElaboraEpicrisis(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Vector<String> obtenerAntecedentesParametrizadosEpicrisis(Connection con, int institucion)
	{
		return SqlBaseEpicrisis1Dao.obtenerAntecedentesParametrizadosEpicrisis(con, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double insertarEpicrisisSecciones(Connection con, DtoEpicrisisSecciones dto)
	{
		return SqlBaseEpicrisis1Dao.insertarEpicrisisSecciones(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double existeEpicrisisSeccionesEnviadas(Connection con, DtoEpicrisisSecciones dto)
	{
		return SqlBaseEpicrisis1Dao.existeEpicrisisSeccionesEnviadas(con, dto);
	}
	
	/**
	 * metodo que consulta las valoraciones iniciales de hospitalizacion y de urgencias
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap<Object, Object> cargarValoracionesIniciales(Connection con, Vector<String> cuentasIngreso)
	{
		return SqlBaseEpicrisis1Dao.cargarValoracionesIniciales(con, cuentasIngreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentasIngreso
	 * @param usuario
	 * @return
	 */
	public HashMap<Object, Object> cargarUltimaEvolucion(Connection con, Vector<String> cuentasIngreso, UsuarioBasico usuario)
	{
		return SqlBaseEpicrisis1Dao.cargarUltimaEvolucion(con, cuentasIngreso, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public boolean actualizarIndicativoEnviadoEpicrisisInterpretacion(Connection con, String codigo, String epicrisisSN)
	{
		return SqlBaseEpicrisis1Dao.actualizarIndicativoEnviadoEpicrisisInterpretacion(con, codigo, epicrisisSN);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> cargarInfoJusNoPos(Connection con, int numeroSolicitud, boolean esArticulo, int institucion, int codigoArticuloServicioOPCIONAL)
	{
		return SqlBaseEpicrisis1Dao.cargarInfoJusNoPos(con, numeroSolicitud, esArticulo, institucion, codigoArticuloServicioOPCIONAL);
	}
	
	/**
	 * 
	 * @param idIngreso
	 * @return
	 */
	public DtoEpicrisis1 cargarEpicrisis1(int idIngreso)
	{
		return SqlBaseEpicrisis1Dao.cargarEpicrisis1(idIngreso, consultaEpicrisis);
	}
	
	/**
	 * 
	 * @param valoracionesIniciales
	 * @return
	 */
	public boolean esValoracionInicialEnEpicrisis(HashMap<Object, Object> valoracionesIniciales)
	{
		return SqlBaseEpicrisis1Dao.esValoracionInicialEnEpicrisis(valoracionesIniciales);
	}
}
