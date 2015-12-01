package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;

import com.princetonsa.dao.odontologia.ParentezcoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseUtilidadOdontologia;
import com.princetonsa.dto.odontologia.DtoParentesco;


public class PostgresqlParentezcoDao implements ParentezcoDao{
	
	
	public ArrayList<DtoParentesco> cargar(DtoParentesco dto) {
		return	SqlBaseUtilidadOdontologia.cargar(dto);
		 
	}
	
	/**
	 * Consulta las interpretaciones de indice de placa, la informacion la devuelve como lenguaje de marcas (XML)
	 * */
	public String cargarInterpretacionIndicePlaca(){
		return SqlBaseUtilidadOdontologia.cargarInterpretacionIndicePlaca();
	}

}
