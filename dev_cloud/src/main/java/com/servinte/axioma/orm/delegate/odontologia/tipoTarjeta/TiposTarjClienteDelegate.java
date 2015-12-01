package com.servinte.axioma.orm.delegate.odontologia.tipoTarjeta;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;

import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.servinte.axioma.orm.TiposTarjCliente;
import com.servinte.axioma.orm.TiposTarjClienteHome;


/**
 * Delegate de tipo de Tarjeta Cliente
 * @author Juan David Ramírez
 * @since 09 Septiembre 2010
 * @version 1.0
 */
public class TiposTarjClienteDelegate extends TiposTarjClienteHome {

	/**
	 * Consultar el tipo de tarjeta cliente para la clase de
	 * venta y el tipo de tarjeta específico
	 * @param tipoTarjeta
	 * @param claseVenta TODO
	 * @author Juan David Ramírez
	 * @return {@link DtoTipoTarjetaCliente}
	 * @since 08 Septiembre 2010
	 */
	public DtoTipoTarjetaCliente consultarTipoTarjetaCliente(double tipoTarjeta, String claseVenta)
	{
		ProjectionList listaProyecciones=Projections.projectionList()
		.add(Projections.property("codigoPk"), "codigoPk")
		.add(Projections.property("convenios.codigo"), "convenio");
		
		if(claseVenta.equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona))
		{
			listaProyecciones.add(Projections.property("serviciosByServicioPersonal.codigo"), "servicioPersonal");
		}
		if(claseVenta.equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa))
		{
			listaProyecciones.add(Projections.property("serviciosByServicioEmpresarial.codigo"), "servicioEmpresarial");
		}
		if(claseVenta.equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar))
		{
			listaProyecciones.add(Projections.property("serviciosByServicioFamiliar.codigo"), "servicioFamiliar");
		}
		
		return (DtoTipoTarjetaCliente) sessionFactory.getCurrentSession()
			.createCriteria(TiposTarjCliente.class, "tipo_tarjeta")
			.setProjection(listaProyecciones)
			.add(Expression.eq("tipo_tarjeta.codigoPk", ((Double)tipoTarjeta).longValue()))
			.setResultTransformer(Transformers.aliasToBean(DtoTipoTarjetaCliente.class)).
			uniqueResult();
	}

	/**
	 * Modificar el consecutivo serial después de realizar una venta de tarjeta cliente
	 * @param codigoPkTipoTarjeta Llave primaria
	 * @param consecutivoSerial Valor del Consecutivo modificado
	 * @return true en caso de éxito, false de lo contrario
	 * @author Juan David Ramírez
	 * @since 12 Septiembre 2010
	 */
	public void modificarConsecutivoSerialDesdeVenta(Long tipoTarjeta, Long consecutivoSerial)
	{
		TiposTarjCliente tarjeta=findById(tipoTarjeta);
		tarjeta.setConsecutivoSerial(consecutivoSerial);
		attachDirty(tarjeta);
	}
}
