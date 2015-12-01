package com.servinte.axioma.servicio.fabrica.odontologia.contrato;


import com.servinte.axioma.servicio.impl.odontologia.contrato.ContratoOndologicoServicio;
import com.servinte.axioma.servicio.impl.odontologia.contrato.FirmaContratoMultiEmpresasServicio;
import com.servinte.axioma.servicio.impl.odontologia.contrato.OtrosSiServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IContratoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresasServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.contrato.IOtrosSiServicio;



/**
 * CONTRATO SERVICIO FABRICA 
 * @author axioma
 *
 */
public abstract class ContratoFabricaServicio 

{
	
	/**
	 * Construtor private para no genera instancia de la fabrica
	 */
	private ContratoFabricaServicio(){
		
	}
	
	
	/**
	 * CREAR INSTANCIA CONTRATO ODONTOLOGICO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static  final IContratoOdontologicoServicio crearContratoOdontologico()
	{
		return new ContratoOndologicoServicio();
	}
	
	
	
	/**
	 * CREA UNA INSTANCIA DE FIRMA MULTIEMPRESA 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IFirmaContratoMultiEmpresasServicio crearFirmaMultiempresa()
	{
		return new FirmaContratoMultiEmpresasServicio();
	}
	
	
	/**
	 * Retorna la implementacion de la interfaz IOtrosSiMundo
	 * @return IOtrosSiMundo
	 */
	public static IOtrosSiServicio crearOtrosSiServicio(){
		return new OtrosSiServicio();
	}

}
