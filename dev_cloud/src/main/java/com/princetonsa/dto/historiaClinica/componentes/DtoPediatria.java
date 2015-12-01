/*
 * Junio 12, 2008
 */
package com.princetonsa.dto.historiaClinica.componentes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.UsuarioBasico;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ValoresPorDefecto;


/**
 * Data Transfer Object: Usado para el manejo del componente pediatria
 * 
 * @author Sebastián Gómez R.
 *
 *Nota * Se contemplan las tablas
 *valoraciones_pediatricas - estados_nutricionales
 *val_valores_des_ped - val_des_pediatrico - tipos_des_ped
 *val_edades_alimentacion - edades_alimentacion
 */
public class DtoPediatria implements Serializable
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(DtoPediatria.class);
	
	
	private int codigoPaciente;
	private String numeroSolicitud;
	/**
	 * Edad en años
	 */
	private int edadPaciente;
	
	private ArrayList<DtoDesarrolloPediatria> desarrollos;
	private ArrayList<DtoObservacionesPediatria> observaciones;
	private ArrayList<DtoEdadAlimentacionPediatria> edadesAlimentacion;
	
	//Arreglos de opciones
	private ArrayList<HashMap<String, Object>> estadosNutricionales;
	
	private InfoDatosInt estadoNutricional;
	private Boolean lactanciaMaterna;
	private Boolean otrasLeches;
	private Boolean alimentacionComplementaria;
	private String descripcionOtrasLeches;
	private String descripcionAlimentacionComplementaria;
	
	/**
	 * Se limpian los datos 
	 *
	 */
	public void clean()
	{
		this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		this.edadPaciente = 0;
		this.numeroSolicitud = "";
		this.desarrollos = null;
		this.observaciones = null;
		this.edadesAlimentacion = null;
		this.estadosNutricionales = null;
		
		
		this.estadoNutricional = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.lactanciaMaterna = null;
		this.otrasLeches = null;
		this.alimentacionComplementaria = null;
		this.descripcionOtrasLeches = "";
		this.descripcionAlimentacionComplementaria = "";
	}
	
	/**
	 * Cosntructor
	 *
	 */
	public DtoPediatria()
	{
		this.clean();
	}

	/**
	 * @return the alimentacionComplementaria
	 */
	public Boolean getAlimentacionComplementaria() {
		return alimentacionComplementaria;
	}

	/**
	 * @param alimentacionComplementaria the alimentacionComplementaria to set
	 */
	public void setAlimentacionComplementaria(Boolean alimentacionComplementaria) {
		this.alimentacionComplementaria = alimentacionComplementaria;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the desarrollos
	 */
	public ArrayList<DtoDesarrolloPediatria> getDesarrollos() {
		if(desarrollos == null)
		{
			desarrollos = new ArrayList<DtoDesarrolloPediatria>();
		}
		return desarrollos;
	}

	/**
	 * @param desarrollos the desarrollos to set
	 */
	public void setDesarrollos(ArrayList<DtoDesarrolloPediatria> desarrollos) {
		this.desarrollos = desarrollos;
	}

	/**
	 * @return the descripcionAlimentacionComplementaria
	 */
	public String getDescripcionAlimentacionComplementaria() {
		return descripcionAlimentacionComplementaria;
	}

	/**
	 * @param descripcionAlimentacionComplementaria the descripcionAlimentacionComplementaria to set
	 */
	public void setDescripcionAlimentacionComplementaria(
			String descripcionAlimentacionComplementaria) {
		this.descripcionAlimentacionComplementaria = descripcionAlimentacionComplementaria;
	}

	/**
	 * @return the descripcionOtrasLeches
	 */
	public String getDescripcionOtrasLeches() {
		return descripcionOtrasLeches;
	}

	/**
	 * @param descripcionOtrasLeches the descripcionOtrasLeches to set
	 */
	public void setDescripcionOtrasLeches(String descripcionOtrasLeches) {
		this.descripcionOtrasLeches = descripcionOtrasLeches;
	}

	/**
	 * @return the edadesAlimentacion
	 */
	public ArrayList<DtoEdadAlimentacionPediatria> getEdadesAlimentacion() {
		if(edadesAlimentacion == null)
		{
			edadesAlimentacion = new ArrayList<DtoEdadAlimentacionPediatria>();
		}
		return edadesAlimentacion;
	}

	/**
	 * @param edadesAlimentacion the edadesAlimentacion to set
	 */
	public void setEdadesAlimentacion(
			ArrayList<DtoEdadAlimentacionPediatria> edadesAlimentacion) {
		this.edadesAlimentacion = edadesAlimentacion;
	}

	/**
	 * @return the estadoNutricional
	 */
	public int getCodigoEstadoNutricional() {
		return estadoNutricional.getCodigo();
	}

	/**
	 * @param estadoNutricional the estadoNutricional to set
	 */
	public void setCodigoEstadoNutricional(int estadoNutricional) {
		this.estadoNutricional.setCodigo(estadoNutricional);
	}
	
	/**
	 * @return the estadoNutricional
	 */
	public String getNombreEstadoNutricional() {
		return estadoNutricional.getNombre();
	}

	/**
	 * @param estadoNutricional the estadoNutricional to set
	 */
	public void setNombreEstadoNutricional(String estadoNutricional) {
		this.estadoNutricional.setNombre(estadoNutricional);
	}

	/**
	 * @return the estadosNutricionales
	 */
	public ArrayList<HashMap<String, Object>> getEstadosNutricionales() {
		if(estadosNutricionales == null)
		{
			estadosNutricionales = new ArrayList<HashMap<String,Object>>();
		}
		return estadosNutricionales;
	}

	/**
	 * @param estadosNutricionales the estadosNutricionales to set
	 */
	public void setEstadosNutricionales(
			ArrayList<HashMap<String, Object>> estadosNutricionales) {
		this.estadosNutricionales = estadosNutricionales;
	}

	/**
	 * @return the lactanciaMaterna
	 */
	public Boolean getLactanciaMaterna() {
		return lactanciaMaterna;
	}

	/**
	 * @param lactanciaMaterna the lactanciaMaterna to set
	 */
	public void setLactanciaMaterna(Boolean lactanciaMaterna) {
		this.lactanciaMaterna = lactanciaMaterna;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the observaciones
	 */
	public ArrayList<DtoObservacionesPediatria> getObservaciones() {
		if(observaciones == null)
		{
			observaciones = new ArrayList<DtoObservacionesPediatria>();
		}
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(ArrayList<DtoObservacionesPediatria> observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the otrasLeches
	 */
	public Boolean getOtrasLeches() {
		return otrasLeches;
	}

	/**
	 * @param otrasLeches the otrasLeches to set
	 */
	public void setOtrasLeches(Boolean otrasLeches) {
		this.otrasLeches = otrasLeches;
	}

	/**
	 * @return the edadPaciente
	 */
	public int getEdadPaciente() {
		return edadPaciente;
	}

	/**
	 * @param edadPaciente the edadPaciente to set
	 */
	public void setEdadPaciente(int edadPaciente) {
		this.edadPaciente = edadPaciente;
	}
	
	/**
	 * Método para preparar nuevas observaciones
	 * @param usuario
	 */
	public void prepararNuevasObservaciones(UsuarioBasico usuario)
	{
		//Se añade nueva observacion de desarrollo
		DtoObservacionesPediatria observacionDesarrollo = new DtoObservacionesPediatria();
		observacionDesarrollo.setCodigoTipo(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo);
		observacionDesarrollo.setNombreTipo(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo).toString());
		observacionDesarrollo.setUsuario(usuario);
		
		this.getObservaciones().add(observacionDesarrollo);
		
		//Se añade nueva observacion de sueños y hábitos
		DtoObservacionesPediatria observacionSuenioHabitos = new DtoObservacionesPediatria();
		observacionSuenioHabitos.setCodigoTipo(ConstantesIntegridadDominio.acronimoSueniosHabitos);
		observacionSuenioHabitos.setNombreTipo(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoSueniosHabitos).toString());
		observacionSuenioHabitos.setUsuario(usuario);
		
		this.getObservaciones().add(observacionSuenioHabitos);
		
		//Si el paciente es menor a 2 años se cargan observaciones diferentes
		if(this.edadPaciente<2)
		{
			//Se añade nueva observacion de valoracion nutricional de menor 2 años
			DtoObservacionesPediatria observacionValNutricional = new DtoObservacionesPediatria();
			observacionValNutricional.setCodigoTipo(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios);
			observacionValNutricional.setNombreTipo(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios).toString());
			observacionValNutricional.setUsuario(usuario);
			
			this.getObservaciones().add(observacionValNutricional);
		}
		else
		{
			//Se añade nueva observacion de tipo alimentacion
			DtoObservacionesPediatria observacionTipoAlimentacion = new DtoObservacionesPediatria();
			observacionTipoAlimentacion.setCodigoTipo(ConstantesIntegridadDominio.acronimoTipoAlimentacion);
			observacionTipoAlimentacion.setNombreTipo(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAlimentacion).toString());
			observacionTipoAlimentacion.setUsuario(usuario);
			
			this.getObservaciones().add(observacionTipoAlimentacion);
			
			//Se añade nueva observacion de valoracion nutricional de mayor 2 años
			DtoObservacionesPediatria observacionValNutricional = new DtoObservacionesPediatria();
			observacionValNutricional.setCodigoTipo(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios);
			observacionValNutricional.setNombreTipo(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios).toString());
			observacionValNutricional.setUsuario(usuario);
			
			this.getObservaciones().add(observacionValNutricional);
		}
		
	}
	
	/**
	 * Método para verificar si se ingresó información de pediatría
	 * @return
	 */
	public boolean ingresoInformacion()
	{
		boolean ingreso = false;
		
		//logger.info("1) ¿Ingresó informacion? "+ingreso);
		
		for(DtoDesarrolloPediatria desarrollo:this.getDesarrollos())
			//Se debe verificar que no exista en la base de datos y que se haya ingresado información
			if(!desarrollo.isExisteBD()&&!desarrollo.getEdadTexto().trim().equals(""))
				ingreso = true;
		
		//logger.info("2) ¿Ingresó informacion? "+ingreso);
		
		for(DtoObservacionesPediatria observaciones:this.getObservaciones())
			//Se debe verificar que no exista en la base de datos y que se haya ingresado informacion
			if(observaciones.getConsecutivo().equals("")&&!observaciones.getValor().trim().equals(""))
				ingreso = true;
		
		//logger.info("3) ¿Ingresó informacion? "+ingreso);
		
		for(DtoEdadAlimentacionPediatria edad:this.getEdadesAlimentacion())
			//Se debe verificar que no exista en la base de datos y que se haya ingresado informacion
			if(!edad.isExisteBD()&&!edad.getEdadTexto().trim().equals(""))
				ingreso = true;
		
		//logger.info("4) ¿Ingresó informacion? "+ingreso);
		
		if(
				this.lactanciaMaterna!=null||
				this.otrasLeches!=null||
				this.alimentacionComplementaria!=null||
				!this.descripcionOtrasLeches.trim().equals("")||
				!this.descripcionAlimentacionComplementaria.trim().equals("")||
				this.getCodigoEstadoNutricional()>0
			)
			ingreso = true;
		
		//logger.info("5) ¿Ingresó informacion? "+ingreso);
		//logger.info("Valor de el codigo estado nutricional=> "+this.getCodigoEstadoNutricional());
		
		return ingreso;
	}
	
	/**
	 * Método que verifica si un componente está cargado
	 * @return
	 */
	public boolean estaCargado()
	{
		boolean cargado = false;
		
		
		for(DtoDesarrolloPediatria desarrollo:this.getDesarrollos())
			if(!desarrollo.getEdadTexto().trim().equals(""))
				cargado = true;
		
		
		for(DtoObservacionesPediatria observaciones:this.getObservaciones())
			if(!observaciones.getValor().trim().equals(""))
				cargado = true;
		
		
		for(DtoEdadAlimentacionPediatria edad:this.getEdadesAlimentacion())
			if(!edad.getEdadTexto().trim().equals(""))
				cargado = true;
		
		
		if(
				this.lactanciaMaterna!=null||
				this.otrasLeches!=null||
				this.alimentacionComplementaria!=null||
				!this.descripcionOtrasLeches.trim().equals("")||
				!this.descripcionAlimentacionComplementaria.trim().equals("")||
				this.getCodigoEstadoNutricional()>0
			)
			cargado = true;
		
		return cargado;
	}
	
	/**
	 * Método que verifica si existe desarrollo psicomotor
	 * @return
	 */
	public boolean isExisteDesarrolloPsicomotor()
	{
		boolean existe = false;
		
		for(DtoDesarrolloPediatria desarrollo:this.getDesarrollos())
			if(desarrollo.getCodigoTipo()==ConstantesBD.codigoTipoDesarrolloPsicomotor&&
				!desarrollo.getEdadTexto().trim().equals(""))
				existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe desarrollo lenguaje
	 * @return
	 */
	public boolean isExisteDesarrolloLenguaje()
	{
		boolean existe = false;
		
		for(DtoDesarrolloPediatria desarrollo:this.getDesarrollos())
			if(desarrollo.getCodigoTipo()==ConstantesBD.codigoTipoDesarrolloLenguaje&&
				!desarrollo.getEdadTexto().trim().equals(""))
				existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existen observaciones desarrollo
	 * @return
	 */
	public boolean isExistenObservacionesDesarrollo()
	{
		boolean existe = false;
		
		for(DtoObservacionesPediatria observacion:this.getObservaciones())
			if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo)&&
				!observacion.getValor().trim().equals(""))
				existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existen sueños y hábitos
	 * @return
	 */
	public boolean isExistenSueniosHabitos()
	{
		boolean existe = false;
		
		for(DtoObservacionesPediatria observacion:this.getObservaciones())
			if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoSueniosHabitos)&&
				!observacion.getValor().trim().equals(""))
				existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe valoracion nutricional
	 * @return
	 */
	public boolean isExisteValoracionNutricional()
	{
		boolean existe = false;
		
		if(this.edadPaciente<2)
		{
			if(this.isExisteLactanciaMaterna())
				existe = true;
			if(this.isExisteOtrasLeches())
				existe = true;
			if(this.isExisteAlimentacionComplementaria())
				existe = true;
		}
		else
		{
			for(DtoEdadAlimentacionPediatria edad:this.getEdadesAlimentacion())
				if(!edad.getEdadTexto().trim().equals(""))
					existe = true;
		}
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe lactancia materna
	 * @return
	 */
	public boolean isExisteLactanciaMaterna()
	{
		boolean existe = false;
		
		if(this.lactanciaMaterna!=null)
			existe = true;
		
		return existe;
	}
	
	
	/**
	 * Método que verifica si existe otras leches
	 * @return
	 */
	public boolean isExisteOtrasLeches()
	{
		boolean existe = false;
		
		if(this.otrasLeches!=null||!this.descripcionOtrasLeches.trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe alimentacion complementaria
	 * @return
	 */
	public boolean isExisteAlimentacionComplementaria()
	{
		boolean existe = false;
		
		if(this.alimentacionComplementaria!=null||!this.descripcionAlimentacionComplementaria.trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existen observaciones de valoración nutrticional menor a 2 años
	 * @return
	 */
	public boolean isExisteObservacionValNutricionalMenor2Anios()
	{
		boolean existe = false;
		
		for(DtoObservacionesPediatria observacion:this.getObservaciones())
			if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios)&&
				!observacion.getValor().trim().equals(""))
				existe = true;
		
		return existe;
	}
	
	
	/**
	 * Método que verifica si existe tipo de alimentacion
	 * @return
	 */
	public boolean isExisteTipoAlimentacion()
	{
		boolean existe = false;
		
		for(DtoObservacionesPediatria observacion:this.getObservaciones())
			if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoTipoAlimentacion)&&
				!observacion.getValor().trim().equals(""))
				existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existen observaciones de valoración nutrticional mayor a 2 años
	 * @return
	 */
	public boolean isExisteObservacionValNutricionalMayor2Anios()
	{
		boolean existe = false;
		
		for(DtoObservacionesPediatria observacion:this.getObservaciones())
			if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios)&&
				!observacion.getValor().trim().equals(""))
				existe = true;
		
		return existe;
	}
	
	
}