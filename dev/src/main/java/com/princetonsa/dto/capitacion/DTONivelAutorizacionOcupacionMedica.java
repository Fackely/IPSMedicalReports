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
public class DTONivelAutorizacionOcupacionMedica implements Serializable {

	private static final long serialVersionUID = 1L;	
	private int codigoPk;
	private int ocupacionMedicaID;
	private int nivelAutorizacionUsuarioID;
	
	private String loginUsusarioRegistra;
	private Date fechaRegistro;
	private String horaRegistro;
	ArrayList<DTOPrioridadOcupacionMedica> listaPrioridadOcuMedica;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DTONivelAutorizacionOcupacionMedica(){
		
	}
	
	/**
	 * 
	 * Método constructor de la clase
	 * @param int idOcupacionMedica
	 * @author, Angela Maria Aguirre
	 */
	public DTONivelAutorizacionOcupacionMedica(int idOcupacionMedica){
		this.ocupacionMedicaID = idOcupacionMedica;
	}
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTONivelAutorizacionOcupacionMedica
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
	 * de tipo DTONivelAutorizacionOcupacionMedica
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = true;
		
		if(obj instanceof DTONivelAutorizacionOcupacionMedica){
			if(!(this.ocupacionMedicaID == ((DTONivelAutorizacionOcupacionMedica)obj).getOcupacionMedicaID())){
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
	 * valor del atributo ocupacionMedicaID
	 *
	 * @author Angela Aguirre 
	 */
	public int getOcupacionMedicaID() {
		return ocupacionMedicaID;
	}
	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo ocupacionMedicaID
	 *
	 * @author Angela Aguirre 
	 */
	public void setOcupacionMedicaID(int ocupacionMedicaID) {
		this.ocupacionMedicaID = ocupacionMedicaID;
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
	 * del atributo nivelAutorizacionUsuarioID
	
	 * @return retorna la variable nivelAutorizacionUsuarioID 
	 * @author Angela Maria Aguirre 
	 */
	public int getNivelAutorizacionUsuarioID() {
		return nivelAutorizacionUsuarioID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutorizacionUsuarioID
	
	 * @param valor para el atributo nivelAutorizacionUsuarioID 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacionUsuarioID(int nivelAutorizacionUsuarioID) {
		this.nivelAutorizacionUsuarioID = nivelAutorizacionUsuarioID;
	}
	
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo listaPrioridadOcuMedica
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<DTOPrioridadOcupacionMedica> getListaPrioridadOcuMedica() {
		return listaPrioridadOcuMedica;
	}

	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo listaPrioridadOcuMedica
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaPrioridadOcuMedica(
			ArrayList<DTOPrioridadOcupacionMedica> listaPrioridadOcuMedica) {
		this.listaPrioridadOcuMedica = listaPrioridadOcuMedica;
	}	

}
