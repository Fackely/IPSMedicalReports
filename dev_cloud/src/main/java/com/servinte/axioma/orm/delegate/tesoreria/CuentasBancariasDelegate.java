package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.CuentasBancarias;
import com.servinte.axioma.orm.CuentasBancariasHome;



/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link CuentasBancarias}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public class CuentasBancariasDelegate extends CuentasBancariasHome{
	

	/**
	 * Retorna un listado con las Cuentas Bancarias asociadas a una Entidad Financiera espec&iacute;fica
	 * 
	 * @param codigoEntidad
	 * @return List<{@link CuentasBancarias}>
	 */
	@SuppressWarnings("unchecked")
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidad(int consecutivoEntidad)
	{
		List<CuentasBancarias> listadoCuentasBancarias =  sessionFactory.getCurrentSession()
			.createCriteria(CuentasBancarias.class)
				.add(Expression.eq("entidadesFinancieras.consecutivo", consecutivoEntidad))
				.addOrder(Order.asc("tipoCuentaBancaria.codigo"))
				.list();
		
		return cargarInformacionCuentasBancarias(listadoCuentasBancarias);
	}
	
	
	/**
	 * Retorna un listado con las Cuentas Bancarias asociadas a una Empresa - Instituci&oacute;n relacionada 
	 * a un Centro de atenci&oacute;n espec&iacute;fico. 
	 * 
	 * @param consecutivoCentroAtencion
	 * @return List<{@link CuentasBancarias}>
	 */
	@SuppressWarnings("unchecked")
	public List<CuentasBancarias> obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(int consecutivoEntidad, int consecutivoCentroAtencion)
	{
		List<CuentasBancarias> listadoCuentasBancarias =  sessionFactory.getCurrentSession()
			.createCriteria(CuentasBancarias.class)
			.createAlias("empresasInstitucion", "empInst")
			.createAlias("empInst.centroAtencions", "centroAtencion")
				.add(Expression.eq("entidadesFinancieras.consecutivo", consecutivoEntidad))
				.add(Expression.eq("centroAtencion.consecutivo", consecutivoCentroAtencion))
				.addOrder(Order.asc("tipoCuentaBancaria.codigo"))
				.list();
		
		return cargarInformacionCuentasBancarias(listadoCuentasBancarias);
	}

	/**
	 * M&eacute;todo que se encarga de cargar la informaci&oacute;n asociada a las 
	 * Cuentas Bancarias que est&aacute;n descritas en otras entidades (tablas)
	 * 
	 * @param listadoCuentasBancarias
	 * @return
	 */
	private List<CuentasBancarias> cargarInformacionCuentasBancarias(List<CuentasBancarias> listadoCuentasBancarias) {
		
		for (CuentasBancarias cuentasBancarias : listadoCuentasBancarias) {
			
			cuentasBancarias.getEntidadesFinancieras().getTerceros().getDescripcion();
			cuentasBancarias.getTipoCuentaBancaria().getDescripcion();
		}
		
		return listadoCuentasBancarias;
	}
}
