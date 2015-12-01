package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

import util.adjuntos.DTOArchivoAdjunto;

/**
 * Dto que almacena la información de las notas aclaratorias asociadas a un ingreso
 * 
 * @author Ricardo Ruiz
 *
 *
 */
public class DtoNotaAclaratoria implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8330574218311399741L;

	/**
	 * Atributo que representa el id de la nota aclaratoria
	 */
	private long codigo;
	
	/**
	 * Atributo que representa la descripción de la nota 
	 */
	private String descripcion;
	
	/**
	 * Atributo que representa la fecha de la nota
	 */
	private Date fecha;
	
	
	/**
	 * Atributo que representa la hora de la nota
	 */
	private String hora;
	
	/**
	 * Atributo que representa el id del profesional
	 */
	private int codigoProfesional;
	
	/**
	 * Atributo que representa el nombre completo del profesional
	 */
	private String nombreCompletoProfesional;
	
	/**
	 * Atributo que representa el numero registro del profesional
	 */
	private String numeroRegistroProfesional;
	
	/**
	 * Atributo que representa las especialidades del profesional
	 */
	private String especialidadesProfesional;
	
	/**
	 * Atributo que representa la estructura de datos de los archivos adjuntos
	 */
	private ArrayList<DTOArchivoAdjunto> archivosAdjuntos = new ArrayList<DTOArchivoAdjunto>();
	
	/**
	 * Constructor de la clase
	 */
	public DtoNotaAclaratoria(){
		
	}
	
	/**
	 * Constructor de la clase
	 */
	public DtoNotaAclaratoria(long codigo, String descripcion, Date fecha, String hora, int codigoProfesional,
							String primerNombre, String segundoNombre, String primerApellido, String segundoApellido,
							String registroMedico){
		this.codigo=codigo;
		this.descripcion=descripcion;
		this.fecha=fecha;
		this.hora=hora;
		this.codigoProfesional=codigoProfesional;
		String nombreCompleto=primerNombre;
		if(segundoNombre != null){
			nombreCompleto=nombreCompleto+" "+segundoNombre;
		}
		nombreCompleto=nombreCompleto+" "+primerApellido;
		if(segundoApellido != null){
			nombreCompleto=nombreCompleto+" "+segundoApellido;
		}
		this.nombreCompletoProfesional=nombreCompleto;
		this.numeroRegistroProfesional=registroMedico;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the archivosAdjutos
	 */
	public ArrayList<DTOArchivoAdjunto> getArchivosAdjuntos() {
		return archivosAdjuntos;
	}

	/**
	 * @param archivosAdjutos the archivosAdjutos to set
	 */
	public void setArchivosAdjuntos(ArrayList<DTOArchivoAdjunto> archivosAdjuntos) {
		this.archivosAdjuntos = archivosAdjuntos;
	}
	
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo archivoAdjunto
	 * 
	 * @return  Retorna la variable archivoAdjunto
	 */
	public DTOArchivoAdjunto getArchivoAdjunto(int index) {
		if(index>=archivosAdjuntos.size())
		{
			for(int i=archivosAdjuntos.size(); i<=index; i++)
			{
				archivosAdjuntos.add(new DTOArchivoAdjunto());
			}
		}
		return archivosAdjuntos.get(index);
	}
	
	/**
	 * Obtiene el número de documentos adjuntos
	 * @return int con la cantidad de documentos
	 * 
	 */
	public int getNumeroDocumentosAdjuntos()
	{
		if(this.archivosAdjuntos==null)
		{
			return 0;
		}
		return this.archivosAdjuntos.size();	
	}

	/**
	 * @return the nombreCompletoProfesional
	 */
	public String getNombreCompletoProfesional() {
		return nombreCompletoProfesional;
	}

	/**
	 * @param nombreCompletoProfesional the nombreCompletoProfesional to set
	 */
	public void setNombreCompletoProfesional(String nombreCompletoProfesional) {
		this.nombreCompletoProfesional = nombreCompletoProfesional;
	}

	/**
	 * @return the numeroRegistroProfesional
	 */
	public String getNumeroRegistroProfesional() {
		return numeroRegistroProfesional;
	}

	/**
	 * @param numeroRegistroProfesional the numeroRegistroProfesional to set
	 */
	public void setNumeroRegistroProfesional(String numeroRegistroProfesional) {
		this.numeroRegistroProfesional = numeroRegistroProfesional;
	}

	/**
	 * @return the especialidadesProfesional
	 */
	public String getEspecialidadesProfesional() {
		return especialidadesProfesional;
	}

	/**
	 * @param especialidadesProfesional the especialidadesProfesional to set
	 */
	public void setEspecialidadesProfesional(String especialidadesProfesional) {
		this.especialidadesProfesional = especialidadesProfesional;
	}

	/**
	 * @return the codigoProfesional
	 */
	public int getCodigoProfesional() {
		return codigoProfesional;
	}

	/**
	 * @param codigoProfesional the codigoProfesional to set
	 */
	public void setCodigoProfesional(int codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}

	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	
}
