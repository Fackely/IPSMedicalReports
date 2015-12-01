package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoResultadoTotalesOrdenesAutorizAEntidadesSub implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;

	/** * C�digo de la Entidad Subcontratada */
	private Integer codigoEntidadSub;
	
	/** * Nombre de la Entidad Subcontratada */
	private String nombreEntidadSub;
	
	private String nivelAtencion;
	
	private Integer cantidadServiciosAutorizados;
	
	private Integer cantidadMedicamentosAutorizados;
	
	private Double valorServiciosAutorizados;
	
	private Double valorMedicamentosAutorizados;
	
	private Integer cantidadServiciosAnulados;
	
	private Integer cantidadMedicamentosAnulados;
	
	private Double valorServiciosAnulados;
	
	private Double valorMedicamentosAnulados;
	
	private String nombreConvenio;
	
	private Integer codigoConvenio;
	
	private String estadoAutorizacion;
	
	private Integer cantidad;
	
	private Double valor;
	
	private String grupoServicios;
	
	private String claseInventarios;
	
	private String nombreServicio;
	
	private String nombreMedicamento;
	
	/** * Lista por Convenios */
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaPorNivelesAtencion;
	
	/** * Objeto jasper para el subreporte del consolidado por convenios */
	private JRDataSource dsTotalNivelesAtencion; 
	
	/** * Lista por Convenios */
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaConvenios;
	
	/** * Objeto jasper para el subreporte del consolidado por convenios */
	private JRDataSource dsTotalConvenios; 
	
	//----------------------------------------------
	
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaTotalesServicios;
	
	private JRDataSource dsTotalesServicios;
	
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaTotalesMedicamentos;
	
	private JRDataSource dsTotalesMedicamentos;
	
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaTotales;
	
	private JRDataSource dsTotales;
	
	//----------------------------------------------
	
	/** * Lista por Convenios */
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaPorConvenios;
	/** * Lista de Valores y Cantidades */
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaValoresCantidadesServicios;
	/** * Lista de Valores y Cantidades e*/
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaValoresCantidadesMedicamentos;
	
	
	//*************************PARA SEGUNDO REPORTE***********
	private String grupoServicio;
	
	private String claseInventario;
	
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaGrupoServicio;
	
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaClaseInventario;
	
	private ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaValores;
	
	
	//***********************************************************
	
	
	//----------------pruebas
	
	
	
	/**
	 * M�todo constructor de la clase
	 */
	public DtoResultadoTotalesOrdenesAutorizAEntidadesSub() {
		this.reset();
	}
	
	/**
	 * M�todo que inicializa los atributos de la clase
	 */
	private void reset()
	{
		this.codigoEntidadSub					= null;
		this.nombreEntidadSub					= "";
		this.listaConvenios						= new ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub>();
		this.listaPorNivelesAtencion			= new ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub>();
		this.nivelAtencion						= "";
		this.cantidadServiciosAutorizados		= null;
		this.cantidadMedicamentosAutorizados	= null;
		this.valorServiciosAutorizados			= null;
		this.valorMedicamentosAutorizados		= null;
		this.cantidadServiciosAnulados			= null;
		this.cantidadMedicamentosAnulados		= null;
		this.valorServiciosAnulados				= null;
		this.valorMedicamentosAnulados			= null;
		this.nombreConvenio						= "";
		this.codigoConvenio						= null;
	
	}
	
	/**
	 * M�todo que retorna el valor del atributo codigoEntidadSub
	 * @return codigoEntidadSub
	 * @author Fabi�n Becerra
	 */
	public Integer getCodigoEntidadSub() {
		return codigoEntidadSub;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoEntidadSub
	 * @param codigoEntidadSub valor que se va a almacenar en el atributo codigoEntidadSub
	 * @author Fabi�n Becerra
	 */
	public void setCodigoEntidadSub(Integer codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}
	/**
	 * M�todo que retorna el valor del atributo nombreEntidadSub
	 * @return nombreEntidadSub
	 * @author Fabi�n Becerra
	 */
	public String getNombreEntidadSub() {
		return nombreEntidadSub;
	}
	/**
	 * M�todo que se encarga de establecer el valor del atributo nombreEntidadSub
	 * @param nombreEntidadSub valor que se va a almacenar en el atributo nombreEntidadSub
	 * @author Fabi�n Becerra
	 */
	public void setNombreEntidadSub(String nombreEntidadSub) {
		this.nombreEntidadSub = nombreEntidadSub;
	}

	/**
	 * M�todo que retorna el valor del atributo listaConvenios
	 * @return listaConvenios
	 * @author Fabi�n Becerra
	 */
	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaConvenios() {
		return listaConvenios;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo listaConvenios
	 * @param listaConvenios valor que se va a almacenar en el atributo listaConvenios
	 * @author Fabi�n Becerra
	 */
	public void setListaConvenios(
			ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	/**
	 * M�todo que retorna el valor del atributo dsTotalConvenios
	 * @return dsTotalConvenios
	 * @author Fabi�n Becerra
	 */
	public JRDataSource getDsTotalConvenios() {
		return dsTotalConvenios;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo dsTotalConvenios
	 * @param dsTotalConvenios valor que se va a almacenar en el atributo dsTotalConvenios
	 * @author Fabi�n Becerra
	 */
	public void setDsTotalConvenios(JRDataSource dsTotalConvenios) {
		this.dsTotalConvenios = dsTotalConvenios;
	}

	/**
	 * M�todo que retorna el valor del atributo nivelAtencion
	 * @return nivelAtencion
	 * @author Fabi�n Becerra
	 */
	public String getNivelAtencion() {
		return nivelAtencion;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nivelAtencion
	 * @param nivelAtencion valor que se va a almacenar en el atributo nivelAtencion
	 * @author Fabi�n Becerra
	 */
	public void setNivelAtencion(String nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadServiciosAutorizados
	 * @return cantidadServiciosAutorizados
	 * @author Fabi�n Becerra
	 */
	public Integer getCantidadServiciosAutorizados() {
		return cantidadServiciosAutorizados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadServiciosAutorizados
	 * @param cantidadServiciosAutorizados valor que se va a almacenar en el atributo cantidadServiciosAutorizados
	 * @author Fabi�n Becerra
	 */
	public void setCantidadServiciosAutorizados(Integer cantidadServiciosAutorizados) {
		this.cantidadServiciosAutorizados = cantidadServiciosAutorizados;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadMedicamentosAutorizados
	 * @return cantidadMedicamentosAutorizados
	 * @author Fabi�n Becerra
	 */
	public Integer getCantidadMedicamentosAutorizados() {
		return cantidadMedicamentosAutorizados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadMedicamentosAutorizados
	 * @param cantidadMedicamentosAutorizados valor que se va a almacenar en el atributo cantidadMedicamentosAutorizados
	 * @author Fabi�n Becerra
	 */
	public void setCantidadMedicamentosAutorizados(
			Integer cantidadMedicamentosAutorizados) {
		this.cantidadMedicamentosAutorizados = cantidadMedicamentosAutorizados;
	}

	/**
	 * M�todo que retorna el valor del atributo valorServiciosAutorizados
	 * @return valorServiciosAutorizados
	 * @author Fabi�n Becerra
	 */
	public Double getValorServiciosAutorizados() {
		return valorServiciosAutorizados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo valorServiciosAutorizados
	 * @param valorServiciosAutorizados valor que se va a almacenar en el atributo valorServiciosAutorizados
	 * @author Fabi�n Becerra
	 */
	public void setValorServiciosAutorizados(Double valorServiciosAutorizados) {
		this.valorServiciosAutorizados = valorServiciosAutorizados;
	}

	/**
	 * M�todo que retorna el valor del atributo valorMedicamentosAutorizados
	 * @return valorMedicamentosAutorizados
	 * @author Fabi�n Becerra
	 */
	public Double getValorMedicamentosAutorizados() {
		return valorMedicamentosAutorizados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo valorMedicamentosAutorizados
	 * @param valorMedicamentosAutorizados valor que se va a almacenar en el atributo valorMedicamentosAutorizados
	 * @author Fabi�n Becerra
	 */
	public void setValorMedicamentosAutorizados(Double valorMedicamentosAutorizados) {
		this.valorMedicamentosAutorizados = valorMedicamentosAutorizados;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadServiciosAnulados
	 * @return cantidadServiciosAnulados
	 * @author Fabi�n Becerra
	 */
	public Integer getCantidadServiciosAnulados() {
		return cantidadServiciosAnulados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadServiciosAnulados
	 * @param cantidadServiciosAnulados valor que se va a almacenar en el atributo cantidadServiciosAnulados
	 * @author Fabi�n Becerra
	 */
	public void setCantidadServiciosAnulados(Integer cantidadServiciosAnulados) {
		this.cantidadServiciosAnulados = cantidadServiciosAnulados;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidadMedicamentosAnulados
	 * @return cantidadMedicamentosAnulados
	 * @author Fabi�n Becerra
	 */
	public Integer getCantidadMedicamentosAnulados() {
		return cantidadMedicamentosAnulados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidadMedicamentosAnulados
	 * @param cantidadMedicamentosAnulados valor que se va a almacenar en el atributo cantidadMedicamentosAnulados
	 * @author Fabi�n Becerra
	 */
	public void setCantidadMedicamentosAnulados(Integer cantidadMedicamentosAnulados) {
		this.cantidadMedicamentosAnulados = cantidadMedicamentosAnulados;
	}

	/**
	 * M�todo que retorna el valor del atributo valorServiciosAnulados
	 * @return valorServiciosAnulados
	 * @author Fabi�n Becerra
	 */
	public Double getValorServiciosAnulados() {
		return valorServiciosAnulados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo valorServiciosAnulados
	 * @param valorServiciosAnulados valor que se va a almacenar en el atributo valorServiciosAnulados
	 * @author Fabi�n Becerra
	 */
	public void setValorServiciosAnulados(Double valorServiciosAnulados) {
		this.valorServiciosAnulados = valorServiciosAnulados;
	}

	/**
	 * M�todo que retorna el valor del atributo valorMedicamentosAnulados
	 * @return valorMedicamentosAnulados
	 * @author Fabi�n Becerra
	 */
	public Double getValorMedicamentosAnulados() {
		return valorMedicamentosAnulados;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo valorMedicamentosAnulados
	 * @param valorMedicamentosAnulados valor que se va a almacenar en el atributo valorMedicamentosAnulados
	 * @author Fabi�n Becerra
	 */
	public void setValorMedicamentosAnulados(Double valorMedicamentosAnulados) {
		this.valorMedicamentosAnulados = valorMedicamentosAnulados;
	}

	/**
	 * M�todo que retorna el valor del atributo nombreConvenio
	 * @return nombreConvenio
	 * @author Fabi�n Becerra
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo nombreConvenio
	 * @param nombreConvenio valor que se va a almacenar en el atributo nombreConvenio
	 * @author Fabi�n Becerra
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * M�todo que retorna el valor del atributo codigoConvenio
	 * @return codigoConvenio
	 * @author Fabi�n Becerra
	 */
	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo codigoConvenio
	 * @param codigoConvenio valor que se va a almacenar en el atributo codigoConvenio
	 * @author Fabi�n Becerra
	 */
	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * M�todo que retorna el valor del atributo listaPorNivelesAtencion
	 * @return listaPorNivelesAtencion
	 * @author Fabi�n Becerra
	 */
	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaPorNivelesAtencion() {
		return listaPorNivelesAtencion;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo listaPorNivelesAtencion
	 * @param listaPorNivelesAtencion valor que se va a almacenar en el atributo listaPorNivelesAtencion
	 * @author Fabi�n Becerra
	 */
	public void setListaPorNivelesAtencion(
			ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaPorNivelesAtencion) {
		this.listaPorNivelesAtencion = listaPorNivelesAtencion;
	}

	/**
	 * M�todo que retorna el valor del atributo dsTotalNivelesAtencion
	 * @return dsTotalNivelesAtencion
	 * @author Fabi�n Becerra
	 */
	public JRDataSource getDsTotalNivelesAtencion() {
		return dsTotalNivelesAtencion;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo dsTotalNivelesAtencion
	 * @param dsTotalNivelesAtencion valor que se va a almacenar en el atributo dsTotalNivelesAtencion
	 * @author Fabi�n Becerra
	 */
	public void setDsTotalNivelesAtencion(JRDataSource dsTotalNivelesAtencion) {
		this.dsTotalNivelesAtencion = dsTotalNivelesAtencion;
	}

	
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}

	/**
	 * M�todo que retorna el valor del atributo cantidad
	 * @return cantidad
	 * @author Fabi�n Becerra
	 */
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo cantidad
	 * @param cantidad valor que se va a almacenar en el atributo cantidad
	 * @author Fabi�n Becerra
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * M�todo que retorna el valor del atributo valor
	 * @return valor
	 * @author Fabi�n Becerra
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * M�todo que se encarga de establecer el valor del atributo valor
	 * @param valor valor que se va a almacenar en el atributo valor
	 * @author Fabi�n Becerra
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}

	public void setListaTotalesServicios(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaTotalesServicios) {
		this.listaTotalesServicios = listaTotalesServicios;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaTotalesServicios() {
		return listaTotalesServicios;
	}

	public void setListaTotalesMedicamentos(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaTotalesMedicamentos) {
		this.listaTotalesMedicamentos = listaTotalesMedicamentos;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaTotalesMedicamentos() {
		return listaTotalesMedicamentos;
	}

	public void setDsTotalesServicios(JRDataSource dsTotalesServicios) {
		this.dsTotalesServicios = dsTotalesServicios;
	}

	public JRDataSource getDsTotalesServicios() {
		return dsTotalesServicios;
	}

	public void setDsTotalesMedicamentos(JRDataSource dsTotalesMedicamentos) {
		this.dsTotalesMedicamentos = dsTotalesMedicamentos;
	}

	public JRDataSource getDsTotalesMedicamentos() {
		return dsTotalesMedicamentos;
	}

	public void setGrupoServicios(String grupoServicios) {
		this.grupoServicios = grupoServicios;
	}

	public String getGrupoServicios() {
		return grupoServicios;
	}

	public void setClaseInventarios(String claseInventarios) {
		this.claseInventarios = claseInventarios;
	}

	public String getClaseInventarios() {
		return claseInventarios;
	}

	public void setListaTotales(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaTotales) {
		this.listaTotales = listaTotales;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaTotales() {
		return listaTotales;
	}

	public void setDsTotales(JRDataSource dsTotales) {
		this.dsTotales = dsTotales;
	}

	public JRDataSource getDsTotales() {
		return dsTotales;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreMedicamento(String nombreMedicamento) {
		this.nombreMedicamento = nombreMedicamento;
	}

	public String getNombreMedicamento() {
		return nombreMedicamento;
	}

	public void setListaPorConvenios(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaPorConvenios) {
		this.listaPorConvenios = listaPorConvenios;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaPorConvenios() {
		return listaPorConvenios;
	}

	public void setListaValoresCantidadesServicios(
			ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaValoresCantidadesServicios) {
		this.listaValoresCantidadesServicios = listaValoresCantidadesServicios;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaValoresCantidadesServicios() {
		return listaValoresCantidadesServicios;
	}

	public void setListaValoresCantidadesMedicamentos(
			ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaValoresCantidadesMedicamentos) {
		this.listaValoresCantidadesMedicamentos = listaValoresCantidadesMedicamentos;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaValoresCantidadesMedicamentos() {
		return listaValoresCantidadesMedicamentos;
	}

	public void setGrupoServicio(String grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	public String getGrupoServicio() {
		return grupoServicio;
	}

	public void setClaseInventario(String claseInventario) {
		this.claseInventario = claseInventario;
	}

	public String getClaseInventario() {
		return claseInventario;
	}

	public void setListaGrupoServicio(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaGrupoServicio) {
		this.listaGrupoServicio = listaGrupoServicio;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaGrupoServicio() {
		return listaGrupoServicio;
	}

	public void setListaClaseInventario(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaClaseInventario) {
		this.listaClaseInventario = listaClaseInventario;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaClaseInventario() {
		return listaClaseInventario;
	}

	public void setListaValores(ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaValores) {
		this.listaValores = listaValores;
	}

	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaValores() {
		return listaValores;
	}
	
	
	
	
}
