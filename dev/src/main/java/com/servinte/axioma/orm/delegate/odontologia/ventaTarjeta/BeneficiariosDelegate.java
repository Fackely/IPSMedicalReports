package com.servinte.axioma.orm.delegate.odontologia.ventaTarjeta;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesIntegridadDominio;

import com.servinte.axioma.orm.BeneficiarioTarjetaCliente;
import com.servinte.axioma.orm.BeneficiarioTarjetaClienteHome;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Delegado de persistencia para BeneficiariosTarjetaCliente
 * @author Edgar Carvajal Ruiz
 * @since 06 Septiembre 2010
 */
public class BeneficiariosDelegate extends BeneficiarioTarjetaClienteHome {

	/**
	 * Busca si el código de la persona pasada por parámetros es
	 * un beneficiario, lo que indica que ya tiene una tarjeta
	 * asociada.
	 * @author Juan David Ramírez
	 * @param codigoPersona Código del a persona pasada por parámetro
	 * @param filtrarTarjetasActivas Filtrar las tarjetas activas en el sistema
	 * @return {@link BeneficiarioTarjetaCliente} Beneficiario encontrado
	 * @since 06 Septiembre 2010
	 */
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersona, boolean filtrarTarjetasActivas)
	{
		Criteria criterio=sessionFactory.getCurrentSession().createCriteria(BeneficiarioTarjetaCliente.class, "beneficiario");
		
		criterio.createAlias("beneficiario.beneficiarioTcPaciente", "benf_paciente");
		criterio.add(Restrictions.eq("benf_paciente.pacientes.codigoPaciente", codigoPersona));
		if(filtrarTarjetasActivas)
		{
			criterio.add(Restrictions.eq("beneficiario.estadoTarjeta", ConstantesIntegridadDominio.acronimoEstadoActivo));
		}
		return (BeneficiarioTarjetaCliente) criterio.uniqueResult();
	}
	
	
	
	/**
	 * Metodo que cargar un Beneficiario con el tipo de la venta  y el tipo de tarjeta
	 * @param codigoPersonas
	 * @return
	 */
	public BeneficiarioTarjetaCliente obtenerBeneficiarioPersona(int codigoPersonas){
		
		UtilidadTransaccion.getTransaccion().begin();
		
		Criteria criterio=sessionFactory.getCurrentSession().createCriteria(BeneficiarioTarjetaCliente.class, "beneficiario");
		
		criterio.createAlias("beneficiario.beneficiarioTcPaciente", "benf_paciente");
		criterio.createAlias("beneficiario.tiposTarjCliente", "tipo_tarj");
		//criterio.createAlias("beneficiario.ventaTarjetaCliente", "venta_client");
		
		
		criterio.add(Restrictions.eq("benf_paciente.pacientes.codigoPaciente", codigoPersonas));
		criterio.add(Restrictions.eq("beneficiario.estadoTarjeta", ConstantesIntegridadDominio.acronimoEstadoActivo));
		
		
		BeneficiarioTarjetaCliente beneficiarioTarjetaCliente=(BeneficiarioTarjetaCliente)criterio.uniqueResult(); 
		
		
		return beneficiarioTarjetaCliente;
	}
	
	
	

	/**
	 * Validar la existencia del serial asociado a un tipo de tarjeta.
	 * 
	 * @param serial
	 * @param codigoTipoTarjeta
	 * @return true en caso de existir el serial, false de lo contrario
	 */
	public boolean existeSerial(Long serial, long codigoTipoTarjeta) {
		
		BeneficiarioTarjetaCliente beneficiario=(BeneficiarioTarjetaCliente) sessionFactory.getCurrentSession()
		.createCriteria(BeneficiarioTarjetaCliente.class, "beneficiario")
		.add(Restrictions.eq("tiposTarjCliente.codigoPk", codigoTipoTarjeta))
		.add(Restrictions.eq("serial", serial)).setMaxResults(1).uniqueResult();
		
		return beneficiario!=null;
	}
	
}
