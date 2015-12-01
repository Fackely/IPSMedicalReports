package com.princetonsa.dto.capitacion;


/**
 * Esta clase se encarga de almacenar los datos de las
 * agrupaciones de servicios parametrizadas por niveles
 * de autorización 
 * 
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class DTOBusquedaNivelAutorAgrupacionServicio {
	
	private int codigoPk;
	private int nivelAutoriSerArt;	
	private String tipoServicio;
	private Integer grupoServicio;
	private Integer especialidad;
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTOBusquedaNivelAutorAgrupacionServicio
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
	 * de tipo DTOBusquedaNivelAutorAgrupacionServicio
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = true;
		if(obj instanceof DTOBusquedaNivelAutorAgrupacionServicio){	
			
			if((this.getGrupoServicio()!=null) && 
					(((DTOBusquedaNivelAutorAgrupacionServicio) obj).getGrupoServicio()!=null)){
				if(!(this.getGrupoServicio().equals(((DTOBusquedaNivelAutorAgrupacionServicio) obj).getGrupoServicio()))){
					iguales=false;
				}
			}
			if(iguales){
				if((this.getTipoServicio()!=null) && 
						(((DTOBusquedaNivelAutorAgrupacionServicio) obj).getTipoServicio()!=null)){
					if(!(this.getTipoServicio().equals(((DTOBusquedaNivelAutorAgrupacionServicio) obj).getTipoServicio()))){
						iguales=false;
					}
				}
				if(iguales){
					if((this.getEspecialidad()!=null) && 
							(((DTOBusquedaNivelAutorAgrupacionServicio) obj).getEspecialidad()!=null)){
						if(!(this.getEspecialidad().equals((((DTOBusquedaNivelAutorAgrupacionServicio) obj).getEspecialidad())))){
							iguales = false;
						}
					}
				}				
			}			
		}		
		return iguales;
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
	 * del atributo tipoServicio
	
	 * @return retorna la variable tipoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoServicio
	
	 * @param valor para el atributo tipoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo grupoServicio
	
	 * @return retorna la variable grupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getGrupoServicio() {
		return grupoServicio;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo grupoServicio
	
	 * @param valor para el atributo grupoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setGrupoServicio(Integer grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo especialidad
	
	 * @return retorna la variable especialidad 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getEspecialidad() {
		return especialidad;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo especialidad
	
	 * @param valor para el atributo especialidad 
	 * @author Angela Maria Aguirre 
	 */
	public void setEspecialidad(Integer especialidad) {
		this.especialidad = especialidad;
	}

}
