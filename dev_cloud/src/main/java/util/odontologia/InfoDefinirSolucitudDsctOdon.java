package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;


/**
 * Clase para la construccion grafica de la funcionalidad Autorizacion de descuento Odontologico
 * @author axioma
 *
 */
public class InfoDefinirSolucitudDsctOdon implements Serializable , Comparable<InfoDefinirSolucitudDsctOdon> {
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal codigoSolicitud;
	private String tipoIndentificacion ;
	private String numeroIdentificacion ;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private InfoDatosInt ingreso;
	private BigDecimal consecutivoPresupuesto;
	private BigDecimal codigoPkPresupuesto;
	private BigDecimal valorPresupuesto;
	private BigDecimal valorPresupuestoSinDctosPromociones;
	private BigDecimal codigoPkPresupuestoDctoOdon;
	
	private InfoDatosInt centroAtencion;
	private String estadoPresupuesto;
	//Elementos para el detalle de autorizaci贸n
	private String fechaHora;
	private String acronimoEstado;
	private String estado;
	private String usuarioGeneracion;
	
	private BigDecimal codigoPkDetDescuentoOdon;
	private BigDecimal codigoPkDetDescuentoOdonAten;
	

	private int codigoPaciente;
	private BigDecimal porcentaje;
	private String nombreViaIngreso;
	private String descripcionMotivo;
	
	private String estadoPresupuestoDcto;
	private String estadoAutorizacionDescuento;
	private BigDecimal codigoMotivoDescuento;
	private String tipoMotivo;
	private String acronimoTipoMotivo;
	private String estadoSolicitud;
	private String acronimoEstadoSolicitud;
	private String fechaInicial;
	private String fechaFinal;
	
	private String observaciones;
	private BigDecimal valorDescuento;
	private int cuenta;
	
	private String acronimoIndicativoMotivo;
	/**
	 * Utilizado para ordenar la consulta Ascendente o Descendente
	 */
	private String ordenamiento;
	
	
	private String tipoBusqueda;
	
	/**
	 * Indica si fue generado en la inclusi贸n o no
	 */
	private boolean inclusion;
	
	/**
	 * Llave primaria del registro del encabezado de la inclusi贸n
	 */
	private long codigoPkEncabezadoInclusion;
	
	/**
	 * Llave primaria del registro del detalle de la Autorizaci贸n de la 
	 * solicitud de descuento
	 */
	private long codigoPkAutorizacionPresuDctoOdon;

	/**
	 * Dias de vigencia que tiene la solicitud de descuento solo
	 * cuando ha sido autorizada
	 */
	private int diasVigencia;
	
	/**
	 * Fecha de la autorizaci贸n de la solicitud de descuento
	 */
	private Date fechaAutorizacion;
	
	/**
	 * Solicitudes Generadas desde Presupuestos
	 */
	private boolean soliGeneraPresupuesto;
	
	/**
	 * Solicitudes Generadas desde Inclusiones
	 */
	private boolean soliGeneraInclusiones;
	
	/**
	 * Estado Presupuesto
	 */
	private String estadoPresupuestos;
	
	/**
	 * Estado Inclusion 
	 */
	private String estadoInclusion;

	/**
	 * Estado Nombre Indicativo Motivo
	 */
	private String nombreIndicativoMotivo;
	
	/**
	 * Ingreso: Columna que muestra el n鷐ero de ingreso asociado a la solicitud de descuento
	 */
	private int ingresoSoliDescuento;
	

	/**
	 * 
	 */
	public void reset()
	{   
		this.ingresoSoliDescuento = ConstantesBD.codigoNuncaValido;
		this.soliGeneraPresupuesto = false; 
		this.soliGeneraInclusiones = false; 
		this.estadoInclusion = "";
		this.estadoPresupuestos= "";
		this.nombreIndicativoMotivo = "";
		this.tipoIndentificacion = "";
		this.numeroIdentificacion ="";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.primerApellido = "";
		this.segundoApellido = "";
		this.ingreso = new InfoDatosInt();
		this.consecutivoPresupuesto =  new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.codigoPkPresupuesto= new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.estadoPresupuesto ="";
		this.centroAtencion = new InfoDatosInt();
		this.codigoPkDetDescuentoOdon = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.estadoPresupuestoDcto="";
		this.codigoPkPresupuestoDctoOdon = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.fechaFinal = "";
		this.fechaInicial="";
		this.tipoBusqueda="";
		this.codigoMotivoDescuento=new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.acronimoTipoMotivo="";
		this.estadoAutorizacionDescuento="";
		this.valorPresupuesto = BigDecimal.ZERO;
		this.cuenta=ConstantesBD.codigoNuncaValido;
		this.valorPresupuestoSinDctosPromociones= BigDecimal.ZERO;
		this.ordenamiento = "";
		this.valorDescuento = new BigDecimal(0);
		this.acronimoEstadoSolicitud = "";
		
		this.codigoPkAutorizacionPresuDctoOdon = ConstantesBD.codigoNuncaValidoLong;
		
		this.codigoPkDetDescuentoOdonAten = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		
		this.acronimoIndicativoMotivo = "";
		
		this.diasVigencia = ConstantesBD.codigoNuncaValido;
	

	}

	/**
	 * @return the tipoIndentificacion
	 */
	public String getTipoIndentificacion() {
		return tipoIndentificacion;
	}

	/**
	 * @return the tipoIndentificacion
	 */
	public String getTipoYNumeroIdentificacion() {
		return this.tipoIndentificacion+" "+this.numeroIdentificacion;
	 }
	
	


	/**
	 * @param tipoIndentificacion the tipoIndentificacion to set
	 */
	public void setTipoIndentificacion(String tipoIndentificacion) {
		this.tipoIndentificacion = tipoIndentificacion;
	}



	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}



	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}



	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	/**
	 * @return the primerNombre
	 */
	public String getNombres() {
		return this.primerApellido+" "+this.segundoApellido+" "+this.getPrimerNombre()+" "+this.getSegundoNombre();
	}
	

	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}



	/**
	 * @return the segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}



	/**
	 * @param segundoNombre the segundoNombre to set
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}



	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}



	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}



	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}



	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}



	/**
	 * @return the ingreso
	 */
	public InfoDatosInt getIngreso() {
		return ingreso;
	}



	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(InfoDatosInt ingreso) {
		this.ingreso = ingreso;
	}



	/**
	 * @return the consecutivoPresupuesto
	 */
	public BigDecimal getConsecutivoPresupuesto() {
		return consecutivoPresupuesto;
	}



	/**
	 * @param consecutivoPresupuesto the consecutivoPresupuesto to set
	 */
	public void setConsecutivoPresupuesto(BigDecimal consecutivoPresupuesto) {
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}

	
	/**
	 * 
	 */
	public InfoDefinirSolucitudDsctOdon() {
		this.reset();
		}
	

	/**
	 * @return the valorDescuento
	 */
	public BigDecimal getValorDescuento() {
		return valorDescuento;
	}


	
	/**
	 * @param valorDescuento the valorDescuento to set
	 */
	public void setValorDescuento(BigDecimal valorDescuento) {
		if(valorDescuento == null)
			valorDescuento = BigDecimal.ZERO;
		this.valorDescuento = valorDescuento;
	}



	public void setCodigoPkPresupuesto(BigDecimal codigoPkPresupuesto) {
		this.codigoPkPresupuesto = codigoPkPresupuesto;
	}



	public BigDecimal getCodigoPkPresupuesto() {
		return codigoPkPresupuesto;
	}



	/**
	 * @return the valorPresupuesto
	 */
	public BigDecimal getValorPresupuesto() {
		return valorPresupuesto;
	}

	/**
	 * @return the valorPresupuesto
	 */
	public String getValorPresupuestoFormateado() {
		return UtilidadTexto.formatearValores(valorPresupuesto.doubleValue());
	}

	/**
	 * @return the valorDescuento
	 */
	public String getValorDescuentoFormateado() {
		return UtilidadTexto.formatearValores(valorDescuento.doubleValue());
	}


	/**
	 * @param valorPresupuesto the valorPresupuesto to set
	 */
	public void setValorPresupuesto(BigDecimal valorPresupuesto) {
		this.valorPresupuesto = valorPresupuesto;
	}


	



	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}



	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}






	public String getFechaHora() {
		return fechaHora;
	}



	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public String getUsuarioGeneracion() {
		return usuarioGeneracion;
	}



	public void setUsuarioGeneracion(String usuarioGeneracion) {
		this.usuarioGeneracion = usuarioGeneracion;
	}



	/**
	 * @return the estadoPresupuesto
	 */
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}



	/**
	 * @param estadoPresupuesto the estadoPresupuesto to set
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
	}



	public void setCodigoPkDetDescuentoOdon(BigDecimal codigoPkDetDescuentoOdon) {
		this.codigoPkDetDescuentoOdon = codigoPkDetDescuentoOdon;
	}



	public BigDecimal getCodigoPkDetDescuentoOdon() {
		return codigoPkDetDescuentoOdon;
	}



	public int getCodigoPaciente() {
		return codigoPaciente;
	}



	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}



	public BigDecimal getPorcentaje() {
		return porcentaje;
	}



	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}



	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}



	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}



	public String getDescripcionMotivo() {
		return descripcionMotivo;
	}



	public void setDescripcionMotivo(String descripcionMotivo) {
		this.descripcionMotivo = descripcionMotivo;
	}



	public String getAcronimoEstado() {
		return acronimoEstado;
	}



	public void setAcronimoEstado(String acronimoEstado) {
		this.acronimoEstado = acronimoEstado;
	}



	public void setEstadoPresupuestoDcto(String estadoPresupuestoDcto) {
		this.estadoPresupuestoDcto = estadoPresupuestoDcto;
	}



	public String getEstadoPresupuestoDcto() {
		return estadoPresupuestoDcto;
	}



	public void setCodigoPkPresupuestoDctoOdon(
			BigDecimal codigoPkPresupuestoDctoOdon) {
		this.codigoPkPresupuestoDctoOdon = codigoPkPresupuestoDctoOdon;
	}



	public BigDecimal getCodigoPkPresupuestoDctoOdon() {
		return codigoPkPresupuestoDctoOdon;
	}
	
	



	public String getEstadoSolicitud() {
		return estadoSolicitud;
	}



	public void setEstadoSolicitud(String estadoSolicitud) {
		this.estadoSolicitud = estadoSolicitud;
	}



	public String getAcronimoEstadoSolicitud() {
		return acronimoEstadoSolicitud;
	}



	public void setAcronimoEstadoSolicitud(String acronimoEstadoSolicitud) {
		this.acronimoEstadoSolicitud = acronimoEstadoSolicitud;
	}



	public BigDecimal getCodigoSolicitud() {
		return codigoSolicitud;
	}



	public void setCodigoSolicitud(BigDecimal codigoSolicitud) {
		this.codigoSolicitud = codigoSolicitud;
	}



	public String getTipoMotivo() {
		return tipoMotivo;
	}



	public void setTipoMotivo(String tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}



	public String getObservaciones() {
		return observaciones;
	}



	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * 
	 * @return
	 */
	public String getFechaInicialBD() {
		return  UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial) ;
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}



	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinalBD() {
		return  UtilidadFecha.conversionFormatoFechaABD(this.fechaFinal);
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}



	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}



	public String getTipoBusqueda() {
		return tipoBusqueda;
	}



	public BigDecimal getCodigoMotivoDescuento() {
		return codigoMotivoDescuento;
	}



	public void setCodigoMotivoDescuento(BigDecimal codigoMotivoDescuento) {
		this.codigoMotivoDescuento = codigoMotivoDescuento;
	}



	public String getAcronimoTipoMotivo() {
		return acronimoTipoMotivo;
	}



	public void setAcronimoTipoMotivo(String acronimoTipoMotivo) {
		this.acronimoTipoMotivo = acronimoTipoMotivo;
	}



	public void setEstadoAutorizacionDescuento(
			String estadoAutorizacionDescuento) {
		this.estadoAutorizacionDescuento = estadoAutorizacionDescuento;
	}



	public String getEstadoAutorizacionDescuento() {
		return estadoAutorizacionDescuento;
	}



	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}



	public int getCuenta() {
		return cuenta;
	}



	/**
	 * @return the valorPresupuestoSinDctosPromociones
	 */
	public BigDecimal getValorPresupuestoSinDctosPromociones() {
		return valorPresupuestoSinDctosPromociones;
	}

	/**
	 * @return the valorPresupuestoSinDctosPromociones
	 */
	public String getValorPresupuestoSinDctosPromocionesFormateado() {
		return UtilidadTexto.formatearValores(valorPresupuestoSinDctosPromociones+"");
	}


	/**
	 * @param valorPresupuestoSinDctosPromociones the valorPresupuestoSinDctosPromociones to set
	 */
	public void setValorPresupuestoSinDctosPromociones(
			BigDecimal valorPresupuestoSinDctosPromociones) {
		this.valorPresupuestoSinDctosPromociones = valorPresupuestoSinDctosPromociones;
	}

	/**
	 * Obtiene el valor del atributo inclusion
	 *
	 * @return Retorna atributo inclusion
	 */
	public boolean isInclusion()
	{
		return inclusion;
	}

	/**
	 * Establece el valor del atributo inclusion
	 *
	 * @param valor para el atributo inclusion
	 */
	public void setInclusion(boolean inclusion)
	{
		this.inclusion = inclusion;
	}

	/**
	 * Obtiene el valor del atributo codigoPkEncabezadoInclusion
	 *
	 * @return Retorna atributo codigoPkEncabezadoInclusion
	 */
	public long getCodigoPkEncabezadoInclusion()
	{
		return codigoPkEncabezadoInclusion;
	}

	/**
	 * Establece el valor del atributo codigoPkEncabezadoInclusion
	 *
	 * @param valor para el atributo codigoPkEncabezadoInclusion
	 */
	public void setCodigoPkEncabezadoInclusion(long codigoPkEncabezadoInclusion)
	{
		this.codigoPkEncabezadoInclusion = codigoPkEncabezadoInclusion;
	}

	/**
	 * @param ordenamiento the ordenamiento to set
	 */
	public void setOrdenamiento(String ordenamiento) {
		this.ordenamiento = ordenamiento;
	}

	/**
	 * @return the ordenamiento
	 */
	public String getOrdenamiento() {
		return ordenamiento;
	}

	/**
	 * @param codigoPkAutorizacionPresuDctoOdon the codigoPkAutorizacionPresuDctoOdon to set
	 */
	public void setCodigoPkAutorizacionPresuDctoOdon(
			long codigoPkAutorizacionPresuDctoOdon) {
		this.codigoPkAutorizacionPresuDctoOdon = codigoPkAutorizacionPresuDctoOdon;
	}

	/**
	 * @return the codigoPkAutorizacionPresuDctoOdon
	 */
	public long getCodigoPkAutorizacionPresuDctoOdon() {
		return codigoPkAutorizacionPresuDctoOdon;
	}

	/**
	 * @return the codigoPkDetDescuentoOdonAten
	 */
	public BigDecimal getCodigoPkDetDescuentoOdonAten() {
		return codigoPkDetDescuentoOdonAten;
	}

	/**
	 * @param codigoPkDetDescuentoOdonAten the codigoPkDetDescuentoOdonAten to set
	 */
	public void setCodigoPkDetDescuentoOdonAten(
			BigDecimal codigoPkDetDescuentoOdonAten) {
		this.codigoPkDetDescuentoOdonAten = codigoPkDetDescuentoOdonAten;
	}

	/**
	 * @param acronimoIndicativoMotivo the acronimoIndicativoMotivo to set
	 */
	public void setAcronimoIndicativoMotivo(String acronimoIndicativoMotivo) {
		this.acronimoIndicativoMotivo = acronimoIndicativoMotivo;
	}

	/**
	 * @return the acronimoIndicativoMotivo
	 */
	public String getAcronimoIndicativoMotivo() {
		return acronimoIndicativoMotivo;
	}

	/**
	 * @param diasVigencia the diasVigencia to set
	 */
	public void setDiasVigencia(int diasVigencia) {
		this.diasVigencia = diasVigencia;
	}

	/**
	 * @return the diasVigencia
	 */
	public int getDiasVigencia() {
		return diasVigencia;
	}

	/**
	 * @param fechaAutorizacion the fechaAutorizacion to set
	 */
	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	/**
	 * @return the fechaAutorizacion
	 */
	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}
	
	
	
	
	public boolean isSoliGeneraPresupuesto() {
		return soliGeneraPresupuesto;
	}

	public void setSoliGeneraPresupuesto(boolean soliGeneraPresupuesto) {
		this.soliGeneraPresupuesto = soliGeneraPresupuesto;
	}
	
	public boolean isSoliGeneraInclusiones() {
		return soliGeneraInclusiones;
	}

	public void setSoliGeneraInclusiones(boolean soliGeneraInclusiones) {
		this.soliGeneraInclusiones = soliGeneraInclusiones;
	}
	
	
	public String getEstadoPresupuestos() {
		return estadoPresupuestos;
	}

	public void setEstadoPresupuestos(String estadoPresupuestos) {
		this.estadoPresupuestos = estadoPresupuestos;
	}

	public String getEstadoInclusion() {
		return estadoInclusion;
	}

	public void setEstadoInclusion(String estadoInclusion) {
		this.estadoInclusion = estadoInclusion;
	}

	/**
	 * @return the ingresoSoliDescuento
	 */
	public int getIngresoSoliDescuento() {
		return ingresoSoliDescuento;
	}

	/**
	 * @param ingresoSoliDescuento the ingresoSoliDescuento to set
	 */
	public void setIngresoSoliDescuento(int ingresoSoliDescuento) {
		this.ingresoSoliDescuento = ingresoSoliDescuento;
	}

	/**
	 * @return the nombreIndicativoMotivo
	 */
	public String getNombreIndicativoMotivo() {
		return nombreIndicativoMotivo;
	}

	/**
	 * @param nombreIndicativoMotivo the nombreIndicativoMotivo to set
	 */
	public void setNombreIndicativoMotivo(String nombreIndicativoMotivo) {
		this.nombreIndicativoMotivo = nombreIndicativoMotivo;
	}
	/*
	 * < 0 Si this < objetoComparacion
	 * > 0 Si objetoComparacion < this
	 * = 0 Si son iguales
	 */
	@Override
	public int compareTo(InfoDefinirSolucitudDsctOdon objetoComparacion) {
		return this.getCentroAtencion().getNombre().compareTo(objetoComparacion.getCentroAtencion().getNombre());
	}
}
