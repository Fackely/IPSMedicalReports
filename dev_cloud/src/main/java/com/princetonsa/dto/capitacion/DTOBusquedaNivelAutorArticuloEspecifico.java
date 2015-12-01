package com.princetonsa.dto.capitacion;


/**
 * Esta clase se encarga de almacenar los datos de 
 * los niveles de autorizaci�n para art�culos espec�ficos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class DTOBusquedaNivelAutorArticuloEspecifico {
	
	private int codigo;
	private int nivelAutoriSerArt;
	private int articuloCodigo;
	private String articuloDescripcion;
	
	/**
	 * 
	 * Este M�todo se encarga de asignar un valor
	 * espec�fico al hashCode de un objeto de tipo DTOBusquedaNivelAutorArticuloEspecifico
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
	 * Este M�todo se encarga de comparar dos objetos
	 * de tipo DTOBusquedaNivelAutorArticuloEspecifico
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = true;
		if(obj instanceof DTOBusquedaNivelAutorArticuloEspecifico){	
			if(!(this.getArticuloCodigo()==(((DTOBusquedaNivelAutorArticuloEspecifico) obj).getArticuloCodigo()))){
				iguales=false;
			}		
		}		
		return iguales;
	}
	
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo codigo
	
	 * @return retorna la variable codigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo codigo
	
	 * @param valor para el atributo codigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo nivelAutoriSerArt
	
	 * @return retorna la variable nivelAutoriSerArt 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutoriSerArt() {
		return nivelAutoriSerArt;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo nivelAutoriSerArt
	
	 * @param valor para el atributo nivelAutoriSerArt 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutoriSerArt(int nivelAutoriSerArt) {
		this.nivelAutoriSerArt = nivelAutoriSerArt;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo articuloCodigo
	
	 * @return retorna la variable articuloCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getArticuloCodigo() {
		return articuloCodigo;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo articuloCodigo
	
	 * @param valor para el atributo articuloCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setArticuloCodigo(int articuloCodigo) {
		this.articuloCodigo = articuloCodigo;
	}
	
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo articuloDescripcion
	
	 * @return retorna la variable articuloDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public String getArticuloDescripcion() {
		return articuloDescripcion;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo articuloDescripcion
	
	 * @param valor para el atributo articuloDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public void setArticuloDescripcion(String articuloDescripcion) {
		this.articuloDescripcion = articuloDescripcion;
	}
	
	

}
