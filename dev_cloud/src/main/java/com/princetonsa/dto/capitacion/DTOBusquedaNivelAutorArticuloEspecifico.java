package com.princetonsa.dto.capitacion;


/**
 * Esta clase se encarga de almacenar los datos de 
 * los niveles de autorización para artículos específicos
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
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTOBusquedaNivelAutorArticuloEspecifico
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
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigo
	
	 * @return retorna la variable codigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigo
	
	 * @param valor para el atributo codigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
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
	 * del atributo articuloCodigo
	
	 * @return retorna la variable articuloCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getArticuloCodigo() {
		return articuloCodigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo articuloCodigo
	
	 * @param valor para el atributo articuloCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setArticuloCodigo(int articuloCodigo) {
		this.articuloCodigo = articuloCodigo;
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo articuloDescripcion
	
	 * @return retorna la variable articuloDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public String getArticuloDescripcion() {
		return articuloDescripcion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo articuloDescripcion
	
	 * @param valor para el atributo articuloDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public void setArticuloDescripcion(String articuloDescripcion) {
		this.articuloDescripcion = articuloDescripcion;
	}
	
	

}
