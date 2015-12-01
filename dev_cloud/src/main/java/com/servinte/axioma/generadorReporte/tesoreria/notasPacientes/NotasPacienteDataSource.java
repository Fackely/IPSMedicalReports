package com.servinte.axioma.generadorReporte.tesoreria.notasPacientes;

import java.text.NumberFormat;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.ValoresPorDefecto;
import util.reportes.dinamico.DataSource;

import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;

public class NotasPacienteDataSource {

	
	/** * Contiene los mensajes correspondiente a este Generador*/
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.NotasPacientesForm");
	
	/**
	 * Atributo que representa un espacio
	 */
	private static final String espacio=" ";
	
	/**
	 * Atributo que representa 4 espacio 
	 */
	private static final String espacioLargo="    ";
	
	
	/**
	 * Atributo que representa la lista de notas pacientes a la cual se le obtiene
	 * el datasource
	 */
	private ArrayList<DtoResumenNotasPaciente> notasPacientes;
	

	/**
	 * Constructor de la clase
	*/
	public NotasPacienteDataSource() {
		
	}
	
	/**
	 * Constructor de la clase para los reportes PDF, Excel
	 * @param notasPacientes
	 */
	public NotasPacienteDataSource(ArrayList<DtoResumenNotasPaciente> notasPacientes) {
		this.setNotasPacientes(notasPacientes);
	}
	
	/**
	 * Método encargado de crear el datasource del consolidado de Notas Pacientes
	 * por Rango para el formato A
	 * @return
	 */
	public JRDataSource crearDataSourceNotaPacienteFormatoA() {
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(2);
		DataSource dataSource = new DataSource("nroNota", "fechaHora", 
										"centroAtenciOnrigen", "paciente", "idPaciente", 
										"centroAtencionDuenio", "ingreso", "concepto", "valorNota");
		
		for (DtoResumenNotasPaciente notaPaciente : getNotasPacientes()) {
			dataSource.add(notaPaciente.getNroNotaPaciente().toString(),
						   notaPaciente.getFechaHora(),
						   notaPaciente.getNombreCentroAtencionOrigen(),
						   notaPaciente.getNombreCompletoPaciente(),
						   notaPaciente.getTipoIdentificacion() + " " + notaPaciente.getNumeroIdentificacion(),
						   notaPaciente.getNombreCentroAtencionByCentroAtencionDuenio(),
						   notaPaciente.getIngresos(),
						   notaPaciente.getDescripcionConcepto(),
						   notaPaciente.getValorNota());
		}
		return dataSource;
	}

	/**
	 * Método encargado de crear el datasource del consolidado de Notas Pacientes
	 * por Rango para el formato A
	 * @return
	 */
	public JRDataSource crearDataSourceNotaPacienteConsolidadoPlano() {
		NumberFormat nf = NumberFormat.getInstance();
	    nf.setMaximumFractionDigits(2);
		DataSource dataSource = new DataSource("institucion", "naturaleza", "nroNota", "fechaHora", 
										"centroAtenciOnrigen", "paciente", "idPaciente", 
										"centroAtencionDuenio", "ingreso", "concepto", "valorNota");
		
		for (DtoResumenNotasPaciente notaPaciente : getNotasPacientes()) {
			dataSource.add(notaPaciente.getNombreEmpresaInstitucion(),
						   (String)ValoresPorDefecto.getIntegridadDominio(notaPaciente.getNaturalezaNotaPaciente()),
						   notaPaciente.getNroNotaPaciente().toString(),
						   notaPaciente.getFechaHora(),
						   notaPaciente.getNombreCentroAtencionOrigen(),
						   notaPaciente.getNombreCompletoPaciente(),
						   notaPaciente.getTipoIdentificacion() + " " + notaPaciente.getNumeroIdentificacion(),
						   notaPaciente.getNombreCentroAtencionByCentroAtencionDuenio(),
						   notaPaciente.getIngresos(),
						   notaPaciente.getDescripcionConcepto(),
						   notaPaciente.getValorNota());
		}
		return dataSource;
	}

	/**
	 * @param notasPacientes the notasPacientes to set
	 */
	public void setNotasPacientes(ArrayList<DtoResumenNotasPaciente> notasPacientes) {
		this.notasPacientes = notasPacientes;
	}

	/**
	 * @return the notasPacientes
	 */
	public ArrayList<DtoResumenNotasPaciente> getNotasPacientes() {
		return notasPacientes;
	}
	


}
