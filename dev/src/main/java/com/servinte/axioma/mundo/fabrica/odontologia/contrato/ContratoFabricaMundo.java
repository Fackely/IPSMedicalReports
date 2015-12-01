package com.servinte.axioma.mundo.fabrica.odontologia.contrato;

import com.servinte.axioma.mundo.impl.odontologia.contrato.ContratoOdontologicoMundo;
import com.servinte.axioma.mundo.impl.odontologia.contrato.FirmaContratoInstitucionMundo;
import com.servinte.axioma.mundo.impl.odontologia.contrato.FirmaContratoMultiEmpresaMundo;
import com.servinte.axioma.mundo.impl.odontologia.contrato.OtrosSiMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IContratoOdontologicoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IFirmaContratoInstitucionMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IOtrosSiMundo;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public abstract  class ContratoFabricaMundo 
{

	
	/**
	 * METODO QUE CREAR UNA INSTANCIA DE CONTRATOS ODONTOLOGICOS MUNDO 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static  IContratoOdontologicoMundo crearContratoOdontologicoMundo()
	{
		return new ContratoOdontologicoMundo();
	}
	
	
	/**
	 * CREAR FIRMAS MULTIEMPRESA 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IFirmaContratoMultiEmpresaMundo crearFirmaMultiEmpresaMundo(){
		return new FirmaContratoMultiEmpresaMundo();
	}
	
	
	
	/**
	 * CREAR FIRMA INSTITUCION
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static IFirmaContratoInstitucionMundo crearFirmaInstitucion()
	{
		return new FirmaContratoInstitucionMundo();
	}

	
	
	/**
	 * Retorna la implementacion de la interfaz IOtrosSiMundo
	 * @return IOtrosSiMundo
	 */
	public static IOtrosSiMundo crearOtrosSiMundo(){
		return new OtrosSiMundo();
	}
	
	
}
