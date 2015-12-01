

package com.servinte.axioma.vista.odontologia.presupuesto;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.odontologia.InfoConvenioContratoPresupuesto;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public  class PresupuestoHelperVista {
	
	
	/**
	 * Contrutor privado para que no generen instancias
	 */
	private PresupuestoHelperVista(){
		
	}
	
	
	
	/**
	 * Metodo para cargar el  atributo puedeo contratatar o precontratar utilizado en:
	 * Se presenta si el CENTRO DE ATENCION que generó el presupuesto es IGUAL al CENTRO DE ATENCION por el cual se LOGUEO el usuario
	 * @author Edgar Carvajal Ruiz
	 * @param listaPresupuestos
	 * @param usuario
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologico> validarCentroAtencionPresupuesto(
																						ArrayList<DtoPresupuestoOdontologico> listaPresupuestos,
																						UsuarioBasico usuario){
		/*
		 *Iteracion de Prespuesto 
		 */
		for(DtoPresupuestoOdontologico dtoPresupuesto: listaPresupuestos)
		{
			if(dtoPresupuesto.getCentroAtencion().getCodigo()==usuario.getCodigoCentroAtencion())
			{
				dtoPresupuesto.setPuedoContratarPrecontratar(Boolean.TRUE);
			}
		}
		
		return listaPresupuestos;
	}

	
	
	
	
	/**
	 * Metodo que recibe una lista de convenios paciente y  retorna una arraylist de infoconvenioContratoPresupuesto 
	 * @param listaConvenios
	 * @return
	 */
	public  static ArrayList<InfoConvenioContratoPresupuesto> transformarListConvenios( List<ConveniosIngresoPaciente>  listConvenioIngresoPa){
		
		
		ArrayList<InfoConvenioContratoPresupuesto> listConvenio= new ArrayList<InfoConvenioContratoPresupuesto>();
	
		
		try{
		
			for(ConveniosIngresoPaciente convenioIngreso: listConvenioIngresoPa)
			{
				InfoConvenioContratoPresupuesto infoConvenioCont= new InfoConvenioContratoPresupuesto();
				
				infoConvenioCont.setContrato(new InfoDatosInt(convenioIngreso.getContratos().getCodigo(), convenioIngreso.getContratos().getNumeroContrato()));
				infoConvenioCont.setConvenio(new InfoDatosInt(convenioIngreso.getContratos().getConvenios().getCodigo(), convenioIngreso.getContratos().getConvenios().getNombre()));
				infoConvenioCont.setAjusteServicio(convenioIngreso.getContratos().getConvenios().getAjusteServicios());
				
				if(convenioIngreso.getActivo()==ConstantesBD.acronimoSiChar)
				{
					infoConvenioCont.setActivo(true);
				}
				
				listConvenio.add(infoConvenioCont);
			}
		}
		catch (Exception e) {
			 Log4JManager.info(e);
			 Log4JManager.error(e);
			 }
		
		return listConvenio;
	}
}
