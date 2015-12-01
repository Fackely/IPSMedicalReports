package com.servinte.axioma.servicio.interfaz.odontologia.contrato;

import java.sql.Connection;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoFirmasContOtrsiMultiEmpresa;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.FirmasContOtrsiempr;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public interface IFirmaContratoMultiEmpresasServicio 
{
	

	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param firmas
	 * @return
	 */
	 public List<FirmasContOtrsiempr> cargarFirmasPorInstucion(FirmasContOtrsiempr firmas);
	
	 
	 /**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(FirmasContOtrsiempr objeto);

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(FirmasContOtrsiempr objeto);
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( FirmasContOtrsiempr objeto);

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public FirmasContOtrsiempr buscarxId(Number id);
	
	

	/**
	 * MODIFICAR LAS FIRMAS MULTI EMPRESA 
	 * @param listaDTOFirmas
	 * @param codEmpresaInstitucion
	 * @param usuario
	 */
	public void modificarFirmasMultiEmpresa(List<DtoFirmasContOtrsiMultiEmpresa> listaDTOFirmas, 
											String codEmpresaInstitucion, 
											UsuarioBasico usuario, Connection con);
	
	

}
