package com.servinte.axioma.servicio.impl.facturacion.convenio;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoEsquemasTarifarios;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IContratoServicio;

/**
 * @author Cristhian Murillo
 *
 */
public class ContratoServicio implements IContratoServicio
{

	IContratoMundo contratoMundo;
	
	
	/**
	 *Construtor 
	 */
	public ContratoServicio(){
		this.contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
	}
	
	
	@Override
	public boolean esVigenteContrato(DtoContrato contrato)
	{
		return contratoMundo.esVigenteContrato(contrato);
	}

	@Override
	public DtoEsquemasTarifarios obtenerEsquemaTarifarioProcedimientosVigente(
			int contrato) {
		return contratoMundo.obtenerEsquemaTarifarioProcedimientosVigente(contrato);
	}

	
	@Override
	public ArrayList<Contratos> listarContratosPorConvenio(int convenio) {
		return contratoMundo.listarContratosPorConvenio(convenio);
	}


	@Override
	public ArrayList<Contratos> listarContratosVigentesPorConvenio(int convenio) {
		return contratoMundo.listarContratosVigentesPorConvenio(convenio);
	}

}
