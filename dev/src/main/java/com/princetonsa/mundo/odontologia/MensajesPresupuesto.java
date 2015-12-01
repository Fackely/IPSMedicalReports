package com.princetonsa.mundo.odontologia;

import java.io.Serializable;

/**
 *Clase para validar si se presentan mensajes informativos del presupuesto
 * @author Edgar Carvajal Ruiz
 *
 */
public class MensajesPresupuesto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Atributo para Saber si se presenta el siguiente Mensaje informatico:
	 * El paciente actualmente tiene otro  presupuesto en estado 
	 * Contratado o Suspendido Temporalmente, no se permite contratar nuevos presupuestos
	 * Este Atributo solo es utilizado en la interfaz grafica
	 */
	private boolean presentaMensajeInformativoContratarPresupusto;
	
	
	
	
	/**
	 * Construtor
	 */
	public MensajesPresupuesto(){
		this.setPresentaMensajeInformativoContratarPresupusto(Boolean.FALSE);
		
	}


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param presentaMensajeInformativoContratarPresupusto
	 */
	public void setPresentaMensajeInformativoContratarPresupusto(
			boolean presentaMensajeInformativoContratarPresupusto) {
		this.presentaMensajeInformativoContratarPresupusto = presentaMensajeInformativoContratarPresupusto;
	}



	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public boolean isPresentaMensajeInformativoContratarPresupusto() {
		return presentaMensajeInformativoContratarPresupusto;
	}
	
	
	

}
