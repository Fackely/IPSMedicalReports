package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;

/**
 * Esta clase se encarga de encarga de definir los m�todos de 
 * negocio para la entidad AutorizacionesEntSubArticu
 * 
 * @author Angela Maria Aguirre
 * @since 9/12/2010
 */
public interface IAutorizacionesEntSubArticuloDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos un registro de
	 * art�culo de una autorizaci�n de entidad subcontratada
	 * 
	 * @param AutorizacionesEntSubArticu art�culo
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarArticuloAutorizacionEntidadSub(AutorizacionesEntSubArticu articulo);
		
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los datos de los art�culos
	 * de una autorizaci�n de capitaci�n, a trav�s del id de su respectiva
	 * autorizaci�n de entidad subcontratada
	 * 
	 * @param long idAutorEntSub
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ArrayList<DtoArticulosAutorizaciones> obtenerDetalleArticulosAutorCapitacion(long idAutorEntSub);
	
	
	/**
	 * Listar los articulos por autorizacion 
	 * @author Fabi�n Becerra
	 * @param dtoParametros
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros);

}
