package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Esta clase se encarga de almacenar los datos de un 
 * nivel de autorización de usuario
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class DTONivelAutorizacionUsuarioEspecifico implements Serializable {

	private static final long serialVersionUID = 1L;	
	private int codigoPk;
	private String usuarioEspLogin;
	private int nivelAutorizacionUsuID;
	private String loginUsusarioRegistra;
	private Date fechaRegistro;
	private String horaRegistro;
	ArrayList<DTOPrioridadUsuarioEspecifico> listaPriodidadUsuarioEsp;
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTONivelAutorizacionUsuarioEspecifico
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
	 * de tipo DTONivelAutorizacionUsuarioEspecifico
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = true;
		
		if(obj instanceof DTONivelAutorizacionUsuarioEspecifico){
			if(!(this.usuarioEspLogin.equals(((DTONivelAutorizacionUsuarioEspecifico)obj).getUsuarioEspLogin()))){
				iguales=false;
			}					
		}		
		return iguales;
	}
	
	
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
	 * valor del atributo usuarioEspLogin
	 *
	 * @author Angela Aguirre 
	 */
	public String getUsuarioEspLogin() {
		return usuarioEspLogin;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo usuarioEspLogin
	 *
	 * @author Angela Aguirre 
	 */
	public void setUsuarioEspLogin(String usuarioEspLogin) {
		this.usuarioEspLogin = usuarioEspLogin;
	}
	
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo loginUsusarioRegistra
	 *
	 * @author Angela Aguirre 
	 */
	public String getLoginUsusarioRegistra() {
		return loginUsusarioRegistra;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo loginUsusarioRegistra
	 *
	 * @author Angela Aguirre 
	 */
	public void setLoginUsusarioRegistra(String loginUsusarioRegistra) {
		this.loginUsusarioRegistra = loginUsusarioRegistra;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo fechaRegistro
	 *
	 * @author Angela Aguirre 
	 */
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo fechaRegistro
	 *
	 * @author Angela Aguirre 
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo horaRegistro
	 *
	 * @author Angela Aguirre 
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo horaRegistro
	 *
	 * @author Angela Aguirre 
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAutorizacionUsuID
	
	 * @return retorna la variable nivelAutorizacionUsuID 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutorizacionUsuID() {
		return nivelAutorizacionUsuID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutorizacionUsuID
	
	 * @param valor para el atributo nivelAutorizacionUsuID 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacionUsuID(int nivelAutorizacionUsuID) {
		this.nivelAutorizacionUsuID = nivelAutorizacionUsuID;
	}
	
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo listaPriodidadUsuarioEsp
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<DTOPrioridadUsuarioEspecifico> getListaPriodidadUsuarioEsp() {
		return listaPriodidadUsuarioEsp;
	}

	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo listaPriodidadUsuarioEsp
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaPriodidadUsuarioEsp(
			ArrayList<DTOPrioridadUsuarioEspecifico> listaPriodidadUsuarioEsp) {
		this.listaPriodidadUsuarioEsp = listaPriodidadUsuarioEsp;
	}

}
