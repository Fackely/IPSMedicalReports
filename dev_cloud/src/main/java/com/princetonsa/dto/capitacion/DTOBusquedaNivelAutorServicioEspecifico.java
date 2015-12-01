package com.princetonsa.dto.capitacion;



/**
 * Esta clase se encarga de almacenar los datos de 
 * los niveles de autorización para servicios específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class DTOBusquedaNivelAutorServicioEspecifico {
	
	private int codigoPk;
	private int nivelAutoriSerArt;	
	private int codigoServicio;
	private String nombreServicio;
	boolean permiteEliminar;
	
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTOBusquedaNivelAutorServicioEspecifico
	 * 
	 * @return int
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public int hashCode() {
		return 0;
	}
	
	/**
	 * 
	 * Este Método se encarga de comparar dos objetos
	 * de tipo DTOBusquedaNivelAutorServicioEspecifico
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = true;
		if(obj instanceof DTOBusquedaNivelAutorServicioEspecifico){	
			if(!(this.getCodigoServicio()==(((DTOBusquedaNivelAutorServicioEspecifico) obj).getCodigoServicio()))){
				iguales=false;
			}		
		}		
		return iguales;
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAutoriSerArt
	
	 * @return retorna la variable nivelAutoriSerArt 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutoriSerArt() {
		return nivelAutoriSerArt;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutoriSerArt
	
	 * @param valor para el atributo nivelAutoriSerArt 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutoriSerArt(int nivelAutoriSerArt) {
		this.nivelAutoriSerArt = nivelAutoriSerArt;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoServicio
	
	 * @return retorna la variable codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoServicio
	
	 * @param valor para el atributo codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo permiteEliminar
	
	 * @return retorna la variable permiteEliminar 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isPermiteEliminar() {
		return permiteEliminar;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo permiteEliminar
	
	 * @param valor para el atributo permiteEliminar 
	 * @author Angela Maria Aguirre 
	 */
	public void setPermiteEliminar(boolean permiteEliminar) {
		this.permiteEliminar = permiteEliminar;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPk
	
	 * @return retorna la variable codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoPk() {
		return codigoPk;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPk
	
	 * @param valor para el atributo codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreServicio
	
	 * @return retorna la variable nombreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreServicio
	
	 * @param valor para el atributo nombreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}	
	
	

}
