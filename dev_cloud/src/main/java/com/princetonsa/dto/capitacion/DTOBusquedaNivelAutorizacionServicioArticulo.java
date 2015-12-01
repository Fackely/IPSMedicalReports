package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;

import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de almacenar los datos de un 
 * nivel de autorizaci�n de servicios y art�culos
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class DTOBusquedaNivelAutorizacionServicioArticulo implements Serializable {

	private static final long serialVersionUID = 1L;	
	private int codigoPk;
	private int nivelAutorizacionID;
	private String descripcionNivelAutorizacion;
	private Usuarios usuarios;
	private ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> listaAgrArticulo;
	private ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> listaAgrServicio;
	private ArrayList<DTOBusquedaNivelAutorServicioEspecifico> listaServicioEsp;
	private ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> listaArticuloEsp;
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo codigoPk
	
	 * @return retorna la variable codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoPk() {
		return codigoPk;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo codigoPk
	
	 * @param valor para el atributo codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo nivelAutorizacionID
	
	 * @return retorna la variable nivelAutorizacionID 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutorizacionID() {
		return nivelAutorizacionID;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo nivelAutorizacionID
	
	 * @param valor para el atributo nivelAutorizacionID 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacionID(int nivelAutorizacionID) {
		this.nivelAutorizacionID = nivelAutorizacionID;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo descripcionNivelAutorizacion
	
	 * @return retorna la variable descripcionNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionNivelAutorizacion() {
		return descripcionNivelAutorizacion;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo descripcionNivelAutorizacion
	
	 * @param valor para el atributo descripcionNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionNivelAutorizacion(String descripcionNivelAutorizacion) {
		this.descripcionNivelAutorizacion = descripcionNivelAutorizacion;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo usuarios
	
	 * @return retorna la variable usuarios 
	 * @author Angela Maria Aguirre 
	 */
	public Usuarios getUsuarios() {
		return usuarios;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo usuarios
	
	 * @param valor para el atributo usuarios 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaAgrArticulo
	
	 * @return retorna la variable listaAgrArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> getListaAgrArticulo() {
		return listaAgrArticulo;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaAgrArticulo
	
	 * @param valor para el atributo listaAgrArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAgrArticulo(
			ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> listaAgrArticulo) {
		this.listaAgrArticulo = listaAgrArticulo;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaAgrServicio
	
	 * @return retorna la variable listaAgrServicio 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> getListaAgrServicio() {
		return listaAgrServicio;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaAgrServicio
	
	 * @param valor para el atributo listaAgrServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAgrServicio(
			ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> listaAgrServicio) {
		this.listaAgrServicio = listaAgrServicio;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaServicioEsp
	
	 * @return retorna la variable listaServicioEsp 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorServicioEspecifico> getListaServicioEsp() {
		return listaServicioEsp;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaServicioEsp
	
	 * @param valor para el atributo listaServicioEsp 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaServicioEsp(
			ArrayList<DTOBusquedaNivelAutorServicioEspecifico> listaServicioEsp) {
		this.listaServicioEsp = listaServicioEsp;
	}
	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaArticuloEsp
	
	 * @return retorna la variable listaArticuloEsp 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> getListaArticuloEsp() {
		return listaArticuloEsp;
	}
	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaArticuloEsp
	
	 * @param valor para el atributo listaArticuloEsp 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaArticuloEsp(
			ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> listaArticuloEsp) {
		this.listaArticuloEsp = listaArticuloEsp;
	}

}
