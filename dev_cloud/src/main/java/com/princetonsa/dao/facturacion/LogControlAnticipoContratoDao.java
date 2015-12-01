package com.princetonsa.dao.facturacion;


import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoLogControlAnticipoContrato;

public interface LogControlAnticipoContratoDao {

	
	
	public double guardar(DtoLogControlAnticipoContrato dto) ;
	public  ArrayList<DtoLogControlAnticipoContrato> cargar(DtoLogControlAnticipoContrato dtoWhere); 
	
}
