/**
 * 
 */
package com.servinte.axioma.mundo.impl.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoEsquemasTarifarios;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.fabrica.facturacion.convenio.ConvenioFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.orm.Contratos;

/**
 * Clase concreta de contratos para el manejo de la lógica de negocio
 * @author Juan David Ramírez
 * @since 10 Septiembre 2010
 */
public class ContratoMundo implements IContratoMundo
{

	private IContratoDAO contratoDAO;
	
	
	public ContratoMundo() {
		inicializar();
	}
	
	
	private void inicializar() 
	{
		contratoDAO	= FacturacionFabricaDAO.crearContratoDAO();
	}
	
	
	@Override
	public boolean esVigenteContrato(DtoContrato contrato)
	{
		return ConvenioFabricaDAO.crearContratoDAO().esVigenteContrato(contrato);
	}

	@Override
	public DtoEsquemasTarifarios obtenerEsquemaTarifarioProcedimientosVigente(int contrato) {
		return ConvenioFabricaDAO.crearContratoDAO().obtenerEsquemaTarifarioProcedimientosVigente(contrato);
	}

	
	@Override
	public Contratos findById(int id) {
		return contratoDAO.findById(id);
	}


	@Override
	public ArrayList<Contratos> listarContratosPorConvenio(int convenio) {
		return contratoDAO.listarContratosPorConvenio(convenio);
	}


	@Override
	public ArrayList<Contratos> listarContratosVigentesPorConvenio(int convenio) {
		return contratoDAO.listarContratosVigentesPorConvenio(convenio);
	}
	
	
	
	@Override
	public ArrayList<Contratos> listarContratos(int codInstitucion){
		return contratoDAO.listarContratos(codInstitucion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo#listarContratosPorConvenioPorFechaMenor(int, java.lang.String, java.util.Date)
	 */
	@Override
	public ArrayList<Contratos> listarContratosPorConvenioPorFechaMenor(int convenio, 
									String campoComparacion, Date fechaComparacion){
		return contratoDAO.listarContratosPorConvenioPorFechaMenor(convenio,
									campoComparacion, fechaComparacion);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo#listarContratosConParametrizacionPresupuestoPorConvenio(int, int)
	 */
	@Override
	public ArrayList<Contratos> listarContratosConParametrizacionPresupuestoPorConvenio(
			int codigoConvenio, Calendar mesAnio) {
		return contratoDAO.listarContratosConParametrizacionPresupuestoPorConvenio(
							codigoConvenio, mesAnio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo#listarTodosContratosVigentesPorConvenio(int)
	 */
	@Override
	public ArrayList<Contratos> listarTodosContratosVigentesPorConvenio(
			int convenio) {
		return contratoDAO.listarTodosContratosVigentesPorConvenio(convenio);
	}
}
