/*
 * @(#)Diagnostico.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * Esta clase guarda la información básica para identificar un diagnóstico dentro del sistema.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class Diagnostico implements Serializable{
	
	
	public static final int VALOR_INICIAL_ID_DIAGNOSTICO = -1;

	/**
	 * Version serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Código alfanumérico de este diagnóstico.
	 */
	private String acronimo;

	/**
	 * Nombre de este diagnóstico.
	 */
	private String nombre;

	/**
	 * El número consecutivo de este diagnóstico, dentro de un agregado de diagnósticos.
	 */
	private int numero;

	/**
	 * Indica si este diagnóstico es o no principal
	 */
	private boolean principal;
	
	/**
	 * Indica si este diagnóstico es o no definitivo
	 */
	private boolean definitivo;

	/**
	 * Tipo de CIE  de este diagnóstico.
	 */
	private int tipoCIE;
	
	
	/**
	 * Campo para saber si el diagnóstico fue seleccionado o no
	 */
	private boolean activo;
	
	/**
	 * Valor de la ficha epidemiológica
	 */
	private String valorFicha;
	
	/**
	 * Campo (OPCIONAL) que maneja el tipo de diagnostico
	 */
	private InfoDatosInt tipoDiagnostico;
	
	/**
	 * Variable que indica si el diagnóstico es de complicacion
	 */
	private boolean complicacion;

	/**
	 * Crea un nuevo objeto <code>Diagnostico</code> .
	 */
	public Diagnostico() {
		this.acronimo = "";
		this.nombre = "";
		this.numero = Diagnostico.VALOR_INICIAL_ID_DIAGNOSTICO;
		this.principal = false;
		this.definitivo = true;
		this.complicacion = false;
		this.tipoCIE = Diagnostico.VALOR_INICIAL_ID_DIAGNOSTICO;
		
		this.activo = false;
		
		this.valorFicha = "";
		this.tipoDiagnostico = new InfoDatosInt();
	}

	/**
	 * Crea un nuevo objeto <code>Diagnostico</code> .
	 */
	public Diagnostico(String diagnostico, int tipoCie, boolean principal) {
		this.acronimo = diagnostico;
		this.nombre = "";
		this.numero = Diagnostico.VALOR_INICIAL_ID_DIAGNOSTICO;
		this.principal = principal;
		this.tipoCIE = tipoCie;
		this.activo = false;
		
	}

	/**
	 * @param acronimoDiagnosticoNoSeleccionado
	 * @param codigoCieDiagnosticoNoSeleccionado
	 */
	public Diagnostico(String acronimo, int tipoCie)
	{
		this.acronimo=acronimo;
		this.tipoCIE=tipoCie;
		this.nombre = "";
		this.activo = false;
	}

	/**
	 * Indica si este diagnóstico es o no principal
	 * @return <b>true</b> si este diagnóstico es principa, <b>false</b> si no.
	 */
	public boolean isPrincipal() {
		return principal;
	}

	/**
	 * Retorna el número de este diagnóstico.
	 * @return el número de este diagnóstico
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * Establece si este diagnóstico es o no principal. 
	 * @param principal dice si este diagnóstico es o no principal
	 */
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	/**
	 * Establece el número consecutivo de este diagnóstico.
	 * @param numero el número consecutivo de este diagnóstico a establecer
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return
	 */
	public int getTipoCIE() {
		return tipoCIE;
	}

	/**
	 * @param string
	 */
	public void setAcronimo(String string) {
		acronimo = string;
	}

	/**
	 * @param string
	 */
	public void setNombre(String string) {
		nombre = string;
	}

	/**
	 * @param i
	 */
	public void setTipoCIE(int i) {
		tipoCIE = i;
	}

	/**
	 * @return the definitivo
	 */
	public boolean isDefinitivo() {
		return definitivo;
	}

	/**
	 * @param definitivo the definitivo to set
	 */
	public void setDefinitivo(boolean definitivo) {
		this.definitivo = definitivo;
	}

	/**
	 * @return the valor
	 */
	public String getValor() 
	{
		String valor = "";
		if(!this.acronimo.equals("")&&this.tipoCIE!=ConstantesBD.codigoNuncaValido&&!this.nombre.equals(""))
			valor = this.acronimo + ConstantesBD.separadorSplit + this.tipoCIE + ConstantesBD.separadorSplit + this.nombre;
		
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		//Adicionalmente se asignan los valores respectivos
		String[] vectorValor = valor.split(ConstantesBD.separadorSplit);
		if(vectorValor.length>2)
		{
			this.acronimo = vectorValor[0];
			this.tipoCIE = Integer.parseInt(vectorValor[1]);
			this.nombre = vectorValor[2];
			
		}
	}

	/**
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the valorFicha
	 */
	public String getValorFicha() {
		return valorFicha;
	}

	/**
	 * @param valorFicha the valorFicha to set
	 */
	public void setValorFicha(String valorFicha) {
		this.valorFicha = valorFicha;
	}

	/**
	 * @return the tipoDiagnostico
	 */
	public int getCodigoTipoDiagnostico() {
		return tipoDiagnostico.getCodigo();
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setCodigoTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico.setCodigo(tipoDiagnostico);
	}
	
	/**
	 * @return the tipoDiagnostico
	 */
	public String getNombreTipoDiagnostico() {
		return tipoDiagnostico.getNombre();
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setNombreTipoDiagnostico(String tipoDiagnostico) {
		this.tipoDiagnostico.setNombre(tipoDiagnostico);
	}

	/**
	 * @return the complicacion
	 */
	public boolean isComplicacion() {
		return complicacion;
	}

	/**
	 * @param complicacion the complicacion to set
	 */
	public void setComplicacion(boolean complicacion) {
		this.complicacion = complicacion;
	}

}