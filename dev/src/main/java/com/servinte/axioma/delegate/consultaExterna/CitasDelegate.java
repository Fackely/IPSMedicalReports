/**
 * 
 */
package com.servinte.axioma.delegate.consultaExterna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.CitaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * @author jeilones
 * @created 23/10/2012
 *
 */
public class CitasDelegate {
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Consulta las citas que han sido atendidas por el un profesional X a un paciente Y
	 *
	 * @param loginUsuario
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 23/10/2012
	 */
	@SuppressWarnings("unchecked")
	public List<CitaDto> consultarCitasAtentidas(String loginUsuario,int codigoIngreso,int codigoPaciente) throws BDException{
		List<CitaDto> listaCitas=new ArrayList<CitaDto>(0);
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			HibernateUtil.beginTransaction();
			HashMap<String, Object>parametros=new HashMap<String, Object>(0);
			parametros.put("codigoEstadoCita", ConstantesBD.codigoEstadoCitaAtendida);
			parametros.put("codigoIngreso", codigoIngreso);
			parametros.put("usuario",loginUsuario);
			parametros.put("codigoPaciente", codigoPaciente);
			
			listaCitas=(List<CitaDto>) persistenciaSvc.createNamedQuery("citas.consultarCitasAtendidas",parametros);
			
			HibernateUtil.endTransaction();
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return listaCitas;
	}
	
	/**
	 * Consulta todos los profesionales que han atendido citas
	 * 
	 * @param codigoInsitucion
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 15/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<ProfesionalSaludDto> consultarProfesionalesAtiendenCitas(int codigoInsitucion) throws BDException{
		List<ProfesionalSaludDto> listaProfesionales=new ArrayList<ProfesionalSaludDto>(0);
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			HibernateUtil.beginTransaction();
			HashMap<String, Object>parametros=new HashMap<String, Object>(0);
			parametros.put("codigoInstitucion", codigoInsitucion);
			
			listaProfesionales=(List<ProfesionalSaludDto>) persistenciaSvc.createNamedQuery("citas.consultarProfesionalesAtiendenCitas",parametros);
			
			HibernateUtil.endTransaction();
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return listaProfesionales;
	}
}
