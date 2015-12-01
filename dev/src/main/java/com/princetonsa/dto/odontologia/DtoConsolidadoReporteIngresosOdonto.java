package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

public class DtoConsolidadoReporteIngresosOdonto implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el cosecutivo del centro 
	 * de atenci&oacute;n
	 */
	private int consecutivoCentroAtencion;
	
	/**
	 * Atributo que almacena el total de pacientes
	 * ingresados por centro de atencion.
	 */
	private int totalPacientesIngresados;
	
	/**
	 * Atributo que almacena el total de pacientes sin valoraciones 
	 * iniciales atendidas.
	 */
	private int totalPacientesSinValIni;
	
	/**
	 * Atributo que almacena el total de pacientes con valoraciones 
	 * iniciales atendidas.
	 */
	private int totalPacientesConValIni;
	
	/**
	 * Atributo que almacena el porcentaje de pacientes sin valoracion inicial
	 */
	private String porcentajePacientesSinValIni ="0";
	
	/**
	 * Atributo que almacena el porcentaje de pacientes con valoracion inicial
	 */
	private String porcentajePacientesConValIni ="0";
	
	/**
	 * Atributo que almacena el total de ingresos con presupuesto.
	 */
	private int totalPacientesConPresupuesto;
	
	/**
	 * Atributo que almacena el total de ingresos sin presupuesto.
	 */
	private int totalPacientesSinPresupuesto;
	
	/**
	 * Atributo que almacena el total del presupuesto
	 * por estado.
	 */
	private int totalPresupuestoPorEstado;
	
	/**
	 * Atributo que almacena el estado del presupuesto.
	 */
	private String estadoPresupuesto;
	
	/**
	 * Atributo que almacena el estado del presupuesto odontol&oacute;gico.
	 */
	private ArrayList<DtoConsolidadoReporteIngresosOdonto> listaTotalPresupuesto;
	
	/**
	 * Atributo que almacena el estado del presupuesto.
	 */
	private String estadoPresupuestoPrecontratado;
	
	/**
	 * Atributo que almacena el total del presupuesto
	 * por estado.
	 */
	private int totalPresupuestoPrecontratado;
	
	/**
	 * Atributo que almacena el nombre del estado del presupuesto.
	 */
	private String nombreEstadoPresupuesto;
	
	 /** Objeto jasper para el subreporte con el total del 
	  *  presupuesto por estado.  
	  */
    private JRDataSource dslistadoTotalPresPorEstado;
    
    /** Objeto jasper para el subreporte del total de presupuestos
     * por estado
     */
    private JRDataSource dsTotalPresupuestos;
    
    /**
     * permite obtener el nombre del estado del presupuesto.
     */
    private String ayudanteEstadoPresupuesto;
    
    /**
     * permite almacenar el total de pacientes ingresados con valini y sin valini.
     */
    private int ayudanteTotalPacientesValIni;
    
    /**
	 * Atributo que almacena el c&oacute;digo de un paciente.
	 */
	private int codigoPaciente;
	
	/**
	 * Atributo que permite ordenar segùn una prioridad los 
	 * estados de presupuesto en el consolidado del reporte.
	 */
	private int prioridadEstadoPresupuesto;
	
	/**
	 * 
	 */
	private String conSolicitudDcto;
	
	
	public static int PRIORIDAD_ESTADO_ACTIVO = 1;
	public static int PRIORIDAD_ESTADO_INACTIVO = 2;
	public static int PRIORIDAD_ESTADO_CONTRATADO_CONTRATADO = 5;
	public static int PRIORIDAD_ESTADO_SUSPENDIDO_TEMPORALMENTE = 6;
	public static int PRIORIDAD_ESTADO_CONTRATADO_TERMINADO = 7;
	public static int PRIORIDAD_ESTADO_CONTRATADO_CANCELADO = 8;
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estadoPresupuestoPrecontratado
	 * 
	 * @return  Retorna la variable estadoPresupuestoPrecontratado
	 */
	public String getEstadoPresupuestoPrecontratado() {
		return estadoPresupuestoPrecontratado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estadoPresupuestoPrecontratado
	 * 
	 * @param  valor para el atributo estadoPresupuestoPrecontratado 
	 */
	public void setEstadoPresupuestoPrecontratado(
			String estadoPresupuestoPrecontratado) {
		this.estadoPresupuestoPrecontratado = estadoPresupuestoPrecontratado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo listaTotalPresupuesto
	 * 
	 * @return  Retorna la variable listaTotalPresupuesto
	 */
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> getListaTotalPresupuesto() {
		return listaTotalPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo listaTotalPresupuesto
	 * 
	 * @param  valor para el atributo listaTotalPresupuesto 
	 */
	public void setListaTotalPresupuesto(
			ArrayList<DtoConsolidadoReporteIngresosOdonto> listaTotalPresupuesto) {
		this.listaTotalPresupuesto = listaTotalPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estadoPresupuesto
	 * 
	 * @return  Retorna la variable estadoPresupuesto
	 */
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estadoPresupuesto
	 * 
	 * @param  valor para el atributo estadoPresupuesto 
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
		
		if (!UtilidadTexto.isEmpty(estadoPresupuesto)) {
			this.ayudanteEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(estadoPresupuesto).toString();
		}
		
		if (this.estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoEstadoActivo)) {
			this.prioridadEstadoPresupuesto = PRIORIDAD_ESTADO_ACTIVO;
		}else if (this.estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoInactivo)) {
			this.prioridadEstadoPresupuesto = PRIORIDAD_ESTADO_INACTIVO;
		}else if (this.estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoContratado)) {
			this.prioridadEstadoPresupuesto = PRIORIDAD_ESTADO_CONTRATADO_CONTRATADO;
		}else if (this.estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente)) {
			this.prioridadEstadoPresupuesto = PRIORIDAD_ESTADO_SUSPENDIDO_TEMPORALMENTE;
		}else if (this.estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoTerminado)) {
			this.prioridadEstadoPresupuesto = PRIORIDAD_ESTADO_CONTRATADO_TERMINADO;
		}else if (this.estadoPresupuesto.equals(ConstantesIntegridadDominio.acronimoContratadoCancelado)) {
			this.prioridadEstadoPresupuesto = PRIORIDAD_ESTADO_CONTRATADO_CANCELADO;
		}
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo totalPresupuestoPorEstado
	 * 
	 * @return  Retorna la variable totalPresupuestoPorEstado
	 */
	public int getTotalPresupuestoPorEstado() {
		return totalPresupuestoPorEstado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo totalPresupuestoPorEstado
	 * 
	 * @param  valor para el atributo totalPresupuestoPorEstado 
	 */
	public void setTotalPresupuestoPorEstado(int totalPresupuestoPorEstado) {
		this.totalPresupuestoPorEstado = totalPresupuestoPorEstado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo consecutivoCentroAtencion
	 * 
	 * @return  Retorna la variable consecutivoCentroAtencion
	 */
	public int getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo consecutivoCentroAtencion
	 * 
	 * @param  valor para el atributo consecutivoCentroAtencion 
	 */
	public void setConsecutivoCentroAtencion(int consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo totalPacientesIngresados
	 * 
	 * @return  Retorna la variable totalPacientesIngresados
	 */
	public int getTotalPacientesIngresados() {
		return totalPacientesIngresados;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo totalPacientesIngresados
	 * 
	 * @param  valor para el atributo totalPacientesIngresados 
	 */
	public void setTotalPacientesIngresados(int totalPacientesIngresados) {
		this.totalPacientesIngresados = totalPacientesIngresados;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo totalPacientesSinValIni
	 * 
	 * @return  Retorna la variable totalPacientesSinValIni
	 */
	public int getTotalPacientesSinValIni() {
		return totalPacientesSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo totalPacientesSinValIni
	 * 
	 * @param  valor para el atributo totalPacientesSinValIni 
	 */
	public void setTotalPacientesSinValIni(int totalPacientesSinValIni) {
		this.totalPacientesSinValIni = totalPacientesSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo totalPacientesConValIni
	 * 
	 * @return  Retorna la variable totalPacientesConValIni
	 */
	public int getTotalPacientesConValIni() {
		return totalPacientesConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo totalPacientesConValIni
	 * 
	 * @param  valor para el atributo totalPacientesConValIni 
	 */
	public void setTotalPacientesConValIni(int totalPacientesConValIni) {
		this.totalPacientesConValIni = totalPacientesConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo porcentajePacientesSinValIni
	 * 
	 * @return  Retorna la variable porcentajePacientesSinValIni
	 */
	public String getPorcentajePacientesSinValIni() {
		
		if (totalPacientesIngresados > 0) {
			if (totalPacientesSinValIni > 0) {
				
				float temp1 = (float) totalPacientesSinValIni;
				float temp2 = (float) totalPacientesIngresados;
				
				double temp = (temp1/temp2)*100;
				
				this.porcentajePacientesSinValIni= UtilidadTexto.formatearValores(temp);
			}else{
				this.porcentajePacientesSinValIni= "0.00";
			}
		}else{
			this.porcentajePacientesSinValIni= "0.00";
		}
		
		return porcentajePacientesSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo porcentajePacientesSinValIni
	 * 
	 * @param  valor para el atributo porcentajePacientesSinValIni 
	 */
	public void setPorcentajePacientesSinValIni(String porcentajePacientesSinValIni) {
		this.porcentajePacientesSinValIni = porcentajePacientesSinValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo porcentajePacientesConValIni
	 * 
	 * @return  Retorna la variable porcentajePacientesConValIni
	 */
	public String getPorcentajePacientesConValIni() {
		
		if (totalPacientesIngresados > 0) {
			if (totalPacientesConValIni > 0) {
				
				float temp1 = (float) totalPacientesConValIni;
				float temp2 = (float) totalPacientesIngresados;
				
				double temp = (temp1/temp2)*100;
				
				this.porcentajePacientesConValIni= UtilidadTexto.formatearValores(temp);
			}else{
				this.porcentajePacientesConValIni= "0.00";
			}
		}else{
			this.porcentajePacientesConValIni= "0.00";
		}
		
		return porcentajePacientesConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo porcentajePacientesConValIni
	 * 
	 * @param  valor para el atributo porcentajePacientesConValIni 
	 */
	public void setPorcentajePacientesConValIni(String porcentajePacientesConValIni) {
		this.porcentajePacientesConValIni = porcentajePacientesConValIni;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo totalPacientesConPresupuesto
	 * 
	 * @return  Retorna la variable totalPacientesConPresupuesto
	 */
	public int getTotalPacientesConPresupuesto() {
		return totalPacientesConPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo totalPacientesConPresupuesto
	 * 
	 * @param  valor para el atributo totalPacientesConPresupuesto 
	 */
	public void setTotalPacientesConPresupuesto(int totalPacientesConPresupuesto) {
		this.totalPacientesConPresupuesto = totalPacientesConPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo totalPacientesSinPresupuesto
	 * 
	 * @return  Retorna la variable totalPacientesSinPresupuesto
	 */
	public int getTotalPacientesSinPresupuesto() {
		return totalPacientesSinPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo totalPacientesSinPresupuesto
	 * 
	 * @param  valor para el atributo totalPacientesSinPresupuesto 
	 */
	public void setTotalPacientesSinPresupuesto(int totalPacientesSinPresupuesto) {
		this.totalPacientesSinPresupuesto = totalPacientesSinPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo totalPresupuestoPrecontratado
	 * 
	 * @return  Retorna la variable totalPresupuestoPrecontratado
	 */
	public int getTotalPresupuestoPrecontratado() {
		return totalPresupuestoPrecontratado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo totalPresupuestoPrecontratado
	 * 
	 * @param  valor para el atributo totalPresupuestoPrecontratado 
	 */
	public void setTotalPresupuestoPrecontratado(int totalPresupuestoPrecontratado) {
		this.totalPresupuestoPrecontratado = totalPresupuestoPrecontratado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEstadoPresupuesto
	 * 
	 * @return  Retorna la variable nombreEstadoPresupuesto
	 */
	public String getNombreEstadoPresupuesto() {
		
		if (estadoPresupuesto != null) {
			nombreEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(estadoPresupuesto).toString();
		}
		
		return nombreEstadoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreEstadoPresupuesto
	 * 
	 * @param  valor para el atributo nombreEstadoPresupuesto 
	 */
	public void setNombreEstadoPresupuesto(String nombreEstadoPresupuesto) {
		this.nombreEstadoPresupuesto = nombreEstadoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dslistadoTotalPresPorEstado
	 * 
	 * @return  Retorna la variable dslistadoTotalPresPorEstado
	 */
	public JRDataSource getDslistadoTotalPresPorEstado() {
		return dslistadoTotalPresPorEstado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dslistadoTotalPresPorEstado
	 * 
	 * @param  valor para el atributo dslistadoTotalPresPorEstado 
	 */
	public void setDslistadoTotalPresPorEstado(
			JRDataSource dslistadoTotalPresPorEstado) {
		this.dslistadoTotalPresPorEstado = dslistadoTotalPresPorEstado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dsTotalPresupuestos
	 * 
	 * @return  Retorna la variable dsTotalPresupuestos
	 */
	public JRDataSource getDsTotalPresupuestos() {
		return dsTotalPresupuestos;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dsTotalPresupuestos
	 * 
	 * @param  valor para el atributo dsTotalPresupuestos 
	 */
	public void setDsTotalPresupuestos(JRDataSource dsTotalPresupuestos) {
		this.dsTotalPresupuestos = dsTotalPresupuestos;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo ayudanteEstadoPresupuesto
	 * 
	 * @return  Retorna la variable ayudanteEstadoPresupuesto
	 */
	public String getAyudanteEstadoPresupuesto() {
		return ayudanteEstadoPresupuesto;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo ayudanteEstadoPresupuesto
	 * 
	 * @param  valor para el atributo ayudanteEstadoPresupuesto 
	 */
	public void setAyudanteEstadoPresupuesto(String ayudanteEstadoPresupuesto) {
		this.ayudanteEstadoPresupuesto = ayudanteEstadoPresupuesto;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  ayudanteTotalPacientesValIni
	 *
	 * @return retorna la variable ayudanteTotalPacientesValIni
	 */
	public int getAyudanteTotalPacientesValIni() {
		return ayudanteTotalPacientesValIni;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo ayudanteTotalPacientesValIni
	 * @param ayudanteTotalPacientesValIni es el valor para el atributo ayudanteTotalPacientesValIni 
	 */
	public void setAyudanteTotalPacientesValIni(int ayudanteTotalPacientesValIni) {
		this.ayudanteTotalPacientesValIni = ayudanteTotalPacientesValIni;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  codigoPaciente
	 *
	 * @return retorna la variable codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo codigoPaciente
	 * @param codigoPaciente es el valor para el atributo codigoPaciente 
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  prioridadEstadoPresupuesto
	 *
	 * @return retorna la variable prioridadEstadoPresupuesto
	 */
	public int getPrioridadEstadoPresupuesto() {
		return prioridadEstadoPresupuesto;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo prioridadEstadoPresupuesto
	 * @param prioridadEstadoPresupuesto es el valor para el atributo prioridadEstadoPresupuesto 
	 */
	public void setPrioridadEstadoPresupuesto(int prioridadEstadoPresupuesto) {
		this.prioridadEstadoPresupuesto = prioridadEstadoPresupuesto;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  conSolicitudDcto
	 *
	 * @return retorna la variable conSolicitudDcto
	 */
	public String getConSolicitudDcto() {
		return conSolicitudDcto;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo conSolicitudDcto
	 * @param conSolicitudDcto es el valor para el atributo conSolicitudDcto 
	 */
	public void setConSolicitudDcto(String conSolicitudDcto) {
		this.conSolicitudDcto = conSolicitudDcto;
	}
}
