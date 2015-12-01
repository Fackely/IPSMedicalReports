package com.servinte.axioma.servicio.impl.odontologia.ventaTarjeta;

import com.servinte.axioma.mundo.fabrica.odontologia.ventaTarjeta.VentaTarjetaMundoFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IBeneficiarioMundo;
import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.servicio.interfaz.odontologia.ventaTarjeta.IBeneficiarioServicio;

/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class BeneficiarioServicio  implements IBeneficiarioServicio{
	
	private IBeneficiarioMundo beneficiarioMundo;
	
	/**
	 *Construtor 
	 */
	public 	BeneficiarioServicio(){
		beneficiarioMundo =VentaTarjetaMundoFabrica.crearBeneficiarioMundo();
	}
	
	@Override
	public BeneficiarioTarjetaCliente buscarxId(Number id) {
		return beneficiarioMundo.buscarxId(id);
	}
	
	@Override
	public void eliminar(BeneficiarioTarjetaCliente objeto) {
		beneficiarioMundo.eliminar(objeto);
	}

	@Override
	public void insertar(BeneficiarioTarjetaCliente objeto) {
		beneficiarioMundo.insertar(objeto);
	}

	@Override
	public void modificar(BeneficiarioTarjetaCliente objeto) {
		beneficiarioMundo.modificar(objeto);
		
	}

	@Override
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersona, boolean filtrarTarjetasActivas)
	{
		return beneficiarioMundo.obtenerBeneficiarioPersona(codigoPersona, filtrarTarjetasActivas);
	}

	@Override
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPaciente(
			int codigoPersona) {
		
		return beneficiarioMundo.obtenerBeneficiarioPaciente(codigoPersona);
	}


}
