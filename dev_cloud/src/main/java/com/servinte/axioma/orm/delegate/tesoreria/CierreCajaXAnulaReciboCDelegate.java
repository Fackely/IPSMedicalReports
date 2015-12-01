package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.CierreCajaXAnulaReciboC;
import com.servinte.axioma.orm.CierreCajaXAnulaReciboCHome;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link CierreCajaXAnulaReciboC}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class CierreCajaXAnulaReciboCDelegate extends CierreCajaXAnulaReciboCHome{

	

	/**
	 * M&eacute;todo que registra la asociaci&oacute;n entre las anulaciones de recibos de caja 
	 * y un movimiento de cierre espec&iacute;fico
	 *
	 * @param listaRecibosCajaTurno
	 * @param movimientosCaja
	 * @return boolean indicando si se realiz&oacute; el proceso de actualizaci&oacute;n
	 */
	public boolean asociarAnulacionesRecibosCajaConCierreTurno(List<AnulacionRecibosCaja> listaAnulacionesRecibosCaja,MovimientosCaja movimientosCaja) {
		
		boolean update =true;
		
		if (listaAnulacionesRecibosCaja!=null && listaAnulacionesRecibosCaja.size()>0){

			for (AnulacionRecibosCaja anulacionReciboCaja : listaAnulacionesRecibosCaja) {						
				
				CierreCajaXAnulaReciboC cierreCajaXAnulaReciboC = new CierreCajaXAnulaReciboC();
				cierreCajaXAnulaReciboC.setMovimientosCaja(movimientosCaja);
				cierreCajaXAnulaReciboC.setAnulacionRecibosCaja(anulacionReciboCaja);
				
				super.persist(cierreCajaXAnulaReciboC);
			}
		}else{
			
			update = false;
		}
		
		return update;
	}
}
