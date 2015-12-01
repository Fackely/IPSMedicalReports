package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import java.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import net.sf.jasperreports.engine.JRDataSource;


/**
 * Esta clase se encarga de recibir los datos creados en la consulta de las promociones odontológicas
 * y es utilizado en la generación del reporte
 * 
 * @author Fabian Becerra
 *
 */
public class DtoResultadoConsultaReportePromocionesOdontologicas implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Atributo donde se almacena el codigo pk de la promoción
	 */
	private int codigoPkPromocion;
	
	
	/**
	 * Atributo donde se almacena la region de la promoción
	 */
	private String region;
	
	
	/**
	 * Atributo donde se almacena la ciudad de la promoción
	 */
	private String ciudad;
	
	
	/**
	 * Atributo donde se almacena el nombre de la promoción
	 */
	private String nombrePromocion;
	
	
	/**
	 * Atributo donde se almacena la categoria del centro de atención
	 */
	private String categoriaAtencion;
	
	
	/**
	 * Atributo donde se almacena el codigo de la categoria del centro de atención
	 */
	private long codigoCategoriaAtencion;
	
	
	/**
	 * Atributo donde se almacena el centro de Atención para el que aplica la promoción Odontológica
	 */
	private String centroAtencion;
	
	
	/**
	 * Atributo donde se almacena el consecutivo del centro de 
	 * Atención para el que aplica la promoción Odontológica
	 */
	private int consecutivoCentroAtencion;
	
	
	/**
	 * Atributo donde se almacena el nombre del convenio para el que aplica la promoción Odontológica
	 */
	private String nombreConvenio;
	
	
	/**
	 * Atributo donde se almacena el codigo del convenio para el que aplica la promoción Odontológica
	 */
	private int codigoConvenio;
	
	
	/**
	 * Atributo donde se almacena el numero de hijos para los que aplica la promoción Odontológica
	 */
	private Integer nroHijos;
	
	
	/**
	 * Atributo donde se almacena el numero de hijos para los que aplica la promoción Odontológica
	 * en formato String para ser utilizado en el reporte pdf
	 */
	private String numeroHijos;
	
	
	/**
	 * Atributo donde se almacena la edad inicial en años desde donde aplica la promoción Odontológica
	 */
	private Integer edadInicial;
	
	
	/**
	 * Atributo donde se almacena la edad final en años hasta donde aplica la promoción Odontológica
	 */
	private Integer edadFinal;
	
	
	/**
	 * Atributo donde se concatena la edad inicial y final para ser utilizada en el reporte pdf
	 */
	private String rangoEdad;
	
	
	/**
	 * Atributo donde se almacena el sexo del paciente que aplica a la promoción odontológica
	 */
	private String sexo;
	
	
	/**
	 * Atributo donde se almacena el estado Civil del paciente que aplica a la promoción odontológica
	 */
	private String estadoCivil;
	
	
	/**
	 * Atributo donde se almacena las fechas y horas iniciales y finales de la vigencia de la promoción odontológica
	 * utilizado en el reporte pdf
	 */
	private String fechasHorasVigencia;
	
	
	/**
	 * Atributo donde se almacena la fecha inicial de vigencia de la promoción odontológica
	 */
	private Date fechaIniVigencia;
	
	
	/**
	 * Atributo donde se almacena la fecha final de vigencia de la promoción odontológica
	 */
	private Date fechaFinVigencia;
	
	
	/**
	 * Atributo donde se almacena la hora inicial de vigencia de la promoción odontológica
	 */
	private String horaIniVigencia;
	
	
	/**
	 * Atributo donde se almacena la hora final de vigencia de la promoción odontológica
	 */
	private String horaFinVigencia;
	
	
	/**
	 * Atributo donde se almacena el porcentaje de descuento de la promoción odontológica
	 */
	private BigDecimal porcentajeDescuento;
	
	
	/**
	 * Atributo donde se almacena el valor de descuento de la promoción odontológica
	 */
	private BigDecimal valorDescuento;
	
	
	/**
	 * Atributo donde se almacena el valor o porcentaje de descuento de la promoción dependiendo el que se ingresó
	 */
	private String ayudanteDescuento;
	
	
	/**
	 * Atributo donde se almacena el porcentaje de descuento de la promoción en formato string para utilizar en el 
	 * reporte archivo plano
	 */
	private String ayudantePorcentajeDescuento;
	
	
	/**
	 * Atributo donde se almacena el valor de descuento de la promoción en formato string para utilizar en el 
	 * reporte archivo plano
	 */
	private String ayudanteValorDescuento;
	
	
	/**
	 * Atributo donde se almacena el porcentaje honorario de la promoción odontológica
	 */
	private BigDecimal porcentajeHonorario;
	
	
	/**
	 * Atributo donde se almacena el valor honorario de la promoción odontológica
	 */
	private Long valorHonorario;
	
	
	/**
	 * Atributo donde se almacena el valor o porcentaje honorario de la promoción dependiendo el que se ingresó
	 */
	private String ayudanteHonorario;
	
	
	/**
	 * Atributo donde se almacena el porcentaje honorario de la promoción en formato string para utilizar en el 
	 * reporte archivo plano
	 */
	private String ayudantePorcentajeHonorario;
	
	
	/**
	 * Atributo donde se almacena el valor honorario de la promoción en formato string para utilizar en el 
	 * reporte archivo plano
	 */
	private String ayudanteValorHonorario;
	
	
	/**
	 * Atributo donde se almacena el código del programa para el que aplica la promoción odontológica
	 */
	private String codigoPrograma;
	
	
	/**
	 * Atributo donde se almacena la ocupación del paciente para el que aplica la promoción odontológica
	 */
	private String ocupacion;
	
	
	/**
	 * Atributo donde se almacena la fecha de grabación de la promoción odontológica
	 */
	private Date fechaModifica;
	
	
	/**
	 * Atributo donde se almacena la fecha de grabación de la promoción odontológica en formato string para
	 * ser utilizado en el reporte en archivo plano
	 */
	private String fechaModificaCadena;
	
	
	/**
	 * Atributo donde se almacena la hora de grabación de la promoción odontológica 
	 */
	private String horaModifica;
	
	
	/**
	 * Atributo donde se almacena el usuario grabación o el usuario que creó la promoción odontológica 
	 */
	private String loginUsuario;
	
	
	/**
	 * Atributo donde se almacena el estado de la promoción odontológica 
	 */
	private String estadoPromocion;
	
	
	
	 /** Objeto jasper para el subreporte de promociones*/
    private JRDataSource dsSubPromociones;
    
    
    /**
	  * Atributo que almacena los registros asociados por promoción
	  */
    private ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> listaPromociones;

    
    /**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoPkPromocion
 	 * 
 	 * @param codigoPkPromocion valor para el atributo codigoPkPromocion
 	 */
	public void setCodigoPkPromocion(int codigoPkPromocion) {
		this.codigoPkPromocion = codigoPkPromocion;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoPkPromocion
	 * 
	 * @return  Retorna la variable codigoPkPromocion
	 */
	public int getCodigoPkPromocion() {
		return codigoPkPromocion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo listaPromociones
 	 * 
 	 * @param listaPromociones valor para el atributo listaPromociones
 	 */
	public void setListaPromociones(ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> listaPromociones) {
		this.listaPromociones = listaPromociones;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo listaPromociones
	 * 
	 * @return  Retorna la variable listaPromociones
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> getListaPromociones() {
		return listaPromociones;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo dsSubPromociones
 	 * 
 	 * @param dsSubPromociones valor para el atributo dsSubPromociones
 	 */
	public void setDsSubPromociones(JRDataSource dsSubPromociones) {
		this.dsSubPromociones = dsSubPromociones;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo dsSubPromociones
	 * 
	 * @return  Retorna la variable dsSubPromociones
	 */
	public JRDataSource getDsSubPromociones() {
		return dsSubPromociones;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo region
 	 * 
 	 * @param region valor para el atributo region
 	 */
	public void setRegion(String region) {
		this.region = region;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo region, si el atributo ciudad y region son vacios
	 *  devuelve TODAS, devuelve "-" si la ciudad no es vacia
	 * 
	 * @return  Retorna la variable region
	 */
	public String getRegion() {
		if(UtilidadTexto.isEmpty(region)&&UtilidadTexto.isEmpty(ciudad)){
			this.region="TODAS";
		}
		else
		if(UtilidadTexto.isEmpty(region)&&!UtilidadTexto.isEmpty(ciudad)&&!ciudad.equals("TODAS")){
			this.region="-";
		}
		else
		if(UtilidadTexto.isEmpty(region)&&!UtilidadTexto.isEmpty(ciudad)&&ciudad.equals("TODAS")){
			this.region="TODAS";
		}
		return region;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ciudad
 	 * 
 	 * @param ciudad valor para el atributo ciudad
 	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ciudad, si el atributo ciudad y region son vacios
	 *  devuelve TODAS, devuelve "-" si la region no es vacia
	 * 
	 * @return  Retorna la variable ciudad
	 */
	public String getCiudad() {
		if(UtilidadTexto.isEmpty(ciudad)&&UtilidadTexto.isEmpty(region)){
			this.ciudad="TODAS";
		}else
		if(UtilidadTexto.isEmpty(ciudad)&&!UtilidadTexto.isEmpty(region)&&!region.equals("TODAS")){
			this.ciudad="-";
		}
		else
		if(UtilidadTexto.isEmpty(ciudad)&&!UtilidadTexto.isEmpty(region)&&region.equals("TODAS")){
			this.ciudad="TODAS";
		}
		return ciudad;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nombrePromocion
 	 * 
 	 * @param nombrePromocion valor para el atributo nombrePromocion
 	 */
	public void setNombrePromocion(String nombrePromocion) {
		this.nombrePromocion = nombrePromocion;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombrePromocion
	 * 
	 * @return  Retorna la variable nombrePromocion
	 */
	public String getNombrePromocion() {
		return nombrePromocion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo categoriaAtencion
 	 * 
 	 * @param categoriaAtencion valor para el atributo categoriaAtencion
 	 */
	public void setCategoriaAtencion(String categoriaAtencion) {
		this.categoriaAtencion = categoriaAtencion;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo categoriaAtencion, si este valor es 
	 *  vacio devuelve la etiqueta "-"
	 * 
	 * @return  Retorna la variable categoriaAtencion
	 */
	public String getCategoriaAtencion() {
		if(UtilidadTexto.isEmpty(categoriaAtencion)){
			categoriaAtencion="-";
		}
		return categoriaAtencion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo centroAtencion
 	 * 
 	 * @param centroAtencion valor para el atributo centroAtencion
 	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo centroAtencion, si este valor es 
	 *  vacio devuelve la etiqueta "TODOS"
	 * 
	 * @return  Retorna la variable centroAtencion
	 */
	public String getCentroAtencion() {
		if(UtilidadTexto.isEmpty(centroAtencion)){
			this.centroAtencion="TODOS";
		}
		return centroAtencion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nombreConvenio
 	 * 
 	 * @param nombreConvenio valor para el atributo nombreConvenio
 	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nombreConvenio, si este valor es 
	 *  vacio devuelve la etiqueta "TODOS"
	 * 
	 * @return  Retorna la variable nombreConvenio
	 */
	public String getNombreConvenio() {
		if(UtilidadTexto.isEmpty(nombreConvenio)){
			this.nombreConvenio="TODOS";
		}
		return nombreConvenio;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo nroHijos
 	 * 
 	 * @param nroHijos valor para el atributo nroHijos
 	 */
	public void setNroHijos(Integer nroHijos) {
		this.nroHijos = nroHijos;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo nroHijos
	 * 
	 * @return  Retorna la variable nroHijos
	 */
	public Integer getNroHijos() {
		return nroHijos;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo edadInicial
 	 * 
 	 * @param edadInicial valor para el atributo edadInicial
 	 */
	public void setEdadInicial(Integer edadInicial) {
		this.edadInicial = edadInicial;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo edadInicial
	 * 
	 * @return  Retorna la variable edadInicial
	 */
	public Integer getEdadInicial() {
		return edadInicial;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo edadFinal
 	 * 
 	 * @param edadInicial valor para el atributo edadFinal
 	 */
	public void setEdadFinal(Integer edadFinal) {
		this.edadFinal = edadFinal;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo edadFinal
	 * 
	 * @return  Retorna la variable edadFinal
	 */
	public Integer getEdadFinal() {
		return edadFinal;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo rangoEdad
 	 * 
 	 * @param rangoEdad valor para el atributo rangoEdad
 	 */
	public void setRangoEdad(String rangoEdad) {
		this.rangoEdad = rangoEdad;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo rangoEdad, concatenando los atributos
	 *  edadInicial y edadFinal, si ambos son nulos
	 *  devuelve la etiqueta "-"
	 * 
	 * @return  Retorna la variable rangoEdad
	 */
	public String getRangoEdad() {
		if(this.edadInicial!=null&&this.edadFinal!=null)
			this.rangoEdad=this.edadInicial.toString()+"-"+this.edadFinal.toString();
		else
			this.rangoEdad="-";
		return rangoEdad;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo sexo
 	 * 
 	 * @param sexo valor para el atributo sexo
 	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo sexo, si es vacio devuelve la etiqueta
	 *  "-", de lo contrario devuelve el acronimo de femenino 
	 *  o masculino respectivamente
	 * 
	 * @return  Retorna la variable sexo
	 */
	public String getSexo() {
		if(UtilidadTexto.isEmpty(sexo)){
			sexo="-";
		}else
		if(sexo.equals("Femenino")){
			sexo=ConstantesIntegridadDominio.acronimoFemenino;
		}
		else if(sexo.equals("Masculino")){
			sexo=ConstantesIntegridadDominio.acronimoMasculino;
		}
			
		return sexo;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo estadoCivil
 	 * 
 	 * @param estadoCivil valor para el atributo estadoCivil
 	 */
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estadoCivil, si es vacio devuelve
	 *  la etiqueta "-"
	 * 
	 * @return  Retorna la variable estadoCivil
	 */
	public String getEstadoCivil() {
		if(UtilidadTexto.isEmpty(estadoCivil))
			this.estadoCivil="-";
		return estadoCivil;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoCategoriaAtencion
 	 * 
 	 * @param codigoCategoriaAtencion valor para el atributo codigoCategoriaAtencion
 	 */
	public void setCodigoCategoriaAtencion(Long codigoCategoriaAtencion) {
		if(codigoCategoriaAtencion!=null){
			this.codigoCategoriaAtencion = codigoCategoriaAtencion;
		}else
			this.codigoCategoriaAtencion=0;
		
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoCategoriaAtencion
	 * 
	 * @return  Retorna la variable codigoCategoriaAtencion
	 */
	public long getCodigoCategoriaAtencion() {
		return codigoCategoriaAtencion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaIniVigencia
 	 * 
 	 * @param fechaIniVigencia valor para el atributo fechaIniVigencia
 	 */
	public void setFechaIniVigencia(Date fechaIniVigencia) {
		this.fechaIniVigencia = fechaIniVigencia;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaIniVigencia
	 * 
	 * @return  Retorna la variable fechaIniVigencia
	 */
	public Date getFechaIniVigencia() {
		return fechaIniVigencia;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaFinVigencia
 	 * 
 	 * @param fechaFinVigencia valor para el atributo fechaFinVigencia
 	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaFinVigencia
	 * 
	 * @return  Retorna la variable fechaFinVigencia
	 */
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaIniVigencia
 	 * 
 	 * @param horaIniVigencia valor para el atributo horaIniVigencia
 	 */
	public void setHoraIniVigencia(String horaIniVigencia) {
		this.horaIniVigencia = horaIniVigencia;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaIniVigencia
	 * 
	 * @return  Retorna la variable horaIniVigencia
	 */
	public String getHoraIniVigencia() {
		return horaIniVigencia;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaFinVigencia
 	 * 
 	 * @param horaFinVigencia valor para el atributo horaFinVigencia
 	 */
	public void setHoraFinVigencia(String horaFinVigencia) {
		this.horaFinVigencia = horaFinVigencia;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaFinVigencia
	 * 
	 * @return  Retorna la variable horaFinVigencia
	 */
	public String getHoraFinVigencia() {
		return horaFinVigencia;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechasHorasVigencia
 	 * 
 	 * @param fechasHorasVigencia valor para el atributo fechasHorasVigencia
 	 */
	public void setFechasHorasVigencia(String fechasHorasVigencia) {
		this.fechasHorasVigencia = fechasHorasVigencia;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechasHorasVigencia, concatenando los 
	 *  atributos fechaIniVigencia, fechaFinVigencia, horaIniVigencia,
	 *  horaFinVigencia, si los atributos son nulos devuelve
	 *  la etiqueta "-"
	 * 
	 * @return  Retorna la variable fechasHorasVigencia
	 */
	public String getFechasHorasVigencia() {
		String fechas="";
		if(this.fechaIniVigencia==null&&this.fechaFinVigencia==null){
			fechas="-";
		}else
		{
			String fechaViIni=UtilidadFecha.conversionFormatoFechaAAp(this.fechaIniVigencia);
			String fechaViFin=UtilidadFecha.conversionFormatoFechaAAp(this.fechaFinVigencia);
			fechas=fechaViIni+"-"+fechaViFin;
		}
		
		String horas="";
		if(UtilidadTexto.isEmpty(this.horaIniVigencia)&&UtilidadTexto.isEmpty(this.horaFinVigencia)){
			horas="-";
		}else
		{
			horas=this.horaIniVigencia+"-"+this.horaFinVigencia;
		}
		
		this.fechasHorasVigencia=fechas+" "+horas;
		
		return fechasHorasVigencia;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoConvenio
 	 * 
 	 * @param codigoConvenio valor para el atributo codigoConvenio
 	 */
	public void setCodigoConvenio(Integer codigoConvenio) {
		if(codigoConvenio!=null){
			this.codigoConvenio = codigoConvenio;
		}else
			this.codigoConvenio=0;
		
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoConvenio
	 * 
	 * @return  Retorna la variable codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo consecutivoCentroAtencion
 	 * 
 	 * @param consecutivoCentroAtencion valor para el atributo consecutivoCentroAtencion
 	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		if(consecutivoCentroAtencion!=null){
			this.consecutivoCentroAtencion = consecutivoCentroAtencion;
		}else
			this.consecutivoCentroAtencion = 0;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencion
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo numeroHijos
 	 * 
 	 * @param numeroHijos valor para el atributo numeroHijos
 	 */
	public void setNumeroHijos(String numeroHijos) {
		this.numeroHijos = numeroHijos;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo numeroHijos, si es nulo devuelve
	 *  la etiqueta "-"
	 * 
	 * @return  Retorna la variable numeroHijos
	 */
	public String getNumeroHijos() {
		if(nroHijos!=null)
		{
			this.numeroHijos=nroHijos.toString();
		}
		else
		{
			this.numeroHijos="-";
		}
		return numeroHijos;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo porcentajeDescuento
 	 * 
 	 * @param porcentajeDescuento valor para el atributo porcentajeDescuento
 	 */
	public void setPorcentajeDescuento(BigDecimal porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo porcentajeDescuento
	 * 
	 * @return  Retorna la variable porcentajeDescuento
	 */
	public BigDecimal getPorcentajeDescuento() {
		return porcentajeDescuento;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo valorDescuento
 	 * 
 	 * @param valorDescuento valor para el atributo valorDescuento
 	 */
	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo valorDescuento
	 * 
	 * @return  Retorna la variable valorDescuento
	 */
	public BigDecimal getValorDescuento() {
		return valorDescuento;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo porcentajeHonorario
 	 * 
 	 * @param porcentajeHonorario valor para el atributo porcentajeHonorario
 	 */
	public void setPorcentajeHonorario(BigDecimal porcentajeHonorario) {
		this.porcentajeHonorario = porcentajeHonorario;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo porcentajeHonorario
	 * 
	 * @return  Retorna la variable porcentajeHonorario
	 */
	public BigDecimal getPorcentajeHonorario() {
		return porcentajeHonorario;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo valorHonorario
 	 * 
 	 * @param valorHonorario valor para el atributo valorHonorario
 	 */
	public void setValorHonorario(Long valorHonorario) {
		this.valorHonorario = valorHonorario;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo valorHonorario
	 * 
	 * @return  Retorna la variable valorHonorario
	 */
	public Long getValorHonorario() {
		return valorHonorario;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ayudanteDescuento
 	 * 
 	 * @param ayudanteDescuento valor para el atributo ayudanteDescuento
 	 */
	public void setAyudanteDescuento(String ayudanteDescuento) {
		this.ayudanteDescuento = ayudanteDescuento;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ayudanteDescuento, si el valor de descuento
	 *  es 0,00 devuelve la etiqueta "-", sino crea el formato con
	 *  dos cifras decimales del valor descuento
	 * 
	 * @return  Retorna la variable ayudanteDescuento
	 */
	public String getAyudanteDescuento() {
		if(porcentajeDescuento==null&&valorDescuento==null){
			this.ayudanteDescuento="";
		}else
		if(porcentajeDescuento==null){
			BigDecimal m= new BigDecimal(this.valorDescuento.doubleValue());
			NumberFormat formatter = new DecimalFormat("###,##0.00");
			String number=formatter.format(m);
			if(number.equals("0,00")){
				this.ayudanteDescuento="-";
			}
			else{
				this.ayudanteDescuento="$"+number;
			}
		}
		else{
			this.ayudanteDescuento=Integer.toString(this.porcentajeDescuento.intValue())+"%";
		}
		return ayudanteDescuento;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ayudanteHonorario
 	 * 
 	 * @param ayudanteHonorario valor para el atributo ayudanteHonorario
 	 */
	public void setAyudanteHonorario(String ayudanteHonorario) {
		this.ayudanteHonorario = ayudanteHonorario;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ayudanteHonorario, si el valor honorario
	 *  es 0,00 devuelve la etiqueta "-", sino crea el formato con
	 *  dos cifras decimales del valor honorario
	 * 
	 * @return  Retorna la variable ayudanteHonorario
	 */
	public String getAyudanteHonorario() {
		if(porcentajeHonorario==null&&valorHonorario==null){
			this.ayudanteHonorario="-";
		}else
		if(porcentajeHonorario==null){
			BigDecimal m= new BigDecimal(this.valorHonorario.doubleValue());
			NumberFormat formatter = new DecimalFormat("###,##0.00");
			String number=formatter.format(m);
			if(number.equals("0,00")){
				this.ayudanteHonorario="-";
			}
			else{
				this.ayudanteHonorario="$"+number;
			}
		}
		else{
			this.ayudanteHonorario=Integer.toString(this.porcentajeHonorario.intValue())+"%";
		}
		return ayudanteHonorario;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo codigoPrograma
 	 * 
 	 * @param codigoPrograma valor para el atributo codigoPrograma
 	 */
	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo codigoPrograma
	 * 
	 * @return  Retorna la variable codigoPrograma
	 */
	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ocupacion
 	 * 
 	 * @param ocupacion valor para el atributo ocupacion
 	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ocupacion, si la ocupacion esta
	 *  vacia devuelve la etiqueta "-"
	 * 
	 * @return  Retorna la variable ocupacion
	 */
	public String getOcupacion() {
		if(UtilidadTexto.isEmpty(ocupacion))
		{
			this.ocupacion="-";
		}
		return ocupacion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaModifica
 	 * 
 	 * @param fechaModifica valor para el atributo fechaModifica
 	 */
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaModifica
	 * 
	 * @return  Retorna la variable fechaModifica
	 */
	public Date getFechaModifica() {
		return fechaModifica;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo horaModifica
 	 * 
 	 * @param horaModifica valor para el atributo horaModifica
 	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo horaModifica
	 * 
	 * @return  Retorna la variable horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo fechaModificaCadena
 	 * 
 	 * @param fechaModificaCadena valor para el atributo fechaModificaCadena
 	 */
	public void setFechaModificaCadena(String fechaModificaCadena) {
		this.fechaModificaCadena = fechaModificaCadena;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaModificaCadena, conviertiendo el atributo
	 *  fechaModifica en String con formato dd/MM/yyyy
	 * 
	 * @return  Retorna la variable fechaModificaCadena
	 */
	public String getFechaModificaCadena() {
		if(fechaModifica!=null)
			this.fechaModificaCadena=UtilidadFecha.conversionFormatoFechaAAp(this.fechaModifica);
		return fechaModificaCadena;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo loginUsuario
 	 * 
 	 * @param loginUsuario valor para el atributo loginUsuario
 	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo loginUsuario
	 * 
	 * @return  Retorna la variable loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo estadoPromocion
 	 * 
 	 * @param estadoPromocion valor para el atributo estadoPromocion
 	 */
	public void setEstadoPromocion(String estadoPromocion) {
		this.estadoPromocion = estadoPromocion;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estadoPromocion, si el estado de la promoción es S
	 *  devuelve Activo, si es N devuelve Inactivo.
	 * 
	 * @return  Retorna la variable estadoPromocion
	 */
	public String getEstadoPromocion() {
		if(estadoPromocion.equals(ConstantesBD.acronimoSi)){
			this.estadoPromocion="Activo";
		}else
		if(estadoPromocion.equals(ConstantesBD.acronimoNo)){
			this.estadoPromocion="Inactivo";
		}
		return estadoPromocion;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ayudantePorcentajeDescuento
 	 * 
 	 * @param ayudantePorcentajeDescuento valor para el atributo ayudantePorcentajeDescuento
 	 */
	public void setAyudantePorcentajeDescuento(
			String ayudantePorcentajeDescuento) {
		this.ayudantePorcentajeDescuento = ayudantePorcentajeDescuento;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ayudantePorcentajeDescuento, este método obtiene el valor
	 *  entero de el atributo porcentajeDescuento, si es nulo este valor
	 *  devuelve la etiqueta "-"
	 * 
	 * @return  Retorna la variable ayudantePorcentajeDescuento
	 */
	public String getAyudantePorcentajeDescuento() {
		if(this.porcentajeDescuento!=null){
			int porcentaje=this.porcentajeDescuento.intValue();
			this.ayudantePorcentajeDescuento=Integer.toString(porcentaje);
		}else
			this.ayudantePorcentajeDescuento="-";
		
		return ayudantePorcentajeDescuento;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ayudanteValorDescuento
 	 * 
 	 * @param ayudanteValorDescuento valor para el atributo ayudanteValorDescuento
 	 */
	public void setAyudanteValorDescuento(String ayudanteValorDescuento) {
		this.ayudanteValorDescuento = ayudanteValorDescuento;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ayudanteValorDescuento, convierte el atributo
	 *  valorDescuento en formato con dos decimales, si este valor es 
	 *  0,00 devuelve la etiqueta "-"
	 * 
	 * @return  Retorna la variable ayudanteValorDescuento
	 */
	public String getAyudanteValorDescuento() {
		if(this.valorDescuento!=null){
			BigDecimal m= new BigDecimal(this.valorDescuento.doubleValue());
			NumberFormat formatter = new DecimalFormat("###,##0.00");
			String number=formatter.format(m);
			if(number.equals("0,00")){
				this.ayudanteValorDescuento="";
			}
			else{
				this.ayudanteValorDescuento=number;
			}
		}else
		{
			this.ayudanteValorDescuento="-";
		}
		return ayudanteValorDescuento;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ayudantePorcentajeHonorario
 	 * 
 	 * @param ayudantePorcentajeHonorario valor para el atributo ayudantePorcentajeHonorario
 	 */
	public void setAyudantePorcentajeHonorario(
			String ayudantePorcentajeHonorario) {
		this.ayudantePorcentajeHonorario = ayudantePorcentajeHonorario;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ayudantePorcentajeHonorario, que obtiene el valor
	 *  entero del atributo porcentajeHonorario, si este valor es nulo
	 *  devuelve la etiqueta "-"
	 * 
	 * @return  Retorna la variable ayudantePorcentajeHonorario
	 */
	public String getAyudantePorcentajeHonorario() {
		if(this.porcentajeHonorario!=null){
			int porcentaje=this.porcentajeHonorario.intValue();
			this.ayudantePorcentajeHonorario=Integer.toString(porcentaje);
		}else
			this.ayudantePorcentajeHonorario="-";
		return ayudantePorcentajeHonorario;
	}

	
	/**
 	 * Método que se encarga de establecer el valor 
 	 *  del atributo ayudanteValorHonorario
 	 * 
 	 * @param ayudanteValorHonorario valor para el atributo ayudanteValorHonorario
 	 */
	public void setAyudanteValorHonorario(String ayudanteValorHonorario) {
		this.ayudanteValorHonorario = ayudanteValorHonorario;
	}

	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo ayudanteValorHonorario, convierte el atributo
	 *  valorHonorario en formato con dos decimales, si este valor es 
	 *  0,00 devuelve la etiqueta "-"
	 * 
	 * @return  Retorna la variable ayudanteValorHonorario
	 */
	public String getAyudanteValorHonorario() {
		if(this.valorHonorario!=null){
			BigDecimal m= new BigDecimal(this.valorHonorario.doubleValue());
			NumberFormat formatter = new DecimalFormat("###,##0.00");
			String number=formatter.format(m);
			if(number.equals("0,00")){
				this.ayudanteValorHonorario="";
			}
			else{
				this.ayudanteValorHonorario=number;
			}
		}
		else{
			this.ayudanteValorHonorario="-";
		}
		return ayudanteValorHonorario;
	}



	
	
	

}
