package com.princetonsa.dao.historiaClinica;

import java.util.ArrayList;

import com.princetonsa.dto.historiaClinica.historicoAtenciones.DtoHistoricosHC;

public interface HistoricoAtencionesDao 
{

	public abstract  ArrayList<DtoHistoricosHC> cargarHistoricoAtenciones(int codigoPaciente);

}
