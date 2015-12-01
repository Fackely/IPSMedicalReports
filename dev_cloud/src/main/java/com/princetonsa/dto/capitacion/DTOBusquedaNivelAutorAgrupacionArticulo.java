/**
 * 
 */
package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import util.UtilidadTexto;

import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.SubgrupoInventario;

/**
 * Esta clase se encarga de almacenar los datos de 
 * los niveles de autorización para agrupación de artículos
 * @author Angela Aguirre
 *
 */
public class DTOBusquedaNivelAutorAgrupacionArticulo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int codigoPk;
	private int nivelAutoriSerArt;
	private Integer subgrupoInventario;
	private String acronimoNaturaleza;
	private Integer codigoInstitucion;
	private Integer grupoInventario;
	private Integer claseInventario;
	private String loginRegistra;
	private Date fechaRegistro;
	private String horaRegistro;
	private String grupoCodigoConcatenado;
	private ArrayList<GrupoInventario> listaGrupoInventario;
	private ArrayList<SubgrupoInventario>listaSubgrupoInventario;
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTOBusquedaNivelAutorAgrupacionArticulo
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
	 * de tipo DTOBusquedaNivelAutorAgrupacionArticulo
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = true;
		if(obj instanceof DTOBusquedaNivelAutorAgrupacionArticulo){			
			if((this.getClaseInventario()!=null) && 
					(((DTOBusquedaNivelAutorAgrupacionArticulo) obj).getClaseInventario()!=null)){				
				if(!(this.getClaseInventario().equals((((DTOBusquedaNivelAutorAgrupacionArticulo) obj).getClaseInventario())))){
					iguales=false;
				}				
			}			
			if(iguales){
				if((!UtilidadTexto.isEmpty(this.getGrupoCodigoConcatenado())) && 
						(!UtilidadTexto.isEmpty(this.getGrupoCodigoConcatenado()))){				
					if(!(this.getGrupoCodigoConcatenado().equals((((DTOBusquedaNivelAutorAgrupacionArticulo) obj).getGrupoCodigoConcatenado())))){
						iguales=false;
					}				
				}
				if(iguales){
					
					if((this.getSubgrupoInventario()!=null) &&
							(((DTOBusquedaNivelAutorAgrupacionArticulo) obj).getSubgrupoInventario()!=null)){
						if(!(this.getSubgrupoInventario().equals(((DTOBusquedaNivelAutorAgrupacionArticulo) obj).getSubgrupoInventario()))){
							iguales = false;
						}
					}					
					if(iguales){						
						if((this.getAcronimoNaturaleza()!=null) && 
								(((DTOBusquedaNivelAutorAgrupacionArticulo) obj).getAcronimoNaturaleza()!=null)){
							if(!(this.getAcronimoNaturaleza().equals(((DTOBusquedaNivelAutorAgrupacionArticulo) obj).getAcronimoNaturaleza()))){
								iguales=false;
							}
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
	 * del atributo subgrupoInventario
	
	 * @return retorna la variable subgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getSubgrupoInventario() {
		return subgrupoInventario;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo subgrupoInventario
	
	 * @param valor para el atributo subgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setSubgrupoInventario(Integer subgrupoInventario) {
		this.subgrupoInventario = subgrupoInventario;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo acronimoNaturaleza
	
	 * @return retorna la variable acronimoNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public String getAcronimoNaturaleza() {
		return acronimoNaturaleza;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo acronimoNaturaleza
	
	 * @param valor para el atributo acronimoNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public void setAcronimoNaturaleza(String acronimoNaturaleza) {
		this.acronimoNaturaleza = acronimoNaturaleza;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoInstitucion
	
	 * @return retorna la variable codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	
	 * @param valor para el atributo codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoInstitucion(Integer codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo grupoInventario
	
	 * @return retorna la variable grupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getGrupoInventario() {
		return grupoInventario;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo grupoInventario
	
	 * @param valor para el atributo grupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setGrupoInventario(Integer grupoInventario) {
		this.grupoInventario = grupoInventario;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo claseInventario
	
	 * @return retorna la variable claseInventario 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getClaseInventario() {
		return claseInventario;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo claseInventario
	
	 * @param valor para el atributo claseInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo loginRegistra
	
	 * @return retorna la variable loginRegistra 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginRegistra() {
		return loginRegistra;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo loginRegistra
	
	 * @param valor para el atributo loginRegistra 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginRegistra(String loginRegistra) {
		this.loginRegistra = loginRegistra;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaRegistro
	
	 * @return retorna la variable fechaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaRegistro
	
	 * @param valor para el atributo fechaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo horaRegistro
	
	 * @return retorna la variable horaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo horaRegistro
	
	 * @param valor para el atributo horaRegistro 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo grupoCodigoConcatenado
	
	 * @return retorna la variable grupoCodigoConcatenado 
	 * @author Angela Maria Aguirre 
	 */
	public String getGrupoCodigoConcatenado() {
		return grupoCodigoConcatenado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo grupoCodigoConcatenado
	
	 * @param valor para el atributo grupoCodigoConcatenado 
	 * @author Angela Maria Aguirre 
	 */
	public void setGrupoCodigoConcatenado(String grupoCodigoConcatenado) {
		this.grupoCodigoConcatenado = grupoCodigoConcatenado;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaGrupoInventario
	
	 * @return retorna la variable listaGrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<GrupoInventario> getListaGrupoInventario() {
		return listaGrupoInventario;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaGrupoInventario
	
	 * @param valor para el atributo listaGrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaGrupoInventario(
			ArrayList<GrupoInventario> listaGrupoInventario) {
		this.listaGrupoInventario = listaGrupoInventario;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaSubgrupoInventario
	
	 * @return retorna la variable listaSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<SubgrupoInventario> getListaSubgrupoInventario() {
		return listaSubgrupoInventario;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaSubgrupoInventario
	
	 * @param valor para el atributo listaSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaSubgrupoInventario(
			ArrayList<SubgrupoInventario> listaSubgrupoInventario) {
		this.listaSubgrupoInventario = listaSubgrupoInventario;
	}
	
}
