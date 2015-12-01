package com.servinte.axioma.servicio.interfaz.odontologia.contrato;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFirmasContOtrosInstOdont;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.ContratoOdontologico;


/**
 * 
 * @author Edgar Carvajal
 */
public interface IContratoOdontologicoServicio 
{

	
	
	/**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(ContratoOdontologico objeto);

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(ContratoOdontologico objeto);
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( ContratoOdontologico objeto);

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public ContratoOdontologico buscarxId(Number id);
	
	
	
	
	
	/**
	 * METO PARA ARMAR EL DTO CONTRATOS INSTITUCION
	 * @author Edgar Carvajal Ruiz
	 * @param contratoOdontologico
	 * @return
	 */
	public ArrayList<DtoFirmasContOtrosInstOdont> consultaDtoFirmasContrato(ContratoOdontologico contratoOdontologico);
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoContrato
	 * @return
	 */
	public ContratoOdontologico consultarAvanzadaContratoOdon(ContratoOdontologico dtoContrato);
	
	
	
	/**
	 * METODO QUE INSERTA MODIFICA Y ELIMINAR CONTRATOS CON SUS DETALLE DE FIRMAS DE INSTITUCION
	 * @author Edgar Carvajal Ruiz
	 * @param contrato
	 * @param listaFirmas
	 * @param usuario
	 */
	public void insertarModificarEliminar(ContratoOdontologico contrato, 
			ArrayList<DtoFirmasContOtrosInstOdont> listaFirmas,
			UsuarioBasico usuario ) ;
	
	
	
	
	

}
