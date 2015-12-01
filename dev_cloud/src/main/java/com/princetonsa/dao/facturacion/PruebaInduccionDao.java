package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface PruebaInduccionDao 
{

	
	HashMap consultarRegistro(Connection con, String codigoPaquete);
	
	
	

}
