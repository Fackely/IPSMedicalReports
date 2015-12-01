package util.adjuntos;

import java.io.Serializable;
import java.util.Date;


/**
 * Esta clase se encarga de transportar la informaci&oacute;n
 * de los documentos adjuntos del sistema.
 *
 * @author Juan David Ram&iacute;rez, Yennifer Guerrero
 * @since 13 Agosto 2010
 *
 */
@SuppressWarnings("serial")
public class DTOArchivoAdjunto implements Serializable{

	/**
	 * Nombre original del documento adjunto
	 */
	private String nombreOriginal;
	
	/**
	 * Nombre generado por el sistema para almacenarlo en el file system
	 */
	private String nombreGenerado;

	/**
	 * Path donde se almacena el documento
	 */
	private String path;

	/**
	 * Indica si se va a almacenar o no en la base de datos
	 */
	private boolean activo;
	
	/**
	 * Fecha en que fue adjuntado el archivo
	 */
	private Date fecha;
	
	/**
	 * Hora en que fue adjuntado el archivo
	 */
	private String hora;


	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 * @author Yennifer Guerrero
	 */
	public DTOArchivoAdjunto(){
		
	}
	
	/**
	 * M&eacute;todo constructor de la clase
	 * @param nombreOriginal Nombre original del documento
	 * @param nombreGenerado Nombre generado por el sistema
	 * @param path Path del parchivo
	 * @author Yennifer Guerrero
	 */
	public DTOArchivoAdjunto(String nombreOriginal, String nombreGenerado, String path) {
		this.nombreOriginal=nombreOriginal;
		this.nombreGenerado=nombreGenerado;
		this.path=path;
	}
	
	/**
	 * Método constructor de la clase
	 * @param nombreOriginal
	 * @param nombreGenerado
	 * @param fechaRegistro
	 * @param horaRegistro
	 * @author Ricardo Ruiz
	 */
	public DTOArchivoAdjunto(String nombreOriginal, String nombreGenerado, Date fechaRegistro, String horaRegistro) {
		this.nombreOriginal=nombreOriginal;
		this.nombreGenerado=nombreGenerado;
		this.fecha=fechaRegistro;
		this.hora=horaRegistro;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreOriginal
	 * 
	 * @return  Retorna la variable nombreOriginal
	 */
	public String getNombreOriginal() {
		return nombreOriginal;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreGenerado
	 * 
	 * @return  Retorna la variable nombreGenerado
	 */
	public String getNombreGenerado() {
		return nombreGenerado;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreOriginal
	 * 
	 * @param  valor para el atributo nombreOriginal 
	 */
	public void setNombreOriginal(String nombreOriginal) {
		this.nombreOriginal = nombreOriginal;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreGenerado
	 * 
	 * @param  valor para el atributo nombreGenerado 
	 */
	public void setNombreGenerado(String nombreGenerado) {
		this.nombreGenerado = nombreGenerado;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo activo
	 * 
	 * @return  Retorna la variable activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo activo
	 * 
	 * @param  valor para el atributo activo 
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo path
	 * 
	 * @return  Retorna la variable path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo path
	 * 
	 * @param  valor para el atributo path 
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fecha
	 * 
	 * @return  Retorna la variable fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo hora
	 * 
	 * @return  Retorna la variable hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fecha
	 * 
	 * @param  valor para el atributo fecha 
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo hora
	 * 
	 * @param  valor para el atributo hora 
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
}
