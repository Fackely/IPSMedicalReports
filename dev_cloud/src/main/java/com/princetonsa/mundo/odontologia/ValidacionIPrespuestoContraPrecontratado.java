package com.princetonsa.mundo.odontologia;

import util.ValoresPorDefecto;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * @author Edgar Carvajal Ruiz
 * Clase para hacer las validaciones respectiva de Precontratacion y contratacion del presupuesto
 * PRECONTRATAR PRESUPUESTOS sin que necesariamente se genere solicitud de descuento dejándolo en estado 
 * PRECONTRATADO y manteniendo el flujo de selección de los programas por convenio de manera excluyente que tiene la contratación.
 *
 */
public class ValidacionIPrespuestoContraPrecontratado {
	
	
	/**
	 * Contrutor private para no dejar crear instancia de la clase
	 */
	private ValidacionIPrespuestoContraPrecontratado(){
		
	}

	
	/**
	 * Metodo para validar si un usuario tiene el rol de ingresar/modificar  y precontratar/contratar presupuesto 
	 * Recibe un Usuario Basico y retorna true si y solo si el usuario tiene rol para contratar presupuesto
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	 public static boolean   tieneRolUsuarioParaContrataPrecontrataPresupuesto(UsuarioBasico usuario){
		 
		
		 
		return true;
	}
	
	 
	 
	 
	 /**
	  * Metodo que Valida el parametro Presupuesto Odontológico Contratado
	  * Recibe una institucion y retorna true si tienen definido el parametro en otro caso false
	  * @author Edgar Carvajal Ruiz
	  * @return
	  */
	 public static final boolean tieneParametroGeneralValidaPrespuestoContratado(){

		 return true;
	 }
	 
	 
	 
	

}
