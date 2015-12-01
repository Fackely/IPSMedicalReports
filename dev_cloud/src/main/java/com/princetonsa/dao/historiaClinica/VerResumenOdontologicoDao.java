package com.princetonsa.dao.historiaClinica;

import java.util.ArrayList;

import com.princetonsa.dto.historiaClinica.componentes.DtoAntecedentesOdontologicosAnt;

public interface VerResumenOdontologicoDao {

	DtoAntecedentesOdontologicosAnt cagarAntecedentesOdontoPrevios(int codigoPaciente);

}
