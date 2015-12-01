package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

public class DtoHonorariosMedico implements Serializable,Comparable<DtoHonorariosMedico>{
	
  
	
	private static final long serialVersionUID = -5974377007097433228L;
	
	/**
	 * Fechas entre las cuales se hizo la busqueda
	 */
	private String fechaInicial;
	private String fechaFinal;
	
	/**
	 * Informacion de todas las facturas de un profesional
	 */
	private String nombreProfesional;
	private String programaOdonto;
	private Character hayPrograma;
	private String factura;
	private String ordenMedica;
	private String identificacionPac;
	private String paciente;
	private String servicio;
	private String convenio;
	private String fechaGeneracion;
	private String centroAtencion;
	private String valorFacturado;
	private String valorHonorario;
	private String valorFacturadoTotal;
	private String valorHonorarioTotal;
	private String valorHonorarioATotal;
	private String valorFacturadoATotal;
	private int esAnulado=0;
	private int esFacturado=0;
	
	/**
	 * @return the esFacturado
	 */
	public int getEsFacturado() {
		return esFacturado;
	}



	/**
	 * @param esFacturado the esFacturado to set
	 */
	public void setEsFacturado(int esFacturado) {
		this.esFacturado = esFacturado;
	}

	
    /**
	 * @return the valorHonorarioATotal
	 */
	public String getValorHonorarioATotal() {
		return valorHonorarioATotal;
	}



	/**
	 * @param valorHonorarioATotal the valorHonorarioATotal to set
	 */
	public void setValorHonorarioATotal(String valorHonorarioATotal) {
		this.valorHonorarioATotal = valorHonorarioATotal;
	}



	/**
	 * @return the valorFacturadoATotal
	 */
	public String getValorFacturadoATotal() {
		return valorFacturadoATotal;
	}



	/**
	 * @param valorFacturadoATotal the valorFacturadoATotal to set
	 */
	public void setValorFacturadoATotal(String valorFacturadoATotal) {
		this.valorFacturadoATotal = valorFacturadoATotal;
	}

	
	/**
	 * Informacion para caundo es un resumen
	 */

	private int esResumen=0;
	private String nombreProfesionalRes;
	private String valorFacturadoRes;
	private String valorFacturadoARes;
	private String valorHonorarioRes;
	private String valorHonorarioARes;
	private String valorNetFacturado;
	private String valorNetHonorario;
	private String valorFacturadoResTotal;
	private String valorFacturadoAResTotal;
	private String valorHonorarioResTotal;
	private String valorHonorarioAResTotal;
	private String valorNetFacturadoTotal;
	private String valorNetHonorarioTotal;
	
	
	/**
	 * Listas con los datos
	 */
	private ArrayList<DtoHonorariosMedico> listaProfesionales;
		private ArrayList<DtoHonorariosMedico> listaFacturas;
		private ArrayList<DtoHonorariosMedico> listaFacturasAnuladas;
		private ArrayList<DtoHonorariosMedico> listaResumenPool;
	
	/**
	 * DataSource para la impresion
	 */
	
	private JRDataSource JRDDtoHonorariosMedico;
		
	private JRDataSource JRDListaProfesionales;
		private JRDataSource JRDListaFacturas;
		private JRDataSource JRDListaFacturasAnuladas;
		private JRDataSource JRDListaResumenPool;
		
		
/** 
 * para cuando es archivo plano
 * @return
 */
		
	public String tituloProfesional;
	public String tituloFacturas;
	public String tituloAnuladas;
	public String tituloResumen;
	public String fechas;
	
	public String lineaFacturas;
	public String lineaAnuladas;
	public String lineasResumen;
	
	public String totalesFactura;
	public String totalesAnuladas;
	public String totalesResumen;
		
	public ArrayList<DtoHonorariosMedico> getListaProfesionales() {
			return listaProfesionales;
		}



		public void setListaProfesionales(
				ArrayList<DtoHonorariosMedico> listaProfesionales) {
			this.listaProfesionales = listaProfesionales;
		}



		public ArrayList<DtoHonorariosMedico> getListaFacturas() {
			return listaFacturas;
		}



		public void setListaFacturas(ArrayList<DtoHonorariosMedico> listaFacturas) {
			this.listaFacturas = listaFacturas;
		}



		public ArrayList<DtoHonorariosMedico> getListaFacturasAnuladas() {
			return listaFacturasAnuladas;
		}



		public void setListaFacturasAnuladas(
				ArrayList<DtoHonorariosMedico> listaFacturasAnuladas) {
			this.listaFacturasAnuladas = listaFacturasAnuladas;
		}
	


		public ArrayList<DtoHonorariosMedico> getListaResumenPool() {
			return listaResumenPool;
		}



		public void setListaResumenPool(ArrayList<DtoHonorariosMedico> listaResumenPool) {
			this.listaResumenPool = listaResumenPool;
		}



		public JRDataSource getJRDDtoHonorariosMedico() {
			return JRDDtoHonorariosMedico;
		}


		
		public void setJRDDtoHonorariosMedico(JRDataSource jRDDtoHonorariosMedico) {
			JRDDtoHonorariosMedico = jRDDtoHonorariosMedico;
		}



		public JRDataSource getJRDListaProfesionales() {
			return JRDListaProfesionales;
		}



		public void setJRDListaProfesionales(JRDataSource jRDListaProfesionales) {
			JRDListaProfesionales = jRDListaProfesionales;
		}



		public JRDataSource getJRDListaFacturas() {
			return JRDListaFacturas;
		}



		public void setJRDListaFacturas(JRDataSource jRDListaFacturas) {
			JRDListaFacturas = jRDListaFacturas;
		}



		public JRDataSource getJRDListaFacturasAnuladas() {
			return JRDListaFacturasAnuladas;
		}



		public void setJRDListaFacturasAnuladas(JRDataSource jRDListaFacturasAnuladas) {
			JRDListaFacturasAnuladas = jRDListaFacturasAnuladas;
		}



		public JRDataSource getJRDListaResumenPool() {
			return JRDListaResumenPool;
		}



		public void setJRDListaResumenPool(JRDataSource jRDListaResumenPool) {
			JRDListaResumenPool = jRDListaResumenPool;
		}



		   public int getEsAnulado() {
				return esAnulado;
			}



			public void setEsAnulado(int esAnulado) {
				this.esAnulado = esAnulado;
			}
			
	public String getFechaInicial() {
		return fechaInicial;
	}



	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}



	public String getFechaFinal() {
		return fechaFinal;
	}



	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}



	public String getNombreProfesional() {
		return nombreProfesional;
	}



	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}



	public String getProgramaOdonto() {
		return programaOdonto;
	}



	public void setProgramaOdonto(String programaOdonto) {
		this.programaOdonto = programaOdonto;
	}



	public Character getHayPrograma() {
		return hayPrograma;
	}



	public void setHayPrograma(Character hayPrograma) {
		this.hayPrograma = hayPrograma;
	}



	public String getFactura() {
		return factura;
	}



	public void setFactura(String factura) {
		this.factura = factura;
	}



	public String getOrdenMedica() {
		return ordenMedica;
	}



	public void setOrdenMedica(String ordenMedica) {
		this.ordenMedica = ordenMedica;
	}



	public String getIdentificacionPac() {
		return identificacionPac;
	}



	public void setIdentificacionPac(String identificacionPac) {
		this.identificacionPac = identificacionPac;
	}



	public String getPaciente() {
		return paciente;
	}



	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}



	public String getServicio() {
		return servicio;
	}



	public void setServicio(String servicio) {
		this.servicio = servicio;
	}



	public String getConvenio() {
		return convenio;
	}



	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}



	public String getFechaGeneracion() {
		return fechaGeneracion;
	}



	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}



	public String getCentroAtencion() {
		return centroAtencion;
	}



	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}



	public String getValorFacturado() {
		return valorFacturado;
	}



	public void setValorFacturado(String valorFacturado) {
		this.valorFacturado = valorFacturado;
	}



	public String getValorHonorario() {
		return valorHonorario;
	}



	public void setValorHonorario(String valorHonorario) {
		this.valorHonorario = valorHonorario;
	}



	public String getValorFacturadoTotal() {
		return valorFacturadoTotal;
	}



	public void setValorFacturadoTotal(String valorFacturadoTotal) {
		this.valorFacturadoTotal = valorFacturadoTotal;
	}



	public String getValorHonorarioTotal() {
		return valorHonorarioTotal;
	}



	public void setValorHonorarioTotal(String valorHonorarioTotal) {
		this.valorHonorarioTotal = valorHonorarioTotal;
	}


	public int getEsResumen() {
		return esResumen;
	}



	public void setEsResumen(int esResumen) {
		this.esResumen = esResumen;
	}



	public String getNombreProfesionalRes() {
		return nombreProfesionalRes;
	}



	public void setNombreProfesionalRes(String nombreProfesionalRes) {
		this.nombreProfesionalRes = nombreProfesionalRes;
	}



	public String getValorFacturadoRes() {
		return valorFacturadoRes;
	}



	public void setValorFacturadoRes(String valorFacturadoRes) {
		this.valorFacturadoRes = valorFacturadoRes;
	}



	public String getValorFacturadoARes() {
		return valorFacturadoARes;
	}



	public void setValorFacturadoARes(String valorFacturadoARes) {
		this.valorFacturadoARes = valorFacturadoARes;
	}



	public String getValorHonorarioRes() {
		return valorHonorarioRes;
	}



	public void setValorHonorarioRes(String valorHonorarioRes) {
		this.valorHonorarioRes = valorHonorarioRes;
	}



	public String getValorHonorarioARes() {
		return valorHonorarioARes;
	}



	public void setValorHonorarioARes(String valorHonorarioARes) {
		this.valorHonorarioARes = valorHonorarioARes;
	}



	public String getValorNetFacturado() {
		return valorNetFacturado;
	}



	public void setValorNetFacturado(String valorNetFacturado) {
		this.valorNetFacturado = valorNetFacturado;
	}



	public String getValorNetHonorario() {
		return valorNetHonorario;
	}



	public void setValorNetHonorario(String valorNetHonorario) {
		this.valorNetHonorario = valorNetHonorario;
	}



	public String getValorFacturadoResTotal() {
		return valorFacturadoResTotal;
	}



	public void setValorFacturadoResTotal(String valorFacturadoResTotal) {
		this.valorFacturadoResTotal = valorFacturadoResTotal;
	}



	public String getValorFacturadoAResTotal() {
		return valorFacturadoAResTotal;
	}



	public void setValorFacturadoAResTotal(String valorFacturadoAResTotal) {
		this.valorFacturadoAResTotal = valorFacturadoAResTotal;
	}



	public String getValorHonorarioResTotal() {
		return valorHonorarioResTotal;
	}



	public void setValorHonorarioResTotal(String valorHonorarioResTotal) {
		this.valorHonorarioResTotal = valorHonorarioResTotal;
	}



	public String getValorHonorarioAResTotal() {
		return valorHonorarioAResTotal;
	}



	public void setValorHonorarioAResTotal(String valorHonorarioAResTotal) {
		this.valorHonorarioAResTotal = valorHonorarioAResTotal;
	}



	public String getValorNetFacturadoTotal() {
		return valorNetFacturadoTotal;
	}



	public void setValorNetFacturadoTotal(String valorNetFacturadoTotal) {
		this.valorNetFacturadoTotal = valorNetFacturadoTotal;
	}



	public String getValorNetHonorarioTotal() {
		return valorNetHonorarioTotal;
	}



	public void setValorNetHonorarioTotal(String valorNetHonorarioTotal) {
		this.valorNetHonorarioTotal = valorNetHonorarioTotal;
	}



	/** Metodo constructor de la clase*/
	public DtoHonorariosMedico()
	{
		this.reset();
	}
	
	
	/**
	 * @return the tituloProfesional
	 */
	public String getTituloProfesional() {
		return tituloProfesional;
	}



	/**
	 * @param tituloProfesional the tituloProfesional to set
	 */
	public void setTituloProfesional(String tituloProfesional) {
		this.tituloProfesional = tituloProfesional;
	}



	/**
	 * @return the tituloFacturas
	 */
	public String getTituloFacturas() {
		return tituloFacturas;
	}



	/**
	 * @param tituloFacturas the tituloFacturas to set
	 */
	public void setTituloFacturas(String tituloFacturas) {
		this.tituloFacturas = tituloFacturas;
	}



	/**
	 * @return the tituloAnuladas
	 */
	public String getTituloAnuladas() {
		return tituloAnuladas;
	}



	/**
	 * @param tituloAnuladas the tituloAnuladas to set
	 */
	public void setTituloAnuladas(String tituloAnuladas) {
		this.tituloAnuladas = tituloAnuladas;
	}



	/**
	 * @return the tituloResumen
	 */
	public String getTituloResumen() {
		return tituloResumen;
	}



	/**
	 * @param tituloResumen the tituloResumen to set
	 */
	public void setTituloResumen(String tituloResumen) {
		this.tituloResumen = tituloResumen;
	}



	/**
	 * @return the fechas
	 */
	public String getFechas() {
		return fechas;
	}



	/**
	 * @param fechas the fechas to set
	 */
	public void setFechas(String fechas) {
		this.fechas = fechas;
	}



	/**
	 * @return the lineaFacturas
	 */
	public String getLineaFacturas() {
		return lineaFacturas;
	}



	/**
	 * @param lineaFacturas the lineaFacturas to set
	 */
	public void setLineaFacturas(String lineaFacturas) {
		this.lineaFacturas = lineaFacturas;
	}



	/**
	 * @return the lineaAnuladas
	 */
	public String getLineaAnuladas() {
		return lineaAnuladas;
	}



	/**
	 * @param lineaAnuladas the lineaAnuladas to set
	 */
	public void setLineaAnuladas(String lineaAnuladas) {
		this.lineaAnuladas = lineaAnuladas;
	}



	/**
	 * @return the lineasResumen
	 */
	public String getLineasResumen() {
		return lineasResumen;
	}



	/**
	 * @param lineasResumen the lineasResumen to set
	 */
	public void setLineasResumen(String lineasResumen) {
		this.lineasResumen = lineasResumen;
	}



	/**
	 * @return the totalesFactura
	 */
	public String getTotalesFactura() {
		return totalesFactura;
	}



	/**
	 * @param totalesFactura the totalesFactura to set
	 */
	public void setTotalesFactura(String totalesFactura) {
		this.totalesFactura = totalesFactura;
	}



	/**
	 * @return the totalesAnuladas
	 */
	public String getTotalesAnuladas() {
		return totalesAnuladas;
	}



	/**
	 * @param totalesAnuladas the totalesAnuladas to set
	 */
	public void setTotalesAnuladas(String totalesAnuladas) {
		this.totalesAnuladas = totalesAnuladas;
	}



	/**
	 * @return the totalesResumen
	 */
	public String getTotalesResumen() {
		return totalesResumen;
	}



	/**
	 * @param totalesResumen the totalesResumen to set
	 */
	public void setTotalesResumen(String totalesResumen) {
		this.totalesResumen = totalesResumen;
	}


	private void reset()
	{		
		this.centroAtencion="";
		this.convenio="";
		this.esAnulado=0;
		this.esResumen=0;
		this.factura="";
		this.fechaFinal="";
		this.fechaGeneracion="";
		this.fechaInicial="";
		this.fechaInicial="";
		this.hayPrograma=0;
		this.identificacionPac="";
		this.listaFacturas=new ArrayList<DtoHonorariosMedico>();
		this.listaFacturasAnuladas=new ArrayList<DtoHonorariosMedico>();
		this.listaProfesionales=new ArrayList<DtoHonorariosMedico>();
		this.listaResumenPool=new ArrayList<DtoHonorariosMedico>();
		this.nombreProfesional="";
		this.nombreProfesionalRes="";
		this.ordenMedica="";
		this.paciente="";
		this.programaOdonto="";
		this.servicio="";
		this.valorFacturado="0";
		this.valorFacturadoARes="0";
		this.valorFacturadoAResTotal="0";
		this.valorFacturadoRes="0";
		this.valorFacturadoResTotal="0";
		this.valorFacturadoTotal="0";
		this.valorHonorario="0";
		this.valorHonorarioARes="0";
		this.valorHonorarioResTotal="0";
		this.valorHonorarioTotal="0";
		this.valorNetFacturado="0";
		this.valorNetFacturadoTotal="0";
		this.valorNetHonorarioTotal="0";
		this.valorNetHonorario="0";
		
		
		this.tituloAnuladas="";
		this.tituloFacturas="";
		this.tituloProfesional="";
		this.tituloResumen="";
		this.lineaAnuladas="";
		this.lineaFacturas="";
		this.lineasResumen="";
		this.totalesAnuladas="";
		this.totalesFactura="";
		this.totalesResumen="";
		
	}

	@Override
	public int compareTo(DtoHonorariosMedico o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

	
}
