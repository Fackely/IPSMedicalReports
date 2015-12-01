package com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dao.fabrica.odontologia.ventastarjeta.VentaTarjetaFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IBeneficiarioDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IBeneficiarioMundo;
import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class BeneficiarioMundo implements IBeneficiarioMundo{
	
	/**
	 * 
	 */
	private IBeneficiarioDAO beneficiarioDAO;
	
	/**
	 * Construtor
	 */
	public BeneficiarioMundo(){
		beneficiarioDAO=VentaTarjetaFabricaDAO.crearBeneficiarioDAO();
	}

	@Override
	public BeneficiarioTarjetaCliente buscarxId(Number id) {
		
		return beneficiarioDAO.buscarxId(id);
	}

	@Override
	public void eliminar(BeneficiarioTarjetaCliente objeto) {
		beneficiarioDAO.eliminar(objeto);
		
	}

	@Override
	public void insertar(BeneficiarioTarjetaCliente objeto) {
		beneficiarioDAO.insertar(objeto);
		
	}

	@Override
	public void modificar(BeneficiarioTarjetaCliente objeto) {
		beneficiarioDAO.modificar(objeto);
		
	}
	
	@Override
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersona, boolean filtrarTarjetasActivas)
	{
		return beneficiarioDAO.obtenerBeneficiarioPersona(codigoPersona, filtrarTarjetasActivas);
	}

	
	
	@Override
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPaciente(int codigoPersona)
	{	
		
		BeneficiarioTarjetaCliente beneficiario= new BeneficiarioTarjetaCliente();
		
		if(codigoPersona>0)
		{
			beneficiario = cargarBeneficiarioPaciente(codigoPersona, beneficiario);
		}
			
		return  beneficiario;
	}

	
	
	/**
	 * 
	 * @param codigoPersona
	 * @param beneficiario
	 * @return
	 */
	private BeneficiarioTarjetaCliente cargarBeneficiarioPaciente(int codigoPersona, BeneficiarioTarjetaCliente beneficiario) {
		try {
			UtilidadTransaccion.getTransaccion().begin();
			
			beneficiario = beneficiarioDAO.obtenerBeneficiarioPersona(codigoPersona);
			
			if(beneficiario!=null){
				
				beneficiario.getTiposTarjCliente().getCodigoPk();
				beneficiario.getVentaTarjetaCliente().getCodigoPk();
				beneficiario.getTiposTarjCliente().getCodigoTipoTarj();
				beneficiario.getVentaTarjetaCliente().getTipoVenta();
			}
			
			UtilidadTransaccion.getTransaccion().commit();
			
		} catch (Exception e) {
			Log4JManager.error(e);
		}
		
		return beneficiario;
	}

	 

	

	
	

}
