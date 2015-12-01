package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.orm.NivelAutorizacion;

/**
 * Esta clase se encarga de definir los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public interface INivelAutorizacionServicio {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrada en el sistema
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTONivelAutorizacion> buscarNivelAutorizacion();
	
	
	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacion(NivelAutorizacion registro);
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar nivel de autorizaci�n de un usuario
	 * 
	 * @return ArrayList<DtoValidacionNivelesAutorizacion>
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorUsuario(String login);
			
	/**
	 * 
	 * Este M�todo se encarga de consultar nivel de autorizaci�n por ocupaci�n medica
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorOcupacionMedica(int codigoOcupacion);
	
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar los niveles de autorizaci�n
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacion(NivelAutorizacion registro);

	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacion(int idNivelAutorizacion);
	
	/**
	 * 
	 * Este m�todo se encarga de verificar si el registro de un nivel de 
	 * autorizaci�n es nuevo para realizar su inserci�n o su actualizaci�n 
	 * 
	 * @param NivelAutorizacion registro
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarRegistroNivelAutorizacion(NivelAutorizacion registro);
	
	/**
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo G�mez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelAutorizacion();

}
