package com.servinte.axioma.actionForm.consultaExterna;


import java.util.ArrayList;
import java.util.List;
import org.apache.struts.validator.ValidatorForm;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.ValorEstandarOrdenadoresDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;



public class ParametrizacionValorEstandarOrdenadoresForm extends ValidatorForm{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2413207571081275264L;

	private List<ValorEstandarOrdenadoresDto> listaOrdenadores = new ArrayList<ValorEstandarOrdenadoresDto>();
	
	private ValorEstandarOrdenadoresDto valorEstandarOrdenadoresDto=new ValorEstandarOrdenadoresDto(); 
	
	private ValorEstandarOrdenadoresDto ordenSeleccionada=new ValorEstandarOrdenadoresDto();
	
	private String estado="";
	
	private int tipoOrden;

	private String valEstOrdCita;
	
	private String valEstSermedOrden;
	
	private String valEstSermedCita;
	
	private List<ValorEstandarOrdenadoresDto> listaOrdenes;

	private List<GrupoServicioDto> gruposServiciosDto=new ArrayList<GrupoServicioDto>(0);
	
	private List<ClaseInventarioDto> clasesInventariosDto=new ArrayList<ClaseInventarioDto>(0);
	
	private List<DtoUnidadesConsulta> unidadAgendaDto =new ArrayList<DtoUnidadesConsulta>(0);
	
	private boolean mostrarSeccionCaptura;
	
	private boolean mensajeIngresoExitoso;
	
	private boolean mostrarSeccionModificar;
	
	private String posicionOrden;
	
	private String posicionEliminar;
	

	public List<ValorEstandarOrdenadoresDto> getListaOrdenadores() {
		return listaOrdenadores;
	}


	public void setListaOrdenadores(
			List<ValorEstandarOrdenadoresDto> listaOrdenadores) {
		this.listaOrdenadores = listaOrdenadores;
	}


	public ValorEstandarOrdenadoresDto getValorEstandarOrdenadoresDto() {
		return valorEstandarOrdenadoresDto;
	}


	public void setValorEstandarOrdenadoresDto(
			ValorEstandarOrdenadoresDto valorEstandarOrdenadoresDto) {
		this.valorEstandarOrdenadoresDto = valorEstandarOrdenadoresDto;
	}


	public int getTipoOrden() {
		return tipoOrden;
	}


	public void setTipoOrden(int tipoOrden) {
		this.tipoOrden = tipoOrden;
	}


	public String getValEstOrdCita() {
		return valEstOrdCita;
	}


	public void setValEstOrdCita(String valEstOrdCita) {
		this.valEstOrdCita = valEstOrdCita;
	}


	public String getValEstSermedOrden() {
		return valEstSermedOrden;
	}


	public void setValEstSermedOrden(String valEstSermedOrden) {
		this.valEstSermedOrden = valEstSermedOrden;
	}


	public String getValEstSermedCita() {
		return valEstSermedCita;
	}


	public void setValEstSermedCita(String valEstSermedCita) {
		this.valEstSermedCita = valEstSermedCita;
	}


	public List<ValorEstandarOrdenadoresDto> getListaOrdenes() {
		return listaOrdenes;
	}


	public void setListaOrdenes(List<ValorEstandarOrdenadoresDto> listaOrdenes) {
		this.listaOrdenes = listaOrdenes;
	}


	public List<GrupoServicioDto> getGruposServiciosDto() {
		return gruposServiciosDto;
	}


	public void setGruposServiciosDto(List<GrupoServicioDto> gruposServiciosDto) {
		this.gruposServiciosDto = gruposServiciosDto;
	}


	public List<DtoUnidadesConsulta> getUnidadAgendaDto() {
		return unidadAgendaDto;
	}


	public void setUnidadAgendaDto(List<DtoUnidadesConsulta> unidadAgendaDto) {
		this.unidadAgendaDto = unidadAgendaDto;
	}


	public boolean isMostrarSeccionCaptura() {
		return mostrarSeccionCaptura;
	}


	public void setMostrarSeccionCaptura(boolean mostrarSeccionCaptura) {
		this.mostrarSeccionCaptura = mostrarSeccionCaptura;
	}


	public List<ClaseInventarioDto> getClasesInventariosDto() {
		return clasesInventariosDto;
	}


	public void setClasesInventariosDto(
			List<ClaseInventarioDto> clasesInventariosDto) {
		this.clasesInventariosDto = clasesInventariosDto;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public boolean isMensajeIngresoExitoso() {
		return mensajeIngresoExitoso;
	}


	public void setMensajeIngresoExitoso(boolean mensajeIngresoExitoso) {
		this.mensajeIngresoExitoso = mensajeIngresoExitoso;
	}


	public boolean isMostrarSeccionModificar() {
		return mostrarSeccionModificar;
	}


	public void setMostrarSeccionModificar(boolean mostrarSeccionModificar) {
		this.mostrarSeccionModificar = mostrarSeccionModificar;
	}


	public String getPosicionOrden() {
		return posicionOrden;
	}


	public void setPosicionOrden(String posicionOrden) {
		this.posicionOrden = posicionOrden;
	}


	public ValorEstandarOrdenadoresDto getOrdenSeleccionada() {
		return ordenSeleccionada;
	}


	public void setOrdenSeleccionada(ValorEstandarOrdenadoresDto ordenSeleccionada) {
		this.ordenSeleccionada = ordenSeleccionada;
	}


	public String getPosicionEliminar() {
		return posicionEliminar;
	}


	public void setPosicionEliminar(String posicionEliminar) {
		this.posicionEliminar = posicionEliminar;
	}
	
	
}
	
