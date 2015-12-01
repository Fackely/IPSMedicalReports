package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

/**
 * @author axioma
 * Agosto 21 de 2009
 *
 */
public class DtoPrograma implements Serializable {

	
	
	/**
	 * 
	 */
	private  double codigo;
	/**
	 * 
	 */
	private String nombre;
	/**
	 * 
	 */
	private String fechaModifica;
	/**
	 * 
	 */
	private String horaModifica;
	/**
	 * 
	 */
	private String usuarioModifica;
	/**
	 * 
	 */
	
	//Elementos agregados por Anexo 857
	/**
	 * 
	 */
	private String codigoPrograma;
	
	private int institucion;
	
	private String activo;
	
	private int especialidad;
	
	private String nombreEspecialidad;
	
	//Elemento para el log
	
	private String eliminado;
	
	//Adicionado el 16 de Oct
	DtoConvencionesOdontologicas dtoConvencion;
	
	/**
	 * @return the codigo
	 */
	
	/**
	 * 
	 */
	 private String yaSeleccionado;
	
	private ArrayList<DtoDetalleProgramas> listaServiciosPrograma;
	
	/**
	 * 
	 * @return
	 */
	public double getCodigo() {
		return codigo;
	}
	
	
	/**
	 * Constructor
	 */
	
	public void clean()
	{	
		 this.codigo=0;
		 this.nombre="";
		 this.fechaModifica="";
		 this.horaModifica="";
		 this.usuarioModifica="";
		 this.codigoPrograma="";
		 this.institucion=ConstantesBD.codigoNuncaValido;
		 this.activo=ConstantesBD.acronimoSi;
		 this.especialidad=ConstantesBD.codigoNuncaValido;
		 this.eliminado="";
		 this.nombreEspecialidad="";
		 this.listaServiciosPrograma = new ArrayList<DtoDetalleProgramas>();
		 this.dtoConvencion=new DtoConvencionesOdontologicas();
			 
		}
	
	public void clean2()
	{	
		 this.codigo=0;
		 this.nombre="";
		 this.fechaModifica="";
		 this.horaModifica="";
		 this.usuarioModifica="";
		 this.codigoPrograma="";
		 this.institucion=ConstantesBD.codigoNuncaValido;
		 this.activo=ConstantesBD.acronimoSi;
		 this.eliminado="";
		 this.nombreEspecialidad="";
		 this.listaServiciosPrograma = new ArrayList<DtoDetalleProgramas>();
		 this.dtoConvencion= new DtoConvencionesOdontologicas();
			 
		}
	
	
	public DtoPrograma(){
		this.clean();
	}
	
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
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
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
			return UtilidadFecha.validarFecha(this.fechaModifica)?UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica):"";
	}
	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	/**
	 * @return the horaModfica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	/**
	 * @param horaModfica the horaModfica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
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


	public String getCodigoPrograma() {
		return codigoPrograma;
	}


	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}


	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	public String getActivo() {
		return activo;
	}


	public void setActivo(String activo) {
		this.activo = activo;
	}


	public int getEspecialidad() {
		return especialidad;
	}


	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
	}


	public String getEliminado() {
		return eliminado;
	}


	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}


	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}


	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}


	/**
	 * @return the listaServiciosPrograma
	 */
	public ArrayList<DtoDetalleProgramas> getListaServiciosPrograma() {
		return listaServiciosPrograma;
	}


	/**
	 * @param listaServiciosPrograma the listaServiciosPrograma to set
	 */
	public void setListaServiciosPrograma(
			ArrayList<DtoDetalleProgramas> listaServiciosPrograma) {
		this.listaServiciosPrograma = listaServiciosPrograma;
	}


	public void setYaSeleccionado(String yaSeleccionado) {
		this.yaSeleccionado = yaSeleccionado;
	}


	public String getYaSeleccionado() {
		return yaSeleccionado;
	}


	public DtoConvencionesOdontologicas getDtoConvencion() {
		return dtoConvencion;
	}


	public void setDtoConvencion(DtoConvencionesOdontologicas dtoConvencion) {
		this.dtoConvencion = dtoConvencion;
	}
}
