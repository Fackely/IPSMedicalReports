package com.servinte.axioma.dto.tesoreria;

import java.util.ArrayList;

public class DtoNotasPorNaturaleza {

	ArrayList<DtoResumenNotasPaciente> dtoResumenNotasDebito;
	
	ArrayList<DtoResumenNotasPaciente> dtoResumenNotasCredito;

	public ArrayList<DtoResumenNotasPaciente> getDtoResumenNotasDebito() {
		return dtoResumenNotasDebito;
	}

	public void setDtoResumenNotasDebito(
			ArrayList<DtoResumenNotasPaciente> dtoResumenNotasDebito) {
		this.dtoResumenNotasDebito = dtoResumenNotasDebito;
	}

	public ArrayList<DtoResumenNotasPaciente> getDtoResumenNotasCredito() {
		return dtoResumenNotasCredito;
	}

	public void setDtoResumenNotasCredito(
			ArrayList<DtoResumenNotasPaciente> dtoResumenNotasCredito) {
		this.dtoResumenNotasCredito = dtoResumenNotasCredito;
	}
	
	
}
