package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;
import com.servinte.axioma.dao.interfaz.tesoreria.ITarjetasFinancierasDAO;
import com.servinte.axioma.orm.delegate.tesoreria.TarjetasFinancierasDelegate;

public class TarjetasFinancierasDAO implements ITarjetasFinancierasDAO {

	@Override
	public ArrayList<DtoTarjetasFinancieras> listarTarjetasFinancieras() {
		return new TarjetasFinancierasDelegate().listarTarjetasFinancieras(true);
	}

}
