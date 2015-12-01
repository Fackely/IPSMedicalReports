package com.servinte.axioma.bl.util.impl;

import java.sql.Connection;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.mundo.PersonaBasica;
import com.servinte.axioma.bl.util.interfaz.IUtilidadesMundo;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Clase que implementa todos los servicios de lógica de negocio del las utilidades 
 * de negocio tranversales a toda la apliación
 * 
 * @author ricruico
 * @version 1.0
 * @created 27-jun-2012 02:24:01 p.m.
 */
public class UtilidadesMundo implements IUtilidadesMundo{

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.util.interfaz.IUtilidadesMundo#existeConsecutivo(java.lang.String, int)
	 * @author ricruico
	 */
	@Override
	public boolean existeConsecutivo(String nombreConsecutivo, int institucion)
			throws IPSException {
		boolean existe=false;
		Connection con= null;
		try{
			con=UtilidadBD.abrirConexion();
			String result=UtilidadBD.obtenerValorActualTablaConsecutivos(con, nombreConsecutivo, institucion);
			if(result != null && !result.trim().isEmpty() && !result.equals(ConstantesBD.codigoNuncaValido+"")){
				existe=true;
			}
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return existe;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.util.interfaz.IUtilidadesMundo#obtenerConsecutivo(java.lang.String, int)
	 * @author ricruico
	 */
	@Override
	public Integer obtenerConsecutivo(String nombreConsecutivo, int institucion)
			throws IPSException {
		Integer consecutivo=null;
		try{
			consecutivo=Integer.valueOf(UtilidadBD.obtenerValorConsecutivoDisponible(nombreConsecutivo, institucion));
		}
		catch (ClassCastException cce) {
			Log4JManager.error(cce.getMessage(),cce);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_CAST, cce);
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return consecutivo;
	}
	
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.util.interfaz.IUtilidadesMundo#cargarPaciente(int, int, int)
	 * @author ricruico
	 */
	@Override
	public PersonaBasica cargarPaciente(int codigoPaciente, int codigoInstitucion, int codigoCentroAtencion)
			throws IPSException {
		PersonaBasica paciente=new PersonaBasica();
		Connection con=null;
		try{
			con=UtilidadBD.abrirConexion();
			paciente.cargar(con, codigoPaciente);
			paciente.cargarPaciente(con, codigoPaciente, String.valueOf(codigoInstitucion), String.valueOf(codigoCentroAtencion));
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return paciente;
	}

	
	
}
