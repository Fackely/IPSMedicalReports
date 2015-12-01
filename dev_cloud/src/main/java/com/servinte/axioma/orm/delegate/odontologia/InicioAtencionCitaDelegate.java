package com.servinte.axioma.orm.delegate.odontologia;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.InicioAtencionCita;
import com.servinte.axioma.orm.InicioAtencionCitaHome;


/**
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con la entidad InicioAtencionCita
 * 
 * @author Fabian Becerra
 * @since 17/11/2010
 */
public class InicioAtencionCitaDelegate extends InicioAtencionCitaHome{
	
	/**
	 * 
	 * Este Método se encarga de guardar el registro de inicio de atención
	 * de cita odontológica
	 * 
	 * @param inicioAtencionCita
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean guardarInicioAtencionCitaOdonto(InicioAtencionCita iac){
		boolean save = true;					
		try{
			super.persist(iac);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de" +
					" inicio atencion de citas: ",e);
		}				
		return save;				
	}
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar un registro de inicio de atención
	 * de cita odontológica
	 * 
	 * @param inicioAtencionCita
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public boolean actualizarInicioAtencionCitaOdonto(InicioAtencionCita iac){
		boolean save = true;					
		try{
			super.merge(iac);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro de" +
					" inicio atencion de citas: ",e);
		}				
		return save;				
	}
	
	/**
	 * 
	 * Este Método se encarga de encontrar un registro de inicio de atención
	 * de cita odontológica por su id = codigo cita
	 * 
	 * @param inicioAtencionCita
	 * @return boolean
	 * @author, Fabian Becerra
	 *
	 */
	public InicioAtencionCita buscarRegistroInicioAtencionCitaOdontoPorID(long codigoCita){
		InicioAtencionCita ic=null;
		try{
			ic=super.findById(codigoCita);
		} catch (Exception e) {
			Log4JManager.error("No se pudo encontrar el registro de" +
					" inicio atencion de citas: ",e);
		}				
		return ic;				
	}

}
