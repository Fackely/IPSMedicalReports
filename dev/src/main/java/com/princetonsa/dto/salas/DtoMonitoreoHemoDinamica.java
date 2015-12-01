package com.princetonsa.dto.salas;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * 
 * @author wilson
 *
 */
public class DtoMonitoreoHemoDinamica implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private int orden;
	
	/**
	 * 
	 */
	private boolean obligatorio;
	
	/**
	 * 
	 */
	private double valorMaximo;
	
	/**
	 * 
	 */
	private double valorMinimo;
	
	/**
	 * 
	 */
	private String formula;
	
	/**
	 * 
	 */
	private String tipoCampo;
	
	/**
	 * 
	 */
	private int numeroDecimales;
	 
	/**
	 * 
	 */
	private boolean permiteNegativos;
	
	/**
	 * 
	 */
	private String onchange;
	
	/**
	 * 
	 */
	private String onkeyup;
	
	/**
	 * 
	 */
	private String onkeydown;
	 
	/**
	 * 
	 */
	private String mayor;
	 
	/**
	 * 
	 */
	private String menor;
	
	/**
	 * 
	 */
	private int campoPadre;

	/**
	 * 
	 */
	private ArrayList<InfoDatosInt> opciones = new ArrayList<InfoDatosInt>();
	
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 * @param orden
	 * @param obligatorio
	 * @param valorMaximo
	 * @param valorMinimo
	 * @param formula
	 * @param tipoCampo
	 * @param numeroDecimales
	 * @param permiteNegativos
	 * @param onchange
	 * @param onkeyup
	 * @param onkeydown
	 * @param mayor
	 * @param menor
	 * @param seccionPadre
	 */
	public DtoMonitoreoHemoDinamica(int codigo, String nombre, int orden, boolean obligatorio, double valorMaximo, double valorMinimo, String formula, String tipoCampo, int numeroDecimales, boolean permiteNegativos, String onchange, String onkeyup, String onkeydown, String mayor, String menor, int campoPadre, ArrayList<InfoDatosInt> opciones) 
	{
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.orden = orden;
		this.obligatorio = obligatorio;
		this.valorMaximo = valorMaximo;
		this.valorMinimo = valorMinimo;
		this.formula = formula;
		this.tipoCampo = tipoCampo;
		this.numeroDecimales = numeroDecimales;
		this.permiteNegativos = permiteNegativos;
		this.onchange = onchange;
		this.onkeyup = onkeyup;
		this.onkeydown = onkeydown;
		this.mayor = mayor;
		this.menor = menor;
		this.campoPadre = campoPadre;
		this.opciones= opciones;
	}

	/**
	 * 
	 *
	 */
	public DtoMonitoreoHemoDinamica() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.orden = ConstantesBD.codigoNuncaValido;
		this.obligatorio = false;
		this.valorMaximo = ConstantesBD.codigoNuncaValido;
		this.valorMinimo = ConstantesBD.codigoNuncaValido;
		this.formula = "";
		this.tipoCampo = "";
		this.numeroDecimales = ConstantesBD.codigoNuncaValido;
		this.permiteNegativos = false;
		this.onchange = "";
		this.onkeyup = "";
		this.onkeydown = "";
		this.mayor = "";
		this.menor = "";
		this.campoPadre = ConstantesBD.codigoNuncaValido;
		this.opciones= new ArrayList<InfoDatosInt>();
	}

	/**
	 * 
	 * @return
	 */
	public boolean getEsNumerico()
	{
		if(this.numeroDecimales>=0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the mayor
	 */
	public String getMayor() {
		return mayor;
	}

	/**
	 * @param mayor the mayor to set
	 */
	public void setMayor(String mayor) {
		this.mayor = mayor;
	}

	/**
	 * @return the menor
	 */
	public String getMenor() {
		return menor;
	}

	/**
	 * @param menor the menor to set
	 */
	public void setMenor(String menor) {
		this.menor = menor;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the numeroDecimales
	 */
	public int getNumeroDecimales() {
		return numeroDecimales;
	}

	/**
	 * @param numeroDecimales the numeroDecimales to set
	 */
	public void setNumeroDecimales(int numeroDecimales) {
		this.numeroDecimales = numeroDecimales;
	}

	/**
	 * @return the obligatorio
	 */
	public boolean getObligatorio() {
		return obligatorio;
	}

	/**
	 * @param obligatorio the obligatorio to set
	 */
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	/**
	 * @return the onchange
	 */
	public String getOnchange() {
		return onchange;
	}

	/**
	 * @param onchange the onchange to set
	 */
	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	/**
	 * @return the onkeydown
	 */
	public String getOnkeydown() {
		return onkeydown;
	}

	/**
	 * @param onkeydown the onkeydown to set
	 */
	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	/**
	 * @return the onkeyup
	 */
	public String getOnkeyup() {
		return onkeyup;
	}

	/**
	 * @param onkeyup the onkeyup to set
	 */
	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return the permiteNegativos
	 */
	public boolean getPermiteNegativos() {
		return permiteNegativos;
	}

	/**
	 * @param permiteNegativos the permiteNegativos to set
	 */
	public void setPermiteNegativos(boolean permiteNegativos) {
		this.permiteNegativos = permiteNegativos;
	}

	/**
	 * @return the seccionPadre
	 */
	public int getCampoPadre() {
		return campoPadre;
	}

	/**
	 * @param seccionPadre the seccionPadre to set
	 */
	public void setCampoPadre(int campoPadre) {
		this.campoPadre = campoPadre;
	}

	/**
	 * @return the tipoCampo
	 */
	public String getTipoCampo() {
		return tipoCampo;
	}

	/**
	 * @param tipoCampo the tipoCampo to set
	 */
	public void setTipoCampo(String tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	/**
	 * @return the valorMaximo
	 */
	public double getValorMaximo() {
		return valorMaximo;
	}

	/**
	 * @param valorMaximo the valorMaximo to set
	 */
	public void setValorMaximo(double valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	/**
	 * @return the valorMinimo
	 */
	public double getValorMinimo() {
		return valorMinimo;
	}

	/**
	 * @param valorMinimo the valorMinimo to set
	 */
	public void setValorMinimo(double valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	/**
	 * @return the opciones
	 */
	public ArrayList<InfoDatosInt> getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones the opciones to set
	 */
	public void setOpciones(ArrayList<InfoDatosInt> opciones) {
		this.opciones = opciones;
	}

	
}
