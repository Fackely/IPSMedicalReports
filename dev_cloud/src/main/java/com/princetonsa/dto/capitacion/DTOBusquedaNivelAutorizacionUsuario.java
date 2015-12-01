package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;

import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de almacenar los datos de un 
 * nivel de autorización de usuario
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class DTOBusquedaNivelAutorizacionUsuario implements Serializable {

	private static final long serialVersionUID = 1L;	
	private int codigoPk;
	private ArrayList<DTONivelAutorizacionUsuarioEspecifico> listaNivelUsuarioEsp;
	private ArrayList<DTONivelAutorizacionOcupacionMedica> listaNivelOcupacionMedica;
	private int nivelAutorizacionID;
	private String descripcionNivelAutorizacion;
	private Usuarios usuarios;
	
	
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo codigoPk
	 *
	 * @author Angela Aguirre 
	 */
	public int getCodigoPk() {
		return codigoPk;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo codigoPk
	 *
	 * @author Angela Aguirre 
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo listaNivelUsuarioEsp
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<DTONivelAutorizacionUsuarioEspecifico> getListaNivelUsuarioEsp() {
		return listaNivelUsuarioEsp;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo listaNivelUsuarioEsp
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaNivelUsuarioEsp(
			ArrayList<DTONivelAutorizacionUsuarioEspecifico> listaNivelUsuarioEsp) {
		this.listaNivelUsuarioEsp = listaNivelUsuarioEsp;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo listaNivelOcupacionMedica
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<DTONivelAutorizacionOcupacionMedica> getListaNivelOcupacionMedica() {
		return listaNivelOcupacionMedica;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo listaNivelOcupacionMedica
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaNivelOcupacionMedica(
			ArrayList<DTONivelAutorizacionOcupacionMedica> listaNivelOcupacionMedica) {
		this.listaNivelOcupacionMedica = listaNivelOcupacionMedica;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo usuarios
	 *
	 * @author Angela Aguirre 
	 */
	public Usuarios getUsuarios() {
		return usuarios;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo usuarios
	 *
	 * @author Angela Aguirre 
	 */
	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcionNivelAutorizacion
	
	 * @return retorna la variable descripcionNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionNivelAutorizacion() {
		return descripcionNivelAutorizacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcionNivelAutorizacion
	
	 * @param valor para el atributo descripcionNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionNivelAutorizacion(String descripcionNivelAutorizacion) {
		this.descripcionNivelAutorizacion = descripcionNivelAutorizacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAutorizacionID
	
	 * @return retorna la variable nivelAutorizacionID 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutorizacionID() {
		return nivelAutorizacionID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutorizacionID
	
	 * @param valor para el atributo nivelAutorizacionID 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacionID(int nivelAutorizacionID) {
		this.nivelAutorizacionID = nivelAutorizacionID;
	}	
	
	

}
