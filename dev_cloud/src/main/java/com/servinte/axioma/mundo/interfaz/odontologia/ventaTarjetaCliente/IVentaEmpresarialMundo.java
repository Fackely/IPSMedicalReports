package com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente;

import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.orm.VentaEmpresarial;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public interface IVentaEmpresarialMundo {
	
	
	/**
	 *  Metodo que Guardar VentaEmpresarial
	 * @author Edgar Carvajal Ruiz
	 */
	public void insertar(VentaEmpresarial objeto);
	
	
	/**
	 * Metodo que recibe un objeto de VentaEmpresarial  y lo modifica de la base de datos
	 * @author Edgar Carvajal 
	 * @param objecto
	 */
	 public void modificar(VentaEmpresarial objeto);
	
	
	/**
	 * Metodo que recibe un objeto de tipo VentaEmpresarial y lo elimina de la base de datos
	 * @author Edgar Carvajal
	 * @param objecto
	 */
	public void eliminar( VentaEmpresarial objeto);

	
	/**
	 * Metodo que recive un Id y retorna un Tipo de Objeto VentaEmpresarial
	 * @author Edgar Carvajal
	 * @param objeto
	 * @param id
	 * @return
	 */
	public VentaEmpresarial buscarxId(Number id);
	
	

}
