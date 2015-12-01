/**
 * 
 */
package com.servinte.axioma.dto.odontologia;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;

import com.ibm.icu.math.BigDecimal;

/**
 * @author armando
 *
 */
public class DtoResultadoReporteCitasOdontologicas
{
	
	
	public DtoResultadoReporteCitasOdontologicas() 
	{
		super();
		this.codigoEmpresaInstitucion = ConstantesBD.codigoNuncaValido;
		this.descripcionEmpresaInstitucion = "";
		this.centrosAtencion = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte>();
		this.fechaInicial = "";
		this.fechaFinal= "";
		this.especialidad= "";
		this.unidadAgenda= "";
		this.servicios= "";
		this.tiposCita= "";
		this.estadosCita= "";
		this.profesional= "";
		this.usuario= "";
		this.conCancelacion= "";
		this.canceladasPor= "";
		this.razonSocial= "";
		this.logoDerecha = "";
		this.logoIzquierda = "";
		this.tipoReporte = ConstantesBD.codigoNuncaValido;
		this.esResumido = false;
	}


	/**
	 * 
	 */
	private int codigoEmpresaInstitucion;
	
	/**
	 * 
	 */
	private String descripcionEmpresaInstitucion;
	
	/**
	 * 
	 */
	private ArrayList<DtoCentroAtencionReporte> centrosAtencion;
	
	private String fechaInicial;
	private String fechaFinal;
	private String especialidad;
	private String unidadAgenda;
	private String servicios;
	private String tiposCita;
	private String estadosCita;
	private String profesional;
	private String usuario;
	private String conCancelacion;
	private String canceladasPor;
	private String razonSocial;
	private String logoDerecha;
	private String logoIzquierda;
    private int tipoReporte;
    /** Objeto jasper para el subreporte de instituciones. */
    transient private JRDataSource dsListadoResultado;
    
    /** Objeto jasper para el subreporte del consolidado */
    transient private JRDataSource dsCentrosAtencion;
    
    private boolean esResumido;
	
	
	public int getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}


	public void setCodigoEmpresaInstitucion(int codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}


	public String getDescripcionEmpresaInstitucion() {
		return descripcionEmpresaInstitucion;
	}


	public void setDescripcionEmpresaInstitucion(
			String descripcionEmpresaInstitucion) {
		this.descripcionEmpresaInstitucion = descripcionEmpresaInstitucion;
	}


	public ArrayList<DtoCentroAtencionReporte> getCentrosAtencion() {
		return centrosAtencion;
	}


	public void setCentrosAtencion(
			ArrayList<DtoCentroAtencionReporte> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}


	public class DtoCentroAtencionReporte
	{
		
		
		public DtoCentroAtencionReporte() 
		{
			super();
			this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
			this.codigoCentroAtencion="";
			this.descripcionCentroAtencion = "";
			this.codigoCiudadCA = ConstantesBD.codigoNuncaValido;
			this.descripcionCiudadCA = "";
			this.codigoRegionCA = ConstantesBD.codigoNuncaValido;
			this.especialidades = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte>();
			this.descripcionRegionCA = "";
			this.codigoPais=ConstantesBD.codigoNuncaValido;
			this.descripcionPais="";
			this.codigoDepartamento=ConstantesBD.codigoNuncaValido;
			this.descripcionDepartamento="";
			
		}

		/**
		 * 
		 */
		private int consecutivoCentroAtencion;
		
		/**
		 * 
		 */
		private String codigoCentroAtencion; 
		
		/**
		 * 
		 */
		private String descripcionCentroAtencion;
		
		/**
		 * 
		 */
		private int codigoCiudadCA;
		
		/**
		 * 
		 */
		private String descripcionCiudadCA;
		
		/**
		 * 
		 */
		private int codigoRegionCA;
		
		/**
		 * 
		 */
		private ArrayList<DtoEspecialidadReporte> especialidades;
		
		
		/**
		 * 
		 */
		private int codigoPais;
		
		/**
		 * 
		 */
		private String descripcionPais;
		
		/**
		 * 
		 */
		private int codigoDepartamento;
		
		/**
		 * 
		 */
		private String descripcionDepartamento;
		
		/**
		 * 
		 */
		private String descripcionRegionCA;
		
		/** Objeto jasper para el subreporte de especialidades */
	    transient private JRDataSource dsEspecialidades;
		
		
		public int getConsecutivoCentroAtencion() {
			return consecutivoCentroAtencion;
		}

		public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
			this.consecutivoCentroAtencion = consecutivoCentroAtencion;
		}

		public String getDescripcionCentroAtencion() {
			return descripcionCentroAtencion;
		}

		public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
			this.descripcionCentroAtencion = descripcionCentroAtencion;
		}

		public int getCodigoCiudadCA() {
			return codigoCiudadCA;
		}

		public void setCodigoCiudadCA(int codigoCiudadCA) {
			this.codigoCiudadCA = codigoCiudadCA;
		}

		public String getDescripcionCiudadCA() {
			return descripcionCiudadCA;
		}

		public void setDescripcionCiudadCA(String descripcionCiudadCA) {
			this.descripcionCiudadCA = descripcionCiudadCA;
		}

		public int getCodigoRegionCA() {
			return codigoRegionCA;
		}

		public void setCodigoRegionCA(int codigoRegionCA) {
			this.codigoRegionCA = codigoRegionCA;
		}

		public String getDescripcionRegionCA() {
			return descripcionRegionCA;
		}

		public void setDescripcionRegionCA(String descripcionRegionCA) {
			this.descripcionRegionCA = descripcionRegionCA;
		}
		
		public ArrayList<DtoEspecialidadReporte> getEspecialidades() {
			return especialidades;
		}

		public void setEspecialidades(ArrayList<DtoEspecialidadReporte> especialidades) {
			this.especialidades = especialidades;
		}

		public String getCodigoCentroAtencion() {
			return codigoCentroAtencion;
		}

		public void setCodigoCentroAtencion(String codigoCentroAtencion) {
			this.codigoCentroAtencion = codigoCentroAtencion;
		}

		public int getCodigoPais() {
			return codigoPais;
		}

		public void setCodigoPais(int codigoPais) {
			this.codigoPais = codigoPais;
		}

		public String getDescripcionPais() {
			return descripcionPais;
		}

		public void setDescripcionPais(String descripcionPais) {
			this.descripcionPais = descripcionPais;
		}

		public int getCodigoDepartamento() {
			return codigoDepartamento;
		}

		public void setCodigoDepartamento(int codigoDepartamento) {
			this.codigoDepartamento = codigoDepartamento;
		}

		public String getDescripcionDepartamento() {
			return descripcionDepartamento;
		}

		public void setDescripcionDepartamento(String descripcionDepartamento) {
			this.descripcionDepartamento = descripcionDepartamento;
		}
		

		
		public class DtoEspecialidadReporte
		{
			
			public DtoEspecialidadReporte() {
				super();
				this.codigoEspecialidad = ConstantesBD.codigoNuncaValido;
				this.descripcionEspecialidad = "";
				this.codigoUnidadAgenda = ConstantesBD.codigoNuncaValido;
				this.descripcionUnidadAgenda = "";
				this.citasSinCancelacion = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte>();
				this.citasConCancelacionPaciente = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte>();
				this.citasConCancelacionInstitucion = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte>();
				
				this.resumenCitas = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoTipoCitaEstadoCita>();
				this.citasSinEspecialidad = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte>();
				this.esResumido = false;
				this.totalCitasEspecialidad = 0;
			}

			public ArrayList<DtoTipoCitaEstadoCita> getResumenCitas() {
				return resumenCitas;
			}

			public void setResumenCitas(ArrayList<DtoTipoCitaEstadoCita> resumenCitas) {
				this.resumenCitas = resumenCitas;
			}

			private int codigoEspecialidad;
			
			private String descripcionEspecialidad;
			
			private int codigoUnidadAgenda;
			
			private String descripcionUnidadAgenda;
			
			private ArrayList<DtoCitasReporte> citasSinCancelacion;
			
			private ArrayList<DtoCitasReporte> citasConCancelacionPaciente;
			
			private ArrayList<DtoCitasReporte> citasConCancelacionInstitucion;
			
			private ArrayList<DtoTipoCitaEstadoCita> resumenCitas;
			
			private ArrayList<DtoCitasReporte> citasSinEspecialidad;
			
			/** Objeto jasper para el subreporte de citas con cancelacion por paciente */
		    transient private JRDataSource dsCitasConCancelacionPaciente;
		    
		    /** Objeto jasper para el subreporte de citas con cancelacion por paciente */
		    transient private JRDataSource dsCitasConCancelacionInstitucion;
		    
		    /** Objeto jasper para el subreporte de citas sin cancelación */
		    transient private JRDataSource dsCitasSinCancelacion;
		    
		    /** Objeto jasper para el subreporte de citas sin cancelación */
		    transient private JRDataSource dsCitasSinEspecialidad;
		    
		    /** Objeto jasper para el subreporte con el resumen de las citas */
		    transient private JRDataSource dsResumenCitas;
		    
		    private int numCitasConCancelacionPaciente;
		    private int numCitasSinCancelacion;
		    private boolean esResumido;
		    private int totalCitasEspecialidad;
		    
			public int getCodigoEspecialidad() {
				return codigoEspecialidad;
			}

			public void setCodigoEspecialidad(int codigoEspecialidad) {
				this.codigoEspecialidad = codigoEspecialidad;
			}

			public String getDescripcionEspecialidad() {
				return descripcionEspecialidad;
			}

			public void setDescripcionEspecialidad(String descripcionEspecialidad) {
				this.descripcionEspecialidad = descripcionEspecialidad;
			}

			public int getCodigoUnidadAgenda() {
				return codigoUnidadAgenda;
			}

			public void setCodigoUnidadAgenda(int codigoUnidadAgenda) {
				this.codigoUnidadAgenda = codigoUnidadAgenda;
			}

			public String getDescripcionUnidadAgenda() {
				return descripcionUnidadAgenda;
			}

			public void setDescripcionUnidadAgenda(String descripcionUnidadAgenda) {
				this.descripcionUnidadAgenda = descripcionUnidadAgenda;
			}

			public ArrayList<DtoCitasReporte> getCitasSinCancelacion() {
				return citasSinCancelacion;
			}

			public void setCitasSinCancelacion(
					ArrayList<DtoCitasReporte> citasSinCancelacion) {
				this.citasSinCancelacion = citasSinCancelacion;
			}

			public ArrayList<DtoCitasReporte> getCitasConCancelacionPaciente() {
				return citasConCancelacionPaciente;
			}

			public void setCitasConCancelacionPaciente(
					ArrayList<DtoCitasReporte> citasConCancelacionPaciente) {
				this.citasConCancelacionPaciente = citasConCancelacionPaciente;
			}

			public ArrayList<DtoCitasReporte> getCitasConCancelacionInstitucion() {
				return citasConCancelacionInstitucion;
			}

			public void setCitasConCancelacionInstitucion(
					ArrayList<DtoCitasReporte> citasConCancelacionInstitucion) {
				this.citasConCancelacionInstitucion = citasConCancelacionInstitucion;
			}

			public class DtoTipoCitaEstadoCita
			{
				public DtoTipoCitaEstadoCita() 
				{
					super();
					this.tipoCita = "";
					this.estadoCita = "";
					this.numeroCitas = 0;
					this.descripcionEspecialidad = "";
					this.ayudanteTipoCita = "";
					this.ayudanteEstadoCita = "";
				}

				private String tipoCita;
				
				private String estadoCita;
				
				private int numeroCitas;
				
				private String descripcionEspecialidad;
				
				private String ayudanteTipoCita;
				
				private String ayudanteEstadoCita;

				public String getTipoCita() {
					return tipoCita;
				}

				public void setTipoCita(String tipoCita) {
					this.tipoCita = tipoCita;
				}

				public String getEstadoCita() {
					return estadoCita;
				}

				public void setEstadoCita(String estadoCita) {
					this.estadoCita = estadoCita;
				}

				public int getNumeroCitas() {
					return numeroCitas;
				}

				public void setNumeroCitas(int numeroCitas) {
					this.numeroCitas = numeroCitas;
				}

				/**
				 * Método que se encarga de obtener el 
				 * valor del atributo  descripcionEspecialidad
				 *
				 * @return retorna la variable descripcionEspecialidad
				 */
				public String getDescripcionEspecialidad() {
					return descripcionEspecialidad;
				}

				/**
				 * Método que se encarga de establecer el valor
				 * del atributo descripcionEspecialidad
				 * @param descripcionEspecialidad es el valor para el atributo descripcionEspecialidad 
				 */
				public void setDescripcionEspecialidad(String descripcionEspecialidad) {
					this.descripcionEspecialidad = descripcionEspecialidad;
				}

				/**
				 * Método que se encarga de obtener el 
				 * valor del atributo  ayudanteTipoCita
				 *
				 * @return retorna la variable ayudanteTipoCita
				 */
				public String getAyudanteTipoCita() {
					return ayudanteTipoCita;
				}

				/**
				 * Método que se encarga de establecer el valor
				 * del atributo ayudanteTipoCita
				 * @param ayudanteTipoCita es el valor para el atributo ayudanteTipoCita 
				 */
				public void setAyudanteTipoCita(String ayudanteTipoCita) {
					this.ayudanteTipoCita = ayudanteTipoCita;
				}

				/**
				 * Método que se encarga de obtener el 
				 * valor del atributo  ayudanteEstadoCita
				 *
				 * @return retorna la variable ayudanteEstadoCita
				 */
				public String getAyudanteEstadoCita() {
					return ayudanteEstadoCita;
				}

				/**
				 * Método que se encarga de establecer el valor
				 * del atributo ayudanteEstadoCita
				 * @param ayudanteEstadoCita es el valor para el atributo ayudanteEstadoCita 
				 */
				public void setAyudanteEstadoCita(String ayudanteEstadoCita) {
					this.ayudanteEstadoCita = ayudanteEstadoCita;
				}
				
			}
			
			public class DtoCitasReporte
			{
				public DtoCitasReporte() 
				{
					super();
					this.codigoCita=ConstantesBD.codigoNuncaValido;
					this.fecha = "";
					this.hora = "";
					this.tipoIDPaciente = "";
					this.numeroIDPaciente = "";
					this.nombresPaciente = "";
					this.telefono = "";
					this.nombresProfesional = "";
					this.tipoCita = "";
					this.estadoCita = "";
					this.codigoMotivoCancelacion = ConstantesBD.codigoNuncaValido;
					this.tipoCancelacion=ConstantesBD.codigoNuncaValido;
					this.descripcionMotivoCancelacion = "";
					this.servicios = new ArrayList<DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte.DtoServiciosCita>();
					this.usuario = "";
					this.valorCita = new BigDecimal(0);
					this.primerNombrePac = "";
					this.segundoNombrePac = ""; 
					this.primerApellidoPac = "";
					this.segundoApellidoPac = "";
					this.primerNombreProf = "";
					this.segundoNombreProf = ""; 
					this.primerApellidoProf = "";
					this.segundoApellidoProf = "";
					
				}

				public int getTipoCancelacion() {
					return tipoCancelacion;
				}

				public void setTipoCancelacion(int tipoCancelacion) {
					this.tipoCancelacion = tipoCancelacion;
				}

				public String getPrimerNombrePac() {
					return primerNombrePac;
				}

				public void setPrimerNombrePac(String primerNombrePac) {
					this.primerNombrePac = primerNombrePac;
				}

				public String getSegundoNombrePac() {
					return segundoNombrePac;
				}

				public void setSegundoNombrePac(String segundoNombrePac) {
					this.segundoNombrePac = segundoNombrePac;
				}

				public String getPrimerApellidoPac() {
					return primerApellidoPac;
				}

				public void setPrimerApellidoPac(String primerApellidoPac) {
					this.primerApellidoPac = primerApellidoPac;
				}

				public String getSegundoApellidoPac() {
					return segundoApellidoPac;
				}

				public void setSegundoApellidoPac(String segundoApellidoPac) {
					this.segundoApellidoPac = segundoApellidoPac;
				}

				public String getPrimerNombreProf() {
					return primerNombreProf;
				}

				public void setPrimerNombreProf(String primerNombreProf) {
					this.primerNombreProf = primerNombreProf;
				}

				public String getSegundoNombreProf() {
					return segundoNombreProf;
				}

				public void setSegundoNombreProf(String segundoNombreProf) {
					this.segundoNombreProf = segundoNombreProf;
				}

				public String getPrimerApellidoProf() {
					return primerApellidoProf;
				}

				public void setPrimerApellidoProf(String primerApellidoProf) {
					this.primerApellidoProf = primerApellidoProf;
				}

				public String getSegundoApellidoProf() {
					return segundoApellidoProf;
				}

				public void setSegundoApellidoProf(String segundoApellidoProf) {
					this.segundoApellidoProf = segundoApellidoProf;
				}

				public int getCodigoCita() {
					return codigoCita;
				}

				public void setCodigoCita(int codigoCita) {
					this.codigoCita = codigoCita;
				}
				
				private String fecha;
				
				/**
				 * 
				 */
				private int codigoCita;

				private String hora;
				
				private String tipoIDPaciente;
				
				private String numeroIDPaciente;
				
				private String nombresPaciente;
				
				private String telefono;
				
				private String nombresProfesional;
				
				private String primerNombrePac;
				private String segundoNombrePac; 
				private String primerApellidoPac;
				private String segundoApellidoPac;

				private String primerNombreProf;
				private String segundoNombreProf; 
				private String primerApellidoProf;
				private String segundoApellidoProf;
			
				private String tipoCita;
				
				private String estadoCita;
				
				private int codigoMotivoCancelacion;
				
				private String descripcionMotivoCancelacion;
				
				private ArrayList<DtoServiciosCita> servicios;
				
				private String usuario;
				
				private BigDecimal valorCita;
				
				private int tipoCancelacion;
				
				private String ayudanteTipoCita;
				
				/**
				 * Permite obtener el estado de la cita según las
				 * convenciones establecidas.
				 */
				private String ayudanteEstadoCita;
				
				/** Objeto jasper para el subreporte de los servicios asociados a la cita*/
			    transient private JRDataSource dsServicios;
			    
			    /**
			     * Almacena el valor de la cita en el formato definido para el sistema.
			     */
			    private String valorCitaFormateado;
				
			    
				public String getFecha() {
					return fecha;
				}

				public void setFecha(String fecha) {
					this.fecha = fecha;
				}

				public String getHora() {
					return hora;
				}

				public void setHora(String hora) {
					this.hora = hora;
				}

				public String getTipoIDPaciente() {
					return tipoIDPaciente;
				}

				public void setTipoIDPaciente(String tipoIDPaciente) {
					this.tipoIDPaciente = tipoIDPaciente;
				}

				public String getNumeroIDPaciente() {
					return numeroIDPaciente;
				}

				public void setNumeroIDPaciente(String numeroIDPaciente) {
					this.numeroIDPaciente = numeroIDPaciente;
				}

				public String getNombresPaciente() {
					return nombresPaciente;
				}

				public void setNombresPaciente(String nombresPaciente) {
					this.nombresPaciente = nombresPaciente;
				}

				public String getTelefono() {
					return telefono;
				}

				public void setTelefono(String telefono) {
					this.telefono = telefono;
				}

				public String getNombresProfesional() {
					
					if (UtilidadTexto.isEmpty(this.nombresProfesional)) {
						this.nombresProfesional = " No asignado ";
					}
					
					return nombresProfesional;
				}

				public void setNombresProfesional(String nombresProfesional) {
					this.nombresProfesional = nombresProfesional;
				}

				public String getTipoCita() {
					return tipoCita;
				}

				public void setTipoCita(String tipoCita) {
					this.tipoCita = tipoCita;
				}

				public String getEstadoCita() {
					return estadoCita;
				}

				public void setEstadoCita(String estadoCita) {
					this.estadoCita = estadoCita;
				}

				public int getCodigoMotivoCancelacion() {
					return codigoMotivoCancelacion;
				}

				public void setCodigoMotivoCancelacion(int codigoMotivoCancelacion) {
					this.codigoMotivoCancelacion = codigoMotivoCancelacion;
				}

				public String getDescripcionMotivoCancelacion() {
					return descripcionMotivoCancelacion;
				}

				public void setDescripcionMotivoCancelacion(String descripcionMotivoCancelacion) {
					this.descripcionMotivoCancelacion = descripcionMotivoCancelacion;
				}

				public ArrayList<DtoServiciosCita> getServicios() {
					return servicios;
				}

				public void setServicios(ArrayList<DtoServiciosCita> servicios) {
					this.servicios = servicios;
				}

				public String getUsuario() {
					return usuario;
				}

				public void setUsuario(String usuario) {
					this.usuario = usuario;
				}

				public BigDecimal getValorCita() {
					return valorCita;
				}

				public void setValorCita(BigDecimal valorCita) {
					this.valorCita = valorCita;
					this.valorCitaFormateado = UtilidadTexto.formatearValores(this.valorCita.doubleValue());
				}

				public class DtoServiciosCita
				{
					
					public DtoServiciosCita() 
					{
						super();
						this.codigoServicio = ConstantesBD.codigoNuncaValido;
						this.codigoPropietarioServicio = "";
						this.descripcionServicio = "";
						this.descripcionPropietarioServicio = "";
						this.valorCita = ConstantesBD.codigoNuncaValidoDouble;
					}

					public double getValorCita() {
						return valorCita;
					}

					public void setValorCita(double valorCita) {
						this.valorCita = valorCita;
					}

					private int codigoServicio;
					
					private String codigoPropietarioServicio;
					
					private String descripcionServicio;
					
					private String descripcionPropietarioServicio;
					
					private double valorCita;

					public int getCodigoServicio() {
						return codigoServicio;
					}

					public void setCodigoServicio(int codigoServicio) {
						this.codigoServicio = codigoServicio;
					}

					public String getCodigoPropietarioServicio() {
						return codigoPropietarioServicio;
					}

					public void setCodigoPropietarioServicio(String codigoPropietarioServicio) {
						this.codigoPropietarioServicio = codigoPropietarioServicio;
					}

					public String getDescripcionServicio() {
						return descripcionServicio;
					}

					public void setDescripcionServicio(String descripcionServicio) {
						this.descripcionServicio = descripcionServicio;
					}

					public String getDescripcionPropietarioServicio() {
						return descripcionPropietarioServicio;
					}

					public void setDescripcionPropietarioServicio(
							String descripcionPropietarioServicio) {
						this.descripcionPropietarioServicio = descripcionPropietarioServicio;
					}
				}

				/**
				 * M&eacute;todo que se encarga de obtener el valor 
				 *  del atributo ayudanteEstadoCita
				 * 
				 * @return  Retorna la variable ayudanteEstadoCita
				 */
				public String getAyudanteEstadoCita() {
					
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoReservado)) {
						this.ayudanteEstadoCita = "Res";
					}
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoProgramado)) {
						this.ayudanteEstadoCita = "Prog";
					}
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoAsignado)) {
						this.ayudanteEstadoCita = "Asig";
					}
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoAreprogramar)) {
						this.ayudanteEstadoCita = "A Rep";
					}
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoNoAsistio)) {
						this.ayudanteEstadoCita = "N Asis";
					}
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoNoAtencion)) {
						this.ayudanteEstadoCita = "N Aten";
					}
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoAtendida)) {
						this.ayudanteEstadoCita = "Aten";
					}
					if (estadoCita.trim().equals(ConstantesIntegridadDominio.acronimoCancelado)) {
						this.ayudanteEstadoCita = "Can";
					}
					
					return ayudanteEstadoCita;
				}

				/**
				 * M&eacute;todo que se encarga de establecer el valor 
				 * del atributo ayudanteEstadoCita
				 * 
				 * @param  valor para el atributo ayudanteEstadoCita 
				 */
				public void setAyudanteEstadoCita(String ayudanteEstadoCita) {
					this.ayudanteEstadoCita = ayudanteEstadoCita;
				}

				/**
				 * M&eacute;todo que se encarga de obtener el valor 
				 *  del atributo ayudanteTipoCita
				 * 
				 * @return  Retorna la variable ayudanteTipoCita
				 */
				public String getAyudanteTipoCita() {
					
					if (tipoCita.trim().equals(ConstantesIntegridadDominio.acronimoPrioritaria)) {
						this.ayudanteTipoCita = "A Prior";
					}
					if (tipoCita.trim().equals(ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial)) {
						this.ayudanteTipoCita = "V Inic";
					}
					if (tipoCita.trim().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon)) {
						this.ayudanteTipoCita = "Cont";
					}
					if (tipoCita.trim().equals(ConstantesIntegridadDominio.acronimoRevaloracion)) {
						this.ayudanteTipoCita = "Rev";
					}
					if (tipoCita.trim().equals(ConstantesIntegridadDominio.acronimoTratamiento)) {
						this.ayudanteTipoCita = "Trat";
					}
					if (tipoCita.trim().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)) {
						this.ayudanteTipoCita = "Int";
					}
					if (tipoCita.trim().equals(ConstantesIntegridadDominio.acronimoAuditoria)) {
						this.ayudanteTipoCita = "Aud";
					}
					
					return ayudanteTipoCita;
				}

				/**
				 * M&eacute;todo que se encarga de establecer el valor 
				 * del atributo ayudanteTipoCita
				 * 
				 * @param  valor para el atributo ayudanteTipoCita 
				 */
				public void setAyudanteTipoCita(String ayudanteTipoCita) {
					this.ayudanteTipoCita = ayudanteTipoCita;
				}

				/**
				 * Método que se encarga de obtener el 
				 * valor del atributo  dsServicios
				 *
				 * @return retorna la variable dsServicios
				 */
				public JRDataSource getDsServicios() {
					return dsServicios;
				}

				/**
				 * Método que se encarga de establecer el valor
				 * del atributo dsServicios
				 * @param dsServicios es el valor para el atributo dsServicios 
				 */
				public void setDsServicios(JRDataSource dsServicios) {
					this.dsServicios = dsServicios;
				}

				/**
				 * Método que se encarga de obtener el 
				 * valor del atributo  valorCitaFormateado
				 *
				 * @return retorna la variable valorCitaFormateado
				 */
				public String getValorCitaFormateado() {
					
					if (UtilidadTexto.isEmpty(this.valorCitaFormateado)) {
						this.valorCitaFormateado = " - ";
					}
					return valorCitaFormateado;
				}

				/**
				 * Método que se encarga de establecer el valor
				 * del atributo valorCitaFormateado
				 * @param valorCitaFormateado es el valor para el atributo valorCitaFormateado 
				 */
				public void setValorCitaFormateado(String valorCitaFormateado) {
					this.valorCitaFormateado = valorCitaFormateado;
				}
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  citasSinEspecialidad
			 *
			 * @return retorna la variable citasSinEspecialidad
			 */
			public ArrayList<DtoCitasReporte> getCitasSinEspecialidad() {
				return citasSinEspecialidad;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo citasSinEspecialidad
			 * @param citasSinEspecialidad es el valor para el atributo citasSinEspecialidad 
			 */
			public void setCitasSinEspecialidad(
					ArrayList<DtoCitasReporte> citasSinEspecialidad) {
				this.citasSinEspecialidad = citasSinEspecialidad;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  dsCitasConCancelacionPaciente
			 *
			 * @return retorna la variable dsCitasConCancelacionPaciente
			 */
			public JRDataSource getDsCitasConCancelacionPaciente() {
				return dsCitasConCancelacionPaciente;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo dsCitasConCancelacionPaciente
			 * @param dsCitasConCancelacionPaciente es el valor para el atributo dsCitasConCancelacionPaciente 
			 */
			public void setDsCitasConCancelacionPaciente(
					JRDataSource dsCitasConCancelacionPaciente) {
				this.dsCitasConCancelacionPaciente = dsCitasConCancelacionPaciente;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  dsCitasConCancelacionInstitucion
			 *
			 * @return retorna la variable dsCitasConCancelacionInstitucion
			 */
			public JRDataSource getDsCitasConCancelacionInstitucion() {
				return dsCitasConCancelacionInstitucion;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo dsCitasConCancelacionInstitucion
			 * @param dsCitasConCancelacionInstitucion es el valor para el atributo dsCitasConCancelacionInstitucion 
			 */
			public void setDsCitasConCancelacionInstitucion(
					JRDataSource dsCitasConCancelacionInstitucion) {
				this.dsCitasConCancelacionInstitucion = dsCitasConCancelacionInstitucion;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  dsCitasSinCancelacion
			 *
			 * @return retorna la variable dsCitasSinCancelacion
			 */
			public JRDataSource getDsCitasSinCancelacion() {
				return dsCitasSinCancelacion;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo dsCitasSinCancelacion
			 * @param dsCitasSinCancelacion es el valor para el atributo dsCitasSinCancelacion 
			 */
			public void setDsCitasSinCancelacion(JRDataSource dsCitasSinCancelacion) {
				this.dsCitasSinCancelacion = dsCitasSinCancelacion;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  dsCitasSinEspecialidad
			 *
			 * @return retorna la variable dsCitasSinEspecialidad
			 */
			public JRDataSource getDsCitasSinEspecialidad() {
				return dsCitasSinEspecialidad;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo dsCitasSinEspecialidad
			 * @param dsCitasSinEspecialidad es el valor para el atributo dsCitasSinEspecialidad 
			 */
			public void setDsCitasSinEspecialidad(JRDataSource dsCitasSinEspecialidad) {
				this.dsCitasSinEspecialidad = dsCitasSinEspecialidad;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  numCitasConCancelacionPaciente
			 *
			 * @return retorna la variable numCitasConCancelacionPaciente
			 */
			public int getNumCitasConCancelacionPaciente() {
				this.numCitasConCancelacionPaciente= citasConCancelacionPaciente.size();
				
				return numCitasConCancelacionPaciente;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo numCitasConCancelacionPaciente
			 * @param numCitasConCancelacionPaciente es el valor para el atributo numCitasConCancelacionPaciente 
			 */
			public void setNumCitasConCancelacionPaciente(int numCitasConCancelacionPaciente) {
				this.numCitasConCancelacionPaciente = numCitasConCancelacionPaciente;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  numCitasSinCancelacion
			 *
			 * @return retorna la variable numCitasSinCancelacion
			 */
			public int getNumCitasSinCancelacion() {
				this.numCitasSinCancelacion = citasSinCancelacion.size();
				return numCitasSinCancelacion;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo numCitasSinCancelacion
			 * @param numCitasSinCancelacion es el valor para el atributo numCitasSinCancelacion 
			 */
			public void setNumCitasSinCancelacion(int numCitasSinCancelacion) {
				this.numCitasSinCancelacion = numCitasSinCancelacion;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  dsResumenCitas
			 *
			 * @return retorna la variable dsResumenCitas
			 */
			public JRDataSource getDsResumenCitas() {
				return dsResumenCitas;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo dsResumenCitas
			 * @param dsResumenCitas es el valor para el atributo dsResumenCitas 
			 */
			public void setDsResumenCitas(JRDataSource dsResumenCitas) {
				this.dsResumenCitas = dsResumenCitas;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  esResumido
			 *
			 * @return retorna la variable esResumido
			 */
			public boolean isEsResumido() {
				return esResumido;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo esResumido
			 * @param esResumido es el valor para el atributo esResumido 
			 */
			public void setEsResumido(boolean esResumido) {
				this.esResumido = esResumido;
			}

			/**
			 * Método que se encarga de obtener el 
			 * valor del atributo  totalCitasEspecialidad
			 *
			 * @return retorna la variable totalCitasEspecialidad
			 */
			public int getTotalCitasEspecialidad() {
				return totalCitasEspecialidad;
			}

			/**
			 * Método que se encarga de establecer el valor
			 * del atributo totalCitasEspecialidad
			 * @param totalCitasEspecialidad es el valor para el atributo totalCitasEspecialidad 
			 */
			public void setTotalCitasEspecialidad(int totalCitasEspecialidad) {
				this.totalCitasEspecialidad = totalCitasEspecialidad;
			}
		}



		/**
		 * Método que se encarga de obtener el 
		 * valor del atributo  dsEspecialidades
		 *
		 * @return retorna la variable dsEspecialidades
		 */
		public JRDataSource getDsEspecialidades() {
			return dsEspecialidades;
		}

		/**
		 * Método que se encarga de establecer el valor
		 * del atributo dsEspecialidades
		 * @param dsEspecialidades es el valor para el atributo dsEspecialidades 
		 */
		public void setDsEspecialidades(JRDataSource dsEspecialidades) {
			this.dsEspecialidades = dsEspecialidades;
		}
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaInicial
	 *
	 * @return retorna la variable fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaInicial
	 * @param fechaInicial es el valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  fechaFinal
	 *
	 * @return retorna la variable fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo fechaFinal
	 * @param fechaFinal es el valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  especialidad
	 *
	 * @return retorna la variable especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo especialidad
	 * @param especialidad es el valor para el atributo especialidad 
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  unidadAgenda
	 *
	 * @return retorna la variable unidadAgenda
	 */
	public String getUnidadAgenda() {
		return unidadAgenda;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo unidadAgenda
	 * @param unidadAgenda es el valor para el atributo unidadAgenda 
	 */
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  servicios
	 *
	 * @return retorna la variable servicios
	 */
	public String getServicios() {
		return servicios;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo servicios
	 * @param servicios es el valor para el atributo servicios 
	 */
	public void setServicios(String servicios) {
		this.servicios = servicios;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  tiposCita
	 *
	 * @return retorna la variable tiposCita
	 */
	public String getTiposCita() {
		return tiposCita;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo tiposCita
	 * @param tiposCita es el valor para el atributo tiposCita 
	 */
	public void setTiposCita(String tiposCita) {
		this.tiposCita = tiposCita;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  estadosCita
	 *
	 * @return retorna la variable estadosCita
	 */
	public String getEstadosCita() {
		return estadosCita;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo estadosCita
	 * @param estadosCita es el valor para el atributo estadosCita 
	 */
	public void setEstadosCita(String estadosCita) {
		this.estadosCita = estadosCita;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  profesional
	 *
	 * @return retorna la variable profesional
	 */
	public String getProfesional() {
		return profesional;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo profesional
	 * @param profesional es el valor para el atributo profesional 
	 */
	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  usuario
	 *
	 * @return retorna la variable usuario
	 */
	public String getUsuario() {
		return usuario;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo usuario
	 * @param usuario es el valor para el atributo usuario 
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  conCancelacion
	 *
	 * @return retorna la variable conCancelacion
	 */
	public String getConCancelacion() {
		return conCancelacion;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo conCancelacion
	 * @param conCancelacion es el valor para el atributo conCancelacion 
	 */
	public void setConCancelacion(String conCancelacion) {
		this.conCancelacion = conCancelacion;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  canceladasPor
	 *
	 * @return retorna la variable canceladasPor
	 */
	public String getCanceladasPor() {
		return canceladasPor;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo canceladasPor
	 * @param canceladasPor es el valor para el atributo canceladasPor 
	 */
	public void setCanceladasPor(String canceladasPor) {
		this.canceladasPor = canceladasPor;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  razonSocial
	 *
	 * @return retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo razonSocial
	 * @param razonSocial es el valor para el atributo razonSocial 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  logoDerecha
	 *
	 * @return retorna la variable logoDerecha
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo logoDerecha
	 * @param logoDerecha es el valor para el atributo logoDerecha 
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  logoIzquierda
	 *
	 * @return retorna la variable logoIzquierda
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo logoIzquierda
	 * @param logoIzquierda es el valor para el atributo logoIzquierda 
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  dsListadoResultado
	 *
	 * @return retorna la variable dsListadoResultado
	 */
	public JRDataSource getDsListadoResultado() {
		return dsListadoResultado;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo dsListadoResultado
	 * @param dsListadoResultado es el valor para el atributo dsListadoResultado 
	 */
	public void setDsListadoResultado(JRDataSource dsListadoResultado) {
		this.dsListadoResultado = dsListadoResultado;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  tipoReporte
	 *
	 * @return retorna la variable tipoReporte
	 */
	public int getTipoReporte() {
		return tipoReporte;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo tipoReporte
	 * @param tipoReporte es el valor para el atributo tipoReporte 
	 */
	public void setTipoReporte(int tipoReporte) {
		this.tipoReporte = tipoReporte;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  dsCentrosAtencion
	 *
	 * @return retorna la variable dsCentrosAtencion
	 */
	public JRDataSource getDsCentrosAtencion() {
		return dsCentrosAtencion;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo dsCentrosAtencion
	 * @param dsCentrosAtencion es el valor para el atributo dsCentrosAtencion 
	 */
	public void setDsCentrosAtencion(JRDataSource dsCentrosAtencion) {
		this.dsCentrosAtencion = dsCentrosAtencion;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  esResumido
	 *
	 * @return retorna la variable esResumido
	 */
	public boolean isEsResumido() {
		return esResumido;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo esResumido
	 * @param esResumido es el valor para el atributo esResumido 
	 */
	public void setEsResumido(boolean esResumido) {
		this.esResumido = esResumido;
	}
}
