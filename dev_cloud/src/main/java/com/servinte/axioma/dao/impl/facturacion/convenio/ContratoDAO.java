package com.servinte.axioma.dao.impl.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoEsquemasTarifarios;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;

/**
 * Clase concreta para el manejo de la persistencia de los contratos
 * @author Juan David Ramírez
 * @since 10 Septiembre 2010
 * @version 1.0
 */
public class ContratoDAO implements IContratoDAO
{

	private ContratosDelegate delegate = new ContratosDelegate();
	
	
	@Override
	public boolean esVigenteContrato(DtoContrato contrato)
	{
		return delegate.esVigenteContrato(contrato);
	}


	@Override
	public DtoEsquemasTarifarios obtenerEsquemaTarifarioProcedimientosVigente(int contrato) {
		Log4JManager.info("---ADVERTENCIA: El metodo obtenerEsquemaTarifarioProcedimientosVigente de la clase ContratoDAO siempre retorna null");
		return null;
		//return new ContratosDelegate().obtenerEsquemaTarifarioProcedimientosVigente(int contrato);
	}

	
	@Override
	public Contratos findById(int id) {
		return delegate.findById(id);
	}


	@Override
	public ArrayList<Contratos> listarContratosPorConvenio(int convenio) {
		return delegate.listarContratosPorConvenio(convenio);
	}


	@Override
	public ArrayList<Contratos> listarContratosVigentesPorConvenio(int convenio) {
		return delegate.listarContratosVigentesPorConvenio(convenio);
	}


	@Override
	public ArrayList<Contratos> listarContratos(int codInstitucion) {
		return delegate.listarContratos(codInstitucion);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO#listarContratosPorConvenioPorFechaMenor(int, java.lang.String, java.util.Date)
	 */
	@Override
	public ArrayList<Contratos> listarContratosPorConvenioPorFechaMenor(int convenio, 
									String campoComparacion, Date fechaComparacion){
		return delegate.listarContratosPorConvenioPorFechaMenor(convenio, 
							campoComparacion, fechaComparacion);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO#listarContratosConParametrizacionPresupuestoPorConvenio(int, int)
	 */
	@Override
	public ArrayList<Contratos> listarContratosConParametrizacionPresupuestoPorConvenio(
			int codigoConvenio, Calendar mesAnio) {
		return delegate.listarContratosConParametrizacionPresupuestoPorConvenio(codigoConvenio, mesAnio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO#listarTodosContratosVigentesPorConvenio(int)
	 */
	@Override
	public ArrayList<Contratos> listarTodosContratosVigentesPorConvenio(
			int convenio) {
		return delegate.listarTodosContratosVigentesPorConvenio(convenio);
	}

}
