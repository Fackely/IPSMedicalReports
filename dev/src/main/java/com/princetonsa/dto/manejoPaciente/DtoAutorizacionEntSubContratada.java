package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import util.ConstantesBD;

@SuppressWarnings("serial")
public class DtoAutorizacionEntSubContratada implements Serializable{

	private String codigoPk;
	private String fechaAutorizacion;
	private String horaAutorizacion;
	private int codEntidadAutorizada;
	private String nomEntidadAutorizada;
	private String dirEntidadAutorizada;
	private String telEntidadAutorizada;
	private String consecutivoAutorizacion;
	private String anoConsecutivo;
	private String numeroSolicitud;
	private int codConvenio;
	private String nomConvenio;
	private String fechaVencimiento;
	private int codServicio;
	private String nomServicio;
	private String cantidad;
	private String observaciones;
	private String tipoAutorizacion;
	private String estado;
	private String contabilizado;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private int codInstitucion;
	private String nomInstitucion;
	private int codPaciente;
	private String nomPaciente;
	private String tipoIdPacinte;
	private String numIdPaciente;
	private Date fechaNacimiento;
	private String fechaVencimientoInicial;
	private String fechaProrroga;
	private String horaProrroga;
	private String fechaAnulacion;
	private String horaAnulacion;
	private String usuarioAnulacion;
	private String motivoAnulacion;
	private ArrayList<DtoProrrogaAutorizacionEntSub> arrayProrrogas;
	
	private String nivel;
	private String nomTipoContrato;
	private int codTipoContrato;
	private String ingreso;
	private String tipoAfiliado;
	private String clasificacionSocioeconomica;
	private Date fechaOrden;
	private String edadPaciente;
	private Integer semanasCotizacion;
	private String acronimoDiagnostico;
	private Integer tipoCieDiagnostico;
	private String codigoPropietario;
	
	/**MEDICAMENTOS E INSUMOS AUTORIZADOS
	*/
	private String codMedicamento;
	private String descripMedicamento;
	private String unidMedidaMedicamento;
	private String dosisMedicamento;
	private String frecuenciaMedicamento;
	private String tipoFrecueMedicamento;
	private String viaMedicamento;
	private String diasTrataMedicamento;
	private String nroDosisTotalMedicamento;
	private String es_medicamento;
	private int tipoSolicitud;
	private String codInsumo;
	private String descripInsumo;
 	private String unidMedidaInsumo;
 	private String nroDosisTotalInsumo;
 	private String observaArticulos;
  	private String conseOrdenMedica;
  	private String naturalezaArticulo;
 	ArrayList <DtoAutorizacionEntSubContratada> agrupaListadoEntSub ;
 	

	 
	
	 
	/**
	 * 
	 */
	public DtoAutorizacionEntSubContratada()
	{
		this.clean();
	}
	
	
	/**
	 * 
	 */
	public void clean()
	{
	 
		 this.codigoPk=new String("");
		 this.tipoAfiliado=new String("");
		 this.codigoPropietario= new String("");
		 this.naturalezaArticulo=new String("");
		 this.semanasCotizacion=null;
		 this.acronimoDiagnostico= new String("");
		 this.tipoCieDiagnostico=null;
		 this.edadPaciente=new String("");
		 this.fechaOrden= null;
		 this.clasificacionSocioeconomica= new String("");
		 this.fechaAutorizacion=new String("");
		 this.horaAutorizacion=new String("");
		 this.codEntidadAutorizada=ConstantesBD.codigoNuncaValido;
		 this.nomEntidadAutorizada=new String("");
		 this.dirEntidadAutorizada=new String("");
		 this.telEntidadAutorizada=new String("");
		 this.consecutivoAutorizacion=new String("");
		 this.anoConsecutivo=new String("");
		 this.numeroSolicitud=new String("");
		 this.codConvenio=ConstantesBD.codigoNuncaValido;
		 this.nomConvenio=new String("");
		 this.fechaVencimiento=new String("");
		 this.codServicio=ConstantesBD.codigoNuncaValido;
		 this.nomServicio=new String("");
		 this.cantidad=new String("");
		 this.observaciones=new String("");
		 this.tipoAutorizacion=new String("");
		 this.contabilizado=new String("");
		 this.fechaModificacion=new String("");
		 this.horaModificacion=new String("");
		 this.usuarioModificacion=new String("");
		 this.codInstitucion=ConstantesBD.codigoNuncaValido;
		 this.nomInstitucion=new String("");
		 this.codPaciente=ConstantesBD.codigoNuncaValido;
		 this.nomPaciente=new String("");
		 this.fechaVencimientoInicial=new String("");
		 this.fechaProrroga=new String("");
		 this.horaProrroga=new String("");
		 this.fechaAnulacion=new String("");
		 this.horaAnulacion=new String("");
		 this.usuarioAnulacion=new String("");
		 this.motivoAnulacion=new String("");
		 this.estado=new String("");
		 this.tipoIdPacinte=new String("");
		 this.numIdPaciente=new String("");
		 this.fechaNacimiento = null;
		 this.nivel=new String("");
		 this.nomTipoContrato=new String("");
		 this.codTipoContrato=ConstantesBD.codigoNuncaValido;
		 this.ingreso=new String("");
		 this.arrayProrrogas=new ArrayList<DtoProrrogaAutorizacionEntSub>();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCodigoPk() {
		return codigoPk;
	}
	
	/**
	 * 
	 * @param codigoPk
	 */
	public void setCodigoPk(String codigoPk) {
		this.codigoPk = codigoPk;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaAutorizacion() {
		return fechaAutorizacion;
	}
	
	/**
	 * 
	 * @param fechaAutorizacion
	 */
	public void setFechaAutorizacion(String fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHoraAutorizacion() {
		return horaAutorizacion;
	}
	
	/**
	 * 
	 * @param horaAutorizacion
	 */
	public void setHoraAutorizacion(String horaAutorizacion) {
		this.horaAutorizacion = horaAutorizacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodEntidadAutorizada() {
		return codEntidadAutorizada;
	}
	
	/**
	 * 
	 * @param codEntidadAutorizada
	 */
	public void setCodEntidadAutorizada(int codEntidadAutorizada) {
		this.codEntidadAutorizada = codEntidadAutorizada;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNomEntidadAutorizada() {
		return nomEntidadAutorizada;
	}
	
	/**
	 * 
	 * @param nomEntidadAutorizada
	 */
	public void setNomEntidadAutorizada(String nomEntidadAutorizada) {
		this.nomEntidadAutorizada = nomEntidadAutorizada;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}
	
	/**
	 * 
	 * @param consecutivoAutorizacion
	 */
	public void setConsecutivoAutorizacion(String consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getAnoConsecutivo() {
		return anoConsecutivo;
	}
	
	/**
	 * 
	 * @param a�oConsecutivo
	 */
	public void setAnoConsecutivo(String anoConsecutivo) {
		this.anoConsecutivo = anoConsecutivo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}
	
	/**
	 * 
	 * @param numeroSolicitud
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodConvenio() {
		return codConvenio;
	}
	
	/**
	 * 
	 * @param codConvenio
	 */
	public void setCodConvenio(int codConvenio) {
		this.codConvenio = codConvenio;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNomConvenio() {
		return nomConvenio;
	}
	
	/**
	 * 
	 * @param nomConvenio
	 */
	public void setNomConvenio(String nomConvenio) {
		this.nomConvenio = nomConvenio;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaVencimiento() {
		return fechaVencimiento;
	}
	
	/**
	 * 
	 * @param fechaVencimiento
	 */
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodServicio() {
		return codServicio;
	}
	
	/**
	 * 
	 * @param codServicio
	 */
	public void setCodServicio(int codServicio) {
		this.codServicio = codServicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNomServicio() {
		return nomServicio;
	}
	
	/**
	 * 
	 * @param nomServicio
	 */
	public void setNomServicio(String nomServicio) {
		this.nomServicio = nomServicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCantidad() {
		return cantidad;
	}
	
	/**
	 * 
	 * @param cantidad
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}
	
	/**
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}
	
	/**
	 * 
	 * @param tipoAutorizacion
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getContabilizado() {
		return contabilizado;
	}
	
	/**
	 * 
	 * @param contabilizado
	 */
	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	
	/**
	 * 
	 * @param fechaModificacion
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}
	
	/**
	 * 
	 * @param horaModificacion
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	
	/**
	 * 
	 * @param usuarioModificacion
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodInstitucion() {
		return codInstitucion;
	}
	
	/**
	 * 
	 * @param codInstitucion
	 */
	public void setCodInstitucion(int codInstitucion) {
		this.codInstitucion = codInstitucion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNomInstitucion() {
		return nomInstitucion;
	}
	
	/**
	 * 
	 * @param nomInstitucion
	 */
	public void setNomInstitucion(String nomInstitucion) {
		this.nomInstitucion = nomInstitucion;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCodPaciente() {
		return codPaciente;
	}
	
	/**
	 * 
	 * @param codPaciente
	 */
	public void setCodPaciente(int codPaciente) {
		this.codPaciente = codPaciente;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNomPaciente() {
		return nomPaciente;
	}
	
	/**
	 * 
	 * @param nomPaciente
	 */
	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaVencimientoInicial() {
		return fechaVencimientoInicial;
	}
	
	/**
	 * 
	 * @param fechaVencimientoInicial
	 */
	public void setFechaVencimientoInicial(String fechaVencimientoInicial) {
		this.fechaVencimientoInicial = fechaVencimientoInicial;
	}
	
	/***
	 * 
	 * 
	 * @return
	 */
	public String getFechaProrroga() {
		return fechaProrroga;
	}
	
	/**
	 * 
	 * @param fechaProrroga
	 */
	public void setFechaProrroga(String fechaProrroga) {
		this.fechaProrroga = fechaProrroga;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHoraProrroga() {
		return horaProrroga;
	}
	
	/**
	 * 
	 * @param horaProrroga
	 */
	public void setHoraProrroga(String horaProrroga) {
		this.horaProrroga = horaProrroga;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaAnulacion() {
		return fechaAnulacion;
	}
	
	/**
	 * 
	 * @param fechaAnulacion
	 */
	public void setFechaAnulacion(String fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHoraAnulacion() {
		return horaAnulacion;
	}
	
	/**
	 * 
	 * @param horaAnulacion
	 */
	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsuarioAnulacion() {
		return usuarioAnulacion;
	}
	
	/**
	 * 
	 * @param usuarioAnulacion
	 */
	public void setUsuarioAnulacion(String usuarioAnulacion) {
		this.usuarioAnulacion = usuarioAnulacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}
	
	/**
	 * 
	 * @param motivoAnulacion
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}


  /**
   * 
   * @return
   */
	public String getEstado() {
		return estado;
	}


   /**
    * 
    * @param estado
    */
	public void setEstado(String estado) {
		this.estado = estado;
	}

/**
 * 
 * @return tipoIdPaciente
 */
public String getTipoIdPacinte() {
	return tipoIdPacinte;
}


public void setTipoIdPacinte(String tipoIdPacinte) {
	this.tipoIdPacinte = tipoIdPacinte;
}


public String getNumIdPaciente() {
	return numIdPaciente;
}


public void setNumIdPaciente(String numIdPaciente) {
	this.numIdPaciente = numIdPaciente;
}


public String getNivel() {
	return nivel;
}


public void setNivel(String nivel) {
	this.nivel = nivel;
}


public int getCodTipoContrato() {
	return codTipoContrato;
}


public void setCodTipoContrato(int codTipoContrato) {
	this.codTipoContrato = codTipoContrato;
}


public String getNomTipoContrato() {
	return nomTipoContrato;
}


public void setNomTipoContrato(String nomTipoContrato) {
	this.nomTipoContrato = nomTipoContrato;
}


public String getIngreso() {
	return ingreso;
}


public void setIngreso(String ingreso) {
	this.ingreso = ingreso;
}


public ArrayList<DtoProrrogaAutorizacionEntSub> getArrayProrrogas() {
	return arrayProrrogas;
}


public void setArrayProrrogas(
		ArrayList<DtoProrrogaAutorizacionEntSub> arrayProrrogas) {
	this.arrayProrrogas = arrayProrrogas;
}


/**
 * @return the dirEntidadAutorizada
 */
public String getDirEntidadAutorizada() {
	return dirEntidadAutorizada;
}


/**
 * @param dirEntidadAutorizada the dirEntidadAutorizada to set
 */
public void setDirEntidadAutorizada(String dirEntidadAutorizada) {
	this.dirEntidadAutorizada = dirEntidadAutorizada;
}


/**
 * @return the telEntidadAutorizada
 */
public String getTelEntidadAutorizada() {
	return telEntidadAutorizada;
}


/**
 * @param telEntidadAutorizada the telEntidadAutorizada to set
 */
public void setTelEntidadAutorizada(String telEntidadAutorizada) {
	this.telEntidadAutorizada = telEntidadAutorizada;
}

/**
 * 
 * @return codigo del Articulo Insumo
 */
public String getCodInsumo() {
	return codInsumo;
}

/**
 * 
 * @param codInsumo
 */
public void setCodInsumo(String codInsumo) {
	this.codInsumo = codInsumo;
}

/***
 * 
 * @return la descripcion de Articulo Insumo
 */
public String getDescripInsumo() {
	return descripInsumo;
}

/**
 * 
 * @param descripInsumo
 */
public void setDescripInsumo(String descripInsumo) {
	this.descripInsumo = descripInsumo;
}

/**
 * 
 * @return la unidad de medida del Articulo Insumo
 */
public String getUnidMedidaInsumo() {
	return unidMedidaInsumo;
}

/**
 * 
 * @param unidMedidaInsumo
 */
public void setUnidMedidaInsumo(String unidMedidaInsumo) {
	this.unidMedidaInsumo = unidMedidaInsumo;
}

/**
 * 
 * @return la cantidad del Articulo Insumo
 */
public String getNroDosisTotalInsumo() {
	return nroDosisTotalInsumo;
}

/**
 * 
 * @param cantidadInsumo
 */
public void setNroDosisTotalInsumo(String nroDosisTotalInsumo) {
	this.nroDosisTotalInsumo = nroDosisTotalInsumo;
}

/**
 * 
 * @param tipoSolicitud 
 */
public void setTipoSolicitud(int tipoSolicitud) {
	this.tipoSolicitud = tipoSolicitud;
}

/**
 * 
 * @return el tipo de solicitud (5-Procedimientos,3-consulta,4-interconsulta,14-cirugia,-6medicamento)
 */
public int getTipoSolicitud() {
	return tipoSolicitud;
}

/**
 * 
 * @return el acronimo 09 o 02 de si es medicamento o no respectivamente(insumo,medicamento)
 */
public String getEs_medicamento() {
	return es_medicamento;
}

/**
 * 
 * @param es_medicamento
 */
public void setEs_medicamento(String es_medicamento) {
	this.es_medicamento = es_medicamento;
}

/**
 * 
 * @return observaciones de la solicitud de medicamentos e insumos 
 */
public String getObservaArticulos() {
	return observaArticulos;
}

/**
 * 
 * @param observaArticulos
 */
public void setObservaArticulos(String observaArticulos) {
	this.observaArticulos = observaArticulos;
}

/**
 * 
 * @return codigo del medicamento
 */
public String getCodMedicamento() {
	return codMedicamento;
}

/**
 * 
 * @param codMedicamento
 */
public void setCodMedicamento(String codMedicamento) {
	this.codMedicamento = codMedicamento;
}

/**
 * 
 * @return descripcion del medicamento
 */
public String getDescripMedicamento() {
	return descripMedicamento;
}

/**
 * 
 * @param descripMedicamento
 */
public void setDescripMedicamento(String descripMedicamento) {
	this.descripMedicamento = descripMedicamento;
}

/**
 * 
 * @return unidad medida del medicamento
 */
public String getUnidMedidaMedicamento() {
	return unidMedidaMedicamento;
}

/**
 * 
 * @param unidMedidaMedicamento
 */
public void setUnidMedidaMedicamento(String unidMedidaMedicamento) {
	this.unidMedidaMedicamento = unidMedidaMedicamento;
}

/**
 * 
 * @return dosis del medicamento
 */
public String getDosisMedicamento() {
	return dosisMedicamento;
}

/**
 * 
 * @param dosisMedicamento
 */
public void setDosisMedicamento(String dosisMedicamento) {
	this.dosisMedicamento = dosisMedicamento;
}

/**
 * 
 * @return frecuencia del medicamento
 */
public String getFrecuenciaMedicamento() {
	return frecuenciaMedicamento;
}

/**
 * 
 * @param frecuenciaMedicamento
 */
public void setFrecuenciaMedicamento(String frecuenciaMedicamento) {
	this.frecuenciaMedicamento = frecuenciaMedicamento;
}

public void setTipoFrecueMedicamento(String tipoFrecueMedicamento) {
	this.tipoFrecueMedicamento = tipoFrecueMedicamento;
}


public String getTipoFrecueMedicamento() {
	return tipoFrecueMedicamento;
}


/**
 * 
 * @return via del medicemnto
 */
public String getViaMedicamento() {
	return viaMedicamento;
}

/**
 * 
 * @param viaMedicamento
 */
public void setViaMedicamento(String viaMedicamento) {
	this.viaMedicamento = viaMedicamento;
}

/**
 * 
 * @return dias tratamiento del medicamento
 */
public String getDiasTrataMedicamento() {
	return diasTrataMedicamento;
}

/**
 * 
 * @param diasTrataMedicamento
 */
public void setDiasTrataMedicamento(String diasTrataMedicamento) {
	this.diasTrataMedicamento = diasTrataMedicamento;
}

/**
 * 
 * @return cantidad medicamento
 */
public String getNroDosisTotalMedicamento() {
	return nroDosisTotalMedicamento;
}

/**
 * 
 * @param cantidadMedicamento
 */
public void setNroDosisTotalMedicamento(String nroDosisTotalMedicamento) {
	this.nroDosisTotalMedicamento = nroDosisTotalMedicamento;
}

/**
 * 
 * @param conseOrdenMedica
 */
public void setConseOrdenMedica(String conseOrdenMedica) {
	this.conseOrdenMedica = conseOrdenMedica;
}

/**
 * 
 * @return consecutivo de orden medica
 */
public String getConseOrdenMedica() {
	return conseOrdenMedica;
}

/**
 * 
 * @return registros agrupados por medicamentos  
 */
public ArrayList<DtoAutorizacionEntSubContratada> getAgrupaListadoEntSub() {
	return agrupaListadoEntSub;
}

/**
 * 
 * @param agrupaListadoEntSub
 */
public void setAgrupaListadoEntSub(
		ArrayList<DtoAutorizacionEntSubContratada> agrupaListadoEntSub) {
	this.agrupaListadoEntSub = agrupaListadoEntSub;
}


public void setFechaNacimiento(Date fechaNacimiento) {
	this.fechaNacimiento = fechaNacimiento;
}


public Date getFechaNacimiento() {
	return fechaNacimiento;
}


public void setTipoAfiliado(String tipoAfiliado) {
	this.tipoAfiliado = tipoAfiliado;
}


public String getTipoAfiliado() {
	return tipoAfiliado;
}


public void setClasificacionSocioeconomica(String clasificacionSocioeconomica) {
	this.clasificacionSocioeconomica = clasificacionSocioeconomica;
}


public String getClasificacionSocioeconomica() {
	return clasificacionSocioeconomica;
}


public void setFechaOrden(Date fechaOrden) {
	this.fechaOrden = fechaOrden;
}


public Date getFechaOrden() {
	return fechaOrden;
}


public void setEdadPaciente(String edadPaciente) {
	this.edadPaciente = edadPaciente;
}


public String getEdadPaciente() {
	return edadPaciente;
}


public void setSemanasCotizacion(Integer semanasCotizacion) {
	this.semanasCotizacion = semanasCotizacion;
}


public Integer getSemanasCotizacion() {
	return semanasCotizacion;
}


public void setNaturalezaArticulo(String naturalezaArticulo) {
	this.naturalezaArticulo = naturalezaArticulo;
}


public String getNaturalezaArticulo() {
	return naturalezaArticulo;
}


public void setAcronimoDiagnostico(String acronimoDiagnostico) {
	this.acronimoDiagnostico = acronimoDiagnostico;
}


public String getAcronimoDiagnostico() {
	return acronimoDiagnostico;
}


public void setTipoCieDiagnostico(Integer tipoCieDiagnostico) {
	this.tipoCieDiagnostico = tipoCieDiagnostico;
}


public Integer getTipoCieDiagnostico() {
	return tipoCieDiagnostico;
}


public void setCodigoPropietario(String codigoPropietario) {
	this.codigoPropietario = codigoPropietario;
}


public String getCodigoPropietario() {
	return codigoPropietario;
}
	
	
}
