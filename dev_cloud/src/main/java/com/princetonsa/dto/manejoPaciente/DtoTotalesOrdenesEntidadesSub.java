package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

public class DtoTotalesOrdenesEntidadesSub implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/*******************TOTALES POR TIPO CONSULTA NIVEL DE ATENCION***************************/
	
	/**Totales por cada servicio-medicamento*/
	private Integer totalServicioAutorizado;
	private Integer totalServicioAnulado;
	private Integer totalArticuloAutorizado;
	private Integer totalArticuloAnulado;
	private BigDecimal valorServicioAutorizado;
	private BigDecimal valorServicioAnulado;
	private BigDecimal valorArticuloAutorizado;
	private BigDecimal valorArticuloAnulado;
	
	/**Totales finales de cada nivel de atencion*/
	private HashMap<String, Integer> totalNivelAutorizado;
	private HashMap<String, Integer> totalNivelAnulado;
	private HashMap<String, BigDecimal> valorNivelAutorizado;
	private HashMap<String, BigDecimal> valorNivelAnulado;	
	
	/**Totales finales de cada convenio*/	
	private HashMap<Integer, Integer> totalConvenioAutorizado;
	private HashMap<Integer, Integer> totalConvenioAnulado;	
	private HashMap<Integer, BigDecimal> valorConvenioAutorizado;
	private HashMap<Integer, BigDecimal> valorConvenioAnulado;	
	
	/**Totales finales de cada entidad*/
	private HashMap<Long, Integer> totalEntidadAutorizado;
	private HashMap<Long, Integer> totalEntidadAnulado;
	private HashMap<Long, BigDecimal> valorEntidadAutorizado;
	private HashMap<Long, BigDecimal> valorEntidadAnulado;
		
	/**Totales finales General*/
	private Integer totalGeneralAutorizado;
	private Integer totalGeneralAnulado;
	private BigDecimal valorGeneralAutorizado;
	private BigDecimal valorGeneralAnulado;
	
	
	/**Descripcion de los Grupos, Clase, Servicios y Medicamentos*/
	private Integer totalGrupoAutorizado;
	private Integer totalGrupoAnulado;
	private BigDecimal valorGrupoAutorizado;
	private BigDecimal valorGrupoAnulado;
	
	private Integer totalClaseAutorizado;
	private Integer totalClaseAnulado;
	private BigDecimal valorClaseAutorizado;
	private BigDecimal valorClaseAnulado;
	
	
	/*******************TOTALES POR TIPO CONSULTA GRUPO SERVICIOS Y CLASE INVENTARIOS***************************/
	/**Lista de totales para Grupo Servicios*/
	/*ArrayList<DtoTotalesOrdenesEntidadesSub>listaGrupoServicio;
	/**Lista de totales para Clase Inventarios* /
	ArrayList<DtoTotalesOrdenesEntidadesSub>listaClaseInventario;*/
	
	private HashMap<String, Integer> totalGrupoAuto = new HashMap<String, Integer>();
	private HashMap<String, Integer> totalGrupoAnul = new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> valorGrupoAuto = new HashMap<String, BigDecimal>();
	private HashMap<String, BigDecimal> valorGrupoAnul = new HashMap<String, BigDecimal>();
	
	private HashMap<String, Integer> totalClaseAuto = new HashMap<String, Integer>();
	private HashMap<String, Integer> totalClaseAnul = new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> valorClaseAuto = new HashMap<String, BigDecimal>();
	private HashMap<String, BigDecimal> valorClaseAnul = new HashMap<String, BigDecimal>();
	
	
	private HashMap<String, Integer> cantidadGrupoAuto=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaGrupoAuto=new HashMap<String, BigDecimal>();
	private HashMap<String, Integer> cantidadGrupoAnul=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaGrupoAnul=new HashMap<String, BigDecimal>();
	
	private HashMap<String, Integer> cantidadClaseAuto=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaClaseAuto=new HashMap<String, BigDecimal>();
	private HashMap<String, Integer> cantidadClaseAnul=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaClaseAnul=new HashMap<String, BigDecimal>();
	
	
	/*******************TOTALES POR TIPO CONSULTA DETALLADO***************************/
	private HashMap<Integer, Integer> totalServicioAuto = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> totalServicioAnul = new HashMap<Integer, Integer>();
	private HashMap<Integer, BigDecimal> valorServicioAuto = new HashMap<Integer, BigDecimal>();
	private HashMap<Integer, BigDecimal> valorServicioAnul = new HashMap<Integer, BigDecimal>();
	
	private HashMap<Integer, Integer> totalArticuloAuto = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> totalArticuloAnul = new HashMap<Integer, Integer>();
	private HashMap<Integer, BigDecimal> valorArticuloAuto = new HashMap<Integer, BigDecimal>();
	private HashMap<Integer, BigDecimal> valorArticuloAnul = new HashMap<Integer, BigDecimal>();
	
	private HashMap<String, Integer> cantidadServiciosAuto=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaServiciosAuto=new HashMap<String, BigDecimal>();
	private HashMap<String, Integer> cantidadServiciosAnul=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaServiciosAnul=new HashMap<String, BigDecimal>();
	
	private HashMap<String, Integer> cantidadArticulosAuto=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaArticulosAuto=new HashMap<String, BigDecimal>();
	private HashMap<String, Integer> cantidadArticulosAnul=new HashMap<String, Integer>();
	private HashMap<String, BigDecimal> tarifaArticulosAnul=new HashMap<String, BigDecimal>();
	
	private Integer contadorServicios;
	private Integer contadorArticulos;
	
	private Integer contadorAutorizaciones;
	
	/**Metodo constructor de la clase*/
	public DtoTotalesOrdenesEntidadesSub() {
		this.reset();
	}
		
	private void reset()
	{	//-
		this.totalServicioAutorizado=new Integer(0);
		this.totalServicioAnulado=new Integer(0);
		this.totalArticuloAutorizado=new Integer(0);
		this.totalArticuloAnulado=new Integer(0);
		this.valorServicioAutorizado=new BigDecimal(0.0);
		this.valorServicioAnulado=new BigDecimal(0.0);
		this.valorArticuloAutorizado=new BigDecimal(0.0);
		this.valorArticuloAnulado=new BigDecimal(0.0);
		this.totalNivelAutorizado=new HashMap<String, Integer>();
		this.totalNivelAnulado=new HashMap<String, Integer>();
		this.valorNivelAutorizado=new HashMap<String, BigDecimal>();
		this.valorNivelAnulado=new HashMap<String, BigDecimal>();
		this.totalConvenioAutorizado=new HashMap<Integer, Integer>();
		this.totalConvenioAnulado=new HashMap<Integer, Integer>();
		this.valorConvenioAutorizado=new HashMap<Integer, BigDecimal>();
		this.valorConvenioAnulado=new HashMap<Integer, BigDecimal>();
		this.totalEntidadAutorizado=new HashMap<Long, Integer>();
		this.totalEntidadAnulado=new HashMap<Long, Integer>();
		this.valorEntidadAutorizado=new HashMap<Long, BigDecimal>();
		this.valorEntidadAnulado=new HashMap<Long, BigDecimal>();
		this.totalGeneralAutorizado=new Integer(0);
		this.totalGeneralAnulado=new Integer(0);
		this.valorGeneralAutorizado=new BigDecimal(0.0).setScale(2);
		this.valorGeneralAnulado=new BigDecimal(0.0).setScale(2);
		
		/*this.listaGrupoServicio=new ArrayList<DtoTotalesOrdenesEntidadesSub>();		
		this.listaClaseInventario=new ArrayList<DtoTotalesOrdenesEntidadesSub>();*/
		
		this.totalGrupoAutorizado=new Integer(0);
		this.totalGrupoAnulado=new Integer(0);
		this.valorGrupoAutorizado=new BigDecimal(0.0).setScale(2);
		this.valorGrupoAnulado=new BigDecimal(0.0).setScale(2);
		
		this.totalClaseAutorizado=new Integer(0);
		this.totalClaseAnulado=new Integer(0);
		this.valorClaseAutorizado=new BigDecimal(0.0).setScale(2);
		this.valorClaseAnulado=new BigDecimal(0.0).setScale(2);
		
		//--
		this.totalGrupoAuto = new HashMap<String, Integer>();
		this.totalGrupoAnul = new HashMap<String, Integer>();
		this.valorGrupoAuto = new HashMap<String, BigDecimal>();
		this.valorGrupoAnul = new HashMap<String, BigDecimal>();
		
		this.totalClaseAuto = new HashMap<String, Integer>();
		this.totalClaseAnul = new HashMap<String, Integer>();
		this.valorClaseAuto = new HashMap<String, BigDecimal>();
		this.valorClaseAnul = new HashMap<String, BigDecimal>();
		
		this.cantidadGrupoAuto=new HashMap<String, Integer>();
		this.tarifaGrupoAuto=new HashMap<String, BigDecimal>();
		this.cantidadGrupoAnul=new HashMap<String, Integer>();
		this.tarifaGrupoAnul=new HashMap<String, BigDecimal>();
		
		this.cantidadClaseAuto=new HashMap<String, Integer>();
		this.tarifaClaseAuto=new HashMap<String, BigDecimal>();
		this.cantidadClaseAnul=new HashMap<String, Integer>();
		this.tarifaClaseAnul=new HashMap<String, BigDecimal>();
		
		//--
		this.totalServicioAuto = new HashMap<Integer, Integer>();
		this.totalServicioAnul = new HashMap<Integer, Integer>();
		this.valorServicioAuto = new HashMap<Integer, BigDecimal>();
		this.valorServicioAnul = new HashMap<Integer, BigDecimal>();
		
		this.totalArticuloAuto = new HashMap<Integer, Integer>();
		this.totalArticuloAnul = new HashMap<Integer, Integer>();
		this.valorArticuloAuto = new HashMap<Integer, BigDecimal>();
		this.valorArticuloAnul = new HashMap<Integer, BigDecimal>();
		
		this.cantidadServiciosAuto=new HashMap<String, Integer>();
		this.tarifaServiciosAuto=new HashMap<String, BigDecimal>();
		this.cantidadServiciosAnul=new HashMap<String, Integer>();
		this.tarifaServiciosAnul=new HashMap<String, BigDecimal>();
		
		this.cantidadArticulosAuto=new HashMap<String, Integer>();
		this.tarifaArticulosAuto=new HashMap<String, BigDecimal>();
		this.cantidadArticulosAnul=new HashMap<String, Integer>();
		this.tarifaArticulosAnul=new HashMap<String, BigDecimal>();
		
		this.contadorServicios=new Integer(0);
		this.contadorArticulos=new Integer(0);
		
		this.setContadorAutorizaciones(new Integer(0));
	}
	
	
	public Integer getTotalServicioAutorizado() {
		return totalServicioAutorizado;
	}
	public void setTotalServicioAutorizado(Integer totalServicioAutorizado) {
		this.totalServicioAutorizado = totalServicioAutorizado;
	}
	public Integer getTotalServicioAnulado() {
		return totalServicioAnulado;
	}
	public void setTotalServicioAnulado(Integer totalServicioAnulado) {
		this.totalServicioAnulado = totalServicioAnulado;
	}
	public Integer getTotalArticuloAutorizado() {
		return totalArticuloAutorizado;
	}
	public void setTotalArticuloAutorizado(Integer totalArticuloAutorizado) {
		this.totalArticuloAutorizado = totalArticuloAutorizado;
	}
	public Integer getTotalArticuloAnulado() {
		return totalArticuloAnulado;
	}
	public void setTotalArticuloAnulado(Integer totalArticuloAnulado) {
		this.totalArticuloAnulado = totalArticuloAnulado;
	}
	public BigDecimal getValorServicioAutorizado() {
		return valorServicioAutorizado;
	}
	public void setValorServicioAutorizado(
			BigDecimal valorServicioAutorizado) {
		this.valorServicioAutorizado = valorServicioAutorizado;
	}
	public BigDecimal getValorServicioAnulado() {
		return valorServicioAnulado;
	}
	public void setValorServicioAnulado(BigDecimal valorServicioAnulado) {
		this.valorServicioAnulado = valorServicioAnulado;
	}
	public BigDecimal getValorArticuloAutorizado() {
		return valorArticuloAutorizado;
	}
	public void setValorArticuloAutorizado(
			BigDecimal valorArticuloAutorizado) {
		this.valorArticuloAutorizado = valorArticuloAutorizado;
	}
	public BigDecimal getValorArticuloAnulado() {
		return valorArticuloAnulado;
	}
	public void setValorArticuloAnulado(BigDecimal valorArticuloAnulado) {
		this.valorArticuloAnulado = valorArticuloAnulado;
	}
	public HashMap<String, Integer> getTotalNivelAutorizado() {
		return totalNivelAutorizado;
	}
	public void setTotalNivelAutorizado(
			HashMap<String, Integer> totalNivelAutorizado) {
		this.totalNivelAutorizado = totalNivelAutorizado;
	}
	public HashMap<String, Integer> getTotalNivelAnulado() {
		return totalNivelAnulado;
	}
	public void setTotalNivelAnulado(HashMap<String, Integer> totalNivelAnulado) {
		this.totalNivelAnulado = totalNivelAnulado;
	}
	public HashMap<String, BigDecimal> getValorNivelAutorizado() {
		return valorNivelAutorizado;
	}
	public void setValorNivelAutorizado(
			HashMap<String, BigDecimal> valorNivelAutorizado) {
		this.valorNivelAutorizado = valorNivelAutorizado;
	}
	public HashMap<String, BigDecimal> getValorNivelAnulado() {
		return valorNivelAnulado;
	}
	public void setValorNivelAnulado(HashMap<String, BigDecimal> valorNivelAnulado) {
		this.valorNivelAnulado = valorNivelAnulado;
	}
	public HashMap<Integer, Integer> getTotalConvenioAutorizado() {
		return totalConvenioAutorizado;
	}
	public void setTotalConvenioAutorizado(
			HashMap<Integer, Integer> totalConvenioAutorizado) {
		this.totalConvenioAutorizado = totalConvenioAutorizado;
	}
	public HashMap<Integer, Integer> getTotalConvenioAnulado() {
		return totalConvenioAnulado;
	}
	public void setTotalConvenioAnulado(
			HashMap<Integer, Integer> totalConvenioAnulado) {
		this.totalConvenioAnulado = totalConvenioAnulado;
	}
	public HashMap<Integer, BigDecimal> getValorConvenioAutorizado() {
		return valorConvenioAutorizado;
	}
	public void setValorConvenioAutorizado(
			HashMap<Integer, BigDecimal> valorConvenioAutorizado) {
		this.valorConvenioAutorizado = valorConvenioAutorizado;
	}
	public HashMap<Integer, BigDecimal> getValorConvenioAnulado() {
		return valorConvenioAnulado;
	}
	public void setValorConvenioAnulado(
			HashMap<Integer, BigDecimal> valorConvenioAnulado) {
		this.valorConvenioAnulado = valorConvenioAnulado;
	}
	public HashMap<Long, Integer> getTotalEntidadAutorizado() {
		return totalEntidadAutorizado;
	}
	public void setTotalEntidadAutorizado(
			HashMap<Long, Integer> totalEntidadAutorizado) {
		this.totalEntidadAutorizado = totalEntidadAutorizado;
	}
	public HashMap<Long, Integer> getTotalEntidadAnulado() {
		return totalEntidadAnulado;
	}
	public void setTotalEntidadAnulado(HashMap<Long, Integer> totalEntidadAnulado) {
		this.totalEntidadAnulado = totalEntidadAnulado;
	}
	public HashMap<Long, BigDecimal> getValorEntidadAutorizado() {
		return valorEntidadAutorizado;
	}
	public void setValorEntidadAutorizado(
			HashMap<Long, BigDecimal> valorEntidadAutorizado) {
		this.valorEntidadAutorizado = valorEntidadAutorizado;
	}
	public HashMap<Long, BigDecimal> getValorEntidadAnulado() {
		return valorEntidadAnulado;
	}
	public void setValorEntidadAnulado(
			HashMap<Long, BigDecimal> valorEntidadAnulado) {
		this.valorEntidadAnulado = valorEntidadAnulado;
	}
	public Integer getTotalGeneralAutorizado() {
		return totalGeneralAutorizado;
	}
	public void setTotalGeneralAutorizado(Integer totalGeneralAutorizado) {
		this.totalGeneralAutorizado = totalGeneralAutorizado;
	}
	public Integer getTotalGeneralAnulado() {
		return totalGeneralAnulado;
	}
	public void setTotalGeneralAnulado(Integer totalGeneralAnulado) {
		this.totalGeneralAnulado = totalGeneralAnulado;
	}
	public BigDecimal getValorGeneralAutorizado() {
		return valorGeneralAutorizado;
	}
	public void setValorGeneralAutorizado(BigDecimal valorGeneralAutorizado) {
		this.valorGeneralAutorizado = valorGeneralAutorizado;
	}
	public BigDecimal getValorGeneralAnulado() {
		return valorGeneralAnulado;
	}
	public void setValorGeneralAnulado(BigDecimal valorGeneralAnulado) {
		this.valorGeneralAnulado = valorGeneralAnulado;
	}
	/*public ArrayList<DtoTotalesOrdenesEntidadesSub> getListaGrupoServicio() {
		return listaGrupoServicio;
	}
	public void setListaGrupoServicio(
			ArrayList<DtoTotalesOrdenesEntidadesSub> listaGrupoServicio) {
		this.listaGrupoServicio = listaGrupoServicio;
	}
	public ArrayList<DtoTotalesOrdenesEntidadesSub> getListaClaseInventario() {
		return listaClaseInventario;
	}
	public void setListaClaseInventario(
			ArrayList<DtoTotalesOrdenesEntidadesSub> listaClaseInventario) {
		this.listaClaseInventario = listaClaseInventario;
	}*/

	/**
	 * @return the totalGrupoAutorizado
	 */
	public Integer getTotalGrupoAutorizado() {
		return totalGrupoAutorizado;
	}

	/**
	 * @param totalGrupoAutorizado the totalGrupoAutorizado to set
	 */
	public void setTotalGrupoAutorizado(Integer totalGrupoAutorizado) {
		this.totalGrupoAutorizado = totalGrupoAutorizado;
	}

	/**
	 * @return the totalGrupoAnulado
	 */
	public Integer getTotalGrupoAnulado() {
		return totalGrupoAnulado;
	}

	/**
	 * @param totalGrupoAnulado the totalGrupoAnulado to set
	 */
	public void setTotalGrupoAnulado(Integer totalGrupoAnulado) {
		this.totalGrupoAnulado = totalGrupoAnulado;
	}

	/**
	 * @return the valorGrupoAutorizado
	 */
	public BigDecimal getValorGrupoAutorizado() {
		return valorGrupoAutorizado;
	}

	/**
	 * @param valorGrupoAutorizado the valorGrupoAutorizado to set
	 */
	public void setValorGrupoAutorizado(BigDecimal valorGrupoAutorizado) {
		this.valorGrupoAutorizado = valorGrupoAutorizado;
	}

	/**
	 * @return the valorGrupoAnulado
	 */
	public BigDecimal getValorGrupoAnulado() {
		return valorGrupoAnulado;
	}

	/**
	 * @param valorGrupoAnulado the valorGrupoAnulado to set
	 */
	public void setValorGrupoAnulado(BigDecimal valorGrupoAnulado) {
		this.valorGrupoAnulado = valorGrupoAnulado;
	}

	/**
	 * @return the totalClaseAutorizado
	 */
	public Integer getTotalClaseAutorizado() {
		return totalClaseAutorizado;
	}

	/**
	 * @param totalClaseAutorizado the totalClaseAutorizado to set
	 */
	public void setTotalClaseAutorizado(Integer totalClaseAutorizado) {
		this.totalClaseAutorizado = totalClaseAutorizado;
	}

	/**
	 * @return the totalClaseAnulado
	 */
	public Integer getTotalClaseAnulado() {
		return totalClaseAnulado;
	}

	/**
	 * @param totalClaseAnulado the totalClaseAnulado to set
	 */
	public void setTotalClaseAnulado(Integer totalClaseAnulado) {
		this.totalClaseAnulado = totalClaseAnulado;
	}

	/**
	 * @return the valorClaseAutorizado
	 */
	public BigDecimal getValorClaseAutorizado() {
		return valorClaseAutorizado;
	}

	/**
	 * @param valorClaseAutorizado the valorClaseAutorizado to set
	 */
	public void setValorClaseAutorizado(BigDecimal valorClaseAutorizado) {
		this.valorClaseAutorizado = valorClaseAutorizado;
	}

	/**
	 * @return the valorClaseAnulado
	 */
	public BigDecimal getValorClaseAnulado() {
		return valorClaseAnulado;
	}

	/**
	 * @param valorClaseAnulado the valorClaseAnulado to set
	 */
	public void setValorClaseAnulado(BigDecimal valorClaseAnulado) {
		this.valorClaseAnulado = valorClaseAnulado;
	}

	/**
	 * @return the totalGrupoAuto
	 */
	public HashMap<String, Integer> getTotalGrupoAuto() {
			return totalGrupoAuto;
	}

	/**
	 * @param totalGrupoAuto the totalGrupoAuto to set
	 */
	public void setTotalGrupoAuto(HashMap<String, Integer> totalGrupoAuto) {
		this.totalGrupoAuto = totalGrupoAuto;
	}

	/**
	 * @return the totalGrupoAnul
	 */
	public HashMap<String, Integer> getTotalGrupoAnul() {
			return totalGrupoAnul;
	}

	/**
	 * @param totalGrupoAnul the totalGrupoAnul to set
	 */
	public void setTotalGrupoAnul(HashMap<String, Integer> totalGrupoAnul) {
		this.totalGrupoAnul = totalGrupoAnul;
	}

	/**
	 * @return the valorGrupoAuto
	 */
	public HashMap<String, BigDecimal> getValorGrupoAuto() {
		return valorGrupoAuto;
	}

	/**
	 * @param valorGrupoAuto the valorGrupoAuto to set
	 */
	public void setValorGrupoAuto(HashMap<String, BigDecimal> valorGrupoAuto) {
		this.valorGrupoAuto = valorGrupoAuto;
	}

	/**
	 * @return the valorGrupoAnul
	 */
	public HashMap<String, BigDecimal> getValorGrupoAnul() {
			return valorGrupoAnul;
	}

	/**
	 * @param valorGrupoAnul the valorGrupoAnul to set
	 */
	public void setValorGrupoAnul(HashMap<String, BigDecimal> valorGrupoAnul) {
		this.valorGrupoAnul = valorGrupoAnul;
	}

	/**
	 * @return the totalClaseAuto
	 */
	public HashMap<String, Integer> getTotalClaseAuto() {
		return totalClaseAuto;
	}

	/**
	 * @param totalClaseAuto the totalClaseAuto to set
	 */
	public void setTotalClaseAuto(HashMap<String, Integer> totalClaseAuto) {
		this.totalClaseAuto = totalClaseAuto;
	}

	/**
	 * @return the totalClaseAnul
	 */
	public HashMap<String, Integer> getTotalClaseAnul() {
		return totalClaseAnul;
	}

	/**
	 * @param totalClaseAnul the totalClaseAnul to set
	 */
	public void setTotalClaseAnul(HashMap<String, Integer> totalClaseAnul) {
		this.totalClaseAnul = totalClaseAnul;
	}

	/**
	 * @return the valorClaseAuto
	 */
	public HashMap<String, BigDecimal> getValorClaseAuto() {
		return valorClaseAuto;
	}

	/**
	 * @param valorClaseAuto the valorClaseAuto to set
	 */
	public void setValorClaseAuto(HashMap<String, BigDecimal> valorClaseAuto) {
		this.valorClaseAuto = valorClaseAuto;
	}

	/**
	 * @return the valorClaseAnul
	 */
	public HashMap<String, BigDecimal> getValorClaseAnul() {
		return valorClaseAnul;
	}

	/**
	 * @param valorClaseAnul the valorClaseAnul to set
	 */
	public void setValorClaseAnul(HashMap<String, BigDecimal> valorClaseAnul) {
		this.valorClaseAnul = valorClaseAnul;
	}

	public HashMap<Integer, Integer> getTotalServicioAuto() {
		return totalServicioAuto;
	}

	public void setTotalServicioAuto(HashMap<Integer, Integer> totalServicioAuto) {
		this.totalServicioAuto = totalServicioAuto;
	}

	public HashMap<Integer, Integer> getTotalServicioAnul() {
		return totalServicioAnul;
	}

	public void setTotalServicioAnul(HashMap<Integer, Integer> totalServicioAnul) {
		this.totalServicioAnul = totalServicioAnul;
	}

	public HashMap<Integer, BigDecimal> getValorServicioAuto() {
		return valorServicioAuto;
	}

	public void setValorServicioAuto(HashMap<Integer, BigDecimal> valorServicioAuto) {
		this.valorServicioAuto = valorServicioAuto;
	}

	public HashMap<Integer, BigDecimal> getValorServicioAnul() {
		return valorServicioAnul;
	}

	public void setValorServicioAnul(HashMap<Integer, BigDecimal> valorServicioAnul) {
		this.valorServicioAnul = valorServicioAnul;
	}

	public HashMap<Integer, Integer> getTotalArticuloAuto() {
		return totalArticuloAuto;
	}

	public void setTotalArticuloAuto(HashMap<Integer, Integer> totalArticuloAuto) {
		this.totalArticuloAuto = totalArticuloAuto;
	}

	public HashMap<Integer, Integer> getTotalArticuloAnul() {
		return totalArticuloAnul;
	}

	public void setTotalArticuloAnul(HashMap<Integer, Integer> totalArticuloAnul) {
		this.totalArticuloAnul = totalArticuloAnul;
	}

	public HashMap<Integer, BigDecimal> getValorArticuloAuto() {
		return valorArticuloAuto;
	}

	public void setValorArticuloAuto(HashMap<Integer, BigDecimal> valorArticuloAuto) {
		this.valorArticuloAuto = valorArticuloAuto;
	}

	public HashMap<Integer, BigDecimal> getValorArticuloAnul() {
		return valorArticuloAnul;
	}

	public void setValorArticuloAnul(HashMap<Integer, BigDecimal> valorArticuloAnul) {
		this.valorArticuloAnul = valorArticuloAnul;
	}

	public HashMap<String, Integer> getCantidadGrupoAuto() {
		return cantidadGrupoAuto;
	}

	public void setCantidadGrupoAuto(HashMap<String, Integer> cantidadGrupoAuto) {
		this.cantidadGrupoAuto = cantidadGrupoAuto;
	}

	public HashMap<String, BigDecimal> getTarifaGrupoAuto() {
		return tarifaGrupoAuto;
	}

	public void setTarifaGrupoAuto(HashMap<String, BigDecimal> tarifaGrupoAuto) {
		this.tarifaGrupoAuto = tarifaGrupoAuto;
	}

	public HashMap<String, Integer> getCantidadGrupoAnul() {
		return cantidadGrupoAnul;
	}

	public void setCantidadGrupoAnul(HashMap<String, Integer> cantidadGrupoAnul) {
		this.cantidadGrupoAnul = cantidadGrupoAnul;
	}

	public HashMap<String, BigDecimal> getTarifaGrupoAnul() {
		return tarifaGrupoAnul;
	}

	public void setTarifaGrupoAnul(HashMap<String, BigDecimal> tarifaGrupoAnul) {
		this.tarifaGrupoAnul = tarifaGrupoAnul;
	}

	public HashMap<String, Integer> getCantidadClaseAuto() {
		return cantidadClaseAuto;
	}

	public void setCantidadClaseAuto(HashMap<String, Integer> cantidadClaseAuto) {
		this.cantidadClaseAuto = cantidadClaseAuto;
	}

	public HashMap<String, BigDecimal> getTarifaClaseAuto() {
		return tarifaClaseAuto;
	}

	public void setTarifaClaseAuto(HashMap<String, BigDecimal> tarifaClaseAuto) {
		this.tarifaClaseAuto = tarifaClaseAuto;
	}

	public HashMap<String, Integer> getCantidadClaseAnul() {
		return cantidadClaseAnul;
	}

	public void setCantidadClaseAnul(HashMap<String, Integer> cantidadClaseAnul) {
		this.cantidadClaseAnul = cantidadClaseAnul;
	}

	public HashMap<String, BigDecimal> getTarifaClaseAnul() {
		return tarifaClaseAnul;
	}

	public void setTarifaClaseAnul(HashMap<String, BigDecimal> tarifaClaseAnul) {
		this.tarifaClaseAnul = tarifaClaseAnul;
	}

	public HashMap<String, Integer> getCantidadServiciosAuto() {
		return cantidadServiciosAuto;
	}

	public void setCantidadServiciosAuto(
			HashMap<String, Integer> cantidadServiciosAuto) {
		this.cantidadServiciosAuto = cantidadServiciosAuto;
	}

	public HashMap<String, BigDecimal> getTarifaServiciosAuto() {
		return tarifaServiciosAuto;
	}

	public void setTarifaServiciosAuto(
			HashMap<String, BigDecimal> tarifaServiciosAuto) {
		this.tarifaServiciosAuto = tarifaServiciosAuto;
	}

	public HashMap<String, Integer> getCantidadServiciosAnul() {
		return cantidadServiciosAnul;
	}

	public void setCantidadServiciosAnul(
			HashMap<String, Integer> cantidadServiciosAnul) {
		this.cantidadServiciosAnul = cantidadServiciosAnul;
	}

	public HashMap<String, BigDecimal> getTarifaServiciosAnul() {
		return tarifaServiciosAnul;
	}

	public void setTarifaServiciosAnul(
			HashMap<String, BigDecimal> tarifaServiciosAnul) {
		this.tarifaServiciosAnul = tarifaServiciosAnul;
	}

	public HashMap<String, Integer> getCantidadArticulosAuto() {
		return cantidadArticulosAuto;
	}

	public void setCantidadArticulosAuto(
			HashMap<String, Integer> cantidadArticulosAuto) {
		this.cantidadArticulosAuto = cantidadArticulosAuto;
	}

	public HashMap<String, BigDecimal> getTarifaArticulosAuto() {
		return tarifaArticulosAuto;
	}

	public void setTarifaArticulosAuto(
			HashMap<String, BigDecimal> tarifaArticulosAuto) {
		this.tarifaArticulosAuto = tarifaArticulosAuto;
	}

	public HashMap<String, Integer> getCantidadArticulosAnul() {
		return cantidadArticulosAnul;
	}

	public void setCantidadArticulosAnul(
			HashMap<String, Integer> cantidadArticulosAnul) {
		this.cantidadArticulosAnul = cantidadArticulosAnul;
	}

	public HashMap<String, BigDecimal> getTarifaArticulosAnul() {
		return tarifaArticulosAnul;
	}

	public void setTarifaArticulosAnul(
			HashMap<String, BigDecimal> tarifaArticulosAnul) {
		this.tarifaArticulosAnul = tarifaArticulosAnul;
	}

	public void setContadorServicios(Integer contadorServicios) {
		this.contadorServicios = contadorServicios;
	}

	public Integer getContadorServicios() {
		return contadorServicios;
	}

	public void setContadorArticulos(Integer contadorArticulos) {
		this.contadorArticulos = contadorArticulos;
	}

	public Integer getContadorArticulos() {
		return contadorArticulos;
	}

	public void setContadorAutorizaciones(Integer contadorAutorizaciones) {
		this.contadorAutorizaciones = contadorAutorizaciones;
	}

	public Integer getContadorAutorizaciones() {
		return contadorAutorizaciones;
	}

	
	
}
