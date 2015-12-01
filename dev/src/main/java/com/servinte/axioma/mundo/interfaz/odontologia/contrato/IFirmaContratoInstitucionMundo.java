package com.servinte.axioma.mundo.interfaz.odontologia.contrato;

import com.servinte.axioma.orm.FirmasContratoOtrosiInst;


/**
 * 
 * @author axioma
 *
 */
public interface IFirmaContratoInstitucionMundo {
	
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(FirmasContratoOtrosiInst objeto);

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(FirmasContratoOtrosiInst objeto);
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( FirmasContratoOtrosiInst objeto);

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public FirmasContratoOtrosiInst buscarxId(Number id);
	
	
	
	

}
