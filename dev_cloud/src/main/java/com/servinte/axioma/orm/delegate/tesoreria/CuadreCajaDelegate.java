package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.servinte.axioma.orm.CuadreCaja;
import com.servinte.axioma.orm.CuadreCajaHome;
import com.servinte.axioma.orm.MovimientosCaja;


/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link CuadreCaja}
 * 
 * @author Jorge Armando Agudelo Quintero
 */

public class CuadreCajaDelegate extends CuadreCajaHome{
	
	
	/**
	 * M&eacute;todo que se encarga de realizar la consulta del cuadre de caja registrado
	 * para un movimiento Arqueo Caja.
	 *
	 * @param arqueoCaja
	 * @return List<{@link DtoCuadreCaja}> con la informaci&oacute;n registrada para el movimiento de Arqueo Caja.
	 */
	@SuppressWarnings("unchecked")
	public List<CuadreCaja> consultarCuadreCajaPorMovimiento (MovimientosCaja arqueoCaja){
		
		List<CuadreCaja> listaCuadreCajas = sessionFactory.getCurrentSession().createCriteria(CuadreCaja.class)
			
			.createAlias("movimientosCaja", "mc")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("formasPago", "fp")
			.createAlias("fp.tiposDetalleFormaPago", "tipoDetFp")
			
			.add(Restrictions.eq("tmc.codigo", ConstantesBD.codigoTipoMovimientoArqueoCaja))
			.add(Restrictions.eq("mc.turnoDeCaja", arqueoCaja.getTurnoDeCaja()))
			.add(Restrictions.eq("mc.codigoPk", arqueoCaja.getCodigoPk()))
			.addOrder(Order.asc("tipoDetFp.prioridad"))
			.list();
			
			for (CuadreCaja cuadreCaja : listaCuadreCajas) {
				
				cuadreCaja.getFormasPago().getTiposDetalleFormaPago().getDescripcion();
			}
			
			return listaCuadreCajas;
	}

}
