package com.servinte.axioma.orm.delegate.tesoreria;

import com.servinte.axioma.orm.DetallePagosRc;
import com.servinte.axioma.orm.DetallePagosRcHome;


/**
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto DetallePagosRc.
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class DetallePagosRcDelegate extends DetallePagosRcHome{

	
	public DetallePagosRc findById (int codigoDetalle){
		
		return super.findById(codigoDetalle);
	}
}
