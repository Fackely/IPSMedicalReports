package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITarjetasFinancierasDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITarjetaFinancieraMundo;

public class TarjetaFinancieraMundo implements ITarjetaFinancieraMundo {

	@Override
	public ArrayList<DtoTarjetasFinancieras> listarTarjetasFinancieras() {
		ITarjetasFinancierasDAO tarjetasFinancierasDAO=TesoreriaFabricaDAO.crearTarjetasFinancieras();
		return tarjetasFinancierasDAO.listarTarjetasFinancieras();
	}

}
