package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;

import com.princetonsa.dto.tesoreria.DtoTarjetasFinancieras;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITarjetaFinancieraMundo;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITarjetaFinancieraServicio;

public class TarjetaFinancieraServicio implements ITarjetaFinancieraServicio {

	@Override
	public ArrayList<DtoTarjetasFinancieras> listarTarjetasFinancieras() {
		ITarjetaFinancieraMundo tarjetaFinancieraMundo=TesoreriaFabricaMundo.crearTarjetasFinancieras();
		return tarjetaFinancieraMundo.listarTarjetasFinancieras();
	}

}
