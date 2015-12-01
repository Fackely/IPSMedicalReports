package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.dto.manejoPaciente.InformacionCentroCostoValoracionDto;

public interface EvolucionesDao 
{

	
	/**
	 * 
	 * @param con
	 * @param evolucion
	 * @return
	 */
	public DtoEvolucion cargarEvolucion(Connection con, String evolucion);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public int insertarEvolucionBase(Connection con, HashMap mapa);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarEvolucionesHospitalarias(Connection con, HashMap mapa);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarEvolSignosVitales(Connection con, HashMap mapa);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarDiagnosticosEvolucion(Connection con, HashMap mapa);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarBalanceLiquidosEvol(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarIngresoCuidadoEspecial(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param codigoTipoEvolucion
	 * @return
	 */
	public int consultarCausaExternaValoracion(Connection con, int codigoCuenta, int codigoTipoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int obtenerCodigoUltimaEvolucion(Connection con, int codigoCuenta);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean actualizarIngresoCuidadoEspecial(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public String[] obtenerDiagnosticoComplicacion(Connection con, int codigoEvolucion);

	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public String[] obtenerDiagnosticoPrincipal(Connection con, int codigoEvolucion);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public String[] consultarTipoMonitoreo(Connection con, int codigoIngreso);

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean actualizarAreaCuenta(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public ArrayList<Diagnostico> consultaDiagnosticosRelacionados(Connection con, int codigoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String[] consultarInfoFallecimiento(Connection con, int codigoCuenta);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @param seccionFija
	 * @return
	 */
	public boolean enviadoEpicrisis(Connection con, String codigoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarComentariosEvolucion(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public String[] consultarOrdenEgreso(Connection con, int codigoEvolucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public int obtenerUltimaConducta(Connection con, int codigoEvolucion);

	/**
	 * MT 5568 
	 * Metodo que consulta la informacion del area del paciente y el tipo de monitoreo por cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 * @author javrammo
	 */
	public InformacionCentroCostoValoracionDto informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(Connection con, int idCuenta);

	
	
}
