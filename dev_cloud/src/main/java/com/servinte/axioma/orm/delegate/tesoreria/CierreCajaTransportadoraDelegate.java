package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.CierreCajaTransportadora;
import com.servinte.axioma.orm.CierreCajaTransportadoraHome;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto CierreCajaTransportadora.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public class CierreCajaTransportadoraDelegate extends CierreCajaTransportadoraHome {


	/**
	 * 
	 * M&eacute;todo que registra la asociaci&oacute;n entre las Entregas a Transportadora de valores realizadas
	 * durante un turno de caja espec&iacute;fico junto con el movimiento de cierre pasado por par&aacute;metro
	 *
	 * @param listaEntregaTransportadora
	 * @param movimientosCaja
	 * @return boolean indicando si la asociaci&oacute;n fue exitosa o no
	 */
	public boolean asociarEntregaTransportadoraCierreCaja(List<EntregaTransportadora> listaEntregaTransportadora, MovimientosCaja movimientosCaja) {
		
		boolean update =true;
		
		if (listaEntregaTransportadora!=null && listaEntregaTransportadora.size()>0){

		
			for (EntregaTransportadora entregaTransportadora : listaEntregaTransportadora) {
				
				CierreCajaTransportadora cierreCajaTransportadora = new CierreCajaTransportadora();
				cierreCajaTransportadora.setEntregaTransportadora(entregaTransportadora);
				cierreCajaTransportadora.setMovimientosCaja(movimientosCaja);
				
				super.persist(cierreCajaTransportadora);
	
			}
		
		}else{
			
			update = false;
		}
		
		return update;
	}
	
}
