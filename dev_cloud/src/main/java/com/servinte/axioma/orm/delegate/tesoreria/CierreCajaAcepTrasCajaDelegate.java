package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.CierreCajaAcepTrasCaja;
import com.servinte.axioma.orm.CierreCajaAcepTrasCajaHome;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link CierreCajaAcepTrasCaja}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public class CierreCajaAcepTrasCajaDelegate extends CierreCajaAcepTrasCajaHome {


	public boolean asociarAceptacionesSolicitudTrasladoCierreCaja(List<AceptacionTrasladoCaja> listaSolicitudesAceptadas, MovimientosCaja movimientosCaja) {
		
		boolean update =true;
		
		if (listaSolicitudesAceptadas!=null && listaSolicitudesAceptadas.size()>0){

			for (AceptacionTrasladoCaja aceptacionTrasladoCaja : listaSolicitudesAceptadas) {
				
				CierreCajaAcepTrasCaja cierreCajaAcepTrasCaja = new CierreCajaAcepTrasCaja();
				cierreCajaAcepTrasCaja.setAceptacionTrasladoCaja(aceptacionTrasladoCaja);
				cierreCajaAcepTrasCaja.setMovimientosCaja(movimientosCaja);
				
				super.persist(cierreCajaAcepTrasCaja);
			}
		
		}else{
			
			update = false;
		}
		
		return update;

	}
	
}
