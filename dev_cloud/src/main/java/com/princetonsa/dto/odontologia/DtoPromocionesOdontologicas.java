package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class DtoPromocionesOdontologicas  implements Serializable{
	
	
	
	/**
	 * ATRIBUTOS DEL DTO PROMOCIONES ODONTOLOGICAS
	 */
	private  int codigoPk;
	private  String nombre;
	private String fechaInicialVigencia;
	private String fechaFinalVigencia;
	private String horaFinalVigencia;
	
	
	

	private String horaInicialVigencia;
	private String activo;
	private String horaModificada;
	private String fechaModificada;
	private String usuarioModifica;
	private int promocionOdontologica;
	private int institucion;
	private String fechaGeneracion;
	
	
	
	

	/**
	 * ATRIBUTOS DE AYUDA PARA HACER LA CONSULTA AVANZADA
	 */
	private String fechaGeneracionInicial;
	private String fechaGeneracionFinal;
	
	
	/**
	 * SE UTILIZA SOLO EN LA CONSULTA AVANZADA 
	 * DTO CARGAR LOS DETALLES DE LAS PROMOCIONES ODONTOLOGICAS
	 */
	private DtoDetPromocionOdo dtoDetPromociones;

	/**
	 * AYUNDANTE PARA SABER SI EXISTE UN DETALLE 
	 */
	private String existeDetalle;
	
	

	/**
	 * 
	 */
	void reset()
	{
		this.codigoPk=0;
		this.nombre="";
		this.fechaInicialVigencia="";
		this.fechaFinalVigencia="";
		this.horaFinalVigencia="";
		this.horaInicialVigencia="";
		this.activo=ConstantesBD.acronimoSi;
    	this.horaModificada="";
		this.fechaModificada="";
		this.usuarioModifica="";
		this.promocionOdontologica=0;
		this.institucion= ConstantesBD.codigoNuncaValido;
		this.dtoDetPromociones= new DtoDetPromocionOdo();
		this.setFechaGeneracion("");
		this.setFechaGeneracionInicial("");
		this.setFechaGeneracionFinal("");
		this.existeDetalle=ConstantesBD.acronimoNo;
	}
	
	/**
	 * 
	 */
	public DtoPromocionesOdontologicas(){
		this.reset();
	}


	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}


	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}


	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	/**
	 * @return the fechaInicialVigencia
	 */
	public String getFechaInicialVigencia() {
		return fechaInicialVigencia;
	}
	
		/**
	 * @return the fechaInicialVigencia
	 */
	public String getFechaInicialVigenciaBD() {
		return  UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialVigencia);
	}

	/**
	 * @param fechaInicialVigencia the fechaInicialVigencia to set
	 */
	
	/**
	 * 
	 * @return
	 */
	public String getFechaVigenciaInicialFormatoBD() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaInicialVigencia);
	}

	/**
	 * @return the fechaFinalVigencia
	 */
	public String getFechaFinalVigencia() {
		return fechaFinalVigencia;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaVigenciaFinalFormatoBD() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaFinalVigencia);
	}
	
	
	

	/**
	 * @param fechaFinalVigencia the fechaFinalVigencia to set
	 */
	public void setFechaFinalVigencia(String fechaFinalVigencia) {
		this.fechaFinalVigencia = fechaFinalVigencia;
	}


	/**
	 * @return the horaFinalVigencia
	 */
	public String getHoraFinalVigencia() {
		return horaFinalVigencia;
	}


	/**
	 * @param horaFinalVigencia the horaFinalVigencia to set
	 */
	public void setHoraFinalVigencia(String horaFinalVigencia) {
		this.horaFinalVigencia = horaFinalVigencia;
	}


	/**
	 * @return the horaInicialVigencia
	 */
	public String getHoraInicialVigencia() {
		return horaInicialVigencia;
	}
	
	


	/**
	 * @param horaInicialVigencia the horaInicialVigencia to set
	 */
	public void setHoraInicialVigencia(String horaInicialVigencia) {
		this.horaInicialVigencia = horaInicialVigencia;
	}


	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}


	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}


	/**
	 * @return the horaModificada
	 */
	public String getHoraModificada() {
		return horaModificada;
	}

	

	/**
	 * @param horaModificada the horaModificada to set
	 */
	public void setHoraModificada(String horaModificada) {
		this.horaModificada = horaModificada;
	}


	/**
	 * @return the fechaModificada
	 */
	public String getFechaModificada() {
		return fechaModificada;
	}

	/**
	 * 
	 */
	public String getFechaModificaBD()
	
	{
		return  UtilidadFecha.conversionFormatoFechaABD(this.fechaModificada);
	}
	
	



	/**
	 * @param fechaModificada the fechaModificada to set
	 */
	public void setFechaModificada(String fechaModificada) {
		this.fechaModificada = fechaModificada;
	}


	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public void setPromocionOdontologica(int promocion_odontologica) {
		this.promocionOdontologica = promocion_odontologica;
	}

	public int getPromocionOdontologica() {
		return promocionOdontologica;
	}

	/**
	 * @param dtoDetPromociones the dtoDetPromociones to set
	 */
	public void setDtoDetPromociones(DtoDetPromocionOdo dtoDetPromociones) {
		this.dtoDetPromociones = dtoDetPromociones;
	}

	/**
	 * @return the dtoDetPromociones
	 */
	public DtoDetPromocionOdo getDtoDetPromociones() {
		return dtoDetPromociones;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getFechaGeneracion() {
		return fechaGeneracion;
	}
	
	public String getFechaGeneracionBD() {
		return  UtilidadFecha.conversionFormatoFechaABD(this.fechaGeneracion);
	}

	

	public String getFechaGeneracionInicial() {
		return fechaGeneracionInicial;
	}
	
	public String getFechaGeneracionInicialBD() {
		return UtilidadFecha.conversionFormatoFechaABD(fechaGeneracionInicial) ;
	}

	public void setFechaGeneracionInicial(String fechaGeneracionInicial) {
		this.fechaGeneracionInicial = fechaGeneracionInicial;
	}
	

	public String getFechaGeneracionFinal() {
		return fechaGeneracionFinal;
	}
	
	public String getFechaGeneracionFinalBD() {
		return  UtilidadFecha.conversionFormatoFechaABD(fechaGeneracionFinal) ;
	}

	public void setFechaGeneracionFinal(String fechaGeneracionFinal) {
		this.fechaGeneracionFinal = fechaGeneracionFinal;
	}

	public void setExisteDetalle(String existeDetalle) {
		this.existeDetalle = existeDetalle;
	}

	public String getExisteDetalle() {
		return existeDetalle;
	}
	
	public void setFechaInicialVigencia(String fechaInicialVigencia) {
		this.fechaInicialVigencia = fechaInicialVigencia;
	}
	
	
	
	
	
}
