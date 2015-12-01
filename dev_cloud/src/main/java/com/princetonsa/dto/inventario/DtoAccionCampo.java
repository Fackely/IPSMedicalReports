/**
 * 
 */
package com.princetonsa.dto.inventario;

/**
 * @author jeilones
 *
 */
public class DtoAccionCampo {

	public static final int ACCION_CAMBIAR_VISIBILIDAD=0;
	public static final int ACCION_UTILIZAR_MEDICAMENTOS_POS=1;
	public static final int ACCION_INFORMAR_RIESGO_TRATAMIENTO=2;
	public static final int ACCION_CAMBIAR_OBLIGATORIEDAD=3;

	private Long codigo;
	private Long campoAccion;
	private String servicioAccion;
	private Long campoAfectado;
	private String servicioAfectado;
	private int accion;
	private int visibilidadInicial;
	private String valorValidar;
	/**
	 * 
	 */
	public DtoAccionCampo() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return the campoAccion
	 */
	public Long getCampoAccion() {
		return campoAccion;
	}
	/**
	 * @param campoAccion the campoAccion to set
	 */
	public void setCampoAccion(Long campoAccion) {
		this.campoAccion = campoAccion;
	}
	/**
	 * @return the servicioAccion
	 */
	public String getServicioAccion() {
		return servicioAccion;
	}
	/**
	 * @param servicioAccion the servicioAccion to set
	 */
	public void setServicioAccion(String servicioAccion) {
		this.servicioAccion = servicioAccion;
	}
	/**
	 * @return the campoAfectado
	 */
	public Long getCampoAfectado() {
		return campoAfectado;
	}
	/**
	 * @param campoAfectado the campoAfectado to set
	 */
	public void setCampoAfectado(Long campoAfectado) {
		this.campoAfectado = campoAfectado;
	}
	/**
	 * @return the servicioAfectado
	 */
	public String getServicioAfectado() {
		return servicioAfectado;
	}
	/**
	 * @param servicioAfectado the servicioAfectado to set
	 */
	public void setServicioAfectado(String servicioAfectado) {
		this.servicioAfectado = servicioAfectado;
	}
	/**
	 * @return the accion
	 */
	public int getAccion() {
		return accion;
	}
	/**
	 * @param accion the accion to set
	 */
	public void setAccion(int accion) {
		this.accion = accion;
	}
	/**
	 * @return the valorValidar
	 */
	public String getValorValidar() {
		return valorValidar;
	}
	/**
	 * @param valorValidar the valorValidar to set
	 */
	public void setValorValidar(String valorValidar) {
		this.valorValidar = valorValidar;
	}
	/**
	 * @return the visibilidadInicial
	 */
	public int getVisibilidadInicial() {
		return visibilidadInicial;
	}
	/**
	 * @param visibilidadInicial the visibilidadInicial to set
	 */
	public void setVisibilidadInicial(int visibilidadInicial) {
		this.visibilidadInicial = visibilidadInicial;
	}

}
