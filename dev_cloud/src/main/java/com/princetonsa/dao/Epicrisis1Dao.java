package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;

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
public interface Epicrisis1Dao 
{
	/**
	 * obtiene los tipos de evolucion dados los combinados de via ingreso - tipo paciente
	 * @param con
	 * @param viasIngresoTiposPaciente
	 * @return
	 */
	public HashMap<Object, Object> obtenerTiposEvolucion(Connection con, ArrayList<InfoDatosInt> viasIngresoTiposPaciente );
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @return
	 */
	public HashMap<Object, Object> cargarCuadroTexto(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, boolean cargarTodas, boolean cargarValoracionesIniciales);
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @param valoracionesInicialesAmostrar 
	 * @return
	 */
	public HashMap<Object, Object> obtenerFechaHoraSolicitudes(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, HashMap<Object, Object> valoracionesInicialesAmostrar);

	/**
	 * Metodo que carga el detalle de la epicrisis, es un mapa que contiene los dto de epicrisis dependiendo del tipo de evolucion - solicitud dada,
	 * para optimizar recursos siempre se carga los detalles que esten dentro del rango del paginador (metodologia JIT).
	 * @param con
	 * @param fechasHorasMap
	 * @param indiceInicialPager
	 * @param indiceFinalPager
	 * @return
	 */
	public HashMap<Object, Object> cargarDetalleEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int indiceInicialPager, int indiceFinalPager, UsuarioBasico usuario, PersonaBasica paciente);
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	

	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public boolean modificarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param codigoTipoEvolucion
	 * @return
	 */
	public boolean existeEpicrisis(Connection con, String codigoPk, int codigoTipoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public  boolean actualizarIndicativoEnviadoEpicrisisEventosAdversos(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public  boolean actualizarIndicativoEnviadoEpicrisisProcedimientos(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public  boolean actualizarIndicativoEnviadoEpicrisisAdminMed(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxServicio(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxSalidaPaciente(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxNotasEnfermeria(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public  boolean actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param tipoEvolucion
	 * @return
	 */
	public boolean eliminarEpicrisis(Connection con, String codigoPk, int tipoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarEpicrisis(Connection con, DtoEpicrisis1 dto);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarEncabezadoEpicrisis(Connection con, DtoEpicrisis1 dto);
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean existeEncabezadoEpicrisis(Connection con, int ingreso);
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public boolean existeFinalizacionEpicrisis(Connection con, int ingreso);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double insertarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarNotaAclaratoria(Connection con, double codigo);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto );
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public HashMap<Object, Object> cargarNotasAclaratorias (Connection con, int ingreso);
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap<Object, Object> cargarMedicoElaboraEpicrisis(Connection con, int ingreso);
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Vector<String> obtenerAntecedentesParametrizadosEpicrisis(Connection con, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double insertarEpicrisisSecciones(Connection con, DtoEpicrisisSecciones dto);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double existeEpicrisisSeccionesEnviadas(Connection con, DtoEpicrisisSecciones dto);

	/**
	 * metodo que consulta las valoraciones iniciales de hospitalizacion y de urgencias
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap<Object, Object> cargarValoracionesIniciales(Connection con, Vector<String> cuentasIngreso);
	
	/**
	 * 
	 * @param con
	 * @param cuentasIngreso
	 * @param usuario
	 * @return
	 */
	public HashMap<Object, Object> cargarUltimaEvolucion(Connection con, Vector<String> cuentasIngreso, UsuarioBasico usuario);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public boolean actualizarIndicativoEnviadoEpicrisisInterpretacion(Connection con, String codigo, String epicrisisSN);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<Object, Object> cargarInfoJusNoPos(Connection con, int numeroSolicitud, boolean esArticulo, int institucion, int codigoArticuloServicioOPCIONAL);
	
	/**
	 * 
	 * @param idIngreso
	 * @return
	 */
	public DtoEpicrisis1 cargarEpicrisis1(int idIngreso);

	/**
	 * 
	 * @param valoracionesIniciales
	 * @return
	 */
	public boolean esValoracionInicialEnEpicrisis(HashMap<Object, Object> valoracionesIniciales);
}
