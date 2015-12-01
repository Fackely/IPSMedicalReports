package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.orm.NivelAutorizacion;

/**
 * Esta clase se encarga de definir los métodos de negocio de 
 * la entidad Nivel de Autorización
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public interface INivelAutorizacionServicio {
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrada en el sistema
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTONivelAutorizacion> buscarNivelAutorizacion();
	
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacion(NivelAutorizacion registro);
	
	
	/**
	 * 
	 * Este Método se encarga de consultar nivel de autorización de un usuario
	 * 
	 * @return ArrayList<DtoValidacionNivelesAutorizacion>
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorUsuario(String login);
			
	/**
	 * 
	 * Este Método se encarga de consultar nivel de autorización por ocupación medica
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorOcupacionMedica(int codigoOcupacion);
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacion(NivelAutorizacion registro);

	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacion(int idNivelAutorizacion);
	
	/**
	 * 
	 * Este método se encarga de verificar si el registro de un nivel de 
	 * autorización es nuevo para realizar su inserción o su actualización 
	 * 
	 * @param NivelAutorizacion registro
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarRegistroNivelAutorizacion(NivelAutorizacion registro);
	
	/**
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo Gómez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelAutorizacion();

}
