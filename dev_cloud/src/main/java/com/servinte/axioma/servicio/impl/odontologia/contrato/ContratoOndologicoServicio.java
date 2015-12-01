package com.servinte.axioma.servicio.impl.odontologia.contrato;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFirmasContOtrosInstOdont;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.odontologia.contrato.ContratoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IContratoOdontologicoMundo;
import com.servinte.axioma.orm.ContratoOdontologico;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IContratoOdontologicoServicio;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class ContratoOndologicoServicio implements IContratoOdontologicoServicio{

	
	
	/**
	 * INTERFAZ CONTRATO MUNDO
	 */
	private IContratoOdontologicoMundo mundoI;
	
	
	
	/**
	 * 
	 */
	public  ContratoOndologicoServicio()
	{
		mundoI=ContratoFabricaMundo.crearContratoOdontologicoMundo();
	}
	
	
	
	
	@Override
	public ContratoOdontologico buscarxId(Number id) {
	
		return mundoI.buscarxId(id);
	}

	@Override
	public ArrayList<DtoFirmasContOtrosInstOdont> consultaDtoFirmasContrato(
			ContratoOdontologico contratoOdontologico) {
		
		return mundoI.consultaDtoFirmasContrato(contratoOdontologico);
	}

	@Override
	public void eliminar(ContratoOdontologico objeto) {
	
		mundoI.eliminar(objeto);
	}

	@Override
	public void insertar(ContratoOdontologico objeto) {
		mundoI.insertar(objeto);
		
	}

	@Override
	public void modificar(ContratoOdontologico objeto) {
		mundoI.modificar(objeto);
		
	}


	@Override
	public ContratoOdontologico consultarAvanzadaContratoOdon(ContratoOdontologico dtoContrato)
	{
		return mundoI.consultarAvanzadaContratoOdon(dtoContrato);
	}




	@Override
	public void insertarModificarEliminar(ContratoOdontologico contrato, 
										ArrayList<DtoFirmasContOtrosInstOdont> listaFirmas,
										UsuarioBasico usuario ) 
	{
		mundoI.insertarModificarEliminar(contrato, listaFirmas, usuario);
		
	}




	
	
	

}
