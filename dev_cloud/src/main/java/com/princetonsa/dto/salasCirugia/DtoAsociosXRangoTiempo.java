package com.princetonsa.dto.salasCirugia;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */

public class DtoAsociosXRangoTiempo implements Serializable
{
	/*---------------------------------------------------------------
	 *  ESTOS ATRIBUTOS PERTENECEN A LA TABLA asocios_x_rango_tiempo
	 ---------------------------------------------------------------*/
	
	/**
	 * Cadigo de Asocios por rango de tiempo
	 */
	private Integer codigo;
	
	/**
	 * Cadigo de la cabezacera de Asocios por rango de tiempo
	 */
	private Integer codigoCabeza;
	
	/**
	 * Indica el tipo de servicio
	 */
	private String  tipoServicio;
	
	/**
	 * indica el tipo de cirugia
	 */
	private String  tipoCirugia;
	
	private Integer servicio;
	
	/**
	 * indica el tipo de anestecia
	 */
	private Integer tipoAnestesia;
	
	/**
	 * indica el tipo de asocio
	 */
	private Integer asocio;
	
	/**
	 * indica liquidarpor
	 * */
	private String liquidarPor;
	
	/**
	 * indica el tipo de tiempo base 
	 */
	private String  tipoTiempoBase;
	
	/**
	 *minutos Rango inicial 
	 */
	private Integer minutosRangoInicial;
	
	/**
	 *minutos rango final 
	 */
	private Integer minutosRangoFinal;
	
	/**
	 *minutos fraccion adicional 
	 */
	private Integer minFracAdicional;
	
	/**
	 * valor de fraccion adiconal
	 */
	private String  valorFracAdiconal;
	
	
	/**
	 * valor del asocio
	 */
	private String  valorAsocio;


	/**
	 * usuario que hace la modificacion 
	 */
	private String  usuarioModifica;
	
	/**
	 * la fecha en la que se hace la modificacion
	 */
	private String	fechaModifica;
	
	/**
	 * la hora en que se hace la modificacion
	 */
	private String horaModifica;
	
	/**
	 * este dato solo va lleno si el esquema tarifario no se 
	 * encuentra en el encabezado.
	 */
	private int esqTar;
	
	private int institucion;
	
	private int tipoEsq;
	
	/**
	 *ArrayList de Dto que almacena la informacion de los codigos
	 *del tarifario oficial  
	 */
	private ArrayList<DtoDetalleAsociosXRangoTiempo> detalleAsociosRangoTiempo;
	
	
	/*-----------------------------------------------------------
	 * METODOS GETTERS AND SETTERS
	 -----------------------------------------------------------*/
	


	public String getValorAsocio() {
		return valorAsocio;
	}

	public void setValorAsocio(String valorAsocio) {
		this.valorAsocio = valorAsocio;
	}

	public Integer getAsocio() {
		return asocio;
	}

	public void setAsocio(Integer asocio) {
		this.asocio = asocio;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	
	


	
	public String getFechaModifica() {
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica() {
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	public Integer getMinFracAdicional() {
		return minFracAdicional;
	}

	public void setMinFracAdicional(Integer minFracAdicional) {
		this.minFracAdicional = minFracAdicional;
	}

	public Integer getMinutosRangoFinal() {
		return minutosRangoFinal;
	}

	public void setMinutosRangoFinal(Integer minutosRangoFinal) {
		this.minutosRangoFinal = minutosRangoFinal;
	}

	public Integer getMinutosRangoInicial() {
		return minutosRangoInicial;
	}

	public void setMinutosRangoInicial(Integer minutosRangoInicial) {
		this.minutosRangoInicial = minutosRangoInicial;
	}

	

	public Integer getTipoAnestesia() {
		return tipoAnestesia;
	}

	public void setTipoAnestesia(Integer tipoAnestesia) {
		this.tipoAnestesia = tipoAnestesia;
	}

	public String getTipoCirugia() {
		return tipoCirugia;
	}

	public void setTipoCirugia(String tipoCirugia) {
		this.tipoCirugia = tipoCirugia;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public String getTipoTiempoBase() {
		return tipoTiempoBase;
	}

	public void setTipoTiempoBase(String tipoTiempoBase) {
		this.tipoTiempoBase = tipoTiempoBase;
	}

	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public String getValorFracAdiconal() {
		return valorFracAdiconal;
	}

	public void setValorFracAdiconal(String valorFracAdiconal) {
		this.valorFracAdiconal = valorFracAdiconal;
	}
	
	
	
	public ArrayList<DtoDetalleAsociosXRangoTiempo> getDetalleAsociosRangoTiempo() {
		return detalleAsociosRangoTiempo;
	}

	public void setDetalleAsociosRangoTiempo(
			ArrayList<DtoDetalleAsociosXRangoTiempo> detalleAsociosRangoTiempo) {
		this.detalleAsociosRangoTiempo = detalleAsociosRangoTiempo;
	}

	
	
	
	public Integer getServicio() {
		return servicio;
	}

	public void setServicio(Integer servicio) {
		this.servicio = servicio;
	}

	

	public Integer getCodigoCabeza() {
		return codigoCabeza;
	}

	public void setCodigoCabeza(Integer codigoCabeza) {
		this.codigoCabeza = codigoCabeza;
	}
	
	public int getEsqTar() {
		return esqTar;
	}

	public void setEsqTar(int esqTar) {
		this.esqTar = esqTar;
	}
	
	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getTipoEsq() {
		return tipoEsq;
	}

	public void setTipoEsq(int tipoEsq) {
		this.tipoEsq = tipoEsq;
	}
	
	/**
	 * @return the liquidarPor
	 */
	public String getLiquidarPor() {
		return liquidarPor;
	}

	/**
	 * @param liquidarPor the liquidarPor to set
	 */
	public void setLiquidarPor(String liquidarPor) {
		this.liquidarPor = liquidarPor;
	}


	/*-----------------------------------------------------------
	 * FIN  METODOS GETTERS AND SETTERS
	 -----------------------------------------------------------*/
	
	/*-----------------------------------------------------------
	 * SOBRE CARGA DE CONSTRUCTORES
	 ------------------------------------------------------------*/
	
	
	/**
	 * Constructor por defecto. 
	 */
	public DtoAsociosXRangoTiempo ()
	{
		this.asocio = -1;
		this.liquidarPor = "";
		this.codigo = -1;
		this.codigoCabeza =-1;
		this.esqTar=-1;
		this.fechaModifica = "";
		this.horaModifica = "";
		this.minFracAdicional = -1;
		this.minutosRangoFinal = -1;
		this.minutosRangoInicial = -1;
		this.tipoAnestesia = -1;
		this.tipoCirugia = "";
		this.tipoServicio = "";
		this.servicio = -1;
		this.tipoTiempoBase = "";
		this.usuarioModifica = "";
		this.valorFracAdiconal = "";
		this.valorAsocio = "";
		this.institucion=-1;
		this.tipoEsq =-1;
		this.detalleAsociosRangoTiempo = new ArrayList<DtoDetalleAsociosXRangoTiempo>();
		
	}
	
	/**
	 * Constructor con todos los campos requeridos
	 * @param asocio
	 * @param codigo
	 * @param convenio
	 * @param esquemaTarifario
	 * @param fechaModifica
	 * @param horaModifica
	 * @param institucion
	 * @param minFracAdicional
	 * @param minutosRangoFinal
	 * @param minutosRangoInicial
	 * @param tipo
	 * @param tipoAnestesia
	 * @param tipoCirugia
	 * @param tipoServicio
	 * @param servicio
	 * @param tipoTiempoBase
	 * @param usuarioModifica
	 * @param valorFracAdiconal
	 * @param detalleAsociosRangoTiempo
	 * @param detalleFechaAsosciosXRangoTiempo
	 */
	public DtoAsociosXRangoTiempo (Integer asocio,Integer tipoEsq,Integer codigo, Integer institucion, Integer codigoCabeza, Integer esqTar,String fechaModifica,
									String horaModifica, Integer minFracAdicional, Integer minutosRangoFinal,
									Integer minutosRangoInicial,Integer tipoAnestesia,String tipoCirugia,
									String tipoServicio, Integer servicio,String tipoTiempoBase,String usuarioModifica, String valorFracAdiconal,
									ArrayList<DtoDetalleAsociosXRangoTiempo> detalleAsociosRangoTiempo,String valorAsocio,String liquidarPor)
	{
		this.asocio = asocio;
		this.liquidarPor = liquidarPor;
		this.codigo = codigo;
		this.codigoCabeza = codigoCabeza;
		this.esqTar=esqTar;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.minFracAdicional = minFracAdicional;
		this.minutosRangoFinal = minutosRangoFinal;
		this.minutosRangoInicial = minutosRangoInicial;
		this.tipoAnestesia = tipoAnestesia;
		this.tipoCirugia = tipoCirugia;
		this.tipoServicio = tipoServicio;
		this.servicio = servicio;
		this.tipoTiempoBase = tipoTiempoBase;
		this.usuarioModifica = usuarioModifica;
		this.valorFracAdiconal = valorFracAdiconal;
		this.detalleAsociosRangoTiempo = detalleAsociosRangoTiempo;
		this.valorAsocio = valorAsocio;
		this.institucion=institucion;
		this.tipoEsq=tipoEsq;
	}
	
	 
	 /**
	  * Constructor solo con los campos que no pueden ser null
	  * @param asocio
	  * @param tipoTiempoBase
	  * @param minutosRangoInicial
	  * @param minutosRangoFinal
	  * @param tipo
	  * @param institucion
	  * @param usuarioModifica
	  * @param fechaModifica
	  * @param horaModifica
	  * @param detalleAsociosRangoTiempo
	  * @param detalleFechaAsosciosXRangoTiempo
	  */
	 public DtoAsociosXRangoTiempo (Integer asocio,String tipoTiempoBase,Integer tipoEsq, Integer institucion,Integer codigoCabeza ,Integer minutosRangoInicial,Integer minutosRangoFinal,
			 						Integer esqTar,String usuarioModifica, String fechaModifica,String horaModifica,
			 						ArrayList<DtoDetalleAsociosXRangoTiempo> detalleAsociosRangoTiempo,
									String valorAsocio,String liquidarPor)
	 {
		
		this.liquidarPor = liquidarPor;
		this.asocio = asocio;
		this.codigo = -1;
		this.codigoCabeza=codigoCabeza;
		this.esqTar=esqTar;
		this.fechaModifica = fechaModifica;
		this.horaModifica = horaModifica;
		this.minFracAdicional = -1;
		this.minutosRangoFinal = minutosRangoFinal;
		this.minutosRangoInicial = minutosRangoInicial;
		this.tipoAnestesia = -1;
		this.tipoCirugia = "";
		this.tipoServicio = "";
		this.servicio = -1;
		this.tipoTiempoBase = tipoTiempoBase;
		this.usuarioModifica = usuarioModifica;
		this.valorFracAdiconal = "";
		this.detalleAsociosRangoTiempo = detalleAsociosRangoTiempo;
		this.valorAsocio = valorAsocio;
		this.institucion=institucion;
		this.tipoEsq=tipoEsq;
		 
		 
	 }	
}