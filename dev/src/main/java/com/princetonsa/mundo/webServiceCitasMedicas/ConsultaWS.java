/**
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.mundo.webServiceCitasMedicas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;

import util.UtilidadBD;
import util.Utilidades;


/**
 * 
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class ConsultaWS
{

	/**
	 * Metodo que retorna un mapa con las especialidades que tiene la institucion.
	 * @return
	 */
	public static HashMap consultarEspecialidades()
	{
		return Utilidades.obtenerUnidadesConsulta();
	}

	/**
	 * Metodo que retorna los medicos, dada una especialidad.
	 * @param codigoEspecilidad
	 * @return
	 */
	public static HashMap consultarMedicosEspecialidades(String codigoEspecilidad)
	{
		return Utilidades.obtenerMedicosUnidadConsulta(codigoEspecilidad);
	}

	/**
	 * Metotodo que retorna en numero de citas atendias por el portal.
	 * @return
	 */
	public static int obtenerCitasAntendiasPortal(String fechaInicial, String fechaFinal)
	{
		int numero=0;
		Connection con=UtilidadBD.abrirConexion();
		numero=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaWSDao().obtenerCitasAntendiasPortal(con,fechaInicial,fechaFinal);
		UtilidadBD.closeConnection(con);
		return numero;
	}

	/**
	 * Metoto que retorna el numero de citas solicitadas del portal.
	 * @return
	 */
	public static int obtenerCitasSolicitadasPortal(String fechaInicial, String fechaFinal)
	{
		int numero=0;
		Connection con=UtilidadBD.abrirConexion();
		numero=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaWSDao().obtenerCitasSolicitadasPortal(con,fechaInicial,fechaFinal);
		UtilidadBD.closeConnection(con);
		return numero;
	}

	/**
	 * Metodo que retorna el total de citas.
	 * @return
	 */
	public static int obtenerTotalCitas(String fechaInicial, String fechaFinal)
	{
		int numero=0;
		Connection con=UtilidadBD.abrirConexion();
		numero=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaWSDao().obtenerTotalCitas(con,fechaInicial,fechaFinal);
		UtilidadBD.closeConnection(con);
		return numero;
	}

}
