package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoResultadoTotalesOrdenesAutorizAEntidadesSub implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;

	/** * Código de la Entidad Subcontratada */
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
	 * Método constructor de la clase
	 */
	public DtoResultadoTotalesOrdenesAutorizAEntidadesSub() {
		this.reset();
	}
	
	/**
	 * Método que inicializa los atributos de la clase
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
	 * Método que retorna el valor del atributo codigoEntidadSub
	 * @return codigoEntidadSub
	 * @author Fabián Becerra
	 */
	public Integer getCodigoEntidadSub() {
		return codigoEntidadSub;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo codigoEntidadSub
	 * @param codigoEntidadSub valor que se va a almacenar en el atributo codigoEntidadSub
	 * @author Fabián Becerra
	 */
	public void setCodigoEntidadSub(Integer codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}
	/**
	 * Método que retorna el valor del atributo nombreEntidadSub
	 * @return nombreEntidadSub
	 * @author Fabián Becerra
	 */
	public String getNombreEntidadSub() {
		return nombreEntidadSub;
	}
	/**
	 * Método que se encarga de establecer el valor del atributo nombreEntidadSub
	 * @param nombreEntidadSub valor que se va a almacenar en el atributo nombreEntidadSub
	 * @author Fabián Becerra
	 */
	public void setNombreEntidadSub(String nombreEntidadSub) {
		this.nombreEntidadSub = nombreEntidadSub;
	}

	/**
	 * Método que retorna el valor del atributo listaConvenios
	 * @return listaConvenios
	 * @author Fabián Becerra
	 */
	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaConvenios() {
		return listaConvenios;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo listaConvenios
	 * @param listaConvenios valor que se va a almacenar en el atributo listaConvenios
	 * @author Fabián Becerra
	 */
	public void setListaConvenios(
			ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	/**
	 * Método que retorna el valor del atributo dsTotalConvenios
	 * @return dsTotalConvenios
	 * @author Fabián Becerra
	 */
	public JRDataSource getDsTotalConvenios() {
		return dsTotalConvenios;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo dsTotalConvenios
	 * @param dsTotalConvenios valor que se va a almacenar en el atributo dsTotalConvenios
	 * @author Fabián Becerra
	 */
	public void setDsTotalConvenios(JRDataSource dsTotalConvenios) {
		this.dsTotalConvenios = dsTotalConvenios;
	}

	/**
	 * Método que retorna el valor del atributo nivelAtencion
	 * @return nivelAtencion
	 * @author Fabián Becerra
	 */
	public String getNivelAtencion() {
		return nivelAtencion;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nivelAtencion
	 * @param nivelAtencion valor que se va a almacenar en el atributo nivelAtencion
	 * @author Fabián Becerra
	 */
	public void setNivelAtencion(String nivelAtencion) {
		this.nivelAtencion = nivelAtencion;
	}

	/**
	 * Método que retorna el valor del atributo cantidadServiciosAutorizados
	 * @return cantidadServiciosAutorizados
	 * @author Fabián Becerra
	 */
	public Integer getCantidadServiciosAutorizados() {
		return cantidadServiciosAutorizados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo cantidadServiciosAutorizados
	 * @param cantidadServiciosAutorizados valor que se va a almacenar en el atributo cantidadServiciosAutorizados
	 * @author Fabián Becerra
	 */
	public void setCantidadServiciosAutorizados(Integer cantidadServiciosAutorizados) {
		this.cantidadServiciosAutorizados = cantidadServiciosAutorizados;
	}

	/**
	 * Método que retorna el valor del atributo cantidadMedicamentosAutorizados
	 * @return cantidadMedicamentosAutorizados
	 * @author Fabián Becerra
	 */
	public Integer getCantidadMedicamentosAutorizados() {
		return cantidadMedicamentosAutorizados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo cantidadMedicamentosAutorizados
	 * @param cantidadMedicamentosAutorizados valor que se va a almacenar en el atributo cantidadMedicamentosAutorizados
	 * @author Fabián Becerra
	 */
	public void setCantidadMedicamentosAutorizados(
			Integer cantidadMedicamentosAutorizados) {
		this.cantidadMedicamentosAutorizados = cantidadMedicamentosAutorizados;
	}

	/**
	 * Método que retorna el valor del atributo valorServiciosAutorizados
	 * @return valorServiciosAutorizados
	 * @author Fabián Becerra
	 */
	public Double getValorServiciosAutorizados() {
		return valorServiciosAutorizados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo valorServiciosAutorizados
	 * @param valorServiciosAutorizados valor que se va a almacenar en el atributo valorServiciosAutorizados
	 * @author Fabián Becerra
	 */
	public void setValorServiciosAutorizados(Double valorServiciosAutorizados) {
		this.valorServiciosAutorizados = valorServiciosAutorizados;
	}

	/**
	 * Método que retorna el valor del atributo valorMedicamentosAutorizados
	 * @return valorMedicamentosAutorizados
	 * @author Fabián Becerra
	 */
	public Double getValorMedicamentosAutorizados() {
		return valorMedicamentosAutorizados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo valorMedicamentosAutorizados
	 * @param valorMedicamentosAutorizados valor que se va a almacenar en el atributo valorMedicamentosAutorizados
	 * @author Fabián Becerra
	 */
	public void setValorMedicamentosAutorizados(Double valorMedicamentosAutorizados) {
		this.valorMedicamentosAutorizados = valorMedicamentosAutorizados;
	}

	/**
	 * Método que retorna el valor del atributo cantidadServiciosAnulados
	 * @return cantidadServiciosAnulados
	 * @author Fabián Becerra
	 */
	public Integer getCantidadServiciosAnulados() {
		return cantidadServiciosAnulados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo cantidadServiciosAnulados
	 * @param cantidadServiciosAnulados valor que se va a almacenar en el atributo cantidadServiciosAnulados
	 * @author Fabián Becerra
	 */
	public void setCantidadServiciosAnulados(Integer cantidadServiciosAnulados) {
		this.cantidadServiciosAnulados = cantidadServiciosAnulados;
	}

	/**
	 * Método que retorna el valor del atributo cantidadMedicamentosAnulados
	 * @return cantidadMedicamentosAnulados
	 * @author Fabián Becerra
	 */
	public Integer getCantidadMedicamentosAnulados() {
		return cantidadMedicamentosAnulados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo cantidadMedicamentosAnulados
	 * @param cantidadMedicamentosAnulados valor que se va a almacenar en el atributo cantidadMedicamentosAnulados
	 * @author Fabián Becerra
	 */
	public void setCantidadMedicamentosAnulados(Integer cantidadMedicamentosAnulados) {
		this.cantidadMedicamentosAnulados = cantidadMedicamentosAnulados;
	}

	/**
	 * Método que retorna el valor del atributo valorServiciosAnulados
	 * @return valorServiciosAnulados
	 * @author Fabián Becerra
	 */
	public Double getValorServiciosAnulados() {
		return valorServiciosAnulados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo valorServiciosAnulados
	 * @param valorServiciosAnulados valor que se va a almacenar en el atributo valorServiciosAnulados
	 * @author Fabián Becerra
	 */
	public void setValorServiciosAnulados(Double valorServiciosAnulados) {
		this.valorServiciosAnulados = valorServiciosAnulados;
	}

	/**
	 * Método que retorna el valor del atributo valorMedicamentosAnulados
	 * @return valorMedicamentosAnulados
	 * @author Fabián Becerra
	 */
	public Double getValorMedicamentosAnulados() {
		return valorMedicamentosAnulados;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo valorMedicamentosAnulados
	 * @param valorMedicamentosAnulados valor que se va a almacenar en el atributo valorMedicamentosAnulados
	 * @author Fabián Becerra
	 */
	public void setValorMedicamentosAnulados(Double valorMedicamentosAnulados) {
		this.valorMedicamentosAnulados = valorMedicamentosAnulados;
	}

	/**
	 * Método que retorna el valor del atributo nombreConvenio
	 * @return nombreConvenio
	 * @author Fabián Becerra
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo nombreConvenio
	 * @param nombreConvenio valor que se va a almacenar en el atributo nombreConvenio
	 * @author Fabián Becerra
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * Método que retorna el valor del atributo codigoConvenio
	 * @return codigoConvenio
	 * @author Fabián Becerra
	 */
	public Integer getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo codigoConvenio
	 * @param codigoConvenio valor que se va a almacenar en el atributo codigoConvenio
	 * @author Fabián Becerra
	 */
	public void setCodigoConvenio(Integer codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * Método que retorna el valor del atributo listaPorNivelesAtencion
	 * @return listaPorNivelesAtencion
	 * @author Fabián Becerra
	 */
	public ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> getListaPorNivelesAtencion() {
		return listaPorNivelesAtencion;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo listaPorNivelesAtencion
	 * @param listaPorNivelesAtencion valor que se va a almacenar en el atributo listaPorNivelesAtencion
	 * @author Fabián Becerra
	 */
	public void setListaPorNivelesAtencion(
			ArrayList<DtoResultadoTotalesOrdenesAutorizAEntidadesSub> listaPorNivelesAtencion) {
		this.listaPorNivelesAtencion = listaPorNivelesAtencion;
	}

	/**
	 * Método que retorna el valor del atributo dsTotalNivelesAtencion
	 * @return dsTotalNivelesAtencion
	 * @author Fabián Becerra
	 */
	public JRDataSource getDsTotalNivelesAtencion() {
		return dsTotalNivelesAtencion;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo dsTotalNivelesAtencion
	 * @param dsTotalNivelesAtencion valor que se va a almacenar en el atributo dsTotalNivelesAtencion
	 * @author Fabián Becerra
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
	 * Método que retorna el valor del atributo cantidad
	 * @return cantidad
	 * @author Fabián Becerra
	 */
	public Integer getCantidad() {
		return cantidad;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo cantidad
	 * @param cantidad valor que se va a almacenar en el atributo cantidad
	 * @author Fabián Becerra
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * Método que retorna el valor del atributo valor
	 * @return valor
	 * @author Fabián Becerra
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * Método que se encarga de establecer el valor del atributo valor
	 * @param valor valor que se va a almacenar en el atributo valor
	 * @author Fabián Becerra
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
