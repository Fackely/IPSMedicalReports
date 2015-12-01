package com.princetonsa.enu.odontologia;


/**
 * 
 * @author axioma
 *
 */
public enum EnumTiposVenta {
	
	
	EMP("Empresarial"),  AFAM("Familiar"), PER("Personal");
	
	
	
	/**
	 *Nombre Enumeracion Tipo Tarjeta 
	 */
	private String  nombre="";
	
	
	/**
	 * 
	 * @param nombre
	 */
	EnumTiposVenta(String nombre){
		this.nombre=nombre;
	}

	
	/**
	 * Retona el nombre de tipo de Tarjeta
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}
	
	

}
