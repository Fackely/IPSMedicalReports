package com.princetonsa.dto.capitacion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * Esta clase se encarga de almacenar los datos de un 
 * nivel de autorización
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class DTONivelAutorizacion implements Serializable {

	private static final long serialVersionUID = 1L;	
	private int codigoPk;
	private int viasIngresoPK;
	private String descripcion;
	private String tipoAutorizacionAcronimo;
	private boolean activo;
	private boolean permiteEliminar;
	private boolean valorModificado;
	
	
	public DTONivelAutorizacion() {
		this.codigoPk		= ConstantesBD.codigoNuncaValido;
		this.descripcion	= "";
	}

	
	
	/**
	 * 
	 * Este Método se encarga de asignar un valor
	 * específico al hashCode de un objeto de tipo DTONivelAutorizacion
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
	 * de tipo DTONivelAutorizacion
	 * @param Object obj
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		boolean iguales = true;
		
		if(obj instanceof DTONivelAutorizacion){
			if(!(this.descripcion.equals(((DTONivelAutorizacion) obj).getDescripcion()))){
				iguales=false;
			}
			if(iguales){
				if(!(this.tipoAutorizacionAcronimo.equals(((DTONivelAutorizacion) obj).getTipoAutorizacionAcronimo()))){
					iguales = false;
				}
				if(iguales){
					if(!(this.viasIngresoPK == ((DTONivelAutorizacion) obj).getViasIngresoPK())){
						iguales = false;
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
	 * del atributo viasIngresoPK
	
	 * @return retorna la variable viasIngresoPK 
	 * @author Angela Maria Aguirre 
	 */
	public int getViasIngresoPK() {
		return viasIngresoPK;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo viasIngresoPK
	
	 * @param valor para el atributo viasIngresoPK 
	 * @author Angela Maria Aguirre 
	 */
	public void setViasIngresoPK(int viasIngresoPK) {
		this.viasIngresoPK = viasIngresoPK;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcion
	
	 * @return retorna la variable descripcion 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcion
	
	 * @param valor para el atributo descripcion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoAutorizacionAcronimo
	
	 * @return retorna la variable tipoAutorizacionAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoAutorizacionAcronimo() {
		return tipoAutorizacionAcronimo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoAutorizacionAcronimo
	
	 * @param valor para el atributo tipoAutorizacionAcronimo 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoAutorizacionAcronimo(String tipoAutorizacionAcronimo) {
		this.tipoAutorizacionAcronimo = tipoAutorizacionAcronimo;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo activo
	
	 * @return retorna la variable activo 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo activo
	
	 * @param valor para el atributo activo 
	 * @author Angela Maria Aguirre 
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
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
	 * del atributo valorModificado
	
	 * @return retorna la variable valorModificado 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isValorModificado() {
		return valorModificado;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorModificado
	
	 * @param valor para el atributo valorModificado 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorModificado(boolean valorModificado) {
		this.valorModificado = valorModificado;
	}

}
