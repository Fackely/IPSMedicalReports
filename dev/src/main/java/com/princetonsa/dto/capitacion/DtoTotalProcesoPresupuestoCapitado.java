package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


/**
 * @author Cristhian Murillo, Fabian Becerra, Camilo Gomez
 */
public class DtoTotalProcesoPresupuestoCapitado implements Serializable
{

	/** * */
	private static final long serialVersionUID = 1L;
	
	/** *cantidadTotal */
	private Integer cantidadTotal;
	
	/** *valor */
	private Double valor;
	
	/** *tipoTotal */
	private String tipoTotal;

	/** *nivelAtencion */
	private Long nivelAtencion;
	
	/** *grupoServicio */
	private Integer grupoServicio;
	
	/** *claseInventario */
	private Integer claseInventario;
	
	/** Fecha del proceso */
	private Date fecha;
	
	/** Fecha del proceso */
	private Date fechaFinal;
	
	/** * Convenio */
	private Integer convenio;
	
	/** * contrato */
	private Integer contrato;
	
	/** *Lista de inconsistencias ocurridas durange la generacion de alguno de los procesos.  */
	private ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistencias;
	
	/** * Resultado de totales calculados */
	private ArrayList<DtoTotalProcesoPresupuestoCapitado> listaTotales;
	
	/** * Código del Servicio */
	private Integer codigoServicio;
	
	/** * Código del articulo */
	private Integer codigoArticulo;
	
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoTotalProcesoPresupuestoCapitado() {
		this.reset();
	}
	
	

	/**
	 * Constructor de la clase que inicializa con los valores del convenio, contrato y fecha enviados.
	 */
	public DtoTotalProcesoPresupuestoCapitado(Integer contrato, Integer convenio, Date fecha) {
		this.contrato 	= contrato;
		this.convenio 	= convenio;
		this.fecha 		= fecha;
	}
	
	
	
	/** * Reset de la forma */
	private void reset() 
	{ 
		this.cantidadTotal			= null;
		this.valor					= null;
		this.tipoTotal				= null;
		this.nivelAtencion			= null;
		this.grupoServicio			= null;
		this.claseInventario		= null;
		this.fecha					= null;
		this.fechaFinal				= null;
		this.convenio				= null;
		this.contrato 				= null;
		this.codigoArticulo			= null;
		this.listaInconsistencias	= new ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado>();
		this.listaTotales			= new ArrayList<DtoTotalProcesoPresupuestoCapitado>();
		this.codigoServicio			= null;
	}

	
	/**
	 * Inicializa en ceros los campos valor y cantidad para empezar a hacer suams acumulativas.
	 */
	public void inicializarParaTotalizar(String tipoTotal){
		this.cantidadTotal	= 0;
		this.valor			= 0.0;
		this.tipoTotal 		= tipoTotal;
	}
	
	
	/**
	* Este Método se encarga de establecer el valor del atributo valor
	* @param valor para el atributo valor 
	* @author Cristhian Murillo
	*/
	public void setValor(Double valor) {
		this.valor = valor;
	}

	
	/**
	* Este Método se encarga de obtener el valor del atributo valor
	* @return retorna la variable valor 
	* @author Cristhian Murillo
	*/
	public Double getValor() {
		return valor;
	}

	
	/**
	 * Este Método se encarga de obtener el valor del atributo nivelAtencion
	 * @return retorna la variable nivelAtencion 
	 * @author Cristhian Murillo
	 */
	public Long getNivelAtencion() {
		return nivelAtencion;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo nivelAtencion
	 * @param valor para el atributo nivelAtencion 
	 * @author Cristhian Murillo
	 */
	public void setNivelAtencion(Long nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo grupoServicio
	 * @return retorna la variable grupoServicio 
	 * @author Cristhian Murillo
	 */
	public Integer getGrupoServicio() {
		return grupoServicio;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo grupoServicio
	 * @param valor para el atributo grupoServicio 
	 * @author Cristhian Murillo
	 */
	public void setGrupoServicio(Integer grupoServicio) {
		this.grupoServicio = grupoServicio;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo claseInventario
	 * @return retorna la variable claseInventario 
	 * @author Cristhian Murillo
	 */
	public Integer getClaseInventario() {
		return claseInventario;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo claseInventario
	 * @param valor para el atributo claseInventario 
	 * @author Cristhian Murillo
	 */
	public void setClaseInventario(Integer claseInventario) {
		this.claseInventario = claseInventario;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo cantidadTotal
	 * @return retorna la variable cantidadTotal 
	 * @author Cristhian Murillo
	 */
	public Integer getCantidadTotal() {
		return cantidadTotal;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo cantidadTotal
	 * @param valor para el atributo cantidadTotal 
	 * @author Cristhian Murillo
	 */
	public void setCantidadTotal(Integer cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo tipoTotal
	 * @return retorna la variable tipoTotal 
	 * @author Cristhian Murillo
	 */
	public String getTipoTotal() {
		return tipoTotal;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo tipoTotal
	 * @param valor para el atributo tipoTotal 
	 * @author Cristhian Murillo
	 */
	public void setTipoTotal(String tipoTotal) {
		this.tipoTotal = tipoTotal;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo fecha
	 * @return retorna la variable fecha 
	 * @author Cristhian Murillo
	 */
	public Date getFecha() {
		return fecha;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo fecha
	 * @param valor para el atributo fecha 
	 * @author Cristhian Murillo
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo convenio
	 * @return retorna la variable convenio 
	 * @author Cristhian Murillo
	 */
	public Integer getConvenio() {
		return convenio;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo convenio
	 * @param valor para el atributo convenio 
	 * @author Cristhian Murillo
	 */
	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo contrato
	 * @return retorna la variable contrato 
	 * @author Cristhian Murillo
	 */
	public Integer getContrato() {
		return contrato;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo contrato
	 * @param valor para el atributo contrato 
	 * @author Cristhian Murillo
	 */
	public void setContrato(Integer contrato) {
		this.contrato = contrato;
	}

	
	/**
	 * Este Método se encarga de obtener el valor del atributo listaInconsistencias
	 * @return retorna la variable listaInconsistencias 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> getListaInconsistencias() {
		return listaInconsistencias;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo listaInconsistencias
	 * @param valor para el atributo listaInconsistencias 
	 * @author Cristhian Murillo
	 */
	public void setListaInconsistencias(
			ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> listaInconsistencias) {
		this.listaInconsistencias = listaInconsistencias;
	}
	

	/**
	 * Este Método se encarga de obtener el valor del atributo listaTotales
	 * @return retorna la variable listaTotales 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> getListaTotales() {
		return listaTotales;
	}
	
	
	/**
	 * Este Método se encarga de establecer el valor del atributo listaTotales
	 * @param valor para el atributo listaTotales 
	 * @author Cristhian Murillo
	 */
	public void setListaTotales(
			ArrayList<DtoTotalProcesoPresupuestoCapitado> listaTotales) {
		this.listaTotales = listaTotales;
	}

	
	/**
	 * Este Método se encarga de establecer el valor del atributo codigoServicio
	 * @param valor para el atributo codigoServicio 
	 * @author Fabian Becerra
	 */
	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo codigoServicio
	 * @return retorna la variable codigoServicio 
	 * @author Fabian Becerra
	 */
	public Integer getCodigoServicio() {
		return codigoServicio;
	}


	/**
	 * Este Método se encarga de establecer el valor del atributo codigoArticulo
	 * @param valor para el atributo codigoArticulo 
	 * @author Fabian Becerra
	 */
	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}


	/**
	 * Este Método se encarga de obtener el valor del atributo codigoArticulo
	 * @return retorna la variable codigoArticulo 
	 * @author Fabian Becerra
	 */
	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}



	/**
	 * @return valor de fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}



	/**
	 * @param fechaFinal el fechaFinal para asignar
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
}
