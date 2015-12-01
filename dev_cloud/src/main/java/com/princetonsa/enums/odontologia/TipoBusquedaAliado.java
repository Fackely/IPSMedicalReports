package com.princetonsa.enums.odontologia;

public enum TipoBusquedaAliado {

	
	ALIADO_SI(0),
	ALIADO_NO_FAMILIAR(1),
	ALIADO_NO_EMPRESARIAL(2),
	ALIADO_NO_PERSONAL(3);
	
	private TipoBusquedaAliado(int posicion) 
	{
		this.tipoBusqueda = posicion;
	}
	
	private int tipoBusqueda;

	/**
	 * @return the tipoBusqueda
	 */
	public int getTipoBusqueda() {
		return tipoBusqueda;
	}

	/**
	 * @param tipoBusqueda the tipoBusqueda to set
	 */
	public void setTipoBusqueda(int tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

	
}
