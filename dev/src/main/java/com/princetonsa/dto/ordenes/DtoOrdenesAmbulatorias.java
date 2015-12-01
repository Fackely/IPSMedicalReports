package com.princetonsa.dto.ordenes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

public class DtoOrdenesAmbulatorias implements Serializable,Comparable<DtoOrdenesAmbulatorias>{
	
	/** */
	private static final long serialVersionUID = 1L;

    
    /** Informacion General*/
	private int institucion;
	private String razonSocial;
	private String nit;
	private String direccion;	
	private String telefono;
	private String indicativo;
	private String actividadEconomica;	
	
	private String titulo;
	private String tituloCE;
	public String getTituloCE() {
		return tituloCE;
	}

	public void setTituloCE(String tituloCE) {
		this.tituloCE = tituloCE;
	}

	public int tipoReportesArt=0; // para identificar si hay ordenes con medicamentos de control 

	public int getTipoReportesArt() {
		return tipoReportesArt;
	}

	public void setTipoReportesArt(int tipoReportesArt) {
		this.tipoReportesArt = tipoReportesArt;
	}

	private String numeroOrden;
	private String consecutivoOrden;
	private String tipoOrden;
	private Date fechaOrden;
	private String usuario;	
	private Byte estadoOrden;
	private String estadoOrdenStr;
	private String profesional;
	private String profesionalCE;
	public String getProfesionalCE() {
		return profesionalCE;
	}

	public void setProfesionalCE(String profesionalCE) {
		this.profesionalCE = profesionalCE;
	}


	private String especialidad;	
	private Boolean urgente;
	private String urgenteStr;
	private String hora;
	private String observaciones;	
	private Character controlEspecial;	
	private String resultado;
	private String motivoAnulacion;
	private String horaAnulacion;
	private Date fechaAnulacion;
	private String primerNombreAnulacion;
	private String primerApellidoAnulacion;
	private String segundoNombreAnulacion;
	private String segundoApellidoAnulacion;
	private boolean hayAnulacion;
	private String centroAtencion;
	
	/**MT 3725 Se adiciona en la impresión el pie de página Ordenes Amb de Medicamentos*/
	private String piePagAmbMed;
	
	/**
	 * Inc 1656
	 * Se debe mostrar si la orden es de PYP
	 * Diana Ruiz	
	 */	
	private boolean pyp;	
	
	
	/** Almacena la informacion de otros servicios, medicamentos o insumos*/
	private String otros;
	private String firmaDigital;	
	private String loginMedico;
	private String primerNombreMedico;
	private String primerApellidoMedico;
	private String segundoNombreMedico;
	private String segundoApellidoMedico;
	private String registroMedico;
	private String tipoIdMedico;
	private String numeroIdMedico;
	private String firmaDigitalMedico;
	
	public String getTipoIdMedico() {
		return tipoIdMedico;
	}

	public void setTipoIdMedico(String tipoIdMedico) {
		this.tipoIdMedico = tipoIdMedico;
	}

	public String getNumeroIdMedico() {
		return numeroIdMedico;
	}

	public void setNumeroIdMedico(String numeroIdMedico) {
		this.numeroIdMedico = numeroIdMedico;
	}

	
	/**Informacion Paciente*/
	private String paciente;
	private String tipoId;
	private String numeroId;
	private String primerNombre;
	private String segundoNombre;
	private String primerApellido;
	private String segundoApellido;
	private Date fechaNacimiento;
	private String sexo;
	private String dirPaciente;
	private String mun;
	private String dpto;
	private String historia;
	private String regimen;
	private String telefonoPersona;
	private String ingreso;
	private Integer cuenta;
	private String convenio;
	private String tipoAfiliado;
	private Integer tipoCieDx;
	private String acronimoDx;
	private String dx;
	private String categoria;

	
	public String getRegimen() {
		return regimen;
	}

	public void setRegimen(String regimen) {
		this.regimen = regimen;
	}
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDirPaciente() {
		return dirPaciente;
	}

	public void setDirPaciente(String dirPaciente) {
		this.dirPaciente = dirPaciente;
	}

	public String getMun() {
		return mun;
	}

	public void setMun(String mun) {
		this.mun = mun;
	}

	public String getDpto() {
		return dpto;
	}

	public void setDpto(String dpto) {
		this.dpto = dpto;
	}

	public String getHistoria() {
		return historia;
	}

	public void setHistoria(String historia) {
		this.historia = historia;
	}

	/********SERVICIOS************* */
	private Integer codigoServicio;	
	private String nombreservicio;
	private String cups;
	private Boolean esPos;
	private String esPosStr;
	private String finalidadServicio;
	private String tipoServicio;
	private Short cantidad;	
	private Boolean consultaExterna;
	private String codigoPropietario;
	
	/********ARTICULOS************* */
	private Integer codigoArticulo;
	private String codigoInterfaz;
	private String nombreArticulo;
	private String unidadMedida;
	private String dosis;
	private String via;
	private Long frecuencia;
	private String forma;
	private String tipoFrecuencia;
	private Long cantidadArticulo;
	private String cantidadArticuloLetras;
	private Boolean medicamento;
	private Integer diasTratamiento;
	private String observacionesMedicamentos;
	private String unidadMedidaDosis;
	private BigDecimal cantidadUnidadMedidaDosis;
	/**
	 * Inc 1869
	 * Mostrar la concentración del articulo y la palabra cada en la frecuencia.
	 * Diana Ruiz
	 */	
	private String concentracion;
	
	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}
	public String getCantidadArticuloLetras() {
		return cantidadArticuloLetras;
	}

	public void setCantidadArticuloLetras(String cantidadArticuloLetras) {
		this.cantidadArticuloLetras = cantidadArticuloLetras;
	}

	
	/****PARAMETROS BUSQUEDA****** */
	private ArrayList<String>parametrosOrdenesAmbulatorias;
	
	/****FILTRO PARA REPORTE**/	
	private long codigoEmpresaInstitucion;
	private String rutaLogo;
	private String ubicacionLogo;
	private String logoIzquierda;
	private String logoDerecha;
	private String nombreArchivoGenerado;
    private boolean saltoPaginaReporte;
    private String tipoImpresion;
	

	/**Agrupa las ordenes con igual numero de orden*/
	private ArrayList <DtoOrdenesAmbulatorias> agrupaOrdenesAmabul; 
	
	/****CARACTERISTICA DE AGRUPAMIENTO POR HOJA****/
	private String caracteristicaAgrupa;
	
	
	/**listas para enviar al dataSource*/
	private ArrayList<DtoOrdenesAmbulatorias>listaServicios;
		private ArrayList<DtoOrdenesAmbulatorias>listaServiciosOrdenes;
		private ArrayList<DtoOrdenesAmbulatorias>listaServiciosOtros;
	
	private ArrayList<DtoOrdenesAmbulatorias>listaArticulos;
		private ArrayList<DtoOrdenesAmbulatorias>listaArticulosMedicamentos;
		private ArrayList<DtoOrdenesAmbulatorias>listaArticulosInsumos;
		private ArrayList<DtoOrdenesAmbulatorias>listaArticulosOtros;
	
	/**DataSource para impresion de Ordenes Ambulatorias*/	
	private JRDataSource JRDDtoGeneralOrdenesServicios;
	private JRDataSource JRDDtoGeneralOrdenesArticulos;


	private JRDataSource JRDlistaServicios;
    	private JRDataSource JRDlistaServiciosOrdenes;
    	private JRDataSource JRDlistaServiciosOtros;
	private JRDataSource JRDlistaArticulos;
    	private JRDataSource JRDlistaArticulosMedicamentos;
    	private JRDataSource JRDlistaArticulosInsumos;
    	private JRDataSource JRDlistaArticulosOtros;
    private JRDataSource JRDlistaArticulosCE; // pára articulos con controlEspecial
	private JRDataSource JRDlistaArticulosMedicamentosCE;
	private JRDataSource JRDlistaArticulosInsumosCE;
	private JRDataSource JRDlistaArticulosOtrosCE;
	public JRDataSource getJRDlistaArticulosCE() {
		return JRDlistaArticulosCE;
	}

	public JRDataSource getJRDlistaArticulosMedicamentosCE() {
		return JRDlistaArticulosMedicamentosCE;
	}

	public void setJRDlistaArticulosMedicamentosCE(
			JRDataSource jRDlistaArticulosMedicamentosCE) {
		JRDlistaArticulosMedicamentosCE = jRDlistaArticulosMedicamentosCE;
	}

	public JRDataSource getJRDlistaArticulosInsumosCE() {
		return JRDlistaArticulosInsumosCE;
	}

	public void setJRDlistaArticulosInsumosCE(
			JRDataSource jRDlistaArticulosInsumosCE) {
		JRDlistaArticulosInsumosCE = jRDlistaArticulosInsumosCE;
	}

	public JRDataSource getJRDlistaArticulosOtrosCE() {
		return JRDlistaArticulosOtrosCE;
	}

	public void setJRDlistaArticulosOtrosCE(JRDataSource jRDlistaArticulosOtrosCE) {
		JRDlistaArticulosOtrosCE = jRDlistaArticulosOtrosCE;
	}

	public void setJRDlistaArticulosCE(JRDataSource jRDlistaArticulosCE) {
		JRDlistaArticulosCE = jRDlistaArticulosCE;
	}


	private String formatoMediaCarta;
	
	
	/** Metodo constructor de la clase*/
	public DtoOrdenesAmbulatorias()
	{
		this.reset();
	}
	
	private void reset()
	{		
		/** Informacion General*/
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.tipoOrden="";
		this.razonSocial="";
		this.nit="";
		this.direccion="";
		this.telefono="";
		this.indicativo="";
		this.actividadEconomica="";
		this.profesional="";
		this.especialidad="";
		this.numeroOrden="";
		this.consecutivoOrden="";
		this.urgente=false;
		this.urgenteStr="";
		this.fechaOrden=new Date();
		this.hora="";
		this.observaciones="";
		this.usuario="";		
		this.controlEspecial='N';
		this.titulo="";
		this.estadoOrden=new Byte((byte)0);
		this.resultado="";
		this.motivoAnulacion="";
		this.horaAnulacion="";
		this.fechaAnulacion=new Date();
		this.primerNombreAnulacion="";
		this.primerApellidoAnulacion="";
		this.segundoNombreAnulacion="";
		this.segundoApellidoAnulacion="";
		this.hayAnulacion=false;
		this.pyp=false;
		this.otros="";
		this.centroAtencion="";
		this.firmaDigital="";
		
		this.piePagAmbMed="";
		
		this.loginMedico="";
		this.primerNombreMedico="";
		this.primerApellidoMedico="";
		this.segundoNombreMedico="";
		this.segundoApellidoMedico="";
		this.registroMedico="";
		this.firmaDigitalMedico="";
		
		/**Informacion Paciente*/
		this.paciente="";
		this.tipoId="";
		this.numeroId="";
		this.primerNombre="";
		this.segundoNombre="";
		this.primerApellido="";
		this.segundoApellido="";
		this.fechaNacimiento=new Date();
		this.telefonoPersona="";
		this.ingreso="";
		this.cuenta=new Integer(0);
		this.convenio="";
		this.categoria="";
		this.tipoAfiliado="";
		this.tipoCieDx=new Integer(0);
		this.acronimoDx="";
		this.dx="";
		
		/********SERVICIOS************* */
		this.codigoServicio=ConstantesBD.codigoNuncaValido;	
		this.setNombreservicio("");
		this.setCups("");
		this.setEsPos(false);
		this.esPosStr="";
		this.finalidadServicio="";
		this.tipoServicio="";
		this.cantidad=new Short((short) 0);	
		this.consultaExterna=false;
		this.codigoPropietario="";
		this.especialidad="";
		
		/********ARTICULOS************* */
		this.codigoArticulo=ConstantesBD.codigoNuncaValido;
		this.codigoInterfaz="";
		this.nombreArticulo="";
		this.unidadMedida="";
		this.dosis="";
		this.via="";
		this.frecuencia=new Long(0);
		this.tipoFrecuencia="";
		this.cantidadArticulo=new Long(0L);
		this.medicamento=false;
		this.diasTratamiento=new Integer(0);
		this.observacionesMedicamentos="";
		this.unidadMedidaDosis="";
		this.cantidadUnidadMedidaDosis= new BigDecimal(0);
		this.concentracion="";
		
		this.parametrosOrdenesAmbulatorias=new ArrayList<String>();
		this.agrupaOrdenesAmabul=new ArrayList<DtoOrdenesAmbulatorias>();
		
		/****FILTRO PARA REPORTE**/	
		this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValidoLong;
		this.rutaLogo="";
		this.ubicacionLogo="";
		this.setLogoIzquierda("");
		this.setLogoDerecha("");
		this.nombreArchivoGenerado="";
		this.saltoPaginaReporte=false;
		this.tipoImpresion="";
		
		/**Caracteristica de agrupamiento de grupo por hoja*/
		this.caracteristicaAgrupa="";
		
		/**listas para enviar al dataSource*/
		this.listaServicios=new ArrayList<DtoOrdenesAmbulatorias>();
		this.listaServiciosOrdenes=new ArrayList<DtoOrdenesAmbulatorias>();
		this.listaServiciosOtros=new ArrayList<DtoOrdenesAmbulatorias>();
		
		this.listaArticulos=new ArrayList<DtoOrdenesAmbulatorias>();
		this.listaArticulosMedicamentos=new ArrayList<DtoOrdenesAmbulatorias>();
		this.listaArticulosInsumos=new ArrayList<DtoOrdenesAmbulatorias>();
		this.listaArticulosOtros=new ArrayList<DtoOrdenesAmbulatorias>();
		
		this.formatoMediaCarta=ConstantesBD.acronimoNo;
	}
	
	

	/**Metodo de ordenamiento para tipoOrden-fehaOrden-usuario-estado*/
	public int compareTo(DtoOrdenesAmbulatorias o) 
    {
    	DtoOrdenesAmbulatorias dtoConsulta = o;    
        
        if(this.tipoOrden.compareToIgnoreCase(dtoConsulta.tipoOrden) == 0)
        {
        	if(this.fechaOrden.compareTo(dtoConsulta.fechaOrden) == 0)
        	{
        		if(this.loginMedico.compareToIgnoreCase(dtoConsulta.loginMedico) == 0)
        		{
        			
        			if(this.centroAtencion.compareToIgnoreCase(dtoConsulta.centroAtencion) == 0)
        			{
        				return this.estadoOrden.compareTo(dtoConsulta.estadoOrden);
        				
        			}else
        			{
        				return this.centroAtencion.compareToIgnoreCase(dtoConsulta.centroAtencion);
        			}        			
        		}else
        		{
        			return this.loginMedico.compareToIgnoreCase(this.loginMedico);
        		}
        	}else
        	{
       			return this.fechaOrden.compareTo(dtoConsulta.fechaOrden);
        	}        	
        }else{
        	return this.tipoOrden.compareToIgnoreCase(dtoConsulta.tipoOrden);
        }
    }
	
	public String toString(){
		return this.tipoOrden+"-"+this.fechaOrden+"-"+this.loginMedico+"-"+this.centroAtencion+"-"+this.estadoOrdenStr;
	}
	
	public void setCaracteristicaAgrupa(String caracteristicaAgrupa) {
		this.caracteristicaAgrupa = caracteristicaAgrupa;
	}

	public String getCaracteristicaAgrupa() {
		return caracteristicaAgrupa;
	}
	
	
	public int getInstitucion() {
		return institucion;
	}
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public String getCentroAtencion() {
		return centroAtencion;
	}
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	public String getProfesional() {
		return profesional;
	}
	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}
	public String getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}
	public String getNumeroOrden() {
		return numeroOrden;
	}
	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}
	public Boolean isUrgente() {
		return urgente;
	}
	public void setUrgente(Boolean urgente) {
		this.urgente = urgente;
	}
	public Date getFechaOrden() {
		return fechaOrden;
	}
	public void setFechaOrden(Date fechaOrden) {
		this.fechaOrden = fechaOrden;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public Integer getCodigoServicio() {
		return codigoServicio;
	}
	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	public String getFinalidadServicio() {
		return finalidadServicio;
	}
	public void setFinalidadServicio(String finalidadServicio) {
		this.finalidadServicio = finalidadServicio;
	}
	public String getTipoServicio() {
		return tipoServicio;
	}
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	public Short getCantidad() {
		return cantidad;
	}
	public void setCantidad(Short cantidad) {
		this.cantidad = cantidad;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Boolean isConsultaExterna() {
		return consultaExterna;
	}
	public void setConsultaExterna(Boolean consultaExterna) {
		this.consultaExterna = consultaExterna;
	}
	public Byte getEstadoOrden() {
		return estadoOrden;
	}
	public void setEstadoOrden(Byte estadoOrden) {
		this.estadoOrden = estadoOrden;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}
	public String getOtros() {
		return otros;
	}
	public void setOtros(String otros) {
		this.otros = otros;
	}

	public void setNombreservicio(String nombreservicio) {
		this.nombreservicio = nombreservicio;
	}

	public String getNombreservicio() {
		return nombreservicio;
	}

	public void setCups(String cups) {
		this.cups = cups;
	}

	public String getCups() {
		return cups;
	}

	public void setEsPos(Boolean esPos) {
		this.esPos = esPos;
	}

	public Boolean isEsPos() {
		return esPos;
	}

	public String getNombreArticulo() {
		return nombreArticulo;
	}

	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public String getDosis() {
		return dosis;
	}

	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public Long getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(Long frecuencia) {
		this.frecuencia = frecuencia;
	}

	public String getTipoFrecuencia() {
		return tipoFrecuencia;
	}

	public void setTipoFrecuencia(String tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}

	public Long getCantidadArticulo() {
		return cantidadArticulo;
	}

	public void setCantidadArticulo(Long cantidadArticulo) {
		this.cantidadArticulo = cantidadArticulo;
	}	

	public Integer getDiasTratamiento() {
		return diasTratamiento;
	}

	public void setDiasTratamiento(Integer diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}

	public ArrayList<String> getParametrosOrdenesAmbulatorias() {
		return parametrosOrdenesAmbulatorias;
	}

	public void setParametrosOrdenesAmbulatorias(ArrayList<String> parametrosOrdenesAmbulatorias) {
		this.parametrosOrdenesAmbulatorias = parametrosOrdenesAmbulatorias;
	}

	public void setCodigoPropietario(String codigoPropietario) {
		this.codigoPropietario = codigoPropietario;
	}

	public String getCodigoPropietario() {
		return codigoPropietario;
	}

	public void setAgrupaOrdenesAmabul(ArrayList <DtoOrdenesAmbulatorias> agrupaOrdenesAmabul) {
		this.agrupaOrdenesAmabul = agrupaOrdenesAmabul;
	}

	public ArrayList <DtoOrdenesAmbulatorias> getAgrupaOrdenesAmabul() {
		return agrupaOrdenesAmabul;
	}

	public void setControlEspecial(Character controlEspecial) {
		this.controlEspecial = controlEspecial;
	}

	public Character getControlEspecial() {
		return controlEspecial;
	}
	
	public Boolean getConsultaExterna() {
		return consultaExterna;
	}

	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}

	

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getNit() {				
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTipoId() {
		return tipoId;
	}

	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	public String getNumeroId() {
		return numeroId;
	}

	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getIngreso() {
		return ingreso;
	}

	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	public Integer getCuenta() {
		return cuenta;
	}

	public void setCuenta(Integer cuenta) {
		this.cuenta = cuenta;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaServicios() {
		return listaServicios;
	}

	public void setListaServicios(ArrayList<DtoOrdenesAmbulatorias> listaServicios) {
		this.listaServicios = listaServicios;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaServiciosOrdenes() {
		return listaServiciosOrdenes;
	}

	public void setListaServiciosOrdenes(
			ArrayList<DtoOrdenesAmbulatorias> listaServiciosOrdenes) {
		this.listaServiciosOrdenes = listaServiciosOrdenes;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaServiciosOtros() {
		return listaServiciosOtros;
	}

	public void setListaServiciosOtros(
			ArrayList<DtoOrdenesAmbulatorias> listaServiciosOtros) {
		this.listaServiciosOtros = listaServiciosOtros;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaArticulos() {
		return listaArticulos;
	}

	public void setListaArticulos(ArrayList<DtoOrdenesAmbulatorias> listaArticulos) {
		this.listaArticulos = listaArticulos;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaArticulosMedicamentos() {
		return listaArticulosMedicamentos;
	}

	public void setListaArticulosMedicamentos(
			ArrayList<DtoOrdenesAmbulatorias> listaArticulosMedicamentos) {
		this.listaArticulosMedicamentos = listaArticulosMedicamentos;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaArticulosInsumos() {
		return listaArticulosInsumos;
	}

	public void setListaArticulosInsumos(
			ArrayList<DtoOrdenesAmbulatorias> listaArticulosInsumos) {
		this.listaArticulosInsumos = listaArticulosInsumos;
	}

	public ArrayList<DtoOrdenesAmbulatorias> getListaArticulosOtros() {
		return listaArticulosOtros;
	}

	public void setListaArticulosOtros(
			ArrayList<DtoOrdenesAmbulatorias> listaArticulosOtros) {
		this.listaArticulosOtros = listaArticulosOtros;
	}

	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	public String getRutaLogo() {
		return rutaLogo;
	}

	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	public String getLogoDerecha() {
		return logoDerecha;
	}

	public JRDataSource getJRDlistaServicios() {
		return JRDlistaServicios;
	}

	public void setJRDlistaServicios(JRDataSource jRDlistaServicios) {
		JRDlistaServicios = jRDlistaServicios;
	}

	public JRDataSource getJRDlistaServiciosOrdenes() {
		return JRDlistaServiciosOrdenes;
	}

	public void setJRDlistaServiciosOrdenes(JRDataSource jRDlistaServiciosOrdenes) {
		JRDlistaServiciosOrdenes = jRDlistaServiciosOrdenes;
	}

	public JRDataSource getJRDlistaServiciosOtros() {
		return JRDlistaServiciosOtros;
	}

	public void setJRDlistaServiciosOtros(JRDataSource jRDlistaServiciosOtros) {
		JRDlistaServiciosOtros = jRDlistaServiciosOtros;
	}

	public JRDataSource getJRDlistaArticulos() {
		return JRDlistaArticulos;
	}

	public void setJRDlistaArticulos(JRDataSource jRDlistaArticulos) {
		JRDlistaArticulos = jRDlistaArticulos;
	}

	public JRDataSource getJRDlistaArticulosMedicamentos() {
		return JRDlistaArticulosMedicamentos;
	}

	public void setJRDlistaArticulosMedicamentos(JRDataSource jRDlistaArticulosMedicamentos) {
		JRDlistaArticulosMedicamentos = jRDlistaArticulosMedicamentos;
	}

	public JRDataSource getJRDlistaArticulosInsumos() {
		return JRDlistaArticulosInsumos;
	}

	public void setJRDlistaArticulosInsumos(JRDataSource jRDlistaArticulosInsumos) {
		JRDlistaArticulosInsumos = jRDlistaArticulosInsumos;
	}

	public JRDataSource getJRDlistaArticulosOtros() {
		return JRDlistaArticulosOtros;
	}

	public void setJRDlistaArticulosOtros(JRDataSource jRDlistaArticulosOtros) {
		JRDlistaArticulosOtros = jRDlistaArticulosOtros;
	}

	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setTelefonoPersona(String telefonoPersona) {
		this.telefonoPersona = telefonoPersona;
	}

	public String getTelefonoPersona() {
		return telefonoPersona;
	}

	public void setJRDDtoGeneralOrdenesServicios(
			JRDataSource jRDDtoGeneralOrdenesServicios) {
		JRDDtoGeneralOrdenesServicios = jRDDtoGeneralOrdenesServicios;
	}

	public JRDataSource getJRDDtoGeneralOrdenesServicios() {
		return JRDDtoGeneralOrdenesServicios;
	}

	public void setJRDDtoGeneralOrdenesArticulos(
			JRDataSource jRDDtoGeneralOrdenesArticulos) {
		JRDDtoGeneralOrdenesArticulos = jRDDtoGeneralOrdenesArticulos;
	}

	public JRDataSource getJRDDtoGeneralOrdenesArticulos() {
		return JRDDtoGeneralOrdenesArticulos;
	}

	public void setDx(String dx) {
		this.dx = dx;
	}

	public String getDx() {
		if(!UtilidadTexto.isEmpty(this.acronimoDx) && this.tipoCieDx!=null)
			dx = this.acronimoDx+" - "+this.tipoCieDx;
		else
			dx = " - ";
		
		return dx;
	}

	public void setTipoCieDx(Integer tipoCieDx) {
		this.tipoCieDx = tipoCieDx;
	}

	public Integer getTipoCieDx() {
		return tipoCieDx;
	}

	public void setAcronimoDx(String acronimoDx) {		
		this.acronimoDx = acronimoDx;
	}

	public String getAcronimoDx() {
		if(UtilidadTexto.isEmpty(this.acronimoDx))
			this.acronimoDx="";
		return acronimoDx;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public void setEsPosStr(String esPosStr) {
		this.esPosStr = esPosStr;
	}

	public String getEsPosStr() {
		return esPosStr;
	}

	public void setUrgenteStr(String urgenteStr) {
		this.urgenteStr = urgenteStr;
	}

	public String getUrgenteStr() {
		return urgenteStr;
	}

	public void setMedicamento(Boolean medicamento) {
		this.medicamento = medicamento;
	}

	public Boolean isMedicamento() {
		return medicamento;
	}

	public void setObservacionesMedicamentos(String observacionesMedicamentos) {
		this.observacionesMedicamentos = observacionesMedicamentos;
	}

	public String getObservacionesMedicamentos() {		
		return observacionesMedicamentos;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	public String getIndicativo() {
		return indicativo;
	}

	public void setSaltoPaginaReporte(boolean saltoPaginaReporte) {
		this.saltoPaginaReporte = saltoPaginaReporte;
	}

	public boolean isSaltoPaginaReporte() {
		return saltoPaginaReporte;
	}

	public void setHayAnulacion(boolean hayAnulacion) {
		this.hayAnulacion = hayAnulacion;
	}

	public boolean isHayAnulacion() {
		return hayAnulacion;
	}

	public void setEstadoOrdenStr(String estadoOrdenStr) {
		this.estadoOrdenStr = estadoOrdenStr;
	}

	public String getEstadoOrdenStr() {
		return estadoOrdenStr;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getHoraAnulacion() {
		return horaAnulacion;
	}

	public void setHoraAnulacion(String horaAnulacion) {
		this.horaAnulacion = horaAnulacion;
	}

	public Date getFechaAnulacion() {
		return fechaAnulacion;
	}

	public void setFechaAnulacion(Date fechaAnulacion) {
		this.fechaAnulacion = fechaAnulacion;
	}

	public String getPrimerNombreAnulacion() {
		return primerNombreAnulacion;
	}

	public void setPrimerNombreAnulacion(String primerNombreAnulacion) {
		this.primerNombreAnulacion = primerNombreAnulacion;
	}

	public String getPrimerApellidoAnulacion() {
		return primerApellidoAnulacion;
	}

	public void setPrimerApellidoAnulacion(String primerApellidoAnulacion) {
		this.primerApellidoAnulacion = primerApellidoAnulacion;
	}


	public String getSegundoNombreAnulacion() {
		return segundoNombreAnulacion;
	}

	public void setSegundoNombreAnulacion(String segundoNombreAnulacion) {
		this.segundoNombreAnulacion = segundoNombreAnulacion;
	}

	public String getSegundoApellidoAnulacion() {
		return segundoApellidoAnulacion;
	}

	public void setSegundoApellidoAnulacion(String segundoApellidoAnulacion) {
		this.segundoApellidoAnulacion = segundoApellidoAnulacion;
	}

	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion;
	}

	public String getTipoImpresion() {
		return tipoImpresion;
	}

	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}

	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	public void setFirmaDigital(String firmaDigital) {
		this.firmaDigital = firmaDigital;
	}

	public String getFirmaDigital() {
		return firmaDigital;
	}

	public String getPathFirmaDigital() 
	{
		return ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+this.firmaDigital;
	}

	public String getPrimerNombreMedico() {
		return primerNombreMedico;
	}

	public void setPrimerNombreMedico(String primerNombreMedico) {
		this.primerNombreMedico = primerNombreMedico;
	}

	public String getPrimerApellidoMedico() {
		return primerApellidoMedico;
	}

	public void setPrimerApellidoMedico(String primerApellidoMedico) {
		this.primerApellidoMedico = primerApellidoMedico;
	}

	public String getSegundoNombreMedico() {
		return segundoNombreMedico;
	}

	public void setSegundoNombreMedico(String segundoNombreMedico) {
		this.segundoNombreMedico = segundoNombreMedico;
	}

	public String getSegundoApellidoMedico() {
		return segundoApellidoMedico;
	}

	public void setSegundoApellidoMedico(String segundoApellidoMedico) {
		this.segundoApellidoMedico = segundoApellidoMedico;
	}

	public String getRegistroMedico() {
		return registroMedico;
	}

	public void setRegistroMedico(String registroMedico) {
		this.registroMedico = registroMedico;
	}

	public String getFirmaDigitalMedico() {
		return firmaDigitalMedico;
	}

	public void setFirmaDigitalMedico(String firmaDigitalMedico) {
		this.firmaDigitalMedico = firmaDigitalMedico;
	}

	public void setLoginMedico(String loginMedico) {
		this.loginMedico = loginMedico;
	}

	public String getLoginMedico() {
		return loginMedico;
	}

	public void setUnidadMedidaDosis(String unidadMedidaDosis) {
		this.unidadMedidaDosis = unidadMedidaDosis;
	}

	public String getUnidadMedidaDosis() {
		return unidadMedidaDosis;
	}

	public void setCantidadUnidadMedidaDosis(BigDecimal cantidadUnidadMedidaDosis) {
		this.cantidadUnidadMedidaDosis = cantidadUnidadMedidaDosis;
	}

	public BigDecimal getCantidadUnidadMedidaDosis() {
		return cantidadUnidadMedidaDosis;
	}

	public void setPyp(boolean pyp) {
		this.pyp = pyp;
	}

	public boolean isPyp() {
		return pyp;
	}

	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}

	public String getConcentracion() {
		return concentracion;
	}

	/**
	 * @return valor de consecutivoOrden
	 */
	public String getConsecutivoOrden() {
		return consecutivoOrden;
	}

	/**
	 * @param consecutivoOrden el consecutivoOrden para asignar
	 */
	public void setConsecutivoOrden(String consecutivoOrden) {
		this.consecutivoOrden = consecutivoOrden;
	}

	public void setFormatoMediaCarta(String formatoMediaCarta) {
		this.formatoMediaCarta = formatoMediaCarta;
	}

	public String getFormatoMediaCarta() {
		return formatoMediaCarta;
	}

	public void setPiePagAmbMed(String piePagAmbMed) {
		this.piePagAmbMed = piePagAmbMed;
	}

	public String getPiePagAmbMed() {
		return piePagAmbMed;
	}
	
}
