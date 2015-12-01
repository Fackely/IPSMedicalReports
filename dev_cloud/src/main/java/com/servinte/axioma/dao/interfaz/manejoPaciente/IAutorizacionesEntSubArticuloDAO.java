package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;

/**
 * Esta clase se encarga de encarga de definir los métodos de 
 * negocio para la entidad AutorizacionesEntSubArticu
 * 
 * @author Angela Maria Aguirre
 * @since 9/12/2010
 */
public interface IAutorizacionesEntSubArticuloDAO {
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos un registro de
	 * artículo de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntSubArticu artículo
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarArticuloAutorizacionEntidadSub(AutorizacionesEntSubArticu articulo);
		
	
	/**
	 * 
	 * Este Método se encarga de consultar los datos de los artículos
	 * de una autorización de capitación, a través del id de su respectiva
	 * autorización de entidad subcontratada
	 * 
	 * @param long idAutorEntSub
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ArrayList<DtoArticulosAutorizaciones> obtenerDetalleArticulosAutorCapitacion(long idAutorEntSub);
	
	
	/**
	 * Listar los articulos por autorizacion 
	 * @author Fabián Becerra
	 * @param dtoParametros
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros);

}
