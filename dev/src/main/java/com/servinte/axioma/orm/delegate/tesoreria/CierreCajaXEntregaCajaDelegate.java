package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.CierreCajaXEntregaCaja;
import com.servinte.axioma.orm.CierreCajaXEntregaCajaHome;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto CierreCajaXEntregaCaja.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public class CierreCajaXEntregaCajaDelegate extends CierreCajaXEntregaCajaHome{


	/**
	 * 
	 * M&eacute;todo que registra la asociaci&oacute;n entre las Entregas a Caja Mayor / Principal realizadas
	 * durante un turno de caja espec&iacute;fico junto con el movimiento de cierre pasado por par&aacute;metro
	 *
	 * @param listaEntregasCajaMayorPrincipal
	 * @param movimientosCaja
	 * @return boolean indicando si la asociaci&oacute;n fue exitosa o no
	 */
	public boolean asociarEntregaCajaCierreCaja(List<EntregaCajaMayor> listaEntregasCajaMayorPrincipal, MovimientosCaja movimientosCaja) {
		
		boolean update =true;
		
		if (listaEntregasCajaMayorPrincipal!=null && listaEntregasCajaMayorPrincipal.size()>0){

			for (EntregaCajaMayor entregaCajaMayorPrincipal : listaEntregasCajaMayorPrincipal) {
				
				CierreCajaXEntregaCaja cierreCajaXEntregaCaja = new CierreCajaXEntregaCaja();
				cierreCajaXEntregaCaja.setEntregaCajaMayor(entregaCajaMayorPrincipal);
				cierreCajaXEntregaCaja.setMovimientosCaja(movimientosCaja);
				
				super.persist(cierreCajaXEntregaCaja);
			}
		
		}else{
			
			update = false;
		}
		
		return update;
	}
	
}
