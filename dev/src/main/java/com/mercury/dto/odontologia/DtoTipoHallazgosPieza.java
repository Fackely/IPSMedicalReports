package com.mercury.dto.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

public class DtoTipoHallazgosPieza {
	
	/**
	 * HashMap que almacena el total de hallazgos encontrados
	 * teniendo como clave el tipo_hallazgo_ceo_cop y como 
	 * valor la cantidad de hallazgos_ceo_cop encontrados
	 */
	private HashMap<Long, Integer> totalTiposHallazgo;
	
	/**
	 * HashMap que almacena el total de piezas dentales del odontograma
	 * teniendo como clave el numero de la pieza y como 
	 * valor la cantidad de hallazgos encontrados para dicha pieza
	 */
	private HashMap<Integer, ArrayList<Integer>> listaPiezaHallazgos;
	
	public DtoTipoHallazgosPieza() {}
	
	public DtoTipoHallazgosPieza(HashMap<Long, Integer> totalTiposHallazgo,
			HashMap<Integer, ArrayList<Integer>> listaPiezaHallazgos) {
		super();
		this.setTotalTiposHallazgo(totalTiposHallazgo);
		this.setListaPiezaHallazgos(listaPiezaHallazgos);
	}

	/**
	 * @param totalTiposHallazgo the totalTiposHallazgo to set
	 */
	public void setTotalTiposHallazgo(HashMap<Long, Integer> totalTiposHallazgo) {
		this.totalTiposHallazgo = totalTiposHallazgo;
	}

	/**
	 * @return the totalTiposHallazgo
	 */
	public HashMap<Long, Integer> getTotalTiposHallazgo() {
		return totalTiposHallazgo;
	}

	/**
	 * @param listaPiezaHallazgos the listaPiezaHallazgos to set
	 */
	public void setListaPiezaHallazgos(HashMap<Integer, ArrayList<Integer>> listaPiezaHallazgos) {
		this.listaPiezaHallazgos = listaPiezaHallazgos;
	}

	/**
	 * @return the listaPiezaHallazgos
	 */
	public HashMap<Integer, ArrayList<Integer>> getListaPiezaHallazgos() {
		return listaPiezaHallazgos;
	}

}
