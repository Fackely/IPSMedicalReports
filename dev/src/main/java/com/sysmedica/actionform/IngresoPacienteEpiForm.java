package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMapping;
import com.princetonsa.mundo.Paciente;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

public class IngresoPacienteEpiForm extends ValidatorForm {

	private String tipoIdentificacion;
	private String numeroIdentificacion;
	
	private int valorBarrio = 0;
	
	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private String fechaNacimiento;
	private int genero;
	private String municipioResidencia;
	private String departamentoResidencia;
	private String lugarResidencia;
	private int codigoBarrioResidencia;
	private String direccion;
	private String zonaDomicilio;
	private String telefono;
	private int ocupacion;
	private String tipoRegimen;
	private int aseguradora;
	private int etnia;
	private boolean desplazado;
	private int codigoEnfermedadNotificable;
	private String estadoCivil;
	private String municipioNacimiento;
	private String departamentoNacimiento;
	private String lugarNacimiento;
	private boolean estaVivo;
	private int edad;
	private String codigoDiagnostico;
	private String codigoDxLargo;
	private int codigoPaciente;
	private int codigoFicha;
	private int codigoEnf;
	
	private Paciente paciente;
	
	private String estado;
	
	private String municipioIdentifica;
	private String departamentoIdentifica;
	private String lugarIdentifica;
	
	private String codigoAseguradora;
	private String convenio;
	
	private int tipoSangre;
	private String grupoPoblacional;
	
	private boolean modifico;
	
	private String paisExpedicion;
	private String paisNacimiento;
	private String paisResidencia;
	
	
	public void reset()
    {
		tipoIdentificacion = "";
		numeroIdentificacion = "";
		primerApellido = "";
		segundoApellido = "";
		primerNombre = "";
		segundoNombre = "";
		fechaNacimiento = "";
		direccion = "";
		zonaDomicilio = "";
		telefono = "";
		municipioResidencia = "021";
		departamentoResidencia = "11";
		codigoBarrioResidencia = -1;
		lugarResidencia = "021-11";
		ocupacion = 0;
		etnia = 1;
		estaVivo = true;
		municipioNacimiento = "021";
		departamentoNacimiento = "11";
		lugarNacimiento = "021-11";
		estadoCivil = "";
		municipioIdentifica = "0";
		departamentoIdentifica = "0";
		lugarIdentifica = "";
		aseguradora = 0;
		tipoSangre = 9;
		grupoPoblacional = ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales;
		valorBarrio =  0;
		codigoEnfermedadNotificable=0;
		codigoAseguradora = "0";
		codigoDxLargo="";
		
		codigoPaciente = 0;
		
		paisExpedicion = "";
		paisNacimiento = "";
		paisResidencia = "";
    }
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();
		
		if (estado.equals("empezar")) {
            
			reset();
		}
		if (estado.equals("consultar")) {
			
			if (numeroIdentificacion.trim().length()==0) {
				
				errores.add("Numero de documento es requerido.",new ActionMessage("error.epidemiologia.numerodocumento"));
				estado = "empezar";
			}
			/*
			else {
				try {
					int n = Integer.parseInt(numeroIdentificacion);
				}
				catch (NumberFormatException nfe) {
					errores.add("Numero de documento no valido.",new ActionMessage("error.epidemiologia.numeroidnovalido"));
					estado = "empezar";
				}
			}
			*/
		}
		if (estado.equals("guardarnuevo")||estado.equals("modificarpaciente")) {
			
			String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			String fechaNacimientoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaNacimiento);
			
			if (primerNombre.trim().length()==0) {
				
				errores.add("Primer nombre del paciente es requerido.",new ActionMessage("error.epidemiologia.faltanombre"));
				
				if (estado.equals("modificarpaciente")) {
					estado = "pacienteexiste";
				}
				else {
					estado = "llenarpaciente";
				}
			}
			if (primerApellido.trim().length()==0) {
				
				errores.add("Primer apellido del paciente es requerido.",new ActionMessage("error.epidemiologia.faltaapellido"));
				
				if (estado.equals("modificarpaciente")) {
					estado = "pacienteexiste";
				}
				else {
					estado = "llenarpaciente";
				}
			}
			if (numeroIdentificacion.trim().length()==0) {
				
				errores.add("Numero de documento del paciente es requerido.",new ActionMessage("error.epidemiologia.faltaid"));
				
				if (estado.equals("modificarpaciente")) {
					estado = "pacienteexiste";
				}
				else {
					estado = "llenarpaciente";
				}
			}
			if (direccion.trim().length()==0) {
				
				errores.add("Direccion de residencia del paciente es requerida.",new ActionMessage("error.epidemiologia.faltadir"));
				
				if (estado.equals("modificarpaciente")) {
					estado = "pacienteexiste";
				}
				else {
					estado = "llenarpaciente";
				}
			}
			if (fechaNacimiento.trim().length()>0) {
				if (!UtilidadFecha.validarFecha(fechaNacimiento)) {
					
					errores.add("Fecha de Nacimiento no valida ", new ActionMessage("errors.formatoFechaInvalido","de Nacimiento "));
					
					if (estado.equals("modificarpaciente")) {
						estado = "pacienteexiste";
					}
					else {
						estado = "llenarpaciente";
					}
				}
				else if (fechaNacimientoTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Fecha de Nacimiento no valida", new ActionMessage("errors.fechaPosteriorIgualActual","de Nacimiento ", "actual"));
					
					if (estado.equals("modificarpaciente")) {
						estado = "pacienteexiste";
					}
					else {
						estado = "llenarpaciente";
					}
				}
			}
			else {
				errores.add("El campo Fecha de Nacimiento es Requerido", new ActionMessage("errors.required","El campo fecha de Nacimiento"));
			}
		}
		
		return errores;
	}


	public int getAseguradora() {
		return aseguradora;
	}


	public void setAseguradora(int aseguradora) {
		this.aseguradora = aseguradora;
	}


	public int getCodigoBarrioResidencia() {
		return codigoBarrioResidencia;
	}


	public void setCodigoBarrioResidencia(int codigoBarrioResidencia) {
		this.codigoBarrioResidencia = codigoBarrioResidencia;
	}


	public int getCodigoEnfermedadNotificable() {
		return codigoEnfermedadNotificable;
	}


	public void setCodigoEnfermedadNotificable(int codigoEnfermedadNotificable) {
		this.codigoEnfermedadNotificable = codigoEnfermedadNotificable;
	}


	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}


	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}


	public boolean isDesplazado() {
		return desplazado;
	}


	public void setDesplazado(boolean desplazado) {
		this.desplazado = desplazado;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public int getEtnia() {
		return etnia;
	}


	public void setEtnia(int etnia) {
		this.etnia = etnia;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public int getGenero() {
		return genero;
	}


	public void setGenero(int genero) {
		this.genero = genero;
	}


	public String getMunicipioResidencia() {
		return municipioResidencia;
	}


	public void setMunicipioResidencia(String municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}


	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}


	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}


	public int getOcupacion() {
		return ocupacion;
	}


	public void setOcupacion(int ocupacion) {
		this.ocupacion = ocupacion;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public String getSegundoApellido() {
		return segundoApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}


	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}


	public String getTipoRegimen() {
		return tipoRegimen;
	}


	public void setTipoRegimen(String tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public int getEdad() {
		return edad;
	}


	public void setEdad(int edad) {
		this.edad = edad;
	}


	public String getLugarResidencia() {
		return lugarResidencia;
	}


	public void setLugarResidencia(String lugarResidencia) {
		this.lugarResidencia = lugarResidencia;
	}


	public String getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}


	public void setDepartamentoNacimiento(String departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}


	public String getEstadoCivil() {
		return estadoCivil;
	}


	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}


	public String getLugarNacimiento() {
		return lugarNacimiento;
	}


	public void setLugarNacimiento(String lugarNacimiento) {
		this.lugarNacimiento = lugarNacimiento;
	}


	public String getMunicipioNacimiento() {
		return municipioNacimiento;
	}


	public void setMunicipioNacimiento(String municipioNacimiento) {
		this.municipioNacimiento = municipioNacimiento;
	}


	public boolean isEstaVivo() {
		return estaVivo;
	}


	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}


	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}


	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}


	public int getCodigoPaciente() {
		return codigoPaciente;
	}


	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}


	public int getCodigoFicha() {
		return codigoFicha;
	}


	public void setCodigoFicha(int codigoFicha) {
		this.codigoFicha = codigoFicha;
	}


	public int getCodigoEnf() {
		return codigoEnf;
	}


	public void setCodigoEnf(int codigoEnf) {
		this.codigoEnf = codigoEnf;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public String getDepartamentoIdentifica() {
		return departamentoIdentifica;
	}


	public void setDepartamentoIdentifica(String departamentoIdentifica) {
		this.departamentoIdentifica = departamentoIdentifica;
	}


	public String getLugarIdentifica() {
		return lugarIdentifica;
	}


	public void setLugarIdentifica(String lugarIdentifica) {
		this.lugarIdentifica = lugarIdentifica;
	}


	public String getMunicipioIdentifica() {
		return municipioIdentifica;
	}


	public void setMunicipioIdentifica(String municipioIdentifica) {
		this.municipioIdentifica = municipioIdentifica;
	}


	public String getCodigoAseguradora() {
		return codigoAseguradora;
	}


	public void setCodigoAseguradora(String codigoAseguradora) {
		this.codigoAseguradora = codigoAseguradora;
	}


	public String getConvenio() {
		return convenio;
	}


	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}


	public int getTipoSangre() {
		return tipoSangre;
	}


	public void setTipoSangre(int tipoSangre) {
		this.tipoSangre = tipoSangre;
	}


	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}


	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}


	public boolean isModifico() {
		return modifico;
	}


	public void setModifico(boolean modifico) {
		this.modifico = modifico;
	}


	public int getValorBarrio() {
		return valorBarrio;
	}


	public void setValorBarrio(int valorBarrio) {
		this.valorBarrio = valorBarrio;
	}


	public String getCodigoDxLargo() {
		return codigoDxLargo;
	}


	public void setCodigoDxLargo(String codigoDxLargo) {
		this.codigoDxLargo = codigoDxLargo;
	}


	public String getPaisExpedicion() {
		return paisExpedicion;
	}


	public void setPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion = paisExpedicion;
	}


	public String getPaisNacimiento() {
		return paisNacimiento;
	}


	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}


	public String getPaisResidencia() {
		return paisResidencia;
	}


	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}
}
