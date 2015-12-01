package com.mercury.mundo.odontologia;

import java.util.ArrayList;

import com.princetonsa.mundo.UsuarioBasico;

public class TratamientoDienteEnSeccion 
{
	private int 		codigo;
	private int 		numeroDiente;
	private int 		codSeccionTratamientoInst;
	private String 		observaciones;
	private boolean 	enBD;
	private ArrayList	analisisDiente;
    private UsuarioBasico   medico;
    private String          fecha;
	
	public TratamientoDienteEnSeccion()
	{
		this.codigo=-1;
		this.numeroDiente=-1;
		this.codSeccionTratamientoInst=-1;
		this.observaciones="";
		this.enBD=false;
		this.analisisDiente=new ArrayList();
        this.medico=new UsuarioBasico();
        this.fecha="";
	}
	
	public int getCodigo()
	{
		return this.codigo;
	}
	
	public void setCodigo(int codigo)
	{
		this.codigo=codigo;
	}
	
	public int getNumeroDiente()
	{
		return this.numeroDiente;
	}
	
	public void setNumeroDiente(int numeroDiente)
	{
		this.numeroDiente=numeroDiente;
	}
	
	public int getCodSeccionTratamientoInst()
	{
		return this.codSeccionTratamientoInst;
	}
	
	public void setCodSeccionTratamientoInst(int codSeccionTratamientoInst)
	{
		this.codSeccionTratamientoInst=codSeccionTratamientoInst;
	}
	
	public String getObservaciones()
	{
		return this.observaciones;
	}
	
	public void setObservaciones(String observaciones)
	{
		this.observaciones=observaciones;
	}
	
	public boolean getEnBD()
	{
		return this.enBD;
	}
	
	public void setEnBD(boolean enBD)
	{
		this.enBD=enBD;
	}
	
	public int getNumAnalisisDiente()
	{
		return this.analisisDiente.size();
	}
	
	public void addAnalisisDiente(AnalisisTratamiento analisisDiente)
	{
			this.analisisDiente.add(analisisDiente);
	}
	
	public AnalisisTratamiento getAnalisisDiente(int i)
	{
		return (AnalisisTratamiento)this.analisisDiente.get(i);
	}
	
    public UsuarioBasico getMedico()
    {
        return this.medico;
    }
    
    public void setMedico(UsuarioBasico medico)
    {
        this.medico=medico;
    }
    
    public String getFecha()
    {
        return this.fecha;
    }
    
    public void setFecha(String fecha)
    {
        this.fecha=fecha;
    }
}
