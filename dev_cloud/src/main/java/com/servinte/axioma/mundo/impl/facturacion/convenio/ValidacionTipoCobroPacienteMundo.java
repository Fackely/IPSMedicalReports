package com.servinte.axioma.mundo.impl.facturacion.convenio;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.princetonsa.mundo.cargos.Convenio;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IContratoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.convenio.IConvenioDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.interfaz.facturacion.IValidacionTipoCobroPacienteMundo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConvenioDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

public class ValidacionTipoCobroPacienteMundo implements IValidacionTipoCobroPacienteMundo
{

	@Override
	public DtoValidacionTipoCobroPaciente validarTipoCobroPacienteServicioConvenioContrato(int codigoContrato)
	{
		DtoValidacionTipoCobroPaciente dto = new DtoValidacionTipoCobroPaciente();
		try {
			HibernateUtil.beginTransaction();
			IContratoDAO dao = FacturacionFabricaDAO.crearContratoDAO();
			IConvenioDAO daoConvenio = FacturacionFabricaDAO.crearConvenioDAO();

			if (codigoContrato != ConstantesBD.codigoNuncaValido) {
				Contratos contrato = dao.findById(codigoContrato);
				Convenios convenio = daoConvenio.findById(contrato.getConvenios().getCodigo());
				contrato.getConvenios();
				if(contrato != null && convenio != null){
					if(!UtilidadTexto.isEmpty(convenio.getManejaMontos())&&convenio.getManejaMontos().equals(ConstantesBD.acronimoSi))
					{
						dto.setManejaMontos(convenio.getManejaMontos());
						dto.setPacientePagaAtencion(contrato.getPacientePagaAtencion()+"");
						dto.setMostrarCalisificacion(ConstantesBD.acronimoSi);
						dto.setMostrarCuotaVefificacion(ConstantesBD.acronimoSi);
						dto.setMostrarNaturalezaPaciente(ConstantesBD.acronimoSi);
						dto.setMostrarPorcentajeCobertura(ConstantesBD.acronimoSi);
						dto.setMostrarTipoAfiliado(ConstantesBD.acronimoSi);
						dto.setTipoCobroPaciente(ConstantesIntegridadDominio.acronimoTipoPacienteManejaMontos);
					}
					else
					{
						dto.setManejaMontos(convenio.getManejaMontos());
						dto.setPacientePagaAtencion(contrato.getPacientePagaAtencion()+"");
						dto.setMostrarCalisificacion(ConstantesBD.acronimoNo);
						dto.setMostrarCuotaVefificacion(ConstantesBD.acronimoNo);
						dto.setMostrarNaturalezaPaciente(ConstantesBD.acronimoNo);
						dto.setMostrarPorcentajeCobertura(ConstantesBD.acronimoNo);
						dto.setMostrarTipoAfiliado(ConstantesBD.acronimoNo);
						dto.setTipoCobroPaciente(ConstantesIntegridadDominio.acronimoTipoPacienteNoManejaMontos);

						if((contrato.getPacientePagaAtencion()+"").equals(ConstantesBD.acronimoSi))
						{
							dto.setPorcentajeMontoCobro(100);
						}
						else
						{
							dto.setPorcentajeMontoCobro(0);
						}
					}
				}
			}
			HibernateUtil.endTransaction();
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.info("Error validarTipoCobroPacienteServicioConvenioContrato: " + e);
		}
		return dto;
	}
}
