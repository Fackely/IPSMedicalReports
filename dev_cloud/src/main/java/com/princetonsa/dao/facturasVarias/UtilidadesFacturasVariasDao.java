package com.princetonsa.dao.facturasVarias;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

/**
 * @author Víctor Hugo Gómez L.
 */
public interface UtilidadesFacturasVariasDao {
	
	/***
	 * ObtenerConceptosFraVarias
	 * @param con
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(Connection con, int institucion, boolean activo);
	

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param activo
	 * @param tipoConcepto
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerConceptosFraVarias(Connection con, int institucion, boolean activo,  String tipoConcepto);
	
	
	
	
	/**
	 * 
	 * @param consecutivoFacturaVaria
	 * @param institucion
	 * @return
	 */
	public BigDecimal obtenerPkFacturaVaria( BigDecimal consecutivoFacturaVaria , int institucion);
	
	
	/**
	 * 
	 * @param dto
	 * @param codigoPkFacturasVarias
	 * @return
	 */
	public String aplicaReciboCaja(Connection con, DtoRecibosCaja dto);

	
}

