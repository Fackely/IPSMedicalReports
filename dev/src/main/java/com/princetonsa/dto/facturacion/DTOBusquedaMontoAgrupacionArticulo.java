package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.UtilidadTexto;
import util.Utilidades;

import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.SubgrupoInventario;


/**
 * Esta clase se encarga de 
 * @author Angela Maria Aguirre
 * @since 7/09/2010
 */
public class DTOBusquedaMontoAgrupacionArticulo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int codigoAgrupacionArticulo;
	private int detalleCodigo;
	private int codigoSubgrupoInventario;
	private String nombreSubgrupoInventario;
	private String codigoNaturaleza;
	private Integer cantidadArticulo;
	private Integer cantidadMonto;
	private Double valorMonto;
	private int codigoInstitucion;
	private int grupoCodigo;
	private int claseInventarioCodigo;
	private String claseNombre;
	private String grupoNombre;
	private String nombreNaturaleza;
	private String grupoCodigoConcatenado;
	private ArrayList<GrupoInventario> listaGrupoInventario;
	private ArrayList<SubgrupoInventario>listaSubgrupoInventario;
	private String valorHelper;
	
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
		
		if(obj instanceof DTOBusquedaMontoAgrupacionArticulo){	
			if(!(this.getClaseInventarioCodigo()!=(((
				DTOBusquedaMontoAgrupacionArticulo) obj).getClaseInventarioCodigo()))){
				return false;
			}
			if((!UtilidadTexto.isEmpty(this.getGrupoCodigoConcatenado())) && 
					(!UtilidadTexto.isEmpty(this.getGrupoCodigoConcatenado()))){				
				if(!(this.getGrupoCodigoConcatenado().equals((((
						DTOBusquedaMontoAgrupacionArticulo) obj).getGrupoCodigoConcatenado())))){
					return false;
				}				
			}
			if(!(this.getCodigoSubgrupoInventario()!=((
					DTOBusquedaMontoAgrupacionArticulo) obj).getCodigoSubgrupoInventario())){
				return false;
			}					
										
			if((this.getCodigoNaturaleza()!=null) && 
					(((DTOBusquedaMontoAgrupacionArticulo) obj).getCodigoNaturaleza()!=null)){
				if(!(this.getCodigoNaturaleza().equals(((
						DTOBusquedaMontoAgrupacionArticulo) obj).getCodigoNaturaleza()))){
					return false;
				}
			}
					
			if(this.getCantidadArticulo()!=null && 
					((DTOBusquedaMontoAgrupacionArticulo) obj).getCantidadArticulo()!=null){
				if(!(this.getCantidadArticulo().equals(
						((DTOBusquedaMontoAgrupacionArticulo) obj).getCantidadArticulo()!=null))){
					return false;
				}
			}
					
			if(this.getCantidadMonto()!=null && 
					((DTOBusquedaMontoAgrupacionArticulo) obj).getCantidadMonto()!=null){
				if(!(this.getCantidadMonto().equals(
						((DTOBusquedaMontoAgrupacionArticulo) obj).getCantidadMonto()!=null))){
					return false;
				}
			}			
			if(this.getValorMonto()!=null && 
					((DTOBusquedaMontoAgrupacionArticulo) obj).getValorMonto()!=null){
				if(!(this.getValorMonto().equals(
						((DTOBusquedaMontoAgrupacionArticulo) obj).getValorMonto()!=null))){
					return false;
				}
			}				
		}
		return true;
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoAgrupacionArticulo
	
	 * @return retorna la variable codigoAgrupacionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoAgrupacionArticulo() {
		return codigoAgrupacionArticulo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoAgrupacionArticulo
	
	 * @param valor para el atributo codigoAgrupacionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoAgrupacionArticulo(int codigoAgrupacionArticulo) {
		this.codigoAgrupacionArticulo = codigoAgrupacionArticulo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo detalleCodigo
	
	 * @return retorna la variable detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public int getDetalleCodigo() {
		return detalleCodigo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo detalleCodigo
	
	 * @param valor para el atributo detalleCodigo 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetalleCodigo(int detalleCodigo) {
		this.detalleCodigo = detalleCodigo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoSubgrupoInventario
	
	 * @return retorna la variable codigoSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoSubgrupoInventario() {
		return codigoSubgrupoInventario;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoSubgrupoInventario
	
	 * @param valor para el atributo codigoSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoSubgrupoInventario(int codigoSubgrupoInventario) {
		this.codigoSubgrupoInventario = codigoSubgrupoInventario;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreSubgrupoInventario
	
	 * @return retorna la variable nombreSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreSubgrupoInventario() {
		return nombreSubgrupoInventario;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreSubgrupoInventario
	
	 * @param valor para el atributo nombreSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreSubgrupoInventario(String nombreSubgrupoInventario) {
		this.nombreSubgrupoInventario = nombreSubgrupoInventario;
	}	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadArticulo
	
	 * @return retorna la variable cantidadArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCantidadArticulo() {
		return cantidadArticulo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadArticulo
	
	 * @param valor para el atributo cantidadArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadArticulo(Integer cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cantidadMonto
	
	 * @return retorna la variable cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public Integer getCantidadMonto() {
		return cantidadMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cantidadMonto
	
	 * @param valor para el atributo cantidadMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCantidadMonto(Integer cantidadMonto) {
		this.cantidadMonto = cantidadMonto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorMonto
	
	 * @return retorna la variable valorMonto 
	 * @author Angela Maria Aguirre 
	 */
	public Double getValorMonto() {
		return valorMonto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorMonto
	
	 * @param valor para el atributo valorMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorMonto(Double valorMonto) {
		if(valorMonto!=null && valorMonto.doubleValue()>=0){
			this.valorMonto = valorMonto;
		}else{
			this.valorMonto = null;
		}
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorHelper
	
	 * @return retorna la variable valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public String getValorHelper() {
		return valorHelper;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorHelper
	
	 * @param valor para el atributo valorHelper 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorHelper(String valorHelper) {
		this.valorHelper=valorHelper;
		if(!UtilidadTexto.isEmpty(valorHelper)){
			Double valorAux = Utilidades.convertirADouble(valorHelper, false);
			this.valorMonto = valorAux;			
		}else{
			this.valorMonto = null;
		}
	}
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoInstitucion
	
	 * @return retorna la variable codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoInstitucion
	
	 * @param valor para el atributo codigoInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoNaturaleza
	
	 * @return retorna la variable codigoNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigoNaturaleza() {
		return codigoNaturaleza;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoNaturaleza
	
	 * @param valor para el atributo codigoNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoNaturaleza(String codigoNaturaleza) {
		this.codigoNaturaleza = codigoNaturaleza;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo grupoCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public int getGrupoCodigo() {
		return grupoCodigo;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo grupoCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public void setGrupoCodigo(int grupoCodigo) {
		this.grupoCodigo = grupoCodigo;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo claseInventarioCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public int getClaseInventarioCodigo() {
		return claseInventarioCodigo;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo claseInventarioCodigo
	 *
	 * @author Angela Aguirre 
	 */
	public void setClaseInventarioCodigo(int claseInventarioCodigo) {
		this.claseInventarioCodigo = claseInventarioCodigo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo claseNombre
	
	 * @return retorna la variable claseNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getClaseNombre() {
		return claseNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo claseNombre
	
	 * @param valor para el atributo claseNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setClaseNombre(String claseNombre) {
		this.claseNombre = claseNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo grupoNombre
	
	 * @return retorna la variable grupoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getGrupoNombre() {
		return grupoNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo grupoNombre
	
	 * @param valor para el atributo grupoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setGrupoNombre(String grupoNombre) {
		this.grupoNombre = grupoNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreNaturaleza
	
	 * @return retorna la variable nombreNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreNaturaleza() {
		return nombreNaturaleza;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreNaturaleza
	
	 * @param valor para el atributo nombreNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreNaturaleza(String nombreNaturaleza) {
		this.nombreNaturaleza = nombreNaturaleza;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo grupoCodigoConcatenado
	 *
	 * @author Angela Aguirre 
	 */
	public String getGrupoCodigoConcatenado() {
		return grupoCodigoConcatenado;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo grupoCodigoConcatenado
	 *
	 * @author Angela Aguirre 
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
