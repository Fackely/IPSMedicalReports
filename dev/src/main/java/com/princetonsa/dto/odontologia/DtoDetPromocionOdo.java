package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosDouble;

public class DtoDetPromocionOdo implements Serializable {
	
	/**
	 * 
	 */
	private int codigoPk;
	private int detPromocionOdo;
	private int  promocionOdontologia;
	private double regionCentroAtencion;
	private String nombreRegion;
	
	
	

	
	private String paisCentroAtencion;
	private String ciudadCentroAtencion;
	
	
	

	private String nombreCiudad;
	
	private String deptoCentroAtencion;
	private String nombrePais; 
	
	


	private String nombreDepartamento;
	
	private double categoriaCentroAtencion;
	private String nombreCategoria;
	
	private InfoDatosDouble programaOdontologico;
	private InfoDatosDouble servicio;
	private int edadInicial;
	private int edadFinal;
	private String nombreEstadoCivil;
	private int sexo;
	private String nombreSexo;
	private String estadoCivil;
	private int nroHijos;
	private int ocupacionPaciente;
	private String nombreOcupacion;
	private double porcentajeDescuento;
	private double valorDescuento;
	
	
	private double porcentajeHonorarios;
	private double valorHonorario;
	private String horaModificada;
	private String fechaModificada;
	private String usuarioModifica;
	
	
	
	
	
	/**
	 * DTO CARGAR LOS CENTROS DE ATENCION PARA UN DETALLE DE PROMOCIONES
	 * SOLO ES UTILIZADO EN EL MOMENTO DE CONSULTA AVANZADA 
	 */
	private DtoDetCaPromocionesOdo dtoDetCaPromociones;
	private ArrayList<DtoDetCaPromocionesOdo> listDetCa = new ArrayList<DtoDetCaPromocionesOdo>();
	/**
	 * DTO CARGAR LOS CONVENIOS ODONTOLOGICOS PARA DIFERENTES DETALLES DE PROMOCIONES ODONTOLOGICAS
	 */
	private DtoDetConvPromocionesOdo dtoDetConvPromociones;
	private ArrayList<DtoDetConvPromocionesOdo> listDetConv = new ArrayList<DtoDetConvPromocionesOdo>();
	
	
	/**
	 * 
	 */
	public void reset(){
			detPromocionOdo=0;
			codigoPk=0;
		 	promocionOdontologia=0;
		 	regionCentroAtencion=0;
		 	paisCentroAtencion="";
		 	ciudadCentroAtencion="";
			deptoCentroAtencion="";
	 		categoriaCentroAtencion=0;
		 	this.setProgramaOdontologico(new InfoDatosDouble());
			edadInicial=0;
			edadFinal=0;
			sexo=0;
			estadoCivil="";
			nroHijos=0;
			ocupacionPaciente=0;
			porcentajeDescuento=0;
			valorDescuento=0;
	 		porcentajeHonorarios=0;
	 		valorHonorario=0;
	 		horaModificada="";
	 		fechaModificada="";
			 usuarioModifica="";
			 this.servicio = new InfoDatosDouble();
			 this.dtoDetCaPromociones= new DtoDetCaPromocionesOdo();
			 this.dtoDetConvPromociones = new DtoDetConvPromocionesOdo();
			 this.listDetCa = new ArrayList<DtoDetCaPromocionesOdo>();
			 this.listDetConv = new ArrayList<DtoDetConvPromocionesOdo>();
			 this.nombreEstadoCivil= "";
			 this.nombreSexo="";
			 this.nombreOcupacion="";
			 this.nombreCiudad="";
			 this.nombreCategoria="";
			 this.nombreDepartamento="";
			 this.setNombreRegion("");
	}


   public DtoDetPromocionOdo(){
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
	 * @return the promocionOdontologia
	 */
	public int getPromocionOdontologia() {
		return promocionOdontologia;
	}



	/**
	 * @param promocionOdontologia the promocionOdontologia to set
	 */
	public void setPromocionOdontologia(int promocionOdontologia) {
		this.promocionOdontologia = promocionOdontologia;
	}



	/**
	 * @return the regionCentroAtencion
	 */
	public double getRegionCentroAtencion() {
		return regionCentroAtencion;
	}



	/**
	 * @param regionCentroAtencion the regionCentroAtencion to set
	 */
	public void setRegionCentroAtencion(double regionCentroAtencion) {
		this.regionCentroAtencion = regionCentroAtencion;
	}



	/**
	 * @return the paisCentroAtencion
	 */
	public String getPaisCentroAtencion() {
		return paisCentroAtencion;
	}



	/**
	 * @param paisCentroAtencion the paisCentroAtencion to set
	 */
	public void setPaisCentroAtencion(String paisCentroAtencion) {
		this.paisCentroAtencion = paisCentroAtencion;
	}



	/**
	 * @return the ciudadCentroAtencion
	 */
	public String getCiudadCentroAtencion() {
		return ciudadCentroAtencion;
	}



	/**
	 * @param ciudadCentroAtencion the ciudadCentroAtencion to set
	 */
	public void setCiudadCentroAtencion(String ciudadCentroAtencion) {
		this.ciudadCentroAtencion = ciudadCentroAtencion;
	}



	/**
	 * @return the deptoCentroAtencion
	 */
	public String getDeptoCentroAtencion() {
		return deptoCentroAtencion;
	}



	/**
	 * @param deptoCentroAtencion the deptoCentroAtencion to set
	 */
	public void setDeptoCentroAtencion(String deptoCentroAtencion) {
		this.deptoCentroAtencion = deptoCentroAtencion;
	}



	/**
	 * @return the categoriaCentroAtencion
	 */
	public double getCategoriaCentroAtencion() {
		return categoriaCentroAtencion;
	}



	/**
	 * @param categoriaCentroAtencion the categoriaCentroAtencion to set
	 */
	public void setCategoriaCentroAtencion(double categoriaCentroAtencion) {
		this.categoriaCentroAtencion = categoriaCentroAtencion;
	}



	/**
	 * @return the programaOdontologico
	 */
	public InfoDatosDouble getProgramaOdontologico() {
		return programaOdontologico;
	}



	/**
	 * @param programaOdontologico the programaOdontologico to set
	 */
	public void setProgramaOdontologico(InfoDatosDouble programaOdontologico) {
		this.programaOdontologico = programaOdontologico;
	}



	/**
	 * @return the edadInicial
	 */
	public int getEdadInicial() {
		return edadInicial;
	}



	/**
	 * @param edadInicial the edadInicial to set
	 */
	public void setEdadInicial(int edadInicial) {
		this.edadInicial = edadInicial;
	}



	/**
	 * @return the edadFinal
	 */
	public int getEdadFinal() {
		return edadFinal;
	}



	/**
	 * @param edadFinal the edadFinal to set
	 */
	public void setEdadFinal(int edadFinal) {
		this.edadFinal = edadFinal;
	}



	/**
	 * @return the sexo
	 */
	public int getSexo() {
		return sexo;
	}



	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(int sexo) {
		this.sexo = sexo;
	}



	/**
	 * @return the estadoCivil
	 */
	public String getEstadoCivil() {
		return estadoCivil;
	}



	/**
	 * @param estadoCivil the estadoCivil to set
	 */
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}



	/**
	 * @return the nroHijos
	 */
	public int getNroHijos() {
		return nroHijos;
	}



	/**
	 * @param nroHijos the nroHijos to set
	 */
	public void setNroHijos(int nroHijos) {
		this.nroHijos = nroHijos;
	}



	/**
	 * @return the ocupacionPaciente
	 */
	public int getOcupacionPaciente() {
		return ocupacionPaciente;
	}



	/**
	 * @param ocupacionPaciente the ocupacionPaciente to set
	 */
	public void setOcupacionPaciente(int ocupacionPaciente) {
		this.ocupacionPaciente = ocupacionPaciente;
	}



	/**
	 * @return the porcentajeDescuento
	 */
	public double getPorcentajeDescuento() {
		return porcentajeDescuento;
	}



	/**
	 * @param porcentajeDescuento the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(double porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}



	/**
	 * @return the valorDescuento
	 */
	public double getValorDescuento() {
		return valorDescuento;
	}



	/**
	 * @param valorDescuento the valorDescuento to set
	 */
	public void setValorDescuento(double valorDescuento) {
		this.valorDescuento = valorDescuento;
	}



	

	/**
	 * @return the porcentajeHonorarios
	 */
	public double getPorcentajeHonorarios() {
		return porcentajeHonorarios;
	}



	/**
	 * @param porcentajeHonorarios the porcentajeHonorarios to set
	 */
	public void setPorcentajeHonorarios(double porcentajeHonorarios) {
		this.porcentajeHonorarios = porcentajeHonorarios;
	}



	/**
	 * @return the valorHonorario
	 */
	public double getValorHonorario() {
		return valorHonorario;
	}



	/**
	 * @param valorHonorario the valorHonorario to set
	 */
	public void setValorHonorario(double valorHonorario) {
		this.valorHonorario = valorHonorario;
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



	public void setDetPromocionOdo(int detPromocionOdo) {
		this.detPromocionOdo = detPromocionOdo;
	}



	public int getDetPromocionOdo() {
		return detPromocionOdo;
	}


	/**
	 * @return the servicio
	 */
	public InfoDatosDouble getServicio() {
		return servicio;
	}


	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosDouble servicio) {
		this.servicio = servicio;
	}


	/**
	 * @param dtoDetCaPromociones the dtoDetCaPromociones to set
	 */
	public void setDtoDetCaPromociones(DtoDetCaPromocionesOdo dtoDetCaPromociones) {
		this.dtoDetCaPromociones = dtoDetCaPromociones;
	}


	/**
	 * @return the dtoDetCaPromociones
	 */
	public DtoDetCaPromocionesOdo getDtoDetCaPromociones() {
		return dtoDetCaPromociones;
	}


	/**
	 * @param dtoDetConvPromociones the dtoDetConvPromociones to set
	 */
	public void setDtoDetConvPromociones(DtoDetConvPromocionesOdo dtoDetConvPromociones) {
		this.dtoDetConvPromociones = dtoDetConvPromociones;
	}


	/**
	 * @return the dtoDetConvPromociones
	 */
	public DtoDetConvPromocionesOdo getDtoDetConvPromociones() {
		return dtoDetConvPromociones;
	}


	/**
	 * @param listDetCa the listDetCa to set
	 */
	public void setListDetCa(ArrayList<DtoDetCaPromocionesOdo> listDetCa) {
		this.listDetCa = listDetCa;
	}


	/**
	 * @return the listDetCa
	 */
	public ArrayList<DtoDetCaPromocionesOdo> getListDetCa() {
		return listDetCa;
	}


	/**
	 * @param listDetConv the listDetConv to set
	 */
	public void setListDetConv(ArrayList<DtoDetConvPromocionesOdo> listDetConv) {
		this.listDetConv = listDetConv;
	}


	/**
	 * @return the listDetConv
	 */
	public ArrayList<DtoDetConvPromocionesOdo> getListDetConv() {
		return listDetConv;
	}


	public void setNombreEstadoCivil(String nombreEstadoCivil) {
		this.nombreEstadoCivil = nombreEstadoCivil;
	}


	public String getNombreEstadoCivil() {
		return nombreEstadoCivil;
	}


	public void setNombreSexo(String nombreSexo) {
		this.nombreSexo = nombreSexo;
	}


	public String getNombreSexo() {
		return nombreSexo;
	}


	public void setNombreOcupacion(String nombreOcupacion) {
		this.nombreOcupacion = nombreOcupacion;
	}


	public String getNombreOcupacion() {
		return nombreOcupacion;
	}



	public String getNombreCiudad() {
		return nombreCiudad;
	}


	public void setNombreCiudad(String nombreCiudad) {
		this.nombreCiudad = nombreCiudad;
	}


	public String getNombreDepartamento() {
		return nombreDepartamento;
	}


	public void setNombreDepartamento(String nombreDepartamento) {
		this.nombreDepartamento = nombreDepartamento;
	}


	public String getNombreCategoria() {
		return nombreCategoria;
	}


	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}


	public void setNombreRegion(String nombreRegion) {
		this.nombreRegion = nombreRegion;
	}


	public String getNombreRegion() {
		return nombreRegion;
	}
	
	public String getNombrePais() {
		return nombrePais;
	}


	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}

	

}
